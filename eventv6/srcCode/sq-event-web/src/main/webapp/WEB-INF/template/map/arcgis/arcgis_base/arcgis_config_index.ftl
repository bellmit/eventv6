<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<title>地图首页</title> 
	<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
	<#include "/component/ImageView.ftl" />
	<script type="text/javascript">
	
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
		<!--<li id="kuangxuanname" class="SelectMap" onclick="arcgisKuangXuan(this);"></li>-->
    	<li class="ClearMap" onclick="clearMyLayer();"></li>
		<li class="ThreeWei" onclick="threeWeiClick();"></li>
		
    	
    	<!--
    	<li class="MapFull"></li>
    	-->
    </ul>
	<div id="mapStyleDiv" class="MapStyle" style="display:none">
		<span class="current">二维图</span>
		<span>三维图</span>
		<span>卫星图</span>
	</div>
</div>
<!-- baseDataTabs end -->

		<!-- 专题地图 -->
  		<form name="gridForm" method="post" action="/admin/grid.shtml" target="_self">
  			<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
  			<input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
  			<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" />
  			<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
  			<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
  			<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
  			<input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
  		</form> 
     <!-----------------------------------框选设置------------------------------------->
    <div class="NorMapOpenDiv2 kuangxuanWindow hide dest" style="bottom:30px; left:60px;">
		<div class="box" style="width:376px;height:343px;">
	    	<div class="title"><span class="fr close" onclick="javascirpt:closeKuangxuan()"></span>选择您想要查看的内容</div>
	        <iframe id="kuangxuanConfig" name="kuangxuanConfig" width="100%" height="100%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/toKuangXuanConfig.jhtml" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
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
        <div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>
        <!-- -->
        <div class="kuangxuan fr"><a href="#"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/kuangxuan.png" />框选统计</a></div>
    	
    	<!--<div class="zhoubian fr"><a href="#"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/kuangxuan.png" />周边资源</a></div>-->
    </div>

    <!-----------------------------------人地事物情------------------------------------->
    <div class="ztIcon AlphaBack titlefirstall" style="display:block;">
		<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/closex.png" /></span><span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/search.png" /></span><div id="titlePath" name="titlePath">专题图层</div></div>
	</div>
	<div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
		<div class="title"><span class="fr" onclick="zhoubianListHide()"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="" onclick="ShowSearchBtnZhouBian()"><img src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
	</div>	
	<div class="ztIcon AlphaBack dest firstall" style="display:block;">
		<ul id="ulFirstall">

		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------人------------------------------------->
	<div class="ztIcon AlphaBack people">
		<ul id="ulPeople">
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------地------------------------------------->
	<div class="ztIcon AlphaBack world">
		<ul id="ulWorld">
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------事------------------------------------->
	<div class="ztIcon AlphaBack metter">
		<ul id="ulMetter">
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
		<ul id="ulThing">
		</ul>
        <div class="clear"></div>
	</div>
    <!-----------------------------------情------------------------------------->
    <div class="ztIcon AlphaBack situation">
		<ul id="ulSituation">
		</ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------组织------------------------------------->
    <div class="ztIcon AlphaBack organization">
		<ul  id="ulOrganization">
		</ul>
        <div class="clear"></div>
	</div>
	<!-----------------------------------精准扶贫------------------------------------->
    	<div class="ztIcon AlphaBack precisionPoverty">
		<ul  id="ulPrecisionPoverty">
		</ul>
        <div class="clear"></div>
	</div>
	
	<div class="NorList AlphaBack">
		<iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
	<div class="NorListZhouBian AlphaBackZhouBian zhoubianList">
		<iframe id="zhoubian_list_frme" name="zhoubian_list_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
	</div>
</div>

</body>
<script src="${rc.getContextPath()}/theme/arcgis/standardmappage/js/function.js"></script> 
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript">

var test1="tets";
 $(function(){
 	modleopen();
 	zhoubianListHide();
 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
 	setTimeout(function(){
 	
 		var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	 	document.getElementById("gridLevelName"+level).checked = true;
	 	if (level-1 >= 5) {
			document.getElementById("buildName0").checked=true;
		}else {
			document.getElementById("buildName0").checked=false;
		}
		eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");//--初始化工作流！
	 	getArcgisInfo();
	 	getLayerMenuInfo();
	 	changeCheckedAndStatus($("#gridLevel").val(),level);
	 	//getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
	 	//getArcgisDataOfBuilds($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType);
	 	locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
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
		$("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
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
		$("#map"+currentN).ffcsMap.clear({layerName : "gridLayer"});
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
	/*if(document.getElementById("buildName0").checked == true) { 
		getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
	}*/
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

	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTree.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		    gisDataCfg=eval(data.gisDataCfg);
		    if(gisDataCfg != null){
		    	var htmlStr = "";
			    var ulFirstallList = gisDataCfg.childrenList;
			    for(var i=0; i<ulFirstallList.length; i++){
			    	htmlStr += "<li class=\""+ulFirstallList[i].className+"\" onclick=\""+ulFirstallList[i].callBack+"\"><p class=\"pic\"><img src=\"${rc.getContextPath()}"+ulFirstallList[i].largeIco+"\" /></p></li>"
			    	var ulFirstall = document.getElementById("ulFirstall");
			    	ulFirstall.innerHTML = htmlStr;
			    	if(ulFirstallList[i].childrenGdcIds != ","){
			    		var secondHtmlStr = "";
			    		var ulSecondList = ulFirstallList[i].childrenList;
			    		for(var j=0;j<ulSecondList.length;j++){
			    			var title = ulSecondList[j].menuName;
			    			secondHtmlStr+="<li class=\""+ulSecondList[j].className+"\" onclick=\""+ulSecondList[j].callBack+"\" title=\""+title+"\">"
										+"<p class=\"pic\"><img src=\"${rc.getContextPath()}"+ulSecondList[j].largeIco+"\"/></p>"
										+"<p class=\"word AlphaBack\">"+ulSecondList[j].menuName+"</p>"
									+"</li>";
			    		}
			    		if(ulFirstallList[i].menuCode == "PEOPLE"){
			    			var ulSecond = document.getElementById("ulPeople");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "WORLD"){
			    			var ulSecond = document.getElementById("ulWorld");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "METTER") {
			    		var ulSecond = document.getElementById("ulMetter");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "THING") {
			    			var ulSecond = document.getElementById("ulThing");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "SITUATION") {
			    			var ulSecond = document.getElementById("ulSituation");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "ORGANIZATION") {
			    			var ulSecond = document.getElementById("ulOrganization");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}else if(ulFirstallList[i].menuCode == "precisionPoverty") {
			    			var ulSecond = document.getElementById("ulPrecisionPoverty");
			    			ulSecond.innerHTML = secondHtmlStr;
			    		}
			    	}
			    }
			    
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
