<script type="text/javascript">
	var uiDomain = "${uiDomain!''}";
    var gmisDomain = "${GMIS_DOMAIN!''}";
	var js_ctx =  "${rc.getContextPath()}";
	var _myServer ="${uiDomain!''}/js/map/arcgis/4.12";
	_myServer = _myServer.replace("http://","");
</script>
<script src="${uiDomain!''}/js/layer/layer.js"></script>

<!-- 引入第三方threejs3D模型库 -->
<script src="${uiDomain!''}/js/map/threejs/build/three.min.js"></script>
<script src="${rc.getContextPath()}/js/map/utils/HashMapUtil.js"></script>
<link rel="stylesheet" href="${uiDomain!''}/js/map/arcgis/4.12/esri/themes/light/main.css" />
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_3d/heatmap.js"></script>


<script src="${uiDomain!''}/js/map/arcgis/4.12/init.js"></script>

<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_3d/ffcsMap3D.js"></script>

<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_3d/arcgis3d_versionnoe.js"></script>
<!-- 
<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_3d/heatmap_arcgis3D_new.js"></script>
 -->




















