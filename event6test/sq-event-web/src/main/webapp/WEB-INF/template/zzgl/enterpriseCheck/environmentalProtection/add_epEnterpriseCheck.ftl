<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>企业巡查信息-新增/编辑</title>
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/component/comboselector/clientJs.jhtml?version=v2"></script>
	
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
		<input type="hidden" id="module" value="${module!}"/>
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
							<td id="secondTd" class="LeftTd">
								<label class="LabName"><span>所属网格：</span></label>
								<input type="hidden" id="gridId" name="gridId" value="<#if inspection.gridId??>${inspection.gridId?c}</#if>">
								<input type="hidden" id="infoOrgCode" name="gridCode" value="${inspection.gridCode!}"/>
								<input type="text" class="inp1 easyui-validatebox timeCell" data-options="required:true,tipPosition:'bottom',validType:'characterCheck'" name="gridName" id="gridName" value="${inspection.gridName!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>检查负责人：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell" data-options="required:true,tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" name="inspectorName" id="inspectorName" value="${inspection.inspectorName!''}" />
				    		</td>
							<td>
								<label class="LabName"><span>检查时间：</span></label>
								<input type="text" class="inp1 Wdate easyui-validatebox timeCell" style="cursor: pointer;" data-options="required:true,tipPosition:'bottom'" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true, isShowClear:false})" name="startTimeStr" id="startTimeStr" value="${inspection.startTimeStr!''}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<input type="hidden" id="inspectResultFlag" name="inspectResultFlag" value="${inspection.inspectResultFlag!'0'}" />
				    			<label class="LabName"></label><div class="Check_Radio"><span><input type="checkbox" id="inspectResultFlagCheck" onclick="isInspectionCheck(this, 'inspectResultFlag');" <#if inspection.inspectResultFlag?? && inspection.inspectResultFlag=='1'>checked</#if> /><label for="inspectResultFlagCheck" style="cursor:pointer">发现污染单位</label></span></div>
				    		</td>
							<td>
								<#if inspection.inspectSeq??>
									<label class="LabName"><span>检查编号：</span></label><div class="Check_Radio FontDarkBlue">${inspection.inspectSeq!''}</div>
								<#else>
									&nbsp;
								</#if>
							</td>
						</tr>
						<tr class="inspectionTr hide">
							<td class="LeftTd">
								<label class="LabName"><span>污染单位名称：</span></label>
								<input type="hidden" class="inspectionItem" id="inspectObjName" name="inspectObjName" value="${inspection.inspectObjName!}"/>
								<input type="hidden" class="inspectionItem" id="inspectObjId" name="inspectObjId" value="<#if inspection.inspectObjId??>${inspection.inspectObjId?c}</#if>" />
								<input type="hidden" id="isAutoClear" value="1" />
								<input type="text" id="corPlaceName" class="comboselector singleCell inspectionItemRequired" data-options="tipPosition:'bottom', dType:'envCorp', afterSelect:selectEnvCorp, height: '28px'" query-params="<#if defaultGridId??>gridId=${defaultGridId?c}</#if>" />
							</td>
							<td>
								<label class="LabName"><span>污染单位地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell inspectionItem inspectionItemRequired" data-options="tipPosition:'bottom',validType:['maxLength[512]','characterCheck']" name="inspectionAddr" id="inspectionAddr" value="${inspection.inspectionAddr!''}" />
							</td>
						</tr>
						<#if false>
						<tr class="inspectionTr">
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span>污染单位地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox wholeCell inspectionItem" data-options="tipPosition:'bottom',validType:['maxLength[512]','characterCheck']" name="inspectionAddr" id="inspectionAddr" value="${inspection.inspectionAddr!''}" />
				    			<span class="hide"><#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/></span>
				        	</td>
				        </tr>
				        </#if>
						</tr>
						<tr class="inspectionTr">
							<td class="LeftTd">
								<label class="LabName"><span>单位负责人：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell inspectionItem" data-options="tipPosition:'bottom',validType:['maxLength[24]','characterCheck']" name="directorOfInspectObj" id="directorOfInspectObj" value="${inspection.directorOfInspectObj!''}" />
				    		</td>
							<td>
								<label class="LabName"><span>联系方式：</span></label>
								<input type="text" class="inp1 easyui-validatebox singleCell inspectionItem" data-options="tipPosition:'bottom',validType:'mobileorphone'" name="directorTelOfInspectObj" id="directorTelOfInspectObj" value="${inspection.directorTelOfInspectObj!''}" />
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
				    			<label class="LabName"><span>图片上传：</span></label><div class="ImgUpLoad" id="fileupload"></div>
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
			$("#secondTd").width($(window).width() * 0.5);
			
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
			
			AnoleApi.initTreeComboBox("inspectTypeName", "inspectType", "${EP_INSPECT_TYPE_PCODE!}", null, ['${inspection.inspectType!}']);
			
			var swfOpt = {
		    	positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${SQ_FILE_URL}',
				script_context_path:'${SQ_FILE_URL}',
				ajaxData: {'eventSeq':1},//未处理
				createUserId: '<#if createUserId??>${createUserId?c}</#if>',
				appCode: 'zhsq_event'
		    };
		    
			<#if inspection.birId??>
				swfOpt["type"] = 'edit'; 
				swfOpt["ajaxData"] = {'bizId':${inspection.birId?c},'attachmentType':'${EP_ATTACHMENT_TYPE!}','eventSeq':'1'};
			</#if>
			
			fileUpload(swfOpt);
			
			isInspectionCheck($("#inspectResultFlagCheck"));
			
			$('#corPlaceName').combogrid('setText', '${inspection.inspectObjName!}');
			
			$('#corPlaceName').combogrid('textbox').css({'cursor': 'pointer'});
			$('#corPlaceName').combogrid('textbox').attr("unselectable", "on");
			$('#corPlaceName').combogrid('textbox').on('blur', changeInspectObjName);
	    });
	    
		function tableSubmit(isDel){
		    var isValid = $("#tableForm").form('validate');
		    
		    if(isValid) {
		    	var corPlaceName = $('#corPlaceName').combogrid('getText');
		    	
		    	if(!corPlaceName && $("#inspectResultFlagCheck").is(':checked') == true) {
		    		$.messager.alert('警告', '污染单位名称不能为空！', 'warning', function() {
		    			$('#corPlaceName').combogrid('textbox').focus();
		    		});
		    		
		    		isValid = false;
		    	} else {
				    $(".inspectionItemRequired:visible").each(function() {
				    	var item = $(this), val = item.val();
				    	
				    	if(!val) {
				    		$.messager.alert('警告', item.siblings("label").text().replace('：', '')+'不能为空！', 'warning', function() {
				    			item.focus();
				    		});
				    		
				    		isValid = false;
				    		return false;
				    	}
				    });
			    }
		    }
		    
			if(isValid){
				modleopen();
				
				if(isDel) {
					$("#isDel").val(isDel);
				}
				
				//获取企业名称，不需要每变化一次就更新一次
				$("#inspectObjName").val($('#corPlaceName').combogrid('getText'));
				$('#inspectObjId').val($('#corPlaceName').combogrid('getValue'));
				
				$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/enterpriseCheck/saveEnterpriseCheck.jhtml");
	      	
			  	$("#tableForm").ajaxSubmit(function(data) {
			  		if(data.success) {
			  			var birId = $("#birId").val(),
			  				isCurrent = birId != "" && birId > 0;
			  			
	  					parent.reloadDataForSubPage(data.tipMsg, isCurrent);
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
				//为了修复IE下文本框不能正常获取焦点的问题
				$("#inspectionAddr").focus();
				$("#inspectionAddr").blur();
			} else {
				$(".inspectionTr").hide();
				$(".inspectionItem").val("");
				$('#corPlaceName').comboselector('clear');
			}
			
			$("#"+ flagId).val(status);
		}
		
		function cancel(){
			parent.closeMaxJqueryWindow();
		}
		
		/*
			markerOperation:地图标注操作类型
							 0表示添加
							 1表示编辑
							 2表示查看
			isEdit:是否是编辑状态
		*/
		function showMap(){
			var callBackUrl = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
				width = 480,
				height = 360,
				gridId = $("#gridId").val(),
				markerOperation = $('#markerOperation').val(),
				id = $('#birId').val(),
				mapType = $("#module").val(),
				isEdit = true,
				parameterJson = getParameterJson();
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
		
		function getParameterJson() {
			var parameterJson={
				"id":$("#id").val(),
				"name":$("#name").val()
			}
			return parameterJson;
		}
		
		//环保企业组件回调方法
		function selectEnvCorp(corBaseInfo) {
			var isAutoClear = $('#isAutoClear').val();
			
			if(corBaseInfo != undefined) {
				$('#inspectObjName').val(corBaseInfo.corName);//企业名称
				$('#inspectionAddr').val(corBaseInfo.corAddr);//法人地址
				$('#directorOfInspectObj').val(corBaseInfo.representativeName);//法人代表
				$('#directorTelOfInspectObj').val(corBaseInfo.telephone);//联系电话
			} else if(isAutoClear == '1') {
				$(".inspectionItem").val("");
			} else {
				$('#isAutoClear').val('1');
				$('#corPlaceName').comboselector('setText', $('#inspectObjName').val());
			}
		}
		//污染单位名称 内容变化回调方法，防止手动输入不同步
		function changeInspectObjName() {
			var backName = $('#inspectObjName').val(),
				curName = $('#corPlaceName').combogrid('getText');
			
			if(curName != backName) {
				$('#isAutoClear').val('0');//不自动清空字段
				$('#inspectObjId').val('');
				$('#inspectObjName').val(curName);
				$('#corPlaceName').comboselector('clear');
			}
		}
	</script>
	
</body>
</html>
