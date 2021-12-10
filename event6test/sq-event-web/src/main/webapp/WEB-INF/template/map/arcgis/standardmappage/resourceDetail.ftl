<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="con LyWatch" style="height:144px;overflow:auto;">
	      <ul>
	          <li>
	               <p class="FontDarkBlue" style="font-size:14px;width:290px;"><b style="cursor:default" title="<#if resInfo.resName?exists>${resInfo.resName}</#if>"><#if resInfo.resName?exists><#if resInfo.resName?length gt 21 >${resInfo.resName[0..21]}...<#else>${resInfo.resName}</#if></#if>&nbsp;</b></p>
	               <p>所属网格：
	               	<#if gridNames??>
	               	<span class="FontDarkBlue">
	               		${gridNames}
	               	</span>
	               	</#if>
	               	</p>
	               	<#if resInfo.resType.name??>
		               	<#if resInfo.resType.name=="消防栓">
			               <p>所属楼宇：
			               	<#if resInfo.buildingName??>
				               	<span class="FontDarkBlue">
				               		${resInfo.buildingName}
				               	</span>
			               	<#else>
			               		<span class="FontDarkBlue">
				               		未知
				               	</span>
			               	</#if>
			               </p>
		               </#if>
	               </#if>
	               <p>备　　注： <span class="FontDarkBlue"><#if resInfo.remark?exists>${resInfo.remark}<#else>暂无</#if></span></p>
	          </li>
	      </ul>
	      <div class="clear"></div>
	</div>			
</body>
</html>
