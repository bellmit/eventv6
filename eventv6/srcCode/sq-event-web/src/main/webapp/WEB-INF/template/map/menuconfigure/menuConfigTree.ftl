<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title> 
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/apusic/om-apusic.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/css/easyuiExtend.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.js"></script>

</head>
<style type="text/css">
*{margin:0; padding:0; list-style:none;}
.AlphaBack{background-color:rgba(0, 0, 0, 0.4); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#1c000000',endColorstr='#1c000000');}
.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-right:10px; display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px; transition:all 0.2s; border-radius:3px; font-size:12px; text-decoration:none;}
.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00; box-shadow:none; border-top:1px solid #c87d00;}
</style>
<body style="overflow-x:hidden;overflow-y:auto" class="easyui-layout">
	
	<input type="hidden" id="orgCode" name="orgCode" value="${orgCode}"/>
	<input type="hidden" id="gdcIdStr" name="gdcIdStr" value="${gdcIdStr}"/>
	<div class="AlphaBack" style="height:28px; padding:4px 0 0 10px;"><a href="#" onclick="save();" class="NorToolBtn SmallSaveBtn">保存</a></div>
	<ul id="gdcTree" class="ztree" ></ul>
	
</body>
<script type="text/javascript">
renderTree();
function renderTree() {
	var setting = {
		view: {
			selectedMulti: true
		},
		check:{
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		}
	};
	var zNodes;
    $.ajax({
        url: "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/gisDataCfgTreeForConfig.json?t="+Math.random(),
        type: 'POST',
        data: { orgCode:'${orgCode}'},
        dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
        ContentType: "application/json; charset=utf-8",
        success: function(data) {
        	var zNodes = JSON.stringify(data);
            $.fn.zTree.init($("#gdcTree"), setting, eval('(' + zNodes + ')'));
        },
        error: function(msg) {
            alert("失败");
        }
    });
}
function save(){
	var treeObj = $.fn.zTree.getZTreeObj("gdcTree");
	var nodes = treeObj.getCheckedNodes(true);
	var gdcIdsStr ="";
	if(nodes.length>0){
		for(var i=0;i<nodes.length;i++){
			gdcIdsStr = (gdcIdsStr == "")? nodes[i].id : gdcIdsStr+","+nodes[i].id;
		}
	}
	if(window.confirm('确定保存？')){
      }else{
         return false;
     }
	$.ajax({
        url: "${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/saveGisDataCfgTreeForConfig.json?t="+Math.random(),
        type: 'POST',
        data: { orgCode:'${orgCode}',gdcIdsStr:gdcIdsStr},
        dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
        ContentType: "application/json; charset=utf-8",
        success: function(data) {
        	if(data.result == true) {
        		alert("保存成功！");
        	}else{
        		alert("保存失败！");
        	}
        },
        error: function(msg) {
            alert("保存失败！");
        }
    });
}
</script>
</html>
