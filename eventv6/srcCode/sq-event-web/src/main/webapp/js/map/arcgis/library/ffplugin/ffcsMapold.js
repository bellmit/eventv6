/**
*
* 功能介绍：对arcgis图层加载及处理
* 版本： v.1
* 日期： 2014.4.18
**/
(function($){
	
	var map ,toolbar,//工具条
	editToolbar,//可编辑工具
	event,
	jsapiBundle,
	esriConfig, 
    Graphic,
    GraphicsLayer,
    KMLLayer,
    OpenStreetMapLayer,
    ArcGISDynamicMapServiceLayer,
    TiledMapServiceLayer,
    ArcGISTiledMapServiceLayer,
    RestTiled, //20140905
    TileInfo,
    AttributeInspector,
    InfoWindowLite, //信息窗口
    InfoTemplate,
    esriLang, 
    webMercatorUtils, 
    SimpleFillSymbol,
    SimpleLineSymbol, 
    SimpleMarkerSymbol, 
    OverviewMap,
    dom, 
    on, 
    Color,
    Editor, 
    TemplatePicker,
    arrayUtils,
    Button;
	
	var Point, Polyline, Polygon;
	var Draw, parser, registry;
	
	var FeatureLayer,SimpleRenderer; // 渲染图层区域
	var TooltipDialog;
	var zoomSymbol,highlightSymbol;
	
	var domStyle,dijitPopup;
	var dialog;
	
	var southCarolinaCounties;
	var domConstruct,Query;
	var updateFeature;
	var isDrawEdit = false;//是否可编辑
	
	var returnDataGraphic; // 用于存放返回几何图形数据
	var drawData ; // draw function call
	
	// 点定位
	var pointInfoDiv;
	
	var hotlayer,builddianpuDv,ptiplayer; //热点提示
	
	var ffcsMap_current_obj = {};
	
	var tempLayer; // 临时图层
	
	/**
	 * 地图对象加载主方法
	 * div绑定加载,如：$("#map").ffcsMap();  	
	 * 参数：options 可根据html中定义的id名称加载，如{mapId:"map111"}
	 */
	$.fn.ffcsMap = function(options){
		$.fn.ffcsMap.init();
		var opts = $.extend({}, $.fn.ffcsMap.defaults, options); 
		return this.each(function(){
			_this = $(this);
			var obj =   $.meta ? $.extend({}, opts, _this.data()) : opts;  
			$.fn.ffcsMap.zoom();
			$.fn.ffcsMap.create(obj.mapId,obj);
		});
	};
	
	/**
	 * 
	 * 默认配置: div id="map"
	 */
	$.fn.ffcsMap.defaults = {
		 mapId : "map",   //div 名称
		 x : -97.395,     //中心点x值
		 y : 37.537,      //中心点y值
		 zoom : 11,       //缩放级别
	     slider : false,  //是否有缩放工具条
	     
	     //加载的图层地址
	     mapUrl : "",    
	     layerType : ""   //图层类型
	};
	
	
	/***
	 * 初始化esri API对象
	 */
	$.fn.ffcsMap.init = function(){
		require([ 
		        "esri/config", "esri/graphic",
		        "esri/layers/GraphicsLayer",
		        "esri/layers/KMLLayer",
		        "esri/layers/OpenStreetMapLayer",
		        "esri/layers/ArcGISDynamicMapServiceLayer",
		        "esri/layers/TiledMapServiceLayer",
		        "esri/layers/ArcGISTiledMapServiceLayer",
		        "esri/layers/TileInfo",
		        "esri/dijit/AttributeInspector",
		        "esri/dijit/InfoWindowLite",
				"esri/InfoTemplate", "esri/lang", 
				"esri/geometry/Point",
				"esri/geometry/Polyline",
				"esri/geometry/Polygon",
				"esri/geometry/webMercatorUtils",
				"esri/symbols/SimpleFillSymbol",
				"esri/symbols/SimpleLineSymbol",
				"esri/symbols/SimpleMarkerSymbol", "dojo/dom", "dojo/on",
				"dojo/_base/Color",
				"esri/dijit/editing/Editor",
				"esri/dijit/editing/TemplatePicker", "dojo/_base/array" ,
				"esri/layers/FeatureLayer",
				"esri/renderers/SimpleRenderer",
				"dijit/TooltipDialog",
				"dojo/dom-style",
				"dijit/popup",
				
				"esri/toolbars/draw",
				"dojo/parser", "dijit/registry",
				
				"esri/toolbars/edit",
				"dojo/_base/event",
				"dojo/i18n!esri/nls/jsapi",
				
				"dojo/dom-construct",
				"esri/tasks/query",
				"dijit/form/Button",
				
				"esri/dijit/OverviewMap",
				
				"dojo/domReady!"
				],
		   function(p_esriConfig, p_Graphic, p_GraphicsLayer, p_KMLLayer,
				   p_OpenStreetMapLayer,
				   p_ArcGISDynamicMapServiceLayer,
				   p_TiledMapServiceLayer,
				   p_ArcGISTiledMapServiceLayer,
				   p_TileInfo,
				   p_AttributeInspector,
				   p_InfoWindowLite,
				   p_InfoTemplate,
				   p_lang,
				   
				   p_Point, p_Polyline, p_Polygon,
				   p_webMercatorUtils, p_SimpleFillSymbol,
				   p_SimpleLineSymbol, p_SimpleMarkerSymbol, p_dom, p_on, p_Color,
				   p_Editor, p_TemplatePicker, p_arrayUtils,
				   p_FeatureLayer,
				   p_SimpleRenderer,
				   p_TooltipDialog,
				   p_domStyle,
				   p_dijitPopup,
				   
				   p_Draw,
				   p_parser, p_registry,
				   p_Edit,p_event,
				   p_jsapiBundle,
				   p_domConstruct,
				   p_Query,
				   p_Button,
				   p_OverviewMap
		   ) {
			// 初始值
			esriConfig = p_esriConfig; 
		    Graphic = p_Graphic;
		    GraphicsLayer = p_GraphicsLayer;
		    KMLLayer = p_KMLLayer;
		    OpenStreetMapLayer = p_OpenStreetMapLayer;
		    ArcGISDynamicMapServiceLayer = p_ArcGISDynamicMapServiceLayer;
		    TiledMapServiceLayer = p_TiledMapServiceLayer;
		    ArcGISTiledMapServiceLayer = p_ArcGISTiledMapServiceLayer;
		    RestTiled = p_ArcGISTiledMapServiceLayer;  //20140905
		    TileInfo = p_TileInfo;
		    AttributeInspector =  p_AttributeInspector;
		    Point = p_Point, 
		    Polyline = p_Polyline;
		    Polygon = p_Polygon;
		    InfoWindowLite = p_InfoWindowLite;
		    InfoTemplate  = p_InfoTemplate;
		    esriLang = p_lang; 
		    webMercatorUtils = p_webMercatorUtils;  
		    SimpleFillSymbol = p_SimpleFillSymbol;
		    SimpleLineSymbol = p_SimpleLineSymbol; 
		    SimpleMarkerSymbol = p_SimpleMarkerSymbol; 
		    dom = p_dom; 
		    on = p_on; 
		    Color = p_Color;
		    Editor = p_Editor; 
		    TemplatePicker = p_TemplatePicker;
		    arrayUtils = p_arrayUtils;
		    
		    Draw = p_Draw; parser = p_parser; registry = p_registry;
		    
		    FeatureLayer = p_FeatureLayer;
		    SimpleRenderer = p_SimpleRenderer;
		    TooltipDialog = p_TooltipDialog;
		    
		    domStyle= p_domStyle;
		    dijitPopup = p_dijitPopup;
		    
		    Edit = p_Edit;
		    event = p_event;
		    jsapiBundle = p_jsapiBundle;
		    domConstruct = p_domConstruct;
		    Query = p_Query;
		    Button = p_Button;
		    OverviewMap = p_OverviewMap;
		});
	};
	
	
	$.fn.ffcsMap.zoom = function(){
		 /*zoomSymbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, 
                new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
			     new Color([20, 156, 255 ]), 1), 
			     new Color([141, 185, 219, 0.3 ]));

		 esriConfig.defaults.map.zoomSymbol = zoomSymbol.toJson(); 
		
		zoomSymbol = new  SimpleFillSymbol(
				 SimpleFillSymbol.STYLE_SOLID, 
		          new  SimpleLineSymbol(
		        		   SimpleLineSymbol.STYLE_SOLID, 
		            new Color([255,255,255,0.35]), 
		            1
		          ),
		          new Color([125,125,125,0.35])
		        );
		*/
	    /*highlightSymbol = new  SimpleFillSymbol(
	    		 SimpleFillSymbol.STYLE_SOLID, 
   	          new  SimpleLineSymbol(
   	        		 SimpleLineSymbol.STYLE_SOLID, 
   	            new Color([255,0,0]), 3
   	          ), 
   	          new Color([125,125,125,0.35])
   	        );*/
	};
	
	/**
	 * 创建地图
	 * 参数1：div id
	 */
	$.fn.ffcsMap.create = function(mapId,opts){
		//parser.parse();
		
		////////////////////
		//esriConfig.defaults.io.proxyUrl = "/proxy";
		//esriConfig.defaults.io.proxyUrl =  "../../tiandt/proxy.jsp";
		//esriConfig.defaults.io.alwaysUseProxy = false;
		if(esri!=undefined){
			esri.config.defaults.map.height = (opts.height!=undefined ? opts.height : 600);
			esri.config.defaults.map.width =  (opts.width!=undefined ? opts.width :1100);
		}
		
        map = new esri.Map(mapId , {
//            basemap: "streets",
	        center: [opts.x, opts.y],
	        zoom: opts.zoom,
	        slider: opts.slider
	      });

        //工具条画图
		//toolbar = new Draw(map);
        
        /////////////////////
        var url = opts.mapUrl;
        $.fn.ffcsMap.createLayer(url, opts.layerType);
        
	    map.infoWindow.resize(245,125);
	        
	    $.fn.ffcsMap.on();
	    //$.fn.ffcsMap.popup();
	};
	
	
	/**---------------------------------------------
	 *   创建图层
	 * ----------------------------------------------
     *  @see 
     *  arcgis图层类型:  	Tiled map services
     *                 	Dynamic map services
     *                 	Feature layers
     *					Graphics (SVG, canvas, etc.)
	 *				    KML
	 *				    Open street map
	 *				    Bing
	 *				    WMS / WMTS
	 *				    Custom layers 
	 *************************************************/
	$.fn.ffcsMap.createLayer = function(url , layerType, options){
		switch(layerType){
			case "restTiled" :
				var tiled = new RestTiled(url);
				map.spatialReference = new esri.SpatialReference({ wkid : options.wkid});
		        map.addLayer(tiled);
				break;
			case "tiled" :
				var tiledMapServiceLayer = new esri.layers.ArcGISTiledMapServiceLayer(url);
				map.spatialReference = new esri.SpatialReference({ wkid : options.wkid});
				map.addLayer(tiledMapServiceLayer);
				$.fn.ffcsMap.setScreenPostition();
				break;
			case "dynamic" :
				var petroFieldsMSL = new ArcGISDynamicMapServiceLayer(url);
		        petroFieldsMSL.setDisableClientCaching(true);
		        map.addLayer(petroFieldsMSL);
		        break;
			case "feature" :
				var featureLayer = new esri.layers.FeatureLayer(url,{
					mode: FeatureLayer.MODE_SNAPSHOT,
		            outFields: ["*"]
			    });
				
				map.addLayer(featureLayer);
				break;
			case "kml" :
				var kmlLayer = new KMLLayer(url); 
			    map.addLayer(kmlLayer);
				break;
			case "osm" :
				var osmLayer = new OpenStreetMapLayer();
		        map.addLayer(osmLayer);
				break;
			case "wmts" :
				dojo.declare("WMTSServiceLayer", esri.layers.TiledMapServiceLayer, {
		            constructor: function() {
		            	  // 空间参考系
		        		  this.spatialReference = new esri.SpatialReference({ wkid : options.wkid});
		        		  // 初始化范围 (左下角坐标，右上角坐标)
		        		  this.initialExtent = new esri.geometry.Extent(options.xmin, options.ymin, options.xmax, options.ymax, this.spatialReference);
		        		  // 全屏范围 
		                  this.fullExtent = new esri.geometry.Extent(options.xmin, options.ymin, options.xmax, options.ymax, this.spatialReference);
		                  // 切片信息
		        		  this.tileInfo = new esri.layers.TileInfo({
		        			  	"compressionQuality" : 0,
		        		        "rows" : options.rows, // 切片高
		        				"cols" : options.cols, // 切片宽
		        				"origin" : { "x" : options.origin.x,"y" : options.origin.y }, // 切图原点
		        				"spatialReference" : {"wkid" : options.wkid},// 空间参考系
		        				"lods" : LevelDetail.lods
		        		  });
		        		  
		        		  this.loaded = true;
		        		  this.onLoad(this);
		            },
		            
		            getTileUrl: function(level, row, col) {
		            	
		            	var prcUrl = "";
		            	var tileMatrix = level * 1 + 1 ;
		            	if(options.layerId=="MapServer"){
		            		if(options.tileMatrix  != undefined ){
		            			tileMatrix = level * 1 ;
		            		}
		            		prcUrl = url ;
		            	}else if(options.layerId=="NPVEC" || options.layerId=="NPCVA" || options.layerId =="NPIMG" || options.layerId =="NPCIA"){
		            		tileMatrix = tileMatrix -1;
		            		prcUrl = url ;
		            	}else if(options.layerId=="TianDiVector" ){
		            		//tileMatrix = tileMatrix -1;
		            		prcUrl = url ;
		            	}else{
		            		prcUrl = url + options.layerId + "_c/wmts";
		            	}
		            	return  prcUrl + "?Service=WMTS&Request=GetTile&Version=1.0.0" +  
			                "&Style=Default&Format="+options.imageFormat+"&serviceMode="+options.serviceMode+"&layer="+options.layerId +  
			                "&TileMatrixSet="+options.tileMatrixSetId+"&TileMatrix=" + tileMatrix + "&TileRow=" + row + "&TileCol=" + col;
		            
		            }
				}); 
				map.addLayer((new WMTSServiceLayer()));
				$.fn.ffcsMap.setScreenPostition();
				break;
		}
	};
	
	/**
	 * 指定城市 并渲染symbol,添加到地图中。
	 */
	$.fn.ffcsMap.definitionExpression = function(){
		//指定城市
        southCarolinaCounties.setDefinitionExpression("STATE_NAME = 'South Carolina'");
        
        southCarolinaCounties.setRenderer(new SimpleRenderer(zoomSymbol));
        map.addLayer(southCarolinaCounties);
	};
	
	/***
	 * 
	 * 地图绑定事件
	 * 
	 */
	$.fn.ffcsMap.on = function(mapId){
		map.on("load", function () {
			map.graphics.enableMouseEvents();
			// 禁用鼠标双击缩放地图事件
			map.disableDoubleClickZoom();
//			map.graphics.on("mouse-out", __closeDialog);
			
			//工具条画图
			toolbar = new esri.toolbars.Draw(map);
	        toolbar.on("draw-end", addToMap);
	        
	        editToolbar = new esri.toolbars.Edit(map);
        });

		map.on("layer-add", function () {
            console.log("Map layer-add event");
        });
        
        map.on("pan-end", function(e) {
			mend_runCount = mend_runCount + 1;
			if(mend_runCount == 1){
				var m_x = e.delta.x ; 
				var m_y = e.delta.y ; 
				var position = $(".NorMapOpenDiv").position(); 
				if(position != undefined){
					var pleft = position.left;
					var ptop = position.top;
					pleft =   pleft + m_x ;
					ptop =  ptop + m_y ;
					$(".NorMapOpenDiv").css("left",pleft);
					$(".NorMapOpenDiv").css("top",ptop);
				}
			}
	            
			mend_runCount = 0;
			
			$.fn.ffcsMap.setScreenPostition();
			
		});
		
		map.on("zoom-end",function(e){
        	$.fn.ffcsMap.setScreenPostition();
		});
		
		//清除地图，关闭提示信息.
		function __closeDialog() {
			console.log("close");
	          //map.graphics.clear();//清除
	          dijitPopup.close(dialog);
	     }
		
		//事件添加到Map上
	    function addToMap(evt) {
	          var symbol;
	          toolbar.deactivate();
	          map.showZoomSlider();
	          switch (evt.geometry.type) {
	            case "point":
	            case "multipoint":
	              symbol = new SimpleMarkerSymbol();
	              break;
	            case "polyline":
	              symbol = new SimpleLineSymbol();
	              break;
	            default:
	              symbol = new SimpleFillSymbol();
	              break;
	          }
	          
	          var graphic = new Graphic(evt.geometry, symbol);
	          returnDataGraphic = graphic;
	          map.graphics.add(graphic);
	     }
	};
	/**
	 * 设当前中心点对象及级别
	 * 
	 */
	$.fn.ffcsMap.setScreenPostition = function(){
		var dh = $(document).height()/ 2 + 325;
		var dw = $(document).width()/ 2 - 325 ;
		//conlose.log(map.container.clientHeight  + ":"+map.container.clientWidth );
		//alert("new:"+dh+":"+dw);
		var cpt = new esri.geometry.Point(
				dh ,
				dw ,
				new esri.SpatialReference({
					wkid : map.spatialReference["wkid"]
				})
		);
		var cmp;
		try{
			cmp = map.toMap(cpt); 
		}catch(e){
			console.log(e);
		}
		
		ffcsMap_current_obj.level = map.getLevel();
		ffcsMap_current_obj.centerPoint = cmp;
		
		//console.log(ffcsMap_current_obj);
	};
	
	
	/**
	 * 获取当前地图中心点对象
	 * 
	 * return  level centerPoint
	 * 
	 */
	$.fn.ffcsMap.getCurrentMapCenterObj = function(){
		return ffcsMap_current_obj;
	};
	
	/**
	 * 获取点、线、面元素的类型及坐标
	 * 
	 * @param graphic 当前graphic对象
	 */
	$.fn.ffcsMap.getShapeInfo = function(graphic) {
		if (graphic == null) {
			return;
		}
		
		var _geometry = graphic.geometry;
		var _type = _geometry.type;
		
		var drawData = "";
		
		switch (_type) {
		case "point":
			drawData = {"type":_type, "coordinates":[_geometry.x, _geometry.y]};
			break;
		case "polyline":
			drawData =  {"type":_type, "coordinates":_geometry.paths};
			break;
		case "polygon":
			drawData =  {"type":_type, "coordinates":_geometry.rings};
			break;
		}
		
		return JSON.stringify(drawData);
	};
	
	/**
	 * 显示几何图形
	 * 
	 * @param data 几何图形的json数据格式
	 * 
	 * 点、{"type":"point","coordinates":[561871.0272757602,3252726.915741879]};
	 * 线、{"type":"polyline","coordinates":[[[559728.3054255827,3254914.070969093],[560041.572718784,3253280.001034287]]]};
	 * 面、{"type":"polygon","coordinates":[[[557199.8490996079,3255223.6305997875],[558884.7191360146,3255138.963763787],
	 *     [558114.2509284116,3253741.9609697815],[557199.8490996079,3255223.6305997875]]]}
	 */
	$.fn.ffcsMap.renderShape = function(data) {
		switch (data.type) {
		case "point" :
			var point = new Point(data.coordinates[0], data.coordinates[1]);
			var symbol = new SimpleMarkerSymbol();
			var graphic = new Graphic(point, symbol);
			map.graphics.add(graphic);
			break;
		case "polyline" :
			var points = [];

			for (var i = 0; i < data.coordinates[0].length; i++) {
            	points[i] = new Point(data.coordinates[0][i]);  
	        }  
			
			var polyline = new Polyline();
			polyline.addPath(points);
			
			var symbol = new SimpleLineSymbol();
			var graphic = new Graphic(polyline, symbol);
			map.graphics.add(graphic);
			break;
		case "polygon" :
			var rings = [];

			for (var i = 0; i < data.coordinates[0].length; i++) {
				rings[i] = new Point(data.coordinates[0][i]);  
	        }  
			
			var polygon = new Polygon();
			polygon.addRing(rings);
			
			var symbol = new SimpleFillSymbol();
			
			var graphic = new Graphic(polygon, symbol);
			map.graphics.add(graphic);
			break;
		}
	};
	
	/**
	 * 
	 * @see 根据x,y 图片,显示定位
	 * 
	 * @param x
	 * @param y
	 * @imgUrl 图标路径
	 * @callBack  参数为x,y数组
	 */
	$.fn.ffcsMap.renderPoint = function (x, y, imgUrl, callBack) {
		if (imgUrl == '' || imgUrl == undefined) {
			imgUrl = 'images/GreenShinyPin.png';
		}
		
		var symbol = new esri.symbols.PictureMarkerSymbol(imgUrl, 34, 48);
		var rwkid = map.spatialReference["wkid"];
		var point = new esri.geometry.Point(x, y,new esri.SpatialReference({ wkid: rwkid }));
		var graphic = new esri.Graphic(point, symbol);
		
		var pointLayer = new esri.layers.GraphicsLayer();
		pointLayer.add(graphic);
		map.addLayer(pointLayer);
		
		var toolbar = new esri.toolbars.Edit(map);
		
		pointLayer.on("click", function(evt) {
			toolbar.activate(Edit.MOVE, evt.graphic);
			
			toolbar.on("graphic-move-stop", function() {
				var newPoint = evt.graphic.geometry;
				
				callBack([newPoint.x, newPoint.y]);
			});
		});
	};
	
	/**
	 * 渲染楼宇网格数据接口 
	 * 
	 * 
	 * @param layerName 图层名称
	 * @param dataUrl 数据请求地址
	 * @param showType 1 hide  2 show
	 * @param isCvt  是否转换数据
	 * @param imgUrl 图片路径
	 * @param fontSize 字体大小
	 * @param isCPImg  true(中心点为图片) false(文字) 是否中心点为图片 2014-12-11
	 * @param callSubwg(_data) 网格中心点图片单击事件方法回调, _data参数为回调的数据
	 */
	$.fn.ffcsMap.render = function(layerName,dataUrl,showType,isCvt,imgUrl,imgWidth,imgHeight,fontSize,isCPImg,callSubwg) {
		var pointLayer; // 点
		var louyuLayer; // 楼宇
		var wanggeLayer; // 网格
		
		//样式默认定义
		var fillStyle = "solid"; // 填充样式
		var lineStyle = "solid"; // 边框样式
		
		// 十六进制颜色
		var defLineColor = "#FFFF00";// 边框颜色 [138,43,226];  // 边框颜色
		var defAreaColor = "#ADFF2F";// 填充颜色 [173,255,47];  // 填充颜色
		
		// 十六进制颜色转为RGB
		var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
		var defRGBAreaColor = new dojo.Color(defAreaColor).toRgb();
		
		var defLineWidth = 5 ; // 边框线条宽度
		
		// 文字颜色
		var defNameColor = "#000000";// 文字颜色
		var defRGBNameColor = new dojo.Color(defNameColor).toRgb();
		
		// 文字大小
		var defTextFont = "8pt"; // 文字大小
		
		if (showType == 0) {
			// 不存在该图层则新建
			if (map.getLayer(layerName) == undefined) {
				pointLayer = new esri.layers.GraphicsLayer({id:layerName});
				map.addLayer(pointLayer);
			} else {
				// 直接获取
				pointLayer = map.getLayer(layerName);
			}
			
			// 绑定事件
			pointLayer.on("mouse-move", function(evt) {
				//$.fn.ffcsDisplayhot(evt);
				mouseMove(evt);
			});
			
			pointLayer.on("mouse-out", function(evt) {
				mouseOut(evt);
			});
		}
		
		if (showType == 1) {
			// 不存在该图层则新建
			if (map.getLayer(layerName) == undefined) {
				louyuLayer = new esri.layers.GraphicsLayer({id:layerName});
				map.addLayer(louyuLayer);
			} else {
				// 直接获取
				louyuLayer = map.getLayer(layerName);
			}
			
			// 绑定事件
			louyuLayer.on("mouse-move", function(evt) {
				//$.fn.ffcsDisplayhot(evt);
				mouseMove(evt);
			});
			
			louyuLayer.on("mouse-out", function(evt) {
				mouseOut(evt);
			});
		} 
		
		if (showType == 2) {
			if (map.getLayer(layerName) == undefined) {
				wanggeLayer = new esri.layers.GraphicsLayer({id:layerName});
				map.addLayer(wanggeLayer);
			} else {
				wanggeLayer = map.getLayer(layerName);
			}
			
			// 绑定事件
			wanggeLayer.on("mouse-move", function(evt) {
				//$.fn.ffcsDisplayhot(evt);
				mouseMove(evt);
			});
			
			wanggeLayer.on("mouse-out", function(evt) {
				mouseOut(evt);
			});
		}
		
		var rwkid = map.spatialReference["wkid"];
		$.getJSON(dataUrl, function(datas) {
			// 设置字体的大小
			var textFont = defTextFont;
			
			if (fontSize != null) {
				textFont = fontSize + "pt";
			}
			
			$.each(datas, function(i,data){
				// 给对象设值   用来区分点、楼宇还是网格
				data.showType = showType;
				data.layerName = layerName;
				
				var _data = {};
				
				if(isCvt){
					_data = $.fn.ffcsMap.DataCvtUtil(data,true);//转换
				}else{
					_data = data;
				}
				
				var rings = [];
				for (var i = 0; i < _data.coordinates[0].length; i++) {
					rings[i] = new esri.geometry.Point(_data.coordinates[0][i]);  
				}  
				
				
				var polygon = new Polygon(new esri.SpatialReference({ wkid : rwkid }));
				polygon.addRing(rings);
				
				// 边框颜色
				var RGBLineColor = defRGBLineColor;
				// 填充颜色
				var RGBAreaColor = defRGBAreaColor;
				
				if(_data.lineColor != "" && _data.lineColor != null){
					RGBLineColor = new Color(_data.lineColor).toRgb();
				}
				
				if(_data.areaColor != "" && _data.areaColor != null){
					RGBAreaColor = new Color(_data.areaColor).toRgb();
				}
				
				// 边框线条宽度
				var lineWidth = defLineWidth;
				
				if (_data.lineWidth != "" && _data.lineWidth != null) {
					lineWidth = _data.lineWidth;
				}
				
				// 文字颜色
				var RGBNameColor = defRGBNameColor;
				
				if (_data.nameColor != "" && _data.nameColor != null) {
					RGBNameColor = new Color(_data.nameColor).toRgb();
				}
				
				// 中心点坐标
				var xloc = _data.x;
			    var yloc = _data.y;
			    
			    // 构造中心点
			    var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid }));
			    
			    // 区域名称 data.name
		        var textString = _data.gridName;
		        
		        // 网格样式    默认显示轮廓
				var symbol = new SimpleFillSymbol(fillStyle,
					          new SimpleLineSymbol(lineStyle, new Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1]), lineWidth),
					          new Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
				
				// 文字样式    默认半透明
				var textSymbol = new esri.symbol.TextSymbol(textString).
									setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
							        setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
							        setFont(new esri.symbol.Font(textFont).
							        setWeight(esri.symbol.Font.WEIGHT_BOLD));
				
				// 0  点
				// 1  楼宇  地图默认为隐形，鼠标移上去时显示（显示区域填充颜色（半透明））
				// 2  网格  地图默认为显性，鼠标移上去时显示（显示区域填充颜色（半透明））
				if (data.showType == 0) {
					if (imgUrl == '' || imgUrl == undefined) {
						imgUrl = 'images/GreenShinyPin.png';
					}
					
					// 文字
					textSymbol.setColor(new dojo.Color([0, 0, 0, 0]));
					textSymbol.setFont(new esri.symbol.Font("12pt"));
					textSymbol.setOffset(35, -10);
					var graphicsText = new esri.Graphic(point, textSymbol);
					pointLayer.add(graphicsText);
					
					var symbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					
					var p = new esri.geometry.Point(data.x, data.y,new esri.SpatialReference({ wkid: rwkid }));
					
					var graphic = new Graphic(p, symbol);
					pointLayer.add(graphic);
					
					graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
				} else if (data.showType == 1) {
			        // 无填充色 data.areaColor
			        symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
			        // 边框颜色 data.lineColor 全透明
			        symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0]));
			        // 文字也默认不显示   全透明  data.nameColor
			        textSymbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 0]));
			        
			        // 文字
			        var graphicsText = new esri.Graphic(point, textSymbol);
			        louyuLayer.add(graphicsText);
			        
			        // 楼宇
			        var graphic = new esri.Graphic(polygon, symbol);
			        louyuLayer.add(graphic);
			        graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
				} else if(data.showType == 2) {
					// 网格中的文字默认半透明显示
					//var graphicsText = new esri.Graphic(point, textSymbol);
					//wanggeLayer.add(graphicsText);
					
					var graphic = new esri.Graphic(polygon, symbol);
					wanggeLayer.add(graphic);
					
					// 网格中的文字默认半透明显示
					var graphicsText;
					if(isCPImg){
						var picsymbol = new esri.symbols.PictureMarkerSymbol(js_ctx +'/js/map/arcgis/library/style/images/wglocal.png', 20, 28);
						graphicsText = new esri.Graphic(point, picsymbol);
						
					    wgPointLayer = new esri.layers.GraphicsLayer();
						wgPointLayer.add(graphicsText);
						
						wgPointLayer.on("click",function(evt){
							if(callSubwg){
								callSubwg(_data);
							}
						});
						
						//wgPointLayer.on("dbl-click",function(evt){
						//	console.log("===========");
						//});
						
						map.addLayer(wgPointLayer);
					}else{
						graphicsText = new esri.Graphic(point, textSymbol);
						wanggeLayer.add(graphicsText);
					}
					
					graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
				}
				
				// 设值     类型，文字
//				if (graphic != null) {
//					graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
//				}
			});
		});
		
	    // 当前鼠标移动所在图形
		function mouseMove(evt) {
			var graphic = evt.graphic;
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				var _data = graphic.attributes.data;
				var type = graphic.attributes.type;
				var symbol = graphic.symbol;
				
				var RGBLineColor = defRGBLineColor;
				var RGBAreaColor = defRGBAreaColor;
				var RGBNameColor = defRGBNameColor;
				
				if(_data.lineColor != "" ){
					RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
				}
				
				if(_data.areaColor != "" ){
					RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
				}
				
				if (_data.nameColor != "") {
					RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
				}
				
				if (type == 0) {
					// 文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([0, 0, 0, 1]));
					//textGraphic.setSymbol(textSymbol);
					var ptitle = textGraphic.symbol.text;
					$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
				} else if (type == 1) { // 楼宇  半透明填充 并显示边界
					symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1])); // 边界
					graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0.5])); // 填充
					
					// 文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1]));
					//textGraphic.setSymbol(textSymbol);
					
					var ptitle = textGraphic.symbol.text;
					$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
				} else { // 网格 不填充
					graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
					var ptitle = graphic.attributes.data.gridName;
					$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
				}
				
				graphic.setSymbol(symbol);
			}
		}
		
		// 鼠标离开
		function mouseOut(evt) {
			var graphic = evt.graphic;
			
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				var _data = graphic.attributes.data;
				var type = graphic.attributes.type;
				var symbol = graphic.symbol;
				
				var RGBLineColor = defRGBLineColor;
				var RGBAreaColor = defRGBAreaColor;
				var RGBNameColor = defRGBNameColor;
				
				if(_data.lineColor != "" ){
					RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
				}
				
				if(_data.areaColor != "" ){
					RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
				}
				
				if (_data.nameColor != "") {
					RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
				}
				
				//div形式提示清空
				if (ptiplayer != null) {
					ptiplayer.innerHTML = "";
				}
				
				// 楼宇  无填充 不显示边界
				if (type == 0) {
					// 不显示文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([0, 0, 0, 0]));
					textGraphic.setSymbol(textSymbol);
				} else if (type == 1) {
					symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0])); // 边界
					graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
					
					// 不显示文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 0]));
					textGraphic.setSymbol(textSymbol);
				} else { // 网格 无填充
					graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
				}
				
				graphic.setSymbol(symbol);
			}
		}
		
	};
	/**
	 *地图轨迹
	 realTime 实时
	 Playback 回放
	 */
	var trajectoryDiv;
	$.fn.ffcsMap.trajectory = function(trajectoryType,title,layerName,dataUrl,lineColor,imgUrl,imgWidth,imgHeight,intervalTime,startTime,endTime) {//样式默认定义
		if (document.getElementById('closeTrajectoryDivSpan') != undefined) {
			document.getElementById('closeTrajectoryDivSpan').click();
		}
		
		var innerDataUrl=dataUrl;
		var polyline = new Polyline();
		var IMGpoint = new Point(0, 0);
		var bottom  = 10;
		var left = (map.width-200) / 2;
		var n=1;
		var points = [];
		var linePoints = [];
		var lineStyle = "solid"; // 边框样式
		var defLineColor = "#FF0000";// 边框颜色 [138,43,226];  // 边框颜色
		var datas;
		var dynamicDatas;
		var timing;
		var dynamicTiming;
		
		if(lineColor != undefined && lineColor != null) {
			defLineColor = lineColor;
		}
		// 十六进制颜色转为RGB
		var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
		var lineWidth = 2;
		
		var trajectoryLayer;
		if (map.getLayer(layerName) == undefined) {
			trajectoryLayer = new esri.layers.GraphicsLayer({id:layerName});
			map.addLayer(trajectoryLayer);
		} else {
			trajectoryLayer = map.getLayer(layerName);
		}
		if("realTime"==trajectoryType) {
			var urlstrs = dataUrl.split("?");
			var paramArrays = urlstrs[1].split("&");
			innerDataUrl = urlstrs[0];
			for(var i=0;i<paramArrays.length;i++) {
				if(paramArrays[i].indexOf("locateTimeBegin")>=0) {
					paramArrays[i] = "locateTimeBegin="+startTime;
				}
				if(i==0){
					innerDataUrl+="?"+paramArrays[i];
				}else {
					innerDataUrl+="&"+paramArrays[i];
				}
			}
		}
		$.getJSON(innerDataUrl, function(data) {
			datas = data;
			if("realTime"==trajectoryType) {
				if(datas != null && datas.length>0) {
					if (trajectoryDiv != null) {
						trajectoryDiv.innerHTML = "";
					}
					showTrajectoryDiv();
					for(var i=0;i<datas.length;i++) {
						linePoints[i]= new Point(datas[i].x,datas[i].y);
					}
					polyline.addPath(linePoints);
					var symbol = new SimpleLineSymbol(lineStyle, new Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
					var graphic = new esri.Graphic(polyline, symbol);
					graphic.setAttributes( {"layerName":layerName});
					IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
					var IMGsymbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					var IMGgraphic = new Graphic(IMGpoint, IMGsymbol);
					trajectoryLayer.add(graphic);
					trajectoryLayer.add(IMGgraphic);
					playTrajectory(intervalTime);
					//document.getElementById("centerTimeDiv").innerHTML=datas[datas.length-1].locateTime;
					$.fn.ffcsMap.centerAt({x:datas[datas.length-1].x,y:datas[datas.length-1].y,zoom:-1});
				}else{
					alert("无定位数据");
				}
			}else if("playBack"==trajectoryType) {
				if(datas != null && datas.length>0) {
					if (trajectoryDiv != null) {
						trajectoryDiv.innerHTML = "";
					}
					showTrajectoryDiv();
					linePoints[0]= new Point(datas[0].x,datas[0].y);
					
					polyline.addPath(linePoints);
					n=1;
					var symbol = new SimpleLineSymbol(lineStyle, new Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
					var graphic = new esri.Graphic(polyline, symbol);
					graphic.setAttributes( {"layerName":layerName});
					IMGpoint.update(datas[0].x,datas[0].y);
					var IMGsymbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					var IMGgraphic = new Graphic(IMGpoint, IMGsymbol);
					trajectoryLayer.add(graphic);
					trajectoryLayer.add(IMGgraphic);
					playTrajectory(intervalTime);
					$.fn.ffcsMap.centerAt({x:datas[0].x,y:datas[0].y,zoom:-1});
				}else{
					alert("该时间段无轨迹定位数据");
				}
			}
		});
		
		function dynamicTrajectory(currentN){
			$.fn.ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"})
			$.fn.ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"})
			/*
			if(currentN >= n) {
				for(var i=n;i<=currentN;i++) {
					polyline.insertPoint(0,i,new Point(datas[i].x,datas[i].y));
				}
			}else if(currentN<n-1) {
				for(var i=n-1;i>currentN;i--) {
					polyline.removePoint(0,i);
				}
			}
			IMGpoint.update(datas[currentN].x,datas[currentN].y);
			*/
			linePoints = [];
			for(var i=0;i<=currentN;i++) {
				linePoints[i]= new Point(datas[i].x,datas[i].y);
			}
			polyline.removePath(0);
			polyline.addPath(linePoints);
			var symbol = new SimpleLineSymbol(lineStyle, new Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
			var graphic = new esri.Graphic(polyline, symbol);
			graphic.setAttributes( {"layerName":layerName});
			IMGpoint.update(datas[currentN].x,datas[currentN].y);
			var IMGsymbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
			var IMGgraphic = new Graphic(IMGpoint, IMGsymbol);
			trajectoryLayer.add(graphic);
			trajectoryLayer.add(IMGgraphic);
			
			
			trajectoryLayer.redraw();
			n=currentN;
			runTrajectoryProgress()
			n++;
			
		}
		function playTrajectory(time){
			var imgobj = document.getElementById('playButton');
			if(imgobj.src.indexOf("gj_09.png")>0) {
				imgobj.src = imgobj.src.replace("gj_09.png","gj_07.png")
			}
			if("realTime"==trajectoryType) {
				timing = setInterval(function(){
					if("realTime"==trajectoryType) {
						var urlstrs = dataUrl.split("?");
						var paramArrays = urlstrs[1].split("&");
						innerDataUrl = urlstrs[0];
						for(var i=0;i<paramArrays.length;i++) {
							if(paramArrays[i].indexOf("locateTimeBegin")>=0) {
								var now = new Date(); 
								now = new Date(now.getTime() - time*1000);
								var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
								paramArrays[i] = "locateTimeBegin=";
							}
							if(i==0){
								innerDataUrl+="?"+paramArrays[i];
							}else {
								innerDataUrl+="&"+paramArrays[i];
							}
						}
					}
					$.getJSON(innerDataUrl, function(data) {
						dynamicDatas=data;
						if(dynamicDatas.length>0){
							for(var i=0;i<dynamicDatas.length;i++){
								datas[datas.length] = dynamicDatas[i];
								//polyline.insertPoint(0,datas.length-1,new Point(dynamicDatas[i].x,dynamicDatas[i].y));
							}
							$.fn.ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"})
							$.fn.ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"})
							IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
							linePoints = [];
							for(var i=0;i<datas.length;i++) {
								linePoints[i]= new Point(datas[i].x,datas[i].y);
							}
							polyline.removePath(0);
							polyline.addPath(linePoints);
							var symbol = new SimpleLineSymbol(lineStyle, new Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
							var graphic = new esri.Graphic(polyline, symbol);
							graphic.setAttributes( {"layerName":layerName});
							IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
							var IMGsymbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
							var IMGgraphic = new Graphic(IMGpoint, IMGsymbol);
							trajectoryLayer.add(graphic);
							trajectoryLayer.add(IMGgraphic);
							trajectoryLayer.redraw();
							//document.getElementById("centerTimeDiv").innerHTML=datas[datas.length-1].locateTime;
						}
					});
				},time*1000);
				dynamicTiming = setInterval(function(){
					var now = new Date(); 
					var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
					document.getElementById("realTimeSpan").innerHTML=nowStr;
				},5*1000);
				
			}else if("playBack"==trajectoryType) {
				var imgobj = document.getElementById('playButton');
				if(imgobj.src.indexOf("gj_09.png")>0) {
					imgobj.src = imgobj.src.replace("gj_09.png","gj_07.png")
				}
				timing = setInterval(function(){
					if( n<datas.length) {
						
						//polyline.insertPoint(0,n,new Point(datas[n].x,datas[n].y));
						//IMGpoint.update(datas[n].x,datas[n].y);
						$.fn.ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"})
						$.fn.ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"})
						linePoints = [];
						for(var i=0;i<=n;i++) {
							linePoints[i]= new Point(datas[i].x,datas[i].y);
						}
						var aa = polyline.removePath(0);
						polyline.addPath(linePoints);
						var symbol = new SimpleLineSymbol(lineStyle, new Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
						var graphic = new esri.Graphic(polyline, symbol);
						graphic.setAttributes( {"layerName":layerName});
						IMGpoint.update(datas[n].x,datas[n].y);
						var IMGsymbol = new esri.symbols.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
						var IMGgraphic = new Graphic(IMGpoint, IMGsymbol);
						trajectoryLayer.add(graphic);
						trajectoryLayer.add(IMGgraphic);
						
						trajectoryLayer.redraw();
						document.getElementById("centerTimeDiv").innerHTML=datas[n].locateTime;
						runTrajectoryProgress()
						n++;
					}else {
						stopTrajectory();
					}
				},time*1000);
			}
		}
		function stopTrajectory(){
			var imgobj = document.getElementById('playButton');
			window.clearInterval(timing);
			if("realTime"==trajectoryType) {
				window.clearInterval(dynamicTiming);
			}
			if(imgobj.src.indexOf("gj_07.png")>0) {
				imgobj.src = imgobj.src.replace("gj_07.png","gj_09.png")
			}
		}
		function runTrajectoryProgress(){
			
			var progress = Math.round((n)*100/(datas.length-1));
			if(progress <2){
				progress = 2;
			}
			document.getElementById("trajectoryProgress").style.width=progress.toString()+"%";
			
		}
		function closeTrajectoryDiv(){
			if (trajectoryDiv!= null) { 
				window.clearInterval(timing);
				if("realTime"==trajectoryType) {
					window.clearInterval(dynamicTiming);
				}
				document.getElementById('trajectoryDiv').innerHTML='';
				$.fn.ffcsMap.clear({layerName : "gridAdminTrajectoryLayer"})
				$.fn.ffcsMap.clear({layerName : "careRoadmemberTrajectoryLayer"})
			}
			
		}
			
	    function getX(obj){  
	        var parObj=obj;    
	        var left=obj.offsetLeft;    
	        while(parObj=parObj.offsetParent){    
	            left+=parObj.offsetLeft;    
	        }    
	        return left;    
	    }    
	    
	    function getY(obj){    
	        var parObj=obj;    
	        var top=obj.offsetTop;    
	        while(parObj = parObj.offsetParent){    
	            top+=parObj.offsetTop;    
	        }    
	     return top;    
	    }    
	    
	    function trajectoryProgressClick(event){
	        var top,left,oDiv;    
	        oDiv=document.getElementById("trajectoryProgressLine");   
	        //获取进度条div的位置 
	        top=getY(oDiv);
	        left=getX(oDiv);
	        
	        var progressX = (event.clientX-left+document.body.scrollLeft)-2; 
	        var currentN = parseInt((progressX/500)*datas.length);
	        dynamicTrajectory(currentN);
	        //document.getElementById("mp_y").innerHTML = (event.clientY-top+document.body.scrollTop) -2+"px";   
	    }  
		
		/**
		 * 轨迹弹出框
		 * 
		 */
		function showTrajectoryDiv() {
		    var stylediv1 = document.body;				
			
			if (trajectoryDiv!= null) {
				trajectoryDiv.innerHTML = "";
				trajectoryDiv.style.left = document.body.offsetLeft + 'px';
				trajectoryDiv.style.top = document.body.offsetTop + 'px';
				trajectoryDiv.style.width = stylediv1.style.width;
				trajectoryDiv.style.height = stylediv1.style.height;
				trajectoryDiv.style.overflow = "hidden";
			}else {
				trajectoryDiv = document.createElement("div");
				trajectoryDiv.id = "trajectoryDiv";
				trajectoryDiv.style.left = document.body.offsetLeft + 'px';
				trajectoryDiv.style.top = document.body.offsetTop + 'px';
				trajectoryDiv.style.width = stylediv1.style.width;
				trajectoryDiv.style.height = stylediv1.style.height;
				trajectoryDiv.style.overflow = "hidden";  
				stylediv1.appendChild(trajectoryDiv);
			}
		    if(startTime == null) {
		    	startTime='';
		    }
		    if(endTime == null) {
		    	endTime='';
		    }
			var mystr2 = "<div class='NorMapOpenDiv1' style='width:520px; margin-left:-260px;left: " + left + "px;bottom:" + bottom + "px'>";
			mystr2 += "<div class='box' style='width:520px;'>";
			mystr2 += "<div class='title'>";
			
			mystr2 += "<span id='closeTrajectoryDivSpan' class='fr close'></span>";
			mystr2 += title;
			mystr2 +="</div>";
			if("realTime"==trajectoryType) {
				mystr2 +="<div class=\"con\">";
				mystr2 +="<div class=\"RealTime\">";
		        mystr2 +="<p>"+startTime+" — <span id=\"realTimeSpan\">"+endTime+"</span></p>";
		        mystr2 +="<p><a href=\"javascript:void(0)\" style=\"background:#751ece;\"><img title=\"播放/暂停\" id=\"playButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_07.png\" /></a></p>";
		        mystr2 +="</div></div>";
			}else if("playBack"==trajectoryType) {
				mystr2 +="<div class=\"con trajectory\">";
		        mystr2 +=	"<div class=\"time\">";
		        mystr2 +=		"<div class=\"begin fl\">"+startTime+"</div>";
		        mystr2 +=		"<div id=\"centerTimeDiv\" class=\"center fl\"></div>";
		        mystr2 +=		"<div id=\"endTimeDiv\" class=\"end fr\">"+endTime+"</div>";
		        mystr2 +=		"<div class=\"clear\"></div>";
				mystr2 +=	"</div>";
		        mystr2 +=	"<div class=\"schedule\">";
		        mystr2 +=		"<div id=\"trajectoryProgressLine\" class=\"line\">";
				mystr2 +=			"<div id=\"trajectoryProgress\" class=\"rate\" style=\"width:2%;\">";
		        mystr2 +=                	"<div class=\"control\"></div>";
		        mystr2 +=           "</div>";
		        mystr2 +=       "</div>";
		        mystr2 +=   "</div>";
		        mystr2 +=   "<div class=\"tools\">";
		        mystr2 +=    	"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
				mystr2 +=       	"<tr>";
				mystr2 +=           	"<td><a href=\"javascript:void(0)\" style=\"background:#c8a5eb;\"><img  title=\"到起点\" id=\"startPointButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_04.png\" /></a></td>";
				mystr2 +=               "<td align=\"center\"><a href=\"javascript:void(0)\" style=\"background:#9e61dd;\"><img title=\"关闭\" id=\"closeButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_06.png\" /></a></td>";
				mystr2 +=               "<td width=\"120\" align=\"center\"><a href=\"javascript:void(0)\" style=\"background:#751ece;\"><img title=\"播放/暂停\" id=\"playButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_07.png\" /></a></td>";
				mystr2 +=               "<td align=\"center\"><a href=\"javascript:void(0)\" style=\"background:#9e61dd;\"><img title=\"快进\" id=\"quickButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_08.png\" /></a></td>";
				mystr2 +=               "<td align=\"right\"><a href=\"javascript:void(0)\" style=\"background:#c8a5eb;\"><img title=\"到终点\" id=\"endPointButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_05.png\" /></a></td>";
				mystr2 +=           "</tr>";
		        mystr2 +=       "</table>";
		        mystr2 +=    "</div>";
		        mystr2 +="</div>"
			}
			
			
			mystr2 += "</div>";
			mystr2 += "</div>"; 
			
			
			trajectoryDiv.innerHTML = mystr2;
			
				
			document.getElementById('closeTrajectoryDivSpan').onclick = function() {
				closeTrajectoryDiv();
			};
			document.getElementById('playButton').onclick = function() {
				var imgobj = document.getElementById('playButton');
				if(imgobj.src.indexOf("gj_09.png")>0) {
					playTrajectory(intervalTime);
				}else if(imgobj.src.indexOf("gj_07.png")>0) {
					stopTrajectory();
				}
			};
			if("playBack"==trajectoryType) {
				document.getElementById('startPointButton').onclick = function() {
					dynamicTrajectory(0);
				};
				document.getElementById('closeButton').onclick = function() {
					closeTrajectoryDiv();
				};
				
				document.getElementById('quickButton').onclick = function() {
					stopTrajectory();
					playTrajectory(intervalTime/4);
				};
				document.getElementById('endPointButton').onclick = function() {
					dynamicTrajectory(datas.length-1);
				};
				document.getElementById('trajectoryProgressLine').onclick = function(event) {
					trajectoryProgressClick(event);
				};
			}
			
		}
	}

	// 对Date的扩展，将 Date 转化为指定格式的String   
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
// 例子：   
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}
	/**
	 * 
	 * 地图区域提示
	 */
	$.fn.ffcsMap.popup = function(){
		dialog = new TooltipDialog({
            id: "tooltipDialog",
            style: "position: absolute; width: 250px; font: normal normal normal 10pt Helvetica;z-index:1000"
          });
       dialog.startup();
       
       southCarolinaCounties.on("mouse-over", function(evt){
          var t = "<b>${NAME}</b><hr><b>2000 Population: </b>${POP2000:NumberFormat}<br>"
            + "<b>2000 Population per Sq. Mi.: </b>${POP00_SQMI:NumberFormat}<br>"
            + "<b>2007 Population: </b>${POP2007:NumberFormat}<br>"
            + "<b>2007 Population per Sq. Mi.: </b>${POP07_SQMI:NumberFormat}";
          var content = esriLang.substitute(evt.graphic.attributes,t);
          var highlightGraphic = new Graphic(evt.graphic.geometry,highlightSymbol);
          map.graphics.add(highlightGraphic);
          
          dialog.setContent(content);

          domStyle.set(dialog.domNode, "opacity", 0.85);
          //加入样式 ，才正常显示。
          dijitPopup.open({
            popup: dialog, 
            x: evt.pageX,
            y: evt.pageY
          });
        });
	};
	
	/***
	 * 画图
	 * 参数：drawType为作图类型
	 */
	$.fn.ffcsMap.draw = function(drawType, callBack){
		isDrawEdit = false;
		editToolbar.deactivate();
		toolbar.activate(Draw[drawType]);
		if(drawType == "POINT"){
			//编辑时隐藏圆的对象
			$.fn.ffcsMap.hideCircleOnDraw(false);
		 }
		toolbar.on("draw-complete", function() {
			drawData = $.fn.ffcsMap.getShapeInfo(returnDataGraphic);
			returnDataGraphic = null;
			
			if (drawData != null) {
				//console.log("后台：新建图形后返回的数据 : " + drawData);
				callBack(drawData); // 回调函数
			}
			
			// 转成json格式
			/*drawData = eval('(' + drawData + ')');
			
			var length = drawData.coordinates[0].length;
			
			// 在鼠标双击的位置添加默认文字
			var x = drawData.coordinates[0][length - 2][0];
			var y = drawData.coordinates[0][length - 2][1];
			
			var point = new esri.geometry.Point(x, y);
			// 文字样式
			var textSymbol = new esri.symbol.TextSymbol("文本").setColor(
						        new dojo.Color([255,0,0])).setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).setFont(
						        new esri.symbol.Font("14px").setWeight(esri.symbol.Font.WEIGHT_BOLD));
		
			// 构造文字Graphic
			var graphicsText = new esri.Graphic(point, textSymbol);
		
			// 显示文字
			map.graphics.add(graphicsText);*/
		});
	};
	
	/**
	 * 
	 * @param  isEdit  = true
	 * @param  callBack(data)  data：返回的json格式数据
	 */
	$.fn.ffcsMap.edit = function(isEdit,callBack){
		// 获取graphicsLayer图层Id
		var graphicsLayerIds = map.graphicsLayerIds;
		
		for (var i = 0; i < graphicsLayerIds.length; i++) {
			var layer = map.getLayer(graphicsLayerIds[i]);
			layer.on("click", function(evt) {
				//begin 控制轮廓数据是否不编辑 ----------
				var _isedit = true;
				
				var od_isedit;
				if (evt.graphic.attributes != null) {
					od_isedit  = evt.graphic.attributes.data._oldData["editAble"];
				}
				if(od_isedit != undefined){
					_isedit = od_isedit;
				}
				//end-----------------------------
	            if(isDrawEdit && _isedit){ 
	            	event.stop(evt);
	            	activateToolbar(evt.graphic);
	            	_graphic = evt.graphic;
	            }
	        });
		}
		
		var _graphic = null;
		
	    isDrawEdit = isEdit;
	    
	    //Activate the toolbar when you click on a graphic
        map.graphics.on("click", function(evt) {
            if(isDrawEdit){ 
            	event.stop(evt);
            	activateToolbar(evt.graphic);
				_graphic = evt.graphic;
            }
        });
        
        // 拖动
        editToolbar.on("graphic-move-stop", function(){
        	editStop();
        });
        
        // 缩小放大
        editToolbar.on("scale-stop", function(){
        	editStop();
        });
        
        // 旋转
        editToolbar.on("rotate-stop", function(){
        	editStop();
        });
        
        function editStop() {
        	if(isDrawEdit){ 
        		editToolbar.deactivate();
        		var data = $.fn.ffcsMap.getShapeInfo(_graphic);
        		
        		_graphic = null;
        		
        		if (data != null) {
        			//console.log("后台：编辑后返回的数据 : " + data);
        			callBack(data); // 回调函数
        			isDrawEdit = false; // should be click edit button again when you want to edit graphic
        		}
        	}
        }
	        
        //激活编辑工具 
	    function activateToolbar(graphic) {
	          var tool = 0;
	          
	          //if (registry.byId("tool_move").checked) {
	            tool = tool | Edit.MOVE; 
	          //}
	          //if (registry.byId("tool_vertices").checked) {
	            tool = tool | Edit.EDIT_VERTICES; 
	          //}
	          //if (registry.byId("tool_scale").checked) {
	            tool = tool | Edit.SCALE; 
	          //}
	          //if (registry.byId("tool_rotate").checked) {
	            tool = tool | Edit.ROTATE; 
	          //}
	          // enable text editing if a graphic uses a text symbol
	          if ( graphic.symbol.declaredClass === "esri.symbol.TextSymbol" ) {
	            tool = tool | Edit.EDIT_TEXT;
	          }
	          //specify toolbar options        
	          var options = {
	            allowAddVertices: true,
	            allowDeleteVertices: true,
	            uniformScaling: true
	          };
	          
	          editToolbar.activate(tool, graphic, options);
	    }
	};
	
	/***
	 * 
	 * 获取信息窗口模板
	 */
	$.fn.ffcsMap.getInfoWinTemplate = function(templateCfg){
		 var w = 300, h=230;
		 var _w = w + 270  , _h = h + 70;
		 var defaultCfg = {
				 title : "<b>${approxacre}</b>",
				 content : "${approxacre} content.<br> " +
				 		   "frame.<br>" +
				 		   "<iframe frameborder=0 width="+ _w +" height="+ _h +" marginheight=0 marginwidth=0 scrolling=yes " +
				 		   "src='"+templateCfg.url+"'></iframe>",
				 url : "",
				 layerUrl : "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSFields/FeatureServer/0",
				 w : w,
				 h : h
				  
		 };
		 var allCfg = $.extend({}, defaultCfg, templateCfg);
	     var template = new InfoTemplate();
	     template.setTitle(allCfg.title);
	     template.setContent(allCfg.content);
	     
	     return template;
	};
	
	/**
	 * 
	 * 为map设置信息窗口
	 */
	$.fn.ffcsMap.setInfoWindow = function(templateCfg){
		 var _infoWindow = new InfoWindowLite(null, domConstruct.create("div", null, null, map.root));
	     _infoWindow.startup();
	     map.setInfoWindow(_infoWindow);
	     map.infoWindow.resize(templateCfg.w, templateCfg.h);
	};
	
	/**
	 * 属性修改
	 */
	$.fn.ffcsMap.attributeEdit = function(templateCfg){
		 var _infoTemplate = $.fn.ffcsMap.getInfoWinTemplate(templateCfg);
		 //设置信息窗口
	     $.fn.ffcsMap.setInfoWindow(templateCfg);
		 //map.on("layers-add-result", initSelectToolbar);
	     var url = "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSFields/FeatureServer/0";
	     //var url = "http://services.arcgis.com/V6ZHFr6zdgNZuVG0/arcgis/rest/services/Street_Trees/FeatureServer/0";
		 var petroFieldsFL = new FeatureLayer(url, {
	          mode: FeatureLayer.MODE_ONDEMAND, //MODE_SELECTION,
	          infoTemplate : _infoTemplate,
	          outFields: ["approxacre","objectid","field_name","activeprod","cumm_oil","cumm_gas","avgdepth"]
//	          outFields: ["*"] // 获取图层中所有字段的值
	        });
		 
	     var selectionSymbol = new SimpleFillSymbol(
	          SimpleFillSymbol.STYLE_NULL, 
	          new SimpleLineSymbol(
	            "solid", 
	            new Color("yellow"), 
	            4
	          ),
	          null
	        );
	        petroFieldsFL.setSelectionSymbol(selectionSymbol);

	        petroFieldsFL.on("edits-complete", function() {
	          petroFieldsMSL.refresh();
	        });

//	     map.addLayers([petroFieldsFL]);
	     map.addLayer(petroFieldsFL);
	        
		 function initSelectToolbar(evt) {
	          var petroFieldsFL =  evt.layers[0].layer;
	          var selectQuery = new Query();

	          map.on("click", function(evt) {
	            selectQuery.geometry = evt.mapPoint;
	            petroFieldsFL.selectFeatures(selectQuery, FeatureLayer.SELECTION_NEW, function(features) {
	              if (features.length > 0) {
	               //store the current feature
	                updateFeature = features[0];
	                map.infoWindow.setTitle(features[0].getLayer().name);
	                map.infoWindow.show(evt.screenPoint,map.getInfoWindowAnchor(evt.screenPoint));
	              } else {
	                map.infoWindow.hide();
	              }
	            });
	          });

	          map.infoWindow.on("hide", function() {
	            petroFieldsFL.clearSelection();
	          });

	          var layerInfos = [{
	            'featureLayer': petroFieldsFL,
	            'showAttachments': false,
	            'isEditable': true,
	            'fieldInfos': [
	              {'fieldName': 'activeprod', 'isEditable':true, 'tooltip': 'Current Status', 'label':'Status:'},
	              {'fieldName': 'field_name', 'isEditable':true, 'tooltip': 'The name of this oil field', 'label':'Field Name:'},
	              {'fieldName': 'approxacre', 'isEditable':false,'label':'Acreage:'},
	              {'fieldName': 'avgdepth', 'isEditable':false, 'label':'Average Depth:'},
	              {'fieldName': 'cumm_oil', 'isEditable':false, 'label':'Cummulative Oil:'},
	              {'fieldName': 'cumm_gas', 'isEditable':false, 'label':'Cummulative Gas:'}
	            ]
	          }];

	          var attInspector = new AttributeInspector({
	            layerInfos:layerInfos
	          }, domConstruct.create("div"));
	          
	          //add a save button next to the delete button
	          var saveButton = new Button({ label: "Save", "class": "saveButton"});
	          domConstruct.place(saveButton.domNode, attInspector.deleteBtn.domNode, "after");
	         
	          saveButton.on("click", function(){
	            updateFeature.getLayer().applyEdits(null, [updateFeature], null);         
	          });
	          
	          attInspector.on("attribute-change", function(evt) {
	            //store the updates to apply when the save button is clicked 
	            updateFeature.attributes[evt.fieldName] = evt.fieldValue;
	          });
	          
	          attInspector.on("next", function(evt) {
	            updateFeature = evt.feature;
	            console.log("Next " + updateFeature.attributes.objectid);
	          });
	          
	          attInspector.on("delete", function(evt){
	            evt.feature.getLayer().applyEdits(null,null,[feature]);
	            map.infoWindow.hide();
	          });

	          map.infoWindow.setContent(attInspector.domNode);
	          map.infoWindow.resize(350, 240);
	     }
	};
	
	
	/**
	 * 数据转换工具(数据旧格式转新格式)
	 * 
	 * @param OldData 旧数据  格式：{id:461,isEdit:true,wid:110750,name:aaa,x:112,y:454,hs: "44.3,77.3",mapt:5,lineColor:"#ffff",lineWidth:1,areaColor:"",nameColor:"",infoOrgCode:"340205002",gridCode:"350205002",gridName:"",gridPath:"",mapCenterLevel: ''} 
	 * @param isJson 是否返回是json数据 true为json
	 * 
	 * @return 新格式：{"x":103.82705,"y":36.06408,"type":"louyu1","coordinates":[[[103.68251929819973,36.09960573005435],[103.72371803390717,36.09993270285838],[103.72862264168373,36.062984631938775],[103.69036667692865,36.06036883902914],[103.69036667692865,36.06036883902914],[103.68251929819973,36.09960573005435]]]}
	 * 
	 */
	$.fn.ffcsMap.DataCvtUtil = function(OldData,isJson){
		
		var newObj = {};
		
		newObj.x = OldData.x;
		newObj.y = OldData.y;
		
		newObj.type = OldData.type ;//"louyu1"; 网格类型
		newObj.coordinates = [];
		
		var temp_hs= new Array();

		if(OldData.hs != undefined){
			temp_hs = OldData.hs.split(',');
		}
		
		//alert("长度："+temp_hs.length);
		 
		var tempCrd = [];
		
		//面数据
		if(temp_hs != null || temp_hs != ""){
			var flag = 1;
			var tempCrdXY = [];
			for(var i = 0 ; i<temp_hs.length ; i++){
				// 转成float
				temp_hs[i] = parseFloat(temp_hs[i]);
				tempCrdXY.push(temp_hs[i]);
				
				if(flag % 2 == 0){
					tempCrd.push(tempCrdXY);
					tempCrdXY = [];
				}
				
				flag++;
			}
			
			newObj.coordinates.push(tempCrd);
			
			newObj.lineColor = OldData.lineColor; //边界线颜色
			newObj.lineWidth = OldData.lineWidth; //边界线宽度
			newObj.areaColor = OldData.areaColor; //区域颜色
			newObj.nameColor = OldData.nameColor; //区域颜色
			newObj.gridName  = OldData.name;  //网格名称
			newObj._oldData = OldData;  
		}else{//点
			
		}
		
		if(isJson){
			return newObj;
		}
		
		return JSON.stringify(newObj);
	};
	
	
	/***
	 * 根据坐标在相应的点位置添加div
	 * 
	 * @param options:{
	 * 		points:[
	 * 				{x:119.30,y:26.08,div:div1},
	 * 				{x:121.30,y:26.08,div:div2}
	 * 				],
	 * 		wkid:2437,
	 * 		rootDivId:"pointDiv"
	 * }
	 * 
	 */
	$.fn.addPointDiv = function(options){ 
		var point = options.points;
		var x = options.x;
		var y = options.y;
		var div = options.div;//$("<div><span>测试div</span></div>");//options.div;
		var wkid = options.wkid;
		
		for(i in points){
			var x = points[i].x;
			var y = points[i].y;
			var div = points[i].div;//$("<div><span>测试div</span></div>");//options.div;
			
			var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid : wkid }));
			
	    	var screenPt = map.toScreen(point);
			
			div.css("position","absolute").css("top",screenPt.y).css("left",screenPt.x).css("z-index","9999");
			$(this	).append(div);
			
			map.on("pan-end",function(evt){
	        	div.css("top",div.offset().top+evt.delta.y).css("left",div.offset().left+evt.delta.x);
			});
			
			map.on("zoom-end",function(evt){
				var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid : wkid }));
		    	var screenPt = map.toScreen(point);
				div.css("position","absolute").css("top",screenPt.y).css("left",screenPt.x).css("z-index","9999");
			});
		}
			
	};
	
	/****
	 * $.fn.ffcsMap.addPointInfo 接口默认参数
	 * 
	 * */
	$.fn.ffcsMap.addPointDefaultOpts = {
		/*********全局范围和单个点范围均生效的参数*************/	
		color : [ 128, 0, 0 ],     //文字颜色
		fontSize : "15px",		   //文字大小
//		fontAlign:esri.symbol.TextSymbol.ALIGN_END,  //文字布局 ALIGN_END    
		fontAlign:"end",  //文字布局 ALIGN_END    
													 //ALIGN_START，ALIGN_MIDDLE
		fontAngle:0,                 //字体斜度
//		fontWeight:esri.symbol.Font.WEIGHT_BOLD,   //文字样式
		fontWeight:"weightbold",   //文字样式
		imageWidth:34,                             //图片默认宽度
		imageHeight:48,							   //图片默认高度
		rImageW : 60,                              //背景图片默认宽度
		rImageH : 50,                              //背景图片默认高度
		iOffsetX : 46,							   //背景图片默认水平偏移量
		iOffsetY : -6.2,                           //背景图片默认垂直偏移量
		tOffsetX : 64.1,                           //文字默认水平偏移量
		tOffsetY : -3.2,                           //文字默认垂直偏移量
		textImageUrl:"images/grid_admin_h_text.png", //文字背景图片路径
			
		/*********只有全局范围生效的参数*************/	
		wkid : 2437               //参考系	
	};
	
	
	/****
	 * 在相应的坐标点添加图片和文字
	 * {
	 *  points:[
	 *          {
	 *           x:557014.4071387239,  //点坐标
	 *           y:3234520.506465449,  //点坐标
	 *           imageUrl:"images/si.png",  //图片路径
	 *           text:"欧阳锋"              //文字，如果不传的话则文字和文字背景图片都不显示
	 *          },{
	 *           x:558014.4071387239,  //点坐标
	 *           y:3264520.506465449,  //点坐标
	 *           imageUrl:"images/du.png",  //图片路径
	 *           text:"郭靖",
	 *           color:"#000"                        //局部参数，只对该点生效
	 *          },
	 *          ],
	 *  fontSize:"18px",      //全局参数，对所有点生效
	 *  wkid:32648,           //参考系
	 * }
	 * */
	$.fn.ffcsMap.addPointInfo = function(options){ 
		
		//全局的参数，对于每个点均生效，但是如果点内配置了相同参数则会覆盖它
		options = $.extend({}, $.fn.ffcsMap.addPointDefaultOpts, options); 
		
		var points = options.points;
		var wkid = options.wkid;
		var layer = new esri.layers.GraphicsLayer(); //新加的图层，用于放置点
		
		for(i in points){
			//点范围的参数，只对这个点生效，该参数会覆盖全局的参数
			var pointOpt = $.extend({}, options, points[i]); 
			var text = pointOpt.text;
			var imageUrl = pointOpt.imageUrl;
			var point = new esri.geometry.Point(pointOpt.x, pointOpt.y, new esri.SpatialReference({ wkid : wkid }));
			
			var markerSymbol = new esri.symbols.PictureMarkerSymbol(
					pointOpt.imageUrl,pointOpt.imageWidth,pointOpt.imageHeight);
			var markerGraphic = new esri.Graphic(point, markerSymbol);
			
			layer.add(markerGraphic);
			
			if(text!==undefined && text!==""){
				var textMarkerSymbol = new esri.symbols.PictureMarkerSymbol(
						pointOpt.textImageUrl,pointOpt.rImageW,pointOpt.rImageH);
				textMarkerSymbol.setOffset(pointOpt.iOffsetX,pointOpt.iOffsetY);
				
				var textSymbol =  new esri.symbol.TextSymbol(text).setColor(
			            new dojo.Color(pointOpt.color)).setAlign(pointOpt.fontAlign).setAngle(pointOpt.fontAngle).setFont(  
			            new esri.symbol.Font(pointOpt.fontSize).setWeight(pointOpt.fontWeight));
				textSymbol.setOffset(pointOpt.tOffsetX,pointOpt.tOffsetY);
				
				var textMarkerGraphic = new esri.Graphic(point, textMarkerSymbol);
				var textGraphic = new esri.Graphic(point, textSymbol);
				
				layer.add(textMarkerGraphic);
				layer.add(textGraphic);
			}
		}
		
		map.addLayer(layer);
		
	};
	
	/**
	 * 地图中心点定位及缩放工具
	 * 参数：
	 * {
	 *	x : -95.249,          //中心点X坐标
	 *	y : 38.954,           //中心点y坐标
	 *	zoom:5                      //缩放级别 0以下表示不缩放
	 *	}
	 * */
	$.fn.ffcsMap.centerAt = function(options){
		var wkid = map.spatialReference["wkid"];
		
		var point = new esri.geometry.Point([options.x,
		                                     options.y],
		                                     new esri.SpatialReference({wkid : wkid})
		                                     );
		if(options.zoom>0){
			map.centerAndZoom(point,options.zoom);
		}else{
			map.centerAt(point);
		}
		$.fn.ffcsMap.setScreenPostition();
		
	};
	
	/****
	 * $.fn.ffcsMap.createOverviewMap 创建鹰眼图接口参数
	 * 
	 * */
	$.fn.ffcsMap.createOverviewMapDefaultOpts = {
			/*位置*/
			attachTo : "bottom-right",
			
			/*透明度*/
			opacity : 0.5,
			
			/*宽度和高度*/
			height:120,
			width:175,
			visible:true
	};
	
	/**
	 * 创建鹰眼图
	 * 
	 */
	$.fn.ffcsMap.createOverviewMap = function(options){
		options = $.extend({map:map},$.fn.ffcsMap.createOverviewMapDefaultOpts, options);
		var overviewMap = new esri.dijit.OverviewMap(options);
		overviewMap.startup();
		overviewMap.show();
	};
	
	/**
	 * 
	 * graphic热点事件
	 * 鼠标触发提示、鼠标单击提示
	 * 
	 * @param opt 提示框宽高
	 * @param layerName 图层名称
	 * @param createDlg(oldData) 回调函数传入oldData对象,返回内容填充到对话框中.
	 * 
	 */
	var mend_runCount = 0;
	
	$.fn.ffcsMap.ffcsDisplayhot = function(opt, layerName, title, createDlg, showDetail){
		tempLayer = new esri.layers.GraphicsLayer({id:'tempLayer'});
		map.addLayer(tempLayer);
	
		var defa_opt = {
				w : 400,
				h : 200
		};
		
		opt = $.extend({}, defa_opt, opt);
		
		var graphics = map.graphicsLayerIds;
		
		var pointlocation ;
		
		for (var i = 0; i < graphics.length; i++) {
			var layer = map.getLayer(graphics[i]);
			
			layer.on("click",function(evt){			
				click(this, evt);
			});
			
			/*
			layer.on("mouse-over", function(evt) {
				mouseOver(evt);
			});
			
			layer.on("mouse-out", function(evt) {
				mouseOut(evt);
			});*/
		}
		
		map.on("zoom-end",function(e){
			if(document.getElementById("builddianpuDv")){
				document.getElementById('builddianpuDv').innerHTML='';
			}
			//if(document.getElementById("pointInfoDiv")){
				//document.getElementById('pointInfoDiv').innerHTML='';
			//}
		});
		
		//mouse-drag-start
		//map.on("mouse-drag-start", function(e) {
			//if(mend_runCount > 1){
				//mend_runCount = 0;
			//}
		//});
		//mouse-drag-end   pan-end
		
		function mouseOver(evt) {
			// 编辑状态不显示信息
			if (!isDrawEdit && evt.graphic.attributes != null) {
				//var g = evt.graphic;
				//var title = evt.graphic.attributes["title"];
				var title = evt.graphic.attributes.data["gridName"];
				var stylediv = document.body;
				if (hotlayer != null) {
					hotlayer.innerHTML = "";
					hotlayer.style.left = document.body.offsetLeft + 'px';
					hotlayer.style.top = document.body.offsetTop + 'px';
					hotlayer.style.width = stylediv.style.width;
					hotlayer.style.height = stylediv.style.height;
					hotlayer.style.overflow = "hidden";
					hotlayer.id = "hotlayer";
				} else {
					hotlayer = document.createElement("div");
					hotlayer.id = "hotlayer";
					hotlayer.style.left = document.body.offsetLeft + 'px';
					hotlayer.style.top = document.body.offsetTop + 'px';
					hotlayer.style.width = stylediv.style.width;
					hotlayer.style.height = stylediv.style.height;
					hotlayer.style.overflow = "hidden";
					stylediv.appendChild(hotlayer);
				}
				
				var top = evt.screenPoint.y;
				var left = evt.screenPoint.x + 10;
				var str = "<div class='hotlabel' style='left: " + left + "px; top:" + top + "px;  position:absolute; z-index: 990;height:17px;overflow:hidden;'>" + title + "</div>";
				hotlayer.innerHTML = str;
			}
		}
		
		function mouseOut(evt) {
			if (hotlayer != null) {
				hotlayer.innerHTML = "";
			}
			
			if (builddianpuDv != null) {
				builddianpuDv.innerHTML = "";
			}
		}
		
		function click(layer, evt) {
			if (layer.id == layerName || layer.id == 'tempLayer') {
				// 编辑状态不显示信息
				if (!isDrawEdit) {
					var resultInfo;
					//var title;
					var x;
					var y;
					var _data;
					var _tempWid;
					
					if (evt.graphic.attributes != null) {
						resultInfo = createDlg(evt.graphic.attributes.data["_oldData"]);
						//title = evt.graphic.attributes.data["gridName"];
						x = evt.graphic.attributes.data["x"];
						y = evt.graphic.attributes.data["y"];
					} else {
						var data = evt.graphic.geometry;
						resultInfo = createDlg(data);
						x = data.x;
						y = data.y;
					}
					$.fn.ffcsMap.centerAt({x:x,y:y,zoom:-1});//,zoom:map._mapParams.zoom
					var top ;//= map.toScreen(pointlocation).y ;
					var left ;//= map.toScreen(pointlocation).x ;
					top  = evt.clientY - opt.h - 51;
					left = evt.clientX - opt.w / 2;
					_data = evt.graphic.attributes.data["_oldData"];
					_tempWid = _data['wid'];
					if(resultInfo!=null && resultInfo!=""){
						$.fn.ffcsMap.initWindow(left, top, opt, title, resultInfo, true, showDetail, _tempWid);
					}
				}
			}
		}
	};
	
	/**
	 * 清除网格图层
	 * 
	 */
	$.fn.ffcsMap.clearGridsLayer = function() {
		document.getElementById('builddianpuDv').innerHTML='';
		$("#map").ffcsMap.clear({layerName : "gridsLayer"});
	}
	
	/**
	 * 定位弹出框
	 * 
	 */
	$.fn.ffcsMap.initWindow = function(left, top, opt, title, resultInfo, success, showDetail, _tempWid) {
	    var stylediv1 = document.body;				
		
		if (builddianpuDv!= null) {
			builddianpuDv.innerHTML = "";
			builddianpuDv.style.left = document.body.offsetLeft + 'px';		
			builddianpuDv.style.top = document.body.offsetTop + 'px';
			builddianpuDv.style.width = stylediv1.style.width;
			builddianpuDv.style.height = stylediv1.style.height;
			builddianpuDv.style.overflow = "hidden";
		}
		else {
			builddianpuDv = document.createElement("div");
			builddianpuDv.id = "builddianpuDv";
			builddianpuDv.style.left = document.body.offsetLeft + 'px';
			builddianpuDv.style.top = document.body.offsetTop + 'px';
			builddianpuDv.style.width = stylediv1.style.width;
			builddianpuDv.style.height = stylediv1.style.height;
			builddianpuDv.style.overflow = "hidden";  
			stylediv1.appendChild(builddianpuDv);
		}
	    
		var mystr2 = "<div id='NorMapOpenDiv' class='NorMapOpenDiv' style='left: " + left + "px;top:" + top + "px'>";
		
		if (success) {
			mystr2 += "<div class='arrow' style='left: " + (opt.w / 2) + "px;'></div>";
		}
		
		mystr2 += "<div class='box' style='width:"+opt.w+"px;height:"+opt.h+"px'>";
		mystr2 += "<div class='title'>";
		
		mystr2 += "<span id='close' class='fr close'></span>";
		
		// 是否显示详情
		if (showDetail != undefined) {
			mystr2 += "<span id='watch' title='查看详情' class='fr watch';></span>";
		}
		
		mystr2 += title;
		
		mystr2 +="</div>";
		
		mystr2 += "<span class='detail'></span>";
		mystr2 += "<div style='height:"+(opt.h-26)+"px' class='o_subst_info'>";
		mystr2 += resultInfo;
		mystr2 += "</div>";
		
		mystr2 += "</div>";
		
		mystr2 += "</div>"; 
		
		builddianpuDv.innerHTML = mystr2;
		if (showDetail != undefined) {
			document.getElementById('watch').onclick = function() {
				showDetail(_tempWid, title);
			};
		}
		
		document.getElementById('close').onclick = function() {
			$.fn.ffcsMap.clearGridsLayer();
		};
		
		$.fn.ffcsMap.fixedPan(left,top,success);
	}
	
	/**
	 * @see 功能说明：图层查询定位(根据名称,中心点定位，对应级别显示)
	 * 
	 * @param  config (配置说明)
	 * {
	 *    layerName : "楼宇",   //图层名
	 *    gridName : "楼宇1",   //1.查找的名称
	 *    id : 111 ,           //2.根据id查找
	 *    wid : 123 ,          //3.根据wid查找
	 *    zoom : 8             //显示级别
	 * }
	 */
	$.fn.ffcsMap.centerAtByLayerText =  function(config){
		
		var  x ,y,wkid; 
		var lys = map._layers;
		var gridLy = map._layers[config.layerName];
		
		wkid = gridLy.spatialReference.wkid;
		for(var i=0 ;i<gridLy.graphics.length; i++){
			var g = gridLy.graphics[i];
			if(g.attributes != undefined ){
				var  gridName = g.attributes.data["gridName"];
				var  q_id  = g.attributes.data._oldData["id"];
				var  q_wid = g.attributes.data._oldData["wid"];
				if (gridName == config.textName || q_wid == config.wid
						|| q_id == config.id) {
					var  x =  g.attributes.data["x"]; 
					var  y =  g.attributes.data["y"];
					break;
				}
			}
		}
		if(x != undefined){
			var zoom = 2;
			zoom =  config.zoom;
			$.fn.ffcsMap.centerAt({x:x,y:y,wkid:wkid,zoom:zoom});
		}
	};
	
	/**
	 * 点定位
	 * @param opt 提示框宽高
	 * @param layerName 图层名称
	 * @param wid
	 * @param 点名称
	 * @param imgUrl 图片地址
	 * @param callback 回调函数 定位成功返回true 失败返回false
	 */
	$.fn.ffcsMap.locationPoint = function(opt, layerName, wid, name, imgUrl, imgWidth,imgHeight, callback, showDetail) {
		var defa_opt = {
				w : 400,
				h : 200
		};
		opt = $.extend({}, defa_opt, opt); 
		
		var x; // x坐标
		var y; // y坐标
		var graphic; 
		var success = false; // 是否找到该定位点
		var tempLayer; // 临时图层
		var _data;
		var _tempWid;
		
		if (map.getLayer('tempLayer') == undefined) {
			tempLayer = new GraphicsLayer({id:'tempLayer'});
			map.addLayer(tempLayer);
		} else {
			tempLayer = map.getLayer('tempLayer');
		}
		
		// 当前定位的图层
		var currentLayer = map.getLayer(layerName);
		
		// 获取图层所有的点元素
		var graphics = currentLayer.graphics;
		
		for (var i = 0; i < graphics.length; i++) {
			// 当前点元素
			graphic = graphics[i];
			
			if(graphic.attributes != undefined) {
				var _wid = graphic.attributes.data._oldData["wid"];
				if (_wid != null && _wid == wid) {
					success = true;	// 定位成功
					x =  graphic.attributes.data["x"]; 
					y =  graphic.attributes.data["y"];
					//_data = graphic.attributes.data["_oldData"];
					_tempWid = _wid;
					break;
				} else {
					//_data = graphic.attributes.data["_oldData"];
					_tempWid = wid;
					graphic = null;
				}
			}else {
				_tempWid = wid;
				graphic = null;
			}
		}
		
		// 不管是否找到清除图层
		map.getLayer('tempLayer').clear();
			
		// 找到该点，定位
		var point;
		var rwkid = map.spatialReference["wkid"];
		if (success) {
			// 添加新图片
			//var symbol = new esri.symbols.PictureMarkerSymbol(imgUrl,imgWidth,imgHeight);
			point = new esri.geometry.Point(x, y,new esri.SpatialReference({ wkid : rwkid }));
			//var _graphic = new Graphic(point, symbol);
			//tempLayer.add(_graphic);
			
			//_graphic.attributes = graphic.attributes;
			
			// 以该点为中心点进行定位
			map.centerAt(point);
		}
		
		// 显示详细信息
		showInfo();
		
		function showInfo() {
			if (builddianpuDv != null) {
				builddianpuDv.innerHTML = "";
			}
			
			var resultInfo;
			var title;
			
			if (graphic != null) {
				graphic.attributes.data["_oldData"].success = success; // 设置是否成功
				resultInfo = callback(graphic.attributes.data["_oldData"]);
			}else{
				resultInfo = callback();
			}
			
			//if (success) {
				//title = graphic.attributes.data["gridName"];
			//} else {
				title = name;
			//}
			
			// 详细信息定位到地图中心
			var top  = success ? map.toScreen(point).y - opt.h - 51 : (map.height - opt.h) / 2;
			var left = success ? map.toScreen(point).x - opt.w / 2 : (map.width - opt.w) / 2;

			//top = (top > 40) ? top : 40;
			//left = (left > 70) ? left : 70;
			$.fn.ffcsMap.initWindow(left, top, opt, title, resultInfo, success, showDetail, wid);
		}
	};
	
	/**
	 * @see 功能说明：画圆时，隐藏之前的圆。
	 * 
	 * @param isShow是否显示, true显示  false隐藏
	 */
	$.fn.ffcsMap.hideCircleOnDraw = function(isShow){
		var gs = map.graphics.graphics;
		
		for(var i=0 ; i<gs.length ;i++){
			if(gs[i]._shape != undefined){
				var  temShape = gs[i]._shape.shape;
				if(temShape["type"] == "circle"){
					if(isShow){
						gs[i].show();
					}else{
						gs[i].hide();
					}
				}
			}
		}
		
	};
	
	/**
	 * 图层中对象双击接口
	 * @param callBack(data) 
	 */
	$.fn.ffcsMap.dbClickByLayer = function(callBack){
		var ids = map.graphicsLayerIds;
		for(var i =0 ; i<ids.length ; i++){
			var _ly = map.getLayer(ids[i]);
			
			_ly.on("dbl-click",function(evt){			
				callBack(evt.graphic.attributes.data["_oldData"]);
			});
		}
	};
	
	/**
	 * 在地图上移动鼠标的时候显示鼠标位置所在的坐标
	 * 
	 * @param callBack(mapPoint); 回调参数：地图的坐标x,y
	 * 
	 */
	$.fn.ffcsMap.showCoordinate = function(callBack){
	    map.on("mouse-move",showPointLocation);
	    
	    //将坐标显示到地图下方的div
        function showPointLocation(evt){
        	var mp = evt.mapPoint;
        	//var screenPt = map.toScreen(mp);
        	try{
        		callBack(mp);
        	}catch(e){
        		
        	}
        }
	};
	
	$.fn.ffcsMap.overviewMap = function() {
		var overviewMapDijit = new esri.dijit.OverviewMap({
			map : map,
			visible : true,
			attachTo : "bottom-right"
		});
		
		// 该方法在OverviewMap构造函数之后,用户与界面交互之前被调用
		overviewMapDijit.startup();
	}
	
	/**
	 * 天地图鹰眼
	 * 
	 */
	$.fn.ffcsMap.tdtOverviewMap = function() {
		var rwkid = map.spatialReference["wkid"];
		// 拖动层是否被按住
		var mousedown = false;
		
		// 鹰眼图div
		var overViewMapDiv = $("<div></div>");
		overViewMapDiv.attr("id", "overviewMap");
		overViewMapDiv.addClass("overviewMap");
		$(document.body).append(overViewMapDiv);
		var overViewMap = new esri.Map("overviewMap", { "nav": false, "logo": false, "slider": false});
		// 设置鹰眼图级别
		overViewMap.setZoom(map.getZoom() - 6);
		
		initTianDiTu(_layerIP, TianDiTu.VEC_BASE_GCS);
		initTianDiTu(_layerIP, TianDiTu.VEC_ANNO_GCS);
		
		// 禁用鹰眼事件
		overViewMap.disablePan();
		overViewMap.disableDoubleClickZoom();
		overViewMap.disableScrollWheelZoom();
		overViewMap.disableClickRecenter();
		overViewMap.disableShiftDoubleClickZoom();
		overViewMap.disableKeyboardNavigation();
		
		// 鹰眼拖动层
		var dragDiv = $("<div></div>");
		dragDiv.attr("id", "dragDiv");
		dragDiv.addClass("dragDiv");
		overViewMapDiv.append(dragDiv);
		
		// 拖动层默认显示位置为鹰眼图中心点
		var dragDivX = overViewMapDiv.width() / 2 - dragDiv.width() / 2; // 拖动层x位置
		var dragDivY = overViewMapDiv.height() / 2 - dragDiv.height() / 2 ; // 拖动层y位置
		
		// 设置拖动层位置
		dragDiv.css({
			left : dragDivX,
			top : dragDivY
		});
		
		// 按下拖动层
		$('#dragDiv').mousedown(function(evt) {
			mousedown = true;
			
			/* 获取需要拖动节点的坐标 */
			var offset_x = $(this)[0].offsetLeft;//x坐标
			var offset_y = $(this)[0].offsetTop;//y坐标
			
			/* 获取当前鼠标的坐标 */
			var mouse_x = evt.pageX;
			var mouse_y = evt.pageY;
			
			/* 绑定拖动事件 */
			/* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
			$(document).bind("mousemove",function(ev){
				/* 计算鼠标移动了的位置 */
				var _x = ev.pageX - mouse_x;
				var _y = ev.pageY - mouse_y;
				
				/* 设置移动后的元素坐标 */
				var now_x = (offset_x + _x);
				var now_y = (offset_y + _y);	
				
				/* 改变目标元素的位置 */
				dragDiv.css({
					top:now_y,
					left:now_x
				});
			});
			
			/* 当鼠标左键松开 */
			$(document).bind("mouseup",function(){
				if (mousedown) {
					// 屏幕坐标
					var pt = new esri.geometry.Point(
							$('#dragDiv')[0].offsetLeft	+ dragDiv.width() / 2,
							$('#dragDiv')[0].offsetTop + dragDiv.height() / 2,
							new esri.SpatialReference({ wkid: rwkid })
					);
					
					// 屏幕坐标转地理坐标
					var mp = overViewMap.toMap(pt); // 当前鼠标地理位置
					map.centerAt(mp);
					dragDiv.css({
						left : dragDivX,
						top : dragDivY
					});
					
					/* 解除事件绑定  */
					$(this).unbind("mousemove");
					mousedown = false;
				}
			});
		});
		
		// 主地图范围变化
		map.on("extent-change", function(evt) {
			// 获取新范围中心点
			var x = map.width / 2;
			var y = map.height / 2;
			
			// 屏幕坐标
			var pt = new esri.geometry.Point(x, y,new esri.SpatialReference({ wkid: rwkid }));
			var mp = map.toMap(pt);
			// 设置缩放级别
			overViewMap.setZoom(map.getZoom() - 6);
			// 设置鹰眼新中心点
			overViewMap.centerAt(mp);
		});
		
		function initTianDiTu(url, options) {
			dojo.declare("WMTSServiceLayer", esri.layers.TiledMapServiceLayer, {
	            constructor: function() {
	            	  // 空间参考系
	        		  this.spatialReference = new esri.SpatialReference({ wkid : options.wkid});
	        		  // 初始化范围 (左下角坐标，右上角坐标)
	        		  this.initialExtent = new esri.geometry.Extent(options.xmin, options.ymin, options.xmax, options.ymax, this.spatialReference);
	        		  // 全屏范围 
	                  this.fullExtent = new esri.geometry.Extent(options.xmin, options.ymin, options.xmax, options.ymax, this.spatialReference);
	                  // 切片信息
	        		  this.tileInfo = new esri.layers.TileInfo({
	        			  	"compressionQuality" : 0,
	        		        "rows" : options.rows, // 切片高
	        				"cols" : options.cols, // 切片宽
	        				"origin" : { "x" : options.origin.x,"y" : options.origin.y }, // 切图原点
	        				"spatialReference" : {"wkid" : options.wkid},// 空间参考系
	        				"lods" : LevelDetail.lods
	        		  });
	        		  
	        		  this.loaded = true;
	        		  this.onLoad(this);
	            },
	            
	            getTileUrl: function(level, row, col) {
	            	
	            	var prcUrl = "";
	            	var tileMatrix = level * 1 + 1 ;
	            	if(options.layerId=="MapServer"){
	            		if(options.tileMatrix  != undefined ){
	            			tileMatrix = level * 1 ;
	            		}
	            		prcUrl = url ;
	            	}else{
	            		prcUrl = url + options.layerId + "_c/wmts";
	            	}
	            	
	            	return  prcUrl + "?Service=WMTS&Request=GetTile&Version=1.0.0" +  
		                "&Style=Default&Format="+options.imageFormat+"&serviceMode="+options.serviceMode+"&layer="+options.layerId +  
		                "&TileMatrixSet="+options.tileMatrixSetId+"&TileMatrix=" + tileMatrix + "&TileRow=" + row + "&TileCol=" + col;
	            
	            }
			});
	            
			overViewMap.addLayer((new WMTSServiceLayer()));
		}
		
		// 鹰眼关闭打开图标
		var overviewOpenDiv = $("<div></div>");
	    overviewOpenDiv.attr("id", "overviewOpen");
	    overviewOpenDiv.addClass("overviewOpen");
		$(document.body).append(overviewOpenDiv);
		
		var overviewCloseDiv = $("<div></div>");
		overviewCloseDiv.attr("id", "overviewClose");
		overviewCloseDiv.addClass("overviewClose");
		$(document.body).append(overviewCloseDiv);
		$('#overviewClose').hide();
		
		$('#overviewMap').hide(); // 默认隐藏缩略图
		
		// 点击打开图标
		$('#overviewOpen').click(function() {
			$('#overviewMap').show(350); // 显示缩略图
			$(this).hide(); // 隐藏自己
			$('#overviewClose').show(); // 显示关闭图标
			overViewMap.setZoom(map.getZoom() - 6);
		});
		
		// 点击关闭图标
		$('#overviewClose').click(function() {
			$('#overviewMap').hide(350); // 隐藏缩略图
			$(this).hide(); // 隐藏自己
			$('#overviewOpen').show(); // 显示关闭图标
		});
		
	}
	
	/**
	 * 清除
	 * 
	 * @param config 配置图层名称后清除此图层 {layerName : "楼宇"}
	 * 
	 */
	$.fn.ffcsMap.clear = function(config){
		if(map.graphics != undefined ){
			map.graphics.clear();
		}
		if(config != undefined){
			var lys = map._layers;
			var gridLy = map._layers[config.layerName];
			if(gridLy != undefined) {
				gridLy.clear();
			}
		}
	};
	
	
	$.fn.ffcsMap.test = function(msg){
		alert(msg);
	};
	
	$.fn.ffcsMap.getMap = function(){
		return map;
	};
	
	$.fn.ffcsMap.getDraw = function(){
		return toolbar;
	};
	/**
	 * 切换地图类型
	 * @param type 地图类型
	 *     img 影像地图
	 *     vec 矢量地图
	 
	$.fn.ffcsMap.switchMap = function(type,mapFile) {
	  // 移除原来图层
	  var layerIds = map.layerIds;
	  
	  for (var i = 0; i < layerIds.length; i++) {
	    var layer = map.getLayer(layerIds[i]);
	    map.removeLayer(layer);
	  }
	  
	  // 影像地图
	  if (type == 'img') {
	      $.fn.ffcsMap.createLayer(mapFile, "wmts", TianDiTu.IMG_BASE_GCS);
	      $.fn.ffcsMap.createLayer(mapFile, "wmts", TianDiTu.IMG_ANNO_GCS);
	  } else if (type == 'vec') { // 普通地图
	    $.fn.ffcsMap.createLayer(mapFile, "wmts", TianDiTu.VEC_BASE_GCS);
	    $.fn.ffcsMap.createLayer(mapFile, "wmts", TianDiTu.VEC_ANNO_GCS);
	  }
	}*/
	
	/**
	 * 
	 * 创建一个DIV
	 * evt 事作对象
	 * divId  如： ptiplayer
	 * className 样式名　hotlabel
	 * tipContext 提示内容
	 * 调用：showCreateDiv(ptiplayer,hotlabel)
	 */
	$.fn.ffcsMap.showCreateDiv = function(evt,divId,className,tipContext){
		
		//点提示
		var pstylediv = document.body;
 
		if (ptiplayer != null) {
			ptiplayer.innerHTML = "";
			ptiplayer.style.left = document.body.offsetLeft + 'px';
			ptiplayer.style.top = document.body.offsetTop + 'px';
			ptiplayer.style.width = pstylediv.style.width;
			ptiplayer.style.height = pstylediv.style.height;
			ptiplayer.style.overflow = 'hidden';
			ptiplayer.id = "'" +divId+ "'";
 
		} else {
			ptiplayer = document.createElement("div");
			ptiplayer.id =  "'" +divId+ "'";
			ptiplayer.style.left = document.body.offsetLeft + 'px';
			ptiplayer.style.top = document.body.offsetTop + 'px';
			ptiplayer.style.width = pstylediv.style.width;
			ptiplayer.style.height = pstylediv.style.height;
			ptiplayer.style.overflow = 'hidden';
			pstylediv.appendChild(ptiplayer);
 
		}
		 
		var top = evt.screenPoint.y + 30;
		var left = evt.screenPoint.x + 10;
		var str = "<div class='"+className+"' style='left: " + left + "px; top:" + top + "px;  position:absolute; z-index: 990; font-size:12px; padding:3px 5px; overflow:hidden; background:#ffffd5; border:1px solid #808080;'>" + tipContext +"</div>";
		ptiplayer.innerHTML = str;
	};
	
	
	$.fn.ffcsMap.geteditToolbar = function(){
		return editToolbar;
	};
	
	$.fn.ffcsMap.fixedPan =  function(w,h,success){
		var moveX = 0 ,moveY = 0;
		console.log(w +","+ h+",success="+ success);
		 
		if(h > 40 && h < 120 ){
			moveY = -0.05 * map.height;
		}else if(h <= 40 && h > -150){
			moveY = - 0.23 * map.height;
		}else if(h <= -150){
			moveY = - 0.75 * map.height;
		}else{
			moveY = -0.01 * map.height;
		}
		
		if(w < -50 ){
			moveX = -0.5 * map.width;
		}else if( w < 77 && success ){
			moveX = -0.15 * map.width;
		}else if (w >= 340 && w < 583 && success){
			moveX = 0.18 * map.width;
		}else if (w >= 583){
			moveX = 0.21 * map.width;
		}else if (w >= 1000){
			moveX = 0.35 * map.width;
		}
		
		if(moveX != 0 || moveY !=0){
			map._fixedPan(moveX, moveY );
		}
	};
	
	/**
	 * 
	 * 单击获取经纬度
	 * @date 2014-12-09
	 */
	$.fn.ffcsMap.onClickGetPoint = function(x, y, imgUrl, callBack) {
		if (imgUrl == '' || imgUrl == undefined) {
			imgUrl = 'images/GreenShinyPin.png';
		}
		var symbol = new esri.symbols.PictureMarkerSymbol(imgUrl, 34, 48);
		var rwkid = map.spatialReference["wkid"];

		var p = new esri.geometry.Point(x, y, new esri.SpatialReference({
			wkid : rwkid
		}));
		var g = new esri.Graphic(p, symbol);

		var pointLayer = new esri.layers.GraphicsLayer({
			id : "select_cpoint_ly"
		});
		pointLayer.add(g);
		map.addLayer(pointLayer);

		map.on("click", function(evt) {
			var removeLayer = map.getLayer("select_cpoint_ly");
			if (removeLayer != null) {
				map.removeLayer(removeLayer);
			}

			var mp = evt.mapPoint;
			var point = new esri.geometry.Point(mp.x, mp.y,
					new esri.SpatialReference({
						wkid : rwkid
					}));
			var graphic = new esri.Graphic(point, symbol);

			var pointLayer = new esri.layers.GraphicsLayer({
				id : "select_cpoint_ly"
			});
			pointLayer.add(graphic);
			map.addLayer(pointLayer);

			return callBack([ mp.x, mp.y ]);
		});
	};
	
})(jQuery);