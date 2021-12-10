dojo.require("esri.tasks.query");
dojo.require("esri.TimeExtent");
dojo.require("esri.layers.FeatureLayer");
dojo.require("esri.InfoTemplate");
dojo.require("esri.toolbars.draw");
dojo.require("esri.renderers.SimpleRenderer");
dojo.require("esri.symbols.SimpleMarkerSymbol");
dojo.require("esri.symbols.PictureMarkerSymbol");
dojo.require("esri.symbols.SimpleFillSymbol");

dojo.require("esri.layers.WMSLayerInfo");
dojo.require("esri.layers.WMSLayer");

/**
* 功能介绍：对要素进行拉框搜索
* 版本： v.1
* 日期： 2014.5.19
 */
(function($) {    
	$.fn.ffcsFillQuery = function(settings){
	
		/*创建时参数中必须包含map*/
		if(typeof(settings.map) == "object"){
			_map = settings.map;
		}
		
		var defaultOptions = {
				//待搜索的图层
				url:"http://sampleserver6.arcgisonline.com/arcgis/rest/services/Census/MapServer/0",
				
				//true:拉框后弹框输入搜索条件
				//false:拉框后直接搜索
				showTextbox:true,
				
				//输出的字段
				outFields:["*"],
				
				//查询的字段
				searchType:"OBJECTID",
				
				searchText:"",
				//输出窗口标题字段
				title:"Block: ${OBJECTID}",
				
				content:"${*}",
				
				/*content:function(){
					var content = "${";
					for(i in outFields){
						content += outFields[i];
						content+=",";
					}
					
					content += "}";
					
					return content;
				},*/
				
				//显示结果的点样式
				imageUrl:"library/mnbootstrap/images/symbolPicker/symbols/pointImg3.png",
				
				//搜索范围
				geometry:null,
				
				//信息框
				infoTemplete:new esri.InfoTemplate(this.title, this.content)
			
		};
		
		var options = $.extend(defaultOptions,settings);
		
		$this = $(this);
	
		var root = $("<div></div>");
		root.addClass("queryBox");
		$this.css("position","relative");
   	 	root.css("position","absolute").css("top",$this.offset().top+root.height)	
 			 .css("left",$this.offset().left).css("right",$this.offset().right);
		
		root.hide();
		$this.parent().append(root);
		
		$this.on("click",function(){
			if(root.children("div").length==0){
				root.append(getPickerItem());
			}
			
			root.toggle();
		});	
		
		_create();
		
		function _create(){
			_draw = new esri.toolbars.Draw(_map);
		}
		
		function getPickerItem(){
			var itemRoot = $("<div></div>");
			itemRoot.addClass("queryBoxRoot");
			
			var selectButton = $("<button>拉框</button>");
			
			selectButton.on("click",function(){
				options.geometry = null;
				_map.graphics.clear();
				_draw.on("draw-end", addGraphics);
				drawGraphics("extent");
        	});
			
			selectButton.appendTo(itemRoot);
			
        	var textbox = $("<input type='textbox' size='7' maxlength='7'></input>");
        	
        	/*textbox.on("change",function(){
        		options.searchText=$(this).val();
        	});*/
        	
        	textbox.appendTo(itemRoot);
        	
        	var searchButton = $("<button>搜索</button>");
        		
        	searchButton.on("click",function(){
        		search(textbox.val());
        		root.hide();
        	});
        		
        	
        	searchButton.appendTo(itemRoot);
        	
        	return itemRoot;
		}
		
		function drawGraphics(type){
			_map.disableMapNavigation();
			_draw.activate(type);
		}
		
		function addGraphics(evt) {
          _draw.deactivate(); 
          _map.enableMapNavigation();
         var symbol = //new esri.symbols.SimpleFillSymbol();
         			new esri.symbols.SimpleFillSymbol( 
        			 esri.symbol.SimpleFillSymbol.STYLE_SOLID,
                    new esri.symbols.SimpleLineSymbol(
                      esri.symbols.SimpleLineSymbol.STYLE_SOLID,
                      new dojo._base.Color('#000'), 
                      1
                    ), 
                    42, 
                    42
                  );
		
          graphic = new esri.Graphic(evt.geometry, symbol);
          
          options.geometry = evt.geometry;
          
          _map.graphics.add(graphic);
          
        }
		
		/*搜索数据*/
		function search(searchTxt){ 
			if(options.geometry== null){
				alert("请先拉框确定搜索范围");
			}
			
			var featureLayer = new esri.layers.FeatureLayer(
		     		   options.url,{          
								outFields:options.outFields});
			featureLayer.setSelectionSymbol(new esri.symbols.PictureMarkerSymbol(
	      			  options.imageUrl,42,42));
			 var query = new esri.tasks.Query();
		     
			 if(searchTxt.trim()!==""){
				 query.where = options.searchType+"="+searchTxt;
			 }
			 
			// queryTask = new esri.tasks.QueryTask(options.url);

			 query.geometry = options.geometry.getExtent();         
		     query.outFields = ["*"];

		    // queryTask.execute(query,showResults);
		     featureLayer.queryFeatures(query,showResults); 
		}
		
		
		function showResults(results){
		    //清除上一次的高亮显示
		   // _map.graphics.clear();
		    //高亮样式
		 //   highlightSymbol = new esri.symbols.SimpleMarkerSymbol();
		     //查询结果样式
		    symbol = new esri.symbols.PictureMarkerSymbol(
	      			  options.imageUrl,42,42);		//new esri.symbols.SimpleMarkerSymbol();
		    //遍历查询结果
		   
		    for (var i=0;i<results.features.length; i++)
		    {
		       var graphic=results.features[i];
		       //设置查询到的graphic的显示样式
		       graphic.setSymbol(symbol);
		       //设置graphic的信息模板
		       graphic.setInfoTemplate(options.infoTemplete);
		       //把查询到的结果添加_map.graphics中进行显示
		       _map.graphics.add(graphic);
		       
		    }
		   // _map.graphics.enableMouseEvents();
		 }
		 
	};
	
})(jQuery);
