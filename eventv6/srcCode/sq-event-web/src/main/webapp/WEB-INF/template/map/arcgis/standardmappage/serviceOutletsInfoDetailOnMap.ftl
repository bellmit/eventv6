<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>便民服务网点概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
		<div class="con LyWatch" style="height:140px;overflow:auto;">
		      <ul>
		          <li>
		               <p class="FontDarkBlue" style="font-size:14px;"><b style="cursor:default" title="<#if outlets.name?exists>${outlets.name!''}</#if>"><#if outlets.name?exists><#if outlets.name?length gt 21 >${outlets.name[0..21]}...<#else>${outlets.name}</#if></#if>&nbsp;</b></p>
		               <p>网点地址：<span class="FontDarkBlue">${outlets.placeAddr!''}</span></p>
		               <p>网点类别：<span class="FontDarkBlue">${outlets.typeName!''}</span></p>
		               <p>管理员：<span class="FontDarkBlue">${outlets.manager!''}</span></p>
		               <p>联系电话：<span class="FontDarkBlue">${outlets.telephone!''}</span></p>
		          </li>
		      </ul>
		      <div class="clear"></div>
		</div>			
</body>
</html>
