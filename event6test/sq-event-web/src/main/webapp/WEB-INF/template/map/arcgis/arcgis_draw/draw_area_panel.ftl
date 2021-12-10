<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache" />
    <title>地图数据编辑panel</title>
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
		#changeStyleDiv{width:160px; position:absolute; z-index:10000; top:32px; left:390px; display:none;}
		#changeStyleDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#changeStyleDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		.styleChangeA:hover{text-decoration : none}
		
		#gridDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:550px; display:none;}
		#gridDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#gridDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}

        #buildDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:175px; display:none;}
        #buildDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
        #buildDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}

		#resourcesDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:550px; display:none;}
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
			<input type="hidden" id="wid" value="${wid}" /> 
	    	<input type="hidden" id="name" value="${name}" /> 
	    	<input type="hidden" id="targetType" value="${targetType}" />
	    	<input type="hidden" id="FACTOR_STATION" value="<#if FACTOR_STATION??>${FACTOR_STATION}</#if>" />
	    	<input type="hidden" id="FACTOR_SERVICE" value="<#if FACTOR_SERVICE??>${FACTOR_SERVICE}</#if>" />
	    	<input type="hidden" id="FACTOR_URL" value="<#if FACTOR_URL??>${FACTOR_URL}</#if>" />
	    	<input type="hidden" id="parentGridId" value="${parentGridId}" /> 
	    	<input type="hidden" id="gridLevel" value="${gridLevel}" /> 
	    	<input type="hidden" id="isDetail" value="<#if isDetail??>${isDetail}</#if>" /> 

	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          <#if (isDetail=='1')>
	          	<td>
	          		<input type="hidden" id="mapt" value="" disabled="true" />
	    			<input type="hidden" id="x" value="" readonly="true"/>
	    			<input type="hidden" id="y" value="" readonly="true"/>
	    	 		<input type="hidden" id="hs" value="" readonly="true"/>
	    	<!-- 	地图类型：<input type="text" id="mapt" value="" disabled="true" />
	    			中心点经度：<input type="text" id="x" value="" readonly="true"/>
	    			中心点纬度：<input type="text" id="y" value="" readonly="true"/>
	    	 		轮廓点集合：<input type="text" id="hs" value="" readonly="true"/>-->
	    	 	</td>
	          <#else>
	          	<td>
	          		<a href="#" onclick="save()" class="NorToolBtn SmallSaveBtn">保存</a>
	          	</td>

					<#if targetType?? && targetType=="build" && USE_DRAW_BUILDING_OUTLINE?? && USE_DRAW_BUILDING_OUTLINE=='false'>
                        <td>
                            <a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">标注</a>
                        </td>
                        <td>
                            <a href="#" onclick="moveToCenterPoint()" class="NorToolBtn SetCenterBtn">移动到网格中心点</a>
                        </td>
					<#else>
                        <td>
                            <a href="#" onclick="editBoundary()" class="NorToolBtn EditBtn">编辑轮廓</a>
                        </td>
                        <td>
                            <a href="#" onclick="drawCenterPoint()" class="NorToolBtn SetCenterBtn">编辑中心点</a>
                        </td>
                        <td>
                            <a href="#" onclick="deleteData()" class="NorToolBtn DelBtn">删除轮廓和中心点</a>
                        </td>
					</#if>

	          	<td>
	          		<input type="hidden" id="mapt" value="" disabled="true" />
	    			<input type="hidden" id="x" value="" readonly="true"/>
	    			<input type="hidden" id="y" value="" readonly="true"/>
	    	 		<input type="hidden" id="hs" value="" readonly="true"/>
	    	<!-- 	地图类型：<input type="text" id="mapt" value="" disabled="true" />
	    			中心点经度：<input type="text" id="x" value="" readonly="true"/>
	    			中心点纬度：<input type="text" id="y" value="" readonly="true"/>
	    	 		轮廓点集合：<input type="text" id="hs" value="" readonly="true"/>-->
	    	 		
	    	 		
	    	 	</td>
				<#if (targetType?? && targetType!="build") || (targetType?? && targetType=="build" && USE_DRAW_BUILDING_OUTLINE?? && USE_DRAW_BUILDING_OUTLINE=='true')>
	    	 	<td>
	    	 		<div class="MapLevel fl" style="width:130px">
		    	 		<ul>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="styleChangeClick();" /></li>
			                <li><a class="styleChangeA" onclick="styleChangeClick();">设置</a></li>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="styleChangeClick();" /></li>
		            	</ul>
	       			 </div>
	       			 
	    	 	</td>
				</#if>
	    	 	<td>
	    	 	<#if (targetType=='grid')>
	    	 	<div class="MapLevel fl" style="width:130px">
		    	 		<ul>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="gridDisplayChange();" /></li>
			                <li><a class="styleChangeA" onclick="gridDisplayChange();">辅助显示</a></li>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="gridDisplayChange();" /></li>
		            	</ul>
	       			 </div>
	    	 	</td>
	    	 	</#if>
				  <#if (targetType=='build')>
					  <div class="MapLevel fl" style="width:130px">
						  <ul>
							  <li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="buildDisplayChange();" /></li>
							  <li><a class="styleChangeA" onclick="buildDisplayChange();">辅助显示</a></li>
							  <li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="buildDisplayChange();" /></li>
						  </ul>
					  </div>
					  </td>
				  </#if>
	    	 	<#if (targetType=='hlhx')>
	    	 	<div class="MapLevel fl" style="width:130px">
		    	 		<ul>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/cengji.png" onclick="resourcesDisplayChange();" /></li>
			                <li><a class="styleChangeA" onclick="resourcesDisplayChange();">辅助显示</a></li>
			            	<li><img src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png" onclick="resourcesDisplayChange();" /></li>
		            	</ul>
	       			 </div>
	    	 	</td>
	    	 	</#if>
	    	 	</#if>
				</tr>
	        </table>
	    </div>
	    
	    <div id="changeStyleDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li onclick="styleChangeClick();" style="text-align:right;">
	    			关闭
	    		</li>
	    		<#if (targetType=='grid')>
          			<li>
          				默认显示层级:
          				<select id="mapCenterLevel" class="inp1" style="width:40px;height:18px;color:#000;padding:0;" onchange="setDefaultMapLevel();"></select>
	    			<!-- <input type="hidden" id="mapCenterLevel" value="" /><td></td> -->
    				</li>
    			</#if>
	    		<li>
	    			名称颜色：<input type="text" id="nameColor" value="" readonly="true" class="inp1" style="width:50px"/>
	    		</li>
	    		<li>
	    			轮廓颜色：<input type="text" id="lineColor" value=""  readonly="true" class="inp1" style="width:50px"/>
	    		</li>
                <li>
                    轮廓粗细：
					<select id="lineWidth" class="inp1" style="width:45px;height:18px;color:#000;padding:0;">
						<option value="1">1px</option>
                        <option value="2">2px</option>
                        <option value="3">3px</option>
                        <option value="4">4px</option>
                        <option value="5">5px</option>
                        <option value="7">7px</option>
                        <option value="8">8px</option>
                        <option value="9">9px</option>
                        <option value="10">10px</option>
                        <option value="11">11px</option>
                        <option value="12">12px</option>
					</select>
                </li>
                <li>
                    区域颜色：<input type="text" id="areaColor" value=""  readonly="true" class="inp1" style="width:50px"/>
                </li>
                <li>
                    区域透明度：
                    <select id="colorNum" class="inp1" style="width:50px;height:18px;color:#000;padding:0;">
                        <option value="0">0</option>
						<option value="0.1">0.1</option>
                        <option value="0.2">0.2</option>
                        <option value="0.3">0.3</option>
                        <option value="0.4">0.4</option>
                        <option value="0.5">0.5</option>
                        <option value="0.7">0.7</option>
                        <option value="0.8">0.8</option>
                        <option value="0.9">0.9</option>
                        <option value="1">1</option>
                    </select>
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
        <div id="buildDisplayDiv" class="AlphaBack" style="display:none;">
            <ul>
                <li>
                    <span><input name="displayZhoubianBuilds" type="checkbox" value="1" onclick="getArcgisDataOfZhoubianBuilds();" id="displayZhoubianBuilds" /></span>周边楼宇
                </li>
            </ul>
        </div>

	    <div id="resourcesDisplayDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li>
	    			<span><input name="displayResourcesLevel1" type="checkbox" value="1" onclick="getArcgisDataOfResourcesByLevel(1);" id="displayResourcesLevel1" /></span>上级轮廓
	    		</li>
	    		<li>
	    			<span><input name="displayResourcesLevel2" type="checkbox" value="2" onclick="getArcgisDataOfResourcesByLevel(2);" id="displayResourcesLevel2" /></span>本级轮廓
	    		</li>
	    	</ul>
	    </div>
	</div>    
    <div id="myMap" style="position:absolute; top:0px;bottom:0px;right:0px; left:0px; border: #ccc 1xp solid;">
    	<iframe data-iframe="true" name="iframeOfMapLoad" id="iframeOfMapLoad" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfDraw.jhtml" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
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

var USE_DRAW_BUILDING_OUTLINE = "<#if USE_DRAW_BUILDING_OUTLINE??>${USE_DRAW_BUILDING_OUTLINE}</#if>";

var isDetail=$("#isDetail").val();
if(isDetail){//当详情时，背景透明
	$(".AlphaBack1").css("background-color","transparent");
}

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
function buildDisplayChange(){
    var mapStyleDiv = document.getElementById("buildDisplayDiv");
    if(mapStyleDiv.style.display == 'none') {
		<#if USE_DRAW_BUILDING_OUTLINE?? && USE_DRAW_BUILDING_OUTLINE=='true'>
			$("#buildDisplayDiv").css("left","550px");
		<#else>
            $("#buildDisplayDiv").css("left","175px");
		</#if>
        mapStyleDiv.style.display = 'block';
    }else {
        mapStyleDiv.style.display = 'none';
    }
}
 function resourcesDisplayChange(){
 	var mapStyleDiv = document.getElementById("resourcesDisplayDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
 }
 function getArcgisDataOfGridsByLevel(level){
 	window.frames["iframeOfMapLoad"].getArcgisDataOfGridsByLevel(level);
 }
function getArcgisDataOfZhoubianBuilds(){
    window.frames["iframeOfMapLoad"].getArcgisDataOfZhoubianBuilds();
}
 function getArcgisDataOfResourcesByLevel(level){
 	window.frames["iframeOfMapLoad"].getArcgisDataOfResourcesByLevel(level);
 }
//获取当前编辑的网格的信息
function loadArcgisData() {
	var url = ""
	var param = "?wid="+document.getElementById("wid").value+"&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType+"&parentGridId="+${parentGridId};
	if($("#targetType").val() == "grid") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json";
	}else if($("#targetType").val() == "build") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";
	}else if($("#targetType").val() == "segmentgrid") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfSegmentGrid.json";
	}else if($("#targetType").val() == "lyjjBuilding") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfLyjjBuilding.json";
	}else if($("#targetType").val() == "hlhx") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfResources.json";
		param = param + "&targetType="+$("#targetType").val();
	}else if($("#targetType").val() == "point") {
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfPoint.json";
		param = param + "&targetType="+$("#targetType").val();
	}else{
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfResources.json";
		param = param + "&targetType="+$("#targetType").val();
	}
	
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
			 if(data != null && data.list !=null && data.list.length>0) {
                 dataInit(data.list[0]);
		 	 }else{
		 		$("#mapt").val("");
				$("#x").val("");
				$("#y").val("");
				$("#hs").val("");
		 	}
		 	if($("#targetType").val() == "grid") {
			var minLevel = 0;
		    var maxLevel = window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.arcgisScalenInfos.length-1;
			for(var i = minLevel;i<=maxLevel;i++){
				var option = "<option";
				if(data && data.list && data.list[0].mapCenterLevel==i){
				 option+=' selected="selected" ';
				}
				option+=" value=\'"+i+"\'>"+i+"</option>";
				$("#mapCenterLevel").append(option);
	    	}
		}
		 }
	 });
}

function dataInit(data) {
	$("#mapt").val(data.mapt);
	$("#x").val(data.x);
	$("#y").val(data.y);
	$("#hs").val(data.hs);
	nameColorObj.color(data.nameColor);
	areaColorObj.color(data.areaColor);
	lineColorObj.color(data.lineColor);
	$("#lineWidth").val(data.lineWidth);
    $("#colorNum").val(data.colorNum);
}

function moveToCenterPoint() {
	window.frames["iframeOfMapLoad"].locateGridCenterById($("#parentGridId").val());
}

function drawCenterPoint() {
	var x = $("#x").val();
	var y = $("#y").val();
	window.frames["iframeOfMapLoad"].drawPoint(pointCallBack,x,y)
}

//设置默认显示层级
function setDefaultMapLevel(){
	window.frames["iframeOfMapLoad"].changeMapLevel();
}

function drawBoundary() {
	window.frames["iframeOfMapLoad"].draw('POLYGON',boundaryCallBack)
}
function editBoundary() {
	var x = $("#x").val();
	var y = $("#y").val();
	var hs = $("#hs").val();
	window.frames["iframeOfMapLoad"].drawPanel(boundaryCallBack,x,y,hs);
}
function getMapArcgisDatas1() {
window.frames["iframeOfMapLoad"].getMapArcgisDatas();
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
	var mapt = window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType;
	$("#mapt").val(mapt);
	if($("#hs").val() == "") {
		$("#hs").val(hs);
	}
}

	    function save(){
			var x=$("#x").val(), y=$("#y").val(), hs=$("#hs").val(),
				nameColor=$("#nameColor").val(),areaColor=$("#areaColor").val(),
				lineColor = $("#lineColor").val(),lineWidth = document.getElementById("lineWidth").value;
            var mapCenterLevel = '';
			if(typeof(document.getElementById("mapCenterLevel")) != 'undefined' && document.getElementById("mapCenterLevel") != null){
				mapCenterLevel = document.getElementById("mapCenterLevel").value;
			}
			var colorNum = 0;
            if(typeof(document.getElementById("colorNum")) != 'undefined' && document.getElementById("colorNum") != null){
                colorNum = document.getElementById("colorNum").value;
            }
	    	if(!x||!y){
	    		alert("中心点未设置"); return;
    		}
	    	//if(USE_DRAW_BUILDING_OUTLINE != "false" && !hs){
	    	//	alert("未绘制轮廓数据！"); return;
	    	//}
	    	
	    	if($("#targetType").val() == "grid") {
	    	 	mapCenterLevel = $("#mapCenterLevel").val();
	    		if(!mapCenterLevel || mapCenterLevel == "") {
	    			if($("#targetType").val() == "grid") {
	    				mapCenterLevel=12;
	    			}else if($("#targetType").val() == "build") {
	    				mapCenterLevel=14;
	    			}
	    		}
	    	}
	    	if(!lineWidth){
	    		
	    		if($("#targetType").val() == "grid") {
    				lineWidth = "2";
    			}else if($("#targetType").val() == "build") {
    				lineWidth = "1";
    			}else {
    				lineWidth = "1";
    			}
	    	}
	    	if(/^#[0-9a-fA-F]{6}$/.test(nameColor)==false){
	    		alert("名称颜色，错误的颜色值！"); return;
	    	}
	    	if(/^#[0-9a-fA-F]{6}$/.test(areaColor)==false){
	    		alert("区域颜色，错误的颜色值！"); return;
	    	}
	    	if(/^#[0-9a-fA-F]{6}$/.test(lineColor)==false){
	    		alert("轮廓颜色，错误的颜色值！"); return;
	    	}
	    	
	    	if(window.frames["iframeOfMapLoad"].ARCGIS_DOCK_MODE == "1" && $("#FACTOR_STATION").val() == "1") {
	    		var layerName;
	    		var attributeCfg;
	    		var oldData;
	    		if($("#targetType").val() == "grid") {
	    			layerName = "gridLayer";
	    			
	    		}else if($("#targetType").val() == "build") {
	    			layerName = "buildLayer";
	    		}
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
                    "colorNum": colorNum,
			        "nameColor": nameColor,
			        "gridName": $("#name").val(),
			        "gridPath": "",
			        "editAble":true,
			        "mapCenterLevel": mapCenterLevel
			    };
	    		window.frames["iframeOfMapLoad"].saveFeatureToServer(layerName,attributeCfg,oldData)
	    	}
	    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/saveArcgisDrawAreaPanel.json?";
	    	var data = "targetType=${targetType}&wid=${wid}&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType;
	    	data +="&x="+x+"&y="+y+"&hs="+hs+"&nameColor="+nameColor+"&areaColor="+areaColor+"&colorNum="+colorNum+"&lineColor="+lineColor+"&lineWidth="+lineWidth+"&mapCenterLevel="+mapCenterLevel;
	    	$.ajax({
			   type: "POST",
			   url: url,
			   data: data,
			   dataType:"json",
			   success: function(data){
			     if(data.flag==true){
				     alert("保存成功！");
				     window.document.location.reload();
			     }else{
                     alert("保存失败！");
				 }
			   },
			   error:function(msg){
			   	 alert("保存失败！");
			   }
			});
	    }
	    function deleteData() {
	    	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
	    	var data = "targetType=${targetType}&wid=${wid}&mapt="+window.frames["iframeOfMapLoad"].currentArcgisConfigInfo.mapType;
			if(!confirm('确定要删除?')){
	    		return false;
	    	}
	    	var type = 1;
	    	var layerName="";
	    	if($("#targetType").val() == "grid") {
	    			layerName = "gridLayer";
	    			type = 2;
	    		}else if($("#targetType").val() == "build") {
	    			layerName = "buildLayer";
	    			type = 1
	    		}
	    	if(window.frames["iframeOfMapLoad"].ARCGIS_DOCK_MODE == "1") {
	    		var condition = " TYPE="+type + " AND LAYERNAME='"+layerName+"' AND GRIDID="+$("#wid").val()+" AND MAPT="+$("#mapt").val();
	    		window.frames["iframeOfMapLoad"].deleteFeatureToServer(condition)
	    	}
	    	
	    	
	    	$.ajax({
			   type: "POST",
			   url: url,
			   data: data,
			   dataType:"json",
			   success: function(data){
			     if(data.flag==true){
				     alert("删除成功！");
				     window.document.location.reload();
			     }if(data.flag==false){
			     	alert("删除失败！");
			     }
			   },
			   error:function(msg){
			   	 alert("删除失败！");
			   }
			});
	    }
</script>
</html>
