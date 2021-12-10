<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
</head>
<body>
	<input type="hidden" id="orgCode" name="orgCode" value="${orgCode}" />
	<form id="submitForm">
		<input type="hidden" id="statId" name="statId" value="${(bo.statId)!}" />
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>学校名称</span></label>
							<input type="hidden" id="schId" name="schId" value="${(bo.schId)!}">
							<select id="schoolName" name="schoolName" class="inp1" style="width:225px" value="${(bo.schoolName)!}">
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>老师人数</span></label>
							<input type="number" id="teachers" name="teachers" value="${(bo.teachers)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>学生人数</span></label>
							<input type="number" id="students" name="students" value="${(bo.students)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>男生人数</span></label>
							<input type="number" id="males" name="males" value="${(bo.males)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>女生人数</span></label>
							<input type="number" id="females" name="females" value="${(bo.females)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999999999999999999999, tipPosition:'bottom'" style="height:30px;" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><font style="color:red">*</font>统计年份</span></label>
							<input type="number" id="statYear" name="statYear" value="${(bo.statYear)!}" class="inp1 easyui-numberbox" data-options="min:0, max:9999, tipPosition:'bottom'" style="height:30px;" />
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
		getSchool();
	});
	
	function getSchool(){
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/szzg/school/listData.json',
			data: {"page":"1","rows":"999999999","orgCode":"${orgCode}"},
			dataType: 'json',
			success: function(data) {
				showSchool(data);
			},
		});
	};
	
	function showSchool(data){
		var select = document.getElementById("schoolName");
		for(var i = 0,l=data.total;i<l;i++){
			select.options[i] = new Option(data.rows[i].schoolName);
			select.options[i].value = data.rows[i].seqid;
		}
		var statId = $("#statId").val();
		var schId = $("#schId").val();
		if(statId != null && statId != ""){
			var select = document.getElementById("schoolName");
			for(var i=0,l=select.length;i<l;i++){
				if(select[i].value == schId){
					select[i].selected = true;
				}
			}
		}else{
			$("#schId").val(data.rows[0].seqid);
		}
	}
	
	$("#schoolName").change(function(){
		$("#schId").val( $(this).val());
	});

	//保存
	function save() {
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/schoolStat/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('提示', data.msg, 'info');
					} else {
						$.messager.alert('提示', data.msg, 'info', function() {
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
