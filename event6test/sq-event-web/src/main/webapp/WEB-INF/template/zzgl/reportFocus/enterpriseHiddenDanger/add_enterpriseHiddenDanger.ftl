<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>企业安全隐患信息-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
		</style>
	</head>
	<body>
		<form id="ehdForm" name="ehdForm" action="" method="post">
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
			<input type="hidden" id="module" value="ENTERPRISE_HIDDEN_DANGER"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
							
			<div id="content-d" class="MC_con content light">
				<div id="eventWechatNorformDiv" class="NorForm">
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
								<label class="LabName">
									<span><label class="Asterik">*</label>所属区域：</span>
								</label>
								<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
								<input type="text" id="gridName" style="width: 54%" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>企业名称：</span></label>
								<input  id="enterpriseName" name="enterpriseName" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.enterpriseName!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>报告方式：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
								<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik <#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '06')><#else>hide</#if>">*</label>反馈时限：</span></label>
								<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="<#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '06')>required:true,</#if>tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:<#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '06')>false<#else>true</#if>, isShowToday:true})" value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>发生地址：
									</span>
								</label>
								<input type="text" class="inp1 easyui-validatebox singleCellClass autoWidth"
									   data-options="<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
									   name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide">
									<span>
										<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>地理标注：
									</span>
								</label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>存在隐患：</span></label>
								<input type="hidden" id="isHiddenDanger" name="isHiddenDanger" value="${reportFocus.isHiddenDanger!'1'}" />
								<div class="zz-form" zz-form-filter="isHiddenDangerDiv">
									<input type="checkbox" zz-form-filter="_isHiddenDangerDiv" <#if (reportFocus.isHiddenDanger?? && reportFocus.isHiddenDanger=='0')><#else>checked</#if>>
								</div>
							</td>
							<td id="hiddenDangerTypeTd" class="LeftTd <#if (reportFocus.isHiddenDanger?? && reportFocus.isHiddenDanger=='0')>hide</#if>">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>隐患类型：</span></label>
								<input type="hidden" id="hiddenDangerType" name="hiddenDangerType" value="${reportFocus.hiddenDangerType!}" />
								<input  id="hiddenDangerTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width:170px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
							</td>
						</tr>
						<tr id="hdtDescribeTr" class="hide">
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>隐患内容：</span></label><textarea name="hdtDescribe" id="hdtDescribe" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.hdtDescribe!}</textarea>
							</td>
						</tr>
						<#if reportFocus.dataSource?? && reportFocus.dataSource == '05'>
							<tr>
								<td class="LeftTd" colspan="2">
									<label class="LabName"><span><label class="Asterik">*</label>举报内容：</span></label><textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
								</td>
							</tr>
						</#if>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>图片上传：
									</span>
								</label>
								<div id="bigFileUploadDiv"></div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>视频上传：</span></label><div id="bigVideoUploadDiv"></div>
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
					<a href="###" onclick="saveEHD(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveEHD(0);" class="BigNorToolBtn SaveBtn">保存</a>
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
			
			$('#ehdForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			init4Location('occurred', {
				<#if reportFocus.dataSource?? && reportFocus.dataSource!='01'>
					//来源是非网格员只需要选择到村社区层级
					_limit_select_level : 5//选择到哪个层级
				</#if>
			});
			
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
			
			AnoleApi.initTreeComboBox("hiddenDangerTypeName", "hiddenDangerType", "B210002002", function(dictValue, item) {
				var flag = $('#hiddenDangerType').val() === '99';
				
				if(flag) {
					$('#hdtDescribeTr').show();
				} else {
					$('#hdtDescribeTr').hide();
					$('#hdtDescribe').val('');
				}
				
				$('#hdtDescribe').validatebox({
					required: flag
				});
			}, ["${reportFocus.hiddenDangerType!}"]);
			
			var bigFileUploadOpt = {
				useType: 'add',
				fileExt: '.jpg,.gif,.png,.jpeg,.webp',
				attachmentData: {attachmentType:'ENTERPRISE_HIDDEN_DANGER'},
				module: 'enterpriseHiddenDanger',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {	
					useType: 'add',
					fileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}],
					attachmentData: {attachmentType:'ENTERPRISE_HIDDEN_DANGER'},
					module: 'enterpriseHiddenDanger',
					individualOpt: {
						isUploadHandlingPic: true
					}
				};
			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'ENTERPRISE_HIDDEN_DANGER'},
				module: 'enterpriseHiddenDanger',
				individualOpt: {
					isUploadHandlingPic: true
				}
			};
			
			if(reportId) {
				$.extend(bigFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(bigFileUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1,2,3',
					bizId: reportId,
					isBindBizId: 'yes'
				});
				
				$.extend(bigViodeUploadOpt, {
					useType: 'edit'
				});
				$.extend(bigViodeUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1',
					bizId: reportId,
					isBindBizId: 'yes'
				});

				$.extend(attachFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(attachFileUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1',
					bizId: reportId,
					isBindBizId: 'yes'
				});
			}
			
			<#if reportFocus.attachmentIds??>
				bigFileUploadOpt["useType"] = 'edit'; 
				bigFileUploadOpt["attachmentData"].attachmentIds = "${reportFocus.attachmentIds!}";
			</#if>
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			zzForm.render('checkbox','isHiddenDangerDiv');
			zzForm.on('checkbox(_isHiddenDangerDiv)', function (data) {
				if(data.checked) {
					$('#isHiddenDanger').val('1');
					$('#hiddenDangerTypeTd').show();
				} else {
					$('#isHiddenDanger').val('0');
					$('#hiddenDangerTypeTd').hide();
					$('#hiddenDangerType').val('');
					$('#hiddenDangerTypeName').val('');
				}
				
				$('#hiddenDangerTypeName').validatebox({
					required: data.checked
				});
			});
	        
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveEHD(btnType) {
			var isValid =  $("#ehdForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				dataSource = $('#dataSource').val()||'';
				
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			if(isValid) {
				//只有网格层级的新增 发生地址、图片需要必填
				if(dataSource == '' || dataSource == '01'){
					if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
						$.messager.alert('警告', "请先完成地理标注！", 'warning');
						return;
					} else {
						isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
					}
				}
			}
			
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#ehdForm").attr("action", "${rc.getContextPath()}/zhsq/reportEHD/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#ehdForm").ajaxSubmit(function(data) {
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
				mapType = 'ENTERPRISE_HIDDEN_DANGER',
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
				
				$('#ehdForm .MC_con').height(winHeight - $('#ehdForm .BigTool').outerHeight());
				$('#ehdForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>