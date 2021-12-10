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
						<label class="LabName"><span>诉求ID：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.reqId)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>办理意见：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.overview)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>回访信息：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.visit)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>满意度评价：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.satisfaction)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>发起人id：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.userId)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>发起人name：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.userName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>发起时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatTime)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>状态：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.status)!}</span>
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
