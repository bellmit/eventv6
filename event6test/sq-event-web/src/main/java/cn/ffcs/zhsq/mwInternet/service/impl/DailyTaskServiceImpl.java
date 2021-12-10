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
import cn.ffcs.workflow.spring.MWInternetService;
import cn.ffcs.zhsq.mwInternet.service.DailyTaskService;
import cn.ffcs.zhsq.mybatis.domain.mwInternet.DailyTask;
import cn.ffcs.zhsq.mybatis.persistence.mwInternet.DailyTaskMapper;

/**
 * @Description: 日常任务表模块服务实现
 * @Author: guoyd
 * @Date: 04-08 10:20:12
 * @Copyright: 2018 福富软件
 */
@Service("dailyTaskServiceImpl")
@Transactional
public class DailyTaskServiceImpl implements DailyTaskService {

	@Autowired
	private DailyTaskMapper dailyTaskMapper; //注入日常任务表模块dao
	@Autowired
	private MWInternetService workflowService;
	
	public Map<String, Object> insert(DailyTask bo,Map<String, Object> params){
		
		Long ddtId = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			dailyTaskMapper.insert(bo);
			ddtId = bo.getDdtId();
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "日常任务新增失败");
			System.out.println("-----日常任务新增失败------");
			e.printStackTrace();
		}
		
		if(ddtId!=null){
			try {
				params.put("formId", ddtId);
				resultMap = workflowService.startMWInternetFlow(params);
				resultMap.put("ddtId", ddtId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				dailyTaskMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	public Map<String, Object> update(DailyTask bo,Map<String, Object> params){
		
		Long ddtId = bo.getDdtId();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			dailyTaskMapper.update(bo);
		} catch (Exception e) {
			resultMap.put("status", "0");
			resultMap.put("message", "告警任务新增失败");
			System.out.println("-----告警任务新增失败------");
			e.printStackTrace();
		}
		
		if(ddtId!=null){
			try {
				params.put("formId", ddtId);
				resultMap = workflowService.startMWInternetFlow(params);
				resultMap.put("ddtId", ddtId);
			} catch (Exception e) {
				resultMap.put("status", "0");
				resultMap.put("message", "工作流启动失败");
				System.out.println("-----工作流启动失败------");
				//dailyTaskMapper.delete(bo);
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	/**
	 * 新增数据
	 * @param bo 日常任务表业务对象
	 * @return 日常任务表id
	 */
	@Override
	public Long insert(DailyTask bo) {
		dailyTaskMapper.insert(bo);
		return bo.getDdtId();
	}

	/**
	 * 修改数据
	 * @param bo 日常任务表业务对象
	 * @return 是否修改成功
	 */
	@Override
	public boolean update(DailyTask bo) {
		long result = dailyTaskMapper.update(bo);
		return result > 0;
	}

	/**
	 * 删除数据
	 * @param bo 日常任务表业务对象
	 * @return 是否删除成功
	 */
	@Override
	public boolean delete(DailyTask bo) {
		long result = dailyTaskMapper.delete(bo);
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
		List<DailyTask> list = dailyTaskMapper.searchDBList(params, rowBounds);
		formate(list, orgCode);
		long count = dailyTaskMapper.countDBList(params);
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
		List<DailyTask> list = dailyTaskMapper.searchJBList(params, rowBounds);
		formate(list, orgCode);
		long count = dailyTaskMapper.countJBList(params);
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
		List<DailyTask> list = dailyTaskMapper.searchAllList(params, rowBounds);
		formate(list, orgCode);
		long count = dailyTaskMapper.countAllList(params);
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
		List<DailyTask> list = dailyTaskMapper.searchFQList(params, rowBounds);
		formate(list, orgCode);
		long count = dailyTaskMapper.countFQList(params);
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
		List<DailyTask> list = dailyTaskMapper.searchEndList(params, rowBounds);
		formate(list, orgCode);
		long count = dailyTaskMapper.countEndList(params);
		EUDGPagination pagination = new EUDGPagination(count, list);
		return pagination;
	}
	
	private void formate(List<DailyTask> list, String orgCode) {
		
	}

	/**
	 * 根据业务id查询数据
	 * @param id 告警任务表id
	 * @return 告警任务表业务对象
	 */
	@Override
	public DailyTask searchById(Long id,String orgCode) {
		DailyTask bo = dailyTaskMapper.searchById(id);
		if(bo!=null){
			List<DailyTask> list = new ArrayList<DailyTask>();
			list.add(bo);
			formate(list, orgCode);
		}
		return bo;
	}

}