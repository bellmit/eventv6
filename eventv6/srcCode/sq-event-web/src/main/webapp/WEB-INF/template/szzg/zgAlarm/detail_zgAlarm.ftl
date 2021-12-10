<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style>
	.LabName{
			width:70px;
	}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>告警类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.alarmTypeName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>告警级别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.alarmLevelName)!}</span>
					</td>
					
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>链接地址：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.alarmUrl)!}</span>
					</td>
					<td>
						<label class="LabName"><span>告警来源：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.alarmSource)!}</span>
					</td>
				</tr>
				<tr>
				   <td>
						<label class="LabName"><span>失效日期：</span></label>
						<span class="Check_Radio FontDarkBlue"><#if bo.invalidDate??>${bo.invalidDate?string('yyyy-MM-dd HH:mm:ss')}</#if></span>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>告警内容：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:507px;">${(bo.alarmContent)!}</span>
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
			//改变滚动条样式
			$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
			
	});
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
