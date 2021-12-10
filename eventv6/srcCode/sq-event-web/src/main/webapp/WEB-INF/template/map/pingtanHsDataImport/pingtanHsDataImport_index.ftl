<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>平潭楼宇轮廓数据导入页面</title>
<#include "/component/commonFiles-1.1.ftl" />

</head>
<body class="easyui-layout">
	<div id="jqueryToolbar">
		<div class="ConList" >
			<div class="ToolBar">
				<div class="tool fr" >
					<a href="#" class="NorToolBtn LeadingInBtn" onclick="openDialog('importDiv')">导入</a>
				</div>
			</div>
		</div>
	</div>
	
	<div id="importDiv" class="easyui-dialog" closed="true" title="导入平潭轮廓数据" style="width:380px;height:155px;" data-options="modal:true" >
		<div class="ConSearch">
			<form id="importForm" action='${rc.getContextPath()}/zhsq/map/dataImportController/importData.jhtml' method="post" enctype="multipart/form-data">
				<input type="text" value="" style="display:none;" /><!-- 用于防止回车后提交 -->
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" class="LeftTd">
								<span>请选择导入文件(文件格式为：<label class="FontRed"><b>txt</b></label>)</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<input type="file" id="importFile" name="importFile" />
							</td>
						</tr>
						<tr>
							<td><a href="#" class="BigNorToolBtn SubmitBtn" style="float:right;" title="确定" onclick="_importData()">确定</a></td>
							<td><a href="#" class="BigNorToolBtn CancelBtn" title="取消" onclick="closeDialog('importDiv')">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div> 
	
<script type="text/javascript">
	$(function(){
		showImportMsg();
	});
	
	//导入
	function _importData() {
		var filePath = $("#importFile").val();
		var filePathLen = filePath.length;
		var warningMsg = "";
		
		if(filePathLen > 0) {
			var fileType = filePath.substring(filePathLen - 3);
			fileType = fileType.toLowerCase();
			if(fileType != "txt") {
				warningMsg = '请选择txt格式文件！';
			}
		} else {
			warningMsg = '请选择导入文件！';
		}
		
		if(warningMsg != "") {
			$.messager.alert('警告', warningMsg, 'warning');
		} else {
			closeDialog('importDiv');
	      	modleopen();
		  	$("#importForm").submit();
	  	}
	}
	
	//打开初始化对话框
	function openDialog(divId){
		$('#'+divId).dialog('open');
	}
	
	//关闭初始化对话框
	function closeDialog(divId){
		$('#'+divId).dialog('close');
	}
	
	function showImportMsg() {//展示导出提示信息
		var count = ${count!0};
		var msg = "";
		
		if(count && count > 0) {
			msg += "涉及记录 "+count+" 条。";
		}
		
		if(msg != "") {
			$.messager.alert('提示', msg, 'info');
		}
	}
</script>

</body>
</html>