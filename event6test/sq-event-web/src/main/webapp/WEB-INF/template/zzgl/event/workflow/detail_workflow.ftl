<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>办理环节详情</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<!--图标说明-->
	<div class="ToolBar" style="border:none; background:none;overflow:hidden;">
		<div id="iconDiv" class="fl">
    		<i class="TaskCurrent"></i>当前环节
    		<i class="TaskHistory"></i>历史环节
    		<i class="TaskReject" style="background-size: cover; width: 12px; height: 12px;"></i>驳回环节
    		<i class="TaskRecall" style="background-size: cover; width: 12px; height: 12px;"></i>撤回环节
    	</div>
	</div>
    <div id="_detailWorkflowDiv" class="tabss">
    	<div class="ProcessingLink" style="padding:0;"> 
    		<div class="ht">
	    		<ul>
		    		<li style="margin-right:50px;width:100px; text-align:right;">办理环节</li>
		    		<li style="width:185px;">办理人/办理时间</li>
		    		<li>办理意见</li>
	    		</ul>
			</div>
            <div class="t_pic"></div>
            <input type="hidden" id="taskListSize" value="<#if taskList??>${taskList?size}<#else>0</#if>" />
            <#if taskList??>
				<#list taskList as l>
					<div class="LinkList fx-spotlist <#if l.PARENT_PRO_TASK_ID??>_isSubProInstance_${l.PARENT_PRO_TASK_ID?c}</#if>">
                    	<div class="WitchLink FontGreen Check_Radio fl">
                    		<#if l.HANDLE_PERSON??>
                    			<div class="FontCurrent">${l.TASK_NAME!}</div>
                    		<#else>
                    			<#if (l.subAndReceivedTaskList?? && l.subAndReceivedTaskList?size>0)>
			                    	<div onclick="showSubTask('${l.TASK_ID?c}', this)" style="cursor: pointer;">
			                    		${l.TASK_NAME!}<div class="SubTaskOpen" title="展开更多意见" style="width:12px; height:12px;"></div>
			                    	</div>
			                    <#else>
			                    	${l.TASK_NAME!}
			                    </#if>
			                    <#if l.OPERATE_TYPE?? && l.OPERATE_TYPE == '2'>
			                    	<div class="RejectOperate" style="width: 36px; height: 18px; margin: 0 auto;"/>
			                    </#if>
                    		</#if>
                    	</div>
	                    <#if l.HANDLE_PERSON??>
	                    	<div class="LinkDotNow fl" <#if l.SUB_PRO_COUNT?? && (l.SUB_PRO_COUNT > 0)>onclick="showSubPro('${l.TASK_ID?c}', this);"</#if>><#if l.SUB_PRO_COUNT?? && (l.SUB_PRO_COUNT > 0)><span class="SubProOpen" title="展开更多" style="margin: -8px 0 0 -8px; width: 27px; height: 27px; cursor: pointer;"></span><#else><img src="${rc.getContextPath()}/images/link_11.png" /></#if></div>
	                    	<div id="_currentTaskHandlerDiv" class="WhoHandle fl" style="width:80%;padding:0 0 0 15px">
	                    		<p>
	                    			<span class="FontCurrent">${l.HANDLE_PERSON} <b class="FontRed"><#if (l.ISTIMEOUT?? && l.ISTIMEOUT=='1')>超时</#if></b></span>
	                    			<#if l.IS_SUPERVISED?? && l.IS_SUPERVISED>
	                    				<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 0 0 2px; width:28px; height:28px;" />
	                    			</#if>
	                    		</p>
	                    <#else>
	                    	<#if l.TASK_NAME??>
	                    	<#if l.OPERATE_TYPE?? && l.OPERATE_TYPE == '2'>
	                    	<div class="LinkDotReject fl"></div>
	                    	<#elseif l.OPERATE_TYPE?? && l.OPERATE_TYPE == '5'>
	                    	<div class="LinkDotRecall fl"></div>
	                    	<#else>
	                    	<div class="LinkDot fl" <#if l.SUB_PRO_COUNT?? && (l.SUB_PRO_COUNT > 0)>onclick="showSubPro('${l.TASK_ID?c}', this);"</#if>><#if l.SUB_PRO_COUNT?? && (l.SUB_PRO_COUNT > 0)><span class="SubProOpen" title="展开更多" style="margin: -8px 0 0 -9px; width: 27px; height: 27px; cursor: pointer;"></span><#else><img src="${rc.getContextPath()}/images/link_11.png" /></#if></div>
	                    	</#if>
	                    	<#elseif l.TASK_TYPE?? && l.TASK_TYPE=='2'><!--会签任务类型为2-->
	                    	<div class="LinkCountersign fl"></div>
	                    	<#else>
	                    	<div class="LinkHandling fl"></div>
	                    	</#if>
	                    	<div id="handlePersonAndTime_${l_index}" class="WhoHandle fl">
	                    		<p>
	                    			<span class="FontDarkBlue">
	                    				<#if l.SUB_HANDLE_PERSON??>
	                    					${l.SUB_HANDLE_PERSON}
	                    				<#else>
	                    					${l.TRANSACTOR_NAME!}<#if l.ORG_NAME??>(${l.ORG_NAME})</#if>
	                    				</#if>
	                    			</span> 
	                    			<#if l.INTER_TIME??>耗时 <span class="FontDarkBlue"><#if (l.INTER_TIME == '0分钟')>小于1分钟<#else>${l.INTER_TIME}</#if></#if></span> <b class="FontRed"><#if (l.ISTIMEOUT?? && l.ISTIMEOUT=='1')>超时</#if></b>
	                    		</p>
	                        	<#if l.END_TIME?? || (l.IS_SUPERVISED?? && l.IS_SUPERVISED)>
	                        		<p>
	                        			<span class="FontDarkBlue <#if l.END_TIME??><#else>hide</#if>">办理时间：</span>${l.END_TIME}
	                        			<#if l.IS_SUPERVISED?? && l.IS_SUPERVISED>
	                        				<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 0 0 2px; width:28px; height:28px;" />
	                        			</#if>
	                        		</p>
	                        	</#if>
	                        	<#if l.DISTRIBUTE_USER_ORG_NAMES??>
	                        		<p>
	                        			<span class="FontRed">分送人员：</span>${l.DISTRIBUTE_USER_ORG_NAMES}
	                        		</p>
	                        	</#if>
	                        	<#if l.SELECT_USER_ORG_NAMES??>
	                        		<p>
	                        			<span class="FontRed">选送人员：</span>${l.SELECT_USER_ORG_NAMES}
	                        		</p>
	                        	</#if>
	                    </#if>
	                    </div>
	                    
	                    <#if l.REMARKS?? && (l.REMARKS!='') >
		                    <div id="handleRemark_${l_index}" class="HandleAdvice fl" style="word-break: break-all;">
		                    	<div class="DialogArrow"></div>
		                    	<span>${l.REMARKS}</span>
		                    </div>
	                    </#if>
	                    
	                    <!--会签任务、处理中任务-->
	                    <div class="clear"></div>
	                    <input type="hidden" id="subTaskListSize_${l_index}" _index="${l_index}" value="<#if l.subAndReceivedTaskList??>${l.subAndReceivedTaskList?size}<#else>0</#if>" />
	                    <#if (l.subAndReceivedTaskList?? && l.subAndReceivedTaskList?size>0)>
	                    	<div id="_subTask_${l.TASK_ID?c}" class="_isSubTask_${l.TASK_ID?c} <#if !(l.IS_CURRENT_TASK?? && l.IS_CURRENT_TASK)>hide</#if>">
	                    	<#list l.subAndReceivedTaskList as sub>
	                    		<div class="DBLink">
	                    			<div id="subPersonAndTime_${l_index}_${sub_index}" class="WhoHandle fl">
	                    				<p><span class="SubTaskPerson" style="margin-right: 3px;"></span><span class="FontDarkBlue">${sub.TRANSACTOR_NAME!}<#if sub.ORG_NAME??>(${sub.ORG_NAME})</#if></span> <#if sub.INTER_TIME??>耗时 <span class="FontDarkBlue"><#if (sub.INTER_TIME == '0分钟')>小于1分钟<#else>${sub.INTER_TIME}</#if></#if></span> <b class="FontRed"><#if (sub.ISTIMEOUT?? && sub.ISTIMEOUT=='1')>超时</#if></b></p>
			                        </div>
				                    
	                    			<#if sub.timeAndRemarkList??>
	                    				<input type="hidden" id="subTimeAndRemarkListSize_${l_index}_${sub_index}" _index="${sub_index}" value="<#if sub.timeAndRemarkList??>${sub.timeAndRemarkList?size}<#else>0</#if>" />
	                    				<#list sub.timeAndRemarkList as timeAndRemark>
	                    				<div class="clear"></div> 
	                    				<#if timeAndRemark.RECEIVE_TIME??>
	                    					<div class="WhoHandle fl">
				                        		<p>
				                        			<span class="FontDarkBlue">接收时间：</span>${timeAndRemark.RECEIVE_TIME}
				                        		</p>
			                        		</div>
			                        	</#if>
			                        	<#if timeAndRemark.END_TIME??>
			                        		<div id="subPersonHandleTime_${l_index}_${sub_index}_${timeAndRemark_index}" class="WhoHandle fl">
				                        		<p>
				                        			<span class="FontDarkBlue">办理时间：</span>${timeAndRemark.END_TIME}
				                        		</p>
				                        		<p><span class="FontDarkBlue">&nbsp;</span></p>
			                        		</div>
			                        	</#if>
				                        
				                        <#if timeAndRemark.REMARKS?? && (timeAndRemark.REMARKS!='') >
						                    <div id="subRemark_${l_index}_${sub_index}_${timeAndRemark_index}" class="HandleAdvice fl" style="word-break: break-all;">
						                    	<div class="DialogArrow"></div>
						                    	<span>${timeAndRemark.REMARKS}</span>
						                    </div>
					                    </#if>
					                    </#list>
	                    			</#if>
	                    			
	                    			<div class="clear"></div>
	                    		</div>
	                    	</#list>
	                    	</div>
	                    </#if>
	                    
	                    <!--催办信息展示-->
	                    <div class="clear"></div>
	                    <input type="hidden" id="remindListSize_${l_index}" _index="${l_index}" value="<#if l.remindList??>${l.remindList?size}<#else>0</#if>" />
	                    <#if (l.remindList?? && l.remindList?size>0)>
	                    	<div id="_remindList_${l.TASK_ID?c}" class="_isSubTask_${l.TASK_ID?c} <#if !(l.IS_CURRENT_TASK?? && l.IS_CURRENT_TASK)>hide</#if>">
	                    	<#list l.remindList as r>
			                    <div class="DBLink">
			                        <div id="remindPersonAndTime_${l_index}_${r_index}" class="WhoHandle fl">
			                            <p><span class="FontRed"><#if r.REMIND_USER_NAME??>[催办人]-${r.REMIND_USER_NAME}</#if></span> <b class="FontRed">催办</b> <span class="FontDarkBlue"><#if r.REMINDED_USER_NAME??>${r.REMINDED_USER_NAME}</#if></span></p>
			                            <p><#if r.REMIND_DATE_??><span class="FontDarkBlue">催办时间：</span>${r.REMIND_DATE_}</#if></p>
			                        </div>
			                        <#if r.REMARKS??>
				                        <div id="remindRemark_${l_index}_${r_index}" class="HandleAdvice fl" style="word-break: break-all;">
				                            <div class="DialogArrow"></div>
				                            <span>${r.REMARKS}</span>
				                        </div>
			                        </#if>
			                    	<div class="clear"></div>
			                    </div>
		                    </#list>
		                    </div>
	                    </#if>
	                </div>
	            </#list>
	        </#if>
	             
            <div class="f_pic"></div>
        </div>
    </div>
    
    <script type="text/javascript">
    	$(function() {
    		<#if isTaskAllUnfolded?? && isTaskAllUnfolded>
    		$("#_detailWorkflowDiv div .SubTaskOpen").each(function() {
    			$(this).parent().click();
    		});
    		</#if>
    	});
    	
    	function showSubTask(taskId, obj) {
    		var subTaskDiv = $(obj).children("div");
    		
    		$("#_detailWorkflowDiv ._isSubTask_"+taskId).toggle();//要先显示，因为隐藏时，不能获取到完整内容的宽度
    		
    		if($("#_detailWorkflowDiv ._isSubTask_"+taskId+":visible").length > 0) {
    			subTaskDiv.addClass("SubTaskClose");
    			subTaskDiv.attr("title", "收起意见");
    		} else {
    			subTaskDiv.removeClass("SubTaskClose");
    			subTaskDiv.attr("title", "展开更多意见");
    		}
    		
    		if($("#_detailWorkflowDiv ._isSubTask_"+taskId).filter(".hasSubTaskWidth").length == 0) {//为了只计算一次
				adjustSubTaskWidth();
				$("#_detailWorkflowDiv ._isSubTask_"+taskId).addClass("hasSubTaskWidth");//添加特定类，用于判断是否计算过宽度
			}
    	}
    	
    	function showSubPro(taskId, obj) {
    		var subProSpan = $(obj).children("span"),
    			isCurrent = $(obj).hasClass('LinkDotNow');
    		
    		$("#_detailWorkflowDiv ._isSubProInstance_"+taskId).toggle();
    		
    		if($("#_detailWorkflowDiv ._isSubProInstance_"+taskId+":visible").length > 0) {
    			subProSpan.css('margin', '-10px 0 0 -9px');
    			subProSpan.addClass("SubProClose");
    			subProSpan.attr("title", "收起更多");
    		} else {
    			subProSpan.css('margin', '-8px 0 0 -9px');
    			subProSpan.removeClass("SubProClose");
    			subProSpan.attr("title", "展开更多");
    		}
    		
    		if(isCurrent) {
    			subProSpan.css('margin-left', '-8px');
    		}
    	}
    	
    	function adjustSubTaskWidth() {//调整子任务的办理意见宽度
    		var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
    		var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.3;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
			var taskListSize = $("#taskListSize").val();	//任务记录数
			var handleTaskNameWidth = $('#_detailWorkflowDiv div.LinkList>div.WitchLink').eq(0).outerWidth();		//处理环节总宽度
			var handleLinkWidth = $('#_detailWorkflowDiv div.LinkList>div.LinkDot').eq(0).outerWidth() || $('#_detailWorkflowDiv div.LinkList>div.LinkDotNow').eq(0).outerWidth();			//办理环节宽度
			
			var subTaskSize = 0;				//子任务(会签任务和处理中任务)记录数
			var subTimeAndRemarkSize = 0;		//子任务办理时间、办理意见记录数
			var subTaskPersonAndTimeWidth = 0;	//子任务办理人/办理时间宽度
			var subTaskPersonHandleTimeWidth = 0;//子任务办理时间/接收时间宽度
			var subRemarkWidth = 0;				//子任务办理意见宽度
			var subTaskIndex = 0;				//子任务的总编号，亦即任务索引号
			
			var remindListSize = 0,				//催办任务记录数
				remindPersonAndTimeWidth = 0,	//催办人员及催办时间宽度
				remindRemarkWidth = 0;			//催办意见宽度
			
			/////////////////////////////子任务办理意见宽度调整 begin///////////////////////////
			//没有子任务的
			$("input[id ^= 'subTaskListSize_'][value='0']").addClass("hasSubTaskWidth");
			
			//将子流程隐藏，因为如果直接隐藏，办理意见宽度计算有误，会导致展示错乱
			$("#_detailWorkflowDiv div[class *= '_isSubProInstance_']").hide();
			
			//子任务办理意见宽度没有计算过的
			$("input[id ^= 'subTaskListSize_']:not('.hasSubTaskWidth')").each(function() {
				subTaskIndex = $(this).attr("_index");
				subTaskSize = $(this).val();//子任务记录数
				subTimeAndRemarkSize = 0;
				subTaskPersonAndTimeWidth = 0;
				subRemarkWidth = 0;
				
				for(var subIndex_ = 0; subIndex_ < subTaskSize; subIndex_++) {//subIndex_为子任务索引号
					//子任务办理意见可见时，开始计算其宽度
					if($("div[id ^= 'subRemark_"+subTaskIndex+"_"+subIndex_+"_']:visible").length > 0) {
						subTimeAndRemarkSize = $("#subTimeAndRemarkListSize_" + subTaskIndex +"_"+ subIndex_).val();
						
						for(var index_ = 0; index_ < subTimeAndRemarkSize; index_++){//index_同人同组织下的办理信息的索引号
							var subPersonHandleTimeDiv = $("#subPersonHandleTime_"+subTaskIndex+"_"+subIndex_+"_"+index_);
							
							if(subPersonHandleTimeDiv.length > 0) {
								subPersonHandleTimeWidth = subPersonHandleTimeDiv.outerWidth();
								
								if(subPersonHandleTimeDiv > maxHandlePersonAndTimeWidth) {
									subPersonHandleTimeDiv.width(maxHandlePersonAndTimeWidth);
									subPersonHandleTimeWidth = subPersonHandleTimeDiv.outerWidth();
								}
								
								subRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - subPersonHandleTimeWidth;
								$("#subRemark_"+subTaskIndex+"_"+subIndex_+"_"+index_).width(subRemarkWidth);//子任务意见宽度
							}
						}
						
						$(this).addClass("hasSubTaskWidth");
					}
				}
				
			});
			
			/////////////////////////////催办任务办理意见宽度调整 begin///////////////////////////
			//没有催办任务的
			$("input[id ^= 'remindListSize_'][value='0']").addClass("hasSubTaskWidth");
			
			//催办任务办理意见宽度没有计算过的
			$("input[id ^= 'remindListSize_']:not('.hasSubTaskWidth')").each(function() {
				subTaskIndex = $(this).attr("_index");
				remindListSize = $(this).val();//催办任务记录数
				remindPersonAndTimeWidth = 0;
				remindRemarkWidth = 0;
				
				//催办任务办理意见可见时，开始计算其宽度
				if($("div[id ^= 'remindRemark_"+subTaskIndex+"_']:visible").length > 0) {
					for(var index_ = 0; index_ < remindListSize; index_++){
						var remindPersonHandleTimeDiv = $("#remindPersonAndTime_"+subTaskIndex+"_"+index_);
						remindPersonAndTimeWidth = remindPersonHandleTimeDiv.outerWidth();
						
						if(remindPersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
							remindPersonHandleTimeDiv.width(maxHandlePersonAndTimeWidth);
							remindPersonAndTimeWidth = remindPersonHandleTimeDiv.outerWidth();
						}
						
						remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
						
						$("#remindRemark_"+subTaskIndex+"_"+index_).width(remindRemarkWidth);//催办意见宽度
					}
					
					$(this).addClass("hasSubTaskWidth");
				}
			});
    	}
    </script>
</body>

</html>