
/**
 * 2014-05-26 liushi add arcgis地图配置信息
 *
 **/
var arcgisConfigInfos;
var currentArcgisConfigInfo;
//记录当前显示地图图层的方法以及所含的参数
var currentLayerLocateFunctionStr="";
var currentListNumStr="";//列表页码已经加载的页码记录
var currentLayerName="";//当前列表加载的图层名称
var currentLayerListFunctionStr="";
var ykFeatureUrl = "http://gistest.fjsq.org/ArcGIS/rest/services/FeaturesTest/FeatureServer/2";
var currentArgs = [];
function addCurrentStatus(layerName, funcArgs) {
	clearCurrentStatus(layerName);
	var cStatus = {
		layerName : layerName,
		funcArgs : funcArgs
	};
	currentArgs.push(cStatus);
}
function clearCurrentStatus(layerName) {
	if (layerName == undefined) {
		currentArgs = [];
	} else {
		for (var i = currentArgs.length - 1; i >= 0; i--) {
			if (currentArgs[i].layerName == layerName) {
				currentArgs.splice(i, 1);
			}
		}
	}
}

function loadArcgis(arcgisConfigInfo,mapDivId,jsSliderDivId,switchMapId,backFn) {
	switchArcgis(arcgisConfigInfo,0,backFn);

}
var winresizeflag=0;
var currentMapDiv = null;
var currentMapType = 0;
var currentLevel = 0;
var currentX = 0;
var currentY = 0;
var currentN = 0;
function switchArcgis(arcgisConfigInfo,n,backFn) {
	n = 0;// 默认都是第一个map0
	currentN = n;
	if(ARCGIS_DOCK_MODE == "1") {//在有要素服务调用的时候需要执行该代码，ARCGIS_DOCK_MODE是功能配置里面的值
		$.fn.ffcsMap.setCors();
	}
	
	function _subFn(isFirstInit) {
		currentMapDiv = document.getElementById("map"+n);
		currentMapDiv.style.display = 'block';
		
		var currentMap = $("#map"+n).ffcsMap.getMap();
		$(".esriControlsBR").hide();
	
		currentArcgisConfigInfo = arcgisConfigInfo;
		var levelDetailObj=new Array();
		var arcgisScalenInfos = arcgisConfigInfo.arcgisScalenInfos;
		var currentLevelDetail;
		if(arcgisScalenInfos.length>0){
			for(var i=0; i<arcgisScalenInfos.length; i++) {
				levelDetailObj[i] = {};
				levelDetailObj[i]['level']=arcgisScalenInfos[i].scaleLevel;
				levelDetailObj[i]['resolution']=arcgisScalenInfos[i].scaleResolution;
				levelDetailObj[i]['scale']=arcgisScalenInfos[i].scaleScale;
			}
			currentLevelDetail = {
				lods:levelDetailObj
			};
			
		}else{
			currentLevelDetail = LevelDetailBackUp;
		}
		//switchClearMyLayer();//在切换地图的时候需要统一的清除显示的图层
		LevelDetail = $.extend({}, LevelDetail , currentLevelDetail);
		
		if (isFirstInit) {
			$("#jsSlider").ffcsSlider({
		        map:currentMap,
		        position:"left-top",
		        imageRoot: js_ctx +"/js/gis/library/mnbootstrap/images/nav/",
		        maxLevel:LevelDetail.lods.length-1,
		        minLevel: arcgisConfigInfo.minZoom,
		        currentLevel:arcgisConfigInfo.zoom
			});
		}
		
		var arcgisServiceInfos = arcgisConfigInfo.arcgisServiceInfos;
		
		//加载新图层
		for(var i=0; i<arcgisServiceInfos.length; i++){
			var serviceLoadType = arcgisServiceInfos[i].serviceLoadType
			var GCS = {
				wkid : arcgisConfigInfo.wkid,
				xmin : arcgisServiceInfos[i].serviceXmin,
				ymin : arcgisServiceInfos[i].serviceYmin,
				xmax : arcgisServiceInfos[i].serviceXmax,
				ymax : arcgisServiceInfos[i].serviceYmax,
				rows : arcgisConfigInfo.mapRows,
				cols : arcgisConfigInfo.mapCols,
				origin : {"x" : arcgisConfigInfo.mapOrgiginX, "y" : arcgisConfigInfo.mapOrgiginY},
				imageFormat : arcgisServiceInfos[i].serviceImageFormat,
				serviceMode : arcgisServiceInfos[i].serviceServiceMode,
				layerId : arcgisServiceInfos[i].serviceLayerId,
				tileMatrixSetId : arcgisServiceInfos[i].serviceTileMartrixSetId,
				tileMatrix : arcgisServiceInfos[i].serviceTileMartrix,
				mapKey : arcgisConfigInfo.mapKey
			}
			$.fn.ffcsMap.createLayer(arcgisServiceInfos[i].serviceUrl, arcgisServiceInfos[i].serviceLoadType, GCS);
			//$.fn.ffcsMap.createLayer(arcgisServiceInfos[i].serviceUrl, "wms", GCS);
		}
		if(ARCGIS_DOCK_MODE == "1") {
		     $.fn.ffcsMap.InstanceFeature();
		}
		if (isFirstInit) {
			if (document.getElementById("gridId") != undefined) {//否则将默认根据当前级别的网格的中心点以及网格配置层级来进行显示
				locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
			} else {
				$("#map"+n).ffcsMap.centerAt({
					x : arcgisConfigInfo.mapCenterX,          //中心点X坐标
					y : arcgisConfigInfo.mapCenterY,           //中心点y坐标
					zoom : arcgisConfigInfo.zoom,
					wkid : arcgisConfigInfo.wkid
				});
			}
			if(window.document.getElementById("map0")!= undefined) {
				//这个是地图加载之后的回调函数，一般在方法里面做图层项的查询显示以及其他的功能，因为有集中不同的地图首页
				getArcgisDataByCurrentSet();
			}
			if(window.document.getElementById("kuangxuanConfig")!= undefined){
				//这个是框选的重置，每次地图切换的时候都会引起框选的变化，比如二维跟三维的框选坐标有时候是不能通用的，所以要进行重置
				var kuangxuanframe = window.document.getElementById("kuangxuanConfig").contentWindow;
				if(kuangxuanframe.mapt != undefined && kuangxuanframe.mapt != currentArcgisConfigInfo.mapType){
					kuangxuanframe.setNeedKuangxuan();
				}
			}
			/*if(currentLayerLocateFunctionStr != undefined && currentLayerLocateFunctionStr!=''){
				//这个是记录最后一次图层列表查询时记录的调用地图定位的方法，在地图切换的时候需要将切换前的定位进行显示
				clearSpecialLayer(currentLayerName);
				eval(currentLayerLocateFunctionStr);
				currentListNumStr = currentListNumStr.substring(currentListNumStr.length-1,currentListNumStr.length);
			} else if (currentArgs.length > 0) {
				for (var i = currentArgs.length - 1; i >= 0; i--) {
					clearSpecialLayer(currentArgs[i].layerName);
					var _args = currentArgs[i].funcArgs;
					var args = new Array();
					for (var j = 0; j < _args.length; j++) {
						args.push("_args["+j+"]");
					}
					eval('(_args.callee(' + args.join(',') + '))');
				}
			}*/
		}
	}
	
	if (currentMapDiv != null) {// 判断是否已经初始化过地图，如果是切换地图就不需要在初始化
		$.fn.ffcsMap.removeLayer();// 移除原来底图图层
		_subFn(false);
	} else {
		$("#map"+n).initMap({
			mapId  : "map"+n,
			slider : false,
			height : $(document).height(),
			width : $(document).width(),
	    	x : arcgisConfigInfo.mapCenterX,          //中心点X坐标
			y : arcgisConfigInfo.mapCenterY,           //中心点y坐标
			zoom : arcgisConfigInfo.zoom,
			maxZoom : arcgisConfigInfo.arcgisScalenInfos.length-1,
			minZoom : arcgisConfigInfo.minZoom
		}, function() {
			_subFn(true);
			if (typeof backFn == "function") backFn.call(this);
		});
	}
}

function switchArcgisByNumber(obj,n,backFn) {
	if(AUTOMATIC_CLEAR_MAP_LAYER == '1') { // 二三维切换时自动清除定位图标
		currentLayerLocateFunctionStr="";
		currentListNumStr = "";
		clearCurrentStatus();
	}
	clearGridAdminTrajectoryLayer();
	if(currentMapStyleObj != obj){
		currentMapStyleObj.className = "";
		obj.className = "current";
		currentMapStyleObj = obj;
		switchArcgis(arcgisConfigInfos[n],0,backFn);
	}

}

function clearOneLayer(layerName) {//进行单个图层的清除，这个时候也要清空对应的记录地图定位的变量以及记录列表页码的变量
	$("#map"+currentN).ffcsMap.clear({layerName : layerName});  //楼宇经济楼宇
	currentLayerLocateFunctionStr="";
	currentListNumStr = "";
	clearCurrentStatus(layerName);
}

function clearMyLayer(layerName) {//进行全图层的清除，包括地图专题项配置的以及额外附加的
	if (typeof MMApi != "undefined") {
		MMApi.clearMap();
	} else {
		clearLyerFunc();
		closeHeatMapLayer();
		clearGridAdminTrajectoryLayer();
		toHideZhouBianSketch();//关闭周边的那个圆圈
		//$("#map"+currentN).ffcsMap.clear({layerName : "getTrajectoryOfGridAdmin"});  //网格员轨迹
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});  //楼宇经济楼宇
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});  //楼宇经济企业
		//$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
		$("#map"+currentN).ffcsMap.clear({layerName : "test001"});  //楼宇经济事件
		$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "statPartyOrganizationLayer"});
		/*
		if("1" != IS_ACCUMULATION_LAYER) { // 不累加
			currentLayerLocateFunctionStr="";
			currentListNumStr = "";
		}
		*/
		if (typeof layerName == "undefined" && typeof renderPartyBackBtn == "function") renderPartyBackBtn();
		if (typeof layerName == "undefined" && typeof renderUrbanBackBtn == "function") renderUrbanBackBtn();
		currentLayerLocateFunctionStr="";
		currentListNumStr = "";
		clearCurrentStatus();
	}
}

//清除同一类图层
function clearSimilarLayer(layerName, clearSimilarLayer) {
	if(typeof layerName != 'undfined' && layerName != null){
		$("#map"+currentN).ffcsMap.clear({layerName : layerName});  //事件
	}
	if(typeof clearSimilarLayer != 'undfined' && clearSimilarLayer){
		$("#map"+currentN).ffcsMap.clearSimilarLayers(layerName);
	}

}

function clearLyerFunc() {//该方法通过后台获取当前网格的配置图层，然后进行统一的清除
	$.ajax({   
		 url: js_ctx+'/zhsq/map/arcgis/arcgis/getClearLyerNames.json?homePageType='+$('#homePageType').val(),   
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 async: false,
		 error: function(data){  
		   alert('获取图层信息出错!'); 
		 },   
		 success: function(data){
		 	if(data.layerNameStr != null && data.layerNameStr != "") {
		 		var layerNames = data.layerNameStr.split(",");
		 		if(layerNames.length >0) {
		 			for(var i=0; i<layerNames.length; i++) {
		 				$("#map"+currentN).ffcsMap.clear({layerName : layerNames[i]});//护路护线
						$("#map"+currentN).ffcsMap.clearSimilarLayers(layerNames[i]);
		 			}
		 		}
		 	}
		 }
	 });
}
function switchClearMyLayer() {//进行地图切换时不需要将定位调用记录清空
	clearGridAdminTrajectoryLayer();
	if(AUTOMATIC_CLEAR_MAP_LAYER == '1') {//通过配置项是否自动清除图层的配置来判断在地图切换时是否要进行清除
		clearLyerFunc();
		//$("#map"+currentN).ffcsMap.clear({layerName : "getTrajectoryOfGridAdmin"});  //
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});  //楼宇经济楼宇
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});  //楼宇经济企业
		$("#map"+currentN).ffcsMap.clear({layerName : "lyjjEventLayer"});  //楼宇经济事件
		$("#map"+currentN).ffcsMap.clear({layerName : "efficiencySupervisionEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "importantEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "dbEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "yjzhEventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "eventSchedulingLayer"});  //事件
		$("#map"+currentN).ffcsMap.clear({layerName : "urgencyEventLayer"});  //事件
	}
}
function clearSpecialLayer(layerName) {//进行单个图层的清除
	if (typeof MMApi != "undefined") {
		MMApi.clearMap(layerName);
	} else {
		if(layerName == 'drugsLayer'){
			$("#map"+currentN).ffcsMap.clear({layerName : 'drugsLayer1'});//在控
			$("#map"+currentN).ffcsMap.clear({layerName : 'drugsLayer2'});//失控
			$("#map"+currentN).ffcsMap.clear({layerName : 'drugsLayer3'});//其他管控
		}
		$("#map"+currentN).ffcsMap.clear({layerName : layerName});
		$("#map"+currentN).ffcsMap.clearSimilarLayers(layerName);
		clearGridAdminTrajectoryLayer();
	}
}
var gridLyerNum = 0;
var currentGridLevel = 1;
//查询网格轮廓数据并显示
function getArcgisDataOfGrids(gridId,gridCode,mapt,gridLevel, noClearGridLayer) {
	var width = 360;
	var height = 245;
	currentGridLevel = gridLevel;

	if(ARCGIS_DOCK_MODE == "1") {
		featureHide("gridLayer"+gridLyerNum);
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataIdsOfGrids.json?gridId='+gridId+'&gridLevel='+gridLevel+'&orgCode='+$("#orgCode").val()
				+'&homePageType='+$("#homePageType").val()+'&gdcId=4&isRootSearch=1&t='+Math.random();
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			    if(data.gridIdsStr != "") {
			    	gridLyerNum++;
			    	$.fn.ffcsMap.InstanceFeature();
			    	var queryCondition = "TYPE=2 AND MAPT="+mapt+" AND GRIDID in(" +  data.gridIdsStr +")";//
			    	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	     			fea.render(queryCondition,'gridLayer'+gridLyerNum,ykFeatureUrl,2,true,null,null,null,null,false,getInfoDetailOnMap);
	     			fea.show('gridLayer'+gridLyerNum,$("#map"+currentN).ffcsMap.getMap());
	     			$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	     			
	     			if (data.gisDataCfg.menuSummaryWidth != null) {
	     				width = data.gisDataCfg.menuSummaryWidth;
	     			}
	     			
	     			if (data.gisDataCfg.menuSummaryHeight != null) {
	     				height = data.gisDataCfg.menuSummaryHeight;
	     			}
					var homePageType = $("#homePageType").val();
					if(typeof(homePageType) != 'undefined' && homePageType != null && homePageType == "ARCGIS_HB_HOME"){
						$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},"gridLayer"+gridLyerNum,'网格',getInfoDetailOnMap,null);
					}else{
						$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},"gridLayer"+gridLyerNum,'网格',getInfoDetailOnMap,showGridDetail);
					}
			    }
			 }
		 });
	} else {
		var flag = "";
		if (noClearGridLayer != 'undefined' && noClearGridLayer == "no") {
			flag = gridLevel;
		} else if (noClearGridLayer != 'undefined' && noClearGridLayer == "no-all") {
			flag = gridLevel;
			$("#map" + currentN).ffcsMap.clear({
				layerName : "gridLayer*"
			});
		} else {
			$("#map" + currentN).ffcsMap.clear({
				layerName : "gridLayer*"
			});
		}
		$("#map"+currentN).ffcsMap.clear({layerName : "dbhLayer"});
		$("#map"+currentN).ffcsMap.clear({layerName : "pkhLayer"});
		$("#map"+currentN).ffcsMap.clear({layerName : "zcbzhLayer"});
		$("#map"+currentN).ffcsMap.clear({layerName : "fpkhLayer"});
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
		var imgurl = js_ctx +'/js/map/arcgis/library/style/images/wglocal.png';
		
		if (false) {// 启动南安三方arcgis要素图层模式
			$.fn.ffcsMap.InstanceFeature();
			var fea =  $.fn.ffcsMap.getFfcsFeature();
			var queryCondition = "GRID_LEVEL = " + gridLevel + " AND INFO_ORG_CODE LIKE '" + $("#orgCode").val() + "%'";
			var fUrl = "http://27.150.172.153:6080/arcgis/rest/services/NanAn/NA_WGH_Orcl/FeatureServer/0";
			
			fea.queryByUrl(fUrl, queryCondition, true, {
				gridLevel : flag
			}, function(results, opts) {
				$("#map" + currentN).ffcsMap.render('gridLayer' + opts.gridLevel, "REMOTE_FEATURE", 2, true, null, null, null, OUTLINE_FONT_SETTINGS, null, null, true, {}, null, results, function() {
					addGridClickEvent(opts.gridLevel);
				});
			});
		} else {
			if (IS_GRID_ARCGIS_SHOW_CENTER_POINT != undefined && IS_GRID_ARCGIS_SHOW_CENTER_POINT == '1') {
				$("#map" + currentN).ffcsMap.render('gridLayer' + flag, url, 2, true, null, null, null, 14, true, getInfoDetailOnMap, true, {}, null, null, function() {
					addGridClickEvent(flag);
				});
			} else {
				$("#map" + currentN).ffcsMap.render('gridLayer' + flag, url, 2, true, null, null, null, OUTLINE_FONT_SETTINGS, null, null, true, {}, null, null, function() {
					addGridClickEvent(flag);
				});
			}
		}
		
		function addGridClickEvent(flag) {
			// 获取当前网格轮廓数据URL
			var mosaicUrl = js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.json?t=' + Math.random();
			var mosaicData = { mapt : mapt, ids : gridId, orgCode : $("#orgCode").val() };
			$("#map" + currentN).ffcsMap.renderForMosaic('gridLayer', mosaicUrl, mosaicData);
			
			// 获取网格图层配置
			var orgCode = $("#orgCode").val();
			var homePageType = $("#homePageType").val();
			$.ajax({
				url : js_ctx + '/zhsq/map/menuconfigure/menuConfig/getGisDataCfgByCode.json?t=' + Math.random(),
				type : 'POST',
				timeout : 300000,
				async: false,
				data : {
					orgCode : orgCode,
					homePageType : homePageType,
					menuCode : 'worldGrid'
				},
				dataType : "json",
				async : true,
				error : function(data) {
					$.messager.alert('友情提示', '获取网格图层配置出现异常!', 'warning');
				},
				success : function(data) {
					if (data.menuSummaryWidth != null) {
	     				width = data.menuSummaryWidth;
	     			}
	     			
	     			if (data.menuSummaryHeight != null) {
	     				height = data.menuSummaryHeight;
	     			}
	     			var orgCode = $("#orgCode").val();
	     			//环保网格的网格概要信息
	     			if (typeof(homePageType) != 'undefined' && homePageType != null && homePageType == "ARCGIS_HB_HOME"){
	     				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},"gridLayer"+flag,'网格',getInfoDetailOnMap,null);
	     			} else if (typeof(homePageType) != 'undefined' && homePageType != null && homePageType == "ARCGIS_STANDARD_HOME" && orgCode.indexOf('320903')==0){
	     				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:550,h:350},"gridLayer"+flag,'网格',getInfoDetailOnMap,null);
	     			} else if (typeof(homePageType) != 'undefined' && homePageType != null && homePageType == "ARCGIS_STANDARD_HOME" && orgCode.indexOf('3209')==0){
	     				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:550,h:350},"gridLayer"+flag,'网格',getInfoDetailOnMap,null);
	     			} else {
	     				$("#map" + currentN).ffcsMap.ffcsDisplayhot({
	     					w: width,
	     					h: height
	     				}, "gridLayer"+flag, '网格', getInfoDetailOnMap, showGridDetail);
	     			}
				}
			});
			
			if(document.getElementById("buildName0") != null && document.getElementById("buildName0").checked == true){
				getArcgisDataOfBuildsPoints(gridId,gridCode,mapt);
			}
		}
	}
}

function getPrecisionPovertyLayer(url,elementsCollectionStr){
	document.getElementById("gridLevelName2").checked = false;
	document.getElementById("gridLevelName3").checked = false;
	document.getElementById("gridLevelName4").checked = false;
	document.getElementById("gridLevelName5").checked = false;
	document.getElementById("gridLevelName6").checked = false;
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "PrecisionPovertyLayer"});
	currentLayerLocateFunctionStr="getPrecisionPovertyLayer('"+url+"','"+elementsCollectionStr+"')";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType+'&elementsCollectionStr='+elementsCollectionStr;
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true,null,null,null,12,null,null,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},menuLayerName,'贫困户',getPrecisionPovertyOnMap); 
	//currentLayerLocateFunctionStr='getArcgisDataOfZhuanTi("'+url+'","'+elementsCollectionStr+'",'+width+','+height+')';
}
function getPrecisionPovertyOnMap(data) {
	var eclist = analysisOfElementsCollectionList(data['elementsCollectionStr']);
	var menuName = eclist["menuName"];
	var menuCode = eclist["menuCode"];
	var callBack = eclist["callBack"];
	var htmlstr = $("#titlePath").html();
	var htmls = htmlstr.split(" &gt; ");
	if(elementsCollectionStr!=undefined && htmlstr.indexOf(menuName)<0){
		htmlstr = htmlstr.replace(htmls[htmls.length -1],"<a href='javascript:void(0);' onclick='"+currentClassificationFuncStr+"'>"+htmls[htmls.length -1]+"</a>")
		$("#titlePath").html(htmlstr + " > "+menuName);
	}
	//clearMyLayer();
	//$("#titlePath").html(htmlstr + " > "+menuName);
 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
	$("#searchBtnId").show();
	$("."+nowAlphaBackShow).hide();
	$(".NorList").show();
	nowAlphaBackShow = "NorList";
	getObjectListUrl(elementsCollectionStr,data['wid']);
	currentLayerListFunctionStr = callBack+"(\""+elementsCollectionStr+"\")";
}

function featureHide(layerName){
	var feature = $.fn.ffcsMap.getFfcsFeature();
	var currentMap = $("#map"+currentN).ffcsMap.getMap();
	feature.hide(layerName,currentMap);
}
//网格
function getArcgisDataOfGridsListByIds(url) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridsLayer"});
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('gridsLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:342,h:225},"gridsLayer", '网格' ,getInfoDetailOnMap,showGridDetail);
}

//护路护线
function getArcgisDataOfICareRoad(url) {
	if("1" != IS_ACCUMULATION_LAYER) {
		switchClearMyLayer();
	}
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('hlhxLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:170},"hlhxLayer", '护路护线' ,getInfoDetailOnMap);
}

//便民服务网点
function getArcgisDataOfServiceOutletsListByIds(url) {
	if("1" != IS_ACCUMULATION_LAYER) {
		switchClearMyLayer();
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_globalEyes.png";
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('serviceOutletsLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:322,h:170},"serviceOutletsLayer", '便民服务网点' ,getInfoDetailOnMap);
}

//查询防控区域轮廓数据并显示
function getArcgisDataOfFkqySegmentGrids(gridId,gridCode,mapt,gridLevel) {
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfFkqyGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('fkqySegmentLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:0,h:0},"fkqySegmentLayer",'防控区域',showFkqySegmentGridsOfQqy); 
	
	//防控区域全球眼显示
	//var imageurl=js_ctx +"/images/map/arcgis/unselected/situation_globalEyes.png";
	//var qqyurl= "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfFkqyGlobalEyes.jhtml?showType=2&gridId="+$("#gridId").val();
	//qqyurl = qqyurl+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	//$("#map"+currentN).ffcsMap.render('fkqyEyesLayer',url,0,true,imageurl, 30, 39);
	//$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:475,h:406},"fkqyEyesLayer",'全球眼',getInfoDetailOnMap);
	
}

//点击防空区域的时候显示区域内的全球眼
function showFkqySegmentGridsOfQqy(data){
	//var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	var url =  js_ctx +'/zhsq/map/arcgis/arcgisDataOfSituationController/standardFkqyGlobalEyes.jhtml?geoString='+data['hs'];
    	url+='&standard=standard';
    	url+="&mapt="+currentArcgisConfigInfo.mapType;
    	
    	$(".titlefirstall .title").css("display","block");
    	$(".titlefirstall").css("display","block");
    	$("#titlePath").html("专题图层 > 全球眼");
	 	$("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
		$("#searchBtnId").show();
		$("."+nowAlphaBackShow).hide();
		$(".NorList").show();
		nowAlphaBackShow = "NorList";
    	showListPanel(url);
    	
}

function locateCenterAndLevel(gridId,mapt) {
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json?mapt='+mapt+'&wid='+gridId+'&t='+Math.random();
	$.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		    var list = data.list;
		    if(list!=null && list.length == 1) {
		    	var obj = list[0];
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.mapCenterLevel
		    	});
		    }else if(currentMapType == currentArcgisConfigInfo.mapType){
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : currentX,          //中心点X坐标
					y : currentY,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : currentLevel
			   	});
		    }
		 }
	 });
}
//江西网格员巡查使用，如果没有网格轮廓的情况下，默认定位到17层级
function locateCenterAndLevelJiangxi(gridId,mapt) {
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json?mapt='+mapt+'&wid='+gridId+'&t='+Math.random();
	$.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 3000, 
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	alert("系统无法获取中心点以及显示层级!");
		 },
		 success: function(data){
		    var list = data.list;
		    if(list!=null && list.length == 1) {
		    	var obj = list[0];
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : obj.x,          //中心点X坐标
					y : obj.y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : obj.mapCenterLevel
		    	});
		    }else if(currentMapType == currentArcgisConfigInfo.mapType){
		    	$("#map"+currentN).ffcsMap.centerAt({
					x : currentX,          //中心点X坐标
					y : currentY,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : 17
			   	});
		    }
		 }
	 });
}

//查询楼栋轮廓数据并显示
function getArcgisDataOfBuilds(gridId,gridCode,mapt) {
	var lc_sanwei_function=null;
	if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
		lc_sanwei_function = showSkylineViewDetail;
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuilds.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('buildLayer',url,1,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayer",'楼栋名片',getInfoDetailOnMap,showBuildDetail);
	
}
function getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt) {
	var lc_sanwei_function=null;
	if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
		lc_sanwei_function = showSkylineViewDetail;
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
    var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/build/"+CENTER_POINT_SETTINGS_BUILD;
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuildsPoints.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('buildLayerPoint',url,0,true,imageurl, 11, 14);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayerPoint",'楼栋名片',getInfoDetailOnMap,showBuildDetail,lc_sanwei_function);
}
var buildLayerNum=0;

//查询楼栋中心点数据并进行中心点定位(2015-01-21 liushi add 三维地图改为楼宇轮廓显示)
function getArcgisDataOfBuildsPoints(gridId,gridCode,mapt) {
	if(ARCGIS_DOCK_MODE == "1") {
		featureHide("buildLayer"+buildLayerNum);
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataIdsOfBuilds.json?gridId='+gridId+'&t='+Math.random();
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			    if(data.buildingIdsStr != "") {
			    	gridLyerNum++;
			    	var queryCondition = "TYPE=1 AND MAPT="+mapt+" GRIDID in(" +  data.buildingIdsStr +")";
			    	var fea =  $.fn.ffcsMap.getFfcsFeature(); //new FfcsFeatureLayer();
	     			fea.render(queryCondition,"buildLayer"+buildLayerNum,ykFeatureUrl,1,true,null,null,null,null,false,getInfoDetailOnMap);
			    	fea.show("buildLayer"+buildLayerNum,$("#map"+currentN).ffcsMap.getMap());
	     			$("#map"+currentN).ffcsMap.getMap().addLayer(fea.esriLayer);
	     			$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayer",'楼栋名片',getInfoDetailOnMap,showBuildDetail,lc_sanwei_function);
			    }
			 }
		 });
	}else {
		
		if(currentArcgisConfigInfo.mapTypeCode == 6) {
			if(IS_IMAGE_MAP_SHOW_CONTOUR == '1') {
				getArcgisDataOfBuilds(gridId,gridCode,mapt);
			}else {
				getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt);
			}
		}else if(currentArcgisConfigInfo.mapTypeCode == 5) {
			if(IS_VECTOR_MAP_SHOW_CONTOUR == '1') {
				getArcgisDataOfBuilds(gridId,gridCode,mapt);
			}else {
				getArcgisDataOfBuildsByPoint(gridId,gridCode,mapt);
			}
		}else {
			getArcgisDataOfBuilds(gridId,gridCode,mapt);
		}
	}
}


//根据ids查询在办事件定位数据进行定位
function getArcgisDataOfWorkingEvent(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('efficiencySupervisionEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('efficiencySupervisionEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"efficiencySupervisionEventLayer",'在办事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询历史事件定位数据进行定位
function getArcgisDataOfHistoricalEvent(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('efficiencySupervisionEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('efficiencySupervisionEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"efficiencySupervisionEventLayer",'历史事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询重大紧急事件定位数据进行定位
function getArcgisDataOfImportantEvent(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('importantEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('importantEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"importantEventLayer",'重大紧急事件',getInfoDetailOnMap,showEfficiencySupervisionDetail);
}

//根据ids查询个人辖区待办事件定位数据进行定位
function getArcgisDataOfDbEvent(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('dbEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('dbEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"dbEventLayer",'待办事件',getInfoDetailOnMap,showEfficiencySupervisionDetail);
}

//根据ids查询历史事件定位数据进行定位
function getArcgisDataOfYjzhEvent(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('yjzhEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('yjzhEventLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"yjzhEventLayer",'事件信息',getInfoDetailOnMap,showYjzhEventDetail);
}
//根据ids查询事件定位数据进行定位
function getArcgisDataOfEvent(url,res,imgWidth,imgHeight){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('eventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_radar.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventLayer',url,0,true,imageurl, imgWidth!=undefined?imgWidth:60, imgHeight!=undefined?imgHeight:60);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:330},"eventLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询事件调度模块的事件定位数据进行定位
function getArcgisDataOfPending(url){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('eventSchedulingLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventSchedulingLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:240},"eventSchedulingLayer",'事件',getInfoDetailOnMap,showEventDetail);
}
//
function impEventCrossDomainCallBack(eventResultsId){
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('eventSchedulingLayer');
	}
	var url = js_ctx+"/zhsq/map/arcgis/dataOfEventScheduling/getArcgisLocateDataListOfPending.jhtml?ids="+eventResultsId+"&showType=2";
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('eventSchedulingLayer',url,0,true,imageurl, 18, 28);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:660,h:340},"eventSchedulingLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

//根据ids查询事件定位数据进行定位（紧急事件） 
function getArcgisDataOfUrgencyEvent(url,res,imgWidth,imgHeight){
	if(imgWidth == null){
		imgWidth = 60;
	}
	if(imgHeight == null){
		imgHeight = 60;
	}
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer('urgencyEventLayer');
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_radar.gif";
	//var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_red.gif";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('urgencyEventLayer',url,0,true,imageurl, imgWidth, imgHeight);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:330},"urgencyEventLayer",'事件',getInfoDetailOnMap,showEventDetail);
}

function clearGridAdminLayer() {
	$("#map"+currentN).ffcsMap.clear({layerName : "gridAdminLayer"});
}


//楼宇经济楼栋定位
function getArcgisDataOfLyjjBuilding(url){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	//清除地图上原来的标记
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('lyjjBuildingLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:470,h:305},"lyjjBuildingLayer",'楼栋名片',getInfoDetailOnMap , showLyjjBuildingDetail);
}

//根据ids查询楼宇经济企业定位数据进行定位
function getArcgisDataOfLyjjCorp(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjCorLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/region_company.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('lyjjCorLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w : 440,h : 315},"lyjjCorLayer",'企业名片',getInfoDetailOnMap,showlyjjCorpBaseDetail);
}

var peopleType = "";
function getArcgisDataOfZhuanTi(url,elementsCollectionStr,width,height,iconWidth,iconHeight,opts, reLoadLocation, locationDatas, clickIconFlag, callbackFun){
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIco = uiDomain +eclist["smallIco"];
	var menuName = eclist["menuName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	currentLayerLocateFunctionStr='getArcgisDataOfZhuanTi("'+url+'","'+elementsCollectionStr+'",'+width+','+height+')';
	
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer(menuLayerName);
	}
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random()+"&elementsCollectionStr="+elementsCollectionStr;
	if("careRoadMemberLayer" == menuLayerName || "worldHlhxLayer" == menuLayerName) {
		$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true);
	}else {
		if(iconWidth != null && iconHeight != null){
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, iconWidth, iconHeight, undefined, undefined, undefined, undefined, opts, reLoadLocation, locationDatas, callbackFun);
		}else{
			// $("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39);
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39, undefined, undefined, undefined, undefined, opts, reLoadLocation, locationDatas, callbackFun);
		}
	}
	
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	var menuDetailUrl =eclist["menuDetailUrl"];
	if(typeof clickIconFlag != 'undefined' && !clickIconFlag){
		//如果clickIconFlag为false则不渲染点击图标弹出概要信息的事件
	}else{
		if(menuLayerName == "gridAdminLayer" || menuLayerName == "lawEnforceOfficLayer"){
			if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getGridAdminInfoDetailOnMapNew,showDetail,lc_sanwei_function);
			}else {
				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getGridAdminInfoDetailOnMapNew,null,lc_sanwei_function);
			}
		}else{
			if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,showDetail,lc_sanwei_function);
			}else {
				$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,null,lc_sanwei_function);
			}
		}
	}

}

function markArcgisData(url,elementsCollectionStr,width,height,iconWidth,iconHeight,opts, reLoadLocation, locationDatas, clickIconFlag){
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIco = uiDomain +eclist["smallIco"];
	var menuName = eclist["menuName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random()+"&elementsCollectionStr="+elementsCollectionStr;
	if("careRoadMemberLayer" == menuLayerName || "worldHlhxLayer" == menuLayerName) {
		$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true);
	}else {
		if(iconWidth != null && iconHeight != null){
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, iconWidth, iconHeight, undefined, undefined, undefined, undefined, opts, reLoadLocation, locationDatas);
		}else{
			// $("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39);
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39, undefined, undefined, undefined, undefined, opts, reLoadLocation, locationDatas);
		}
	}
	
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	var menuDetailUrl =eclist["menuDetailUrl"];
	if(typeof clickIconFlag != 'undefined' && !clickIconFlag){
		//如果clickIconFlag为false则不渲染点击图标弹出概要信息的事件
	}else{
		if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
			$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,showDetail,lc_sanwei_function);
		}else {
			$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,null,lc_sanwei_function);
		}
	}
}

function customDivZhuanTi(layerName, opt) {
	var _opt = opt || {};
	_opt.createDlg = getInfoDetailOnMapNew;
	var menuDetailUrl = analysisOfElementsCollection(opt.ecs, "menuDetailUrl");
	if (menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null") {
		_opt.showDetail = showDetail;
	}
	$("#map"+currentN).ffcsMap.renderDiv(layerName, _opt);
}
function customPointerZhuanTi(url,elementsCollectionStr,width,height,iconWidth,iconHeight){
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIco = uiDomain +eclist["smallIco"];
	var menuName = eclist["menuName"];
	var menuLayerName = eclist["menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	addCurrentStatus(menuLayerName, arguments);
	
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer(menuLayerName);
	}
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random()+"&elementsCollectionStr="+elementsCollectionStr;
	if("careRoadMemberLayer" == menuLayerName || "worldHlhxLayer" == menuLayerName) {
		$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true);
	}else {
		if(iconWidth != null && iconHeight != null){
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, iconWidth, iconHeight, undefined, undefined, undefined, undefined, {
				displayStyle: "1"
			});
		}else{
			$("#map"+currentN).ffcsMap.render(menuLayerName,url,0,true,smallIco, 30, 39, undefined, undefined, undefined, undefined, {
				displayStyle: "1"
			});
		}
	}
	
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	var menuDetailUrl =eclist["menuDetailUrl"];
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,showDetail,lc_sanwei_function);
	}else {
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,null,lc_sanwei_function);
	}
}
var _busiData = null;
function spillGisMarkerOfZhuanTi(datas, elementsCollectionStr, width, height) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIco = uiDomain +eclist["smallIco"];
	var menuName = eclist["menuName"];
	var menuLayerName = eclist["menuLayerName"];
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	_busiData = datas;
	currentLayerLocateFunctionStr='spillGisMarkerOfZhuanTi(_busiData,"'+elementsCollectionStr+'",'+width+','+height+')';
	
	if("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer(menuLayerName);
	}
	$("#map"+currentN).ffcsMap.renderForPoint(elementsCollectionStr, menuLayerName, datas, smallIco, 30, 39);
	
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	var menuDetailUrl =eclist[ "menuDetailUrl"];
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,showDetail,lc_sanwei_function);
	}else {
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},menuLayerName,menuName,getInfoDetailOnMapNew,null,lc_sanwei_function);
	}
}

//地图列表信息显示回调函数 地图标注点击事件
function getInfoDetailOnMapNew(data) {
	var elementsCollectionStr = data['elementsCollectionStr'];
	var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
	if (menuSummaryUrl == '' || menuSummaryUrl == undefined || menuSummaryUrl == "null") {
		return null;
	}
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl += ""+data['wid'];

	if(typeof(data) != 'undefined' && data != null && typeof(data.reLoadSummaryData) != 'undefined' && data.reLoadSummaryData != null && data.reLoadSummaryData == false){
		var dataStr = JSON.stringify(data);
		menuSummaryUrl += "&data="+encodeURIComponent(dataStr);
	}else{
		if(menuSummaryUrl.indexOf('&grid=') < 0 && menuSummaryUrl.indexOf('?gridId=') < 0) {
			menuSummaryUrl += '&gridId='+$("#gridId").val();
		}
	}
	//如果个别业务有比较特殊的参数可以进行条件判断增加
	var context = '<iframe id="iframe_info" name="iframe_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	return context;
}

//网格员地图列表信息显示回调函数 地图标注点击事件
function getGridAdminInfoDetailOnMapNew(data) {
	var elementsCollectionStr = data['elementsCollectionStr'];
	var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
	var menuLayerName =analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}

	var id = data['wid'];
	var url = menuSummaryUrl+id;
	var height = 380;
	var width = 430;
    //根据ID获取要操控元素
    var deptObjs=document.getElementById("get_grid_name_frme").contentWindow;
    $(deptObjs.document).find(".selected").each(function(){
        $(this).removeClass("selected");
    });
    if(deptObjs.document.getElementById("liebiao"+id)){
    	deptObjs.document.getElementById("liebiao"+id).className="selected";
    }
	if(menuLayerName == "lawEnforceOfficLayer"){
		height = 300;
		width = 380;
	}

	var params = {
		title: "人员详情表",
		targetUrl: url,
		height: height,
		width: width,
		top: 32,
		left: 0,
		modal: false,
		collapsible: true,
		resizable: false
	}

	closeMaxJqueryWindow();//关闭前一次打开的窗口
	showMaxJqueryWindowByParams(params);
}
function getDetailOnMapOfListClick(elementsCollectionStr,width,height,wid,gridId, data,params,onzoom) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIcoSelected = uiDomain+eclist["smallIcoSelected"];
	var menuName = eclist["menuName"];
	
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	var menuSummaryUrl = eclist["menuSummaryUrl"];
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl +=""+wid;
	if(typeof(data) != 'undefined' && data != null && typeof(data.reLoadSummaryData) != 'undefined' && data.reLoadSummaryData != null && data.reLoadSummaryData == false){
		var dataStr = JSON.stringify(data);
		menuSummaryUrl += "&data="+encodeURIComponent(dataStr);
	}else {
		if (gridId != '' && gridId != undefined) {
			menuSummaryUrl += "&gridId=" + gridId;
		}
	}
	if(typeof(params) != 'undefined'){
		for(var key in params){
			menuSummaryUrl += '&'+ key +'='+params[key];
		}
	}

	var menuDetailUrl =eclist["menuDetailUrl"];
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="iframe_info" menuLayerName="'+menuLayerName+'" name="iframe_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},showDetail,lc_sanwei_function,elementsCollectionStr);
	}else {
		
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height,nopan:(typeof(eclist['nopan'])!= 'undefined'?eclist['nopan']:undefined),top:(typeof(eclist['top'])!= 'undefined'?eclist['top']:undefined),
		nozoom:(typeof(onzoom) != 'undefined'?onzoom:false)},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="iframe_info" menuLayerName="'+menuLayerName+'" name="iframe_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},null,lc_sanwei_function);
	}
}

function locationAtPoint(wid, elementsCollectionStr, opt) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var layerName = eclist["menuLayerName"];
	$("#map"+currentN).ffcsMap.locationAtPoint(layerName, wid, elementsCollectionStr, opt);
}

function showDetail(id, title,data){
	var eclist = analysisOfElementsCollectionList(data['elementsCollectionStr']);
	
	var menuDetailUrl =eclist["menuDetailUrl"];
	var width =eclist["menuDetailWidth"];
	var height =eclist["menuDetailHeight"];
	if(width == "null"){
		width = null;height=null;
	}else {
		width = parseInt(width);
		height = parseInt(height);
	}
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		if(menuDetailUrl.indexOf('http://')<0){
			menuDetailUrl = js_ctx + menuDetailUrl;
		}
		if(id != null && typeof id=='string'){
			var idArr = id.split("-");
			if(idArr != null && idArr.length >1){
				id = idArr[0];
			}
		}
		menuDetailUrl += ""+id+"&t="+Math.random();
		
		if(menuDetailUrl.indexOf('&gridId=') < 0 && menuDetailUrl.indexOf('?gridId=') < 0) {
			menuDetailUrl += '&gridId='+$("#gridId").val();
		}
		try{
			parent.showMaxJqueryWindow(title,menuDetailUrl,width,height);
		}catch(e){
			showMaxJqueryWindow(title,menuDetailUrl,width,height);
		}
	}
}

//城市综合体
function getArcgisDataOfSegmentGrid(url, type) {
	var title = "城市综合体";
	
	if (type == '02') {
		title = "木屋片区";
	}
	$("#map"+currentN).ffcsMap.clear({layerName : "segmentLayer"});
	var url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('segmentLayer',url,2,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:850,h:410},"segmentLayer", title ,getInfoDetailOnMap);
}

function showGridDetail(id, title, dataObj) {
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	var url = "";
	if (false) {
		url = sq_zzgrid_url+"/zzgl/map/data/gridBase/standardGridDetailIndex.jhtml?infoOrgCode=" + dataObj.infoOrgCode;
	} else {
		url = sq_zzgrid_url+"/zzgl/map/data/gridBase/standardGridDetailIndex.jhtml?gridId="+id;
	}
	showMaxJqueryWindow(title,url,850,400);
}

function showEventDetail(id, title) {
	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	//console.info("showEventDetail -> "+id);
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	var workFlowId = arr[1];
	var instanceId = arr[2];
	var taskId = arr[3];
	var eventType = arr[4];
//	alert("eventId -> "+eventId+",workFlowId -> "+workFlowId+",instanceId -> "+instanceId+",taskId -> "+taskId+",eventType -> "+eventType);
//	console.info("eventId -> "+eventId);
//	console.info("workFlowId -> "+workFlowId);
//	console.info("instanceId -> "+instanceId);
//	console.info("taskId -> "+taskId);
//	console.info("eventType -> "+eventType);
//	console.info("eventId -> "+eventId);
	var url = js_ctx+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
	var winWidth = 900, winHeight = 400;
	
	if(typeof fetchWinWidth == 'function' ) {
		winWidth = fetchWinWidth();
	}
	if(typeof fetchWinHeight == 'function' ) {
		winHeight = fetchWinHeight();
	}
	
	showMaxJqueryWindow(title,url,winWidth,winHeight,true);	
}

function showEfficiencySupervisionDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	//var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/detail.jhtml?buildingId="+id;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	if(height>400){
		height = 400;
	}
	showMaxJqueryWindow(title,url,1050,height);	
}

function showYjzhEventDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	//var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/detail.jhtml?buildingId="+id;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	if(height>400){
		height = 400;
	}
	showMaxJqueryWindow(title,url,1050,height);	
}

function showPendingDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	//var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/detail.jhtml?buildingId="+id;
	var url = sq_zzgrid_url+"/zzgl/event/requiredEvent/eventDetail.jhtml?eventId="+id;
	var height = $(document).height()-20;
	if(height>400){
		height = 400;
	}
	showMaxJqueryWindow(title,url,1050,height);
}

function showBuildDetail(id, title) {
	var sq_zzgrid_url =  document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+id;
	var height = $(document).height()-20;
	if(height>520){
		height = 520;
	}
	showMaxJqueryWindow(title,url,948,405);	
}

function showPartyDetail(id,orgCode, title) {
	var url = js_ctx+"/zhsq/map/arcgis/arcgisdataofthing/partyMemberIndex.jhtml?partyGroupId="+id+"&orgCode="+orgCode;
	var height = $(document).height()-20;
	if(height>520){
		height = 520;
	}
	showMaxJqueryWindow(title,url,948,405);	
}

//显示楼宇经济楼宇详情
function showLyjjBuildingDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/building/view.jhtml?buildingId="+id+"&fromMap=1";
	
	showMaxJqueryWindow(title,url,980,420);
}
//显示楼宇经济企业详情
function showlyjjCorpBaseDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/corpBase/view.jhtml?corpId="+id+"&fromMap=1";
	showMaxJqueryWindow(title,url,980,420);
}




//地图列表信息显示回调函数
function getInfoDetailOnMap(data) {
	var mapt = currentArcgisConfigInfo.mapType;
	var layer = data['layerName'];
	var url = "";
	var context = "";
	if(layer == 'gridLayer' || layer == 'gridsLayer' || layer.indexOf('gridLayer') == 0) {//网格图层信息显示
		var homePageType = $("#homePageType").val();
		if(typeof(homePageType) != 'undefined' && homePageType != null && homePageType == "ARCGIS_HB_HOME"){
			url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getHBGridInfoDetailOnMap.jhtml?gridId='+data['wid']+'&t='+Math.random();
		} else if (false) {
			url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?infoOrgCode='+data['infoOrgCode']+'&t='+Math.random();
		} else {
			url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId='+data['wid']+'&t='+Math.random();
		}
	    context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if (layer == 'serviceOutletsLayer') {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdataofregion/getServiceOutletsInfoDetailOnMap.jhtml?soId='+data['wid']+'&t='+Math.random();
	    context = '<iframe id="serviceOutlets_info" name="serviceOutlets_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == 'buildLayer' || layer=='buildLayerPoint') {//楼宇图层信息显示
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="build_info" name="build_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "eventLayer" || layer == "urgencyEventLayer") {
		//console.info("data ---> " + data['wid']);
		var modeType = data['modeType'];
		var arr = new Array();
		arr = data['wid'].split(",");
		var wid = arr[0];
		var workFlowId = arr[1];
		var instanceId = arr[2];
		url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent.jhtml?modeType='+modeType+'&eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+wid+'&taskId='+data['taskId']+'&t='+Math.random()+'&mapt='+mapt;
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
	}else if(layer == "buildingLayer") {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="efficiencySupervisionEventLayer"){
    	//url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	context = '<iframe id="efficiencySupervision_info" name="efficiencySupervision_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="importantEventLayer"){
    	//url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid'];
    	context = '<iframe id="importantEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="segmentLayer"){
    	//url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	var url = sq_zzgrid_url +'/zzgl/grid/segmentGrid/detail/'+data['wid']+'.jhtml?segmentType=taijiangpianqu';
    	//url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid'];
    	context = '<iframe id="segment_info" name="segment_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="dbEventLayer"){
    	//url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid'];
    	context = '<iframe id="dbEventLayer_info" name="dbEventLayer_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="eventSchedulingLayer"){
    	//url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['wid']+'&taskInfoId='+data['taskId']+'&t='+Math.random();
    	context = '<iframe id="pending_info" name="pending_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer=="yjzhEventLayer"){
    	var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
    	url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+data['eventId']+'&taskInfoId='+data['taskInfoId']+'&t='+Math.random();
    	context = '<iframe id="yjzhEvent_info" name="yjzhEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjBuildingLayer") {//楼宇经济楼宇
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjBuildingInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjCorLayer") {//楼宇经济企业
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjCorpBaseInfoDetailOnMap.jhtml?corpId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }else if(layer == "lyjjEventLayer") {//楼宇经济事件
    	 //alert(data['eventId']);
    	url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjEventInfoDetailOnMap.jhtml?eventId='+data['wid']+'&t='+Math.random();
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    }
	return context;
}

function localtionGridsPoints(id) {
	$("#map"+currentN).ffcsMap.locationPoint({w:342,h:225},'gridsLayer', id, '网格信息', '',30,39, function(data){
        var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId='+id+'&t='+Math.random();
    	context = '<iframe id="globalEyes_info" name="globalEyes_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showGridDetail);
}


//加载楼宇经济楼宇定位数据
function localtionLyjjBuildingPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	$("#map"+currentN).ffcsMap.locationPoint({w:470,h:305},'lyjjBuildingLayer', id, '楼栋名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjBuildingInfoDetailOnMap.jhtml?buildingId='+id+'&t='+Math.random();
  	context = '<iframe id="building_info" name="building_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
  	return context;
  },showLyjjBuildingDetail);
}
//加载楼宇经济企业定位数据
function localtionLyjjCorpPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	
	$("#map"+currentN).ffcsMap.locationPoint({w:470,h:305},'lyjjCorLayer', id, '企业名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjCorpBaseInfoDetailOnMap.jhtml?corpId='+id+'&t='+Math.random();
		context = '<iframe id="cor_info" name="cor_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		return context;
	},showlyjjCorpBaseDetail);
}
//

//事件点击列表弹出层
function localtionEventPoint(type,id,instanceId,workFlowId,eventType,urgencyDegree,modeType){
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	var workFlowId = arr[1];
	var instanceId = arr[2];
	var taskId = arr[3];
	var eventType = arr[4];
	
	if (typeof MMApi != "undefined") {	// 判断是否是高德地图
		var opt = {};
		opt.w = 340;
		opt.h = 330;
		if (urgencyDegree != '01') {
			opt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_red.gif";
			opt.layerName = "urgencyEventLayer";
		} else {
			opt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
			opt.layerName = "eventLayer";
		}
		opt.title = "事件信息";
		opt.url = js_ctx + '/zhsq/event/eventDisposalController/detailEvent/'+ eventType +'.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId+'&mapt='+MMGlobal.MapType;
		
		opt.isShowDetail = true;
		opt.detailUrl = js_ctx + "/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
		opt.width = 900;
		opt.height = 400;
		
		return MMApi.clickOverlayById(eventId, opt, "newEvent");
	}
	
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_radar.gif";
	/*if(urgencyDegree!='01'){
		var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_radar.gif";
	}*/
	
	var mapt = currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.locationPoint({w:340,h:330},'eventLayer', id, '事件信息', imageurl, 60, 60, function(data){
        url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent/'+ eventType +'.jhtml?eventType=map&modeType='+modeType+'&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId+'&mapt='+mapt;
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEventDetail);
}

//事件点击列表弹出层没有概要！
function localtionEventPointNoInfo(type,id,instanceId,workFlowId,eventType,urgencyDegree){
	/*var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	if(urgencyDegree!='01'){
		var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_red.gif";
	}
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	$("#map"+currentN).ffcsMap.locationPoint({w:340,h:210},'eventLayer', id, '事件信息', imageurl, 18, 28, function(data){
        url =  js_ctx +'/zhsq/event/eventDisposalController/detailEvent/'+ eventType +'.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId;
    	context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEventDetail);*/
    
    /*var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
	//console.info("showEventDetail -> "+id);*/
	var arr = new Array();
	arr = id.split(",");
	var eventId = arr[0];
	var workFlowId = arr[1];
	var instanceId = arr[2];
	var taskId = arr[3];
	var eventType = arr[4];
//	alert("eventId -> "+eventId+",workFlowId -> "+workFlowId+",instanceId -> "+instanceId+",taskId -> "+taskId+",eventType -> "+eventType);
//	console.info("eventId -> "+eventId);
//	console.info("workFlowId -> "+workFlowId);
//	console.info("instanceId -> "+instanceId);
//	console.info("taskId -> "+taskId);
//	console.info("eventType -> "+eventType);
//	console.info("eventId -> "+eventId);
	var url = js_ctx+"/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
	showMaxJqueryWindow("待办事件",url,900,400);	
}

//在办事件点击列表弹出层
function localtionEfficiencySupervisionPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'efficiencySupervisionEventLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
    	context = '<iframe id="efficiencySupervision_info" name="efficiencySupervision_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//重大紧急事件点击列表弹出层
function localtionImportantEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'importantEventLayer', eventId, '重大紧急事件', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
    	context = '<iframe id="importantEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//待办事件点击列表弹出层
function localtionDbEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'dbEventLayer', eventId, '待办事件', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
    	context = '<iframe id="dbEvent_info" name="importantEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showEfficiencySupervisionDetail);
}

//应急指挥里面的事件处理点击列表弹出层
function localtionYjzhEventPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'yjzhEventLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
    	context = '<iframe id="yjzhEvent_info" name="yjzhEvent_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showYjzhEventDetail);
}

//事件调度模块事件点击列表弹出层
function localtionPendingPoint(tid,eventId){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/event/event_green.gif";
	$("#map"+currentN).ffcsMap.locationPoint({w:660,h:340},'eventSchedulingLayer', eventId, '事件信息', imageurl, 18, 28, function(data){
        var sq_zzgrid_url = document.getElementById("SQ_ZZGRID_URL").value;
        //url =  js_ctx +'/zhsq/event/arcgisDataOfEfficiencySupervisionController/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
        var url =  sq_zzgrid_url +'/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId='+eventId+'&taskInfoId='+tid;
    	context = '<iframe id="pending_info" name="pending_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    	return context;
    },showPendingDetail);
}
function impEventClickCrossDomainCallBack(tid,eventId,type) {
	localtionPendingPoint(tid,eventId);
}




function arcgisKuangXuan(){
	var abc = $(".SelectMap");
	if(abc.hasClass("DelSelected")) {
		abc.removeClass("DelSelected");
		$('#map').ffcsMap.geteditToolbar().deactivate();
		$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
			layerName : "kuangxuanLayer"
		});
	} else {
		abc.addClass("DelSelected");
		$("#map"+currentN).ffcsMap.clear({
			layerName : "kuangxuanLayer"
		});
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
		$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
		$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack);
		
	}
}
//楼宇经济框选
function arcgisKuangXuanLyjj(){
	var abc = $(".SelectMap");
	if(abc.hasClass("DelSelected")) {
		abc.removeClass("DelSelected");
		$('#map').ffcsMap.geteditToolbar().deactivate();
		$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
			layerName : "kuangxuanLayer"
		});
	} else {
		abc.addClass("DelSelected");
		$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
			layerName : "kuangxuanLayer"
		});
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
		$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
		$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanLyjjCallBack);
	}
}



//框选统计页面弹出框
function arcgisKuangXuanCallBack(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	var url = js_ctx +'/zhsq/map/arcgis/arcgisdata/getKuangXuanData.json?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+hs+'&t='+Math.random();
	showMaxJqueryWindow("框选统计",url,400,200,null,null,kuangxuanCallBackOnClose);
}

//楼宇经济框选统计页面弹出框
function arcgisKuangXuanLyjjCallBack(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	var url = js_ctx +'/zhsq/map/arcgis/arcgisdata/getKuangXuanLyjjData.json?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+hs+'&t='+Math.random();
	showMaxJqueryWindow("统计数据",url,600,400,null,null,kuangxuanCallBackOnClose);
}


var selectedKuangXuans = "";
function setKuangXuanState(){
	var gridLevel = parseInt($("#gridLevel").val());
	if (gridLevel >= 4) {
		$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
			layerName : "kuangxuanLayer"
		});
		$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack1);
	} else {
		$.messager.alert('友情提示', '仅支持街道及以下层级进行框选!','info');
	}
	
}
function showKuangxuanHs(){
	var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
	var url="";
	if(topWin.geoString == ""){
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
	}else {
		url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?geoString='+topWin.geoString+'&t='+Math.random();
	}
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
		layerName : "kuangxuanLayer"
	});
	$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,2,true);
}
function arcgisKuangXuanCallBack1(data){
	var data1 = JSON.parse(data);
	var hs = data1.coordinates.toString();
	//获取iframe的window对象
    var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
    //通过获取到的window对象操作HTML元素，这和普通页面一样
    topWin.geoString = hs;
    topWin.mapt = currentArcgisConfigInfo.mapType;
    topWin.moveNeedKuangxuan();
    topWin.toStatKuangxuanData(currentArcgisConfigInfo.mapType, hs);
	//document.frames("kuangxuanConfig").document.geoString = hs;
	//document.frames['kuangxuanConfig'].geoString = hs;
	//var url = js_ctx +'/zhsq/map/arcgis/arcgisdata/getKuangXuanStatData.json?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+hs+'&kuangxuanTypeStr='+selectedKuangXuans+'&t='+Math.random();
	//showMaxJqueryWindow("框选统计",url,400,200,null,null,kuangxuanCallBackOnClose1);
}

//从框选配置页面直接进行数据查询
function arcgisKuangXuanGetData(mapt,geoString,selectedKuangXuanStr){
	var topWin = window.document.getElementById("kuangxuanConfig").contentWindow;
	topWin.toStatKuangxuanData();
	//var url = js_ctx +'/zhsq/map/arcgis/arcgisdata/getKuangXuanStatData.json?mapt='+currentArcgisConfigInfo.mapType+'&geoString='+geoString+'&kuangxuanTypeStr='+selectedKuangXuanStr+'&t='+Math.random();
	//showMaxJqueryWindow("框选统计",url,400,200,null,null,kuangxuanCallBackOnClose1);
}
function closeKuangxuan(){
	$(".kuangxuan").click;
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
		layerName : "kuangxuanLayer"
	});
}
function kuangxuanCallBackOnClose1(){
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
		layerName : "kuangxuanLayer"
	});
}
function zhoubianCallBackOnClose1(){
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
		layerName : "zhoubianLayer"
	});
}
//框选统计弹出框关闭回调
function kuangxuanCallBackOnClose(){
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({
		layerName : "kuangxuanLayer"
	});
	//2014-09-12 liushi 自动触发更改框选显示状态
	arcgisKuangXuan();
	/*2014-09-12 liushi 目前没有找到代码控制停止编辑状态的api暂时不进行关闭触发轮廓选择的操作
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfKuangXuanLayer.json?t='+Math.random();
	$("#map"+currentN).ffcsMap.render('kuangxuanLayer',url,1,true);
	$("#map"+currentN).ffcsMap.draw('POLYGON',arcgisKuangXuanCallBack);
	*/
}

//查询网格员轨迹数据
function getTrajectoryOfGridAdmin(url,locateTimeBegin,locateTimeEnd,titileName, backFn) {
	var title ="网格员轨迹";
	if(typeof(titileName) != 'undefined' && titileName != null && titileName != ''){
		title = titileName;
	}
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",title,'gridAdminTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd, backFn);
}
//查询渔船设备轨迹
function getTrajectoryOfBoat(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "boatTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/layer_default.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",'渔船设备轨迹','boatTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}
//查询护路护线队员轨迹数据
function getTrajectoryOfCareRoadMember(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",'队员轨迹','careRoadmemberTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}
//查询网格员实时轨迹
function getDynamicTrajectoryOfGridAdmin(url,locateTimeBegin,locateTimeEnd, backFn) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'网格员实时轨迹','gridAdminTrajectoryLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd, backFn);
}

function clearGridAdminTrajectoryLayer(){
	$("#map"+currentN).ffcsMap.clearTrajectory();

}

//查询渔船设备实时轨迹
//function getDynamicTrajectoryOfBoat(url,locateTimeBegin,locateTimeEnd) {
//	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"});
//	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
//	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
//	$("#map"+currentN).ffcsMap.trajectory("realTime",'渔船设备实时轨迹','boatTrajectoryLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
//}
//查询护路护线队员实施轨迹
function getDynamicTrajectoryOfCareRoadMember(url,locateTimeBegin,locateTimeEnd) {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'队员实时轨迹','careRoadmemberTrajectoryLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
}

//周边资源
function surroundingRes(eventId){
	var url = document.getElementById("SQ_ZZGRID_URL").value+"/zzgl/map/data/res/showSurroundingResList.jhtml?eventId="+eventId+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	showMaxJqueryWindow("周边资源",url,700,380,null,null);
}

//用于网格
function clearGridLayer() {
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridsLayer"});
}



//加载楼宇经济事件定位数据
function localtionLyjjEventPoint(id){
	imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	
	$("#map").ffcsMap.locationPoint({w:470,h:305},'lyjjEventLayer', id, '事件名片', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getlyjjEventInfoDetailOnMap.jhtml?eventId='+id+'&t='+Math.random();
		context = '<iframe id="event_info" name="event_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		return context;
	},showLyjjEventDetail);
}

//楼宇经济事件定位
function getArcgisDataOfLyjjEvent(url){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/building_unitsHouse.png";
	//清除地图上原来的标记
	$("#map").ffcsMap.clear({layerName : "lyjjBuildingLayer"});
	$("#map").ffcsMap.clear({layerName : "lyjjCorLayer"});
	$("#map").ffcsMap.clear({layerName : "lyjjEventLayer"});
	
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map").ffcsMap.render('lyjjEventLayer',url,0,true,imageurl, 30, 39);
	$("#map").ffcsMap.ffcsDisplayhot({w:470,h:305},"lyjjEventLayer",'事件名片',getInfoDetailOnMap , showLyjjEventDetail);
}

//显示楼宇经济事件详情
function showLyjjEventDetail(id, title) {
	var sq_lyjj_url =  document.getElementById("SQ_LYJJ_URL").value;
	
	$.ajax({
	      url: js_ctx+'/zhsq/map/arcgis/arcgisdataoflyjj/getLyjjEventInfo.json',
	      type:'POST',
	      dataType:"json",
	      async: true,
	      data:{
	         eventId:id
	      },
	      success:function(xxxJson){
			 var result = xxxJson.result;
	      	 if(result=="success"){
	      		 
	      		var data=xxxJson.record;
	      		var formId=data.FORM_ID;
      			var formType=data.FORM_TYPE;
  				var instanceId=data.WF_INSTANCE_ID;
				var wfDbId=data.WF_DBID_;
				var operateType=data.WF_OPERATE_TYPE;
				var activityName=data.WF_ACTIVITY_NAME_;
	      		activityName = encodeURIComponent(encodeURIComponent(activityName));
	      		
	      		var url= sq_lyjj_url+"/be/event/toEventDetail.jhtml?eventId="+id+"&formId="+formId+"&formType="+formType+"&instanceId="+instanceId+"&operFlag=look_db&taskId="+wfDbId+"&operateType="+operateType+"&activityName="+activityName+"&fromMap=2"; 
	      		showMaxJqueryWindow(title,url);
	      	 }else{
	      		 alert("请联系系统管理员!!!");
	      	 }
	      }
	});
}


//
function toSearchZhouBian(x,y){
	
	if(x == undefined || y == undefined ){
		alert("没有参照点坐标，无法进行周边资源查询！");
	}else {
		var data = '?x='+x+'&y='+y+'&mapt='+currentArcgisConfigInfo.mapType+'&socialOrgCode='+$("#socialOrgCode").val()+'&homePageType='+$("#homePageType").val();
		var url = js_ctx+'/zhsq/map/zhoubian/zhouBianStat/toZhouBianConfig.jhtml'+ data;
		$('#zhoubianConfig').attr('src', url);
		showZhoubian();
	}
}
var scopeCircleLayerNum=0;
function toShowZhouBianSketch(x,y,distance){
	//toHideZhouBianSketch();
	$("#map"+currentN).ffcsMap.addScopeCircle({x : x, y : y ,radius : distance ,imgUrl:js_ctx +"/js/map/arcgis/library/style/images/GreenShinyPin.png"},function(d){
 		scopeCircleLayerNum = d.scopeCircleCount;
 	});
}
function toHideZhouBianSketch(){
	if(typeof scopeCircleLayerNum != 'undefined' && scopeCircleLayerNum != null){
		$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "scopeCircleLayer"+scopeCircleLayerNum});
	}

}
var load3D = true;
var lc_x;
var lc_y;
var lc_name;
var lc_show_url;
function showSkylineViewDetail(wid,title,data){
   skylineViewData = data;
   url=js_ctx+"/zhsq/map/arcgis/arcgis/toLcSkylineView.jhtml";
   document.getElementById("lc_skylineview_frme").src = url
   var height = $(document).height();
   var width = $(document).width();
    $("#dialog").dialog({
		resizable: false,
		height: height,
		width: width
	});
	
	lc_x=skylineViewData.x;
	lc_y=skylineViewData.y;
	lc_name=skylineViewData.gridName;
	var arr = new Array();
		arr = data['wid'].split(",");
		var wid = arr[0];
		var workFlowId = arr[1];
		var instanceId = arr[2];
		
		//lc_show_url = 'http://'+_myServer_compact+'/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+wid+'&taskId='+data['taskId']+'&t='+Math.random();
		lc_show_url = 'http://'+_myServer_compact+'/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId='+data['wid']+'&t='+Math.random();
}

//根据ids查询新经济组织定位数据进行定位
function getArcgisDataOfNonPublicOrg(url){
	$("#map"+currentN).ffcsMap.clear({layerName : "nonPublicOrgLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/new_economy.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('nonPublicOrgLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:340,h:195},"nonPublicOrgLayer",'新经济组织',getInfoDetailOnMap,showNonPublicOrgDetail);
}

function showMarkersForUrbanObj(elementsCollectionStr, menuName, objCode, treeIcon, checked, opts, orgCode, gridId) {
	var settings = {
		displayStyle : "1"
	};
	$.extend(settings, opts);
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist[ "menuLayerName"];

	var menuCode = "urbanObj" + objCode;
	var layerName = menuLayerName + objCode;
	
	if (!checked) {
		$("#map" + currentN).ffcsMap.clear({layerName : layerName});
		clearCurrentStatus(layerName);
		return;
	} else {
		addCurrentStatus(layerName, arguments);
	}
	
	var smallIco = uiDomain + eclist[ "smallIco"];
	var menuName = eclist[ "menuName"];
	var width = eclist[ "menuSummaryWidth"];
	var height = eclist[ "menuSummaryHeight"];
	if (width == "null") {
		width = 350;
	}
	if (height == "null") {
		height = 200;
	}
	
	layer.load(0);
	if (!orgCode) orgCode = $("#orgCode").val();
	if (!gridId) gridId = $("#gridId").val();
	var gisUrl = js_ctx + "/zhsq/map/arcgis/arcgisdataofthing/getUrbanObjGisLocateDataList.jhtml?infoOrgCode=" +
				orgCode + "&objCode=" + objCode + "&mapt=" + 
				currentArcgisConfigInfo.mapType + "&elementsCollectionStr=" + 
				elementsCollectionStr + "&t=" + Math.random();
	
	$("#map"+currentN).ffcsMap.render(layerName,gisUrl,0,true,treeIcon, 22, 29,undefined,undefined,undefined,undefined, settings);
	
	var menuDetailUrl = eclist[ "menuDetailUrl"];
	var _detail = null;
	if (menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null") {
		_detail = showDetail;
	}
	$("#map" + currentN).ffcsMap.ffcsDisplayhot({
		w : width,
		h : height
	}, layerName, menuName, getInfoDetailOnMapNew, _detail, null);
	layer.closeAll('loading');
}

function markerArcgisDataOfZhuanTi(elementsCollectionStr, rowsObj, markerType, opts) {
	var settings = {
		fieldId : "id",
		fieldName : "name",
		rowsObj : rowsObj,
		preMakeFunc : null,
		gisDataUrl : null
	};
	settings = $.extend({}, settings, opts);
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist[ "menuLayerName"];
	var smallIco = uiDomain + eclist[ "smallIco"];
	var menuName = eclist[ "menuName"];
	var menuSummaryWidth = eclist[ "menuSummaryWidth"];
	var menuSummaryHeight = eclist[ "menuSummaryHeight"];
	if (menuSummaryWidth == "null") {
		menuSummaryWidth = 350;
	}
	if (menuSummaryHeight == "null") {
		menuSummaryHeight = 200;
	}
	if ("1" != IS_ACCUMULATION_LAYER) {
		clearSpecialLayer(menuLayerName);
	}

	if (rowsObj != null && rowsObj.length > 0) {
		addCurrentStatus(menuLayerName, arguments);
	
		var ary = new Array();
		for (var i = 0; i < rowsObj.length; i++) {
			ary.push(rowsObj[i][settings.fieldId]);
		}
		var idStr = ary.join(",").replace(' ', '');
		var url = "";
		if (settings.gisDataUrl != null) {
			url = settings.gisDataUrl + "&mapt="
				+ currentArcgisConfigInfo.mapType + "&elementsCollectionStr="
				+ elementsCollectionStr + "&t=" + Math.random();
		} else {
			url = js_ctx
				+ "/zhsq/map/arcgis/arcgisdata/getArcgisInfoList.jhtml?idStr=" 
				+ idStr + "&markerType=" + markerType + "&mapt="
				+ currentArcgisConfigInfo.mapType + "&elementsCollectionStr="
				+ elementsCollectionStr + "&t=" + Math.random();
		}

		$("#map" + currentN).ffcsMap.render(menuLayerName, url, 0, true,
				smallIco, 30, 39, null, null, null, null, settings);

		var menuDetailUrl = eclist[ "menuDetailUrl"];
		var _detail = null;
		if (menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null") {
			_detail = showDetail;
		}

		if(menuLayerName == "gridAdminLayer"){
			$("#map"+currentN).ffcsMap.ffcsDisplayhot({
				w:menuSummaryWidth,
				h:menuSummaryHeight
			},menuLayerName,menuName,getGridAdminInfoDetailOnMapNew,_detail);
		}else {
			$("#map" + currentN).ffcsMap.ffcsDisplayhot({
				w: menuSummaryWidth,
				h: menuSummaryHeight
			}, menuLayerName, menuName, getInfoDetailOnMapNew, _detail, null);
		}
	}
}

function clickMarkerById(elementsCollectionStr, wid) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist[ "menuLayerName"];
	var smallIcoSelected = uiDomain + eclist[ "smallIcoSelected"];
	var menuName = eclist[ "menuName"];
	var menuSummaryUrl = eclist[ "menuSummaryUrl"];
	if (menuSummaryUrl.indexOf('http://') < 0) {
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl += "" + wid + "&t=" + Math.random();
	var menuSummaryWidth = eclist[ "menuSummaryWidth"];
	var menuSummaryHeight = eclist[ "menuSummaryHeight"];
	if (menuSummaryWidth == "null") {
		menuSummaryWidth = 350;
	}
	if (menuSummaryHeight == "null") {
		menuSummaryHeight = 200;
	}
	var menuDetailUrl = eclist[ "menuDetailUrl"];
	var _detail = null;
	if (menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null") {
		_detail = showDetail;
	}
	$("#map" + currentN).ffcsMap.locationPoint({
		w : menuSummaryWidth,
		h : menuSummaryHeight
	}, menuLayerName, wid, menuName, smallIcoSelected,
	30, 39,
	function(data) {
		context = '<iframe id="iframe_info" name="iframe_info" width="100%" height="100%" src="'
				+ menuSummaryUrl
				+ '" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		return context;
	}, _detail, null, elementsCollectionStr);
}


function locationFFCSGlobalEyesPoint(id,rtspUrl){
	var imageurl=uiDomain+"/images/map/gisv0/map_config/selected/situation_globalEyes.png";
	$("#map"+currentN).ffcsMap.locationPoint({w : 475,h : 406},'globalEyesLayer', id, '全球眼播放信息', imageurl,30,39, function(data){
		var url =  js_ctx +'/zhsq/alarm/videoSurveillanceController/globalEyesPlay.jhtml?deviceNums='+id;
		context = '<iframe id="globalEyes_info" name="globalEyes_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		//context = '<embed type="application/x-vlc-plugin" name="video1" autoplay="yes" loop="yes" width="100%" height="100%" target="'+ rtspUrl +'" />';
		return context;
	});
}

//根据ids查询全球眼定位数据进行定位
function getArcgisDataOfGlobalEyes(url, clearLayer){
	if(typeof(clearLayer) == 'undefined' || clearLayer == null || clearLayer != false){
		$("#map"+currentN).ffcsMap.clear({layerName : "globalEyesLayer"});
	}
	var imageurl=uiDomain+"/images/map/gisv0/map_config/unselected/situation_globalEyes.png";
	url = url+"&mapt="+currentArcgisConfigInfo.mapType+'&t='+Math.random();
	$("#map"+currentN).ffcsMap.render('globalEyesLayer',url,0,true,imageurl, 30, 39);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:475,h:406},"globalEyesLayer",'全球眼',showGlobalEye);
}

/**
 * 根据坐标值和距离撒点周边重点人员
 * @param x
 * @param y
 * @param distance
 * @param bizType
 */
function showMapDatasByDistance(x, y, distance, bizType){
	//定位到周围添加圆圈
	$("#map"+currentN).ffcsMap.addScopeCircle({x : x, y : y ,radius : distance ,imgUrl:js_ctx +"/js/map/arcgis/library/style/images/GreenShinyPin.png"},function(d){
		scopeCircleLayerNum = d.scopeCircleCount;
	});

	//撒点定位
	gisPositionByBizType(x, y, distance, bizType);
}

//--定位
var keyPersonStr = "drugs,rectify,camps,petition,neuropathy,dangerous,heresy";
function gisPositionByBizType(x, y, distance, bizType){

	if(typeof bizType != 'undefined' && bizType != null){
		var bizTypeArr = bizType.split(",");
		if(bizTypeArr != null && bizTypeArr.length >0){
			for(var i=0;i<bizTypeArr.length;i++){
				var subBizType = bizTypeArr[i];
				if(subBizType=="keyPerson"){
					var keyPersonArr = keyPersonStr.split(",");
					for(var j=0;j<keyPersonArr.length;j++){
						gisPositionDone(x, y, distance, keyPersonArr[j]);
					}
				}else{
					gisPositionDone(x, y, distance, subBizType);
				}

			}
		}
	}

}

function gisPositionDone(x, y, distance, bizType){
	//先通过类型获取elementsCollectionStr串,再定位
	$.ajax({
		type : "POST",
		url : js_ctx+'/zhsq/map/menuconfigure/menuConfig/getGisDataCfgByCode.jhtml?menuCode='+bizType,
		dataType : "json",
		async : false,
		success : function(data) {
			//撒点定位
			if(data != null && typeof data.elementsCollectionStr != 'undefined' && data.elementsCollectionStr != null){
				var elementsCollectionStr = data.elementsCollectionStr;
				var layerName = analysisOfElementsCollection(elementsCollectionStr, "menuLayerName");
				if(typeof layerName != 'undefined' && layerName != null){
					$("#map"+currentN).ffcsMap.clear({layerName : layerName});
				}
				var orgCode = $("#orgCode").val();
				var url = js_ctx + "/zhsq/map/zhoubian/zhouBianStat/queryZhouBianByBiztypeList.jhtml?infoOrgCode=" +
					orgCode + "&mapType=" +
					currentArcgisConfigInfo.mapType + "&distance=" + distance + "&x=" + x + "&y=" + y + "&zhoubianTypeStr=" + bizType+"&elementsCollectionStr="+elementsCollectionStr;

				currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
				getArcgisDataOfZhuanTi(url,elementsCollectionStr,490,270);
			}else{
				$.messager.alert('友情提示','地图配置信息获取出现异常!','info');
			}
		}
	});
}


//查询轨迹数据
function getTrajectoryListData(userId,userName,imsi,locateTimeBegin,locateTimeEnd,titileName) {
	var params = "?locateTimeBegin="+locateTimeBegin+"&locateTimeEnd="+locateTimeEnd+"&imsi="+imsi+"&userId="+userId+"&userName="+userName+"&t="+Math.random();
	var url = js_ctx + "/zhsq/map/arcgis/arcgis/getTrajectoryListData.jhtml"+params;
	var title ="查询轨迹";
	if(typeof(titileName) != 'undefined' && titileName != null && titileName != ''){
		title = titileName;
	}
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"});
	var imageurl=uiDomain+"/images/map/gisv0/map_config/arcgis_base/admin_trace.png";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",title,'gridAdminTrajectoryLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}

/**
 * 修改网格信息
 * @param gridId
 * @param gridCode
 * @param gridName
 */
function changeGridInfo(gridId, gridCode, gridName, orgCode){
	var gridLevel = 1;
	if(typeof(gridCode) != 'undefined' && gridCode != null){
		if(gridCode.length == 2){
			gridLevel = 1;
		}else if(gridCode.length == 4){
			gridLevel = 2;
		}else if(gridCode.length == 6){
			gridLevel = 3;
		}else if(gridCode.length == 9){
			gridLevel = 4;
		}else if(gridCode.length == 12){
			gridLevel = 5;
		}else if(gridCode.length == 15){
			gridLevel = 6;
		}

	}

	if(typeof(gridLevel) != 'undefined'){
		var glns = $("input[name='gridLevelName']");
		for(var i=0; i<glns.length; i++) {
			glns[i].checked = false;
		}
	}

	$("#changeGridName").text(gridName);
	$("input[name='gridId']").val(gridId);
	$("input[name='gridCode']").val(gridCode);
	$("input[name='orgCode']").val(orgCode);
    $("input[name='orgCode']").change();
	$("input[name='gridName']").val(gridName);
	$("input[name='gridLevel']").val(gridLevel);
	currentGridLevel = gridLevel + 1;
}

/**
 * 获取子级网格轮廓信息
 * @param parentGridId
 */
function getArcgisDataOfChildrenGrids(parentGridId) {
	var width = 360;
	var height = 245;
	$("#map"+currentN).ffcsMap.clear({layerName : "gridLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "dbhLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "pkhLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "zcbzhLayer"});
	$("#map"+currentN).ffcsMap.clear({layerName : "fpkhLayer"});

	if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
		eval(currentLayerListFunctionStr);
	}
	var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfChildrenGridsByParentId.json?mapt='+currentArcgisConfigInfo.mapType+'&parentGridId='+parentGridId+'&t='+Math.random();

	if(IS_GRID_ARCGIS_SHOW_CENTER_POINT != undefined && IS_GRID_ARCGIS_SHOW_CENTER_POINT == '1') {
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,14,true,getInfoDetailOnMap,true);
	}else{
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS,null,null,true);
	}
	// 获取当前网格轮廓数据URL
	var mosaicUrl = js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridsListByIds.json?t=' + Math.random();
	var mosaicData = { mapt : currentArcgisConfigInfo.mapType, ids : $("#gridId").val(), orgCode : $("#orgCode").val() };
	$("#map" + currentN).ffcsMap.renderForMosaic('gridLayer', mosaicUrl, mosaicData);

	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:width,h:height},"gridLayer",'网格',getInfoDetailOnMap,showGridDetail);

}

//出租车轨迹数据
function getTrajectoryOfTaxi(url,locateTimeBegin,locateTimeEnd,icon) {
	$("#map"+currentN).ffcsMap.clear({layerName : "taixLayer"});
	var imageurl = icon?(uiDomain+icon):'/zhsq_event/images/icon_taxi.png';
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("playBack",'出租车轨迹','taixLayer',url,"#0000FF",imageurl,30,39,1,locateTimeBegin,locateTimeEnd);
}

//出租车实时轨迹
function getDynamicTrajectoryOfTaxi(url,locateTimeBegin,locateTimeEnd,icon) {
	$("#map"+currentN).ffcsMap.clear({layerName : "taixLayer"});
	var imageurl = icon?(uiDomain+icon):'/zhsq_event/images/icon_taxi.png';
	url = url + "&mapt="+currentArcgisConfigInfo.mapType;
	$("#map"+currentN).ffcsMap.trajectory("realTime",'出租车实时轨迹','taixLayer',url,"#0000FF",imageurl,30,39,10,locateTimeBegin,locateTimeEnd);
}

function drawMultiLine(layerName,points,lineOpt){
	//$("#map"+currentN).ffcsMap.clear({layerName : layerName});
	$("#map"+currentN).ffcsMap.drawLine(layerName,points,lineOpt);
	$("#map"+currentN).ffcsMap.centerAt({x:points[0][0].x,y:points[0][0].y,zoom:-1});
	$("#map"+currentN).ffcsMap.getMap().setZoom(14);
}

function addSinglePointInfo(layerName,x,y,imageUrl,text,color){
	var opt = {
			x:x,
			y:y,
			imageUrl:imageUrl,
			text:text,
			color:color
	};
	
	$("#map"+currentN).ffcsMap.addSinglePointInfo(layerName,opt);
}

function showGlobalEyes(title, elementsCollectionStr, wid){
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	var menuSummaryUrl = eclist["menuSummaryUrl"];
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	if(menuSummaryWidth == "" || menuSummaryWidth == "null" || menuSummaryWidth == null){
		menuSummaryWidth = 475;
	}
	if(menuSummaryHeight == "" || menuSummaryHeight == "null" || menuSummaryHeight == null){
		menuSummaryHeight = 406;
	}
	/*甘南*/
	if(eclist.orgCode.indexOf('621503')>-1 && eclist.menuCode == 'globalEyes'){
        menuSummaryWidth = 800;
        menuSummaryHeight = 500;
	}
	menuSummaryUrl +=""+wid;
	showVideoWindow(title, menuSummaryUrl, menuSummaryWidth, menuSummaryHeight, false);
}

function showDHGlobalPlay(id){
	showGlobalPlayBox(id);
}

function getDetailOnMapOfListClickOnTitle(elementsCollectionStr,width,height,wid,gridId, data,params) {
	var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
	var menuLayerName = eclist["menuLayerName"];
	var smallIcoSelected = uiDomain+eclist["smallIcoSelected"];
	var menuName = eclist["menuName"];
	
	var menuSummaryWidth = eclist["menuSummaryWidth"];
	var menuSummaryHeight = eclist["menuSummaryHeight"];
	
	if (menuSummaryWidth != "null") {
		width = menuSummaryWidth;
	}
	
	if (menuSummaryHeight != "null") {
		height = menuSummaryHeight;
	}
	
	var menuSummaryUrl = eclist["menuSummaryUrl"];
	if(menuSummaryUrl.indexOf('http://')<0){
		menuSummaryUrl = js_ctx + menuSummaryUrl;
	}
	menuSummaryUrl +=""+wid;
	if(typeof(data) != 'undefined' && data != null && typeof(data.reLoadSummaryData) != 'undefined' && data.reLoadSummaryData != null && data.reLoadSummaryData == false){
		var dataStr = JSON.stringify(data);
		menuSummaryUrl += "&data="+encodeURIComponent(dataStr);
	}else {
		if (gridId != '' && gridId != undefined) {
			menuSummaryUrl += "&gridId=" + gridId;
		}
	}
	if(typeof(params) != 'undefined'){
		for(var key in params){
			menuSummaryUrl += '&'+ key +'='+params[key];
		}
	}

	var menuDetailUrl =eclist["menuDetailUrl"];
	var lc_sanwei_function=null;
	if('buildingLayer' == menuLayerName) {
		if(LC_INFO_ORG_CODE != "" && $('#orgCode').val().indexOf(LC_INFO_ORG_CODE)==0) {
			lc_sanwei_function = showSkylineViewDetail;
		}
	}
	if(menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null"){
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height,notitle:true,nozoom:true},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="iframe_info" menuLayerName="'+menuLayerName+'" name="iframe_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},showDetail,lc_sanwei_function,elementsCollectionStr);
	}else {
		$("#map"+currentN).ffcsMap.locationPoint({w : width,h : height,notitle:true,nozoom:true},menuLayerName, wid, menuName, smallIcoSelected,30,39, function(data){
    		context = '<iframe id="iframe_info" menuLayerName="'+menuLayerName+'" name="iframe_info" width="100%" height="100%" src="'+menuSummaryUrl+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    		return context;
    	},null,lc_sanwei_function);
	}
}

function showGridOutlineByZoom(zoom) {
	if ($("#autoShowGridLevel").is(':checked') && MAP_LEVEL_CFG) {
		var _level = MAP_LEVEL_CFG.split(",");
		var i = _level.length - 1;
		var isDraw = false;
		for (; i >= 0; i--) {
			var l = parseInt(_level[i]);
			if (l != -1 && zoom >= l) {
				isDraw = true;
				break;
			}
		}
		if (isDraw) {
			var gridId = $("#gridId").val();
			var orgCode = $("#orgCode").val();
			getArcgisDataOfGrids(gridId, orgCode, currentArcgisConfigInfo.mapType, i + 1);
		}
	}
}

