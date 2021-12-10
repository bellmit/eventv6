<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>禁毒事件详情</title>
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	
	<style type="text/css">
		.cellWidth{width: 128px;}
		.doubleCellWidth{width: 85%;}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<form id="drugEnforcementForm" name="tableForm" action="" method="post">
			<div id="norFormDiv" class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>姓名：</span></label>
								<label id="addictName" class="FontDarkBlue Check_Radio">${drugEnforcementEvent.addictName!}</label>
							</td>
							<td>
								<label class="LabName"><span>公民身份号码：</span></label>
								<label id="addictIdCard" class="FontDarkBlue Check_Radio">${drugEnforcementEvent.addictIdCard!}</label>
							</td>
						</tr>
						<tr id="drugGridNameTr">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>场所：</span></label>
								<label id="drugGridName" class="FontDarkBlue Check_Radio">${drugEnforcementEvent.addictGridPath!}</label>
							</td>
						</tr>
						<tr id="drugVarTr">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>吸食毒品种类：</span></label>
								<label id="drugVar" class="FontDarkBlue Check_Radio doubleCellWidth">${drugEnforcementEvent.addictDrugVarName!'无'}</label>
							</td>
						</tr>
						<#if drugEventContentDict?? && (drugEventContentDict?size > 0)>
							<#list drugEventContentDict as item>
								<#if item_index % 2 == 0 >
									<tr>
								</#if>
										<td class="LeftTd">
											<label class="LabName"></label><div class="Check_Radio"><span><input type="checkbox" <#if drugEnforcementEvent.content?? && (drugEnforcementEvent.content?index_of(item.dictGeneralCode) >= 0)>checked</#if> disabled />${item.dictName}</span></div>
										</td>
								<#if item_index % 2 == 1 >
									</tr>
								</#if>
							</#list>
							
							<#if (drugEventContentDict?size % 2 == 1)>
									<td class="LeftTd"></td>
								</tr>
							</#if>
						</#if>
						<tr>
							<td colspan="2">
								<label class="LabName"><span>社会毒情：</span></label>
								<label class="FontDarkBlue Check_Radio">${drugEnforcementEvent.drugSocialSituationName!}</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>联系人员：</span></label>
								<label class="FontDarkBlue Check_Radio">${drugEnforcementEvent.contactUser!}</label>
							</td>
							<td>
								<label class="LabName"><span>联系电话：</span></label>
								<label class="FontDarkBlue Check_Radio">${drugEnforcementEvent.contactTel!}</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>办理期限：</span></label>
								<label class="FontDarkBlue Check_Radio">${drugEnforcementEvent.handleDateStr!}</label>
							</td>
							<td>
								<label class="LabName"><span>办结时间：</span></label>
								<label class="FontDarkBlue Check_Radio">${drugEnforcementEvent.finDateStr!}</label>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>图片上传：</span></label><div class="ImgUpLoad" id="fileupload"></div>
							</td>
						</tr>
						<tr id="adviceTr" class="hide">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>办理意见：</span></label><textarea rows="3" style="height:80px;" id="closeAdvice" class="area1 easyui-validatebox fast-reply" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']"></textarea>
							</td>
						</tr>
						<#if curTaskName??>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>当前环节：</span></label>
								<label class="FontDarkBlue Check_Radio doubleCellWidth">${curTaskName}<#if taskPersonStr??>|${taskPersonStr!}</#if></label>
							</td>
						</tr>
						</#if>
					</table>
				</div>
			</div>
		</form>
		
		<#if isCurHandler?? && isCurHandler>
			<div id="workflowHandleDiv">
				<#include "/zzgl/event/workflow/handle_node_base.ftl" />
			</div>
		</#if>
		
		<div id="workflowDetailDiv" class="hide">
			<div class="h_20"></div>
			<div class="ConList">
				<div class="nav" id="tab">
					<ul>
						<li id="01_li" class="current">处理环节</li>
						<li id="04_li" class="hide">短信推送记录</li>
					</ul>
				</div>
				<div id="tabContentDiv" class="ListShow ListShow2">
					<div id="01_li_div" class="t_a_b_s">
						<div id="workflowDetail" border="false" style="overflow-y: hidden;"></div>
					</div>
					<div id="04_li_div" class="t_a_b_s hide">
						<div id="eventMsgSendDiv" border="false"></div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	
	<#if instanceId??>
	<#elseif listType?? && listType==1>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="showAdvice('<#if drugEnforcementEvent.drugEnforcementId??>${drugEnforcementEvent.drugEnforcementId?c}</#if>');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
        		<a href="###" onclick="showAdvice('<#if drugEnforcementEvent.drugEnforcementId??>${drugEnforcementEvent.drugEnforcementId?c}</#if>', '1');" class="BigNorToolBtn BigJieAnBtn">归档</a>
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    <#else>
    	<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="closeDetailWin();" class="BigNorToolBtn CancelBtn">关闭</a>
        	</div>
        </div>
    </#if>

	
	<script type="text/javascript">
		$(function() {
			var options = { 
				axis : "yx", 
				theme : "minimal-dark" 
			},
			swfOpt = {
				positionId:'fileupload',//附件列表DIV的id值',
				type:'detail',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				ajaxData: {'bizId':'${drugEnforcementEvent.drugEnforcementId?c}','attachmentType':'${ATTACHMENT_TYPE!}','eventSeq':'1,2,3'},
				showPattern: 'pic',
				imgDomain: '${imgDownPath!}'
			};
			
			$("#norFormDiv").width($(window).width());
			
			enableScrollBar('content-d',options); 
			
			valueFormatter('${drugEnforcementEvent.isRestoreFamilyRelationship!}', 'isRestoreFamilyRelationship');
			valueFormatter('${drugEnforcementEvent.isSocialAssistance!}', 'isSocialAssistance');
			valueFormatter('${drugEnforcementEvent.isEmploymentService!}', 'isEmploymentService');
			
			$("#workflowHandleDiv .DealProcess .LabName").width(90);//为了使办理环节的标题与详情的标题对齐
			
			showWorkflowDetail();
			
			<#if isCurHandler?? && isCurHandler>
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/drugEnforcementEvent/subWorkflowData.jhtml'
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/drugEnforcementEvent/rejectWorkflowData.jhtml'
					},
					sendSms: {
						smsContentUrl: "${rc.getContextPath()}/zhsq/drugEnforcementEvent/capSmsContent.jhtml",
						isSendSms: true
					}
				});
				
				$.extend(swfOpt, {
					type			: 'edit',
					file_types		: '*.jpg;*.gif;*.png;*.jpeg',
					radio_list		: [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}],
					appCode			: 'zhsq_event',
					initAttrParam	: {'attachmentType':'${ATTACHMENT_TYPE!}', 'bizId': '${drugEnforcementEvent.drugEnforcementId?c}'}
				});
			</#if>
			
			fileUpload(swfOpt);
			
			var lis = $("#tab").find("li");
			lis.each(function() {
				$(this).bind("click", function() {
					var tabId = $(this).attr("id");
					
					lis.removeClass("current");
					$("#tabContentDiv .t_a_b_s").hide();
					
					$(this).addClass("current");
					
					$("#" + tabId + "_div").show();
					
					if(tabId == "04_li") {
						if($("#eventMsgSendDiv > iframe").length == 0) {
							$('<iframe style="width: 100%; height: 100%; border: none; "/>').appendTo($("#eventMsgSendDiv"));
							$("#eventMsgSendDiv > iframe").attr("src", "${rc.getContextPath()}/zhsq/drugEnforcementEvent/toEventMsgSendMidList.jhtml?bizId=${drugEnforcementEvent.drugEnforcementId?c}&bizType=DRUG_ENFORCEMENT_EVENT&taskBizType=DRUG_ENFORCEMENT_EVENT_TASK&targetTypes=SMS");
						}
					}
				});
			});
			
			$("#closeAdvice").width($(window).width() * 0.85);
		});
		
		function closeDetailWin(){
			parent.closeMaxJqueryWindow();
		}
		
		function flashData(msg) {//工作办理回调
			parent.reloadDataForSubPage(msg);
		}
		
		function valueFormatter(value, lableId) {
			var val = "";
			
			if(value) {
				switch(value) {
					case '0': {
						val = "不需要"; break;
					}
					case '1': {
						val = "需要"; break;
					}
				}
			}
			
			$("#" + lableId).html(val);
		}
		
		function showWorkflowDetail() {
			var instanceId = "<#if instanceId??>${instanceId?c}</#if>";
			
			if(instanceId != "" && instanceId > 0) {
				$("#workflowDetailDiv").show();
				
				$("#workflowDetail").panel({
					height:'auto',
					width:'auto',
					overflow:'no',
					href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=" + instanceId,
					onLoad:function(){//配合detail_workflow.ftl使用
						var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
						var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.4;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
						var taskListSize = $("#taskListSize").val();	//任务记录数
						var handleTaskNameWidth = 115;		//处理环节总宽度
						var handleLinkWidth = 21;			//办理环节宽度
						var handlePersonAndTimeWidth = 0;	//办理人/办理时间宽度
						var handleRemarkWidth = 0;			//办理意见宽度
						
						var remindSize = 0;					//催办记录数
						var remindPersonAndTimeWidth = 0;	//催办人和催办时间宽度
						var remindRemarkWidth = 0;			//催办意见宽度
						
						if($("#_currentTaskHandlerDiv").length) {//当前办理人员
							$("#_currentTaskHandlerDiv").width((workflowDetailWidth - handleTaskNameWidth - handleLinkWidth) * 0.85);
						}
						
						for(var index = 0; index < taskListSize; index++){
							remindSize = $("#remindListSize_"+index).val();//催办记录数
							remindPersonAndTimeWidth = 0;
							remindRemarkWidth = 0;
							
							handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
							
							if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
								$("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
								handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
							}
							
							handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;
							
							$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度
							
							for(var index_ = 0; index_ < remindSize; index_++){
								remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
								remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
								$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
							}
						}
						
						adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
						
						$("#eventMsgSendDiv").width($("#workflowDetail").width());
						$("#eventMsgSendDiv").height($("#workflowDetail").height());
						showEventMsgSendMidTab();
					}
				});
			}
		}
		
		function showAdvice(drugEnforcementId, isClose) {
			if(isClose && isClose == '1') {
				$('#closeAdvice').validatebox({
					required: true
				});
				
				$("#adviceTr").show();
			} else {
				$("#adviceTr").hide();
				
				$('#closeAdvice').validatebox({
					required: false
				});
				
				$('#closeAdvice').val("");
			}
			
			if($("#drugEnforcementForm").form('validate')) {
				startWorkflow(drugEnforcementId, isClose);
			}
		}
		
		function startWorkflow(drugEnforcementId, isClose) {
			if(drugEnforcementId && drugEnforcementId > 0) {
				modleopen();
				
				isClose = isClose || '0';
				
				$.ajax({
					type: "POST",
					url : '${rc.getContextPath()}/zhsq/drugEnforcementEvent/startWorkflow4DrugEnforcement.jhtml',
					data: {'drugEnforcementId' : drugEnforcementId, 'isClose' : isClose, 'advice' : $('#closeAdvice').val()},
					dataType:"json",
					success: function(data){
						if(data.success && data.success == true) {
							if(isClose == '1') {
								var isCurrent = drugEnforcementId && drugEnforcementId > 0;
									
								parent.reloadDataForSubPage(data.tipMsg, isCurrent);
							} else {
								parent.searchData();
								parent.detail(drugEnforcementId, "2");
								if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function'){
									parent.closeBeforeMaxJqueryWindow();
								}
							}
						} else {
							modleclose();
	  					
		  					if(data.tipMsg) {
		  						$.messager.alert('错误', data.tipMsg, 'error');
		  					} else {
		  						$.messager.alert('错误', '操作失败！', 'error');
		  					}
						}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
				});
			}
		}
		
		function showEventMsgSendMidTab() {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/drugEnforcementEvent/listMsgSendMidData.json',
				data: {bizId:'<#if drugEnforcementEvent.drugEnforcementId??>${drugEnforcementEvent.drugEnforcementId?c}</#if>', bizType:'DRUG_ENFORCEMENT_EVENT', taskBizType:'DRUG_ENFORCEMENT_EVENT_TASK', targetTypes:'SMS'},
				dataType:"json",
				success: function(data) {
					if(data && data.total > 0) {
						$("#04_li").show();
					}
				},
				error:function(data) {
					$.messager.alert("错误", "获取事件短信推送记录失败！", "error");
				}
			});
		}
	</script>
	
</body>
</html>
