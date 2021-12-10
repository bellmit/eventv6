<!--
jquery easyui window 包装，用于页面弹出窗口。最大化
应用页面调用showMaxJqueryWindow(title, targetUrl);
参数：标题，窗口显示的页面地址

maximized: true
-->
<div id="MaxJqueryWindowContent"></div>

<script type="text/javascript">
	var dialogs = [];
	var maxJqueryWindowId;
	function showMaxJqueryWindow(title, targetUrl,width,height, maximizable, scroll, callBackOnClose) {
		var $MaxJqueryWindow;
		var top = 0;
		var left = 0;
		if(width==undefined || width==null) {
			width = $(window).width();
		}
		if(height==undefined || height==null) {
			height = $(window).height();
		}
		if(maximizable==undefined || maximizable==null){
			maximizable = false;
		}
		if(scroll==undefined || scroll==null){
			scroll = "no";
		}
		left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
		top = parseInt(($(window).height()-height)*0.5+$(document).scrollTop());
		maxJqueryWindowId = "MaxJqueryWindow_" + dialogs.length;
		var maxJqueryWin =  '<div id="'+ maxJqueryWindowId +'" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"'+
								'closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">'+
								"<iframe scrolling="+scroll+" frameborder='0' style='width:100%;height:100%;'></iframe>"+
							'</div>';
		var MaxJqueryWindowContentHtml = $("#MaxJqueryWindowContent").html();
		MaxJqueryWindowContentHtml += maxJqueryWin;
		$("#MaxJqueryWindowContent").html(MaxJqueryWindowContentHtml);
					
		$MaxJqueryWindow = $('#'+maxJqueryWindowId).window({
		    title: (title==null || title=="")?"信息窗口":title,
		    width: width,
		    height: height,
		    top: top,
		    left: left,
		    shadow: false,
		    modal: true,
		    closed: true,
		    closable: true,//是否有关闭按钮
		    minimizable: false,
		    maximizable: maximizable,
		    collapsible: false,
		    onBeforeClose: function () {
                 if(callBackOnClose != undefined && callBackOnClose != null) {
                 	callBackOnClose();
                 }
            },
            onClose: function(){
				var dialogTmp = dialogs.pop();
				$(dialogTmp).parent().remove();//防止已关闭的窗口再次加载信息
			}
		});
		
		$('#'+maxJqueryWindowId+' > iframe').attr('src', targetUrl);//减少多次请求
		
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		
		dialogs.push($MaxJqueryWindow);
		$MaxJqueryWindow.window('open');
	}

    function showMaxJqueryVideoWindow(title, targetVideoUrl,width,height, maximizable, scroll, callBackOnClose) {
        var $MaxJqueryWindow;
        var top = 0;
        var left = 0;
        if(width==undefined || width==null) {
            width = $(window).width();
        }
        if(height==undefined || height==null) {
            height = $(window).height();
        }
        if(maximizable==undefined || maximizable==null){
            maximizable = false;
        }
        if(scroll==undefined || scroll==null){
            scroll = "no";
        }
        left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
        top = parseInt(($(window).height()-height)*0.5+$(document).scrollTop());
        maxJqueryWindowId = "MaxJqueryWindow_" + dialogs.length;
        var maxJqueryWin =  '<div id="'+ maxJqueryWindowId +'" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"'+
                'closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">'+
				"	<embed type='application/x-vlc-plugin' name='video1' autoplay='yes' loop='no' width='100%' height='100%' target='"+ targetVideoUrl +"' />" +
//                '<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" id="vlc" events="True">' +
//				'<param name="MRL" value="" />' +
//				'<param name="ShowDisplay" value="True" />' +
//				'<param name="AutoLoop" value="False" />' +
//				'<param name="AutoPlay" value="False" />' +
//				'<param name="Volume" value="50" />' +
//				'<param name="toolbar" value="false" />' +
//				'<param name="StartTime" value="0" />' +
//				'<EMBED pluginspage="http://www.videolan.org"' +
//				'type="application/x-vlc-plugin"' +
//				'version="VideoLAN.VLCPlugin.2"' +
//				'toolbar="false"' +
//				'loop="true"' +
//				'text="Waiting for video"' +
//				'name="vlc">' +
//				'</EMBED>' +
//				'</object>' +
				'</div>';
        var MaxJqueryWindowContentHtml = $("#MaxJqueryWindowContent").html();
        MaxJqueryWindowContentHtml += maxJqueryWin;
        $("#MaxJqueryWindowContent").html(MaxJqueryWindowContentHtml);

        $MaxJqueryWindow = $('#'+maxJqueryWindowId).window({
            title: (title==null || title=="")?"信息窗口":title,
            width: width,
            height: height,
            top: top,
            left: left,
            shadow: false,
            modal: true,
            closed: true,
            closable: true,//是否有关闭按钮
            minimizable: false,
            maximizable: maximizable,
            collapsible: false,
            onBeforeClose: function () {
                if(callBackOnClose != undefined && callBackOnClose != null) {
                    callBackOnClose();
                }
            },
            onClose: function(){
                var dialogTmp = dialogs.pop();
                $(dialogTmp).parent().remove();//防止已关闭的窗口再次加载信息
            }
        });

//        $('#'+maxJqueryWindowId+' > video').attr('src', targetVideoUrl);//减少多次请求
//        $('#'+maxJqueryWindowId+' > video').play();
        if(window["$MaxJqueryWindowOnClose"]){
            $MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
        }

        dialogs.push($MaxJqueryWindow);
        $MaxJqueryWindow.window('open');
    }
	
	function showMaxJqueryWindowByParams(params) {
		var $MaxJqueryWindow;
		
		var defaultParam = {
			title: "信息窗口",
			targetUrl: '',
			top: null,
			left: null,
			width: $(window).width(),
			height: $(window).height(),
			scroll: "no",//是否有滚动条
			shadow: false,
		    modal: true,//是否有遮罩层
		    closed: true,//是否可关闭
		    closable: true,//是否有关闭按钮
		    minimizable: false,//是否可最小化
		    maximizable: false,//是否可最大化
		    collapsible: false,//是否可收起
		    onBeforeClose: function () {
		    	if(params.onBeforeClose && typeof params.onBeforeClose == 'function') {
		    		params.onBeforeClose();
		    	}
            },
            onClose: function(){
				var dialogTmp = dialogs.pop();
				$(dialogTmp).parent().remove();//防止已关闭的窗口再次加载信息
			}
		};
		
		$.extend(defaultParam, params);
		
		if(defaultParam.top == null || isNaN(defaultParam.top)) {
			defaultParam.top = parseInt(($(window).height()-defaultParam.height)*0.5+$(document).scrollLeft());
		}
		if(defaultParam.left == null || isNaN(defaultParam.left)) {
			defaultParam.left = parseInt(($(window).width()-defaultParam.width)*0.5+$(document).scrollTop());
		}
		
		var scroll = defaultParam.scroll;
		var targetUrl = defaultParam.targetUrl;
		
		maxJqueryWindowId = "MaxJqueryWindow_" + dialogs.length;
		var maxJqueryWin =  '<div id="'+ maxJqueryWindowId +'" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"'+
								'closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">'+
								"<iframe scrolling="+scroll+" frameborder='0' style='width:100%;height:100%;'></iframe>"+
							'</div>';
		var MaxJqueryWindowContentHtml = $("#MaxJqueryWindowContent").html();
		MaxJqueryWindowContentHtml += maxJqueryWin;
		$("#MaxJqueryWindowContent").html(MaxJqueryWindowContentHtml);
					
		$MaxJqueryWindow = $('#'+maxJqueryWindowId).window(defaultParam);
		$('#'+maxJqueryWindowId+' > iframe').attr('src', targetUrl);//减少多次请求
		
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		
		dialogs.push($MaxJqueryWindow);
		$MaxJqueryWindow.window('open');
	}
	
	function closeMaxJqueryWindow() {//关闭最后一个窗口 需要在打开窗口方法调用之前使用
		var len = dialogs.length;
		if(len > 0){
			var removeDialog = dialogs[len - 1];
			
			removeDialog.window('close');//会调用 onClose方法
		}
	}
	
	function closeBeforeMaxJqueryWindow() {//关闭之前所有的窗口 需要在打开窗口方法调用之后使用
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