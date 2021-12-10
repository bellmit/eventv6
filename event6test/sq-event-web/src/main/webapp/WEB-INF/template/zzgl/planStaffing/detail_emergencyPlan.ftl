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
				    <td class="LeftTd">
							<label class="LabName"><span><font style="color: red">*</font>所属区域：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.regionName)!}</span>
					</td>
					<td class="LeftTd">
						<label class="LabName"><span><font style="color: red">*</font>预案类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.planTypeName)!}</span>
					</td>
				</tr>
				<tr >
					<td colspan="2">
						<label class="LabName"><span>预案内容：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 80%">${(bo.planContent)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>备注：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 80%">${(bo.remark)!}</span>
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
