<!-- 
#队伍选择
说明：
#1、入口函数showTeamSelector，参数：无
#2、引用页面需要有回调函数teamSelectorCallback，参数：teamId,buildingName
 -->
 
<div id="TeamSelector" class="easyui-window" title="选择数据" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<iframe id="TeamSelectorIframe" scrolling='auto' frameborder='0' src='' style='width:99.9%;height:100%;'></iframe>
</div>
<script>
	var $teamSelectorWin;
	var teamSelectorInitFlag = false;
	var teamSelectorTmpGridId = -1;

    var jqRow; 

	function showTeamSelector(o,teamId, reload) {
	     jqRow = o.parentNode.parentNode;
	     var teamId=$("input[name=registerUnitId]", jqRow).val();
	     
		if(reload||!teamSelectorInitFlag || teamSelectorTmpGridId!=teamId) {
			var iframe = document.getElementById("TeamSelectorIframe");
			
			var href = "${rc.getContextPath()}/zhsq/team/search/index.jhtml?1=1";
			if(teamId){
				href+= "&teamId="+teamId;
			}
            href+= "&isUnit=1";//是责任单位
            href+= "&bizType=0";//综治机构
            href+= "&teamType=02";//综治办
			iframe.src = href;
			teamSelectorTmpGridId = teamId;
			teamSelectorInitFlag = true;
		}
	    $teamSelectorWin = $('#TeamSelector').window({
	    	title:"选择信息",
	    	width: 700,
	    	height: 380,
	    	top:($(window).height()-360)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-600)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$teamSelectorWin.window('open');
		//if(blCenter){
		    $teamSelectorWin.window('center');
		//}
	}
	
	function closeTeamSelector() {
		$teamSelectorWin.window('close');
	}
	
	//-- 选择完毕回调函数
	function teamSelectComplete(teamId, name, teamTypeStr, row) {
		closeTeamSelector();
		teamSelectorCallback(teamId, name, teamTypeStr, row);
	}
</script>