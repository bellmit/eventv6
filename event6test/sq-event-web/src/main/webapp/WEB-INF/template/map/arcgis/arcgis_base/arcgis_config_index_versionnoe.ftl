<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title>
	<script type="text/javascript" src="${rc.getContextPath()}/js/globalPlayVue/vue.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/globalPlayVue/iview.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/globalPlayVue/encrypt.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/globalPlayVue/index.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/globalPlayVue/initPlay.js"></script>
	<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<#include "/component/ImageView.ftl" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/zhoubian_index.js"></script>
    <link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
	<script src="${ANOLE_COMPONENT_URL}/js/components/popWindow/jquery.anole.popWindow.js"></script>
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/migration_map.css">
    <link href="${rc.getContextPath()}/css/kuangxuan.css" rel="stylesheet" type="text/css" />
    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
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
		.maparrow_red{ background:url(${rc.getContextPath()}/images/map_tree_arrow_red.png) no-repeat; width:9px; height:6px; position:absolute; left:10px; bottom:-8px;}
		.message-div{color:black;font-size:14px;padding-left:20px;padding-right:20px;padding-top:4px;}
		.message-div p {line-height:25px;}
		.message-div p a {text-decoration:none; color:#27a6f7; float:right; padding-top:10px;} 
		.layerSkin{ position:absolute;border-radius:8px; padding:0px 5px 10px 5px;background:rgba(30,64,89,0.75);}
		.mapTreeButton {position:absolute;}
		.MapTools ul :nth-child(3) {
			background: url(${uiDomain!''}/web-assets/extend/superMap/superMap_base/images/mapStyle/mapStyle_1.png) ;
		}
		.NightMode {
			background: url(${uiDomain!''}/web-assets/extend/superMap/superMap_base/images/mapStyle/mapStyle_2.png)!important;
		}
	</style>
</head>
<body style="width:100%;height:100%;border:none;" >
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
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
        <div class="sjmenu-yjcd" style="display: none;width:200px;">
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
		<#if gridCode?? && gridCode?starts_with("500151")><!-- 铜梁 -->
		<li  id="mapModeLi" onclick="modeClick();"></li>
		</#if>
    </ul>
	<div id="layerTileStyleEX" >
	</div>
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
  			<input type="hidden" name="isNanAn" id="isNanAn" value="${(NAN_AN)}" />
  		</form>
    <div id="heatMapDiv" class="timelinemain Migration_AlphaBack" style="display:none;">
        <div id="heatMapDiv-close" class="timelineicon" title="关闭" onclick="closeHeatMapLayer();"></div>
        <div id="spectialPopulationDiv" style="text-align: left; color:white; font-size: 15px; padding-bottom: 8px; margin-top: 8px; display: none;">
			<input name="spectialPopulation" type="checkbox" checked="checked" value="heresy"/><span style="cursor: pointer;">邪教人员</span>&nbsp;
            <input name="spectialPopulation" type="checkbox" checked="checked" value="drugs"/><span style="cursor: pointer;">吸毒人员</span>&nbsp;
            <input name="spectialPopulation" type="checkbox" checked="checked" value="camps"/><span style="cursor: pointer;">刑释人员</span>&nbsp;
            <input name="spectialPopulation" type="checkbox" checked="checked" value="rectify"/><span style="cursor: pointer;">矫正人员</span>&nbsp;
            <br/>
            <input name="spectialPopulation" type="checkbox" checked="checked" value="petition"/><span style="cursor: pointer;">上访人员</span>&nbsp;
            <input name="spectialPopulation" type="checkbox" checked="checked" value="dangerous"/><span style="cursor: pointer;">危险品从业人员</span>&nbsp;
            <input name="spectialPopulation" type="checkbox" checked="checked" value="neuropathy"/><span style="cursor: pointer;">精神病障碍患者</span>&nbsp;
        </div>
    </div>
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

<!-- 事件热力图框选-->
<div class="wrap_media_con" id="wrap_media" style="position:absolute;left:300px;z-index:9;display:none;top:50px;">
	<input type="hidden" id="media_time_begin">
	<input type="hidden" id="media_time_end">
    		<div class="wrap_media_cont">
    			<ul class="media_time media_time_top mt8" style="padding-top:6px;">
    				<li class="active"><a href="javascript:eventMeasure('showHeat',0);" id="media_time_0"></a></li>
    				<li><a href="javascript:eventMeasure('showHeat',2);"  id="media_time_2"></a></li>
    				<li><a href="javascript:eventMeasure('showHeat',4);"  id="media_time_4"></a></li>
	    		</ul>
	    		<ul class="media_list" id="media_list">
		    		<li class="active" onclick="eventMeasure('showHeat',0)"></li>
		    		<li onclick="eventMeasure('showHeat',1)"></li>
		    		<li onclick="eventMeasure('showHeat',2)"></li>
		    		<li onclick="eventMeasure('showHeat',3)"></li>
		    		<li onclick="eventMeasure('showHeat',4)"></li>
		    	</ul>
    			<ul class="media_time media_time_bottom pd50">
    				<li><a href="javascript:eventMeasure('showHeat',1);" id="media_time_1"></a></li>
    				<li><a href="javascript:eventMeasure('showHeat',3);"  id="media_time_3"></a></li>
	    		</ul>
    		</div>
    		<div class="wrap_play">
    			<div class="wrap_play_box" id="play_box">
	    			<a href="javascript:eventMeasure('play',this);" class="play_btn"></a>
    		</div>
    	</div>
    	<div class="wrap_media_btn">
    		<a href="javascript:eventMeasure();">框选统计</a>
    	</div>
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
<!-- Mediasoup视频消息组件 -->	
  <#if isUseMediasoup??  && isUseMediasoup!="false" && isUseMediasoup!="0"> 
	<script src="${uiDomain!''}/web-assets/common/js/mpush4mediasoup.js "></script>	
	<script type="text/javascript">
	 var  wsUserId='${userId!}';//当前登录用户id，作为消息用户主键
	  mpush.init({//初始化
	   wsUrl:'${wsUrl!}',//消息地址
	   mediaUrl:'${mediaUrl!}',//视频地址
	   wsUserId:wsUserId,//当前用户id
	   displayName:'${partyName!}',//当前用户姓名
	   components_domain:'${COMPONENTS_URL}',//组件工程域名
	   uiDomain:'${uiDomain!}',//ui公共样式域名
	   isUseMediasoup:'${isUseMediasoup!'1'}'
	 });
     connect();  //连接服务器 
	</script>	
 </#if>
<script type="text/javascript">
var resourceDomain = "${RESOURCE_DOMAIN!}";
function gloybalPlay(channelId){
	$.blockUI({message: "控件加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'50%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
	try{
		if (DHWsInstance) {
			globaleyesLogin(channelId,resourceDomain);
		} else {
			$.unblockUI();
			$.messager.show({
	        	    title:'友情提示',
	        	    msg:'视频监控播放失败！请检查网络或者点击<a href="'+resourceDomain+'/monitorInstallSoft.rar" style="color:rgb(255, 0, 0) !important;">下载相应版本<a/>插件！注意：请使用360浏览器极速模式下查看监控视频。',
	        	    showType:'show',
	        		timeout:5000,
	        	    style:{
	        	    	baileft:200, // 与左边du界的距离zhidao
	        	    	top:200 // 与顶zhuan部的shu距离
	        	    }
	        	});
		}
	}catch(err){
		$.unblockUI();
		$.messager.show({
	        	    title:'友情提示',
	        	    msg:'视频监控播放失败！请检查网络或者点击<a href="'+resourceDomain+'/monitorInstallSoft.rar" style="color:rgb(255, 0, 0) !important;">下载相应版本<a/>插件！注意：请使用360浏览器极速模式下查看监控视频。',
	        	    showType:'show',
	        		timeout:5000,
	        	    style:{
	        	    	baileft:200, // 与左边du界的距离zhidao
	        	    	top:200 // 与顶zhuan部的shu距离
	        	    }
	        	});
	}
}



    var divDrag = {
	kxWinObj: null,
	zbWinObj: null
};
	var LUO_FANG = "<#if LUO_FANG??>${LUO_FANG}</#if>";
var test1="tets";
var heatLayers = {};
 $(function(){
     $("#spectialPopulationDiv").find("span").click(function() {
         var ck = $(this).prev();
         ck.attr("checked", !ck.attr("checked"));
         if (typeof heatLayers["spectialPopulation"] != "undefined") {
             heatLayers["spectialPopulation"].query();
         }
     });
     $("#spectialPopulationDiv").find("input").click(function() {
         if (typeof heatLayers["spectialPopulation"] != "undefined") {
             heatLayers["spectialPopulation"].query();
         }
     });

     window.focus();
     modleopen();
 	zhoubianListHide();
 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
 	setTimeout(function() {
 		var level = judgeNextOutline(parseInt($("#gridLevel").val()));
 		changeCheckedAndStatus($("#gridLevel").val(), level);

		eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");//--初始化工作流！
	 	getArcgisInfo(function() {// 地图初始化完毕
	 		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	 		if ($("#orgCode").val().indexOf("3501") == 0) {
	 			$("#HeatMapDiv").show();
	 		}
	 		//消息提示框
	 		$.initAnolePopWindow({
	 			voiceCall : function(partyName, verifyMobile) {
	 				showCall(verifyMobile, partyName);
	 			},
	 			voiceBox : function(partyName, verifyMobile) {
	 				showCall(verifyMobile, partyName);
	 			},
	 			clickCallback : function(msgId,mgsData){
	 			var viewLink=mgsData.message.viewLink;
	 			if(viewLink.indexOf('zhsq/map/arcgis/arcgisDataOfDeviceController/alertDeviceDetail')>0){
	 				 $('.messagecon').remove();//移除原有的弹出框
	 				window.parent.showMaxJqueryWindow("告警",'${rc.getContextPath()}'+viewLink+msgId,550,300);
	 			}
	 			  console.log(msgId);
	 			}
	 		});
	 		
	 		$(".nicescroll-rails").hide();
	 	});

	 	changeCheckedAndStatus($("#gridLevel").val(), level);
	 	
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
		if ($("#isNanAn").val()) {
			var value = document.getElementById("li"+level).innerText;
			$("#level").html(value);
			getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType, level, "no");
		} else {
			var glns = $("input[name='gridLevelName']");
			for(var i=0; i<glns.length; i++) {
				if(glns[i].value!=level){
					glns[i].checked = false;
				}
			}
			var value = document.getElementById("li"+level).innerText;
			$("#level").html(value);
			getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
		}
	}else {
		if(ARCGIS_DOCK_MODE == "1") {
			featureHide("gridLayer"+gridLyerNum);
		}else {
			$("#map"+currentN).ffcsMap.clear({layerName : "gridLayer" + ($("#isNanAn").val() ? level : "")});
		}
		
	}
}
function getArcgisDataByCurrentSet(){
	var glns = $("input[name='gridLevelName']");
	for(var i=0; i<glns.length; i++) {
		if (glns[i].checked == true) {
			var idval = $(glns[i]).attr("id");
			var l = parseInt(idval.substring("gridLevelName".length,idval.length));
			getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,l, ($("#isNanAn").val() ? "no" : undefined));
		}
	}
	
	if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType);
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
function modeClick(){
	if($('#mapModeLi').hasClass('NightMode')){
		$('#layerTileStyleEX').empty();
		$('#mapModeLi').removeClass('NightMode');
	}else{
		$('#layerTileStyleEX').append($("<style> .map .container .layersDiv .layerTile {filter: invert(93%) sepia(88%) grayscale(70%) saturate(31) hue-rotate(170deg) brightness(0.9)}</style>"));
		$('#mapModeLi').addClass('NightMode');
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
			 //铜梁
			 <#if gridCode?? && gridCode?starts_with("500151")>
			 $("#mapStyleDiv").css('bottom','58px');
			 </#if>
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
                            var menuCode = ulFirstallList[i].menuCode;
                            if (menuCode != '') {
                                var id = "ul" + menuCode.toUpperCase().substring(0, 1) + menuCode.substring(1, menuCode.length);
                                $(".MapBar").find("." + menuCode).remove();
                                var html = '<div class="ztIcon AlphaBack '+menuCode+'"><ul id="'+id+'"></ul><div class="clear"></div></div>';
                                $(".MapBar").append(html);
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

function clearMyLayerB(param) {
	clearMyLayer();
	clearMyLayerA();
	if(mapTreeDiv){
		$("#ImgMapTree").remove();
	}
	if(currLayerOpenIndex && param == undefined){
		layer.close(currLayerOpenIndex);
		$("#wrap_media").hide();getOrgBack();
			clearSpecialLayer('EVENT_STATISTICS');	
			currLayerOpenIndex = undefined;
			eventHeatMap.clearData();
			$("#NorMapOpenDiv").remove();
	}
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
//--定位
	function gisPosition(res){
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfDeviceController/getArcgisLocateDataListOfDeviceInfos.jhtml?ids="+res+"&markerType=7778&mapt=5&elementsCollectionStr=";
			
			//$.fn.ffcsMap.centerAt({x:x,y:y,zoom:-1});
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')"; 
			//window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),490,270);
	}
	
function getOrgBack(){
	var iframe = document.getElementById("layui-layer-iframe"+currLayerOpenIndex);
	if(!iframe){
		return;
	}
	ifWindow = iframe.contentWindow,
	COLOR =  $.fn.ffcsMap.getColor(), redColor = COLOR.fromString("rgb(255,0,0)"),SYMBOL =  $.fn.ffcsMap.getSymbol();
	var textGraphic = ifWindow.textGraphic,gridGraphics = ifWindow.gridGraphics;//
	var k=0;
	if(gridGraphics.length == 0){
		return;
	}
	if(gridGraphics[0].attributes){
		k=0;
	}else{
		k=textGraphic.length;
	}
	for(var i=0,l=textGraphic.length;i<l;i++,k++){//修改轮廓底色
			var d = gridGraphics[k].attributes.data;
			var pieDiv = document.getElementById("pie_"+d._oldData.infoOrgCode);
			if(pieDiv){
				$(pieDiv).parent().remove();
			}
			var lineC=[],areaC=[];
			for(var j=1;j<7;j+=2){
				lineC.push(parseInt("0x"+d.lineColor.slice(j,j+2)));        
				areaC.push(parseInt("0x"+d.areaColor.slice(j,j+2)));        
			}
			lineC = COLOR.fromString("rgba(" + lineC.join(",") + ",0.5)");
			areaC = COLOR.fromString("rgba(" + areaC.join(",") + ","+d.colorNum+")");
			gridGraphics[k].setSymbol(new SYMBOL.SimpleFillSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID,
						new SYMBOL.SimpleLineSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID, lineC, 2),areaC));
			textGraphic[i].symbol.setColor(redColor);
			textGraphic[i].setGeometry(textGraphic[i].geometry.offset(0,0));
	}
}
var currLayerOpenIndex,eventHeatMap;;
function showStatisticsMsg(elementsCollectionStr) {
    var winHeight=document.getElementById('map'+currentN).offsetHeight-100;
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var menuListUrl = eclist["menuListUrl"];
    var menuName = eclist["menuName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
    var menuSummaryHeight = eclist["menuSummaryHeight"];
    var menuCode = eclist[ "menuCode"];
    var menuLayerName = eclist["menuLayerName"];
    var offset = '10px';
    if(menuCode == 'EVENT_STATISTICS'){
    	offset = 'rt';
		$.fn.ffcsMap.getMap().getLayer("gridLayer").onClick=function(){};
	}
    if(menuListUrl.indexOf('http://')<0){
        menuListUrl = js_ctx + menuListUrl;
    }
    if(menuListUrl.indexOf("?")<=0){
        menuListUrl += "?";
    }
	var gridId = $("#gridId").val();
	menuListUrl += "&gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&orgCode="+$("#orgCode").val()+"&infoOrgCode="+$("#orgCode").val()+"&elementsCollectionStr="+encodeURIComponent(elementsCollectionStr);
//    if(menuListUrl.indexOf("?")<=0){
//        menuListUrl += "?t="+Math.random();
//    }
//    menuListUrl += "&gridId="+$("#gridId").val()+"&noneGetLayer=true";

	if(typeof currLayerOpenIndex != 'undefined' && currLayerOpenIndex != null){
        layer.close(currLayerOpenIndex);
		
	}
    currLayerOpenIndex = layer.open({
		id: 'statisticsMsg'+menuCode,
		closeBtn:'1',
        type: 2,
		title: [menuName, 'background-color: rgba(19,55,81,0); border-bottom : 0px solid rgba(19,55,81,0.8); color: #F8F8F8; font-size: 20px;'],
        shadeClose: false,
        shade: 0,
		offset: offset,
        skin: 'layerSkin',
        area: [menuSummaryWidth+'px', menuSummaryHeight+'px'],
        content: menuListUrl,
        cancel : function(index, layero) {
			$("#wrap_media").hide();
			getOrgBack();
			clearSpecialLayer('EVENT_STATISTICS');	
			eventHeatMap.clearData();$("#NorMapOpenDiv").remove();
			currLayerOpenIndex = undefined;
        }, success: function(layero, index){
            clearMyLayerB(currLayerOpenIndex);
		}
    });
}


</script>

</html>
