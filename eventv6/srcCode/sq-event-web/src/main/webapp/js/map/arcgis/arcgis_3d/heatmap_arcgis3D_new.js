setTimeout(function() {
	dojo.addOnLoad(function () {
	    dojo.declare("HeatmapLayer",null, {
	    	name:"HeatmapLayer",
	        view: null,
	        heatmap: null,
	        config: null,
	        visible:true,
	        data:null,
	        Point:null,
	        gLang:null,
	        constructor: function (view,Point,config,gLang,data,max) {
	            this.init(view,Point,config,gLang,data,max);
	        },
	        init:function(view,Point, config, data,max) {
	            this.setBaseMap(view);
	            this.createDIV();
	            this.defaultConfig();
	            // 更新配置
	            if(config){
	                this.setConfig(config);
	            }
	            this.createLayer();
	            this.Point=Point;
	            this.gLang=gLang;
	            this.setData(data);
	            this.setMax(max);
	            this.startMapEventListeners();
	        },
	        setBaseMap:function(view) {
	            this.view = view;
	        },
	        defaultConfig:function() {
	            this.config={
	                container: this.box,
	                radius: 40,
	                debug: false,
	                visible: true,
	                useLocalMaximum: false,
	                gradient: {
	                    0.45: "rgb(000,000,255)",
	                    0.55: "rgb(000,255,255)",
	                    0.65: "rgb(000,255,000)",
	                    0.95: "rgb(255,255,000)",
	                    1.00: "rgb(255,000,000)"
	                    	
	                }
				};
	            // this.heatmap = heatmapFactory.create(this.config);
	        },
	        setConfig:function (config) {
	            this.config={
	                container: this.box,
	                radius: config.radius,
	                maxOpacity: config.maxOpacity,
	                minOpacity: config.minOpacity,
	                debug: false,
	                visible: true,
	                useLocalMaximum: false,
	                gradient: config.gradient
	            };
	        },
	        setData:function (points,max) {
	            this.data = points
	            this.setMax(max);
	        },
	        setMax:function (max) {
	        	if(max !== null && max !== undefined && max !== ''){
	        		 this.max = max;
	        	}
	        },
	        setVisible:function(bool) {
	            if (!this.box || this.visible===bool) return;
	            this.box.hidden = !bool;
	            this.visible = bool;
	            bool===true && setCharts();
	        },
	        refreshBegin:function(){
	            this.box.hidden = true;
	        },
	        refreshEnd:function(){
	            this.box.hidden = false;
	        },
	        on:function(eventName, handler, context){
	            this.chart.on(eventName, handler, context);
	        },
	        off:function(eventName, handler, context){
	            this.chart.off(eventName, handler, context);
	        },
	        map_DragStart_Listener : null,
	        map_DragEnd_Listener : null,
	        map_ZoomStart_Listener : null,
	        map_ZoomEnd_Listener : null,
	        map_ExtentChange_Listener : null,
	        map_click_Listener : null,
	        /* 创建HeatMaplayer的容器，添加到map的layers下面 */
	        createDIV:function () {
	            this.box = document.createElement("div");
	            this.box.setAttribute("id","testData");
	            this.box.style.width =  this.view.width + 'px';
	            this.box.style.height = this.view.height + 'px';
	            this.box.style.position = "absolute";
	            this.box.style.top = 0;
	            this.box.style.left = 0;
	            let parent = document.getElementsByClassName("esri-view-surface")[0];
	            parent.appendChild(this.box);
	        },
			/* 创建HeatMaplayer的容器，添加到map的layers下面 */
	        createLayer:function() {
	            this.heatmap = h337.create(this.config);
	        },
	        /* 转换数据 */
	        convertHeatmapData: function (data) {
	            var heatPluginData = {
	                max: this.max!=null?this.max: this.MaxValue(data),
	                data: [] // 空数据
	            };
				var	parsedData = {};
					var extent = $.fn.ffcsMap.getViewExtent(),x=0,y=0;
	                for (var i = 0; i < data.length; i++) {
						x= parseFloat(data[i][0]),y=parseFloat(data[i][1]);
						// 屏幕外不渲染
						if( extent.xmax>x  &&  extent.xmin<x &&  extent.ymax >y &&  extent.ymin<y){	
							var screenpoint = this.view.toScreen(new this.Point({
	                            longitude:data[i][0],
	                            latitude:data[i][1],
	                        }));
							
						/*
						 * if(!parsedData[screenpoint.x]){
						 * parsedData[screenpoint.x] = {}; }
						 * if(!parsedData[screenpoint.x][screenpoint.y]){
						 * parsedData[screenpoint.x][screenpoint.y] = 0; }
						 * parsedData[screenpoint.x][screenpoint.y] +=1;
						 * //相同坐标，权重累加
						 */		                        heatPluginData.data.push(
	                            {
	                                x:Math.round(screenpoint.x),
	                                y:Math.round(screenpoint.y),
	                                value: 1
	                            }
	                        )
						}
	                }
				
	         
	            return heatPluginData;
	        },
	        MaxValue:function (data) {
	            // 为设置value,则默认为1
	            var max=1;
	            	 if(data.length >1  && data[1].length==3){
		                    for(var i=0;i<data.length;i++){
		                        if(max<=data[i][2]){
		                            max=data[i][2]
		                        }
		                    }
		                }
	            
	           
	            return max;
	        },
	        addData:function () {
	            let data = this.convertHeatmapData(this.data)
	            this.heatmap.setData(data);
	            this.box.style.position = "absolute";
	        },
	        /* 刷新layer */
	        freshenLayer:function () {
	            this.heatmap.setData({
	                max: 1,
	                data: []
	            });
	            this.addData();
	        },
	        /* 清除渲染效果 */
	        clearData: function () {
	            // empty heat map
	           this.heatmap.removeData();
	           this.heatmap.setData( []);
	        },
			/* 销毁实例 */
	        removeLayer:function() {
	            this.clearData();
	/*
	* this.box.outerHTML = ""; this.view = null; this.box = null; this.config =
	* null;
	*/
	            this.data = [];
	            /*
				 * this.map_DragStart_Listener.remove();
				 * this.map_DragEnd_Listener.remove();
				 * this.map_ZoomStart_Listener.remove();
				 * this.map_ZoomEnd_Listener.remove();
				 * this.map_ExtentChange_Listener.remove();
				 */
	        },
			/* 监听地图事件，根据图层是否显示，判断是否重绘热力图 */
	        startMapEventListeners:function() {
	        	if(this.gLang!=null && this.gLang!== undefined ){
	        		   let view=this.view;
	 				  view.watch("extent",gLang.hitch(this,function () {
	 				  if(!this.visible) return; this.freshenLayer();
	 				  this.box.hidden = false; }));
	 				 view.watch("camera",gLang.hitch(this,function () {
	 				  if(!this.visible) return; this.freshenLayer();
	 				  this.box.hidden = false; }));
	        	}		
				 
	        }
	    });
	});
			}, 3000);


