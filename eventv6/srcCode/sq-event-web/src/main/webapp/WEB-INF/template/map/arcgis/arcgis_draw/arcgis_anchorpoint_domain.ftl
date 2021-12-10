<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title>
<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
</head>

<body>
<button onclick="endPointLocate()">确定</button> &nbsp;
地图类型：<input type="text" readonly="true" name="mapt" id="mapt" value="${mapt}" />
x：<input type="text" readonly="true" name="x" id="x" value="${x?c}" />
y：<input type="text" readonly="true" name="y" id="y" value="${y?c}" />
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
<div style="height:0px">
<iframe id="cross_domain_frame" name="cross_domain_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

</body>
<script>
var flagPoint = false;
jQuery(document).ready(function() {
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
	
	$("html").attr("class","");
	getArcgisInfo();
	/*
	getArcgisOfGridDatas(document.getElementById("gridId").value,currentArcgisConfigInfo.mapType);
	setTimeout(function(){
		getMapArcgisPointInfo();
	},3000); 
	*/
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
		    for(var i=0; i<arcgisConfigInfos.length; i++){
		    	if(i==0){
		    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}else (
		    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	)
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
	getMapArcgisPointInfo();
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
		    arcgisMapConfigInfo.Init(mapConfigInfo);
		    var mapt = $('#mapt').val();
		    if(mapt != null && mapt !='0') {//表示地图默认显示为2维
		    	if(mapt == '5') {
		    		currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
		    	}
		    }else {
		    	 if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
		    		currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
		   		 }else if(arcgisMapConfigInfo.mapStartType == "30") {//表示地图默认显示为2维
			    	currentArcgisMapInfo.Init3D(arcgisMapConfigInfo);
			    }
		    }
		    loadArcgisMap("map","jsSlider","vec");
		    
		 }
	 });
}
function setMapArcgisPoint() {

	var x = $('#x').val();
	var y = $('#y').val();
	if(x == 0 && y == 0) {
		x = currentArcgisConfigInfo.mapCenterX;
		y = currentArcgisConfigInfo.mapCenterY;
	}
	$('#x').val(x);
	$('#y').val(y);
	$('#mapt').val(currentArcgisConfigInfo.mapType);
	$("#map"+currentN).ffcsMap.onClickGetPoint(x,y,'${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', pointGetCallBack);
	
	$("#map"+currentN).ffcsMap.centerAt({
		x : parseFloat(x),          //中心点X坐标
		y : parseFloat(y),           //中心点y坐标
		wkid : 4326, //wkid 2437
		zoom : 14
	});
		    	
}

function pointGetCallBack(data) {
	var xys = data.toString().split(",");
	var x = xys[0];
	var y = xys[1];
	$('#x').val(x);
	$('#y').val(y);
	$('#mapt').val(arcgisMapConfigInfo.mapStartType);
}

function endPointLocate() {
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	
	var data = 'x='+x+'&y='+y+'&mapt='+mapt;
	var url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?'+ data;
	$('#cross_domain_frame').attr('src', url);
	parent.closeMaxJqueryWindow(); // 关闭弹出框
}
function getArcgisOfGridDatas(gridId,mapt) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridForPoint.json?mapt='+mapt+'&wid='+gridId;
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true);
}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
