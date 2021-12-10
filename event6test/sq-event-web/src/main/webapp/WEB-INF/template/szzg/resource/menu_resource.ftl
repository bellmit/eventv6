<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/normal.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<link rel="stylesheet" href="${rc.getContextPath()}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
<script src="${rc.getContextPath()}/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script> 
<title>网格首页</title>
</head> 
<body   class="easyui-layout">
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
    <input id="rootGdcPid" type="hidden" value="4" />
				<div id="dictTree" class="ztree" style="margin:10px;width:200px;height:280px"></div>	
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList" style="text-align:center;">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn" style="float:inherit;">关闭</a>
            </div>
        </div>	
</body>
<body>
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
			}
		});
	}
	
	var selectedNode = "";
	
	function _onClick(event, treeId, node) {//点击节点触发事件
		window.parent.document.getElementById("menuId").value=node.id;
		window.parent.document.getElementById("menuName").value=node.name;
		window.parent.closeMaxJqueryWindow();
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
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</body>
</html>