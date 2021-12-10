<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>网格轮廓绑定地图首页</title>

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
	addGridFeatureLayer();
	$("#map"+currentN).ffcsMap.getMap().on("click", function(evt) {
		if(evt.graphic != null){
			var rings = evt.graphic.geometry.rings;
			var hs = "";
			//获取轮廓的数据
			var sumX=0,sumY=0,sumLength=0;
			if(rings.length > 0){
				for(var j = 0;j < rings.length; j++){	
					var ring = rings[j];
					sumLength += ring.length;
					for(var i = 0;i < ring.length; i++){
						if(hs != null && hs != ''){
							hs = hs + ",";
						}
						sumX +=  Number(ring[i][0]);
						sumY +=  Number(ring[i][1]);
						hs = hs + ring[i][0] + ',' + ring[i][1];
					}
				}
			}
			//计算中心点坐标
			var x = sumX/sumLength;
			var y = sumY/sumLength;
			var wid = parent.document.getElementById("wid").value;
			var name = parent.document.getElementById("name").value;
			var data = {
				"x" : x,
				"y" : y,
				"hs" : hs,
				"wid" : wid,
				"name" : name,
			}
			if(wid != null && wid != '' && typeof(wid) != 'undefined'){
				gridBinding(data);
			}
		}
    });
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
	var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+parent.document.getElementById("wid").value;
	$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS);
}

function getArcgisDataOfGridsByLevel(level) {
	if(window.parent.document.getElementById("displayLevel"+level).checked == true) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType='+level+'&gridId='+parent.document.getElementById("wid").value;
	 	$("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true);
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
	//window.parent.loadArcgisData();
    //getMapArcgisDatas();
    if(gridId != "" && gridId != null && typeof(gridId) != undefined){
		//判断是否有网格轮廓
		window.parent.hasArcgisData();
	}
}

//==========================================================
//加载网格轮廓
//0表示点    1、线   2、轮廓
//var ykFeatureUrl = "http://gistest.fjsq.org/ArcGIS/rest/services/FeaturesTest/FeatureServer/2";

var featureObj ;
function addGridFeatureLayer(gridLevel){
	var bindGridLayer = $("#map"+currentN).ffcsMap.getMap()._layers['bindGridLayer'];
	if(bindGridLayer != undefined) {
		bindGridLayer.clear();
		delete $("#map"+currentN).ffcsMap.getMap()._layers['bindGridLayer'];
	}
	
	//现网
	var ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HC_WG/FeatureServer/";
	
	if(gridLevel != null && gridLevel != '' && typeof(gridLevel) != 'undefined'){
		gridLevel = gridLevel - 2;
		ykFeatureUrl = ykFeatureUrl + '' + gridLevel;
	}else{
		ykFeatureUrl = ykFeatureUrl + "2";
	}
	var feaLayer = new esri.layers.FeatureLayer(ykFeatureUrl,{
		mode: esri.layers.FeatureLayer.MODE_SELECTION,
        outFields: ["*"],
        id : 'bindGridLayer'
    });
    var queryTask = new esri.tasks.QueryTask(ykFeatureUrl);
	var query = new esri.tasks.Query();
	query.outFields = ["*"];
	var queryCondition = "1=1 ";
	query.where= queryCondition;
	query.returnGeometry = true;//返回轮廓数据这个 是地图的图形  你设置成false 只会给你返回属性数据
	feaLayer.selectFeatures(query);
	queryTask.execute(query, showResults, errorResults);
	function showResults(results) {
		var sqsymbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
                 new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,
                 new dojo.Color([198, 120, 249, 1]), 1),
                 new dojo.Color([125, 125, 125, 0])); //color的最后一个参数是透明度，0：完全透明，1：完全不透明
	    if (results.features != null) {
			for(var i=0;i<results.features.length;i++){
				var g=results.features[i];
				g.setSymbol(sqsymbol);
				$("#map"+currentN).ffcsMap.getMap().graphics.add(g);
			}
		}
	}
    function errorResults(results) {
    	var rs = results;
    }
    
    feaLayer.on("load",function(){
		//中心点名称处理
		feaLayer.queryFeatures(query,function(featureSet){
			var i=1;
		},function(){
			console.log("feature render failed!");
		});
	});
    $("#map"+currentN).ffcsMap.getMap().addLayer(feaLayer);
	
	
}
/************
function addGridFeatureLayer(){
	gridLyerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "2=2 ";
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	fea.render(queryCondition, 'bindGridLayer', ykFeatureUrl, 2, true,null,null,null,null,false,gridBinding);
	fea.show('bindGridLayer',$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	
	var layer = fea.esriLayer;
	
	layer.on("click", function(evt) {
		var rings = evt.graphic.geometry.rings;
		var hs = "";
		//获取轮廓的数据
		var sumX=0,sumY=0,sumLength=0;
		if(rings.length > 0){
			for(var j = 0;j < rings.length; j++){	
				var ring = rings[j];
				sumLength += ring.length;
				for(var i = 0;i < ring.length; i++){
					if(hs != null && hs != ''){
						hs = hs + ",";
					}
					sumX +=  Number(ring[i][0]);
					sumY +=  Number(ring[i][1]);
					hs = hs + ring[i][0] + ',' + ring[i][1];
				}
			}
		}
		//计算中心点坐标
		var x = sumX/sumLength;
		var y = sumY/sumLength;
		var wid = parent.document.getElementById("wid").value;
		var name = parent.document.getElementById("name").value;
		var data = {
			"x" : x,
			"y" : y,
			"hs" : hs,
			"wid" : wid,
			"name" : name,
		}
		if(wid != null && wid != '' && typeof(wid) != 'undefined'){
			gridBinding(data);
		}
    });
	
	
	layer.on("mouse-over",function(evt){
		var graphic = evt.graphic;
		if (graphic.attributes != null) {
			console.log(graphic.attributes);
			var _data = graphic.attributes.DATA;
			var type = graphic.attributes.TYPE;
			var symbol = graphic.symbol;
			var currentLayerName = graphic.attributes.layerName;
			// 十六进制颜色转为RGB
			var defLineColor = "#FFFF00";// 边框颜色 [138,43,226];  // 边框颜色
			var defAreaColor = "#ADFF2F";// 填充颜色 [173,255,47];  // 填充颜色
			var defNameColor = "#000000";// 文字颜色
			var defLineWidth = 5 ; // 边框线条宽度
			var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
			var defRGBAreaColor = new dojo.Color(defAreaColor).toRgb();
			var defRGBNameColor = new dojo.Color(defNameColor).toRgb();
			var RGBLineColor = defRGBLineColor;
			var RGBAreaColor = defRGBAreaColor;
			var RGBNameColor = defRGBNameColor;
			
			if(_data.lineColor != "" ){
				RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
			}
			
			if(_data.areaColor != "" ){
				RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
			}
			
			if (_data.nameColor != "") {
				RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
			}
			// 边框线条宽度
			var lineWidth = defLineWidth;
			
			if (_data.lineWidth != "" && _data.lineWidth != null) {
				lineWidth = _data.lineWidth;
			}
			var fillStyle = "solid"; // 填充样式
			var lineStyle = "solid"; // 边框样式
			var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
					          new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
					          new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
			symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1])); // 边界
			graphic.setSymbol(symbol);
			graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0.5])); // 填充
			
			// 文字
			var textGraphic = _data.gridName;
			var textSymbol = textGraphic.symbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1]));
			var ptitle = _data.gridName;
			$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
		}
	});
	
	layer.on("mouse-out",function(evt){
		var graphic = evt.graphic;
		if (graphic.attributes != null) {
			console.log(graphic.attributes);
			var _data = graphic.attributes.DATA;
			var type = graphic.attributes.TYPE;
			var symbol = graphic.symbol;
			var currentLayerName = graphic.attributes.layerName;
			// 十六进制颜色转为RGB
			var defLineColor = "#FFFF00";// 边框颜色 [138,43,226];  // 边框颜色
			var defAreaColor = "#ADFF2F";// 填充颜色 [173,255,47];  // 填充颜色
			var defNameColor = "#000000";// 文字颜色
			var defLineWidth = 5 ; // 边框线条宽度
			var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
			var defRGBAreaColor = new dojo.Color(defAreaColor).toRgb();
			var defRGBNameColor = new dojo.Color(defNameColor).toRgb();
			var RGBLineColor = defRGBLineColor;
			var RGBAreaColor = defRGBAreaColor;
			var RGBNameColor = defRGBNameColor;
			
			if(_data.lineColor != "" ){
				RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
			}
			
			if(_data.areaColor != "" ){
				RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
			}
			
			if (_data.nameColor != "") {
				RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
			}
			// 边框线条宽度
			var lineWidth = defLineWidth;
			
			if (_data.lineWidth != "" && _data.lineWidth != null) {
				lineWidth = _data.lineWidth;
			}
			var fillStyle = "solid"; // 填充样式
			var lineStyle = "solid"; // 边框样式
			var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
					          new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
					          new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
			symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1])); // 边界
			graphic.setSymbol(symbol);
			graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0.5])); // 填充
			
			// 文字
			var textGraphic = _data.gridName;
			var textSymbol = textGraphic.symbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1]));
			var ptitle = _data.gridName;
			$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
		}
	});
	
}*****************************/
//==========================================================

//绑定操作
function gridBinding(data) {
	var wid = data['wid'];
	var name = data['name'];
	var hs = data['hs'];
	var x = data['x'];
	var y = data['y'];
	var nameColor = parent.document.getElementById("nameColor").value;
	var areaColor = parent.document.getElementById("areaColor").value;
	var lineColor = parent.document.getElementById("lineColor").value;
	var lineWidth = Number(parent.document.getElementById("lineWidth").value);
	var mapCenterLevel = Number(parent.document.getElementById("mapCenterLevel").value);
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/saveArcgisDrawAreaPanel.jhtml?";
	var data = "targetType=grid&wid="+wid+"&mapt="+currentArcgisConfigInfo.mapType;
	data +="&x="+x+"&y="+y+"&hs="+hs+"&nameColor="+nameColor+"&areaColor="+areaColor+"&lineColor="+lineColor+"&lineWidth="+lineWidth+"&mapCenterLevel="+mapCenterLevel;
	if(confirm("确定要进行此绑定操作吗？")){
		$.ajax({
			type: "POST",
			url: url,
			data: data,
			dataType:"json",
			error: function(data){
				parent.DivShow("绑定报错了!");
			},
			success: function(data){
				if(data.flag == true) {
					parent.DivShow("绑定成功!");
					clearGridLayer();
					//中心点定位
					locateCenterAndLevel(wid,currentArcgisConfigInfo.mapType);
					//获取网格轮廓
					parent.loadArcgisData();
					getMapArcgisDatas();
				}else {
					parent.DivShow("绑定失败!");
				}
			}
		});
    } else {
       
    }
	
	return "";
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

var checkSaveResult = 0;

function clearGridLayer(){
	window.parent.document.getElementById("changeStyleDiv").style.display = 'none';
	window.parent.document.getElementById("gridDisplayDiv").style.display = 'none';
	window.parent.document.getElementById("displayLevel1").checked = false;
	window.parent.document.getElementById("displayLevel2").checked = false;
	window.parent.document.getElementById("displayLevel3").checked = false;
	var gridLayer = $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer'];
	if(gridLayer != undefined) {
		gridLayer.clear();
		delete $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer'];
	}
	var gridLayer1 = $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer1'];
	if(gridLayer1 != undefined) {
		gridLayer1.clear();
		delete $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer1'];
	}
	var gridLayer2 = $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer2'];
	if(gridLayer2 != undefined) {
		gridLayer2.clear();
		delete $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer2'];
	}
	var gridLayer3 = $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer3'];
	if(gridLayer3 != undefined) {
		gridLayer3.clear();
		delete $("#map"+currentN).ffcsMap.getMap()._layers['gridLayer3'];
	}
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
