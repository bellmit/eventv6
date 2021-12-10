
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/custom_msgClient.js" type="text/javascript" charset="utf-8"></script> 
<script type="text/javascript">
	
	var base = '${rc.getContextPath()}';
	var imgDomain = '${IMG_URL}';
	var uiDomain = '${uiDomain}';
	var componentsDomain = '${SQ_COMPONENTS_URL}';//公共组件工程域名
	
	$(function(){
        var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        }; 
        
        $("#eventWechatNorformDiv").width($(window).width());
        
        enableScrollBar('content-d',options); 
		
		var bigFileUploadOpt = {
			useType: 'view',
			attachmentData: {bizId:'<#if eventWechat.eventVerifyId??>${eventWechat.eventVerifyId?c}</#if>', attachmentType:'${attachmentType!}'},
			module: 'attachment',
			parentObj:window.parent,
			appcode: 'sqfile'
		};
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		
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