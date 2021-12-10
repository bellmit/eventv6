<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><@block name="handlePageTitle">新办理事件</@block></title>

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
		.DealManDiv {width: calc(100% - 85px);}
	</style>
	
	<@block name="extraCss"></@block>
</head>

<body>
	<div id="handleWanzhouquDiv">
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
		<input type="hidden" id="attachLabelDictObj" value="" />
		
		<input type="hidden" id="nextHandlerJson" name="nextHandlerJson" />
		<input type="hidden" id="planConfigId" name="planConfigId" />
		<input type="hidden" id="isPlan" name="isPlan" />
		
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
			<tr id="remarkTr">
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>批示意见：</span></label>
					<textarea rows="3" style="width: 710px;height:50px;" id="advice" name="advice" class="area1 autoDoubleCell easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']"></textarea>
				</td>
			</tr>
			<tr class="warning hide">
				<td>
					<label class="LabName"><span><label class="Asterik warningAsterik hide">*</label>预案类型：</span></label>
					<input type="hidden" class="warningInput" id="planType" name="planType" />
					<input type="text" id="planTypeName" class="inp1 warningInput easyui-validatebox" style="width: 112px;" data-options="tipPosition:'bottom'" />
				</td>
			</tr>
			<tr class="warning hide">
				<td>
					<label class="LabName"><span><label class="Asterik warningAsterik hide">*</label>预案等级：</span></label>
					<input type="hidden" class="warningInput" id="planLevel" name="planLevel" />
					<input type="text" id="planLevelName" class="inp1 warningInput easyui-validatebox" style="width: 112px;" data-options="tipPosition:'bottom'" />
				</td>
			</tr>
			<tr class="warning hide">
				<td>
					<label class="LabName"><span><label class="Asterik warningAsterik hide">*</label>影响程度：</span></label>
					<div class="Check_Radio">
						<input type="radio" id="influenceLevel_1" name="influenceLevel" value="1" style="cursor: pointer;"/><label for="influenceLevel_1" style="cursor: pointer; margin-right: 5px;"><img src="${rc.getContextPath()}/images/influenceLevel/1.png" style="margin:0 10px 0 0; width:28px; height:28px;" /></lable>
					</div>
					<div class="Check_Radio">
						<input type="radio" id="influenceLevel_2" name="influenceLevel" value="2" style="cursor: pointer;"/><label for="influenceLevel_2" style="cursor: pointer; margin-right: 5px;"><img src="${rc.getContextPath()}/images/influenceLevel/2.png" style="margin:0 10px 0 0; width:28px; height:28px;" /></lable>
					</div>
					<div class="Check_Radio">
						<input type="radio" id="influenceLevel_3" name="influenceLevel" value="3" style="cursor: pointer;"/><label for="influenceLevel_3" style="cursor: pointer; margin-right: 5px;"><img src="${rc.getContextPath()}/images/influenceLevel/3.png" style="margin:0 10px 0 0; width:28px; height:28px;" /></lable>
					</div>
				</td>
			</tr>
			<tr class="userDiv hide"> 
			    <td>
				    <label class="LabName"><span><label class="Asterik">*</label>牵头领导：</span></label>
				    <div class="Check_Radio">
				    	<a href="###" class="NorToolBtn EditBtn userSelectBtn" onclick="_selectHandler();">选择牵头领导</a>
					</div>
					<input type="hidden" class="userIds" name="nextStaffId" id="userIds" userType="1" />
					<input type="hidden" class="curOrgIds" name="curOrgIds" id="curOrgIds" userType="1" />
					<input type="hidden" class="curOrgNames" id="curOrgNames" userType="1" />
			    	<div class="FontDarkBlue fl DealMan"><b class="htmlUserNames" name="htmlUserNames" id="htmlUserNames" userType="1"></b></div>
					<input type="hidden" class="htmlUserIds" name="htmlUserIds" id="htmlUserIds" userType="1"/>
			    </td>
	    	</tr>
	    	<tr class="userNameTr hide"> 
			    <td>
			    	<label class="LabName"></label>
			    	<div class="FontDarkBlue fl DealMan Check_Radio DealManDiv">
			    		<input type="hidden" class="userNames" id="userNames" name="userNames" userType="1" />
			    	</div>
			    </td>
	    	</tr>
	    	<tr class="userDiv hide"> 
			    <td>
				    <label class="LabName"><span>配合领导：</span></label>
				    <div class="Check_Radio">
				    	<a href="###" class="NorToolBtn EditBtn userSelectBtn" onclick="_selectHandler({nextUserIdElId:'coordinateLeaderIds',nextUserNameElId:'coordinateLeaderNames',nextOrgIdElId:'coordinateLeaderOrgIds',nextOrgNameElId:'coordinateLeaderOrgNames'});">选择配合领导</a>
					</div>
					<input type="hidden" class="userIds" id="coordinateLeaderIds" userType="2" />
					<input type="hidden" class="curOrgIds" id="coordinateLeaderOrgIds" userType="2" />
					<input type="hidden" class="curOrgNames" id="coordinateLeaderOrgNames" userType="2" />
			    	<div class="FontDarkBlue fl DealMan"><b class="htmlUserNames" id="htmlCoordinateLeaderNames" userType="2"></b></div>
					<input type="hidden" class="htmlUserIds" id="htmlCoordinateLeaderIds" userType="2"/>
			    </td>
	    	</tr>
	    	<tr class="userNameTr hide"> 
			    <td>
			    	<label class="LabName"></label>
			    	<div class="FontDarkBlue fl DealMan Check_Radio DealManDiv">
			    		<input type="hidden" class="userNames" id="coordinateLeaderNames" userType="2" />
			    	</div>
			    </td>
	    	</tr>
	    	<tr class="userDiv hide"> 
			    <td>
				    <label class="LabName"><span>主办科室：</span></label>
				    <div class="Check_Radio">
				    	<a href="###" class="NorToolBtn EditBtn userSelectBtn" onclick="_selectHandler({nextUserIdElId:'hostDepartmentIds',nextUserNameElId:'hostDepartmentNames',nextOrgIdElId:'hostDepartmentOrgIds',nextOrgNameElId:'hostDepartmentOrgNames'});">选择主办科室</a>
					</div>
					<input type="hidden" class="userIds" id="hostDepartmentIds" userType="3" />
					<input type="hidden" class="curOrgIds" id="hostDepartmentOrgIds" userType="3" />
					<input type="hidden" class="curOrgNames" id="hostDepartmentOrgNames" userType="3"/>
			    	<div class="FontDarkBlue fl DealMan"><b class="htmlUserNames" id="htmlHostDepartmentNames" userType="3"></b></div>
					<input type="hidden" class="htmlUserIds" id="htmlHostDepartmentIds" userType="3"/>
			    </td>
	    	</tr>
	    	<tr class="userNameTr hide"> 
			    <td>
			    	<label class="LabName"></label>
			    	<div class="FontDarkBlue fl DealMan Check_Radio DealManDiv">
			    		<input type="hidden" class="userNames" id="hostDepartmentNames" userType="3" />
			    	</div>
			    </td>
	    	</tr>
	    	<tr class="userDiv hide"> 
			    <td>
				    <label class="LabName"><span>配合科室：</span></label>
				    <div class="Check_Radio">
				    	<a href="###" class="NorToolBtn EditBtn userSelectBtn" onclick="_selectHandler({nextUserIdElId:'coordinateDepartmentIds',nextUserNameElId:'coordinateDepartmentNames',nextOrgIdElId:'coordinateDepartmentOrgIds',nextOrgNameElId:'coordinateDepartmentOrgNames'});">选择配合科室</a>
					</div>
					<input type="hidden" class="userIds" id="coordinateDepartmentIds" userType="4" />
					<input type="hidden" class="curOrgIds" id="coordinateDepartmentOrgIds" userType="4" />
					<input type="hidden" class="curOrgNames" id="coordinateDepartmentOrgNames" userType="4"/>
			    	<div class="FontDarkBlue fl DealMan"><b class="htmlUserNames" id="htmlCoordinateDepartmentNames" userType="4"></b></div>
					<input type="hidden" class="htmlUserIds" id="htmlCoordinateDepartmentIds" userType="4"/>
			    </td>
	    	</tr>
	    	<tr class="userNameTr hide"> 
			    <td>
			    	<label class="LabName"></label>
			    	<div class="FontDarkBlue fl DealMan Check_Radio DealManDiv">
			    		<input type="hidden" class="userNames" id="coordinateDepartmentNames" userType="4" />
			    	</div>
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
								<span><input type="radio" name="evaLevel" value="${evaItem.dictGeneralCode}" id="eva_${evaItem.dictId?c}" <#if evaItem.dictGeneralCode=='02'>checked</#if>/><label for="eva_${evaItem.dictId?c}" style="cursor:pointer;">${evaItem.dictName}</label></span>
							</#list>
						</div>
					</td>
		        </tr>
	        </#if>
			<tr id="evaluate_content" style="display: none;">
				<td>
					<label class="LabName"><span><label class="Asterik">*</label>评价意见：</span></label>
					<textarea rows="3" style="width: 710px;height:50px;" id="evaContent" name="evaContent" class="area1 autoDoubleCell easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']"></textarea>
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
			<input type="hidden" id="receiverIds" name="receiverIds" />
			
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
				
				<a href="###" onclick="prewarning();" class="BigNorToolBtn warning hide" style="width:100px; padding:0 12px 0 10px;"><img src="${rc.getContextPath()}/images/topeople2.png" style="vertical-align:middle; margin:-4px 6px 0 0;" />发布预警</a>
				<a href="###" onclick="warning();" class="BigNorToolBtn warning hide" style="width:100px; padding:0 12px 0 10px;"><img src="${rc.getContextPath()}/images/finish2.png" style="vertical-align:middle; margin:-4px 6px 0 0;" />启动预案</a>
	        </div>
	    </div>	
			
		<#include "/zzgl/event/workflow/select_user.ftl" />
	</div>
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
        
        <@block name="bigFileUploadOption"></@block>
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
        
        $('#bigFileUploadDiv').delay(500).promise().done(defaultRadioCheck);//延时加载设置默认选中项(延时0.5秒)，为了能在图片上传控件加载完成后，才调用
        
        AnoleApi.initListComboBox("planTypeName", "planType", "A001139", null, null, {
        	OnChanged : _capPlanConfigStaff
        });
		AnoleApi.initListComboBox("planLevelName", "planLevel", "A001140", null, null, {
			OnChanged : _capPlanConfigStaff
		});
        
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
		
		_formValidate4Warning(false, $('#sendSms_').is(':checked'));
		
		flag = reflashSmscontent($('#sendSms_').is(':checked'));
		
		if(flag) {
			flag = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
		}
		
		<@block name="attachmentCheck"></@block>
		
		if(flag) {
			_submitTask(flag);
		}
	}
	
	function _submitTask() {
		modleopen();
		
		_nextHandlerJsonConstructor();
		
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
				parent.flashData(null, true);
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
		
		if(nodeName_ == 'taskCoordinate') {
			$('#handleWanzhouquDiv .warning').show();
		} else {
			$('#handleWanzhouquDiv .warning').hide();
			$('#handleWanzhouquDiv input.warningInput').each(function() {
				$(this).val('');
			});
		}
		
		$('#handleWanzhouquDiv input.warningInput.easyui-validatebox').each(function() {
			$(this).validatebox({
				required: false
			});
		});
		
		//为了使得按钮显示、隐藏之后仍能居中展示
		$("#handleWanzhouquDiv .BtnList").each(function(){
			/*15寸电脑“更改文本、应用等项目的大小”设置为125%时，首尾按钮通过.width()获取宽度时会少一个像素，有设置固定宽度的，不会有该问题*/
			var x = $(this).find("a").length > 1 ? 2 : 0;
			$(this).find("a:visible").each(function() {
				x=x+$(this).outerWidth(true)+1;
			});
			$(this).css("width",x);
		});
		
		$('#_handleEventTable').find('.userIds,.userNames,.curOrgIds,.curOrgNames,.htmlUserIds').each(function() {
			$(this).val('');
		});
		$('#_handleEventTable .htmlUserNames').each(function() {
			$(this).html('');
		});
		$('#_handleEventTable .htmlUserNames').hide();
		$('#_handleEventTable .userSelectBtn').show();
		
		$('#selectedNodeValue').val('');
		$('#selectedNodeDynamicSelect').val('');
		$('#selectedNodenNodeType').val('');
		
		$('#evaluate_level').hide();
		$('#evaluate_content').hide();
		
		$('#_handleEventTable .userDiv').hide();
		$('#_handleEventTable .userNameTr').hide();
		$('#_handleEventTable .userNameTr p').remove();
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
				//nodeCode: 'U3__PJ',
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
					$("#_handleEventTable .userSelectBtn").hide();
					$('#_handleEventTable .userDiv').hide();
				} else {
					g_EventNodeCode = data.eventNodeCode;
					var isCollect = false,		//采集
						isClose = false,		//结案
						isPlaceFile = false,	//归档
						isDisplayUser = data.isDisplayUser,	//是否只展示可办理人员
						isSelectUser = data.isSelectUser,	//是否选择办理人员
						isSelectOrg = data.isSelectOrg,		//是否选择组织
						isUploadHandledPic = data.isUploadHandledPic || false;//是否可上传处理后图片
					
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
						$('#_handleEventTable .userDiv').show();
						$("#_handleEventTable .userSelectBtn").hide();
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
						$('#_handleEventTable .userDiv').show();
						$("#_handleEventTable .userSelectBtn").hide();
						$('#remarkTr').show();
					} else if(isSelectOrg) {// 下派、分流、上报或者采集人组织
						$('#userNames').parent().show();
						$('#userNames').show();
						$('#htmlUserNames').hide();
						$("#_handleEventTable .userSelectBtn").show();
						$('#_handleEventTable .userDiv').show();
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
						var labelDictInit = $('#attachLabelDictObj').val();
						var labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict;
						var labelPre = {'name':'处理前', 'value':'1'},
							labelHanding = {'name':'处理中', 'value':'2'},
							labelAfter = {'name':'处理后', 'value':'3'};
						var radioList = [labelPre], checkedLabel = labelPre;
						
						
						if(labelDictInit) {
							labelDict = JSON.parse(labelDictInit);
						} else {
							labelDictInit = labelDict;
							$('#attachLabelDictObj').val(JSON.stringify(labelDictInit));
						}
						
						if(labelDict) {
							var labelNameObj = {};
							
							for(var index in labelDict) {
								labelNameObj[labelDict[index].value] = labelDict[index].name;
							}
							
							if(labelNameObj[labelPre.value]) {
								labelPre.name = labelNameObj[labelPre.value];
							} else {
								labelPre = null;
							}
							
							if(labelNameObj[labelHanding.value]) {
								labelHanding.name = labelNameObj[labelHanding.value];
							} else {
								labelHanding = null;
							}
							
							if(labelNameObj[labelAfter.value]) {
								labelAfter.name = labelNameObj[labelAfter.value];
							} else {
								labelAfter = null;
							}
						}
						
						if(labelHanding != null && isUploadHandlingPic) {
							checkedLabel = labelHanding;
							radioList.push(labelHanding);
						}
						
						if(labelAfter != null) {
							if(isUploadHandledPic == true) {
								if(isClose) {
									checkedLabel = labelAfter;
								}
								
								radioList.push(labelAfter);
							} else {
								if(g_EventNodeCode==undefined || isCollect) {
									checkedLabel = labelAfter;
									radioList.push(labelAfter);
								}else if(isClose) {
									checkedLabel = labelAfter;
									radioList.push(labelAfter);
								}
							}
						}
						
						for(var index in radioList) {
							var checked = false;
							
							if(radioList[index].value == checkedLabel.value) {
								checked = true;
							}
							
							radioList[index].checked = checked;
						}
						
						$("#imageUpload").show();
						
						$('#bigFileUploadDiv').changeLabelDict(radioList);
						
						<@block name="radioCheckSuccessAfter"></@block>
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
        		$.messager.alert('提示', "请选择牵头领导!", 'info');
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
	
	function showSmsCont(opt) {
		if($("#sendSms_").attr("checked")){
			var flag = false;
			__validate(function(data){
				flag = data;
				$("#sendSms_").attr("checked", data);
			});
			opt = opt || {};
			
			if(flag){
				modleopen();
				
				var nodeId = $('#nodeId').val(),
					dataOption = <@block name="smsContentDataOption">{'formId': $("#formId").val(), 'taskId': $("#taskId").val(), 'curnodeName': $("#curNodeName").val(), 'nodeName': $("#nodeName_").val(), 'advice': $("#advice").val(), 'receiverIds': $('#userIds').val(), 'nodeId': nodeId, 'isCapReceiverMobilePhone': false<@block name="smsContentDataExtraOption"></@block>}</@block>;
				
				$.extend(dataOption, opt);
				
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml',
					data: dataOption,
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
	function _selectHandler(opt) {
		var obj = {
			<@block name="selectUserInitExtraParam"></@block>
			nextUserIdElId 		: 'userIds',
			nextUserNameElId 	: 'userNames',
			nextOrgIdElId 		: 'curOrgIds',
			nextOrgNameElId 	: 'curOrgNames',
			nextNodeType 		: 'task',
			formId				: $("#formId").val(),
			formType			: $("#formType").val(),
			instanceId			: $("#instanceId").val(),
			callback4Confirm	: _nextHandlerConstructor
		};
		opt = opt || {};
		
		$.extend(obj, opt);
		
		selectUserByObj(obj);
	}
	
	function _nextHandlerConstructor() {
		var userIds = $('#' + hiddenId).val(),
			userNames = $('#' + fileldId).val(),
			orgIds = $('#' + curOrgIds).val(),
			userType = $('#' + hiddenId).attr('userType');
		
		if(userIds && orgIds) {
			var userIdArray = userIds.split(','),
				userNameArray = userNames.split(','),
				orgIdArray = orgIds.split(','),
				inputId = null;
			
			$('#' + fileldId).parents('tr').show();
			$('#' + fileldId).siblings('p').hide();
			
			for(var index = 0, len = userIdArray.length; index < len; index++) {
				inputId = userIdArray[index] + '-' + orgIdArray[index] + '-' + userType + '-advice';
				
				if($('#' + inputId).length == 1) {
					$('#' + inputId).parent().show();
				} else {
					$('#' + fileldId).before('<p style="margin-bottom: 10px;"><b style="display: inline-block;width: 9%;text-align: right;">' + userNameArray[index] + '：</b><input type="text" class="inp1 easyui-validatebox" id="' + inputId + '" data-options="tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" style="width: calc(91% - 8px);box-sizing: border-box;"/></p>');
				}
			}
			
			$('#' + fileldId).siblings('p:hidden').remove();
			$.parser.parse($('#_handleEventTable'));
		} else {
			$('#' + fileldId).parents('tr').hide();
			$('#' + fileldId).siblings('p').remove();
		}
	}
	
	function _nextHandlerJsonConstructor() {
		var userType = null, userTypeArray = null;
			userIds = null, orgIds = null,
			userNames = null,
			nextHandler = {};
		
		$('#_handleEventTable .userIds').each(function() {
			userType = $(this).attr('userType');
			userTypeArray = [];
			userIds = $(this).val();
			orgIds = $('#_handleEventTable .curOrgIds[userType=' + userType + ']').eq(0).val();
			userNames = $('#_handleEventTable .userNames[userType=' + userType + ']').eq(0).val();
			
			if(userIds && orgIds) {
				var userIdArray = userIds.split(','), orgIdArray = orgIds.split(','),
					userNameArray = userNames.split(','),
					userTypeObj = null;
				
				for(var index = 0, len = userIdArray.length; index < len; index++) {
					var advice = $('#' + userIdArray[index] + '-' + orgIdArray[index] + '-' + userType + '-advice').val();
					userTypeObj = {}
					
					userTypeObj.userId = userIdArray[index];
					userTypeObj.userName = userNameArray[index];
					userTypeObj.orgId = orgIdArray[index];
					
					if(advice) {
						userTypeObj.advice = advice;
					}
					
					userTypeArray.push(userTypeObj);
				}
				
				nextHandler[userType] = userTypeArray;
			}
		});
		
		var nextHandlerJson = JSON.stringify(nextHandler);
		
		$('#nextHandlerJson').val(nextHandlerJson);
	}
	
	function prewarning() {
		_formValidate4Warning(true, true, {'noteType' : 'eventPrewarningNote', 'planTypeName': $('#planTypeName').val(), 'planLevelName' : $('#planLevelName').val()});
		
		if($('#smsContent').is(':visible')) {
			$.messager.confirm('提示', '您确定发送预警短信吗？', function(r) {
				if(r) {
					modleopen();
					
					_nextHandlerJsonConstructor();
					
					$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/sendSmsContent.jhtml");
					
					$("#flowSaveForm").ajaxSubmit(function(data) {
						modleclose();
						
						$.messager.alert('提示', '短信发送成功！', 'info', function() {
							cancel();
						});
					});
				}
			});
		}
	}
	
	function warning() {
		var flag = _formValidate4Warning(true, true, {'noteType' : 'eventWarningNote'});
		
		if(flag && $('#smsContent').is(':visible')) {
			$.messager.confirm('提示', '您确定启动预案吗？', function(r) {
				if(r) {
					$('#isPlan').val('1');
					_submitTask();
				}
			});
		}
	}
	
	function reflashSmscontent(isChecked, opt) {
		var flag = true;
		
		$('#smsContent').hide();
			
		__validate(function(data) {
			flag = data;
		});
		
		if(flag) {
			$("#sendSms_").attr("checked", isChecked);
			
			showSmsCont(opt);
			
			$('#smsContent').validatebox({
				required: isChecked
			});
			
			$('#isPlan').val('0');
			$('#smsContent').show();
		}
		
		return flag;
	}
	
	function _formValidate4Warning(isWarning, isSendSms, opt) {
		var flag = true;
		
		if(isWarning == true) {
			$('#handleWanzhouquDiv label.warningAsterik').show();
			$('#handleWanzhouquDiv input.warningInput.easyui-validatebox').each(function() {
				$(this).validatebox({
					required: true
				});
			});
			
			if($('#_handleEventTable input[type=radio][name=influenceLevel]').is(':visible') && $('#_handleEventTable input[type=radio][name=influenceLevel]:checked').length == 0) {
				$.messager.alert("警告", "请选择影响程度！", "warning");
				return false;
			}
			
			flag = reflashSmscontent(isSendSms, opt);
		} else {
			$('#handleWanzhouquDiv label.warningAsterik').hide();
			$('#handleWanzhouquDiv input.warningInput.easyui-validatebox').each(function() {
				$(this).validatebox({
					required: false
				});
			});
		}
		
		return flag;
	}
	
	function _capPlanConfigStaff(dictValue, items) {
		var planType = $('#planType').val(), planLevel = $('#planLevel').val();
		
		if(planType && planLevel) {
			$.messager.confirm('提示', '您确定获取应急预案人员配置信息吗？', function(r) {
				if(r) {
					modleopen();
					$.ajax({
						type: "POST",
						url : "${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/capPlanConfigStaff.jhtml",
						data: {'planType': $('#planType').val(), 'planLevel': $("#planLevel").val()},
						dataType:"json",
						success: function(data) {
							modleclose();
							var planConfigStaff = data.planConfigStaff;
							
							if(planConfigStaff) {
								var isUserExist = false,
									planConfigStaffObj = {},
									userType = null;
								
								for(var index = 0, len = planConfigStaff.length; index < len; index++) {
									userType = planConfigStaff[index].userType;
									
									planConfigStaffObj[userType] = planConfigStaff[index];
								}
								
								$('#_handleEventTable input.userIds').each(function() {
									if($(this).val()) {
										isUserExist = true; return;
									}
								});
								
								if(planConfigStaff.length > 0) {
									$('#planConfigId').val(planConfigStaff[0].planConfigId);
								}
								
								if(isUserExist) {
									$.messager.confirm('提示', '您确定覆盖现有人员信息吗？', function(r) {
										if(r) {
											_coverStaff(planConfigStaffObj);
										} else {
											_mergeStaff(planConfigStaffObj);
										}
									});
								} else {
									_mergeStaff(planConfigStaffObj);
								}
							} else if(data.msgWrong) {
								$.messager.alert('错误', data.msgWrong, 'error');
							}
							
						},
						error:function(data) {
							modleclose();
							$.messager.alert('错误','获取应急人员配置失败！','error');
						}
					});
				}
			});
		}
	}
	
	function _coverStaff(planConfigStaffObj) {//人员覆盖
		planConfigStaffObj = planConfigStaffObj || {};
		
		$('#_handleEventTable input.userIds').each(function() {
			var userType = $(this).attr('userType'),
				planConfigStaff = planConfigStaffObj[userType] || {},
				configUserIds = planConfigStaff.configUserIds || '',
				configUserNames = planConfigStaff.configUserNames || '',
				configOrgIds = planConfigStaff.configOrgIds || '',
				configOrgNames = planConfigStaff.configOrgNames || '';
			
			$(this).val(configUserIds);
			$('#_handleEventTable input.userNames[userType=' + userType + ']').eq(0).val(configUserNames);
			$('#_handleEventTable input.curOrgIds[userType=' + userType + ']').eq(0).val(configOrgIds);
			$('#_handleEventTable input.curOrgNames[userType=' + userType + ']').eq(0).val(configOrgNames);
			$('#_handleEventTable input.htmlUserIds[userType=' + userType + ']').eq(0).val(configUserIds);
			$('#_handleEventTable b.htmlUserNames[userType=' + userType + ']').eq(0).html(configUserNames);
			
			hiddenId = $(this).attr('id');
			fileldId = $('#_handleEventTable input.userNames[userType=' + userType + ']').eq(0).attr('id');
			curOrgIds = $('#_handleEventTable input.curOrgIds[userType=' + userType + ']').eq(0).attr('id');
			
			_nextHandlerConstructor();
		});
	}
	
	function _mergeStaff(planConfigStaffObj) {//人员合并
		if(planConfigStaffObj) {
			var curOrgIdsObj = null, userNamesObj = null, orgNamesObj = null;
			
			$('#_handleEventTable input.userIds').each(function() {
				var userType = $(this).attr('userType'),
					planConfigStaff = planConfigStaffObj[userType] || {},
					userObj = null;
				
				curOrgIdsObj = $('#_handleEventTable input.curOrgIds[userType=' + userType + ']').eq(0);
				userNamesObj = $('#_handleEventTable input.userNames[userType=' + userType + ']').eq(0);
				orgNamesObj = $('#_handleEventTable input.curOrgNames[userType=' + userType + ']').eq(0);
				
				userObj = _isConfigUserExist($(this).val(), userNamesObj.val(), curOrgIdsObj.val(), orgNamesObj.val(), planConfigStaff);
				
				$(this).val(userObj.userIds);
				userNamesObj.val(userObj.userNames);
				curOrgIdsObj.val(userObj.orgIds);
				orgNamesObj.val(userObj.orgNames);
				$('#_handleEventTable input.htmlUserIds[userType=' + userType + ']').eq(0).val(userObj.userIds);
				$('#_handleEventTable b.htmlUserNames[userType=' + userType + ']').eq(0).html(userObj.userNames);
				
				hiddenId = $(this).attr('id');
				fileldId = userNamesObj.attr('id');
				curOrgIds = curOrgIdsObj.attr('id');
				
				_nextHandlerConstructor();
			});
		}
	}
	
	function _isConfigUserExist(userIds, userNames, orgIds, orgNames, planConfigStaff) {
		planConfigStaff = planConfigStaff || {};
		
		var userIdArray = [],
			orgIdArray = [],
			userNameArray = [],
			orgNameArray = [],
			configUserIds = planConfigStaff.configUserIds || '',
			configUserNames = planConfigStaff.configUserNames || '',
			configOrgIds = planConfigStaff.configOrgIds || '',
			configOrgNames = planConfigStaff.configOrgNames || ''
			configUserIdArray = [],
			configUserNameArray = [],
			configOrgIdArray = [],
			configOrgNameArray = [],
			configUserIdRemainArray = [],
			configUserNameRemainArray = [],
			configOrgIdRemainArray = [],
			configOrgNameRemainArray = [],
			userObj = {},
			userIdKey = null;
		
		if(userIds) {
			userIdArray = userIds.split(',');
		}
		if(orgIds) {
			orgIdArray = orgIds.split(',');
		}
		if(userNames) {
			userNameArray = userNames.split(',');
		}
		if(orgNames) {
			orgNameArray = orgNames.split(',');
		}
		
		if(configUserIds) {
			configUserIdArray = configUserIds.split(',');
		}
		if(configUserNames) {
			configUserNameArray = configUserNames.split(',');
		}
		if(configOrgIds) {
			configOrgIdArray = configOrgIds.split(',');
		}
		if(configOrgNames) {
			configOrgNameArray = configOrgNames.split(',');
		}
		
		for(var index = 0, len = userIdArray.length; index < len; index++) {
			userObj[userIdArray[index] + '-'+ orgIdArray[index]] = true;
		}
		
		for(var index = 0, len = configUserIdArray.length; index < len; index++) {
			userIdKey = configUserIdArray[index] + '-' + configOrgIdArray[index];
			
			if(!userObj[userIdKey]) {
				configUserIdRemainArray.push(configUserIdArray[index]);
				configUserNameRemainArray.push(configUserNameArray[index]);
				configOrgIdRemainArray.push(configOrgIdArray[index]);
				configOrgNameRemainArray.push(configOrgNameArray[index]);
			}
		}
		
		if(userIdArray && userIdArray.length > 0) {
			configUserIdRemainArray = configUserIdRemainArray.concat(userIdArray);
		}
		if(userNameArray && userNameArray.length > 0) {
			configUserNameRemainArray = configUserNameRemainArray.concat(userNameArray);
		}
		if(orgIdArray && orgIdArray.length > 0) {
			configOrgIdRemainArray = configOrgIdRemainArray.concat(orgIdArray);
		}
		if(orgNameArray && orgNameArray.length > 0) {
			configOrgNameRemainArray = configOrgNameRemainArray.concat(orgNameArray);
		}
		
		return {
			'userIds'	: configUserIdRemainArray.join(','),
			'userNames'	: configUserNameRemainArray.join(','),
			'orgIds'	: configOrgIdRemainArray.join(','),
			'orgNames'	: configOrgNameRemainArray.join(',')
		};
	}
</script>

<#include "/component/ComboBox.ftl" />

</html>