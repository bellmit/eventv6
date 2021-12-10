package cn.ffcs.zhsq.decisionMaking;

import java.util.Map;

/**
 * 工作流决策类
 * @author zhangls
 *
 */
public interface IWorkflowDecisionMakingService<T> {
	/**
	 * 决策方法
	 * @param params 
	 * decisionService 决策类对应的实现类 
	 * @return 决策结果
	 */
	public T makeDecision(Map<String, Object> params) throws Exception;
}
