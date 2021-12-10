package cn.ffcs.zhsq.wap.grid.common;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	/** 何种数据库 */
	public static String DB_VERSION = "MYSQL";// MYSQL;SQLSERVER

	/** 数据库中BOOL值的定义 */
	public static String DB_BOOL_TRUE_VALUE = "1";
	public static String DB_BOOL_FALSE_VALUE = "0";
	
	/** 数据库模式定义 */
	public static String SCHEMA_PLATFORM = "PLATFORM";
	public static String SCHEMA_CMS = "CMS";
	public static String SCHEMA_RESIDENTDB = "RESIDENTDB";
	public static String SCHEMA_IPM = "IPM";
	public static String SCHEMA_CIAPP = "CIAPP";
	public static String SCHEMA_FRIEDN = "FRIEND";
	public static String SCHEMA_OA = "OA";
	public static String SCHEMA_DOC = "DOC";
	public static String SCHEMA_KM = "KM";
	public static String SCHEMA_AM = "AM";
	public static String SCHEMA_PM = "PM";
	public static String SCHEMA_HR = "HR";
	public static String SCHEMA_ASSET = "ASSET";
	public static String SCHEMA_FM = "FM";
	public static String SCHEMA_EMS = "EMS";
	public static String SCHEMA_ORB = "ORB";
	public static String SCHEMA_CMB ="CMB";
	public static String SCHEMA_OBD = "OBD";
	public static String SCHEMA_PDB = "PDB";
	
	public  final static String SCHEMA_PLATFORM_USER = "S_USER";//用户表
	
	public  final static String SCHEMA_PLATFORM_DATA_DICT = "S_DATA_DICT";//字典表
	
	public static final String PEND_TABLE = Constants.SCHEMA_PLATFORM+".WF_PEND";
	
	public static String GENERATE_KEY = "identity";
	
	public static Map schemaMap = new HashMap();
	
	public static String PLATFORM_SCHEMA_KEY = "platform";

	/** 用户登录信息标记 */
	public static final String LOGIN_USER_SECTORS_TORKEN = "login.user.sectors";
	public static final String LOGIN_USER_TORKEN = "login.user";
	public static final String LOGIN_USER_SUPER = "login.super";
	public static final String LOGIN_USER_ORG_TORKEN = "login.user.org";
	public static final String LOGIN_USER_ORGS_TORKEN = "login.user.orgs";
	public static final String LOGIN_USER_ORG_CODE = "login.user.org.code";
	public static final String LOGIN_CATEGORY = "login.category";
	public static final String USER_MENU_TREE = " user.menu.tree";
	public static final String USER_ACTION_MAP = "user.action.map";
	public static final String USER_AUTH_URI = "user.auth.uri";
	public static final String USER_CONTEXTMENU = "user.contextmenu";
	public static final String USER_BASE_SYS_ID = "base_sys_id";
	public static final String LOGIN_USER_ID = "login.user.id";
	public static final String LOGIN_USER_NAME = "login.user.name";
	public static final String USER_PORTLET = "user.portlet";
	public static final String USER_DESKTOP_LAYOUT = "user.desktop.layout";
	public static final String USER_ORG_REGION = "user.org.region";
	public static final String USER_THEMES = "user.theme";
	public static final String USER_CURRENT_CLIENT = "user.current.client";
	public static final String ORG_AUTH_URI = "org.auth.uri";
	public static final String CALLBACK_SESSION="calback.session";

	public final static String XML_DOCUMENT_HEAD = "<?xml version='1.0' encoding='UTF-8'?>";
	
	public static final int RECORD_ALL_OBJECT = 0;
	public static final int RECORD_USER_PERM = 1;
	public static final int RECORD_ORG_PERM = 2;
	public static final int RECORD_OFFICE_PERM = 3;
	
	/***********代办紧急类型**************/
	public static final String TODO_URGENCY_SIMPLE = "普通";
	public static final String TODO_URGENCY_DISPATCH = "急件";
	public static final String TODO_URGENCY_URGENCY = "紧急";
	
   	/***********业务单据状态类型**************/
	public static final String BUSSINESS_STATE_NO_SUBMIT = "1"; 
	public static final String BUSSINESS_STATE_SUBMIT = "2";
	public static final String BUSSINESS_STATE_END = "3";
	public static final String FILE_UPLOAD_PATH = "";
	
	public final static Map<String,String> categoryMap = new HashMap<String,String>();
	static {
		/*categoryMap.put("1", "网站管理");
		categoryMap.put("2", "居民管理");
		categoryMap.put("3", "沟通平台");
		categoryMap.put("4", "平安社区");
		categoryMap.put("5", "社区服务");
		categoryMap.put("6", "系统管理");*/
		categoryMap.put("9", "系统管理");
	}
	public static final String MSG_NUMBER = "059188286133";//晋江：059585472988//福州：059188286133//长乐：059128830259，通过groovy进行控制
	//用于区分省内外，如重庆、贵州
	public static final String REGION="region";
	public static final String REGION_CQ="cq";
	
	/**
	 * 项目的物理路径
	 * 
	 * @see cn.ffcs.web.servlet.ContextListener#contextInitialized
	 */
	public static String SCIM_PATH = "";
	
	/**
	 * 企业总机服务器地址
	 * 
	 * @see cn.ffcs.web.servlet.ContextListener#contextInitialized
	 */
	public static String IM_ROOT_HTTP_PORT_ADDR = "";
	
	
	
	//出租屋管理
	public static String RENTALHOUSE_OBJ_TYPE = "01";
	public static String RENTALHOUSE_OBJ_CAT = "03";
	
	//全球眼管理
	public static String MONITOR_OBJ_TYPE = "01020501";
	public static String MONITOR_OBJ_CAT = "02";
	
	public static final String STATUS_SUCCESS = "0";    //成功的状态
	public static final String STATUS_FAIL = "1";       //失败的状态
    public static final String CLIENT_SESSION_ID = "client_session_id";//jsessionid
    public static final String CLIENT_TOKEN_ID = "tokenKey";
    public static final int TOKEN_TIMEOUT = 20;

    
}