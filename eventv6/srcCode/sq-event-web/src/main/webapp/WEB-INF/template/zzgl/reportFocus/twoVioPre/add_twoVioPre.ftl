<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>两违事件-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
			.squareMeterUnit{width: 65px;}
		</style>
	</head>
	<body>
		<form id="twoVioPreForm" name="twoVioPreForm" action="" method="post">
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
			<input type="hidden" id="module" value="TWO_VIO_PRE"/>
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
								<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox" style="width: 67%;" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>业主姓名：</span></label>
								<input  id="personInvolved" name="personInvolved" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.personInvolved!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName">
									<span>
										<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>发生地址：
									</span>
								</label>
								<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="<#if reportFocus.dataSource == '' || reportFocus.dataSource == '01'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>报告方式：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
								<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
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
						<tr id="routineTr">
							<td class="LeftTd">
								<label class="LabName"><span>占地面积：</span></label>
								<input type="text" id="areaCovered" name="areaCovered" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:99999999.99,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.areaCovered!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>办理用地和规划手续：</span></label>
								<div class="zz-form" zz-form-filter="isRoutineDiv">
									<input type="hidden" id="isRoutine" name="isRoutine" value="${reportFocus.isRoutine!'0'}" />
									<input type="checkbox" zz-form-filter="isRoutineCheckBox" zz-text="是|否" <#if reportFocus.isRoutine?? && reportFocus.isRoutine=='1'>checked</#if>/>
								</div>
							</td>
						</tr>
						<tr id="lecPpnTr" style="<#if reportFocus.isRoutine?? && reportFocus.isRoutine == '1'><#else>display: none</#if>">
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>土地证号：</span></label>
								<input id="lecCode" name="lecCode" style="width: 310px;" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="<#if reportFocus.isRoutine?? && reportFocus.isRoutine == '1'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.lecCode!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>规划许可证号：</span></label>
								<input  id="ppnCode" name="ppnCode" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="<#if reportFocus.isRoutine?? && reportFocus.isRoutine == '1'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.ppnCode!}" />
							</td>
						</tr>
						<#if reportFocus.dataSource?? && reportFocus.dataSource == '03'>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>图斑编号：</span></label>
								<input type="text" class="inp1 easyui-validatebox autoWidth" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="mapNum" id="mapNum" value="${reportFocus.mapNum!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>耕地面积：</span></label>
								<input type="text" id="cultivableLandArea" name="cultivableLandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.cultivableLandArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>基本农田面积：</span></label>
								<input type="text" id="farmlandArea" name="farmlandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.farmlandArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>林地面积：</span></label>
								<input type="text" id="woodlandArea" name="woodlandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.woodlandArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>允许建设区面积：</span></label>
								<input type="text" id="constructableArea" name="constructableArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.constructableArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>公益林面积：</span></label>
								<input type="text" id="publicForestArea" name="publicForestArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.publicForestArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>条件建设区面积：</span></label>
								<input type="text" id="conditionalConstructionArea" name="conditionalConstructionArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.conditionalConstructionArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>城乡规划面积：</span></label>
								<input type="text" id="urpArea" name="urpArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.urpArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>限制建设区面积：</span></label>
								<input type="text" id="restrictedConstructionArea" name="restrictedConstructionArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.restrictedConstructionArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>村庄规划面积：</span></label>
								<input type="text" id="villagePlanningArea" name="villagePlanningArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.villagePlanningArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>生态红线面积：</span></label>
								<input type="text" id="ecologicalRedlineArea" name="ecologicalRedlineArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.ecologicalRedlineArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						</#if>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>建设状态：</span></label>
								<input type="hidden" id="constructionStatus" name="constructionStatus" value="${reportFocus.constructionStatus!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" id="constructionStatusName" value="" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>建（构）筑物用途：</span></label>
								<input type="hidden" id="buildingUsage" name="buildingUsage" value="${reportFocus.buildingUsage!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" id="buildingUsageName" value="" />
							</td>
						</tr>
						<tr id="conStatusDescribeTr" class="hide">
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>建设状态说明：</span>
								</label>
								<textarea name="conStatusDescribe" id="conStatusDescribe" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.conStatusDescribe!}</textarea>
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
					<a href="###" onclick="saveTwoVioPre(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveTwoVioPre(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
		
		<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			var dataSource = $('#dataSource').val(),
				locationOpt = {};
			
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			if(dataSource != '01') {
				locationOpt._limit_select_level = 5;
			}
			
			if(dataSource == '03') {
				$.extend(locationOpt, {
					_addressMap : {//编辑页面可以传这个参数，非必传参数
						_wgGisType : 'twoVioShape',
						_wgGisId : $('#reportId').val()
					}
				});
			}
			
			init4Location('occurred', locationOpt);
			
			AnoleApi.initListComboBox("constructionStatusName", "constructionStatus", "B210001001", function (dictValue, item) {
				var flag = isContainOther($('#constructionStatus').val());

				if(flag) {
					$('#conStatusDescribeTr').show();
				} else {
					$('#conStatusDescribeTr').hide();
					$('#conStatusDescribe').val('');
				}

			}, ["${reportFocus.constructionStatus!}"], {
				ShowOptions: {
					EnableToolbar : true
				},
				OnCleared: function() {
					$('#conStatusDescribeTr').hide();
					$('#conStatusDescribe').val('');
				}
			});
			AnoleApi.initListComboBox("buildingUsageName", "buildingUsage", "B210001002", null, ["${reportFocus.buildingUsage!}"], {
				ShowOptions: {
					EnableToolbar : true
				}
			});
			
			zzForm.render('checkbox','isRoutineDiv');
			zzForm.on('checkbox(isRoutineCheckBox)', function (data) {
				if(data.checked) {
					 $('#isRoutine').val('1');
					 $('#lecPpnTr').show();
					 $('#lecCode').validatebox({
						 required: true
					 });
					$('#ppnCode').validatebox({
						required: true
					});
				} else {
					$('#isRoutine').val('0');
					$('#lecPpnTr').hide();
					$('#lecCode').validatebox({
						required: false
					});
					$('#ppnCode').validatebox({
						required: false
					});
					$('#lecCode').val('');
					$('#ppnCode').val('');
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
				attachmentData: {attachmentType:'TWO_VIO_PRE'},
				module: 'twoVioPre',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {
				useType: 'add',
				fileExt: '.mp4,.avi,.amr',
				labelDict:[{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'TWO_VIO_PRE'},
				module: 'twoVioPre',
				individualOpt: {
					isUploadHandlingPic: true
				}
			};
			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict:[{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'TWO_VIO_PRE'},
				module: 'twoVioPre',
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

			$('#twoVioPreForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveTwoVioPre(btnType) {
			var isValid =  $("#twoVioPreForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				dataSource = $('#dataSource').val()||'';
				
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');

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
				
				$("#twoVioPreForm").attr("action", "${rc.getContextPath()}/zhsq/reportTwoVioPre/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#twoVioPreForm").ajaxSubmit(function(data) {
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
				mapType = 'TWO_VIO_PRE',
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
				
				$('#twoVioPreForm .MC_con').height(winHeight - $('#twoVioPreForm .BigTool').outerHeight());
				$('#twoVioPreForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		function isContainOther(dictValue) {
			var flag = false;

			if(isNotBlankStringTrim(dictValue) && dictValue == '99') {
				flag = true;
			}

			return flag;
		}
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>