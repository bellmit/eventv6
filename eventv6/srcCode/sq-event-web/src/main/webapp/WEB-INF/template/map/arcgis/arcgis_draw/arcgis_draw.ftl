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
		<!--
    	<li class="ClearMap"></li>
    	<li class="SelectMap"></li>
    	<li class="MapFull"></li>
    	-->
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
<!--
	<div id="switchMap">
		<span onclick="switchMap('img')" id="imgMap" title="显示卫星地图">
			<span id="imgInner">
				<span id="inner"></span>
				<span id="text">卫星</span>
			</span>
		</span>
		<span onclick="switchMap('vec')" id="vecMap" title="显示普通地图">
			<span id="vecInner">
				<span id="inner"></span>
				<span id="text">地图</span>
			</span>
		</span>
	</div> 
 -->
</body>
<script>
var flagPoint = false;
var defaultx;
var defaulty;
$(function(){

	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
	$("html").attr("class","");
    setTimeout(function(){
        getArcgisInfo();
    },100);

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
		    //如果是楼宇经济，不管什么顺序，都是默认显示3d
		    if(parent.document.getElementById("targetType").value == 'lyjjBuilding'){
		    	for(var i=0; i<arcgisConfigInfos.length; i++){
			    	if(arcgisConfigInfos[i].mapType==30){
			    		curIndex = i;
			    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	}else{
			    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	}
		   		}
		    }else{
		    	for(var i=0; i<arcgisConfigInfos.length; i++){
			    	if(i==0){
			    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	}else {
			    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
			    	}
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
	if(parent.document.getElementById("targetType").value == 'grid') {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+parent.document.getElementById("wid").value;
	 	$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS,null,null,true);
	 	//$("#map").ffcsMap.render('gridLayer',url,2,true);
	}else if(parent.document.getElementById("targetType").value == 'build') {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
		$("#map"+currentN).ffcsMap.render('buildLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS_BUILD,null,null,true);
		
		//var url1 =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
		//$("#map").ffcsMap.render('buildLayer',url1,1,true);
        //var imageurl="${uiDomain!''}/images/map/gisv0/map_config/unselected/build/build_locate_point.png";
        //$("#map"+currentN).ffcsMap.render('buildLayerPoint',url1,0,true,imageurl, 11, 14);
	}else if(parent.document.getElementById("targetType").value == 'segmentgrid') {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisSegmentGridDataList.jhtml?mapt='+currentArcgisConfigInfo.mapType+'&ids='+parent.document.getElementById("wid").value;
		$("#map"+currentN).ffcsMap.render('segmentLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}else if(parent.document.getElementById("targetType").value == 'lyjjBuilding') {
		//画出所属网格的轮廓
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+parent.document.getElementById("parentGridId").value;
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		//画出楼宇的轮廓
		var url1 =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfLyjjbuildings.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
		$("#map"+currentN).ffcsMap.render('gridLayer',url1,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}else if(parent.document.getElementById("targetType").value == 'hlhx') {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisDataOfResourcesListByLevel.jhtml?mapt='+currentArcgisConfigInfo.mapType+'&wid='+parent.document.getElementById("wid").value+"&targetType=hlhx&showType=0";
		$("#map"+currentN).ffcsMap.render('segmentLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}else if(parent.document.getElementById("targetType").value == 'point') {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfPoints.json?mapt='+currentArcgisConfigInfo.mapType+'&pointId='+parent.document.getElementById("wid").value;
		$("#map"+currentN).ffcsMap.render('pointLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS_BUILD,null,null,true);
	}else{
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisDataOfResourcesListByLevel.jhtml?mapt='+currentArcgisConfigInfo.mapType+'&wid='+parent.document.getElementById("wid").value+"&targetType="+parent.document.getElementById("targetType").value+"&showType=0";
		$("#map"+currentN).ffcsMap.render('segmentLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
	}
}

function getArcgisDataOfGridsByLevel(level) {
	if(window.parent.document.getElementById("displayLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType='+level+'&gridId='+parent.document.getElementById("wid").value;
	 	//$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true,null,null,null,true,getInfoDetailOnMap);
	 	$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true);
	 	//$("#map").ffcsMap.render('gridLayer'+level,url,2,true);
	}else {
		$("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'+level});
	}
}
function getArcgisDataOfZhoubianBuilds() {
    if(window.parent.document.getElementById("displayZhoubianBuilds").checked == true) {
        var url1 =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+parent.document.getElementById("wid").value;
        $("#map").ffcsMap.render('buildLayer',url1,1,true);
        var imageurl="${uiDomain!''}/images/map/gisv0/map_config/unselected/build/build_locate_point.png";
        $("#map"+currentN).ffcsMap.render('buildLayerPoint',url1,0,true,imageurl, 11, 14);
    }else {
        $("#map"+currentN).ffcsMap.clear({layerName : 'buildLayerPoint'});
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
	window.parent.loadArcgisData();
    getMapArcgisDatas();
	if(parent.document.getElementById("targetType").value == 'grid') {
		if(window.parent.document.getElementById("displayLevel1").checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=1&gridId='+parent.document.getElementById("wid").value;
		 	$("#map"+currentN).ffcsMap.render('gridLayer1',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		}
		if(window.parent.document.getElementById("displayLevel2").checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=2&gridId='+parent.document.getElementById("wid").value;
		 	$("#map"+currentN).ffcsMap.render('gridLayer2',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		}
		if(window.parent.document.getElementById("displayLevel3").checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=3&gridId='+parent.document.getElementById("wid").value;
		 	$("#map"+currentN).ffcsMap.render('gridLayer3',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		}
	}else if(parent.document.getElementById("targetType").value == 'hlhx'){
		if(window.parent.document.getElementById("displayResourcesLevel1").checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisDataOfResourcesListByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=1&wid='+parent.document.getElementById("wid").value+"&targetType="+parent.document.getElementById("targetType").value;
	 		$("#map"+currentN).ffcsMap.render('displayResourcesLevel'+level,url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		}
		if(window.parent.document.getElementById("displayResourcesLevel2").checked == true) {
			var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisDataOfResourcesListByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=2&wid='+parent.document.getElementById("wid").value+"&targetType="+parent.document.getElementById("targetType").value;
	 		$("#map"+currentN).ffcsMap.render('displayResourcesLevel'+level,url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
		}
	}else if(parent.document.getElementById("targetType").value == 'build'){
        getArcgisDataOfZhoubianBuilds();
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

function locateCenterAndLevel(id,mapt) {
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&wid='+id+'&targetType='+parent.document.getElementById("targetType").value,
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		 	var obj = data.result;
		 	if(obj != null) {
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
				parent.document.getElementById("x").value = obj.x;
                parent.document.getElementById("y").value = obj.y;
				if(id == obj.wid && defaultx != null && defaultx != 0 && defaulty != null && defaulty != 0){
                    $("#map"+currentN).ffcsMap.showCenterPoint(defaultx, defaulty, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png');
				}

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
		$("#map").ffcsMap.edit(true,callBack,false);
	}
	$("#map").ffcsMap.onClickGetPointEnd();
}
function drawPoint(callBack,x,y){
	if(x == null || x == "" || x == 0) {
		x = defaultx;
		y = defaulty;
		parent.pointCallBack([x,y]);
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
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}"+parent.document.getElementById("FACTOR_URL").value);
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	fea.delFeaByCondition(parent.document.getElementById("FACTOR_SERVICE").value,condition);
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
