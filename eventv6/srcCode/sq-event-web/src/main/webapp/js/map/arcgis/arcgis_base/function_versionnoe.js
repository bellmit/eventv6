$(function() {
	$("#ulFirstall").css("max-height", document.getElementById('map0').offsetHeight-62);
	/*-------------------------------树形列表显示隐藏-------------------------------------*/
		var selectTree = $(".SelectTree");
		$(".local").hover(function() {
			selectTree.removeClass("dest").animate({top: 32},10, function() {
				selectTree.show();
			});
		}, function() {
			selectTree.addClass("dest").animate({top: 32}, 10, function() {
				selectTree.hide();
			});
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
		var mapLevel = $(".SelectTree2");
		$(".MapLevel").hover(function() {
			mapLevel.removeClass("dest").animate({top: 32}, 10, function() {
				mapLevel.show();
			});
		}, function() {
			mapLevel.addClass("dest").animate({top: 32}, 10, function() {
				mapLevel.hide();
			});
		});
	/*--------------------------------------------------------------------*/
	/*-----------------------------框选----------------------------------*/

		var $NavDiv = $(".kuangxuantongji .nav ul li");
		$NavDiv.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex = $NavDiv.index(this);
			$(".tabss").children().eq(NavIndex).show().siblings().hide();
		});
		$(".kuangxuan").click(function() {
			var div = $(".kuangxuanWindow");
			if($("#kuangxuanConfig").attr("src") == "") {
				$("#kuangxuanConfig").attr("src",js_ctx+"/zhsq/map/arcgis/arcgisdata/toKuangXuanConfig.jhtml?homePageType="+$("#homePageType").val()+"&socialOrgCode="+$("#socialOrgCode").val()+"&gridId="+$("#gridId").val());
			}
			if(div.hasClass("dest")) {
					div.removeClass("dest");
					$(".kuangxuanWindow").toggle();
					if (typeof MMApi != "undefined") {// 高德地图
						MMApi.openKuangxuan();
					} else {
						showKuangxuanHs();
					}
			} else {
					div.addClass("dest");
					$(".kuangxuanWindow").toggle();
					if (typeof MMApi != "undefined") {// 高德地图
						MMApi.closeKuangxuan();
					} else {
						kuangxuanCallBackOnClose1();
					}
			}
		});
		
		/*----------------------------周边资源z----------------------------------*/
		/*-------------------------------专题图层列表高度自适应-------------------------------------*/
		setTimeout(function(){
			var height=$(window).height();
			$(".ListShow").css("height",height-113);
		}, 100);
		/*--------------------------------------------------------------------*/
});


function closeKuangxuan() {
	$(".kuangxuan").click();
	if (typeof MMApi != "undefined") {
		MMApi.closeKuangxuan();
	} else {
		kuangxuanCallBackOnClose1();
	}
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
    $(".common").hide();
	$("#searchBtnId").hide();
	$(".firstall").show();
	nowAlphaBackShow = "firstall";
	currentLayerListFunctionStr="";
}
var currentClassificationFuncStr = "";//用于记录当前
function classificationClick(elementsCollectionStr){
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuName = eclist["menuName"];
	var menuCode = eclist["menuCode"];
	var callBack = eclist["callBack"];
	if (callBack == "classificationClick") {
		getChildListOfLayer(elementsCollectionStr);
	}
	//$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > "+menuName);

	var htmlstr = $("#titlePath").html();
	var htmls = htmlstr.split(" &gt; ");
	if(elementsCollectionStr!=undefined && htmlstr.indexOf(menuName)<0){
		htmlstr = htmlstr.replace(htmls[0],"<a href='javascript:void(0);' onclick='firstall()'>专题图层</a>")

		if (htmls.length > 1) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
		}
		$("#titlePath").html(htmlstr + " > "+menuName);
	} else {
		var gdcId = eclist["gdcId"];

		//htmlstr = htmlstr.replace(htmls[0],"<a href='javascript:void(0);' onclick='firstall()'>专题图层</a>")

		for (var i = htmls.length - 1; i >= 0; i--) {
			var obj = htmls[i];

			if (obj.indexOf(gdcId) == -1) {
				htmls.splice(i, i + 1);
			} else {
				htmls[i] = menuName;
				break;
			}
		}
		htmls = htmls.join(" &gt; ");
		$("#titlePath").html(htmls);
	}

	$(".ztIcon ul").css("max-height",document.getElementById('map'+currentN).offsetHeight-62);
    $("."+nowAlphaBackShow).hide();
	$(".common").show();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "firstall";
	currentLayerListFunctionStr="";
	currentClassificationFuncStr=callBack+"(\""+elementsCollectionStr+"\")";
}

function getChildListOfLayer(elementsCollectionStr) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var gdcId = eclist["gdcId"];
	var orgCode = eclist["orgCode"];
	var menuCode = eclist["menuCode"];
	var homePageType = $("#homePageType").val();
	var ulSecond=null;
	//if (menuCode != '') {
		var id = "ul" + menuCode.toUpperCase().substring(0, 1) + menuCode.substring(1, menuCode.length);
		ulSecond = document.getElementById("ulCommon");
	//}
	//if(ulSecond.innerHTML=="") {
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
		    			var callBack = ulSecondList[j].callBack;
		    			
	    				secondHtmlStr+="<li class=\""+ulSecondList[j].className+"\" id=\""+ulSecondList[j].gdcId+"\" onclick=\""+ulSecondList[j].callBack+"\" title=\""+title+"\">"
									+"<p class=\"pic\"><img src=\""+uiDomain+ulSecondList[j].largeIco+"\"/></p>"
									+"<p class=\"word AlphaBack\">"+ulSecondList[j].menuName+"</p>"
								+"</li>";
		    			
		    		}
					if(typeof(ulSecond) != 'undefined' && ulSecond != null && typeof(ulSecond.innerHTML) != 'undefined'){
                        ulSecond.innerHTML = "";
						ulSecond.innerHTML = "<div class=\"h_20\"></div>"+secondHtmlStr;
					}
		    	}
		     }
		  }
	  });
	//}
}

function analysisOfElementsCollection(elementsCollectionStr,elementsName){
	if(elementsCollectionStr) {
		var ecs = elementsCollectionStr.split(",_,");
		var eclist = new Array();
		for(var i=0;i<ecs.length;i++){
			var e = ecs[i].split("_,_");
			if(elementsName == e[0]){
				return e[1];
			}
		}
	}
	
	return "";
}

function analysisOfElementsCollectionList(elementsCollectionStr){
	if(elementsCollectionStr) {
		var ecs = elementsCollectionStr.split(",_,");
		var eclist = {};
		for(var i=0;i<ecs.length;i++){
			var e = ecs[i].split("_,_");
			eclist[e[0]]=e[1];
		}
		return eclist;
	}
	
	return {};
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

function CloseY(thisObj) {
	var div1 = $(".titlefirstall");
	var opacity = div1.is(":hidden") ? "show" : "hide";
	CloseZ(div1, opacity, function() {
		if (div1.is(":hidden")) {
			$(thisObj).find("img").attr("src", uiDomain + "/images/to_left.png");
		} else {
			$(thisObj).find("img").attr("src", uiDomain + "/images/to_right.png");
		}
	});
	if (opacity == "show") {
		var divLevel = div1.data("divLevel");
		CloseZ(divLevel, opacity);
	} else {
		var divLevel1 = $("div[type='level1']:visible").length,
			divLevel2 = $("div[type='level2']:visible").length,
			divLevel3 = $("div[type='level3']:visible").length;
		if (divLevel1 > 0) {
			div1.data("divLevel", $("div[type='level1']:visible"));
			CloseZ($("div[type='level1']:visible"), opacity);
		} else if (divLevel2 > 0) {
			div1.data("divLevel", $("div[type='level2']:visible"));
			CloseZ($("div[type='level2']:visible"), opacity);
		} else if (divLevel3 > 0) {
			div1.data("divLevel", $("div[type='level3']:visible"));
			CloseZ($("div[type='level3']:visible"), opacity);
		}
	}
}

function CloseZ(div, opacity, fn) {
	if (div.is(":hidden")) {
		var width = div.attr("_width");
		div.animate({
			width: width,
			opacity: opacity
		}, 300, null, fn);
	} else {
		div.attr("_width", div.width());
		div.animate({
			width: 0,
			opacity: opacity
		}, 300, null, fn);
	}
}
function showObjectList(elementsCollectionStr, gridId, orgCode) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist["menuCode"];
	var menuName = eclist["menuName"];
	var callBack = eclist["callBack"];
	var layerName = eclist["menuLayerName"];
	var htmlstr = $("#titlePath").html();
	if(htmlstr == "undefined" || htmlstr == null){
		htmlstr = "";
	}
	var htmls = htmlstr.split(" &gt; ");
	if(elementsCollectionStr!=undefined && htmlstr.indexOf(menuName)<0){
		htmlstr = htmlstr.replace(htmls[0],"<a href='javascript:void(0);' onclick='firstall()'>专题图层</a>")
		
		if (htmls.length > 1) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
		}
	
		$("#titlePath").html(htmlstr + " > <span title='"+menuName+"' style='margin-left:0px;'>"+menuName+"</span>");
	}
	if("1" == AUTOMATIC_CLEAR_MAP_LAYER) {
		clearMyLayer();
		toHideZhouBianSketch();
	}
	//clearMyLayer();
	if (typeof clearMyLayerA == "function") {
		clearMyLayerA();
	}
	if(gHeatLayer != null){
		gHeatLayer.clearData();
	}
	clearSpecialLayer(layerName);
	currentListNumStr=""
	//$("#titlePath").html(htmlstr + " > "+menuName);
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
    $(".common").hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	if($("#isNanAn").val() && "0"==AUTOMATIC_CLEAR_MAP_LAYER){
		showMapTreeToMap(elementsCollectionStr);
	}
	getObjectListUrl(elementsCollectionStr, gridId, orgCode);
	currentLayerListFunctionStr = callBack+"(\""+elementsCollectionStr+"\")";
}
var mapTreeDiv;
var mapTreeUl;
function showMapTreeToMap(elementsCollectionStr) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var gdcId = eclist["gdcId"];
	var className=$('#'+gdcId).attr('class');
	var menuCode = eclist["menuCode"];
	var menuName = eclist["menuName"];
	var callBack = eclist["callBack"];
	var layerName = eclist["menuLayerName"];
	var largeIco = eclist["largeIco"];
	var stylediv1 = document.body;
	var myHtml="";
	var deleteImg=js_ctx+"/js/map/spgis/img/delete.png";
	if(mapTreeUl && mapTreeUl.length>=20 && !$("#li"+gdcId).length>0){
		alert("最多同时展示10个图层");
		return;
	}
	if (mapTreeDiv!= null) {
	}else {
		mapTreeDiv = document.createElement("div");
		mapTreeDiv.id = "ImgMapTree";
		mapTreeDiv.className="ztIcon";
		mapTreeDiv.style.top = '87%';
		mapTreeDiv.style.left = '17%';
		mapTreeDiv.style.bottom = '0px';
		mapTreeDiv.style.position = "absolute";
		mapTreeDiv.style.width = "935px";
		mapTreeDiv.style.height = "100px";
		mapTreeDiv.style.overflow = "hidden";
		stylediv1.appendChild(mapTreeDiv);
		myHtml+="<ul id='ImgMapTreeUl'>";
	}
	myHtml+="<li id=\"li"+gdcId+"\" style=\"margin-left:10px;margin-top:10px;margin-bottom:10px;cursor:auto;\" class=\""+className+"\" title=\""+menuName+"\">"
	+"<p class=\"pic\"><img src=\""+uiDomain+largeIco+"\"/></p>"
	+"<p class=\"word AlphaBack\">"+menuName+"</p>"
	+"</li>";
	myHtml+="<li id=\"imgli"+gdcId+"\" style=\"margin:0;width:16px;height:16px\"><img id=\"img"+gdcId+"\" class=\"mapTreeButton\" title=\"删除\" src="+deleteImg+"></li>";
	if (mapTreeUl) {
		if($("#li"+gdcId).length>0){
			
		}else{
			$("#ImgMapTreeUl").append(myHtml);
		}
	}else{
		myHtml+="</ul>";
		mapTreeDiv.innerHTML = myHtml;
		mapTreeUl=$("#ImgMapTreeUl");
		$("#ImgMapTree").css("display","block");
	}
	$("#img"+gdcId).on("click",function () { delthisliandimg(gdcId,layerName) });
	
	
}

function delthisliandimg(gdcId,layerName){//清空图片和图层
	if(gdcId){
		$("#li"+gdcId).remove();
		$("#imgli"+gdcId).remove();
	}
	$("#map"+currentN).ffcsMap.clear({layerName : layerName});
}

function showZhouBianObjectList(name,zhoubianName,x,y,distance,mapType) {
	zhoubianListShow();
	var htmlstr = $("#titlePathZhouBian").html();
	$("#titlePathZhouBian").html("周边资源" + " > "+name);
 	$("#zhoubian_list_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$(".NorListZhouBian").show();
	getObjectListZhouBianUrl(zhoubianName,x,y,distance,mapType);
}

function showKuangXuanObjectList(name,kuangxuanName,geoString,mapType) {
	if (typeof MMApi == "undefined") {
		showKuangxuanHs();
	}
	zhoubianListShow();
	var htmlstr = $("#titlePathZhouBian").html();
	$("#titlePathZhouBian").html("框选统计" + " > "+name);
 	$("#zhoubian_list_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$(".NorListZhouBian").show();
	var url = js_ctx+'/zhsq/map/kuangxuan/kuangxuanStat/toKuangXuanPage.jhtml?kuangxuanType='+kuangxuanName+'&geoString='+geoString+'&mapType='+mapType+'&infoOrgCode='+$("#orgCode").val()+'&gridId='+$("#gridId").val()+'&homePageType='+$("#homePageType").val()+'&t='+Math.random();
	if(url != ""){
		showZhouBianListPanel(url);
	}
}

function zhoubianListShow(){
	var div = $(".titlezhoubian");
	var divzhuanti = $(".titlefirstall");
	if(div.hasClass("dest")) {
		divzhuanti.addClass("dest").animate({top: 32}, 10, function(){
				$(".titlefirstall").hide();
				$("."+nowAlphaBackShow).hide();
		});
		div.removeClass("dest").animate({top: 32}, 10, function(){
			$(".titlezhoubian").show();
			$(".zhoubianList").show();
			
		});
	}
}
function zhoubianListHide(){
	var div = $(".titlezhoubian");
	var divzhuanti = $(".titlefirstall");
	divzhuanti.removeClass("dest").animate({top: 32}, 10, function(){
		$(".titlefirstall").show();
		$("."+nowAlphaBackShow).show();
	});
	div.addClass("dest").animate({top: 32}, 10, function(){
		
		$(".titlezhoubian").hide();
		$(".zhoubianList").hide();
	});
}
//还要获取当前的mapt
function getObjectListZhouBianUrl(zhoubianName,x,y,distance,mapType){
	var url="";
	url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType='+zhoubianName+'&x='+x+'&y='+y+'&distance='+distance+'&mapType='+mapType+'&infoOrgCode='+$("#orgCode").val()+'&gridId='+$("#gridId").val()+'&homePageType='+$("#homePageType").val()+'&t='+Math.random();
	if(url != ""){
		showZhouBianListPanel(url);
	}
}

//显示重点人口信息
function showZhouBianListPanel(url){
    $("#zhoubian_list_frme").attr("src",url); 
}

function getObjectListUrl(elementsCollectionStr,gridId,orgCode) {
	if (!gridId) gridId = $("#gridId").val();
	if (!orgCode) orgCode = $("#orgCode").val();
	var _mapt = "5";
	if (typeof MMApi != "undefined") {
		_mapt = MMGlobal.MapType;
	} else {
		_mapt = currentArcgisConfigInfo.mapType;
	}
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist["menuCode"];
	var menuListUrl = eclist["menuListUrl"];
	if(menuListUrl.indexOf('http://')<0){
		menuListUrl = js_ctx + menuListUrl;
	}
	if(menuListUrl.indexOf("?")<=0){
		menuListUrl += "?t="+Math.random();
	}
	menuListUrl += "&mapt="+_mapt+"&gridId="+gridId+"&gridCode="+orgCode+"&orgCode="+orgCode+"&infoOrgCode="+orgCode+"&noneGetLayer=true";
	
	var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+  
       '<input id="elementsCollectionStr" name="elementsCollectionStr" type="hidden" value="'+elementsCollectionStr+'"/>'+  
       '</form>';
    document.getElementById('get_grid_name_frme').contentWindow.document.write(html);  
	document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
}
//显示重点人口信息
function showListPanel(url){
    $("#get_grid_name_frme").attr("src",url); 
}
//显示事件采集录入-安全工作
function showAddEventPanelForAQGZ(){
	mapObjectName = "新增事件信息";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml?trigger=AQGZ&typesForList=06';
	showMaxJqueryWindow("新增事件信息", url, 800, 400);	
}
//显示事件采集录入
function showAddEventPanel(){
	mapObjectName = "新增事件信息";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml';
	showMaxJqueryWindow("新增事件信息", url, 800, 400);	
}
function showHeatMapPanel(elementsCollectionStr) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist["menuCode"];
	var menuCode = eclist["menuCode"];
	var menuListUrl = eclist["menuListUrl"];
	var layerName = eclist["menuLayerName"];
	var callBack = eclist["callBack"];
	if (menuListUrl.indexOf("?") <= 0) {
		menuListUrl += "?t=" + Math.random();
	}
	menuListUrl += "&mapType="+currentArcgisConfigInfo.mapType+"&infoOrgCode="+$("#orgCode").val();

	if(menuCode == "spectialPopulation"){
		closeHeatMapLayer();
		clearMyLayer();
		//if (typeof heatLayers[menuCode] == "undefined") {
			heatLayers[menuCode] = new HeatMapLayer(menuCode);
		//}
		heatLayers[menuCode].setUrl(menuListUrl);
		heatLayers[menuCode].show();
	}else if(menuCode == "trafficAccident"){
		closeHeatMapLayer();
		clearMyLayer();
		$("#trafficURL").val(menuListUrl);
		optHeatTrafficMap(menuListUrl);
	}else{
		closeHeatMapLayer();
		clearMyLayer();
		//if (typeof heatLayers[menuCode] == "undefined") {
			heatLayers[menuCode] = new HeatMapLayer(menuCode);
		//}
		heatLayers[menuCode].setUrl(menuListUrl);
		heatLayers[menuCode].show();
	}
	currentLayerLocateFunctionStr=callBack+"(\""+elementsCollectionStr+"\")";
}
//显示事件语音呼叫
function showVoiceCallPanel(){
	//引用zzgrid链接
	//var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    //var url =  sq_zzgrid_url +'/zzgl/map/zhddData/centerControl/voiceCall.jhtml?gridId='+$("#gridId").val();
	var url=js_ctx+'/zhsq/map/arcgis/arcgisCenterControl/voiceCall.jhtml?gridId='+$("#gridId").val();	
	showMaxJqueryWindow("语音呼叫", url, 720,463);
}

function optHeatTrafficMap(isOpen) {
	$("#heatMapDiv").hide();
	$("#heatTrafficMapDiv").show();
	$("#heatTrafficMapDiv-close").show();
	$("#trafficAccidentDiv").show();
	if (typeof MMApi != "undefined") {
		MMApi.heatMap(isOpen);
	} else {
		layer.load(0);
		if (gHeatLayer == null) {
			gHeatLayer = $("#map" + currentN).ffcsMap.createHeatMap("heatLayer", {
				"useLocalMaximum": true,
		        "radius": 20,
		        "gradient": {
					"0.45": "rgb(000,000,255)",
					"0.65": "rgb(000,255,255)",
					"0.75": "rgb(000,255,000)",
					"0.95": "rgb(255,255,000)",
					"1.00": "rgb(255,000,000)"
		        }
			});
		}
		
		gHeatLayer.clearData();
		if (isOpen) {
			var dataUrl = isOpen;
			$("#map" + currentN).ffcsMap.heatMap(gHeatLayer, dataUrl, function() {
				layer.closeAll('loading');
			});
		} else {
			layer.closeAll('loading');
		}
	}
	
	this.clearHeatMap = function() {
		if (typeof me.heatLayer != "undefined") {
			me.heatLayer.clearData();
		}
	};
	
	this.setUrl = function(url) {
		_url = url;
	};
	
	return { setUrl : this.setUrl,  hide : this.clearHeatMap };
}

function showEventCollectEntryPanel(){
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url+'/zzgl/event/outPlatform/eventCollectEntry.jhtml?close=no';
	showMaxJqueryWindow("采集录入", url, 760,480);	
}

//党风廉政模块的弹窗显示
function showOrgIndependentPanel(type, width, height){
	//引用zzgrid链接
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    var url =  "";
    var title = "";
    var width=500;
    var height=400;
    if(type=='jgw'){   //纪工委
      url = sq_zzgrid_url+'/zzgl/map/ztywData/inspectOrganization/inspectOrgInfo.jhtml?gridId='+$("#gridId").val();
   	  url+='&groupType=001';
      title = "纪工委";
      width=1000;
      height=400;
    }else if(type=='cj'){  //组织架构-村居
      url = sq_zzgrid_url+'/zzgl/map/ztywData/inspectOrganization/inspectOrgInfo.jhtml?gridId='+$("#gridId").val();
   	  url+='&groupType=002';
      title = "村居";
      width=1000;
      height=400;
    }else if(type=='sjll'){  //事件录入
      url =  sq_zzgrid_url+'/zzgl/event/outPlatform/efficiencyCollectEntry.jhtml';
      title = "事件录入";
      width=700;
      height=420;
    }else if(type=='hdkz_jd'){  //活动开展情况-街道
      url = sq_zzgrid_url + '/zzgl/map/ztywData/activitiesSituation/streetList.jhtml';
      title = "街道";
      width=1070;
      height=400;
    }else if(type=='hdkz_cj'){  //活动开展情况-村居
      url = sq_zzgrid_url + '/zzgl/map/ztywData/activitiesSituation/aVillageSceneList.jhtml';
      title = "村居";
      width=1070;
      height=400;
    }else if(type=='jcrfzz'){  //基层人防职责
      url = sq_zzgrid_url + '/theme/scim/styles/sosp_haicang/images/html/jcrfzz.html';
      title = "基层人防职责";
      width=1000;
      height=400;
      scroll = "yes";
    }else if(type=='jcrfxz'){  //基层人防小组
      url = sq_zzgrid_url + '/zzgl/infoOpen/infoOpenInfo.jhtml?catalogId=100701&isGis=1';
      title = "基层人防小组";
      width=1000;
      height=400;
      scroll = "yes";
    }else if(type=='fkjbfb'){  //防空警报分布
      url = sq_zzgrid_url + '/theme/scim/styles/sosp_haicang/images/html/images/rmfk_fkjbfbt.png';
      title = "防空警报分布";
      width=1070;
      height=400;
      scroll = "yes";
    }else if(type=='fkfajyl'){  //防空方案及演练
      url = sq_zzgrid_url + '/theme/scim/styles/sosp_haicang/images/html/fkfajyl.html';
      title = "防空方案及演练";
      width=500;
      height=235;
    }else if(type=='zhtxsd'){  //指挥通信手段
      url = 'http://218.104.140.158/';
      title = "指挥通信手段";
      width=1000;
      height=400;
      scroll = "yes";
    }else if(type=='zcfg'){  //政策法规
      url = sq_zzgrid_url + "/zzgl/infoOpen/infoOpenInfo.jhtml?catalogId=100702&isGis=1";
      title = "政策法规";
      width=1000;
      height=400;
      scroll = "yes";
    }else if(type=='rfjjsqcl'){  //人防结建申请材料
      url = sq_zzgrid_url + "/zzgl/infoOpen/infoOpenInfo.jhtml?catalogId=100703&isGis=1";
      title = "人防结建申请材料";
      width=1000;
      height=400;
      scroll = "yes";
    }else if(type=='rfgcfb'){  //人防工程分布
      url = sq_zzgrid_url + "/theme/scim/styles/sosp_haicang/images/html/rfgcfb1.ftl";
      title = "人防工程分布";
      width=1000;
      height=400;
    }

    if(height == null && width == null){
    	height = $(document).height();
    	width = $(document).width();
    }
    showMaxJqueryWindow(title, url, width, height, null, scroll);
}

//选择树的回调函数
function gridTreeClickCallback(gridId,gridName,orgId,orgCode,gridInitPhoto,gridLevel,gridCode){
	if($("#isNanAn").val()){
		
	}else{
		$(".SelectTree").css("display","none");
	}
    if(gridCode != $("input[name='gridCode']").val() || (typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' && SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && gridIds.split(",").length>2)) {
    	$("#changeGridName").text(gridName);
	    $("input[name='gridId']").val(gridId);
	    $("input[name='gridCode']").val(gridCode);
		$("input[name='gridName']").val(gridName);
		$("input[name='gridLevel']").val(gridLevel);
		$("input[name='orgCode']").val(orgCode);
		if (typeof MMApi != "undefined") {// 高德地图
			var gridLevel = parseInt($("#gridLevel").val());
		 	var level = gridLevel + 1;
		 	if (gridLevel == 2) {
		 		level = gridLevel + 2;
		 	} else if (gridLevel == 6) {
		 		level = gridLevel;
		 	}
			changeCheckedAndStatus(gridLevel, level);
			MMApi.setCenter(gridId, gridCode);
			$("#buildName0").attr("checked", false);
			if (currentLayerListFunctionStr != undefined && currentLayerListFunctionStr != '') {
				eval(currentLayerListFunctionStr);
			}
		} else {
			var level = judgeNextOutline(parseInt(gridLevel));
			//document.getElementById("gridLevelName"+level).checked = true;
			changeCheckedAndStatus(gridLevel, level);
		
			locateCenterAndLevel(gridId,currentArcgisConfigInfo.mapType);
			clearMyLayer();
			if((typeof LUO_FANG != 'undefined' && LUO_FANG == "true") || (typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' && SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && level != 2)){
				level = level - 1;
			}
			
			if ($("#isNanAn").val()) {
				getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level, "no-all");
			} else {
				getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
			}
			
			if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
				//alert(currentLayerListFunctionStr);
				eval(currentLayerListFunctionStr);
			}
		}
		if (typeof clearMyLayerA == "function") {
			clearMyLayerA();
		}
    }
}

function judgeNextOutline(cGridLevel) {
	var nextGridLevel = parseInt(cGridLevel) + 1;
	if (document.getElementById("li" + nextGridLevel)) {
		return nextGridLevel;
	}
	return cGridLevel;
}

//当前选择网格的级别，当前加载网格轮廓的级别
function changeCheckedAndStatus(gridLevel,level) {
	// 全部隐藏和取消选中
	$(".MapLevel>.SelectTree2 li").each(function() {
		$(this).addClass("hide");
		$(this).find("input")[0].checked = false;
	});
	// 隐藏楼宇
	$("#BuildOutlineDiv").addClass("hide");
	
	if (level-1 >= 5) {
		if(document.getElementById("buildName0").checked == true) {
			getArcgisDataOfBuildsByCheck();
		}
	}else {
		document.getElementById("buildName0").checked=false;
	}
	if (typeof MMApi != "undefined") {// 高德地图
		for (var i = gridLevel; i <= 6; i++) {
			$("#li"+i).removeClass("hide");
		}
		$("#li3").addClass("hide");
		if (document.getElementById("gridLevelName"+level) != undefined) {
			document.getElementById("gridLevelName"+level).checked = true;
			var value = document.getElementById("li"+level).innerText;
			$("#level").html(value); 
		}
	} else {
		$(".MapLevel>.SelectTree2 li").each(function() {
			if (parseInt($(this).attr("index")) >= parseInt(gridLevel)) {
				$(this).removeClass("hide");
			}
		});
		if(((typeof LUO_FANG != 'undefined' && LUO_FANG == "true") || 
				(typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' 
					&& SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true")) 
					&& document.getElementById("gridLevelName"+(level-1)) != undefined
		) {
			var operateLevel = level;
			if($("#gridLevel").val() == "6"){
				operateLevel = level;
			}else{
				operateLevel = level -1;
			}
			document.getElementById("gridLevelName"+(operateLevel)).checked = true;
			var value = document.getElementById("li"+(operateLevel)).innerText;
			$("#level").html(value);
		}else{
			if(document.getElementById("gridLevelName"+level) != undefined) {
				document.getElementById("gridLevelName"+level).checked = true;
				var value = document.getElementById("li"+level).innerText;
				$("#level").html(value);
			}
			if ($("#isNanAn").val()) {
				var _level = judgeNextOutline(parseInt(level));
				if (_level != level && document.getElementById("gridLevelName" + _level) != undefined) {
					document.getElementById("gridLevelName"+_level).checked = true;
					var value = document.getElementById("li"+_level).innerText;
					$("#level").html(value);
				}
			}
		}
	}
	if (parseInt(gridLevel) == parseInt(level)) {
		$("#BuildOutlineDiv").removeClass("hide");
	}
}

function showDetailLigerUI(title,url,width,height){
	var _showWinClass=showWinClass;
	_showWinClass.height=height;
	showWinClass.url=url;
	showWinClass.title=title;
	showWinClass.width=width;
	showWinClass.showMax=true;
	showWinClass.showToggle=false;
	showWinClass.showMin=false;
	showWinClass.isResize=false;
	showWinClass.slide=false;//动作
	showWinClass.isDrag=true;//拖动
	showWinClass.isMax=true;
	showWinClass.isunmask=true;//取消遮罩层
	showWinClass.modal=false;
	showWinClass.name=title;
    showWinClass.buttons=null;
	
	showGridWin(_showWinClass);
	 
}

/******START LYJJ********/

function showLyjjList(objectName) {
	var htmlstr = "<a href='javascript:void(0);' onclick='firstall()'>专题图层</a>";
	$("#titlePath").html(htmlstr + " > "+objectName);
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	mapObjectName = objectName;//用于回调列表刷新
	getLyjjListUrl("lyjj"+objectName);
}

function getLyjjListUrl(objectName) {
	var url;
	/****从方法showLyjjList调用而来，每个objectName都会加上一个lyjj前缀 ****/
	if("lyjj企业"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisdataoflyjj/toLyjjCorpIndex.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}else if("lyjj楼栋"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisdataoflyjj/toLyjjBuildingIndex.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}else if("lyjj事件"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisdataoflyjj/toLyjjEventIndex.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
}

/******END LYJJ********/

function showObjectListForTree(elementsCollectionStr, checked) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist[ "menuCode"];
	var layerName = eclist[ "menuLayerName"];
	if (!checked) {
		excuteSPGISorARCGIS(function() {
			MMApi.clearMap(layerName);
		}, function() {
			$("#map"+currentN).ffcsMap.clear({layerName : layerName});
		});
		if (menuCode == "rentRoom" && typeof rentRoomTip == "function") {
			rentRoomTip(false);
		}
		return;
	}
	var dataUrl = "";
	var gisUrl = "";
	var width = 100;
	var height = 100;
	var fieldName = "";
	var orgCode = $("#orgCode").val();
	var gridId = $("#gridId").val();
	var params = {};
	params.page=1;
	params.rows=100000;
	if (menuCode == "controlsafetyRanks0") {//机构队伍
		dataUrl = "/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?showType=2&ids=";
		width = 330;
		height = 170;
		fieldName = "teamId";
		params.orgCode = orgCode;
		params.bizType = "0";
	} else if (menuCode == "controlsafetyRanks1") {//群防群治队伍
		dataUrl = "/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?showType=2&ids=";
		width = 330;
		height = 170;
		fieldName = "teamId";
		params.orgCode = orgCode;
		params.bizType = "1";
	} else if (menuCode == "center") {//综治中心
		dataUrl = "/zhsq/map/arcgis/arcgisdataofgoods/controlsafetyRanksListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfControlsafetyRanks.jhtml?showType=2&ids=";
		width = 330;
		height = 170;
		fieldName = "teamId";
		params.orgCode = orgCode;
		params.bizType = "8";
	} else if (menuCode == "networkingInfoCenter") {//各视联网信息中心
		dataUrl = "/zhsq/map/arcgis/arcgisdataofregion/listNicData.jhtml";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfNic.jhtml?ids=";
		width = 340;
		height = 195;
		fieldName = "nicId";
		params.infoOrgId=gridId;
	} else if (menuCode == "aidsPatient") {//艾滋病人员
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "drugs") {//吸毒人员
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "camps") {//刑释人员
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "neuropathy") {//精神障碍患者
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "rectify") {//矫正人员
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "youth") {//重点青少年
		dataUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?showType=2&userIds=";
		width = 400;
		height = 236;
		fieldName = "ciRsId";
		params.gridId=gridId;
		params.elementsCollectionStr=elementsCollectionStr;
	} else if (menuCode == "corLayer") {//企业
		dataUrl = "/zhsq/map/arcgis/arcgisdataofregion/corBaseListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofregion/getArcgisCorLocateDataList.jhtml?showType=2&ids=";
		width = 400;
		height = 235;
		fieldName = "cbiId";
		params.gridId=gridId;
		params.status="001";
	} else if (menuCode == "nonPublicOrg") {//新经济组织
		dataUrl = "/zhsq/map/arcgis/arcgisdataofnewgroup/nonPublicOrgListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofnewgroup/getArcgisLocateDataListOfNewGroup.jhtml?type='01B029'&ids=";
		width = 400;
		height = 235;
		fieldName = "cbiId";
		params.gridId=gridId;
	} else if (menuCode == "newSocialOrg") {//新社会组织
		dataUrl = "/zhsq/map/arcgis/arcgisdataofnewgroup/newSocialOrgListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofnewgroup/getArcgisLocateDataListOfNewSociety.jhtml?type='01B032'&ids=";
		width = 340;
		height = 195;
		fieldName = "orgId";
		params.gridId=gridId;
	} else if (menuCode == "logisticsSafety") {//寄递物流企业
		dataUrl = "/zhsq/map/arcgis/arcgisDataOfSocietyController/logisticsListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLogisticsSafetyLocateDataListByIds.jhtml?ids=";
		width = 660;
		height = 400;
		fieldName = "lsId";
		params.gridId=gridId;
		params.status="1";
	} else if (menuCode == "dispute") {//矛盾纠纷
		dataUrl = "/zhsq/map/arcgis/arcgisDataOfSocietyController/disputeListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfDispute.jhtml?ids=";
		width = 490;
		height = 270;
		fieldName = "mediationId";
		params.gridId=gridId;
	} else if (menuCode == "campus") {//校园周边
		dataUrl = "/zhsq/map/arcgis/arcgisdataofgoods/campusListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfCampus.jhtml?showType=2&resTypeCode=0801&ids=";
		width = 350;
		height = 280;
		fieldName = "plaId";
		params.gridId=$("#gridId").val();
	} else if (menuCode == "worldHlhx") {//护路护线
		dataUrl = "/zhsq/map/arcgis/arcgisdataofregion/iCareRoadListData.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfHlhxListByIds.jhtml?showType=2&ids=";
		width = 370;
		height = 170;
		fieldName = "lotId";
		params.gridId=gridId;
	} else if (menuCode == "realPeople") {//实有人口
		dataUrl = "/zhsq/map/gisstat/gisStat/getRealPeople.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
		width = 350;
		height = 270;
		params.gridId=gridId;
		layer.load(0);
		$.ajax({
			url: js_ctx + dataUrl + '?t=' + Math.random(),
			type: 'POST',
			data: params,
			dataType: "json",
			error: function(data) {
				layer.closeAll('loading');
			 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
			},
			success: function(list) {
				layer.closeAll('loading');
				var results = "";
				if (list && list.length > 0) {
					var map = new HashMap();
					for (var i = 0; i < list.length; i++) {
						var val = list[i];
						results = results + "," + val["ID_"];
						map.put(val["ID_"] + "", val["TOTAL_"]);
					}
					results = results.substring(1, results.length);
					
					gisUrl = js_ctx + gisUrl + results;
					
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					opt.getText = function(data) {
						var html = '<div class="mapbox"><i class="mapicon"><img src="'+uiDomain+'/images/map_tree_realPeople.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
						return html;
					};
					excuteSPGISorARCGIS(function() {
						MMApi.markerIcons(opt, "drawText");
					}, function() {
						customDivZhuanTi(layerName, opt);
					});
				}
			}
		});
		return;
	} else if (menuCode == "rentRoom") {//出租屋
		dataUrl = "/zhsq/map/gisstat/gisStat/getRentRoom.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
		width = 700;
		height = 320;
		params.gridId=gridId;
		layer.load(0);
		$.ajax({
			url: js_ctx + dataUrl + '?t=' + Math.random(),
			type: 'POST',
			data: params,
			dataType: "json",
			error: function(data) {
				layer.closeAll('loading');
			 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
			},
			success: function(list) {
				layer.closeAll('loading');
				var results = "";
				if (list && list.length > 0) {
					var map = new HashMap();
					var colorMap = new HashMap();
					for (var i = 0; i < list.length; i++) {
						var val = list[i];
						results = results + "," + val["ID_"];
						map.put(val["ID_"] + "", val["TOTAL_"]);
						colorMap.put(val["ID_"] + "", val["COLOR_"]);
					}
					results = results.substring(1, results.length);
					
					gisUrl = js_ctx + gisUrl + results;
					
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					opt.getText = function(data) {
						var html = '<div class="mapbox" style="background-color:'+colorMap.get(data.wid + "")+'"><i class="mapicon"><img src="'+uiDomain+'/images/map_tree_rentRoom.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
						return html;
					};
					excuteSPGISorARCGIS(function() {
						MMApi.markerIcons(opt, "drawText");
					}, function() {
						customDivZhuanTi(layerName, opt);
					});
					if (typeof rentRoomTip == "function") {
						rentRoomTip(true);
					}
				}
			}
		});
		return;
	} else if (menuCode == "majorRelatedEvents") {//重特大事件
		dataUrl = "/zhsq/map/gisstat/gisStat/getMajorRelatedEvents.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
		width = 800;
		height = 350;
		params.gridId=gridId;
		layer.load(0);
		$.ajax({
			url: js_ctx + dataUrl + '?t=' + Math.random(),
			type: 'POST',
			data: params,
			dataType: "json",
			error: function(data) {
				layer.closeAll('loading');
			 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
			},
			success: function(list) {
				layer.closeAll('loading');
				var results = "";
				if (list && list.length > 0) {
					var map = new HashMap();
					for (var i = 0; i < list.length; i++) {
						var val = list[i];
						results = results + "," + val["GRID_ID"];
						map.put(val["GRID_ID"] + "", val["BASENUM"]);
					}
					results = results.substring(1, results.length);
					
					gisUrl = js_ctx + gisUrl + results;
					
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					opt.getText = function(data) {
						var html = '<div class="mapbox"><i class="mapicon"><img src="'+uiDomain+'/images/map/gisv0/special_config/images/tree_majorRelatedEvents.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
						return html;
					};
					excuteSPGISorARCGIS(function() {
						MMApi.markerIcons(opt, "drawText");
					}, function() {
						customDivZhuanTi(layerName, opt);
					});
				}
			}
		});
		return;
	} else if (menuCode == "schoolRelatedEvents") {//涉及师生安全事件
		dataUrl = "/zhsq/map/gisstat/gisStat/getSchoolRelatedEvents.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
		width = 800;
		height = 350;
		params.gridId=gridId;
		layer.load(0);
		$.ajax({
			url: js_ctx + dataUrl + '?t=' + Math.random(),
			type: 'POST',
			data: params,
			dataType: "json",
			error: function(data) {
				layer.closeAll('loading');
			 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
			},
			success: function(list) {
				layer.closeAll('loading');
				var results = "";
				if (list && list.length > 0) {
					var map = new HashMap();
					for (var i = 0; i < list.length; i++) {
						var val = list[i];
						results = results + "," + val["GRID_ID"];
						map.put(val["GRID_ID"] + "", val["BASENUM"]);
					}
					results = results.substring(1, results.length);
					
					gisUrl = js_ctx + gisUrl + results;
					
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					opt.getText = function(data) {
						var html = '<div class="mapbox"><i class="mapicon"><img src="'+uiDomain+'/images/map/gisv0/special_config/images/tree_schoolRelatedEvents.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
						return html;
					};
					excuteSPGISorARCGIS(function() {
						MMApi.markerIcons(opt, "drawText");
					}, function() {
						customDivZhuanTi(layerName, opt);
					});
				}
			}
		});
		return;
	} else if (menuCode == "relatedRoadEvents") {//涉及线路事件
		dataUrl = "/zhsq/map/gisstat/gisStat/getRelatedRoadEvents.json";
		gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
		width = 850;
		height = 350;
		params.gridId=gridId;
		layer.load(0);
		$.ajax({
			url: js_ctx + dataUrl + '?t=' + Math.random(),
			type: 'POST',
			data: params,
			dataType: "json",
			error: function(data) {
				layer.closeAll('loading');
			 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
			},
			success: function(list) {
				layer.closeAll('loading');
				var results = "";
				if (list && list.length > 0) {
					var map = new HashMap();
					for (var i = 0; i < list.length; i++) {
						var val = list[i];
						results = results + "," + val["GRID_ID"];
						map.put(val["GRID_ID"] + "", val["BASENUM"]);
					}
					results = results.substring(1, results.length);
					
					gisUrl = js_ctx + gisUrl + results;
					
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					opt.getText = function(data) {
						var html = '<div class="mapbox"><i class="mapicon"><img src="'+uiDomain+'/images/map/gisv0/special_config/images/tree_relatedRoadEvents.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
						return html;
					};
					excuteSPGISorARCGIS(function() {
						MMApi.markerIcons(opt, "drawText");
					}, function() {
						customDivZhuanTi(layerName, opt);
					});
				}
			}
		});
		return;
	}
	layer.load(0);
	$.ajax({ 
		url: js_ctx + dataUrl + '?t=' + Math.random(),
		type: 'POST',
		data: params,
		dataType: "json",
		error: function(data) {
			layer.closeAll('loading');
		 	$.messager.alert('友情提示','获取定位坐标信息出现异常!','warning');
		},
		success: function(data) {
			layer.closeAll('loading');
			var list = data.rows;
			var results = "";
			if (list && list.length > 0) {
				for (var i = 0; i < list.length; i++) {
					var val = list[i];
					results = results + "," + val[fieldName];
				}
				results = results.substring(1, results.length);
				
				gisUrl = js_ctx + gisUrl + results;
				
				excuteSPGISorARCGIS(function() {
					var opt = {};
					opt.w = width;
					opt.h = height;
					opt.ecs = elementsCollectionStr;
					opt.gridId = gridId;
					opt.url = gisUrl;
					return MMApi.markerIcons(opt);
				}, function() {
					return customPointerZhuanTi(gisUrl, elementsCollectionStr, width, height);
				});
			}
		}
	});
}

function renderCustomZhuanTi(checked, sYear, pCode, elementsCollectionStr) {
	if (!elementsCollectionStr) elementsCollectionStr = $("#keyPopEcs").val();
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist[ "menuCode"];
	var layerName = eclist[ "menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	var menuListUrl = eclist["menuListUrl"];
	$("#map"+currentN).ffcsMap.clear({layerName : layerName});
	if (!checked) {
		return;
	}
	var gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
	var orgCode = $("#orgCode").val();
	var gridId = $("#gridId").val();
	var params = {};
	params.page = 1;
	params.rows = 100000;
	params.gridId = gridId;
	params.sYear = sYear;
	params.pCode = pCode;
	if (menuListUrl && menuListUrl.indexOf('?') == -1) {
		menuListUrl += '?t=' + Math.random();
	} else {
		menuListUrl += '&t=' + Math.random();
	}
	layer.load(0);
	$.ajax({
		url: menuListUrl,
		type: 'POST',
		data: params,
		dataType: "json",
		complete:function() {
			layer.closeAll('loading');
		},
		error: function(data) {
			$.messager.alert('友情提示','获取图层【'+layerName+'】数据出错!','warning');
		},
		success: function(list) {
			var results = "";
			if (list && list.length > 0) {
				var map = new HashMap();
				var colorMap = new HashMap();
				for (var i = 0; i < list.length; i++) {
					var val = list[i];
					results = results + "," + val["ID_"];
					map.put(val["ID_"] + "", val["TOTAL_"]);
					colorMap.put(val["ID_"] + "", val["COLOR_"]);
				}
				results = results.substring(1, results.length);
				
				gisUrl = js_ctx + gisUrl + results;
				
				var opt = {};
				opt.w = menuSummaryWidth;
				opt.h = menuSummaryHeight;
				opt.ecs = elementsCollectionStr;
				opt.gridId = gridId;
				opt.url = gisUrl;
				opt.getText = function(data) {
					var html = '<div class="mapbox" style="background-color:'+colorMap.get(data.wid + "")+'"><i class="mapicon"><img src="'+uiDomain+'/images/map_tree_realPeople.png" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
					return html;
				};
				customDivZhuanTi(layerName, opt);
			}
		}
	});
}

function renderPartyMember(elementsCollectionStr, checked, orgCode, gridId) {
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	
	if (!elementsCollectionStr) elementsCollectionStr = $("#partyMemberEcs").val();
	
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist[ "menuCode"];
	var layerName = eclist[ "menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	var menuListUrl = eclist["menuListUrl"];
	var treeIcon = eclist["treeIcon"];
	
	if ("1" == AUTOMATIC_CLEAR_MAP_LAYER) {
		if (typeof (checked) == "undefined") {// 汇聚图层初始调用时
			clearMyLayer();
		} else {
			clearMyLayer(layerName);
		}
	}
	
	clearSpecialLayer(layerName);
	
	locateCenterAndLevel(gridId, currentArcgisConfigInfo.mapType);
	getArcgisDataOfGrids(gridId, orgCode, currentArcgisConfigInfo.mapType, gridLevel(orgCode) + 1);
	
	if (typeof (checked) != "undefined" && !checked) {
		return;
	}
	var gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
	var params = {};
	params.page = 1;
	params.rows = 100000;
	params.gridId = gridId;
	params.infoOrgCode = orgCode;
	if (menuListUrl && menuListUrl.indexOf('?') == -1) {
		menuListUrl += '?t=' + Math.random();
	} else {
		menuListUrl += '&t=' + Math.random();
	}
	layer.load(0);
	$.ajax({
		url: menuListUrl,
		type: 'POST',
		data: params,
		dataType: "json",
		complete:function() {
			layer.closeAll('loading');
		},
		error: function(data) {
			$.messager.alert('友情提示','获取图层【'+layerName+'】数据出错!','warning');
		},
		success: function(list) {
			var results = "";
			if (list && list.length > 0) {
				var map = new HashMap();
				for (var i = 0; i < list.length; i++) {
					var val = list[i];
					results = results + "," + val["ID_"];
					map.put(val["ID_"] + "", val["TOTAL_"]);
				}
				results = results.substring(1, results.length);
				
				gisUrl = js_ctx + gisUrl + results;
				
				var opt = {};
				opt.w = menuSummaryWidth;
				opt.h = menuSummaryHeight;
				opt.ecs = elementsCollectionStr;
				opt.gridId = gridId;
				opt.url = gisUrl;
				opt.getText = function(data) {
					var html = '<div class="mapbox"><i class="mapicon"><img src="' + uiDomain + treeIcon + '" width="14" height="14" /></i>'+map.get(data.wid + "")+'<div class="maparrow"></div></div>';
					return html;
				};
				// customDivZhuanTi(layerName, opt);
                if (menuSummaryWidth == "null") {
                    menuSummaryWidth = 400;
                }
                if (menuSummaryHeight == "null") {
                    menuSummaryHeight = 260;
                }
				var _opt = opt || {};
                var cOrgCode = orgCode;
                var level = gridLevel(cOrgCode);
                //如果是社区级就直接撒点（全球眼汇聚功能要在乡镇级开始撒点）
                if (level >= 5 || (menuCode == "statGlobalEyes" && level>=4)) {//全球眼汇聚功能要在乡镇级开始撒点
                    var remark = eclist[ "remark"];
                    if (remark != '' && remark != undefined && remark != "null") {
                        renderPartyMember(elementsCollectionStr, false, cOrgCode, gridId);
                        var gisDataUrl = js_ctx + remark + cOrgCode;
                        getArcgisDataOfZhuanTi(gisDataUrl, elementsCollectionStr, menuSummaryWidth, menuSummaryHeight);
                    }
                }else{
                    _opt.createDlg = function(data) {
                        layer.load(0);
                        var cGridId = data.wid;
                        var cOrgCode = data.infoOrgCode;
                        var level = gridLevel(cOrgCode);
                        renderPartyBackBtn(elementsCollectionStr, cOrgCode, orgCode, gridId);
                        if (level >= 5 || (menuCode == "statGlobalEyes" && level>=4)) {//全球眼汇聚功能要在乡镇级开始撒点
                            var remark = eclist[ "remark"];
                            if (remark != '' && remark != undefined && remark != "null") {
                                renderPartyMember(elementsCollectionStr, false, cOrgCode, cGridId);
                                var gisDataUrl = js_ctx + remark + cOrgCode;
                                getArcgisDataOfZhuanTi(gisDataUrl, elementsCollectionStr, menuSummaryWidth, menuSummaryHeight);
                            }
                        } else {
                            renderPartyMember(elementsCollectionStr, true, cOrgCode, cGridId);
                        }
                        layer.closeAll('loading');
                        return null;
                    };
                    $("#map"+currentN).ffcsMap.renderDiv(layerName, _opt);
				}

			}
		}
	});
}

function renderConverge(elementsCollectionStr, checked, orgCode, gridId) {
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	
	if (!elementsCollectionStr) elementsCollectionStr = $("#partyMemberEcs").val();
	
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist[ "menuCode"];
	var layerName = eclist[ "menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	var menuListUrl = eclist["menuListUrl"];
	var convergeUrl = eclist["convergeUrl"];
	var treeIcon = eclist["treeIcon"];
	
	if ("1" == AUTOMATIC_CLEAR_MAP_LAYER) {
		if (typeof (checked) == "undefined") {// 汇聚图层初始调用时
			clearMyLayer();
		} else {
			clearMyLayer(layerName);
		}
	}
	if (typeof (checked) == "undefined") {// 汇聚图层初始调用时
		$(".partyMemberBtn").remove();
	}
	
	$(".mapbox").remove();
	clearSpecialLayer(layerName);
	
	locateCenterAndLevel(gridId, currentArcgisConfigInfo.mapType);
	getArcgisDataOfGrids(gridId, orgCode, currentArcgisConfigInfo.mapType, gridLevel(orgCode) + 1);
	
	if (typeof (checked) != "undefined" && !checked) {
		return;
	}
	var gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
	var params = {};
	params.page = 1;
	params.rows = 100000;
	params.gridId = gridId;
	params.infoOrgCode = orgCode;
	if (menuListUrl && menuListUrl.indexOf('?') == -1) {
		menuListUrl += '?t=' + Math.random();
	} else {
		menuListUrl += '&t=' + Math.random();
	}
	layer.load(0);
	$.ajax({
		url: convergeUrl,
		type: 'POST',
		data: params,
		dataType: "json",
		complete:function() {
			layer.closeAll('loading');
		},
		error: function(data) {
			$.messager.alert('友情提示','获取图层【'+layerName+'】数据出错!','warning');
		},
		success: function(list) {
			var tPathObjs = $("#titlePath").children();
			var pl = tPathObjs.length;
			if (pl > 1) {
				var tagName = tPathObjs.eq(pl - 1).prop("tagName");
				if (tagName == "SPAN") tPathObjs.eq(pl - 2).click();
			}
			
			var results = "";
			if (list && list.length > 0) {
				var map = new HashMap();
				for (var i = 0; i < list.length; i++) {
					var val = list[i];
					results = results + "," + val["ID_"];
					map.put(val["ID_"] + "", val["TOTAL_"] + (val["COLOR_"] ? "-" + val["COLOR_"] : ""));
				}
				results = results.substring(1, results.length);
				
				gisUrl = js_ctx + gisUrl + results;
				
				var opt = {};
				opt.w = menuSummaryWidth;
				opt.h = menuSummaryHeight;
				opt.ecs = elementsCollectionStr;
				opt.gridId = gridId;
				opt.url = gisUrl;
				opt.getText = function(data) {
					var str = map.get(data.wid + "");
					var strs = str.split("-");
					var style = "";
					var maparrow = "maparrow";
					if (strs.length > 1) {
						style = 'border:2px solid ' + strs[1] + ';font-weight:bold;color:' + strs[1];
						maparrow = "maparrow_red";
					}
					var html = '<div class="mapbox" style="'+style+'"><i class="mapicon"><img src="' + uiDomain + treeIcon + '" width="14" height="14" /></i>'+strs[0]+'<div class="'+maparrow+'"></div></div>';
					return html;
				};
				// customDivZhuanTi(layerName, opt);
                if (menuSummaryWidth == "null") {
                    menuSummaryWidth = 400;
                }
                if (menuSummaryHeight == "null") {
                    menuSummaryHeight = 260;
                }
				var _opt = opt || {};
                var cOrgCode = orgCode;
                var level = gridLevel(cOrgCode);
                
                if (level >= 5) {
                    showObjectList(elementsCollectionStr);
                } else {
                    _opt.createDlg = function(data) {
                        layer.load(0);
                        var cGridId = data.wid;
                        var cOrgCode = data.infoOrgCode;
                        var level = gridLevel(cOrgCode);
                        renderConvergeBackBtn(elementsCollectionStr, cOrgCode, orgCode, gridId);
                        if (level >= 5 || (menuCode == "statGlobalEyes" && level>=4)) {//全球眼汇聚功能要在乡镇级开始撒点
                        	showObjectList(elementsCollectionStr, cGridId, cOrgCode);
                        } else {
                            renderConverge(elementsCollectionStr, true, cOrgCode, cGridId);
                        }
                        layer.closeAll('loading');
                        return null;
                    };
                    $("#map"+currentN).ffcsMap.renderDiv(layerName, _opt);
				}
			}
		}
	});
}

function renderConvergeBackBtn(elementsCollectionStr, cOrgCode, orgCode, gridId) {
	if (!cOrgCode) cOrgCode = $("#orgCode").val();
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	if (cOrgCode == orgCode) {// 隐藏按钮
		$('.partyMemberBtn').remove();
	} else {
		var html = '<div class="partyMemberBtn" orgCode="'+orgCode+'" gridId="'+gridId+'" style="position:absolute;z-index:10;left:100px;top:45px;width:36px;height:36px;" title="返回" onclick="$(this).remove();renderConverge(\''+elementsCollectionStr+'\', true, \''+orgCode+'\', '+gridId+');"><img src="'+js_ctx+'/images/fallback.png" /></div>';
		$('body').append(html);
	}
}

function getUrbanApi() {
	if ('SMART_CITY' == $("#homePageType").val()) {
		if ($("#statisticsMsgurbanObject").find('iframe').length > 0) {
			if (typeof $("#statisticsMsgurbanObject").find('iframe')[0].contentWindow.getZtreeObj == "function") {
				return $("#statisticsMsgurbanObject").find('iframe')[0].contentWindow;
			}
		}
		if (typeof window.frames['statisticsMsgurbanObject'].getZtreeObj == "function") {
			return window.frames['statisticsMsgurbanObject'];
		}
	} else {
		if (typeof window.frames['get_grid_name_frme'].getZtreeObj == "function") {
			return window.frames['get_grid_name_frme'];
		}
	}
	
	return null;
}

function renderUrbanObj(elementsCollectionStr, checked, orgCode, gridId,length) {

	if (!orgCode) {
		if ($('.partyMemberBtn').length > 0) {
			var backBtnObj = $('.partyMemberBtn').eq(0);
			orgCode = backBtnObj.attr("cOrgCode");
		} else {
			orgCode = $("#orgCode").val();
		}
	}
	if (!gridId) {
		if ($('.partyMemberBtn').length > 0) {
			var backBtnObj = $('.partyMemberBtn').eq(0);
			gridId = backBtnObj.attr("cGridId");
		} else {
			gridId = $("#gridId").val();
		}
	}
	
	if (!elementsCollectionStr) elementsCollectionStr = $("#partyMemberEcs").val();
	
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuCode = eclist[ "menuCode"];
	var layerName = eclist[ "menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	var menuListUrl = js_ctx + "/zhsq/map/gisstat/gisStat/getUrbanCount.jhtml";
	var treeIcon = eclist["treeIcon"];
	
	if ("1" == AUTOMATIC_CLEAR_MAP_LAYER) {
		if (typeof (checked) == "undefined") {// 汇聚图层初始调用时
			clearMyLayer();
		} else {
			clearMyLayer(layerName);
		}
	}
	
	clearSpecialLayer(layerName);

	locateCenterAndLevel(gridId, currentArcgisConfigInfo.mapType);
	getArcgisDataOfGrids(gridId, orgCode, currentArcgisConfigInfo.mapType, gridLevel(orgCode) + 1);

	if (length == 0 || (typeof (length) == "undefined" && typeof (checked) != "undefined" && !checked)){
		return;
	}
	var urbanApi = getUrbanApi();
	var gisUrl = "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.jhtml?showType=2&ids=";
	var params = {};
	params.page = 1;
	params.rows = 100000;
	params.gridId = gridId;
	params.infoOrgCode = orgCode;
	if (urbanApi != null) {
		var urbanCode = urbanApi.getSelectedUrbanCode();
		params.urbanCode = urbanCode;
	}
	if (menuListUrl && menuListUrl.indexOf('?') == -1) {
		menuListUrl += '?t=' + Math.random();
	} else {
		menuListUrl += '&t=' + Math.random();
	}
	
	layer.load(0);
	$.ajax({
		url: menuListUrl,
		type: 'POST',
		data: params,
		dataType: "json",
		complete:function() {
			layer.closeAll('loading');
		},
		error: function(data) {
			$.messager.alert('友情提示','获取图层【'+layerName+'】数据出错!','warning');
		},
		success: function(list) {
			var results = "";
			if (list && list.length > 0) {
				var map = new HashMap();
				for (var i = 0; i < list.length; i++) {
					var val = list[i];
					results = results + "," + val["ID_"];
					map.put(val["ID_"] + "", val["list"]);

				}
				results = results.substring(1, results.length);
				
				gisUrl = js_ctx + gisUrl + results;

				var opt = {};
				opt.w = menuSummaryWidth;
				opt.h = menuSummaryHeight;
				opt.ecs = elementsCollectionStr;
				opt.gridId = gridId;
				opt.url = gisUrl;
				opt.getText = function(data) {
					var html = '<div class="mapbox">';
					var list = map.get(data.wid + "");
					for(var i=0;i<list.length;i++){
						var val = list[i];
						var img = val["IMG"];
						var tatal = val["TOTAL"];
						html=html+'<i class="mapicon"><img src="' + uiDomain + img + '" width="14" height="14" /></i>'+tatal+'</br>';
					}
					html=html+'<div class="maparrow"></div></div>';
					return html;
				};
				// customDivZhuanTi(layerName, opt);
				
				var _opt = opt || {};
				_opt.createDlg = function(data) {
					layer.load(0);
					var cGridId = data.wid;
					var cOrgCode = data.infoOrgCode;
					var level = gridLevel(cOrgCode);
					renderUrbanBackBtn(elementsCollectionStr, cOrgCode, cGridId, orgCode, gridId);
					if (level >= 5) {
						renderUrbanObj(elementsCollectionStr, false);
						var api = getUrbanApi();
						if (api != null) {
							var nodes = api.getCheckedNodes();
							for (var i = 0; i < nodes.length; i++) {
								var treeIcon = nodes[i].icon.replace('.png', '_Marker.png');
								showMarkersForUrbanObj(elementsCollectionStr, nodes[i].name, nodes[i].value, treeIcon, nodes[i].checked, {}, cOrgCode, cGridId);
							}
						}
					} else {
						renderUrbanObj(elementsCollectionStr, true, cOrgCode, cGridId);
					}
					layer.closeAll('loading');
					return null;
				};
				$("#map"+currentN).ffcsMap.renderDiv(layerName, _opt);
			}
		}
	});
}

function renderPartyBackBtn(elementsCollectionStr, cOrgCode, orgCode, gridId) {
	if (!cOrgCode) cOrgCode = $("#orgCode").val();
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	if (cOrgCode == orgCode) {// 隐藏按钮
		$('.partyMemberBtn').remove();
	} else {
		var html = '<div class="partyMemberBtn" orgCode="'+orgCode+'" gridId="'+gridId+'" style="position:absolute;z-index:10;left:100px;top:45px;width:36px;height:36px;" title="返回" onclick="$(this).remove();renderPartyMember(\''+elementsCollectionStr+'\', true, \''+orgCode+'\', '+gridId+');"><img src="'+js_ctx+'/images/fallback.png" /></div>';
		$('body').append(html);
	}
}

function renderUrbanBackBtn(elementsCollectionStr, cOrgCode, cGridId, orgCode, gridId) {
	if (!cOrgCode) cOrgCode = $("#orgCode").val();
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	if (cOrgCode == orgCode) {// 隐藏按钮
		$('.partyMemberBtn').remove();
	} else {
		var html = '<div class="partyMemberBtn" cOrgCode="'+cOrgCode+'" cGridId="'+cGridId+'" style="position:absolute;z-index:10;left:100px;top:45px;width:36px;height:36px;" title="返回" onclick="$(this).remove();renderUrbanObj(\''+elementsCollectionStr+'\', true, \''+orgCode+'\', '+gridId+');"><img src="'+js_ctx+'/images/fallback.png" /></div>';
		$('body').append(html);
	}
}

var firstGridId, firstGridCode, firstGridName, firstInfoOrgCode;
function renderGridBackBtn(cGridId, cGridCode, cGridName, cInfoOrgCode, pGridId) {

	//if (cOrgCode == firstInfoOrgCode) {// 隐藏按钮
	//	firstInfoOrgCode = "";
	//	$('.gridBtn').remove();
	//} else {
		var html = '<div class="gridBtn" cOrgCode="'+cInfoOrgCode+'" cGridId="'+cGridId+'" style="position:absolute;z-index:10;left:100px;top:45px;width:36px;height:36px;" title="返回" onclick="gridBackBtnCallBack('+cGridId+', \''+cGridCode+'\', \''+cGridName+'\', \''+cInfoOrgCode+'\');$(this).remove();getArcgisDataOfChildrenGrids('+pGridId+');"><img src="'+js_ctx+'/images/fallback.png" /></div>';
		$('body').append(html);
	//}
}

function gridBackBtnCallBack(cGridId, cGridCode, cGridName, cInfoOrgCode){
	locateCenterAndLevel(cGridId,currentArcgisConfigInfo.mapType);
	changeGridInfo(cGridId, cGridCode, cGridName, cInfoOrgCode);
	if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
		eval(currentLayerListFunctionStr);
	}
	if (cInfoOrgCode == firstInfoOrgCode) {// 隐藏按钮
		firstInfoOrgCode = "";
		$('.gridBtn').remove();
	}
}

function markPartyOrg(elementsCollectionStr, url) {
	var imageurl = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_radar.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('urgencyEventLayer',url, 0, true, imageurl, 60, 60);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:330}, "urgencyEventLayer", '事件', getInfoDetailOnMap, showEventDetail);
	
	var mapt = currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.locationPoint({w:340,h:330},'eventLayer', id, '事件信息', imageurl, 60, 60, function(data){
        url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent/'+ eventType +'.jhtml?eventType=map&modeType='+modeType+'&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId+'&mapt='+mapt;
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEventDetail);
}

function gridLevel(infoOrgCode) {
	if (infoOrgCode) {
		switch (infoOrgCode.length) {
		case 2:// 省 1
			return 1;
		case 4:// 市 2
			return 2;
		case 6:// 区 3
			return 3;
		case 9:// 街道 4
			return 4;
		case 12:// 社区 5
			return 5;
		case 15:// 网格 6
			return 6;
		}
	}
	return -1;
}

function excuteSPGISorARCGIS(spgisFn, arcgisFn) {
	if (typeof MMApi != "undefined") {
		if (typeof spgisFn == "function") {
			spgisFn.call(this);
		}
	} else {
		if (typeof arcgisFn == "function") {
			arcgisFn.call(this);
		}
	}
}
var gHeatLayer = null;
function optHeatMap(isOpen) {
	if (typeof MMApi != "undefined") {
		MMApi.heatMap(isOpen);
	} else {
		layer.load(0);
		if (gHeatLayer == null) {
			gHeatLayer = $("#map" + currentN).ffcsMap.createHeatMap("heatLayer", {
				"useLocalMaximum": true,
		        "radius": 20,
		        "gradient": {
		            "0.15": "rgb(000,000,255)",
		            "0.65": "rgb(000,255,255)",
		            "0.75": "rgb(000,255,000)",
		            "0.85": "rgb(255,255,000)",
		            "1.00": "rgb(255,000,000)"
		        }
			});
		}
		gHeatLayer.clearData();
		if (isOpen) {
			var dataUrl = js_ctx + '/zhsq/map/arcgis/arcgisdataofgoods/getBizLocateInfoList.jhtml?bizType=01';
			$("#map" + currentN).ffcsMap.heatMap(gHeatLayer, dataUrl, function() {
				layer.closeAll('loading');
			});
		} else {
			layer.closeAll('loading');
		}
	}
}

function HeatMapLayer(type) {
	var me = this;
	var _url = "";
	var _type = type;

	me.heatLayer = $("#map"+currentN).ffcsMap.createHeatMap("heatLayer", {
		"useLocalMaximum": true,
		"radius": 20,
		"gradient": {
			"0.15": "rgb(000,000,255)",
			"0.65": "rgb(000,255,255)",
			"0.75": "rgb(000,255,000)",
			"0.85": "rgb(255,255,000)",
			"1.00": "rgb(255,000,000)"
		}
	});

	this.showHeatMap = function() {
		if (_type == "spectialPopulation") {
			$("#heatTrafficMapDiv").hide();
			$("#heatMapDiv").show();
			$("#heatMapDiv-close").show();
			$("#spectialPopulationDiv").show();
		}else if(_type == "trafficAccident"){
			$("#heatMapDiv").hide();
			$("#heatTrafficMapDiv").show();
			$("#heatTrafficMapDiv-close").show();
			$("#trafficAccidentDiv").show();
		}else{
			$("#heatMapDiv").hide();
			$("#heatTrafficMapDiv").hide();
		}
		me.queryData();
	};


	this.queryData = function() {
		layer.load(0);
		var dataUrl = _url;
		if (_type == "spectialPopulation") {
			var ary = new Array();
			$("input[name='spectialPopulation']:checked").each(function () {
				ary.push("'" + $(this).val() + "'");
			});
			dataUrl += "&spectialPopulations=" + ary.join(",");
		}
		me.clearHeatMap();
		$("#map"+currentN).ffcsMap.heatMap(me.heatLayer, dataUrl, function() {
			layer.closeAll('loading');
		});
	};

	this.clearHeatMap = function() {
		if (typeof me.heatLayer != "undefined") {
			me.heatLayer.clearData();
		}
	};

	this.setUrl = function(url) {
		_url = url;
	};

	return { setUrl : this.setUrl, show : this.showHeatMap, hide : this.clearHeatMap, query: this.queryData };
}

function closeHeatMapLayer() {
	currentLayerLocateFunctionStr = "";
	$("#heatMapDiv").hide();
	$("#heatTrafficMapDiv").hide();
	if (typeof heatLayers != "undefined" && heatLayers != null) {
		for (var k in heatLayers) {
			heatLayers[k].hide();
		}
	}
	if(typeof gHeatLayer != "undefined" && gHeatLayer != null){
		gHeatLayer.clearData();
	}
}

var lastMenuCode;
function mapEchartZoomEnd(){
	if(lastMenuCode && lastMenuCode == 'ORG_PRODUCTION'){
		var top_ = map_level[$("#map"+currentN).ffcsMap.getMap().getLevel()];
		$(".map_echarts").css({top:top_});
	}
}
var map_level={11:'20px',12:'60px',13:' 120px',14:'250px',15:'510px',16:'1030px',17:'2060px',18:'4130px',19:'8270px'};
function renderJJStatistics(layerName,data,type,typeNum) {
	var ffcsMap = $("#map"+currentN).ffcsMap; 
	ffcsMap.clear({layerName : "STATISTICS_LAYER"});
	var level = ffcsMap.getMap().getLevel();
	var gridId = $("#gridId").val();
	var opt = {gridId:gridId,datas:data,
			getText:function(data) {
				var div = document.getElementById("pie_"+data.INFO_ORG_CODE);
				if(div){
					$(div).parent().remove();
				}
				return "<div id='pie_"+data.INFO_ORG_CODE+"' class='map_echarts' style='width:80px;height:80px;position:relative;left:-25px;top:"+map_level[level]+";'></div>";
			},_createDlg : function(data) {	return null;}};
	ffcsMap.renderDivForData(layerName, opt);
	if(type == 'gpbr' || type =='retail' || type == 'ldmj'){//gpbr,retail,ldmj
		var option = {color:['rgba(159,215,251,0.8)','rgba(255,152,115,0.8)','rgba(218,115,214,0.8)'],tooltip:{show:false},legend:{show:false,data:['收入']},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'category',data:['收入'],show:false}],yAxis:[{type:'value',show:false}],grid:{x:5,y:0,x2:10,y2:5,borderWidth:0},
				series:[{name:'收入',type:'bar',stack:'总量',itemStyle:{normal:{label:{formatter:("{c}"+(type == 'ldmj'?'%':'')),show:true,position:'insideRight'},color:(function(){return 'rgba(135,206,250,0.8)';})()}},data:[]}]};
		var col = {'gpbr':'GPBR_SUM','retail':'VAL_','ldmj':'IDX_VAL'},column=col[type];
		for(var i=0,l=data.length;i<l;i++){
			option.series[0].data=[{value:fnNum(data[i][column]), name:'收入',orgCode:data[i].INFO_ORG_CODE,orgName:data[i].REGION_NAME}];
			var myChart = echarts.init(document.getElementById('pie_'+data[i].INFO_ORG_CODE));
			myChart.setOption(option);
			myChart.on('click',function(param){
				if(currLayerEchartIndex){
					layer.close(currLayerEchartIndex);
				}
				openChartClick(param.data.orgName,param.data.orgCode,js_ctx + '/zhsq/szzg/zgStatisticsController/showJT.jhtml?page=dongtai_'+type+'&orgCode='+param.data.orgCode+(typeNum?('_'+typeNum):''),955,450);
			});
		}
	}else{//gdp,invest
		var option = {color:['rgba(159,215,251,0.8)','rgba(255,152,115,0.8)','rgba(218,115,214,0.8)'],
		tooltip:{show:false},	legend:{show:false,data:['一产','二产','三产']},toolbox:{show:false},calculable:false,
			series:[{name:'',type:'pie',itemStyle:{normal:{label:{show:true,position:'inner',formatter:function(para,a,b,c){
return b?b:'';}},labelLine:{show:false}}},radius:'100%',data:[]}]};
		for(var i=0,l=data.length;i<l;i++){
			option.series[0].data=[
			{value:YiNum(fnNum(data[i].ONE_SUM)), name:'一产',orgCode:data[i].INFO_ORG_CODE,orgName:data[i].REGION_NAME},
			{value:YiNum(fnNum(data[i].TWO_SUM)), name:'二产',orgCode:data[i].INFO_ORG_CODE,orgName:data[i].REGION_NAME},
			{value:YiNum(fnNum(data[i].THREE_SUM)), name:'三产',orgCode:data[i].INFO_ORG_CODE,orgName:data[i].REGION_NAME}];
			var myChart = echarts.init(document.getElementById('pie_'+data[i].INFO_ORG_CODE));
			myChart.setOption(option);
			myChart.on('click',function(param){
				if(currLayerEchartIndex){
					layer.close(currLayerEchartIndex);
				}
				openChartClick(param.data.orgName,param.data.orgCode,js_ctx + '/zhsq/szzg/zgStatisticsController/showJT.jhtml?page=dongtai_'+type+'&orgCode='+param.data.orgCode,955,450);
			});
		}
	}
	
}
function renderJJStatistics_YP(layerName,data,type,typeNum) {
	var ffcsMap = $("#map"+currentN).ffcsMap;
	ffcsMap.clear({layerName : "STATISTICS_LAYER"});
	var level = ffcsMap.getMap().getLevel();
	var gridId = $("#gridId").val();
	var opt = {gridId:gridId,datas:data,
			getText:function(data) {
				var div = document.getElementById("pie_"+data.INFO_ORG_CODE);
				if(div){
					$(div).parent().remove();
				}
				return "<div id='pie_"+data.INFO_ORG_CODE+"' style='width:80px;height:80px;position:relative;left:-40px;top:"+map_level[level]+";'></div>";
			},_createDlg : function(data) {	return null;}};
	ffcsMap.renderDivForData(layerName, opt);
	
		var option = {color:['rgba(159,215,251,0.5)','rgba(255,152,115,0.5)','rgba(218,115,214,0.5)'], tooltip:{show:false},legend:{show:false,data:['收入']},toolbox:{show:false},calculable:false,
				  xAxis:[{type:'category',data:['收入'],show:false}],yAxis:[{type:'value',show:false}],grid:{x:5,y:0,x2:10,y2:5,borderWidth:0},
				series:[{name:'收入',type:'bar',stack:'总量',itemStyle:{normal:{label:{formatter:("{c}"+(type == 'ldmj'?'%':'')),show:true,position:'insideRight',textStyle:{color:'rgb(0,0,0)'}},color:(function(){return 'rgba(255,180,0,0.6)';})()}},data:[]}]};
		var col = {'invest':'GDP_SUM','retail':'VAL_','ldmj':'IDX_VAL'},column=col[type];
		
		for(var i=0,l=data.length;i<l;i++){
			option.series[0].data=[{value:fnNum(data[i][column]), name:'收入',orgCode:data[i].INFO_ORG_CODE,orgName:data[i].REGION_NAME}];
			var myChart = echarts.init(document.getElementById('pie_'+data[i].INFO_ORG_CODE));
			myChart.setOption(option);
			myChart.on('click',function(param){
				if(currLayerEchartIndex){
					layer.close(currLayerEchartIndex);
				}
				openChartClick(param.data.orgName,param.data.orgCode,js_ctx + '/zhsq/szzg/zgStatisticsController/showJT.jhtml?page=dongtai_'+type+'_yp&orgCode='+param.data.orgCode+(typeNum?('_'+typeNum):''),955,450);
			});
		}
	
	
}

function fnNum(n){return n?n:0;}
function YiNum(n){return Math.round(n/10000)/10;}
var currLayerEchartIndex = null;
function openChartClick(title,orgCode,url,width,height){
		var pageWidth = window.innerWidth-282-967;
		currLayerEchartIndex = layer.open({
		id: 'statisticsMsg'+orgCode,
		closeBtn:'1',
        type: 2,
		title: [title, 'background-color: rgba(19,55,81,0); border-bottom : 0px solid rgba(19,55,81,0.8); color: #F8F8F8; font-size: 20px;'],
        shadeClose: false,
        shade: 0,
		offset: ['0px',(pageWidth<0?0:pageWidth)+'px'],
        skin: 'layerSkin',
        area: [width+'px', height+'px'],
        content: url
    });

}
var _measure;
function eventMeasure(other,num){
	if(other){
		var iframe = document.getElementById("layui-layer-iframe"+currLayerOpenIndex).contentWindow;
		if(other =='play'){
			var paly = $("#play_box");
			if(paly.hasClass("on")){
				paly.removeClass("on");
				iframe.clear_();
			}else{
				paly.addClass("on");
				iframe.showHeat();
			}
		}else if(other =='showHeat'){
			iframe.showHeat(num);
		}else if(other =='clear'){
			if(areaGraphicsArr[0]){
				var graphics = $("#map"+currentN).ffcsMap.getMap().graphics;
				for(var i=0;i<areaGraphicsArr[0].length;i++){
					graphics.remove(areaGraphicsArr[0][i]);
				}
					graphics.remove(allPolygonGrapLine);
				_measure.measureClearAll();
				$("#map"+currentN).ffcsMap.clear({layerName : "EVENT_STATISTICS"});
				$("#NorMapOpenDiv").hide();
			}
		}
	}else{
		if(_measure){
			if(areaGraphicsArr[0]){
				$("#NorMapOpenDiv").hide();
				var graphics = $("#map"+currentN).ffcsMap.getMap().graphics;
				for(var i=0;i<areaGraphicsArr[0].length;i++){
					graphics.remove(areaGraphicsArr[0][i]);
				}
					graphics.remove(allPolygonGrapLine);
				_measure.measureClearAll();
				$("#map"+currentN).ffcsMap.clear({layerName : "EVENT_STATISTICS"});
				
			}
		}
		_measure = $("#map"+currentN).ffcsMap.measureAreaD(eventMeasureCallBack);
	}
	
	
}

function eventMeasureCallBack(paths,label){
	var x=0,y=0;var path=paths[0][0]+','+paths[0][1];
	for(var i=paths.length-1;i>=0;i--){
		x += paths[i][0],y += paths[i][1];
		path+=','+paths[i][0]+','+ paths[i][1];
	}
	x = x/paths.length,y = y /paths.length;
	paths.push(paths[0][0],paths[0][1]);
	var icoUrl = js_ctx+'/images/map/openlayers/build_locate_point.png';
	var url = '/zhsq/szzg/eventController/kuangxuanPage.jhtml?points='+path+'&area='+parseFloat(label)+'&minID='+document.getElementById("minID").value+
	'&maxID='+document.getElementById("maxID").value;
	if(lastDateNo >0){
		url += '&lastDateNo='+lastDateNo;
	}
	var data = [{coordinates:[[]],x:x,y:y,gridName:'框选统计',_oldData:{wid:1,elementsCollectionStr:'menuSummaryUrl_,_'+url}}];
	$("#map"+currentN).ffcsMap.render('EVENT_STATISTICS',undefined,0,undefined,icoUrl,3,3,undefined,true,undefined,undefined,{},false,data);
	getDetailOnMapOfListClickOnTitle('menuSummaryWidth_,_320,_,menuSummaryHeight_,_106,_,menuLayerName_,_EVENT_STATISTICS,_,smallIcoSelected_,_'+icoUrl+',_,menuName_,_框选统计,_,menuSummaryUrl_,_'+url+'&wid=',320,110,1);
}