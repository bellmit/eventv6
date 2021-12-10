<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv=X-UA-Compatible content="IE=edge">
	<title>地图首页</title>
	<link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
	<#include "/map/mapabc/relyJs.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	
	<script type="text/javascript" src="${rc.getContextPath()}/js/map/map.api.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
	<#include "/component/ImageView.ftl" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<script type="text/javascript">
	//图片查看器回调
	function ffcs_viewImg(fieldId){
		var sourceId = fieldId + "_Div";
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	}
	</script>
	<style>
		.topBtn{margin-right:10px; margin-top:5px; width:80px;}
		.topBtn a{display:block; width:80px; height:22px; background:#4489ca; color:#fff; text-align:center; line-height:22px;}
		.topBtn a:hover{background:#5ea2e1; color:#fff; text-decoration:none;}
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
<div class="MapTools" style="top:261px;z-index:2;">
	<ul>
    	<li class="ClearMap" onclick="clearMyLayer();"></li>
    	<li class="ThreeWei" onclick="threeWeiClick();"></li>
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none;width:128px;">
		<span class="current" type="pingtandt">矢量图</span>
		<span type="pingtanimage">影像图</span>
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
	<!-----------------------------------热力图时间轴------------------------------------->
    <div class="NorMapOpenDiv2 heatMapTimeAxis hide dest" style="bottom:5px; left:5px;">
		<div class="box" style="width:1000px;height:87px;">
	    	<div class="title"></div>
	        <div class="range" style="width:100%;"></div>
	    </div>
	</div>
	<!-----------------------------------迁徙图------------------------------------->
    <div class="NorMapOpenDiv2 migrationMap hide dest" style="top:10px;left:10px;bottom:10px;right:10px;position:absolute;z-index:11;">
		<div class="box" style="width:100%;height:100%;">
	    	<div class="title"><span class="fr close" onclick="migrationMap();"></span>迁徙图</div>
	        <iframe id="migrationMap" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
	    </div>
	</div>
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
        <div class="topBtn fr"><a href="javascript:void(0);" onclick="window.parent.cut();">9+X综合地图</a></div>
        <!-- 
        <div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>
        <div class="kuangxuan fr"><a href="#"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/kuangxuan.png" />框选统计</a></div>
    	-->
    	<div class="migration zhoubian fr" onclick="migrationMap();"><a href="#"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/kuangxuan.png" />迁徙图</a></div>
    </div>

    <!-----------------------------------人地事物情------------------------------------->
    <div style="position:absolute;top:35px;z-index:3;right:0px;margin-top:-3px;cursor:pointer;background-color:black;">
    	<span class="fr" style="height:30px;width:30px;" onclick="CloseY(this)">
			<img style="margin-top:6px;margin-left:7px;" src="${uiDomain!''}/images/to_right.png" />
		</span>
    </div>
    <div class="ztIcon AlphaBack titlefirstall" style="display:none;">
		<div class="title">
			<span id="searchBtnId" class="fr" style="margin-top:2px;margin-right:25px;display:none;" onclick="ShowSearchBtn()">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" />
			</span>
			<div id="titlePath" name="titlePath">专题图层</div>
		</div>
	</div>
	<div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
		<div class="title"><span class="fr" style="margin-top:3px;" onclick="zhoubianListHide()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="margin-top:3px;" onclick="ShowSearchBtnZhouBian()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
	</div>	
	<div type="level1" class="ztIcon AlphaBack dest firstall" style="display:block;">
		<div id="divTreeMenu">
			<ul id="ulFirstall"></ul>
		</div>
        <div class="clear"></div>
	</div>
    <!-----------------------------------人------------------------------------->
	<div type="level2" class="ztIcon AlphaBack people">
		<ul id="ulPeople"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------地------------------------------------->
	<div type="level2" class="ztIcon AlphaBack world">
		<ul id="ulWorld"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------事------------------------------------->
	<div type="level2" class="ztIcon AlphaBack metter">
		<ul id="ulMetter"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------物------------------------------------->
	<div type="level2" class="ztIcon AlphaBack thing">
		<ul id="ulThing"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------情------------------------------------->
    <div type="level2" class="ztIcon AlphaBack situation">
		<ul id="ulSituation"></ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------组织------------------------------------->
    <div type="level2" class="ztIcon AlphaBack organization">
		<ul  id="ulOrganization"></ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------精准扶贫------------------------------------->
    <div type="level2" class="ztIcon AlphaBack precisionPoverty">
		<ul  id="ulPrecisionPoverty"></ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------通用------------------------------------->
    <div class="ztIcon AlphaBack common">
        <ul  id="ulCommon"></ul>
        <div class="clear"></div>
    </div>
	
	<div type="level3" class="NorList AlphaBack" style="z-index:3;">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
	<div class="NorListZhouBian AlphaBackZhouBian zhoubianList" style="z-index:3;">
		<iframe id="zhoubian_list_frme" name="zhoubian_list_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</div>

<div id="dialog" title="三维图展示">
    <iframe id="lc_skylineview_frme" name="lc_skylineview_frme" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>
<!-- 热力拥挤度图例 -->
<div class="map_noprint AlphaBack" style="display:none;">
  <div class="legend-text">热力拥挤度图例：</div>
  <span class="legend-item legend-1">非常舒适
    <div class="legend-item-wrapper" style="display:none;">
      <div class="legend-item-tip">
        <div class="legent-item-pic"></div>人数：<em ratio="0.1">0</em>人<span class="legend-item-tip-arrow"></span>
      </div>
     </div>
   </span>
  <span class="legend-item legend-2">舒适
    <div class="legend-item-wrapper" style="display:none;">
      <div class="legend-item-tip">
        <div class="legent-item-pic"></div>人数：<em ratio="0.2">0</em>人<span class="legend-item-tip-arrow"></span>
      </div>
    </div>     
  </span>
  <span class="legend-item legend-3">一般
    <div class="legend-item-wrapper" style="display:none;">
      <div class="legend-item-tip">
        <div class="legent-item-pic"></div>人数：<em ratio="0.4">0</em>人<span class="legend-item-tip-arrow"></span>
      </div>
    </div>     
  </span>
  <span class="legend-item legend-4">拥挤
    <div class="legend-item-wrapper" style="display:none;">
      <div class="legend-item-tip">
        <div class="legent-item-pic"></div>人数：<em ratio="0.8">0</em>人<span class="legend-item-tip-arrow"></span>
      </div>
    </div>     
  </span>
  <span class="legend-item legend-5">非常拥挤
    <div class="legend-item-wrapper" style="display:none;">
      <div class="legend-item-tip">
        <div class="legent-item-pic"></div>人数：<em ratio="1.0">0</em>人<span class="legend-item-tip-arrow"></span>
      </div>
    </div>
  </span>
</div>

</body>
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function_versionnoe.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript">
var divDrag = {
	kxWinObj: null,
	zbWinObj: null
};
var test1="tets";

function _init() {
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
	window.onresize=function(){
		$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	};
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
	});
	$(document).mouseup(function() {
		divDrag.kxWinObj.css("cursor", "default");
		divDrag.zbWinObj.css("cursor", "default");
		$(this).unbind("mousemove");
	});
}

$(function(){
 	layer.load(0);
 	_init();
 	zhoubianListHide();
 	getLayerMenuInfo();
 	var gridLevel = parseInt($("#gridLevel").val());
 	var level = gridLevel + 1;
 	if (gridLevel == 2) {
 		level = gridLevel + 2;
 	} else if (gridLevel == 6) {
 		level = gridLevel;
 	}
 	document.getElementById("gridLevelName"+level).checked = true;
	changeCheckedAndStatus(gridLevel, level);
	var eventDomain = "${rc.getContextPath()}";
	var zzgridDomain = "${SQ_ZZGRID_URL}";
	eventInit(eventDomain, zzgridDomain);//--初始化工作流！
	MMApi.LoadMap(eventDomain, zzgridDomain, "${SPGIS_IP!''}", "SpGisMap", function(mapApi) {
		layer.closeAll('loading');
		mapApi.Init("map0", $("#gridId").val(), $("#orgCode").val(), {
			openMapClick : true,
			openBoundsChange : true
		});
		// 开启热力图
		$("#BuildOutlineDiv").removeClass("hide");
		$("#HeatMapDiv").removeClass("hide");
		
		$("#mapStyleDiv").find("span").click(function() {
			if (!$(this).hasClass("current")) {
				$("#mapStyleDiv").find("span").removeClass("current");
				$(this).addClass("current");
				mapApi.showSicMapOfName($(this).attr("type"));
				threeWeiClick();
			}
		});
	}, {
	<#if fillOpacity??>
		fillOpacity : '${fillOpacity}'
	</#if>
	});
 });

function threeWeiClick() {
	var mapStyleDiv = document.getElementById("mapStyleDiv");
 	if (mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	} else {
 		mapStyleDiv.style.display = 'none';
 	}
}

function migrationMap() {
	var migrationMap = $(".migrationMap");
	if (migrationMap.is(":hidden")) {
		$("#migrationMap").attr("src", MMGlobal.ContextPath + "/zhsq/map/arcgis/arcgis/toMigrationMap.jhtml");
	} else {
		try {
			$("#migrationMap")[0].contentWindow.MMApi.removeAllMigrationLine();
		} catch(e) {}
	}
	migrationMap.toggle();
}
 
function getArcgisDataOfBuildsByCheck() {
	if(document.getElementById("buildName0").checked == true) {
		MMApi.drawBuildingLine($("#gridId").val(), $("#orgCode").val());
	}else {
		MMApi.clearMap("buildingWMSLayer");
	}
}
function getArcgisDataOfGridsByLevel(level) {
	if (document.getElementById("gridLevelName"+level).checked == true) {
		var glns = $("input[name='gridLevelName']");
		for(var i=0; i<glns.length; i++) {
			if(glns[i].value!=level){
				glns[i].checked = false;
			}
		}

		var value = document.getElementById("li"+level).innerText;
		$("#level").html(value);
		MMApi.getArcgisDataOfGrids($("#gridId").val(), $("#gridCode").val(), MMGlobal.MapType ,level);
	} else {
		
	}
}

function MapTipHide() {
	$("#_map_tip").find("li").html("");//窗帘效果展开
}
function MapTipShow(msg) {
	$("#_map_tip").find("li").html(msg);
	setTimeout("MapTipHide()", 3000);
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
		 }
	 });
}

var currentMapStyleObj;

function getLayerMenuInfo() {
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	var displayStyle = '0'; // 平铺
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getDisplayStyle.json?t='+Math.random(),
		type: 'POST',
		timeout: 300000,
		data: { orgCode:orgCode,homePageType:homePageType},
		dataType:"json",
		error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		},
		success: function(data){
			if (data == '1') { // 树形展示
				displayStyle = data;
			}
			if (displayStyle == '0') {
				$.ajax({ 
					 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
					 type: 'POST',
					 timeout: 300000,
					 data: { orgCode:orgCode,homePageType:homePageType,gdcId:4,isRootSearch:1},
					 dataType:"json",
					 error: function(data){
					 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
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
								    	htmlStr += "<li class=\""+ulFirstallList[i].className+"\" onclick=\""+ulFirstallList[i].callBack+"\" title=\""+ulFirstallList[i].menuName+"\"><p class=\"pic\"><img src=\""+uiDomain+ulFirstallList[i].largeIco+"\" /></p></li>";
							    	} else {
								    	htmlStr += "<li class=\""+ulFirstallList[i].className+"\" onclick=\""+ulFirstallList[i].callBack+"\" title=\""+ulFirstallList[i].menuName+"\"><p class=\"pic\"><img src=\""+uiDomain+ulFirstallList[i].largeIco+"\" /></p><p class=\"word AlphaBack\">"+ulFirstallList[i].menuName+"</p></li>";
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
				var setting = {
					edit: {
						drag: {isCopy: true, isMove: false},
						enable: true,
						showRemoveBtn: false,
						showRenameBtn: false
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick:treeNodeClick
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
							$("#divTreeMenu").addClass("TreeMenu");
							$("#divTreeMenu").css("height", $(window).height() - 62);
							$("#ulFirstall").addClass("ztree");
							$.fn.zTree.init($("#ulFirstall"), setting, data);
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
	if(treeNode.callBack != null &&(treeNode.callBack).length>0){
		var titlehtml = "<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > "+treeNode.name;
		$("#titlePath").html(titlehtml);
		
	 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
		$("#searchBtnId").show();
		$("."+nowAlphaBackShow).hide();
		$(".NorList").show();
		nowAlphaBackShow = "NorList";
		eval(treeNode.callBack);
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
	var callBackUrl = 'http://${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
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
</script>
</html>
