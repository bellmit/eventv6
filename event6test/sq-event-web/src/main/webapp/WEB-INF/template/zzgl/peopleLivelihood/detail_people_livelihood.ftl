<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>民生信息详情</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/bigFileUpload.ftl" />
<style>
i.spot-xh {
    display: inline-block;
    color: #f54952;
    padding-right: 5px;
    /* vertical-align: middle; */
    font-size: 12px;
    font-weight: 100;
    text-align: right;
    vertical-align:top;
}

.area1 {
    width: 78%;
    height: 42px;
}

.LabName {
    width: 120px;
}

.bigFile-upload-box{
	margin-top:5px;
}

</style>

</head>
<body>
	 <form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/peopleLivelihood/saveOrUpdateInfo.jhtml" method="post">
	       <input type="hidden" name="infoId" id="infoId" value="${infoId!''}" />
	       <input type="hidden" name="infoOrgCode" id="infoOrgCode" value="${infoOrgCode!''}" />
	       
	       <div id="content-d" class="MC_con content light" style="overflow-x:hidden; position:relative;">
    		<div class="NorForm NorForm1">
    			</br>
    			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			     	<tr>
		                <td class="LeftTd" colspan="2">
		                    <label class="LabName"><span><i class="spot-xh">*</i>所属辖区:</span></label>
		                    <div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if gridPath??>${gridPath}</#if></div>
		                </td>
		            </tr>
			     	<tr>
		                <td class="LeftTd" style="width:50%">
		                    <label class="LabName"><span><i class="spot-xh">*</i>信息状态:</span></label>
		                    <div class="Check_Radio FontDarkBlue"><#if statusName??>${statusName}</#if></div>
		                </td>
		                <td>
		                    <label class="LabName"><span><i class="spot-xh">*</i>紧急程度:</span></label>
		                    <div class="Check_Radio FontDarkBlue"><#if urgenceDegreeName??>${urgenceDegreeName}</#if></div>
		                </td>
		            </tr>
			     	<tr>
		                <td class="LeftTd">
		                    <label class="LabName"><span><i class="spot-xh">*</i>民生信息类型:</span></label>
		                    <div class="Check_Radio FontDarkBlue"><#if infoTypeName??>${infoTypeName}</#if></div>
		                </td>
		                <td>
		                    <label class="LabName"><span><i class="spot-xh">*</i>民生动态类型:</span></label>
		                    <div class="Check_Radio FontDarkBlue"><#if infoTrendsTypeName??>${infoTrendsTypeName}</#if></div>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>信息标题:</span></label>
		                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if infoTitle??>${infoTitle}</#if></div>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>发生地点:</span></label>
		                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if occurred??>${occurred}</#if></div>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>信息内容:</span></label>
		                	<div class="Check_Radio FontDarkBlue" style="width: 80%;"><#if infoContent??>${infoContent}</#if></div>
		                </td>
		            </tr>
		            <tr id="adviceTr" class="hide">
			    		<td class="LeftTd" colspan="2">
			    			<label class="LabName"><span><i class="spot-xh">*</i>办理意见:</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
			    		</td>
			    	</tr>
			    	<tr id="adviceTr" class="hide">
			    		<td class="LeftTd" colspan="2">
			    			<label class="LabName"><span><i class="spot-xh">*</i>办理意见:</span></label><textarea rows="3" style="height:80px;" id="advice" name="advice" class="area1 easyui-validatebox fast-reply" data-options="tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']">${advice!}</textarea>
			    		</td>
			    	</tr>
		            <tr>
			    		<td colspan="2" class="LeftTd">
			    			<label class="LabName"><span>相关附件:</span></label><div id="bigFileUploadDiv"></div>
			    		</td>
			    	</tr>
    			</table>
    		</div>
			</div>
			<div class="BigTool">
        		<div class="BtnList" style="width:230px">
        			<#if status??>
        				<#if status=='99'>
				        	<a href="###" onclick="saveAndSubmit();" class="BigNorToolBtn BigShangBaoBtn">提交</a>
				        	<a href="###" onclick="saveAndSubmit(1);" class="BigNorToolBtn BigShangBaoBtn">结案</a>
			        	</#if>
		        	</#if>
		        	<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">返回</a>
            	</div>
        	</div>	
		</form>
		
		
</body>

<script type="text/javascript">

	$(function(){
		<#if status??>
			<#if status=='99'>
	        	$('.BtnList').css("width","340px");
        	<#else>
        		$('.BtnList').css("width","160px");
        	</#if>
    	</#if>
        $('.numberbox').css({"height":"30px","margin-top":"-5px","width":"60px"});
        $('.textbox-text').css({"font-size":"14px","padding-top":"4px"});
        $(".textbox-text").select();
        $(".textbox-text").bind('focus',function(){
            $(this).select();
        })
        
        var bigFileUploadOpt = {
			useType: 'view',
			attachmentData: {bizId:'<#if infoSeqId??>${infoSeqId}</#if>',attachmentType:"PEOPLE_LIVE"},
			module: 'attachment',
			appcode: 'sqfile',
			fileNumLimit: 9,
			isUseLabel: false
		};
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		
	});
	
	
	function saveAndSubmit(isClose){
		
		if(isClose && isClose == '1') {
			$('#advice').validatebox({
				required: true
			});
			
			$("#adviceTr").show();
			
		} else {
			$("#adviceTr").hide();
			
			$('#advice').validatebox({
				required: false
			});
			
		}
		
		var isValid =  $("#tableForm").form('validate');
		
		if(isValid){
		
			modleopen();
			var postParams={};
			postParams.infoId="${infoId!}";
			if(isClose){
				postParams.isClose=isClose;
				postParams.advice=$('#advice').val();
			}
				
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/peopleLivelihood/startWorkflow.json',
				data: postParams,
				dataType:"json",
				success: function(data) {
					modleclose();
					
					if(data.code==1){
	  					$.messager.alert('提示', '提交成功!', 'info');
	  					parent.addCallBack("${infoId!}",data.instanceId);
	  				} else {
	  					if(data.msg) {
	  						$.messager.alert('提示', '提交失败!'+data.msg, 'info');
	  					} else {
	  						$.messager.alert('提示', '提交失败！', 'info');
	  					}
	  				}
				},
				error:function(data){
					$.messager.alert('错误','连接超时！','error');
					modleclose();
				}
			});
		}
	}


	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	


</script>
</html>