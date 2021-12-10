
(function($) {    
	var draw,Query,SimpleMarkerSymbol,PictureMarkerSymbol,SimpleFillSymbol,PictureFillSymbol,FeatureLayer,opts;
	
	/**
	 * 元素拉框查询接口
	 */
	$.fn.ffcsQuery = function(options){
		$.fn.ffcsQuery.init();
		opts = $.extend({}, $.fn.ffcsQuery.defaults, options); 
	};
	
	$.fn.ffcsQuery.init = function(){
		require([ 
			        "esri/tasks/query",
			        "esri/layers/FeatureLayer",
			        "esri/InfoTemplate",
			        "esri/symbols/SimpleMarkerSymbol",
			        "esri/symbols/PictureMarkerSymbol",
			        "esri/symbols/SimpleFillSymbol",
			        "esri/symbols/PictureFillSymbol",
					"dojo/domReady!"
					],
			   function(p_query, p_FeatureLayer,
					   p_InfoTemplate, p_SimpleMarkerSymbol,
					   p_PictureMarkerSymbol,
					   p_SimpleFillSymbol,p_PictureFillSymbol
			   ) {
					// 初始值
				Query = p_query; 
				FeatureLayer = p_FeatureLayer;
				InfoTemplate = p_InfoTemplate;
				SimpleMarkerSymbol = p_SimpleMarkerSymbol;
				PictureMarkerSymbol = p_PictureMarkerSymbol;
				SimpleFillSymbol = p_SimpleFillSymbol;
				PictureFillSymbol = p_PictureFillSymbol;
			});
	};
	
	$.fn.ffcsQuery.defaults = {
			//待搜索的图层
			url:"http://sampleserver6.arcgisonline.com/arcgis/rest/services/Census/MapServer/0",
			
			//输出的字段
			outFields:["*"],
			
			//查询的字段
			searchType:"OBJECTID",
			
			searchText:"",
			//输出窗口标题字段
			title:"Block: ${OBJECTID}",
			
			content:"${*}",
			
			//显示结果的点样式
			imageUrl:"library/mnbootstrap/images/symbolPicker/symbols/pointImg3.png",
			
			geometryType:"extent",
			
			//搜索范围
			geometry:null
			
			//信息框
			//infoTemplete:new InfoTemplate(this.title, this.content)
	};
	
	/*
	 * 拉框
	 * type:拉框时画图的类型 默认是矩形
	 **/
	$.fn.ffcsQuery.drawGeometry = function(options){
		opts = $.extend(opts, options);
		
		opts.geometry = null;
		opts.map.graphics.clear();
		opts.draw.on("draw-end", addGraphics);
		drawGraphics(opts.geometryType);
		
		function drawGraphics(type){
			opts.map.disableMapNavigation();
			opts.draw.activate(type);
		}
		
		function addGraphics(evt) { 
		 opts.draw.deactivate(); 
		 opts.map.enableMapNavigation();
         var symbol = new SimpleFillSymbol();
         			/*new SimpleFillSymbol( 
        			 SimpleFillSymbol.STYLE_CROSS,
                    new SimpleLineSymbol(
                      SimpleLineSymbol.STYLE_SOLID,
                      new dojo._base.Color('#000'), 
                      1
                    ), 
                    42, 
                    42
                  );*/

         var graphic = new esri.Graphic(evt.geometry, symbol);
          
          opts.geometry = evt.geometry;
          
          opts.map.graphics.add(graphic);
          
          $.fn.ffcsQuery.query();
		}
	};
	
	/*
	 * 查询接口
	 * url：查询的图层路径
	 * searchType:搜索的字段名
	 * searchText：搜索的文本
	 * geometry：搜索的范围，默认按照拉框中的范围
	 * title:点击查询出的元素的信息框的标题 ${}中代表字段值，如${OBJECTID}
	 * content：点击查询出的元素的信息框的内容${}中代表字段值，如${OBJECTID}，${*}代表输出全部字段值
	 * imageUrl：查询出的元素的样式图片路径
	 * */
	$.fn.ffcsQuery.query = function(options){
		opts = $.extend(opts, options);
		
		var featureLayer = new FeatureLayer(
				opts.url,{outFields:opts.outFields});
		featureLayer.setSelectionSymbol(new PictureMarkerSymbol(
				opts.imageUrl,42,42));
		
		var query = Query();
	     
		 if(opts.searchText.trim()!==""){
			 query.where = opts.searchType+ "=" + opts.searchText;
		 }
		 
		 query.geometry = opts.geometry.getExtent();         
	     query.outFields = ["*"];
	     
	     featureLayer.queryFeatures(query,showResults); 
	     
	     function showResults(results){
			    //清除上一次的高亮显示
			   // _map.graphics.clear();
			    //高亮样式
			 //   highlightSymbol = new esri.symbols.SimpleMarkerSymbol();
			     //查询结果样式
			    symbol = new PictureMarkerSymbol(
			    		opts.imageUrl,42,42);		//new esri.symbols.SimpleMarkerSymbol();
			    //遍历查询结果
			    for (var i=0;i<results.features.length; i++)
			    {
			       var graphic=results.features[i];
			       //设置查询到的graphic的显示样式
			       graphic.setSymbol(symbol);
			       //设置graphic的信息模板
			       graphic.setInfoTemplate(new InfoTemplate(opts.title, opts.content));
			       //把查询到的结果添加_map.graphics中进行显示
			       opts.map.graphics.add(graphic);
			       
			    }
			   // _map.graphics.enableMouseEvents();
			 }
	};
	
})(jQuery);
