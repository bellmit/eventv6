/**
 * 
 */
package cn.ffcs.zhsq;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ffcs.system.publicFilter.CommonController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.ffcs.common.Total;
import cn.ffcs.uam.bo.UserInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.grid.MixedGridInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.GisMapConfigInfo;
import cn.ffcs.shequ.mybatis.domain.zzgl.map.data.GisMapInfo;
import cn.ffcs.shequ.zzgl.service.grid.IMixedGridInfoService;
import cn.ffcs.shequ.zzgl.service.map.IGisInfoService;
import cn.ffcs.system.publicUtil.Base64;
import cn.ffcs.uam.service.IFunConfigurationService;
import cn.ffcs.zhsq.base.local.service.IGridLogService;
import cn.ffcs.zhsq.mybatis.domain.map.menuconfigure.GisDataCfg;
import cn.ffcs.zhsq.utils.CommonFunctions;
import cn.ffcs.zhsq.utils.ConstantValue;
import cn.ffcs.zhsq.utils.domain.App;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 综治基础控制器
 *
 */
public class ZZBaseController extends CommonController {
	@Autowired
	protected IMixedGridInfoService mixedGridInfoService;
	@Autowired
	private IFunConfigurationService funConfigurationService;
	@Autowired
	protected IGridLogService logService; //日志服务
	@Autowired 
	protected IGisInfoService gisInfoService; //gis地图相关服务
	
	protected static final String KEY_START_GRID_ID = "startGridId"; //默认网格入口
	protected static final String KEY_START_GRID_PHOTO = "startGridPhoto"; //默认网格封面
	protected static final String KEY_START_GRID_NAME = "startGridName"; //默认网格名称
	protected static final String KEY_START_GRID_LEVEL = "startGridLevel"; //默认网格名称
	protected static final String KEY_START_GRID_CODE_NAME = "startGridCodeName"; //默认网格名称
	protected static final String KEY_START_GRID_CODE = "startGridCode"; //默认网格编码
	protected static final String KEY_DEFAULT_INFO_ORG_ID = "defaultInfoOrgId"; //默认信息域组织ID
	protected static final String KEY_DEFAULT_INFO_ORG_CODE = "defaultInfoOrgCode"; //默认信息域组织编码
	protected static final String KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE = "defaultEncryptInfoOrgCode"; //已加密的默认地域编码
	protected static final String KEY_DEFAULT_INFO_ORG_NAME = "defaultInfoOrgName"; //默认信息域组织名称
	//福州市级平台的特定排序
	protected static final String GRID_CODE[] ={"350102","350103","350104","350111","350105","350181","350182","350121","350122","350124","350123","350125"}; 
	protected static final int[] STAT_OF_GRID_SQ={69,52,0,66,14,0,22,27,31,22,7,10};//64,42
	
	protected boolean isEmpty(Object obj){
		if(obj==null || "".equals(obj+"") || "null".equals(obj+"") || ((obj+"").trim()).isEmpty()) return true;
		
		return false;
	}
	/**
	 * 获取登录用户对应的默认信息域
	 * @param session
	 * @return
	 */
	protected Map<String, Object> getDefaultOrgInfo(HttpSession session) {
		Map<String, Object> defaultOrgInfo = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//-- 获取管理的信息域组织
		Long defaultInfoOrgId = -99L;
		String defaultInfoOrgCode = "";
		String defaultInfoOrgName = "";
		List<Long> infoOrgIdList = new ArrayList<Long>();
		if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
			defaultInfoOrgId = userInfo.getInfoOrgList().get(0).getOrgId();
			infoOrgIdList.add(defaultInfoOrgId);
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
			defaultInfoOrgName = userInfo.getInfoOrgList().get(0).getOrgName();
		}
		defaultOrgInfo.put(KEY_DEFAULT_INFO_ORG_ID, defaultInfoOrgId);
		defaultOrgInfo.put(KEY_DEFAULT_INFO_ORG_CODE, defaultInfoOrgCode);
		defaultOrgInfo.put(KEY_DEFAULT_INFO_ORG_NAME, defaultInfoOrgName);
		return defaultOrgInfo;
	}
	
	/**
	 * 根据网格orgCode获取网格数据
	 * @param orgCode
	 * @return
	 */
	public MixedGridInfo getGridInfoByOrgCode(String orgCode){
		MixedGridInfo gridInfo = mixedGridInfoService.getDefaultGridByOrgCode(orgCode);
		return gridInfo;
	}
	
	/**
	 * 获取登录用户对应的默认网格
	 * @param session
	 * @return
	 */
	protected Map<String, Object> getDefaultGridInfo(HttpSession session) {
		Map<String, Object> defaultGridInfo = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//-- 获取管理的信息域组织
		Long defaultInfoOrgId = -99L;
		String defaultInfoOrgCode = "";
		List<Long> infoOrgIdList = new ArrayList<Long>();
		if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
			defaultInfoOrgId = userInfo.getInfoOrgList().get(0).getOrgId();
			infoOrgIdList.add(defaultInfoOrgId);
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
		}
		defaultGridInfo.put(KEY_DEFAULT_INFO_ORG_ID, defaultInfoOrgId);
		defaultGridInfo.put(KEY_DEFAULT_INFO_ORG_CODE, defaultInfoOrgCode);
		
		if(StringUtils.isNotBlank(defaultInfoOrgCode)) {//构建加密的地域编码
			defaultGridInfo.put(KEY_DEFAULT_ENCRYPT_INFO_ORG_CODE, new String(Base64.encode(defaultInfoOrgCode.getBytes())));
		}
		
		//-- 获取对应的网格根节点
		List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
		//-- 设置列表默认网格入口
		Long startGridId = -99L;
		String startGridName = "";
		String startGridCodeName = "";
		String startGridPhoto = "";
		Integer startGridLevel = 0;
		if(gridInfoList!=null && gridInfoList.size()>0) {
			startGridId = gridInfoList.get(0).getGridId();
			startGridName = gridInfoList.get(0).getGridName();
			startGridCodeName = (gridInfoList.get(0).getGridCode() + " - " + gridInfoList.get(0).getGridName());
			startGridPhoto = gridInfoList.get(0).getGridPhoto();
			startGridLevel = gridInfoList.get(0).getGridLevel();
		}
		defaultGridInfo.put(KEY_START_GRID_ID, startGridId);
		defaultGridInfo.put(KEY_START_GRID_PHOTO, startGridPhoto);
		defaultGridInfo.put(KEY_START_GRID_NAME, startGridName);
		defaultGridInfo.put(KEY_START_GRID_CODE_NAME, startGridCodeName); 
		defaultGridInfo.put(KEY_START_GRID_LEVEL, startGridLevel); 
		return defaultGridInfo;
	}
	
	protected String getDefaultInfoOrgCode(HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		// -- 获取管理的信息域组织
		String defaultInfoOrgCode = "";
		if (userInfo.getInfoOrgList() != null && userInfo.getInfoOrgList().size() > 0) {
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
		}
		return defaultInfoOrgCode;
	}
	
	protected String getDefaultInfoOrgName(HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		// -- 获取管理的信息域组织
		String defaultInfoOrgName = "";
		if (userInfo.getInfoOrgList() != null && userInfo.getInfoOrgList().size() > 0) {
			defaultInfoOrgName = userInfo.getInfoOrgList().get(0).getOrgName();
		}
		return defaultInfoOrgName;
	}
	
	/**
	 * 2013-08-06 liush
	 * 获取登录用户对应的默认网格，没有等级的限制
	 * @param session
	 * @return
	 */
	protected Map<String, Object> getDefaultGridInfoForEvent(HttpSession session) {
		Map<String, Object> defaultGridInfo = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		//-- 获取管理的信息域组织
		Long defaultInfoOrgId = -99L;
		String defaultInfoOrgCode = "";
		List<Long> infoOrgIdList = new ArrayList<Long>();
		if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
			defaultInfoOrgId = userInfo.getInfoOrgList().get(0).getOrgId();
			infoOrgIdList.add(defaultInfoOrgId);
			defaultInfoOrgCode = userInfo.getInfoOrgList().get(0).getOrgCode();
		}
		defaultGridInfo.put(KEY_DEFAULT_INFO_ORG_ID, defaultInfoOrgId);
		defaultGridInfo.put(KEY_DEFAULT_INFO_ORG_CODE, defaultInfoOrgCode);
		//-- 获取对应的网格根节点
		List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridMappingListByOrgIdListForEvent(infoOrgIdList);
		//-- 设置列表默认网格入口
		Long startGridId = -99L;
		String startGridName = "";
		String startGridCodeName = "";
		String startGridPhoto = "";
		if(gridInfoList!=null && gridInfoList.size()>0) {
			startGridId = gridInfoList.get(0).getGridId();
			startGridName = gridInfoList.get(0).getGridName();
			startGridCodeName = (gridInfoList.get(0).getGridCode() + " - " + gridInfoList.get(0).getGridName());
			startGridPhoto = gridInfoList.get(0).getGridPhoto();
		}
		defaultGridInfo.put(KEY_START_GRID_ID, startGridId);
		defaultGridInfo.put(KEY_START_GRID_PHOTO, startGridPhoto);
		defaultGridInfo.put(KEY_START_GRID_NAME, startGridName);
		defaultGridInfo.put(KEY_START_GRID_CODE_NAME, startGridCodeName); 
		return defaultGridInfo;
	}
	/**
	 * 获取当前请求对应grid信息
	 * @param session
	 * @param request
	 * @return
	 */
	protected MixedGridInfo getMixedGridInfo(HttpSession session, HttpServletRequest request){
		MixedGridInfo defaultGridInfo=null;
		Long gridId;
		if(!StringUtils.isBlank(request.getParameter("gridId"))){
			gridId = Long.parseLong(request.getParameter("gridId"));
			defaultGridInfo = mixedGridInfoService.findMixedGridInfoById(gridId, false);
		}	
		else{
			UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
			Long defaultInfoOrgId = -99L;
			List<Long> infoOrgIdList = new ArrayList<Long>();
			if(userInfo.getInfoOrgList()!=null && userInfo.getInfoOrgList().size()>0) {
				defaultInfoOrgId = userInfo.getInfoOrgList().get(0).getOrgId();
				infoOrgIdList.add(defaultInfoOrgId);
			}
			//-- 获取对应的网格根节点
			List<MixedGridInfo> gridInfoList = mixedGridInfoService.getMixedGridMappingListByOrgIdList(infoOrgIdList, ConstantValue.ORG_TYPE_ALL);
			if(gridInfoList!=null && gridInfoList.size()>0) {
				defaultGridInfo = gridInfoList.get(0);
			}
		}
		return defaultGridInfo;
	}
	
	/**
	 * 输出xml文件
	 * @param res
	 * @param xmlStr
	 * @throws Exception
	 */
	public void outXml(HttpServletResponse res, String xmlStr) throws Exception {
		res.setCharacterEncoding("utf-8");
		res.setContentType("application/xml");
		res.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = res.getWriter();
		pw.write(xmlStr);
		pw.close();
	}
	
	
	
	/**
	 * 输出json文件
	 * @param res
	 * @param xmlStr
	 * @throws Exception
	 */
	public void outJosn(HttpServletResponse res, String jsonStr) throws Exception {
		res.setCharacterEncoding("utf-8");
		res.setContentType("application/json;charset=UTF-8");
		res.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = res.getWriter();
		pw.write(jsonStr);
		pw.close();
	}
	
	public void outJs(HttpServletResponse res, String js) throws Exception {
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/javascript;charset=UTF-8");
		PrintWriter pw = res.getWriter();
		pw.write(js);
		pw.close();
	}
	/**
	 * 返回json字段形式字符串，如： "name":"张三"
	 * @param Name
	 * @param obj
	 * @return
	 */
	public String getJsonFieldString(String fieldName, Object value){
		String name = "\""+fieldName+"\"";
		if(value==null){
			return name+":\"\"";
		}else{
			if(value.getClass()==Long.class || value.getClass()==Integer.class|| value.getClass()==Float.class){
				return name+":"+value.toString();
			}else{
				return name+":\""+value.toString()+"\"";
			}
		}
	}
	/**
	 * 返回xml元素形式字符串，如： <name>张三</name>
	 * @param fieldName
	 * @param value
	 * @param isCDATA
	 * @return
	 */
	public String getXmlElementString(String fieldName, Object value, boolean isCDATA){
		String val = (value==null)? "": value.toString();
		if(isCDATA){
			return "<"+fieldName+"><![CDATA["+val+"]]></"+fieldName+">";
		}else{
			return "<"+fieldName+">"+val+"</"+fieldName+">";
		}
	}
	
	
	/**
	 * 
	 * @param gisInfo
	 * @return
	 */
	public String getGisInfoXml(GisMapInfo gisInfo) {
		StringBuffer sb_xml = new StringBuffer("\n<GisForm>");
		sb_xml.append("\n<id>"+gisInfo.getId()+"</id>");
		sb_xml.append("\n<wid>"+gisInfo.getWid()+"</wid>");
		sb_xml.append("\n<gridId>"+gisInfo.getGridId()+"</gridId>");
		sb_xml.append("\n<gridCode>"+gisInfo.getGridCode()+"</gridCode>");
		sb_xml.append("\n<level>"+gisInfo.getLevel()+"</level>");
		sb_xml.append("\n<mapt>"+gisInfo.getMapt()+"</mapt>");
		sb_xml.append("\n<gridName>"+gisInfo.getGridName()+"</gridName>");
		sb_xml.append("\n<code>"+gisInfo.getInfoOrgCode()+"</code>");
		sb_xml.append("\n<gid>"+gisInfo.getGid()+"</gid>");
		sb_xml.append("\n<hs>"+gisInfo.getHs()+"</hs>");
		sb_xml.append("\n<x>"+gisInfo.getX()+"</x>");
		sb_xml.append("\n<y>"+gisInfo.getY()+"</y>");
		sb_xml.append("\n<c>"+gisInfo.getC()+"</c>");
		sb_xml.append("\n<name>"+gisInfo.getName()+"</name>");
		sb_xml.append("\n<path>"+gisInfo.getPath()+"</path>");
		sb_xml.append("\n<pid>"+gisInfo.getPid()+"</pid>");
		sb_xml.append("\n<address>"+gisInfo.getAddress()+"</address>");
		sb_xml.append("\n<legdate>"+gisInfo.getLegdate()+"</legdate>");
		sb_xml.append("\n<isGreatEvent>"+gisInfo.getIsGreatEvent()+"</isGreatEvent>");
		sb_xml.append("\n<urgencyDegree>"+gisInfo.getUrgencyDegree()+"</urgencyDegree>");
		if(gisInfo.getTid()!=null) sb_xml.append("\n<tid>"+gisInfo.getTid()+"</tid>");
		sb_xml.append("\n<type>"+gisInfo.getType()+"</type>");
		sb_xml.append("\n<buildingName>"+gisInfo.getBuildingName()+"</buildingName>");
		sb_xml.append("\n</GisForm>");
		return sb_xml.toString();
	}
	
	/**
	 * 呼叫中心事件录入
	 * @param gisInfo
	 * @return
	 */
	public String getGisInfoCallInXml(GisMapInfo gisInfo,String id,String sCalleeNbr) {
		StringBuffer sb_xml = new StringBuffer("\n<Gisinfo>");
		sb_xml.append("\n<id>"+gisInfo.getId()+"</id>");
		sb_xml.append("\n<wid>"+gisInfo.getWid()+"</wid>");
		sb_xml.append("\n<gridId>"+gisInfo.getGridId()+"</gridId>");
		sb_xml.append("\n<gridCode>"+gisInfo.getGridCode()+"</gridCode>");
		sb_xml.append("\n<level>"+gisInfo.getLevel()+"</level>");
		sb_xml.append("\n<mapt>"+gisInfo.getMapt()+"</mapt>");
		sb_xml.append("\n<gridName>"+gisInfo.getGridName()+"</gridName>");
		sb_xml.append("\n<code>"+gisInfo.getInfoOrgCode()+"</code>");
		sb_xml.append("\n<gid>"+gisInfo.getGid()+"</gid>");
		sb_xml.append("\n<hs>"+gisInfo.getHs()+"</hs>");
		sb_xml.append("\n<x>"+gisInfo.getX()+"</x>");
		sb_xml.append("\n<y>"+gisInfo.getY()+"</y>");
		sb_xml.append("\n<c>"+gisInfo.getC()+"</c>");
		sb_xml.append("\n<name>"+gisInfo.getName()+"</name>");
		sb_xml.append("\n<path>"+gisInfo.getPath()+"</path>");
		sb_xml.append("\n<pid>"+gisInfo.getPid()+"</pid>");
		sb_xml.append("\n<address>"+gisInfo.getAddress()+"</address>");
		sb_xml.append("\n<userId>"+id+"</userId>");
		sb_xml.append("\n<phone>"+sCalleeNbr+"</phone>");
		sb_xml.append("\n<legdate>"+gisInfo.getLegdate()+"</legdate>");
		sb_xml.append("\n<urgencyDegree>"+gisInfo.getUrgencyDegree()+"</urgencyDegree>");
		//sb_xml.append("\n<isGreatEvent>"+gisInfo.getIsGreatEvent()+"</isGreatEvent>");
		if(gisInfo.getTid()!=null) sb_xml.append("\n<tid>"+gisInfo.getTid()+"</tid>");
		sb_xml.append("\n<type>"+gisInfo.getType()+"</type>");
		sb_xml.append("\n</Gisinfo>");
		return sb_xml.toString();
	}
	
	/**
	 * 将List转化为xml格式的数据
	 * 
	 * @param totalNum
	 * @param inList
	 *            ，需要转换的list
	 * @return String
	 */
	public String getXmlFromList(long totalNum, List inList) {
		Total total = new Total();
		// 创建临时的List对象
		List results = inList;
		results.add(total);
		// 创建XStream对象
		XStream xs = new XStream(new DomDriver());
		// 为所有的类创建别名，别名为不包含包名的类名
		for (int i = 0; i < results.size(); i++) {
			Class clzz = results.get(i).getClass();
			// 得到全限定类名
			String fullName = clzz.getName();
			// System.out.println("==========="+fullName);

			// 以"."分割字符串
			String[] temp = fullName.split("\\.");
			xs.alias(temp[temp.length - 1], clzz);
		}
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ xs.toXML(results);
		return xmlString;
	}
	
	/**
	 * 域名内容进行替换,支持多个域名访问综治网格
	 * @param request
	 * @param url
	 * @return
	 */	
	protected String getNewUrl(HttpSession session, String url) {
		try {
			String defaultDomain = getTopDomain(url);
			String newDoMain = App.TOP.getDomain(session);
			if (org.apache.commons.lang.StringUtils.isNotBlank(newDoMain)){
				url = url.replace(defaultDomain, newDoMain);//都进行替换,因为即使域名一样,session也不同,每个登陆用户都有一个session			
			}			
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return url;
		}
	}
	
	 /**
	 * 提取一串url中的顶级域名
	 * @param path
	 * @return
	 */
	private String getTopDomain(String path){
		try {
			String pathInfo = path;
			pathInfo = pathInfo.replace("http://", "");
			int endIndex = pathInfo.indexOf("/");
			if(endIndex>0){
				pathInfo = pathInfo.substring(0,endIndex);
			}
			
			int index = pathInfo.indexOf(".");
			pathInfo = pathInfo.substring(index+1,pathInfo.length());
			
			/*if(pathInfo.indexOf(":")>0){
				pathInfo = pathInfo.substring(0,pathInfo.indexOf(":"));
			}*/
			return pathInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 2015-04-01 liushi add
	 * 解析地图专题图层相关配置参数
	 * @param elementsCollectionStr
	 * @param elementsName
	 * @return
	 */
	public String analysisOfElementsCollection(String elementsCollectionStr,String elementsName){
		String[] ecs = elementsCollectionStr.split(",_,");
		for(int i=0;i<ecs.length;i++){
			String[] e = ecs[i].split("_,_");
			if(elementsName.equals(e[0])){
				return e[1];
			}
		}
		return "";
	}
	/**
	 * 2015-06-09 liushi add
	 * 根据模块代码获取模块使用地图的类型
	 * @param session 
	 * @param mapType 模块地图类型代码
	 * @return
	 */
	public String getCurrentMapType(HttpSession session,String mapType) {
		Map<String, Object> defaultGridInfo = this.getDefaultGridInfo(session);
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		MixedGridInfo gridInfo = mixedGridInfoService.findMixedGridInfoById(Long.valueOf(defaultGridInfo.get(KEY_START_GRID_ID).toString()), false);
		String mapTypeResult=null;
		if(mapType!=null && !"".equals(mapType) && mapType.length()>1) {
			mapTypeResult = this.funConfigurationService.turnCodeToValue("MAP_TYPE_CODE", mapType, IFunConfigurationService.CFG_TYPE_FACT_VAL, userInfo.getOrgCode(), IFunConfigurationService.CFG_ORG_TYPE_0);
			if(mapTypeResult != null) {
				mapType = mapTypeResult;
			}
		}
		String orgCode = userInfo.getOrgCode();
		String mapEngineType = "";
		if(orgCode.indexOf("350111106")==0) {
			mapEngineType="005";
		}else {
			mapEngineType = gridInfo.getMapType();
		}
		if("005".equals(mapEngineType)) {
			if ("2".equals(mapType)) mapType = "5";
			if("3".equals(mapType)) mapType = "30";
		}else if("004".equals(mapEngineType)) {
			GisMapConfigInfo mapConfig = gisInfoService.findGisMapConfigInfoByOrgCode(gridInfo.getInfoOrgCode());
			if ("2".equals(mapType)) mapType = String.valueOf(mapConfig.getMap_type());
			if("3".equals(mapType)) mapType = String.valueOf(mapConfig.getMap_type_3D());
		}
		return mapType;
	}
	
	protected void transGisDataCfgUrl(List<GisDataCfg> gisDataCfgList,HttpSession session) {
		for(GisDataCfg obj:gisDataCfgList) {
			transGisDataCfgUrl(obj, session);
		}
	}
	
	protected void transGisDataCfgUrl(GisDataCfg obj, HttpSession session) {
		UserInfo userInfo = (UserInfo) session.getAttribute(ConstantValue.USER_IN_SESSION);
		if (obj != null) {
			String menuListUrl = obj.getMenuListUrl();
			String menuSummaryUrl = obj.getMenuSummaryUrl();
			String menuDetailUrl = obj.getMenuDetailUrl();
			String elementsCollectionStr = obj.getElementsCollectionStr();
			String callBack = obj.getCallBack();
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
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
				callBack = callBack.replaceAll(menuSummaryUrlDomain, actMenuSummaryUrlDomain);
			}
			if (menuDetailUrl != null && !"".equals(menuDetailUrl)) {
				String menuDetailUrlDomain = menuDetailUrl.split("/")[0];
				String actMenuDetailUrlDomain = CommonFunctions.getDomain(session, menuDetailUrlDomain);

				if (StringUtils.isBlank(actMenuDetailUrlDomain)) {
					actMenuDetailUrlDomain = funConfigurationService.turnCodeToValue("APP_DOMAIN", menuDetailUrlDomain,
							IFunConfigurationService.CFG_TYPE_URL, userInfo.getOrgCode(),
							IFunConfigurationService.CFG_ORG_TYPE_0);
				}

				menuDetailUrlDomain = "\\" + menuDetailUrlDomain;
				menuDetailUrl = menuDetailUrl.replaceFirst(menuDetailUrlDomain, actMenuDetailUrlDomain);
				elementsCollectionStr = elementsCollectionStr.replaceAll(menuDetailUrlDomain, actMenuDetailUrlDomain);
				callBack = callBack.replaceAll(menuDetailUrlDomain, actMenuDetailUrlDomain);
			}
			obj.setMenuListUrl(menuListUrl);
			obj.setMenuSummaryUrl(menuSummaryUrl);
			obj.setMenuDetailUrl(menuDetailUrl);
			obj.setElementsCollectionStr(elementsCollectionStr);
			obj.setCallBack(callBack);
			if (obj.getChildrenList() != null && obj.getChildrenList().size() > 0) {
				this.transGisDataCfgUrl(obj.getChildrenList(), session);
			}
		}
	}

	/**
	 * 从request中获得参数Map，并返回可读的Map
	 *
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if(null == valueObj){
				value = "";
			}else if(valueObj instanceof String[]){
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){
					value = values[i] + ",";
				}
				value = value.substring(0, value.length()-1);
			}else{
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}
}
