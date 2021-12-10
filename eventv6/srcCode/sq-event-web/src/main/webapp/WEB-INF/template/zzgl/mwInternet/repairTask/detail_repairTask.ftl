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
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>所属网格：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.infoOrgName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>故障类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultTypeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联系人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkUser)!}</span>
					</td>
					<td>
						<label class="LabName"><span>电话：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkTel)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>上报时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.repairTimeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>预约时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.orderTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd RightTd">
						<label class="LabName"><span>故障地址：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultAddr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd RightTd">
						<label class="LabName"><span>故障描述：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultDesc)!}</span>
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
				ajaxData: {'bizId':${bo.drtId?c},'attachmentType':'${bizType}'},
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
