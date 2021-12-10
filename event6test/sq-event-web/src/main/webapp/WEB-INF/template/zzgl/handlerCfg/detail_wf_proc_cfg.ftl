<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="x-ua-compatible" content="ie=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>环节配置信息详情</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<style type="text/css">
		.cellWidth{width: 85%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="wfpcId" name="wfpcId" value="<#if wfProcCfg.wfpcId??>${wfProcCfg.wfpcId?c}</#if>" />
		
		<div class="OpenWindow EditFunctionSetInfo">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>所属地域：</span></label><div class="Check_Radio FontDarkBlue cellWidth">${wfProcCfg.regionName!}</div>
							</td>
						</tr>
				    	<tr>
				    		<td class="LeftTd">
								<label class="LabName"><span>事件类别：</span></label><div class="Check_Radio FontDarkBlue cellWidth">${wfProcCfg.eventCodeNames!}</div>
							</td>
			    		</tr>
			    		<tr>
			    			<td class="LeftTd">
								<label class="LabName"><span>流程环节：</span></label><div class="Check_Radio FontDarkBlue cellWidth">${wfProcCfg.taskCodeName!}</div>
							</td>
			    		</tr>
				    	<tr>
				    		<td class="LeftTd">
								<label class="LabName"><span>人员信息：</span></label><div id="transactorsDiv" class="Check_Radio FontDarkBlue cellWidth"></div>
							</td>
				    	</tr>
				    </table>
				</div>
			</div>
		    <div class="clear"></div>
			<div class="BigTool">
	        	<div class="BtnList">
			    	<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
	            </div>
	        </div>	
		</div>
	</form>
	
</body>

<script type="text/javascript">
	$(function() {
		$(window).load(function() { 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	    }); 
	    
        fetchCfgActor();
	});
	
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
					var transactors = "";
					
					if(data && data.length) {
						for(var index in data) {
							transactors += "," + data[index].orgName + "-" + data[index].userName;
						}
						
						if(transactors) {
							transactors = transactors.substr(1);
						}
					}
					
					$('#transactorsDiv').html(transactors);
				},
				error:function(data){
					$.messager.alert('错误','环节人员配置获取失败！','error');
				}
			});
		}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</html>