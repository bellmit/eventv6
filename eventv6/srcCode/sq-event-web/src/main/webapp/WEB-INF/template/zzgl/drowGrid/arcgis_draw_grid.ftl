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
	//locateCenterAndLevel(parent.document.getElementById("wid").value,currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+parent.document.getElementById("wid").value;
	$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS,null,null,true);
}

function getArcgisDataOfGridsByLevel(level) {
	if(window.parent.document.getElementById("displayLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType='+level+'&gridId='+parent.document.getElementById("wid").value;
	 	$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true,null,null,null,null,null,null,true);
	}else {
		$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'+level});
	}
}

function getArcgisDataByCurrentSet(){
	var gridId = window.parent.document.getElementById("wid").value;
	if(gridId != "" && gridId != null && typeof(gridId) != undefined){
		//中心点定位
		locateCenterAndLevel(gridId,window.parent.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType);
	}
	window.parent.loadArcgisData();
    getMapArcgisDatas();
    if(gridId != "" && gridId != null && typeof(gridId) != undefined){
		//判断是否有网格轮廓
		window.parent.hasArcgisData();
	}
}

//var showNum = 1;
function locateCenterAndLevel(id,mapt) {
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&wid='+id+'&targetType=grid',
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
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

function clearGridLayer(){
	$("#map").ffcsMap.toolbarDeactivate();
	$("#map").ffcsMap.onClickGetPointEnd();
	$("#map").ffcsMap.changeIsDrawEdit();
	
	window.parent.document.getElementById("changeStyleDiv").style.display = 'none';
	window.parent.document.getElementById("gridDisplayDiv").style.display = 'none';
	window.parent.document.getElementById("displayLevel1").checked = false;
	window.parent.document.getElementById("displayLevel2").checked = false;
	window.parent.document.getElementById("displayLevel3").checked = false;
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer1'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer2'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer3'});
	$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'});
}

function hideCenterPointImg(x, y){
	$("#map"+currentN).ffcsMap.hideCenterPointImg(x, y);
}

function showCenterPoint(x,y){
	$("#map").ffcsMap.showCenterPoint(x, y, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png');
}

function changeMapLevel(){
	var mapCenterLevel = parent.document.getElementById("mapCenterLevel").value;
	$("#map"+currentN).ffcsMap.centerAt({
		x : currentArcgisConfigInfo.mapCenterX,          //中心点X坐标
		y : currentArcgisConfigInfo.mapCenterY,           //中心点y坐标
		wkid : currentArcgisConfigInfo.wkid, //wkid 2437
		zoom : mapCenterLevel
	});
}
</script>
</html>
