<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>两违事件详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${GIS_DOMAIN}/js/gis/base/mapMarker.js"></script>
		
		<style type="text/css">
			.singleCellClass{width: 68%;}
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
				margin-top:5px;
				*margin-top:9px;
				word-break:break-all;
			}
			.datagrid-mask-msg {
				box-sizing:content-box
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
							<li><a href="##" divId="mainDiv" class="active">两违详情</a></li>
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
					<div class="fw-det-tog fw-det-tog-n">
						<!--用于地图-->
						<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="markerOperation" name="markerOperation" value="3"/>
						<input type="hidden" id="module" value="TWO_VIO_PRE"/>
						<#--两违事件来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<#--是否上传处理后图片-->
						<input type="hidden" id="isUploadHandledPic" value="${isUploadHandledPic!'false'}"/>
						<input type="hidden" id="isUploadNewFile" value=""/>
						
						<ul>
							<li>
								<p>
									<p style="margin-top: -4px;" id="resmarkerDiv"></p>&nbsp;<p id="occurredDiv" style="font-size: 16px;">${reportFocus.occurred!}</p>
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
									<label class="LabName labelNameWide">
										<span>报告时间：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!}</div>
								</td>
							</tr>
							<tr>
								<td style="width: 40%;">
									<label class="LabName"><span>报告方式：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>业主姓名：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.personInvolved!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>占地面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.areaCovered!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>办理用地和规划手续：</span></label><div class="Check_Radio FontDarkBlue"><#if reportFocus.isRoutine?? && reportFocus.isRoutine=='1'>是<#else>否</#if></div>
								</td>
							</tr>
							<#if reportFocus.isRoutine?? && reportFocus.isRoutine == '1'>
							<tr>
								<td>
									<label class="LabName"><span><label class="Asterik">*</label>土地证号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.lecCode!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span><label class="Asterik">*</label>规划许可证号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.ppnCode!}</div>
								</td>
							</tr>
							</#if>
							<#if reportFocus.dataSource?? && reportFocus.dataSource == '03'>
							<tr>
								<td colspan="2">
									<label class="LabName"><span><label class="Asterik">*</label>图斑编号：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.mapNum!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>耕地面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.cultivableLandArea!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>基本农田面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.farmlandArea!'0'}（平方米）</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>林地面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.woodlandArea!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>允许建设区面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.constructableArea!'0'}（平方米）</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>公益林面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.publicForestArea!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>条件建设区面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.conditionalConstructionArea!'0'}（平方米）</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>城乡规划面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.urpArea!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>限制建设区面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.restrictedConstructionArea!'0'}（平方米）</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>村庄规划面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.villagePlanningArea!'0'}（平方米）</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>生态红线面积：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.ecologicalRedlineArea!'0'}（平方米）</div>
								</td>
							</tr>
							</#if>
							<tr>
								<#if reportFocus.reportStatus?? && reportFocus.reportStatus != '60'>
									<td>
										<label class="LabName">
											<span>处置时限：</span>
										</label><div class="Check_Radio FontDarkBlue">${DUEDATESTR_!}</div>
									</td>
								</#if>
								<td <#if reportFocus.reportStatus?? && reportFocus.reportStatus == '60'>colspan="2"</#if>>
									<label class="LabName <#if reportFocus.reportStatus?? && reportFocus.reportStatus != '60'>labelNameWide</#if>">
										<span>建（构）筑物用途：</span>
									</label><div class="Check_Radio FontDarkBlue">${reportFocus.buildingUsageName!}</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<label class="LabName"><span>建设状态：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.constructionStatusName!}</div>
								</td>
							</tr>

							<#if reportFocus.dataSource?? && reportFocus.dataSource == '05'>
							<tr>
								<td colspan="2">
									<label class="LabName"><span><label class="Asterik">*</label>举报内容：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
								</td>
							</tr>
							</#if>
							<tr>
								<td colspan="2">
									<label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
								</td>
							</tr>
						</table>

						<form id="twoVioPreDetailForm" name="twoVioPreDetailForm" action="" method="post" enctype="multipart/form-data">
							<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
							<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
							<input type="hidden" id="reportType" name="reportType" value="${reportType!}" />

							<div style="padding-top: 10px;overflow-y: auto">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
						</form>
					</div>
					
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
				bigFileUploadOpt = null;
			baseWorkRejectCallback = null;//存放原有的驳回方法
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
			    fileUploadObj = new Object();
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'twoVioPre',
					attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					//labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}],
					uploadSuccessCallback : function(file,response){
						uploadback(file,response);
					},
					deleteCallback:function(obj){
						deleteback(obj,fileUploadObj);
					}
				};
			var bigViodeUploadOpt = {
					useType			: 'view',
					fileExt			: '.mp4,.avi,.amr',
					module			: 'twoVioPre',
					attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}],
					uploadSuccessCallback : function(file,response){
						uploadback(file,response);
					},
					deleteCallback:function(obj){
						deleteback(obj);
					}
				};
			var attachFileUploadOpt = {
				useType			: 'view',
				fileExt			: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				module			: 'twoVioPre',
				attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				},
				//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
				labelDict		: [{'name':'处理前', 'value':'1'}],
				uploadSuccessCallback : function(file,response){
					uploadback(file,response);
				},
				deleteCallback:function(obj){
					deleteback(obj);
				}
			};
			
			<#if isAble2Handle?? && isAble2Handle>
				var baseWorkOption = BaseWorkflowNodeHandle.initParam(),//获取默认的设置
					reportType = $('#reportType').val(),
					selectHandler = baseWorkOption.selectHandler;
				
				basWorkSubTaskCallback = baseWorkOption.subTask.subTaskCallback;
				baseWorkRejectCallback = baseWorkOption.reject.rejectCallback;
				
				$.extend(selectHandler, {
					callback4Confirm : function() {
						_capDynamicContent($('#nextOrgNames').val());
					},
					userSelectedLimit : 1
				});
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportTwoVioPre/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportTwoVioPre/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
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
				
				bigFileUploadOpt["useType"] = 'edit'; 
				bigViodeUploadOpt["useType"] = 'edit';
				attachFileUploadOpt["useType"] = 'edit';
			</#if>
			
			<#if feedbackCount?? && (feedbackCount > 0)>
				fetchTwoVioPreFeedback();
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
			
			fileUploadObj = bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
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
			capcapMarkerData({
				data : {
					wgGisType	: 'twoVioShape',
					wgGisId		: $('#reportId').val()			
				}
			});
			
			//由于其他页签的内容高度会影响当前页签高度，因此手动触发首个页签点击事件，用于将其他页签的高度隐藏
			$('#topTitleUl  > li > a').eq(0).click();
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#twoVioPreDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportTwoVioPre/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#twoVioPreDetailForm").ajaxSubmit(function(data) {
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
			var adviceNote = data.adviceNote || '',
				dynamicContentMap = data.dynamicContentMap || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				isShowRegion = dynamicContentMap.isShowRegion || false,
				isShowNumInput = dynamicContentMap.isShowNumInput || false,
				isNumInputEditable = isShowNumInput,
				numInputOption = null,
				formTypeId = $('#formTypeId').val(),
				curNodeName = $('#curNodeName').val(),
				nextNodeName = $('#nodeName_').val();
			
			if(formTypeId == '3500' && curNodeName == 'task1') {
				$('#htmlUserNames input[type=radio][name=userRadio]').on('click', function() {
					_capDynamicContent();
				});
			}
			
			if(isShowNumInput) {
				numInputOption = dynamicContentMap.numInputOption;
				
				if(numInputOption) {
					numInputOption = eval('(' + numInputOption + ')');
					isNumInputEditable = numInputOption.isEditable;
					
					if(isBlankParam(isNumInputEditable)) {
						isNumInputEditable = true;
					}
				} else {
					numInputOption = {};
				}
			}
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}
			
			_handlerConstructor(data);
			
			if($('#adviceNote').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
			}
			
			$('#adviceNote').val(adviceNote);
			
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
							
							if($('#dynamicContentNumInputTr').length == 1 
							&& $('#dynamicContentNumber').length == 1
							&& formTypeId == '350' && curNodeName == 'task7' && nextNodeName == 'task11') {
								if(dictValue == '1') {
									$('#dynamicContentNumber').numberbox({
										required: true
									});
									$('#dynamicContentNumInputTr').show();
								} else {
									$('#dynamicContentNumInputTr').hide();
									$('#dynamicContentNumber').numberbox({
										required: false,
										value: ''
									});
								}
							}
							
							_capDynamicContent($('#dynamicContent').val());
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
				var textJson = dynamicContentMap.textJson || '';
				
				$('#dealProcessTable tr.dynamicContentAreaTr').remove();
				
				if(isNotBlankString(textJson)) {
					var textJsonArray = eval('(' + textJson + ')'),
						textObj = null;
					
					for(var index = 0, len = textJsonArray.length; index < len; index++) {
						textObj = textJsonArray[index];
						$('#remarkTr').before(
							'<tr class="dynamicContentAreaTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>' + textObj.name + '：</span></label>' +
									'<textarea rows="3" style="height:50px;" id="dynamicContent' + index + '" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
								'</td>' + 
							'</tr>'
						);
					}
				} else {
					$('#remarkTr').before(
						'<tr class="dynamicContentAreaTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="dynamicContent" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' + 
							'</td>' + 
						'</tr>'
					);
				}
				
				var winWidth = $(window).width();
				$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
					$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
						   .addClass("isSettledAutoWidth");
				});
				
				var textarea = $('#dealProcessTable tr.dynamicContentAreaTr textarea').eq(0);
				if(formTypeId == '350') {
					textarea.val('');
					textarea.removeAttr('name');
					
					if(curNodeName == 'task4' && nextNodeName == 'task5') {
						var belongRegionName = '${reportFocus.belongRegionName!}';
						
						textarea.val(belongRegionName);
						textarea.attr('name', 'belongRegionName');
					}
				}
				
				$.parser.parse($('#dealProcessTable'));
			} else if($('#dealProcessTable tr.dynamicContentAreaTr').length == 1) {
				$('#dealProcessTable tr.dynamicContentAreaTr').hide();
				$('#dealProcessTable tr.dynamicContentAreaTr textarea').each(function() {
					$(this).validatebox({
						required: false
					});
				});
			}
			
			if(isShowNumInput) {
				$('#dynamicContentNumInputTr').remove();
				
				if($('#dynamicContentNumInputTr').length == 0) {
					if(isNumInputEditable == true) {
						$('#remarkTr').before(
							'<tr id="dynamicContentNumInputTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.numInputLabelName + '：</span></label>' +
									'<input id="dynamicContentNumber" style="height: 28px;" class="inp1 autoDoubleCell" data-options="tipPosition:\'bottom\'"></input>' + 
								'</td>' + 
							'</tr>'
						);
						
						$('#dynamicContentNumber').numberbox({
							min : numInputOption.min || 1,
							max : numInputOption.max || 99,
							required: true,
							events : {
								blur: function() {
									_capDynamicContent($('#dynamicContentNumber').numberbox('getValue'));
								}
							}
						});
					} else {
						$('#remarkTr').before(
							'<tr id="dynamicContentNumInputTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.numInputLabelName + '：</span></label>' +
									'<div class="Check_Radio FontDarkBlue"></div>' + 
								'</td>' + 
							'</tr>'
						);
					}
				}
				
				$('#dynamicContentNumInputTr').show();
			} else if($('#dynamicContentNumInputTr').length == 1) {
				$('#dynamicContentNumInputTr').hide();
				
				if($('#dynamicContentNumber').length == 1) {
					$('#dynamicContentNumber').numberbox({
						required: false
					});
				}
			}
			
			if(isShowRegion) {
				if($('#dynamicContentRegionTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentRegionTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.regionLabelName + '：</span></label>' +
								'<input type="text" id="dynamicContentRegionName" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\'"/>' + 
								'<input type="hidden" id="dynamicContentRegionCode" name="regionCode" />' + 
							'</td>' + 
						'</tr>'
					);
					
					AnoleApi.initGridZtreeComboBox("dynamicContentRegionName", null,
			            function(gridId, items) {
			                if (items && items.length > 0) {
			                    $("#dynamicContentRegionCode").val(items[0].orgCode);
			                    _capDynamicContent();
			                    
			                    if(formTypeId == '350' && curNodeName == 'task5' && nextNodeName == 'task2') {
			                    	BaseWorkflowNodeHandle.checkRadio($('#_node_' + $('#nodeId').val()), {
			                    		isClearNextUser: false,
			                    		ajaxData: {
			                    			isAlterBenchmark: true,
			                    			benchmarkRegionCode: items[0].orgCode
			                    		}
			                    	});
			                    }
			                }
			            },
			            {
			                ChooseType:"1",
			                ShowOptions:{
			                    AllowSelectLevel:"5,6"//选择到哪个层级
			                }
			            }
			        );
				}
				
				$('#dynamicContentRegionName').validatebox({
					required: true
				});
				
				$('#dynamicContentRegionTr').show();
			}  else if($('#dynamicContentRegionTr').length == 1) {
				$('#dynamicContentRegionTr').hide();
				$('#dynamicContentRegionName').val('');
				$('#dynamicContentRegionName').validatebox({
					required: false
				});
			}
			
			_capDynamicContent();
			_itemSizeAdjust();
		}
		
		function _subTaskOperate() {
			if($('#dynamicContentDictTr').length == 1 && $('#dynamicContentDictTr').is(':visible')) {
				$('#dynamicDict').validatebox({
					required: true
				});
			}
			
			if($('#dealProcessTable tr.dynamicContentAreaTr:visible').length > 0) {
				$('#dealProcessTable tr.dynamicContentAreaTr textarea:visible').each(function() {
					$(this).validatebox({
						required: true
					});
				});
			}
			
			if($('#dynamicContentNumber').length == 1 && $('#dynamicContentNumInputTr').is(':visible')) {
				$('#dynamicContentNumber').numberbox({
					required: true
				});
			}
			
			//是否上传处理后图片
			var isUploadHandledPic = $('#isUploadHandledPic').val()||false;
			var isValid = true;

			if(isUploadHandledPic == true || isUploadHandledPic == 'true'){
				isValid = this.verifyAttIsUpload('处理后',3,isUploadHandledPic);
			}

			if(isValid){
				if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
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
			
			if($('#dealProcessTable tr.dynamicContentAreaTr').length > 0) {
				$('#dealProcessTable tr.dynamicContentAreaTr textarea').each(function() {
					$(this).validatebox({
						required: false
					});
				});
			}
			
			if($('#dynamicContentNumber').length == 1) {
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
		
		function _capDynamicContent(dynamicContent) {
			var adviceNote = $('#adviceNote').val(),
				advice = '';
			
			if(adviceNote) {
				var dynKey = null, dynId = null, dynVal = null, dynLen = 3;
				
				advice = adviceNote;
				dynamicContent = dynamicContent || $('#dynamicContent').val();
				
				if(advice.indexOf('@dynamicContent@') >= 0 && dynamicContent) {
					advice = advice.replaceAll('@dynamicContent@', dynamicContent);
				}

				if(advice.indexOf('@nextOrgNames@') >= 0) {
					var nextOrgNames = $('#nextOrgNames').val();

					if(nextOrgNames) {
						advice = advice.replaceAll('@nextOrgNames@', nextOrgNames);
					}
				}
				
				if(advice.indexOf('@nextUserName@') >= 0) {
					var nextUserNameObj = $('#htmlUserNames input[type=radio][name=userRadio]:checked'),
						nextUserName = null;
					
					if(nextUserNameObj.length > 0) {
						nextUserName = $('#htmlUserNames label[for=' + nextUserNameObj.eq(0).attr('id') + ']').eq(0).html();
					}
					
					if(nextUserName) {
						advice = advice.replaceAll('@nextUserName@', nextUserName);
					}
				}
				
				if(advice.indexOf('@nextRegionName@') >= 0) {
					var nextRegionName = $('#dynamicContentRegionName').val();
					
					if(nextRegionName) {
						advice = advice.replaceAll('@nextRegionName@', nextRegionName);
					}
				}
				
				if(advice.indexOf('@dynamicNumber@') >= 0) {
					var number = $('#dynamicContentNumber').numberbox('getValue');
					
					if(number) {
						advice = advice.replaceAll('@dynamicNumber@', number);
					}
				}
				
				for(var index = 0; index < dynLen; index++) {
					dynId = 'dynamicContent' + index;
					dynKey = '@' + dynId + '@';
					
					if($('#' + dynId).length > 0 && advice.indexOf(dynKey) >= 0) {
						dynVal = $('#' + dynId).val();
						
						if(dynVal) {
							advice = advice.replaceAll(dynKey, dynVal);
						}
					}
				}
				
				for(var index = 0; index < dynLen; index++) {
					dynKey = '@dynamicContent' + index + '@';
					
					if(advice.indexOf(dynKey) >= 0) {
						advice = ''; break;
					}
				}
				
				if(advice.indexOf('@dynamicContent@') >= 0
				|| advice.indexOf('@dynamicNumber@') >= 0
				|| advice.indexOf('@nextUserName@') >= 0
				|| advice.indexOf('@nextOrgNames@') >= 0
				|| advice.indexOf('@nextRegionName@') >= 0) {
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
					href: "${rc.getContextPath()}/zhsq/reportTwoVioPre/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
					onLoad:function(){//配合detail_workflow.ftl使用
						var workflowDetailWidth = $('#workflowDetail').width() - 10 - 10;//10px分别为左右侧距离
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
    
		<#if feedbackCount?? && (feedbackCount > 0)>
			function fetchTwoVioPreFeedback() {
				var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
				$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
				$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
				$("#feedbackListDiv").height('auto');
			}
		</#if>
	</script>
	
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>