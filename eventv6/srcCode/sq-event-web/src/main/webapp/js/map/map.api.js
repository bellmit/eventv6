/*
 * 多种地图引擎对外提供公用API；
 * Copyright 2016, YangCQ
 */
// 该全局变量命名空间：MMGlobal
var MM_SPGIS_IP = "172.26.181.152";
var MMGlobal = {};
// 该Api采用命名空间：MMApi
if (typeof MMApi == "undefined") var MMApi = {};
/*****************************************/
/**            * 对外提供接口 *            **/
/*****************************************/
// 地图初始化
MMApi.LoadMap = function(eventDomain, zzgridDomain, ip, engineType, loadedFn, options) {
	MM_SPGIS_IP = ip;
	MMGlobal = {
		mapObj : null,
		toolbar : null,
		overview : null,
		mouseTool : null,
		scale : null,
		ContextPath : eventDomain,
		SQ_ZZGRID_URL : zzgridDomain,
		// 地图类型
		MapType : 5,
		// 框选标识
		KXPolygon : null,
		IsZhouBian : false,
		// 网格中心点坐标
		CurrGridInfo : null,
		fillOpacity : 0.3,
		TipObj : null,
		InfoWindow : null,
		MD5 : "A90CBC0CAC5126EB38780AB9C14FB2FC",
		MapEngine : {
			"MapABC" : {
				relyJs : [ "http://app.mapabc.com/apis?t=javascriptmap&v=3&key=b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3",
				           "/js/map/mapabc.impl.js" ]
			},
			"AMap" : {
				relyJs : [ "http://webapi.amap.com/maps?v=1.3&key=65bee16376b1f60716f038c35e715c7e", 
				           "/js/map/amap.impl.js" ]
			},
			"SpGisMap" : {
				relyJs : [ "/js/map/spgis/lib/xdate.js", 
				           "/js/map/spgis/lib/jquery.cookie.js", 
				           "/js/map/spgis/lib/jQuery.md5.js", 
						   "/js/map/spgis/lib/json2.js", 
						   "http://"+MM_SPGIS_IP+":6060/fjts/fjts/getspgisjs?ldomnlkweioi32kioli3=default", 
				           "/js/map/spgis/lib/OpenLayers.extend.js", 
				           "/zhsq/map/arcgis/arcgis/getBizIdJs.jhtml", 
				           "/js/map/spgis.impl.js" ]
			}
		}
	};
	$.extend(MMGlobal, options);
	var engine = MMGlobal.MapEngine[engineType];
	MMApi.LoadJs(engine.relyJs, function() {
		// 验证用户标识，成功后回调
		SPGIS.VerifyKey("2da73f2f-6485-479b-9e0c-da85b49d370c", function(e) {
			if (e.state == 1) {
				alert("验证用户标识异常");
				return;
			}
			var mapApi = new MapApiImpl();
			for (var fn in mapApi) {
				if (typeof MMApi[fn] == "function") {
					MMApi[fn] = mapApi[fn];
				}
			}
			if (typeof loadedFn == "function") {
				loadedFn.call(this, mapApi);
			}
		});
	});
};
MMApi.Init = function(targetId, gridId, infoOrgCode) {};
// 初始化点位标注
MMApi.InitMarkPoint = function(targetId, gridId, x, y, isEdit, callBackFn) {};
// 左上角网格回调函数
MMApi.setCenter = function(gridId, infoOrgCode) {};
// 开始进行框选操作
MMApi.startKuangXuan = function(callBack) {};
// 重新进行框选操作
MMApi.renewKuangxuan = function() {};
// 打开框选
MMApi.openKuangxuan = function() {};
// 结束框选操作
MMApi.closeKuangxuan = function() {};
// 获取框选后的坐标值
MMApi.getGeoString = function() {};
// 重画指定Level的网格轮廓
MMApi.getArcgisDataOfGrids = function(gridId, gridCode, mapt, gridLevel) {};
// 根据参数清空覆盖物
MMApi.clearMap = function(busiCode, ctlType) {};
// 标注图层图标
MMApi.markerIcons = function(opt, ctlType) {};
// 单击列表触发覆盖物弹出事件
MMApi.clickOverlayById = function(id, opt, ctlType) {};
// 单击地图上存在标注点的事件
MMApi.clickExistOverlayById = function(id, lon, lat, layerName) {};
// 开启/关闭热力图
MMApi.heatMap = function(isOpen) {};
// 画楼宇轮廓
MMApi.drawBuildLine = function() {};
MMApi.drawBuildingLine = function(gridId, infoOrgCode) {};
// 迁徙图（全国各市：1，省内各市：2）
MMApi.initMigrateDtMap = function(targetId) {};
MMApi.drawMigrationLine = function(outCityName, inCityName) {};
MMApi.initMigrateDtMapEx = function(isOutType) {};
MMApi.removeAllMigrationLine = function() {};

// 关闭弹出窗口
MMApi.closeInfoWindow = function() {};
// 详情接口
MMApi.showDetail = function(title, url, width, height) {};
// 切换地图底图
MMApi.showSicMapOfName = function(type) {};
// 动态批量加载js
MMApi.LoadJs = function(aryJs, callBack) {
	if (typeof aryJs != "undefined" && aryJs.length > 0) {
		var i = 0;
		if (arguments.length > 2) {
			i = arguments[2];
		}
		var url = "";
		var _call = null;
		if (typeof aryJs[i] == "object") {
			url = aryJs[i]["path"];
			_call = aryJs[i]["call"];
		} else {
			url = aryJs[i];
		}
		if (typeof url == "function") {
			url.call();
			if (typeof _call == "function") _call.call();
			if (aryJs.length - 1 > i) {
				MMApi.LoadJs(aryJs, callBack, ++i);
			} else {
				if (typeof callBack == "function") callBack.call();
			}
		} else {
			if (!url.startWith("http://")) {
				url = MMGlobal.ContextPath + url;
			}
			$.getScript(url, function() {
				if (typeof _call == "function") _call.call();
				if (aryJs.length - 1 > i) {
					MMApi.LoadJs(aryJs, callBack, ++i);
				} else {
					if (typeof callBack == "function") callBack.call();
				}
			});
		}
	}
};

String.prototype.endWith = function(s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length)
		return false;
	return this.substring(this.length - s.length) == s;
};

String.prototype.startWith = function(s) {  
	if (s == null || s == "" || this.length == 0 || s.length > s.length)
		return false;
	return this.substr(0, s.length) == s;
};

String.prototype.gblen = function() {
	var len = 0;
	for ( var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) > 127 || this.charCodeAt(i) == 94) {
			len += 2;
		} else {
			len++;
		}
	}
	return len;
};
