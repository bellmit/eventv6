function MapApiImpl() {
	var me = this;
	me.opt = {
		lon : "119.7904055",
		lat : "25.504242"
	};
	me.layerLevel = new Array();
	//地图初始化
	me.Init = function(targetId, gridId, infoOrgCode, otherOpt) {
		if (typeof otherOpt == "undefined") otherOpt = {};

		var map = MMGlobal.mapObj = new SPGIS.Map(targetId);

		MMGlobal.mapObj.OLMap = map.layerset.map;

		// me.baseLayer = new SPGIS.Layer.BaseLayer("dt");
		// map.addLayer(me.baseLayer);

		map.addControl(new SPGIS.Control.ScaleLine());

		me.showSicMapOfName("pingtandt");
		// 初始化缩放工具
		me.InitZoomBar(targetId, MMGlobal.mapObj.OLMap, otherOpt.zoomBarOffset);
   	    

		var bounds = new SPGIS.Geometry.Bounds(119.784790, 25.499390, 119.796021, 25.509094);
		map.showOfBounds(bounds);
		map.setCenter(new SPGIS.Mapping.LonLat(me.opt.lon, me.opt.lat));
		
		me.gridLayer = new SPGIS.Layer.Vector("gridLayer");
		map.addLayer(me.gridLayer);

		me.kuangXuanLayer = new SPGIS.Layer.Vector("kuangXuanLayer");
		map.addLayer(me.kuangXuanLayer);

		me.buildingLayer = new SPGIS.Layer.Vector("buildingLayer");
		map.addLayer(me.buildingLayer);

		me.gridTextLayer = new SPGIS.Layer.Markers("gridTextLayer");
		map.addLayer(me.gridTextLayer);
		/*
		me.WMSGridLayer = me.getGridLayer();
		map.addLayer(me.WMSGridLayer);
		me.WMSJZLayer = me.getJZLayer();
		map.addLayer(me.WMSJZLayer);
		map.addLayer(me.WMSBuildLayer);
		*/

		me.markers = new SPGIS.Layer.Markers("markers");
		map.addLayer(me.markers);

		me.MAP_ACTION = map.createAction();
		me.setCenter(gridId, infoOrgCode);
		
		$("#_map_tip").removeClass("hide");
		if (typeof otherOpt.openMapClick != "undefined" && otherOpt.openMapClick == true) {
			me.InitMapEvent();
		}
		if (typeof otherOpt.openBoundsChange != "undefined" && otherOpt.openBoundsChange == true) {
			map.addEventListener("zoomend", me.BoundsChangeEvent);
		}
		
	};
	
	me.InitMarkPoint = function(targetId, gridId, x, y, isEdit, callBackFn) {
		var map = MMGlobal.mapObj = new SPGIS.Map(targetId);

		map.addControl(new SPGIS.Control.ScaleLine());
		
		me.showSicMapOfName("pingtanimage");
		
		MMGlobal.mapObj.OLMap = map.layerset.map;

		var bounds = new SPGIS.Geometry.Bounds(119.784790, 25.499390, 119.796021, 25.509094);
		map.showOfBounds(bounds);
		map.setCenter(new SPGIS.Mapping.LonLat(me.opt.lon, me.opt.lat));
		
		me.gridLayer = new SPGIS.Layer.Vector("gridLayer");
		map.addLayer(me.gridLayer);

		me.kuangXuanLayer = new SPGIS.Layer.Vector("kuangXuanLayer");
		map.addLayer(me.kuangXuanLayer);

		me.gridTextLayer = new SPGIS.Layer.Markers("gridTextLayer");
		map.addLayer(me.gridTextLayer);
		
		// me.buildingLayer = new SPGIS.Layer.Markers("buildingLayer");
		// map.addLayer(me.buildingLayer);
		/*
		me.WMSGridLayer = me.getGridLayer();
		map.addLayer(me.WMSGridLayer);
		me.WMSJZLayer = me.getJZLayer();
		map.addLayer(me.WMSJZLayer);
		*/
		me.markers = new SPGIS.Layer.Markers("markers");
		map.addLayer(me.markers);

		// me.MAP_ACTION = map.createAction();
		if (typeof isEdit != "undefined" && (isEdit == true || isEdit == "true")) {
			var click = SPGIS.Control.ClickControl(function(e) {
				me.markers.clearMarkers();
				
				var lonlat = map.getLonLatFromPixel(e.xy).toShortString();
		    	var lon = lonlat.split(",")[0] * 1.0;
		    	var lat = lonlat.split(",")[1] * 1.0;
		    	var size = new SPGIS.Mapping.Size(21, 25);
		    	var offset = new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h);
		    	var icon = new SPGIS.Theme.Icon(MMGlobal.ContextPath + '/js/map/spgis/lib/img/marker-gold.png', size, offset);
				var markerLonLat = new SPGIS.Mapping.LonLat(lon, lat);
				var feature = new SPGIS.FeatureOrdinary(me.markers, markerLonLat);
				feature.closeBox = false;
				feature.data.overflow = "hidden";
				feature.data.icon = icon;
				var marker = feature.createMarker();
				me.markers.addMarker(marker);
				
				if (typeof callBackFn == "function") {
					callBackFn.call(this, lon, lat, MMGlobal.MapType);
				}
			});
			
			map.addControl(click);
			click.activate();
		}
		
		
		var gisGridInfo = me.getGisGridInfo(gridId);
		me.drawGridLine(gisGridInfo, false);
		
		if (typeof x != "undefined" && x != "" && typeof y != "undefined" && y != "") {
	    	var size = new SPGIS.Mapping.Size(21, 25);
	    	var offset = new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h);
	    	var icon = new SPGIS.Theme.Icon(MMGlobal.ContextPath + '/js/map/spgis/lib/img/marker-gold.png', size, offset);
			var markerLonLat = new SPGIS.Mapping.LonLat(x, y);
			var feature = new SPGIS.FeatureOrdinary(me.markers, markerLonLat);
			feature.closeBox = false;
			feature.data.overflow = "hidden";
			feature.data.icon = icon;
			var marker = feature.createMarker();
			me.markers.addMarker(marker);
			MMGlobal.mapObj.setCenter(markerLonLat);
		} else {
			MMGlobal.mapObj.setCenter(new SPGIS.Mapping.LonLat(gisGridInfo.lon, gisGridInfo.lat));
		}
	};

	me.InitMapEvent = function() {
		var gridDS = null;// new SPGIS.Data.DataSource("wanggenew");
		var buildDS = new SPGIS.Data.DataSource("cqjzwnew");
		// 连接数据源
		// gridDS.conn();
		buildDS.conn();
		var _click = SPGIS.Control.ClickControl(function(e) {
			var ds = typeof me["buildingWMSLayer"] == "undefined" ? gridDS : buildDS;
			if (ds == null) return false;
			layer.load(0, { time: 6 * 1000 });
			if (!ds.isSucceed()) {
				Lemon.MSG.MsgPanel.Debug("数据源连接失败！");
				return false;
			}
			var lonlat = MMGlobal.mapObj.getLonLatFromPixel(e.xy).toShortString();
			var point = new SPGIS.Geometry.Point(lonlat.split(",")[0], lonlat.split(",")[1]);
			var p1 = new SPGIS.Geometry.Point((point.x - 0.00000000001), (point.y + 0.00000000001));
			var p2 = new SPGIS.Geometry.Point((point.x + 0.00000000001), (point.y + 0.00000000001));
			var p3 = new SPGIS.Geometry.Point((point.x + 0.00000000001), (point.y - 0.00000000001));
			var p4 = new SPGIS.Geometry.Point((point.x - 0.00000000001), (point.y - 0.00000000001));
			var ring = new SPGIS.Geometry.LinearRing([ p1, p2, p3, p4 ]);
			var polygon = new SPGIS.Geometry.Polygon(ring);
			var param = new SPGIS.Data.PolygonParameter(polygon);
			var query = new SPGIS.Data.QueryData(ds, param);
			query.submit(function(d) {
				layer.closeAll('loading');
				if (d.chains.length != 0) {
					var datas = d.chains;
					if (datas != null && datas.length > 0) {
						var customid = datas[0].customid;
						// alert(customid);
						// Lemon.MSG.MsgPanel.Debug("biz:" + customid);
						if (customid != null && typeof customid != "undefined" && parseInt(customid) > 0) {
							if (typeof me["buildingWMSLayer"] == "undefined") {
								if (BIZ_IDS.GRID_IDS.indexOf(customid + ",") != -1) {
									var gridId = customid;
									var lonLat = MMGlobal.mapObj.getLonLatFromPixel(e.xy);
									var opt = me.getGridCard(gridId);
									me.openInfoWindow(lonLat, opt, 0);
								} else {
									MapTipShow("该网格不在管辖范围内，无权查看！");
								}
							} else {
								if (BIZ_IDS.BUILDING_IDS.indexOf(customid + ",") != -1) {
									var buildingId = customid;
									var lonLat = MMGlobal.mapObj.getLonLatFromPixel(e.xy);
									var opt = me.getBuildingCard(buildingId);
									me.openInfoWindow(lonLat, opt, 0);
								} else {
									MapTipShow("该楼宇不在管辖范围内，无权查看！");
								}
							}
						} else {
							MapTipShow("未绑定轮廓！");
						}
					}
				}
			});
		});
		MMGlobal.mapObj.addControl(_click);
		_click.activate();
	};

	me.InitZoomBar = function(mapId, olMap, offsetPixel) {
		if (typeof offsetPixel == "undefined") {
			offsetPixel = new SPGIS.Mapping.Pixel(7, 20);
		}
		olMap.addControl(new SPGIS.Control.PanZoomBar(), offsetPixel);
		var top = 80 + 2 * offsetPixel.y;
		var left = 44 + 2 * offsetPixel.x;
		var divHtml = '<div class="arrowbox" gridLevel="6" style="top:'+top+'px;left:'+left+'px;"><div class="arrowico"></div>网格</div>\
						<div class="arrowbox" gridLevel="5" style="top:'+(top + 23)+'px;left:'+left+'px;"><div class="arrowico"></div>社区</div>\
						<div class="arrowbox" gridLevel="4" style="top:'+(top + 45)+'px;left:'+left+'px;"><div class="arrowico"></div>乡镇</div>';
		$("#" + mapId).parent().append(divHtml);
		$(".arrowbox").click(function() {
			var zoom = me.getZoomByGridLevel($(this).attr("gridLevel"));
			if (zoom >= 0) {
				MMGlobal.mapObj.zoomTo(zoom);
				me.BoundsChangeEvent();
			}
		});
	};

	me.BoundsChangeEvent = function(type) {
		try {
			var showLevel = parseInt($('input[name="gridLevelName"]:checked ').val());
			var zoom = MMGlobal.mapObj.OLMap.getZoom();
			if (type != "ignore") {
				var loadLevel = me.getNeedLoadLevel(zoom);
				var curGridLevel = parseInt($("#gridLevel").val());
				if (loadLevel != showLevel && loadLevel >= curGridLevel) {
					// 函数在：function_version.js
					changeCheckedAndStatus(curGridLevel, loadLevel);
					// 函数在：首页
					getArcgisDataOfGridsByLevel(loadLevel);
					showLevel = parseInt($('input[name="gridLevelName"]:checked ').val());
				}
			}
			if (!isNaN(showLevel)) {
				var ratio = 10;
				if (showLevel == 2) { // 市
					ratio = 0;
				} else if (showLevel == 3) { // 区县
					ratio = 0;
				} else if (showLevel == 4) { // 乡镇
					ratio = 1;
				} else if (showLevel == 5) { // 社区
					ratio = 2;
				} else if (showLevel == 6) { // 网格
					ratio = 3;
				}
				if (zoom < ratio) {
					me.gridTextLayer.layer.setOpacity(0);
				} else {
					me.gridTextLayer.layer.setOpacity(1);
				}
			}
		} catch (e) {}
	};

	me.getNeedLoadLevel = function(zoom) {
		if (zoom == 0) {
			return 2;// 市
		} else if (zoom == 1 || zoom == 2) {
			return 4;// 乡镇
		} else if (zoom == 3 || zoom == 4) {
			return 5;// 社区
		} else if (zoom >= 5) {
			return 6;// 网格
		} 
	};

	me.getZoomByGridLevel = function(gridLevel) {
		if (gridLevel) {
			switch (parseInt(gridLevel)) {
			case 2:// 市 2
				return 0;
			case 4:// 街道 4
				return 1;
			case 5:// 社区 5
				return 3;
			case 6:// 网格 6
				return 5;
			}
		}
		return -1;
	};

	me.showSicMapOfName = function(layerName) {
		/*var layers = MMGlobal.mapObj.OLMap.getLayersByName("wangge");
		if (layers.length > 0) {
			for (var i = 0; i < layers.length; i++) {
				var index = MMGlobal.mapObj.OLMap.getLayerIndex(layers[i]);
				console.log("WANGGE图层的LayerIndex：" + index);//WANGGE
			}
		}*/
		
		MMGlobal.mapObj.showSicMapOfName(layerName);
		/*var layer = MMGlobal.mapObj.OLMap.getLayersByName(layerName);
		MMGlobal.mapObj.OLMap.setLayerIndex(layer, 1);
		MMGlobal.mapObj.OLMap.setBaseLayer(layer);*/
	};
	
	me.getGridLayer = function() {
		// wms图层参数
		var params = {
			layers : 'wangge',
			transparent : true
		};
		// wms选项
		var options = {
			buffer : 0,
			displayOutsideMaxExtent : true,
			isBaseLayer : false,
			opacity : 1
		};
		// 创建wms图层
		var WMSlayer = new SPGIS.Layer.WMS("wangge", "http://"+MM_SPGIS_IP+":8080/spgis/jack/wms", params, options);
		return WMSlayer;
	};

	me.getJZLayer = function() {
		// wms图层参数
		var params = {
			layers : 'jzpoint',
			transparent : true
		};
		// wms选项
		var options = {
			buffer : 0,
			displayOutsideMaxExtent : true,
			isBaseLayer : false,
			opacity : 1
		};
		// 创建wms图层
		var WMSlayer = new SPGIS.Layer.WMS("jzpoint", "http://"+MM_SPGIS_IP+":8080/spgis/jack/wms", params, options);
		return WMSlayer;
	};
	
	me.getBuildLayer = function() {
		// wms图层参数
		var params = {
			layers : 'CQJZW',
			transparent : true
		};
		// wms选项
		var options = {
			buffer : 0,
			displayOutsideMaxExtent : true,
			isBaseLayer : false,
			opacity : 1
		};
		// 创建wms图层
		var WMSlayer = new SPGIS.Layer.WMS("CQJZW", "http://"+MM_SPGIS_IP+":8080/spgis/jack/wms", params, options);
		return WMSlayer;
	};

	// 左上角网格回调函数
	me.setCenter = function(gridId, infoOrgCode) {
		var gisGridInfo = me.getGisGridInfo(gridId, infoOrgCode);
		var lonlat = new SPGIS.Mapping.LonLat(gisGridInfo.lon, gisGridInfo.lat);
		MMGlobal.mapObj.setCenter(lonlat);
		MMGlobal.mapObj.zoomTo(parseInt(MMGlobal.CurrGridInfo.level));
		var showLevel = $('input[name="gridLevelName"]:checked ').val();
		me.getArcgisDataOfGrids(gridId, infoOrgCode, MMGlobal.MapType, showLevel);
	};

	me.startKuangXuan = function(callBack) {	
		me.MAP_ACTION.createPolygon(me.kuangXuanLayer, function(e) {
			me.polygonFeature = e.feature;
			if (typeof callBack != "undefined") {
				var rings = e.feature.geometry.components;
				var ringPoints = rings.components;
				var ary = new Array();
				for (var i = 0; i < rings.length; i++) {
					var ringPoints = rings[i].components;
					for (var j = 0; j < ringPoints.length; j++) {
						ary.push(ringPoints[j].x + "," + ringPoints[j].y);
					}
				}
				callBack.call(this, ary.join(','));
			}
		});
	};

	me.renewKuangxuan = function(callBack) {
		if (typeof me.polygonFeature != "undefined") {
			me.kuangXuanLayer.removeFeatures(me.polygonFeature);
			me.polygonFeature = null;
		}
		me.startKuangXuan(callBack);
	};

	me.openKuangxuan = function() {
		me.kuangXuanLayer.layer.setVisibility(true);
	};

	me.closeKuangxuan = function() {
		me.kuangXuanLayer.layer.setVisibility(false);
	};

	me.transGisGridInfo = function(obj) {
		var gisGridInfo = {
			lon : me.opt.lon,
			lat : me.opt.lat,
			level : "3",
			lineColor : "",
			lineWidth : "",
			areaColor : "",
			nameColor : "",
			gridId : "",
			infoOrgCode : "",
			hs : "",
			gridName : ""
		};
		if (typeof obj != "undefined") {
			if (obj.x != null && obj.x != "" && obj.y != null && obj.y != "") {
				gisGridInfo.lon = obj.x;
				gisGridInfo.lat = obj.y;
			}
			if (obj.mapCenterLevel) {
				gisGridInfo.level = obj.mapCenterLevel > 7 ? 7 : obj.mapCenterLevel;
			}
			gisGridInfo.lineColor = (obj.lineColor ? obj.lineColor : "#ff0000");
			gisGridInfo.lineWidth = "2";// (obj.lineWidth ? obj.lineWidth : "2");
			gisGridInfo.areaColor = (obj.areaColor ? obj.areaColor : "#ff0000");
			gisGridInfo.nameColor = (obj.nameColor ? obj.nameColor : "#ff0000");
			gisGridInfo.gridId = (obj.wid ? obj.wid + "" : "");
			gisGridInfo.infoOrgCode = (obj.infoOrgCode ? obj.infoOrgCode : "");
			gisGridInfo.hs = (obj.hs ? obj.hs : "");
			gisGridInfo.gridName = (obj.gridName ? obj.gridName : "");
		}
		return gisGridInfo;
	};

	// 获取当前网格相关GIS信息
	me.getGisGridInfo = function(gridId, infoOrgCode) {
		me.clearMap("grid");
		var url =  MMGlobal.ContextPath +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json?mapt='+MMGlobal.MapType+'&wid='+gridId+'&t='+Math.random();
		$.ajax({   
			 url: url,
			 type: 'POST',
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','系统无法获取中心点以及显示层级！','warning');
			 },
			 success: function(data){
			    var list = data.list;
			    if (list != null && list.length > 0) {
			    	var obj = list[0];
			    	MMGlobal.CurrGridInfo = me.transGisGridInfo(obj);
			    } else {
			    	MMGlobal.CurrGridInfo = me.transGisGridInfo(obj);
			    	MMGlobal.CurrGridInfo.level = me.gridLevelToMapLevel(infoOrgCode);
			    }
			 }
		});
		return MMGlobal.CurrGridInfo;
	};
	// 获取网格信息
	me.getArcgisDataOfGrids = function(gridId, gridCode, mapt, gridLevel) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			me.clearMap("grid");
			var url =  MMGlobal.ContextPath +'/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrids.json?mapt='+mapt+'&gridId='+gridId+'&gridCode='+gridCode+'&gridLevel='+gridLevel+'&t='+Math.random();
			me.excuteAjax(url, {}, arguments);
		} else {
			$.each(datas, function(i, data) {
				var gisGridInfo = me.transGisGridInfo(data);
		    	me.drawGridLine(gisGridInfo);
			});
			me.BoundsChangeEvent("ignore");
			// MMGlobal.mapObj.setFitView();	// 适应视野
		}
	};
	
	// 画网格框， 网格轮廓：2 楼宇轮廓：4 线：6 标注：8
	me.drawGridLine = function(gisGridInfo, isClick) {
		if (gisGridInfo.hs && gisGridInfo.hs.length > 0) {
			var polygons = new Array();// 多边形的经纬度坐标数组
			var lonLats = gisGridInfo.hs.split(",");
			for (var i = 0; i < lonLats.length; i+=2) {
				polygons.push(new SPGIS.Geometry.Point(lonLats[i], lonLats[i + 1]));
			}
			var gridPolygon = new SPGIS.Geometry.Polygon(new SPGIS.Geometry.LinearRing(polygons));
			var gridFeature = new SPGIS.Feature(gridPolygon);
			var style = {
				strokeOpacity : 1,
				strokeColor : gisGridInfo.lineColor,
				strokeWidth : gisGridInfo.lineWidth,
				strokeLinecap : "round",
				pointRadius : 4,
				pointerEvents : "visiblePainted",
				fillOpacity : MMGlobal.fillOpacity,
				fillColor : gisGridInfo.areaColor,
				graphicOpacity : 0.1
			};
			gridFeature.style = style;
			me.gridLayer.addFeatures(gridFeature);
			var _offset_x = 40;
			if (gisGridInfo.gridName != null && gisGridInfo.gridName != "") {
				_offset_x = gisGridInfo.gridName.gblen() * (20 / 2) / 2;
			}
			var offset = new SPGIS.Mapping.Pixel(-_offset_x, -10);
			var icon = new OpenLayers.Icon("", null, offset, null, 
				'<div title="'+gisGridInfo.gridName+'" style="color: '+gisGridInfo.nameColor+';font-size:20px; text-shadow: 0 0 5px #fff, 0 0 10px #fff, 0 0 15px #fff, 0 0 40px #ff00de, 0 0 70px #ff00de; display: block; white-space: nowrap; cursor: pointer;">'+gisGridInfo.gridName+'</div>');
			// var popclass = OpenLayers.Class(OpenLayers.Popup.Anchored, { 'autoSize' : false });
			var markerLonLat = new SPGIS.Mapping.LonLat(gisGridInfo.lon, gisGridInfo.lat);
			var feature = new SPGIS.FeatureOrdinary(me.markers, markerLonLat);
			feature.closeBox = false;
			// feature.popupClass = popclass;
			feature.data.overflow = "hidden";
			feature.data.icon = icon;
			var marker = feature.createMarker();
			marker.id = "grid_" + gisGridInfo.infoOrgCode + "_text_" + gisGridInfo.gridId;
			if (typeof isClick == "undefined" || isClick != false) {
				marker.events.register("click", feature, function(evt) {
					var opt = me.getGridCard(gisGridInfo.gridId);
					me.openInfoWindow(markerLonLat, opt, 0);
					OpenLayers.Event.stop(evt);
				});
			}
			
			me.gridTextLayer.addMarker(marker);

			return gridPolygon;
		}
		return null;
	};

	// 获取楼宇信息
	me.drawBuildingLine = function(gridId, infoOrgCode) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			me.clearBuildLine();
			var url =  MMGlobal.ContextPath +'/zhsq/map/arcgis/arcgisdataofbuilding/getLocateDataListOfBuilding.json?mapt='+MMGlobal.MapType+'&gridId='+gridId+'&infoOrgCode='+infoOrgCode+'&t='+Math.random();
			me.excuteAjax(url, {}, arguments);
		} else {
			me.buildingLayer.layer.events.on({
				'featureclick': function(layer) {
					var feature = layer.feature;
					var buildingId = feature.wid;
					var lonLat = feature.lonLat;
					var opt = me.getBuildingCard(buildingId);
					me.openInfoWindow(lonLat, opt, 0);
				}
			});
			$.each(datas, function(i, gisData) {
		    	me.drawBuildingLineEx(me.buildingLayer, gisData);
			});
		}
	};

	me.drawBuildingLineEx = function(layer, gisInfo, isClick) {
		if (gisInfo.hs && gisInfo.hs.length > 0) {
			var polygons = new Array();// 多边形的经纬度坐标数组
			var lonLats = gisInfo.hs.split(",");
			for (var i = 0; i < lonLats.length; i+=2) {
				polygons.push(new SPGIS.Geometry.Point(lonLats[i], lonLats[i + 1]));
			}
			var polygon = new SPGIS.Geometry.Polygon(new SPGIS.Geometry.LinearRing(polygons));
			var feature = new SPGIS.Feature(polygon);
			var style = {
				strokeOpacity : 1,
				strokeColor : '#6197D3',
				strokeWidth : '2',
				strokeLinecap : "round",
				pointRadius : 4,
				pointerEvents : "visiblePainted",
				fillOpacity : 0.3,
				fillColor : '#6197D3',
				graphicOpacity : 0.1
			};
			feature.style = style;
			feature.wid = gisInfo.wid;
			feature.lonLat = new SPGIS.Mapping.LonLat(gisInfo.x, gisInfo.y);
			layer.addFeatures(feature);

			return feature;
		}
		return null;
	};

	// 网格层级转换成地图缩放级别
	me.gridLevelToMapLevel = function(infoOrgCode) {
		if (infoOrgCode) {
			switch (infoOrgCode.length) {
			case 2:// 省 1
				return 1;
			case 4:// 市 2
				return 10;
			case 6:// 区 3
				return 11;
			case 9:// 街道 4
				return 5;
			case 12:// 社区 5
				return 13;
			case 15:// 网格 6
				return 13;
			}
		}
		return 5;
	};

	me.clearBuildLine = function() {
		if (me.buildingWMSLayer) {
			MMGlobal.mapObj.OLMap.removeLayer(me.buildingWMSLayer.layer);
			delete me["buildingWMSLayer"];
		}
		me.buildingLayer.layer.events.clearMouseListener();
		me.buildingLayer.removeAllFeatures();
	};

	// 清空："grid"网格级别清空
	me.clearMap = function(busiCode, ctlType) {
		if (MMGlobal.mapObj) {
			me.closeInfoWindow();
			if (busiCode == "grid" && (typeof ctlType == "undefined" || ctlType == null || ctlType == "")) {// 网格级
				me.gridLayer.removeAllFeatures();
				// me.clearBuildLine();
				me.gridTextLayer.clearMarkers();
			} else {
				if (busiCode == "buildingWMSLayer") {
					me.clearBuildLine();
				} else if (typeof me[busiCode] != "undefined" && typeof me[busiCode] != null) {
					me[busiCode].clearMarkers();
				} else {
					if (typeof busiCode == "undefined" || busiCode == "busi") {
						busiCode = "busi";// 业务级
						ctlType = "start";
					} else if (typeof ctlType == "undefined") {
						ctlType = "end";
					}
					var allMarkers = me.markers.layer.markers;
					me.clearMapEx(me.markers, allMarkers, ctlType, busiCode);
					/*
					overlays = MMGlobal.mapObj.getAllOverlays("polyline");
					me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
					
					overlays = MMGlobal.mapObj.getAllOverlays("polygon");
					me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
					
					overlays = MMGlobal.mapObj.getAllOverlays("circle");
					me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
					*/
				}
			}
		}
	};

	me.clearMapEx = function(optObj, overlays, ctlType, busiCode) {
		if (optObj && (overlays && overlays.length > 0)) {
			var _temps = new Array();
			for (var i = 0; i < overlays.length; i++) {
				if (overlays[i].id) {
					var _id = overlays[i].id + "";
					if (ctlType == "start") {
						if (_id.startWith(busiCode)) {
							_temps.push(overlays[i]);
						}
					} else {
						if (_id.endWith(busiCode)) {
							_temps.push(overlays[i]);
						}
					}
					/*if (!_id.startWith("grid")) {
						
					}*/
				}
			}
			for (var i = 0; i < _temps.length; i++) {
				optObj.removeMarker(_temps[i]);
			}
		}
	};

	// 标注图标：	opt.id、opt.icon、opt.lon、opt.lat、opt.w、opt.h、opt.url、opt.name、
//				opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
	me.markerIcon = function(layerObj, opt, layerName, size) {
		if (typeof size == "undefined") {
			size = new SPGIS.Mapping.Size(30, 39);
		}
		opt.iconSize = size;
		// var popclass = OpenLayers.Class(OpenLayers.Popup.Anchored, { 'autoSize' : false });
		var markerLonLat = new SPGIS.Mapping.LonLat(opt.lon, opt.lat);
		var feature = new SPGIS.FeatureOrdinary(me.markers, markerLonLat);
		feature.closeBox = false;
		// feature.popupClass = popclass;
		feature.data.overflow = "hidden";
		feature.data.icon = new OpenLayers.Icon(opt.icon, size, new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h));
		feature.data.id = opt.id;
		var marker = feature.createMarker();
		marker.events.element.title = opt.name;
		marker.id = opt.id;
		marker.opt = opt;
		marker.events.register("click", feature, function(evt) {
			// 这里不能用markerLonLat，该markerLonLat精度被缩减过
			var tMarker = me.getOverlaysById("busi_" + opt.lon + "_" + opt.lat + "_text_" + layerName, layerObj);
			if (tMarker) {
				var ids = tMarker._ids.split("'");
				var names = tMarker._names.split("'");
				var htmlStr = "";
				htmlStr += '<div class="menuNav" style="width:100%;height:223px;">';
				htmlStr += '<h2><span class="fr close" onclick="MMApi.closeInfoWindow();"></span></h2>';
				htmlStr += '<div class="navlist" id="SNmenuNav">';
				htmlStr += '<dl>';
				for (var i = 0; i < ids.length; i++) {
					htmlStr += '<dt class="icon04"><a href="javascript:void(0);" onclick=\'MMApi.clickExistOverlayById("'+ids[i]+'", "'+opt.lon+'", "'+opt.lat+'", "'+layerName+'");\' title="'+names[i]+'">'+names[i]+'</a></dt>';
				}
				htmlStr += '</dl>';
				htmlStr += '</div>';
				htmlStr += '</div>';
				me.popup = new OpenLayers.Popup("popup_" + opt.id, 
					new OpenLayers.LonLat(opt.lon, opt.lat), 
					new OpenLayers.Size(205, 225), 
					new OpenLayers.Pixel(5, -5), 
					htmlStr, 
					false);
				me.popup.setBorder("");
				me.popup.setBackgroundColor("");
				MMGlobal.mapObj.OLMap.addPopup(me.popup, true);
			} else {
				me.openInfoWindow(markerLonLat, opt, opt.iconSize.h);
				OpenLayers.Event.stop(evt);
			}
		});
		layerObj.addMarker(marker);
	};

	me.markerIcons = function(opt, ctlType) {
		if (typeof ctlType == "undefined") ctlType = "common";
		if (ctlType == "common") {
			var elementsCollectionStr = opt.ecs;
			if (elementsCollectionStr == "") {
				alert("该图层缺少elementsCollectionStr参数！");
				return;
			}
			var ecsObj = me.getElementsCollectionObj(elementsCollectionStr);
			var layerName = ecsObj["menuLayerName"];
			var menuSummaryWidth = ecsObj["menuSummaryWidth"];
			var menuSummaryHeight = ecsObj["menuSummaryHeight"];
			if (menuSummaryWidth != null && menuSummaryWidth != '' && menuSummaryWidth != "null") {
				opt.w = parseInt(menuSummaryWidth);
			}
			if (menuSummaryHeight != null && menuSummaryHeight != '' && menuSummaryHeight != "null") {
				opt.h = parseInt(menuSummaryHeight);
			}
			me.markerIconsForCommon(opt, layerName);
		} else if (ctlType == "oldEvent") {
			me.markerIconsForOldEvent(opt, "eventLayer");
		} else if (ctlType == "eventLayer" || ctlType == "urgencyEventLayer") {
			me.markerIconsForNewEvent(opt, ctlType);
		} else if (ctlType == "building" || ctlType == "grid" ||  ctlType == "globalEyes") {
			var elementsCollectionStr = opt.ecs;
			if (elementsCollectionStr == "") {
				alert("该图层缺少elementsCollectionStr参数！");
				return;
			}
			var ecsObj = me.getElementsCollectionObj(elementsCollectionStr);
			var menuSummaryWidth = ecsObj["menuSummaryWidth"];
			var menuSummaryHeight = ecsObj["menuSummaryHeight"];
			if (menuSummaryWidth != null && menuSummaryWidth != '' && menuSummaryWidth != "null") {
				opt.w = parseInt(menuSummaryWidth);
			}
			if (menuSummaryHeight != null && menuSummaryHeight != '' && menuSummaryHeight != "null") {
				opt.h = parseInt(menuSummaryHeight);
			}
			var layerName = ecsObj["menuLayerName"];
			var tbName = "", colName = "customid";
			if (ctlType == "building") {
				tbName = "fwlkbz_point";
			} else if (ctlType == "grid") {
				tbName = "gridbz_point";
			} else if (ctlType == "globalEyes") {
				tbName = "p_quanqiuyan";
			}
			me.markerIconsForSPGIS(opt, layerName, tbName, colName);
		} else if (ctlType == "drawText") {
			var elementsCollectionStr = opt.ecs;
			if (elementsCollectionStr == "") {
				alert("该图层缺少elementsCollectionStr参数！");
				return;
			}
			var ecsObj = me.getElementsCollectionObj(elementsCollectionStr);
			var menuSummaryWidth = ecsObj["menuSummaryWidth"];
			var menuSummaryHeight = ecsObj["menuSummaryHeight"];
			if (menuSummaryWidth != null && menuSummaryWidth != '' && menuSummaryWidth != "null") {
				opt.w = parseInt(menuSummaryWidth);
			}
			if (menuSummaryHeight != null && menuSummaryHeight != '' && menuSummaryHeight != "null") {
				opt.h = parseInt(menuSummaryHeight);
			}
			var layerName = ecsObj["menuLayerName"];
			me.markerIconsForText(opt, layerName);
		}
	};
	// 入参：opt：opt.w、opt.h、opt.url、opt.title、opt.gridId
	// 出参：
	me._getMarkerParams = function(opt, data, ecsObj) {
		if (typeof ecsObj == "undefined") ecsObj = me.getElementsCollectionObj(opt.ecs);
		var menuLayerName = ecsObj["menuLayerName"];
		var smallIco = uiDomain + ecsObj["smallIco"];
		var menuName = ecsObj["menuName"];
		var menuSummaryUrl = ecsObj["menuSummaryUrl"];
		var menuDetailUrl = ecsObj["menuDetailUrl"];
		var menuDetailWidth = ecsObj["menuDetailWidth"];
		var menuDetailHeight = ecsObj["menuDetailHeight"];
		
		var markerOpt = {};
		markerOpt.layerName = menuLayerName;
		markerOpt.id = "busi_" + data.wid + "_" + menuLayerName;
		markerOpt.icon = smallIco;
		markerOpt.title = menuName;
		markerOpt.w = opt.w;
		markerOpt.h = opt.h;
		
		if (typeof (data.wid) == "undefined") data.wid = "";
		if (typeof (opt.gridId) == "undefined") opt.gridId = $("#gridId").val();
		
		markerOpt.url = menuSummaryUrl + data.wid;
		if (markerOpt.url.indexOf("gridId=") == -1) {
			markerOpt.url += "&gridId=" + opt.gridId;
		}
		
		if (typeof (data.name) != "undefined") markerOpt.name = data.name;
		if (typeof (data.x) != "undefined" && data.x != null) markerOpt.lon = data.x;
		if (typeof (data.y) != "undefined" && data.y != null) markerOpt.lat = data.y;
		if (menuDetailUrl != '' && menuDetailUrl != undefined && menuDetailUrl != "null") {
			markerOpt.isShowDetail = true;
			markerOpt.detailUrl = menuDetailUrl + data.wid;
			if (markerOpt.detailUrl.indexOf("gridId=") == -1) {
				markerOpt.detailUrl += "&gridId=" + opt.gridId;
			}
			if (typeof menuDetailWidth != "undefined" && parseInt(menuDetailWidth) > 0) markerOpt.width = menuDetailWidth;
			if (typeof menuDetailHeight != "undefined" && parseInt(menuDetailHeight) > 0) markerOpt.height = menuDetailHeight;
		}
		return markerOpt;
	};

	/*
	opt：opt.w、opt.h、opt.url、opt.title
	*/
	me.openInfoWindow = function(lonLat, _opt, iconHeight, isNeedArrow) {
		if (typeof iconHeight == "undefined") iconHeight = 39;
		if (typeof isNeedArrow == "undefined") isNeedArrow = true;
		var opt = $.extend(true, {}, _opt);
		if (lonLat != null && typeof lonLat != "undefined") {
			opt.isArrow = true;
		} else {
			opt.isArrow = false;
			lonLat = new SPGIS.Mapping.LonLat(MMGlobal.CurrGridInfo.lon, MMGlobal.CurrGridInfo.lat);
		}
		opt.isArrow = opt.isArrow && isNeedArrow;
		if (typeof me.popup != "undefined" && me.popup != null) {
			me.popup.destroy();
		}
		me.popup = new OpenLayers.Popup("popup_" + opt.id, 
			new OpenLayers.LonLat(lonLat.lon, lonLat.lat), 
			new OpenLayers.Size(opt.w, opt.h + 51), 
			new OpenLayers.Pixel(-(opt.w / 2 + 5), -(opt.h + 51 + (iconHeight * 2 / 3))), 
			me.getInfoWindowHtml(opt, lonLat), 
			false);
		me.popup.setBorder("");
		me.popup.setBackgroundColor("");
		MMGlobal.mapObj.OLMap.addPopup(me.popup, true);

		var pixel = MMGlobal.mapObj.getPixelFromLonLat(lonLat);
		var currSize = MMGlobal.mapObj.OLMap.getCurrentSize();
		var x1 = (currSize.w - 230) / 2, y1 = currSize.h / 2 + (opt.h / 2) + 25;
		MMGlobal.mapObj.OLMap.pan(-x1 + pixel.x, -y1 + pixel.y);
	};

	me.getInfoWindowHtml = function(opt, lonLat) {
		if (typeof opt.url != "undefined" && opt.url.indexOf('?') == -1) {
			opt.url += '?mapEngineType=006&t=' + Math.random();
		} else {
			opt.url += '&mapEngineType=006&t=' + Math.random();
		}
		opt.content = '<iframe width="100%" height="100%" src="'+opt.url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		
		var w = opt.w;
		var h = opt.h;
		var lon = lonLat.lon;
		var lat = lonLat.lat;
		var isArrow = (opt.isArrow == true) ? true : false;
		var isShowDetail = opt.isShowDetail ? true : false;
		var title = opt.title ? opt.title : "";
		var content = opt.content ? opt.content : "";
		var mystr = "<div id='NorMapOpenDiv' class='NorMapOpenDiv' style='width: "+w+"px; height: "+h+"px;'>";
		if (isArrow) {
			mystr += "<div class='arrow' style='left: " + (opt.w / 2) + "px;'></div>";
		}
		mystr += "<div class='box' style='width:" + opt.w + "px;height:" + opt.h + "px'>";
		mystr += "<div class='title'>";
		mystr += "<span id='close' class='fr close' onclick='MMApi.closeInfoWindow();'></span>";
		// 是否显示详情
		if (isShowDetail) {
			mystr += "<span id='watch' title='查看详情' class='fr watch' onclick='MMApi.showDetail(\""+opt.title+"\", \""+opt.detailUrl+"\", "+opt.width+", "+opt.height+");'></span>";
		}
		mystr += title;
		mystr += "</div>";
		mystr += "<span class='detail'></span>";
		mystr += "<div style='height:" + (opt.h - 26) + "px;' class='o_subst_info'>";
		mystr += content;
		mystr += "</div>";
		mystr += "</div>";
		mystr += "</div>";
		return mystr;
	};

	me.closeInfoWindow = function() {
		if (typeof me.popup != "undefined" && me.popup != null) {
			me.popup.destroy();
		}
		me.popup = null;
	};

	// 单机覆盖物事件
	me.clickOverlayById = function(id, opt, ctlType) {
		if (typeof ctlType == "undefined") ctlType = "common";
		if (ctlType == "common") {
			var ecsObj = me.getElementsCollectionObj(opt.ecs);
			var layerName = ecsObj["menuLayerName"];
			var menuSummaryWidth = ecsObj["menuSummaryWidth"];
			var menuSummaryHeight = ecsObj["menuSummaryHeight"];
			if (menuSummaryWidth != null && menuSummaryWidth != '' && menuSummaryWidth != "null") {
				opt.w = parseInt(menuSummaryWidth);
			}
			if (menuSummaryHeight != null && menuSummaryHeight != '' && menuSummaryHeight != "null") {
				opt.h = parseInt(menuSummaryHeight);
			}
			me.clickOverlayForCommonById(id, opt, layerName);
		} else if (ctlType == "oldEvent") {
			me.clickOverlayForOldEventById(id, opt, "eventLayer");
		} else if (ctlType == "newEvent") {
			me.clickOverlayForNewEventById(id, opt, opt.layerName);
		}
	};

	me.showDetail = function(title, url, width, height) {
		if (url != '' && url != undefined && url != "null") {
			if (url.indexOf('http://') < 0) {
				url = MMGlobal.ContextPath + url;
			}
			if (url.indexOf('?') == -1) {
				url += '?t=' + Math.random();
			} else {
				url += '&t=' + Math.random();
			}
			width = parseInt(width);
			if (isNaN(width) || width <= 0) width = null;
			height = parseInt(height);
			if (isNaN(height) || height <= 0) height = null;
			showMaxJqueryWindow(title, url, width, height);
		}
	};

	me.getElementsCollectionObj = function(elementsCollectionStr) {
		var obj = {};
		if (typeof elementsCollectionStr != "undefined") {
			var ecs = elementsCollectionStr.split(",_,");
			for (var i = 0; i < ecs.length; i++) {
				var e = ecs[i].split("_,_");
				obj[e[0]] = e[1];
			}
		}
		return obj;
	};

	me.markerIconsForCommon = function(opt, layerName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			if ("1" != IS_ACCUMULATION_LAYER) {
				me.clearMap(layerName);
			}
			var url = opt.url + "&mapt=" + MMGlobal.MapType + '&t=' + Math.random();
			me.excuteAjax(url, { "elementsCollectionStr" : opt.ecs }, arguments);
		} else {
			var ecsObj = me.getElementsCollectionObj(opt.ecs);
			$.each(datas, function(i, data) {
				var markerOpt = me._getMarkerParams(opt, data, ecsObj);
				me.markerIcon(me.markers, markerOpt, layerName);
			});
			me.markerIconEx(arguments, me.markers, layerName);
		}
	};

	me.markerIconsForText = function(opt, layerName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			if ("1" != IS_ACCUMULATION_LAYER) {
				me.clearMap(layerName);
			}
			var url = opt.url + "&mapt=" + MMGlobal.MapType + '&t=' + Math.random();
			me.excuteAjax(url, { "elementsCollectionStr" : opt.ecs }, arguments);
		} else {
			var ecsObj = me.getElementsCollectionObj(opt.ecs);
			$.each(datas, function(i, data) {
				var text = "";
				if (typeof opt.getText == "function") {
					text = opt.getText(data);
				}
				var markerOpt = me._getMarkerParams(opt, data, ecsObj);
				me.drawText(me.markers, markerOpt, layerName, text, opt.isClick);
			});
		}
	};

	me.markerIconsForNewEvent = function(opt, layerName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			if ("1" != IS_ACCUMULATION_LAYER) {
				me.clearMap(layerName);
			}
			var url = opt.url + "&mapt=" + MMGlobal.MapType + '&t=' + Math.random();
			me.excuteAjax(url, {}, arguments);
		} else {
			$.each(datas, function(i, data) {
				// 标注图标：	opt.id、opt.icon、opt.lon、opt.lat、opt.w、opt.h、opt.url、opt.name、
				//			opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
				var markerOpt = $.extend(true, {}, opt);
				var arr = data['wid'].split(",");
				var eventId = arr[0];
				var workFlowId = arr[1];
				var instanceId = arr[2];
				var taskId = arr[3];
				var eventType = arr[4];
				markerOpt.id = "busi_" + eventId + "_" + layerName;
				if (layerName == "urgencyEventLayer") {
					markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_red.gif";
				} else {
					markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
				}
				markerOpt.lon = data.x;
				markerOpt.lat = data.y;
				markerOpt.title = "事件";
				markerOpt.name = data.name;
				markerOpt.url =  MMGlobal.ContextPath +'/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=map&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId+'&taskId='+data['taskId'];
				markerOpt.isShowDetail = true;
				markerOpt.detailUrl = "/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType="+eventType+"&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=l&cachenum=" + Math.random();
				markerOpt.width = 900;
				markerOpt.height = 400;
				me.markerIcon(me.markers, markerOpt, layerName, new SPGIS.Mapping.Size(18, 28));
			});
			me.markerIconEx(arguments, me.markers, layerName, new SPGIS.Mapping.Pixel(4, -37));
		}
	};

	me.markerIconsForOldEvent = function(opt, layerName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			if ("1" != IS_ACCUMULATION_LAYER) {
				me.clearMap(layerName);
			}
			var url = opt.url + "&mapt=" + MMGlobal.MapType + '&t=' + Math.random();
			me.excuteAjax(url, {}, arguments);
		} else {
			$.each(datas, function(i, data) {
				// 标注图标：	opt.id、opt.icon、opt.lon、opt.lat、opt.w、opt.h、opt.url、opt.name、
				//			opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
				var markerOpt = $.extend(true, {}, opt);
				markerOpt.id = "busi_" + data.wid + "_" + layerName;
				markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
				markerOpt.lon = data.x;
				markerOpt.lat = data.y;
				markerOpt.title = "事件";
				markerOpt.name = data.name;
				var taskId = data.taskId == null ? "" : data.taskId;
				markerOpt.url = MMGlobal.SQ_ZZGRID_URL + '/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId=' + data.wid + '&taskInfoId=' + taskId;
				me.markerIcon(me.markers, markerOpt, layerName, new SPGIS.Mapping.Size(18, 28));
			});
			me.markerIconEx(arguments, me.markers, layerName, new SPGIS.Mapping.Pixel(4, -37));
		}
	};

	me.markerIconsForSPGIS = function(opt, layerName, tableName, columnName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			if ("1" != IS_ACCUMULATION_LAYER) {
				me.clearMap(layerName);
			}
			var url = opt.url + "&mapt=" + MMGlobal.MapType + '&t=' + Math.random();
			var _args = arguments;
			$.ajax({
				type : "post",
				url : url,
				data : { "elementsCollectionStr" : opt.ecs },
				dataType : "json",
				success : function(datas) {
					var ids = new Array();
					$.each(datas, function(i, data) {
						if (data.wid != null && parseInt(data.wid) > 0) {
							ids.push(data.wid);
						}
					});
					layer.load(0, { time: 6 * 1000 });
					me.queryMarkersData(tableName, columnName, ids, function(srcData) {
						me.excuteAjaxEx(datas, srcData, _args);
						layer.closeAll('loading');
					});
				}
			});
		} else {
			var ecsObj = me.getElementsCollectionObj(opt.ecs);
			$.each(datas, function(i, data) {
				var markerOpt = me._getMarkerParams(opt, data, ecsObj);
				me.markerIcon(me.markers, markerOpt, layerName);
			});
			me.markerIconEx(arguments, me.markers, layerName);
		}
	};

	me.markerIconEx = function(args, layerObj, layerName, offset) {
		if (typeof offset == "undefined") offset = new SPGIS.Mapping.Pixel(8, -46);
		var map1 = args[args.length - 3];
		var map2 = args[args.length - 2];
		var map3 = args[args.length - 1];
		var keys = map1.keys();
		var vals = map1.values();
		var names = map2.values();
		var ids = map3.values();
		for (var i = 0; i < keys.length; i ++) {
			var count = vals[i];
			if (count > 1) {
				var lonlat = keys[i].split("_");
				var size = new OpenLayers.Size(22, 16);
				var icon = new OpenLayers.Icon("", size, offset, null, 
					'<div style="width:22px;height:16px;font-size:11px;line-height:1.2;text-align:center;background-color:#FF4A58;color:#FFFFFF;">'+count+'</div>');
				var marker = new OpenLayers.Marker(new OpenLayers.LonLat(lonlat[0], lonlat[1]), icon);
				marker.id = "busi_" + keys[i] + "_text_" + layerName;
				marker._names = names[i];
				marker._ids = ids[i];
				layerObj.addMarker(marker);
			}
		}
	};

	me.getAjaxData = function(_args) {
		if (typeof _args != "undefined" && _args.length > 4) {
			if (_args[_args.length - 5] == MMGlobal.MD5) {
				return _args[_args.length - 4];
			}
		}
		return null;
	};

	/*
		url:
		data:
		srcData:用其他数据源的经纬度来代替URL返回的数据源的经纬度
			格式：[{
				'wid':11,
				'x':11.11,
				'y':11.11
			}]
		_args:
	*/
	me.excuteAjax = function(url, data, _args) {
		layer.load(0);
		var newDataUrl = "";
		var parameterData = {};
		var urls = url.split('?');
		if (urls.length == 2) {
			var string_a = urls[1];
			var string = string_a.split('&');
			for (var i = 0; i < string.length; i++) {
			    var str = string[i].split('=');
			    parameterData[str[0]] = str[1];
			}
		}
		newDataUrl = urls[0];
		parameterData = $.extend({}, parameterData, data);
		$.ajax({
			type : "post",
			url : newDataUrl,
			data : parameterData,
			dataType : "json",
			error : function() {
				layer.closeAll('loading');
				// $.messager.alert('友情提示','excuteAjax：请求数据出错！', 'warning');
			},
			success : function(datas) {
				layer.closeAll('loading');
				me.excuteAjaxEx(datas, null, _args);
			}
		});
	};

	me.excuteAjaxEx = function(datas, rsMap, _args) {
		if (typeof rsMap != "undefined" && rsMap != null) {
			$.each(datas, function(i, data) {
				data.x = null;
				data.y = null;
			});
			for (var i = datas.length - 1;i >= 0; i--) {
				var xy = rsMap.get(datas[i].wid + "");
				if (xy != null) {
					datas[i].x = xy.x;
					datas[i].y = xy.y;
				} else {
					datas.splice(i, 1);
				}
			}
		}
		if (typeof datas != "undefined" && datas != null) {
			var map1 = new HashMap();
			var map2 = new HashMap();
			var map3 = new HashMap();
			$.each(datas, function(i, data) {
				if (data.x != null && data.x != "" && data.y != null && data.y != "") {
					var val1 = map1.get(data.x + "_" + data.y);
					var val2 = map2.get(data.x + "_" + data.y);
					var val3 = map3.get(data.x + "_" + data.y);
					if (val1 == null) {
						map1.put(data.x + "_" + data.y, 1);
						map2.put(data.x + "_" + data.y, data.name);
						map3.put(data.x + "_" + data.y, data.wid);
					} else {
						map1.put(data.x + "_" + data.y, val1 + 1);
						map2.put(data.x + "_" + data.y, val2 + "'" + data.name);
						map3.put(data.x + "_" + data.y, val3 + "'" + data.wid);
					}
				}
			});
			$.each(datas, function(i, data) {
				var names = map2.get(data.x + "_" + data.y);
				if (names != null) data.name = names;
			});
			var args = new Array();
			for (var i = 0; i < _args.length; i++) {
				args.push("_args[" + i + "]");
			}
			args.push("MMGlobal.MD5");
			args.push("datas");
			args.push("map1");
			args.push("map2");
			args.push("map3");
			eval('(_args.callee(' + args.join(',') + '))');
		}
	};

	me.clickExistOverlayById = function(id, lon, lat, layerName) {
		if (layerName == "eventLayer" || layerName == "urgencyEventLayer") {
			var arr = id.split(",");
			id = arr[0];
		}
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id, me.markers);
		if (marker) {
			var lonLat = new SPGIS.Mapping.LonLat(lon, lat);
			me.openInfoWindow(lonLat, marker.opt);
		}
	};

	me.clickOverlayForCommonById = function(id, opt, layerName) {
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id, me.markers);
		if (marker) {
			me.openInfoWindow(marker.lonlat, marker.opt);
		} else {
			var markerOpt = me._getMarkerParams(opt, { wid : id });
			me.openInfoWindow(null, markerOpt);
		}
	};

	me.clickOverlayForOldEventById = function(id, opt, layerName) {
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id, me.markers);
		if (marker) {
			me.openInfoWindow(marker.lonlat, marker.opt);
		} else {
			var markerOpt = $.extend(true, {}, opt);
			markerOpt.id = "busi_" + id + "_" + layerName;
			markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
			markerOpt.title = "事件";
			var taskId = opt.taskId == null ? "" : opt.taskId;
			markerOpt.url = MMGlobal.SQ_ZZGRID_URL + '/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId=' + id + '&taskInfoId=' + taskId;
			me.openInfoWindow(null, markerOpt);
		}
	};

	me.clickOverlayForNewEventById = function(id, opt, layerName) {
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id, me.markers);
		if (marker) {
			me.openInfoWindow(marker.lonlat, marker.opt, marker.opt.iconSize.h);
		} else {
			var markerOpt = $.extend(true, {}, opt);
			markerOpt.id = "busi_" + id + "_" + layerName;
			me.openInfoWindow(null, markerOpt);
		}
	};

	me.getOverlaysById = function(id, layerObj) {
		if (typeof id == "undefined" || id.length == 0) return null;
		var allMarker = layerObj.layer.markers;
		for (var i = 0; i < allMarker.length; i++) {
			if (id == allMarker[i].id) {
				return allMarker[i];
				break;
			}
		}
		return null;
	};

	me.initMigrateDtMap = function(targetId) {
		// 定位到平潭
		var map = MMGlobal.mapObj = new SPGIS.Map(targetId);
		MMGlobal.mapObj.OLMap = map.layerset.map;
		var bounds = new SPGIS.Geometry.Bounds(119.784790, 25.499390, 119.796021, 25.509094);
		map.showOfBounds(bounds); 
		me.showSicMapOfName("china");
		me.migrationMapLayer = new OpenLayers.Layer.Vector("migrationMapLayer");
		MMGlobal.mapObj.OLMap.addLayer(me.migrationMapLayer);
		me.initMigrateDtMapEx("0");
	};

	// isOutType: 0-省内, 1-省外
	me.initMigrateDtMapEx = function(isOutType) {
		if (isOutType == "0") {
			MMGlobal.mapObj.zoomTo(4);
			var location = new SPGIS.Mapping.LonLat(119.796021, 25.509094);
			MMGlobal.mapObj.setCenter(location);
		} else {
			MMGlobal.mapObj.zoomTo(0);
			var location = new SPGIS.Mapping.LonLat(118.84456975019, 32.553395229787);
			MMGlobal.mapObj.setCenter(location);
		}		
	};

	me.removeAllMigrationLine = function() {
		if (typeof me.migrationMapLayer != "undefined" && me.migrationMapLayer != null) {
			// TODO:销毁
			var features = me.migrationMapLayer.getFeaturesByAttribute("animation", "animation");
			for (var i = 0;i < features.length; i++) {
	    		features[i].e.clearAnimation();
	    	}
			me.migrationMapLayer.removeAllFeatures();
			var layers = MMGlobal.mapObj.OLMap.getLayersByName("mtactionlayer");
			for (var i = 0;i < layers.length; i++) {
				var layer = layers[i];
				if (layer != null && !layer.isBaseLayer) {
					layer.removeAllFeatures();
					if (layer.name != "migrationMapLayer") {
						MMGlobal.mapObj.OLMap.removeLayer(layer);
					}
				}
			}
		}
	};

	me.drawMigrationLine = function(outCityName, inCityName) {
		var migrate = new SPGIS.Theme.MigrateChartByName(outCityName, inCityName, null);
		migrate.getFeature(function(e) {
			// 若想出线动点必须用此方法，参数为map
			e.setAnimation(MMGlobal.mapObj);
			// 图层上添加迁徙线
			var f = e.feature;
			f.attributes = { "animation" : "animation" };
			f.e = e;
			me.migrationMapLayer.addFeatures(f);
		});
	};

	// 热力图
	me.heatMap = function(isOpen) {
		if (typeof isOpen == "undefined") isOpen = true;
		if (isOpen) {
			me.drawHeatMapTimeAxis();
		} else {
			me.destroyHeatMapTimeAxis();
		}
	};
	// 画热力图时间轴
	me.drawHeatMapTimeAxis = function() {
		// 图示
		var heatChart = $(".map_noprint");
		heatChart.show();
		heatChart.find("span").unbind("hover");
		heatChart.find("span").hover(function(e) {
			$(this).find(".legend-item-wrapper").show();
			var em = $(this).find("em");
			em.html(parseInt(me.heatMapLayer.maxpeople * parseFloat(em.attr("ratio")) * 15));
		}, function(e) {
			$(this).find(".legend-item-wrapper").hide();
		});
		// 时间轴
		var timeAxis = $(".heatMapTimeAxis");
		timeAxis.show();
		var xDate = new XDate();
		timeAxis.find(".title").html('<span class="fr" onclick="MMApi.heatMap(true);">刷新</span>' + xDate.toString("yyyy-MM-dd") + "时间刻度（小时）");
		var range = timeAxis.find(".range");
		range.children().remove();
		var currHours = xDate.getHours();
		for (var i = 0; i <= currHours; i++) {
			range.append('<div id="R1" hour="'+i+'" style="width:31px;"><div class="dot"><span></span><em style="width:27px;">'+i+'点</em></div></div>');
		}
		timeAxis.find(".box").width((currHours + 1) * 31 + 20);
		var childrens = range.children();
		childrens.bind("click", function() {
			childrens.removeClass("current1");
			$(this).addClass("current1");
			var hour = $(this).attr("hour");
			me.createHeatMap(xDate, hour);
		});
		childrens.eq(currHours - 1).click();
	};
	// 销毁热力图时间轴
	me.destroyHeatMapTimeAxis = function() {
		$(".heatMapTimeAxis").hide();
		$(".map_noprint").hide();
		// TODO:销毁
		//MMGlobal.mapObj.OLMap.removeLayer(me.heatMapLayer);
		me.heatMapLayer.hide();
		// me.showSicMapOfName("pingtanimage");
		// me.heatMapLayer = null;
	};
	// 创建热力图
	me.createHeatMap = function(xDate, hours) {
		// me.showSicMapOfName("pingtandt");
		var heatMapLayer = null;
		if (typeof me.heatMapLayer == "undefined") {
			heatMapLayer = me.heatMapLayer = new SPGIS.Theme.HeatChart(MMGlobal.mapObj, 'heatMapLayer');
		} else {
			heatMapLayer = me.heatMapLayer;
		}
		heatMapLayer.hide();

		heatMapLayer.cityName = "福州市";
		// 要显示某个时间的热力图
		heatMapLayer.date = xDate.toString("yyyyMMdd");
		// 0-23 表示一天中的时间，如果省略则表示显示全天的热力数据
		heatMapLayer.hour = hours;

		// 颜色梯度区间, 每个值的前一个数字表示因子在当前视野范围内的稠密程度
		heatMapLayer.colorInterval = { 0.1: "rgb(0,0,255)", 0.2: "rgb(0,255,255)", 0.4: "rgb(0,255,0)", 0.8: "yellow", 1.0: "rgb(255,0,0)"};
		// 模糊级数
		heatMapLayer.blur = 20;
		// 模糊半径，弧度
		heatMapLayer.radius = 5;
		layer.load(0, { time: 6 * 1000 });
		try {
			heatMapLayer.show(function() {
				layer.closeAll('loading');
			});
		} catch (e) {
			layer.closeAll('loading');
		}
	};
	
	// 画楼宇轮廓
	me.drawBuildLine = function() {
		me.clearBuildLine();
		me.buildingWMSLayer = me.getBuildLayer();
		MMGlobal.mapObj.addLayer(me.buildingWMSLayer);
		var index = 1;
		var layers = MMGlobal.mapObj.OLMap.getLayersByName("gridLayer");//wangge
		if (layers.length > 0) {
			index = MMGlobal.mapObj.OLMap.getLayerIndex(layers[0]);
		}
		MMGlobal.mapObj.OLMap.setLayerIndex(me.buildingWMSLayer.layer, index);
	};
	/*
		描述：查询高德点位数据
		参数：	tableName 表名 fwlkbz_point.customid
				ids 数组
				callBackFn 回调函数
		返回：{}|Object对象，例：
			{
				"我们业务ID" : new SPGIS.Mapping.LonLat(高德lon, 高德lat)
			}
	*/
	me.queryMarkersData = function(tableName, columnName, ids, callBackFn) {
		var rsMap = new HashMap();
		if (ids != null && ids.length > 0) {
			var str = "{return ";
			var ary = new Array();
			for (var i = 0; i < ids.length; i++) {
				ary.push("this." + columnName + "=='"+ids[i]+"'");
			}
			str += ary.join(" || ");
			str += ";}";
			// 创建数据源，fwlkbz_point：房屋轮廓标注点数据表
			var ds = new SPGIS.Data.DataSource(tableName, function() {
				if (ds.isSucceed()) {
					// 属性查询参数对象，传入的是字段名称和要查询的值
					var param = new SPGIS.Data.AttributeParameter(str);
					// 数据查询功能对象，传入的参数是数据源、查询参数
					var query = new SPGIS.Data.QueryData(ds, param); 
					// 执行查询
					query.submit(function(d) {
						// Lemon.MSG.MsgPanel.Debug(d);
						var datas = d.chains;
						if (datas != null && datas.length > 0) {
							for (var i = 0; i < datas.length; i++) {
								rsMap.put(datas[i][columnName] + "", {
									x : datas[i].geometry.coordinates[0],
									y : datas[i].geometry.coordinates[1]
								});
							}
						}
						if (typeof callBackFn == "function") {
							callBackFn.call(this, rsMap);
						}
					});
				}
			});
			// 连接数据源
			ds.conn();
		} else {
			if (typeof callBackFn == "function") {
				callBackFn.call(this, rsMap);
			}
		}
	};
	
	me.drawText = function(layer, opt, layerName, text, isClick) {
		var offset = new SPGIS.Mapping.Pixel(-10, -49);// -39为div高度，-10为字体一半高度
		var icon = new OpenLayers.Icon("", null, offset, null, text);
		var markerLonLat = new SPGIS.Mapping.LonLat(opt.lon, opt.lat);
		var feature = new SPGIS.FeatureOrdinary(layer, markerLonLat);
		feature.closeBox = false;
		feature.data.overflow = "hidden";
		feature.data.icon = icon;
		var marker = feature.createMarker();
		marker.id = opt.id;
		marker.opt = opt;
		marker.events.register("click", feature, function(evt) {
			if (!(isClick == false)) {
				me.openInfoWindow(markerLonLat, opt, 10, false);
			}
			OpenLayers.Event.stop(evt);
		});
		layer.addMarker(marker);
	};

	me.getBuildingCard = function(buildingId) {
		var opt = {};
		opt.w = 370;
		opt.h = 280;
		opt.title = "楼栋名片";
		opt.url = MMGlobal.ContextPath + "/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId="+buildingId;
		opt.isShowDetail = true;
		opt.detailUrl = MMGlobal.SQ_ZZGRID_URL + "/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId="+buildingId;
		opt.width = 948;
		opt.height = 405;
		return opt;
	};

	me.getGridCard = function(gridId) {
		var opt = {};
		opt.title = "网格";
		opt.w = 362;
		opt.h = 247;
		opt.url = MMGlobal.ContextPath + "/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId=" + gridId + "&t=" + Math.random();
		opt.isShowDetail = true;
		opt.detailUrl = MMGlobal.SQ_ZZGRID_URL + "/zzgl/map/data/gridBase/standardGridDetailSub.jhtml?gridId=" + gridId + "&t=" + Math.random();
		opt.width = 854;
		opt.height = 400;
		return opt;
	};
}
