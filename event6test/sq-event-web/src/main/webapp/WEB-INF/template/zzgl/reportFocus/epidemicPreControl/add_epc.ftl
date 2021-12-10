<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>疫情防控-新增/编辑</title>
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
		</style>
	</head>
	<body>
		<form id="epcForm" name="epcForm" action="" method="post">
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
			<input type="hidden" id="module" value="EPIDEMIC_PRE_CONTROL"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
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
							<#if reportFocus.collectSource?? && reportFocus.collectSource == '05'>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>医疗机构名称：</span></label>
								<input id="tipoffContent" name="tipoffContent" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width:170px;" data-options="required: true,validType:['maxLength[50]','characterCheck'],tipPosition:'bottom'" value="${reportFocus.tipoffContent!}" />
							</td>
							<#else>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label class="Asterik">*</label>管控类型：</span></label>
								<input type="hidden" id="kpcType" name="kpcType" value="${reportFocus.kpcType!}" />
								<input  id="kpcTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width:170px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
							</td>
							</#if>
						</tr>
						<tr>
							<#if reportFocus.collectSource?? && (reportFocus.collectSource=='02' || reportFocus.collectSource=='03' || reportFocus.collectSource=='04')>
								<td class="LeftTd">
									<label class="LabName"><span>数据来源：</span></label>
									<input  id="dataSourceName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
								</td>
							<#else>
								<td class="LeftTd">
									<label class="LabName"><span>报告方式：</span></label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
								</td>
							</#if>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>反馈时限：</span></label>
								<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:true, isShowToday:true})" value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>发生地址：</span></label><input type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label id="kpcNameAsterik" class="Asterik hide">*</label>人员姓名：</span></label>
								<input  id="kpcName" name="kpcName" type="text" class="inp1 easyui-validatebox autoRequired singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']" value="${reportFocus.kpcName!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label id="kpcTelAsterik" class="Asterik hide">*</label>联系电话：</span></label>
								<input  id="kpcTel" name="kpcTel" type="text" class="inp1 easyui-validatebox autoRequired singleCellInpClass" style="width: 170px;" data-options="tipPosition:'bottom',validType:'mobileorphone'" value="${reportFocus.kpcTel!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>证件类型：</span></label>
								<input type="hidden" id="kpcCardType" name="kpcCardType" value="${reportFocus.kpcCardType!}" />
								<input  id="kpcCardTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span><label id="kpcIdCardAsterik" class="Asterik hide">*</label>证件号码：</span></label>
								<input id="kpcIdCard" name="kpcIdCard" type="text" class="inp1 easyui-validatebox autoRequired singleCellInpClass" style="width:170px;" data-options="validType:['idcard'],tipPosition:'bottom'" value="${reportFocus.kpcIdCard!}" onblur="fetchAgeByIdCard();" />
							</td>
						</tr>
						<#--网格员采集-->
						<#if reportFocus.collectSource?? && reportFocus.collectSource == '01'>
							<tr>
								<td class="LeftTd">
									<label class="LabName"><span>交通方式：</span></label>
									<input type="hidden" id="trafficMode" name="trafficMode" value="${reportFocus.trafficMode!}" />
									<input  id="trafficModeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['characterCheck']" />
								</td>
								<td class="LeftTd">
									<label class="LabName labelNameWide"><span>入南时间：</span></label>
									<input type="text" id="arriveTime" name="arriveTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd', isShowClear:true, isShowToday:true})" value="${reportFocus.arriveTimeStr!}" readonly="readonly"></input>
								</td>
							</tr>
							<tr>
								<td class="LeftTd" colspan="2">
									<label class="LabName"><span>何地入南：</span></label>
									<input  id="kpcWhere" name="kpcWhere" placeholder="何地入南" type="text" style="width: 80%"
											class="inp1 easyui-validatebox singleCellInpClass"
											data-options="tipPosition:'bottom',validType:['maxLength[500]','characterCheck']" value="${reportFocus.kpcWhere!}" />
								</td>
							</tr>
						</#if>
						<#--非网格员采集-->
						<#if reportFocus.collectSource?? && (reportFocus.collectSource=='02' || reportFocus.collectSource=='03' || reportFocus.collectSource=='04')>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>来自何国：</span></label>
								<input type="hidden" id="kpcOrigin" name="kpcOrigin" value="${reportFocus.kpcOrigin!}" />
								<input  id="kpcOriginName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>年龄：</span></label>
								<input id="kpcAge" name="kpcAge" type="text" class="inp1 easyui-numberbox singleCellInpClass" style="height: 28px; width:170px;" data-options="min:0, max:999, readonly:true" value="<#if reportFocus.kpcAge??>${reportFocus.kpcAge?c}</#if>" />（周岁）
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>入南目的：</span></label>
								<input type="hidden" id="kpcGoal" name="kpcGoal" value="${reportFocus.kpcGoal!}" />
								<input  id="kpcGoalName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>入南时间：</span></label>
								<input type="text" id="arriveTime" name="arriveTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd', isShowClear:true, isShowToday:true})" value="${reportFocus.arriveTimeStr!}" readonly="readonly"></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>交通方式：</span></label>
								<input type="hidden" id="trafficMode" name="trafficMode" value="${reportFocus.trafficMode!}" />
								<input  id="trafficModeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;" data-options="tipPosition:'bottom',validType:['characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>交通班次：</span></label>
								<input id="trafficFrequency" name="trafficFrequency" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width:170px;" data-options="validType:['maxLength[100]','characterCheck'],tipPosition:'bottom'" value="${reportFocus.trafficFrequency!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>随行人员：</span></label><textarea name="kpcRetinue" id="kpcRetinue" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${reportFocus.kpcRetinue!}</textarea>
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
								<label class="LabName"><span>图片上传：</span></label><div id="bigFileUploadDiv"></div>
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
					<a href="###" onclick="saveEPC(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveEPC(0);" class="BigNorToolBtn SaveBtn">保存</a>
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
			
			//放置在组件初始化之前，以防止组件初始化后导致结构调整，从而影响宽度计算
			$('#epcForm .autoWidth').each(function() {
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
							<#if reportFocus.collectSource??>
								//来源是市外事组02、市疫情防控组03、市大数据组的04，允许选择到乡镇街道
								<#if reportFocus.collectSource == '02' || reportFocus.collectSource == '03' ||reportFocus.collectSource == '04' >
									AllowSelectLevel:"4,5,6"//选择到哪个层级
								<#elseif reportFocus.collectSource=='01' || reportFocus.collectSource==''>
									AllowSelectLevel:"6"//选择到哪个层级
								<#elseif reportFocus.collectSource!='01'>
									AllowSelectLevel:"5,6"//选择到哪个层级
								</#if>
							<#else>
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
				attachmentData: {attachmentType:'EPIDEMIC_PRE_CONTROL'},
				module: 'epidemicPreControl',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {	
					useType: 'add',
					fileExt: '.mp4,.avi,.amr',
					showFileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}],
					attachmentData: {attachmentType:'EPIDEMIC_PRE_CONTROL'},
					module: 'epidemicPreControl',
					individualOpt: {
						isUploadHandlingPic: true
					}
				};
			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				showFileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'EPIDEMIC_PRE_CONTROL'},
				module: 'epidemicPreControl',
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

			if($('#kpcTypeName').length > 0) {
				AnoleApi.initTreeComboBox("kpcTypeName", "kpcType", {
					"B210003002" : [${kpcTypeDictCodes!}]
				}, null, ["${reportFocus.kpcType!}"]);
			}
			
			//证件类型
			AnoleApi.initTreeComboBox("kpcCardTypeName", "kpcCardType", "D030001", null, ["${reportFocus.kpcCardType!'111'}"],
					{
						OnChanged : function(value, items) {onchange4CardType(value, items, true);}
					}
			);
			
			//数据来源
			if($('#dataSourceName').length > 0) {
				AnoleApi.initTreeComboBox("dataSourceName", "dataSource", "B210003004", null, ["${reportFocus.dataSource!}"], {
					ShowOptions: {
						EnableToolbar : true
					}
				});
			}
			
			if($('#kpcOriginName').length > 0) {
				AnoleApi.initTreeComboBox("kpcOriginName", "kpcOrigin", "D033004", null, ["${reportFocus.kpcOrigin!}"], {
					EnabledSearch : true,
					ShowOptions: {
						EnableToolbar : true
					}
				});
			}
			
			if($('#kpcGoalName').length > 0) {
				AnoleApi.initTreeComboBox("kpcGoalName", "kpcGoal", "B210003005", null, ["${reportFocus.kpcGoal!'99'}"]);
			}
			
			var trafficModeArray = [], trafficMode = "${reportFocus.trafficMode!}";
			
			if(trafficMode) {
				trafficModeArray = trafficMode.split(",");
			}
			
			if($('#trafficModeName').length > 0) {
				AnoleApi.initTreeComboBox("trafficModeName", "trafficMode", "B210003007", null, trafficModeArray, {
					RenderType : "01",
					ShowOptions: {
						EnableToolbar : true
					}
				});
			}
			
			onchange4CardType($('#kpcCardType').val());
			
			var isSmallMedical = <#if reportFocus.collectSource?? && reportFocus.collectSource=='05'>true<#else>false</#if>;
			$('#epcForm .autoRequired').each(function() {
				var itemId = $(this).attr('id');
				
				$('#' + itemId).validatebox({
					required: isSmallMedical
				});
				
				if(isSmallMedical) {
					$('#' + itemId + 'Asterik').show();
				} else {
					$('#' + itemId + 'Asterik').hide();
				}
			});
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveEPC(btnType) {
			var isValid =  $("#epcForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val();
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#epcForm").attr("action", "${rc.getContextPath()}/zhsq/reportEPC/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#epcForm").ajaxSubmit(function(data) {
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
				mapType = 'EPIDEMIC_PRE_CONTROL',
				isEdit = true,
				parameterJson = {
					'id' : $('#id').val(),
					'name' : $('#name').val()
				};
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
		
		function onchange4CardType(value, items, isClear) {
			if(value == '111') {
				$('#kpcIdCard').validatebox({
					validType:'idcard'
				});
			} else {
				$('#kpcIdCard').validatebox({
					validType:['maxLength[50]','characterCheck']
				});
			}
			
			fetchAgeByIdCard(isClear);
		}
		
		function fetchAgeByIdCard(isClear) {
			var kpcCardType = $('#kpcCardType').val(),
				kpcAge = '';
			
			if($('#kpcAge').length == 0) {
				return;
			}
			
			if(!isClear) {
				kpcAge = $('#kpcAge').numberbox('getValue');
			}
			
			if(kpcCardType == '111') {
				var kpcIdCard = $('#kpcIdCard').val();
				
				if(kpcIdCard) {
					kpcAge = fetchAgeByBirthday(fetchBirthdayByIdcard(kpcIdCard));
				}
				
				$('#kpcAge').numberbox('readonly', true);
			} else {
				$('#kpcAge').numberbox('readonly', false);
			}
			
			$('#kpcAge').numberbox('setValue', kpcAge);
		}
	    
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height(), winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				_winHeight = winHeight;
				_winWidth = winWidth;
				
				$('#epcForm .MC_con').height(winHeight - $('#epcForm .BigTool').outerHeight());
				$('#epcForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>