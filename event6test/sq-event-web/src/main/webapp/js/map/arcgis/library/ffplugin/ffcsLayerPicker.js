;(function($) { 
	
	/**
	* 功能介绍：显示与隐藏地图上的图层的相关工具
	* 版本： v.1
	* 日期： 2014.5.19
	 */
	$.fn.ffcsLayerPicker = function(settings){
		
		/*创建时参数中必须包含map*/
		if(typeof(settings.map) == "object"){
			_map = settings.map;
		}
		
		var defaultOptions = {
				createLayerPicker:true
		};
		
		var options = $.extend(defaultOptions,settings);
		
		$this = $(this);
		
		
		var root = $("<div></div>");
		root.addClass("layerPicker");
		root.hide();

		$(this).parent().append(root);
		
		$(this).css("position","relative");	
		root.css("position","absolute").css("top",$(this).offset().top+root.height)	
		.css("left",$(this).offset().left).css("right",$(this).offset().right);

		$(this).click(function(){
			if(root.children("div").length==0){
				if(options.createLayerPicker){
					root.append(getPickerItem());
				}
			}
			
			root.toggle();
		}); 
		
		function getPickerItem(){
			var itemRoot = $("<div></div>");
        	
			itemRoot.addClass("layerPickerRoot");
			
        	for(i in _map.layerIds){
        		var layerDiv = $("<div></div>");
        		var layer = _map.getLayer(_map.layerIds[i]);
        		var checkbox = $("<input type='checkbox' value='"+layer.id+"' checked='checked' width='30px'></input>");
        		
        		checkbox.click(function(){
        			if($(this).attr("checked")=="checked"){
        				_map.getLayer($(this).attr("value")).show();
        			}else{
        				_map.getLayer($(this).attr("value")).hide();
        			}
        		});
        		checkbox.appendTo(layerDiv);
        		
        		$("<span>"+layer.id+"</span>").appendTo(layerDiv);
        		
        		layerDiv.appendTo(itemRoot);
        	}
        	
        	
        	return itemRoot;
		}
			
			
	};
})(jQuery);