<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>信访人员稳控详情</title>
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
		<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
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
				margin-top:5px;
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
			.mapTab{ padding-left:20px; cursor:pointer;}/*地图为标注状态下的图标*/
			.mapTab2{ padding-left:20px; cursor:pointer; color:#4489ca;}
			.mapTab3{ width:11px; height:20px;display:inline-block;}
			
		</style>
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul id="topTitleUl" class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">信访人员稳控信息</a></li>
							<#if instanceId??>
							<li><a href="##" divId="taskDetailDiv">处理环节</a></li>
							</#if>
							<#if feedbackCount?? && (feedbackCount > 0)>
								<li><a href="##" divId="feedbackListDiv">信息反馈列表</a></li>
							</#if>
						</ul>
					</div>
				</div>
				<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode}" />
				<!-- 主体内容 -->
				<div id="mainDiv" class="fw-main tabContent" style="border-bottom: 1px solid #e5e5e5">
					<form id="ppDetailForm" name="ppDetailForm" action="" method="post" enctype="multipart/form-data">
						<input type="hidden" id="isStart" name="isStart" value="false" />
						<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
						<input type="hidden" name="isSaveResMarkerInfo" value="true" />
						<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
						<input type="hidden" id="reportType" name="reportType" value="${reportType!}" />
						<!--用于地图-->
						<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="name" name="name" value="" />
						<input type="hidden" id="markerOperation" name="markerOperation" value="3"/>
						<input type="hidden" id="module" value="PETITION_PERSON"/>
						<input type="hidden" id="module_iResidenceAddr" value="PETITION_PERSON_IRESIDENCEADDR"/>
						<input type="hidden" id="module_petitionAddr" value="PETITION_PERSON_PETITIONADDR"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						
						<div class="fw-det-tog fw-det-tog-n">
							<div class="" >
								<ul>
									<li>
										<p>
											<p id="resmarkerDiv" onclick="reSizeResmarker();" style="margin-top: -4px;"></p>&nbsp;<p style="font-size: 16px;<#if !(reportFocus.occurred??)>color: #C0C0C0;</#if>">${reportFocus.occurred!'（暂无发生地址信息！）'}</p>
										</p>
									</li>
								</ul>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<label class="LabName"><span>报告编号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>所属区域：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.regionPath!}</div>
										</td>
									</tr>
									<tr>
										<td>
											<label class="LabName"><span>报告人姓名：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span>报告时间：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!}</div>
										</td>
									</tr>
									<tr>
										<td style="width: 40%;">
											<label class="LabName"><span>报告方式：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span>报告状态：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.reportStatusName!}</div>
										</td>
									</tr>
									<#if reportFocus.userTel>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>报告人电话：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.userTel!}</div>
										</td>
									</tr>
									</#if>
									<tr>
										<td>
											<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.partyName_!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>证件类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.certTypeName!}</div>
										</td>
									</tr>
									<tr>
										<td>
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>证件号码：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.identityCard!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>现住地：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.iResidenceAddr!}</div>
										</td>
									</tr>
									<tr>
										<td>
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>人口类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.partyTypeName!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span>涉及人数：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.partyNum?c}</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><#if reportFocus.dataSource=='01'||reportFocus.dataSource=='02'><label class="Asterik">*</label></#if>上访类型：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.petitionTypesName!}</div>
										</td>
									</tr>
									<#if reportFocus.dataSource=='02'>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>信访动态：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.petitionTrend!}</div>
										</td>
									</tr>	
									</#if>
									<#if reportFocus.dataSource == '03'>
									<tr>
										<td>
											<label class="LabName"><span><label class="Asterik">*</label>稳控类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.controlTypeName!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>人员类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.petitionPartyTypeName!}</div>
										</td>
									</tr>
									</#if>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>信访事项：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.itemRemark!}</div>
										</td>
									</tr>	
									<tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
										<td colspan="2">
											<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
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
									<#if reportFocus.controlResult>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>稳控结果：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.controlResultName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.petitionAddr>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>信访地方：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.petitionAddr!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.isRightOrg>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>本街镇：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.isRightOrgStr!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.curCity>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>所属街镇：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.curCity!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.amTime || reportFocus.pmTime>
									<tr>
										<td>
											<label class="LabName"><span>上午时间：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.amTime!}</div>
										</td>
										<td>
											<label class="LabName labelNameWide"><span>下午时间：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.pmTime!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.feedbackResult>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>反馈结果：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.feedbackResultName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.stateMessage>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>情况描述：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.stateMessage!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.endTime>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>末次时间：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.endTimeStr!}</div>
										</td>
									</tr>
									</#if>
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
			baseWorkRejectCallback = null;//存放原有的驳回方法
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				_winWidth = $(window).width(),
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt		: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'businessProblem',
					attachmentData	: {bizId: reportId, attachmentType:'PETITION_PERSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
	                labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}],
	                uploadSuccessCallback : function(file,response){
	                    uploadback(file,response);
	                },
	                deleteCallback:function(obj){
	                    deleteback(obj);
	                }
				},
				attachFileUploadOpt = {
					useType			: 'view',
					fileExt		: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					module			: 'businessProblem',
					attachmentData	: {bizId: reportId, attachmentType:'PETITION_PERSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}],
					uploadSuccessCallback : function(file,response){
						uploadback(file,response);
					},
					deleteCallback:function(obj){
						deleteback(obj);
					}
				};
			
			$('#ppDetailForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.90);
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
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportPetPer/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportPetPer/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportPetPer/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
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
			
			<#if feedbackCount?? && (feedbackCount > 0)>
				fetchFFPFeedback();
			</#if>
			
			init4FetchRemind();
			//autoRequiredRender();
			
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
			<#if isAble2Handle?? && isAble2Handle>
				bigFileUploadOpt["useType"] = 'edit';
				attachFileUploadOpt["useType"] = 'edit';
			</#if>
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
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
			
			/*页面打开时判断地图标注是否存在*/
			capcapMarkerData();
			
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
				mapType = 'PETITION_PERSON',
				isEdit = false;
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#ppDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportPetPer/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#ppDetailForm").ajaxSubmit(function(data) {
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
			/*
			if($('#selectUserDiv').length == 0) {
				$('#distributeUserDiv').after(
					'<tr id="selectUserDiv" class="hide">' + 
						'<td>' +
							'<label class="LabName"><span><label id="selectUserLabel" defaultValue="选送人员">选送人员</label>：</span></label>' +
							'<div class="FontDarkBlue fl DealMan"><b id="htmlSelectUserNames"></b></div>' +
						'</td>'+
					'</tr>'
				);
			}*/
			if($('#selectUserDiv').length == 0) {
				$('#distributeUserDiv').after(
					'<tr id="selectUserDiv" class="hide">' + 
						'<td>' +
							'<label class="LabName"><span><label id="selectUserLabel" defaultValue="选送人员">选送人员</label>：</span></label>' +
							'<div class="Check_Radio hide" id="selectUserPath"><a href="###" class="NorToolBtn EditBtn" id="userSelectBtn" onclick="openSelectPerson();">选择人员</a></div>'+
							'<div class="FontDarkBlue fl DealMan Check_Radio" style="margin-left: 86px;"><b name="htmlSelectUserNames" id="htmlSelectUserNames"></b></div>'+
							'<input type="hidden" name="selectUserNames" id="selectUserNames"/>'+
							'<input type="hidden" name="selectOrgNames" id="selectOrgNames"/>'+
						'</td>'+
					'</tr>'
				);
			}
			
		}
		
		function onchange4CardType(value, items, isClear) {
			if(value == '111') {
				$('#_identityCard').validatebox({
					validType:'idcard'
				});
			} else {
				$('#_identityCard').validatebox({
					validType:['maxLength[50]','characterCheck']
				});
			}
		}
	
		function radioCheckCallback(data) {//下一环节选中回调方法
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				dynamicContentNextOrgNameLabel = '@nextOrgNames@',
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val(),
				startDivisionCode = data.dynamicContentMap.startDivisionCode || '';
			var formTypeId = '${formTypeId!}';
			_handlerConstructor(data);
			if($('#adviceNote').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
			}
			var adviceNoteSeparate = null;
			for(var noteIndex = 0;; noteIndex++) {
				adviceNoteSeparate = dynamicContentMap["adviceNote_" + noteIndex];
				if(adviceNoteSeparate) {
					if($('#adviceNote_' + noteIndex).length == 0) {
						$('#flowSaveForm').append('<input type="hidden" id="adviceNote_' + noteIndex + '" value="" />');
					}
					$('#adviceNote_' + noteIndex).val(adviceNoteSeparate);
				} else {
					break;
				}
			}
			$('#adviceNote').val(adviceNote);
			
			if(formTypeId=='362'){
				if(curNodeName=='task1'){
					$('#advice').val(adviceNote);
				}else if(curNodeName=='task2'||curNodeName=='task5'){
					if($('#partyName_Tr').length == 0) {
						$("input[name='nextNode']").each(function(){
						    if($(this).attr('nodename')=='task0'){
						    	$(this).parent().hide();
						    }else if($(this).attr('nodename')=='end1'){
						    	$(this).parent().show();
						    	$(this).click();
						    }
						});
						$('#remarkTr').before(
							'<tr id="partyName_Tr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label>' +
									'<input id="_partyName_" name="_partyName_" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.partyName_!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_partyName_').validatebox({
							required: true
						});
						
						$('#partyName_Tr').show();
						$('#ppDetailForm').append('<input type="hidden" id="partyName_" name="partyName_" value="" />');
					}
					if($('#certTypeNameTr').length == 0) {//certType,certTypeName
						$('#remarkTr').before(
							'<tr id="certTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件类型：</span></label>' + 
									'<input type="hidden" id="_certType" name="_certType" value="${reportFocus.certType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_certTypeName" name="_certTypeName" value="${reportFocus.certTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="certType" name="certType" value="" />');
						AnoleApi.initListComboBox("_certTypeName", "_certType", "D030001", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.certType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								onchange4CardType(value, items, true);
							},
						});
						$('#_certTypeName').validatebox({
							required: true
						});
						
						$('#certTypeNameTr').show();
					}
					if($('#identityCardTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="identityCardTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件号码：</span></label>' +
									'<input id="_identityCard" name="_identityCard" class="inp1 easyui-validatebox " style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[50]\',\'idcard\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.identityCard!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_identityCard').validatebox({
							required: true
						});
						var certType = "${reportFocus.certType!}";
						onchange4CardType(certType);
						
						$('#identityCardTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="identityCard" name="identityCard" value="" />');
					}
					//iResidenceAddr,residenceAddrNo
					if($('#iResidenceAddrTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="iResidenceAddrTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>现住地：</span></label>' +
									'<input type="hidden" id="_residenceAddrNo" name="_residenceAddrNo" value="${reportFocus.residenceAddrNo!}" />' + 
									'<input id="_iResidenceAddr" name="_iResidenceAddr" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.iResidenceAddr!}"/>' + 
								'</td>' + 
								'<td class="hide">' + 
									'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
									'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>'+
								'</td>' + 
							'</tr>'
						);
						$('#_iResidenceAddr').validatebox({
							required: true
						});
						
						$('#iResidenceAddrTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="residenceAddrNo" name="residenceAddrNo" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="iResidenceAddr" name="iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="x_iResidenceAddr" name="resMarker_iResidenceAddr.x" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="y_iResidenceAddr" name="resMarker_iResidenceAddr.y" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="hs_iResidenceAddr" name="hs_iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="mapt_iResidenceAddr" name="resMarker_iResidenceAddr.mapType" value="" />');
						
						init4Location_iResidenceAddr('_iResidenceAddr',startDivisionCode);
						getMarkerData_iResidenceAddr();
						<#if reportFocus.resMarker_iResidenceAddr??>
							var resMarkerX = "${reportFocus.resMarker_iResidenceAddr.x!}",
								resMarkerY = "${reportFocus.resMarker_iResidenceAddr.y!}",
								resMarkerMapType = "${reportFocus.resMarker_iResidenceAddr.mapType!}";
							
							if(resMarkerX && resMarkerY && resMarkerMapType) {
								callBackOfData_iResidenceAddr(resMarkerX, resMarkerY, null, resMarkerMapType);
							}
						</#if>
					}
					if($('#partyTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="partyTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>人口类型：</span></label>' + 
									'<input type="hidden" id="_partyType" name="_partyType" value="${reportFocus.partyType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_partyTypeName" name="_partyTypeName" value="${reportFocus.partyTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="partyType" name="partyType" value="" />');
						AnoleApi.initListComboBox("_partyTypeName", "_partyType", "D069003", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.partyType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_partyTypeName').validatebox({
							required: true
						});
						
						$('#partyTypeNameTr').show();
					}
					
					if($('#controlResultNameTr').length == 0) {//controlResultName
						$('#tr_epath').before(
							'<tr id="controlResultNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>稳控结果：</span></label>' + 
									'<input type="hidden" id="_controlResult" name="_controlResult" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_controlResultName" name="_controlResultName" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="controlResult" name="controlResult" value="" />');
						AnoleApi.initListComboBox("_controlResultName", "_controlResult", "B210012004", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, "", { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								if(value=='02'){
									$('#_petitionTypesName').validatebox({
										required: true
									});
									
									$('#petitionTypesNameTr').show();
									
									$('#_petitionAddr').validatebox({
										required: false
									});
									
									$('#petitionAddrTr').hide();
									
									$("#_petitionAddr").val('');
									$('#x_petitionAddr').val('');
									$('#y_petitionAddr').val('');
									$('#mapt_petitionAddr').val('');
									$("#mapTab2_petitionAddr").html('标注地理位置');
								}else if(value=='03'){
									$('#_petitionTypesName').validatebox({
										required: true
									});
									
									$('#petitionTypesNameTr').show();
									
									$('#_petitionAddr').validatebox({
										required: true
									});
									
									$('#petitionAddrTr').show();
								}else{
									$('#_petitionTypesName').validatebox({
										required: false
									});
									
									$('#petitionTypesNameTr').hide();
									initPetitionTypes();
									
									$('#_petitionAddr').validatebox({
										required: false
									});
									
									$('#petitionAddrTr').hide();
									
									$("#_petitionAddr").val('');
									$('#x_petitionAddr').val('');
									$('#y_petitionAddr').val('');
									$('#mapt_petitionAddr').val('');
									$("#mapTab2_petitionAddr").html('标注地理位置');
								}
								
								if(value=='01'||value=='02'||value=='03'){
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='end1'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='task0'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}else{
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='task0'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='end1'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}
							},
						});
						$('#_controlResultName').validatebox({
							required: true
						});
						
						$('#controlResultNameTr').show();
					}else{
						var _controlResult = $("#_controlResult").val();
						if(_controlResult=='01'||_controlResult=='02'||_controlResult=='03'){
							var temp = "adviceNote_" + (parseInt(_controlResult)-1);
							$('#adviceNote').val(dynamicContentMap[temp]);
						}else if(_controlResult=='04'||_controlResult=='05'){
							var temp = "adviceNote_" + (parseInt(_controlResult)-4);
							$('#adviceNote').val(dynamicContentMap[temp]);
						}
						_capDynamicContent();
					}
				
					if($('#petitionTypesNameTr').length == 0) {//controlResultName
						$('#remarkTr').before(
							'<tr id="petitionTypesNameTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>上访类型：</span></label>' + 
									'<input type="hidden" id="_petitionTypes" name="_petitionTypes" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:180px;" data-options="required:true,tipPosition:\'bottom\'" id="_petitionTypesName" name="_petitionTypesName" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="petitionTypes" name="petitionTypes" value="" />');
						var petitionTypesArray = [], petitionTypes = "${reportFocus.petitionTypes!}";
						if(petitionTypes) {
							petitionTypesArray = petitionTypes.split(",");
						}
						AnoleApi.initTreeComboBox("_petitionTypesName", "_petitionTypes", "A001130002", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, petitionTypesArray, {
							RenderType : "01",
							ShowOptions: {
								EnableToolbar : false
							}
						});
						$('#_petitionTypesName').validatebox({
							required: false
						});
						
						$('#petitionTypesNameTr').hide();
					}
					
					if($('#petitionAddrTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="petitionAddrTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>信访地方：</span></label>' +
									'<input id="_petitionAddr" name="_petitionAddr" style="width:160px;" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
								'</td>' + 
								'<td class="hide">' + 
									'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
									'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>'+
								'</td>' + 
							'</tr>'
						);
						$('#_petitionAddr').validatebox({
							required: false
						});
						
						$('#petitionAddrTr').hide();
						$('#ppDetailForm').append('<input type="hidden" id="petitionAddr" name="petitionAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="x_petitionAddr" name="resMarker_petitionAddr.x" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="y_petitionAddr" name="resMarker_petitionAddr.y" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="hs_petitionAddr" name="hs_petitionAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="mapt_petitionAddr" name="resMarker_petitionAddr.mapType" value="" />');
						
						init4Location_petitionAddr('_petitionAddr',startDivisionCode);
						getMarkerData_petitionAddr();
					}
				}else if(curNodeName=='task6'){
					if($('#isRightOrgTr').length == 0) {//isRightOrg
						$("input[name='nextNode']").each(function(){
						    if($(this).attr('nodename')=='task1'){
						    	$(this).parent().hide();
						    }else if($(this).attr('nodename')=='task3'){
						    	$(this).parent().show();
						    	$(this).click();
						    }
						});
						$('#tr_epath').before(
							'<tr id="isRightOrgTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>本街镇：</span></label>' + 
									'<input type="hidden" id="_isRightOrg" name="_isRightOrg" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_isRightOrgStr" name="_isRightOrgStr" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="isRightOrg" name="isRightOrg" value="" />');
						AnoleApi.initListComboBox("_isRightOrgStr", "_isRightOrg", "", function(dictValue, item) {
								_capDynamicContent($('#dynamicContent').val(), dictValue);
							}, null, {
								DataSrc: [{'name':'是', 'value':'1'},{'name':'否', 'value':'0'}],
								ShowOptions: {
									EnableToolbar : false
								},
								OnChanged: function(value, items) {
									if(value=='1'){
										$('#_curCity').validatebox({
											required: false
										});
										$('#_curCity').val('');
										$('#curCityTr').hide();
										
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task1'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task3'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}else{
										$('#_curCity').validatebox({
											required: true
										});
										
										$('#curCityTr').show();
										
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task3'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task1'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								},
						});
						$('#_isRightOrgStr').validatebox({
							required: true
						});
						
						$('#isRightOrgTr').show();
					}else{
						_capDynamicContent();
					}
				
					if($('#curCityTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="curCityTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>所属街镇：</span></label>' +
									'<textarea rows="3" style="height:50px;" id="_curCity" name="_curCity" class="area1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
								'</td>' + 
							'</tr>'
						);
						
						$('#_curCity').validatebox({
							required: false
						});
						
						$('#doWorkMessageTr').hide();
						$('#ppDetailForm').append('<input type="hidden" id="curCity" name="curCity" value="" />');
					}
				}else if(curNodeName=='task3'){
					if($('#partyName_Tr').length == 0) {
						$('#remarkTr').before(
							'<tr id="partyName_Tr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label>' +
									'<input id="_partyName_" name="_partyName_" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.partyName_!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_partyName_').validatebox({
							required: true
						});
						
						$('#partyName_Tr').show();
						$('#ppDetailForm').append('<input type="hidden" id="partyName_" name="partyName_" value="" />');
					}
					if($('#certTypeNameTr').length == 0) {//certType,certTypeName
						$('#remarkTr').before(
							'<tr id="certTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件类型：</span></label>' + 
									'<input type="hidden" id="_certType" name="_certType" value="${reportFocus.certType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_certTypeName" name="_certTypeName" value="${reportFocus.certTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="certType" name="certType" value="" />');
						AnoleApi.initListComboBox("_certTypeName", "_certType", "D030001", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.certType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								onchange4CardType(value, items, true);
							},
						});
						$('#_certTypeName').validatebox({
							required: true
						});
						
						$('#certTypeNameTr').show();
					}
					if($('#identityCardTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="identityCardTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件号码：</span></label>' +
									'<input id="_identityCard" name="_identityCard" class="inp1 easyui-validatebox " style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[50]\',\'idcard\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.identityCard!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_identityCard').validatebox({
							required: true
						});
						var certType = "${reportFocus.certType!}";
						onchange4CardType(certType);
						
						$('#identityCardTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="identityCard" name="identityCard" value="" />');
					}
					//iResidenceAddr,residenceAddrNo
					if($('#iResidenceAddrTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="iResidenceAddrTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>现住地：</span></label>' +
									'<input type="hidden" id="_residenceAddrNo" name="_residenceAddrNo" value="${reportFocus.residenceAddrNo!}" />' + 
									'<input id="_iResidenceAddr" name="_iResidenceAddr" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.iResidenceAddr!}"/>' + 
								'</td>' + 
								'<td class="hide">' + 
									'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
									'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>'+
								'</td>' + 
							'</tr>'
						);
						$('#_iResidenceAddr').validatebox({
							required: true
						});
						
						$('#iResidenceAddrTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="residenceAddrNo" name="residenceAddrNo" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="iResidenceAddr" name="iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="x_iResidenceAddr" name="resMarker_iResidenceAddr.x" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="y_iResidenceAddr" name="resMarker_iResidenceAddr.y" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="hs_iResidenceAddr" name="hs_iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="mapt_iResidenceAddr" name="resMarker_iResidenceAddr.mapType" value="" />');
						
						init4Location_iResidenceAddr('_iResidenceAddr',startDivisionCode);
						getMarkerData_iResidenceAddr();
						<#if reportFocus.resMarker_iResidenceAddr??>
							var resMarkerX = "${reportFocus.resMarker_iResidenceAddr.x!}",
								resMarkerY = "${reportFocus.resMarker_iResidenceAddr.y!}",
								resMarkerMapType = "${reportFocus.resMarker_iResidenceAddr.mapType!}";
							
							if(resMarkerX && resMarkerY && resMarkerMapType) {
								callBackOfData_iResidenceAddr(resMarkerX, resMarkerY, null, resMarkerMapType);
							}
						</#if>
					}
					if($('#partyTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="partyTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>人口类型：</span></label>' + 
									'<input type="hidden" id="_partyType" name="_partyType" value="${reportFocus.partyType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_partyTypeName" name="_partyTypeName" value="${reportFocus.partyTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="partyType" name="partyType" value="" />');
						AnoleApi.initListComboBox("_partyTypeName", "_partyType", "D069003", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.partyType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_partyTypeName').validatebox({
							required: true
						});
						
						$('#partyTypeNameTr').show();
					}
					if($('#controlTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="controlTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>稳控类型：</span></label>' + 
									'<input type="hidden" id="_controlType" name="_controlType" value="${reportFocus.controlType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_controlTypeName" name="_controlTypeName" value="${reportFocus.controlTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="controlType" name="controlType" value="" />');
						AnoleApi.initListComboBox("_controlTypeName", "_controlType", "B210012002", function(dictValue, item) {
							var temp = "adviceNote_" + (parseInt(dictValue)-1);
							$('#adviceNote').val(dynamicContentMap[temp]);
							_capDynamicContent();
						}, ["${reportFocus.controlType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								if(value=='01'){
									$('#_amTime').validatebox({
										required: false
									});
									$('#_amTime').val("");
									$('#amTimeTr').hide();
									
									$('#_pmTime').validatebox({
										required: false
									});
									$('#_pmTime').val("");
									$('#pmTimeTr').hide();
								}else if(value=='02'){
									$('#_amTime').validatebox({
										required: true
									});
									$('#amTimeTr').show();
									
									$('#_pmTime').validatebox({
										required: true
									});
									$('#pmTimeTr').show();
								}
							},
						});
						$('#_controlTypeName').validatebox({
							required: true
						});
						
						$('#controlTypeNameTr').show();
					}
					if($('#petitionPartyTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="petitionPartyTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>人员类型：</span></label>' + 
									'<input type="hidden" id="_petitionPartyType" name="_petitionPartyType" value="${reportFocus.petitionPartyType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_petitionPartyTypeName" name="_petitionPartyTypeName" value="${reportFocus.petitionPartyTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="petitionPartyType" name="petitionPartyType" value="" />');
						AnoleApi.initListComboBox("_petitionPartyTypeName", "_petitionPartyType", "B210012003", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.petitionPartyType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_petitionPartyTypeName').validatebox({
							required: true
						});
						
						$('#petitionPartyTypeNameTr').show();
					}
					
					if($('#amTimeTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="amTimeTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName labelNameWide"><span><label class="Asterik">*</label>上午时间：</span></label>' + 
									'<input type="text" id="_amTime" name="_amTime" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({onpicked:pickedCallBack,readOnly:true, dateFmt:\'HH:mm\',maxDate:\'12:00\', isShowClear:false})" value="" readonly="readonly"></input>'+
								'</td>' + 
							'</tr>'
						);
						var controlType = "${reportFocus.controlType!''}";
						if(controlType=='02'){
							$('#_amTime').validatebox({
								required: true
							});
							
							$('#amTimeTr').show();
						}
						
						$('#ppDetailForm').append('<input type="hidden" id="amTime" name="amTime" value="" />');
					}
					if($('#pmTimeTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="pmTimeTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName labelNameWide"><span><label class="Asterik">*</label>下午时间：</span></label>' + 
									'<input type="text" id="_pmTime" name="_pmTime" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({onpicked:pickedCallBack,readOnly:true, dateFmt:\'HH:mm\',minDate:\'12:00\', isShowClear:false})" value="" readonly="readonly"></input>'+
								'</td>' + 
							'</tr>'
						);
						var controlType = "${reportFocus.controlType!''}";
						if(controlType=='02'){
							$('#_pmTime').validatebox({
								required: true
							});
							
							$('#pmTimeTr').show();
						}
						
						$('#ppDetailForm').append('<input type="hidden" id="pmTime" name="pmTime" value="" />');
					}
				}else if(curNodeName=='task4'){
					if($('#partyName_Tr').length == 0) {
						$("input[name='nextNode']").each(function(){
						    if($(this).attr('nodename')=='task0'){
						    	$(this).parent().hide();
						    }else if($(this).attr('nodename')=='end1'){
						    	$(this).parent().show();
						    	$(this).click();
						    }
						});
						$('#remarkTr').before(
							'<tr id="partyName_Tr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label>' +
									'<input id="_partyName_" name="_partyName_" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.partyName_!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_partyName_').validatebox({
							required: true
						});
						
						$('#partyName_Tr').show();
						$('#ppDetailForm').append('<input type="hidden" id="partyName_" name="partyName_" value="" />');
					}
					if($('#certTypeNameTr').length == 0) {//certType,certTypeName
						$('#remarkTr').before(
							'<tr id="certTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件类型：</span></label>' + 
									'<input type="hidden" id="_certType" name="_certType" value="${reportFocus.certType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_certTypeName" name="_certTypeName" value="${reportFocus.certTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="certType" name="certType" value="" />');
						AnoleApi.initListComboBox("_certTypeName", "_certType", "D030001", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.certType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								onchange4CardType(value, items, true);
							},
						});
						$('#_certTypeName').validatebox({
							required: true
						});
						
						$('#certTypeNameTr').show();
					}
					if($('#identityCardTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="identityCardTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>证件号码：</span></label>' +
									'<input id="_identityCard" name="_identityCard" class="inp1 easyui-validatebox " style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[50]\',\'idcard\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.identityCard!}"/>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_identityCard').validatebox({
							required: true
						});
						var certType = "${reportFocus.certType!}";
						onchange4CardType(certType);
						
						$('#identityCardTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="identityCard" name="identityCard" value="" />');
					}
					//iResidenceAddr,residenceAddrNo
					if($('#iResidenceAddrTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="iResidenceAddrTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>现住地：</span></label>' +
									'<input type="hidden" id="_residenceAddrNo" name="_residenceAddrNo" value="${reportFocus.residenceAddrNo!}" />' + 
									'<input id="_iResidenceAddr" name="_iResidenceAddr" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.iResidenceAddr!}"/>' + 
								'</td>' + 
								'<td class="hide">' + 
									'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
									'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>'+
								'</td>' + 
							'</tr>'
						);
						$('#_iResidenceAddr').validatebox({
							required: true
						});
						
						$('#iResidenceAddrTr').show();
						$('#ppDetailForm').append('<input type="hidden" id="residenceAddrNo" name="residenceAddrNo" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="iResidenceAddr" name="iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="x_iResidenceAddr" name="resMarker_iResidenceAddr.x" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="y_iResidenceAddr" name="resMarker_iResidenceAddr.y" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="hs_iResidenceAddr" name="hs_iResidenceAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="mapt_iResidenceAddr" name="resMarker_iResidenceAddr.mapType" value="" />');
						
						init4Location_iResidenceAddr('_iResidenceAddr',startDivisionCode);
						getMarkerData_iResidenceAddr();
						<#if reportFocus.resMarker_iResidenceAddr??>
							var resMarkerX = "${reportFocus.resMarker_iResidenceAddr.x!}",
								resMarkerY = "${reportFocus.resMarker_iResidenceAddr.y!}",
								resMarkerMapType = "${reportFocus.resMarker_iResidenceAddr.mapType!}";
							
							if(resMarkerX && resMarkerY && resMarkerMapType) {
								callBackOfData_iResidenceAddr(resMarkerX, resMarkerY, null, resMarkerMapType);
							}
						</#if>
					}
					if($('#partyTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="partyTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>人口类型：</span></label>' + 
									'<input type="hidden" id="_partyType" name="_partyType" value="${reportFocus.partyType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_partyTypeName" name="_partyTypeName" value="${reportFocus.partyTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="partyType" name="partyType" value="" />');
						AnoleApi.initListComboBox("_partyTypeName", "_partyType", "D069003", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.partyType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_partyTypeName').validatebox({
							required: true
						});
						
						$('#partyTypeNameTr').show();
					}
					if($('#controlTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="controlTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>稳控类型：</span></label>' + 
									'<input type="hidden" id="_controlType" name="_controlType" value="${reportFocus.controlType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_controlTypeName" name="_controlTypeName" value="${reportFocus.controlTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="controlType" name="controlType" value="" />');
						AnoleApi.initListComboBox("_controlTypeName", "_controlType", "B210012002", function(dictValue, item) {
							_capDynamicContent();
						}, ["${reportFocus.controlType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_controlTypeName').validatebox({
							required: true
						});
						
						$('#controlTypeNameTr').show();
					}
					if($('#petitionPartyTypeNameTr').length == 0) {//partyType
						$('#remarkTr').before(
							'<tr id="petitionPartyTypeNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>人员类型：</span></label>' + 
									'<input type="hidden" id="_petitionPartyType" name="_petitionPartyType" value="${reportFocus.petitionPartyType!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_petitionPartyTypeName" name="_petitionPartyTypeName" value="${reportFocus.petitionPartyTypeName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="petitionPartyType" name="petitionPartyType" value="" />');
						AnoleApi.initListComboBox("_petitionPartyTypeName", "_petitionPartyType", "B210012003", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, ["${reportFocus.petitionPartyType!''}"], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								
							},
						});
						$('#_petitionPartyTypeName').validatebox({
							required: true
						});
						
						$('#petitionPartyTypeNameTr').show();
					}
					if($('#controlResultNameTr').length == 0) {//partyType
						$('#tr_epath').before(
							'<tr id="controlResultNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>稳控结果：</span></label>' + 
									'<input type="hidden" id="_controlResult" name="_controlResult" value="${reportFocus.controlResult!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_controlResultName" name="_controlResultName" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="controlResult" name="controlResult" value="" />');
						AnoleApi.initListComboBox("_controlResultName", "_controlResult", "", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, [""], { 
							DataSrc: [{'name':'状态稳定', 'value':'01'},{'name':'正在跟踪动态', 'value':'02'},{'name':'正在化解稳控', 'value':'03'},{'name':'稳控到位', 'value':'04'}],
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								if(value=='02'){
									$('#_petitionTypesName').validatebox({
										required: true
									});
									
									$('#petitionTypesNameTr').show();
									
									$('#_petitionAddr').validatebox({
										required: false
									});
									
									$('#petitionAddrTr').hide();
									
									$("#_petitionAddr").val('');
									$('#x_petitionAddr').val('');
									$('#y_petitionAddr').val('');
									$('#mapt_petitionAddr').val('');
									$("#mapTab2_petitionAddr").html('标注地理位置');
								}else if(value=='03'){
									$('#_petitionTypesName').validatebox({
										required: true
									});
									
									$('#petitionTypesNameTr').show();
									
									$('#_petitionAddr').validatebox({
										required: true
									});
									
									$('#petitionAddrTr').show();
								}else{
									$('#_petitionTypesName').validatebox({
										required: false
									});
									
									$('#petitionTypesNameTr').hide();
									initPetitionTypes();
									
									$('#_petitionAddr').validatebox({
										required: false
									});
									
									$('#petitionAddrTr').hide();
									
									$("#_petitionAddr").val('');
									$('#x_petitionAddr').val('');
									$('#y_petitionAddr').val('');
									$('#mapt_petitionAddr').val('');
									$("#mapTab2_petitionAddr").html('标注地理位置');
								}
								
								if(value=='01'||value=='02'||value=='03'){
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='end1'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='task0'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}else if(value=='04'){
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='task0'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='end1'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}
							},
						});
						$('#_controlResultName').validatebox({
							required: true
						});
						
						$('#controlResultNameTr').show();
					}else{
						var _controlResult = $("#_controlResult").val();
						if(_controlResult=='01'||_controlResult=='02'||_controlResult=='03'){
							var temp = "adviceNote_" + (parseInt(_controlResult)-1);
							$('#adviceNote').val(dynamicContentMap[temp]);
						}else if(_controlResult=='04'||_controlResult=='05'){
							$('#adviceNote').val(adviceNote);
						}
						_capDynamicContent();
					}
					if($('#petitionTypesNameTr').length == 0) {//petitionTypes
						$('#remarkTr').before(
							'<tr id="petitionTypesNameTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>上访类型：</span></label>' + 
									'<input type="hidden" id="_petitionTypes" name="_petitionTypes" value="${reportFocus.petitionTypes!}" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:180px;" data-options="required:true,tipPosition:\'bottom\'" id="_petitionTypesName" name="_petitionTypesName" value="${reportFocus.petitionTypesName!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="petitionTypes" name="petitionTypes" value="" />');
						var petitionTypesArray = [], petitionTypes = "${reportFocus.petitionTypes!}";
						if(petitionTypes) {
							petitionTypesArray = petitionTypes.split(",");
						}
						AnoleApi.initTreeComboBox("_petitionTypesName", "_petitionTypes", "A001130002", function(dictValue, item) {
							//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, petitionTypesArray, {
							RenderType : "01",
							ShowOptions: {
								EnableToolbar : false
							}
						});
						$('#_petitionTypesName').validatebox({
							required: false
						});
						
						$('#petitionTypesNameTr').hide();
					}
					if($('#petitionAddrTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="petitionAddrTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>信访地方：</span></label>' +
									'<input id="_petitionAddr" name="_petitionAddr" style="width:160px;" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[250]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
								'</td>' + 
								'<td class="hide">' + 
									'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
									'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>'+
								'</td>' + 
							'</tr>'
						);
						$('#_petitionAddr').validatebox({
							required: false
						});
						
						$('#petitionAddrTr').hide();
						$('#ppDetailForm').append('<input type="hidden" id="petitionAddr" name="petitionAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="x_petitionAddr" name="resMarker_petitionAddr.x" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="y_petitionAddr" name="resMarker_petitionAddr.y" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="hs_petitionAddr" name="hs_petitionAddr" value="" />');
						$('#ppDetailForm').append('<input type="hidden" id="mapt_petitionAddr" name="resMarker_petitionAddr.mapType" value="" />');
						
						init4Location_petitionAddr('_petitionAddr',startDivisionCode);
						getMarkerData_petitionAddr();
					}
				}
			}else if(formTypeId=='36201'){
				if(curNodeName=='task1'){
					var controlType = "${reportFocus.controlType}";
					var temp = "adviceNote_" + (parseInt(controlType)-1);
					$('#adviceNote').val(dynamicContentMap[temp]);
					_capDynamicContent();
				}else if(curNodeName=='task2'){
					if($('#feedbackResultNameTr').length == 0) {//partyType
						$("input[name='nextNode']").each(function(){
						    if($(this).attr('nodename')=='end1'){
						    	$(this).parent().hide();
						    }else if($(this).attr('nodename')=='task0'){
						    	$(this).parent().show();
						    	$(this).click();
						    }
						});
						$('#tr_epath').before(
							'<tr id="feedbackResultNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>反馈结果：</span></label>' + 
									'<input type="hidden" id="_feedbackResult" name="_feedbackResult" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_feedbackResultName" name="_feedbackResultName" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#ppDetailForm').append('<input type="hidden" id="feedbackResult" name="feedbackResult" value="" />');
						AnoleApi.initListComboBox("_feedbackResultName", "_feedbackResult", "B210012005", function(dictValue, item) {
							var temp = "adviceNote_" + (parseInt(dictValue)-1);
							$('#adviceNote').val($('#'+temp).val());
							_capDynamicContent();
						}, [""], { 
							ShowOptions: {
								EnableToolbar : false
							},
							OnChanged: function(value, items) {
								if(value=='01'){
									$('#_stateMessage').validatebox({
										required: true
									});
									
									$('#stateMessageTr').show();
									
									$('#_endTime').validatebox({
										required: false
									});
									$('#endTimeTr').hide();
									$("#_endTime").val('');
								}else if(value=='02'){
									$('#_endTime').validatebox({
										required: true
									});
									
									$('#endTimeTr').show();
									
									$('#_stateMessage').validatebox({
										required: false
									});
									$('#stateMessageTr').hide();
									$("#_stateMessage").val('');
								}else{
									$('#_stateMessage').validatebox({
										required: false
									});
									$('#stateMessageTr').hide();
									$("#_stateMessage").val('');
									
									$('#_endTime').validatebox({
										required: false
									});
									$('#endTimeTr').hide();
									$("#_endTime").val('');
								}
							},
						});
						$('#_feedbackResultName').validatebox({
							required: true
						});
						
						$('#feedbackResultNameTr').show();
					}else{
						var _feedbackResult = $("#_feedbackResult").val();
						var temp = "adviceNote_" + (parseInt(_feedbackResult)-1);
						$('#adviceNote').val(dynamicContentMap[temp]);
						_capDynamicContent();
					}
					if($('#stateMessageTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="stateMessageTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>情况描述：</span></label>' +
									'<textarea rows="3" style="height:50px;" id="_stateMessage" name="_stateMessage" class="area1 easyui-validatebox doubleCellClass" data-options="tipPosition:\'bottom\',validType:[\'maxLength[2000]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
								'</td>' + 
							'</tr>'
						);
						
						$('#_stateMessage').validatebox({
							required: false
						});
						
						$('#stateMessageTr').hide();
						$('#ppDetailForm').append('<input type="hidden" id="stateMessage" name="stateMessage" value="" />');
					}
					if($('#endTimeTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="endTimeTr" style="display:none;">' + 
								'<td>' + 
									'<label class="LabName labelNameWide"><span><label class="Asterik">*</label>末次时间：</span></label>' + 
									'<input type="text" id="_endTime" name="_endTime" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({onpicked:pickedCallBack,readOnly:true, dateFmt:\'yyyy-MM-dd HH\', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>'+
								'</td>' + 
							'</tr>'
						);
						
						$('#_endTime').validatebox({
							required: false
						});
						
						$('#ppDetailForm').append('<input type="hidden" id="endTime" name="endTime" value="" />');
					}
				}
			}
		}
		
		
		function pickedCallBack(){
			
			if($("#_amTime").length>0||$("#_pmTime").length>0||$("#_endTime").val()){
				_capDynamicContent();
			}
		}
		
		function init_ffpDetailForm(){
			var formTypeId = '${formTypeId!}';
			var curNodeName = '${curNodeName!}';
			if(formTypeId=='362'){
				if(curNodeName=='task2'||curNodeName=='task5'){
					$("#partyName_").val($("#_partyName_").val());
					$("#certType").val($("#_certType").val());
					$("#identityCard").val($("#_identityCard").val());
					$("#residenceAddrNo").val($("#_residenceAddrNo").val());
					$("#iResidenceAddr").val($("#_iResidenceAddr").val());
					
					$("#partyType").val($("#_partyType").val());
					$("#controlResult").val($("#_controlResult").val());
					$("#petitionTypes").val($("#_petitionTypes").val());
					$("#petitionAddr").val($("#_petitionAddr").val());
				}else if(curNodeName=='task6'){
					$("#curCity").val($("#_curCity").val());
					$("#isRightOrg").val($("#_isRightOrg").val());
				}else if(curNodeName=='task3'){
					$("#partyName_").val($("#_partyName_").val());
					$("#certType").val($("#_certType").val());
					$("#identityCard").val($("#_identityCard").val());
					$("#residenceAddrNo").val($("#_residenceAddrNo").val());
					$("#iResidenceAddr").val($("#_iResidenceAddr").val());
					
					$("#partyType").val($("#_partyType").val());
					$("#controlType").val($("#_controlType").val());
					$("#petitionPartyType").val($("#_petitionPartyType").val());
					$("#amTime").val($("#_amTime").val());
					$("#pmTime").val($("#_pmTime").val());
				}else if(curNodeName=='task4'){
					$("#partyName_").val($("#_partyName_").val());
					$("#certType").val($("#_certType").val());
					$("#identityCard").val($("#_identityCard").val());
					$("#residenceAddrNo").val($("#_residenceAddrNo").val());
					$("#iResidenceAddr").val($("#_iResidenceAddr").val());
					
					$("#partyType").val($("#_partyType").val());
					$("#controlType").val($("#_controlType").val());
					$("#petitionPartyType").val($("#_petitionPartyType").val());
					$("#controlResult").val($("#_controlResult").val());
					$("#petitionTypes").val($("#_petitionTypes").val());
					$("#petitionAddr").val($("#_petitionAddr").val());
				}
			}else if(formTypeId=='36201'){
				if(curNodeName=='task1'){
					
				}else if(curNodeName=='task2'){
					$("#feedbackResult").val($("#_feedbackResult").val());
					$("#endTime").val($("#_endTime").val());
					$("#stateMessage").val($("#_stateMessage").val());
				}
			}
		}
		
		function _subTaskOperate() {
			var isValid =  $("#flowSaveForm").form('validate');
			
			if(isValid && $('#bigFileUploadDivAsterik').is(':visible')) {
				isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
			}
			
			if(isValid) {
				var residenceAddrNo = $("#residenceAddrNo").val();
				if(residenceAddrNo==null||residenceAddrNo==''){
					residenceAddrNo = $("#regionCode").val();
				}
				init_ffpDetailForm();
				isValid = $("#ppDetailForm").form('validate');
				
				if(isValid) {
					modleopen();
					
					$("#ppDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportPetPer/saveReportFocus.jhtml");
					
					$("#ppDetailForm").ajaxSubmit(function(data) {
						modleclose();
	
						if(data.success && data.success == true) {
							if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
								basWorkSubTaskCallback();
							}
						} else {
							$.messager.alert('错误', data.tipMsg, 'error');
						}
					});
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
				
				if(formTypeId=='362'){
					if(curNodeName=='task2'||curNodeName=='task5'){
						advice = toDo_capDynamicContent('@partyName_@',adviceNote,'_partyName_',advice);
						advice = toDo_capDynamicContent('@petitionTypesName@',adviceNote,'_petitionTypesName',advice);
						advice = toDo_capDynamicContent('@petitionAddr@',adviceNote,'_petitionAddr',advice);
						var _controlResult = $("#_controlResult").val();
						if(_controlResult==null||_controlResult==''){
							advice = '';
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else if(curNodeName=='task6'){
						advice = toDo_capDynamicContent('@curCity@',adviceNote,'_curCity',advice);
						var isRightOrg = $("#isRightOrg").val();
						if(isRightOrg==null||isRightOrg==''){
							advice = '';
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else if(curNodeName=='task3'){
						advice = toDo_capDynamicContent('@partyName_@',adviceNote,'_partyName_',advice);
						advice = toDo_capDynamicContent('@petitionPartyTypeName@',adviceNote,'_petitionPartyTypeName',advice);
						advice = toDo_capDynamicContent('@amTime@',adviceNote,'_amTime',advice);
						advice = toDo_capDynamicContent('@pmTime@',adviceNote,'_pmTime',advice);
						var _petitionPartyType = $("#_petitionPartyType").val();
						if(_petitionPartyType==null||_petitionPartyType==''){
							advice = '';
						}else{
							if(_petitionPartyType=='01'){
								advice = advice.replaceAll('@petitionPartyTypeTime@', '3');
							}else if(_petitionPartyType=='02'){
								advice = advice.replaceAll('@petitionPartyTypeTime@', '7');
							}
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else if(curNodeName=='task4'){
						advice = toDo_capDynamicContent('@partyName_@',adviceNote,'_partyName_',advice);
						advice = toDo_capDynamicContent('@petitionAddr@',adviceNote,'_petitionAddr',advice);
						var _controlResult = $("#_controlResult").val();
						if(_controlResult==null||_controlResult==''){
							advice = '';
						}else{
							var _petitionTypes = $("#_petitionTypes").val();
							if((_controlResult=='02'||_controlResult=='03')&&_petitionTypes!=null&&_petitionTypes!=''){
								advice = toDo_capDynamicContent('@petitionTypesName@',adviceNote,'_petitionTypesName',advice);
							}
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else{
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}
				}else if(formTypeId=='36201'){
					if(curNodeName=='task1'){
						var _petitionPartyType = "${reportFocus.petitionPartyType!}";
						if(_petitionPartyType=='01'){
							advice = advice.replaceAll('@petitionPartyTypeTime@', '3');
						}else if(_petitionPartyType=='02'){
							advice = advice.replaceAll('@petitionPartyTypeTime@', '7');
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else if(curNodeName=='task2'){
						advice = toDo_capDynamicContent('@stateMessage@',adviceNote,'_stateMessage',advice);
						advice = toDo_capDynamicContent('@endTimeStr@',adviceNote,'_endTime',advice);
						var _feedbackResult = $("#_feedbackResult").val();
						if(_feedbackResult==null||_feedbackResult==''){
							advice = '';
						}
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}else{
						if(advice.indexOf('@') >= 0) {
							advice = '';
						}
					}
				}
			}
			$('#advice').val(advice);
			init_ffpDetailForm();
		}
	
		function showWorkflowDetail() {//流程详情
			var instanceId = "${instanceId!}";
			if(instanceId) {
				modleopen();
				$("#workflowDetail").panel({
					height:'auto',
					width:'auto',
					overflow:'no',
					href: "${rc.getContextPath()}/zhsq/reportPetPer/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				isRequired = false;
			
			if((formTypeId == '358' && curNodeName == 'task3') || dataSource != '05') {
				isRequired = true;
			}
			
			autoRequiredBase('ppDetailForm', isRequired);
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
			var url = "${rc.getContextPath()}/zhsq/reportPetPer/toSelectPerson.jhtml?selectUserIds="
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
		
		
		function getMarkerData_iResidenceAddr(){
			var markerOperation = $("#markerOperation").val(); // 地图操作类型
			var id = $("#id").val();
			var module = $("#module_iResidenceAddr").val(); // 模块
			if (typeof module == "undefined" || module == "") {
				module = 'EVENT_V1';
			}
			
			var showName = "标注地理位置";
			
			$.ajax({   
				 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?markerOperation='+markerOperation+'&id='+id+'&module='+module+'&t='+Math.random(),
				 type: 'POST',
				 timeout: 3000,
				 dataType:"json",
				 async: false,
				 error: function(data){
				 	$.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning'); 
				 },
				 success: function(data) {
				 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
				 		if (data && data.x != "" && data.x != null) {
				 			showName = "修改地理位置";
				 		} else {
				 			showName = "标注地理位置";
				 		}
					} else if (markerOperation == 2) { // 查看标注
						showName = "查看地理位置";
					}
					if(data){
				 	if (data.x != "" && data.x != null) {
				 		$("#x_iResidenceAddr").val(data.x);
				 	}
				 	if (data.y != "" && data.y != null) {
				 		$("#y_iResidenceAddr").val(data.y);
				 	}
			 		if (data.mapt != "" && data.mapt != null) {
				 		$("#mapt_iResidenceAddr").val(data.mapt);
				 	}}
				 }
			});
			 
			if(markerOperation == 3) {
				$("#mapTab2_iResidenceAddr").html("");//为了展示可视部分
				$("#mapTab2_iResidenceAddr").parent().addClass("mapTab3")
				  .css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
				  .attr("title", "查看地理位置");
			} else {
				$("#mapTab2_iResidenceAddr").html(showName);
			}
		}
	
		function callBackOfData_iResidenceAddr(x,y,hs,mapt) {
			$('#x_iResidenceAddr').val(x);
			$('#y_iResidenceAddr').val(y);
			$("#mapt_iResidenceAddr").val(mapt);
			$("#hs_iResidenceAddr").val(hs);
			var showName = "修改地理位置";
			$("#mapTab2_iResidenceAddr").html(showName);
			try {
				closeMaxJqueryWindowForMapMarker();
			} catch(e) {}
		}
		
		function init4Location_iResidenceAddr(locationId,startDivisionCode, option) {
			var locationOption = {
				_source : 'XIEJING',//必传参数，数据来源
				_select_scope : 0,
				_show_level : 6,//显示到哪个层级
				_context_show_level : 0,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
				_startAddress :"${reportFocus.iResidenceAddr!}",
				_startDivisionCode : startDivisionCode, //默认选中网格，非必传参数
				_customAddressIsNull : false,
				_addressMap : {//编辑页面可以传这个参数，非必传参数
					_addressMapShow : true,//是否显示地图标注功能
					_addressMapIsEdit : true
				},
				BackEvents : {
					OnSelected : function(api) {
						var isLocated = api.addressData._addressMap._addressMapIsEdit || false,
							latitude = '', longitude = '', mapType = '5',
							showName = "标注地理位置";
						
						$("#_iResidenceAddr").val(api.getAddress());
						$("#_residenceAddrNo").val(api.getInfoOrgCode());
						_capDynamicContent();
						if(isLocated == true) {
							latitude = api.addressData._addressMap._addressMapX;
							longitude = api.addressData._addressMap._addressMapY;
							mapType = api.addressData._addressMap._addressMapType;
						}
						
						if(latitude && longitude) {
							showName = "修改地理位置";
						} else {
							latitude = '';
							longitude = '';
							mapType = '';
						}
						
						$('#x_iResidenceAddr').val(latitude);
						$('#y_iResidenceAddr').val(longitude);
						$('#mapt_iResidenceAddr').val(mapType);
						$("#mapTab2_iResidenceAddr").html(showName);
					},
					OnCleared : function(api) {
						//清空按钮触发的事件
						$("#_iResidenceAddr").val('');
						$('#x_iResidenceAddr').val('');
						$('#y_iResidenceAddr').val('');
						$('#mapt_iResidenceAddr').val('');
						$("#mapTab2_iResidenceAddr").html('标注地理位置');
					}
				}
			};
			
			<#if reportFocus.resMarker_iResidenceAddr??>
				$.extend(locationOption._addressMap, {
					_addressMapX	: '${reportFocus.resMarker_iResidenceAddr.x!}',
					_addressMapY	: '${reportFocus.resMarker_iResidenceAddr.y!}',
					_addressMapType	: '${reportFocus.resMarker_iResidenceAddr.mapType!}' 
				});
			</#if>
			
			option = option || {};
			
			for(var index in option) {
				if(typeof option[index] === 'object') {
					$.extend(locationOption[index], option[index]);
				} else {
					locationOption[index] = option[index];
				}
			}
			
			$("#" + locationId).anoleAddressRender(locationOption);
		}
		
		
		
		function getMarkerData_petitionAddr(){
			var markerOperation = $("#markerOperation").val(); // 地图操作类型
			var id = $("#id").val();
			var module = $("#module_petitionAddr").val(); // 模块
			if (typeof module == "undefined" || module == "") {
				module = 'EVENT_V1';
			}
			
			var showName = "标注地理位置";
			
			$.ajax({   
				 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?markerOperation='+markerOperation+'&id='+id+'&module='+module+'&t='+Math.random(),
				 type: 'POST',
				 timeout: 3000,
				 dataType:"json",
				 async: false,
				 error: function(data){
				 	$.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning'); 
				 },
				 success: function(data) {
				 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
				 		if (data && data.x != "" && data.x != null) {
				 			showName = "修改地理位置";
				 		} else {
				 			showName = "标注地理位置";
				 		}
					} else if (markerOperation == 2) { // 查看标注
						showName = "查看地理位置";
					}
					if(data){
				 	if (data.x != "" && data.x != null) {
				 		$("#x_petitionAddr").val(data.x);
				 	}
				 	if (data.y != "" && data.y != null) {
				 		$("#y_petitionAddr").val(data.y);
				 	}
			 		if (data.mapt != "" && data.mapt != null) {
				 		$("#mapt_petitionAddr").val(data.mapt);
				 	}}
				 }
			});
			 
			if(markerOperation == 3) {
				$("#mapTab2_petitionAddr").html("");//为了展示可视部分
				$("#mapTab2_petitionAddr").parent().addClass("mapTab3")
				  .css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
				  .attr("title", "查看地理位置");
			} else {
				$("#mapTab2_petitionAddr").html(showName);
			}
		}
	
		function callBackOfData_petitionAddr(x,y,hs,mapt) {
			$('#x_petitionAddr').val(x);
			$('#y_petitionAddr').val(y);
			$("#mapt_petitionAddr").val(mapt);
			$("#hs_petitionAddr").val(hs);
			var showName = "修改地理位置";
			$("#mapTab2_petitionAddr").html(showName);
			try {
				closeMaxJqueryWindowForMapMarker();
			} catch(e) {}
		}
		
		function init4Location_petitionAddr(locationId, startDivisionCode,option) {
			var locationOption = {
				_source : 'XIEJING',//必传参数，数据来源
				_select_scope : 0,
				_show_level : 6,//显示到哪个层级
				_context_show_level : 0,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
				_startAddress :"${reportFocus.petitionAddr!}",
				_startDivisionCode : startDivisionCode, //默认选中网格，非必传参数
				_customAddressIsNull : false,
				_addressMap : {//编辑页面可以传这个参数，非必传参数
					_addressMapShow : true,//是否显示地图标注功能
					_addressMapIsEdit : true
				},
				BackEvents : {
					OnSelected : function(api) {
						var isLocated = api.addressData._addressMap._addressMapIsEdit || false,
							latitude = '', longitude = '', mapType = '5',
							showName = "标注地理位置";
						
						$("#_petitionAddr").val(api.getAddress());
						_capDynamicContent();
						if(isLocated == true) {
							latitude = api.addressData._addressMap._addressMapX;
							longitude = api.addressData._addressMap._addressMapY;
							mapType = api.addressData._addressMap._addressMapType;
						}
						
						if(latitude && longitude) {
							showName = "修改地理位置";
						} else {
							latitude = '';
							longitude = '';
							mapType = '';
						}
						
						$('#x_petitionAddr').val(latitude);
						$('#y_petitionAddr').val(longitude);
						$('#mapt_petitionAddr').val(mapType);
						$("#mapTab2_petitionAddr").html(showName);
					},
					OnCleared : function(api) {
						//清空按钮触发的事件
						_capDynamicContent();
						$("#_petitionAddr").val('');
						$('#x_petitionAddr').val('');
						$('#y_petitionAddr').val('');
						$('#mapt_petitionAddr').val('');
						$("#mapTab2_petitionAddr").html('标注地理位置');
					}
				}
			};
			
			<#if reportFocus.resMarker_petitionAddr??>
				$.extend(locationOption._addressMap, {
					_addressMapX	: '${reportFocus.resMarker_petitionAddr.x!}',
					_addressMapY	: '${reportFocus.resMarker_petitionAddr.y!}',
					_addressMapType	: '${reportFocus.resMarker_petitionAddr.mapType!}' 
				});
			</#if>
			
			option = option || {};
			
			for(var index in option) {
				if(typeof option[index] === 'object') {
					$.extend(locationOption[index], option[index]);
				} else {
					locationOption[index] = option[index];
				}
			}
			
			$("#" + locationId).anoleAddressRender(locationOption);
		}
		
		
		function initPetitionTypes(){
			var _petitionTypes = $("#_petitionTypes").val() || '';
			var petitionTypes = "${reportFocus.petitionTypes!''}";
			var flag = true;
			var t_petitionTypes = _petitionTypes.split(",");
			var tpetitionTypes = petitionTypes.split(",");
			if(t_petitionTypes.length!=tpetitionTypes.length){
				flag = false;
			}
			if(flag){
				for(var i=0;i<tpetitionTypes.length;i++){
					var temp = false;
					for(var j=0;j<t_petitionTypes.length;j++){
						if(tpetitionTypes[i]==t_petitionTypes[j]){
							temp = true;
						}
					}
					if(!temp){
						flag = false;
						break;
					}
				}
			}
			if(!flag){
				$("#_petitionTypes").val(petitionTypes);
				var petitionTypesName = "${reportFocus.petitionTypesName!''}";
				$("#_petitionTypesName").val(petitionTypesName);
				
				AnoleApi.initTreeComboBox("_petitionTypesName", "_petitionTypes", "A001130002", function(dictValue, item) {
					_capDynamicContent($('#dynamicContent').val(), dictValue);
				}, tpetitionTypes, {
					RenderType : "01",
					ShowOptions: {
						EnableToolbar : false
					}
				});
			}
		}
		
		function reSizeResmarker(){
			setTimeout(initReSizeResmarker,600);
		}
		
		function initReSizeResmarker(){
			$("#MaxJqueryWindowForCross").parent().css("top","0px");
		}
	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/petitionPerson/detail_base.ftl" />
</html>