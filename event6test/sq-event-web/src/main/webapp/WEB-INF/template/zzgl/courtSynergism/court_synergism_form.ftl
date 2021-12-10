<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
		.Asterik{color: red;}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="synergismId" name="synergismId" value="${(bo.synergismId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>申请类别：</span></label>
							<input type="hidden" name="applyType" id="applyType" value='${(bo.applyType)!}' />
							<input type="text" id="applyTypeCN" name="applyTypeCN" value="" class="inp1 easyui-validatebox" data-options="required:true" />
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>法官姓名：</span></label>
							<input type="text" id="courtName" name="courtName" value="${(bo.courtName)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>联系方式：</span></label>
							<input type="text" id="contactInformation" name="contactInformation" value="${(bo.contactInformation)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'mobileorphone', tipPosition:'bottom'"  />
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>所属部门：</span></label>
							<input type="text" id="department" name="department" value="${(bo.department)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[30]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>申请时间：</span></label>
							<input type="text" id="applyDate" name="applyDate" value="${(bo.applyDateStr)!}" class="inp1 Wdate" data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
						<td>
							<label class="LabName"><span>所属网格：</span></label>
							<input type="hidden" id="gridCode" name="gridCode" value="${(bo.gridCode)!}" />
							<input type="text" id="gridName" name="gridName" value="${(bo.gridName)!}" class="inp1 easyui-validatebox" readonly/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="LabName"><span>事项说明：</span></label>
							<textarea style="height:150px;width:60%;" name="itemDescription" id="itemDescription" value="" class="inp1 InpDisable easyui-validatebox" data-options="required:false,validType:'maxLength[500]'" >${(bo.itemDescription)!}</textarea>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn BigShangBaoBtn" onClick="save(1);">提交</a>
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save(0);">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
	$(function () {
		AnoleApi.initListComboBox("applyTypeCN", "applyType", "B12322002",null,["${(bo.applyType)!}"]);
	})

	//保存
	function save(type) {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			let url = '${rc.getContextPath()}/zhsq/courtSynergism/save.json';
			var subMig = '保存成功！';
			if(type == 1){
				url = '${rc.getContextPath()}/zhsq/courtSynergism/save.json?saveType=submit';
				subMig = '提交成功！';
			}
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: url,
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.success) {
                        $.messager.alert('提示', subMig, 'info', function() {
                            parent.closeMaxJqueryWindow();
                        });
                        parent.searchData();
					} else {
					    let type = data.type;
					    let message = "保存失败！"
                        switch (type) {
                            case '0':  message = "提交失败！"
                                break;
                            case '-2':  message = "无法获取镇专班组织！"
                                break;
                            case '-1':  message = "无法获取镇专班人员！"
                                break;
                            case '-3':  message = "上报异常！"
                                break;
                        }
                        $.messager.alert('错误', message, 'error');
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
