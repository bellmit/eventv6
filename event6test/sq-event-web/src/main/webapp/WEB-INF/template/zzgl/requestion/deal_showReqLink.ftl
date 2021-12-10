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
						<td align="center" style="width: 120px;border: 1px solid #cecece;">
							<label class="LabName"><span>办理环节</span></label>
						</td>
						<td align="center" style="width: 220px;border: 1px solid #cecece;">
							<label class="LabName"><span>办理信息</span></label>
						</td>
						<td align="center" style="width: 220px;border: 1px solid #cecece;">
							<label class="LabName"><span>处理意见</span></label>
						</td>
						<td align="center" style="width: 220px;border: 1px solid #cecece;">
							<label class="LabName"><span>附件</span></label>
						</td>
					</tr>
					<#list taskList as task>
					    <tr>
					        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.TASK_NAME)!}<#if task.OPERATE_TYPE=='2'><br/>（驳回 ）<br/></#if></td>
					        <td style="vertical-align: middle;border: 1px solid #cecece;">办理人：${(task.TRANSACTOR_NAME)!}<br/>办理时间：${(task.END_TIME)!}</td>
					        <td style="vertical-align: middle;border: 1px solid #cecece;" title="${(task.REMARKS)!}"><div style="white-space:break-word;text-overflow:ellipsis;overflow:hidden;width:220px;">${(task.REMARKS)!}</div></td>
					        <td style="vertical-align: middle;border: 1px solid #cecece;">
					        	<#if task.fileList ?? && (task.fileList?size > 0)>
		                			<#list task.fileList as file>
		                				<div>
											<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br>
										</div>
								    </#list>
					            </#if>
					        </td>
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
