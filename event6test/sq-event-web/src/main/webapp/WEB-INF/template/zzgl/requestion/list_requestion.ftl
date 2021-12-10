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
		.width65px{width:75px;}
		.w150{width:150px;}
		.keyBlank{color:gray;}
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
					<li>关键字：</li>
	                <li>
		                <input name="keyWord" type="text" class="inp1" id="keyWord" value="可输入案件编号、标题等关键字" style="color:gray; width:170px;" onfocus="if(this.value=='可输入案件编号、标题等关键字'){this.value='';}$(this).attr('style','width:170px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:170px;');this.value='可输入案件编号、标题等关键字';}" onkeydown="_onkeydown();" />
	            	</li>
					<li>案件类型：</li>
	                <li>
	                	<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable w150" />
	                </li>
	                <li style="position:relative;">
	            		<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	            		<div class="AdvanceSearch DropDownList hide" style="width:430px; top: 42px; left: -130px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm" style="position:relative;">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>办理部门：</span></label>
	                                    			<input id="typeName" name="typeName" type="text" class="inp1 InpDisable w150" />
	                                    		</td>
	                                    	</tr>
	                                    	<tr>
	                                    		<td>
	                                    			<label class="LabName width65px"><span>提交时间：</span></label>
	                                    			<input type="text" id="happenTimeStr" name="happenTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'endTimeStr', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
								                		~
								                	<input type="text" id="endTimeStr" name="endTimeStr" class="inp1 Wdate easyui-validatebox" style="width:155px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, minDate:'happenTimeStr', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
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
			<div class="tool fr">
				<!--<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">处理案件</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">案件登记</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("typeName", "type", "${caseTypeDict}", null, null, {
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
			url: '${rc.getContextPath()}/zhsq/requestion/listData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'code', title:'案件编号', align:'center', width:100,formatter : function(val, rec) {
					return '<a href="javascript:detail(\''+rec.reqId+'\');" >'+val+'</a>';
				}},
				{field:'creatTime', title:'提交时间', align:'center', width:100},
				{field:'type', title:'案件类型', align:'center', width:100},
				{field:'title', title:'案件标题', align:'center', width:100},
				{field:'content', title:'办理部门', align:'center', width:100},
				{field:'expectTime', title:'办理状态', align:'center', width:100}
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
		var url = '${rc.getContextPath()}/zhsq/requestion/form.jhtml';
		showMaxJqueryWindow('新增', url, 800, 400);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/requestion/form.jhtml?id=' + rows[0].reqId;
			showMaxJqueryWindow('编辑', url, 800, 400);
		}
	}
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/requestion/detail.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, 800, 400);
	}
	
	//办理
	function todo() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/requestion/todo.jhtml?id=" + rows[0].reqId;
			showMaxJqueryWindow('详情', url, 800, 400);
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
						url: '${rc.getContextPath()}/zhsq/requestion/del.json',
						data: {
							reqId: rows[0].reqId
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
		var a = new Array();
		var type = $("#type").val();
		a["type"] = type;
		var happenTimeStr = $('#happenTimeStr').val();
		a["happenTimeStr"] = happenTimeStr;
		var endTimeStr = $('#endTimeStr').val();
		a["endTimeStr"] = endTimeStr;
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="可输入案件编号、标题等关键字") {
			a["keyWord"] = keyWord;
		}
		doSearch(a);
	}
	
	//重置
	function resetCondition() {
		$("#searchForm")[0].reset();
		$("#keyWord").attr('style','font-size:12px;color:gray; width:170px;');
		searchData();
	}
	
	function doSearch(queryParams) {
		// resetCondition();
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData();
		}
	}
	
</script>
</html>
