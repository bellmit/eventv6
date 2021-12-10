/**
 * 
 */
package cn.ffcs.zhsq.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import cn.ffcs.zhsq.utils.filter.StringFilter;
import cn.ffcs.zhsq.utils.filter.StringFilterFactory;
import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 输出字符串文本过滤标签
 * 已经在spring-dispatcher.xml中配置了标签
 * 用法<@string_filter chain="html,js,a,url,dirty">三等功${test}sdf的s${test23}斯蒂芬森个</@string_filter>
 * 使用StringFilter装饰器进行过滤处理
 * @author Administrator
 * 
 */
public class StringFilterDirective implements TemplateDirectiveModel {

	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		// 检查是否传递参数，此指令禁止传参！
		if (params.isEmpty()) {
			throw new TemplateModelException("This directive need some parameters.");
		}

		if (!params.containsKey("chain")) {
			throw new TemplateModelException("This directive require 'chain' parameter.");
		}

		// 禁用循环变量
		if (loopVars.length != 0) {
			throw new TemplateModelException("This directive doesn't allow loop variables.");
		}

		SimpleScalar chain = (SimpleScalar) params.get("chain");

		// 指令内容体不为空
		if (body != null) {
			body.render(new StringFilterWriter(env.getOut(), chain.getAsString()));
		} else {
			throw new RuntimeException("missing body");
		}
	}

	/**
	 * 输出流的包装器(字符串过滤器)
	 */
	private static class StringFilterWriter extends Writer {

		private final Writer out;
		private final StringFilter stringFilter;

		StringFilterWriter(Writer out, String chain) {
			this.out = out;
			this.stringFilter = StringFilterFactory.getInstance().getStringFilterChain(chain);
		}

		public void write(char[] cbuf, int off, int len) throws IOException {
			char[] transformedCbuf = new char[len];
			for (int i = 0; i < len; i++) {
				transformedCbuf[i] = cbuf[i + off];
			}
			String result = stringFilter.filter(String.valueOf(transformedCbuf));
			out.write(result);
		}

		public void flush() throws IOException {
			out.flush();
		}

		public void close() throws IOException {
			out.close();
		}
	}

}
