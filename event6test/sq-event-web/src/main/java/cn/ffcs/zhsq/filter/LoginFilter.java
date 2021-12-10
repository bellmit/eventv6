/**
 *
 */
package cn.ffcs.zhsq.filter;
import cn.ffcs.cookie.bo.UserCookie;
import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.cookie.service.UserCookieService;
import cn.ffcs.shequ.base.domain.db.OrgExtraInfo;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.base.service.IOrgService;
import cn.ffcs.uam.bo.FunConfigureSetting;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.PartyUserCertifyBO;
import cn.ffcs.uam.bo.PriActionBO;
import cn.ffcs.uam.bo.UserBO;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.uam.service.UserManageOutService;
import cn.ffcs.zhsq.utils.Base64;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.CookieUtils;
import cn.ffcs.zhsq.utils.DateUtils;
import cn.ffcs.zhsq.utils.domain.App;
import com.ffcs.auth.platform.spring.util.AESEncoder;
import com.ffcs.auth.platform.spring.util.RSAEncoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 登录过滤器
 * @author guohh
 *
 */
public class LoginFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	static Map<String, Object> styleMap = new HashMap<String, Object>();

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		logger.info("requestUrl: "+req.getRequestURL());
        /*String imgHttpString="http://img";
        String imgFileDomain="http://img.fjsq.org/";
        String uiHttpString="http://static.";
        String uiFileDomain="http://static.fjsq.org/";
        String gmisDomainStr = "http://gmisdev.";
        String gmisPath = "";
        String newDoMain=this.getTopDomain(req.getRequestURL().toString());
        req.getSession().setAttribute(ConstantValue.NEW_ZZ_PLATFORM_DOMAIN, newDoMain);
        req.getSession().setAttribute(ConstantValue.IMG_ZZGRID_DOMAIN, imgFileDomain);
        req.getSession().setAttribute(ConstantValue.SQ_UI_DOMAIN, uiFileDomain);

        if(ConstantValue.IS_PRODUCT) {
        	System.out.println("url=" + req.getRequestURL().toString());
        	System.out.println("ConstantValue.ACCESS_HOST=" + ConstantValue.ACCESS_HOST);
            if(!req.getRequestURL().toString().contains(ConstantValue.ACCESS_HOST)) {
                res.sendRedirect(ConstantValue.UAM_LOGIN_URL);
                return;
            }
        }*/

		//-- upload的不过滤
		String reqUri = req.getRequestURI();
		//用户界面风格
		String uiStyle = "";
		if(reqUri.contains("upload") ||reqUri.contains("checkUsersHasTrajectoryJsonp") || reqUri.contains("getImportStatus")
				|| reqUri.contains("getSyncStatus")|| reqUri.contains("getExportStatus")
				|| reqUri.contains("zhsq/requestion/appSave")||reqUri.contains("zhsq/requestion/appDetail")
				|| reqUri.contains("zhsq/requestion/appUpdateOwnerDeal")
				|| reqUri.contains("zhsq/disputeMediation/getResInfo")
				|| reqUri.contains("zhsq/disputeMediation/resolveSave")
				|| reqUri.contains(".applet")//不用登录请用这个后缀
		) {
			filterChain.doFilter(req, res);
			return;
		} else {
			//-- 获取session中的用户信息
			HttpSession session = req.getSession();
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			ServletContext servletContext = session.getServletContext();
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

			if(request.getParameter("defaultPageAction")!=null){
				session.setAttribute("defaultPageAction", request.getParameter("defaultPageAction"));
			}else{
				session.setAttribute("defaultPageAction", null);
				//session.removeAttribute("defaultPageAction");
			}

			//-- 动态获取域名
            /*try {
                FileUrlProvideService fileUrlProvideService = (FileUrlProvideService)ctx.getBean("fileUrlProvideService");
                imgHttpString = fileUrlProvideService.getFileDomain();
                String domain = imgHttpString + newDoMain+"/"+ConstantValue.RESOURSE_DOMAIN_KEY;//构造http://img.fjsq.org/zzgrid
//                String domain = fileUrlProvideService.getFileDomain(ConstantValue.RESOURSE_DOMAIN_KEY);
                logger.info("image domain------"+domain);
                if(!StringUtils.isBlank(domain)) {
                    if(!domain.endsWith("/")) domain = (domain+"/");
                    ConstantValue.RESOURSE_SERVER_PATH = domain;
                } else {
                    ConstantValue.RESOURSE_SERVER_PATH = ConstantValue.DEFAULT_RESOURSE_SERVER_PATH;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ConstantValue.RESOURSE_SERVER_PATH = ConstantValue.DEFAULT_RESOURSE_SERVER_PATH;
            }
            logger.info("image domain used------"+ConstantValue.RESOURSE_SERVER_PATH);*/
			checkLogin(req, res);

			CacheService cacheService = (CacheService) ctx.getBean("cacheService");

			logger.info("==================统一认证 认证开始===================================");
			if (CookieUtils.getCookie(req, "UAM_TOKEN_FLAG") != null && (CookieUtils.getCookie(req, "UAM_TOKEN_FLAG").getValue()).equals("0")) {
				String tokenflag = CookieUtils.getCookie(req, "UAM_TOKEN_FLAG").getValue();
				String resourcekey = CookieUtils.getCookie(req, "resourcekey").getValue();
				String resource = CookieUtils.getCookie(req, "resource").getValue();
				if (tokenflag == null || !"0".equals(tokenflag) || resourcekey == null || resource == null) {
					logger.info("cookies有认证信息存在---------------------->但是tokenflag为空，登录失败，清空session");
					userInfo=null;
					session.invalidate();
				} else {
					try {
						byte[] result = RSAEncoder.decryptByPublicKey(Base64.decode(resourcekey), ConstantValue.UAM_PUBLIC_KEY.getBytes());
						String keyresoure = new String(result);
						logger.info("keyresoure---------------------->" + keyresoure);
						String token = AESEncoder.decryptAES(resource, keyresoure);
						logger.info("token---------------------->" + token);

						UserCookieService userCookieService = (UserCookieService) ctx.getBean("userCookieService");
						UserManageOutService userManageService = (UserManageOutService) ctx.getBean("userManageService");

						//-- 根据token从统一认证中获取登录的用户信息
						//UserCookie userCookie = userCookieService.getUserCookie(token);
						//-- 新版userCookie改造，改成从map中获取userCookie
						Map<String, Object> cookieMapInfo = userCookieService.getUserCookieAsMap(token);
						if(cookieMapInfo!=null) {
	                       /* String redirectDomain = (String) cookieMapInfo.get(ConstantValue.REDIRECT_DOMAIN_KEY);
	                        if(!StringUtils.isBlank(redirectDomain)) ConstantValue.UAM_LOGIN_URL = redirectDomain;
	                        if(cookieMapInfo.get(ConstantValue.USER_COOKIE_UAM_DOMAIN_KEY)!=null){
	                        	ConstantValue.UAM_DOMAIN_URL_PATH = cookieMapInfo.get(ConstantValue.USER_COOKIE_UAM_DOMAIN_KEY).toString();
	                        }*/
							UserCookie userCookie = (UserCookie) cookieMapInfo.get(ConstantValue.USER_COOKIE_KEY);
							if(userCookie==null || userCookie.getUserId()==null || userCookie.getUserId()<=0L
									|| userCookie.getOrgCode()==null || "".equals(userCookie.getOrgCode())
									|| userCookie.getLocationList()==null || userCookie.getLocationList().size()==0) {
								logger.info("统一认证memcache中用户信息有误，系统判定非法进入，清空session");
								userInfo=null;
								session.invalidate();
							} else {
								session.setAttribute("SQ_ORG_CODE",userCookie.getOrgCode());
	                            /*try {
	                            	IFunConfigurationService configureationService = (IFunConfigurationService)ctx.getBean("funConfigurationService");
	                            	String staticDomain =  configureationService.changeCodeToValue(ConstantValue.APP_DOMAIN, ConstantValue.STATIC_DOMAIN, IFunConfigurationService.CFG_TYPE_URL, userCookie.getOrgCode());
	                            	String gmisDomain = configureationService.changeCodeToValue(ConstantValue.APP_DOMAIN, ConstantValue.GMIS_DOMAIN, IFunConfigurationService.CFG_TYPE_URL, userCookie.getOrgCode());
	                            	gmisPath = configureationService.changeCodeToValue(ConstantValue.APP_DOMAIN, ConstantValue.GMIS_CONTEXT_PATH, IFunConfigurationService.CFG_TYPE_URL, userCookie.getOrgCode());
	                            	if(null != staticDomain && !"".equals(staticDomain)){
	                            		uiHttpString = staticDomain;
	                            	}
	                            	if(StringUtils.isNotBlank(gmisDomain)){
	                            		gmisDomainStr = gmisDomain;
	                            	}
	                            	logger.info("gmisDomainStr------------------------"+gmisDomainStr + ConstantValue.UAM_DOMAIN_URL_PATH + gmisPath);
	                            	logger.info("staticDomain------------------------"+uiHttpString);
	                            } catch (Exception e) {
	                            	e.printStackTrace();
	                    			logger.error("功能配置服务调用失败噢！");
	                    		}
	                            session.setAttribute(ConstantValue.SQ_GMIS_DOMAIN, gmisDomainStr + ConstantValue.UAM_DOMAIN_URL_PATH + gmisPath);//gmis域名
	                        	uiFileDomain = uiHttpString + ConstantValue.UAM_DOMAIN_URL_PATH;
	                        	session.setAttribute(ConstantValue.SQ_UI_DOMAIN, uiFileDomain);	//-- 公共样式目录
*/

								//查看是否开启终端登录验证
								boolean isValid =userManageService.IsValidToken(userCookie.getUserId(), token);
								if(!isValid) {
									if(session!=null) {
										Enumeration<String> enumeration = session.getAttributeNames();
										while (enumeration.hasMoreElements()) {
											String key = enumeration.nextElement().toString();
											session.removeAttribute(key);
										}
									}
									res.sendRedirect(cacheService.getUamLoginUrl() + "/redirectLogin?type=1&redirectUrl=" + getAllUrl(req));
									return;
								}

								String orgCode = null;
								List<OrgEntityInfoBO> orgList = userCookie.getLocationList();
								if(orgList != null && orgList.size() > 0){
									orgCode = orgList.get(0).getOrgCode();
									logger.info("=======userCookie.getLocationList() has elements, the fist value is:" + orgCode);
								}

								if(StringUtils.isBlank(orgCode)){
									logger.info("=======userCookie.getLocationList not get orgCode, so get userCookie.getOrgCode()");
									orgCode = userCookie.getOrgCode();
								}

								logger.info("orgCode:"+orgCode);
								session.setAttribute("SQ_GRID_CODE",orgCode);
								if(StringUtils.isNotBlank(userCookie.getUiStyle())) {
									uiStyle = userCookie.getUiStyle();
								}

								Boolean styleMapFlag = false;//功能配制查找样式分类
								for (String str : styleMap.keySet()) {
									//map.keySet()返回的是所有key的值
									if(userCookie.getOrgCode().equals(str)){
										styleMapFlag = true;//得到每个key多对用value的值
										req.setAttribute("styleCSS", StringUtils.trimToEmpty(styleMap.get(str).toString()));
										break;
									}
								}
								if(!styleMapFlag){
									IFunConfigurationService funConfigurationService = (IFunConfigurationService)ctx.getBean("funConfigurationService");
									try {
										List<FunConfigureSetting> funConfigurationList = funConfigurationService.findConfigureSettingLatestList("GGYS_CATALOG", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0,  1);
										if(!funConfigurationList.isEmpty()){
											System.out.println("-------"+funConfigurationList.size()+"--------");
											System.out.println("-------"+funConfigurationList.toString()+"--------");
											FunConfigureSetting temp = null;
											for(FunConfigureSetting fun : funConfigurationList){
												if(temp==null){
													temp = fun;
													System.out.println("---orgcode----"+temp.getOrgCode()+"--------");
												}else{
													if(fun.getOrgCode().length()>temp.getOrgCode().length()){
														temp = fun;
														System.out.println("---orgcode----"+temp.getOrgCode()+"--------");
													}
												}
											}
											System.out.println("---CfgVal----"+temp.getCfgVal()+"--------");
											styleMap.put(userCookie.getOrgCode(), temp.getCfgVal());
											req.setAttribute("styleCSS", temp.getCfgVal());
										}else{
											styleMap.put(userCookie.getOrgCode(), "");
											req.setAttribute("styleCSS", "");
										}
									} catch (Exception e) {
										req.setAttribute("styleCSS", "");
										e.printStackTrace();
										logger.error("功能配置服务调用失败噢！");
									}
								}
								session.setAttribute(ConstantValue.UI_STYLE, uiStyle);
								session.setAttribute(ConstantValue.USER_COOKIE_IN_SESSION, userCookie);
								//add by zhanggf for 人口管理
								session.setAttribute(ConstantValue.SESSION_KEY_USERCOOKIE, userCookie);
								session.setAttribute(ConstantValue.SESSION_KEY_USERID, userCookie.getUserId());
								List<OrgEntityInfoBO> boList = userCookie.getLocationList();
								session.setAttribute(ConstantValue.SESSION_KEY_LOCATION_LIST, boList);
								//end
								boolean userInfoChange = false;
								UserInfo oldUserInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
								if(oldUserInfo==null) userInfoChange = true;
								if(!userInfoChange && oldUserInfo.getUserId().longValue()!=userCookie.getUserId().longValue()) userInfoChange = true;
								if(!userInfoChange && !oldUserInfo.getOrgCode().equals(userCookie.getOrgCode())) userInfoChange = true;
								if(!userInfoChange && oldUserInfo.getInfoOrgList().size()!=userCookie.getLocationList().size()) userInfoChange = true;
								if(!userInfoChange) {
									for(OrgEntityInfoBO org : userCookie.getLocationList()) {
										boolean isExists = false;
										for(OrgEntityInfoBO org1 : oldUserInfo.getInfoOrgList()) {
											if(org.getOrgId().longValue()==org1.getOrgId().longValue()) {
												isExists = true;
												break;
											}
										}
										if(!isExists) {
											userInfoChange = true;
											break;
										}
									}
								}
								if(userInfoChange) {
									//-- 从userCookie生成本地UserInfo信息
									userInfo = new UserInfo();
									Long verifyMobile = userCookie.getVerifyMobile();
									userInfo.setOrgCode(userCookie.getOrgCode());
									userInfo.setOrgId(userCookie.getOrgId());
									userInfo.setOrgName(userCookie.getOrgName());
									userInfo.setPartyId(userCookie.getPartyId());
									userInfo.setPartyName(userCookie.getPartyName());
									if(verifyMobile!=null && verifyMobile>0){
										userInfo.setVerifyMobile(String.valueOf(verifyMobile));
									}
									userInfo.setUserId(userCookie.getUserId());
									userInfo.setUserName(userCookie.getRegisValue());
									userInfo.setCatalogInfoId(userCookie.getCatalogInfoId());
									if(null != userCookie.getIdCard() && !"".equals(userCookie.getIdCard())){
										userInfo.setIdentityCard(userCookie.getIdCard());
									}else {
										logger.info("获取用户身份证号码失败，水印功能失效，请检查！");
									}
									if(userCookie.getLocationList()!=null) {
										userInfo.setInfoOrgList(userCookie.getLocationList());
									}
									//-- 获取用户归属地理地经纬度信息
									try {
										IOrgService orgService = (IOrgService) ctx.getBean("orgService");
										Long infoOrgId = (userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0)?(userInfo.getInfoOrgList().get(0).getOrgId()):-1L;
										OrgExtraInfo orgExtraInfo = orgService.getOrgExtraInfo(infoOrgId); //获取信息域的中心点位
										userInfo.setOrgLatitude(orgExtraInfo.getLatitude());
										userInfo.setOrgLongitude(orgExtraInfo.getLongitude());
									} catch (Exception e) {
										logger.error("找不到userId："+userInfo.getUserId()+" 对应的组织地理信息！");
									}
									session.setAttribute(ConstantValue.USER_IN_SESSION, userInfo);
									session.removeAttribute(ConstantValue.SYSTEM_PRIVILEGE_ACTION);
									session.removeAttribute(ConstantValue.SYSTEM_PRIVILEGE_MAP);
								} else {
									logger.info("userId："+userInfo.getUserId()+" 信息没变化无需更新！");
								}
								CommonFunctions.initAppDomains(req, userInfo, cookieMapInfo, token);
								// Add By @YangCQ : 添加SQ_ZZGRID_URL、ANOLE_COMPONENT_URL信息到请求里
								req.setAttribute("ANOLE_COMPONENT_URL", App.SYSTEM.getDomain(session));
								req.setAttribute("SQ_UAM_URL", App.UAM.getDomain(session));
								req.setAttribute("SQ_ZZGRID_URL", App.ZZGRID.getDomain(session));
								req.setAttribute("IMG_URL", App.IMG.getDomain(session));
								req.setAttribute("uiDomain", App.UI.getDomain(session));
								req.setAttribute("SQ_BI_URL", App.BI.getDomain(session));
								req.setAttribute("JSESSIONID", session.getId());
								req.setAttribute("SPGIS_IP", ConstantValue.SPGIS_IP);
								req.setAttribute("SQ_FILE_URL", App.SQFILE.getDomain(session));
								req.setAttribute("SQ_ZONE_URL", App.ZONE.getDomain(session));
								req.setAttribute("SC_EVENT_URL", App.SCEVENT.getDomain(session));
								req.setAttribute("SQ_EVENT_URL", App.EVENT.getDomain(session));
								req.setAttribute("RS_DOMAIN", App.RS.getDomain(session));
								req.setAttribute("OA_DOMAIN", App.OA.getDomain(session));
								req.setAttribute("SQ_SKY_URL",App.SKY.getDomain(session));
								req.setAttribute("COMPONENTS_URL",App.COMPONENTS.getDomain(session));
								req.setAttribute("SQ_GMIS_URL",App.GMIS.getDomain(session));
								req.setAttribute("GEO_URL", App.GEO.getDomain(session));//标准地址库应用
								req.setAttribute("GIS_DOMAIN", App.GIS.getDomain(session));
	                            req.setAttribute("BDIO_URL", App.BDIO.getDomain(session));
								req.setAttribute("UI_STYLE", uiStyle);
								// Add By @YangCQ : 添加用户按钮权限
								this.initPrivilege(req, ctx, userCookie);
							}
						} else {
							logger.info("统一认证memcache中用户信息有误，系统判定非法进入，清空session");
							userInfo=null;
							session.invalidate();
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("统一认证memcache读取有误，系统判定非法进入，清空session");
						userInfo=null;
						session.invalidate();
					}
				}
			} else {
				logger.info("cookie中未读取到统一认证的相关信息");
			}
			logger.info("==================统一认证 认证结束===================================\n");

			if(userInfo!=null) {
				logger.info(userInfo.toString());
				filterChain.doFilter(req, res);
			} else {//第三方事件对接
				String userName = req.getParameter("userName");
				String passWord = req.getParameter("passWord");
				boolean validateFlag = true;

				if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(passWord)){
					UserManageOutService userManageService = (UserManageOutService) ctx.getBean("userManageService");
					UserBO user = userManageService.getUserInfoByRegistValue(userName);

					if(user!=null && user.getUserId()!=null && passWord.equals(user.getPassword())){
						if(ConstantValue.PASSWORD_TYPE_CYCLE.equals(user.getPasswordType())) {//定期修改密码
							PartyUserCertifyBO bo = userManageService.findPartyUserCertifyByUserId(user.getUserId());
							Long now = new Date().getTime();
							Long old = bo.getStatusTime().getTime();
							Long days = (now - old) / (1000 * 60 * 60 * 24);
							if (days > user.getPasswordCycle() * 30) {
								logger.info("您的密码已经过期,请修改您的密码。");
								validateFlag = false;
							}
						}else if(ConstantValue.PASSWORD_TYPE_SHORT.equals(user.getPasswordType())) { //短密码
							Date passwordDate = user.getExpireDate();
							int result = passwordDate.compareTo(new Date());
							if(result < 0) {
								logger.info("对不起您的密码已经失效。");
								validateFlag = false;
							}
						}
					}
				}else{
					validateFlag = false;
				}

				if(validateFlag){
					filterChain.doFilter(req, res);
				}else{
					res.sendRedirect(cacheService.getUamLoginUrl() + "/redirectLogin?redirectUrl=" + getAllUrl(req));
				}

				return;
			}
		}
	}

	private String getAllUrl(HttpServletRequest request) {
		String requestUrl = request.getRequestURL().toString();
		Map<String, String[]> params = request.getParameterMap();
		String queryString = "";
		for (Object key : params.keySet()) {
			String[] values = params.get(key);
			queryString += key + "=" + values[0] + "&";
		}
		if (queryString.length() > 0) queryString = "?" + queryString.substring(0, queryString.length() - 1);
		return requestUrl + queryString;
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
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Deprecated
	private void initPrivilegeDeprecated(HttpServletRequest req, WebApplicationContext ctx, UserCookie userCookie) {
		HttpSession session = req.getSession();
		String privilegeId = req.getParameter(ConstantValue.SYSTEM_PRIVILEGE_ACTION);
		if (StringUtils.isBlank(privilegeId)) {
			privilegeId = "-1";
		}
		req.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_ACTION, privilegeId);

		Map<String, String> json = null;

		try {
			json = (Map<String, String>) session.getAttribute(ConstantValue.SYSTEM_PRIVILEGE_MAP);
		} catch(ClassCastException e) {
			json = null;
		}

		if (json == null) {
			json = new HashMap<String, String>();
		}

		if (!"-1".equals(privilegeId)) {
			CacheService cacheService = (CacheService) ctx.getBean("cacheService");
			UserManageOutService userManageService = (UserManageOutService) ctx.getBean("userManageService");

			if (cacheService != null && userManageService != null) {
				Map<String, PriActionBO> tempMap = cacheService.getActionMap();
				logger.info("cacheService.getActionMap ==> "+DateUtils.getNow() +":" + tempMap);
				String menuAction = null;
				if (StringUtils.isNotBlank(privilegeId) && !"null".equals(privilegeId)) {
					menuAction = userManageService.selectUserPrivilegeAction(
							userCookie.getUserId(),
							userCookie.getOrgId(),
							null,
							Long.valueOf(privilegeId));
					logger.info("userManageService.selectUserPrivilegeAction ==> "+DateUtils.getNow() +":" + menuAction);
					// 清理
					List<String> temp = new ArrayList<String>();
					Set<Map.Entry<String, String>> set = json.entrySet();
					for (Map.Entry<String, String> entry : set) {
						if (privilegeId.equals(entry.getValue())) {
							temp.add(entry.getKey());
						}
					}
					for (String key : temp) {
						json.remove(key);
					}
				}
				if (StringUtils.isNotBlank(menuAction) && !"null".equals(menuAction)) {
					String[] actionStr = menuAction.split(",");
					for (String s : actionStr) {
						if (StringUtils.isNotBlank(s) && !"null".equals(s)) {
							PriActionBO tempBO = tempMap.get(s);
							if (tempBO != null) {
								json.put(tempBO.getCode(), privilegeId);
							} else {
								logger.info("actionKey:" + s + "==>actionMap:" + tempMap);
							}
						}
					}
				}
			} else {
				logger.error("获取权限服务接口有误：cacheService=" + cacheService + " userManageService=" + userManageService +
						"为了不影响按钮功能使用，默认写入所有权限。");
				json.put("all", privilegeId);
			}
		}
		session.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_MAP, json);
	}

	private void initPrivilege(HttpServletRequest req, WebApplicationContext ctx, UserCookie userCookie) {
		HttpSession session = req.getSession();
		String privilegeAction = req.getParameter(ConstantValue.SYSTEM_PRIVILEGE_ACTION);
		String privilegeCode = req.getParameter(ConstantValue.SYSTEM_PRIVILEGE_CODE);
		String userType = userCookie.getUserType();

		if(StringUtils.isBlank(userType)){
			userType = ConstantValue.USER_TYPE_REGIST;
		}

		//清空历史权限，防止影响后续未配置权限的菜单
		session.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_ACTION, null);
		session.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_CODE, null);

		if(StringUtils.isNotBlank(privilegeCode)) {
			session.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_CODE, privilegeCode);
		} else if(StringUtils.isNotBlank(privilegeAction)) {
			session.setAttribute(ConstantValue.SYSTEM_PRIVILEGE_ACTION, privilegeAction);
		}

		session.setAttribute(ConstantValue.SYSTEM_ROLE, userCookie.getRoleList());//存放角色信息

		//判断是否是管理员
		if(ConstantValue.USER_TYPE_SUPER.equals(userType)||ConstantValue.USER_TYPE_DEVELOPER.equals(userType)|| ConstantValue.USER_TYPE_ADMIN.equals(userType)){
			session.setAttribute(ConstantValue.SYSTEM_ADMIN,"1");
		}


	}

	private void checkLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String __rtc = req.getParameter("__rtc");
		if ("1".equals(__rtc)) {
			String redirectUrl = req.getParameter("redirectUrl");
			String tokenflag = req.getParameter("UAM_TOKEN_FLAG");
			String resourcekey = req.getParameter("resourcekey");
			String resource = req.getParameter("resource");
			String targetDomain = CommonFunctions.getTopDomain(redirectUrl);
			// 0表示已经登陆
			Cookie cookie = CommonFunctions.getCookieByName(req, "UAM_TOKEN_FLAG");
			if (cookie == null) {
				Cookie cookieFlag = new Cookie("UAM_TOKEN_FLAG", tokenflag);
				cookieFlag.setMaxAge(60 * 60 * 10);
				cookieFlag.setPath("/");
				cookieFlag.setDomain(targetDomain);
				res.addCookie(cookieFlag);
			} else {
				cookie.setValue("0");
				cookie.setMaxAge(60 * 60 * 10);
				cookie.setPath("/");
				cookie.setDomain(targetDomain);
				res.addCookie(cookie);
			}
			// 解密的密钥
			cookie = CommonFunctions.getCookieByName(req, "resourcekey");
			if (cookie == null) {
				Cookie cookiekey = new Cookie("resourcekey", resourcekey);
				cookiekey.setMaxAge(60 * 60 * 10);
				cookiekey.setPath("/");
				cookiekey.setDomain(targetDomain);
				res.addCookie(cookiekey);
			} else {
				cookie.setValue(resourcekey);
				cookie.setMaxAge(60 * 60 * 10);
				cookie.setPath("/");
				cookie.setDomain(targetDomain);
				res.addCookie(cookie);
			}
			// 用密钥将令牌加密
			cookie = CommonFunctions.getCookieByName(req, "resource");
			if (cookie == null) {
				cookie = new Cookie("resource", resource);
				cookie.setMaxAge(60 * 60 * 10);
				cookie.setPath("/");
				cookie.setDomain(targetDomain);
				res.addCookie(cookie);
			} else {
				cookie.setValue(resource);
				cookie.setMaxAge(60 * 60 * 10);
				cookie.setPath("/");
				cookie.setDomain(targetDomain);
				res.addCookie(cookie);
			}
			PrintWriter writer = res.getWriter();
			writer.write("<script>location.href = '" + redirectUrl + "';</script>");
			writer.close();
			return;
		}
	}
}