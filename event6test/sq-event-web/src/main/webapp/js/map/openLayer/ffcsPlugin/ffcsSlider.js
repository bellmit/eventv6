/**
*
* 功能介绍：适用于用OpenLayers3引擎加载的地图缩放以及位移工具
* 版本： v.1
* 日期： 2014.4.21
* 使用方法：
* 	需要引入jquery.ui.draggable.js和jquery-ui.js否则报无法找到draggable方法
* 	引入样式<link rel="stylesheet" type="text/css" href="<%=path%>/mnbootstrap/css/maptools.css" />
*   页面上创建div并调用代码 $("#jsSlider").esriSlider({map:map});
**/
;(function($) {    
	var _map,_sliderSelfUrl ; 
	$.fn.ffcsSlider = function(settings){

		/*创建时参数中必须包含map*/
		if(typeof(settings.map) == "object"){
			_map = settings.map;
		}

		if(!checkMap()){
			return;
		}
		
		//_sliderSelfUrl = document.location.href.substring(0,document.location.href.lastIndexOf("/") );
		_sliderSelfUrl = "../../../../js/map/arcgis" 
		var defaultOptions = {
				/*是否创建位移工具*/
				createPan:true,
				
				/*是否创建缩放工具*/
				createSlide:true,
				
				/*是否创建比例尺工具*/
				createScaleBar:true,
				
				/*是否创建中心点定位工具*/
				createCenterTool:false,
				
				/*控件位置*/
				position:"left-top",
				sliderPosition:{
					"right-top":{top:"120px",left:"auto",right:"0px"},
					"left-top":{top:"40px",left:"10px",right:"auto"}
				},
				
				sliderClass:"jsSlider",
				
				imageRoot:_sliderSelfUrl+"/library/mnbootstrap/images/nav/",
				
				/* 位移工具相关的参数 */
				panClass:"jsPan",
				panId:"jsPan",
				
				/* 放大缩小工具相关的参数 */
				slideClass:"jsSlide",
				slideId:"jsSlide",
				topZoomClass:"jsSlideTopZoom",
				topZoomId:"jsSlideTopZoom",
				bottomZoomClass:"jsSlideBottomZoom",
				bottomZoomId:"jsSlideBottomZoom",
				currentZoomClass:"jsSlideCurrentZoom",
				currentZoomId:"jsSlideCurrentZoom",
				zoomButtonClass:"jsSlideButton",
				zoomButtonId:"jsSlideButton",
				downZoomClass:"jsSlideDownZoom",
				downZoomId:"jsSlideDownZoom",
				
				/*缩放级别*/
				maxLevel:19,
				minLevel:0,
				currentLevel:11,
				
				/* 位移工具相关的参数 */
				scalebarUnit:"dual",
				
				/* 定位到的中心点位置*/
				centerButtonClass:"centerButton",
				centerX:558094.4071387239,
				centerY:3264610.506465449,
				centerExtend:{"wkid":32648}
				
			};
		
		var options = $.extend(defaultOptions,settings);
		
		var view = map.getView();
		view.setZoom(options.currentLevel);
		
		var $this = $(this);
	
		_clear();
		_create();
		
		function _clear(){
			if($this.children().length > 0){
				$this.children().remove();
			}
		}
		
		function _create(){
			$this.addClass(options.sliderClass);
			$this.css("top",options.sliderPosition[options.position].top)
			.css("left",options.sliderPosition[options.position].left)
			.css("right",options.sliderPosition[options.position].right);
			
			if(options.createPan){
				_createPan();
			}
			if(options.createSlide){
				_createSlide();
			}
			if(options.createScaleBar){
				//_createScaleBar();
			}
			
			if(options.createCenterTool){
				_createCenterTool();
			}
		}

		/*创建位移工具*/
		function _createPan(){
			/* 位移按钮事件 */
			var innerDivMethods = {
				jsPanLeft:function(){
					if(checkMap()){
						// 注意： 应用内置的动画，实现平移动画
        				var pan = ol.animation.pan({
      						duration: 2000,
      						source: _map.getView().getCenter()
    					});
    					// 注意： 使用beforeRender来添加
    					map.beforeRender(pan);
    					var view =  _map.getView();
			            var resolution = view.getResolution();
			            var size = _map.getSize();
			            var center = view.getCenter();
			            center[0] = center[0] - (size[0] * resolution) / 2;
			            view.setCenter(center);
			            
					}
				},
				jsPanRight:function(){
					if(checkMap()){
						// 注意： 应用内置的动画，实现平移动画
        				var pan = ol.animation.pan({
      						duration: 2000,
      						source: _map.getView().getCenter()
    					});
    					// 注意： 使用beforeRender来添加
    					map.beforeRender(pan);
    					var view =  _map.getView();
			            var resolution = view.getResolution();
			            var size = _map.getSize();
			            var center = view.getCenter();
			            center[0] = center[0] + (size[0] * resolution) / 2;
			            view.setCenter(center);
					}
				},
				jsPanUp:function(){
					if(checkMap()){
						// 注意： 应用内置的动画，实现平移动画
        				var pan = ol.animation.pan({
      						duration: 2000,
      						source: _map.getView().getCenter()
    					});
    					// 注意： 使用beforeRender来添加
    					map.beforeRender(pan);
    					var view =  _map.getView();
			            var resolution = view.getResolution();
			            var size = _map.getSize();
			            var center = view.getCenter();
			            center[1] = center[1] - (size[1] * resolution) / 2;
			            view.setCenter(center);
					}
				},
				jsPanDown:function(){
					if(checkMap()){
						// 注意： 应用内置的动画，实现平移动画
        				var pan = ol.animation.pan({
      						duration: 2000,
      						source: _map.getView().getCenter()
    					});
    					// 注意： 使用beforeRender来添加
    					map.beforeRender(pan);
    					var view =  _map.getView();
			            var resolution = view.getResolution();
			            var size = _map.getSize();
			            var center = view.getCenter();
			            center[1] = center[1] + (size[1] * resolution) / 2;
			            view.setCenter(center);
					}
				}
			};
		
			var $_panThis = $(this);
		
			/*位移元素参数*/
			var innerDivOptions = {
				ids:["jsPanLeft","jsPanRight","jsPanUp","jsPanDown"],
				type:"div",
				methods:innerDivMethods,
				innerClass:"${innerId}InnerDiv"
			};
			
			/* 创建控件*/
			var panDiv = $("<div id='"+options.panId+"'></div>").addClass(options.panClass);
			panDiv.appendTo($this);
			
			var innerDivOptionsIds = innerDivOptions.ids;
			for(i in innerDivOptionsIds){
				$("<"+innerDivOptions.type+" id='"+innerDivOptionsIds[i]+"'>"+"</"+innerDivOptions.type+">")
					.addClass(innerDivOptions.innerClass.replace("${innerId}",innerDivOptionsIds[i]))
					.appendTo(panDiv);
			}
			
			panDiv.children().addClass(innerDivOptions.innerClass.replace("${innerId}",panDiv.attr("id").toString()));
			
			/*点击位移控件触发事件*/
			panDiv.children().on("click",function(){
					if(checkMap()){
						innerDivMethods[$(this).attr("id").toString()].call($_panThis);
					}
			});
			
			/*鼠标离开控件上时改变背景*/
			panDiv.children().mouseout(function(){
				panDiv.css("background-image","url("+options.imageRoot+"jsPan.png)"); 
			});
			
			/*鼠标在控件上时改变背景*/
			panDiv.children().mouseover(function(){
				var imageName = $(this).attr("id");
				panDiv.css("background-image","url("+options.imageRoot+imageName+".png)"); 
			});
		}
		
		/*创建缩放工具*/
		function _createSlide(){
			var slideDiv = $("<div id='"+options.slideId+"'></div>").addClass(options.slideClass);
			slideDiv.appendTo($this);
			
			var topZoom =  $("<div id='"+options.topZoomId+"'>"+"</div>")
			.addClass(options.topZoomClass);

			var currentZoom = $("<div id='"+options.currentZoomId+"'>"+"</div>")
						.addClass(options.currentZoomClass);
			
			var zoomButton = $("<div id='"+options.zoomButtonId+"'>"+"</div>")
						.addClass(options.zoomButtonClass);
			
			var downZoom = $("<div id='"+options.downZoomId+"'>"+"</div>")
						.addClass(options.downZoomClass);
			
			var bottomZoom = $("<div id='"+options.bottomZoomId+"'>"+"</div>")
						.addClass(options.bottomZoomClass); 
				
			
			/*点击放大按钮，地图缩放级别+1 */
			topZoom.click(function(){
				var view = map.getView();
				var zoom = view.getZoom();
				var currentLevel = zoom;
				currentLevel = (currentLevel == options.maxLevel ? currentLevel : currentLevel+1);
				options.currentLevel = currentLevel;
				var view = map.getView();
				view.setZoom(currentLevel);
				_setPosition(currentLevel);
			});
			
			/*点击缩小按钮，地图缩放级别-1 */
			bottomZoom.click(function(){
				var view = map.getView();
				var zoom = view.getZoom();
				var currentLevel = zoom;
				currentLevel = (currentLevel == options.minLevel ? currentLevel : currentLevel-1);
				options.currentLevel = currentLevel;
				var view = map.getView();
				view.setZoom(currentLevel);
				_setPosition(currentLevel);
			});

			/*设置当前滚动条的位置*/
			function _setPosition(level){
				zoomButton.css("top",(options.maxLevel-level)*(currentZoom.height()-zoomButton.height())/(options.maxLevel-options.minLevel));
				return true;
			}
			
			/*根据当前滚动条的位置设置底图缩放级别*/
			function _setLevel(){
				var currentLevel = options.maxLevel*((zoomButton.offset().top - topZoom.offset().top-topZoom.height())/100);
				var view = _map.getView();
				var zoom = view.getZoom();
				view.setZoom(zoom);
			}
			
			topZoom.appendTo(slideDiv);		
			zoomButton.appendTo(currentZoom);
			currentZoom.appendTo(slideDiv);	
			bottomZoom.appendTo(slideDiv);	
			
			/*设置滑块初始位置*/
			_setPosition(options.currentLevel);
			
			var minTop;
			/*  地图缩放工具，层级大小可以拖放
			$(zoomButton).draggable({
				containment:currentZoom,
				grid: [20, (currentZoom.height()-zoomButton.height()-0.01)/(options.maxLevel-options.minLevel+1)],
				axis:"y",
				start: function(event,ui){
					minTop = ui.offset.top;
				},
				drap:function(event,ui){
					
				},
				stop:function(event,ui){
					var level = _map.getLevel()-Math.round((ui.offset.top - minTop)*(options.maxLevel-options.minLevel)/(currentZoom.height()-zoomButton.height()));
					var view = map.getView();
					view.setZoom(level);
					_setPosition(level);
				}
			});
			*/
			_map.getView().on('change:resolution', function(){
				var view = _map.getView();
				var zoom = view.getZoom();
				options.currentLevel = zoom;
				options.currentLevel = options.currentLevel > options.maxLevel ? options.maxLevel : options.currentLevel;
				options.currentLevel = options.currentLevel < options.minLevel ? options.minLevel : options.currentLevel;
				_setPosition(options.currentLevel);
	        });
			
		}
		 
		/*创建比例尺工具
		function _createScaleBar(){
			var scalebar = new esri.dijit.Scalebar({
				map : _map,
				// "dual"表示同时显示米和公里单位
				// "english"是默认值，显示的是米，而"metric"显示的是公里
				scalebarUnit : options.scalebarUnit
			});
		}*/
		
		function _createCenterTool(){
			var centerToolRoot = $("<div title='定位到中心点'>"+"</div>").addClass(options.centerButtonClass); 
			centerToolRoot.css("cursor","pointer");
			
			/*点击按钮，定位到指定位置*/
			centerToolRoot.click(function(){
				_map.centerAt(new esri.geometry.Point([options.centerX,
				                                       options.centerY],
				                                       options.centerExtend));
			});
			
			$this.append(centerToolRoot);
		}
		
		function checkMap(){
			return (typeof(_map) == "object");
		}
		
	};
	
})(jQuery);
