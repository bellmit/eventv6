dojo.require("esri.geometry.Point");
(function($) {    
	
	/*
	 * 地图中心点定位工具
	 * 参数：
	 * {
	 * 	map:currentMap,             //当前地图
		centerX : -95.249,          //中心点X坐标
		centerY : 38.954,           //中心点y坐标
		centerExtend : {wkid:2437}, //wkid
		zoom:5                      //缩放级别 0以下表示不缩放
		}
	 * */
	$.ffcsMapCenterAt = function(options){
		if(typeof(options.map) == "object"){
			var point = new esri.geometry.Point([options.centerX,
			                                     options.centerY],
			                                     options.centerExtend);
			options.map.centerAt(point);
			
			if(options.zoom>0){
				options.map.setLevel(options.zoom);
			}
		}
		
	};		
	
})(jQuery);
