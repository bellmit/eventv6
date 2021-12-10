<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>联动单位ID：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkageUnitId)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联络员：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkMan)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联络员联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkManTel)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>分管领导：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.leaderName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>分管领导联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.leaderTel)!}</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
