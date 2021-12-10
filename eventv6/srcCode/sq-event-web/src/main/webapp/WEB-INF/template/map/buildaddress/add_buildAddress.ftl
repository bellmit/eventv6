<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
</head>
<style type="text/css">
.BigNorToolBtn {
  display: inline-block;
  float: left;
  height: 29px;
  margin: 0 10px;
  padding: 0 13px 0 35px;
  font-family: Microsoft YaHei;
  font-size: 16px;
  line-height: 30px;
  color: #fff;
  text-align: center;
  border-radius: 3px;
  background-repeat: no-repeat;
  background-color: #2980B9;
  background-position: 12px 8px;
  transition: all 0.5s;
}
.ConSearch .btns {
	float:left;
	width: 200px;
	height:32px;
	font-size:14px;
	line-height: 37px;
}
input[type="checkbox"] {
  margin: 3px 3px 7px 4px;
}
</style>
<body class="easyui-layout">
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/save.jhtml" method="post" enctype="multipart/form-data">
		<input type="hidden" name="relateType" id="relateType" value="<#if relateType??>${relateType}</#if>"/>
		<input type="hidden" name="addressId" id="addressId" value="<#if addressId??>${addressId}</#if>"/>
		<input type="hidden" name="mapt" id="mapt" value="<#if mapt??>${mapt}</#if>"/>
		<input type="hidden" name="x" id="x" value="<#if x??>${x}</#if>"/>
		<input type="hidden" name="y" id="y" value="<#if y??>${y}</#if>"/>
		<input type="hidden" name="hs" id="hs" value="<#if hs??>${hs}</#if>"/>
		<input type="hidden" name="buildingId" id="buildingId" value=""/>
		<input type="hidden" name="isRelationValue" id="isRelationValue" value="0"/>
		<div class="fl">
	    	<ul>
	    		<li>楼宇名称：</li>
	    		<li><input id="buildingNameForSearch" type="text" class="inp1 InpDisable" value="" style="width:150px;"/></li>
	    		<li><input name="isRelation" type="checkbox" value="2" onclick="changeIsRelation();" id="isRelation" />是否关联</li>
	        </ul>
	    </div>
	    <div class="btns">
	    	<ul>
	        	<li><a href="#" class="chaxun" title="点击搜索" onclick="searchData()">搜索</a></li>
	        	<li><a href="#" class="BigNorToolBtn SaveBtn" title="保存选择的关联关系" onclick="javascript:tableSubmit();">保存</a></li>
	        </ul>
	    </div>
	    </form>
	</div>
</div>
<div region="center" border="false" style="overflow:hidden;">
	<table id="list"></table>
</div>



<script type="text/javascript">
$(function() {
	loadDataList();
});

function changeIsRelation() {
	if(document.getElementById("isRelation").checked == true){
		$("#isRelationValue").val("1");
	}else {
		$("#isRelationValue").val("0");
	}
}
function searchData() {
	$('#list').datagrid('clearSelections');
	$("#list").datagrid('options').queryParams = getQueryData();
	$("#list").datagrid('load');
}
function getQueryData() {
	var a = {};
	var buildingName = $("#buildingNameForSearch").val();
	var isRelationValue = $("#isRelationValue").val();
	a["buildingName"] = buildingName;
	a["isRelationValue"] = isRelationValue;
	return a;
}


function loadDataList() {
	$('#list').datagrid({
		url:"${rc.getContextPath()}/zhsq/map/buildaddress/buildAddress/buildingListData.json?t="+Math.random(),
		width:600,
		height:600,
		nowrap: false,
		striped: true,
		fit: true,
		singleSelect: true,
		rownumbers: true,
		columns:[[
		    {field:'ck',checkbox:true},
		    {field:'buildingId',title:'ID', align:'center',hidden:true},
            {field:'buildingName',title:'楼宇名称', align:'left',width:150},
            {field:'buildingAddress',title:'楼宇地址', align:'left',width:250},
            {field:'addressId',title:'关联状态', align:'left',width:100,
            	formatter : function(value, rec, index) {
            		var res=""
            		if(value != null && value != "") {
            			res="已关联";
            		}else {
            			res="未关联";
            		}
					return res;
				}
			}
		]],
		 
		toolbar:'#jqueryToolbar',
		pagination:true,
		pageSize: 20,
		queryParams: getQueryData(),
		onLoadSuccess : function(data) {
		    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
			if(data.total == 0) {
				var body = $(this).data().datagrid.dc.body2;
				body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.png" title="暂无数据"/></div>');
			}
		},
		onClickRow : function(rowIndex, rowData) {
			buttonAccessByStatus(rowData.status_);
		}
	});
	
	//设置分页控件
    var p = $('#list').datagrid('getPager');
	$(p).pagination({
		pageSize: 20,//每页显示的记录条数，默认为
		pageList: [20,30,40,50],//可以设置每页记录条数的列表
		beforePageText: '第',//页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
	});
}
	function tableSubmit(){
		var rows = $('#list').datagrid('getSelections');
		if(rows.length!=1){
			$.messager.alert('提示','请选中一条关联的数据!','warning');
			return;
		}
		$("#buildingId").val(rows[0].buildingId);
	    var msg = "";
		modleopen();
		$("#tableForm").ajaxSubmit(function(data) {
	  		if(data.flag == true){
	  			msg = "保存成功！"
	  			modleclose();
	  			parent.reloadDataForSubPage(msg);
			}else{
				modleclose();
				$.messager.alert('错误','保存失败，请重试！', 'error');
			}
		});
	}
	</script>
</body>
</html>

