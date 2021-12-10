<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>莆田中间页面首页</title>
	<script type="text/javascript">
		var js_ctx =  "${rc.getContextPath()}";
		var _myServer = "${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
		var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
	</script>
	<link href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" />
    <link href="${rc.getContextPath()}/js/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/style/css/mapindex.css" />
	<script src="${rc.getContextPath()}/js/map/arcgis/library/3.8/init.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	
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
	
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	
	<link href="${rc.getContextPath()}/theme/arcgis/standardmappage/css/public.css" rel="stylesheet" type="text/css" />
	<link href="${rc.getContextPath()}/theme/arcgis/standardmappage/css/map.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/public.css">
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/css/frame.css">
	<link href="${rc.getContextPath()}/theme/arcgis/standardmappage/css/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css">
	<script src="${rc.getContextPath()}/theme/arcgis/standardmappage/js/jquery.mCustomScrollbar.concat.min.js"></script>
	
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	
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
	<div class="page-container" id="map" style="position: absolute; width:100%; height:100%; z-index: 1;">
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
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
  		</form> 
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
        <!--<div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>-->
    </div>
</div>

<div id="full-layout">
	<!--------------------------SystemLeftContent begin---------------------->
    	<div id="shad_y" class="shad_y"></div>
    	<div id="Slidebtn" class="Slidebtn"><a id="toggle-slide" class="toggle-slide full-con" href="#"></a></div>
    <!--------------------------SystemLeftContent end---------------------->

    <!--------------------------MainRight begin---------------------->
        <div class="MainRight" id="slide">
			<div class="title">
            	<ul>
                	<li class="current" id="a1" onclick="Tab('a','box_',6,1)">批办事项</li>
                    <li id="a2" onclick="Tab('a','box_',6,2)">已办事项</li>
                    <li id="a3" onclick="Tab('a','box_',6,3)">应急指挥</li>
                    <li id="a4" onclick="Tab('a','box_',6,4)">视频调度</li>
                    <li id="a5" onclick="Tab('a','box_',6,5)">舆情动态</li>
                </ul>
            </div>
			<div class="box" id="SlideBox">
            <!--------------------------待办事项 begin---------------------->
                <div id="box_1" style="display:block;">                 
                    <iframe data-iframe="true" name="iframeOfBatchDo" id="batchDo" src="" style="width:100%;height:100%;" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
                </div>
            <!--------------------------待办事项 end---------------------->
            <!--------------------------已办事项 begin---------------------->
                <div id="box_2" style="display:none;">
                     <iframe data-iframe="true" name="iframeOfHadDo" id="hadDo" src="" style="width:100%;height:100%;" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
                </div>
            <!--------------------------已办事项 end---------------------->
            <!--------------------------应急指挥 begin---------------------->
                <div id="box_3" style="display:none;">
                    <div class="NavBox">
                        <img width='350' src="${rc.getContextPath()}/theme/arcgis/standardmappage_putian/images/putianyinjizhihui1.png"/>
                    </div>
                </div>
            <!--------------------------应急指挥 end---------------------->
            <!--------------------------视频调度 begin---------------------->
				<div id="box_4" style="display:none;">                
                    <iframe data-iframe="true" name="iframeOfVideoGo" id="videoGo" src="" style="width:100%;height:100%;" scrolling="no" frameborder="0" allowtransparency="true"></iframe>
                </div>
            <!--------------------------视频调度 end---------------------->
            <!--------------------------舆情动态 begin---------------------->
                <div id="box_5" style="display:none;">
                  建设中...
                </div>
            <!--------------------------舆情动态 end---------------------->
			</div>
        </div>
    <!--------------------------MainRight end---------------------->
</div>
</body>
<script src="${rc.getContextPath()}/theme/arcgis/standardmappage/js/function.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
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
 $("#map").css("height",$(document).height());
	$("#map").css("width",$(document).width());
 	eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");
 	getArcgisInfo();
 	var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
 	document.getElementById("gridLevelName"+level).checked = true;
 	changeCheckedAndStatus($("#gridLevel").val(),level);
 	getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
 	
 	// 设置每个iframe高度
 	var height = document.getElementById('map').offsetHeight - 32 - 38;
 	
 	for (var i = 1; i < 6; i++) {
 		$("#box_" + i).css('height', height);
	}
	Tab('a','box_',6,1);
 })

 
function getArcgisDataOfBuildsByCheck() {
	if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
	}else {
		$("#map").ffcsMap.clear({layerName : "buildLayerPoint"});
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
		$("#map").ffcsMap.clear({layerName : "gridLayer"});
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

function Tab(Xname,Cname,Lenght,j){
	for(i=1;i<Lenght;i++){
		eval("$('#"+Xname+i+"').attr('class', '')");
		}
		
	eval("$('#"+Xname+j+"').attr('class', 'current')");
	
	for(i=1;i<Lenght;i++){
		eval("$('#"+Cname+i+"').css('display', 'none')");
		eval("$('#"+Cname+j+"').css('display', 'block')");
	}
	//批办事项
	if(j==1){
	   if(document.getElementById("batchDo").src != "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/batchEventPutian.jhtml?orgCode="+${orgCode}) {
	   		document.getElementById("batchDo").src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/batchEventPutian.jhtml?orgCode="+${orgCode};
	   }else {
	   		setTimeout(function (){
	    		window.frames['iframeOfBatchDo'].gisPosition();
	    	},100); 
	   }
	}
	//已办事项
	if(j==2){
		if(document.getElementById("hadDo").src != "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/hadDoEventPutian.jhtml?orgCode="+${orgCode}) {
	   		document.getElementById("hadDo").src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/hadDoEventPutian.jhtml?orgCode="+${orgCode};
	   }else {
	   		setTimeout(function (){
	    		window.frames['iframeOfBatchDo'].gisPosition();
	    	},100); 
	   }
	}
	
	// 视频调度
	if(j==4){
	   if(document.getElementById("videoGo").src != "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesPutian.jhtml?orgCode="+${orgCode}) {
	   		document.getElementById("videoGo").src = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesPutian.jhtml?orgCode="+${orgCode};
	   }else {
	   		setTimeout(function (){
	    		window.frames['iframeOfBatchDo'].gisPosition();
	    	},100); 
	   }
	}
}

var fullLayout=$("#full-layout");

//隐藏显示侧边导航
$(".toggle-slide").on("click",function(){
	if(fullLayout.hasClass("full-con")){
		fullLayout.removeClass("full-con");
		$("#shad_y").css("right", "340px");
		$("#Slidebtn").css("right", "340px");
	}else{
		fullLayout.addClass("full-con");
		$("#shad_y").css("right", 0);
		$("#Slidebtn").css("right", 0);
	}
	return false;
});

</script>
</html>
