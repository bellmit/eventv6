package cn.ffcs.zhsq.smsTask.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.system.publicUtil.EUDGPagination;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.mybatis.domain.smsTask.SmsTask;
import cn.ffcs.zhsq.mybatis.persistence.smsTask.SmsTaskMapper;
import cn.ffcs.zhsq.smsTask.service.ISmsTaskService;
import cn.ffcs.zhsq.utils.ConstantValue;

@Service(value = "smsTaskServiceImpl")
public class SmsTaskServiceImpl implements ISmsTaskService {
	
	@Autowired
	private SmsTaskMapper smsTaskMapper;
	
	@Autowired
	private IFunConfigurationService configurationService;
	
	@Override
	public Long insertSmsTask(SmsTask smsTask){
		boolean result = false;
		smsTask = checkSmsTask(smsTask);
		if(this.checkSmsTaskValid(smsTask)){
			if(smsTask.getTaskId() != null){
				result = updateSmsTaskById(smsTask);
			}else{
				result = smsTaskMapper.insert(smsTask) > 0;
			}
		}
		
		return result ? smsTask.getTaskId() : -1L;
	}
	
	@Override
	public Long insertSmsTask(List<SmsTask> smsTaskList){
		int result = 0;
		
		if(smsTaskList!=null && smsTaskList.size()>0){
			List<SmsTask> smsTaskUpdateList = new ArrayList<SmsTask>();
			List<SmsTask> smsTaskInsertList = new ArrayList<SmsTask>();
			for(SmsTask smsTask : smsTaskList){
				smsTask = checkSmsTask(smsTask);
				if(this.checkSmsTaskValid(smsTask)){
					if(smsTask.getTaskId() != null){
						smsTaskUpdateList.add(smsTask);
					}else{
						smsTaskInsertList.add(smsTask);
					}
				}
			}
			
			if(smsTaskInsertList.size() > 0){
				result += smsTaskMapper.insertSmsTaskBatch(smsTaskInsertList);
			}
			
			if(smsTaskUpdateList.size() > 0){
				result += smsTaskMapper.updateSmsTaskBatch(smsTaskUpdateList);
			}
		}
		
		
		return Long.valueOf(result);
	}
	
	@Override
	public boolean updateSmsTaskById(SmsTask smsTask){
		int result = smsTaskMapper.update(smsTask);
		return result > 0;
	}
	
	@Override
	public boolean deleteSmsTaskById(Long taskId){
		int result = smsTaskMapper.delete(taskId);
		return result > 0;
	}
	
	@Override
	public Integer findSmsTaskCount(Map<String, Object> params){
		int count = smsTaskMapper.findCountByCriteria(params);
		
		return count;
	}
	
	@Override
	public EUDGPagination findSmsTaskPagination(int pageNo,
			int pageSize, Map<String, Object> params) {
		pageNo = pageNo<1 ? 1:pageNo;
		pageSize = pageSize<1?10:pageSize;
		RowBounds rowBounds = new RowBounds((pageNo-1)*pageSize, pageSize);
		
		Integer count = findSmsTaskCount(params);
		List<SmsTask> list = smsTaskMapper.findPageListByCriteria(params, rowBounds);
		
		EUDGPagination eudgPagination = new EUDGPagination(count, list);
		
		return eudgPagination;
	}
	
	@Override
	public List<SmsTask> findSmsTaskList(Map<String, Object> params){
		List<SmsTask> list = smsTaskMapper.findPageListByCriteria(params);
		
		return list;
	}
	
	@Override
	public List<Map> findPageListByDynamic(Map<String, Object> param){
		List<Map> list = smsTaskMapper.findPageListByDynamic(param);
		
		return list;
	}
	
	@Override
	public List<Map> findListByView(Map<String, Object> param){
		List<Map> list = smsTaskMapper.findListByView(param);
		
		return list;
	}
	
	@Override
	public boolean isSmsTaskExist(SmsTask smsTask){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizId", smsTask.getBizId());
		params.put("bizType", smsTask.getBizType());
		params.put("taskType", smsTask.getTaskType());
		params.put("taskStatus", smsTask.getTaskStatus());
		params.put("phoneNum", smsTask.getPhoneNum());
		
		Integer smsTaskCount = findSmsTaskCount(params);
		
		return smsTaskCount > 0;
	}
	
	@Override
	public boolean isSmsTaskAble(SmsTask smsTask){
		smsTask = this.checkSmsTask(smsTask);
		
		return smsTask != null;
	}
	
	/**
	 * 检测当前smsTask是否有相应的同类任务
	 * 有未完成的，则更新成功、失败数值；
	 * 有已完成的，则清空smsTask；
	 * 没有同类任务，则保持原有
	 * @param smsTask
	 * @return 变更后的smsTask
	 */
	private SmsTask checkSmsTask(SmsTask smsTask){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bizId", smsTask.getBizId());
		params.put("bizType", smsTask.getBizType());
		params.put("taskType", smsTask.getTaskType());
		params.put("taskStatus", ISmsTaskService.TASK_STATUS_UNFINISH);
		params.put("phoneNum", smsTask.getPhoneNum());
		List<SmsTask> smsTaskList = findSmsTaskList(params);
		
		if(smsTaskList!=null && smsTaskList.size()>0){//若有未完成的任务，则更新相应的发送成功次数和发送失败次数
			SmsTask smsTmp = smsTaskList.get(0);
			if(smsTmp.getSuccessCount() == null){
				smsTmp.setSuccessCount(0);
			}
			
			if(smsTmp.getFailCount() == null){
				smsTmp.setFailCount(0);
			}
			
			if(smsTask.getSuccessCount() == null){
				smsTask.setSuccessCount(0);
			}
			
			if(smsTask.getFailCount() == null){
				smsTask.setFailCount(0);
			}
			
			smsTmp.setSuccessCount(smsTmp.getSuccessCount()+smsTask.getSuccessCount());
			smsTmp.setFailCount(smsTmp.getFailCount()+smsTask.getFailCount());
			smsTmp.setTaskStatus(smsTask.getTaskStatus());
			
			String failCountLimit = configurationService.turnCodeToValue(ConstantValue.FAIL_COUNT_LIMIT, "", IFunConfigurationService.CFG_TYPE_FACT_VAL, IFunConfigurationService.CFG_ORG_TYPE_0);
			int failCount = 0;
			if(StringUtils.isNotBlank(failCountLimit)){
				try{
					failCount = Integer.valueOf(failCountLimit);
				}catch(Exception e){
					failCount = 0;
					e.printStackTrace();
				}
			}
			
			if(smsTmp.getFailCount() <= failCount){
				smsTask = smsTmp;
			}else{
				smsTmp.setTaskStatus(ISmsTaskService.TASK_STATUS_FINISH);
				this.updateSmsTaskById(smsTmp);//结束任务，防止重复读取该任务
				
				smsTask = null;
			}
			
		}else{
			params.put("taskStatus", ISmsTaskService.TASK_STATUS_FINISH);
			smsTaskList = findSmsTaskList(params);
			if(smsTaskList!=null && smsTaskList.size()>0){
				smsTask = null;//对于已存在的同类任务，则置空当前的smsTask
			}
		}
		
		return smsTask;
	}
	
	/**
	 * 判断smsTask属性的有效性
	 * @param smsTask
	 * @return 必填属性有效则返回true
	 */
	private boolean checkSmsTaskValid(SmsTask smsTask){
		boolean flag = true;
		if(smsTask == null){
			flag = false;
		}else{
			if(smsTask.getBizId() == null){
				flag = false;
			}else if(StringUtils.isBlank(smsTask.getBizType())){
				flag = false;
			}else if(StringUtils.isBlank(smsTask.getTaskType())){
				flag = false;
			}else if(StringUtils.isBlank(smsTask.getPhoneNum())){
				flag = false;
			}
		}
		
		return flag;
	}
}
