<!--
jquery easyui window 包装，自定义弹出窗口。
自定义弹出框的width,height
应用页面调用showCustomEasyWindow(title,width,height,targetUrl);

自定义弹出框的width,height,top,left
showCustomEasyWindow2(title,width,height,top,left,targetUrl)
参数：标题，窗口显示的页面地址,top,left,width,height
-->
<div id="CustomEasyWin" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"
	closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
</div>

<script type="text/javascript">
	var $CustomEasyWin;
	function showCustomEasyWindow(title,targetUrl,width,height, maximizable, scroll, callBackOnClose) {
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
		$("#CustomEasyWin").empty().html("<iframe scrolling="+ scroll +" frameborder='0' id='CustomEasyWinIframe' src='"+targetUrl+"' style='width:99.9%;height:100%;'></iframe>");
		$CustomEasyWin = $('#CustomEasyWin').window({
		    title: (title==null || title=="")?"信息窗口":title,
		    top:($(window).height()-height)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-width)*0.5+$(document).scrollLeft(),
		    width:width,
		    height:height,
		    shadow: false,
		    modal: true,
		    closed: true,
		    minimizable: false,
		    maximizable: maximizable,
		    collapsible: false,
		    onBeforeClose: function () {
		    	try{
		    		var winIframe = document.getElementById("CustomEasyWinIframe");
		    	 	if(typeof winIframe.contentWindow.TV_Disable == 'function'){
		    	 		winIframe.contentWindow.TV_Disable();//为了点击窗口关闭按钮时，能关闭设备
		    	 	}
		    	}catch(e){}
		    	
                if(callBackOnClose != undefined && callBackOnClose != null) {
                	callBackOnClose();
                }
            }
		});
		$CustomEasyWin.window('open');
	}
	
	function showCustomEasyWindow2(title,targetUrl,width,height,top,left) {
		$("#CustomEasyWin").empty().html("<iframe scrolling='auto' frameborder='0' src='"+targetUrl+"' style='width:99.9%;height:100%;'></iframe>")
		$CustomEasyWin = $('#CustomEasyWin').window({
		    title: (title==null || title=="")?"信息窗口":title,
		    top:top,
	    	left:left,
		    width:width,
		    height:height,
		    shadow: true,
		    modal: true,
		    closed: true,
		    minimizable: false,
		    maximizable: false,
		    collapsible: false
		});
		$CustomEasyWin.window('open');
	}
	
	function closeCustomEasyWin() {
		$CustomEasyWin.window('close');
	}
</script>