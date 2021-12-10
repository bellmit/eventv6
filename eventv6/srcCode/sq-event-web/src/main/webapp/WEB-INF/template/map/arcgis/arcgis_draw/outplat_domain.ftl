<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title>
	<script type="text/javascript">
		var callBackUrl = "${callBackUrl}";
	</script>
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
		
			#xyDisplayDiv{width:200px; position:absolute; z-index:1000; top:32px; left:80px; display:none;}
		#xyDisplayDiv ul li{color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#xyDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
		
	</style>
<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
</head>

<body>
<input type="hidden" name="mapt" id="mapt" value="${mapt}" />
<input type="hidden" readonly="true" name="x" id="x" value="${x?c}" />
<input type="hidden" readonly="true" name="y" id="y" value="${y?c}" />
<input type="hidden" readonly="true" name="mapCenterLevel" id="mapCenterLevel" value="${mapCenterLevel?c}" />

<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
<#if isEdit??>
	<#if (isEdit == "true")>
		<div class="MapBar">
			<div class="con AlphaBack1" style="height:32px">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
			        <tr style="float:left">
					    <td>
			            	<a href="###" onclick="endPointLocate()" class="NorToolBtn SmallSaveBtn">确定</a>
			            </td>
			            <td>
			            	<input type="hidden" readonly="true" name="mapt" id="mapt" value="" />
							<input type="hidden" readonly="true" name="x" id="x" value="" />
							<input type="hidden" readonly="true" name="y" id="y" value="" />
						</td>
						<td>
			    	 		<div class="MapLevel fl" style="width:130px">
				    	 		<ul>
					                <li><a class="styleChangeA" onclick="xyDisplayChange();">经纬查询</a></li>
					            	<li><img src="${uiDomain}/images/map/gisv0/special_config/images/xiala.png" onclick="xyDisplayChange();"></li>
				            	</ul>
			       			 </div>
			    	 	</td>
						<td>
						<#if noPosition??>
			    	 		<div id="tipMessage" style="color:yellow;font-size:16px;width: 100%;text-align: center;padding-top: 7px;">未标注地理位置！</div>
			    	 		</#if>
			    	 	</td>
			         </tr>
			    </table>    	
			</div>
		</div>
		<#else>
		<#if noPosition??>
	<div class="MapBar">
			<div class="con AlphaBack1" style="height:32px">
			<div id="tipMessage" style="color:yellow;font-size:16px;width: 100%;text-align: center;padding-top: 7px;">未标注地理位置！</div>
	</div>
		</div>
		</#if>
	</#if>
	<#else><#if noPosition??>
	<div class="MapBar">
			<div class="con AlphaBack1" style="height:32px">
			<div id="tipMessage" style="color:yellow;font-size:16px;width: 100%;text-align: center;padding-top: 7px;">未标注地理位置！</div>
	</div>
		</div></#if>
</#if>


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
  <div id="xyDisplayDiv" class="AlphaBack" style="display:none;">
	    	<ul>
	    		<li>
	    			<span>经度:<input name="searchX" type="text" value="" id="searchX" /></span>
	    		</li>
	    		<li>
	    			<span>纬度:<input name="searchY" type="text" value="" id="searchY" /></span>
	    		</li>
	    		<li>
	    			<span style="width:40px">&nbsp;</span>
	    			<input type="button" value="查询" class="NorBtn" onclick="searchXY()">
	    		</li>
	    	</ul>
	    </div>
<div style="height:0px">
<iframe id="cross_domain_frame" name="cross_domain_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

</body>
<script>
function xyDisplayChange(){
 	var mapStyleDiv = document.getElementById("xyDisplayDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
}
var flagPoint = false;
var mapType = '<#if mapType??>${mapType}</#if>';
var isEdit = '${isEdit}';
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
function showPolygon() {
	var map = $.fn.ffcsMap.getMap();

	//-- 区域
	var locationList = jQuery.parseJSON('${locationListJson}');
	if(locationList != null && locationList.length>0) {
		var locationPoints = [];
		for(var i=0;i<locationList.length;i++) {
			//var point = new google.maps.LatLng(locationList[i].y, locationList[i].x);
			var point = new esri.geometry.Point(locationList[i].x, locationList[i].y);
			locationPoints.push(point);
		}
		locationPoints.push(new esri.geometry.Point(locationList[0].x, locationList[0].y));
		//polygon = new google.maps.Polygon({map:map, path: locationPoints, strokeColor: 'blue', strokeOpacity: 0.8, strokeWeight: 2, fillColor: 'blue', fillOpacity: 0.35});
		
		var polygon = new esri.geometry.Polygon();
		polygon.addRing(locationPoints);
		
		
		var symbol = new esri.symbols.SimpleFillSymbol();
		symbol.setColor(new dojo.Color([255,0,0,0.25]));   
		
		var graphic = new esri.Graphic(polygon, symbol);
		
		var polygonGraphicsLayer = new esri.layers.GraphicsLayer({id:'polygonGraphicsLayer'});
		polygonGraphicsLayer.add(graphic);
		map.addLayer(polygonGraphicsLayer);
	}
}
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
	if(isEdit == "true" || isEdit == true){
		isEdit = true;
	}else {
		isEdit = false;
	}
	var imgUrl = '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png';
	<#if isEdit??>
		<#if (isEdit == "false")>
			<#if noPosition??>imgUrl = '';</#if>
		</#if>
		
		<#else>
		<#if noPosition??>imgUrl = '';</#if>
	</#if>
	
	$("#map").ffcsMap.onClickGetPoint(x,y, imgUrl , pointGetCallBack,isEdit);
	
	
	
	$("#map").ffcsMap.centerAt({
		x : parseFloat(x),          //中心点X坐标
		y : parseFloat(y),           //中心点y坐标
		wkid : 4326, //wkid 2437
		zoom : mapCenterLevel
	});
		    	
}

function pointGetCallBack(data) {
	var xys = data.toString().split(",");
	var x = xys[0];
	var y = xys[1];
	$('#x').val(x);
	$('#y').val(y);
	//$('#mapt').val(arcgisMapConfigInfo.mapStartType);
}

function endPointLocate() {
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	
	var data = 'x='+x+'&y='+y+'&mapt='+mapt;
	var isJinJiang = "<#if isJinJiang??>${isJinJiang!''}</#if>";
	if(typeof isJinJiang != 'undefined' && isJinJiang == "true"){
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getAuxiliaryPoliceAddrsByXY.json?',
            type: 'POST',
            timeout: 3000,
            dataType: "json",
            async: false,
            error: function (result) {
                $.messager.alert('友情提示', '获取协警地址异常!', 'warning');
            },
            success: function (result) {
				if(typeof result != "undefined" && typeof result['addr'] != "undefined"){
                    data = data + '&address='+result['addr'];
				}
                var url;
                if(callBackUrl != ''){
                    url = callBackUrl+'?'+ data;
                }else {
                    url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?'+ data;
                }
                $('#cross_domain_frame').attr('src', url);
                //parent.closeMaxJqueryWindow(); // 关闭弹出框
            }
        });
	}else{
        var url;
        if(callBackUrl != ''){
            url = callBackUrl+'?'+ data;
        }else {
            url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?'+ data;
        }
        $('#cross_domain_frame').attr('src', url);
        //parent.closeMaxJqueryWindow(); // 关闭弹出框
	}

}
function getArcgisOfGridDatas(gridId,mapt) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridForPoint.json?mapt='+mapt+'&wid='+gridId;
		$("#map").ffcsMap.render('gridLayer',url,2,true);
}

function searchXY(){
	var x = document.getElementById('searchX').value;
	var y = document.getElementById('searchY').value;
	if(x.length ==0 || y.length ==0){
		$.messager.alert('友情提示', '请输入经纬度!');
		return ;
	}
	x = parseFloat(x),y=parseFloat(y);
	if( isNaN(x) || isNaN(y)){
		$.messager.alert('友情提示', '经纬度只能是浮点数!');
		return ;
	}
	
	if( x < -180 || x >180 || y<-90 || y>90){
		$.messager.alert('友情提示', '经纬度超出地球了!');
		return ;
	}
	document.getElementById('searchX').value=x;
	document.getElementById('searchY').value=y;
	if(x != undefined && y != undefined ){
		$("#map").ffcsMap.centerAt({x : x,y : y});
		$("#map").ffcsMap.showCenterPoint(x, y, '/zhsq_event/js/map/arcgis/library/style/images/RedShinyPin.png','1');
		document.getElementById('x').value = x;
		document.getElementById('y').value = y;
	}else{
		 $.messager.alert('友情提示', '请输入经纬度!');
	}
}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
