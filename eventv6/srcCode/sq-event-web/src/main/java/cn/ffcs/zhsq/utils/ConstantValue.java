/**
 *
 */
package cn.ffcs.zhsq.utils;

import cn.ffcs.shequ.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


/**
 * 常量类
 * @author guohh
 *
 */

public abstract class ConstantValue {
	//返回结果状态码
	public static final Integer SUCCESS_CODE=1;  //成功
	public static final Integer ERROR_CODE=0;	 //失败

    public static final String STATUS_SUCCESS = "0";    //成功的状态
    public static final String STATUS_FAIL = "1";       //失败的状态
	
	//-- 超时状态
	public static final String HANDLE_STATUS_TO = "00";//未过期
	public static final String HANDLE_STATUS_IN = "01";//将到期
	public static final String HANDLE_STATUS_OVER = "02";//已过期
	public static final String HANDLE_STATUS_OVER_TIME = "03";//超期
	
	//-- 启动 配置文件
	public static final String PORTAL_CONFIG_FILE = "global.properties";
	public static final String SERV_CONFIG_FILE = "serv-config.properties";
	public static final String PORTAL_CONFIG_PROPERTIES = "portal-config.properties";
	public static final String MARKER_TYPE_AQI = "7788"; //马尾设备
	//-- 文字过滤配置文件
	public static final String STRING_FILTER_CONFIG_FILE = "string-filter.properties";
	
	//-- 个性化开关配置文件
	public static final String INDIVIDUAL_CONFIG_FILE = "individual-config.properties";

	//-- global.properties配置文件
	public static final String GLOBAL_PROPERTIES = "global.properties";
	
	//-- UAM中的cookie token key
	public static String UAM_COOKIE_TOKEN_KEY = "memcached";

	// --需要访问到的数据库其他用户名
	public static String OTHER_DB_USER_ZHSQ_STAT = "zhsq_stat"; // --福建
	public static String OTHER_DB_USER_CMS = "cms"; // --福建
	
	//--是否是正式发布版本，发布需设置
	public static String SQ_USER_ID = "SQ_USER_ID_AQEBBQADSwAwSAJBAKsZnA0ZPlzaUveP8aUYqy3jQkuF3cVsxlOkrbYlS22N";
	public static boolean IS_PRODUCT = true;
	//public static String ACCESS_HOST = "gd";
	
	//楼宇经济访问路径
	//public static String SQ_LYJJ_URL="http://gd.fjsq.org:8080/lyjj";

	//-- 不同平台之间的加密私钥
	public static String ENCRYPTION_KEY_FOR_OTHER_PLATFORM = "www.cnsq.org";

	//-- 程序中的平台开关（福建 35、宁夏 64）
	public static int PLATFORM_SWITCH = 35;

	//-- 居民网站域名
	//public static String PLATFORM_DOMAIN_ROOT = "cnsq.org";

	//-- 平台cookie公钥
	public static String UAM_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKsZnA0ZPlzaUveP8aUYqy3jQkuF3cVsxlOkrbYlS22NPVwL1PHkrMYVlkrt04bxLhEXgHLDe+0kxzkRSEWdnRsCAwEAAQ==";

	//-- 统一登录地址
	public static String UAM_LOGIN_URL = "http://uam.cnsq.org/admin";

	//-- 资源服务器域名
	public static String RESOURSE_DOMAIN_KEY = "zzgrid";
	
	//redis相关
    //事件监控redis地址序号
    public static final Integer JEDIS_DB_EVENT = 5;//redis中事件库所使用的块编码
    public static final String EVENT_CODE = "eventCode";//redis存储今日事件编号的key值&&
    /**
	 * redis服务器地址编号
	 */
	public static final int JEDIS_DB_EVENT_ADDRESS = 5;
	
	//是否使用ES
		public static boolean IS_USE_ES = false;
//	//--资源文件访问服务器
	//public static String RESOURSE_SERVER_PATH="http://img.cnsq.org/zzgrid/";
	//public static String DEFAULT_RESOURSE_SERVER_PATH="http://img.cnsq.org/zzgrid/";
	//public static String UAM_DOMAIN_URL_PATH = "";
	
	//-- 实际综治网格域名
	//public static String NEW_ZZ_PLATFORM_DOMAIN = "fjsq.org";//先初始化
	
	//-- 图片服务器域名
	//public static String IMG_ZZGRID_DOMAIN = "http://img.fjsq.org";//先初始化

	//-- UI服务器域名
	//public static String SQ_UI_DOMAIN = "uiDomain";//
	//-- GMIS服务器域名
	//public static String SQ_GMIS_DOMAIN = "gmisDomain";//
	
	//--资源文件上传根路径
	public static String RESOURSE_SAVE_ROOT_PATH="/mnt/mfs/sq_upload";

	//GIS地图引擎服务器地址
	//public static String GIS_MAP_BASE_PATH="http://220.162.239.191:8998";
	//调用居民项目里面的短信发送路径
	//public static String SEND_MSG_URL="http://newsms.fjsq.org/msgSend/sendPage.jhtml";
	
	//oauth工程部署地址（获取菜单）
	//public static String OAUTH_URL="http://uam.ygj.aishequ.org";
	//zzgrid项目路径
	//public static String SQ_ZZGRID_URL="http://gd.fjsq.org:8093/zzgrid_p_v2.0.011";
	
	//public static String ANOLE_COMPONENT_URL="";
	
	//public static String RESOURCE_DOMAIN_URL="http://res.fjsq.org";
	
	//public static String SQ_ZHSQ_EVENT_URL="gd.fjsq.org:8094/sq_zhsq_event";
	//public static String WATER_MONITOR_URL="gd.fjsq.org:8094/sq_zhsq_event";
	//firegrid项目路径
	//public static String SQ_FIREGRID_URL="http://gd.fjsq.org";
	//icgrid_hp项目路径
	//public static String ICGRID_URL ="http://gd.fjsq.org";
	
	//工作流服务器地址
	//public static String WORKFLOW_SERVER_PATH="http://workflow.fjsq.org/workflow";
	
	//外系统域名
	//public static String RESIDENT_DOMAIN="http://rs";
	//public static String RESIDENT_IMPEXP_DOMAIN="http://bd";
	//public static String RESOURCE_DOMAIN="http://res";
	
	//人民调解.工作流表单ID
	public static String FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION = "105351";
	public static String FLOW_FORM_TYPE_ID_PETITION = "105352";
	
	//流动接口 外口数据
	//public static String  FLOW_POPULATION_URL="-1";

	//人口数据库域名
	//public static String  POPULATION_URL="-1";
	
	//是否跳转短信发送页面
	public static boolean IS_SEND_MSG_SHOW = false;
	
	/**
	 * 字典Pcode
	 */
	//便民服务网点类型
	public static final String SERVICE_OUTLETS_TYPE = "B556";
	//租户类型   表名称:T_DC_AREA_ROOM_RENT ，字段：LIVE_TYPE
	public static final String AREA_ROOM_RENT_LIVE_TYPE = "D134001";
	//两车管理类别
	public static final String PARKING_MANAGE_TYPE = "B561";
	
	public static final String DICT_PLACENAME = "B373";// 九小场所类型
	
	public static final String POOR_HOLD = "D186003";// 贫困户标识
	public static final String LOW_INCOME = "D186002";// 贫困户标识lowIncome
	public static final String DICT_ORG_LEVEL = "B000";// 组织层级
	
	//-- 安全生产.常规安全检查信息表
 	public static final String TABLE_NEW_SAFETY_CHECK_INFO = "t_zz_new_safety_check_info";
 	
	// --综治.安全生产.常规安全检查--处理状态
	public static final String COLUMN_DISPOSE_STATUS = "dispose_status";


	/**
	 * 工作流状态：归档
	 */
	public static String WORKFLOW_STATUS_FILE = "2";
	
	/**
	 * 海沧区功能域组织编码
	 */
	public static String HAICANG_FUNC_ORG_CODE="350205";
	/**
	 * 石狮市功能域组织编码
	 */
	public static String SHISHI_FUNC_ORG_CODE="350512";
	/**
	 * 晋江市功能域组织编码
	 */
	public static String JINJIANG_FUNC_ORG_CODE="350582";
	/**
	 * 南安市功能域组织编码
	 */
	public static String NANAN_FUNC_ORG_CODE="350583";
	/**
	 * 江阴市功能域组织编码
	 */
	public static String JIANGYIN_FUNC_ORG_CODE="320281";
	
	/**
	 * 盐都功能域组织编码
	 */
	public static String YANDU_FUNC_ORG_CODE="320903";

	/**
	 * 盐城功能域组织编码
	 */
	public static String YANCHENG_FUNC_ORG_CODE="3209";

	/**
	 * 盐都功能域组织编码
	 */
	public static String DAFENG_ORG_CODE="320904";

	/**
	 * 响水
	 */
	public static String XIANGSHUI_INFO_ORG_CODE="320921";
	
	/**
	 * 石狮 查看矛盾纠纷事件 涉及金额
	 */
	public static String SHISHI_INVOLVED_MONEY_ORG_CODE = "";
	
	/**
	 * 事件默认类别
	 */
	public static String EVENT_DEFAULT_TYPE = "";
	
	/**
	 * 屏蔽越级上报功能
	 */
	public static String EVENT_NO_SHOW = "";
    /**
     * 万宁功能域组织编码
     */
    public static String WANNING_FUNC_ORG_CODE="4690";
	/**
	 * 事件表所属用户
	 */
	public static String EVENT_DEFAULT_DATABASE = "";
	
	public static final String MAP_TYPE_TIANDITU = "005"; //地图类型，使用天地图
	
	public static boolean ENABLE_BTN_AUTHORITY = false;
	
	public static String PEACE_REPORT_URL = "";
	
	public static String IS_IMAGE_MAP_SHOW_CONTOUR = "false";
	
	public static String SPGIS_IP = "172.26.181.152";
	
	public static String ZHSQ_SZZG_DB = "";
	public static String WORKFLOW_DB = "";//工作流数据库用户名

	/**
	 * 综合执法人员组织编码
	 */
	public static String LAW_ENFORCE_OFFIC_FUNC_ORG_CODE="360502007A01";
    /**
     * 智慧云眼平台编码
     */
    public static String PLATFORM_CODE;
    public static String GZ_CODE = "ganzhou";
	/**
	 * 初始化配置
	 * @param configFilePath
	 */
	public static void init(String configFilePath){
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(configFilePath+PORTAL_CONFIG_FILE));
			props.load(in);
			
			//-- SQ_USER_ID
			String outputTmp = "---Load dbuser.zhsq_szzg: ";
			String ZHSQ_SZZG_DB = props.getProperty("dbuser.zhsq_szzg");
			if(ZHSQ_SZZG_DB!=null && !ZHSQ_SZZG_DB.trim().equals("")) {
				ConstantValue.ZHSQ_SZZG_DB = ZHSQ_SZZG_DB.trim() + ".";
				outputTmp += ConstantValue.ZHSQ_SZZG_DB;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.ZHSQ_SZZG_DB);
			}
			System.out.println(outputTmp);
			//dbuser.workflow
			outputTmp = "---Load dbuser.zhsq_szzg: ";
			String WORKFLOW_DB = props.getProperty("dbuser.workflow");
			if(WORKFLOW_DB!=null && !WORKFLOW_DB.trim().equals("")) {
				ConstantValue.WORKFLOW_DB = WORKFLOW_DB.trim() + ".";
				outputTmp += ConstantValue.WORKFLOW_DB;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.WORKFLOW_DB);
			}
			System.out.println(outputTmp);

			//-- SQ_USER_ID
			outputTmp = "---Load SQ_USER_ID: ";
			String SQ_USER_ID = props.getProperty("SQ_USER_ID");
			if(SQ_USER_ID!=null && !SQ_USER_ID.trim().equals("")) {
				ConstantValue.SQ_USER_ID = SQ_USER_ID.trim();
				outputTmp += ConstantValue.SQ_USER_ID;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.SQ_USER_ID);
			}
			System.out.println(outputTmp);
			//影像图是否显示轮廓
			outputTmp = "---Load IS_IMAGE_MAP_SHOW_CONTOUR: ";
			String IS_IMAGE_MAP_SHOW_CONTOUR = props.getProperty("IS_IMAGE_MAP_SHOW_CONTOUR");
			if(IS_IMAGE_MAP_SHOW_CONTOUR != null && !IS_IMAGE_MAP_SHOW_CONTOUR.trim().equals("")) {
				ConstantValue.IS_IMAGE_MAP_SHOW_CONTOUR = IS_IMAGE_MAP_SHOW_CONTOUR;
				outputTmp += ConstantValue.IS_IMAGE_MAP_SHOW_CONTOUR;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.IS_IMAGE_MAP_SHOW_CONTOUR);
			}
			System.out.println(outputTmp);
			
			//--平安建设
			outputTmp = "---Load PEACE_REPORT_URL: ";
			String PEACE_REPORT_URL = props.getProperty("PEACE_REPORT_URL");
			if(PEACE_REPORT_URL!=null && !PEACE_REPORT_URL.trim().equals("")) {
				ConstantValue.PEACE_REPORT_URL = PEACE_REPORT_URL.trim();
				outputTmp += ConstantValue.PEACE_REPORT_URL;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.PEACE_REPORT_URL);
			}
			System.out.println(outputTmp);
			//-- IS_PRODUCT
			outputTmp = "---Load IS_PRODUCT: ";
			String IS_PRODUCT = props.getProperty("dubbo.zookeeper.id");
			if(IS_PRODUCT!=null && !IS_PRODUCT.trim().equals("")) {
				ConstantValue.IS_PRODUCT = "product".equals(IS_PRODUCT.trim());
				outputTmp += ConstantValue.IS_PRODUCT;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.IS_PRODUCT);
			}
			System.out.println(outputTmp);
			//-- ACCESS_HOST
			/*outputTmp = "---Load ACCESS_HOST: ";
			String ACCESS_HOST = props.getProperty("ACCESS_HOST");
			if(ACCESS_HOST!=null && !ACCESS_HOST.trim().equals("")) {
				ConstantValue.ACCESS_HOST = ACCESS_HOST.trim();
				outputTmp += ConstantValue.ACCESS_HOST;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.ACCESS_HOST);
			}
			System.out.println(outputTmp);*/
			//-- ENCRYPTION_KEY_FOR_OTHER_PLATFORM
			outputTmp = "---Load ENCRYPTION_KEY_FOR_OTHER_PLATFORM: ";
			String ENCRYPTION_KEY_FOR_OTHER_PLATFORM = props.getProperty("ENCRYPTION_KEY_FOR_OTHER_PLATFORM");
			if(ENCRYPTION_KEY_FOR_OTHER_PLATFORM!=null && !ENCRYPTION_KEY_FOR_OTHER_PLATFORM.trim().equals("")) {
				ConstantValue.ENCRYPTION_KEY_FOR_OTHER_PLATFORM = ENCRYPTION_KEY_FOR_OTHER_PLATFORM.trim();
				outputTmp += ConstantValue.ENCRYPTION_KEY_FOR_OTHER_PLATFORM;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.ENCRYPTION_KEY_FOR_OTHER_PLATFORM);
			}
			System.out.println(outputTmp);
			//-- PLATFORM_SWITCH
			outputTmp = "---Load PLATFORM_SWITCH: ";
			String PLATFORM_SWITCH = props.getProperty("PLATFORM_SWITCH");
			if(PLATFORM_SWITCH!=null && !PLATFORM_SWITCH.trim().equals("")) {
				ConstantValue.PLATFORM_SWITCH = Integer.parseInt(PLATFORM_SWITCH.trim());
				outputTmp += ConstantValue.PLATFORM_SWITCH;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.PLATFORM_SWITCH);
			}
			System.out.println(outputTmp);
			//-- PLATFORM_DOMAIN_ROOT
			/*outputTmp = "---Load PLATFORM_DOMAIN_ROOT: ";
			String PLATFORM_DOMAIN_ROOT = props.getProperty("PLATFORM_DOMAIN_ROOT");
			if(PLATFORM_DOMAIN_ROOT!=null && !PLATFORM_DOMAIN_ROOT.trim().equals("")) {
				ConstantValue.PLATFORM_DOMAIN_ROOT = PLATFORM_DOMAIN_ROOT.trim();
				outputTmp += ConstantValue.PLATFORM_DOMAIN_ROOT;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.PLATFORM_DOMAIN_ROOT);
			}
			System.out.println(outputTmp);*/
			//-- UAM_PUBLIC_KEY
			outputTmp = "---Load UAM_PUBLIC_KEY: ";
			String UAM_PUBLIC_KEY = props.getProperty("UAM_PUBLIC_KEY");
			if(UAM_PUBLIC_KEY!=null && !UAM_PUBLIC_KEY.trim().equals("")) {
				ConstantValue.UAM_PUBLIC_KEY = UAM_PUBLIC_KEY.trim();
				outputTmp += ConstantValue.UAM_PUBLIC_KEY;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.UAM_PUBLIC_KEY);
			}
			System.out.println(outputTmp);
			//-- UAM_LOGIN_URL
			outputTmp = "---Load UAM_LOGIN_URL: ";
			String UAM_LOGIN_URL = props.getProperty("UAM_LOGIN_URL");
			if(UAM_LOGIN_URL!=null && !UAM_LOGIN_URL.trim().equals("")) {
				ConstantValue.UAM_LOGIN_URL = UAM_LOGIN_URL.trim();
				outputTmp += ConstantValue.UAM_LOGIN_URL;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.UAM_LOGIN_URL);
			}
			System.out.println(outputTmp);
			//-- RESOURSE_DOMAIN_KEY
			outputTmp = "---Load RESOURSE_DOMAIN_KEY: ";
			String RESOURSE_DOMAIN_KEY = props.getProperty("RESOURSE_DOMAIN_KEY");
			if(RESOURSE_DOMAIN_KEY!=null && !RESOURSE_DOMAIN_KEY.trim().equals("")) {
				ConstantValue.RESOURSE_DOMAIN_KEY = RESOURSE_DOMAIN_KEY.trim();
				outputTmp += ConstantValue.RESOURSE_DOMAIN_KEY;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESOURSE_DOMAIN_KEY);
			}
			System.out.println(outputTmp);
			//-- RESOURSE_SERVER_PATH
			/*outputTmp = "---Load RESOURSE_SERVER_PATH: ";
			String RESOURSE_SERVER_PATH = props.getProperty("RESOURSE_SERVER_PATH");
			if(RESOURSE_SERVER_PATH!=null && !RESOURSE_SERVER_PATH.trim().equals("")) {
				ConstantValue.RESOURSE_SERVER_PATH = RESOURSE_SERVER_PATH.trim();
				outputTmp += ConstantValue.RESOURSE_SERVER_PATH;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESOURSE_SERVER_PATH);
			}
			System.out.println(outputTmp);*/
			//-- DEFAULT_RESOURSE_SERVER_PATH
			/*outputTmp = "---Load DEFAULT_RESOURSE_SERVER_PATH: ";
			String DEFAULT_RESOURSE_SERVER_PATH = props.getProperty("DEFAULT_RESOURSE_SERVER_PATH");
			if(DEFAULT_RESOURSE_SERVER_PATH!=null && !DEFAULT_RESOURSE_SERVER_PATH.trim().equals("")) {
				ConstantValue.DEFAULT_RESOURSE_SERVER_PATH = DEFAULT_RESOURSE_SERVER_PATH.trim();
				outputTmp += ConstantValue.DEFAULT_RESOURSE_SERVER_PATH;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.DEFAULT_RESOURSE_SERVER_PATH);
			}
			System.out.println(outputTmp);*/
			//-- RESOURSE_SAVE_ROOT_PATH
			outputTmp = "---Load RESOURSE_SAVE_ROOT_PATH: ";
			String RESOURSE_SAVE_ROOT_PATH = props.getProperty("RESOURSE_SAVE_ROOT_PATH");
			if(RESOURSE_SAVE_ROOT_PATH!=null && !RESOURSE_SAVE_ROOT_PATH.trim().equals("")) {
				ConstantValue.RESOURSE_SAVE_ROOT_PATH = RESOURSE_SAVE_ROOT_PATH.trim();
				outputTmp += ConstantValue.RESOURSE_SAVE_ROOT_PATH;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESOURSE_SAVE_ROOT_PATH);
			}
			System.out.println(outputTmp);
			//-- UAM_COOKIE_TOKEN_KEY
			outputTmp = "---Load UAM_COOKIE_TOKEN_KEY: ";
			String UAM_COOKIE_TOKEN_KEY = props.getProperty("UAM_COOKIE_TOKEN_KEY");
			if(UAM_COOKIE_TOKEN_KEY!=null && !UAM_COOKIE_TOKEN_KEY.trim().equals("")) {
				ConstantValue.UAM_COOKIE_TOKEN_KEY = UAM_COOKIE_TOKEN_KEY.trim();
				outputTmp += ConstantValue.UAM_COOKIE_TOKEN_KEY;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.UAM_COOKIE_TOKEN_KEY);
			}
			System.out.println(outputTmp);
			//-- RESIDENT_DOMAIN
			/*outputTmp = "---Load RESIDENT_DOMAIN: ";
			String RESIDENT_DOMAIN = props.getProperty("RESIDENT_DOMAIN");
			if(RESIDENT_DOMAIN!=null && !RESIDENT_DOMAIN.trim().equals("")) {
				ConstantValue.RESIDENT_DOMAIN = RESIDENT_DOMAIN.trim();
				outputTmp += ConstantValue.RESIDENT_DOMAIN;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESIDENT_DOMAIN);
			}
			System.out.println(outputTmp);*/
			//-- RESIDENT_IMPEXP_DOMAIN
			/*outputTmp = "---Load RESIDENT_IMPEXP_DOMAIN: ";
			String RESIDENT_IMPEXP_DOMAIN = props.getProperty("RESIDENT_IMPEXP_DOMAIN");
			if(RESIDENT_IMPEXP_DOMAIN!=null && !RESIDENT_IMPEXP_DOMAIN.trim().equals("")) {
				ConstantValue.RESIDENT_IMPEXP_DOMAIN = RESIDENT_IMPEXP_DOMAIN.trim();
				outputTmp += ConstantValue.RESIDENT_IMPEXP_DOMAIN;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESIDENT_IMPEXP_DOMAIN);
			}
			System.out.println(outputTmp);*/
			//-- RESOURCE_DOMAIN
			/*outputTmp = "---Load RESOURCE_DOMAIN: ";
			String RESOURCE_DOMAIN = props.getProperty("RESOURCE_DOMAIN");
			if(RESOURCE_DOMAIN!=null && !RESOURCE_DOMAIN.trim().equals("")) {
				ConstantValue.RESOURCE_DOMAIN = RESOURCE_DOMAIN.trim();
				outputTmp += ConstantValue.RESOURCE_DOMAIN;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESOURCE_DOMAIN);
			}
			System.out.println(outputTmp);*/
			//-- GIS_MAP_BASE_PATH
			/*outputTmp = "---Load GIS_MAP_BASE_PATH: ";
			String GIS_MAP_BASE_PATH = props.getProperty("GIS_MAP_BASE_PATH");
			if(GIS_MAP_BASE_PATH!=null && !GIS_MAP_BASE_PATH.trim().equals("")) {
				ConstantValue.GIS_MAP_BASE_PATH = GIS_MAP_BASE_PATH.trim();
				outputTmp += ConstantValue.GIS_MAP_BASE_PATH;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.GIS_MAP_BASE_PATH);
			}*/
			/*String SEND_MSG_URL = props.getProperty("SEND_MSG_URL");
			if(SEND_MSG_URL != null && !SEND_MSG_URL.trim().equals("")) {
				ConstantValue.SEND_MSG_URL = SEND_MSG_URL.trim();
				outputTmp += ConstantValue.SEND_MSG_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SEND_MSG_URL);
			}
			System.out.println(outputTmp);*/
			//OAUTH工程部署路径
			/*outputTmp = "---Load OAUTH_URL: ";
			String OAUTH_URL = props.getProperty("OAUTH_URL");
			if(OAUTH_URL != null && !OAUTH_URL.trim().equals("")) {
				ConstantValue.OAUTH_URL = OAUTH_URL.trim();
				outputTmp += ConstantValue.OAUTH_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.OAUTH_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load SQ_ZZGRID_URL: ";
			String SQ_ZZGRID_URL = props.getProperty("SQ_ZZGRID_URL");
			if(SQ_ZZGRID_URL != null && !SQ_ZZGRID_URL.trim().equals("")) {
				ConstantValue.SQ_ZZGRID_URL = SQ_ZZGRID_URL.trim();
				outputTmp += ConstantValue.SQ_ZZGRID_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SQ_ZZGRID_URL);
			}
			System.out.println(outputTmp);*/
			/*outputTmp = "---Load SQ_LYJJ_URL: ";
			String SQ_LYJJ_URL = props.getProperty("SQ_LYJJ_URL");
			if(SQ_LYJJ_URL != null && !SQ_LYJJ_URL.trim().equals("")) {
				ConstantValue.SQ_LYJJ_URL = SQ_LYJJ_URL.trim();
				outputTmp += ConstantValue.SQ_LYJJ_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SQ_LYJJ_URL);
			}
			System.out.println(outputTmp);*/
			
			outputTmp = "---Load ANOLE_COMPONENT_URL: ";
			/*String ANOLE_COMPONENT_URL = props.getProperty("ANOLE_COMPONENT_URL");
			if(ANOLE_COMPONENT_URL != null && !ANOLE_COMPONENT_URL.trim().equals("")) {
				ConstantValue.ANOLE_COMPONENT_URL = ANOLE_COMPONENT_URL.trim();
				outputTmp += ConstantValue.ANOLE_COMPONENT_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.ANOLE_COMPONENT_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load SQ_ZZGRID_URL: ";
			String SQ_ZHSQ_EVENT_URL = props.getProperty("SQ_ZHSQ_EVENT_URL");
			if(SQ_ZHSQ_EVENT_URL != null && !SQ_ZHSQ_EVENT_URL.trim().equals("")) {
				ConstantValue.SQ_ZHSQ_EVENT_URL = SQ_ZHSQ_EVENT_URL.trim();
				outputTmp += ConstantValue.SQ_ZHSQ_EVENT_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SQ_ZHSQ_EVENT_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load WATER_MONITOR_URL: ";
			String WATER_MONITOR_URL = props.getProperty("WATER_MONITOR_URL");
			if(WATER_MONITOR_URL != null && !WATER_MONITOR_URL.trim().equals("")) {
				ConstantValue.WATER_MONITOR_URL = WATER_MONITOR_URL.trim();
				outputTmp += ConstantValue.WATER_MONITOR_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.WATER_MONITOR_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load SQ_FIREGRID_URL: ";
			String SQ_FIREGRID_URL = props.getProperty("SQ_FIREGRID_URL");
			if(SQ_FIREGRID_URL != null && !SQ_FIREGRID_URL.trim().equals("")) {
				ConstantValue.SQ_FIREGRID_URL = SQ_FIREGRID_URL.trim();
				outputTmp += ConstantValue.SQ_FIREGRID_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SQ_FIREGRID_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load ICGRID_URL: ";
			String ICGRID_URL = props.getProperty("ICGRID_URL");
			if(ICGRID_URL != null && !ICGRID_URL.trim().equals("")) {
				ConstantValue.ICGRID_URL = ICGRID_URL.trim();
				outputTmp += ConstantValue.ICGRID_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.ICGRID_URL);
			}
			System.out.println(outputTmp);*/
			
			/*outputTmp = "---Load RESOURCE_DOMAIN_URL: ";
			String RESOURCE_DOMAIN_URL = props.getProperty("RESOURCE_DOMAIN_URL");
			if(RESOURCE_DOMAIN_URL != null && !RESOURCE_DOMAIN_URL.trim().equals("")) {
				ConstantValue.RESOURCE_DOMAIN_URL = RESOURCE_DOMAIN_URL.trim();
				outputTmp += ConstantValue.RESOURCE_DOMAIN_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.RESOURCE_DOMAIN_URL);
			}
			System.out.println(outputTmp);*/
			//-- WORKFLOW_SERVER_PATH
			/*outputTmp = "---Load WORKFLOW_SERVER_PATH: ";
			String WORKFLOW_SERVER_PATH = props.getProperty("WORKFLOW_SERVER_PATH");
			if(WORKFLOW_SERVER_PATH != null && !WORKFLOW_SERVER_PATH.trim().equals("")) {
				ConstantValue.WORKFLOW_SERVER_PATH = WORKFLOW_SERVER_PATH.trim();
				outputTmp += ConstantValue.WORKFLOW_SERVER_PATH;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.WORKFLOW_SERVER_PATH);
			}
			System.out.println(outputTmp);*/
			//-- FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION 海沧 人民调解
			outputTmp = "---Load FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION: ";
			String FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION = props.getProperty("FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION");
			if(FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION != null && !FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION.trim().equals("")) {
				ConstantValue.FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION = FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION.trim();
				outputTmp += ConstantValue.FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.FLOW_FORM_TYPE_ID_PEOPLE_MEDIATION);
			}
			System.out.println(outputTmp);
			//-- FLOW_FORM_TYPE_ID_PETITION 海沧 司法信访事件
			outputTmp = "---Load FLOW_FORM_TYPE_ID_PETITION: ";
			String FLOW_FORM_TYPE_ID_PETITION = props.getProperty("FLOW_FORM_TYPE_ID_PETITION");
			if(FLOW_FORM_TYPE_ID_PETITION != null && !FLOW_FORM_TYPE_ID_PETITION.trim().equals("")) {
				ConstantValue.FLOW_FORM_TYPE_ID_PETITION = FLOW_FORM_TYPE_ID_PETITION.trim();
				outputTmp += ConstantValue.FLOW_FORM_TYPE_ID_PETITION;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.FLOW_FORM_TYPE_ID_PETITION);
			}
			System.out.println(outputTmp);
			//-- SPGIS_IP 平潭高德IP
			outputTmp = "---Load SPGIS_IP: ";
			String SPGIS_IP = props.getProperty("SPGIS_IP");
			if(SPGIS_IP != null && !SPGIS_IP.trim().equals("")) {
				ConstantValue.SPGIS_IP = SPGIS_IP.trim();
				outputTmp += ConstantValue.SPGIS_IP;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SPGIS_IP);
			}
			System.out.println(outputTmp);
			
			
			//-- FLOW_POPULATION_URL 流动接口 省外外口数据
			/*outputTmp = "---Load FLOW_POPULATION_URL: ";
			String FLOW_POPULATION_URL = props.getProperty("FLOW_POPULATION_URL");
			if(FLOW_POPULATION_URL != null && !FLOW_POPULATION_URL.trim().equals("")) {
				ConstantValue.FLOW_POPULATION_URL = FLOW_POPULATION_URL.trim();
				outputTmp += ConstantValue.FLOW_POPULATION_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.FLOW_POPULATION_URL);
			}
			System.out.println(outputTmp);*/
			
			
			//-- POPULATION_URL 人口数据库域名
			/*outputTmp = "---Load POPULATION_URL: ";
			String POPULATION_URL = props.getProperty("POPULATION_URL");
			if(POPULATION_URL != null && !POPULATION_URL.trim().equals("")) {
				ConstantValue.POPULATION_URL = POPULATION_URL.trim();
				outputTmp += ConstantValue.POPULATION_URL;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.POPULATION_URL);
			}
			System.out.println(outputTmp);*/
			String AQI_CITY_STATION_ID = props.getProperty("AQI_CITY_STATION_ID");
			if(AQI_CITY_STATION_ID != null && !AQI_CITY_STATION_ID.trim().equals("")) {
				ConstantValue.AQI_CITY_STATION_ID = AQI_CITY_STATION_ID.trim();
				outputTmp += ConstantValue.AQI_CITY_STATION_ID;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.AQI_CITY_STATION_ID);
			}
			System.out.println(outputTmp);

			outputTmp = "---Load NANCHANG_FUNC_ORG_CODE: ";
			String NANCHANG_FUNC_ORG_CODE = props.getProperty("NANCHANG_FUNC_ORG_CODE");
			if(NANCHANG_FUNC_ORG_CODE!=null && !NANCHANG_FUNC_ORG_CODE.trim().equals("")) {
				ConstantValue.NANCHANG_FUNC_ORG_CODE = NANCHANG_FUNC_ORG_CODE.trim();
				outputTmp += ConstantValue.NANCHANG_FUNC_ORG_CODE;
			} else {
				outputTmp += (" error! Use Default: " + ConstantValue.NANCHANG_FUNC_ORG_CODE);
			}
			System.out.println(outputTmp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in!=null) {
				try {
					in.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//-- 读取文字过滤器配置文件
		Properties sfProps = new Properties();
		InputStream sfIn = null;
		try {
			String outputTmp = "---Load StringFilter: ";
			sfIn = new BufferedInputStream(new FileInputStream(configFilePath+STRING_FILTER_CONFIG_FILE));
			sfProps.load(sfIn);
			Set<Entry<Object, Object>> sfPropsSet = sfProps.entrySet();
			Iterator<Entry<Object, Object>> sfPropsIt = sfPropsSet.iterator();
			while(sfPropsIt.hasNext()) {
				Entry<Object, Object> item = sfPropsIt.next();
				String key = String.valueOf(item.getKey());
				String value = String.valueOf(item.getValue());
				STRING_FILTER_MAP.put(key, value);
				System.out.println(outputTmp + key + " - " + value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sfIn!=null) {
				try {
					sfIn.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//-- 读取个性化开关配置文件
		Properties icProps = new Properties();
		InputStream icIn = null;
		try {
			icIn = new BufferedInputStream(new FileInputStream(configFilePath+INDIVIDUAL_CONFIG_FILE));
			icProps.load(icIn);
			//-- HAICANG_FUNC_ORG_CODE 海沧个性化开关
			String outputTmp = "---Load HAICANG_FUNC_ORG_CODE: ";
			String HAICANG_FUNC_ORG_CODE = icProps.getProperty("HAICANG_FUNC_ORG_CODE");
			if(HAICANG_FUNC_ORG_CODE != null && !HAICANG_FUNC_ORG_CODE.trim().equals("")) {
				ConstantValue.HAICANG_FUNC_ORG_CODE = HAICANG_FUNC_ORG_CODE.trim();
				outputTmp += ConstantValue.HAICANG_FUNC_ORG_CODE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.HAICANG_FUNC_ORG_CODE);
			}
			System.out.println(outputTmp);
			//-- SHISHI_FUNC_ORG_CODE 海沧个性化开关
			outputTmp = "---Load SHISHI_FUNC_ORG_CODE: ";
			String SHISHI_FUNC_ORG_CODE = icProps.getProperty("SHISHI_FUNC_ORG_CODE");
			if(SHISHI_FUNC_ORG_CODE != null && !SHISHI_FUNC_ORG_CODE.trim().equals("")) {
				ConstantValue.SHISHI_FUNC_ORG_CODE = SHISHI_FUNC_ORG_CODE.trim();
				outputTmp += ConstantValue.SHISHI_FUNC_ORG_CODE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SHISHI_FUNC_ORG_CODE);
			}
			System.out.println(outputTmp);
			//-- SHISHI_INVOLVED_MONEY_ORG_CODE 涉及金额字段个性化开关
			outputTmp = "---Load SHISHI_INVOLVED_MONEY_ORG_CODE: ";
			String SHISHI_INVOLVED_MONEY_ORG_CODE = icProps.getProperty("SHISHI_INVOLVED_MONEY_ORG_CODE");
			if(SHISHI_INVOLVED_MONEY_ORG_CODE != null && !SHISHI_INVOLVED_MONEY_ORG_CODE.trim().equals("")) {
				ConstantValue.SHISHI_INVOLVED_MONEY_ORG_CODE = SHISHI_INVOLVED_MONEY_ORG_CODE.trim();
				outputTmp += ConstantValue.SHISHI_INVOLVED_MONEY_ORG_CODE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.SHISHI_INVOLVED_MONEY_ORG_CODE);
			}
			System.out.println(outputTmp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(icIn!=null) {
				try {
					icIn.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//-- 读取global.properties配置文件
		Properties globalProps = new Properties();
		InputStream globalIn = null;
		try {
			globalIn = new BufferedInputStream(new FileInputStream(configFilePath+GLOBAL_PROPERTIES));
			globalProps.load(globalIn);
			//-- event.default.type 事件默认类型
			String outputTmp = "---Load event.default.type: ";
			String EVENT_DEFAULT_TYPE = globalProps.getProperty("event.default.type");
			if(EVENT_DEFAULT_TYPE != null && !EVENT_DEFAULT_TYPE.trim().equals("")) {
				ConstantValue.EVENT_DEFAULT_TYPE = EVENT_DEFAULT_TYPE.trim();
				outputTmp += ConstantValue.EVENT_DEFAULT_TYPE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.EVENT_DEFAULT_TYPE);
			}
			System.out.println(outputTmp);
			//-- enable_btn_authority 是否开启按钮权限
			outputTmp = "---Load enable_btn_authority: ";
			String enable_btn_authority = globalProps.getProperty("enable_btn_authority");
			if(enable_btn_authority != null && !enable_btn_authority.trim().equals("")) {
				ConstantValue.ENABLE_BTN_AUTHORITY = Boolean.valueOf(enable_btn_authority.trim());
				outputTmp += ConstantValue.ENABLE_BTN_AUTHORITY;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.ENABLE_BTN_AUTHORITY);
			}
			System.out.println(outputTmp);
			//-- event.noShow 屏蔽越级上报功能
			outputTmp = "---Load event.noShow: ";
			String EVENT_NO_SHOW = globalProps.getProperty("event.noShow");
			if(EVENT_NO_SHOW != null && !EVENT_NO_SHOW.trim().equals("")) {
				ConstantValue.EVENT_NO_SHOW = EVENT_NO_SHOW.trim();
				outputTmp += ConstantValue.EVENT_NO_SHOW;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.EVENT_NO_SHOW);
			}
			System.out.println(outputTmp);
			//-- event.default.database 事件表所属用户
			outputTmp = "---Load event.default.database: ";
			String EVENT_DEFAULT_DATABASE = globalProps.getProperty("event.default.database");
			if(EVENT_DEFAULT_DATABASE != null && !EVENT_DEFAULT_DATABASE.trim().equals("")) {
				ConstantValue.EVENT_DEFAULT_DATABASE = EVENT_DEFAULT_DATABASE.trim();
				outputTmp += ConstantValue.EVENT_DEFAULT_DATABASE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.EVENT_DEFAULT_DATABASE);
			}
			System.out.println(outputTmp);
			
			//-- IS_SEND_MSG_SHOW 是否跳转短信发送界面
			outputTmp = "---Load IS_SEND_MSG_SHOW: ";
			String IS_SEND_MSG_SHOW = globalProps.getProperty("IS_SEND_MSG_SHOW");
			if(IS_SEND_MSG_SHOW != null && !IS_SEND_MSG_SHOW.trim().equals("")) {
				ConstantValue.IS_SEND_MSG_SHOW = Boolean.valueOf(IS_SEND_MSG_SHOW.trim());
				outputTmp += ConstantValue.IS_SEND_MSG_SHOW;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.IS_SEND_MSG_SHOW);
			}
			System.out.println(outputTmp);

			//-- LAW_ENFORCE_OFFIC_FUNC_ORG_CODE 综合执法部门编码
			outputTmp = "---Load LAW_ENFORCE_OFFIC_FUNC_ORG_CODE: ";
			String LAW_ENFORCE_OFFIC_FUNC_ORG_CODE = globalProps.getProperty("LAW_ENFORCE_OFFIC_FUNC_ORG_CODE");
			if(!StringUtils.isEmpty(LAW_ENFORCE_OFFIC_FUNC_ORG_CODE)) {
				ConstantValue.LAW_ENFORCE_OFFIC_FUNC_ORG_CODE = LAW_ENFORCE_OFFIC_FUNC_ORG_CODE.trim();
				outputTmp += ConstantValue.LAW_ENFORCE_OFFIC_FUNC_ORG_CODE;
			}else {
				outputTmp += (" error! Use Default: " + ConstantValue.LAW_ENFORCE_OFFIC_FUNC_ORG_CODE);
			}
			System.out.println(outputTmp);
            //-- PLATFORM_CODE 智慧云眼平台编码
            outputTmp = "---Load PLATFORM_CODE: ";
            String PLATFORM_CODE = globalProps.getProperty("PLATFORM_CODE");
            if(!StringUtils.isEmpty(PLATFORM_CODE)) {
                ConstantValue.PLATFORM_CODE = PLATFORM_CODE.trim();
                outputTmp += ConstantValue.PLATFORM_CODE;
            }else {
                outputTmp += (" error! Use Default: " + ConstantValue.PLATFORM_CODE);
            }
            System.out.println(outputTmp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(globalIn!=null) {
				try {
					globalIn.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

        //-- 读取portal-config.properties配置文件
        Properties portalProps = new Properties();
        InputStream portalIn = null;
        try {
            portalIn = new BufferedInputStream(new FileInputStream(configFilePath+PORTAL_CONFIG_PROPERTIES));
            portalProps.load(portalIn);
            //三书一函专业编码
            String outputTmp = "---Load SSYH_PROFESSION_CODE: ";
            String SSYH_PROFESSION_CODE = portalProps.getProperty("SSYH_PROFESSION_CODE");
            if(SSYH_PROFESSION_CODE!=null && !SSYH_PROFESSION_CODE.trim().equals("")) {
                ConstantValue.SSYH_PROFESSION_CODE = SSYH_PROFESSION_CODE.trim();
                outputTmp += ConstantValue.SSYH_PROFESSION_CODE;
            } else {
                outputTmp += (" error! Use Default: " + ConstantValue.SSYH_PROFESSION_CODE);
            }
            System.out.println(outputTmp);
            //三书一函专业名称
            outputTmp = "---Load SSYH_PROFESSION_NAME: ";
            String SSYH_PROFESSION_NAME = portalProps.getProperty("SSYH_PROFESSION_NAME");
            if(SSYH_PROFESSION_NAME!=null && !SSYH_PROFESSION_NAME.trim().equals("")) {
                ConstantValue.SSYH_PROFESSION_NAME = SSYH_PROFESSION_NAME.trim();
                outputTmp += ConstantValue.SSYH_PROFESSION_NAME;
            } else {
                outputTmp += (" error! Use Default: " + ConstantValue.SSYH_PROFESSION_NAME);
            }
            System.out.println(outputTmp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(portalIn!=null) {
                try {
                    portalIn.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}

	/**
	 * 获取资源服务根目录
	 * @return
	 */
	/*public static String getRootResourseServerPath() {
		int start = ConstantValue.RESOURSE_SERVER_PATH.indexOf(ConstantValue.RESOURSE_DOMAIN_KEY);
		String rootPath = ConstantValue.RESOURSE_SERVER_PATH;
		if(start>0 && rootPath.length()>7)
			rootPath = rootPath.substring(0, rootPath.length()-7);
		return rootPath;
	}*/

	/**
	 * 获取资源服务保存根目录
	 * @return
	 */
	public static String getRootResourseSavePath() {
		int start = ConstantValue.RESOURSE_SAVE_ROOT_PATH.indexOf(ConstantValue.RESOURSE_DOMAIN_KEY);
		String rootPath = ConstantValue.RESOURSE_SAVE_ROOT_PATH;
		if(start>0 && rootPath.length()>7)
			rootPath = rootPath.substring(0, rootPath.length()-7);
		return rootPath;
	}

	//-- 新版usercookie改造
	public static final String USER_COOKIE_KEY = "userCookieKey";
	public static final String REDIRECT_DOMAIN_KEY = "redirectDomainKey";
	public static final String USER_COOKIE_UAM_DOMAIN_KEY = "uam_domain_key";   //uam将域名写入memcahce时所用的key
	
	//-- 用户信息在session中的key
	public static final String USER_IN_SESSION = "USER_IN_SESSION"; //--本系统用户信息
	public static final String USER_COOKIE_IN_SESSION = "USER_COOKIE_IN_SESSION"; //--统一认证COOKIE用户信息
	public static final String UUMEMKEY_IN_SESSION = "UUMEMKEY_IN_SESSION";
	public static final String MODULELIST_IN_SESSION = "MODULELIST_IN_SESSION";
	//-- 子级用户信息在session中的key
	public static final String SUBUSERLIST_IN_SESSION = "SUBUSERLIST_IN_SESSION";
	//-- 网格列表在session中的key
	public static final String KEY_GRIDLAYERINFO_LIST = "KEY_GRIDLAYERINFO_LIST";
	
	//按钮权限相关----begin
	//按钮权限(旧)
	public static final String SYSTEM_PRIVILEGE_MAP = "system_privilege_map";
	//按钮权限(新)——菜单id
	public static final String SYSTEM_PRIVILEGE_ACTION = "system_privilege_action";
	//按钮权限——权限编码
	public static final String SYSTEM_PRIVILEGE_CODE = "system_privilege_code";
	//按钮权限——角色key
	public static final String SYSTEM_ROLE = "system_role";
	//按钮权限——系统管理员key
	public static final String SYSTEM_ADMIN = "system_admin";
	public static final String USER_TYPE_REGIST = "002";	//后台注册人员的用户类型
	public static final String USER_TYPE_SUPER = "003";		//超级管理员的用户类型
	public static final String USER_TYPE_DEVELOPER = "004";	//开发人员的用户类型
	public static final String USER_TYPE_ADMIN = "009";		//管理人员的用户类型
	//按钮权限相关----end
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 巡防操作类型 0：保存
	 */
	public static final String PATROL_OPERATE_TYPE_SAVE = "0";
	/**
	 * 巡防操作类型 1：上报
	 */
	public static final String PATROL_OPERATE_TYPE_REPORT = "1";

	/**
	 * 事件关联业务类型：巡防
	 */
	public static final String BIZ_TYPE_EVENT_RELA_PATROL = "PATROL";
	/**
	 * 事件关联业务类型：矛盾纠纷
	 */
	public static final String BIZ_TYPE_EVENT_RELA_DISPUTE = "DISPUTE";
	/**
	 * 事件关联业务类型：重口走访
	 */
	public static final String BIZ_TYPE_EVENT_RELA_KEY_PERSON_VISIT = "KEY_PERSON_VISIT";
	/**
	 * 事件关联业务类型：消防安全
	 */
	public static final String BIZ_TYPE_EVENT_RELA_FIRE_FIGHT = "FIRE_FIGHT";
	/**
	 * 事件关联业务类型：车辆违章
	 */
	public static final String BIZ_TYPE_EVENT_RELA_CARS_BREAK_RULES = "CARS_BREAK_RULES";
	/**
	 * 事件关联业务类型：出租户管理
	 */
	public static final String BIZ_TYPE_EVENT_RELA_RENT_HOUSEHOLDS = "RENT_HOUSEHOLDS";
	/**
	 * 事件关联业务类型：施工环境保障
	 */
	public static final String BIZ_TYPE_EVENT_RELA_CONSTRUCTION_SAFEGUARD = "CONSTRUCTION_SAFEGUARD";
	
	//--文字过滤器映射
	public static final Map<String, String> STRING_FILTER_MAP = new HashMap<String, String>();

	//--表名
	public static final String TABLE_ORG = "t_dc_org_entity_info";
	public static final String TABLE_SOCIAL_ORG = "t_dc_org_social_info";

	//--消防网格图片保存路径
	public static final String EVENT_ANNEX_RESOURSE_FLODER = "eventAnnex";
	
	//--小城镇建设图片保存路径
	public static final String TOWN_ANNEX_RESOURSE_FLODER = "townAnnex";
	
	public static final String TASK_ANNEX_RESOURSE_FLODER  ="TASK";

    //居民数据导入失败的记录文件
	public static final String RESIDENT_INFO_IMPORT_FAILURE_PATH="resident/import-failure/";

	//-- 地图类型
	public static final int MAPTYPE_IMAGE = 1;

	//-- 状态
	public static final String STATUS_DEFAULT = "001";
	public static final String STATUS_DEFAULT_DELETE = "003";


	/**********事件*************/
	//--事件 状态
	/** 状态：已受理 */
	public static final String EVENT_STATUS_RECEIVED = "00";
	/** 状态：已上报 */
	public static final String EVENT_STATUS_REPORT = "01";
	/** 状态：已分流 */
	public static final String EVENT_STATUS_DISTRIBUTE = "02";
	/** 状态：已结案 */
	public static final String EVENT_STATUS_ARCHIVE = "03";
	/** 状态：已结束 */
	public static final String EVENT_STATUS_END = "04";
	/** 状态：挂起 */
	public static final String EVENT_STATUS_HAND_ON = "05";
	/** 状态：已删除，并回退采集人 */
	public static final String STATUS_DEL_AND_RETURN_BACK = "06";
	/** 状态：草稿 未启动流程 */
	public static final String EVENT_STATUS_DRAFT = "99";
	
	/** 超时状态：超时待办 */
	public static final String EVENT_TODO_HANDLE_STATUS = "01";
	/** 超时状态：超时已办 */
	public static final String EVENT_DONE_HANDLE_STATUS = "02";

	// -- 紧急程度
	/** 一般 */
	public static final String URGENCY_DEGREE_1 = "01";
	/** 急(弃用) */
	public static final String URGENCY_DEGREE_2 = "03";
	/** 紧急 */
	public static final String URGENCY_DEGREE_3 = "02";
	/** 一般程度 推后30天 */
	public static final int URGENCY_DEGREE_1_HANDLE = 30;
	/** 急 推后三天 */
	public static final int URGENCY_DEGREE_2_HANDLE = 3;
	/** 紧急 推后1天 */
	public static final int URGENCY_DEGREE_3_HANDLE = 1;

	//任务清单
	public static final String TASK_CODE_COLLECT = "COLLECT";

	public static final String TASK_CODE_PROC = "PROC";

	public static final String TASK_CODE_CALLCENTER = "CALLCENTER";

	public static final String TASK_NAME_COLLECT = "采集";

	public static final String TASK_NAME_PROC = "处理";

	public static final String TASK_NAME_CALLCENTER = "呼叫中心接收任务单";

	public static final String TASK_INFO_STATUS_BEGIN = "00";//已受理

	public static final String TASK_INFO_STATUS_END = "01";//已结束
	
	public static final String TASK_INFO_STATUS_DELETE = "06";//已删除

	//绩效考核管理
	public static final String MONTHASSESSMENT="01";//考核类型:月考核
	//网络运行监控
	public static final String SEARCH_WORK="0201";//项目代码；如0201表示综治出租户巡查
	public static final String SAFETY_INSPECTION="0202";//项目代码；如0202表示综治安全检查
	public static final String KEY_POPULATION="0203";//项目代码；如0203表示综治重口走访
	public static final String EVENT_ZS_TJ="9801";//项目代码 :事件添加总数
	public static final String EVENT_ZS_BL="9802";//项目代码 :事件添加代办
	public static final String EVENT_ZS_GQ="9803";//项目代码 :事件添加过期



	/**
	 * 采集途径：手机上报
	 */
	public static final String COLLECT_WAY_PHONE = "01";
	/**
	 * 采集途径：PC录入
	 */
	public static final String COLLECT_WAY_PC = "02";//
	/**
	 * 采集途径：呼叫中心
	 */
	public static final String COLLECT_WAY_CALLCENTER = "03";
	/**
	 * 采集途径：网站群
	 */
	public static final String COLLECT_WAY_WEBSITE = "04";
	/**
	 * 采集途径：微信
	 */
	public static final String COLLECT_WAY_WECHAT = "05";

	/**********事件*************/
	//--综治相关-------------------------------



	/**
	 * 相关表名
	 */
	//-- 居民表
	public static final String TABLE_CI_RS = "T_DC_CI_RS_TOP"; // 居民表


	public static final String TABLE_DICTIONARY = "t_dc_data_dictionary_info"; // 数据字典表
	//-- 风险评估登记表
	public static final String TABLE_RISK = "t_zz_risk_assessment";
	//-- 矛盾纠纷
	public static final String TABLE_DISPUTE = "t_zz_contradic_dispute";
	//-- 重点人群
	public static final String TABLE_IMPORTCROWD = "t_zz_import_crowd"; // 重点人群

	public static final String TABLE_UNITEDOBJECT = "T_DC_CI_RS_UNITED_OBJECT"; // 统战对象

	public static final String TABLE_IMPORTVISTOR = "t_zz_import_vistor"; // 重点人群走访记录
	//-- 黑点整治
	public static final String TABLE_BLACK = "t_zz_govern_black";
	//-- 重点企业
	public static final String TABLE_IMPORTCOM = "t_zz_import_company";
	//-- 帮扶对象
	public static final String TABLE_GOVAID = "t_zz_support_object"; // 帮扶对象
	public static final String TABLE_GOVAIDVISTOR = "t_zz_support_vistor"; // 帮扶对象走访记录
	//-- 犯罪记录
	public static final String TABLE_CIRS_CRIMINAL = "t_dc_ci_rs_management"; // 犯罪记录
	//-- 车辆信息
	public static final String TABLE_CIRS_CAR = "t_dc_ci_rs_car";
	//-- 房屋出租
	public static final String TABLE_CIRS_LANDLORD = "t_dc_ci_rs_landlord";
	//-- 平安联防信息管理
	public static final String TABLE_CIRS_COMMUNITY_SECURITY= "t_dc_ci_community_security";
	//-- 平安联防接警管理
	public static final String TABLE_CIRS_COMMUNITY_SECURITY_LOG = "t_dc_ci_community_security_log";
	//-- 网格人员配置表
	public static final String TABLE_CI_STAFF_CONTACT = "t_dc_ci_staff_contact";
	//-- 重点单位
	public static final String TABLE_IMPORT_UNIT = "t_zz_import_unit";
	//-- 租赁房
	public static final String TABLE_RENTING_ROOM = "t_zz_renting_room";
	//-- 基层力量
	public static final String TABLE_GRASS_ROOTS = "t_zz_grass_roots";
	//-- 矛盾纠纷调解
	public static final String TABLE_DISPUTE_MEDIATION = "t_zz_dispute_mediation";
	//-- 矛盾纠纷调解结果表
	public static final String TABLE_DISPUTE_MEDIATION_RES = "t_zz_dispute_mediation_res";
	//-- 消防火灾隐患排查
	public static final String TABLE_FIRE_CONTROL_RECORD = "t_zz_fire_control_record";
	//-- 安全隐患排查
	public static final String TABLE_SAFETY_RISK_INFO = "t_zz_safety_risk_info";
	//-- 两新组织表
	public static final String TABLE_TWO_NEW_ORG = "t_zz_two_new_org";
	//-- 东孚镇指挥调度事件
	public static final String TABLE_592_DISPATCH_EVENT = "t_zz_592_dispatch_event";
	//-- 月季度考评表
	public static final String TABLE_STAT_USER_EVALUATE = "t_dc_stat_user_evaluate";
	// -- 综治.网格管理人员安排表
	public static final String TABLE_GRID_ADMIN = "t_dc_grid_admin";
	// -- 综治.其他重点人员
	public static final String TABLE_KEY_ACCESS = "t_zz_key_access";
	//--综治.吸毒人员信息
	public static final String TABLE_DRUG_RECORD = "t_zz_drug_record";
	// -- 综治.刑释解教信息
	public static final String TABLE_RELEASED_RECORD = "t_zz_released_record";
	//-- 综治.社区矫正信息
 	public static final String TABLE_CORRECTIONAL_RECORD = "t_zz_correctional_record";
 	//-- 综治.邪教人员信息
 	public static final String TABLE_CULT_RECORD = "t_zz_cult_record";
 	//-- 综治.摩托车工信息
 	public static final String TABLE_MOTO_RECORD = "t_zz_moto_record";
 	//-- 综治.重点上访人员信息
 	public static final String TABLE_PETITION_RECORD = "t_zz_petition_record";
	//-- 综治.危险品从业信息
	public static final String TABLE_DANGEROUS_GOODS_RECORD = "t_zz_dangerous_goods_record";
	// -- 综治.重性精神病症记录
	public static final String TABLE_MENTAL_ILLNESS_RECORD = "t_zz_mental_illness_record";
   
    //--综治.重点人员走访
    public static final String TABLE_VISIT_RECORD = "t_zz_visit_record";
 	//-- 综治.市政设施.道路管理
 	public static final String TABLE_ROAD_MANAGEMENT = "t_zy_road_management";
	// --居民
    public static final String TABLE_DICTIONARY_CI_RS = "t_dc_ci_rs";
    //--证件表
    public static final String TABLE_DICTIONARY_PARTY_CENTIFICATION = "t_dc_party_centification";
    
    //养殖
    public static final String TABLE_AQUACULTURE = "t_zz_aquaculture";
    //农机
    public static final String TABLE_AGRI_MACHINE = "t_zz_agri_machine";
    
 // -- 楼房信息表
    public static final String TABLE_AREA_BUILDING_INFO = "t_dc_area_building_info";
    
    // -- 房屋管理信息
    public static final String TABLE_AREA_ROOM_INFO = "t_dc_area_room_info";
	// -- 出租屋表
    public static final String TABLE_AREA_ROOM_RENT = "t_dc_area_room_rent";
    // -- 综治.事件管理.外平台采集事件
    public static final String TABLE_EVENT_DISPOSAL = "t_sj_event_disposal";
    //-- 重点场所管理.法人单位基础信息
   	public static final String TABLE_COR_BASE_INFO = "t_zz_cor_base_info";
   	//-- 学校
   	public static final String TABLE_CAMPUS = "t_zz_campus";
	//-- 学校类型
   	public static final String COLUMN_CAMPUS_TYPE = "campus_type";
   	
    public static final String TABLE_RESEARCH_AND_DEVELOP = "t_zz_research_and_develop";
    public static final String TABLE_TEST_CASE = "t_zz_test_case";
	public static final String TABLE_TEST_CASE_STEP = "t_zz_test_case_step";
	public static final String TABLE_AUDIT_RECORD = "t_zz_audit_record";
	public static final String TABLE_AUDIT_RECORD_INFO_LOG = "t_zz_audit_record_info_log";
    //-- 门店
   	public static final String TABLE_PLA_STORE = "t_zz_pla_store";
   	//-- 违法建设
   	public static final String TABLE_ILLEGAL_CONSTRUCTION = "t_zz_ct_illegal_construction";
    //--党建工作.党员服务
    public static final String T_DJ_SERVICE_RECORD = "t_dj_service_record";
    public static final String T_DJ_HANDLING_PROCESS = "t_dj_handling_process";
    //纪检组织
    public static final String T_DC_CI_INSPECTOR = "t_dc_ci_inspector";
    //-- 企业(法人)员工基础信息
   	public static final String TABLE_CORP_CIRSTOP = "t_zz_corp_ci_rs_top";
    //-- 土地地籍信息表
  	public static final String TABLE_LAND_CADASTRAL_INFO = "t_dc_land_cadastral_info";
    //-- 护路护线表
  	public static final String TABLE_CARE_ROAD = "t_zz_care_road";
   	//参与人个人信息
   	public static final String TABLE_PARTY_INDIVIDUAL = "t_dc_party_individual";
   	//参与人个人信息.性别
   	public static final String COLUMN_PARTY_INDIVIDUAL_GENDER = "gender";
   	// -- 综治.小城镇建设.建设项目管理
    public static final String TABLE_CONSTRUCTION_PROJECT = "t_dc_construction_project";
    // -- 综治.小城镇建设.项目进度管理  
    public static final String TABLE_PROJECT_PROGRESS = "t_dc_project_progress";
    
   	public static final String T_TASK_DISPATCH = "t_task_dispatch";
   	
   	// -- 农业管理.耕地信息管理
    public static final String TABLE_ARABLE_LAND = "t_zz_arable_land";
    
   	
	//-- 人民调解
	public static final String TABLE_PEOPLE_MEDIATION = "t_zz_people_mediation";
   	//人民调解.纠纷类别  
   	public static final String COLUMN_PEOPLE_MEDIATION_DISPUTE_TYPE = "dispute_type";
	//人民调解.调解级别 
   	public static final String COLUMN_PEOPLE_MEDIATION_MEDIATE_LEVEL = "mediate_level";
	//人民调解.协议类型
	public static final String COLUMN_PEOPLE_MEDIATION_AGREE_TYPE = "agree_type";
   	
	/*************海康WebService begin*****************/
	public static  int HK_TIME_SECOND =30;
	public static  int START =0;
	public static  int LIMIT =50;
	/**卡口**/
	public static  String HK_ThirdBayonetService ="http://172.117.242.3:5300/services/ThirdBayonetService";
	public static  String HK_userName ="zhhgt";
	public static  String HK_passwd ="hgt12345";
	/**人脸**/
	public static  String HK_HFSNetService ="http://172.117.253.194:8000/WebService";
	
	/*************海康WebService end*****************/
	
	/**
	 * 数据字典中登记的信息
	 */
    //--纪检组织.职位
	public static final String COLUMN_INSPECTOR_IN_DUTY = "in_duty";
    //--党建工作.服务类型
	public static final String COLUMN_PARTY_SERV_TYPE = "type_";
    //--党建工作.性别
	public static final String COLUMN_PARTY_SERV_SEX = "sex";
    //--党建工作.状态
	public static final String COLUMN_PARTY_SERV_STATUS = "status";
    //--党建工作.操作
	public static final String COLUMN_PARTY_SERV_RECORD_OPERATE_TYPE = "operate_type";
	//-- 风险评估
	public static final String COLUMN_RISK_PROJECTLEVEL = "project_level";
	public static final String COLUMN_RISK_RISKLEVEL = "risk_level";
	public static final String COLUMN_RISK_PROJECTRESULT = "project_result";
	//-- 矛盾纠纷
	public static final String COLUMN_DISPUTE_DISPUTELEVEL = "dispute_level";
	public static final String COLUMN_DISPUTE_DISPUTEKIND = "dispute_kind";
	//-- 黑点
	public static final String COLUMN_BLACK_BLACKLEVEL = "black_level";
	public static final String COLUMN_BLACK_BLACKKIND = "black_kind";
	public static final String COLUMN_BLACK_EVENTTYPE = "event_type";
	//-- 重点人群
	public static final String COLUMN_IMPORT_CROWDLEVEL = "level";
	public static final String COLUMN_IMPORT_CROWDKIND = "kind";
	//-- 帮扶对象
	public static final String COLUMN_AID_OBJECTLEVEL = "level";
	public static final String COLUMN_AID_OBJECTKIND = "kind";
	//-- 租赁房
	public static final String COLUMN_RENT_LESSEENATURE = "lessee_nature";
	//-- 重点单位
	public static final String COLUMN_IMPORT_UNITTYPE = "type";
	//-- 矛盾纠纷调解纠纷类型（一级）
	public static final String COLUMN_DISPUTE_MEDIATION_TYPE1 = "dispute_type_1";
	//-- 安全隐患排查项目类型
	public static final String COLUMN_SAFETY_RISK_PROJECT = "project";
	//-- 两新组织单位性质
	public static final String COLUMN_TWO_ORG_ENTERPRISE_NATURE = "enterprise_nature";
	//-- 东孚镇指挥调度事件 事件级别
	public static final String COLUMN_592_DISPATCH_EVENT_LEVEL = "event_level";
	//-- 月季度考类型字段
	public static final String COLUMN_STAT_USER_EVALUATE_TYPE = "eva_type";
	//-- 消防火灾隐患排查-单位类型
	public static final String COLUMN_FIRE_CONTROL_UNIT_TYPE = "unit_type";
	//-- 消防火灾隐患排查-证件类型
	public static final String COLUMN_FIRE_CONTROL_CERT_TYPE = "cert_type";
	//-- 安全隐患排查-单位类型
	public static final String COLUMN_SAFETY_RISK_UNIT_TYPE = "unit_type";
	//-- 安全隐患排查-证件类型
	public static final String COLUMN_SAFETY_RISK_CERT_TYPE = "cert_type";
	//-- 矛盾纠纷调解-协议类型
	public static final String COLUMN_DISPUTE_MEDIATION_RES_AGREE_TYPE = "agree_type";
	//-- 综治.危险品从业信息--从业类别
	public static final String COLUMN_DANGEROUS_GOODS_WORK_TYPE = "work_type";
	//-- 综治.危险品从业信息--从业状态
	public static final String COLUMN_DANGEROUS_GOODS_WORK_STATUS = "work_status";
	//--综治.刑释解教信息--刑释解教类型
	public static final String COLUMN_RELEASED_RECORD_TEACH_TYPE = "teach_type";
	//--综治.刑释解教信息--原职业
	public static final String COLUMN_RELEASED_RECORD_ORIGINAL_OCCUPATIOR = "original_occupatior";
	//--综治.刑释解教信息--家庭经济状况
	public static final String COLUMN_RELEASED_RECORD_ECONOMIC_STATUS = "economic_status";
	//--综治.刑释解教信息--现职业
	public static final String COLUMN_RELEASED_RECORD_CUR_OCCUPATIOR = "cur_occupatior";
	// --综治.刑释解教信息--帮教类别
	public static final String COLUMN_RELEASED_RECORD_ADMONISH_TYPE = "admonish_type";
	//--网格管理人员安排表--职务
	public static final String COLUMN_GRID_ADMIN_DUTY= "duty";
	//--综治.重性精神病症记录--危险程度
	public static final String COLUMN_MENTAL_ILLNESS_RECORD_RISK_DEGREE = "risk_degree";
	//--综治.重性精神病症记录--发病频度
	public static final String COLUMN_MENTAL_ILLNESS_RECORD_FREQUENCY = "frequency";
	//--综治.重性精神病症记录--家庭经济状况
	public static final String COLUMN_MENTAL_ILLNESS_RECORD_ECONOMIC_STATUS = "economic_status";
	//--综治.社区矫正信息--矫正类型
	public static final String COLUMN_CORRECTIONAL_CORRECT_TYPE = "correct_type";
	//--综治.社区矫正信息--原职务
	public static final String COLUMN_CORRECTIONAL_ORIGINAL_OCCUPATIOR = "original_occupatior";
	//--综治.社区矫正信息--现职务
	public static final String COLUMN_CORRECTIONAL_CUR_OCCUPATIOR = "cur_occupatior";
	//--综治.社区矫正信息--管理级别
	public static final String COLUMN_CORRECTIONAL_MANAGEMENT_LEVEL = "management_level";
	//综治.其他重点人员 --人员类型
	public static final String COLUMN_KEY_ACCESS_CATALOG = "catalog_";
	//综治。重点人员走访---受访人员类型
	public static final String COLUMN_VISIT_RECORD_VISIT_TYPE = "visited_type";
	//综治。重点人员走访--走访形式
	public static final String COLUMN_VISIT_RECORD_VISIT_FORM ="visit_form";
	//综治。重点人员走访--走访效果
	public static final String COLUMN_VISIT_RECORD_VISIT_EFFECT="visit_effect";
	//综治。重点人员走访--近期动态
	public static final String COLUMN_VISIT_RECORD_RECENT_STATE = "recent_state";
	//--综治.市政设施.道路管理--道路管理级别
	public static final String COLUMN_ROAD_MANAGEMENT_LEVEL = "road_level";
	//--综治.市政设施.道路管理--道路类别
	public static final String COLUMN_ROAD_MANAGEMENT_TYPE = "road_type";
	//--楼房信息表--使用用途
	public static final String COLUMN_AREA_BUILDING_INFO_USE_NATURE = "use_nature";
	//人员-类型
	public static final String COLUMN_CI_RS_TYPE = "type";
	public static final String COLUMN_PARTY_INDIVIDUAL_MARITAL_STATUS= "marital_status";
	//--楼房信息表--状态
	public static final String COLUMN_AREA_BUILDING_INFO_BUILDING_STATUS = "building_status";
	//--出租屋--住户类型
	public static final String COLUMN_AREA_ROOM_RENT_LIVE_TYPE = "live_type";
	//--房屋管理信息表--单元用途
	public static final String COLUMN_AREA_ROOM_INFO_UNIT_USES = "unit_uses";
    //研发管理
    public static final String COLUMN_RESEARCH_AND_DEVELOP_PRIORITY_TYPE = "priority_type";
    public static final String COLUMN_RESEARCH_AND_DEVELOP_IMPORTANCE = "importance";
    public static final String COLUMN_RESEARCH_AND_DEVELOP_TASK_TYPE = "task_type";
    public static final String COLUMN_RESEARCH_AND_DEVELOP_SEGMENT_TYPE = "segment_type";
    public static final String COLUMN_RESEARCH_AND_DEVELOP_SPECIALTY_TYPE = "specialty_type";

    //小城镇建设
    //--综治.建设项目管理
    public static final String COLUMN_CONSTRUCTION_PROJECT_NATURE = "project_nature";
    public static final String COLUMN_CONSTRUCTION_PROJECT_TYPE = "project_type";
    public static final String COLUMN_CONSTRUCTION_PROJECT_KEY = "is_key_project";
    
    public static final String COLUMN_PROJECT_PROGRESS_CURRENT_STATUS = "current_status";

	//-- 状态
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_DELETE = 3;

	public static final String DC_COLUMN_VALUE_KEY = "COLUMN_VALUE";
	public static final String DC_COLUMN_VALUE_REMARK_KEY = "COLUMN_VALUE_REMARK";

	/**********重点人员走访安排表start**************/
	/** 刑释解教人员 */
	public static final String OBJ_TYPE_RELEASE = "01";
	/** 社区矫正人员 */
	public static final String OBJ_TYPE_CORRECTION = "02";
	/** 闲散青少年 */
	public static final String OBJ_TYPE_IDLE = "03";
	/** 重性精神病人员 */
	public static final String OBJ_TYPE_MENTAL_ILLNESS = "04";
	/** 吸毒人员 */
	public static final String OBJ_TYPE_DRUG = "05";
	/** 其他重点人员 */
	public static final String OBJ_TYPE_KEY = "12";
	/** 信访人员 */
	public static final String OBJ_TYPE_PETITION = "06";
	/** 需要救助人员 */
	public static final String OBJ_TYPE_AID = "07";
	/** 危险品从业人员 */
	public static final String OBJ_TYPE_DANAGER_GOODS = "08";
	/** 居家养老人员 */
	public static final String OBJ_TYPE_OLD = "09";
	/** 邪教人员 */
	public static final String OBJ_TYPE_CULT = "10";
	/** 摩托车工 */
	public static final String OBJ_TYPE_MOTO = "11";
	/** 其他人员  */
	public static final String OBJ_TYPE_OTHER = "99";

	/****************重点人员走访安排表end***************/

	/****************重点人员走访类型begin***************/
	//吸毒人员
	public static final String VISIT_TYPE_DRUG = "001";
	//刑释解教人员
	public static final String VISIT_TYPE_RELEASE = "002";
	//社区矫正人员
	public static final String VISIT_TYPE_CORRECTION = "003";
	//邪教人员
	public static final String VISIT_TYPE_CULT = "004";
	//信访人员
	public static final String VISIT_TYPE_PETITION = "005";
	//危险品从业人员
	public static final String VISIT_TYPE_DANAGER_GOODS = "006";
	//重性精神病人员
	public static final String VISIT_TYPE_MENTAL_ILLNESS = "007";
	//摩托车工
	public static final String VISIT_TYPE_MOTO = "008";
	/****************重点人员走访类型end*****************/
	
	//--综治.事件管理.外平台采集事件--事件大类
	public static final String COLUMN_EVENT_DISPOSAL_BIG_TYPE = "big_type";
	//--综治.事件管理.外平台采集事件--事件小类
	public static final String COLUMN_EVENT_DISPOSAL_SMALL_TYPE = "small_type";
	//--综治.事件管理.外平台采集事件--影响范围
	public static final String COLUMN_EVENT_DISPOSAL_INFLUENCE_DEGREE = "influence_degree";
	//--综治.事件管理.外平台采集事件--信息来源
	public static final String COLUMN_EVENT_DISPOSAL_SOURCE = "source";
	//--综治.事件管理.外平台采集事件--涉及人数
	public static final String COLUMN_EVENT_DISPOSAL_INVOLVED_NUM = "involved_num";
	//--综治.事件管理.外平台采集事件--状态
	public static final String COLUMN_EVENT_DISPOSAL_STATUS = "status";
	//--综治.事件管理.外平台采集事件--紧急程度
	public static final String COLUMN_EVENT_DISPOSAL_URGENCY_DEGREE = "urgency_degree";
	//--综治.事件管理.外平台采集事件--结果类型 wap
	public static final String COLUMN_EVENT_DISPOSAL_RESULTS_TYPE = "results_type";
	//--综治.事件管理.外平台采集事件--采集渠道
	public static final String COLUMN_EVENT_DISPOSAL_COLLECT_WAY = "collect_way";
	//--默认事件小类
	public static final String DEFAULT_SMALL_TYPE = "0701";
	/**事件默认对接平台编码*/
	public static final String DEFAULT_EVENT_BIZ_PLATFORM = "0";
	
	//--综治.案件警情管理
	public static final String T_ZZ_CASES = "t_zz_cases";
	public static final String COLUMN_CASES_TYPE = "type_";
	//社会组织管理
	public static final String T_DC_SOCIETY_ORG_INFO = "t_dc_society_org_info";
	//组织分类'01.学术性；02.行业性；03.专业性；04.联合性；05.港澳台；06.外国商会；   08.共产党；  09.纪检组织；99其它；';
	public static final String COLUMN_ORG_TYPE = "type_";
	//'组织性质0.法人；1.集体；2.个人';
	public static final String COLUMN_ORG_NATURE = "nature";
	//行业分类'00.科技研究；01.生态环境；02.教育；03.卫生；04.社会服务；05.文化；06.体育；07.法律；08.工商业服务；09.宗教；10.农业及农村发展；11.职业及从业者组织；12.国际及涉外组织；13.其他';
	public static final String COLUMN_ORG_INDUSTRY = "industry";
	
	//--法人单位基础信息--法人类型
	public static final String COLUMN_COR_TYPE = "cor_type";
	//--法人单位基础信息--注册或开办资金币种
	public static final String COLUMN_COR_REGISTERED_CURRENCY = "registered_currency";
	//--法人单位基础信息--行业分类
	public static final String COLUMN_COR_CATEGORY = "category";
	//--法人单位基础信息--经济类型
	public static final String COLUMN_COR_ECONOMIC_TYPE = "economic_type";
	//--门店基础信息--店招状态
	public static final String COLUMN_STORE_STATUS = "store_status";
	//--门店基础信息--垃圾处置费是否缴纳
	public static final String COLUMN_GARBAGE_IS_COST = "garbage_is_cost";
	//--门店基础信息--店招审批状态
	public static final String COLUMN_STORE_APPROVA_STATUS = "store_approva_status";
	//--违法建设--性质
	public static final String COLUMN_NATURE = "nature";
	
	//--企业(法人)员工基础信息--工作职务
	public static final String COLUMN_CUR_DUTY = "cur_duty";
	//--企业(法人)员工基础信息--综治职务
	public static final String COLUMN_ZZGROUP_DUTY = "zzgroup_duty";
	
	// ---------town--------------start
	// --综治.村居城居管理--房屋类型
	public static final String COLUMN_BUILDING_BUILDING_NATURE = "building_nature";
	// --综治.村居城居管理--建筑结构
	public static final String COLUMN_BUILDING_STRUCTURE = "building_structure";
	
	// --综治.土地地籍管理--土地来源
	public static final String COLUMN_LAND_SOURCE = "land_source";
	// --综治.土地地籍管理--权属性质
	public static final String COLUMN_LAND_OWNER_NATURE = "owner_nature";
	// --综治.土地地籍管理--用途类型
	public static final String COLUMN_LAND_USE_TYPE = "use_type";
	
	// -------------town------------stop
	
	//--护路护线信息--治安隐患等级
	public static final String COLUMN_SEC_RISK_LEVEL = "sec_risk_level";
	
	public static final String COLUMN_TASK_TYPE = "task_type";
	
	public static final String COLUMN_TASK_LEVEL = "task_level";

	//农业管理
	public static final String COLUMN_ARABLE_LAND_CATALOG = "catalog_";
	
	
	//评价对象---事件
	public static final String EVA_OBJ_EVENT = "01";
	//督办对象---任务
	public static final String EVA_OBJ_TASK = "02";
	//评价对象---新事件
	public static final String EVA_OBJ_NEW_EVENT = "03";
	//评价对象---案件
	public static final String EVA_OBJ_CASE = "04";


	//---工作流相关-----

	public static final long WORK_FLOW_STOP_NODE_ID = -3;

	//-- 矛盾纠纷工作流事件ID
	public static final long DISPUTE_WORKFLOW_EVENT_ID = 21;
	//-- 网格公文流转事件ID
	public static final long GRID_FILETRANS_WORKFLOW_EVENT_ID = 22;
	//-- 综治矛盾纠纷调解单据录入
	public static final long WORKFLOW_EVENT_PTZZ_DISPUTE = 41;
	//-- 综治消防火灾隐患单据录入
	public static final long WORKFLOW_EVENT_PTZZ_FIRECONTROL = 42;
	//-- 综治安全隐患单据录入
	public static final long WORKFLOW_EVENT_PTZZ_SAFETYRISK = 43;
	//-- 综治风险评估单据录入
	public static final long WORKFLOW_EVENT_PTZZ_RISKASSESSMENT = 44;
	//-- 漳州矛盾纠纷调解
	public static final long WORKFLOW_EVENT_ZZZZ_DISPUTE = 45;
	//-- 重点人群
    public static final long WORKFLOW_EVENT_IMPORT_CROWD = 46;
	//-- 东孚指挥调度事件
    public static final long WORKFLOW_EVENT_DF_DISPATCH = 47;
    //-- 通用事件
    public static final long WORKFLOW_EVENT_GENERAL_EVENT = 50;

    //--------------项目管理
    public static final long WORKFLOW_EVENT_PTZZ_GENERAL_EVENT = 50;
    public static final long WORKFLOW_EVENT_TASKMANGE_EVENT = 51; //任务管理
    public static final long WORKFLOW_EVENT_RELEASEMANAGE_EVENT = 52; //发布管理
    public static final long WORKFLOW_EVENT_RESEARCHMANGE_EVENT = 53;//研发管理
    public static final long WORKFLOW_EVENT_EMERGENCYMANAGE_EVENT = 54;//紧急事件申请
    public static final long WORKFLOW_EVENT_EMERGENCYMANGE_EVENT = 54;// 紧急事件


    /**
	 * 公文流转
	 */
	//-- 公文类型
	public static final String TABLE_OA_DOC_TYPE = "t_dc_oa_doc_type";
	//-- 公文流转实例
	public static final String TABLE_OA_DOC_FLOW = "t_dc_oa_doc_flow";
	//-- 个案分析
	public static final String TABLE_OA_CASE_ANA = "t_dc_oa_case_ana";
	//-- 例行报告
	public static final String TABLE_OA_REPORT = "t_dc_oa_report";
	//-- 民情日志
	public static final String TABLE_OA_CUSTOM_LOG = "t_dc_oa_custom_log";
	//-- 信息发布
	public static final String TABLE_OA_NOTICE = "t_dc_oa_notice";
	//-- 公文输入组
	public static final String TABLE_OA_INPUT_GROUP = "t_dc_oa_input_group";
	//-- 公文回复
	public static final String TABLE_OA_DOC_REPLAY = "t_dc_oa_doc_replay";
	//-- 公文附件
	public static final String TABLE_OA_DOC_ATTACHMENT = "t_dc_oa_doc_attachment";

	//-- 公文事务级别
	public static final String COLUMN_OA_DOCLEVEL = "doc_level";

	//-- 消息类型
	public static final int TYPE_OA_MESSAGE_SEND = 1;  // 发送的消息
	public static final int TYPE_OA_MESSAGE_RECV = 2;  // 接收的消息

	//-- 流程状态
	//public static final String STATUS_OA_FLOW_COMMIT = "001"; // 提交
	public static final String STATUS_OA_FLOW_UNREAD = "002"; // 待阅
	public static final String STATUS_OA_FLOW_DELETE = "003"; // 删除
	public static final String STATUS_OA_FLOW_SAVE = "004"; // 保存
	public static final String STATUS_OA_FLOW_FINISH = "005"; // 完毕
	//-- 公文流转列表搜索标识
	public static final String STATUS_OA_FLOW_TODO = "101"; // 待办标识，列表搜索用
	public static final String STATUS_OA_FLOW_DONE = "102"; // 已完成标识，列表搜索用

	//-- 输入类型
	public static final int TYPE_OA_INPUT_REPLAY = 1;
	public static final int TYPE_OA_INPUT_ATTACH = 2;

	//-- 提交类型
	public static final int TYPE_OA_POST_SAVE = 1;
	public static final int TYPE_OA_POST_COMMIT = 2;

	//-- 统计信息相关
	public static final long STAT_TYPE_ID_FOR_RESIDENTGRID = 2L;  //-- 居民网格的统计类型ID
	public static final long STAT_TYPE_ID_FOR_ENTERPRISEGRID = 3L;  //-- 居民网格的统计类型ID

	//-- 组织网格详细信息类型
	public static final int TYPE_GRID_DETAIL_ORG = 1;
	public static final int TYPE_GRID_DETAIL_LAYER = 2;

	//-- 漳州统计信息地区配置表类型
	public static final String TYPE_ZZ_JX_PLACE_TYPE_1 = "001";
	public static final String TYPE_ZZ_JX_PLACE_TYPE_2 = "002";
	public static final String TYPE_ZZ_JX_PLACE_TYPE_3 = "003";

	//-- 事件附件类型
	public static final String TYPE_ANNEX_001 = "001"; //矛盾纠纷调解（莆田、漳州版）
	public static final String TYPE_ANNEX_002 = "002"; //消防火灾隐患排查
	public static final String TYPE_ANNEX_003 = "003"; //安全隐患排查
	public static final String TYPE_ANNEX_004 = "004"; //消防火灾隐患排查整改情况附件类型
	public static final String TYPE_ANNEX_005 = "005"; //安全隐患排查整改情况附件类型
	public static final String TYPE_ANNEX_006 = "006"; //消防演练记录管理
    public static final String TYPE_ANNEX_007 = "007"; //通用事件
    public static final String TYPE_ANNEX_008 = "008"; //任务管理附件
    public static final String TYPE_ANNEX_009 = "009"; //任务管理--子任务附件
    public static final String TYPE_ANNEX_010 = "010"; //研发管理附件
    public static final String TYPE_ANNEX_011 = "011"; //研发管理--审核步骤相关附件
    public static final String TYPE_ANNEX_012 = "012"; //任务派发附件
    public static final String TYPE_ANNEX_013 = "013"; //任务代办附件
    public static final String TYPE_ANNEX_014 = "014"; // 建设项目--项目规划图
	public static final String TYPE_ANNEX_015 = "015"; // 建设项目进度--工地图片
	public static final String TYPE_ANNEX_016 = "016"; // 信访事件附件
	public static final String TYPE_ANNEX_017 = "017"; // 楼栋外观图附件
	public static final String TYPE_ANNEX_018 = "018"; // 楼栋结构图附件
	
	/** 事件时限状态：正常 */
	public static final String LIMIT_TIME_STATUS_NORMAL = "1";
	/** 事件时限状态：将到期 */
	public static final String LIMIT_TIME_STATUS_TO_EXPIRE = "2";
	/** 事件时限状态：已过期 */
	public static final String LIMIT_TIME_STATUS_EXPIRED = "3";

	//-- 日志
	public static final String CHANGE_LOG_STATUS_DEFAULT = "S";
	public static final String CHANGE_LOG_APP_CODE = "zzgrid";
	public static final String CHANGE_LOG_ACTION_INSERT = "I"; //操作类型，插入
	public static final String CHANGE_LOG_ACTION_DELETE = "D"; //操作类型，删除
	public static final String CHANGE_LOG_ACTION_UPDATE = "U"; //操作类型，更新
	public static final String CI_RS_UNITED_OBJECT = "UNITED_OBJECT"; // 统战对象
	public static final String CHANGE_LOG_MODEL_CROWD = "IMPORT_CROWD"; // 重点人群
	public static final String CHANGE_LOG_MODEL_UNIT = "IMPORT_UNIT"; // 重点单位
	public static final String CHANGE_LOG_MODEL_BLACK = "GOVERN_BLACK"; // 重点场所

	//-- 网格刷新类型
	public static final String GRID_FLUSH_TYPE_AREA = "AREA"; //删除小区
	public static final String GRID_FLUSH_TYPE_BUILDING = "BUILDING"; //删除楼栋
	public static final String GRID_FLUSH_TYPE_ROOM = "ROOM"; //删除房间

	//-- 接口类型
	public static final String INTF_ORG_QUERY_TYPE_ID = "id";  //--通过ID找组织
	public static final String INTF_ORG_QUERY_TYPE_CODE = "code"; //--通过编码找组织
	
	/**
	 * 江西赣州
	 */
	public static String JIANGXI_FUNC_ORG_CODE="36";
	
	/**
	 * 网格类型
	 */
	public static final String GRID_TYPE_ENTERPRISE = "002";
	
	/**
	 * 地图类型
	 */
	public static final String MAP_TYPE_GOOGLE = "001"; //地图类型，使用google地图
	public static final String MAP_TYPE_IMAGE = "002"; //地图类型，使用图片
	public static final String MAP_TYPE_BAIDU = "003"; //地图类型，使用百度地图
	public static final String MAP_TYPE_ICT_GIS_ENGINE = "004"; //地图类型，使用ICT地图引擎
	/**
	 * 地图动作类型
	 */
	public static final String MAP_ACTION_TYPE_EDIT = "edit"; // 地图配置
	public static final String MAP_ACTION_TYPE_SHOW = "show"; // 地图展示
	public static final String MAP_ACTION_TYPE_SELECT_POINT = "selectPoint"; // 地图展示

	/**
	 * 网格管理员职务
	 */
	public static final String GRID_ADMIN_DUTY_001 = "001"; //网格管理员
	public static final String GRID_ADMIN_DUTY_002 = "002"; //网格协管员
	public static final String GRID_ADMIN_DUTY_003 = "003"; //网格督导员
	public static final String GRID_ADMIN_DUTY_004 = "004"; //包片段警

	/**
	 * 标注地图类型
	 */
	public static final String MARKER_MAP_TYPE_01 = "01"; //二维
	public static final String MARKER_MAP_TYPE_02 = "02"; //三维 2.5d

	/**
	 * 标注类型分类
	 */
	public static final String MARKER_TYPE_BASE_RES = "020101"; //基础的资源
	public static final String MARKER_TYPE_PARTY = "0101"; //党组织
	public static final String MARKER_TYPE_GLOBAL_EYES = "02020501"; //全球眼
	public static final String MARKER_TYPE_EVENT = "0301"; //事件
	public static final String CASSES = "010302"; // 案件警情
	public static final String MARKER_TYPE_PEOPLE_MEDIATION="0501";//人民调解

	public static final String MARKER_TYPE_LAND = "0901"; //土地使用
	public static final String MARKER_TYPE_PROJECT = "0902"; //建设项目
	

	public static final String BIZ_TYPE_ORG = "0";//综治机构
	public static final String BIZ_TYPE_PREVTION = "1";//群防群治
	public static final String BIZ_TYPE_CAREROAD_ORG = "2";//护路护线
	public static final String BIZ_TYPE_WORKSAFETY = "3";//安监
	public static final String BIZ_TYPE_RISKRATINGORG = "4";//社会稳定风险评估领导机构
	public static final String BIZ_TYPE_POVERTY_REDUCTION = "5";//扶贫驻村
	public static final String BIZ_TYPE_MAINTENANCE_TEAM = "6";//维稳队伍
	public static final String BIZ_TYPE_NATIONAL_SECURITY_GROUP = "7"; // 国家安全小组
	public static final String BIZ_TYPE_CENTER = "8"; // 综治中心
	
	
	public static final String MARKER_TYPE_DEVICE = "7778"; //马尾设备
	public static final String YANPING_TYPE_DEVICE = "7779"; //智慧农业(延平)
	public static final String ADMINISTRATIONPENALTY_MARKER_TYPE = "7780"; //行政处罚
	public static final String WELL_KNOWN_TRADEMARK_MARKER_TYPE = "7781"; //驰名商标
	public static final String FAITHFUL_ENTERPRISE_MARKER_TYPE = "7782"; //守重企业

	
	/**
	 * 党员服务状态
	 */
	public static final String PARTY_SERV_STATUS_RECEIVED_01 = "01"; //办理中
	public static final String PARTY_SERV_STATUS_RECEIVED_02 = "02"; //办理中(已过期)
	public static final String PARTY_SERV_STATUS_END = "04"; //已办结
	public static final String DAILY_ACTIVITY_TYPE = "01";//01党建活动
	public static final String DAILY_ACTIVITY_STATUS_001 = "001";//001活动有效
	public static final String DAILY_ACTIVITY_STATUS_003 = "003";//001活动无效

	/**
	 * 党员服务操作类型
	 */
	public static final String RECORD_OPERATE_TYPE_RECEIVED = "01"; //受理
	public static final String RECORD_OPERATE_TYPE_DISTRIBUTE = "02"; //转办
	public static final String RECORD_OPERATE_TYPE_OVERDUE_DISTRIBUTE = "03"; //过期转办
	public static final String RECORD_OPERATE_TYPE_END = "04"; //已结束

	public static final String ALIVE_STATUS = "1";//居民有效状态
	/**
	 * add by zhanggf for 人口管理
	 */
	/**
	 * 居民关联有效状态
	 */
	public static final String MEMBERS_ALIVE_STATUS = "001";
	/**
	 * 家庭信息数据状态
	 */
	public static final String HOME_INFO_STATUS="001";
	/**
	 * 首选联系人
	 */
	public static final String DEFAULT_CONTACTOR="0";
	/**
	 * 家庭成员有效状态
	 */
	public static final String HOME_MEMBER_ALIVE_STATUS = "001";
	public static final String TABLE_CI_RS_UNEMPLOYED = "T_DC_CI_RS_UNEMPLOYED"; // 居民就业信息表
	public static final String TABLE_CI_RS_FP = "T_DC_CI_RS_FP"; // 居民计生信息
	/**
	 * 资源服务器域名
	 */
	//public static String RS_DOMAIN_VAL;

	/*@Autowired(required = true)
	public void setRSDomain(@Value("${rs_domain}") String rsDomain) {
		ConstantValue.RS_DOMAIN_VAL = rsDomain;
	}*/
	//每天短信发送量最大限额
	public static final int SEND_MSG_DAILY_LIMIT = 5000;
	/**
	 * 用户的权限菜单列表
	 */
	public static final String SESSION_KEY_USERCOOKIE="cookie_usercookie";
	/**
	 * 用户ID信息
	 */
	public static final String SESSION_KEY_USERID="cookie_user_id";
	/**
	 * 信息域
	 */
	public static final String SESSION_KEY_LOCATION_LIST="cookie_location_list";
	
	/**
	 * 任务派发的状态
	 * @author chenyu3
	 *
	 */
	public enum taskStatus{
		notIssued("0", "未下发"), 
		issued("1", "已下发"), 
		start("2", "已开始"),
		finish("3", "已完成"),
		delete("4", "删除"),
		archive("5", "已归档");
		private String index;  
		private String showName;  
		
		 // 构造方法   
	    private taskStatus(String index,String showName) {   
	        this.showName = showName;   
	        this.index = index;   
	    }  
	 // get set 方法   
	    public String getShowName() {   
	        return showName;   
	    }   
	    public String getIndex() {   
	        return index;   
	    }
	    
	}
	
	public enum taskProcStatus{
		notStart("0", "未开始"), 
		start("1", "已开始"), 
		finish("2", "已完成"),
		issued("3", "已发布"),
		back("4", "回退"),
		archive("5", "已归档");
		private String index;  
		private String showName;  
		
		 // 构造方法   
	    private taskProcStatus(String index,String showName) {   
	        this.showName = showName;   
	        this.index = index;   
	    }  
	 // get set 方法   
	    public String getShowName() {   
	        return showName;   
	    }   
	    public String getIndex() {   
	        return index;   
	    }
	    
	}
	/**
	 * 轮渡定位IP
	 */
	public static final String ferry_ip="220.162.239.162";
	/**
	 * 轮渡定位端口
	 */
	public static final String ferry_port="7001";
	/**
	 * 轮渡定位账号
	 */
	public static final String ferry_username="ptzzwg";
	/**
	 * 轮渡定位密码
	 */
	public static final String ferry_password="ptzzwg";

	//--新版工作流---流程状态
	public static final String FLOW_METADATA_STATUS_000 = "000"; //流程未启动
	public static final String FLOW_METADATA_STATUS_001 = "001"; //流程执行中
	public static final String FLOW_METADATA_STATUS_002 = "002"; //流程已结束
	
	//手机端巡查打卡时间分割点
	public static final int INSPECTION_SEP_HOUR = 14;
	
	/**
	 * 信息域类型  1 为单位如：福州市；  0为部门 如：财政厅； -1为所有
	 */
	public static final int ORG_TYPE_ALL = -1; //所有
	public static final int ORG_TYPE_DEPT = 0; //部门
	public static final int ORG_TYPE_UNIT = 1; //单位
	
	//-- 短信平台标记
	public static final String SMS_PLATFORM_FLAG = "zhsq_event";
	
	//事件的小类型为矛盾纠纷
	public static final String DISPUTE_TYPE = "0203";
	//事件的小类型为红袖标
	public static final String PATROL_TYPE = "0603";
	
	
	//图片附件上传 EVENT_SEQ
	public static final String EVENT_SEQ_1 = "1";//事件处理前
	public static final String EVENT_SEQ_2 = "2";//事件处理中
	public static final String EVENT_SEQ_3 = "3";//事件处理后
	
	//固定巡查打卡使用 开始打卡与结束打卡之间的时间间隔配置
	public static final Map<String, Double> INSPECTION_TIMELIMIT_MAP = new HashMap<String, Double>();
	static{
		INSPECTION_TIMELIMIT_MAP.put("360", 0.5);//江西
		INSPECTION_TIMELIMIT_MAP.put("361", 0.5);//江西
	}

	/**
	 * 网上办事，办理完更新状态到网站群
	 */
	public static final String OA_ONLINE_APPROVAL_UPDATESTATUS = "zzgrid_oa_online_approval_updateStatus";
	
	// --系统相关-------------------------------
	public static final String VIEW_USER_ROLE_INFO = "v_t_dc_user_role_info";
	
	// -- 网格平房标识
	public static final int GRID_RESULT_ROOM_TYPE_BUNGALOW = 2;
	
	public static final String twoDimensionalMapType="2";	//二维地图返回的type值
	public static final String threeDimensionalMapType="20";//三维地图返回的type值
	
	// -- 列表页中最大的条数（打印、居民搜索等）
	public static final int WHOLE_LIST_MAX_ROW = 50;
	
	// -- 数据库字段类型
	public static final int DB_COLUMN_TYPE_VARCHAR = 1;
	public static final int DB_COLUMN_TYPE_LONG = 2;
	public static final int DB_COLUMN_TYPE_INTEGER = 3;
	public static final int DB_COLUMN_TYPE_DATE = 4;
	public static final int DB_COLUMN_TYPE_BOLB = 5;
	public static final int DB_COLUMN_TYPE_DOUBLE = 6;
	
	/**
	 * 公文流转
	 */
	// -- 表格审核
	public static final String TABLE_OA_APPROVAL = "t_dc_oa_approval";
	// -- 指定公文默认接收人
	public static final String TABLE_OA_ANONYMOUS = "T_DC_OA_ANONYMOUS";
	// -- 网上审核
	public static final String TABLE_OA_ONLINEAPPROVAL = "T_DC_OA_OnlineApproval";
	// -- 网上审批-消防安全检查申报表
	public static final String TABLE_OA_WSBS_FIRE_CHECK = "t_dc_wsbs_fire_check_apply";
	
	// -- 综治.网格管理人员安排表
    public static final String TABLE_DICTIONARY_PARTY_INDIVIDUAL = "t_dc_party_individual";
    // -- 网格表
	public static final String TABLE_GRID = "t_dc_grid";
	
	// -- 通用事件
	public static final String TABLE_GENERAL_EVENT = "T_ZZ_GENERAL_EVENT";
	
	// 紧急事件表
	public static final String TABLE_EMERGENCY_MANAGE = "T_ZZ_EMERGENCY_MANAGE";
	
	// 发布管理表
	public static final String TABLE_RELEASE_MANAGE = "T_ZZ_RELEASE_MANAGE";
	
	// 任务管理表
	public static final String TABLE_TASK_MANAGE = "T_ZZ_task_manage";
	public static final String TABLE_TASK_ASSIGN_INFO = "T_ZZ_task_Assign_info";
	
	//人民调解
	public static final String IS_SUCCESS = "1"; //成功
	
	// -- 物业信息
	public static final String TABLE_CI_RS_PROPERTY = "t_dc_ci_rs_property";
	
	// 网格表--网格生成状态
	public static final String COLUMN_GRID_STATUS = "grid_status";
	
	public static final String TABLE_PARTY_CENTIFICATION = "t_dc_party_centification";
	
	// --综治.事件管理.外平台采集事件--业务类型
	public static final String COLUMN_EVENT_DISPOSAL_SUPERVISE_OBJECT_TYPE = "supervise_object_type";
	
	public static final String TASK_CODE_WEBSITE = "WEBSITE";
	public static final String TASK_NAME_WEBSITE = "来自互动共治平台";
	
	// 事件 --评价表
	public static final String TABLE_EVA_RESULT = "t_sj_eva_result";
	// 评价表--评价等级
	public static final String COLUMN_EVA_RESULT_EVA_LEVEL = "eva_level";
	/** 未评价 */
	public static final String EVALUATE_FLAG_NO = "0";
	/** 已评价 */
	public static final String EVALUATE_FLAG_YES = "1";
	//评价等级字典pcode
	public static final String EVALUATE_LEVEL_PCODE = "D050001";
	
	/**事件流程名称*/
	public static final String EVENT_WORKFLOW_NAME = "事件流程";
	/**南昌事件流程名称*/
	public static final String EVENT_WORKFLOW_NAME_NCH = "南昌事件流程";
	/**事件流程图名称*/
	public static final String EVENT_WORKFLOW_WFTYPEID = "fa";
	/**事件流程 FORM_TYPE_ID*/
	public static final String EVENT_WORKFLOW_FORM_TYPE_ID = "300";
	/**事件子流程 FORM_TYPE_ID*/
	public static final Integer EVENT_SUB_WORKFLOW_FORM_TYPE_ID = 301;
	/**事件流程 流程申请中*/
	public static final String EVENT_WORKFLOW_STATUS_START = "1";
	/**事件流程 流程申请结束*/
	public static final String EVENT_WORKFLOW_STATUS_END = "2";
	
	public static final String EVENT_WORKFLOW_STATUS_START_NAME = "申请中";
	public static final String EVENT_WORKFLOW_STATUS_END_NAME = "已完成";
	public static final String EVENT_WORKFLOW_STATUS_DEFAULT_NAME = "未启动";
	
	/**事件CODE_使用序列名称*/
	public static final String SEQ_EVENT_CODE = "SEQ_EVENT_CODE";
	/**涉及业务案(事)件编号序列*/
	public static final String SEQ_RE_NO = "SEQ_RE_NO";
	
	
	//紧急程度pcode
	public static final String URGENCY_DEGREE_PCODE = "A001093271";
	//紧急程度变动是否发送消息
	public static final String SEND_URGENCY_MSG = "SEND_URGENCY_MSG";
	//影响范围pcode
	public static final String INFLUENCE_DEGREE_PCODE = "A001093094";
	//信息来源pcode
	public static final String SOURCE_PCODE = "A001093222";
	//涉及人数pcode
	public static final String INVOLVED_NUM_PCODE = "A001093270";
	//事件大类pcode
	public static final String BIG_TYPE_PCODE = "A001093199";
	//事件小类pcode
	public static final String SMALL_TYPE_PCODE = "A001093228";
	//事件状态pcode
	public static final String STATUS_PCODE = "A001093095";
	//事件子状态pcode
	public static final String SUB_STATUS_PCODE = "B900";
	//附件类型pcode
	public static final String ATTR_FLAG_PCODE = "B063";
	//事件采集渠道pcode
	public static final String COLLECT_WAY_PCODE = "A001093096";
	//八员队伍
	public static final String EMT_TYPE_PCODE = "B218";
	//涉及业务案(事)件 主犯(嫌疑犯)证件类型pcode
	public static final String PRISONERS_DOC_TYPE = "B010";
	//设计业务案(事)件 案(事)件性质
	public static final String NATURE = "B040";
	//案件类型
	public static final String CASE_TYPE_CODE = "B044";
	
	public static final String EVENT_TYPE_DC = "B401000";
	public static final String EVENT_LEVEL_DC = "B401001";

	public static final String NINE_SMALL_TYPE_DC = "D006014";
	
	public static final String RESOURCE_RANGE_PCODE = "B219";
	
	// 预案类型
	public static final String EMERGENC_PLAN_TYPE = "A001139";
	public static final String EMERGENC_PLAN_LEVEL = "A001140";
	public static final String EMERGENC_PLAN_ROLE =  "A001141";
	
	/*************************功能配置功能************************************/
	public static final String MODULE_CODE_PCODE = "B046";//所属模块
	public static final String CFG_VAL_TYPE_PCODE = "B047";//配置值类型
	
	//短信模板编码
	public static final String SMS_NOTE_TYPE = "SMS_NOTE_TYPE";//短信模板功能配置编码(整合中)
	public static final String DEFAULT_NOTE = "defaultNote";
	public static final String REPORT_NOTE = "reportNote";
	public static final String SHUNT_NOTE = "shuntNote";
	public static final String CLOSE_NOTE = "closeNote";
	public static final String REMIND_NOTE = "remindNote";//督办短信模板
	public static final String URGE_NOTE = "urgeNote";//催办短信模板
	public static final String WILL_EXPIRE_NOTE = "willExpireNote";
	public static final String EMERGENCY_RESPONSE_NOTE = "emergencyResponseNote";//应急预案短信模板
	public static final String EVENT_PREWARNING_NOTE = "eventPrewarningNote";//事件预警短信模板
	public static final String EVENT_WARNING_NOTE = "eventWarningNote";//事件预案短信模板
	//以下短信模板配置在smsNoteType功能配置中
	public static final String DRUG_ENFORCEMENT_EVENT_REPORT_NOTE = "drugEnforcementEventReportNote";//禁毒事件上报操作短信模板
	public static final String DRUG_ENFORCEMENT_EVENT_SEND_NOTE = "drugEnforcementEventSendNote";//禁毒事件下派操作短信模板
	public static final String DRUG_ENFORCEMENT_EVENT_SEND_IMPLEMENT_NOTE = "drugEnforcementEventSend4ImplementNote";//禁毒事件下派实施短信模板
	public static final String DRUG_ENFORCEMENT_EVENT_TASK_OVER_TIME_NOTE = "drugEnforcementEventTaskOverTimeNote";//禁毒事件办理环节超时短信模板
	public static final String DRUG_ENFORCEMENT_EVENT_ARCHIVE_NOTE = "drugEnforcementEventArchiveNote";//禁毒事件归档短信模板
	public static final String DEPARTMENT_HANDLE_NODE = "departmentHandleNode";//专业部门处理短信模板
	public static final String TWO_VIO_PRE_NOTE = "twoVioPreNote";//两违防治短信/消息模板
	public static final String ENTERPRISE_HIDDEN_DANGER_NOTE = "ehdNote";//企业安全隐患短信/消息模板
	public static final String EPIDEMIC_PRE_CONTROL_NOTE = "epcNote";//疫情防控短信/消息模板
	public static final String HOUSE_HIDDEN_DANGER_NOTE = "hhdNote";//房屋安全隐患短信/消息模板
	public static final String WATER_QUALITY_NOTE = "wqNote";//流域水质问题短信/消息模板
	public static final String MEETING_NOTE = "meetingNote";//三会一课短信/消息模板
	public static final String POOR_SUPPORT_VISIT_NOTE = "psvNote";//扶贫走访短信/消息模板
	public static final String RURAL_HOUSING_NOTE = "ruralHousingNote";//农村建房(一告知四到场)短信/消息模板
	public static final String FOREST_FIRE_PREVENTION_NOTE = "ffpNote";//森林防灭火短信/消息模板
	public static final String BUSINESS_PROBLEM_NOTE = "bpNote";//营商问题短信/消息模板
	public static final String POVERTY_PRE_MONITOR_NOTE = "ppmNote";//致贫返贫短信/消息模板
	public static final String PETITION_PERSON_NOTE = "ppNote";//信访人员稳控短信/消息模板
	public static final String MARTYRS_FACILITY_NOTE = "mfNote";//烈士纪念设施短信/消息模板
	public static final String ENVIRONMENTAL_HEALTH_TREATMENT_NOTE = "ehtNote";//环境卫生整治/消息模板
	public static final String THREE_ONE_TREATMENT_NOTE = "totNote";//三合一整治/消息模板
	//事件大类展示 权限控制
	public static final String TYPES = "types";
	//事件信息来源展示 过滤控制
	public static final String SOURCES = "sources";
	//新增事件默认事件类型 配合TYPES使用 触发条件
	public static final String DEFAULT_TYPE = "_DEFAULT";
	//新增事件默认巡防类型触发条件（南昌指挥中心）
	public static final String DEFAULT_PATROL_TYPE = "DEFAULT_PATROL_TYPE";
	//南昌巡防类型字典父级编码
	public static final String PATROL_TYPE_PCODE = "A001093092";
	//南昌巡防事件类型类型映射字典父级编码
	public static final String EVENT_TYPE_PCODE = "B915006";
	//南昌指挥调度默认其他点位查询联系人职位功能配置触发条件
	public static final String CONNTACT_USER_POSITION = "contact_user_position";
	//地图图层列表是否启用单个撒点配置
	public static final String LOCATION_SINGLE_FLAG = "LOCATION_SINGLE_FLAG";
	//事件工作流按职位名称选人
	public static String EVENT_WORFLOW_POSITIONNAME = "eventWorkflowPositionName";
	public static String EVENT_WORFLOW_POSITIONNAME1 = "eventWorkflowPositionName1";
	public static String EVENT_WORFLOW_POSITIONNAME2 = "eventWorkflowPositionName2";
	public static String EVENT_WORFLOW_POSITIONNAME3 = "eventWorkflowPositionName3";
	public static String EVENT_WORFLOW_POSITIONNAME4 = "eventWorkflowPositionName4";
	public static String EVENT_WORFLOW_POSITIONNAME5 = "eventWorkflowPositionName5";
	public static String EVENT_WORFLOW_POSITIONNAME6 = "eventWorkflowPositionName6";
	//工作流是否结案
	public static String WORKFLOW_IS_TO_COLSE = "1";//结案
	public static String WORKFLOW_IS_NO_CLOSE = "0";//不结案
	
	//新事件附件类型
	public static final String EVENT_ATTACHMENT_TYPE = "ZHSQ_EVENT";
	
	/**
	 * 双随机事件附件类型
	 */
	public static final String EVENT_RANDOM_ATTACHMENT = "ZHSQ_EVENT_RANDOM";
	/**
	 * 微信事件附件类型
	 */
	public static final String EVENT_WECHAT_ATTACHMENT_TYPE = "ZHSQ_EVENT_WECHAT";

	public static final String EVENT_CLUE_ATTACHMENT_TYPE = "ZHSQ_EVENT_CLUE";
	/**
	 * 微信事件地图标注模块编码
	 */
	public static final String EVENT_WECHAT_MODULE_CODE = "ZHSQ_EVENT_WECHAT";
	
	/**
	 * 案件附件类型
	 */
	public static final String EVENT_CASE_ATTACHMENT_TYPE = "EVENT_CASE";
	/**
	 * 案件地图标注模块编码
	 */
	public static final String EVENT_CASE_MODULE_CODE = "EVENT_CASE";
	/**
	 * 案件编码序列
	 */
	public static final String SEQ_EVENT_CASE_CODE = "SEQ_EVENT_CASE_CODE";
	
	/**
	 * 草稿
	 */
	public static String PATROL_DRAFT = "0";//草稿
	/**
	 * 归档
	 */
	public static String PATROL_END = "1";//归档
	/**
	 * 巡防状态：上报
	 */
	public static String PATROL_REPORT = "2";//上报
	
	//流程图节点层级
	public static int EVENT_SHUNT = 0;//分流
	public static int EVENT_REPORT = 1;//上报
	public static int EVENT_UP_REPORT = 2;//越级上报
	public static int EVENT_CLOSE = 3;//结案
	public static int EVENT_END = 4;//归档
	
	public static int EVENT_SUPERVISE = 5;//督办
	public static int EVENT_URGE = 6;//催办
	
	public static int EVENT_START = 7;//启动
	
	/**
	 * 功能配置：业务模块详情URL的功能代码
	 */
	//功能代码
//	public static String BIZ_DETAIL_URL = "BIZ_DETAIL_URL";
	public static String BIZ_DETAIL_URL_IN_EVENT = "BIZ_DETAIL_URL_IN_EVENT";
	//新事件归档回写业务模块服务配置
	public static String EVENT_ARCH_CALLBACK_SERVICE = "EVENT_ARCH_CALLBACK_SERVICE_IN_EVENT";
	//功能配置：“事件类型与业务模块类型关联信息”的功能编码
	public static String EVENT_BIZ_TYPE = "EVENT_BIZ_TYPE_IN_EVENT";
	/**“事件类型与业务模块类型关联信息”中业务类型的中文名称，触发条件为功能配置EVENT_BIZ_TYPE_IN_EVENT中的配置值*/
	public static String EVENT_REPORT_BIZ_TYPE_NAME = "EVENT_REPORT_BIZ_TYPE_NAME";
	//应用域名配置公共样式域名配置触发条件
	public static String STATIC_DOMAIN = "$STATIC_DOMAIN";
	//应用域名配置GMIS域名配置触发条件
	public static String GMIS_DOMAIN = "$GMIS_DOMAIN";
	//应用域名配置GMIS项目名称
	public static String GMIS_CONTEXT_PATH = "$GMIS_CONTEXT_PATH";
	//功能配置：“是否启用新事件”的功能编码
	public static String IS_USE_NEW_EVENT = "IS_USE_NEW_EVENT";
	//功能配置：“应用域名配置”的功能编码
	public static String APP_DOMAIN = "APP_DOMAIN";
	//功能配置：红袖标事件类型的功能编码事
	public static final String PATROL_EVENT_TYPE = "PATROL_EVENT_TYPE";
	//功能配置：“综治网格_事件上报_各地市上报事件类型配置”的功能编码
	public static String REPORT_EVENT_TYPE = "REPORT_EVENT_TYPE_IN_EVENT";


	//消防检查上报成的事件归档回写服务配置触发条件
	public static String INSPECT_INFO_EVENT_TYPE = "INSPECT_INFO_EVENT_TYPE";
	//消防检查上报成的事件bizType
	public static String INSPECT_INFO_BIZ_TYPE = "INSPECT_INFO";


	public static String closeCallBack = "parent.parent.closeMaxJqueryWindow";
	
	public static String iframeUrl = "/zhsq/event/eventDisposalController/isDomain.jhtml?";
	
	/**************************************处理时限配置 开始*********************************/
	//处理时限 相对采集时间的时间间隔 单位为天
	public static final String HANDLE_DATE_INTERVAL = "HANDLE_DATE_INTERVAL";
	//南昌事件县区转给乡镇街道可以设置默认办结时限（功能配置触发条件）
	public static final String EVENT_DEFAULT_HANDLE_INTERVAL = "eventDefaultHandleInterval";
	//事件处理时限配置 w(weekDay)结尾表示间隔单位为工作日；n(naturalDay)结尾表示间隔单位为自然日
	public static final String EVENT_HANDLE_DATE_INTERVAL = "weekNaturalDayEvent";
	/**************************************处理时限配置 结束*********************************/
	
	//短信发送时限 相对处理时限的时间间隔 单位为小时
	public static final String WILL_EXPIRE_TIME_INTERVAL = "WILL_EXPIRE_TIME_INTERVAL";
	//是否发送将到期短信提醒 为true时，才发送
	public static final String IS_SEND_WILL_EXPIRE_SMS = "IS_SEND_WILL_EXPIRE_SMS";
	//是否展示处理时限
	public static final String SHOW_HANDLE_DATE = "SHOW_HANDLE_DATE";
	//展示事件处理时限
	public static final String SHOW_EVENT_HANDLE_DATE = "event";
	//展示环节处理时限
	public static final String SHOW_STEP_HANDLE_DATE = "step";
	//发送将到期短信提醒时间间隔
	public static final String WILL_EXPIRE_SMS_PERIOD = "WILL_EXPIRE_SMS_PERIOD";
	//事件对接时间间隔
	public static final String WILL_EXPIRE_EVENT_PERIOD = "WILL_EXPIRE_EVENT_PERIOD";
	//将到期短信发送失败次数限制
	public static final String FAIL_COUNT_LIMIT = "FAIL_COUNT_LIMIT";
	//事件详情页面是否可编辑
	public static final String IS_DETAIL_2_EDIT = "IS_DETAIL_2_EDIT";
	//事件详情页面展示事件相关类别信息
	public static final String IS_CAP_EVENT_LABEL_INFO = "isCapEventLabelInfo";
	//事件新增页面是否展示标签信息
	public static final String SHOW_LABEL_INPUT = "SHOW_LABEL_INPUT";
	
	//============================语音呼叫相关配置 开始===================================
	public static final String VOICE_CALL_ATTRIBUTE = "VOICE_CALL_ATTRIBUTE";
	
	//触发条件
	public static final String ON_VOICE_CALL = "on";//是否启用语音呼叫功能
	
	//============================语音呼叫相关配置 结束===================================
	
	//============================T_BAS_SMS_TASK、T_BAS_MSG_SEND_MID 信息发送相关配置 begin===============================================
	//以下配置需要项目重启后，方能生效
	public static final String SMS_TASK_ATTRIBUTE = "SMS_TASK_ATTRIBUTE";//信息发送功能配置编码
	public static final String IS_DRUG_ENFORCEMENT_EVENT_VALID = "isDrugEnfocementEventValid";//是否启用禁毒事件短信发送轮询，true为启用
	public static final String IS_SEND_DRUG_ENFOCEMENT_EVENT_TASK_OVER_TIME_SMS = "isSendDrugEnforcementEventTaskOverTimeSms";//是否发送禁毒事件环节超时短信，true为发送
	public static final String DRUG_ENFORCEMENT_EVENT_DEFAULT_ORG_CODE = "drugEnforcementEventDefaultOrgCode";//禁毒事件短信发送轮询默认组织
	public static final String DRUG_ENFORCEMENT_EVENT_POLLING_PERIOD = "drugEnforcementEventPollingPeriod";//禁毒事件短信发送轮询周期，单位为毫秒
	
	//南昌(NCH) 大联动(JC)消息发送相关配置
	public static final String IS_SEND_MSG_NCHJC_VALID = "isSendMsgNCHJCValid";//是否启用南昌大联动消息发送轮询，true为启用
	public static final String NCHJC_MSG_DEFAULT_ORG_CODE = "NCHJCMsgDefaultOrgCode";//南昌大联动消息发送轮询默认组织编码，多个值使用英文逗号连接
	public static final String NCHJC_MSG_POLLING_PERIOD = "NCHJCMsgPollingPeriod";//南昌大联动消息发送轮询周期，单位为天

	//事件自动评价归档轮询相关配置
	public static final String IS_EVA_VALID = "isEvaValid";//是否启用自动评价归档轮询，true为启用
	public static final String EVA_DEFAULT_ORG_CODE = "evaDefaultOrgCode";//自动评价归档轮询默认组织编码，多个值使用英文逗号连接
	public static final String EVA_POLLING_PERIOD = "evaPollingPeriod";//自动评价归档轮询周期，单位为小时
	public static final String EVA_PAGINATION_LIMIT = "evaPaginationLimit";//自动评价归档每页处理记录数
	public static final String EVA_TASK_NAME = "evaTaskName";//获取可自动评价事件需要经过的环节名称，多个值使用英文逗号分隔
	public static final String EVA_EVENT_STATUS = "evaEventStatus";//获取可自动评价事件状态，多个值使用英文逗号分隔

	//============================T_BAS_SMS_TASK、T_BAS_MSG_SEND_MID信息发送相关配置 end=================================================
	
	//============================EVENT_EXPAND_ATTRIBUTE 事件扩展实现类相关配置 begin====================================================
	public static final String EVENT_EXPAND_ATTRIBUTE = "EVENT_EXPAND_ATTRIBUTE";
	public static final String EVENT_EXPAND_SERVICE = "eventExpandService";//事件扩展实现类触发条件
	//============================EVENT_EXPAND_ATTRIBUTE 事件扩展实现类相关配置 end======================================================

	//============================ENTER_GRID_SCENARIO_APPLICATION 入格场景应用功能配置 begin====================================================
	public static final String ENTER_GRID_SCENARIO_APPLICATION = "ENTER_GRID_SCENARIO_APPLICATION";
	//============================ENTER_GRID_SCENARIO_APPLICATION 入格场景应用功能配置 end======================================================
	
	//应急预案密码设置
	public static final String EMERGENCY_RESPONSE_PASSWORD = "EMERGENCY_RESPONSE_PASSWORD";
	
	public static final String VALIDATE_LENGTH_FUNCTION_CODE = "VALIDATE_LENGTH";//数据字段长度验证
	
	/**
	 * 判断组织层级为社区的网格员是否是网格员
	 */
	public static final String IS_COMMUNITY_GRID_ADMIN = "IS_COMMUNITY_GRID_ADMIN";
	
	/**
	 * 案件类型在数据字典中编码
	 */
	public static final String CASES_TYPE = "B558";
	
	//============================消息、短信发送配置 begin=================================================
	public static final String SEND_MSG_AND_SMS_CFG = "SEND_MSG_AND_SMS_CFG";//消息、短信发送配置编码
	public static final String SEND_MSG_AND_SMS_CFG_TRIGGER_4_MOBILE_MSG = "isSendMsg2Mobile";//是否将消息推送到手机端
	public static final String IS_SHOW_SEND_MSG = "isShowSendMessage";//是否展示短信发送功能
	//============================消息、短信发送配置 end===================================================
	
	//============================事件展示相关设置 begin=================================================
	public static final String EVENT_EXHIBITION_ATTRIBUTE = "EVENT_EXHIBITION_ATTRIBUTE";
	public static final String IS_SHOW_ATTENTION_BTN = "isShowAttentionBtn";//是否展示关注功能
	public static final String IS_SHOW_EVENT_EXTEND_INFO = "isShowEventExtendInfo";//是否展示拓展表字段--预留暂未使用
	public static final String IS_SHOW_EVENT_EXTEND_INFO_DEFAULT = "0";//是否展示拓展表字段-默认值1：展示；0：不展示--预留暂未使用
	public static final String IS_TASK_ALL_UNFOLDED = "isTaskAllUnfolded";//事件处理环节信息是否全部展开显示，true为全部展开
	public static final String DEFAULT_ORDER_BY = "defaultOrderBy";//事件列表默认排序
	public static final String PRIVATE_FOLDER_NAME4_EVENT="privateFolderName4Event";//个性事件页面路径配置，如：/jinjiang
	public static final String DESCRIP_CHARACTER_LIMIT = "eventDescriCharacterLimit";//事件描述最小字符限制
	public static final String IS_SHOW_PLATFORM_SELECT = "isShowPlatformSelect";//是否展示事件列表平台来源下拉框
	public static final String IS_SHOW_ORG_SELECT = "isShowOrgSelect";//是否展示事件列表经办组织下拉框
	public static final String REMOVE_DICT_CODE = "removeDictCode";//使用时可在触发条件后增添上父级字典编码，用以区分是要移除哪个字典下的哪一项，格式为字典编码:是否移除; 如B591001002:true; 表示需要移除字典项B591001002
	public static final String IS_SHOW_SUPERVISION_TYPE = "isShowSupervisionType";//督办页面是否显示督办类别
	public static final String REMOT_SORT_GRID_LEVEL = "remotSortGridLevel";//支持远程排序的网格层级，多个值使用英文逗号进行分割
	//============================事件展示相关设置 end=================================================
	
	//============================时限申请设置 begin=================================================
	public static final String TIME_APPLICATION_ATTRIBUTE = "TIME_APPLICATION_ATTRIBUTE";
	public static final String TIME_APPLICATION_SERVICE = "timeApplicationService";//时限申请处理类
	public static final String LIST_APPLICATION_TYPE = "listApplicationType_";//审核列表、申请列表可查询的类别
	//============================时限申请设置 end===================================================
	
	//============================与第三方对接使用 begin=================================================
	public static final String THREE_PART_JOINT_UNOIN = "THREE_PART_JOINT_UNOIN";//对接使用配置的功能编码
	public static final String POSITION_NAME_FUNCODE = "POSITION_NAME_JOINT";//用户职位名称
	public static final String CLOSE_NODE_NAME_TRIGGER = "closeNodeName";//结案节点名称触发条件
	public static final String IS_REMOVE_NODE_NAME_TRIGGER = "isRemoveNodeName";//用于判断是否需要移除节点名称 true为需要移除；配合REMOVE_NODE_NAME_TRIGGER使用
	public static final String REMOVE_NODE_NAME_TRIGGER = "removeNodeNames";//需要移除的节点名称 与bizPlatform相关联
	public static final String DEFAULT_EVENT_TYPE_TRIGGER = "defaultEventType";//默认事件类别
	public static final String DEFAULT_RECEIVE_USER_TRIGGER = "defaultReceiveUser";//默认接收人员
	public static final String DEFAULT_RECEIVE_ORG_TRIGGER = "defaultReceiveOrg";//默认接收人员所属组织
	public static final String DEFAULT_REPORT_ADVICE_TRIGGER = "defaultReportAdvice";//默认上报事件办理意见触发条件
	public static final String DEFAULT_SHUNT_ADVICE_TRIGGER = "defaultShuntAdvice";//默认分流事件办理意见触发条件
	public static final String DEFAULT_REJECT_ACTIVITY_NAME_TRIGGER = "defaultRejectActivityName";//默认驳回环节名称
	public static final String DEFAULT_REPORT_ACTIVITY_NAME_TRIGGER = "defaultReportActivityName";//默认上报环节名称
	public static final String DEFAULT_CURRENT_ACTIVITY_NAME_TRIGGER = "defaultCurrentActivityName";//默认当前环节名称
	public static final String DEFAULT_CURRENT_USER_ID_TRIGGER = "defaultCurrentUserId";//默认当前事件办理人员id
	public static final String DEFAULT_CURRENT_GROUP_ID_TRIGGER = "defaultCurrentGroupId";//默认当前事件办理人员所属组织id
	public static final String DEFAULT_CURRENT_ORG_ID_TRIGGER = "defaultCurrentOrgId";//默认当前事件办理组织id
	public static final String DICT_PCODE_TRIGGER = "dictPcode";//对接双方转换字段使用的字典pcode
	public static final String BIZ_PLATFORM_PCODE = "B563";//事件对接时，事件对接平台字典pcode
	public static final String BIZ_PLATFORM_INVALID = "000";//无效的对接平台
	public static final String EFFECT_12345 = "EFFECT_12345";//迪艾斯12345便民服务对接生效
	public static final String XB_ICT = "XB_ICT";//西滨对接
	public static final String EFFECT_AH = "EFFECT_AH";//安海对接生效
	public static final String EFFECT_NW = "EFFECT_NW";//南威市级平台对接生效
	public static final String DS_WS_URL = "DS_WS_URL";//迪艾斯便民服务中心12345对接webservice服务地址
	public static final String DS_WS_URI = "DS_WS_URI";//
	public static final String WX_WS_URI = "WX_WS_URI";//微信对接webservice服务地址
	public static final String ML_WS_URI = "ML_WS_URI";//梅岭对接webservice服务地址
	public static final String XB_WS_URL = "XB_WS_URL";//西滨对接webservice服务地址
	public static final String AH_WS_URL = "AH_WS_URL";//安海对接webservice服务地址
	public static final String NW_WS_URL = "NW_WS_URL";//南威市级平台对接webservice服务地址
	public static final String NW_WS_URL_TASK = "NW_WS_URL_TASK";//南威市级平台对接webservice服务地址
	//======================oauth constants===============================
    public static final String PASSWORD_TYPE_SHORT = "003";//短期密码类型
    public static final String PASSWORD_TYPE_CYCLE = "002";//定期修改密码类型
    public static final String PASSWORD_TYPE_EVER = "001";//永久密码类型EVENT_DOCKING
	public static final String EVENT_DOCKING_ORG = "B567";//事件对接组织转换
	public static final String EVENT_DOCKING_TYPE = "B583";//事件对接类型转换
	public static final String PLATFORM_USERNAME = "PLATFORM_USERNAME";//平台对接用户名
	public static final String PLATFORM_PASSWORD = "PLATFORM_PASSWORD";//平台对接密码
    //============================与第三方对接使用 end===================================================
	
	//============================民众诉求 begin===================================================
	public static final String APPEAL_TYPE_PCODE = "A001093088";//诉求类型父级字典编码
	public static final String APPEAL_HANDLE_SIT_PCODE = "A001093089";//诉求处理情况父级字典编码
	public static final String APPEAL_SOURCE_PCODE = "A001093087";//诉求来源父级字典编码
	public static final String APPEAL_MAPPING_PCODE = "B915007";//诉求类型与事件类型映射父级字典编码
	public static final String OPEN_RESTRICTION = "openRestriction";//诉求列表是否打开地域限制
	//============================民众诉求end===================================================
    
    public static String SESSION_TOKEN_KEY = "SESSION_TOKEN_KEY";
    
    /**
	 * 违法建设性质类型在数据字典中编码
	 */
	public static final String CTILLEGAL_TYPE = "B557";
	
	//是否可上传处理中的图片
	public static final String IS_UPLOAD_HANDLING_PIC = "IS_UPLOAD_HANDLING_PIC";
	
	/**
	 * 是否启用新的事件办理页面
	 */
	public static final String IS_USER_NEW_HANDLE_EVENT = "IS_USER_NEW_HANDLE_EVENT";
	/**
	 * 是否新窗口打开
	 */
	public static final String IS_OPEN_NEW_WINDOWS = "IS_OPEN_NEW_WINDOWS";
	/**
	 * 启用新的事件办理页面的时间
	 */
	public static final String TIME_TO_USE_NEW_HANDLE_EVENT = "TIME_TO_USE_NEW_HANDLE_EVENT";
	
	/**
	 * 设置事件属性过滤条件
	 */
	public static final String EVENT_ATTRIBUTE = "EVENT_ATTRIBUTE";
	/**
	 * 设置审核列表事件属性过滤条件
	 */
	public static final String EVENT_VERIFY_ATTRIBUTE = "EVENT_VERIFY_ATTRIBUTE";
	public static final String REMOVE_BIZPLATFORM = "REMOVE_BIZPLATFORM";
	  /**
     * 审核列表查询配置
     */
    public static final String BIZ_PLATFORM_4_QUERY = "bizPlatform4Query";
    
	// 事件处理类
	public static final String EVENT_HANDLE = "EVENT_HANDLE";
	public static final String PRIVATE_FOLDER_NAME4_EVENT_VERIFY = "privateFolderName4EventVerify";
	
	
	// 地图标注操作
	public static final int ADD_MARKER = 0;		// 添加标注
	public static final int EDIT_MARKER = 1;	// 编辑标注
	public static final int WATCH_MARKER = 2;	// 查看标注
	public static final int TITLE_MARKER = 3;	// 通过title展示提示信息
	
	//功能配置：“是否是否同时使用二维和三维地图”的功能编码
	public static String IS_USE_TWO_TYPES_MAP = "IS_USE_TWO_TYPES_MAP";
	
	//新经济组织在表T_ZY_RES_MARKER中的类型
	public static String NON_PUBLIC_ORG_MARKER_TYPE = "01B029";
	//新社会组织在表T_ZY_RES_MARKER中的类型
	public static String ORGANIZATION_MARKER_TYPE = "01B032";
	
	//出租屋在表T_ZY_RES_MARKER中的类型
	public static final String MARKER_TYPE_ROOM_RENT = "ROOM_RENT";   //出租屋
	
	/**
	 * 组织专业编码
	 */
	public static final String GOV_PROFESSION_CODE = "zfgl";	//政府管理专业
	
	//组织层级
	public static final String UNITED_PREVENTION_GROUP_ORG_LEVEL = "7";//联防组网格组织层级
	public static final String GRID_ORG_LEVEL = "6";		//网格组织层级
	public static final String COMMUNITY_ORG_LEVEL = "5";	//社区组织层级
	public static final String STREET_ORG_LEVEL = "4";		//街道组织层级
	public static final String DISTRICT_ORG_LEVEL = "3";	//区组织层级
	public static final String MUNICIPAL_ORG_LEVEL = "2";	//市组织层级
	public static final String PROVINCE_ORG_LEVEL = "1";	//省组织层级
	
	/**
	 * 事件子状态
	 */
	public static final String ACCEPT_STATUS = "00";			//事件子状态——已受理
	public static final String HANDLING_STATUS = "01";		//事件子状态——处理中
	public static final String CONFIRMING_STATUS = "02";	//事件子状态——办结待核实
	public static final String REJECT_SUB_STATUS = "03";		//事件子状态——驳回
	public static final String RECALL_SUB_STATUS = "04";	//事件子状态——撤回
	public static final String PREPRESS_REJECT_STATUS = "05";//事件子状态——预处理驳回
	public static final String PREPRESS_PASS_STATUS = "06";//事件子状态——预处理通过

	public static final String PUBLIC_APPEAL_HANDLESIT_01 = "1";	//待处理
	public static final String PUBLIC_APPEAL_HANDLESIT_02 = "2";	//审核中
	public static final String PUBLIC_APPEAL_HANDLESIT_03 = "3";	//已处理
	public static final String PUBLIC_APPEAL_HANDLESIT_04 = "4";	//已驳回

	/************************************设置新事件工作流相关过滤条件 开始******************/
	/**
	 * 思明区工作流相关配置
	 */
	public static final String WORKFLOW_ATTRIBUTE = "WORKFLOW_ATTRIBUTE";
	//总网格长职位配置 触发条件
	public static final String GRID_ADMIN = "gridAdmin";
	public static final String GRID_ADMIN_DUTY = "gridAdmin_duty";
	//事件办理页面配置 触发条件
	public static final String HANDLE_EVENT_PAGE = "handleEventPage";
	//IEventDisposalWorkflowService实现类配置 触发条件
	public static final String EVENT_WORKFLOW = "eventWorkflow";
	/**Event4WorkflowMapper接口全路径配置 触发条件*/
	public static final String EVENT_4_WORKFLOW_MAPPER = "event4WorkflowMapper";
	//IDrugEnforcementEventService实现类配置 触发条件
	public static final String DRUG_ENFORCEMENT_EVENT_WORKFLOW = "drugEnforcementEventWorkflow";
	//IFiveKeyElementService实现类配置 触发条件
	public static final String FIVEKEY_ELEMENT = "fiveKeyElement";
	//事件工作流名称 触发条件
	public static final String WORKFLOW_NAME = "workflowName";
	//决策节点决策类 触发条件
	public static final String DECISION_MAKING_SERVICE = "decisionMakingService";
	//可以启动流程的职能部门的专业编码 触发条件 配置值格式为：专业编码1,专业名称1;专业编码2,专业名称2;
	public static final String PROFESSION_CODE_4_START = "professionCode4Start";
	
	/**
	 * 江西省南昌市南昌县工作流相关配置
	 */
	//IWorkflowDecisionMakingService 事件状态决策类配置 触发条件
	public static final String EVENT_STATUS = "eventStatus";
	//事件评价人员 userId1,orgId1;userId2,orgId2; 触发条件
	public static final String EVALUATE_USER_ORG_ID = "evaluateUserOrgId";
	//处理中环节任务业务类型
	public static final String HANDLING_TASK_TYPE = "handling";
	//处理中环节编码
	public static final String HANDLING_TASK_CODE = "task0";
	//处理中环节名称
	public static final String HANDLING_TASK_NAME = "处理中";
	//受理环节编码
	public static final String ACCEPT_TASK_CODE = "taskAccept";
	//受理环节名称
	public static final String ACCEPT_TASK_NAME = "受理";
	//协同办理环节编码
	public static final String COORDINATE_TASK_CODE = "taskCoordinate";
	//协同办理环节名称
	public static final String COORDINATE_TASK_NAME = "协同处理";
	/**
	 * 事件预处理环节编码
	 */
	public static final String PREPRESS_TASK_CODE = "taskPrepress";
	/**
	 * 事件预处理环节名称
	 */
	public static final String PREPRESS_TASK_NAME = "预处理";
	
	/**
	 * 延平大联动工作流相关配置
	 */
	public static final String DECISION_RESULT_TASK_NAME = "decisionResultTaskName";//决策2优先使用环节名称 "decisionResultTaskName_" + 流程图名称 + "_" + bizPlatform
	public static final String IS_SHOW_HANDLING_TASK = "isShowHandlingTask";//是否展示处理中环节
	
	/************************************设置新事件工作流相关过滤条件 结束******************/
	
	/**
	 * 事件转派——转派按钮功能配置编码
	 */
	public static final String IS_TRANSFER_ABLE = "IS_TRANSFER_ABLE";
	
	/**
	 * 精准扶贫地图定位类型常量
	 */
	public static final String LOW_INCOME_H = "LOW_INCOME_H"; //低保户
	public static final String POOR_H = "POOR_H";//贫困户
	public static final String SUPPORT_H = "SUPPORT_H";//政策保障户
	public static final String NON_POOR_H = "NON_POOR_H"; //非贫困户
	
	/**
	 * 命案防控
	 */
	public static final String HOMICIDE_CASE_BIZ_CODE = "05";//命案防控编号配置管理编号类型
	public static final String GENDER_PCODE = "B153";//性别字典pcode
	public static final String PROFESSION_TYPE_PCODE = "B265";//职业类别字典pcode
	public static final String NATIONALITY_PCODE = "B113";//国籍字典pcode
	public static final String NATION_PCODE = "B162";//国籍字典pcode
	public static final String NATION_RS_PCODE = "D177003";//国籍字典pcode
	public static final String CARD_TYPE_PCODE = "B010";//证件类型字典pcode
	public static final String MARRIAGE_PCODE = "B151";//婚姻状况字典pcode
	public static final String POLITICS_PCODE = "B118";//政治面貌字典pcode
	public static final String EDUCATION_PCODE = "B064";//学历字典pcode
	public static final String RELIGION_PCODE = "B168";//宗教信仰字典pcode
	public static final String PEOPLETYPE_PCODE = "B416";//人员类别字典pcode
	
	// 消防队
	public static final String MARKER_TYPE_FIRE_XF_XY = "0701001";   //现役消防队
    public static final String MARKER_TYPE_FIRE_XF_YW = "0701002";   //义务消防队
    public static final String MARKER_TYPE_FIRE_XF_ZY = "0701003";   //志愿消防队
    
    // 消防水池
    public static final String MARKER_TYPE_FIRE_POOL = "0605";
    
    public static final String FLOW_CODE = "591_17";
    
    /**
	 * 晋江市信息域编码
	 */
	public static String JINJIANG_INFO_ORG_CODE="350582";
	
	/**
	 * 平潭信息域编码
	 */
	public static String PINGTAN_INFO_ORG_CODE="3510";

	/**
	 * 江西南昌县
	 */
	public static String NANCHANG_FUNC_ORG_CODE="3601";

	/**
	 * 福建省南平市
	 */
	public static String NANPING_INFO_ORG_CODE="3507";


	/**
	 * 罗坊信息域编码
	 */
	public static String LUOFANG_INFO_ORG_CODE="360502007";

	//是否启用国标
	public static final String ENABLE_GB = "ENABLE_GB";
	// 重大涉事安全案件、涉及线路事件、涉及师生安全事件
	public static final String MAP_TYPE_SAFE_EVENT = "SAFE_EVENT";
	// 消防对接功能配置的功能编码
	public static final String FIRE_CONTROL_DOCKING_URL = "FIRE_CONTROL_DOCKING_URL";
	// 消防对接功能配置的设置值0（甘肃消防对接url：FIRE_URL_GS）
	public static final String FIRE_URL_GS = "FIRE_URL_GS";
	// 消防对接功能配置的设置值1（社区微型消防站：COMMUNITY_FIRE_STATION）
	public static final String COMMUNITY_FIRE_STATION = "COMMUNITY_FIRE_STATION";
	// 消防对接功能配置的设置值2（重点微型消防站：KEY_MINI_FIRE_STATION）
	public static final String KEY_MINI_FIRE_STATION = "KEY_MINI_FIRE_STATION";
	// 消防对接功能配置的设置值3（消防关键部件：FIRE_KEY_COMPONENTS）
	public static final String FIRE_KEY_COMPONENTS = "FIRE_KEY_COMPONENTS";
	// 消防对接功能配置的设置值4（查看小网格检查、宣传轨迹页面：FIRE_CONTROL）
	public static final String FIRE_CONTROL = "FIRE_CONTROL";
	// 消防对接功能配置的设置值5（小网格检查、宣传轨迹对应的teamId：FIRE_CONTROL_TEAMID）
	public static final String FIRE_CONTROL_TEAMID = "FIRE_CONTROL_TEAMID";
	// 消防对接功能配置的设置值6（消防关键部件teamId：FIRE_KEY_COMPONENTS_TEAMID）
	public static final String FIRE_KEY_COMPONENTS_TEAMID = "FIRE_KEY_COMPONENTS_TEAMID";
	// 消防对接功能配置的设置值7（KEY_MINI_FIRE_STATION对应的teamId：KEY_MINI_FIRE_STATION_TEAMID）
	public static final String KEY_MINI_FIRE_STATION_TEAMID = "KEY_MINI_FIRE_STATION_TEAMID";
	// 消防对接功能配置的设置值8（社区微型消站对应的teamId：COMMUNITY_FIRE_STATION_TEAMID）
	public static final String COMMUNITY_FIRE_STATION_TEAMID = "COMMUNITY_FIRE_STATION_TEAMID";
	// 消防对接功能配置的设置值9（社区微型消防站对用的flag：COMMUNITY_FIRE_STATION_FLAG）
	public static final String COMMUNITY_FIRE_STATION_FLAG = "COMMUNITY_FIRE_STATION_FLAG";
	// 消防对接功能配置的设置值10（重点微型消防站对应的flag：KEY_MINI_FIRE_STATION_FLAG）
	public static final String KEY_MINI_FIRE_STATION_FLAG = "KEY_MINI_FIRE_STATION_FLAG";

	// 舆情详情链接功能配置的功能编码
	public static final String PUBLIC_SENTIMENT = "PUBLIC_SENTIMENT";
	// 舆情详情链接功能配置的设置值
	public static final String SENTIMENT_URL = "SENTIMENT_URL";

	//是否启用语音盒呼叫功能配置的功能编码
	public static final String IS_USER_YYHHJ ="IS_USER_YYHHJ" ;
	//是否启用发送短信功能配置的功能编码
	public static final String IS_USER_FSDX ="IS_USER_FSDX" ;
	//是否启用视频通话功能配置的功能编码
	public static final String IS_USER_MMP ="IS_USER_MMP" ;
	//是否启用ANYCHAT视频通话功能配置的功能编码
	public static final String IS_USER_ANYCHAT ="IS_USER_ANYCHAT" ;
	
	//是否启用mediasoup视频通话功能配置的功能编码
	public static final String IS_USE_MEDIASOUP ="IS_USE_MEDIASOUP" ;
	//是否启用软电话功能配置的功能编码
	public static final String IS_USER_RDHHJ = "IS_USER_RDHHJ";
	//语音视频 服务器地址
	public static final String MMP_SVRIP="tcp://sm?27.155.64.203:7109//mmp?27.155.64.203:82//";
	//语音视频 VAG地址
	public static final String MMP_MEDIAURL="27.155.64.203:655";
	
	public static final String MMP_OCX_URL="27.155.64.203:655";

	/**
	 * 甘肃省功能域组织编码
	 * 62
	 */
	public static String GANSU_FUNC_ORG_CODE="62";

	/**
	 * 市政消防栓
	 */
	public static String SHIZHENG_XIAOFANGSHUAN = "02010601";

	/**
	 * 市政设施地图标注类型
	 */
	public static String RES_DEFAULT = "RES_DEFAULT";
	
	//禁毒事件
	/**
	 * 社会毒情
	 */
	public static String DRUG_SOCIAL_SITUATION = "B178";
	/**
	 * 禁毒事件事件内容
	 */
	public static String DRUG_EVENT_CONTENT = "B164";

	/**
	 * 网格各层级显示中心点名称的地图层级设置
	 */
	public static String SHOW_WG_POINT_MAP_LEVEL_CODE = "SHOW_WG_POINT_MAP_LEVEL";//功能编码
	public static String MAP_LEVEL_TRIG_CONDITION_PROVINCE = "PROVINCE";//省
	public static String MAP_LEVEL_TRIG_CONDITION_CITY = "CITY";//市
	public static String MAP_LEVEL_TRIG_CONDITION_COUNTY = "COUNTY";//区县
	public static String MAP_LEVEL_TRIG_CONDITION_STREET = "STREET";//街道
	public static String MAP_LEVEL_TRIG_CONDITION_COMMUNITY = "COMMUNITY";//社区
	public static String MAP_LEVEL_TRIG_CONDITION_GRID = "GRID";//网格


	//是否显示当前层级网格的轮廓
	public static String SHOW_CURRENT_GRID_LEVEL_OUTLINE = "SHOW_CURRENT_GRID_LEVEL_OUTLINE";
	
	// 城市综管  水质量
	public static final String MAP_TYPE_ZG_WATER = "ZG_WATER";
	// 城市综管   水质量 水质类别
    public static final String  ZG_WATER_TYPE = "S007001001";
    
	// 城市综管   告警类型
    public static final String  ZG_ALARM_TYPE = "S009001";
    // 城市综管   告警级别
    public static final String  ZG_ALARM_LEVEL = "S009002";
    /**
     * 事件审核状态字典
     */
    public static final String EVENT_VERIFY_STATUS_PCODE = "B195";
    //设备-路灯
    public static final String DEVICE_LIGHTING="100011";
    
    //设备-环境监测  EnvMonitoring
    public static final String DEVICE_ENVMONITORING="100017";
    
   //设备-门禁
    public static final String DEVICE_ACCESS_CONTROL="100009";
    
  //设备-车辆识别
    public static final String DEVICE_CAR_CONTROL="100010";
    
    //设备-充电桩
    public static final String DEVICE_CHARGINGPILE="100012";

	/**
	 * 南平延平区功能域组织编码
	 */
	public static String YANPING_FUNC_ORG_CODE="350701";
	
	/**
	 * 空气质量抓取城市
	 */
	
	public static String  AQI_CITY_STATION_ID="jiangyin";//默认
	
	public static String DICT_DEVICE_TYPE = "B572";//设备类型
	public static String DICT_DEVICE_MANU = "B573";//设备厂商
	
	public static String REPAIRTASK_FAULT_TYPE = "B199";//报修类型
	
	public static String MWWLW_GLY = "马尾物联网管理员";//
	public static String MWWLW_CZY = "马尾物联网处置员";//
	/**
	 * 模块使用地图类型的功能配置编码
	 */
	public static String MAP_TYPE_CODE = "MAP_TYPE_CODE";

	/**
	 * 业务模块代码与对应的地图图层关联功能配置
	 */
	public static String MODULE_MAP_MENU_CODE_CONFIG = "MODULE_MAP_MENU_CODE_CONFIG";
	
	

	/**
	 * 单位问题台账
	 */
	//单位级别
	public static final String UNIT_LEVEL = "B590008";
	//纪律处分
	public static final String DISCIPLINARY_PUNISHMENT = "B590015";
	
	//问题处置类型
	public static final String PROC_TYPE = "B590010";
	
	//违纪违规资金类别，字典编码为：B590002
  	public static final String VIOLATION_MONEY_TYPE = "B590002";	
	/**
	 * 个人问题台账
	 */
    //政治面貌
  	public static final String POLITICS  = "B590006";
	//违纪人员职级
	public static final String PROFESSION_TYPE  = "B590007";
    //党纪处分，字典编码为：B590012001
  	public static final String PARTY_FLAG  = "B590012001";
	
	//政纪处分，字典编码为：B590012002
	public static final String DISCIPLINE_FLAG = "B590012002";
	
	//组织处理类型，字典编码为：B590013
    public static final String ORG_PROC_TYPE = "B590013";
    
 
  		
  	//违规违纪违法类别，字典编码为：B590003
  	public static final String VIOLATION_TYPE = "B590003";
  	//扫黑除恶类类，字典编码为：B590003000
  	public static final String VIOLATION_TYPE_0 = "B590003000";
  	//违规违纪类，字典编码为：B590003000
  	public static final String VIOLATION_TYPE_1 = "B590003001";
  	//作风建设类类，字典编码为：B590003
  	public static final String VIOLATION_TYPE_2 = "B590003002";
  	
  	//四种形态，字典编码为：B590014
  	public static final String SHAPE = "B590014";

	//公共厕所
	public static final String COMFORT_STATION_TYPE_CODE = "02010301";
	//路灯
	public static final String STREET_LAMP_TYPE_CODE = "02010501";
	//消防栓
	public static final String HIRE_HYDRANT_TYPE_CODE = "02010601";
	//公交车站
	public static final String BUS_STATION_TYPE_CODE = "02020401";
	//全球眼
	public static final String GLOBAL_EYE_TYPE_CODE = "02020501";

	public static final String MARKER_TYPE_FIRE_SN = "0601";   //室内消防栓
	public static final String MARKER_TYPE_FIRE_SW = "0602";   //室外消防栓
	public static final String MARKER_TYPE_FIRE_TR = "0603";   //天然水源
	public static final String MARKER_TYPE_FIRE_ZL = "0604";   //自来水公司
	public static final String MARKER_TYPE_FIRE_PL = "0605";   //消防水池
	public static final String MARKER_TYPE_FIRE_XF = "0701";   //消防队
	
	
	//综治网格解决跨域的中间方法
	public static String iframeUrl2 = "/zzgl/eventReport/isDomain.jhtml?";
	
	/**
	 * 影响范围在数据字典中编码 B466
	 */
	public static String SCOPE_INFLUNECE = "B466";
	
	/**
	 * 事件性质在数据字典中编码 B467
	 */
	public static String EVENT_NATURE = "B467";
	
	/**
	 * 矛盾纠纷类型在数据字典中编码 B043
	 * 
	 */
	public static String DISPUTE_TYPE_CODE = "B043";
	
	/**
	 * 矛盾纠纷调处方式在数据字典中编码 B038
	 */
	public static String MEDIATION_TYPE_CODE = "B038";
	/**
	 * 南安个性化矛盾纠纷获取详情使用代理
	 */
	public static final String SHOW_DISPUTE_DETAIL_AGENT = "SHOW_DISPUTE_DETAIL_AGENT";
	/**
	 * 活动管理类型在数据字典中编码 B544
	 */
	public static String ACTIVITY_TYPE_CODE = "B544";
	/**
	 * 事件规模在数据字典中编码 B036
	 */
	public static String DISPUTE_SCALE_CODE = "B036";
	
	/**
   	 * 矛盾纠纷附件类型
   	 */
   	public static final String DISPUTE_ATTACHMENT_TYPE = "dispute_attachment_type";
	/**
	 * 市政设施-井盖
	 */
	public static final String COLUMN_ROAD_ATTACHMENT_TYPE = "transparent";
   	// 地图标注--矛盾纠纷
 	public static String DISPUTE_MEDIATION = "DISPUTE_MEDIATION";
 	
 	//功能配置：是否显示涉台
 	public static String DISPLAY_TAIWANG_FLAG = "DISPLAY_TAIWANG_FLAG";
 	/**
	 * 矛盾纠纷状态：上报
	 */
	public static final String DISPUTESTATUS_REPORT = "2"; //上报
	/**
	 * 矛盾纠纷状态：结案
	 */
	public static final String DISPUTESTATUS_CLOSE = "3"; //结案
	
	// 地图标注--新经济组织模块代码
	public static String NEW_NONPUBLICORG_CODE = "NEW_ECONOMY_ORG";
	// 地图标注--活动管理
	public static String DAILY_ACTIVITY = "ACTIVITY";
	// 地图标注--室外消防栓模块代码
	public static String FIRERES2 = "FIRERES2";
	// 地图标注--天然水源模块代码
	public static String FIRERES3 = "FIRERES3";
	// 地图标注--自来水公司模块代码
	public static String FIRERES4 = "FIRERES4";
	// 地图标注--消防队模块代码
	public static String FIRETEAM = "FIRETEAM";
	// 地图标注--消防水池
	public static String FIRE_POOL = "FIRE_POOL";
	// 地图标注--通用事件
	public static String GENERAL_EVENT = "GENERAL_EVENT";
	
	public static final String MARKER_TYPE_PLACEINFO = "PLACE"; //场所
	// 地图标注--新社会组织模块代码
	public static String NEW_ORGANIZATION_CODE = "NEW_SOCIAL_ORG";
	// 地图标注--新消防队模块代码
	public static String FIRETEAM_NEW = "FIRETEAM_NEW";
	
	// 地图标注--全球眼模块代码
	public static String GLOBAL_EYES = "GLOBALEYES";
	// 地图标注--校园周边模块代码
	public static String CAMPUS_AROUND = "CAMPUS";
	// 地图标注--城市道路模块代码
	public static String ROAD_MANAGE = "RES_ROAD";
	// 地图标注--旧事件模块代码
	public static String EVENT_V0 = "EVENT_V0";
	// 地图标注--出租屋模块代码
	public static String ROOM_RENT = "ROOM_RENT";
    // 地图标注--城市部件模块代码
    public static String URBAN_OBJ = "020130";
    // 地图标注--城市部件模块代码
    public static final String URBAN_TYPE_PCODE = "D005011";
    // 地图标注--安全检查模块代码
 	public static String SAFE_CHECK = "SAFE_CHECK";
 	public static final String MARKER_TYPE_DISPUTE_MEDIATION = "DISPUTE";   //矛盾纠纷
 	public static final String MARKER_TYPE_GENERAL_EVENT = "01Z030";   //通用事件
	public static final String MARKER_TYPE_FIRE_CONTROL = "01Z031";   //消防火灾
	//施工环境保障上报成的事件归档回写服务配置触发条件
	public static String DISPUTE_EVENT_TYPE = "DISPUTE_EVENT_TYPE";
	
	/**
	 * 矛盾纠纷状态：驳回
	 */
	public static final String DISPUTESTATUS_BOHUI = "5"; //驳回
	
	/**
	 * 矛盾纠纷状态：受理
	 */
	public static final String DISPUTESTATUS_ACCEPT = "1"; //未受理
	
	
	//事件审核表作废状态编码
	public static final Object IS_INVALID = "08";
	
	/**
	 * 厦门市思明区功能域组织编码
	 */
	public static String SIMING_FUNC_ORG_CODE="350201";
	
	/**
	 * 风格样式
	 */
	
	public static String UI_STYLE="UI_STYLE";
	
	public static String GRID_ADMIN_WITH_WORK_FLAG = "GRID_ADMIN_WITH_WORK_FLAG";
	//点位信息地图TYPE常量
	public static String POINT_INFO_TYPE = "point";
	
	public static String MAP_ENGINE_NAME = "arcgis2D";
	/**
	 * 南昌点位类型
	 */
	public static String MAP_POINT_TYPE_CODE = "D236002";
	
	
	//网格树展示区域功能
    public static final String GRID_CONFIGURATION = "GRID_CONFIGURATION";//通过此配置获取列表网格树展示方式
	public static final String GRID = "GRID";//通过网格树展示方式触发条件
	
	
	public static final String STATUS_DICTCODE = "B497";//发布状态字典

	public static final String JIANHU_FUNC_ORG_CODE = "320925";//发布状态字典

	/**
	 * 网格员协作- 协作类型字典编码
	 */
	public static final String SEND_TYPE_DICTCODE = "B12322001";
	 /**
	 * 法院协作办公- 申请类型字典编码
	 */
	public static final String APPLY_TYPE_DICTCODE = "B12322002";
	 /**
	 * 法院协作办公- 满意度字典编码
	 */
	public static final String SATISFACTION_DICTCODE = "B12322003";
	/**
	 * 事件标签所属业务类型字典父级编码
	 */
	public static final String LABEL_BIZ_TYPE = "B596";
	/**
	 * 事件标签问题上报模块编码
	 */
	public static final String LABEL_EVENT_PROBLEM_REPORT = "001";
	/**
	 * 事件标签巡防事件发生类型模块编码
	 */
	public static final String LABEL_EVENT_PATROL_TYPE = "002";
	/**
	 * 布控库类型
	 */
	public static final String LIB_TYPE = "A002004008001";

	/**
	 * 民族
	 */
	public static final String NATIONALITY = "D177003";

	/**
	 * 布控任务类型
	 */
	public static final String MONITOR_TASK_TYPE = "A002004008002";

	/**
	 *报警推送方式
	 */
	public static final String ALARM_MODE = "A002004008003";

	/**
	 *任务多重方式
	 */
	public static final String REPEAT_MODE = "A002004008004";

	/**
	 *布控任务状态
	 */
	public static final String TASK_TYPE = "A002004008005";

	/**
	 *报警存储时间
	 */
	public static final String EXPIRE_TYPE = "A002004008006";

	/**
	 *是否忽略报警状态
	 */
	public static final String IGNORE_STATUS = "A002004008007";

	/**
	 * 警情处理状态
	 */
	public static final String ALARM_OPERATION_TYPE = "A002004008008";

	/**
	 * 报警类型
	 */
	public static final String ALARM_TYPES = "A002004008009";
	
	/**
	 * 南安12345对接-来源渠道
	 */
	public static final String  HOTLINE_SOURCES = "B0909002";
	
	/**
	 * 南安12345对接-年龄范围
	 */
	public static final String  HOTLINE_AGE = "B0909003";
	/**
	 * 南安12345对接-诉求类型
	 */
	public static final String  HOTLINE_APPEAL_TYPE = "B0909004";
	
	/**
	 * 南安12345对接-事件状态
	 */
	public static final String  HOTLINE_EVENT_STATUS = "B0909005";
	
	/**
	 * 南安12345对接-事件类型
	 */
	public static final String  HOTLINE_EVENT_TYPE = "A001093199789";
	
	
	/**************************************西藏昌都民生信息模块相关字典配置 开始*********************************/
	//民生信息类型
	public static final String INFO_TYPE_PCODE = "A001135001";
	//民生动态类型
	public static final String INFO_TRENDS_TYPE_PCODE = "A001135002";
	//紧急程度
	public static final String URGENCE_DEGREE_PCODE = "A001135003";
	//信息状态
	public static final String INFO_STATUS_PCODE = "A001135004";
	//信息子状态
	public static final String INFO_SUB_STATUS = "A001135005";
	/**************************************西藏昌都民生信息模块相关字典配置 结束*********************************/
	
	
	/**************************************相似事件匹配规则 开始*********************************/
	//相似事件规则总配置
	public static final String SIMILAR_EVENT_RULE = "SIMILAR_EVENT_RULE";
	//采集时间范围配置
	public static final String SIMILAR_CREATE_TIME = "similarCreateTime";
	/**************************************相似事件匹配规则 结束*********************************/

	/**********************************周宁海康执法仪参数 start *****************************/
	public static String ZN_HK_PLATFORM_IP = "27.151.3.199";
	public static String ZN_HK_PLATFORM_PORT = "1443";
	public static String ZN_HK_PLATFORM_APPKEY = "25735875";
	public static String ZN_HK_PLATFORM_SECRET_KEY = "lh3RqXbHOrqdR5kaKKmr";
	public static String ZN_HK_PLATFORM_EVENTTYPE_GPS = "851969"; //gps
	public static String ZN_HK_PLATFORM_EVENTTYPE_ALARM = "589825";//报警输入
	/**********************************周宁海康执法仪参数 end *******************************/

	/**********************************延平海康执法仪参数 start *****************************/
	public static String YP_HK_PLATFORM_IP = "220.160.226.42";
	public static String YP_HK_PLATFORM_PORT = ZN_HK_PLATFORM_PORT;
	public static String YP_HK_PLATFORM_APPKEY = "28691385";
	public static String YP_HK_PLATFORM_SECRET_KEY = "LC87YGm0HvuBaaH9HIzT";
	/**********************************周宁海康执法仪参数 end *******************************/

    /**********************************三书一函 start *****************************/
    /**
     * 三书一函——文件类型
     */
    public static final String  ELIMINATE_LETTER_THO_LETTER_TYPE = "B301001";
	/**
	 * 三书一函——行业领域
	 */
	public static final String  ELIMINATE_LETTER_THO_INDUS = "B301002";
	/**
	 * 三书一函——整改情况
	 */
	public static final String  ELIMINATE_LETTER_CHG_TYPE = "B301003";
	/**
	 * 三书一函系统编号
	 */
	public static final String  ELIMINATE_LETTER_THO_SYS_CODE = "14";
	/**
	 * 三书一函——流程表单id
	 */
	public static final Long ELIMINATE_LETTER_THO_FORM_ID = 765L;
    /**
     * 三书一函——任务流程图名称
     */
    public static final String ELIMINATE_LETTER_THO_WORKFLOW_NAME = "三书一函流程";
    /**
     * 三书一函——任务流程图代码
     */
    public static final String ELIMINATE_LETTER_THO_WFTYPE_ID = "ssyh_flow";
    /**
     * 三书一函专业编码
     */
    public static String SSYH_PROFESSION_CODE;
    /**
     * 三书一函专业名称
     */
    public static String SSYH_PROFESSION_NAME;
    /**********************************三书一函 end *****************************/



	/**********************************涉黑案件上报 start *****************************/
	/**
	 * 涉黑案件上报——涉案性质
	 */
	public static final String  INVOLVED_NATURE = "B30301";

	/**
	 * 涉黑案件上报——一审状态
	 */

	public static final String  TRIAL_STATUS = "B30302";

	/**
	 * 涉黑案件类型--按地图撒点类型统计
	 */
	public static final String  MAP_STATISTICS = "map";
	/**
	 * 涉黑案件类型--按案件类型统计
	 */
	public static final String  CASE_TYPE_STATISTICS = "type";

	/**
	 * 涉黑案件类型--案件详情
	 */
	public static final String  CASE_TYPE_DETAIL = "detail";
	/**
	 * 涉黑案件类型--案件列表
	 */
	public static final String  CASE_TYPE_LIST = "list";


	/**
	 * 涉黑案件类型--在侦
	 */
	public static final String  CASE_TYPE_ZZ = "zz";

	/**
	 * 涉黑案件类型--在诉
	 */
	public static final String  CASE_TYPE_ZS = "zs";

	/**
	 * 涉黑案件类型--一审
	 */
	public static final String  CASE_TYPE_YS = "ys";
	/**
	 * 涉黑案件类型--二审
	 */
	public static final String  CASE_TYPE_ES = "es";
	/**
	 * 涉黑案件类型--审结
	 */
	public static final String  CASE_TYPE_SJ = "sj";
	/**
	 * 涉黑案件类型--督办
	 */
	public static final String  CASE_TYPE_DB = "db";

	/**
	 * 涉黑案件类型--督办
	 */
	public static final String  CASE_TYPE_ALL = "all";

	/**********************************涉黑案件上报 start *****************************/

}
