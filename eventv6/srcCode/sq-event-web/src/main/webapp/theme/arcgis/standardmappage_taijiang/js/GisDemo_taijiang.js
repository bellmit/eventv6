
var License = 'ab9ka-1uai8-ninsp-28zap-mopla';
var hosturl = "gis";
var mapviewp;
var layersetData; //gis图控制对象   
var layer; //叠加点图层控制对象 
var eventLayer;
var isFirstLoading = true;
var gis_map_wggly=_map_wggly;//管理员定位地图
var startIsLoadMap=_startIsLoadMap;//首页开始展示地图
var localMapt=ini_mpt;//定位地图类型发布时候需要更具地图类型修改 gisDEMO.JSP
var mapt;
var __gridLevel = (parseInt(_gridLevel) < 6) ? (parseInt(_gridLevel) + 1) : parseInt(_gridLevel);
var cururl=null;
//2014-05-21 liushi add 做成轨迹线路颜色可动态配置的方式
var gjColor="#ff0000";

var isFirstLoadOf2dMap = false;
//地图定位计算
function nowMapType(inmapt){
   mapt=inmapt;
}

//加载建筑物列表的地图数据
var layerForGridList;
function loadMapDataLayerForGridList(gridIds, type, layerKey) {//
    if (gridIds != null) {
        var dataurl = "zzgl/map/data/gis/mapDataForGridList.jhtml?&gridIds=" + gridIds + "&mapt=" + mapt;
        if(layerKey==null) layoutKey = "loadMapDataLayerForGridList";
        layersetData.MoveLayer(layerKey);
        layerForGridList = layersetData.OpenXMLLayer(dataurl, layerKey, 'gid;name;hs,x,y;wid');
        //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
        layerForGridList.Style = polygonStyle(type);
       // houseLayer.LocationGeoMetry(null,null);//不居中
        layersetData.Draw();
    }
}

//加载建筑物列表的地图数据
var layerForBuildingList;
function loadMapDataLayerForBuildingList(buildingIds, type, layerKey) {//
    if (buildingIds != null) {
        var dataurl = "zzgl/map/data/gis/mapDataForBuildingList.jhtml?&buildingIds=" + buildingIds + "&mapt=" + mapt;
        if(layerKey==null) layoutKey = "loadMapDataLayerForBuildingList";
        layersetData.MoveLayer(layerKey);
        layerForBuildingList = layersetData.OpenXMLLayer(dataurl, layerKey, 'gid;name;hs,x,y;wid');
        //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
        layerForBuildingList.Style = polygonStyle(type);
       // houseLayer.LocationGeoMetry(null,null);//不居中
        layersetData.Draw();
    }
}
//加载居民列表的对应的地图数据
var layerForUserList;
function loadMapDataLayerForUserList(userIds, type, layerKey) {//
    if (userIds != null) {
        var dataurl = "zzgl/map/data/gis/mapDataForUserList.jhtml?&userIds=" + userIds + "&mapt=" + mapt;
        if(layerKey==null) layoutKey = "loadMapDataLayerForUserList";
        layersetData.MoveLayer(layerKey);
        layerForUserList = layersetData.OpenXMLLayer(dataurl, layerKey, 'gid;name;hs,x,y;wid');
        //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
        layerForUserList.Style = polygonStyle(type);
       // houseLayer.LocationGeoMetry(null,null);//不居中
        layersetData.Draw();
    }
}



function threemap(){
    loadMyMap(startIsLoadMap);
}
//加载指定类型的地图(会触发地图加载完成)
function loadMyMap(myMapt){
	//if(mapt==myMapt) return;
	//$.ligerDialog.waitting('正在加载地图数据...');
	if (layersetData != null) layersetData.ClearLayers();
	switch(myMapt+""){
		case "1":{//天地加载引擎
			load_Tiandi();
			break;
		}
		case "2":{//google地图加载引擎，google航拍
			load_Google();
			break;
		}
		case "4":{//相对坐标
			load_3D();
			break;
		}
		case "20":{//经过处理的2.5维加载引擎
			load_Googlemap3d();
			break;
		}
		default:{
			
		}
	}
    afterMapLoadEvent(myMapt);
    //$.ligerDialog.closeWaitting();
}

//加载地图后回调事件
function afterMapLoadEvent(mapt){
    var gridId = document.getElementById("gridId").value;
    readDataLayer_gridListByGridId(gridId,__gridLevel,_gridLevel); //画当前网格线
    /*
    if(mapt == '2') {//台江要求定位区政府中心点
    	readDataLayer_districtGovernment();
    }
    */
    setTimeout(function () { 
    	readDataLayer_baseBuildingListByGridId(gridId);
    }, 1000);  //标注建筑物
}
function readDataLayer_districtGovernment() {//台江区政府定位
	var dataUrl = 'zzgl/map/data/gis/mapDataForDistrictGovernment.jhtml';
    var layerName = 'queryDistrictGovernmentlayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var districtGovernmentLayer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    this.Style = polygonStyleOfDistrictGovernment();
	    //this.LocationGeoMetry(null,null);//可以定位到大致的中心点位
	    layersetData.Draw();
    });
}

//网格本身定位（用于点击选择网格时的定位）
function readDataLayer_selfGrid(centerGridId) {//台江区政府定位
	var dataUrl = 'zzgl/map/data/gis/mapDataForGridItselfByGridId.jhtml?gridId=' + centerGridId + '&mapt=' + mapt + '&t=' + Math.random();
    var layerName = 'queryGridCenterLayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var districtGovernmentLayer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    this.Style = polygonStyleOfCenterGrid();
	    this.LocationGeoMetry(centerGridId,null);//可以定位到大致的中心点位
    });
    var mapCenterLevel = getGridCenterLevel(centerGridId);
    if(mapCenterLevel != undefined && mapCenterLevel != null) {
    	myGridLevelTime = setInterval(function(){
		 	gradualChangeOfGrid(parseInt(mapCenterLevel),districtGovernmentLayer);
		}, 700);
    }
}


//2014-04-23 liushi add 获取当前网格地图显示层级
function getGridCenterLevel(centerGridId){
var mapCenterLevel = null;
    $.ajax({   
		 url: 'zzgl/map/data/gis/getGridCenterLevelByGridIdAndMapt.json?gridId='+centerGridId+'&mapt='+mapt,   
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 async: false,
		 error: function(data){  
		   $.messager.alert('友情提示','获取当前网格地图显示层级出错!','warning'); 
		 },   
		 success: function(data){
		 	mapCenterLevel = data.mapCenterLevel;
		 	gjColor=data.gjColor;
		 }
	 });
	 return mapCenterLevel;
}

var myGridLevelTime;
//画网格(new)
function readDataLayer_gridListByGridId(gridId, gridLevel,selfGridLevel) {
    if (gridId) {
        document.getElementById("gridId").value = gridId;
    } else {
        gridId = document.getElementById("gridId").value;
    }
    if (gridLevel) {
        __gridLevel = gridLevel;
    } else {
        gridLevel = __gridLevel;
    }
    if (layersetData != undefined) {
        layersetData.MoveLayer('showBaseGridList');
    }
    var dataUrl = 'zzgl/map/data/gis/mapDataForGridListByGridId.jhtml?gridId=' + gridId + '&mapt=' + mapt + '&level=' + gridLevel + '&t=' + Math.random();
    var layerName = 'querylayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var layer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    layer.Style = polygonStylegrid(2,1);
	    if(isFirstLoadOf2dMap == true && mapt == 2 ) {//台江首次加载二位地图需要定位到区政府中心
	    	isFirstLoadOf2dMap == false;
	    	readDataLayer_districtGovernment();
	    }
	    readDataLayer_selfGrid(gridId)
	    layersetData.Draw();
    });
    	//alert(layersetData.Coordiante.Level)当前的网格层级
    //层级渐变层级

}
function gradualChangeOfGrid(targetGridCenterLevel,currentLayer) {
	var currentGridCenterLevel = parseInt(layersetData.Coordiante.Level);
	if(currentGridCenterLevel<targetGridCenterLevel) {
		currentLayer.LocationGeoMetry(null,parseInt(currentGridCenterLevel+1));
		layersetData.Draw();
	}else if(currentGridCenterLevel>targetGridCenterLevel) {
		currentLayer.LocationGeoMetry(null,parseInt(currentGridCenterLevel-1));
		layersetData.Draw();
	}
	if(parseInt(layersetData.Coordiante.Level) == targetGridCenterLevel) {
		window.clearInterval(myGridLevelTime);
	}
}


function getDynamicCentLevel(gridLevel,mapt) {
	var map_show_level = parseInt(layersetData.Coordiante.Level);
 	if(mapt == 4 || mapt == 20) {
 		switch(parseInt(gridLevel)) {
 			case 3:
 				map_show_level = 0;
				break;
			case 4:
				map_show_level = 2;
				break;
			case 5:
				map_show_level = 4;
				break;
			case 6:
				map_show_level = 5;
				break;
			case 7:
				map_show_level = 5;
				break;
 		}
 	}else if(mapt == 2 || mapt == 1) {
 		switch(parseInt(gridLevel)) {
 			case 3:
 				map_show_level = 2;
				break;
			case 4:
				map_show_level = 4;
				break;
			case 5:
				map_show_level = 5;
				break;
			case 6:
				map_show_level = 6;
				break;
			case 7:
				map_show_level = 6;
				break;
 		}
 	}
	return map_show_level;
}
//画事件统计 add by zhongshm 2013.10.22
function readDataLayer_statListByGridId(gridId, gridLevel) {
	var style = polygonStylegrid(4,1);
	if(style == null){
		return;
	}
    if (gridId) {
        document.getElementById("gridId").value = gridId;
    } else {
        gridId = document.getElementById("gridId").value;
    }
    if (gridLevel) {
        __gridLevel = gridLevel;
    } else {
        gridLevel = __gridLevel;
    }
    if (layersetData != undefined) {
        layersetData.MoveLayer('showBaseGridList');
    }
    var dataUrl = 'zzgl/map/data/gis/mapDataForGridListByGridId.jhtml?gridId=' + gridId + '&mapt=' + mapt + '&level=' + gridLevel + '&t=' + Math.random();
    var layerName = 'querystatlayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var statlayer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    statlayer.Style = style;
	    
	    statlayer.LocationGeoMetry(null,null);//可以定位到大致的中心点位
	    layersetData.Draw();
    });
}


//（新方法）展示基础建筑物(new)，加载地图的时候将加载楼宇数据
function readDataLayer_baseBuildingListByGridId(gridId) {
	//debugger;
    if (gridId) {
        document.getElementById("gridId").value = gridId;
    } else {
        gridId = document.getElementById("gridId").value;
    }
    if (layersetData != undefined) {
        layersetData.MoveLayer('showBaseGridList');
    }
    var dataUrl = 'zzgl/map/data/gis/mapDataForBuildingListByGridId.jhtml?gridId=' + gridId + '&mapt=' + mapt + '&t=' + Math.random();
    var layerName = 'ctrlHotspot';
    if (layersetData) {
        layersetData.CreateHotspotLayer();
        layersetData.MoveLayer(layerName);
    }
    //异步方式
//    $.ajax({
//        type: "GET",
//        asyc: true,
//        url: dataUrl,
//        dataType: "xml",
//        success: function (xml) {
//            var data = xmlToDataTable(xml, "GisForm");
    //            showLayers(data, 'gid;name;hs,x,y', polygonStyle(4), layerName);
//        }
    //    });

	//OpenXMLLayer
/*    var hotlayer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y');
    hotlayer.Style = polygonStyle(4);
    layersetData.Draw();
    */

var hotlayer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
    hotlayer.Style = polygonStyle(4);
    layersetData.Draw();
    });

}


function onSelectGrid(gridId,gridName,orgId,orgCode,gridInitPhoto,gridLevel){
	$("#gridbutton").val(gridName);
	$("input[name='gridId']").val(gridId);
	$("input[name='gridName']").val(gridName);
	
	$.ligerDialog.hide();
    readDataLayer_gridListByGridId(gridId, (gridLevel<6 ? gridLevel+1: 6));
}

var gridWin;
function selectGrid(){//admin/grid/dynamicTree.shtml?method=initTreeGrid&type=grid_selector&forceFlag=1&callback=setGridData&winName=grid
	var options = {
		title:'网格选择',
		url:'zzgl/map/gis/gridTree.jhtml?startGridId='+document.getElementById("startGridId").value,
		width:300,
		height: 400
	};
	options = $.extend({
		height: 400, 
		url: "",
		width : 600,
		title:"",
		showMax: false, 
		showToggle: false, 
		showMin: false, 
		isResize: false,
		slide: false
	}, options);
	
	gridWin = $.ligerDialog.open(options);
}

/**
 * 自动选择地图
 * @param {} _localMapt 默认地图类型
 * @param {} _mapt 当前地图类型
 */
function autoGoMap(_localMapt, _mapt) {	

}


function autoGogly(_mapt){//管理员

	
}


function setDivSize() {
    mapviewp.style.top = "0px";
    mapviewp.style.left = "0px";
    mapviewp.style.width = (document.body.offsetWidth - 0) + "px";
    mapviewp.style.height = (document.body.offsetHeight - 0) + "px";
}
   
function IniGlobal() {
    layer = null;
    mapviewp = document.getElementById('cdiv');
    
    setDivSize();
    document.body.onresize = resize;

    layersetData = new LayerSet();
    layersetData.IsToolBar = false;
    layersetData.License = License;
    toolbardir = hosturl + "/toolbar/";
     layersetData.Margin_Left = 100;
   // layersetData.Margin_Right = 100;
    //toolbardir = jsContextPath+"/gis/toolbar/";
}

function resize() {
    //自适应地图
    setDivSize();
    layersetData.SetScreenWidth(mapviewp.offsetWidth);
    layersetData.SetScreenHeight(mapviewp.offsetHeight);
    //layersetData.ToolMoveToRight();
    // layersetData.ToolMoveToLeft();
}

function load_Googlemap(ll,xx,yy) {
	//alert("load_Googlemap");
	//alert(xx+","+yy+","+ll)
    IniGlobal();
     nowMapType(2);
	//mapt=2;
    //依据具体地址做修改
     var _url="";
	if(_mgoogle_hangpai_map_url=="null"||_mgoogle_hangpai_map_url==undefined){
	_url="/xzmap/%pic.jpg";
		
	}else {
	_url=_mgoogle_hangpai_map_url;
	}
    var bakdir = hosturl + _url; //地图背景路径   
    //alert(xx+","+yy+","+ll) 
    //alert(bakdir);
    //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
    layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [parseInt(_map_minl), parseInt(_map_maxl)], bakdir);
   // layersetData.ToolMoveToRight();
    layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
    layersetData.Coordiante.IsTiandi = 0;
    //alert(ll);alert(xx);alert(yy);alert(1111);//parseInt(ll), new Point(parseFloat(xx),parseFloat(yy)
    if (!ll || parseInt(ll) >= 5) {
        ll = 4;
    }
    layersetData.Coordiante.CollateCoordinateSys(ll, new Point(parseFloat(xx), parseFloat(yy))); //将福州移动地图中心位置。福州:119.30, 26.09;长乐:119.518557,25.965867
    // alert();
   // layersetData.Coordiante.CollateCoordinateSys(1, new Point(118.003750,24.474813)); //将福州移动地图中心位置。福州:119.30, 26.09;长乐:119.518557,25.965867
    layersetData.Draw();
}

function load_Tiandimap(ll,xx,yy) {
    IniGlobal();
    nowMapType(1);
	//mapt=1;
	isLoadTiandi = true;
    //依据具体地址做修改
    var bakdir = hosturl + _mgoogle_hangpai_map_url; //地图背景路径    
    //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
    layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [parseInt(_map_minl), parseInt(_map_maxl)], bakdir);        
   // layersetData.ToolMoveToRight();
    layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
    layersetData.Coordiante.IsTiandi = 1;
   // alert(parseInt(ll)+'====');alert(parseInt(xx)+'====');alert(parseInt(yy)+'====');//parseInt(ll), new Point(parseFloat(xx),parseFloat(yy)
    layersetData.Coordiante.CollateCoordinateSys(parseInt(ll), new Point(parseFloat(xx),parseFloat(yy))); //将福州移动地图中心位置//fz119.30, 26.09 cl:119.5,25.8
   // debugger;
    layersetData.Draw(); 
}

function load_3Dmap(l,x,y) {
	
    IniGlobal();
    nowMapType(4);
	var bakdir = hosturl + _3dmap_url;
	//alert(bakdir);
    //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
    layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [parseInt(_map_minl_3D), parseInt(_map_maxl_3D)], bakdir);
    //layersetData.ToolMoveToRight();
    layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
    layersetData.Coordiante.Is3D = 1;
    //将福州移动地图中心位置69,67//福州：70.6568, 67.1569西滨：69.564551, 69.529883//福州0，西滨3
    
    layersetData.Coordiante.CollateCoordinateSys(parseInt(l), new Point(parseFloat(x), parseFloat(y))); 
    layersetData.Draw();

}

/**
 * 谷歌3维地图
 * @param {} ll
 * @param {} xx
 * @param {} yy
 */
 function __load_Googlemap3d(xx, yy, ll) {
     IniGlobal();
     
     nowMapType(20);
     var bakdir = hosturl + _3dmap_url; //$map.map_host + $map.mapDir_google_3d; //地图背景路径    
     //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
     layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [parseInt(_map_minl_3D), parseInt(_map_maxl_3D)], bakdir);
     // layersetData.ToolMoveToRight();
     layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
     layersetData.Coordiante.IsTiandi = 0;
     layersetData.Coordiante.CollateCoordinateSys(parseInt(ll), new Point(parseFloat(xx), parseFloat(yy))); //将福州移动地图中心位置。福州:119.30, 26.09;长乐:119.518557,25.965867
     layersetData.Draw();
 }

 function load_Googlemap3d() {
     __load_Googlemap3d(_cent_x_3D, _cent_y_3D, _cent_level_3D);
 }

function load_Tiandi(){//登录加载
	load_Tiandimap(_cent_level,_cent_x,_cent_y);
    
}
function load_3D(){//登录加载
    // alert("3d");
	//var load_3Dc=_cent_level;
	//var load_3Dx=_cent_x;
	//var load_3Dy=_cent_y;
	//alert(load_3Dc);alert(load_3Dx);alert(load_3Dy);
    load_3Dmap(_cent_level_3D,_cent_x_3D,_cent_y_3D);
}
function load_Google(){
	//alert(load_3Dc);alert(load_3Dx);alert(load_3Dy);alert();
	 //alert("x:"+load_3Dx+",y:"+load_3Dy+",l:"+load_3Dc);
	isFirstLoadOf2dMap = true;
    load_Googlemap(_cent_level,_cent_x,_cent_y);
	
}

/**
 * 初始化块状楼宇
 */
function inibuild(){
	//alert(11);
 	//getMapBuildingCount();
      inibuildnew(); 
	
}
function inibuildnew(){

    var jzwurl = jsContextPath + "/zzgl/map/data/gis/mapDataForBuildingListByGridId.jhtml?mapt="+mapt+"&gridId="+document.getElementById("gridId").value;
	jzwurl += "&openjzw=1";
	//alert(jzwurl);
	//window.top.showMaskLayer();//遮罩层
	layersetData.CreateHotspotLayer();
	layersetData.MoveLayer('ctrlHotspot');
	var hotlayer = layersetData.OpenXMLLayer(jzwurl, 'ctrlHotspot', 'gid;name;hs,x,y');
	hotlayer.Style = polygonStyle(4);
	layersetData.Draw();
  //  window.top.hiddMaskLayer();//遮罩层

}
/**
 * 获取楼宇画图总数
 */

function getMapBuildingCount(){
	var count;
	var url = '/gis.shtml?method=ajaxGetMapBuildCount&gridCode='+document.getElementById("gridCode").value+'&mapt='+mapt;
	//alert(url);
	$.post(url, function(data) {
		//debugger;
	var buildingCount=data.count;
	 var getDelta=10;
	 //var getPageNum=1;
	 var pagecount=parseInt(buildingCount/getDelta)+1;
	  for(var getPageNum=1;getPageNum<=pagecount; getPageNum++){
	      get_map_buil_page(buildingCount,getDelta,getPageNum,"ctrlHotspot"+getPageNum);
	  }
	    
	 
	});
	
	
}

var levelFlag;
var maxlevel = 7;
function load_map(zl) {
    IniGlobal();
    levelFlag = zl;

    //依据具体地址做修改
    var bakdir = hosturl + "/3dmap/%pic.jpg"

    //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
    //为跟天地图级别一样多，虚构出-3、-2、-1缩放级别。:实际0级别，对应显示级别则为3
    layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [3 - maxlevel, 3], bakdir);
    layersetData.ToolMoveToRight();
    layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
    layersetData.Coordiante.Is3D = 1;
    layersetData.Coordiante.CollateCoordinateSys(zl, new Point(16, 16)); //将福州移动地图中心位置
    layersetData.MapTool.BeforeZoom = function (layset, ZoomLevel, canzoom) {
        //canzoom：显示级别是否有效。ZoomLevel显示级别，因有虚构缩放级别，canzoom不准。函数返回值：是否进行缩放
        if (ZoomLevel <= maxlevel - 4)//-3,-2,-1级别不可显示
        {
            load_map2(ZoomLevel);
            return false;
        }
        else if (canzoom) {
            levelFlag = ZoomLevel;
            readDataLayer(cururl);
            return true
        }
    };
    readDataLayer(cururl);
    layersetData.Draw();
}

function load_map2(zl) {
    IniGlobal();
    levelFlag = zl;
    //依据具体地址做修改
    var bakdir = hosturl + "/gl_xz/%pic.png"; //地图背景路径    
    //初始化坐标系统为google背景图，指定图片输出div,输出区域宽，输出区域高，坐标投影方式，背景图片对应原google图最小、最大缩放比例，背景路径
    //为跟3D图级别一样多，补出16-19缩放级别。:实际只显示0、1、3级别
    layersetData.Ini(mapviewp, mapviewp.offsetWidth, mapviewp.offsetHeight, CoordinateType.Google, [13, 13 + maxlevel], bakdir);
    resize();
    layersetData.Coordiante.MapType = 2; //使用分目录存储的图片背景，要在Ini语句之后设定
    layersetData.Coordiante.IsTiandi = 1;
    layersetData.Coordiante.CollateCoordinateSys(zl, new Point(119.281461, 26.091717)); //将福州移动地图中心位置
    layersetData.MapTool.BeforeZoom = function (layset, ZoomLevel, CanZoom) {
        //canzoom：显示级别是否有效。ZoomLevel显示级别，因有补充缩放级别，canzoom不准。函数返回值：是否进行缩放
        if (ZoomLevel > maxlevel - 4) {
            load_map(ZoomLevel);
            return false;
        }
        else if (CanZoom) {
            levelFlag = ZoomLevel;
            readDataLayer(cururl);
            return true;
        }
    };
    readDataLayer(cururl);
    layersetData.Draw();
}

function readDataLayergrid(hosturl,type) {
    if (hosturl!=null)
    {        	
    	cururl=hosturl;
    	layersetData.MoveLayer('querylayer');  
        var s = hosturl+"&levelflag=8";
        layer = layersetData.OpenXMLLayer(s, 'querylayer', 'gid;name;hs,x,y');
        //layer = layersetData.OpenJsonLayer(s, 'querylayer', 'gid;name;hs,x,y');
        //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
        layer.Style = polygonStylegrid(2,1);
        layer.LocationGeoMetry();
        layersetData.Draw();
    }
}
    
function gis_ToPan() {
 	layersetData.MapTool.ToPan("");
}
   
//全球眼标注
function gis_GetPoint() {
	layersetData.MapTool.ToGetPoint("");
    layersetData.MapTool.AfterGetPoint = function (geo, flag) {
       // var l = layersetData.OpenVirtualLayer("tmp");
      //  var g = l.AssignAddGeometry(geo);
      //  g.Id = newid++;
     //   g.Style = polygonStyle(1);
     //   l.Draw();
     $("#hideDiv3").html("").css("display","none");
     var t = "<div ><iframe  style='width:606px;height:380px;' src='/cicommunity.shtml?&method=listbd&x="+geo.Points[0].X+"&y="+geo.Points[0].Y+"'></iframe></div>";
    	//document.getElementById("tc").src ="/gis.shtml?&method=gridPeadminByCode&arrangeId="+gridid;
    	//hintDiv.innerHTML = t;
    	$("#hideDiv3").html(t).css("display","").css("z-index",9999).css("opacity",1);
     
     
    }
}
   
   
    
    
    var newid = 1;
    function gis_GetPolygon() {
        iniPs();
        layersetData.MapTool.ToGetPolygon("");
        layersetData.MapTool.AfterGetPolygon = function (geo, flag) {
            var l = layersetData.OpenVirtualLayer("tmp");
            var g = l.AssignAddGeometry(geo);
            g.Id = newid++;
            g.Style = polygonStyle(1);
            l.Draw();
           
        }
    }   
        //人员地图定位
        var openjzw = 0;
        var openqqy = 0;
        var jzw_url = null;
        var jzw_type = null;        
        function readDataLayer(dataurl,type) { 
            if (dataurl != null) {
                jzw_url = dataurl;
                jzw_type = type;
                dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
                if(dw_flag==0){
                	if(layersetData!=undefined){
                		layersetData.MoveLayer('queryadminlayer');
                	}
                	
                }
                if(wg_flag==0){
                	if(layersetData!=undefined){
                		layersetData.MoveLayer('querylayer');
                	}
                }
                if(sj_flag==0){
                	if(layersetData!=undefined){
                		 layersetData.MoveLayer('queryeventlayer');
                	}
                  }
                layersetData.MoveLayer('queryjzwlayer');
                layer = layersetData.OpenXMLLayer(dataurl, 'queryjzwlayer', 'gid;name;hs,x,y;wid');
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                layer.Style = polygonStyle(type);
                layersetData.Draw();
            }
        }
        function readDataLayer_building(dataurl,type) { 
            if (dataurl != null) {
                jzw_url = dataurl;
                jzw_type = type;
                dataurl += "&mapt=" + mapt;
                layersetData.MoveLayer('querybuildinglayer');
                layer = layersetData.OpenXMLLayer(dataurl, 'querybuildinglayer', 'gid;name;hs,x,y');
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                layer.Style = polygonStyle(type);
                layersetData.Draw();
            }
        }
        //安全隐患、消防火灾隐患
        function readDataLayer_hiddendanger(dataurl,type) { 
            if (dataurl != null) {
                jzw_url = dataurl;
                jzw_type = type;
                dataurl += "&mapt=" + mapt;
                layersetData.MoveLayer('queryhiddendangerlayer');
                layer = layersetData.OpenXMLLayer(dataurl, 'queryhiddendangerlayer', 'gid;name;hs,x,y;wid');
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                layer.Style = polygonStyle(type);
                layersetData.Draw();
            }
        }
        //出租屋定位
        var houseLayer="";
        function readDataLayer_czhouse(dataurl,type) {//queryhouselayer
            if (dataurl != null) {
                jzw_url = dataurl;
                jzw_type = type;
                dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt+"&gridCode="+document.getElementById("gridCode").value;
                if(dw_flag==0){
                	if(layersetData!=undefined){
                		layersetData.MoveLayer('queryadminlayer');
                	}
                }
                if(wg_flag==0){
                	if(layersetData!=undefined){
                		layersetData.MoveLayer('querylayer');
                	}
                	
                }
                if(sj_flag==0){
                	if(layersetData!=undefined){
                		layersetData.MoveLayer('queryeventlayer');
                	}
                  	 
                  }
                layersetData.MoveLayer('queryhouselayer');
               	// debugger;
                houseLayer = layersetData.OpenXMLLayer(dataurl, 'queryhouselayer', 'gid;name;hs,x,y;wid');
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                houseLayer.Style = polygonStyle(type);
               	// houseLayer.LocationGeoMetry(null,null);//不居中
                layersetData.Draw();
            }
        }
        //全球眼
        var qqyLayer="";
        function readQqyDataLayer(dataurl,type) {
        	if (dataurl != null) {
                jzw_url = dataurl;
                jzw_type = type;
                dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
                layersetData.MoveLayer('queryQqylayer');
                qqyLayer = layersetData.OpenXMLLayer(dataurl, 'queryQqylayer', 'id;name;x,y');
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                qqyLayer.Style = qqyDisposalStyle(1);
                layersetData.Draw();
               // layer.LocationGeoMetry(null,null);//2//不居中
                layersetData.Draw();
            }
        }
        
        
     var dzzlayer="";
        //党组织
     function readDzzDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
//             if(dw_flag==0){
//            	 if(layersetData!=undefined){
//            		 layersetData.MoveLayer('queryadminlayer');
//            	 }
//             	
//             }
//             if(wg_flag==0){
//            	 if(layersetData!=undefined){
//            		 layersetData.MoveLayer('querylayer');
//            	 }
//             	
//             }
//             if(sj_flag==0){
//            	 if(layersetData!=undefined){
//            		 layersetData.MoveLayer('queryeventlayer');
//            	 }
//               	 
//             }
//            if(qqy_flag==0){
//            	if(layersetData!=undefined){
//            		  layersetData.MoveLayer('queryQqylayer');
//            	 }
//               	
//            } 
//            if(dzz_flag==0){
//            	if(layersetData!=undefined){
//            		 layersetData.MoveLayer('queryDzzlayer');
//            	 }
//               	 
//            } 
          //  if(dzz_flag==1){
             layersetData.MoveLayer('queryDzzlayer');
             
             dzzlayer = layersetData.OpenXMLLayer(dataurl, 'queryDzzlayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             dzzlayer.Style = dzzDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            // layer.LocationGeoMetry(null,null);//2//不居中
             layersetData.Draw();
            }
        // }
     }
     /**
      * 场所
      * @param {} dataurl
      * @param {} type
      */
     var placelayer="";
     function readPlaceDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('queryPlacelayer');
             placelayer = layersetData.OpenXMLLayer(dataurl, 'queryPlacelayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             placelayer.Style = placeDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            // layer.LocationGeoMetry(null,null);//2//不居中
             layersetData.Draw();
            
         }
     }
     /***
      * 治保会
      * @type String
      */
        var zhibaohuilayer="";
     function readZhibaohuiDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('zhibaohuilayer');
             zhibaohuilayer = layersetData.OpenXMLLayer(dataurl, 'zhibaohuilayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             zhibaohuilayer.Style = zhibaohuiDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            //layer.LocationGeoMetry(null,4);//2//不居中
             layersetData.Draw();
            
         }
     }
     /***
      * 警务室
      * @type String
      */
          var jinwushilayer="";
     function readJinwushiDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('jinwushilayer');
             jinwushilayer = layersetData.OpenXMLLayer(dataurl, 'jinwushilayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             jinwushilayer.Style = jinwushiDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            //layer.LocationGeoMetry(null,4);//2//不居中
             layersetData.Draw();
            
         }
     }
        /***
      * 警务室
      * @type String
      */
          var xunluoduilayer="";
     function readXunluoduiDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('xunluoduilayer');
             xunluoduilayer = layersetData.OpenXMLLayer(dataurl, 'xunluoduilayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             xunluoduilayer.Style = xunluoduiDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            //layer.LocationGeoMetry(null,4);//2//不居中
             layersetData.Draw();
            
         }
     }
     
        /**
      * 案件警情
      * @param {} dataurl
      * @param {} type
      */
     var caseplayer="";
     function readCaseDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('queryCaselayer');
             caseplayer = layersetData.OpenXMLLayer(dataurl, 'queryCaselayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             caseplayer.Style = caseDisposalStyle(1);
           //  layersetData.Draw();//重复DRAW
          
            // layer.LocationGeoMetry(null,null);//2//不居中
             layersetData.Draw();
            
         }
     }
     
        /**
      * 企业
      * @param {} dataurl
      * @param {} type
      */
     var corplayer="";
     function readCorpDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('queryCorplayer');
             corplayer = layersetData.OpenXMLLayer(dataurl, 'queryCorplayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             corplayer.Style = corpDisposalStyle(1);
             layersetData.Draw();
            
         }
     }
             /**
      * 隐患排查
      * @param {} dataurl
      * @param {} type
      */
     var dangouslayer="";
     function readDangousDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('queryDangouslayer');
             dangouslayer = layersetData.OpenXMLLayer(dataurl, 'queryDangouslayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             dangouslayer.Style = dangousDataLayer(1);
             layersetData.Draw();
         }
     }
     var gridAdminLayerxlgj="";//layer 网格管理员图层巡逻段警
     /**
      * 管理员定位巡逻段警
      * @param {} dataurl
      * @param {} type
      */
     function readAdminPositionLayerxlgj(dataurl,type) {//管理员定位巡逻段警
     	
         if (dataurl != null) {
         	if(trace_flag==1){
     			trace_flag==0;
     			gjlayer.close_gj(true);
         	}
         	if(layersetData!=undefined){
         	 layersetData.MoveLayer('queryadminlayer');	
         	}
         	
         	if(sj_flag==0){
         		if(layersetData!=undefined){
         			layersetData.MoveLayer('queryeventlayer');
         		}
            	 
            }
            if(wg_flag==0){
            	if(layersetData!=undefined){
         			layersetData.MoveLayer('querylayer');
         		}
         	   
            }
            //if(dw_flag==1){
            	
                dw_url = dataurl;
         	   dataurl = dataurl+"&mapt=" + mapt;
         	   gridAdminLayerxlgj = layersetData.OpenXMLLayer(dataurl, 'queryadminlayer', 'gid;name;hs,x,y');
         	   change3dCoor(gridAdminLayerxlgj);
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                gridAdminLayerxlgj.Style = adminPositionStyle(type);
                layersetData.Draw();
               // layer.LocationGeoMetry(null,null);//2//不居中
                layersetData.Draw();
           // }
         }
     }
     var gridAdminLayer="";//layer 网格管理员图层
     /**
      * 管理员定位
      * @param {} dataurl图层url
      * @param {} type样式类型
      */
     function readAdminPositionLayer(dataurl,type) {//管理员定位
     	 	if(mapt == 4) {
				mapt == 2;
				loadMyMap(2);
			}
         if (dataurl != null) {
         	if(trace_flag==1){
     			trace_flag==0;
     			gjlayer.close_gj(true);
         	}
         	if(layersetData!=undefined){
         	 layersetData.MoveLayer('queryadminlayer');	
         	}	
             dw_url = dataurl;
         	   dataurl = dataurl+"&mapt=" + mapt;
         	   gridAdminLayer = layersetData.OpenXMLLayer(dataurl, 'queryadminlayer', 'gid;name;hs,x,y');
         	   //change3dCoor(gridAdminLayer);
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                gridAdminLayer.Style = adminPositionStyle(type);
                layersetData.Draw();
                layer.LocationGeoMetry(null,null);//2//不居中
                layersetData.Draw();
         }
     }
     function readPeCorrectionLayer(dataurl,type) {//矫正人员定位//2
    	
    	   if(mapt != 1 && !isLoadTiandi){//却换到2维
	       			load_Tiandimap(_cent_level,_cent_x,_cent_y);
	           	}
        
            	if(layersetData!=undefined){
         			layersetData.MoveLayer('queryadminlayer');
         		}
                dw_url = dataurl;
         	   dataurl = dataurl+"&mapt=" + mapt;
         	   layer = layersetData.OpenXMLLayer(dataurl, 'queryadminlayer', 'gid;name;hs,x,y');
         	    change3dCoor(layer);
                //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
                layer.Style = adminPositionStyle(type);
                layersetData.Draw();
               // layer.LocationGeoMetry(null,null);//2//不居中
                layersetData.Draw();
           
         
     }
//事件定位
        function readEventDisposalLayer(dataurl,type) {
            if (dataurl != null) {
            	layersetData.MoveLayer('queryeventlayer');
            	if(dw_flag==0){
            		if(layersetData!=undefined){
         			layersetData.MoveLayer('queryadminlayer');
         		}
            		
                }
               if(wg_flag==0){
            	   if(layersetData!=undefined){
         			layersetData.MoveLayer('querylayer');
         		  
         		}
            	   
               }
               if(sj_flag==1){
            	   sj_url = dataurl;
            	   dataurl = dataurl+"&mapt=" + mapt;
            	   eventLayer = layersetData.OpenXMLLayer(dataurl, 'queryeventlayer', 'gid;name;hs,x,y');
                   eventLayer.Style = eventDisposalStyle(type);
                   var lvl = document.getElementById("level").value;
                   if(lvl==null || lvl==0 || lvl==1){
                   	lvl = 0;
                   
                   }
                   layersetData.Draw();
                   if (mapt == 4){
                	//不居中   layer.LocationGeoMetry(null,null);//parseInt(lvl)+1//不居中
                   }else{
                	//  layer.LocationGeoMetry(null,null);//parseInt(lvl)+2//不居中
                   }
                  
                   layersetData.Draw();
               }
            }
        }
        function change3dCoor(gjlayer)
        {//调整定位轨迹
        	//debugger;
        	if (mapt == 4) {
                var lastx = 0;var lasty = 0;var p = null;
                for (var i = 0; i < gjlayer.Geometrys.length; i++) {
                    if (Math.abs(gjlayer.Geometrys[i].Points[0].X - lastx) > 0.000002 ||
                        Math.abs(gjlayer.Geometrys[i].Points[0].Y - lasty) > 0.000002) {
                        lastx = gjlayer.Geometrys[i].Points[0].X;
                        lasty = gjlayer.Geometrys[i].Points[0].Y;
                        p = getOMap('fz', lastx, lasty);
                    }
                    gjlayer.Geometrys[i].Points[0].X = p.x / 32 / 256;
                    gjlayer.Geometrys[i].Points[0].Y = p.y / 32 / 256;
                } 
            }else if(mapt==20){//debugger;
            	for (var i = 0; i < gjlayer.Geometrys.length; i++) {
            		if(gjlayer.Geometrys[i].Data.mapt == '' || gjlayer.Geometrys[i].Data.mapt == null){
	            		gjlayer.Geometrys[i].Points[0].X = gjlayer.Geometrys[i].Points[0].X+parseFloat(_position_offset_x_3D);//+ 0.0012;
	                    gjlayer.Geometrys[i].Points[0].Y = gjlayer.Geometrys[i].Points[0].Y+parseFloat(_position_offset_y_3D);//-0.0001;
            		}
            		//alert("mapt"+gjlayer.Geometrys[i].Data.mapt)
            	    // alert(gjlayer.Geometrys[i].Points[0].X+"==="+gjlayer.Geometrys[i].Points[0].Y);
                    
                }
            }else if(mapt==2){
            	for (var i = 0; i < gjlayer.Geometrys.length; i++) {  
            		if(gjlayer.Geometrys[i].Data.mapt == '' || gjlayer.Geometrys[i].Data.mapt == null){
	            		gjlayer.Geometrys[i].Points[0].X = gjlayer.Geometrys[i].Points[0].X+parseFloat(_position_offset_x);//+ 0.0012;
	                    gjlayer.Geometrys[i].Points[0].Y = gjlayer.Geometrys[i].Points[0].Y+parseFloat(_position_offset_y);//-0.0001;
            		}
            	     // alert(gjlayer.Geometrys[i].Points[0].X+"==="+gjlayer.Geometrys[i].Points[0].Y);
                    
                } 
            }
        }
function change3dCoorOffset(gjlayer) {// 调整定位轨迹
	// alert(2323);
	// debugger;
	if (mapt == 4) {
		var lastx = 0;
		var lasty = 0;
		var p = null;
		for (var i = 0; i < gjlayer.Geometrys.length; i++) {
			var point = ELatLng2EPoint({Lat:gjlayer.Geometrys[i].Points[0].X,Lng:gjlayer.Geometrys[i].Points[0].Y});
			gjlayer.Geometrys[i].Points[0].X = point.X;
			gjlayer.Geometrys[i].Points[0].Y = point.Y;
		}
	} else {// 2wei
		for (var i = 0; i < gjlayer.Geometrys.length; i++) {
			gjlayer.Geometrys[i].Points[0].X = gjlayer.Geometrys[i].Points[0].X;// +
			// 0.0012;
			gjlayer.Geometrys[i].Points[0].Y = gjlayer.Geometrys[i].Points[0].Y;// -0.0001;
		}
	}
}
        var wg_flag = 1,sj_flag=0,dw_flag=0,qqy_flag=0;dzz_flag=0;
        var sj_url=null,dw_url=null;dzz_url=null;qqy_url=null;
        var isLoadTiandi = false;
        //管理党组织定位
      /**
       * @zhangzhihua
       * @param {} type
       * @param {} setDp
       */
        function selectOperateType(type, setDp){//专题图层入口
            
            if(type==3){//网格员
            	autoGogly(mapt);	
           		myGridAdmindiv(1);//获取网格员
            }else if(type==4){//全球眼
	            myQqydiv(1);//获取全球眼信息
			}else if (type == 6) {//街道网格
	            readDataLayer_gridListByGridId(null, 4,$("input[name='gridLevel']").val());
			}else if(type==7){//社区网格
	            readDataLayer_gridListByGridId(null, 5,$("input[name='gridLevel']").val());
			}else if(type==8){//基础网格
	            readDataLayer_gridListByGridId(null, 6,$("input[name='gridLevel']").val());
			}else if(type==12) {//台江片区网格
				readDataLayer_segmentGridListByGridId(null,7,6)
			}
			else if(type==40){//企业
	            myCorpdiv(1);//获取场所信息
			}else if(type==47){//消防栓
	            xiaofangshuandiv(1);//获取隐患
			}else if (type == 51) {//场所
				myZhongDianChangSuoDiv(1);
			}
			
			
	   }
    //查看新事件
	function lookNewEventMessage(){
//     	if(mapt!=4){
//        	load_3D();
//        }
        window.parent.document.getElementById("setDisplay2").checked = true;
        sj_flag = 1;
     	layersetData.MoveLayer('queryeventlayer');
     	var _url = jsContextPath + "/gis.shtml?method=findBusEventDisposal&mapt="+localMapt+"&isPage=N&mapt=" + mapt+"&beginToStr="+document.getElementById("beginToStr").value;
      	layer = layersetData.OpenXMLLayer(_url, 'queryeventlayer', 'gid;name;hs,x,y');
      	
      	layer.Style = eventDisposalStyle(1);
      	layersetData.Draw();
		//layer.LocationGeoMetry(null,null);//2不居中
		layersetData.Draw();
	}
	var trace_flag = 0;
	function look_admin_trace(_url,div,img){//管理员轨迹轨迹回放
		// if(mapt != 1 && !isLoadTiandi){//却换到2维
	       		//	load_Tiandi();
	        //   	}
			autoGogly(mapt);
	   	trace_flag = 1;
	   	//layersetData.MoveLayer('queryadminlayer');
	   	gjlayer = layersetData.MoveLayer(div);
	   	layersetData.BeforeLayerSetData=function(dt, gjlayer){
    	  if(dt.length==0){
    	  	 if(gjlayer.Name==div){
    	  	    alert("没有记录，请更换时间段");
    	  	 	
    	  	 }
    		  return false;
    	  }
    	  if(gjlayer.Name==div){
    			 
    			 	showgjbofang();
    			 
    		 } 
    	   
    	  
        }
        _url = _url+"&mapt="+mapt;
        gjlayer = layersetData.OpenXMLLayer(_url, div, 'gid;name;x,y');
       //gjlayer.Geometrys[2].Data.locateTime
        //change3dCoor(gjlayer);
        gjlayer.CloseDisplay();
       var times="";
       alert(hosturl+"==hosturl");
        gjlayer.gj(jsContextPath+"/gis/toolbar/"+img, "#0000ff", 3, 4, "locateTime", 'infodesc',function(img, timevalue){},function (div, value) {
        	                  
        	                    var _html ="<font color=blue style='margin:10px 0px 0px 5px'><strong>"+document.getElementById("bofangusrname").outerText; 
        	                     
        	                     _html=_html+"[";
        	                     _html=_html+times;
        	                     _html=_html+"]";
        	                    _html=_html+"</strong></font>";
        						div.innerHTML = _html; 
        						div.style.width="200px";
        						div.style.height="53px";
        						div.style.top="-50px";
        						div.style.padding="10px 0px 0px 5px";
        					//	div.style.bottom="35px"
        						//div.style.width="180px";
        						
        						div.style.background="url(/images/scim/duihuakuang.png)";
             }, false,1000,
                       function(index,count){/*设置进度条位置，count为总数，index为当前值*/
        	var lr=index/count;
               if(gjlayer.Geometrys.length==0){
            	   return false;
            	   
               }  
              times= gjlayer.Geometrys[index].Data.locateTime;
              
              if(times.length>0){
            	  times=times.substring(0, times.length-2);
            	  
              }
              toPalyPointGj(lr);
              if(index==count-1){
            	  
            	 stopPlyGj_grid(); 
              }
        
        });
   
             // gjlayer.gj(hosturl + "/toolbar/admin_trace1.png", "#0a2cfb", 3, 4,"18905900987","18905900987");//,"18905900987","18905900987"
         
	   }
	function look_admin_check(_url,div,img){//管理员轨迹轨迹查询
		// if(mapt != 1 && !isLoadTiandi){//却换到2维
	       	//		load_Tiandi();
	        //   	}
	           	autoGogly(mapt);
	          // 	alert(100);
	   	trace_flag = 1;
	   	
	   	//layersetData.MoveLayer('queryadminlayer');
	   	gjlayer = layersetData.MoveLayer(div);
	   	
	   	layersetData.BeforeLayerSetData=function(dt, gjlayer){
	   		
    	  if(dt.length==0){
    	  	 if(gjlayer.Name==div){
    	  	   alert("没有记录，请更换时间段");
    	  	 }
    		
    		  return false;
    	  }
    	 
    	  
        }
        _url = _url+"&mapt="+mapt;
        gjlayer = layersetData.OpenXMLLayer(_url, div, 'gid;name;x,y');
      //  layersetData.FindLayer("queryadmintracelayer")=gjlayer
       
        //change3dCoor(gjlayer);
        gjlayer.CloseDisplay();
          gjlayer.gj(jsContextPath+"/gis/toolbar/"+img, "#0000ff", 3, 2, null, 'infodesc',null,function (div, value) { div.innerHTML = document.getElementById("bofangusrname").outerText; }, false,1000,
                   function(index,count){/*设置进度条位置，count为总数，index为当前值*/
    
       });
       // gjlayer.gj(hosturl + "/toolbar/admin_trace1.png", "#0a2cfb", 3, 2);
	}
	function look_admin_ssgj(_url,div,img){//管理员实时轨迹
			//autoGogly(mapt);
		// if(mapt != 1 ){//却换到2维
	       //			load_Tiandi();
	       //    	}
	   	trace_flag = 1;
	   	//layersetData.MoveLayer('queryadminlayer');
	   	gjlayer = layersetData.MoveLayer(div);
	   	 layersetData.BeforeLayerSetData=function(dt, gjlayer){
	   		
    	  if(dt.length==0){ 
    		 hidessgjbofang();
    		   stopPessgjj();
    		   stopssgjj();
	           if(gjlayer.Name==div){
	               alert("没有记录");
	           	
	           }
    		 
    		  return false;
    	  }else{
    		 if(gjlayer.Name==div){
    			 
    			 	 showssgjbofang();
    			 
    		 } 
    	
    	  }
    	 
    	  
        }
        _url = _url+"&mapt="+mapt;
        gjlayer = layersetData.OpenXMLLayer(_url, div, 'gid;name;x,y');
      //  layersetData.FindLayer("queryadmintracelayer")=gjlayer
     // debugger;
       var  times= gjlayer.Geometrys[gjlayer.Geometrys.length-1].Data.locateTime;
              if(times.length>0){
            	  times=times.substring(0, times.length-2);
            	  
              }
        //change3dCoor(gjlayer);
        gjlayer.CloseDisplay();
        gjlayer.gj(jsContextPath+"/gis/toolbar/"+img, "#0000ff", 3, 5, null, 'infodesc',null,
        	     function (div, value) {
        	            var _html ="<font color=blue style='margin:10px 0px 0px 5px'><strong>"+document.getElementById("ssgjusername").outerText; 
        	                     
        	                     _html=_html+"[";
        	                     _html=_html+times;
        	                     _html=_html+"]";
        	                    _html=_html+"</strong></font>";
        						div.innerHTML = _html; 
        						div.style.width="200px";
        						div.style.height="53px";
        						div.style.top="-50px";
        						div.style.padding="10px 0px 0px 5px";
        					//	div.style.bottom="35px"
        						//div.style.width="180px";
        						
        						div.style.background="url(/images/scim/duihuakuang.png)";
        	                    // div.innerHTML = document.getElementById("ssgjusername").outerText+"["+times+"]"; 
        	                   //    div.style.width="180px";
        	        }, false,1000,
                   function(index,count){/*设置进度条位置，count为总数，index为当前值*/
    
       });
                       
                       
       // gjlayer.gj(hosturl + "/toolbar/admin_trace1.png", "#0a2cfb", 3, 0);//timefield, descfield ,"18905900987","18905900987"
      
	}
	
	function Location(bid)
	{
    	var flag=	layer.LocationGeoMetry(bid,null);//2
    	if (!flag)
    		{
    		layersetData.ShowInfoDiv(
		            function (infodiv) {
		                infodiv.innerHTML =bid;
		                infodiv.style.height = '200px';
		                infodiv.style.width = '200px';
		            }
	            );
    		}
    	
		layersetData.Draw();
	}
      
	//查看管理员
	function tc(gridid){
		   return false;
		   if(gridid==''||gridid==null){
	        	alert("没有三员信息");
	        	return false;
	        	
	        }
		
		     var t = "<div><iframe style='width:525px;height:200px;' scrolling='no'  src='/gis.shtml?&method=gridPeadminById&arrangeId="+gridid+"' ></iframe></div>";
				
                 
                 layersetData.ShowInfoDiv(
		            function (infodiv) {
		            //	debugger;
		                infodiv.innerHTML =t;
		                infodiv.style.height = '192px';
		                infodiv.style.width = '525px';
		            	//showOnePerson(personId);
		            	
		            }
	            );
		
   //   	var t = "<div><iframe  style='width:525px;height:200px;' scrolling='no' src='/gis.shtml?&method=gridPeadminByCode&arrangeId="+gridid+"'></iframe></div>";
      	//document.getElementById("tc").src ="/gis.shtml?&method=gridPeadminByCode&arrangeId="+gridid;
      	//hintDiv.innerHTML = t;
	//	$("#hideDiv").html(t).css("display","").css("z-index",9999).css("opacity",1);
		//$("#hideDiv").show();
	//	document.getElementById("pd").value ="123";
	}
      
	

//当前未办事件
	var isLastDbTimeFirstSet = false;
    var showCount=0;
    var lastDbTime = "";
	var isHaveNew = false;
	function getCurrentEventDisposal(){
		// 重新加载个人待办信息
		showBusEventDisposalLayer();
		
		// 有新的个人待办事件时播放声音
		if (isHaveNew) {
			Player.controls.play();
		}
		setTimeout(getCurrentEventDisposal, 1000 * 60 * 2);
	}
	function closePrompt(){
		$('#eventPrompt').hide(1000);
	}
	
function clearOtherIframe() {
    	document.getElementById("query_user_iframe").src = jsContextPath + "/residentinfo.shtml?method=returnGis";
    	document.getElementById("query_building_iframe").src = "/admin/floor.shtml?method=returnGis";
    	document.getElementsByName("list_event_iframe")[0].src = "/admin/event_n.shtml?method=dbList&busEventMenu=DB&forwardUrl=gisList";
    }
    
    /**
	 * 人员信息层 
	 */
	function popPersonLayer(pid) {
		//$("#personLayer").show();
		$("#rsId").val(pid);
    	$("#iframe_map_person_base").attr("src", jsContextPath+"/zzgl/map/data/residentionfo/detail/" + pid+".jhtml");
		$("#personLayer").css("position","absolute").css("z-index",200).css("left", 0).css("top", 0).show();
	}
	
	
	
    /**
	 * 查看详情
	 */
	var __detailEventLayer;
	function onDetails(eventId){
 		//var taskInfoId = $("#hiddenEventLayer input[name=taskInfoId]").val();
		//var eventId = $("#hiddenEventLayer input[name=eventId]").val();
		//var url = jsContextPath+"/gis.shtml?&method=getBusEventDisposalDetailsById&menuFormId=7541&taskInfoId="+ taskInfoId +"&eventId="+eventId;
		var url = jsContextPath+"/zzgl/event/outPlatform/eventDatailIndex.jhtml?eventId="+eventId;
		var _showWinClass={
			height:600,
			url:url,
			title:"事件基本信息",
			width:1024,
			showMax:true,//是否最大化
			showToggle:true,//是否显示收缩按钮
			showMin:false,//是否最小化
			isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:true,//拖动
			isMax:true,
			target:null,//目标对象，指定它将以appendTo()的方式载入
			modal:true,//是否模态对话框
			name:null,//创建iframe时 作为iframe的name和id
			content:null,//内容
			isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
			buttons:null,//按钮
			fixedType:null//是否固定在右下角
		};
		__detailEventLayer = showGridWin(_showWinClass);	
		
	}
		
	/**
	 * 弹出事件信息层 
	 * @param tid taskInfoId
	 * @param pid eventId
	 */
	var __eventLayer;
	function popEventLayer(tid, pid, eventType) {
		var url;
		var detailUrl;
		var forwardURL;
		if (eventType && eventType.indexOf("1604")!=-1) {
			//forwardURL = "look_efficiency_complain_event_form";
			//url = jsContextPath+"/gis.shtml?method=lookEventFormById&forwardURL=" + forwardURL + "&taskInfoId=" + tid + "&eventId=" + pid;
			if(eventType=="event_1604_todo") 
				url = jsContextPath+"/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId="+pid+"&taskInfoId="+tid;
			else {
				url = jsContextPath+"/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId="+pid;
			}
		}else {
			url = jsContextPath+"/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?taskInfoId=" + (tid==null?"":tid) + "&eventId=" + pid;
		}
		/*if(eventType!=null){
			if(eventType=="0105"){
				url = jsContextPath+"/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId="+pid+"&taskInfoId="+tid;
			}else{
				url = jsContextPath+"/zzgl/event/outPlatform/getEventDisposalMsg.jhtml?eventId="+pid+"&taskInfoId="+tid;
			}
		}*/
		var _showWinClass={
			height:345,
			url:url,
			title:"事件基础信息",
			width:663,
			showMax:true,//是否最大化
			showToggle:true,//是否显示收缩按钮
			showMin:false,//是否最小化
			isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:true,//拖动
			isMax:false,
			modal:true,//是否模态对话框
			isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
			name:"iframe_event_base",
			buttons: [{ text: '详情', 
						onclick: function (item, dialog) { 
                     		onDetails(pid);//详情
			    	 	} 
			    	 }, 
					{ text: '关闭', 
						onclick: function (item, dialog) { 
							dialog.close();
			    		} 
			    	}]

			
		};
		
		__eventLayer = showGridWin(_showWinClass);	
	}
		
	/**
	 * 弹出事件一级分流信息层 
	 */
	var __eventDistributeLayer;
	function popEventDistributeLayer() {
		var taskInfoId = $("#hiddenEventLayer input[name=taskInfoId]").val();
		var eventId = $("#hiddenEventLayer input[name=eventId]").val();
		var type = $("#hiddenEventLayer input[name=type]").val();
		var zbOrgCode = window.frames["iframe_event_base"].document.getElementById("zbOrgCode").value;
		var zbOrgName = window.frames["iframe_event_base"].document.getElementById("zbOrgName").value;
		/*
		$("#eventDistributeLayer").show();
    	$("#iframe_event_distributary").attr("src", "/admin/event_n.shtml?method=distributaryEventItem&forwardUrl=gisDistributary&busEventDisposalOM.taskInfoId=" + taskInfoId + "&eventId=" + eventId);
		$("#eventDistributeLayer").css("position","absolute").css("z-index",10000).css("left", 0).css("top", 0).show();
		*/
		var url = jsContextPath+"/admin/event_n.shtml?method=distributaryEventItem&forwardUrl=gisDistributary" + 
			"&busEventDisposalOM.taskInfoId=" + taskInfoId + "&eventId=" + eventId +
    		"&zbOrgCode=" + encodeURI(zbOrgCode) +
    		"&zbOrgName=" + encodeURI(zbOrgName);
		
		var _showWinClass={
			height:380,
			url:url,
			title:"分流",
			width:700,
			//showMax:true,//是否最大化
			//showToggle:true,//是否显示收缩按钮
			//showMin:false,//是否最小化
			//isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:false,//拖动
			isMax:false,
			modal:true,//是否模态对话框
			isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
			name:"iframe_event_distributary"
			
		};
		
		__eventDistributeLayer = showGridWin(_showWinClass);	
	}
	
	/**
	 * 弹出事件二级分流信息层 
	 */
	var __eventDistribute2Layer;
	function popEventDistribute2Layer() {
		var taskInfoId = $("#hiddenEventLayer input[name=taskInfoId]").val();
		var eventId = $("#hiddenEventLayer input[name=eventId]").val();
		var type = $("#hiddenEventLayer input[name=type]").val();
		var personCode = window.frames["iframe_event_base"].document.getElementById("personCode").value;
		var personName = window.frames["iframe_event_base"].document.getElementById("personName").value;
		
		var url = jsContextPath+"/admin/event_n.shtml?method=distributary2EventItem&forwardUrl=gisDistributary2" + 
    		"&busEventDisposalOM.taskInfoId=" + taskInfoId + 
    		"&eventId=" + eventId +
    		"&type=" + type +
    		"&personCode=" + encodeURI(personCode) +
    		"&personName=" + encodeURI(personName);
    		
    	var _showWinClass={
			height:380,
			url:url,
			title:"分流",
			width:700,
			//showMax:true,//是否最大化
			//showToggle:true,//是否显示收缩按钮
			//showMin:false,//是否最小化
			//isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:false,//拖动
			isMax:false,
			modal:true,//是否模态对话框
			isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
			name:"iframe_event_distributary2"
			
		};
		
		__eventDistribute2Layer = showGridWin(_showWinClass);	
	}
	
	/**
	 * 弹出全球眼信息层 
	 */
	var __globalEyeLayer;
	function popGlobalEyeLayer() {
		var url = jsContextPath+"/gis.shtml?&method=showQQYInfo&id=666";
		
		var _showWinClass={
			height:380,
			url:url,
			title:"全球眼",
			width:700,
			showMax:true,//是否最大化
			showToggle:true,//是否显示收缩按钮
			showMin:false,//是否最小化
			isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:true,//拖动
			isMax:false,
			modal:true,//是否模态对话框
			isHidden:true,//关闭对话框时是否只是隐藏，还是销毁对话框
			name:"iframe_global_eye"
			
		};
		
		__globalEyeLayer = showGridWin(_showWinClass);	
		
	}
	
	/**
	 * 弹出周边资源信息层 
	 */
	var __surroundingResLayer;
	function popSurroundingResLayer() {
		var url = jsContextPath+"/gis.shtml?&method=showSurroundingResList";
		
		var _showWinClass={
			height:380,
			url:url,
			title:"周边资源搜索",
			width:700,
			showMax:true,//是否最大化
			showToggle:true,//是否显示收缩按钮
			showMin:false,//是否最小化
			isResize:true,//是否调整大小
			slide:false,//动作
			isDrag:true,//拖动
			isMax:false,
			modal:true,//是否模态对话框
			isHidden:true//关闭对话框时是否只是隐藏，还是销毁对话框
			
		};
		
		__surroundingResLayer = showGridWin(_showWinClass);	
		
	}
	
	function showsearch(objId) {  
		var wnd = $(window), doc = $(document);  
		var left = doc.scrollLeft();  
		var top = doc.scrollTop();  
		left += (wnd.width() - $("#" + objId).width())/2;  
		top += (wnd.height() - $("#" + objId).height())/2;  
		$("#" + objId).css("position","absolute");  
		$("#" + objId).css("top",top);  
		$("#" + objId).css("left",left);  
		if($("#" + objId).css("display")=="none")  
		{  
			$("#" + objId).show("slow");  
		}else  
		{  
			$("#" + objId).hide("slow");  
		}
	}  
	
				
    function jzwclosePrompt(){
    	//alert(121);
    	var tt=$("#jzwPrompt");
    	
    	layersetData.CloseClickDiv();
    	
    	$(".pop-tip a-t-l").css("display","none");
    	$("#jzwPrompt").css("display","none");
    	$("#hideDiv2").css("display","none");
    	$("#jzwPrompt").hide();
    	$("#hideDiv2").hide();
    	
    }
    function  jzwopenPrompt(evnt,t,id){
    	//$("#hideDiv").html(t).css("display","").css("z-index",9999).css("opacity",1);
    	var myEvent = evnt || window.event;
    	var left = myEvent.clientX;
    	var top = myEvent.clientY-55;
    	document.getElementById("gisXX").value =t ;
    	var buildDetailUrl = "zzgl/grid/areaBuildingInfo/gisDetail.jhtml?buildingId=";
    	document.getElementById("get_jzw_frme").src =buildDetailUrl+id;
    	$("#jzwPrompt").css("position","absolute").css("z-index",9999).css("left",left).css("top",top).show();
    	//return $("#jzwPrompt");
    	//$("#hideDiv2").width($(document).width()).height($(document).height()).css("background-color","#fff")
    	//.css("opacity",.6).css("z-index",9997).show();
    }
    
    function xxs(name){
    	$("#jzwPrompt").hide();
    	$("#hideDiv2").hide();
    	var a = document.getElementById("gisXX").value;
    	$("#getFloorMessage").css("width",document.body.clientWidth);
        $("#getFloorMessage").css("height",document.body.clientHeight);
       // $("#getFloorMessage").show(500);
        $("#get_floor_message_frme").css("width",document.body.clientWidth);
        $("#get_floor_message_frme").css("height",document.body.clientHeight);
        document.getElementById("get_floor_message_frme").src =a;
  
      
        	 var _showWinClass=showWinClass;
					_showWinClass.height=480;
					showWinClass.url=a;
					showWinClass.title=name;
					showWinClass.width=850;
					showWinClass.showMax=true;
					showWinClass.showToggle=false;
					showWinClass.showMin=false;
					showWinClass.isResize=false;
					showWinClass.slide=false;//动作
					showWinClass.isDrag=true;//拖动
					showWinClass.isMax=true;
					showWinClass.isunmask=true;//取消遮罩层
					showWinClass.modal=false;
					showWinClass.name="11";
				    showWinClass.buttons=null;
					
					showGridWin(_showWinClass);
					 
    }
    
    //---------------------------网格概况信息层 start add by shenyj 2012/06/06--------------------------------
    /**
     * 弹出网格概况信息层
     * @id 网格ID
     */
    function popGridIntro(id,clickDiv,data){
    	
    	   $("#gridID").val(id);//设置隐藏域网格ID的值，用于显示网格详细信息（viewGridDetail）时可以获取到该网格ID
	    	$("#get_grid_intro_frme").attr("src", "zzgl/map/data/gridBase/gridIntro.jhtml?mapt="+localMapt+"&gridId="+id);//设置网格概况信息iframe的URL
	    	document.getElementById("get_grid_intro_frme").style.height="90px";
	    //	var target = $("#" + id);//网格文字旁边的管理员图片对象，该对象是以网格ID命名
	    //	var offsetX = -20;//相对管理员图片对象在X轴上的偏移量
	    //	var offsetY = -45;//相对管理员图片对象在Y轴上的偏移量	
    	if(id=='965'){//直接转入详细
    		viewGridDetail(data);
    	}else if(id=='3725'){
    		viewGridDetail(data);
    	}else if(id=='3726'){
    		viewGridDetail(data);
    	}else if(id=='3727'){
    		viewGridDetail(data);
    	}else if(id=='3728'){
    		viewGridDetail(data);
    	}else if(id=='3749'){
    		viewGridDetail(data);
    	}else if(id=='5102'){
    		viewGridDetail(data);
    	}else{
	    //	$("#gridIntroDiv").css("position","absolute").css("z-index",9999).css("left",target.offset().left+offsetX).css("top",target.offset().top+offsetY).show();
	             $("#gridIntroDiv").show();
			    		    var outhtml=$("#gridIntroDiv").get(0).outerHTML;
    			        	$("#gridIntroDiv").hide();  
				    	//$("#gridIntroDiv").css("position","absolute").css("z-index",9999).css("left",target.offset().left+offsetX).css("top",target.offset().top+offsetY).show();
				  
				   // clickDiv.innerHTML=outhtml;//650 80
				    //clickDiv.style.top="80px";
				   // clickDiv.style.left="650px";
				   // clickDiv.style.width="450px";
				   // clickDiv.style.height="175px";
				    var _showWinClass=showWinClass;
					_showWinClass.height=160;
					showWinClass.url="zzgl/map/data/gridBase/gridIntro.jhtml?mapt="+localMapt+"&gridId="+id;
					showWinClass.title=data.gridName;
					showWinClass.width=450;
					showWinClass.showMax=false;
					showWinClass.showToggle=false;
					showWinClass.showMin=false;
					showWinClass.isResize=false;
					showWinClass.slide=false;//动作
					showWinClass.isDrag=true;//拖动
					showWinClass.isMax=false;
					showWinClass.isunmask=true;//取消遮罩层
					showWinClass.modal=false;
					showWinClass.name=id;
					showWinClass.buttons=[{ text: '详情', onclick: function (item, dialog) { 
                        wingrid.close();//关闭
						viewGridDetail(data);//详情
				    	
				    	layersetData.CloseClickDiv();
				    	 } }, 
				    	{ text: '关闭', onclick: function (item, dialog) { 
				    	dialog.close();
				    	layersetData.CloseClickDiv();
				    	} }];
					
					showGridWin(_showWinClass);
				    
    		
    	}
    	
    }
    
    /**
     * 显示网格详细信息
     */
     var wingrid;
    function viewGridDetail(data) {
    	closeGridIntro();//隐藏网格简介概况层
    	//$("#getGridMessage").css("width",document.body.clientWidth);
      //  $("#getGridMessage").css("height",document.body.clientHeight);
    	//$("#getGridMessage").show(500);
    	//$("#get_grid_message_frme").css("width",document.body.clientWidth);
       // $("#get_grid_message_frme").css("height",document.body.clientHeight);
        var gridID = (data&&data.id)? data.id: $("#gridID").val();
    	//$("#get_grid_message_frme").attr("src", "/gis.shtml?method=lookBasicGridMsg&gridId="+gridID);//设置网格详细信息iframe的URL
         var _url="zzgl/map/data/gridBase/gridDetailIndex.jhtml?mapt="+localMapt+"&gridId="+gridID;
             wingrid = $.ligerDialog.open({height:400, url: _url, title:null,width: 1284, showMax: true, showToggle: true, showMin: false, isResize: true, slide: false ,title:data.gridName,isDrag:true}); 
           wingrid.max();
         // $(".l-dialog-table").attr("width","1360px");
    }
    
    /**
     * 显示人员详细信息
     */
    function showInfoDetail(url) {
    	closeGridIntro();//隐藏网格简介概况层
    	$("#getPersonalMessage").css("width",document.body.clientWidth);
        $("#getPersonalMessage").css("height",document.body.clientHeight);
    	$("#getPersonalMessage").show(500);
    	$("#get_personal_message_frme").css("width",document.body.clientWidth);
        $("#get_personal_message_frme").css("height",document.body.clientHeight);
    	$("#get_personal_message_frme").attr("src", url);//设置网格详细信息iframe的URL
    }
    
    /**
     * 隐藏网格简介概况层
     */
    function closeGridIntro() {
    	layersetData.CloseClickDiv();
    	closeDiv("gridIntroDiv");
    }
    
    /**
     * 隐藏层
     * @param id {String} 层ID
     */
    function closeDiv(id) {
    	
    	$("#" + id).hide();
    	layersetData.CloseClickDiv();
    }
    //---------------------------网格概况信息层 end--------------------------------
    
  //  load_Tiandi();
    
    
$('html').bind('mousewheel',function(){
	  	$("#jzwPrompt").hide();
    	$("#hideDiv2").hide();
	 }); 
	 /*
	 $('html').bind('mouseup',function(){
	  	$("#jzwPrompt").hide();
    	$("#hideDiv2").hide();
	 }); 
	 */
	 
	 $('html').mouseup(function(){
	  	$("#jzwPrompt").hide();
    	$("#hideDiv2").hide();
    	
	 }); 
	 //弹出层
   function ffcs_position(){
	var de = document.documentElement;
  var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
   var h = self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
   ffcs_HEIGHT = ffcs_HEIGHT;
  $("#tab_contoner").css({width:ffcs_WIDTH +"px",height:ffcs_HEIGHT +"px",
    left:0+"px" });
   $("#ffcs_frame").css("height",(ffcs_HEIGHT - $("#contspan").height() - 3) +"px");
   $("#ffcs_frame").css("width",(ffcs_WIDTH - 5)+"px");
//	$("#tab_contoner").css("height",h+"px");
 }
 
function myCzhdiv(url){ 
	ffcs_hide();
	var url = "zzgl/map/data/region/rentRoom.jhtml?gridId="+document.getElementById("gridId").value;
	ffcs_show('出租屋信息',url,0,map_renthous_dialog_width,0,false);//map_renthous_dialog_width:267长乐,249其他
}
function myHiddenDangerdiv(url){
	ffcs_hide();
	var url = "zzgl/map/data/event/hiddendanger.jhtml?eventId=43&gridId="+document.getElementById("gridId").value;
	ffcs_show('安全隐患',url,0,map_pe_dialog_width,0,false);//map_renthous_dialog_width:267长乐,249其他
}
function myHiddenDangerOfFirediv(url){
	ffcs_hide();
	var url = "zzgl/map/data/event/hiddendanger.jhtml?eventId=42&gridId="+document.getElementById("gridId").value;
	ffcs_show('消防火灾隐患',url,0,map_pe_dialog_width,0,false);//map_renthous_dialog_width:267长乐,249其他
}

/***
 * 警物室
 * @param {} url
 */
function myJinwushidiv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/organization.shtml?method=listForGis&gridCode="+document.getElementById("gridCode").value+"&type=04";
   // alert(url);
	ffcs_show('警务室信息',url,0,249,0,false);
	// 初次调用不显示
	if (isFirstLoading) {
		//$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}
/***
 * 巡逻干警
 * @param {} url
 */
function myxunluoganjingdiv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/admin/gridPeAdmin.shtml?method=listXlgjByGridforGis&gridId="+document.getElementById("gridId").value+"&gridCode="+document.getElementById("gridCode").value;
   //alert(url);
	ffcs_show('巡逻段警信息',url,0,249,0,false);
	
	// 初次调用不显示
	
	if (isFirstLoading) {
	//	$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}
/***
 * 巡逻队
 * @param {} url
 */
function myXunluoduidiv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/organization.shtml?method=listForGis&gridCode="+document.getElementById("gridCode").value+"&type=05";
   // alert(url);
	ffcs_show('巡逻队信息',url,0,249,0,false);
	// 初次调用不显示
	if (isFirstLoading) {
		//$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}
/**
 * 案件警情
 * @param {} url
 */
function myZhiBaoHuidiv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/organization.shtml?method=listForGis&gridCode="+document.getElementById("gridCode").value+"&type=03";
   // alert(url);
	ffcs_show('治保会信息',url,0,249,0,false);
	// 初次调用不显示
	if (isFirstLoading) {
		//$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}
/**
 * 案件警情
 * @param {} url
 */
function myCasediv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/admin/event/cases.shtml?method=casesListForGis&gridCode="+document.getElementById("gridCode").value;
   // alert(url);
	ffcs_show('案件警情信息',url,0,249,0,false);
	// 初次调用不显示
	if (isFirstLoading) {
		//$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}

 // 重点场所
     var keyPlacelayer="";
     function readKeyPlaceDataLayer(dataurl,type) {
     	if (dataurl != null) {
            // if (mapt != 4) {
            //     load_3D();
            // }
             //readDataLayer_wg(wg_url,1,'');
             jzw_url = dataurl;
             jzw_type = type;
             dataurl += "&openjzw=" + openjzw + "&mapt=" + mapt;
             layersetData.MoveLayer('queryKeyPlacelayer');
             keyPlacelayer = layersetData.OpenXMLLayer(dataurl, 'queryKeyPlacelayer', 'gid;name;hs,x,y');
             //layer.IsFlashLayer = false;//设定图层是否闪烁，如果为真，在打开gis图闪烁功能时，该图层将闪烁，默认为假
             keyPlacelayer.Style = keyPlaceDisposalStyle(type);
             layersetData.Draw();
         }
     }
     
/**
 * 重点场所
 * @param url
 */
function myZhongDianChangSuoDiv(url) {
	var url = jsContextPath+"/zzgl/map/data/region/place.jhtml?gridId="+document.getElementById("gridId").value;
    $("#get_grid_name_frme").attr("src",url); 
 
}


/**
 * 企业
 * @param {} url
 */
function myCorpdiv(url){
		
          
	var url = jsContextPath+"/zzgl/map/data/region/tjCorBase.jhtml?gridId="+document.getElementById("gridId").value;
	
	$("#get_grid_name_frme").attr("src",url); 
	 
}

/***
 * 消防栓数据列表
 * @param {} url
 */
function xiaofangshuandiv(url){
          
	var url = jsContextPath + "/zzgl/map/data/region/resource.jhtml?resTypeId=22&gridId="+document.getElementById("gridId").value;
	$("#get_grid_name_frme").attr("src",url); 
}

/**
 * 党组织
 * @param {} url
 */
function myDzzidiv(url){
		
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = jsContextPath + "/partyOrganization.shtml?method=listGisdzz&gridId="+document.getElementById("gridId").value+"&gridCode="+document.getElementById("gridCode").value;
 //  alert(url);
	ffcs_show('党组织信息',url,0,249,0,false);
	// 初次调用不显示
	if (isFirstLoading) {
		//$("#tab_contoner").css("display", "none");
	}
	//window.top.hiddMaskLayer();
}
/*
 * 全球眼
 */
function myQqydiv(url){
		
	var url = jsContextPath+"/zzgl/map/data/region/globalEyes.jhtml?orgCode="+document.getElementById("gridCode").value;
	$("#get_grid_name_frme").attr("src",url); 
}

//画网格(new)  台江片区网格
function readDataLayer_segmentGridListByGridId(gridId, gridLevel) {
	if (gridId) {
        document.getElementById("gridId").value = gridId;
    } else {
        gridId = document.getElementById("gridId").value;
    }
    if (layersetData != undefined) {
        layersetData.MoveLayer('showBaseGridList');
    }
    var dataUrl = 'zzgl/map/data/gis/mapDataForSegmentGridListByGridId.jhtml?gridId=' + gridId + '&mapt=' + mapt;
    var layerName = 'querySegmentGridlayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var layer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    this.Style = polygonStylegrid(7,1);
	    layersetData.Draw();
    });
}
//片区网格根据片区网格ids进行查询定位标记
function readDataLayer_segmentGridListByIds(url, type) {
    if (layersetData != undefined) {
        layersetData.MoveLayer('showBaseGridList');
    }
    var dataUrl = url+ '&mapt=' + mapt;
    var layerName = 'querySegmentGridlayer';
    if (layersetData) {
        layersetData.MoveLayer(layerName);
    }
    var layer = layersetData.OpenJsonLayer(dataUrl, layerName, 'gid;name;hs,x,y',function(){
	    this.Style = polygonStylegrid(7,1);
	    layersetData.Draw();
    });
}
//查看片区信息
function showOneSegmentGrid(segmentGridId, segmentGridName) {

	var _showWinClass = showWinClass;
	_showWinClass.height = 410;
	showWinClass.url = jsContextPath+"/zzgl/grid/segmentGrid/detail/"+segmentGridId+".jhtml";
	showWinClass.title = segmentGridName;
	showWinClass.width = 850;
	showWinClass.showMax = false;
	showWinClass.showToggle = true;
	showWinClass.showMin = false;
	showWinClass.isResize = true;
	showWinClass.slide = false;// 动作
	showWinClass.isDrag = true;// 拖动
	showWinClass.isMax = false;
	showWinClass.isunmask = true;// 取消遮罩层
	showWinClass.modal = true;
	showWinClass.name = segmentGridId;
	showWinClass.buttons = null;

	showGridWin(_showWinClass);
}
//查看楼宇信息
function showOneBuildingInfo(buildingId, buildingName) {
	var flag = layer.LocationGeoMetry(buildingId, null);// 2
	if (!flag) {
		// 清除上次选中图片
		var buildId = "#build" + buildingId;
		var buildText = "#buildtext" + buildingId;
		clearLastImgnew(buildId,buildText)
		showOneBuildingInfodiv(buildingId, buildingName);
	} else {
		layersetData.Draw();
	}
}

//查看楼宇信息
function showOneBuildingInfodiv(buildingId, buildingName) {
	
	var _showWinClass = showWinClass;
	_showWinClass.height = 410;
	showWinClass.url = jsContextPath+"/zzgl/grid/areaBuildingInfo/detail.jhtml?buildingId="+buildingId;
	showWinClass.title = buildingName;
	showWinClass.width = 850;
	showWinClass.showMax = false;
	showWinClass.showToggle = true;
	showWinClass.showMin = false;
	showWinClass.isResize = true;
	showWinClass.slide = false;// 动作
	showWinClass.isDrag = true;// 拖动
	showWinClass.isMax = false;
	showWinClass.isunmask = true;// 取消遮罩层
	showWinClass.modal = true;
	showWinClass.name = buildingId;
	showWinClass.buttons = null;

	showGridWin(_showWinClass);
}

/*
 * 网格管理员
 */
function myGridAdmindiv(url){
	var url = jsContextPath+"/zzgl/map/data/region/gridAdmin.jhtml?gridId="+document.getElementById("gridId").value+"&gridCode="+document.getElementById("gridCode").value;
	$("#get_grid_name_frme").attr("src",url); 
	
}

/*function queryByPersonType(type){
	//showMaskLayer();
	ffcs_hide();
		
	var url = getUrlQueryByPersonType(type);
	ffcs_show('人',url,0,map_pe_dialog_width,0,false);//map_pe_dialog_width:275长乐,249其他
	//window.top.hiddMaskLayer();
}*/

function queryByPersonType(url){
	//showMaskLayer();
	ffcs_hide();
	//var url = getUrlQueryByPersonType(type);
	ffcs_show('人',url,0,map_pe_dialog_width,0,false);//map_pe_dialog_width:275长乐,249其他
	//window.top.hiddMaskLayer();
}

function showMaskLayer(){
	var $body=$("body");//取到body对象
	var clientHeight =window.parent.document.body.clientHeight;//父窗体高度
	var div=window.parent.document.getElementById("loading");//取到父窗体的遮罩层对象
	//设置遮罩层样式
	$(div).css({"width":$body.width(),"height":clientHeight,left:$body.offset().left,top:$body.offset().top});
	$("select").hide();//隐藏本窗体的select标签对象
	//显示遮罩层
	$(div).show();
}
/**
 * 根据人员类型搜索
 *
 * @param type 人员类型
 */
function getUrlQueryByPersonType(type){
	//var gridId = document.getElementById("gridId").value;
	var gridId = 7726;
	type = type == null ? "" : type;
	var url = "/zzgrid_hp/gis/residentionfo/index.jhtml?" +
		"gridId=" + gridId + 
		"&queryType=" + type +
		"&page=1"+
		"&partyType=2"+
		"&returnFlag=0";//是否需要返回上一步
	return url;
}

function queryByPersonName(nameObject, nameTip){
	
	clearValueWhenEq(nameObject, nameTip);//当对象的值为v时，清空对象的值。即如果姓名输入框的值为"<%=NAME_TIP%>"即表示未输入姓名
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = getUrlQueryByPersonName(nameObject.value);
	ffcs_show('人',url,0,map_pe_dialog_width,0,false);//map_pe_dialog_width:275,249
	//window.top.hiddMaskLayer();
}

/**
 * 根据人员类型搜索
 *
 * @param type 人员类型
 */
function getUrlQueryByPersonName(name){
	var gridCode = document.getElementById("gridCode").value;
	name = name == null ? "" : name;
	var url = jsContextPath + "/residentinfo.shtml?method=gislist" +
		"&name=" + encodeURI(name) +
		"&queryType=" +
		"&returnFlag=0";//是否需要返回上一步
	return url;
}


/**
 * 根据楼宇类型搜索
 */
function queryByBuildingType(type){
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = getUrlQueryByBuildingType(type);
	ffcs_show('房',url,0,249,0,false);
	//window.top.hiddMaskLayer();
}

/**
 * 根据楼宇类型搜索
 *
 * @param type 楼宇类型
 */
function getUrlQueryByBuildingType(type){
	var gridCode = document.getElementById("gridCode").value;
	type = type == null ? "" : type;
	var url = jsContextPath + "/admin/floor.shtml?method=gisList" +
		"&gridCode=" + gridCode + 
		"&useNature=" + type +
		"&name=" +
		"&returnFlag=0";//是否需要返回上一步
	return url;
}

/**
 * 根据楼宇名称搜索
 */
function queryByBuildingName(nameObject, nameTip){
	clearValueWhenEq(nameObject, nameTip);//当对象的值为v时，清空对象的值。即如果姓名输入框的值为"<%=NAME_TIP%>"即表示未输入姓名
	//window.top.showMaskLayer();
          
	ffcs_hide();
		
	var url = getUrlQueryByBuildingName(nameObject.value);
	ffcs_show('房',url,0,249,0,false);
	//window.top.hiddMaskLayer();
}

/**
 * 根据楼宇名称搜索
 *
 * @param type 人员类型
 */
function getUrlQueryByBuildingName(name){
	var gridCode = document.getElementById("gridCode").value;
	name = name == null ? "" : name;
	var url = jsContextPath + "/admin/floor.shtml?method=gisList" +
		"&gridCode=" + gridCode + 
		"&name=" + encodeURI(name) +
		"&queryType=" +
		"&returnFlag=0";//是否需要返回上一步
	return url;
}

/**
 * 当对象的值为v时，清空对象的值
 
 * @param o {Object} 对象
 * @param v {String}
 */
function clearValueWhenEq(o, v) {
	if (o.value == v) {
		o.value = "";
	}
}

/**
 * 当对象聚焦时，如果对象的值为value，则清空其值。<p>
 * 
 * <pre>
 * 调用举例：文本输入框的"请输入姓名搜索"功能。
 * HTML代码:〈html:text property="name"  style="background:transparent; width:176px; border:1px solid #cdcdcd;float:left;" value="请输入姓名搜索" onfocus="focusName(this,'请输入姓名搜索');" onblur="blurName(this,'请输入姓名搜索');"/〉
 * </pre>
 * 
 * @param o {Object} 对象
 * @param value {String}
 * @see blurObject(o, value)
 */
function focusObject(o, value) {
	if (o.value == value) {
		o.value = "";
	}
}

/**
 * 当对象失去焦点时，如果对象的值为空，则给对象赋值为value。<p>
 * <pre>
 * 调用举例：文本输入框的"请输入姓名搜索"功能。
 * HTML代码:〈html:text property="name"  style="background:transparent; width:176px; border:1px solid #cdcdcd;float:left;" value="请输入姓名搜索" onfocus="focusName(this,'请输入姓名搜索');" onblur="blurName(this,'请输入姓名搜索');"/〉
 * </pre>
 * 
 * @param o {Object} 对象
 * @param value {String}
 * @see focusObject(o, value)
 */
function blurObject(o, value) {
	if (o.value == "") {
		o.value = value;
	}
}
function gis_Pub_GetPoint(callbackfun) {
	funtype = "1";
	layersetData.MapTool.ToGetPoint("");
	layersetData.MapTool.AfterGetPoint = function(geo, flag) {
		var x = geo.Points[0].X;
		var y = geo.Points[0].Y;

		// var xvalue =
		// window.frames.right_event_frame.document.getElementById("resMarkedOM.x");
		// var yvalue =
		// window.frames.right_event_frame.document.getElementById("resMarkedOM.y");

		// xvalue.value=x;
		// yvalue.value=y;
		// showDivMap();
		getXYforBiaoZhu(mapt, x, y);
	}

}

function showyjya(url){
	var url = url;
	//showInfoDetail(url);
	var _showWinClass = showWinClass;
	_showWinClass.height = 420;
	_showWinClass.url = url;
	_showWinClass.title = "应急预案信息";
	_showWinClass.width = 760;
	_showWinClass.showMax = true;
	_showWinClass.showToggle = false;
	_showWinClass.showMin = false;
	_showWinClass.isResize = false;
	_showWinClass.slide = false;// 动作
	_showWinClass.isDrag = true;// 拖动
	_showWinClass.isunmask = true;// 取消遮罩层
	_showWinClass.isMax = true;
	_showWinClass.buttons = null;
	_showWinClass.name = "renyuanjichuxinxi";
	showGridWin(_showWinClass);
}
	function tojjyak(url){
	var url = url;
	//showInfoDetail(url);
	var _showWinClass = showWinClass;
	_showWinClass.height = 420;
	_showWinClass.url = url;
	_showWinClass.title = "应急响应";
	_showWinClass.width = 760;
	_showWinClass.showMax = true;
	_showWinClass.showToggle = false;
	_showWinClass.showMin = false;
	_showWinClass.isResize = false;
	_showWinClass.slide = false;// 动作
	_showWinClass.isDrag = true;// 拖动
	_showWinClass.isunmask = true;// 取消遮罩层
	_showWinClass.isMax = true;
	_showWinClass.buttons = null;
	_showWinClass.name = "renyuanjichuxinxi";
	showGridWin(_showWinClass);
}

