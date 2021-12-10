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
	<div id="content-d" class="MC_con content light">
		<div class="NorForm">
			<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue" style="margin-top:0px;" >日常任务信息</div></td>
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
