<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.inp1 {width:120px;}
		.hidden-more{
			overflow: hidden;
		    text-overflow: ellipsis;
		    display: block;
		    white-space: nowrap;
		    float:left;
		    width: 100%;
		}
		.urgency-icon{
			float: left;
		    top: 17px;
		    position: relative
		}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<input class="inp1" type="hidden" id="menuType" name="menuType" value="${menuType!''}"/>
			<div class="fl">
				<ul>
					<li>流程名称：</li><li><input class="inp1" type="text" id="flowName" name="flowName"  /></li>
					<li>当事人姓名：</li><li><input class="inp1" type="text" id="receiveName" name="receiveName" /></li>
					<li>当事人地址：</li><li><input class="inp1" type="text" id="receiveAddr" name="receiveAddr" /></li>
					<li style="position: relative;"><a href="#" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	                    <div class="AdvanceSearch DropDownList hide" style="width: 400px; top: 42px; left: -160px;">
	                        <div class="LeftShadow">
	                            <div class="RightShadow">
	                                <div class="list NorForm">
	                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	                                        
	                                        <tr>
	                                            <td>
	                                            	<!-- publishDate -->
	                                                <label class="LabName"><span>发起时间：</span></label>
	                                                <input type="text" class="inp1 Wdate" style="width:120px; height:28px"
	                                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'sendDateEnd\')}'});" 
	                                                       name="sendDateStart" id="sendDateStart"/>
	                                                <label>~</label>
	                                                <input type="text" class="inp1 Wdate" style="width:120px; height:28px"
	                                                       onClick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'sendDateStart\')}'});" 
	                                                       name="sendDateEnd" id="sendDateEnd"/>
	                                            </td>
	                                        </tr>
	                                        
	                                        <tr>
	                                            <td>
	                                                <label class="LabName"><span>协助类型：</span></label>
                                                    <input type="hidden" name="sendType" id="sendType"/>
                                                    <input type="text" id="sendTypeName" name="sendTypeName" value="" class="inp1 easyui-validatebox" />
                                                </td>
	                                        </tr>
	                                        <tr>
	                                            <td>
	                                                <label class="LabName"><span>流程状态：</span></label>
	                                                <input type="hidden" id="status" name="status" />
	            									<input type="text" id="statusCN" name="statusCN" style="width: 120px;" class="inp1" data-options="validType:'maxLength[10]', tipPosition:'bottom'" />
	                                            </td>
	                                        </tr>
	                                    </table>
	                                </div>
	
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
		<#if menuType=="mycreate" || menuType=="mywait">
			<div class="ToolBar">
				<div class="tool fr">
<#--					<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="handle();">办理</a>-->
<#--					<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
					<@actionCheck></@actionCheck>
				</div>
			</div>
		</#if>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		//案件类型；01草稿、02流转中、03已送达、04未送达、05归档
		AnoleApi.initListComboBox("statusCN", "status", null, null, ["-1"], {
			ShowOptions : {
				EnableToolbar : false
			},
			DataSrc : [
				{"name":"全部","value":"-1"},
				{"name":"草稿","value":"01"},
				{"name":"流转中","value":"02"},
				{"name":"已送达","value":"03"},
				{"name":"未送达","value":"04"},
				{"name":"归档","value":"05"}
			]
		});
        AnoleApi.initListComboBox("sendTypeName", "sendType", "B12322001",null,[]);
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
			url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/listData.jhtml',
			queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'flowName', title:'流程名称', align:'left', width:120,
					formatter:function(value, rec, rowIndex){
                        var temp = '';
                        if(value == null || typeof (value) == 'undefined' || value == ""){
                            temp = '-';
                        }else{
                            temp = value;
                        }
                        
                        var urgencyIcon = "";
                        var htmlStr = '<div class="clearfix" ><a class="hidden-more" href="###" class="" title="' + value + '" onclick="detail(' + rec.sendId + ')"> ' + temp  +'</a></div>' ;
                        var surplusDateNumAbs = Math.abs(rec.surplusDateNum);
                        var titleTip = rec.surplusDateNum<0?"已超期"+surplusDateNumAbs+"天":"办理时间剩余"+surplusDateNumAbs+"天";
                        if(rec.surplusDateNum<=1 && rec.status=='02' ){
                        	//temp="发起时间发起时间发起时间发起时间";
                        	urgencyIcon = '<i class="ToolBarUrgency urgency-icon" title="'+titleTip+'" ></i>';
                        	htmlStr = '<div class="clearfix" ><a  href="###" class="hidden-more" style="width:80%;" title="' + value + '" onclick="detail(' + rec.sendId + ')"> ' + temp +'</a>'+ urgencyIcon + '</div>' ;
                        }
                                              
                        
                        return htmlStr;
                    }	
				},
				{field:'sendTypeCN', title:'协作类型', align:'center', width:70, formatter:forrmatCol},
				{field:'publishDateStr', title:'发起时间', align:'center', width:70, formatter:forrmatCol},
				{field:'limitDateNum', title:'办理时限(天)', align:'center', width:70, formatter:forrmatCol},
				/* {field:'limitDateStr', title:'最后时间', align:'center', width:100, formatter:forrmatCol},
				{field:'surplusDateNum', title:'剩余时间(天)', align:'center', width:100, formatter:forrmatCol}, */
				{field:'receiveName', title:'当事人姓名', align:'center', width:110, formatter:forrmatCol},
				{field:'receiveAddr', title:'当事人地址', align:'center', width:110, formatter:forrmatCol},
				{field:'statusStr', title:'状态', align:'center', width:50, formatter:forrmatCol},
				{field:'currentHandler', title:'当前办理人', align:'center', width:110, formatter:forrmatCol},
				{field:'currentCodeCn', title:'当前办理组织', align:'center', width:110, formatter:forrmatCol},
				{field:'currentNode', title:'当前环节', align:'center', width:85, formatter:forrmatCol}
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
	
	//列表格式换
	function forrmatCol(value, rec, rowIndex){
		var temp = '';
        if(value == null || typeof (value) == 'undefined' || value == ""){
            temp = '-';
        }else{
            temp = value;
        }
        var htmlStr = '<span class="hidden-more" title="' + value + '">' + temp + '</span>';
        return htmlStr;
	}
	
	//新增
	function add() {
		var url = '${rc.getContextPath()}/zhsq/gdPersionSendFlow/form.jhtml';
		showMaxJqueryWindow('新增', url, null, null);
	}
	
	//编辑
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var status = rows[0].status;
			if(status!="01"){
				$.messager.alert('提示', '流程已启动，不允许编辑!', 'warning');
				return;
			}
			var url = '${rc.getContextPath()}/zhsq/gdPersionSendFlow/form.jhtml?id=' + rows[0].sendId;
			showMaxJqueryWindow('编辑', url, null, null);
			
		}
	}
	
	//办理
	function handle() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var url = '${rc.getContextPath()}/zhsq/gdPersionSendFlow/view.jhtml?showHandle=y&id=' + rows[0].sendId;
			showMaxJqueryWindow('办理', url, null, null); 
		}
	}
	
	
	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/gdPersionSendFlow/view.jhtml?id=" + id;
		showMaxJqueryWindow('详情', url, null, null);
	}
	
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条记录!', 'warning');
		} else {
			var status = rows[0].status;
			if(status!="01"){
				$.messager.alert('提示', '流程已启动，不允许删除!', 'warning');
				return;
			}
			
			$.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
				if (r) {
					modleopen(); //打开遮罩层
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/del.json',
						data: {
							sendId: rows[0].sendId
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('错误', '删除失败！', 'error');
							} else {
								$.messager.alert('提示', '删除成功！', 'info');
								searchDataReLoad();
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
	
	//查询：列表回到第一页
	function searchData() {
		$('#list').datagrid('load', $('#searchForm').serializeObject());
	}
	
	//查询：列表停留在当前页
	function searchDataReLoad() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		var menuType = $("#menuType").val();
		$('#searchForm').form('clear');
		$("#menuType").val(menuType);
		searchData();
	}
</script>
</html>
