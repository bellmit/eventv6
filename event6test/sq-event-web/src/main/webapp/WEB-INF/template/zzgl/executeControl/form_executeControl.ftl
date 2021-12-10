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
                <input type="hidden" id="id" name="id" value="${bo.id}"/>
                <input type="hidden" id="controlLibraryId" name="controlLibraryId" value="${bo.controlLibraryId}"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td>
                            <label class="LabName"><span>管理员ID</span></label>
                            <input type="text" id="userIds" name="userIds" value="${(bo.userIds)!}" class="inp1 easyui-validatebox" readonly="readonly" data-options="validType:'maxLength[20]', tipPosition:'bottom'"  />
                        </td>
                    </tr>
					<tr>
						<td>
							<label class="LabName"><span>布控库名称</span></label>
							<input type="text" id="name" name="name" value="${(bo.name)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[25]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>描述</span></label>
							<input type="text" id="description" name="description" value="${(bo.description)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>布控库类型</span></label>
                            <input type="hidden" id="libType" name="libType"/>
							<input type="text" id="libTypeCN" name="libTypeCN" value="${(bo.libType)!}" class="inp1 easyui-validatebox" readonly="readonly"data-options="validType:'maxLength[24]', tipPosition:'bottom'"  />
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

    $(function() {
        AnoleApi.initListComboBox("libTypeCN", "libType", null, null, ['${bo.libType}'], {
            DataSrc : [{"name":"黑名单库", "value":"1"},{"name":"白名单库", "value":"2"}],
            ShowOptions : {
                EnableToolbar : true
            },
            DefText : '请选择'
        });
    });

	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/event/executeControl/save.json',
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
