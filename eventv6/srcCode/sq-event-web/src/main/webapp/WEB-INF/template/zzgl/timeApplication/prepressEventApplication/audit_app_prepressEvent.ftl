<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>预处理事件申请审核页面</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.areaClass{width:82%;}
		.auditRadio{cursor: pointer; color: #7c7c7c;}
	</style>
</head>
<body>
	<form id="timeApplicationForm" name="timeApplicationForm" action="" method="post">
		<input type="hidden" name="applicationId" value="<#if timeApp.applicationId??>${timeApp.applicationId?c}</#if>" />
		<input type="hidden" name="checkId" value="<#if timeApp.checkId??>${timeApp.checkId?c}</#if>" />
		<input type="hidden" id="duplicateEventId" name="duplicateEventId" />
		
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm">
				<div id="checkTableDiv">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
				    			<label class="LabName"></label><div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditSuccess" value="1" checked/><label id="auditSuccessLabel" for="auditSuccess" class="auditRadio">审核通过</label></div>
					        </td>
					        <td class="LeftTd">
				    			<div class="Check_Radio"><input type="radio" name="timeAppCheckStatus" id="auditFail" value="2"/><label id="auditFailLabel" for="auditFail" class="auditRadio">审核不通过</label></div>
					        </td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label id="checkAdviceLabel" class="LabName"><span>审核意见：</span></label><textarea name="checkAdvice" id="checkAdvice" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[200]','characterCheck'],tipPosition:'top'" style="height:100px;"></textarea>
					        </td>
						</tr>
					</table>
				</div>
			</div>
			
			<div id="duplicateEventDiv" region="center" border="false" style="width:100%; height: 500px; overflow:hidden;">
				<table style="width:100%" id="duplicateEventList"></table>
			</div>
		</div>
		
		<div id="btnListDiv" class="BigTool">
        	<div class="BtnList">
        		<a href="###" class="BigNorToolBtn BigNorToolBtn SaveBtn" onclick="timeAppAudit();">确定</a>
        		<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();">取消</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function() {
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	        
	        $('#timeApplicationForm input[type="radio"][name="timeAppCheckStatus"]').on('click', function() {
	        	var auditStatus = $(this).val();
	        	
	        	$('#checkAdvice').validatebox({
					required: auditStatus == '2'
				});
	        });
	        
	        $('#duplicateEventDiv').height($(window).height() - $('#checkTableDiv').outerHeight(true) - $('#btnListDiv').outerHeight(true));
	        loadDuplicateEventData();
	        
	        $('#checkAdvice').width(($(window).width() - $('#checkAdviceLabel').outerWidth(true)) * 0.96);
	        $("#norFormDiv").width($(window).width());
	    });
		
		function loadDuplicateEventData() {
			$('#duplicateEventList').datagrid({
				width:600,
	            height:300,
	            nowrap: true,
	            rownumbers:true,
	            remoteSort:false,
	            striped: true,
	            fit: true,
	            fitColumns: true,
	            singleSelect: false,
	            checkOnSelect : true,
	            selectOnCheck : true,
	            idField:'eventId',
	            url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
	            columns:[[
	                {field:'eventId',title:'ID', align:'center',checkbox:true},
	                {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
	                {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.14),sortable:true, formatter: dateFormatter},
	                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
	                {field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
	                {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.1),sortable:true},
	                {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter}
	            ]],
	            toolbar : '',
	            pagination:true,
	            pageSize: 20,
	            queryParams: {'eventType': 'all', 'isMapDistanceSearch4SelfEvent': true, 'selfEventId': '<#if timeApp.businessKeyId??>${timeApp.businessKeyId?c}</#if>'},
	            onLoadError: function () {//数据加载异常
	            	listError('duplicateEventList');
	            }
			});
			
			//设置分页控件
			var p = $('#duplicateEventList').datagrid('getPager');
			$(p).pagination({
				pageSize: 20,//每页显示的记录条数，默认为
	            pageList: [20,30,40,50],//可以设置每页记录条数的列表
	            beforePageText: '第',//页数文本框前显示的汉字
	            afterPageText: '页    共 {pages} 页',
	            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
			});
		}
		
		function clickFormatter(value, rowData) {
			var title = '';
			
			if(value) {
				title = '<a href="###" onclick="parent.detail(' + rowData.eventId + ')">' + value + '</a>';
			}
			
			return title;
		}
		
		function dateFormatter(value, rowData, rowIndex) {
	        if(value && value.length >= 10) {
	            value = value.substring(0,10);
	        }
	
	        return value;
	    }
	    
		function timeAppAudit() {
			var isValid =  $("#timeApplicationForm").form('validate');
			
			if(isValid) {
				var selectedRows = $('#duplicateEventList').datagrid('getSelections'),
				      duplicateEventIdArray = [];
				      
				modleopen();
				
				for(var index in selectedRows) {
					duplicateEventIdArray.push(selectedRows[index].eventId);
				}
			
				$("#timeApplicationForm").attr("action","${rc.getContextPath()}/zhsq/timeApplication/auditTimeApplication.jhtml");
				
				$('#duplicateEventId').val(duplicateEventIdArray.toString());
				
				$("#timeApplicationForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success) {
						parent.reloadDataForSubPage(data.tipMsg, true);
					} else {
						var tipMsg = data.tipMsg || '操作失败！';
						
						$.messager.alert('错误', tipMsg, 'error');
					}
				});
				
			}
		}
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>
