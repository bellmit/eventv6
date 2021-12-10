<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
</head>
<body style="width:100%;height:100%;" >
	<ul id="gridTree" class="ztree" ></ul>
</body>
<script type="text/javascript">
renderTree();
function renderTree(withCode, startGridId, isShowCheck) {
	var enable = true;
	if (isShowCheck != null) enable = isShowCheck;
	if(withCode==null) withCode=0;
	else if(withCode!=0) withCode=1;
	var isStandard=1;
	var setting = {
		async: {
			enable: true,
			url:"${rc.getContextPath()}/zhsq/grid/mixedGrid/gridZTree.json?t="+Math.random(),
			autoParam:["id=gridId"],
			dataFilter: filter,
			otherParam: {"withCode":withCode, "startGridId":(startGridId?startGridId:-1),"isStandard":isStandard}
		},
		check:{
			enable: enable,
			chkStyle: "checkbox",
			chkboxType: { "Y": "s", "N": "s" }
		},
		callback:{
			onClick:treeNodeClick
		}
	};
	$.fn.zTree.init($("#gridTree"), setting);
}

function treeNodeClick(event, treeId, treeNode, clickFlag) {
	if(treeNode.id<=0) return;
	window.parent.gridTreeClickCallback(treeNode.id, treeNode.name, treeNode.orgId, treeNode.orgCode, treeNode.gridPhoto, parseInt(treeNode.gridLevel),treeNode.gridCode);
}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		if(childNodes[i].name) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
	}
	return childNodes;
}

function reloadSelectTreeNode() {
	var treeObj = $.fn.zTree.getZTreeObj("gridTree");
	var nodes = treeObj.getSelectedNodes();
	if (nodes.length>0) {
		if(nodes[0].isParent=="true")
			treeObj.reAsyncChildNodes(nodes[0], "refresh");
		else {
			var parentNode = treeObj.getNodeByParam("id", nodes[0].pid, null);
			if(parentNode) {
				treeObj.reAsyncChildNodes(parentNode, "refresh");
			}
		}
	}
}
/*
  add by 王伟敏
  date:2013/12/19
*/
function treeNodeIsBySelect(){
   var treeObj = $.fn.zTree.getZTreeObj("gridTree");
   var nodes = treeObj.getSelectedNodes();
   if (nodes.length>0) {
      return true;
   }else{
      return false;
   }
}
</script>
</html>