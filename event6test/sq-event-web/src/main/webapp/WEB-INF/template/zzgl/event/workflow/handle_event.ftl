<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>办理事件</title>

	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<style type="text/css">
	.DealProcess{background-color:#F9F9F9;}
	.DealProcess td{padding-bottom:10px;}
	.DealProcess .LabName{width:100px;}
	.DealMan{width:630px; line-height:24px;}
	.MarskDiv{width:844px; height:52px; position:absolute; z-index:2; top:0; left:0;}
	.Asterik{font-size:12px; color:red; }
	/*办理人员全选样式*/
	.SelectAll{padding:2px 8px; *padding:0 8px 2px 5px; display:block; float:left; border-radius:3px; background:#2dcc70; color:#fff; font-weight:normal; font-size:12px;}
	.SelectAll:hover{background:#27ae61;}
	</style>
</head>

<body>
	<input type="hidden" id="formId" name="formId" value="${formId?c}">
	<input type="hidden" id="taskId" name="taskId" value="${taskId}">
	<input type="hidden" id="instanceId" name="instanceId" value="${instanceId?c}">
	<input type="hidden" id="deploymentId" name="deploymentId" value="${deploymentId?c}">
	
	<input type="hidden" id="curNodeId" name="curNodeId" value="${curNodeId?c}"> 
	<input type="hidden" id="curNodeName" name="curNodeName" value="${curNode.nodeName}"> 
	<input type="hidden" id="formTypeId" name="formTypeId" value="<#if curNode.formTypeId??>${curNode.formTypeId?c}</#if>"> 

	<input type="hidden" id="nodeId" name="nodeId">
	<input type="hidden" id="nodeName_" name="nodeName_">  
	
	<input type="hidden" id="selectedNodeValue" name="selectedNodeValue" value=""> 
	<input type="hidden" id="selectedNodeDynamicSelect" name="selectedNodeDynamicSelect" value=""> 
	<input type="hidden" id="selectedNodenNodeType" name="selectedNodenNodeType" value=""> 
	
	<input type="hidden" id="beforeNodeName" name="beforeNodeName" value="<#if backTask??>${backTask.taskName!}</#if>"> 
	<input type="hidden" id="workFlowId" name="workFlowId" value="${workFlowId?c}"> 
	
	<input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
	
	<!--流程有关-->
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="DealProcess">
		<tr id="tr_epath">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>下一环节：</span></label>
				<#if taskNodes??>
					<div class="Check_Radio" style="margin-top:7px;">
						<#list taskNodes as node>
							<span>
								<input type="radio" id="${node.nodeName}" name="nextNode" onclick="checkRadio(this);"
								nodeName="${node.nodeName}" nodeType="${node.nodeType}" 
								dynamicSelect="<#if node.dynamicSelect??>${node.dynamicSelect}</#if>" nodeId="${node.nodeId?c}" 
								value="${node.nodeName}"><label for="${node.nodeName}" style="cursor:pointer">${node.nodeName}</label></input>
							</span>
						</#list>
					</div>
				</#if>
			</td>
		</tr>
		
		 <tr id="userDiv" style="display: none;"> 
		    <td>
			    <label class="LabName"><span>选择办理人：</span></label>
			    <div class="Check_Radio">
			    	<a href="###" class="NorToolBtn EditBtn fl" id="userSelectBtn" onclick="selectUser('userIds', 'userNames', 'curOrgIds', 'task');">选择办理人</a>
				</div>
				<input type="hidden" name="userIds" id="userIds"/>
				<input type="hidden" name="curOrgIds" id="curOrgIds"/>
				<div class="FontDarkBlue fl DealMan"><b name="userNames" id="userNames"></b></div>
		    	<div class="FontDarkBlue fl DealMan"><b name="htmlUserNames" id="htmlUserNames"></b></div>
				<input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
				<!-- <input type="text" name="htmlUserNames" id="htmlUserNames" class="mustinput" readonly="readonly" style="width: 250px !important;"/> -->
		    </td>
    	</tr> 
		
		<tr id="imageUpBefore" style="display: none;">
			<td><label class="LabName"><span>图片上传：</span></label><div class="ImgUpLoad" id="fileupload"></div></td>
		</tr>
        <tr id="evaluate_level" style="display: none;"> 
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>评价等级：</span></label>
				<div class="Check_Radio" style="margin-top:7px;">
					<span><input type="radio" name="evaLevel" value="01" id="unSatisfyLevel"/><label for="unSatisfyLevel" style="cursor:pointer;">不满意</label></span>
	    			<span><input type="radio" name="evaLevel" value="02" id="satisfyLevel" checked/><label for="satisfyLevel" style="cursor:pointer;">满意</label></span>
	    			<span><input type="radio" name="evaLevel" value="03" id="verySatisfyLevel" /><label for="verySatisfyLevel" style="cursor:pointer;">非常满意</label></span>
    			</div>
			</td>
        </tr>
		<tr id="evaluate_content" style="display: none;">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>评价意见：</span></label>
				<textarea rows="3" style="width: 710px;height:50px;" id="evaContent" name="evaContent" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"></textarea>
			</td>
		</tr>
		<tr id="remarkTr">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label>
				<textarea rows="3"	style="width: 710px;height:50px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
			</td>
		</tr>
		<tr>
			<td>
				<label class="LabName"><span></span></label>
				<div class="Check_Radio">
					<input type='checkbox' id='sendSms_' name='sendSms_' onclick="showSmsCont();" /><label for="sendSms_" style="cursor:pointer;">发送短信</label>
					<!-- <input type='checkbox' id='sendSms_a' name='sendSms_a' class="toolbar-check" />发送邮件&nbsp;&nbsp;
					<input type='checkbox' id='sendSms_b' name='sendSms_b' class="toolbar-check" />站内信息&nbsp;&nbsp; -->
				</div>
			</td>
		</tr>
	</table>
	<div id="sendCont" name="sendCont" style="display:none;" >
		<table width="100%" border="0" cellspacing="0" class="DealProcess">
			<tr>
				<td>
					<label class="LabName"><span>手机号码：</span></label>
					<div class="Check_Radio"><input id="otherMobileNums" name="otherMobileNums" class="inp1" style="width:711px; *width:706px;" type="text" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" defaultValue="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔"></div>
				</td>
			</tr>
			<tr class="item">
				<td>
					<label class="LabName"><span>短信内容：</span></label>
					<textarea rows="3"  id="smsContent" name="smsContent" class="area1 easyui-validatebox"	style="width:711px;" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']"></textarea>
				</td>
			</tr>
		</table>
	</div>
	
	<div style="width:100%; height: 32px; background: #f4f4f4; padding:10px 0;position:relative;">
		<div id="operateBtnMask" class="MarskDiv hide"></div>
    	<div class="BtnList">
    		<#if operateLists??>
				<#list operateLists as l>
					<a href="###" onclick="${l.operateEvent}();" class="BigNorToolBtn" style="width:60px; padding:0 12px 0 10px;"><img src="${rc.getContextPath()}/images/${l.operateEvent}.png" style="vertical-align:middle; margin:-4px 6px 0 0;" />${l.anotherName}</a>
				</#list>
			</#if>
        </div>
    </div>	
	
	<#include "/zzgl/event/workflow/select_user_event.ftl" />
</body>

<script type="text/javascript">
	$(function(){
	   var curNodeName = $('#curNodeName').val();
	   if(curNodeName == "评价") {
		   $('#remarkTr').hide();
		   $('#evaluate_level').show();
		   $('#evaluate_content').show();
	   }
	   
	   var radioList = [{'name':'处理前', 'value':'1'},{'name':'处理后', 'value':'3'}];
	   var eventSeq = "1,3";
		
	   var swfOpt = {
	   		positionId:'fileupload',//附件列表DIV的id值',
			type:'edit',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',
			ajaxData: {'bizId':${formId?c},'attachmentType':'${EVENT_ATTACHMENT_TYPE!}','eventSeq':eventSeq},
			file_types:'*.jpg;*.gif;*.png;*.jpeg;*.webp;',
			appCode:'zhsq_event',
			radio_list: radioList,
	   };
	   
	   swfUpload1 = fileUpload(swfOpt);
	   
	   var defaultMsg = $('#otherMobileNums').attr("defaultValue");
	   $('#otherMobileNums').focus(function() {  
	   		
	   		if(defaultMsg == $('#otherMobileNums').val()) $('#otherMobileNums').val("");  
			}).blur(function(){
			if($('#otherMobileNums').val() == "") $('#otherMobileNums').val(defaultMsg);  
	   });
	});
	
	//待办提交
	function subTask(){
		var flag = false;
		
		__validate(function(data){
			flag = data;
		});
		
		if(flag) {
			modleopen();
					
			$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml");
			
			$("#flowSaveForm").ajaxSubmit(function(data) {
				modleclose();
				
				var message = '提交成功！';
				
				if(data.result != "success") {
					if(data.msgWrong) {
						message = data.msgWrong;
					} else {
						message = '提交失败！';
					}
				}
				
				$.messager.alert('提示', message, 'info', function(){
					cancel();
				});
			});
		}
	}
	
	function reject(){
		$.messager.confirm('驳回提示', '确定要驳回吗？流程将返回上一操作步骤！', function(r) {
			if (r) {
				var flag = _formValidate();
        		
        		if(flag) {
        			modleopen();
					
					$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/workflow/workflowController/rejectInstance.jhtml");
					
					$("#flowSaveForm").ajaxSubmit(function(data) {
						modleclose();
						
						var message = '驳回成功！';
						
						if(data.result != "success") {
							if(data.msgWrong) {
								message = data.msgWrong;
							} else {
								message = '驳回失败！';
							}
						}
						
						$.messager.alert('提示', message, 'info', function() {
							cancel();
						});
					});
        		}
			}
		});
	}
	
	///取消
	function cancel(){
		modleclose();
		$("#operateBtnMask").removeClass("hide");//打开按钮屏蔽层，使按钮不可点击
		window.location.reload();//刷新当前页面
		<#if source?? && source = 'workPlatform'>
			//工作台调用
			var ifrs = $('iframe', top.document);
			for(var i = 0, len = ifrs.size(); i < len; i++){
				var ifr = ifrs.get(i);
				if(ifr.src.lastIndexOf("listData_todo_event.jhtml") == (ifr.src.length-"listData_todo_event.jhtml".length)){
					ifr.contentWindow.reLoadList();
					top.topDialog.closeDialog();
				}
			}
		<#elseif source?? && source = 'oldWorkPlatform'>	
			//旧工作台调用
			var ifrs = $('iframe', top.document);
			for(var i = 0, len = ifrs.size(); i < len; i++){
				var ifr = ifrs.get(i);
				if(ifr.src.lastIndexOf("listData_todo_event.jhtml") == (ifr.src.length-"listData_todo_event.jhtml".length)){
					ifr.contentWindow.reLoadList();
					parent.closeLhgdialog();
				}
			}
		</#if>
		<#if (isLoadJs?? && isLoadJs)>
			try{
				flashData();
			}catch(e){
				
			}
		<#else>
			try{
				parent.flashData();
			}catch(e){}
			window.parent.closeMaxJqueryWindow();
		</#if>
	}
	
	//执行路径判断是否需要动态选择人员
	//$("input[name='nextNode'][type='radio']").click(checkRadio());
	
	function checkRadio(obj){
		$('#userIds').val('');
		$('#userNames').html('');
		$('#curOrgIds').val('');
		$('#htmlUserIds').val('');
		//$('#htmlUserNames').val('');
		$('#htmlUserNames').html('');
		$('#htmlUserNames').hide();
		$('#userSelectBtn').show();
		
		$('#selectedNodeValue').val('');
		$('#selectedNodeDynamicSelect').val('');
		$('#selectedNodenNodeType').val('');
		
		$('#imageUpBefore').hide();
		$('#evaluate_level').hide();
		$('#evaluate_content').hide();
		
		$('#userDiv').hide();
		$('#nodeId').val($(obj).attr('nodeId'));
		$('#nodeName_').val($(obj).attr('nodeName'));
		
		$('#selectedNodeValue').val($(obj).val());
		$('#selectedNodeDynamicSelect').val($(obj).attr('dynamicSelect'));
		$('#selectedNodenNodeType').val($(obj).attr('nodeType'));
		
		//隐藏短信发送内容
		$("#sendSms_").attr("checked", false);
		$("#sendCont").hide();
		
		if($(obj).attr('dynamicSelect') == '1'){
			modleopen();
			var curnodeName_ = document.getElementById("curNodeName").value;
			var nodeName_ = document.getElementById("nodeName_").value;
			// 通过ajax请求判断是否隐藏人员选择器，如果是上报越级，隐藏人员选择器，并将值传入userIds和userNames
			$.ajax({
				type:"POST",
				url:"${rc.getContextPath()}/zhsq/workflow/workflowController/getUsersForEvent.jhtml",
				data:{
					curnodeName:curnodeName_,
					nodeName:nodeName_
				},
				success:function(data){
					modleclose();
					if(data.level == 1 || data.level == 2 || data.level == 7) {
						$('#userIds').val(data.userIds);
						$('#userNames').html(data.userNames);
						$('#curOrgIds').val(data.orgIds);
						$('#userNames').hide();
						var htmlUserIds = $('#userIds').val();
						var htmlUserOrgIds = $('#curOrgIds').val();
						var htmlUserNames = $('#userNames').html();
						if(htmlUserNames!=null && htmlUserNames.length>0){
							var htmlUserContent = "";
							var htmlUserArray = {};
							var htmlIdArray = {};
							var htmlUserOrgIdArray = {};
							var len = 0;
							htmlUserArray = htmlUserNames.split(',');
							htmlIdArray = htmlUserIds.split(',');
							htmlUserOrgIdArray = htmlUserOrgIds.split(',');
							len = htmlUserArray.length;
							
							if(len > 0){
								htmlUserContent += '<div class="Check_Radio">';
								htmlUserContent += '<p style="display:block; height:28px;">';
								htmlUserContent += '<span class="SelectAll">';
								htmlUserContent += "<input type='checkbox'  id='htmlUserCheckAll' onclick='checkAllUser()' checked/>";
								htmlUserContent += "<label style='cursor:pointer;' for='htmlUserCheckAll'>全选</label>";
								htmlUserContent += "</span>";
								htmlUserContent += '</p>';
								
								for(var index = 0; index < len; index++){
									htmlUserContent += "<input type='checkbox' name='htmlUserCheckbox' id='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] +"' onclick='checkUser()' checked/>";
									htmlUserContent += "<label style='cursor:pointer;' for='"+ htmlIdArray[index] +"'>"+htmlUserArray[index]+"</label>" + '&nbsp;&nbsp;';
								}
								
								htmlUserContent += '</div>';
							}
							$('#htmlUserNames').html(htmlUserContent);
						}
						$('#htmlUserNames').show();
						$('#htmlUserIds').val(htmlUserIds);
						$('#userDiv').show();
						$("#userSelectBtn").hide();
					} else if(data.level == 0) {
						$('#userNames').show();
						$('#htmlUserNames').hide();
						$("#userSelectBtn").show();
						$('#userDiv').show();
					} else if(data.msg == "职位未配置相关人员") {
						$.messager.alert("警告", "职位未配置相关人员！", "warning");
						$('#userNames').show();
						$('#htmlUserNames').hide();
						$("#userSelectBtn").show();
						$('#userDiv').show();
					}
				}
			});
		}
	
		if($('#nodeName_').val()=="结案"){
			$('#imageUpBefore').show();
		}else if($('#nodeName_').val()=="归档"){
			$('#remarkTr').hide();
			$('#evaluate_level').show();
			$('#evaluate_content').show();
		}
	}
	
	function selectUser_(_hiddenId, _fileldId, _curOrgIds, _nodeType, nodeId){
		
		if(_nodeType=='fork' && !$("#"+nodeId).attr('checked')) {
			$.messager.alert('消息提示','请先勾选相应的分支路径!'); 
		}else{
			selectUser(_hiddenId, _fileldId, _curOrgIds, _nodeType, nodeId);
		}
	}
	
	function __validate(callback){
		
		var validate = true;
	
    	//执行路径
    	var execPath=$("#selectedNodeValue").val();
    	
    	if(isBlankString(execPath)){
    		$.messager.alert('提示', "请选择下一环节!", 'info');
            validate = false;
        }else if($("#selectedNodenNodeType").val()=='1'&&
        		$("#selectedNodeDynamicSelect").val()=='1'){
        	var userIds = $("#userIds").val();
        	
        	if(isBlankString(userIds)){
        		$.messager.alert('提示', "请选择办理人员!", 'info');
	            validate = false;
        	}
        }
        
        if(validate) {
        	validate = _formValidate();
		}
        
	    callback(validate);
	}
	
	function _formValidate() {
		var validate = false;
		
		$('#advice').validatebox({
    		required: $("#advice").is(":visible")
    	});
    	
    	$('#evaContent').validatebox({
    		required: $("#evaContent").is(":visible")
    	});
    	
    	$('#smsContent').validatebox({
    		required: $("#smsContent").is(":visible")
    	});
    	
    	validate = $("#flowSaveForm").form('validate');
    	
    	if(validate) {
    		var mobileValid = "",
				advice = $("#advice").val(),
				evaContent = $("#evaContent").val(),
				attachments = "",
				attachmentInpt = $('#flowSaveForm input[name="attachmentId"]');
			
			if(attachmentInpt && attachmentInpt.length) {
				attachmentInpt.each(function() {
					attachments += "," + $(this).val();
				});
				
				attachments = attachments.substr(1);
				
				$("#attachmentIds").val(attachments);
			}
			
			if($("#otherMobileNums").is(":visible")) {
				var defaultMsg = $("#otherMobileNums").attr("defaultValue"),
					otherMobileNums = $("#otherMobileNums").val();
				
				if(defaultMsg != otherMobileNums) {
					var mobileArray = otherMobileNums.split(","),
						mobile = null,
						msgFail = "";
					
					for(var index = 0, len = mobileArray.length; index < len; index++) {
						mobile = mobileArray[index];
						
						if(mobile) {
							if(isMobile(mobile)) {
								mobileValid += "," + mobile;
							} else {
								msgFail += "[" + mobile + "] ";
							}
						}
					}
					
					if(msgFail.length > 0) {
						validate = false;
						$.messager.alert("警告", "存在如下不合理的手机号码：" + msgFail, "warning");
					} else if(mobileValid.length > 0) {
						mobileValid = mobileValid.substr(1);
					}
				}
			}
			
			$("#otherMobileNumsHidden").val(mobileValid);
			
			if(!advice && evaContent) {
				$("#advice").val(evaContent);
			}
    	}
    	
    	return validate;
	}
	
	function showImage(){
		var formId =$("#formId").val();
		var url='${rc.getContextPath()}/zhsq/event/eventDisposalController/toUploadMultiFiles.jhtml?eventId='+formId;
		showMaxJqueryWindow('上传图片',url,800,400);
	}
	
	function showEvaluate(){
		var formId =$("#formId").val();
		var url='${rc.getContextPath()}/zhsq/event/eventDisposalController/toCommentEvent.jhtml?eventId='+formId; 
		showMaxJqueryWindow('评价',url,800,400);
	}
	
	function closeWinDialog(){
		closeMaxJqueryWindow();
	}
	
	function showSmsCont() {
		if($("#sendSms_").attr("checked")){
			var flag = false;
			__validate(function(data){
				flag = data;
				$("#sendSms_").attr("checked", data);
			});
			if(flag){
				modleopen();
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml',
					data: 'formId='+ $("#formId").val()+ "&taskId="+ $("#taskId").val() +'&curnodeName='+ $("#curNodeName").val() +"&nodeName="+ $("#nodeName_").val(),
					dataType:"json",
					success: function(data){
						var smsContent = data.smsContent;
						smsContent = smsContent.replace('@advice',$("#advice").val());
						$("#smsContent").val(smsContent);
						$("#sendCont").show();
						modleclose();
					},
					error:function(data){
						modleclose();
						$.messager.alert('错误','连接错误！','error');
						$("#sendSms_").attr("checked", false);
					}
		    	});
			}
		}else{
			$("#sendCont").hide();
			$("#smsContent").val('');
			$("#otherMobileNums").val('');
		}
	}
	
	function checkUser(isShowMsg){
		var userIds = "";
		var userOrgIds = "";
		var userCheckbox = $("input[name='htmlUserCheckbox'][type='checkbox']:checked");
		var allCheckBoxLen = $("input[name='htmlUserCheckbox'][type='checkbox']").length;
		var checkedBoxLen = userCheckbox.length;
		
		userCheckbox.each(function() {
			userIds += "," + $(this).attr("id");
			userOrgIds += "," + $(this).attr("orgid");
		});
		
		$("#htmlUserCheckAll").attr('checked',allCheckBoxLen == checkedBoxLen);
		
		if(userIds.length > 0){
			userIds = userIds.substr(1);
			userOrgIds = userOrgIds.substr(1);
		}else if(isShowMsg != false){
			$.messager.alert('提示', "请选择办理人员!", 'info');
		}
		
		$('#userIds').val(userIds);
		$('#curOrgIds').val(userOrgIds);
	}
	
	function checkAllUser(){
		var userCheckbox = {};
		var isCheckAll = $("#htmlUserCheckAll").attr("checked"); 
		userCheckbox = $("input[name='htmlUserCheckbox'][type='checkbox']");
		
		for(var index = 0, len = userCheckbox.length; index < len; index++){
			userCheckbox[index].checked = isCheckAll;
		}
		
		checkUser(isCheckAll=='checked');
	}
</script>

</html>