package cn.ffcs.zhsq.timeApplication;

import java.util.Map;

/**
 * 时限申请回调
 * @author zhangls
 *
 */
public interface ITimeApplicationCallbackService {
	public void timeApplicationCallback(Map<String, Object> params);
}
