<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>隐患路段、隐患路口-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="NorMapOpenDiv" style="width:100%;">	
		<div class="con LyWatch" style="height:165px;overflow:auto;">
        	<ul>
                <li>
                	<p class="FontDarkBlue" style="font-size:14px;width:100%;word-break: break-all;"><b>${dangRoad.drName!}&nbsp;</b></p>
                    <p>
                    	行政区划：
                    	<span class="FontDarkBlue">
                    		<#if dangRoad.infoOrgFullName??>
                    			${dangRoad.infoOrgFullName}
                    		<#else>
                    			${dangRoad.infoOrgName!}
                    		</#if>
                    		&nbsp;
                    	</span>
                    </p>
                    <p>
                    	<#if dangRoad.drType?? && dangRoad.drType=='1'>
                    	连接道路：
                    	<#elseif dangRoad.drType?? && dangRoad.drType=='2'>
                    	所在道路：
                    	</#if>
                    	<span class="FontDarkBlue">${dangRoad.roadName!}&nbsp;</span>
                    </p>
                    <p>
                    	采集人员：
                    	<span class="FontDarkBlue">${dangRoad.collPerson!''}&nbsp;</span>
                    </p>
                    <p>
                    	联系方式：
                    	<span class="FontDarkBlue">${dangRoad.collTel!}&nbsp;</span>
                    </p>
                    <p>
                    	隐患情况描述：
                    	<span class="FontDarkBlue">${dangRoad.drDesc!}&nbsp;</span>
                    </p>
                    <p>
                    	备注：
                    	<span class="FontDarkBlue">${dangRoad.remark!}&nbsp;</span>
                    </p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
	</div>
</body>
</html>