package cn.ffcs.zhsq.intermediateData.eventVerify.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.module.event.bo.Event;
import cn.ffcs.module.event.service.EventService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.crypto.HashIdManager;
import cn.ffcs.shequ.mybatis.domain.zzgl.attachment.Attachment;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.res.ResMarker;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.res.IResMarkerService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.CoordinateInverseQuery.service.ICoordinateInverseQueryService;
import cn.ffcs.zhsq.intermediateData.eventVerify.service.IEventVerifyBaseService;
import cn.ffcs.zhsq.map.coordinateconversion.service.IBaseConversionService;
import cn.ffcs.zhsq.mybatis.domain.CoordinateInverseQuery.CoordinateInverseQueryGridInfo;
import cn.ffcs.zhsq.mybatis.persistence.intermediateData.eventVerify.EventVerifyMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.JsonUtils;

/**
 * @Description: 微信事件信息接收表模块服务实现
 * @Author: zhangls
 * @Date: 08-21 09:05:05
 * @Copyright: 2017 福富软件
 */
@Service("eventVerify4WechatService")
@Transactional
public class EventVerify4WechatServiceImpl implements IEventVerifyBaseService {

	@Autowired
	private ICoordinateInverseQueryService coordinateInverseQueryService;

	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	private IResMarkerService resMarkerService;

	@Autowired
	private IBaseConversionService baseConversionService;

	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	@Autowired
	private IFunConfigurationService funConfigurationService;

	@Autowired
	private IMixedGridInfoService mixedGridInfoService;

	@Autowired
	private EventService wehatEventService;
	
	@Autowired
	private EventVerifyMapper eventVerifyMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	//审核模块地图定位类型
	private final String EVENT_VERIFY_MODULE_CODE = "ZHSQ_EVENT_VERIFY";
	//审核模块附件类型
	private final String EVENT_VERIFY_ATTACHMENT_TYPE = "ZHSQ_EVENT_VERIFY";
	//事件审核相关配置功能编码
	private final String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE";

	@Override
	public Long saveEventVerify(Map<String, Object> eventVerify) throws Exception {
		Long eventVerifyId = -1L;

		if(eventVerify != null) {
			Long gridId = -1L;
			String infoOrgCode = null;
			String longitude = null,
				   latitude = null,
				   DEFAULT_VALID_STATUS = "01",//默认状态 待审核
				   eventVerifyStatus = null,
				   userOrgCode = null;
			boolean flag = false,
					isForce2Update = false;//强制更新

			if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
				eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());

				if(eventVerifyId > 0) {
					Map<String, Object> eventVerifyMap = this.findEventVerifyById(eventVerifyId);
					if(CommonFunctions.isNotBlank(eventVerifyMap, "status")) {
						eventVerifyStatus = eventVerifyMap.get("status").toString();
					}
				}
			} else {
				if(CommonFunctions.isBlank(eventVerify, "status")) {
					eventVerify.put("status", DEFAULT_VALID_STATUS);
				}
			}

			if(CommonFunctions.isNotBlank(eventVerify, "userOrgCode")) {
				userOrgCode = eventVerify.get("userOrgCode").toString();
			}

			if(CommonFunctions.isNotBlank(eventVerify, "isForce2Update")) {
				isForce2Update = Boolean.valueOf(eventVerify.get("isForce2Update").toString());
			}

			if(isForce2Update || DEFAULT_VALID_STATUS.equals(eventVerifyStatus) || eventVerifyId <= 0) {
				formatEventVerifyIn(eventVerify);

				if(CommonFunctions.isNotBlank(eventVerify, "infoOrgCode")) {
					infoOrgCode = eventVerify.get("infoOrgCode").toString();
				}
				if(CommonFunctions.isNotBlank(eventVerify, "longitude")) {
					longitude = eventVerify.get("longitude").toString();
				}
				if(CommonFunctions.isNotBlank(eventVerify, "latitude")) {
					latitude = eventVerify.get("latitude").toString();
				}

				//通过经纬度获取默认网格
				if(StringUtils.isBlank(infoOrgCode) && StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {//通过经纬度转换为网格信息
					Map<String, Object> params = new HashMap<String, Object>();
					String EVENT_VERIFY_FUNC_CODE = "EVENT_VERIFY_ATTRIBUTE",
						   MAPTYPE = "mapType",
						   mapTypeStr = null;
					Integer mapType = null;

					if(CommonFunctions.isNotBlank(eventVerify, "mapType")) {
						mapTypeStr = eventVerify.get("mapType").toString();
					} else {
						mapTypeStr = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE, MAPTYPE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
					}

					if(StringUtils.isNotBlank(mapTypeStr)) {
						try {
							mapType = Integer.valueOf(mapTypeStr);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}

					if(mapType == null || mapType < 0) {
						mapType = 5;//默认使用二维地图
					}

					params.put("x", longitude);
					params.put("y", latitude);
					params.put("mapt", mapType);

					List<CoordinateInverseQueryGridInfo> gridInfoList = coordinateInverseQueryService.findGridInfo(params);
					if(gridInfoList != null && gridInfoList.size() > 0) {
						CoordinateInverseQueryGridInfo queryGridInfo = gridInfoList.get(0);
						gridId = queryGridInfo.getGridId();
						eventVerify.put("infoOrgCode", queryGridInfo.getInfoOrgCode());
					} else {
						logger.error("经度[" + longitude + "],纬度[" + latitude + "] 未有对应的网格信息！");
					}
				}
			} else if(!DEFAULT_VALID_STATUS.equals(eventVerifyStatus)) {//状态不是01[审核中]的，不可进行编辑
				throw new ZhsqEventException("该记录当前状态为【"+ baseDictionaryService.changeCodeToName(ConstantValue.EVENT_VERIFY_STATUS_PCODE, eventVerifyStatus, userOrgCode) +"】，不可进行数据编辑！");
			}

			if(eventVerifyId > 0) {
				flag = eventVerifyMapper.update(eventVerify) > 0;
			} else {
				flag = eventVerifyMapper.insert(eventVerify) > 0;
			}

			if(flag) {
				if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
					eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
				}

				if(CommonFunctions.isNotBlank(eventVerify, "innerAttachmentIds") ||
						CommonFunctions.isNotBlank(eventVerify, "outerAttachmentIds")) {
					String attachmentType = EVENT_VERIFY_ATTACHMENT_TYPE;

					if(CommonFunctions.isNotBlank(eventVerify, "attachmentType")) {
						attachmentType = eventVerify.get("attachmentType").toString();
					}

					if(CommonFunctions.isNotBlank(eventVerify, "innerAttachmentIds")) {
						String innerAttachmentIds = eventVerify.get("innerAttachmentIds").toString();

						attachmentService.updateBizId(eventVerifyId, attachmentType, innerAttachmentIds);
					} else if(CommonFunctions.isNotBlank(eventVerify, "outerAttachmentIds")) {
						String outerAttachmentIds = eventVerify.get("outerAttachmentIds").toString();
						String[] outerAttachIdArray = outerAttachmentIds.split(",");
						Long attachmentId = null;
						Attachment attachment = null;
						List<Attachment> attachmentList = new ArrayList<Attachment>();

						for(String attachIdStr : outerAttachIdArray) {
							if(StringUtils.isNotBlank(attachIdStr)) {
								try {
									attachmentId = Long.valueOf(attachIdStr);
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}

								if(attachmentId != null && attachmentId > 0) {
									attachment = attachmentService.findById(attachmentId);
									if(attachment != null) {
										attachment.setAttachmentId(null);
										attachment.setBizId(eventVerifyId);
										attachment.setAttachmentType(attachmentType);

										attachmentList.add(attachment);
									}
								}
							}
						}

						if(attachmentList.size() > 0) {
							attachmentService.saveAttachment(attachmentList);
						}
					}
				}

				//保存经纬度信息
				if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
					ResMarker resMarker = new ResMarker();
					String markerType = EVENT_VERIFY_MODULE_CODE,
						   mapt = "5";

					if(CommonFunctions.isNotBlank(eventVerify, "markerType")) {
						markerType = eventVerify.get("markerType").toString();
					}

					resMarker.setResourcesId(eventVerifyId);
					resMarker.setCatalog("03");
					resMarker.setX(longitude);
					resMarker.setY(latitude);
					resMarker.setMarkerType(markerType);

					if(StringUtils.isNotBlank(infoOrgCode) && gridId < 0) {//获取默认网格
						MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
						if(gridInfo != null) {
							gridId = gridInfo.getGridId();
						}
					}

					if(gridId > 0) {//获取网格的地图类型
						Map<String, Object> maptMap = baseConversionService.getMaptAndConversionXY(gridId, null, markerType, longitude, latitude);
						if(CommonFunctions.isNotBlank(maptMap, "mapt")) {
							mapt = maptMap.get("mapt").toString();
						}
					}

					resMarker.setMapType(mapt);

					resMarkerService.saveOrUpdateResMarker(resMarker);
				}
			}
		}

		return eventVerifyId;
	}

	@Override
	public boolean updateEventVerifyById(Map<String, Object> eventVerify, UserInfo userInfo) throws Exception {
		boolean flag = false;

		if(eventVerify != null) {
			Long userId = -1L;

			if(CommonFunctions.isNotBlank(eventVerify, "updateUserId")) {
				userId = Long.valueOf(eventVerify.get("updateUserId").toString());
			}

			if(userInfo != null) {
				if(userId == null || userId < 0) {
					eventVerify.put("updateUserId", userInfo.getUserId());
				}

				if(CommonFunctions.isBlank(eventVerify, "userOrgCode")) {
					eventVerify.put("userOrgCode", userInfo.getOrgCode());
				}
			}

			flag = this.saveEventVerify(eventVerify) > 0;
			
			if(flag && CommonFunctions.isNotBlank(eventVerify, "operateType")) {
				int operateType = Integer.valueOf(eventVerify.get("operateType").toString());
				String status = null;
				
				switch(operateType) {
					case 0: {//上报
						status = Event.HANDLE_STATUS_DEALING;
						break;
					}
					case 1: {//驳回
						status = Event.HANDLE_STATUS_BACK;
						break;
					}
				}
				
				if(StringUtils.isNotBlank(status) ) {
					Long eventVerifyId = null;
					
					if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
						eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
					}
					
					if(eventVerifyId != null && eventVerifyId > 0) {
						Map<String, Object> eventWechatMap = this.findEventVerifyById(eventVerifyId);
						String remark = "";
						Long bizId = -1L;
						
						if(CommonFunctions.isNotBlank(eventWechatMap, "bizId")) {
							try {
								bizId = Long.valueOf(eventWechatMap.get("bizId").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
						}
						
						if(bizId != null && bizId > 0) {
							if(CommonFunctions.isNotBlank(eventWechatMap, "remark")) {
								remark = eventWechatMap.get("remark").toString();
							}
							
							Event wechatEvent = new Event();
							wechatEvent.setEventId(bizId);
							wechatEvent.setHandleSatus(status);
							wechatEvent.setHandleCont(remark);
							
							try {
								wehatEventService.update(wechatEvent);
							} catch(Exception e) {
								logger.error("微信回调失败，失败原因如下：" + e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		return flag;
	}

	@Override
	public boolean deleteEventVerifyById(Long eventVerifyId, Long delUserId, Map<String, Object> eventVerify) {
		boolean flag = false;

		if(eventVerifyId != null && eventVerifyId > 0) {
			flag = eventVerifyMapper.delete(eventVerifyId, delUserId) > 0;
		}

		return flag;
	}

	@Override
	public Map<String, Object> findEventVerifyById(Long eventVerifyId, Map<String, Object> eventVerify) {
		Map<String, Object> eventVerifyResultMap = null;
		String userOrgCode = null;

		if(eventVerifyId != null && eventVerifyId > 0) {
			if(CommonFunctions.isNotBlank(eventVerify, "userOrgCode")) {
				userOrgCode = eventVerify.get("userOrgCode").toString();
			}
			eventVerifyResultMap = this.findEventVerifyById(eventVerifyId, userOrgCode);
		}

		return eventVerifyResultMap;
	}

	@Override
	public List<Map<String, Object>> findEventVerifyByParam(Map<String, Object> params) {
		List<Map<String, Object>> resultMapList = null;
		String userOrgCode = null;

		resultMapList = eventVerifyMapper.findEventVerifyByParam(params);

		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		}
		
		this.formatEventVerifyOut(resultMapList, userOrgCode);

		return resultMapList;
	}

	@Override
	public int findEventVerifyCount(Map<String, Object> params) {
		this.formatQueryParam(params);

		int count = eventVerifyMapper.findCountByCriteria(params);

		return count;
	}

	@Override
	public EUDGPagination findEventVerifyPagination(int pageNo, int pageSize,
			Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);

		this.formatQueryParam(params);

		int count = eventVerifyMapper.findCountByCriteria(params);
		List<Map<String, Object>> eventVerifyList = new ArrayList<Map<String, Object>>();

		if(count > 0) {
			String orgCode = "";
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				orgCode = params.get("userOrgCode").toString();
			}

			eventVerifyList = eventVerifyMapper.findPageListByCriteria(params, rowBounds);

			this.formatEventVerifyOut(eventVerifyList, orgCode);
		}

		EUDGPagination eventVerifyPagination = new EUDGPagination(count, eventVerifyList);

		return eventVerifyPagination;
	}

	@Override
	public Map<String, Object> fetchParam4Event(Map<String, Object> eventVerify) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long eventVerifyId = -1L;
		String attachmentType = EVENT_VERIFY_ATTACHMENT_TYPE,
			   markerType = EVENT_VERIFY_MODULE_CODE,
			   moduleCode = EVENT_VERIFY_MODULE_CODE;
		boolean isCapAttachmentId = false;

		resultMap.putAll(eventVerify);

		if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
			try {
				eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(CommonFunctions.isNotBlank(eventVerify, "attachmentType")) {
			attachmentType = eventVerify.get("attachmentType").toString();
		}
		if(CommonFunctions.isNotBlank(eventVerify, "markerType")) {
			markerType = eventVerify.get("markerType").toString();
		}
		if(CommonFunctions.isNotBlank(eventVerify, "moduleCode")) {
			moduleCode = eventVerify.get("moduleCode").toString();
		}
		if(CommonFunctions.isNotBlank(eventVerify, "isCapAttachmentId")) {
			isCapAttachmentId = Boolean.valueOf(eventVerify.get("isCapAttachmentId").toString());
		}

		if(isCapAttachmentId && eventVerifyId != null && eventVerifyId > 0) {
			List<Attachment> attahmentList = null;
			String outerAttachmentIds = "";

			attahmentList = attachmentService.findByBizId(eventVerifyId, attachmentType);

			if(attahmentList != null && attahmentList.size() > 0) {
				StringBuffer attachmentId = new StringBuffer("");

				for(Attachment attachment : attahmentList) {
					attachmentId.append(",").append(attachment.getAttachmentId());
				}

				outerAttachmentIds = attachmentId.substring(1);
			}

			resultMap.put("outerAttachmentIds", outerAttachmentIds);
		}

		resultMap.put("attachmentType", attachmentType);
		resultMap.put("markerType", markerType);
		resultMap.put("moduleCode", moduleCode);

		return resultMap;
	}

	/**
	 * 通过主键id获取事件审核信息
	 * @param eventVerifyId
	 * @return
	 */
	private Map<String, Object> findEventVerifyById(Long eventVerifyId) {
		Map<String, Object> eventVerify = null;

		if(eventVerifyId != null && eventVerifyId > 0) {
			eventVerify = eventVerifyMapper.findById(eventVerifyId);
		}

		return eventVerify;
	}

	/**
	 * 通过主键id获取事件审核信息，并格式化输出属性
	 * @param eventVerifyId
	 * @param orgCode	组织编码
	 * @return
	 */
	private Map<String, Object> findEventVerifyById(Long eventVerifyId, String orgCode) {
		Map<String, Object> eventVerify = findEventVerifyById(eventVerifyId);

		if(eventVerify != null && !eventVerify.isEmpty()) {
			formatEventVerifyOut(eventVerify, orgCode);
		}

		return eventVerify;
	}

	/**
	 * 格式化查询参数
	 * @param params
	 */
	private void formatQueryParam(Map<String, Object> params) {
		if(params != null && !params.isEmpty()) {
			boolean isDefaultJurisdiction = true,//是否使用默认地域查询
					isCapConfigureParam = false;//是否优先使用功能配置中的设置值
			String userOrgCode = null;

			if(CommonFunctions.isNotBlank(params, "isDefaultJurisdiction")) {
				isDefaultJurisdiction = Boolean.valueOf(params.get("isDefaultJurisdiction").toString());
			}
			if(CommonFunctions.isNotBlank(params, "isCapConfigureParam")) {
				isCapConfigureParam = Boolean.valueOf(params.get("isCapConfigureParam").toString());
			}
			if(isDefaultJurisdiction && CommonFunctions.isBlank(params, "infoOrgCode")
					 && CommonFunctions.isNotBlank(params, "defaultInfoOrgCode")) {
				params.put("infoOrgCode", params.get("defaultInfoOrgCode"));
			}
			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}

			if(params.containsKey("statusList") && CommonFunctions.isNotBlank(params,"statusList") && CommonFunctions.isBlank(params,"status")){
				if(params.get("statusList") instanceof String){
					String[] statusArr = (params.get("statusList").toString()).split(",");
					params.put("statusList",Arrays.asList(statusArr));
				} else if (params.get("statusList") instanceof List){

				} else {
					params.remove("statusList");
					params.put("status","-1");
				}
			} else if (CommonFunctions.isBlank(params,"statusList") && CommonFunctions.isNotBlank(params,"status")){
				String[] statusArr = (params.get("status").toString()).split(",");

				if(statusArr.length > 1){
					params.put("statusList",Arrays.asList(statusArr));
				}
			}

			//为了保证isJurisdictionQuery属性一定是布尔类型
			if(CommonFunctions.isNotBlank(params, "isJurisdictionQuery")) {
				params.put("isJurisdictionQuery", Boolean.valueOf(params.get("isJurisdictionQuery").toString()));
			}

			if (isCapConfigureParam) {
				String BIZ_PLATFORM_4_QUERY = "bizPlatform4Query", bizPlatform4Query = null;

				if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
					userOrgCode = params.get("userOrgCode").toString();
				}

				bizPlatform4Query = funConfigurationService.turnCodeToValue(EVENT_VERIFY_FUNC_CODE,
						BIZ_PLATFORM_4_QUERY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode,
						IFunConfigurationService.CFG_ORG_TYPE_0);

				if (StringUtils.isNotBlank(bizPlatform4Query)) {
					params.put("bizPlatformList", Arrays.asList(bizPlatform4Query.split(",")));
				}
			} else if (params.containsKey("bizPlatformList") && CommonFunctions.isNotBlank(params, "bizPlatformList")
					&& CommonFunctions.isBlank(params, "bizPlatform")) {
				if (params.get("bizPlatformList") instanceof String) {
					String[] bizPlatformArr = (params.get("bizPlatformList").toString()).split(",");
					params.put("bizPlatformList", Arrays.asList(bizPlatformArr));
				} else if (params.get("bizPlatformList") instanceof List) {

				}
			} else if (CommonFunctions.isBlank(params, "bizPlatformList")
					&& CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String[] bizPlatformArray = params.get("bizPlatform").toString().split(",");
				
				if (bizPlatformArray.length > 1) {
					params.put("bizPlatformList", Arrays.asList(bizPlatformArray));
				}
			}

			// 如果已经传入来源平台搜索条件，则取来源平台为传入值与默认值二者的交集
			if (CommonFunctions.isNotBlank(params, "bizPlatformForSearch")) {
				
				//获取页面搜索来源平台集合
				List<String> searchList = new ArrayList<String>(Arrays.asList(params.get("bizPlatformForSearch").toString().split(",")));
				
				//获取默认的搜索来源平台集合
				List<String> defaultList=new ArrayList<String>();
				if(CommonFunctions.isNotBlank(params, "bizPlatformList")) {
					defaultList=new ArrayList<String>((List<String>)params.get("bizPlatformList"));
				}else if(CommonFunctions.isNotBlank(params, "bizPlatform")){
					defaultList=new ArrayList<String>(Arrays.asList(params.get("bizPlatform").toString().split(",")));
				}
				
				defaultList.retainAll(searchList);
				
				
				params.remove("bizPlatform");
				params.put("bizPlatformList", defaultList);
				
				
			}

			
		}
	}
	
	/**
	 * 格式化输入数据
	 * @param eventVerify
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void formatEventVerifyIn(Map<String, Object> eventVerify) throws ParseException {
		if(CommonFunctions.isBlank(eventVerify, "happenTime") && CommonFunctions.isNotBlank(eventVerify, "happenTimeStr")) {
			eventVerify.put("happenTime", DateUtils.convertStringToDate(eventVerify.get("happenTimeStr").toString(), DateUtils.PATTERN_24TIME));
		}
		
		if(!eventVerify.containsKey("dataJsonMap")) {
			eventVerify.put("dataJsonMap", new HashMap<String, Object>());
		}
		
		if(CommonFunctions.isNotBlank(eventVerify, "dataJsonMap")) {
			Map<String, Object> eventVerifyMap = null,
								dataJsonMap = null;
			Object dataJsonMapObj = eventVerify.get("dataJsonMap");
			
			if(dataJsonMapObj instanceof Map) {
				dataJsonMap = (Map<String, Object>)dataJsonMapObj;
			} else if(dataJsonMapObj instanceof String) {
				dataJsonMap = JsonUtils.json2Map(dataJsonMapObj.toString());
			}
			
			if(CommonFunctions.isNotBlank(eventVerify, "eventVerifyId")) {
				Long eventVerifyId = -1L;
				
				try {
					eventVerifyId = Long.valueOf(eventVerify.get("eventVerifyId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(eventVerifyId > 0) {
					eventVerifyMap = this.findEventVerifyById(eventVerifyId);
					
					formatField4Clob(eventVerifyMap);
				}
			}
			
			if(CommonFunctions.isBlank(dataJsonMap, "eventBizPlatform")) {
				String eventBizPlatform = null;
				
				if(CommonFunctions.isNotBlank(eventVerifyMap, "eventBizPlatform")) {
					eventBizPlatform = eventVerifyMap.get("eventBizPlatform").toString();
				} else if(CommonFunctions.isNotBlank(eventVerifyMap, "bizPlatform")) {
					eventBizPlatform = eventVerifyMap.get("bizPlatform").toString();
				} else if(CommonFunctions.isNotBlank(eventVerify, "bizPlatform")) {
					eventBizPlatform = eventVerify.get("bizPlatform").toString();
				}
				
				if(eventBizPlatform != null) {
					dataJsonMap.put("eventBizPlatform", eventBizPlatform);
				}
			}
			
			if(dataJsonMap != null) {
				Map<String, Object> dataJsonClobMap = new HashMap<String, Object>();
				
				if(CommonFunctions.isNotBlank(eventVerifyMap, "dataJsonStr")) {
					dataJsonClobMap = JsonUtils.json2Map(eventVerifyMap.get("dataJsonStr").toString());
				}
				
				dataJsonClobMap.putAll(dataJsonMap);
				
				eventVerify.put("dataJson", JsonUtils.mapToJson(dataJsonClobMap));
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param eventVerify
	 * @param orgCode
	 */
	private void formatEventVerifyOut(Map<String, Object> eventVerify, String orgCode) {
		if(eventVerify != null) {
			List<Map<String, Object>> eventVerifyList = new ArrayList<Map<String, Object>>();
			
			eventVerifyList.add(eventVerify);
			
			formatEventVerifyOut(eventVerifyList, orgCode);
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param eventVerifyList
	 * @param orgCode	组织编码
	 */
	private void formatEventVerifyOut(List<Map<String, Object>> eventVerifyList, String orgCode) {
		if(eventVerifyList != null) {
			Date happenTime = null;
			List<BaseDataDict> statusDict = null, bizPlatformDict = null;
			
			Map<String,String> map = new LinkedHashMap<String,String>();
			
			if(StringUtils.isNotBlank(orgCode)) {
				Map<String, Object> dictMap = new HashMap<String, Object>();
//				dictMap.put("orgCode", orgCode);
				dictMap.put("dictPcode", "B593");
				
				statusDict = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVENT_VERIFY_STATUS_PCODE, orgCode);
				bizPlatformDict = baseDictionaryService.findDataDictListByCodes(dictMap);
				
				if(bizPlatformDict != null) {
					for (BaseDataDict baseDataDict : bizPlatformDict) {
						map.put(baseDataDict.getDictGeneralCode(), baseDataDict.getDictRemark());
					}
				}
			}
			
			for(Map<String, Object> eventVerify : eventVerifyList) {
				happenTime = null;
				
				formatField4Clob(eventVerify);
				
				if(CommonFunctions.isNotBlank(eventVerify, "happenTime")) {
					happenTime = (Date)eventVerify.get("happenTime");
				}
				
				if(happenTime != null) {
					eventVerify.put("happenTimeStr", DateUtils.formatDate(happenTime, DateUtils.PATTERN_24TIME));
				}
				
				DataDictHelper.setDictValueForField(eventVerify, "status", "statusName", statusDict);

				if(CommonFunctions.isNotBlank(eventVerify, "bizPlatform")) {
					eventVerify.put("bizPlatformName", map.get(eventVerify.get("bizPlatform")));
				}
				
				if(CommonFunctions.isNotBlank(eventVerify,"eventVerifyId")) {
					eventVerify.put("eventVerifyHashId", HashIdManager.encrypt(eventVerify.get("eventVerifyId")));
				}
			}
		}
	}
	
	/**
	 * 格式化CLOB字段
	 * @param eventVerify
	 */
	private void formatField4Clob(Map<String, Object> eventVerify) {
		if(CommonFunctions.isNotBlank(eventVerify, "dataJson")) {
			//将CLOB字段转换为字符串
			Clob dataJson = (Clob)eventVerify.get("dataJson");
	        StringBuffer datJsonBuffer = new StringBuffer();
	        
			try {
				Reader reader = dataJson.getCharacterStream();
				BufferedReader dataJsonReader = new BufferedReader(reader); 
		        String dataJsonLine = dataJsonReader.readLine();
		         
		        while (dataJsonLine != null) { 
		        	datJsonBuffer.append(dataJsonLine); 
		        	dataJsonLine = dataJsonReader.readLine(); 
		        } 
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
			if(datJsonBuffer.length() > 0) {//将DATA_JSON转换为Map
				eventVerify.put("dataJsonStr", datJsonBuffer.toString());
				eventVerify.putAll(JsonUtils.json2Map(datJsonBuffer.toString()));
			}
			
			eventVerify.put("dataJson", null);
		}
	}
	

}