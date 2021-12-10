<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>营商环境问题详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
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
		</style>
	</head>
	
	<body>
		<div class="container_fluid">
			<!-- 顶部标题 -->
			<div id="formDiv" class="form-warp-sh form-warp-sh-min"><!-- 外框 -->
				<div id="topTitleDiv" class="fw-toptitle">
					<div class="fw-tab">
						<ul id="topTitleUl" class="fw-tab-min clearfix">
							<li><a href="##" divId="mainDiv" class="active">营商环境问题信息</a></li>
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
					<form id="bpDetailForm" name="bpDetailForm" action="" method="post" enctype="multipart/form-data">
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
						<input type="hidden" id="module" value="BUSINESS_PROBLEM"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						<input name="orgScopeName" id="orgScopeName" type="hidden" value="${reportFocus.orgScopeName!''}" />
						
						<div class="fw-det-tog fw-det-tog-n">
							<div class="" >
								<ul>
									<li>
										<p>
											<p id="resmarkerDiv" style="margin-top: -4px;"></p>&nbsp;<p style="font-size: 16px;<#if !(reportFocus.occurred??)>color: #C0C0C0;</#if>">${reportFocus.occurred!'（暂无发生地址信息！）'}</p>
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
									<#if reportFocus.dataSource=='02'>
									<#if reportFocus.doOrgName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>部门名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doOrgName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.doBusiness>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>办理业务：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doBusiness!}</div>
										</td>
									</tr>
									</#if>
									</#if>
									<#if reportFocus.dataSource=='03'||reportFocus.dataSource=='04'>
									<#if reportFocus.enterpriseName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>企业名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.enterpriseName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.mainBusiness>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>主营业务：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.mainBusiness!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.doOrgName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>办理部门：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doOrgName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.doBusiness>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>办理业务：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doBusiness!}</div>
										</td>
									</tr>
									</#if>
									</#if>
									<#if reportFocus.dataSource=='01'||reportFocus.dataSource=='05'>
									<tr>
										<td <#if reportFocus.massesName><#else>colspan="2"</#if>>
											<label class="LabName"><span><label class="Asterik">*</label>反映对象：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportObjStr!}</div>
										</td>
										<#if reportFocus.massesName>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>群众姓名：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.massesName!}</div>
										</td>
										</#if>
									</tr>
									<#if reportFocus.enterpriseName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>企业名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.enterpriseName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.reflectWay>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>反映途径：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.reflectWay!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.openWork>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>开展业务：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.openWork!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.doOrgName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>部门名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doOrgName!}</div>
										</td>
									</tr>
									</#if>
									</#if>
									<#if reportFocus.dataSource=='06'>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>企业名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.enterpriseName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.dataSource=='07'>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>企业名称：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.enterpriseName!}</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>注册地：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.statePlace!}</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span>开展活动：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.openMove!}</div>
										</td>
									</tr>
									</#if>
									<tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
										<td colspan="2">
											<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>问题描述：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
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
									<#if reportFocus.isProblem>
									<tr>
										<td>
											<label class="LabName">
												<span><label class="Asterik">*</label>营商问题：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.isProblemStr!}</div>
										</td>
										<#if reportFocus.feedbackTime>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>反馈时限：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.feedbackTimeStr!}</div>
										</td>
										</#if>
									</tr>
									</#if>
									<#if reportFocus.dutyOrgName>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>责任部门：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.dutyOrgName!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.leadOrgName>
									<tr style="display:none;">
										<td colspan="2">
											<label class="LabName">
												<span>牵头部门：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.leadOrgName!}</div>
										</td>
									</tr>
									</#if>
									
									<#if reportFocus.feedbackType>
									<tr>
										<td>
											<label class="LabName">
												<span><label class="Asterik">*</label>反馈类型：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.feedbackTypeStr!}</div>
										</td>
										<#if reportFocus.doCondition>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>处置情况：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.doConditionName!}</div>
										</td>
										</#if>
									</tr>
									</#if>
									<#if reportFocus.doDepartment>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>处置部门：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doDepartment!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.doWorkMessage>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>已开展工作：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.doWorkMessage!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.situationMeaasge>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>情况说明：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.situationMeaasge!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.isDelay>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>是否延期：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.isDelayStr!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.disagreeRemark>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>理由：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.disagreeRemark!}</div>
										</td>
									</tr>
									</#if>
									<#if reportFocus.isOrgScope>
									<tr>
										<td>
											<label class="LabName">
												<span><label class="Asterik">*</label>部门范围：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.isOrgScopeStr!}</div>
										</td>
										<#if reportFocus.doDate>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>受理日期：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.doDateStr!}</div>
										</td>
										</#if>
									</tr>
									</#if>
									<#if reportFocus.dutyName>
									<tr>
										<td>
											<label class="LabName">
												<span><label class="Asterik">*</label>负责人：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.dutyName!}</div>
										</td>
										<#if reportFocus.dutyTel>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>联系方式：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.dutyTel!}</div>
										</td>
										</#if>
									</tr>
									</#if>
									<#if reportFocus.dutyRemark>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>三定方案：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.dutyRemark!}</div>
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
					attachmentData	: {bizId: reportId, attachmentType:'BUSINESS_PROBLEM', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				},
				attachFileUploadOpt = {
					useType		: 'view',
					fileExt		: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					module		: 'businessProblem',
					attachmentData	: {bizId: reportId, attachmentType:'BUSINESS_PROBLEM', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				};
			
			$('#bpDetailForm .autoWidth').each(function() {
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
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportBusPro/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportBusPro/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportBusPro/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
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
			autoRequiredRender();
			
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
				mapType = 'BUSINESS_PROBLEM',
				isEdit = false;
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#bpDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportBusPro/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#bpDetailForm").ajaxSubmit(function(data) {
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
							'<div class="FontDarkBlue fl DealMan Check_Radio" style="margin-left: 76px;"><b name="htmlSelectUserNames" id="htmlSelectUserNames"></b></div>'+
							'<input type="hidden" name="selectUserNames" id="selectUserNames"/>'+
							'<input type="hidden" name="selectOrgNames" id="selectOrgNames"/>'+
						'</td>'+
					'</tr>'
				);
			}
			
		}
	
		function radioCheckCallback(data) {//下一环节选中回调方法
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				dynamicContentNextOrgNameLabel = '@nextOrgNames@',
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val();
			
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
			if(curNodeName=='task1'){
				if($("#dataSource").val()=='05'){
					var reportObj = '${reportFocus.reportObj}';
					var temp = "adviceNote_" + (parseInt(reportObj)-1);
					$('#adviceNote').val(dynamicContentMap[temp]);
					$('#advice').val($('#adviceNote').val());
				}else{
					if($("#dataSource").val()=='01'){
						var reportObj = '${reportFocus.reportObj!''}'
						if(adviceNote.indexOf('企业/群众') >= 0) {
							if(reportObj=='1'){
								adviceNote = adviceNote.replaceAll('企业/群众', '企业');
							}else if(reportObj=='2'){
								adviceNote = adviceNote.replaceAll('企业/群众', '群众');
							}
						}
					}
					$('#advice').val(adviceNote);
				}
			}else if(curNodeName=='task2'){
				if($('#isProblemTr').length == 0) {
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')=='task4'){
					    	$(this).parent().hide();
					    }
					});
				}else{
					_capDynamicContent();
				}
				
				if($('#isProblemTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="isProblemTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>营商问题：</span></label>' + 
								'<input type="hidden" id="_isProblem" name="_isProblem" value="1" />' + 
								'<input type="hidden" id="_task3ReceiveTime" name="_task3ReceiveTime" value="1" />' +
								'<input type="text" class="inp1 easyui-validatebox" style="width:80px;" data-options="required:true,tipPosition:\'bottom\'" id="_isProblemStr" name="_isProblemStr" value="是" />' + 
							'</td>' + 
						'</tr>'
					);
					$('#bpDetailForm').append('<input type="hidden" id="isProblem" name="isProblem" value="" />');
					$('#bpDetailForm').append('<input type="hidden" id="task3ReceiveTime" name="task3ReceiveTime" value="" />');
					AnoleApi.initListComboBox("_isProblemStr", "_isProblem", "", function(dictValue, item) {
						//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
						_capDynamicContent($('#dynamicContent').val(), dictValue);
					}, '1', { 
						DataSrc: [{'name':'是', 'value':'1'},{'name':'否', 'value':'0'}],
						OnChanged: function(value) {
							if(value == '1') {
								$("#dutyOrgNameTr").show();
								//$("#leadOrgNameTr").show();
								$("#feedbackTimeTr").hide();
								$('#_dutyOrgName').validatebox({
									required: true
								});
								$('#_feedbackTime').validatebox({
									required: false
								});
								$("#_feedbackTime").val('');
								$("#_task3ReceiveTime").val('1');
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task4'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task3'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							} else {
								$("#feedbackTimeTr").show();
								$("#dutyOrgNameTr").hide();
								$("#leadOrgNameTr").hide();
								$('#_dutyOrgName').validatebox({
									required: false
								});
								$('#_feedbackTime').validatebox({
									required: true
								});
								$("#_dutyOrgName").val('');
								$("#_leadOrgName").val('');
								$("#_task3ReceiveTime").val('');
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task3'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task4'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}
						},
					});
					$('#_isProblemStr').validatebox({
						required: true
					});
					
					$('#isProblemTr').show();
				}
				
				var dataSource = $("#dataSource").val();
				if(dataSource==='09'){
					if($('#gridNameTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="gridNameTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>所属区域：</span></label>' +
									'<input type="text" id="gridName_" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox inputWidth" data-options="required:true,tipPosition:\'bottom\'"/>' +
									'<input type="hidden" id="regionCode_" name="regionCode_" value="${reportFocus.regionCode!}" />' + 
								'</td>' + 
							'</tr>'
						);
						$('#regionCode_').validatebox({
							required: true
						});
						
						$('#gridNameTr').show();
						$('#bpDetailForm').append('<input type="hidden" id="_regionCode_" name="_regionCode_" value="" />');
					
						//加载网格
						AnoleApi.initGridZtreeComboBox("gridName_", null,
								function(gridId, items) {
									if (items && items.length > 0) {
										$("#regionCode_").val(items[0].orgCode);
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
					}
				}
				
				if($('#dutyOrgNameTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dutyOrgNameTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>责任部门：</span></label>' +
								'<input id="_dutyOrgName" name="_dutyOrgName" class="inp1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
							'</td>' + 
						'</tr>'
					);
					$('#_dutyOrgName').validatebox({
						required: true
					});
					
					$('#dutyOrgNameTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="dutyOrgName" name="dutyOrgName" value="" />');
				}
				
				
				if($('#leadOrgNameTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="leadOrgNameTr">' + 
							'<td>' + 
								'<label class="LabName"><span>牵头部门：</span></label>' +
								'<input id="_leadOrgName" name="_leadOrgName" class="inp1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
							'</td>' + 
						'</tr>'
					);
					$('#_leadOrgName').validatebox({
						required: false
					});
					
					$('#leadOrgNameTr').hide();
					$('#bpDetailForm').append('<input type="hidden" id="leadOrgName" name="leadOrgName" value="" />');
				}
				
				if($('#feedbackTimeTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="feedbackTimeTr" style="display:none;">' + 
							'<td>' + 
								'<label class="LabName labelNameWide"><span><label class="Asterik">*</label>反馈时限：</span></label>' + 
								'<input type="text" id="_feedbackTime" name="_feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({onpicked:pickedCallBack,readOnly:true, dateFmt:\'yyyy-MM-dd HH:mm:ss\', isShowClear:false, isShowToday:false})" value="" readonly="readonly"></input>'+
							'</td>' + 
						'</tr>'
					);
					
					$('#_feedbackTime').validatebox({
						required: false
					});
					
					$('#bpDetailForm').append('<input type="hidden" id="feedbackTime" name="feedbackTime" value="" />');
				}
				
				var winWidth = $(window).width();
				$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
					$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
						   .addClass("isSettledAutoWidth");
				});
			}else if(curNodeName=='task3'){
				if($('#feedbackTypeTr').length == 0) {
					var orgScopeName = '${orgScopeName!}';
					$("#orgScopeName").val(orgScopeName);
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')=='task0'||$(this).attr('nodename')=='task6'){
					    	$(this).parent().hide();
					    }
					});
				}else{
					var doCondition = $("#_doCondition").val();
					if(doCondition!=null&&doCondition!=''){
						if(nextNodeName!='task6'){
							var temp = "adviceNote_" + (parseInt(doCondition)-1);
							$('#adviceNote').val(dynamicContentMap[temp]);
						}
					}
					_capDynamicContent();
				}
				
				if($('#feedbackTypeTr').length == 0) {
					var isDelay = "${reportFocus.isDelay!''}";
					if('2'==isDelay){
						$('#tr_epath').before(
							'<tr id="feedbackTypeTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>反馈类型：</span></label>' + 
									'<input type="hidden" id="_feedbackType" name="_feedbackType" value="3" />' + 
									'<input type="hidden" id="_task3SubTime" name="_task3SubTime" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_feedbackTypeStr" name="_feedbackTypeStr" value="限期外反馈" />' + 
								'</td>' + 
							'</tr>'
						);
						AnoleApi.initListComboBox("_feedbackTypeStr", "_feedbackType", "", function(dictValue, item) {
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, "3", {
							DataSrc: [{'name':'限期外反馈', 'value':'3'}],
							OnChanged: function(value) {
								
							},
						});
					}else{
						$('#tr_epath').before(
							'<tr id="feedbackTypeTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>反馈类型：</span></label>' + 
									'<input type="hidden" id="_feedbackType" name="_feedbackType" value="" />' + 
									'<input type="hidden" id="_task3SubTime" name="_task3SubTime" value="" />' + 
									'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_feedbackTypeStr" name="_feedbackTypeStr" value="" />' + 
								'</td>' + 
							'</tr>'
						);
						AnoleApi.initListComboBox("_feedbackTypeStr", "_feedbackType", "", function(dictValue, item) {
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}, null, {
							DataSrc: [{'name':'中期报告', 'value':'1'},{'name':'限期内反馈', 'value':'2'}],
							OnChanged: function(value) {
								var doCondition = $("#_doCondition").val();
								var str = '';
								if(doCondition!=''&&doCondition!=null){
									$("#_doCondition").val('');
									$("#_doConditionStr").val('');
									$("#_situationMeaasge").val('');
								}
								if(value == '1') {
									init_doCondition('B210010003');
								
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='task5'||$(this).attr('nodename')=='task6'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='task0'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								} else if(value == '2') {
									init_doCondition('B210010004');
								
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='task0'||$(this).attr('nodename')=='task6'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='task5'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}
							},
						});
					}
					
					$('#_feedbackTypeStr').validatebox({
						required: true
					});
					
					$('#feedbackTypeTr').show();
					
					$('#bpDetailForm').append('<input type="hidden" id="feedbackType" name="feedbackType" value="" />');
					$('#bpDetailForm').append('<input type="hidden" id="task3SubTime" name="task3SubTime" value="" />');
					
				}
				
				if($('#doConditionTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="doConditionTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>处置情况：</span></label>' + 
								'<input type="hidden" id="_doCondition" name="_doCondition" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_doConditionStr" name="_doConditionStr" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					
					init_doCondition('B210010003');
					
					$('#_doConditionStr').validatebox({
						required: true
					});
					
					$('#doConditionTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="doCondition" name="doCondition" value="" />');
				}
				
				if($('#doDepartmentTr').length == 0) {
					var doDepartment = "${reportFocus.doDepartment!''}";
					$('#remarkTr').before(
						'<tr id="doDepartmentTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>处置部门：</span></label>' +
								'<input id="_doDepartment" name="_doDepartment" class="inp1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="'+doDepartment+'"/>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_doDepartment').validatebox({
						required: true
					});
					
					$('#doDepartmentTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="doDepartment" name="doDepartment" value="" />');
				}
				
				if($('#doWorkMessageTr').length == 0) {
					var doWorkMessage = "${reportFocus.doWorkMessage!''}";
					$('#remarkTr').before(
						'<tr id="doWorkMessageTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>开展工作：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="_doWorkMessage" name="_doWorkMessage" class="area1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());">'+doWorkMessage+'</textarea>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_doWorkMessage').validatebox({
						required: true
					});
					
					$('#doWorkMessageTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="doWorkMessage" name="doWorkMessage" value="" />');
				}
				
				if($('#situationMeaasgeTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="situationMeaasgeTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>情况说明：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="_situationMeaasge" name="_situationMeaasge" class="area1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[1000]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_situationMeaasge').validatebox({
						required: true
					});
					
					$('#situationMeaasgeTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="situationMeaasge" name="situationMeaasge" value="" />');
				}
			}else if(curNodeName=='task4'){
				if($('#isOrgScopeTr').length == 0) {
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')=='task2'){
					    	$(this).parent().hide();
					    }
					});
					var orgScopeName = '${orgScopeName!}';
					$("#orgScopeName").val(orgScopeName);
				}
				if($('#isOrgScopeTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="isOrgScopeTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>部门范围：</span></label>' + 
								'<input type="hidden" id="_isOrgScope" name="_isOrgScope" value="1" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:80px;" data-options="required:true,tipPosition:\'bottom\'" id="_isOrgScopeStr" name="_isOrgScopeStr" value="是" />' + 
							'</td>' + 
						'</tr>'
					);
					AnoleApi.initListComboBox("_isOrgScopeStr", "_isOrgScope", "", function(dictValue, item) {
						//$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
						_capDynamicContent($('#dynamicContent').val(), dictValue);
					}, '1', {
						DataSrc: [{'name':'是', 'value':'1'},{'name':'否', 'value':'2'}],
						OnChanged: function(value) {
							if(value == '1') {
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task2'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task5'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
								$('#doDateTr').show();
								$('#_doDate').validatebox({
									required: true
								});
								$('#dutyNameTr').show();
								$('#_dutyName').validatebox({
									required: true
								});
								$('#dutyTelTr').show();
								$('#_dutyTel').validatebox({
									required: true
								});
								
								$('#dutyRemarkTr').hide();
								$('#_dutyRemark').val('');
								$('#_dutyRemark').validatebox({
									required: false
								});
								var orgScopeName = '${orgScopeName!}';
								$("#orgScopeName").val(orgScopeName);
							} else if(value == '2') {
								$('#doDateTr').hide();
								$('#_doDate').val('');
								$('#_doDate').validatebox({
									required: false
								});
								$('#dutyNameTr').hide();
								$('#_dutyName').val('');
								$('#_dutyName').validatebox({
									required: false
								});
								$('#dutyTelTr').hide();
								$('#_dutyTel').val('');
								$('#_dutyTel').validatebox({
									required: false
								});
								$('#dutyRemarkTr').show();
								$('#_dutyRemark').validatebox({
									required: true
								});
								$("#orgScopeName").val("");
								
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task5'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task2'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}
						},
					});
					$('#_isOrgScopeStr').validatebox({
						required: true
					});
					
					$('#isOrgScopeTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="isOrgScope" name="isOrgScope" value="" />');
				}
				
				if($('#doDateTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="doDateTr">' + 
							'<td>' + 
								'<label class="LabName labelNameWide"><span><label class="Asterik">*</label>受理日期：</span></label>' + 
								'<input type="text" id="_doDate" name="_doDate" class="inp1 Wdate easyui-validatebox singleCellClass" style="width:170px; cursor:pointer;" data-options="required:true" onclick="WdatePicker({onpicked:pickedCallBack,readOnly:true, dateFmt:\'yyyy-MM-dd HH:mm:ss\', isShowClear:false, isShowToday:true})" value="" readonly="readonly"></input>'+
							'</td>' + 
						'</tr>'
					);
					
					$('#_doDate').validatebox({
						required: true
					});
					
					$('#doDateTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="doDate" name="doDate" value="" />');
				}
				
				if($('#dutyNameTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dutyNameTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>负责人：</span></label>' +
								'<input id="_dutyName" name="_dutyName" style="width:160px;" class="inp1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_dutyName').validatebox({
						required: true
					});
					
					$('#dutyNameTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="dutyName" name="dutyName" value="" />');
				}
				
				if($('#dutyTelTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dutyTelTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>联系方式：</span></label>' +
								'<input id="_dutyTel" name="_dutyTel" style="width:160px;" class="inp1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\',\'mobileorphone\']" onblur="_capDynamicContent($(this).val());" value=""/>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_dutyTel').validatebox({
						required: true
					});
					
					$('#dutyTelTr').show();
					$('#bpDetailForm').append('<input type="hidden" id="dutyTel" name="dutyTel" value="" />');
				}
				
				if($('#dutyRemarkTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dutyRemarkTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>三定方案：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="_dutyRemark" name="_dutyRemark" class="area1 easyui-validatebox doubleCellClass" data-options="tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
					
					$('#_dutyRemark').validatebox({
						required: false
					});
					
					$('#dutyRemarkTr').hide();
					$('#bpDetailForm').append('<input type="hidden" id="dutyRemark" name="dutyRemark" value="" />');
				}
			}else if(curNodeName=='task5'){
				var doCondition = '${reportFocus.doCondition!}';
				var isProblem = '${reportFocus.isProblem!}';
				if(isProblem=='1'){
					if($('#situationMeaasgeTr').length == 0) {
						$('#remarkTr').before(
							'<tr id="situationMeaasgeTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>情况说明：</span></label>' +
									'<textarea rows="3" style="height:50px;" id="_situationMeaasge" name="_situationMeaasge" class="area1 easyui-validatebox doubleCellClass" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[1000]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
								'</td>' + 
							'</tr>'
						);
						$('#_situationMeaasge').validatebox({
							required: false
						});
						
						$('#situationMeaasgeTr').show();
						$('#bpDetailForm').append('<input type="hidden" id="situationMeaasge" name="situationMeaasge" value="" />');
						
						var str = '';
						if(doCondition=='01'){
							str += '您/贵部门（单位）反映的'+'${reportFocus.tipoffContent!}'+'营商环境问题，经'+'${reportFocus.doDepartment!}'+'部门（单位）办理，已经解决，请前往xxx部门（单位）办理。若已办理，可忽略本信息，感谢您的支持！';
							$("#_situationMeaasge").val(str);
						}else if(doCondition=='02'){
							str += '您/贵部门（单位）反映的'+'${reportFocus.tipoffContent!}'+'营商环境问题，经'+'${reportFocus.doDepartment!}'+'部门（单位）协调解决，取得xxxx成果，预计可在xx年xx月xx日前完成处置，感谢您的理解！';
							$("#_situationMeaasge").val(str);
						}else if(doCondition=='03'){
							str += '您/贵部门（单位）反映的'+'${reportFocus.tipoffContent!}'+'营商环境问题，因xxxxxx等客观因素，短期内无法解决，需xxxxxx（要能体现这种情况下如何解决），感谢您的理解！';
							$("#_situationMeaasge").val(str);
						}else if(doCondition=='04'){
							str += '根据《xxxxxx》规定：“xxxx”，您/贵部门（单位）反映的'+'${reportFocus.tipoffContent!}'+'问题暂时无法解决，敬请谅解！若反映问题的部门（单位）/个人有疑议，可于xx月xx日至xx部门（单位）xx科面谈或拨打xxxx电话进行沟通。';
							$("#_situationMeaasge").val(str);
						}
						//var temp = "adviceNote_" + (parseInt(doCondition)-1);
						//$('#adviceNote').val(dynamicContentMap[temp]);
						_capDynamicContent();
						$('#advice').val(str);
					}
				}else{
					_capDynamicContent();
					$('#advice').val($('#adviceNote').val());
				}
			}else if(curNodeName=='task6'){
				if($('#isDelayTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="isDelayTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>是否延期：</span></label>' + 
								'<input type="hidden" id="_isDelay" name="_isDelay" value="" />' + 
								'<input type="hidden" id="_delaySize" name="_delaySize" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:80px;" data-options="required:true,tipPosition:\'bottom\'" id="_isDelayStr" name="_isDelayStr" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					$('#bpDetailForm').append('<input type="hidden" id="isDelay" name="isDelay" value="" />');
					$('#bpDetailForm').append('<input type="hidden" id="delaySize" name="delaySize" value="" />');
					AnoleApi.initListComboBox("_isDelayStr", "_isDelay", "", function(dictValue, item) {
						var temp = "adviceNote_" + (parseInt(dictValue)-1);
						$('#adviceNote').val(dynamicContentMap[temp]);
						_capDynamicContent($('#dynamicContent').val(), dictValue);
					}, '1', { 
						DataSrc: [{'name':'同意', 'value':'1'},{'name':'不同意', 'value':'2'}],
						OnChanged: function(value) {
							if(value == '1') {
								$("#disagreeRemarkTr").hide();
								$('#_disagreeRemark').validatebox({
									required: false
								});
								$("#_disagreeRemark").val('');
								var delaySize = "${reportFocus.delaySize!'0'}";
								$("#_delaySize").val(Number(delaySize)+1);
							} else {
								$("#disagreeRemarkTr").show();
								$('#_disagreeRemark').validatebox({
									required: true
								});
								var delaySize = "${reportFocus.delaySize!'0'}";
								$("#_delaySize").val(delaySize);
							}
						},
					});
					$('#_isDelayStr').validatebox({
						required: true
					});
					
					$('#isDelayTr').show();
				}
				
				if($('#disagreeRemarkTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="disagreeRemarkTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>理由：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="_disagreeRemark" name="_disagreeRemark" class="area1 easyui-validatebox doubleCellClass" data-options="tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
					$('#_disagreeRemark').validatebox({
						required: false
					});
					
					$('#disagreeRemarkTr').hide();
					$('#bpDetailForm').append('<input type="hidden" id="disagreeRemark" name="disagreeRemark" value="" />');
					
				}
			}
		}
		
		
		function pickedCallBack(){
			
			var doDate = $("#_doDate").val();
			var feedbackTime = $("#_feedbackTime").val();
			if($("#_doDate").length>0||$("#_feedbackTime").length>0){
				_capDynamicContent();
			}
		}
		
		function init_ffpDetailForm(){
			var curNodeName = '${curNodeName!}';
			if(curNodeName=='task2'){
				$("#task3ReceiveTime").val($("#_task3ReceiveTime").val());
				$("#isProblem").val($("#_isProblem").val());
				$("#dutyOrgName").val($("#_dutyOrgName").val());
				$("#leadOrgName").val($("#_leadOrgName").val());
				$("#feedbackTime").val($("#_feedbackTime").val());
				
				if($("#dataSource").val()=='09'){
					$("#_regionCode_").val($("#regionCode_").val());
				}
			}else if(curNodeName=='task3'){
				$("#task3SubTime").val($("#_task3SubTime").val());
				$("#feedbackType").val($("#_feedbackType").val());
				$("#doCondition").val($("#_doCondition").val());
				$("#doDepartment").val($("#_doDepartment").val());
				$("#doWorkMessage").val($("#_doWorkMessage").val());
				$("#situationMeaasge").val($("#_situationMeaasge").val());
			}else if(curNodeName=='task4'){
				$("#isOrgScope").val($("#_isOrgScope").val());
				$("#doDate").val($("#_doDate").val());
				$("#dutyName").val($("#_dutyName").val());
				$("#dutyTel").val($("#_dutyTel").val());
				$("#dutyRemark").val($("#_dutyRemark").val());
			}else if(curNodeName=='task5'){
				if($("#_situationMeaasge").length>0){
					$("#situationMeaasge").val($("#_situationMeaasge").val());
				}
			}else if(curNodeName=='task6'){
				$("#isDelay").val($("#_isDelay").val());
				$("#disagreeRemark").val($("#_disagreeRemark").val());
				$("#delaySize").val($("#_delaySize").val());
			}
		}
		
		function _subTaskOperate() {
			var isValid =  $("#flowSaveForm").form('validate');
			
			if(isValid && $('#bigFileUploadDivAsterik').is(':visible')) {
				isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
			}
			
			if(isValid) {
				init_ffpDetailForm();
				isValid = $("#bpDetailForm").form('validate');
				
				if(isValid) {
					modleopen();
					
					$("#bpDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportBusPro/saveReportFocus.jhtml");
					
					$("#bpDetailForm").ajaxSubmit(function(data) {
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
				
				if(curNodeName=='task1'){
					if($("#dataSource").val()=='05'){
						var reportObj = '${reportFocus.reportObj}';
						var temp = "adviceNote_" + (parseInt(reportObj)-1);
						$('#adviceNote').val($("#"+temp).val());
						advice = $('#adviceNote').val();
					}else{
						if($("#dataSource").val()=='01'){
							var reportObj = '${reportFocus.reportObj!''}'
							if(advice.indexOf('企业/群众') >= 0) {
								if(reportObj=='1'){
									advice = advice.replaceAll('企业/群众', '企业');
								}else if(reportObj=='2'){
									advice = advice.replaceAll('企业/群众', '群众');
								}
							}
						}
					}
				}else if(curNodeName=='task2'){
					advice = toDo_capDynamicContent('@dutyOrgName@',adviceNote,'_dutyOrgName',advice);
					advice = toDo_capDynamicContent('@leadOrgName@',adviceNote,'_leadOrgName',advice);
					advice = toDo_capDynamicContent('@feedbackTime@',adviceNote,'_feedbackTime',advice);
					if(advice.indexOf('@leadOrgName@') >= 0) {
						advice = advice.replaceAll('@leadOrgName@', '');
					}
					if(advice.indexOf('@feedbackTime@') >= 0){
						advice = '';
					} 
					if(advice.indexOf('@dutyOrgName@') >= 0){
						advice = '';
					}
				}else if(curNodeName=='task3'){
					advice = toDo_capDynamicContent('@doDepartment@',adviceNote,'_doDepartment',advice);
					advice = toDo_capDynamicContent('@doWorkMessage@',adviceNote,'_doWorkMessage',advice);
					advice = toDo_capDynamicContent('@situationMeaasge@',adviceNote,'_situationMeaasge',advice);
					
					if(advice.indexOf('@') >= 0) {
						advice = '';
					}
				}else if(curNodeName=='task4'){
					advice = toDo_capDynamicContent('@doDate@',adviceNote,'_doDate',advice);
					advice = toDo_capDynamicContent('@dutyName@',adviceNote,'_dutyName',advice);
					advice = toDo_capDynamicContent('@dutyTel@',adviceNote,'_dutyTel',advice);
					advice = toDo_capDynamicContent('@dutyRemark@',adviceNote,'_dutyRemark',advice);
					if(advice.indexOf('@') >= 0) {
						advice = '';
					}
				}else if(curNodeName=='task5'){
					advice = toDo_capDynamicContent('@situationMeaasge@',adviceNote,'_situationMeaasge',advice);
					if(advice.indexOf('@') >= 0) {
						advice = '';
					}
				}else if(curNodeName=='task6'){
					advice = toDo_capDynamicContent('@disagreeRemark@',adviceNote,'_disagreeRemark',advice);
					if(advice.indexOf('@') >= 0) {
						advice = '';
					}
				}else{
					if(advice.indexOf('@') >= 0) {
						advice = '';
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
					href: "${rc.getContextPath()}/zhsq/reportBusPro/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
			
			autoRequiredBase('bpDetailForm', isRequired);
		}
		
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchFFPFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
		
		function init_doCondition(code){
			$("#_task3SubTime").val('');
			var isDelay = "${reportFocus.isDelay!''}";
			if(isDelay=='2'){
				AnoleApi.initListComboBox("_doConditionStr", "_doCondition", "", function(dictValue, item) {
						var temp = "adviceNote_" + (parseInt(dictValue)-1);
						$('#adviceNote').val($("#"+temp).val());
						_capDynamicContent($('#dynamicContent').val(), dictValue);
					}, null, {
						DataSrc: [{'name':'已完成处置', 'value':'01'},{'name':'短期内无法解决', 'value':'03'}],
						OnChanged: function(value) {
							var str = '';
							if(value == '01') {
								str = '已完成处置，用时xx个工作日';
								$("#_situationMeaasge").val(str);
							} else if(value == '03') {
								str = '取得xxxx成果，因该营商环境问题涉及xxxxxx等客观因素，且短期内该营商环境问题无法解决，需xxxxxx（要能体现这种情况下如何解决）';
								$("#_situationMeaasge").val(str);
							}
						},
					});
			}else{
				AnoleApi.initListComboBox("_doConditionStr", "_doCondition", code, function(dictValue, item) {
						var nextNodeName = $('#nodeName_').val();
						if(nextNodeName!='task6'){
							var temp = "adviceNote_" + (parseInt(dictValue)-1);
							$('#adviceNote').val($("#"+temp).val());
							_capDynamicContent($('#dynamicContent').val(), dictValue);
						}
					}, null, {
						OnChanged: function(value) {
							$("#_task3SubTime").val('');
							var nextNodeName = $('#nodeName_').val();
							var feedbackType = $("#_feedbackType").val();
							var str = '';
							if(value == '01') {
								$("#doDepartmentTr").show();
								$('#_doDepartment').validatebox({
									required: true
								});
								$("#doWorkMessageTr").show();
								$('#_doWorkMessage').validatebox({
									required: true
								});
								if(feedbackType=='1'){
									str = '取得xxxx成果，预计可在限期内完成处置';
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task5'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task0'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}else if(feedbackType=='2'){
									str = '已完成处置，用时xx个工作日';
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task0'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task5'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}
								$("#_situationMeaasge").val(str);
							} else if(value == '02') {
								$("#doDepartmentTr").show();
								$('#_doDepartment').validatebox({
									required: true
								});
								$("#doWorkMessageTr").show();
								$('#_doWorkMessage').validatebox({
									required: true
								});
								if(feedbackType=='1'){
									str = '因该营商环境问题涉及xxxxxx等方面因素，10个工作日内处理难度较大，预计可在xx年xx月xx日前完成处置';
								}else if(feedbackType=='2'){
									$("#_task3SubTime").val('1');
									str = '取得xxxx成果，下阶段计划xxxx，预计可在xx年xx月xx日前完成处置，申请延期10个工作日';
									if(isDelay=='1'){
										var delaySize = "${reportFocus.delaySize!'0'}";
										str = '取得xxxx成果，下阶段计划xxxx，预计可在xx年xx月xx日前完成处置，申请再延期10个工作日，当前已延期'+delaySize+'次';
									}
									$("input[name='nextNode']").each(function(){
									    if($(this).attr('nodename')=='task5'||$(this).attr('nodename')=='task0'){
									    	$(this).parent().hide();
									    }else if($(this).attr('nodename')=='task6'){
									    	$(this).parent().show();
									    	$(this).click();
									    }
									});
								}
								$("#_situationMeaasge").val(str);
							} else if(value == '03') {
								$("#doDepartmentTr").show();
								$('#_doDepartment').validatebox({
									required: true
								});
								$("#doWorkMessageTr").show();
								$('#_doWorkMessage').validatebox({
									required: true
								});
								if(feedbackType=='1'){
									str = '因该营商环境问题涉及xxxxxx等方面因素，10个工作日内处理难度较大，且短期内该营商环境问题无法解决，需xxxxxx（要能体现这种情况下如何解决）';
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task5'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task0'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}else if(feedbackType=='2'){
									var isDelay = "${reportFocus.isDelay!''}";
									str = '取得xxxx成果，因该营商环境问题涉及xxxxxx等客观因素，且短期内该营商环境问题无法解决，需xxxxxx（要能体现这种情况下如何解决）';
									if(isDelay=='1'){
										str = '取得xxxx成果，延期后发现该营商环境问题涉及xxxxxx等客观因素，且短期内该营商环境问题无法解决，需xxxxxx（要能体现这种情况下如何解决）';
									}
									
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task0'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task5'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}
								$("#_situationMeaasge").val(str);
							} else if(value == '04') {
								$("#doDepartmentTr").hide();
								$('#_doDepartment').validatebox({
									required: false
								});
								$("#doWorkMessageTr").hide();
								$('#_doWorkMessage').validatebox({
									required: false
								});
								$("#_doDepartment").val('');
								$("#_doWorkMessage").val('');
								if(feedbackType=='1'){
									str = '根据《xxxxxx》规定：“xxxx”，'+'${reportFocus.tipoffContent!}'+'问题暂时无法解决，敬请谅解。若反映问题的部门（单位）/个人有疑议，可于xx月xx日至xx部门（单位）xx科面谈或拨打电话号码xxxx进行沟通';
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task5'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task0'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}else if(feedbackType=='2'){
									str = '因《xxxxxx》规定：“xxxx”，'+'${reportFocus.tipoffContent!}'+'营商环境问题暂时无法解决，若反映问题的部门（单位）/个人有疑议，可通知其于xx月xx日至xx部门（单位）xx科面谈或拨打xxxx电话进行沟通';
									if(nextNodeName=='task6'){
										$("input[name='nextNode']").each(function(){
										    if($(this).attr('nodename')=='task0'||$(this).attr('nodename')=='task6'){
										    	$(this).parent().hide();
										    }else if($(this).attr('nodename')=='task5'){
										    	$(this).parent().show();
										    	$(this).click();
										    }
										});
									}
								}
								$("#_situationMeaasge").val(str);
							}
						},
					});
			}
		}
		
		function openSelectPerson(){
			
			var selectUserIds = $("#selectUserIds").val();
			var selectOrgIds = $("#selectOrgIds").val();
			var selectUserNames = $("#selectUserNames").val();
			var selectOrgNames = $("#selectOrgNames").val();
			var regionCode = $("#regionCode").val();
			var url = "${rc.getContextPath()}/zhsq/reportBusPro/toSelectPerson.jhtml?selectUserIds="
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
	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/businessProblem/detail_base.ftl" />
</html>