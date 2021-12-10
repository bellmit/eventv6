<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />

	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/ui/css/normal.css"  />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />

</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>申请类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.applyTypeCN)!}</span>
					</td>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>法官姓名：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.courtName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>所属部门：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.department)!}</span>
					</td>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.contactInformation)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span><label class="Asterik">*</label>申请时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.applyDateStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>所属网格：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.gridName)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>事项说明：</span></label>
						<span style="width: 80%" class="Check_Radio FontDarkBlue">${(bo.itemDescription)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>状态：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.statusCN)!}</span>
					</td>
					<td>
						<label class="LabName"><span>申请人姓名：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatorName)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>满意度：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.satisfactionCN)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>办理意见：</span></label>
						<span style="width: 80%" class="Check_Radio FontDarkBlue">${(bo.advice)!}</span>
					</td>
				</tr>
			</table>
		</div>

		<#if instanceId?? && (instanceId > 0)>
			<div class="h_20"></div>
			<div class="ConList">
				<div class="nav" id="tab">
					<ul>
						<#if instanceId??>
							<li id="01_li" class="current">处理环节</li>
						</#if>
					</ul>
				</div>
				<div class="ListShow ListShow2">
					<#if instanceId??>
						<div id="01_li_div" class="t_a_b_s">
							<div id="workflowDetail" border="false"></div>
						</div>
					</#if>
				</div>
			</div>
			<div class="h_20"></div>
		</#if>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>

<!--流程展示JS-->
<script>
	$(function(){
		var _winHeight = $(window).height();
		var _winWidth = $(window).width();

		<#if instanceId??>
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

						handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;

						$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度

						for(var index_ = 0; index_ < remindSize; index_++){
							remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
							remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
							$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
						}
					}

					adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
					var html = "<i class=\"TaskCurrent\"></i>当前环节" +
							"<i class=\"TaskHistory\"></i>历史环节"
					$("#iconDiv").html(html)
				}
			});
		</#if>
	});


</script>
</html>
