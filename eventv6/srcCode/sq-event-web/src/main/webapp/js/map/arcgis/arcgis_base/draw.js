var map;
var toolbar;
var tool;

/*
 * 创建Map，默认创建的是世界地图
 * mapId : id为mapId参数的div标签
 */
function Map(mapId) {
	map = new esri.Map(mapId, {
		basemap: "streets",
	});
	
	map.on("load", createToolbar);
}

/*
 * 绘图方法
 * 
 * shape参数可选值
 * point：点
 * multi_point：多个点
 * line：线段
 * polyline：连续线段
 * freehand_polyline：曲线
 * triangle：三角形
 * extent：长方形
 * circle：圆
 * ellipse：椭圆
 * polygon：多边形
 * freehand_polygo：不规则封闭图形
 */
function draw(shape) {
	tool = shape.toUpperCase();
	
	toolbar.activate(esri.toolbars.Draw[tool]);
}

// 创建绘图工具
function createToolbar() {
	toolbar = new esri.toolbars.Draw(map);
	toolbar.on("draw-end", addToMap);
}

/*
 * 将所绘图形添加到图层
 * event:绘图事件
 */
function addToMap(event) {
	var symbol;
	
	toolbar.deactivate();
	
	switch (event.geometry.type) {
	case "point":
    case "multipoint":
		symbol = new esri.symbol.SimpleMarkerSymbol();
		break;
	case "polyline":
		symbol = new esri.symbol.SimpleLineSymbol();
		break;
	default:
		symbol = new esri.symbol.SimpleFillSymbol();
		break;
	}

	var graphic = new esri.Graphic(event.geometry, symbol);
	map.graphics.add(graphic);
}
