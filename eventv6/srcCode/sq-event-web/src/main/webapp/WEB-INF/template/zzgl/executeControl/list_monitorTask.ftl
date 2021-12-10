<!DOCTYPE html>
<html>
<head>
	<title>布控任务管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.inp1 {width:100px;}
        .ToolBar .fr {
            width: 580px;
        }
        .LabName{
            text-align: left;
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
                <input type="hidden" id="libIds" name="libIds" value="${bo.libIds}"/>
                <input type="hidden" id="controlTaskId" name="controlTaskId" value="${bo.controlTaskId}"/>
                <input type="hidden" id="libType" name="libType" value="${bo.libType}"/>
			<div class="fl">
				<ul>
                    <li>任务名称：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" name="name" id="name" style="width: 156px" />
                    </li>
                    <li>任务类型：</li>
                    <li>
                        <input type="hidden" id="taskType" name="taskType"/>
                        <input class="inp1 InpDisable" type="text" id="taskTypeCN" name="taskTypeCN" style="width: 156px" />
                    </li>
                    <li>任务状态：</li>
                    <li>
                        <input type="hidden" id="taskStatus" name="taskStatus"/>
                        <input class="inp1 InpDisable" type="text" id="taskStatusCN" name="taskStatusCN" style="width: 156px" />
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
            	<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="addPersonnel();">布控人员管理</a>
                <a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="editIgnoreStatus();">是否忽略报警消息</a>
                <a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="editTaskStatus();">设置任务状态</a>
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
				<@actionCheck></@actionCheck>
			</div>
		</div>
	</div>
<#include "/zzgl/executeControl/form_deviceDis.ftl" />
</body>
<script type="text/javascript">
	var id = "";
	var controlLibraryId = "";
	var libType = "";
	$(function() {

        nationComboBox = AnoleApi.initListComboBox("taskTypeCN", "taskType", "${taskType}", null, ["${bo.libType!}"],{
            ShowOptions : {
                EnableToolbar : true
            }
        });

        nationComboBox = AnoleApi.initListComboBox("taskStatusCN", "taskStatus", "${taskStatus}", null, ["${bo.taskStatus!}"], {
            ShowOptions:{
                EnableToolbar : true
            }
        });

        window.addEventListener ('message', function(event) {
            console.log(event.data);
            if (event.data != null){
                var split = event.data.split(",");
                id = split[0];
                controlLibraryId = split[1];
                libType = split[2];
			}
            loadList(controlLibraryId);
        });
	});
	
	//加载列表
	function loadList(controlLibraryId) {
        //获取父页面传来的数据
		$('#libIds').val(controlLibraryId);
		var data = $('#searchForm').serializeJson();
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/event/monitorTask/listData.jhtml',
            queryParams: data,
			columns: [[
				{field:'name', title:'任务名称', align:'center', width:100/*,formatter : function(val, rec) {
                        return '<div class="sub"><a title="'+val+'" onclick="detail(\''+rec.controlTaskId+'\')">'+val+'</a></div>';
                    }*/},
				{field:'taskStatusCN', title:'任务状态', align:'center', width:60},
                {field:'validTime', title:'任务有效期开始时间', align:'center', width:100},
                {field:'invalidTime', title:'任务有效期开始时间', align:'center', width:100},
                {field:'taskTypeCN', title:'任务类型', align:'center', width:60}
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

	//是否忽略报警消息
	function editIgnoreStatus() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = "";
            var ignoreStatus = rows[0].ignoreStatus;
            if (ignoreStatus != null){
                url = '${rc.getContextPath()}/zhsq/event/monitorTask/editIgnoreIndex.jhtml?controlTaskId='
                        + rows[0].controlTaskId + "&ignoreStatus=" + ignoreStatus;
            }else {
                url = '${rc.getContextPath()}/zhsq/event/monitorTask/editIgnoreIndex.jhtml?controlTaskId='
                        + rows[0].controlTaskId;
            }
            showMaxJqueryWindow('是否忽略报警消息', url, 400, 200);
        }
    }

    //设置任务状态
    function editTaskStatus() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = "";
            var taskStatus = rows[0].taskStatus;
            if (taskStatus != null){
                url = '${rc.getContextPath()}/zhsq/event/monitorTask/editTaskIndex.jhtml?controlTaskId='
                        + rows[0].controlTaskId + "&taskStatus=" + taskStatus;
            }else {
				url = '${rc.getContextPath()}/zhsq/event/monitorTask/editTaskIndex.jhtml?controlTaskId='
                        + rows[0].controlTaskId;
			}
            showMaxJqueryWindow('设置任务状态', url, 400, 200);
        }
    }

    //详情
    function detail(id) {
        var url = '${rc.getContextPath()}/zhsq/event/monitorTask/view.jhtml?id=' + id;
        showMaxJqueryWindow('查看详情', url, 1000, 500);
    }
	
	//新增
	function add(libIds) {
		var url = '${rc.getContextPath()}/zhsq/event/monitorTask/form.jhtml?libIds=' + controlLibraryId;
		showMaxJqueryWindow('新增', url, 1000, 500);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/event/monitorTask/form.jhtml?id=' + rows[0].id;
			showMaxJqueryWindow('编辑', url, 1000, 500);
		}
	}

	//布控人员管理
	function addPersonnel() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            console.log(rows)
            var url = "${rc.getContextPath()}/zhsq/event/controlPersonnel/index.jhtml?libIds=" + rows[0].libIds + "&libType="
					+ libType + "&controlTaskId=" + rows[0].controlTaskId + "&id=" + rows[0].id;
            showMaxJqueryWindow('布控人员管理', url);
        }
	}

	function showDetail(titile, url) {
        showMaxJqueryWindow(titile, url);
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
						url: '${rc.getContextPath()}/zhsq/event/monitorTask/del.jhtml',
						data: {
							id: rows[0].id,
                            controlTaskId: rows[0].controlTaskId
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
		//$('#searchForm').form('clear');
        $("#name").val('');
        $("#taskType").val('');
		searchData();
	}

    //取消
    function cancel() {
        $('#editTaskStatus').window('close');
    }

</script>
</html>
