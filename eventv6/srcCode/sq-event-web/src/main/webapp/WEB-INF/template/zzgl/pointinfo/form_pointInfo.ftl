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
		<input type="hidden" id="gridPath" name="gridPath" value="${(bo.gridPath)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>主键</span></label>
							<input type="text" id="id" name="id" value="${(bo.id)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备id</span></label>
							<input type="text" id="deviceId" name="deviceId" value="${(bo.deviceId)!}" class="inp1 easyui-numberbox" data-options="min:0, max:99999999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备编码</span></label>
							<input type="text" id="deviceCid" name="deviceCid" value="${(bo.deviceCid)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备名称</span></label>
							<input type="text" id="deviceName" name="deviceName" value="${(bo.deviceName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备定位信息(纬度)</span></label>
							<input type="text" id="latitude" name="latitude" value="${(bo.latitude)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备定位信息(经度)</span></label>
							<input type="text" id="longitude" name="longitude" value="${(bo.longitude)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>设备序列号</span></label>
							<input type="text" id="deviceSn" name="deviceSn" value="${(bo.deviceSn)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>地域编码</span></label>
							<input type="text" id="regionCode" name="regionCode" value="${(bo.regionCode)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>网格编码</span></label>
							<input type="text" id="gridCode" name="gridCode" value="${(bo.gridCode)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建人</span></label>
							<input type="text" id="creator" name="creator" value="${(bo.creator)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
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
							<label class="LabName"><span>更新人</span></label>
							<input type="text" id="updator" name="updator" value="${(bo.updator)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>更新时间</span></label>
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
				url: '${rc.getContextPath()}//zhsq/event/pointInfo/save.json',
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
