package cn.ffcs.zhsq.docking.event.service.impl;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.zhsq.docking.event.IOperationInstanceService;
import cn.ffcs.zhsq.mybatis.persistence.docking.event.OperationInstanceMapper;
import cn.ffcs.zhsq.utils.CommonFunctions;


@Service(value="operationInstanceService")
public class OperationInstanceServiceImpl implements IOperationInstanceService {

	@Autowired OperationInstanceMapper operationInstanceMapper;
	
	@Override
	public Long update(Map<String, Object> param) {
		
		// 将历史流程的实例ID设置为无效的，去除所有历史流程(param:oldInstanceId,instanceId)
		if (CommonFunctions.isNotBlank(param, "oldInstanceId")) {
			return operationInstanceMapper.updateWfTask(param);
		}
		// 将实例表的采集或者结束时间做更新（param:startTime或者endTime,instanceId）
		return operationInstanceMapper.update(param);
	}

	@Override
	public Long updateIsTimeOut(Map<String, Object> param) {
		return operationInstanceMapper.updateIsTimeOut(param);
	}
	


}
