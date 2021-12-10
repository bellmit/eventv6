<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>

<style>

</style>
</head>
<body class="easyui-layout">

	<div id="jqueryToolbar">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
            	<li>所属网格：</li>
                <li>
                	<input id="gridId" name="gridId" type="hidden" value="${startGridId?c}"/>
                	<input id="bizType" name="bizType" type="hidden"/>
                	<input id="gridCode" name="gridCode" type="hidden"/>
                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;" value="<#if startGridName??>${startGridName}</#if>"/>
                </li>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="" style="font-size:12px;font-style:italic;color:gray; width:210px;" onfocus="if(this.value==''){this.value='';}$(this).attr('style','width:210px;');" onblur="if(this.value==''){$(this).attr('style','font-size:12px;font-style:italic;color:gray;width:210px;');this.value='';}"/></li>
            	<li>
            	</li>
            </ul>
        </div>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
		<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
			
			
		</script>	
        <div class="tool fr">
        	<@ffcs.right rightCode="setting" parentCode="${system_privilege_action?default('')}">
        		<a href="#" class="NorToolBtn ShangBaoBtn" onclick="setting();">配置</a>
        	</@ffcs.right>
			<@ffcs.right rightCode="link" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" onclick="link();">对接</a>
			</@ffcs.right>
        </div>
    </div>
	

</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
<script type="text/javascript">
	var idStr = "";
    $(function(){
    	ztreeComboBoxObj = AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#gridId").val(grid.gridId);
				$("#gridCode").val(grid.gridCode);
			}
		});
    	
    	loadDataList();//init data
    });
    
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			remoteSort:false,
			striped: true,
			fit: true,
			singleSelect: true,
			idField:'EVENT_ID',
			url:'${rc.getContextPath()}/zhsq/dockingmanage/listData.json',
			columns:[[
		        {field:'EVENT_ID',title:'EVENT_ID', align:'left',width:fixWidth(0.1),sortable:true,
					formatter:function(value,rec,rowIndex){
						var d = '<a href="javascript:show('+rec.eventId+')">'+value+'</a>';
                		return d;
                	}
				},
                {field:'EVENT_NAME',title:'事件名称', align:'left',width:fixWidth(0.57),sortable:true},
                {field:'STATUS',title:'事件状态', align:'left',width:fixWidth(0.17),sortable:true},
				{field:'exchangeFlag',title:'查看', align:'center',width:fixWidth(0.1),sortable:true,
					formatter:function(value,rec,rowIndex){
						var d = '<a href="javascript:showLog('+rec.eventId+')">查看</a>';
                		return d;
                	}
				}
			]],
			toolbar:'#jqueryToolbar',
// 			pagination:true,
// 			pageSize: 20,
			onLoadSuccess:function(data){
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
				if(data.total==0){
				var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.gif" title="暂无数据"/></div>');
				}
			},
			queryParams:{bizPlatform:'004'}
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }
    
    function setting(){
    	
    }
    
    function showLog(eventId){
    	
    }
    
    function show(eventId){
    	var rows = $('#list').datagrid('getSelections');
		var num = rows.length;
		if (num != 1) {
			$.messager.alert('提示', '请选择一条记录！', 'warning');
			return;
		} else {
			var eventId = rows[0].EVENT_ID;
			var url = '${rc.getContextPath()}/zhsq/dockingmanage/sub/index.jhtml?eventId='+eventId;
			showMaxJqueryWindow("信息", url, 600, fixHeight(0.9));
		}
    }
    
	function link() {
		var rows = $('#list').datagrid('getSelections');
		var num = rows.length;
		if (num != 1) {
			$.messager.alert('提示', '请选择一条记录！', 'warning');
			return;
		} else {
			var eventId = rows[0].EVENT_ID;
			$.ajax({
				type : "POST",
				url : '${rc.getContextPath()}/zhsq/dockingmanage/report.jhtml',
				data : 'eventId=' + eventId,
				dataType : "json",
				success : function(data) {
					alert(data);
				},
				error : function(data) {
					$.messager.alert('错误', '连接错误！', 'error');
				}
			});
		}

	}

	var idStr = "";
	var startGridId = "";

	function searchData(b) {
		var a = new Array();
		var keyWord = $("#keyWord").val();
		if (keyWord != null && keyWord != "" && keyWord != "")
			a["keyWord"] = keyWord;
		var gridId = $("#gridId").val();
		if (gridId != null && gridId != "") {
			a["gridId"] = gridId;
		}
		var gridCode = $("#gridCode").val();
		if (gridCode != null && gridCode != "") {
			a['gridCode'] = gridCode;
		}
		doSearch(a);
	}

	function doSearch(queryParams) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
	}

	function resetCondition() {
		$("#keyWord").val("");
		$('#gridName').val("");
		$('#gridId').val("");
		searchData();
	}
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result) {
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
		$("#list").datagrid('load');
	}
</script>
</html>