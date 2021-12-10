<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="labelId" name="labelId" value="${(bo.labelId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>标签名称：</span></label>
							<div class="Check_Radio FontDarkBlue"><#if eventLabel.labelName??>${eventLabel.labelName}</#if></div>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>所属模块：</span></label>
							<div class="Check_Radio FontDarkBlue"><#if eventLabel.labelModelName??>${eventLabel.labelModelName}</#if></div>
						</td>
					</tr>
					
					<tr>
						<td>
							<label class="LabName"><span>所属地域：</span></label>
							<div class="Check_Radio FontDarkBlue"><#if eventLabel.gridPath??>${eventLabel.gridPath}</#if></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</body>

</html>
