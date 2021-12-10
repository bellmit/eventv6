<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>gis</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<body style="background-color: #fff;overflow-y:auto;overflow-x:hidden;">
	<div class="NorMapOpenDiv">
	        <div class="con">
				<div class="UnitInfo" style="border-bottom:none;">
					<ul>
						<p class="FontDarkBlue" style="width:100%;font-size:14px;margin-bottom: 5px;margin-top: 10px;"><b style="cursor:default" title="<#if ciRsParty.name??>${ciRsParty.name}</#if>"><#if ciRsParty.name??>${ciRsParty.name}</#if>&nbsp;</b></p>
						<li style="width:100%;">隶属党组织：<span>${ciRsParty.partyGroupName!''}</span></li>
						<li style="width:100%;">公民身份号码：<span>${ciRsParty.identityCard!''}</span></li>
						<li>性别：<span>${ciRsParty.genderCN!''}</span></li>
						<li>联系电话：<span>${ciRsParty.residentMobile!''}</span></li>
						<li>党员类型：<span>${ciRsParty.partyMemberTypeCN!''}</span></li>
						<li>入伍时间：<span><#if ciRsParty.partyArmDate??>${ciRsParty.partyArmDate?date}</#if></span></li>
						<li>入党时间：<span><#if ciRsParty.partyJoinDate??>${ciRsParty.partyJoinDate?date}</#if></span></span></li>
					</ul>
					<div class="clear"></div>
				</div>
	        </div>
	</div>
</body>
<script type="text/javascript">


</script>
</html>
