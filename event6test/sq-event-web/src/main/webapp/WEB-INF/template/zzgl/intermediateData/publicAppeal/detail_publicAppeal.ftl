<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>民众诉求事件详情</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/commonFiles-1.1.ftl" />
	
	<style type="text/css">
		.cellWidth{width: 128px;}
		.doubleCellWidth{width: 85%;}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div id="norFormDiv" class="NorForm" style="width:718px;">
			<div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

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
                    </ul>
                </div>
                <div class="ListShow ListShow2">
                    <div id="01_li_div" class="t_a_b_s">
                    	<div id="workflowDetail" border="false" style="overflow-y: hidden;"></div>
                    </div>
                </div>
        	</div>
            
            <div class="h_10"></div> 
        </div>
	</div>
	
	<#if instanceId??>
	<#elseif listType?? && listType==1>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="###" onclick="startWorkflow('<#if drugEnforcementEvent.drugEnforcementId??>${drugEnforcementEvent.drugEnforcementId?c}</#if>');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
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
		$(function(){

            BaseWorkflowNodeHandle.initParam({
                subTask: {
                    subTaskUrl: '${rc.getContextPath()}/zhsq/publicAppeal/subWorkflowData.jhtml'
                },
                reject: {
                    rejectUrl: '${rc.getContextPath()}/zhsq/publicAppeal/rejectWorkflowData.jhtml'
                }
            });
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        
	        $("#norFormDiv").width($(window).width());
	        
	        enableScrollBar('content-d',options); 
	        
	        valueFormatter('${drugEnforcementEvent.isRestoreFamilyRelationship!}', 'isRestoreFamilyRelationship');
	        valueFormatter('${drugEnforcementEvent.isSocialAssistance!}', 'isSocialAssistance');
	        valueFormatter('${drugEnforcementEvent.isEmploymentService!}', 'isEmploymentService');
	        
	        $("#workflowHandleDiv .DealProcess .LabName").width(90);//为了使办理环节的标题与详情的标题对齐
	        
	        showWorkflowDetail();
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
					}
				});
			}
		}
		
		function startWorkflow(drugEnforcementId) {
			if(drugEnforcementId && drugEnforcementId > 0) {
				modleopen();
				
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/publicAppeal/startWorkflow4DrugEnforcement.jhtml',
					data: {'publicAppealId' : drugEnforcementId},
					dataType:"json",
					success: function(data){
						if(data.success && data.success == true) {
							parent.searchData();
							parent.detail(drugEnforcementId, "2");
							if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function'){
								parent.closeBeforeMaxJqueryWindow();
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

        //待办提交
        function subTaskCallBack() {
            var url = $("#specificUrl").val();

            if(url) {//关闭打开的个性表单窗口
                closeMaxJqueryWindow();
            }

            var flag = true;

            __validate(function(data) {
                flag = data;
            });

            if(flag){
                modleopen();
                var nextUserIds = $("#nextUserIds").val(),
                        nextNode = $("#selectedNodeValue").val(),
                        nextOrgIds = $("#nextOrgIds").val(),
                        advice = $("#advice").val(),
                        formId = $("#formId").val();

                $.ajax({
                    type: "POST",
                    url : '${rc.getContextPath()}/zhsq/publicAppeal/subWorkflowData.jhtml',
                    data: {'nextUserIds': nextUserIds,'nextOrgIds':nextOrgIds,'nextNodeName': nextNode,'advice': advice,
                        'formId':formId},
                    dataType:"json",
                    success: function(data){
                        modleclose();

                        var result = data.success,
                                msg = '';

                        if(result && result == true) {
                            msg = data.tipMsg || '操作成功！';
                            cancel(msg);
                        } else {
                            msg = data.tipMsg || '操作失败！';
                            $.messager.alert('错误', msg, 'error');
                        }
                    },
                    error:function(data){
                        $.messager.alert('提示','提交失败！','info', function(){
                            cancel();
                        });
                    }
                });
            }
        }

	</script>
	
</body>
</html>
