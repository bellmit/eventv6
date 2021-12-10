<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title>

	<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	
</head>

<body>

	<div>
		<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
		</div>
		<div class="page-container" id="map1" style="position: absolute; width:100%; height:100%; z-index: 2;display:none;">
		</div>
		<div class="page-container" id="map2" style="position: absolute; width:100%; height:100%; z-index: 3;display:none;">
		</div>
	</div>
	<div id="jsSlider"></div>
<div class="MapTools">
	<ul>
		<li class="ThreeWei" onclick="threeWeiClick()"></li>
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
</body>
<script>
var flagPoint = false;
var defaultx;
var defaulty;
jQuery(document).ready(function() {
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
	$("html").attr("class","");
	getArcgisInfo();
	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 }
});
function threeWeiClick(){
 	var mapStyleDiv = document.getElementById("mapStyleDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
 }
var currentMapStyleObj;
//获取arcgis地图路径的配置信息
function getArcgisInfo(){
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
		 },
		 success: function(data){
		    arcgisConfigInfos=eval(data.arcgisConfigInfos);
		    var htmlStr = "";
		    var curIndex = 0;
	    	for(var i=0; i<arcgisConfigInfos.length; i++){
		    	if(i==0){
		    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}else {
		    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}
	    	}
		    
		    var mapStyleDiv = document.getElementById("mapStyleDiv");
		    mapStyleDiv.innerHTML = htmlStr;
		    $("#mapStyleDiv").width(60*arcgisConfigInfos.length+8)
		    
		    if(htmlStr!=""){
		    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[curIndex]
		    }
		    
		    if(arcgisConfigInfos.length > 0) {
		    	currentArcgisConfigInfo = arcgisConfigInfos[curIndex];
		    	loadArcgis(arcgisConfigInfos[curIndex],"map","jsSlider","switchMap");
		    }
		 }
	 });
}

//2014-05-26 liushi add获取加载地图的信息并且初始化地图信息对象
function getMapArcgisInfo(){
	$.ajax({
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapArcgisInfo.json',
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
		 },
		 success: function(data){
		    var mapConfigInfo=eval(data.mapConfigInfo);
		    arcgisMapConfigInfo.Init(mapConfigInfo);// = new ArcgisMapConfigInfo(mapConfigInfo);
		    if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
		    	currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
		    }
		    loadArcgisMap("map","jsSlider");
		    window.parent.loadArcgisData();
		 }
	 });
}

function getMapArcgisDatas() {
	locateCenterAndLevel(parent.document.getElementById("wid").value,currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
	$("#map"+currentN).ffcsMap.render('buildLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS_BUILD,null,null,true);
}

function getMapArcgisData(){
	if(window.parent.document.getElementById("displayLevel2").checked == true) {
		var url1 =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
		$("#map").ffcsMap.render('buildLayer2',url1,1,true);
		var imageurl="${uiDomain!''}/images/map/gisv0/map_config/unselected/build/build_locate_point.png";
		$("#map"+currentN).ffcsMap.render('gridLayer2',url1,0,true,imageurl, 11, 14);
		
		
		//var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfBuild.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
		//$("#map"+currentN).ffcsMap.render('gridLayer2',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS_BUILD);
	}else{
		$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer2'});
		$("#map"+currentN).ffcsMap.clear({layerName : 'buildLayer2'});
	}
}
function getArcgisDataOfGrids(level) {
	if(window.parent.document.getElementById("displayLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0&gridId='+parent.document.getElementById("gridId").value;
	 	//alert(url);
		$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true);
	}else{
		$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'+level});
	}
}
function getArcgisDataOfGridsByLevel(level) {
	if(window.parent.document.getElementById("displayLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType='+level+'&gridId='+parent.document.getElementById("gridId").value;
	 	//alert(url);
		$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true);
	}else{
		$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'+level});
	}
}
function getArcgisDataOfResourcesByLevel(level) {
	if(window.parent.document.getElementById("displayResourcesLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisDataOfResourcesListByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType='+level+'&wid='+parent.document.getElementById("wid").value+"&targetType="+parent.document.getElementById("targetType").value;
	 	$("#map"+currentN).ffcsMap.render('displayResourcesLevel'+level,url,2,true);
	}else {
		$("#map"+currentN).ffcsMap.clear({layerName : 'displayResourcesLevel'+level});
	}
}
function getArcgisDataByCurrentSet(){
	var buildingId = window.parent.document.getElementById("wid").value;
	if(buildingId != "" && buildingId != null && typeof(buildingId) != undefined){
		//中心点定位
		locateCenterAndLevel(buildingId,window.parent.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType);
	}
	
	window.parent.loadArcgisData();
    getMapArcgisDatas();
    if(buildingId != "" && buildingId != null && typeof(buildingId) != undefined){
		//判断是否有楼宇轮廓
		window.parent.hasArcgisData();
	}
    
}

function locateGridCenterById(gridId) {
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json?mapt='+currentArcgisConfigInfo.mapType+'&wid='+gridId+'&t='+Math.random();
	$.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 5000, 
		 dataType:"json",
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		    var list = data.list;
		    if(list!=null && list.length > 0) {
		    	var obj = list[0];
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.mapCenterLevel
		    	});
		    }
		 }
	 });
}

//var showNum = 1;
function locateCenterAndLevel(id,mapt) {
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&wid='+id+'&targetType=build',
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	//alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		 	var obj = data.result;
		 	var showLevel = 0;
		 	if(obj != null) {	
		 		/*if(showNum == 1){
		 			showLevel = obj.mapCenterLevel;
		 		}else{
		 			showLevel = $(currentMapDiv).ffcsMap.getCurrentMapCenterObj().level;
		 		}
		 		if(showNum == 1){
		 			showNum = showNum +1;
		 		}*/
		 		if(obj.mapCenterLevel == undefined || obj.mapCenterLevel == null) {
		 			obj.mapCenterLevel = currentArcgisConfigInfo.arcgisScalenInfos.length-2;
		 		}
			 	$("#map"+currentN).ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.mapCenterLevel
		    	});
		    	defaultx = obj.x;
		    	defaulty = obj.y;
		 	}else {
		 		defaultx = currentArcgisConfigInfo.mapCenterX;
		 		defaulty = currentArcgisConfigInfo.mapCenterY;
		 	}
		 }
	 });
}

//drawType：POINT（点）、POLYLINE（线）、POLYGON（面）
function drawPanel(callBack,x,y,hs){
	if(hs==null || hs == "" || hs.split(",").length<4) {
		$("#map").ffcsMap.draw("POLYGON",callBack);
	}else {
		$("#map").ffcsMap.toolbarDeactivate();
		$("#map").ffcsMap.hideCenterPointImg(x, y);
		$("#map").ffcsMap.edit(true,callBack,false);
	}
	$("#map").ffcsMap.onClickGetPointEnd();
}
function drawPoint(callBack,x,y){
	$("#map").ffcsMap.toolbarDeactivate();
	$("#map").ffcsMap.changeIsDrawEdit();
	if(x == null || x == "" || x == 0) {
		x = defaultx;
		y = defaulty;
	}
	
	$("#map").ffcsMap.onClickGetPoint(x,y,'${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', callBack,true);
}
var checkSaveResult = 0;
function saveFeatureToServer(layerName,attributeCfg,oldData) {
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}"+parent.document.getElementById("FACTOR_URL").value);
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	var currentMap = $("#map"+currentN).ffcsMap.getMap();
	var wid = attributeCfg.ID;
	var condition = "ID="+wid;
	
	fea.queryByFeaServer(parent.document.getElementById("FACTOR_SERVICE").value,condition,function(featureSet){
		checkSaveResult = featureSet.features.length;
		if(checkSaveResult == 0) {
			fea.createFeatureToServer(layerName,currentMap,attributeCfg,oldData);
		}else {
			var temp_attrAllCfig = $.extend(true,featureSet.features[0].attributes, attributeCfg);
			
			fea.editFeatureToServer(temp_attrAllCfig,oldData);
		}
	}, null);
}

function deleteFeatureToServer(condition) {
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}"+parent.document.getElementById("FACTOR_URL"));
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	fea.delFeaByCondition(parent.document.getElementById("FACTOR_SERVICE"),condition);
}

function clearBuildingLayer(){
	$("#map").ffcsMap.toolbarDeactivate();
	$("#map").ffcsMap.onClickGetPointEnd();
	$("#map").ffcsMap.changeIsDrawEdit();
	window.parent.document.getElementById("changeStyleDiv").style.display = 'none';
	window.parent.document.getElementById("gridDisplayDiv").style.display = 'none';
	window.parent.document.getElementById("displayLevel0").checked = false;
	window.parent.document.getElementById("displayLevel2").checked = false;
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer0'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer1'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer2'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer3'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'buildLayer'});
}


function hideCenterPointImg(x, y){
	$("#map"+currentN).ffcsMap.hideCenterPointImg(x, y);
}

function showCenterPoint(x,y){
	$("#map").ffcsMap.showCenterPoint(x, y, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png');
}
</script>
</html>
