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
		<input type="hidden" id="appealId" name="appealId" value="${(bo.appealId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span>网站群主键：</span></label>
							<input type="text" id="outId" name="outId" value="${(bo.outId)!}" style="height:30px;" class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求编号：</span></label>
							<input type="text" id="appealNo" name="appealNo" value="${(bo.appealNo)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[18]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求标题：</span></label>
							<input type="text" id="appealTitle" name="appealTitle" value="${(bo.appealTitle)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[200]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求类别：</span></label>
							<input type="text" id="appealCatalog" name="appealCatalog" value="${(bo.appealCatalog)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求内容：</span></label>
							<input type="text" id="content" name="content" value="${(bo.content)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1000]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>来源：</span></label>
							<input type="text" id="source" name="source" value="${(bo.source)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[200]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求时间：</span></label>
							<input type="text" id="appealTime" name="appealTime" value="${(bo.appealTime)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="inp1 Wdate" data-options="tipPosition:'bottom'" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求状态：</span></label>
							<input type="text" id="appealStatus" name="appealStatus" value="${(bo.appealStatus)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>处理情况：</span></label>
							<input type="text" id="handleSit" name="handleSit" value="${(bo.handleSit)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>处理结果：</span></label>
							<input type="text" id="handleRs" name="handleRs" value="${(bo.handleRs)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1000]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>姓名：</span></label>
							<input type="text" id="userName" name="userName" value="${(bo.userName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>手机号：</span></label>
							<input type="text" id="phone" name="phone" value="${(bo.phone)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>是否公开：</span></label>
							<input type="text" id="isPub" name="isPub" value="${(bo.isPub)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建人：</span></label>
							<input type="text" id="createUserId" name="createUserId" value="${(bo.createUserId)!}" style="height:30px;" class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建时间：</span></label>
							<input type="text" id="createTime" name="createTime" value="${(bo.createTime)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="inp1 Wdate" data-options="tipPosition:'bottom'" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>最后一次更新人：</span></label>
							<input type="text" id="updateUserId" name="updateUserId" value="${(bo.updateUserId)!}" style="height:30px;" class="inp1 easyui-numberbox" data-options="min:0, max:99999999, tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>最后一次更新时间：</span></label>
							<input type="text" id="updateTime" name="updateTime" value="${(bo.updateTime)!}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" class="inp1 Wdate" data-options="tipPosition:'bottom'" readonly />
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
				url: '${rc.getContextPath()}/gmis/publicAppeal/save.json',
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
