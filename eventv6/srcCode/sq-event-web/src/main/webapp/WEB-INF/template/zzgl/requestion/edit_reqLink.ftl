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
	<style>
        .leftTitle {
            width: 190px;
            height: 55px;
            line-height: 55px;
            border-bottom: 1px solid #fff;
            text-align: center;
            vertical-align: middle;
            background-color: #e1e1e1;
            cursor: pointer;
        }

        .leftNow {
            background-color: #fff;
        }

        .leftTitle span {
            font-size: 14px;
            color: #333333;
            font-weight: 700;
        }

        .LeftTd2 {
            text-align: center;
            background-color: #66ccff;
            border-bottom: 1px solid #cecece;
            width: 100px;
        }

        .LeftTd3 {
            text-align: center;
            background-color: #fff;
            border-bottom: 1px solid #cecece;
            width: 100px;
        }

        .LabName2 {
            color: #333333;
            font-weight: 400;
            text-align: center;
        }

        .LabName3 {
            color: #333333;
            font-weight: 400;
            text-align: center;
            width: 120px;
        }

        .LabName4 {
            width: 194px;
            text-align: center;
        }

        .list th, .list td {
            border: 1px solid #BED3DF; /* 单元格边框 */
            padding-top: 2px;
            padding-bottom: 2px;
            font-size: 14px;
            padding-left: 2px;
            padding-right: 2px;
            height: 30px;
            border-bottom: 1px solid #9E9E9E;
            font-weight: bold;
            padding-top: 8px;
            text-align: center;
        }
    </style>
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
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>事件类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.typeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>提交时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span>标      题：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.title)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>事件描述：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.content)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>企业名称：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.reqObjName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联  系 人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkMan)!}</span>
					</td>
					<td>
						<label class="LabName"><span>联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkTel)!}</span>
					</td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">附件信息</div></td>
                </tr>
                <tr>
					<td colspan="2">
						<label class="LabName"><span>截止日期：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(overTime)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>附       件：</span></label>
						<div id="fileupload1" class="ImgUpLoad" style="padding-top:4px;"></div></td>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>备       注：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.desc)!}</span>
					</td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">受理信息</div></td>
                </tr>
			</table>
			<input type="hidden" id="isParent2Do" name="isParent2Do" value="${isParent2Do}"/>
		<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/requestion/submitChild.jhtml"  method="post">
			<input type="hidden" name="buttonType" value=""/>
			<input type="hidden" name="taskId" value="${taskId}"/>
			<input type="hidden" name="nextNodeName" value="${nextNodeName}"/>
			<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
			<input type="hidden" name="reqId" value="${bo.reqId}"/>
			<input type="hidden" id="rluId" name="rluId" value="${rluId}"/>
			
           <tr>
                <td colspan="5" class="LeftTd RightTd"><label class="LabName"><span>附件：</span></label>
                <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
    	   </tr>  
           <tr>
				<td colspan="5">
					<label class="LabName"><span>受理意见：</span></label>
					<textarea name="detectedOverview" id="detectedOverview" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[1000]','characterCheck']"  style="width:650px;float: left;margin-bottom:4px;margin-top:4px;"></textarea>
				</td>
			</tr>
		</form>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" >
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
    			<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="javascript:back_();">驳回</a>
    		</#if>
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
		/*
		fileUpload({ 
			positionId:'fileupload',//附件列表DIV的id值',
			type:'add',//add edit detail
			initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${rc.getContextPath()}',
			ajaxData: {'eventSeq':1},//未处理
		});*/
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
    	var detectedOverview = $("#detectedOverview").val();
    	if(detectedOverview==""||detectedOverview==null){
    		parent.$.messager.alert('提示', '请填写受理意见', 'info');
    	}else{
    		var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("2");
				$("#tableForm").ajaxSubmit(function(data) {
	  				if(data.result=="success"){
	  					 var isParent2Do = $("#isParent2Do").val();
	  					 if("1"==isParent2Do){
	  					 	var instanceId = $("#instanceId").val();
	  					 	var rluId = $("#rluId").val();
	  					 	parent.flashRec(instanceId,rluId,"单位办理");
	  					 }else{
	  					 	parent.searchData();
	  					 }
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
    
    function back_(){
    	var detectedOverview = $("#detectedOverview").val();
    	if(detectedOverview==""||detectedOverview==null){
    		parent.$.messager.alert('提示', '请填写受理意见', 'info');
    	}else{
    		$.messager.confirm('提示', '您确定驳回该信息吗?', function(r) {
				if (r) {
					var isValid =  $("#tableForm").form('validate');
					if(isValid){
						modleopen();
						$("input[name='buttonType']").val("1");
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
