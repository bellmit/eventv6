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
			<input type="hidden" id="id" name="id" value="${(bo.id)!}">
			<input type="hidden" id="isRelease" name="isRelease" value="${(bo.isRelease)!}">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span><font color="red">*</font>热点主题：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.topicName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><font color="red">*</font>分析规则：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:80%">${(bo.rule)!}</span>
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
