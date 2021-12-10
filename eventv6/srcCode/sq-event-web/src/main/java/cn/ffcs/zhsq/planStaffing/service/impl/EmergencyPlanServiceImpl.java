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
import cn.ffcs.zhsq.mybatis.persistence.planStaffing.EmergencyPlanMapper;
import cn.ffcs.zhsq.planStaffing.IEmergencyPlanService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 应急预案管理模块服务实现
 * @Author: LINZHU
 * @Date: 04-22 16:31:15
 * @Copyright: 2021 福富软件
 */
@Service("emergencyPlanServiceImpl")
@Transactional
public class EmergencyPlanServiceImpl implements IEmergencyPlanService {

	@Autowired
	private EmergencyPlanMapper emergencyPlanMapper; //注入应急预案管理模块dao
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	/**
	 * 新增数据
	 * @param bo 应急预案管理业务对象
	 * @return 应急预案管理id
	 */
	@Override
	public String insert(EmergencyPlan bo) {
		emergencyPlanMapper.insert(bo);
		return bo.getPlanId();
	}

	/**
	 * 修改数据
	 * @param bo 应急预案管理业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(EmergencyPlan bo) {
		long result = emergencyPlanMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 应急预案管理业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(EmergencyPlan bo) {
		long result = emergencyPlanMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案管理分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<EmergencyPlan> list = emergencyPlanMapper.searchList(params, rowBounds);
		formatOutData(list, params);
		long count = emergencyPlanMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 字典转化
	 * @param list
	 * @param params
	 */
	private void formatOutData(List<EmergencyPlan> list,Map<String, Object> params){
		List<BaseDataDict> typeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EMERGENC_PLAN_TYPE, params.get("userOrgCode").toString());
		 for (EmergencyPlan emergencyPlan : list) {
			for (BaseDataDict baseDataDict : typeDictList) {
				  if(baseDataDict.getDictGeneralCode().equals(emergencyPlan.getPlanType())) {
					  emergencyPlan.setPlanTypeName(baseDataDict.getDictName());
					  continue;
				  }
			   }
		}
	
	}

	/**
	 * 根据业务id查询数据
	 * @param id 应急预案管理id
	 * @return 应急预案管理业务对象
	 */
	@Override
	public EmergencyPlan searchById(String id,String userOrgCode) {
		EmergencyPlan bo = emergencyPlanMapper.searchById(id);
		bo.setPlanTypeName(baseDictionaryService.changeCodeToName(ConstantValue.EMERGENC_PLAN_TYPE, bo.getPlanType(), userOrgCode));
		return bo;
	}

	@Override
	public long countList(Map<String, Object> params) {
		return emergencyPlanMapper.countList(params);
	}

}