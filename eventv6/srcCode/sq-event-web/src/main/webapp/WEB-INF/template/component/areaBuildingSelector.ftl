<!-- 
#楼栋选择
说明：
#1、入口函数showAreaBuildingSelector，参数：无
#2、引用页面需要有回调函数areaBuildingSelectorCallback，参数：buildingId,buildingName
 -->
 
<div id="AreaBuildingSelector" class="easyui-window" title="选择楼栋" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<iframe id="AreaBuildingSelectorIframe" scrolling='auto' frameborder='0' src='' style='width:99.9%;height:100%;'></iframe>
</div>
<script>
	var $areaBuildingSelectorWin;
	var areaBuildingSelectorInitFlag = false;
	var areaBuildingSelectorTmpGridId = -1;

	function showAreaBuildingSelector(gridId, blCenter) {
		if(!areaBuildingSelectorInitFlag || areaBuildingSelectorTmpGridId!=gridId) {
			var iframe = document.getElementById("AreaBuildingSelectorIframe");
			iframe.src = ("${rc.getContextPath()}/zhsq/building/areaBuildingInfo/listBuilding.jhtml?roomSelect=false"+(gridId==null?"":("&gridId="+gridId)));
			areaBuildingSelectorTmpGridId = gridId;
			areaBuildingSelectorInitFlag = true;
		}
	    $areaBuildingSelectorWin = $('#AreaBuildingSelector').window({
	    	title:"选择楼栋",
	    	width: 600,
	    	height: 360,
	    	top:($(window).height()-360)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-600)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$areaBuildingSelectorWin.window('open');
		//if(blCenter){
		    $areaBuildingSelectorWin.window('center');
		//}
	}
	
	function closeAreaBuildingSelector() {
		$areaBuildingSelectorWin.window('close');
	}
	
	//-- 选择完毕回调函数
	function areaBuildingSelectComplete(buildingId, buildingName,buildingAddress) {
		closeAreaBuildingSelector();
		areaBuildingSelectorCallback(buildingId, buildingName,buildingAddress);
	}
</script>