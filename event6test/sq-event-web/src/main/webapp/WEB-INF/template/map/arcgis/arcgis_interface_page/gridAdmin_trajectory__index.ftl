<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>网格员轨迹展示页面</title>
	<#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
    <link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
	<script src="${ANOLE_COMPONENT_URL}/js/components/popWindow/jquery.anole.popWindow.js"></script>
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/migration_map.css">
	<script type="text/javascript">

	</script>

</head>
<body style="width:100%;height:100%;border:none;" >
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}<#elseif girdInfo?? && gridInfo.gridId??>${gridInfo.gridId}</#if>" />
    <input type="hidden" name="gridAdminId" id="gridAdminId" value="<#if gridAdminId??>${gridAdminId?c}</#if>" />
    <input type="hidden" name="queryDay" id="queryDay" value="<#if queryDay??>${queryDay!''}</#if>" />
    <input type="hidden" name="startTime" id="startTime" value="<#if startTime??>${startTime!''}</#if>" />
    <input type="hidden" name="endTime" id="endTime" value="<#if endTime??>${endTime!''}</#if>" />
    <input type="hidden" name="currentTime" id="currentTime" value="<#if currentTime??>${currentTime!''}</#if>" />
    <input type="hidden" name="showDetailFlag" id="showDetailFlag" value="<#if showDetailFlag??>${showDetailFlag!''}</#if>" />

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


</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
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

	 	getArcgisInfo(function() {// 地图初始化完毕
	 		loadArcgisDataGrid();
	 		locateCenterAndLevelJiangxi($("#gridId").val(),currentArcgisConfigInfo.mapType);

			var gridAdminId = $("#gridAdminId").val();
			var queryDay = $("#queryDay").val();
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
			var showDetailFlag = $("#showDetailFlag").val();
            if(typeof showDetailFlag != 'undefined' && showDetailFlag=="true") {
                if (typeof gridAdminId != 'undefined' && gridAdminId != null && gridAdminId != "") {
                    showGridAdminInfo($("#gridAdminId").val(), queryDay, startTime, endTime);
                }
            }else{
				trajectoryQuery();
			}

	 	});

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
 });

function getArcgisDataByCurrentSet(){
	var glns = $("input[name='gridLevelName']");
	for(var i=0; i<glns.length; i++) {
		if(glns[i].checked == true){
			var idval = $(glns[i]).attr("id");
			var l = parseInt(idval.substring("gridLevelName".length,idval.length));
			getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,l);
		}
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




function showGridAdminInfo(gridAdminId, queryDay, startTime, endTime){
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+gridAdminId;
	if(typeof queryDay != 'undefined'){
		url = url + '&queryDay='+queryDay;
	}
	if(typeof startTime != 'undefined'){
		url = url + '&startTime='+startTime;
	}
	if(typeof endTime != 'undefined'){
		url = url + '&endTime='+endTime;
	}
	url = url + '&play=true';//立即播放
	var params = {
		title: "人员详情表",
		targetUrl: url,
		height: 365,
		width: 430,
		top: 32,
		left: 64,
		modal: false,
		collapsible: true,
		resizable: false
	}
	closeMaxJqueryWindow();//关闭前一次打开的窗口
	showMaxJqueryWindowByParams(params);
}

 /*****

  *****/
 var current = $("#currentTime").val();
 current = Date.parse(current.replace(/-/g,"/"));
 var currentDate = new Date(current); // 当前时间



 var currentYear = currentDate.getFullYear();//当前年份
 var currentMonth = currentDate.getMonth() + 1;// 当前月份

 var currentDate2 = new Date(currentYear, currentMonth, 0);
 var lastDate = currentDate2.getDate(); // 当前月最后一天

 var dateLine = new Date();
 dateLine.setFullYear(currentYear);
 dateLine.setMonth(currentMonth - 1);
 dateLine.setDate(lastDate);

 var jsJiangYinFlag = "<#if jsJiangYinFlag??>${jsJiangYinFlag}<#else>false</#if>";

 // 获取三个月前的时间
 function get3MonthBefor(currentDate){
     var resultDate,year,month,date,hms;
     year = currentDate.getFullYear();
     month = currentDate.getMonth()+1;
     date = 1;
     switch(month)
     {
         case 1:
         case 2:
             month += 10;
             year--;
             break;
         default:
             month -= 2;
             break;
     }

     month = (month < 10) ? ('0' + month) : month;
     resultDate = year + '/' + month +'/' +date;
     return new Date(resultDate);
 }

 function trajectoryQuery(){
     var start = $("#startTime").val();
     var end = $("#endTime").val();
	 var mobileTelephone = "<#if girdAdminInfo?? && girdAdminInfo.mobileTelephone??>${girdAdminInfo.mobileTelephone}</#if>";
     var imsi = "<#if girdAdminInfo?? && girdAdminInfo.imsi??>${girdAdminInfo.imsi}</#if>";
     var userId = '<#if girdAdminInfo?? && girdAdminInfo.userId??>${girdAdminInfo.userId}</#if>';

     var tempDate = get3MonthBefor(currentDate);

     if (start > end) {
         alert("开始时间需要小于结束时间");
         return;
     }

     var start2 = Date.parse(start.replace(/-/g,"/"));
     var startDate = new Date(start2); // 开始时间

     var end2 = Date.parse(end.replace(/-/g,"/"));
     var endDate = new Date(end2); // 结束时间
     if (startDate.getTime() < tempDate.getTime()
             || startDate.getTime() > dateLine.getTime()
             || endDate.getTime() > dateLine.getTime()) {
         alert("只能查询三个月内（从" + (tempDate.getMonth() + 1) + "月1号开始）的轨迹数据");
         return;
     }
     var userName = "<#if girdAdminInfo?? && girdAdminInfo.userName??>${girdAdminInfo.userName}</#if>";

     var params = "?locateTimeBegin="+start+"&imsi="+imsi+"&mobileTelephone="+mobileTelephone+"&userId="+userId+"&userName="+userName+"&jsJiangYinFlag="+jsJiangYinFlag+"&t="+Math.random();

	 var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataTrajectoryOfGridAdmin.jhtml"+params;
	 getTrajectoryOfGridAdmin(url,start,end);
 }
 
 function loadArcgisDataGrid(){
 	var url =  '/zhsq_event/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+$("#gridId").val();
	$("#map0").ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS,null,null,true);
 }

</script>
</html>
