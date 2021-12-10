/**
 * 功能介绍：arcgis3d引擎封装能力层 版本： v.1 日期： 2019.7.16
 */
;
(function($, _doc, _win) {
	// 引入对象
	var gMap, gSceneView, gTileLayer, gWebTileLayer, gTileInfo, gExternalRenderers, gSpatialReference, gEsriRequest, gUrlUtils, gCamera, gPolyline, gPoint, gSimpleFillSymbol, gColor, gGraphic, gSimpleLineSymbol, gTextSymbol, gFont, gGraphicsLayer, gPolygon, gPictureMarkerSymbol;
    var gLang;
	// 3D对象
	var gScene, gCamera, gAmbient, gSun;
	// 创建对象
	var map, view;
	$.fn.ffcsMap = {};
	// 默认配置: div id="map"
	$.fn.ffcsMap.defaults = {
		mapId : "map", // div 名称
		height : 0,
		width : 0,
		x : -97.395, // 中心点x值
		y : 37.537, // 中心点y值
		zoom : 11, // 缩放级别
		slider : false,
		arcgisConfigInfo : null, // 地图相关配置信息
		events : {
			click : null,// 单击回调函数
			double_click : null,// 双击回调函数
			drag : null,// 拖动地图回调函数
			resize : null,// 视图大小更改时激发
			zoom_change : null,
			click_3d : null,
			click_graphic : null
		// 点击绘图回调函数
		}
	};
	var gIssRenderers = [];
	/**
	 * 获取arcgis地图路径的配置信息
	 */
	$.fn.ffcsMap.getArcgisInfo = function(engineName, backFn) {
		$.ajax({
			url : js_ctx + '/zhsq/map/arcgis/arcgis/getArcgisInfoForJsonp.json?jsoncallback=?&engineName=' + engineName + '&t=' + Math.random(),
			type : 'POST',
			timeout : 3000,
			dataType : "jsonp",
			error : function(data) {
				layer.msg('地图配置信息获取出现异常！', {
					icon : 5
				});
			},
			success : function(data) {
				if (typeof backFn == "function"){ 
					backFn(data);
				}
			}
		});
	};
	// 初始化地图
	$.fn.ffcsMap.initMap = function(arcgisConfigInfo, options, backFn) {
		var _this = $(this);
		$.extend(true, $.fn.ffcsMap.defaults, {
			arcgisConfigInfo : arcgisConfigInfo
		}, options);
		var opts = $.fn.ffcsMap.defaults;
		require([ "esri/Map", "esri/views/SceneView", "esri/layers/TileLayer", "esri/layers/WebTileLayer", "esri/layers/support/TileInfo", "esri/views/3d/externalRenderers", "esri/geometry/SpatialReference", "esri/request", "esri/core/urlUtils",
				"esri/Camera", "esri/geometry/Polyline", "esri/geometry/Point", "esri/symbols/SimpleFillSymbol", "esri/Color", "esri/Graphic", "esri/symbols/SimpleLineSymbol", "esri/symbols/TextSymbol", "esri/symbols/Font",
				"esri/layers/GraphicsLayer", "esri/geometry/Polygon", "esri/symbols/PictureMarkerSymbol","dojo/_base/lang" ], function(Map, SceneView, TileLayer, WebTileLayer, TileInfo, externalRenderers, SpatialReference, esriRequest, urlUtils, Camera,
				Polyline, Point, SimpleFillSymbol, Color, Graphic, SimpleLineSymbol, TextSymbol, Font, GraphicsLayer, Polygon, PictureMarkerSymbol,lang) {
			gMap = Map;
			gSceneView = SceneView;
			gTileLayer = TileLayer;
			gWebTileLayer = WebTileLayer;
			gTileInfo = TileInfo;
			gExternalRenderers = externalRenderers;
			gSpatialReference = SpatialReference;
			gEsriRequest = esriRequest;
			gUrlUtils = urlUtils;
			gPolyline = Polyline;
			gPoint = Point;
			gSimpleFillSymbol = SimpleFillSymbol;
			gColor = Color;
			gGraphic = Graphic;
			gSimpleLineSymbol = SimpleLineSymbol;
			gTextSymbol = TextSymbol;
			gFont = Font;
			gGraphicsLayer = GraphicsLayer;
			gPolygon = Polygon;
			gPictureMarkerSymbol = PictureMarkerSymbol;
    	    gLang=lang; 
			
			if (arcgisConfigInfo.arcgisServiceInfos.length > 0) {
				var baseLayers = new Array();
				$.fn.ffcsMap.createMapStep1(baseLayers, arcgisConfigInfo, opts, 0, function() {
					$.fn.ffcsMap.createMapStep2(baseLayers, arcgisConfigInfo, opts);
					if (backFn) backFn();
				});
			} else {
				layer.msg('地图配置缺少底图服务信息！', {
					icon : 5
				});
			}
		});
	};
	// 创建底图
	$.fn.ffcsMap.createMapStep1 = function(baseLayers, arcgisConfigInfo, opts, startIndex, backFn) {
		var arcgisServiceInfo = arcgisConfigInfo.arcgisServiceInfos[startIndex];
		$.fn.ffcsMap.fetchMapTokenForUrl(arcgisServiceInfo, function() {
			var layer = $.fn.ffcsMap.createLayer(arcgisConfigInfo, arcgisServiceInfo, opts);
			baseLayers.push(layer);
			if (arcgisConfigInfo.arcgisServiceInfos.length - 1 == startIndex) {
				if (backFn) backFn();
			} else {
				$.fn.ffcsMap.createMapStep1(baseLayers, arcgisConfigInfo, opts, ++startIndex, backFn);
			}
		});
	};
	$.fn.ffcsMap.fetchMapTokenForUrl = function(arcgisServiceInfo, backFn) {
		if (arcgisServiceInfo.tokenJs) {
			$.getScript(js_ctx + "/js/map/arcgis/token/" + arcgisServiceInfo.tokenJs, function() {
				var serviceUrl = arcgisServiceInfo.agentServiceUrl ? arcgisServiceInfo.agentServiceUrl : arcgisServiceInfo.serviceUrl;
				fetchMapTokenForUrl(serviceUrl, function(newUrl) {
					arcgisServiceInfo.agentServiceUrl = newUrl;
					if (backFn) backFn();
				});
			});
		} else {
			if (backFn) backFn();
		}
	};
	// 创建地图和场景
	$.fn.ffcsMap.createMapStep2 = function(baseLayers, arcgisConfigInfo, opts) {

		// 创建空Map对象
		map = new gMap({
			basemap : {
				baseLayers : baseLayers
			}
		});
	// 创建3D场景视图
		view = new gSceneView({
			container : opts.mapId,
			spatialReference : {
				wkid : arcgisConfigInfo.wkid
			},
			map : map,
			zoom : opts.zoom,
			center : {
				x : opts.x,
				y : opts.y,
				spatialReference : {
					wkid : arcgisConfigInfo.wkid
				}
			}
		});
		
		view.environment.lighting.cameraTrackingEnabled = true;// 启用摄像头跟踪
		var date = new Date(2019, 7, 30, 8, 30, 30);
		view.environment.lighting.date = date;
		// 指示是否显示由太阳投射的阴影。阴影仅为真实世界的三维对象显示
		view.environment.lighting.directShadowsEnabled = true;
		// 指示是否显示环境光遮挡阴影
		view.environment.lighting.ambientOcclusionEnabled = true;

		// setup the three.js scene
		// /////////////////////////////////////////////////////////////////////////////////////

		gScene = new THREE.Scene();

		// setup the camera
		gCamera = new THREE.PerspectiveCamera();

		// setup scene lighting
		gAmbient = new THREE.AmbientLight(0xffffff, 0.5);// 环境光
		gScene.add(gAmbient);
		gSun = new THREE.DirectionalLight(0xffffff, 0.5);// 平行光
		gScene.add(gSun);
		/*
		 * var cam = new Camera({ heading : 360, //
		 * 摄像机的罗盘航向（度）。当北边是屏幕顶部时，航向为零。它随着视图顺时针旋转而增加。角度总是在0到360度之间进行标准化。 tilt :
		 * 45, // 从相机位置向下投影时，相机相对于表面的倾斜度。当直视表面时，倾斜度为零；当相机平行于表面时，倾斜度为90度。
		 * position : { longitude : opts.x, latitude : opts.y, z : 520,
		 * spatialReference : { wkid : arcgisConfigInfo.wkid } } });
		 * view.goTo(cam);
		 */
		view.goTo({
			tilt : 0
		});
		$.fn.ffcsMap.on();
	};

	// 创建图层
	$.fn.ffcsMap.createLayer = function(arcgisConfigInfo, arcgisServiceInfo, options) {
		var arcgisScalenInfos = arcgisConfigInfo.arcgisScalenInfos;
		switch (arcgisServiceInfo.serviceLoadType) {
		case "tiled":
			var agentUrl = arcgisServiceInfo.agentServiceUrl;
			var tiledLayer = new gTileLayer({
				spatialReference : {
					wkid : arcgisConfigInfo.wkid
				},
				url: agentUrl ? agentUrl : arcgisServiceInfo.serviceUrl// URL points to a cached tiled map service hosted on
						// ArcGIS Server
			});
			return tiledLayer;
		case "wmts":
			// 组装地图层级
			var lods = new Array();
			if (arcgisScalenInfos.length > 0) {
				for (var i = 0; i < arcgisScalenInfos.length; i++) {
					lods[i] = {};
					lods[i]['level'] = arcgisScalenInfos[i].scaleLevel;
					lods[i]['levelValue'] = arcgisScalenInfos[i].scaleLevel + 1;
					lods[i]['resolution'] = arcgisScalenInfos[i].scaleResolution;
					lods[i]['scale'] = arcgisScalenInfos[i].scaleScale;
				}
			}
			// 包含有关瓷砖铺层、立面层和WebTileLayers的瓷砖方案的信息
			var tileInfo = new gTileInfo({
				dpi : 90.71428571427429,// 90.71428571427429 瓷砖方案的每英寸点数（dpi），96意思坐标单位是米
				rows : arcgisConfigInfo.mapRows,
				cols : arcgisConfigInfo.mapCols,
				compressionQuality: 0,
				origin : {
					x : arcgisConfigInfo.mapOrgiginX,
					y : arcgisConfigInfo.mapOrgiginY
				},
				spatialReference : {
					wkid : arcgisConfigInfo.wkid
				},
				lods : lods
			});
			var url = arcgisServiceInfo.serviceUrl;
			if (url.indexOf("tianditu.com") != -1) {
				url = "{serviceUrl}/{layer}_{TileMatrixSet}/wmts?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile&LAYER={layer}&STYLE=default&FORMAT={serviceImageFormat}&TILEMATRIXSET={TileMatrixSet}&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}&tk={tk}";
			} else {
				url = "{serviceUrl}?SERVICE=WMTS&VERSION=1.0.0&REQUEST=GetTile&LAYER={layer}&STYLE=default&FORMAT={serviceImageFormat}&TILEMATRIXSET={TileMatrixSet}&TILEMATRIX={level}&TILEROW={row}&TILECOL={col}";
			}
			var agentUrl = arcgisServiceInfo.agentServiceUrl;
			url = $.fn.ffcsMap.URLAdapter({
				serviceUrl : agentUrl ? agentUrl : arcgisServiceInfo.serviceUrl,
				serviceMode : arcgisServiceInfo.serviceServiceMode,
				layer : arcgisServiceInfo.serviceLayerId,
				TileMatrixSet : arcgisServiceInfo.serviceTileMartrixSetId,
				serviceImageFormat : arcgisServiceInfo.serviceImageFormat,
				tk : arcgisConfigInfo.mapKey
			}, url);
			
			var tiledLayer = new gWebTileLayer(url, {
				subDomains : [ "t0" ],
				tileInfo : tileInfo,
				spatialReference : { wkid: arcgisConfigInfo.wkid }
			});
			return tiledLayer;
		}
	};

	$.fn.ffcsMap.URLAdapter = function(opts, url) {
		$.each(opts, function(key, val) {
			url = url.replaceAll("{" + key + "}", val);
		});
		return url;
	};

	$.fn.ffcsMap.getZoom = function() {
		return view.zoom;
	};

	$.fn.ffcsMap.setZoom = function(zoom) {
		view.zoom = zoom;
	};
	/**
	 * view.goTo({ center: [-126, 49], zoom: 13, tilt: 75, heading: 105 });
	 */
	$.fn.ffcsMap.goToView = function(opts) {
		view.goTo(opts);
	};

	// 获取屏幕中心点 return Point
	$.fn.ffcsMap.getViewCenter = function() {
		return view.center;
		/*
		 * var screenPoint = { x : $(_doc).width() / 2, y : $(_doc).height() / 2 };
		 * return view.toMap(screenPoint);
		 */
	};
	
	
	$.fn.ffcsMap.getViewExtent = function() {
		return view.extent;
	};

	$.fn.ffcsMap.getView = function() {
		return view;
	};

	$.fn.ffcsMap.getMap = function() {
		return map;
	};
	
	$.fn.ffcsMap.gPictureMarkerSymbol = function() {
		return gPictureMarkerSymbol;
	};
	
	/**
	 * 复制图层更改透明度（统一更改样式）
	 * @param layerName
	 * @param newStyle = {
				fillStyle : "solid",// 填充样式
				lineStyle : "solid",// 边框样式
				lineColor : "#FFFF00",// 边框颜色
				lineWidth : 2,// 边框线条宽度
				areaColor : "#ADFF2F",// 填充颜色
				nameColor : "#000000",// 文字颜色
				fontSize : "8pt",
				opacity : 0.3, // 透明度
				centerText : ""
			}
	 */
	$.fn.ffcsMap.cloneLayerSetOpacity = function(layerName, newStyle) {
		var graphicsLayer = map.findLayerById(layerName);
		if (graphicsLayer) {
			// 清除3D网格样式
			$.fn.ffcsMap.resetOutline3D("clear");
			// 样式默认定义
			var defStyle = {
				fillStyle : "solid",// 填充样式
				lineStyle : "solid",// 边框样式
				lineColor : "#FFFF00",// 边框颜色
				lineWidth : 2,// 边框线条宽度
				areaColor : "#ADFF2F",// 填充颜色
				nameColor : "#000000",// 文字颜色
				fontSize : "8pt",
				opacity : 0.3, // 透明度
				centerText : ""
			};
			var graphics = graphicsLayer.graphics;
			var newGraphics = new Array();
			for (var i = graphics.length - 1; i >= 0; i--) {
				var oldGraphic = graphics.getItemAt(i);
				var oldData = oldGraphic.getAttribute("data");
				var type = oldGraphic.getAttribute("type");
				if (type == "polygon" || type == "text") {
					var newGraphic = oldGraphic.clone();
					var data = $.extend(true, defStyle, oldData, newStyle);
					var newData = $.extend(true, oldData, data);
					newGraphic.setAttribute("data", newData);
					if (type == "polygon") {
						// 十六进制颜色转为RGB
						var RGBLineColor = new gColor(data.lineColor).toRgb();
						var RGBAreaColor = new gColor(data.areaColor).toRgb();
						// 网格样式 默认显示轮廓
						var lineSymbol = new gSimpleLineSymbol({
							style : data.lineStyle,
							color : new gColor([ RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1 ]),
							width : data.lineWidth
						});
						var symbol = new gSimpleFillSymbol(data.fillStyle, lineSymbol, new gColor([ RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], data.opacity ]));
						newGraphic.symbol = symbol;
						newGraphic.geometry.hasZ = false;
						newGraphic.geometry.hasM = false;
						graphicsLayer.remove(oldGraphic);
						graphicsLayer.add(newGraphic);
					} else if (type == "text") {
						// 十六进制文字颜色转为RGB
						var RGBNameColor = new gColor(data.nameColor).toRgb();
						// 文字样式 默认半透明
						var textSymbol = new gTextSymbol({
							color : new gColor([ RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1 ]),
							text : data.centerText + '\n',
							font : new gFont({
								size : data.fontSize,
								weight : "bold"
							})
						});
						newGraphic.symbol = textSymbol;
						newGraphic.geometry.hasZ = false;
						newGraphic.geometry.hasM = false;
						graphicsLayer.remove(oldGraphic);
						graphicsLayer.add(newGraphic);
					}
				}
			}
		}
	};

	/**
	 * 画标注点接口
	 * 
	 * @param layerName
	 *            图层名称
	 * @param strucDataAry
	 *            网格信息对象集
	 * @param otherOpts
	 *            修改默认样式参数
	 * @param backFn
	 */
	$.fn.ffcsMap.renderPoint = function(layerName, strucDataAry, otherOpts, backFn) {
		if (strucDataAry != null && strucDataAry.length == 0)
			return;
		// 样式默认定义
		var _struc = {
			iconUrl : uiDomain + "/images/map/gisv0/map_config/unselected/build/build_locate_point_red.png",
			iconWidth : 18,
			iconHeight : 28,
			fontColor : "#fff",// 文字颜色
			fontSize : "18pt",
			bizData : {},// 存储业务数据
			x : 0,
			y : 0,
			text : ""
		};
		$.extend(true, _struc, otherOpts);
		// 坐标系
		var spatialReference = new gSpatialReference($.fn.ffcsMap.defaults.arcgisConfigInfo.wkid);
		// 创建绘图图层
		var graphicsLayer = map.findLayerById(layerName);
		if (typeof graphicsLayer == "undefined") {
			graphicsLayer = new gGraphicsLayer({
				id : layerName
			});
			map.layers.add(graphicsLayer);
		}
		$.each(strucDataAry, function(i, strucData) {
			var data = $.extend(true, {}, _struc, strucData);
			// 给对象设值 用来区分点、楼宇还是网格
			data.layerName = layerName;
			
			// 构造标注点
			var point = new gPoint(data.x, data.y, spatialReference);
			
			// 标注点
			var symbol = new gPictureMarkerSymbol({
				url : data.iconUrl,
				width : data.iconWidth,
				height : data.iconHeight,
				xoffset : 0,
				yoffset : data.iconHeight / 2
			});
			var graphic = new gGraphic({
				symbol : symbol,
				geometry : point,
				attributes : {
					"data" : data,
					"type" : "point"
				}
			});
			
			graphicsLayer.add(graphic);
		});
		if (typeof backFn == "function")
			backFn();
	};

	/**
	 * 删除图层接口
	 * 
	 * @param layerName
	 *            图层名称
	 * @param strucDataAry
	 *            网格信息对象集
	 * @param otherOpts
	 *            修改默认样式参数
	 * @param backFn
	 */
	$.fn.ffcsMap.removeLayer = function(layerName, otherOpts, backFn) {
		if (typeof map == "undefined") {
			console.log('fn $.fn.ffcsMap.removeLayer map == null ');
			return;
		}
		var graphicsLayer = map.findLayerById(layerName);
		if (typeof graphicsLayer != "undefined") {
			map.remove(graphicsLayer);
		}
		// 清除3D网格样式
		$.fn.ffcsMap.resetOutline3D("clear");
		if (typeof backFn == "function")
			backFn();
	};
	/**
	 * 画多边形接口
	 * 
	 * @param layerName
	 *            图层名称
	 * @param strucDataAry
	 *            网格信息对象集
	 * @param otherOpts
	 *            修改默认样式参数
	 * @param backFn
	 */
	$.fn.ffcsMap.renderPolygon = function(layerName, strucDataAry, otherOpts, backFn) {
		if (strucDataAry != null && strucDataAry.length == 0)
			return;
		// 样式默认定义
		var _struc = {
			fillStyle : "solid",// 填充样式
			lineStyle : "solid",// 边框样式
			lineColor : "#FFFF00",// 边框颜色
			lineWidth : 2,// 边框线条宽度
			areaColor : "#ADFF2F",// 填充颜色
			nameColor : "#000000",// 文字颜色
			fontSize : "8pt",
			opacity : 0.3, // 透明度
			points : [],// 点集合，对象：{ x : 0, y : 0 }
			bizData : {},// 存储业务数据
			centerX : 0,
			centerY : 0,
			centerText : "",
			dot : {
				color : "#FF0000",
				outline: {
					color: "#FFFFFF",
					width: 2
				}
			}
		};
		$.extend(true, _struc, otherOpts);
		// 坐标系
		var spatialReference = new gSpatialReference($.fn.ffcsMap.defaults.arcgisConfigInfo.wkid);
		// 创建绘图图层
		var graphicsLayer = map.findLayerById(layerName);
		if (typeof graphicsLayer == "undefined") {
			graphicsLayer = new gGraphicsLayer({
				id : layerName
			});
			if (layerName == "gridLayer") {
				map.layers.add(graphicsLayer, 0);// 网格默认最底层级
			}else{
				map.layers.add(graphicsLayer, 1);// 网格默认最底层级
			}
		}
		$.each(strucDataAry, function(i, strucData) {
			var data = $.extend(true, {}, _struc, strucData);
			// 给对象设值 用来区分点、楼宇还是网格
			data.layerName = layerName;
			// 组装轮廓点位
			var polygon = new gPolygon(spatialReference);
			var rings = [];
			for (var i = 0; i < data.points.length; i++) {
				rings[i] = new gPoint({
					longitude : data.points[i].x,
					latitude : data.points[i].y
				});
			}
			polygon.addRing(rings);

			// 十六进制颜色转为RGB
			var RGBLineColor = new gColor(data.lineColor).toRgb();
			var RGBAreaColor = new gColor(data.areaColor).toRgb();
			// 十六进制文字颜色转为RGB
			var RGBNameColor = new gColor(data.nameColor).toRgb();

			// 网格样式 默认显示轮廓
			var lineSymbol = new gSimpleLineSymbol({
				style : data.lineStyle,
				color : new gColor([ RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1 ]),
				width : data.lineWidth
			});
			var symbol = new gSimpleFillSymbol(data.fillStyle, lineSymbol, new gColor([ RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], data.opacity ]));
			// 添加轮廓
			var graphicPolygon = new gGraphic({
				symbol : symbol,
				geometry : polygon,
				attributes : {
					"data" : data,
					"type" : "polygon"
				}
			});
			graphicsLayer.add(graphicPolygon);

			if (data.centerX > 0 && data.centerY > 0 && data.centerText && data.centerText.length >0) {
				// 构造中心点
				var point = new gPoint(data.centerX, data.centerY, spatialReference);
				// 文字样式 默认半透明
				var textSymbol = new gTextSymbol({
					color : new gColor([ RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1 ]),
					text : data.centerText + '\n',
					font : new gFont({
						size : data.fontSize,
						weight : "bold"
					})
				});
				var graphicsText = new gGraphic({
					symbol : textSymbol,
					geometry : point,
					attributes : {
						"data" : data,
						"type" : "text"
					}
				});
				graphicsLayer.add(graphicsText);
				// 中心圆点
				var dotSymbol = {
					type: "simple-marker", // autocasts as new SimpleMarkerSymbol()
					color: data.dot.color,
					outline: {
						color: data.dot.outline.color,// autocasts as new SimpleLineSymbol()
						width: data.dot.outline.width
					}
				};

				var graphicDot = new gGraphic({
					geometry: point,
					symbol: dotSymbol,
					attributes : {
						"data" : data,
						"type" : "dot"
					}
				});
				graphicsLayer.add(graphicDot);
			}
		});

		// 回调
		if (typeof backFn == "function") {
			backFn(graphicsLayer);
		}
	};
	$.fn.ffcsMap.getWallHeight = function() {
		var zoom = Math.round(view.zoom);
		var height = 750;
		switch(zoom) {
		case 1:height = 3000;break;
		case 2:height = 2750;break;
		case 3:height = 2500;break;
		case 4:height = 2250;break;
		case 5:height = 2000;break;
		case 6:height = 1750;break;
		case 7:height = 1500;break;
		case 8:height = 1250;break;
		case 9:height = 1000;break;
		case 10:height = 750;break;
		case 11:height = 500;break;
		case 12:height = 300;break;
		case 13:height = 150;break;
		case 14:height = 100;break;
		case 15:height = 50;break;
		case 16:height = 40;break;
		case 17:height = 30;break;
		case 18:height = 20;break;
		case 19:height = 10;break;
		case 20:height = 5;break;
		}
		return height;
	};
	
	// 网格轮廓3D效果
	var _curr_move_grid_graphic = {
		wid : null,
		polygon : null,
		wall : null,
		dot : null,
		text : null
	};
	var _prev_move_grid_graphic = [];
	
	var _curr_click_grid_graphic = {
		wid : null,
		polygon : null,
		wall : null,
		dot : null,
		text : null
	};
	var _prev_click_grid_graphic = [];
	
	$.fn.ffcsMap.resetOutline3D = function(type) {
		function _clear_click() {
			if (_curr_click_grid_graphic.wid) {
				graphicsLayer.remove(_curr_click_grid_graphic.polygon);
				graphicsLayer.remove(_curr_click_grid_graphic.wall);
				graphicsLayer.remove(_curr_click_grid_graphic.dot);
				graphicsLayer.remove(_curr_click_grid_graphic.text);
				_curr_click_grid_graphic.wid = null;
				_curr_click_grid_graphic.polygon = null;
				_curr_click_grid_graphic.wall = null;
				_curr_click_grid_graphic.dot = null;
				_curr_click_grid_graphic.text = null;
			}
			for (var i = _prev_click_grid_graphic.length - 1; i >= 0; i--) {
				graphicsLayer.add(_prev_click_grid_graphic[i]);
				_prev_click_grid_graphic.splice(i, 1);
			}
		}
		
		function _clear_move() {
			if (_curr_move_grid_graphic.wid) {
				graphicsLayer.remove(_curr_move_grid_graphic.polygon);
				graphicsLayer.remove(_curr_move_grid_graphic.wall);
				graphicsLayer.remove(_curr_move_grid_graphic.dot);
				graphicsLayer.remove(_curr_move_grid_graphic.text);
				_curr_move_grid_graphic.wid = null;
				_curr_move_grid_graphic.polygon = null;
				_curr_move_grid_graphic.wall = null;
				_curr_move_grid_graphic.dot = null;
				_curr_move_grid_graphic.text = null;
			}
			for (var i = _prev_move_grid_graphic.length - 1; i >= 0; i--) {
				graphicsLayer.add(_prev_move_grid_graphic[i]);
				_prev_move_grid_graphic.splice(i, 1);
			}
		}
		
		function _transfer_moveToclick() {
			_curr_click_grid_graphic.wid = _curr_move_grid_graphic.wid;
			_curr_click_grid_graphic.polygon = _curr_move_grid_graphic.polygon;
			_curr_click_grid_graphic.wall = _curr_move_grid_graphic.wall;
			_curr_click_grid_graphic.dot = _curr_move_grid_graphic.dot;
			_curr_click_grid_graphic.text = _curr_move_grid_graphic.text;
			
			_curr_move_grid_graphic.wid = null;
			_curr_move_grid_graphic.polygon = null;
			_curr_move_grid_graphic.wall = null;
			_curr_move_grid_graphic.dot = null;
			_curr_move_grid_graphic.text = null;
			
			_prev_click_grid_graphic = _prev_move_grid_graphic;
			_prev_move_grid_graphic = [];
		}
		
		var graphicsLayer = map.findLayerById("gridLayer");
		if (graphicsLayer) {
			if (type == "move") {
				_clear_move();
			} else if (type == "click") {
				_clear_click();
				_transfer_moveToclick();
			} else if (type == "clear") {
				_clear_move();
				_clear_click();
			}
		}
	};
	$.fn.ffcsMap.gridOutline3D = function(graphic, type) {
		if (!graphic) {
			$.fn.ffcsMap.resetOutline3D("clear");
			return;
		}
		// 样式默认定义
		var _struc = {
			polygon : {
				fillStyle : "solid",
				lineStyle : "solid",
				height : $.fn.ffcsMap.getWallHeight(),
				lineColor : "#C0FEFF",
				lineWidth : 2,
				areaColor : "#37F3FF",
				lineOpacity : 1,
				areaOpacity : 0.6
			},
			wall : {
				color : "#404640",
				height : $.fn.ffcsMap.getWallHeight() - 5,
				width : 3
			}
		};
		var data = graphic.getAttribute("data");
		// 判断绘图是否有Z轴
		if (!graphic.geometry.hasZ || type == "click") {
			if (type == "move") {// 从2D滑入
				$.fn.ffcsMap.resetOutline3D("move");
				
				var graphicsLayer = map.findLayerById("gridLayer");
				
				_curr_move_grid_graphic.wid = data._oldData.wid;
				_prev_move_grid_graphic.push(graphic.clone());
				
				var c_graphic = graphic.clone();// 需要克隆才有效果
				var polygon = c_graphic.geometry;
				
				var paths = [];
				var rings = polygon.rings[0];
				for (var i = 0; i < rings.length; i++) {
					paths[i] = [ rings[i][0], rings[i][1] ];
					rings[i][2] = _struc.polygon.height;
				}
				// 抬高多边形
				polygon.hasZ = true;
				polygon.hasM = false;
				polygon.removeRing(0);
				polygon.addRing(rings);
				
				// 十六进制颜色转为RGB
				var RGBLineColor = new gColor(_struc.polygon.lineColor).toRgb();
				var RGBAreaColor = new gColor(_struc.polygon.areaColor).toRgb();
				// 网格样式 默认显示轮廓
				var lineSymbol = new gSimpleLineSymbol({
					style : _struc.polygon.lineStyle,
					color : new gColor([ RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], _struc.polygon.lineOpacity ]),
					width : _struc.polygon.lineWidth
				});
				var symbol = new gSimpleFillSymbol({
					style : _struc.polygon.fillStyle, 
					outline : lineSymbol, 
					color : new gColor([ RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _struc.polygon.areaOpacity ])
				});
				c_graphic.symbol = symbol;
				
				graphicsLayer.remove(graphic);
				graphicsLayer.add(c_graphic);
				_curr_move_grid_graphic.polygon = c_graphic;
				// 添加围墙
				var wallPolyline = new gPolyline({
					spatialReference : polygon.spatialReference,
					paths : [ paths ]
				});
				const wallSymbol = {
					type : "line-3d",
					symbolLayers : [ {
						type : "path",
						profile : "quad",
						material : {
							color : _struc.wall.color
						},
						width : _struc.wall.width, // the width in m
						height : _struc.wall.height, // the height in m
						anchor : "bottom", // the vertical anchor is set to the lowest point of the wall
						profileRotation : "all"
					} ]
				};
				var graphicPolyline = new gGraphic({
					symbol : wallSymbol,
					geometry : wallPolyline,
					attributes : {
						"data" : data,
						"type" : "grid_wall"
					}
				});
				graphicsLayer.add(graphicPolyline);
				_curr_move_grid_graphic.wall = graphicPolyline;
				
				var graphics = graphicsLayer.graphics;
				for (var i = graphics.length - 1; i >= 0; i--) {
					var oldGraphic = graphics.getItemAt(i);
					var oldData = oldGraphic.getAttribute("data");
					var oldType = oldGraphic.getAttribute("type");
					if ((oldType == "text" || oldType == "dot") && oldData.layerName == "gridLayer" && oldData._oldData.wid === data._oldData.wid) {
						_prev_move_grid_graphic.push(oldGraphic.clone());
						var newGraphic = oldGraphic.clone();
						var point = newGraphic.geometry;
						point.hasZ = true;
						point.hasM = false;
						point.z = _struc.polygon.height + 30;
						graphicsLayer.remove(oldGraphic);
						graphicsLayer.add(newGraphic);
						_curr_move_grid_graphic[oldType] = newGraphic;
					}
				}
			} else if (type == "click") {
				$.fn.ffcsMap.resetOutline3D("click");
			}
		} else if (type == "move" && data._oldData.wid == _curr_click_grid_graphic.wid) {// 从3D滑入
			$.fn.ffcsMap.resetOutline3D("move");
		}
	};

	// 地图绑定事件
	$.fn.ffcsMap.on = function(mapId) {
		var spatialReference = new gSpatialReference($.fn.ffcsMap.defaults.arcgisConfigInfo.wkid);
		var graphicsText = null;
		view.on("pointer-move", function(event) {
			view.hitTest(event).then(function(response) {
				var result = response.results[0];
				if (result) {
					var type = result.graphic.getAttribute("type");
					var data = result.graphic.getAttribute("data");
					if (data && type == "point") {
						if (graphicsText) {
							graphicsText.layer.remove(graphicsText);
							graphicsText = null;
						}
						// 十六进制文字颜色转为RGB
						var color = new gColor(data.fontColor);
						// 构造标注点
						var point = new gPoint(data.x, data.y, spatialReference);
						// 文字样式 默认半透明
						var textSymbol = new gTextSymbol({
							color : color,
							text : data.text,
							font : new gFont({
								size : data.fontSize,
								weight : "bold"
							}),
							xoffset : 0,
							yoffset : 20
						});
						graphicsText = new gGraphic({
							symbol : textSymbol,
							geometry : point
						});
						result.graphic.layer.add(graphicsText);
					} else if (data && data.layerName == "gridLayer" && type == "polygon") {
						$.fn.ffcsMap.gridOutline3D(result.graphic, "move");
					} else {
						if (graphicsText) {
							graphicsText.layer.remove(graphicsText);
							graphicsText = null;
						}
					}
				} else {
					if (graphicsText) {
						graphicsText.layer.remove(graphicsText);
						graphicsText = null;
					}
					// 重置网格
					$.fn.ffcsMap.resetOutline3D("move");
				}
			});
		});
		
		if (typeof $.fn.ffcsMap.defaults.events.double_click == "function") {
			view.on("double-click", function(event) {
				event.stopPropagation();
				view.hitTest(event).then(function(response) {
					$.fn.ffcsMap.defaults.events.double_click(view,response.results[0],event);
				});
			});
		}
		view.on("click", function(event) {
			view.hitTest(event).then(function(response) {
				var result = response.results[0];
				if (result) {//必须要有的方法没必要再判断是否存在
					var type = result.graphic.getAttribute("type");
					var data = result.graphic.getAttribute("data");
					if (data && data.layerName == "gridLayer" && type == "polygon") {
						$.fn.ffcsMap.gridOutline3D(result.graphic, "click");
					}
					//if (typeof $.fn.ffcsMap.defaults.events.click_graphic == "function") {
						//if (result.graphic.getAttribute("data")) {
					$.fn.ffcsMap.defaults.events.click_graphic(result.graphic);
						//}
					//}
				} else {
					// 重置网格
					$.fn.ffcsMap.resetOutline3D("clear");
					//if (typeof $.fn.ffcsMap.defaults.events.click == "function") {
					$.fn.ffcsMap.defaults.events.click(event);
					//}
				}
			});
		});
		var dragIndex = 0;
		if (typeof $.fn.ffcsMap.defaults.events.drag == "function") {

		}
		if (typeof $.fn.ffcsMap.defaults.events.resize == "function") {
			view.on("resize", function(e) {
				$.fn.ffcsMap.defaults.events.resize(event);
			});
		}
		if (typeof $.fn.ffcsMap.defaults.events.zoom_change == "function") {
			var zoomIndex = 0,zoomBegin=true;
			view.watch("zoom", function(zoom) {
				if(zoomBegin){// 模拟拖动开始
					zoomBegin = false;
					$.fn.ffcsMap.defaults.events.zoom_begin(view);
				}
				clearTimeout(zoomIndex);
				zoomIndex=setTimeout(function() {// 模拟拖动结束
					if (view.zoom - zoom == 0) {
						zoomBegin = true;
						$.fn.ffcsMap.defaults.events.zoom_change(view);
					}
				}, 1000);
			});
		}
	};

	// Geometry相关3D渲染函数
	$.fn.ffcsMap.Geometry = {};
	/*
	 * 三维结构对象：var struc3D = { geometry : {// 几何图形三维 width : 0, height : 0, depth :
	 * 0 }, position : {// 三维坐标位置 x : 0, y : 0, z : 0 }, material : {// 材质
	 * opacity : 0.9, transparent : true, color : '' }, rotate : { x : 0, y : 0,
	 * z : 0 } };
	 */
	/*
	 * 在地图添加3D长方形图形 layerName：图层名称，后续可根据名称清除3D物体
	 * struc3DAry：三维结构数组：[{struc3D},{struc3D},{struc3D}...] backFn：渲染完毕回调函数
	 */
	$.fn.ffcsMap.Geometry.Box = function(layerName, struc3DAry, backFn) {
		if (struc3DAry != null && struc3DAry.length == 0)
			return false;

		var raycaster = new THREE.Raycaster();
		var mouse = new THREE.Vector2();

		function onDocumentMouseDown(event, scene, camera) {
			// 通过鼠标点击的位置计算出raycaster所需要的点的位置，以屏幕中心为原点，值的范围为-1到1.

			mouse.x = (event.clientX / window.innerWidth) * 2 - 1;
			mouse.y = -(event.clientY / window.innerHeight) * 2 + 1;

			// 通过鼠标点的位置和当前相机的矩阵计算出raycaster
			raycaster.setFromCamera(mouse, camera);

			// 获取raycaster直线和所有模型相交的数组集合
			var intersects = raycaster.intersectObjects(scene.children);

			// 将所有的相交的模型的颜色设置为红色，如果只需要将第一个触发事件，那就数组的第一个模型改变颜色即可
			for (var i = 0; i < intersects.length; i++) {

				// intersects[i].object.material.color.set(0xff0000);

				if (typeof $.fn.ffcsMap.defaults.events.click_3d == "function") {
					$.fn.ffcsMap.defaults.events.click_3d(intersects[i].object);
				}
			}

		}
		var issExternalRenderer = {
			renderer : null, // three.js renderer

			models : [], // ISS model

			// struc3DAry : struc3DAry,

			// Setup function, called once by the ArcGIS JS API.
			setup : function(context) {
				// initialize the three.js renderer
				// ////////////////////////////////////////////////////////////////////////////////////
				this.renderer = new THREE.WebGLRenderer({
					context : context.gl,
					premultipliedAlpha : false
				});
				this.renderer.setPixelRatio(window.devicePixelRatio);
				this.renderer.setViewport(0, 0, $.fn.ffcsMap.defaults.width, $.fn.ffcsMap.defaults.height);

				// prevent three.js from clearing the buffers provided by the
				// ArcGIS JS API.
				this.renderer.autoClearDepth = false;
				this.renderer.autoClearStencil = false;
				this.renderer.autoClearColor = false;

				// The ArcGIS JS API renders to custom offscreen buffers, and
				// not to the default framebuffers.
				// We have to inject this bit of code into the three.js runtime
				// in order for it to bind those
				// buffers instead of the default ones.
				var originalSetRenderTarget = this.renderer.setRenderTarget.bind(this.renderer);
				this.renderer.setRenderTarget = function(target) {
					originalSetRenderTarget(target);
					if (target == null) {
						context.bindRenderTarget();
					}
				};

				for (i = 0; i < struc3DAry.length; i++) {
					var struc3D = struc3DAry[i];
					// 盒子几何图形类，构造：长宽高
					var cubeGeometry = new THREE.BoxGeometry(struc3D.geometry.width, struc3D.geometry.height, struc3D.geometry.depth);
					cubeGeometry.translate(0, 0, struc3D.geometry.depth / 2);
					cubeGeometry.rotateX(struc3D.rotate.x);
					cubeGeometry.rotateY(struc3D.rotate.y);
					cubeGeometry.rotateZ(struc3D.rotate.z);
					// 一种材质
					var boxMaterial = new THREE.MeshLambertMaterial({
						opacity : struc3D.material.opacity,
						transparent : struc3D.material.transparent,
						color : struc3D.material.color
					});
					// 物体+材质=模型
					var cube = new THREE.Mesh(cubeGeometry, boxMaterial);

					// this.models[i] = cube;
					gScene.add(cube);

					var posEst = [ struc3D.position.x, struc3D.position.y, struc3D.position.z ];

					var renderPos = [ 0, 0, 0 ];

					// 将位置从给定的空间引用转换为内部渲染坐标系。允许的输入空间参考是有限的，取决于查看模式
					var spatialReference = new gSpatialReference($.fn.ffcsMap.defaults.arcgisConfigInfo.wkid);

					gExternalRenderers.toRenderCoordinates(view, posEst, 0, spatialReference, renderPos, 0, 1);
					cube.position.set(renderPos[0], renderPos[1], renderPos[2]);

					// for the region, we position a torus slightly under ground
					// the torus also needs to be rotated to lie flat on the
					// ground
					// posEst = [ posEst[0], posEst[1], posEst[2] ];

					var transform = new THREE.Matrix4();
					transform.fromArray(gExternalRenderers.renderCoordinateTransformAt(view, posEst, spatialReference, new Array(16)));
					transform.decompose(cube.position, cube.quaternion, cube.scale);

				}
				_doc.addEventListener('mousedown', function(event) {
					onDocumentMouseDown(event, gScene, gCamera);
				}, false);
				context.resetWebGLState();
			},

			render : function(context) {
				// update camera parameters
				// /////////////////////////////////////////////////////////////////////////////////
				var cam = context.camera;

				gCamera.position.set(cam.eye[0], cam.eye[1], cam.eye[2]);
				gCamera.up.set(cam.up[0], cam.up[1], cam.up[2]);
				gCamera.lookAt(new THREE.Vector3(cam.center[0], cam.center[1], cam.center[2]));

				// Projection matrix can be copied directly
				gCamera.projectionMatrix.fromArray(cam.projectionMatrix);

				// update ISS and region position
				// /////////////////////////////////////////////////////////////////////////////////

				// update lighting
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// view.environment.lighting.date = Date.now(); // 关闭时间等于关闭光源
				/*
				 * var l = context.sunLight;
				 * this.sun.position.set(l.direction[0], l.direction[1],
				 * l.direction[2]); this.sun.intensity = l.diffuse.intensity;
				 * this.sun.color = new THREE.Color(l.diffuse.color[0],
				 * l.diffuse.color[1], l.diffuse.color[2]);
				 * 
				 * this.ambient.intensity = l.ambient.intensity;
				 * this.ambient.color = new THREE.Color(l.ambient.color[0],
				 * l.ambient.color[1], l.ambient.color[2]);
				 */
				// draw the scene
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				this.renderer.resetGLState();
				this.renderer.render(gScene, gCamera);

				// as we want to smoothly animate the ISS movement, immediately
				// request a re-render
				gExternalRenderers.requestRender(view);

				// cleanup
				context.resetWebGLState();
			}
		};
		gExternalRenderers.add(view, issExternalRenderer);
		// 全局缓存Renderer
		gIssRenderers.push({
			layerName : layerName,
			modelType : "Box",
			issRenderer : issExternalRenderer
		});
	};

	// 删除3D物体
	$.fn.ffcsMap.Geometry.RemoveBox = function(layerName) {
		if (gIssRenderers && gIssRenderers.length > 0) {
			$.each(gIssRenderers, function(i, obj) {
				if (obj && obj.modelType == "Box" && obj.layerName == layerName) {
					gExternalRenderers.remove(view, obj.issRenderer);
					gIssRenderers.splice(i, 1);
				}
			});
		}
	};
	/*
	 * 三维结构对象：var strucText = { position : {// 坐标 x : 0, y : 0, z : 0 }, font :
	 * {// 字体属性 size : 14, height : 1, color : 0xffffff, bevelEnabled : false,
	 * bevelSize : 1 }, rotate : { x : 0, y : 0, z : 0 } };
	 */
	/*
	 * 在地图添加3D文字图形 layerName：图层名称，后续可根据名称清除3D物体
	 * struc3DAry：三维结构数组：[{strucText},{strucText},{strucText}...]
	 * backFn：渲染完毕回调函数
	 */
	$.fn.ffcsMap.Geometry.Text = function(layerName, struc3DAry, backFn) {
		if (struc3DAry != null && struc3DAry.length == 0)
			return false;
		var strucText = {
			position : {// 坐标
				x : 0,
				y : 0,
				z : 0
			},
			font : {// 字体属性
				size : 14,
				height : 1,
				color : 0xffffff,
				bevelEnabled : false,
				bevelSize : 1,
				text : ""
			},
			rotate : {
				x : 0,
				y : 0,
				z : 0
			}
		};
		var issExternalRenderer = {
			renderer : null, // three.js renderer

			// textModels : [], // ISS model
			// lineModels : [], // ISS model

			// Setup function, called once by the ArcGIS JS API.

			setup : function(context) {
				var me = this;
				// initialize the three.js renderer
				// ////////////////////////////////////////////////////////////////////////////////////
				this.renderer = new THREE.WebGLRenderer({
					context : context.gl,
					premultipliedAlpha : false
				});
				this.renderer.setPixelRatio(window.devicePixelRatio);
				this.renderer.setViewport(0, 0, $.fn.ffcsMap.defaults.width, $.fn.ffcsMap.defaults.height);

				// prevent three.js from clearing the buffers provided by the
				// ArcGIS JS API.
				this.renderer.autoClearDepth = false;
				this.renderer.autoClearStencil = false;
				this.renderer.autoClearColor = false;

				// The ArcGIS JS API renders to custom offscreen buffers, and
				// not to the default framebuffers.
				// We have to inject this bit of code into the three.js runtime
				// in order for it to bind those
				// buffers instead of the default ones.
				var originalSetRenderTarget = this.renderer.setRenderTarget.bind(this.renderer);
				this.renderer.setRenderTarget = function(target) {
					originalSetRenderTarget(target);
					if (target == null) {
						context.bindRenderTarget();
					}
				};

				// setup the three.js scene
				// /////////////////////////////////////////////////////////////////////////////////////

				var loader = new THREE.FontLoader();
				loader.load(uiDomain + '/js/map/threejs/examples/fonts/FZXiaoBiaoSong-B05S_Regular.json', function(font) {

					for (i = 0; i < struc3DAry.length; i++) {
						var struc3D = $.extend({}, strucText, struc3DAry[i]);

						var textGeometry = new THREE.TextGeometry(struc3D.font.text, {
							"font" : font,
							"size" : struc3D.font.size,
							"height" : struc3D.font.height,
							"bevelEnabled" : struc3D.font.bevelEnabled,
							"bevelSize" : struc3D.font.bevelSize
						});
						textGeometry.rotateX(struc3D.rotate.x);
						textGeometry.rotateY(struc3D.rotate.y);
						textGeometry.rotateZ(struc3D.rotate.z);
						text = new THREE.Mesh(textGeometry, new THREE.MultiMaterial([ new THREE.MeshPhongMaterial({
							color : struc3D.font.color,
							shading : THREE.FlatShading
						}), new THREE.MeshPhongMaterial({
							color : struc3D.font.color,
							shading : THREE.SmoothShading
						}) ]));

						textGeometry.computeBoundingBox();

						var centerOffset = -0.5 * (textGeometry.boundingBox.max.x - textGeometry.boundingBox.min.x);
						textGeometry.translate(centerOffset, -(struc3D.font.size / 2), 0);

						gScene.add(text);

						var posEst = [ struc3D.position.x, struc3D.position.y, struc3D.position.z ];

						var renderPos = [ 0, 0, 0 ];

						// 将位置从给定的空间引用转换为内部渲染坐标系。允许的输入空间参考是有限的，取决于查看模式
						var spatialReference = new gSpatialReference($.fn.ffcsMap.defaults.arcgisConfigInfo.wkid);

						gExternalRenderers.toRenderCoordinates(view, posEst, 0, spatialReference, renderPos, 0, 1);
						text.position.set(renderPos[0], renderPos[1], renderPos[2]);

						// for the region, we position a torus slightly under
						// ground
						// the torus also needs to be rotated to lie flat on the
						// ground
						// posEst = [ posEst[0], posEst[1], 300 ];

						var transform = new THREE.Matrix4();
						transform.fromArray(gExternalRenderers.renderCoordinateTransformAt(view, posEst, spatialReference, new Array(16)));
						transform.decompose(text.position, text.quaternion, text.scale);
					}
				});

				context.resetWebGLState();
			},

			render : function(context) {
				// update camera parameters
				// /////////////////////////////////////////////////////////////////////////////////
				var cam = context.camera;

				gCamera.position.set(cam.eye[0], cam.eye[1], cam.eye[2]);
				gCamera.up.set(cam.up[0], cam.up[1], cam.up[2]);
				gCamera.lookAt(new THREE.Vector3(cam.center[0], cam.center[1], cam.center[2]));

				// Projection matrix can be copied directly
				gCamera.projectionMatrix.fromArray(cam.projectionMatrix);

				// update ISS and region position
				// /////////////////////////////////////////////////////////////////////////////////

				// update lighting
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				// view.environment.lighting.date = Date.now(); // 关闭时间等于关闭光源
				/*
				 * var l = context.sunLight;
				 * this.sun.position.set(l.direction[0], l.direction[1],
				 * l.direction[2]); this.sun.intensity = l.diffuse.intensity;
				 * this.sun.color = new THREE.Color(l.diffuse.color[0],
				 * l.diffuse.color[1], l.diffuse.color[2]);
				 * 
				 * this.ambient.intensity = l.ambient.intensity;
				 * this.ambient.color = new THREE.Color(l.ambient.color[0],
				 * l.ambient.color[1], l.ambient.color[2]);
				 */
				// draw the scene
				// ///////////////////////////////////////////////////////////////////////////////////////////////////
				this.renderer.resetGLState();
				this.renderer.render(gScene, gCamera);

				// as we want to smoothly animate the ISS movement, immediately
				// request a re-render
				gExternalRenderers.requestRender(view);

				// cleanup
				context.resetWebGLState();
			}
		};
		gExternalRenderers.add(view, issExternalRenderer);
		// 全局缓存Renderer
		gIssRenderers.push({
			layerName : layerName,
			modelType : "Text",
			issRenderer : issExternalRenderer
		});
	};
	// 删除3D文字
	$.fn.ffcsMap.Geometry.RemoveText = function(layerName) {
		if (gIssRenderers && gIssRenderers.length > 0) {
			$.each(gIssRenderers, function(i, obj) {
				if (obj && obj.modelType == "Text" && obj.layerName == layerName) {
					gExternalRenderers.remove(view, obj.issRenderer);
					gIssRenderers.splice(i, 1);
				}
			});
		}
	};

	
	/**
	 * 
	 * 从内存删除scene中的几何体模型数据，释放内存
	 */
	$.fn.ffcsMap.removeCube = function(scene){
		
		// 判断类型
		if(gScene instanceof THREE.Scene){
			 var allChildren = gScene.children;
			for(var i = allChildren.length-1; i>=0; i--){
				 var lastObject = allChildren[i];
				 if(lastObject instanceof THREE.Mesh){
			          gScene.remove(lastObject);    
				}
			}	
		}   
}

	
	
	
	/**
	 * 热力图展现
	 * 
	 * $.fn.ffcsMap.createHeatMap = function(layerName,config,struc3DAry)
	 */
	// Heat相关3D渲染函数
	$.fn.ffcsMap.createHeatMap = {};
	$.fn.ffcsMap.createHeatMap.isClear=true;
	$.fn.ffcsMap.createHeatMap.Box = function(layerName,config,struc3DAry,max) {
		
		var raycaster = new THREE.Raycaster();
		var mouse = new THREE.Vector2();
		var heatMapLayer={
				name:layerName,
	            view: null,
	            heatmap: null,
	            config: null,
	            visible:true,
	            data:null,
	            constructor: function (view,config,data,max) {
	                this.init(view,config,data,max);

	            },
	            init:function(view, config, data,max) {
	                this.setBaseMap(view);
	                this.createDIV();
	                this.defaultConfig();
	                // 更新配置
	                if(config){
	                    this.setConfig(config);
	                }
	                this.createLayer();
	                this.setData(data);
	                this.setMax(max);
	                this.startMapEventListeners();
	            },
	            setBaseMap:function(view) {
	                this.view = view;
	            },
	            defaultConfig:function() {
	                this.config={
	                    container: this.box,
	                    radius: 40,
	                    debug: false,
	                    visible: true,
	                    useLocalMaximum: false,
	                    gradient: {
	                        0.45: "rgb(000,000,255)",
	                        0.55: "rgb(000,255,255)",
	                        0.65: "rgb(000,255,000)",
	                        0.95: "rgb(255,255,000)",
	                        1.00: "rgb(255,000,000)"
	                        	
	                    }
					};
	                // this.heatmap = heatmapFactory.create(this.config);
	            },
	            setConfig:function (config) {
	                this.config={
	                    container: this.box,
	                    radius: config.radius,
	                    maxOpacity: config.maxOpacity,
	                    minOpacity: config.minOpacity,
	                    debug: false,
	                    visible: true,
	                    useLocalMaximum: false,
	                    gradient: config.gradient
	                };
	            },
	            setData:function (points,max) {
	                this.data = points
	                this.setMax(max);
	            },
	            setMax:function (max) {
	            	if(max !== null && max !== undefined && max !== ''){
	            		 this.max = max;
	            	}
	            },
	            setVisible:function(bool) {
	                if (!this.box || this.visible===bool) return;
	                this.box.hidden = !bool;
	                this.visible = bool;
	                bool===true && setCharts();
	            },
	            refreshBegin:function(){
	                this.box.hidden = true;
	            },
	            refreshEnd:function(){
	                this.box.hidden = false;
	            },
	            on:function(eventName, handler, context){
	                this.chart.on(eventName, handler, context);
	            },
	            off:function(eventName, handler, context){
	                this.chart.off(eventName, handler, context);
	            },
	            map_DragStart_Listener : null,
	            map_DragEnd_Listener : null,
	            map_ZoomStart_Listener : null,
	            map_ZoomEnd_Listener : null,
	            map_ExtentChange_Listener : null,
	            map_click_Listener : null,
	            /* 创建HeatMaplayer的容器，添加到map的layers下面 */
	            createDIV:function () {
	                this.box = document.createElement("div");
	                this.box.setAttribute("id",layerName);
	                this.box.style.width =  this.view.width + 'px';
	                this.box.style.height = this.view.height + 'px';
	                this.box.style.position = "absolute";
	                this.box.style.top = 0;
	                this.box.style.left = 0;
	                let parent = document.getElementsByClassName("esri-view-surface")[0];
	                parent.appendChild(this.box);
	            },
				/* 创建HeatMaplayer的容器，添加到map的layers下面 */
	            createLayer:function() {
	                this.heatmap = h337.create(this.config);
	            },
	            /* 转换数据 */
	            convertHeatmapData: function (data) {
	                var heatPluginData = {
	                    max: this.max!=null?this.max: this.MaxValue(data),
	                    data: [] // 空数据
	                };
					var	parsedData = {};
						var extent = $.fn.ffcsMap.getViewExtent(),x=0,y=0;
		                for (var i = 0; i < data.length; i++) {
							x= parseFloat(data[i][0]),y=parseFloat(data[i][1]);
							// 屏幕外不渲染
							if( extent.xmax>x  &&  extent.xmin<x &&  extent.ymax >y &&  extent.ymin<y){	
								var screenpoint = this.view.toScreen(new gPoint({
		                            longitude:data[i][0],
		                            latitude:data[i][1],
		                        }));
								
							/*
							 * if(!parsedData[screenpoint.x]){
							 * parsedData[screenpoint.x] = {}; }
							 * if(!parsedData[screenpoint.x][screenpoint.y]){
							 * parsedData[screenpoint.x][screenpoint.y] = 0; }
							 * parsedData[screenpoint.x][screenpoint.y] +=1;
							 * //相同坐标，权重累加
							 */		                        heatPluginData.data.push(
		                            {
		                                x:Math.round(screenpoint.x),
		                                y:Math.round(screenpoint.y),
		                                value: 1
		                            }
		                        )
							}
		                }
					
	             
	                return heatPluginData;
	            },
	            MaxValue:function (data) {
	                // 为设置value,则默认为1
	                var max=1;
	                	 if(data.length >1  && data[1].length==3){
	 	                    for(var i=0;i<data.length;i++){
	 	                        if(max<=data[i][2]){
	 	                            max=data[i][2]
	 	                        }
	 	                    }
	 	                }
	                
	               
	                return max;
	            },
	            addData:function () {
	                let data = this.convertHeatmapData(this.data)
	                this.heatmap.setData(data);
	                this.box.style.position = "absolute";
	            },
	            /* 刷新layer */
	            freshenLayer:function () {
	                this.heatmap.setData({
	                    max: 1,
	                    data: []
	                });
	                this.addData();
	            },
	            /* 清除渲染效果 */
	            clearData: function () {
	                // empty heat map
	               this.heatmap.removeData();
	               this.heatmap.setData( []);
	            },
				/* 销毁实例 */
	            removeLayer:function() {
	                this.clearData();
/*
 * this.box.outerHTML = ""; this.view = null; this.box = null; this.config =
 * null;
 */
	                this.data = [];
	                /*
					 * this.map_DragStart_Listener.remove();
					 * this.map_DragEnd_Listener.remove();
					 * this.map_ZoomStart_Listener.remove();
					 * this.map_ZoomEnd_Listener.remove();
					 * this.map_ExtentChange_Listener.remove();
					 */
	            },
				/* 监听地图事件，根据图层是否显示，判断是否重绘热力图 */
	            startMapEventListeners:function() {
	              /*
					 * let view=this.view;
					 * view.watch("extent",gLang.hitch(this,function () {
					 * if(!this.visible) return; this.freshenLayer();
					 * this.box.hidden = false; }));
					 * view.watch("camera",gLang.hitch(this,function () {
					 * if(!this.visible) return; this.freshenLayer();
					 * this.box.hidden = false; }));
					 */
	            }

	        };

		heatMapLayer.constructor(view,config,struc3DAry,max);
		heatMapLayer.freshenLayer();
		$.fn.ffcsMap.createHeatMap.heatMapLayer=heatMapLayer;
	return heatMapLayer;
	};
	
	
	
	
	
})(jQuery, document, window);

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
