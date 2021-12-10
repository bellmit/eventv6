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
	function showMaxJqueryWindow(title, targetUrl,width,height,scroll) {
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
		//left = parseInt(($(window).width() - width)/2);
		//top = parseInt(($(window).height() - height)/2);
		left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
		top = Math.abs(parseInt(($(window).height()-height)*0.5+$(document).scrollTop()));
		//alert($(window).width()+","+width+","+$(window).height()+","+height+","+top+","+left);
		$("#MaxJqueryWindow").empty().html("<iframe scrolling='"+scroll+"' frameborder='0' src='"+targetUrl+"' style='width:100%;height:100%;'></iframe>")
		$MaxJqueryWindow = $('#MaxJqueryWindow').window({
		    title: (title==null || title=="")?"信息窗口":title,
		    width: width,
		    height: height,
		    top: top,
		    left: left,
		    shadow: true,
		    modal: true,
		    closed: true,
		    minimizable: false,
		    maximizable: false,
		    collapsible: false, 
		    onBeforeClose: function () {
		    	try{
			    	if(callBackOnClose != undefined && callBackOnClose != null) {
	                 	callBackOnClose();
	                 }	
		    	}
		    	catch(e){
		    	}
              
            }
		});
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		$MaxJqueryWindow.window('open');
	}
	
	function showMaxJqueryWindowForCrossDomain(title, targetUrl,width,height) {
		var top = 0;
		var left = 0;
		if(width == undefined) {
			width = $(window).width();
		}
		if(height == undefined) {
			height = $(window).height();
		}
		left = parseInt(($(document).width() - width)/2) + $(document).scrollLeft();
		top = parseInt($(document).scrollTop());
		//alert($(window).width()+","+width+","+$(window).height()+","+height+","+top+","+left);
		$("#MaxJqueryWindow").empty().html("<iframe scrolling='no' frameborder='0' src='"+targetUrl+"' style='width:99.9%;height:100%;'></iframe>")
		$MaxJqueryWindow = $('#MaxJqueryWindow').window({
		    title: (title==null || title=="")?"信息窗口":title,
		    width: width,
		    height: height,
		    top: top,
		    left: left,
		    shadow: true,
		    modal: true,
		    closed: true,
		    minimizable: false,
		    maximizable: true,
		    collapsible: false
		});
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		$MaxJqueryWindow.window('open');
	}
	
	function closeMaxJqueryWindow() {
		$MaxJqueryWindow.window('close');
	}
	
	function modifyMaxJqueryWindowTitle(title) {
		$MaxJqueryWindow.panel({title:title});
	}
	
	function showMaxJqueryWindows(title, targetUrl,width,height, maximizable, scroll, callBackOnClose) {
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
		var maxJqueryWindowId = "MaxJqueryWindow_" + dialogs.length;
		var maxJqueryWin =  '<div id="'+ maxJqueryWindowId +'" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"'+
								'closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">'+
								"<iframe scrolling="+scroll+" frameborder='0' src='"+targetUrl+"' style='width:100%;height:100%;'></iframe>"+
							'</div>';
		var MaxJqueryWindowContentHtml = $("#MaxJqueryWindow").html();
		MaxJqueryWindowContentHtml += maxJqueryWin;
		$("#MaxJqueryWindow").html(MaxJqueryWindowContentHtml);
					
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
				dialogs.pop();
			}
		});
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		
		dialogs.push($MaxJqueryWindow);
		$MaxJqueryWindow.window('open');
	}
	
	function closeMaxJqueryWindows() {//关闭最后一个窗口
		var len = dialogs.length;
		if(len > 0){
			dialogs[len - 1].window('close');//会调用 onClose方法
		}
	}
	
	function closeBeforeMaxJqueryWindow() {//关闭之前所有的窗口 需要在打开窗口方法调用之后使用
		var len = dialogs.length;
		if(len > 1){
			var winDialog = dialogs[len - 1];//为了保存当前打开的窗口
			for(var index = len - 2; index >= 0; index--){
				dialogs[index].window('close');//会调用 onClose方法
			}
			dialogs = [];
			dialogs.push(winDialog);
		}
	}
	
	function showMaxJqueryWindowTop(title, targetUrl,width,height,scroll) {
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
		//left = parseInt(($(window).width() - width)/2);
		//top = parseInt(($(window).height() - height)/2);
		left = parseInt(($(window).width()-width)*0.5+$(document).scrollLeft());
		//top = Math.abs(parseInt(($(window).height()-height)*0.5+$(document).scrollTop()));
		//alert($(window).width()+","+width+","+$(window).height()+","+height+","+top+","+left);
		$("#MaxJqueryWindow").empty().html("<iframe scrolling='"+scroll+"' frameborder='0' src='"+targetUrl+"' style='width:100%;height:100%;'></iframe>")
		$MaxJqueryWindow = $('#MaxJqueryWindow').window({
		    title: (title==null || title=="")?"信息窗口":title,
		    width: width,
		    height: height,
		    top: top,
		    left: left,
		    shadow: true,
		    modal: true,
		    closed: true,
		    minimizable: false,
		    maximizable: false,
		    collapsible: false, 
		    onBeforeClose: function () {
		    	try{
			    	if(callBackOnClose != undefined && callBackOnClose != null) {
	                 	callBackOnClose();
	                 }	
		    	}
		    	catch(e){
		    	}
              
            }
		});
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		$MaxJqueryWindow.window('open');
	}
	
	
	
</script>