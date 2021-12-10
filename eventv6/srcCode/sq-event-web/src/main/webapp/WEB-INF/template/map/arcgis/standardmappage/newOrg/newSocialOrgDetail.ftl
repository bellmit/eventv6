<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新社会组织信息-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="overflow-y:auto;overflow-x:hidden;">
	<div class="NorMapOpenDiv">	
				<div class="con LyWatch">
		        	<ul>
		                <li>
		                	<p class="FontDarkBlue" style="font-size:14px;"><b><#if record.orgName??>${record.orgName}</#if></b></p>
		                    <p>所属网格：<span class="FontDarkBlue"><#if record.gridName??>${record.gridName}</#if></span></p>
		                    <p>组织类别：<span class="FontDarkBlue"><#if record.typeName??>${record.typeName}</#if></span></p>
		                    <p>办公地址：<span class="FontDarkBlue">
		                    <#if record.registeredAddr??><#if record.registeredAddr?length gt 18 >${record.registeredAddr[0..18]}...<#else>${record.registeredAddr}</#if></#if></span></p>
		                	<p>主要职责：<span class="FontDarkBlue">
		                	<#if record.aim??><#if record.aim?length gt 18 >${record.aim[0..18]}...<#else>${record.aim}</#if></#if></span></p>
		                </li>
		            </ul>
		            <div class="clear"></div>
		        </div>
	</div>
</body>
</html>