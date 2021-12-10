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
import cn.ffcs.zhsq.mybatis.domain.planStaffing.PlanConfig;
import cn.ffcs.zhsq.mybatis.persistence.planStaffing.PlanConfigMapper;
import cn.ffcs.zhsq.planStaffing.IPlanConfigService;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 应急预案配置模块服务实现
 * @Author: LINZHU
 * @Date: 04-23 15:47:50
 * @Copyright: 2021 福富软件
 */
@Service("planConfigServiceImpl")
@Transactional
public class PlanConfigServiceImpl implements IPlanConfigService {

	@Autowired
	private PlanConfigMapper planConfigMapper; //注入应急预案配置模块dao
	@Autowired
	private IBaseDictionaryService baseDictionaryService;

	/**
	 * 新增数据
	 * @param bo 应急预案配置业务对象
	 * @return 应急预案配置id
	 */
	@Override
	public String insert(PlanConfig bo) {
		planConfigMapper.insert(bo);
		return bo.getPlanConfigId();
	}

	/**
	 * 修改数据
	 * @param bo 应急预案配置业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(PlanConfig bo) {
		long result = planConfigMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 应急预案配置业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(PlanConfig bo) {
		long result = planConfigMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 应急预案配置分页数据对象
	 */
	@Override
	public EUDGPagination searchList(int page, int rows, Map<String, Object> params) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<PlanConfig> list = planConfigMapper.searchList(params, rowBounds);
		formatOutData(list, params);
		long count = planConfigMapper.countList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	/**
	 * 字典转化
	 * @param list
	 * @param params
	 */
	private void formatOutData(List<PlanConfig> list,Map<String, Object> params){
		List<BaseDataDict> typeDictList = baseDictionaryService.getDataDictListOfSinglestage(ConstantValue.EMERGENC_PLAN_LEVEL, params.get("userOrgCode")==null?null:params.get("userOrgCode").toString());
		 for (PlanConfig planConfig : list) {
			for (BaseDataDict baseDataDict : typeDictList) {
				  if(baseDataDict.getDictGeneralCode().equals(planConfig.getPlanLevel())) {
					  planConfig.setPlanLevelName(baseDataDict.getDictName());
					  continue;
				  }
			   }
		}
	
	}

	/**
	 * 根据业务id查询数据
	 * @param id 应急预案配置id
	 * @return 应急预案配置业务对象
	 */
	@Override
	public PlanConfig searchById(String id,String userOrgCode) {
		PlanConfig bo = planConfigMapper.searchById(id);
		if(bo.getPlanLevel()!=null) {
			bo.setPlanLevelName(baseDictionaryService.changeCodeToName(ConstantValue.EMERGENC_PLAN_LEVEL, bo.getPlanLevel(), null));
		}
		return bo;
	}

	@Override
	public long countList(Map<String, Object> params) {
		return planConfigMapper.countList(params);
	}

}