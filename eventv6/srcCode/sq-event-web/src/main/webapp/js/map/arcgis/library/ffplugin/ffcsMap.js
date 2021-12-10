/**
*
* 功能介绍：对arcgis图层加载及处理
* 版本： v.1
* 日期： 2014.4.18
**/

(function($){
	var polyline ;
	var map ,toolbar,//工具条
	editToolbar,//可编辑工具
	event,
	Editor,
	gPictureMarkerSymbol,
	RestTiled; //20140905
	

	
	var isDrawEdit = false;//是否可编辑
	
	var returnDataGraphic; // 用于存放返回几何图形数据
	var drawData ; // draw function call
	
	// 点定位
	var pointInfoDiv;
	
	var hotlayer,builddianpuDv,ptiplayer; //热点提示
	var mutilPiontPanelDiv;
	
	var ffcsMap_current_obj = {};
	var tempLayer; // 临时图层
	var _measure;
	
	//多图层，每个图层相同点叠加，支持翻页增量叠加
	var mutilPointObjArr = []; //多图层对象
	var renderPointListData = []; //存render接口 点列表数据
	var zkMapElement = null;  //每个图层的点、div元素存在对象
	
	
	//要素图层相关变量
	var ffcs_curFeature;
	var isFeatureLayer = false;  //是否是要素图层，是时将取属性转对象
	
	/**
	 * 地图对象加载主方法
	 * div绑定加载,如：$("#map").ffcsMap();  	
	 * 参数：options 可根据html中定义的id名称加载，如{mapId:"map111"}
	 */
	$.fn.ffcsMap = function(options){
		$.fn.ffcsMap.init();
		var opts = $.fn.ffcsMap.defaults = $.extend(true, {}, $.fn.ffcsMap.defaults, options);
		return this.each(function(){
			_this = $(this);
			var obj =   $.meta ? $.extend({}, opts, _this.data()) : opts;  
			$.fn.ffcsMap.zoom();
			$.fn.ffcsMap.create(obj.mapId,obj);
		});
	};
	
	$.fn.initMap = function(options, backFn) {
		var _this = $(this);
		$.fn.ffcsMap.init(function() {
			var opts = $.fn.ffcsMap.defaults = $.extend(true, {}, $.fn.ffcsMap.defaults, options);
			_this.each(function() {
				var obj = $.meta ? $.extend({}, opts, $(this).data()) : opts;  
				$.fn.ffcsMap.zoom();
				$.fn.ffcsMap.create(obj.mapId, obj);
			});
			if (typeof backFn == "function") backFn.call(this);
		});
	};
	
	/***
	 * 初始化esri API对象
	 */
	$.fn.ffcsMap.init = function(backFn){
		require([ 
					"esri/dijit/editing/Editor",
					"esri/toolbars/edit",
					"dojo/_base/event",
					"esri/layers/ArcGISTiledMapServiceLayer",
	                "esri/symbols/PictureMarkerSymbol"
				],
		   function(
				 p_Editor,
				 p_Edit,
				 p_event,
				 p_ArcGISTiledMapServiceLayer,
				 p_PictureMarkerSymbol
		   ) {
			// 初始值
			 Editor = p_Editor;
			 Edit = p_Edit;
			 event = p_event;
			 RestTiled = p_ArcGISTiledMapServiceLayer;
			 gPictureMarkerSymbol = p_PictureMarkerSymbol;
			 if (typeof backFn == "function") backFn.call(this);
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
		 minZoom : 0,	  //最小缩放层级
	     //加载的图层地址
	     mapUrl : "",    
	     layerType : "",   //图层类型
	     events : {
	    	 "zoom-end" : null
	     }
	};
	
	$.fn.ffcsMap.zoom = function(){
		
	};
	
    $.fn.ffcsMap.removeLayer = function() {
    	for (var i = map.layerIds.length - 1; i >= 0; i--) {
    		var layer = map.getLayer(map.layerIds[i]);
    		layer.hide();
    		map.removeLayer(layer);
		}
    };
	
	/**
	 * 创建地图
	 * 参数1：div id
	 */
	$.fn.ffcsMap.create = function(mapId,opts){
		
		if(esri!=undefined){
			esri.config.defaults.map.height = (opts.height!=undefined ? opts.height : 600);
			esri.config.defaults.map.width =  (opts.width!=undefined ? opts.width :1100);
		}
		
        map = new esri.Map(mapId , {
	        center: [opts.x, opts.y],
	        zoom: opts.zoom,
	        slider: opts.slider,
			minZoom: opts.minZoom
	      });

        var url = opts.mapUrl;
        $.fn.ffcsMap.createLayer(url, opts.layerType);
        
	        
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
				var _spatialReference = new esri.SpatialReference({ wkid : options.wkid});
				tiledMapServiceLayer.initialExtent = new esri.geometry.Extent({
					xmin : options.xmin,
					ymin : options.ymin,
					xmax : options.xmax,
					ymax : options.ymax,
					spatialReference : _spatialReference
				});
				map.extent = tiledMapServiceLayer.initialExtent;
				
				map.spatialReference = _spatialReference;
				map.addLayer(tiledMapServiceLayer);
				$.fn.ffcsMap.setScreenPostition();
				
				break;
			case "dynamic" :
				var petroFieldsMSL = new esri.layers.ArcGISDynamicMapServiceLayer(url);
				var _spatialReference = new esri.SpatialReference({ wkid : options.wkid});
				petroFieldsMSL.initialExtent = new esri.geometry.Extent({
					xmin : options.xmin,
					ymin : options.ymin,
					xmax : options.xmax,
					ymax : options.ymax,
					spatialReference : _spatialReference
				});
				petroFieldsMSL.setDisableClientCaching(true);
				
				map.extent = petroFieldsMSL.initialExtent;
				map.spatialReference = _spatialReference;
		        map.addLayer(petroFieldsMSL);
		        break;
		    case "wms" :
		    	
				  var layer1 = new esri.layers.WMSLayerInfo({
			        name: '1',
			        title: 'Rivers'
			      });
			      var layer2 = new esri.layers.WMSLayerInfo({
			        name: '2',
			        title: 'Cities'
			      });
			      
			      var resourceInfo = {
			        extent: new esri.geometry.Extent(options.xmin, options.ymin, options.xmax, options.ymax, {wkid: options.wkid}),
			        layerInfos: []
			      };
			      var wmsLayer = new esri.layers.WMSLayer(url, {resourceInfo: resourceInfo,visibleLayers: []});
			      map.addLayers([wmsLayer]);
		        break;
			case "feature" :
				var featureLayer = new esri.layers.FeatureLayer(url,{
					mode: esri.layers.FeatureLayer.MODE_SNAPSHOT,
		            outFields: ["*"]
			    });
				
				map.addLayer(featureLayer);
				break;
			case "kml" :
				var kmlLayer = new esri.layers.KMLLayer(url); 
			    map.addLayer(kmlLayer);
				break;
			case "osm" :
				var osmLayer = new esri.layers.OpenStreetMapLayer();
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
                        var arcgisServerLayerId = options.layerId;
                        var request = "GetTile";
		            	if(options.layerId=="MapServer"){
		            		if(options.tileMatrix  != undefined ){
		            			tileMatrix = level * 1 ;
		            		}
		            		prcUrl = url + "/tile/"+tileMatrix+"/"+row+"/"+col+"?blankTile=false";
		            		return prcUrl;
		            	}else if(options.layerId=="NPVEC" || options.layerId=="NPCVA"
							|| options.layerId =="NPIMG" || options.layerId =="NPCIA"
							|| options.layerId == "JJVEC"
							|| options.layerId =="NPSHA" || options.layerId =="NPCSA"
                            || options.layerId =="vec_hn" || options.layerId =="cva_hn"
						){//(vec:矢量图层，cva:矢量标签图层，img:影像图层,cia:影像标签图层，ter:地形,cta:地形标签图层)
		            		tileMatrix = tileMatrix -1;
		            		prcUrl = url ;
                            //海南地图
                            if(options.layerId =="vec_hn" || options.layerId =="cva_hn"){
                                arcgisServerLayerId = arcgisServerLayerId.replace("_hn", "");
                            }
		            	}else if(options.layerId =="GZ_VEC_JX_7_19" || options.layerId =="GZ_VEC_JX_ZZB"
							|| options.layerId =="GZ_VECZJ_JX_7_19" || options.layerId =="GZ_VEC_NC_18_20"
								||options.layerId =="GZ_IMG_2M_JX_14"){
							//江西省平台对接地图--已调试（矢量底图）
							tileMatrix = level-1;
							//row = col * 2;
							//col = row * 2;
							prcUrl = url ;
						}else if(options.layerId=="TianDiVector"
                            || options.layerId=="vec_fj" || options.layerId=="cva_fj"
                            || options.layerId =="img_fj" || options.layerId =="cia_fj"
                            || options.layerId =="ter_fj"){
		            		//tileMatrix = tileMatrix -1;
		            		prcUrl = url ;
		            	} else if(options.layerId=="hn_bigdata"){//海南政务外网地图
                            prcUrl = url ;
                            tileMatrix = tileMatrix;
                            //request = "getcapabilities";
                        } else if(url.indexOf("api/Wmts/index") != -1){//晋江地图
                        	prcUrl = url;
                        }else{
		            		prcUrl = url + options.layerId + "_c/wmts";
		            	}

                        prcUrl = prcUrl + "?SERVICE=WMTS&request="+request+"&Version=1.0.0" +
                            "&Style=Default&Format="+options.imageFormat+"&serviceMode="+options.serviceMode+"&layer="+arcgisServerLayerId +
                            "&TileMatrixSet="+options.tileMatrixSetId+"&TileMatrix=" + tileMatrix + "&TileRow=" + row + "&TileCol=" + col;
		            	// if(prcUrl.indexOf("t0.tianditu.com")>0){
                        if(options.mapKey != 'undefined' && options.mapKey != null && options.mapKey != ''){
                            prcUrl = prcUrl + "&tk=" + options.mapKey;
                        }

                        // }
		            	return  prcUrl;

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
			var linecolor = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, dojo.Color.fromString("#0076ff"), 2);
	        toolbar.fillSymbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,	linecolor,dojo.Color.fromString("rgba(0,0,0,0.1)"));

				
			toolbar.lineSymbol = linecolor;
			
	        editToolbar = new esri.toolbars.Edit(map);
	        
	        //dojo.connect(map.graphics, "onClick", $.fn.ffcsMap.dealDelLeng);
	        _measure =  new Measure(map,toolbar);
	        //_measure.onEvent();
        });

		//map.on("layer-add", function () {
            //console.log("Map layer-add event");
        //});
		var _position = null;
		var _mapDivObj = null;
		var _x = _y = 0;
		map.on("mouse-down", function(e) {
			_mapDivObj = $("#NorMapOpenDiv");
			if (_mapDivObj != null && _mapDivObj != undefined) {
				_position = _mapDivObj.offset();
			}
			_x = e.x, _y = e.y;
		});
		map.on("mouse-drag", function(e) {
			mend_runCount = mend_runCount + 1;
			if (mend_runCount == 1) {
				var m_x = e.x - _x;
				var m_y = e.y - _y;
				if (_position != null && typeof _position != "undefined") {
					var pleft = _position.left + m_x;
					var ptop = _position.top + m_y;
					_mapDivObj.css("left", pleft);
					_mapDivObj.css("top", ptop);
				}
			}
			mend_runCount = 0;
		});
		map.on("mouse-drag-end", function(e) {
			_mapDivObj = null;
			_position = null;
			_x = _y = 0;
		});
        map.on("pan-end", function(e) {
			mend_runCount = mend_runCount + 1;
			if(mend_runCount == 1){
				var m_x = e.delta.x ; 
				var m_y = e.delta.y ; 
				
				$.fn.ffcsMap.winDivGoToMarker();
				 
				var mutilPiontPosition =  $(".menuNav").position(); 
				if(mutilPiontPosition != undefined){
					var mt_left = mutilPiontPosition.left;
					var mt_top = mutilPiontPosition.top;
					mt_left =   mt_left + m_x ;
					mt_top =  mt_top + m_y ;
					$(".menuNav").css("left",mt_left);
					$(".menuNav").css("top",mt_top);
				}
				
				
			}
	            
			mend_runCount = 0;
			
			$.fn.ffcsMap.setScreenPostition();
			
			
		});
        
        map.on("zoom-end",function(e){
        	if (mutilPiontPanelDiv!= null) {
				mutilPiontPanelDiv.innerHTML = "";
			 
			}
        	$.fn.ffcsMap.setScreenPostition();
        	
        	if ($.fn.ffcsMap.defaults.events["zoom-end"]) {
        		var _zoom = map.getZoom();
        		$.fn.ffcsMap.defaults.events["zoom-end"].call(this, _zoom);
        	}
		});
        
       
		
		//清除地图，关闭提示信息.
		function __closeDialog() {
			//console.log("close");
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
	              symbol = new esri.symbols.SimpleMarkerSymbol();
	              break;
	            case "polyline":
	              symbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, dojo.Color.fromString("#0076ff"), 2);
	              break;
	            default:
	              symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,	new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, dojo.Color.fromString("#0076ff"), 2),dojo.Color.fromString("rgba(0,0,0,0.1)"));
	              break;
	          }
	          
	          var graphic = new esri.Graphic(evt.geometry, symbol);
	          returnDataGraphic = graphic;
	          map.graphics.add(graphic);
	          
	         if (map.drawMode == "measureLengthD") {//动态测距处理
	        	 _measure.checkOut(map, evt.geometry);
	         }
	         else if (map.drawMode == "measureAreaD") {//动态测面积处理
	        	 _measure.checkArea(map, evt.geometry);
	         }
	     }
	    
	    ///////测距移动
	    dojo.connect(map, "onMouseMove", function (evt) {
	        evt = evt ? evt : (window.event ? window.event : null);
	        if (map.drawMode == "measureLengthD" || map.drawMode == "measureAreaD") {
	        	_measure.pointOutDbClickEnd(evt.clientX + 10, evt.clientY + 10, "单击确定起点");
	        }
	        if (map.drawMode == "drawLine" || map.drawMode == "drawPloy") {
	        	_measure.pointOutDbClickEnd(evt.clientX + 10, evt.clientY + 10, "单击确定点，双击结束");
	        }
	        if (map.drawMode == "measureLengthD") {
	            pointLength = evt.mapPoint;
	            pointLengthProj = pointLength; //thisSDMap.thisChangeProj(pointLength);
	            if (clickNum != -1) {
	                length = _measure.getDistanceInEarth(pointLengthProj, pointArrProj[clickNum]);
	                if ((lengthZ + length) < 1000) {
	                	_measure.pointOutDbClickEnd(evt.clientX + 10, evt.clientY + 10, (lengthZ + length).toFixed(0) + "米<br/>单击确定地点，双击结束");
	                } else {
	                	_measure.pointOutDbClickEnd(evt.clientX + 10, evt.clientY + 10, ((lengthZ + length) / 1000).toFixed(1) + "公里<br/>单击确定地点，双击结束");
	                }
	            }
	        } else if (map.drawMode == "measureAreaD") {
	            pointArea = evt.mapPoint;
	            pointAreaProj = pointArea;
	            if (clickAreaNum != -1) {
	                var areaZmove = areaZ + _measure.getTriangleArea(pointAreaProj, pointAreaArrProj[clickAreaNum], pointAreaArrProj[0]);
	                var areaWithUnit = "";
	                if (areaUnit == "hectare") {
	                    areaWithUnit = ((Math.abs(areaZmove) / 10000) > 1 ? (Math.abs(areaZmove) / 10000).toFixed(2) : (Math.abs(areaZmove) / 10000).toFixed(4)) + "公顷";
	                }else if (areaUnit == "squareMeter") {
	                    areaWithUnit = parseInt(Math.abs(areaZmove)) + "平方米";
	                }else if (areaUnit == "squareKiloMeters") {
	                    areaWithUnit = ((Math.abs(areaZmove) / 1000000) > 1 ? (Math.abs(areaZmove) / 1000000).toFixed(3) : (Math.abs(areaZmove) / 1000000).toFixed(6)) + "平方公里";
	                }else if (areaUnit == "acres") {
	                    areaWithUnit = ((Math.abs(areaZmove) / 666.7) > 1 ? (Math.abs(areaZmove) / 666.7).toFixed(1) : (Math.abs(areaZmove) / 666.7).toFixed(2)) + "亩";
	                }

	                _measure.pointOutDbClickEnd(evt.clientX + 10, evt.clientY + 10, areaWithUnit + "<br/>单击确定地点，双击结束");

	                evPolygonLine.setPoint(0, 1, pointArea);
	                allPolygonGrapLine.setGeometry(evPolygonLine);
	            }
	        }
	    });
	    
	    dojo.connect(map, "onClick", function (evt) {
	    	if (map.drawMode == "measureLengthD") {
	            evt = evt ? evt : (window.event ? window.event : null);
	            pointLength = evt.mapPoint;
	            pointLengthProj = pointLength;
	            var text = null;
	            if (clickNum == -1) {
	                lengthNum++;
	                text = "起点";
	                lengthGraphicsArr[lengthNum - 1] = [];
	                textDivArr[lengthNum - 1] = new ZLKeyValueMapping();
	                CheckedRulerS = 2;

	                evLine = new esri.geometry.Polyline(new esri.SpatialReference({ wkid: map.spatialReference["wkid"] }));
	                evLine.addPath([[pointLength.x, pointLength.y]]);
	                allLineGrap = new esri.Graphic(evLine, toolbar.lineSymbol);
	                map.graphics.add(allLineGrap);
	                lengthGraphicsArr[lengthNum - 1].push(allLineGrap);
	                pointArrProj = [];
	            } else {
	                lengthZ += length;
	                text = lengthZ < 1000 ? parseInt(lengthZ) + "米" : ((lengthZ) / 1000).toFixed(1) + "公里";
	                evLine.insertPoint(0, evLine.paths[0][evLine.paths[0].length], pointLength);
	                allLineGrap.setGeometry(evLine);
	            }
	            textDivArr[lengthNum - 1].put(pointLength, null);
	            _measure.createTextDiv(map, pointLength, lengthNum - 1, text);

	            var pointlengthGraphic = new esri.Graphic(pointLength, toolbar.markerSymbol);
	            map.graphics.add(pointlengthGraphic);
	            lengthGraphicsArr[lengthNum - 1].push(pointlengthGraphic);

	            pointArr.push(pointLength); //把一次测距中的点存入点临时数组中
	            pointArrProj.push(pointLengthProj);
	            clickNum++; //一次测距点的个数
	        } else if (map.drawMode == "measureAreaD") {
	            evt = evt ? evt : (window.event ? window.event : null);
	            pointArea = evt.mapPoint;
	            pointAreaProj = pointArea;
	            var text = null;
	            if (clickAreaNum == -1) {
	                //创建一个面
	                evPolygon = new esri.geometry.Polygon(new esri.SpatialReference({ wkid: map.spatialReference["wkid"] }));
	                evPolygon.addRing([[pointArea.x, pointArea.y], [pointArea.x, pointArea.y], [pointArea.x, pointArea.y]]);
	                allPolygonGrap = new esri.Graphic(evPolygon, toolbar.fillSymbol);
	                map.graphics.add(allPolygonGrap);
	                //创建一个活动的线
	                evPolygonLine = new esri.geometry.Polyline(new esri.SpatialReference({ wkid: map.spatialReference["wkid"] }));
	                evPolygonLine.addPath([[pointArea.x, pointArea.y], [pointArea.x, pointArea.y]]);
	                allPolygonGrapLine = new esri.Graphic(evPolygonLine, toolbar.lineSymbol);
	                map.graphics.add(allPolygonGrapLine);

	                areaNum++;
	                areaGraphicsArr[areaNum - 1] = [];
	                textDivArr1[areaNum - 1] =  new ZLKeyValueMapping();
	                CheckedRulerS = 2;
	                text = "0";
	                textDivArr1[areaNum - 1].put(pointArea, null);
	                areaGraphicsArr[areaNum - 1].push(allPolygonGrap);
	                areaGraphicsArr[areaNum - 1].push(allPolygonGrapLine);
	                pointAreaArrProj = [];
	            } else {
	                areaZ +=  _measure.getTriangleArea(pointAreaProj, pointAreaArrProj[clickAreaNum], pointAreaArrProj[0]);

	                var areaWithUnit = "";
	                if (areaUnit == "hectare") {
	                    areaWithUnit = ((Math.abs(areaZ) / 10000) > 1 ? (Math.abs(areaZ) / 10000).toFixed(2) : (Math.abs(areaZ) / 10000).toFixed(4)) + "公顷";
	                }else if (areaUnit == "squareMeter") {
	                    areaWithUnit = parseInt(Math.abs(areaZ)) + "平方米";
	                }else if (areaUnit == "squareKiloMeters") {
	                    areaWithUnit = ((Math.abs(areaZ) / 1000000) > 1 ? (Math.abs(areaZ) / 1000000).toFixed(3) : (Math.abs(areaZ) / 1000000).toFixed(6)) + "平方公里";
	                }else if (areaUnit == "acres") {
	                    areaWithUnit = ((Math.abs(areaZ) / 666.7) > 1 ? (Math.abs(areaZ) / 666.7).toFixed(1) : (Math.abs(areaZ) / 666.7).toFixed(2)) + "亩";
	                }

	                text = areaWithUnit;
	                _measure.createTextDiv1(map, pointArea, areaNum - 1, text);

	                evPolygon.insertPoint(0, evPolygon.rings[0].length - 1, pointArea);
	                allPolygonGrap.setGeometry(evPolygon);

	                evPolygonLine.setPoint(0, 1, pointArea);
	                evPolygonLine.setPoint(0, 2, pointArea);
	                allPolygonGrapLine.setGeometry(evPolygonLine);
	            }
	            clickAreaNum++;
	            pointAreaArr.push(pointArea);
	            pointAreaArrProj.push(pointAreaProj);
	        }
	    });
	    
	    
	    dojo.connect(map, "onExtentChange", function (extent, delta, levelChange, lod) {
	    	if($("#orgCode").val() && $("#orgCode").val().indexOf("350582")==0){//晋江
	    		if(levelChange && lod.level>=16){
		    		$("#map0_layer2").hide();
		    		$("#map0_layer3").hide();
		    		$("#map0_layer0").show();
		    		$("#map0_layer1").show();
		    	}else if(levelChange && lod.level<16){
		    		$("#map0_layer2").show();
		    		$("#map0_layer3").show();
		    		$("#map0_layer0").hide();
		    		$("#map0_layer1").hide();
		    	}
	    	}
	        if (lengthNum > 0) {
	            for (var j = 0; j < textDivArr.length; j++) {
	                for (var i = 0; i < textDivArr[j].elements.length; i++) {
	                    if (extent.contains(textDivArr[j].element(i).key)) {
	                        textDivArr[j].element(i).value.style.visibility = "visible";
	                    } else {
	                        textDivArr[j].element(i).value.style.visibility = "hidden";
	                    }
	                    _measure.createTextDiv(map, textDivArr[j].element(i).key, j);
	                }
	            }
	        }
	        if (areaNum > 0) {
	            for (var k = 0; k < textDivArr1.length; k++) {
	                if (extent.contains(textDivArr1[k].element(0).key)) {
	                    textDivArr1[k].element(0).value.style.visibility = "visible";
	                } else {
	                    textDivArr1[k].element(0).value.style.visibility = "hidden";
	                }
	                _measure.createTextDiv1(map, textDivArr1[k].element(0).key, k);
	            }
	        }
	        
	        for(var m = 0; m<mutilPointObjArr.length ; m++){
        		var _layerName =  mutilPointObjArr[m].layerName;
        		
    	        var layer = map.getLayer(_layerName);
    	        if(layer && layer.graphics && layer.graphics.length>0 && layer.graphics[0].geometry.type=='point'){//如果是撒点才处理
    	        	createPointCounttList=[];//清空标记
    	        	for(var j=0;j<layer.graphics.length;j++){//将点位全部展示
        	        	layer.graphics[j].visible=true;
        	    	}
    	        }
    	        
	        	for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
	                if (mutilPointObjArr[m].layerType == "customLayer") {
	                	var itemData = mutilPointObjArr[m].renderPointListData[k];
						$.fn.ffcsMap.createDivA(
											mutilPointObjArr[m].zkMapElement,
											mutilPointObjArr[m].zkMapElement.elements[k],
											m, k, itemData);
	                } else {
						var label = mutilPointObjArr[m].zkMapElement.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[k].key);
						$.fn.ffcsMap.createPointCounttDiv(mutilPointObjArr[m].zkMapElement, mutilPointObjArr[m].zkMapElement.elements[k] ,label, m ,k ,mutilPointObjArr[m].layerName);
					}
	            }
        	}
        	
	        
	    });
	    
	    ///测距事件end
	    
	   
	};
	/**
	 * 设当前中心点对象及级别
	 * 
	 */
	$.fn.ffcsMap.setScreenPostition = function(){
		var dh = $(document).height()/ 2;
		var dw = $(document).width()/ 2 ;
		//conlose.log(map.container.clientHeight  + ":"+map.container.clientWidth );
		var cpt = new esri.geometry.Point(
				dw ,
				dh ,
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
			var point = new esri.geometry.Point(data.coordinates[0], data.coordinates[1]);
			var symbol = new SimpleMarkerSymbol();
			var graphic = new esri.Graphic(point, symbol);
			map.graphics.add(graphic);
			break;
		case "polyline" :
			var points = [];

			for (var i = 0; i < data.coordinates[0].length; i++) {
            	points[i] = new esri.geometry.Point(data.coordinates[0][i]);  
	        }  
			
			var polyline = new esri.geometry.Polyline();
			polyline.addPath(points);
			
			var symbol = new esri.symbol.SimpleLineSymbol();
			var graphic = new esri.Graphic(polyline, symbol);
			map.graphics.add(graphic);
			break;
		case "polygon" :
			var rings = [];

			for (var i = 0; i < data.coordinates[0].length; i++) {
				rings[i] = new esri.geometry.Point(data.coordinates[0][i]);  
	        }  
			
			var polygon = new esri.geometry.Polygon();
			polygon.addRing(rings);
			
			var symbol = new esri.symbol.SimpleLineSymbol();
			
			var graphic = new esri.Graphic(polygon, symbol);
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
		
		var symbol = new gPictureMarkerSymbol(imgUrl, 34, 48);
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
	
	$.fn.ffcsMap.renderForPoint = function(elementsCollectionStr,layerName,datas,imgUrl,imgWidth,imgHeight,fontSize) {
		var pointLayer; // 点
		
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
		
		zkMapElement = null;
		if (map.getLayer(layerName) == null) {
			renderPointListData = [];
			zkMapElement = new ZLKeyValueMapping();
		} else {
			for(var m=0; m<mutilPointObjArr.length ; m++){
				if(mutilPointObjArr[m].layerName==layerName){
					zkMapElement = mutilPointObjArr[m].zkMapElement;
				}
			}
			if(zkMapElement == null) {
				zkMapElement = new ZLKeyValueMapping();
			}
		}
		
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
		
		var rwkid = map.spatialReference["wkid"];
		
		// 设置字体的大小
		var textFont = defTextFont;
		
		if (fontSize != null) {
			textFont = fontSize + "pt";
		}
		$.each(datas, function(i, data) {
			// 给对象设值   用来区分点、楼宇还是网格
			var _data = {};
			_data.id	= data["ID"];
			_data.wid	= data["WID"];
			_data.name	= data["NAME"];
			_data.x		= data["X"];
			_data.y		= data["Y"];
			_data.mapt	= data["MAP_TYPE"];
			_data.layerName = layerName;
			_data._oldData = _data;
			_data.elementsCollectionStr = elementsCollectionStr;
			if (_data.wid == null || _data.wid == '' 
				|| _data.x == null || _data.x == ''
				|| _data.y == null || _data.y == '') {
				return;
			}
			
			// 边框颜色
			var RGBLineColor = defRGBLineColor;
			// 填充颜色
			var RGBAreaColor = defRGBAreaColor;
			
			if(_data.lineColor != "" && _data.lineColor != null){
				RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
			}
			
			if(_data.areaColor != "" && _data.areaColor != null){
				RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
			}
			
			// 边框线条宽度
			var lineWidth = defLineWidth;
			
			if (_data.lineWidth != "" && _data.lineWidth != null) {
				lineWidth = _data.lineWidth;
			}
			
			// 文字颜色
			var RGBNameColor = defRGBNameColor;
			
			if (_data.nameColor != "" && _data.nameColor != null) {
				RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
			}
			
			// 中心点坐标
			var xloc = _data.x;
			var yloc = _data.y;
			
			// 构造中心点
			var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid }));
			
			// 区域名称 data.name
			var textString = _data.name;
			
			// 网格样式    默认显示轮廓
			var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
						  new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
						  new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
			
			// 文字样式    默认半透明
			var textSymbol = new esri.symbol.TextSymbol(textString).
								setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
								setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
								setFont(new esri.symbol.Font(textFont).
								setWeight(esri.symbol.Font.WEIGHT_BOLD));
			
			if (imgUrl == '' || imgUrl == undefined) {
				imgUrl = 'images/GreenShinyPin.png';
			}
			// 文字
			textSymbol.setColor(new dojo.Color([0, 0, 0, 0]));
			textSymbol.setFont(new esri.symbol.Font("12pt"));
			textSymbol.setOffset(35, -10);
			var graphicsText = new esri.Graphic(point, textSymbol);
			pointLayer.add(graphicsText);
			
			var symbol = new esri.symbol.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
			
			var p = new esri.geometry.Point(_data.x, _data.y, new esri.SpatialReference({ wkid: rwkid }));
			
			var graphic = new esri.Graphic(p, symbol);
			graphic.setAttributes( {"textGraphic":graphicsText, "data":_data, "layerName":layerName});
			
			pointLayer.add(graphic);
			
			var rpData = {};
			rpData.textGraphic = graphicsText;
			rpData.data = _data;
			rpData.layerName = layerName;
			renderPointListData.push(rpData);
			zkMapElement.put(p,null);
		});
	
		var mpObj = {};
		mpObj.layerName = layerName;
		mpObj.renderPointListData = renderPointListData;
		mpObj.zkMapElement = zkMapElement;
		mutilPointObjArr.push(mpObj);
		renderPointListData = [];
	
		for(var m=0; m<mutilPointObjArr.length; m++){
			for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
				var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
				if(div){
					div.style.visibility = "visible"; 
				}
			}
		}
		createPointCounttList=[];
		for(var m=0; m<mutilPointObjArr.length ; m++){
			if(mutilPointObjArr[m].layerName==layerName){
				for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
					var label = mutilPointObjArr[m].zkMapElement.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
					$.fn.ffcsMap.createPointCounttDiv(mutilPointObjArr[m].zkMapElement, mutilPointObjArr[m].zkMapElement.elements[i] ,label, m ,i,mutilPointObjArr[m].layerName );
				}
			}
		}
		
		// 当前鼠标移动所在图形,由于底下还有一个mouseMove同名方法，DEBUG时不进这个，暂时改名
		function mouseMoveOld(evt) {
			var graphic = evt.graphic;
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				var _data = graphic.attributes.data;
				var type = graphic.attributes.type;
				var symbol = graphic.symbol;
				var currentLayerName = graphic.attributes.layerName;
				
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
				
				// 文字
				var textGraphic = graphic.attributes.textGraphic;
				var textSymbol = textGraphic.symbol.setColor(new dojo.Color([0, 0, 0, 1]));
				//textGraphic.setSymbol(textSymbol);
				var ptitle = textGraphic.symbol.text;
				
				var mutilPoint_data = graphic.attributes.data;
				var curr_pointList = [];
							
				for(var m=0;m<mutilPointObjArr.length;m++){
					for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
						if (mutilPointObjArr[m].layerName != currentLayerName) {
							continue;
						}
					
						var rx = mutilPointObjArr[m].renderPointListData[i].data["x"];
						var ry = mutilPointObjArr[m].renderPointListData[i].data["y"];
						var name = mutilPointObjArr[m].renderPointListData[i].data["gridName"];
						if(mutilPoint_data["x"] == rx && mutilPoint_data["y"]== ry){
							curr_pointList.push(mutilPointObjArr[m].renderPointListData[i]);
						}
					}
				}
				
				if(curr_pointList.length > 1){
					ptitle = "";
					for(var i=0; i< curr_pointList.length ; i++){
						var temp_ptitle = curr_pointList[i].textGraphic.symbol.text;
						
						if(i==0){
							 ptitle = temp_ptitle;
						}else{
							 if (ptitle.indexOf(temp_ptitle) == -1) {
								 ptitle = ptitle +","+ temp_ptitle ;
							 }
						}
					}
				}
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
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
				
				// 不显示文字
				var textGraphic = graphic.attributes.textGraphic;
				var textSymbol = textGraphic.symbol.setColor(new dojo.Color([0, 0, 0, 0]));
				textGraphic.setSymbol(textSymbol);
				graphic.setSymbol(symbol);
			}
		}
	};

	/**
	*	传datas渲染图片及文字
	*/
	
	
$.fn.ffcsMap.render4PointList = function(elementsCollectionStr,layerName,datas,imgUrl,imgWidth,imgHeight,fontSize) {	
		var pointLayer; // 点
		
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
		
		zkMapElement = null;
		if (map.getLayer(layerName) == null) {
			renderPointListData = [];
			zkMapElement = new ZLKeyValueMapping();
		} else {
			for(var m=0; m<mutilPointObjArr.length ; m++){
				if(mutilPointObjArr[m].layerName==layerName){
					zkMapElement = mutilPointObjArr[m].zkMapElement;
				}
			}
			if(zkMapElement == null) {
				zkMapElement = new ZLKeyValueMapping();
			}
		}
		
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
		var ecs = elementsCollectionStr.split(",_,");
		var eclist = {};
		for(var i=0;i<ecs.length;i++){
			var e = ecs[i].split("_,_");
			eclist[e[0]]=e[1];
		}
		pointLayer.on("click", function(evt) {
			var graphic = evt.graphic;
			if (graphic.attributes != null && graphic.attributes.data.elementsCollectionStr != null && graphic.attributes.data.elementsCollectionStr.length>0) {
				
				$.fn.ffcsMap.locationPoint({w:eclist["menuSummaryWidth"],h:eclist["menuSummaryHeight"]},layerName, evt.graphic.attributes.data.wid, 
				eclist['menuName'], null,null,null, function(data){
					var url = eclist['menuSummaryUrl']+data.wid;
					return '<iframe id="window_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
				});
			}
		});
		
		var rwkid = map.spatialReference["wkid"];
		
		// 设置字体的大小
		var textFont = defTextFont;
		
		if (fontSize != null) {
			textFont = fontSize + "pt";
		}
		$.each(datas, function(i, data) {
			// 给对象设值   用来区分点、楼宇还是网格
			var _data = {};
			_data.id	= data["ID"];
			_data.wid	= data["WID"];
			_data.name	= data["NAME"];
			_data.x		= data["X"];
			_data.y		= data["Y"];
			_data.mapt	= data["MAP_TYPE"];
			_data.layerName = layerName;
			_data._oldData = _data;
			_data.elementsCollectionStr = elementsCollectionStr;
			if (_data.wid == null || _data.wid == '' 
				|| _data.x == null || _data.x == ''
				|| _data.y == null || _data.y == '') {
				return;
			}
			
			// 边框颜色
			var RGBLineColor = defRGBLineColor;
			// 填充颜色
			var RGBAreaColor = defRGBAreaColor;
			
			if(_data.lineColor != "" && _data.lineColor != null){
				RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
			}
			
			if(_data.areaColor != "" && _data.areaColor != null){
				RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
			}
			
			// 边框线条宽度
			var lineWidth = defLineWidth;
			
			if (_data.lineWidth != "" && _data.lineWidth != null) {
				lineWidth = _data.lineWidth;
			}
			
			// 文字颜色
			var RGBNameColor = defRGBNameColor;
			
			if (_data.nameColor != "" && _data.nameColor != null) {
				RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
			}
			
			// 中心点坐标
			var xloc = _data.x;
			var yloc = _data.y;
			
			// 构造中心点
			var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid }));
			
			// 区域名称 data.name
			var textString = _data.name;
			
			// 网格样式    默认显示轮廓
			var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
						  new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
						  new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
			
			// 文字样式    默认半透明
			var textSymbol = new esri.symbol.TextSymbol(textString).
								setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
								setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
								setFont(new esri.symbol.Font(textFont).
								setWeight(esri.symbol.Font.WEIGHT_BOLD));
			
			if (imgUrl == '' || imgUrl == undefined) {
				imgUrl = 'images/GreenShinyPin.png';
			}
			// 文字
			textSymbol.setColor(new dojo.Color([255, 0, 0, 1]));
			textSymbol.setFont(new esri.symbol.Font("12pt"));
			//textSymbol.setOffset(0, 0);
			var graphicsText = new esri.Graphic(point, textSymbol);
			pointLayer.add(graphicsText);
			
			var symbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
			symbol.setOffset(-40, 10);
			var p = new esri.geometry.Point(_data.x, _data.y, new esri.SpatialReference({ wkid: rwkid }));
			
			var graphic = new esri.Graphic(p, symbol);
			graphic.setAttributes( {"textGraphic":graphicsText, "data":_data, "layerName":layerName});
			
			pointLayer.add(graphic);
			
			var rpData = {};
			rpData.textGraphic = graphicsText;
			rpData.data = _data;
			rpData.layerName = layerName;
			renderPointListData.push(rpData);
			zkMapElement.put(p,null);
		});
	
		var mpObj = {};
		mpObj.layerName = layerName;
		mpObj.renderPointListData = renderPointListData;
		mpObj.zkMapElement = zkMapElement;
		mutilPointObjArr.push(mpObj);
		renderPointListData = [];
	
		for(var m=0; m<mutilPointObjArr.length; m++){
			for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
				var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
				if(div){
					div.style.visibility = "visible"; 
				}
			}
		}
		
		for(var m=0; m<mutilPointObjArr.length ; m++){
			if(mutilPointObjArr[m].layerName==layerName){
				for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
					var label = mutilPointObjArr[m].zkMapElement.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
					$.fn.ffcsMap.createPointCounttDiv(mutilPointObjArr[m].zkMapElement, mutilPointObjArr[m].zkMapElement.elements[i] ,label, m ,i );
				}
			}
		}
		
		// 当前鼠标移动所在图形
		function mouseMove(evt) {
			var graphic = evt.graphic;
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				var _data = graphic.attributes.data;
				var symbol = graphic.symbol;
				var currentLayerName = graphic.attributes.layerName;
				
				// 文字
				var textGraphic = graphic.attributes.textGraphic;
				var ptitle = textGraphic.symbol.text;
				
				var mutilPoint_data = graphic.attributes.data;
				var curr_pointList = [];
							
				for(var m=0;m<mutilPointObjArr.length;m++){
					for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
						if (mutilPointObjArr[m].layerName != currentLayerName) {
							continue;
						}
					
						var rx = mutilPointObjArr[m].renderPointListData[i].data["x"];
						var ry = mutilPointObjArr[m].renderPointListData[i].data["y"];
						var name = mutilPointObjArr[m].renderPointListData[i].data["gridName"];
						if(mutilPoint_data["x"] == rx && mutilPoint_data["y"]== ry){
							curr_pointList.push(mutilPointObjArr[m].renderPointListData[i]);
						}
					}
				}
				
				if(curr_pointList.length > 1){
					ptitle = "";
					for(var i=0; i< curr_pointList.length ; i++){
						var temp_ptitle = curr_pointList[i].textGraphic.symbol.text;
						
						if(i==0){
							 ptitle = temp_ptitle;
						}else{
							 if (ptitle.indexOf(temp_ptitle) == -1) {
								 ptitle = ptitle +","+ temp_ptitle ;
							 }
						}
					}
				}
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
				graphic.setSymbol(symbol);
			}
		}
		
		// 鼠标离开
		function mouseOut(evt) {
			var graphic = evt.graphic;
			
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				//div形式提示清空
				if (ptiplayer != null) {
					ptiplayer.innerHTML = "";
				}
			}
		}
	};
	
	/**
	 * 渲染自定义div
	 * graphic : evt.graphic  点对象
	 * ids :{divId:显示的div,arrowId:箭头div}
	 */
	$.fn.ffcsMap.renderDiv = function(layerName, opt) {
		function getElementsCollectionObj(elementsCollectionStr) {
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
		var textLayer; // 文本图层
		var dataUrl = opt.url;
		var fetchContentFn = opt.getText;
		var ecsObj = getElementsCollectionObj(opt.ecs);
		zkMapElement = null;
		if (map.getLayer(layerName) == null) {
			renderPointListData = [];
			zkMapElement = new ZLKeyValueMapping();
		} else {
			for (var m = 0; m < mutilPointObjArr.length; m++) {
				if (mutilPointObjArr[m].layerName == layerName) {
					zkMapElement = mutilPointObjArr[m].zkMapElement;
				}
			}
			if(zkMapElement == null) {
				zkMapElement = new ZLKeyValueMapping();
			}
		}
		// 不存在该图层则新建，存在则直接获取
		if (map.getLayer(layerName) == undefined) {
			textLayer = new esri.layers.GraphicsLayer({id:layerName});
			map.addLayer(textLayer);
		} else {
			textLayer = map.getLayer(layerName);
		}
		var rwkid = map.spatialReference["wkid"];
		var newDataUrl = "";
		var parameterData = {};
		function getParameter(url) {
			var child1 = url.split("&elementsCollectionStr=");
			newDataUrl = child1[0];
			var res = {};
			if (child1.length > 1) {
				res = {
					"elementsCollectionStr" : child1[1]
				};
			}
			var urls = newDataUrl.split('?');
			if (urls.length == 2) {
				var string_a = urls[1];
				var string = string_a.split('&');
				for ( var i = 0; i < string.length; i++) {
					var str = string[i].split('=');
					res[str[0]] = str[1];
				}
				parameterData = res;
			}
			newDataUrl = urls[0];
		}
		getParameter(dataUrl);
		parameterData["mapt"] = currentArcgisConfigInfo.mapType;
		$.ajax({
			type : "POST",
			url : newDataUrl,
			data : parameterData,
			dataType : "json",
			async : false,
			success : function(datas) {
				$.each(datas, function(i, data) {
					// 给对象设值 用来区分点、楼宇还是网格
					data.layerName = layerName;
					data.elementsCollectionStr = opt.ecs;
					var p = new esri.geometry.Point(data.x, data.y, new esri.SpatialReference({ wkid : rwkid }));
					var content = "";
					if (typeof fetchContentFn == "function") {
						content = fetchContentFn.call(this, data);
					}
					var rpData = {};
					rpData.data = data;
					rpData.layerName = layerName;
					rpData.content = content;
					renderPointListData.push(rpData);

					zkMapElement.put(p, null);
				});

				var mpObj = {};
				mpObj.layerName = layerName;
				mpObj.renderPointListData = renderPointListData;
				mpObj.zkMapElement = zkMapElement;
				mpObj.layerType = "customLayer";
				mutilPointObjArr.push(mpObj);
				renderPointListData = [];

				for ( var m = 0; m < mutilPointObjArr.length; m++) {
					for ( var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
						var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
						if (div) {
							div.style.visibility = "visible";
						}
					}
				}

				// ======20150529修改 begin======
				for ( var m = 0; m < mutilPointObjArr.length; m++) {
					if (mutilPointObjArr[m].layerName == layerName) {
						for ( var i = 0; i < mutilPointObjArr[m].zkMapElement.elements.length; i++) {
							var label = mutilPointObjArr[m].zkMapElement
									.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
							var itemData = mutilPointObjArr[m].renderPointListData[i];
							itemData.offset = {x:-14,y:-55};
							$.fn.ffcsMap.createDivA(
											mutilPointObjArr[m].zkMapElement,
											mutilPointObjArr[m].zkMapElement.elements[i],
											m, i, itemData,
							function(point, busiData) {
								var offset = {left: 1, top: -57};
								var screenPt = map.toScreen(point);
								var top = screenPt.y - opt.h + offset.top;
								var left = screenPt.x - opt.w / 2 + offset.left;
								opt.lng = point.x;
								opt.lat = point.y;
								var resultInfo = opt.createDlg(busiData);
								if (resultInfo == null) return;
								$.fn.ffcsMap.initWindow(left, top, opt, ecsObj["menuName"],
											resultInfo, false, opt.showDetail, itemData.data["wid"], itemData.data, false, offset);
							});
						}
					}
				}
				// ======20150529修改 end======
			}
		});
	};
	/**
	 * 渲染网格区域外蒙版 
	 * 
	 * 
	 * @param layerName 图层名称
	 * @param dataUrl   数据路径
	 * @param dataPm    数据参数
	 * @param callbackFun 成功回调函数
	 */
	$.fn.ffcsMap.renderForMosaic = function(layerName, dataUrl, dataPm, callbackFun) {
		$.ajax({
			type: "POST",
			url: js_ctx + "/zhsq/map/arcgis/arcgisdata/getGridMosaicCfg.json?t=" + Math.random(),
			data: dataPm,
			dataType: "json",
			success: function(datas) {
				if (datas) {
					var opt = {
						color :"#2B2B2B",
						opacity : 0.9,
						fillStyle : "solid",
						lineStyle : "solid"
					}; 
					var cfgs = datas.split(',');
					if (cfgs.length == 2) {
						opt.color = cfgs[0];
						opt.opacity = cfgs[1];
					}
					var wanggeLayer; // 网格图层
					if (map.getLayer(layerName) == undefined) {
						wanggeLayer = new esri.layers.GraphicsLayer({id:layerName});
						var _i = 0;
                        var _tempLayer = "gridLayer";
                        var flag = layerName.lastIndexOf(_tempLayer) == 0 && layerName.length > _tempLayer.length;
                        if (flag) {
                            var lys = map.graphicsLayerIds;
                            for (var i = lys.length - 1; i >= 0; i--) {
                                var k = lys[i];
                                if (k.indexOf(_tempLayer) == 0 && k.length > _tempLayer.length) {
                                    var i1 = k.substring(_tempLayer.length, k.length);
                                    var i2 = layerName.substring(_tempLayer.length, layerName.length);
                                    if (parseInt(i2) > parseInt(i1)) {
                                        _i = i + 1;
                                        break;
                                    }
                                }
                            }
                            map.addLayer(wanggeLayer, _i);
                        } else {
                            map.addLayer(wanggeLayer, 0);
                        }
					} else {
						wanggeLayer = map.getLayer(layerName);
					}

					$.ajax({
						type: "POST",
						url: dataUrl,
						data: dataPm,
						dataType: "json",
						success: function(datas) {
							if (datas && datas.length > 0) {
								var data = datas[0];
                                var _data = $.fn.ffcsMap.toRingsDataCvt(data, true);//转换
                                
                                var mosaicColor = new dojo.Color(opt.color).toRgb();
                                var rwkid = map.spatialReference["wkid"];
                                var mosaicPolygon = new esri.geometry.Polygon(new esri.SpatialReference({wkid: rwkid}));
                                mosaicPolygon.addRing([[180.0, 90.0],[-180.0, 90.0],[-180.0, -90.0],[180.0, -90.0],[180.0, 90.0]]);
                                
                                if (_data.coordinates) {
                                	for (var j = 0; j < _data.coordinates.length; j++) {
                                		mosaicPolygon.addRing(_data.coordinates[j]);
                                	}
                                }
                                
                                // 马赛克轮廓样式
                                var mosaicSymbol = new esri.symbol.SimpleFillSymbol(opt.fillStyle, new esri.symbol.SimpleLineSymbol(opt.lineStyle, new dojo.Color([mosaicColor[0], mosaicColor[1], mosaicColor[2], 0]), 0), new dojo.Color([mosaicColor[0], mosaicColor[1], mosaicColor[2], opt.opacity]));
                                var mosaicGraphic = new esri.Graphic(mosaicPolygon, mosaicSymbol);
                                wanggeLayer.add(mosaicGraphic);
							}
							if (callbackFun) callbackFun();
						}
					});
				}
			}
		});
	};
	
	/**
	 * 渲染楼宇网格数据接口 
	 * 
	 * 
	 * @param layerName 图层名称
	 * @param isCvt  是否转换数据
	 * @param imgUrl 图片路径
	 * @param fontSize 字体大小
	 * @param isCPImg  true(中心点为图片) false(文字) 是否中心点为图片 2014-12-11
	 * @param callSubwg(_data) 网格中心点图片单击事件方法回调, _data参数为回调的数据
	 * @param isShowArea 是否显示区域颜色
	 */
	$.fn.ffcsMap.render = function(layerName,dataUrl,showType,isCvt,imgUrl,imgWidth,imgHeight,fontSize,isCPImg,callSubwg,isShowArea, otherOpt, reLoadLocation, locationDatas, renderCallbackFun) {
		var settings = {
			displayStyle : "1",
			fieldId : undefined,
			fieldName : undefined,
			rowsObj : undefined,
			preMakeFunc : undefined,
			isShowMarkerName : 0
		};
		otherOpt = $.extend({}, settings, otherOpt);
		var pointLayer; // 点
		var louyuLayer; // 楼宇
		var wanggeLayer; // 网格
		var linePoints = [];
		var polyline = new esri.geometry.Polyline();
		
		//样式默认定义
		var fillStyle = "solid"; // 填充样式
		var lineStyle = "solid"; // 边框样式
		
		// 十六进制颜色
		var defLineColor = "#FFFF00";// 边框颜色 [138,43,226];  // 边框颜色
		var defAreaColor = "#ADFF2F";// 填充颜色 [173,255,47];  // 填充颜色
		
		// 十六进制颜色转为RGB
		var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
		var defRGBAreaColor = new dojo.Color(defAreaColor).toRgb();
		
		var defLineWidth = 2 ; // 边框线条宽度
		
		// 文字颜色
		var defNameColor = "#000000";// 文字颜色
		var defRGBNameColor = new dojo.Color(defNameColor).toRgb();
		
		// 文字大小
		var defTextFont = "8pt"; // 文字大小
		
		zkMapElement = null;
		if( map.getLayer(layerName)==null){
			renderPointListData = [];
	    	zkMapElement = new ZLKeyValueMapping();
		}else {
			for(var m=0; m<mutilPointObjArr.length ; m++){
				if(mutilPointObjArr[m].layerName==layerName){
					zkMapElement = mutilPointObjArr[m].zkMapElement;
				}
			}
			if(zkMapElement == null) {
				zkMapElement = new ZLKeyValueMapping();
			}
		}
		
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
				var _i = 0;
				var _tempLayer = "gridLayer";
				var flag = layerName.lastIndexOf(_tempLayer) == 0 && layerName.length > _tempLayer.length;
				if (flag) {
					var lys = map.graphicsLayerIds;
					for (var i = lys.length - 1; i >= 0; i--) {
						var k = lys[i];
						if (k.indexOf(_tempLayer) == 0 && k.length > _tempLayer.length) {
							var i1 = k.substring(_tempLayer.length, k.length);
							var i2 = layerName.substring(_tempLayer.length, layerName.length);
							if (parseInt(i2) > parseInt(i1)) {
								_i = i + 1;
								break;
							}
						}
					}
					map.addLayer(wanggeLayer, _i);
				} else {
					map.addLayer(wanggeLayer, 0);
				}
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
		var newDataUrl = "";
		var parameterData={};
		function getParameter(url) {
			if(!url){return;}
			var child1 = url.split("&elementsCollectionStr=");
			newDataUrl = child1[0];
			var res = {};
			if(child1.length>1){
				res={"elementsCollectionStr":child1[1]};
			}
			var urls = newDataUrl.split('?');
			if (urls.length == 2) {
				var string_a = urls[1];
				var string = string_a.split('&');
				for(var i = 0;i<string.length;i++){
				    var str = string[i].split('=');
				    res[str[0]]=str[1];
				}
				parameterData = res;
			}
			newDataUrl = urls[0];
		}
		if (typeof(reLoadLocation) != 'undefined' && reLoadLocation != null && reLoadLocation == false) {
			successCallBackB();
		} else {
			if ("REMOTE_FEATURE" == dataUrl) {
				if (locationDatas.features != null) {
					var datas = new Array();
					for (var i = 0; i < locationDatas.features.length; i++) {
						var g = locationDatas.features[i];
						var data = {
							// id: 217875,
							// wid: 217486,
							name: g.attributes.GRID_NAME,
							x: g.attributes.CEN_LON,
							y: g.attributes.CEN_LAT,
							rings: g.geometry.rings[0],
							mapt: 5,
							lineColor: g.attributes.LINE_COLOR,
							lineWidth: g.attributes.LINE_WIDTH,
							areaColor: g.attributes.AREA_COLOR,
							nameColor: g.attributes.NAME_COLOR,
							colorNum: g.attributes.COLOR_NUM,
							editAble: false,
							elementsCollectionStr: null,
							subBusiType: null,
							address: null,
							bizId: null,
							type: null,
							infoOrgCode: g.attributes.INFO_ORG_CODE,
							gridCode: g.attributes.INFO_ORG_CODE,
							gridName: g.attributes.GRID_NAME,
							gridPath: null,
							mapCenterLevel: 11
						};
						datas.push(data);
					}
					successCallBackA(datas);
				}
			} else {
				getParameter(dataUrl);
				$.ajax({
					type: "POST",
					url: newDataUrl,
					data:parameterData,
					dataType:"json",
					async:false,
					success: successCallBackA
				});
			}
		}
		
		function successCallBackA(datas) {
			// 设置字体的大小
			var textFont = defTextFont;

			if (fontSize != null) {
				textFont = fontSize + "pt";
			}
			if (datas.length == 0 && "xiejingLayer" == layerName) {
				$.messager.alert('提示','暂无周边标准地址信息！','info');
			}

			if(datas != null) {
				$.each(datas, function(i, data) {
					var currRow = null;
					if (otherOpt.rowsObj && otherOpt.rowsObj.length > 0) {
						for (var i = 0; i < otherOpt.rowsObj.length; i++) {
							var row = otherOpt.rowsObj[i];
							var id = row[otherOpt.fieldId];
							if (data.wid == id) {
								data.name = row[otherOpt.fieldName];
								currRow = row;
								break;
							}
						}
					}
					if(data.bizType=='device'){//设备
							data.name =data.name+" "+data.eventDisposal.occurred;
					}
					if (data.elementsCollectionStr == null && parameterData.elementsCollectionStr != null) {
						data.elementsCollectionStr = parameterData.elementsCollectionStr;
					}
					// 给对象设值   用来区分点、楼宇还是网格
					data.showType = showType;
					data.layerName = layerName;

					var _data = {};

					if (isCvt) {
                        _data = $.fn.ffcsMap.toRingsDataCvt(data, true);//转换
                    } else {
                        _data = data;
                    }
                    
                    var polygon = new esri.geometry.Polygon(new esri.SpatialReference({wkid: rwkid}));
                    if (_data.coordinates) {
                    	for (var j = 0; j < _data.coordinates.length; j++) {
                            polygon.addRing(_data.coordinates[j]);
                    	}
                    }
                    if (data.rings) {
                        polygon.addRing(data.rings);
                    }
					
					//esri/geometry/

					// 边框颜色
					var RGBLineColor = defRGBLineColor;
					// 填充颜色
					var RGBAreaColor = defRGBAreaColor;

					if(_data.lineColor != "" && _data.lineColor != null){
						RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
					}

					if(_data.areaColor != "" && _data.areaColor != null){
						RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
					}

					// 边框线条宽度
					var lineWidth = defLineWidth;

					if (_data.lineWidth != "" && _data.lineWidth != null) {
						lineWidth = _data.lineWidth;
					}

					// 文字颜色
					var RGBNameColor = defRGBNameColor;

					if (_data.nameColor != "" && _data.nameColor != null) {
						RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
					}

					// 中心点坐标
					var xloc = _data.x;
					var yloc = _data.y;

					// 构造中心点
					var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid }));

					// 区域名称 data.name
					var textString = _data.gridName;

					// 网格样式    默认显示轮廓
					var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
						new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
						new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));

					// 文字样式    默认半透明
					var textSymbol = new esri.symbol.TextSymbol(textString).
						setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
						setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
						setFont(new esri.symbol.Font(textFont).
							setWeight(esri.symbol.Font.WEIGHT_BOLD));

					/*var font = new esri.symbol.Font(textFont, esri.symbol.Font.STYLE_NORMAL, esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
					 var textSymbol = new esri.symbol.TextSymbol(textString, font, new dojo.Color([0, 0, 255]));
					 */

					// 0  点
					// 1  楼宇  地图默认为隐形，鼠标移上去时显示（显示区域填充颜色（半透明））
					// 2  网格  地图默认为显性，鼠标移上去时显示（显示区域填充颜色（半透明））
					if (data.showType == 0) {
						if (imgUrl == '' || imgUrl == undefined) {
							imgUrl = 'images/GreenShinyPin.png';
						}
						//类型不同，定位图标也不同
						if(typeof data.elementsCollectionStr != "undefined" && data.elementsCollectionStr != null ){
							var iconUrl = uiDomain +analysisOfElementsCollection(data.elementsCollectionStr,"smallIco");
							if (typeof iconUrl != "undefined" && iconUrl != null && iconUrl.length > 0) {
								imgUrl = iconUrl;
							}
						}
						if (typeof data.subBusiType != "undefined" && typeof keyPlaceIconMap != "undefined") {
							var iconUrl = keyPlaceIconMap.get(data.subBusiType);
							if (typeof iconUrl != "undefined" && iconUrl != null && iconUrl.length > 0) {
								imgUrl = iconUrl;
							}
						}
						_data.isShowMarkerName = otherOpt.isShowMarkerName;
						// 文字
						textSymbol.setColor(new dojo.Color([255, 0, 0, otherOpt.isShowMarkerName]));
						textSymbol.setFont(new esri.symbol.Font("12pt").setWeight(esri.symbol.Font.WEIGHT_BOLD));
						textSymbol.setOffset(0, 20);
						var graphicsText = new esri.Graphic(point, textSymbol);
						//pointLayer.add(graphicsText);

						var symbol = new esri.symbol.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
						if (typeof otherOpt.preMakeFunc == "function") {
							var iconObj = otherOpt.preMakeFunc.call(this, currRow);
							if (iconObj != null) {
								symbol = new gPictureMarkerSymbol(iconObj.url, iconObj.width, iconObj.height);
							}
						}

						var p = new esri.geometry.Point(data.x, data.y,new esri.SpatialReference({ wkid: rwkid }));

						var graphic = null;
						var infoTemplate = null;
						// 部件需要有图标选中样式，arcgis的graphic图标选中样式需要InfoTemplate
						if(typeof layerName != 'undefined' && layerName.indexOf("urbanObjectLayer")>=0){
							infoTemplate = new esri.InfoTemplate();
							graphic = new esri.Graphic(p, symbol, null, infoTemplate);
						}else{
							graphic = new esri.Graphic(p, symbol);
						}
						pointLayer.add(graphic);

						var rpData = {};
						rpData.type = data.showType;
						rpData.textGraphic = graphicsText;
						rpData.data = _data;
						rpData.layerName = layerName;
						var isHad = 0;
						for(var m=0;m<mutilPointObjArr.length;m++){
							for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
								if (mutilPointObjArr[m].layerName != layerName) {
									continue;
								}
								var wid = mutilPointObjArr[m].renderPointListData[i].data._oldData.wid;
								if(data.wid == wid){
									//isHad = 1;
									break;
								}
							}
						}
						//if(isHad == 0){
							renderPointListData.push(rpData);
							p.wid = _data._oldData.wid;
							zkMapElement.put(p,null);
							graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
						//}

					} else if (data.showType == 1) {
						// 无填充色 data.areaColor
						symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _data.colorNum]));
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
						if(isShowArea == true) {
							symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _data.colorNum]));
						}
						var graphic = new esri.Graphic(polygon, symbol);
						wanggeLayer.add(graphic);


						// 网格中的文字默认半透明显示
						var graphicsText;
						var wgPointLayer;
						if(isCPImg){
							var picsymbol = new gPictureMarkerSymbol(js_ctx +'/js/map/arcgis/library/style/images/wglocal.png', 20, 28);
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
							showWgCenterPoint(wgPointLayer, null, true);
						}else{
							graphicsText = new esri.Graphic(point, textSymbol);
							wanggeLayer.add(graphicsText);
							showWgCenterPoint(graphicsText, null, true);
						}

						graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});

						map.on("zoom-end",function(evt){
							showWgCenterPoint(graphicsText, wgPointLayer, false);
						});
					}

					// 设值     类型，文字
//				if (graphic != null) {
//					graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
//				}
				});

				// 立体防控
				if ("idssControlLayer" == layerName) {
					defLineColor = "#FF0000";
					defRGBLineColor = new dojo.Color(defLineColor).toRgb();
					defLineWidth = 2;

					$.getJSON(dataUrl, function(datas) {
						if(datas != null && datas.length>0) {
							for (var i = 0, j = datas.length; i < j; i++) {
								linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
								polyline.addPath(linePoints);
								var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), defLineWidth);
								var graphic = new esri.Graphic(polyline, symbol);
								pointLayer.add(graphic);
							}
						}
					});
				}

				var mpObj = {};
				mpObj.layerName = layerName;
				mpObj.renderPointListData = renderPointListData;
				mpObj.zkMapElement = zkMapElement;
				mutilPointObjArr.push(mpObj);
				renderPointListData = [];

				for(var m=0; m<mutilPointObjArr.length; m++){
					for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
						var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
						if(div){
							div.style.visibility = "visible";
						}
					}
				}

				//======20150529修改 begin======
				if (showType == 0) {
					createPointCounttList=[];
					for(var m=0; m<mutilPointObjArr.length ; m++){
						if(mutilPointObjArr[m].layerName==layerName){
							for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
								var label = mutilPointObjArr[m].zkMapElement.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
								$.fn.ffcsMap.createPointCounttDiv(mutilPointObjArr[m].zkMapElement, mutilPointObjArr[m].zkMapElement.elements[i], label, m, i,mutilPointObjArr[m].layerName);
							}
						} else if (typeof otherOpt != 'undefined' && typeof otherOpt.displayStyle != 'undefined' && otherOpt.displayStyle != "1") {
							$.fn.ffcsMap.clearWithOutNumLayer({layerName : mutilPointObjArr[m].layerName});
							for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
								var point = mutilPointObjArr[m].zkMapElement.elements[i].key;
								var screen = map.toScreen(point);

								var div = mutilPointObjArr[m].zkMapElement.get(point);
								if(div != null && typeof(div) != 'undefined'){
									if (!$(div).find(':first-child').hasClass('mapbox')) {
										if(typeof(div.style) != 'undefined'){
											div.style.display = "none";
										}
										if(typeof(div.innerHTML) != 'undefined'){
											div.innerHTML = "";
										}
									}
								}
							}

						}
					}
				}

			}
			//======20150529修改 end======
			//回调
			if(typeof renderCallbackFun == "function" && datas != null){
				renderCallbackFun(datas);
			}
		}
		
		function successCallBackB() {
			// 设置字体的大小
			var textFont = defTextFont;

			if (fontSize != null) {
				textFont = fontSize + "pt";
			}
			getParameter(dataUrl);
			$.each(locationDatas, function(i, data) {
				var currRow = null;
				if (otherOpt.rowsObj && otherOpt.rowsObj.length > 0) {
					for (var i = 0; i < otherOpt.rowsObj.length; i++) {
						var row = otherOpt.rowsObj[i];
						var id = row[otherOpt.fieldId];
						if (data.wid == id) {
							data.name = row[otherOpt.fieldName];
							currRow = row;
							break;
						}
					}
				}
				if (data.elementsCollectionStr == null && parameterData.elementsCollectionStr != null) {
					data.elementsCollectionStr = parameterData.elementsCollectionStr;
				}
				// 给对象设值   用来区分点、楼宇还是网格
				data.showType = showType;
				data.layerName = layerName;

				if (isCvt) {
                    _data = $.fn.ffcsMap.toRingsDataCvt(data, true);// 转换
                } else {
                    if (!data.gridName) {
                        data.gridName = data.name;
                    }
                    _data = data;
                    _data._oldData = data;
                }

                var polygon = new esri.geometry.Polygon(new esri.SpatialReference({wkid: rwkid}));
                if (_data.coordinates) {
                	for (var j = 0; j < _data.coordinates.length; j++) {
                        polygon.addRing(_data.coordinates[j]);
                	}
                }

				// 边框颜色
				var RGBLineColor = defRGBLineColor;
				// 填充颜色
				var RGBAreaColor = defRGBAreaColor;

				if(_data.lineColor != "" && _data.lineColor != null){
					RGBLineColor = new dojo.Color(_data.lineColor).toRgb();
				}

				if(_data.areaColor != "" && _data.areaColor != null){
					RGBAreaColor = new dojo.Color(_data.areaColor).toRgb();
				}

				// 边框线条宽度
				var lineWidth = defLineWidth;

				if (_data.lineWidth != "" && _data.lineWidth != null) {
					lineWidth = _data.lineWidth;
				}

				// 文字颜色
				var RGBNameColor = defRGBNameColor;

				if (_data.nameColor != "" && _data.nameColor != null) {
					RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
				}

				// 中心点坐标
				var xloc = _data.x;
				var yloc = _data.y;

				// 构造中心点
				var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid }));

				// 区域名称 data.name
				var textString = _data.gridName;

				// 网格样式    默认显示轮廓
				var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
					new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
					new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));

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
					if (typeof data.subBusiType != "undefined" && typeof keyPlaceIconMap != "undefined") {
						var iconUrl = keyPlaceIconMap.get(data.subBusiType);
						if (typeof iconUrl != "undefined" && iconUrl != null && iconUrl.length > 0) {
							imgUrl = iconUrl;
						}
					}
					_data.isShowMarkerName = otherOpt.isShowMarkerName;
					// 文字
					textSymbol.setColor(new dojo.Color([255, 0, 0, otherOpt.isShowMarkerName]));
					textSymbol.setFont(new esri.symbol.Font("12pt").setWeight(esri.symbol.Font.WEIGHT_BOLD));
					textSymbol.setOffset(0, 20);
					var graphicsText = new esri.Graphic(point, textSymbol);
					// pointLayer.add(graphicsText);

					var symbol = new esri.symbol.PictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					if (typeof otherOpt.preMakeFunc == "function") {
						var iconObj = otherOpt.preMakeFunc.call(this, currRow);
						if (iconObj != null) {
							symbol = new gPictureMarkerSymbol(iconObj.url, iconObj.width, iconObj.height);
						}
					}

					var p = new esri.geometry.Point(data.x, data.y,new esri.SpatialReference({ wkid: rwkid }));

					var graphic = new esri.Graphic(p, symbol);
					pointLayer.add(graphic);

					var rpData = {};
					rpData.type = data.showType;
					rpData.textGraphic = graphicsText;
					rpData.data = _data;
					rpData.layerName = layerName;
					var isHad = 0;
					for(var m=0;m<mutilPointObjArr.length;m++){
						for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
							if (mutilPointObjArr[m].layerName != layerName) {
								continue;
							}
							var wid = mutilPointObjArr[m].renderPointListData[i].data._oldData.wid;
							if(data.wid == wid){
								isHad = 1;
								break;
							}
						}
					}
					//if(isHad == 0){
						renderPointListData.push(rpData);
						p.wid = _data._oldData.wid;
						zkMapElement.put(p,null);
						graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
					//}

				} else if (data.showType == 1) {
					// 无填充色 data.areaColor
					symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _data.colorNum]));
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
					if(isShowArea == true) {
						symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _data.colorNum]));
					}
					var graphic = new esri.Graphic(polygon, symbol);
					wanggeLayer.add(graphic);


					// 网格中的文字默认半透明显示
					var graphicsText;
					if(isCPImg){
						var picsymbol = new gPictureMarkerSymbol(js_ctx +'/js/map/arcgis/library/style/images/wglocal.png', 20, 28);
						graphicsText = new esri.Graphic(point, picsymbol);


						wgPointLayer = new esri.layers.GraphicsLayer();
						wgPointLayer.add(graphicsText);

						wgPointLayer.on("click",function(evt){
							if(callSubwg){
								callSubwg(_data);
							}
						});


						map.addLayer(wgPointLayer);
					}else{
						graphicsText = new esri.Graphic(point, textSymbol);
						wanggeLayer.add(graphicsText);
					}

					graphic.setAttributes( {"type":data.showType, "textGraphic":graphicsText, "data":_data, "layerName":layerName});
				}

			});


			// 立体防控
			if ("idssControlLayer" == layerName) {
				defLineColor = "#FF0000";
				defRGBLineColor = new dojo.Color(defLineColor).toRgb();
				defLineWidth = 2;

				$.getJSON(dataUrl, function(datas) {
					if(datas != null && datas.length>0) {
						for (var i = 0, j = datas.length; i < j; i++) {
							linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
							polyline.addPath(linePoints);
							var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), defLineWidth);
							var graphic = new esri.Graphic(polyline, symbol);
							pointLayer.add(graphic);
						}
					}
				});
			}

			var mpObj = {};
			mpObj.layerName = layerName;
			mpObj.renderPointListData = renderPointListData;
			mpObj.zkMapElement = zkMapElement;
			mutilPointObjArr.push(mpObj);
			renderPointListData = [];

			for(var m=0; m<mutilPointObjArr.length; m++){
				for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
					var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
					if(div){
						div.style.visibility = "visible";
					}
				}
			}

			//======20150529修改 begin======
			if (showType == 0) {
				createPointCounttList=[];
				for(var m=0; m<mutilPointObjArr.length ; m++){
					if(mutilPointObjArr[m].layerName==layerName){
						for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
							var label = mutilPointObjArr[m].zkMapElement.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
							$.fn.ffcsMap.createPointCounttDiv(mutilPointObjArr[m].zkMapElement, mutilPointObjArr[m].zkMapElement.elements[i], label, m, i,mutilPointObjArr[m].layerName);
						}
					} else if (typeof otherOpt != 'undefined' && typeof otherOpt.displayStyle != 'undefined' && otherOpt.displayStyle != "1") {

						for(var i=0 ; i< mutilPointObjArr[m].zkMapElement.elements.length; i++){
							var point = mutilPointObjArr[m].zkMapElement.elements[i].key;
							var screen = map.toScreen(point);

							var div = mutilPointObjArr[m].zkMapElement.get(point);
							if(div != null && typeof(div) != 'undefined'){
								if (!$(div).find(':first-child').hasClass('mapbox')) {
									if(typeof(div.style) != 'undefined'){
										div.style.display = "none";
									}
									if(typeof(div.innerHTML) != 'undefined'){
										div.innerHTML = "";
									}
								}
							}
						}
						$.fn.ffcsMap.clearWithOutNumLayer({layerName : mutilPointObjArr[m].layerName});

					}
				}
			}
			//回调
			if(typeof renderCallbackFun == "function"){
                renderCallbackFun(locationDatas);
			}
		}

		function showWgCenterPoint(graphicsText,wgPointLayer,clearFlag){
			if(typeof wanggeLayer != 'undefined' && wanggeLayer != null) {
				var mapLevel = map.getLevel();
				if(mapLevel < 0){
					mapLevel = 0;
				}
				var gridLevel = currentGridLevel;
				var showWgPointMapLevel = 0;
				if(typeof gridLevel != 'undefined' && gridLevel != null){
					if(gridLevel == 1 && typeof MAP_LEVEL_TRIG_CONDITION_PROVINCE != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_PROVINCE;
					}else if(gridLevel == 2 && typeof MAP_LEVEL_TRIG_CONDITION_CITY != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_CITY;
					}else if(gridLevel == 3 && typeof MAP_LEVEL_TRIG_CONDITION_COUNTY != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_COUNTY;
					}else if(gridLevel == 4 && typeof MAP_LEVEL_TRIG_CONDITION_STREET != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_STREET;
					}else if(gridLevel == 5 && typeof MAP_LEVEL_TRIG_CONDITION_COMMUNITY != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_COMMUNITY;
					}else if(gridLevel == 6 && typeof MAP_LEVEL_TRIG_CONDITION_GRID != 'undefined'){
						showWgPointMapLevel = MAP_LEVEL_TRIG_CONDITION_GRID;
					}else{
						showWgPointMapLevel = 0;
					}
				}
				if(typeof wanggeLayer != 'undefined' && wanggeLayer != null && mapLevel >= Number(showWgPointMapLevel)){
					if (isCPImg && typeof wgPointLayer != 'undefined' && wanggeLayer != null) {
						map.addLayer(wgPointLayer);
					} else {
						if(clearFlag){
							wanggeLayer.add(graphicsText);
						}else{
							graphicsText.show();
						}
					}
				}else{
					if (isCPImg && typeof wgPointLayer != 'undefined' && wanggeLayer != null) {
						map.removeLayer(wgPointLayer);
					} else {
						if(clearFlag && mapLevel >= Number(showWgPointMapLevel)){
							//wanggeLayer.add(graphicsText);
						}else{
							graphicsText.hide();
						}

					}
				}
			}
		}
		
	    // 当前鼠标移动所在图形
		function mouseMove(evt) {
			var graphic = evt.graphic;
			// 文字没有attributes属性
			if (graphic.attributes != null) {
				var _data = graphic.attributes.data;
				var type = graphic.attributes.type;
				var symbol = graphic.symbol;
				var currentLayerName = graphic.attributes.layerName;
				
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
					
					var mutilPoint_data = graphic.attributes.data;
					var curr_pointList = [];
								
					for(var m=0;m<mutilPointObjArr.length;m++){
						for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
							if (mutilPointObjArr[m].layerName != currentLayerName) {
								continue;
							}
						
							var rx = mutilPointObjArr[m].renderPointListData[i].data["x"];
							var ry = mutilPointObjArr[m].renderPointListData[i].data["y"];
							var isAnotherGroup=false;
							if(createPointCounttList){//如果当前点属于别的组别，即已被别的点位合并，不展示
								for(var j=0;j<createPointCounttList.length;j++){
									if(createPointCounttList[j].x==rx && 
											createPointCounttList[j].y==ry && 
											createPointCounttList[j].group==graphic.geometry.wid){
										//如果当前点属于本点的组
										isAnotherGroup=true;
									}else if(mutilPointObjArr[m].renderPointListData[i].data["_oldData"].wid
											==graphic.geometry.wid){//如果是当前点也加入
										isAnotherGroup=true;
									}
								}
							}
							var name = mutilPointObjArr[m].renderPointListData[i].data["gridName"];
							var thisPoint1=new esri.geometry.Point(mutilPoint_data["x"],mutilPoint_data["y"]);
							var thisPoint2=new esri.geometry.Point(rx,ry);
							if(caculateLLRB(thisPoint1,thisPoint2) && isAnotherGroup){
								curr_pointList.push(mutilPointObjArr[m].renderPointListData[i]);
							}
						}
					}
					
					if(curr_pointList.length > 1){
						ptitle = "";
						for(var i=0; i< curr_pointList.length ; i++){
							var temp_ptitle = curr_pointList[i].textGraphic.symbol.text;
							
							/*
							if (ptitle.indexOf(temp_ptitle) == -1) {
								ptitle += "," + temp_ptitle;
							}
							
							if (ptitle.length > 0) {
								ptitle = ptitle.substring(1, ptitle.length);
							}
							*/
							
							if(i==0){
								 ptitle = temp_ptitle;
							}else{
								 if (ptitle.indexOf(temp_ptitle) == -1) {
								 	 ptitle = ptitle +","+ temp_ptitle ;
								 }
							}
						}
					}
					
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
					//网格选中边框高亮
					symbol.setOutline(new esri.symbol.SimpleLineSymbol("solid", new dojo.Color([0,255,255]),3));
					//graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
					var ptitle = graphic.attributes.data.gridName;
					if(ptitle){
						$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
					}
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
					var isShow = 0;
					if (typeof _data.isShowMarkerName != "undefined") isShow = _data.isShowMarkerName;
					// 不显示文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([255, 0, 0, isShow]));
					textGraphic.setSymbol(textSymbol);
				} else if (type == 1) {
					symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0])); // 边界
					graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
					
					// 不显示文字
					var textGraphic = graphic.attributes.textGraphic;
					var textSymbol = textGraphic.symbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 0]));
					textGraphic.setSymbol(textSymbol);
				} else { // 网格 无填充
					//grid去除高亮
					symbol.setOutline(new esri.symbol.SimpleLineSymbol("solid", new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), _data.lineWidth));
					//graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
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
	var trajectoryLayer;
	var dynamicTiming;
	var timing;
	$.fn.ffcsMap.trajectory = function(trajectoryType,title,layerName,dataUrl,lineColor,imgUrl,imgWidth,imgHeight,intervalTime,startTime,endTime,backFn) {//样式默认定义
		$.fn.ffcsMap.clearTrajectory();
		if (document.getElementById('closeTrajectoryDivSpan') != undefined) {
			document.getElementById('closeTrajectoryDivSpan').click();
		}
		var innerDataUrl=dataUrl;
		var polyline = new esri.geometry.Polyline();
		var IMGpoint = new esri.geometry.Point(0, 0);
		var bottom  = 10;
		var left = (map.width-200) / 2;
		var n=1;
		var points = [];
		var linePoints = [];
		var lineStyle = "solid"; // 边框样式
		var defLineColor = "#FF0000";// 边框颜色 [138,43,226];  // 边框颜色
		var datas;
		var dynamicDatas;
		if(lineColor != undefined && lineColor != null) {
			defLineColor = lineColor;
		}
		// 十六进制颜色转为RGB
		var defRGBLineColor = new dojo.Color(defLineColor).toRgb();
		var lineWidth = 2;
		

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
			//盐都优化 开始结束时间为轨迹开始和轨迹结束时间
			if(datas.length>0){
				startTime = datas[0].locateTime;
				endTime = datas[datas.length-1].locateTime;
			}
			if("realTime"==trajectoryType) {
				if(datas != null && datas.length>0) {
					if (trajectoryDiv != null) {
						trajectoryDiv.innerHTML = "";
					}
					showTrajectoryDiv();
					for(var i=0;i<datas.length;i++) {
						linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
					}
					polyline.addPath(linePoints);
					var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
					var graphic = new esri.Graphic(polyline, symbol);
					graphic.setAttributes( {"layerName":layerName});
					IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
					var IMGsymbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					var IMGgraphic = new esri.Graphic(IMGpoint, IMGsymbol);
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
					$.fn.ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"})
					$.fn.ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"})

					if (trajectoryDiv != null) {
						trajectoryDiv.innerHTML = "";
					}
					showTrajectoryDiv();
					linePoints[0]= new esri.geometry.Point(datas[0].x,datas[0].y);
					for(var i=0;i<datas.length;i++) {
						linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
					}

					//polyline.addPath(linePoints);
					n=1;
                    // if(datas[i].isInGrid == 'Y'){
                    //     lineStyle = 'solid';
                    // }else{
                    //     lineStyle = 'short-dot';
                    // }
					var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
					var graphic = new esri.Graphic(polyline, symbol);
					graphic.setAttributes( {"layerName":layerName});
					IMGpoint.update(datas[0].x,datas[0].y);
					var IMGsymbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
					var IMGgraphic = new esri.Graphic(IMGpoint, IMGsymbol);
					// trajectoryLayer.add(graphic);
					// trajectoryLayer.add(IMGgraphic);

					playTrajectory(intervalTime);
					$.fn.ffcsMap.centerAt({x:datas[0].x,y:datas[0].y,zoom:-1});
				}else{
					alert("该时间段无轨迹定位数据");
				}
			}
			
			if (backFn) backFn();
		});
		
		function dynamicTrajectory(currentN){
			$.fn.ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"})
			$.fn.ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"})
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
			//linePoints = [];
			//for(var i=0;i<=currentN;i++) {
			//	linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
			//}
			//polyline.removePath(0);
			//polyline.addPath(linePoints);
			//var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
			//var graphic = new esri.Graphic(polyline, symbol);
			//graphic.setAttributes( {"layerName":layerName});
			IMGpoint.update(datas[currentN].x,datas[currentN].y);
			var IMGsymbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
			var IMGgraphic = new esri.Graphic(IMGpoint, IMGsymbol);
			//trajectoryLayer.add(graphic);
			trajectoryLayer.add(IMGgraphic);
			
			
			trajectoryLayer.redraw();
			n=currentN;
			runTrajectoryProgress()
			n++;
			
		}
		function playTrajectory(time){
			var imgobj = document.getElementById('playButton');
			if(imgobj.src.indexOf("gj_07.png")>0) {
				imgobj.src = imgobj.src.replace("gj_07.png","gj_09.png")
			}
			// 执行前先清除定时器
			window.clearInterval(timing);
			window.clearInterval(dynamicTiming);
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
							$.fn.ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"})
							$.fn.ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"})
							IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
							linePoints = [];
							for(var i=0;i<datas.length;i++) {
								linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
							}
							polyline.removePath(0);
							polyline.addPath(linePoints);
							var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
							var graphic = new esri.Graphic(polyline, symbol);
							graphic.setAttributes( {"layerName":layerName});
							IMGpoint.update(datas[datas.length-1].x,datas[datas.length-1].y);
							var IMGsymbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
							var IMGgraphic = new esri.Graphic(IMGpoint, IMGsymbol);
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
				if(imgobj.src.indexOf("gj_07.png")>0) {
					imgobj.src = imgobj.src.replace("gj_07.png","gj_09.png")
				}
				timing = setInterval(function(){
					if( n<datas.length) {
						
						//polyline.insertPoint(0,n,new Point(datas[n].x,datas[n].y));
						//IMGpoint.update(datas[n].x,datas[n].y);

						//$.fn.ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"});
						//$.fn.ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"});
						//linePoints = [];
						//for(var i=0;i<=n;i++) {
						//	linePoints[i]= new esri.geometry.Point(datas[i].x,datas[i].y);
						//}
						//var aa = polyline.removePath(0);
						//polyline.addPath(linePoints);
						//var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
						//var graphic = new esri.Graphic(polyline, symbol);
						//graphic.setAttributes( {"layerName":layerName});


						var linePoints = [];
                        linePoints[0] = new esri.geometry.Point(datas[n-1].x,datas[n-1].y);
						linePoints[1]= new esri.geometry.Point(datas[n].x,datas[n].y);
                        var polyline1 = new esri.geometry.Polyline();
                        polyline1.addPath(linePoints);
                        if(datas[n] != null && typeof datas[n] != 'undefined' && typeof datas[n].isInGrid != 'undefiend' && datas[n].isInGrid == 'N'){
                            lineStyle = 'dash';
                        }else{
                            lineStyle = 'solid';
                        }
                        var symbol = new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1]), lineWidth);
                        var graphic = new esri.Graphic(polyline1, symbol);
                        graphic.setAttributes( {"layerName":layerName});
                        trajectoryLayer.add(graphic);


						IMGpoint.update(datas[n].x,datas[n].y);
						var IMGsymbol = new gPictureMarkerSymbol(imgUrl, imgWidth, imgHeight);
						var IMGgraphic = new esri.Graphic(IMGpoint, IMGsymbol);
						//trajectoryLayer.add(graphic);
						trajectoryLayer.add(IMGgraphic);
						
						trajectoryLayer.redraw();
						document.getElementById("centerTimeDiv").innerHTML=datas[n].locateTime;
						runTrajectoryProgress();
						n++;
					}else {
						stopTrajectory();
					}
				},time*500);
			}
		}
		function stopTrajectory(){
			var imgobj = document.getElementById('playButton');
			window.clearInterval(timing);
			if("realTime"==trajectoryType) {
				window.clearInterval(dynamicTiming);
			}
			if(imgobj.src.indexOf("gj_09.png")>0) {
				imgobj.src = imgobj.src.replace("gj_09.png","gj_07.png")
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
                $.fn.ffcsMap.clearTrajectory();//关闭轨迹窗口后清除轨迹
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
		        mystr2 +="<p><a href=\"javascript:void(0)\" style=\"background:#751ece;\"><img title=\"播放/暂停\" id=\"playButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_09.png\" /></a></p>";
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
				mystr2 +=               "<td width=\"120\" align=\"center\"><a href=\"javascript:void(0)\" style=\"background:#751ece;\"><img title=\"播放/暂停\" id=\"playButton\" src=\""+js_ctx+"/theme/arcgis/standardmappage/images/gj_09.png\" /></a></td>";
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
				if(imgobj.src.indexOf("gj_07.png")>0) {
					playTrajectory(intervalTime);
				}else if(imgobj.src.indexOf("gj_09.png")>0) {
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

	$.fn.ffcsMap.clearTrajectory = function() {//样式默认定义
		if (trajectoryDiv!= null) {

			if(timing != null && typeof(timing) != 'undefined') {
				window.clearInterval(timing);
			}

			if(dynamicTiming != null && typeof(dynamicTiming) != 'undefined') {
				window.clearInterval(dynamicTiming);
			}
			document.getElementById('trajectoryDiv').innerHTML='';
			$.fn.ffcsMap.clearWithOutNumLayer({layerName : "gridAdminTrajectoryLayer"})
			$.fn.ffcsMap.clearWithOutNumLayer({layerName : "careRoadmemberTrajectoryLayer"})
		}

		$("#map"+currentN).ffcsMap.clear({layerName : 'gridAdminTrajectoryLayer'});
		if(trajectoryLayer != null && typeof(trajectoryLayer) != 'undefined'){
			map.removeLayer(trajectoryLayer);
			$("#map"+currentN).ffcsMap.clear({layerName : trajectoryLayer.id});
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
		dialog = new dijit.TooltipDialog({
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
          var highlightGraphic = new esri.Graphic(evt.graphic.geometry,highlightSymbol);
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
		isDrawEdit = true;
		editToolbar.deactivate();
		toolbar.activate(esri.toolbars.Draw[drawType]);
		if(drawType == "POINT"){
			//编辑时隐藏圆的对象
			$.fn.ffcsMap.hideCircleOnDraw(false);
		 }
		toolbar.on("draw-complete", function() {
			isDrawEdit = false;
			drawData = $.fn.ffcsMap.getShapeInfo(returnDataGraphic);
			
			if(ffcs_curFeature != undefined){
				ffcs_curFeature.setGraphic(returnDataGraphic);
			}
			
			
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
	 * @param  isMoveCPoint 是否可改变中心点
	 */
	$.fn.ffcsMap.edit = function(isEdit,callBack ,isMoveCPoint){
		if(isMoveCPoint == undefined){
			isMoveCPoint = true;
		}
		
		// 获取graphicsLayer图层Id
		var graphicsLayerIds = map.graphicsLayerIds;
		 // 图元编辑结束
		dojo.connect(map, "onClick", function(evt) {
			editToolbar.deactivate();
		});
		for (var i = 0; i < graphicsLayerIds.length; i++) {
			var layer = map.getLayer(graphicsLayerIds[i]);
			if(typeof layer != "undefined" && layer != null && layer != ""){
				layer.on("click", function(evt) {
					//begin 控制轮廓数据是否不编辑 ----------
					var _isedit = true;

					var od_isedit;
					if (evt.graphic.attributes != null) {
						if(evt.graphic.attributes["DATA"]!=undefined){
							var feaData = JSON.parse(evt.graphic.attributes["DATA"]);
							od_isedit = feaData._oldData["editAble"];
						}else{
							od_isedit  = evt.graphic.attributes.data._oldData["editAble"];
						}
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
        
        //点移动停止
        editToolbar.on("vertex-move-stop", function(evt){
        	_graphic = evt.graphic;
        	editStop(evt);
        });
        
        // 拖动
        editToolbar.on("graphic-move-stop", function(evt){
        	editStop(evt);
        });
        
        // 缩小放大
        editToolbar.on("scale-stop", function(evt){
        	editStop(evt);
        });
        
        // 旋转
        editToolbar.on("rotate-stop", function(evt){
        	editStop(evt);
        });
        
        function editStop(evt) {
        	if(isDrawEdit){ 
        		//editToolbar.deactivate();
        		var data = $.fn.ffcsMap.getShapeInfo(_graphic);
        		
        		if(ffcs_curFeature != undefined){
        			ffcs_curFeature.setGraphic(_graphic);
        			//var _feaLayer = evt.graphic.getLayer();
        			//ffcs_curFeature.setEsriLayer(_feaLayer);
        		}
        		
        		_graphic = null;
        		
        		if (data != null) {
        			//console.log("后台：编辑后返回的数据 : " + data);
        			callBack(data); // 回调函数
        			//isDrawEdit = false; // should be click edit button again when you want to edit graphic
        		}
        	}
        }
	        
        //激活编辑工具 
	    function activateToolbar(graphic) {
	          var tool = 0;
	          
	          //if (dijit.registry.byId("tool_move").checked) {
	            tool = tool | Edit.MOVE; 
	          //}
	          //if (dijit.registry.byId("tool_vertices").checked) {
	            tool = tool | Edit.EDIT_VERTICES; 
	          //}
	          //if (dijit.registry.byId("tool_scale").checked) {
	            tool = tool | Edit.SCALE; 
	          //}
	          //if (dijit.registry.byId("tool_rotate").checked) {
	            tool = tool | Edit.ROTATE; 
	          //}
	          // enable text editing if a graphic uses a text symbol
        	  if ( graphic.symbol.declaredClass === "esri.symbol.TextSymbol" ) {
        		  if(isMoveCPoint){
        			  tool = tool | Edit.EDIT_TEXT ; 
        		  }else{
        			  return;
        		  }
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
	     var template = new esri.InfoTemplate();
	     template.setTitle(allCfg.title);
	     template.setContent(allCfg.content);
	     
	     return template;
	};
	
	/**
	 * 
	 * 为map设置信息窗口
	 */
	$.fn.ffcsMap.setInfoWindow = function(templateCfg){
		 var _infoWindow = new esri.dijit.InfoWindowLite(null, domConstruct.create("div", null, null, map.root));
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
		 var petroFieldsFL = new esri.layers.FeatureLayer(url, {
	          mode: esri.layers.FeatureLayer.MODE_ONDEMAND, //MODE_SELECTION,
	          infoTemplate : _infoTemplate,
	          outFields: ["approxacre","objectid","field_name","activeprod","cumm_oil","cumm_gas","avgdepth"]
//	          outFields: ["*"] // 获取图层中所有字段的值
	        });
		 
	     var selectionSymbol = new esri.symbol.SimpleFillSymbol(
	    		 esri.symbol.SimpleFillSymbol.STYLE_NULL, 
	          new esri.symbol.SimpleLineSymbol(
	            "solid", 
	            new dojo.Color("yellow"), 
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
	          var selectQuery = new esri.tasks.Query();

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

	          var attInspector = new esri.dijit.AttributeInspector({
	            layerInfos:layerInfos
	          }, domConstruct.create("div"));
	          
	          //add a save button next to the delete button
	          var saveButton = new dijit.form.Button({ label: "Save", "class": "saveButton"});
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
	            //console.log("Next " + updateFeature.attributes.objectid);
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
			newObj.colorNum = OldData.colorNum;//区域透明度
		}else{//点
			
		}
		
		if(isJson){
			return newObj;
		}
		
		return JSON.stringify(newObj);
	};
	
	$.fn.ffcsMap.toRingsDataCvt = function (OldData, isJson) {

        var newObj = {};

        newObj.x = OldData.x;
        newObj.y = OldData.y;

        newObj.type = OldData.type;//"louyu1"; 网格类型
        newObj.coordinates = [];

        var temp_hs = new Array();

        if (OldData.hs != undefined) {
            temp_hs = OldData.hs.split('@');
            
            for (var i = 0; i < temp_hs.length; i++) {
            	if (temp_hs[i]) {
            		var tempCrd = [];
            		var temp_xy = temp_hs[i].split(',');
                    for (var j = 0; j < temp_xy.length; j+=2) {
                        // 转成float
                    	var p = new esri.geometry.Point(parseFloat(temp_xy[j]), parseFloat(temp_xy[j + 1]));
                    	tempCrd.push(p);
                    }
                    newObj.coordinates.push(tempCrd);
            	}
            }
        }
        
        newObj.lineColor = OldData.lineColor; //边界线颜色
        newObj.lineWidth = OldData.lineWidth; //边界线宽度
        newObj.areaColor = OldData.areaColor; //区域颜色
        newObj.nameColor = OldData.nameColor; //区域颜色
        newObj.gridName = OldData.name;  //网格名称
        newObj._oldData = OldData;
        newObj.colorNum = OldData.colorNum;//区域透明度

        if (isJson) {
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
			
			var markerSymbol = new gPictureMarkerSymbol(
					pointOpt.imageUrl,pointOpt.imageWidth,pointOpt.imageHeight);
			var markerGraphic = new esri.Graphic(point, markerSymbol);
			
			layer.add(markerGraphic);
			
			if(text!==undefined && text!==""){
				var textMarkerSymbol = new gPictureMarkerSymbol(
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
		if (options != null && options.x != null && options.y !=null){
            var wkid = map.spatialReference["wkid"];

            var point = new esri.geometry.Point([options.x,
                    options.y],
                new esri.SpatialReference({wkid : wkid})
            );
            if(options.zoom>0){
                map.setLevel(options.zoom);
                map.centerAndZoom(point,options.zoom);
            }else{
                map.centerAt(point);
            }
            $.fn.ffcsMap.setScreenPostition();
		}
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
	 * @param showSkyline(showData)   增加3D显示  add by 20150421 
	 */
	var mend_runCount = 0;
	$.fn.ffcsMap.ffcsDisplayhot = function(opt, layerName, title, createDlg, showDetail ,showSkyline){
		//$.fn.ffcsMap.setIsFeatureLayer(false); //点图层设非要素图层
		//tempLayer = new esri.layers.GraphicsLayer({id:'tempLayer'});
		//map.addLayer(tempLayer);
		
		var defa_opt = {
            w : 400,
            h : 200
		};
		
		opt = $.extend({}, defa_opt, opt);
		
		var graphics = map.graphicsLayerIds;
		
		var pointlocation ;
		
		var layer = map.getLayer(layerName);
		var timer = null;

		layer.on("click",function(evt){
			clearTimeout(timer);
			timer = setTimeout(function () { //在单击事件中添加一个setTimeout()函数，设置单击事件触发的时间间隔

				click(layer, evt);
			}, 300);

			//click(this, evt);
		});



		//创建右键菜单
		//createGraphicsMenu();
		//var ctxMenuForGraphics;
        //
		//function createGraphicsMenu() {
		//	// Creates right-click context menu for GRAPHICS
		//	ctxMenuForGraphics = new dojo.dijit.Menu({});
		//	ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
		//		label: "Edit",
		//		onClick: function() {
		//			if ( selected.geometry.type !== "point" ) {
		//				editToolbar.activate(Edit.EDIT_VERTICES, selected);
		//			} else {
		//				alert("Not implemented");
		//			}
		//		}
		//	}));
        //
		//	ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
		//		label: "Move",
		//		onClick: function() {
		//			editToolbar.activate(Edit.MOVE, selected);
		//		}
		//	}));
        //
		//	ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
		//		label: "Rotate/Scale",
		//		onClick: function() {
		//			if ( selected.geometry.type !== "point" ) {
		//				editToolbar.activate(Edit.ROTATE | Edit.SCALE, selected);
		//			} else {
		//				alert("Not implemented");
		//			}
		//		}
		//	}));
        //
		//	ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
		//		label: "Style",
		//		onClick: function() {
		//			alert("Not implemented");
		//		}
		//	}));
        //
		//	//ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
		//	ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
		//		label: "Delete",
		//		onClick: function() {
		//			map.graphics.remove(selected);
		//		}
		//	}));
        //
		//	ctxMenuForGraphics.startup();
        //
		//	layer.on("mouse-over", function(evt) {
		//		// We'll use this "selected" graphic to enable editing tools
		//		// on this graphic when the user click on one of the tools
		//		// listed in the menu.
		//		selected = evt.graphic;
        //
		//		// Let's bind to the graphic underneath the mouse cursor
		//		ctxMenuForGraphics.bindDomNode(evt.graphic.getDojoShape().getNode());
		//	});
        //
		//	layer.on("mouse-out", function(evt) {
		//		ctxMenuForGraphics.unBindDomNode(evt.graphic.getDojoShape().getNode());
		//	});
		//}
		//function createGraphicsMenu() {
		//	ctxMenuForGraphics = new dijit.Menu({});
        //
		//	ctxMenuForGraphics.addChild(new dijit.MenuItem({
		//		label: "删除",
		//		onClick: function () {
		//			layer.remove(selected)
		//		}
		//	}));
        //
		//	//当鼠标在gl图层的图形上方时绑定该图形的点击事件
		//	layer.on("mouse-over", function(evt) {
		//		selected = evt.graphic;
		//		ctxMenuForGraphics.bindDomNode(evt.graphic.getDojoShape().getNode());
		//	});
        //
		//	//当鼠标移出gl图层的图形上方时取消绑定该图形的点击事件
		//	layer.on("mouse-out", function(evt) {
		//		ctxMenuForGraphics.unBindDomNode(evt.graphic.getDojoShape().getNode());
		//	});
		//}

		//启动右键菜单
		//ctxMenuForGraphics.startup();

		//网格轮廓范围内双击进入下级
		if(typeof(layerName) != 'undefined' && layerName == 'gridLayer'){
			layer.on("dbl-click",function(evt){
				clearTimeout(timer); //在双击事件中，先清除前面click事件的时间处理
				//获取子级网格轮廓
				var gridLevel = 1;
				if (evt.graphic.attributes != null && evt.graphic.attributes.data!=undefined && typeof evt.graphic.attributes["data"] != 'undefined') {
					if(firstInfoOrgCode == ""){
						firstGridId = $("input[name='gridId']").val();
						firstGridCode = $("input[name='gridCode']").val();
						firstGridName = $("input[name='gridName']").val();
						firstInfoOrgCode = $("input[name='orgCode']").val();
					}
					var gridId = evt.graphic.attributes.data._oldData['wid'];
					var gridName = evt.graphic.attributes.data._oldData['name'];
					var gridCode = evt.graphic.attributes.data._oldData['gridCode'];
					var infoOrgCode = evt.graphic.attributes.data._oldData['infoOrgCode'];
					if(typeof(infoOrgCode) != 'undefined' && infoOrgCode != null){
						if(infoOrgCode.length == 2){
							gridLevel = 1;
						}else if(infoOrgCode.length == 4){
							gridLevel = 2;
						}else if(infoOrgCode.length == 6){
							gridLevel = 3;
						}else if(infoOrgCode.length == 9){
							gridLevel = 4;
						}else if(infoOrgCode.length == 12){
							gridLevel = 5;
						}else if(infoOrgCode.length == 15){
							gridLevel = 6;
						}
					}
					var _gridId = $("input[name='gridId']").val();
					if(typeof gridLevel != 'undefined' && gridLevel != null && _gridId != gridId) {
						renderGridBackBtn($("input[name='gridId']").val(), $("input[name='gridCode']").val(), $("input[name='gridName']").val(), $("input[name='orgCode']").val(), $("input[name='gridId']").val());
						changeGridInfo(gridId, gridCode, gridName, infoOrgCode);
						
						locateCenterAndLevel(gridId,currentArcgisConfigInfo.mapType);
						getArcgisDataOfChildrenGrids(evt.graphic.attributes["data"]._oldData["wid"]);
					}
				}
			});
		}

		/*
		for (var i = 0; i < graphics.length; i++) {
			var layer = map.getLayer(graphics[i]);
			
			layer.on("click",function(evt){			
				click(this, evt);
			});
			
			
			layer.on("mouse-over", function(evt) {
				mouseOver(evt);
			});
			
			layer.on("mouse-out", function(evt) {
				mouseOut(evt);
			});
		}*/
		
		map.on("zoom-end",function(e) {
			$.fn.ffcsMap.winDivGoToMarker();
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

					if (typeof evt.graphic.attributes != 'undefined' && evt.graphic.attributes != null) {
						//======201501修改 begin======
						//console.log("renderPointListData长度："+renderPointListData.length);
						var pointList = [];
						 
						for(var m=0;m<mutilPointObjArr.length;m++){
							if(layerName==mutilPointObjArr[m].layerName){
								for(var i=0; i< mutilPointObjArr[m].renderPointListData.length ; i++){
									var rx = mutilPointObjArr[m].renderPointListData[i].data["x"];
									var ry = mutilPointObjArr[m].renderPointListData[i].data["y"];
									var name = mutilPointObjArr[m].renderPointListData[i].data["gridName"];
									//翻页叠加，但是同一条记录只叠加一次
									var mWid = mutilPointObjArr[m].renderPointListData[i].data._oldData["wid"];
									var repirtNum = 0;
									for(var j=0; j<pointList.length; j++){
										var rwid = pointList[j].data._oldData["wid"];
										if(mWid == rwid){
											repirtNum = 1;
										}
									}
									//合并点判断
									var isAnotherGroup=false;
									if(createPointCounttList){//如果当前点属于别的组别，即已被别的点位合并，不展示
										for(var j=0;j<createPointCounttList.length;j++){
											if(createPointCounttList[j].x==rx && 
													createPointCounttList[j].y==ry && 
													createPointCounttList[j].group==evt.graphic.geometry.wid){
												//如果当前点属于本点的组
												isAnotherGroup=true;
											}else if(mutilPointObjArr[m].renderPointListData[i].data["_oldData"].wid
													==evt.graphic.geometry.wid){//如果是当前点也加入
												isAnotherGroup=true;
											}
										}
									}
									
									if(repirtNum == 1){
										 
									}else{
										var thisPoint1=new esri.geometry.Point(evt.graphic.attributes.data["x"],evt.graphic.attributes.data["y"]);
										var thisPoint2=new esri.geometry.Point(rx,ry);
										if(caculateLLRB(thisPoint1,thisPoint2) && isAnotherGroup){
											pointList.push(mutilPointObjArr[m].renderPointListData[i]);
										}
									}
								}
							}
						}
						
						//===========201501修改 end==========
						
						if(evt.graphic.attributes["DATA"]!=undefined){
							var feaData = JSON.parse(evt.graphic.attributes["DATA"]);
							x =  feaData.x;
							y =  feaData.y;
						}else{
							x = evt.graphic.attributes.data["x"];
							y = evt.graphic.attributes.data["y"];
						}
						
						 
						
						if(pointList.length > 1){
							$.fn.ffcsMap.mutilPointPanel(evt,pointList,opt, layerName, title, createDlg, showDetail , showSkyline);
						}else{
							if(evt.graphic.attributes["DATA"]!=undefined){
								var feaData = JSON.parse(evt.graphic.attributes["DATA"]);
								if(typeof feaData != 'undefined' && feaData != null) {
									var elementsCollectionStr = feaData['elementsCollectionStr'];
									if (typeof elementsCollectionStr != 'undefined') {
										resultInfo = createDlg(feaData._oldData);
									}
								}
							}else{
								var feaData = evt.graphic.attributes.data["_oldData"];
								if(typeof feaData != 'undefined' && feaData != null) {
									var elementsCollectionStr = feaData['elementsCollectionStr'];
									if (typeof elementsCollectionStr != 'undefined') {
										resultInfo = createDlg(feaData);
									}
								}
							}
							
						}
						
					} else {
						var data = evt.graphic.geometry;
						if(typeof data != 'undefined' && data != null){
							var elementsCollectionStr = data['elementsCollectionStr'];
							if(typeof elementsCollectionStr != 'undefined'){
								resultInfo = createDlg(data);
								x = data.x;
								y = data.y;
							}
						}



					}
					//$.fn.ffcsMap.centerAt({x:x,y:y,zoom:-1});//,zoom:map._mapParams.zoom
					var top ;//= map.toScreen(pointlocation).y ;
					var left ;//= map.toScreen(pointlocation).x ;
					top  = evt.clientY - opt.h - 51;
					left = evt.clientX - opt.w / 2;
					
					if(typeof(evt.graphic.attributes) != 'undefined' && evt.graphic.attributes["DATA"]!=undefined){
						var feaData = JSON.parse(evt.graphic.attributes["DATA"]);
						_data = feaData._oldData;
					}else{
						try {
							_data = evt.graphic.attributes.data["_oldData"];
						} catch(e) {}
					}
					
					if(typeof resultInfo != 'undefined' && resultInfo!=null && resultInfo!=""){
						_tempWid = _data['wid'];
						opt.lng = x;
						opt.lat = y;
						if(layer.id == "globalEyesLayer" || layer.id == "statGlobalEyesLayer"){//全球眼图层、全球眼汇聚图层
							var elementsCollectionStr = _data['elementsCollectionStr'];
							var wid = _data["wid"];
							var type = _data["subBusiType"];
							var arr=type.split("-");
							var channelId = _data["channelId"];
							if(arr[0]=="14"){
								if (arr[1]!=null && arr[1] == '1') {
									var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
									closeBeforeMaxJqueryWindow();//关闭之前所有的窗口
									showVideoWindow(title, menuSummaryUrl+wid, opt.w, opt.h, false);
								}else {
									showDHGlobalPlay(wid);
								}
							} else if (arr[0]=="23" && analysisOfElementsCollection(elementsCollectionStr,"orgCode").indexOf('3507')>-1){
								gloybalPlay(channelId);
							} else{
								var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
								closeBeforeMaxJqueryWindow();//关闭之前所有的窗口
								if(analysisOfElementsCollection(elementsCollectionStr,"orgCode").indexOf('621503')>-1){
                                    showVideoWindow(title, menuSummaryUrl+wid, 800, 500, false);
								}else{
									showVideoWindow(title, menuSummaryUrl+wid, opt.w, opt.h, false);
								}
							}
						}else{
							$.fn.ffcsMap.initWindow(left, top, opt, title, resultInfo, true, showDetail, _tempWid, _data ,showSkyline, null, evt.graphic);
						}

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
		//toHideZhouBianSketch();
		//$("#map").ffcsMap.clear({layerName : "gridsLayer"});
	}

	
	/**
	 * 定位弹出框
	 *
	 * @Param  dataObj  Add by 20150403 10:24
	 * 
	 * @param  showSkyline Add by 20150421 
	 * 
	 */
	$.fn.ffcsMap.initWindow = function(left, top, opt, title, resultInfo, success, showDetail, _tempWid, dataObj,showSkyline, divOffset, evt_graphic) {
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
		
		if (divOffset == null || typeof divOffset == "undefined") {
			divOffset = { left: 1, top: -51};
		}

		var mystr2 = "<div id='NorMapOpenDiv' class='NorMapOpenDiv' offset='"+divOffset.left+","+divOffset.top+"' lnglat='"+opt.lng+","+opt.lat+"' style='left: " + left + "px;top:" + top + "px';z-index:3;>";
		
		if (success) {
			mystr2 += "<div class='arrow' style='left: " + (opt.w / 2) + "px;'></div>";
		}
		
		mystr2 += "<div class='box' style='width:"+opt.w+"px;height:"+opt.h+"px'>";
		if(opt.notitle){
			mystr2 += "<div class='title' style='height:0px;'>";	
			mystr2 += "<span id='close' class='fr' style='position: absolute;left:"+(opt.w-28)+"px;color:#fff;text-align: center;'>X</span>";
			mystr2 += "</div>";	
			mystr2 += "<div style='height:"+(opt.h)+"px;' class='o_subst_info'>";
		}else{
			mystr2 += "<div class='title'>";		
			mystr2 += "<span id='close' class='fr close'></span>";
			
			// 是否显示详情
			if (showDetail != undefined) {
				mystr2 += "<span id='watch' title='查看详情' class='fr watch';></span>";
			}
			
			if(showSkyline != undefined && success){
				mystr2 += "<span id='showskylineMap' title='查看三维地图' class='fr watchSanwei';></span>";
			}
			mystr2 += title;		
			mystr2 +="</div>";
			
			mystr2 += "<span class='detail'></span>";
			mystr2 += "<div style='height:"+(opt.h-26)+"px;' class='o_subst_info'>";
		}
		
		
		mystr2 += resultInfo;
		mystr2 += "</div>";
		
		mystr2 += "</div>";
		
		mystr2 += "</div>"; 
		
		builddianpuDv.innerHTML = mystr2;
		if (showDetail != undefined) {
			document.getElementById('watch').onclick = function() {
				var items = (_tempWid + "").split("-");
				showDetail(items[0], title, dataObj);  //param add dataObj
			};
		}
		
		if (showSkyline != undefined && success) {
			document.getElementById('showskylineMap').onclick = function() {
				showSkyline(_tempWid, title, dataObj);  //param add dataObj
			};
		}

		document.getElementById('close').onclick = function() {
			//清除部件图标选中样式
			var pathElArr = document.getElementsByTagName('path');
			if(typeof pathElArr != "undefined" && pathElArr != null && pathElArr.length >0){
				for(var i=0;i< pathElArr.length;i++){
					if(typeof pathElArr[i].parentNode != 'undefined' && pathElArr[i].parentNode.id.indexOf("graphics") >0){
						pathElArr[i].parentNode.removeChild(pathElArr[i]);
						i=-1;
					}
				}

			}
			$.fn.ffcsMap.clearGridsLayer();
		};
		var _isMoveDiv = false;
		var dragMapOpenDivObj = $("#NorMapOpenDiv");
		var ox = oy = 0;
		dragMapOpenDivObj.mousedown(function(e) {
			_isMoveDiv = true;
			$(this).css("cursor", "move");
			var offset = $(this).offset();
			var x = e.pageX - offset.left;
			var y = e.pageY - offset.top;
			ox = e.pageX, oy = e.pageY;
			$(document).bind("mousemove", function(ev) {
				dragMapOpenDivObj.stop();
				map.__pan(ev.pageX - ox, ev.pageY - oy);
				var _x = ev.pageX - x;
				var _y = ev.pageY - y;
				dragMapOpenDivObj.css({
					left : _x,
					top : _y
				});
			});
		}).mouseup(function(ev) {
			if (_isMoveDiv) {
				map.__panEnd(ev.pageX - ox, ev.pageY - oy);
				_isMoveDiv = false;
			}
			dragMapOpenDivObj.css("cursor", "default");
			ox = oy = 0;
			$(document).unbind("mousemove");
		});
		
		// 获取概要信息中iframe的id值，并动态设置height，设置overflow-y属性
		var id = resultInfo.split("\""); 
		var $iframe = $("#" + id[1]);
		
		$iframe.on('load', function(){
		    var height = $iframe.height();	
			var $body = $iframe.contents().find("body");
			$body.css("height", height);
			$body.css("overflow-y", "auto");
		});

		 if(opt.lng != null && opt.lat != null ){
            if(typeof(opt['nopan'])== 'undefined')//默认移动到中心点
			setTimeout(function() {
				$.fn.ffcsMap.fixedPan(left,top,success,opt.lng,opt.lat);
			}, 500);
		 }

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
	$.fn.ffcsMap.locationAtPoint = function(layerName, wid, elementsCollectionStr, opt) {
		var defa_opt = {
				w : 400,
				h : 200
		};
		opt = $.extend({}, defa_opt, opt); 
		
		var x; // x坐标
		var y; // y坐标
		var graphic; 
		var success = false; // 是否找到该定位点
		//var tempLayer; // 临时图层
		var _data;
		var _tempWid;
		
		/*if (map.getLayer('tempLayer') == undefined) {
			tempLayer = new esri.GraphicsLayer({id:'tempLayer'});
			map.addLayer(tempLayer);
		} else {
			tempLayer = map.getLayer('tempLayer');
		}*/
		
		// 当前定位的图层
		var currentLayer = map.getLayer(layerName);
		
		// 获取图层所有的点元素
		var graphics = null
		if(currentLayer != null && typeof(currentLayer) != 'undefined' && currentLayer.graphics != null){
			graphics = currentLayer.graphics;
		}

		if(graphics != null && typeof(graphics.length) != 'undefined'){
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
		}

		
		// 不管是否找到清除图层
		//map.getLayer('tempLayer').clear();
			
		// 找到该点，定位
		var point;
		var rwkid = map.spatialReference["wkid"];
		if (success) {
			point = new esri.geometry.Point(x, y,new esri.SpatialReference({ wkid : rwkid }));
			if (!opt.nozoom) {
				var orgCode = $("#orgCode").val();
				var gridZoom = map.getMaxZoom() - 1;
				if (orgCode.indexOf("3209") == 0) {// 盐城市
					gridZoom = map.getMaxZoom();
				}
				if (gridZoom > 0) {
					map.setZoom(gridZoom);
				}
			}
			// 以该点为中心点进行定位
			map.centerAt(point);
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
	$.fn.ffcsMap.locationPoint = function(opt, layerName, wid, name, imgUrl, imgWidth,imgHeight, callback, showDetail,showSkyline,elementsCollectionStr) {
		var defa_opt = {
				w : 400,
				h : 200
		};
		opt = $.extend({}, defa_opt, opt); 
		
		var x; // x坐标
		var y; // y坐标
		var graphic; 
		var success = false; // 是否找到该定位点
		//var tempLayer; // 临时图层
		var _data;
		var _tempWid;
		
		/*if (map.getLayer('tempLayer') == undefined) {
			tempLayer = new esri.GraphicsLayer({id:'tempLayer'});
			map.addLayer(tempLayer);
		} else {
			tempLayer = map.getLayer('tempLayer');
		}*/
		
		// 当前定位的图层
		var currentLayer = map.getLayer(layerName);
		
		// 获取图层所有的点元素
		var graphics = null
		if(currentLayer != null && typeof(currentLayer) != 'undefined' && currentLayer.graphics != null){
			graphics = currentLayer.graphics;
		}

		if(graphics != null && typeof(graphics.length) != 'undefined'){
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
		}

		
		// 不管是否找到清除图层
		//map.getLayer('tempLayer').clear();
			
		// 找到该点，定位
		var point;
		var rwkid = map.spatialReference["wkid"];
		if (success) {
			// 添加新图片
			//var symbol = new gPictureMarkerSymbol(imgUrl,imgWidth,imgHeight);
			point = new esri.geometry.Point(x, y,new esri.SpatialReference({ wkid : rwkid }));
			//var _graphic = new Graphic(point, symbol);
			//tempLayer.add(_graphic);
			
			//_graphic.attributes = graphic.attributes;
			
			// 以该点为中心点进行定位
			//map.centerAt(point);
		}
		if(!opt.nozoom){
			map.setZoom(map.getMaxZoom() - 1);
		}
		// 显示详细信息
		showInfo();
		
		// 以该点为中心点进行定位
		//map.centerAt(point);
		
		function showInfo() {
			if (builddianpuDv != null) {
				builddianpuDv.innerHTML = "";
			}
			
			var resultInfo;
			var title;
			var _data = null;
			if (graphic != null) {
				graphic.attributes.data["_oldData"].success = success; // 设置是否成功
				resultInfo = callback(graphic.attributes.data["_oldData"]);
				_data = graphic.attributes.data["_oldData"];
			}else{
				_data = {"elementsCollectionStr":elementsCollectionStr};
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

			// top = (top > 40) ? top : 40;
			// left = (left > 70) ? left : 70;
			opt.lng = (x==null?null:x);
			opt.lat = (y==null?null:y);
			$.fn.ffcsMap.initWindow(left, typeof(opt['top'])!= 'undefined'?opt['top']:top, opt, title, resultInfo, success, showDetail, wid, _data , showSkyline);
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
		if(map != undefined){
			if(map.graphics != undefined){
				map.graphics.clear();
			}
			if(config != undefined){
				var _layerName = config.layerName;
				var flag = _layerName.lastIndexOf("*") != -1;
				var lys = map._layers;
				if (flag) {
					_layerName = _layerName.substring(0, _layerName.lastIndexOf("*"));
					for (var k in lys) {
						if (k.indexOf(_layerName) != -1) {
							var gridLy = map._layers[k];
							if(gridLy != undefined) {
								map.removeLayer(gridLy);
								// gridLy.clear();
								// delete map._layers[k];
							}
						}
					}
				} else {
					var gridLy = map._layers[config.layerName];
					if(gridLy != undefined) {
						map.removeLayer(gridLy);
						// gridLy.clear();
						// delete map._layers[config.layerName];
					}
				}
				
				if(_measure != undefined){
					_measure.measureClearAll();
				}

				for(var m=0; m<mutilPointObjArr.length; m++){
					/*
					 for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
					 var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
					 if(div){
					 //div.style.visibility = "hidden";
					 }
					 }*/
					if(mutilPointObjArr[m].layerName == config.layerName || (flag && mutilPointObjArr[m].layerName.indexOf(_layerName) != -1)) {
						for (var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
							var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
							if(div){
								if (!$(div).find(':first-child').hasClass('mapbox')) {
									if(typeof(div.style) != 'undefined'){
										div.style.display = "none";
									}
									if(typeof(div.innerHTML) != 'undefined'){
										div.innerHTML = "";
									}
								}
								div.style.visibility = "hidden";

							}
						}
						mutilPointObjArr.remove(m);
					}
				}

				renderPointListData = [];
				//mutilPointObjArr=[];
				/*
				 if(zkMapElement!=null){
				 zkMapElement.clear();
				 }*/

				if(ptiplayer!=undefined){
					ptiplayer.innerHTML = "";
				}
			}
		}
	};
	
	
	
	$.fn.ffcsMap.clearWithOutNumLayer = function(config){
		if(map != undefined){
			if(map.graphics != undefined ){
				map.graphics.clear();
			}
			if(config != undefined){
				var lys = map._layers;
				var gridLy = map._layers[config.layerName];
				if(gridLy != undefined) {
					gridLy.clear();
					delete map._layers[config.layerName];
				}
			}

			if(_measure != undefined){
				_measure.measureClearAll();
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


	$.fn.ffcsMap.clearSimilarLayers = function(layerName) {
		if(map != undefined && map.graphicsLayerIds != undefined){
			var layerIds = map.graphicsLayerIds;

			for (var i = 0; i < layerIds.length; i++) {
				if(layerName != '' && layerIds[i].indexOf(layerName) == 0){
					var layer = map.getLayer(layerIds[i]);
					if(typeof layer != 'undefeined' && layer != null){
						//map.removeLayer(layer);
						$.fn.ffcsMap.clearWithOutNumLayer({layerName : layerIds[i]});
					}
				}
			}
		}
	}

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
		var str ="<div class='"+className+"' style='left: " + left + "px; top:" + top + "px;  position:absolute; z-index: 990; font-size:12px; padding:3px 5px; overflow:hidden; background:#ffffd5; border:1px solid #808080;'>" + tipContext +"</div>";
		ptiplayer.innerHTML = str;
	};
	
	
	$.fn.ffcsMap.geteditToolbar = function(){
		return editToolbar;
	};

	$.fn.ffcsMap.winDivGoToMarker = function() {
		var builddianpuDv = $("#NorMapOpenDiv");
		if (builddianpuDv.length > 0) {
			var lnglat = builddianpuDv.attr("lnglat").split(",");
			var offset = builddianpuDv.attr("offset").split(",");
			if (typeof lnglat != "undefined" && lnglat.length > 1) {
				var point = new esri.geometry.Point(lnglat[0], lnglat[1], new esri.SpatialReference({ wkid : map.spatialReference["wkid"] }));
				var screenPt = map.toScreen(point);
				var w = builddianpuDv.width(), h = builddianpuDv.height();
				builddianpuDv.css({
					left : screenPt.x - w / 2 + parseInt(offset[0]),
					top : screenPt.y - h + parseInt(offset[1])
				});
			}
		}
	};
	
	$.fn.ffcsMap.fixedPan = function(left,top,success,lng,lat) {
		var dragDivObj = $("#NorMapOpenDiv");
		var mapDiv = $("#" + map.id);
		var mW = mapDiv.width(), mH = mapDiv.height();
		var divW = dragDivObj.width(), divH = dragDivObj.height();
		var centerX = (mW / 2) - (divW / 2), centerY = (mH / 2) - (divH / 2);
		if (typeof lng == "undefined" || typeof lat == "undefined") {
			dragDivObj.css({ left : centerX, top : centerY });
		} else {
			var point = new esri.geometry.Point(lng, lat, new esri.SpatialReference({ wkid : map.spatialReference["wkid"] }));
			var screenPt = map.toScreen(point);
			map.__panEnd(-screenPt.x + (mW / 2), -screenPt.y + (mH / 2) + (divH / 2));
			// 移动DIV
			$.fn.ffcsMap.winDivGoToMarker();
		}
	};
	/**
	 * 
	 * 单击获取经纬度
	 * @date 2014-12-09
	 */
	var pointEditAble=true;
	$.fn.ffcsMap.onClickGetPointEnd = function() {
		pointEditAble = false;
	}
	
	$.fn.ffcsMap.onClickGetPoint = function(x,y,imgUrl,callBack,isEdit){
		if (imgUrl == '' || imgUrl == undefined) {
			imgUrl = 'images/GreenShinyPin.png';
		}
		var symbol = new gPictureMarkerSymbol(imgUrl, 34, 48);
		var rwkid = map.spatialReference["wkid"];
		
		var p = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid: rwkid }));
		var g = new esri.Graphic(p, symbol);
		
		var pointLayer = new esri.layers.GraphicsLayer({id:"select_cpoint_ly"});
		pointLayer.add(g);
		map.addLayer(pointLayer);
		pointEditAble = isEdit;
		map.on("click", function(evt){
			if(pointEditAble != undefined && !pointEditAble){
				return false;
			}
			var removeLayer = map.getLayer("select_cpoint_ly");
			if(removeLayer != null){
				map.removeLayer(removeLayer);
			}
			
			var mp = evt.mapPoint;
			var point = new esri.geometry.Point(mp.x, mp.y,new esri.SpatialReference({ wkid: rwkid }));
			var graphic = new esri.Graphic(point, symbol);
			
			var pointLayer = new esri.layers.GraphicsLayer({id:"select_cpoint_ly"});
			pointLayer.add(graphic);
			map.addLayer(pointLayer);
			
        	return callBack([mp.x, mp.y]);
		});
	};
	
	/**
	 * @desc 根据中心点、半径添加圆，用于显示某范围。
	 * 
	 * @params  {x:119.302,y:26.066,radius:100,imgUrl:"images/icon_4.png"}
	 *           x,y圆点中心  radius半径
	 * @return  callback(params)
	 * @date 2015-01-14
	 */
	dojo.require("esri.geometry.Circle");
	var scopeCircleCount = 0;
	$.fn.ffcsMap.addScopeCircle= function(params,callback){
		$.fn.ffcsMap.clearWithOutNumLayer({layerName : "scopeCircleLayer"+scopeCircleCount});
		
		scopeCircleCount = scopeCircleCount+1;
		 if (params.imgUrl == '' || params.imgUrl == undefined) {
			 params.imgUrl = 'images/GreenShinyPin.png';
		 }
		 var symbol= new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, 
	     			new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,	new dojo.Color([0, 0, 255]), 2), 
	     			new dojo.Color([0, 0, 255, 0.2]));
		 var rwkid = map.spatialReference["wkid"]; 
		 var ctpoint = new esri.geometry.Point(params.x, params.y ,new esri.SpatialReference({ wkid: rwkid }));
		 var circle = new esri.geometry.Circle({
             center: ctpoint,
             geodesic: true,
             radius: params.radius
           });
		 var scGraphic = new esri.Graphic(circle, symbol);
		 
		 var pcSymbol = new gPictureMarkerSymbol(params.imgUrl,32, 32);
		 var pcGraphic = new esri.Graphic(ctpoint);
		 
		 var scLayer = new esri.layers.GraphicsLayer({id:"scopeCircleLayer"+scopeCircleCount});
		 scLayer.add(scGraphic);
		 scLayer.add(pcGraphic);
		 map.addLayer(scLayer, 2);
		 var result = {
		 	scopeCircleCount:scopeCircleCount,
			layer:scLayer
		 }
		 callback(result);
	 };

	 /**＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	 * @desc 测距
	 *＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝ 
	 */
	 $.fn.ffcsMap.caluDistance = function(){
		 _measure.caluDistance();
	 };
	
	 //动态测面积
	 $.fn.ffcsMap.measureAreaD = function (callBack) {
		 _measure.measureAreaD();
		 if (typeof callBack != "undefined"){
			 _measure.areaDCallBack=callBack;
		 }
		 return _measure;
	 };
	 var createPointCounttList=[];
	$.fn.ffcsMap.createPointCounttDiv = function(zkMap, element, label, mpIndex, elIndex,layerName, offset) {
		if (typeof offset == "undefined") {
			offset = {};
			offset.x = 18;
			offset.y = -35;
		}
		
		var layer = map.getLayer(layerName);
		
		var point = element.key;
		var screen = map.toScreen(point);
		//label = label + 1;
		var div = zkMap.get(point);
		if(layer && layer.graphics){
			for(var j=0;j<layer.graphics.length;j++){//先检查当前点
				if(layer.graphics[j].geometry.wid==element.key.wid){
					if(!layer.graphics[j].visible){
						$(".pointCountClass"+element.key.wid).css("display","none");
						return;
					}
				}
			}
		}
		
		for (i = 0; i < zkMap.elements.length; i++) {
			//如果caculateLLRB返回true且两个点位WID不同，即认为是同一点展示
			if(caculateLLRB(zkMap.elements[i].key,element.key)){
				if(zkMap.elements[i].key.wid==element.key.wid){
					div = zkMap.elements[i].value;
				}
				if(zkMap.elements[i].key.wid!=element.key.wid && layer){//是否隐藏点位,如果两点不是同一点
					for(var j=0;j<layer.graphics.length;j++){
						if(layer.graphics[j].geometry.wid==zkMap.elements[i].key.wid){
							if(layer.graphics[j].visible){//如果还在展示，就隐藏
								layer.graphics[j].visible=false;
								var createPointCount={
										x:layer.graphics[j].geometry.x,
										y:layer.graphics[j].geometry.y,
										wid:zkMap.elements[i].key.wid,
										group:element.key.wid
								};
								createPointCounttList.push(createPointCount);
							}
						}
					}
				}
				/*if (div != null) {
					break;
				}*/
			}
		}
		label=1;
		for(var q=0;q<createPointCounttList.length;q++){
			if(createPointCounttList[q].group==element.key.wid){
				label++;
			}
		}
		
		if (!div) {
			div = $.fn.ffcsMap.createDivEx(null, point, offset, "");
			if (label <= 1) {
				 div.style.overflow = "hidden";
				// $(".work_sz").css("display", "none");
			} else {
				div.style.overflow = "visible";
				div.innerHTML = "<span class='work_sz'>" + label + "</span>";
			}
			var mapdiv = document.getElementById(map.id);
			mapdiv.appendChild(div);
			mutilPointObjArr[mpIndex].zkMapElement.element(elIndex).value = div;
		} else {
			div = $.fn.ffcsMap.createDivEx(div, point, offset, "");
			div.setAttribute("class", "pointCountClass"+element.key.wid);
			$(".pointCountClass"+element.key.wid).css("display","none");
			if (div.children[0] != undefined) {
				div.children[0].innerText = label;
				if (label <= 1) {
					//div.style.overflow = "hidden";
				} else {
					$(".pointCountClass"+element.key.wid).css("display","block");
					div.style.overflow = "visible";
					div.style.display = "block";
				}
			}else if(div.children[0] == undefined && label>1){
				div.innerHTML = "<span class='work_sz'>" + label + "</span>";
				$(".pointCountClass"+element.key.wid).css("display","block");
				div.style.overflow = "visible";
				div.style.display = "block";
			}
		}
	};

	$.fn.ffcsMap.createDivA = function(zkMap, element, mpIndex, elIndex, itemData, callbackFn) {
		var point = element.key;
		var screen = map.toScreen(point);

		var div = zkMap.get(point);

		for (var i = 0; i < zkMap.elements.length; i++) {
			if ((zkMap.elements[i].key.x == element.key.x)
					&& (zkMap.elements[i].key.y == element.key.y)) {
				div = zkMap.elements[i].value;
				if (div != null) {
					break;
				}
			}

		}

		if (!div) {
			div = $.fn.ffcsMap.createDivEx(null, point, itemData.offset, itemData.content, itemData.data, callbackFn);
			var mapdiv = document.getElementById(map.id);
			mapdiv.appendChild(div);
			mutilPointObjArr[mpIndex].zkMapElement.element(elIndex).value = div;
		} else {
			div = $.fn.ffcsMap.createDivEx(div, point, itemData.offset, itemData.content, itemData.data);
		}
	};

	$.fn.ffcsMap.createDivEx = function(div, point, offset, content, busiData, callbackFn) {
		var def_offset_height = 0;
		if (typeof offset == "undefined") {
			offset = {};
			offset.x = 0;
			offset.y = 0;
		} else {
			def_offset_height = 22;
		}
		if (point) {
			var screen = map.toScreen(point);
			if (div) {
				if ($(div).children().length > 0) {
					offset.y = 0 - $(div).children().eq(0).outerHeight(true) - def_offset_height;
				}
	         	div.style.left = (screen.x + offset.x) + "px";
	         	div.style.top = (screen.y + offset.y) + "px";
			} else {
				/*div = document.createElement("div");
				div.innerHTML = content;
				div.style.position = "absolute";
				div.style.right = "auto";
				div.style.zIndex = 850;
				div.style.bottom = "auto";
				div.style.overflow = "visible";*/
				var $div = $('<div></div>').css({
					position:"absolute",
					right:"auto",
					zIndex:850,
					bottom:"auto",
					overflow:"visible"
				});
				$(document.body).append($div);
				$div.html(content);
				if ($div.children().length > 0) {
					offset.y = 0 - $div.children().eq(0).outerHeight(true) - def_offset_height;
				}
				div = $div[0];
				div.style.left = (screen.x + offset.x) + "px"; //-330
				div.style.top = (screen.y + offset.y) + "px";  //+30
				$(div).click(function() {
					if (typeof callbackFn == "function") {
						callbackFn.call(this, point, busiData);
					}
				});
			}
		}
		return div;
	};
	 
 	/**
	 * 同一点多条数据列表框显示
	 * 
	 * @date 2015-03-11
	 * 
	 */
	var mtpp_temp_pointList,mtpp_temp_createDlg;
	var mtpp_temp_opt, mtpp_temp_layerName, mtpp_temp_title, mtpp_temp_showDetail;
	var mtpp_temp_evt;
	
	var mtpp_showSkyline;
	$.fn.ffcsMap.mutilPointPanel = function(evt,pointList,opt, layerName, title, createDlg, showDetail ,showSkyline) {
		
		mtpp_temp_pointList = pointList;
		mtpp_temp_createDlg = createDlg;
		mtpp_temp_opt = opt;
		mtpp_temp_layerName = layerName;
		mtpp_temp_title = title; 
		mtpp_temp_showDetail = showDetail;
		mtpp_temp_evt = evt;
		
		mtpp_showSkyline = showSkyline;
		
	    var stylediv1 = document.body;				
		
		if (mutilPiontPanelDiv!= null) {
			mutilPiontPanelDiv.innerHTML = "";
			mutilPiontPanelDiv.style.left = document.body.offsetLeft + 'px';		
			mutilPiontPanelDiv.style.top = document.body.offsetTop + 'px';
			mutilPiontPanelDiv.style.width = stylediv1.style.width;
			mutilPiontPanelDiv.style.height = "100%";
			mutilPiontPanelDiv.style.position = "absolute";
		}
		else {
			mutilPiontPanelDiv = document.createElement("div");
			mutilPiontPanelDiv.id = "mutilPiontPanelDiv";
			mutilPiontPanelDiv.style.left = document.body.offsetLeft + 'px';
			mutilPiontPanelDiv.style.top = document.body.offsetTop + 'px';
			mutilPiontPanelDiv.style.width = stylediv1.style.width;
			mutilPiontPanelDiv.style.height = "100%";
			mutilPiontPanelDiv.style.position = "absolute";
			stylediv1.appendChild(mutilPiontPanelDiv);
		}
		
		var top = evt.screenPoint.y  ;
		var left = evt.screenPoint.x ;
		var height = 218;
		
		var hrefHtml = "";
        var str="";
		for(var i=0; i< pointList.length ; i++){
			var rx = pointList[i].data["x"];
			var ry = pointList[i].data["y"];
			var name = pointList[i].data["gridName"];
			var wid = pointList[i].data._oldData["wid"];
			var subBusiType = pointList[i].data._oldData["subBusiType"];
			var channelId = pointList[i].data._oldData["channelId"];
			var tmp_html = "";
				var arr=[];
			if(subBusiType){
                arr=subBusiType.split("-");
			}
			if(arr.length>0&&arr[0]=="23"){
				tmp_html ="<dl><dt class='icon04'><a href='javascript:gloybalPlay(\""+channelId +"\");' title='"+name+"'>";
			} else {
				tmp_html ="<dl><dt class='icon04'><a href='javascript:$.fn.ffcsMap.showInfoWinByWid(\""+wid +"\");' title='"+name+"'>";
			}
		    var l=8;
		    if(pointList[i].data._oldData.bizType=='device'){
		    	 l=14;
			     if(i==0){
						str="width:260px";
			      }
			 }
			if(name.length>l) {
				tmp_html= tmp_html+ name.substr(0,l)+"...";
			}else {
				tmp_html =tmp_html+ name;
			}
			tmp_html=tmp_html+"</a></dt></dl>";
			
			hrefHtml =  hrefHtml + tmp_html;
		            
		}
				
		var mystr2 =  "<div class='menuNav' style='left: " + left + "px; top:" + top + "px;height:"+ height  +"px;"+str+"'>"+
		              "<h2><span class='fr close' onclick='javascript:$.fn.ffcsMap.closeMutilPiontPanelDiv()'></span></h2><div class='navlist' id='SNmenuNav'>" + hrefHtml +
		              "</div></div>" ;
		
		mutilPiontPanelDiv.innerHTML = mystr2;
	};
	$.fn.ffcsMap.closeMutilPiontPanelDiv=function() {
		mutilPiontPanelDiv.innerHTML = "";
	}
	
	/**
	 * 点图层重叠数据列表明细显示
	 * 
	 * @date 2015-03-12
	 */
	$.fn.ffcsMap.showInfoWinByWid= function(wid){
		var resultInfo ;
		var x,y;
		var _data = null;
		for(var i=0; i< mtpp_temp_pointList.length ; i++){
			var temp_wid = mtpp_temp_pointList[i].data._oldData["wid"];
			x = mtpp_temp_pointList[i].data["x"];
			y = mtpp_temp_pointList[i].data["y"];
			if(wid == temp_wid){
				resultInfo = mtpp_temp_createDlg(mtpp_temp_pointList[i].data._oldData);
				_data = mtpp_temp_pointList[i].data._oldData;
				break;
			}
		}
        
        var rwkid = map.spatialReference["wkid"];
    	var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid : rwkid }));
		
    	var screenPt = map.toScreen(point);
		var top  = screenPt.y - mtpp_temp_opt.h - 51;
		var left = screenPt.x - mtpp_temp_opt.w / 2;
		
		//$.fn.ffcsMap.centerAt({x:x,y:y,zoom:-1});
		if(resultInfo!=null && resultInfo!=""){
			if(mtpp_temp_layerName == "globalEyesLayer" || mtpp_temp_layerName == "statGlobalEyesLayer"){
				var elementsCollectionStr = _data['elementsCollectionStr'];
				var wid = _data["wid"];
				// var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
				// closeBeforeMaxJqueryWindow();//关闭之前所有的窗口
				// showVideoWindow(mtpp_temp_title, menuSummaryUrl+wid, mtpp_temp_opt.w, mtpp_temp_opt.h, false);

                if(_data["subBusiType"]=="14"){
                    showDHGlobalPlay(wid);
                }else{
                    var menuSummaryUrl =analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
                    closeBeforeMaxJqueryWindow();//关闭之前所有的窗口
                    showVideoWindow(title, menuSummaryUrl+wid, opt.w, opt.h, false);
                }
			}else{
				mtpp_temp_opt.lng = x;
				mtpp_temp_opt.lat = y;
				$.fn.ffcsMap.initWindow(left, top, mtpp_temp_opt, mtpp_temp_title, resultInfo, true, mtpp_temp_showDetail, wid, _data ,mtpp_showSkyline);

			}

		}
	};
	//---------要素图层相关--------------------------------
	/**
	 * 实例要素对象 
	 * 
	 */
	$.fn.ffcsMap.InstanceFeature = function(){
		ffcs_curFeature = new FfcsFeatureLayer();
	}
	
	/**
	 * 获取当前要素对象
	 */
	$.fn.ffcsMap.getFfcsFeature =  function(){
		return ffcs_curFeature;
	}
	
	/**
	 * 设置是否是要素图层
	 * 
	 * val  is true or false
	 */
	$.fn.ffcsMap.setIsFeatureLayer =  function(val){
		 isFeatureLayer = val;
	};
	
	/**
	 * 获取提示div变量
	 * 
	 */
	$.fn.ffcsMap.getPtiplayer = function(){
		return ptiplayer;
	};
	
	/**
	 * 设置代理URL
	 * 
	 */
	$.fn.ffcsMap.setProxy = function(url){
		esri.config.defaults.io.proxyUrl = url;
	};
	
	/**
	 * 访止跨域时控制台出错 (The map before loading)
	 * 
	 */
	$.fn.ffcsMap.setCors = function(domainName){
		if(domainName == undefined){
			esri.config.defaults.io.corsDetection=false;
		}else{
			esri.config.defaults.io._processedCorsServers[domainName]=1;
		}
	}
	
	/**
	 * 热力图展现
	 * 
	 * heatmap-arcgis.js
	 * heatmap.js
	 * 页面增加   <div id="heatLayer"></div>
	 * 
	 * @param dataUrl  数据地址
	 * 
	 */
	$.fn.ffcsMap.createHeatMap = function(domId, config) {
		var opt = {
	        config: {
	            "useLocalMaximum": true,
	            "radius": 20,
	            "gradient": {
	                0.45: "rgb(000,000,255)",
	                0.65: "rgb(000,255,255)",
	                0.75: "rgb(000,255,000)",
	                0.95: "rgb(255,255,000)",
	                1.00: "rgb(255,000,000)"
	            }
	        },
	        "map" : map,
	        "domNodeId": domId,
	        "opacity": 0.85
	    };
	    if (typeof config != "undefined") opt.config = config;
		var heatLayer = new HeatmapLayer(opt);
	    map.addLayer(heatLayer);
	    return heatLayer;
	};
	$.fn.ffcsMap.heatMap = function(heatLayer, dataUrl, callBack) {
		var heatData = [];
		$.getJSON(dataUrl, function(datas) {
			$.each(datas, function(i,data){
					var point = new esri.geometry.Point(data.x, data.y ,new esri.SpatialReference({ wkid : map.spatialReference["wkid"] }));
				    var heatObj = {};
				    heatObj.attributes ={
						count : data.count
					};
				    heatObj.geometry = point;
				    heatData.push(heatObj);
					//console.log("i="+i+",x="+data.x+",y="+data.y);
			});
			heatLayer.setData(heatData);
			if (typeof callBack != "undefined") callBack.call(this);
		});
	};
	
	$.fn.ffcsMap.toolbarDeactivate = function(){
		// 获取graphicsLayer图层Id
		if(typeof editToolbar != 'undefined' && editToolbar != null) {
            editToolbar.deactivate();
        }
	}
	
	$.fn.ffcsMap.hideCenterPointImg = function(x, y){
		var symbol = new gPictureMarkerSymbol("", 34, 48);
		var rwkid = map.spatialReference["wkid"];
		
		var removeLayer = map.getLayer("select_cpoint_ly");
		if(removeLayer != null){
			map.removeLayer(removeLayer);
		}
		var rwkid = map.spatialReference["wkid"];
		var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid: rwkid }));
		var graphic = new esri.Graphic(point, symbol);
		
		var pointLayer = new esri.layers.GraphicsLayer({id:"select_cpoint_ly"});
		pointLayer.add(graphic);
		map.addLayer(pointLayer);
	}
	
	/**
	* 改变轮廓可编辑状态
	*/
	$.fn.ffcsMap.changeIsDrawEdit = function(){
		isDrawEdit = false; // should be click edit button again when you want to edit graphic
	}
	
	/**
	* 编辑工具失效
	*/
	$.fn.ffcsMap.toolbarDeactivate = function(){
		// 获取graphicsLayer图层Id
		if(typeof editToolbar != 'undefined' && editToolbar != null) {
            editToolbar.deactivate();
        }
	}
	
	/**
	* 隐藏中心点定位图标
	*/
	$.fn.ffcsMap.hideCenterPointImg = function(x, y){
		var symbol = new gPictureMarkerSymbol("", 1, 1);
		var rwkid = map.spatialReference["wkid"];
		
		var removeLayer = map.getLayer("select_cpoint_ly");
		if(removeLayer != null){
			map.removeLayer(removeLayer);
		}
		var rwkid = map.spatialReference["wkid"];
		var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid: rwkid }));
		var graphic = new esri.Graphic(point, symbol);
		
		var pointLayer = new esri.layers.GraphicsLayer({id:"select_cpoint_ly"});
		pointLayer.add(graphic);
		map.addLayer(pointLayer);
	}
	
	/**
	* 改变轮廓可编辑状态
	*/
	$.fn.ffcsMap.changeIsDrawEdit = function(){
		isDrawEdit = false; // should be click edit button again when you want to edit graphic
	}
	
	/**
	 * 显示中心点定位图标
	 */
	$.fn.ffcsMap.showCenterPoint = function(x, y, imgUrl){
		if (imgUrl == '' || imgUrl == undefined) {
			imgUrl = 'images/GreenShinyPin.png';
		}
		var symbol = new gPictureMarkerSymbol(imgUrl, 34, 48);
		var rwkid = map.spatialReference["wkid"];
		
		var removeLayer = map.getLayer("select_cpoint_ly");
		if(removeLayer != null){
			map.removeLayer(removeLayer);
		}
		var rwkid = map.spatialReference["wkid"];
		var point = new esri.geometry.Point(x, y, new esri.SpatialReference({ wkid: rwkid }));
		var graphic = new esri.Graphic(point, symbol);
		
		var pointLayer = new esri.layers.GraphicsLayer({id:"select_cpoint_ly"});
		pointLayer.add(graphic);
		map.addLayer(pointLayer);
	};
	/**
	 * 返回颜色对象
	 */
	$.fn.ffcsMap.getColor = function(){
		return dojo.Color;
	};
	$.fn.ffcsMap.getSymbol = function(){
		return esri.symbol;
	};
	$.fn.ffcsMap.getEsri = function(){
		return esri;
	};
	
	/**
	 * 渲染自定义div
	 */
	$.fn.ffcsMap.renderDivForData = function(layerName, opt) {
		var textLayer; // 文本图层
		var fetchContentFn = opt.getText;  
		renderPointListData = [];
		zkMapElement = new ZLKeyValueMapping();
		// 不存在该图层则新建，存在则直接获取
		if (map.getLayer(layerName) == undefined) {
			textLayer = new esri.layers.GraphicsLayer({id:layerName});
			map.addLayer(textLayer);
		} else {
			textLayer = map.getLayer(layerName);
		}
		var rwkid = map.spatialReference["wkid"];
		
		$.each(opt.datas, function(i, data) {
			// 给对象设值 用来区分点、楼宇还是网格
			data.layerName = layerName;
			var p = new esri.geometry.Point(data.x, data.y, new esri.SpatialReference({ wkid : rwkid }));
			var content = "";
			if (typeof fetchContentFn == "function") {
				content = fetchContentFn.call(this, data);
			}
			var rpData = {};
			rpData.data = data;
			rpData.layerName = layerName;
			rpData.content = content;
			renderPointListData.push(rpData);
			zkMapElement.put(p, null);
		});

		var mpObj = {};
		mpObj.layerName = layerName;
		mpObj.renderPointListData = renderPointListData;
		mpObj.zkMapElement = zkMapElement;
		mpObj.layerType = "customLayer";
		mutilPointObjArr.push(mpObj);
		renderPointListData = [];

		for ( var m = 0; m < mutilPointObjArr.length; m++) {
			for ( var k = 0; k < mutilPointObjArr[m].zkMapElement.elements.length; k++) {
				var div = mutilPointObjArr[m].zkMapElement.elements[k].value;
				if (div) {
					div.style.visibility = "visible";
				}
			}
		}

		// ======20150529修改 begin======
		for ( var m = 0; m < mutilPointObjArr.length; m++) {
			if (mutilPointObjArr[m].layerName == layerName) {
				for ( var i = 0; i < mutilPointObjArr[m].zkMapElement.elements.length; i++) {
					var label = mutilPointObjArr[m].zkMapElement
							.keyEquesNum(mutilPointObjArr[m].zkMapElement.elements[i].key);
					var itemData = mutilPointObjArr[m].renderPointListData[i];
					itemData.offset = {x:-14,y:-55};
					$.fn.ffcsMap.createDivA(
									mutilPointObjArr[m].zkMapElement,
									mutilPointObjArr[m].zkMapElement.elements[i],
									m, i, itemData,
					function() {});
				}
			}
		}
				// ======20150529修改 end======
			
	};
	
	/*
	 * 划线
	 * @param layerName :图层名
	 * @points 定位点数组 格式(二维数组,多条线路): [[{x:126.123456,y:22.345678}]]
	 * @lineOpt 线样式 , 一维数组,映射某条线路的样式
	 * */
	$.fn.ffcsMap.drawLine = function(layerName,points,lineOpt){
		for(var i=0;i<points.length;i++){
			$.fn.ffcsMap.clear({layerName : layerName+i});
		}
		var polyline = new esri.geometry.Polyline();
		var IMGpoint = new esri.geometry.Point(0, 0);
		var bottom  = 10;
		var defaultLineOpt={
			lineStyle:esri.symbol.SimpleLineSymbol.STYLE_DASH,lineColor:'#FF0000',lineWidth:2,
			imgUrl:'',imgWidth:'',imgHeight:''
		};
		var linePoints = [];
		for(var i=0,len=points.length;i<len;i++){
			var thisLayer = new esri.layers.GraphicsLayer({id:layerName+i});
			map.addLayer(thisLayer);
			linePoints = [];
			var realLineOpt = $.extend({},defaultLineOpt,lineOpt[i]);
			//console.log(realLineOpt.lineColor);
			var defRGBLineColor = new dojo.Color(realLineOpt.lineColor);
			for(var j=0,llen=points[i].length;j<llen;j++){
				linePoints[j]= new esri.geometry.Point(points[i][j].x,points[i][j].y);
			}
			polyline.addPath(linePoints);
			//defRGBLineColor = new dojo.Color(defRGBLineColor).toRgb();
			//defRGBLineColor = new dojo.Color([defRGBLineColor[0], defRGBLineColor[1], defRGBLineColor[2], 1])
			var symbol = new esri.symbol.SimpleLineSymbol(realLineOpt.lineStyle,defRGBLineColor, realLineOpt.lineWidth);
			var graphic = new esri.Graphic(polyline, symbol);
			thisLayer.add(graphic);
			$.fn.ffcsMap.centerAt({x:points[0][0].x,y:points[0][0].y,zoom:-1});
			$.fn.ffcsMap.getMap().setZoom(14);
		}
	};
	
	$.fn.ffcsMap.addSinglePointInfo = function(layerName,ipoint,options2){
		$.fn.ffcsMap.clear({layerName : layerName});
		var options = $.extend({}, $.fn.ffcsMap.addPointDefaultOpts, options2); 
		
		var layer = new esri.layers.GraphicsLayer({id:layerName});
			
			var pointOpt = $.extend({}, options, ipoint); 
			var text = pointOpt.text;
			var imageUrl = pointOpt.imageUrl;
			var point = new esri.geometry.Point(pointOpt.x, pointOpt.y);
			
			var markerSymbol = new gPictureMarkerSymbol(pointOpt.imageUrl,pointOpt.imageWidth,pointOpt.imageHeight);
			var markerGraphic = new esri.Graphic(point, markerSymbol);
			
			layer.add(markerGraphic);
			
			if(text!==undefined && text!==""){
				var textMarkerSymbol = new gPictureMarkerSymbol(
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
		map.addLayer(layer);
		
	};
})(jQuery);


Array.prototype.remove=function(dx) 
{ 
    if(isNaN(dx)||dx>this.length){return false;} 
    for(var i=0,n=0;i<this.length;i++) 
    { 
        if(this[i]!=this[dx]) 
        { 
            this[n++]=this[i] 
        } 
    } 
    this.length-=1 
};


//画轮廓方法
function getGridLayer(datas,layerName) {
	var map = $.fn.ffcsMap.getMap();
	clearSpecialLayer(layerName);//清除图层
	 var wanggeLayer = new esri.layers.GraphicsLayer({id:layerName});
	 map.addLayer(wanggeLayer, 99);
	 var rwkid =  map.spatialReference["wkid"];
	  var fillStyle = "solid"; // 填充样式
	  var lineStyle = "solid"; // 边框样式
	 var defTextFont = "8pt"; // 文字大小
	 
	// 绑定事件
     
     wanggeLayer.on("click", function(evt) {
         var id =evt.graphic.geometry.spatialReference.tbid;
         if (typeof window.frames['get_grid_name_frme'].selected == "function") {
     		window.frames['get_grid_name_frme'].selected(id);
     	 }
     });
     wanggeLayer.on("mouse-move", function(evt) {
         mouseMove(evt);
     });

     wanggeLayer.on("mouse-out", function(evt) {
         mouseOut(evt);
     });
     
	  
		//$.getJSON("http://static.beta.aishequ.org/web-assets/_big-screen/JiangYinPlatform/js/mapDataJson.json", function(datas){
			 $.each(datas, function(i, data) {
				var _data = $.fn.ffcsMap.DataCvtUtil(data,true);//转换
				 var rings = [];
				for (var i = 0; i < _data.coordinates[0].length; i++) {
					rings[i] = new esri.geometry.Point(_data.coordinates[0][i]);
				}
				 var polygon = new esri.geometry.Polygon(new esri.SpatialReference({ wkid : rwkid,tbid:_data.type,gridName:_data.gridName,data:_data}));
	              polygon.addRing(rings);
				  var RGBLineColor = new dojo.Color(_data.lineColor).toRgb(), RGBAreaColor = new dojo.Color(_data.areaColor).toRgb(),lineWidth = _data.lineWidth,
				  RGBNameColor = new dojo.Color(_data.nameColor).toRgb();
				 var xloc = _data.x;
				var yloc = _data.y;
				var tbId=_data.type;//图斑id
				// 构造中心点
				var point = new esri.geometry.Point(xloc, yloc ,new esri.SpatialReference({ wkid : rwkid ,tbid:_data.type,gridName:_data.gridName,data:_data}));

				// 区域名称 data.name
				var textString = _data.gridName;

				// 网格样式    默认显示轮廓
				var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
					new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5]), lineWidth),
					new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
				// 文字样式    默认半透明
				var textSymbol = new esri.symbol.TextSymbol(textString).
				setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
				setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
				setFont(new esri.symbol.Font(defTextFont).
				setWeight(esri.symbol.Font.WEIGHT_BOLD));
				 symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], _data.colorNum]));
				  var graphic = new esri.Graphic(polygon, symbol);
	             wanggeLayer.add(graphic);
				  graphicsText = new esri.Graphic(point, textSymbol);
	            wanggeLayer.add(graphicsText);
			 });
		//});
			 //当前鼠标移动所在图形
		     function mouseMove(evt) {
		         var graphic = evt.graphic;
	             var symbol = graphic.symbol;
	             var _data = evt.graphic.geometry.spatialReference.data;
	             var RGBLineColor = new dojo.Color("#FFFD00").toRgb(), 
	             RGBAreaColor = new dojo.Color("#FFFD00").toRgb(),
				 RGBNameColor = new dojo.Color("#FFFD00").toRgb();
	             symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1])); // 边界
                 graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0.5])); // 填充
	             graphic.setSymbol(symbol);
		     }

		     // 鼠标离开
		     function mouseOut(evt) {
		         var graphic = evt.graphic;
		         var symbol = graphic.symbol;
		         var RGBLineColor = new dojo.Color("#ff0000").toRgb(), 
		         RGBAreaColor = new dojo.Color("#ff0000").toRgb(),
				 RGBNameColor = new dojo.Color("#ff0000").toRgb();
		         symbol.outline.setColor(new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0.5])); // 边界
                 graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
	             graphic.setSymbol(symbol);
		     }
	}
	
	
