<!DOCTYPE html>
<html>
<head>
	<title>办理页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	
</head>
<body>
	<div class="MC_con content light">
		<div class="ConList">
	     	<div class="nav" id="tab" style="margin-top:10px;">
		        <ul>
		            <li class="current">基本情况</li>
		            <li>办理详情</li>
		        </ul>
	    	</div>
	 	</div>
	 	<div name="tab" id="content0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue" style="margin-top:0px;" >报修任务信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>所属网格：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.infoOrgName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>故障类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultTypeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联系人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkUser)!}</span>
					</td>
					<td>
						<label class="LabName"><span>电话：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkTel)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>上报时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.repairTimeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>预约时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.orderTimeStr)!}</span>
					</td>
				</tr>
				<#if bo.overTimeStr ?? >
					<tr>
						<td>
							<label class="LabName"><span>处理时限：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.overTimeStr)!}</span>
						</td>
						<td>
						</td>
					</tr>
				</#if>
				<tr>
					<td colspan="2" class="LeftTd RightTd">
						<label class="LabName"><span>故障描述：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultDesc)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd RightTd">
						<label class="LabName"><span>故障地址：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.faultAddr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>附       件：</span></label>
						<div id="fileupload1" class="ImgUpLoad" style="padding-top:4px;"></div>
					</td>
				</tr>
			</table>
		<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/repairTask/submit.jhtml"  method="post">
			<input type="hidden" id="buttonType" name="buttonType" value=""/>
			<input type="hidden" id="taskId" name="taskId" value="${taskId}"/>
			<input type="hidden" id="nextNodeName" name="nextNodeName" value="${nextNodeName}"/>
			<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
			<input type="hidden" id="drtId" name="drtId" value="${bo.drtId}"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
	           <tr>
	                <td class="LeftTd" colspan="5"><div class="title FontDarkBlue">${nodeNameZH}</div></td>
	           </tr>
	           <#if dynamicSelect ?? && dynamicSelect=='1'>
	           		<tr>
		                <td colspan="5">
							<label class="LabName"><span>选择办理人：</span></label>
							<div class="Check_Radio">
						    	<a href="#" class="NorToolBtn EditBtn fl" id="userSelectBtn" onclick="selectUser('userIds', 'userNames', 'curOrgIds', 'curOrgNames', 'MWWLW_GLY');">选择办理人</a>
							</div>
							<input type="hidden" id="userIds" name="userIds" value=""/>
							<input type="hidden" id="curOrgIds" name="curOrgIds" value=""/>
							<input type="hidden" id="curOrgNames" name="curOrgNames" value=""/>
							<div class="FontDarkBlue fl DealMan Check_Radio"><b name="userNames" id="userNames"></b></div>
						</td>
		    	    </tr> 
		    	    <tr>
						<td colspan="5">
							<label class="LabName"><span>处理时限：</span></label>
							<input type="text" class="inp1 Wdate easyui-validatebox timeCell" style="cursor: pointer;width:150px;" data-options="tipPosition:'bottom'" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true, isShowClear:false})" name="overTime" id="overTime" value="" />
						</td>
					</tr> 
	           </#if>
	           <tr>
	                <td colspan="5" class="LeftTd RightTd"><label class="LabName"><span>附件：</span></label>
	                <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
	    	   </tr>  
	           <tr>
					<td colspan="5">
						<label class="LabName"><span>办理意见：</span></label>
						<textarea name="overview" id="overview" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[1000]','characterCheck']"  style="width:650px;float: left;margin-bottom:4px;margin-top:4px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" style="width: 120px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理环节</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理信息</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>处理意见</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>附件</span></label>
					</td>
				</tr>
				<#list taskList as task>
				    <tr>
				        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.TASK_NAME)!}<#if task.OPERATE_TYPE=='2'><br/>（驳回 ）<br/></#if></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">办理人：${(task.TRANSACTOR_NAME)!}<br/>办理时间：${(task.END_TIME)!}</td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;" title="${(task.REMARKS)!}"><div style="white-space:break-word;text-overflow:ellipsis;overflow:hidden;width:220px;">${(task.REMARKS)!}</div></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">
				        	<#if task.fileList ?? && (task.fileList?size > 0)>
	                			<#list task.fileList as file>
	                				<div>
										<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br>
									</div>
							    </#list>
				            </#if>
				        </td>
				    </tr>
				</#list>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<#if operateLists ?? && (operateLists > 1)>
    			<a href="javascript:;" class="BigNorToolBtn RejectBtn" onClick="javascript:back_();">驳回</a>
    		</#if>
    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="javascript:send();">提交</a>
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="javascript:cancel();">关闭</a>
        </div>
    </div>
    <#if dynamicSelect ?? && dynamicSelect=='1'>
    	<#include "/zzgl/mwInternet/warnTask/select_user.ftl" />
    </#if>
</body>
<script type="text/javascript">
	var addListSize = 1;
	$(function(){
		var $NavDiv2 = $("#tab ul li");
		$NavDiv2.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = $NavDiv2.index(this);
			$("div[id^='content']").hide();
			$("#content"+NavIndex2).show();
		});
		
		var swfOpt1 = {
	    	positionId:'fileupload',//附件列表DIV的id值',
			type:'add',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',
			script_context_path:'${SQ_FILE_URL}',
			ajaxData: {'eventSeq':1},//未处理
			imgDomain:'${imgDownPath!}',//图片域名 type为add或者edit，showPattern为pic时，生效
			appCode: 'zhsq_event'
	    };
	    
		fileUpload(swfOpt1);
		
		var swfOpt = {
	    	positionId:'fileupload1',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			script_context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			ajaxData: {'bizId':${bo.drtId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE!}','eventSeq':'1'}
	    };
		fileUpload(swfOpt);
				
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}

    /*提交记录*/
    function send() {
    	var flag = true;
    	var nextNodeName = $("#nextNodeName").val();
    	if("task3"==nextNodeName){
	    	if($("#curOrgIds").val()==null||$("#curOrgIds").val()==''
	    		||$("#curOrgIds").val()==null||$("#curOrgIds").val()==''){
	    		 parent.$.messager.alert('提示', '请选择办理人', 'info', function () {
	    		 		flag = false;
		                return;
		         });
		         return;
			}
			if($("#overTime").val()==null||$("#overTime").val()==''){
				parent.$.messager.alert('提示', '请填写办理时限', 'info', function () {
	    		 		flag = false;
		                return;
		        });
				return;
			}
    	}
		if(flag){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("1");
				$("#tableForm").ajaxSubmit(function(data) {
	  				if(data.result=="success"){
	  					   parent.searchData();
	  					   parent.$.messager.alert('提示', '提交成功', 'info', function () {
	  					 	  parent.closeMaxJqueryWindow();
			               });
	  				}else{
	  					 parent.$.messager.alert('提示', '提交失败', 'info', function () {
			                return;
			            });
	  				}
				});
			}
		}else{
			parent.$.messager.alert('提示', '', 'info', function () {
                
            });
		}
    }
    //驳回
    function back_(){
    	var overview = $("#overview").val();
    	if(overview==""||overview==null){
    		parent.$.messager.alert('提示', '请填写受理意见', 'info');
    	}else{
    		$.messager.confirm('提示', '您确定驳回该信息吗?', function(r) {
				if (r) {
					var isValid =  $("#tableForm").form('validate');
					if(isValid){
						modleopen();
						$("input[name='buttonType']").val("2");
						$("#tableForm").ajaxSubmit(function(data) {
							if(data.result=="success"){
			  					 parent.searchData();
			  					 parent.$.messager.alert('提示', '驳回成功', 'info', function () {
					                parent.closeMaxJqueryWindow();
					             });
			  				}else{
			  					parent.$.messager.alert('提示', '驳回失败', 'info', function () {
					                return;
					            });
			  				}
						});
					}
				}
			});
    	}
    }
    
	
</script>
</html>
