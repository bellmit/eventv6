<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
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
				<input type="hidden" id="id" name="id" value="${bo.deviceId}"/>
				<input type="hidden" id="dwtId" name="dwtId" value="${bo.dwtId}"/>
				<input type="hidden" id="markerOperation" name="markerOperation" value="2" />
				<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
				<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">告警信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>设备名称：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.deviceName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>告警时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.warnTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>告警信息：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.warnInfo)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>设备地址：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.deviceAddr)!}</span>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>设备标识：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.deviceGlag)!}</span>
					</td>
					<td>
						<label class="LabName"><span>设备厂商：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.manufacturerStr)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>设备类型：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.deviceTypeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>所属网格：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.infoOrgName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>地理标注：</span></label>
		    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
					</td>
					<td>
						<#if bo.overTimeStr ?? >
							<label class="LabName"><span>处理时限：</span></label>
							<span class="Check_Radio FontDarkBlue">${(bo.overTimeStr)!}</span>
						</#if>
					</td>
				</tr>
			</table>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<th align="center" style="width: 120px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理环节</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理信息</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>处理意见</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>附件</span></label>
					</th>
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
										<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br/>
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
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
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
		
		var swfOpt = {
	    	positionId:'fileupload1',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			script_context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			ajaxData: {'bizId':${bo.dwtId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE!}','eventSeq':'1'}
	    };
		fileUpload(swfOpt);
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
	function showMap() {
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = $(window).width() - 15;
		var height = $(window).height() - 15;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var mapType = $("#module").val();
		var isEdit = false;
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	
	function getParameterJson() {
		var parameterJson = {
			"id": $("#id").val(),
			"name": $("#name").val()
		}
		return parameterJson;
	}
</script>
</html>
