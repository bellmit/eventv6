<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	
	<style type="text/css">
		.labelTd{width:10%;}
	</style>
	
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
				<tr>
					<td class="labelTd">
						<label class="LabName"><span>办理单位：</span></label>
					</td>
					<td>
						<span class="Check_Radio FontDarkBlue">${(bo.rdName)!}</span>
					</td>
					<td class="labelTd">
						<label class="LabName"><span>办 理人：</span></label>
					</td>
					<td>
						<span class="Check_Radio FontDarkBlue">${(bo.createName)!}</span>
					</td>
					
				</tr>
				<tr>
					<td class="labelTd">
						<label class="LabName"><span>办理时间：</span></label>
					</td>
					<td colspan="3">
						<span  class="Check_Radio FontDarkBlue">${(bo.createTimeStr)!}</span>
					</td>
					
				</tr>
				<tr>
					<td class="labelTd">
						<label class="LabName"><span>处理结果：</span></label>
					</td>
					<td colspan="3">
						<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.handleResult)!}</span>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<label class="LabName"><span>处理过程：</span></label>
					</td>
					<td colspan="3">
						<span style="width:90%" class="Check_Radio FontDarkBlue">${(bo.handleProcess)!}</span>
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
	$(function(){
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
