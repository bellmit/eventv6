<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>农村建房-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/residentSelector.js"></script>
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
		<form id="ruralHousing" name="ruralHousing" action="" method="post">
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
			<input type="hidden" id="module" value="RURAL_HOUSING"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
							
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
								<label class="LabName">
									<span><label class="Asterik">*</label>所属区域：</span>
								</label>
								<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
								<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<input type="hidden" id="partyId" name="partyId" value="<#if reportFocus.partyId??>${reportFocus.partyId?c}</#if>" />
								<input type="hidden" id="householder" name="householder" value="${reportFocus.householder!}" />
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>建房户姓名：</span></label>
								<input id="resident" type="text" class="inp1 easyui-validatebox inputWidth" style="cursor: pointer;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" value="${reportFocus.householder!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>报告方式：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
								<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>规划许可证编号：</span></label>
								<input  id="rcpCode" name="rcpCode" type="text" class="inp1 easyui-validatebox inputWidth" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.rcpCode!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>发生地址：</span></label>
								<input type="text" class="inp1 easyui-validatebox" style="width: 300px;" data-options="required: true,tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>宅基地批准书编号：</span></label>
								<input  id="rhaCode" name="rhaCode" type="text" class="inp1 easyui-validatebox inputWidth" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.rhaCode!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>证件类型：</span></label>
								<input type="hidden" id="rhCardType" name="rhCardType" value="${reportFocus.rhCardType!}" />
								<div id="rhCardTypeName" class="Check_Radio FontDarkBlue">${reportFocus.rhCardTypeName!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>证件号码：</span></label>
								<input type="hidden" id="rhIdCard" name="rhIdCard" value="${reportFocus.rhIdCard!}" />
								<div id="rhIdCardName" class="Check_Radio FontDarkBlue">${reportFocus.rhIdCard!}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
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
					<a href="###" onclick="saveRH(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveRH(0);" class="BigNorToolBtn SaveBtn">保存</a>
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
			
			init4Location('occurred');
			
			$('#resident').residentSelector({
				panelHeight : 300,
				panelWidth 	: 550,
				dataDomain 	: '${RS_DOMAIN!}',
				type 		: 'v6resident',
				relCode		: 'T',//户籍人口
				srchType	: '002',//模糊查找
				value		: {
					value	: "<#if reportFocus.partyId??>${reportFocus.partyId?c}</#if>",
					text	: "${reportFocus.householder!}"
				},
				onClickRow 	: function(index, row) {
					$("#resident").val(row.partyName);
					$('#partyId').val(row.partyId);
					$('#householder').val(row.partyName);
					$('#rhCardType').val(row.certType);
					$('#rhCardTypeName').html(row.certTypeCN);
					$('#rhIdCard').val(row.identityCard);
					$('#rhIdCardName').html(row.identityCard);
				},
				afterClear	: function(householder, partyId) {
					$('#partyId').val('');
					$('#householder').val('');
					$('#rhCardType').val('');
					$('#rhCardTypeName').html('');
					$("#rhIdCard").val('');
					$('#rhIdCardName').html('');
				}
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
							AllowSelectLevel:"6"//选择到哪个层级
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
				attachmentData: {attachmentType:'RURAL_HOUSING'},
				labelDict: [{'name':'签订告知书', 'value':'1'}],
				module: 'ruralHousing',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();

			var attachFileUploadOpt = {
						useType: 'add',
						fileExt: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
						attachmentData: {attachmentType:'RURAL_HOUSING'},
						labelDict: [{'name':'附件', 'value':'1'}],
						module: 'ruralHousing',
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
					eventSeq	: '1',
					bizId		: reportId,
					isBindBizId	: 'yes'
				});

				$.extend(attachFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(attachFileUploadOpt.attachmentData, {
					useType		: 'edit',
					eventSeq	: '1',
					bizId		: reportId,
					isBindBizId	: 'yes'
				});
			}
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			$('#ruralHousing .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveRH(btnType) {
			var isValid =  $("#ruralHousing").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val();
			
			if(isValid) {
				var partyId = $('#partyId').val();
				
				if(isBlankParam(partyId) || partyId <= 0) {
					$.messager.alert('警告', '请选择有效的建房户信息！', 'warning');
					return;
				}
			}
			
			if(isValid) {
				isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
				
				if(isValid) {
					var labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict,
						typeNameObj = {},
						option = {};
					
					if(labelDict) {
						for(var index in labelDict) {
							typeNameObj[labelDict[index].value] = labelDict[index].name;
						}
						
						option.typeNameObj = typeNameObj;
					}
					
					isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
				}
			}
			
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#ruralHousing").attr("action", "${rc.getContextPath()}/zhsq/reportRuralHousing/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#ruralHousing").ajaxSubmit(function(data) {
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
				mapType = 'RURAL_HOUSING',
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
				
				$('#ruralHousing .MC_con').height(winHeight - $('#ruralHousing .BigTool').outerHeight());
				$('#ruralHousing .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>