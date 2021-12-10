<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/listSet.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
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
                        <li>诉求编号：</li>
                        <li><input type="text" name="appealNo" class="inp1 queryParam" id="appealNo" value="" style="width:150px;" /></li>
                        <li>诉求标题：</li>
                        <li><input type="text" name="appealTitle" class="inp1 queryParam" id="appealTitle" value="" style="width:150px;" /></li>
                        <li>诉求状态：</li>
                        <li>
                        	<input type="hidden" id="handleSit" name="handleSit" class="inp1 queryParam" />
                        	<input class="inp1" type="text" id="handleSitStr" name="handleSitStr" style="width:90px;" />
                        </li>

                        <li style="position:relative;">
                            <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                            <div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                                <div class="LeftShadow">
                                    <div class="RightShadow">
                                        <div class="list NorForm" style="position:relative;">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td><label class="LabName width65px"><span>诉求类别：</span></label>
														<input class="inp1 queryParam" type="hidden" id="appealCatalog" name="appealCatalog" style="width:135px;" />
														<input class="inp1" type="text" id="appealCatalogCN" name="appealCatalogCN" style="width:145px;" />
													</td>
                                                </tr>
                                                <tr>
                                                    <td><label class="LabName width65px"><span>来源：</span></label>
                                                        <input class="inp1 queryParam" type="hidden" id="source" name="source" style="width:135px;" />
                                                        <input class="inp1" type="text" id="sourceCN" name="sourceCN" style="width:145px;" />
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div class="BottomShadow"></div>
                                    </div>
                                </div>
                            </div>
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
			<div class="tool fr" id="toolFrDIV">
				<@actionCheck></@actionCheck>
                <a id="examine" href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="examine();">审核</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

	var listType = "${listType}";

	$(function() {

        AnoleApi.initListComboBox("appealCatalogCN", "appealCatalog", "A001093088", null, null, {

            ShowOptions : {
                EnableToolbar : true
            }
            
        });

        AnoleApi.initListComboBox("sourceCN", "source", "A001093087", null, null, {
            ShowOptions : {
                EnableToolbar : true
            }
        });
        
        AnoleApi.initListComboBox("handleSitStr", "handleSit", "A001093089", null, null, {
            ShowOptions : {
                EnableToolbar : true
            }
        });

		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
	
		var queryParams = queryData();
	
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/publicAppeal/listData.jhtml',
			queryParams: queryParams,
			columns: [[
				{field:'appealNo', title:'诉求编号', align:'center', width:90,
                    formatter:function(value,rec,rowIndex){
                        if(value==null)return "";
                        var f = '<a href="###" title='+ rec.appealTitle +' onclick=detail('+ rec.appealId+ ')>'+value+'</a>&nbsp;';
                        return f;
                    }
                },
				{field:'appealTitle', title:'诉求标题', align:'center', width:120},
				{field:'appealCatalogStr', title:'诉求类别', align:'center', width:100},
				{field:'appealCatalog', width:100,hidden:'true'},
//				{field:'content', title:'诉求内容', align:'center', width:100},
				{field:'sourceStr', title:'来源', align:'center', width:60},
				{field:'source', align:'center',hidden:'true'},
				{field:'appealTimeStr', title:'诉求时间', align:'center', width:100},
				//{field:'appealStatus', title:'诉求状态', align:'center', width:100},
				{field:'userName', title:'姓名', align:'center', width:80},
				{field:'phone', title:'手机号', align:'center', width:80},
				{field:'orgEntityPath', title:'所属区域', align:'center', width:180},
                {field:'handleSit', width:60,hidden:'true'},
                {field:'handleSitStr', title:'处理情况', align:'center', width:60}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
            onLoadSuccess: function(data) {
                listSuccess(data); //暂无数据提示
            },
            onLoadError: function() {
                listError();
            },
            onSelect:function(index,rec){
                authority(rec);
            }
		});
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/gmis/publicAppeal/form.jhtml';
		showMaxJqueryWindow('新增', url, 500, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/gmis/publicAppeal/form.jhtml?id=' + rows[0].appealId;
			showMaxJqueryWindow('编辑', url, 500, 400);
		}
	}

	//审核
	function examine1() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/publicAppeal/examine.jhtml?id=' + rows[0].appealId;
			showMaxJqueryWindow('审核', url, 500, 400);
		}
	}


    function examine() {
        var rows = $('#list').datagrid('getSelections');
        var opt = {
            'maxHeight': 400,
            'maxWidth': 800,
            'onBeforeClose': searchData
        };

        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = '${rc.getContextPath()}/zhsq/publicAppeal/examine.jhtml?id=' + rows[0].appealId;

            opt.title = "审核";
            opt.targetUrl = url;
            openJqueryWindowByParams(opt);
        }
    }

	function reject(){
        var rows = $('#list').datagrid('getSelections');
        var opt = {
            'maxHeight': 400,
            'maxWidth': 800
        };
        var url = '${rc.getContextPath()}/zhsq/publicAppeal/reject.jhtml?id=' + rows[0].appealId;

        opt.title = "驳回";
        opt.targetUrl = url;

        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            openJqueryWindowByParams(opt);
        }
	}

	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/publicAppeal/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 800, 400);
	}





	function eventDetail(id, listType) {
        var opt = {
//            'maxHeight': 400,
//            'maxWidth': 800,
            'onBeforeClose': searchData
        };

        listType = listType || "${listType!}";

        var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventId=' + id + "&eventType=todo";

        opt.title = "查看民众诉求事件";
        opt.targetUrl = url;

        openJqueryWindowByParams(opt);

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
						url: '${rc.getContextPath()}/gmis/publicAppeal/del.json',
						data: {
							appealId: rows[0].appealId
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
	
	function queryData() {
    	var searchArray = new Array();
    	
		$("#searchForm .queryParam").each(function() {
			var val = $(this).val(), key = $(this).attr("name");
			
			if($(this).hasClass("keyBlank")) {
				val = "";
			}
			
			if(isNotBlankString(val) && isBlankString(searchArray[key])){
				searchArray[key] = val;
			}
		});
		
		searchArray["listType"]=listType;
		
		return searchArray;
	}

    function searchData(isCurrent){//查询
		doSearch(queryData(), isCurrent);
	}

    function doSearch(queryParams,isCurrent){
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams=queryParams;
        if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
    }
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		var queryParams=queryData();
		$("#list").datagrid('options').queryParams = queryParams;
		searchData();
	}
	
	
	//按钮设置
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = [];
			
			if(selectedRow.handleSit == '1') {
				showBtn = ["EditBtn"];
			}
			
			$("#toolFrDIV > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDIV ." + showBtn[index]).show();
			}
		}
	}
	
	//查询
	function searchData1() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
</script>
</html>
