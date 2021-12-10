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
		.doubleCellClass{width:72%;}
		.areaClass{width:85%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if inspection.birId??>${inspection.birId?c}</#if>" />
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		<input type="hidden" id="gridId" value="<#if inspection.gridId??>${inspection.gridId?c}</#if>">
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查类型：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${inspection.inspectTypeName!''}</div>
							</td>
							<td class="LeftTd" width="300px;">
								<label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue">${inspection.gridName!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>被检查单位名称：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${inspection.inspectObjName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>检查编号：</span></label><div class="Check_Radio FontDarkBlue">${inspection.inspectSeq!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>单位负责人：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${inspection.directorOfInspectObj!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>联系方式：</span></label><div class="Check_Radio FontDarkBlue">${inspection.directorTelOfInspectObj!''}</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>单位地址：</span></label><div class="Check_Radio FontDarkBlue areaClass">${inspection.inspectionAddr!''}</div>
				        	</td>
				        </tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查人：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${inspection.inspectorName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>检查时间：</span></label><div class="Check_Radio FontDarkBlue">${inspection.startTimeStr!''}</div>
							</td>
						</tr>
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
				    			<label class="LabName"><span>照片：</span></label><div class="ImgUpLoad" id="fileupload"></div>
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
				ajaxData: {'bizId':${inspection.birId?c},'attachmentType':'${FC_ATTACHMENT_TYPE!}','eventSeq':'1'}
		    };
		    
			fileUpload(swfOpt);
	    });
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
		
	</script>
	
</body>
</html>
