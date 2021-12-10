<!DOCTYPE html>
<html>
<head>

	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
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
                        <input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam"/>
                        <input id="gridId" name="gridId" type="text" class="hide queryParam"/>
                        <input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                    </li>
                    <li>事件标题：</li>
                    <li>
                        <input id="eventName" name="eventName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                    </li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:400px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td><label class="LabName width65px"><span>事件编号：</span></label><input class="inp1 queryParam" type="text" id="code" name="code" style="width:135px;"></input></td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>推送人：</span></label><input class="inp1 queryParam" type="text" id="pushName" name="pushName" style="width:135px;"></input></td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>采集时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="createTimeStart" name="createTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="createTimeEnd" name="createTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
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
				<@actionCheck></@actionCheck> 
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		loadList(); //加载列表
		
		 AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
            if(items!=undefined && items!=null && items.length>0){
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
            }
        }, {
            OnCleared: function() {
                $("#infoOrgCode").val('');
            },
            ShowOptions: {
                EnableToolbar : true
            }
        }); 
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
			url: '${rc.getContextPath()}/zhsq/eventPush/listData.json',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'id', hidden:true},
				{field:'eventId', hidden:true},
				{field:'eventName', title:'事件标题', align:'center', width:'30%',formatter: titleFormatter},
				{field:'typeCN', title:'事件分类', align:'center', width:'18%',formatter: tipFormatter},
				{field:'gridPath', title:'所属网格', align:'center', width:'20%',formatter: tipFormatter},
				{field:'statusCN', title:'当前状态', align:'center', width:'10%'},
				{field:'createName', title:'推送人', align:'center', width:'8%'},
				{field:'createTime', title:'采集时间', align:'center', width:'10%',formatter: dateFormatter}
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
						url: '${rc.getContextPath()}/zhsq/eventPush/del.json',
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
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
	
	
	//标题
	function titleFormatter(value, rec, rowIndex){
		if(value == "") return "";
		return '<a href="###"  onclick="showDetailRow('+rec.eventId+','+rec.instanceId+','+rec.workFlowId+','+rec.bizPlatform+','+rec.type+')"><span style="color:blue; text-decoration:underline;">'+value+'</span></a>';
	}
	
	//提示
	function tipFormatter(value, rec, rowIndex){
		if(value == "") return "";
		return '<span title = ' + value+'>' + value +'</span>';
	}
	
	//详情
	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
        if(!eventId){
            $.messager.alert('提示','请选择一条记录！','info');
        }else{
            if(bizPlatform == "001"){
                var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
                showMaxJqueryWindow("查看事件信息", url);
            }else{
                var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
                showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
            }
        }
    }
	
	
	//格式化时间戳
	function dateFormatter(value, rec, rowIndex){
		if(value == "") return "";
		return format(value);
	}
	
	function add0(m){return m<10?'0'+m:m }
	function format(shijianchuo)
	{
		var time = new Date(shijianchuo);
		var y = time.getFullYear();
		var m = time.getMonth()+1;
		var d = time.getDate();
		var h = time.getHours();
		var mm = time.getMinutes();
		var s = time.getSeconds();
		return y+'-'+add0(m)+'-'+add0(d);
	}
</script>
</html>
