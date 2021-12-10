<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新经济组织信息-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="overflow-y:auto;overflow-x:hidden;">
	<div class="NorMapOpenDiv">	
				<div class="con LyWatch">
		        	<ul>
		                <li>
		                	<p class="FontDarkBlue" style="font-size:14px;width:300px;">
		                	<b>
		                		<#if record.corName??>
		                			<#if record.corName?length gt 18 >${record.corName[0..18]}...<#else>${record.corName}</#if>
		                		</#if>
		                		<#if record.involveType??>
		                			<#if record.involveType=='TAIWANG'>&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/involvedTaiWan.png"></#if>
		                		</#if>
		                	</b>
		                	</p>
		                    <p>所属网格：<span class="FontDarkBlue">${gridNames!''}</span></p>
		                    <p>企业类别：<span class="FontDarkBlue"><#if record.corTypeName??>${record.corTypeName}</#if></span></p>
		                    <p>企业地址：<span class="FontDarkBlue">
		                    <#if record.corAddr??><#if record.corAddr?length gt 18 >${record.corAddr[0..18]}...<#else>${record.corAddr}</#if></#if></span></p>
		                	<p>企业联系方式：<span class="FontDarkBlue"><#if record.telephone??>${record.telephone}</#if></span></p>
		                </li>
		            </ul>
		            <div class="clear"></div>
		        </div>
	</div>
</body>
</html>