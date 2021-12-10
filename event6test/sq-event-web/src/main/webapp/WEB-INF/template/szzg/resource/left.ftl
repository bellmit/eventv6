<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script> 
<title>资源树首页</title>
 <style type="text/css">
    .move{ 
   	  height:248px;
	  overflow-y:auto;
    }
    </style>
</head> 
<body>
    <div id="dictTree" class="ztree" style="margin:10px;"></div>
	<div style="display:none;text-align:center;" id="cancel" >
		<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn" style="float:none;">关闭</a>
	</div>
    <input id="rootGdcPid" type="hidden" value="4" />
<script type="text/javascript">
if (typeof String.prototype.startsWith != 'function') {
 String.prototype.startsWith = function (prefix){
  return this.slice(0, prefix.length) === prefix;
 };
}
	var id_ = "${id!'-1'}",move=false,info=false,ztree={};
	$(document).ready(function(){
		if(id_.startsWith('move')){
			move=true;
			id_ = id_.split("_")[1];
			document.getElementById("cancel").style.display = "block";
			$("#dictTree").addClass("move");
		}else if(id_=='info'){
			info=true;
		}
		initMenuTree();
	});
	
	function initMenuTree() {
		var setting = {
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
		if(move){
			setting.callback.onClick = _move;
		}else if(info){
			setting.callback.onClick = _info;
		}
		$.ajax({
			type : 'POST',
			dataType : "json",
			url : "${rc.getContextPath()}/zhsq/szzg/zgResourceController/findTypeTree.json",
			error : function() {
				layer.alert("获取资源树异常");
			},
			success : function(data) {
				if( !data || data.length == 0){
					layer.alert("请先添加根节点！");
					_onClick('', '', {id:'-1'});
					return;
				}
				var sNodes = [],hasNodeOpen=false;
				for(var i=0,l=data.length;i<l;i++){
					var d = {id:data[i].resTypeId,pid:data[i].parentTypeId,name:data[i].resTypeName,typeCode:data[i].typeCode};
					if( d.id == id_ && !move){
						d['open']=true;
						hasNodeOpen=true;
					}
					sNodes.push(d);
				}
				sNodes = arry2TreeFormat(sNodes);
				ztree = $.fn.zTree.init($("#dictTree"), setting, sNodes);
				var node = '';
				node = ztree.getNodes()[0];
				if(hasNodeOpen && !move ){
					node = ztree.getNodeByParam("id", id_, null);
				}
				ztree.expandNode(node, true, false, true);
				ztree.selectNode(node);
				info?_info('','',node):_onClick('', '', node);
			}
		});
	}
	
	var selectedNode = "";
	
	function _info(event, treeId, node) {//资源信息
		if(node.isParent){
			return;
		}
		var url = "${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=list_info&id="+node.typeCode;
		$("#main",window.parent.document).attr("src",url);
	}
	function _onClick(event, treeId, node) {//点击节点触发事件
		var url = "${rc.getContextPath()}/zhsq/szzg/zgResourceController/page.jhtml?page=list&id="+node.id;
		$("#main",window.parent.document).attr("src",url);
	}
	function getPath(id){
		var node = ztree.getNodeByParam("id", id, null);
		var nodePath = node.getPath(),path="";
		for(var i=0,l=nodePath.length;i<l;i++){
			path += nodePath[i].name +"-";
		}
		return path.substring(0,path.length-1);
	}
	function _move(event, treeId, node){//父节点 不能移动到 子节点上
		var nodePath = node.getPath();
		for(var i=0,l=nodePath.length;i<l;i++){
			if(nodePath[i].id == id_){
				window.parent.showMoveMsg();
				return ;
			}
		}
		parent.moveNode(node.id,node.name);
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
	function arry2TreeFormat(sNodes){
		var r = [];
		var tmpMap = [];
		var id="id",pid="pid",children="children";
		for (i=0, l=sNodes.length; i<l; i++) {
			tmpMap[sNodes[i][id]] = sNodes[i];
		}
		for (i=0, l=sNodes.length; i<l; i++) {
			if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
				if (!tmpMap[sNodes[i][pid]][children])
					tmpMap[sNodes[i][pid]][children] = [];
				tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
			} else {
				r.push(sNodes[i]);
			}
		}
		return r;
	}
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</body>
</html>