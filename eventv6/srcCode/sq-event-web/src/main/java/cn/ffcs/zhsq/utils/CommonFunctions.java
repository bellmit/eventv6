package cn.ffcs.zhsq.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;

import cn.ffcs.cookie.service.CacheService;
import cn.ffcs.file.service.FileUrlProvideService;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.uam.bo.OrgEntityInfoBO;
import cn.ffcs.uam.bo.OrgSocialInfoBO;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService;
import cn.ffcs.zhsq.mybatis.domain.map.arcgis.GisStatConfig;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GdZTreeNode;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.domain.App;
import cn.ffcs.zhsq.utils.domain.IDomain;

/**
 * Created by IntelliJ IDEA. User: guohh Date: 11-9-7 Time: 上午9:44 To change this template use File | Settings | File
 * Templates.
 */
public class CommonFunctions {

	private static final Logger logger = LoggerFactory.getLogger(CommonFunctions.class);

	/**
	 * 根据浏览器UA生成下载文件名称（解决乱码问题）
	 * 
	 * @param fileName
	 * @param userAgent
	 * @return
	 */
	public static String buildFileNameByUa(String fileName, String userAgent) {
		try {
			String new_filename = URLEncoder.encode(fileName, "UTF8");
			// 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
			String rtn = "filename=\"" + new_filename + "\"";
			if (userAgent != null && !"".equals(userAgent)) {
				userAgent = userAgent.toLowerCase();
				// IE浏览器，只能采用URLEncoder编码
				if (userAgent.indexOf("msie") != -1) {
					rtn = "filename=\"" + new_filename + "\"";
				}
				// Opera浏览器只能采用filename*
				else if (userAgent.indexOf("opera") != -1) {
					rtn = "filename*=UTF-8''" + new_filename;
				}
				// Safari浏览器，只能采用ISO编码的中文输�?
				else if (userAgent.indexOf("safari") != -1) {
					rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
				}
				// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输�?
				else if (userAgent.indexOf("applewebkit") != -1) {
					// new_filename = MimeUtility.encodeText(fileName, "UTF8", "B");
					// rtn = "filename=\"" + new_filename + "\"";
					rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
				}
				// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输�?
				else if (userAgent.indexOf("mozilla") != -1) {
					rtn = "filename*=UTF-8''" + new_filename;
				}
			}
			return rtn;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 根据文件名后缀获取文件类型
	 * 
	 * @param subfix
	 * @return
	 */
	public static String getContentTypeBySubfix(String subfix) {
		if (subfix != null && subfix.length() > 0) {
			String tmpSubfix = subfix.toLowerCase();
			if (tmpSubfix.equals("doc") || tmpSubfix.equals("docx"))
				return "application/msword";
			else if (tmpSubfix.equals("dms") || tmpSubfix.equals("lha") || tmpSubfix.equals("lzh")
					|| tmpSubfix.equals("exe") || tmpSubfix.equals("class"))
				return "application/octet-stream bin";
			else if (tmpSubfix.equals("pdf"))
				return "application/pdf";
			else if (tmpSubfix.equals("ppt") || tmpSubfix.equals("pptx"))
				return "appication/powerpoint";
			else if (tmpSubfix.equals("zip"))
				return "application/zip";
			else if (tmpSubfix.equals("mpeg") || tmpSubfix.equals("mp2"))
				return "audio/mpeg";
			else if (tmpSubfix.equals("xls") || tmpSubfix.equals("xlsx"))
				return "application/msexcel";
		}
		return "";
	}

	/**
	 * 根据类型获取当年的季度列表
	 * 
	 * @param except
	 *            已有的季度列表
	 * @param type
	 *            1 排除 2 本身
	 * @return
	 */
	public static List<Map<String, String>> getQuarterListByType(List<String> except, int type) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String[] quarters = new String[] { (year + "01"), (year + "02"), (year + "03"), (year + "04") };
		switch (type) {
		case 1: // 排除
			for (int i = 0; i < quarters.length; i++) {
				Map<String, String> quarter = new HashMap<String, String>();
				boolean canAdd = true;
				if (except != null && except.size() > 0) {
					for (int j = 0; j < except.size(); j++) {
						if (quarters[i].equals(except.get(j)))
							canAdd = false;
					}
				}
				if (canAdd) {
					quarter.put("quarter_value", quarters[i]);
					quarter.put("quarter_text", (year + "年第" + (i + 1) + "季度"));
					result.add(quarter);
				}
			}
			break;
		case 2: // 包含
			if (except != null && except.size() > 0) {
				for (int j = 0; j < except.size(); j++) {
					Map<String, String> quarter = new HashMap<String, String>();
					boolean extra = true;
					for (int i = 0; i < quarters.length; i++) {
						if (quarters[i].equals(except.get(j))) {
							quarter.put("quarter_value", quarters[i]);
							quarter.put("quarter_text", (year + "年第" + (i + 1) + "季度"));
							result.add(quarter);
							extra = false;
						}
					}
					if (extra) {
						String quarterValue = except.get(j);
						quarter.put("quarter_value", quarterValue);
						quarter.put("quarter_text",
								(quarterValue.substring(0, 4) + "年第" + quarterValue.substring(5) + "季度"));
						result.add(quarter);
					}
				}
			}
			break;
		}
		return result;
	}

	/**
	 * 获取季度名称
	 * 
	 * @param quarter
	 * @return
	 */
	public static String getQuarterText(String quarter) {
		if (quarter == null || quarter.length() != 6)
			return "";
		String year = quarter.substring(0, 4);
		String count = quarter.substring(5);
		return (year + "年第" + count + "季度");
	}

	/**
	 * 获取访问者ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteIp(HttpServletRequest request) {
		// -- 获取ip地址
		String ipKey = request.getHeader("X-Real-IP");
		if (ipKey == null || ipKey.length() == 0 || "unknown".equalsIgnoreCase(ipKey))
			ipKey = request.getHeader("X-Forwarded-For");
		if (ipKey == null || ipKey.length() == 0 || "unknown".equalsIgnoreCase(ipKey))
			ipKey = request.getHeader("Proxy-Client-IP");
		else {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ipKey.indexOf(',');
			if (index != -1) {
				ipKey = ipKey.substring(0, index);
			}
		}
		if (ipKey == null || ipKey.length() == 0 || "unknown".equalsIgnoreCase(ipKey))
			ipKey = request.getHeader("WL-Proxy-Client-IP");
		if (ipKey == null || ipKey.length() == 0 || "unknown".equalsIgnoreCase(ipKey))
			ipKey = request.getRemoteAddr();
		return ipKey;
	}

	/**
	 * 逗号隔开的数值列表转成List
	 * 
	 * @param idListStr
	 * @return
	 */
	public static List<Long> idStrToList(String idListStr) {
		List<Long> idList = new ArrayList<Long>();
		if (!StringUtils.isBlank(idListStr)) {
			String[] idArray = idListStr.split(",");
			for (String idStr : idArray) {
				try {
					idList.add(Long.parseLong(idStr));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return idList;
	}

	/**
	 * 数值List转成逗号隔开的数值字符串
	 * 
	 * @param idList
	 * @return
	 */
	public static String idListToStr(List<Long> idList) {
		String idStr = "";
		if (idList != null && idList.size() > 0) {
			for (Long id : idList) {
				idStr += ("," + id);
			}
			idStr = idStr.substring(1);
		}
		return idStr;
	}

	/**
	 * 计算日期差
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long daysBetween(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1Tmp = sdf.parse(sdf.format(date1));
			Date date2Tmp = sdf.parse(sdf.format(date2));
			long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
			long date1Time = date1Tmp.getTime();
			long date2Time = date2Tmp.getTime();
			long diff = date1Time - date2Time;
			long days = diff / nd;// 计算差多少天
			if (diff % nd > 0)
				days++;
			return days;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * 过滤字符串中的html标签
	 * 
	 * @param source
	 * @return
	 */
	public static String filterHtml(String source) {
		if (StringUtils.isBlank(source))
			return source;
		if (source.indexOf("<") == -1)
			return source;
		int start = source.indexOf("<");
		int end = source.indexOf(">");
		String result;
		if (end < start) {
			result = source.replaceFirst(">", "");
		} else {
			result = source.substring(0, start) + source.substring(end + 1, source.length());
		}
		return filterHtml(result);
	}

	/**
	 * 将Json格式的字符串转换成Map<String,Object>对象返回
	 * 
	 * @param jsonString
	 *            需要进行转换的Json格式字符串
	 * @return 转换后的Map<String,Object>对象
	 */
	public static Map<String, Object> json2Map(String jsonString) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Set<String> set = jsonObject.keySet();
		Iterator<String> keyIter = jsonObject.keySet().iterator();
		String key;
		Object value;
		Map<String, Object> valueMap = new HashMap<String, Object>();
		set.iterator();
		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 判断map中key对应值是否为空
	 * 
	 * @param map
	 * @param key
	 * @return true为空；false为非空
	 */
	public static boolean isBlank(Map<String, ?> map, String key) {
		boolean flag = true;
		if (map != null && StringUtils.isNotBlank(key)) {
			Object keyObj = map.get(key);
			if (keyObj != null) {
				String keyStr = keyObj.toString();
				flag = StringUtils.isBlank(keyStr);
			}
		}

		return flag;
	}

	/**
	 * 判断map中key对应值是否非空
	 * 
	 * @param map
	 * @param key
	 * @return true为非空；false为空
	 */
	public static boolean isNotBlank(Map<String, ?> map, String key) {
		return !isBlank(map, key);
	}

	/**
	 * 对数组进行从小到大升序排列
	 * 
	 * @param intAttr
	 * @return
	 */
	public static Integer[] orderInturn(Integer[] intAttr) {
		if (intAttr != null && intAttr.length > 0) {
			for (int index = 0, j = 0, len = intAttr.length; index < len; index++) {// 从小到大，进行排序
				int temp = intAttr[index];
				j = index;

				while (++j < len) {
					int tempj = intAttr[j];
					if (temp > tempj) {
						intAttr[index] = tempj;
						intAttr[j] = temp;
						temp = tempj;
					}
				}
			}
		}

		return intAttr;
	}

	/**
	 * 对数组进行从小到大升序排列
	 * 
	 * @param strAttr
	 * @return
	 */
	public static String[] orderInturn(String[] strAttr) {
		if (strAttr != null && strAttr.length > 0) {
			List<Integer> itemList = new ArrayList<Integer>();
			for (int index = 0, len = strAttr.length; index < len; index++) {
				String itemStr = strAttr[index];
				if (StringUtils.isNotBlank(itemStr)) {
					try {
						itemList.add(Integer.valueOf(itemStr));
					} catch (Exception e) {
					}
				}
			}

			Integer[] itemAttr = itemList.toArray(new Integer[itemList.size()]);

			itemAttr = orderInturn(itemAttr);

			strAttr = new String[itemAttr.length];
			for (int index = 0, len = itemAttr.length; index < len; index++) {
				strAttr[index] = String.valueOf(itemAttr[index]);
			}
		}

		return strAttr;
	}

	/**
	 * 获取包含中文的字符串的长度
	 * 
	 * @param str
	 * @return
	 */
	public static int countChineseLength(String str) {
		int strLength = 0;

		if (StringUtils.isNotBlank(str)) {
			String strTmp = str.replaceAll("[^\\x00-\\xff]", "**");
			strLength = strTmp.length();
		}

		return strLength;
	}

	/**
	 * 判断给定的字符串是否在指定的长度之内
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isLengthValidate(String str, int length) {
		int strLength = countChineseLength(str);

		return strLength <= length;
	}

	/**
	 * 判断map中key对应的值的长度是否在指定长度之内
	 * 
	 * @param map
	 * @param key
	 * @param length
	 * @return
	 */
	public static boolean isLengthValidate(Map<String, Object> map, String key, int length) {
		boolean isValidate = true;

		if (isNotBlank(map, key)) {
			String str = map.get(key).toString();
			isValidate = isLengthValidate(str, length);

			if (!isValidate) {
				map.put("msg", "参数[" + key + "]的输入长度大于" + length + "，请检查！");
			}
		}

		return isValidate;
	}

	public static GdZTreeNode transBaseGridInfoToGdZTreeNode(OrgEntityInfoBO orgEntityInfoBO) {
		GdZTreeNode node = new GdZTreeNode();
		node.setOpen(false);
		node.setIsParent(!orgEntityInfoBO.isLeaf());
		node.setId(String.valueOf(orgEntityInfoBO.getOrgId()));
		node.setPId(String.valueOf(orgEntityInfoBO.getParentOrgId()));
		node.setName(orgEntityInfoBO.getOrgName());
		node.setOrgCode(orgEntityInfoBO.getOrgCode());
		node.setOrgId(orgEntityInfoBO.getOrgId());
		node.setLevel(orgEntityInfoBO.getChiefLevel());
		return node;
	}

	public static GdZTreeNode transSocialOrgToZTreeNode(OrgSocialInfoBO socialOrg) {
		return transSocialOrgToZTreeNodeA(socialOrg, null);
	}

	public static GdZTreeNode transSocialOrgToZTreeNodeA(OrgSocialInfoBO socialOrg, Boolean isLeaf) {
		GdZTreeNode node = new GdZTreeNode();
		node.setOpen(false);
		node.setIsParent(isLeaf == null ? !socialOrg.isLeaf() : !socialOrg.isLeaf() && !isLeaf);
		node.setId(String.valueOf(socialOrg.getOrgId()));
		node.setPId(String.valueOf(socialOrg.getParentOrgId()));
		node.setName(socialOrg.getOrgName());
		if (socialOrg.getIcon() != null && socialOrg.getIcon().length() > 0)
			node.setIcon(socialOrg.getIcon());
		node.setLayerType(socialOrg.getOrgType());
		node.setOrgCode(socialOrg.getOrgCode());
		node.setLevel(socialOrg.getChiefLevel());
		node.setGridLevel(socialOrg.getChiefLevel());
		node.setProfessionCode(socialOrg.getProfessionCode());
		return node;
	}
	
	public static boolean isIP(String ip) {
		if (StringUtils.isNotBlank(ip)) {
			String pattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
					+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
			return ip.trim().matches(pattern);
		}
		return false;
	}

	public static String getTopDomain(String path) {
		String pathInfo = path;
		pathInfo = pathInfo.replace("http://", "");
		int endIndex = pathInfo.indexOf("/");
		if (endIndex > 0) {
			pathInfo = pathInfo.substring(0, endIndex);
		}
		endIndex = pathInfo.indexOf(":");
		if (endIndex > 0) {
			pathInfo = pathInfo.substring(0, endIndex);
		}
		if (!isIP(pathInfo)) {
			int index = pathInfo.indexOf(".");
			pathInfo = pathInfo.substring(index + 1, pathInfo.length());
		}
		return pathInfo;
	}

	public static String resetDomain(String xxDomain, String topDomain) {
		if (StringUtils.isNotBlank(xxDomain)) {
			return xxDomain.replaceFirst(CommonFunctions.getTopDomain(xxDomain), topDomain);
		} else {
			return xxDomain;
		}
	}

	public static void initAppDomains(HttpServletRequest request, UserInfo userInfo, Map<String, Object> cookieMapInfo,
			String token) {
		HttpSession session = request.getSession();
		ServletContext servletContext = session.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		FileUrlProvideService fileUrlProvideService = ctx.getBean(FileUrlProvideService.class);
		IFunConfigurationService funConfigurationService = ctx.getBean(IFunConfigurationService.class);

		String topDomain = cookieMapInfo == null ? CommonFunctions.getTopDomain(request.getRequestURL().toString())
				: null;
		boolean isUpdate = false;

		if (session.getAttribute(ConstantValue.SESSION_TOKEN_KEY) == null) {
			isUpdate = true;
			session.setAttribute(ConstantValue.SESSION_TOKEN_KEY, token);// 日志接口需要保存TOKEN,这里做下cookie的token保存
		} else {
			String oldToken = (String) session.getAttribute(ConstantValue.SESSION_TOKEN_KEY);
			if (!oldToken.equals(token)) {
				isUpdate = true;
				session.setAttribute(ConstantValue.SESSION_TOKEN_KEY, token);
			}
		}

		// 登陆域名
		if (cookieMapInfo != null) {
			topDomain = (String) cookieMapInfo.get(ConstantValue.USER_COOKIE_UAM_DOMAIN_KEY);// 获取域名
			if (session.getAttribute(App.TOP.getCode()) == null) {
				isUpdate = true;
				session.setAttribute(App.TOP.getCode(), topDomain);// 从登录信息当中获取域名
			} else {
				String tempDomain = (String) session.getAttribute(App.TOP.getCode());
				if (!tempDomain.equals(topDomain)) {
					isUpdate = true;
					session.setAttribute(App.TOP.getCode(), topDomain);// 从登录信息当中获取域名
				}
			}
			String uamLoginUrl = (String) cookieMapInfo.get(ConstantValue.REDIRECT_DOMAIN_KEY);
			String targetDomain = CommonFunctions.getTopDomain(uamLoginUrl);
			if (StringUtils.isNotBlank(uamLoginUrl) && CommonFunctions.isIP(targetDomain)) {
				ConstantValue.UAM_LOGIN_URL = uamLoginUrl;
			} else {
				ConstantValue.UAM_LOGIN_URL = uamLoginUrl.replace(targetDomain, topDomain);
			}
		}
		/*if (isUpdate || session.getAttribute(App.IMG.getCode()) == null) {
			String imgDoamin = fileUrlProvideService.getFileDomain();
			imgDoamin = imgDoamin + topDomain;
			session.setAttribute(App.IMG.getCode(), imgDoamin);
			session.setAttribute(App.IMG_ZZGRID.getCode(), imgDoamin + "/" + ConstantValue.RESOURSE_DOMAIN_KEY + "/");
		}*/
        JSONObject jsonObject = new JSONObject();
        for (App app : App.values()) {
            if (app.getType() == IDomain.REMOTE) {
                if (isUpdate || session.getAttribute(app.getCode()) == null) {
                    isUpdate = true;
                    // 如果功能配置的是IP，那就不进行域名替换
                    String appDomain = funConfigurationService.getAppDomain( app.getCode(), userInfo.getOrgCode(), topDomain);
                    if (StringUtils.isNotBlank(appDomain)) {
                        session.setAttribute(app.getCode(), appDomain.trim());
                        jsonObject.put(app.getCode().substring(1),appDomain);
                    } else {
                        logger.error("请在系统管理->功能配置->域名配置->配置【" + app.getCode() + "】的域名！");
                    }
                    //CommonFunctions.setAppDomain(session, funConfigurationService, topDomain, app.getCode(), userInfo.getOrgCode());
                }
            }
        }
        if (isUpdate || session.getAttribute("ffcsDomain") == null) {
            session.setAttribute("ffcsDomain", JSONObject.toJSONString(jsonObject));
        }
	}

	private static void setAppDomain(HttpSession session, IFunConfigurationService funConfigurationService,
			String topDomain, String appCode, String orgCode) {
		String appDomain = funConfigurationService.getAppDomain(appCode, orgCode, topDomain);
		if (StringUtils.isNotBlank(appDomain)) {
			session.setAttribute(appCode, appDomain.trim());
		} else {
			logger.info("请在系统管理->功能配置->域名配置->配置【" + appCode + "】的域名！");
		}
	}

	public static boolean isShowSatisfyRate(String orgCode) {
		IFunConfigurationService funConfigurationService = SpringContextUtil.getApplicationContext().getBean(
				IFunConfigurationService.class);
		String display = funConfigurationService.turnCodeToValue("SHOW_SATISF_RATE", "",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		return "1".equals(display);
	}

	public static String getDomain(HttpSession session, String code) {
		String domain = "";
		if (StringUtils.isNotBlank(code)) {
			App[] apps = App.values();
			for (App app : apps) {
				if (code.trim().equals(app.getCode())) {
					domain = app.getDomain(session);
					break;
				}
			}
		}
		if (StringUtils.isBlank(domain))
			new IllegalArgumentException("未找到【" + code + "】对应App域名");
		return domain;
	}

	public static List<Map<String, Object>> rsToList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData rms = null;
		Map<String, Object> map = null;
		if (rs != null) {
			rms = rs.getMetaData();
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (int i = 1; i <= rms.getColumnCount(); i++) {
					String field = rms.getColumnName(i).toUpperCase();
					map.put(field, rs.getObject(field));
				}
				list.add(map);
			}
			rs.close();
		}
		return list;
	}

	public static List<String> fetchGridLevelNames(String orgCode) {
		IFunConfigurationService funConfigurationService = SpringContextUtil.getApplicationContext().getBean(
				IFunConfigurationService.class);
		String DIVISIONS_LEVEL_NAME_CFG = funConfigurationService.turnCodeToValue("DIVISIONS_LEVEL_NAME_CFG", "",
				IFunConfigurationService.CFG_TYPE_FACT_VAL, orgCode, IFunConfigurationService.CFG_ORG_TYPE_0);
		if (StringUtils.isNotBlank(DIVISIONS_LEVEL_NAME_CFG)) {
			return Arrays.asList(DIVISIONS_LEVEL_NAME_CFG.trim().split(","));
		}
		return null;
	}

	public static String transGridLevelName(List<String> gridLevelNames, int gridLevel) {
		if (gridLevelNames != null && gridLevel >= 2) {
			return gridLevelNames.get(gridLevel - 2);
		}
		return "";
	}

	public static void transGisDataCfgUrl(GisDataCfg gisDataCfg, HttpSession session) {
		if (gisDataCfg != null) {
			ServletContext servletContext = session.getServletContext();
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			IFunConfigurationService funConfigurationService = ctx.getBean(IFunConfigurationService.class);
			
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			String menuListUrl = gisDataCfg.getMenuListUrl();
			String menuSummaryUrl = gisDataCfg.getMenuSummaryUrl();
			String menuDetailUrl = gisDataCfg.getMenuDetailUrl();
			String elementsCollectionStr = gisDataCfg.getElementsCollectionStr();
			String callBack = gisDataCfg.getCallBack();
			if (menuListUrl != null && !"".equals(menuListUrl)) {
				String menuListUrlDomain = menuListUrl.split("/")[0];
				String actMenuListUrlDomain = CommonFunctions.getDomain(session, menuListUrlDomain);

				if (StringUtils.isBlank(actMenuListUrlDomain)) {
					actMenuListUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN", menuListUrlDomain,
							IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}

				menuListUrlDomain = "\\" + menuListUrlDomain;
				menuListUrl = menuListUrl.replaceFirst(menuListUrlDomain, actMenuListUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
				callBack = callBack.replaceAll(menuListUrlDomain, actMenuListUrlDomain);
			}
			if (menuSummaryUrl != null && !"".equals(menuSummaryUrl)) {
				String menuSummaryUrlDomain = menuSummaryUrl.split("/")[0];
				String actMenuSummaryUrlDomain = CommonFunctions.getDomain(session, menuSummaryUrlDomain);

				if (StringUtils.isBlank(actMenuSummaryUrlDomain)) {
					actMenuSummaryUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
							menuSummaryUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}

				menuSummaryUrlDomain = "\\" + menuSummaryUrlDomain;
				menuSummaryUrl = menuSummaryUrl.replaceFirst(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuSummaryUrlDomain,
						actMenuSummaryUrlDomain);
				callBack = callBack.replaceAll(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
			}
			if (menuDetailUrl != null && !"".equals(menuDetailUrl)) {
				String menuDetailUrlDomain = menuDetailUrl.split("/")[0];
				String actMenuDetailUrlDomain = CommonFunctions.getDomain(session, menuDetailUrlDomain);

				if (StringUtils.isBlank(actMenuDetailUrlDomain)) {
					actMenuDetailUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN",
							menuDetailUrlDomain, IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}

				menuDetailUrlDomain = "\\" + menuDetailUrlDomain;
				menuDetailUrl = menuDetailUrl.replaceFirst(menuDetailUrlDomain, actMenuDetailUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuDetailUrlDomain,
						actMenuDetailUrlDomain);
				callBack = callBack.replaceAll(menuDetailUrlDomain, actMenuDetailUrlDomain);
			}
			gisDataCfg.setMenuListUrl(menuListUrl);
			gisDataCfg.setMenuSummaryUrl(menuSummaryUrl);
			gisDataCfg.setMenuDetailUrl(menuDetailUrl);
			gisDataCfg.setElementsCollectionStr(elementsCollectionStr);
			gisDataCfg.setCallBack(callBack);
		}

	}
	
	public static int getGridLevel(String infoOrgCode) {
		if (StringUtils.isNotBlank(infoOrgCode)) {
			switch(infoOrgCode.length()) {
			case 2:
				return 1;
			case 4:
				return 2;
			case 6:
				return 3;
			case 9:
				return 4;
			case 12:
				return 5;
			case 15:
				return 6;
			}
		}
		return -1;
	}
	
	public static String getInfoOrgCode(String infoOrgCode, int gridLevel) {
		if (gridLevel > 0 && StringUtils.isNotBlank(infoOrgCode) && getGridLevel(infoOrgCode) >= gridLevel) {
			switch(gridLevel) {
			case 1:
				return infoOrgCode.substring(0, 2);
			case 2:
				return infoOrgCode.substring(0, 4);
			case 3:
				return infoOrgCode.substring(0, 6);
			case 4:
				return infoOrgCode.substring(0, 9);
			case 5:
				return infoOrgCode.substring(0, 12);
			case 6:
				return infoOrgCode.substring(0, 15);
			}
		}
		return "";
	}
	
	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * @param name
	 *            cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}
	
	/**
	 * 将路径中的首个建设范围路径移除
	 * 要求建设范围路径在原路径中为起始
	 * 如：
	 * 1、原路径为：测试省测试市测试街道测试社区，建设范围路径为：测试省测试市
	 * 替换后结果为：测试街道测试社区
	 * 2、原路径为：测试省测试市测试街道测试社区，建设范围路径为：测试市
	 * 替换后结果为：测试省测试市测试街道测试社区
	 * 3、原路径为：测试省测试市测试街道测试社区，建设范围路径为：测试
	 * 替换后结果为：省测试市测试街道测试社区
	 * 
	 * @param originalPath	需要替换建设范围的原路径
	 * @param cacheService	缓存接口，用于获取建设范围路径
	 * @return
	 */
	public static String replaceScopePath(String originalPath, CacheService cacheService) {
		if(StringUtils.isNotBlank(originalPath)) {
			//功能编码为：BUILD_SCOPE_SETTING，每个平台只能配置一个，修改需要重启cookie生效
			Map<String, String> scopeSettingMap = cacheService.getBuildScopeSettingMap();
			
			if(isNotBlank(scopeSettingMap, "firstValue")) {
				String scopeSetting = scopeSettingMap.get("firstValue");
				Pattern pattern = Pattern.compile(scopeSetting);
				
				if(pattern.matcher(originalPath).lookingAt()) {//从第一个字符进行匹配,匹配成功了不再继续匹配
					originalPath = originalPath.replaceFirst(scopeSetting, "");//只替换首个
					
					if(StringUtils.isBlank(originalPath) && isNotBlank(scopeSettingMap, "secondValue")) {
						originalPath = scopeSettingMap.get("secondValue");
					}
				}
			}
		}
		
		return originalPath;
	}
	
	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
	
	public static Map<String, Object> initMap(String infoOrgCode, String homePageType, HttpSession session,
			ModelMap map) throws Exception {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		Map<String, Object> initMapParams = (Map<String, Object>) session.getAttribute("initMapParams");
		if (initMapParams == null) {
			initMapParams = new HashMap<String, Object>();
			IFunConfigurationService funConfigurationService = SpringContextUtil.getApplicationContext()
					.getBean(cn.ffcs.uam.service.IFunConfigurationService.class);
			IGisStatConfigService gisStatConfigService = SpringContextUtil.getApplicationContext()
					.getBean(cn.ffcs.zhsq.map.arcgis.service.IGisStatConfigService.class);
			IMixedGridInfoService mixedGridInfoService = SpringContextUtil.getApplicationContext()
					.getBean(cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService.class);
			//是否启用ANYCHAT视频通话
			String isUserAnychat = funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			initMapParams.put("isUserAnychat", isUserAnychat);
			//是否启用ANYCHAT视频通话
			String isUseMediasoup= funConfigurationService.turnCodeToValue(ConstantValue.IS_USE_MEDIASOUP, "",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			initMapParams.put("isUseMediasoup", isUseMediasoup);
			//是否自动清除图层
			String AUTOMATIC_CLEAR_MAP_LAYER = funConfigurationService.turnCodeToValue("AUTOMATIC_CLEAR_MAP_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//影像图是否显示楼宇轮廓
			//String IS_IMAGE_MAP_SHOW_CONTOUR = this.funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//影像图是否显示楼宇轮廓
			String IS_IMAGE_MAP_SHOW_CONTOUR = funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "IMAGE", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//影像图是否显示楼宇轮廓
			String IS_VECTOR_MAP_SHOW_CONTOUR = funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CONTOUR", "VECTOR", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//网格轮廓是否显示中心点图标
			String IS_GRID_ARCGIS_SHOW_CENTER_POINT = funConfigurationService.turnCodeToValue("ARCGIS_SHOW_CENTER_POINT", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//广东乐从镇信息域code
			String LC_INFO_ORG_CODE = funConfigurationService.turnCodeToValue("LC_INFO_ORG_CODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//网格轮廓字体设置
			String OUTLINE_FONT_SETTINGS = funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//楼宇轮廓字体设置
			String OUTLINE_FONT_SETTINGS_BUILD = funConfigurationService.turnCodeToValue("OUTLINE_FONT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//
			String IS_ACCUMULATION_LAYER = funConfigurationService.turnCodeToValue("IS_ACCUMULATION_LAYER", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String ARCGIS_DOCK_MODE = funConfigurationService.turnCodeToValue("ARCGIS_DOCK_MODE", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String arcgisFactorUrl = funConfigurationService.turnCodeToValue("ARCGIS_FACTOR_TAG", "SAFTY_TEAM_FACTOR_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			//楼宇中心点图标样式
			String CENTER_POINT_SETTINGS_BUILD = funConfigurationService.turnCodeToValue("CENTER_POINT_SETTINGS_BUILD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);

			//网格各层级显示中心点名称的地图层级设置
			String MAP_LEVEL_TRIG_CONDITION_PROVINCE = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_PROVINCE, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String MAP_LEVEL_TRIG_CONDITION_CITY = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_CITY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String MAP_LEVEL_TRIG_CONDITION_COUNTY = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_COUNTY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String MAP_LEVEL_TRIG_CONDITION_STREET = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_STREET, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String MAP_LEVEL_TRIG_CONDITION_COMMUNITY = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_COMMUNITY, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			String MAP_LEVEL_TRIG_CONDITION_GRID = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, ConstantValue.MAP_LEVEL_TRIG_CONDITION_GRID, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			
			String AUTO_SHOW_GRID_LEVEL = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, "AUTO_SHOW_GRID_LEVEL", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if (StringUtils.isNotBlank(AUTO_SHOW_GRID_LEVEL)) {
				initMapParams.put("AUTO_SHOW_GRID_LEVEL", AUTO_SHOW_GRID_LEVEL);
				if ("true".equals(AUTO_SHOW_GRID_LEVEL)) {
					String MAP_LEVEL_CFG = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_WG_POINT_MAP_LEVEL_CODE, "MAP_LEVEL_CFG", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
					initMapParams.put("MAP_LEVEL_CFG", MAP_LEVEL_CFG);
				}
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_PROVINCE)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_PROVINCE", MAP_LEVEL_TRIG_CONDITION_PROVINCE);
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_CITY)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_CITY", MAP_LEVEL_TRIG_CONDITION_CITY);
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_COUNTY)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_COUNTY", MAP_LEVEL_TRIG_CONDITION_COUNTY);
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_STREET)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_STREET", MAP_LEVEL_TRIG_CONDITION_STREET);
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_COMMUNITY)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_COMMUNITY", MAP_LEVEL_TRIG_CONDITION_COMMUNITY);
			}
			if(StringUtils.isNotBlank(MAP_LEVEL_TRIG_CONDITION_GRID)){
				initMapParams.put("MAP_LEVEL_TRIG_CONDITION_GRID", MAP_LEVEL_TRIG_CONDITION_GRID);
			}


			//人脸识别调用方法获取
			String SURVEILLANCE_MSG_METHOD = funConfigurationService.turnCodeToValue("SURVEILLANCE_MSG_METHOD", "", IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(SURVEILLANCE_MSG_METHOD)){
				initMapParams.put("SURVEILLANCE_MSG_METHOD", SURVEILLANCE_MSG_METHOD);
			}


			if(AUTOMATIC_CLEAR_MAP_LAYER == null || "".equals(AUTOMATIC_CLEAR_MAP_LAYER)) {
				AUTOMATIC_CLEAR_MAP_LAYER = "1";
			}
			if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
				OUTLINE_FONT_SETTINGS = "14";
			}
			if(IS_IMAGE_MAP_SHOW_CONTOUR == null || "".equals(IS_IMAGE_MAP_SHOW_CONTOUR)) {
				IS_IMAGE_MAP_SHOW_CONTOUR = "0";
			}
			if(IS_VECTOR_MAP_SHOW_CONTOUR == null || "".equals(IS_VECTOR_MAP_SHOW_CONTOUR)) {
				IS_VECTOR_MAP_SHOW_CONTOUR = "0";
			}
			if(IS_GRID_ARCGIS_SHOW_CENTER_POINT == null || "".equals(IS_GRID_ARCGIS_SHOW_CENTER_POINT)) {
				IS_GRID_ARCGIS_SHOW_CENTER_POINT = "0";
			}
			if(LC_INFO_ORG_CODE == null || "".equals(LC_INFO_ORG_CODE)) {
				LC_INFO_ORG_CODE = "010";
			}
			if(OUTLINE_FONT_SETTINGS == null || "".equals(OUTLINE_FONT_SETTINGS)) {
				OUTLINE_FONT_SETTINGS = "14";
			}
			if(IS_ACCUMULATION_LAYER == null || "".equals(IS_ACCUMULATION_LAYER)) {
				IS_ACCUMULATION_LAYER = "0";
			}
			if(ARCGIS_DOCK_MODE == null || "".equals(ARCGIS_DOCK_MODE)) {
				ARCGIS_DOCK_MODE = "0";
			}
			//楼宇中心点图标样式--默认图标
			if(CENTER_POINT_SETTINGS_BUILD == null || "".equals(CENTER_POINT_SETTINGS_BUILD)) {
				CENTER_POINT_SETTINGS_BUILD = "build_locate_point.png";
			}
			List<String> gridLevelNames = CommonFunctions.fetchGridLevelNames(userInfo.getOrgCode());
			//获取框选配置是否启用
			GisStatConfig gisStatConfig = new GisStatConfig();
			gisStatConfig = gisStatConfigService.getGisStatConfig("0", homePageType, infoOrgCode);
			if(gisStatConfig != null){
				initMapParams.put("kuangxuanFlag","yes");
			}
			if (gridLevelNames != null) {//地图显示XX轮廓配置
				initMapParams.put("DIVISIONS_LEVEL_NAME_CFG", gridLevelNames);
			}

			initMapParams.put("AUTOMATIC_CLEAR_MAP_LAYER", AUTOMATIC_CLEAR_MAP_LAYER);
			initMapParams.put("IS_IMAGE_MAP_SHOW_CONTOUR", IS_IMAGE_MAP_SHOW_CONTOUR);
			initMapParams.put("IS_VECTOR_MAP_SHOW_CONTOUR", IS_VECTOR_MAP_SHOW_CONTOUR);
			initMapParams.put("IS_GRID_ARCGIS_SHOW_CENTER_POINT", IS_GRID_ARCGIS_SHOW_CENTER_POINT);
			initMapParams.put("LC_INFO_ORG_CODE", LC_INFO_ORG_CODE);
			initMapParams.put("OUTLINE_FONT_SETTINGS", OUTLINE_FONT_SETTINGS);
			initMapParams.put("OUTLINE_FONT_SETTINGS_BUILD", OUTLINE_FONT_SETTINGS_BUILD);
			initMapParams.put("IS_ACCUMULATION_LAYER", IS_ACCUMULATION_LAYER);
			initMapParams.put("ARCGIS_DOCK_MODE", ARCGIS_DOCK_MODE);
			initMapParams.put("CENTER_POINT_SETTINGS_BUILD", CENTER_POINT_SETTINGS_BUILD);
			initMapParams.put("arcgisFactorUrl", arcgisFactorUrl);
			
			//是否启用视频通话
			String IS_USER_MMP = funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_MMP, null,IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			initMapParams.put("isUserMmp", IS_USER_MMP);
			initMapParams.put("anyChatServerUrl", funConfigurationService.turnCodeToValue(ConstantValue.IS_USER_ANYCHAT, "SERVER_URL",IFunConfigurationService.CFG_TYPE_FACT_VAL,userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0));
			String SHOW_CURRENT_GRID_LEVEL_OUTLINE = funConfigurationService.turnCodeToValue(ConstantValue.SHOW_CURRENT_GRID_LEVEL_OUTLINE, null, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(StringUtils.isNotBlank(SHOW_CURRENT_GRID_LEVEL_OUTLINE)){
				initMapParams.put("SHOW_CURRENT_GRID_LEVEL_OUTLINE", SHOW_CURRENT_GRID_LEVEL_OUTLINE);
				List<Long> infoOrgIdList = new ArrayList<Long>();
				if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
					for(OrgEntityInfoBO org : userInfo.getInfoOrgList()) {
						infoOrgIdList.add(org.getOrgId());
					}
				}
				//-- 获取对应的网格根节点
				List<MixedGridInfo> list = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
				if(list != null && list.size()>0){
					String gridIds = "",infoOrgCodes = "";
					for(int i=0;i<list.size();i++){
						gridIds = gridIds + list.get(i).getGridId() + ",";
						infoOrgCodes = infoOrgCodes + list.get(i).getInfoOrgCode() + ",";
					}
					initMapParams.put("gridIds", gridIds);
					initMapParams.put("infoOrgCodes", infoOrgCodes);
					initMapParams.put("currentUserGridLevel", list.get(0).getGridLevel());
				}
			}
			session.setAttribute("initMapParams", initMapParams);
		}
		map.put("isUserAnychat", initMapParams.get("isUserAnychat"));
		map.put("isUseMediasoup", initMapParams.get("isUseMediasoup"));
		map.put("AUTO_SHOW_GRID_LEVEL", initMapParams.get("AUTO_SHOW_GRID_LEVEL"));
		map.put("MAP_LEVEL_CFG", initMapParams.get("MAP_LEVEL_CFG"));
		map.put("MAP_LEVEL_TRIG_CONDITION_PROVINCE", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_PROVINCE"));
		map.put("MAP_LEVEL_TRIG_CONDITION_CITY", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_CITY"));
		map.put("MAP_LEVEL_TRIG_CONDITION_COUNTY", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_COUNTY"));
		map.put("MAP_LEVEL_TRIG_CONDITION_STREET", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_STREET"));
		map.put("MAP_LEVEL_TRIG_CONDITION_COMMUNITY", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_COMMUNITY"));
		map.put("MAP_LEVEL_TRIG_CONDITION_GRID", initMapParams.get("MAP_LEVEL_TRIG_CONDITION_GRID"));
		map.put("SURVEILLANCE_MSG_METHOD", initMapParams.get("SURVEILLANCE_MSG_METHOD"));
		map.put("kuangxuanFlag", initMapParams.get("kuangxuanFlag"));
		map.put("DIVISIONS_LEVEL_NAME_CFG", initMapParams.get("DIVISIONS_LEVEL_NAME_CFG"));
		map.put("AUTOMATIC_CLEAR_MAP_LAYER", initMapParams.get("AUTOMATIC_CLEAR_MAP_LAYER"));
		map.put("IS_IMAGE_MAP_SHOW_CONTOUR", initMapParams.get("IS_IMAGE_MAP_SHOW_CONTOUR"));
		map.put("IS_VECTOR_MAP_SHOW_CONTOUR", initMapParams.get("IS_VECTOR_MAP_SHOW_CONTOUR"));
		map.put("IS_GRID_ARCGIS_SHOW_CENTER_POINT", initMapParams.get("IS_GRID_ARCGIS_SHOW_CENTER_POINT"));
		map.put("LC_INFO_ORG_CODE", initMapParams.get("LC_INFO_ORG_CODE"));
		map.put("OUTLINE_FONT_SETTINGS", initMapParams.get("OUTLINE_FONT_SETTINGS"));
		map.put("OUTLINE_FONT_SETTINGS_BUILD", initMapParams.get("OUTLINE_FONT_SETTINGS_BUILD"));
		map.put("IS_ACCUMULATION_LAYER", initMapParams.get("IS_ACCUMULATION_LAYER"));
		map.put("ARCGIS_DOCK_MODE", initMapParams.get("ARCGIS_DOCK_MODE"));
		map.put("CENTER_POINT_SETTINGS_BUILD", initMapParams.get("CENTER_POINT_SETTINGS_BUILD"));
		map.put("arcgisFactorUrl", initMapParams.get("arcgisFactorUrl"));
		map.put("isUserMmp", initMapParams.get("isUserMmp"));
		map.put("anyChatServerUrl", initMapParams.get("anyChatServerUrl"));
		map.put("SHOW_CURRENT_GRID_LEVEL_OUTLINE", initMapParams.get("SHOW_CURRENT_GRID_LEVEL_OUTLINE"));
		map.put("gridIds", initMapParams.get("gridIds"));
		map.put("infoOrgCodes", initMapParams.get("infoOrgCodes"));
		map.put("currentUserGridLevel", initMapParams.get("currentUserGridLevel"));
		return initMapParams;
	}
	
	/**
	 * 合并两个对象非空属性
	 * @param <M>
	 * @param target
	 * @param destination
	 * @throws Exception
	 */
	public static <M> void mergeBean(M target, M destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
        
        for(PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if(descriptor.getWriteMethod() != null) {
                Object originalValue = descriptor.getReadMethod().invoke(target);

                // Only copy values values where the destination values is null
                if(originalValue == null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(destination);
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }
            }
        }
    }
	
	/**
	 * 对象依据指定属性按照拼音排序，要求该对象具有该属性，且该属性值不为空
	 * @param <M>
	 * @param resultList			待比较的对象列表
	 * @param comparePropertyName	进行比较的对象属性
	 * @param orderby				1 升序；2 降序；
	 */
	public static <M> void beanListSort4Pinyin(List<M> resultList, String comparePropertyName, int orderby) {
		if(resultList != null && resultList.size() > 1) {
			 Collections.sort(resultList, new Comparator<M>() {
				 public int compare(M m1, M m2) {
					 BeanInfo beanInfo1 = null, beanInfo2 = null;
					 Object valObj1 = null, valObj2 = null;
					 int result = 0;
					 
					 try {
						 beanInfo1 = Introspector.getBeanInfo(m1.getClass());
						 beanInfo2 = Introspector.getBeanInfo(m2.getClass());
						 
						 if(beanInfo1 != null) {
							 for(PropertyDescriptor descriptor : beanInfo1.getPropertyDescriptors()) {
								 if(descriptor.getName().equals(comparePropertyName)) {
									 valObj1 = descriptor.getReadMethod().invoke(m1);
									 break;
								 }
							 }
						 }
						 
						 if(beanInfo2 != null) {
							 for(PropertyDescriptor descriptor : beanInfo2.getPropertyDescriptors()) {
								 if(descriptor.getName().equals(comparePropertyName)) {
									 valObj2 = descriptor.getReadMethod().invoke(m2);
									 break;
								 }
							 }
						 }
					 } catch(Exception e) {
						 e.printStackTrace();
					 }
					 
					 if(valObj1 != null && valObj2 != null) {
						 String name1 = valObj1.toString(), name2 = valObj2.toString();
						 
						 if(StringUtils.isNotBlank(name1) && StringUtils.isNotBlank(name2)) {
							 Collator instance = Collator.getInstance(Locale.CHINA);
							 int direct = 1;
							 
							 result = instance.compare(name1, name2);
							 
							 switch(orderby) {
								 case 1 : {//升序
									 direct = 1; break;
								 }
								 case 2 : {//降序
									 direct = -1; break;
								 }
								 default : {
									 direct = 1; break;
								 }
							 }
							 
							 result *= direct;
						 }
					 }
					 
					 return result;
				 }
			 });
		}
	}
	
	/**
	 * map依据指定属性按照拼音排序，要求该对象具有该属性，且该属性值不为空
	 * @param resultList			待比较的map列表
	 * @param comparePropertyName	进行比较的key
	 * @param orderby				1 升序；2 降序；
	 */
	public static void mapListSort4Pinyin(List<Map<String, Object>> resultList, String comparePropertyName, int orderby) {
		if(resultList != null && resultList.size() > 1) {
	        Collections.sort(resultList, new Comparator<Map<String, Object>>() {
	            public int compare(Map<String, Object> object1, Map<String, Object> object2) {
	            	int result = 0;
	            	
            		if(isNotBlank(object1, comparePropertyName) && isNotBlank(object2, comparePropertyName)) {
            			String name1 = object1.get(comparePropertyName).toString();
            			String name2 = object2.get(comparePropertyName).toString();
            			int direct = 1;
            			
            			Collator instance = Collator.getInstance(Locale.CHINA);
		                
		                result = instance.compare(name1, name2);
		                
		                switch(orderby) {
							 case 1 : {//升序
								 direct = 1; break;
							 }
							 case 2 : {//降序
								 direct = -1; break;
							 }
							 default : {
								 direct = 1; break;
							 }
						 }
						 
						 result *= direct;
            		}
	            	
	                return result;
	            }
	        });
		}
    }
	
	/**
	 * 判断字符串是否为Base64加密串
	 * 
	 * Base64加密串满足如下条件：
	 * 1、字符串只可能包含A-Z，a-z，0-9，+，/，=字符
	 * 2、字符串长度是4的倍数
	 * 3、=只会出现在字符串最后，可能没有或者一个等号或者两个等号
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		boolean flag = false;
		
		if (StringUtils.isNotBlank(str) && str.length() % 4 == 0) {
			char[] strChars = str.toCharArray();
			
			for (char c : strChars) {
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') 
						|| c == '+' || c == '/' || c == '=') {
					flag = true;
				} else {
					flag = false;
					break;
				}
			}
		}
		
		return flag;
	}
}