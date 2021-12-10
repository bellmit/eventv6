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
                <b style="cursor:default" title="<#if bo.name?exists>${bo.name}</#if>">
                    <a class="FontDarkBlue"  style="font-size:14px;" href="javascrpit:void(0);">
					<#if bo.name?exists><#if bo.name?length gt 18 >${bo.name[0..18]}...<#else>${bo.name}</#if></#if>&nbsp;
                    </a>
                </b>
            </p>
            <p>阵地名称 ：
			<#if bo.name??>
                <span class="FontDarkBlue">${bo.name}</span>
			</#if>&nbsp;
            </p>
            <p>阵地类型：
			<#if bo.typeCN??>
                <span class="FontDarkBlue">${bo.typeCN}</span>
			</#if>&nbsp;
            </p>
            <p>负责人：
			<#if bo.personInCharge??>
                <span class="FontDarkBlue">${bo.personInCharge}</span>
			</#if>&nbsp;
            </p>
            <p>联系电话：
			<#if bo.phone??>
                <span class="FontDarkBlue">${bo.phone}</span>
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
