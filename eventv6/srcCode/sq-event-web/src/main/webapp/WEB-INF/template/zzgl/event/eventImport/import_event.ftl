<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
<#include "/component/commonFiles-1.1.ftl" />

</head>
<body class="easyui-layout">
	<div id="jqueryToolbar">
		<div class="ConList" >
			<div class="ToolBar">
				<div class="tool fr" >
					<a href="###" class="NorToolBtn LeadingInBtn" onclick="openDialog('importDiv')">导入</a>
				</div>
			</div>
		</div>
	</div>
	
	<div id="operateMask" class="MarskLayDiv hide"></div>
	<div id="importProgressDev" class="PopDiv NorForm hide">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="2" class="LeftTd">
					<label class="LabName" style="width:115px"><span>需导入记录总数：</span></label><div class="Check_Radio FontDarkBlue"><span id="importTotal">0</span></div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="LeftTd">
					<label class="LabName" style="width:115px"><span>成功导入记录数：</span></label><div class="Check_Radio FontDarkBlue"><span id="importSuccess"></span></div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="LeftTd">
					<label class="LabName" style="width:115px"><span>失败导入记录数：</span></label><div class="Check_Radio FontRed"><span id="importFail"></span><a href="###" id="downProgress" class="hide" onclick="downLoadFail();">下载失败记录</a></div>
				</td>
			</tr>
			<tr id="finishProgress" class="hide">
				<td style="width: 59%">
	    			<a href="###" onclick="closeProgressDiv();" class="BigNorToolBtn CancelBtn" style="float:right;">关闭</a>
	    		</td>
	    		<td></td>
			</tr>
	    </table>
	</div>
	
	<div id="importDiv" class="easyui-dialog" closed="true" title="事件导入" style="width:380px;height:155px;" data-options="modal:true" >
		<div class="ConSearch">
			<form id="importForm" action='${rc.getContextPath()}/zhsq/event/import/importData.jhtml' method="post" enctype="multipart/form-data">
				<input type="text" value="" style="display:none;" /><!-- 用于防止回车后提交 -->
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" class="LeftTd">
								<span>请选择导入文件(文件格式为：<label class="FontRed"><b>xls</b></label>)</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<input type="file" id="importFile" name="importFile" />
							</td>
						</tr>
						<tr>
							<td><a href="###" class="BigNorToolBtn SubmitBtn" style="float:right;" title="确定" onclick="_importData()">确定</a></td>
							<td><a href="###" class="BigNorToolBtn CancelBtn" title="取消" onclick="closeDialog('importDiv')">取消</a></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div> 
	
<script type="text/javascript">
	var _checkProgressIntervalId = null;//导入进程展示intervalId
	
	$(function(){
		<#if resultObj??>
			showImportMsg();
		<#elseif threadId??>
			showProgress();
		</#if>
	});
	
	//导入
	function _importData() {
		var filePath = $("#importFile").val();
		var filePathLen = filePath.length;
		var warningMsg = "";
		
		if(filePathLen > 0) {
			var fileType = filePath.substring(filePathLen - 3);
			fileType = fileType.toLowerCase();
			if(fileType != "xls") {
				warningMsg = '请选择xls格式文件！';
			}
		} else {
			warningMsg = '请选择导入文件！';
		}
		
		if(warningMsg != "") {
			$.messager.alert('警告', warningMsg, 'warning');
		} else {
			modleopen();
			closeDialog('importDiv');
			
	      	$("#importForm").attr('action', '${rc.getContextPath()}/zhsq/event/import/importData.jhtml');
	      	
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
		var msg = "";
		
		<#if resultObj??>
			msg = "${resultObj.tipMsg!}";
		</#if>
		
		if(msg != "") {
			$.messager.alert('提示', msg, 'info');
		}
	}
	
	function showProgress() {//展示导入数据信息框
		$('.PopDiv').css({'top':$(window).height()/3, 'width': '43%'});
		$("#operateMask").show();
		$("#importProgressDev").show();
		
		modleopen();
		_checkProgressIntervalId = setInterval("checkProgress()", 5000);
	}
	
	function checkProgress() {//展示导入数据进度
		var threadId = <#if threadId??>${threadId?c}<#else>-1</#if>;
		var total = $("#importTotal").html(),
			success = $("#importSuccess").html(),
			fail = $("#importFail").html();
		
		if(isNotBlankParam(success) && isNotBlankParam(fail)) {
			total = parseInt(total, 10);
			success = parseInt(success, 10);
			fail = parseInt(fail, 10);
			
			if(total == (success + fail) ) {
				clearInterval(_checkProgressIntervalId);
				$("#finishProgress").show();
				
				if(fail > 0) {
					$("#downProgress").show();
				}
				
				modleclose();
			}
		}
		
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/event/import/checkProgress.jhtml',
			data: 'threadId='+ threadId,
			dataType:"json",
			success: function(data){
				var total = data.total;
				
				if(total >= 0) {//总数未统计完成时，不更新进度，防止进度更新提前终止，因为 0 = 0 + 0
					$("#importTotal").html(total);
					$("#importSuccess").html(data.success);
					$("#importFail").html(data.fail);
				}
			},
			error:function(data){
				$.messager.alert('错误','连接错误！','error');
			}
    	});
	}
	
	function closeProgressDiv() {//关闭导入数据进度框，并清除相应进度数据
		$("#finishProgress").hide();
		$("#downProgress").hide();
		
		$("#operateMask").hide();
		$("#importProgressDev").hide();
		
		var threadId = <#if threadId??>${threadId?c}<#else>-1</#if>;
		
		$.ajax({
			type: "POST",
    		url : '${rc.getContextPath()}/zhsq/event/import/delProgress.jhtml',
			data: 'threadId='+ threadId,
			dataType:"json",
			success: function(data){
			}
    	});
	}
	
	function downLoadFail() {//下载导入失败记录
		var threadId = <#if threadId??>${threadId?c}<#else>-1</#if>;
		
		$("#importForm").attr('action', '${rc.getContextPath()}/zhsq/event/import/downLoadFail.jhtml?threadId='+threadId);
		
		$("#importForm").submit();
	}
	
</script>

</body>
</html>