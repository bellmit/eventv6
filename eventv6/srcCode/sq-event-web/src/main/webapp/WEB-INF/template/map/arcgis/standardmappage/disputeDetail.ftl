<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详情</title>
<link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
	.UnitInfo ul li{padding-right:3px;width:370px;}
	body{overflow-y: hidden!important;}
</style>

</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body>

<div id="content-d" class="MC_con content light UnitInfo">
	<ul>
		<li>事件名称：<span><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></span>
			<#if disputeMediation.involveType??>
				<#if disputeMediation.involveType=='TAIWANG'>&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/involvedTaiWan.png"></#if>
			</#if>
		</li>
		<li>发生日期：<span><#if disputeMediation.happenTimeStr??>${disputeMediation.happenTimeStr}</#if></span></li>
		<li>涉及人数：<span><#if disputeMediation.involveNum??>${disputeMediation.involveNum}（人）&nbsp;</#if></span></li>
		<li>发生地点：<span title="${disputeMediation.happenAddr!''}"><#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if></span></li>
		<li>事件简述：<span title="${disputeMediation.disputeCondition!''}"><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></span></li>
	</ul>
	<div class="clear"></div>
</div>
<script type="text/javascript">
    $(window).load(function() {
        var options = {
            axis : "yx",
            theme : "minimal-dark"
        };
        enableScrollBar('content-d',options);
    });
</script>
</body>
</html>