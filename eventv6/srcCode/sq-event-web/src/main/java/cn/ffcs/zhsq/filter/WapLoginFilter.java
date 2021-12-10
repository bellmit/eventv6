/**
 * 
 */
package cn.ffcs.zhsq.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.ffcs.cookie.service.UserCookieService;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IUserService;
import cn.ffcs.shequ.grid.domain.db.OrgEntityInfo;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.service.OrgEntityInfoOutService;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.CookieUtils;
import cn.ffcs.zhsq.utils.ParamUtils;
import cn.ffcs.zhsq.wap.grid.common.Constants;


/**
 * 登录过滤器
 * @author guohh
 *
 */
public class WapLoginFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	
	public void destroy() {
		// TODO Auto-generated method stub

	}
	/**
	 * 用户登录
	 * @param req
	 * @param res
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doLogin(HttpServletRequest req, HttpServletResponse res,
                         String userName,String orgCode,
                         FilterChain filterChain)throws IOException, ServletException{

		HttpSession session = req.getSession();
		ServletContext servletContext = session.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		IUserService userService = (IUserService)ctx.getBean("userService");
    	
    	//-- 允许主动输入SQ_USER_ID进行认证
//	    	String loginCode = req.getParameter("loginCode");
//	    	String orgCode = req.getParameter("orgCode");
		String uumemkey = req.getParameter("uumemkey");
		if(uumemkey!=null && !"".equals(uumemkey))
			session.setAttribute(ConstantValue.UUMEMKEY_IN_SESSION, uumemkey);
		//-- 判断userId
		if(userName!=null && !"".equals(userName) && orgCode!=null && !"".equals(orgCode)) {
			try {
				UserInfo userInfo = userService.getUserExtraInfoByUsername(userName, orgCode);
				if(userInfo!=null) {
					session.setAttribute(ConstantValue.USER_IN_SESSION, userInfo);
					filterChain.doFilter(req, res);
				} else {
					writeErrorResponse(req, res, "未获取到登录信息！");
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				writeErrorResponse(req, res, "参数错误！");
			}
		} else {
			writeErrorResponse(req, res, "未获取到登录信息！");
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        String orgCode = ParamUtils.getString(req,"orgCode",ParamUtils.getString(req,"userOrgCode",null));
        long positionId = ParamUtils.getLong(req, "positionId", -1);
         /* 获取用户基本信息 check token  */
        {
            WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            IUserService userService = (IUserService)ctx.getBean("userService");
            UserCookieService userCookieService = (UserCookieService) ctx.getBean("userCookieService");
            OrgEntityInfoOutService orgEntityInfoService = (OrgEntityInfoOutService)ctx.getBean("systemOrgEntityInfoService");
            JSONObject o = checkLogin(session,req,res,userService,orgEntityInfoService,orgCode,positionId);
            if (!Constants.STATUS_SUCCESS.equals(o.get("status").toString())) {
                writeErrorResponse(req, res, o.get("desc").toString());
                return;
            }

            filterChain.doFilter(req, res);
        }
	}
	
	/**
	 * 向客户端写错误提示信息
	 * @param response
	 * @throws IOException
	 */
	private void writeErrorResponse(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("<HEAD><TITLE>"+message+"</TITLE></HEAD>");
			out.println("<BODY>");
			out.print("<span style='font-size: 24px; font-weight: bold;'>"+message+"</span>");
			out.println("</BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}


	
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}


    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    public static net.sf.json.JSONObject checkLogin(HttpSession session,HttpServletRequest request,HttpServletResponse response,
                                                    IUserService userService,
                                                    OrgEntityInfoOutService orgEntityInfoService,
                                                    String userOrgCode,long positionId){
        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        try{
            //get tokenKey
            String tokenKey = request.getHeader(Constants.CLIENT_TOKEN_ID);
            if (StringUtils.isEmpty(tokenKey)) {
                Cookie cookie = CookieUtils.getCookie(request, Constants.CLIENT_TOKEN_ID);
                if (cookie != null) tokenKey = cookie.getValue();
            }
            if (StringUtils.isEmpty(tokenKey)) {
                tokenKey = ParamUtils.getString(request, Constants.CLIENT_TOKEN_ID, null);
            }

            //get session userinfo
            UserInfo userInfo = null;
            if(session.getAttribute(ConstantValue.USER_IN_SESSION) != null) {
                userInfo = (UserInfo)session.getAttribute(ConstantValue.USER_IN_SESSION);
            }
            //
            String preTime = null;
            if (userInfo == null || (userOrgCode != null && !userOrgCode.equals(userInfo.getOrgCode()))) {
                if(StringUtils.isEmpty(userOrgCode)){
                    json.put("status", Constants.STATUS_FAIL);
                    json.put("desc", "请求传递的参数不完全或不正确");
                    return json;
                }
                if (StringUtils.isEmpty(tokenKey)) {
                    json.put("status", Constants.STATUS_FAIL);
                    json.put("desc", "获取不到认证令牌");
                    return json;
                }
                try {
                    if (tokenKey.indexOf("-") == -1) {
                        tokenKey = new String(Base64.decode(tokenKey));
                    }
                } catch (Exception e) {
                }

                String[] tokenInfo = tokenKey.split("-"); //20130605091105-api_zhsq-41-54387
                long userId = Long.parseLong(tokenInfo[2]);
                preTime = tokenInfo[0];
                Date preDate = formatter.parse(preTime);
                long diffTime = (new Date()).getTime() - preDate.getTime();
                if (diffTime > (Constants.TOKEN_TIMEOUT * 60 * 1000)) {
                    json.put("status", Constants.STATUS_FAIL);
                    json.put("desc", "认证信息已失效,请重新登入");
                    // request.getSession().invalidate();
                    //return json;
                }
                
                userInfo = userService.getUserExtraInfoByUserId(userId, userOrgCode);
                if (userInfo == null) {
                    json.put("status", Constants.STATUS_FAIL);
                    json.put("desc", "无效的Token");
                    return json;
                }
                if (positionId != -1) {
                    List<OrgEntityInfoBO> orgEntityInfoBOList = orgEntityInfoService.findUserLocationList(userInfo.getUserId(), userInfo.getOrgId(), positionId);
                    if (orgEntityInfoBOList != null && orgEntityInfoBOList.size() > 0) {
                    	userInfo.setInfoOrgList(orgEntityInfoBOList);
                    }
                }

                session.setAttribute(ConstantValue.USER_IN_SESSION, userInfo);
            }
            //update tokenKey
            if(tokenKey != null && preTime != null) {
                //String newTokenKey = formatter.format(new Date())+tokenKey.substring(preTime.length()-1,tokenKey.length());
                response.setHeader("tokenKey",tokenKey);
            }

            json.put("status", Constants.STATUS_SUCCESS);
            json.put("desc", "登入成功");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status", Constants.STATUS_FAIL);
            json.put("desc", "认证信息出现异常:"+e.toString());
            return json;
        }
    }
}
