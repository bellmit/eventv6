package cn.ffcs.zhsq.keyelement.impl;

import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.INodeCodeHandler;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:江西省平台人员选择处理类
 * @Author: youwj
 * @Date: 2019/8/1 16:55
 */
@Service(value = "fiveKeyElement4JXPlatformService")
public class FiveKeyElement4JXPlatformServiceImpl extends FiveKeyElementForEventServiceImpl {
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Override
    public Map<String, Object> getNodeInfoForEvent(
            UserInfo userInfo,
            String curnodeName, String nodeName, String nodeCode, String nodeId, Map<String, Object> params)
            throws Exception {
        Map<String, Object> resultMap = super.getNodeInfoForEvent(userInfo, curnodeName, nodeName, nodeCode, nodeId, params);
        
        //如果是当前环节处理受理环节，那么直接展示当前用户
        if(!"end1".equals(nodeName) && !ConstantValue.HANDLING_TASK_CODE.equals(nodeName)) {
        	String userConstructType = capUserConstructType(curnodeName, nodeName),
        			CONSTRUCT_TYPE_USER = "1",
        			CONSTRUCT_TYPE_ORG = "2";
	        if(CONSTRUCT_TYPE_USER.equals(userConstructType)) {
	        	
	        	resultMap.put("userIds", userInfo.getUserId().toString());
	        	resultMap.put("userNames", userInfo.getPartyName());
	        	resultMap.put("orgIds", userInfo.getOrgId().toString());
	        	
	        	resultMap.put("isSelectUser", true);
	        	//办理页面是否显示组织选中 true为是，false为否
	        	resultMap.put("isSelectOrg", false);
	        	resultMap.put("isAdviceRequired", false);
	        } else if(CONSTRUCT_TYPE_ORG.equals(userConstructType)) {
	        	//当前环节处于办理环节，则需要选择组织进行办理
	        	resultMap.put("isJointHandled",true);
	        	resultMap.put("isJointOperated", false);
	        	resultMap.put("isSelectOrg", true);
	        	resultMap.put("isSelectUser", false);
	        	resultMap.put("isShowInterval", false);
	        	resultMap.put("isUploadHandledPic", false);
	        	
	        	//如果当前环节是网格员受理，那么此时结点会默认选中乡镇综治中心受理
	        	//代码里自动找到对于的乡镇综治中心
	        	try {
					if("task3".equals(curnodeName) && "task6".equals(nodeName)) {
						//根据用户组织id找到上级的乡镇综治中心
						Long userOrgId = userInfo.getOrgId();
						OrgSocialInfoBO streetOrgInfo = null;
						List<OrgSocialInfoBO> orgInfoList = orgSocialInfoService.findSuperiorByCode(userOrgId, 2, ConstantValue.GOV_PROFESSION_CODE);
						
						if(orgInfoList != null && orgInfoList.size() > 0) {
							streetOrgInfo = orgInfoList.get(0);
						} else {
							streetOrgInfo = new OrgSocialInfoBO();
							
							if(logger.isErrorEnabled()) {
								logger.error("组织id【" + userOrgId + "】，组织名称【" + userInfo.getOrgName() + "】没有找到街道层级组织信息！");
							}
						}
						
						resultMap.put("assistOrgIds", streetOrgInfo.getOrgId());
						resultMap.put("assistOrgNames", streetOrgInfo.getOrgName());
						resultMap.put("assistOrgNamesHtml", streetOrgInfo.getOrgName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	
	        }
        }

        return resultMap;
    }
    
    @Override
	public List<GdZTreeNode> getTreeForEvent(
			UserInfo userInfo, Long orgId,
			String nodeCode, Integer level, String professionCode, Map<String, Object> params) throws Exception {
    	List<GdZTreeNode> treeNodeList = super.getTreeForEvent(userInfo, orgId, nodeCode, level, professionCode, params);
    	
    	if(treeNodeList != null && treeNodeList.size() > 0) {
    		INodeCodeHandler nodeCodeHandler = this.createNodeCodeHandle(nodeCode);
    		
    		if(userInfo != null 
    				&& nodeCodeHandler.isFromDept() 
    				&& nodeCodeHandler.isToDept() 
    				&& (nodeCodeHandler.isReport()
    						|| (nodeCodeHandler.isSend() 
    								&& level != null 
    								&& level == nodeCodeHandler.getLineLevel()))) {
    			Long userOrgId = userInfo.getOrgId();
    			String reserveProfessionCode = null;
    			
    			if(userOrgId != null && userOrgId > 0) {
    				OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(userOrgId);
    				
    				if(orgInfo != null) {
    					reserveProfessionCode = orgInfo.getProfessionCode();
    				}
    			}
    			
    			if(StringUtils.isNotBlank(reserveProfessionCode)) {
    				List<GdZTreeNode> reserveTreeNodeList = new ArrayList<GdZTreeNode>();
    				
    				for(GdZTreeNode treeNode : treeNodeList) {
    					if(reserveProfessionCode.equals(treeNode.getProfessionCode())) {
    						reserveTreeNodeList.add(treeNode);
    					}
    				}
    				
    				treeNodeList = reserveTreeNodeList;
    			}
    		}
    	}
		
		return treeNodeList;
	}
    
    /**
	 * 设置节点的clickable属性
	 * @param nodeCodeHandler
	 * @param nodes
	 * @param isFromCommunityGridAdmin
	 */
	protected void addClickableAttr2Node(INodeCodeHandler nodeCodeHandler, List<GdZTreeNode> nodes, boolean isFromCommunityGridAdmin) {
		if(nodes != null) {
			//当为上报操作时，均可点击，因为上级树只会展示一级
			//当下派或者分立给职能部门时，可点击的只有末端的职能部门
			boolean isReport = nodeCodeHandler.isReport(),		//是否上报
					isSplitFlow = nodeCodeHandler.isSplitFlow(),//是否分流
					isSplitFlowGlobal = nodeCodeHandler.isSplitFlowGlobal(),//是否分流给所有职能部门
					isSend = nodeCodeHandler.isSend(),			//是否下派
					isToDept = nodeCodeHandler.isToDept(),		//是否到部门
					isGlobal = nodeCodeHandler.isGlobal(),		//sq_event_web
					isClickable = true;							//节点是否可点击
			
			int lineLevel = nodeCodeHandler.getLineLevel(),
				gridLevel = 0;									//节点层级
			String gridLevelStr = "",
				   ORG_TYPE_DEPT = String.valueOf(ConstantValue.ORG_TYPE_DEPT);
				
			for(GdZTreeNode node : nodes) {
				if(isSplitFlowGlobal) {
					isClickable = !node.getIsParent() && ORG_TYPE_DEPT.equals(node.getLayerType());
				} else if(!isReport && !isSplitFlow && !isGlobal) {
					isClickable = false;
					gridLevelStr = node.getGridLevel();
					gridLevel = 0;
					
					if(StringUtils.isNotBlank(gridLevelStr)) {
						try{
							gridLevel = Integer.valueOf(gridLevelStr);
						} catch(NumberFormatException e) {
							gridLevel = 0;
							e.printStackTrace();
						}
					}
					
					if(isToDept) {//目标为职能部门办理
						isClickable = gridLevel == (lineLevel + 1);
					} else if(isSend) {
						isClickable = gridLevel == lineLevel;
					} else {
						isClickable = !node.getIsParent();
					}
				}
				
				node.setClickable(isClickable);
			}
		}
	}
	
	/**
	 * 构造节点人员选择类型
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	protected Map<String, String> init4TaskUserConstruct() {
		Map<String, String> initMap = new HashMap<String, String>();
		
		initMap.put("task2", "2");
		initMap.put("task4", "2");
		initMap.put("task6", "2");
		initMap.put("task8", "2");
		initMap.put("task10", "2");
		initMap.put("task12", "2");
		initMap.put("task14", "2");
		initMap.put("task16", "2");
		initMap.put("task18", "2");
		initMap.put("task20", "2");
		
		initMap.put("task3", "1");
		initMap.put("task5", "1");
		initMap.put("task7", "1");
		initMap.put("task9", "1");
		initMap.put("task11", "1");
		initMap.put("task13", "1");
		initMap.put("task15", "1");
		initMap.put("task17", "1");
		initMap.put("task19", "1");
		initMap.put("task21", "1");
		
		return initMap;
	}
	
	/**
	 * 获取节点人员选择类型
	 * 
	 * 优先获取下一节点对应的人员选择类型
	 * @param curNodeName	当前环节节点名称
	 * @param nextNodeName	下一环节节点名称
	 * @return
	 * 		1 选择人员；
	 * 		2 选择组织；
	 */
	private String capUserConstructType(String curNodeName, String nextNodeName) {
		Map<String, String> initMap = init4TaskUserConstruct();
		String userConstructType = null;
		
		if(CommonFunctions.isNotBlank(initMap, curNodeName) && CommonFunctions.isNotBlank(initMap, nextNodeName)) {
			userConstructType = initMap.get(nextNodeName);
		} else if(CommonFunctions.isNotBlank(initMap, curNodeName)) {
			userConstructType = initMap.get(curNodeName);
		} else {
			String nodeKey = curNodeName + "-" + nextNodeName;
			
			if(CommonFunctions.isNotBlank(initMap, nodeKey)) {
				userConstructType = initMap.get(nodeKey);
			}
		}
		
		return userConstructType;
	}
}
