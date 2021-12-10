package cn.ffcs.zhsq.docking.event;

import java.util.Map;

/**
 * 
 * 将事件的历史流程的实例进行更新
 * @author 吴兴钱
 *
 */
public interface IOperationInstanceService {
	
	/**
	 * 将历史流程的实例ID设置为无效的，去除所有历史流程
	 * @param  oldInstanceId  
	 * @param  instanceId
	 * @return
	 */
    public Long update(Map<String,Object> param);
    
    /**
     * 对应事件的办理流程设置为超时
     * @param  	isTimeOut 1：超时 0：未超时
     * @param	taskId
     * @return
     */
    public Long updateIsTimeOut(Map<String,Object> param);


}
