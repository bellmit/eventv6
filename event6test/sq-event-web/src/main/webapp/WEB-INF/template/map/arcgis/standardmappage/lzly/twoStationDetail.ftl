<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
<div class="con LyWatch" style="height:154px;overflow:auto;">
    <ul>
        <li>
            <p class="FontDarkBlue" style="font-size:14px;width:auto">
                <b style="cursor:default" title="<#if twoStation.stationName?exists>${twoStation.stationName}</#if>">
                    <a class="FontDarkBlue"  style="font-size:14px;" href="javascrpit:void(0);">
					<#if twoStation.stationName?exists><#if twoStation.stationName?length gt 21 >${twoStation.stationName[0..21]}...<#else>${twoStation.stationName}</#if></#if>&nbsp;
                    </a>
                </b>
            </p>
            <p>行政区划：
			<#if twoStation.stationName??>
                <span class="FontDarkBlue">${twoStation.stationName}</span>
			</#if>&nbsp;
            </p>
            <p>两站类别：
			<#if twoStation.stationTypeCN??>
                <span class="FontDarkBlue">${twoStation.stationTypeCN}</span>
			</#if>&nbsp;
            </p>
            <p>单位编码：
			<#if twoStation.stationCode??>
                <span class="FontDarkBlue">${twoStation.stationCode}</span>
			</#if>&nbsp;
            </p>
            <p>建设时间：
			<#if twoStation.buildingDateCN??>
                <span class="FontDarkBlue">${twoStation.buildingDateCN}</span>
			</#if>&nbsp;
            </p>
        </li>
    </ul>
    <div class="clear"></div>

</div>
</body>
<script type="text/javascript">

</script>

</html>
