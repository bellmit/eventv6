<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
</head>
<body>
	<div class="MC_con content light">
		<div class="ConList">
	     	<div class="nav" id="tab" style="margin-top:10px;">
		        <ul>
		            <li class="current">基本情况</li>
		            <li>办理详情</li>
		        </ul>
	    	</div>
	 	</div>
	 	<div name="tab" id="content0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>事件类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.typeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>日      期：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.expectTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>标      题：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.title)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>事件描述：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.content)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>企业名称：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.reqObjName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联  系 人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkMan)!}</span>
					</td>
					<td>
						<label class="LabName"><span>联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkTel)!}</span>
					</td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">附件信息</div></td>
                </tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>附       件：</span></label>
						<span class="Check_Radio FontDarkBlue"></span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>备       注：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.desc)!}</span>
					</td>
				</tr>
			</table>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>办理环节</span></label>
					</td>
					<td>
						<label class="LabName"><span>办理信息</span></label>
					</td>
					<td>
						<label class="LabName"><span>处理时间</span></label>
					</td>
					<td>
						<label class="LabName"><span>处理意见</span></label>
					</td>
					<td>
						<label class="LabName"><span>附件</span></label>
					</td>
				</tr>
				<#list taskList as task>
				    <tr>
				        <td><label class="LabName"><span class="FontDarkBlue">${(task.TASK_NAME)!}</span></label></td>
				        <td><label class="LabName"><span>办理人：${(task.TRANSACTOR_NAME)!}</span></label></td>
				        <td><label class="LabName"><span>${(task.END_TIME)!}</span></label></td>
				        <td><label class="LabName"><span>${(task.REMARKS)!}</span></label></td>
				        <td><label class="LabName"><span></span></label></td>
				    </tr>
				</#list>
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
		var $NavDiv2 = $("#tab ul li");
		$NavDiv2.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = $NavDiv2.index(this);
			$("div[id^='content']").hide();
			$("#content"+NavIndex2).show();
		});
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
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

