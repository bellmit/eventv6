<!-- 
#组织选择
说明：
#1、入口函数showSingleOrgSelector，参数：无
#2、引用页面需要有回调函数singleOrgSelectCallback，参数：选择的组织ID，选择的组织名称
 -->
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/apusic/om-apusic.css">

<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/operamasks-ui.min.js"></script>
<div id="SingleOrgSelector" class="easyui-window" title="组织选择" minimizable="false" maximizable="false" collapsible="false" closed="true" modal="false" style="width:600px;height:300px;padding:1px;overflow:hidden;">
	<table width="100%" border="0" cellspacing="1" cellpadding="0">
		<tr>
			<td style="background-color: #F3F8FE;">
            	<div id="singleOrgSelectorTree" style="height:240px; width:200px; overflow:auto;"></div>
            </td>
		</tr>
		<tr>
			<td align="center">
				<input type="button" value="确定" onclick="singleOrgSelectComplete()" />
				<input type="button" value="关闭" onclick="closeSingleOrgSelector()" />
			</td>
		</tr>
	</table>
</div>
<script>
	var $singleOrgSelectorWin;
	var treeInitFlag = false;

	function showSingleOrgSelector() {
		if(!treeInitFlag) {
			loadOrgTree();
			treeInitFlag = true;
		}
	    $singleOrgSelectorWin = $('#SingleOrgSelector').window({
	    	title:"组织选择（双击或者选中节点）",
	    	width: 220,
	    	height: 306,
	    	top:($(window).height()-306)*0.5+$(document).scrollTop(),
	    	left:($(window).width()-220)*0.5+$(document).scrollLeft(),
	    	shadow: true,
	    	modal:true,
	    	closed:true,
	    	minimizable:false,
	    	maximizable:false,
	    	collapsible:false
	    });
		$singleOrgSelectorWin.window('open');
	}
	
	function closeSingleOrgSelector() {
		$singleOrgSelectorWin.window('close');
	}
	
	function loadOrgTree() {
		var rootUrl = "${rc.getContextPath()}/zhsq/grid/mixedGrid/orgTree.json?t="+Math.random();
		$("#singleOrgSelectorTree").omTree({
	        dataSource :rootUrl,
	        simpleDataModel: true,
	        onBeforeExpand : function(node){
	        	var nodeDom = $("#"+node.nid);
	        	if(nodeDom.hasClass("hasChildren")) {
	        		nodeDom.removeClass("hasChildren");
	        		$.ajax({
	                	url: '${rc.getContextPath()}/zhsq/grid/mixedGrid/orgTree.json?orgId='+node.id+'&t='+Math.random(),
	          			method: 'POST',
	    				dataType: 'json',
        				success: function(data) {
	          				$("#singleOrgSelectorTree").omTree("insert", data, node);
	    				}
	      			});
	        	}
	        	return true;
	        }, onDblClick: function(node, event) {
	        	singleOrgSelectCallback(node.id, node.text);
	        	closeSingleOrgSelector();
            },
			onSuccess: function(data){
				$('#singleOrgSelectorTree').omTree('expandAll');
			}
	    });
	}
	
	function singleOrgSelectComplete() {
		var node = $('#singleOrgSelectorTree').omTree('getSelected');
		singleOrgSelectCallback(node.id, node.text);
		closeSingleOrgSelector();
	}
</script>