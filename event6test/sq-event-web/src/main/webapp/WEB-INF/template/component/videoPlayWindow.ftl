<!--
jquery easyui window 包装，用于页面弹出窗口。最大化
应用页面调用showMaxJqueryWindow(title, targetUrl);
参数：标题，窗口显示的页面地址

maximized: true
-->
<div id="MaxJqueryWindow" class="easyui-window" title="信息窗口" minimizable="false" maximizable="false" resizable="false" collapsible="false" inline="false"
     closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
</div>

<script type="text/javascript">
	var dialogs = [];
    var $MaxJqueryWindow;
    function showVideoWindow(title, targetUrl,width,height,scroll) {
        var top = 0;
        var left = 0;
        if(width == undefined || width=='') {
            width = $(window).width();
        }
        if(height == undefined || height=='') {
            height = $(window).height();
        }
        if (scroll == undefined) {
            scroll = 'auto';
        }
        left = parseInt(($(window).width() - width)/2);
        top = parseInt(($(window).height() - height)/2);
        $("#MaxJqueryWindow").empty().html("<iframe id='playFrame' scrolling='"+scroll+"' frameborder='0' src='"+targetUrl+"' style='width:99.9%;height:100%;'></iframe>")
        $MaxJqueryWindow = $('#MaxJqueryWindow').window({
            title: (title==null || title=="")?"信息窗口":title,
            width: width,
            height: height,
            top: top,
            left: left,
            shadow: false,
            modal: true,
            closed: true,
            resizable:true,
            minimizable: false,
            maximizable: false,
            collapsible: false,
            onBeforeClose: function () {
                try{
                    if(callBackOnClose != undefined && callBackOnClose != null) {
                        callBackOnClose();
                    }
                }
                catch(e){}
            }
        });
        if(window["$MaxJqueryWindowOnClose"]){
            $MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
        }
        $MaxJqueryWindow.window('open');
    }

    function callBackOnClose() {
    	var iframe = document.getElementById('playFrame').contentWindow;
    	gmMsgClient.sent(iframe, "globalVideoMsg", {callName:"closeW"});
        iframe.closeW();
    }
	
	function closeVideoWindow() {//关闭最后一个窗口 需要在打开窗口方法调用之前使用
		var len = dialogs.length;
		if(len > 0){
			var removeDialog = dialogs[len - 1];
			
			removeDialog.window('close');//会调用 onClose方法
		}
	}
	
	function closeBeforeVideoWindow() {//关闭之前所有的窗口 需要在打开窗口方法调用之后使用
		var len = dialogs.length;
		if(len > 1){
			var winDialog = dialogs[len - 1],//为了保存当前打开的窗口
				removeDialog = null,
				removeDialogArray = $.merge([], dialogs);
			
			for(var index = len - 2; index >= 0; index--){
				removeDialog = removeDialogArray[index];
				dialogs = $.merge([], [removeDialog]);
				
				removeDialog.window('close');//会调用 onClose方法
			}
			dialogs = [];
			dialogs.push(winDialog);
		}
	}
</script>