<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title> 
	<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<#include "/component/ImageView.ftl" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
    <link rel="stylesheet" href="${uiDomain!''}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/zhoubian_index.js"></script>
    <link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
	<script src="${ANOLE_COMPONENT_URL}/js/components/popWindow/jquery.anole.popWindow.js"></script>
	<script type="text/javascript">
	//图片查看器回调
	function ffcs_viewImg(fieldId){
		var sourceId = fieldId + "_Div";
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	}
	</script>
	<style>
		.ztree li{overflow:hidden;}
		.ztree li span{font-size:14px;}
		.mapbox{ cursor: pointer;background:#fff; border:2px solid #2ab7b2; padding:5px; position:relative; min-width:30px; display:block; white-space:nowrap;}
		.mapicon{ display:inline-block;margin-right:3px;}
		.maparrow{ background:url(${uiDomain!''}/images/map_tree_arrow.png) no-repeat; width:9px; height:6px; position:absolute; left:10px; bottom:-8px;}
		.message-div{color:black;font-size:14px;padding-left:20px;padding-right:20px;padding-top:4px;}
		.message-div p {line-height:25px;}
		.message-div p a {text-decoration:none; color:#27a6f7; float:right; padding-top:10px;}
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
		<div id="heatLayer"></div>
	</div>
	<div id="jsSlider"></div>
    <div class="sjmenu" style="z-index: 12;display: none;">
        <p class="resourceCurrent"><span class="sjmenu-fx"></span><a href="#">展开</a></p>
        <ul class="sjmenu-an" style="display: none;">
            <li class="sjmeenus_first" id="team_li"><p class="sjmenu-icon1"></p><a href="#">响应机构</a></li>
            <li class="sjmeenus" id="gridadmin_li"><p class="sjmenu-icon2"></p><a href="#">网格员</a></li>
            <li class="sjmeenus" id="eye_li"><p class="sjmenu-icon3"></p><a href="#">视频探头</a></li>
        </ul>
        <div class="sjmenu-yjcd" style="display: none;">
            在附近找：
            <select id="distance" class="" style="color:#000;">
                <option value="" selected>请选择范围</option>
                <option value="500">500米</option>
                <option value="1000">1公里</option>
                <option value="3000">3公里</option>
                <option value="5000">5公里</option>
            </select>
            <div class="left_content1" style="margin-top:3px;">
				<div class="rangebox">
					<ul>
						<li id="orgteam_li"><div></div><i><img src="${uiDomain!''}/images/p_icon1.png" width="52" height="55" alt=""/></i><a href="#">综治机构</a></li>
						<li id="prevtionteam_li"><div></div><i class="ys1"><img src="${uiDomain!''}/images/p_icon2.png" width="52" height="55" alt=""/></i><a href="#">群防群治</a></li>
					</ul>
				</div>
            </div>
            <div class="left_content2" style="margin-top:3px;">
            </div>
            <div class="left_content3" style="margin-top:3px;">
            </div>
        </div><!--end .sjmenu-yjcd-->
    </div>
<div class="MapTools">
	<ul>
    	<li class="ClearMap" onclick="clearMyLayerB();"></li>
		<li class="ThreeWei" onclick="threeWeiClick();"></li>
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
  			<input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
  			<input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
  		</form> 
     <!-----------------------------------框选设置------------------------------------->
    <div class="NorMapOpenDiv2 kuangxuanWindow hide dest" style="bottom:30px; left:60px;">
		<div class="box" style="width:376px;height:343px;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeKuangxuan()"></span>选择您想要查看的内容</div>
	        <iframe id="kuangxuanConfig" name="kuangxuanConfig" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	    <!--<div class="shadow"></div>-->
	</div>
	<!-----------------------------------设置------------------------------------->
    <div class="NorMapOpenDiv2 zhoubianWindow hide dest" style="bottom:30px; left:60px;">
		<div class="box" style="width:376px;height:404px;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeZhoubian()"></span>选择您想要查看的内容</div>
	        <iframe id="zhoubianConfig" name="zhoubianConfig" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	    <!--<div class="shadow"></div>-->
	</div>
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
        <div class="labtop fl" style=""></div><#--头部已选择-->
        <div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>
        <!-- -->
		<#if kuangxuanFlag?? && kuangxuanFlag == "yes">
            <div class="kuangxuan fr"><a href="#"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/kuangxuan.png" />框选统计</a></div>
		</#if>

    	<!--<div class="zhoubian fr"><a href="#"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/kuangxuan.png" />周边资源</a></div>-->
    </div>

    <!-----------------------------------人地事物情------------------------------------->
    <div class="ztIcon AlphaBack titlefirstall" style="display:none;">
		<div class="title"><span class="fr" onclick="CloseX()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePath" name="titlePath">专题图层</div></div>
	</div>
	<div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
		<div class="title"><span class="fr" onclick="zhoubianListHide()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="" onclick="ShowSearchBtnZhouBian()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
	</div>	
	<div class="ztIcon AlphaBack dest firstall" style="display:block;">
		<div id="divTreeMenu">
			<ul id="ulFirstall"></ul>
		</div>
        <div class="clear"></div>
	</div>
    <!-----------------------------------人------------------------------------->
	<div class="ztIcon AlphaBack people">
		<ul id="ulPeople"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------地------------------------------------->
	<div class="ztIcon AlphaBack world">
		<ul id="ulWorld"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------事------------------------------------->
	<div class="ztIcon AlphaBack metter">
		<ul id="ulMetter"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------物------------------------------------->
	<div class="ztIcon AlphaBack thing">
		<ul id="ulThing"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------情------------------------------------->
    <div class="ztIcon AlphaBack situation">
		<ul id="ulSituation"></ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------组织------------------------------------->
    <div class="ztIcon AlphaBack organization">
		<ul  id="ulOrganization"></ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------精准扶贫------------------------------------->
    <div class="ztIcon AlphaBack precisionPoverty">
		<ul  id="ulPrecisionPoverty"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------通用------------------------------------->
    <div class="ztIcon AlphaBack common">
        <ul  id="ulCommon"></ul>
        <div class="clear"></div>
    </div>
	
	<div class="NorList AlphaBack">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
	<div class="NorListZhouBian AlphaBackZhouBian zhoubianList">
		<iframe id="zhoubian_list_frme" name="zhoubian_list_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</div>

<div id="dialog" title="三维图展示">
    <iframe id="lc_skylineview_frme" name="lc_skylineview_frme" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

<!-- 出租屋色块提示 -->
<div id="rentRoomTip" class="AlphaBack" style="display:none;right:261px;bottom:2px;position: absolute;z-index:10; top: auto; left: auto; padding: 7px; color:#fff; font-size:12px;">
  <div class="legend-text">出租屋色块提示：</div>
  <span class="legend-item" style="background-color:#ED0000;">超出均值</span>
  <span class="legend-item" style="background-color:#FDC100;">均值范围</span>
  <span class="legend-item" style="background-color:#05AF4C;">低于均值</span>
</div>

</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/videoPlayWindow.ftl" />
<#include "/component/customEasyWin.ftl" />
<#include "/component/mmp.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
	<script src="${rc.getContextPath()}/js/map/spgis/lib/jQuery.md5.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/message/video_surveillance_msg.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/xdate.js"></script>
<script type="text/javascript">




    var divDrag = {
	kxWinObj: null,
	zbWinObj: null
};
var test1="tets";
 $(function(){
     window.focus();
     modleopen();
 	zhoubianListHide();
 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
 	setTimeout(function(){
 		var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	 	document.getElementById("gridLevelName"+level).checked = true;
		eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");//--初始化工作流！
	 	getArcgisInfo(function() {// 地图初始化完毕
	 		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	 		if ($("#orgCode").val().indexOf("3501") == 0) {
	 			$("#HeatMapDiv").show();
	 		}
	 		$.initAnolePopWindow({
	 			voiceCall : function(partyName, verifyMobile) {
	 				showCall(verifyMobile, partyName);
	 			},
	 			voiceBox : function(partyName, verifyMobile) {
	 				showCall(verifyMobile, partyName);
	 			}
	 		});
	 	});
	 	
	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	
	 	modleclose();
	 	
	 	getLayerMenuInfo();
	 	
	 	checkNodes();

 	},100);
 	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 }
	divDrag.kxWinObj = $(".kuangxuanWindow");
	divDrag.zbWinObj = $(".zhoubianWindow");
	divDrag.kxWinObj.mousedown(function(e) {
		$(this).css("cursor", "move");
		var offset = $(this).offset();
		var x = e.pageX - offset.left;
		var y = e.pageY - offset.top;
		$(document).bind("mousemove", function(ev) {
			divDrag.kxWinObj.stop();
			var _x = ev.pageX - x;
			var _y = ev.pageY - y;
			divDrag.kxWinObj.css({
				left : _x,
				top : _y
			});
		});
	}).mouseup(function() {
		divDrag.kxWinObj.css("cursor", "default");
		$(document).unbind("mousemove");
	});
	divDrag.zbWinObj.mousedown(function(e) {
		$(this).css("cursor", "move");
		var offset = $(this).offset();
		var x = e.pageX - offset.left;
		var y = e.pageY - offset.top;
		$(document).bind("mousemove", function(ev) {
			divDrag.zbWinObj.stop();
			var _x = ev.pageX - x;
			var _y = ev.pageY - y;
			divDrag.zbWinObj.css({
				left : _x,
				top : _y
			});
		});
	}).mouseup(function() {
		divDrag.zbWinObj.css("cursor", "default");
		$(document).unbind("mousemove");
	});
 });

var msgIds = new Array();
var msgTels = new Array();
function getMsgs(){
    return {msgUserIds:msgIds,msgTels:msgTels};
}

var x;
var y;

//显示周边专题
function showResources(inputx,inputy){

    if(inputx==""||inputy==""||inputx==null||inputy==null){
        $('.sjmenu').hide();
    }else{
        $('.sjmenu').show();
        $('.sjmenu-an').css('display','none');
        $('.resourceCurrent').click();
        if(!(inputx == x && inputy == y)){
            x = inputx;y = inputy;
            cleanZhouBianLayer();

            removetype('gridadmin');
            removetype('eye');
            removetype('orgteam');
            removetype('prevtionteam');

            gisZhouBian();
        }
    }
}
function getArcgisDataOfBuildsByCheck() {
	if(document.getElementById("buildName0").checked == true) {
		$("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
		$("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"}); 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
	}else {
		if(ARCGIS_DOCK_MODE == "1") {
			featureHide("buildLayer"+gridLyerNum);
		}else {
			$("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
			$("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
		}
		
	}
	
}
function getArcgisDataOfGridsByLevel(level) {
	if(document.getElementById("gridLevelName"+level).checked == true) {
		var glns = $("input[name='gridLevelName']");
		for(var i=0; i<glns.length; i++) {
			if(glns[i].value!=level){
				glns[i].checked = false;
			}
		}

		var value = document.getElementById("li"+level).innerText;
		$("#level").html(value);
		
		getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level)
	}else {
		if(ARCGIS_DOCK_MODE == "1") {
			featureHide("gridLayer"+gridLyerNum);
		}else {
			$("#map"+currentN).ffcsMap.clear({layerName : "gridLayer"});
		}
		
	}
}
function getArcgisDataByCurrentSet(){
	var glns = $("input[name='gridLevelName']");
	for(var i=0; i<glns.length; i++) {
		if(glns[i].checked == true){
			var idval = $(glns[i]).attr("id");
			var l = parseInt(idval.substring("gridLevelName".length,idval.length));
			getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,l);
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
function getArcgisInfo(backFn){
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
		    	loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap",backFn);
		    }
		 }
	 });
}
var displayStyle = '0'; // 平铺
function getLayerMenuInfo(){
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getDisplayStyle.json?t='+Math.random(),
		type: 'POST',
		timeout: 300000,
		data: { orgCode:orgCode,homePageType:homePageType},
		dataType:"json",
		async: false,
		error: function(data){
		 	$.messager.alert('友情提示','图层展示方式配置信息获取出现异常!','warning');
		},
		success: function(data){
			if (data == '1') { // 树形展示
				displayStyle = data;
			}
		}
	});
	
	if (displayStyle == '0') {
		$.ajax({ 
			 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
			 type: 'POST',
			 timeout: 300000,
			 data: { orgCode:orgCode,homePageType:homePageType,gdcId:4,isRootSearch:1},
			 dataType:"json",
			 async: true,
			 error: function(data){
			 	$.messager.alert('友情提示','图层配置信息获取出现异常!','warning');
			 },
			 success: function(data){
			    gisDataCfg=eval(data.gisDataCfg);
			    if(gisDataCfg != null){
			    	var htmlStr = "";
			    	var callBack = "";
				    var ulFirstallList = gisDataCfg.childrenList;
				    if (ulFirstallList.length > 0) {
					    for(var i=0; i<ulFirstallList.length; i++){
					    	callBack = ulFirstallList[i].callBack;
					    	if (callBack.indexOf("classificationClick") != -1) {
						    	htmlStr += "<li class=\""+ulFirstallList[i].className+"\" onclick=\""+ulFirstallList[i].callBack+"\" title=\""+ulFirstallList[i].menuName+"\"><p class=\"pic\"><img src=\""+uiDomain+ulFirstallList[i].largeIco+"\" /></p></li>"
					    	} else {
						    	htmlStr += "<li class=\""+ulFirstallList[i].className+"\" onclick=\""+ulFirstallList[i].callBack+"\" title=\""+ulFirstallList[i].menuName+"\"><p class=\"pic\"><img src=\""+uiDomain+ulFirstallList[i].largeIco+"\" /></p><p class=\"word AlphaBack\">"+ulFirstallList[i].menuName+"</p></li>"
					    	}
					    	if(ulFirstallList[i].menuCode=='thing'){
					    		gdcId = ulFirstallList[i].gdcId;
					    		if(gdcId != null){
							    	getMenuInfo(gdcId);
							    }
					    	}
					    }
					    var ulFirstall = document.getElementById("ulFirstall");
				    	ulFirstall.innerHTML = "<div class=\"h_20\"></div>"+htmlStr;
				    } else {
				    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
				    }
			    }
			 }
		 });
	} else {
		$("#divTreeMenu").addClass("TreeMenu").css("height", $(window).height() - 62);
		$("#ulFirstall").addClass("ztree");
		var setting = {
			edit: {
				drag: {isCopy: true, isMove: false},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			check: {
				enable: true,
				chkStyle: "checkbox",
				chkboxType: { "Y": "ps", "N": "ps" }
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			view: {
				fontCss: function(treeId, treeNode) {
					return {color:"#FFFFFF", "font-size":"14px"};
				}
			},
			callback: {
				onCheck: zTreeOnCheck,
				onClick: treeNodeClick
			}
	   	};
		
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgTree.json?orgCode="+orgCode+"&homePageType="+homePageType+"&t="+Math.random(),
			error : function() {
			},
			success: function(data){
				if (data != null && data.length > 1) {
					$.fn.zTree.init($("#ulFirstall"), setting, data);
					var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
					var nodes = zTree.transformToArray(zTree.getNodes());
					for (var i = 0; i < nodes.length; i++) {
						var id = nodes[i].id;// 父节点
						for (var j = 0; j < nodes.length; j++) {
							if (id == nodes[j].pId) {// 子节点
								node = zTree.getNodeByParam("id", id, null);
								var menuCode = analysisOfElementsCollection(nodes[j].elementsCollectionStr, "menuCode");
								if (menuCode == defCheckObj.menuCode) {
									defCheckObj.nodes.push(nodes[j]);
									zTree.expandNode(node, true, false, false, false);
								}
								var bCheck = true;
								node.icon = null;
								node.nocheck = bCheck;
								zTree.updateNode(node);
							}
						}
					}
					$(".ico_close").remove();
					$(".ico_open").remove();
					checkNodes();
				} else {
					$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
				}
			}
		});
	}
}

var mapMenu = new HashMap();
function getMenuInfo(gdcId){
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	var displayStyle = '0'; // 平铺
	$.ajax({ 
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 300000,
		 data: { orgCode:orgCode,homePageType:homePageType,gdcId:gdcId,isRootSearch:0},
		 dataType:"json",
		 async: true,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
			var gisDataCfg=eval(data.gisDataCfg);
		    var ulFirstallList = gisDataCfg.childrenList;
		    if (ulFirstallList.length > 0) {
		    	for(var i=0; i<ulFirstallList.length; i++){
		    		//console.log('---'+ulFirstallList[i].menuCode);
		    		if(ulFirstallList[i].menuCode == 'hitFace'){//人脸
		    			mapMenu.put("hitFace",ulFirstallList[i].elementsCollectionStr);
		    		}
		    	}
		    }
		 }
	 });
}

function treeNodeClick(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.cancelSelectedNode(treeNode);
}

function zTreeOnCheck(event, treeId, treeNode) {
	if (treeNode.children != null && treeNode.children.length > 0) {
		for (var n = 0; n < treeNode.children.length; n++) {
			var node = treeNode.children[n];
			zTreeOnCheck(event, treeId, node);
		}
	} else if (treeNode.callBack != null) {
		var _callBack = treeNode.callBack.replace('showObjectList', 'showObjectListForTree').replace(')', ',' + treeNode.checked + ')');
		eval(_callBack);
	}
}

function clearMyLayerB() {
	clearMyLayer();
	clearMyLayerA();
}

function clearMyLayerA() {
	if (displayStyle == '1') {
		var zTree = $.fn.zTree.getZTreeObj("ulFirstall");
		zTree.checkAllNodes(false);
		rentRoomTip(false);
	}
	if (typeof window.frames['get_grid_name_frme'].clearMyLayerB == "function") {
		window.frames['get_grid_name_frme'].clearMyLayerB();
	}
}

var defCheckObj = {
	menuCode : "${menuCode!''}",
	index : 0,
	nodes : []
};

function checkNodes() {
	if (defCheckObj.menuCode != '') {
		defCheckObj.index++;
		if (defCheckObj.index == 2 && defCheckObj.nodes.length > 0) {
			var treeObj = $.fn.zTree.getZTreeObj("ulFirstall");
			for (var i = 0, l = defCheckObj.nodes.length; i < l; i++) {
				treeObj.checkNode(defCheckObj.nodes[i], true, false, true);
			}
		}
	}
}

var winType = "";//用于判断是否关闭详细窗口
var mapObjectName = "";//用于确定刷新的列表
function flashData(){
	if(winType!="" && winType=='0'){//关闭详细窗口
		closeMaxJqueryWindow();
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

	var isEditFlag;
	function locationTagging(gridId,x,y,mapt,isEdit){
		var callBackUrl = 'http://${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml'
		var width = 480;
		var height = 380;
		var mapType = 'EVENT_V1';
		isEditFlag = isEdit;
		initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType)
	}
	function mapMarkerSelectorCallback(mapt, x, y){//将标注信息传递到新增、编辑页面
		var childIframeContents = $("#MaxJqueryWindowContent").find("iframe").contents();
		childIframeContents.find("#mapt").val(mapt);
		childIframeContents.find("#x").val(x);
		childIframeContents.find("#y").val(y);
		var showName = "修改地理位置";
		if(isEditFlag == false) {
			showName = "查看地理位置";
		}
		childIframeContents.find("#mapTab").html(showName);
		childIframeContents.find(".mapTab").addClass("mapTab2");
		closeMaxJqueryWindowForCross();
		
	}
	
	function rentRoomTip(isShow) {
		if (isShow) $("#rentRoomTip").show();
		else $("#rentRoomTip").hide();
	}
	
	function showCall(bCall, userName, userImg) {
		layer.open({
			type: 2,
			title: '语音盒呼叫',
			shadeClose: true,
			shade: 0.5,
			area: ['314px', '410px'],
			content: '${rc.getContextPath()}/zhsq/map/arcgis/voiceInterface/go.jhtml?bCall='+bCall+'&userName='+userName+'&userImg='+userImg
		});
	} 
var userName='${userName!}@mmp',ocxId="MMPLogicOcx",previewId="MMPPreviewWndOcx",svrip="${svrip!}",mediaurl="${mediaurl!}",max=4;	

$(window).load(function() {

	if('${isUserMmp!}'!='' && '${isUserMmp!}'=='true'){//是否有开启视频通话
	    initMmpSelector();
	    if(checkActiveX()){
			setTimeout('loginOcx()',300); 
		}
	}
	 
});

function loginOcx(){
	 initOcx(userName,ocxId,previewId,max);
	 var ret=LoginMMP(svrip,mediaurl,userName);
	 if (ret == 0) {
		   //document.getElementById("output").value = "login succ";   
	 }
	 else {
	      //document.getElementById("output").value = "login failed";
	 }

}


</script>
</html>
