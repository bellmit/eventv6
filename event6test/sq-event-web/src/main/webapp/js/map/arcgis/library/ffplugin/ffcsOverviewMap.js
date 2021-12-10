dojo.require("esri.dijit.OverviewMap");

(function($) {    
	$.ffcsOverviewMap = function(settings){
		
		var defaultOptions = {
			/*位置*/
			attachTo : "bottom-right",
			
			/*透明度*/
			opacity : 0.5,
			
			/*宽度和高度*/
			height:120,
			width:175
		};
		
		var options = $.extend(defaultOptions,settings);
		
		if(typeof(options.map) == 'object'){
			_overviewMap = new esri.dijit.OverviewMap(options);
			_overviewMap.startup();
		}
		
		$.extend($,{
			show : function() {
				if(checkOverviewMap()){
					_overviewMap.show();
				}
			},
			hide : function() {
				if(checkOverviewMap()){
					_overviewMap.hide();
				}
			},
			destroy : function() {
				if(checkOverviewMap()){
					_overviewMap.destroy();
				}
			}
		});
		
		function checkOverviewMap(){
			return (typeof(_overviewMap) == 'object');
		}
		
		return this;
	};
	
	
	
})(jQuery);
