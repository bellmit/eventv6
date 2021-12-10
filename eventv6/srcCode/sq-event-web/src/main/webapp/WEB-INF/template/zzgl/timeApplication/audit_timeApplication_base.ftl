<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>申请审核页面</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	
	<style type="text/css">
		.areaClass{width:82%;}
		.auditRadio{cursor: pointer; color: #7c7c7c;}
	</style>
</head>
<body>
	<form id="timeApplicationForm" name="timeApplicationForm" action="" method="post">
		<input type="hidden" name="applicationId" value="<#if timeApp.applicationId??>${timeApp.applicationId?c}</#if>" />
		<input type="hidden" name="checkId" value="<#if timeApp.checkId??>${timeApp.checkId?c}</#if>" />
		
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"></label><div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditSuccess" value="1" checked/><label id="auditSuccessLabel" for="auditSuccess" class="auditRadio">审核通过</label></div>
					        </td>
					        <td class="LeftTd">
				    			<div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditFail" value="2"/><label id="auditFailLabel" for="auditFail" class="auditRadio">审核不通过</label></div>
					        </td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label id="checkAdviceLabel" class="LabName"><span>审核意见：</span></label><textarea name="checkAdvice" id="checkAdvice" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[200]','characterCheck'],tipPosition:'top'" style="height:100px;"></textarea>
					        </td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" class="BigNorToolBtn BigNorToolBtn SaveBtn" onclick="timeAppAudit();">确定</a>
        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();">取消</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function(){
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	        
	        $('#timeApplicationForm input[type="radio"][name="timeAppCheckStatus"]').on('click', function() {
	        	var auditStatus = $(this).val();
	        	
	        	$('#checkAdvice').validatebox({
					required: auditStatus == '2'
				});
	        });
	        
	        $('#checkAdvice').width(($(window).width() - $('#checkAdviceLabel').outerWidth(true)) * 0.96);
	        $("#norFormDiv").width($(window).width());
	    });
		
		function timeAppAudit() {
			var isValid =  $("#timeApplicationForm").form('validate');
			
			if(isValid) {
				modleopen();
				
				$("#timeApplicationForm").attr("action","${rc.getContextPath()}/zhsq/timeApplication/auditTimeApplication.jhtml");
				
				$("#timeApplicationForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success) {
						parent.reloadDataForSubPage(data.tipMsg, true);
					} else {
						var tipMsg = data.tipMsg || '操作失败！';
						
						$.messager.alert('错误', tipMsg, 'error');
					}
				});
				
			}
		}
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>
