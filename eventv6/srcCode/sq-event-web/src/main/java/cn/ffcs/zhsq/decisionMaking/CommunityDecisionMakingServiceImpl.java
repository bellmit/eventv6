package cn.ffcs.zhsq.decisionMaking;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.service.OrgSocialInfoOutService;
import cn.ffcs.zhsq.keyelement.service.IFiveKeyElementService;
import cn.ffcs.zhsq.utils.CommonFunctions;

/**
 * 思明区 社区流程图决策类
 * 必填参数
 * decisionService	指定实现类 communityDecisionMakingService
 * curUserId	当前用户
 * curOrgId		当前用户所属组织
 * curNodeCode	当前环节节点编码
 * @author zhangls
 *
 */
@Service(value = "communityDecisionMakingService")
public class CommunityDecisionMakingServiceImpl extends
		ApplicationObjectSupport implements IWorkflowDecisionMakingService<String> {

	@Autowired
	private IFiveKeyElementService fiveKeyElementService;
	
	@Autowired
	private OrgSocialInfoOutService orgSocialInfoService;
	
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	
	//决策1
	private static final String DECISION_MAKING_NODE_CODE = "decision1";	//决策1环节节点编码
	private static final String COMMUNITY_REGISTER_NODE_CODE = "task1";		//社区登记问题环节
	private static final String GRID_ADMIN_HANDLE_NODE_CODE = "task2";		//网格员办理环节
	private static final String TOP_GRID_ADMIN_HANDLE_NODE_CODE = "task3";	//总网格长办理环节
	
	@Override
	public String makeDecision(Map<String, Object> params) throws Exception {
		Long curUserId = -1L;		//当前用户
		Long curOrgId = -1L;		//当前用户所属组织
		String curNodeCode = "";	//当前环节节点编码
		String nextNodeCode = "";	//下一环节节点编码
		boolean isGridAdmin = false;	//当前登入人员是否为网格员
		boolean isTopGridAdmin = false;	//当前登入人员是否为总网格长
		
		if(CommonFunctions.isNotBlank(params, "curUserId")) {
			try {
				curUserId = Long.valueOf(params.get("curUserId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curUserId]："+params.get("curUserId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curUserId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curOrgId")) {
			try {
				curOrgId = Long.valueOf(params.get("curOrgId").toString());
			} catch(NumberFormatException e) {
				throw new NumberFormatException("参数[curOrgId]："+params.get("curOrgId")+" 不能转换为Long型！");
			}
		} else {
			throw new IllegalArgumentException("缺失参数[curOrgId]，请检查！");
		}
		
		if(CommonFunctions.isNotBlank(params, "curNodeCode")) {
			curNodeCode = params.get("curNodeCode").toString();
		} else {
			throw new IllegalArgumentException("缺失参数[curNodeCode]，请检查！");
		}
		
		isGridAdmin = fiveKeyElementService.isUserIdGridAdmin(curOrgId, curUserId);
		
		if(curOrgId > 0) {
			OrgSocialInfoBO orgInfo = orgSocialInfoService.findByOrgId(curOrgId);
			if(orgInfo != null) {
				List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridMappingListByOrgIdForEvent(orgInfo.getLocationOrgId());
				String gridAdminDuty = fiveKeyElementService.capGridAdminDuty(curOrgId);
				
				for(MixedGridInfo gridInfo : gridInfoList) {
					isTopGridAdmin = fiveKeyElementService.isUserIdGridAdminExists(gridInfo.getGridId(), curUserId, gridAdminDuty);
					
					if(isTopGridAdmin) {
						break;
					}
				}
			} else {
				throw new Exception("参数[curOrgId]："+curOrgId+" 没有对应的组织信息！");
			}
		}
		if(DECISION_MAKING_NODE_CODE.equals(curNodeCode)) {//决策1
			if(isTopGridAdmin) {
				nextNodeCode = TOP_GRID_ADMIN_HANDLE_NODE_CODE;
			} else if(isGridAdmin) {
				nextNodeCode = GRID_ADMIN_HANDLE_NODE_CODE;
			} else {
				nextNodeCode = COMMUNITY_REGISTER_NODE_CODE;
			}
		}
		
		if(StringUtils.isBlank(nextNodeCode)) {
			throw new Exception("没有可用的下一环节！");
		}
		
		return nextNodeCode;
	}

}
