package cn.ffcs.zhsq.security;


/**
 * 权限处理接口
 * @author zengy 2014-05-07
 * */
public interface IRightHandler {
	/**
	 * 判断是否拥有权限 这边没有返回值，所以如果没有该权限就直接抛出异常
	 * @param rightCode 权限编号
	 * */
	public void hasRight(String parentCode, String rightCode) throws Exception;
}
