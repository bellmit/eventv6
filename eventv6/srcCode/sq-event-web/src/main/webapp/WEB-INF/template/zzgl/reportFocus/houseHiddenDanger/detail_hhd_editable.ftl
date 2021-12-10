<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>房屋安全隐患事件详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<script type="text/javascript" src="${SQ_ZZGRID_URL}/es/component/comboselector/clientJs.jhtml"></script>
		
		<style type="text/css">
			.singleCellClass{width: 55%;}
			.doubleCellClass{width: 88%}
			.labelNameWide{width: 132px;}
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
							<li><a href="##" divId="mainDiv" class="active">房屋安全隐患信息</a></li>
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
					<form id="hhdDetailForm" name="hhdDetailForm" action="" method="post">
						<input type="hidden" id="isStart" name="isStart" value="false" />
						<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
						<input type="hidden" name="isSaveResMarkerInfo" value="true" />
						<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
						<input type="hidden" id="reportType" name="reportType" value="${reportFocus.reportType!}" />
						<input type="hidden" id="riskType" name="riskType" value="${reportFocus.riskType!}" />
						<!--用于地图-->
						<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="name" name="name" value="" />
						<input type="hidden" id="markerOperation" name="markerOperation" value="2"/>
						<input type="hidden" id="module" value="HOUSE_HIDDEN_DANGER"/>
						<input type="hidden" id="isUploadCasePic" value="false"/>
						<input type="hidden" id="isUploadPuniPic" value="false"/>
						<#--是否上传处理后图片-->
						<input type="hidden" id="isUploadHandledPic" value="${isUploadHandledPic!'false'}"/>
						<input type="hidden" id="picTypeName" value=""/>
						<input type="hidden" id="isUploadNewFile" value=""/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<input type="hidden" id="isCheckRegion" name="isCheckRegion" value="true" />
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						<input type="hidden" id="invalidTimeStamp" name="invalidTimeStamp" value="" />
						
						<div>
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
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>报告方式：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!}</div>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>报告编号：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName">
												<span><label class="Asterik">*</label>所属区域：</span>
											</label>
											<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
											<input type="text" id="gridName" style="width: 250px;" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span><#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')><label class="Asterik">*</label></#if>反馈时限：</span></label>
											<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass" data-options="<#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')>required:true,</#if>tipPosition:'bottom'" style="width:170px; cursor:pointer;" onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', <#if reportFocus.dataSource?? && (reportFocus.dataSource == '04' || reportFocus.dataSource == '05' || reportFocus.dataSource == '06' || reportFocus.dataSource == '07')>isShowClear:false, <#else>isShowClear:true, </#if>isShowToday:false})" value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
										</td>
									</tr>
									<#--只有网格员上报的才显示-->
									<tr class="<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'><#else >hide</#if>">
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>楼栋名称：</span></label>
											<input type="hidden" id="buildingId" name="buildingId" value="<#if reportFocus.buildingId??>${reportFocus.buildingId?c}</#if>" />
											<input type="hidden" id="buildingName" name="buildingName" value="${reportFocus.buildingName!}" />
											<input type="text" id="hhdBuilding" class="comboselector" style="height: 28px; width: 250px;" data-options="<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'>required:true,</#if>dType:'building', afterSelect:combo_afterSelect, value: '<#if reportFocus.buildingId??>${reportFocus.buildingId?c}</#if>'" query-params="orgCode=${reportFocus.regionCode!'-1'}" />
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
											<label class="LabName"><span><label class="Asterik">*</label>楼栋地址：</span></label>
											<#if reportFocus.dataSource == '01' && reportFocus.riskType != '3'>
												<input type="hidden" name="occurred" id="occurred" value="${reportFocus.occurred!}"/>
												<div id="occurredLabel" class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.occurred!}</div>
											<#else >
												<input type="text" class="inp1 easyui-validatebox" style="width: 250px;"
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
											<label class="LabName"><span><label class="Asterik">*</label>隐患说明：</span></label><textarea name="riskDescribe" id="riskDescribe" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;width: 87%;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.riskDescribe!}</textarea>
										</td>
									</tr>
									<#if reportFocus.dataSource?? && reportFocus.dataSource == '02'>
									<tr>
										 <td class="LeftTd" colspan="2">
										 	<label class="LabName"><span><label class="Asterik">*</label>举报内容：</span></label><textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 easyui-validatebox" style="height:64px;resize: none;width: 87%;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
										 </td>
									</tr>
									</#if>
									<tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
										<td colspan="2">
											<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
										</td>
									</tr>
 									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox" style="height:64px;resize: none;width: 87%" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
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
	
	<#include "/component/ComboBox.ftl" />
	
	<script type="text/javascript">
		var basWorkSubTaskCallback = null,//存放原有的提交回调方法
			baseWorkRejectCallback = null,//存放原有的驳回方法
			detailFormJsonObject = null;
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'houseHiddenDanger',
					attachmentData	: {bizId: reportId, attachmentType:'HOUSE_HIDDEN_DANGER', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					uploadSuccessCallback : function(file,response){
						$('#isUploadNewFile').val(response.attachmentId);
					},
					deleteCallback:function(obj){
						deleteback(obj);
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}]
				};
			var bigViodeUploadOpt = {
					useType			: 'view',
					fileExt			: '.mp4,.avi,.amr',
					module			: 'houseHiddenDanger',
					attachmentData	: {bizId: reportId, attachmentType:'HOUSE_HIDDEN_DANGER', eventSeq: '1', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					uploadSuccessCallback : function(file,response){
						$('#isUploadNewFile').val(response.attachmentId);
					},
					deleteCallback:function(obj){
						deleteback(obj);
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}]
				};
			var attachFileUploadOpt = {
				useType			: 'view',
				fileExt			: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				module			: 'houseHiddenDanger',
				attachmentData	: {bizId: reportId, attachmentType:'HOUSE_HIDDEN_DANGER', eventSeq: '1', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				},
				uploadSuccessCallback : function(file,response){
					$('#isUploadNewFile').val(response.attachmentId);
				},
				deleteCallback:function(obj){
					deleteback(obj);
				},
				//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
				labelDict		: [{'name':'处理前', 'value':'1'}]
			};
			<#if reportFocus.dataSource != '01' || reportFocus.riskType == '3'>
				init4Location('occurred', {
					_startAddress :"${reportFocus.occurred!}",
					_startDivisionCode : "${reportFocus.regionCode!}"//默认选中网格，非必传参数
				});
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
							<#if (isEditableNode?? && isEditableNode) || (reportFocus.collectSource?? && reportFocus.collectSource == '01')>
								AllowSelectLevel:"6"//选择到哪个层级
							<#elseif (isEditableNode?? && !isEditableNode)>
								AllowSelectLevel:"4,5,6"//不是核实环节 （撤回操作）无法明确到村社区网格层级时，允许选择到乡镇街道
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
			
			<#if isAble2Handle?? && isAble2Handle>
				var baseWorkOption = BaseWorkflowNodeHandle.initParam(),//获取默认的设置
					reportType = $('#reportType').val(),
					selectHandler = baseWorkOption.selectHandler;
				
				basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
				baseWorkRejectCallback = baseWorkOption.reject.rejectCallback;
				
				$.extend(selectHandler, {
					userSelectedLimit : 1
				});
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportHHD/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportHHD/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportHHD/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
					},
					evaluate: {
						isShowEva: false
					},
					initDefaultValueSet: initDefaultValueSet,
					checkRadio: {
						radioCheckCallback: radioCheckCallback
					},
					selectHandler : selectHandler
				});
				
				bigFileUploadOpt["useType"] = 'edit'; 
				bigViodeUploadOpt["useType"] = 'edit';
				attachFileUploadOpt["useType"] = 'edit';
			</#if>
			
			<#if feedbackCount?? && (feedbackCount > 0)>
				fetchHHDFeedback();
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
			
			detailFormJsonObject = $('#hhdDetailForm').serializeArray();
			var isEditableNode = $('#isEditableNode').val();
			
			if(isEditableNode == 'true') {
				$('#invalidTimeStamp').val(new Date().getTime());
			}
			
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
				mapType = 'HOUSE_HIDDEN_DANGER',
				isEdit = true,
				parameterJson = {
					'id' : $('#id').val(),
					'name' : $('#name').val()
				};
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#hhdDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportHHD/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#hhdDetailForm").ajaxSubmit(function(data) {
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
		
		function initDefaultValueSet() {
			var curNodeName = "${curNodeName!}", formTypeId = "${formTypeId!}";
			
			initDefaultValueSet4Base();
			
			if(formTypeId == '35301' && curNodeName == 'task4' && $('#_riskType').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="_riskType" name="riskType" value="" />');
			}
		}
	
		function radioCheckCallback(data) {//下一环节选中回调方法
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				curNodeName = '${curNodeName!}',
				formTypeId = "${formTypeId!}";
			$('#advice').val('');
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}
			
			if(!isShowText && !isShowDict) {
				$('#advice').val(adviceNote);
			}
			
			_handlerConstructor(data);
			
			if(isShowDict || isShowText) {
				if($('#adviceNote').length == 0) {
					$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
				}
				
				$('#adviceNote').val(adviceNote);
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
							var _adviceNote = dynamicContentMap["adviceNote_" + dictValue] || adviceNote,
									_dynamicContent = $('#dynamicContent').val();

							if(formTypeId == '35301' && curNodeName == 'task1') {
								if(dictValue == '1') {
									$('#dynamicContentAreaTr').hide();

									$('#dynamicContent').val('');
									$('#dynamicContent').validatebox({
										required: false
									});
								} else {
									$('#dynamicContent').validatebox({
										required: true
									});

									$('#dynamicContentAreaTr').show();
								}
							} else if(formTypeId == '35301' && curNodeName == 'task4') {
								$('#_riskType').val(dictValue);
								
								if(item && item.length > 0) {
									_dynamicContent = item[0].name;
								}
							}

							$('#adviceNote').val(_adviceNote);
							_capDynamicContent(_dynamicContent);
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
			
			if(isShowText) {
				if($('#dynamicContentAreaTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentAreaTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="dynamicContent" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
					
					var winWidth = $(window).width();
					$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
						$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
							   .addClass("isSettledAutoWidth");
					});
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

			//判断是否上传处理后图片
			var isUploadHandledPic = data.isUploadHandledPic||false;
			$('#isUploadHandledPic').val(isUploadHandledPic);
			$('#picTypeName').val(data.picTypeName||'');
			
			autoRequiredRender();
			_itemSizeAdjust();
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
			//flowSaveForm流程办理页面先进行验证
			var isValid =  $("#flowSaveForm").form('validate');

			if(isValid){
				//是否上传处理后图片
				var isUploadHandledPic = $('#isUploadHandledPic').val()||false;

				if(isUploadHandledPic == true || isUploadHandledPic == 'true') {
					isValid = this.verifyAttIsUpload('处理后', 3, isUploadHandledPic);
				}
			}
			if(isValid){
				if(formCompare4HHD() == false) {
					saveHHD();
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

		function _capDynamicContent(dynamicContent, dictValue) {
			var adviceNote = $('#adviceNote').val(),
					advice = '';

			if(adviceNote) {
				var dynamicContentLabel = '@dynamicContent@';

				if(adviceNote.indexOf(dynamicContentLabel) >= 0) {
					if(dynamicContent) {
						advice = adviceNote.replaceAll('@dynamicContent@', dynamicContent);
					}
				} else {
					advice = adviceNote;
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
					href: "${rc.getContextPath()}/zhsq/reportHHD/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
		function saveHHD() {
			var isValid =  $("#hhdDetailForm").form('validate'),
				buildingId = $('#buildingId').val(),
				dataSource = $('#dataSource').val()||'',
				riskType = $('#riskType').val()||'',
				invalidTimeStamp = $('#invalidTimeStamp').val();
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			$('#invalidTimeStamp').val(new Date().getTime());
			
			if(isValid) {
				if(isBlankParam(buildingId) && dataSource == '01' && riskType != '3') {
					$.messager.alert('警告', "请选择有效的楼栋信息！", 'warning');
					return;
				}else{
					$('#occurred').validatebox({
						required:true
					});
				}
				
				if(isValid) {
					var curNodeName = $('#curNodeName').val(),
						nextNodeName = $('#nodeName_').val(),
						formTypeId = $('#formTypeId').val(),
						isCheckRegion = true;
					
					if($('#bigFileUploadDivAsterik').is(':visible')) {
						var riskType = $('#riskType').val(),
							dataSource = $('#dataSource').val();
						
						if(riskType == '0' && dataSource == '01') {
							isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
						}
						
						if(formTypeId == '35301' && curNodeName == 'task3' && nextNodeName == 'task4') {
							isValid = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'));
						}
					}
					
					if(formTypeId == '35301' && curNodeName == 'task3' && nextNodeName == 'end1') {
						isCheckRegion = false;
					}
					
					$('#isCheckRegion').val(isCheckRegion);
				}
			}

			if(isValid) {
				modleopen();

				$("#hhdDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportHHD/saveReportFocus.jhtml");

				$("#hhdDetailForm").ajaxSubmit(function(data) {
					modleclose();

					if(data.success && data.success == true) {
						var isEditableNode = $('#isEditableNode').val();
						
						$('#invalidTimeStamp').val(invalidTimeStamp);
						
						if(isEditableNode == 'true') {
							if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
								basWorkSubTaskCallback();
							}
						} else {
							var checkedRadio = $('#tr_epath input[type=radio][name=nextNode]:checked').eq(0);
							
							if(checkedRadio.length > 0) {
								BaseWorkflowNodeHandle.checkRadio(checkedRadio.eq(0));
							}
						}
					} else {
						$.messager.alert('错误', data.tipMsg, 'error');
					}
				});
			}
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

			$('#hhdBuilding').comboselector("clear");//会回调combo_afterSelect
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
		
		function autoRequiredRender() {
			var curNodeName = $('#curNodeName').val(),
				nextNodeName = $('#nodeName_').val(),
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				riskType = $('#riskType').val(),
				isEditableNode = $('#isEditableNode').val(),
				isRequired = false;
			
			if((formTypeId == '35301' && curNodeName == 'task3' && nextNodeName == 'task4') || (isEditableNode == 'false' && riskType == '0' && dataSource == '01')) {
				isRequired = true;
			}
			
			autoRequiredBase('hhdDetailForm', isRequired);
		}
		
		function formCompare4HHD() {
			var detailFormJsonObjectNow = $('#hhdDetailForm').serializeArray();
			var eliminateObj = {"invalidTimeStamp":""};
			var compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow,eliminateObj);
			
			detailFormJsonObject = detailFormJsonObjectNow;
			
			return compareFlag;
		}
		
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchHHDFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>