<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图首页</title>
<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
<script type ="text/javascript">
var jsBasePath = "${rc.getContextPath()}";
</script>
<script type="text/javascript" src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/map_labeling_interface.js"></script>
    <link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
<#include "/component/ImageView.ftl" />
    <link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
    <script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/map/spgis/lib/jQuery.md5.js"></script>
    <script type="text/javascript">
        //图片查看器回调
//         function ffcs_viewImg(fieldId){
//             var sourceId = fieldId + "_Div";
//             var imgDiv = $("#"+sourceId+"");
//             imgDiv.find('.fancybox-button').eq(0).click();
//         }
    </script>
    <style>
        .ztree li{overflow:hidden;}
        .ztree li span{font-size:14px;}
        .mapbox{ cursor: pointer;background:#fff; border:2px solid #2ab7b2; padding:5px; position:relative; min-width:30px; display:block; white-space:nowrap;}
        .mapicon{ display:inline-block;margin-right:3px;}
        .bukong{margin-right:10px; margin-top:5px; width:100px;}
        .bukong a{display:block; width:100px; height:22px; background:#1eca1e; color:#fff; text-align:center; line-height:22px;}
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
  		
<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
		<!--
    	<div class="zt fr"><a style="text-decoration:none;" class="bukong-a" href="javascript:showStandard();">卡口运行数据</a></div>
    	-->
		<div id="bukongMapBar" class="bukong fr">
			<a class="bukong-a" href="#" style="padding-left: 3px;" onclick="showGlobalEyeList();">
				<span id="bukongMapBarName">全球眼</span>
				<#--<span>-->
					<#--<img style="vertical-align:middle;" src="http://static.bug.aishequ.org/images/map/gisv0/special_config/images/xiala.png">-->
				<#--</span>-->
			</a>
			<div id="controlApplyList" class="clear bukong-appply-list hide">
				<ul>
					<li class="bukong-selected" onclick="showGlobalEyeList();">查看全球眼</li>
                    <#--showGlobalEye()-->
				</ul>
			</div>
		</div>
	</div>
    <div class="ztIcon AlphaBack titlefirstall" style="display:block;">
        <div class="title">
			<span class="fr" onclick="CloseX()">
				<img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/closex.png" />
			</span>
			<!--
			<span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()">
				<img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/search.png" />
			</span>
			-->
            <span class="fr" onclick="showGlobalEyeList()">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/refresh.png" />
			</span>
			<div id="titlePath" name="titlePath">全球眼</div>
		</div>
    </div>
    <div class="NorList AlphaBack">
        <iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="520px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
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
<script type="text/javascript" src="${rc.getContextPath()}/js/message/video_surveillance_msg.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/xdate.js"></script>
<script type="text/javascript">
var divDrag = {
	kxWinObj: null,
	zbWinObj: null
};

function CloseX(){
    $(".titlefirstall").hide();
    $("#get_grid_name_frme").hide();
}

var test1="tets";
$(function(){
    window.focus();
 	modleopen();
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

 	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
	 }


//    $("#bukongMapBar").hover(function() {
//        $("#controlApplyList").show();
//    }, function() {
//        $("#controlApplyList").hide();
//    });
    showGlobalEyeList();
});

function showStandard(){
    showMaxJqueryWindow("卡口运行数据", url, 800, 400, true);

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


//图片查看器回调
// function ffcs_viewImg(fieldId){
// 	var sourceId = fieldId + "_Div";
// 	var imgDiv = $("#"+sourceId+"");
// 	imgDiv.find('.fancybox-button').eq(0).click();
// }
	
	function showGlobalEyeList(){
        clearGridAdminTrajectoryLayer();
        //clearMyLayer();
        $(".titlefirstall").show();
        $("#get_grid_name_frme").show();
        $("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-60);
        $("#searchBtnId").show();
        $("."+nowAlphaBackShow).hide();
        $(".NorList").show();

		var _mapt = "5";
		if (typeof MMApi != "undefined") {
			_mapt = MMGlobal.MapType;
		} else {
			_mapt = currentArcgisConfigInfo.mapType;
		}
		var menuListUrl = "/zhsq/alarm/videoSurveillanceController/globalEyes.jhtml?";
		if(menuListUrl.indexOf('http://')<0){
			menuListUrl = js_ctx + menuListUrl;
		}
		menuListUrl += "&orgCode="+$("#orgCode").val();

		var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+
				'</form>';
		document.getElementById('get_grid_name_frme').contentWindow.document.write(html);
		document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
	}


</script>
</html>
