
	<script type="text/javascript">
		var uiDomain = "${uiDomain!''}";
		var js_ctx =  "${rc.getContextPath()}";
		var _myServer ="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
		_myServer = _myServer.replace("http://","");
		var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
		_myServer_compact = _myServer_compact.replace("http://","");
		
		var AUTOMATIC_CLEAR_MAP_LAYER = "<#if AUTOMATIC_CLEAR_MAP_LAYER??>${AUTOMATIC_CLEAR_MAP_LAYER}</#if>";
		var IS_IMAGE_MAP_SHOW_CONTOUR = "<#if IS_IMAGE_MAP_SHOW_CONTOUR??>${IS_IMAGE_MAP_SHOW_CONTOUR}</#if>";
		var IS_VECTOR_MAP_SHOW_CONTOUR = "<#if IS_VECTOR_MAP_SHOW_CONTOUR??>${IS_VECTOR_MAP_SHOW_CONTOUR}</#if>";
		var IS_GRID_ARCGIS_SHOW_CENTER_POINT = "<#if IS_GRID_ARCGIS_SHOW_CENTER_POINT??>${IS_GRID_ARCGIS_SHOW_CENTER_POINT}</#if>";
		var OUTLINE_FONT_SETTINGS = "<#if OUTLINE_FONT_SETTINGS??>${OUTLINE_FONT_SETTINGS}</#if>";
		var LC_INFO_ORG_CODE = "<#if LC_INFO_ORG_CODE??>${LC_INFO_ORG_CODE}</#if>";
		var OUTLINE_FONT_SETTINGS_BUILD = "<#if OUTLINE_FONT_SETTINGS_BUILD??>${OUTLINE_FONT_SETTINGS_BUILD}</#if>";
		var IS_ACCUMULATION_LAYER = "<#if LC_INFO_ORG_CODE??>${IS_ACCUMULATION_LAYER}</#if>";
		var ARCGIS_DOCK_MODE = "<#if ARCGIS_DOCK_MODE??>${ARCGIS_DOCK_MODE}</#if>";
		var arcgisFactorUrl = "<#if arcgisFactorUrl??>${arcgisFactorUrl}</#if>";
		var CENTER_POINT_SETTINGS_BUILD = "<#if CENTER_POINT_SETTINGS_BUILD??>${CENTER_POINT_SETTINGS_BUILD}</#if>";
	</script>
	
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/mapindex.css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/icon.css">
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<!-- 
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css">
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	<script src="${uiDomain!''}/images/map/gisv0/special_config/js/jquery.mCustomScrollbar.concat.min.js"></script>
	-->
	<script src="${rc.getContextPath()}/js/map/utils/HashMapUtil.js"></script>
	<script src="${rc.getContextPath()}/js/map/utils/keyPlaceIcon.js"></script>
	<script src="${rc.getContextPath()}/js/layer/layer.js"></script>
	
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_versionnoe.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function_versionnoe.js"></script>
	
	<style>
		.arrowbox{cursor:pointer; background:#6197D3; text-align:center; color: white; position:absolute;z-index:2; width:33px; height:18px;}
		.arrowico{    line-height: 0;
		    border: 9px solid transparent;
		    border-right: 10px solid #6197D3;
		    height: 0;
		    overflow: hidden;
		    position: absolute;
		    left: -19px;
		    top: 0px;}
	</style>