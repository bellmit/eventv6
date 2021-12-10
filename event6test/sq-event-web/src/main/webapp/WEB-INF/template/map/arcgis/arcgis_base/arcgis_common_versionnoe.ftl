
	<script type="text/javascript">
		var uiDomain = "${uiDomain!''}";
        var gmisDomain = "${GMIS_DOMAIN!''}";
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
        var SURVEILLANCE_MSG_METHOD = "<#if SURVEILLANCE_MSG_METHOD??>${SURVEILLANCE_MSG_METHOD}</#if>";

        //网格各层级显示中心点名称的地图层级设置
        var MAP_LEVEL_TRIG_CONDITION_PROVINCE = "<#if MAP_LEVEL_TRIG_CONDITION_PROVINCE??>${MAP_LEVEL_TRIG_CONDITION_PROVINCE}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_CITY = "<#if MAP_LEVEL_TRIG_CONDITION_CITY??>${MAP_LEVEL_TRIG_CONDITION_CITY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_COUNTY = "<#if MAP_LEVEL_TRIG_CONDITION_COUNTY??>${MAP_LEVEL_TRIG_CONDITION_COUNTY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_STREET = "<#if MAP_LEVEL_TRIG_CONDITION_STREET??>${MAP_LEVEL_TRIG_CONDITION_STREET}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_COMMUNITY = "<#if MAP_LEVEL_TRIG_CONDITION_COMMUNITY??>${MAP_LEVEL_TRIG_CONDITION_COMMUNITY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_GRID = "<#if MAP_LEVEL_TRIG_CONDITION_GRID??>${MAP_LEVEL_TRIG_CONDITION_GRID}<#else>0</#if>";
        var MAP_LEVEL_CFG = "<#if MAP_LEVEL_CFG??>${MAP_LEVEL_CFG}</#if>";
        var playerMessenger;
	</script>
	
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/dojo/dijit/themes/claro/claro.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/mapindex.css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
	
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css" />
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-7-2.min.js"></script>
	
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
	<script src="${uiDomain!''}/js/jquery-ui.js" ></script>
	<script src="${uiDomain!''}/js/jquery.ui.draggable.js" ></script>
	<!-- 
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css">
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery-ui.js" ></script>
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/jquery-ui-1.10.4/ui/jquery.ui.draggable.js" ></script>
	<script src="${uiDomain!''}/images/map/gisv0/special_config/js/jquery.mCustomScrollbar.concat.min.js"></script>
	-->
	<script src="${rc.getContextPath()}/js/map/utils/HashMapUtil.js"></script>
	<#if socialOrgCode?? && socialOrgCode == '320903'>
		<script src="${rc.getContextPath()}/js/map/utils/keyPlaceIcon_yd.js"></script>
	<#else>
		<script src="${rc.getContextPath()}/js/map/utils/keyPlaceIcon.js"></script>
	</#if>
	<script src="${rc.getContextPath()}/js/layer/layer.js"></script>
	<#if NEW_HEATMAP??>
			<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap_new.js"></script>
	</#if>
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
	
	<#if NEW_HEATMAP??>
		<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap_arcgis_new.js"></script>
	<#else>
		<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap-arcgis.js"></script>
		<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/heatmap.js"></script>
	</#if>
	
	<script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_versionnoe.js"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function_versionnoe.js"></script>
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/component/msgClient.jhtml"></script>
    <#--<script type="text/javascript" src="${uiDomain!''}/js/gmMessager.js"></script>-->
    <link rel="stylesheet" type="text/css" href="${SQ_ZZGRID_URL}/styles/dhGlobalEyeStyle/css/video.css"/>
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/styles/dhGlobalEyeStyle/js/videoBox.js"></script>