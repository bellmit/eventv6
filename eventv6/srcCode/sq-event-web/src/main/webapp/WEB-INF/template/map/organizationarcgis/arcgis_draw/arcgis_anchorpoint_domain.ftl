<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title>
	<style type="text/css">
*{margin:0; padding:0; list-style:none;}
.MapBar{height:32px; position:absolute; z-index:10; top:0; left:0; width:100%; font-size:12px; line-height:32px; color:#fff;}
.MapBar .con{height:32px; width:100%;}
.MapBar td{height:32px;}
.MapBar td span{color:#FF3;}
.AlphaBack{background-color:rgba(0, 0, 0, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c000000',endColorstr='#8c000000');}
.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
.button1{width:60px; height:28px; line-height:26px; text-align:center;}
</style>
	<script type="text/javascript">
		var js_ctx =  "${rc.getContextPath()}";
		var _myServer = "${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
		var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
	</script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/default/easyui.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<script src="${rc.getContextPath()}/js/map/arcgis/library/3.8/init.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsMap.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsMeasure.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSlider.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsOverviewMap.js"></script> 
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsLayerPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsFillQuery.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/ffcsTianDiTuLayer.js"></script> 

	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/arcgis_demo.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/arcgis_draw.js"></script>
	
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<script src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
</head>

<body>
<div class="MapBar">
	<div class="con AlphaBack">
		<input type="hidden" id="orgLocationInfoJSONString"  name="orgLocationInfoJSONString" />
		<input type="hidden" id="orgId"  name="orgId" value="${orgId}"/>
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            
            <td width="40">名称：</td>
            <td width="120">${orgExtraInfo.orgName}</td>
            
            <td width="70"><input name="" type="button" value="画轮廓" class="button1" onclick="draw('POLYGON')"></td>
            <td width="70"><input name="" type="button" value="标中心点" class="button1" onclick="draw('POINT')"></td>
            <td width="70"><input name="" type="button" value="编辑轮廓" class="button1" onclick="edit()"></td>
            <td ><input name="" type="button" value="保存" class="button1" onclick="save()"></td>
          </tr>
        </table>
    </div>
</div>
<div class="page-container" id="map" style="position: absolute; top:0px;bottom:0px;right:0px; left:0px; z-index: 1;">
</div>
<div id="jsSlider"></div>
<div class="MapTools">
	<ul>
		<li class="ThreeWei" onclick="threeWeiClick();"></li>
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
	var flagPoint = false;
	var currentMapStyleObj;
	var x=0;
	var y=0;
	var hs="";
	var mapt=0;
	jQuery(document).ready(function() {
		getArcgisInfo();
		getArcgisDrawDataOfOrgs();
	});
	
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
	
function getArcgisDrawDataOfOrgs() {
	locateCenterAndLevel($('#orgId').val(),currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDrawDataOfOrgs.json?mapt='+currentArcgisConfigInfo.mapType+'&orgId='+$('#orgId').val();
 	$("#map").ffcsMap.render('orgLayer',url,2,true);
 	
}
function locateCenterAndLevel(id,mapt) {
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&orgId='+id,
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
		 		if(x == 0 && y==0){
			 		x = obj.x;
			 		y = obj.y;
		 		}
			 	$("#map").ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.zoom
		    	});
		 	}
		 }
	 });
}

function draw(drawType){
	if(drawType == 'POINT') {
		$("#map").ffcsMap.draw(drawType,boundaryCallBack);
	}else {
		$("#map").ffcsMap.draw(drawType,boundaryCallBack);
	}
}	
function edit(){
	$("#map").ffcsMap.edit(true,boundaryCallBack);
}
function save(){
	if(mapt==0){
		alert("你还没有进行编辑操作！");
	}else {
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/saveArcgisDataOfOrg.json?mapt='+mapt+'&x='+x+'&y='+y+'&hs='+hs+'&orgId='+$('#orgId').val(),
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	alert("保存失败!");
			 },
			 success: function(data){
			 	if(data.flag == true){
			 		alert("保存成功！");
			 		window.opener.location.href=window.opener.location.href;
			 	}else {
			 		alert("保存失败!");
			 	}
			 }
		 });
	}
}



function boundaryCallBack(data) {
	var data1 = JSON.parse(data);
	var type = data1.type;
	mapt = currentArcgisConfigInfo.mapType;
	if(type == "polygon") {
		hs = data1.coordinates.toString();
		var xys = hs.split(",");
		x = xys[0];
		y = xys[1];
	}else if(type == "point") {
		var xys = data1.coordinates.toString().split(",");
		x = xys[0];
		y = xys[1];
	}
}

</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
