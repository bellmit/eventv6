<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
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
			<form id="searchForm" action="${rc.getContextPath()}/zhsq/event/pointInfo/exportData.json" method="post" enctype ="multipart/form-data" target="hiddenFrame">
			<div class="fl">
				<ul>
                    <li>所属网格：</li>
                    <li>
                        <input type="hidden" id="regionCode" name="regionCode" value="${regionCode!}" />
                        <input type="hidden" id="gridId" value=""${gridId!}"/>
                        <input class="inp1 InpDisable" type="text" id="gridName" style="width: 156px" value="<#if gridName??>${gridName}</#if>"/>
                    </li>
                    <li>是否在线：</li>
                    <li>
                        <input type="hidden" id="deviceStatus" name="deviceStatus"/>
                        <input class="inp1 InpDisable" type="text" id="deviceStatusCN" style="width: 156px" />
                    </li>
                    <li>设备名称：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" name="deviceName" id="deviceName" style="width: 156px" />
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
            	<a href="javascript:void(0)" class="NorToolBtn ExportBtn" onclick="exportData();">导出</a>
				<!--<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {

        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#gridId").val(grid.gridId);
                $("#regionCode").val(grid.orgCode);
            }
        });

        AnoleApi.initListComboBox("deviceStatusCN", "deviceStatus", null, null, [''], {
            DataSrc : [{"name":"在线", "value":"1"},{"name":"离线", "value":"0"}],
            ShowOptions : {
                EnableToolbar : true
            },
            DefText : '请选择'
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
			url: '${rc.getContextPath()}/zhsq/event/pointInfo/listData.jhtml',
            //queryParams: $('#searchForm').serializeJson(),
			columns: [[
                {field:'gridPath', title:'网格名称', align:'center', width:200,formatter : function(val, rec) {
                    /*var str;
                    if(val.length>20){
                        str = val.substr(0,20)+'...';
                    }*/
                    return '<div class="sub"><a title="'+val+'" onclick="showVideo('+rec.id+')">'+val+'</a></div>';
                }},
				{field:'deviceName', title:'设备名称', align:'center', width:100},
				{field:'deviceId', title:'设备id', align:'center', width:100},
				{field:'latitude', title:'设备纬度', align:'center', width:100},
				{field:'longitude', title:'设备经度', align:'center', width:100},
				{field:'deviceStatus', title:'状态', align:'center', width:100,formatter:function(value,rec,rowIndex){
					var deviceStatus = rec.deviceStatus;
					if(deviceStatus == '1'){
				      	value='在线';
				    }else{
				      	value='离线';
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
		var url = '${rc.getContextPath()}/zhsq/event/pointInfo/form.jhtml';
		showMaxJqueryWindow('新增', url, 500, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/event/pointInfo/form.jhtml?id=' + rows[0].id;
			showMaxJqueryWindow('编辑', url, 500, 400);
		}
	}

    //查看视频
    function showVideo(id){
        var url = '${rc.getContextPath()}/zhsq/event/pointInfo/showVideo.jhtml?id='+id;
        showMaxJqueryWindow("视频播放", url, 800, 550, null);
    }

	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/event/pointInfo/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 800, 300);
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
						url: '${rc.getContextPath()}/zhsq/event/pointInfo/del.jhtml',
						data: {
							id: rows[0].id
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
