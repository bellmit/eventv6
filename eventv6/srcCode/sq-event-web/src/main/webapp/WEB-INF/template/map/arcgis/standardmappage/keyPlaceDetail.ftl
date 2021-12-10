<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重点场所管理信息-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="NorMapOpenDiv">	
				<div class="con LyWatch" style="height:165px;overflow:auto;">
		        	<ul>
		                <li>
		                	<p class="FontDarkBlue" style="font-size:14px;width:260px;"><b><#if placeInfo.plaName??>${placeInfo.plaName}</#if>&nbsp;</b></p>
		                    <p>场所类型：<span class="FontDarkBlue"><#if placeInfo.plaTypeLabel??>${placeInfo.plaTypeLabel}</#if>&nbsp;</span></p>
		                    <p>场所面积：<span class="FontDarkBlue"><#if placeInfo.placeArea??>${placeInfo.placeArea?c}&nbsp;平方米</#if>&nbsp;</span></p>
		                    <p>场所地址：<span class="FontDarkBlue"><#if placeInfo.roomAddress??>${placeInfo.roomAddress}</#if>&nbsp;</span></p>
		                	<p>负责人电话：<span class="FontDarkBlue"><#if placeInfo.principalPhone??>${placeInfo.principalPhone}</#if>&nbsp;</span></p>
		                    <p>从业人员数：<span class="FontDarkBlue"><#if placeInfo.staffNum??>${placeInfo.staffNum}</#if>&nbsp;</span></p>
		                </li>
		            </ul>
		            <div class="clear"></div>
		        </div>
	</div>
</body>
</html>