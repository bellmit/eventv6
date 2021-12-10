<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
		.area1{
		 width: 570px;
		}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="planId" name="planId" value="${(bo.planId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span><font style="color: red">*</font>所属区域：</span></label>
							<input type="hidden" id="regionCode" name="regionCode" value="${bo.regionCode!regionCode}" />
                        	<input type="text" id="gridName" name="gridName" value="${bo.regionName!regionName}" class="inp1 comboxWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
						</td>
						<td class="LeftTd">
							<label class="LabName"><span><font style="color: red">*</font>预案类型：</span></label>
							    <input type="hidden" id="planType" name="planType" value="${bo.planType!''}" />
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom'" id="planTypeName" value="${bo.planTypeName!''}" />
						</td>
			
					</tr>
		<!-- 			<tr>   
						<td colspan="2">
							<label class="LabName"><span>预案名称</span></label>
							<input type="text" id="planName" name="planName" value="${(bo.planName)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[60]', tipPosition:'bottom'"  />
						</td>
					</tr> -->
					
					<tr>
						<td colspan="2">
							<label class="LabName"><span>预案内容：</span></label>
							<textarea name="planContent" id="planContent" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${(bo.planContent)!}</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="LabName"><span>备注：</span></label>
							<textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${(bo.remark)!}</textarea>
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
	$(function(){
		//所属区域
		AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#regionCode").val(grid.gridCode);   
			} 
		}, {
			OnCleared: function() {
				$("#regionCode").val('');
			},
			ShowOptions: {
				EnableToolbar : true
			}
		});
		AnoleApi.initTreeComboBox("planTypeName", "planType", "A001139", null, ['${bo.planType!}']);
		
	});
	
	
	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			var regionCode=$("#regionCode").val();
			var planType=$("#planType").val();
			var planId=$("#planId").val();
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/countList.json?regionCodeAll='+regionCode+'&planType='+planType+'&excludeId='+planId,
				//data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if(data>0){
						$.messager.alert('错误', '预案已存在，请修改预案！', 'error');
						modleclose(); //关闭遮罩层
					}else{
						$.ajax({
							type: 'POST',
							url: '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/save.json',
							data: $('#submitForm').serializeArray(),
							dataType: 'json',
							success: function(data) {
								if (data.result == 'fail') {
									$.messager.alert('错误', '保存失败！', 'error');
								} else {
									$.messager.alert('提示', '保存成功！', 'info', function() {
										parent.closeMaxJqueryWindow();
									});
									 var id = $("#planId").val();
									var isCurrent=false;
									if(id!=''){
										isCurrent=true;
									}
									parent.searchData(isCurrent);
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
