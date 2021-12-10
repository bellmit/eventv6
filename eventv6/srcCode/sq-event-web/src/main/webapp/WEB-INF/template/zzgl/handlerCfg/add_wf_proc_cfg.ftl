<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>新增/编辑环节配置信息</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<style type="text/css">
		.inpCombox{width:200px;}
		.cellWidth{width: 85%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="wfcId" name="wfcId" value="<#if wfcId??>${wfcId?c}</#if>" />
		<input type="hidden" id="wfpcId" name="wfpcId" value="<#if wfProcCfg.wfpcId??>${wfProcCfg.wfpcId?c}</#if>" />
		
		<div class="OpenWindow EditFunctionSetInfo">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    	<tr>
				    		<td class="LeftTd" width="320px">
				    			<label class="LabName"><span>所属地域：</span></label>
				    			<input type="hidden" id="regionCode" name="regionCode" value="${wfProcCfg.regionCode!}" />
				    			<input type="text" class="inp1 inpCombox easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="regionName" value="${wfProcCfg.regionName!}" />
				    		</td>
				    	</tr>
				    	<tr>
					    	<td class="LeftTd">
				    			<label class="LabName"><span>事件类别：</span></label>
				    			<input type="hidden" id="eventCodes" name="eventCodes" value="${wfProcCfg.eventCodes!}" />
				    			<input type="text" class="inp1 inpCombox easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="eventCodesName" />
				    		</td>
			    		</tr>
			    		<tr>
					    	<td class="LeftTd">
				    			<label class="LabName"><span>流程环节：</span></label>
				    			<input type="hidden" id="taskCode" name="taskCode" value="${wfProcCfg.taskCode!}" />
				    			<input type="text" class="inp1 inpCombox easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="taskCodeName" />
				    		</td>
			    		</tr>
				    	<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>人员信息：</span></label>
				    			<div class="Check_Radio cellWidth">
				    				<input type="hidden" id="curUserIds" name="transactorIds" value="${wfProcCfg.transactorIds!}"/>
									<input type="hidden" id="curOrgIds" name="transactorOrgIds" value="${wfProcCfg.transactorOrgIds!}"/>
									<input type="hidden" name="transactorType" value="0" />
									<input type="hidden" id="curOrgNames" />
									
							    	<a href="###" class="NorToolBtn EditBtn fl" onclick="_selectUserAll();">选择人员信息</a>
							    	<div class="FontDarkBlue fl DealMan"><b id="curUserNames"></b></div>
								</div>
				    		</td>
				    	</tr>
				    </table>
				</div>
			</div>
		    <div class="clear"></div>
			<div class="BigTool">
	        	<div class="BtnList">
			    	<a href="#" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
			    	<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
	            </div>
	        </div>	
		</div>
	</form>
	
	<#include "/zzgl/event/workflow/select_user.ftl" />
	<#include "/component/ComboBox.ftl" />
</body>

<script type="text/javascript">
	var g_EventNodeCode = {};	//人员选择窗口需要使用
	
	$(function() {
		$(window).load(function() { 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	    }); 
	    
	    AnoleApi.initGridZtreeComboBox("regionName", "regionCode", function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				$("#regionCode").val(grid.orgCode);
			} 
		}, {
            Async : {
				enable : true,
				autoParam : [ "id=gridId" ],
				dataFilter : _filter,
				otherParam : {
					rootName: "行政区划",
					startGridId : "<#if startGridId??>${startGridId?c}</#if>"
				}
			}
        });
        
        var eventCodeArray = null,
        	eventCodes = $("#eventCodes").val();
        
        if(eventCodes) {
        	eventCodeArray = eventCodes.split(",");
        }
        
        AnoleApi.initTreeComboBox("eventCodesName", "eventCodes", "${eventTypePcode!}", null, eventCodeArray, {
        	RenderType: "01",
        	ShowOptions:{
				ChkboxType : {
					"Y": "s", 
					"N": "ps" 
				}
			}
        });
        
        fetchTaskCodes();
        fetchCfgActor();
	});
	
	function tableSubmit(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/saveWfProcCfg.jhtml");
	      	
	      	modleopen();
		  	
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		modleclose();
		  		
  				if(data.success && data.success == true){
  					var isCurrentPage = $("#wfpcId").val() != "";
  					
  					parent.flashData(data.tipMsg, isCurrentPage);
  				} else {
  					$.messager.alert('错误', data.tipMsg, 'error');
  				}
			});
	  	}
	}
	
	//获取环节编码
	function fetchTaskCodes() {
		var wfcId = $("#wfcId").val();
		
		if(wfcId) {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/fetchTaskCodes.jhtml',
				data: 'wfcId='+ wfcId,
				dataType:"json",
				success: function(data){
					if(data && data.length) {
						var taskCodeArray = [];
						
						for(var index in data) {
							var taskCodeObj = {};
							
							taskCodeObj.name = data[index].taskCodeName;
							taskCodeObj.value = data[index].taskCode;
							
							taskCodeArray.push(taskCodeObj);
						}
						
						AnoleApi.initListComboBox("taskCodeName", "taskCode", null, null, ["${wfProcCfg.taskCode!}"], {
				        	DataSrc: taskCodeArray
				        });
					}
				},
				error:function(data){
					$.messager.alert('错误','流程环节获取失败！','error');
				}
			});
		}
	}
	
	//获取人员配置信息
	function fetchCfgActor() {
		var wfpcId = $("#wfpcId").val();
		
		if(wfpcId) {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/fetchCfgActor.jhtml',
				data: {'wfpcId': wfpcId},
				dataType:"json",
				success: function(data){
					var userIds = "", userNames = "",
						orgIds = "", orgIdNames = "";
					
					if(data && data.length) {
						for(var index in data) {
							userIds += "," + data[index].userId;
							userNames += "," + data[index].userName;
							orgIds += "," + data[index].orgId;
							orgIdNames += "," + data[index].orgName;
						}
						
						userIds = userIds.substr(1);
						userNames = userNames.substr(1);
						orgIds = orgIds.substr(1);
						orgIdNames = orgIdNames.substr(1);
					}
					
					$('#curUserIds').val(userIds);
					$('#curUserNames').html(userNames);
					$('#curOrgIds').val(orgIds);
					$('#curOrgNames').val(orgIdNames);
				},
				error:function(data){
					$.messager.alert('错误','已配置流程环节获取失败！','error');
				}
			});
		}
	}
	
	//点击办理人员选择
	function _selectUserAll() {
		g_EventNodeCode.nodeCode = "${nodeCode!}";
		
		selectUser('curUserIds', 'curUserNames', 'curOrgIds', 'curOrgNames', 'task', '0');<!--思明区需要使用nodeId参数-->
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</html>