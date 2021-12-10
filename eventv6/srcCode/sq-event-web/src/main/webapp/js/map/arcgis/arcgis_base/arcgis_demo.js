
/**
 * 2014-05-26 liushi add arcgis地图配置信息
 *
 **/
function ArcgisMapConfigInfo(){
	this.Init = function(data) {
		this.id = data.id;
		this.orgCode = data.orgCode;
		this.theme = data.theme;//主题用于控制各个首页差异的一般不用
		this.mapStartType = data.mapStartType;//map_start_type;//默认显示地图类型
		this.mapFile = data.mapFile;//map_file;//二维地图文件路径
		this.mapFile3D = data.mapFile3D;//map_file_3D;//三维地图文件路径
		this.mapCenterX = data.mapCenterX;//map_center_x;//二维地图中心点位x
		this.mapCenterY = data.mapCenterY;//map_center_y;//二维地图中心点位y
		this.mapCenterLevel = data.mapCenterLevel;//map_center_level;//二维地图默认显示级别
		this.mapCenterX3D = data.mapCenterX3D;//map_center_x_3D;//三维地图中心点位x
		this.mapCenterY3D = data.mapCenterY3D;//map_center_y_3D;//三维地图中心点位y
		this.mapCenterLevel3D = data.mapCenterLevel3D;//map_center_level_3D;//三维地图默认显示级别
		this.mapType = data.mapType;//map_type;//二维地图类型
		this.mapType3D = data.mapType3D;//map_type_3D;//三维地图类型
		this.mapMinl = data.mapMinl;//map_minl;//二维地图最小缩放
		this.mapMaxl = data.mapMaxl;//map_maxl;//二维地图最大缩放
		this.mapMinl3D = data.mapMinl3D;//map_minl_3D;//三维地图最小缩放
		this.mapMaxl3D = data.mapMaxl3D;//map_maxl_3D;//三维地图最大缩放
		this.positionOffsetX = data.positionOffsetX;//position_offset_x;//二维地图定位偏移量
		this.positionOffsetY = data.positionOffsetY;//position_offset_y;//二维地图定位偏移量
		this.positionOffsetX3D = data.positionOffsetX3D;//position_offset_x_3D;//三维地图定位偏移量
		this.positionOffsetY3D = data.positionOffsetY3D;//position_offset_y_3D;//三维地图定位偏移量
		this.engineType = data.engineType;//引擎类.gis、arcgis
		this.contrastColor = data.contrastColor;//地图对比颜色（跟地图底色有鲜明对比的颜色）
	}
}

function CurrentArcgisMapInfo() {
	this.Init2D = function(data) {
		this.id = data.id;
		this.orgCode = data.orgCode;
		this.theme = data.theme;//主题用于控制各个首页差异的一般不用
		this.mapFile = data.mapFile;//map_file;//二维地图文件路径
		this.mapCenterX = data.mapCenterX;//map_center_x;//二维地图中心点位x
		this.mapCenterY = data.mapCenterY;//map_center_y;//二维地图中心点位y
		this.mapCenterLevel = data.mapCenterLevel;//map_center_level;//二维地图默认显示级别
		this.mapType = data.mapType;//map_type;//二维地图类型
		this.mapMinl = data.mapMinl;//map_minl;//二维地图最小缩放
		this.mapMaxl = data.mapMaxl;//map_maxl;//二维地图最大缩放
		this.positionOffsetX = data.positionOffsetX;//position_offset_x;//二维地图定位偏移量
		this.positionOffsetY = data.positionOffsetY;//position_offset_y;//二维地图定位偏移量
		this.engineType = data.engineType;//引擎类.gis、arcgis
		this.contrastColor = data.contrastColor;//地图对比颜色（跟地图底色有鲜明对比的颜色）
	}
	
	this.Init3D = function(data) {
		this.id = data.id;
		this.orgCode = data.orgCode;
		this.theme = data.theme;//主题用于控制各个首页差异的一般不用
		this.mapFile = data.mapFile3D;//map_file_3D;//三维地图文件路径
		this.mapCenterX = data.mapCenterX3D;//map_center_x_3D;//三维地图中心点位x
		this.mapCenterY = data.mapCenterY3D;//map_center_y_3D;//三维地图中心点位y
		this.mapCenterLevel = data.mapCenterLevel3D;//map_center_level_3D;//三维地图默认显示级别
		this.mapType = data.mapType3D;//map_type_3D;//三维地图类型
		this.mapMinl = data.mapMinl3D;//map_minl_3D;//三维地图最小缩放
		this.mapMaxl = data.mapMaxl3D;//map_maxl_3D;//三维地图最大缩放
		this.positionOffsetX = data.positionOffsetX3D;//position_offset_x_3D;//三维地图定位偏移量
		this.positionOffsetY = data.positionOffsetY3D;//position_offset_y_3D;//三维地图定位偏移量
		this.engineType = data.engineType;//引擎类.gis、arcgis
		this.contrastColor = data.contrastColor;//地图对比颜色（跟地图底色有鲜明对比的颜色）
	}
}

//2014-05-26 liushi add 地图配置信息对象
var arcgisMapConfigInfo = new ArcgisMapConfigInfo();
//2014-05-26 liushi add 当前地图的信息
var currentArcgisMapInfo = new CurrentArcgisMapInfo();
var arcgisConfigInfos;
var currentArcgisConfigInfo;
//记录当前显示地图图层的方法以及所含的参数
var currentLayerLocateFunctionStr;
var currentLayerListFunctionStr;

//2014-05-26 liushi add 加载地图   参数：加载map的div的id名称、加载地图缩放条的div的id名称
function loadArcgisMap(mapDivId,jsSliderDivId,jsSwitchMap) {
	 $("#"+mapDivId).ffcsMap({
			mapId  : mapDivId,
			slider : false,
			x : currentArcgisMapInfo.mapCenterX,
	    	y : currentArcgisMapInfo.mapCenterY,
	    	zoom : currentArcgisMapInfo.mapCenterLevel
	});
	
	var currentMap = $("#"+mapDivId).ffcsMap.getMap();
	if(jsSliderDivId != undefined && jsSliderDivId != null) {
		$("#"+jsSliderDivId).ffcsSlider({
	        map:currentMap,
	        position:"left-top",
	        imageRoot: js_ctx +"/js/map/arcgis/library/mnbootstrap/images/nav/",
	        maxLevel:arcgisConfigInfo.arcgisScalenInfos.length-1,
	        minLevel:0,
	        currentLevel:arcgisConfigInfo.zoom
		});
	}
	
	if(jsSwitchMap == undefined || jsSwitchMap == null) {
		switchMap('un');
	}else{
		switchMap('vec');
	}
}

function loadArcgis(arcgisConfigInfo,mapDivId,jsSliderDivId,switchMapId) {
	switchArcgis(arcgisConfigInfo,0)
}

var winresizeflag=0;
var currentMapDiv = null;
var currentMapType = 0;
var currentLevel = 0;
var currentX = 0;
var currentY = 0;
var currentN = 0;

function switchArcgis(arcgisConfigInfo,n) {
	currentN = n;
	if(currentMapDiv != null) {
		var aa = $(currentMapDiv).ffcsMap.getCurrentMapCenterObj();
		if(aa.centerPoint != undefined) {
			currentMapType = currentArcgisConfigInfo.mapType;
			currentLevel = aa.level;
			currentX = aa.centerPoint.x;
			currentY = aa.centerPoint.y;
		}

		currentMapDiv.style.display = 'none';
		// 移除原来底图图层
		var mapobj=$(currentMapDiv).ffcsMap.getMap();
		var layerIds = mapobj.layerIds;
		var layerIdsLength = layerIds.length;
		try {
			mapobj.removeAllLayers();
		} catch (e) {

		}
		currentMapDiv.innerHTML = "";
	}

	if(currentLevel != -1){
		$("#map" + n).ffcsMap({
			mapId: "map" + n,
			slider: false,
			height: $(document).height(),
			width: $(document).width(),
			x: arcgisConfigInfo.mapCenterX,          //中心点X坐标
			y: arcgisConfigInfo.mapCenterY,           //中心点y坐标
			zoom: currentLevel
		});
	}else{
		$("#map" + n).ffcsMap({
			mapId: "map" + n,
			slider: false,
			height: $(document).height(),
			width: $(document).width(),
			x: arcgisConfigInfo.mapCenterX,          //中心点X坐标
			y: arcgisConfigInfo.mapCenterY,           //中心点y坐标
			zoom : arcgisConfigInfo.zoom
		});
	}


	currentMapDiv = document.getElementById("map"+n);
	currentMapDiv.style.display = 'block';

	var currentMap = $("#map"+n).ffcsMap.getMap();
	$(".esriControlsBR").hide();

	currentArcgisConfigInfo = arcgisConfigInfo;
	var levelDetailObj=new Array();
	var arcgisScalenInfos = arcgisConfigInfo.arcgisScalenInfos;
	var currentLevelDetail;

	if(arcgisScalenInfos.length>0){
		for(var i=0; i<arcgisScalenInfos.length; i++) {
			levelDetailObj[i] = {};
			levelDetailObj[i]['level']=arcgisScalenInfos[i].scaleLevel;
			levelDetailObj[i]['resolution']=arcgisScalenInfos[i].scaleResolution;
			levelDetailObj[i]['scale']=arcgisScalenInfos[i].scaleScale;
		}
		currentLevelDetail = {
			lods:levelDetailObj
		};

	}else{
		currentLevelDetail = LevelDetailBackUp;
	}

	switchClearMyLayer();
	LevelDetail = $.extend({}, LevelDetail , currentLevelDetail);
	$("#jsSlider").ffcsSlider({
	        map:currentMap,
	        position:"left-top",
	        imageRoot: js_ctx +"/js/map/arcgis/library/mnbootstrap/images/nav/",
	        maxLevel:LevelDetail.lods.length-1,
	        minLevel:0,
	        currentLevel:arcgisConfigInfo.zoom
	});
	var arcgisServiceInfos = arcgisConfigInfo.arcgisServiceInfos;

	//加载新图层
	for(var i=0; i<arcgisServiceInfos.length; i++){
		var serviceLoadType = arcgisServiceInfos[i].serviceLoadType
		var GCS = {
			wkid : arcgisConfigInfo.wkid,
			xmin : arcgisServiceInfos[i].serviceXmin,
			ymin : arcgisServiceInfos[i].serviceYmin,
			xmax : arcgisServiceInfos[i].serviceXmax,
			ymax : arcgisServiceInfos[i].serviceYmax,
			rows : arcgisConfigInfo.mapRows,
			cols : arcgisConfigInfo.mapCols,
			origin : {"x" : arcgisConfigInfo.mapOrgiginX, "y" : arcgisConfigInfo.mapOrgiginY},
			imageFormat : arcgisServiceInfos[i].serviceImageFormat,
			serviceMode : arcgisServiceInfos[i].serviceServiceMode,
			layerId : arcgisServiceInfos[i].serviceLayerId,
			tileMatrixSetId : arcgisServiceInfos[i].serviceTileMartrixSetId,
			tileMatrix : arcgisServiceInfos[i].serviceTileMartrix
		}
		$.fn.ffcsMap.createLayer(arcgisServiceInfos[i].serviceUrl, arcgisServiceInfos[i].serviceLoadType, GCS);
	}

	if(window.document.getElementById("map0")!= undefined) {
		getArcgisDataByCurrentSet();
	}

	if(currentMapType == currentArcgisConfigInfo.mapType && currentLevel != -1) {
		console.log("*********:"+currentX+"--------:"+currentY+"------:"+currentLevel);
		$("#map"+n).ffcsMap.centerAt({
			x : currentX,          //中心点X坐标
			y : currentY,           //中心点y坐标
			wkid : arcgisConfigInfo.wkid, //wkid 2437
			zoom : currentLevel
	   	});
	}else if(document.getElementById("gridId") != undefined){
		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	}

	if(window.document.getElementById("kuangxuanConfig")!= undefined){
		var kuangxuanframe = window.document.getElementById("kuangxuanConfig").contentWindow;
		if(kuangxuanframe.mapt != undefined && kuangxuanframe.mapt != currentArcgisConfigInfo.mapType){
			kuangxuanframe.setNeedKuangxuan();
		}
	}

	if(currentLayerLocateFunctionStr != undefined && currentLayerLocateFunctionStr!=''){
		eval(currentLayerLocateFunctionStr)
	}
}

function switchArcgisTaijiang(arcgisConfigInfo,n) {
	modleopen();
	currentN = n;
	
	if(currentMapDiv != null) {
		var aa = $(currentMapDiv).ffcsMap.getCurrentMapCenterObj();
		currentMapType = currentArcgisConfigInfo.mapType;
		currentLevel = aa.level;
		currentX = aa.centerPoint.x;
		currentY = aa.centerPoint.y;
		currentMapDiv.style.display = 'none';
		// 移除原来底图图层
		var mapobj=$(currentMapDiv).ffcsMap.getMap();
		var layerIds = mapobj.layerIds;
		var layerIdsLength = layerIds.length;
		mapobj.removeAllLayers();
		currentMapDiv.innerHTML = "";
	}
	
	$("#map"+n).ffcsMap({
		mapId  : "map"+n,
		slider : false,
		height : $(document).height(),
		width : $(document).width(),
    	x : arcgisConfigInfo.mapCenterX,          //中心点X坐标
		y : arcgisConfigInfo.mapCenterY,           //中心点y坐标
		zoom : arcgisConfigInfo.zoom
	});
	
	currentMapDiv = document.getElementById("map"+n);
	currentMapDiv.style.display = 'block';
	
	var currentMap = $("#map"+n).ffcsMap.getMap();
	$(".esriControlsBR").hide();

	currentArcgisConfigInfo = arcgisConfigInfo;
	var levelDetailObj=new Array();
	var arcgisScalenInfos = arcgisConfigInfo.arcgisScalenInfos;
	var currentLevelDetail;
	
	if(arcgisScalenInfos.length>0){
		for(var i=0; i<arcgisScalenInfos.length; i++) {
			levelDetailObj[i] = {};
			levelDetailObj[i]['level']=arcgisScalenInfos[i].scaleLevel;
			levelDetailObj[i]['resolution']=arcgisScalenInfos[i].scaleResolution;
			levelDetailObj[i]['scale']=arcgisScalenInfos[i].scaleScale;
		}
		currentLevelDetail = {
			lods:levelDetailObj
		};
	}else{
		currentLevelDetail = LevelDetailBackUp;
	}
	
	switchClearMyLayer();
	
	LevelDetail = $.extend({}, LevelDetail , currentLevelDetail);
	
	$("#jsSlider").ffcsSlider({
	        map:currentMap,
	        position:"left-top",
	        imageRoot: js_ctx +"/js/map/arcgis/library/mnbootstrap/images/nav/",
	        maxLevel:LevelDetail.lods.length-1,
	        minLevel:0,
	        currentLevel:arcgisConfigInfo.zoom
	});
	
	var arcgisServiceInfos = arcgisConfigInfo.arcgisServiceInfos;
	
	//加载新图层
	for(var i=0; i<arcgisServiceInfos.length; i++){
		var serviceLoadType = arcgisServiceInfos[i].serviceLoadType
		var GCS = {
			wkid : arcgisConfigInfo.wkid,
			xmin : arcgisServiceInfos[i].serviceXmin,
			ymin : arcgisServiceInfos[i].serviceYmin,
			xmax : arcgisServiceInfos[i].serviceXmax,
			ymax : arcgisServiceInfos[i].serviceYmax,
			rows : arcgisConfigInfo.mapRows,
			cols : arcgisConfigInfo.mapCols,
			origin : {"x" : arcgisConfigInfo.mapOrgiginX, "y" : arcgisConfigInfo.mapOrgiginY},
			imageFormat : arcgisServiceInfos[i].serviceImageFormat,
			serviceMode : arcgisServiceInfos[i].serviceServiceMode,
			layerId : arcgisServiceInfos[i].serviceLayerId,
			tileMatrixSetId : arcgisServiceInfos[i].serviceTileMartrixSetId,
			tileMatrix : arcgisServiceInfos[i].serviceTileMartrix
		}
		$.fn.ffcsMap.createLayer(arcgisServiceInfos[i].serviceUrl, arcgisServiceInfos[i].serviceLoadType, GCS);
	}
	
	if(window.document.getElementById("map0")!= undefined) {
		getArcgisDataByCurrentSet();
	}
	
	if(currentMapType == currentArcgisConfigInfo.mapType) {
		$("#map"+n).ffcsMap.centerAt({
			x : currentX,          //中心点X坐标
			y : currentY,           //中心点y坐标
			wkid : arcgisConfigInfo.wkid, //wkid 2437
			zoom : currentLevel
	   	});
	}else{
		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	}
	
	if(window.document.getElementById("kuangxuanConfig")!= undefined){
		var kuangxuanframe = window.document.getElementById("kuangxuanConfig").contentWindow;
		if(kuangxuanframe.mapt != undefined && kuangxuanframe.mapt != currentArcgisConfigInfo.mapType){
			kuangxuanframe.setNeedKuangxuan();
		}
	}
	
	if(currentLayerLocateFunctionStr != undefined && currentLayerLocateFunctionStr!=''){
		//alert(currentLayerLocateFunctionStr);
		eval(currentLayerLocateFunctionStr)
	}
	
	modleclose();
}

function threeWeiClick(){
	var mapStyleDiv = document.getElementById("mapStyleDiv");
	if(mapStyleDiv.style.display == 'none') {
		mapStyleDiv.style.display = 'block';
	}else {
		mapStyleDiv.style.display = 'none';
	}
}

function switchArcgisByNumber(obj,n) {
	if(currentMapStyleObj != obj){
		currentMapStyleObj.className = "";
		obj.className = "current";
		currentMapStyleObj = obj;
		switchArcgis(arcgisConfigInfos[n],n);
	}
}

function switchMap(type) {
	if (type == 'img') {
		$("#vecMap").show();
		$("#imgMap").hide();
	} else if(type == 'vec'){
		$("#vecMap").hide();
		$("#imgMap").show();
	} else{
		$("#vecMap").hide();
		$("#imgMap").hide();
	}
	
var _LevelDetail = {
		//江西地图比例尽
		lods :[
		     	  {"level" : 0, "resolution" :  4.758922011660561, "scale" :  2000000000},
		    	 {"level" : 1, "resolution" : 4.045083709911476, "scale" : 1700000000},
		    	 {"level" : 2, "resolution" : 3.3312454081623923, "scale" : 1400000000},
		    	 {"level" : 3, "resolution" :  2.855353206996336, "scale" : 1200000000},
		    	 {"level" : 4, "resolution" : 2.3794610058302803, "scale" : 1000000000},
		    	 {"level" : 5, "resolution" :  1.9035688046642243, "scale" : 800000000},
		    	 {"level" : 6, "resolution" :  1.427676603498168, "scale" :  600000000},
		    	 {"level" : 7, "resolution" : 0.713838301749084, "scale" : 300000000}
		    ]
	};
	//江西2.5D
	var newCommon = {
			//wkid : 4490,
			xmin : -365.35119266055153, 
			ymin : -1494.6818348623858, 
			xmax : 4288.351192660551, 
			ymax : 171.79999999999973 ,
			origin : { "x" : -400.0,"y" : 922.9},
			layerId : "MapServer",
			tileMatrix : 0   
		};
	

	
	//是wmts时,比例尺不同时使用
	
	//江西范围参数
	//TianDiTu.VEC_BASE_GCS = $.extend({}, TianDiTu.VEC_BASE_GCS , newCommon);
	
	// 影像地图	
	if (type == 'img') {
	    $.fn.ffcsMap.createLayer(currentArcgisMapInfo.mapFile, "wmts", TianDiTu.IMG_BASE_GCS);
	    $.fn.ffcsMap.createLayer(currentArcgisMapInfo.mapFile, "wmts", TianDiTu.IMG_ANNO_GCS);
	
		//重庆影像
	    //var url = "http://www.digitalcq.com/RemoteRest/services/CQMap_IMG/MapServer";
		//var url_lb = "http://www.digitalcq.com/RemoteRest/services/CQMap_IMG_LABEL/MapServer";
		//$.fn.ffcsMap.createLayer(url, "tiled",TianDiTu.IMG_BASE_GCS);
		//$.fn.ffcsMap.createLayer(url_lb, "tiled",TianDiTu.IMG_ANNO_GCS);
		
	} else if (type == 'vec') { // 普通地图
		//重庆
		//var url = "http://www.digitalcq.com/RemoteRest/services/CQMap_VEC/MapServer";
		//$.fn.ffcsMap.createLayer(url, "tiled",TianDiTu.VEC_BASE_GCS);
		
		//江西
		//var url = "http://59.63.161.126:6080/arcgis/rest/services/nancang/MapServer/WMTS";
		//$.fn.ffcsMap.createLayer(url , "wmts", TianDiTu.VEC_BASE_GCS);
		
		$.fn.ffcsMap.createLayer(currentArcgisMapInfo.mapFile, "wmts", TianDiTu.VEC_BASE_GCS);
		$.fn.ffcsMap.createLayer(currentArcgisMapInfo.mapFile, "wmts", TianDiTu.VEC_ANNO_GCS);
	} else {
		$.fn.ffcsMap.createLayer(currentArcgisMapInfo.mapFile, "tiled", TianDiTu.VEC_BASE_GCS);
	}
}

function clearMyLayer() {
	$("#map"+currentN).ffcsMap.clear({layerName : "hlhxLayer"});//护路护线
	$("#map"+currentN).ffcsMap.clear({layerName : "gridsLayer"});//全球眼
	$("#map"+currentN).ffcsMap.clear({layerName : "globalEyesLayer"});//全球眼
	$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});     //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});     //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"}); //网格员 巡逻段警
	$("#map"+currentN).ffcsMap.clear({layerName : "buildingLayer"});  //房
	$("#map"+currentN).ffcsMap.clear({layerName : "keyPlaceLayer"});  //重点场所
	$("#map"+currentN).ffcsMap.clear({layerName : "peopleLayer"});    //人
	$("#map"+currentN).ffcsMap.clear({layerName : "corLayer"});       //企业
	$("#map"+currentN).ffcsMap.clear({layerName : "rentRoomLayer"});  //出租屋
	$("#map"+currentN).ffcsMap.clear({layerName : "partyGroupLayer"});//党组织
	$("#map"+currentN).ffcsMap.clear({layerName : "resourceLayer"});  //物
	$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"}); //在办事件（旧）
	$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});   //事件调度（旧）
	$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});   //重大紧急事件
	$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});   //待办事件
	$("#map"+currentN).ffcsMap.clear({layerName : "socialOrganizationLayer"});  //巡逻队、警务室、治保组织等社会组织
	$("#map"+currentN).ffcsMap.clear({layerName : "casesLayer"});            //案件警情
	$("#map"+currentN).ffcsMap.clear({layerName : "dangousLayer"});            //隐患
	$("#map"+currentN).ffcsMap.clear({layerName : "newSocialOrgLayer"});            //新社会组织
	$("#map"+currentN).ffcsMap.clear({layerName : "nonPublicOrgLayer"});            //新经济组织 
	$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});            //应急指挥事件
	$("#map"+currentN).ffcsMap.clear({layerName : "segmentLayer"});            //城市综合体
	$("#map"+currentN).ffcsMap.clear({layerName : "fkqySegmentLayer"});             //防控区域
	$("#map"+currentN).ffcsMap.clear({layerName : "swjcLayer"});             //水文
	$("#map"+currentN).ffcsMap.clear({layerName : "tempLayer"});             //定位图层
	$("#map"+currentN).ffcsMap.clear({layerName : "careRoadMemberLayer"});  //护路护线队员
	$("#map"+currentN).ffcsMap.clear({layerName : "safetyPersonManageLayer"});  //消防安全管理员
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});  //楼宇经济楼宇
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});  //楼宇经济企业
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
	$("#map"+currentN).ffcsMap.clear({layerName : "newResourceLayer"});  //（消防栓、天然水源、自来水公司）
	$("#map"+currentN).ffcsMap.clear({layerName : "serviceOutletsLayer"});  //便民服务网点
	$("#map"+currentN).ffcsMap.clear({layerName : "importUnitLayer"});	//重点单位
	$("#map"+currentN).ffcsMap.clear({layerName : "campusLayer"});  //校园周边
	$("#map"+currentN).ffcsMap.clear({layerName : "fireTeamLayer"});  //消防队
	$("#map"+currentN).ffcsMap.clear({layerName : "controlsafetyRanksLayer"});  //安监队伍
	$("#map"+currentN).ffcsMap.clear({layerName : "parkingManageLayer"});  //两车管理
	$("#map"+currentN).ffcsMap.clear({layerName : "ridkLayer"});  //安全隐患
	$("#map"+currentN).ffcsMap.clear({layerName : "storeLayer"});  //门店
	$("#map"+currentN).ffcsMap.clear({layerName : "landscapeLayer"});  //园林绿化
	currentLayerLocateFunctionStr="";
}

function switchClearMyLayer() {//进行地图切换时不需要将定位调用记录清空
	$("#map"+currentN).ffcsMap.clear({layerName : "storeLayer"});  //门店
	$("#map"+currentN).ffcsMap.clear({layerName : "landscapeLayer"});  //园林绿化
	$("#map"+currentN).ffcsMap.clear({layerName : "hlhxLayer"});//护路护线
	$("#map"+currentN).ffcsMap.clear({layerName : "gridsLayer"});//全球眼
	$("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});//全球眼
	$("#map"+currentN).ffcsMap.clear({layerName : "globalEyesLayer"});//全球眼
	$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});     //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});     //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"}); //网格员 巡逻段警
	$("#map"+currentN).ffcsMap.clear({layerName : "buildingLayer"});  //房
	$("#map"+currentN).ffcsMap.clear({layerName : "keyPlaceLayer"});  //重点场所
	$("#map"+currentN).ffcsMap.clear({layerName : "peopleLayer"});    //人
	$("#map"+currentN).ffcsMap.clear({layerName : "corLayer"});       //企业
	$("#map"+currentN).ffcsMap.clear({layerName : "rentRoomLayer"});  //出租屋
	$("#map"+currentN).ffcsMap.clear({layerName : "partyGroupLayer"});//党组织
	$("#map"+currentN).ffcsMap.clear({layerName : "resourceLayer"});  //物
	$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"}); //在办事件（旧）
	$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});   //事件调度（旧）
	$("#map"+currentN).ffcsMap.clear({layerName : "socialOrganizationLayer"});  //巡逻队、警务室、治保组织等社会组织
	$("#map"+currentN).ffcsMap.clear({layerName : "casesLayer"});            //案件警情
	$("#map"+currentN).ffcsMap.clear({layerName : "newSocialOrgLayer"});            //新社会组织
	$("#map"+currentN).ffcsMap.clear({layerName : "nonPublicOrgLayer"});            //新经济组织 
	$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});            //应急指挥事件
	$("#map"+currentN).ffcsMap.clear({layerName : "segmentLayer"});            //城市综合体
	$("#map"+currentN).ffcsMap.clear({layerName : "fkqySegmentLayer"});            //防控区域
	$("#map"+currentN).ffcsMap.clear({layerName : "swjcLayer"});            //水文
	$("#map"+currentN).ffcsMap.clear({layerName : "tempLayer"});             //定位图层
	$("#map"+currentN).ffcsMap.clear({layerName : "careRoadMemberLayer"});  //护路护线队员
	$("#map"+currentN).ffcsMap.clear({layerName : "safetyPersonManageLayer"});  //消防安全管理员
	$("#map"+currentN).ffcsMap.clear({layerName : "serviceOutletsLayer"});  //便民服务网点
	$("#map"+currentN).ffcsMap.clear({layerName : "importUnitLayer"});	//重点单位
	$("#map"+currentN).ffcsMap.clear({layerName : "campusLayer"});	//校园
	$("#map"+currentN).ffcsMap.clear({layerName : "newResourceLayer"});  //（消防栓、天然水源、自来水公司）
	$("#map"+currentN).ffcsMap.clear({layerName : "fireTeamLayer"});  //消防队
	$("#map"+currentN).ffcsMap.clear({layerName : "controlsafetyRanksLayer"});  //安监队伍
	$("#map"+currentN).ffcsMap.clear({layerName : "parkingManageLayer"});  //两车管理
	$("#map"+currentN).ffcsMap.clear({layerName : "ridkLayer"});  //安全隐患
}

//查询网格轮廓数据并显示
function getArcgisDataOfGrids(gridId,gridCode,mapt,gridLevel) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridLayer"});
	//locateCenterAndLevel(gridId,mapt);
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
	var imgurl = js_ctx +'/js/map/arcgis/library/style/images/wglocal.png';
	if(IS_GRID_ARCGIS_SHOW_CENTER_POINT != undefined && IS_GRID_ARCGIS_SHOW_CENTER_POINT == '1') {
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,14,true,getInfoDetailOnMap);
	}else{
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:342,h:225},"gridLayer",'网格',getInfoDetailOnMap,showGridDetail); 
	if(document.getElementById("buildName0").checked == true){
		getArcgisDataOfBuildsPoints(gridId,gridCode,mapt)
	}
}

//网格
function getArcgisDataOfGridsListByIds(url) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridsLayer"});
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('gridsLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:342,h:225},"gridsLayer", '网格' ,getInfoDetailOnMap,showGridDetail);
}

//护路护线
function getArcgisDataOfICareRoad(url) {
	$("#map"+currentN).ffcsMap.clear({layerName : "hlhxLayer"});
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('hlhxLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:170},"hlhxLayer", '护路护线' ,getInfoDetailOnMap);
}

//便民服务网点
function getArcgisDataOfServiceOutletsListByIds(url) {
	$("#map"+currentN).ffcsMap.clear({layerName : "serviceOutletsLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_serviceOutlets.png";
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('serviceOutletsLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:322,h:170},"serviceOutletsLayer", '便民服务网点' ,getInfoDetailOnMap);
}

//两车管理
function getArcgisDataOfParkingManageListByIds(url) {
	$("#map"+currentN).ffcsMap.clear({layerName : "parkingManageLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/parking_manage.png";
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('parkingManageLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:322,h:220},"parkingManageLayer", '两车管理' ,getInfoDetailOnMap);
}

//查询防控区域轮廓数据并显示
function getArcgisDataOfFkqySegmentGrids(gridId,gridCode,mapt,gridLevel) {
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfFkqyGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('fkqySegmentLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:0,h:0},"fkqySegmentLayer",'防控区域',showFkqySegmentGridsOfQqy); 
	
}

//点击防空区域的时候显示区域内的全球眼
function showFkqySegmentGridsOfQqy(data){
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/standardFkqyGlobalEyes.jhtml?geoString='+data['hs'];
	url+='&standard=standard';
	url+="&mapt="+currentArcgisConfigInfo.mapType;
	
	$(".titlefirstall .title").css("display","block");
	$(".titlefirstall").css("display","block");
	$("#titlePath").html("专题图层 > 全球眼");
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	showListPanel(url);
}

function locateCenterAndLevel(gridId,mapt) {
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json?mapt='+mapt+'&wid='+gridId+'&t='+Math.random();
	$.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		    var list = data.list;
		    if(list!=null && list.length == 1) {
		    	var obj = list[0];
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.mapCenterLevel
		    	});
		    }else if(currentMapType == currentArcgisConfigInfo.mapType){
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : currentX,          //中心点X坐标
					y : currentY,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : currentLevel
			   	});
		    }
		 }
	 });
}

//查询楼栋轮廓数据并显示
function getArcgisDataOfBuilds(gridId,gridCode,mapt) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "buildLayer"});
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuilds.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('buildLayer',url,1,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayer",'楼栋名片',getInfoDetailOnMap,showBuildDetail,lc_sanwei_function);
}
function getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "buildLayerPoint"});
    var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/build/build_locate_point.png";
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuildsPoints.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('buildLayerPoint',url,0,true,imageurl, 11, 14);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayerPoint",'楼栋名片',getInfoDetailOnMap,showBuildDetail,lc_sanwei_function);
}
var buildLayerNum=0;
var lc_sanwei_function=null;
//查询楼栋中心点数据并进行中心点定位(2015-01-21 liushi add 三维地图改为楼宇轮廓显示)
function getArcgisDataOfBuildsPoints(gridId,gridCode,mapt) {
	
	if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
		lc_sanwei_function = showSkylineViewDetail;
	}
	if(currentArcgisConfigInfo.mapTypeCode == 6) {
		if(IS_IMAGE_MAP_SHOW_CONTOUR == '1') {
			getArcgisDataOfBuilds(gridId,gridCode,mapt);
		}else {
			getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt);
		}
		
	}else if(currentArcgisConfigInfo.mapTypeCode == 5) {
		if(IS_VECTOR_MAP_SHOW_CONTOUR == '1') {
			getArcgisDataOfBuilds(gridId,gridCode,mapt);
		}else {
			getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt);
		}
	}else {
		getArcgisDataOfBuilds(gridId,gridCode,mapt);
	}
}

//根据ids查询全球眼定位数据进行定位
function getArcgisDataOfGlobalEyes(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "globalEyesLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_globalEyes.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('globalEyesLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:475,h:406},"globalEyesLayer",'全球眼',getInfoDetailOnMap);
}

//根据ids查询水文监测点位数据进行定位
function getArcgisLocateDataListOfSwjc(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "swjcLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/swjc.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('swjcLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:910,h:400},"swjcLayer",'水文点位信息',getInfoDetailOnMap);
}

//根据ids查询在办事件定位数据进行定位
function getArcgisDataOfWorkingEvent(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('efficiencySupervisionEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"efficiencySupervisionEventLayer",'在办事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询历史事件定位数据进行定位
function getArcgisDataOfHistoricalEvent(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('efficiencySupervisionEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"efficiencySupervisionEventLayer",'历史事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询重大紧急事件定位数据进行定位
function getArcgisDataOfImportantEvent(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('importantEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"importantEventLayer",'重大紧急事件',getInfoDetailOnMap,showEfficiencySupervisionDetail);
}

//根据ids查询个人辖区待办事件定位数据进行定位
function getArcgisDataOfDbEvent(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('dbEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"dbEventLayer",'待办事件',getInfoDetailOnMap,showEfficiencySupervisionDetail);
}

//根据ids查询历史事件定位数据进行定位
function getArcgisDataOfYjzhEvent(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('yjzhEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"yjzhEventLayer",'事件信息',getInfoDetailOnMap,showYjzhEventDetail);
}

//根据ids查询事件定位数据进行定位
function getArcgisDataOfEvent(url,res){
	//console.info(res);
	$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:230},"eventLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询事件调度模块的事件定位数据进行定位
function getArcgisDataOfPending(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventSchedulingLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"eventSchedulingLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

function impEventCrossDomainCallBack(eventResultsId){
	$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});
	var url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/getArcgisLocateDataListOfPending.jhtml?ids="+eventResultsId+"&showType=2";
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventSchedulingLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"eventSchedulingLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询事件定位数据进行定位（紧急事件） 
function getArcgisDataOfUrgencyEvent(url,res){
	$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_red.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('urgencyEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:230},"urgencyEventLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

function clearGridAdminLayer() {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"});
}

//根据ids查询网格员定位数据进行定位
function getArcgisDataOfGridAdminOrXldj(url,type){
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_gridAdmin.png";
	var title = '网格员';
	if(type=='xldj'){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_policeteam.png";
		title = '巡逻段警';
	}
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('gridAdminLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:390,h:300},"gridAdminLayer",title,getInfoDetailOnMap);
}

//根据ids查询消防安全管理员定位数据进行定位
function getArcgisDataOfSafetyPersonManage(url,type){
	$("#map"+currentN).ffcsMap.clear({layerName : "safetyPersonManageLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/safety_person_manage.png";
	var title = '消防安全管理员';
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('safetyPersonManageLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:240},"safetyPersonManageLayer",title,getInfoDetailOnMap);
}

//根据ids查询护路护线队员定位数据进行定位
function getArcgisDataOfCareRoadMember(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "careRoadMemberLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/careRoad_member.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('careRoadMemberLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:390,h:300},"careRoadMemberLayer","护路护线队员",getInfoDetailOnMap);
}

//根据ids查询出租屋定位数据进行定位
function getArcgisDataOfRentRoom(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "rentRoomLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/region_let.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('rentRoomLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:365,h:215},"rentRoomLayer",'出租屋名片',getInfoDetailOnMap,showRentRoomDetail);
}

//根据ids查询"物"定位数据进行定位
function getArcgisDataOfResource(type,url){
	var imageurl;
	var title;
	
	switch(type) {
		case "02020401":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_busStation.png";
			title="公交站";
			break;
		case "02010301":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_toilet.png";
			title="公共厕所";
			break;
		case "02010501":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_streetlight.png";
			title="路灯";
			break;
		case "020107":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_wellLid.png";
			title="井盖";
			break;
		case "020201":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongshui.png";
			title="供水";
			break;
		case "020202":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongre.png";
			title="供热";
			break;
		case "020203":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_ranqi.png";
			title="燃气";
			break;
		case "02010601":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_fireHydrant.png";
			title="消防栓";
			break;
		case "020304":
			imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_jws.png";
			title="派出所";
			break;
		case "0601":
		case "0602":
			//新消防栓
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_fireHydrant.png";
			title="消防栓";
			break;
		case "0603":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_naturalWaterSource.png";
			title="天然水源";
			break;
		case "0604":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongshui.png";
			title="自来水公司";
			break;
	}
		
	$("#map"+currentN).ffcsMap.clear({layerName : "resourceLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('resourceLayer',url,0,true,imageurl, 30, 39);
	if("0601"==type || "0602"==type || "0603"==type || "0604"==type){
		$("#map"+currentN).ffcsMap.clear({layerName : "newResourceLayer"});
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:240},"newResourceLayer",title,getInfoDetailOnMap,showBuildDetail);
	}else{
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:320,h:170},"resourceLayer",title,getInfoDetailOnMap);
	}
}

//根据ids查询新社会组织定位数据进行定位
function getArcgisDataOfNewSocialOrg(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "newSocialOrgLayer"});
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/new_society.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('newSocialOrgLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:195},"newSocialOrgLayer",'新社会组织',getInfoDetailOnMap,showNewSocialOrgDetail);
}

//根据ids查询新经济组织定位数据进行定位
function getArcgisDataOfNonPublicOrg(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "nonPublicOrgLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/new_economy.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('nonPublicOrgLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:195},"nonPublicOrgLayer",'新经济组织',getInfoDetailOnMap,showNonPublicOrgDetail);
}

//根据ids查询"房"定位数据进行定位
function getArcgisDataOfBuilding(url,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_dormitory.png";
	if(type == "wyglzz") {//物业管理住宅 001
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_estateManagement.png";
	}else if(type == "sqtgzz") {//社区托管住宅 002
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_communityManaged.png";	
	}else if(type == "dwzgzz") {//单位自管住宅 003
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsManaged.png";
	}else if(type == "ss") {//宿舍 004
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_dormitory.png";
	}else if(type == "mf") {//民房 005
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_citizenHouse.png";
	}else if(type == "xzl") {//写字楼 006

	}else if(type == "dwl") {//单位楼 007
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	}else if(type == "cszht") {//城市综合体 008
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_urbanComplex.png";
	}else if(type == "gygc") {//公园广场 009
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_park.png";
	}else if(type == "lg") {//旅馆 010
	
	}else if(type == "yjy") {//影剧院 011
	
	}else if(type == "tyg") {//体育馆 012
	
	}else if(type == "yy") {//医院 013
	
	}else if(type == "yh") {//银行 014
	
	}else if(type == "sm") {//寺庙 015
		
	}else if(type == "gd") {//工地 016
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_workSite.png";
	}else if(type == "ck") {//仓库 017
	
	}else if(type == "szly") {//商住两用 018
	
	}else if(type == "xx") {//学校 019
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_school.png";
	}else if(type == "cf") {//厂房 020
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_factory.png";
	}else if(type == "qt") {//其他 999
	
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "buildingLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('buildingLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:235},"buildingLayer",'楼栋名片',getInfoDetailOnMap,showBuildDetail);
}

//楼宇经济楼栋定位
function getArcgisDataOfLyjjBuilding(url){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_dormitory.png";
	//清除地图上原来的标记
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('lyjjBuildingLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:235},"lyjjBuildingLayer",'楼栋名片',getInfoDetailOnMap , showLyjjBuildingDetail);
}

//根据ids查询重点场所定位数据进行定位
function getArcgisDataOfKeyPlace(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "keyPlaceLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/region_keyPlace.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('keyPlaceLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 340,h : 195},"keyPlaceLayer",'重点场所名片',getInfoDetailOnMap,showKeyPlaceDetail);
}

//巡逻队、警务室、治保组织等社会组织
function getArcgisDataOfSociety(url,type) {
	var title = "";
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/region_keyPlace.png";
	if(type=="03"){//治保会
		imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_zbh.png";
		title = "治保会名片";
	}else if(type=="04"){//警务室
		imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_jws.png";
		title = "警务室名片";
	}else if(type=="05"){//巡逻队
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_policeteam.png";
		title = "巡逻队名片";
	}
	
	$("#map"+currentN).ffcsMap.clear({layerName : "socialOrganizationLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('socialOrganizationLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 700,h : 380},"socialOrganizationLayer",title,getInfoDetailOnMap);
}

//根据ids查询案件警情定位数据进行定位
function getArcgisDataOfCases(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "casesLayer"});
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_ajjq.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('casesLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 485,h : 310},"casesLayer",'案件警情名片',getInfoDetailOnMap);
}

//根据ids查询隐患定位数据进行定位
function getArcgisDataOfDangous(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "dangousLayer"});
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_yh.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('dangousLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 645,h : 320},"dangousLayer",'隐患名片',getInfoDetailOnMap);
}

//根据ids查询安全隐患定位数据进行定位
function getArcgisDataOfRisk(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "riskLayer"});
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_yh.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('riskLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 635,h : 345},"riskLayer",'安全隐患',getInfoDetailOnMap,showRiskDetail);
}

//根据ids查询企业定位数据进行定位
function getArcgisDataOfCor(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "corLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/region_company.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('corLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 340,h : 245},"corLayer",'企业名片',getInfoDetailOnMap,showCorBaseDetail);
}

//根据ids查询楼宇经济企业定位数据进行定位
function getArcgisDataOfLyjjCorp(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});
	
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/lyjj_corp.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('lyjjCorLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 340,h : 245},"lyjjCorLayer",'企业名片',getInfoDetailOnMap,showlyjjCorpBaseDetail);
}

//根据ids查询党组织定位数据进行定位
function getArcgisDataOfPartyGroup(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "partyGroupLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_partyOrganization.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('partyGroupLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:160},"partyGroupLayer",'党组织',getInfoDetailOnMap,showPartyOrgDetail);
}

var peopleType = "";
//根据ids查询定位数据进行定位
function getArcgisDataOfPeople(url,type){
	peopleType = type;
	var imageurl="";
	if(type == "partyMerber"){//党员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_partyMember.png";
	}else if(type == "volunteer"){//志愿者
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_volunteer.png";
	}else if(type == "retire"){//退休人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_retire.png";
	}else if(type == "homeAge"){//居家养老
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_yanglao.png";
	}else if(type == "military"){//兵役
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_serveArmy.png";
	}else if(type == "unemployment"){//失业
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_unemployed.png";
	}else if(type == "welfare"){//低保
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_lowObject.png";
	}else if(type == "disability"){//残障人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_physicalDisabilities.png";
	}else if(type == "neuropathy") {//精神病人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_psychosis.png";
	}else if(type == "dangerous") {//危险品从业
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_dangerousGoods.png";
	}else if(type == "petition") {//上访人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_petitioner.png";
	}else if(type == "drugs") {//吸毒人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_drugAddict.png";
	}else if(type == "heresy") {//邪教人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_heresy.png";
	}else if(type == "rectify") {//矫正
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_rectify.png";
	}else if(type == "camps") {//刑释解教人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/people_RehabilitationXieJiao.png";
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "peopleLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('peopleLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:400,h:235},"peopleLayer",'人员名片',getInfoDetailOnMap,showPopDetail);
}

//城市综合体
function getArcgisDataOfSegmentGrid(url, type) {
	var title = "城市综合体";
	
	if (type == '02') {
		title = "木屋片区";
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "segmentLayer"});
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('segmentLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:850,h:410},"segmentLayer", title ,getInfoDetailOnMap);
}

function showGridDetail(id, title) {
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    var url = sq_zzgrid_url+"/zzgl/map/data/gridBase/standardGridDetailIndex.jhtml?gridId="+id;
	showMaxJqueryWindow(title,url,850,400);
}

function showEventDetail(id, title) {
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	//console.info("showEventDetail -> "+id);
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	var workFlowId = arr[1];
	var instanceId = arr[2];
	var taskId = arr[3];
	var eventType = arr[4];
	var url = js_ctx+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
	showMaxJqueryWindow(title,url,900,400);	
}

function showEfficiencySupervisionDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	
	if(height>520){
		height = 520;
	}
	
	showMaxJqueryWindow(title,url,1140,height);	
}

function showYjzhEventDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	
	if(height>520){
		height = 520;
	}
	
	showMaxJqueryWindow(title,url,1140,height);	
}

function showPendingDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	
	if(height>520){
		height = 520;
	}
	
	showMaxJqueryWindow(title,url,1140,height);
}

function showBuildDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+id;
	var height = $(document).height()-20;
	
	if(height>520){
		height = 520;
	}
	
	showMaxJqueryWindow(title,url,948,405);	
}

//显示楼宇经济楼宇详情
function showLyjjBuildingDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/building/view.jhtml?buildingId="+id+"&fromMap=1";
	
	showMaxJqueryWindow(title,url,980,420);
}
//显示楼宇经济企业详情
function showlyjjCorpBaseDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/corpBase/view.jhtml?corpId="+id+"&fromMap=1";
	showMaxJqueryWindow(title,url,980,420);
}

function showRentRoomDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/map/data/region/rentRoomDetail.jhtml?rentId='+id;
	 showMaxJqueryWindow(title,url,920,400);	
}

function showCorBaseDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/corBaseInfo/detail.jhtml?cbiId=' + id;;
	 showMaxJqueryWindow(title,url,945,410);	
}

function showPopDetail(id, title) {
	var population_url =  document.getElementById("POPULATION_URL").value;
	var url = population_url+"/cirs/viewResident.jhtml?menu=1&ciRsId="+id;
	showMaxJqueryWindow(title,url,800,370);	
}

function showKeyPlaceDetail(id ,title){
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/placeInfo/detail.jhtml?plaId='+id;
	showMaxJqueryWindow(title,url,850,410);
}

function showPartyOrgDetail(id,title){
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofthing/getPartyOrgInfoDetailOnMap.jhtml?partyGroupId='+id;
	showMaxJqueryWindow(title,url,800,330);
}

// 新经济组织详细信息
function showNonPublicOrgDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/nonPublicOrg/detail.jhtml?cbiId='+id;
	showMaxJqueryWindow(title,url,850,410);
}

// 新社会组织详细信息
function showNewSocialOrgDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/society/organization/detail.jhtml?flag=1&orgId='+id;
	showMaxJqueryWindow(title,url,850,410);
}

//新消防栓、自然水源、自来水公司详细信息
function showNewSourceDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/fireGrid/fireResource/detail.jhtml?id='+id;
	showMaxJqueryWindow(title,url,850,410);
}
//门店详细信息
function showStoreDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/store/detail.jhtml?storeId='+id+'&showClose=false';
	showMaxJqueryWindow(title,url,600,400);
}

//校园周边详细信息
function showCampusDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/campus/detail.jhtml?plaId='+id;
	showMaxJqueryWindow(title,url,850,410);
}

//机构队伍、群防群治队伍、安监队伍详细信息
function showControlsafetyRanksDetail0(id, title) {
	var teamId = id;
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/newPrvetionTeam/new/detail.jhtml?showClose=1&teamId='+teamId+'&bizType=0';
	showMaxJqueryWindow(title,url,610,280);
}

//机构队伍、群防群治队伍、安监队伍详细信息
function showControlsafetyRanksDetail1(id, title) {
	var teamId = id;
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/newPrvetionTeam/new/detail.jhtml?bizType=1&showClose=1&teamId='+teamId;
	showMaxJqueryWindow(title,url,610,280);
}

//机构队伍、群防群治队伍、安监队伍详细信息
function showControlsafetyRanksDetail3(id, title) {
	var teamId = id;
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/newPrvetionTeam/new/detail.jhtml?bizType=3&showClose=1&teamId='+teamId;
	showMaxJqueryWindow(title,url,610,280);
}

//安全隐患详细信息
function showRiskDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/productSafety/routineExamination/detail.jhtml?checkId='+id;
	showMaxJqueryWindow(title,url,980,400);
}

//地图列表信息显示回调函数
function getInfoDetailOnMap(data) {
	var layer = data['layerName'];
	var url = "";
	var context = "";
	if(layer == 'gridLayer' || layer == 'gridsLayer') {//网格图层信息显示
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if (layer == 'serviceOutletsLayer') {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getServiceOutletsInfoDetailOnMap.jhtml?soId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="serviceOutlets_info" name="serviceOutlets_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if (layer == 'parkingManageLayer') {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofbuilding/getParkingManageInfoDetailOnMap.jhtml?pmId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="parkingManage_info" name="parkingManage_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if (layer == 'hlhxLayer') {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getICareRoadInfoDetailOnMap.jhtml?lotId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="iCareRoad_info" name="iCareRoad_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == 'buildLayer' || layer=='buildLayerPoint') {//楼宇图层信息显示
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="build_info" name="build_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == 'peopleLayer') {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofpoplocal/getPeopleInfoDetailOnMap.jhtml?ciRsId='+data['wid']+'&t='+Math.random()+'&gridId='+$("#gridId").val();
    	context = '<iframe id="people_info" name="people_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "globalEyesLayer") {
		//引用zzgrid链接
		var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        var url =  sq_zzgrid_url +'/zzgl/map/data/situation/globalEyesPlay.jhtml?monitorId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="globalEyes_info" name="globalEyes_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "swjcLayer") {
		//引用水文监测链接
		var water_monitor_url = document.getElementById("WATER_MONITOR_URL").value;
        var url = water_monitor_url + '/water/waterList.jhtml?setButton=true&monitorId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="water_monitor" name="water_monitor" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "gridAdminLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="gridAdmin_info" name="gridAdmin_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "safetyPersonManageLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofpoplocal/corpStaffDetail.jhtml?corpStaffId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="safetyPersonManage_info" name="safetyPersonManage_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "careRoadMemberLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/careRoadMemberDetail.jhtml?memberId='+data['wid']+'&imsi='+data['id']+'&t='+Math.random();
    	context = '<iframe id="careRoadMember_info" name="careRoadMember_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "eventLayer" || layer == "urgencyEventLayer") {
		var arr = new Array();
		arr = data['wid'].split(",");
		var wid = arr[0];
		var workFlowId = arr[1];
		var instanceId = arr[2]
		url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+wid+'&taskId='+data['taskId']+'&mapt='+currentArcgisConfigInfo.mapType+'&t='+Math.random();
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "buildingLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "keyPlaceLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/keyPlaceDetail.jhtml?plaId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="keyPlace_info" name="keyPlace_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "resourceLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/resourceDetail.jhtml?resId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="resource_info" name="resource_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "corLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getCorInfoDetailOnMap.jhtml?cbiId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "rentRoomLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/rentRoomDetail.jhtml?rentId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="rentRoom_info" name="rentRoom_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "partyGroupLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofthing/getPartyOrgSummaryInfoOnMap.jhtml?partyGroupId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="efficiencySupervisionEventLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&mapt='+currentArcgisConfigInfo.mapType+'&t='+Math.random();
    	context = '<iframe id="efficiencySupervision_info" name="efficiencySupervision_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="importantEventLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="importantEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="segmentLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	var url = sq_zzgrid_url +'/zzgl/grid/segmentGrid/detail/'+data['wid']+'.jhtml?segmentType=taijiangpianqu';
    	context = '<iframe id="segment_info" name="segment_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="dbEventLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="dbEventLayer_info" name="dbEventLayer_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="eventSchedulingLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&taskInfoId='+data['taskId']+'&mapt='+currentArcgisConfigInfo.mapType+'&t='+Math.random();
    	context = '<iframe id="pending_info" name="pending_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="socialOrganizationLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/society/organization/detail.jhtml?orgId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="pending_info" name="pending_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="casesLayer"){
    	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
		url =  sq_zzgrid_url +'/zzgl/resident/cases/detail.jhtml?ccId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="cases_info" name="cases_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="dangousLayer"){
    	var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSocietyController/viewDangerous.jhtml?id='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="dangous_info" name="dangous_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>';
    	return context;
    }else if(layer=="riskLayer"){
    	var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/viewRisk.jhtml?id='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="risk_info" name="risk_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>';
    	return context;
    }else if(layer=="nonPublicOrgLayer"){
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofnewgroup/nonPublicOrgDetail.jhtml?orgId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="nonPublicOrg_info" name="nonPublicOrg_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="newSocialOrgLayer"){
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofnewgroup/newSocialOrgDetail.jhtml?orgId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="newSocialOrg_info" name="newSocialOrg_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="yjzhEventLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	if(data['taskId'] == undefined) {
    		url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&mapt='+currentArcgisConfigInfo.mapType+'&t='+Math.random();
    	}else {
    		url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&taskInfoId='+data['taskId']+'&mapt='+currentArcgisConfigInfo.mapType+'&t='+Math.random();
    	}
    	
    	context = '<iframe id="yjzhEvent_info" name="yjzhEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjBuildingLayer") {//楼宇经济楼宇
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjBuildingInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjCorLayer") {//楼宇经济企业
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjCorpBaseInfoDetailOnMap.jhtml?corpId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjEventLayer") {//楼宇经济事件
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjEventInfoDetailOnMap.jhtml?eventId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "newResourceLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/newResourceDetail.jhtml?resId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="new_resource_info" name="new_resource_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "importUnitLayer"){//重点单位
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofthing/importUnitDetail.jhtml?importUnitId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="importUnit_info" name="importUnit_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "campusLayer") {//校园周边 
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/campusDetail.jhtml?plaId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="campus_info" name="campus_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    } else if (layer == "fireTeamLayer") {
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/fireTeamDetail.jhtml?resId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="fireTeam_info" name="fireTeam_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if (layer == "controlsafetyRanksLayer") {//安监队伍
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?teamId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="controlsafetyRanks_info" name="controlsafetyRanks_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }
    
	return context;
}

function localtionGridsPoints(id) {
	$("#map"+currentN).ffcsMap.locationPoint({w:342,h:225},'gridsLayer', id, '网格信息', '',30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId='+id+'&t='+Math.random();
    	context = '<iframe id="globalEyes_info" name="globalEyes_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showGridDetail);
}

function locationGlobalEyesPoint(id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/situation_globalEyes.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 475,h : 406},'globalEyesLayer', id, '全球眼播放信息', imageurl,30,39, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        var url =  sq_zzgrid_url +'/zzgl/map/data/situation/globalEyesPlay.jhtml?monitorId='+id+'&t='+Math.random();
        //var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesPlay.jhtml?monitorId='+id;
    	context = '<iframe id="globalEyes_info" name="globalEyes_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

function locationSwjcPoint(id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/swjc.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 910,h : 400},'swjcLayer', id, '水文监测点位信息', imageurl,30,39, function(data){
        var water_monitor_url = document.getElementById("WATER_MONITOR_URL").value;
        var url = water_monitor_url + '/water/waterList.jhtml?setButton=true&monitorId='+id+'&t='+Math.random();
    	context = '<iframe id="water_monitor" name="water_monitor" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//地图网格员或巡逻段警列表点击触发事件
function localtionGridAdminOrXldjPoint(id,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/situation_gridAdmin.png";
	var title = '网格员信息';
	if(type=='xldj'){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_policeteam.png";
		title = '巡逻段警信息';
	}
	$("#map"+currentN).ffcsMap.locationPoint({w:390,h:300},'gridAdminLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+id+'&t='+Math.random();
    	context = '<iframe id="gridAdmin_info" name="gridAdmin_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//地图便民服务网点列表点击触发事件
function localtionServiceOutletsPoint(id,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_serviceOutlets.png";
	var title = '便民服务网点信息';
	$("#map"+currentN).ffcsMap.locationPoint({w:322,h:170},'serviceOutletsLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getServiceOutletsInfoDetailOnMap.jhtml?soId='+id+'&t='+Math.random();
    	context = '<iframe id="serviceOutlets_info" name="serviceOutlets_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//地图两车管理列表点击触发事件
function localtionParkingManagePoint(id,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/parking_manage.png";
	var title = '两车管理信息';
	$("#map"+currentN).ffcsMap.locationPoint({w:322,h:220},'parkingManageLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofbuilding/getParkingManageInfoDetailOnMap.jhtml?pmId='+id+'&t='+Math.random();
    	context = '<iframe id="parkingManage_info" name="parkingManage_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//地图消防安全管理员列表点击触发事件
function localtionSafetyPersonManagePoint(id,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/safety_person_manage.png";
	var title = '消防安全管理员信息';
	$("#map"+currentN).ffcsMap.locationPoint({w:360,h:240},'safetyPersonManageLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofpoplocal/corpStaffDetail.jhtml?corpStaffId='+id+'&t='+Math.random();
    	context = '<iframe id="gridAdmin_info" name="gridAdmin_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//地图护路护线队员列表点击触发事件
function localtionCareRoadMemberPoint(id,imsi){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/careRoad_member.png";
	$("#map"+currentN).ffcsMap.locationPoint({w:390,h:300},'careRoadMemberLayer', id, '护路护线队员', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/careRoadMemberDetail.jhtml?memberId='+id+'&imsi='+imsi+'&t='+Math.random();
    	context = '<iframe id="careRoadMember_info" name="careRoadMember_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

function localtionBuildingPoint(type,id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_dormitory.png";
	if(type == "wyglzz") {//物业管理住宅 001
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_estateManagement.png";
	}else if(type == "sqtgzz") {//社区托管住宅 002
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_communityManaged.png";	
	}else if(type == "dwzgzz") {//单位自管住宅 003
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsManaged.png";
	}else if(type == "ss") {//宿舍 004
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_dormitory.png";
	}else if(type == "mf") {//民房 005
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_citizenHouse.png";
	}else if(type == "xzl") {//写字楼 006

	}else if(type == "dwl") {//单位楼 007
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	}else if(type == "cszht") {//城市综合体 008
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_urbanComplex.png";
	}else if(type == "gygc") {//公园广场 009
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_park.png";
	}else if(type == "lg") {//旅馆 010
	
	}else if(type == "yjy") {//影剧院 011
	
	}else if(type == "tyg") {//体育馆 012
	
	}else if(type == "yy") {//医院 013
	
	}else if(type == "yh") {//银行 014
	
	}else if(type == "sm") {//寺庙 015
		
	}else if(type == "gd") {//工地 016
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_workSite.png";
	}else if(type == "ck") {//仓库 017
	
	}else if(type == "szly") {//商住两用 018
	
	}else if(type == "xx") {//学校 019
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_school.png";
	}else if(type == "cf") {//厂房 020
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_factory.png";
	}else if(type == "qt") {//其他 999
	
	}
	$("#map"+currentN).ffcsMap.locationPoint({w:370,h:235},'buildingLayer', id, '楼栋', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+id+'&t='+Math.random();
    	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showBuildDetail);
}

//重点单位列表点击触发事件
function localImportUnitPoint(id, type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_importUnit.png";
	var title = '重点单位信息';
	$("#map"+currentN).ffcsMap.locationPoint({w:360,h:260},'importUnitLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofthing/importUnitDetail.jhtml?importUnitId='+id+'&t='+Math.random();
    	context = '<iframe id="importUnit_info" name="importUnit_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//重点单位地图定位点击触发事件
function getArcgisDataOfImportUnit(url,type){
	clearImportUnitLayer();
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_importUnit.png";
	var title = '重点单位信息';
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('importUnitLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:260},"importUnitLayer",title,getInfoDetailOnMap);
}

//清除重点单位图层
function clearImportUnitLayer(){
	$("#map"+currentN).ffcsMap.clear({layerName : "importUnitLayer"});
}

//加载楼宇经济楼宇定位数据
function localtionLyjjBuildingPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	$("#map"+currentN).ffcsMap.locationPoint({w:370,h:235},'lyjjBuildingLayer', id, '楼栋名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjBuildingInfoDetailOnMap.jhtml?buildingId='+id+'&t='+Math.random();
  	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
  	return context;
  },showLyjjBuildingDetail);
}

//加载楼宇经济企业定位数据
function localtionLyjjCorpPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	
	$("#map"+currentN).ffcsMap.locationPoint({w:370,h:235},'lyjjCorLayer', id, '企业名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjCorpBaseInfoDetailOnMap.jhtml?corpId='+id+'&t='+Math.random();
		context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		return context;
	},showlyjjCorpBaseDetail);
}

//事件点击列表弹出层
function localtionEventPoint(type,id,instanceId,workFlowId,eventType,urgencyDegree){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	var layer = "eventLayer";
	
	if(urgencyDegree!='01'){
		var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_red.gif";
		layer = "urgencyEventLayer";
	}
	
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	
	$("#map"+currentN).ffcsMap.locationPoint({w:340,h:230},layer, id, '事件信息', imageurl, 18, 28, function(data){
        url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent/'+ eventType +'.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId;
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEventDetail);
}

//事件点击列表弹出层没有概要！
function localtionEventPointNoInfo(type,id,instanceId,workFlowId,eventType,urgencyDegree){
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	var workFlowId = arr[1];
	var instanceId = arr[2];
	var taskId = arr[3];
	var eventType = arr[4];
	var url = js_ctx+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
	showMaxJqueryWindow("待办事件",url,900,400);	
}

//在办事件点击列表弹出层
function localtionEfficiencySupervisionPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'efficiencySupervisionEventLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="efficiencySupervision_info" name="efficiencySupervision_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//重大紧急事件点击列表弹出层
function localtionImportantEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'importantEventLayer', eventId, '重大紧急事件', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="importantEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//待办事件点击列表弹出层
function localtionDbEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'dbEventLayer', eventId, '待办事件', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="dbEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//城市综合体事件点击列表弹出层
function localtionSegmentGridPoint(id,type){
	var title = "城市综合体";
	
	if (type == '02') {
		title = "木屋片区";
	}
	
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	
	$("#map"+currentN).ffcsMap.locationPoint({w:850,h:410},'segmentLayer', id, title, imageurl, 18, 28, function(data){
		var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	var url = sq_zzgrid_url +'/zzgl/grid/segmentGrid/detail/'+id+'.jhtml?segmentType=taijiangpianqu';
    	//url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="segment_info" name="segment_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    });
}

//应急指挥里面的事件处理点击列表弹出层
function localtionYjzhEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'yjzhEventLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="yjzhEvent_info" name="yjzhEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showYjzhEventDetail);
}

//事件调度模块事件点击列表弹出层
function localtionPendingPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'eventSchedulingLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid+'&mapt='+currentArcgisConfigInfo.mapType;
    	context = '<iframe id="pending_info" name="pending_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showPendingDetail);
}

function impEventClickCrossDomainCallBack(tid,eventId,type) {
	localtionPendingPoint(tid,eventId);
}

function localtionKeyPlacePoint(id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/region_keyPlace.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 190},'keyPlaceLayer', id, '重点场所名片', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/keyPlaceDetail.jhtml?plaId='+id+'&t='+Math.random();
    	context = '<iframe id="keyPlace_info" name="keyPlace_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showKeyPlaceDetail);
}

function localtionRentRoomPoint(id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/region_let.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 365,h : 215},'rentRoomLayer', id, '出租屋名片', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/rentRoomDetail.jhtml?rentId='+id+'&t='+Math.random();
    	context = '<iframe id="rentRoom_info" name="rentRoom_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showRentRoomDetail); 
}

function localtionSocietyPoint(id,type){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/region_let.png";
	if(type=="03"){//治保会
		imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_zbh.png";
		title = "治保会名片";
	}else if(type=="04"){//警务室
		imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_jws.png";
		title = "警务室名片";
	}else if(type=="05"){//巡逻队
		imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_policeteam.png";
		title = "巡逻队名片";
	}
	$("#map"+currentN).ffcsMap.locationPoint({w : 450,h : 300},'socialOrganizationLayer', id, title, imageurl,30,39, function(data){
    	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
		var url =  sq_zzgrid_url +'/zzgl/society/organization/detail.jhtml?orgId=' +id+'&t='+Math.random();
    	context = '<iframe id="society_info" name="society_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    }); 
}

function localtionCasesPoint(id){
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_ajjq.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 485,h : 310},'casesLayer', id, '案件警情名片', imageurl,30,39, function(data){
    	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
		var url =  sq_zzgrid_url +'/zzgl/resident/cases/detail.jhtml?ccId='+id+'&t='+Math.random();
    	context = '<iframe id="cases_info" name="cases_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    }); 
}

function localtionDangousPoint(id){
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_yh.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 645,h : 320},'dangousLayer', id, '隐患名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSocietyController/viewDangerous.jhtml?id='+id+'&t='+Math.random();
    	context = '<iframe id="dangous_info" name="dangous_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>';
    	return context;
	}); 
}

// 安全隐患列表点击定位
function localtionRiskPoint(id){
	var imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_yh.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 635,h : 345},'riskLayer', id, '安全隐患', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/viewRisk.jhtml?id='+id+'&t='+Math.random();
    	context = '<iframe id="risk_info" name="risk_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>';
    	return context;
	}, showRiskDetail); 
}

function localtionResourcePoint(type,id){
	var imageurl;
	var title;
	
	switch(type) {
		case "02020401":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_busStation.png";
			title="公交站";
			break;
		case "02010301":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_toilet.png";
			title="公共厕所";
			break;
		case "02010501":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_streetlight.png";
			title="路灯";
			break;
		case "020107":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_wellLid.png";
			title="井盖";
			break;
		case "020201":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongshui.png";
			title="供水";
			break;
		case "020202":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongre.png";
			title="供热";
			break;
		case "020203":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_ranqi.png";
			title="燃气";
			break;
		case "02010601":
			imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_fireHydrant.png";
			title="消防栓";
			break;
		case "020304":
			imageurl = uiDomain+"/images/map/gisv0/map_config/unselected/situation/situation_jws.png";
			title="派出所";
			break;
	}
	
	$("#map"+currentN).ffcsMap.locationPoint({w : 320,h : 170},'resourceLayer', id, title, imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/resourceDetail.jhtml?resId='+id+'&t='+Math.random();
    	context = '<iframe id="resource_info" name="resource_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
    	return context;
    });
}

// 新经济组织
function localtionNonPublicOrgPoint (id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/new_economy.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 195},'nonPublicOrgLayer', id, '新经济组织', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofnewgroup/nonPublicOrgDetail.jhtml?orgId='+id+'&t='+Math.random();
    	context = '<iframe id="nonPublicOrg_info" name="nonPublicOrg_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showNonPublicOrgDetail);
}

// 新社会组织
function localtionNewSocialOrgOrgPoint (id){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/new_society.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 195},'newSocialOrgLayer', id, '新社会组织', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofnewgroup/newSocialOrgDetail.jhtml?orgId='+id+'&t='+Math.random();
    	context = '<iframe id="newSocialOrg_info" name="newSocialOrg_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showNewSocialOrgDetail);
}

function getPopDetailOnMapOfListClick(type,wid,gridId) {
	var imageurl="";
	if(type == "partyMerber"){//党员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_partyMember.png";
	}else if(type == "volunteer"){//志愿者
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_volunteer.png";
	}else if(type == "retire"){//退休人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_retire.png";
	}else if(type == "homeAge"){//居家养老
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_yanglao.png";
	}else if(type == "military"){//兵役
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_serveArmy.png";
	}else if(type == "unemployment"){//失业
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_unemployed.png";
	}else if(type == "welfare"){//低保
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_lowObject.png";
	}else if(type == "disability"){//残障人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_physicalDisabilities.png";
	}else if(type == "neuropathy") {//精神病人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_psychosis.png";
	}else if(type == "dangerous") {//危险品从业
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_dangerousGoods.png";
	}else if(type == "petition") {//上访人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_petitioner.png";
	}else if(type == "drugs") {//吸毒人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_drugAddict.png";
	}else if(type == "heresy") {//邪教人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_heresy.png";
	}else if(type == "rectify") {//矫正
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_rectify.png";
	}else if(type == "camps") {//刑释解教人员
		imageurl=uiDomain+"/images/map/gisv0/map_config/selected/people_RehabilitationXieJiao.png";
	}
	$("#map"+currentN).ffcsMap.locationPoint({w : 400,h : 190},'peopleLayer', wid, '人员名片', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofpoplocal/getPeopleInfoDetailOnMap.jhtml?ciRsId='+wid+'&t='+Math.random()+'&gridId='+gridId;
    	context = '<iframe id="people_info" name="people_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showPopDetail);
}

//显示企业详细信息并进行定位
function getCorDetailOnMapOfListClick(cbiId) {
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/region_company.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 245},'corLayer', cbiId, '企业名片', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getCorInfoDetailOnMap.jhtml?cbiId='+cbiId+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showCorBaseDetail);
}

//显示护路护线信息并进行定位
function getICareRoadDetailOnMap(lotId) {
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 170},'hlhxLayer', lotId, '护路护线', '',30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getICareRoadInfoDetailOnMap.jhtml?lotId='+lotId+'&t='+Math.random();
	    context = '<iframe id="iCareRoad_info" name="iCareRoad_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	    return context;
    });
}

//显示党组织详细信息并进行定位
function getPartyOrgDetailOnMapOfListClick(partyGroupId) {
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/things_partyOrganization.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 160},'partyGroupLayer', partyGroupId, '党组织信息', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofthing/getPartyOrgSummaryInfoOnMap.jhtml?partyGroupId='+partyGroupId+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showPartyOrgDetail);
}

function arcgisKuangXuan(){
	var abc = $(".SelectMap");
	if(abc.hasClass("DelSelected")) {
		abc.removeClass("DelSelected");
		$('#map').ffcsMap.geteditToolbar().deactivate();
		$("#map"+currentN).ffcsMap.clear({
			layerName : "kuangxuanLayer"
		});
	} else {
		abc.addClass("DelSelected");
		$("#map"+currentN).ffcsMap.clear({
			layerName : "kuangxuanLayer"
		});
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
		$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
		$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack);
	}
}

//楼宇经济框选
function arcgisKuangXuanLyjj(){
	var abc = $(".SelectMap");
	if(abc.hasClass("DelSelected")) {
		abc.removeClass("DelSelected");
		$('#map').ffcsMap.geteditToolbar().deactivate();
		$("#map"+currentN).ffcsMap.clear({
			layerName : "kuangxuanLayer"
		});
	} else {
		abc.addClass("DelSelected");
		$("#map"+currentN).ffcsMap.clear({
			layerName : "kuangxuanLayer"
		});
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
		$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
		$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanLyjjCallBack);
	}
}

//框选统计页面弹出框
function arcgisKuangXuanCallBack(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	var url = js_ctx +'/zhsq/map/arcgis/arcgisdata/getKuangXuanData.json?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+hs+'&t='+Math.random();
	showMaxJqueryWindow("框选统计",url,400,200,null,null,kuangxuanCallBackOnClose);
}

//楼宇经济框选统计页面弹出框
function arcgisKuangXuanLyjjCallBack(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+'/be/statistic/lyjjChoose.jhtml?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+hs+'&t='+Math.random();
	showMaxJqueryWindow("框选数据",url,900,380);
}

var selectedKuangXuans = "";
function setKuangXuanState(){
	$("#map"+currentN).ffcsMap.clear({
		layerName : "kuangxuanLayer"
	});
	$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack1);
}

function showKuangxuanHs(){
	var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
	var url="";
	if(topWin.geoString == ""){
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
	}else {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?geoString='+topWin.geoString+'&t='+Math.random();
	}
	$("#map"+currentN).ffcsMap.clear({
		layerName : "kuangxuanLayer"
	});
	$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,2,true);
}

function arcgisKuangXuanCallBack1(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	//获取iframe的window对象
    var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
    //通过获取到的window对象操作HTML元素，这和普通页面一样
    topWin.geoString = hs;
    topWin.mapt = currentArcgisConfigInfo.mapType;
    topWin.moveNeedKuangxuan();
    topWin.toStatKuangxuanData();
}

//从框选配置页面直接进行数据查询
function arcgisKuangXuanGetData(mapt,geoString,selectedKuangXuanStr){
	var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
	topWin.toStatKuangxuanData();
}

function closeKuangxuan(){
	$(".kuangxuan").click;
	$("#map"+currentN).ffcsMap.clear({
		layerName : "kuangxuanLayer"
	});
}

function kuangxuanCallBackOnClose1(){
	$("#map"+currentN).ffcsMap.clear({
		layerName : "kuangxuanLayer"
	});
}

function zhoubianCallBackOnClose1(){
	$("#map"+currentN).ffcsMap.clear({
		layerName : "zhoubianLayer"
	});
}

//框选统计弹出框关闭回调
function kuangxuanCallBackOnClose(){
	$("#map"+currentN).ffcsMap.clear({
		layerName : "kuangxuanLayer"
	});
	//2014-09-12 liushi 自动触发更改框选显示状态
	arcgisKuangXuan();
	/*2014-09-12 liushi 目前没有找到代码控制停止编辑状态的api暂时不进行关闭触发轮廓选择的操作
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
	$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
	$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack);
	*/
}

//查询网格员轨迹数据
function getTrajectoryOfGridAdmin(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",'网格员轨迹','gridAdminTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}

//查询护路护线队员轨迹数据
function getTrajectoryOfCareRoadMember(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",'护路护线队员轨迹','careRoadmemberTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}

//查询网格员实施轨迹
function getDynamicTrajectoryOfGridAdmin(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'网格员实时轨迹','gridAdminTrajectoryLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
}

//出租车实时轨迹
function getDynamicTrajectoryOfTaxi(url,locateTimeBegin,locateTimeEnd,icon) {
	$("#map"+currentN).ffcsMap.clear({layerName : "taixLayer"});
	var imageurl = icon?(uiDomain+icon):'/zhsq_event/images/icon_taxi.png';
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'出租车实时轨迹','taixLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
}

//查询护路护线队员实施轨迹
function getDynamicTrajectoryOfCareRoadMember(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'护路护线队员实时轨迹','careRoadmemberTrajectoryLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
}

//周边资源
function surroundingRes(eventId){
	var url = document.getElementById("SQ_ZZGRID_URL").value+"/zzgl/map/data/res/showSurroundingResList.jhtml?eventId="+eventId+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	showMaxJqueryWindow("周边资源",url,700,380,null,null);
}

//用于网格
function clearGridLayer() {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridsLayer"});
}

//加载楼宇经济事件定位数据
function localtionLyjjEventPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	
	$("#map").ffcsMap.locationPoint({w:370,h:235},'lyjjEventLayer', id, '事件名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjEventInfoDetailOnMap.jhtml?eventId='+id+'&t='+Math.random();
		context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		return context;
	},showLyjjEventDetail);
}

//楼宇经济事件定位
function getArcgisDataOfLyjjEvent(url){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/lyjj_event.png";
	//清除地图上原来的标记
	$("#map").ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	$("#map").ffcsMap.clear({layerName : "lyjjCorLayer"});
	$("#map").ffcsMap.clear({layerName : "lyjjEventLayer"});
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map").ffcsMap.render('lyjjEventLayer',url,0,true,imageurl, 30, 39);
	$("#map").ffcsMap.ffcsDisplayhot({w:370,h:235},"lyjjEventLayer",'事件名片',getInfoDetailOnMap , showLyjjEventDetail);
}

//显示楼宇经济事件详情
function showLyjjEventDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	
	$.ajax({
	      url: js_ctx+'/zhsq/map/arcgis/arcgisdataoflyjj/getLyjjEventInfo.json',
	      type:'POST',
	      dataType:"json",
	      async: true,
	      data:{
	         eventId:id
	      },
	      success:function(xxxJson){
			 var result = xxxJson.result;
	      	 if(result=="success"){
	      		 
	      		var data=xxxJson.record;
	      		var formId=data.FORM_ID;
      			var formType=data.FORM_TYPE;
  				var instanceId=data.WF_INSTANCE_ID;
				var wfDbId=data.WF_DBID_;
				var operateType=data.WF_OPERATE_TYPE;
				var activityName=data.WF_ACTIVITY_NAME_;
	      		activityName = encodeURIComponent(encodeURIComponent(activityName));
	      		
	      		var appUser=data.WF_APP_USER;
	      		var url= sq_lyjj_url+"/be/event/toEventDetail.jhtml?eventId="+id+"&formId="+formId+"&formType="+formType+"&instanceId="+instanceId+"&operFlag=look_db&taskId="+wfDbId+"&operateType="+operateType+"&activityName="+activityName+"&fromMap=2&appUser="+appUser;
	      		showMaxJqueryWindow(title,url,1040,420);
	      	 }else{
	      		 alert("请联系系统管理员!!!");
	      	 }
	      }
	});
}

function toSearchZhouBian(x,y){
	if(x == undefined || y == undefined ){
		alert("没有参照点坐标，无法进行周边资源查询！");
	}else {
		var data = '?x='+x+'&y='+y+'&mapt='+currentArcgisConfigInfo.mapType + '&t=' + Math.random();
		var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianConfig.jhtml'+ data;
		$('#zhoubianConfig').attr('src', url);
		showZhoubian();
		$("#map"+currentN).ffcsMap.clearGridsLayer();
		$("#map"+currentN).ffcsMap.closeMutilPiontPanelDiv()
	}
}

var scopeCircleLayerNum=0;
function toShowZhouBianSketch(x,y,distance){
	//toHideZhouBianSketch();
	$("#map"+currentN).ffcsMap.addScopeCircle({x : x, y : y ,radius : distance ,imgUrl:js_ctx +"/js/map/arcgis/library/style/images/GreenShinyPin.png"},function(d){
 		scopeCircleLayerNum = d.scopeCircleCount;
 	});
}

function toHideZhouBianSketch(){
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "scopeCircleLayer"+scopeCircleLayerNum});
}

//新消防栓、自来水公司、天然水源
function localtionNewResourcePoint(type, id) {
	var imageurl = "";
	var title = "";
	var url = js_ctx + '/zhsq/map/arcgis/arcgisdataofgoods/newResourceDetail.jhtml?resId=' + id + '&t=' + Math.random();
	if ("0601" == type || "0602" == type) {
		// 新消防栓
		imageurl = js_ctx + "/images/map/arcgis/unselected/things_fireHydrant.png";
		title = "消防栓";
	} else if ("0603" == type) {
		// 天然水源
		imageurl = js_ctx + "/images/map/arcgis/unselected/things_naturalWaterSource.png";
		title = "天然水源";
	} else if ("0604" == type) {
		// 自来水厂
		imageurl = js_ctx + "/images/map/arcgis/unselected/things_gongshui.png";
		title = "自来水公司";
	} else if ("0601" == type) {
		imageurl = js_ctx + "/images/map/arcgis/unselected/things_fireteam.png";
		title = "消防队";
		url = js_ctx + '/zhsq/map/arcgis/arcgisdataofgoods/fireTeamDetail.jhtml?resId=' + id + '&t=' + Math.random();
	}
	$("#map"+currentN).ffcsMap.locationPoint({w : 370,h : 240},'newResourceLayer', id, title, imageurl,30,39, function(data){
    	context = '<iframe id="new_resource_info" name="new_resource_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
    	return context;
    },showNewSourceDetail);
}

// ADD BY @YangCQ 
function drawArcgisDataForCommon(settings) {
	var _config = {
		title : "",
		imgUrl : "/images/map/arcgis/unselected/things_busStation.png",
		url : "",
		layerName : "",
		width : 370,
		height : 240,
		flgUrl : "",
		dlgUrl : "",
		dlgW : 700,
		dlgH : 430
	};
	
	$.extend(_config, settings);
	$("#map" + currentN).ffcsMap.clear({ layerName : _config.layerName });
	_config.url = _config.url + "&mapt=" + currentArcgisConfigInfo.mapType + '&t=' + Math.random();
	$("#map" + currentN).ffcsMap.render(_config.layerName, _config.url, 0, true, js_ctx + _config.imgUrl, 30, 39);
	$("#map" + currentN).ffcsMap.ffcsDisplayhot({
		w : _config.width,
		h : _config.height
	}, _config.layerName, _config.title, function(data) {
		var url =  js_ctx + _config.flgUrl + data['wid'] + '&t=' + Math.random();
	    context = '<iframe id="'+_config.layerName+'_info" name="'+_config.layerName+'_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	    return context;
	}, function(id, title) {
		var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
		var url = sq_zzgrid_url + _config.dlgUrl + id;
		showMaxJqueryWindow("详情", url, _config.dlgW, _config.dlgH);
	});
}

// 消防队
function localtionPointForCommon(settings) {
	var _config = {
		id : "",
		title : "",
		imgUrl : "/images/map/arcgis/unselected/things_gongshui.png",
		url : "",
		layerName : "",
		width : 370,
		height : 240,
		func1 : null
	};
	
	$.extend(_config, settings);
	$("#map" + currentN).ffcsMap.locationPoint({ w : _config.width, h : _config.height },
		_config.layerName,
		_config.id,
		_config.title,
		js_ctx + _config.imgUrl,
		30,
		39,
		function(data) {
			context = '<iframe id="fire_team_info" name="new_resource_info" width="100%" height="100%" src="'
					+ _config.url
					+ '" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
			return context;
		}, _config.func1);
}

//根据ids查询消防栓、天然水源、自来水公司定位数据进行定位
function getArcgisDataOfNewResource(type,url){
	var imageurl;
	var title;
	if("0601"==type || "0602"==type){
	//新消防栓
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_fireHydrant.png";
		title="消防栓";
	}else if("0603"==type){
	//天然水源
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_naturalWaterSource.png";
		title="天然水源";
	}else if("0604"==type){
	//自来水厂
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_gongshui.png";
		title="自来水公司";
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "newResourceLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('newResourceLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:240},"newResourceLayer",title,getInfoDetailOnMap,showNewSourceDetail);
}

//校园周边定位
function localtionCampusPoint(type,id){
	var imageurl;
	var title;
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_campus.png";
	title="校园周边";
	$("#map"+currentN).ffcsMap.locationPoint({w : 350,h : 280},'campusLayer', id, title, imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/campusDetail.jhtml?plaId='+id+'&t='+Math.random();
		context = '<iframe id="campus_info" name="campus_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
		return context;
    },showCampusDetail);
}

//根据ids查询校园周边定位数据进行定位
function getArcgisDataOfCampus(type,url){
	var imageurl;
	var title;
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_campus.png";
	title="校园周边";
	$("#map"+currentN).ffcsMap.clear({layerName : "campusLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('campusLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:350,h:280},"campusLayer",title,getInfoDetailOnMap,showCampusDetail);
}

//根据ids查询安监队伍定位数据进行定位
function getArcgisDataOfControlsafetyRanks(type,url,bizType){
	var imageurl;
	var title;
	if(0 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/team.png";
		title="机构队伍";
	}else if(1 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/team.png";
		title="群防群治队伍";
	}else if(3 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_controlsafetyRanks.png";
		title="安监队伍";
	}
	
	$("#map"+currentN).ffcsMap.clear({layerName : "controlsafetyRanksLayer"});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('controlsafetyRanksLayer',url,0,true,imageurl, 30, 39);
	if(bizType==0){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:300,h:150},"controlsafetyRanksLayer",title,getInfoDetailOnMap,showControlsafetyRanksDetail0);//(地图概要信息弹出详细页面)
	}else if(bizType==1){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:300,h:150},"controlsafetyRanksLayer",title,getInfoDetailOnMap,showControlsafetyRanksDetail1);//(地图概要信息弹出详细页面)
	}else if(bizType==3){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:300,h:150},"controlsafetyRanksLayer",title,getInfoDetailOnMap,showControlsafetyRanksDetail3);//(地图概要信息弹出详细页面)
	}
}

//安监队伍、机构队伍、群防群治队伍定位
function localtionControlsafetyRanksPoint(type,id,bizType){
	var imageurl;
	var title;
	if(0 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/team.png";
		title="机构队伍";
			
		$("#map"+currentN).ffcsMap.locationPoint({w : 300,h : 150},'controlsafetyRanksLayer', id, title, imageurl,30,39, function(data){
			var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?teamId='+id+'&t='+Math.random()+'&bizType='+bizType+'';
			context = '<iframe id="controlsafetyRanks_info" name="controlsafetyRanks_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
			return context;
	    },showControlsafetyRanksDetail0);//(地图概要信息弹出详细页面)
	}else if(1 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/team.png";
		title="群防群治队伍";
		
		$("#map"+currentN).ffcsMap.locationPoint({w : 300,h : 150},'controlsafetyRanksLayer', id, title, imageurl,30,39, function(data){
			var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?bizType='+bizType+'&teamId='+id+'&t='+Math.random();
			context = '<iframe id="controlsafetyRanks_info" name="controlsafetyRanks_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
			return context;
	    },showControlsafetyRanksDetail1);//(地图概要信息弹出详细页面)
	}else if(3 == bizType){
		imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/things_controlsafetyRanks.png";
		title="安监队伍";
		
		$("#map"+currentN).ffcsMap.locationPoint({w : 300,h : 150},'controlsafetyRanksLayer', id, title, imageurl,30,39, function(data){
			var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?bizType='+bizType+'&teamId='+id+'&t='+Math.random();
			context = '<iframe id="controlsafetyRanks_info" name="controlsafetyRanks_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="auto" frameborder=0></iframe>';
			return context;
	    },showControlsafetyRanksDetail3);//(地图概要信息弹出详细页面)
	}
}


//显示安监企业详细信息并进行定位
function getAnjianCorDetailOnMapOfListClick(cbiId) {
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/region_company.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 340,h : 245},'corLayer', cbiId, '安监企业名片', imageurl,30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getCorInfoDetailOnMap.jhtml?isAnjian=true&cbiId='+cbiId+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showAnjianCorBaseDetail);
}

//显示安监企业详情
function showAnjianCorBaseDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url +'/zzgl/grid/safetyStation/mapDetail.jhtml?corpId=' + id;;
	showMaxJqueryWindow(title,url,670,380);
}

//根据ids查询安监企业定位数据进行定位
function getArcgisDataOfAnjianCor(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "corLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/region_company.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('corLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 340,h : 245},"corLayer",'安监企业名片',getInfoDetailOnMap,showAnjianCorBaseDetail);
}
