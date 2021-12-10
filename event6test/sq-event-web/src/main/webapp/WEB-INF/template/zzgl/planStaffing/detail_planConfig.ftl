<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/normal-custom.css" />	
<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/add.css" />
<style type="text/css">
.LabName{
  text-align: left;
}
</style>
</head>
<body>
	<div id="content-d" class="MC_con content light NorForm"  style="padding-right: 10px;padding-left: 10px">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="LeftTd">
						<label class="LabName"><span><font style="color: red">*</font>预案类型:</span></label>
						<span class="Check_Radio FontDarkBlue">${(emergencyPlan.planTypeName)!}</span>
					</td>
			</tr>
			<tr>
					<td class="LeftTd">
						<label class="LabName"><span>预案内容:</span></label>
						<span class="Check_Radio FontDarkBlue" style="width: 80%">${(emergencyPlan.planContent)!}</span>
					</td>
			</tr>
				<tr>
					<td>
						<label class="LabName"><span><font style="color: red">*</font>预案等级:</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.planLevelName)!}</span>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
$(function(){
	initTabs();
	
});
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
	
	
	 function initTabs(){
			
		 $.ajax({
				type: 'POST',
				url: '${SQ_UAM_URL}/system/uam/baseDictionary/baseDictionaryController/getDataDictTreeForJsonp.json?dictPcode=${EMERGENC_PLAN_ROLE}&jsoncallback=?',
				//data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					var str='';
					if(data.length>0){
						 for (var i = 0; i < data.length; i++) {
							 str='';
							 str+='<div id="con1" class="title FontDarkBlue pos-rela" >'+data[i].name+'<div class="title-r-btn"></div></div>';
							 str+='<div class="table-normal"><table id="list_'+data[i].value+'"  class="list" width="100%" border="0" cellspacing="0" cellpadding="0"><thead><tr>';
							 str+='<th style="width:300px"><font color="red">*</font>姓名</th>';
							 str+='<th style="width:400px"><font color="red">*</font>主要职责</th>';
							 str+=' </tr></thead><tbody></tbody></table></div>';
							 $("#content-d").append(str);
							 <#if (memberList)??>
							    <#if (memberList?size > 0)>
							       
							        <#list memberList as member>
							          if(data[i].value=='${member.planRole}'){
							        	  var obj={};
								          obj.planRole='${member.planRole}';
								          obj.userId='${member.userId}';
								          obj.planMemberId='${member.planMemberId}';
								          obj.orgId='${member.orgId}';
								          obj.orgName='${member.orgName}';
								          obj.partyName='${member.userName}';
								          obj.mainJob='${member.mainJob}';
							               addRecByObj(data[i].value,obj); 
						               }
							        </#list>
							    </#if>
							</#if>
							 
						 }
					}
					  var options = { 
					            axis : "yx", 
					            theme : "minimal-dark" 
					        }; 
					  enableScrollBar('content-d',options);
				  
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
	 }	
	 function addRecByObj(flag,obj){
			var  str =  "<tr id='tr_"+obj.userId+"'>" +
	           "<td>" +
	               '<span id="userName"   style="width: 300px;" >  '+obj.partyName+'<span>' +   
	           "</td>" +
	           "<td>" + 
	               '<span id="mainJob" name="mainJob"   style="width: 400px;">'+obj.mainJob+'<span>' +  
	           "</td>" +
	           "</tr>";
			 $("#list_"+flag).append(str);       
		}
</script>
</html>
