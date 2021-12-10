<!-- 网格树控件
引用页面需要有回调函数gridTreeClickCallback方法，参数为gridId,gridName,orgId,orgCode,gridPhoto
 -->

<ul id="gridTree" class="ztree" ></ul>
<script type="text/javascript">
function renderTree(withCode, startGridId, isShowCheck) {
	var enable = true;
	if (isShowCheck != null) enable = isShowCheck;
	if(withCode==null) withCode=0;
	else if(withCode!=0) withCode=1;
	var setting = {
		async: {
			enable: true,
			url:"${rc.getContextPath()}/zhsq/grid/mixedGrid/gridZTree.json?t="+Math.random(),
			autoParam:["id=gridId"],
			dataFilter: filter,
			otherParam: {"withCode":withCode, "startGridId":(startGridId?startGridId:-1)}
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
	gridTreeClickCallback(treeNode.id, treeNode.name, treeNode.orgId, treeNode.orgCode, treeNode.gridPhoto, parseInt(treeNode.gridLevel),treeNode.gridCode);
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