<!--
jquery easyui window 包装，用于页面弹出窗口。最大化
应用页面调用showMaxJqueryWindow(title, targetUrl);
参数：标题，窗口显示的页面地址

maximized: true
-->
<div id="MaxJqueryWindowForCross" class="easyui-window" title="信息窗口" minimizable="false" maximizable="false" resizable="false" collapsible="false" inline="false"
	closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
</div>

<script type="text/javascript">
	var $MaxJqueryWindowForCross;
	function showMaxJqueryWindowForCross(title, targetUrl,width,height) {
		var top = 0;
		var left = 0;
		if(width == undefined || width == "") {
			width = $(window).width();
		}
		if(height == undefined || height == "") {
			height = $(window).height();
		}
		left = parseInt(($(window).width() - width)/2) + $(document).scrollLeft();
		top = parseInt(($(window).height() - height) / 2) + $(document).scrollTop();
		$("#MaxJqueryWindowForCross").empty().html("<iframe allowfullscreen='true' webkitallowfullscreen='true' mozallowfullscreen='true' allowusermedia='allowusermedia' allow='microphone;camera;midi;encrypted-media;' scrolling='no' frameborder='0' src='"+targetUrl+"' style='width:99.9%;height:100%;'></iframe>")
		$MaxJqueryWindowForCross = $('#MaxJqueryWindowForCross').window({
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
		if(window["$MaxJqueryWindowForCrossOnClose"]){
			$MaxJqueryWindowForCross.window({onClose:window["$MaxJqueryWindowForCrossOnClose"]});
		} 
		$MaxJqueryWindowForCross.window('open');
	}
	
	function closeMaxJqueryWindowForCross() {
		$MaxJqueryWindowForCross.window('close');
	}
	
	function modifyMaxJqueryWindowForCrossTitle(title) {
		$MaxJqueryWindowForCross.panel({title:title});
	}
</script>