<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>消息发送页面——网格员</title>

	<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/js/fastreply/fastReply.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div class="NorForm">
			<form id="sendMsgForm" name="sendMsgForm" action="" method="post">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"><span>消息内容：</span></label><textarea name="msgContent" id="msgContent" class="area1 easyui-validatebox" style="width:85%; height:90px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[500]','characterCheck']" ></textarea>
					        </td>
						</tr>
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"><span>接收人员：</span></label>
				    			<input type="text" id="receivePerson" class="comboselector" data-options="dType:'gridAdmin', afterSelect: selectGridAdmin, multiple: true, panelHeight: 220, value: <#if gridAdmin.gridAdminId??>${gridAdmin.gridAdminId?c}</#if>" query-params="mapType=${mapType!''}&x=${x!''}&y=${y!''}&distance=${distance!''}<#if infoOrgCode??>&orgCode=${infoOrgCode}</#if>" style="width: 460px; height: 28px;" />
	            		
				    			<div id="personUsedDiv" class="PersonUsed" style="width: 100%; *width: 94%; padding-top: 5px; padding-left: 30px; *padding-bottom: 5px;">
					    			<div id="receivePersonList" class="list">
					    			</div>
				    			</div>
					        </td>
						</tr>
						<tr>
							<td id="sendTypeTd" class="LeftTd">
				    			<label class="LabName"><span>发送方式：</span></label>
				    			<div class="Check_Radio"><input type="checkbox" id="sendInside" name="_sendTypeCheckbox" value="0" checked /><label for="sendInside" style="cursor: pointer;">站内消息</label></div>
				    			<div class="Check_Radio" style="margin-left: 100px;"><input type="checkbox" id="sendOutside" name="_sendTypeCheckbox" value="1" checked /><label for="sendOutside" style="cursor: pointer;">短信消息</label></div>
					        </td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	
	<div class="BigTool">
    	<div class="BtnList">
			<a href="###" onclick="tableSubmit();" class="BigNorToolBtn BigShangBaoBtn">发送</a>
			<a href="###" onclick="_closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
        </div>
    </div>
	
	<script type="text/javascript">
	    $(function(){
			var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        
	        enableScrollBar('content-d',options);
	        
	        $('#receivePerson').combogrid('textbox').css({'cursor': 'pointer'});
	        
	        <#if gridAdmin.userId??>
	        <#else>
	        	var gridAdminList = [], gridAdmin = {};
	        	gridAdmin.gridAdminId = "<#if gridAdmin.gridAdminId??>${gridAdmin.gridAdminId?c}</#if>",
				gridAdmin.partyName = "${gridAdmin.partyName!}",
				gridAdmin.mobileTelephone = "${gridAdmin.mobileTelephone!}";
	        	gridAdminList.push(gridAdmin);
	        	
	        	selectGridAdmin(gridAdminList);
	        </#if>
        });
        
		function tableSubmit(){
		    var isValid = $("#sendMsgForm").form('validate');
		    
			if(isValid){
				var checkboxChecked = $('#sendTypeTd input[type="checkbox"][name="_sendTypeCheckbox"]:checked'),
					checkedBoxLen = checkboxChecked.length,
					receiverLen = $("#receivePersonList > span").length,
					msgContent = $("#msgContent").val(),
					successLen = 0,
					failLen = 0,
					userIds = [],
					phoneNums = [];
					
				if(checkedBoxLen == 0) {
					$.messager.alert('提示', '请选择消息发送方式！', 'info');
				} else if(receiverLen == 0) {
					$.messager.alert('提示', '请选择消息接收人员！', 'info');
				} else {
					modleopen();
					var sendTypeArray = [];
					
					checkboxChecked.each(function() {
						sendTypeArray.push($(this).val());
					});
					
					sendTypeArray.push("2");//手动添加手机消息发送
					
					for(var index = 0, sendTypeArrayLen = sendTypeArray.length; index < sendTypeArrayLen; index++) {
						var sendType = sendTypeArray[index];
						
						switch(sendType) {
							case "0": {//发送站内消息
								userIds = [];
								$('#receivePersonList input[id="userIds"]').each(function() {
									var userId = $(this).val();
									
									if(userId) {
										userIds.push(userId);
									}
								});
								
								if(userIds.length) {
									$.ajax({
										type : 'POST',
										dataType : "jsonp",
										data: {'msgContent': encodeURIComponent(msgContent), 'userIds': userIds.toString(), 'moduleCode': '99', 'bizType': '${bizType!}'},
										url : "${ANOLE_COMPONENT_URL}/system/uam/newmessage/addMsg4Jsonp.json?jsoncallback=?&t="+Math.random(),
										success: function(data) {
											if(data.result == "1") {
												successLen++;
											} else if(data.msg) {
												failLen++;
												$.messager.alert('错误', '站内消息发送失败：'+data.msg, 'error');
											}
											
											if((successLen + failLen) == sendTypeArrayLen) {
												modleclose();
												
												if(successLen == sendTypeArrayLen) {
													$.messager.alert('提示', '消息发送成功!', 'info', function() {
														_closeWin();
													});
												}
											}
										},
										error : function() {
											failLen++;
											$.messager.alert('错误', '站内消息发送失败!', 'error');
										}
									});
									
								} else {
									$.messager.alert('警告', '没有可接收消息的人员!', 'warning');
								}
								
								break;
							}
							case "1": {//发送短信
								$('#receivePersonList input[id="phoneNums"]').each(function() {
									var phoneNum = $(this).val();
									
									if(phoneNum && isMobile(phoneNum)) {//手机号码才发送短信
										phoneNums.push(phoneNum);
									}
								});
								
								if(phoneNums.length) {
									$.ajax({
										type : 'POST',
										dataType : "json",
										data: {'msgContent': msgContent, 'phoneNums': phoneNums.toString(), 'sendType': 1},
										url : "${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/sendSms.json?t="+Math.random(),
										success: function(data) {
											if(data.result == true) {
												successLen++;
											} else {
												var msgWrong = "短信发送失败！";
												
												failLen++;
												
												if(data.msg) {
													msgWrong = '短信发送失败：' + data.msg;
												}
												
												$.messager.alert('错误', msgWrong, 'error');
											}
											
											if((successLen + failLen) == sendTypeArrayLen) {
												modleclose();
												
												if(successLen == sendTypeArrayLen) {
													$.messager.alert('提示', '消息发送成功!', 'info', function() {
														_closeWin();
													});
												}
											}
										},
										error : function() {
											failLen++;
											$.messager.alert('错误', '短信发送失败!', 'error');
										}
									});
								} else {
									$.messager.alert('警告', '没有可接收短信的手机号码!', 'warning');
								}
								
								break;
							}
							case "2": {//推送消息到手机端
								userIds = [];
								$('#receivePersonList input[id="userIds"]').each(function() {
									var userId = $(this).val();
									
									if(userId) {
										userIds.push(userId);
									}
								});
								
								$.ajax({
									type : 'POST',
									dataType : "json",
									data: {'msgContent': msgContent, 'userIds': userIds.toString()},
									url : "${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/pushMsg2Mobile.json?t="+Math.random(),
									success: function(data) {
										data = new Function("return (" + data + ")")();
										
										if(data.result == true) {
											successLen++;
										} else {
											var msgWrong = "手机消息推送失败！";
											
											failLen++;
											
											if(data.msg) {
												msgWrong = '手机消息推送失败：' + data.msg;
											}
											
											$.messager.alert('错误', msgWrong, 'error');
										}
										
										if((successLen + failLen) == sendTypeArrayLen) {
											modleclose();
											
											if(successLen == sendTypeArrayLen) {
												$.messager.alert('提示', '消息发送成功!', 'info', function() {
													_closeWin();
												});
											}
										}
									},
									error : function() {
										failLen++;
										$.messager.alert('错误', '手机消息推送失败!', 'error');
									}
								});
							}
							
						}
						
					}
				}
			}
		}
		
		function _closeWin() {
			var closeFunction = parent.closeMaxJqueryWindow;
			
			closeFunction();
		}
		
		function delReceivePerson(gridAdminId) {
			$('#receivePersonList > span[id="'+ gridAdminId +'"]').remove();
			
			$("#receivePerson").comboselector("clearValue",gridAdminId);
		}
		
		function selectGridAdmin(gridAdminList) {
			$("#receivePersonList > span").remove();
			
			if(gridAdminList && gridAdminList.length) {
				$(gridAdminList).each(function(index, gridAdmin) {
					var gridAdminSpan = "",
						gridAdminId = gridAdmin.gridAdminId || "",
						userId = gridAdmin.userId || "",
						partyName = gridAdmin.partyName || "",
						phoneNum = gridAdmin.mobileTelephone || "";
					
					if(gridAdminId && (userId || phoneNum)) {
						var isNotItemExist = true;
						
						if(userId) {
							isNotItemExist = isNotItemExist && $('#receivePersonList input[id="userIds"][value="'+ userId +'"]').length == 0;
						}
						
						if(phoneNum) {
							isNotItemExist = isNotItemExist && $('#receivePersonList input[id="phoneNums"][value="'+ phoneNum +'"]').length == 0;
						}
						
						if(isNotItemExist) {
							gridAdminSpan = '<span id="'+ gridAdminId +'" class="qingse">'+
											'	<input type="hidden" id="userIds" value="'+ userId +'" />'+
											'	<input type="hidden" id="phoneNums" value="'+ phoneNum +'" />'+
											'	<a>'+ partyName;
							if(phoneNum) {
								gridAdminSpan += '('+ phoneNum +')';
							}
							gridAdminSpan += '	</a>'+
											 '	<em onclick="delReceivePerson(\''+ gridAdminId +'\');"></em>'+
											 '</span>';
							$(gridAdminSpan).appendTo($("#receivePersonList"));
						}
					}
				});
			}
		}
	</script>
	
</body>
</html>
