<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>各视联网信息中心-概要信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.spanWordBreak{word-break: break-all;}
	.NorToolBtn {
		display:block;
		float:left;
		margin-right:10px;
		padding:4px 7px 4px 7px;
		color:#fff;
		line-height:14px;
		background-repeat:no-repeat;
		background-color:#2980B9;
		background-position:7px 5px;
		transition:all 0.2s;
		border-radius:3px;
	}
	.NorToolBtn:hover {
		color:#fff;
		text-decoration:none;
		background-color:#3498DB;
	}
	body, html {overflow:auto;}
</style>

</head>
<body>
	<div class="NorMapOpenDiv">	
		<div class="con LyWatch" style="overflow:auto;">
        	<ul>
                <li>
                	<p class="FontDarkBlue spanWordBreak" style="font-size:14px;width:100%;">${urbanObj.name!''}<b>&nbsp;</b></p>
                	<p>
                    	部件类型：<span class="FontDarkBlue spanWordBreak">${urbanObj.classCodeLb!''}&nbsp;</span>
                    </p>
                    <p>主管部门名称：<span class="FontDarkBlue spanWordBreak">${urbanObj.mainDeptName!''}&nbsp;</span></p>
                    <p>部件状态：<span class="FontDarkBlue spanWordBreak">${urbanObj.objStatusLb!''}&nbsp;</span></p>
                    <p>养护单位名称：<span class="FontDarkBlue spanWordBreak">${urbanObj.cureDeptName!''}&nbsp;</span></p>
                    <p>位置：<span class="FontDarkBlue spanWordBreak">${urbanObj.position!''}&nbsp;</span></p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
	</div>
</body>
</html>
