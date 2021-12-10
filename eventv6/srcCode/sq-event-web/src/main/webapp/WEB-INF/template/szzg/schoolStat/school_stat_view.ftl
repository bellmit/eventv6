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
						<label class="LabName"><span><font style="color:red">*</font>学校名称：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.schoolName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color:red">*</font>老师人数：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.teachers)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color:red">*</font>学生人数：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.students)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color:red">*</font>男生人数：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.males)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color:red">*</font>女生人数：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.females)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color:red">*</font>统计年份：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.statYear)!}</span>
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
