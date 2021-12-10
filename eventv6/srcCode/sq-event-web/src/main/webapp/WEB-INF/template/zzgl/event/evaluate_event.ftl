<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>评价事件</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	</head>
	<body>
		<form id="evaluateEventForm" name="evaluateEventForm" action="" method="post">
			<input type="hidden" id="eventIdStr" name="eventIdStr" value="${eventIdStr!}"/>
			<input type="hidden" name="evaObj" value="${evaObj!}"/>
			<input type="hidden" name="evaType" value="${evaType!}"/>
			
			<div id="content-d" class="MC_con content light">
				<div id="evaluateNorFormDiv" class="NorForm" style="width:718px;">
					<div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<#if evaLevelDict??>
								<tr> 
									<td>
										<label class="LabName"><span><label class="Asterik">*</label>评价等级：</span></label>
										<div class="Check_Radio">
											<#list evaLevelDict as evaItem>
												<span><input type="radio" name="evaLevel" value="${evaItem.dictGeneralCode}" id="eva_${evaItem.dictId?c}" <#if evaItem.dictGeneralCode=='02'>checked</#if>/><label for="eva_${evaItem.dictId?c}" style="cursor:pointer;">${evaItem.dictName}</label></span>
											</#list>
										</div>
									</td>
								</tr>
							</#if>
							<tr>
								<td>
									<label class="LabName"><span><label class="Asterik <#if isEvaContentRequired?? && isEvaContentRequired==true><#else>hide</#if>">*</label>评价意见：</span></label>
									<textarea id="evaContent" name="evaContent" class="area1 easyui-validatebox fast-reply" data-options="<#if isEvaContentRequired?? && isEvaContentRequired==true>required:true,</#if>tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" style="width: 710px;height:100px;"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			
			<div id="evaluateBigToolDiv" class="BigTool">
	        	<div class="BtnList">
	        		<a href="###" onclick="evaluateEvent();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
	            </div>
	        </div>
		</form>
		
		<script type="text/javascript">
			$(function() {
				var winWidth = $(window).width();
				
				<#if isPanelLoaded?? && isPanelLoaded==true>        	
	        	$("#evaluateEventForm div.BtnList").each(function(){
					/*15寸电脑“更改文本、应用等项目的大小”设置为125%时，首尾按钮通过.width()获取宽度时会少一个像素，有设置固定宽度的，不会有该问题*/
					var x = $(this).find("a").length > 1 ? 2 : 0;
					$(this).find("a").each(function(){
						x=x+$(this).outerWidth(true)+1;
					});
					$(this).css("width",x);
				});
				</#if>

				$('#evaluateNorFormDiv').width(winWidth);
				$('#evaContent').width((winWidth - $('#evaContent').siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			function evaluateEvent() {//点击提交按钮
				var isValid = $("#evaluateEventForm").form('validate');
				
				if(isValid) {
					if($('#evaluateNorFormDiv input[type=radio][name=evaLevel]:checked').length == 0) {
						$.messager.alert('警告', '请选择评价等级！', 'warning');
						isValid = false;
					}
				}
				
				if(isValid) {
					var eventIdStr = $('#eventIdStr').val();
					
					if(eventIdStr) {
						modleopen();
						
						$("#evaluateEventForm").attr("action", '${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/evaluateEvent4Batch.jhtml');
						
						$("#evaluateEventForm").ajaxSubmit(function(data) {
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
						$.messager.alert('错误','缺少需要评价的事件！','error');
					}
				}
			}
			
			function closeWin() {//点击取消按钮
				parent.closeMaxJqueryWindow();
			}
		</script>
	</body>
</html>
