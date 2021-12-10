<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache" />
    <title>地图网格数据绑定panel</title>
    <style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
		#changeStyleDiv{width:160px; position:absolute; z-index:10000; top:32px; left:10px; display:none;}
		#changeStyleDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#changeStyleDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		.styleChangeA:hover{text-decoration : none}
		
		#gridDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:145px; display:none;}
		#gridDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#gridDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		
		#resourcesDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:50px; display:none;}
		#resourcesDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#resourcesDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
	</style>
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    
	
    <link rel="stylesheet" href="${rc.getContextPath()}/js/cxcolor/css/jquery.cxcolor.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/cxcolor/js/jquery.cxcolor.js" ></script>
</head>
<body>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<input type="hidden" id="gridId" value="" /> 
			<input type="hidden" id="wid" value="" /> 
	    	<input type="hidden" id="name" value="" /> 
	    	<input type="hidden" id="targetType" value="grid" />
	    	<input type="hidden" id="FACTOR_STATION" value="<#if FACTOR_STATION??>${FACTOR_STATION}</#if>" />
	    	<input type="hidden" id="FACTOR_SERVICE" value="<#if FACTOR_SERVICE??>${FACTOR_SERVICE}</#if>" />
	    	<input type="hidden" id="FACTOR_URL" value="<#if FACTOR_URL??>${FACTOR_URL}</#if>" />
	    	<input type="hidden" id="parentGridId" value="${parentGridId}" /> 
	    	<input type="hidden" id="gridLevel" value="${gridLevel}" /> 
	    	<input type="hidden" id="lineWidth" value=""/>
	    	<input type="hidden" id="areaColor" value="#ff0000"  readonly="true" class="inp1" style="width:50px"/>
		
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          	<!--
	          	<td>
	          		<a href="#" onclick="save()" class="NorToolBtn SmallSaveBtn">保存</a>
	          	</td>
	          	<td>
	          		<a href="#" onclick="editBoundary()" class="NorToolBtn EditBtn">编辑轮廓</a>
	    		</td>
	    		<td>
	          	 	<a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">编辑中心点</a>
	          	</td>
	    		<td>
	          		<a href="#" onclick="deleteData()" class="NorToolBtn DelBtn">删除轮廓和中心点</a>
	          	</td>
	          	-->
	          	<td>
	          		<input type="hidden" id="mapt" value="" disabled="true" />
	    			<input type="hidden" id="x" value="" readonly="true"/>
	    			<input type="hidden" id="y" value="" readonly="true"/>
	    	 		<input type="hidden" id="hs" value="" readonly="true"/>
	    	 	</td>
	    	 	<td>
	    	 		<div class="MapLevel fl" style="width:95px">
		    	 		<ul>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="styleChangeClick();" /></li>
			                <li><a class="styleChangeA" onclick="styleChangeClick();">设置</a></li>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="styleChangeClick();" /></li>
		            	</ul>
	       			 </div>
	    	 	</td>
	    	 	<td>
	    	 		<div class="MapLevel fl" style="width:130px">
		    	 		<ul>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="gridDisplayChange();" /></li>
			                <li><a class="styleChangeA" onclick="gridDisplayChange();">辅助显示</a></li>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="gridDisplayChange();" /></li>
		            	</ul>
	       			 </div>
	    	 	</td>
	    	 	<td>
	    	 		<div id="tipMessage" style="color:yellow;font-size:16px"></div>
	 				<div class="blind" style="color:yellow;font-size:16px;float:right;"></div>
	    	 	</td>
	        </table>
	    </div>
	    
	    <div id="changeStyleDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li onclick="styleChangeClick();" style="text-align:right;">
	    			关闭
	    		</li>
      			<li>
      				默认显示层级:
      				<select id="mapCenterLevel" class="inp1" style="width:40px;height:18px;color:#000;padding:0;" onchange="setDefaultMapLevel();"></select>
				</li>
	    		<li>
	    			名称颜色：<input type="text" id="nameColor" value="" readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    		<li>
	    			轮廓颜色：<input type="text" id="lineColor" value=""  readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    	</ul>
	    </div>
	    <div id="gridDisplayDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li>
	    			<span><input name="displayLevel1" type="checkbox" value="1" onclick="getArcgisDataOfGridsByLevel(1);" id="displayLevel1" /></span>上级轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayLevel2" type="checkbox" value="2" onclick="getArcgisDataOfGridsByLevel(2);" id="displayLevel2" /></span>本级轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayLevel3" type="checkbox" value="3" onclick="getArcgisDataOfGridsByLevel(3);" id="displayLevel3" /></span>下级轮廓
	    		</li>
	    	</ul>
	    </div>
	</div>    
    <div id="myMap" style="position:absolute; top:0px;bottom:0px;right:0px; left:0px; border: #ccc 1xp solid;">
    	<iframe data-iframe="true" name="iframeOfMapLoad" id="iframeOfMapLoad" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfDrawGrid.jhtml?bindFlag=true" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
    </div>
</body>
<script>
// 直接调用
var nameColorObj = $("#nameColor").cxColor();
var areaColorObj = $("#areaColor").cxColor();
var lineColorObj = $("#lineColor").cxColor();
nameColorObj.color("#ff0000");
areaColorObj.color("#ff0000");
lineColorObj.color("#ff0000");


function styleChangeClick(){
 	var mapStyleDiv = document.getElementById("changeStyleDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
}

function gridDisplayChange(){
 	var mapStyleDiv = document.getElementById("gridDisplayDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
}
 	
function getArcgisDataOfGridsByLevel(level){
 	window.frames["iframeOfMapLoad"].getArcgisDataOfGridsByLevel(level);
}
	
function getArcgisDataOfResourcesByLevel(level){
	window.frames["iframeOfMapLoad"].getArcgisDataOfResourcesByLevel(level);
}

function setDefaultMapLevel(){
	window.frames["iframeOfMapLoad"].changeMapLevel();
}
//获取当前编辑的网格的信息
function loadArcgisData() {
	var url = "";
	var param = "?wid="+document.getElementById("wid").value+"&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType+"&parentGridId="+${parentGridId};
	url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json";
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
				window.frames["iframeOfMapLoad"].showCenterPoint($("#x").val(),$("#y").val());
		 	}
		 	var minLevel = 0;
		    var maxLevel = window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.arcgisScalenInfos.length-1;
		    document.getElementById("mapCenterLevel").innerHTML='';
			for(var i = minLevel;i<=maxLevel;i++){
				var option = "<option";
				if(data.list[0].mapCenterLevel==i){
				 option+=' selected="selected" ';
				}
				option+=" value=\'"+i+"\'>"+i+"</option>";
				$("#mapCenterLevel").append(option);
			}
		 }
	 });
}

function dataInit(data) {
	$("#mapt").val(data.mapt);
	$("#x").val(data.x);
	$("#y").val(data.y);
	$("#hs").val(data.hs);
	$("#name").val(data.gridName);
	window.frames["iframeOfMapLoad"].showCenterPoint(data.x,data.y);
	nameColorObj.color(data.nameColor);
	areaColorObj.color(data.areaColor);
	lineColorObj.color(data.lineColor);
	$("#lineWidth").val(data.lineWidth);
}


function getMapArcgisDatas1() {
	window.frames["iframeOfMapLoad"].getMapArcgisDatas();
}

function save(){
	document.getElementById("tipMessage").innerHTML = "";
	var x=$("#x").val(), y=$("#y").val(), hs=$("#hs").val(), nameColor=$("#nameColor").val(),areaColor=$("#areaColor").val(),lineColor = $("#lineColor").val(),lineWidth = $("#lineWidth").val(),mapCenterLevel = document.getElementById("mapCenterLevel").value;
	if(!x||!y){
		DivShow('中心点未设置！');
		return;
	}
	if(!hs){
		DivShow('未绘制轮廓数据！'); 
		return;
	}
	
	if(!lineWidth){
		lineWidth = "1";
	}
	if(/^#[0-9a-fA-F]{6}$/.test(nameColor)==false){
		DivShow('名称颜色，错误的颜色值！'); 
		return;
	}
	if(/^#[0-9a-fA-F]{6}$/.test(areaColor)==false){
		DivShow('区域颜色，错误的颜色值！');  
		return;
	}
	if(/^#[0-9a-fA-F]{6}$/.test(lineColor)==false){
		DivShow('轮廓颜色，错误的颜色值！'); 
		return;
	}
	
	if(window.frames["iframeOfMapLoad"].ARCGIS_DOCK_MODE == "1" && $("#FACTOR_STATION").val() == "1") {
		var layerName;
		var attributeCfg;
		var type = 1;
		var oldData;
		layerName = "gridLayer";
		type = 1;
		attributeCfg = {
			"ID":$("#wid").val(),
			"PID":${parentGridId},
			"NAME":$("#name").val(),
			"LEVEL":"${gridLevel}"
		};
		
		oldData = {
	        "id": $("#wid").val(),
	        "wid": $("#wid").val(),
	        "name": $("#name").val(),
	        "x": x,
	        "y": y,
	        "hs": hs,
	        "mapt": 5,
	        "lineColor": lineColor,
	        "layerName": layerName,
	        "lineWidth": lineWidth,
	        "areaColor": areaColor,
	        "nameColor": nameColor,
	        "gridName": $("#name").val(),
	        "gridPath": "",
	        "editAble":true,
	        "mapCenterLevel": mapCenterLevel
	    };
		window.frames["iframeOfMapLoad"].saveFeatureToServer(layerName,attributeCfg,oldData)
	}
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/saveArcgisDrawAreaPanel.json?";
	var data = "targetType=grid&wid="+$("#wid").val()+"&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType;
	data +="&x="+x+"&y="+y+"&hs="+hs+"&nameColor="+nameColor+"&areaColor="+areaColor+"&lineColor="+lineColor+"&lineWidth="+lineWidth+"&mapCenterLevel="+mapCenterLevel;
	$.ajax({
	   type: "POST",
	   url: url,
	   data: data,
	   dataType:"json",
	   success: function(data){
	     if(data.flag==true){
		    DivShow('保存成功！');
			window.document.getElementById("hs").value = "";
		    window.frames["iframeOfMapLoad"].clearGridLayer();
		    window.frames["iframeOfMapLoad"].showCenterPoint(x,y);
		    loadArcgisData();
			window.frames["iframeOfMapLoad"].getMapArcgisDatas();
	     }
	   },
	   error:function(msg){
		DivShow('保存失败！'); 
	   }
	});
}
	    
function DivHide(){
	$(".blind").slideUp();//窗帘效果展开
}

function DivShow(msg){
	$(".blind").html(msg);
	$(".blind").slideDown();//窗帘效果展开
	setTimeout("this.DivHide()",800);
}

//判断是否有网格轮廓信息
function hasArcgisData() {
	var url = "";
	var param = "?wid="+document.getElementById("wid").value+"&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType+"&parentGridId="+${parentGridId};
	url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json";
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
	 		if(data.list.length==0 && wid != "" && wid != null && typeof(wid) != undefined) {
				tipMess = "未标注中心点。未画网格轮廓！";
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
					tipMess = "未画网格轮廓！";
					$("#tipMessage").append(tipMess);
		 		}
		 	}
	 	}
 	});
}
</script>
</html>
