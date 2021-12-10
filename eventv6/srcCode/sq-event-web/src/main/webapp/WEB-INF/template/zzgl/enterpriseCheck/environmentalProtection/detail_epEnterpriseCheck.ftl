<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>企业巡查信息-详情</title>
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	
	<style type="text/css">
		.singleCellClass{width:72%; *width:70%;}
		.areaClass{width:85%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if inspection.birId??>${inspection.birId?c}</#if>" />
		<input type="hidden" id="module" value="${module!}"/>
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		<input type="hidden" id="gridId" value="<#if inspection.gridId??>${inspection.gridId?c}</#if>">
		
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查类型：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${inspection.inspectTypeName!''}</div>
							</td>
							<td class="LeftTd" width="300px;">
								<label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue">${inspection.gridName!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查负责人：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${inspection.inspectorName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>检查时间：</span></label><div class="Check_Radio FontDarkBlue">${inspection.startTimeStr!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>是否发现污染单位：</span></label>
								<div class="Check_Radio FontDarkBlue">
									<#if inspection.inspectResultFlag?? && inspection.inspectResultFlag == '1'>
										是
									<#else>
										否
									</#if>
								</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>检查编号：</span></label><div class="Check_Radio FontDarkBlue">${inspection.inspectSeq!''}</div>
							</td>
						</tr>
						<#if inspection.inspectResultFlag?? && inspection.inspectResultFlag == '1'>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>污染单位名称：</span></label><div class="Check_Radio FontDarkBlue areaClass">${inspection.inspectObjName!''}</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>污染单位地址：</span></label><div class="Check_Radio FontDarkBlue areaClass">${inspection.inspectionAddr!''}</div>
								<#if false>
				    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
				    			</#if>
				        	</td>
				        </tr>
				        <tr>
							<td class="LeftTd">
								<label class="LabName"><span>单位负责人：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${inspection.directorOfInspectObj!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>联系方式：</span></label><div class="Check_Radio FontDarkBlue">${inspection.directorTelOfInspectObj!''}</div>
							</td>
						</tr>
						</#if>
				        <tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>检查内容：</span></label><div class="Check_Radio FontDarkBlue areaClass">${inspection.content!''}</div>
					        </td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2" >
				    			<label class="LabName"><span>检查结果：</span></label><div class="Check_Radio FontDarkBlue areaClass">${inspection.inspectResult!''}</div>
					        </td>
						</tr>
						<tr>
				    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>图片上传：</span></label><div class="ImgUpLoad" id="fileupload"></div>
				    		</td>
				    	</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" class="BigNorToolBtn CancelBtn" onclick="closeWin();">关闭</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function(){
			$("#norFormDiv").width($(document).width());//为了适应弹框的变化
			
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	        
	        var swfOpt = {
		    	positionId:'fileupload',//附件列表DIV的id值',
				type:'detail',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				script_context_path:'${SQ_FILE_URL}',
				ajaxData: {'bizId':${inspection.birId?c},'attachmentType':'${EP_ATTACHMENT_TYPE!}','eventSeq':'1'}
		    };
		    
			fileUpload(swfOpt);
	    });
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
		
		function showMap(){
			var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
				width = 480,
				height = 360,
				gridId = $("#gridId").val(),
				mapType = $('#module').val(),
				isEdit = false;
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
	</script>
	
</body>
</html>
