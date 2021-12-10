<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script> 
<title>网格首页</title>
</head> 
<body>
    <div id="dictTree" class="ztree" style="margin:10px;"></div>
    <input id="rootGdcPid" type="hidden" value="4" />
<script type="text/javascript">
	$(document).ready(function(){
		initMenuTree();
	});
	
	function initMenuTree() {
		var setting = {
			async : {
				enable : true,
				url:"${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/menuZTreeForJsonp.json?jsoncallback=?",
				autoParam:["id=gdcPid"],
				dataFilter: _filter,
				dataType : "jsonp",
				otherParam : {
					"rootGdcPid" : $("#rootGdcPid").val()
				}
			},
			edit: {
				drag: {isCopy: true, isMove: false},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: _onClick
			}
		};
	
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/gisDataCfgTreeLeft.json?gdcPid=1",
			error : function() {
				
			},
			success : function(data) {
				$.fn.zTree.init($("#dictTree"), setting, data);
				var treeObj = $.fn.zTree.getZTreeObj("dictTree");
				var node = treeObj.getNodeByParam("id", "4", null);
				treeObj.selectNode(node);
			}
		});
	}
	
	var selectedNode = "";
	
	function _onClick(event, treeId, node) {//点击节点触发事件
		var url = "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/toList.jhtml?gdcPid="+node.id;
		$("#main",window.parent.document).attr("src",url);
		var treeObj = $.fn.zTree.getZTreeObj("dictTree");
		var nodes = treeObj.getSelectedNodes();
		$("#rootGdcPid").val(nodes[0].id);
		selectedNode = nodes[0];
	}
	
	function reloadSelectTreeNode() {
		var treeObj = $.fn.zTree.getZTreeObj("dictTree");
		var nodes = treeObj.getSelectedNodes();
		
		if (nodes.length>0) {
			if(nodes[0].isParent == true){
				var parentNode = treeObj.getNodeByParam("id", nodes[0].id, null);
				treeObj.reAsyncChildNodes(parentNode, "refresh");
			} else { // 如果不是父节点，刷新上级
				var parentNode = treeObj.getNodeByParam("id", selectedNode.pId, null);
				treeObj.reAsyncChildNodes(parentNode, "refresh");
			}
		} else {
			var node = treeObj.getNodeByParam("id", $("#rootGdcPid").val(), null);
			treeObj.selectNode(node);
			reloadSelectTreeNode();
			//var parentNode = treeObj.getNodeByParam("id", $("#rootGdcPid").val(), null);
			//treeObj.reAsyncChildNodes(parentNode, "refresh");
		}
	}
	
	function _filter(treeId, parentNode, childNodes) {
		if (!childNodes)
			return null;
		for ( var i = 0, l = childNodes.length; i < l; i++) {
			if (childNodes[i].name) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
		}
		return childNodes;
	}
</script>
</body>
</html>