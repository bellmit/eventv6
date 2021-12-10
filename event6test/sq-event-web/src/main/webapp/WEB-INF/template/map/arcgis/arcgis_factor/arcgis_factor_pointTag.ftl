<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>填报管理页面</title>
	<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		/*.AlphaBack1 tr td{padding-top:3px;padding-right:2px;}*/
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
		td{margin: 0;padding: 5px;list-style: none;}
	</style>
</head>
<body >
<#include "/component/maxJqueryEasyUIWin.ftl" />
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<input type="hidden" id="gridId" value="${gridId?c}" disabled="true" />
			<input type="hidden" id="markerType" value="${markerType}" disabled="true" />
			<input type="hidden" id="arcgisFactorUrl" value="${arcgisFactorUrl}" disabled="true" />
			<input type="hidden" id="parameterJsonStr" value="${parameterJsonStr}" disabled="true" />
			<input type="hidden" id="id" value="${id?c}" disabled="true" />
			<input type="hidden" id="mapt" value="${mapt}" disabled="true" />
			<input type="hidden" id="x" value="${x}" readonly="true"/>
			<input type="hidden" id="y" value="${y}" readonly="true"/>
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          	<td>
	          		<a href="#" onclick="endPointLocate()" class="NorToolBtn SmallSaveBtn">确定</a>
	    		</td>
	    		<td>
	          	 	<a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">标注</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="deleteData()" class="NorToolBtn DelBtn">删除</a>
	          	</td>
	        </table>
	    </div>
	</div>

<div id="myMap" style="position:absolute; top:0px;bottom:0px;right:0px; left:0px; border: #ccc 1xp solid;">
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
</div>
<div style="height:0px">
<iframe id="cross_domain_frame" name="cross_domain_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>
<script type="text/javascript">
var callBackUrl = "${callBackUrl}";
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
		    	loadArcgis(arcgisConfigInfos[curIndex]);
		    }
		 }
	 });
}
//0表示点    1、线   2、轮廓
var ykFeatureUrl = "http://gistest.fjsq.org/ArcGIS/rest/services/FeaturesTest/FeatureServer/0";
var pointLayerNum = 0;
function getArcgisDataByCurrentSet(){
	
	if("1101" == $("#markerType").val()) {//安监队伍
		ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/4";
	}else if("1001" == $("#markerType").val()){//加油站
		ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/3";
	}else if("1002" == $("#markerType").val()){//危化企业
		ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/0";
	}else if("1003" == $("#markerType").val()){//油库
		ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/2";
	}else if("1004" == $("#markerType").val()){//重大危险源
		ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/1";
	}
	featureHide("pointLayer"+pointLayerNum);
	pointLayerNum++;
	var queryCondition = "ID="+$("#id").val();
	$.fn.ffcsMap.InstanceFeature();
	
	if("1101" == $("#markerType").val()) {
		queryCondition = "TEAM_ID="+$("#id").val();
	}else {
		queryCondition = "STATION_ID="+$("#id").val();
	}
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"pointLayer"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,null);
	fea.show("pointLayer"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},"pointLayer"+pointLayerNum,'位置',null,null); 
	locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
}

function getInfoDetailOnMap(data) {
	
}

function locateCenterAndLevel(id,mapt) {
	if($("#x").val() == "") {
		defaultx = currentArcgisConfigInfo.mapCenterX;
		defaulty = currentArcgisConfigInfo.mapCenterY;
	}else {
		defaultx = $("#x").val()*1;
		defaulty = $("#y").val()*1;
	}
	$("#map"+currentN).ffcsMap.centerAt({
		x : defaultx,          //中心点X坐标
		y : defaulty,           //中心点y坐标
		wkid : currentArcgisConfigInfo.wkid, //wkid 2437
		zoom : currentArcgisConfigInfo.zoom
	});
}

function drawCenterPoint() {
	var x = $("#x").val();
	var y = $("#y").val();
	$("#map").ffcsMap.draw("POINT",pointCallBack);
}
function pointCallBack(data) {
	var xys = eval("["+data+"]")[0].coordinates;
	var x = xys[0];
	var y = xys[1];
	$('#x').val(x);
	$('#y').val(y);
	var mapt = currentArcgisConfigInfo.mapType;
	$("#mapt").val(mapt);
}
function endPointLocate() {
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	saveFeature();
	var data = 'x='+x+'&y='+y+'&mapt='+mapt;
	var url;
	url = callBackUrl+'?'+ data;
	$('#cross_domain_frame').attr('src', url);
}
function saveFeature() {
	var x = $("#x").val()*1;
	var y = $("#y").val()*1;
	if(ARCGIS_DOCK_MODE == "1") {
		var layerName="pointLayer";
		var attributeCfg;
		var type = 1;
		var oldData;
		var parameterJsonStr = $("#parameterJsonStr").val();
		var parameterJson = eval("("+parameterJsonStr+")");
		attributeCfg = {
			"ID":$("#id").val(),
			"MAP_TYPE":$("#mapt").val(),
			"MARKER_TYPE":$("#markerType").val(),
			"X":$("#x").val(),
			"Y":$("#y").val()
		};
		var param = $.extend(true, attributeCfg,parameterJson);
		oldData = {
	        "id": param.ID,
	        "wid": param.ID,
	        "name": param.NAME,
	        "x": param.X,
	        "y": param.Y,
	        "hs": param.X+","+param.Y,
	        "mapt": param.MAP_TYPE
	    };
		saveFeatureToServer("pointLayer",param,oldData)
	}
}

//-- 供子页调用的重新载入数据方法
function reloadDataForSubPage(result) {
	closeMaxJqueryWindow();
	$.messager.alert('提示', result, 'info');
}


var checkSaveResult = 0;
function saveFeatureToServer(layerName,attributeCfg,oldData) {
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}${arcgisFactorUrl}");
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	var currentMap = $("#map"+currentN).ffcsMap.getMap();
	var wid = attributeCfg.ID;
	var condition;
	if("1101" == $("#markerType").val()) {
		condition = "TEAM_ID="+wid;
	}else {
		condition = "STATION_ID="+wid;
	}
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
	var queryCondition;
	$.fn.ffcsMap.InstanceFeature();
	if("1101" == $("#markerType").val()) {
		queryCondition = "TEAM_ID="+$("#id").val();
	}else {
		queryCondition = "STATION_ID="+$("#id").val();
	}
	$.fn.ffcsMap.setProxy("${rc.getContextPath()}${arcgisFactorUrl}");
	var fea =  $.fn.ffcsMap.getFfcsFeature();
	fea.delFeaByCondition(ykFeatureUrl,queryCondition);
}
</script>

</body>
</html>