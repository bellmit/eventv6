dojo.require("esri.toolbars.draw");
dojo.require("esri.symbols.SimpleMarkerSymbol");
dojo.require("esri.symbols.SimpleLineSymbol");
dojo.require("esri.symbols.SimpleFillSymbol");
dojo.require("esri.symbols.PictureFillSymbol");
dojo.require("esri.symbols.CartographicLineSymbol");
dojo.require("esri.symbols.PictureMarkerSymbol");
dojo.require("esri.graphic");
dojo.require("dojo._base.Color");
dojo.require("esri.layers.GraphicsLayer");
dojo.require("dojox.widget.ColorPicker");
dojo.require("dojo.dom-style");

/**
*
* 功能介绍：点线面的绘制与样式选择工具
* 版本： v.1
* 日期： 2014.5.14
**/
;(function($) {    		
	var symbolOption = {
		point : {
			symbolType : "point",
			drawButtonText : "画点",
			styleButtonText : "样式",
			pickerId : "pointPicker",
			pickerClass : "pointPicker",
			imageRoot : "library/mnbootstrap/images/symbolPicker/symbols/",
			imageClass : "pointImage",
			imageNames : [ "pointImg1.png", "pointImg2.png", "pointImg3.png" ],
			currentImage : "pointImg1.png",
			root : null
		},
		line : {
			symbolType : "line",
			drawButtonText : "画线",
			styleButtonText : "样式",
			pickerId : "linePicker",
			pickerClass : "linePicker",
			root : null
		},
		polygon : {
			symbolType : "polygon",
			drawButtonText : "画面",
			styleButtonText : "样式",
			pickerId : "polygonPicker",
			pickerClass : "polygonPicker",
			imageRoot : "library/mnbootstrap/images/symbolPicker/symbols/",
			imageClass : "polygonImage",
			imageNames : [ "polygonImg1.jpg", "polygonImg2.png" ],
			currentImage : "polygonImg1.jpg",
			root : null
		},
		/*已选的宽度*/
		symbolSize : {
			line : 1,
			polygon : 1
		},
		/*已选的颜色*/
		symbolColor : {
			line : "#000",
			polygonLine : "#000",
			polygonFill : "#000"
		},
		/*可选和已选的样式*/
		symbolStyle : {
			line : {
//				currentStyle : esri.symbols.SimpleLineSymbol.STYLE_SOLID,
				currentStyle : "solid",
				style : [ {
					name : "style_solid",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SOLID
					value : "solid"
				}, {
					name : "style_dash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASH
					value : "dash"
				}, {
					name : "style_dashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASHDOT
					value : "dashdot"
				}, {
					name : "style_dashDotDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASHDOTDOT
					value : "dashdotdot"
				}, {
					name : "style_Dot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DOT
					value : "dot"
				}, {
					name : "style_longDash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_LONGDASH
					value : "longdash"
				}, {
					name : "style_longDashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_LONGDASHDOT
					value : "longdashdot"
				}, {
					name : "style_null",
//					value : esri.symbols.SimpleLineSymbol.STYLE_NULL
					value : "null"
				}, {
					name : "style_shortDash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASH
					value : "shortdash"
				}, {
					name : "style_shortDashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASHDOT
					value : "shortdashdot"
				}, {
					name : "style_shortDashDotDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASHDOTDOT
					value : "shortdashdotdot"
				}, {
					name : "style_shortDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDOT
					value : "shortdot"
				} ]
			},
			polygonLine : {
//				currentStyle : esri.symbols.SimpleLineSymbol.STYLE_SOLID,
				currentStyle : "solid",
				style : [ {
					name : "style_shortSolid",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SOLID
					value : "shortsolid"
				}, {
					name : "style_dash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASH
					value : "dash"
				}, {
					name : "style_dashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASHDOT
					value : "dashdot"
				}, {
					name : "style_dashDotDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DASHDOTDOT
					value : "dashdotdot"
				}, {
					name : "style_Dot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_DOT
					value : "dot"
				}, {
					name : "style_longDash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_LONGDASH
					value : "longdash"
				}, {
					name : "style_longDashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_LONGDASHDOT
					value : "longdashdot"
				}, {
					name : "style_null",
//					value : esri.symbols.SimpleLineSymbol.STYLE_NULL
					value : "null"
				}, {
					name : "style_shortDash",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASH
					value : "shortdash"
				}, {
					name : "style_shortDashDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASHDOT
					value : "shortdashdot"
				}, {
					name : "style_shortDashDotDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDASHDOTDOT
					value : "shortdashdotdot"
				}, {
					name : "style_shortDot",
//					value : esri.symbols.SimpleLineSymbol.STYLE_SHORTDOT
					value : "shortdot"
				} ]
			},
			polygonFill : {
//				currentStyle : esri.symbol.SimpleFillSymbol.STYLE_SOLID,
				currentStyle : "solid",
				style : [
						{
							name : "style_solid",
//							value : esri.symbol.SimpleFillSymbol.STYLE_SOLID
							value : "solid"
						},
						{
							name : "style_backward_diagonal",
//							value : esri.symbol.SimpleFillSymbol.STYLE_BACKWARD_DIAGONAL
							value : "backwarddiagonal"
						},
						{
							name : "style_cross",
//							value : esri.symbol.SimpleFillSymbol.STYLE_CROSS
							value : "cross"
						},
						{
							name : "style_diagonal_cross",
//							value : esri.symbol.SimpleFillSymbol.STYLE_DIAGONAL_CROSS
							value : "diagonalcross"
						},
						{
							name : "style_forward_diagonal",
//							value : esri.symbol.SimpleFillSymbol.STYLE_FORWARD_DIAGONAL
							value : "forwarddiagonal"
						}, {
							name : "style_null",
//							value : esri.symbol.SimpleFillSymbol.STYLE_NULL
							value : "null"
						}, {
							name : "style_vertical",
//							value : esri.symbol.SimpleFillSymbol.STYLE_VERTICAL
							value : "vertical"
						} ]
			}

		}

	};
	
	
	$.fn.ffcsSymbolPicker = function(settings){
		
		if(typeof(settings.map) == "object"){
			_map = settings.map;
		}

		var defaultOptions = {
				createPoint:true,       // 是否创建画点相关工具
				createLine:true,		// 是否创建画线相关工具
				createPolygon:true		// 是否创建画面相关工具
		};
		
		var options = $.extend(defaultOptions,settings);

		_draw = new esri.toolbars.Draw(_map);//options.draw;
		_draw.on("draw-end", addGraphic);
		
		
		$this = $(this);
		
		var root = $("<div></div>");
		root.addClass("sbPicker");
		root.hide();

		$(this).parent().append(root);
		
		$(this).css("position","relative");	
		root.css("position","absolute").css("top",$(this).offset().top+root.height)	
		.css("left",$(this).offset().left).css("right",$(this).offset().right);

		$(this).click(function(){
			if(root.children("div").length==0){
				if(options.createPoint){
					root.append(getPickerItem("point"));
				}
				if(options.createLine){
					root.append(getPickerItem("line"));
				}
				if(options.createPolygon){
					root.append(getPickerItem("polygon"));
				}
			}
			
			root.toggle();
		}); 
		
		function getPickerItem(symbolType){
			var pickerRoot = $("<div></div>");
			var drawButton = $("<div><span>"+symbolOption[symbolType].drawButtonText+"</span></div>").addClass("sbPicker-item").appendTo(pickerRoot);
			drawButton.on("click",function(){
				_map.disableMapNavigation();
				_draw.activate(symbolType);
			});
			var styleButton = $("<div id='"+symbolType+"-style"+"'><span>"+symbolOption[symbolType].styleButtonText+"</span></div>").addClass("sbPicker-item").appendTo(pickerRoot);
			styleButton.addClass(symbolType+"-style");
			
			var linePicker = getPicker(symbolType);
			styleButton.append(linePicker);
			
			styleButton.children("div").hide();
			
			styleButton.children("span").click(function(){
				$(this).parent().parent().siblings().children().children("span").next("div").hide();
				$(this).next("div").toggle();
			});
			
			drawButton.click(function(){
				styleButton.children("div").hide();
			});
			
			return pickerRoot;
		}
		
		function drawGraphics(type){
			_map.disableMapNavigation();
			_draw.activate(type);
		}
		
		//绘制动作完毕时将对应图形添加到图层
        function addGraphic(evt) {
          //deactivate the toolbar and clear existing graphics 
          
          _draw.deactivate(); 
          _map.enableMapNavigation();

          // figure out which symbol to use
          var symbol;
          
          switch (evt.geometry.type) {
          case "point":
          case "multipoint":
            symbol = new esri.symbols.PictureMarkerSymbol(
      			  symbolOption["point"].imageRoot+symbolOption["point"].currentImage,42,42);
            break;
          case "line":
          case "polyline":
            symbol = new esri.symbols.SimpleLineSymbol(
            		getSymbolStyle("line"),
            	    new dojo._base.Color(getSymbolColor("line")), getSymbolSize("line"));
            break;
          default:
            symbol = new esri.symbols.SimpleFillSymbol(getSymbolStyle("polygonFill"),
                    new esri.symbols.SimpleLineSymbol(
                    		getSymbolStyle("polygonLine"),getSymbolColor("polygonLine"), 
                    		getSymbolSize("polygon")
                    ), 
                    42, 
                    42
                  );
            break;
        }
          
		
          graphic = new esri.Graphic(evt.geometry, symbol);
          
          _map.graphics.add(graphic);
          
        }
		
        /*获取对应图形的div*/
        function getPicker(symbolType){
        	var pickerRoot;
        	switch(symbolType){
        		case "point":
        			pickerRoot = getPointPickerRoot();
        			break;
        		case "line":
        			pickerRoot = getLinePickerRoot();
        			break;
        		case "polygon":
        			pickerRoot = getPolygonPickerRoot();
        			break;
        	}       		
			return pickerRoot;
        }
        
        /*点样式相关div*/
        function getPointPickerRoot(){
        	var symbolType = "point";
        	var symbolRoot = $("<div>"+"</div>");
			symbolRoot.addClass("symbolPicker");
			var currentSymbolOption = symbolOption[symbolType];
				
			var pickerRoot = $("<div>"+"</div>");
			pickerRoot.addClass(currentSymbolOption.pickerClass);
			
			var submitButton = $("<button>确定</button>");
			submitButton.addClass("symbolPicker-submitButton");
			
			pickerRoot.appendTo(symbolRoot);
			submitButton.appendTo(symbolRoot);
			symbolOption[symbolType].root = symbolRoot;
			
			submitButton.click(function(){
				$(this).parent().hide();
			});
			
			for(i in currentSymbolOption.imageNames){
				var currentImage = currentSymbolOption.imageNames[i];
				
				var currentImgSpan = $("<span id='"+currentImage+"'>"+"</span>")
					.css("background-image","url("+currentSymbolOption.imageRoot+currentImage+")")
					.appendTo(pickerRoot);
					
				currentImgSpan.addClass("imageSpan");
					
				currentImgSpan.mouseover(function(){
						$(this).addClass("imageSpan-hover");
					}
				);
				
				currentImgSpan.mouseout(function(){
						$(this).removeClass("imageSpan-hover");
					}
				);
				
				currentImgSpan.click(function(){
					symbolOption[symbolType].currentImage = $(this).attr("id");
					$(this).siblings().removeClass("imageSpan-selected");
					$(this).addClass("imageSpan-selected");
				});
			}		
			
			return symbolRoot;
        }
        
        /*线样式相关div*/
        function getLinePickerRoot(){
        	var symbolType = "line";
        	var symbolRoot = $("<div>"+"</div>");
			symbolRoot.addClass("symbolPicker");
			var currentSymbolOption = symbolOption[symbolType];
				
			var pickerRoot = $("<div>"+"</div>");
			pickerRoot.addClass(currentSymbolOption.pickerClass);
			
			var submitButton = $("<button>确定</button>");
			submitButton.addClass("symbolPicker-submitButton");
			
			pickerRoot.appendTo(symbolRoot);
			submitButton.appendTo(symbolRoot);
			symbolOption[symbolType].root = symbolRoot;
			
			submitButton.click(function(){
				$(this).parent().hide();
			});
			
			var stylePicker = getStylePicker(symbolType);
			var colorPicker = getColorPicker(symbolType);
			var sizePicker = getSizePicker(symbolType);
			
			stylePicker.appendTo(pickerRoot);
			colorPicker.appendTo(pickerRoot);
			sizePicker.appendTo(pickerRoot);
			
			return symbolRoot;
        
        }
        
        /*面样式相关div*/
        function getPolygonPickerRoot(symbolType){
        	var symbolType = "polygon";
        	var symbolRoot = $("<div>"+"</div>");
			symbolRoot.addClass("symbolPicker");
			var currentSymbolOption = symbolOption[symbolType];
				
			var pickerRoot = $("<div>"+"</div>");
			pickerRoot.addClass(currentSymbolOption.pickerClass);
			
			var submitButton = $("<button>确定</button>");
			submitButton.addClass("symbolPicker-submitButton");
			
			pickerRoot.appendTo(symbolRoot);
			submitButton.appendTo(symbolRoot);
			symbolOption[symbolType].root = symbolRoot;
			
			submitButton.click(function(){
				$(this).parent().hide();
			});
			
			var polygonLineStylePicker = getStylePicker("polygonLine");
			var polygonFillStylePicker = getStylePicker("polygonFill");
			
			var polygonLineColorPicker = getColorPicker("polygonLine");
			var polygonFillColorPicker = getColorPicker("polygonFill");
			
			//var polygonLineSizePicker = getSizePicker("polygonLine");
			//var polygonFillSizePicker = getSizePicker("polygonFill");
			
			var sizePicker = getSizePicker("polygon");
			
			polygonLineStylePicker.appendTo(pickerRoot);
			polygonFillStylePicker.appendTo(pickerRoot);
			polygonLineColorPicker.appendTo(pickerRoot);
			polygonFillColorPicker.appendTo(pickerRoot);
			sizePicker.appendTo(pickerRoot);
			
			return symbolRoot;
        }
        
        /*返回样式选择框*/
        function getStylePicker(symbolType){
        	var stylePickerRoot;
        	if(symbolType=="polygonFill"){
        		stylePickerRoot = $("<div>填充样式</div>");
        	}else{
        		stylePickerRoot = $("<div>线条样式</div>");
        	}
        	var stylePickerChooser = $("<select></select>");
        	
        	var symbolStyle = symbolOption["symbolStyle"][symbolType].style;
        	var currentStyleName = symbolOption["symbolStyle"][symbolType].currentStyleName;
        	for(i in symbolStyle){
        		$("<option value='"+symbolStyle[i].value+"'>"+symbolStyle[i].name+"</option>").appendTo(stylePickerChooser);
        	}
        	
        	stylePickerChooser.on("change",function(){
        		symbolOption["symbolStyle"][symbolType].currentStyle=$(this).children("option:selected").val();
        	});
        	
        	stylePickerChooser.appendTo(stylePickerRoot);
        	
        	return stylePickerRoot;
        }
        
        /*返回颜色选择框*/
        function getColorPicker(symbolType){
        	var colorPickerRoot; 
        	
        	if(symbolType=="polygonFill"){
        		colorPickerRoot = $("<div>填充颜色</div>");
        	}else{
        		colorPickerRoot = $("<div>线条颜色</div>");
        	}
        	
			var colorPickerTextbox = $("<input type='textbox' size='7' maxlength='7'></input>");

			var colorPickerDiv = $("<div id='"+symbolType+"colorPicker'></div>");
			
			colorPickerTextbox.val(symbolOption["symbolColor"][symbolType]);
			colorPickerTextbox.on("change",function(){
        		symbolOption["symbolColor"][symbolType]=$(this).val();
        	});
			
			colorPickerRoot.append(colorPickerTextbox);
			return colorPickerRoot;
        }
        
        /*返回宽度选择框*/
        function getSizePicker(symbolType){
        	var sizePickerDiv = $("<div>线条宽度:</div>");
        	var sizePickerTextbox = $("<input type='textbox' size='7' maxlength='3'></input>");

        	sizePickerTextbox.val(symbolOption["symbolSize"][symbolType]);
        	sizePickerTextbox.on("change",function(){
        		symbolOption["symbolSize"][symbolType]=$(this).val();
        	});

        	sizePickerDiv.append(sizePickerTextbox);
        	return sizePickerDiv;
        }
        
        function getSymbolStyle(symbolType){
        	return symbolOption["symbolStyle"][symbolType].currentStyle;
        }
        
        function getSymbolSize(symbolType){
        	return symbolOption["symbolSize"][symbolType];
        }
        
        function getSymbolColor(symbolType){
        	return symbolOption["symbolColor"][symbolType];
        }
	};
	
})(jQuery);
