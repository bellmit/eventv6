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
				<li>所属区域：</li>
				<li>
					<input type="hidden" id="orgCode" name="orgCode"  value="${orgCode!}" >
                     <input class="inp1 inp2 InpDisable easyui-validatebox" style="width:100px" type="text" id="orgName"
                              name="orgName"  value="${orgName!}" />
           		 </li>
           		 <li>监测站点名称：</li>
				<li>
					<input type="text" id="name" name="name" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
				</li>
				<li>水质类别：</li>
				<li>
					<input type="hidden" name="szlb" id="szlb" value='' />
					<input type="text" id="szlbName" name="szlbName" value="" class="inp1 easyui-validatebox" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
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
	   AnoleApi.initTreeComboBox("szlbName", null, '${ZG_WATER_TYPE!}', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('szlb').value = items[0].value;
            }
        }, ["${(bo.szlb)!}"], {ChooseType: '1', ShowOptions: {EnableToolbar: true}});
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
			url: '${rc.getContextPath()}/zhsq/szzg/water/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'orgName', title:'所属区域', align:'center', width:100},
				{field:'name', title:'监测站点名称', align:'center', width:100,
					formatter: function (val, rec) {
                       return '<a href="javascript:detail(' +rec.seqId+')">' + val + '</a>';
                    }
				},
				{field:'longitude', title:'经度', align:'center', width:100},
				{field:'dimensions', title:'纬度', align:'center', width:100},
				{field:'phvalue', title:'PH(无量纲)', align:'center', width:100},
				{field:'rjy', title:'溶解氧(mg/l)', align:'center', width:100},
				{field:'mgzs', title:'高锰酸盐指数(mg/l)', align:'center', width:100},
				{field:'al', title:'氨氯(mg/l)', align:'center', width:100},
				{field:'szlb', title:'水质类别', align:'center', width:100},
				{field:'endTime', title:'检测时间', align:'center', width:100,formatter:formatDatebox},
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
		var url = '${rc.getContextPath()}/zhsq/szzg/water/add.jhtml';
		showMaxJqueryWindow('新增', url, 650, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/szzg/water/add.jhtml?id=' + rows[0].seqId;
			showMaxJqueryWindow('编辑', url, 650, 400);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/szzg/water/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 560, 400);
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
						url: '${rc.getContextPath()}/zhsq/szzg/water/del.json',
						data: {
							seqId: rows[0].seqId
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
			orgCode: $('#orgCode').val(), 
			szlb: $('#szlb').val(), 
			name : $('#name').val()
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
	    return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)  
	} 
</script>
</html>
