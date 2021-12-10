<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>事件-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		width:120px;
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
	}
</style>

</head>
<body>
	<div class="NorMapOpenDiv">
		<div class="con LyWatch" style="height:220px;overflow:auto;">
        	<ul>
        		<li style="width:300px"><a class="FontDarkBlue" href="javascript:void(0);" style="font-size:14px;font-weight:bold" onclick="showEventDetail('<#if record.eventId??>${record.eventId}</#if>');"><#if record.title??>${record.title}</#if></a></li>
				<li style="width:300px;">表单类型：<span class="FontDarkBlue">
							 <#if record.formType??>
							   <#if (record.formType=="01")>单元</#if>
							   <#if (record.formType=="02")>整层</#if>
							   <#if (record.formType=="03")>整座</#if>
                          	 </#if>
							</span></li>
				<li style="width:300px;">表单操作：<span class="FontDarkBlue">
							 <#if record.formOpt??>
							   <#if (record.formOpt=="01")>新增</#if>
							   <#if (record.formOpt=="02")>变更</#if>
                        	 </#if>
							</span></li>
				<li style="width:300px;">紧急程度：<span class="FontDarkBlue">
                    		<#if record.urgency??>
							   <#if (record.urgency=="01")>一般</#if>
							   <#if (record.urgency=="02")>紧急</#if>
                            </#if>
						   </span></li>
				<li style="width:300px;">处理时限：<span class="FontDarkBlue">
							<#if record.handled??>${record.handled}</#if>
							</span></li>
				<li style="width:300px;">备注：<span class="FontDarkBlue">
							<#if record.remark??>${record.remark}</#if>
							</span></li>
			</ul>
           <div class="clear"></div>
        </div>
	</div>
	
<script type="text/javascript">
   function showEventDetail(eventId){
       var sq_lyjj_url = window.parent.document.getElementById("SQ_LYJJ_URL").value;
		$.ajax({
		      url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataoflyjj/getLyjjEventInfo.json',
		      type:'POST',
		      dataType:"json",
		      async: true,
		      data:{
		         eventId:eventId
		      },
		      success:function(xxxJson){
				 var result = xxxJson.result;
		      	 if(result=="success"){
		      		 
		      		var data=xxxJson.record;
		      		var formId=data.FORM_ID;
	      			var formType=data.FORM_TYPE;
	  				var instanceId=data.WF_INSTANCE_ID;
					var wfDbId=data.WF_DBID_;
					var operateType=data.WF_OPERATE_TYPE;
					var activityName=data.WF_ACTIVITY_NAME_;
		      		activityName = encodeURIComponent(encodeURIComponent(activityName));
		      		var appUser=data.WF_APP_USER;
		      		var url= sq_lyjj_url+"/be/event/toEventDetail.jhtml?eventId="+eventId+"&formId="+formId+"&formType="+formType+"&instanceId="+instanceId+"&operFlag=look_db&taskId="+wfDbId+"&operateType="+operateType+"&activityName="+activityName+"&fromMap=2"+"&appUser="+appUser; 
		      		//var url= sq_lyjj_url+"/be/event/toEventDetail.jhtml?eventId="+eventId+"&formId="+formId+"&formType="+formType+"&instanceId="+instanceId+"&operFlag=look_db&taskId="+wfDbId+"&operateType="+operateType+"&activityName="+activityName+"&fromMap=2"; 
		      		window.parent.showMaxJqueryWindow('事件名片',url,948,420);
		      	 }else{
		      		 alert("请联系系统管理员!!!");
		      	 }
		      }
		});
   }

</script>				
</body>
</html>
