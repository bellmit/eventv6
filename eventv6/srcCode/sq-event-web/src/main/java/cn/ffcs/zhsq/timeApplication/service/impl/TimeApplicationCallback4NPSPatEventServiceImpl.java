package cn.ffcs.zhsq.timeApplication.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.zzgl.service.holidayinfo.IHolidayInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCallbackService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationCheckService;
import cn.ffcs.zhsq.timeApplication.ITimeApplicationService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

/**
 * 南平市随手拍时限申请回调
 * @author zhangls
 *
 */
@Service(value = "timeApplicationCallback4NPSPatEventService")
public class TimeApplicationCallback4NPSPatEventServiceImpl implements
		ITimeApplicationCallbackService {
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Resource(name = "eventWechat4NPSPatService")
    private IEventVerifyBaseService eventVerifyBaseService;
	
	@Resource(name = "timeApplication4NPSPatService")
	private ITimeApplicationService timeApplicationService;
	
	@Autowired
	private ITimeApplicationCheckService timeApplicationCheckService;
	
	@Autowired
	private IHolidayInfoService holidayInfoService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void timeApplicationCallback(Map<String, Object> params) {
		String timeAppCheckStaus = null, userOrgCode = null;
		
		if(CommonFunctions.isNotBlank(params, "timeAppCheckStatus")) {
			timeAppCheckStaus = params.get("timeAppCheckStatus").toString();
		}
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}

		//只有审核完成后，才需要进行后续的操作
		if(ITimeApplicationCheckService.CHECK_STATUS.STATUS_SUCCESS.getValue().equals(timeAppCheckStaus)) {
			Long applicationId = null, updaterId = null, checkId = null;
			int interval = 0;
			Date applicationDate = null;
			Map<String, Object> timeApplication = new HashMap<String, Object>();
			
			if(CommonFunctions.isNotBlank(params, "updaterId")) {
				updaterId = Long.valueOf(params.get("updaterId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "checkId")) {
				checkId = Long.valueOf(params.get("checkId").toString());
			}
			
			if(CommonFunctions.isNotBlank(params, "applicationId")) {
				applicationId = Long.valueOf(params.get("applicationId").toString());
				
				timeApplication = timeApplicationService.findTimeAppliationById(applicationId, null);
			}
			
			if(CommonFunctions.isNotBlank(timeApplication, "businessKeyId")) {
				Long eventId = Long.valueOf(timeApplication.get("businessKeyId").toString());
				EventDisposal event = new EventDisposal(), eventTmp = null;
				String applicationType = null;
				
				if(CommonFunctions.isNotBlank(timeApplication, "interval")) {
					interval = Integer.valueOf(timeApplication.get("interval").toString());
				}
				
				if(CommonFunctions.isNotBlank(timeApplication, "applicationType")) {
					applicationType = timeApplication.get("applicationType").toString();
				}
				
				eventTmp = eventDisposalService.findEventByIdSimple(eventId);
				
				//待办延时申请 时限为：原办结期限 + 申请天数
				if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TODO.getValue().equals(applicationType)) {
					
					if(eventTmp != null) {
						applicationDate = eventTmp.getHandleDate();
					}
				
					if(applicationDate != null && interval > 0) {
						String intervalUnit = null;
						Date handleDate = null;
						
						if(CommonFunctions.isNotBlank(timeApplication, "intervalUnit")) {
							intervalUnit = timeApplication.get("intervalUnit").toString();
						}
						
						if(ITimeApplicationService.INTERVAL_UNIT.WEEK_DAY.toString().equals(intervalUnit)) {
							handleDate = holidayInfoService.getWorkDateByAfterWorkDay(applicationDate, interval);
						} else if(ITimeApplicationService.INTERVAL_UNIT.NATURAL_DAY.toString().equals(intervalUnit)) {
							handleDate = DateUtils.addInterval(applicationDate, interval, "00");
						}
						
						//调整事件办理时限及时限状态
						if(handleDate != null) {
							event.setEventId(eventId);
							event.setHandleDate(handleDate);
							event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
							
							eventDisposalService.updateEventDisposalById(event);
						}
					}
				} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_TYPE.getValue().equals(applicationType)) {
					if(eventTmp != null) {
						String eventTypePre = null,
							   eventType = eventTmp.getType();
						StringBuffer eventTypeAppRemark = new StringBuffer("");
						
						if(CommonFunctions.isNotBlank(timeApplication, "eventTypePre")) {
							eventTypePre = timeApplication.get("eventTypePre").toString();
						}
						
						if(eventType.equals(eventTypePre)) {
							String eventStatus = eventTmp.getStatus();
							
							if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
								logger.error("事件已归档，不可进行事件分类调整！");
								eventTypeAppRemark.append("事件已归档，不可进行事件分类调整！");
							} else {
								if(CommonFunctions.isNotBlank(timeApplication, "eventTypeAfter")) {
									//构建分类变更后调整事件办理时限，原事发时间+修改后的分类的办理时长，将办理时限状态设置为正常
									Date happenDate = eventTmp.getHappenTime(),
										 eventHandleDate = null;
									int intervalWeekDays = 0,	//工作日
										intervalNaturalDays = 0;//自然日
									String eventTypeAfter = timeApplication.get("eventTypeAfter").toString();
									
									if(StringUtils.isNotBlank(userOrgCode)) {
										String intervalDayStr = null;
										
										intervalDayStr = funConfigurationService.changeCodeToValue(ConstantValue.HANDLE_DATE_INTERVAL, ConstantValue.EVENT_HANDLE_DATE_INTERVAL + eventTypeAfter, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.DIRECTION_UP_FUZZY);
										
										if(StringUtils.isNotBlank(intervalDayStr)) {
											String intervalWeekdayStr = null,		//工作日
												   intervalNaturalDayStr = null,	//自然日
												   WEEK_DAY_INTERVAL = "w",			//工作日间隔类型
												   NATURAL_DAY_INTERVAL = "n";		//自然日间隔类型
											
											intervalDayStr = intervalDayStr.trim().toLowerCase();
											
											if(intervalDayStr.endsWith(WEEK_DAY_INTERVAL)) {
												intervalWeekdayStr = intervalDayStr.replace(WEEK_DAY_INTERVAL, "");
											} else if(intervalDayStr.endsWith(NATURAL_DAY_INTERVAL)) {
												intervalNaturalDayStr = intervalDayStr.replace(NATURAL_DAY_INTERVAL, "");
											} else {//默认为工作日
												intervalWeekdayStr = intervalDayStr;
											}
											
											if(StringUtils.isNotBlank(intervalWeekdayStr)) {
												try {
													intervalWeekDays = Integer.valueOf(intervalWeekdayStr);
												} catch(NumberFormatException e) {
													e.printStackTrace();
												}
											} else if(StringUtils.isNotBlank(intervalNaturalDayStr)) {
												try {
													intervalNaturalDays = Integer.valueOf(intervalNaturalDayStr);
												} catch(NumberFormatException e) {
													e.printStackTrace();
												}
											}
										} 
									}
									
									if(intervalWeekDays > 0) {
										eventHandleDate = holidayInfoService.getWorkDateByAfterWorkDay(happenDate, intervalWeekDays);
									} else if(intervalNaturalDays > 0) {
										eventHandleDate = DateUtils.addInterval(happenDate, intervalNaturalDays, "00");
									}
									
									if(eventHandleDate != null) {
										event.setHandleDate(eventHandleDate);
										event.setHandleDateFlag(ConstantValue.LIMIT_TIME_STATUS_NORMAL);
									}
									
									event.setEventId(eventId);
									event.setType(eventTypeAfter);
									eventDisposalService.updateEventDisposalById(event);
								}
							}
						} else {
							Map<String, Object> dictMap = new HashMap<String, Object>(),
												eventTypeMap = null;
							List<BaseDataDict> eventTypeDictList = null;
							StringBuffer message = new StringBuffer("");
							String eventClassPre = "", eventClassNow = "";
							
							dictMap.put("orgCode", userOrgCode);
							dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
							
							eventTypeDictList = baseDictionaryService.findDataDictListByCodes(dictMap);
							
							eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventType, ConstantValue.BIG_TYPE_PCODE, eventTypeDictList);
							
							if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
								eventClassNow = eventTypeMap.get("dictFullPath").toString();
							}
							
							eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventTypePre, ConstantValue.BIG_TYPE_PCODE, eventTypeDictList);
							
							if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
								eventClassPre = eventTypeMap.get("dictFullPath").toString();
							}
							
							message.append("事件类别不一致，现有事件类别为【").append(eventClassNow).append("】，申请提交时事件类别为【").append(eventClassPre).append("】！");
							
							logger.error(message.toString());
							eventTypeAppRemark.append(message);
						}
						
						//调整事件分类审核记录的备注信息
						if(eventTypeAppRemark.length() > 0 && checkId != null && checkId > 0) {
							
							Map<String, Object> eventTypeAppMap = new HashMap<String, Object>();

							eventTypeAppMap.put("checkId", checkId);
							eventTypeAppMap.put("updaterId", updaterId);
							eventTypeAppMap.put("userOrgCode", userOrgCode);
							eventTypeAppMap.put("checkRemark", eventTypeAppRemark.toString());
							eventTypeAppMap.put("isCallback", false);
							
							try {
								timeApplicationCheckService.updateTimeAppCheckById(eventTypeAppMap);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
				} else if(ITimeApplicationService.APPLICATION_TYPE.EVENT_VERIFY.getValue().equals(applicationType)) {
					Long eventVerifyId = null;
					String eventVerifyStatus = null,
						   REPORT_STATUS = "02",
						   eventStatus = eventTmp.getStatus();
					StringBuffer eventVerifyAppRemark = new StringBuffer("");
					
					if(ConstantValue.EVENT_STATUS_END.equals(eventStatus)) {
						logger.error("事件已归档，不可进行事件重置！");
						eventVerifyAppRemark.append("事件已归档，不可进行事件重置！");
					} else {
						if(CommonFunctions.isNotBlank(timeApplication, "businessId")) {
							eventVerifyId = Long.valueOf(timeApplication.get("businessId").toString());
						}
						
						if(eventVerifyId != null && eventVerifyId > 0) {
							Map<String, Object> eventVerifyMap = eventVerifyBaseService.findEventVerifyById(eventVerifyId, null);
							
							if(CommonFunctions.isNotBlank(eventVerifyMap, "status")) {
								eventVerifyStatus = eventVerifyMap.get("status").toString();
							}
							
							if(REPORT_STATUS.equals(eventVerifyStatus)) {
								//将事件审核记录调整为待审核
								String DEFAULT_VALID_STATUS = "01";
								Map<String, Object> eventVerify = new HashMap<String, Object>();
								UserInfo userInfo = new UserInfo();
								
								eventVerify.put("eventVerifyId", eventVerifyId);
								eventVerify.put("status", DEFAULT_VALID_STATUS);
								eventVerify.put("isForce2Update", true);//强制更新
								userInfo.setUserId(updaterId);
								
								try {
									eventVerifyBaseService.updateEventVerifyById(eventVerify, userInfo);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								if(eventTmp != null) {
									eventVerifyAppRemark.append("原事件编号：").append(eventTmp.getCode());
								}
								
								//调整事件重置申请记录的备注信息
								if(eventVerifyAppRemark.length() > 0 && applicationId != null && applicationId > 0) {
									Map<String, Object> timeAppMap = new HashMap<String, Object>();

									timeAppMap.put("applicationId", applicationId);
									timeAppMap.put("updaterId", updaterId);
									timeAppMap.put("remark", "原事件编号：" + eventTmp.getCode());
									
									try {
										timeApplicationService.updateTimeApplicationById(timeAppMap);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								//删除事件
								List<Long> eventIdList = new ArrayList<Long>();
								
								eventIdList.add(eventId);
								
								eventDisposalService.deleteEventDisposalByIds(eventIdList, updaterId);
							} else {
								logger.error("事件审核记录【"+ eventVerifyId +"】的状态为【"+ eventVerifyStatus +"】，不是【已上报】！");
								eventVerifyAppRemark.append("事件审核记录【").append(eventVerifyId).append("】的状态为【").append(eventVerifyStatus).append("】，不是【已上报】！");
							}
							
						}
					}
					
					//调整事件重置审核记录的备注信息
					if(eventVerifyAppRemark.length() > 0 && checkId != null && checkId > 0) {
						
						Map<String, Object> timeVerifyMap = new HashMap<String, Object>();

						timeVerifyMap.put("checkId", checkId);
						timeVerifyMap.put("updaterId", updaterId);
						timeVerifyMap.put("userOrgCode", userOrgCode);
						timeVerifyMap.put("checkRemark", eventVerifyAppRemark.toString());
						timeVerifyMap.put("isCallback", false);
						
						try {
							timeApplicationCheckService.updateTimeAppCheckById(timeVerifyMap);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
