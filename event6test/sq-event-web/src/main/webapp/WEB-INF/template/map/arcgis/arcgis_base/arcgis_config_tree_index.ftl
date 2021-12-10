<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title> 
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />

	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_demo.js"></script>
    <style type="text/css">
    
	</style>
</head>
<body style="width:100%;height:100%;border:none;" >
 	<div id="firstImgs" style="display: none;"></div>
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
		<li id="kuangxuanname" class="SelectMap" onclick="arcgisKuangXuan(this);"></li>
    	<li class="ClearMap" onclick="clearMyLayer();"></li>
		<li class="ThreeWei" onclick="threeWeiClick();"></li>
		
    	
    	<!--
    	<li class="MapFull"></li>
    	-->
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
<!-- baseDataTabs end -->

		<!-- 专题地图 -->
  		<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
  			<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
  			<input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
  			<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" />
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
  		</form> 
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
        <div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>
    </div>
    
    
    <!-----------------------------------人地事物情------------------------------------->
    <div class="ztIcon AlphaBack titlefirstall" style="display:block;">
		<div class="title"><span class="fr" onclick="CloseX()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePath" name="titlePath">专题图层</div></div>
	</div>	
	<div class="ztIcon AlphaBack dest firstall" style="display:block;">
    	<div class="TreeMenu" id="divTreeMenu">
			<ul id="gdcTree" class="ztree" ></ul>
		</div>
        <div class="clear"></div>
	</div>
	<div class="NorList AlphaBack">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</div>

</body>
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/videoPlayWindow.ftl" />
<#include "/component/customEasyWin.ftl" />
	<script  src="${rc.getContextPath()}/js/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script  src="${rc.getContextPath()}/js/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript">
var test1="tets";
 $(function(){
 	modleopen();
 	setTimeout(function(){
 	 	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
		var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	 	document.getElementById("gridLevelName"+level).checked = true;
	 	if (gridLevel-1 >= 5) {
			document.getElementById("buildName0").checked=true;
		}else {
			document.getElementById("buildName0").checked=false;
		}
	 	getArcgisInfo();
	 	getLayerMenuInfo();
	 	
	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	//getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
	 	var height = $("#map0")[0].scrollHeight-62;
		$("#divTreeMenu").css("max-height",height);
		modleclose();
	},100); 
	window.onresize=function(){
  	 	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 }
 })


   function showOrHidden(divName){
      // alert($(divName).css("display"));
		if($(divName).css("display")=="block"){
			$(divName).hide("normal");
		}else{
			$(".zttc_jcsj").hide();
			$(divName).show("normal");
		}
		
	 }
	 function showOrHiddenb(divName){
        if($(divName).css("display")=="block"){
			$(divName).hide("normal");
			//if(divName==".XMright"){
			//	$("#showButton").show();
			//}
			var imgurl="${rc.getContextPath()}/theme/scim/styles/images/zhihuijiandu/xm_r_btmbg.png";
			$("#btimg").attr("src",imgurl);
		}else{
				//$(".zhddfx").hide();
			$(divName).show("normal");
			var imgurl="${rc.getContextPath()}/theme/scim/styles/images/zhihuijiandu/xm_r_btmbg_a.png";
			$("#btimg").attr("src",imgurl);
			//if(divName==".XMright"){
			//	$("#showButton").hide();
			//}
		}
		
	 }


 
function getArcgisDataOfBuildsByCheck() {
	if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
	}else {
		$("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
	}
	
}
function getArcgisDataOfGridsByLevel(level) {
	if(document.getElementById("gridLevelName"+level).checked == true) {
		for(i=0; i<document.all("gridLevelName").length; i++) {
			if(document.all("gridLevelName")[i].value!=level){
				document.all("gridLevelName")[i].checked = false;
			}
		}
		
		var value = document.getElementById("li"+level).innerText;
		$("#level").html(value);
		
		getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level)
	}else {
		$("#map"+currentN).ffcsMap.clear({layerName : "gridLayer"});
	}
	
}

function getArcgisDataByCurrentSet(){
	for(i=0; i<document.all("gridLevelName").length; i++) {
		if(document.all("gridLevelName")[i].checked == true){
			var idval = $(document.all("gridLevelName")[i]).attr("id");
			var l = parseInt(idval.substring("gridLevelName".length,idval.length));
			getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,l);
		}
	}
	if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
	}
}

//获取arcgis地图路径的配置信息
function getMapArcgisInfo(){
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapArcgisInfo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
		 },
		 success: function(data){
		    var mapConfigInfo=eval(data.mapConfigInfo);
		    arcgisMapConfigInfo.Init(mapConfigInfo);// = new ArcgisMapConfigInfo(mapConfigInfo);
		    
		    if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
		    	currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
		    }else if(arcgisMapConfigInfo.mapStartType == "30") {//表示地图默认显示为2维
		    
		    	currentArcgisMapInfo.Init3D(arcgisMapConfigInfo);
		    }
		    loadArcgisMap("map","jsSlider","vec");
		 }
	 });
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

function getLayerMenuInfo(){
		var setting = {
		check:{
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},callback:{
			onClick:treeNodeClick
		}
	};
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeShow.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		     var zNodes = JSON.stringify(data);
		     $.fn.zTree.init($("#gdcTree"), setting, eval('(' + zNodes + ')'));
		    
		 }
	 });
}

function treeNodeClick(event, treeId, treeNode, clickFlag) {
	if(treeNode.id<=0) return;
	if(treeNode.gridPhoto != null &&(treeNode.gridPhoto).length>0){
		eval(treeNode.gridPhoto);
	}
}
var winType = "";//用于判断是否关闭详细窗口
var mapObjectName = "";//用于确定刷新的列表
function flashData(){
	if(winType!="" && winType=='0'){//关闭详细窗口
		closeCustomEasyWin();
		winType = "";
	}
	
	if(mapObjectName == "待办事件"){
		metter();
		showObjectList(mapObjectName);
	}else if(mapObjectName == "将到期"){
		metter();
		showObjectList(mapObjectName);
	}
}
function firstall(){
	$("#titlePath").html("专题图层");
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	$(".firstall").show();
	nowAlphaBackShow = "firstall";
}

function showObjectList(objectName) {
	var titlehtml = "<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > "+objectName;
	$("#titlePath").html(titlehtml);
	
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	mapObjectName = objectName;//用于回调列表刷新
	getObjectListUrl(objectName);
}
</script>
</html>
