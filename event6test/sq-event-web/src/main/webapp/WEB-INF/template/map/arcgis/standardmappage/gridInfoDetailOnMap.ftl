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
            <dd><b><a href="javascript:void(0);" onclick="showGridDetail('${gridInfo.gridName}','${gridInfo.gridId}')">${gridInfo.gridName}</a></b></dd>
            <dd class="FontCyan"><b><#if statOfGridStr??>${statOfGridStr}</#if></b></dd>
        </dl>
        <div class="clear"></div>
    </div>
    
	<div class="WgInfo">
		<#if jj??>
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_17.png" /></dt>
	            <dd>常住</dd>
	            <dd class="FontPurple"><b><#if czCount??>${czCount}<#else>0</#if>人</b></dd>
	        </dl>
	        
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_24.png" /></dt>
	            <dd>户籍</dd>
	            <dd class="FontPurple"><b><#if hjCount??>${hjCount}<#else>0</#if>人</b></dd>
	        </dl>
	        
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_25.png" /></dt>
	            <dd>辖区外常住</dd>
	            <dd class="FontPurple"><b><#if ppCount??>${ppCount}<#else>0</#if>人</b></dd>
	        </dl>
		
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_18.png" /></dt>
	            <dd>流动</dd>
	            <dd class="FontBlue"><b><#if floatCount??>${floatCount}<#else>0</#if>人</b></dd>
	        </dl>
			
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_11.png" /></dt>
	            <dd>网格员</dd>
	            <dd class="FontRed"><b><#if countadmin??>${countadmin}人<#else>0</#if></b></dd>
	        </dl>
	        <div class="clear"></div>
		<#elseif nc??>
            <dl>
                <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_24.png" /></dt>
                <dd>户籍人口</dd>
                <dd class="FontPurple"><b><#if hjCount??>${hjCount}<#else>0</#if>人</b></dd>
            </dl>

            <dl>
                <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_18.png" /></dt>
                <dd>流动人口</dd>
                <dd class="FontBlue"><b><#if floatCount??>${floatCount}<#else>0</#if>人</b></dd>
            </dl>

            <dl>
                <dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_11.png" /></dt>
                <dd>网格员</dd>
                <dd class="FontRed"><b><#if countadmin??>${countadmin}人<#else>0</#if></b></dd>
            </dl>
            <div class="clear"></div>
        <#else>
        	<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_17.png" /></dt>
	            <dd>常住人口</dd>
	            <dd class="FontPurple"><b><#if ppCount??>${ppCount}<#else>0</#if>人</b></dd>
	        </dl>
		
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_18.png" /></dt>
	            <dd>流动人口</dd>
	            <dd class="FontBlue"><b><#if floatCount??>${floatCount}<#else>0</#if>人</b></dd>
	        </dl>
			
			<dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_11.png" /></dt>
	            <dd>网格力量</dd>
	            <dd class="FontRed"><b><#if countadmin??>${countadmin}人<#else>0</#if></b></dd>
	        </dl>
			<!-- <dl>
	        	<dt><img src="${uiDomain!''}/images/map/gisv0/special_config/images/earth_11.png" /></dt>
	            <dd>矛盾纠纷</dd>
	            <dd class="FontRed"><b style="cursor:pointer" onclick="showDisputeBar('${gridInfo.gridId}')"><#if countadmin??>${countDispute}人<#else>0</#if></b></dd>
	        </dl> -->
	        <div class="clear"></div>
        </#if>
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

function showDisputeBar(gridId) {
	var url = "${rc.getContextPath()}/zhsq/map/gisstat/gisStat/getDisputeBar.jhtml?infoOrgCode="+gridId;
	window.parent.showMaxJqueryWindow('网格',url,null,null);
}
</script>
</html>
