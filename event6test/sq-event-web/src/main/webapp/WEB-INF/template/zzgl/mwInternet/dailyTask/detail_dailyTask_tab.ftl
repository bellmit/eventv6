<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>
<body>
	<div class="MC_con content light">
		<div class="ConList">
	     	<div class="nav" id="tab" style="margin-top:10px;">
		        <ul>
		            <li class="current">基本情况</li>
		            <li>办理详情</li>
		        </ul>
	    	</div>
	 	</div>
	 	<div name="tab" id="content0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">日常任务信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>所属网格：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.infoOrgName)!}</span>
					</td>
					<td>
						<#if bo.createTimeStr ?? >
							<label class="LabName"><span>提交时间：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.createTimeStr)!}</span>
						</#if>
					</td>
				</tr>
				<#if bo.overTimeStr ?? >
					<tr>
						<td>
							<label class="LabName"><span>处理时限：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.overTimeStr)!}</span>
						</td>
						<td></td>
					</tr>
				</#if>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>任务标题：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.taskTitle)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>任务描述：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.taskDesc)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>附       件：</span></label>
						<div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div>
					</td>
				</tr>
			</table>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<th align="center" style="width: 120px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理环节</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理信息</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>处理意见</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>附件</span></label>
					</th>
				</tr>
				<#list taskList as task>
				    <tr>
				        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.TASK_NAME)!}<#if task.OPERATE_TYPE=='2'><br/>（驳回 ）<br/></#if></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">办理人：${(task.TRANSACTOR_NAME)!}<br/>办理时间：${(task.END_TIME)!}</td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;" title="${(task.REMARKS)!}"><div style="white-space:break-word;text-overflow:ellipsis;overflow:hidden;width:220px;">${(task.REMARKS)!}</div></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">
				        	<#if task.fileList ?? && (task.fileList?size > 0)>
	                			<#list task.fileList as file>
	                				<div>
										<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br/>
									</div>
							    </#list>
				            </#if>
				        </td>
				    </tr>
				</#list>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	$(function(){
		var $NavDiv2 = $("#tab ul li");
		$NavDiv2.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = $NavDiv2.index(this);
			$("div[id^='content']").hide();
			$("#content"+NavIndex2).show();
		});
		
		var swfOpt1 = {
	    		positionId:'fileupload',//附件列表DIV的id值',
				type:'detail',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${bo.ddtId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE}'},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 
	    };
	    
		fileUpload(swfOpt1);
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
