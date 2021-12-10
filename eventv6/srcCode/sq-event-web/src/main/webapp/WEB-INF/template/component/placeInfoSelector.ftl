<!-- 
#场所选择
说明：
#1、入口函数showPlaceInfoSelector，参数：无
#2、引用页面需要有回调函数placeInfoSelectorCallback，参数：plaId,plaName
 -->
 
<div id="PlaceInfoSelector" class="easyui-window" title="选择场所" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<iframe id="PlaceInfoSelectorIframe" scrolling='auto' frameborder='0' src='' style='width:99.9%;height:100%;'></iframe>
</div>
<script>
	var $placeInfoSelectorWin;
	var placeInfoSelectorInitFlag = false;
	var placeInfoSelectorTmpGridId = -1;

	function showPlaceInfoSelector(gridId) {
		if(!placeInfoSelectorInitFlag || placeInfoSelectorTmpGridId!=gridId) {
			var iframe = document.getElementById("PlaceInfoSelectorIframe");
			iframe.src = ("${rc.getContextPath()}/zhsq/placeInfo/listPlace.jhtml"+(gridId==null?"":("&gridId="+gridId)));
            placeInfoSelectorTmpGridId = gridId;
			placeInfoSelectorInitFlag = true;
		}
	    $placeInfoSelectorWin = $('#PlaceInfoSelector').window({
	    	title:"选择场所",
	    	width: 600,
	    	height: 450,
	    	top:($(window).height()-450)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-600)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$placeInfoSelectorWin.window('open');
	}
	
	function closePlaceInfoSelector() {
		$placeInfoSelectorWin.window('close');
	}
	
	//-- 选择完毕回调函数
	function placeInfoSelectComplete(plaId, plaName, roomAddress, plaType, plaTypeName) {
		closePlaceInfoSelector();
		placeInfoSelectorCallback(plaId, plaName, roomAddress, plaType, plaTypeName);
	}
</script>