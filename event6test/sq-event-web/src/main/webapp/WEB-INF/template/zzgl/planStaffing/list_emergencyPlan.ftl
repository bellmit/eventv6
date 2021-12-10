<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
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
				    <li>所属区域：</li><li>
				        <input type="hidden" id="regionCode" name="regionCode" class="queryParam"/>
	                	<input type="hidden" id="gridId" name="gridId" class="queryParam"/>
	                	<input type="text" id="gridName" name="gridName" class="inp1 InpDisable" style="width: 150px;" />
				    </li>
					<!-- <li>预案名称：</li><li><input class="inp1" type="text" id="planName" name="planName" /></li> -->
					<li>预案类型：</li><li>
							<input type="hidden" id="planType" name="planType" value="${bo.planType!''}" />
							<input type="text" class="inp1 easyui-validatebox singleCell"  id="planTypeName" value="${bo.planTypeName!''}" />
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
			<@actionCheck></@actionCheck>     
			 <!--    <a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="config();">预案配置</a>
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a> -->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#regionCode").val(grid.gridCode);   
			} 
		}, {
			OnCleared: function() {
				$("#regionCode").val('');
			},
			ShowOptions: {
				EnableToolbar : true
			}
		});
		AnoleApi.initTreeComboBox("planTypeName", "planType", "A001139", null, ['${bo.planType!}'],{
			ShowOptions: {
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
			url: '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'regionName', title:'所属区域', align:'center', width:'34%'},  
				{field:'planTypeName', title:'预案类型', align:'center', width:'30%',
					formatter:function(value,rec,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						var f = '<a href="###" title='+ rec.planTypeName +' onclick="detail(\''+ rec.planId +'\')">'+value+'</a>&nbsp;';
						return f;
					}
				},
				/* {field:'planContent', title:'预案内容', align:'center', width:100}, */
				/* {field:'planName', title:'预案名称', align:'center', width:100}, */
				/* {field:'remark', title:'备注', align:'center', width:100}, */
				{field:'createTime', title:'创建时间', align:'center', width:'33%',formatter:function(value){
                    return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                }}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				//listSuccess(data); //暂无数据提示
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/add.jhtml';
		showMaxJqueryWindow('新增预案', url, 700, 300);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/add.jhtml?id=' + rows[0].planId;
			showMaxJqueryWindow('编辑预案', url, 700, 300);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/detail.jhtml?id=" + id;
		showMaxJqueryWindow('预案详情', url,700, 300);
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			$.messager.confirm('提示', '请确认是否删除预案?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/zzgl/emergencyPlan/del.json',
						data: {
							planId: rows[0].planId
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info');
								searchData(true);
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
	function searchData(isCurrent) {
		if(isCurrent && isCurrent == true) {
    		$("#list").datagrid('reload', $('#searchForm').serializeJson());
    	} else {
    		$("#list").datagrid('load', $('#searchForm').serializeJson());
    	}
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
	
	
	function config(){
		
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/zzgl/planConfig/index.jhtml?system_privilege_code=planConfig&planId=' + rows[0].planId;
			showMaxJqueryWindow('预案配置', url);
		}
	}
</script>
</html>
