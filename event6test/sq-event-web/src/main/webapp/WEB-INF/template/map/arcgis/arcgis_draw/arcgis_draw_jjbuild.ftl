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
	}
	$("#map").ffcsMap.onClickGetPoint(x,y,'${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', callBack,true);
}
var checkSaveResult = 0;
function saveFeatureToServer(layerName,attributeCfg,oldData) {
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}/proxy.jsp");
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	var currentMap = $("#map"+currentN).ffcsMap.getMap();
	var wid = attributeCfg.GRIDID;
	var condition = "TYPE="+attributeCfg.TYPE+" AND GRIDID="+wid;
	fea.queryByFeaServer(ykFeatureUrl,condition,function(featureSet){
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
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}/proxy.jsp");
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	fea.delFeaByCondition(ykFeatureUrl,condition);
}
</script>
</html>
