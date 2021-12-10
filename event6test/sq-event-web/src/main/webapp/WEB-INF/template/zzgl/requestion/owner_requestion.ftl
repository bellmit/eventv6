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
	 	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/requestion/submit.jhtml"  method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>事件类别：</span></label>
                        <input type="hidden" id="type" name="type" value="<#if bo.type??>${bo.type}</#if>">
                        <input name="typeStr" id="typeStr" type="text" style="height: 28px;width: 200px;" class="inp1 inp2 easyui-validatebox" value="<#if bo.typeStr??>${bo.typeStr}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                    </td>
					<td>
						<label class="LabName"><span>提交时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd"><label class="LabName"><span>标题：</span></label>
                    <input name="title" id="title" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 590px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if bo.title??>${bo.title}</#if>"/></td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>事件描述：</span></label>
                    <textarea name="content" id="content" cols="45" rows="5" style="width: 590px;height: 65px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']"><#if bo.content??>${bo.content}</#if></textarea></td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd"><label class="LabName"><span>企业名称：</span></label>
                    <input name="reqObjName" id="reqObjName" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 590px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if bo.reqObjName??>${bo.reqObjName}</#if>"/></td>
				</tr>
				<tr>
					<td class="LeftTd"><label class="LabName"><span>联系人：</span></label>
                    	<input name="linkMan" id="linkMan" type="text" style="height: 28px;width: 200px;" class="inp1 inp2 easyui-validatebox" value="<#if bo.linkMan??>${bo.linkMan}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                    </td>
                    <td><label class="LabName"><span>联系方式：</span></label>
                        <input name="linkTel" id="linkTel" type="text" style="height: 28px;width: 200px;" class="inp1 inp2 easyui-validatebox" value="<#if bo.linkTel??>${bo.linkTel}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','mobileorphone']"/>
                    </td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">附件信息</div></td>
                </tr>
				<tr>
					<td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>附       件：</span></label>
                    <div id="fileupload1" class="ImgUpLoad" style="padding-top:4px;"></div></td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>备       注：</span></label>
						<textarea name="desc" id="desc" cols="45" rows="5" style="width: 580px;height: 65px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom'"><#if bo.desc??>${bo.desc}</#if></textarea></td>
					</td>
				</tr>
			</table>
			<input type="hidden" name="buttonType" value=""/>
			<input type="hidden" name="taskId" value="${taskId}"/>
			<input type="hidden" name="nextNodeName" value="task2"/>
			<input type="hidden" name="instanceId" value="${instanceId}"/>
			<input type="hidden" name="reqId" value="${bo.reqId}"/>
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
    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="javascript:send();">提交</a>
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="javascript:cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	$(function(){
		var $NavDiv2 = $("#tab ul li");
		$NavDiv2.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = $NavDiv2.index(this);
			$("div[id^='content']").hide();
			$("#content"+NavIndex2).show();
		});
		
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("typeStr", "type", "${caseTypeDict}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
		
		var swfOpt = {
	    	positionId:'fileupload1',//附件列表DIV的id值',
			type:'edit',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			script_context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			ajaxData: {'bizId':${bo.reqId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE!}','eventSeq':'1'}
	    };
		fileUpload(swfOpt);
		
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
    /*派发记录*/
    function send() {
    	var flag = true;
		if(flag){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("5");
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
		}
    }
    
</script>
</html>
