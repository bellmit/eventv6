<!DOCTYPE html>
<html>
<head>
	<title>布控库管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.inp1 {width:100px;}
        .sub{
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;
        }
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm" method="post">
			<div class="fl">
				<ul>
                    <li>布控库名称：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" name="name" id="name" style="width: 156px" />
                    </li>
                    <li>布控库类型：</li>
                    <li>
                        <input type="hidden" id="libType" name="libType"/>
                        <input class="inp1 InpDisable" type="text" id="libTypeCN" name="libTypeCN" style="width: 156px" />
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
            	<a href="javascript:void(0)" class="NorToolBtn ExportBtn" onclick="detail();">布控人员管理</a>
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
        nationComboBox = AnoleApi.initListComboBox("libTypeCN", "libType", "A002004008001", null, ["${bo.libType!}"], {
            ShowOptions : {
                EnableToolbar : true
            }
        });
        loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/event/executeControl/listData.jhtml',
            queryParams: $('#searchForm').serializeJson(),
			columns: [[
                /*{field:'gridPath', title:'网格名称', align:'center', width:200,formatter : function(val, rec) {
                    return '<div class="sub"><a title="'+val+'" onclick="showVideo('+rec.id+')">'+val+'</a></div>';
                }},*/
				{field:'name', title:'布控库名称', align:'center', width:100},
				{field:'description', title:'描述', align:'center', width:150},
				{field:'libType', title:'布控库类型', align:'center', width:100,formatter:function(value,rec){
                        if(value == '1'){
                            value='黑名单库';
                        }else if (value == '2') {
                            value='白名单库';
                        }
                        return value;
                    }}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/executeControl/form.jhtml';
		showMaxJqueryWindow('新增', url, 500, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/event/executeControl/form.jhtml?id=' + rows[0].id;
			showMaxJqueryWindow('编辑', url, 500, 400);
		}
	}

	//详情
	function detail() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = "${rc.getContextPath()}/zhsq/event/executeControl/detail.jhtml?id=" + rows[0].id;
            showMaxJqueryWindow('布控人员管理', url);
        }
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/event/executeControl/del.jhtml',
						data: {
							id: rows[0].id,
                            controlLibraryId: rows[0].controlLibraryId
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info');
								searchData();
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				}
			});
		}
	}
	
	//查询
	function searchData() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
    function exportData() {
		$("#searchForm").submit();
    }
    
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
</script>
</html>
