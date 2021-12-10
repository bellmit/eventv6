<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>扶贫走访信息-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
		
		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		
		<!--贫困户人员选择 开始-->
		<script src="${uiDomain!''}/web-assets/plugins/layui-v2.4.5/layui/layui.all.js"></script>
		<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/custom_msgClient.js "></script>
		<script src="${uiDomain!''}/web-assets/extend/person-select/help-people-select/js/ffcs.select.helppeople.js"></script>
		<!--贫困户人员选择 开始-->
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
		</style>
	</head>
	<body>
		<form id="psvForm" name="psvForm" action="" method="post">
			<input type="hidden" id="isStart" name="isStart" value="false" />
			<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
			<input type="hidden" name="isSaveResMarkerInfo" value="true" />
			<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" name="reportUUID" value="${reportFocus.reportUUID!}" />
			<input type="hidden" name="reportType" value="${reportType!}" />
			<input type="hidden" name="riskType" value="${reportFocus.riskType!}" />
			
			<!--用于地图-->
			<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" id="name" name="name" value="" />
			<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
			<input type="hidden" id="module" value="POOR_SUPPORT_VISIT"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<input type="hidden" id="collectSource" name="collectSource" value="${reportFocus.collectSource!}"/>

			<#--报告方式-->
			<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
							
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
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>贫困户姓名：</span></label>
								<input type="hidden" id="poorVillagerId" name="poorVillagerId" value="<#if reportFocus.poorVillagerId??>${reportFocus.poorVillagerId?c}</#if>" />
								<input type="text" id="poorVillagerName" name="poorVillagerName" class="inp1 easyui-validatebox mailReceive singleCellClass" style="width:170px; cursor: pointer;" data-options="required:true,tipPosition:'bottom'" onclick="initPSVComponent();" value="${reportFocus.poorVillagerName!}" readonly></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<#if reportFocus.collectSource == '' || reportFocus.collectSource == '01'><label class="Asterik">*</label></#if>发生地址：
									</span>
								</label>
								<input type="text" class="inp1 easyui-validatebox autoWidth"
									   data-options="<#if reportFocus.collectSource == '' || reportFocus.collectSource == '01'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
									   name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide">
									<span>
										<#if reportFocus.collectSource == '' || reportFocus.collectSource == '01'><label class="Asterik">*</label></#if>地理标注：
									</span>
								</label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<#if reportFocus.collectSource == '' || reportFocus.collectSource == '01'><label class="Asterik">*</label></#if>图片上传：
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
					<a href="###" onclick="savePSV(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="savePSV(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
		
		<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	</body>
	
	<script type="text/javascript">
		var _psvSelector = null;//贫困户人员选择操作对象
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			$('#psvForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			init4Location('occurred', {
				<#if reportFocus.collectSource?? && reportFocus.collectSource!='01'>
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
							<#if reportFocus.collectSource?? && reportFocus.collectSource!='01'>
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
				attachmentData: {attachmentType:'POOR_SUPPORT_VISIT'},
				module: 'poorSupportVisit',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {	
					useType: 'add',
					fileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}],
					attachmentData: {attachmentType:'POOR_SUPPORT_VISIT'},
					module: 'poorSupportVisit',
					individualOpt: {
						isUploadHandlingPic: true
					}
				};
			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'POOR_SUPPORT_VISIT'},
				module: 'poorSupportVisit',
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
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function savePSV(btnType) {
			var isValid =  $("#psvForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				collectSource = $('#collectSource').val()||'';
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			if(isValid) {
				var poorVillagerId = $('#poorVillagerId').val();
				
				if(isBlankParam(poorVillagerId)) {
					$.messager.alert('警告', "请先完成贫困户选择！", 'warning');
					return;
				}

				//二级网格员 地理标注 处理前图片必填
				if(collectSource == '' || collectSource == '01'){
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
				
				$("#psvForm").attr("action", "${rc.getContextPath()}/zhsq/reportPSV/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#psvForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						if(isStart) {
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
				mapType = 'POOR_SUPPORT_VISIT',
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
		
		function initPSVComponent() {
			var opt = {
				//layer的相关配置
				layer: {
					area: ['100%', '100%'], //宽高
				},
				postParam: {
					regionCode: '${defaultRegionCode!-1}',
					themesStyle: '${UI_STYLE!''}'
				},
				context: '${COMPONENTS_URL!}'
			}
			
			if(_psvSelector != null) {
				_psvSelector.wapSelectEvent('cancelEvent');
			}
			
			_psvSelector = $('#poorVillagerName').initSelect(opt, function (data) {
				if(data) {
					$('#poorVillagerId').val(data.poorHoldId);
					$('#poorVillagerName').val(data.poorHoldName);
				}
			});
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height(), winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				_winHeight = winHeight;
				_winWidth = winWidth;
				
				$('#psvForm .MC_con').height(winHeight - $('#psvForm .BigTool').outerHeight());
				$('#psvForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>