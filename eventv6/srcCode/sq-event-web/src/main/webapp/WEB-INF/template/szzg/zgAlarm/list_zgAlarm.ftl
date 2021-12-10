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
				<li>告警类型：</li>
				<li>
					<input type="hidden" name="alarmType" id="alarmType" value='' />
					<input type="text" id="alarmTypeName" name="alarmTypeName" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
				</li>
				<li>告警级别：</li>
				<li>
					<input type="hidden" name="alarmLevel" id="alarmLevel" value='' />
					<input type="text" id="alarmLevelName" name="alarmTypeName" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
				</li>
				 <li>告警内容：</li>
				<li>
					<input type="text" id="alarmContent" name="alarmContent" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[500]', tipPosition:'bottom'" />
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
		AnoleApi.initTreeComboBox("alarmTypeName", null, '${ZG_ALARM_TYPE!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('alarmType').value = items[0].value;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});
        AnoleApi.initTreeComboBox("alarmLevelName", null, '${ZG_ALARM_LEVEL!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('alarmLevel').value = items[0].value;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});

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
			url: '${rc.getContextPath()}/zhsq/szzg/zgAlarm/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'alarmTypeName', title:'告警类型', align:'center', width:100},
				{field:'alarmLevelName', title:'告警级别', align:'center', width:100},
				{field:'alarmContent', title:'告警内容', align:'center', width:100,
					formatter: function (val, rec) {
                       return '<a href="javascript:detail(' +rec.alarmId+')">' + val + '</a>';
                    }
				},
				{field:'invalidDate', title:'失效时间', align:'center', width:100,formatter:formatDatebox},
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
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/szzg/zgAlarm/add.jhtml';
		showMaxJqueryWindow('新增', url, 600, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/szzg/zgAlarm/add.jhtml?id=' + rows[0].alarmId;
			showMaxJqueryWindow('编辑', url, 600, 400);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/szzg/zgAlarm/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 600, 400);
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
						url: '${rc.getContextPath()}/zhsq/szzg/zgAlarm/del.json',
						data: {
							alarmId: rows[0].alarmId
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
		 $('#list').datagrid('reload', {
			alarmType: $('#alarmType').val(), 
			alarmLevel: $('#alarmLevel').val(), 
			alarmContent : $('#alarmContent').val()
		});
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
	
	Date.prototype.format = function (format) {  
	    var o = {  
	        "M+": this.getMonth() + 1, // month  
	        "d+": this.getDate(), // day  
	        "h+": this.getHours(), // hour  
	        "m+": this.getMinutes(), // minute  
	        "s+": this.getSeconds(), // second  
	        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
	        "S": this.getMilliseconds()  
	        // millisecond  
	    }  
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	}  
	function formatDatebox(value,type) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	    if (type != null && type == 'M' ) {  
	         return dt.format("yyyy-MM"); 
	    }  
	    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
	} 
</script>
</html>
