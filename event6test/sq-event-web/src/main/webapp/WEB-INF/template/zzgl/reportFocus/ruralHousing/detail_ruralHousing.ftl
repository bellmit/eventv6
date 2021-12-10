<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>农村建房详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
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
							<li><a href="##" divId="mainDiv" class="active">农村建房信息</a></li>
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
						<input type="hidden" id="module" value="RURAL_HOUSING"/>

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
									<label class="LabName labelNameWide"><span><label class="Asterik">*</label>规划许可证编号：</span></label><div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.rcpCode!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span><label class="Asterik">*</label>建房户姓名：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.householder!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span><label class="Asterik">*</label>宅基地批准书编号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.rhaCode!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>证件类型：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.rhCardTypeName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide"><span>证件号码：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.rhIdCard!}</div>
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
						</table>
						
						<form id="rhDetailForm" name="rhDetailForm" action="" method="post" enctype="multipart/form-data">
							<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
							<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
							<input type="hidden" id="reportType" name="reportType" value="${reportType!}" />
							<input type="hidden" id="storey" name="storey" value="${reportFocus.storey!}" />
							
							<div <#if isAble2Handle?? && isAble2Handle && curNodeName?? && curNodeName == 'task3'>class="NorForm"</#if> style="margin-bottom: 5px;">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<#if isAble2Handle?? && isAble2Handle && curNodeName?? && curNodeName == 'task3'>
								<tr style="border-top: 1px dotted #cecece">
									<td class="LeftTd">
										<label class="LabName"><span>东权利人：</span></label>
										<input id="eastNeighbor" name="eastNeighbor" type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.eastNeighbor!}" onblur="_capDynamicNeighbor($(this).val());" />
									</td>
									<td class="LeftTd">
										<label class="LabName"><span>西权利人：</span></label>
										<input id="westNeighbor" name="westNeighbor" type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.westNeighbor!}" onblur="_capDynamicNeighbor($(this).val());" />
									</td>
								</tr>
								<tr>
									<td class="LeftTd">
										<label class="LabName"><span>南权利人：</span></label>
										<input id="southNeighbor" name="southNeighbor" type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.southNeighbor!}" onblur="_capDynamicNeighbor($(this).val());" />
									</td>
									<td class="LeftTd">
										<label class="LabName"><span>北权利人：</span></label>
										<input id="northNeighbor" name="northNeighbor" type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.northNeighbor!}" onblur="_capDynamicNeighbor($(this).val());" />
									</td>
								</tr>
								<#else>
								<tr>
									<td class="LeftTd">
										<label class="LabName"><span>东权利人：</span></label>
										<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.eastNeighbor!}</div>
									</td>
									<td class="LeftTd">
										<label class="LabName"><span>西权利人：</span></label>
										<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.westNeighbor!}</div>
									</td>
								</tr>
								<tr>
									<td class="LeftTd">
										<label class="LabName"><span>南权利人：</span></label>
										<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.southNeighbor!}</div>
									</td>
									<td class="LeftTd">
										<label class="LabName"><span>北权利人：</span></label>
										<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.northNeighbor!}</div>
									</td>
								</tr>
								</#if>
							</table>
							</div>
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="LeftTd" colspan="2">
										<label class="LabName"><span><label class="Asterik">*</label>图片上传：</span></label><div id="bigFileUploadDiv"></div>
									</td>
								</tr>
								<tr>
									<td class="LeftTd" colspan="2">
										<label class="LabName"><span>附件上传：</span></label><div id="attachFileUploadDiv"></div>
									</td>
								</tr>
							</table>
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
			baseWorkRejectCallback = null;//存放原有的驳回方法
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				actionableAttachmentType = "${actionableAttachmentType!}",
				labelDict = [{'name':'签订告知书', 'value':'1', 'isShow': false}, {'name':'建筑放样', 'value':'2', 'isShow': false}, {'name':'基槽验线', 'value':'3', 'isShow': false}, {'name':'施工节点', 'value':'4', 'isShow': false}, {'name':'竣工验收', 'value':'5', 'isShow': false}, {'name':'办结归档', 'value':'6', 'isShow': false}],
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'ruralHousing',
					attachmentData	: {bizId: reportId, attachmentType:'RURAL_HOUSING', eventSeq: '1,2,3,4,5,6', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: labelDict
				};
			var attachFileUploadOpt = {
				useType			: 'view',
				fileExt			: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				module			: 'ruralHousing',
				attachmentData	: {bizId: reportId, attachmentType:'RURAL_HOUSING', eventSeq: '1', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				},
				//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
				labelDict		: [{'name':'附件', 'value':'1'}]
			};
			
			if(actionableAttachmentType) {
				var labelIndex = parseInt(actionableAttachmentType, 10),
					len = labelDict.length;
				
				if(len >= labelIndex) {
					if(len > labelIndex) {
						labelDict.splice(labelIndex);
					}
					
					labelDict[labelIndex - 1].checked = true;
					labelDict[labelIndex - 1].isShow = true;
					bigFileUploadOpt.labelDict = labelDict;
				}
			}
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
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportRuralHousing/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportRuralHousing/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
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
				
				<#if actionableAttachmentType?? && (actionableAttachmentType > 0)>
					bigFileUploadOpt["useType"] = 'edit';
					attachFileUploadOpt["useType"] = 'edit';
				</#if>
			</#if>
			
			<#if feedbackCount?? && (feedbackCount > 0)>
				fetchRHFeedback();
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
			
			$('#topTitleUl  > li > a').eq(0).click();
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function startWorkflow() {//启动流程
			var reportId = $("#reportId").val();
			
			if(reportId) {
				$("#rhDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportRuralHousing/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#rhDetailForm").ajaxSubmit(function(data) {
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
				isShowNumInput = dynamicContentMap.isShowNumInput || false,
				isNumInputEditable = isShowNumInput,
				dynamicContentNextOrgNameLabel = '@nextOrgNames@',
				numInputOption = null;
			
			if(isShowNumInput) {
				numInputOption = dynamicContentMap.numInputOption;
				
				if(numInputOption) {
					numInputOption = eval('(' + numInputOption + ')');
					isNumInputEditable = numInputOption.isEditable;
				} else {
					numInputOption = {};
				}
			}
				
			$('#advice').val('');
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}
			
			if($('#dynamicAddress').length == 1) {
				$('#dynamicAddress').val('');
			}
			
			if(!isShowText && !isShowDict && !isNumInputEditable && adviceNote.indexOf(dynamicContentNextOrgNameLabel) < 0) {
				$('#advice').val(adviceNote);
			}
			
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
						$('#adviceNote_' + noteIndex).val(adviceNoteSeparate);
					}
				} else {
					break;
				}
			}
			
			$('#adviceNote').val(adviceNote);
			
			if($('#eastNeighbor').length > 0) {
				_capDynamicNeighbor();
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
							min : -2,
							max : 99,
							required: true,
							events : {
								blur: function() {
									_capDynamicContent($('#dynamicContentNumber').numberbox('getValue'), null, 5);
								}
							}
						});
					} else {
						var storeyId = numInputOption.attrId || 'storey';
						
						$('#remarkTr').before(
							'<tr id="dynamicContentNumInputTr">' + 
								'<td>' + 
									'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.numInputLabelName + '：</span></label>' +
									'<div class="Check_Radio FontDarkBlue">' + $('#' + storeyId).val() + '</div>' + 
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
			
			if(isShowText) {
				var formTypeId = '${formTypeId!}',
					curNodeName = '${curNodeName!}',
					nextNodeName = $('#nodeName_').val();
				
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
			
			if($('#dynamicContentNumber').length == 1 && $('#dynamicContentNumInputTr').is(':visible')) {
				$('#dynamicContentNumber').numberbox({
					required: true
				});
			}

			var isValid =  $("#flowSaveForm").form('validate');
			
			if(isValid) {
				var actionableAttachmentType = "${actionableAttachmentType!}",
					labelDict = $('#bigFileUploadDiv').getInstanceX().labelDict;
				
				if(actionableAttachmentType) {
					var labelIndex = parseInt(actionableAttachmentType, 10),
						len = labelDict.length;
					
					if(len >= labelIndex) {
						var typeNameObj = {},
							option = {},
							typeIndex = labelIndex == len ? labelIndex : labelIndex + 1;
						
						if(labelDict) {
							for(var index in labelDict) {
								typeNameObj[labelDict[index].value] = labelDict[index].name;
							}
							
							option.typeNameObj = typeNameObj;
						}
						
						isValid = checkAttachment4BigFileUpload(typeIndex, $('#bigFileUploadDiv div[file-status="complete"]'), null, option);
					}
				}
			}
			
			if(isValid) {
				<#if isAble2Handle?? && isAble2Handle && curNodeName?? && (curNodeName == 'task3' || curNodeName == 'task7')>
				isValid = $("#rhDetailForm").form('validate');
				
				if(isValid) {
					modleopen();
					
					if($('#dynamicContentNumber').length > 0) {
						$('#storey').val($('#dynamicContentNumber').numberbox('getValue'));
					}
					
					$("#rhDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportRuralHousing/saveReportFocus.jhtml");
					
					$("#rhDetailForm").ajaxSubmit(function(data) {
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
				<#else>
				if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
					basWorkSubTaskCallback();
				}
				</#if>
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
		
		function _capDynamicNeighbor(dynamicContent) {
			var eastNeighbor = $('#eastNeighbor').val(),
				westNeighbor = $('#westNeighbor').val(),
				southNeighbor = $('#southNeighbor').val(),
				northNeighbor = $('#northNeighbor').val();
			
			if(eastNeighbor || westNeighbor || southNeighbor || northNeighbor) {
				$('#adviceNote').val($('#adviceNote_0').val());
			} else {
				$('#adviceNote').val($('#adviceNote_1').val());
			}
			
			_capDynamicContent(dynamicContent);
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
					dynamicContentNumLabel = '@dynamicNumber@',
					eastNeighborLabel = '@eastNeighbor@',
					westNeighborLabel = '@westNeighbor@',
					southNeighborLabel = '@southNeighbor@',
					northNeighborLabel = '@northNeighbor@';
				
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
				
				if(adviceNote.indexOf(eastNeighborLabel) >= 0) {
					var eastNeighbor = $('#eastNeighbor').val();
					
					advice = advice.replaceAll(eastNeighborLabel, eastNeighbor);
				}
				
				if(adviceNote.indexOf(westNeighborLabel) >= 0) {
					var westNeighbor = $('#westNeighbor').val();
					
					advice = advice.replaceAll(westNeighborLabel, westNeighbor);
				}
				
				if(adviceNote.indexOf(southNeighborLabel) >= 0) {
					var southNeighbor = $('#southNeighbor').val();
					
					advice = advice.replaceAll(southNeighborLabel, southNeighbor);
				}
				
				if(adviceNote.indexOf(northNeighborLabel) >= 0) {
					var northNeighbor = $('#northNeighbor').val();
					
					advice = advice.replaceAll(northNeighborLabel, northNeighbor);
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
					href: "${rc.getContextPath()}/zhsq/reportRuralHousing/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchRHFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
	</script>
	
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>