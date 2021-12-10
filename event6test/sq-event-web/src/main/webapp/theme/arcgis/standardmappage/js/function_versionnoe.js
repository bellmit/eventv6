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
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	var callBack = analysisOfElementsCollection(elementsCollectionStr,"callBack");
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > "+menuName);
	$("."+menuCode).show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = menuCode;
	currentLayerListFunctionStr="";
	currentClassificationFuncStr=callBack+"(\""+elementsCollectionStr+"\")";
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
function showObjectList(elementsCollectionStr) {
	var menuName = analysisOfElementsCollection(elementsCollectionStr,"menuName");
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	var callBack = analysisOfElementsCollection(elementsCollectionStr,"callBack");
	var htmlstr = $("#titlePath").html();
	var htmls = htmlstr.split(" &gt; ");
	if(elementsCollectionStr!=undefined){
		htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
	}
	
	clearMyLayer();
	$("#titlePath").html(htmlstr + " > "+menuName);
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	getObjectListUrl(elementsCollectionStr);
	currentLayerListFunctionStr = callBack+"('"+elementsCollectionStr+"')";
}

function showZhouBianObjectList(name,zhoubianName,x,y,distance,mapType) {
	zhoubianListShow();
	var htmlstr = $("#titlePathZhouBian").html();
	$("#titlePathZhouBian").html("周边资源" + " > "+name);
 	$("#zhoubian_list_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$(".NorListZhouBian").show();
	getObjectListZhouBianUrl(zhoubianName,x,y,distance,mapType);
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
	url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType='+zhoubianName+'&x='+x+'&y='+y+'&distance='+distance+'&mapType='+mapType+'&infoOrgCode='+$("#orgCode").val()+'&t='+Math.random();
	if(url != ""){
		showZhouBianListPanel(url);
	}
}

//显示重点人口信息
function showZhouBianListPanel(url){
    $("#zhoubian_list_frme").attr("src",url); 
}

function getObjectListUrl(elementsCollectionStr) {
	var menuCode = analysisOfElementsCollection(elementsCollectionStr,"menuCode");
	if(menuCode == 'organizationImportUnit') {
	
	}else {
		var menuListUrl = js_ctx + analysisOfElementsCollection(elementsCollectionStr,"menuListUrl");
		if(menuListUrl.indexOf("?")<=0){
			menuListUrl += "?t="+Math.random();
		}
		menuListUrl += "&gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&orgCode="+$("#orgCode").val()+"&infoOrgCode="+$("#orgCode").val();
		
		var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+  
           '<input id="elementsCollectionStr" name="elementsCollectionStr" type="hidden" value="'+elementsCollectionStr+'"/>'+  
           '</form>';
        document.getElementById('get_grid_name_frme').contentWindow.document.write(html);  
		document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
		//$("#get_grid_name_frme").attr("src",menuListUrl); 
	}
}
//显示重点人口信息
function showListPanel(url){
    $("#get_grid_name_frme").attr("src",url); 
}
//显示事件采集录入-安全工作
function showAddEventPanelForAQGZ(){
	mapObjectName = "事件采集";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml?trigger=AQGZ&typesForList=06';
	showMaxJqueryWindow("新增事件采集", url, 800, 400);	
}
//显示事件采集录入
function showAddEventPanel(){
	mapObjectName = "事件采集";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml';
	showMaxJqueryWindow("新增事件采集", url, 800, 400);	
}
//显示事件语音呼叫
function showVoiceCallPanel(){
	//引用zzgrid链接
	//var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    //var url =  sq_zzgrid_url +'/zzgl/map/zhddData/centerControl/voiceCall.jhtml?gridId='+$("#gridId").val();
	var url=js_ctx+'/zhsq/map/arcgis/arcgisCenterControl/voiceCall.jhtml?gridId='+$("#gridId").val();	
	showMaxJqueryWindow("语音呼叫", url, 720,463);
}

function showEventCollectEntryPanel(){
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	var url =  sq_zzgrid_url+'/zzgl/event/outPlatform/eventCollectEntry.jhtml';
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
    $(".SelectTree").css("display","none");
    if(gridCode != $("input[name='gridCode']").val()) {
    	$("#changeGridName").text(gridName);
	    $("input[name='gridId']").val(gridId);
	    $("input[name='gridCode']").val(gridCode);
		$("input[name='gridName']").val(gridName);
		$("input[name='gridLevel']").val(gridLevel);
		$("input[name='orgCode']").val(orgCode);
		var level = (parseInt(gridLevel) < 6) ? parseInt(gridLevel)+1 : parseInt(gridLevel)
		//document.getElementById("gridLevelName"+level).checked = true;
		changeCheckedAndStatus(gridLevel,level)
		
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

