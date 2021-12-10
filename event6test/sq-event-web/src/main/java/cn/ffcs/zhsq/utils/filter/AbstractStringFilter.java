/**
 * 
 */
package cn.ffcs.zhsq.utils.filter;

/**
 * 模板方法(Template Method)模式
 * 
 * 抽象的过滤器，将相同的实现部分抽象出来
 * 
 * @author Administrator
 *
 */
public abstract class AbstractStringFilter implements StringFilter {
	
	private StringFilter stringFilter;
	
	public void setNextStringFilter(StringFilter stringFilter) {
		this.stringFilter = stringFilter;
	}

	public String filter(String source) {
		String target = doFilter(source);
		if (stringFilter == null) {
			return target;
		}
		return stringFilter.filter(target);
	}
	
	/**
	 * 模板抽象方法,传入要处理的string,返回处理完的string
	 * 遵循模板方法doXXX命名方式
	 * @param source
	 * @return
	 */
    public abstract String doFilter(String source);
}
