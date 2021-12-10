<!--
jquery easyui window 包装，用于页面弹出窗口。最大化
应用页面调用showMaxJqueryWindow(title, targetUrl);
参数：标题，窗口显示的页面地址

maximized: true
-->
<div id="MaxJqueryWindow" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"
	closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
</div>

<script type="text/javascript">
	var $MaxJqueryWindow;
	function showMaxJqueryWindow(title, targetUrl,width,height, maximizable, scroll, callBackOnClose) {
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
		//alert($(window).width()+","+width+","+$(window).height()+","+height+","+top+","+left);
		$("#MaxJqueryWindow").empty().html("<iframe scrolling="+scroll+" frameborder='0' src='"+targetUrl+"' style='width:100%;height:100%;'></iframe>")
		$MaxJqueryWindow = $('#MaxJqueryWindow').window({
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
            }
		});
		if(window["$MaxJqueryWindowOnClose"]){
			$MaxJqueryWindow.window({onClose:window["$MaxJqueryWindowOnClose"]});
		} 
		$MaxJqueryWindow.window('open');
	}
	
	function closeMaxJqueryWindow() {
		$MaxJqueryWindow.window('close');
	}
</script>