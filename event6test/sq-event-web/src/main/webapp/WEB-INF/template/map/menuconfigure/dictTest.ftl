<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据字典新增</title> 
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
</head>

<body>

<input id="dictTree" name="dictTree">

<select id="dictSelect" name="dictSelect" style="width:150px;">
</select>
	<div>
		<tr class="item" colspan="6">
		    <td class="itemtit">&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="javascript:tableSubmit();">保存</a></td>
		    <td class="itemtit">&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="cancel()">关闭</a></td>
		</tr>
	</div>
</body>
<script type="text/javascript">
$('#dictTree').combotree({
	url: '${rc.getContextPath()}/dictionary/dictSelectTree.json?dictPcode=A',
	onLoadSuccess:function(node,data){
		
	}
});
$('#dictSelect').combobox({
	url:'${rc.getContextPath()}/dictionary/dictSelect.json?dictPcode=A',
	valueField:'dictCode',
	textField:'dictName'
});

function tableSubmit(){

}
</script>
</html>