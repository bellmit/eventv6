package cn.ffcs.zhsq.base.local.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志模块枚举(常量，对新增开放，对修改关闭)
 */
public enum LogModule{
	
	mapDataForSegmentGrid("005", "MAP_SEGMENT_GRID_GIS", "Segment网格地图数据"),
	mapDataForResMarker("004", "T_ZY_RES_MARKER", "资源标注地图数据"),
	mapDataForBuilding("003", "MAP_WG_JZW_GIS", "建筑物Gis地图数据"),
	mapDataForGrid("002", "MAP_BUS_GRID_GIS", "网格Gis地图数据"),
	mapDataForConfig("001", "MAP_BUS_GRID_GIS", "gis地图配置数据"),
	areaBuildingInfo("AREA_BUILDING_INFO", "T_DC_AREA_BUILDING_INFO", "楼宇基础数据"),
	importCrowd("IMPORT_CROWD", "t_zz_import_crowd", "重点人群基础数据"),
	//csk 增加护路护线案件信息日志
	roadCase("ROAD_CASE","T_ZZ_RELATED_EVENTS","护路护线案件");

	
	private String moduleCode;
	private String tableName;
	private String desc;
	private static List<LogModule> allLogModule= null;
	
	LogModule(String moduleCode, String tableName, String desc){
		this.moduleCode = moduleCode;
		this.tableName = tableName;
		this.desc = desc;
	}
	
	
	public static LogModule getLogModuleByCode(String moduleCode){
		LogModule logModule = null;
		for(LogModule obj :LogModule.values()){
			if(moduleCode==obj.getModuleCode()) logModule = obj;
		}
		return logModule;
	}
	
	public static List<LogModule> getAllLogModule(){
		if(LogModule.allLogModule == null){
			List<LogModule> modulelist = new ArrayList<LogModule>();
			for(LogModule obj :LogModule.values()){
				modulelist.add(obj);
			}
			LogModule.allLogModule = modulelist;
		}
		return LogModule.allLogModule;
	}
	
	public String getModuleCode(){
		return this.moduleCode;
	}
	public String getDesc(){
		return this.desc;
	}
	public String getTableName(){
		return this.tableName;
	}
	
}

