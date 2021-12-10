<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body >
	<div class="WgInfo" style="border-bottom:1px solid #c7c7c7;">
    	<dl class="tongji" style="height:65px;">
        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_08.png" /></dt>
            <dd><b><#if gridInfo?? && gridInfo.gridName??>${gridInfo.gridName}</#if></b></dd>
            <dd class="FontCyan"><b><#if statOfGridStr??>${statOfGridStr}</#if></b></dd>
        </dl>
        <div class="clear"></div>
    </div>
    
	<div class="WgInfo">

        <dl>
            <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_18.png" /></dt>
            <dd>网格员</dd>
            <dd class="FontBlue"><b><#if countadmin??>${countadmin}人<#else>0</#if></b></dd>
        </dl>

		<dl>
			<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_16.png" /></dt>
			<dd>办结事件</dd>
			<dd class="FontPurple"><b><#if hadEndEvent??>${hadEndEvent}<#else>0</#if>件</b></dd>
		</dl>

        <dl>
            <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_23.png" /></dt>
            <dd>未办结事件</dd>
            <dd class="FontRed"><b><#if noEndEvent??>${noEndEvent}<#else>0</#if>件</b></dd>
        </dl>

        <dl>
            <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_07.png" /></dt>
            <dd>企业</dd>
            <dd class="FontBlue"><b><#if corNum??>${corNum}<#else>0</#if>家</b></dd>
        </dl>
		<div class="clear"></div>
    </div>
</body>
<script type="text/javascript">
function showGridDetail(title,gridId) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	//var url = sq_zzgrid_url+"/zzgl/map/data/gridBase/gridDetailIndex.jhtml?gridId="+gridId;
	var url = sq_zzgrid_url+"/zzgl/map/data/gridBase/standardGridDetailIndex.jhtml?gridId="+gridId;
	//window.parent.showDetailLigerUI(title,url,850,480);
	window.parent.showMaxJqueryWindow('网格',url,850,400);
}
</script>
</html>
