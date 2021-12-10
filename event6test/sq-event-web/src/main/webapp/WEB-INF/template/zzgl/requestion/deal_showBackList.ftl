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
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" style="width: 80px;border: 1px solid #cecece;">
							<label class="LabName" style="text-align:center;"><span>时间</span></label>
						</td>
						<td align="center" style="width: 150px;border: 1px solid #cecece;">
							<label class="LabName" style="text-align:center;"><span>单位</span></label>
						</td>
						<td align="center" style="width: 80px;border: 1px solid #cecece;">
							<label class="LabName" style="text-align:center;"><span>联络员</span></label>
						</td>
						<td align="center" style="width: 80px;border: 1px solid #cecece;">
							<label class="LabName" style="text-align:center;"><span>电话</span></label>
						</td>
						<td align="center" style="width: 350px;border: 1px solid #cecece;">
							<label class="LabName" style="text-align:center;"><span>驳回信息</span></label>
						</td>
					</tr>
					<#list taskList as task>
					    <tr>
					        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.UPDATETIMESTR)!}</td>
					        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.LINKAGEUNITNAME)!}</td>
					        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.LINKMAN)!}</td>
					        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.LINKMANTEL)!}</td>
					        <td style="vertical-align: middle;border: 1px solid #cecece;" title="${(task.REMARKS)!}"><div style="white-space:break-word;text-overflow:ellipsis;overflow:hidden;width:350px;">${(task.REMARKS)!}</div></td>
					    </tr>
					</#list>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
