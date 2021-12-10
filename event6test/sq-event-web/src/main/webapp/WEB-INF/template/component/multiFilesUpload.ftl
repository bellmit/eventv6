<!-- 
#上传照片
说明：
#1、入口函数showmultiFilesSelector
参数：
#selectorType 		图片上传操作类型，可设置有"add edit detail"，默认为add
#eventSeq	  		图片类型 1 为上传处理前照片；2 为上传处理后照片
#selectorInitType	图片上传初始化类型 默认为ajax
#fileTypes			可选择的图片类型，默认为：jpg; gif; png; jpeg

#2、引用页面需要有回调函数multiFilesCallback，参数：users
 -->
 
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>

<div id="multiFilesSelector" class="easyui-window" title="上传照片" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:550px;height:350px;padding:1px;overflow:hidden;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="border-t">
		<tr>
			<td class="border_b" colspan="4">
				<div id="fileupload"></div>
			</td>
		</tr>
	</table>
</div>

<script type="text/javascript">
	var multiFilesSelectorWin;
	
	function inintSelector(selectorType, eventSeq, selectorInitType, fileTypes){
		if(selectorType==undefined || selectorType==null || selectorType==""){
			selectorType = "add";
		}
		
		if(fileTypes==undefined || fileTypes==null || fileTypes==""){
			fileTypes = "*.jpg;*.gif;*.png;*.jpeg";
		}
		
		if(selectorInitType==undefined || selectorInitType==null || selectorInitType==""){
			selectorInitType = "ajax";
		}
		
		swfUpload = fileUpload({ 
			positionId:'fileupload',//附件列表DIV的id值',
			type:selectorType,//add edit detail
			initType:selectorInitType,//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'eventSeq':eventSeq},//未处理
			file_types:fileTypes
		});
	}
	
	function showmultiFilesSelector(selectorType, eventSeq, selectorInitType, fileTypes) {
		inintSelector(selectorType, eventSeq, selectorInitType, fileTypes);//初始化图片上传控件
		
	    $multiFilesSelectorWin = $('#multiFilesSelector').window({
	    	title:"上传照片",
	    	width: 515,
	    	height: 356,
	    	top:($(window).height()-400)*0.5+$(document).scrollTop(),
		    left:($(window).width()-600)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$multiFilesSelectorWin.window('open');
	}
</script>