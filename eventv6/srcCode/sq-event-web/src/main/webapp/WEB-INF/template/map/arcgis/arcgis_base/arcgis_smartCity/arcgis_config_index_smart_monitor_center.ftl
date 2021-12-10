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
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_video_versionnoe.ftl" />  
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
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/basic/monitor-air.css"/>
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
		.message-div{color:black;font-size:14px;padding-left:20px;padding-right:20px;padding-top:4px;}
		.message-div p {line-height:25px;}
		.message-div p a {text-decoration:none; color:#27a6f7; float:right; padding-top:10px;} 
		.layerSkin{ position:absolute;border-radius:8px; padding:0px 5px 10px 5px;background:rgba(30,64,89,0.75);}
        
	 .event_con {
		 background-color: #ffff;
	    position: absolute;
	    width: 48%;
	    height: 68%;
	    z-index: 9999;
	}
	.header>img {
    vertical-align: middle;
    height: 35px;
    margin-top: 20px;
	}
	
	.video_nav>li .cur {
   	 	background-image: url(${rc.getContextPath()}/images/cur.png);
  		background-repeat:no-repeat;
		display: inline-block;
		width: 60px;
		height: 60px;
	}

	.header  {
   		position: absolute;
    		top: 0px;
    		z-index: 100;
    		width: 100%;
	}
		.nightMode img {
			filter:saturate(76.74%) hue-rotate(40deg) brightness(91.57%) invert(100%);
		}


	</style>
</head> 
<body style="width:100%;height:100%;border:none;" >  
<div class="header" style="z-index: 10;" > <img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-logo.png"></div>
	
	<!--头部 start-->
	<div class="header-wrap" style="top:120px;z-index: 10" >
		<div class="header-items clearfix">
			<div class="header-arrow header-close"></div>
			<div class="header-contain">
			<div class="header-cont header-cont1">
				<div class="header-conte"  >   
					<i><img    src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-jk-hd-1.png"></i>
					<em>区域</em>
				</div>
				
				<!-- gridLevel == 2 -->
				<div class="hide" id="area_2">
					<ul class="header-lists clearfix"  >
							<li>
								区县<em id="county"></em>个
							</li>
							<span class="header-lists-bar"></span>
							<li>
								乡镇<em id="towns"></em>个
							</li>
						</ul>
						<ul class="header-lists clearfix">
							<li>
								社区<em id="street"></em>个
							</li>
							<span class="header-lists-bar"></span>
							<li>
								网格<em id="gridnum"></em>个
							</li>
						</ul>
				</div>
				<div class="hide" id="area_3">
					<ul class="header-lists clearfix">
							<li>
								乡镇<em id="towns"></em>个
							</li>
							<span class="header-lists-bar"></span>
							<li>
								社区<em id="street"></em>个
							</li>
						</ul>
						<ul class="header-lists clearfix">
							<li>
								网格<em id="gridnum"></em>个
							</li>
						</ul>
				</div>
				<div class="hide" id="area_4">
					<ul class="header-lists clearfix">
							<li>
								社区<em id="street"></em>个
							</li>
							<span class="header-lists-bar"></span>
							<li>
								网格<em id="gridnum"></em>个
							</li>
						</ul>
						<ul class="header-lists clearfix">
						</ul>
				</div>
				<div class="hide" id="area_5">
						<ul class="header-lists clearfix">
							<li>
								网格<em id="gridnum"></em>个
							</li>
							<span class="header-lists-bar"></span>
							
						</ul>
						<ul class="header-lists clearfix">
						</ul>
				</div>
				<div class="header-list" id="area">
				</div>
			</div>
			<div class="header-cont header-cont2">
				<div class="header-conte">
					<i><img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-jk-hd-2.png"></i>
					<em>事件</em>
				</div>
				<div class="header-list">
					<ul class="header-lists clearfix">
						<li>
							总数<em id="sumEvent"></em>个
						</li>
						<span class="header-lists-bar"></span>
						<li>
							待办<em id="backlogEvent"></em>个
						</li>
					</ul>
					<ul class="header-lists clearfix">
						<li>
							办结<em id="endEvent"></em>个
						</li>
						<span class="header-lists-bar"></span>
						<li>
							超时<em id="timeoutEvent"></em>个
						</li>
					</ul>
				</div>
			</div>
			<div class="header-cont header-cont3">
				<div class="header-conte">
					<i><img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-jk-hd-3.png"></i>
					<em>监控</em>
				</div>
				<div class="header-list">
					<ul class="header-lists clearfix">
						<li>
							总数<em id="globalNum"></em>个
						</li>
					</ul>
				</div>
			</div>
			<div class="header-cont header-cont4">
				<div class="header-conte">
					<i><img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-jk-hd-4.png"></i>
					<em>资源</em>
				</div>
				<div class="header-list">
				
					<ul class="header-lists clearfix">
						<li>
							网格员<em id="gridMemberNum"></em>个
						</li>
						<span class="header-lists-bar"></span>
						<li>
							医院<em id="hospitalNum"></em>个
						</li>
					</ul>
					<ul class="header-lists clearfix">
						
						<li>
							派出所<em id="policeNum"></em>个
						</li>
						 <span class="header-lists-bar"></span>
						<li>
							消防<em id="fireNum"></em>个
						</li>
					</ul>
					
				</div>
			</div>
			</div>
		</div>
	</div>
     <input type="hidden" name="tempdir" id="tempdir" value="${tempdir}" />
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
	<div id="jsSlider" ></div>
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
<div class="MapTools" ">
	<ul>
    	<li class="ClearMap" onclick="clearMyLayerB();"></li>
		<li class="ThreeWei" onclick="threeWeiClick();"></li>
		<li class="GoBack" onclick="openSwitchMapModel();"></li>
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="bottom:58px;display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
	<div id="switchMapModel" class="MapStyle" style="width:80px;display:none;">
		<span onclick="switchMapModel(this, 'nightMode')">黑夜模式</span>
	</div>
</div>
<!-- baseDataTabs end -->

		<!-- 专题地图 -->
  		<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
  			<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
  			<input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
  			<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" onchange="showEyesPoint()"/>
  			<input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
  			<input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
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

	<div class="mr-container" id = "viewTop">
	
    <div class="MapBar"  style="top:88px;">
	    <div class="con AlphaBack" >
	    	<#include "/map/arcgis/arcgis_base/top.ftl">
	    </div>
	    <!-----------------------------------人地事物情------------------------------------->
	  <div class="ztIcon AlphaBack titlefirstall"  style="display:none;" >
			<div class="title" >
	<!-- 		<span class="fr" onclick="CloseX()">
			<img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span> -->
			<span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePath" name="titlePath">专题图层</div></div>
		</div>
		<div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
			<div class="title"><span class="fr" onclick="zhoubianListHide()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="" onclick="ShowSearchBtnZhouBian()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
		</div>	
		<div class="ztIcon AlphaBack dest firstall" style="display:block;top:62px;">
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
	    <div class="ztIcon AlphaBack common" >
	        <ul  id="ulCommon"></ul>
	        <div class="clear"></div>
	    </div>
		
		<div class="NorList AlphaBack" style="top:30px">
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
    	<!--告警弹窗-->
		<div class="warn clearfix" style="top: 80%;left: 2px;display: none;">
			<div class="warn-r fl">
				<h5><img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-warn-pic1.png" />告警信息</h5>
				<p id="warn-content" style='min-height: 50px;'>&nbsp;</p>
			</div>
			<div class="warn-l fl"></div>
		</div>
</body>
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
	var LUO_FANG = "<#if LUO_FANG??>${LUO_FANG}</#if>"
var test1="tets";
var heatLayers = {};
 $(function(){
	 
	 //用来计算周边事件和全球眼列表的高度
//	 var viewTop= $('.header-wrap').height();
//	 $('#projectView').css('top',viewTop);
	 var  hdH = $('.header-items').height();
	 $('.header-contain').height(0);
  
     var warnH = $('.warn').height();
     $('.warn-l').height(warnH);
     $('.warn-l').on('click', function(){
     	if($(this).hasClass('warn-on')){
     		$(this).removeClass('warn-on');
     		$('.warn').animate({'left': '2px'});
     		$('.warn-l').css({'WebkitTansform': 'rotate(180deg)', 'MozTransform': 'rotate(180deg)', 'MsTransform': 'rotate(180deg)', 'OTransform': 'rotate(180deg)', 'transform': 'rotate(180deg)'});
     	}else{
     		$(this).addClass('warn-on');
     		$('.warn').animate({'left': '-310px'});
     		$('.warn-l').css({'WebkitTansform': 'rotate(0deg)', 'MozTransform': 'rotate(0deg)', 'MsTransform': 'rotate(0deg)', 'OTransform': 'rotate(0deg)', 'transform': 'rotate(0deg)'});
     	}
     });
     
     
//   头部分类折叠
		$('.header-arrow').on('click', function(){
			if($(this).hasClass('header-close')){//展开
				$(this).siblings('.header-contain').css({'height': hdH});
				$(this).removeClass('header-close');	
				setTimeout(function(){
				     $('.header-cont').height(hdH);
				 }, 500);
				//$(".titlefirstall").css({"top": $(".titlefirstall").position().top+hdH-32 + "px"});
				//$(".firstall").css({"top": $(".firstall").position().top+hdH-32 + "px"});
				//$(".NorList").css({"top": $(".header").height()+hdH-32 +30 + "px"});
			   // $('#get_grid_name_frme').height($(window).height() - $(".header").height() - hdH-32 -30);
				 $(".titlefirstall").css({"top":  130+32 + "px"});
				 $(".firstall").css({"top": 130+32+30 + "px"});
				 $(".NorList").css({"top": 130+32+30 + "px"});
				 var grid_name_frme_height=$(window).height() - $(".header").height()-32-130-30;
				 $('#get_grid_name_frme').height(grid_name_frme_height);
				 $("#get_grid_name_frme").contents().find("#content-md").height(grid_name_frme_height-77);
				 
				 $(".jsSlider").css({"top": 120+130 + "px"});
				 $(".MapTools").css({"top": 320+130 + "px"});
			}else{//关闭
				$(this).siblings('.header-contain').css({'height': '0px'});
				$(this).addClass('header-close');
			
				 $(".titlefirstall").css({"top":"32px"});
				 $(".firstall").css({"top":"62px"});
				 $(".NorList").css({"top": 62 + "px"});
				 
				 var grid_name_frme_height=$(window).height() - $(".header").height()-32-30;
				 $('#get_grid_name_frme').height(grid_name_frme_height);
				 $("#get_grid_name_frme").contents().find("#content-md").height(grid_name_frme_height-77);
				 
				$(".jsSlider").css({"top": 120 + "px"});
				$(".MapTools").css({"top": 320 + "px"});
				 
				 //$(".titlefirstall").css({"top": $(".titlefirstall").position().top-hdH+32 + "px"});
				//$(".firstall").css({"top": $(".firstall").position().top-hdH+32 + "px"});
				//$(".NorList").css({"top": $(".NorList").position().top-hdH+32 + "px"});
				//$('#get_grid_name_frme').height($(window).height() - $(".header").height()+32 - 30);
			}
		});
		setTimeout(function(){
			 $(".titlefirstall").css({"top":"32px"});
			 $(".firstall").css({"top":"62px"});
			 $(".NorList").css({"top":"62px"});
			 $('#get_grid_name_frme').height($(window).height() - $(".header").height()-32-30);
			 $(".jsSlider").css({"top": 120 + "px"});
			 $(".MapTools").css({"top": 320 + "px"});
		 }, 500);

	 
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

 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
 	setTimeout(function(){
 		var level;
		level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
		if(LUO_FANG == "true" && typeof document.getElementById("gridLevelName"+(level-1)) != 'undefined'
		&& document.getElementById("gridLevelName"+(level-1)) != null){
            document.getElementById("gridLevelName"+(level-1)).checked = true;
		}else{
            document.getElementById("gridLevelName"+level).checked = true;
		}

		eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");//--初始化工作流！
	 	getArcgisInfo(function() {// 地图初始化完毕
	 		$(".warn").show();
	 		modleclose();
	 		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	 		if ($("#orgCode").val().indexOf("3501") == 0) {
	 			$("#HeatMapDiv").show();
	 		}
	 		// 初始化黑夜模式地图
	 		switchMapModel($("#switchMapModel").children().eq(0), 'nightMode');
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
	 	});

	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	
		 //区域数据
		 vdeioDataList({});
		 //全球眼、网格员、消防、派出所数据
		 vdeioOtherDataList({});
		 //用来显示重大紧急事件（轮播用的）
		 majorEvent('');
        showEyesPoint($("#orgCode").val());

        zhoubianListHide();
	 	
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
 
 function firstall(){   
		$("#titlePath").html("专题图层");   
		$("."+nowAlphaBackShow).hide();
	    $(".common").hide();
		$("#searchBtnId").hide();
		$(".firstall").show();
		nowAlphaBackShow = "firstall";
		currentLayerListFunctionStr="";
		/* $(".titlefirstall").css({"top": $(".titlefirstall").position().top+$('.header-items').height() + "px"});
		setTimeout(function(){    
			$(".firstall").css({"top": $(".firstall").position().top+$('.header-items').height() + "px"});
		 }, 500); */

}

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
		 	$.messager.alert('友情提示','数据获取出现异常!','warning'); 
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
 
 function openSwitchMapModel(){
 	var mapStyleDiv = document.getElementById("switchMapModel");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
 }
 
 function switchMapModel(obj, className) {
 	if ($(obj).hasClass('current')) {
 		$(obj).removeClass('current');
 		$('.map .container .layersDiv').removeClass(className);
 		
 		$("#viewTop").css("background", "");
 	} else {
 		$(obj).addClass('current');
 		$('.map .container .layersDiv').addClass(className);
 		
 		$("#viewTop").css("background", "#051026");
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
	var orgCode=$("input[name='orgCode']").val(),gridId=$("input[name='gridId']").val();
	vdeioOtherDataList({'orgCode':orgCode,'gridId':gridId});
	vdeioDataList({'orgCode':orgCode,'gridId':gridId});
	majorEvent(orgCode);
    showEyesPoint(orgCode);
	/////////
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
	
	
	//用来显示南平视频大屏的基础数据
	function vdeioDataList(params){
		var gridLevel = $("#gridLevel").val();
		$.ajax({ 
			 url: '${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/listData.json',
			 type: 'POST',
			 data: params,
			 dataType:"json",
			 error: function(data){
			 	$.messager.alert('友情提示','数据获取出现异常!','warning');
			 },
			 success: function(data){
				
				 if(gridLevel > 1 && gridLevel<6) {
					 $("#area").html($("#area_"+gridLevel).html());
					 $("#area #county").html(0);
					 $("#area #towns").html(0); 
					 $("#area #street").html(0); 
					 $("#area #gridnum").html(0); 
					 for(var i=0; i<data.length; i++) {
						 if(data[i].GRID_LEVEL == '3') {//区县
							 var county = data[i].LEVELCOUNT;
							 $("#area #county").html(county); 
						 }
						 if(data[i].GRID_LEVEL == '4') {//乡镇
							 var towns = data[i].LEVELCOUNT;
							 $("#area #towns").html(towns); 
						 }
						 if(data[i].GRID_LEVEL == '5') {//社区
							 var street = data[i].LEVELCOUNT;
							 $("#area #street").html(street); 
						 }
						 if(data[i].GRID_LEVEL == '6') {//网格
							 var gridnum = data[i].LEVELCOUNT;
							 $("#area #gridnum").html(gridnum); 
						 }
					 }
				 }else{
					 $("#area").html(''); 
				 } 
				 
			 }
		 });
		
	}
	
	//用来显示南平视频大屏的
	function vdeioOtherDataList(params){
		$.ajax({ 
			 url: '${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/listOtherData.json',
			 type: 'POST',
			 data: params,
			 dataType:"json",
			 error: function(data){
			 	$.messager.alert('友情提示','数据获取出现异常!','warning');
			 },
			 success: function(data){
				var globalNum = data.globalNum;
				var policeNum = data.policeNum;
				var fireNum = data.fireNum;
				var gridNum = data.gridNum;
				var hospitalNum = data.hospitalNum;

				var sumEvent = data.sumEvent;
				var backlogEvent = data.backlogEvent;
				var endEvent = data.endEvent;
				var timeoutEvent = data.timeoutEvent;
				
				$("#globalNum").html(globalNum);
				$("#policeNum").html(policeNum); 
				$("#fireNum").html(fireNum); 
				$("#gridMemberNum").html(gridNum); 
				$("#hospitalNum").html(hospitalNum); 
				
				$("#sumEvent").html(sumEvent);
				$("#backlogEvent").html(backlogEvent); 
				$("#endEvent").html(endEvent); 
				$("#timeoutEvent").html(timeoutEvent); 
			 }
		 });
		
	}
	 var dataList;
	 var c=0;
	 var showCount=0;
	 var urgencyResults="";
	//用来显示重大紧急事件（轮播用的）
	function majorEvent(orgCode) {
		$.ajax({ 
			 url: '${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/findListByUrgencyDegree.json?urgencyDegrees=04,03,02&t='+Math.random(),
			 type: 'POST',
			 dataType:"json",
			 data: { urgencyDegrees:'04,03,02',orgCode:orgCode},
			 error: function(data){
			 	$.messager.alert('友情提示','数据获取出现异常!','warning');
			 },
			 success: function(data){
				
				 if(data.length>0){
					 dataList=data; 
					 showCount=data.length>10?10:data.length;
					 showWarn();
					 
					 for(var i=0; i<data.length; i++) {
						 val=data[i];
						 urgencyResults=urgencyResults+","+val.eventId;
					 }
					 urgencyResults=urgencyResults.substring(1, urgencyResults.length);
					 //gisPosition(urgencyResults);
				 }else{
					 showCount=0;
					 dataList=[];
					 c=0;
				 }
				 showWarn();
			 }
		 });
	}
	function showEyesPoint(orgCode) {
	    if (!orgCode){
	        orgCode=$("#orgCode").val();
		}
        var elementsCollectionStr  ="gdcId_,_25640,_,orgCode_,_3507,_,homePageType_,_SMART_MONITOR_CENTER,_,largeIco_,_/images/map/gisv0/special_config/images/icon27.png,_,smallIco_,_/images/map/gisv0/map_config/unselected/situation_globalEyes.png,_,treeIcon_,_null,_,menuCode_,_globalEyes,_,menuName_,_南平全球眼,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/situation_globalEyes.png,_,menuListUrl_,_http://event.v6.aishequ.org/zhsq_event/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyes.jhtml,_,menuSummaryUrl_,_http://gd.v6.aishequ.org/zzgrid/zzgl/map/data/situation/globalEyesPlay.jhtml?monitorId=,_,menuLayerName_,_globalEyesLayer,_,menuDetailUrl_,_null,_,menuDetailWidth_,_null,_,menuDetailHeight_,_null,_,menuSummaryWidth_,_null,_,menuSummaryHeight_,_null,_,convergeUrl_,_null,_,scatterPointUrl_,_null,_,remark_,_情-全球眼,_,callBack_,_showObjectList,_,";
        var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getEyesPointDataList.jhtml?bindType=collectionEyes&orgCode="+orgCode;
        currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
        clearSpecialLayer("globalEyesLayer")
        getArcgisDataOfZhuanTi(url,elementsCollectionStr,490,270,30,30);
    }
$(function(){
	 $("#warn-content").click(function(){
		 if($(this).attr("eventId")!=undefined){
			var url='${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/eventDetail.jhtml?mapt=5&eventId='+$(this).attr("eventId");
			showMaxJqueryWindow1('详情', url, 800, 450);  
		 }
	 });

});
	
	function showWarn(){
		if(showCount>0){
			$(".warn").show();
			//于 2018-12-06 17:03:38 在 南平汽车站 发生集体事件，7-8个人集体对殴 happen_time
			var str="于"+formatDatebox(dataList[c].happenTime)+"在"+dataList[c].occurred+"发生" +dataList[c].content;
			var eventId=dataList[c].eventId;
			$("#warn-content").attr("title",str);
			$("#warn-content").html(cutstr(str, 100));
			$("#warn-content").attr("eventId",eventId);
	         c+=1;
			  if(c==showCount){
	        	 c=0;
	         }
		}else{
			$(".warn").hide();
		}
		
	}
	  var GetLength = function (str) {
	        ///<summary>获得字符串实际长度，中文2，英文1</summary>
	        ///<param name="str">要获得长度的字符串</param>
	        var realLength = 0, len = str.length, charCode = -1;
	        for (var i = 0; i < len; i++) {
	            charCode = str.charCodeAt(i);
	            if (charCode >= 0 && charCode <= 128) realLength += 1;
	            else realLength += 2;
	        }
	        return realLength;
	    };

 

	    /** 
	     * js截取字符串，中英文都能用 
	     * @param str：需要截取的字符串 
	     * @param len: 需要截取的长度 
	     */
	    function cutstr(str, len) {
	        var str_length = 0;
	        var str_len = 0;
	        str_cut = new String();
	        str_len = str.length;
	        for (var i = 0; i < str_len; i++) {
	            a = str.charAt(i);
	            str_length++;
	            if (escape(a).length > 4) {
	                //中文字符的长度经编码之后大于4  
	                str_length++;
	            }
	            str_cut = str_cut.concat(a);
	            if (str_length >= len) {
	                str_cut = str_cut.concat("...");
	                return str_cut;
	            }
	        }
	        //如果给定字符串小于指定长度，则返回源字符串；  
	        if (str_length < len) {
	            return str;
	        }
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
	var elementsCollectionStr  ="smallIco_,_/images/map/gisv0/map_config/unselected/event/event_radar.gif,_,treeIcon_,_null,_,menuCode_,_dbEventanquan,_,menuName_,_待办事件（重大）,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/event/event_radar.gif,_,menuListUrl_,_/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?eventType=todo&type=06&page=globalEye,_,menuSummaryUrl_,_/zhsq/map/nanpingVideoDataController/eventDetail.jhtml?mapt=5&eventId=,_,menuLayerName_,_事件详情,_,menuDetailUrl_,_null,_,menuDetailWidth_,_600,_,menuDetailHeight_,_450,_,menuSummaryWidth_,_800,_,menuSummaryHeight_,_450,_,remark_,_null,_,callBack_,_showObjectList,_,";
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataList.jhtml?ids="+res+"&mapt=5";
		 currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
    clearSpecialLayer("事件详情")
		 getArcgisDataOfZhuanTi(url,elementsCollectionStr,490,270,30,30);  
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
  
//取窗口可视范围的高度
function getClientHeight()
{
  var clientHeight=0;
  if(document.body.clientHeight&&document.documentElement.clientHeight)
  {
  var clientHeight = (document.body.clientHeight<document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
  }
  else
  {
  var clientHeight = (document.body.clientHeight>document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
  }
  return clientHeight;
}

function showMaxJqueryWindow1(title, targetUrl,width,height, maximizable, scroll, callBackOnClose) {
	var $MaxJqueryWindow;
	var top = 0;
	var left = 0;
	if(width==undefined || width==null) {
		width = $(window).width();
	}
	if(height==undefined || height==null) {
		height = $(window).height();
	}
	if(maximizable==undefined || maximizable==null){
		maximizable = false;
	}
	if(scroll==undefined || scroll==null){
		scroll = "no";
	}
	left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
	top = parseInt(($(window).height()-height)*0.5+$(document).scrollTop());
	maxJqueryWindowId = "MaxJqueryWindow_" + dialogs.length;
	var maxJqueryWin =  '<div id="'+ maxJqueryWindowId +'" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"'+
							'closed="true" modal="true" style="width:'+width+'px;height:'+height+'px;padding:1px; overflow:hidden;">'+
							"<iframe scrolling="+scroll+" frameborder='0' style='width:100%;height:100%;'></iframe>"+
						'</div>';
	var MaxJqueryWindowContentHtml = $("#MaxJqueryWindowContent").html();
	MaxJqueryWindowContentHtml += maxJqueryWin;
	$("#MaxJqueryWindowContent").html(MaxJqueryWindowContentHtml);
	$MaxJqueryWindow = $('#'+maxJqueryWindowId).window({
	    title: (title==null || title=="")?"信息窗口":title,
	    width: width,
	    height: height,
	    top: top,
	    left: left,
	    shadow: false,
	    modal: true,
	    closed: true,
	    closable: true,//是否有关闭按钮
	    minimizable: false,
	    maximizable: maximizable,
	    collapsible: false,
	    onBeforeClose: function () {
             if(callBackOnClose != undefined && callBackOnClose != null) {
             	callBackOnClose();
             }
        },
        onClose: function(){
			var dialogTmp = dialogs.pop();
			$(dialogTmp).parent().remove();//防止已关闭的窗口再次加载信息
		}
	});
	
	$('#'+maxJqueryWindowId+' > iframe').attr('src', targetUrl);//减少多次请求
	
	if(window["$MaxJqueryWindowOnClose"]){
		$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
	} 
	
	dialogs.push($MaxJqueryWindow);
	$MaxJqueryWindow.window('open');
}
Date.prototype.format = function (format) {  
    var o = {  
        "M+": this.getMonth() + 1, // month  
        "d+": this.getDate(), // day  
        "h+": this.getHours(), // hour  
        "m+": this.getMinutes(), // minute  
        "s+": this.getSeconds(), // second  
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S": this.getMilliseconds()  
        // millisecond  
    }  
    if (/(y+)/.test(format))  
        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
            .substr(4 - RegExp.$1.length));  
    for (var k in o)  
        if (new RegExp("(" + k + ")").test(format))  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
    return format;  
}  
function formatDatebox(value,type) {  
    if (value == null || value == '') {  
        return '';  
    }  
    var dt;  
    if (value instanceof Date) {  
        dt = value;  
    } else {  
        dt = new Date(value);  
    }  
    if (type != null && type == 'M' ) {  
         return dt.format("yyyy-MM"); 
    }  
    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
} 
</script>
<script type="text/javascript">
     //告警信息  
      setInterval("myInterval()",15000);//1000为1秒钟

       function myInterval()
       {
    	  
    		if(showCount>0){
    			showWarn();
    		}
        }
 </script>
</html>
