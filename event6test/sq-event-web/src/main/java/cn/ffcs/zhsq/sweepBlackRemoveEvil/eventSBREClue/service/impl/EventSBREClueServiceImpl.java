package cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.zhsq.typeRela.service.ITypeRelaService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.gmis.mybatis.domain.prvetionTeam.PrvetionTeam;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.zzgl.service.attachment.IAttachmentService;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.MessageOutService;
import cn.ffcs.zhsq.event.service.IEventDisposalWorkflowService;
import cn.ffcs.zhsq.event.service.IInvolvedPeopleService;
import cn.ffcs.zhsq.moduleRela.service.IModuleRelaService;
import cn.ffcs.zhsq.mybatis.domain.event.InvolvedPeople;
import cn.ffcs.zhsq.mybatis.domain.moduleRela.ModuleRela;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREClue.EventSBREClue;
import cn.ffcs.zhsq.mybatis.domain.sweepBlackRemoveEvil.eventSBREvilGang.EventSBREvilGang;
import cn.ffcs.zhsq.mybatis.persistence.sweepBlackRemoveEvil.eventSBREClue.EventSBREClueMapper;
import cn.ffcs.zhsq.selfDefinedException.ZhsqEventException;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREClue.service.IEventSBREClueService;
import cn.ffcs.zhsq.sweepBlackRemoveEvil.eventSBREvilGang.service.IEventSBREvilGangService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.DataDictHelper;
import cn.ffcs.zhsq.utils.DateUtils;

import com.alibaba.fastjson.JSON;

/**
 * Created by 张天慈 on 2018/05/18.
 */
@Service("eventSBREClueServiceImpl")
@Transactional
public class EventSBREClueServiceImpl extends EventSBREClueWorkflowServiceImpl implements IEventSBREClueService {
    @Autowired
    private EventSBREClueMapper eventSBREClueMapper;
    @Autowired
    private IModuleRelaService moduleRelaService;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private IBaseDictionaryService baseDictionaryService;
    @Autowired
    private IInvolvedPeopleService involvedPeopleService;
    @Autowired
    private IEventSBREvilGangService eventSBREvilGangService;
    //线索服务
    @Autowired
    private IEventSBREClueService clueService;
    @Autowired
	private IEventDisposalWorkflowService eventDisposalWorkflowService;
    @Autowired
	private MessageOutService messageService;
    @Autowired
	private IFunConfigurationService funConfigurationService;

    @Autowired
    private ITypeRelaService typeRelaService;


    //@Autowired
    //private IPrvetionTeamService prvetionTeamService;

    private String MODULECODELEFT = "001",						//左方模块编码，001 扫黑除恶_线索管理
    			   MODULECODERIGHT = "001",						//右方模块编码，001 扫黑除恶_黑恶团伙管理
    			   CLUE_STATUS_DRAFT = "99",					//线索状态-草稿
    			   CLUE_STATUS_REPORTED = "00",					//线索状态-已上报
    			   CLUE_STATUS_TRANSFER = "01",					//线索状态-流转中
    			   CLUE_STATUS_CHECK = "02",					//线索状态-待核查
    			   CLUE_STATUS_FEEDBACK = "03",					//线索状态-已核查待处置
    			   CLUE_STATUS_EVA = "07",						//线索状态-待评价
    			   CLUE_STATUS_END = "04",						//线索状态-结束
    			   ATTACHMENT_TYPE = "eventSBREClueAttachment",	//附件类型
                   NEED_ENCRYPT = "1",							//加密
                   DISPOSAL_METHOD_REGISTER = "01",				//处置方式-立案
                   DISPOSAL_METHOD_PUNISH = "02",				//处置方式-行政处罚
                   DISPOSAL_METHOD_TRANSFER = "03",				//处置方式-移交其他机关处理
                   DISPOSAL_METHOD_OTHER = "99",				//处置方式-其他
                   CLUESTATUS_PCODE ="B591005",					//线索状态字典编码
                   INVOLVE_ATTACK_FOCUS_PCODE ="B591006",		//涉及打击重点字典编码
                   TYPERELA_BIZ_TYPE = "01";		     	    //关联业务类型，01 扫黑除恶_涉及打击重点统计分析


    @Override
    public Long saveClue(EventSBREClue clue, Map<String, Object> param, UserInfo userInfo) throws Exception {
        Long clueId = clue.getClueId();

        //格式化参数
        this.formatEntityDataIn(clue, userInfo);

        //登记人员
        if (clue.getCreatorId() == null) {
            clue.setCreatorId(userInfo.getUserId());
        }
        //设置默认线索状态
        if (StringUtils.isBlank(clue.getClueStatus())) {
            clue.setClueStatus(CLUE_STATUS_DRAFT);
        }

        if (eventSBREClueMapper.insert(clue) > 0) {
            clueId = clue.getClueId();
        }

        //保存线索关联记录--.判断有无关联的黑恶团伙，有则保存并新增关联记录；
        if (clueId > 0) {
            this.saveExtraInfo(clueId, param, userInfo);
        }

        return clueId;
    }

    @Override
    public Boolean deleteByClueId(Long clueId, Long userId) {
    	boolean flag = false;
    	
    	if(clueId != null && clueId > 0) {
    		flag = eventSBREClueMapper.deleteByClueId(clueId, userId) > 0;
    		
    		if(flag) {
    			batchReadMessage(clueId);

    			//删除关联记录
                try {
                    Map<String,Object> param = new HashMap<>();
                    param.put("bizId",clueId);
                    param.put("bizType",TYPERELA_BIZ_TYPE);
                    typeRelaService.delTypeRela(param);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    		}
    	}
        
        return flag;
    }

    @Override
    public Boolean updateClue(EventSBREClue clue, Map<String, Object> params, UserInfo userInfo) throws Exception {
        boolean flag = false;
        StringBuffer msgWrong = new StringBuffer("");

        if (clue != null) {
        	Long clueId = clue.getClueId();
            
        	if(clueId != null && clueId > 0) {
        		EventSBREClue clueTmp = findByClueId(clueId);
        		
        		if(clueTmp != null) {
        			String clueStatus = clueTmp.getClueStatus();
        			if(CLUE_STATUS_END.equals(clueStatus)) {
        				msgWrong.append("该线索状态为【").append(baseDictionaryService.changeCodeToName(CLUESTATUS_PCODE, clueStatus, userInfo.getOrgCode())).append("】，不可编辑！");
        			}
        		} else {
        			msgWrong.append("clueId[").append(clueId).append("]没有对应有效的线索信息！");
        		}
        	} else {
        		msgWrong.append("缺少属性[clueId]！");
        	}
            
        	if(msgWrong.length() > 0) {
        		throw new Exception(msgWrong.toString());
        	} else {
	            //格式化参数
	            this.formatEntityDataIn(clue, userInfo);
	            
	            flag = eventSBREClueMapper.update(clue) > 0;
	            
	            //保存线索关联记录--.判断有无关联的黑恶团伙，有则保存并新增关联记录；
	            if (flag) {
	                this.saveExtraInfo(clue.getClueId(), params, userInfo);
	            }
        	}
        }
        
        return flag;
    }

    @Override
    public Map<String, Object> findByClueId(Long clueId, Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        if (clueId != null && clueId > 0) {
        	EventSBREClue clue = findByClueId(clueId);

            if(clue != null) {
            	String userOrgCode = null,
            		   isEncrypt = clue.getIsEncrypt();
            	
            	//线索详情加密权限验证
            	if(NEED_ENCRYPT.equals(isEncrypt)) {
            		boolean isCheckEncrypt = false;
            		if(CommonFunctions.isNotBlank(params, "isCheckEncrypt")) {
            			isCheckEncrypt = Boolean.valueOf(params.get("isCheckEncrypt").toString());
            		}
            		
            		if(isCheckEncrypt) {
            			StringBuffer msgWrong = new StringBuffer("");
            			Long encryptUserId = -1L, encryptOrgId = -1L;
            			
            			if(CommonFunctions.isNotBlank(params, "encryptUserId")) {
            				try {
            					encryptUserId = Long.valueOf(params.get("encryptUserId").toString());
            				} catch(NumberFormatException e) {
            					msgWrong.append("属性encryptUserId[").append(params.get("encryptUserId")).append("]不能转换为有效的Long型数据！");
            					e.printStackTrace();
            				}
            			} else {
            				msgWrong.append("缺少属性[encryptUserId]！");
            			}
            			if(CommonFunctions.isNotBlank(params, "encryptOrgId")) {
            				try {
            					encryptOrgId = Long.valueOf(params.get("encryptOrgId").toString());
            				} catch(NumberFormatException e) {
            					msgWrong.append("属性encryptOrgId[").append(params.get("encryptOrgId")).append("]不能转换为有效的Long型数据！");
            					e.printStackTrace();
            				}
            			} else {
            				msgWrong.append("缺少属性[encryptOrgId]！");
            			}
            			
            			if(msgWrong.length() == 0) {
            				UserInfo userInfo = new UserInfo();
            				userInfo.setUserId(encryptUserId);
            				userInfo.setOrgId(encryptOrgId);
            				
            				this.checkAuthority(clue, userInfo);
            			}
            			
            			if(msgWrong.length() > 0) {
            				throw new Exception(msgWrong.toString());
            			}
            		}
            	}
            	
	            if(CommonFunctions.isNotBlank(params, "userOrgCode")) {
	            	userOrgCode = params.get("userOrgCode").toString();
	            }

	            formatDetailDataOut(clue, userOrgCode);

				resultMap.put("clue", clue);
				
				resultMap.putAll(this.capExtraInfo(clue, params));
            }
        }

        return resultMap;
    }

    /**
     * 依据线索id获取线索信息
     * @param clueId	线索id
     * @return
     */
    private EventSBREClue findByClueId(Long clueId) {
    	EventSBREClue clue = null;
    	
    	if(clueId != null && clueId > 0) {
    		clue = eventSBREClueMapper.findById(clueId);
    	}
    	
    	return clue;
    }
    
    @Override
    public boolean checkAuthority(Long clueId, UserInfo userInfo) throws Exception {
    	boolean authorCheckFlag = false;
    	
    	if(clueId != null && clueId > 0) {
    		EventSBREClue clue = findByClueId(clueId);
    		
    		if(clue != null) {
    			authorCheckFlag = checkAuthority(clue, userInfo);
    		}
    	}
    	
    	return authorCheckFlag;
    }
    
    @Override
    public boolean checkAuthority(EventSBREClue clue, UserInfo userInfo) throws Exception {
    	boolean authorCheckFlag = false;
    	String isEncrypt = null;
    	
    	if(clue != null) {
    		isEncrypt = clue.getIsEncrypt();
    	}
    	
    	//线索详情加密权限验证
    	if(NEED_ENCRYPT.equals(isEncrypt)) {
			StringBuffer msgWrong = new StringBuffer("");
			Long encryptUserId = userInfo.getUserId(), encryptOrgId = userInfo.getOrgId();
			
			if(msgWrong.length() == 0) {
				String clueStatus = clue.getClueStatus();
				Long clueCreatorId = clue.getCreatorId(),
					 clueId = clue.getClueId();
				
				if(CLUE_STATUS_DRAFT.equals(clueStatus)) {//判断当前用户是否为线索的创建人员
					authorCheckFlag = encryptUserId.equals(clueCreatorId);
				} else {
					//判断当前用户是否为线索的当前办理人员
					authorCheckFlag = this.isCurTaskPaticipant(clueId, userInfo, null);
					
					if(!authorCheckFlag) {
						//判断当前用户是否为线索的经办人员
						authorCheckFlag = this.isHandledPerson(clueId, null, encryptUserId, encryptOrgId);
					}
				}
				
				if(!authorCheckFlag) {
					msgWrong.append("该线索为加密线索，您无权查看线索详情！");
				}
			}
			
			if(msgWrong.length() > 0) {
				throw new Exception(msgWrong.toString());
			}
    	}
    	
    	return authorCheckFlag;
    }
    
    @Override
    public EUDGPagination findCluePagination(int pageNo, int pageSize, Map<String, Object> params, UserInfo userInfo) {
        int listType = 1;
        
        if (CommonFunctions.isNotBlank(params, "listType")) {
            try {
                listType = Integer.valueOf(params.get("listType").toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        EUDGPagination eventCluePagination = null;

        //格式化查询参数
        this.formatQueryData(params, userInfo);

        switch (listType) {
            case 1: {//草稿列表
                eventCluePagination = this.findEventSBRECluePagination4Draft(pageNo, pageSize, params);
                break;
            }
            case 2: {//待办列表
                eventCluePagination = this.findEventSBRECluePagination4Todo(pageNo, pageSize, params);
                break;
            }
            case 3: {//经办列表
                eventCluePagination = this.findEventSBRECluePagination4Handled(pageNo, pageSize, params);
                break;
            }
            case 4: {//辖区所有
                eventCluePagination = this.findEventSBRECluePagination4Jurisdiction(pageNo, pageSize, params);
                break;
            }
            case 5: {//我发起的列表
            	eventCluePagination = this.findEventSBRECluePagination4Initiator(pageNo, pageSize, params);
                break;
            }
        }

        if (eventCluePagination == null) {
            eventCluePagination = new EUDGPagination();
            eventCluePagination.setTotal(0);
            eventCluePagination.setRows(new ArrayList<EventSBREClue>());
        }

        return eventCluePagination;
    }

    @Override
    public int findClueCount(Map<String,Object> params, UserInfo userInfo) {
    	int listType = 1,
    		count = 0;
    	
        if (CommonFunctions.isNotBlank(params, "listType")) {
            try {
                listType = Integer.valueOf(params.get("listType").toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        //格式化查询参数
        this.formatQueryData(params, userInfo);
    	
        switch (listType) {
	        case 1: {//草稿列表
	        	count = this.findClueCount4Draft(params);
	            break;
	        }
	        case 2: {//待办列表
	        	count = this.findClueCount4Todo(params);
	            break;
	        }
	        case 3: {//经办列表
	        	count = this.findClueCount4Handled(params);
	            break;
	        }
	        case 4: {//辖区所有
	        	count = this.findClueCount4Jurisdiction(params);
	            break;
	        }
	        case 5: {//我发起的
	        	count = this.findClueCount4Initiator(params);
	        	break;
	        }
	    }
    	
    	return count;
    }
    
    /**
     * 分页获取线索草稿列表
     * @param pageNo	页码
     * @param pageSize	每页记录数
     * @param params
     * 			creatorId	线索创建者id
     * @return
     */
    private EUDGPagination findEventSBRECluePagination4Draft(int pageNo, int pageSize,
                                                             Map<String, Object> params) {
        String userOrgCode = null;
        int count = findClueCount4Draft(params);
        List<EventSBREClue> eventSBREClueList = new ArrayList<>();

        if (count > 0) {
        	pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 20 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
            
            eventSBREClueList = eventSBREClueMapper.findPageListByCriteria(params, rowBounds);

            if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
                userOrgCode = params.get("userOrgCode").toString();
            }
            this.formatListDataOut(eventSBREClueList, userOrgCode);
        }

        EUDGPagination eventCluePagination = new EUDGPagination(count, eventSBREClueList);

        return eventCluePagination;
    }
    
    /**
     * 统计线索草稿数量
     * @param params
     * 			creatorId	线索创建者id
     * @return
     */
    private int findClueCount4Draft(Map<String, Object> params) {
		int count = eventSBREClueMapper.findCountByCriteria(params);
		
		return count;
	}

    /**
     * 分页获取线索待办列表
     * @param pageNo	页码
     * @param pageSize	每页记录数
     * @param params
     * 			curUserId	当前办理人员id，数据类型String
     * 			curOrgId	当前办理组织id，数据类型String
     * @return
     */
    private EUDGPagination findEventSBRECluePagination4Todo(int pageNo, int pageSize,
                                                            Map<String, Object> params) {
        String userOrgCode = null;
        int count = findClueCount4Todo(params);
        List<EventSBREClue> eventSBREClueList = new ArrayList<>();

        if (count > 0) {
        	pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 20 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
            
            eventSBREClueList = eventSBREClueMapper.findPageList4Todo(params, rowBounds);

            if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
                userOrgCode = params.get("userOrgCode").toString();
            }
            this.formatListDataOut(eventSBREClueList, userOrgCode);
        }

        EUDGPagination eventCluePagination = new EUDGPagination(count, eventSBREClueList);

        return eventCluePagination;
    }
    
    /**
     * 统计线索待办数量
     * @param params
     * 			curUserId	当前办理人员id，数据类型String
     * 			curOrgId	当前办理组织id，数据类型String
     * @return
     */
    private int findClueCount4Todo(Map<String, Object> params) {
    	int count = eventSBREClueMapper.findCount4Todo(params);
    	
    	return count;
    }

    /**
     * 分页获取线索经办列表
     * @param pageNo	页码
     * @param pageSize	每页记录数
     * @param params
     * 			handledUserId	经办人员id，数据类型String
     * 			handledOrgId	经办组织id，数据类型String
     * @return
     */
    private EUDGPagination findEventSBRECluePagination4Handled(int pageNo, int pageSize,
                                                               Map<String, Object> params) {
        String userOrgCode = null;
        int count = findClueCount4Handled(params);
        List<EventSBREClue> eventSBREClueList = new ArrayList<>();

        if (count > 0) {
        	pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 20 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
            
            eventSBREClueList = eventSBREClueMapper.findPageList4Handled(params, rowBounds);

            if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
                userOrgCode = params.get("userOrgCode").toString();
            }
            this.formatListDataOut(eventSBREClueList, userOrgCode);
        }

        EUDGPagination eventCluePagination = new EUDGPagination(count, eventSBREClueList);

        return eventCluePagination;
    }

    /**
     * 统计线索经办数量
     * @param params
     * 			handledUserId	经办人员id，数据类型String
     * 			handledOrgId	经办组织id，数据类型String
     * @return
     */
    private int findClueCount4Handled(Map<String, Object> params) {
    	int count = eventSBREClueMapper.findCount4Handled(params);
    	
    	return count;
    }
    
    /**
     * 分页获取线索辖区所有列表
     * @param pageNo	页码
     * @param pageSize	每页记录数
     * @param params
     * 			infoOrgCode	地域编码
     * @return
     */
    private EUDGPagination findEventSBRECluePagination4Jurisdiction(int pageNo, int pageSize,
                                                                    Map<String, Object> params) {
        String userOrgCode = null;
        int count = 0;
        List<EventSBREClue> eventSBREClueList = new ArrayList<>();
        
        count = findClueCount4Jurisdiction(params);
        
        if (count > 0) {
        	pageNo = pageNo < 1 ? 1 : pageNo;
            pageSize = pageSize < 1 ? 20 : pageSize;
            RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
            
            eventSBREClueList = eventSBREClueMapper.findPageList4Jurisdiction(params, rowBounds);

            if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
                userOrgCode = params.get("userOrgCode").toString();
            }
            this.formatListDataOut(eventSBREClueList, userOrgCode);
        }

        EUDGPagination eventCluePagination = new EUDGPagination(count, eventSBREClueList);

        return eventCluePagination;
    }
    /**
     * 获取线索辖区所有列表 不分页
     * @param params
     * @return
     */
    public List<Map<String, Object>>  findEventSBREClueList(Map<String, Object> params, UserInfo userInfo) {
    	List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
    	 String userOrgCode = null;
    	 //格式化查询参数
         this.formatQueryData(params, userInfo);
         List<EventSBREClue> eventSBREClueList=eventSBREClueMapper.findEventSBREClueList(params);
    	 if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
             userOrgCode = params.get("userOrgCode").toString();
         }
	 	 params.put("isCapGangInfo", false);//是否获取黑恶团伙信息，true为获取；默认为false
    	 params.put("isCapInformantInfo", true);//是否获取举报人信息，true为获取；默认为false
    	 params.put("isCapReportedInfo", true);//是否获取被举报人信息，true为获取；默认为false
    	 params.put("isCapDisposeUnit", false);//是否获取综治单位新，true为获取；默认为false
        	
         Map<String, Object> map=null;
    	 for (EventSBREClue eventSBREClue : eventSBREClueList) {
    		 map=new HashMap<String, Object>();
    		 Long instanceId =null;
    		 try {
				instanceId = clueService.capInstanceId(eventSBREClue.getClueId(), userInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, Object> task2=null,task3=null;
			if(instanceId!=null &&instanceId > 0) {
				List<Map<String, Object>> queryTaskList = eventDisposalWorkflowService.queryProInstanceDetail(instanceId, IEventDisposalWorkflowService.SQL_ORDER_ASC, userInfo);
				for (Map<String, Object> queryTask : queryTaskList) {
					if(queryTask.get("TASK_CODE").equals("task2")){//线索流转
						task2=getTask(task2, queryTask);
					}else if(queryTask.get("TASK_CODE").equals("task3")){//线索核查
						task3=getTask(task3, queryTask);
					}
				}
				map.put("task2", task2);
				map.put("task3", task3);
			 }
    		 formatDetailDataOut(eventSBREClue, userOrgCode);
    		 map.put("clue", eventSBREClue);
    		 map.putAll(this.capExtraInfo(eventSBREClue, params));
    		 list.add(map);
		 }
    	 return list;
    	
    }
    
    
    private Map<String, Object> getTask(Map<String, Object> task,Map<String, Object> queryTask){
    	try {
			if(task==null){
				task=new HashMap<String, Object>();
				task.put("ORG_NAME", queryTask.get("ORG_NAME"));
				task.put("TRANSACTOR_NAME", queryTask.get("TRANSACTOR_NAME"));
				task.put("END_TIME",  queryTask.get("END_TIME")==null?"":DateUtils.formatDate(queryTask.get("END_TIME").toString(), DateUtils.PATTERN_DATE));
				task.put("REMARKS", queryTask.get("REMARKS"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return task;
    	
    }

    /**
     * 统计辖区内线索数量
     * @param params
     * 			infoOrgCode	地域编码
     * @return
     */
    private int findClueCount4Jurisdiction(Map<String, Object> params) {
    	int count = eventSBREClueMapper.findCount4Jurisdiction(params);
    	
    	return count;
    }
    
    /**
     * 分页获取我发起的数据列表
     * @param pageNo	页面
     * @param pageSize	每页记录数
     * @param params
     * 			initiatorId 	发起人员id 
     * 			initiatorOrgId 	发起人员所属组织id
     * @return
     */
    private EUDGPagination findEventSBRECluePagination4Initiator(int pageNo, int pageSize,
            Map<String, Object> params) {
		String userOrgCode = null;
		int count = 0;
		List<EventSBREClue> eventSBREClueList = new ArrayList<>();
		
		count = findClueCount4Initiator(params);
		
		if (count > 0) {
			pageNo = pageNo < 1 ? 1 : pageNo;
			pageSize = pageSize < 1 ? 20 : pageSize;
			RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
			
			eventSBREClueList = eventSBREClueMapper.findPageList4Initiator(params, rowBounds);
			
			if (CommonFunctions.isNotBlank(params, "userOrgCode")) {
				userOrgCode = params.get("userOrgCode").toString();
			}
			this.formatListDataOut(eventSBREClueList, userOrgCode);
		}
		
		EUDGPagination eventCluePagination = new EUDGPagination(count, eventSBREClueList);
		
		return eventCluePagination;
	}
		    
    /**
     * 统计我发起的线索数量
     * @param params
     * 			initiatorId 	发起人员id 
     * 			initiatorOrgId 	发起人员所属组织id
     * @return
     */
    private int findClueCount4Initiator(Map<String, Object> params) {
    	int count = eventSBREClueMapper.findCount4Initiator(params);
    	
    	return count;
    }
    
    @Override
	public boolean startWorkflow4Clue(Long clueId, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
    	boolean flag = super.startWorkflow4Clue(clueId, userInfo, extraParam);
    	
    	if(flag) {
    		EventSBREClue eventSBREClue = new EventSBREClue();
    		eventSBREClue.setClueId(clueId);
    		//办理期限设置为当前系统时间 30个自然日之后
    		eventSBREClue.setHandleDate(DateUtils.addInterval(new Date(), 30, "00"));
    		
    		this.updateClue(eventSBREClue, null, userInfo);
    	}
    	
    	return flag;
    }
    
    @Override
	public boolean subWorkflow4Clue(Long clueId, String nextNodeName,
			List<UserInfo> nextUserInfoList, UserInfo userInfo,
			Map<String, Object> extraParam) throws Exception {
    	boolean flag = super.subWorkflow4Clue(clueId, nextNodeName, nextUserInfoList, userInfo, extraParam);
    	
    	if(flag) {
    		batchReadMessage(clueId);
    	}
    	
    	return flag;
    }
    
    @Override
	public boolean rejectWorkflow4Clue(Long clueId, String rejectToNodeName,
			UserInfo userInfo, Map<String, Object> extraParam) throws Exception {
    	boolean flag = super.rejectWorkflow4Clue(clueId, rejectToNodeName, userInfo, extraParam);
    	
    	if(flag) {
    		batchReadMessage(clueId);
    	}
    	
    	return flag;
    }
    
    /**
     * 批量读取线索相关消息
     * @param clueId
     */
    private void batchReadMessage(Long clueId) {
    	if(clueId != null && clueId > 0) {
    		String CLUE_MSG_MODULE_CODE = "07";					//线索消息模块编码
	    	Map<String, Object> params = new HashMap<String, Object>();
	    	
	    	params.put("bizId", clueId);
	    	params.put("moduleCode", CLUE_MSG_MODULE_CODE);
	    	messageService.batchReadMessage(params);
    	}
    }
    
    /**
     * 获取线索额外信息
     * @param clue		线索信息
     * @param params
     * 			isCapGangInfo		是否获取黑恶团伙信息，true为获取；默认为false
     * 			isCapInformantInfo	是否获取举报人信息，true为获取；默认为false
     * 			isCapReportedInfo	是否获取被举报人信息，true为获取；默认为false
     * 			isCapDisposeUnit	是否获取综治单位新，true为获取；默认为false
     * @return
     */
    private Map<String, Object> capExtraInfo(EventSBREClue clue, Map<String, Object> params) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	if(clue != null) {
	    	Long clueId = clue.getClueId();
	    	
	    	if(clueId != null && clueId > 0) {
	    		//获取黑恶团伙信息
	    		boolean isCapGangInfo = false;
	    		
	    		if(CommonFunctions.isNotBlank(params, "isCapGangInfo")) {
	    			isCapGangInfo = Boolean.valueOf(params.get("isCapGangInfo").toString());
	    		}
	    		
	    		if(isCapGangInfo) {
	    			Map<String, Object> moduleMap = new HashMap<String, Object>();
	    			moduleMap.put("moduleCodeRight", MODULECODERIGHT);
	    			
	    			List<ModuleRela> moduleRelaList = moduleRelaService.findModuleRelaByLeft(MODULECODELEFT, clueId, moduleMap);
	    			if(moduleRelaList != null) {
	    				Long gangId = -1L;
	    				EventSBREvilGang gangInfo = null;
	    				List<EventSBREvilGang> gangList = new ArrayList<EventSBREvilGang>();
	    				
	    				for(ModuleRela moduleRela : moduleRelaList) {
	    					gangId = moduleRela.getModuleIdRight();
	    					
	    					if(gangId != null && gangId > 0) {
	    						gangInfo = eventSBREvilGangService.searchById(gangId);
	    						if(gangInfo != null) {
	    							gangList.add(gangInfo);
	    						}
	    					}
	    				}
	    				
	    				if(gangList.size() > 0) {
	    					resultMap.put("gangList", gangList);
	    				}
	    			}
	    		}
	    		
	    		//获取举报人信息
	            boolean isCapInformantInfo = false;
	            if(CommonFunctions.isNotBlank(params, "isCapInformantInfo")) {
	            	isCapInformantInfo = Boolean.valueOf(params.get("isCapInformantInfo").toString());
	            }
	            
	            if(isCapInformantInfo) {
	            	List<InvolvedPeople> informantInfoList = involvedPeopleService.findInvolvedPeopleListByBiz(clueId, InvolvedPeople.BIZ_TYPE.SBRE_CLUE_INFORMANT.getBizType());
		            if (informantInfoList != null && informantInfoList.size() > 0) {
		                resultMap.put("informantInfo", informantInfoList.get(0));
		            }
	            }
	            
	            //获取被举报人信息
	            boolean isCapReportedInfo = false;
	            if(CommonFunctions.isNotBlank(params, "isCapReportedInfo")) {
	            	isCapReportedInfo = Boolean.valueOf(params.get("isCapReportedInfo").toString());
	            }
	            if(isCapReportedInfo) {
	            	List<InvolvedPeople> reportedInfoList = involvedPeopleService.findInvolvedPeopleListByBiz(clueId, InvolvedPeople.BIZ_TYPE.SBRE_CLUE_REPORTED.getBizType());
		            if (reportedInfoList != null && reportedInfoList.size() > 0) {
		                resultMap.put("reportedInfoList", reportedInfoList);
		            }
	            }
	            
	            //获取综治单位信息
	            boolean isCapDisposeUnit = false;
	            if(CommonFunctions.isNotBlank(params, "isCapDisposeUnit")) {
	            	isCapDisposeUnit = Boolean.valueOf(params.get("isCapDisposeUnit").toString());
	            }
	            if(isCapDisposeUnit) {
	            	String disposalMethod = clue.getDisposalMethod(),
	            		   disposalUnitName = null;
	            	Long disposeUnitId = -1L;
	            	PrvetionTeam disposeUnitInfo = null;
	            	
	            	if(DISPOSAL_METHOD_REGISTER.equals(disposalMethod)) {
	            		disposeUnitId = clue.getRegisterUnit();
	            		disposalUnitName = clue.getRegisterUnitName();
	            	} else if(DISPOSAL_METHOD_PUNISH.equals(disposalMethod)) {
	            		disposeUnitId = clue.getPunishUnit();
	            		disposalUnitName = clue.getPunishUnitName();
	            	} else if(DISPOSAL_METHOD_TRANSFER.equals(disposalMethod)) {
	            		disposeUnitId = clue.getTransferUnit();
	            		disposalUnitName = clue.getTransferUnitName();
	            	}
	            	
	            	if(StringUtils.isNotBlank(disposalUnitName)) {
	            		disposeUnitInfo = new PrvetionTeam();
	            		disposeUnitInfo.setName(disposalUnitName);
	            	} else if(disposeUnitId != null && disposeUnitId > 0) {
	            		//disposeUnitInfo = prvetionTeamService.findPrvetionTeamById(disposeUnitId, null);
	            	}
	            	
	            	if(disposeUnitInfo != null) {
            			resultMap.put("disposeUnitInfo", disposeUnitInfo);
            		}
	            }
	    	}
    	}
    	
    	return resultMap;
    }
    
    /**
     * 保存额外信息
     *
     * @param clueId 线索id
     * @param param  attachmentIds	附件id，以英文逗号相连
     *               gangIds 		黑恶团伙ids，以英文逗号相连
     *               involveAttackFocus	涉及打击重点字典业务编码，以英文逗号相连
     */
    private void saveExtraInfo(Long clueId, Map<String, Object> param, UserInfo userInfo) {
    	Long creatorId = -1L;
    	
    	if(userInfo != null) {
    		creatorId = userInfo.getUserId();
    	}
        //涉及人员（被举报人、举报人）
        if (CommonFunctions.isNotBlank(param,"involvedPeopleArr")) {
            //删除举报人、被举报人信息-能否一次性删除举报人、被举报人信息
            involvedPeopleService.deleteInvolvedPeopleByBiz(clueId, InvolvedPeople.BIZ_TYPE.SBRE_CLUE_INFORMANT.getBizType());
            involvedPeopleService.deleteInvolvedPeopleByBiz(clueId, InvolvedPeople.BIZ_TYPE.SBRE_CLUE_REPORTED.getBizType());

            List<InvolvedPeople> invopeopleList = JSON.parseArray(param.get("involvedPeopleArr").toString(),InvolvedPeople.class);
            String involvedPeopleName = null;
            
            for (InvolvedPeople involvedPeople:invopeopleList) {
            	involvedPeopleName = involvedPeople.getName();
            	
            	//允许输入空格
            	if(involvedPeopleName != null && !"".equals(involvedPeopleName)) {
	                involvedPeople.setName(involvedPeopleName);
	                involvedPeople.setBizId(clueId);
	                if (creatorId > 0) {
	                    involvedPeople.setCreateUserId(creatorId);
	                }
	                try {
	                    involvedPeopleService.insertInvolvedPeople(involvedPeople);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
            	}
            }
        }

        //附件信息
        if (CommonFunctions.isNotBlank(param, "attachmentIds")) {
            attachmentService.updateBizId(clueId, ATTACHMENT_TYPE, param.get("attachmentIds").toString());
        }

        boolean isAlterGangRela = false;
        
        if(CommonFunctions.isNotBlank(param, "isAlterGangRela")) {
        	isAlterGangRela = Boolean.valueOf(param.get("isAlterGangRela").toString());
        }
        
        if(isAlterGangRela) {
        	Map<String, Object> gangParam = new HashMap<String, Object>();
        	
        	gangParam.put("moduleIdLeft", clueId);
        	gangParam.put("moduleCodeLeft", MODULECODELEFT);
        	
	        //黑恶团伙ids，使用英文逗号分隔
	        if (CommonFunctions.isNotBlank(param, "gangIds")) {
	            String[] gangIdArray = param.get("gangIds").toString().split(",");
	            List<Long> gangIdList = new ArrayList<Long>();
	            Long gangId = -1L;
	            
	            for(String gangIdStr : gangIdArray) {
	            	if(StringUtils.isNotBlank(gangIdStr)) {
	            		try {
	            			gangId = Long.valueOf(gangIdStr);
	            		} catch(NumberFormatException e) {
	            			gangId = -1L;
	            			e.printStackTrace();
	            		}
	            		
	            		if(gangId > 0) {
	            			gangIdList.add(gangId);
	            		}
	            	}
	            }
	            
	            if(gangIdList.size() > 0) {
	            	gangParam.put("moduleCodeRight", MODULECODERIGHT);
	            	gangParam.put("moduleIdRightList", gangIdList);
	                
	                if(creatorId > 0) {
	                	gangParam.put("creatorId", creatorId);
	                }
	                
	                try {
	    				moduleRelaService.saveModuleRelaBatch(gangParam);
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	            }
	            
	        } else {//清空现有的黑恶团伙关联关系
	        	moduleRelaService.deleteByLeft(MODULECODELEFT, clueId, gangParam);
	        }
        }

        /*保存涉及打击重点信息*/
        if(CommonFunctions.isNotBlank(param,"involveAttackFocus")){
            Map<String,Object> attFocusParam = new HashMap<>();
            List<Long> attFocusCodeList = new ArrayList<>();

            param.put("bizId",clueId);
            param.put("bizType",TYPERELA_BIZ_TYPE);
            /*删除原有涉及打击重点关联记录*/
            try {
                typeRelaService.delTypeRela(param);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(CommonFunctions.isNotBlank(param,"involveAttackFocus")){
                param.put("typeValue",param.get("involveAttackFocus"));
                param.put("typeCode",INVOLVE_ATTACK_FOCUS_PCODE);
                param.put("bizColumn","INVOLVE_ATTACK_FOCUS");
                try {
                    typeRelaService.insertTypeRela(param,userInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 格式化输入数据-新增、编辑
     *
     * @param
     * @throws ZhsqEventException 
     *
     */
    private void formatEntityDataIn(EventSBREClue clue, UserInfo userInfo) throws ZhsqEventException {
        if (null != clue) {
            Long userId = -1L;
            String reportDateStr = clue.getReportDateStr(),		//举报时间
            	   registerDateStr = clue.getRegisterDateStr(),	//立案时间
            	   punishDateStr = clue.getPunishDateStr(),		//处罚时间
            	   transferDateStr = clue.getTransferDateStr(),	//移交时间
            	   feedbackDateStr = clue.getFeedbackDateStr(),	//反馈时间
            	   disposalMethod = clue.getDisposalMethod(),	//处置方式
            	   clueSource = clue.getClueSource(),			//线索来源
            	   involveAttackFocus = clue.getInvolveAttackFocus();	//线索涉及打击重点

            if (userInfo != null) {
                userId = userInfo.getUserId();
            }

            if (clue.getReportDate() == null && StringUtils.isNotBlank(reportDateStr)) {
                try {
                    clue.setReportDate(DateUtils.convertStringToDate(reportDateStr, DateUtils.PATTERN_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            
            if(StringUtils.isNotBlank(disposalMethod)) {
            	if(DISPOSAL_METHOD_REGISTER.equals(disposalMethod)) {
            		if (clue.getRegisterDate() == null && StringUtils.isNotBlank(registerDateStr)) {
                        try {
                            clue.setRegisterDate(DateUtils.convertStringToDate(registerDateStr, DateUtils.PATTERN_DATE));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
            		
            		clue.setPunishDate(null);
            		clue.setPunishDateStr(null);
            		clue.setPunishmenter(null);
            		clue.setPunishUnit(null);
            		
            		clue.setTransferDate(null);
            		clue.setTransferDateStr(null);
            		clue.setTransferName(null);
            		clue.setTransferUnit(null);
            		
            		clue.setDisposalSituation(null);
            	} else if(DISPOSAL_METHOD_PUNISH.equals(disposalMethod)) {
            		if (clue.getPunishDate() == null && StringUtils.isNotBlank(punishDateStr)) {
                        try {
                            clue.setPunishDate(DateUtils.convertStringToDate(punishDateStr, DateUtils.PATTERN_DATE));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
            		
            		clue.setRegisterDate(null);
            		clue.setRegisterDateStr(null);
            		clue.setRegisterName(null);
            		clue.setRegisterUnit(null);
            		clue.setSuspectedCharges(null);
            		
            		clue.setTransferDate(null);
            		clue.setTransferDateStr(null);
            		clue.setTransferName(null);
            		clue.setTransferUnit(null);
            		
            		clue.setDisposalSituation(null);
            	} else if(DISPOSAL_METHOD_TRANSFER.equals(disposalMethod)) {
            		if (clue.getTransferDate() == null && StringUtils.isNotBlank(transferDateStr)) {
                        try {
                            clue.setTransferDate(DateUtils.convertStringToDate(transferDateStr, DateUtils.PATTERN_DATE));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
            		
            		clue.setRegisterDate(null);
            		clue.setRegisterDateStr(null);
            		clue.setRegisterName(null);
            		clue.setRegisterUnit(null);
            		clue.setSuspectedCharges(null);
            		
            		clue.setPunishDate(null);
            		clue.setPunishDateStr(null);
            		clue.setPunishmenter(null);
            		clue.setPunishUnit(null);
            		
            		clue.setDisposalSituation(null);
            	} else if(DISPOSAL_METHOD_OTHER.equals(disposalMethod)) {
            		clue.setRegisterDate(null);
            		clue.setRegisterDateStr(null);
            		clue.setRegisterName(null);
            		clue.setRegisterUnit(null);
            		clue.setSuspectedCharges(null);
            		
            		clue.setPunishDate(null);
            		clue.setPunishDateStr(null);
            		clue.setPunishmenter(null);
            		clue.setPunishUnit(null);
            		
            		clue.setTransferDate(null);
            		clue.setTransferDateStr(null);
            		clue.setTransferName(null);
            		clue.setTransferUnit(null);
            	}
            	
            	//设置反馈时间、反馈人姓名
            	//clue.setFeedbackDate(new Date());
            	//clue.setFeedbackName(userInfo.getPartyName());
            	
            	if(clue.getFeedbackDate() == null && StringUtils.isNotBlank(feedbackDateStr)) {
            		try {
                        clue.setFeedbackDate(DateUtils.convertStringToDate(feedbackDateStr, DateUtils.PATTERN_DATE));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            	}
            	
            }
            
            if(StringUtils.isNotBlank(clueSource)) {
            	String  CLUESOURCE_PCODE = "B591001",
            			userOrgCode = userInfo.getOrgCode(),
            			removeDictCodeStr = funConfigurationService.changeCodeToValue(ConstantValue.EVENT_EXHIBITION_ATTRIBUTE, ConstantValue.REMOVE_DICT_CODE + CLUESOURCE_PCODE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userOrgCode);
            	
            	if(StringUtils.isNotBlank(removeDictCodeStr)) {
            		String[] removeDictCodeArray = removeDictCodeStr.split(";"),
            				 dictCodeArray = null;
            		List<String> removeDictCodeList = new ArrayList<String>();
            		boolean isRemove = false;
            		
            		for(String dictCodeStr : removeDictCodeArray) {
            			if(StringUtils.isNotBlank(dictCodeStr)) {
            				dictCodeArray = dictCodeStr.split(":");
            				if(dictCodeArray != null && dictCodeArray.length > 1) {
            					isRemove = Boolean.valueOf(dictCodeArray[1]);
            					if(isRemove) {
            						removeDictCodeList.add(dictCodeArray[0]);
            					}
            				}
            			}
            		}
            		
            		if(removeDictCodeList.size() > 0) {
            			List<BaseDataDict> clueSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(CLUESOURCE_PCODE, userOrgCode);
            			
            			if(clueSourceDictList != null) {
            				StringBuffer msgWrong = new StringBuffer("");
            				
	            			for(String removeDictCode : removeDictCodeList) {
	            				for(BaseDataDict clueSourceDict : clueSourceDictList) {
	            					if(removeDictCode.equals(clueSourceDict.getDictCode()) && clueSource.equals(clueSourceDict.getDictGeneralCode())) {
	            						msgWrong.append("无法使用线索来源【").append(clueSourceDict.getDictName()).append("】！");
	            						break;
	            					}
	            				}
	            				
	            			}
	            			
	            			if(msgWrong.length() > 0) {
	            				msgWrong.insert(0, "当前组织【" + userInfo.getOrgName() + "】");
	            				throw new ZhsqEventException(msgWrong.toString());
	            			}
            			}
            			
            		}
            	}
            }

            if(StringUtils.isNotBlank(involveAttackFocus)){
                clue.setInvolveAttackFocus("," + involveAttackFocus + ",");
            }

            if (userId > 0 && clue.getUpdaterId() == null) {
                clue.setUpdaterId(userId);
            }
        }

    }

    /**
     * 格式化查询参数
     */
    private void formatQueryData(Map<String, Object> params, UserInfo userInfo) {
        if (params != null && !params.isEmpty()) {
            int listType = 1;
            Long userId = -1L, userOrgId = -1L;
            String userOrgCode = null,
            	   userIdStr = "",
            	   userOrgIdStr = "";
            	   
            List<String> statusList = new ArrayList<String>();

            if (userInfo != null) {
                userOrgCode = userInfo.getOrgCode();
                userId = userInfo.getUserId();
                userOrgId = userInfo.getOrgId();
            }
            
            if(userId != null && userId > 0) {
            	userIdStr = userId.toString();
            }
            
            if(userOrgId != null && userOrgId > 0) {
            	userOrgIdStr = userOrgId.toString();
            }

            if (CommonFunctions.isNotBlank(params, "listType")) {
                try {
                    listType = Integer.valueOf(params.get("listType").toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            switch (listType) {
                case 1: {//草稿列表
                    statusList.add(CLUE_STATUS_DRAFT);
                    break;
                }
                default: {
                	statusList.add(CLUE_STATUS_REPORTED);//已上报
                    statusList.add(CLUE_STATUS_TRANSFER);//流转中
                    statusList.add(CLUE_STATUS_CHECK);//待核查
                    statusList.add(CLUE_STATUS_FEEDBACK);//已核查待处置
                    statusList.add(CLUE_STATUS_EVA);//待评价
                    statusList.add(CLUE_STATUS_END);
                    break;
                }
            }

            if (CommonFunctions.isBlank(params, "userOrgCode") && StringUtils.isNotBlank(userOrgCode)) {
                params.put("userOrgCode", userOrgCode);
            }

            //线索来源多选查询
            if (CommonFunctions.isNotBlank(params, "clueSource")) {
                String[] sourceList = (params.get("clueSource").toString()).split(",");
                params.put("sourceList", sourceList);
            }
            //线索状态多选查询-查询框
            if (CommonFunctions.isNotBlank(params, "clueStatus")) {
                String[] clueStatuss = (params.get("clueStatus").toString()).split(",");

                statusList.retainAll(Arrays.asList(clueStatuss));
            }

            params.put("statusList", statusList);

            //权限控制
            switch (listType) {
                case 1: {//草稿列表-当前登录用户只可以查看自己的草稿
                    if (CommonFunctions.isBlank(params, "creatorId") && StringUtils.isNotBlank(userIdStr)) {
                        params.put("creatorId", userId);
                    }
                    break;
                }
                case 2: {//待办列表
                	if (CommonFunctions.isNotBlank(params, "curUserId")) {
                		params.put("curUserId", params.get("curUserId").toString());
                	} else if (StringUtils.isNotBlank(userIdStr)) {
                        params.put("curUserId", userIdStr);
                    }
                	if (CommonFunctions.isNotBlank(params, "curOrgId")) {
                		params.put("curOrgId", params.get("curOrgId").toString());
                	} else if (StringUtils.isNotBlank(userOrgIdStr)) {
                        params.put("curOrgId", userOrgIdStr);
                    }
                    break;
                }
                case 3: {//经办列表
                	if (CommonFunctions.isBlank(params, "handledUserId") && StringUtils.isNotBlank(userIdStr)) {
                		params.put("handledUserId", userIdStr);
                	}
                	if (CommonFunctions.isBlank(params, "handledOrgId") && StringUtils.isNotBlank(userOrgIdStr)) {
                		params.put("handledOrgId", userOrgIdStr);
                	}
                	
                	if (CommonFunctions.isNotBlank(params, "handledUserId")) {
                		try {
                    		params.put("handledUserId", Long.valueOf(params.get("handledUserId").toString()));
                		} catch(NumberFormatException e) {
                			throw new IllegalArgumentException("线索办理人员id【handledUserId】：" + params.get("handledUserId") + "，不是有效数值！");
                		}
                	} else {
                		throw new IllegalArgumentException("缺少线索办理人员id【handledUserId】！");
                	}
                	if (CommonFunctions.isNotBlank(params, "handledOrgId")) {
                		try {
                    		params.put("handledOrgId", Long.valueOf(params.get("handledOrgId").toString()));
                		} catch(NumberFormatException e) {
                			throw new IllegalArgumentException("线索办理组织id【handledOrgId】：" + params.get("handledOrgId") + "，不是有效数值！");
                		}
                	} else {
                		throw new IllegalArgumentException("缺少线索办理组织id【handledOrgId】！");
                	}

                    break;
                }
                case 4: {//辖区所有列表
                	boolean isCopyClue = false;
                	
                	if (CommonFunctions.isNotBlank(params, "isCopyClue")) {
                		isCopyClue = Boolean.valueOf(params.get("isCopyClue").toString());
                	}
                	
                	//拷贝线索信息时，不进行辖区内过滤
                    if (!isCopyClue) {
                    	if(CommonFunctions.isBlank(params, "infoOrgCode") && CommonFunctions.isNotBlank(params, "startInfoOrgCode")) {
                    		params.put("infoOrgCode", params.get("startInfoOrgCode"));
                    	}
                    	if(CommonFunctions.isBlank(params, "regionCode") && CommonFunctions.isNotBlank(params, "startRegionCode")) {
                    		params.put("regionCode", params.get("startRegionCode"));
                    	}
                    }

                    break;
                }
                case 5: {//我发起的列表
                	if (CommonFunctions.isBlank(params, "initiatorId") && StringUtils.isNotBlank(userIdStr)) {
                		params.put("initiatorId", userIdStr);
                	}
                	if (CommonFunctions.isBlank(params, "initiatorOrgId") && StringUtils.isNotBlank(userOrgIdStr)) {
                		params.put("initiatorOrgId", userOrgIdStr);
                	}
                	
                	if (CommonFunctions.isNotBlank(params, "initiatorId")) {
                		try {
                    		params.put("initiatorId", Long.valueOf(params.get("initiatorId").toString()));
                		} catch(NumberFormatException e) {
                			throw new IllegalArgumentException("线索发起用户织id【initiatorId】：" + params.get("initiatorId") + "，不是有效数值！");
                		}
                	} else {
                		throw new IllegalArgumentException("缺少线索发起用户id【initiatorId】！");
                	}
                	if (CommonFunctions.isNotBlank(params, "initiatorOrgId")) {
                		try {
                    		params.put("initiatorOrgId", Long.valueOf(params.get("initiatorOrgId").toString()));
                		} catch(NumberFormatException e) {
                			throw new IllegalArgumentException("线索发起组织织id【initiatorOrgId】：" + params.get("initiatorOrgId") + "，不是有效数值！");
                		}
                	} else {
                		throw new IllegalArgumentException("缺少线索发起组织id【initiatorOrgId】！");
                	}

                    break;
                }
            }
        }
    }

    /**
     * 格式化输出数据-列表
     */
    private void formatListDataOut(List<EventSBREClue> clueList, String userOrgCode) {
        if (clueList != null && clueList.size() > 0) {
            String  CLUESOURCE_PCODE = "B591001",
                    IMPORTANTDEGREE_PCODE = "B591002",
                    clueSource = null,
                    importantDegree = null,
                    clueStatus = null,
                    isEncrypt = null,
                    isRealName = null,
                    clueTitle = null;

            List<BaseDataDict> clueSourceDictList = baseDictionaryService.getDataDictListOfSinglestage(CLUESOURCE_PCODE,userOrgCode),
                               importantDegreeDictList = baseDictionaryService.getDataDictListOfSinglestage(IMPORTANTDEGREE_PCODE,userOrgCode),
                               clueStatusDictList = baseDictionaryService.getDataDictListOfSinglestage(CLUESTATUS_PCODE,userOrgCode);
            Map<String, String> YES_NO_MAP = new HashMap<String, String>() {
            	{
            		put("0", "否");
            		put("1", "是");
            	}
            };
            Date createDate = null, handleDate = null, feedbackDate = null;

            for (EventSBREClue clue : clueList) {
                clueSource = clue.getClueSource();
                importantDegree = clue.getImportantDegree();
                clueStatus = clue.getClueStatus();
                isEncrypt = clue.getIsEncrypt();
                isRealName = clue.getIsRealName();  
                clueTitle = clue.getClueTitle();
                createDate = clue.getCreateDate();
                handleDate = clue.getHandleDate();
                feedbackDate = clue.getFeedbackDate();

                //处理线索加密标题
                if (NEED_ENCRYPT.equals(isEncrypt) && clueTitle.length() >= 6) {
                    clue.setClueTitle(clueTitle.substring(0,6) + "******");
                }

                clue.setIsEncryptName(YES_NO_MAP.get(isEncrypt));
                clue.setIsRealNameName(YES_NO_MAP.get(isRealName));

                if (StringUtils.isNotBlank(clueSource)) {
                    try {
                        DataDictHelper.setDictValueForField(clue, "clueSource", "clueSourceName", clueSourceDictList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (StringUtils.isNotBlank(importantDegree)) {
                    try {
                        DataDictHelper.setDictValueForField(clue, "importantDegree", "importantDegreeName", importantDegreeDictList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (StringUtils.isNotBlank(clueStatus)) {
                    try {
                        DataDictHelper.setDictValueForField(clue, "clueStatus", "clueStatusName", clueStatusDictList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (createDate != null) {
                    clue.setCreateDateStr(DateUtils.formatDate(createDate, DateUtils.PATTERN_DATE));
                }
                if (handleDate != null) {
                	clue.setHandleDateStr(DateUtils.formatDate(handleDate, DateUtils.PATTERN_DATE));
                }
                if (feedbackDate != null) {
                	clue.setFeedbackDateStr(DateUtils.formatDate(feedbackDate, DateUtils.PATTERN_DATE));
                }
            }

        }
    }

    /**
     * 格式化输出数据-详情
     * @param clue			线索信息
     * @param userOrgCode	组织编码
     */
    private void formatDetailDataOut(EventSBREClue clue, String userOrgCode) {
    	if(clue != null) {
    		Date reportDate = clue.getReportDate(),
    			 registerDate = clue.getRegisterDate(),
    			 punishDate = clue.getPunishDate(),
    			 transferDate = clue.getTransferDate();
    		String clueTitle = clue.getClueTitle(),
    			   disposalMethod = clue.getDisposalMethod(),
    			   suspectedCharges = clue.getSuspectedCharges(),
    			   invAttFocus = clue.getInvolveAttackFocus(),
    			   DISPOSAL_METHOD_PCODE = "B591003",//处置方式
    			   SUSPECTED_CHARGES_PCODE = "B591004",//涉嫌罪名
    			   INVOLVE_ATTACK_FOCUS_PCODE = "B591006";//涉及打击重点
    		List<EventSBREClue> clueList = new ArrayList<EventSBREClue>();
    		List<BaseDataDict> disposalMethodDictList = baseDictionaryService.getDataDictListOfSinglestage(DISPOSAL_METHOD_PCODE, userOrgCode);

    		clueList.add(clue);

    		if(reportDate != null) {
    			clue.setReportDateStr(DateUtils.formatDate(reportDate, DateUtils.PATTERN_DATE));
    		}
    		if(registerDate != null) {
    			clue.setRegisterDateStr(DateUtils.formatDate(registerDate, DateUtils.PATTERN_DATE));
    		}
    		if(punishDate != null) {
    			clue.setPunishDateStr(DateUtils.formatDate(punishDate, DateUtils.PATTERN_DATE));
    		}
    		if(transferDate != null) {
    			clue.setTransferDateStr(DateUtils.formatDate(transferDate, DateUtils.PATTERN_DATE));
    		}
    		if(StringUtils.isNotBlank(disposalMethod)) {
    			try {
                    DataDictHelper.setDictValueForField(clue, "disposalMethod", "disposalMethodName", disposalMethodDictList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    		}
    		if(StringUtils.isNotBlank(suspectedCharges)) {
    			clue.setSuspectedChargesName(baseDictionaryService.changeCodeToName(SUSPECTED_CHARGES_PCODE, suspectedCharges, userOrgCode));
    		}
    		if(StringUtils.isNotBlank(invAttFocus)){
    		    Map<String,Object> dictMap = new HashMap<>();
    		    dictMap.put("orgCode",userOrgCode);
    		    dictMap.put("dictPcode",INVOLVE_ATTACK_FOCUS_PCODE);

                List<BaseDataDict> invAttFocusDictList = baseDictionaryService.findDataDictListByCodes(dictMap);

                StringBuffer dictFullPath = new StringBuffer("");
                String[] bigTypeArr = invAttFocus.split(",");
                String bigTypeName = "",bigDictCode = null;
                int n = 0;

                for(String bigType:bigTypeArr){
                    int bigTypeLen = bigType.length();
                    do{
                        bigTypeName = "";

                        for(BaseDataDict dataDict:invAttFocusDictList){
                            if((StringUtils.isNotBlank(bigDictCode) && !INVOLVE_ATTACK_FOCUS_PCODE.equals(bigDictCode) && bigDictCode.equals(dataDict.getDictCode())
                            || (StringUtils.isNotBlank(bigType) && bigType.equals(dataDict.getDictGeneralCode())))){
                                bigTypeName = dataDict.getDictName();
                                bigDictCode = dataDict.getDictPcode();
                                bigType = null;
                                break;

                            }
                        }

                        if(StringUtils.isNotBlank(bigTypeName)) {
                            if (bigTypeLen == 2) {
                                dictFullPath.insert(0, bigTypeName).insert(0, ",");
                            } else {
                                //判断是偶数次还是奇数次，偶数次用"-"分割，奇数次用","分割
                                if(StringUtils.isNotBlank(bigTypeName) && n%2==0) {
                                    dictFullPath.insert(0, bigTypeName).insert(0, "-");
                                    n++;
                                } else if (StringUtils.isNotBlank(bigTypeName)) {
                                    dictFullPath.insert(0, bigTypeName).insert(0, ",");
                                    n++;
                                }
                            }
                        }

                    }while (StringUtils.isNotBlank(bigTypeName));
                }

                if(dictFullPath.length() > 0){
                    clue.setInvolveAttackFocusName(dictFullPath.substring(1));
                }
            }

    		formatListDataOut(clueList, userOrgCode);

    		clue.setClueTitle(clueTitle);//详情不加密展示
    	}
    }

}
