<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>时限申请详情页面</title>
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
								<label class="LabName"><span>申请类别：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.applicationTypeName!''}</div>
							</td>
							<td class="LeftTd">
								<#if listType?? && listType == '1' && timeApp.timeAppCheckStatus?? && timeApp.timeAppCheckStatus == '3'>
									<label class="LabName"><span>申请次数：</span></label><div class="Check_Radio FontDarkBlue"><#if timeAppCount??>${timeAppCount?c}<#else>0</#if>次</div>
								<#else>
									
								</#if>
							</td>
						</tr>
						<tr>
							<#if timeApp.interval??>
								<td class="LeftTd" width="50%;">
									<label class="LabName"><span>申请时间：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.createTimeStr!''}</div>
								</td>
								<td class="LeftTd">
									<label class="LabName"><span>申请时限：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.intervalName!''}</div>
								</td>
							<#else>
								<td class="LeftTd" colspan="2">
									<label class="LabName"><span>申请时间：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.createTimeStr!''}</div>
								</td>
							</#if>
						</tr>
						<#if timeApp.applicationType == '8'>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>更改前分类：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.eventClassPre!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>更改后分类：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.eventClassAfter!''}</div>
							</td>
						</tr>
						</#if>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>申请原因：</span></label><div class="Check_Radio FontDarkBlue areaClass">${timeApp.reason!''}</div>
					        </td>
						</tr>
					</table>
					
					<!--为了支持通用，只有在事件相关的申请类别时，才展示事件的信息-->
					<#if timeApp.applicationType?? && (timeApp.applicationType == '1' || timeApp.applicationType == '2' || timeApp.applicationType == '3' || timeApp.applicationType == '4' || timeApp.applicationType == '5' || timeApp.applicationType == '8' || timeApp.applicationType == '9') >
						<div class="WebNotice" >
							<p>此申请关联了事件信息，<a href="###" onclick="showEventDetail(<#if timeApp.businessKeyId??>${timeApp.businessKeyId?c}</#if>);">点击查看</a></p>
						</div>
					</#if>
					
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<#if listType?? && listType == '1' && timeApp.timeAppCheckStatus?? && timeApp.timeAppCheckStatus == '3'>
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"></label><div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditSuccess" value="1" checked/><label for="auditSuccess" class="auditRadio">审核通过</label></div>
					        </td>
					        <td class="LeftTd">
				    			<div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditFail" value="2"/><label for="auditFail" class="auditRadio">审核不通过</label></div>
					        </td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>审核意见：</span></label><textarea name="checkAdvice" id="checkAdvice" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[200]','characterCheck'],tipPosition:'top'" style="width:80%;height:50px;"></textarea>
					        </td>
						</tr>
						<#else>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>审核人员：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.auditorName!''}<#if timeApp.auditorOrgName??>(${timeApp.auditorOrgName})</#if></div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>审核状态：</span></label><div class="Check_Radio FontDarkBlue">${timeApp.timeAppCheckStatusName!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>审核意见：</span></label><div class="Check_Radio FontDarkBlue areaClass">${timeApp.checkAdvice!''}</div>
					        </td>
						</tr>
						</#if>
						
						<#if timeApp.checkRemark??>
						<tr>
							<td class="LeftTd" colspan="2" >
								<label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue areaClass">${timeApp.checkRemark!}</div>
							</td>
						</tr>
						</#if>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
        		<#if listType?? && listType == '1' && timeApp.timeAppCheckStatus?? && timeApp.timeAppCheckStatus == '3'>
        		<a href="###" class="BigNorToolBtn BigNorToolBtn SaveBtn" onclick="timeAppAudit();">确定</a>
        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();">取消</a>
        		<#else>
				<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();">关闭</a>
				</#if>
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
		
		function showEventDetail(eventId) {
			if(eventId) {
				var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all" + "&eventId=" + eventId;
				
			  	parent.openJqueryWindowByParams({
			  		title: "查看事件信息",
			  		targetUrl: url
			  	});
		  	} else {
		  		$.messager.alert('警告', '缺少有效的事件id！', 'warning');
		  	}
		}
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>
