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
			<input type="hidden" id="mapt" value="" disabled="true" />
			<input type="hidden" id="x" value="" readonly="true"/>
			<input type="hidden" id="y" value="" readonly="true"/>
	 		<input type="hidden" id="hs" value="" readonly="true"/>
	 		<input type="hidden" id="wid" value="${wid}" /> 
	    	<input type="hidden" id="name" value="${name}" /> 
	    	<input type="hidden" id="targetType" value="build" />
	    	<input type="hidden" id="parentGridId" value="${parentGridId}" /> 
	    	<input type="hidden" id="gridLevel" value="${gridLevel}" />
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          <!--
	          	<td>
	          		<a href="#" onclick="relate()" class="NorToolBtn SmallSaveBtn">直接关联</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="editBoundary()" class="NorToolBtn EditBtn">轮廓重新编辑</a>
	    		</td>
	    		<td>
	          	 	<a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">编辑中心点</a>
	          	</td>
	          	-->
	    	 	<td>
	    	 		<div id="tipMessage" style="color:yellow;font-size:16px"></div>
	 				<div class="blind" style="color:yellow;font-size:16px;float:right;"></div>
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

<script type="text/javascript">

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
function getArcgisDataByCurrentSet(){
	getArcgisDatasOfBuildOfBinding()
}

//获取当前编辑的网格的信息
function loadArcgisData() {
	var url = "";
	var param = "?wid="+document.getElementById("wid").value+"&mapt="+currentArcgisConfigInfo.mapType+"&parentGridId="+${parentGridId};
	url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";
	url = url+param;
	$.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert('网格地图信息获取出现异常，请联系维护人员处理！'); 
		 },
		 success: function(data){
		 	if(data.list.length>0) {
		 		dataInit(data.list[0]);
		 	}else if(data.list.length==0) {
		 		$("#mapt").val("");
				$("#x").val("");
				$("#y").val("");
				$("#hs").val("");
				showCenterPoint($("#x").val(),$("#y").val());
		 	}
		 }
	 });
}
function dataInit(data) {
	$("#mapt").val(data.mapt);
	$("#x").val(data.x);
	$("#y").val(data.y);
	$("#hs").val(data.hs);
	showCenterPoint(data.x,data.y);
}
function getArcgisDatasOfBuildOfBinding() {
	//locateCenterAndLevel(document.getElementById("wid").value,currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/getArcgisDrawDataOfBuildsOfBinding.json';
	$("#map"+currentN).ffcsMap.render('buildLayerBinding',url,1,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:255},"buildLayerBinding",'楼栋名片',buildBinding);
}
function buildBinding(data) {
	var ids = data['ids'];
	var names = data['names'];
	var id = data['id'];
	var hs = data['hs'];
	var x = data['x'];
	var y = data['y'];
	var nameColor = data['nameColor'];
	var areaColor = data['areaColor'];
	var lineColor = data['lineColor'];
	var lineWidth = data['lineWidth'];
	var areaColor = data['areaColor'];
	var wid = $('#wid').val();
	var name = $('#name').val();
	var data = 'wid='+wid+'&name='+name+'&ids='+ids+'&names='+names+'&id='+id+'&x='+x+'&y='+y+'&hs='+hs+'&mapt='+currentArcgisConfigInfo.mapType
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/updateArcgisDrawDataOfBuildOfBinding.json?'+data;
	if(confirm("确定要进行此绑定操作吗？")){
         $.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	alert("绑定报错了!");
			 },
			 success: function(data){
			 	if(data.flag == true) {
			 		alert("绑定成功!");
			 	}else {
			 		alert("绑定失败!");
			 	}
			 }
		 });
    } else {
       
    }
	
	return "";
}

function showCenterPoint(x,y){
	$("#map").ffcsMap.showCenterPoint(x, y, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png');
}

function locateCenterAndLevel(id,mapt) {
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&wid='+id+'&targetType=build',
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

//判断是否有楼宇轮廓信息
	function hasArcgisData() {
		var url = "";
		var param = "?wid="+document.getElementById("wid").value+"&mapt="+window.currentArcgisConfigInfo.mapType+"&parentGridId="+${parentGridId};
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";
		url = url+param;
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	alert('网格地图信息获取出现异常，请联系维护人员处理！'); 
			 },
			 success: function(data){
	    		$("#tipMessage").html("");
		 		var tipMess = "";
		 		var wid = document.getElementById("wid").value;
			 	if(data.list.length==0 && wid != "" && wid != null && typeof(wid) != undefined) {
					tipMess = "未标注中心点。未画楼宇轮廓！";
					window.document.getElementById("tipMessage").innerHTML = tipMess;
			 		$("#mapt").val("");
					$("#x").val("");
					$("#y").val("");
					$("#hs").val("");
			 	}else{
			 		if(data.list[0].x == "" || data.list[0].y == ""){
						tipMess = "未标注中心点。";
						window.document.getElementById("tipMessage").innerHTML = tipMess;
			 		}
			 		var hs = data.list[0].hs.split(",");
			 		if(hs.length<=2){
						tipMess = "未画楼宇轮廓！";
						$("#tipMessage").append(tipMess);
			 		}
			 	}
			 }
		 });
	}

function getInfoDetailOnMap(data) {
	var layer = data['layerName'];
	var url = "";
	var context = "";
	if(layer == 'gridLayer' || layer == 'gridsLayer') {//网格图层信息显示
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == 'buildLayer' || layer=='buildLayerPoint') {//楼宇图层信息显示
		if(directRelateFlag == true) {
			var addressId = data['id'];
			var addressName = data['name'];
			directRelate(addressId,addressName)
		}
	}
}
var directRelateFlag = false;
function relate() {
	if(directRelateFlag == false) {
		directRelateFlag = true;
	}else if(directRelateFlag == true) {
		directRelateFlag = false;
	}
}

function drawCenterPoint() {
	var x = $("#x").val();
	var y = $("#y").val();
	window.frames["iframeOfMapLoad"].drawPoint(pointCallBack,x,y);
}


function drawBoundary() {
	window.frames["iframeOfMapLoad"].draw('POLYGON',boundaryCallBack);
}
function boundaryCallBack(data) {
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	var xys = hs.split(",");
	var x = xys[0];
	var y = xys[1];
	var type = data1.type;
	var mapt = window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType;
	$("#mapt").val(mapt);
	if($("#x").val() == "" || $("#y").val() == "") {
		$("#x").val(x);
		$("#y").val(y);
	}
	if(xys.length >=4) {
		$("#hs").val(hs);
	}
}
function pointCallBack(data) {
	var xys = data.toString().split(",");
	var x = xys[0];
	var y = xys[1];
	var hs = x+","+y;
	$('#x').val(x);
	$('#y').val(y);
	var mapt = currentArcgisConfigInfo.mapType;
	$("#mapt").val(mapt);
	if($("#hs").val() == "") {
		$("#hs").val(hs);
	}
}

function directRelate(addressId,addressName) {
	var url = '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/add.jhtml?addressId='+addressId+'&relateType=direct'; 
	showMaxJqueryWindow(addressName + "关联楼宇", url,600,450);
}
function editionRelate(addressId,addressName) {
	var x = $("#x").val();
	var y = $("#y").val();
	var hs = $("#hs").val();
	var mapt = $("#mapt").val();
	var mapCenterLevel="";
	if(!x||!y){
		alert("中心点未设置"); return;
	}
	if(!hs){
		alert("未绘制轮廓数据！"); return;
	}
	var url = '${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/add.jhtml?x='+x+'&y='+y+'&hs='+hs+'&mapt='+mapt+'&relateType=edition'; 
	showMaxJqueryWindow(addressName + "关联楼宇", url,600,450);
}
function clearBuildingLayer(){
	$("#map").ffcsMap.toolbarDeactivate();
	$("#map").ffcsMap.onClickGetPointEnd();
	$("#map").ffcsMap.changeIsDrawEdit();
	$("#map"+currentN).ffcsMap.clear({layerName : 'buildLayer'});
}
</script>

</body>
</html>