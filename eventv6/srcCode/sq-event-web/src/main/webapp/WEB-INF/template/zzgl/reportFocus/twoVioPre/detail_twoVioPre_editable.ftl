<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>两违事件详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />

		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		<style type="text/css">
			.singleCellClass{width: 55%;}
			.doubleCellClass{width: 88%}
			.labelNameWide{width: 132px;}
			.squareMeterUnit{width: 65px;}
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
					<form id="twoVioPreForm" name="twoVioPreForm" action="" method="post">
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
						<input type="hidden" id="module" value="TWO_VIO_PRE"/>
						<#--信息采集来源-->
						<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
						<#--是第一副网格长核实环节-->
						<input type="hidden" id="isEditableNode" name="isEditableNode" value="<#if isEditableNode??>${isEditableNode?c}<#else>true</#if>"/>
						<#--报告方式-->
						<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
						
						<input type="hidden" id="isCheckRegion" name="isCheckRegion" value="true" />
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
											<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox singleCellClass" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>业主姓名：</span></label>
											<input  id="personInvolved" name="personInvolved" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" value="${reportFocus.personInvolved!}" />
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label id="occurredAsterik" class="Asterik autoRequiredAsterik">*</label>发生地址：</span></label>
											<input type="text" class="inp1 easyui-validatebox autoRequired autoWidth" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>地理标注：</span></label>
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>占地面积：</span></label>
											<input type="text" id="areaCovered" name="areaCovered" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:99999999.99,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.areaCovered!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>办理用地和规划手续：</span></label>
											<div class="zz-form" zz-form-filter="isRoutineDiv">
												<input type="hidden" id="isRoutine" name="isRoutine" value="${reportFocus.isRoutine!'0'}" />
												<input type="checkbox" zz-form-filter="isRoutineCheckBox" zz-text="是|否" <#if reportFocus.isRoutine?? && reportFocus.isRoutine=='1'>checked</#if> />
											</div>
										</td>
									</tr>
									<tr id="lecPpnTr" style="<#if reportFocus.isRoutine?? && reportFocus.isRoutine == '1'><#else>display: none</#if>">
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>土地证号：</span></label>
											<input  id="lecCode" name="lecCode" style="width: 235px;" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:false,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.lecCode!}" onblur="_capDynamicContent();" />
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span><label class="Asterik">*</label>规划许可证号：</span></label>
											<input  id="ppnCode" name="ppnCode" style="width: 217px" type="text" class="inp1 easyui-validatebox singleCellInpClass" data-options="required:false,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" value="${reportFocus.ppnCode!}" onblur="_capDynamicContent();" />
										</td>
									</tr>
									<#if reportFocus.dataSource?? && reportFocus.dataSource == '03'>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>图斑编号：</span></label>
											<input type="text" class="inp1 easyui-validatebox" style="width: 87%;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="mapNum" id="mapNum" value="${reportFocus.mapNum!}" />
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>耕地面积：</span></label>
											<input type="text" id="cultivableLandArea" name="cultivableLandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.cultivableLandArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>基本农田面积：</span></label>
											<input type="text" id="farmlandArea" name="farmlandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.farmlandArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>林地面积：</span></label>
											<input type="text" id="woodlandArea" name="woodlandArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.woodlandArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>允许建设区面积：</span></label>
											<input type="text" id="constructableArea" name="constructableArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.constructableArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>公益林面积：</span></label>
											<input type="text" id="publicForestArea" name="publicForestArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.publicForestArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>条件建设区面积：</span></label>
											<input type="text" id="conditionalConstructionArea" name="conditionalConstructionArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.conditionalConstructionArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>城乡规划面积：</span></label>
											<input type="text" id="urpArea" name="urpArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.urpArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>限制建设区面积：</span></label>
											<input type="text" id="restrictedConstructionArea" name="restrictedConstructionArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.restrictedConstructionArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>村庄规划面积：</span></label>
											<input type="text" id="villagePlanningArea" name="villagePlanningArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.villagePlanningArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>生态红线面积：</span></label>
											<input type="text" id="ecologicalRedlineArea" name="ecologicalRedlineArea" class="inp1 easyui-numberbox singleCellClass" data-options="tipPosition:'bottom',max:9999,precision:2" style="width: 210px; height: 28px;" value="${reportFocus.ecologicalRedlineArea!}" />
											<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
										</td>
									</tr>
									</#if>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span>建设状态：</span></label>
											<input type="hidden" id="constructionStatus" name="constructionStatus" value="${reportFocus.constructionStatus!}" />
											<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" id="constructionStatusName" value="" />
										</td>
										<td class="LeftTd">
											<label class="LabName labelNameWide"><span>建（构）筑物用途：</span></label>
											<input type="hidden" id="buildingUsage" name="buildingUsage" value="${reportFocus.buildingUsage!}" />
											<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" id="buildingUsageName" value="" />
										</td>
									</tr>
									<tr id="conStatusDescribeTr" class="hide">
										<td class="LeftTd" colspan="2">
											<label class="LabName">
												<span>建设状态说明：</span>
											</label>
											<textarea name="conStatusDescribe" id="conStatusDescribe" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" onblur="_capDynamicContent();">${reportFocus.conStatusDescribe!}</textarea>
										</td>
									</tr>
									<#if reportFocus.dataSource?? && reportFocus.dataSource == '05'>
										<tr>
											<td class="LeftTd" colspan="2">
												<label class="LabName"><span><label class="Asterik">*</label>举报内容：</span></label><textarea name="tipoffContent" id="tipoffContent" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']">${reportFocus.tipoffContent!}</textarea>
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
			gridTreeObj = null,//网格树对象
			detailFormJsonObject = null;
			
		$(function () {
			var $winH = 0, $topH = 0, $btnH = 0,
				reportId = $("#reportId").val(),
				bigFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.jpg,.gif,.png,.jpeg,.webp',
					module			: 'twoVioPre',
					attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}, {'name':'处理中', 'value':'2','checked':true}, {'name':'处理后', 'value':'3'}]
				},
				bigViodeUploadOpt = {
					useType			: 'view',
					fileExt			: '.mp4,.avi,.amr',
					module			: 'twoVioPre',
					attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}]
				},
				attachFileUploadOpt = {
					useType			: 'view',
					fileExt			: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					module			: 'twoVioPre',
					attachmentData	: {bizId: reportId, attachmentType:'TWO_VIO_PRE', eventSeq: '1', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					},
					//替换附件上传时参数 labelDict，待办列表需要替换，附件默认为处理中
					labelDict		: [{'name':'处理前', 'value':'1'}]
				},
				_winWidth = $(window).width(),
				dataSource = $('#dataSource').val(),
				locationOpt = {
					_startAddress :"${reportFocus.occurred!}",
					_startDivisionCode : "${reportFocus.regionCode!}" //默认选中网格，非必传参数
				};
			
			$('#twoVioPreForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.90);
			});
			
			if(dataSource != '01') {
				locationOpt._limit_select_level = 5;
			}
			
			if(dataSource == '03') {
				$.extend(locationOpt, {
					_addressMap : {//编辑页面可以传这个参数，非必传参数
						_wgGisType : 'twoVioShape',
						_wgGisId : $('#reportId').val()
					}
				});
			}
			
			init4Location('occurred', locationOpt);

			zzForm.render('checkbox','isRoutineDiv');
			zzForm.on('checkbox(isRoutineCheckBox)', function (data) {
				if(data.checked) {
					$('#isRoutine').val('1');
					$('#lecPpnTr').show();
				} else {
					$('#isRoutine').val('0');
					$('#lecPpnTr').hide();
					$('#lecCode').val('');
					$('#ppnCode').val('');
				}
				
				_capDynamicContent();
			});

			AnoleApi.initListComboBox("constructionStatusName", "constructionStatus", "B210001001", function (dictValue, item) {
				var flag = isContainOther($('#constructionStatus').val());

				if(flag) {
					$('#conStatusDescribeTr').show();
				} else {
					$('#conStatusDescribeTr').hide();
					$('#conStatusDescribe').val('');
				}

			}, ["${reportFocus.constructionStatus!}"], {
				ShowOptions: {
					EnableToolbar : true
				},
				OnChanged: function(dictValue, item) {
					_capDynamicContent();
				},
				OnCleared: function() {
					$('#conStatusDescribeTr').hide();
					$('#conStatusDescribe').val('');
				}
			});
			AnoleApi.initListComboBox("buildingUsageName", "buildingUsage", "B210001002", null, ["${reportFocus.buildingUsage!}"], {
				ShowOptions: {
					EnableToolbar : true
				}
			});

			//加载网格
			gridTreeObj = AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							$("#regionCode").val(items[0].orgCode);
						}
					},
					{
						ChooseType:"1",
						ShowOptions:{
							<#if (isEditableNode?? && isEditableNode) || (reportFocus.dataSource?? && reportFocus.dataSource == '01')>
							//来源是非网格员只需要选择到村社区层级
							AllowSelectLevel:"6"//选择到哪个层级
							<#else >
							AllowSelectLevel:"5,6"//选择到哪个层级
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
						subTaskUrl: '${rc.getContextPath()}/zhsq/reportTwoVioPre/subWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						subTaskCallback: _subTaskOperate
					},
					reject: {
						rejectUrl: '${rc.getContextPath()}/zhsq/reportTwoVioPre/rejectWorkflow4ReportFocus.jhtml?reportType=' + reportType,
						rejectCallback: _rejectOperate
					},
					delete: {
						deleteUrl: '${rc.getContextPath()}/zhsq/reportTwoVioPre/delReportFocus.jhtml?reportType=' + reportType + '&reportUUID=' + $('#reportUUID').val()
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
			
			$('#topTitleUl  > li > a').eq(0).click();
			
			detailFormJsonObject = $('#twoVioPreForm').serializeArray();
			
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
				mapType = 'TWO_VIO_PRE',
				isEdit = true;
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
		}
		
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
			_itemSizeAdjust();
		}
		
		function dynamicConstructor(data) {//下一环节动态内容构造方法
			var dynamicContentMap = data.dynamicContentMap || {},
				isShowText = dynamicContentMap.isShowText || false,
				isShowDict = dynamicContentMap.isShowDict || false,
				isBaseInfoEditable = data.isBaseInfoEditable || false;
			
			if($('#dynamicDict').length == 1) {
				$('#dynamicDict').val('');
			}
			
			if($('#dynamicContent').length == 1) {
				$('#dynamicContent').val('');
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
							
							var curNodeName = $('#curNodeName').val(),
								nextNodeName = $('#nodeName_').val(),
								formTypeId = $('#formTypeId').val();
							
							if(isShowText && formTypeId == '350' && curNodeName == 'task3' && nextNodeName == 'task4') {
								var isContentRequired = true,
									verifyStatus = '1',
									isCheckRegion = true,
									allowSelectLevel = '6';
								
								$('#dynamicContent').val('');
								$('#dynamicContent').removeAttr('name');
								
								if(dictValue == '3') {
									isContentRequired = false;
									isCheckRegion = false;
									verifyStatus = '2';
									allowSelectLevel = '5,6';
									$('#dynamicContentAreaTr').hide();
								} else if(dictValue == '4') {
									isCheckRegion = false;
									$('#dynamicContent').attr('name', 'belongRegionName');
									$('#dynamicContentSpan').html('<label class="Asterik">*</label>所属村：');
									verifyStatus = '3';
									allowSelectLevel = '5,6';
									$('#dynamicContentAreaTr').show();
								} else {
									$('#dynamicContentSpan').html('<label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：');
									$('#dynamicContentAreaTr').show();
								}
								
								$('#dynamicContent').validatebox({
									required: isContentRequired
								});
								
								$('#isCheckRegion').val(isCheckRegion);
								$('#verifyStatus').val(verifyStatus);
								gridTreeObj.settings.ShowOptions.AllowSelectLevel = allowSelectLevel;
								autoRequiredRender();
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
				$('#dynamicContentAreaTr').remove();
				
				if($('#dynamicContentAreaTr').length == 0) {
					$('#remarkTr').before(
						'<tr id="dynamicContentAreaTr">' + 
							'<td>' + 
								'<label class="LabName"><span id="dynamicContentSpan"><label class="Asterik">*</label>' + dynamicContentMap.textLabelName + '：</span></label>' +
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
		}
		
		function adviceNoteConstructor(data) {//下一环节短信模板构造方法
			var adviceNote = data.adviceNote || '',
				adviceNoteInitial = data.adviceNoteInitial || '',
				dynamicContentMap = data.dynamicContentMap || {};
			
			if($('#dynamicDictId').length > 0) {
				var dictValue = $('#dynamicDictId').val();
				
				if(dictValue) {
					adviceNote = dynamicContentMap["adviceNote_" + dictValue] || adviceNote;
					adviceNoteInitial = dynamicContentMap["adviceNoteInitial_" + dictValue] || adviceNoteInitial;
				}
			}
			
			if($('#adviceNote').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNote" value="" />');
			}
			
			if($('#adviceNoteInitial').length == 0) {
				$('#flowSaveForm').append('<input type="hidden" id="adviceNoteInitial" value="" />');
			}
			
			$('#adviceNote').val(adviceNote);
			$('#adviceNoteInitial').val(adviceNoteInitial);
			
			_capDynamicContent();
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
			var isValid = $("#flowSaveForm").form('validate');
			if(isValid) {
				var isRoutine = $('#isRoutine').val();
				
				if(isRoutine == '1') {
					if($('#lecCode').val() == '' && $('#ppnCode').val() == '') {
						$.messager.alert('提示','土地证号或规划许可证号不能为空!','warning');
						return;
					}
				}
			}
			
			if(isValid) {
				if(formCompare4TwoVioPre() == false) {
					saveTwoVioPre();
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
		
		function _capDynamicContent(dynamicContent) {
			var adviceNote = $('#adviceNote').val(),
				advice = '',
				dataSource = $('#dataSource').val();
			
			dynamicContent = dynamicContent || $('#dynamicContent').val();
			
			if(adviceNote) {
				advice = adviceNote;
				
				if(dynamicContent && advice.indexOf('@dynamicContent@') >= 0) {
					advice = advice.replaceAll('@dynamicContent@', dynamicContent);
				}
				
				if(advice.indexOf('@tipoffContent@') >= 0) {
					//群众举报事件动态变更举报内容
					if(dataSource && dataSource == '05') {
						advice = advice.replaceAll('@tipoffContent@', dynamicContent);
					}
				}
				
				if(advice.indexOf('@constructionStatusName@') >= 0) {
					var constructionStatusName = $('#constructionStatusName').val();
					
					if(isNotBlankString(constructionStatusName)) {
						var conStatusDescribe = $('#conStatusDescribe').val();
						
						if(isNotBlankString(conStatusDescribe)) {
							constructionStatusName = conStatusDescribe;
						}
						
						constructionStatusName = "该地块现" + constructionStatusName;
					}
					
					advice = advice.replaceAll('@constructionStatusName@', constructionStatusName);
				}
				
				if(advice.indexOf('@lecCode@') >= 0) {
					var lecCode = $('#lecCode').val(),
						lecContent = "未办理合法用地手续";
					
					if(isNotBlankString(lecCode)) {
						lecContent = "有办理合法用地手续，证号为：" + lecCode;
					}
					
					advice = advice.replaceAll('@lecCode@', lecContent);
				}
				
				if(advice.indexOf('@ppnCode@') >= 0) {
					var ppnCode = $('#ppnCode').val(),
						ppnContent = "未办理建设工程规划许可证";
					
					if(isNotBlankString(ppnCode)) {
						ppnContent = "有办理建设工程规划许可证，证号为：" + ppnCode;
					}
					
					advice = advice.replaceAll('@ppnCode@', ppnContent);
				}
				
				if(advice.indexOf('@dynamicContent@') >= 0
				|| advice.indexOf('@tipoffContent@') >= 0
				|| advice.indexOf('@constructionStatusName@') >= 0
				|| advice.indexOf('@lecCode@') >= 0
				|| advice.indexOf('@ppnCode@') >= 0) {
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
		function saveTwoVioPre() {
			var isValid =  $("#twoVioPreForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val();
			
			isValid = isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('bigVideoUploadDiv')
					&& checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			
			if(isValid) {
				if($('#occurredAsterik').is(':visible') && isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)) {
					$.messager.alert('警告', "请先完成地理标注！", 'warning');
					return;
				} else {
					if($('#bigFileUploadDivAsterik').is(':visible')) {
						var dataSource = $('#dataSource').val(),
							formTypeId = $('#formTypeId').val(),
							curNodeName = $('#curNodeName').val(),
							nextNodeName = $('#nodeName_').val();
						
						if(dataSource == '01') {
							isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
						}
						
						if(formTypeId == '350') {
							var isCheckHandlingPic = false;
							
							if(curNodeName == 'task3' && nextNodeName == 'task4') {
								var dictValue = $('#dynamicDictId').val();
								
								isCheckHandlingPic = dictValue == '0' || dictValue == '1' || dictValue == '2' || dictValue == '4';
							} else if(curNodeName == 'task4' && nextNodeName == 'task6') {
								isCheckHandlingPic = true;
							}
							
							if(isValid && isCheckHandlingPic) {
								isValid = checkAttachment4BigFileUpload(2, $('#bigFileUploadDiv div[file-status="complete"]'));
							}
						}
					}
				}
			}
			
			if(isValid) {
				modleopen();

				$("#twoVioPreForm").attr("action", "${rc.getContextPath()}/zhsq/reportTwoVioPre/saveReportFocus.jhtml");

				$("#twoVioPreForm").ajaxSubmit(function(data) {
					modleclose();

					if(data.success && data.success == true) {
						var isRegionNotChanged = specificFormAttrCompare(detailFormJsonObject, $('#twoVioPreForm').serializeArray(), {'regionCode' : ''}),
							compareFlag = specificFormCompare4TwoVioPre();
						
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
		
		function autoRequiredRender() {
			var curNodeName = $('#curNodeName').val(),
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				isRequired = dataSource == '01';
			
			if(formTypeId == '350' && curNodeName == 'task3') {
				var dictValue = $('#dynamicDictId').val();
				
				isRequired = dictValue == '0' || dictValue == '1' || dictValue == '2' || dictValue == '4';
			}
			
			autoRequiredBase('twoVioPreForm', isRequired);
		}
		
		function formCompare4TwoVioPre() {
			var isForce2Save = $('#isForce2Save').val();
			var compareFlag = true;
			
			if(isForce2Save === 'false') {
				var detailFormJsonObjectNow = $('#twoVioPreForm').serializeArray();
				
				compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, {
					'constructionStatus' : '00',
					'buildingUsage' : '00'
				});
			} else {
				compareFlag = false;
				$('#isForce2Save').val('false');
			}
			
			return compareFlag;
		}
		
		function specificFormCompare4TwoVioPre() {
			var detailFormJsonObjectNow = $('#twoVioPreForm').serializeArray(),
				adviceNoteInitial = $('#adviceNoteInitial').val(),
				extraSpecificAttrId = {},
				specificAttrId = null,
				compareFlag = false;
			
			if(adviceNoteInitial.indexOf("@mapNum@") >= 0) {
				extraSpecificAttrId.mapNum = '';
			}
			if(adviceNoteInitial.indexOf("@areaCovered@") >= 0) {
				extraSpecificAttrId.areaCovered = '';
			}
			if(adviceNoteInitial.indexOf("@cultivableLandArea@") >= 0) {
				extraSpecificAttrId.cultivableLandArea = '';
			}
			if(adviceNoteInitial.indexOf("@personInvolved@") >= 0) {
				extraSpecificAttrId.personInvolved = '';
			}
			if(adviceNoteInitial.indexOf("@tipoffContent@") >= 0) {
				extraSpecificAttrId.tipoffContent = '';
			}
			
			specificAttrId = $.extend(extraSpecificAttrId, _capAdviceNoteAssociatedAttr(adviceNoteInitial));
			compareFlag = specificFormAttrCompare(detailFormJsonObject, detailFormJsonObjectNow, specificAttrId);
				
			detailFormJsonObject = detailFormJsonObjectNow;
			
			return compareFlag;
		}
		
		<#if feedbackCount?? && (feedbackCount > 0)>
		function fetchTwoVioPreFeedback() {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#reportUUID").val()+"&bizType=${bizType!}";
			$("#feedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
			$("#feedbackListDiv > iframe").width($("#workflowDetail").width());
			$("#feedbackListDiv").height('auto');
		}
		</#if>
		function isContainOther(dictValue) {
			var flag = false;

			if(isNotBlankStringTrim(dictValue) && dictValue == '99') {
				flag = true;
			}

			return flag;
		}
	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/base/detail_base.ftl" />
</html>