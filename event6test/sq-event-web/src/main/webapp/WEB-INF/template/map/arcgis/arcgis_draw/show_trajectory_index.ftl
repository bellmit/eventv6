<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>人员轨迹地图首页</title>
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		.AlphaBack1 tr td{padding-top:3px;padding-right:2px;}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
	</style>
<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
</head>

<body>
<input type="hidden" name="mapt" id="mapt" value="${mapt}" />
<input type="hidden" readonly="true" name="x" id="x" value="${x?c}" />
<input type="hidden" readonly="true" name="y" id="y" value="${y?c}" />
<input type="hidden" readonly="true" name="mapCenterLevel" id="mapCenterLevel" value="${mapCenterLevel?c}" />

<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />

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
<div style="height:0px">
<iframe id="cross_domain_frame" name="cross_domain_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

</body>
<script>
var mapType = '<#if mapType??>${mapType}</#if>';
var userId = "<#if userId??>${userId}</#if>";
var userName = "<#if userName??>${userName}</#if>";
var imsi = "<#if imsi??>${imsi}</#if>";
var locateTimeBegin = "<#if locateTimeBegin??>${locateTimeBegin}</#if>";
var locateTimeEnd = "<#if locateTimeEnd??>${locateTimeEnd}</#if>";
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
		 	var aci = eval(data.arcgisConfigInfos);
		 	arcgisConfigInfos = new Array();
		 	var mapTypeTemporary=0;
		 	var mapTypeN=0;
		 	for(var i=0; i<aci.length; i++){
		 		if(mapType == "") {
		 			if(i==0){
		    			arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeTemporary = aci[i].mapType;
		    			mapTypeN++;
		    		}else if(mapTypeTemporary == aci[i].mapType) {
		    			arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		    		}
		 		}else if(mapType == "2") {
		 			if(aci[i].mapType == 5) {
		 				arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		 			}
		 		}else if(mapType == "3") {
		 			if(aci[i].mapType == 30) {
		 				arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		 			}
		 		}else {
		 			arcgisConfigInfos=aci;
		 		}
		 	}
		    var htmlStr = "";
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
		    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
		    }
		    
		    if(arcgisConfigInfos.length > 0) {
		    	loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap");
		    }
		 }
	 });
}

function switchArcgisByNumber(obj,n) {
	if(currentMapStyleObj != obj){
		currentMapStyleObj.className = "";
		obj.className = "current";
		currentMapStyleObj = obj;
		switchArcgis(arcgisConfigInfos[n],n);
	}
}

function getArcgisDataByCurrentSet(){
	getArcgisOfGridDatas(document.getElementById("gridId").value,currentArcgisConfigInfo.mapType);
	setTimeout(function() {
		setMapArcgisPoint();
        getTrajectoryListData(userId,userName,imsi,locateTimeBegin,locateTimeEnd,"轨迹查询");
	},1000);
}

function setMapArcgisPoint() {

	var x = $('#x').val();
	var y = $('#y').val();
    var mapCenterLevel = $('#mapCenterLevel').val();
	if(x == 0 && y == 0) {
		x = currentArcgisConfigInfo.mapCenterX;
		y = currentArcgisConfigInfo.mapCenterY;
	}
	if(mapCenterLevel == 0){
        mapCenterLevel =currentArcgisConfigInfo.zoom;
	}
	$('#x').val(x);
	$('#y').val(y);
	$('#mapt').val(currentArcgisConfigInfo.mapType);

	
	$("#map").ffcsMap.centerAt({
		x : parseFloat(x),          //中心点X坐标
		y : parseFloat(y),           //中心点y坐标
		wkid : 4326, //wkid 2437
		zoom : mapCenterLevel
	});
		    	
}


function getArcgisOfGridDatas(gridId,mapt) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridForPoint.json?mapt='+mapt+'&wid='+gridId;
		$("#map").ffcsMap.render('gridLayer',url,2,true);
}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
