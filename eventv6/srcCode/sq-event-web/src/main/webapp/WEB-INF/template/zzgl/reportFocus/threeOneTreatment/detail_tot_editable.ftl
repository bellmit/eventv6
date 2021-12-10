<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>三合一整治详情反馈</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />

		<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/jquery.ffcs.pc.select.js"></script>
		<script src="${uiDomain!''}/web-assets/extend/person-select/v1.0.0/js/custom_msgClient.js "></script>

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
			.squareMeterUnit{width: 65px;}
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
					<form id="totDetailForm" name="totDetailForm" action="" method="post">
						<input type="hidden" id="isStart" name="isStart" value="false" />
						<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
						<input type="hidden" name="isSaveResMarkerInfo" value="true" />
						<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="reportUUID" name="reportUUID" value="${reportFocus.reportUUID!}" />
						<input type="hidden" id="reportType" name="reportType" value="${reportFocus.reportType!}" />
						<!--用于地图-->
						<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
						<input type="hidden" id="name" name="name" value="" />
						<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
						<input type="hidden" id="module" value="THREE_ONE_TREATMENT"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<input type="hidden" id="collectSource" name="collectSource" value="${reportFocus.collectSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						<#--是否上传处理后图片-->
						<input type="hidden" id="isUploadHandledPic" value="${isUploadHandledPic!'false'}"/>
						<#--是否上传处理中图片-->
						<input type="hidden" id="isUploadHandlingdPic" value='false'/>
						<input type="hidden" id="picTypeName" value=""/>
						<input type="hidden" id="isUploadNewFile" value=""/>
						<!--是否需要定位信息-->
						<input type="hidden" id="isLocationRequired" value="false" />
						<input type="hidden" id="dictLabelName" value="" />
						
						<input type="hidden" id="verifyStatus" name="verifyStatus" value="0" />
						<input type="hidden" id="isForce2Save" value= "<#if isEditableNode??>${isEditableNode?c}<#else>false</#if>" />
						
						<div>
							<div id="eventWechatNorformDiv" class="NorForm">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>报告人姓名：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reporterName!}</div>
											<input type="hidden" name="reporterName" value="${reportFocus.reporterName!}" />
										</td>
										<td>
											<label class="LabName labelNameWide">
												<span>报告人电话：</span>
											</label><div class="Check_Radio FontDarkBlue">${reportFocus.reporterTel!}</div>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>报告编号：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportCode!}</div>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide">
												<span>报告时间：</span>
											</label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!''}</div>
											<#--<input type="text" id="reportTime" name="reportTime" class="inp1 Wdate easyui-validatebox singleCellClass"
												   style="width:170px; cursor:pointer;" data-options="required:true"
												   onclick="WdatePicker({readOnly:true, maxDate:'${(reportFocus.reportTimeStr!maxReportTimeStr)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${reportFocus.reportTimeStr!}" readonly="readonly"></input>-->
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName">
												<span>
													<label class="Asterik">*</label>发现渠道：
												</span>
											</label>
											<input  id="dataSourceName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;"
													data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
										</td>
										<td class="LeftTd" id="reportWayTd">
											<label class="LabName labelNameWide"><span>报告方式：</span></label>
											<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
											<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
										</td>
										<td class="LeftTd hide" id="discoveryStaffTd" <#if reportFocus.dataSource?? && reportFocus.dataSource!='03'>style="display: none"</#if>>
											<label class="LabName labelNameWide">
												<span><label class="Asterik">*</label>发现部门：</span>
											</label>
											<input  id="discoveryStaff" name="discoveryStaff" type="text" class="inp1 easyui-validatebox singleCellInpClass"
													style="width: 210px;" data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']"
													value="${reportFocus.discoveryStaff!}" />
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName">
												<span><label class="Asterik">*</label>所属区域：</span>
											</label>
											<input type="hidden" id="regionLevel" name="regionLevel" />
											<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
											<input type="text" id="gridName" style="width: 300px;" value="${reportFocus.regionName!}"
												   class="inp1 easyui-validatebox singleCellInpClass" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>隐患类型：</span></label>
											<input type="hidden" id="hiddenDangerType" name="hiddenDangerType" value="${reportFocus.hiddenDangerType!}" />
											<input  id="hiddenDangerTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 170px;"
													data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span>
													<label id="occurredAsterik" class="Asterik">*</label>发生地址：
												</span>
											</label>
											<input type="text" class="inp1 easyui-validatebox autoWidth"
												   data-options="tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
												   name="occurred" id="occurred" value="${reportFocus.occurred!}" />
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span><label class="Asterik locationAsterik hide">*</label>地理标注：</span></label>
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName">
												<span><label class="Asterik locationAsterik autoRequiredAsterik hide">*</label>业主姓名：</span>
											</label>
											<input  id="personInvolved" name="personInvolved" type="text" class="inp1 easyui-validatebox autoRequired singleCellInpClass" style="width: 300px;"
													data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']" value="${reportFocus.personInvolved!}" />
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide">
												<span><label id="siteAreaLabel" class="Asterik hide">*</label>场所面积：</span>
											</label>
											<input  id="siteArea" name="siteArea" type="text" class="inp1 easyui-numberbox singleCellInpClass" style="width: 210px; height: 28px;"
													data-options="tipPosition:'bottom',max:99999999.99,precision:2" value="${reportFocus.siteArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" <#if reportFocus.reportStatus?? && reportFocus.reportStatus=='60'>style="display: none" </#if>>
											<label class="LabName">
												<span>处置时限：</span>
											</label>
											<div class="Check_Radio FontDarkBlue">${DUEDATESTR_!}</div>
										</td>
										<td class="LeftTd" id="feedbackTimeTr" <#if reportFocus.dataSource?? && reportFocus.dataSource!='02'>style="display: none"</#if>>
											<label class="LabName labelNameWide"><span>反馈时限：</span></label>
											<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass"
												   data-options="tipPosition:'bottom'" style="width:210px; cursor:pointer;"
												   onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:true, isShowToday:false})"
												   value="${reportFocus.feedbackTimeStr!}" readonly="readonly"/>
										</td>
									</tr>
									<tr id="hdtDescribeTr" class="hide">
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>隐患描述：</span>
											</label>
											<textarea name="hdtDescribe" id="hdtDescribe" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;"
													  data-options="tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${reportFocus.hdtDescribe!}</textarea>
										</td>
									</tr>
									<tr id="tipoffContentTr" class="hide">
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span><label class="Asterik">*</label>举报内容：</span>
											</label>
											<textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;"
													  data-options="tipPosition:'bottom',validType:['maxLength[300]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
										</td>
									</tr>
 									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span>备注：</span>
											</label>
											<textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
										</td>
									</tr>
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
			gridTreeObj = null,//网格树对象
			_winHeight = 0, _winWidth = 0,
			detailFormJsonObject = null;

		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				dataSource = $("#dataSource").val() || "${reportFocus.dataSource!}",
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
					uploadSuccessCallback : function(file,response){
						uploadback(file,response);
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
				module			: 'threeOneTreatment',
				attachmentData	: {bizId: reportId, attachmentType:'THREE_ONE_TREATMENT', eventSeq: '1', isBindBizId: 'yes'},
				individualOpt 	: {
					isUploadHandlingPic : true
				},
				uploadSuccessCallback : function(file,response){
					uploadback(file,response);
				},
				deleteCallback:function(obj){
					deleteback(obj);
				},
				//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
				labelDict		: [{'name':'处理前', 'value':'1'}]
			};
            
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			$('#totDetailForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.87);
			});
			
			init4Location('occurred', {
				_startAddress :"${reportFocus.occurred!}",
				_startDivisionCode : "${reportFocus.regionCode!}", //默认选中网格，非必传参数
				_limit_select_level : 5//选择到哪个层级
			});
			if(dataSource == '05'){
				$('#tipoffContentTr').show();
				$('#tipoffContent').validatebox({required:true});
			}

			//加载网格
			if($('#gridName').length > 0) {
				gridTreeObj = AnoleApi.initGridZtreeComboBox("gridName", null,
						function(gridId, items) {
							if (items && items.length > 0) {
								//$("#regionLevel").val(items[0].gridLevel);
								$("#regionCode").val(items[0].orgCode);
							}
						},
						{
							ChooseType:"1",
							ShowOptions:{
								//选择到哪个层级 默认为网格层级
								AllowSelectLevel:"6"
							}
						}
				);
			}
			
			//隐患类型
			if($('#hiddenDangerTypeName').length > 0){
				AnoleApi.initTreeComboBox("hiddenDangerTypeName", "hiddenDangerType", "B210015003",
						function (dictValue, item) {
							var flag = isContainOther($('#hiddenDangerType').val());

							if(flag) {
								$('#hdtDescribeTr').show();
							} else {
								$('#hdtDescribeTr').hide();
								$('#hdtDescribe').val('');
							}

							$('#hdtDescribe').validatebox({
								required: flag
							});
						},
						["${reportFocus.hiddenDangerType!}"],
						{
							ShowOptions: {
								EnableToolbar : true
							}
						}
				);
			}

			//初始化编辑页面信息
			onchange4DataSource(dataSource,'');
			//发现渠道
			if($('#dataSourceName').length > 0) {
				AnoleApi.initTreeComboBox("dataSourceName", "dataSource", "B210015001",
						null, ["${reportFocus.dataSource!}"],
						{
							ShowOptions: {
								EnableToolbar : true
							},
							OnChanged : function(value, items) {
								onchange4DataSource(value, items);
							},
							FilterData:function(data) {
								var resuleData = [];
								//非12345上报的 才去除12345选项
								//网格员发现、12345
								if(dataSource == '01' || dataSource == '02'){
									for(var i=0;i<data.length;i++){
										if(data[i].value == dataSource){
											resuleData.push(data[i]);
										}
									}
								}else{
									for(var i=0;i<data.length;i++){
										if(data[i].value != '01' && data[i].value != '02'){
											resuleData.push(data[i]);
										}
									}
								}
								return resuleData;
							}
						});
			}
			
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
					subTaskUrl: '${rc.getContextPath()}/zhsq/reportTOT/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
					subTaskCallback: _subTaskOperate
				},
				reject: {
					rejectUrl: '${rc.getContextPath()}/zhsq/reportTOT/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
					rejectCallback: _rejectOperate
				},
				delete: {
					deleteUrl: '${rc.getContextPath()}/zhsq/reportTOT/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
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

			autoRequiredRender();
			
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
			
			detailFormJsonObject = $('#totDetailForm').serializeArray();
			
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
					mapType = 'EPIDEMIC_PRE_CONTROL',
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
				$("#totDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportTOT/startWorkflow4ReportFocus.jhtml");

				modleopen();

				$("#totDetailForm").ajaxSubmit(function(data) {
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
			var isSelectOrg = data.selectUser.isSelectOrg || false;

			if(isSelectOrg){
				_handlerConstructor2(data,isSelectOrg);
			}else{
				_handlerConstructor(data);
			}

			_itemSizeAdjust();
		}
		
		function dynamicConstructor(data) {//下一环节动态内容构造方法
			var dynamicContentMap = data.dynamicContentMap || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				isShowAddress = dynamicContentMap.isShowAddress || false,
				formTypeId = $('#formTypeId').val(),
				curNodeName = $('#curNodeName').val(),
				nextNodeName = $('#nodeName_').val();
			
			$('#isUploadHandlingdPic').val('false');
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}

			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
			}

			if($('#dynamicAddress').length == 1) {
				$('#dynamicAddress').val('');
			}

			//判断核实环节动态字典标签是否是：属实情况 （非网格员采集）
			if(formTypeId == '352') {
				if(curNodeName == 'task1') {
					if(nextNodeName == 'task2') {
						var collectSource = $('#collectSource').val();
						
						if(collectSource == '01') {
							allowSelectLevel = "6";
						} else {
							allowSelectLevel = "5,6";
						}
					} else if(nextNodeName == 'task13' || nextNodeName == 'task14' || nextNodeName == 'task15') {
						allowSelectLevel = "4,5,6";
					}
					
					gridTreeObj.settings.ShowOptions.AllowSelectLevel = allowSelectLevel;
				} else if(curNodeName == 'task4' && nextNodeName == 'task5') {
					$('#dictLabelName').val(dynamicContentMap.dictLabelName || '');
				} else if(curNodeName == 'task3' && nextNodeName == 'task5') {
					//网格员采集
					$('#dictLabelName').val('属实情况');
					$('#isUploadHandlingdPic').val('true');
				}
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
							$('#adviceNoteInitial').val(dynamicContentMap["adviceNoteInitial_" + dictValue]);
							
							_capDynamicContent($('#dynamicContent').val(), dictValue);
							
							if(formTypeId == '352' && curNodeName === 'task4') {
								var allowSelectLevel = "5,6",
									isEditableNode = false;
								
								if(dictValue == '0') {
									isEditableNode = true;
									allowSelectLevel = "6";
								}
								
								$('#verifyStatus').val(parseInt(dictValue, 10) + 1);
								$('#isEditableNode').val(isEditableNode);
								gridTreeObj.settings.ShowOptions.AllowSelectLevel = allowSelectLevel;
							}
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
								'<label class="LabName"><span><label class="Asterik">*</label><label id="dynamicTextLabelName">' + dynamicContentMap.textLabelName + '</label>：</span></label>' +
								'<textarea rows="3" style="height:50px;" id="dynamicContent" class="area1 easyui-validatebox autoDoubleCell" data-options="required:true,tipPosition:\'bottom\',validType:[\'maxLength[200]\',\'characterCheck\']" onblur="_capDynamicContent($(this).val());"></textarea>' +
							'</td>' +
						'</tr>'
					);

					var winWidth = $(window).width();
					$("#flowSaveForm .autoDoubleCell").not(".isSettledAutoWidth").each(function() {
						$(this).width((winWidth - $(this).siblings(".LabName").eq(0).outerWidth(true)) * 0.92)
								.addClass("isSettledAutoWidth");
					});
					
					$('#dynamicTextLabelName').attr('textLabelName', dynamicContentMap.textLabelName);
					
					if(dynamicDictJson) {
						var dynamicDictArray = eval('(' + dynamicDictJson + ')'),
							textIndex = null,
							textName = null;
						
						for(var index in dynamicDictArray) {
							textIndex = "textLabelName_" + index;
							textName = dynamicContentMap[textIndex];
							
							if(textName) {
								$('#dynamicTextLabelName').attr(textIndex, textName);
							}
						}
					}
				}

				if(curNodeName == 'task4') {
					$('#dynamicContentAreaTr').hide();
				} else {
					$('#dynamicContent').validatebox({
						required: true
					});

					$('#dynamicContentAreaTr').show();
				}

			} else if($('#dynamicContentAreaTr').length == 1) {
				$('#dynamicContentAreaTr').hide();
				$('#dynamicContent').validatebox({
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
								'<input type="text" class="inp1 easyui-validatebox" data-options="tipPosition:\'bottom\',validType:[\'maxLength[1500]\',\'characterCheck\']" name="occurred" id="dynamicAddress" value="" />' +
								'<input type="hidden" name="regionCode" id="dynamicRegionCode"/>' +
							'</td>' +
						'</tr>'
					);

					$("#dynamicAddress").anoleAddressRender({
						_source : 'XIEJING',//必传参数，发现渠道
						_select_scope : 0,
						_show_level : 6,//显示到哪个层级
						_context_show_level : 1,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
						_startAddress :"",
						_startDivisionCode : startDivisionCode, //默认选中网格，非必传参数
						_customAddressIsNull : false,
						_addressMap : {//编辑页面可以传这个参数，非必传参数
							_addressMapShow : false,//是否显示地图标注功能
							_addressMapIsEdit : true
						},
						BackEvents : {
							OnSelected : function(api) {
								$("#dynamicAddress").val(api.getAddress());
								$("#dynamicRegionCode").val(api.getInfoOrgCode());
								_capDynamicContent(api.getAddress());
							},
							OnCleared : function(api) {
								//清空按钮触发的事件
								$("#dynamicAddress").val('');
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
			
			if(curNodeName === 'task3') {
				var allowSelectLevel = "6",
					isEditableNode = true,
					isLocationRequired = true;
					
				if(nextNodeName === 'end1') {
					isEditableNode = false;
					allowSelectLevel = "5,6";
					isLocationRequired = false;
				}
				
				locationRequiredCheck(isLocationRequired);
				$('#isEditableNode').val(isEditableNode);
				gridTreeObj.settings.ShowOptions.AllowSelectLevel = allowSelectLevel;
			}
			
			//判断是否上传处理后图片
			var isUploadHandledPic = data.isUploadHandledPic||false;
			$('#isUploadHandledPic').val(isUploadHandledPic);
			$('#picTypeName').val(data.picTypeName||'');
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

			$('#adviceNote').val(adviceNote);
			$('#adviceNoteInitial').val(adviceNoteInitial);
			
			_capDynamicContent(dynamicContent, dictValue);
		}
		
		function _subTaskOperate() {
			var isUploadHandlingdPic = $('#isUploadHandlingdPic').val()||false;
			var dataSource = $('#dataSource').val();
            var longitude = $('#x').val(),
                latitude = $('#y').val(),
				occurred = $('#occurred').val(),
                mapType = $('#mapt').val();

			if($('#dynamicContentDictTr').length == 1 && $('#dynamicContentDictTr').is(':visible')) {
				$('#dynamicDict').validatebox({
					required: true
				});
				var dictLabelName = $('#dictLabelName').val()||'';
				var dynamicDictId = $('#dynamicDictId').val()||'';
				//情况属实时，处理中图片为必填
				if('属实情况' == dictLabelName && '0' == dynamicDictId){
					isUploadHandlingdPic = true;
				}
			}

			if($('#dynamicContentAreaTr').length == 1 && $('#dynamicContentAreaTr').is(':visible')) {
				$('#dynamicContent').validatebox({
					required: true
				});
			}

			if(dataSource == '03') {
				if($('#discoveryStaffTd').length == 1 && $('#discoveryStaffTd').is(':visible') && isBlankStringTrim($('#discoveryStaff').val())){
					$.messager.alert('警告', "请输入发现部门！", 'warning');
					return;
				}
			}else if(dataSource == '05'){
				if($('#tipoffContentTr').length == 1 && $('#tipoffContentTr').is(':visible') && isBlankStringTrim($('#tipoffContent').val())){
					$.messager.alert('警告', "请输入举报内容！", 'warning');
					return;
				}
			}

			if(dataSource != '05'){
                var isLocationRequired = $('#isLocationRequired').val();

				if(isBlankStringTrim(occurred)){
					$.messager.alert('警告', "请选择发生地址！", 'warning');
					return;
				}
                if(isLocationRequired == 'true' && isBlankStringTrim(longitude)
						&& isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
                    $.messager.alert('警告', "请输入发生地址！", 'warning');
                    return;
                }
            }

			//flowSaveForm流程办理页面先进行验证
			var isValid =  $("#flowSaveForm").form('validate');

			if(isValid){
				//判断是否上传处理后图片
				var isUploadHandledPic = $('#isUploadHandledPic').val()||false;
				//处理后
				var typeName = $('#picTypeName').val();
				//个性化参数
				var optionObj = {
					isUseOption:true,
					msg:typeName
				};

				//需要开启个性化参数，提示信息中附件名称根据传参确定
				if(isUploadHandledPic == true || isUploadHandledPic == 'true'){
					isValid = verifyAttIsUpload(typeName,3,isUploadHandledPic,optionObj);
				}
				if(isUploadHandlingdPic == true || isUploadHandlingdPic == 'true'){
					typeName = '处理中';
					optionObj.msg = typeName;
					isValid = verifyAttIsUpload(typeName,2,isUploadHandlingdPic,optionObj);
				}
			}
			if(isValid) {
				if(formCompare4TOT() == false) {
					saveTOT();
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
				curNodeName = '${curNodeName!}';

			if(curNodeName == 'task4') {
				var isLocationRequired = false;
				
				if(dictValue == '0') {
					$('#dynamicContentAreaTr').hide();
					$('#dynamicContent').validatebox({
						required: false
					});
					isLocationRequired = true;
				} else {
					var dynamicTextLabelName = $('#dynamicTextLabelName').attr('textLabelName_' + dictValue);
					
					if(isBlankParam(dynamicTextLabelName)) {
						dynamicTextLabelName = $('#dynamicTextLabelName').attr('textLabelName');
					}
					
					$('#dynamicTextLabelName').html(dynamicTextLabelName);
					$('#dynamicContent').validatebox({
						required: true
					});
					$('#dynamicContentAreaTr').show();
				}
				
				locationRequiredCheck(isLocationRequired);
			}

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
					href: "${rc.getContextPath()}/zhsq/reportEPC/flowDetail.jhtml?instanceId=" + instanceId + "&reportType=" + $('#reportType').val() + "&listType=${listType!}",
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
		
		function saveTOT() {
			var isValid =  $("#totDetailForm").form('validate'),
					longitude = $('#x').val(),
					latitude = $('#y').val(),
					mapType = $('#mapt').val();
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv') && checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			if(isValid) {
				var isLocationRequired = $('#isLocationRequired').val();
				
				if(isLocationRequired == 'true' && isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
					$.messager.alert('警告', "请先完成地理标注！", 'warning');
					return;
				}
			}
			
			if(isValid) {
				modleopen();

				$("#totDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportTOT/saveReportFocus.jhtml");

				$("#totDetailForm").ajaxSubmit(function(data) {
					modleclose();

					if(data.success && data.success == true) {
						var isRegionNotChanged = specificFormAttrCompare(detailFormJsonObject,
												$('#totDetailForm').serializeArray(),
												{'regionCode' : '','reportTime':'','occurred':'','dataSource':''}),
							compareFlag = specificFormCompare4TOT();
						
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
		}
		
		function locationRequiredCheck(isLocationRequired) {
			$('#isLocationRequired').val(isLocationRequired);
			
			if(isLocationRequired == true || isLocationRequired == 'true') {
				$('#occurred').validatebox({
					required: true
				});
				
				$('#eventWechatNorformDiv label.locationAsterik').show();
			} else {
				$('#occurred').validatebox({
					required: false
				});
				
				$('#eventWechatNorformDiv label.locationAsterik').hide();
			}
		}
		
		function autoRequiredRender() {
			var isSmallMedical = <#if reportFocus.collectSource?? && reportFocus.collectSource=='05'>true<#else>false</#if>;
			
			autoRequiredBase('totDetailForm', isSmallMedical);
		}
		
		function formCompare4TOT() {
			var isForce2Save = $('#isForce2Save').val();
			var compareFlag = true;
			
			if(isForce2Save === 'false') {
				var detailFormJsonObjectNow = $('#totDetailForm').serializeArray();
				
				compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, {/*
					'personInvolved' : '',
					'siteArea' : '',
					'remark' : ''
				*/});
			} else {
				compareFlag = false;
				$('#isForce2Save').val('false');
			}
			
			return compareFlag;
		}
		
		function specificFormCompare4TOT() {
			var detailFormJsonObjectNow = $('#totDetailForm').serializeArray(),
				adviceNoteInitial = $('#adviceNoteInitial').val(),
				extraSpecificAttrId = {},
				specificAttrId = null,
				compareFlag = false;

			if(adviceNoteInitial.indexOf("@tipoffContent@") >= 0) {
				extraSpecificAttrId.tipoffContent = '';
			}
			
			specificAttrId = $.extend(extraSpecificAttrId, _capAdviceNoteAssociatedAttr(adviceNoteInitial));
			compareFlag = specificFormAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, specificAttrId);
				
			detailFormJsonObject = detailFormJsonObjectNow;
			
			return compareFlag;
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

		function onchange4DataSource(value, items) {
			$('#dataSource').val(value);
			//动态变更所属区域可选择层级
			//网格巡查发现 选择到网格层级
			if(value == '01'){
				gridTreeObj.settings.ShowOptions.AllowSelectLevel = '6';
			}else{
				gridTreeObj.settings.ShowOptions.AllowSelectLevel = '5,6';
			}

			if(value == '02'){
				$('#feedbackTimeTr').show();
				$('#feedbackTime').validatebox({required:true});
			}else{
				$('#feedbackTimeTr').hide();
				$('#feedbackTime').validatebox({required:false});
			}

			//泉州市级相关部门督查（检查）发现 发现部门
			if(value == '03'){
				$('#discoveryStaffTd').show();
				$('#discoveryStaff').validatebox({required:true});

				//隐藏报告方式
				$('#reportWayTd').hide();
			}else{
				$('#discoveryStaffTd').hide();
				$('#discoveryStaff').validatebox({required:false});

				//展示报告方式
				$('#reportWayTd').show();
			}

			/*群众举报发现 发生地址非必填*/
			if(value == '05'){
				$('#occurredAsterik').hide();
				/*$('#x').val('');
				$('#y').val('');
				$('#occurred').val('');
				$('#mapt').val('');*/

				$('#tipoffContentTr').show();
				$('#tipoffContent').validatebox({required:true});

				//发生地址非必填
                $('#isLocationRequired').val('false');
			}else{
				$('#occurredAsterik').show();
				$('#tipoffContentTr').hide();
				$('#tipoffContent').validatebox({required:false});

                //发生地址必填
                $('#isLocationRequired').val('true');
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
					orgInitRole:[100220,101700],//角色id
					multi:true,                 //是否允许多选
					isRepeat:true,              //用户是否可以重复
					isUseReallyOrgType : true,	//是否只选择本级用户
					orgType:0					//组织类型： 1 单位 0 部门

					//clickLevelRange:[1,2]    //部门类型组织树可以点击的有效层级
					//isSubordinate:false,     //是否查询下级数据，只有部门类型查人员有效
					//orgInitRole:[143,150],
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
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>