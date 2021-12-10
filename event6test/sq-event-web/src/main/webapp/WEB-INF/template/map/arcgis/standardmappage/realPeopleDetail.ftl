<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body >
	<div class="WgInfo" style="border-bottom:1px solid #c7c7c7;">
    	<dl class="tongji" style="height:45px;">
        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_08.png" /></dt>
            <dd><b><a href="javascript:void(0);">${gridInfo.gridName}</a></b></dd>
        </dl>
        <div class="clear"></div>
    </div>
    
	<div class="WgInfo">
	<#list list as l>
		<dl>
        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_18.png" /></dt>
            <dd>${l.TEXT_}</dd>
            <dd class="FontBlue"><b><#if l.TOTAL_??>${l.TOTAL_}<#else>0</#if>人</b></dd>
        </dl>
	</#list>
        <div class="clear"></div>
    </div>
</body>
</html>
