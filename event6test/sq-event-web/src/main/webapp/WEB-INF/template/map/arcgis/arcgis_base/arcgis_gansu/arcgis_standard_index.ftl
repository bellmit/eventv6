<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title>
	<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
	
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
    <#include "/component/ImageView.ftl" />
	<script type="text/javascript">
	//图片查看器回调
	function ffcs_viewImg(fieldId){
		var sourceId = fieldId + "_Div";
		var imgDiv = $("#"+sourceId+"");
		imgDiv.find('.fancybox-button').eq(0).click();
	}
	</script>

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
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
<!--
	<div id="switchMap">
		<span onclick="switchMap('img')" id="imgMap" title="显示卫星地图">
			<span id="imgInner">
				<span id="inner"></span>
				<span id="text">卫星</span>
			</span>
		</span>
		<span onclick="switchMap('vec')" id="vecMap" title="显示普通地图">
			<span id="vecInner">
				<span id="inner"></span>
				<span id="text">地图</span>
			</span>
		</span>
	</div> 
 -->
	
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
		<div class="title"><span class="fr" onclick="CloseX()"><img title="关闭" src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/closex.png" /></span><span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img title="查询" src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/search.png" /></span><div id="titlePath" name="titlePath">专题图层</div></div>
	</div>	
	<div class="ztIcon AlphaBack dest firstall" style="display:block;">
	
    	<div class="h_20"></div>
		<ul>
			<li class="GreenBg" onclick="people()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon01.png" /></p>
			</li>
			<li class="YellowBg" onclick="world()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon02.png" /></p>
			</li>
			<li class="CyanBg" onclick="metter()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon03.png" /></p>
			</li>
			<li class="PrinkBg" onclick="thing()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon04.png" /></p>
			</li>
			<li class="PurpleBg" onclick="situation()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon05.png" /></p>
			</li>
			<li class="BlueBg" onclick="organization()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon30.png" /></p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------人------------------------------------->
	<div class="ztIcon AlphaBack people">
    	
    	<div class="h_20"></div>
		<ul>
			<!--
			<li class="GreenBg" onclick="showObjectList('党员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon001.png" /></p>
				<p class="word AlphaBack">党员</p>
			</li>
			<li class="GreenBg" onclick="showObjectList('退休人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon002.png" /></p>
				<p class="word AlphaBack">退休人员</p>
			</li>
			<li class="GreenBg" onclick="showObjectList('居家养老人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon003.png" /></p>
				<p class="word AlphaBack">居家养老</p>
			</li>
			<li class="YellowBg" onclick="showObjectList('服兵役人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon004.png" /></p>
				<p class="word AlphaBack">服兵役人员</p>
			</li>
			<li class="YellowBg" onclick="showObjectList('失业人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon005.png" /></p>
				<p class="word AlphaBack">失业人员</p>
			</li>
			<li class="YellowBg" onclick="showObjectList('低保人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon06.png" /></p>
				<p class="word AlphaBack">低保人员</p>
			</li>
			
			<li class="CyanBg" onclick="showObjectList('残障人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon07.png" /></p>
				<p class="word AlphaBack">残障人员</p>
			</li>
			-->
			<li class="CyanBg" onclick="showObjectList('重精神病人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon08.png" /></p>
				<p class="word AlphaBack">重精神病</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('危险品从业人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon09.png" /></p>
				<p class="word AlphaBack">危险品从业</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('上访人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon10.png" /></p>
				<p class="word AlphaBack">上访人员</p>
			</li>
			<li class="PrinkBg" onclick="showObjectList('吸毒人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon11.png" /></p>
				<p class="word AlphaBack">吸毒人员</p>
			</li>
			<li class="PrinkBg" onclick="showObjectList('邪教人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon12.png" /></p>
				<p class="word AlphaBack">邪教人员</p>
			</li>
			<li class="PrinkBg" onclick="showObjectList('矫正人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon13.png" /></p>
				<p class="word AlphaBack">矫正人员</p>
			</li>
			<li class="PurpleBg" onclick="showObjectList('刑释解教人员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon14.png" /></p>
				<p class="word AlphaBack">刑释解教</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------地------------------------------------->
	<div class="ztIcon AlphaBack world">
    	
    	<div class="h_20"></div>
		<ul>
			<li class="YellowBg" onclick="showObjectList('公交站')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon24.png" /></p>
				<p class="word AlphaBack">公交站</p>
			</li>
			<li class="PrinkBg" onclick="showLyjjList('楼栋')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">楼栋</p>
			</li>
			<li class="YellowBg" onclick="showObjectList('出租屋')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon15.png" /></p>
				<p class="word AlphaBack">出租屋</p>
			</li>
			
			<li class="YellowBg" onclick="showObjectList('企业')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">企业</p>
			</li>
			 <!-----------------------------------房------------------------------------->
			<li class="CyanBg" onclick="showObjectList('物业管理住宅')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">物业管理住宅</p>
			</li>
			<!--
			<li class="CyanBg" onclick="showObjectList('社区托管住宅')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">社区托管住宅</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('单位自管住宅')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">单位自管住宅</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('单位楼')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">单位楼</p>
			</li>
			<li class="PrinkBg" onclick="showObjectList('工地')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">工地</p>
			</li>
			<li class="PurpleBg" onclick="showObjectList('宿舍')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">宿舍</p>
			</li>
			<li class="PurpleBg" onclick="showObjectList('民房')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">民房</p>
			</li>
			<li class="GreenBg" onclick="showObjectList('厂房')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">厂房</p>
			</li>
			-->
			<li class="CyanBg" onclick="showObjectList('城市综合体')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">城市综合体</p>
			</li>
			
			<li class="CyanBg" onclick="showObjectList('公园广场')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">公园广场</p>
			</li>
			
			<li class="PurpleBg" onclick="showObjectList('学校')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon17.png" /></p>
				<p class="word AlphaBack">学校</p>
			</li>
			
			<li class="PurpleBg" onclick="showObjectList('重点场所')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon16.png" /></p>
				<p class="word AlphaBack">重点场所</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------事------------------------------------->
	<div class="ztIcon AlphaBack metter">
    	
    	<div class="h_20"></div>
		<ul>
			<li class="CyanBg" onclick="showAddEventPanel()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/collect.png" /></p>
				<p class="word AlphaBack">采集录入</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('待办事件')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon20.png" /></p>
				<p class="word AlphaBack">待办事件</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('将到期')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon22.png" /></p>
				<p class="word AlphaBack">将到期</p>
			</li>
			<li class="CyanBg" onclick="showObjectList('关注事件')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/attention.png" /></p>
				<p class="word AlphaBack">关注事件</p>
			</li>
			<!--
			<li class="CyanBg" onclick="showVoiceCallPanel()">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon18.png" /></p>
				<p class="word AlphaBack">语音呼叫</p>
			</li>
			-->
			<li class="CyanBg" onclick="showObjectList('辖区事件')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/all.png" /></p>
				<p class="word AlphaBack">辖区事件</p>
			</li>
		</ul>
        <div class="clear"></div>
        <!-- <div class="zonglan">
            <p class="current">事件总览</p>
            <p>区域事件总览</p>
        </div>
		<div class="h_20"></div>
        <div class="tab_box">
            <div class="sjzl tabs">
                <div class="chart" style="text-align:center;"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/chart.png" /></div>
                <div class="h_20"></div>
                <div class="tushi">指针：结案率<img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/sekuai_03.png" />较差<img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/sekuai_05.png" />一般<img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/sekuai_07.png" />良好</div>
                <div class="h_10"></div>
                <p>已 结 案：<span>251（86.9%）</span></p>
                <p>处 理 中：<span>251（86.9%）</span></p>
                <p>超时案件：<span>251（86.9%）</span></p>
                <div class="h_10"></div>
            </div>
            <div class="qyzl tabs hide">
                <div class="chart" style="text-align:center;"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/chart2.png" /></div>
                <div class="h_20"></div>
            </div>
        </div> -->
	</div>
    <!-----------------------------------物------------------------------------->
	<div class="ztIcon AlphaBack thing">
    	
    	<div class="h_20"></div>
		<ul>
			<li class="PrinkBg" onclick="showObjectList('消防栓')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon23.png" /></p>
				<p class="word AlphaBack">消防栓</p>
			</li>
			
			<li class="PrinkBg" onclick="showObjectList('公共厕所')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon25.png" /></p>
				<p class="word AlphaBack">公共厕所</p>
			</li>
			
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------情------------------------------------->
    <div class="ztIcon AlphaBack situation">
    	
    	<div class="h_20"></div>
		<ul>
			
			<li class="PurpleBg" onclick="showObjectList('网格员')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon26.png" /></p>
				<p class="word AlphaBack">网格员</p>
			</li>
			<li class="PurpleBg" onclick="showObjectList('全球眼')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon27.png" /></p>
				<p class="word AlphaBack">全球眼</p>
			</li>
			
		</ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------组织------------------------------------->
    <div class="ztIcon AlphaBack organization">
    	
    	<div class="h_20"></div>
		<ul>
			<!--
			<li class="BlueBg" onclick="showObjectList('党组织')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/dangzuzhi.png" /></p>
				<p class="word AlphaBack">党组织</p>
			</li>
			<li class="BlueBg" onclick="showObjectList('重点场所')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/icon16.png" /></p>
				<p class="word AlphaBack">重点场所</p>
			</li>
			-->
			<li class="BlueBg" onclick="showObjectList('新经济组织')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/new_economy.png" /></p>
				<p class="word AlphaBack">新经济组织</p>
			</li>
			<li class="BlueBg" onclick="showObjectList('新社会组织')">
				<p class="pic"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/new_society.png" /></p>
				<p class="word AlphaBack">新社会组织</p>
			</li>
		</ul>
        <div class="clear"></div>
	</div>
	
	<div class="NorList AlphaBack">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</div>

</body>
<script src="${rc.getContextPath()}/theme/arcgis/standardmappage/js/function.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />
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
	 	$("html").attr("class","");
	 	eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");
	 	var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	 	document.getElementById("gridLevelName"+level).checked = true;
	 	getArcgisInfo();
	 	
	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
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

</script>
</html>
