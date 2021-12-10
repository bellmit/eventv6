<#include "/component/ComboBox.ftl" />
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="eventTaskForm">
			<input type="hidden" id="startInfoOrgCode" value="${infoOrgCode!}" />
			<input type="hidden" id="eventType" name="eventType" value="all" class="queryParam" />
			
			<!--转派按钮使用属性-->
			<input type="text" id="transferUserIds" class="hide"/>
			<input type="text" id="transferUserNames" class="hide"/>
			<input type="text" id="transferCurOrgIds" class="hide"/>
			<input type="text" id="transferCurOrgNames" class="hide"/>
			
	        <div class="fl">
	        	<ul>
	        		<li>当前办理人：</li>
	        		<li>
	        			<input type="text" id="curUserIds" name="curUserIds" class="queryParam hide"/>
						<input type="text" id="curOrgIds" name="curOrgIds" class="queryParam hide"/>
						<input type="text" id="curOrgNames" class="hide"/>
						
	        			<input type="text" id="curUserNames" class="inp1 InpDisable" style="cursor: pointer; width: 135px;" onclick="_selectUserAll();" readonly />
	        		</li>
	        		<li>所属网格：</li>
	                <li><input type="text" id="infoOrgCode" name="infoOrgCode" class="queryParam hide"/><input type="text" id="gridId" name="gridId" class="queryParam hide"/><input type="text" id="gridName" name="gridName" class="inp1 InpDisable" style="width: 150px;" /></li>
	                <li>事件标题：</li>
	                <li><input type="text" id="eventName" name="eventName" class="queryParam inp1" style="width: 135px;" onkeydown="_onkeydown();"></input></li>
	                <li>事件编号：</li>
	                <li><input type="text" id="code" name="code" class="queryParam inp1" style="width: 135px;" onkeydown="_onkeydown();"></input></li>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="###" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
	            	<li><a href="###" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>
        </form>
        
        <div class="clear"></div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="toolBarDiv">
    		
        <div id="toolFrDiv" class="tool fr">
        	<#if isTransferAble?? && isTransferAble>
        		<a href="###" class="NorToolBtn TransferBtn" onclick="taskTransfer();">转派</a>
        	</#if>
        </div>
    </div>
	
</div>

<script type="text/javascript">
	var _transferTaskIntervalId = null;
	
	<!--人员选择使用参数-->
	var g_EventNodeCode = {};
	
	$(function(){
		if($('#toolFrDiv > a').length == 0){
			$("#toolBarDiv").hide();
		}
		
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
	});
	
	function _selectUserAll() {//办理人员选择条件
		g_EventNodeCode.nodeCode = "${nodeCode!}";
		
		selectUser('curUserIds', 'curUserNames', 'curOrgIds', 'curOrgNames', 'task', '0');<!--思明区需要使用nodeId参数-->
	}
	
	<#if isTransferAble?? && isTransferAble>
		function taskTransfer() {//任务转派
			var taskId = "";
			$("input[name='CUR_TASK_ID']:checked").each(function() {
				taskId = $(this).val();
			});
			
			if(isBlankString(taskId)) {
				$.messager.alert('警告','请选中要转派的环节再执行此操作!','warning');
			} else {
				modleopen();
				
				var selectItem = $("#list").datagrid("getSelected");
				
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventTask/transferNodeCode.jhtml',
					data: {'instanceId': selectItem.INSTANCE_ID},
					dataType:"json",
					success: function(data){
						modleclose();
						
						if(data.nodeCode) {
							g_EventNodeCode.nodeCode = data.nodeCode;
						} else {
							g_EventNodeCode.nodeCode = "${nodeCode!}"
						}
						
						selectUser('transferUserIds', 'transferUserNames', 'transferCurOrgIds', 'transferCurOrgNames', 'task', data.nodeId);
						
						if(_transferTaskIntervalId == null) {
							_transferTaskIntervalId = setInterval("beforeTaskferTask()", 50);
						}
					},
					error:function(data){
						modleclose();
						$.messager.alert('错误', '获取节点编码失败！', 'error');
					}
		    	});
				
			}
		}
		
		function beforeTaskferTask() {//准备转派任务
			var transferUserIds = $("#transferUserIds").val();
			
			if(isNotBlankString(transferUserIds)) {
				clearInterval(_transferTaskIntervalId);
				_transferTaskIntervalId = null;
				transferTask();
			}
		}
		
		function transferTask() {//开始转派任务
			modleopen();
			
			var selectItem = $("#list").datagrid("getSelected");
			
			$.ajax({
				type: "POST",
	    		url : '${rc.getContextPath()}/zhsq/event/eventTask/transferTask.jhtml',
				data: {
					'taskId': selectItem.CUR_TASK_ID, 
					'instanceId': selectItem.INSTANCE_ID,
					'userIds': $("#transferUserIds").val(),
					'userNames': $("#transferUserNames").val(),
					'orgIds': $("#transferCurOrgIds").val(),
					'orgNames': $("#transferCurOrgNames").val()
				},
				dataType:"json",
				success: function(data){
					modleclose();
					
					if(data.result) {
						$.messager.alert('提示', '任务转派成功！', 'info');
						conditionSearch();
					} else if(data.msg) {
						$.messager.alert('错误', data.msg, 'error');
					}
				},
				error:function(data){
					modleclose();
					$.messager.alert('错误', '任务转派失败！', 'error');
				}
	    	});
	    	
	    	//清空原有选择的人员
			$("#transferUserIds").val("");
			$("#transferUserNames").val("");
			$("#transferCurOrgIds").val("");
			$("#transferCurOrgNames").val("");
		}
	</#if>
	
	function detail(eventId, instanceId){
		if(eventId) {
		    var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&instanceId="+instanceId+"&eventId="+eventId+"&cachenum=" + Math.random();
	    	showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
		} else {
	    	$.messager.alert('警告','请选择一条事件！','warning');
		}
	}
	
	function resetCondition() {//重置
		$('#eventTaskForm')[0].reset();
		
		conditionSearch();
	}
	
	function conditionSearch(){//查询
		var searchArray = new Array();
		
		$(".queryParam").each(function() {
			var val = $(this).val();
			var key = $(this).attr("name");
			
			if(isNotBlankString(val)){
				searchArray[key] = val;
			}
		});
		
		if(isBlankString(searchArray.infoOrgCode)) {
			searchArray["infoOrgCode"] = $("#startInfoOrgCode").val();
		}
		
		if(_transferTaskIntervalId != null) {
			clearInterval(_transferTaskIntervalId);
			_transferTaskIntervalId = null;
		}
		
		doSearch(searchArray);
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			conditionSearch();
		}
	}
    
    function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');			//重新加载事件列表
	}
</script>