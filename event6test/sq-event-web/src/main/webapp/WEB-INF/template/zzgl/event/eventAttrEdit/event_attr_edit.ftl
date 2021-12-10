<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>事件属性编辑页面</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<@block name="extraJs4EventAttrEdit"></@block>
		
		<style type="text/css">
			.w60{width: 60%;}
		</style>
	</head>
	<body>
		<div>
			<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
				
				<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
					<div id="norFormDiv" class="NorForm" style="width:784px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<@block name="extraTr4EventAttrEdit"></@block>
						</table>
					</div>
				</div>
				
				<div class="BigTool">
					<div class="BtnList">
						<a href="###" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
						<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
					</div>
				</div>
			</form>
		</div>
	</body>
	
	<script type="text/javascript">
		$(function() {
			$("#norFormDiv").width($(window).width());
			
			var options = {
				axis : "yx", 
				theme : "minimal-dark" 
			};
			enableScrollBar('content-d',options);
			
			<@block name="extraInit4EventAttrEdit"></@block>
		});
		
		function tableSubmit() {
			var isValid =  $("#tableForm").form('validate');
			
			if(isValid) {
				$("#tableForm").attr("action", "${rc.getContextPath()}/zhsq/event/eventDisposalController/editEvent/0701.jhtml");
		      	
		      	modleopen();
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		var msg = "事件更新";
			  		if(data.result && data.result == true) {
			  			msg += "成功！";
			  		} else {
			  			msg += "失败！";
			  		}
			  		
			  		parent.reloadDataForSubPage(msg);
			  	});
		  	}
		}
		
		function cancel() {
			if(parent && typeof parent.closeMaxJqueryWindow === 'function') {
				parent.closeMaxJqueryWindow();
			}
		}
        
	</script>
</html>