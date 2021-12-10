package cn.ffcs.zhsq.keyelement.service;

/**
 * 节点编码处理接口
 * 
 * @author YangCQ
 * 
 */
public interface INodeCodeHandler {

	public static String ORG_UINT = "U";

	public static String ORG_DEPT = "D";
	/**专项网格*/
	public static String ORG_EXCLUSIVE_GRID = "E";
	
	/**
	 * 操作——上报
	 */
	public static String OPERATE_REPORT = "R";
	/**
	 * 操作——变级上报
	 */
	public static String OPERATE_REPORT_X = "r";
	/**
	 * 操作——下派
	 */
	public static String OPERATE_SEND = "S";
	/**
	 * 操作——分流
	 */
	public static String OPERATE_FLOW = "F";
	/**
	 * 操作——展示全部职能部门
	 * f0展示根节点
	 */
	public static String OPERATE_FLOW_GLOABAL = "f";
	/**
	 * 操作——到指定人员
	 */
	public static String OPERATE_PERSON = "P";
	/**
	 * 操作——到指定组织
	 */
	public static String OPERATE_ORGANIZATION = "O";
	/**
	 * 操作——到采集人员组织
	 */
	public static String OPERATE_COLLECT = "C";
	/**
	 * 操作——到采集人员
	 */
	public static String OPERATE_BEGIN = "B";
	/**
	 * 操作——展示全部组织
	 * G0 包含根节点
	 */
	public static String OPERATE_GLOBAL = "G";
	/**
	 * 操作——配置人员
	 * 读取功能配置中配置的人员信息 功能配置编码：WORKFLOW_ATTRIBUTE
	 */
	public static String OPERATE_ALLOCATION = "A";
	
	/**
	 * 操作——同组织操作
	 * 相同组织间的人员扭转
	 */
	public static String OPERATE_EQUALITY = "E";
	
	/**
	 * 网格
	 */
	public static int GRID = 6;
	/**
	 * 社区
	 */
	public static int COMMUNITY = 5;
	/**
	 * 街道
	 */
	public static int STREET = 4;
	/**
	 * 县区
	 */
	public static int COUNTY = 3;

	/**
	 * 是否是上报
	 * 
	 * @return
	 */
	public boolean isReport();

	/**
	 * 是否是变级上报
	 * @return
	 */
	public boolean isReportX();
	
	/**
	 * 是否是下派
	 * 
	 * @return
	 */
	public boolean isSend();

	/**
	 * 是否是分流
	 * 
	 * @return
	 */
	public boolean isSplitFlow();
	/**
	 * 是否分流全部职能部门
	 * @return
	 */
	public boolean isSplitFlowGlobal();

	/**
	 * 获取线路代码
	 * 
	 * @return -1:无效
	 */
	public String getLineCode();

	/**
	 * 获取线路几级
	 * 
	 * @return -1:无效
	 */
	public int getLineLevel();

	/**
	 * 是否来自单位
	 * 
	 * @return
	 */
	public boolean isFromUnit();

	/**
	 * 是否来自部门
	 * 
	 * @return
	 */
	public boolean isFromDept();

	/**
	 * 是否来自专项网格
	 * @return
	 */
	public boolean isFromExclusiveGrid();
	
	/**
	 * 是否到单位
	 * 
	 * @return
	 */
	public boolean isToUnit();

	/**
	 * 是否到部门
	 * 
	 * @return
	 */
	public boolean isToDept();

	/**
	 * 是否到专项网格
	 * @return
	 */
	public boolean isToExclusiveGrid();
	
	/**
	 * 获取发起端的层级
	 * 
	 * @return -1:无效
	 */
	public int getFromLevel();

	/**
	 * 获取目的端的层级
	 * 
	 * @return -1:无效
	 */
	public int getToLevel();

	/**
	 * 是否是采集
	 * 
	 * @return
	 */
	public boolean isCollect();

	/**
	 * 是否是结案
	 * 
	 * @return
	 */
	public boolean isClose();

	/**
	 * 是否是评价
	 * 
	 * @return
	 */
	public boolean isComment();

	/**
	 * 是否是归档
	 * 
	 * @return
	 */
	public boolean isPlaceFile();

	/**
	 * 是否下派到网格
	 * 
	 * @return
	 */
	public boolean isSendToGrid();

	/**
	 * 是否下派到社区
	 * 
	 * @return
	 */
	public boolean isSendToCommunity();

	/**
	 * 是否上报到社区
	 * 
	 * @return
	 */
	public boolean isReportToCommunity();

	/**
	 * 是否从网格上报到社区
	 * 
	 * @return
	 */
	public boolean isGridReportToCommunity();

	/**
	 * 是否到网格
	 * 
	 * @return
	 */
	public boolean isToGrid();

	/**
	 * 是否到社区
	 * 
	 * @return
	 */
	public boolean isToCommunity();

	/**
	 * 是否来自网格
	 * 
	 * @return
	 */
	public boolean isFromGrid();

	/**
	 * 是否来自社区
	 * 
	 * @return
	 */
	public boolean isFromCommunity();

	/**
	 * 是否是配置到人
	 */
	public boolean isPerson();
	
	/**
	 * 是否是配置到组织
	 */
	public boolean isOrganization();
	
	/**
	 * 是否由采集人员所属组织办理
	 * @return
	 */
	public boolean isToCollect();
	
	/**
	 * 是否由采集人员办理
	 * @return
	 */
	public boolean isToBegin();
	
	/**
	 * 是否展示全部组织
	 * @return
	 */
	public boolean isGlobal();
	
	/**
	 * 是否读取功能配置的人员配置信息
	 * @return
	 */
	public boolean isAllocation();
	
	/**
	 * 是否在同组织间进行人员扭转
	 * @return
	 */
	public boolean isEquality();
}
