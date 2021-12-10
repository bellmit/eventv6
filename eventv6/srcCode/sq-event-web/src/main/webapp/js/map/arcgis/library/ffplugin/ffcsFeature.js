

function FfcsFeatureLayer(){
	
	this.FFmap = null;
	this.esriLayer = null;
	this.graphic = null;
}

/**
 * 根据要素服务URL查询要素接口 
 * 
 * @param layerUrl 要素图层URL
 * @param queryCondition 查询条件  同sql语法
 * @param isUseGeometry 是否需要空间数据
 * @param showResults 返回结果集
 * @param errorResults 错误结果集
 */
FfcsFeatureLayer.prototype.queryByUrl = function(layerUrl, queryCondition, isUseGeometry, opts, showResults, errorResults) {
	var queryTask = new esri.tasks.QueryTask(layerUrl);
	var query = new esri.tasks.Query();
	query.outFields = [ "*" ];
	query.where = queryCondition;
	query.returnGeometry = isUseGeometry;
	queryTask.execute(query, function(results) {
		showResults(results, opts);
	}, errorResults);
};

/**
 * 渲染楼宇网格数据接口 
 * 
 * @param queryCondition 查询条件  同sql语法
 * @param layerName 图层名称
 * @param dataUrl 数据请求地址
 * @param showType 1 hide  2 show
 * @param isCvt  是否转换数据
 * @param imgUrl 图片路径
 * @param fontSize 字体大小
 * @param isCPImg  true(中心点为图片) false(文字) 是否中心点为图片 2014-12-11
 * @param callSubwg(_data) 网格中心点图片单击事件方法回调, _data参数为回调的数据
 */
FfcsFeatureLayer.prototype.render = function(queryCondition,layerName,dataUrl,showType,isCvt,imgUrl,imgWidth,imgHeight,fontSize,isCPImg,callSubwg,loadType){
	var _map = $.fn.ffcsMap.getMap();
	$.fn.ffcsMap.setIsFeatureLayer(true);
	
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
	// 边框颜色
	var RGBLineColor = defRGBLineColor;
	// 填充颜色
	var RGBAreaColor = defRGBAreaColor;
	// 边框线条宽度
	var lineWidth = defLineWidth;
	 
	// 设置字体的大小
	var textFont = defTextFont;
	if (fontSize != null) {
		textFont = fontSize + "pt";
	}

	// 文字颜色
	var RGBNameColor = defRGBNameColor;
	
	 // 网格样式    默认显示轮廓
	//var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
	//	          new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1]), lineWidth),
	//	          new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
	if(loadType == "wfs") {
		loadWfsLayer(this);
	}else {
		if(showType==0) {
			loadFeaturePointLayer(this);
		}else if(showType==1){//自己渲染
			loadFeatureLayer(this);
		}else if(showType==2) {
			loadCollectionFeatureLayer(this);
		}
	}
	
	
	
	var rwkid = _map.spatialReference["wkid"];
	var feaLayer; //要素图层
	var factorLayer;
	var featureObj ;
		//加载要素
	function loadWfsLayer(feaObj){
		var opts = {
	        "url": dataUrl,
	        "version": "1.1.0",
	        "nsLayerName": "http://medford.opengeo.org|citylimits",
	        "wkid": rwkid,
	        "mode": "SNAPSHOT",
	        "maxFeatures": 100,
	        "showDetails": true    
	    };
	
	    esriConfig.defaults.io.proxyUrl = "/proxy/";
	    var factorLayer = new esri.layers.WFSLayer(opts);
	    factorLayer = new WFSLayer(opts);
	   	_map.addLayer(factorLayer);
	}
	function loadCollectionFeatureLayer(feaObj){
		
		var feaLayer = new esri.layers.FeatureLayer(dataUrl,{
			mode: esri.layers.FeatureLayer.MODE_SELECTION,
	        outFields: ["*"],
	        id : layerName
	    });
	    //http://sampleserver1.arcgisonline.com/ArcGIS/rest/services/Specialty/ESRI_StatesCitiesRivers_USA/MapServer/0
	    //http://10.2.2.84:6080/arcgis/rest/services/HC_WG/MapServer/0
	    var queryTask = new esri.tasks.QueryTask(dataUrl);
		var query = new esri.tasks.Query();
    	query.outFields = ["*"];
		query.where= queryCondition;
		query.returnGeometry = false;
		feaLayer.selectFeatures(query);
		queryTask.execute(query, showResults, errorResults);
		function showResults (results) {
          var sqsymbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
                     new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_DASHDOT,
                         new dojo.Color([198, 120,249, 1]), 1.5),
                     new dojo.Color([125, 125, 125, 0]));  
		    if (results.features != null) { 
		       for(var i=0;i<results.features.length;i++)
			   {
				   var g=results.features[i]
				   g.setSymbol(sqsymbol);
				   _map.graphics.add(g);
			   }
		    }
        }
        function errorResults(results) {
        	var rs = results;
        }
        
        feaLayer.on("load",function(){
				//中心点名称处理
				feaLayer.queryFeatures(query,function(featureSet){
					var i=1;
					/*
					//处理要素集合
					for(var i=0; i<featureSet.features.length ; i++){
						var x = featureSet.features[i].geometry["x"];
						var y = featureSet.features[i].geometry["y"];
						//var _dataStr = featureSet.features[i].attributes["DATA"];
						//var _dataObj = JSON.parse(_dataStr);
						featureObj = featureSet.features[i];
						setFeaturePointStyle(null,x,y);
					}
					*/
				},function(){
					console.log("feature render failed!");
				});
			});
        _map.addLayer(feaLayer);
        if(feaObj !=undefined){
			feaObj.setEsriLayer(feaLayer);
		}
        //_map.addLayer(feaLayer);
	}
	//加载要素
	function loadFeatureLayer(feaObj){
		
			// 不存在该图层则新建
			//if (_map.getLayer(layerName) == undefined) {
				feaLayer = new esri.layers.FeatureLayer(dataUrl,{
					mode: esri.layers.FeatureLayer.MODE_SELECTION,
			        outFields: ["*"],
			        id : layerName
			    });
				
				if(queryCondition != null){
					var query = new esri.tasks.Query();
					query.where= queryCondition;
					feaLayer.selectFeatures(query);
					feaLayer.on("load",function(){
						//中心点名称处理
						feaLayer.queryFeatures(query,function(featureSet){
							
							//处理要素集合
							for(var i=0; i<featureSet.features.length ; i++){
								var _dataStr = featureSet.features[i].attributes["DATA"];
								var _dataObj = JSON.parse(_dataStr);
								
								//设置样式
								if(_dataObj.lineColor != "" && _dataObj.lineColor != null){
									RGBLineColor = new dojo.Color(_dataObj.lineColor).toRgb();
								}
								
								if(_dataObj.areaColor != "" && _dataObj.areaColor != null){
									RGBAreaColor = new dojo.Color(_dataObj.areaColor).toRgb();
								}
								
								if (_dataObj.lineWidth != "" && _dataObj.lineWidth != null) {
									lineWidth = _dataObj.lineWidth;
								}
								
								if (_dataObj.nameColor != "" && _dataObj.nameColor != null) {
									RGBNameColor = new dojo.Color(_dataObj.nameColor).toRgb();
								}
								
								featureObj = featureSet.features[i];
								
								if(showType == 1){
									setFeatureStyle1(_dataObj,null);
								}else if(showType == 2){
									setFeatureStyle2(_dataObj,null);
								}
								
							}
							
							
						},function(){
							console.log("feature render failed!");
						});
					
					});
					
				}
				
				_map.addLayer(feaLayer);
				
				if(feaObj !=undefined){
					feaObj.setEsriLayer(feaLayer);
				}
				
				//this.esriLayer = feaLayer;

		//}else {
		//	feaLayer = _map.getLayer(layerName);
		//}
			
		// 绑定事件
		feaLayer.on("mouse-move", function(evt) {
			mouseMove(evt);
		});
		
		feaLayer.on("mouse-out", function(evt) {
			mouseOut(evt);
		});

	}
	//加载要素
	function loadFeaturePointLayer(feaObj){
		
		feaLayer = new esri.layers.FeatureLayer(dataUrl,{
			mode: esri.layers.FeatureLayer.MODE_SELECTION,
	        outFields: ["*"],
	        id : layerName
	    });
		if(queryCondition != null){
			var query = new esri.tasks.Query();
			query.where= queryCondition;
			feaLayer.selectFeatures(query);
			
			feaLayer.on("load",function(){
				//中心点名称处理
				feaLayer.queryFeatures(query,function(featureSet){
					var i=1;
					/*
					//处理要素集合
					for(var i=0; i<featureSet.features.length ; i++){
						var x = featureSet.features[i].geometry["x"];
						var y = featureSet.features[i].geometry["y"];
						//var _dataStr = featureSet.features[i].attributes["DATA"];
						//var _dataObj = JSON.parse(_dataStr);
						featureObj = featureSet.features[i];
						setFeaturePointStyle(null,x,y);
					}
					*/
				},function(){
					console.log("feature render failed!");
				});
			});
		}
		_map.addLayer(feaLayer);
		if(feaObj !=undefined){
			feaObj.setEsriLayer(feaLayer);
		}
		/*
		// 绑定事件
		feaLayer.on("mouse-move", function(evt) {
			mouseMove(evt);
		});
		
		feaLayer.on("mouse-out", function(evt) {
			mouseOut(evt);
		});*/

	}
	
	//-----------------  设置轮廓样式 1 --------------------
	function setFeatureStyle1(_dataObj,feature_Obj){
		var textString = _dataObj.gridName;
		// 文字样式    默认半透明
		var textSymbol = new esri.symbol.TextSymbol(textString).
							setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
					        setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
					        setFont(new esri.symbol.Font(textFont).
					        setWeight(esri.symbol.Font.WEIGHT_BOLD));
		
		// 文字也默认不显示   全透明  data.nameColor
        textSymbol.setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 0]));
        
        
        // 构造中心点
	    var point = new esri.geometry.Point(_dataObj.x, _dataObj.y ,new esri.SpatialReference({ wkid : rwkid }));
	    // 文字
        var graphicsText = new esri.Graphic(point, textSymbol);
        
        var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
		          new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 0]), lineWidth),
		          new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
		
		featureObj.setSymbol(symbol);
        
        feaLayer.add(graphicsText);
        
	}
	
	//------------------- 设置轮廓样式 2 ---------------------------------
	function setFeatureStyle2(_dataObj,feature_Obj){
        // 网格样式    默认显示轮廓
		var symbol = new esri.symbol.SimpleFillSymbol(fillStyle,
			          new esri.symbol.SimpleLineSymbol(lineStyle, new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2], 1]), lineWidth),
			          new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));
        
		featureObj.setSymbol(symbol);
		
		// 构造中心点
	    var point = new esri.geometry.Point(_dataObj.x, _dataObj.y ,new esri.SpatialReference({ wkid : rwkid }));
	    // 区域名称 data.name
        var textString = _dataObj.gridName;
		
        // 文字样式    默认半透明
		var textSymbol = new esri.symbol.TextSymbol(textString).
							setColor(new dojo.Color([RGBNameColor[0], RGBNameColor[1], RGBNameColor[2], 1])).
					        setAlign(esri.symbol.Font.ALIGN_START).setAngle(0).
					        setFont(new esri.symbol.Font(textFont).
					        setWeight(esri.symbol.Font.WEIGHT_BOLD));
		
		// 网格中的文字默认半透明显示
		var graphicsText;
		if(featureObj.visible){
			if(isCPImg){
				var picsymbol = new esri.symbols.PictureMarkerSymbol('images/icon_4.png', 18, 18);
				graphicsText = new esri.Graphic(point, picsymbol);
				
			    var wgPointLayer = new esri.layers.GraphicsLayer();
				wgPointLayer.add(graphicsText);
				wgPointLayer.on("click",function(evt){
					if(callSubwg){
						callSubwg(_dataObj);
					}
				});
				
				_map.addLayer(wgPointLayer);
			}else{
				graphicsText = new esri.Graphic(point, textSymbol);
				feaLayer.add(graphicsText);
			}
		}
		
		 //feaLayer.refresh();
		
	}
		//------------------- 设置轮廓样式 2 ---------------------------------
	function setFeaturePointStyle(_dataObj,x,y){
		// 构造中心点
	    var point = new esri.geometry.Point(x, y ,new esri.SpatialReference({ wkid : rwkid }));
		
		// 网格中的文字默认半透明显示
		var graphicsText;
		if(featureObj.visible){
			var picsymbol = new esri.symbols.PictureMarkerSymbol(js_ctx+'/js/map/arcgis/library/style/images/RedShinyPin.png', 18, 18);
			graphicsText = new esri.Graphic(point, picsymbol);
			var wgPointLayer = new esri.layers.GraphicsLayer();
			wgPointLayer.add(graphicsText);
			wgPointLayer.on("click",function(evt){
				if(callSubwg){
					callSubwg(_dataObj);
				}
			});
			$("#map"+currentN).ffcsMap.centerAt({
					x : x,          //中心点X坐标
					y : y,           //中心点y坐标
					wkid : currentArcgisConfigInfo.wkid, //wkid 2437
					zoom : 12
		    	});
			_map.addLayer(wgPointLayer);
		}
	}
	//----------------- 当前鼠标移动所在图形 --------------------------------
	function mouseMove(evt) {
		var graphic = evt.graphic;
		// 文字没有attributes属性
		if (graphic.attributes != null) {
			var _data = JSON.parse(graphic.attributes.DATA);
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
			if(showType == 0) {
				var ptitle = JSON.parse(graphic.attributes["DATA"]).gridName;
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel","biaoti");
			} else if(showType == 1) { // 楼宇  半透明填充 并显示边界
				
				var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
			            new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,
			              new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2],1]),lineWidth),//线轮廓
			            new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0.5]));//填充
				
				graphic.setSymbol(symbol);
				
				var ptitle = JSON.parse(graphic.attributes["DATA"]).gridName;
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
			} else if(showType == 2) { // 网格 不填充
				//graphic.symbol.setColor(new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0])); // 填充
				
				var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
			            new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,
			              new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2],1]),lineWidth),//线轮廓
			            new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));//填充
				
				graphic.setSymbol(symbol);
				
				var ptitle = JSON.parse(graphic.attributes["DATA"]).gridName;
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel",ptitle);
			}
			
		}
	}
	
	//---------------------- 鼠标离开 -------------------------------
	function mouseOut(evt) {
		var graphic = evt.graphic;
		
		if (graphic.attributes != null) {
			var _data = JSON.parse(graphic.attributes.DATA);
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
			var _ptiplayer = $.fn.ffcsMap.getPtiplayer();
			if (_ptiplayer != null) {
				_ptiplayer.innerHTML = "";
			}
			
			// 楼宇  无填充 不显示边界
			if(showType == 0) {
				var ptitle = JSON.parse(graphic.attributes["DATA"]).gridName;
				$.fn.ffcsMap.showCreateDiv(evt,"ptiplayer","hotlabel","biaoti");
			} else if (showType == 1) {
				var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
			            new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,
			              new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2],0])),//线轮廓
			            new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));//填充
				
				graphic.setSymbol(symbol);
			
			} else if(showType == 2) { // 网格 无填充
				var symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID,
			            new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,
			              new dojo.Color([RGBLineColor[0], RGBLineColor[1], RGBLineColor[2],1]),lineWidth),//线轮廓
			            new dojo.Color([RGBAreaColor[0], RGBAreaColor[1], RGBAreaColor[2], 0]));//填充
				
				graphic.setSymbol(symbol);
			}
		}
	}
}

 
FfcsFeatureLayer.prototype.setEsriLayer = function (featureLayer) {
    this.esriLayer = featureLayer;
}

FfcsFeatureLayer.prototype.setFFmap = function (map) {
    this.FFmap = map;
}


FfcsFeatureLayer.prototype.setGraphic = function (graphic) {
    this.graphic = graphic;
}

/**
 *  创建要素到服务端
 *  
 * @param layerId  图层id
 * @param feaMap      地图对象
 * @param attributeCfg  json对象 属性配置(TYPE TEXTGRAPHIC DATA LAYERNAME GRIDID(WID) PARENTGRIDID GRIDLEVEL GRIDNAME)
 * @param oldData    json对象　 自定义配置oldData内容.
 */
FfcsFeatureLayer.prototype.createFeatureToServer = function(layerId,feaMap,attributeCfg,oldData){
	var _data = {};
	_data = $.fn.ffcsMap.DataCvtUtil(oldData,true);//转换
	_data._oldData = oldData;
	var ds = JSON.stringify(_data);
	
	if(attributeCfg == undefined ){
		return false;
	}
	var graphic = this.graphic;
	
	var temp_attributeCfg = $.extend(true,attributeCfg,{"DATA": ds});
	graphic.setAttributes(temp_attributeCfg);
	
	var graphics = [];
	graphics.push(graphic);
	
	if(this.esriLayer == undefined){
		var _feaLayer = feaMap.getLayer(layerId);
		this.setEsriLayer(_feaLayer);
	}
	
	this.addFeature(graphics, successInfo , faildInfo);
	
	function successInfo(){
		alert("要素创建成功!");
	}
	
	function faildInfo(){
		alert("要素创建失败!");
	}
}

/**
 * 修改服务端要素
 * 
 * @param attributeCfg   属性配置
 * @param oldData        自定义配置
 * 
 */
FfcsFeatureLayer.prototype.editFeatureToServer = function(attributeCfg,oldData){
	var _data={};
    _data = $.fn.ffcsMap.DataCvtUtil(oldData,true);//转换
    _data._oldData = oldData;
	var ds = JSON.stringify(_data);
	
	if(attributeCfg == undefined ){
		return false;
	}
	
	var graphic = this.graphic;
	
	var temp_attributeCfg = $.extend(true,attributeCfg,{"DATA": ds});
	//var temp_attrAllCfig = $.extend(true,this.graphic.attributes, temp_attributeCfg);
	graphic.setAttributes(temp_attributeCfg);
	
	this.updateFeature([graphic],successInfo,faildInfo);
	
	function successInfo(){
		alert("要素修改成功!");
	}
	
	function faildInfo(){
		alert("要素修改失败!");
	}
	
}

/**
 * 从服务端删除要素
 * 
 * @param layerId  图层名称
 * @param feaMap　　地图对象
 */
FfcsFeatureLayer.prototype.deleteFeatureToServer =  function(layerId,feaMap){
	var _feaLayer = feaMap.getLayer(layerId);
	
	var ffcsFeatureLayer =  this;
	
	_feaLayer.on("Click",function(evt){
		var delGraphic = evt.graphic;
		
		ffcsFeatureLayer.setGraphic(delGraphic);
		ffcsFeatureLayer.setEsriLayer(_feaLayer);
		ffcsFeatureLayer.deleteFeature([delGraphic], successcall, failedcall);
		
	});
	
	
	function successcall(){
		alert("要素删除成功!");
	}
	function failedcall(){
		alert("要素删除失败!");
    }
}


/**
 * 根据条件删除要素对象
 * @param url
 * @param condition
 * @date 2015-7-14
 */
FfcsFeatureLayer.prototype.delFeaByCondition =  function(url,condition ){
	var _map = $.fn.ffcsMap.getMap();
    var query = new esri.tasks.Query();
    query.where = condition;
    query.outSpatialReference = new esri.SpatialReference({ wkid: _map.spatialReference["wkid"] });
    var del_temp_esriLayer = new esri.layers.FeatureLayer(url,{
    	 mode: esri.layers.FeatureLayer.MODE_AUTO,
    	 outFields: ["*"]
    });
    
    if(del_temp_esriLayer instanceof esri.layers.FeatureLayer){
    	del_temp_esriLayer.queryFeatures(query, function(featureSet){
    		 del_temp_esriLayer.applyEdits(null, null, featureSet.features, successcall, failedcall);
    	 }, null);
    	
    }
    
    function successcall(){
		alert("要素删除成功!");
	}
	function failedcall(){
		alert("要素删除失败!");
    }
};



/**
 * 根据sql条件查询要素  在callback中取值
 * @url  面要素服务地址
 * @param condition   同SQL语句
 * @param  callback  回调取值(featureSet) featureSet.features.length
 * 					 长度为零表示没有存在要素
 * @param  errback  执行失败回调函数
 * @date 2015-7-8
 */
FfcsFeatureLayer.prototype.queryByFeaServer = function (url,condition ,callback, errback) {
	var _map = $.fn.ffcsMap.getMap();
    var query = new esri.tasks.Query();
    query.where = condition;
    query.outSpatialReference = new esri.SpatialReference({ wkid: _map.spatialReference["wkid"] });
    var temp_esriLayer = new esri.layers.FeatureLayer(url,{
		mode:  esri.layers.FeatureLayer.MODE_SNAPSHOT,
		outFields: ["*"]
    });
    
    this.setEsriLayer(temp_esriLayer);
    
    if(temp_esriLayer instanceof esri.layers.FeatureLayer){
    	 temp_esriLayer.queryFeatures(query, callback, errback);
    }else{
    	var featureSet ={};
    	featureSet.features ={};
    	featureSet.features.length = -1;
    	callback(featureSet);
    }
   
};



FfcsFeatureLayer.prototype.hide = function (layerId,feaMap) {
	this.esriLayer = feaMap.getLayer(layerId);
	if(this.esriLayer!=undefined){
		this.esriLayer.hide();
	}
}

FfcsFeatureLayer.prototype.show = function (layerId,feaMap) {
	this.esriLayer = feaMap.getLayer(layerId);
    this.esriLayer.show();
}

FfcsFeatureLayer.prototype.on = function (event, func) {
    return dojo.connect(this.esriLayer, "on" + event, func);
}

FfcsFeatureLayer.prototype.deleteListener = function (handler) {
    dojo.disconnect(handler);
}

FfcsFeatureLayer.prototype.getVisibility = function () {
    return this.esriLayer.visible;
}


//添加图元
FfcsFeatureLayer.prototype.add = function (graphic) {
    this.esriLayer.add(graphic);
}

//删除图元
FfcsFeatureLayer.prototype.remove = function (graphic) {
    this.esriLayer.remove(graphic);
}

//是否可编辑
FfcsFeatureLayer.prototype.isEditable = function () {
    return this.esriLayer.isEditable();
}

//设置是否可编辑
FfcsFeatureLayer.prototype.setEditable = function (editable) {
    this.esriLayer.setEditable(editable);
}


//根据要素ID查询要素
FfcsFeatureLayer.prototype.query = function (feaIds, condition, geometry, callback, errback) {

    var query = new esri.tasks.Query();
		
		if (feaIds != null){
    query.objectIds = feaIds;
  }
   
   if(condition !=null){
   	 query.condition = condition;
    }
    
    if (geometry != null){
    query.geometry = geometry;
		}

    // 确保返回的空间参考系一致
    query.outSpatialReference = new esri.SpatialReference({ wkid: 4326 });

    this.esriLayer.queryFeatures(query, func, errback);
}

//根据要素ID查询要素
FfcsFeatureLayer.prototype.queryByFeatureIDs = function (feaIds, callback, errback) {

    var query = new esri.tasks.Query();

    query.objectIds = feaIds;

    // 确保返回的空间参考系一致
    query.outSpatialReference = new esri.SpatialReference({ wkid: 4326 });

    this.esriLayer.queryFeatures(query, func, errback);
}

//根据sql条件查询要素, 返回要素集
FfcsFeatureLayer.prototype.queryByCondition = function (condition, callback, errback) {

    var query = new esri.tasks.Query();

    query.where = condition;

    // 确保返回的空间参考系一致
    query.outSpatialReference = new esri.SpatialReference({ wkid: 4326 });

    this.esriLayer.queryFeatures(query, callback, errback);
}

//根据Geometry条件查询要素, 返回要素集
FfcsFeatureLayer.prototype.queryByGeometry = function (geometry, callback, errback) {

    var query = new esri.tasks.Query();

    query.geometry = geometry;

    // 确保返回的空间参考系一致
    query.outSpatialReference = new esri.SpatialReference({ wkid: 4326 });

    this.esriLayer.queryFeatures(query, callback, errback);
}

//添加要素
FfcsFeatureLayer.prototype.addFeature = function (graphics, callback, errback) {
    this.esriLayer.applyEdits(graphics, null, null, callback, errback);
}

//更新要素
FfcsFeatureLayer.prototype.updateFeature = function (graphics, callback, errback) {
    this.esriLayer.applyEdits(null, graphics, null, callback, errback);
}

//删除要素
FfcsFeatureLayer.prototype.deleteFeature = function (graphics, callback, errback) {
    this.esriLayer.applyEdits(null, null, graphics, callback, errback);
}

//根据要素ID删除
FfcsFeatureLayer.prototype.deleteByFeatureIds = function (feaIds, callback, errback) {

    function findDelete(featureSet) {
        this.esriLayer.applyEdits(null, null, featureSet.features, callback, errback);
    }

    //首先查找到该要素
    queryByFeatureIDs(feaIds, findDelete, errback);
}

//根据sql条件删除
FfcsFeatureLayer.prototype.deleteByCondition = function (condition, callback, errback) {

    function findDelete(featureSet) {
        this.esriLayer.applyEdits(null, null, featureSet.features, callback, errback);
    }

    //首先查找到该要素
    queryByCondition(condition, findDelete, errback);
}

//根据图元删除
FfcsFeatureLayer.prototype.deleteByGeometry = function (geometry, callback, errback) {

    function findDelete(featureSet) {
        this.esriLayer.applyEdits(null, null, featureSet.features, callback, errback);
    }

    //首先查找到该要素
    queryByCondition(geometry, findDelete, errback);
}

FfcsFeatureLayer.prototype.getFfcsFeature =  function(){
	return ffcsFeature;
}


