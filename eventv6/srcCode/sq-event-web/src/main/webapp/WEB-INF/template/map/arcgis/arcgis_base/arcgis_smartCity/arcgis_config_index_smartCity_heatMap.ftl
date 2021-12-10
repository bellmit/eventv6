<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title><!--用于从bi大屏选择日期直接跳转-->
<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
    <link href="${rc.getContextPath()}/css/kuangxuan.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${uiDomain!''}/js/echarts/echarts-all.js"></script>
	<style type="text/css">
        .layerSkin{ position:absolute;border-radius:8px; padding:0px 5px 10px 5px;background:rgba(30,64,89,0.75);}
        .mapbox{ cursor: pointer;background:#fff; border:2px solid #2ab7b2; padding:5px; position:relative; min-width:30px; display:block; white-space:nowrap;}
		.mapicon{ display:inline-block;margin-right:3px;}
		.maparrow{ background:url(${uiDomain!''}/images/map_tree_arrow.png) no-repeat; width:9px; height:6px; position:absolute; left:10px; bottom:-8px;}
	</style>
</head>
<body style="width:100%;height:100%;border:none;" class="npmap">
<div class="npcityMainer">
	
    	<div class="wrap_media_con" id="wrap_media" style="position:absolute;left:500px;z-index:9;display:none;top:10px;">
	<input type="hidden" id="minID">
	<input type="hidden" id="maxID">
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
  			<input type="hidden" name="keyPopEcs" id="keyPopEcs" value="${keyPopEcs!''}" />
  			<input type="hidden" name="partyMemberEcs" id="partyMemberEcs" value="${partyMemberEcs!''}" />
  			<input type="hidden" name="param_date" id="param_date" value="${PARAM_DATE!''}" />
  		</form>
    <div class="npcityBottom" id="npcityBottom" style="display:none;">
        <div class="clearfloat"></div>
    </div><!--end .npcityBottom-->

</div><!--end .npcityMainer-->
</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">
var lastDateNo=0;//热力图列表展示新列表后删除 T_EVENT_KUANGXUAN 旧数据
 $(function(){
     window.focus();
     modleopen();
	var domH = $(document).height(),domW = $(document).width();
 	$("#map0").css({"height":domH,"width":domW});
 	$("#map1").css({"height":domH,"width":domW});
 	$("#map2").css({"height":domH,"width":domW});
	$("#wrap_media").css("left",(document.body.offsetWidth-604)/2);
 	setTimeout(function(){
	 	getArcgisInfo(function() {
			getLayerMenuInfo();
	 	});
	 	modleclose();
 	},100);
 	window.onresize=function(){
		domH = $(document).height(),domW = $(document).width();
		$("#map0").css({"height":domH,"width":domW});
		$("#map1").css({"height":domH,"width":domW});
		$("#map2").css({"height":domH,"width":domW});
	 };
 });



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
	getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,Number($("#gridLevel").val())+1);
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
		    	}else {
		    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
				}
		    }
		    var mapStyleDiv = document.getElementById("mapStyleDiv");
		    mapStyleDiv.innerHTML = htmlStr;
		    $("#mapStyleDiv").width(60*arcgisConfigInfos.length+8);

		    if(htmlStr!=""){
		    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0];
		    }

		    if(arcgisConfigInfos.length > 0) {
		    	loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap",backFn);
		    }
		 }
	 });
}
function getLayerMenuInfo(){
    var orgCode = $("#orgCode").val();
    var homePageType = $("#homePageType").val();
	$.ajax({
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.jhtml',
		 type: 'POST',
		 data: { orgCode:orgCode,homePageType:homePageType,gdcId:4,isRootSearch:1},
		 dataType:"json",
		 async: false,
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
					if(ulFirstallList[0].menuCode="EVENT_STATISTICS"){
						setTimeout(function(){
							eval(ulFirstallList[0].callBack);
						},1000);
					}
                } else {
					$.messager.alert('友情提示','地图首页内容未配置，请联系管理员!','info');
				}
			}
		 }
	});
}


var mapMenu = new HashMap();
function getSubMenuInfo(elementsCollectionStr){
	var gdcId = analysisOfElementsCollection(elementsCollectionStr,"gdcId");
    var orgCode = $("#orgCode").val();
    var homePageType = $("#homePageType").val();
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
				var npcitybmenuHTML = "";
                for(var i=0; i<ulFirstallList.length; i++){
                    npcitybmenuHTML += "<dd><a href='#' onclick='showStatisticsMsg(\""+ulFirstallList[i].elementsCollectionStr+"\")'>"+ulFirstallList[i].menuName+"</a></dd>"
                }
                npcitybmenuHTML = "<div class='npcitybmenu citybgbox' id='npcitybmenu' style='max-height: 465px;overflow: hidden;top:-"+(48*ulFirstallList.length)+"px;'><dl>"+npcitybmenuHTML+"</dl></div>";
                layer.tips(npcitybmenuHTML, '#npcityBottomLi_'+gdcId, {
                    tips: [1, 'rgba(19,55,81,0.5)'], //还可配置颜色
                    time: 0,
                    shade: 0.1,
                    shadeClose: true,
                    closeBtn:0
                });
            }
        }
    });
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
var currLayerOpenIndex,lastMenuCode,eventHeatMap;
var currElementsCollectionStr = "";
function showStatisticsMsg(elementsCollectionStr) {
    var winHeight=document.getElementById('map'+currentN).offsetHeight-100;
    currElementsCollectionStr = elementsCollectionStr;
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var menuListUrl = eclist["menuListUrl"];
    var menuName = eclist["menuName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
    var menuSummaryHeight = eclist["menuSummaryHeight"];
    var menuCode = eclist[ "menuCode"];
    var menuLayerName = eclist["menuLayerName"];
    var offset = '10px';
    if ( menuCode == 'VIDEO_EQUIPMENT' || menuCode == 'urbanObject' ||menuCode == 'ORG_PRODUCTION' || menuCode == 'URBAN_COMPONENTS') {
    	menuSummaryWidth = 270;
    	menuSummaryHeight = 550;
    	offset = 'rt';
		if(menuCode == 'ORG_PRODUCTION'){
	    	menuSummaryHeight = document.documentElement.clientHeight - 20;
		}
    }
    if(menuCode == 'EVENT_STATISTICS'){
    	offset = 'rt';
		$.fn.ffcsMap.getMap().getLayer("gridLayer").onClick=function(){
		};
	}
    if(menuListUrl.indexOf('http://')<0){
        menuListUrl = js_ctx + menuListUrl;
    }
    if(menuListUrl.indexOf("?")<=0){
        menuListUrl += "?";
    }
	var gridId = $("#gridId").val();
	menuListUrl += "&date="+$("#param_date").val()+"&gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&orgCode="+$("#orgCode").val()+"&infoOrgCode="+$("#orgCode").val()+"&elementsCollectionStr="+encodeURIComponent(elementsCollectionStr);
//    if(menuListUrl.indexOf("?")<=0){
//        menuListUrl += "?t="+Math.random();
//    }
//    menuListUrl += "&gridId="+$("#gridId").val()+"&noneGetLayer=true";
    layer.closeAll('tips');
	if(typeof currLayerOpenIndex != 'undefined' && currLayerOpenIndex != null){
        layer.close(currLayerOpenIndex);
		if( lastMenuCode == 'ORG_PRODUCTION'){getOrgBack();}
		if( lastMenuCode == 'EVENT_STATISTICS'){
			$("#wrap_media").hide();getOrgBack();
			eventHeatMap.clearData();
			clearSpecialLayer('EVENT_STATISTICS');
			$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:245},"gridLayer",'网格',getInfoDetailOnMap, showGridDetail);
		}
	}
    if(typeof AUTOMATIC_CLEAR_MAP_LAYER != 'undefined' && "1" == AUTOMATIC_CLEAR_MAP_LAYER) {
        clearMyLayer();
    }
	lastMenuCode= menuCode;
    currLayerOpenIndex = layer.open({
		id: 'statisticsMsg'+menuCode,
		closeBtn:'1',
        type: 2,
		title: [menuName+"<div><select><option>1</option><option>2</option></select></div>", 'background-color: rgba(19,55,81,0); border-bottom : 0px solid rgba(19,55,81,0.8); color: #F8F8F8; font-size: 20px;'],
        shadeClose: false,
        shade: 0,
		offset: offset,
        skin: 'layerSkin',
        area: [menuSummaryWidth+'px', menuSummaryHeight+'px'],
//        area: ['1084px', '482px'],
        content: menuListUrl,
        cancel : function(index, layero) {
			if( lastMenuCode == 'ORG_PRODUCTION'){
				getOrgBack();
				layer.close(currLayerEchartIndex);
				lastMenuCode = null;
			}
			if( 'EVENT_STATISTICS' == menuCode){
				$("#wrap_media").hide();getOrgBack();
				eventHeatMap.clearData();
				clearSpecialLayer('EVENT_STATISTICS');
				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:245},"gridLayer",'网格',getInfoDetailOnMap, showGridDetail);
				lastMenuCode = null;
			}
        }, success: function(layero, index){
			if(eclist["remark"] && eclist["remark"] != 'null'){
				var title = $(".layui-layer-title");
				var html = title.html();
				title.html(html+"<span style='font-family:宋体;font-size:18px;color:rgb(184, 186, 182);'>"+eclist["remark"]+"<span>");
			}
            clearMyLayerB()
		}
    });
}

 /**
  * 撒点定位
  * @param locationObj  定位数据对象
  */
function locationPointsOnMap(locationObj){
     var locationMaplist = null;//定位数据对象
	 var mapLocationDataInfoUrl = null;//获取定位数据对象
	 var clickIconFlag = false;//是否点击图标显示信息

     var layerName = analysisOfElementsCollection(currElementsCollectionStr,"menuLayerName");
     currentLayerName = layerName;
     if("1" != IS_ACCUMULATION_LAYER){
         clearSpecialLayer(layerName);
     }

	 if(typeof locationObj != 'undefined' && locationObj != null){
		 if(locationObj.clickIconFlag != 'undefined' && locationObj.clickIconFlag != null){
             clickIconFlag = locationObj.clickIconFlag;
		 }else{
             clickIconFlag = false;
		 }

		 if(typeof(locationObj.locationMaplist) != 'undefined' && locationObj.locationMaplist != null){
             locationMaplist = locationObj.locationMaplist;

			 //只有一个点的时候，地图把中心点移到这个点
             if(locationMaplist.length == 1){
                 $("#map"+currentN).ffcsMap.centerAt({
                     x : locationMaplist[0].x,          //中心点X坐标
                     y : locationMaplist[0].y           //中心点y坐标
                 });
			 }

			 //撒点，在地图上添加定位图标
             if(currElementsCollectionStr != "") {
                 currentLayerLocateFunctionStr = "getArcgisDataOfZhuanTi('" + undefined + "','" + currElementsCollectionStr + "')";
                 getArcgisDataOfZhuanTi(undefined, currElementsCollectionStr, 320, 170, undefined, undefined, undefined, false, locationMaplist,clickIconFlag);
             }
		 }else if(typeof(locationObj.mapLocationDataInfoUrl) != 'undefined' && locationObj.mapLocationDataInfoUrl != null){
             mapLocationDataInfoUrl = locationObj.mapLocationDataInfoUrl;

             //撒点，在地图上添加定位图标
             if(currElementsCollectionStr != "") {
                 currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+mapLocationDataInfoUrl+"','"+currElementsCollectionStr+"')";
                 getArcgisDataOfZhuanTi(mapLocationDataInfoUrl,currElementsCollectionStr,320,170, undefined, undefined, undefined, false, undefined,clickIconFlag);
             }
		 }else{
             layer.open({
                 title: '提示',
                 content: '无法获取定位信息!'
             });
		 }

	 }else{
         layer.open({
             title: '提示',
             content: '无法获取定位信息!'
         });
	 }

}

function clearMyLayerB() {
	clearMyLayer();
}


</script>
</html>
