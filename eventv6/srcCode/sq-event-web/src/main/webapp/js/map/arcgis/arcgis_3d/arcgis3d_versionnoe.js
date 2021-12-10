/**
 * 功能介绍：arcgis3d业务封装能力层 版本： v.1 日期： 2019.7.16
 */
$(function() {
	// 获取地图配置信息
	$.fn.ffcsMap.getArcgisInfo('arcgis3D', function(data) {
		var arcgisConfigInfos = eval(data.arcgisConfigInfos);
		if (arcgisConfigInfos.length > 0) {
			// 调用地图初始化函数，默认加载第一个配置
			loadArcgisMap(arcgisConfigInfos[0], {
				mapId : "map0"
			}, function() {
				// 地图初始化完毕加载3D物体
				initMapCallback();
			});
		} else {
			layer.msg('您尚未配置地图服务！', {
				icon : 5
			});
		}
	});
});

function loadArcgisMap(arcgisConfigInfo, options, backFn) {
	var defOpts = {
		mapId : "map",
		slider : false,
		height : $(document).height(),
		width : $(document).width(),
		x : arcgisConfigInfo.mapCenterX, // 中心点X坐标
		y : arcgisConfigInfo.mapCenterY, // 中心点y坐标
		zoom : arcgisConfigInfo.zoom,
		events : {
			click : function(event) {
			},
			double_click : function(view,results) {
				mapDoubleClick(view,results,event);
			},
			drag : function(event) {
			},
			zoom_begin :function(view){
				mapZoomBegin(view);//地图主页必须要有的方法，每个地图个性化回调内容
			},
			zoom_change : function(view) {
				mapZoomChange(view);//地图主页必须要有的方法，每个地图个性化回调内容
			},
			click_3d : function(modelObj) {
			},
			click_graphic : function(graphic) {
				mapClick(graphic);//地图主页必须要有的方法，每个地图个性化回调内容
			}
		}
	};
	defOpts = $.extend({}, defOpts, options);
	$.fn.ffcsMap.initMap(arcgisConfigInfo, defOpts, function() {
		// 地图初始化完毕回调函数
		if (typeof backFn == "function")
			backFn();
	});
}

function loadGeometryBox() {
	var struc3DAry = new Array();
	var struc3D = {
		geometry : {// 几何图形三维
			width : 25,
			height : 70,
			depth : 300
		},
		position : {// 三维坐标位置
			x : 119.313111,
			y : 26.089101,
			z : 90
		},
		material : {// 材质
			opacity : 0.9,
			transparent : true,
			color : '#22559f'
		},
		rotate : {// 旋转角度
			x : 0,
			y : 0,
			z : -2.9111
		}
	};
	struc3DAry.push(struc3D);
	struc3D = {
		geometry : {// 几何图形三维
			width : 100,
			height : 100,
			depth : 1000
		},
		position : {// 三维坐标位置
			x : 119.313,
			y : 26.09,
			z : 90 
		},
		material : {// 材质
			opacity : 0.9,
			transparent : true,
			color : '#ffffff'
		},
		rotate : {// 旋转角度
			x : 0,
			y : 0,
			z : -3
		}
	};
	struc3DAry.push(struc3D);
	$.fn.ffcsMap.Geometry.Box("boxLayer", struc3DAry);
	// 删除例子
	setTimeout(function() {
		// $.fn.ffcsMap.Geometry.RemoveBox("boxLayer");

		var point = $.fn.ffcsMap.getViewCenter();
		/*$.fn.ffcsMap.getView().popup.open({
			// Set the popup's title to the coordinates of the location
			title : "Reverse geocode: [" + point.longitude + ", " + point.latitude + "]",
			location : point,
			content : "This is a point of interest"
		});*/
		var zoom = $.fn.ffcsMap.getZoom();
		$.fn.ffcsMap.setZoom(zoom + 1);
		var layers = $.fn.ffcsMap.getMap().layers;
		console.log("layers:" + layers);

		var defStyle = {
			fillStyle : "solid",// 填充样式
			lineStyle : "solid",// 边框样式
		};
		var strucDataAry = [];
		var gridData = {
			lineColor : "#FFFF00",// 边框颜色
			lineWidth : 3,// 边框线条宽度
			areaColor : "#ADFF2F",// 填充颜色
			nameColor : "#000000",// 文字颜色
			fontSize : "28pt",
			opacity : 0.3, // 透明度
			centerX : 119.31334674186591,
			centerY : 26.08922494838853,
			centerText : "测试画轮廓能力",
			infoOrgCode:'3601',
			points : [ {// 点集合，对象：{ x : 0, y : 0 }
				x : 119.31194489458508,
				y : 26.090262955611514
			}, {
				x : 119.31220520514718,
				y : 26.08830772535337
			}, {
				x : 119.31455782176693,
				y : 26.08840767359968
			}, {
				x : 119.31407401713541,
				y : 26.090537005597106
			} ]
		};
		strucDataAry.push(gridData);

		gridData = {
			lineStyle : "short-dash",// 边框样式
			lineColor : "#000000",// 边框颜色
			lineWidth : 2,// 边框线条宽度
			areaColor : "#abc6ef",// 填充颜色
			nameColor : "#ea0c3e",// 文字颜色
			fontSize : "18pt",
			opacity : 0.5, // 透明度
			centerX : 119.31022791844183,
			centerY : 26.089255473285046,
			centerText : "测试画轮廓123",
			infoOrgCode:'3601',
			points : [ {// 点集合，对象：{ x : 0, y : 0 }
				x : 119.30787145705814,
				y : 26.09044423468106
			}, {
				x : 119.30877088421767,
				y : 26.087953960824045
			}, {
				x : 119.31118030218393,
				y : 26.08815408009509
			}, {
				x : 119.3107697940341,
				y : 26.090331977911156
			} ]
		};   
/*		strucDataAry.push(gridData);

		$.fn.ffcsMap.renderPolygon("gridLayer", strucDataAry, defStyle, function() {
			console.log("渲染完毕！");
			setTimeout(function() {
				// $.fn.ffcsMap.removeLayer("gridLayer", {}, function() {});
			}, 7000);
		});*/

		var strucBuildAry = [];
		var strucBuild = {
			iconUrl : uiDomain + "/images/map/gisv0/map_config/unselected/building_citizenHouse.png",
			iconWidth : 30,
			iconHeight : 39,
			fontColor : "#000000",// 文字颜色
			fontSize : "8pt",
			bizData : {},// 存储业务数据
			x : 119.31132334117645, 
			y : 26.086753682601532,
			text : "测试点位"
		};
		strucBuildAry.push(strucBuild);
		strucBuild = {
			bizData : {},// 存储业务数据
			x : 119.31591136386858, 
			y : 26.087666604852433,
			text : "测试点位3",
			id:33
		};
		strucBuildAry.push(strucBuild);
		$.fn.ffcsMap.renderPoint("buildingLayer", strucBuildAry, {}, function() {
			console.log("渲染完毕！");
			setTimeout(function() {
				// $.fn.ffcsMap.removeLayer("buildingLayer", {}, function() {});
			}, 7000);
		});

	}, 7000);
}

/**
 * 数据转换工具(数据旧格式转新格式)
 * 
 * @param OldData 旧数据  格式：{id:461,isEdit:true,wid:110750,name:aaa,x:112,y:454,hs: "44.3,77.3",mapt:5,lineColor:"#ffff",lineWidth:1,areaColor:"",nameColor:"",infoOrgCode:"340205002",gridCode:"350205002",gridName:"",gridPath:"",mapCenterLevel: ''} 
 * @param isJson 是否返回是json数据 true为json
 * 
 * @return 新格式：
 * {
			lineColor : "#FFFF00",// 边框颜色
			lineWidth : 3,// 边框线条宽度
			areaColor : "#ADFF2F",// 填充颜色
			nameColor : "#000000",// 文字颜色
			fontSize : "28pt",
			opacity : 0.3, // 透明度
			centerX : 119.31334674186591,
			centerY : 26.08922494838853,
			centerText : "测试画轮廓能力",
			infoOrgCode:'3601',
			points : [ {// 点集合，对象：{ x : 0, y : 0 }
				x : 119.31194489458508,
				y : 26.090262955611514
			}, {
				x : 119.31220520514718,
				y : 26.08830772535337
			}, {
				x : 119.31455782176693,
				y : 26.08840767359968
			}, {
				x : 119.31407401713541,
				y : 26.090537005597106
			} ]
		}
 * 
 */

DataCvtUtil = function(OldData,isJson){
	var newObj = {};
	newObj.opacity =OldData.opacity? OldData.opacity :0.5;// 透明度
	//newObj.opacity = 0.5;// 透明度
	newObj.centerText = OldData.name;
	newObj.centerX = OldData.x;
	newObj.centerY = OldData.y;
	newObj.text=OldData.name;
	newObj.id=OldData.id;
	newObj.pointType = OldData.pointType;//点位类型
	newObj.pointTypeStr = OldData.pointTypeStr;//点位类型字典
	var temp_hs= new Array();
	if(OldData.hs != undefined){
		temp_hs = OldData.hs.split(',');
	}
	var tempCrd = [],x=0,y=0;
	//面数据
	if(temp_hs != null || temp_hs.length>0){
		for(var i = 0 ; i<temp_hs.length ; i++){
		   	x = parseFloat(temp_hs[i++]),
			y = parseFloat(temp_hs[i]);
			if(x && y){
				tempCrd.push({x:x,y:y});
			}
			
		}
		newObj.points=tempCrd;
		newObj.lineColor = OldData.lineColor; //边界线颜色
		newObj.lineWidth = OldData.lineWidth; //边界线宽度
		newObj.areaColor = OldData.areaColor; //区域颜色
		newObj.nameColor = OldData.nameColor; //字体颜色
		newObj.fontSize = "18pt";
		newObj.pointLevel= OldData.pointLevel;
		newObj.pointManager= OldData.pointManager;
		newObj.pmTel= OldData.pmTel;
		newObj.infoOrgCode= OldData.infoOrgCode;
		newObj._oldData = OldData;
		newObj.colorNum = OldData.colorNum;//区域透明度
	} 
	if(isJson){
		return newObj;
	}
	return JSON.stringify(newObj);
};

function loadGeometryText() {
	var struc3DAry = new Array();
	var strucText = {
		position : {// 坐标
			x : 119.313111,
			y : 26.089101,
			z : 310
		},
		font : {// 字体属性
			size : 14,
			height : 1,
			color : 0xffffff,
			bevelEnabled : false,
			bevelSize : 1,
			text : "双子星大厦"
		},
		rotate : {// 旋转角度
			x : 45,
			y : 0,
			z : 0
		}
	};
	struc3DAry.push(strucText);
	strucText = {
		position : {// 坐标
			x : 119.313,
			y : 26.09,
			z : 110
		},
		font : {// 字体属性
			size : 14,
			height : 1,
			color : 0x22559f,
			bevelEnabled : false,
			bevelSize : 1,
			text : "东方明珠"
		},
		rotate : {// 旋转角度
			x : 45,
			y : 0,
			z : 0
		}
	};
	struc3DAry.push(strucText);
	$.fn.ffcsMap.Geometry.Text("textLayer", struc3DAry);
	// 删除例子
	setTimeout(function() {
	//	$.fn.ffcsMap.Geometry.RemoveBox("testLayer");
	}, 3000);
}