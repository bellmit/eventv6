<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>事件批量提交</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/commonFiles-1.1.ftl" />
		<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	</head>
	<body>
		<form id="archiveEventForm" name="archiveEventForm" action="" method="post">
			<input type="hidden" id="eventIdStr" name="eventIdStr" value="${eventIdStr!}"/>
			<input type="hidden" name="nextNodeName" value="${nextNodeName!}" />
			
			<div id="content-d" class="MC_con content light">
				<div id="norFormDiv" class="NorForm" style="width:718px;">
					<div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label>
									<textarea id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" style="width: 710px;height:100px;"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			
			<div class="BigTool">
	        	<div class="BtnList">
	        		<a href="###" onclick="subEvent();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
	            </div>
	        </div>
		</form>
		
		<script type="text/javascript">
			$(function() {
				var winWidth = $(window).width();
				
				$('#norFormDiv').width(winWidth);
				$('#advice').width((winWidth - $('#advice').siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			function subEvent() {//点击提交按钮
				var isValid = $("#archiveEventForm").form('validate');
				
				if(isValid) {
					var eventIdStr = $('#eventIdStr').val();
					
					if(eventIdStr) {
						modleopen();
						
						$("#archiveEventForm").attr("action", '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/subEvent4Batch.jhtml');
						
						$("#archiveEventForm").ajaxSubmit(function(data) {
							modleclose();
							
							if(data.success == true) {
								if(parent && typeof parent.searchData === 'function' ) {
									parent.searchData();
								}
								
								$.messager.alert('提示', data.tipMsg, 'info', closeWin);
							} else {
								$.messager.alert('错误', data.tipMsg, 'error');
							}
						});
					} else {
						$.messager.alert('错误','缺少需要办理的事件！','error');
					}
				}
			}
			
			function closeWin() {//点击取消按钮
				parent.closeMaxJqueryWindow();
			}
		</script>
		
	</body>
</html>
