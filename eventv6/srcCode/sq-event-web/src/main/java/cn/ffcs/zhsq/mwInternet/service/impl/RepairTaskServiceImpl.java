package cn.ffcs.zhsq.mwInternet.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.bo.BaseDataDict;
import cn.ffcs.uam.service.IBaseDictionaryService;
import cn.ffcs.workflow.spring.MWInternetService;
import cn.ffcs.zhsq.mwInternet.service.RepairTaskService;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.RepairTask;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.RepairTask;
import cn.ffcs.zhsq.mybatis.persistence.mwInternet.RepairTaskMapper;
import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * @Description: 报修任务表模块服务实现
 * @Author: guoyd
 * @Date: 04-08 10:19:19
 * @Copyright: 2018 福富软件
 */
@Service("repairTaskServiceImpl")
@Transactional
public class RepairTaskServiceImpl implements RepairTaskService {

	@Autowired
	private RepairTaskMapper repairTaskMapper; //注入报修任务表模块dao
	@Autowired
	private IBaseDictionaryService dictionaryService;
	@Autowired
	private MWInternetService workflowService;
	
	public Map<String, Object> insert(RepairTask bo,Map<String, Object> params){
		
		Long drtId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			repairTaskMapper.insert(bo);
			drtId = bo.getDrtId();
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "告警任务新增失败");
			System.out.println("-----告警任务新增失败------");
			e.printStackTrace();
		}
		
		if(drtId!=null){
			try {
				params.put("formId", drtId);
				resultMap = workflowService.startMWInternetFlow(params);
				resultMap.put("drtId", drtId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				repairTaskMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	public Map<String, Object> update(RepairTask bo,Map<String, Object> params){
		
		Long drtId = bo.getDrtId();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			repairTaskMapper.update(bo);
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "告警任务新增失败");
			System.out.println("-----告警任务新增失败------");
			e.printStackTrace();
		}
		
		if(drtId!=null){
			try {
				params.put("formId", drtId);
				resultMap = workflowService.startMWInternetFlow(params);
				resultMap.put("drtId", drtId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				bo.setState("0");
				repairTaskMapper.update(bo);
				//repairTaskMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 新增数据
	 * @param bo 报修任务表业务对象
	 * @return 报修任务表id
	 */
	@Override
	public Long insert(RepairTask bo) {
		repairTaskMapper.insert(bo);
		return bo.getDrtId();
	}

	/**
	 * 修改数据
	 * @param bo 报修任务表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(RepairTask bo) {
		long result = repairTaskMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 报修任务表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(RepairTask bo) {
		long result = repairTaskMapper.delete(bo);
		return result > 0;
	}

	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchDBList(int page, int rows, Map<String, Object> params,String orgCode) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<RepairTask> list = repairTaskMapper.searchDBList(params, rowBounds);
		formate(list, orgCode);
		long count = repairTaskMapper.countDBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchJBList(int page, int rows, Map<String, Object> params,String orgCode) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<RepairTask> list = repairTaskMapper.searchJBList(params, rowBounds);
		formate(list, orgCode);
		long count = repairTaskMapper.countJBList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchAllList(int page, int rows, Map<String, Object> params,String orgCode) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<RepairTask> list = repairTaskMapper.searchAllList(params, rowBounds);
		formate(list, orgCode);
		long count = repairTaskMapper.countAllList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchFQList(int page, int rows, Map<String, Object> params,String orgCode) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<RepairTask> list = repairTaskMapper.searchFQList(params, rowBounds);
		formate(list, orgCode);
		long count = repairTaskMapper.countFQList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	/**
	 * 查询数据（分页）
	 * @param params 查询参数
	 * @return 告警任务表分页数据对象
	 */
	@Override
	public EUDGPagination searchEndList(int page, int rows, Map<String, Object> params,String orgCode) {
		RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
		List<RepairTask> list = repairTaskMapper.searchEndList(params, rowBounds);
		formate(list, orgCode);
		long count = repairTaskMapper.countEndList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	private void formate(List<RepairTask> list, String orgCode) {
		//报修类型
		List<BaseDataDict> faultType = dictionaryService.getDataDictListOfSinglestage(ConstantValue.REPAIRTASK_FAULT_TYPE, orgCode);
		if(list!=null && list.size()>0){
			for(RepairTask re : list){
				if(null==re.getFaultType()) continue;
				for(BaseDataDict dataDict : faultType){
					if(re.getFaultType().equals(dataDict.getDictGeneralCode())){
						re.setFaultTypeStr(dataDict.getDictName());
						break;
					}
				}
			}
		}
	}

	/**
	 * 根据业务id查询数据
	 * @param id 告警任务表id
	 * @return 告警任务表业务对象
	 */
	@Override
	public RepairTask searchById(Long id,String orgCode) {
		RepairTask bo = repairTaskMapper.searchById(id);
		if(bo!=null){
			List<RepairTask> list = new ArrayList<RepairTask>();
			list.add(bo);
			formate(list, orgCode);
		}
		return bo;
	}

}