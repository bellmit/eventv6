<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv=X-UA-Compatible content="IE=edge">
	<title>平潭合体地图</title>
	<#include "/component/commonFiles-1.1.ftl" />
</head>
<body style="overflow:hidden;">
	<iframe id="iframe1" style="border:0px;" width="100%" height="100%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=SPGIS_BUSI_HOME<#if menuCode??>&menuCode=${menuCode}</#if><#if fillOpacity??>&fillOpacity=${fillOpacity}</#if>"></iframe>
	<iframe id="iframe2" style="border:0px;display:none;" width="100%" height="100%" src="${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=MAPABC_STANDARD_HOME<#if fillOpacity??>&fillOpacity=${fillOpacity}</#if>"></iframe>
</body>
<script>
	$(document).ready(function() {
		$("#btn").click(function() {
			$("#iframe1").toggle();
			$("#iframe2").toggle();
		});
	});

	function cut() {
		$("#iframe1").toggle();
		$("#iframe2").toggle();
	}
	
	$("#iframe1").height($(document).height());
	$("#iframe2").height($(document).height());
</script>
</html>
