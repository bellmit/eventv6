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
						<p class="FontDarkBlue" style="width:100%;font-size:14px;margin-bottom: 5px;margin-top: 10px;"><b style="cursor:default" title="<#if ciPartyGroup.partyGroupName?exists>${ciPartyGroup.partyGroupName}</#if>"><#if ciPartyGroup.partyGroupName?exists><#if ciPartyGroup.partyGroupName?length gt 21 >${ciPartyGroup.partyGroupName[0..20]}...<#else>${ciPartyGroup.partyGroupName}</#if></#if>&nbsp;</b></p>
						<li>书记姓名：<span>${ciPartyGroup.secretaryName!''}</span></li>
						<li>联系电话：<span>${ciPartyGroup.telephone!''}</span></li>
						<li>支部类型：
							<span>
								<#if ciPartyGroup.groupType??>
									<#if ciPartyGroup.groupType=="0">
										党工委
									<#elseif ciPartyGroup.groupType=="1">
										机关支部
									<#elseif ciPartyGroup.groupType=="2">
										村（居）党组织
									<#elseif ciPartyGroup.groupType=="3">
										网格党支部
									<#elseif ciPartyGroup.groupType=="4">
										其他类型的非公党支部
									</#if>
								</#if>
							</span>
						</li>
						<#if !showPartyMember??>
						<li>党&nbsp;员&nbsp;数：<span><a href="###" onclick="showPartyDetail(${ciPartyGroup.partyGroupId?c})">${ciPartyGroup.memberNum!''}</a>&nbsp;人</span></li>
						</#if>
						<li style="width: 276px;">所在楼宇：<span><#if buildingAddress??>${buildingAddress!''}<#else>未知</#if></span></li>
					</ul>
					<div class="clear"></div>
				</div>
	        </div>
	</div>
</body>
<script type="text/javascript">

function showPartyDetail(partyGroupId){
	var orgCode = '${ciPartyGroup.orgCode!''}';
	var memberNum = '${ciPartyGroup.memberNum!''}';
	if(memberNum != '0'){
		window.parent.showPartyDetail(partyGroupId,orgCode, "详细信息");
	}
}

</script>
</html>
