function MapApiImpl() {
	var me = this;
	//地图初始化
	me.Init = function(targetId, gridId, infoOrgCode) {
		$(".MapTools").css({ "top" : "295px"});
		$('body').append('<div id="_title_tip" style="position:absolute;z-index:250;display:none;white-space:nowrap;padding:3px;border:1px solid #000;font-size:16px;background:#fff;"></div>');
		MMGlobal.TipObj = $("#_title_tip");
		var opt = {
			center : new AMap.LngLat("119.30305419", "26.08059574"),// 设置默认地图中心点
			level : 3,//初始地图缩放级别
			zooms : [ 3, 18 ] //地图缩放级别范围
		};
		MMGlobal.mapObj = new AMap.Map(targetId, opt);		// 加载地图基本控件
		MMGlobal.mapObj.plugin([ "AMap.ToolBar", "AMap.Scale", "AMap.MouseTool" ], function() { //加载工具条插件，工具条包括方向键盘、标尺键盘和自动定位控制
			MMGlobal.toolbar = new AMap.ToolBar({
				offset : new AMap.Pixel (8, 37),
				ruler : true,
				direction : true
			});
			MMGlobal.mapObj.addControl(MMGlobal.toolbar);	// 加载鹰眼 
			MMGlobal.scale = new AMap.Scale();
			MMGlobal.mapObj.addControl(MMGlobal.scale);
			// 鼠标工具
			MMGlobal.mouseTool = new AMap.MouseTool(MMGlobal.mapObj);
		});
		
		me.setCenter(gridId, infoOrgCode);
	};

	// 左上角网格回调函数
	me.setCenter = function(gridId, infoOrgCode) {
		var gisGridInfo = me.getGisGridInfo(gridId, infoOrgCode);
		var lnglat = new AMap.LngLat(gisGridInfo.lng, gisGridInfo.lat);
		MMGlobal.mapObj.setCenter(lnglat);
		MMGlobal.mapObj.setZoom(gisGridInfo.level);
		var showLevel = $('input[name="gridLevelName"]:checked ').val();
		me.getArcgisDataOfGrids(gridId, infoOrgCode, MMGlobal.MapType, showLevel);
	};

	me.startKuangXuan = function(callBack) {	
		MMGlobal.mouseTool.polygon();
		
		AMap.event.addListener(MMGlobal.mouseTool, "draw", function(e) {
			MMGlobal.mouseTool.close();
			MMGlobal.KXPolygon = e.obj;
			if (typeof callBack != "undefined") {
				callBack.call(this, me.getGeoString());
			}
		});
	};

	me.renewKuangxuan = function(callBack) {
		MMGlobal.mouseTool.close(true);
		MMGlobal.KXPolygon = null;
		MMGlobal.mouseTool.polygon();
		AMap.event.addListener(MMGlobal.mouseTool, "draw", function(e) {
			MMGlobal.mouseTool.close();
			MMGlobal.KXPolygon = e.obj;
			if (typeof callBack != "undefined") {
				callBack.call(this, me.getGeoString());
			}
		});
	};

	me.closeKuangxuan = function() {
		MMGlobal.mouseTool.close(true);
		MMGlobal.KXPolygon = null;
		me.clearMap();
	};

	me.getGeoString = function() {
		if (MMGlobal.KXPolygon != null) {
			var ary = new Array();
			var lngLats = MMGlobal.KXPolygon.getPath();
			for (var i = 0; i < lngLats.length; i++) {
				ary.push(lngLats[i].toString());
			}
			return ary.join(',');
		}
		return "";
	};

	me.transGisGridInfo = function(obj) {
		var gisGridInfo = {
			lng : "119.30305419",
			lat : "26.08059574",
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
			gisGridInfo.lng = obj.x;
			gisGridInfo.lat = obj.y;
			gisGridInfo.level = me.gridLevelToMapLevel(obj.infoOrgCode);
			gisGridInfo.lineColor = (obj.lineColor ? obj.lineColor : "");
			gisGridInfo.lineWidth = (obj.lineWidth ? obj.lineWidth : "");
			gisGridInfo.areaColor = (obj.areaColor ? obj.areaColor : "");
			gisGridInfo.nameColor = (obj.nameColor ? obj.nameColor : "");
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
			 timeout: 3000, 
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
			MMGlobal.mapObj.setFitView();	// 适应视野
		}
	};

	// 画网格框， 网格轮廓：2 楼宇轮廓：4 线：6 标注：8
	me.drawGridLine = function(gisGridInfo) {
		if (gisGridInfo.hs && gisGridInfo.hs.length > 0) {
			var marker = new AMap.Marker({
				position : new AMap.LngLat(gisGridInfo.lng, gisGridInfo.lat),
				offset : new AMap.Pixel(-70, -30),
				draggable : false,
				content : '<div style="color: '+gisGridInfo.nameColor+';font-size:25px; display: block; white-space: nowrap; cursor: pointer;">'+gisGridInfo.gridName+'</div>'
			});
			marker.id = "grid_" + gisGridInfo.infoOrgCode + "_text_" + gisGridInfo.gridId;
			marker.gisGridInfo = gisGridInfo;
			MMGlobal.mapObj.add(marker);
			// 绑定事件
			marker.on("click", function(e) {
				var opt = {};
				opt.title = "网格";
				opt.w = 362;
				opt.h = 247;
				opt.url = MMGlobal.ContextPath + "/zhsq/map/arcgis/arcgisdata/getGridInfoDetailOnMap.jhtml?gridId=" + gisGridInfo.gridId + "&t=" + Math.random();
				opt.isShowDetail = true;
				opt.detailUrl = MMGlobal.SQ_ZZGRID_URL + "/zzgl/map/data/gridBase/standardGridDetailSub.jhtml?gridId=" + gisGridInfo.gridId + "&t=" + Math.random();
				opt.width = 854;
				opt.height = 400;
				me.openInfoWindow(e.target.getPosition(), opt);
			});
			
			var polygons = new Array();// 多边形的经纬度坐标数组
			var lngLats = gisGridInfo.hs.split(",");
			for (var i = 0; i < lngLats.length; i+=2) {
				polygons.push(new AMap.LngLat(lngLats[i], lngLats[i + 1]));
			}
			var gridPolygon = new AMap.Polygon({			// 自定义构造MMap.Polygon对象
				path : polygons,						// 面经纬度数组
				strokeColor : gisGridInfo.lineColor,	// 线颜色
				strokeWeight : gisGridInfo.lineWidth,	// 线宽
				fillColor : gisGridInfo.areaColor,		// 填充色
				fillOpacity : 0.1						// 填充透明度
			});
			gridPolygon.id = "grid_" + gisGridInfo.infoOrgCode + "_polygon_" + gisGridInfo.gridId;
			MMGlobal.mapObj.add(gridPolygon);	//加载多边形覆盖物
			return gridPolygon;
		}
		return null;
	};

	// 网格层级转换成地图缩放级别
	me.gridLevelToMapLevel = function(infoOrgCode) {
		if (infoOrgCode) {
			switch (infoOrgCode.length) {
			case 2:// 省 1
				return 9;
			case 4:// 市 2
				return 10;
			case 6:// 区 3
				return 11;
			case 9:// 街道 4
				return 12;
			case 12:// 社区 5
				return 13;
			case 15:// 网格 6
				return 13;
			}
		}
		return 11;
	};

	// 清空："grid"网格级别清空
	me.clearMap = function(busiCode, ctlType) {
		if (MMGlobal.mapObj) {
			MMGlobal.mapObj.clearInfoWindow();
			if (busiCode == "grid") {// 网格级
				MMGlobal.mapObj.clearMap();
			} else {
				if (typeof busiCode == "undefined" || busiCode == "busi") {
					busiCode = "busi";// 业务级
					ctlType = "start";
				} else if (typeof ctlType == "undefined") {
					ctlType = "end";
				}
				var overlays = MMGlobal.mapObj.getAllOverlays("marker");
				me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
				
				overlays = MMGlobal.mapObj.getAllOverlays("polyline");
				me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
				
				overlays = MMGlobal.mapObj.getAllOverlays("polygon");
				me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
				
				overlays = MMGlobal.mapObj.getAllOverlays("circle");
				me.clearMapEx(MMGlobal.mapObj, overlays, ctlType, busiCode);
			}
		}
	};

	me.clearMapEx = function(mapObj, overlays, ctlType, busiCode) {
		if (mapObj && (overlays && overlays.length > 0)) {
			var _temps = new Array();
			for (var i = 0; i < overlays.length; i++) {
				if (overlays[i].id) {
					var _id = overlays[i].id + "";
					if (!_id.startWith("grid")) {
						if (ctlType == "start") {
							if (_id.startWith(busiCode)) {
								_temps.push(overlays[i]);
							}
						} else {
							if (_id.endWith(busiCode)) {
								_temps.push(overlays[i]);
							}
						}
					}
				}
			}
			mapObj.remove(_temps);
		}
	};

	// 标注图标：	opt.id、opt.icon、opt.lng、opt.lat、opt.w、opt.h、opt.url、opt.name、
//				opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
	me.markerIcon = function(mapObj, opt, layerName, pixel) {
		if (typeof pixel == "undefined")  pixel = new AMap.Pixel(-15, -39);
		var marker = new AMap.Marker({
			position : new AMap.LngLat(opt.lng, opt.lat),
			icon : opt.icon,
			offset : pixel,
			draggable : false,
			title : opt.name
		});
		marker.id = opt.id;
		mapObj.add(marker);
		marker["opt"] = opt;
		marker.on("click", function(e) {
			var tMarker = me.getOverlaysById("busi_" + e.target.getPosition().B + "_" + e.target.getPosition().G + "_text_" + layerName);
			if (tMarker) {
				tMarker._names;
				tMarker._ids;
				
				var htmlStr = "";
				htmlStr += '<div class="menuNav" style="left: 672px; top:292px;height:218px;">';
				htmlStr += '<h2><span class="fr close" onclick="javascript:$.fn.ffcsMap.closeMutilPiontPanelDiv()"></span></h2>';
				htmlStr += '<div class="navlist" id="SNmenuNav">';
				htmlStr += '<dl>';
				htmlStr += '<dt class="icon04"><a href=\'javascript:$.fn.ffcsMap.showInfoWinByWid("114135803");\' title="闲散青少年">闲散青少年</a></dt>';
				htmlStr += '</dl>';
				htmlStr += '</div>';
				htmlStr += '</div>';
			} else {
				me.openInfoWindow(e.target.getPosition(), opt);
			}
		});
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
			me.markerIconsForCommon(opt, layerName);
		} else if (ctlType == "oldEvent") {
			me.markerIconsForOldEvent(opt, "eventLayer");
		} else if (ctlType == "buildingLayer") {
			opt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/build/" + CENTER_POINT_SETTINGS_BUILD;
			opt.url = MMGlobal.ContextPath + "/zhsq/map/arcgis/arcgisdata/getBuildInfoDetailOnMap.jhtml?buildingId=";
			opt.isShowDetail = true;
			opt.detailUrl = MMGlobal.SQ_ZZGRID_URL + "/zzgl/grid/areaBuildingInfo/standardDetail.jhtml?buildingId=";
			opt.width = 948;
			opt.height = 405;
			me.markerIconsForBuilding(opt, "buildingLayer");
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
		if (typeof (data.x) != "undefined") markerOpt.lng = data.x;
		if (typeof (data.y) != "undefined") markerOpt.lat = data.y;
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
	me.openInfoWindow = function(lngLat, _opt) {
		var opt = $.extend(true, {}, _opt);
		if (lngLat != null && typeof lngLat != "undefined") {
			opt.isArrow = true;
		} else {
			opt.isArrow = false;
			lngLat = new AMap.LngLat(MMGlobal.CurrGridInfo.lng, MMGlobal.CurrGridInfo.lat);
		}
		if (typeof opt.url != "undefined" && opt.url.indexOf('?') == -1) {
			opt.url += '?t=' + Math.random();
		} else {
			opt.url += '&t=' + Math.random();
		}
		opt.content = '<iframe width="100%" height="100%" src="'+opt.url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
		
		var html = me.getInfoWindowHtml(opt);
		
		MMGlobal.InfoWindow = null;
		MMGlobal.InfoWindow = new AMap.InfoWindow({
			isCustom : false,
			content : html,
			offset : new AMap.Pixel(-opt.w / 2 - 1, -66 - opt.h)
		});
		MMGlobal.InfoWindow.open(MMGlobal.mapObj, lngLat);
		
		setTimeout(function() {
			var offset = $("#NorMapOpenDiv").offset();
			var x = $(window).width() / 2 - opt.w / 2 - offset.left;
			var y = $(window).height() / 2 - opt.h / 2 - offset.top;
			MMGlobal.mapObj.panBy(x, y);
		}, 300);
	};

	me.closeInfoWindow = function() {
		$("#close").unbind();
		$("#watch").unbind();
		if (typeof MMGlobal.InfoWindow != "undefined" && MMGlobal.InfoWindow != null) {
			MMGlobal.InfoWindow.close();
		} else {
			MMGlobal.mapObj.clearInfoWindow();
		}
		MMGlobal.InfoWindow = null;
	};

	me.getInfoWindowHtml = function(opt) {
		var w = opt.w;
		var h = opt.h;
		var lng = opt.lng;
		var lat = opt.lat;
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

	// 单机覆盖物事件
	me.clickOverlayById = function(id, opt, ctlType) {
		if (typeof ctlType == "undefined") ctlType = "common";
		if (ctlType == "common") {
			var layerName = analysisOfElementsCollection(opt.ecs, "menuLayerName");
			me.clickOverlayForCommonById(id, opt, layerName);
		} else if (ctlType == "oldEvent") {
			me.clickOverlayForOldEventById(id, opt, "eventLayer");
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
				me.markerIcon(MMGlobal.mapObj, markerOpt, layerName);
			});
			me.markerIconEx(arguments, layerName);
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
				// 标注图标：	opt.id、opt.icon、opt.lng、opt.lat、opt.w、opt.h、opt.url、opt.name、
				//			opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
				var markerOpt = $.extend(true, {}, opt);
				markerOpt.id = "busi_" + data.wid + "_" + layerName;
				markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
				markerOpt.lng = data.x;
				markerOpt.lat = data.y;
				markerOpt.title = "事件";
				markerOpt.name = data.name;
				markerOpt.url = MMGlobal.SQ_ZZGRID_URL + '/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId=' + data.wid + '&taskInfoId=' + data.taskId;
				me.markerIcon(MMGlobal.mapObj, markerOpt, layerName, new AMap.Pixel(-9, -28));
			});
			me.markerIconEx(arguments, layerName, new AMap.Pixel(4, -37));
		}
	};

	me.markerIconEx = function(args, layerName, offset) {
		if (typeof offset == "undefined") offset = new AMap.Pixel(8, -46);
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
				var lnglat = keys[i].split("_");
				var marker = new AMap.Marker({
					position : new AMap.LngLat(lnglat[0], lnglat[1]),
					offset : offset,
					draggable : false,
					content : '<div style="width:22px;height:16px;font-size:11px;line-height:1.2;text-align:center;background-color:#FF4A58;color:#FFFFFF;">'+count+'</div>'
				});
				marker.id = "busi_" + keys[i] + "_text_" + layerName;
				marker._names = names[i];
				marker._ids = ids[i];
				MMGlobal.mapObj.add(marker);
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

	me.excuteAjax = function(url, data, _args) {
		$.ajax({
			type : "post",
			url : url,
			data : data,
			dataType : "json",
			error : function() {
				$.messager.alert('友情提示','excuteAjax：请求数据出错！', 'warning');
			},
			success : function(datas) {
				if (typeof datas != "undefined" && datas != null) {
					var map1 = new HashMap();
					var map2 = new HashMap();
					var map3 = new HashMap();
					$.each(datas, function(i, data) {
						var val1 = map1.get(data.x + "_" + data.y);
						var val2 = map2.get(data.x + "_" + data.y);
						var val3 = map3.get(data.x + "_" + data.y);
						if (val1 == null) {
							map1.put(data.x + "_" + data.y, 1);
							map2.put(data.x + "_" + data.y, data.name);
							map3.put(data.x + "_" + data.y, data.wid);
						} else {
							map1.put(data.x + "_" + data.y, val1 + 1);
							map2.put(data.x + "_" + data.y, val2 + "、" + data.name);
							map3.put(data.x + "_" + data.y, val3 + "、" + data.wid);
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
			}
		});
	};

	me.clickOverlayForCommonById = function(id, opt, layerName) {
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id);
		if (marker) {
			me.openInfoWindow(marker.getPosition(), marker.opt);
		} else {
			var markerOpt = me._getMarkerParams(opt, { wid : id });
			me.openInfoWindow(null, markerOpt);
		}
	};

	me.clickOverlayForOldEventById = function(id, opt, layerName) {
		var _id = "busi_" + id + "_" + layerName;
		var marker = me.getOverlaysById(_id);
		if (marker) {
			me.openInfoWindow(marker.getPosition(), marker.opt);
		} else {
			var markerOpt = $.extend(true, {}, opt);
			markerOpt.id = "busi_" + id + "_" + layerName;
			markerOpt.icon = uiDomain + "/images/map/gisv0/map_config/unselected/event/event_green.gif";
			markerOpt.title = "事件";
			markerOpt.url = MMGlobal.SQ_ZZGRID_URL + '/zzgl/map/ztywData/efficiencySupervision/eventDetail.jhtml?eventId=' + id + '&taskInfoId=' + opt.taskId;
			me.openInfoWindow(null, markerOpt);
		}
	};

	me.markerIconsForBuilding = function(opt, layerName) {
		var datas = me.getAjaxData(arguments);
		if (datas == null) {
			me.clearMap(layerName);
			var url =  MMGlobal.ContextPath + '/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuildsPoints.json?mapt='+MMGlobal.MapType+'&gridId='+opt.gridId+'&gridCode='+opt.gridCode+'&t='+Math.random();
			me.excuteAjax(url, {}, arguments);
		} else {
			$.each(datas, function(i, data) {
				// 标注图标：	opt.id、opt.icon、opt.lng、opt.lat、opt.w、opt.h、opt.url、opt.name、
				//			opt.title、opt.isShowDetail、opt.detailUrl、opt.width、opt.height
				var markerOpt = $.extend(true, {}, opt);
				markerOpt.id = "busi_" + data.wid + "_" + layerName;
				markerOpt.lng = data.x;
				markerOpt.lat = data.y;
				markerOpt.name = data.name;
				markerOpt.url = markerOpt.url + data.wid;
				markerOpt.detailUrl = markerOpt.detailUrl + data.wid;
				me.markerIcon(MMGlobal.mapObj, markerOpt, layerName, new AMap.Pixel(-9, -28));
			});
			me.markerIconEx(arguments, layerName, new AMap.Pixel(4, -37));
		}
	};

	me.getOverlaysById = function(id) {
		if (typeof id == "undefined" || id.length == 0) return null;
		var overlays = MMGlobal.mapObj.getAllOverlays();
		for (var i = 0; i < overlays.length; i++) {
			if (overlays[i].id == id) {
				return overlays[i];
				break;
			}
		}
	};
}
