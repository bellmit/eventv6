<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页结案</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>
<body>
	<div title="请上传处理后图片" id="editInfo" style="margin:0">
		<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
				<input type="hidden" id="eventId" name="eventId" value="<#if eventId??>${eventId?c}</#if>"/>
				<input type="hidden" id="sendSMSFlag" name="sendSMSFlag" value="1"/>
				<tr class="item">
						<td class="itemtit"">&nbsp;处理后图片：</td>
						<td class="border_b" colspan="4">
							<div id="fileupload"></div>
						</td>
					</tr>
					<tr class="item">
					<td class="itemtit" colspan="2" style="text-align:right">&nbsp;
						<a href="javascript:void(0)" class="easyui-linkbutton" style="line-height:0px;" data-options="iconCls:'icon-undo'" onclick="javascript:save();">确认结案</a>
					</td>
					</tr>
				</table>
		</form>
	</div>
</body>

<script type="text/javascript">
	$(function(){		
		<#if eventId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${eventId?c},'attachmentType':'${type}','eventSeq':3},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
		<#else>
			swfUpload = fileUpload({ 
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'eventSeq':3},//未处理
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
		</#if>
	});
	
	function save(){
		modleopen();
		var eventId = $("#eventId").val();
		if(eventId!=undefined && eventId!=null && eventId!=""){
			$.ajax({
				type: "POST",
	    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
				data: 'eventId='+ eventId +'&toClose=1',
				dataType:"json",
				success: function(data){
					if(parent.parent.startWorkFlow != undefined){
						parent.parent.startWorkFlow(data);
			   		}else if(parent.startWorkFlow != undefined){
			   			parent.startWorkFlow(data);
			   		}else{
			   			$.messager.alert('错误','连接错误！','error');
			   		}
				},
				error:function(data){
					$.messager.alert('错误','连接错误！','error');
				}
	    	});
		}else{
			parent.tableSubmit('saveEvent',parent.parent.startWorkFlow, "1");
		}
	}
	
</script>
</html>
