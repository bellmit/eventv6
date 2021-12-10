<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片上传</title>
<link href="${rc.getContextPath()}/css/normal.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/css/add_people.css" rel="stylesheet" type="text/css" /
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link href="${rc.getContextPath()}/css/jquery.mCustomScrollbar.css" rel="stylesheet"  type="text/css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />

<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/jquery-1.7.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery.form.js" ></script>

<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>

<body>
	<form id="multiFilesForm" action="${rc.getContextPath()}/zhsq/event/eventDisposalController/uploadMultiFiles.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" id="bizId" name="bizId" value="${eventId?c}" />
		<div class="MC_con content light">
			<div class="h_20"></div>
			<div class="NorForm LeftForm">
				<ul>
					<li><label class="LabName" style="width:85px;">处理前图片：</label><div id="fileupload"></div></li>
	    			<li><label class="LabName" style="width:85px;">处理后图片：</label><div id="fileuploaded"></div></li>
				</ul>
			</div>
			<div class="BigTool">
				<div class="BtnList" style="width:301px;">
					<a href="###" class="BigNorToolBtn GreenBtn" onclick="cancle();"><img src="${rc.getContextPath()}/images/cancel.png" />取消</a>
					<a href="###" class="BigNorToolBtn BlueBtn" onclick="save();"><img src="${rc.getContextPath()}/images/sys1_25.png" />保存</a>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
<script type="text/javascript">
	$(function(){
		swfUpload1 = fileUpload({
			positionId:'fileupload',//附件列表DIV的id值',
			type:'edit',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'bizId':${eventId?c},'attachmentType':'${type}','eventSeq':1},
			ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
			file_types:'*.jpg;*.gif;*.png;*.jpeg'
		});
		
		swfUpload2 = fileUpload({
			positionId:'fileuploaded',//附件列表DIV的id值',
			upload_table:'upload_table1',
			cancel_button:'cancel_button1',	
			type:'edit',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'bizId':${eventId?c},'attachmentType':'${type}','eventSeq':3},
			ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
			file_types:'*.jpg;*.gif;*.png;*.jpeg'
		});
	});
	
	function save(){
		$("#multiFilesForm").ajaxSubmit(function(data) {
			if(data.result){
				parent.closeMaxJqueryWindow();
			}else{
				$.messager.alert('错误','图片保存失败','error');
			}
		});
    	
    	$.blockUI({message: "保存中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'42%',
		    		background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
	}
	
	function cancle(){
		parent.closeMaxJqueryWindow();
	}
</script>