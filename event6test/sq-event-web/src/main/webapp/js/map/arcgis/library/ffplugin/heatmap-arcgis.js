dojo.addOnLoad(function () {
    dojo.declare("HeatmapLayer", [esri.layers.DynamicMapServiceLayer], {
        /*
	{
		map: <a handle to the map>,
		domNodeId: <an id to the domNode>,
	}
	*/
        // variables
        properties: {},
        heatMap: null,
        // constructor
        constructor: function (properties) {
            dojo.safeMixin(this.properties, properties);
            // map var
            this._map = this.properties.map;
            // last data storage
            this.lastData = [];
            // map node
            this.domNode = document.getElementById(this.properties.domNodeId);
            // config
            this.config = {
                element: this.domNode,
                width: this._map.width,
                height: this._map.height,
				isMapChange:{xmax:0,xmin:0,ymin:0,ymax:0},
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
            // mix in config for heatmap.js settings
            dojo.safeMixin(this.config, properties.config);
            // create heatmap
            this.heatMap = heatmapFactory.create(this.config);
            // loaded
            this.loaded = true;
            this.onLoad(this);
            // global maximum value
            this.globalMax = 0;
            // connect on resize
            dojo.connect(this._map, "onResize", this, this.resizeHeatmap);
            // heatlayer div styling
            this.domNode.style.position = 'relative';
            this.domNode.style.display = 'none';
        },
        resizeHeatmap: function (extent, width, height) {
            // set heatmap data size
            this.heatMap.set("width", width);
            this.heatMap.set("height", height);
            // set width and height of container
            dojo.style(this.domNode, {
                "width": width + 'px',
                "height": height + 'px'
            });
            // set width and height of canvas element inside of container
            var child = dojo.query(':first-child', this.domNode);
            if (child) {
                child.attr('width', width);
                child.attr('height', height);
            }
            // set atx canvas width and height fix
            var actx = this.heatMap.get("actx");
            actx.canvas.height = height;
            actx.canvas.width = width;
            this.heatMap.set("actx", actx);
            // refresh image and heat map size
            this.refresh();
        },
        // stores heatmap converted data into the plugin which renders it
        storeHeatmapData: function (heatPluginData) {
            // set heatmap data
            this.heatMap.store.setDataSet(heatPluginData);
        },
        // converts parsed data into heatmap format
        convertHeatmapData: function (parsedData) {
            // variables
            var xParsed, yParsed, heatPluginData, dataPoint, screenGeometry;
            // set heat plugin data object
            heatPluginData = {
                max: parsedData.max,
                data: [] // empty data
            };
            // if data
            if (parsedData.data) {
                // for all x values
                for (xParsed in parsedData.data) {
                    // if data[x]
                    if (parsedData.data.hasOwnProperty(xParsed)) {
                        // for all y values and count
                        for (yParsed in parsedData.data[xParsed]) {
                            if (parsedData.data[xParsed].hasOwnProperty(yParsed)) {
                                // convert data point into screen geometry
                                screenGeometry = esri.geometry.toScreenGeometry(this._map.extent, this._map.width, this._map.height, parsedData.data[xParsed][yParsed].dataPoint);
                                // push to heatmap plugin data array
                                heatPluginData.data.push({
                                    x: screenGeometry.x,
                                    y: screenGeometry.y,
                                    count: parsedData.data[xParsed][yParsed].count // count value of x,y
                                });
                            }
                        }
                    }
                }
            }
            // store in heatmap plugin which will render it
            this.storeHeatmapData(heatPluginData);
        },
        // runs through data and calulates weights and max
        parseHeatmapData: function (features) {
            // variables
            var i, parsedData, dataPoint, attributes;
            // if data points exist
            if (features ) {
                // create parsed data object
                parsedData = {
                    max: 0,
                    data: []
                };
                if (!this.config.useLocalMaximum) {
                    parsedData.max = this.globalMax;
                }
                // for each data point
                for (i = 0; i < features.length; i++) {
                    // create geometry point
                    dataPoint = esri.geometry.Point(features[i].geometry);
                    // check point
                    var validPoint = false;
                    // if not using local max, point is valid
                    if (!this.config.useLocalMaximum) {
                        validPoint = true;
                    }
                    // using local max, make sure point is within extent
                    else if(this._map.extent.contains(dataPoint)){
                        validPoint = true;
                    }
                    if (validPoint) {
                        // attributes
                        attributes = features[i].attributes;
                        // if array value is undefined
                        if (!parsedData.data[dataPoint.x]) {
                            // create empty array value
                            parsedData.data[dataPoint.x] = [];
                        }
                        // array value array is undefined
                        if (!parsedData.data[dataPoint.x][dataPoint.y]) {
                            // create object in array
                            parsedData.data[dataPoint.x][dataPoint.y] = {};
                            // if count is defined in datapoint
                            if (attributes && attributes.hasOwnProperty('count')) {
                                // create array value with count of count set in datapoint
                                parsedData.data[dataPoint.x][dataPoint.y].count = attributes.count;
                            } else {
                                // create array value with count of 0
                                parsedData.data[dataPoint.x][dataPoint.y].count = 0;
                            }
                        }
                        // add 1 to the count
                        parsedData.data[dataPoint.x][dataPoint.y].count += 1;
                        // store dataPoint var
                        parsedData.data[dataPoint.x][dataPoint.y].dataPoint = dataPoint;
                        // if count is greater than current max
                        if (parsedData.max < parsedData.data[dataPoint.x][dataPoint.y].count) {
                            // set max to this count
                            parsedData.max = parsedData.data[dataPoint.x][dataPoint.y].count;
                            if (!this.config.useLocalMaximum) {
                                this.globalMax = parsedData.data[dataPoint.x][dataPoint.y].count;
                            }
                        }

                    }
                }
                // convert parsed data into heatmap plugin formatted data
                this.convertHeatmapData(parsedData);
            }
        },
		 
		resizeSingleHeatmap: function (extent, width, height) {
            // set heatmap data size
            this.heatMap.set("width", width);
            this.heatMap.set("height", height);
            // set width and height of container
            dojo.style(this.domNode, {
                "width": width + 'px',
                "height": height + 'px'
            });
            // set width and height of canvas element inside of container
            var child = dojo.query(':first-child', this.domNode);
            if (child) {
                child.attr('width', width);
                child.attr('height', height);
            }
            // set atx canvas width and height fix
            var actx = this.heatMap.get("actx");
            actx.canvas.height = height;
            actx.canvas.width = width;
            this.heatMap.set("actx", actx);
			//this.refresh();
        },
		parseSingleHeatmapData: function (features) {
            if (features==null ||features==undefined||features.length==0 ) {
				return;
			}
				parsedData =  {};
				var extent = this._map.extent, width=this._map.width, height=this._map.height,sg,x=0,y=0;
               
                for (var i = 0; i < features.length; i++) {
					x = parseFloat(features[i].geometry.x),y=parseFloat(features[i].geometry.y);
					if( extent.xmax>x  &&  extent.xmin<x &&  extent.ymax >y &&  extent.ymin<y){
						sg = esri.geometry.toScreenGeometry(extent, width, height,features[i].geometry);
						if(!parsedData[sg.x]){
							parsedData[sg.x] = {};
						}
						if(!parsedData[sg.x][sg.y]){
							parsedData[sg.x][sg.y] = 0;
						}
						parsedData[sg.x][sg.y] +=1;
					}
                }
				heatPluginData = {max:0,data:[]};
				for (xParsed in parsedData) {
                    // if data[x]
                    if (parsedData.hasOwnProperty(xParsed)) {
                        // for all y values and count
                        for (yParsed in parsedData[xParsed]) {
                            if (parsedData[xParsed].hasOwnProperty(yParsed)) {
                               heatPluginData.data.push({x: xParsed,y: yParsed,count: parsedData[xParsed][yParsed]});
							   if(heatPluginData.max < parsedData[xParsed][yParsed]){
								   heatPluginData.max = parsedData[xParsed][yParsed];
							   }
                            }
                        }
                    }
                }
			if(this.config.singleMax){
				heatPluginData.max = this.config.singleMax;
			}	
			this.heatMap.store.setDataSet(heatPluginData);	
        },
		setSingleData:function(features){
			this.resizeSingleHeatmap(null, this._map.width, this._map.height);
            this.lastData = features;
            this.parseSingleHeatmapData(features);
			this.refresh();
		},
        // set data function call
        setData: function (features) {
			if(features==null || features.length==0){
				return;
			}
			if(this.config.single){
				this.setSingleData(features);
				return;
			}
            // set width/height
            this.resizeHeatmap(null, this._map.width, this._map.height);
            // store points
            this.lastData = features;
            // create data and then store it
            this.parseHeatmapData(features);
            // redraws the heatmap
            this.refresh();
        },
        // add one feature to the heatmap
        addDataPoint: function (feature) {
            if (feature) {
                // push to data
                this.lastData.push(feature);
                // set data
                setData(this.lastData);
            }
        },
        // return data set of features
        exportDataSet: function () {
            return this.lastData;
        },
        // clear data function
        clearData: function () {
            // empty heat map
            this.heatMap.clear();
            // empty array
            var empty = [];
            // set data to empty array
            this.setData(empty);
        },
        // get image
        getImageUrl: function (extent, width, height, callback) {
			if(this.config.isMapChange.xmin == extent.xmin && this.config.isMapChange.ymin == extent.ymin && 
				this.config.isMapChange.ymax == extent.ymax && this.config.isMapChange.xmax == extent.xmax){
            callback(this.heatMap.get("canvas").toDataURL("image/png"));
				return;
			}
			this.config.isMapChange = extent;
			if(this.config.single){
				this.setSingleData(this.lastData);
			}else{
				this.parseHeatmapData(this.lastData);
			}
        }
    });
});