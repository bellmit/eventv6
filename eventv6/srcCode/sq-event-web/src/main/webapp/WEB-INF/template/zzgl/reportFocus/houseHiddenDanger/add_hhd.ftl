<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>房屋安全隐患事件-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
		</style>
	</head>
	<body>
		<form id="hhdForm" name="hhdForm" action="" method="post">
			<input type="hidden" id="isStart" name="isStart" value="false" />
			<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
			<input type="hidden" name="isSaveResMarkerInfo" value="true" />
			<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" name="reportUUID" value="${reportFocus.reportUUID!}" />
			<input type="hidden" name="reportType" value="${reportType!}" />
			<input type="hidden" id="riskType" name="riskType" value="${reportFocus.riskType!}" />
			
			<!--用于地图-->
			<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" id="name" name="name" value="" />
			<input type="hidden" id="markerOperation" name="markerOperation" value="2"/>
			<input type="hidden" id="module" value="HOUSE_HIDDEN_DANGER"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
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
								<input type="text" id="gridName" style="width: 300px;" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')><label class="Asterik">*</label></#if>反馈时限：</span></label>
								<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="<#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')>required:true,</#if>tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', <#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')>isShowClear:false, <#else>isShowClear:true, </#if>isShowToday:false})" value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
							</td>
						</tr>
						<tr class="<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'><#else>hide</#if>">
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>楼栋名称：</span></label>
								<input type="hidden" id="buildingId" name="buildingId" value="<#if reportFocus.buildingId??>${reportFocus.buildingId?c}</#if>" />
								<input type="hidden" id="buildingName" name="buildingName" value="${reportFocus.buildingName!}" />
								<input type="text" id="hhdBuilding" class="comboselector" style="height: 28px; width: 310px;" data-options="<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'>required:true,</#if>dType:'building', afterSelect:combo_afterSelect, value: '<#if reportFocus.buildingId??>${reportFocus.buildingId?c}</#if>'" query-params="orgCode=${reportFocus.regionCode!'-1'}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
					<#if reportFocus.dataSource?? && reportFocus.dataSource == '07'>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>企业/个体户：</span></label>
								<input  id="enterpriseName" name="enterpriseName" placeholder="企业/个体工商户名称" type="text" style="width: 80%" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.enterpriseName!}" />
							</td>
						</tr>
					</#if>
						<tr>
							<td class="LeftTd">
								<label class="LabName">
									<span><label class="Asterik">*</label>楼栋地址：</span>
								</label>
								<#--网格员上报 并且 不是 突发的-->
								<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'>
									<input type="hidden" name="occurred" id="occurred" value="${reportFocus.occurred!}"/>
									<div id="occurredLabel" class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.occurred!}</div>
								<#else >
									<input type="text" class="inp1 easyui-validatebox" style="width: 300px;"
										   data-options="required:true,tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
										   name="occurred" id="occurred" value="${reportFocus.occurred!}" />
								</#if>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>隐患类别：</span></label>
								<input type="hidden" id="hhdType" name="hhdType" value="${reportFocus.hhdType!}" />
								<input id="hhdTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width:170px;" data-options="required:true,tipPosition:'bottom'" />
							</td>
						</tr>
						<tr id="riskDescribeTr" class="hide">
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>隐患说明：</span></label><textarea name="riskDescribe" id="riskDescribe" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.riskDescribe!}</textarea>
							</td>
						</tr>
						<#if reportFocus.dataSource?? && reportFocus.dataSource == '02'>
							<tr>
								<td class="LeftTd" colspan="2">
									<label class="LabName"><span><label class="Asterik">*</label>举报内容：</span></label><textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
								</td>
							</tr>
						</#if>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<#--网格层级采集 处理前图片必传-->
										<#if (reportFocus.riskType == '' || reportFocus.riskType == '0') && (reportFocus.dataSource == '01' || reportFocus.dataSource == '')><label class="Asterik">*</label></#if>图片上传：
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
					<a href="###" onclick="saveHHD(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveHHD(0);" class="BigNorToolBtn SaveBtn">保存</a>
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

			<#if reportFocus.dataSource != '01' || reportFocus.riskType == '3'>
				init4Location('occurred', {});
			</#if>

			//加载网格
			AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							var regionCode = items[0].orgCode;

							$("#regionCode").val(regionCode);
							reInitBuilding(regionCode);
						}
					},
					{
						ChooseType:"1",
						ShowOptions:{
							//来源是网格的 选择到网格层级
							<#if (reportFocus.riskType == '' || reportFocus.riskType == '0') && (reportFocus.dataSource == '01' || reportFocus.dataSource == '')>
								AllowSelectLevel:"6"//选择到哪个层级
							<#elseif reportFocus.dataSource == '02' || reportFocus.dataSource == '03' || reportFocus.dataSource == '05' || reportFocus.dataSource == '07' ||reportFocus.riskType == '3'>
								//来源是非网格员最高可以选择到乡镇街道层级
								AllowSelectLevel:"4,5,6"//选择到哪个层级
							<#else >
								AllowSelectLevel:"5,6"//选择到哪个层级
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
				attachmentData: {attachmentType:'HOUSE_HIDDEN_DANGER'},
				module: 'houseHiddenDanger',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {	
					useType: 'add',
					fileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}],
					attachmentData: {attachmentType:'HOUSE_HIDDEN_DANGER'},
					module: 'houseHiddenDanger',
					individualOpt: {
						isUploadHandlingPic: true
					}
				};
			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'HOUSE_HIDDEN_DANGER'},
				module: 'houseHiddenDanger',
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
			
			var hhdTypeArray = [], hhdType = "${reportFocus.hhdType!}";
			
			if(hhdType) {
				hhdTypeArray = hhdType.split(",");
			}
			
			AnoleApi.initTreeComboBox("hhdTypeName", "hhdType", "B210004003", function(dictValue, item) {
				var flag = isContainOther($('#hhdType').val());
				
				if(flag) {
					$('#riskDescribeTr').show();
				} else {
					$('#riskDescribeTr').hide();
					$('#riskDescribe').val('');
				}
				
				$('#riskDescribe').validatebox({
					required: flag
				});
			}, hhdTypeArray, {
				RenderType : "01"
			});
            
			$('#hhdForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});

			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveHHD(btnType) {
			var isValid =  $("#hhdForm").form('validate'),
				buildingId = $('#buildingId').val(),
				riskType = $('#riskType').val() || '',
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				dataSource = $('#dataSource').val()||'';
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');

			if(isValid) {
				if(dataSource == '01') {
					if(isBlankParam(buildingId)){
						$.messager.alert('警告', "请选择有效的楼栋信息！", 'warning');
						return;
					}
					if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)){
						$.messager.alert('警告', "楼栋地址未获取到经纬度，请定位！", 'warning');
						return;
					}
				}else if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)){
					$.messager.alert('警告', "请完成地理标注！", 'warning');
					return;
				}
				//房屋安全隐患来源为网格的 新增/编辑 才进行处理前附件的判断
				if((riskType == '' || riskType == '0') && (dataSource == '' || dataSource == '01')) {
					isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
				}
			}
			
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#hhdForm").attr("action", "${rc.getContextPath()}/zhsq/reportHHD/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#hhdForm").ajaxSubmit(function(data) {
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
				mapType = '5',
				isEdit = true,
				parameterJson = {
					'id' : $('#id').val(),
					'name' : $('#name').val()
				};

			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
		
		function combo_afterSelect(data, target) {
			var buildingId = '', buildingName = '', buildingAddress = '',
				isLocated = '', latitude = '', longitude = '', mapType = '';
			
			if(data) {
				if(data == '') {//清空选择值
					var regionCode = $('#regionCode').val() || "${reportFocus.regionCode!'-1'}";
					
					reInitBuilding(regionCode);
				} else {
					buildingId = data.buildingId;
					buildingName = data.buildingName;
					buildingAddress = data.buildingAddress;
					isLocated = data.tdNewMarker || '';
					
					if(isLocated == '1') {
						latitude = data.x || '';
						longitude = data.y || '';
						mapType = '5';
					}
				}
			}
			
			$('#buildingId').val(data.buildingId);
			$('#buildingName').val(data.buildingName);
			$('#occurred').val(buildingAddress);
			$('#occurredLabel').html(buildingAddress);

			if(isBlankStringTrim(latitude) || isBlankStringTrim(longitude) ||　isBlankStringTrim(mapType)){
				$('#x').val('${reportFocus.resMarker.x}');
				$('#y').val('${reportFocus.resMarker.y}');
				$('#mapt').val('${reportFocus.resMarker.mapType}');
			}else{
				$('#x').val(latitude);
				$('#y').val(longitude);
				$('#mapt').val(mapType);
			}
		}
		
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		function reInitBuilding(regionCode) {
			var options = $("#hhdBuilding").comboselector("options"),
				queryParams = options["query-params"];
			
			regionCode = regionCode || '-1';
			
			queryParams = queryParams || {};
			queryParams.orgCode = regionCode;
			
			$('#hhdBuilding').comboselector("clear");//会回调 combo_afterSelect
			$('#hhdBuilding').comboselector('iniQueryParams', queryParams);
		}
		
		function isContainOther(dictValue) {
			var flag = false;
			
			if(isNotBlankStringTrim(dictValue)) {
				var dictArray = dictValue.split(','),
					DICT_OTHER = '99';
				
				for(var index in dictArray) {
					if(dictArray[index] === DICT_OTHER) {
						flag = true; break;
					}
				}
			}
			
			return flag;
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height(), winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				_winHeight = winHeight;
				_winWidth = winWidth;
				
				$('#hhdForm .MC_con').height(winHeight - $('#hhdForm .BigTool').outerHeight());
				$('#hhdForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
	</script>
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>