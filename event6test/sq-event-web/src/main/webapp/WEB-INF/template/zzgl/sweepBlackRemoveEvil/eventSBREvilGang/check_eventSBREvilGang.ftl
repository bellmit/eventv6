<!DOCTYPE html>
<html>
<head>
	<title>选择团伙</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl">
<style>

.inp1 {width:150px;}
</style>
</head>
<body class="easyui-layout">
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden; position:relative;">
		<div class="NorForm">
			<div class="ConSearch">
		        <div class="fl">
		        	<ul>
		        	<li>所属区域：</li>
	                <li>
		                <input type="text" id="infoOrgCode" name="regionCode" value="" class="queryParam hide" />
		                <input type="text" id="gridName" name="gridName" value="" class="inp1 InpDisable w150" />
		            </li>
		           	 <li>团伙名称：</li><li><input class="inp1" type="text" id="gangName" name="gangName" /></li>
		            </ul>
		        </div>
		        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="search_()">查询</a></li>
				</ul>
	       	 </div>
		‍
			</div>
			<div class="clear"></div>  
			<div   border="false" style="margin-left:5px;margin-top:5px;float:left; width:95%; height:303px; overflow:hidden; position:relative;">
				<table id="list" style="width:100%"></table>
			</div>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="#" class="BigNorToolBtn SaveBtn" onclick="javascript:save();">确定</a>
    		<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancel();">取消</a>
        </div>
    </div>
</body>

<script type="text/javascript">
var idsStr='${ids!}';
var ids =idsStr.split(",");
$(function(){
	//$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
	}, {
        	OnCleared: function() {
        		$("#infoOrgCode").val('');
        	},
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
	$('#list').datagrid({
		nowrap : true,
		striped : true,
		singleSelect: false,
		fit : true,
		fitColumns: true,//让列宽自适应窗口宽度
		pageSize: 20,
		idField : 'gangId',
		rownumbers:true,
		checkOnSelect : true,
		selectOnCheck : true,
		scrollbarSize :0,
		url: '${rc.getContextPath()}/zhsq/eventSBREvilGang/listData.jhtml',
		columns: [[
			{field:'ck',checkbox:true},
			{field:'gridPath', title:'所属区域', align:'center', width:100,formatter: titleFormatter},
			{field:'gangName',title:'团伙名称',align:'center', width:'100',formatter: titleFormatter}
		]],
		//toolbar: '#jqueryToolbar',
		pagination: true,
		onLoadSuccess:function(data){
			if(data.total == 0){
				$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
			}
			selectRows();
		},
		onLoadError:function(){
			$('.datagrid-body-inner').eq(0).addClass("l_elist");
			$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
		} ,
		onUncheck:function(index, row){
                   removeCheck(row.gangId);
        },
        onCheck:function(index, row){
                   addCheck(row.gangId);
        }
	}).pagination({
		pageSize: 20,//每页显示的记录条数，默认为
		pageList: [20,30,40,50],//可以设置每页记录条数的列表
		beforePageText: '第',//页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: ''
	});
		
});



//取消	
function cancel() {
	parent.closeMaxJqueryWindow();
}	

// 保存
function save() {
    var rows = $('#list').datagrid('getSelections'); 
	parent.checkEvilGangs(rows);
}

function search_(){
	var gangName = $('#gangName').val();
	var infoOrgCode = $('#infoOrgCode').val();
	$('#list').datagrid('reload', {
		gangName : gangName,
		infoOrgCode:infoOrgCode
	});
}

  function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function existCheck(id){//是否选择
		for(var j=0;j<ids.length;j++){
			if(id == ids[j]){
				return false;
			}
		}
		return true;
	}
	
	function addCheck(id){//添加选择
			if(existCheck(id)){
				ids.push(id);
			}
    }
	function removeCheck(id){//移除选择
		for(var j=0;j<ids.length;j++){
			if(id == ids[j]){
				ids.splice(j,1);
			}
		}
	}
	function selectRows(){
	    //获取数据列表中的所有数据
	    var rows = $("#list").datagrid("getRows");  
	    //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
	    for(var i=0;i<rows.length;i++){
	      var rowId = rows[i].gangId;
	      for(var j=0;j<ids.length;j++){
	        if(rowId==ids[j]){
	          var index = $("#list").datagrid("getRowIndex",rows[i]);
	          $("#list").datagrid("checkRow",index);
	        }
	      }
	    }
  	}
	
</script>
</html>
