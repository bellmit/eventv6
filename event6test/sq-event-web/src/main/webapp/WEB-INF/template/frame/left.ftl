<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综治信息管理</title>
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/public.css">
<LINK rel=stylesheet type=text/css href="${rc.getContextPath()}/theme/frame/css/frame.css">
<script type=text/javascript src="${rc.getContextPath()}/theme/frame/js/outlook.js"></script>
<style type=text/css>
    body { BACKGROUND: #eef4fa }
</style>
</head>

<body>
<div style="DISPLAY: none" id="left_main_nav">
        </div>
        <div id="right_main_nav">
        </div>
        <script type=text/javascript>
            var outlookbar = new outlook();
            var t;
        </script>
        <!-- 管理员菜单 -->
        <!-- 日常管理员菜单 -->
        <!-- CP菜单 -->
        <script type=text/javascript>
        	window.onload = function() {
		        initinav('地理信息');
		        try {
		            setInterval(function(){document.getElementById("right_main_nav").style.height = (parent.document.body.clientHeight -135) + "px"},300);
		        } catch(e) {}
		    }
        	//-- tab 1
        	t = outlookbar.addtitle('网格员协助送达流程', '地理信息', 1)
        	 outlookbar.additem('法院协作办公（我的发起）', t, '${rc.getContextPath()}/zhsq/courtSynergism/index.jhtml?menuType=mycreate');
        	 outlookbar.additem('法院协作办公（我的待办）', t, '${rc.getContextPath()}/zhsq/courtSynergism/index.jhtml?menuType=mywait');
        	 outlookbar.additem('法院协作办公（我的经办）', t, '${rc.getContextPath()}/zhsq/courtSynergism/index.jhtml?menuType=myhandle');
        	 outlookbar.additem('发起协助送达流程（我的发起）', t, '${rc.getContextPath()}/zhsq/gdPersionSendFlow/index.jhtml?menuType=mycreate');
        	 outlookbar.additem('流程待办事项（我的待办）', t, '${rc.getContextPath()}/zhsq/gdPersionSendFlow/index.jhtml?menuType=mywait');
        	 outlookbar.additem('流程查询（我的经办）', t, '${rc.getContextPath()}/zhsq/gdPersionSendFlow/index.jhtml?menuType=myhandle');
        	t = outlookbar.addtitle('案件联动', '地理信息', 1)
        	 outlookbar.additem('案件联动', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=index');
        	 outlookbar.additem('未处理案件', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=untreated');
        	 outlookbar.additem('案件查询', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=search');
        	 outlookbar.additem('待办案件', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=wait');
        	 outlookbar.additem('经办案件', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=hanlde');
        	 outlookbar.additem('归档案件', t, '${rc.getContextPath()}/zhsq/ypms/mscase/index.jhtml?menuType=archive');
        	t = outlookbar.addtitle('南昌地图', '地理信息', 1)
        	outlookbar.additem('arcgis3D地图(新版)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_3D_HOME');
        	t = outlookbar.addtitle('真三维地图', '地理信息', 1)
        	outlookbar.additem('万宁三维地图', t, '${rc.getContextPath()}/zhsq/map/DDEarthController/index.jhtml');
        	t = outlookbar.addtitle('地图图层', '地理信息', 1)
            outlookbar.additem('平潭楼宇轮廓数据导入', t, '${rc.getContextPath()}/zhsq/map/dataImportController/toIndex.jhtml');
        	outlookbar.additem('平潭网格轮廓编辑', t, '${rc.getContextPath()}/zhsq/map/gridOLPingTan/gridOLPingTanController/spGisGridIndex.jhtml');
        	outlookbar.additem('平潭ol楼宇轮廓中心点编辑', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/spGisBlindBuildIndex.jhtml');
        	outlookbar.additem('平潭ol网格标注', t, '${rc.getContextPath()}/zhsq/map/gridOL/gridOLController/spGisGridIndex.jhtml');
        	outlookbar.additem('单条楼宇轮廓编辑跳转中间页面', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/singleBuildingDrawIndex.jhtml?buildingId='+4432645);
        	outlookbar.additem('(晋江)楼宇轮廓批量编辑', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/blindBuildAddressIndex.jhtml');
        	outlookbar.additem('(晋江)楼宇轮廓单条编辑', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/olwfs.jhtml?buildingId='+1052868);
        	outlookbar.additem('wfs测试', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/cswfs.jhtml');
        	outlookbar.additem('楼宇关联菜单', t, '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/index.jhtml');
        	outlookbar.additem('安监企业地图', t, '${rc.getContextPath()}/zhsq/map/safetysupervision/safetySupervision/index.jhtml');
        	outlookbar.additem('高德地图可配置首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=MAPABC_STANDARD_HOME');
        	outlookbar.additem('平潭综合业务地图', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=SPGIS_BUSI_HOME');
        	outlookbar.additem('平潭合体地图', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toPtHome.jhtml');
        	outlookbar.additem('重点人群专题首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=SPGIS_KEY_POPU');
        	outlookbar.additem('重点人群专题首页2', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=SPGIS_KEY_POPU2');
        	outlookbar.additem('重点人群专题首页(ARCGIS)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_KEY_POPU');
        	outlookbar.additem('立体防控地图首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=IDSS');
            outlookbar.additem('智能视频监控地图首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=INTELLIGENT_VIDEO_SURVEILLANCE');
        	outlookbar.additem('矛盾纠纷地图首页', t, '${rc.getContextPath()}/zhsq/dispute/main/index.jhtml');
// 			outlookbar.additem('平潭矛盾纠纷首页', t, '${rc.getContextPath()}/zhsq/dispute/main/index.jhtml');
        	outlookbar.additem('地图可配置首页(新版)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_STANDARD_HOME');
            outlookbar.additem('智慧城市地图可配置首页(新版)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=SMART_CITY');
			outlookbar.additem('环保地图可配置首页(新版)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_HB_HOME');
        	outlookbar.additem('地图部件首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=URBAN_OBJ_HOME&menuCode=urbanObject');
        	outlookbar.additem('可配置消防首页(新版)', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_FIRECONTROL_HOME');
        	outlookbar.additem('海沧安监', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfFactor.jhtml?homePageType=ARCGIS_HC_AJ');
        	outlookbar.additem('地图统计首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toGisStatIndex.jhtml?homePageType=ARCGIS_STAT_HOME');
        	outlookbar.additem('地图可配置首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml');
        	outlookbar.additem('地图可配置树形首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=tree');
        	//outlookbar.additem('标准地图首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=standard');
        	//outlookbar.additem('甘肃地图首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=gansu');
        	outlookbar.additem('福州地图首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toFuzhouArcgis.jhtml');
        	outlookbar.additem('台江地图图层', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=taijiang');
        	//outlookbar.additem('晋江消防网格图层', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=jinjiangfiregrid');
        	//outlookbar.additem('莆田中间页面首页', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=putian');
        	//outlookbar.additem('海沧安监', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=haicanganjian');
        	//outlookbar.additem('新阳街道基础数据', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=xinyangjcsj');
        	//outlookbar.additem('新阳街道指挥调度', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=xinyangzhdd');
        	//outlookbar.additem('新阳街道党组织', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=xinyangdzz');
        	//outlookbar.additem('中华街道民生图层', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=zhonghuams');
        	outlookbar.additem('中华街道党员图层', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=zhonghuady');
        	outlookbar.additem('中华街道防控区域', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=zhonghuafkqy');
        	outlookbar.additem('危房视频监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=001');
        	outlookbar.additem('警示威慑监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=002');
        	outlookbar.additem('森林视频监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=003');
        	outlookbar.additem('客流视频监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=004');
        	outlookbar.additem('区域视频监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=005');
        	outlookbar.additem('语音视频监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=006');
        	outlookbar.additem('全球眼监控', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=globalEyes');
        	//outlookbar.additem('组织地图展示', t, '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/toMapArcgisOfFireGridCrossDomain.jhtml?orgId=4175');
        	<!--outlookbar.additem('水文监测点位', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=swjc');-->
        	//outlookbar.additem('水文监测点位', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfStandard.jhtml?arcgisUrlType=swjc');
        	
        	t = outlookbar.addtitle('地图编辑', '地理信息', 1)
            outlookbar.additem('演示首页', t, '${rc.getContextPath()}/zhsq/map/buildBindController/index.jhtml');
            outlookbar.additem('三维地图楼宇绑定', t, '${rc.getContextPath()}/zhsq/map/buildBindController/index_3d_binding.jhtml');
            outlookbar.additem('三维地图楼宇信息展示', t, '${rc.getContextPath()}/zhsq/map/buildBindController/show_building_bind_index.jhtml');
            outlookbar.additem('单辆车轨迹演示', t, '${rc.getContextPath()}/zhsq/map/buildBindController/show_oneCar_trajectory_index.jhtml');
            outlookbar.additem('多辆车轨迹演示', t, '${rc.getContextPath()}/zhsq/map/buildBindController/show_multitermCar_trajectory_index.jhtml');
            outlookbar.additem('多辆车轨迹演示2', t, '${rc.getContextPath()}/zhsq/map/buildBindController/show_car_trajectory_index.jhtml');
			outlookbar.additem('(晋江)单条网格轮廓编辑', t, '${rc.getContextPath()}/zhsq/map/gridOL/gridOLController/olwfs.jhtml?singleFlag=true&gridId='+211646);
        	outlookbar.additem('(晋江)批量网格轮廓编辑', t, '${rc.getContextPath()}/zhsq/map/gridOL/gridOLController/blindGridIndex.jhtml');
        	outlookbar.additem('网格轮廓编辑实例', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/toArcgisDrawAreaPanel.jhtml?targetType=point&wid=49');
        	outlookbar.additem('网格轮廓编辑（新版）', t, '${rc.getContextPath()}/zhsq/drowGridController/index.jhtml');
        	outlookbar.additem('网格轮廓绑定', t, '${rc.getContextPath()}/zhsq/bindGridController/index.jhtml');
        	outlookbar.additem('网格轮廓编辑（新版）-无轮廓', t, '${rc.getContextPath()}/zhsq/drowGridController/index.jhtml?isDraw=0');
        	outlookbar.additem('楼栋轮廓编辑实例', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/toArcgisDrawAreaPanel.jhtml?targetType=build&wid=1046516');
        	outlookbar.additem('楼栋轮廓编辑（新版）', t, '${rc.getContextPath()}/zhsq/drowBuildingController/index.jhtml');
        	outlookbar.additem('楼栋轮廓编辑（绑定）', t, '${rc.getContextPath()}/zhsq/drowBuildingController/indexBinding.jhtml');
        	outlookbar.additem('楼栋轮廓编辑（新版）-无轮廓', t, '${rc.getContextPath()}/zhsq/drowBuildingController/index.jhtml?isDraw=0');
        	outlookbar.additem('护路护线轮廓编辑实例', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/toArcgisDrawAreaPanel.jhtml?targetType=hlhx&wid=4');
        	outlookbar.additem('点定位编辑实例', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfAnchorPoint.jhtml');
        	outlookbar.additem('点定位编辑保存', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfAnchorPointForSave.jhtml?resourcesId=4791&markerType=02020501&catalog=02');
            outlookbar.additem('通过参数获取人员轨迹', t, '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/showTrajectoryIndex.jhtml?userId=184');
            outlookbar.additem('定位点批量编辑', t, '${rc.getContextPath()}/zhsq/drawMapDataController/index.jhtml');
            outlookbar.additem('地图数据维护', t, '${rc.getContextPath()}/zhsq/map/mapDataMaintain/index.jhtml');
        	t = outlookbar.addtitle('事件处理', '事件处理', 1)
        	outlookbar.additem('单位问题台账', t, '${rc.getContextPath()}/zhsq/unitProb/index.jhtml');
        	outlookbar.additem('个人问题台账', t, '${rc.getContextPath()}/zhsq/personProb/index.jhtml');
        	outlookbar.additem('事件绩效首页', t, '${rc.getContextPath()}/zhsq/event/eventOverviewController/indexA.jhtml');
        	outlookbar.additem('新增事件', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson=%7BisReport:false%7D');
        	//outlookbar.additem('草稿事件', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listDraftEvent.jhtml');
        	outlookbar.additem('草稿事件', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=draft&model=l&system_privilege_code=zhsq_event_draft');
        	outlookbar.additem('待办列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&system_privilege_code=zhsq_event_todo');
        	outlookbar.additem('待办列表-无评价', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&eventAttrTrigger=noEvaluate');
        	outlookbar.additem('待评列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&eventAttrTrigger=evaluate&system_privilege_code=zhsq_event_closed');
        	outlookbar.additem('待核实列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&eventAttrTrigger=verified&system_privilege_code=zhsq_event_closed');
        	outlookbar.additem('我发起的', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=my&model=l&system_privilege_code=zhsq_event_launched');
        	outlookbar.additem('我的关注', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=attention&model=l');
        	outlookbar.additem('我的归档', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=myhistory&model=l');
        	outlookbar.additem('经办列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=done&model=l&system_privilege_code=zhsq_event_handled');
        	outlookbar.additem('经办历史', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=donehistory&model=l');
        	outlookbar.additem('归档列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=history&model=l&extraParams=%7BisEnableDefaultCreatTime:true%7D&system_privilege_code=zhsq_event_archived');
        	outlookbar.additem('辖区所有', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&extraParams=%7BisEnableDefaultCreatTime:true%7D&system_privilege_code=zhsq_event_jurisdiction');
        	outlookbar.additem('辖区所有(领导通)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&extraParams=%7B%22multiple%22:true%7D&system_privilege_code=zhsq_event_push');
        	outlookbar.additem('辖区所有(支持办理查询)', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&system_privilege_code=zhsq_event_jurisdiction_eventorg');
        	outlookbar.additem('辖区需要督办事件', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=toRemind&model=l&system_privilege_code=zhsq_event_supervise');
        	outlookbar.additem('辖区需要督办事件-无评价', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=toRemind&model=l&eventAttrTrigger=noEvaluate');
        	outlookbar.additem('辖区我督办的事件', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&extraParams=%7B%22isCapMySupervised%22:true%7D&system_privilege_code=zhsq_event_my_supervision');
        	outlookbar.additem('视图归档', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=view&model=l');
        	outlookbar.additem('事件接口', t, '${rc.getContextPath()}/zhsq/event/openEventDisposalController/openlistData.jhtml?page=1&rows=5&eventType=todo');
        	outlookbar.additem('归档事件导入', t, '${rc.getContextPath()}/zhsq/event/import/toIndex.jhtml');
        	outlookbar.additem('归档事件导入xml', t, '${rc.getContextPath()}/zhsq/event/importXml/toIndex.jhtml');
        	outlookbar.additem('归档事件导入批量', t, '${rc.getContextPath()}/zhsq/event/importEvent4File/toList.jhtml?system_privilege_code=zhsq_event_import_4_file');
        	outlookbar.additem('转派列表', t, '${rc.getContextPath()}/zhsq/event/eventTask/toListEventTask.jhtml?system_privilege_code=zhsq_event_task_transfer');
        	outlookbar.additem('事件等级预警', t, '${rc.getContextPath()}/zhsq/warningScheme/index.jhtml?system_privilege_code=warningScheme');
        	outlookbar.additem('领导通列表', t, '${rc.getContextPath()}/zhsq/eventPush/index.jhtml?system_privilege_code=zhsq_ldt');
            
            outlookbar.additem('预案人员配置', t, '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/index.jhtml');
        	//盐都 大丰的矛盾纠纷 和事件的不同
            t = outlookbar.addtitle('矛盾纠纷调处', '事件处理', 1)
            outlookbar.additem('矛盾纠纷', t, '${rc.getContextPath()}/zhsq/disputeMediation/9x/index.jhtml?system_privilege_action=3261');
            //csk 新增南安矛盾纠纷（个性化）
            outlookbar.additem('矛盾纠纷（南安）', t, '${rc.getContextPath()}/zhsq/event/mediationNa/index.jhtml');

        	t = outlookbar.addtitle('江阴事件', '事件处理', 1);
        	outlookbar.additem('事件复核列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=3&system_privilege_code=jy_delete_event_auditing');
        	outlookbar.additem('事件删除列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=4&isEnableDefaultCreatTime=true&system_privilege_code=jy_delete_event_confirm');
        	outlookbar.additem('事件督查复核列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=31&system_privilege_code=jy_delete_event_auditing');
        	outlookbar.additem('事件督查列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=41&isEnableDefaultCreatTime=true&system_privilege_code=jy_delete_event_confirm');
        	
        	t = outlookbar.addtitle('盐都区事件', '事件处理', 1);
        	outlookbar.additem('事件预处理审核', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=5&system_privilege_code=zhsq_event_prepress_auditing');
        	outlookbar.additem('核验不通过事件列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=6&system_privilege_code=yd_event_check_fail');
        	outlookbar.additem('街道事件评价列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/toListEventEva.jhtml?evaType=1&system_privilege_code=yd_street_event_eva');
        	outlookbar.additem('县区事件评价列表', t, '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/toListEventEva.jhtml?evaType=2&system_privilege_code=yd_district_event_eva');
        	
        	t = outlookbar.addtitle('延平城管事件', '事件处理', 1);
        	outlookbar.additem('事件挂起复核列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=32&system_privilege_code=ypcg_suspend_event_auditing');
        	outlookbar.additem('事件挂起列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=42&isEnableDefaultCreatTime=true&system_privilege_code=ypcg_suspend_event_confirm');
        	outlookbar.additem('我的挂起事件列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=42&isEnableDefaultCreatTime=true&isCreatedBySelf=true&system_privilege_code=ypcg_suspend_event_my');
        	
        	t = outlookbar.addtitle('江西省平台事件', '事件处理', 1);
        	outlookbar.additem('无效事件复核列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=33&system_privilege_code=ypcg_suspend_event_auditing');
        	outlookbar.additem('辖区内无效事件列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=43&isEnableDefaultCreatTime=true&system_privilege_code=jxplatform_invalid_event_confirm');
        	outlookbar.additem('我的无效事件列表', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=43&isEnableDefaultCreatTime=true&isCreatedBySelf=true&system_privilege_code=jxplatform_invalid_event_my');
        	outlookbar.additem('八员队伍', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&isForce2Unit=true&isCapEventExtend=true&emtType=255&system_privilege_code=zhsq_event_jurisdiction_eventorg_emt');
        	outlookbar.additem('八员队伍(城管)', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&isForce2Unit=true&isCapEventExtend=true&emtType=1&system_privilege_code=zhsq_event_jurisdiction_eventorg_cg');
        	outlookbar.additem('八员队伍(公安)', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&isForce2Unit=true&isCapEventExtend=true&emtType=2&system_privilege_code=zhsq_event_jurisdiction_eventorg_ga');
        	outlookbar.additem('八员队伍(市监局)', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&isForce2Unit=true&isCapEventExtend=true&emtType=4&system_privilege_code=zhsq_event_jurisdiction_eventorg_sjj');
        	outlookbar.additem('八员队伍(环卫)', t, '${rc.getContextPath()}/zhsq/event/eventOrg/toListEventOrg.jhtml?isEnableDefaultCreatTime=true&isForce2Unit=true&isCapEventExtend=true&emtType=8&system_privilege_code=zhsq_event_jurisdiction_eventorg_hw');
        	
        	t = outlookbar.addtitle('诉求', '事件处理', 1);
            outlookbar.additem('诉求(审核)', t, '${rc.getContextPath()}/zhsq/publicAppeal/index.jhtml?listType=1&system_privilege_code=examine');
            outlookbar.additem('12345数据列表', t, '${rc.getContextPath()}/zhsq/event/bianminPlatformDataController/index.jhtml');
            //outlookbar.additem('诉求(待办)', t, '${rc.getContextPath()}/zhsq/publicAppeal/index.jhtml?listType=2');
            //outlookbar.additem('诉求(辖区)', t, '${rc.getContextPath()}/zhsq/publicAppeal/index.jhtml?listType=3');
            //outlookbar.additem('诉求(经办)', t, '${rc.getContextPath()}/zhsq/publicAppeal/index.jhtml?listType=4');
            //outlookbar.additem('诉求(我发起的)', t, '${rc.getContextPath()}/zhsq/publicAppeal/index.jhtml?listType=5');

            t = outlookbar.addtitle('事件对接', '事件处理', 1);
            outlookbar.additem('微信事件审核(本网格)', t, '${rc.getContextPath()}/zhsq/eventWechat/toList.jhtml?system_privilege_code=zhsq_event_wechat_docking');
            outlookbar.additem('微信事件审核(辖区内)', t, '${rc.getContextPath()}/zhsq/eventWechat/toList.jhtml?isCapConfigureParam=true&isJurisdictionQuery=true&system_privilege_code=zhsq_event_wechat_docking');
            outlookbar.additem('线索事件', t, '${rc.getContextPath()}/zhsq/eventWechat/clue/toList.jhtml?system_privilege_code=zhsq_event_wechat_docking');

			t = outlookbar.addtitle('案件处理', '事件处理', 1);
			outlookbar.additem('草稿案件', t, '${rc.getContextPath()}/zhsq/eventCase/toList.jhtml?listType=1&system_privilege_code=zhsq_event_case_draft');
			outlookbar.additem('待办案件', t, '${rc.getContextPath()}/zhsq/eventCase/toList.jhtml?listType=2&system_privilege_code=zhsq_event_case_todo');
			outlookbar.additem('经办案件', t, '${rc.getContextPath()}/zhsq/eventCase/toList.jhtml?listType=3&system_privilege_code=zhsq_event_case_handled');
			outlookbar.additem('我发起的案件', t, '${rc.getContextPath()}/zhsq/eventCase/toList.jhtml?listType=4&system_privilege_code=zhsq_event_case_initiator');
			outlookbar.additem('辖区所有案件', t, '${rc.getContextPath()}/zhsq/eventCase/toList.jhtml?listType=5&system_privilege_code=zhsq_event_case_jurisdiction');
			
			t = outlookbar.addtitle('南昌大数据个性事件菜单', '事件处理', 1);
			outlookbar.additem('辖区所有(巡访事件专用)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&system_privilege_code=zhsq_event_jurisdiction&extraParams=%7BisEnableDefaultCreatTime:true,searchEventLabel:true,labelModel:%22002%22,patrolType:%221,2%22%7D');
			outlookbar.additem('待办事件(巡访事件专用)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&system_privilege_code=zhsq_event_todo&extraParams=%7BpatrolType:%221,2%22%7D');
			outlookbar.additem('在办事件(巡访事件专用)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&eventAttrTrigger=areaOperation&model=l&system_privilege_code=zhsq_event_jurisdiction&extraParams=%7BpatrolType:%221,2%22%7D');
			outlookbar.additem('辖区所有(非问题上报事件)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&model=l&system_privilege_code=zhsq_event_jurisdiction&extraParams=%7BisEnableDefaultCreatTime:true,bizPlatform:%220,3601001,3601002%22%7D');
			outlookbar.additem('待办事件(非问题上报事件)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=todo&model=l&system_privilege_code=zhsq_event_todo&extraParams=%7BbizPlatform:%220,3601001,3601002%22%7D');
			outlookbar.additem('在办事件(非问题上报事件)', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=all&eventAttrTrigger=areaOperation&model=l&system_privilege_code=zhsq_event_jurisdiction&extraParams=%7BbizPlatform:%220,3601001,3601002%22%7D');
			outlookbar.additem('待办事件(背街小巷)', t, '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=todo&model=l&extraParams=%7BbizPlatform:3601018%7D');
			outlookbar.additem('经办事件(背街小巷)', t, '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=done&model=l&extraParams=%7BbizPlatform:3601018%7D');
			outlookbar.additem('辖区所有事件(背街小巷)', t, '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=all&model=l&extraParams=%7BisEnableDefaultCreatTime:true,bizPlatform:3601018%7D');
			outlookbar.additem('在办事件(背街小巷)', t, '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=all&eventAttrTrigger=areaOperation&model=l&extraParams=%7BbizPlatform:%223601018%22%7D');
			outlookbar.additem('待办问题(问题上报)', t, '${rc.getContextPath()}/zhsq/event/problemReportController/listEvent.jhtml?t=todo&model=l&extraParams=%7BbizPlatform:3601017%7D');
			outlookbar.additem('经办问题(问题上报)', t, '${rc.getContextPath()}/zhsq/event/problemReportController/listEvent.jhtml?t=done&model=l&extraParams=%7BbizPlatform:3601017%7D');
			outlookbar.additem('辖区所有问题(问题上报)', t, '${rc.getContextPath()}/zhsq/event/problemReportController/listEvent.jhtml?t=all&model=l&extraParams=%7BisEnableDefaultCreatTime:true,bizPlatform:3601017%7D');
			outlookbar.additem('事件审核(华为AI)', t, '${rc.getContextPath()}/zhsq/eventWechat/toList.jhtml?isJurisdictionQuery=true&bizPlatformForSearch=3601025');
			outlookbar.additem('辖区所有事件(华为AI)', t, '${rc.getContextPath()}/zhsq/event/streetLandEventController/listEvent.jhtml?t=all&model=l&extraParams=%7BisEnableDefaultCreatTime:true,bizPlatform:3601025,showFullTypeDict:true%7D');
			outlookbar.additem('辖区所有事件(大屏)', t, '${rc.getContextPath()}/zhsq/event/eventAnalyseController/toEventListBackstagePage.jhtml');
			outlookbar.additem('文明创建不下线(大屏)', t, '${rc.getContextPath()}/zhsq/nanChang3D/toUncivilizedPage.jhtml');
			
			t = outlookbar.addtitle('南昌大联动', '事件处理', 1);
			outlookbar.additem('草稿事件-移除矛盾纠纷', t, '${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t=draft&model=l&trigger=REMOVE_DISPUTE_MEDIATION&system_privilege_code=zhsq_event_draft');
			outlookbar.additem('我的审核列表(辖区内)', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=1&system_privilege_code=nch_jointCo_my_auditing');
			outlookbar.additem('我的申请列表(辖区内)', t, '${rc.getContextPath()}/zhsq/timeApplication/toList4Event.jhtml?listType=2&system_privilege_code=nch_jointCo_my_application');
			outlookbar.additem('辖区内超时事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?listType=3&handleDateFlag=3&system_privilege_code=nch_jointCo_overtime');
			outlookbar.additem('我的待办列表', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?listType=4&system_privilege_code=nch_jointCo_todo');
			outlookbar.additem('我的被督查督办事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?isInspected=1&listType=2&system_privilege_code=nch_jointCo_my_inspected');
			outlookbar.additem('辖区内被督查督办事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?isInspected=1&listType=1&system_privilege_code=nch_jointCo_inspected');
			outlookbar.additem('联合办理事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?isJointHandled=1&listType=1&system_privilege_code=nch_jointCo_handled');
			outlookbar.additem('联席交办事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?isJointOperated=1&listType=1&system_privilege_code=nch_jointCo_operated');
			
			t = outlookbar.addtitle('厦门环保', '事件处理', 1);
			outlookbar.additem('辖区内事件', t, '${rc.getContextPath()}/zhsq/eventExtend/toEventList.jhtml?listType=3&zoneCode=amoy&system_privilege_code=amoy_envpro_jurisdiction');
			
			t = outlookbar.addtitle('热点主题和词云', '事件处理', 1);
        	outlookbar.additem('热点主题', t, '${rc.getContextPath()}/zhsq/eventTopic/index.jhtml?bizType=1&system_privilege_code=eventTopic');
        	outlookbar.additem('词云后台', t, '${rc.getContextPath()}/zhsq/eventTopic/index.jhtml?bizType=2&system_privilege_code=keyWords');
			
			t = outlookbar.addtitle('标签模块', '事件处理', 1);
        	outlookbar.additem('标签管理', t, '${rc.getContextPath()}/zhsq/event/eventLabelController/toListPage.jhtml');

            t = outlookbar.addtitle('日常工作', '日常工作', 1)
            outlookbar.additem('南安入格事项统计报表', t, '${rc.getContextPath()}/zhsq/statistics/toReportForStatistics.jhtml');
            outlookbar.additem('南安疫情防控超时环节统计报表', t, '${rc.getContextPath()}/zhsq/statistics/toEpcOverdueStatistics.jhtml?reportType=4&system_privilege_code=epcOverdue_Statistics');
            outlookbar.additem('南安两违防治超时环节统计报表', t, '${rc.getContextPath()}/zhsq/statistics/toEpcOverdueStatistics.jhtml?reportType=1&system_privilege_code=epcOverdue_Statistics');
            outlookbar.additem('南安工业企业安全超时环节统计报表', t, '${rc.getContextPath()}/zhsq/statistics/toEpcOverdueStatistics.jhtml?reportType=3&system_privilege_code=ehdOverdue_Statistics');
            outlookbar.additem('南安入格初始化', t, '${rc.getContextPath()}/zhsq/eventAndReportJsonp/initReportFocusPage.jhtml');
            outlookbar.additem('舆情', t, '${rc.getContextPath()}/zhsq/publicSentiment/publicSentimentController/index.jhtml');
        	outlookbar.additem('巡防', t, '${rc.getContextPath()}/zhsq/patrolController/index.jhtml');
        	outlookbar.additem('应急预案', t, '${rc.getContextPath()}/zhsq/emergencyResponse/listEmergencyResponse.jhtml?trigger=emergencyResponse');
        	outlookbar.additem('涉事案件列表', t, '${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/listRelatedEvents.jhtml?system_privilege_code=related_road_events');
        	outlookbar.additem('校园涉事安全案件列表', t, '${rc.getContextPath()}/zhsq/relatedEvents/SchoolRelatedEventsController/listRelatedEvents.jhtml?system_privilege_code=school_related_events');
        	outlookbar.additem('通用涉事安全案件列表', t, '${rc.getContextPath()}/zhsq/relatedEvents/CommonRelatedEventsController/listRelatedEvents.jhtml');
        	outlookbar.additem('重大涉事安全案件列表', t, '${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/listRelatedEvents.jhtml?system_privilege_code=major_related_events');
        	outlookbar.additem('重大涉事安全案件列表(南昌)', t, '${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/listRelatedEvents.jhtml?system_privilege_code=major_related_events&type=nanchang');
        	outlookbar.additem('命案防控', t, '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toHomicideCaseList.jhtml?system_privilege_code=homicideCase');
        	outlookbar.additem('命案防控(南昌)', t, '${rc.getContextPath()}/zhsq/relatedEvents/homicideCase/toHomicideCaseList.jhtml?system_privilege_code=homicideCase&type=nanchang');
        	outlookbar.additem('环保企业巡查', t, '${rc.getContextPath()}/zhsq/enterpriseCheck/toList.jhtml?system_privilege_code=environmental_protection');
        	outlookbar.additem('消防检查', t, '${rc.getContextPath()}/zhsq/fireControlCheck/index.jhtml?system_privilege_code=fire_control');
        	outlookbar.additem('江西月报', t, '${rc.getContextPath()}/zhsq/zzgl/briefingController/list.jhtml');

        	t = outlookbar.addtitle('禁毒事件', '日常工作', 1)
        	outlookbar.additem('禁毒事件-草稿', t, '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toList.jhtml?listType=1&system_privilege_code=drug_enforcement_event_draft');
        	outlookbar.additem('禁毒事件-待办', t, '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toList.jhtml?listType=2&system_privilege_code=drug_enforcement_event_todo');
        	outlookbar.additem('禁毒事件-经办', t, '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toList.jhtml?listType=4&system_privilege_code=drug_enforcement_event_handled');
        	outlookbar.additem('禁毒事件-辖区所有', t, '${rc.getContextPath()}/zhsq/drugEnforcementEvent/toList.jhtml?listType=3&system_privilege_code=drug_enforcement_event_jurisdiction');
        	
        	t = outlookbar.addtitle('民生信息模块', '日常工作', 1)
        	outlookbar.additem('民生信息-采集列表', t, '${rc.getContextPath()}/zhsq/peopleLivelihood/index.jhtml?listType=1&system_privilege_code=people_livelihood_draft');
        	outlookbar.additem('民生信息-待办列表', t, '${rc.getContextPath()}/zhsq/peopleLivelihood/index.jhtml?listType=2&system_privilege_code=people_livelihood_todo');
        	outlookbar.additem('民生信息-经办列表', t, '${rc.getContextPath()}/zhsq/peopleLivelihood/index.jhtml?listType=3&system_privilege_code=people_livelihood_done');
        	outlookbar.additem('民生信息-辖区列表', t, '${rc.getContextPath()}/zhsq/peopleLivelihood/index.jhtml?listType=4&system_privilege_code=people_livelihood_all');
        	
			t = outlookbar.addtitle('政企互联', '日常工作', 1)
        	outlookbar.additem('政企中心(待办)', t, '${rc.getContextPath()}/zhsq/requestion/indexDB_Main.jhtml');
        	outlookbar.additem('政企中心(查询)', t, '${rc.getContextPath()}/zhsq/requestion/indexAll_Main.jhtml');
        	outlookbar.additem('政企单位(待办)', t, '${rc.getContextPath()}/zhsq/requestion/indexDB_Task.jhtml');
        	outlookbar.additem('政企单位(经办)', t, '${rc.getContextPath()}/zhsq/requestion/indexJB_Task.jhtml');
        	outlookbar.additem('诉求联动', t, '${rc.getContextPath()}/zhsq/reqLink/index.jhtml');
            outlookbar.additem('企业联动', t, '${rc.getContextPath()}/zhsq/corpLink/index.jhtml');
            outlookbar.additem('智慧延平政企互联', t, '${rc.getContextPath()}/zhsq/internetEnterprise/getDateList.jhtml');
            
            t = outlookbar.addtitle('告警任务', '日常工作', 1)
            outlookbar.additem('我发起的告警', t, '${rc.getContextPath()}/zhsq/warnTask/index.jhtml?indexType=fq');
        	outlookbar.additem('待办告警', t, '${rc.getContextPath()}/zhsq/warnTask/index.jhtml?indexType=db');
        	outlookbar.additem('已办告警', t, '${rc.getContextPath()}/zhsq/warnTask/index.jhtml?indexType=jb');
        	outlookbar.additem('归档', t, '${rc.getContextPath()}/zhsq/warnTask/index.jhtml?indexType=end');
        	outlookbar.additem('全部', t, '${rc.getContextPath()}/zhsq/warnTask/index.jhtml?indexType=all');
            
            t = outlookbar.addtitle('报修任务', '日常工作', 1)
            outlookbar.additem('我发起的报修', t, '${rc.getContextPath()}/zhsq/repairTask/index.jhtml?indexType=fq');
        	outlookbar.additem('待办报修', t, '${rc.getContextPath()}/zhsq/repairTask/index.jhtml?indexType=db');
        	outlookbar.additem('已办报修', t, '${rc.getContextPath()}/zhsq/repairTask/index.jhtml?indexType=jb');
        	outlookbar.additem('归档', t, '${rc.getContextPath()}/zhsq/repairTask/index.jhtml?indexType=end');
        	outlookbar.additem('全部', t, '${rc.getContextPath()}/zhsq/repairTask/index.jhtml?indexType=all');
            
            t = outlookbar.addtitle('日常工作任务', '日常工作', 1)
            outlookbar.additem('联防长', t, '${rc.getContextPath()}/zhsq/jointDefence/toList.jhtml?positionId=982');
            outlookbar.additem('我发起的任务', t, '${rc.getContextPath()}/zhsq/dailyTask/index.jhtml?indexType=fq');
        	outlookbar.additem('待办任务', t, '${rc.getContextPath()}/zhsq/dailyTask/index.jhtml?indexType=db');
        	outlookbar.additem('已办任务', t, '${rc.getContextPath()}/zhsq/dailyTask/index.jhtml?indexType=jb');
        	outlookbar.additem('归档', t, '${rc.getContextPath()}/zhsq/dailyTask/index.jhtml?indexType=end');
        	outlookbar.additem('全部', t, '${rc.getContextPath()}/zhsq/dailyTask/index.jhtml?indexType=all');
	    
            t = outlookbar.addtitle('新疆执纪问责', '日常工作', 1);
            outlookbar.additem('执纪问责-草稿', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=1&system_privilege_code=account_enforce_draft');
        	outlookbar.additem('执纪问责-待办', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=2&system_privilege_code=account_enforce_todo');
        	outlookbar.additem('执纪问责-经办', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=3&system_privilege_code=account_enforce_handled');
        	outlookbar.additem('执纪问责-我发起的', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=4&system_privilege_code=account_enforce_my');
        	outlookbar.additem('执纪问责-辖区所有', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=5&system_privilege_code=account_enforce_jurisdiction');
        	outlookbar.additem('执纪问责-辖区归档', t, '${rc.getContextPath()}/zhsq/accountabilityEnforcement/toList.jhtml?listType=6&system_privilege_code=account_enforce_jurisdiction_archive');

            t = outlookbar.addtitle('扫黑除恶', '日常工作', 1);
            outlookbar.additem('线索管理-草稿', t, '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=1&system_privilege_code=event_SBREClue_draft');
            outlookbar.additem('线索管理-待办', t, '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=2&system_privilege_code=event_SBREClue_todo');
            outlookbar.additem('线索管理-经办', t, '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=3&system_privilege_code=event_SBREClue_handled');
            outlookbar.additem('线索管理-我发起的', t, '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=5&system_privilege_code=event_SBREClue_initiator');
            outlookbar.additem('线索管理-辖区所有', t, '${rc.getContextPath()}/zhsq/eventSBREClue/toList.jhtml?listType=4&system_privilege_code=event_SBREClue_jurisdiction');
            outlookbar.additem('黑恶团伙管理', t, '${rc.getContextPath()}/zhsq/eventSBREvilGang/index.jhtml?system_privilege_code=event_SBREGang_list');
            
            t = outlookbar.addtitle('南安入格事件', '日常工作', 1);
            outlookbar.additem('配送人员配置', t, '${rc.getContextPath()}/zhsq/reportMsgCCCfg/toList.jhtml?system_privilege_code=focusReport_msgCCCfg');
            outlookbar.additem('我的阅办', t, '${rc.getContextPath()}/zhsq/reportFocus/toIntegrationMsgReadingList.jhtml?system_privilege_code=reportFocus_msgReading');
            outlookbar.additem('信息反馈-我的反馈', t, '${rc.getContextPath()}/zhsq/reportFeedback/toListPage.jhtml?listType=1&system_privilege_code=reportFeedback_mine');
            outlookbar.additem('信息反馈-辖区所有', t, '${rc.getContextPath()}/zhsq/reportFeedback/toListPage.jhtml?system_privilege_code=reportFeedback_jurisdiction');
			
            t = outlookbar.addtitle('两违防治', '日常工作', 1);
            outlookbar.additem('两违防治-草稿', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=1&system_privilege_code=twoVioPre_draft');
            outlookbar.additem('两违防治-待办', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=2&system_privilege_code=twoVioPre_todo');
            
            outlookbar.additem('两违防治-两违状态跟踪', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=8&system_privilege_code=twoVioPre_statusTrack');
            /*outlookbar.additem('两违防治-处置情况报告', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=9&system_privilege_code=twoVioPre_situationReport');*/
            outlookbar.additem('两违防治-交办部门查处', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=10&system_privilege_code=twoVioPre_departmentDeal');

            outlookbar.additem('两违防治-经办', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=3&system_privilege_code=twoVioPre_handled');
            outlookbar.additem('两违防治-我发起的', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=4&system_privilege_code=twoVioPre_initiator');
            outlookbar.additem('两违防治-辖区所有', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=5&system_privilege_code=twoVioPre_jurisdiction');
            outlookbar.additem('两违防治-辖区归档', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toList.jhtml?listType=6&system_privilege_code=twoVioPre_archived');
            outlookbar.additem('两违防治-我的阅办', t, '${rc.getContextPath()}/zhsq/reportTwoVioPre/toMsgReadingList.jhtml?listType=7&system_privilege_code=twoVioPre_msgReading');
            
            t = outlookbar.addtitle('住建业务', '日常工作', 1);
            outlookbar.additem('房屋安全隐患-草稿', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=1&system_privilege_code=houseHiddenDanger_draft');
            outlookbar.additem('房屋安全隐患-待办', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=2&system_privilege_code=houseHiddenDanger_todo');
            
            outlookbar.additem('房屋安全隐患-隐患状态跟踪', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=8&system_privilege_code=houseHiddenDanger_statusTrack');
            
            outlookbar.additem('房屋安全隐患-经办', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=3&system_privilege_code=houseHiddenDanger_handled');
            outlookbar.additem('房屋安全隐患-我发起的', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=4&system_privilege_code=houseHiddenDanger_initiator');
            outlookbar.additem('房屋安全隐患-辖区所有', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=5&system_privilege_code=houseHiddenDanger_jurisdiction');
            outlookbar.additem('房屋安全隐患-辖区归档', t, '${rc.getContextPath()}/zhsq/reportHHD/toList.jhtml?listType=6&system_privilege_code=houseHiddenDanger_archived');
            outlookbar.additem('房屋安全隐患-我的阅办', t, '${rc.getContextPath()}/zhsq/reportHHD/toMsgReadingList.jhtml?listType=7&system_privilege_code=houseHiddenDanger_msgReading');
            
            t = outlookbar.addtitle('企业安全隐患', '日常工作', 1);
            outlookbar.additem('企业安全隐患-草稿', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=1&system_privilege_code=enterpriseHiddenDanger_draft');
            outlookbar.additem('企业安全隐患-待办', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=2&system_privilege_code=enterpriseHiddenDanger_todo');
            outlookbar.additem('企业安全隐患-经办', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=3&system_privilege_code=enterpriseHiddenDanger_handled');
            outlookbar.additem('企业安全隐患-我发起的', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=4&system_privilege_code=enterpriseHiddenDanger_initiator');
            outlookbar.additem('企业安全隐患-辖区所有', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=5&system_privilege_code=enterpriseHiddenDanger_jurisdiction');
            outlookbar.additem('企业安全隐患-辖区归档', t, '${rc.getContextPath()}/zhsq/reportEHD/toList.jhtml?listType=6&system_privilege_code=enterpriseHiddenDanger_archived');
            outlookbar.additem('企业安全隐患-我的阅办', t, '${rc.getContextPath()}/zhsq/reportEHD/toMsgReadingList.jhtml?listType=7&system_privilege_code=enterpriseHiddenDanger_msgReading');
            
            t = outlookbar.addtitle('疫情防控', '日常工作', 1);
            outlookbar.additem('疫情防控-草稿', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=1&system_privilege_code=epidemicPreControl_draft');
            outlookbar.additem('疫情防控-待办', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=2&system_privilege_code=epidemicPreControl_todo');
            outlookbar.additem('疫情防控-经办', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=3&system_privilege_code=epidemicPreControl_handled');
            outlookbar.additem('疫情防控-我发起的', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=4&system_privilege_code=epidemicPreControl_initiator');
            outlookbar.additem('疫情防控-辖区所有', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=5&system_privilege_code=epidemicPreControl_jurisdiction');
            outlookbar.additem('疫情防控-辖区归档', t, '${rc.getContextPath()}/zhsq/reportEPC/toList.jhtml?listType=6&system_privilege_code=epidemicPreControl_archived');
            outlookbar.additem('疫情防控-我的阅办', t, '${rc.getContextPath()}/zhsq/reportEPC/toMsgReadingList.jhtml?listType=7&system_privilege_code=epidemicPreControl_msgReading');
            
            t = outlookbar.addtitle('流域水质', '日常工作', 1);
            outlookbar.additem('流域水质-草稿', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=1&system_privilege_code=waterQuality_draft');
            outlookbar.additem('流域水质-待办', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=2&system_privilege_code=waterQuality_todo');
            outlookbar.additem('流域水质-经办', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=3&system_privilege_code=waterQuality_handled');
            outlookbar.additem('流域水质-我发起的', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=4&system_privilege_code=waterQuality_initiator');
            outlookbar.additem('流域水质-辖区所有', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=5&system_privilege_code=waterQuality_jurisdiction');
            outlookbar.additem('流域水质-辖区归档', t, '${rc.getContextPath()}/zhsq/reportWQ/toList.jhtml?listType=6&system_privilege_code=waterQuality_archived');
            outlookbar.additem('流域水质-我的阅办', t, '${rc.getContextPath()}/zhsq/reportWQ/toMsgReadingList.jhtml?listType=7&system_privilege_code=waterQuality_msgReading');
            
            t = outlookbar.addtitle('三会一课', '日常工作', 1);
            outlookbar.additem('三会一课-草稿', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=1&system_privilege_code=meetingsAndLesson_draft');
            outlookbar.additem('三会一课-待办', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=2&system_privilege_code=meetingsAndLesson_todo');
            outlookbar.additem('三会一课-经办', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=3&system_privilege_code=meetingsAndLesson_handled');
            outlookbar.additem('三会一课-我发起的', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=4&system_privilege_code=meetingsAndLesson_initiator');
            outlookbar.additem('三会一课-辖区所有', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=5&system_privilege_code=meetingsAndLesson_jurisdiction');
            outlookbar.additem('三会一课-辖区归档', t, '${rc.getContextPath()}/zhsq/reportMeeting/toList.jhtml?listType=6&system_privilege_code=meetingsAndLesson_archived');
            outlookbar.additem('三会一课-我的阅办', t, '${rc.getContextPath()}/zhsq/reportMeeting/toMsgReadingList.jhtml?listType=7&system_privilege_code=meetingsAndLesson_msgReading');

            t = outlookbar.addtitle('扶贫走访', '日常工作', 1);
            outlookbar.additem('扶贫走访-草稿', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=1&system_privilege_code=poorSupportVisit_draft');
            outlookbar.additem('扶贫走访-待办', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=2&system_privilege_code=poorSupportVisit_todo');
            outlookbar.additem('扶贫走访-经办', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=3&system_privilege_code=poorSupportVisit_handled');
            outlookbar.additem('扶贫走访-我发起的', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=4&system_privilege_code=poorSupportVisit_initiator');
            outlookbar.additem('扶贫走访-辖区所有', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=5&system_privilege_code=poorSupportVisit_jurisdiction');
            outlookbar.additem('扶贫走访-辖区归档', t, '${rc.getContextPath()}/zhsq/reportPSV/toList.jhtml?listType=6&system_privilege_code=poorSupportVisit_archived');
            outlookbar.additem('扶贫走访-我的阅办', t, '${rc.getContextPath()}/zhsq/reportPSV/toMsgReadingList.jhtml?listType=7&system_privilege_code=poorSupportVisit_msgReading');
            
            t = outlookbar.addtitle('农村建房', '日常工作', 1);
            outlookbar.additem('农村建房-草稿', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=1&system_privilege_code=ruralHousing_draft');
            outlookbar.additem('农村建房-待办', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=2&system_privilege_code=ruralHousing_todo');
            outlookbar.additem('农村建房-经办', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=3&system_privilege_code=ruralHousing_handled');
            outlookbar.additem('农村建房-我发起的', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=4&system_privilege_code=ruralHousing_initiator');
            outlookbar.additem('农村建房-辖区所有', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=5&system_privilege_code=ruralHousing_jurisdiction');
            outlookbar.additem('农村建房-辖区归档', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toList.jhtml?listType=6&system_privilege_code=ruralHousing_archived');
            outlookbar.additem('农村建房-我的阅办', t, '${rc.getContextPath()}/zhsq/reportRuralHousing/toMsgReadingList.jhtml?listType=7&system_privilege_code=ruralHousing_msgReading');
            
            t = outlookbar.addtitle('森林防灭火', '日常工作', 1);
            outlookbar.additem('森林防灭火-草稿', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=1&system_privilege_code=forestFirePrevention_draft');
            outlookbar.additem('森林防灭火-待办', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=2&system_privilege_code=forestFirePrevention_todo');
            outlookbar.additem('森林防灭火-经办', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=3&system_privilege_code=forestFirePrevention_handled');
            outlookbar.additem('森林防灭火-我发起的', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=4&system_privilege_code=forestFirePrevention_initiator');
            outlookbar.additem('森林防灭火-辖区所有', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=5&system_privilege_code=forestFirePrevention_jurisdiction');
            outlookbar.additem('森林防灭火-辖区归档', t, '${rc.getContextPath()}/zhsq/reportFFP/toList.jhtml?listType=6&system_privilege_code=forestFirePrevention_archived');
            outlookbar.additem('森林防灭火-我的阅办', t, '${rc.getContextPath()}/zhsq/reportFFP/toMsgReadingList.jhtml?listType=7&system_privilege_code=forestFirePrevention_msgReading');
            
            t = outlookbar.addtitle('营商问题', '日常工作', 1);
            outlookbar.additem('营商问题-草稿', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=1&system_privilege_code=businessProblem_draft');
            outlookbar.additem('营商问题-待办', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=2&system_privilege_code=businessProblem_todo');
            outlookbar.additem('营商问题-经办', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=3&system_privilege_code=businessProblem_handled');
            outlookbar.additem('营商问题-我发起的', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=4&system_privilege_code=businessProblem_initiator');
            outlookbar.additem('营商问题-辖区所有', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=5&system_privilege_code=businessProblem_jurisdiction');
            outlookbar.additem('营商问题-辖区归档', t, '${rc.getContextPath()}/zhsq/reportBusPro/toList.jhtml?listType=6&system_privilege_code=businessProblem_archived');
            outlookbar.additem('营商问题-我的阅办', t, '${rc.getContextPath()}/zhsq/reportBusPro/toMsgReadingList.jhtml?listType=7&system_privilege_code=businessProblem_msgReading');

            t = outlookbar.addtitle('致贫返贫监测', '日常工作', 1);
            outlookbar.additem('致贫返贫监测-草稿', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=1&system_privilege_code=povertyPreMonitor_draft');
            outlookbar.additem('致贫返贫监测-待办', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=2&system_privilege_code=povertyPreMonitor_todo');
            outlookbar.additem('致贫返贫监测-经办', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=3&system_privilege_code=povertyPreMonitor_handled');
            outlookbar.additem('致贫返贫监测-我发起的', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=4&system_privilege_code=povertyPreMonitor_initiator');
            outlookbar.additem('致贫返贫监测-辖区所有', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=5&system_privilege_code=povertyPreMonitor_jurisdiction');
            outlookbar.additem('致贫返贫监测-辖区归档', t, '${rc.getContextPath()}/zhsq/reportPPM/toList.jhtml?listType=6&system_privilege_code=povertyPreMonitor_archived');
            outlookbar.additem('致贫返贫监测-我的阅办', t, '${rc.getContextPath()}/zhsq/reportPPM/toMsgReadingList.jhtml?listType=7&system_privilege_code=povertyPreMonitor_msgReading');

            t = outlookbar.addtitle('信访人员稳控', '日常工作', 1);
            outlookbar.additem('信访人员稳控-草稿', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=1&system_privilege_code=petitionPerson_draft');
            outlookbar.additem('信访人员稳控-待办', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=2&system_privilege_code=petitionPerson_todo');
            outlookbar.additem('信访人员稳控-情况反馈', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=8&system_privilege_code=petitionPerson_statusTrack');
            outlookbar.additem('信访人员稳控-经办', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=3&system_privilege_code=petitionPerson_handled');
            outlookbar.additem('信访人员稳控-我发起的', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=4&system_privilege_code=petitionPerson_initiator');
            outlookbar.additem('信访人员稳控-辖区所有', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=5&system_privilege_code=petitionPerson_jurisdiction');
            outlookbar.additem('信访人员稳控-辖区归档', t, '${rc.getContextPath()}/zhsq/reportPetPer/toList.jhtml?listType=6&system_privilege_code=petitionPerson_archived');
            outlookbar.additem('信访人员稳控-我的阅办', t, '${rc.getContextPath()}/zhsq/reportPetPer/toMsgReadingList.jhtml?listType=7&system_privilege_code=petitionPerson_msgReading');
            
            t = outlookbar.addtitle('烈士纪念设施', '日常工作', 1);
            outlookbar.additem('烈士纪念设施-草稿', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=1&system_privilege_code=martyrsFacility_draft');
            outlookbar.additem('烈士纪念设施-待办', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=2&system_privilege_code=martyrsFacility_todo');
            outlookbar.additem('烈士纪念设施-经办', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=3&system_privilege_code=martyrsFacility_handled');
            outlookbar.additem('烈士纪念设施-我发起的', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=4&system_privilege_code=martyrsFacility_initiator');
            outlookbar.additem('烈士纪念设施-辖区所有', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=5&system_privilege_code=martyrsFacility_jurisdiction');
            outlookbar.additem('烈士纪念设施-辖区归档', t, '${rc.getContextPath()}/zhsq/reportMarFac/toList.jhtml?listType=6&system_privilege_code=martyrsFacility_archived');
            outlookbar.additem('烈士纪念设施-我的阅办', t, '${rc.getContextPath()}/zhsq/reportMarFac/toMsgReadingList.jhtml?listType=7&system_privilege_code=martyrsFacility_msgReading');

            t = outlookbar.addtitle('环境卫生整治', '日常工作', 1);
            outlookbar.additem('环境卫生整治-草稿', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=1&system_privilege_code=environmentHealTreatment_draft');
            outlookbar.additem('环境卫生整治-待办', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=2&system_privilege_code=environmentHealTreatment_todo');
            outlookbar.additem('环境卫生整治-经办', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=3&system_privilege_code=environmentHealTreatment_handled');
            outlookbar.additem('环境卫生整治-我发起的', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=4&system_privilege_code=environmentHealTreatment_initiator');
            outlookbar.additem('环境卫生整治-辖区所有', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=5&system_privilege_code=environmentHealTreatment_jurisdiction');
            outlookbar.additem('环境卫生整治-辖区归档', t, '${rc.getContextPath()}/zhsq/reportEHT/toList.jhtml?listType=6&system_privilege_code=environmentHealTreatment_archived');
            outlookbar.additem('环境卫生整治-我的阅办', t, '${rc.getContextPath()}/zhsq/reportEHT/toMsgReadingList.jhtml?listType=7&system_privilege_code=environmentHealTreatment_msgReading');

            t = outlookbar.addtitle('三合一整治', '日常工作', 1);
            outlookbar.additem('三合一整治-草稿', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=1&system_privilege_code=threeOneTreatment_draft');
            outlookbar.additem('三合一整治-待办', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=2&system_privilege_code=threeOneTreatment_todo');

            outlookbar.additem('三合一整治-隐患状态跟踪', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=8&system_privilege_code=threeOneTreatment_statusTrack');

            outlookbar.additem('三合一整治-经办', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=3&system_privilege_code=threeOneTreatment_handled');
            outlookbar.additem('三合一整治-我发起的', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=4&system_privilege_code=threeOneTreatment_initiator');
            outlookbar.additem('三合一整治-辖区所有', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=5&system_privilege_code=threeOneTreatment_jurisdiction');
            outlookbar.additem('三合一整治-辖区归档', t, '${rc.getContextPath()}/zhsq/reportTOT/toList.jhtml?listType=6&system_privilege_code=threeOneTreatment_archived');
            outlookbar.additem('三合一整治-我的阅办', t, '${rc.getContextPath()}/zhsq/reportTOT/toMsgReadingList.jhtml?listType=7&system_privilege_code=threeOneTreatment_msgReading');

            t = outlookbar.addtitle('三书一函', '日常工作', 1);
            outlookbar.additem('新增文书(公检法司使用)', t, '${rc.getContextPath()}/zhsq/eliminateLetterTho/add/index.jhtml');
            outlookbar.additem('待办文书(行业部门使用)', t, '${rc.getContextPath()}/zhsq/eliminateLetterTho/wait/index.jhtml?listType=reply');
            outlookbar.additem('文书审核(公检法司扫黑办使用)', t, '${rc.getContextPath()}/zhsq/eliminateLetterTho/wait/index.jhtml?listType=audit');
            outlookbar.additem('辖区文书', t, '${rc.getContextPath()}/zhsq/eliminateLetterTho/jurisdiction/index.jhtml');
            outlookbar.additem('归档文书(省扫黑办使用)', t, '${rc.getContextPath()}/zhsq/eliminateLetterTho/archive/index.jhtml');

        	t = outlookbar.addtitle('系统管理', '系统设置', 1)
            outlookbar.additem('定时器管理', t, '${rc.getContextPath()}/zhsq/timerManage/index.jhtml');
        	outlookbar.additem('数据管理', t, '${rc.getContextPath()}/zhsq/dataExchangeController/index.jhtml');
        	outlookbar.additem('统计图层样式配置', t, '${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/index.jhtml');
        	outlookbar.additem('框选配置管理', t, '${rc.getContextPath()}/zhsq/map/gisStatConfig/index.jhtml?statType=0');
        	outlookbar.additem('周边配置管理', t, '${rc.getContextPath()}/zhsq/map/gisStatConfig/index.jhtml?statType=1');
        	outlookbar.additem('专题图层资源管理', t, '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/index.jhtml');
        	outlookbar.additem('专题图层菜单配置', t, '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/listPageIndexCfg.jhtml');
        	outlookbar.additem('功能配置检测', t, '${rc.getContextPath()}/zhsq/checkConfigController/index.jhtml');
        	outlookbar.additem('事件采集标准配置', t, '${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/index.jhtml');
        	outlookbar.additem('事件流程环节办理人配置', t, '${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/toList.jhtml?system_privilege_code=zhsq_wf_actor_cfg');
            outlookbar.additem('log4j日志级别动态配置', t, '${rc.getContextPath()}/zhsq/hotChangeLog4jLevel/toIndex.jhtml');
            outlookbar.additem('天梯图初始化配置', t, '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/indexLa.jhtml');

        	t = outlookbar.addtitle('人文素质', '城市综管', 1)
        	outlookbar.additem('学校分布', t, '${rc.getContextPath()}/zhsq/szzg/school/index.jhtml');
        	outlookbar.additem('学生人数统计', t, '${rc.getContextPath()}/zhsq/szzg/schoolStat/index.jhtml?system_privilege_code=schoolStat');
            outlookbar.additem('文化程度分析', t, '${rc.getContextPath()}/zhsq/szzg/education/index.jhtml');
            outlookbar.additem('小学就读分析', t, '${rc.getContextPath()}/zhsq/szzg/education/toPriEdu.jhtml');

        	//outlookbar.additem('城市运行', t, '${rc.getContextPath()}/zhsq/map/thresholdcolorcfg/thresholdColorCfg/index.jhtml');

            t = outlookbar.addtitle('应急预案', '城市综管', 1)
            outlookbar.additem('应急预案树', t, '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/treeIndex.jhtml');
            outlookbar.additem('应急预案内容', t, '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/contentIndex.jhtml');

            t = outlookbar.addtitle('公共服务', '城市综管', 1)
            outlookbar.additem('医院信息管理', t, '${rc.getContextPath()}/zhsq/szzg/hospital/index.jhtml');
 			outlookbar.additem('水电煤', t, '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/index.jhtml');
 
            t = outlookbar.addtitle('生活环境 ', '城市综管',1)
            outlookbar.additem('水质量', t, '${rc.getContextPath()}/zhsq/szzg/water/index.jhtml');
            outlookbar.additem('绿化指标', t, '${rc.getContextPath()}/zhsq/szzg/greenindicators/index.jhtml');
            outlookbar.additem('城市绿化分布', t, '${rc.getContextPath()}/zhsq/szzg/green/index.jhtml');
            outlookbar.additem('国家森林达标情况', t, '${rc.getContextPath()}/zhsq/szzg/greenstd/index.jhtml');



        	t = outlookbar.addtitle('人口基本情况', '城市综管', 1)
			outlookbar.additem('退休人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001005');
			outlookbar.additem('低保人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001006');
			outlookbar.additem('失业人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001007');
			outlookbar.additem('服兵役人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001008');
			outlookbar.additem('残疾人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001009');
			outlookbar.additem('居家养老人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001010');
			outlookbar.additem('上访人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001011');
			outlookbar.additem('吸毒人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001012');
			outlookbar.additem('邪教人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001013');
			outlookbar.additem('矫正人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001014');
			outlookbar.additem('刑释解教人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001015');
			outlookbar.additem('重精神病人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001016');
			outlookbar.additem('危险从业人员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001017');
			outlookbar.additem('人口趋势', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001003');
			outlookbar.additem('人口基本', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001001');
			outlookbar.additem('困难党员', t, '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/index.jhtml?pcode=S001004');
		
           
			t = outlookbar.addtitle('经济运行', '城市综管', 1)
            outlookbar.additem('地区生产总值', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s1');
            outlookbar.additem('财政收支', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s2');
            outlookbar.additem('固定资产投资', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s3');
            outlookbar.additem('社会消费品零售总额', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s4');
            outlookbar.additem('对外经济', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s5');
            outlookbar.additem('工商注册', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s6');
            outlookbar.additem('房地产情况', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s7');
            outlookbar.additem('拆迁安置情况', t, '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/index.jhtml?stype=s8');
            outlookbar.additem('行政处罚', t, '${rc.getContextPath()}/zhsq/szzg/zgPenaltyController/index.jhtml?stype=s9');
            outlookbar.additem('驰名商标', t, '${rc.getContextPath()}/zhsq/szzg/trademark/index.jhtml?stype=s9');
            outlookbar.additem('守重企业',t,'${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/index.jhtml');

          t = outlookbar.addtitle('告警信息', '城市综管', 1)
          outlookbar.additem('告警信息', t, '${rc.getContextPath()}/zhsq/szzg/zgAlarm/index.jhtml');
          t = outlookbar.addtitle('调度资源管理', '城市综管', 1)
          outlookbar.additem('资源树管理', t, '${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=index');
          outlookbar.additem('资源信息管理', t, '${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=index&id=info');
        
        </script>
        <!-- CP_check菜单 -->
        <!-- 彩信退订管理员菜单 -->

</body>
</html>
