<script type="text/javascript">
	$(function(){
        var options = { 
            axis : "y", 
            theme : "minimal-dark" 
        }; 
        
        $("#eventWechatNorformDiv").width($(window).width());
        
        enableScrollBar('content-d',options); 
		
		var swfOpt = {
	    	positionId:'fileupload',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',
			imgDomain:'${imgDownPath!}',
			showPattern: 'pic',
			ajaxData: {'bizId':'<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>','attachmentType':'${attachmentType!}'}
	    };
	    
		fileUpload(swfOpt);
		
    });
	
	/*
		markerOperation:地图标注操作类型
						 0表示添加
						 1表示编辑
						 2表示查看
		isEdit:是否是编辑状态
	*/
	function showMap(){
		var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
			width = 480,
			height = 360,
			gridId = $("#gridId").val(),
			markerOperation = $('#markerOperation').val(),
			id = '<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>',
			mapType = $("#module").val(),
			isEdit = false,
			parameterJson = {
				"id": id,
				"name":''
			};
		
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	
	function closeWin() {
		parent.closeMaxJqueryWindow();
	}
</script>