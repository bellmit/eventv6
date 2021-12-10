<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>江西省平台办理事件</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<#include "/component/bigFileUpload.ftl" />
	<style type="text/css">
	.DealProcess{background-color:#F9F9F9;}
	.DealProcess td{padding-bottom:10px;}
	.DealProcess .LabName{margin-left: 10px; width:75px;}
	.DealMan{width:630px; line-height:24px;}
	.MarskDiv{width:844px; height:52px; position:absolute; z-index:2; top:0; left:0;}
	/*办理人员全选样式*/
	.SelectAll{padding:2px 8px; *padding:0 8px 2px 5px; display:block; float:left; border-radius:3px; background:#2dcc70; color:#fff; font-weight:normal; font-size:12px;}
	.SelectAll:hover{background:#27ae61;}
	</style>
</head>

<body>
	<input type="hidden" id="formId" name="formId" value="${formId?c}">
	<input type="hidden" id="isDetail2Edit" name="isDetail2Edit" value="false">
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
	
	<input type="hidden" id="specificUrl" winName="" value="" />
	
	<input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
	
	<!--流程有关-->
	<table id="_eventHandleTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="DealProcess">
		<tr id="tr_epath">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>下一环节：</span></label>
				<#if taskNodes??>
					<div id="taskNodesDiv" class="Check_Radio" style="margin-top:7px; width: 90%; line-height: 20px;">
						<#list taskNodes as node>
							<span>
								<input type="radio" id="_node_${node.nodeId?c}" name="nextNode" onclick="checkRadio(this);"
								nodeName="${node.nodeName}" nodeType="${node.nodeType}" nodeCode="${node.transitionCode!''}"
								dynamicSelect="<#if node.dynamicSelect??>${node.dynamicSelect}</#if>" nodeId="${node.nodeId?c}" 
								value="${node.nodeName}"><label for="_node_${node.nodeId?c}" style="cursor:pointer"><#if node.nodeNameZH??>${node.nodeNameZH}<#else>${node.nodeName}</#if></label></input>
							</span>
						</#list>
					</div>
				</#if>
			</td>
		</tr>
		
		<tr id="userDiv" style="display: none;"> 
		    <td>
			    <label class="LabName"><span><label class="Asterik">*</label><label id="handlerLabel" defaultValue="办理人员">办理人员</label>：</span></label>
			    <div class="Check_Radio">
			    	<a href="###" class="NorToolBtn EditBtn" id="userSelectBtn" onclick="_selectHandler();">选择人员</a>
				</div>
				<input type="hidden" name="nextStaffId" id="userIds"/>
				<input type="hidden" name="nextStaffNames" id="nextStaffNames"/>
				<input type="hidden" name="curOrgIds" id="curOrgIds"/>
				<input type="hidden" name="nextOrgNames" id="curOrgNames" />
				<div class="FontDarkBlue fl DealMan Check_Radio"><b id="userNames"></b></div>
		    	<div class="FontDarkBlue fl DealMan"><b id="htmlUserNames"></b></div>
				<input type="hidden" name="htmlUserIds" id="htmlUserIds"/>
				<!-- <input type="text" name="htmlUserNames" id="htmlUserNames" class="mustinput" readonly="readonly" style="width: 250px !important;"/> -->
		    </td>
    	</tr> 
    	
    	<tr id="assistOrgDiv" class="hide"> 
		    <td>
			    <label class="LabName"><span><label class="Asterik">*</label><label id="assistOrgLabelHtml" defaultValue="办理单位">办理单位</label>：</span></label>
			    <div class="Check_Radio">
			    	<a href="###" class="NorToolBtn EditBtn" id="assistOrgBtn" onclick="_selecHandleOrgInfo();">选择单位</a>
				</div>
				<input type="hidden" name="assistOrgIds" id="assistOrgIds"/>
				<input type="hidden" name="assistOrgNames" id="assistOrgNames"/>
				<input type="hidden" name="assistOrgLabel" id="assistOrgLabel" />
				<div class="FontDarkBlue fl DealMan Check_Radio"><b  id="assistOrgNamesHtml"></b></div>
		    </td>
    	</tr>
    	
		<tr id="imageUpload">
			<td><label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div></td>
		</tr>
		<tr class="eventTypeTr hide">
			<td>
				<label class="LabName"><span><label class="Asterik">*</label>事件分类：</span></label>
				<input type="hidden" id="type" name="type" value="<#if event.type??>${event.type}</#if>" />
				<input type="text" class="inp1 InpDisable easyui-validatebox" style="width:155px;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
			</td>
		</tr>
		<@block name="intervalTr">
		<tr class="intervalTr hide">
			<td>
				<label class="LabName"><span>时限：</span></label>
				<input type="text" id="interval" name="interval" class="inp1 easyui-numberbox" data-options="tipPosition:'bottom',max:60,min:1" style="width: 210px; height: 28px;" value="" />
				<label class="LabName" style="float: none; display: inline-block; margin-left: 0; width: 25px;">(天)</label>
			</td>
		</tr>
		</@block>
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
		<tr id="remarkTr">
			<td>
				<label class="LabName"><span><label id="adviceAsterik" class="Asterik">*</label>办理意见：</span></label>
				<textarea rows="3" style="width: 710px;height:50px;" id="advice" name="advice" class="area1 autoDoubleCell easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']" <@block name="extraAttribute4Advice"></@block>></textarea>
			</td>
		</tr>
		<tr <#if isShowSendMsg?? && isShowSendMsg><#else>class="hide"</#if>>
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
					<div class="Check_Radio"><input id="otherMobileNums" class="inp1" style="width:711px; *width:706px;" type="text" value="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔" defaultValue="可以输入额外需要发送短信的手机号码，以英文逗号(,)分隔"></div>
				</td>
			</tr>
			<tr class="item">
				<td>
					<label class="LabName"><span>短信内容：</span></label>
					<textarea rows="3" id="smsContent" name="smsContent" class="area1 autoDoubleCell easyui-validatebox"	style="width:711px;" data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']"></textarea>
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
	<#include "/zzgl/event/workflow/select_orgInfo.ftl" />
	<#include "/component/ComboBox.ftl" />
</body>

<script type="text/javascript">
	var g_EventNodeCode = null;// 全局用于存放事件节点信息
	var eventTreeApi=null;
	$(function(){
	   var curNodeName = $('#curNodeName').val(),
	   	   winWidth = $(window).width();
	   
	   if(curNodeName == "评价") {
	   	   $("#imageUpload").hide();
		   $('#remarkTr').hide();
		   $('#evaluate_level').show();
		   $('#evaluate_content').show();
	   }
	   
	   var typesDictCode = "${typesDictCode!}";
	   if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			eventTreeApi = AnoleApi.initTreeComboBox("typeName", "type", { 
				"${bigTypePcode!}" : [${typesDictCode!}] 
			}, <@block name="callBackSelected4eventType">null</@block>, [<#if event.type??>"${event.type}"</#if>], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
			});
		} else {
			eventTreeApi = AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, [<#if event.type??>"${event.type}"</#if>], {
				<@block name="extraSetting4eventType"></@block>
				EnabledSearch : true
			});
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
	   
	   if($('#taskNodesDiv').length > 0) {
		   //调整下一办理环节宽度
		   $('#taskNodesDiv').width(($('#_eventHandleTable').width() - $('#taskNodesDiv').siblings().eq(0).outerWidth(true)) * 0.98);
	   }
	   
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
	
		//将选择的牵头单位添加到组织处
		if($('#assistOrgIds').val()){
			$('#userIds').val($('#assistOrgIds').val());
			$('#nextStaffNames').val($('#assistOrgNames').val());
		}
	
	
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
				
				showMaxJqueryWindow($("#specificUrl").attr("winName"), url, 555, 315);
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
					
			$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/workflow/workflowController/submitInstanceForEventPC.jhtml?isDetail2Edit="+$('#isDetail2Edit').val());
			
			$("#flowSaveForm").ajaxSubmit(function(data) {
				modleclose();
				
				var message = '提交成功！';
				
				if(data.result != "success") {
					if(data.msgWrong) {
						message = data.msgWrong;
					} else {
						message = '提交失败！';
					}
				}else{
					var isUsePostMessage = '${isUsePostMessage!}';
					var messageUrl = '${messageUrl!}';
					if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
						window.parent.postMessage("${callBack!}('"+$("input[name='nextNode']:checked").val()+"')", messageUrl);
						return;
					}
				}
				
				$.messager.alert('提示', message, 'info', function(){
					if("${acceptNode}"&&data.result == "success"){
						modleopen();
						window.location.reload();//刷新当前页面
					}else{
						cancel();
					}
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
						}else{
							var isUsePostMessage = '${isUsePostMessage!}';
							var messageUrl = '${messageUrl!}';
							if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
								window.parent.postMessage("${callBack!}('reject')", messageUrl);
								return;
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
		$('#userIds').val('');
		$('#userNames').html('');
		$('#nextStaffNames').val('');
		$('#curOrgIds').val('');
		$('#curOrgNames').val('');
		$('#htmlUserIds').val('');
		$('#htmlUserNames').html('');
		$('#htmlUserNames').hide();
		$('#userSelectBtn').show();
		
		$('#assistOrgIds').val('${assistOrgIds!}');
		$('#assistOrgNames').val('${assistOrgNames!}');
		$('#assistOrgLabel').val('');
		$('#assistOrgNamesHtml').html('${assistOrgNamesHtml!}');
		
		$('#selectedNodeValue').val('');
		$('#selectedNodeDynamicSelect').val('');
		$('#selectedNodenNodeType').val('');
		
		$('#evaluate_level').hide();
		$('#evaluate_content').hide();
		
		$('#userDiv').hide();
		$('#assistOrgDiv').hide();
		$('#nodeId').val($(obj).attr('nodeId'));
		$('#nodeName_').val($(obj).attr('nodeName'));
		
		$('#selectedNodeValue').val($(obj).val());
		$('#selectedNodeDynamicSelect').val($(obj).attr('dynamicSelect'));
		$('#selectedNodenNodeType').val($(obj).attr('nodeType'));
		
		//隐藏短信发送内容
		$("#sendSms_").attr("checked", false);
		$("#sendCont").hide();
		
		//隐藏时限
		$("#_eventHandleTable .intervalTr").hide();
		$('#interval').numberbox({
			required: false
		});
		$("#interval").numberbox('setValue', '');
		
		modleopen();
		g_EventNodeCode = null;
		var curnodeName_ = document.getElementById("curNodeName").value;
		var nodeName_ = document.getElementById("nodeName_").value;
		var nodeCode_ = $(obj).attr('nodeCode');
		var nodeId_ = $(obj).attr('nodeId');
		var eventId_ = $("#formId").val();
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
					$('#assistOrgDiv').hide();
				} else {
					g_EventNodeCode = data.eventNodeCode;
					var isCollect = false,		//采集
						isClose = false,		//结案
						isPlaceFile = false,	//归档
						isDisplayUser = data.isDisplayUser,	//是否只展示可办理人员
						isSelectUser = data.isSelectUser,	//是否选择办理人员
						isSelectOrg = data.isSelectOrg,		//是否选择组织
						isUploadHandledPic = data.isUploadHandledPic || false,//是否可上传处理后图片
						isJointHandled = data.isJointHandled,	//是否联合办理
						isJointOperated = data.isJointOperated;	//是否联席交办
					
					$("#specificUrl").val(data.specificUrl || "");
					$("#specificUrl").attr("winName", data.specificUrlName || "");
					
					if(g_EventNodeCode != undefined){
						isCollect = g_EventNodeCode.collect;			//采集
						isClose = g_EventNodeCode.close;				//结案
						isPlaceFile = g_EventNodeCode.placeFile;		//归档
					}
					
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
							htmlUserNames = $('#userNames').html(),
							isAdviceRequired = data.isAdviceRequired;
							
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
						
						isAdviceRequired = isAdviceRequired == undefined ? true : isAdviceRequired;
						$('#advice').validatebox({
							required: isAdviceRequired
						});
						if(isAdviceRequired) {
							$('#adviceAsterik').show();
						} else {
							$('#adviceAsterik').hide();
						}
						
						$('#remarkTr').show();
					} else if(isSelectOrg) {// 下派、分流、上报或者采集人组织
						if(data.userIds) {
							$('#userIds').val(data.userIds);
						}
						if(data.userNames) {
							$('#userNames').html(data.userNames);
						}
						if(data.orgIds) {
							$('#curOrgIds').val(data.orgIds);
						}
						
						$('#userNames').parent().show();
						$('#userNames').show();
						$('#htmlUserNames').hide();
						$("#userSelectBtn").show();
						$('#userDiv').show();
						$('#remarkTr').show();
					}
					
					var handlerLabel = $("#handlerLabel").attr("defaultValue"),
						assistOrgLabelHtml = $("#assistOrgLabelHtml").attr("defaultValue");
						
					if(isJointHandled || isJointOperated) {
						if(isJointHandled) {
							handlerLabel = "牵头单位";
							assistOrgLabelHtml = "牵头单位"
						} else if(isJointOperated) {
							handlerLabel = "交办单位";
							assistOrgLabelHtml = "交办单位";
						}
						
						if(data.assistOrgIds){
							$('#assistOrgIds').val(data.assistOrgIds);
						}
						if(data.assistOrgNames){
							$('#assistOrgNames').val(data.assistOrgNames);
						}
						if(data.assistOrgNamesHtml){
							$('#assistOrgNamesHtml').html(data.assistOrgNamesHtml);
						}
						
						$('#assistOrgLabel').val(assistOrgLabelHtml);
						$("#assistOrgDiv").show();
						$('#userDiv').hide();
					} else {
						$("#assistOrgDiv").hide();
						$('#userDiv').show();
					}
					
					$("#handlerLabel").html(handlerLabel);
					$("#assistOrgLabelHtml").html(assistOrgLabelHtml);
					
					if(($('#curNodeName').val()=='task7')||(isPlaceFile&&(<#if event.type>false<#else>true</#if>))){
						$("#_eventHandleTable .eventTypeTr").show();
						$('#typeName').validatebox('options').required = true;
						$('#isDetail2Edit').val(true);
					}else{
						$("#_eventHandleTable .eventTypeTr").hide();
						$('#typeName').validatebox('options').required = false;
						$('#isDetail2Edit').val(false);
					}
					
					
					//图片上传的展示设置
					if (isPlaceFile) {// 归档
						$("#userDiv").hide();
					}
					
					//放开图片上传的限制
					var radioList = [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}];
					
					$("#imageUpload").show();
					
					$('#bigFileUploadDiv').changeLabelDict(radioList);
					
					<@block name="operateAfterNextNodeSelected"></@block>
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
        	
        	if(isBlankString(userIds)&&isNotBlankString($('#assistOrgIds').val())){
        		userIds=$('#assistOrgIds').val();
        	}
        	
        	if(isBlankString(userIds)){
        		var msg = "请选择" + $("#handlerLabel").html() + "！";
        		
        		$.messager.alert('提示', msg, 'info');
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
		
		if($("#advice").is(":visible")) {
			$('#advice').validatebox({
	    		required: $('#advice').validatebox('options').required
	    	});
    	}
    	
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
			
			$('#nextStaffNames').val($('#userNames').html());
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
				
				$("#flowSaveForm").attr("action", '${rc.getContextPath()}/zhsq/workflow/workflowController/eventNoteContent.jhtml?curnodeName='+ $("#curNodeName").val() +'&nodeName='+ $("#nodeName_").val());
				
				$("#flowSaveForm").ajaxSubmit(function(data) {
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
	function _selectHandler(option) {
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
			orgRootId			: $('#orgRootId').val(),
			userSelectedLimit	: '1'
		};
		
		obj = $.extend({}, obj, option);
		
		selectUserByObj(obj);
	}
	
	//打开协办组织选择窗口
	function _selecHandleOrgInfo() {
		var obj = {
			nextOrgIdElId		: 'assistOrgIds', 
			nextOrgNameElId		: 'assistOrgNames', 
			nextOrgNameHtmlId	: 'assistOrgNamesHtml',
			userSelectedLimit	: '1',
			formId				: $("#formId").val()
		};
		
		SelectWin4OrgInfo.selectOrgInfoByObj(obj);
	}
	
	<@block name="extraFunction4Handle"></@block>
</script>

</html>