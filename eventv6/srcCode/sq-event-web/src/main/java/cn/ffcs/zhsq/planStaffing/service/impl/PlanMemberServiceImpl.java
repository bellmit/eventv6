package cn.ffcs.zhsq.planStaffing.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.EmergencyPlan;
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanMember;
import cn.ffcs.zhsq.mybatis.persistence.planStaffing.PlanMemberMapper;
import cn.ffcs.zhsq.planStaffing.IPlanMemberService;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 应急预案人员模块服务实现
 * @Author: LINZHU
 * @Date: 04-28 16:17:29
 * @Copyright: 2021 福富软件
 */
@Service("planMemberServiceImpl")
@Transactional
public class PlanMemberServiceImpl implements IPlanMemberService {
   @Autowired
	private IBaseDictionaryService baseDictionaryService;
	@Autowired
	private PlanMemberMapper planMemberMapper; //注入应急预案人员模块dao

	/**
	 * 新增数据
	 * @param bo 应急预案人员业务对象
	 * @return 应急预案人员id
	 */
	@Override
	public String insert(PlanMember bo) {
		planMemberMapper.insert(bo);
		return bo.getPlanMemberId();
	}

	/**
	 * 修改数据
	 * @param bo 应急预案人员业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(PlanMember bo) {
		long result = planMemberMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 应急预案人员业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(PlanMember bo) {
		long result = planMemberMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案人员分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<PlanMember> list = planMemberMapper.searchList(params, rowBounds);
		long count = planMemberMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}

	/**
	 * 根据业务id查询数据
	 * @param id 应急预案人员id
	 * @return 应急预案人员业务对象
	 */
	@Override
	public PlanMember searchById(String id,String userOrgCode) {
		PlanMember bo = planMemberMapper.searchById(id);
		return bo;
	}

	@Override
	public long batchDelete(Map<String, Object> params) {
		return planMemberMapper.batchDelete(params);
	}

	@Override
	public long batchInsert(List<PlanMember> vList) {
		return planMemberMapper.batchInsert(vList);
	}

	@Override
	public long batchUpdate(List<PlanMember> vList) {
		return planMemberMapper.batchUpdate(vList);
	}

	@Override
	public List<PlanMember> findPlanMemberListByParams(Map<String, Object> params) {
		 List<PlanMember> list=planMemberMapper.findPlanMemberListParams(params); 
		 if(CommonFunctions.isNotBlank(params, "isCN")) {
			 if(params.get("isCapInformantInfo").equals("true")) {
				 formatOutData(list, params);
			 }
		 }
		return list;
	}
	/**
	 * 字典转化
	 * @param list
	 * @param params
	 */
	private void formatOutData(List<PlanMember> list,Map<String, Object> params){
		List<BaseDataDict> typeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EMERGENC_PLAN_ROLE, params.get("userOrgCode")==null?null:params.get("userOrgCode").toString());
		 for (PlanMember emergencyPlan : list) {
			for (BaseDataDict baseDataDict : typeDictList) {
				  if(baseDataDict.getDictGeneralCode().equals(emergencyPlan.getPlanRole())) {
					  emergencyPlan.setPlanRoleName(baseDataDict.getDictName());
					  continue;
				  }
			   }
		}
	}
}