<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑民生信息</title>
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
		                <td class="LeftTd" style="width:50%">
		                    <label class="LabName"><span><i class="spot-xh">*</i>所属辖区:</span></label>
		                    <input name="gridId" id="gridId" type="hidden" class="queryParam" value="${gridId!}"/>
		                    <input name="gridName" id="gridName" type="text" class="inp1 InpDisable easyui-validatebox"  value="${gridName!''}"  data-options="required:true,tipPosition:'bottom'"/>
		                </td>
		                <td>
		                    <label class="LabName"><span><i class="spot-xh">*</i>紧急程度:</span></label>
                            <input type="text" id="urgenceDegree" name="urgenceDegree" class="hide queryParam easyui-validatebox" value="${urgenceDegree!}"/>
                            <input type="text" id="urgenceDegreeName" class="inp1 selectWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
		                </td>
		            </tr>
			     	<tr>
		                <td class="LeftTd">
		                    <label class="LabName"><span><i class="spot-xh">*</i>民生信息类型:</span></label>
                            <input type="text" id="infoType" name="infoType" class="hide queryParam" value="${infoType!}"/>
                            <input type="text" id="infoTypeName" class="inp1 selectWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
		                </td>
		                <td>
		                    <label class="LabName"><span><i class="spot-xh">*</i>民生动态类型:</span></label>
                            <input type="text" id="infoTrendsType" name="infoTrendsType" class="hide queryParam easyui-validatebox" value="${infoTrendsType!}"/>
                            <input type="text" id="infoTrendsTypeName" class="inp1 selectWidth easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>信息标题:</span></label>
                			<textarea name="infoTitle" id="infoTitle" cols="50" rows="2" class="area1 easyui-validatebox" data-options="validType:'length[0,100]',required:true,tipPosition:'bottom'" >${infoTitle!''}</textarea>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>发生地点:</span></label>
                			<textarea name="occurred" id="occurred" cols="50" rows="2" class="area1 easyui-validatebox" data-options="validType:'length[0,100]',required:true,tipPosition:'bottom'" >${occurred!''}</textarea>
		                </td>
		            </tr>
		            <tr>
		                <td class="LeftTd" colspan="2">
		                	<label class="LabName"><span><i class="spot-xh">*</i>信息内容:</span></label>
                			<textarea name="infoContent" id="infoContent" cols="50" rows="2" class="area1 easyui-validatebox" data-options="validType:'length[0,200]',required:true,tipPosition:'bottom'" >${infoContent!''}</textarea>
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
		        	<a href="###" onclick="tableSubmit();" class="BigNorToolBtn BigShangBaoBtn">保存</a>
		        	<a href="###" onclick="tableSubmit(-1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
		        	<a href="###" onclick="tableSubmit(1);" class="BigNorToolBtn BigShangBaoBtn">结案</a>
		        	<a href="###" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            	</div>
        	</div>	
		</form>
		
		
</body>

<script type="text/javascript">

	$(function(){
	
        $('.BtnList').css("width","420px");
        $('.numberbox').css({"height":"30px","margin-top":"-5px","width":"60px"});
        $('.textbox-text').css({"font-size":"14px","padding-top":"4px"});
        $(".textbox-text").select();
        $(".textbox-text").bind('focus',function(){
            $(this).select();
        })
        
        
        //初始化网格树控件
        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(isNotBlankParam(items) && items.length > 0) {
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
				$("#gridId").val(grid.gridId);
			}
		});
		
		AnoleApi.initTreeComboBox("infoTypeName", "infoType", "A001135001", null, ['${infoType!}'], {
        	ChooseType : "1",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
		
		AnoleApi.initTreeComboBox("infoTrendsTypeName", "infoTrendsType", "A001135002", null, ['${infoTrendsType!}'], {
        	ChooseType : "1",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
		
		AnoleApi.initTreeComboBox("urgenceDegreeName", "urgenceDegree", "A001135003", null, ['${urgenceDegree!}'], {
        	ChooseType : "1",
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
        
        var bigFileUploadOpt = {
			useType: 'edit',
			attachmentData: {bizId:'<#if infoSeqId??>${infoSeqId}</#if>',attachmentType:"PEOPLE_LIVE"},
			module: 'attachment',
			appcode: 'sqfile',
			fileNumLimit: 9,
			isUseLabel: false
		};
        
        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		
	});
	
	function tableSubmit(isClose){
	
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

			$("#tableForm").ajaxSubmit(function(data) {
				if(data.result){
				 	modleclose();
				 	
				 	if(isClose!=null){
				 		modleopen();
				 		var postParams={};
				 		var backInfoId=data.result.operationResult;
						postParams.infoId=backInfoId;
						if(isClose=='1'){
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
				  					parent.addCallBack(backInfoId,data.instanceId);
				  				} else {
				  					if(data.msg) {
				  						$.messager.alert('提示', '提交失败!'+data.msg, 'info');
				  					} else {
				  						$.messager.alert('提示', '提交失败！', 'info');
				  					}
				  					modleclose();
				  				}
							},
							error:function(data){
								$.messager.alert('错误','连接超时！','error');
								modleclose();
							}
						});
				 	}else{
					 	if($('#infoId').val()){
							parent.reloadDataForSubPage("修改民生信息成功！",true);
					 	}else{
							parent.reloadDataForSubPage("新增民生信息成功！",true);
					 	}
				 	}
				 	
				}else{
					if($('#infoId').val()){
						$.messager.alert('提示','修改民生信息失败！','info');
				 	}else{
						$.messager.alert('提示','新增民生信息失败！','info');
				 	}
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