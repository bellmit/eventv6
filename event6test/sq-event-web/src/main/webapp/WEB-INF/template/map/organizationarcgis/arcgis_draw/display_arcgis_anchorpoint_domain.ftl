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
.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
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
            <td width="40"align = "right">名称：</td>
            <td width="120">${orgExtraInfo.orgName}</td>
            <td id="td0" width="20" align="right"><input onclick="changePolygon(this);" type="checkbox" name="checkbox" id="checkbox" style="margin-top:2px;"></td>
            <td id="td00" width="130" align="left">&nbsp;<span>显示子级区划图</span></td>
            <td id="td1" ><a href="#" onclick="editMap()" class="NorToolBtn EditBtn">编辑</a></td>
            <td id="td2" width="90" style="display:none;"><a href="#" onclick="draw('POLYGON')" class="NorToolBtn DrawBorderBtn">画轮廓</a></td>
            <td id="td3" width="100" style="display:none;"><a href="#" onclick="draw('POINT')" class="NorToolBtn SetCenterBtn">标中心点</a></td>
            <td id="td4" width="100" style="display:none;"><a href="#" onclick="edit()" class="NorToolBtn EditBtn">编辑轮廓</a></td>
            <td id="td5" width="80" style="display:none;"><a href="#" onclick="save()" class="NorToolBtn SmallSaveBtn">保存</a></td>
            <td id="td6" style="display:none;"><a href="#" onclick="showMap()" class="NorToolBtn BackBtn">返回</a></td>
          </tr>
        </table>
    </div>
</div>
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
<#include "/component/customEasyWin.ftl" />
<script>
	var flagPoint = false;
	var currentMapStyleObj;
	var x=0;
	var y=0;
	var hs="";
	var mapt=0;
	var editOrView = 'view';
	jQuery(document).ready(function() {
			$("#map0").css("height",$(document).height());
			$("#map0").css("width",$(document).width());
			$("#map1").css("height",$(document).height());
			$("#map1").css("width",$(document).width());
			$("#map2").css("height",$(document).height());
			$("#map2").css("width",$(document).width());
			setTimeout(function(){
			 	getArcgisInfo();
			 	
			},100); 
			window.onresize=function(){
			  	$("#map0").css("height",$(document).height());
				$("#map0").css("width",$(document).width());
				$("#map1").css("height",$(document).height());
				$("#map1").css("width",$(document).width());
				$("#map2").css("height",$(document).height());
				$("#map2").css("width",$(document).width());
			 }
	});
	
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
function getArcgisDataByCurrentSet(){
	getArcgisDataOfOrg();
}
function changePolygon(obj){
	if(obj.checked == true){
		getArcgisDataOfSubOrgs();
	}else if(obj.checked == false){
		getArcgisDataOfOrg();
	}
}

function getArcgisDataOfOrg(){
	$("#map").ffcsMap.clear({layerName : "orgLayer"});
	locateCenterAndLevel($('#orgId').val(),currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDataOfOrg.json?mapt='+currentArcgisConfigInfo.mapType+'&orgId='+$('#orgId').val();
 	$("#map").ffcsMap.render('orgLayer',url,2,true);
 	getArcgisDataOfOrgCenter();
}
function getArcgisDataOfOrgCenter(){
	$("#map").ffcsMap.clear({layerName : "orgCenterLayer"});
	var imgUrl = '${rc.getContextPath()}/js/map/arcgis/library/style/images/location_point.png';
	var url =  '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDataOfOrgCenter.json?mapt='+currentArcgisConfigInfo.mapType+'&orgId='+$('#orgId').val();
 	$("#map").ffcsMap.render('orgCenterLayer',url,0,true,imgUrl, 30, 40);
	$("#map").ffcsMap.ffcsDisplayhot({w:360,h:270},"orgCenterLayer",'组织机构',getInfoDetailOnMap);
}

function getArcgisDataOfSubOrgs(){
	$("#map").ffcsMap.clear({layerName : "orgLayer"});
	locateCenterAndLevel($('#orgId').val(),currentArcgisConfigInfo.mapType);
	var url =  '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDataOfSubOrgs.json?mapt='+currentArcgisConfigInfo.mapType+'&orgId='+$('#orgId').val();
 	$("#map").ffcsMap.render('orgLayer',url,2,true);
 	getArcgisDataOfSubOrgsCenter();
}

function getArcgisDataOfSubOrgsCenter(){
	$("#map").ffcsMap.clear({layerName : "orgCenterLayer"});
	var imgUrl = '${rc.getContextPath()}/js/map/arcgis/library/style/images/location_point.png';
	var url =  '${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getArcgisDataOfSubOrgsCenter.json?mapt='+currentArcgisConfigInfo.mapType+'&orgId='+$('#orgId').val();
 	$("#map").ffcsMap.render('orgCenterLayer',url,0,true,imgUrl, 30, 40);
	$("#map").ffcsMap.ffcsDisplayhot({w:360,h:270},"orgCenterLayer",'组织机构',getInfoDetailOnMap);
}


function getInfoDetailOnMap(mapData){
	var url = "";
	var context = "";
	var orgId=mapData['wid'];
	var isLeaf = "${isLeaf}";
	
	/*if("1"==isLeaf) {
		var dataUrl = "${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getGridInfoList.jhtml?orgId="+orgId+"&t="+Math.random();   
		$.ajax({
				type: "POST",
				url: dataUrl,
				dataType:"json",
				async:false,
				success: function(data){
					var tableBody='<table width="95%" border="0" cellspacing="0" cellpadding="0" style="border:1px solid #ccc; text-align:center;margin:10px auto;line-height:30px;">';
						
					if(data.length==0) {
						tableBody += "<tr>"
							tableBody+='<td class="border_b_r">没有子级网格信息</td>';
							tableBody += "</tr>"
					} else {
						for(var i=0;i<data.length;i++){
							var val=data[i];
							tableBody += "<tr>"
							if(editOrView = 'eidt'){
								var url = "${SQ_ZZGRID_URL}/admin/grid/layer/show/root/'+val.gridId+'.jhtml" ;
								tableBody+='<td><a href="void(0)" onclick="showMaxJqueryWindow(\"'+val.gridName+'\",\"'+url+'\")">'+val.gridName+'</a></td>';
							}else{
								var url = "${SQ_ZZGRID_URL}/admin/grid/layerForShow/show/root/'+val.gridId+'.jhtml"
								tableBody+='<td><a href="void(0)" onclick="showMaxJqueryWindow(\"'+val.gridName+'\",\"'+url+'\")">'+val.gridName+'</a></td>';
							}					
							tableBody += "</tr>"
						}
					}
					tableBody += "</table>";
					context = tableBody;
				}
			});
	}else{*/
			var getstaticsdataUrl =  "${rc.getContextPath()}/zhsq/map/organizationarcgis/organizationarcgis/getOrgStatInfoList.jhtml?orgId="+orgId+"&t="+Math.random();
			$.ajax({
				type: "POST",
				url: getstaticsdataUrl,
				dataType:"json",
				async:false,
				success: function(data){
					var tableBody='<table width="95%" border="0" cellspacing="0" cellpadding="0" style="border:1px solid #ccc; text-align:center;margin:10px auto;line-height:30px;">';
					if(data.length==0) {
						tableBody += "<tr>"
							tableBody+='<td>没有找到相关的统计信息</td>';
							tableBody += "</tr>"
					} else {
						for(var i=0;i<data.length;i++){
							var val=data[i];
							tableBody += "<tr>";
							tableBody+='<td class="border_b_r" width="50%">';
							if(val.resDealHost!=null && val.resDealHost!="" && val.resDealAction!=null && val.resDealAction!="") {
								tableBody+='<a href="'+val.resDealHost+val.resDealAction+'?dealParam='+val.resDealParamEncoderStr+'&title='+val.statTypeNameEncoderStr+'">'+val.statTypeName+'</a>'
							} else {
								tableBody+=val.statTypeName;
							}
							tableBody+='</td>';
							tableBody+='<td  width="50%">'+val.statValue+'</td>';
							tableBody += "</tr>";
						}
					}
					tableBody += "</table>";
					context = tableBody;
				}
				
			});
	//}

	return context;
}

function getArcgisDrawDataOfOrgs() {
	$("#map").ffcsMap.clear({layerName : "orgLayer"});
	$("#map").ffcsMap.clear({layerName : "orgCenterLayer"});
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
function editMap(){
	$("#td0").hide();
	$("#td00").hide();
	$("#td1").hide();
	$("#td2").show();
	$("#td3").show();
	$("#td4").show();
	$("#td5").show();
	$("#td6").show();
	getArcgisDrawDataOfOrgs();
}
function showMap(){
	location.reload();
	/*
	$("#td0").show();
	$("#td00").show();
	$("#td1").show();
	$("#td2").hide();
	$("#td3").hide();
	$("#td4").hide();
	$("#td5").hide();
	$("#td6").hide();
	changePolygon(document.getElementById("checkbox"));
	*/
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
