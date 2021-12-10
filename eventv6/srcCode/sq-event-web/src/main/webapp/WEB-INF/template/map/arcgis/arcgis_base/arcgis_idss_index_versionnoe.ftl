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
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function_idss.js"></script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<#include "/component/ImageView.ftl" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<style>
	.controller{ position:absolute; left:50%; margin-left:-250px; bottom:0;background:url(${rc.getContextPath()}/images/fk_bg1.png) no-repeat; width:545px; height:48px;}
	.main-bg{ position:absolute; left:45px; top:-38px;}
	.main-bg ul{ list-style:none; padding:0; margin:0;}
	.main-bg ul li{ float:left; margin:0 0px 0 0; width:76px;text-align:center; color:#fff;}
	.main-bg ul li i{ background:#00b2f2; border-radius:50px; width:55px; height:55px; line-height:55px; display:block; margin:4px auto;cursor:pointer;}
	.main-bg ul li i.fk_current{ background:#ff6600;}
	.main-bg ul li i img{ margin-top:11px;}
	.main-bg ul li a{ color:#fff; text-decoration:none;}
	
	.search-box{ position:relative; left:68px; top:10px; background:#fff; width:300px; height:40px;}
	.ui-dropdown{ position:absolute; left:0; top:0; color:#666;}
	.ui-dropdown-hd{ cursor:pointer;display:inline-block; width:75px;padding: 10px 10px 9px 15px;line-height: 21px;height: 21px; color:#666;}
	.ui-dropdown-hd .iconfont{ background:url(${rc.getContextPath()}/images/fk_icon1.png) no-repeat; width:7px; height:4px; display:inline-block; margin-left:7px; vertical-align:middle;}
	.ui-dropdown ul{ background:#fff; width:100px; width:300px;}
	.ui-dropdown ul li{ padding:6px 0 6px 15px;float:left;width:85px;cursor:pointer;height:33px;}
	.ui-dropdown ul li div{ width:30px;height:30px;margin-right:3px;float:left;}
	.ui-dropdown ul li a{ color:#666; text-decoration:none;line-height:32px;}
	.ui-dropdown ul li:hover{ background:#f7f7f7;}
	.search-query{ width:238px; display:inline-block; height:40px; border:none; line-height:40px;padding: 0px 10px 0px 3px; color:#666;}
	.ui-btn{ background:#3385ff url(${rc.getContextPath()}/images/fk_icon2.png) center center no-repeat; position:absolute; right:0; top:0;display:inline-block; border:none; height:40px; line-height:40px; width:60px; cursor:pointer;}
	.input-clear{ cursor:pointer;position:absolute; right:68px; top:11px; background:url(${rc.getContextPath()}/images/fk_icon3.gif) no-repeat; width:17px; height:17px;}
	.bukong{margin-right:10px; margin-top:5px; width:80px;}
	.bukong a{display:block; width:80px; height:22px; background:#1eca1e; color:#fff; text-align:center; line-height:22px;}
	.bukong a:hover{background:#43e443; color:#fff; text-decoration:none;}
	
	.ztree li span{font-size:14px;}
	
	.zt a{display:block; width:80px; height:22px; background:#4489ca; color:#fff; text-align:center; line-height:22px;}
	.zt{margin-right:10px; margin-top:5px; width:80px;}
	
	.bukong-appply-list {
		position: absolute;
		width:80px;
		background-color: #fff;
		z-index: 3;
	}
	.bukong-appply-list ul li{cursor: pointer; text-align: center; border-bottom: 1px dotted; height: 28px; line-height: 28px;}
	.bukong-appply-list ul li:hover{background-color: #81EA81;}
	.bukong-selected{background-color: #FFEBBF;}
	
	.mapbox{ cursor: pointer;background:#fff; border:2px solid #2ab7b2; padding:5px; position:relative; min-width:30px; display:block; white-space:nowrap;}
	.mapicon{ display:inline-block;margin-right:3px;}
	.maparrow{ background:url(${uiDomain!''}/images/map_tree_arrow.png) no-repeat; width:9px; height:6px; position:absolute; left:10px; bottom:-8px;}
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
	    	<li class="ClearMap" onclick="clearMyLayerA();"></li>
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
  			<input type="hidden" name="selectType" id="selectType" value="" />
  			<input type="hidden" name="gdcId" id="gdcId" value="" />
  		</form> 
  		
  		<!-----------------------------------重点人员列表信息------------------------------------->
    <div class="NorMapOpenDiv2 kuangxuanWindow listWindow hide dest" style="bottom:100px; right:330px; z-index:11;">
		<div class="box" style="width:376px;height:343px; z-index:11;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeListDetail()"></span>重点人员列表</div>
	        <iframe id="listDetail" name="listDetail" width="100%" height="92%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	</div>
	<!-----------------------------------异常车辆列表信息------------------------------------->
    <div class="NorMapOpenDiv2 kuangxuanWindow listCarWindow hide dest" style="bottom:100px; right:330px; z-index:11;">
		<div class="box" style="width:425px;height:343px; z-index:11;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeCarListDetail()"></span>异常出入车辆列表</div>
	        <iframe id="listCarDetail" name="listCarDetail" width="100%" height="92%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	</div>
  		
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
    	<div class="zt fr"><a style="text-decoration:none;" class="bukong-a" href="javascript:showStandard();">专题图层</a></div>
    	<div id="bukongMapBar" class="bukong fr">
    		<a class="bukong-a" href="#" style="padding-left: 3px;">
    			<span id="bukongMapBarName">布控任务</span>
    			<span>
    				<img style="vertical-align:middle;" src="${uiDomain!''}/images/map/gisv0/special_config/images/xiala.png">
    			</span>
    		</a>
    		<div id="controlApplyList" class="clear bukong-appply-list hide">
				<ul>
					<li class="bukong-selected" onclick="showIdss(this, 'get_idss');">布控任务</li>
					<li onclick="showIdss(this, 'get_idss_target');">布控黑名单</li>
				</ul>
			</div>
    	</div>
    </div>
    <!-----------------------------------人地事物情------------------------------------->
    <div class="ztIcon AlphaBack dest titlefirstall" style="top:32px;z-index:2;display:block;">
		<div class="title">
			<span class="fr" onclick="CloseX()">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" />
			</span>
			<div>专题图层</div>
		</div>
	</div>
	<div class="ztIcon AlphaBack dest firstall" style="z-index:2;display:block;">
		<div id="busiTreeMenu">
			<ul id="busiTreeUL"></ul>
		</div>
        <div class="clear"></div>
	</div>
	
	<!-----------------------------------布控任务------------------------------------->
    <div class="ztIcon AlphaBack dest titleIdss" style="top:32px;display:none;">
		<div class="title">
			<span class="fr" onclick="CloseX()">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" />
			</span>
			<span id="searchBtnId" class="fr" style="" onclick="ShowSearchBtn()">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" />
			</span>
			<div id="titlePath" name="titlePath">布控任务</div>
			
		</div>
	</div>
	<div class="ztIcon AlphaBack dest Idssall" style="">
		<div id="divTreeMenu">
			<ul id="ulFirstall"></ul>
		</div>
        <div class="clear"></div>
	</div>

	<!-- <div id="controller" class="controller">
	  <div class="main-bg">
	    <ul>
	      <li><i id="accessControl"><img src="${rc.getContextPath()}/images/fp_icon1.png" width="30" height="30" /></i><a href="#">门禁设备</a></li>
	      <li><i id="visitorMachine"><img src="${rc.getContextPath()}/images/fp_icon2.png" width="30" height="30" /></i><a href="#">访客机</a></li>
	      <li><i id="accessBrake"><img src="${rc.getContextPath()}/images/fp_icon3.png" width="30" height="30" /></i><a href="#">车闸设备</a></li>
	      <li><i id="accessAlarm"><img src="${rc.getContextPath()}/images/fp_icon4.png" width="30" height="30" /></i><a href="#">报警机</a></li>
	      <li><i id="videoCamera"><img src="${rc.getContextPath()}/images/fp_icon5.png" width="30" height="30" /></i><a href="#">视频摄像头</a></li>
	      <li><i id="bayonetEquipment"><img src="${rc.getContextPath()}/images/fp_icon6.png" width="30" height="30" /></i><a href="#">卡口设备</a></li>
	    </ul>
	  </div>
	</div> -->
	
	<div class="search-box" style="border:1px solid #7d7d7d;">
		<div>
	     <div id="s-search" class="ui-dropdown">
	       <div id="dropdown" class="dst kw" style="border:1px solid #7d7d7d;z-index:111111;position:absolute; left:-1px; top:40px; background:#fff; width:300px; height:90px; display:none;">
			<div class="ui-dropdown">
				<ul>
			         <li>
			         	<div style="background-color:#0FCF00;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/menu_accessControl.png" /></div>
			         	<a id="accessControl" href="#">门禁设备</a>
			         </li>
			         <li>
			         	<div style="background-color:#458B74;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/visitorMachine.png" /></div>
			         	<a id="visitorMachine" href="#">访客机</a>
			         </li>
			         <li>
			         	<div style="background-color:#CD7054;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/accessBrake.png" /></div>
			         	<a id="accessBrake" href="#">车闸设备</a>
			         </li>
			         <li>
			         	<div style="background-color:#FF0000;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/menu_accessAlarm.png" /></div>
			         	<a id="accessAlarm" href="#">报警机</a>
			         </li>
			         <li style="width:93px;">
			         	<div style="background-color:#6CA6CD;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/menu_videoCamera.png" /></div>
			         	<a id="videoCamera" href="#">视频摄像头</a>
			         </li>
			         <li style="padding-left:7px;">
			         	<div style="background-color:#8B0000;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/menu_bayonetEquipment.png" /></div>
			         	<a id="bayonetEquipment" href="#">卡口设备</a>
			         </li>
			       </ul>
			</div>
			</div>
	     </div>
	     <input name="keyWord" id="keyWord" type="text" class="search-query kw" />
	     <div id="input-clear" onclick="resetCondition()" class="input-clear" style="display:none;"></div>
	     <button class="ui-btn" id="ui-btn" onclick="search('all')"></button>
	     </div><!-- left:68px; top:82px; -->
	</div>
	
	  <div id="get_grid_name" class="NorList AlphaBack" style="left:68px;top:87px;width:300px;max-height:700px;">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	  </div>
	  <div id="idssControlApplyDiv">
		  <div id="get_idss" class="NorList AlphaBack dest" style="max-height:700px;">
			<iframe id="get_idss_frme" name="get_idss_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
		  </div>
		  <div id="get_idss_target" class="NorList AlphaBack dest" style="max-height:700px;">
			<iframe id="get_idss_target_frame" name="get_idss_target_frame" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
		  </div>
	  </div>
</div>

<div id="dialog" title="三维图展示">
    <iframe id="lc_skylineview_frme" name="lc_skylineview_frme" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/videoPlayWindow.ftl" />
<#include "/component/customEasyWin.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/message/msg.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/xdate.js"></script>
<script type="text/javascript">
var divDrag = {
	kxWinObj: null,
	zbWinObj: null
};

function CloseX(){
	$(".dest").hide();
}

var test1="tets";
 $(function(){

 	modleopen();
 	
 	$("#get_grid_name").css("height",$(document).height()-155);
 	$("#jsSlider").css("left",$(document).width()-100);
 	$(".controller").css("top",$(document).height()-50);
 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
 	
	var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
 	document.getElementById("gridLevelName"+level).checked = true;
 	getArcgisInfo();
 	
 	changeCheckedAndStatus($("#gridLevel").val(),level);
 	locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
 	modleclose();
	getBusiHomeMenuInfo();
 	getLayerMenuInfo();
	
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
	
	$("#dropdown li").click(function(){
		$("#dropdown").hide();
		$("#selectType").val($(this).children("a").attr("id"));
		$("#ui-btn").click();
	});
	$("#keyWord").click(function(){
		$("#dropdown").show();
		$("#input-clear").show();
	});
	$("#s-search").hover(function() {
	}, function() {
		$("#dropdown").hide();
	});
	$("#keyWord").keyup(function(){
		if($(this).val() != ''){
			$("#input-clear").show();
			$("#dropdown").hide();
		}else{
			$("#dropdown").show();
			$("#input-clear").hide();
		}
	});
	
	if($("#get_grid_name").is(":hidden")){
		$("#ui-btn").toggle(function(){
			if($("#get_grid_name").is(":hidden")){
				$("#get_grid_name").animate({height: 'toggle', opacity: 'toggle'}, "slow");
			}
		},function(){
			if($("#get_grid_name").is(":hidden")){
				$("#get_grid_name").animate({height: 'toggle', opacity: 'toggle'}, "slow");
			}
		});
	}
	
	$("#bukongMapBar").hover(function() {
		$("#controlApplyList").show();
	}, function() {
		$("#controlApplyList").hide();
	});
});

 	function showStandard(){
 		if($(".titlefirstall").is(":hidden")){
 	 		$(".titleIdss").hide();
 			$(".Idss").hide();
 			$("#idssControlApplyDiv > div").hide();
 			//$("#get_grid_name_frme").hide();
 			$(".titlefirstall").show();
 			$(".firstall").show();
 		}else{
 			$(".titlefirstall").hide();
 			$(".firstall").hide();
 		}
// 		$(".bukong a").html('专题图层');
 	}
 	
 	function showIdss(liObj, iframeDivId, isHideAll){
 		var iframeObj = $("#" + iframeDivId + " > iframe"),
 			isNotSameIframe = !$(liObj).hasClass("bukong-selected"),
 			isTitleHidden = $(".titleIdss").is(":hidden");
 		
 		if(isTitleHidden) {
 			$(".titleIdss").show();
 			$(".Idss").show();
 			
 			$(".titlefirstall").hide();
 			$(".firstall").hide();
 		} else if(isHideAll) {
			$(".titleIdss").hide();
			$(".Idss").hide();
 		}
 		
 		$("#idssControlApplyDiv > div").hide();
 		
 		if(!isHideAll || isTitleHidden) {
			$("#" + iframeDivId).show();
			
			if(isNotSameIframe) {
				iframeObj.attr('src', iframeObj.attr('_targetSrc'));
			}
		}
		
		$(liObj).siblings().removeClass("bukong-selected");
		$(liObj).addClass("bukong-selected");
		
		$("#bukongMapBarName").html($(liObj).html());
		
		$("#controlApplyList").hide();
 	}
 
	function resetCondition(){
		$("#keyWord").val('');
		$("#input-clear").hide();
		$("#get_grid_name").hide();
		$("#dropdown").hide();
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
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	var displayStyle = '0'; // 平铺
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getDisplayStyle.json?t='+Math.random(),
		type: 'POST',
		timeout: 300000,
		data: { orgCode:orgCode,homePageType:homePageType},
		dataType:"json",
		async: false,
		error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
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
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
			 },
			 success: function(data){
			    gisDataCfg=eval(data.gisDataCfg);
			    if(gisDataCfg != null){
			    	var htmlStr = "";
			    	var callBack = "";
			    	var elementsCollectionStr = "";
				    var ulFirstallList = gisDataCfg.childrenList;
				    if (ulFirstallList.length > 0) {
				    	var gdcId = null;
				    	var idssGdcId = null;
					    for(var i=0; i<ulFirstallList.length; i++){
				    		if(ulFirstallList[i].menuCode == 'thing'){
				    			gdcId = ulFirstallList[i].gdcId;
				    		}
				    		if(ulFirstallList[i].menuCode == 'situation'){
				    			idssGdcId = ulFirstallList[i].gdcId;
				    		}
					    }
// 					    console.log('--'+gdcId+'--'+idssGdcId);
					    if(gdcId != null && idssGdcId != null){
					    	getMenuInfo(gdcId,idssGdcId);
					    }else{
					    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
					    }
				    } else {
				    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
				    }
			    }
			 }
		 });
	}
}

function getBusiHomeMenuInfo() {
	var orgCode = $("#orgCode").val();
	var homePageType = "SPGIS_BUSI_HOME";
	var displayStyle = '0'; // 平铺
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getDisplayStyle.json?t='+Math.random(),
		type: 'POST',
		timeout: 300000,
		data: { orgCode:orgCode, homePageType:homePageType},
		dataType:"json",
		error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		},
		success: function(data){
			if (data == '1') { // 树形展示
				displayStyle = data;
			}
			if (displayStyle == '1') {
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
							$("#busiTreeMenu").addClass("TreeMenu");
							$("#busiTreeMenu").css("height", document.getElementById('map'+currentN).offsetHeight-62);
							$("#busiTreeUL").addClass("ztree");
							$.fn.zTree.init($("#busiTreeUL"), setting, data);
							var zTree = $.fn.zTree.getZTreeObj("busiTreeUL");
							var nodes = zTree.transformToArray(zTree.getNodes());
							for (var i = 0; i < nodes.length; i++) {
								var id = nodes[i].id;
								for (var j = 0; j < nodes.length; j++) {
									if (id == nodes[j].pId) {
										node = zTree.getNodeByParam("id", id, null);
										node.nocheck = true;
										zTree.updateNode(node);
									}
								}
							}
							$(".ico_close").remove();
							$(".ico_open").remove();
						} else {
							$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
						}
					}
				});
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

function clearMyLayerA() {
	clearMyLayer();
	var zTree = $.fn.zTree.getZTreeObj("busiTreeUL");
	zTree.checkAllNodes(false);
}

var mapMenu = new HashMap();

function getIdssInfo(idssGdcId){
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	var displayStyle = '0'; // 平铺
	$.ajax({ 
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 300000,
		 data: { orgCode:orgCode,homePageType:homePageType,gdcId:idssGdcId,isRootSearch:0},
		 dataType:"json",
		 async: true,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		    gisDataCfg=eval(data.gisDataCfg);
		    if(gisDataCfg != null){
		    	var htmlStr = "";
		    	var callBack = "";
		    	var elementsCollectionStr = "";
			    var ulFirstallList = gisDataCfg.childrenList;
			    if (ulFirstallList.length > 0) {
			    	for(var i=0; i<ulFirstallList.length; i++){
			    		if(ulFirstallList[i].menuCode == 'idssControl'){//布控任务
			    			mapMenu.put("idssControl",ulFirstallList[i].callBack);
			    			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/toArcgisDataListOfTargetRef.jhtml?infoOrgCode=${orgCode!''}&gridId=${gridId?c}";
			    			url = url + "&elementsCollectionStr=" + ulFirstallList[i].elementsCollectionStr;
			    			$('#get_idss').css("display","none");
			    			$('#get_idss_target').css("display","none");
			    			
			    			$('#get_idss_frme').attr("src",url);
			    			$('#get_idss_frme').attr("_targetSrc",url);
			    			
			    			var targetUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/toArcgisDataListOfTargetRef.jhtml?infoOrgCode=${orgCode!''}&gridId=${gridId?c}&pattern=0"
			    			 				+ "&elementsCollectionStr=" + ulFirstallList[i].elementsCollectionStr;
			    			$('#get_idss_target_frame').attr("_targetSrc", targetUrl);
			    		}
			    	}
			    } else {
			    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
			    }
		    }
		 }
	 });
}

function getMenuInfo(gdcId,idssGdcId){
	$("#gdcId").val(gdcId);
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
		    gisDataCfg=eval(data.gisDataCfg);
		    if(gisDataCfg != null){
		    	var htmlStr = "";
		    	var callBack = "";
		    	var elementsCollectionStr = "";
			    var ulFirstallList = gisDataCfg.childrenList;
			    if (ulFirstallList.length > 0) {
			    	for(var i=0; i<ulFirstallList.length; i++){
			    		if(ulFirstallList[i].menuCode == 'accessControl'){//门禁设备
			    			mapMenu.put("accessControl",ulFirstallList[i].elementsCollectionStr);
			    		}else if(ulFirstallList[i].menuCode == 'accessBrake'){//车闸设备
			    			mapMenu.put("accessBrake",ulFirstallList[i].elementsCollectionStr);
			    		}else if(ulFirstallList[i].menuCode == 'bayonetEquipment'){//卡口设备
			    			mapMenu.put("bayonetEquipment",ulFirstallList[i].elementsCollectionStr);
			    		}else if(ulFirstallList[i].menuCode == 'accessAlarm'){//报警机
			    			mapMenu.put("accessAlarm",ulFirstallList[i].elementsCollectionStr);
			    		}else if(ulFirstallList[i].menuCode == 'visitorMachine'){//访客机
			    			mapMenu.put("visitorMachine",ulFirstallList[i].elementsCollectionStr);
			    		}else if(ulFirstallList[i].menuCode == 'videoCamera'){//视频设备
			    			mapMenu.put("videoCamera",ulFirstallList[i].elementsCollectionStr);
			    		}
			    	}
			    } else {
			    	$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
			    }
		    }
		    getIdssInfo(idssGdcId);
		 }
	 });
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
		initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType);
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

	//图片查看器回调
	function ffcs_viewImg(fieldId){
		var sourceId = fieldId + "_Div";
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	}
	
	function gisPosition(eqpType){
		layer.load(0);
		if("1" != IS_ACCUMULATION_LAYER) {
			clearSpecialLayer(eqpType + 'Layer');
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getLocateDataListOfEquipment.jhtml?eqpType="+eqpType;
		currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+mapMenu.get(eqpType)+"')";
		getArcgisDataOfZhuanTi(url,mapMenu.get(eqpType),340,195);
	}
	
	function search(eqpType){
		$("#dropdown").hide();
		$("#input-clear").show();
		var infoOrgCode = $("#orgCode").val();
		
		var keyWord = $("#keyWord").val();
// 		$('#get_grid_name').css("display","block");
		if($("#selectType").val() != ''){
			eqpType = $("#selectType").val();
		}
		var eqpTypeCode = "";
		if(eqpType=="accessControl"){
			eqpTypeCode = "001";
		}else if(eqpType=="accessBrake"){
			eqpTypeCode = "003";
		}else if(eqpType=="bayonetEquipment"){
			eqpTypeCode = "006";
		}else if(eqpType=="accessAlarm"){
			eqpTypeCode = "004";
		}else if(eqpType=="visitorMachine"){
			eqpTypeCode = "002";
		}else if(eqpType=="videoCamera"){
			eqpTypeCode = "005";
		}else if(eqpType=="all"){
			eqpTypeCode = "";
		}
		$("#selectType").val("");
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toDataListOfEquipment.jhtml?keyWord="+keyWord+"&eqpType="+eqpTypeCode+"&infoOrgCode="+infoOrgCode+"&gridId=${gridId?c}";
		url = url + "&gdcId=" + $("#gdcId").val();
		var elementsCollectionStr = mapMenu.get(eqpType);
		$('#get_grid_name_frme').attr("src",url);
	}
	
	function dropdownOut(){
		$('#dropdown').hide();
	}

	function closeCarListDetail(){
		$(".listCarWindow").addClass("dest");
		$(".listCarWindow").toggle();
	}
	
	function closeListDetail() {
		$(".listWindow").addClass("dest");
		$(".listWindow").toggle();
	}
</script>
</html>
