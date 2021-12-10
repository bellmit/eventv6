<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>新增/编辑业务配置信息</title>
	<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/js/fastreply/fastReply.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	
	<style type="text/css">
		.inp335px{width:200px;}
		.summary{border-bottom:1px solid #d8d8d8; height:56px;}
		.summary ul li{float:left; font-size:12px; line-height:28px; padding:0 10px; margin-right:5px;}
		.DealMan{width:300px; line-height:24px;}
		.taskCodeSelected{border:2px solid #80c269;}
		/*
		.taskCodeSelected, .taskCodeSelected:hover{border:2px solid #80c269; background:url(${uiDomain}/images/map/gisv0/special_config/images/gou.png) no-repeat right bottom;}
		*/
	</style>
</head>
<body>
	<div id="summaryDiv">
		<div class="summary">
			<div style="padding: 10px 10px 0 10px;"><b>业务配置基本信息</b></div>
			<ul>
		    	<li>所属地域：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.regionName??>${handlerWfCfg.regionName}</#if></span></li>
		    	<li>业务类型：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.bizTypeName??>${handlerWfCfg.bizTypeName}</#if></span></li>
		    	<li>配置名称：<span class="FontDarkBlue"><#if handlerWfCfg?? && handlerWfCfg.wfCfgName??>${handlerWfCfg.wfCfgName}</#if></span></li>
		    </ul>
		</div>
	</div>
	
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="wfcId" name="wfcId" value="<#if handlerWfCfg.wfcId??>${handlerWfCfg.wfcId?c}</#if>" />
		
		<div class="OpenWindow EditFunctionSetInfo">
			<div id="fixedParamDiv" style="border-bottom:1px dotted #d8d8d8; padding-top: 5px; padding-bottom: 5px;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
			    	<tr>
			    		<td class="LeftTd" width="320px">
			    			<label class="LabName"><span>所属地域：</span></label>
			    			<input type="hidden" id="regionCode" name="regionCode" value="${handlerWfCfg.regionCode!}" />
			    			<input type="text" class="inp1 inp335px easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="regionName" value="" />
			    		</td>
			    		<td class="LeftTd">
			    			<label class="LabName"><span>事件类别：</span></label>
			    			<input type="hidden" id="eventCodes" value="" />
			    			<input type="text" class="inp1 inp335px easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="eventCodesName" />
			    		</td>
			    	</tr>
			    </table>
		    </div>
		    
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    	<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>环节信息：</span></label>
				    			<div>已配置：<span class="chengse">&nbsp;&nbsp;</span>&nbsp;&nbsp;未配置：<span class="qingse">&nbsp;&nbsp;</span></div>
				    			<div class="FastReply" style="height: 200px;">
					    			<div class="PersonUsed">
						    			<div id="taskCodesDiv" class="list fr" style="width: 400px; position: relative;">
						    			</div>
					    			</div>
				    			</div>
				    		</td>
				    		<td class="RightTd" rowspan="2" style="line-height:20px;">
				    			<p><b>使用说明：</b></p>
								<p><b>所属地域：</b><span class="FontDarkBlue">以业务配置所属地域为根节点的行政区划。</span></p>
								<p><b>环节信息：</b><span class="FontDarkBlue">与业务配置中业务类型和配置名称对应的环节信息，如业务类型为工作流时，对应的是流程图环节信息。</span></p>
								<p><b>人员信息：</b><span class="FontDarkBlue">选中环节信息对应的人员配置。</span></p>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>人员信息：</span></label>
				    			<div class="Check_Radio">
				    				<input type="hidden" id="curUserIds" name="transactorIds" />
									<input type="hidden" id="curOrgIds" name="transactorOrgIds" />
									<input type="hidden" name="transactorType" value="0" />
									<input type="hidden" id="curOrgNames" />
									
							    	<a href="###" class="NorToolBtn EditBtn fl" onclick="_selectUserAll();">选择环节人员</a>
							    	<div class="FontDarkBlue fl DealMan"><b id="curUserNames"></b></div>
								</div>
				    		</td>
				    	</tr>
				    </table>
				</div>
			</div>
		    <div class="clear"></div>
			<div id="btnDiv" class="BigTool">
	        	<div class="BtnList">
			    	<a href="#" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">保存</a>
			    	<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
	            </div>
	        </div>	
		</div>
	</form>
	
	<#include "/zzgl/event/workflow/select_user.ftl" />
	<#include "/component/ComboBox.ftl" />
</body>

<script type="text/javascript">
	var g_EventNodeCode = {},	//人员选择窗口需要使用
		eventCodeCombox = null;
	
	$(function(){
		$(window).load(function(){ 
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
				
				regionCallback();
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
        
        eventCodeCombox = AnoleApi.initTreeComboBox("eventCodesName", "eventCodes", "${eventTypePcode!}", regionCallback, null, {
        	RenderType: "01",
        	ShowOptions:{
				ChkboxType : {
					"Y": "s", 
					"N": "ps" 
				}
			}
        });
        
        $("#content-d").height($(window).height() - $("#btnDiv").outerHeight(true) - $("#summaryDiv").outerHeight(true) - $("#fixedParamDiv").outerHeight(true));
        
        fetchTaskCodes();
	});
	
	//点击保存，保存记录
	function tableSubmit(){
		var selectedTask = $("#taskCodesDiv > span.taskCodeSelected");
		
		if(selectedTask.length == 0) {
			$.messager.alert('警告', '请选择环节信息！', 'warning');
		} else {
			var taskCode = selectedTask.eq(0).attr("id"),
				eventCodes = "",
				eventCodeSpan = $("#eventCodesDiv > span"),
				selectedEventCode = eventCodeSpan.filter(".taskCodeSelected"),
				wfpcId = selectedTask.children("em").eq(0).attr("wfpcId"),
				url = "${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/saveWfProcCfg.jhtml";
			
			url += "?taskCode=" + taskCode; 
			
			eventCodeSpan.each(function() {
				eventCodes += "," + $(this).attr('id');
			});
			
			if(selectedEventCode.length == 0) {
				eventCodes += "," + $("#eventCodes").val();
			}
			
			url += "&eventCodes=" + eventCodes.substr(1);
			
			if(wfpcId) {
				url += "&wfpcId=" + wfpcId;
			}
			
			$("#tableForm").attr("action", url);
	      	
	      	modleopen();
		  	
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		modleclose();
		  		
  				if(data.success && data.success == true){
  					regionCallback();
  					$.messager.alert('提示', data.tipMsg, 'info');
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
						var taskCodeSpan = "",
							taskCode = "";
						
						for(var index in data) {
							taskCode = data[index].taskCode;
							
							taskCodeSpan += '<span id="'+ taskCode +'" class="qingse" >';
							taskCodeSpan += '<a href="###" onclick="selectTaskCode(\''+ taskCode +'\')">';
							taskCodeSpan += data[index].taskCodeName;
							taskCodeSpan += '</a>';
							taskCodeSpan += '</span>';
						}
						
						$("#taskCodesDiv").html(taskCodeSpan);
					}
				},
				error:function(data){
					$.messager.alert('错误','流程环节获取失败！','error');
				}
			});
		}
	}
	
	//所属地域选择后，回调方法
	function regionCallback() {//获取已有配置的流程环节
		var wfcId = $("#wfcId").val(),
			regionCode = $("#regionCode").val(),
			eventCodes = $("#eventCodes").val();
		
		if(regionCode && eventCodes) {
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/fetchCfgTaskCodes.jhtml',
				data: {'wfcId': wfcId, 'regionCode': regionCode, 'eventCodes': eventCodes, 'searchType': 'eq'},
				dataType:"json",
				success: function(data){
					$("#taskCodesDiv > span").removeClass("chengse");
					$("#taskCodesDiv > span > em").remove();
					
					if(data && data.length) {
						var taskCodeSpan = "",
							taskCodeObj = null,
							taskCodeEm = null,
							taskWfpcId = "",
							taskEventCodes = "";
						
						for(var index in data) {
							taskCodeObj = $("#" + data[index].taskCode);
							taskWfpcId = data[index].wfpcId;
							taskEventCodes = data[index].eventCodes;
							
							if(!taskCodeObj.hasClass('chengse')) {
								taskCodeObj.addClass('chengse');
								
								$("<em wfpcId='"+ taskWfpcId +"' eventCodes='"+ taskEventCodes +"' onclick=\"delProcCfg('" + data[index].wfpcId +"', '"+ data[index].taskCode +"')\"></em>").appendTo(taskCodeObj);
							}
						}
					}
				},
				error:function(data){
					$.messager.alert('错误','已配置流程环节获取失败！','error');
				}
			});
		} else {
			$("#taskCodesDiv > span").removeClass("chengse");
			$("#taskCodesDiv > span > em").remove();
		}
		
		unSelectTaskCode();
	}
	
	//流程环节选择后，回调方法
	function selectTaskCode(taskCode) {//用于获取已配置的人员
		var isValid =  $("#tableForm").form('validate');
		
		if(isValid) {
			var wfcId = $("#wfcId").val(),
				regionCode = $("#regionCode").val();
			
			if(regionCode && taskCode) {
				selectedItem("taskCodesDiv", taskCode);
				
				var taskCodeEm = $("#" + taskCode + " > em"),
					eventCodes = $("#eventCodes").val(),
					wfpcId = "";
				
				if(taskCodeEm && taskCodeEm.length) {
					var eventCodeArray = null,
						taskEventCodes = taskCodeEm.eq(0).attr('eventCodes');
					
					wfpcId = taskCodeEm.eq(0).attr('wfpcId');
					
					if(eventCodes) {
						if(taskEventCodes) {
							eventCodes += "," + $("#eventCodes").val();
						}
					} else {
						eventCodes = taskEventCodes;
					}
					
					if(eventCodes) {
						eventCodeArray = eventCodes.split(",");
						
						$.unique(eventCodeArray);//去重
						
						//重新初始化，为了达到下拉选中的效果
						/*eventCodeCombox = AnoleApi.initTreeComboBox("eventCodesName", "eventCodes", "A001093199", regionCallback, eventCodeArray, {
				        	RenderType: "01",
				        	ShowOptions:{
								EnableToolbar : true
							}
				        });*/
					}
				}
				
				if(!eventCodes) {//清空数值
					eventCodeCombox.doClearing();
				}
				
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
							$.messager.alert('错误','流程环节人员配置获取失败！','error');
						}
					});
				}
			}
		}
	}
	
	//清除环节配置选中效果
	function unSelectTaskCode() {
		selectedItem('taskCodesDiv');
		
		$('#curUserIds').val('');
		$('#curUserNames').html('');
		$('#curOrgIds').val('');
		$('#curOrgNames').val('');
	}
	
	//环节编码 设置选中效果
	function selectedItem(divId, code) {
		var spanEm = null,
			marginLeft = 0;
		
		$("#"+ divId +" > span.taskCodeSelected").each(function() {
			spanEm = $(this).children('em');
			
			//$(this).width($(this).width() + 4);
			$(this).height($(this).height() + 4);
			$(this).removeClass('taskCodeSelected');
			//$(this).children('a').css('color', '#fff');
			
			if(spanEm.length) {
				marginLeft = parseInt(spanEm.eq(0).css('margin-left')) + 4;
				spanEm.eq(0).css('margin-left', marginLeft + 'px');
			}
		});
		
		if(code) {
			//$("#" + code).width($("#" + code).width() - 4);
			$("#" + code).height($("#" + code).height() - 4);
			
			$("#" + code).addClass('taskCodeSelected');
			//$("#" + code + " > a").css('color', '#000');
			
			spanEm = $("#" + code + " > em");
			
			if(spanEm.length) {
				marginLeft = parseInt(spanEm.eq(0).css('margin-left')) - 4;
				spanEm.eq(0).css('margin-left', marginLeft + 'px');
			}
		}
	}
	
	//删除环节配置
	function delProcCfg(wfpcId, taskCode) {
		$.messager.confirm('提示', '您确定删除选中的环节编码配置吗？', function(r) {
			if (r){
				modleopen();
				
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/event/eventHandlerWfProcCfg/delProcCfg.jhtml',
					data: 'wfpcId='+wfpcId,
					dataType:"json",
					success: function(data) {
						modleclose();
						
						if(data.success && data.success == true){
		  					$.messager.alert('提示', data.tipMsg, 'info');
		  					
		  					regionCallback();
		  				} else {
		  					$.messager.alert('错误', data.tipMsg, 'error');
		  				}
					},
					error:function(data){
						modleclose();
						$.messager.alert('错误','环节配置删除连接超时！','error');
					}
				});
			}
		});
	}
	
	//点击办理人员选择
	function _selectUserAll() {
		var selectedTask = $("#taskCodesDiv > span.taskCodeSelected");
		
		if(selectedTask.length == 0) {
			$.messager.alert('警告', '请选择环节信息！', 'warning');
		} else {
			g_EventNodeCode.nodeCode = "${nodeCode!}";
			
			selectUser('curUserIds', 'curUserNames', 'curOrgIds', 'curOrgNames', 'task', '0');<!--思明区需要使用nodeId参数-->
		}
	}
	
	//点击关闭按钮
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</html>