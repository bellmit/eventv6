<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>营商环境问题-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
			.inputWidth{width: 170px;}
			.w300{width: 300px;}
		</style>
	</head>
	<body>
		<form id="bpForm" name="bpForm" action="" method="post">
			<input type="hidden" id="isStart" name="isStart" value="false" />
			<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
			<input type="hidden" name="isSaveResMarkerInfo" value="true" />
			<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" name="reportUUID" value="${reportFocus.reportUUID!}" />
			<input type="hidden" name="reportType" value="${reportType!}" />
			<!--用于地图-->
			<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" id="name" name="name" value="" />
			<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
			<input type="hidden" id="module" value="BUSINESS_PROBLEM"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<#--报告方式-->
			<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
			<input name="userTel" type="hidden" value="${userTel!''}" />			
							
			<div id="content-d" class="MC_con content light">
				<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>报告人姓名：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
								<input type="hidden" name="reporterName" value="${reportFocus.reporterName!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide">
									<span>报告时间：</span>
								</label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!''}</div>
								<input type="hidden" id="reportTime" name="reportTime" value="${reportFocus.reportTimeStr!}"></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>所属区域：</span></label>
								<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
								<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>报告方式：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
							</td>
						</tr>
						<#if reportFocus.dataSource == '02'>
						<tr id="doOrgNamePath">
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>部门名称：</span></label>
								<input  id="doOrgName" name="doOrgName" placeholder="" type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.doOrgName!}" />
							</td>
						</tr>
						<tr id="doBusinessPath">
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>办理业务：</span></label>
								<input  id="doBusiness" name="doBusiness" placeholder="" type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.doBusiness!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>事发地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						</#if>
						<#if reportFocus.dataSource == '05'||reportFocus.dataSource == '01'>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>反映对象：</span></label>
								<input type="hidden" id="reportObj" name="reportObj" value="${reportFocus.reportObj!}"/>
								<input type="text" name="reportObjStr" id="reportObjStr" value="${reportFocus.reportObjStr!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd" id="massesNamePath" <#if reportFocus.reportObj != '2'>style="display:none;"</#if>>
								<label class="LabName"><span><label class="Asterik">*</label>群众姓名：</span></label>
								<input type="text" name="massesName" id="massesName" value="${reportFocus.massesName!}" class="inp1 easyui-validatebox w300" data-options="<#if reportFocus.reportObj == '2'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[10]','characterCheck']"/>
							</td>
						</tr>
						<tr id="enterpriseNamePath" <#if reportFocus.reportObj?? &&reportFocus.reportObj != '1'>style="display:none;"</#if>>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>企业名称：</span></label>
								<input  id="enterpriseName" name="enterpriseName" placeholder="" type="text" style="width: 80%" class="inp1 easyui-validatebox autoWidth" data-options="<#if reportFocus.reportObj == '1'||reportFocus.reportObj==null||reportFocus.reportObj==''>required:true,</#if>tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.enterpriseName!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>企业地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						</#if>
						<#if reportFocus.dataSource == '05'>
						<tr id="reportWayPath" <#if reportFocus.reportObj == '3'||reportFocus.reportObj == '4'>style="display:none;"</#if>>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>反映途径：</span></label>
								<input  id="reflectWay" name="reflectWay" placeholder="" type="text"  class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.reflectWay!}" />
							</td>
						</tr>
						<tr id="openWorkPath" <#if reportFocus.reportObj != '3'>style="display:none;"</#if>>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>开展业务：</span></label>
								<input  id="openWork" name="openWork" placeholder="" type="text"  class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.openWork!}" />
							</td>
						</tr>
						<tr id="doOrgNamePath" <#if reportFocus.reportObj != '4'>style="display:none;"</#if>>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>部门名称：</span></label>
								<input  id="doOrgName" name="doOrgName" placeholder="" type="text"  class="inp1 easyui-validatebox autoWidth" data-options="<#if reportFocus.reportObj == '4'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.doOrgName!}" />
							</td>
						</tr>
						</#if>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>问题描述：</span></label><textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>附件上传：</span></label><div id="attachFileUploadDiv"></div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="saveFFP(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveFFP(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
		
		<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			$('#bpForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			if($("#dataSource").val()=='01'||$("#dataSource").val()=='05'||$("#dataSource").val()=='02') {
				init4Location('occurred');
			}
			//加载网格
			AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							$("#regionCode").val(items[0].orgCode);
						}
					},
					{
						ChooseType:"1",
						ShowOptions:{
							<#if reportFocus.dataSource?? && reportFocus.dataSource!='01'>
								//来源是非网格员只需要选择到村社区层级
								AllowSelectLevel:"5,6"//选择到哪个层级
							<#else >
								AllowSelectLevel:"6"//选择到哪个层级
							</#if>
						}
					}
			);
			
			<#if reportFocus.resMarker??>
				var resMarkerX = "${reportFocus.resMarker.x!}",
					resMarkerY = "${reportFocus.resMarker.y!}",
					resMarkerMapType = "${reportFocus.resMarker.mapType!}";
				
				if(resMarkerX && resMarkerY && resMarkerMapType) {
					callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
				}
			</#if>
		
			var bigFileUploadOpt = {
				useType: 'add',
				fileExt: '.jpg,.gif,.png,.jpeg,.webp',
				attachmentData: {attachmentType:'BUSINESS_PROBLEM'},
				module: 'businessProblem',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();

			var attachFileUploadOpt = {
						useType: 'add',
						fileExt: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
						attachmentData: {attachmentType:'BUSINESS_PROBLEM'},
						module: 'businessProblem',
						individualOpt: {
							isUploadHandlingPic: true
						}
					};
			
			if(reportId) {
				$.extend(bigFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(bigFileUploadOpt.attachmentData, {
					useType		: 'edit',
					eventSeq	: '1,2,3',
					bizId		: reportId,
					isBindBizId	: 'yes'
				});

				$.extend(attachFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(attachFileUploadOpt.attachmentData, {
					useType		: 'edit',
					eventSeq	: '1,2,3',
					bizId		: reportId,
					isBindBizId	: 'yes'
				});
			}
			
			<#if reportFocus.attachmentIds??>
				bigFileUploadOpt["useType"] = 'edit'; 
				bigFileUploadOpt["attachmentData"].attachmentIds = "${reportFocus.attachmentIds!}";
			</#if>
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			if($('#reportObj').length > 0&&$("#dataSource").val()=='01') {
				AnoleApi.initTreeComboBox("reportObjStr", "reportObj", null, null, ["${reportFocus.reportObj!'1'}"], {
					DataSrc : [{"name":"企业", "value":"1"}, {"name":"群众", "value":"2"}],
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						if(value == '1') {
							$('#massesNamePath').hide();
							$('#enterpriseNamePath').show();
							$('#enterpriseName').validatebox({
								required: true
							});
							$('#massesName').validatebox({
								required: false
							});
							$('#massesName').val('');
						} else {
							$('#enterpriseNamePath').hide();
							$('#massesNamePath').show();
							$('#massesName').validatebox({
								required: true
							});
							$('#enterpriseName').validatebox({
								required: false
							});
							$('#enterpriseName').val('');
						}
					},
				});
			}
			
			if($('#reportObj').length > 0&&$("#dataSource").val()=='05') {
				AnoleApi.initTreeComboBox("reportObjStr", "reportObj", null, null, ["${reportFocus.reportObj!'1'}"], {
					DataSrc : [{"name":"企业", "value":"1"}, {"name":"群众", "value":"2"},{"name":"市直部门", "value":"3"}, {"name":"各级巡视巡察", "value":"4"}],
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						if(value == '1') {
							$('#massesNamePath').hide();
							$('#enterpriseNamePath').show();
							$('#reportWayPath').show();
							$('#openWorkPath').hide();
							$('#doOrgNamePath').hide();
							$('#enterpriseName').validatebox({
								required: true
							});
							$('#massesName').validatebox({
								required: false
							});
							$('#doOrgName').validatebox({
								required: false
							});
							$('#massesName').val('');
						} else if(value == '2'){
							$('#enterpriseNamePath').hide();
							$('#massesNamePath').show();
							$('#reportWayPath').show();
							$('#openWorkPath').hide();
							$('#doOrgNamePath').hide();
							$('#massesName').validatebox({
								required: true
							});
							$('#enterpriseName').validatebox({
								required: false
							});
							$('#doOrgName').validatebox({
								required: false
							});
							$('#enterpriseName').val('');
						}else if(value == '3'){
							$('#massesNamePath').hide();
							$('#enterpriseNamePath').hide();
							$('#reportWayPath').hide();
							$('#openWorkPath').show();
							$('#doOrgNamePath').hide();
							$('#doOrgName').validatebox({
								required: false
							});
							$('#enterpriseName').validatebox({
								required: false
							});
							$('#massesName').validatebox({
								required: false
							});
						}else if(value == '4'){
							$('#massesNamePath').hide();
							$('#enterpriseNamePath').hide();
							$('#reportWayPath').hide();
							$('#openWorkPath').hide();
							$('#doOrgNamePath').show();
							$('#doOrgName').validatebox({
								required: true
							});
							$('#enterpriseName').validatebox({
								required: false
							});
							$('#massesName').validatebox({
								required: false
							});
						}
					},
				});
			}
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveFFP(btnType) {
			var isValid =  $("#bpForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				MASSES_REPORT_DATASOURCE = "05",
				dataSource = $('#dataSource').val();
			/*
			if(isValid) {
				isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
				
				if(isValid && MASSES_REPORT_DATASOURCE != dataSource) {
					isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
				}
			}*/
			
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#bpForm").attr("action", "${rc.getContextPath()}/zhsq/reportBusPro/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#bpForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						if(isStart) {
							var	outerCallBack = "${reportFocus.callBack!}";
							if(outerCallBack){
								eval(outerCallBack)(data);
							}
							
							parent.searchData();
							parent.detail(data.reportUUID, data.instanceId, '2');
							if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
								parent.closeBeforeMaxJqueryWindow();
							}
						} else {
		  					parent.reloadDataForSubPage(data.tipMsg);
		  				}
					} else {
						$.messager.alert('错误', data.tipMsg, 'error');
					}
				});
			}
		}
		
		function showMap() {
			var callBackUrl = '${SQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
				width = 480, height = 360,
				gridId = "${defaultGridIdStr!'-1'}",
				markerOperation = $('#markerOperation').val(),
				id = $('#id').val(),
				mapType = 'BUSINESS_PROBLEM',
				isEdit = true,
				parameterJson = {
					'id' : $('#id').val(),
					'name' : $('#name').val()
				};
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
	    
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height(), winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				_winHeight = winHeight;
				_winWidth = winWidth;
				
				$('#bpForm .MC_con').height(winHeight - $('#bpForm .BigTool').outerHeight());
				$('#bpForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>