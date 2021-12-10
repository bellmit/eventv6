<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件评论</title>
<link href="${rc.getContextPath()}/css/normal.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/css/add_people.css" rel="stylesheet" type="text/css" /
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" >
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery.form.js" ></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
</head>
<body>
		<div title="请输入评价信息" id="editInfo" style="margin:0">
			<form id="commentForm" action="${rc.getContextPath()}/zhsq/event/eventDisposalController/commentEvent.jhtml" method="post">
				<input type="hidden" name="eventId" id="eventId" value="<#if eventId??>${eventId?c}</#if>"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="60" align="right">评价等级：</td>
						<td>
							<input type="radio" name="evaLevel" value="01" id="evaLevel" >不满意&nbsp;
			    			<input type="radio" name="evaLevel" value="02" id="evaLevel" checked>满意&nbsp;
			    			<input type="radio" name="evaLevel" value="03" id="evaLevel" ><label for="verySatisfyLevel">非常满意</label> 
						</td>
					</tr>
					<tr>
						<td width="60" align="right">评价意见：</td>
						<td>
							<textarea rows="5" cols="50" id="evaContent" name="evaContent" class="easyui-validatebox" data-options="required:true,validType:'maxLength[255]'"></textarea>
						</td>
					</tr>
				</table>
				<div class="BigTool">
					<div class="BtnList" style="width:301px;">
						<a href="###" class="BigNorToolBtn GreenBtn" onclick="cancle();"><img src="${rc.getContextPath()}/images/cancel.png" />取消</a>
						<a href="###" class="BigNorToolBtn BlueBtn" onclick="javascript:save();"><img src="${rc.getContextPath()}/images/sys1_25.png" />保存</a>
					</div>
				</div>
			</form>
		</div>
	<script type="text/javascript">
	
		//控制不满意评价
		function radio_click(obj){
			if(obj.value=='01'){
   				//$("#dissf").css("display","inline");//显示
   				//$("#opinion").css("display","inline");
   				//$("#evaContent").attr("data-options","required:true,validType:'maxLength[255]'");
			}
			else{
				$("#dissf").css("display","none");//隐藏
				$("#opinion").css("display","none");
				//$("#evaContent").attr("data-options","validType:'maxLength[255]'");
			}
		}
	
		//驳回
		function reject(){
			var evaLevel = $('input[name=evaLevel]:checked').val();			
			if(evaLevel =="01"){
				var evaContent = $('#evaContent').val();
				if(evaContent==null || evaContent==''){
					$.messager.alert('错误','请填写评价意见','error');
					return false;
				}
			}
			$("#commentForm").attr("action", "${rc.getContextPath()}/zzgl/event/requiredEvent/reject.jhtml");
			$('#commentForm').submit();
			$.blockUI({message: "正在处理，请稍候..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
		    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		    return true;
		}
		
		function save(){
			var eventId = $("#eventId").val();
			var evaLevel = $('input[name=evaLevel]:checked').val();
			if(evaLevel==null || evaLevel==''){
				$.messager.alert('错误','请选择评价等级','error');
				return false;
			}
			
			/*if(evaLevel =="01"){
				var evaContent = $('#evaContent').val();
				if(evaContent==null || evaContent==''){
					$.messager.alert('错误','请填写评价意见','error');
					return false;
				}
			}*/
			
			var isValid =  $("#commentForm").form('validate');
			if(isValid){
				$("#commentForm").ajaxSubmit(function(data) {
					if(data.result){
						parent.setEvaluateId(data.result, '评论成功');
						parent.closeMaxJqueryWindow();
					}else{
						$.unblockUI();
						$.messager.alert('错误','评论失败','error');
					}
				});
				
				$.blockUI({message: "保存中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
		    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
			}
	    	
			return true;
		}
		
		function cancle(){
			parent.closeMaxJqueryWindow();
		}
	</script>
</body>
</html>
