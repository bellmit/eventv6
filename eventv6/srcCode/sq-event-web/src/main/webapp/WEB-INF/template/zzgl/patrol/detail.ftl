<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡防</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript">
	<#if source??>
		document.domain = "${updomain!}";
	</#if>
</script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>
<body>
<div>
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
	                <div class="NorForm NorForm2 ThreeColumn">
                	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/patrolController/save.jhtml"  method="post">
                   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                       <tr>
                       	<#if patrol.status?? && patrol.status!="0">
                        <td class="LeftTd"><label class="LabName">编号：</label>
                        	<#if patrol.code??>${patrol.code}</#if>
                        </td>
                        </#if>
                        <td class="LeftTd" <#if patrol.status?? && patrol.status=="0"> colspan="2"</#if>><label class="LabName">所属网格：</label>
	                        <input type="hidden" id="patrolId" name="patrolId" value="<#if patrol.patrolId??>${patrol.patrolId}</#if>">
	                        <input type="hidden" id="gridId" name="gridId" value="<#if patrol.gridId??>${patrol.gridId}</#if>">
					 		<input type="hidden" id="gridCode" name="gridCode" value="<#if patrol.gridCode??>${patrol.gridCode}</#if>">
                        	<#if patrol.gridName??>${patrol.gridName}</#if>
                        </td>
                        
                      </tr>
                      
                      <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName">事件名称：</label>
                        	<div class="Check_Radio" style="width:460px;"><#if patrol.name??>${patrol.name}</#if></div>
                        </td>
            		</tr>
            		
            		 <tr>
                        <td class="LeftTd"><label class="LabName">带队负责人：</label>
                        	<#if patrol.principal??>${patrol.principal}</#if>
                        </td>
                        <td><label class="LabName">联系电话：</label>
                        	<#if patrol.principalTel??>${patrol.principalTel}</#if>
                        </td>
                     </tr>
                     
                     <tr>
                        <td class="LeftTd"><label class="LabName">巡防开始时间：</label>
                        	<#if patrol.startPatrolTimeStr??>${patrol.startPatrolTimeStr}</#if>
                        </td>
                        <td><label class="LabName">巡防结束时间：</label>
                        	<#if patrol.endPatrolTimeStr??>${patrol.endPatrolTimeStr}</#if>
                        </td>
                     </tr>
                        
                     <tr>
                        <td class="LeftTd"><label class="LabName">巡防区域：</label>
                        	<div class="Check_Radio" style="width:250px;"><#if patrol.occurred??>${patrol.occurred}</#if></div>
                        </td>
                        <td><label class="LabName">巡防人数：</label>
                        	<#if patrol.involvedNum??>${patrol.involvedNum}</#if>（人）
                        </td>
            		</tr>
                     
                      <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName">巡防情况：</label>
                        	<div class="Check_Radio" style="width:460px;"><#if patrol.content??>${patrol.content}</#if></div>
                        </td>
            		</tr>
                      
            		<tr>
                        <td colspan="2" class="LeftTd"><label class="LabName">处置情况：</label>
                        	<div class="Check_Radio" style="width:460px;"><#if patrol.handleResult??>${patrol.handleResult}</#if></div>
                        </td>
            		</tr>
                     
                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName">巡防照片：</label>
                        <div id="fileupload" class="ImgUpLoad"></div></td>
            		</tr>    
                      
                    </table>
                    </form>
                    </div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">关闭</a>
        </div>
    </div>
</div>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">
$(function(){
	<#if patrol.patrolId??>
		fileUpload({ 
			positionId:'fileupload',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'bizId':'${patrol.patrolId?c}','attachmentType':'${bizType}'},//未处理
			ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）
			file_types:'*.jpg;*.gif;*.png;*.jpeg;*.zip;*.doc;*.docx;*.xls;*.txt'
		});
	</#if>
})

	
	function cancel(){
		<#if source?? && source = 'workPlatform'>
			parent.parent.top.topDialog.closeDialog();
		<#elseif  source?? && source = 'oldWorkPlatform'>
			//var iframeUrl = "${rc.getContextPath()}/zhsq/event/eventDisposalController/isDomain.jhtml?";
			//iframeUrl += "&source=${source}";
			//$("#crossOverIframe").attr("src", iframeUrl);
			parent.closeMaxJqueryWindow();
		<#else>
			parent.closeMaxJqueryWindow();
		</#if>
		
	}
	
</script>

<script>
(function($){
	$(window).load(function(){
		var options = {
			axis : "yx",
			theme : "dark"
		};
		enableScrollBar('content-d',options);
	});
})(jQuery);
</script>
<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" />
</html>