<!-- 
#场所选择
说明：
#1、入口函数showPlaceInfoSelector，参数：无
#2、引用页面需要有回调函数placeInfoSelectorCallback，参数：plaId,plaName
 -->
 
<div id="MapMarkerSelector" class="easyui-window" title="选择标注" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<iframe id="MapMarkerSelectorIframe" scrolling='auto' frameborder='0' src='' style='width:99.9%;height:100%;'></iframe>
	<iframe id="crossDomainPage" scrolling='auto' frameborder='0' src='' style='width:0;height:0;'></iframe>
</div>
<script>
	var $mapMarkerSelectorWin;
	var mapMarkerSelectorInitFlag = false;
	var mapMarkerSelectorTmpGridId = -1;
	var initCenterPoints;

	function showMapMarkerInfoSelector(gridId, centerPoints) {
		initCenterPoints = centerPoints;
		if(!mapMarkerSelectorInitFlag || mapMarkerSelectorTmpGridId!=gridId) {
			var iframe = document.getElementById("MapMarkerSelectorIframe");
			iframe.src = ("${rc.getContextPath()}/zzgl/map/loadMap.jhtml?actionType=selectPoint&gridId="+(gridId==null?"":(gridId)));
            mapMarkerSelectorTmpGridId = gridId;
			mapMarkerSelectorInitFlag = true;
		}
	    $mapMarkerSelectorWin = $('#MapMarkerSelector').window({
	    	title:"选择地图位置",
	    	width: 550,
	    	height: 430,
	    	top:($(window).height()-430)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-550)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$mapMarkerSelectorWin.window('open');
		$mapMarkerSelectorWin.window('center');
	}
	
	function closeMapMarkerSelector() {
		$mapMarkerSelectorWin.window('close');
	}
	
	function getDefaultCenter(mapType) {
		var centerPoint = new Array();
		if(initCenterPoints && initCenterPoints.length>0) {
			for(var i=0; i<initCenterPoints.length; i++) {
				if(initCenterPoints[i].length>=3) {
					if(initCenterPoints[i][0]==mapType) {
						centerPoint.push(initCenterPoints[i][1]);
						centerPoint.push(initCenterPoints[i][2]);
						//--第三个参数
						if(initCenterPoints[i].length>=4) {
							centerPoint.push(initCenterPoints[i][3]);
						}
						break;
					}
				}
			}
			//第三个参数   不能标注
			if(centerPoint==null||centerPoint.length==0){
			   for(var i=0; i<initCenterPoints.length; i++) {
				if(initCenterPoints[i].length>=3) {
						//--第三个参数
						if(initCenterPoints[i].length>=4&&initCenterPoints[i][3]!=null) {
							centerPoint.push(initCenterPoints[i][3]);
							break;
					}
				}
			 }
		  }
		}
		return centerPoint;
	}
	
	//-- 选择完毕回调函数
	function setCenterLocation(mapType, x, y) {
		closeMapMarkerSelector();
		if(mapType==null || mapType=="") mapType='100';
		mapMarkerSelectorCallback(mapType, x, y);
	}
</script>