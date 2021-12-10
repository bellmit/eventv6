<!-- 
#网格选择
说明：
#1、入口函数showSingleMixedGridSelector，参数：无
#2、引用页面需要有回调函数singleMixedGridSelectZtreeCallback，参数：gridId,gridName,orgId,orgCode,gridPhoto
 -->
 
<div id="SingleMixedGridSelector" class="easyui-window" title="选择网格" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td style="background-color: #F3F8FE;">
				<div id="singleMixedGridSelectorZTree" class="ztree" style="height:230px; width:230px; overflow:auto;"></div>
            </td>
		</tr>
		<tr>
			<td align="center">
				<input type="button" value="确定" onclick="singleMixedGridSelectComplete()" />
				<input type="button" value="关闭" onclick="closeSingleMixedGridSelector()" />
			</td>
		</tr>
	</table>
</div>
<script>
	var $singleMixedGridSelectorWin;
	var treeInitFlag = false;

	function showSingleMixedGridSelector(blCenter, gridId) {
		if(!treeInitFlag) {
			loadMixedGridTree(gridId);
			treeInitFlag = true;
		}
	    $singleMixedGridSelectorWin = $('#SingleMixedGridSelector').window({
	    	title:"选择网格（双击或者选中节点）",
	    	width: 260,
	    	height: 306,
	    	top:($(window).height()-306)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-260)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$singleMixedGridSelectorWin.window('open');
		//if(blCenter){
		    $singleMixedGridSelectorWin.window('center');
		//}
	}
	
	function closeSingleMixedGridSelector() {
		$singleMixedGridSelectorWin.window('close');
	}
	
	function loadMixedGridTree(gridId) {
		var setting = {
			async: {
				enable: true,
				url:"${rc.getContextPath()}/zhsq/grid/mixedGrid/gridZTree.json",
				autoParam:["id=gridId"],
				dataFilter: filter,
				otherParam: {"startGridId":(gridId?gridId:-1)}
			},
			check:{
				enable: true,
				chkStyle: "checkbox",
				chkboxType: { "Y": "s", "N": "s" }
			},
			callback:{
				onDblClick:zTreeOnDblClick
			}
		};
		$.fn.zTree.init($("#singleMixedGridSelectorZTree"), setting);
	}
	
	function zTreeOnDblClick(event, treeId, treeNode) {
		if(treeNode.id<=0) return;//禁止选择：'网格信息'
    	singleMixedGridSelectCallback(treeNode.id, treeNode.name, treeNode.orgId, treeNode.orgCode, treeNode.gridPhoto);
    	closeSingleMixedGridSelector();
	};
	
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			if(childNodes[i].name) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
		}
		return childNodes;
	}
	
	function singleMixedGridSelectComplete() {
		var treeObj = $.fn.zTree.getZTreeObj("singleMixedGridSelectorZTree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes==null || nodes.length==0 || nodes[0].id<=0) return;
		singleMixedGridSelectCallback(nodes[0].id, nodes[0].name, nodes[0].orgId, nodes[0].orgCode, nodes[0].gridPhoto);
		
		closeSingleMixedGridSelector();
	}
</script>