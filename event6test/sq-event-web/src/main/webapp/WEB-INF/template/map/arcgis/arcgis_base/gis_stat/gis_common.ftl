
	<script type="text/javascript">
		var uiDomain = "${uiDomain!''}";
		var js_ctx =  "${rc.getContextPath()}";
		var _myServer ="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
		_myServer = _myServer.replace("http://","");
		var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
		_myServer_compact = _myServer_compact.replace("http://","");
	</script>
	
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/mapindex.css" />
	<link href="${SQ_ZHSQ_EVENT_URL}/theme/arcgis/gisstat/tjstyle.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/icon.css">
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/init.js"></script>
	
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsFeature.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMap.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMeasure.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSlider.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsOverviewMap.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsLayerPicker.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsFillQuery.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsTianDiTuLayer.js"></script> 
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap-arcgis.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap.js"></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/gis_stat/gis.js"></script>