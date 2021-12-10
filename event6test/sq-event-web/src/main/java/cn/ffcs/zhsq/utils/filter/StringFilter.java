/**
 * 
 */
package cn.ffcs.zhsq.utils.filter;

/**
 * 字符串过滤接口（Decorator模式）
 * 
 * Decorator接口Decorator是嵌套结构，还会有建造者(Builder)模式或简单工厂模式对其进行进一步封装
 * 
 * 主要用于过滤用户输入信息
 * 
 * @author Administrator
 *
 */
public interface StringFilter {
	
	/**
	 * 关联下一个装饰器
	 * @param stringFilter
	 */
	public void setNextStringFilter(StringFilter stringFilter);
	
	/**
	 * 处理过滤
	 * @param source 源输入
	 * @return
	 */
	public String filter(String source);
}
