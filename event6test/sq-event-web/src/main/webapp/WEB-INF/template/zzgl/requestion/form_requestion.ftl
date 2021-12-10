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
		<input type="hidden" id="reqId" name="reqId" value="${(bo.reqId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td>
							<label class="LabName"><span>诉求对象ID</span></label>
							<input type="text" id="reqObjId" name="reqObjId" value="${(bo.reqObjId)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>诉求对象名称</span></label>
							<input type="text" id="reqObjName" name="reqObjName" value="${(bo.reqObjName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联系人</span></label>
							<input type="text" id="linkMan" name="linkMan" value="${(bo.linkMan)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>联系方式</span></label>
							<input type="text" id="linkTel" name="linkTel" value="${(bo.linkTel)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[30]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>编号</span></label>
							<input type="text" id="code" name="code" value="${(bo.code)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>标题</span></label>
							<input type="text" id="title" name="title" value="${(bo.title)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>类型</span></label>
							<input type="text" id="type" name="type" value="${(bo.type)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>内容</span></label>
							<input type="text" id="content" name="content" value="${(bo.content)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[4000]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>期望时间</span></label>
							<input type="text" id="expectTime" name="expectTime" value="${(bo.expectTime)!}" class="inp1 Wdate" data-options="tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>发起人id</span></label>
							<input type="text" id="userId" name="userId" value="${(bo.userId)!}" class="inp1 easyui-numberbox" data-options="min:0, max:999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>发起人name</span></label>
							<input type="text" id="userName" name="userName" value="${(bo.userName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>创建时间</span></label>
							<input type="text" id="creatTime" name="creatTime" value="${(bo.creatTime)!}" class="inp1 Wdate" data-options="tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>来源</span></label>
							<input type="text" id="source" name="source" value="${(bo.source)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[1]', tipPosition:'bottom'"  />
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
				url: '${rc.getContextPath()}/zhsq/requestion/save.json',
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
