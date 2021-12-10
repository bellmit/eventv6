package cn.ffcs.zhsq.mybatis.persistence.docking.event;

import java.util.Map;

public interface OperationInstanceMapper {
	
	
	public Long update(Map<String,Object> param);
	
	public Long updateIsTimeOut(Map<String,Object> param);
	
	public Long updateWfTask(Map<String,Object> param);
	
	
}