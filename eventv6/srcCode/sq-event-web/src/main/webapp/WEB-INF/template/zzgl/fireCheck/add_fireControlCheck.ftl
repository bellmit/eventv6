<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>消防检查信息-新增/编辑</title>
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>

	<style type="text/css">
		.singleCell{width: 215px;}
		.timeCell{width: 122px;}
		.areaCell{width: 577px; height:64px; resize: none;}
		.wholeCell{width: 577px;}
	</style>
</head>
<body>
	<#include "/component/ComboBox.ftl" />
	
	<form id="tableForm" name="tableForm" action="" method="post">
		<input type="hidden" name="type" value="${inspection.type!}" />
		<input type="hidden" id="isDel" name="isDel" value="${inspection.isDel!'2'}" />
		<input type="hidden" id="birId" name="birId" value="<#if inspection.birId??>${inspection.birId?c}</#if>"/>
		<!--用于地图-->
		<input type="hidden" id="id" value="<#if inspection.birId??>${inspection.birId?c}</#if>" />
		<input type="hidden" id="markerOperation" value="${markerOperation!}"/>
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查类型：</span></label>
								<input type="hidden" id="inspectType" name="inspectType" value="${inspection.inspectType!''}" />
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom'" id="inspectTypeName" value="${inspection.inspectTypeName!''}" />
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>所属网格：</span></label>
								<input type="hidden" id="gridId" name="gridId" value="<#if inspection.gridId??>${inspection.gridId?c}</#if>">
								<input type="hidden" id="infoOrgCode" name="gridCode" value="${inspection.gridCode!}"/>
								<input type="text" class="inp1 easyui-validatebox timeCell" data-options="required:true,tipPosition:'bottom',validType:'characterCheck'" name="gridName" id="gridName" value="${inspection.gridName!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>被检查单位名称：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="inspectObjName" id="inspectObjName" value="${inspection.inspectObjName!''}" />
				    		</td>
							<td>
								<#if inspection.inspectSeq??>
									<label class="LabName"><span>检查编号：</span></label><div class="Check_Radio FontDarkBlue">${inspection.inspectSeq!''}</div>
								<#else>
									&nbsp;
								</#if>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>单位负责人：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:false,tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" name="directorOfInspectObj" id="directorOfInspectObj" value="${inspection.directorOfInspectObj!''}" />
				    		</td>
							<td>
								<label class="LabName"><span>联系方式：</span></label>								
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:false,tipPosition:'bottom',validType:'mobileorphone'" name="directorTelOfInspectObj" id="directorTelOfInspectObj" value="${inspection.directorTelOfInspectObj!''}" />
				    		</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>单位地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:false,tipPosition:'bottom',validType:['maxLength[512]','characterCheck']" name="inspectionAddr" id="inspectionAddr" value="${inspection.inspectionAddr!''}" />
				    		</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查人：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" name="inspectorName" id="inspectorName" value="${inspection.inspectorName!''}" />
				    		</td>
							<td>
								<label class="LabName"><span>检查时间：</span></label>
								<input type="text" class="inp1 Wdate easyui-validatebox timeCell" style="cursor: pointer;" data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true, isShowClear:false})" name="startTimeStr" id="startTimeStr" value="${inspection.startTimeStr!''}" />
							</td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>检查内容：</span></label><textarea name="content" id="content" class="area1 easyui-validatebox areaCell" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" >${inspection.content!}</textarea>
					        </td>
						</tr>
						<tr>
							<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span>检查结果：</span></label><textarea name="inspectResult" id="inspectResult" class="area1 easyui-validatebox areaCell" data-options="required:true,tipPosition:'bottom',validType:['maxLength[2048]','characterCheck']" >${inspection.inspectResult!}</textarea>
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
        		<a href="#" onclick="tableSubmit();" class="BigNorToolBtn SaveBtn">存为草稿</a>
        		<a href="#" onclick="tableSubmit('1');" class="BigNorToolBtn BigJieAnBtn">归档</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
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
		    
		    AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
				if(isNotBlankParam(items) && items.length>0){
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				} 
			});
			
			AnoleApi.initTreeComboBox("inspectTypeName", "inspectType", "${FC_INSPECT_TYPE_PCODE!}", null, ['${inspection.inspectType!}']);
			
			var swfOpt = {
		    	positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				script_context_path:'${SQ_FILE_URL}',
				ajaxData: {'eventSeq':1},//未处理
				createUserId: '<#if createUserId??>${createUserId?c}</#if>',
				imgDomain:'${imgDownPath!}',//图片域名 type为add或者edit，showPattern为pic时，生效
				appCode: 'zhsq_event'
		    };
		    
			<#if inspection.birId??>
				swfOpt["type"] = 'edit'; 
				swfOpt["ajaxData"] = {'bizId':${inspection.birId?c},'attachmentType':'${FC_ATTACHMENT_TYPE!}','eventSeq':'1'};
			</#if>
			
			fileUpload(swfOpt);
			
			isInspectionCheck($("#inspectResultFlagCheck"));
	    });
	    
		function tableSubmit(isDel){
		    var isValid = $("#tableForm").form('validate');
		    
			if(isValid){
				modleopen();
				
				if(isDel) {
					$("#isDel").val(isDel);
				}
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/fireControlCheck/saveFireCheck.jhtml");
	      	
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		if(data.success) {
	  					parent.reloadDataForSubPage(data.tipMsg);
	  				} else {
	  					modleclose();
	  					
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', '操作失败！', 'error');
	  					}
	  				}
				});
			}
		}
		
		function isInspectionCheck(checkObj, flagId) {//设置inspectResultFlag值
			var isChecked = $(checkObj).is(":checked");
			var status = "0";
			
			if(isChecked) {
				status = "1";
				$(".inspectionTr").show();
			} else {
				$(".inspectionTr").hide();
				$(".inspectionItem").val("");
			}
			
			$("#"+ flagId).val(status);
		}
		
		function cancel(){
			parent.closeMaxJqueryWindow();
		}

	</script>
</body>
</html>
