<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>三会一课详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		
		<style type="text/css">
			.singleCellClass{width: 68%;}
			.doubleCellClass{width: 88%}
			.labelNameWide{width: 132px;}
			.inputWidth{width: 170px;}
			.w300{width: 300px;}
			.ToolBar {
				border-top:0px;
				border-bottom:0px;
				background:#fff;
				padding: 6px 0px;/*解决表格跳动*/
				color:rgba(82,148,232,.8);
				height: 38px;
				zoom:1;
			}
			.Check_Radio {
				float:left;
				margin-top:3px;
				*margin-top:9px;
				word-break:break-all;
			}
			.datagrid-mask-msg {
				box-sizing:content-box
			}
			/*添加额外样式，解决由于受其他样式影响，详情页地图初始化后按钮底部阴影和换行*/
			div.ConSearch{
				box-shadow: 0 0 black
			}
			div.MainContent{
				box-sizing: content-box
			}
		</style>
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul id="topTitleUl" class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">三会一课信息</a></li>
							<#if instanceId??>
							<li><a href="##" divId="taskDetailDiv">处理环节</a></li>
							</#if>
							<#if feedbackCount?? && (feedbackCount > 0)>
								<li><a href="##" divId="feedbackListDiv">信息反馈列表</a></li>
							</#if>
						</ul>
					</div>
				</div>
				
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent" style="border-bottom: 1px solid #e5e5e5">
					<form id="tmolDetailForm" name="tmolDetailForm" action="" method="post" enctype="multipart/form-data">
						<input type="hidden" id="isStart" name="isStart" value="false" />
						<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
						<input type="hidden" name="isSaveResMarkerInfo" value="true" />
						<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
						<input type="hidden" id="reportType" name="reportType" value="${reportType!}" />
						<!--用于地图-->
						<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="name" name="name" value="" />
						<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
						<input type="hidden" id="module" value="MEETINGS_AND_LESSON"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<input type="hidden" id="collectSource" name="collectSource" value="${reportFocus.collectSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<input type="hidden" id="isCheckRegion" name="isCheckRegion" value="true" />
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						
						<div>
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
										</td>
									</tr>

									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>会议时间：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.meetingTimeStr!}</div>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>会议类型：</span></label>
											<input type="hidden" id="meetingType" name="meetingType" value="${reportFocus.meetingType!}" />
											<input  id="meetingTypeName" type="text" class="inp1 easyui-validatebox singleCellClass" style="width: 170px;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
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
									</tr>

									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>报告方式：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
											<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>报告编号：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>党组织名称：</span>
											</label>
											<input id="partyGroupName" name="partyGroupName" type="text" class="inp1 easyui-validatebox autoWidth" data-options="required:true,tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.partyGroupName!}" />
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label id="occurredAsterik" class="Asterik autoRequiredAsterik">*</label>发生地址：</span></label>
											<input type="text" class="inp1 easyui-validatebox autoRequired autoWidth" data-options="required: true,tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span>地理标注：</span></label>
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName">
												<span><label class="Asterik">*</label>应到会党员：</span>
											</label>
											<input type="text" style="height:28px;"  class="inp1 easyui-numberbox w300" data-options="min:1,max:999,required:true" name="shouldAttriveNum" id="shouldAttriveNum" value="<#if reportFocus?? && reportFocus.shouldAttriveNum??>${reportFocus.shouldAttriveNum?c}<#else>0</#if>"/>（人）
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide">
												<span><label class="Asterik">*</label>实到会党员：</span>
											</label>
											<input type="text" style="width:170px; height:28px;" class="inp1 easyui-numberbox singleCellInpClass" data-options="min:1,max:999,required:true" name="actualNumber" id="actualNumber" value="<#if reportFocus?? && reportFocus.actualNumber??>${reportFocus.actualNumber?c}<#else>0</#if>"  />（人）
										</td>
									</tr>
									<tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
										<td colspan="2">
											<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label id="bigFileUploadDivAsterik" class="Asterik autoRequiredAsterik">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
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
					</form>
					
					<!--办理信息-->
					<#if isAble2Handle?? && isAble2Handle>
					<div class="fw-det-tog fw-det-tog-n mt20">
						<div class="fw-det-tog-top">
							<h5><i style="background:#4f91ff;"></i>处置流程</h5>
						</div>
						<div class="fw-chul">
							<#include "/zzgl/event/workflow/handle_node_base.ftl" />
						</div>
					</div>
					</#if>
					
				</div>
				
				<#if instanceId??>
				<div id="taskDetailDiv" class="fw-main tabContent" style="padding-top: 3px;">
					<!-- 处理环节 -->
					<div id="workflowDetail" border="false"></div>
				</div>
				<#elseif listType?? && listType=='1'>
				<!--操作按钮-->
				<div id="btnDiv" class="btn-warp">
					<a class="btn-bon blue-btn" onclick="startWorkflow();">提交</a>
				</div>
				</#if>
				<#if feedbackCount?? && (feedbackCount > 0)>
					<div id="feedbackListDiv" class="fw-main tabContent"></div>
				</#if>
			</div>
		</div>
	</body>
	
	<script type="text/javascript">
		var basWorkSubTaskCallback = null,//存放原有的提交回调方法
			baseWorkRejectCallback = null,//存放原有的驳回方法
			detailFormJsonObject = null;
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				_winWidth = $(window).width(),
				bigFileUploadOpt = {
					useType			: 'edit',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'meetingsAndLesson',
					attachmentData	: {bizId: reportId, attachmentType:'MEETINGS_AND_LESSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				},
				bigVideoUploadOpt = $.extend({}, bigFileUploadOpt, {
					fileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}]
				}),
				attachFileUploadOpt = $.extend({}, bigFileUploadOpt, {
					fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					labelDict: [{'name':'处理前', 'value':'1'}]
				});
			
			$('#tmolDetailForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.90);
			});
			
			init4Location('occurred', {
				_startAddress :"${reportFocus.occurred!}",
				_startDivisionCode : "${reportFocus.regionCode!}" //默认选中网格，非必传参数
			});
			
			<#if isAble2Handle?? && isAble2Handle>
				var baseWorkOption = BaseWorkflowNodeHandle.initParam(),//获取默认的设置
					reportType = $('#reportType').val(),
					selectHandler = baseWorkOption.selectHandler;
				
				basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
				baseWorkRejectCallback = baseWorkOption.reject.rejectCallback;
				
				$.extend(selectHandler, {
					callback4Confirm : _capDynamicContent,
					userSelectedLimit : 1
				});
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportMeeting/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportMeeting/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportMeeting/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
					},
					evaluate: {
						isShowEva: false
					},
					initDefaultValueSet: initDefaultValueSet4Base,
					checkRadio: {
						radioCheckCallback: radioCheckCallback
					},
					selectHandler : selectHandler
				});
			</#if>
			
			AnoleApi.initTreeComboBox("meetingTypeName", "meetingType", {
				"B210006001" : [${meetingTypeDictCodes!}]
			}, null, ["${reportFocus.meetingType!}"]);
			
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
							<#if (isEditableNode?? && isEditableNode) || reportFocus.collectSource??>
								<#if reportFocus.collectSource=='02'>
									AllowSelectLevel:"6"
								<#elseif reportFocus.collectSource == '01' || reportFocus.collectSource == '03' || reportFocus.collectSource == '04'>
									//来源是村社区、乡镇街道的需要选择到村社区或者网格层级
									AllowSelectLevel:"5,6"
								<#elseif reportFocus.collectSource == '05' || reportFocus.collectSource == '06'>
									//市职部门选择到市级
									AllowSelectLevel:"3,4,5,6"
								</#if>
							<#else>
								AllowSelectLevel:"5,6"
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
			
			<#if feedbackCount?? && (feedbackCount > 0)>
				fetchFFPFeedback();
			</#if>
			
			init4FetchRemind();
			
			//顶部页签切换相应事件
			$('#topTitleUl').on('click', 'li a', function() {
				$('#topTitleUl > li > a').removeClass('active');
				
				$(this).addClass('active');
				
				var divId = $(this).attr('divId'),
					iframeItemList = $("#" + divId + " iframe"),
					iframeLen = iframeItemList.length,
					mainDivWidth = $(window).width() - 20 - 20;//左右边距20，样式container_fluid设置的
				var $winH = $(window).height(), $topH = $('#topTitleDiv').outerHeight(true), $btnH = $('#btnDiv').outerHeight(true);
				
				$('#' + divId).height($winH - $topH - $btnH);
				$('#' + divId).width(mainDivWidth + 10 + 10);
					
				if(divId == 'taskDetailDiv' && !$('#taskDetailDiv').hasClass('flowDetailLoaded')) {
					$('#taskDetailDiv').addClass('flowDetailLoaded');
					
					showWorkflowDetail();
					
					$('#workflowDetail').width(mainDivWidth);
				}
				
				if(iframeLen == 1) {
					var iframeItem = iframeItemList.eq(0),
							iframeLoaded = iframeItem.attr("_loadflag");
					if(isBlankStringTrim(iframeLoaded)) {
						iframeItem.attr("_loadflag", true);//用于防止重复加载
						iframeItem.attr('src', iframeItem.attr('iframeSrc'));//为了调整因页签切换而导致的列宽不足
					}
				}
				
				$('#formDiv div.tabContent').hide();
				$('#' + divId).show();
			});
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigVideoUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			$(window).on('load resize', function () {
				$winH = $(window).height();
				$topH = $('#topTitleDiv').outerHeight(true);
				$btnH = $('#btnDiv').outerHeight(true);
				
				$('#mainDiv').height($winH - $topH - $btnH);
				$('#taskDetailDiv').height($winH - $topH - $btnH);
				
				//滚动条初始化
				$("#mainDiv").niceScroll({
					cursorcolor:"rgba(0, 0, 0, 0.3)",
					cursoropacitymax:1,
					touchbehavior:false,
					cursorwidth:"4px",
					cursorborder:"0",
					cursorborderradius:"4px"
				});
			});
			
			detailFormJsonObject = $('#tmolDetailForm').serializeArray();
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function showMap() {
			var callBackUrl = '${SQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
				width = 480, height = 360,
				gridId = "${defaultGridIdStr!'-1'}",
				markerOperation = $('#markerOperation').val(),
				id = $('#id').val(),
				mapType = 'MEETINGS_AND_LESSON',
				isEdit = false;
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#tmolDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMeeting/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#tmolDetailForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						parent.searchData();
						parent.detail($('#reportUUID').val(), data.instanceId, '2');
						if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
							parent.closeBeforeMaxJqueryWindow();
						}
					} else {
						if(data.tipMsg) {
							$.messager.alert('错误', data.tipMsg, 'error');
						} else {
							$.messager.alert('错误', '操作失败！', 'error');
						}
					}
				});
			}
		}
		
		function radioCheckCallback(data) {//下一环节选中回调方法
			data = data || {};
			
			data = $.extend({
				'isClearDynamic' : true,
				'isClearAdviceNote' : true
			}, data);
			
			if(data.isClearDynamic === true) {
				dynamicConstructor(data);
			}
			
			if(data.isClearAdviceNote === true) {
				adviceNoteConstructor(data);
			}
			
			_handlerConstructor(data);
			autoRequiredRender();
			_itemSizeAdjust();
		}
		
		function dynamicConstructor(data) {//下一环节动态内容构造方法
			var dynamicContentMap = data.dynamicContentMap || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				isShowNumInput = dynamicContentMap.isShowNumInput || false,
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val();
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}
			
			if($('#dynamicAddress').length == 1) {
				$('#dynamicAddress').val('');
			}
			
			if(isShowDict) {
				if($('#dynamicContentDictTr').length == 0) {
					var dynamicDictJson = dynamicContentMap.dynamicDictJson || '',
						dictPcode = dynamicContentMap.dictPcode || '';
					
					$('#remarkTr').before(
						'<tr id="dynamicContentDictTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.dictLabelName + '：</span></label>' + 
								'<input type="hidden" id="dynamicDictId" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:\'bottom\'" id="dynamicDict" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					
					if(dictPcode) {
						AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", dictPcode, function(dictValue, item) {
							var dictName = '';

							if(item && item.length > 0) {
								dictName = item[0].name;
							}
							_capDynamicContent(dictName, dictValue);
						});
					} else if(dynamicDictJson) {
						AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", null, function(dictValue, item) {
							$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, null, {
							DataSrc: eval('(' + dynamicDictJson + ')')
						});
					}
				}
				
				$('#dynamicDict').validatebox({
					required: true
				});
				
				$('#dynamicContentDictTr').show();
			} else if($('#dynamicContentDictTr').length == 1) {
				$('#dynamicContentDictTr').hide();
				$('#dynamicDict').validatebox({
					required: false
				});
			}
			
			if(isShowNumInput) {
				$('#dynamicContentNumInputTr').remove();
				
				if($('#dynamicContentNumInputTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentNumInputTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.numInputLabelName + '：</span></label>' +
								'<input id="dynamicContentNumber" style="height: 28px;" class="inp1 autoDoubleCell" data-options="tipPosition:\'bottom\'"></input>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#dynamicContentNumber').numberbox({
						min : -2,
						max : 99,
						required: true,
						events : {
							blur: function() {
								_capDynamicContent($('#dynamicContentNumber').numberbox('getValue'), null, 5);
							}
						}
					});
				}
				
				$('#dynamicContentNumInputTr').show();
			} else if($('#dynamicContentNumInputTr').length == 1) {
				$('#dynamicContentNumInputTr').hide();
				$('#dynamicContentNumber').numberbox({
					required: false
				});
			}
			
			if(isShowText) {
				var formTypeId = '${formTypeId!}';
				
				$('#dynamicContentAreaTr').remove();
				
				if($('#dynamicContentAreaTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentAreaTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="dynamicContent" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
				}
				
				$('#dynamicContent').validatebox({
					required: true
				});
				
				$('#dynamicContentAreaTr').show();
			} else if($('#dynamicContentAreaTr').length == 1) {
				$('#dynamicContentAreaTr').hide();
				$('#dynamicContent').validatebox({
					required: false
				});
			}
			
			if(isShowNumInput || isShowText) {
				var winWidth = $(window).width();
				$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
					$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
						   .addClass("isSettledAutoWidth");
				});
			}
		}
		
		function adviceNoteConstructor(data) {//下一环节短信模板构造方法
			var adviceNote = data.adviceNote || '',
				adviceNoteInitial = data.adviceNoteInitial || '',
				dynamicContentMap = data.dynamicContentMap || {},
				dictValue = null,
				dynamicContent = null;
			
			if($('#dynamicDictId').length > 0) {
				dictValue = $('#dynamicDictId').val();
				
				if(dictValue) {
					adviceNote = dynamicContentMap["adviceNote_" + dictValue] || adviceNote;
					adviceNoteInitial = dynamicContentMap["adviceNoteInitial_" + dictValue] || adviceNoteInitial;
					dynamicContent = $('#dynamicDict').val();
				}
			}
			
			dynamicContent = $('#dynamicContent').val() || dynamicContent;
			
			if($('#adviceNote').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
			}
			if($('#adviceNoteInitial').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNoteInitial" value="" />');
			}
			
			var adviceNoteSeparate = null, adviceNoteInitialSeparate = null;
			for(var noteIndex = 0;; noteIndex++) {
				adviceNoteSeparate = dynamicContentMap["adviceNote_" + noteIndex];
				adviceNoteInitialSeparate = dynamicContentMap["adviceNoteInitial_" + noteIndex];
				if(adviceNoteSeparate) {
					if($('#adviceNote_' + noteIndex).length == 0) {
						$('#flowSaveForm').append('<input type="hidden" id="adviceNote_' + noteIndex + '" value="" />');
						$('#adviceNote_' + noteIndex).val(adviceNoteSeparate);
					}
					
					if(adviceNoteInitialSeparate) {
						if($('#adviceNoteInitial_' + noteIndex).length == 0) {
							$('#flowSaveForm').append('<input type="hidden" id="adviceNoteInitial_' + noteIndex + '" value="" />');
							$('#adviceNoteInitial_' + noteIndex).val(adviceNoteInitialSeparate);
						}
					}
				} else {
					break;
				}
			}
			
			$('#adviceNote').val(adviceNote);
			$('#adviceNoteInitial').val(adviceNoteInitial);
			
			_capDynamicContent(dynamicContent, dictValue);
		}
		
		function _subTaskOperate() {
			if($('#dynamicContentDictTr').length == 1 && $('#dynamicContentDictTr').is(':visible')) {
				$('#dynamicDict').validatebox({
					required: true
				});
			}
			
			if($('#dynamicContentAreaTr').length == 1 && $('#dynamicContentAreaTr').is(':visible')) {
				$('#dynamicContent').validatebox({
					required: true
				});
			}
			
			if($('#dynamicContentNumInputTr').length == 1 && $('#dynamicContentNumInputTr').is(':visible')) {
				$('#dynamicContentNumber').numberbox({
					required: true
				});
			}

			var isValid =  $("#flowSaveForm").form('validate');
			
			if(isValid) {
				isValid = $("#tmolDetailForm").form('validate');
				
				isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
						&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
				
				if(isValid) {
					var longitude = $('#x').val(),
						latitude = $('#y').val(),
						mapType = $('#mapt').val(),
						collectSource = $('#collectSource').val();
					
					//二级网格采集的 地理标注 处理前图片必填
					if(collectSource == '02') {
						if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
							$.messager.alert('警告', "请先完成地理标注！", 'warning');
							return;
						} else {
							isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
						}
					}
				}
				
				if(isValid) {
					var shouldAttriveNum = parseInt($('#shouldAttriveNum').val(), 10);
					var actualNumber = parseInt($('#actualNumber').val(), 10);
					
					if(actualNumber > shouldAttriveNum) {
						$.messager.alert('提示', '应到会党员应大于等于实到会党员！', 'info');
						return;
					}
				}
				
				if(formCompare4TMOL() == false) {
					if(isValid) {
						modleopen();
						
						$("#tmolDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMeeting/saveReportFocus.jhtml");
						
						$("#tmolDetailForm").ajaxSubmit(function(data) {
							modleclose();
		
							if(data.success && data.success == true) {
								var isRegionNotChanged = specificFormAttrCompare(detailFormJsonObject, $('#tmolDetailForm').serializeArray(), {'regionCode' : ''}),
									compareFlag = specificFormCompare4TMOL();
								
								if(isRegionNotChanged == true && compareFlag == true) {
									if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
										basWorkSubTaskCallback();
									}
								} else {
									var checkedRadio = $('#tr_epath input[type=radio][name=nextNode]:checked').eq(0),
										isClearNextUser = false;
									
									if(isRegionNotChanged === false) {
										isClearNextUser = true;
									}
									
									if(checkedRadio.length > 0) {
										BaseWorkflowNodeHandle.checkRadio(checkedRadio.eq(0), {
											isClearNextUser: isClearNextUser,
											isClearDynamic : false
										});
									}
								}	
							} else {
								$.messager.alert('错误', data.tipMsg, 'error');
							}
						});
					}
				} else if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
					basWorkSubTaskCallback();
				}
			}
		}
		
		function _rejectOperate() {
			if($('#dynamicContentDictTr').length == 1) {
				$('#dynamicDict').validatebox({
					required: false
				});
			}
			
			if($('#dynamicContentAreaTr').length == 1) {
				$('#dynamicContent').validatebox({
					required: false
				});
			}
			
			if($('#dynamicContentNumInputTr').length == 1) {
				$('#dynamicContentNumber').numberbox({
					required: false
				});
			}
				
			if(baseWorkRejectCallback != null && typeof baseWorkRejectCallback === 'function') {
				baseWorkRejectCallback();
			}
		}
		
		function _checkSelectUser() {
			var selectUserIds = "", selectOrgIds = "";
			var userCheckbox = $("input[name='htmlSelectUserCheckbox'][type='checkbox']:checked");
			var allCheckBoxLen = $("input[name='htmlSelectUserCheckbox'][type='checkbox']").length;
			var checkedBoxLen = userCheckbox.length;
			
			if(userCheckbox.length > 0) {
				userCheckbox.each(function() {
					selectUserIds += "," + $(this).attr("userid");
					selectOrgIds += "," + $(this).attr("orgid");
				});
				
				if(selectUserIds.length > 0) {
					selectUserIds = selectUserIds.substr(1);
				}
				
				if(selectOrgIds.length > 0) {
					selectOrgIds = selectOrgIds.substr(1);
				}
			}
			
			$("#htmlSelectUserCheckAll").attr('checked',allCheckBoxLen == checkedBoxLen);
			
			$('#selectOrgIds').val(selectOrgIds);
			$('#selectUserIds').val(selectUserIds);
		}
		
		function _checkAllSelectUser() {
			var isCheckAll = $("#htmlSelectUserCheckAll").attr("checked");
			var userCheckbox = $("input[name='htmlSelectUserCheckbox'][type='checkbox']");
			
			for(var index = 0, len = userCheckbox.length; index < len; index++){
				userCheckbox[index].checked = isCheckAll;
			}
			
			_checkSelectUser();
		}
		
		function _capDynamicContent(dynamicContent, dictValue, dynamicType) {
			var adviceNote = $('#adviceNote').val(),
				advice = '',
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val(),
				formTypeId = '${formTypeId!}',
				dynamicTextContent = dynamicContent;
				dynamicNumContent = '';
			
			if($('#dynamicContentNumber').length > 0) {
				dynamicNumContent = $('#dynamicContentNumber').numberbox('getValue');
			}
			
			if(dynamicType == 5) {
				dynamicTextContent = $('#dynamicContent').val();
			}
			
			if(adviceNote) {
				var dynamicContentLabel = '@dynamicContent@',
					dynamicContentNextOrgNameLabel = '@nextOrgNames@',
					dynamicContentNumLabel = '@dynamicNumber@';
				
				advice = adviceNote;
				
				if(advice.indexOf(dynamicContentLabel) && dynamicTextContent) {
					advice = advice.replaceAll(dynamicContentLabel, dynamicTextContent);
				}
				
				if(advice.indexOf(dynamicContentNumLabel) >= 0 && dynamicNumContent) {
					advice = advice.replaceAll(dynamicContentNumLabel, dynamicNumContent);
				}
				
				if(adviceNote.indexOf(dynamicContentNextOrgNameLabel) >= 0) {
					var nextOrgNames = $('#nextOrgNames').val();
					
					if(nextOrgNames) {
						advice = advice.replaceAll(dynamicContentNextOrgNameLabel, nextOrgNames);
					}
				}
				
				if(advice.indexOf(dynamicContentLabel) >= 0 
					|| advice.indexOf(dynamicContentNextOrgNameLabel) >= 0
					|| advice.indexOf(dynamicContentNumLabel) >= 0) {
					advice = '';
				}
			}
			
			$('#advice').val(advice);
		}
	
		function showWorkflowDetail() {//流程详情
			var instanceId = "${instanceId!}";
			if(instanceId) {
				modleopen();
				$("#workflowDetail").panel({
					height:'auto',
					width:'auto',
					overflow:'no',
					href: "${rc.getContextPath()}/zhsq/reportMeeting/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
					onLoad:function(){//配合detail_workflow.ftl使用
						var workflowDetailWidth = $("#workflowDetail").width() - 10 - 10;//10px分别为左右侧距离
						var maxHandlePersonAndTimeWidth = workflowDetailWidth * 0.4;//人员办理意见的最大宽度，为了使人员信息过长时，办理意见不分行
						var taskListSize = $("#taskListSize").val();	//任务记录数
						var handleTaskNameWidth = 115;		//处理环节总宽度
						var handleLinkWidth = 21;			//办理环节宽度
						var handlePersonAndTimeWidth = 0;	//办理人/办理时间宽度
						var handleRemarkWidth = 0;			//办理意见宽度
						
						var remindSize = 0;					//催办记录数
						var remindPersonAndTimeWidth = 0;	//催办人和催办时间宽度
						var remindRemarkWidth = 0;			//催办意见宽度
						
						for(var index = 0; index < taskListSize; index++){
							remindSize = $("#remindListSize_"+index).val();//催办记录数
							remindPersonAndTimeWidth = 0;
							remindRemarkWidth = 0;
							
							handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
							
							if(handlePersonAndTimeWidth > maxHandlePersonAndTimeWidth) {
								$("#handlePersonAndTime_"+index).width(maxHandlePersonAndTimeWidth);
								handlePersonAndTimeWidth = $("#handlePersonAndTime_"+index).outerWidth();
							}
							
							handleRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - handlePersonAndTimeWidth;
							
							$("#handleRemark_"+index).width(handleRemarkWidth);//办理意见宽度
							
							for(var index_ = 0; index_ < remindSize; index_++){
								remindPersonAndTimeWidth = $("#remindPersonAndTime_"+index+"_"+index_).outerWidth();
								remindRemarkWidth = workflowDetailWidth - handleTaskNameWidth - handleLinkWidth - remindPersonAndTimeWidth;
								$("#remindRemark_"+index+"_"+index_).width(remindRemarkWidth);//催办意见宽度
							}
						}
						
						adjustSubTaskWidth();//调整子任务(会签任务和处理中任务)办理意见宽度
						
						modleclose();
					}
				});
			}
		}
		
		function autoRequiredRender() {
			var curNodeName = $('#collectSource').val(),
				isRequired = false;
			
			if(curNodeName == '02') {
				isRequired = true;
			}
			
			autoRequiredBase('tmolDetailForm', isRequired);
		}
		
		function formCompare4TMOL() {
			var detailFormJsonObjectNow = $('#tmolDetailForm').serializeArray();
			var compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow);
			
			return compareFlag;
		}
		
		function specificFormCompare4TMOL() {
			var detailFormJsonObjectNow = $('#tmolDetailForm').serializeArray(),
				adviceNoteInitial = $('#adviceNoteInitial').val(),
				extraSpecificAttrId = {},
				specificAttrId = null,
				compareFlag = false;
			
			if(adviceNoteInitial.indexOf("@partyGroupName@") >= 0) {
				extraSpecificAttrId.partyGroupName = '';
			}
			if(adviceNoteInitial.indexOf("@shouldAttriveNum@") >= 0) {
				extraSpecificAttrId.shouldAttriveNum = '';
			}
			if(adviceNoteInitial.indexOf("@actualNumber@") >= 0) {
				extraSpecificAttrId.actualNumber = '';
			}
			
			specificAttrId = $.extend(extraSpecificAttrId, _capAdviceNoteAssociatedAttr(adviceNoteInitial));
			compareFlag = specificFormAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, specificAttrId);
				
			detailFormJsonObject = detailFormJsonObjectNow;
			
			return compareFlag;
		}
		
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchFFPFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>