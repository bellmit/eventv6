<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/customEasyWin.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
				<li>所属年份</li>
                <li><input type="text" class="inp1 Wdate timeClass" id="syear" name="syear"  style="width:100px;"
                           onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy'})" value="${syear!}" readonly="true"/>
                </li>

				<li>所属区域：</li>
				<li>
					<input type="hidden" id="orgCode" name="orgCode"  value="${orgCode!}" >
                     <input class="inp1 inp2 InpDisable easyui-validatebox" style="width:100px" type="text" id="orgName"
                              name="orgName"  value="${orgName!}" />
           		 </li>
				<li>类型：</li>
				<li>
					<input type="hidden" id="type" name="type"  value="" >
                     <input class="inp1 inp2 InpDisable easyui-validatebox" style="width:100px" type="text" 
                               id="typeName" name="typeName"  value="" />
				</li>
				</ul>
			</div>
		        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            		<li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
		        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
			<div class="tool fr">
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
		        </div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
	
		AnoleApi.initGridZtreeComboBox("orgName", null, function(gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value=items[0].orgCode;
            }
        }, {
            rootName: "所属区域",
            ChooseType : '1',
            isShowPoorIcon: "1",
            ShowOptions : { GridShowToLevel : 4,EnableToolbar : true},
            OnCleared:function(){
                document.getElementById('orgCode').value='';
            }
        });
        AnoleApi.initListComboBox("typeName", "type", null, null, ["${(type)!}"], {
		DataSrc : [{"name":"水", "value":"1"},{"name":"电", "value":"2"},{"name":"煤", "value":"3"}]
		});
		//加载列表
		loadList(); 
	});
	//加载列表
	function loadList() {
	   var type=$("#type").val();
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/listData.jhtml',
			queryParams: { //查询参数
				'type':type,
				'orgCode':'${(orgCode)!}',
				'syear':'${(syear)!}'
			},
			columns: [[
				{field: 'hydropowerCoalId', checkbox: true, width: 40, hidden: 'true'},
				{field:'orgName', title:'所属网格', align:'center', width:100},
				{field:'syear', title:'所属年月', align:'center', width:100},
					{field:'type', title:'类型', align:'center', width:100,
					formatter: function (val, rec) {
					    if(val=='1')
                        	return '水';
                        else if(val=='2')
                        	return '电';
                        else if(val=='3')
                        	return '煤';
                        else
                        return val;
                    }
				},
				{field:'usage', title:'使用量(<span id="unit"><#if type=='2'>度<#else>吨</#if></span>)', align:'center', width:100,
					formatter: function (val, rec) {
                        var content = '<a href="javascript:detail('+ rec.hydropowerCoalId + ')"><li title="' + val + '" class="tip">' + val + '</li></a>';
                        return content;
                    }},
				{field:'yearBasis', title:'同比(%)', align:'center', width:100},
				{field:'linkRelRatio', title:'环比(%)', align:'center', width:100},
				{field:'yearBasisInc', title:'同比增长(%)', align:'center', width:100},
				{field:'linkRelRatioInc', title:'环比增长(%)', align:'center', width:100}
			
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
			   if (data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
			},
			onLoadError: function() {
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
                $('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			}
		});
		 //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 20,//每页显示的记录条数，默认为
            pageList: [20, 30, 40, 50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
        });
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/add.jhtml';
		showMaxJqueryWindow('新增', url, 450, 280);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/add.jhtml?id=' + rows[0].hydropowerCoalId;
			showMaxJqueryWindow('编辑', url, 450, 280);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 450, 280);
	}
	
	//删除
	function del(){
			var ids = [];
			var rows = $('#list').datagrid('getSelections');
			for(var i=0;i<rows.length;i++){
				ids.push(rows[i].hydropowerCoalId); 
			}
			var idStr = ids.join(',');
			if(idStr==null || idStr=="") {
				$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
				return;
			}
			if (ids.length != 1) {
				$.messager.alert('提示','只能选择一条数据执行此操作！','info');
				return;
			}
			
			$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r) {
				if (r){
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/szzg/hydropowerCoal/batchDelete.jhtml',
						data: 'idStr='+idStr,
						dataType:"json",
						success: function(data) {
							reloadDataForSubPage('成功删除 ' + data.result + ' 条数据！');
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
	}
	
	//查询
	  function searchData() {
	    var type=$("#type").val();
        if (type != null & type != "") {
            if(type == '2'){
            	$("#unit").html("度");
            }else $("#unit").html("吨");
        }
        $('#list').datagrid('reload', {
			type: type,
			syear: $('#syear').val(),
			orgCode:$('#orgCode').val()
		});
    }
	
	//重置
	function resetCondition() {
		
		$('#syear').val('${syear!}');
		$('#orgCode').val('${orgCode!}');
		$('#orgName').val('${orgName!}');
		AnoleApi.initListComboBox("typeName", "type", null, null, ["${(type)!}"], {
			DataSrc : [{"name":"水", "value":"1"},{"name":"电", "value":"2"},{"name":"煤", "value":"3"}]
		});
		searchData();
	}
	    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
       // $("#list").datagrid('load');
       searchData();
    }
    
    function setType(type){
    	AnoleApi.initListComboBox("typeName", "type", null, null, [type], {
			DataSrc : [{"name":"水", "value":"1"},{"name":"电", "value":"2"},{"name":"煤", "value":"3"}]
		});
		
    }
    
</script>
</html>
