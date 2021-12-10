package cn.ffcs.zhsq.event.controller.eventImport;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.workflow.om.MyTask;
import cn.ffcs.workflow.om.Node;
import cn.ffcs.workflow.om.ProInstance;
import cn.ffcs.zhsq.ZZBaseController;
import cn.ffcs.zhsq.event.service.IEventDisposalDockingService;
import cn.ffcs.zhsq.event.service.IEventDisposalService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.mybatis.domain.event.EventDisposal;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.message.Msg;
import cn.ffcs.zhsq.utils.message.ResultObj;

@Controller
public abstract class ImportEventAbstractController extends ZZBaseController {
	/**
	 * 将导入失败的记录转换为导出内容
	 * @param eventMapList
	 * @return
	 */
	public abstract String exportFailEventContent(List<Map<String, Object>> eventMapList);
	/**
	 * 列表跳转url
	 * @return
	 */
	public abstract String fetchIndexUrl();
	/**
	 * 导入文件转换失败提示信息
	 * @return
	 */
	public abstract String fetchExportMsg();
	/**
	 * 导出文件名称
	 * @return
	 */
	public abstract String fetchExportName();
	
	@Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
	
	@Autowired
	private IEventDisposalDockingService eventDisposalDockingService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	private UserManageOutService userManageService;
	
	@Autowired
	private IBaseDictionaryService baseDictionaryService;
	
	@Autowired
	private IEventDisposalService eventDisposalService;
	
	//需要修改的环节名称
	private static final String COLLECT_TASK_CODE = "CJ";//采集事件环节编码
	//默认事件类型
	private static final String DEFAULT_EVENT_TYPE = "99";//其他
	//存放导入线程的操作结果
	protected static Map<String, Object> importResultMap = new HashMap<String, Object>();
	//导入失败事件文件名称
	protected static final String EXPORT_EXCEL_NAME = "导入失败事件";
	
	/**
	 * 跳转导入页面
	 * @param session
	 * @param request
	 * @param tipMsgType
	 * @param failCount
	 * @param succCount
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/toIndex")
	public String toIndex(HttpSession session,
			HttpServletRequest request, 
			@RequestParam(value = "threadId", required = false, defaultValue="-1") Long threadId,
			ModelMap map){
		if(threadId > 0) {
			ResultObj resultObj = null;
			int tipMsgType = 0;
			
			if(CommonFunctions.isNotBlank(importResultMap, threadId+"_tipMsgType")) {
				tipMsgType = Integer.valueOf(importResultMap.get(threadId+"_tipMsgType").toString());
			}
			
			switch(tipMsgType) {
				case 1: {//JDOMException异常
					resultObj = Msg.OPERATE.getResult(false, fetchExportMsg());
					break;
				}
				case 4: {//IOException异常
					resultObj = Msg.OPERATE.getResult(false, "文件读取失败！");
					break;
				}
			}
			
			if(resultObj != null) {
				map.addAttribute("resultObj", resultObj);
			}
			
			map.addAttribute("threadId", threadId);
		}
		
		return fetchIndexUrl();	
	}
	
	/**
	 * 获取事件记录导入进度
	 * @param threadId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkProgress", method = RequestMethod.POST)
	public Map<String, Object> checkProgress(
			@RequestParam(value = "threadId") Long threadId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_total")) {
			resultMap.put("total", importResultMap.get(threadId+"_total"));
		} else if(CommonFunctions.isNotBlank(importResultMap, threadId+"_isActive")) {
			//不将总数默认值设置为0，是因为在总数计算较慢时，会导致页面进度更新提前结束，因为0 = 0 + 0
			resultMap.put("total", -1);
		} else {
			resultMap.put("total", 0);
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_success")) {
			resultMap.put("success", importResultMap.get(threadId+"_success"));
		} else {
			resultMap.put("success", 0);
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_fail")) {
			resultMap.put("fail", importResultMap.get(threadId+"_fail"));
		} else {
			resultMap.put("fail", 0);
		}
		
		return resultMap;
	}
	
	/**
	 * 下载导入失败记录
	 * @param request
	 * @param response
	 * @param threadId
	 */
	@RequestMapping(value = "/downLoadFail")
	public void downLoadFail(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam(value = "threadId") Long threadId) {
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_failEventList")) {
			List<Map<String, Object>> failEventList = (List<Map<String, Object>>)importResultMap.get(threadId+"_failEventList");
			if(!failEventList.isEmpty()) {
				exportData(request, response, failEventList);
			}
		}
	}
	
	/**
	 * 清除线程变量
	 * @param threadId 线程编号
	 */
	@RequestMapping(value = "/delProgress")
	public void delProgress(@RequestParam(value = "threadId") Long threadId) {
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_total")) {
			importResultMap.remove(threadId+"_total");
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_success")) {
			importResultMap.remove(threadId+"_success");
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_fail")) {
			importResultMap.remove(threadId+"_fail");
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_failEventList")) {
			importResultMap.remove(threadId+"_failEventList");
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_tipMsgType")) {
			importResultMap.remove(threadId+"_tipMsgType");
		}
		
		if(CommonFunctions.isNotBlank(importResultMap, threadId+"_isActive")) {
			importResultMap.remove(threadId+"_isActive");
		}
	}
	
	/**
	 * 导出导入失败的事件
	 * @param request
	 * @param response
	 * @param exportContent	导出文件内容
	 */
	private void exportData(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> eventMapList) {
		String exportContent = exportFailEventContent(eventMapList);
		
        if(exportContent.length() > 0) {
        	PrintWriter out = null;
        	response.setCharacterEncoding("UTF-8");
        	
    		try {
    			response.setHeader("Content-disposition", "attachment;filename=" + fetchExportName());
    			out = response.getWriter();
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		try {
    			out.write(exportContent);
    			out.flush();
    		} finally {
    			out.close();
    		}
        }
	}
	
	/**
	 * 事件相关字段转换
	 * @param eventMapList
	 */
	private void dicChange(List<Map<String, Object>> eventMapList) {
		if(eventMapList != null && eventMapList.size() > 0) {
			MixedGridInfo defaultGridInfo = null;
			List<OrgSocialInfoBO> orgInfoList = null;
			List<UserBO> userBOList = null;
			OrgSocialInfoBO defaultOrgInfo = null;
			UserBO defaultUserBO = null;
			String infoOrgCode = "", bigTypeDicCode = null, type = null;
			String[] typeNameArray = null;
			Map<String, String> influenceMap = new HashMap<String, String>(),//影响范围
								urgencyMap = new HashMap<String, String>();//紧急程度
			
			Map<String, Object> dictMap = new HashMap<String, Object>();
			dictMap.put("dictPcode", ConstantValue.BIG_TYPE_PCODE);
			
			List<BaseDataDict> influenceDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.INFLUENCE_DEGREE_PCODE, null),
							   urgencyDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.URGENCY_DEGREE_PCODE, null),
							   eventTypeDict = baseDictionaryService.findDataDictListByCodes(dictMap);
			
			if(influenceDictList != null) {
				for(BaseDataDict dataDict : influenceDictList) {
					influenceMap.put(dataDict.getDictName(), dataDict.getDictGeneralCode());
				}
			}
			if(urgencyDictList != null) {
				for(BaseDataDict dataDict : urgencyDictList) {
					urgencyMap.put(dataDict.getDictName(), dataDict.getDictGeneralCode());
				}
			}
			
			for(Map<String, Object> eventMap : eventMapList) {
				if(CommonFunctions.isNotBlank(eventMap, "typeName")) {//事件类型，名称转换为字典业务编码，格式为：城市管理-市容管理
					typeNameArray = eventMap.get("typeName").toString().split("-");
					bigTypeDicCode = ConstantValue.BIG_TYPE_PCODE;
					type = null;
					
					for(String typeName : typeNameArray) {
						if(StringUtils.isNotBlank(typeName)) {
							for(BaseDataDict dataDict : eventTypeDict) {
								if(typeName.equals(dataDict.getDictName()) && bigTypeDicCode.equals(dataDict.getDictPcode())) {
									bigTypeDicCode = dataDict.getDictCode();
									type = dataDict.getDictGeneralCode();
									break;
								}
							}
						}
					}
					
					eventMap.put("type", type);
				}
				if(CommonFunctions.isBlank(eventMap, "type")) {
					eventMap.put("type", DEFAULT_EVENT_TYPE);//设置默认事件类别为"其他"
				}
				if(!influenceMap.isEmpty() && CommonFunctions.isNotBlank(eventMap, "influenceDegreeName")) {
					eventMap.put("influenceDegree", influenceMap.get(eventMap.get("influenceDegreeName")));
				}
				if(!urgencyMap.isEmpty() && CommonFunctions.isNotBlank(eventMap, "urgencyDegreeName")) {
					eventMap.put("urgencyDegree", urgencyMap.get(eventMap.get("urgencyDegreeName")));
				}
				if(CommonFunctions.isBlank(eventMap, "happenTimeStr")) {
					eventMap.put("happenTimeStr", DateUtils.getNow());
				}
				if(CommonFunctions.isBlank(eventMap, "finTimeStr")) {
					eventMap.put("finTimeStr", DateUtils.getNow());
				}
				if(CommonFunctions.isNotBlank(eventMap, "infoOrgCode")) {
					infoOrgCode = eventMap.get("infoOrgCode").toString();
					
					defaultGridInfo = mixedGridInfoService.getDefaultGridByOrgCode(infoOrgCode);
					
					if(defaultGridInfo != null) {
						eventMap.put("gridId", defaultGridInfo.getGridId());
						eventMap.put("gridName", defaultGridInfo.getGridName());
						eventMap.put("gridCode", infoOrgCode);
						
						if(CommonFunctions.isNotBlank(eventMap, "orgId")) {
							Long orgId = -1L;
							
							try {
								orgId = Long.valueOf(eventMap.get("orgId").toString());
							} catch(NumberFormatException e) {
								e.printStackTrace();
							}
							
							if(orgId > 0) {
								defaultOrgInfo = orgSocialInfoService.findByOrgId(orgId);
							}
						}
						
						if(defaultOrgInfo == null) {
							orgInfoList = orgSocialInfoService.findByLocationId(defaultGridInfo.getInfoOrgId());
							
							if(orgInfoList != null && orgInfoList.size() > 0) {
								defaultOrgInfo = orgInfoList.get(0);
							}
						}
						
						if(defaultOrgInfo != null) {
							eventMap.put("orgId", defaultOrgInfo.getOrgId());
							eventMap.put("orgCode", defaultOrgInfo.getOrgCode());
							
							if(CommonFunctions.isBlank(eventMap, "closeOrgName")) {
								eventMap.put("closeOrgName", defaultOrgInfo.getOrgName());
							}
							
							userBOList = userManageService.getUserListByUserExampleParamsOut(null, null, defaultOrgInfo.getOrgId());
							
							if(userBOList != null && userBOList.size() > 0) {
								defaultUserBO = userBOList.get(0);
								
								eventMap.put("userId", defaultUserBO.getUserId());
								
								if(CommonFunctions.isBlank(eventMap, "closeUserName")) {
									eventMap.put("closeUserName", defaultUserBO.getPartyName());
								}
								if(CommonFunctions.isBlank(eventMap, "contactUser")) {
									eventMap.put("contactUser", defaultUserBO.getPartyName());
									if(CommonFunctions.isBlank(eventMap, "tel")) {
										eventMap.put("tel", defaultUserBO.getVerifyMobile());
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 办理事件
	 * @param eventMapList
	 * @return 导入失败的事件
	 */
	protected List<Map<String, Object>> handleEvent(List<Map<String, Object>> eventMapList, Long threadId) {
		List<Map<String, Object>> failEventList = new ArrayList<Map<String, Object>>();
		int total = 0;
		
		if(eventMapList != null) {
			total = eventMapList.size();
		}
		
		//可能导入空文件
		if(CommonFunctions.isBlank(importResultMap, threadId+"_total")) {
			importResultMap.put(threadId+"_total", total);
		}
		
		if(total > 0) {
			dicChange(eventMapList);
			
			total = eventMapList.size();
			EventDisposal event = null;
			boolean isEventValid = true;
			UserInfo userInfo = new UserInfo();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			
			List<MyTask> myTaskList = null;
			Map<String, Object> taskDoneMap = new HashMap<String, Object>(),
								taskMap = new HashMap<String, Object>();
			MyTask myTask = null;
			
			boolean result = true;
			Long eventId = -1L, instanceIdL = -1L;
			String instanceId = "";
			StringBuffer msgWrong = new StringBuffer("");
			int success = 0, fail = 0;
			
			for(Map<String, Object> eventMap : eventMapList) {
				msgWrong.setLength(0);
				
				event = eventDisposalDockingService.jsonToEvent(eventMap);
				msgWrong = new StringBuffer("");
				
				try {
					isEventValid = isEventValid(event, eventMap);
				} catch (ZhsqEventException e) {
					isEventValid = false;
					msgWrong.append(e.getMessage());
					e.printStackTrace();
				}
				
				if(isEventValid) {
					resultMap = new HashMap<String, Object>();
					userInfo = new UserInfo();
					
					if(CommonFunctions.isNotBlank(eventMap, "userId")) {
						userInfo.setUserId((Long)eventMap.get("userId"));
					}
					
					userInfo.setPartyName(eventMap.get("closeUserName").toString());
					
					if(CommonFunctions.isNotBlank(eventMap, "orgId")) {
						userInfo.setOrgId((Long)eventMap.get("orgId"));
					}
					if(CommonFunctions.isNotBlank(eventMap, "orgCode")) {
						userInfo.setOrgCode(eventMap.get("orgCode").toString());
					}
					
					resultMap = eventDisposalDockingService.saveEventDisposalAndEvaluate(event, userInfo, event.getResult());
					
					if(CommonFunctions.isNotBlank(resultMap, "result")) {
						result = Boolean.valueOf(resultMap.get("result").toString());
						eventId = -1L;
						instanceId = "";
						
						if(CommonFunctions.isNotBlank(resultMap, "instanceId")) {
							instanceId = resultMap.get("instanceId").toString();
							
							try {
								instanceIdL = Long.valueOf(instanceId);
							} catch(NumberFormatException e) {
								instanceIdL = -1L;
								e.printStackTrace();
							}
						}
						
						if(result && instanceIdL > 0) {//更新事件采集任务的相关属性
							
							/*try {//暂停2秒，用于帮助任务更新成功
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}*/
							
							if(CommonFunctions.isNotBlank(resultMap, "eventId")) {
								eventId = Long.valueOf(resultMap.get("eventId").toString());
								
								//更新处理时限
								EventDisposal eventTmp = eventDisposalService.findEventByIdSimple(eventId),
											  eventTemp = new EventDisposal();
								Date createTime = eventTmp.getCreateTime(), handleDate = eventTmp.getHandleDate();
								
								if(createTime != null && handleDate != null) {
									eventTemp.setEventId(eventId);
									eventTemp.setHandleDate(new Date(createTime.getTime() + handleDate.getTime() - new Date().getTime()));
									eventDisposalService.updateEventDisposalById(eventTemp);
								}
							}
							
							//获取事件采集节点任务
							Node collectNode = capCollectNode(instanceIdL, COLLECT_TASK_CODE);
							
							if(collectNode != null) {
								taskDoneMap = eventDisposalWorkflowService.capDoneTaskInfo(instanceIdL, collectNode.getNodeName());
							}
							
							if(CommonFunctions.isNotBlank(taskDoneMap, "tasks")) {
								taskMap = new HashMap<String, Object>();
								
								myTaskList = (List<MyTask>)taskDoneMap.get("tasks");
								
								if(myTaskList != null && myTaskList.size() > 0) {//变更事件采集环节任务信息
									myTask = myTaskList.get(0);
									taskMap.put("taskId", myTask.getTaskId());
									taskMap.put("taskName", myTask.getTaskName());
									taskMap.put("transactorName", eventMap.get("closeUserName"));
									taskMap.putAll(resultMap);
									taskMap.put("transactOrgName", eventMap.get("closeOrgName"));
									taskMap.put("taskAdvice", eventMap.get("result"));
									taskMap.put("taskCreateTime", eventMap.get("happenTimeStr"));
									taskMap.put("taskReadTime", eventMap.get("happenTimeStr"));
									taskMap.put("taskEndTime", eventMap.get("finTimeStr"));
									
									eventDisposalWorkflowService.saveOrUpdateTask(taskMap);
								}
							}
						} else {
							if(eventId < 0) {
								msgWrong.append("事件新增失败！");
							} else if(StringUtils.isBlank(instanceId)) {
								msgWrong.append("事件流程启动失败，事件编号为[").append(eventId).append("]！");
							}
						}
					}
				}
				
				if(msgWrong.length() > 0) {
					eventMap.put("msg", msgWrong);
					
					failEventList.add(eventMap);
					
					if(CommonFunctions.isNotBlank(importResultMap, threadId+"_fail")) {
						fail = Integer.valueOf(importResultMap.get(threadId+"_fail").toString());
					}
					
					importResultMap.put(threadId+"_fail", ++fail);
				} else {
					if(CommonFunctions.isNotBlank(importResultMap, threadId+"_success")) {
						success = Integer.valueOf(importResultMap.get(threadId+"_success").toString());
					}
					
					importResultMap.put(threadId+"_success", ++success);
				}
			}
		}
		
		if(!failEventList.isEmpty()) {
			importResultMap.put(threadId+"_failEventList", failEventList);
		}
		
		return failEventList;
	}
	
	/**
	 * 验证事件字段是否有效
	 * @param event
	 * @return
	 * @throws ZhsqEventException
	 */
	private boolean isEventValid(EventDisposal event, Map<String, Object> eventMap) throws ZhsqEventException{
		boolean isValid = true;
		
		if(event != null) {
			Long gridId = event.getGridId();
			Date happenTime = event.getHappenTime(),
				 finTime = event.getFinTime();
			StringBuffer msgWrong = new StringBuffer("");
			
			if(CommonFunctions.isBlank(eventMap, "infoOrgCode")) {
				msgWrong.append("所属网格不得为空！");
			} else if(gridId == null || gridId < 0) {
				msgWrong.append("所属网格[").append(eventMap.get("infoOrgCode")).append("] 不是有效的地域编码！");
			} else {//网格有效
				if(CommonFunctions.isBlank(eventMap, "orgId")) {
					msgWrong.append("所属网格[").append(event.getGridCode()).append("][").append(gridId).append("] 未能找到有效的组织信息！");
				} else if(CommonFunctions.isBlank(eventMap, "userId")) {//组织有效 用户无效
					msgWrong.append("所属网格[").append(event.getGridCode())
							.append("][").append(gridId).append("] 对应的组织信息[")
							.append(eventMap.get("orgId"))
							.append("] 未能找到有效的用户信息！");
				}
			}
			
			if(happenTime == null) {
				if(CommonFunctions.isNotBlank(eventMap, "happenTimeStr")) {
					msgWrong.append("事发时间[").append(eventMap.get("happenTimeStr")).append("] 不是有效的时间， 时间格式需为：YYYY-MM-DD HH:MM:SS！");
				} else {
					msgWrong.append("事发时间不得为空！");
				}
			} else {
				String createTimeStr = event.getCreateTimeStr();
				Date createTime = event.getCreateTime();
				
				if(StringUtils.isBlank(createTimeStr) && createTime == null) {//采集时间为空时，使用事发时间
					event.setCreateTime(happenTime);//将事件采集事件设置为事发时间
				}
			}
			
			if(finTime == null) {
				if(CommonFunctions.isNotBlank(eventMap, "finTimeStr")) {
					msgWrong.append("结案时间[").append(eventMap.get("finTimeStr")).append("] 不是有效的时间， 时间格式需为：YYYY-MM-DD HH:MM:SS！");
				} else {
					msgWrong.append("结案时间不得为空！");
				}
			}
			
			int eventNameLength = 100,
				contentLength = 4000,
				occurredLength = 255,
				resultLength = 1000;
			
			if(!CommonFunctions.isLengthValidate(eventMap, "eventName", eventNameLength)) {
				msgWrong.append("事件标题的输入长度超过了[").append(eventNameLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "content", contentLength)) {
				msgWrong.append("事件描述的输入长度超过了[").append(contentLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "occurred", occurredLength)) {
				msgWrong.append("事发地址的输入长度超过了[").append(occurredLength).append("]个字符！");
			}
			if(!CommonFunctions.isLengthValidate(eventMap, "result", resultLength)) {
				msgWrong.append("事件办理结果的输入长度超过了[").append(resultLength).append("]个字符！");
			}
			
			if(msgWrong.length() > 0) {
				isValid = false;
				throw new ZhsqEventException(msgWrong.toString());
			}
		}
		
		return isValid;
	}
	
	/**
	 * 通过节点编码查找对应的节点
	 * @param instanceId	流程事件
	 * @param nodeCode		节点编码
	 * @return
	 */
	private Node capCollectNode(Long instanceId, String nodeCode) {
		ProInstance proInstance = eventDisposalWorkflowService.capProInstanceByInstanceId(instanceId);
		Node collectNode = null;
		
		if(proInstance != null) {
			List<Node> nodeList = eventDisposalWorkflowService.queryNodes(proInstance.getWorkFlowId());
			if(nodeList != null) {
				for(Node node : nodeList) {
					if(nodeCode.equals(node.getNodeCode())) {
						collectNode = node; break;
					}
				}
			}
		}
		
		return collectNode;
	}
	
}
