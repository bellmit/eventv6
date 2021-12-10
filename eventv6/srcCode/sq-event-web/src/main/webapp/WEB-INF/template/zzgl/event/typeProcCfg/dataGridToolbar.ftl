<#include "/component/ComboBox.ftl" />
<style type="text/css">
.width65px{width:65px;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <form id="searchForm">
        <div class="fl">
        	<ul>
                <li>所属网格：</li>
                <li>
                	<input id="regionCode" value="${regionCode!''}" type="text" style="display:none;"/>
                	<input id="regionName" name="regionName" type="text" class="inp1 InpDisable" style="width:160px;" value="${regionName!''}"/>
                </li>
            	<li>事件类型：</li>
                <li>
                	<input id="eventType" type="text" style="display:none;"/>
                	<input id="eventTypeName" name="eventTypeName" type="text" class="inp1 InpDisable" style="width:160px;"/>
                </li>
            </ul>
        </div>
        </form>
        <div class="btns">
        	<ul>            	
            	<li><a href="###" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
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
    		<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
    			<a href="###" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			</@ffcs.right>
			<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a href="###" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a href="###" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	var queryValue = "";
	
	$(function() {
		AnoleApi.initGridZtreeComboBox("regionName", null, function(gridId, items) {
			if (items && items.length > 0) {
				$("#regionCode").val(items[0].orgCode);
			}
		});
        
        AnoleApi.initTreeComboBox("eventTypeName", "eventType", "A001093199", null, null, {
        	ChooseType : "1",
        	ShowOptions : {
        		EnableToolbar : true
        	}
        });
	});
	
	function add() {
		var url = '${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/toSavePage.jhtml?optType=0';
		var opt = $.extend({}, _win_opt, {
	    	title: "新增事件采集配置",
	    	targetUrl: url,
	    	width: 770,
	    	maxHeight: 306
	    });
		openJqueryWindowByParams(opt);
	}
	
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		if(rows.length == 0) {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		var url = '${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/toSavePage.jhtml?regionCode='+rows[0].regionCode+'&type='+rows[0].type;
		var opt = $.extend({}, _win_opt, {
	    	title: "编辑事件采集配置",
	    	targetUrl: url,
	    	width: 770,
	    	maxHeight: 306
	    });
		openJqueryWindowByParams(opt);
	}
	
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if(rows.length == 0) {
			$.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
			return;
		}
		$.messager.confirm('提示', '您确定删除选中的记录吗？', function(r){
			if (r){
				modleopen();
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventTypeProcCfgController/delete.jhtml',
					data: {"regionCode" : rows[0].regionCode, "type" : rows[0].type},
					dataType:"json",
					success: function(data){
						DivShow(data.tipMsg);
						$('#list').datagrid('unselectAll');
						$('#list').datagrid('getPager').pagination('select');
						modleclose();
					},
					error:function(data){
						$.messager.alert('错误','连接超时！','error');
						modleclose();
					}
				});
			}
		});
	}
	
	function resetCondition() {
		$("#searchForm")[0].reset();
		searchData(1);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			searchData(1);
		}
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, isCurrPage) {
		closeMaxJqueryWindow();
		DivShow(result);
		if (isCurrPage) {
			$('#list').datagrid('clearSelections');
			$('#list').datagrid('getPager').pagination('select');
		} else {
			searchData();
		}
	}
	
</script>