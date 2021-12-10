<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title> 
	<#include "/map/arcgis/arcgis_base/gis_stat/gis_common.ftl" />
</head>
<body style="width:100%;height:100%;border:none;" >
<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
	<input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
	<input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
	<input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" />
	<input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
	
	<input type="hidden" name="gridName" id="gridName" value="${gridName}" />
	<input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
	<input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
	<input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
</form>
<div class="citymainer">
<div class="">
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
	    	<li class="ClearMap" onclick="clearMyLayer();"></li>
			<li class="ThreeWei" onclick="threeWeiClick();"></li>
	    </ul>
		<div id="mapStyleDiv" class="MapStyle" style="display:none">
			<span class="current">二维图</span>
			<span>三维图</span>
			<span>卫星图</span>
		</div>
	</div>
<!-- baseDataTabs end -->
    
    <div class="rk-main">
    
      <div class="ssicon"></div>
      
      <div class="rk-main-r">
        <div class="rk-box1" style="width:200px;">
          <div class="sjgl-tit">统计</div>
          <div class="box1-menu">
          <ul id="menuUl">
            
          </ul>
          </div><!----end .box1-menu--->
        </div><!----end .rk-box1--->
        
        <div id="lineDiv1" class="rk-line" style="display:none;"></div>
        <div id="boxDiv1" class="rk-box1" style="display:none;width:380px;">
           
           <div id="sjiconDiv1" class="sj-icon1"></div>
           <iframe id="stat_frme" name="stat_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
        </div><!----end .rk-box1--->
        
        <div class="rk-line" style="display:none;"></div>
        <div class="rk-box1" style="display:none;">
            <div class="sj-icon1"></div>
            <h3>年龄段分布<span class="sjgl-dw">（单位：万人）</span></h3>
            <p><img src="${rc.getContextPath()}/theme/arcgis/gisstat/images/rk_img6.png" width="287" height="346" /></p>
        </div><!----end .rk-box1--->
      </div><!----end .rk-main-r--->
    </div><!----end .rk-main--->
    
    
    
  </div> <!----end .zdsxcon--->
</div><!----end .citymainer--->
</body>
<script src="${rc.getContextPath()}/js/map/arcgis/gis_stat/function.js"></script> 
<script type="text/javascript">

var test1="tets";
 $(function(){
 	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",$(document).width());
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",$(document).width());
	/*
	$(".rk-main").css("height",$(document).height()-100);
	$(".rk-main-r").css("height",$(document).height()-100);
	*/
 	setTimeout(function(){
	 	getArcgisInfo();
	 	getLayerMenuInfo();
 	},100); 
 	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",$(document).width());
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",$(document).width());
		/*
		$(".rk-main").css("height",$(document).height());
		$(".rk-main-r").css("height",$(document).height());
		*/
	 }
 	
 })
function getArcgisDataByCurrentSet(){
/*
	var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
	getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,level);
	*/
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
		    	switchArcgis(arcgisConfigInfos[0],0)
		    }
		 }
	 });
}
function getLayerMenuInfo(){
	var orgCode = $("#orgCode").val();
	var homePageType = $("#homePageType").val();
	$.ajax({ 
		 url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeByRoot.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 data: { socialOrgCode:orgCode,homePageType:homePageType,gdcId:6},
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
		 },
		 success: function(data){
		    gisDataCfg=eval(data.gisDataCfg);
		    if(gisDataCfg != null){
		    	var htmlStr = "";
		    	var menuUlObj = document.getElementById("menuUl");
			    var ulFirstallList = gisDataCfg.childrenList;
			    for(var i=0; i<ulFirstallList.length; i++){
			    	htmlStr +=  "<li class=\""+ulFirstallList[i].className+"\" ><a href=\"javascript:void(0)\" onclick=\""+ulFirstallList[i].callBack+"\">"+ulFirstallList[i].menuName+"</a><span class=\"menuIcon\"><a href=\"javascript:void(0)\" onclick=\"classificationClick('"+ulFirstallList[i].elementsCollectionStr+"')\"><img src=\"${rc.getContextPath()}/theme/arcgis/gisstat/images/sjgl_icon8.png\" width=\"7\" height=\"11\" /></span></li>";
			    	if(i == 0) {
			    		htmlStr += "<ul id=\""+ulFirstallList[i].menuCode+"\">";
			    		var menuCode = analysisOfElementsCollection(ulFirstallList[i].elementsCollectionStr,"menuCode");
			    		currentMenuCode = menuCode;
			    	}else {
			    		htmlStr += "<ul  id=\""+ulFirstallList[i].menuCode+"\" style=\"display:none;\">";
			    	}
		    		var secondHtmlStr = "";
		    		var ulSecondList = ulFirstallList[i].childrenList;
		    		if (ulSecondList != null) {
		    			for(var j=0;j<ulSecondList.length;j++){
			    			var title = ulSecondList[j].menuName;
			    			secondHtmlStr +=  "<li><a href=\"javascript:void(0)\" onclick=\""+ulSecondList[j].callBack+"\"><img src=\"${rc.getContextPath()}"+ulSecondList[j].largeIco+"\" width=\"5\" height=\"5\" />&nbsp;"+title+"</a></li>";
			    		}
		    		}
		    		htmlStr += secondHtmlStr + "</ul>";
			    }
			    menuUlObj.innerHTML = htmlStr;
			    
		    }
		    
		 }
	 });
}
</script>
</html>
