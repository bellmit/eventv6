<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>烈士纪念设施详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
		<script type="text/javascript" src="${ZZGL_DOMAIN}/es/component/comboselector/clientJs.jhtml"></script>
		
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
							<li><a href="##" divId="mainDiv" class="active">烈士纪念设施信息</a></li>
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
					<form id="mfDetailForm" name="mfDetailForm" action="" method="post" enctype="multipart/form-data">
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
						<input type="hidden" id="module" value="MARTYRS_FACILITY"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<input type="hidden" id="isCheckRegion" name="isCheckRegion" value="true" />
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						<input id="isFirstInit" type="hidden" value="1" />
						<input type="hidden" id="isForce2Save" value= "<#if isEditableNode??>${isEditableNode?c}<#else>false</#if>" />
						
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
											<label class="LabName">
												<span><label class="Asterik">*</label>所属区域：</span>
											</label>
											<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
											<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>报告编号：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>报告方式：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
											<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>报告状态：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportStatusName!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>纪念设施：</span></label>
											<input  id="markInit" type="hidden" value="${reportFocus.markId!}" />
											<input  id="markId" name="markId" type="hidden" value="${reportFocus.markId!}" class="easyui-validatebox" data-options="required:true" />
											<input  id="markName" name="markName" type="hidden" value="${reportFocus.markId!}" class="easyui-validatebox" data-options="required:true"/>
											<input  id="markNameHidden" name="markNameHidden"
													placeholder="纪念设施" type="text"
													style="height: 28px; width: 300px;"
													class="comboselector easyui-validatebox"
													data-options="dType:'place', afterSelect:markAfterSelect, required:true, required:true, tipPosition:'bottom',validType:['maxLength[200]','characterCheck']"
													query-params="orgCode=${reportFocus.regionCode}&plaType=1205&orderBy=points"
													value="${reportFocus.markId!}"
											/>
										</td>
									</tr>
									<#if reportFocus.dataSource != '01'>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>发现渠道：</span></label>
											<input type="hidden" id="dataSource_" name="dataSource_" value="${reportFocus.dataSource!}"/>
											<input type="text" name="dataSourceName" id="dataSourceName" value="${dataSourceName!''}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>损坏方式：</span></label>
											<input type="hidden" id="damageMode" name="damageMode" value="${reportFocus.damageMode!}"/>
											<input type="text" name="damageModeName" id="damageModeName" value="${damageModeName!''}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
									</tr>
									<tr id="departmentNamePath" <#if reportFocus.dataSource != '03'> style="display:none;"</#if>>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>发现部门：</span></label>
											<input  id="departmentName" name="departmentName" placeholder="" type="text" class="inp1 easyui-validatebox autoWidth" data-options="<#if reportFocus.dataSource == '03'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.departmentName!}" />
										</td>
									</tr>
									</#if>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>发生地址：</span></label>
											<input type="text" class="inp1 easyui-validatebox autoWidth" data-options="required:true, tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span>地理标注：</span></label>
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</td>
									</tr>
									<#if reportFocus.dataSource == '02'>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>登记内容：</span></label><textarea  name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
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
					</form>
					
					<!--办理信息-->
					<#if isAble2Handle?? && isAble2Handle>
					<div class="fw-det-tog fw-det-tog-n mt20">
						<div class="fw-det-tog-top">
							<h5><i style="background:#4f91ff;"></i>处置流程</h5>
						</div>
						<div class="fw-chul">
							<#include "/zzgl/event/workflow/handle_nanan_business_problem.ftl" />
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
					module			: 'martyrsFacility',
					attachmentData	: {bizId: reportId, attachmentType:'MARTYRS_FACILITY', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				},
				attachFileUploadOpt = {
					useType			: 'edit',
					fileExt			: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					module			: 'martyrsFacility',
					attachmentData	: {bizId: reportId, attachmentType:'MARTYRS_FACILITY', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				};
			
			$('#mfDetailForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.90);
			});
			
			init4Location('occurred');
			
			//加载网格
			AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							$("#regionCode").val(items[0].orgCode);
							sentParams(items[0].orgCode);
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
			
			reportId = $('#reportId').val();
			
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
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportMarFac/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportMarFac/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportMarFac/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
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
			</#if>
			
			if($('#dataSource_').length > 0&&$("#dataSource").val()=='02') {
				AnoleApi.initTreeComboBox("dataSourceName", "dataSource_", null, null, ["${reportFocus.dataSource!''}"], {
					DataSrc : [{"name":"12345便民服务平台受理登记", "value":"02"}],
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						
					},
				});
			}else if($('#dataSource_').length > 0&&$("#dataSource").val()!='01') {
				AnoleApi.initTreeComboBox("dataSourceName", "dataSource_", null, null, ["${reportFocus.dataSource!''}"], {
					DataSrc : [{"name":"部门检查发现交办", "value":"03"}, {"name":"群众举报", "value":"04"}, {"name":"新闻媒体曝光", "value":"05"}],
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						if(value == '03') {
							$('#departmentNamePath').show();
							$('#departmentName').validatebox({
								required: true
							});
						} else if(value == '04'||value == '05') {
							$('#departmentNamePath').hide();
							$('#departmentName').validatebox({
								required: false
							});
							$("#departmentName").val('');
						}
						$("#dataSource").val(value);
						
					},
				});
			}
			if($("#dataSource").val()!='01'){
				AnoleApi.initTreeComboBox("damageModeName", "damageMode", "B210013002", null, ["${reportFocus.damageMode!''}"], {
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						
					},
				});
			}
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
			
			detailFormJsonObject = $('#mfDetailForm').serializeArray();
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
				mapType = 'MARTYRS_FACILITY',
				isEdit = true;
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#mfDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMarFac/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#mfDetailForm").ajaxSubmit(function(data) {
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
			if($('#distributeUserIds').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="distributeUserIds" name="distributeUserIds" value="" />');
			}
			
			if($('#distributeOrgIds').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="distributeOrgIds" name="distributeOrgIds" value="" />');
			}
			
			if($('#selectOrgIds').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="selectUserIds" name="selectUserIds" value="" />');
			}
			
			if($('#selectOrgIds').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="selectOrgIds" name="selectOrgIds" value="" />');
			}
			
			$('#handlerLabel').attr('defaultValue', '主送人员');
			$('#handlerLabel').html('主送人员');
			$('#remarkSpanHtml').html('<label class="Asterik">*</label>报告内容：');
			
			if($('#distributeUserDiv').length == 0) {
				$('#userDiv').after(
					'<tr id="distributeUserDiv" class="hide">' + 
						'<td>' +
							'<label class="LabName"><span><label id="distributeUserLabel" defaultValue="分送人员">分送人员</label>：</span></label>' +
							'<div class="FontDarkBlue fl DealMan"><b id="htmlDistributeUserNames"></b></div>' +
						'</td>'+
					'</tr>'
				);
			}
			
			if($('#selectUserDiv').length == 0) {
				$('#distributeUserDiv').after(
					'<tr id="selectUserDiv" class="hide">' + 
						'<td>' +
							'<label class="LabName"><span><label id="selectUserLabel" defaultValue="选送人员">选送人员</label>：</span></label>' +
							'<div class="FontDarkBlue fl DealMan"><b id="htmlSelectUserNames"></b></div>' +
						'</td>'+
					'</tr>'
				);
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
			
			if($("#isFirstInit").val()=='1'){
				detailFormJsonObject = $('#mfDetailForm').serializeArray();
				$("#isFirstInit").val('');
			}
		}
		
		function dynamicConstructor(data) {////下一环节动态内容构造方法
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val();
			
		}
		
		function adviceNoteConstructor(data) {//下一环节短信模板构造方法
			var adviceNote = data.adviceNote || '',
				adviceNoteInitial = data.adviceNoteInitial || '',
				dynamicContentMap = data.dynamicContentMap || {},
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val();
			
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
			
			if(curNodeName=='task1'){
				_capDynamicContent();
			}
		}
		
		function _subTaskOperate() {
			var isValid =  $("#flowSaveForm").form('validate');

			if(!$("#markId").validatebox('isValid') || !$("#markName").validatebox('isValid')) {
				$.messager.alert('警告', '请选择纪念设施！', 'warning');
				return;
			}

			if(!$("#occurred").validatebox('isValid')) {
				if($("#markId").validatebox('isValid') && $("#markName").validatebox('isValid')) {
					$.messager.alert('警告', '未获取到经纬度，请选择发生地址！', 'warning');
				} else {
					$.messager.alert('警告', '缺少发生地址，请选择纪念设施！', 'warning');
				}
				return;
			}

			if(!($('#dataSource').val() == '02' && ('${curNodeName!}' == 'task1' || '${curNodeName!}' == 'task2'))) {
				if($('input[name="resMarker.x"]').val() == '' || $('input[name="resMarker.y"]').val() == '') {
					$.messager.alert('警告', '未获取到经纬度，请重新选择发生地址定位！', 'warning');
					return;
				}
			}

			if(isValid) {
				isValid = $("#mfDetailForm").form('validate');
				if(isValid) {
					if(formCompare4MF() == false) {
						modleopen();
						$("#mfDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMarFac/saveReportFocus.jhtml");
						$("#mfDetailForm").ajaxSubmit(function(data) {
							modleclose();
							if(data.success && data.success == true) {
								/*
								var checkedRadio = $('#tr_epath input[type=radio][name=nextNode]:checked').eq(0);
								if(checkedRadio.length > 0) {
									BaseWorkflowNodeHandle.checkRadio(checkedRadio.eq(0));
								}*/
								
								var isRegionNotChanged = specificFormAttrCompare(detailFormJsonObject, $('#mfDetailForm').serializeArray(), {'regionCode' : ''}),
									compareFlag = specificFormCompare4MF();
								
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
					}else if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
						basWorkSubTaskCallback();
					}
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
		
		function toDo_capDynamicContent(dynamicLabel,adviceNote,dynamicStr,advice){
			if(adviceNote&&dynamicLabel) {
				if(adviceNote.indexOf(dynamicLabel) >= 0) {
					var value = $('#'+dynamicStr).val();
					if(value) {
						advice = advice.replaceAll(dynamicLabel, value);
					}
				}
			}
			return advice;
		}
		
		function _capDynamicContent(dynamicContent, dictValue, dynamicType) {
			var adviceNote = $('#adviceNote').val(),
				advice = '',
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val(),
				formTypeId = '${formTypeId!}';
			if(adviceNote) {
				var dynamicContentNextOrgNameLabel = '@nextOrgNames@';
				advice = adviceNote;
				
				if(adviceNote.indexOf(dynamicContentNextOrgNameLabel) >= 0) {
					var nextOrgNames = $('#nextOrgNames').val();
					if(nextOrgNames) {
						advice = advice.replaceAll(dynamicContentNextOrgNameLabel, nextOrgNames);
					}else{
						advice = '';
					}
				}
				
				if(curNodeName=='task1'){
				
				}	
				if(advice.indexOf('@') >= 0) {
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
					href: "${rc.getContextPath()}/zhsq/reportMarFac/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
			var curNodeName = $('#curNodeName').val(),
				nextNodeName = $('#nodeName_').val(),
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				isEditableNode = $('#isEditableNode').val(),
				isRequired = false;
			
			if((formTypeId == '358' && curNodeName == 'task3' && nextNodeName == 'task4') || (isEditableNode == 'false' && dataSource != '05')) {
				isRequired = true;
			}
			
			autoRequiredBase('mfDetailForm', isRequired);
		}
		
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchFFPFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
		
		function openSelectPerson(){
			
			var selectUserIds = $("#selectUserIds").val();
			var selectOrgIds = $("#selectOrgIds").val();
			var selectUserNames = $("#selectUserNames").val();
			var selectOrgNames = $("#selectOrgNames").val();
			var regionCode = $("#regionCode").val();
			var url = "${rc.getContextPath()}/zhsq/reportMarFac/toSelectPerson.jhtml?selectUserIds="
				+selectUserIds+"&selectOrgIds="+selectOrgIds+"&selectUserNames="+selectUserNames
				+"&selectOrgNames="+selectOrgNames+"&regionCode="+regionCode;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 700,
		  		height: 410,
		  		maximizable: false,
		  		title: "人员选择",
		  		targetUrl: url
		  	});
		}
		
		function setSelectPerson(ids,names,orgIds,orgNames){
			
			$("#selectUserIds").val(ids);
			$("#selectOrgIds").val(orgIds);
			$("#htmlSelectUserNames").html(names);
			$("#selectUserNames").val(names);
			$("#selectOrgNames").val(orgNames);
		}
		
		function formCompare4MF() {
			var isForce2Save = $('#isForce2Save').val();
			var compareFlag = true;
			
			if(isForce2Save === 'false') {
				var detailFormJsonObjectNow = $('#mfDetailForm').serializeArray();
				
				compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow);
			} else {
				compareFlag = false;
				$('#isForce2Save').val('false');
			}
			
			return compareFlag;
		}
		
		function specificFormCompare4MF() {
			var detailFormJsonObjectNow = $('#mfDetailForm').serializeArray(),
				adviceNoteInitial = $('#adviceNoteInitial').val(),
				extraSpecificAttrId = {},
				specificAttrId = null,
				compareFlag = false;
			
			if(adviceNoteInitial.indexOf("@tipoffContent@") >= 0) {
				extraSpecificAttrId.tipoffContent = '';
			}
			if(adviceNoteInitial.indexOf("@damageModeName@") >= 0) {
				extraSpecificAttrId.damageModeName = '';
			}
			if(adviceNoteInitial.indexOf("@departmentName@") >= 0) {
				extraSpecificAttrId.departmentName = '';
			}
			if(adviceNoteInitial.indexOf("@dataSourceName@") >= 0) {
				extraSpecificAttrId.dataSourceName = '';
			}
			
			specificAttrId = $.extend(extraSpecificAttrId, _capAdviceNoteAssociatedAttr(adviceNoteInitial));
			compareFlag = specificFormAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, specificAttrId);
				
			detailFormJsonObject = detailFormJsonObjectNow;
			
			return compareFlag;
		}
	</script>

	<script type="text/javascript">

		//客户端选中后事件(选中/取消选中后，均会触发)
		var markAfterSelect = function(data, target){

			console.log(data);

			if($('#markInit').length > 0) {
				var isInit = false;
				if($('#markInit').val() != '') {
					isInit = true;
				}
				$('#markInit').remove();
				if(isInit) {
					return;
				}
			}

			if(data == null) return;

			modleopen();

			$('#markName').val(data.plaName);
			$('#markId').val(data.plaId);
			$('#x').val('');
			$('#y').val('');
			$('#occurred').val('');
			$('#occurredDiv').text('');

			if(!!data.plaId) {
				$.ajax({
					type: "POST",
					url: '${rc.getContextPath()}/zhsq/reportMarFac/findPlaceInfoById.jhtml?plaId=' + data.plaId,
					dataType:'json',
					success: function(response){
						modleclose();
						if(response.success) {
							var x = response.data.x;
							var y = response.data.y;
							var baseAddr = response.data.baseAddr;

							$('#x').val(x);
							$('#y').val(y);
							$('#mapt').val('5');
							$('#occurred').val(baseAddr);
							$('#occurredDiv').text(baseAddr);

						} else {
							alert(response.tipMsg);
						}
					},
					error: function(response){
						alert('findPlaceInfoById请求失败');
						modleclose();
					}
				});
			} else {
				modleclose();
			}

		}

		var sentParams = function(orgCode){
			clearData();
			initMarkInput();
			var params = {};
			params["orgCode"] = orgCode;
			params["plaType"] = "1205";
			$('#markNameHidden').comboselector("queryData", params);
		}

		function clearData(){
			$('#markNameHidden').comboselector("clear");
		}

		var initMarkInput = function() {
			$('#markId').val();
			$('#markName').val();
			$('#markNameHidden').val();
			$('#x').val('');
			$('#y').val('');
			$('#mapt').val('5');
			$('#occurred').val('');
			$('#occurredDiv').text('');
		}

	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/martyrsFacility/detail_base.ftl" />
</html>