window.onload = function(){
/*-------------------------------树形列表显示隐藏-------------------------------------*/
	$(".local li").click(function() {
			
			var div = $(".SelectTree");
			if(div.hasClass("dest")) {
					div.removeClass("dest").animate({top: 32},10, function(){
						$(".SelectTree").show();
					});
			} else {
					div.addClass("dest").animate({top: 32}, 10, function(){
						$(".SelectTree").hide();
					});
			}
	});
	
	/**台江地图**/
	$(".location li").click(function() {
			
			var div = $(".SelectTree");
			if(div.hasClass("dest")) {
					div.removeClass("dest").animate({top: 50, left: 75},10, function(){
						$(".SelectTree").show(500);
					});
			} else {
					div.addClass("dest").animate({top: 50, left: 75}, 10, function(){
						$(".SelectTree").hide(500);
					});
			}
	});
/*--------------------------------------------------------------------*/
/*-------------------------------专题图层列表高度自适应-------------------------------------*/
			setInterval(function(){
						var height=$(window).height();
						$(".ListShow").css("height",height-113);
				},3);
/*--------------------------------------------------------------------*/
/*-------------------------------专题图层显示隐藏-------------------------------------*/

	$(".zhuanti").click(function() {
			var div = $(".titlefirstall");
			if(div.hasClass("dest")) {
					div.removeClass("dest").animate({top: 32}, 10, function(){
						$(".titlefirstall").show();
						$("."+nowAlphaBackShow).show();
					});
			} else {
					div.addClass("dest").animate({top: 32}, 10, function(){
						$(".titlefirstall").hide();
						$("."+nowAlphaBackShow).hide();
					});
			}
	});
/*--------------------------------------------------------------------*/


/*--------------------------层级选项------------------------------------------*/
	$(".MapLevel li").click(function() {
		var div = $(".SelectTree2");
		if(div.hasClass("dest")) {
			div.removeClass("dest").animate({top: 32}, 10, function(){
				$(".SelectTree2").show();
			});
		} else {
			div.addClass("dest").animate({top: 32}, 10, function(){
				$(".SelectTree2").hide();
			});
		}
	});
/*--------------------------------------------------------------------*/
/*-----------------------------框选----------------------------------*/

	var $NavDiv = $(".kuangxuantongji .nav ul li");
	$NavDiv.click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		var NavIndex = $NavDiv.index(this);
		$(".tabss").children().eq(NavIndex).show().siblings().hide();
	});
	$(function(){
		$(".kuangxuan").click(function() {
			var div = $(".kuangxuanWindow");
			if($("#kuangxuanConfig").attr("src") == "") {
				$("#kuangxuanConfig").attr("src",js_ctx+"/zhsq/map/arcgis/arcgisdata/toKuangXuanConfig.jhtml?homePageType="+$("#homePageType").val()+"&socialOrgCode="+$("#socialOrgCode").val());
			}
			if(div.hasClass("dest")) {
					div.removeClass("dest");
					$(".kuangxuanWindow").toggle();
					showKuangxuanHs();
			} else {
					div.addClass("dest");
					kuangxuanCallBackOnClose1();
					$(".kuangxuanWindow").toggle();
			}
		})
	});
	
	/*----------------------------周边资源z----------------------------------*/

}


function closeKuangxuan() {
	$(".kuangxuan").click();
	kuangxuanCallBackOnClose1();
}
function showZhoubian(){
	var div = $(".zhoubianWindow");
	if(div.hasClass("dest")) {
			div.removeClass("dest");
			$(".zhoubianWindow").toggle();
	}
}
function closeZhoubian() {
	toHideZhouBianSketch();
	var div = $(".zhoubianWindow");
	div.addClass("dest");
	$(".zhoubianWindow").toggle();
}

var nowAlphaBackShow = "firstall";
function firstall(){
	$("#titlePath").html("专题图层");
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	$(".firstall").show();
	nowAlphaBackShow = "firstall";
	currentLayerListFunctionStr="";
}
var currentClassificationFuncStr = "";//用于记录当前
function classificationClick(elementsCollectionStr){
	getChildListOfLayer(elementsCollectionStr);
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	var callBack = analysisOfElementsCollection(elementsCollectionStr,"callBack");
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > "+menuName);
	$(".ztIcon ul").css("max-height",document.getElementById('map'+currentN).offsetHeight-62);
	$("."+menuCode).show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = menuCode;
	currentLayerListFunctionStr="";
	currentClassificationFuncStr=callBack+"(\""+elementsCollectionStr+"\")";
}

function getChildListOfLayer(elementsCollectionStr) {
	var gdcId = analysisOfElementsCollection(elementsCollectionStr,"gdcId");
	var orgCode = analysisOfElementsCollection(elementsCollectionStr,"orgCode");
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	var homePageType = $("#homePageType").val();
	var ulSecond=null;
	if(menuCode == "people"){
		ulSecond = document.getElementById("ulPeople");
	}else if(menuCode == "world"){
		ulSecond = document.getElementById("ulWorld");
	}else if(menuCode == "metter") {
		ulSecond = document.getElementById("ulMetter");
	}else if(menuCode == "thing") {
		ulSecond = document.getElementById("ulThing");
	}else if(menuCode == "situation") {
		ulSecond = document.getElementById("ulSituation");
	}else if(menuCode == "organization") {
		ulSecond = document.getElementById("ulOrganization");
	}else if(menuCode == "precisionPoverty") {
		ulSecond = document.getElementById("ulPrecisionPoverty");
	}
	if(ulSecond.innerHTML=="") {
		$.ajax({ 
		 url: js_ctx+'/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 300000,
		 data: { orgCode:orgCode,homePageType:homePageType,gdcId:gdcId,isRootSearch:0},
		 dataType:"json",
		 async: true,
		 error: function(data){
		 	$.messager.alert('友情提示','专题图层信息获取出现异常!','warning');
		 },
		 success: function(data){
		    gisDataCfg=eval(data.gisDataCfg);
		    var secondHtmlStr = "";
		    if(gisDataCfg != null){
			    var ulSecondList = gisDataCfg.childrenList;
		    	if(ulSecondList != null && ulSecondList.length>0){
		    		for(var j=0;j<ulSecondList.length;j++){
		    			var title = ulSecondList[j].menuName;
		    			secondHtmlStr+="<li class=\""+ulSecondList[j].className+"\" onclick=\""+ulSecondList[j].callBack+"\" title=\""+title+"\">"
									+"<p class=\"pic\"><img src=\""+uiDomain+ulSecondList[j].largeIco+"\"/></p>"
									+"<p class=\"word AlphaBack\">"+ulSecondList[j].menuName+"</p>"
								+"</li>";
		    		}
		    		ulSecond.innerHTML = "<div class=\"h_20\"></div>"+secondHtmlStr;
		    	}
		     }
		  }
	  });
	}
}

function analysisOfElementsCollection(elementsCollectionStr,elementsName){
	var ecs = elementsCollectionStr.split(",_,");
	var eclist = new Array();
	for(var i=0;i<ecs.length;i++){
		var e = ecs[i].split("_,_");
		if(elementsName == e[0]){
			return e[1];
		}
	}
	return "";
}



function ShowSearchBtn(){
	window.frames['get_grid_name_frme'].ShowOrCloseSearchBtn();
}
function CloseSearchBtn(){
	window.frames['get_grid_name_frme'].CloseSearchBtn();
}
function ShowSearchBtnZhouBian(){
	window.frames['zhoubian_list_frme'].ShowOrCloseSearchBtn();
}
function CloseSearchBtnZhouBian(){
	window.frames['zhoubian_list_frme'].CloseSearchBtn();
}
function CloseX(){
	$(".zhuanti").click();
}

function showFactorObject(elementsCollectionStr) {
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	eval(menuCode+"Factor()");
}	

//网格
function gridFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HC_WG/MapServer/1";
	//ykFeatureUrl="http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StatesCitiesRivers_USA/MapServer/0";
	//ykFeatureUrl = "http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Demographics/ESRI_Census_USA/MapServer/5";
	featureHide("grid"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"grid"+pointLayerNum,ykFeatureUrl,2,true,null,null,null,null,true);//
	fea.show("grid"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},"grid"+pointLayerNum,'位置'); 
	
}

var pointLayerNum = 0;
//安监企业
function safetyEnterpriseFactor(){
	
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/3";
	featureHide("safetyEnterprise"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"safetyEnterprise"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,true);//
	fea.show("safetyEnterprise"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:275},"safetyEnterprise"+pointLayerNum,'位置'); 
	
}
//加油站
function gasStationFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/3";
	featureHide("gasStation"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"gasStation"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,stationDetail);
	fea.show("gasStation"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:600,h:410},"gasStation"+pointLayerNum,'位置',stationDetail,null); 
}
//危化企业
function chemicalEnterpriseFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/0";
	featureHide("chemicalEnterprise"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"chemicalEnterprise"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,stationDetail);
	fea.show("chemicalEnterprise"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:600,h:410},"chemicalEnterprise"+pointLayerNum,'位置',stationDetail,null); 
}
//油库
function oilDepotFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/2";
	featureHide("oilDepot"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"oilDepot"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,stationDetail);
	fea.show("oilDepot"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:600,h:410},"oilDepot"+pointLayerNum,'位置',stationDetail,null); 
}
//重大危险源
function dangerSourceFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/1";
	featureHide("dangerSource"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"dangerSource"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,stationDetail);
	fea.show("dangerSource"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:600,h:410},"dangerSource"+pointLayerNum,'位置',stationDetail,null); 
}

function stationDetail(data) {
	var stationId = data['wid'];
	var url = "";
	var context = "";
	url =  $("#SQ_ZZGRID_URL").val() +'/zzgl/grid/safetyStation/stationDetail.jhtml?stationId='+stationId+'&standard=onMapShow&t='+Math.random();
	context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	return context;
}
//安监队伍
function safetyTeamFactor(){
	ykFeatureUrl = "http://10.2.2.84:6080/arcgis/rest/services/HCAJWG/FeatureServer/4";
	featureHide("safetyTeam"+pointLayerNum);
	pointLayerNum++;
	$.fn.ffcsMap.InstanceFeature();
	var queryCondition = "1=1";
	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	fea.render(queryCondition,"safetyTeam"+pointLayerNum,ykFeatureUrl,0,true,null,null,null,null,false,safetyTeamDetail);
	fea.show("safetyTeam"+pointLayerNum,$("#map"+currentN).ffcsMap.getMap());
	$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},"safetyTeam"+pointLayerNum,'位置',safetyTeamDetail,null); 
}
function safetyTeamDetail(data) {
	var teamId = data['wid'];
	var url = "";
	var context = "";
	url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksDetail.jhtml?teamId='+teamId+'&t='+Math.random();
	context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	return context;
}

//显示重点人口信息
function showListPanel(url){
    $("#get_grid_name_frme").attr("src",url); 
}

//选择树的回调函数
function gridTreeClickCallback(gridId,gridName,orgId,orgCode,gridInitPhoto,gridLevel,gridCode){
    $(".SelectTree").css("display","none");
    if(gridCode != $("input[name='gridCode']").val()) {
    	$("#changeGridName").text(gridName);
	    $("input[name='gridId']").val(gridId);
	    $("input[name='gridCode']").val(gridCode);
		$("input[name='gridName']").val(gridName);
		$("input[name='gridLevel']").val(gridLevel);
		$("input[name='orgCode']").val(orgCode);
		var level = (parseInt(gridLevel) < 6) ? parseInt(gridLevel)+1 : parseInt(gridLevel);
		//document.getElementById("gridLevelName"+level).checked = true;
		changeCheckedAndStatus(gridLevel,level);
		
		locateCenterAndLevel(gridId,currentArcgisConfigInfo.mapType);
		clearMyLayer();
		getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
		
		if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
			//alert(currentLayerListFunctionStr);
			eval(currentLayerListFunctionStr);
		}
    }
}
//当前选择网格的级别，当前加载网格轮廓的级别
function changeCheckedAndStatus(gridLevel,level) {
	$("#li2").addClass("hide");
	document.getElementById("gridLevelName2").checked = false;
	$("#li3").addClass("hide");
	//document.getElementById("gridLevelName3").disabled=true;
	document.getElementById("gridLevelName3").checked = false;
	$("#li4").addClass("hide");
	//document.getElementById("gridLevelName4").disabled=true;
	document.getElementById("gridLevelName4").checked = false;
	$("#li5").addClass("hide");
	//document.getElementById("gridLevelName5").disabled=true;
	document.getElementById("gridLevelName5").checked = false;
	$("#li6").addClass("hide");
	//document.getElementById("gridLevelName6").disabled=true;
	document.getElementById("gridLevelName6").checked = false;
	
	$("#liBuild0").addClass("hide");
	$(".Build").addClass("hide");
	//document.getElementById("buildName0").disabled=true;
	//document.getElementById("buildName0").checked=false;
	
	/*if (level-1 >= 5) {
		document.getElementById("buildName0").checked=true;
	}else {
		document.getElementById("buildName0").checked=false;
	}*/
	if(document.getElementById("gridLevelName"+gridLevel) != undefined) {
		$("#li"+gridLevel).removeClass("hide");
	}
	if(document.getElementById("gridLevelName"+level) != undefined) {
		$("#li"+level).removeClass("hide");
		document.getElementById("gridLevelName"+level).checked = true;
		var value = document.getElementById("li"+level).innerText;
		$("#level").html(value); 
	}
	if(document.getElementById("gridLevelName"+(parseInt(gridLevel)+2)) != undefined) {
		$("#li"+(parseInt(gridLevel)+2)).removeClass("hide");
	}
	if(parseInt(gridLevel)>4) {
		$("#liBuild0").removeClass("hide");
		$(".Build").removeClass("hide");
	}
}



