<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
	</style>
<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/normal-custom.css" />	
<link rel="stylesheet" type="text/css" href="${SQ_GMIS_URL}/css/jj/css/add.css" />

<script src="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/layui.all.js"></script>
<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/jquery.ffcs.pc.select.js"></script>
<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/custom_msgClient.js "></script>
<style type="text/css">
.LabName{
  text-align: left;
}
</style>
</head>
<body>
	<form id="submitForm">
	
	
	    <input type="hidden" id="planMemberArr" name="planMemberArr" value="" />  
	    <input type="hidden" id="planId" name="planId" value="${(bo.planId)!planId}" />  
		<input type="hidden" id="planConfigId" name="planConfigId" value="${(bo.planConfigId)!}" />
		<div id="content-d" class="MC_con content light NorForm" style="padding-right: 10px;padding-left: 10px">
			<div name="tab" class="NorForm">
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
						<td class="LeftTd">    
							 <label class="LabName"><span><font style="color: red">*</font>预案等级:</span></label>
							 <input type="hidden" id="planLevel" name="planLevel" value="${bo.planLevel!''}" />
							 <input  type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom'" id="planLevelName" value="${bo.planLevelName!''}" />
						</td>
					</tr>
				</table>
			</div>
 
	    </div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
$(function(){
	AnoleApi.initTreeComboBox("planLevelName", "planLevel", "A001140", null, ['${bo.planLevel!}']);
	
});
	//保存
	function save() {
		var submitData ={};
		var isValid = $('#submitForm').form('validate');
		
		var tableList=getTableList();
		$("#planMemberArr").val(JSON.stringify(tableList));
		if (isValid) {
			modleopen(); //打开遮罩层
			
			
			
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/zzgl/planConfig/countList.json?planId=${(bo.planId)!planId}'+'&planLevel='+$("#planLevel").val()+'&excludeId=${(bo.planConfigId)!}',
				//data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if(data>0){
						$.messager.alert('错误', '预案等级已存在，请修改等级！', 'error');
						modleclose(); //关闭遮罩层
					}else{
						$.ajax({
							type: 'POST',
							url: '${rc.getContextPath()}/zhsq/zzgl/planConfig/save.json',
							data: $('#submitForm').serializeArray(),
							dataType: 'json',
							success: function(data) {
								if (data.result == 'fail') {
									$.messager.alert('错误', '保存失败！', 'error');
								} else {
									$.messager.alert('提示', '保存成功！', 'info', function() {
										parent.closeMaxJqueryWindow();
									});
									parent.searchData();
								}
							},
							error: function(data) {
								$.messager.alert('错误', '连接超时！', 'error');
							},
							complete : function() {
								modleclose(); //关闭遮罩层
							}
						});
					}
		 
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
	
		}
	}
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
	 
    /*删除一行记录*/
    function delRec(num,obj){
            $.messager.confirm("提示","确定要删除本条记录吗?",function (r) {
                if(r){$(obj).parent().parent().remove();}
            });
        
    }
    
    
    
    
 $(function(){
	 initTabs();
 });   
 
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
						 str+='<div id="con1" class="title FontDarkBlue pos-rela" >'+data[i].name+'<div class="title-r-btn"><a href="###" class="BigNorToolBtn add-addr" id="add_'+data[i].value+'"><i class="icon-add-w"></i>新 增</a></div></div>';
						 str+='<div class="table-normal"><table id="list_'+data[i].value+'"  class="list" width="100%" border="0" cellspacing="0" cellpadding="0"><thead><tr>';
						 str+='<th style="width:300px"><font color="red">*</font>姓名</th>';
						 str+='<th style="width:400px"><font color="red">*</font>主要职责</th>';
						 str+='<th style="width:92px"></th>';
						 str+=' </tr></thead><tbody></tbody></table></div>';
						 $("#content-d").append(str);
							/* 	 var initJSONArr = [{
									 orgId: "960",                       //组织id
									             orgName: "福州市政府口",            //组织名称
									             partyName: "福州市管理员",          //用户名称
									             userId: "1086"                      //用户id
									 }];//初始化数据,如果是新增页面,则不需要设置
							 */ 
						 
						var  initJSONArr=[];//初始化数据,如果是新增页面,则不需要设置
						 
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
						              //initJSONArr.push(obj);
					               }
						        </#list>
						    </#if>
						</#if>
				 
						initSelectPepole(data[i].value,initJSONArr);
						 
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
 
 //选人初始化
 function initSelectPepole(divId,initJSONArr){
			 var wapSelect1 = $("#add_"+divId).initSelect(${orgId},function(res){ 
				 if(res.length>0){
					 var names='';
					 for (var i = 0; i < res.length; i++) {
						 var obj=res[i];
						 obj.planMemberId='';
						 obj.mainJob='';     
						 if($("#list_"+divId+" #tr_"+obj.userId).length==0){
							 addRecByObj(divId,obj);   
						 }else{
							 if(names!=''){
								 names+=',';
							 }
							 names+=obj.partyName;
						 }
						
					}    
					 if(names!=''){
							$.messager.alert('提示', '人员（'+names+'）已存在，不再重复添加！', 'info');
						 
					 }
				 }
			 },{
			layer:{
		           area: ['700px', '380px'], //宽高          
		    },
		    postParam:{
		    	initValue: JSON.stringify(initJSONArr), //用户回填使用
		    	chooseType:['department','people'],  //'department','people', 'position','role'
		    	multi:true,                 //是否允许多选
		    	isRepeat:true,              //用户是否可以重复
		    	
		    	//clickLevelRange:[1,2]    //部门类型组织树可以点击的有效层级
    	        //isSubordinate:false,     //是否查询下级数据，只有部门类型查人员有效
    	        //orgInitRole:[143,150],
    	        //orgInitPosition:[2080,2540]
    	        //topLevel:1
		    },
			 context:'${$COMPONENTS_DOMAIN}'
			 });
 }
 
 /*增加记录*/
 var rec = 0;
 //选择人员后新增
 function addRecByObj(flag,obj){
	   rec="";
	  //{isNewData: "0", userId: "30021894", orgId: "407000", orgName: "周家坝街道", partyName: "周家坝副书记", …}
		var  str =  "<tr id='tr_"+obj.userId+"'>" +
		    ' <input type="hidden" id="planRole'+rec+'" name="planRole" value="'+flag+'" /> '+
		    ' <input type="hidden" id="planMemberId'+rec+'" name="planMemberId" value="'+obj.planMemberId+'" /> '+
		    ' <input type="hidden" id="userId'+rec+'" name="userId" value="'+obj.userId+'" /> '+
		    ' <input type="hidden" id="orgId'+rec+'" name="orgId" value="'+obj.orgId+'" /> '+
		    ' <input type="hidden" id="orgName'+rec+'" name="orgName" value="'+obj.orgName+'" /> '+
           "<td>" +
               '<input type="hidden"  id="userName'+rec+'"   name="userName"  value="'+obj.partyName+'" /> <span    style="width: 300px;">  '+obj.partyName+'</span>' +
           "</td>" +
           "<td>" +
               '<input id="mainJob'+rec+'" name="mainJob"  class="inp1" style="width: 400px;" value="'+obj.mainJob+'"/>' +
           "</td>" +

           "<td>" +
               '<a href="###" class="del-tr bg-gray" onclick="delRec(1,this);"><i class="icon-del"></i>删 除</a>' +
           "</td>" +
           "</tr>";
		   rec++;
		 $("#list_"+flag).append(str);  
		 $("#list_"+flag+" #mainJob").validatebox({
			    required: true,
				tipPosition:'bottom',
				validType:['maxLength[100]']
		 });
	}
 
 function getTableList(){
	 var tablelist=[];
	  $(".list>tbody").find("tr").each(function(){//每次获取一行信息
         // var tdArr = $(this).children();
          var planRole =  $(this).find('#planRole').eq(0).val(); 
          var planMemberId = $(this).find('#planMemberId').eq(0).val(); 
          var userId = $(this).find('#userId').eq(0).val(); 
          var userName = $(this).find('#userName').eq(0).val(); 
          var mainJob = $(this).find('#mainJob').eq(0).val(); 
          var orgId = $(this).find('#orgId').eq(0).val(); 
          var orgName = $(this).find('#orgName').eq(0).val(); 
          if(userId!=undefined){
              var obj=new Object();
                obj.planRole=planRole;
                obj.planMemberId=planMemberId;
                obj.userId=userId;
                obj.userName=userName;
                obj.orgId=orgId;
                obj.orgName=orgName;
                obj.mainJob=mainJob;
                tablelist.push(obj);
          }
      });
	 return tablelist;
 }
 
</script>
</html>
