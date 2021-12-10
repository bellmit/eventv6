package cn.ffcs.zhsq.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-5-13
 * Time: 上午9:32
 * To change this template use File | Settings | File Templates.
 */
public class SystemInterceptor implements HandlerInterceptor {
    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //logger.debug("----------->Pre-handle");
        return true;
    }

    public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) return;
        if(logger.isDebugEnabled()) {
            logger.debug("----------->request.getServerName():"+request.getServerName());
            logger.debug("----------->request.getPathInfo():"+request.getPathInfo());
            logger.debug("----------->request.getContextPath():"+request.getContextPath());
            logger.debug("----------->request.getRequestURI():"+request.getRequestURI());
            logger.debug("----------->request.getRequestURL():"+request.getRequestURL());
            logger.debug("----------->view:"+modelAndView.getViewName());
        }
        String contentPath = request.getContextPath()+"/wap";
        String uri = request.getRequestURI();
        String view = modelAndView.getViewName();
        if((!view.startsWith("redirect")&&!view.startsWith("forward")) && uri.toLowerCase().startsWith(contentPath)) {
            modelAndView.setViewName("/wap"+modelAndView.getViewName());
            if(logger.isDebugEnabled()) {
                logger.debug("----------->view change:"+modelAndView.getViewName());
            }
        }
    }

    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //logger.debug("----------->After completion handle");
    }
}
