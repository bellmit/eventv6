package cn.ffcs.zhsq.api.enums;
/**
 * 
 * @Description: 对接平台业务类型枚举类
 * @Author: wuxq
 * @Date: 01-17 20:08:21
 * @Copyright: 2018 福富软件
 *
 */
public 	enum BizPlatfromEnum {
	/**
	 * 命名规则：对接地区_业务编码
	 * 对接业务值：对接地区编码+业务值  如：3507001
	 */
	
	DF_SPT("江苏省平台对接（天阙）","062"), 
	DX_APP("定西APP对接","6212001"),
	DX_WECHAT("定西微信对接","6212002"),//走IEventVerifyBaseService接口
	XS_SPT("响水对接省平台","064"),
	JH_SPT("建湖对接省平台","065"),
	YD_SPT("盐都对接省平台（天阙）","069"),
	KFQ_SPT("开发区对接省平台","070"),
	YD_SYN_12345("盐都拉取12345","077"),
	YC_RQY("盐城五地区全要素对接燃气云平台","320903001"),
	KFQ_SYC_12345("开发区拉取12345","320908001"),
	REPORT_YTH("全要素上报事件至一体化","320904001"),
	BACK_YTH("全要素反馈办结至一体化","320904002"),
	DF_SYN_12345("大丰拉取12345","206"),
	GS_LN("甘肃陇南对接","046"),
	GS_TQ("甘肃反馈信息至公众版APP（天阙）","202004"),
	GS_CG("甘肃城关区城管对接","620102001"),
	GS_QZBL("甘肃群众爆料对接","62001"),
	GS_PAZYZWX("甘肃平安志愿者","62002"),
	KFQ_12345("开发区对接1234","209"),
	XS_12345("响水对接12345","208"),
	YC_12345("大丰对接12345","206"),
	YD_12345("盐都对接12345","206"),
	NC_12345("南昌综治对接12345","044"),
	NC_CG("南昌综治上报至城管平台","072"),
	NC_SJ("南昌综治反馈信息给手机端APP","202001"),
	NC_XCX("南昌高新小程序对接","068"),
	NC_PAZYZ("南昌平安志愿者","202002"),
	JX_PAZYZ("江西省平台对接平安志愿者","36001"),
	JX_TQ("江西省平台对接天阙平台","36002"),
	SR_JX("上饶市对接江西省平台","36003"),
	NC_JX("南昌市对接江西省平台","36004"),
	FLOW_TO_PAZYZ("各地市上报的平安志愿者事件的流程同步至省平安志愿者","36005"),
	GZ_JX("赣州市对接江西省平台","36006"),
	JX_CLOSE_PAZYZ("各层级事件办结量推送给省平安志愿者","36007"),
	NC_XF_XCX("南昌市巡防小程序","3601001"),
	NC_HK_YJ("海康预警事件对接","3601002"),
	NC_DSJ_ZZ("南昌大数据平台上报审核事件到综治平台","3601003"),
	NC_12345_RX("南昌12345政府热线对接","3601004"),
	NC_ZZ_DSJ_GD("南昌综治平台推送归档事件至大数据平台","3601005"),
	NC_DSJ_CG("南昌大数据平台上报事件至城管平台","3601006"),
	NC_ZZ_DSJ_DEL("南昌综治平台推送被删除事件至大数据平台","3601007"),
	NC_GXQ("南昌高新区对接","3601008"),
	NC_SNYC_STATE("南昌事件结案状态同步至审核列表","3601009"),
	NC_DSJ_AUTO_CLOSE("南昌大数据平台自动结案评价被城管驳回的AI事件","3601010"),
	NC_WX("平安南昌微信公众号对接","3601011"),
	NC_GXQ_XCX("南昌高新区随手拍小程序对接","3601012"),
	NC_GXQ_APP("南昌高新区网格通APP对接","3601013"),
	WN_CLOUD_EYE("武宁云眼AI对接","3601014"),
	JKQ_REPORT_NCZZ("经开区上报审核事件至南昌市综治平台","3601015"),
	NCZZ_BACK_JKQ("南昌综治反馈审核信息及同步流程环节至经开区","3601015"),
	NCZZ_REPORT_JKQ("南昌综治上报审核事件至经开区","3601016"),
	JKQ_BACK_NCZZ("经开区反馈审核信息及同步流程环节至南昌市综治平台","3601016"),
	NC_PROBLEM_REPORT("南昌问题上报","3601017"),
	JKQ_REPORT_VR("经开区上报事件至VR","3601018"),
	JKQ_SYN_NC("经开区自采自结已归档事件同步至南昌市","3601019"), 
	JKQ_REJECT_NC("经开区审核变事件后驳回至南昌市","3601020"),
	NC_RS_XZ("南昌人社险种查询并入库","3601021"),
	NC_DSJ_EYE("南昌大数据平台对接智慧云眼","3601022"),
	NC_NCX("南昌南昌县对接","3601023"),
	NC_NCX_APP("南昌南昌县APP对接","3601024"),
	NC_DSJ_HUAWEI("南昌大数据平台对接华为AI分析预警","3601025"),
	NC_HK_SD_AI("南昌大数据平台对接海康实地AI","3601026"),
	WN_SYNC_MST("武宁拉取码上通历史归档数据","3601027"),
	WN_MST("武宁拉取码上通数据上报事件","3601028"),
	WN_BACK_MST("武宁反馈上报事件的办结信息至码上通","3601029"),
	GZ_PAZYZ("赣州平安志愿者","3607001"),
	GZ_12345("赣州12345对接","3607002"),
	GZ_SZCG("赣州数字城管对接","3607003"),
	GZ_ZGQ_CS("赣州市章贡区吹哨对接","360702001"),
	GZ_ZGQ_CG("赣州市章贡区城管对接","360702002"),
	GZ_ZHYY("赣州市智慧云眼对接","360702003"),
	GZ_NKQ_SJ("赣州市南康区市监平台事件对接","360782001"),
	GZ_NKQ_JW("赣州市南康区警务平台事件对接","360782002"),
	GZ_XC_WECHAT("赣州新城镇微信对接","360723102001"),
	GZ_NJ_WECHAT("赣州南迳镇微信对接","360729104001"),
	JX_JJZ("锦江微信公众号反馈结案信息给手机端","012"),
	JX_LFZ("罗坊镇事件上报奖励对接","360502007001"),
	JX_LFZ_GFT("罗坊镇赣福通对接","360502007002"),
	JX_CCX("江西省村村享手环对接告警结束时间","36"),
	JY_JJ("江阴交警对接","066"),
	JY_LW("江阴综合执法对接","030"),
	JY_lW_CG("江阴城管对接","031"),
	JY_SZZ("江阴省综治对接","047"),
	JY_XIAOFANG("江阴消防对接","067"),
	JY_XUNFANG("江阴巡防对接","042"),
	JY_GAYQ("江阴公安疫情管理平台","043"),
	JY_XXK("江阴驳回结案反馈给徐霞客","052"),
	JY_XXK_RG("江阴入格事件结案反馈信息给徐霞客","100001"),
	JY_JZ_12345("江阴街镇对接12345","038"),
	JY_SJ_12345("江阴市级对接12345","018"),
	JY_CG("江阴对接数字城管","320281001"),
	JY_YWJD("江阴徐霞客业务监督下派","320281002"),
	JY_DC("江阴徐霞客督查下派","320281003"),
	JY_BACK_DELAY("江阴反馈延期审核结果至徐霞客","320281004"), 
	JY_IsIn_CG("同步江阴数字城管上报的事件是否入格","320281005"),
	JY_XXK_CG("江阴徐霞客城管上报事件","320281006"),
	JY_XXK_XF("江阴徐霞客巡防上报事件","320281007"),
	JY_XXK_YSB("江阴徐霞客增加预上报事件","320281008"),
	JY_XXK_BACK_HANG("江阴徐霞客反馈挂起事件","320281009"),
	JY_XXK_TURN_REPORT("江阴徐霞客反馈转上报事件","320281010"),
	JY_CDJD("江阴城东街道对接","320281011"),
	JY_CJ("江阴长江平台对接","320281012"),
	JY_CJJD("江阴澄江街道对接","320281013"),
	JY_CJJD_BACK_HANG("江阴澄江街道预上报-反馈挂起事件","320281014"),
	JY_CJJD_TURN_REPORT("江阴澄江街道预上报-反馈转上报事件","320281015"),
	JY_CJJD_DC("江阴澄江街道督查下派","320281016"),
	JY_CJJD_YWJD("江阴澄江街道业务监督下派","320281017"),
	JY_CJJD_CG("江阴澄江街道城管上报事件","320281018"),
	JY_XGJD("江阴夏港街道对接","320281019"),
	JY_XGJD_BACK_HANG("江阴夏港街道预上报-反馈挂起事件","320281020"),
	JY_XGJD_TURN_REPORT("江阴夏港街道预上报-反馈转上报事件","320281021"),
	JY_XGJD_DC("江阴夏港街道督查下派","320281022"),
	JY_XGJD_YWJD("江阴夏港街道业务监督下派","320281023"),
	JY_XGJD_CG("江阴夏港街道城管上报事件","320281024"),
	JY_CJ_SJZHZX("江阴长江平台提交事件至市级指挥中心","320281025"),
	JY_CJ_12345("江阴长江平台提交事件至市级12345","320281026"),
	JY_CJ_QS("江阴长江平台提交事件至市级12345，我方收到后调用长江签收接口","320281027"),
	JY_SJZHZX_ZHZF("江阴市级指挥中心对接综合执法","320281028"),
	JY_SJZHZX_XXK("市级指挥中心下派事件至徐霞客","320281029"), 
	JY_CLOSE_XXK("徐霞客上报事件至市级指挥中心，办结后反馈回去","320281030"), 
	JJ_WECHAT("晋江微信上报对接","350582001"),
	JJ_GZ_WECHAT("晋江公众微信上报对接","011"),
	JJ_BMFWZX("晋江便民服务中心对接","004"),
	NP_SSPTOYP("南平随手拍驳回结案反馈给延平","204003"),
    NP_YPTOSSP("南平延平上报至随手拍","204003"),
	NP_SQ("南平延平区诉求事件对接","202"),
	NP_SSPAPP("延平随身拍App","204002"),
	NP_SHB("南平省环保对接","088"),
	NP_SZCG("南平延平区数字城管对接","060"),
	NP_ZHST("南平延平区智慧生态对接","060"),
	NP_ZQZTC("南平政企直通车","204004"),
	NP_JSYS("南平市大网格与南平金山银山平台对接","3507001"),
	NP_DWG_YP("南平大网格推送延平区域已归档的事件至延平区","350701001"),
	NP_SSP_YP("南平随身拍推送延平区域已归档的事件至延平区","350701002"),
	YP_GZ_WECHAT("延平公众微信对接","350701003"),
	YP_WECHAT("延平监督员微信对接","350701004"),
	NP_WYS("南平市武夷山平台对接","3507002"),
	NP_SNYC_JSYS("南平大网格历史归档环保事件给金山银山平台","3507003"),
	NP_ST("南平商汤对接","3507004"),
	FJS_SZZT("福建省数字政通环保事件对接","034"),
	NP_SZZT("南平数字政通环保事件对接","034"),
	WN_12345("海南万宁12345对接","035"),
	WN_SJ("万宁公众版APP反馈信息给手机端APP","202003"),
	WN_WX("万宁微信公众号对接","469006001"),
	WN_DB("万宁督办信息对接","469006002"),
	WN_SQ("万宁诉求事件对接","202"),
	XM_SM("厦门-思明区环保网格事件对接","020"),
	XM_TA("厦门-同安区环保网格事件对接","021"),
	XM_XA("厦门-翔安区环保网格事件对接","022"),
	XM_HC("厦门-海沧区环保网格事件对接","027"),
	XM_HL("厦门-湖里区环保网格事件对接","028"),
	XM_JM("厦门-集美区环保网格事件对接","029"),
	XM_SHB("厦门省环保对接","099"),
	XM_ICT("厦门环保对接ICT平台","3502001"),
	XA_XCX("雄安小程序对接","1306001"),
	ZZ_JCJ("漳州市司法网格平台与漳州市110接处警系统对接","3506001"),
	ZZ_SQNC("漳州市社区农村110平台事件对接","3506002"),
	ZZ_12345("漳州12345对接","3506003"),
	GZ_JINJIANG("赣州市锦江镇对接","360622101001"),
	ZZ_XLXQ("漳州乡里乡亲对接","3506004"),
	SM_ZFJCYH("执法检查隐患","3504001"),
	SM_NH("三明宁化对接","3504002"),
	SM_MXCG("三明明溪城管对接","3504004"),
	SM_YXCG("三明尤溪城管对接","3504005"),
	SM_YACG("三明永安城管对接","3504006"),
	ZY_APP("张掖公众App上报到审核列表","6217001"),
	ZY_WECHAT("张掖微信公众号上报到审核列表","6217002"),
	ZY_TO_SPT("张掖同步归档事件至省平台","6217003"),
	ZY_SP("张掖视频对接","6217004"),
	SD_WECHAT("山丹县微信对接","621704001"),
	SM_JN("三明建宁对接","3504003"),
	NA_12345("南安12345事件对接","350583001"),
	NA_EVENT_BACK_12345("南安12345事件信息反馈退回对接","350583002"),
	NA_EVENT_FAST("南安事件速报","350583005"),
	NA_PUBLIC_ADVICE("南安群众建议","350583006"),
	NA_YS_ZQZTC("南安营商入格事件对接政企直通车","350583007"),
	NA_BACK_ZQZTC("结案反馈至政直通车","350583008"),
	BY_WX_WBS("白银微信小程序微报事","6204001"),
	BY_WX_WHY("白银微信小程序微呼应","6204002"),
	BY_WX_WTJ("白银微信小程序微调解","6204003"),
	BY_BKYJ("白银布控预警对接","6204004"),
	BY_MSB("白银码上办对接","6204006"),
	BY_GZ_APP("白银公众APP对接","6204005"),
	JYG_APP("嘉峪关公众APP对接","6202001"),
	JYG_SPT("嘉峪关同步归档事件至省平台","6202002"),
	LX_APP("临夏公众APP对接","6214001"),
	JX_GQC_WRJ("江西共青城无人机","360431001"),
	NP_YP_CGWF("南平市延平区城管违法智能识别对接","350701005"),
	TS_EVENT_REPORT("天水微信公众号","6205001"),
	WZ_EVENT_FAST("万州事件速报","500101001"),
	WZ_PUBLIC_ADVICE("万州群众建议","500101002"),
	CD_XLGC("西藏昌都市与雪亮工程对接到审核列表","5403001"),
	CX_XCX("楚雄小程序对接","532301001"),
	CX_NETWORK("楚雄物联网设备告警事件对接","532301002"),
    CX_BKYJ("楚雄布控预警","532301003"),
	CX_DOOR("楚雄物联网门禁预警事件对接","532301004"),
	CX_RYBK("楚雄人员布控","532301005");
	
	
	private String name;
	private String value;
	
	private BizPlatfromEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}