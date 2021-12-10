<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>校园周边概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:173px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;">
			               <b style="cursor:default" title="<#if eventInfo.reName?exists>${eventInfo.reName}</#if>">
			               	<div class="FontDarkBlue"  style="width:100%;font-size:14px;">
								<#if eventInfo.reName?exists><#if eventInfo.reName?length gt 21 >${eventInfo.reName[0..21]}...<#else>${eventInfo.reName}</#if></#if>
							</div>
			               </b>
		               </p>
		               <p>所在地段：
		               	<#if eventInfo.bizName??>
		               	<span class="FontDarkBlue" title="${eventInfo.bizName}">
		               		<#if eventInfo.bizName?exists><#if eventInfo.bizName?length gt 21 >${eventInfo.bizName[0..21]}...<#else>${eventInfo.bizName}</#if></#if>
		               	</span>
		               	</#if>
		               	</p>
		               <p>发生地址：
		               	<#if eventInfo.occuAddr??>
			               	<span class="FontDarkBlue" title="${eventInfo.occuAddr}">
			               		<#if eventInfo.occuAddr?length gt 21 >
			               			${eventInfo.occuAddr[0..21]}...
			               		<#else>
			               			${eventInfo.occuAddr}
			               		</#if>
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>发生日期：
		               	<#if eventInfo.occuDateStr??>
			               	<span class="FontDarkBlue" title="${eventInfo.occuDateStr}">
			               		${eventInfo.occuDateStr}
			               	</span>
		               	<#else>
		               		<span class="FontDarkBlue">
			               		未知
			               	</span>
		               	</#if>&nbsp;
		               </p>
		               <p>是否破案：
		               	<#if eventInfo.isDetectionName??>
			               	<span class="FontDarkBlue" title="${eventInfo.isDetectionName}">
			               		${eventInfo.isDetectionName}
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

</html>
