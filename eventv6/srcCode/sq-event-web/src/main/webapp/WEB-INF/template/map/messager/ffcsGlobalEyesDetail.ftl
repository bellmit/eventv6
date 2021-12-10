<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ffcs全球眼-概要信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">

	body, html {overflow:auto;}
</style>

</head>
<body>
	<div class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false" closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
	<iframe id="showIframe" frameborder='0' style='width:100%;height:100%;' src="${targetURL!''}"></iframe>
	</div>
</body>
<script type="text/javascript">
	$(function(){
        var deviceNums = "${deviceNums!''}";
        var url = "${rc.getContextPath()}/zhsq/alarm/videoSurveillanceController/globalEyesShowUrl.jhtml?deviceNums="+deviceNums;
        $.ajax({
            type : "POST",
            url : url,
            dataType:"json",
            async : true,
            success : function(data){
                if(data.rtspUrl != null && data.rtspUrl != ''){
                    $('#showIframe').attr('src', data.rtspUrl);//减少多次请求
                }else{
                    $.messager.alert("提示", '获取不到播放地址(摄像头不在线)!',"error");
                }
            },
            error : function(){
                $.messager.alert("提示","请求错误！","error");
            }
        });
	})
</script>
</html>
