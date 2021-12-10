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
                <b style="cursor:default" title="<#if importantUnit.iuName?exists>${importantUnit.iuName}</#if>">
                    <a class="FontDarkBlue"  style="font-size:14px;" href="javascrpit:void(0);">
					<#if importantUnit.iuName?exists><#if importantUnit.iuName?length gt 21 >${importantUnit.iuName[0..21]}...<#else>${importantUnit.iuName}</#if></#if>
                    </a>
                </b>
            </p>
            <p>行政区划：
			<#if importantUnit.infoOrgFullName??>
                <span class="FontDarkBlue" style="width:220px;">${importantUnit.infoOrgFullName}</span>
			</#if>
            </p>
            <p>类型：
			<#if importantUnit.iuTypeCN??>
                <span class="FontDarkBlue">${importantUnit.iuTypeCN}</span>
			</#if>
            </p>
            <p>电话：
			<#if importantUnit.iuTel??>
                <span class="FontDarkBlue">${importantUnit.iuTel}</span>
			</#if>
            </p>
        </li>
    </ul>
    <div class="clear"></div>

</div>
</body>
<script type="text/javascript">

</script>

</html>
