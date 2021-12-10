package cn.ffcs.zhsq.event.service.impl;

import cn.ffcs.zhsq.eventExpand.service.IEventDisposal4ExpandService;
import org.mybatis.spring.SqlSessionTemplate;

import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.file.service.IAttachmentService;
import cn.ffcs.gis.mybatis.domain.base.ResMarker;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.zhsq.event.service.IEvent4WorkflowService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelRelaService;
import cn.ffcs.zhsq.eventExpand.service.IEventLabelService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.mybatis.persistence.event.Event4WorkflowMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件与工作流关联相关接口
 * @author zhangls
 *
 */
@Service(value = "event4WorkflowServiceImpl")
public class Event4WorkflowServiceImpl implements IEvent4WorkflowService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IFunConfigurationService funConfigurationService;
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private Event4WorkflowMapper event4WorkflowMapper;
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	@Autowired
	private IEventLabelService eventLabelService;
	
	@Autowired
	private IEventLabelRelaService eventLabelRelaService;

	@Autowired
	private IEventDisposal4ExpandService eventDisposalExpandService;

    private static List<String> orderByFieldList = new ArrayList<String>();

    static{
        String[] fieldArray = new String[]{"CREATE_TIME","HAPPEN_TIME","FIN_TIME"};
        for(String item : fieldArray){
            orderByFieldList.add(item);
            orderByFieldList.add(item+"ASC");
            orderByFieldList.add(item+"DESC");
            orderByFieldList.add("T1."+item);
            orderByFieldList.add("T1."+item+"ASC");
            orderByFieldList.add("T1."+item+"DESC");
        }
        orderByFieldList.add("T4.CREATE_DESC");
    }
	@Override
	public int findEventCount(Map<String, Object> params) throws ZhsqEventException {
		int count = 0,
			listType = 0;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		switch(listType) {
			case 0: {//草稿事件
				count = eventMapper.findCount4Draft(params);
				break;
			}
			case 1: {//待办事件
				count = eventMapper.findCount4Todo(params);
				break;
			}
			case 2: {//经办事件
				count = eventMapper.findCount4Handled(params);
				break;
			}
			case 3: {//辖区所有(支持办理人员查询)
				count = eventMapper.findCount4EventOrg(params);
				break;
			}
			case 4: {//归档列表
				count = eventMapper.findCount4Archive(params);
				break;
			}
			case 5: {// 辖区所有
				count = eventMapper.findCount4Jurisdiction(params);
			}
			case 9: {//辖区所有(展示当前环节)
				break;
			}
			case 6: {//我发起的
				count = eventMapper.findCount4Initiator(params);
				break;
			}
			case 7: {//我的关注
				count = eventMapper.findCount4Attention(params);
				break;
			}
			case 8: {//辖区内需要督办
				count = eventMapper.findCount4Supervise(params);
				break;
			}
		}
		
		return count;
	}
	
	@Override
	public EUDGPagination findEventListPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		Integer listType = 0;
		EUDGPagination eventPagination = null;
		
		formatParamIn(params);
		
		if(CommonFunctions.isNotBlank(params, "listType")) {
			try {
				listType = Integer.valueOf(params.get("listType").toString());
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		switch(listType) {
			case 0: {//草稿
				eventPagination = this.findEvent4DraftPagination(pageNo, pageSize, params); break;
			}
			case 1: {//待办
				eventPagination = this.findEvent4TodoPagination(pageNo, pageSize, params); break;
			}
			case 2: {//经办
				eventPagination = this.findEventList4HandledPagination(pageNo, pageSize, params); break;
			}
			case 3: {//辖区所有(支持办理人员查询)
				eventPagination = this.findEventList4EventOrgPagination(pageNo, pageSize, params); break;
			}
			case 4: {//归档列表
				eventPagination = findEventList4ArchivePagination(pageNo, pageSize, params); break;
			}
			case 9: {}//辖区所有(展示当前环节)
			case 5: {//辖区所有
				eventPagination = this.findEventList4JurisdictionPagination(pageNo, pageSize, params); break;
			}
			case 6: {//我发起的
				eventPagination = findEventList4InitiatorPagination(pageNo, pageSize, params); break;
			}
			case 7: {//我的关注列表
				eventPagination = findEventList4AttentionPagination(pageNo, pageSize, params); break;
			}
			case 8: {//辖区所有需要督办列表
				eventPagination = findEventList4SupervisePagination(pageNo, pageSize, params); break;
			}
			default: {
				eventPagination = new EUDGPagination(0, new ArrayList<Map<String, Object>>()); break;
			}
		}
		
		return eventPagination;
	}
	
	
	/**
	 * 分页获取事件草稿列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					curUserId	当前办理人员
	 * 					curOrgId	当前办理组织
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEvent4DraftPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Draft(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Draft(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	
	/**
	 * 分页获取事件待办列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					curUserId	当前办理人员
	 * 					curOrgId	当前办理组织
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEvent4TodoPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Todo(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Todo(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 经办事件列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					handledUserId	经办人员id
	 * 					handledOrgId	经办组织id
	 * @return
	 * @throws ZhsqEventException 
	 */
	private EUDGPagination findEventList4HandledPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Handled(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Handled(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 辖区所有(支持办理人员查询)
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					eventOrgUserId	办理人员id
	 * 					eventOrgOrgId		办理组织id
	 * @return
	 */
	private EUDGPagination findEventList4EventOrgPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4EventOrg(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4EventOrg(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 分页获取事件归档列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEventList4ArchivePagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Archive(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Archive(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 分页获取事件辖区所有列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEventList4JurisdictionPagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Jurisdiction(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Jurisdiction(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 分页获取我发起的事件列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					initiatorId		事件发起用户id
	 * 					initiatorOrgId	事件发起组织id
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEventList4InitiatorPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Initiator(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Initiator(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 分页获取我的关注的事件列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * 					attentionUserId		事件关注用户id
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEventList4AttentionPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Attention(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Attention(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 分页获取事件辖区所有需要督办列表
	 * @param pageNo	页码
	 * @param pageSize	每页记录数
	 * @param params
	 * @return
	 * @throws ZhsqEventException
	 */
	private EUDGPagination findEventList4SupervisePagination(int pageNo,
			int pageSize, Map<String, Object> params) throws ZhsqEventException {
		pageNo = pageNo < 1 ? 1 : pageNo;
		pageSize = pageSize < 1 ? 10 : pageSize;
		RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
		int count = 0;
		List<Map<String, Object>> eventList = null;
		Event4WorkflowMapper eventMapper = capMapper(params);
		
		count = eventMapper.findCount4Supervise(params);
		
		if(count > 0) {
			eventList = eventMapper.findPageList4Supervise(params, rowBounds);
		} else {
			eventList = new ArrayList<Map<String, Object>>();
		}
		
		formatMapDataOut(eventList, params);
		
		EUDGPagination eventPagination = new EUDGPagination(count, eventList);
		
		return eventPagination;
	}
	
	/**
	 * 格式化查询条件
	 * @param params
	 * @throws ZhsqEventException 
	 */
	@SuppressWarnings("unchecked")
	private void formatParamIn(Map<String, Object> params) throws ZhsqEventException {
		if(params != null && !params.isEmpty()) {
			//事件主表动态条件
			StringBuffer dynamicSql4Event = new StringBuffer("");
			List<String> defaultStatusList = new ArrayList<String>(),//列表默认状态
								statusList = null;
			Integer listType = 0;
			StringBuffer msgWrong = new StringBuffer("");

           /* Object orderByField = params.get("orderByField");
            if(orderByField!=null){
                boolean flag = orderByFieldList.contains(String.valueOf(orderByField).replaceAll("\\s*","").toUpperCase());
                if(!flag){
                    params.remove("orderByField");
                    logger.error("无效的order By语句："+orderByField);
                }
            }*/

			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
			
			switch(listType) {
				case 0: {//草稿
					if(CommonFunctions.isBlank(params, "creatorId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("creatorId", Long.valueOf(params.get("userId").toString()));
					}
					
					if(CommonFunctions.isNotBlank(params, "creatorId")) {
						try {
							Long creatorId = Long.valueOf(params.get("creatorId").toString().trim());
							params.put("creatorId", creatorId);
						} catch (NumberFormatException e) {
							msgWrong.append("事件采集人员id【creatorId】：").append(params.get("creatorId")).append("，不是有效的数值！");
						}
					} else {
						msgWrong.append("缺少事件采集人员id【creatorId】！");
					}
					break;
				}
				case 1: {//待办
					//curUserId、curOrgId查询时需要为字符类型
					if(CommonFunctions.isBlank(params, "curUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("curUserId", params.get("userId").toString());
					}
					if(CommonFunctions.isBlank(params, "curOrgId") && CommonFunctions.isNotBlank(params, "orgId")) {
						params.put("curOrgId", params.get("orgId").toString());
					}
					
					if(CommonFunctions.isBlank(params, "curUserId")) {
						msgWrong.append("缺少当前办理人员id 【curUserId】！");
					}
					if(CommonFunctions.isBlank(params, "curOrgId")) {
						msgWrong.append("缺少当前办理组织id 【curOrgId】！");
					}
					break;
				}
				case 2: {//经办
					if(CommonFunctions.isBlank(params, "handledUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("handledUserId", params.get("userId"));
					}
					if(CommonFunctions.isBlank(params, "handledOrgId") && CommonFunctions.isNotBlank(params, "orgId")) {
						params.put("handledOrgId", params.get("orgId"));
					}
					
					if(CommonFunctions.isNotBlank(params, "handledUserId")) {
						try {
							params.put("handledUserId", Long.valueOf(params.get("handledUserId").toString()));
						} catch(NumberFormatException e) {
							msgWrong.append("事件办理人员id【handledUserId】：").append(params.get("handledUserId")).append("，不是有效的数值！");
						}
					} else {
						msgWrong.append("缺少办理人员id 【handledUserId】！");
					}
					
					if(CommonFunctions.isNotBlank(params, "handledOrgId")) {
						try {
							params.put("handledOrgId", Long.valueOf(params.get("handledOrgId").toString()));
						} catch(NumberFormatException e) {
							msgWrong.append("事件办理组织id【handledOrgId】：").append(params.get("handledOrgId")).append("，不是有效的数值！");
						}
					} else {
						msgWrong.append("缺少办理组织id 【handledOrgId】！");
					}
					break;
				}
				case 3: {//辖区所有(支持办理人员查询)
					//eventOrgUserId、eventOrgOrgId查询时需要为字符类型
					if(CommonFunctions.isNotBlank(params, "eventOrgUserId")) {
						params.put("eventOrgUserId", params.get("eventOrgUserId").toString());
					} else if(CommonFunctions.isNotBlank(params, "userId")) {
						params.put("eventOrgUserId", params.get("userId").toString());
					}
					if(CommonFunctions.isNotBlank(params, "eventOrgOrgId")) {
						params.put("eventOrgOrgId", params.get("eventOrgOrgId").toString());
					} else if(CommonFunctions.isNotBlank(params, "orgId")) {
						params.put("eventOrgOrgId", params.get("orgId").toString());
					}
					
					if(CommonFunctions.isNotBlank(params, "eventOrgUserId") && CommonFunctions.isBlank(params, "eventOrgOrgId")) {
						msgWrong.append("缺少办理人员所属组织【eventOrgOrgId】！");
					}
					break;
				}
				case 6: {//我发起的
					if(CommonFunctions.isBlank(params, "initiatorId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("initiatorId", params.get("userId").toString());
					}
					if(CommonFunctions.isBlank(params, "initiatorOrgId") && CommonFunctions.isNotBlank(params, "orgId")) {
						params.put("initiatorOrgId", params.get("orgId"));
					}
					Long initiatorId=0L;
					Long initiatorOrgId=0L;
					if(CommonFunctions.isNotBlank(params, "initiatorId")) {
						try {
							initiatorId = Long.valueOf(params.get("initiatorId").toString().trim());
							params.put("initiatorId", initiatorId);
						} catch(NumberFormatException e) {
							msgWrong.append("事件发起人员用户id【initiatorId】：").append(params.get("initiatorId")).append("，不是有效的数值！");
						}
					} else {
						msgWrong.append("缺少事件发起人员用户id【initiatorId】！");
					}
					
					if(CommonFunctions.isNotBlank(params, "initiatorOrgId")) {
						try {
							initiatorOrgId = Long.valueOf(params.get("initiatorOrgId").toString().trim());
							params.put("initiatorOrgId", initiatorOrgId);
						} catch (NumberFormatException e) {
							msgWrong.append("事件发起人员组织id【initiatorOrgId】：").append(params.get("initiatorOrgId")).append("，不是有效的数值！");
						}
					} else {
						msgWrong.append("缺少事件发起人员组织id【initiatorOrgId】！");
					}
					
					if(initiatorId<=0&&initiatorOrgId<=0) {
						msgWrong.append("事件发起人员用户id【initiatorId】：").append(params.get("initiatorId"))
						.append("事件发起人员组织id【initiatorOrgId】：").append(params.get("initiatorOrgId")).append("，不是有效的数值！");
					}else if(initiatorId<=0&&initiatorOrgId>0) {
						//只传入组织Id会导致用户看到该组织下所有的事件，包括不是他发起的事件
						msgWrong.append("缺少事件发起人员用户id【initiatorId】！");
						//另外如果只传入用户id而不传组织id，用户会看到他自己发起的所有事件（包括不同的组织）
						//由于这种情况用户并没有看到不属于自己该看到的事件，因此这种情况不做传参限定
					}
					
					break;
				}
				case 5: {//辖区所有事件

				}
				case 7: {//我的关注事件
					if(CommonFunctions.isBlank(params, "attentionUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("attentionUserId", params.get("userId"));
					}
					
					if(CommonFunctions.isBlank(params, "attentionUserId")) {
						msgWrong.append("缺少事件关注人员用户id【attentionUserId】！");
					} else {
						Long attentionUserId = null;
						
						try {
							attentionUserId = Long.valueOf(params.get("attentionUserId").toString());
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						
						if(attentionUserId != null && attentionUserId > 0) {
							params.put("attentionUserId", attentionUserId);
						} else {
							msgWrong.append("事件关注人员用户id【attentionUserId】：【").append(params.get("attentionUserId")).append("】不是有效的用户id！");
						}
					}
					break;
				}
			
			}
			
			//是否获取我督办过的事件
			if(CommonFunctions.isNotBlank(params, "isCapMySupervised")) {
				boolean isCapMySupervised = Boolean.valueOf(params.get("isCapMySupervised").toString());
				if(isCapMySupervised) {
					if(CommonFunctions.isBlank(params, "superviseUserId") && CommonFunctions.isNotBlank(params, "userId")) {
						params.put("superviseUserId", params.get("userId"));
					}
					
					if(CommonFunctions.isBlank(params, "superviseUserId")) {
						msgWrong.append("缺少事件督办操作人员用户id【superviseUserId】！");
					}
				}
				params.put("isCapMySupervised", isCapMySupervised);
			}
			
			if(msgWrong.length() > 0) {
				throw new ZhsqEventException(msgWrong.toString());
			}
			
			defaultStatusList.add(ConstantValue.EVENT_STATUS_RECEIVED);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_REPORT);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_DISTRIBUTE);
			defaultStatusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
			
			switch(listType) {
				case 0: {//草稿
					defaultStatusList.clear();
					defaultStatusList.add(ConstantValue.EVENT_STATUS_DRAFT); 
					break;
				}
				case 1: {//待办
				}
				case 8: {//辖区内需要督办事件
					break;
				}
				case 4: {//归档
					defaultStatusList.clear();
					defaultStatusList.add(ConstantValue.EVENT_STATUS_END); 
					break;
				}
				case 9: {}
				case 5: {//辖区所有
					/*南昌事件-辖区所有列表高级查询：办理单位、办理方式为结案的事件状态过滤*/
					String handledType = "",
							  EVENT_STATUS = "nanChangEventEnd";
					if (CommonFunctions.isNotBlank(params,"handledTaskUnitId")) {
						params.put("handledTaskUnitId",params.get("handledTaskUnitId").toString());
					}
					if (CommonFunctions.isNotBlank(params,"handledType")) {
						handledType = params.get("handledType").toString();
					}
					if (CommonFunctions.isNotBlank(params,"handledTaskUnitId") && StringUtils.isNotBlank(handledType) && EVENT_STATUS.equals(handledType) ) {
						defaultStatusList.clear();
						defaultStatusList.add(ConstantValue.EVENT_STATUS_ARCHIVE);
					}
					
					if(CommonFunctions.isNotBlank(params, "isMapDistanceSearch4SelfEvent")) {
						boolean isMapDistanceSearch4SelfEvent = Boolean.valueOf(params.get("isMapDistanceSearch4SelfEvent").toString());
						
						if(isMapDistanceSearch4SelfEvent) {
							String MAP_TYPE_DEFAULT = "5";//默认使用二维地图
							String userOrgCode = null;
							Long selfEventId = null;
							EventDisposal selfEvent = null;
							
							if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
								userOrgCode = params.get("userOrgCode").toString();
							} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
								userOrgCode = params.get("orgCode").toString();
							}
							
							if(CommonFunctions.isNotBlank(params, "selfEventId")) {
								try {
									selfEventId = Long.valueOf(params.get("selfEventId").toString());
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
							}
							
							if(selfEventId != null && selfEventId > 0) {
								selfEvent = eventDisposalService.findEventByIdAndMapt(selfEventId, MAP_TYPE_DEFAULT.toString(), userOrgCode);
							}
							
							if(selfEvent != null) {
								ResMarker resMarker = selfEvent.getResMarker();
								
								if(resMarker != null) {
									params.put("infoOrgCode", selfEvent.getGridCode());
									params.put("isMapDistanceSearch", false);
									params.put("mapDistanceWithin", 10);//查找距离该事件10米范围的其他事件
									params.put("mapType", MAP_TYPE_DEFAULT);
									params.put("longitude", resMarker.getX());
									params.put("latitude", resMarker.getY());
									params.put("createTimeStart", DateUtils.formatDate(DateUtils.addInterval(new Date(), -3, "00"), DateUtils.PATTERN_DATE));
									params.put("createTimeEnd", DateUtils.getToday());
									params.put("eventNameAccurate", selfEvent.getEventName());
									params.put("eliminateEventIdList", String.valueOf(selfEvent.getEventId()));//需要将自身扣除
								}
							}
						}
					}
					
					if(CommonFunctions.isNotBlank(params, "isCapMapInfo")) {
						boolean isCapMapInfo = Boolean.valueOf(params.get("isCapMapInfo").toString());
						
						if(isCapMapInfo) {
							String mapType = "5";
							
							if(CommonFunctions.isNotBlank(params, "mapType")) {
								mapType = params.get("mapType").toString();
							}
							
							params.put("mapType", mapType);
						}
						
						//mapper中需要使用boolean类型
						params.put("isCapMapInfo", isCapMapInfo);
					}
					
					if(CommonFunctions.isNotBlank(params, "isMapDistanceSearch")) {
						boolean isMapDistanceSearch = Boolean.valueOf(params.get("isMapDistanceSearch").toString());
						
						if(isMapDistanceSearch) {
							String mapType = "5";
							Long mapDistanceWithin = 0L;
							Double longitude = 0D, latitude = 0D;
							
							if(CommonFunctions.isNotBlank(params, "mapType")) {
								mapType = params.get("mapType").toString();
							}
							if(CommonFunctions.isNotBlank(params, "mapDistanceWithin")) {
								try {
									mapDistanceWithin = Long.valueOf(params.get("mapDistanceWithin").toString());
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
							}
							if(CommonFunctions.isNotBlank(params, "longitude")) {
								try {
									longitude = Double.valueOf(params.get("longitude").toString());
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
							}
							if(CommonFunctions.isNotBlank(params, "latitude")) {
								try {
									latitude = Double.valueOf(params.get("latitude").toString());
								} catch(NumberFormatException e) {
									e.printStackTrace();
								}
							}
							
							params.put("mapType", mapType);
							params.put("mapDistanceWithin", mapDistanceWithin);
							params.put("longitude", longitude);
							params.put("latitude", latitude);
						}
						
						params.put("isMapDistanceSearch", isMapDistanceSearch);
					}
				}
				default: {
					defaultStatusList.add(ConstantValue.EVENT_STATUS_END); break;
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "emtType")) {
				Integer emtType = null;
				
				try {
					emtType = Integer.valueOf(params.get("emtType").toString());
				} catch(NumberFormatException e) {}
				
				if(emtType != null && emtType >= 0) {
					params.put("emtType", emtType);
					params.put("isCapEventExtend", true);
				} else {
					params.remove("emtType");
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "isCapEventExtend")) {
				params.put("isCapEventExtend", Boolean.valueOf(params.get("isCapEventExtend").toString()));
			}
			
			if(CommonFunctions.isNotBlank(params, "statusList")) {
				Object statusListObj = params.get("statusList");
				if(statusListObj instanceof List) {
					statusList = (List<String>) params.get("statusList");
				} else if(statusListObj instanceof String) {
					statusList = Arrays.asList(statusListObj.toString().split(","));
				}
			} else if(CommonFunctions.isNotBlank(params, "statusArray")) {
				Object statusArray = params.get("statusArray");
				if(statusArray instanceof String[]) {
					statusList = Arrays.asList((String[]) params.get("statusArray"));
				}
			} else if(CommonFunctions.isNotBlank(params, "status")) {
				statusList = Arrays.asList(params.get("status").toString().split(","));
			}
			
			if(statusList != null) {
				defaultStatusList.retainAll(statusList);
				params.put("statusList", defaultStatusList);
			} else {
				params.put("statusList", defaultStatusList);
			}
			if(CommonFunctions.isNotBlank(params, "collectWay")) {
				String collectWay = params.get("collectWay").toString();
				if(collectWay.contains(",")) {
					params.put("collectWayArray", collectWay.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "subStatus")) {
				String subStatus = params.get("subStatus").toString();
				if(subStatus.contains(",")) {
					params.put("subStatusArray", subStatus.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "influenceDegree")) {
				String influenceDegree = params.get("influenceDegree").toString();
				if(influenceDegree.contains(",")) {
					params.put("influenceDegreeArray", influenceDegree.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "urgencyDegree")) {
				String urgencyDegree = params.get("urgencyDegree").toString();
				if(urgencyDegree.contains(",")) {
					params.put("urgencyDegreeArray", urgencyDegree.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "source")) {
				String source = params.get("source").toString();
				if(source.contains(",")) {
					params.put("sourceArray", source.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "patrolType")) {
				String patrolType = params.get("patrolType").toString();
				if(patrolType.contains(",")) {
					params.put("patrolTypeArray", patrolType.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "supervisionType")) {
				String supervisionType = params.get("supervisionType").toString();
				if(supervisionType.contains(",")) {
					params.put("supervisionTypeArray", supervisionType.split(","));
				}
			}
			if(CommonFunctions.isNotBlank(params, "attrFlag")) {
				String attrFlag = params.get("attrFlag").toString();
				
				if(!attrFlag.contains(",%")) {
					attrFlag = attrFlag.replaceAll(",", ",%");
				}
				
				params.put("attrFlag", attrFlag);
			}
			if(CommonFunctions.isNotBlank(params, "evaLevelList")) {
				Object evaLevelListObj = params.get("evaLevelList");
				if(evaLevelListObj instanceof String) {
					params.put("evaLevelList", Arrays.asList(evaLevelListObj.toString().split(","))); 
				}
			}
			if(CommonFunctions.isNotBlank(params, "bizPlatform")) {
				String bizPlatform = params.get("bizPlatform").toString();
				
				if(bizPlatform.contains(",")) {
					params.put("bizPlatformArray", bizPlatform.split(","));
					params.remove("bizPlatform");
				}
			}
			if(CommonFunctions.isNotBlank(params, "eInfoOrgCode")) {//有加密的地域编码
				byte[] decodedData = Base64.decode(params.get("eInfoOrgCode").toString());
				params.put("infoOrgCode", new String(decodedData));
			}
			if(CommonFunctions.isNotBlank(params, "gridId") && CommonFunctions.isBlank(params, "infoOrgCode")) {
				Long gridId = null;
				
				try {
					gridId = Long.valueOf(params.get("gridId").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
				
				if(gridId != null && gridId > 0) {
					MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
					if(gridInfo != null) {
						params.put("infoOrgCode", gridInfo.getInfoOrgCode());
					}
				}
			}
			if(CommonFunctions.isNotBlank(params, "eventLabel")) {
				Object evaLevelListObj = params.get("eventLabel");
				if(evaLevelListObj instanceof String) {
					params.put("eventLabelList", Arrays.asList(evaLevelListObj.toString().split(",")));
				}
			}
			
			//辖区内查询相关列表需要去除顶级网格模糊查询
			switch(listType) {
				case 3: {}	//辖区所有(支持办理人员查询)
				case 4: {}	//归档列表
				case 5: {}	//辖区所有
				case 8: {}	//辖区内需要督办
				case 9: {	//辖区所有(展示当前环节)
					if(CommonFunctions.isNotBlank(params, "infoOrgCode")) {
						String infoOrgCode = params.get("infoOrgCode").toString();
						//功能编码为：BUILD_SCOPE_SETTING，每个只能平台配置一个，修改需要重启cookie生效
						if(infoOrgCode.equals(cacheService.getBuildScopeSettingCode())) {
							params.remove("infoOrgCode");
						}
					}
					break;
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "eventIdList")) {
				Object eventIdListObj = params.get("eventIdList");
				String[] eventIdArray = null;
				List<Long> eventIdList = new ArrayList<Long>();
				
				if(eventIdListObj instanceof String) {
					eventIdArray = ((String) eventIdListObj).split(",");
				} if(eventIdListObj instanceof String[]) {
					eventIdArray = (String[])eventIdListObj;
				} else if(eventIdListObj instanceof List) {
					List<Object> eventIdObjList = (List<Object>)eventIdListObj;
					List<String> eventIdStrList = new ArrayList<String>();
					
					for(Object eventIdObj : eventIdObjList) {
						eventIdStrList.add(eventIdObj.toString());
					}
					
					eventIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
				}
				
				if(eventIdArray != null) {
					Long eventIdL = -1L;
					
					for(String eventId : eventIdArray) {
						if(StringUtils.isNotBlank(eventId)) {
							try {
								eventIdL = Long.valueOf(eventId);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(eventIdL != null && eventIdL > 0) {
								eventIdList.add(eventIdL);
							}
						}
					}
				}
				
				params.put("eventIdList", eventIdList);
			}
			if(CommonFunctions.isNotBlank(params, "eliminateEventIdList")) {
				Object eventIdListObj = params.get("eliminateEventIdList");
				String[] eventIdArray = null;
				List<Long> eventIdList = new ArrayList<Long>();
				
				if(eventIdListObj instanceof String) {
					eventIdArray = ((String) eventIdListObj).split(",");
				} if(eventIdListObj instanceof String[]) {
					eventIdArray = (String[])eventIdListObj;
				} else if(eventIdListObj instanceof List) {
					List<Object> eventIdObjList = (List<Object>)eventIdListObj;
					List<String> eventIdStrList = new ArrayList<String>();
					
					for(Object eventIdObj : eventIdObjList) {
						eventIdStrList.add(eventIdObj.toString());
					}
					
					eventIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
				}
				
				if(eventIdArray != null) {
					Long eventIdL = -1L;
					
					for(String eventId : eventIdArray) {
						if(StringUtils.isNotBlank(eventId)) {
							try {
								eventIdL = Long.valueOf(eventId);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(eventIdL != null && eventIdL > 0) {
								eventIdList.add(eventIdL);
							}
						}
					}
				}
				
				params.put("eliminateEventIdList", eventIdList);
			}
			//是否查询事件列表额外列
			if(CommonFunctions.isNotBlank(params, "isCapEventAdditionalColumn")) {
				params.put("isCapEventAdditionalColumn", Boolean.valueOf(params.get("isCapEventAdditionalColumn").toString()));
			}
			
			//格式化事件标签Id入参
			if(CommonFunctions.isNotBlank(params, "eventLabelIds")) {
				Object eventLabelIdObj = params.get("eventLabelIds");
				String[] eventLabelIdArray = null;
				List<Long> eventLabelIdList = new ArrayList<Long>();
				
				if(eventLabelIdObj instanceof String) {
					eventLabelIdArray = ((String) eventLabelIdObj).split(",");
				} if(eventLabelIdObj instanceof String[]) {
					eventLabelIdArray = (String[])eventLabelIdObj;
				} else if(eventLabelIdObj instanceof List) {
					List<Object> eventIdObjList = (List<Object>)eventLabelIdObj;
					List<String> eventIdStrList = new ArrayList<String>();
					
					for(Object eventIdObj : eventIdObjList) {
						eventIdStrList.add(eventIdObj.toString());
					}
					
					eventLabelIdArray = eventIdStrList.toArray(new String[eventIdStrList.size()]);
				}
				
				if(eventLabelIdArray != null) {
					Long eventLabelIdL = -1L;
					
					for(String eventLabelId : eventLabelIdArray) {
						if(StringUtils.isNotBlank(eventLabelId)) {
							try {
								eventLabelIdL = Long.valueOf(eventLabelId);
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(eventLabelIdL != null && eventLabelIdL > 0) {
								eventLabelIdList.add(eventLabelIdL);
							}
						}
					}
				}
				if(eventLabelIdList.size()>0) {
					params.remove("eventLabelIds");
					params.put("eventLabelIdList", eventLabelIdList);
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "eventAttrTrigger")) {
				String eventAttrTrigger = params.get("eventAttrTrigger").toString(),
					   eventAttrName = "",
					   orgCode = "";
				
				if(CommonFunctions.isNotBlank(params, "eventAttrOrgCode")){
					orgCode = params.get("eventAttrOrgCode").toString();
				} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
					orgCode = params.get("orgCode").toString();
				}
				
				eventAttrName = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
				
				if(StringUtils.isNotBlank(eventAttrName)){
					String[] eventAttrNameArray = eventAttrName.split(",");
					String attrValue = "";
					
					for(String attrName : eventAttrNameArray){
						attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
						
						if(StringUtils.isNotBlank(attrValue)){
							dynamicSql4Event.append(" AND T1.").append(attrName).append(" IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
						} else {
							attrValue = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_ATTRIBUTE, eventAttrTrigger+"_"+attrName+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
							
							if(StringUtils.isNotBlank(attrValue)) {
								dynamicSql4Event.append(" AND T1.").append(attrName).append(" NOT IN ('").append(attrValue.trim().replaceAll(",", "','")).append("')");
							}
						}
					}
				}
			}
			
			if(CommonFunctions.isNotBlank(params, "type")) {
				String type = params.get("type").toString();
				
				if(type.contains(",")) {
					params.put("types", params.get("type"));
					params.remove("type");
				}
			}
			if(CommonFunctions.isNotBlank(params, "types") || CommonFunctions.isNotBlank(params, "trigger")) {
				boolean isRemoveTypes = false;
				String types = "";
				
				if(CommonFunctions.isNotBlank(params, "isRemoveTypes")) {//isRemoveTypes为true时，去除types中包含的事件类型；isRemoveTypes为false时，展示types中包含的事件类型
					isRemoveTypes = Boolean.valueOf(params.get("isRemoveTypes").toString());
				}
				
				if(CommonFunctions.isNotBlank(params, "types")) {
					types = params.get("types").toString().trim();
				} else if(CommonFunctions.isNotBlank(params, "trigger")) {
					String trigger = params.get("trigger").toString(),
						   orgCode = "";
					
					if(CommonFunctions.isNotBlank(params, "orgCode")) {
						orgCode = params.get("orgCode").toString();
					}
					
					types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger, IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					
					if(StringUtils.isBlank(types)) {
						isRemoveTypes = true;
						types = funConfigurationService.changeCodeToValue(ConstantValue.TYPES, trigger+"_NO", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode);
					}
				}
				
				if(StringUtils.isNotBlank(types)) {
					String[] typesArray = types.split(",");
					
					if(typesArray.length > 0) {
						if(isRemoveTypes) {
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" AND T1.TYPE_ NOT LIKE '").append(_type).append("%'");
								}
							}
						} else {
							dynamicSql4Event.append(" AND ( 1 != 1 ");
							for(String _type : typesArray) {
								if(StringUtils.isNotBlank(_type)) {
									dynamicSql4Event.append(" OR T1.TYPE_ LIKE '" + _type + "%'");
								}
							}
							dynamicSql4Event.append(") ");
						}
					}
				}
			}

			if(dynamicSql4Event.length() > 0) {
				params.put("dynamicSql4Event", dynamicSql4Event.toString());
			}
		}
	}
	
	/**
	 * 格式化输出数据
	 * @param eventList
	 * @param params
	 * 		listType	列表类型
	 * 			4		辖区归档列表
	 */
	private void formatMapDataOut(List<Map<String, Object>> eventList, Map<String, Object> params) {
		if(eventList != null && eventList.size() > 0) {
			Map<String, Object> dictMap = new HashMap<String, Object>();
			String userOrgCode = null;
			Integer listType = 0;
			List<BaseDataDict> influenceDegreeDictList = null,
							   urgencyDegreeDictList = null,
							   statusDictList = null,
							   subStatusDictList = null,
							   eventTypeDict = null,
							   evaLevelDictList = null,
							   sourceDictList = null,
							   patrolTypeDictList=null,
							   collectWayDictList = null,
							   emtTypeDictList = null;
			List<Node> workflowNodeList = null;
			boolean isCapCurNodeInfo = false,
					isCapCurHandlerName = false;

			if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
				userOrgCode = params.get("orgCode").toString();
			}
			
			if(CommonFunctions.isNotBlank(params, "listType")) {
				try {
					listType = Integer.valueOf(params.get("listType").toString());
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
			
			dictMap.put("orgCode", userOrgCode);
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			influenceDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, userOrgCode);
			urgencyDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, userOrgCode);
			statusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.STATUS_PCODE, userOrgCode);
			subStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SUB_STATUS_PCODE, userOrgCode);
			eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			sourceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.SOURCE_PCODE, userOrgCode);
			emtTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EMT_TYPE_PCODE, userOrgCode);
			
			Map<String,Object> eventLabelMap=new HashMap<String,Object>();
			if(CommonFunctions.isNotBlank(params, "searchEventLabel")&&CommonFunctions.isNotBlank(params, "labelModel")) {
				Map<String,Object> searchLabelParams=new HashMap<String,Object>();
				searchLabelParams.put("searchEventLabel", params.get("searchEventLabel").toString());
				searchLabelParams.put("labelModel", params.get("labelModel").toString());
				List<Map<String, Object>> eventLabelList = eventLabelService.searchListByParam(searchLabelParams);
				for (Map<String, Object> map : eventLabelList) {
					eventLabelMap.put(map.get("labelId").toString(), map.get("labelName").toString());
				}
			}
			
			switch(listType) {
				case 2: {//经办事件列表
					if(CommonFunctions.isNotBlank(params, "isCapCurHandlerName")) {
						isCapCurHandlerName = Boolean.valueOf(params.get("isCapCurHandlerName").toString());
					}
					break;
				}
				case 4: {//辖区归档事件列表
					evaLevelDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EVALUATE_LEVEL_PCODE, userOrgCode);
					break;
				}
				case 5: {//辖区所有事件列表
					if(CommonFunctions.isNotBlank(params, "isCapCurNodeInfo")) {
						isCapCurNodeInfo = Boolean.valueOf(params.get("isCapCurNodeInfo").toString());
					}
					if(CommonFunctions.isNotBlank(params, "isCapCurHandlerName")) {
						isCapCurHandlerName = Boolean.valueOf(params.get("isCapCurHandlerName").toString());
					}
					break;
				}
				case 9: {//辖区所有(展示当前环节)
					isCapCurNodeInfo = true;
					
					collectWayDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.COLLECT_WAY_PCODE, userOrgCode);
					patrolTypeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.PATROL_TYPE_PCODE, userOrgCode);
					if(CommonFunctions.isNotBlank(params, "isCapCurHandlerName")) {
						isCapCurHandlerName = Boolean.valueOf(params.get("isCapCurHandlerName").toString());
					}
					
					break;
				}
			}
			
			if(isCapCurNodeInfo) {
				Long workflowId = null;//为了让queryNodes找到对应的方法
				
				workflowNodeList = eventDisposalWorkflowService.queryNodes(workflowId);
			}
			
			for(Map<String, Object> eventMap : eventList) {
				userOrgCode = null; 
				
				// 设置事件分类全路径
				if(CommonFunctions.isNotBlank(eventMap, "type") && eventTypeDict != null) {
					Map<String, Object> eventTypeMap = DataDictHelper.capMultilevelDictInfo(eventMap.get("type").toString(), ConstantValue.BIG_TYPE_PCODE, eventTypeDict);
					
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictName")) {
						eventMap.put("typeName", eventTypeMap.get("dictName"));
					}
					if(CommonFunctions.isNotBlank(eventTypeMap, "dictFullPath")) {
						eventMap.put("eventClass", eventTypeMap.get("dictFullPath"));
					}
				}
				if(CommonFunctions.isNotBlank(eventMap, "curNodeName")) {
					if(workflowNodeList != null) {
						Long workflowId = -1L;
						String curNodeName = eventMap.get("curNodeName").toString();
						
						if(CommonFunctions.isNotBlank(eventMap, "workflowId")) {
							workflowId = Long.valueOf(eventMap.get("workflowId").toString());
						}
						
						//找到指定流程图中的节点，否则多张流程图中可能有同名的节点
						if(workflowId > 0) {
							for(Node workflowNode : workflowNodeList) {
								if(workflowId.equals(workflowNode.getWorkFlowId()) && curNodeName.equals(workflowNode.getNodeName())) {
									eventMap.put("curNodeNameZH", workflowNode.getNodeNameZH());
									break;
								}
							}
						}
					}
					
					//用当前环节名称是否存在，来判断流程是否已归档，已归档事件不进行人员转换
					if(isCapCurHandlerName) {
						Long instanceId = null;
						Map<String, Object> curDataMap = null;
						StringBuffer curHandlerName = new StringBuffer("");
						String[] userNameArray = null, orgNameArray = null;
						
						if(CommonFunctions.isNotBlank(eventMap, "instanceId")) {
							instanceId = Long.valueOf(eventMap.get("instanceId").toString());
						}
						
						if(instanceId != null && instanceId > 0) {
							curDataMap = eventDisposalWorkflowService.curNodeData(instanceId);
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_USERNAME_ALL")) {
								userNameArray = curDataMap.get("WF_USERNAME_ALL").toString().split(",");
							}
							
							if(CommonFunctions.isNotBlank(curDataMap, "WF_ORGNAME_ALL")) {
								orgNameArray = curDataMap.get("WF_ORGNAME_ALL").toString().split(",");
							}
						}
						
						if(userNameArray != null && orgNameArray != null) {
							for(int index = 0, userNameLen = userNameArray.length, orgNameLen = orgNameArray.length; index < userNameLen; index++) {
								curHandlerName.append(userNameArray[index]);
								
								if(orgNameLen > index) {
									curHandlerName.append("(").append(orgNameArray[index]).append(");");
								}
							}
						} else if(orgNameArray != null) {
							for(String orgName : orgNameArray) {
								curHandlerName.append("(").append(orgName).append(");");
							}
						}
						
						if(curHandlerName.length() > 0) {
							eventMap.put("curHandlerName", curHandlerName.toString());
						}
					}
				}
				
				// 影响范围
				DataDictHelper.setDictValueForField(eventMap, "influenceDegree", "influenceDegreeName", influenceDegreeDictList);
				
				// 紧急程度
				DataDictHelper.setDictValueForField(eventMap, "urgencyDegree", "urgencyDegreeName", urgencyDegreeDictList);
				
				// 事件状态
				DataDictHelper.setDictValueForField(eventMap, "status", "statusName", statusDictList);
				
				// 事件子状态
				DataDictHelper.setDictValueForField(eventMap, "subStatus", "subStatusName", subStatusDictList);
				
				// 事件巡访类型
				DataDictHelper.setDictValueForField(eventMap, "patrolType", "patrolTypeName", patrolTypeDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "statusName") && CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
					String subStatus = eventMap.get("subStatus").toString(),
						   subStatusName = eventMap.get("subStatusName").toString();
					
					if(ConstantValue.REJECT_SUB_STATUS.equals(subStatus)) {
						eventMap.put("statusName", subStatusName);
					} else if(CommonFunctions.isNotBlank(eventMap, "subStatusName")) {
						eventMap.put("statusName", eventMap.get("statusName") + "-" + subStatusName);
					}
				}
				
				// 评价等级
				DataDictHelper.setDictValueForField(eventMap, "evaLevel", "evaLevelName", evaLevelDictList);
				
				// 事件来源
				DataDictHelper.setDictValueForField(eventMap, "source", "sourceName", sourceDictList);
				
				// 采集方式
				DataDictHelper.setDictValueForField(eventMap, "collectWay", "collectWayName", collectWayDictList);
				
				//八员队伍
				DataDictHelper.setDictValueForField(eventMap, "emtType", "emtTypeName", emtTypeDictList);
				
				if(CommonFunctions.isNotBlank(eventMap, "happenTime")) {
					eventMap.put("happenTimeStr", DateUtils.formatDate((Date)eventMap.get("happenTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "handleDate")) {
					eventMap.put("handleDateStr", DateUtils.formatDate((Date)eventMap.get("handleDate"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "finTime")) {
					eventMap.put("finTimeStr", DateUtils.formatDate((Date)eventMap.get("finTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "createTime")) {
					eventMap.put("createTimeStr", DateUtils.formatDate((Date)eventMap.get("createTime"), DateUtils.PATTERN_24TIME));
				}
				if(CommonFunctions.isNotBlank(eventMap, "remindMark") && CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "3");
				} else if(CommonFunctions.isNotBlank(eventMap, "superviseMark")) {
					eventMap.put("remindStatus", "2");
				} else if(CommonFunctions.isNotBlank(eventMap, "remindMark")) {
					eventMap.put("remindStatus", "1");
				}
				if(CommonFunctions.isNotBlank(eventMap, "wfAttentionStatus")) {
					eventMap.put("isAttention", "1".equals(eventMap.get("wfAttentionStatus").toString()));
				}
				//去除网格全路径中的顶级路径
				if(CommonFunctions.isNotBlank(eventMap, "gridPath")) {
					eventMap.put("gridPath", CommonFunctions.replaceScopePath(eventMap.get("gridPath").toString(), cacheService));
				}
				
				//判断是否获取相关附件信息
				if(CommonFunctions.isNotBlank(params, "isGitAttr")) {
					if(Boolean.valueOf(params.get("isGitAttr").toString())) {
						List attrs=new ArrayList();
						try {
							attrs=attachmentService.findByBizId(Long.valueOf(eventMap.get("eventId").toString()), ConstantValue.EVENT_ATTACHMENT_TYPE);
						} catch (Exception e) {
							e.printStackTrace();
						}
						eventMap.put("attachments", attrs);
					}
				}

				
				//判断是否获取办结亮灯情况handleDate
				eventDisposalExpandService.expandFormatMapDataOut(eventMap,null,params);

				if(eventLabelMap.size()>0) {
					StringBuffer eventLabelName=new StringBuffer("");
					StringBuffer eventLabelIds=new StringBuffer("");
					try {
						List<Map<String, Object>> eventLabelRelaList = eventLabelRelaService.searchByEventId(Long.valueOf(eventMap.get("eventId").toString()), new HashMap<String,Object>());
						for (int i = 0, j = eventLabelRelaList.size();i<j; i++) {
							String key=eventLabelRelaList.get(i).get("LABEL_ID").toString();
							if(eventLabelMap.containsKey(key)) {
								if(i==(j-1)) {
									eventLabelName.append(eventLabelMap.get(key));
									eventLabelIds.append(key);
								}else {
									eventLabelName.append(eventLabelMap.get(key)+",");
									eventLabelIds.append(key+",");
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					eventMap.put("eventLabelName", eventLabelName.toString());
					eventMap.put("eventLabelIds", eventLabelIds.toString());
				}
				
			}
		}
	}

	
	/**
	 * 获取列表查询使用的mapper
	 * @param params	额外参数
	 * 			userOrgCode	组织编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Event4WorkflowMapper capMapper(Map<String, Object> params) {
		Class<Event4WorkflowMapper> classMapper = null;
		Event4WorkflowMapper eventMapper = null;
		String userOrgCode = null, mapperClassFullName = null;
		
		if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
			userOrgCode = params.get("userOrgCode").toString();
		} else if(CommonFunctions.isNotBlank(params, "orgCode")) {
			userOrgCode = params.get("orgCode").toString();
		}
		
		mapperClassFullName = funConfigurationService.turnCodeToValue(ConstantValue.WORKFLOW_ATTRIBUTE, ConstantValue.EVENT_4_WORKFLOW_MAPPER, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		
		if(StringUtils.isNotBlank(mapperClassFullName)) {
			try {
				classMapper = (Class<Event4WorkflowMapper>) Class.forName(mapperClassFullName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if(classMapper != null) {
			eventMapper = sqlSessionTemplate.getMapper(classMapper);
		} else {
			eventMapper = event4WorkflowMapper;
		}
		
		return eventMapper;
	}
}
