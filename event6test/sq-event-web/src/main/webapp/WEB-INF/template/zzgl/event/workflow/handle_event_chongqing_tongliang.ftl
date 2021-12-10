<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="handlePageTitle">重庆铜梁区事件办理页面</@block></title>

	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<#include "/component/bigFileUpload.ftl" />
	
	<@block name="extraJs"></@block>
	
	<style type="text/css">
		.DealProcess{background-color:#F9F9F9;}
		.DealProcess td{padding-bottom:10px;}
		.DealProcess .LabName{margin-left: 10px; width:75px;}
		.DealMan{width:630px; line-height:24px;}
		.MarskDiv{width:844px; height:52px; position:absolute; z-index:2; top:0; left:0;}
		/*办理人员全选样式*/
		.SelectAll{padding:2px 8px; *padding:0 8px 2px 5px; display:block; float:left; border-radius:3px; background:#2dcc70; color:#fff; font-weight:normal; font-size:12px;}
		.SelectAll:hover{background:#27ae61;}
		
		
		.star-five-grey{
			width: 0;
			height: 0;
			color: grey;
			margin: 50px 0;
			position: relative;
			display: block;
			border-left: 100px solid transparent;
			border-right: 100px solid transparent;
			border-bottom: 70px solid grey;
			-moz-transfrom:rotate(35deg);
			-webkit-transform:rotate(35deg);
			-ms-transform:rotate(35deg);
			-o-transform:rotate(35deg);
		}
		
			
		.star-five-grey:before{
			width: 0;
			height: 0;
			border-left: 30px solid transparent;
			border-right: 30px solid transparent;
			border-bottom: 80px solid grey;
			position: absolute;
			top: -45px;
			left: -65px;
			color: white;
			display: block;
			content: "";
			-moz-transform:rotate(-35deg);
			-webkit-transform:rotate(-35deg);
			-ms-transform:rotate(-35deg);
			-o-transform:rotate(-35deg);
		}
		
		.star-five-grey:after{
			width: 0;
			height: 0;
			display: block;
			position: absolute;
			color: grey;
			top: 3px;
			left: -105px;
			border-left: 100px solid transparent;
			border-right: 100px solid transparent;
			border-bottom: 70px solid grey;
			content: "";
			-moz-transform:rotate(-70deg);
			-webkit-transform:rotate(-70deg);
			-ms-transform:rotate(-70deg);
			-o-transform:rotate(-70deg);
		}
		
		
		
		.star-five-red{
			width: 0;
			height: 0;
			color: red;
			margin: 50px 0;
			position: relative;
			display: block;
			border-left: 100px solid transparent;
			border-right: 100px solid transparent;
			border-bottom: 70px solid red;
			-moz-transfrom:rotate(35deg);
			-webkit-transform:rotate(35deg);
			-ms-transform:rotate(35deg);
			-o-transform:rotate(35deg);
		}
		
			
		.star-five-red:before{
			width: 0;
			height: 0;
			border-left: 30px solid transparent;
			border-right: 30px solid transparent;
			border-bottom: 80px solid red;
			position: absolute;
			top: -45px;
			left: -65px;
			color: white;
			display: block;
			content: "";
			-moz-transform:rotate(-35deg);
			-webkit-transform:rotate(-35deg);
			-ms-transform:rotate(-35deg);
			-o-transform:rotate(-35deg);
		}
		
		.star-five-red:after{
			width: 0;
			height: 0;
			display: block;
			position: absolute;
			color: red;
			top: 3px;
			left: -105px;
			border-left: 100px solid transparent;
			border-right: 100px solid transparent;
			border-bottom: 70px solid red;
			content: "";
			-moz-transform:rotate(-70deg);
			-webkit-transform:rotate(-70deg);
			-ms-transform:rotate(-70deg);
			-o-transform:rotate(-70deg);
		}
		
	</style>
	
	<@block name="extraCss"></@block>
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
	<input type="hidden" id="nodeCode" name="nodeCode" />
	
	<input type="hidden" id="selectedNodeValue" name="selectedNodeValue" value=""> 
	<input type="hidden" id="selectedNodeDynamicSelect" name="selectedNodeDynamicSelect" value=""> 
	<input type="hidden" id="selectedNodenNodeType" name="selectedNodenNodeType" value=""> 
	
	<input type="hidden" id="beforeNodeName" name="beforeNodeName" value="<#if backTask??>${backTask.taskName!}</#if>"> 
	<input type="hidden" id="workFlowId" name="workFlowId" value="${workFlowId?c}"> 
	
	<input type="hidden" id="specificUrl" winName="" value="" />
	
	<input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
	
	<!--流程有关-->
	<table id="_handleEventTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="DealProcess">
		<tr id="tr_epath">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>下一环节：</span></label>
				<#if taskNodes?? && (taskNodes?size > 0) >
					<div class="Check_Radio" style="margin-top:7px;">
						<#list taskNodes as node>
							<span>
								<input type="radio" id="_node_${node.nodeId?c}" name="nextNode" onclick="checkRadio(this);"
								nodeName="${node.nodeName}" nodeType="${node.nodeType}" nodeCode="${node.transitionCode!''}"
								dynamicSelect="<#if node.dynamicSelect??>${node.dynamicSelect}</#if>" nodeId="${node.nodeId?c}" 
								value="${node.nodeName}"><label for="_node_${node.nodeId?c}" style="cursor:pointer"><#if node.nodeNameZH??>${node.nodeNameZH}<#else>${node.nodeName}</#if></label></input>
							</span>
						</#list>
					</div>
				<#else>
					<div class="Check_Radio FontRed" style="margin-top:7px;">
						<b>
							${emptyTip4NextTaskNode!'没有可办理的下一环节！'}
						</b>
					</div>
				</#if>
			</td>
		</tr>
		
		 <tr id="userDiv" style="display: none;"> 
		    <td>
			    <label class="LabName"><span><label class="Asterik">*</label>办理人员：</span></label>
			    <div class="Check_Radio">
			    	<a href="###" class="NorToolBtn EditBtn" id="userSelectBtn" onclick="_selectHandler();">选择办理人</a>
				</div>
				<input type="hidden" name="nextStaffId" id="userIds"/>
				<input type="hidden" name="curOrgIds" id="curOrgIds"/>
				<input type="hidden" id="curOrgNames" />
				<div class="FontDarkBlue fl DealMan Check_Radio"><b name="userNames" id="userNames"></b></div>
		    	<div class="FontDarkBlue fl DealMan"><b name="htmlUserNames" id="htmlUserNames"></b></div>
				<input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
				<!-- <input type="text" name="htmlUserNames" id="htmlUserNames" class="mustinput" readonly="readonly" style="width: 250px !important;"/> -->
		    </td>
    	</tr> 
		<tr id="imageUpload">
			<td><label class="LabName"><span>附件上传：</span></label><div id="bigFileUploadDiv"></div></td>
		</tr>
		<@block name="otherFillingItem"></@block>
		<#if evaLevelDict??>
	        <tr id="evaluate_level" style="display: none;"> 
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>评价等级：</span></label>
					<div class="Check_Radio" style="margin-top:7px;">
						<#list evaLevelDict as evaItem>
							<div title="${evaItem.dictName}" class="star-five-grey evaDiv" style="zoom:0.09;float:left;margin-left:25px;cursor: pointer;" evaLevel="${evaItem.dictGeneralCode}" evaName="${evaItem.dictName}"></div>
						</#list>
					</div>
					<label class="LabName"><span id="evaLevelStr" class="FontDarkBlue"></span></label>
					<input type="hidden" name="evaLevel" id="evaLevel"/>
				</td>
	        </tr>
        </#if>
		<tr id="evaluate_content" style="display: none;">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>评价意见：</span></label>
				<textarea rows="3" style="width: 710px;height:50px;" id="evaContent" name="evaContent" class="area1 autoDoubleCell easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"></textarea>
			</td>
		</tr>
		<tr id="remarkTr">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>办理意见：</span></label>
				<textarea rows="3" style="width: 710px;height:50px;" id="advice" name="advice" class="area1 autoDoubleCell easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
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
					<input type="hidden" id="otherMobileNumsHidden" name="otherMobileNums" value="" />
					<label class="LabName"><span>手机号码：</span></label>
					<input id="otherMobileNums" class="inp1 autoDoubleCell" style="width:711px; *width:706px;" type="text" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" defaultValue="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔">
				</td>
			</tr>
			<tr class="item">
				<td>
					<label class="LabName"><span>短信内容：</span></label>
					<textarea rows="3"  id="smsContent" name="smsContent" class="area1 autoDoubleCell easyui-validatebox"	style="width:711px;" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']"></textarea>
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
		
	<#include "/zzgl/event/workflow/select_user.ftl" />
</body>

<script type="text/javascript">
	var g_EventNodeCode = null;// 全局用于存放事件节点信息
	
	$(function() {
	   var curNodeName = $('#curNodeName').val(),
	   	   winWidth = $(window).width();
	   	   
	   if(curNodeName == "评价") {
	   	   $("#imageUpload").hide();
		   $('#remarkTr').hide();
		   $('#evaluate_level').show();
		   $('#evaluate_content').show();
	   }
	   
	   var bigFileUploadOpt = {
        	useType: 'edit',
        	<@block name="fileTypes">
        	fileExt: '.jpg,.gif,.png,.jpeg,.webp',
        	</@block>
        	<@block name="maxSingleFileSize">
        	maxSingleFileSize: 10,
        	</@block>
        	attachmentData: {'bizId':${formId?c},'attachmentType':'${EVENT_ATTACHMENT_TYPE!}','eventSeq':'1,2,3', 'isBindBizId': 'yes'},
        	module: 'event',
        	styleType: 'list',
        	individualOpt : {
        		isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
        	}
        };

        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        
        $('#bigFileUploadDiv').delay(500).promise().done(defaultRadioCheck);//延时加载设置默认选中项(延时0.5秒)，为了能在图片上传控件加载完成后，才调用
        
		//自动调整需要自适应宽度的组件，由于受滚动条组件的影响，会加载多次，从而添加isSettledAutoWidth用于防止多次初始化
		$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
			$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
				   .addClass("isSettledAutoWidth");
		});
			
		var defaultMsg = $('#otherMobileNums').attr("defaultValue");
		$('#otherMobileNums').focus(function(){  
			if(defaultMsg == $('#otherMobileNums').val()) $('#otherMobileNums').val("");  
			}).blur(function(){
			if($('#otherMobileNums').val() == "") $('#otherMobileNums').val(defaultMsg);  
		});
	});
	
	function subTask() {
		var url = "";
		
		<#if bizDetailUrl??>
			url = $("#specificUrl").val();
		</#if>
		
		if(url) {
			var flag = true;
		
			__validate(function(data){
				flag = data;
			});
			
			if(flag) {
				url += $("#formId").val();
				
				url = url.replace('$bizId', '${eventReportBizId!}');
				
				showMaxJqueryWindow($("#specificUrl").attr("winName"), url, 650, 355);
			}
		} else {
			subTaskCallBack();
		}
	}
	
	//待办提交
	function subTaskCallBack() {
		var url = $("#specificUrl").val(),
			flag = true;
		
		if(url) {//关闭打开的个性表单窗口
			closeMaxJqueryWindow();
		}
		
		__validate(function(data){
			flag = data;
		});
		
		if(flag) {
			flag = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
		}
		
		<@block name="attachmentCheck"></@block>
		
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
	
	function reject() {
		$.messager.confirm('驳回提示', '确定要驳回吗？流程将返回上一操作步骤！', function(r) {
			if (r) {
				var flag = _formValidate();
				
				if(flag) {
					flag = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
				}
				
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
	
	function feedback() {
		var flag = _formValidate();
		
		if(flag) {
			modleopen();
			$.ajax({
				type: "POST",
				url : "${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml",
				data: {'taskId': $("#taskId").val(), 'deploymentId': $("#deploymentId").val(), 'instanceId': $("#instanceId").val(), 'advice': $("#advice").val(), 'nextNode': 'task0', 'attachmentId': $("#attachmentIds").val()},
				dataType:"json",
				success: function(data) {
					modleclose();
					var message = '提交成功！';
					
					if(data.result != "success") {
						message = data.msgWrong || '提交失败！';
					}
					
					$.messager.alert('提示', message, 'info', function() {
						cancel();
					});
				},
				error:function(data) {
					$.messager.alert('错误','连接错误！','error');
				}
			});
		}
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
	
	function defaultRadioCheck(){
		//设置默认选中第一个选项
		<#if taskNodes??>
			<#list taskNodes as node>
				<#if node_index == 0>
					var radioCheck = $('#_node_${node.nodeId}');
					$(radioCheck).attr('checked', true);
					checkRadio(radioCheck);
				</#if>
			</#list>
		</#if>
	}
	
	//执行路径判断是否需要动态选择人员
	//$("input[name='nextNode'][type='radio']").click(checkRadio());
	
	function checkRadio(obj) {
		modleopen();
		g_EventNodeCode = null;
		var curnodeName_ = $("#curNodeName").val(),
			nodeName_ = $(obj).attr('nodeName'),
			nodeCode_ = $(obj).attr('nodeCode'),
			nodeId_ = $(obj).attr('nodeId'),
			eventId_ = $("#formId").val();
		
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
		
		$('#evaluate_level').hide();
		$('#evaluate_content').hide();
		
		$('#userDiv').hide();
		$('#nodeId').val(nodeId_);
		$('#nodeName_').val(nodeName_);
		$('#nodeCode').val(nodeCode_);
		
		$('#selectedNodeValue').val($(obj).val());
		$('#selectedNodeDynamicSelect').val($(obj).attr('dynamicSelect'));
		$('#selectedNodenNodeType').val($(obj).attr('nodeType'));
		
		//隐藏短信发送内容
		$("#sendSms_").attr("checked", false);
		$("#sendCont").hide();
		
		// 通过ajax请求判断是否隐藏人员选择器，如果是上报越级，隐藏人员选择器，并将值传入userIds和userNames
		$.ajax({
			type:"post",
			url:"${rc.getContextPath()}/zhsq/keyelement/keyElementController/getNodeInfoForEvent.jhtml",
			data:{
				eventId : eventId_,
				curnodeName: curnodeName_,
				nodeName: nodeName_,
				nodeCode: nodeCode_,
				nodeId: nodeId_
			},
			dataType:"json",
			success:function(data) {
				modleclose();
				if (data.msg && data.msg != "") {
					$.messager.alert("警告", data.msg, "warning");
					$('#userNames').hide();
					$('#userNames').parent().hide();
					$('#htmlUserNames').hide();
					$("#userSelectBtn").hide();
					$('#userDiv').hide();
				} else {
					g_EventNodeCode = data.eventNodeCode;
					var isCollect = false,		//采集
						isClose = false,		//结案
						isPlaceFile = false,	//归档
						isDisplayUser = data.isDisplayUser,	//是否只展示可办理人员
						isSelectUser = data.isSelectUser,	//是否选择办理人员
						isSelectOrg = data.isSelectOrg,		//是否选择组织
						isUploadHandledPic = data.isUploadHandledPic || false;//是否可上传处理后图片
					
					if($('#curNodeName').val() == "task9") {//处于评价环节
				   	    $("#imageUpload").hide();
					    $('#remarkTr').hide();
					    $('#evaluate_level').show();
					    $('#evaluate_content').show();
					    
						//给每个评价等级设置点击事件
						$('.evaDiv').click(function(){
							$('.evaDiv').each(function(){
								$(this).removeClass('star-five-red');
								$(this).addClass('star-five-grey');
							})
							$(this).prevAll().each(function(){
								$(this).removeClass('star-five-grey');
								$(this).addClass('star-five-red');
							})
							
							$(this).removeClass('star-five-grey');
							$(this).addClass('star-five-red');
							
							$('#evaLevel').val($(this).attr('evaLevel'));
							$('#evaLevelStr').html($(this).attr('evaName'));
						})
						
						
						//设置默认选中
						var evaList=$('.evaDiv');
						for(var i=0,j=evaList.length;i<j;i++){
							if((i+1)/j>=0.5){
								evaList[i].click();
								break;
							}
						}
					    
					}
					
					$("#specificUrl").val(data.specificUrl || "");
					$("#specificUrl").attr("winName", data.specificUrlName || "");
					
					if(g_EventNodeCode != undefined){
						isCollect = g_EventNodeCode.collect;			//采集
						isClose = g_EventNodeCode.close;				//结案
						isPlaceFile = g_EventNodeCode.placeFile;		//归档
					}
					
					<@block name="isShowOtherFillingItem"></@block>
					
					if (isDisplayUser) {//只展示人员信息，不可修改
						$('#userIds').val(data.userIds);
						$('#userNames').html(data.userNames);
						$('#curOrgIds').val(data.orgIds);
						$('#userNames').hide();
						$('#userNames').parent().hide();
						
						var htmlUserIds = $('#userIds').val(),
							htmlUserOrgIds = $('#curOrgIds').val(),
							htmlUserNames = $('#userNames').html();
						
						if(htmlUserNames!=null && htmlUserNames.length>0){
							var htmlUserContent = "",
								htmlUserArray = {},
								htmlIdArray = {},
								htmlUserOrgIdArray = {},
								len = 0,
								userLabelId = "";
								
							htmlUserArray = htmlUserNames.split(',');
							len = htmlUserArray.length;
							
							if(len > 0){
								htmlUserContent += '<div class="Check_Radio">';
								
								for(var index = 0; index < len; index++){
									htmlUserContent += '<span class="SelectAll" style="margin-bottom: 3px;">' + htmlUserArray[index] + '</span>';
								}
								
								htmlUserContent += '</div>';
							}
							
							$('#htmlUserNames').html(htmlUserContent);
						}
						$('#htmlUserNames').show();
						$('#htmlUserIds').val(htmlUserIds);
						$('#userDiv').show();
						$("#userSelectBtn").hide();
						$('#remarkTr').show();
					} else if (isSelectUser) {// 事件采集、指定到人、指定到组织
						$('#userIds').val(data.userIds);
						$('#userNames').html(data.userNames);
						$('#curOrgIds').val(data.orgIds);
						$('#userNames').hide();
						$('#userNames').parent().hide();
						var htmlUserIds = $('#userIds').val(),
							htmlUserOrgIds = $('#curOrgIds').val(),
							htmlUserNames = $('#userNames').html();
							
						if(htmlUserNames!=null && htmlUserNames.length>0){
							var htmlUserContent = "",
								htmlUserArray = {},
								htmlIdArray = {},
								htmlUserOrgIdArray = {},
								len = 0,
								userLabelId = "";
								
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
									userLabelId = htmlIdArray[index] + "_" + htmlUserOrgIdArray[index] + "_" + index;
									
									htmlUserContent += "<input type='checkbox' name='htmlUserCheckbox' id='"+ userLabelId +"' userid='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] +"' onclick='checkUser()' checked/>";
									htmlUserContent += "<label style='cursor:pointer;' for='"+ userLabelId +"'>"+htmlUserArray[index]+"</label>" + '&nbsp;&nbsp;';
								}
								
								htmlUserContent += '</div>';
							}
							
							$('#htmlUserNames').html(htmlUserContent);
						}
						$('#htmlUserNames').show();
						$('#htmlUserIds').val(htmlUserIds);
						$('#userDiv').show();
						$("#userSelectBtn").hide();
						$('#remarkTr').show();
					} else if(isSelectOrg) {// 下派、分流、上报或者采集人组织
						$('#userNames').parent().show();
						$('#userNames').show();
						$('#htmlUserNames').hide();
						$("#userSelectBtn").show();
						$('#userDiv').show();
						$('#remarkTr').show();
					}
					
					//图片上传的展示设置
					if (isPlaceFile) {// 归档
						$("#imageUpload").hide();
						$('#remarkTr').hide();
						$('#evaluate_level').show();
						$('#evaluate_content').show();
					} else {
						var isUploadHandlingPic = <#if isUploadHandlingPic??>${isUploadHandlingPic?c}</#if>;
						var radioList = [{'name':'处理前', 'value':'1'}];
						var radioName = "处理前";//默认选中"处理前"
						
						if(isUploadHandlingPic){
							radioList.push({'name':'处理中', 'value':'2'});
							radioName = "处理中";
						}
						
						if(isUploadHandledPic == true) {
							radioList.push({'name':'处理后', 'value':'3'});
							
							if(isClose){
								radioName = "处理后";
							}
						} else {
							if(g_EventNodeCode==undefined || isCollect){
								radioList.push({'name':'处理后', 'value':'3'});
								radioName = "处理前";
							}else if(isClose){
								radioList.push({'name':'处理后', 'value':'3'});
								radioName = "处理后";
							}else if($('#curNodeName').val() == "task9"){
								radioList.push({'name':'处理后', 'value':'3'});
								radioName = "处理后";
							}
						}
						
						$("#imageUpload").show();
						
						$('#bigFileUploadDiv').changeLabelDict(radioList);
					}
				}
			},
			error:function() {
				modleclose();
				$.messager.alert('消息提示','网络异常!');
			}
		});
	}
	
	function selectUser_(_hiddenId, _fileldId, _curOrgIds, _curOrgNames, _nodeType, nodeId) {
		if (_nodeType=='fork' && !$("#"+nodeId).attr('checked')) {
			$.messager.alert('消息提示','请先勾选相应的分支路径!'); 
		} else {
			selectUser(_hiddenId, _fileldId, _curOrgIds, _curOrgNames, _nodeType, nodeId);
		}
	}
	
	function __validate(callback) {
		
		var validate = true;
		var isPerson = false;
		var isOrganization = false;
    	//执行路径
    	var execPath=$("#selectedNodeValue").val();
    	
    	if(g_EventNodeCode != undefined){
    		isPerson = g_EventNodeCode.person;
			isOrganization = g_EventNodeCode.organization;
    	}
    	
    	if(isBlankString(execPath)){
    		$.messager.alert('提示', "请选择下一环节!", 'info');
            validate = false;
        }else if($("#selectedNodenNodeType").val()=='1'&&
        		($("#selectedNodeDynamicSelect").val()=='1' || isPerson || isOrganization || g_EventNodeCode==undefined)){
        	//selectedNodenNodeType为3时表示结束节点；为1时表示办理节点
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
				
				var nodeId = $('#_handleEventTable input[type=radio][name=nextNode]:checked').attr('nodeId');
				
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml',
					data: {'formId': $("#formId").val(), 'taskId': $("#taskId").val(), 'curnodeName': $("#curNodeName").val(), 'nodeName': $("#nodeName_").val(), 'advice': $("#advice").val(), 'receiverIds': $("#userIds").val(), 'nodeId': nodeId},
					dataType:"json",
					success: function(data){
						var msgWrong = data.msgWrong;
						
						if(msgWrong) {
							$.messager.alert('错误', msgWrong, 'error');
							$("#sendSms_").attr("checked", false);
						} else {
							var smsContent = data.smsContent || '',
								receiverMobilePhones = data.receiverMobilePhones || $('#otherMobileNums').attr("defaultValue");
							
							$("#smsContent").val(smsContent);
							$("#otherMobileNums").val(receiverMobilePhones);
							
							$("#sendCont").show();
						}
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
			userIds += "," + $(this).attr("userid");
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
	
	//打开人员选择窗口
	function _selectHandler() {
		var obj = {
			<@block name="selectUserInitExtraParam"></@block>
			nextUserIdElId 			: 'userIds',
			nextUserNameElId 	: 'userNames',
			nextOrgIdElId 			: 'curOrgIds',
			nextOrgNameElId 	: 'curOrgNames',
			nextNodeType 			: 'task',
			formId						: $("#formId").val(),
			formType					: $("#formType").val(),
			instanceId					: $("#instanceId").val()
		};
		
		selectUserByObj(obj);
	}
</script>

</html>