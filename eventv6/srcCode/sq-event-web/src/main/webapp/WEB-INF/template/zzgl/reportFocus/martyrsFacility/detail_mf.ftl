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
		<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
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
				<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode}" />
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
									<#if reportFocus.dataSource == '01'>
									<td colspan="2">
										<label class="LabName"><span><label class="Asterik">*</label>是否损坏：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.isDamagedStr!}</div>
									</td>
									</#if>
									<#if listType?? && listType=='2' && reportFocus?? && reportFocus.dataSource == '02' && NODE_NAME == 'task2'>
										<tr>
											<td class="LeftTd">
												<label class="LabName"><span><label class="Asterik">*</label>纪念设施：</span></label>
												<input  id="markInit" type="hidden" value="${reportFocus.markId!}" />
												<input  id="markId" name="markId" type="hidden" value="${reportFocus.markId!}" class="easyui-validatebox" data-options="required:true" />
												<input  id="markName" name="markName" type="hidden" value="${reportFocus.markName!}" class="easyui-validatebox" data-options="required:true"/>
												<input  id="markNameHidden" name="markNameHidden"
														placeholder="纪念设施" type="text"
														style="height: 28px; width: 240px;"
														class="comboselector easyui-validatebox"
														data-options="dType:'place', afterSelect:markAfterSelect, required:true, required:true, tipPosition:'bottom',validType:['maxLength[200]','characterCheck']"
														query-params="orgCode=${reportFocus.regionCode}&plaType=1205&orderBy=points"
														value="${reportFocus.markId!}"
												/>
											</td>
										</tr>
									<#else>
										<tr>
											<td colspan="2">
												<label class="LabName">
													<span><label class="Asterik">*</label>纪念设施：</span>
												</label>
												<div class="Check_Radio FontDarkBlue">${reportFocus.markName!}</div>
											</td>
										</tr>
									</#if>
									<tr>
										<td <#if reportFocus.damageMode><#else>colspan="2"</#if>>
											<label class="LabName"><span><label class="Asterik">*</label>发现渠道：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.dataSourceName!}</div>
										</td>
										<#if reportFocus.damageMode>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>损坏方式：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.damageModeName!}</div>
										</td>
										</#if>
									</tr>
									<#if reportFocus.dataSource == '03'>
									<td colspan="2">
										<label class="LabName"><span><label class="Asterik">*</label>发现部门：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.departmentName!}</div>
									</td>
									</#if>
									<tr <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
										<td colspan="2">
											<label class="LabName"><span>处置时限：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${DUEDATESTR_!}</div>
										</td>
									</tr>
									<#if reportFocus.dataSource == '02'>
									<tr>
										<td colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>登记内容：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
										</td>
									</tr>
									</#if>
									<tr>
										<td colspan="2">
											<label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label class="Asterik" id="bigFileUploadDivMust" style="display:none;">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>附件上传：</span></label><div id="attachFileUploadDiv"></div>
										</td>
									</tr>
									<#if reportFocus.feedbackType>
									<tr>
										<td>
											<label class="LabName">
												<span><label class="Asterik">*</label>反馈情况：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.feedbackTypeName!}</div>
										</td>
										<#if reportFocus.doType>
										<td>
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>处置结果：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.doTypeName!}</div>
										</td>
										</#if>
									</tr>
									</#if>
									<#if reportFocus.acceptanceResult>
									<tr>
										<td colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>验收结果：</span>
											</label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.acceptanceResultName!}</div>
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
					module			: 'martyrsFacility',
					attachmentData	: {bizId: reportId, attachmentType:'MARTYRS_FACILITY', eventSeq: '1,2,3', isBindBizId: 'yes'},
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
					module			: 'martyrsFacility',
					attachmentData	: {bizId: reportId, attachmentType:'MARTYRS_FACILITY', eventSeq: '1,2,3', isBindBizId: 'yes'},
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
			
			$('#mfDetailForm .autoWidth').each(function() {
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
				mapType = 'MARTYRS_FACILITY',
				isEdit = false;
			
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
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				dynamicContentNextOrgNameLabel = '@nextOrgNames@',
				curNodeName = '${curNodeName!}',
				nextNodeName = $('#nodeName_').val(),
				startDivisionCode = data.dynamicContentMap.startDivisionCode || '';
			
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
				
			}else if(curNodeName=='task2'){
				_capDynamicContent();
				
				var winWidth = $(window).width();
				$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
					$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
						   .addClass("isSettledAutoWidth");
				});
			}else if(curNodeName=='task3'){
				$("#bigFileUploadDivMust").show();
				if($('#feedbackTypeTr').length == 0) {
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')=='end1'){
					    	$(this).parent().hide();
					    }
					});
				}else{
					var feedbackType = $("#_feedbackType").val();
					if(feedbackType!=null&&feedbackType!=''){
						if(nextNodeName=='end1'){
							var temp = "adviceNote_" + (parseInt(feedbackType)-1);
							$('#adviceNote').val(dynamicContentMap[temp]);
						}
					}
					_capDynamicContent();
				}
				
				if($('#feedbackTypeTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="feedbackTypeTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>反馈情况：</span></label>' + 
								'<input type="hidden" id="_feedbackType" name="_feedbackType" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_feedbackTypeName" name="_feedbackTypeName" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					AnoleApi.initListComboBox("_feedbackTypeName", "_feedbackType", "B210013003", function(dictValue, item) {
						
					}, "", {
						OnChanged: function(value) {
							if(value=='03'||value=='02'){
								$('#_occurred').validatebox({
									required: true
								});
								$("#occurredMust").show();
							}else if(value=='01'){
								$('#_occurred').validatebox({
									required: false
								});
								$("#occurredMust").hide();
							}
							
							if(value=='01'||value=='02'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task4'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='end1'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}else if(value=='03'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='end1'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task4'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}
						},
					});
					
					$('#_feedbackTypeName').validatebox({
						required: true
					});
					$('#feedbackTypeTr').show();
					$('#mfDetailForm').append('<input type="hidden" id="feedbackType" name="feedbackType" value="" />');
				}
				
				if($('#occurredTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="occurredTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik" id="occurredMust" style="display:none;">*</label>发生地址：</span></label>' +
								'<input id="_occurred" name="_occurred" class="inp1 easyui-validatebox autoWidth" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[1500]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());" value="${reportFocus.occurred!}"/>' + 
							'</td>' + 
							'<td class="hide">' + 
								'<label class="LabName labelNameWide"><span>地理标注：</span></label>'+
								'<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2"></b></span>'+
							'</td>' + 
						'</tr>'
					);
					$('#_occurred').validatebox({
						required: false
					});
					
					$('#occurredTr').show();
					$('#mfDetailForm').append('<input type="hidden" id="occurred" name="occurred" value="" />');
					$('#mfDetailForm').append('<input type="hidden" id="x_occurred" name="resMarker_occurred.x" value="" />');
					$('#mfDetailForm').append('<input type="hidden" id="y_occurred" name="resMarker_occurred.y" value="" />');
					$('#mfDetailForm').append('<input type="hidden" id="hs_occurred" name="hs_occurred" value="" />');
					$('#mfDetailForm').append('<input type="hidden" id="mapt_occurred" name="resMarker_occurred.mapType" value="" />');
						
					init4Location_occurred('_occurred',startDivisionCode);
					getMarkerData_occurred();
					<#if reportFocus.resMarker??>
						var resMarkerX = "${reportFocus.resMarker.x!}",
							resMarkerY = "${reportFocus.resMarker.y!}",
							resMarkerMapType = "${reportFocus.resMarker.mapType!}";
						
						if(resMarkerX && resMarkerY && resMarkerMapType) {
							callBackOfData_occurred(resMarkerX, resMarkerY, null, resMarkerMapType);
						}
					</#if>
				}
			}else if(curNodeName=='task4'){
				$("#bigFileUploadDivMust").show();
				if($('#doTypeTr').length == 0) {
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')=='task5'){
					    	$(this).parent().hide();
					    }
					});
				}else{
					_capDynamicContent();
				}
				
				if($('#doTypeTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="doTypeTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>处置结果：</span></label>' + 
								'<input type="hidden" id="_doType" name="_doType" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_doTypeName" name="_doTypeName" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					AnoleApi.initListComboBox("_doTypeName", "_doType", "B210013004", function(dictValue, item) {
						
					}, "", {
						OnChanged: function(value) {
							if(value=='01'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task5'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task6'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}else if(value=='02'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='task6'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='task5'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}
						},
					});
					
					$('#_doTypeName').validatebox({
						required: true
					});
					$('#doTypeTr').show();
					$('#mfDetailForm').append('<input type="hidden" id="doType" name="doType" value="" />');
				}
			}else if(curNodeName=='task5'){
				$("#bigFileUploadDivMust").show();
				_capDynamicContent();
			}else if(curNodeName=='task6'){
				if($('#acceptanceResultTr').length == 0) {
					$("input[name='nextNode']").each(function(){
					    if($(this).attr('nodename')!='end1'){
					    	$(this).parent().hide();
					    }
					});
				}else{
					_capDynamicContent();
				}
				
				if($('#acceptanceResultTr').length == 0) {
					$('#tr_epath').before(
						'<tr id="acceptanceResultTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>验收结果：</span></label>' + 
								'<input type="hidden" id="_acceptanceResult" name="_acceptanceResult" value="" />' + 
								'<input type="text" class="inp1 easyui-validatebox" style="width:160px;" data-options="required:true,tipPosition:\'bottom\'" id="_acceptanceResultName" name="_acceptanceResultName" value="" />' + 
							'</td>' + 
						'</tr>'
					);
					AnoleApi.initListComboBox("_acceptanceResultName", "_acceptanceResult", "B210013005", function(dictValue, item) {
						
					}, "", {
						OnChanged: function(value) {
							if(value=='01'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')!='end1'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')=='end1'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}else if(value=='02'){
								$("input[name='nextNode']").each(function(){
								    if($(this).attr('nodename')=='end1'){
								    	$(this).parent().hide();
								    }else if($(this).attr('nodename')!='end1'){
								    	$(this).parent().show();
								    	$(this).click();
								    }
								});
							}
						},
					});
					
					$('#_acceptanceResultName').validatebox({
						required: true
					});
					$('#acceptanceResultTr').show();
					$('#mfDetailForm').append('<input type="hidden" id="acceptanceResult" name="acceptanceResult" value="" />');
				}
			}
		}
		
		function init_ffpDetailForm(){
			var curNodeName = '${curNodeName!}';
			if(curNodeName=='task2'){
				
			}else if(curNodeName=='task3'){
				$("#feedbackType").val($("#_feedbackType").val());
				$("#occurred").val($("#_occurred").val());
			}else if(curNodeName=='task4'){
				$("#doType").val($("#_doType").val());
			}else if(curNodeName=='task5'){
				
			}else if(curNodeName=='task6'){
				$("#acceptanceResult").val($("#_acceptanceResult").val());
			}
		}
		
		function _subTaskOperate() {

			var isValid =  $("#flowSaveForm").form('validate');

			if($('#dataSource').val() == '02' && '${curNodeName!}' == 'task2') {
				if(!$("#markId").validatebox('isValid') || !$("#markName").validatebox('isValid')) {
					$.messager.alert('警告', '请选择纪念设施！', 'warning');
					return;
				}
			}

			if(isValid && $('#bigFileUploadDiv').is(':visible')) {
				var curNodeName = '${curNodeName!}';
				if(curNodeName=='task3'){
					var feedbackType = $("#_feedbackType").val();
					if(feedbackType=='02'){
						isValid = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'))
							&&checkAttachment4BigFileUpload(3, $('#bigFileUploadDiv div[file-status="complete"]'));
					}else{
						isValid = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'));
					}
				}else if(curNodeName=='task4'){
					var doType = $("#_doType").val();
					if(doType=='01'){
						isValid = checkAttachment4BigFileUpload(3, $('#bigFileUploadDiv div[file-status="complete"]'));
					}else{
						isValid = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'));
					}
				}else if(curNodeName=='task5'){
					isValid = checkAttachment4BigFileUpload(3, $('#bigFileUploadDiv div[file-status="complete"]'));
				}
			}
			if(isValid) {
				init_ffpDetailForm();
				isValid = $("#mfDetailForm").form('validate');
				
				if(isValid) {
					modleopen();
					
					$("#mfDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportMarFac/saveReportFocus.jhtml");
					
					$("#mfDetailForm").ajaxSubmit(function(data) {
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
					
				}else if(curNodeName=='task2'){
					
				}else if(curNodeName=='task3'){
					var feedbackType = $("#_feedbackType").val();
					if(feedbackType==null||feedbackType==''){
						advice = '';
					}else{
						if(feedbackType=='02'||feedbackType=='03'){
							advice = toDo_capDynamicContent('@occurred@',adviceNote,'_occurred',advice);
						}else if(feedbackType=='01'){
							if(adviceNote.indexOf('@occurred@') >= 0) {
								advice = advice.replaceAll('@occurred@', $('#_occurred').val());
							}
						}
					}
				}else if(curNodeName=='task4'){
					var doType = $("#_doType").val();
					if(doType==null||doType==''){
						advice = '';
					}
				}else if(curNodeName=='task5'){
					
				}else if(curNodeName=='task6'){
					var acceptanceResult = $("#_acceptanceResult").val();
					if(acceptanceResult==null||acceptanceResult==''){
						advice = '';
					}
				}
				if(advice.indexOf('@') >= 0) {
					advice = '';
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
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				isRequired = false;
			
			if((formTypeId == '358' && curNodeName == 'task3') || dataSource != '05') {
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
		
		
		function getMarkerData_occurred(){
			var markerOperation = $("#markerOperation").val(); // 地图操作类型
			var id = $("#id").val();
			var module = $("#module").val(); // 模块
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
				 		$("#x_occurred").val(data.x);
				 	}
				 	if (data.y != "" && data.y != null) {
				 		$("#y_occurred").val(data.y);
				 	}
			 		if (data.mapt != "" && data.mapt != null) {
				 		$("#mapt_occurred").val(data.mapt);
				 	}}
				 }
			});
			 
			if(markerOperation == 3) {
				$("#mapTab2_occurred").html("");//为了展示可视部分
				$("#mapTab2_occurred").parent().addClass("mapTab3")
				  .css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
				  .attr("title", "查看地理位置");
			} else {
				$("#mapTab2_occurred").html(showName);
			}
		}
	
		function callBackOfData_occurred(x,y,hs,mapt) {
			$('#x_occurred').val(x);
			$('#y_occurred').val(y);
			$("#mapt_occurred").val(mapt);
			$("#hs_occurred").val(hs);
			var showName = "修改地理位置";
			$("#mapTab2_occurred").html(showName);
			try {
				closeMaxJqueryWindowForMapMarker();
			} catch(e) {}
		}
		
		function init4Location_occurred(locationId, startDivisionCode,option) {
			var locationOption = {
				_source : 'XIEJING',//必传参数，数据来源
				_select_scope : 0,
				_show_level : 6,//显示到哪个层级
				_context_show_level : 0,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
				_startAddress :"${reportFocus.occurred!}",
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
						
						$("#_occurred").val(api.getAddress());
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
						
						$('#x_occurred').val(latitude);
						$('#y_occurred').val(longitude);
						$('#mapt_occurred').val(mapType);
						$("#mapTab2_occurred").html(showName);
					},
					OnCleared : function(api) {
						//清空按钮触发的事件
						$("#_occurred").val('');
						$('#x_occurred').val('');
						$('#y_occurred').val('');
						$('#mapt_occurred').val('');
						$("#mapTab2_occurred").html('标注地理位置');
						_capDynamicContent();
					}
				}
			};
			
			<#if reportFocus.resMarker??>
				$.extend(locationOption._addressMap, {
					_addressMapX	: '${reportFocus.resMarker.x!}',
					_addressMapY	: '${reportFocus.resMarker.y!}',
					_addressMapType	: '${reportFocus.resMarker.mapType!}' 
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
		
		function reSizeResmarker(){
			setTimeout(initReSizeResmarker,600);
		}
		
		function initReSizeResmarker(){
			$("#MaxJqueryWindowForCross").parent().css("top","0px");
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

			$('#markName').val(data.plaName);
			$('#markId').val(data.plaId);
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
		}

	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/martyrsFacility/detail_base.ftl" />
</html>