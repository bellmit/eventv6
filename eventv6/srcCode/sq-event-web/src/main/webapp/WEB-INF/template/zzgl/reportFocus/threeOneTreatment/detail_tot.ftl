<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>三合一整治详情</title>
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

		<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/jquery.ffcs.pc.select.js"></script>
		<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/custom_msgClient.js "></script>
		
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
			div.HandleAdvice{
				width: 458px;
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
							<li><a href="##" divId="mainDiv" class="active">整治信息</a></li>
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
						<input type="hidden" id="module" value="THREE_ONE_TREATMENT"/>
						<#--是否上传处理后图片-->
						<input type="hidden" id="isUploadHandledPic" value="${isUploadHandledPic!'false'}"/>
						<input type="hidden" id="isUploadHandlingPic" value="${isUploadHandlingPic!'false'}"/>
						<input type="hidden" id="picTypeName" value=""/>
						<input type="hidden" id="isUploadNewFile" value=""/>
                        <#--发生地址-->
						<input type="hidden" id="occurred" value="${reportFocus.occurred!}"/>

						<ul>
							<li>
								<p>
									<p style="margin-top: -4px;" id="resmarkerDiv"></p>&nbsp;<p id="occurredDiv" style="font-size: 16px;<#if !(reportFocus.occurred??)>color: #C0C0C0;</#if>">${reportFocus.occurred!'（暂无发生地址信息！）'}</p>
								</p>
							</li>
						</ul>
						
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
									<label class="LabName">
										<span>发现渠道：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.dataSourceName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide">
										<span><label class="Asterik">*</label>报告方式：</span>
									</label>
									<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.reportWayName!'办公操作平台'}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName"><span>报告编号：</span></label><div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide">
										<span><label class="Asterik">*</label>所属区域：</span>
									</label>
									<div class="Check_Radio FontDarkBlue singleCellClass">${reportFocus.regionPath!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName">
										<span>报告人姓名：</span>
									</label><div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide">
										<span>报告人电话：</span>
									</label><div class="Check_Radio FontDarkBlue">${reportFocus.reporterTel!}</div>
								</td>
							</tr>
							<tr>
								<td style="width: 40%;">
									<label class="LabName">
										<span>报告方式：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!}</div>
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
									<label class="LabName">
										<span>业主姓名：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.personInvolved!}</div>
								</td>
								<td>
									<label class="LabName labelNameWide">
										<span>场所面积：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.siteArea!}</div>
								</td>
							</tr>
							<tr>
								<td>
									<label class="LabName">
										<span>隐患类型：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${reportFocus.hiddenDangerTypeName!}</div>
								</td>
								<td <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>class="hide"</#if>>
									<label class="LabName labelNameWide">
										<span>处置时限：</span>
									</label>
									<div class="Check_Radio FontDarkBlue">${DUEDATESTR_!}</div>
								</td>
							</tr>
                            <#if reportFocus.disposalResultStr??>
                                <tr>
                                    <td>
                                        <label class="LabName">
                                            <span>处置结果：</span>
                                        </label>
                                        <div class="Check_Radio FontDarkBlue">${reportFocus.disposalResultStr!}（一级网格处置结果）</div>
                                    </td>
                                    <#if reportFocus.extensionDateStr??>
                                        <td>
                                            <label class="LabName labelNameWide">
                                                <span>延期日期：</span>
                                            </label>
                                            <div class="Check_Radio FontDarkBlue">${reportFocus.extensionDateStr!}</div>
                                        </td>
                                    </#if>
                                </tr>
                            </#if>
							<#if reportFocus.discoveryStaff??>
								<tr>
									<td colspan="2">
										<label class="LabName">
											<span>发现部门：</span>
										</label>
										<div class="Check_Radio FontDarkBlue">${reportFocus.discoveryStaff!}</div>
									</td>
								</tr>
							</#if>
							<#if reportFocus.dataSource?? && reportFocus.dataSource == '02'>
								<tr>
									<td colspan="2">
										<label class="LabName">
											<span>反馈时限：</span>
										</label>
										<div class="Check_Radio FontDarkBlue">${reportFocus.feedbackTimeStr!}</div>
									</td>
								</tr>
							</#if>
							<#if reportFocus.tipoffContent??>
								<tr>
									<td colspan="2">
										<label class="LabName">
											<span><label class="Asterik">*</label>举报内容：</span>
										</label>
										<div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.tipoffContent!}</div>
									</td>
								</tr>
							</#if>
							<#if reportFocus.hdtDescribe?? && reportFocus.hiddenDangerType == '99'>
								<tr>
									<td colspan="2">
										<label class="LabName">
											<span><label class="Asterik">*</label>隐患描述：</span>
										</label>
										<div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.hdtDescribe!}</div>
									</td>
								</tr>
							</#if>
							<tr>
								<td colspan="2">
									<label class="LabName">
										<span>备注：</span>
									</label>
									<div class="Check_Radio FontDarkBlue doubleCellClass">${reportFocus.remark!}</div>
								</td>
							</tr>
						</table>
						
						<form id="ehdDetailForm" name="ehdDetailForm" action="" method="post" enctype="multipart/form-data">
							<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
							<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
							<input type="hidden" id="reportType" name="reportType" value="${reportType!}" />
							<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}" />
							
							<div style="padding-top: 10px;">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="LeftTd" colspan="2">
										<label class="LabName">
											<span><#if reportFocus.dataSource?? && reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>图片上传：</span>
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
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'threeOneTreatment',
					attachmentData	: {bizId: reportId, attachmentType:'THREE_ONE_TREATMENT', eventSeq: '1,2,3', isBindBizId: 'yes'},
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
			var bigViodeUploadOpt = {
					useType			: 'view',
					fileExt			: '.mp4,.avi,.amr',
					module			: 'threeOneTreatment',
					attachmentData	: {bizId: reportId, attachmentType:'THREE_ONE_TREATMENT', eventSeq: '1', isBindBizId: 'yes'},
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
				module			: 'threeOneTreatment',
				attachmentData	: {bizId: reportId, attachmentType:'THREE_ONE_TREATMENT', eventSeq: '1', isBindBizId: 'yes'},
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
					callback4Confirm : _capDynamicContent,
					userSelectedLimit : 1
				});
				
				BaseWorkflowNodeHandle.initParam({
					subTask: {
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportTOT/subWorkflow4ReportFocus.jhtml?reportType=' + reportType + '&dataSource=' + $('#dataSource').val(),
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportTOT/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
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
				fetchTOTFeedback();
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
				$("#ehdDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportTOT/startWorkflow4ReportFocus.jhtml");
				
				modleopen();
				
				$("#ehdDetailForm").ajaxSubmit(function(data) {
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
				reportFocus = data.reportFocus || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowText2 = dynamicContentMap.isShowText2 || false,
				isShowInput = dynamicContentMap.isShowInput || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				isShowAddress = dynamicContentMap.isShowAddress || false,
				isShowRegion = dynamicContentMap.isShowRegion || false,
				isShowRectifyDate = dynamicContentMap.isShowRectifyDate || false,
				isRevertOccuredNote = dynamicContentMap.isRevertOccuredNote || false,
				dynamicContentNextOrgNameLabel = '@nextOrgNames@',
				formTypeId = '${formTypeId!}',
				curNodeName = '${curNodeName!}',
				occurred	= '${reportFocus.occurred!}',
				nextNodeName = $('#nodeName_').val();


			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}
			
			if($('#dynamicAddress').length == 1) {
				if(isNotBlankStringTrim(occurred)){
					$('#dynamicAddress').val(occurred);
				}else{
					$('#dynamicAddress').val('');
				}
			}
			if($('#extensionDateTr').length == 1){
				$('#extensionDate').validatebox({required:false});
				$('#extensionDateTr').hide();
			}

			var isSelectOrg = data.selectUser.isSelectOrg || false;

			if(isSelectOrg){
				_handlerConstructor2(data,isSelectOrg);
			}else{
				_handlerConstructor(data);
			}
			
			if($('#adviceNote').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
			}

			if(isRevertOccuredNote){
                if(adviceNote.indexOf($('#occurred').val()) >= 0){
                    adviceNote = adviceNote.replaceAll($('#occurred').val(),'@occurred@');
                }
            }
			$('#adviceNote').val(adviceNote);

			//当前环节为镇级处置 下一环节为 组织验收 保存镇级办理时限
			if(curNodeName == 'task5' && nextNodeName == 'task7'){
				//记录一级网格办理时限
				if($('#streetDuedate').length == 0){
					$('#remarkTr').before('<input type="hidden" id="streetDuedate" name="streetDuedate" value="${DUEDATESTR_!}" />');
				}else{
					$('#streetDuedate').val("${DUEDATESTR_!}");
				}
			}

			if(isShowInput){
			    var inputName = dynamicContentMap.inputName;
				if($('#dynamicInputTr').length == 0){
					$('#remarkTr').before(
							'<tr id="dynamicInputTr">' +
                                '<td>' +
                                    '<label class="LabName"><span><label class="Asterik">*</label>' + inputName + '：</span></label>' +
                                    '<input type="hidden" id="dynamicInputVal"/>' +
                                    '<input type="text" style="width: 210px; height: 28px;" id="dynamicInput" name="dynamicInput" class="inp1 easyui-numberbox singleCellInpClass" data-options="tipPosition:\'bottom\',max:999999"/>' +
                                    '<label class="LabName" style="float: none; display: inline-block; margin-left: 0;text-align: inherit;padding-top: 3px;">（人）</label>' +
                                '</td>' +
							'</tr>'
					);
				}
                $('#dynamicInput').numberbox({
                    required:true,
                    min:0,
                    max:99999,
                    onChange:function () {
                    	var dynamicInputVal = $(this).val();

						$('#dynamicInputVal').val(dynamicInputVal);
                        _capDynamicContent();
                    }
                });
                $('#dynamicInputTr').show();
                $('#dynamicInput').numberbox({required:true});
			}else if($('#dynamicInputTr').length == 1){
                $('#dynamicInputTr').hide();
                $('#dynamicInput').numberbox({required:false});
            }
			
			if(isShowDict) {
				if($('#dynamicContentDictTr').length == 0) {
					var dynamicDictJson = dynamicContentMap.dynamicDictJson || '',
						dictLabelName = dynamicContentMap.dictLabelName || '',
						dictPcode = dynamicContentMap.dictPcode || '';

					$('#remarkTr').before(
						'<tr id="dynamicContentDictTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.dictLabelName + '：</span></label>' + 
								'<input type="hidden" id="dynamicDictName" value="" />' +
								'<input type="hidden" id="dynamicDictId" name="dynamicDictId" value="" />' +
								'<input type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:\'bottom\'" id="dynamicDict" value="" />' +
							'</td>' + 
						'</tr>'
					);
					
					if(dictPcode) {
						AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", dictPcode, function(dictValue, item) {
							var dictName = '';

							if(item && item.length > 0) {
								dictName = item[0].name;
								$('#dynamicDictName').val(dictName);
							}
							if(dictLabelName == '隐患类型'){
								if(dictValue == '99'){
									if($('#hdtDescribeTr').length == 0){
										$('#remarkTr').before(
												'<tr id="hdtDescribeTr">' +
												'<td>' +
												'<label class="LabName"><span><label class="Asterik">*</label>隐患描述：</span></label>' +
												'<textarea rows="3" style="height:50px;" id="hdtDescribe" name="hdtDescribe" class="area1 easyui-validatebox autoDoubleCell DealMan" data-options="tipPosition:\'bottom\',validType:[\'maxLength[300]\',\'characterCheck\']" onblur="_capDynamicContent(\'\',\''+ dictValue +'\');"></textarea>' +
												'</td>' +
												'</tr>'
										);
									}else{
										$('#hdtDescribeTr').show();
									}
									$('#hdtDescribe').val(reportFocus.hdtDescribe);
									$('#hdtDescribe').validatebox({required:true});
									_itemSizeAdjust92();
								}else{
									$('#hdtDescribeTr').hide();
									$('#hdtDescribe').val('');
									$('#hdtDescribe').validatebox({required:false});
								}
							}
							_capDynamicContent(dictName, dictValue);
						});
					} else if(dynamicDictJson) {
						AnoleApi.initListComboBox("dynamicDict", "dynamicDictId", null, function(dictValue, item) {
							$('#adviceNote').val(dynamicContentMap["adviceNote_" + dictValue]);
							//延期日期
							if(formTypeId == '364'){
								if(curNodeName == 'task4' && nextNodeName == 'task5' && dictLabelName == '处置结果'){
									//记录一级网格办理时限
									if($('#communityDuedate').length == 0){
										$('#remarkTr').before('<input type="hidden" id="communityDuedate" name="communityDuedate" value="${DUEDATESTR_!}" />');
									}
									if(dictValue == '1'){
										if($('#extensionDateTr').length == 0){
											$('#dynamicContentDictTr').before(
													'<tr id="extensionDateTr">' +
													'<td>' +
													'<label class="LabName"><span><label class="Asterik">*</label>延期日期：</span></label>' +
													'<input type="text" id="extensionDate" name="extensionDate" class="inp1 Wdate easyui-validatebox singleCellClass"' +
													'data-options="tipPosition:\'bottom\'" style="width:170px; cursor:pointer;"' +
													'onclick="WdatePicker({readOnly:true,minDate:\'%y-%M-%d\', dateFmt:\'yyyy-MM-dd\', isShowClear:true, isShowToday:true,onpicked:_capDynamicContent})"' +
													'value="${reportFocus.extensionDateStr!}" readonly="readonly"></input>' +
													'</td>' +
													'</tr>'
											);
										}
										$('#extensionDate').validatebox({required:true});
										$('#extensionDateTr').show();
									}else{
										$('#extensionDate').validatebox({required:false});
										$('#extensionDateTr').hide();
									}
									//已完成整治
									if(dictValue == '0'){
										$('#dynamicContentAreaTr').hide();
										$('#dynamicContent').validatebox({
											required: false
										});

										$('#dynamicContentAreaTr2').hide();
										$('#dynamicContent2').validatebox({
											required: false
										});
									}else{
										$('#dynamicContentAreaTr').show();
										$('#dynamicContent').validatebox({
											required: true
										});

										$('#dynamicContentAreaTr2').show();
										$('#dynamicContent2').validatebox({
											required: true
										});
									}
								}else if(curNodeName == 'task5' && nextNodeName == 'task4'
										&& dynamicContentMap.disposalResult == '1' && dictLabelName == '处置结果'){
									if(dictValue == '0'){
										$('#dynamicContentAreaTr').hide();
										$('#dynamicContent').validatebox({
											required: false
										});
									}else{
										$('#dynamicContentAreaTr').show();
										$('#dynamicContent').validatebox({
											required: true
										});
									}
								}
							}else if(formTypeId == '36401'){
								if(curNodeName == 'task2' && nextNodeName == 'task0' && dictLabelName == '跟踪情况'){
									if(dictValue == '2'){
										if($('#extensionDateTr').length == 0){
											$('#dynamicContentDictTr').before(
													'<tr id="extensionDateTr">' +
													'<td>' +
													'<label class="LabName"><span><label class="Asterik">*</label>延期日期：</span></label>' +
													'<input type="text" id="extensionDate" name="extensionDate" class="inp1 Wdate easyui-validatebox singleCellClass"' +
													'data-options="tipPosition:\'bottom\'" style="width:170px; cursor:pointer;"' +
													'onclick="WdatePicker({readOnly:true,minDate:\'%y-%M-%d\', dateFmt:\'yyyy-MM-dd\', isShowClear:true, isShowToday:true,onpicked:_capDynamicContent})"' +
													'value="${reportFocus.extensionDateStr!}" readonly="readonly"></input>' +
													'</td>' +
													'</tr>'
											);
										}
										$('#extensionDate').validatebox({required:true});
										$('#extensionDateTr').show();
									}else{
										$('#extensionDate').validatebox({required:false});
										$('#extensionDateTr').hide();
									}

									//申请延期、尚未整治
									if(dictValue == '2' || dictValue == '3'){
										$('#dynamicContentAreaTr').show();
										$('#dynamicContent').validatebox({
											required: true
										});
									}else{
										$('#dynamicContentAreaTr').hide();
										$('#dynamicContent').validatebox({
											required: false
										});
									}
								}
							}

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
			
			if(isShowText) {
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

					//调整样式宽度
					_itemSizeAdjust92();

					//主流程 一级网格处置申请延期的 镇级处置时 动态文本先隐藏 根据动态字典决定是否展示
					if((formTypeId == '364' && curNodeName == 'task5' && nextNodeName == 'task4' && dynamicContentMap.disposalResult == '1'
							&& dynamicContentMap.dictLabelName == '处置结果')
						||(formTypeId == '36401' && curNodeName == 'task2' && nextNodeName == 'task0' && dynamicContentMap.dictLabelName == '跟踪情况')
					){
						$('#dynamicContentAreaTr').hide();
						$('#dynamicContent').validatebox({
							required: false
						});
					}else{
						$('#dynamicContentAreaTr').show();
						$('#dynamicContent').validatebox({
							required: true
						});
					}
				}
			} else if($('#dynamicContentAreaTr').length == 1) {
				$('#dynamicContentAreaTr').hide();
				$('#dynamicContent').validatebox({
					required: false
				});
			}

            if(isShowText2) {
            	var textLabelName2 = dynamicContentMap.textLabelName2;

            	//去除必填星号
				if(formTypeId == '364' && curNodeName == 'task6' && nextNodeName == 'end1' && textLabelName2 == '行政处罚'){
					textLabelName2 = textLabelName2;
				}else{
					textLabelName2 = '<label class="Asterik">*</label>' + textLabelName2;
				}

                $('#dynamicContentAreaTr2').remove();

                if($('#dynamicContentAreaTr2').length == 0) {
                    $('#remarkTr').before(
                        '<tr id="dynamicContentAreaTr2">' +
                        '<td>' +
                        '<label class="LabName"><span>' + textLabelName2 + '：</span></label>' +
                        '<textarea rows="3" style="height:50px;" id="dynamicContent2" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' +
                        '</td>' +
                        '</tr>'
                    );

                    //调整样式宽度
                    _itemSizeAdjust92();

                    if(formTypeId == '364' && curNodeName == 'task6' && nextNodeName == 'end1' && textLabelName2 == '行政处罚'){
						$('#dynamicContent2').validatebox({
							required: false
						});
					}else{
						$('#dynamicContent2').validatebox({
							required: true
						});
					}
                }
            } else if($('#dynamicContentAreaTr2').length == 1) {
                $('#dynamicContentAreaTr2').hide();
                $('#dynamicContent2').validatebox({
                    required: false
                });
            }

            if(isShowRectifyDate){
				if($('#rectifyDateTr').length == 0){
					$('#dynamicContentAreaTr').before(
							'<tr id="rectifyDateTr">' +
								'<td>' +
									'<label class="LabName"><span><label class="Asterik">*</label>整治日期：</span></label>' +
									'<input type="text" id="rectifyDate" name="rectifyDate" class="inp1 Wdate easyui-validatebox singleCellClass"' +
									'data-options="tipPosition:\'bottom\'" style="width:170px; cursor:pointer;"' +
									'onclick="WdatePicker({readOnly:true,minDate:\'%y-%M-%d\', dateFmt:\'yyyy-MM-dd\', isShowClear:true, isShowToday:true,onpicked:_capDynamicContent})"' +
									'value="" readonly="readonly"></input>' +
								'</td>' +
							'</tr>'
					);
				}
				$('#rectifyDate').validatebox({required:true});
				$('#rectifyDateTr').show();
			}else{
				$('#rectifyDate').validatebox({required:false});
				$('#rectifyDateTr').hide();
			}
			
			if(isShowRegion) {
				if($('#dynamicContentRegionTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentRegionTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.regionLabelName + '：</span></label>' +
								'<input type="text" id="dynamicContentRegionName" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\'"/>' + 
								'<input type="hidden" id="dynamicContentRegionCode" name="regionCode" />' + 
								'<input type="hidden" id="dynamicContentRegionPath" />' + 
							'</td>' + 
						'</tr>'
					);
					
					AnoleApi.initGridZtreeComboBox("dynamicContentRegionName", null,
			            function(gridId, items) {
			                if (items && items.length > 0) {
			                	$('#dynamicContentRegionPath').val(items[0].gridPath);
			                    $("#dynamicContentRegionCode").val(items[0].orgCode);
			                    _capDynamicContent();
			                    
			                    if(formTypeId =='364' && curNodeName == 'task6' && nextNodeName == 'task2') {
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
			                    AllowSelectLevel:"6"//选择到哪个层级
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
			
			if(isShowAddress) {
				if($('#dynamicContentAddressTr').length == 0) {
					var startDivisionCode = dynamicContentMap.startDivisionCode || '';
					
					$('#remarkTr').before(
						'<tr id="dynamicContentAddressTr">' + 
							'<td>' + 
								'<label class="LabName"><span><label class="Asterik">*</label>' + dynamicContentMap.addressLabelName + '：</span></label>' +
								'<input type="text" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1500]\',\'characterCheck\']" name="occurred" id="dynamicAddress" value="'+occurred+'" />' +
								'<input type="hidden" name="regionCode" id="dynamicRegionCode"/>' +
							'</td>' +
						'</tr>'
					);
					$('#flowSaveForm').append('<input type="hidden" name="latitude" id="latitude" value="'+reportFocus.resMarker.x+'"/>');
					$('#flowSaveForm').append('<input type="hidden" name="longitude" id="longitude" value="'+reportFocus.resMarker.y+'"/>');
					$('#flowSaveForm').append('<input type="hidden" name="maptype" id="maptype" value="'+reportFocus.resMarker.mapType+'"/>');
					
					$("#dynamicAddress").anoleAddressRender({
						_source : 'XIEJING',//必传参数，发现渠道
						_select_scope : 0,
						_show_level : 6,//显示到哪个层级
						_context_show_level : 1,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
						_startAddress :occurred,
						_startDivisionCode : startDivisionCode, //默认选中网格，非必传参数
						_customAddressIsNull : false,
						<#--<#if reportFocus.resMarker??>-->
						_addressMap : {//编辑页面可以传这个参数，非必传参数
							_addressMapShow : true,//是否显示地图标注功能
							_addressMapIsEdit : true,
							_addressMapX : reportFocus.resMarker.x,
							_addressMapY : reportFocus.resMarker.y,
							_addressMapType : reportFocus.resMarker.mapType
						},
						<#--<#else >
							_addressMap : {//编辑页面可以传这个参数，非必传参数
								_addressMapShow : false,//是否显示地图标注功能
								_addressMapIsEdit : true
							},
						</#if>-->
						BackEvents : {
							OnSelected : function(api) {
								$("#dynamicAddress").val(api.getAddress());
								$("#dynamicRegionCode").val(api.getInfoOrgCode());

								var isLocated = api.addressData._addressMap._addressMapIsEdit || false,
										latitude = '', longitude = '', mapType = '5',
										showName = "标注地理位置";

								$("#occurred").val(api.getAddress());

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

								$('#latitude').val(latitude);
								$('#longitude').val(longitude);
								$('#maptype').val(mapType);
								//$("#mapTab2").html(showName);

								_capDynamicContent();
							},
							OnCleared : function(api) {
								//清空按钮触发的事件
								$("#dynamicAddress").val('');
								$('#latitude').val('');
								$('#longitude').val('');
								$('#maptype').val('');
								//$("#mapTab2").html('标注地理位置');
							}
						}
					});
				}
				
				$('#dynamicAddress').validatebox({
					required: true
				});
				
				$('#dynamicContentAddressTr').show();

			} else if($('#dynamicContentAddressTr').length == 1) {
				$('#dynamicContentAddressTr').hide();
				$('#dynamicAddress').validatebox({
					required: false
				});
			}

			//判断是否上传处理中、后图片
			var isUploadHandlingPic = data.isUploadHandlingPic||false;
			var isUploadHandledPic = data.isUploadHandledPic||false;
			$('#isUploadHandlingPic').val(isUploadHandlingPic);
			$('#isUploadHandledPic').val(isUploadHandledPic);
			$('#picTypeName').val(data.picTypeName||'');
			
			_capDynamicContent();
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
				//判断是否上传处理后图片
				var isUploadHandledPic = $('#isUploadHandledPic').val()||false;
				var isUploadHandlingPic = $('#isUploadHandlingPic').val()||false;
				//处理后
				var typeName = $('#picTypeName').val();
				//个性化参数
				var optionObj = {
					isUseOption:true,
					msg:typeName
				};
				//校验附件文书是否上传成功校验结果，默认上传成功，当环节不需要强制上传附件时，可以正常提交工作流
				var isValid = true;
				//需要开启个性化参数，提示信息中附件名称根据传参确定
				if(isUploadHandlingPic == true || isUploadHandlingPic == 'true'){
					typeName = '处理中';
					optionObj.msg = typeName;
					isValid = verifyAttIsUpload(typeName,2,isUploadHandlingPic,optionObj);
					if(!isValid){return;}
				}
				if(isUploadHandledPic == true || isUploadHandledPic == 'true'){
					typeName = '处理后';
					optionObj.msg = typeName;
					isValid = verifyAttIsUpload(typeName,3,isUploadHandledPic,optionObj);
					if(!isValid){return;}
				}
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
				advice = '',
				curNodeName = $('#curNodeName').val(),
				nextNodeName = $('#nodeName_').val(),
				formTypeId = $('#formTypeId').val();
			
			if(adviceNote) {
				var dynamicContentLabel = '@dynamicContent@',
                    dynamicContentLabel2 = '@dynamicContent2@',
					dynamicContentNextOrgNameLabel = '@nextOrgNames@',
					dynamicCommunityRegionNameLabel = '@dynamicCommunityRegionName@',
					dynamicDictLabel = '@dynamicHiddenDangerType@',
					dynamicInputLabel = '@peopleNumber@',
					dynamicOccurredLabel = '@occurred@',
                    extensionDateLabel = '@extensionDate@',
					rectifyDateLabel = '@rectifyDate@',
					dynamicContentRegionPathLabel = '@nextRegionPath@';
				
				advice = adviceNote;
				
				if(advice.indexOf(dynamicContentLabel) >= 0) {
                    var dynamicContent = $('#dynamicContent').val();

                    if(dynamicContent){
                        advice = advice.replaceAll('@dynamicContent@', dynamicContent);
                    }
				}
                if(advice.indexOf(dynamicContentLabel2) >= 0) {
                    var dynamicContent2 = $('#dynamicContent2').val();

                    //主流程市级处置环节 行政处罚文本框为空时 赋默认值
                    if($('#formTypeId').val() == '364' && $('#curNodeName').val() == 'task6'
							&& $('#nodeName_').val() == 'end1' && isBlankStringTrim(dynamicContent2)){
						dynamicContent2 = '无';
						$('#dynamicContent2').val('无');
					}

                    if(dynamicContent2){
                        advice = advice.replaceAll('@dynamicContent2@', dynamicContent2);
                    }
                }
				
				if(advice.indexOf(dynamicContentNextOrgNameLabel) >= 0) {
					var nextOrgNames = $('#nextOrgNames').val();
					
					if(nextOrgNames) {
						advice = advice.replaceAll(dynamicContentNextOrgNameLabel, nextOrgNames);
					}
				}
				
				if(advice.indexOf(dynamicContentRegionPathLabel) >= 0) {
					var nextRegionPath = $('#dynamicContentRegionPath').val();
					
					if(nextRegionPath) {
						advice = advice.replaceAll(dynamicContentRegionPathLabel, nextRegionPath);
					}
				}
				if(advice.indexOf(dynamicCommunityRegionNameLabel) >= 0) {
					var nextRegionPath = $('#nextOrgNames').val();

					if(nextRegionPath) {
						advice = advice.replaceAll(dynamicCommunityRegionNameLabel, nextRegionPath);
					}
				}
                if(advice.indexOf(dynamicDictLabel) >= 0) {
                    var dynamicDictName = $('#dynamicDictName').val();

                    if(dynamicDictName) {

                        if(formTypeId == '364'){
                            if(curNodeName == 'task3' && nextNodeName == 'task4' && dictValue == '99'){
                                var hdtDescribe = $('#hdtDescribe').val();
                                if(hdtDescribe){
                                    dynamicDictName = dynamicDictName + '(' + hdtDescribe + ')';
                                }
                            }
                        }

                        advice = advice.replaceAll(dynamicDictLabel, dynamicDictName);
                    }
                }
                if(advice.indexOf(dynamicInputLabel) >= 0) {
                    var dynamicInputVal = $('#dynamicInputVal').val();

                    if(dynamicInputVal) {
                        advice = advice.replaceAll(dynamicInputLabel, dynamicInputVal);
                    }
                }
                if(advice.indexOf(dynamicOccurredLabel) >= 0) {
                    var dynamicOccurred = $('#occurred').val();

                    if(dynamicOccurred) {
                        advice = advice.replaceAll(dynamicOccurredLabel, dynamicOccurred);
                    }
                }
                if(advice.indexOf(extensionDateLabel) >= 0) {
                    var extensionDate = $('#extensionDate').val();

                    if(extensionDate) {
                        advice = advice.replaceAll(extensionDateLabel, extensionDate);
                    }
                }
				if(advice.indexOf(rectifyDateLabel) >= 0) {
					var rectifyDate = $('#rectifyDate').val();

					if(rectifyDate) {
						advice = advice.replaceAll(rectifyDateLabel, rectifyDate);
					}
				}
				
				if(advice.indexOf(dynamicContentLabel) >= 0 
					|| advice.indexOf(dynamicContentLabel2) >= 0
					|| advice.indexOf(dynamicContentNextOrgNameLabel) >= 0
					|| advice.indexOf(dynamicContentRegionPathLabel) >= 0
					|| advice.indexOf(dynamicDictLabel) >= 0
					|| advice.indexOf(dynamicInputLabel) >= 0
					|| advice.indexOf(dynamicOccurredLabel) >= 0
					|| advice.indexOf(extensionDateLabel) >= 0
					|| advice.indexOf(rectifyDateLabel) >= 0
					|| advice.indexOf(dynamicCommunityRegionNameLabel) >= 0) {
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
					href: "${rc.getContextPath()}/zhsq/reportTOT/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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

						//调整办理中意见宽度
						$("div[id^='subRemark_']").css({'width':'470px'});
						
						modleclose();
					}
				});
			}
		}
		function _itemSizeAdjust92(){
			var winWidth = $(window).width();
			$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
				$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
						.addClass("isSettledAutoWidth");
			});
			$('#mainDiv').getNiceScroll().resize();
		}

		/**
		 * 重写选送人员构造方式（支持通过组织选人）
		 * 分送、选送人员构造
		 * isClearNextUser 是否清除办理人员信息，true表示清除原有，并重新构造；默认为true
		 * isSelectOrg：选送人员是否通过组织选人
		 */
		function _handlerConstructor2(data,isSelectOrg) {
			isSelectOrg = isSelectOrg || false;

			//选送人员通过组织进行人员选择
			if(isSelectOrg){
				data = data || {};
				data = $.extend({
					'isClearNextUser': true
				}, data);

				if(data.isClearNextUser === false) {
					return;
				}

				$('#distributeUserDiv').hide();
				$('#htmlDistributeUserNames').html('');
				$('#distributeUserIds').val('');
				$('#distributeOrgIds').val('');
				$('#selectUserDiv').hide();
				$('#htmlSelectUserNames').html('');
				$('#selectOrgIds').val('');
				$('#selectUserIds').val('');

				if(data.distributeUser) {
					var distributeUserIds = data.distributeUser.userIds || '',
							distributeOrgIds = data.distributeUser.orgIds || '',
							isDisplayUser = data.distributeUser.isDisplayUser;

					$('#distributeUserIds').val(distributeUserIds);
					$('#distributeOrgIds').val(distributeOrgIds);

					if (isDisplayUser) {//只展示人员信息，不可修改
						var htmlUserNames = data.distributeUser.userNames || '';

						if(htmlUserNames) {
							var htmlUserContent = "",
									htmlUserArray = {},
									len = 0;

							htmlUserArray = htmlUserNames.split(',');
							len = htmlUserArray.length;

							if(len > 0) {
								htmlUserContent += '<div class="Check_Radio">';

								for(var index = 0; index < len; index++) {
									htmlUserContent += '<span class="SelectAll" style="margin-bottom: 3px;">' + htmlUserArray[index] + '</span>';
								}

								htmlUserContent += '</div>';
							}

							$('#htmlDistributeUserNames').html(htmlUserContent);
						}

						$('#distributeUserDiv').show();
					}
				}

				if(data.selectUser) {
					var selectUserIds = data.selectUser.userIds || '',
							selectOrgIds = data.selectUser.orgIds || '',
							isSelectUser = data.selectUser.isSelectUser,
							nananOrgId = data.selectUser.nananOrgId,
							isSelectOrg = data.selectUser.isSelectOrg;//是否通过选择组织进行人员的选择

					if (isSelectUser) {// 事件采集、指定到人、指定到组织
						var htmlUserNames = data.selectUser.userOrgNames || '';

						if(htmlUserNames) {
							var htmlUserContent = "",
									htmlUserArray = {},
									htmlIdArray = {},
									htmlUserOrgIdArray = {},
									len = 0,
									userLabelId = "";

							htmlUserArray = htmlUserNames.split(',');
							htmlIdArray = selectUserIds.split(',');
							htmlUserOrgIdArray = selectOrgIds.split(',');
							len = htmlUserArray.length;

							if(len > 0) {
								htmlUserContent += '<div class="Check_Radio">';
								htmlUserContent += '<p style="display:block; height:28px;">';
								htmlUserContent += '<span class="SelectAll">';
								htmlUserContent += "<input type='checkbox'  id='htmlSelectUserCheckAll' onclick='_checkAllSelectUser();' />";
								htmlUserContent += "<label style='cursor:pointer;' for='htmlSelectUserCheckAll'>全选</label>";
								htmlUserContent += "</span>";
								htmlUserContent += '</p>';

								for(var index = 0; index < len; index++) {
									userLabelId = htmlIdArray[index] + "_" + htmlUserOrgIdArray[index] + "_" + index;

									htmlUserContent += "<input type='checkbox' name='htmlSelectUserCheckbox' id='"+ userLabelId +"' userid='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] +"' onclick='_checkSelectUser();' />";
									htmlUserContent += "<label style='cursor:pointer;' for='"+ userLabelId +"'>"+htmlUserArray[index]+"</label>" + '&nbsp;&nbsp;';
								}

								htmlUserContent += '</div>';
							}

							$('#htmlSelectUserNames').html(htmlUserContent);
						}

						$('#selectUserDiv').show();
					}else if(isSelectOrg){
						if($('#selectUserBtnDiv').length == 0){
							$('#htmlSelectUserNames').parent().before(
									'<div class="Check_Radio" id="selectUserBtnDiv">' +
									'<a href="###" class="NorToolBtn EditBtn" id="selectUserBtn">选择人员</a>' +
									'</div>'
							);
							selectHandler2(nananOrgId);
						}
						$('#selectUserDiv').show();
					}
				}
			}else{
				this._handlerConstructor(data);
			}
		}
		function selectHandler2(nananOrgId){
			var orgId = nananOrgId || '406338';

			var wapSelect1 = $("#selectUserBtn").initSelect(orgId,function(res){
				if(res.length>0){
					var selectOrgIds = '';
					var selectUserIds = '';

					for(index in res){
						selectUserIds += "," + res[index].userId;
						selectOrgIds += "," + res[index].orgId;
					}
					if(selectUserIds.length > 0) {
						selectUserIds = selectUserIds.substr(1);
					}

					if(selectOrgIds.length > 0) {
						selectOrgIds = selectOrgIds.substr(1);
					}
					$('#selectUserIds').val(selectUserIds);
					$('#selectOrgIds').val(selectOrgIds);
				}
			},{
				layer:{
					area: ['700px', '380px'], //宽高
				},
				postParam:{
					//initValue: JSON.stringify(initJSONArr), //用户回填使用
					chooseType:['department','people'],  //'department','people', 'position','role'
					orgInitRole:[100220,101700],
					multi:true,                 //是否允许多选
					isRepeat:true,              //用户是否可以重复
					isUseReallyOrgType : true,	//是否只选择本级用户
					orgType:0					//组织类型： 1 单位 0 部门

					//clickLevelRange:[1,2]    //部门类型组织树可以点击的有效层级
					//isSubordinate:false,     //是否查询下级数据，只有部门类型查人员有效
					//orgInitPosition:[2080,2540]
					//topLevel:1
				},
				context:'${$COMPONENTS_DOMAIN}'
			});
		}
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchTOTFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
	</script>
	
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>