<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<style type="text/css">
		.inp1 {width:150px;}
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
					
					 <li>所属区域：</li>
	                <li>
		                <input type="text" id="infoOrgCode" name="regionCode" value="" class="queryParam hide" />
		                <input type="text" id="gridName" name="gridName" value="" class="inp1 InpDisable w150" />
		            </li>
					<li>团伙名称：</li><li><input class="inp1" type="text" id="gangName" name="gangName" /></li>
					<li>打击状态：</li>
					<li>
						<input id="hitStatus" name="hitStatus" type="text" value="" class="queryParam hide"/>
		                <input id="hitStatusName" type="text" class="inp1 InpDisable w150" />
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
				<!--<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
				<@actionCheck></@actionCheck>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		loadList(); //加载列表
		AnoleApi.initGridZtreeComboBox("gridName", "infoOrgCode", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
         AnoleApi.initListComboBox("hitStatusName", "hitStatus", null, null, null, {
        	DataSrc : [{"name":"已扫除", "value":"1"},{"name":"扫除中", "value":"2"}],
        	IsTriggerDocument: false,
        	ShowOptions:{
        		EnableToolbar : true
        	}
        });
	});
	
	//加载列表
	function loadList() {
	    var queryParams = {infoOrgCode:'${infoOrgCode!}'};
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/eventSBREvilGang/listData.jhtml',
			queryParams: queryParams,
			idField:'gangId',
			columns: [[
				{field:'gangName', title:'团伙名称', align:'center', width:100,formatter: clickFormatter},
				{field:'gridPath', title:'所属区域', align:'center', width:100,formatter: titleFormatter},
				{field:'situation', title:'团伙涉黑涉恶情况', align:'center', width:200,formatter: titleFormatter},
				{field:'hitStatus', title:'打击状态', align:'center', width:50,
					formatter:function(value,rec,rowIndex){
                        var f;
                        if(value == '1'){
                            f = '已扫除';
                        }else if(value == '2'){
                            f = '扫除中';
                        }
                       
						f = '<span title="'+ f +'" >'+ f +'</span>';
						
                        return f;
                    }
				},
				{field:'gangRemark', title:'备注', align:'center', width:100,formatter: titleFormatter},
				{field:'activityZone', title:'主要活动地带', align:'center', width:100,formatter: titleFormatter}
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
		var url = '${rc.getContextPath()}/zhsq/eventSBREvilGang/add.jhtml';
		openJqueryWindowByParams({
                title			: "新增团伙信息",
                targetUrl		: url
            });
		//showMaxJqueryWindow('新增', url, 700, 600);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/eventSBREvilGang/add.jhtml?gangId=' + rows[0].gangId;
			openJqueryWindowByParams({
                title			: "编辑团伙信息",
                targetUrl		: url
            });
			//showMaxJqueryWindow('编辑', url, 700, 600);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/eventSBREvilGang/detail.jhtml?gangId=" + id;
		openJqueryWindowByParams({
                title			: "查看团伙信息",
                targetUrl		: url
        });
		//showMaxJqueryWindow('详情', url, 700, 600);
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
						url: '${rc.getContextPath()}/zhsq/eventSBREvilGang/del.json',
						data: {
							gangId: rows[0].gangId
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
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	function clickFormatter(value, rec, rowIndex) {
        var title = '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.gangId + '\')">'+ value +'</a>';
        return title;
    }
	
</script>
</html>
