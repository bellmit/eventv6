<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>民生信息详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<style type="text/css">
			.singleCellClass{width: 68%;}
			.doubleCellClass{width: 88%}
			.labelNameWide{width: 132px;}
			.ToolBar {
				border-top:0px;
				border-bottom:0px;
				background:#fff;
				padding: 6px 0px;/*解决表格跳动*/
				color:rgba(82,148,232,.8);
				height: 38px;
				zoom:1;
			}
			.Check_Radio {
				float:left;
				margin-left:3px;
				word-break:break-all;
			}
			.datagrid-mask-msg {
				box-sizing:content-box
			}
			
			
			.fw-main {
		    	padding: 0px 10px 0px 10px;
		    }
		    
			.WitchLink {
			    width: 150px !important;
			    margin-left: -50px;
			}
			
			.DBLink {
			    padding: 10px 0 0 142px!important;
			}
			
			.ProcessingLink .ht ul li:nth-child(1){
				margin-right: 120px!important;
			}
			
			.t_pic{margin-left: 40px;}
			.f_pic{margin-left: 40px;}
			.LinkList{margin-left: 40px;}
			
			.bigFile-upload-box {
			    margin-top: 5px;
			}
			
		</style>
		

		
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul id="topTitleUl" class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">民生信息详情</a></li>
							<#if showFlowDetail?? && showFlowDetail>
							<li><a href="##" divId="taskDetailDiv">处理环节</a></li>
							</#if>
						</ul>
					</div>
				</div>
				
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent" style="border-bottom: 1px solid #e5e5e5;overflow:hidden">
				   <input type="hidden" name="infoId" id="infoId" value="${infoId!''}" />
			       <input type="hidden" name="infoOrgCode" id="infoOrgCode" value="${infoOrgCode!''}" />
			       
			       <div id="content-d" class="MC_con content light" style="overflow-x:hidden; position:relative;">
		    			<div class="NorForm NorForm1" id="pageDetailDiv">
		    				<div class="fw-det-tog-top">
								<h5><i style="background:#4f91ff;"></i>民生信息详情</h5>
							</div>
			    			<table width="100%" border="0" cellspacing="0" cellpadding="0">
						     	<tr>
					                <td class="LeftTd">
					                    <label class="LabName"><span><i class="spot-xh">*</i>所属辖区:</span></label>
					                    <div class="Check_Radio FontDarkBlue" style="width: 60%;"><#if gridPath??>${gridPath}</#if></div>
					                </td>
					                <td>
					                    <label class="LabName"><span><i class="spot-xh">*</i>上报时间:</span></label>
					                    <div class="Check_Radio FontDarkBlue"><#if happenTimeStr??>${happenTimeStr}</#if></div>
					                </td>
					            </tr>
						     	<tr>
					                <td class="LeftTd" style="width:50%">
					                    <label class="LabName"><span><i class="spot-xh">*</i>信息状态:</span></label>
					                    <div class="Check_Radio FontDarkBlue"><#if statusName??>${statusName}</#if></div>
					                </td>
					                <td>
					                    <label class="LabName"><span><i class="spot-xh">*</i>紧急程度:</span></label>
					                    <div class="Check_Radio FontDarkBlue"><#if urgenceDegreeName??>${urgenceDegreeName}</#if></div>
					                </td>
					            </tr>
						     	<tr>
					                <td class="LeftTd">
					                    <label class="LabName"><span><i class="spot-xh">*</i>民生信息类型:</span></label>
					                    <div class="Check_Radio FontDarkBlue"><#if infoTypeName??>${infoTypeName}</#if></div>
					                </td>
					                <td>
					                    <label class="LabName"><span><i class="spot-xh">*</i>民生动态类型:</span></label>
					                    <div class="Check_Radio FontDarkBlue"><#if infoTrendsTypeName??>${infoTrendsTypeName}</#if></div>
					                </td>
					            </tr>
					            <tr>
					                <td class="LeftTd" colspan="2">
					                	<label class="LabName"><span><i class="spot-xh">*</i>信息标题:</span></label>
					                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if infoTitle??>${infoTitle}</#if></div>
					                </td>
					            </tr>
					            <tr>
					                <td class="LeftTd" colspan="2">
					                	<label class="LabName"><span><i class="spot-xh">*</i>发生地点:</span></label>
					                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if occurred??>${occurred}</#if></div>
					                </td>
					            </tr>
					            <tr>
					                <td class="LeftTd" colspan="2">
					                	<label class="LabName"><span><i class="spot-xh">*</i>信息内容:</span></label>
					                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if infoContent??>${infoContent}</#if></div>
					                </td>
					            </tr>
					            <#if curTaskName?? && isCurHandler?? && isCurHandler>
					            <tr>
					                <td class="LeftTd" colspan="2">
					                	<label class="LabName"><span>当前环节:</span></label>
					                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if curTaskName??>${curTaskName}</#if><#if taskPersonStr??>|${taskPersonStr}</#if></div>
					                </td>
					            </tr>
					            </#if>
					            <#if isCurHandler?? && isCurHandler>
					            <#else>
						            <tr>
							    		<td colspan="2" class="LeftTd">
							    			<label class="LabName" style="margin-left: 8px;"><span>相关附件：</span></label><div id="bigFileUploadDiv"></div>
							    		</td>
							    	</tr>
						    	</#if>
			    			</table>
		    			</div>
					</div>
					
					<!--办理信息-->
					<#if isCurHandler?? && isCurHandler>
						<div id="pageHandleDiv" class="fw-det-tog fw-det-tog-n mt20" style="position:relative">
							<div class="fw-det-tog-top">
								<h5><i style="background:#4f91ff;"></i>处置流程</h5>
							</div>
							<div class="fw-chul">
								<#include "/zzgl/event/workflow/handle_node_people_livelihood.ftl" />
							</div>
						</div>
					</#if>
					
				</div>
				
				<#if showFlowDetail?? && showFlowDetail>
					<div id="taskDetailDiv" class="fw-main tabContent" style="padding-top: 3px;">
						<!-- 处理环节 -->
						<div id="workflowDetail" border="false"></div>
					</div>
				</#if>
			</div>
		</div>
	</body>
	
	<script type="text/javascript">
		var basWorkSubTaskCallback = null,//存放原有的提交回调方法
			baseWorkRejectCallback = null;//存放原有的驳回方法
		var bigFileUploadOpt;	
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0;
			
			bigFileUploadOpt = {
				useType: 'view',
				attachmentData: {bizId:'<#if infoSeqId??>${infoSeqId}</#if>',attachmentType:"PEOPLE_LIVE"},
				module: 'attachment',
				appcode: 'sqfile',
				fileNumLimit: 9,
				isUseLabel: false
			};
        
			<#if showFlowDetail?? && showFlowDetail>
				showWorkflowDetail();
			</#if>
			//顶部页签切换相应事件
			$('#topTitleUl').on('click', 'li a', function() {
				$('#topTitleUl > li > a').removeClass('active');
				
				$(this).addClass('active');
				
				$('#formDiv div.tabContent').hide();
				var divId = $(this).attr('divId');
				var iframeItemList = $("#" + divId + " iframe"),
						iframeLen = iframeItemList.length;
				if(iframeLen == 1) {
					var iframeItem = iframeItemList.eq(0),
							iframeLoaded = iframeItem.attr("_loadflag");
					if(isBlankStringTrim(iframeLoaded)) {
						iframeItem.attr("_loadflag", true);//用于防止重复加载
						iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
					}
				}
				$('#' + divId).show();
				
			});
			
			$(window).on('load resize', function () {
				$winH = $(window).height();
				$topH = $('#topTitleDiv').outerHeight(true);
				$btnH = $('#btnDiv').outerHeight(true);
				
				$('#mainDiv').height($winH - $topH - $btnH);
				$('#content-d').height($winH - $topH - $btnH);
				
				var detailHeight=$('#pageDetailDiv').height();
				var toppx=0.9*detailHeight-408;
            	$('#pageHandleDiv').css('margin-top',toppx+'px');
            	//滚动条初始化
            	<#if isCurHandler?? && isCurHandler>
					$("#mainDiv").niceScroll({
						cursorcolor:"rgba(0, 0, 0, 0.3)",
						cursoropacitymax:1,
						touchbehavior:false,
						cursorwidth:"4px",
						cursorborder:"0",
						cursorborderradius:"4px"
					});
				<#else>
					$("#content-d").niceScroll({
						cursorcolor:"rgba(0, 0, 0, 0.3)",
						cursoropacitymax:1,
						touchbehavior:false,
						cursorwidth:"4px",
						cursorborder:"0",
						cursorborderradius:"4px"
					});
				</#if>
			});
			
			
			<#if isCurHandler?? && isCurHandler>
				var baseWorkOption = BaseWorkflowNodeHandle.initParam();//获取默认的设置
				
				basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/peopleLivelihood/subWorkflow.jhtml',
						subTaskCallback: subTaskCallbackFun
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/peopleLivelihood/subWorkflow.jhtml'
					},
					evaluate: {
						isShowEva: false
					},
					checkRadio: {
						radioCheckCallback: radioCheckCallbackFun
					},

					selectHandler: {
						isShowOrgNameFuzzyQuery : true
					},
					selectOrgInfo: {
						isShowOrgNameFuzzyQuery : true
					}
				});
				
				bigFileUploadOpt["useType"] = 'edit'; 
				
	
			</#if>
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
			
		});
		
		
		function subTaskCallbackFun(){
			flag = true;
		
		
			__validate(function(data){
				flag = data;
			});
		
		
			if(flag) {
				modleopen();
						
				$("#flowSaveForm").attr("action", "${rc.getContextPath()}/zhsq/peopleLivelihood/subWorkflow.jhtml?infoId="+$('#infoId').val());
				
				$("#flowSaveForm").ajaxSubmit(function(data) {
					modleclose();
					
					var message = '提交成功！';
					
					if(data.code != "1") {
						if(data.msg) {
							message = data.msg;
						} else {
							message = '提交失败！';
						}
					}else{
						var isUsePostMessage = '${isUsePostMessage!}';
						var messageUrl = '${messageUrl!}';
						if(messageUrl!='' && isUsePostMessage === 'true' && parent && parent.postMessage && typeof parent.postMessage === 'function') {
							window.parent.postMessage('${callBack!}()', messageUrl);
							return;
						}
					}
					
					$.messager.alert('提示', message, 'info', function(){
						parent.submitCallBack();
					});
				});
			}
		}
		
		function radioCheckCallbackFun(){
		
		}
		
	
		function showWorkflowDetail() {//流程详情
			var instanceId = "${instanceId!}";
			if(instanceId) {
				$("#workflowDetail").panel({
					height:'auto',
					width:'auto',
					overflow:'no',
					href: "${rc.getContextPath()}/zhsq/workflow/workflowController/flowDetail.jhtml?instanceId=${instanceId}",
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
	
	                    for(var index = 0; index < taskListSize; index++){
	                        remindSize = $("#remindListSize_"+index).val();//催办记录数
	                        remindPersonAndTimeWidth = 0;
	                        remindRemarkWidth = 0;
	
	                        handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
	
	                        if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
	                            $("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
	                            handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
	                        }
							<@block name="handleRemarkWidthBlock">
	                        handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth-40;
							</@block>
	                        $("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度
	
	                        for(var index_ = 0; index_ < remindSize; index_++){
	                            remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
	                            remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
	                            $("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
	                        }
	                    }
	
	                    adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
	                    
	                    //为了修复流程加载缓慢导致，点击该页签时，内容不展示的问题
						$('#topTitleUl > li > a.active').eq(0).click();
					}
				});
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
	        	var userIds = $("#nextUserIds").val();
	        	
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
				
				$('#nextStaffNames').val($('#userNames').html());
	    	}
	    	
	    	return validate;
		}
		
	</script>
</html>