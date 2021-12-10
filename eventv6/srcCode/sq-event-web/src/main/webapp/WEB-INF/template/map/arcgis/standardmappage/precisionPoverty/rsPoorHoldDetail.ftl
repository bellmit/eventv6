<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>贫困户关注信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div id="content-d" class="MC_con content light" style="height:450px;">
        <div class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="LeftTd"><label class="LabName"><span>姓名：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.name??>${rsPoorHold.name}</#if></div></td>
					<td><label class="LabName"><span>性别：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.genderCN??>${rsPoorHold.genderCN}</#if></div></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>公民身份号码或残疾证号码：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.identityCard??>${rsPoorHold.identityCard}</#if></div></td>
					<td><label class="LabName"><span>与户主关系：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.holderRelationCN??>${rsPoorHold.holderRelationCN}</#if></div></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>民族：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.ethnicCN??>${rsPoorHold.ethnicCN}</#if></div></td>
					<td><label class="LabName"><span>文化程度：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.educationCN??>${rsPoorHold.educationCN}</#if></div></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>在校生情况：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.school??>${rsPoorHold.school}</#if></div></td>
					<td><label class="LabName"><span>健康情况：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.health??>${rsPoorHold.health}</#if></div></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>劳动能力：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.abilityWork??>${rsPoorHold.abilityWork}</#if></div></td>
					<td><label class="LabName"><span>务工情况：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.ruResumeWork??>${rsPoorHold.ruResumeWork}</#if></div></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>务工时间：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.workDate??>${rsPoorHold.workDate}</#if></div></td>
					<td>
					</td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>参加新型农村合作医疗：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.isMedicalInsurance??><#if (rsPoorHold.isMedicalInsurance=='1')>已参加</#if></#if></div></td>
					<td><label class="LabName"><span>参加城乡居民基本养老保险：</span></label><div class="Check_Radio FontDarkBlue"><#if rsPoorHold.isEndowmentInsurance??><#if (rsPoorHold.isEndowmentInsurance=='1')>已参加</#if></#if></div></td>
				</tr>
			</table>
		</div>
    </div>
</body>
</html>
