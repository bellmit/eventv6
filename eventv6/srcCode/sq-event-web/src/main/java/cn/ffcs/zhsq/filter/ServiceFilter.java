package cn.ffcs.zhsq.filter;/**
 * Created by Administrator on 2017/8/19.
 */

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter
 *
 * @author zhongshm
 * @create 2017-08-19 15:43
 **/
public class ServiceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
