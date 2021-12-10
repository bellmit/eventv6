<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增/编辑业务配置信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
	.inp335px{width:200px;}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" id="wfcId" name="wfcId" value="<#if handlerWfCfg.wfcId??>${handlerWfCfg.wfcId?c}</#if>" />
		
		<div class="OpenWindow EditFunctionSetInfo">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
				    	<tr>
				    		<td class="LeftTd" width="320px">
				    			<label class="LabName"><span>所属地域：</span></label>
				    			<input type="hidden" id="regionCode" name="regionCode" value="${handlerWfCfg.regionCode!}" />
				    			<input type="text" class="inp1 inp335px easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="regionName" value="${handlerWfCfg.regionName!}" />
				    		</td>
				    		<td class="RightTd" rowspan="3" style="line-height:24px;">
				    			<p><b>使用说明：</b></p>
								<p><b>配置信息：</b>需要先选择相应的业务类型，才能获取相关配置信息选项。</p>
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>业务类型：</span></label>
				    			<input type="hidden" id="bizType" name="bizType" value="${handlerWfCfg.bizType!}" />
				    			<input type="text" class="inp1 inp335px easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="bizTypeName" />
				    		</td>
				    	</tr>
				    	<tr>
				    		<td class="LeftTd">
					    		<label class="LabName"><span>配置信息：</span></label>
					    		<input type="hidden" id="wfCfgId" name="wfCfgId" value="<#if handlerWfCfg.wfCfgId??>${handlerWfCfg.wfCfgId?c}</#if>" />
					    		<input type="text" class="inp1 inp335px easyui-validatebox" data-options="required:true,tipPosition:'bottom'" id="wfCfgIdName" value="" />
	                		</td>
				    	</tr>
				    </table>
				</div>
			</div>
		    <div class="clear"></div>
			<div class="BigTool">
	        	<div class="BtnList">
			    	<a href="#" onclick="tableSubmit('saveConfigure');" class="BigNorToolBtn SaveBtn">保存</a>
			    	<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
	            </div>
	        </div>	
		</div>
	</form>
	<#include "/component/ComboBox.ftl" />
</body>

<script type="text/javascript">
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
			} 
		}, {
            rootName: "行政区划"
        });
        
        <#if bizTypeMapList?? && (bizTypeMapList?size > 0)>
        	var bizTypeArray = [];
        	
        	<#list bizTypeMapList as list>
        		var bizTypeObj = {};
        		bizTypeObj.name = "${list.name!}";
        		bizTypeObj.value = "${list.value!}";
        		bizTypeArray.push(bizTypeObj);
        	</#list>
        	
        	AnoleApi.initListComboBox("bizTypeName", "bizType", null, bizTypeCallback, ["${handlerWfCfg.bizType!}"], {
	        	DataSrc: bizTypeArray
	        });
	        
	        AnoleApi.initListComboBox("wfCfgIdName", "wfCfgId", null, null, null, {
	        	DataSrc: []
	        });
	        
        </#if>
	});
	
	function tableSubmit(m){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/saveWfCfg.jhtml");
	      	
	      	modleopen();
		  	
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		modleclose();
		  		
  				if(data.success && data.success == true){
  					var isCurrentPage = $("#wfcId").val() != "";
  					
  					parent.flashData(data.tipMsg, isCurrentPage);
  				} else {
  					$.messager.alert('错误', data.tipMsg, 'error');
  				}
			});
	  	}
	}
	
	function bizTypeCallback() {
		var bizType = $("#bizType").val();
		
		if(bizType) {
			modleopen();
			
			$.ajax({
				type: "POST",
				url : '${rc.getContextPath()}/zhsq/event/eventHandlerWfCfg/fetchWfCfgList.jhtml',
				data: 'bizType='+ bizType,
				dataType:"json",
				success: function(data){
					var wfCfgArray = [],
						selectedItem = [],
						selectedWfCfgId = "";
					
					modleclose();
					
					if(data && data.length) {
						for(var index in data) {
							var wfCfgObj = {};
							wfCfgObj.name = data[index].name;
							wfCfgObj.value = data[index].value;
							wfCfgArray.push(wfCfgObj);
						}
						
						selectedWfCfgId = $("#wfCfgId").val();
					}
					
					selectedItem.push(selectedWfCfgId);
					
					AnoleApi.initListComboBox("wfCfgIdName", "wfCfgId", null, null, selectedItem, {
			        	DataSrc: wfCfgArray
			        });
			        
				},
				error:function(data){
					modleclose();
					$.messager.alert('错误','业务数据获取失败！','error');
				}
			});
		}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
</script>
</html>