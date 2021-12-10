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
		<input type="hidden" id="dwtId" name="dwtId" value="${(bo.dwtId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>所属小区</span></label>
							<input type="text" id="infoOrgCode" name="infoOrgCode" value="${(bo.infoOrgCode)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[32]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>小区名称</span></label>
							<input type="text" id="infoOrgName" name="infoOrgName" value="${(bo.infoOrgName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[32]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备编号</span></label>
							<input type="text" id="deviceId" name="deviceId" value="${(bo.deviceId)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备名称</span></label>
							<input type="text" id="deviceName" name="deviceName" value="${(bo.deviceName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[32]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备标识</span></label>
							<input type="text" id="deviceGlag" name="deviceGlag" value="${(bo.deviceGlag)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>告警时间</span></label>
							<input type="text" id="warnTime" name="warnTime" value="${(bo.warnTime)!}" class="inp1 Wdate" data-options="tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>告警信息</span></label>
							<input type="text" id="warnInfo" name="warnInfo" value="${(bo.warnInfo)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[256]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备地址</span></label>
							<input type="text" id="deviceAddr" name="deviceAddr" value="${(bo.deviceAddr)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[256]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建人</span></label>
							<input type="text" id="createBy" name="createBy" value="${(bo.createBy)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建时间</span></label>
							<input type="text" id="createTime" name="createTime" value="${(bo.createTime)!}" class="inp1 Wdate" data-options="tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>修改人</span></label>
							<input type="text" id="updateBy" name="updateBy" value="${(bo.updateBy)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>修改时间</span></label>
							<input type="text" id="updateTime" name="updateTime" value="${(bo.updateTime)!}" class="inp1 Wdate" data-options="tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/warnTask/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
					}
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
		}
	}
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
