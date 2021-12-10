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
	currentLayerLocateFunctionStr="";
}
function people(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 人");
	
	$(".people").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "people";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
}
function world(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 地");
	
	$(".world").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "world";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
}
function metter(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 事");
	if(mapObjectName!=undefined && mapObjectName!=""){
		mapObjectName == "";
	}
	$(".metter").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "metter";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
}
function thing(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 物");
	
	$(".thing").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "thing";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
}
function situation(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 情");
	
	$(".situation").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "situation";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
}
function organization(){
	$("#titlePath").html("<a href='javascript:void(0);' onclick='firstall()'>专题图层</a> > 组织");
	
	$(".organization").show();
	$("."+nowAlphaBackShow).hide();
	$("#searchBtnId").hide();
	nowAlphaBackShow = "organization";
	currentLayerListFunctionStr="";
	currentLayerLocateFunctionStr="";
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

function showObjectList(objectName,resourceType) {
	var htmlstr = $("#titlePath").html();
	var htmls = htmlstr.split(" &gt; ");
	if(resourceType == undefined || resourceType == ''){
		if("人" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='people()'>"+htmls[htmls.length -1]+"</a>")
		}else if("地" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='world()'>"+htmls[htmls.length -1]+"</a>")
		}else if("事" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='metter()'>"+htmls[htmls.length -1]+"</a>")
		}else if("物" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='thing()'>"+htmls[htmls.length -1]+"</a>")
		}else if("情" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='situation()'>"+htmls[htmls.length -1]+"</a>")
		}else if("组织" == htmls[htmls.length -1]) {
			htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='organization()'>"+htmls[htmls.length -1]+"</a>")
		}
	}
	var tmpObjectName = objectName;
	if(objectName.split("-").length > 1){
		objectName = objectName.split("-")[0];
	}else if(objectName.split("_").length > 1){
		objectName = objectName.split("_")[0];
	}
	$("#titlePath").html(htmlstr + " > "+objectName);
	objectName = tmpObjectName;
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	mapObjectName = objectName;//用于回调列表刷新
	getObjectListUrl(objectName);
	currentLayerListFunctionStr = "getObjectListUrl('"+objectName+"')";
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

function getObjectListUrl(objectName) {
	var url;
	if("党员"==objectName){
		url= js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=partyMerber';
		url+='&themeType=standard';
		showListPanel(url);
	}
	if("退休人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=retire';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("居家养老人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=homeAge';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("服兵役人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=military';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("失业人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=unemployment';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("低保人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=welfare';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("残障人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=disability';
		url+='&standard=standard';
		showListPanel(url);
	}else if("事件查询" == objectName) {
		url="${rc.getContextPath()}/zzgl/map/zhddData/eventStat/standardPending.jhtml?gridId=${gridId}";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("重精神病人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=neuropathy';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("危险品从业人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=dangerous';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("上访人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=petition';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("吸毒人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=drugs';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("邪教人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=heresy';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("矫正人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=rectify';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("刑释解教人员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=camps';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("消防安全管理员"==objectName){
		url= js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfSafetyPersonManage.jhtml?gridId='+$("#gridId").val();
		url+='&themeType=standard';
		showListPanel(url);
	}
	if("护路护线"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfICareRoad.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("便民服务网点"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfServiceOutlets.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("企业"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfCorBase.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
		//selectOperateType('40', 1);
		//standardCorBase.ftl
	}
	if("出租屋"==objectName){
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfRentRoom.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
		//selectOperateType('9', 1);
	}
	if("网格"==objectName){
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/grid.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
		//selectOperateType('9', 1);
	}
	if("重点场所"==objectName){
		//selectOperateType('51', 1);  
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfKeyPlace.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("物业管理住宅"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=wyglzz';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("社区托管住宅"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=sqtgzz';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("单位自管住宅"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=dwzgzz';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("单位楼"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=dwl';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("写字楼"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=xzl';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("城市综合体"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=cszht';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("工地"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=gd';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("公园广场"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=gygc';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("宿舍"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=ss';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("学校"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=xx';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("民房"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=mf';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("厂房"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfBuilding.jhtml?gridId='+$("#gridId").val()+'&type=cf';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("两车管理"==objectName){
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofbuilding/toArcgisDataListOfParkingManage.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("党组织"==objectName){
		//selectOperateType('51', 1);
		var url = js_ctx+'/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfo.jhtml?gridId='+$("#gridId").val()+'&groupType=';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("消防栓"==objectName){
		//selectOperateType('47', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=22&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("公交站"==objectName){
		//selectOperateType('48', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=15&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("公共厕所"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=17&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("路灯"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=24&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("井盖"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=23&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("供水"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=9&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("供热"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=10&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("燃气"==objectName){
		//selectOperateType('49', 1);
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=11&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("护路护线队员"==objectName){
		//selectOperateType('3', 1);
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/careRoadMembers.jhtml?gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("网格员"==objectName){
		//selectOperateType('3', 1);
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdmin.jhtml?gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("全球眼"==objectName){
		//selectOperateType('4', 1);
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyes.jhtml?orgCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("全球眼GIS"==objectName){
		//selectOperateType('4', 1);
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyesGis.jhtml?orgCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("危房视频"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/spjk.jhtml?orgCode="+$("#gridCode").val() + "&eyesType=001";;
		url+='&standard=standard';
		showListPanel(url);
	}
	if("警示威慑"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/spjk.jhtml?orgCode="+$("#gridCode").val() + "&eyesType=002";;
		url+='&standard=standard';
		showListPanel(url);
	}
	if("森林视频"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/spjk.jhtml?orgCode="+$("#gridCode").val() + "&eyesType=003";;
		url+='&standard=standard';
		showListPanel(url);
	}
	if("客流视频"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/spjk.jhtml?orgCode="+$("#gridCode").val() + "&eyesType=004";;
		url+='&standard=standard';
		showListPanel(url);
	}
	if("区域视频"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/spjk.jhtml?orgCode="+$("#gridCode").val() + "&eyesType=005";;
		url+='&standard=standard';
		showListPanel(url);
	}
	if("水文监测"==objectName) {
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/swjc.jhtml?orgCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("待办事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=todo';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("待办事件-AQGZ"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=todo&type=0601,0602';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("将到期"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=todo&objectName=01';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("关注事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=attention';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("辖区事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=all';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("归档事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=history';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("关注事件-AQGZ"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=attention&type=0601,0602';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("辖区事件-AQGZ"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=all&type=0601,0602';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("归档事件-AQGZ"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofeventlocal/toArcgisDataListOfEvent.jhtml?gridId='+$("#gridId").val()+'&eventType=history&type=0601,0602';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("机关支部"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfo.jhtml?gridId='+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&groupType=1";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("村（居）党组织"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfo.jhtml?gridId='+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&groupType=2";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("网格党支部"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfo.jhtml?gridId='+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&groupType=3";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("其他类型非公党支部"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofthing/partyOrgInfo.jhtml?gridId='+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&groupType=4";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("高年龄、高党龄"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisDataOfSpecialPartyController/specialPartyInfo.jhtml?orgCode='+$("#gridCode").val()+"&partyMemberType=1,2";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("农村70周岁以上生活困难老党员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisDataOfSpecialPartyController/specialPartyInfo.jhtml?orgCode='+$("#gridCode").val()+"&partyMemberType=4";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("普通生活困难党员"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisDataOfSpecialPartyController/specialPartyInfo.jhtml?orgCode='+$("#gridCode").val()+"&partyMemberType=5";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("党廉-在办事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController/workingEvent.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("党廉-历史事件"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController/historicalEvent.jhtml?gridId='+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("人防管理员分布"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdmin.jhtml?gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("待办上报_旧"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/standardPending.jhtml?gridId="+$("#gridId").val()+"&statusName=report";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("待办分流_旧"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/standardPending.jhtml?gridId="+$("#gridId").val()+"&statusName=shunt";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("将到期_旧"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/standardPending.jhtml?gridId="+$("#gridId").val()+"&statusName=notExpire";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("已过期_旧"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/standardPending.jhtml?gridId="+$("#gridId").val()+"&statusName=expire";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("志愿者"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofpoplocal/toArcgisDataListOfPeople.jhtml?gridId='+$("#gridId").val()+'&type=volunteer';
		url+='&standard=standard';
		showListPanel(url);
	}
	if("巡逻段警"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardXldj.jhtml?gridId="+$("#gridId").val()+"&duty='004'";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("巡逻队"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardSociety.jhtml?gridId="+$("#gridId").val()+"&type='05'";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("治保会"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardSociety.jhtml?gridId="+$("#gridId").val()+"&type='03'";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("警务室"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardSociety.jhtml?gridId="+$("#gridId").val()+"&type='04'";
		url+='&standard=standard';
		showListPanel(url);
	}
	if("案件警情"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardCases.jhtml?gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("隐患"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardDangous.jhtml?gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("安全隐患"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/risk.jhtml?orgCode="+$("#orgCode").val();
		showListPanel(url);
	}
	if("派出所"==objectName){
		//url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSocietyController/standardPending.jhtml?gridId="+$("#gridId").val();
		//url+='&standard=standard';
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfResource.jhtml?resTypeId=25&gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("新经济组织"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisdataofnewgroup/toArcgisDataListOfNonPublicOrg.jhtml?gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("新社会组织"==objectName){
		url = js_ctx+"/zhsq/map/arcgis/arcgisdataofnewgroup/toArcgisDataListOfNewSocialOrg.jhtml?gridId="+$("#gridId").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("新消防栓"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfNewResource.jhtml?resTypeId=0&orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("自来水公司"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfNewResource.jhtml?resTypeId=4&orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("天然水源"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfNewResource.jhtml?resTypeId=3&orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("校园周边"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfCampus.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("门店"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfStore.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if("园林绿化"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfLandscape.jhtml?orgCode="+$("#orgCode").val();
		url+='&standard=standard';
		showListPanel(url);
	}
	if ("消防队" == objectName) {
		var url = js_ctx
				+ "/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfFireTeam.jhtml?infoOrgCode="
				+ $("#orgCode").val();
		showListPanel(url);
	}
	if ("重点单位" == objectName) {
		var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianPage.jhtml?zhoubianType=zhouBianStatOfImportUnitService&infoOrgCode='+$("#orgCode").val()+'&t='+Math.random();
		showListPanel(url);
	}
	if("安监队伍"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfControlsafetyRanks.jhtml?bizType=3&orgCode="+$("#orgCode").val();
		showListPanel(url);
	}
	if("机构队伍"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfControlsafetyRanks.jhtml?bizType=0&orgCode="+$("#orgCode").val();
		showListPanel(url);
	}
	if("群防群治队伍"==objectName){
		var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofgoods/toArcgisDataListOfControlsafetyRanks.jhtml?bizType=1&orgCode="+$("#orgCode").val();
		showListPanel(url);
	}
	if("安监企业"==objectName){
		url=js_ctx+'/zhsq/map/arcgis/arcgisdataofregion/toArcgisDataListOfAnjianCorBase.jhtml?gridId='+$("#gridId").val()+'&cateVals=002';
		url+='&standard=standard';
		showListPanel(url);
	}
}
//显示重点人口信息
function showListPanel(url){
    //$("#message").css("display","block");
    //$("#personType").css("display","none");
    $("#get_grid_name_frme").attr("src",url); 
}
//显示事件采集录入-安全工作
function showAddEventPanelForAQGZ(){
	mapObjectName = "新增事件信息";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml?trigger=AQGZ&typesForList=0601,0602';
	showMaxJqueryWindow("新增事件信息", url, 800, 400);	
}
//显示事件采集录入
function showAddEventPanel(){
	mapObjectName = "新增事件信息";
    url=js_ctx+'/zhsq/event/eventDisposalController/toAddEventByType.jhtml';
	showMaxJqueryWindow("新增事件信息", url, 800, 400);	
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
	var url =  sq_zzgrid_url+'/zzgl/event/outPlatform/eventCollectEntry.jhtml?arcgisFlag=true&close=no';
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
   	  url+='&groupType=0';
      title = "纪工委";
      width=1000;
      height=400;
    }else if(type=='cj'){  //组织架构-村居
      url = sq_zzgrid_url+'/zzgl/map/ztywData/inspectOrganization/inspectOrgInfo.jhtml?gridId='+$("#gridId").val();
   	  url+='&groupType=2';
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
		changeCheckedAndStatus(gridLevel,level)
		locateCenterAndLevel(gridId,currentArcgisConfigInfo.mapType);
		clearMyLayer();
		getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);
		
		if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
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
	document.getElementById("buildName0").checked=false;
	if (level-1 >= 5) {
		document.getElementById("buildName0").checked=true;
	}else {
		document.getElementById("buildName0").checked=false;
	}
	
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
/******END LYJJ********/

