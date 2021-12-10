<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>校园周边概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:240px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;width:auto">
			               <b style="cursor:default" title="<#if placeInfo.plaName?exists>${placeInfo.plaName}</#if>">
			               	<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showCampusDetail('<#if placeInfo.plaName?exists>${placeInfo.plaName}</#if>','<#if placeInfo.plaId?exists>${placeInfo.plaId}</#if>')">
								<#if placeInfo.plaName?exists><#if placeInfo.plaName?length gt 21 >${placeInfo.plaName[0..21]}...<#else>${placeInfo.plaName}</#if></#if>&nbsp;
							</a>
			               </b>
		               </p>
		               <p>所属网格：
		               	<#if placeInfo.gridName??>
		               	<span class="FontDarkBlue" title="${placeInfo.gridName}">
		               		${placeInfo.gridName}
		               	</span>
		               	</#if>&nbsp;
		               	</p>
		               <p>地址：
		               	<#if placeInfo.roomAddress??>
			               	<span class="FontDarkBlue" title="${placeInfo.roomAddress}">
			               		<#if placeInfo.roomAddress?length gt 21 >
			               			${placeInfo.roomAddress[0..21]}...
			               		<#else>
			               			${placeInfo.roomAddress}
			               		</#if>
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>学校类型：
		               	<#if placeInfo.campusTypeStr??>
			               	<span class="FontDarkBlue" title="${placeInfo.campusTypeStr}">
			               		${placeInfo.campusTypeStr}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>主要负责人：
		               	<#if placeInfo.principal??>
			               	<span class="FontDarkBlue" title="${placeInfo.principal}">
			               		${placeInfo.principal}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>联系方式：
		               	<#if placeInfo.principalPhone??>
			               	<span class="FontDarkBlue" title="${placeInfo.principalPhone}">
			               		${placeInfo.principalPhone}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>办主任：
		               	<#if placeInfo.policeName??>
			               	<span class="FontDarkBlue" title="${placeInfo.policeName}">
			               		${placeInfo.policeName}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>联系方式：
		               	<#if placeInfo.policeTel??>
			               	<span class="FontDarkBlue" title="${placeInfo.policeTel}">
			               		${placeInfo.policeTel}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>保安人数：
		               	<#if placeInfo.staffNum??>
			               	<span class="FontDarkBlue" title="${placeInfo.staffNum}">
			               		${placeInfo.staffNum}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		          </li>
		      </ul>
		      <div class="clear"></div>
		      
		</div>			
</body>
<script type="text/javascript">

function showCampusDetail(title,id) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = sq_zzgrid_url+"/zzgl/grid/campus/detail.jhtml?plaId="+id;
	window.parent.showMaxJqueryWindow(title,url,948,405);	
}
</script>

</html>
