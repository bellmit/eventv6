
/**
 * 2014-05-26 liushi add arcgis地图配置信息
 *
 **/
var arcgisConfigInfos;
var currentArcgisConfigInfo;
//记录当前显示地图图层的方法以及所含的参数
var currentLayerLocateFunctionStr="";
var currentListNumStr="";//列表页码已经加载的页码记录

var currentLayerListFunctionStr="";
var ykFeatureUrl = "http://gistest.fjsq.org/ArcGIS/rest/services/FeaturesTest/FeatureServer/0";



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
		mapobj.removeAllLayers();
		currentMapDiv.innerHTML = "";
	}
	if(ARCGIS_DOCK_MODE == "1") {
		$.fn.ffcsMap.setCors();
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
	if(ARCGIS_DOCK_MODE == "1") {
	     $.fn.ffcsMap.InstanceFeature();
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
	if(window.document.getElementById("map0")!= undefined) {
		getArcgisDataByCurrentSet();
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
		currentListNumStr = currentListNumStr.substring(currentListNumStr.length-1,currentListNumStr.length);
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

function clearMyLayer() {
	for(var i=0;i<=pointLayerNum;i++) {
		featureHide("gasStation"+i);
		featureHide("chemicalEnterprise"+i);
		featureHide("oilDepot"+i);
		featureHide("dangerSource"+i);
		featureHide("safetyTeam"+i);
	}
	/*
	clearLyerFunc();
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});  //楼宇经济楼宇
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});  //楼宇经济企业
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
	$("#map"+currentN).ffcsMap.clear({layerName : "test001"});  //楼宇经济事件
	$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});  //事件
	$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});  //事件
	currentLayerLocateFunctionStr="";
	currentListNumStr = "";
	*/
}
function clearLyerFunc() {
	$.ajax({   
		 url: js_ctx+'/zhsq/map/arcgis/arcgis/getClearLyerNames.json?homePageType='+$('#homePageType').val(),   
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 async: false,
		 error: function(data){  
		   alert('获取图层信息出错!'); 
		 },   
		 success: function(data){
		 	if(data.layerNameStr != null && data.layerNameStr != "") {
		 		var layerNames = data.layerNameStr.split(",");
		 		if(layerNames.length >0) {
		 			for(var i=0; i<layerNames.length; i++) {
		 				$("#map"+currentN).ffcsMap.clear({layerName : layerNames[i]});//护路护线
		 			}
		 		}
		 	}
		 }
	 });
}
function switchClearMyLayer() {//进行地图切换时不需要将定位调用记录清空
	/*
	for(var i=0;i<=pointLayerNum;i++) {
		featureHide("gasStation"+i);
		featureHide("chemicalEnterprise"+i);
		featureHide("oilDepot"+i);
		featureHide("dangerSource"+i);
		featureHide("safetyTeam"+i);
	}
	
	if(AUTOMATIC_CLEAR_MAP_LAYER == '1') { 
		clearLyerFunc();
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});  //楼宇经济楼宇
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});  //楼宇经济企业
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
		$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});  //事件
	}*/
	
}
var gridLyerNum = 0;
//查询网格轮廓数据并显示
function getArcgisDataOfGrids(gridId,gridCode,mapt,gridLevel) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridLayer"});
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "dbhLayer"});
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "pkhLayer"});
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "zcbzhLayer"});
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "fpkhLayer"});
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
	var imgurl = js_ctx +'/js/map/arcgis/library/style/images/wglocal.png';
	if(IS_GRID_ARCGIS_SHOW_CENTER_POINT != undefined && IS_GRID_ARCGIS_SHOW_CENTER_POINT == '1') {
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,14,true,getInfoDetailOnMap);
	}else{
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},"gridLayer",'网格',getInfoDetailOnMap,showGridDetail);
	/*
	if(document.getElementById("buildName0").checked == true){
		getArcgisDataOfBuildsPoints(gridId,gridCode,mapt)
	}*/
}

function getPrecisionPovertyLayer(url,elementsCollectionStr){
	document.getElementById("gridLevelName2").checked = false;
	document.getElementById("gridLevelName3").checked = false;
	document.getElementById("gridLevelName4").checked = false;
	document.getElementById("gridLevelName5").checked = false;
	document.getElementById("gridLevelName6").checked = false;
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridLayer"});
	currentLayerLocateFunctionStr="getPrecisionPovertyLayer('"+url+"','"+elementsCollectionStr+"')";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType+'&elementsCollectionStr='+elementsCollectionStr;
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true,null,null,null,12,null,null,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},menuLayerName,'贫困户',getPrecisionPovertyOnMap); 
	//currentLayerLocateFunctionStr='getArcgisDataOfZhuanTi("'+url+'","'+elementsCollectionStr+'",'+width+','+height+')';
}
function getPrecisionPovertyOnMap(data) {
	var elementsCollectionStr = data['elementsCollectionStr'];
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	var callBack = analysisOfElementsCollection(elementsCollectionStr,"callBack");
	var htmlstr = $("#titlePath").html();
	var htmls = htmlstr.split(" &gt; ");
	if(elementsCollectionStr!=undefined && htmlstr.indexOf(menuName)<0){
		htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
		$("#titlePath").html(htmlstr + " > "+menuName);
	}
	//clearMyLayer();
	//$("#titlePath").html(htmlstr + " > "+menuName);
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	getObjectListUrl(elementsCollectionStr,data['wid']);
	currentLayerListFunctionStr = callBack+"(\""+elementsCollectionStr+"\")";
}

function featureHide(layerName){
	var feature = $.fn.ffcsMap.getFfcsFeature();
	var currentMap = $("#map"+currentN).ffcsMap.getMap();
	feature.hide(layerName,currentMap);
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
	if("1" != IS_ACCUMULATION_LAYER) {
		switchClearMyLayer();
	}
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('hlhxLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:170},"hlhxLayer", '护路护线' ,getInfoDetailOnMap);
}

//便民服务网点
function getArcgisDataOfServiceOutletsListByIds(url) {
	if("1" != IS_ACCUMULATION_LAYER) {
		switchClearMyLayer();
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_globalEyes.png";
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('serviceOutletsLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:322,h:170},"serviceOutletsLayer", '便民服务网点' ,getInfoDetailOnMap);
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

//查询楼栋中心点数据并进行中心点定位(2015-01-21 liushi add 三维地图改为楼宇轮廓显示)
function getArcgisDataOfBuildsPoints(gridId,gridCode,mapt) {
	var lc_sanwei_function=null;
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



function clearGridAdminLayer() {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"});
}


var peopleType = "";

function getArcgisDataOfZhuanTi(url,elementsCollectionStr,width,height){
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	var smallIco = uiDomain +analysisOfElementsCollection(elementsCollectionStr,"smallIco");
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	
	currentLayerLocateFunctionStr='getArcgisDataOfZhuanTi("'+url+'","'+elementsCollectionStr+'",'+width+','+height+')';
	
	if("1" != IS_ACCUMULATION_LAYER) {
		switchClearMyLayer();
	}
	//$("#map"+currentN).ffcsMap.clear({layerName : menuLayerName});
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random()+"&elementsCollectionStr="+elementsCollectionStr;
	if("careRoadMemberLayer" == menuLayerName) {
		$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true);
	}else {
		$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39);
	}
	
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	var menuDetailUrl =analysisOfElementsCollection(elementsCollectionStr,"menuDetailUrl");
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,showDetail,lc_sanwei_function);
	}else {
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,null,lc_sanwei_function);
	}
	
}

//地图列表信息显示回调函数
function getInfoDetailOnMapNew(data) {
	var elementsCollectionStr = data['elementsCollectionStr'];
	var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl += ""+data['wid']+"&t="+Math.random()+'&gridId='+$("#gridId").val();
	//如果个别业务有比较特殊的参数可以进行条件判断增加
	var context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	return context;
}

function getDetailOnMapOfListClick(elementsCollectionStr,width,height,wid,gridId) {
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	var smallIcoSelected = uiDomain+analysisOfElementsCollection(elementsCollectionStr,"smallIcoSelected");
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuSummaryUrl = analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl +=""+wid+"&t="+Math.random();
	if(gridId != '' && gridId != undefined){
		menuSummaryUrl +="&gridId="+gridId;
	}
	
	var menuDetailUrl =analysisOfElementsCollection(elementsCollectionStr,"menuDetailUrl");
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="people_info" name="people_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},showDetail,lc_sanwei_function,elementsCollectionStr);
	}else {
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="people_info" name="people_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},null,lc_sanwei_function);
	}
}
function showDetail(id, title,data){
	var elementsCollectionStr = data['elementsCollectionStr'];
	var menuDetailUrl =analysisOfElementsCollection(elementsCollectionStr,"menuDetailUrl");
	var width =analysisOfElementsCollection(elementsCollectionStr,"menuDetailWidth");
	var height =analysisOfElementsCollection(elementsCollectionStr,"menuDetailHeight");
	if(width == "null"){
		width = null;height=null;
	}else {
		width = parseInt(width);
		height = parseInt(height);
	}
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		if(menuDetailUrl.indexOf('http://')<0){
			menuDetailUrl = js_ctx + menuDetailUrl;
		}
		menuDetailUrl += ""+id+"&t="+Math.random()+'&gridId='+$("#gridId").val();
		showMaxJqueryWindow(title,menuDetailUrl,width,height);
	}
}

function showGridDetail(id, title) {
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    var url = sq_zzgrid_url+"/zzgl/map/data/gridBase/standardGridDetailIndex.jhtml?gridId="+id;
	showMaxJqueryWindow(title,url,850,400);
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
	}else if(layer == 'buildLayer' || layer=='buildLayerPoint') {//楼宇图层信息显示
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="build_info" name="build_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}
	return context;
}


