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
		<input type="hidden" id="rluId" name="rluId" value="${(bo.rluId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span>满意度：</span></label>
							<input type="radio" name="satisfaction" value="1" <#if bo.satisfaction=='1' || bo.satisfaction=='' || bo.satisfaction==null>checked="checked"</#if>><label>满意 </label>
        					<input type="radio" name="satisfaction" value="2" <#if bo.satisfaction=='2' >checked="checked"</#if>><label>基本满意</label>
        					<input type="radio" name="satisfaction" value="3" <#if bo.satisfaction=='3' >checked="checked"</#if>><label>不满意</label>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>回访信息:</span></label>
							<textarea name="detectedOverview" id="detectedOverview" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[2000]','characterCheck']"  style="width:370px;height:130px;float: left;margin-bottom:4px;margin-top:4px;">${(bo.visit)!}</textarea>
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
				url: '${rc.getContextPath()}/zhsq/requestion/saveDeal.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else {
						var rluId = $("#rluId").val();
						parent.flashList(rluId,"已评价");
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
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
