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
	<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/zhoubian_index.js"></script>
	<link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
	<script src="${ANOLE_COMPONENT_URL}/js/components/popWindow/jquery.anole.popWindow.js"></script>
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/migration_map.css">
	<link href="${rc.getContextPath()}/css/kuangxuan.css" rel="stylesheet" type="text/css" />
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

		.textFont{text-shadow:#000 1px 0 0,#000 0 1px 0,#000 -1px 0 0,#000 0 -1px 0;}
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


<div class="MapBar">
    <div class="con AlphaBack">
    	<#include "/map/arcgis/arcgis_base/top.ftl">
        <div class="labtop fl" style=""></div><#--头部已选择-->
    </div>

    <div class="ztIcon AlphaBack titlefirstall" style="top: 32px; display: block;">
        <div class="title">
			<div id="titlePath" name="titlePath">专题图层</div>
		</div>
    </div>
    <div class="ztIcon AlphaBack dest firstall" style="display: block;">
        <ul id="ulFirstall" style="max-height: 564px;">
            <div class="h_20"></div>
            <li class="PrinkBg" onclick="getBizUrl('pipeLine')" title="管道">
				<p class="pic">
					<img src="http://static.fjsq.org/images/map/gisv0/special_config/images/icon37.png">
				</p>
                <p class="word AlphaBack">管道</p>
			</li>

		</ul>
		<div class="clear"></div>
	</div>

    <div class="NorList AlphaBack">
        <iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
    </div>
</div>

</body>
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script type="text/javascript">

 $(function(){
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
		document.getElementById("gridLevelName"+level).checked = true;

	 	getArcgisInfo(function() {// 地图初始化完毕
	 		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	 	});

	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	
	 	modleclose();

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
 });

var x;
var y;

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
			//getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,l);
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
			$("#mapStyleDiv").width(60*arcgisConfigInfos.length+8);
			if(htmlStr!=""){
				currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
			}
			if(arcgisConfigInfos.length > 0) {
				loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap",backFn);
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
	if (typeof window.frames['get_grid_name_frme'].clearMyLayerB == "function") {
		window.frames['get_grid_name_frme'].clearMyLayerB();
	}
}

function clearMyLayerB(param) {
	clearMyLayer();
	clearMyLayerA();
	if (currLayerOpenIndex && param == undefined) {
		layer.close(currLayerOpenIndex);
		$("#wrap_media").hide();
		getOrgBack();
		clearSpecialLayer('EVENT_STATISTICS');
		currLayerOpenIndex = undefined;
		eventHeatMap.clearData();
		$("#NorMapOpenDiv").remove();
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

 function getBizUrl(bizType) {
     $("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
     $("#searchBtnId").show();
     $("."+nowAlphaBackShow).hide();
     $(".NorList").show();

     var menuListUrl = js_ctx+"/zhsq/map/mapTestController/standardPipeLine.jhtml";

     if(bizType == "pipeLine"){
         menuListUrl = js_ctx+"/zhsq/map/mapTestController/standardPipeLine.jhtml";
	 }

	 var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+
			 '</form>';

     var htmlstr = $("#titlePath").html();
     var htmls = htmlstr.split(" &gt; ");
     if(htmlstr.indexOf("管道")<0){
         htmlstr = htmlstr.replace(htmls[0],"<a href='javascript:void(0);' onclick='firstall()'>专题图层</a>")

         // if (htmls.length > 1) {
         //     htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
         // }
         $("#titlePath").html(htmlstr + " > "+"管道");
     }

	 document.getElementById('get_grid_name_frme').contentWindow.document.write(html);
	 document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
 }


function showPipeLines(bizType, linColor){
    if(typeof bizType == 'undefined' || bizType == null || bizType == "" ){
        bizType = "中国电信";
        linColor = "#FD6525";
	}
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/map/mapTestController/pipeLineListData.json?pipeLineKind='+bizType,
		type: 'POST',
		timeout: 3000,
		dataType:"json",
		async: false,
		error: function(data){
			$.messager.alert('友情提示','获取管道信息出现异常!','warning');
		},
		success: function(data){
            var points = [];//二维数组线路
            var lineOpt = [];
			if(data != null && data.length > 0){

			    for(var i=0;i<data.length;i++){
                    var linePoints = [];
                    linePoints.push({x:data[i].startLongitude, y:data[i].startLatitude});
                    linePoints.push({x:data[i].endLongitude, y:data[i].endLatitude});
                    lineOpt.push({lineColor:linColor,lineStyle:'solid', textName : bizType});
                    // if(i==data.length-1){
                    //     linePoints.push({x:data[i].endLongitude, y:data[i].endLatitude});
					// }
                    points.push(linePoints);
				}
			}
            // linePoints = [{x:120.690839,y:33.181859},
			// 	{x:120.171115,y:33.019262},
            //     {x:119.621496,y:32.957241},
            //     {x:118.98219,y:33.203128},
            //     {x:117.935844,y:33.063813}]
            // points.push(linePoints);
            // var lineOpt = [
            //     {lineColor:linColor,lineStyle:'solid', textName : bizType}];//线路样式,一维数组
            $.fn.ffcsMap.clearSimilarLayers({layerName : "pipeLine"});
            $("#map"+currentN).ffcsMap.drawLine("pipeLine",points, lineOpt);
            //$("#map"+currentN).ffcsMap.centerAt({x:points[0][0].x,y:points[0][0].y});


		}

	});
}

//返回专题图层
function firstall(){
	$("#titlePath").html("专题图层");
	$("."+nowAlphaBackShow).hide();
	$(".NorList").hide();
	$(".firstall").show();
}
</script>
</html>
