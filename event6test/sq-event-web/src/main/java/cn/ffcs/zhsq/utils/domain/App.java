package cn.ffcs.zhsq.utils.domain;

import javax.servlet.http.HttpSession;

import cn.ffcs.zhsq.utils.ConstantValue;


public enum App implements IDomain {
	
	
	/**
	 * 顶级域名
	 */
	TOP("$TOP_DOMAIN", false, "", IDomain.LOCAL), 
	
	/**
	 * 综治网格应用 例:http://gd.fjsq.org/zzgrid
	 * http://gd.fjsq.fj.cegn.cn:8440/zzgrid
	 */
	ZZGRID("$ZZGRID_DOMAIN", false, "", IDomain.REMOTE), 

	/**
	 * 事件应用 例:http://event.fjsq.org/zhsq_event
	 */
	EVENT("$EVENT_DOMAIN", false, "http://local.develop.aishequ.org:8450/zhsq_event", IDomain.REMOTE),
	/**
	 * GBP应用 例:http://GBP.fjsq.org/GBP
	 */
	GBP("$GBP_DOMAIN", false, "", IDomain.REMOTE),

	/**
	 * 智慧城管案件应用 http://scevent.cim.aishequ.org/scevent
	 */
	SCEVENT("$SCEVENT_DOMAIN", false, "", IDomain.REMOTE), 
	
	/**
	 * 系统管理应用 例:http://sysdev.fjsq.org
	 */
	SYSTEM("$SYSTEM_DOMAIN", false, "http://gd.fjsq.org:8301/sq_system_portal", IDomain.REMOTE), 
	/**
	 * 公共样式应用 例:http://static.aishequ.org
	 */
	UI("$UI_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * GMIS应用 例:http://gmis.fjsq.org/gmis
	 */
	GMIS("$GMIS_DOMAIN", false, "http://gd.fjsq.org:10010/sq-gmis-web", IDomain.REMOTE), 
	/**
	 * 人口应用 例:http://rs.fjsq.org
	 */
	RS("$RS_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 网站群 http://zone.sq.aishequ.org
	 */
	ZONE("$ZONE_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 两站两员
	 */
	LZLY("$LZLY_DOMAIN", false, "http://gd.fjsq.org:8484", IDomain.REMOTE),
	/**
	 * 文件服务应用 例:http://img.fjsq.org
	 */
	IMG("$IMG_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 文件服务应用（域名有包含【/zzgrid/】） 例:http://img.fjsq.org/zzgrid/
	 */
	IMG_ZZGRID("$IMG_ZZGRID_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 工作流应用 例:http://workflow.fjsq.org/workflow
	 */
	WORKFLOW("$WORKFLOW_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 旧地图 例:http://map.fjsq.org
	 */
	OLD_GIS_MAP("$OLD_GIS_MAP_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 消息应用 例:http://newsms.fjsq.org
	 */
	SMS("$SMS_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * UAM应用 例:http://uam.ygj.aishequ.org
	 */
	UAM("$UAM_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 数据交换应用 例:http://bdio.v6.aishequ.org
	 */
	BDIO("$BDIO_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 导入导出应用 例:http://bd.fjsq.org
	 */
	IMPEXP("$IMPEXP_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 资源应用 例:http://res.fjsq.org
	 */
	RESOURCE("$RESOURCE_DOMAIN", false, "", IDomain.REMOTE), 
	/**
	 * 报表应用 例:http://bi.fjsq.org
	 */
	BI("$BI_DOMAIN", false, "http://gd6.v6.aishequ.org:8088", IDomain.REMOTE),
	/**
	 * 水纹应用 例:http://water.fjsq.org/water
	 */
	WATER("$WATER_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 楼宇经济应用 例:http://lyjj.fjsq.org:8080/lyjj
	 */
	LYJJ("$LYJJ_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 消防网格应用 例:http://fire.fjsq.org:8080/fire
	 */
	FIREGRID("$FIREGRID_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 工商网格应用 例:http://icgrid.fjsq.org:8080/icgrid
	 */
	ICGRID("$ICGRID_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 平安建设的路径
	 */
	PAJS("$PAJS_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 标准地址库应用 例:http://gd.fjsq.org:9601/geo
	 */
	GEO("$GEO_DOMAIN", false, "http://gd.fjsq.org:9601/geo", IDomain.REMOTE),
	/**
	 * 立体防控应用 例:http://idssdev.fjsq.org
	 */
	IDSS("$IDSS_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 附件上传 http://filedev.fjsq.org
	 */
	SQFILE("$SQ_FILE_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * 
	 */
	SKY("$SKY_DOMAIN", false, "", IDomain.REMOTE),
	/**
	 * oa http://oa.sq.aishequ.org/sq-oa-web
	 */
	OA("$OA_DOMAIN", false, "", IDomain.REMOTE),
	
	/**
	 * GIS工程
	 */
	GIS("$GIS_DOMAIN", false, "", IDomain.REMOTE),
	
	/*
	 *  公共组件工程域名
	 * */
	COMPONENTS("$COMPONENTS_DOMAIN", false, "", IDomain.REMOTE);
	
	
	
	
	private String code;
	private boolean debug;
	private String debugUrl;
	private int type;

	private App(String code, boolean isDebug, String debugUrl, int type) {
		this.code = code;
		this.debug = isDebug;
		this.debugUrl = debugUrl;
		this.type = type;
	}

	@Override
	public String toString() {
		return this.code;
	}

	@Override
	public boolean isDebug() {
		return this.debug;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDomain(HttpSession session) {
		String domain = (String) session.getAttribute(this.code);
		if (!ConstantValue.IS_PRODUCT && this.debug) {
			domain = this.getDebugUrl();
		}
		return domain;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public String getDebugUrl() {
		return this.debugUrl;
	}
	
}
