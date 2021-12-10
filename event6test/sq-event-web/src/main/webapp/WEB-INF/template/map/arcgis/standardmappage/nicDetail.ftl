<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>各视联网信息中心-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="NorMapOpenDiv" style="width:100%;">	
		<div class="con LyWatch" style="height:165px;overflow:auto;">
        	<ul>
                <li>
                	<p class="FontDarkBlue" style="font-size:14px;width:100%;word-break: break-all;"><b>${nicInfo.name!}&nbsp;</b></p>
                    <p>地址：<span class="FontDarkBlue">${nicInfo.addr_!}&nbsp;</span></p>
                    <p>
                    	中心联系方式：<span class="FontDarkBlue">${nicInfo.centerContactTel!}&nbsp;</span>
                    	层级：<span class="FontDarkBlue">${nicInfo.levelName!}&nbsp;</span>
                    </p>
                    <p>所属网格：<span class="FontDarkBlue">${gridNames!''}&nbsp;</span></p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
	</div>
</body>
</html>