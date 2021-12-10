
/**
 * 2014-05-26 liushi add arcgis地图配置信息
 *
 **/
var arcgisConfigInfos;
var currentArcgisConfigInfo;
//记录当前显示地图图层的方法以及所含的参数
var currentLayerLocateFunctionStr="";
var currentListNumStr="";//列表页码已经加载的页码记录

var currentLayerListFunctionStr="";
var winresizeflag=0;
var currentMapDiv = null;
var currentMapType = 0;
var currentLevel = 0;
var currentX = 0;
var currentY = 0;
var currentN = 0;
function switchArcgis(arcgisConfigInfo,n) {
	currentN = n;
	if(currentMapDiv != null) {
		var aa = $(currentMapDiv).ffcsMap.getCurrentMapCenterObj();
		if(aa.centerPoint != undefined) {
			currentMapType = currentArcgisConfigInfo.mapType;
			currentLevel = aa.level;
			currentX = aa.centerPoint.x;
			currentY = aa.centerPoint.y;
		}
		
		currentMapDiv.style.display = 'none';
		// 移除原来底图图层
		var mapobj=$(currentMapDiv).ffcsMap.getMap();
		var layerIds = mapobj.layerIds;
		var layerIdsLength = layerIds.length;
		mapobj.removeAllLayers();
		currentMapDiv.innerHTML = "";
	}
	$("#map"+n).ffcsMap({
		mapId  : "map"+n,
		slider : false,
		height : $(document).height(),
		width : $(document).width(),
    	x : arcgisConfigInfo.mapCenterX,          //中心点X坐标
		y : arcgisConfigInfo.mapCenterY,           //中心点y坐标
		zoom : arcgisConfigInfo.zoom
	});
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
	switchClearMyLayer();
	LevelDetail = $.extend({}, LevelDetail , currentLevelDetail);
	$("#jsSlider").ffcsSlider({
	        map:currentMap,
	        position:"left-top",
	        imageRoot: js_ctx +"/js/map/arcgis/library/mnbootstrap/images/nav/",
	        maxLevel:LevelDetail.lods.length-1,
	        minLevel:0,
	        currentLevel:arcgisConfigInfo.zoom
	});
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
			tileMatrix : arcgisServiceInfos[i].serviceTileMartrix
		}
		$.fn.ffcsMap.createLayer(arcgisServiceInfos[i].serviceUrl, arcgisServiceInfos[i].serviceLoadType, GCS);
	}
	if(window.document.getElementById("map0")!= undefined) {
		getArcgisDataByCurrentSet();
	}
	if(currentMapType == currentArcgisConfigInfo.mapType && currentLevel != -1) {
		console.log("*********:"+currentX+"--------:"+currentY+"------:"+currentLevel);
		$("#map"+n).ffcsMap.centerAt({
			x : currentX,          //中心点X坐标
			y : currentY,           //中心点y坐标
			wkid : arcgisConfigInfo.wkid, //wkid 2437
			zoom : currentLevel
	   	});
	}else if(document.getElementById("gridId") != undefined){
		locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
	}
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
function switchArcgisByNumber(obj,n) {
	if(currentMapStyleObj != obj){
		currentMapStyleObj.className = "";
		obj.className = "current";
		currentMapStyleObj = obj;
		switchArcgis(arcgisConfigInfos[n],n);
	}
}

function clearMyLayer() {
	clearLyerFunc();
	currentLayerLocateFunctionStr="";
	currentListNumStr = "";
}
function clearLyerFunc() {
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
		 			}
		 		}
		 	}
		 }
	 });
}
function switchClearMyLayer() {//进行地图切换时不需要将定位调用记录清空
	clearLyerFunc();
}


function getPrecisionPovertyLayer(url,elementsCollectionStr){
	
	currentLayerLocateFunctionStr="getPrecisionPovertyLayer('"+url+"','"+elementsCollectionStr+"')";
	url = url + "&mapt="+currentArcgisConfigInfo.mapType+'&elementsCollectionStr='+elementsCollectionStr;
	var menuLayerName = analysisOfElementsCollection(elementsCollectionStr,"menuLayerName");
	$("#map"+currentN).ffcsMap.clearWithOutNumLayer({layerName : menuLayerName});
	$("#map"+currentN).ffcsMap.render(menuLayerName,url,2,true,null,null,null,12,null,null,true);
	$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},menuLayerName,'扶贫户',getPrecisionPovertyOnMap); 
}
function getPrecisionPovertyOnMap(data) {
	/*
	var elementsCollectionStr = data['elementsCollectionStr'];
	var gridId = data['wid'];
	showObjectStat(elementsCollectionStr,gridId);
	*/
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

//查询网格轮廓数据并显示
function getArcgisDataOfGrids(gridId,gridCode,mapt,gridLevel) {
		var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
		var imgurl = js_ctx +'/js/map/arcgis/library/style/images/wglocal.png';
		$("#map"+currentN).ffcsMap.render('gridLayer',url,2,true,null,null,null,12);
		$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:360,h:225},"gridLayer",'网格',getInfoDetailOnMap,showGridDetail); 
}
