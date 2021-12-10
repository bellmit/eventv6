<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>信访人员稳控详情</title>
		<script>
			window.__enableEasyComp__ = false;
		</script>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/residentSelector.js"></script>
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
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
						<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
						<input type="hidden" id="module" value="PETITION_PERSON"/>
						<input type="hidden" id="module_iResidenceAddr" value="PETITION_PERSON_IRESIDENCEADDR"/>
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
											<input type="hidden" id="regionCode" name="regionCode" <#if reportFocus.isRightOrg== '0'>value=""<#else>value="${reportFocus.regionCode!}"</#if> />
											<input type="text" id="gridName" <#if reportFocus.isRightOrg== '0'>value=""<#else>value="${reportFocus.regionName!}"</#if> class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
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
											<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label>
											<input type="hidden" id="partyId" name="partyId" value="${reportFocus.partyId!}"/>
											<input type="hidden" id="partyName_" name="partyName_" value="${reportFocus.partyName_!}"/>
											<input type="text" name="partyName" id="partyName" value="${reportFocus.partyName_!}" class="inp1 easyui-validatebox w300" data-options="tipPosition:'bottom',required:true,validType:['maxLength[250]','characterCheck']"/>
										</td>
										<td class="LeftTd">
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>证件类型：</span></label>
											<input type="hidden" id="certType" name="certType" value="${reportFocus.certType!}"/>
											<input type="text" name="certTypeName" id="certTypeName" value="${reportFocus.certTypeName!}" class="inp1 easyui-validatebox w300" data-options="<#if reportFocus.dataSource == '02'>required:true,</#if>tipPosition:'bottom'"/>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>证件号码：</span></label>
											<input type="hidden" id="identityCard" name="identityCard" value="${reportFocus.identityCard!}"/>
											<input type="text" name="identityCard_" id="identityCard_" value="${reportFocus.identityCard!}" onblur="set_identityCard($(this).val());" class="inp1 easyui-validatebox w300" data-options="<#if reportFocus.dataSource == '02'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[50]','idcard']"/>
										</td>
										<td class="LeftTd">
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>现住地：</span></label>
											<input type="hidden" id="residenceAddrNo" name="residenceAddrNo" value="${reportFocus.residenceAddrNo!}"/>
											<input type="hidden" id="iResidenceAddr" name="iResidenceAddr" value="${reportFocus.iResidenceAddr!}"/>
											<input type="text" name="iResidenceAddr_" id="iResidenceAddr_" value="${reportFocus.iResidenceAddr!}" class="inp1 easyui-validatebox w300" data-options="<#if reportFocus.dataSource == '02'>required:true,</#if>tipPosition:'bottom',validType:['maxLength[250]','characterCheck']"/>
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span>地理标注：</span></label>
											<style type="text/css">
												.mapTab{ padding-left:20px; cursor:pointer;}/*地图为标注状态下的图标*/
												.mapTab2{ padding-left:20px; cursor:pointer; color:#4489ca;}
												.mapTab3{ width:11px; height:20px;display:inline-block;}
											</style>
											
											<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2_iResidenceAddr">标注地理位置</b></span>
											
											<input id="x_iResidenceAddr" name="resMarker_iResidenceAddr.x" type="hidden"  value=""/>
											<input id="y_iResidenceAddr" name="resMarker_iResidenceAddr.y" type="hidden"  value=""/>
											<input id="hs_iResidenceAddr" name="hs_iResidenceAddr" type="hidden"  value=""/>
											<input id="mapt_iResidenceAddr" name="resMarker_iResidenceAddr.mapType" type="hidden"  value=""/>
											
											<script type="text/javascript">
												getMarkerData_iResidenceAddr();
												
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
												
												function init4Location_iResidenceAddr(locationId, option) {
													var locationOption = {
														_source : 'XIEJING',//必传参数，数据来源
														_select_scope : 0,
														_show_level : 6,//显示到哪个层级
														_context_show_level : 0,//回填到街道，使用时是需要进行地址搜索，而不能直接点击确定
														_startAddress :"${reportFocus.iResidenceAddr!}",
														_startDivisionCode : "${startDivisionCode!}", //默认选中网格，非必传参数
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
																
																$("#iResidenceAddr").val(api.getAddress());
																$("#iResidenceAddr_").val(api.getAddress());
																
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
																var partyId = $('#partyId').val();
																if(partyId==null||partyId==''){
																	$("#iResidenceAddr").val('');
																	$("#iResidenceAddr_").val('');
																	$("#residenceAddrNo").val('');
																	$('#x_iResidenceAddr').val('');
																	$('#y_iResidenceAddr').val('');
																	$('#mapt_iResidenceAddr').val('');
																	$("#mapTab2_iResidenceAddr").html('标注地理位置');
																}else{
																	$("#iResidenceAddr_").val(api.getAddress());
																}
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
											</script>
										</td>
									</tr>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><#if reportFocus.dataSource == '02'><label class="Asterik">*</label></#if>人口类型：</span></label>
											<input type="hidden" id="partyType" name="partyType" value="${reportFocus.partyType!}"/>
											<input type="text" name="partyTypeName" id="partyTypeName" value="${reportFocus.partyTypeName!}" class="inp1 easyui-validatebox w300" data-options="<#if reportFocus.dataSource == '02'>required:true,</#if>tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName"><span>涉及人数：</span></label>
											<input type="text" style="height:28px;"  class="inp1 easyui-numberbox w300" data-options="min:0,max:999,tipPosition:'bottom'" name="partyNum" id="partyNum" value="${reportFocus.partyNum?c}"/>（人）
										</td>
									</tr>
									<#if reportFocus.dataSource == '02'||reportFocus.dataSource == '01'>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><label class="Asterik">*</label>上访类型：</span></label>
											<input type="hidden" id="petitionTypes" name="petitionTypes" value="${reportFocus.petitionTypes!}"/>
											<input type="text" name="petitionTypesName" id="petitionTypesName" value="${reportFocus.petitionTypesName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
									</tr>
									<#if reportFocus.dataSource == '02'>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>信访动态：</span></label><textarea onblur="_capDynamicContent($(this).val());" name="petitionTrend" id="petitionTrend" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.petitionTrend!'描述购买出行车票或串联等信息'}</textarea>
										</td>
									</tr>
									</#if>
									</#if>
									<#if reportFocus.dataSource == '03'>
									<tr>
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>稳控类型：</span></label>
											<input type="hidden" id="controlType" name="controlType" value="${reportFocus.controlType!}"/>
											<input type="text" name="controlTypeName" id="controlTypeName" value="${reportFocus.controlTypeName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
										<td class="LeftTd">
											<label class="LabName"><span><label class="Asterik">*</label>人员类型：</span></label>
											<input type="hidden" id="petitionPartyType" name="petitionPartyType" value="${reportFocus.petitionPartyType!}"/>
											<input type="text" name="petitionPartyTypeName" id="petitionPartyTypeName" value="${reportFocus.petitionPartyTypeName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
										</td>
									</tr>
									</#if>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span>信访事项：</span></label><textarea name="itemRemark" id="itemRemark" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.itemRemark!}</textarea>
										</td>
									</tr>
									<tr>
										<td class="LeftTd" colspan="2">
											<label class="LabName"><span><#if reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>发生地址：</span></label>
											<input type="text" class="inp1 easyui-validatebox autoWidth" data-options="tipPosition:'bottom',<#if reportFocus.dataSource == '01'>required:true,</#if>validType:['maxLength[1500]','characterCheck']" name="occurred" id="occurred" value="${reportFocus.occurred!}" />
										</td>
										<td class="LeftTd hide">
											<label class="LabName labelNameWide"><span>地理标注：</span></label>
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</td>
									</tr>
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
											<label class="LabName"><span><#if reportFocus.dataSource == '01'><label class="Asterik">*</label></#if>图片上传：</span></label><div id="bigFileUploadDiv"></div>
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
					module			: 'petitionPerson',
					attachmentData	: {bizId: reportId, attachmentType:'PETITION_PERSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				},
				attachFileUploadOpt = {
					useType			: 'edit',
					fileExt			: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
					module			: 'petitionPerson',
					attachmentData	: {bizId: reportId, attachmentType:'PETITION_PERSON', eventSeq: '1,2,3', isBindBizId: 'yes'},
					individualOpt 	: {
						isUploadHandlingPic : true
					}
				};
			
			$('#ppDetailForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.90);
			});
			
			init4Location('occurred');
			init4Location_iResidenceAddr('iResidenceAddr_');
			
			var infoOrgCode_ = '';
			if($("#dataSource").val()!='01') {
				infoOrgCode_ = '350583';
			}
			
			$('#partyName').residentSelector({
				panelHeight : 300,
				panelWidth 	: 550,
				dataDomain 	: '${RS_DOMAIN!}',
				type 		: 'v6resident',
				relCode		: 'T',//户籍人口
				srchType	: '002',//模糊查找
				editable    : true,
				value		: {
					value	: "<#if reportFocus.partyId??>${reportFocus.partyId?c}</#if>",
					text	: "${reportFocus.partyName_!}"
				},
				onLoadSuccess : function(){
					if($("#isFirstInit").val()=='1'){
						detailFormJsonObject = $('#ppDetailForm').serializeArray();
						$("#isFirstInit").val('');
					}
				},
				onClickRow 	: function(index, row) {
					$('#partyName').val(row.partyName);
					$("#partyName_").val(row.partyName);
					$('#partyId').val(row.partyId);
					$('#certType').val(row.certType);
					$('#certTypeName').val(row.certTypeCN);
					$('#identityCard').val(row.identityCard);
					$('#identityCard_').val(row.identityCard);
					
					onchange4CardType(row.certType);
					
					initParty(row.partyId);
					_capDynamicContent();
				},
				onBulrCB	: function(value) {
					$('#certTypeName').removeAttr('disabled');//
					$('#identityCard_').removeAttr('disabled');
					$('#iResidenceAddr_').removeAttr('disabled');
					$('#partyTypeName').removeAttr('disabled');//
					
					$('#partyName_').val(value);
					$('#partyName').val(value);
					if($('#partyId').val()!=null&&$('#partyId').val()!=''){
						$('#partyId').val('');
						$('#certType').val('');
						$('#certTypeName').val('');
						$("#identityCard").val('');
						$("#identityCard_").val('');
						
						onchange4CardType('');
						
						$('#iResidenceAddr').val('');
						$('#iResidenceAddr_').val('');
						$('#residenceAddrNo').val('');
						$('#partyType').val('');
						$('#partyTypeName').val('');
						
						$('#x_iResidenceAddr').val('');
						$('#y_iResidenceAddr').val('');
						$('#mapt_iResidenceAddr').val('');
						$("#mapTab2_iResidenceAddr").html('标注地理位置');
					}
					
					var dataSource = $("#dataSource").val();
					if(dataSource=='02'){
						$('#iResidenceAddr_').validatebox({
							required: true
						});
						$('#certTypeName').validatebox({
							required: true
						});
						$('#partyTypeName').validatebox({
							required: true
						});
					}
					
					_capDynamicContent();
				},
				afterClear	: function(householder, partyId) {
					$('#certTypeName').removeAttr('disabled');//
					$('#identityCard_').removeAttr('disabled');
					$('#iResidenceAddr_').removeAttr('disabled');
					$('#partyTypeName').removeAttr('disabled');//
				
					$('#partyId').val('');
					$('#partyName_').val('');
					$('#partyName').val('');
					$('#certType').val('');
					$('#certTypeName').val('');
					$("#identityCard").val('');
					$("#identityCard_").val('');
					
					onchange4CardType('');
					
					$('#iResidenceAddr').val('');
					$('#iResidenceAddr_').val('');
					$('#residenceAddrNo').val('');
					$('#partyType').val('');
					$('#partyTypeName').val('');
					
					$('#x_iResidenceAddr').val('');
					$('#y_iResidenceAddr').val('');
					$('#mapt_iResidenceAddr').val('');
					$("#mapTab2_iResidenceAddr").html('标注地理位置');
					
					var dataSource = $("#dataSource").val();
					if(dataSource=='02'){
						$('#iResidenceAddr_').validatebox({
							required: true
						});
						$('#certTypeName').validatebox({
							required: true
						});
						$('#partyTypeName').validatebox({
							required: true
						});
					}
					
					_capDynamicContent();
				}
			},{'infoOrgCode':infoOrgCode_});
			
			var partyId = $('#partyId').val();
			if(partyId!=null&&partyId!=''){
				$('#certTypeName').attr('disabled',"disabled");//
				$('#identityCard_').attr('disabled',"disabled");
				$('#iResidenceAddr_').attr('disabled',"disabled");
				$('#partyTypeName').attr('disabled',"disabled");//
			}
			
			//加载网格
			AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							$("#regionCode").val(items[0].orgCode);
							$("input[name='nextNode']").each(function(){
							    if($(this).attr('nodename')=='task2'
								    ||$(this).attr('nodename')=='task5'
								    ||$(this).attr('nodename')=='task6'){
							    	$(this).click();
							    	return false;
							    }
							});
						}
					},
					{
						ChooseType:"1",
						ShowOptions:{
							<#if reportFocus.dataSource?? && reportFocus.dataSource!='01'>
								//来源是非网格员只需要选择到街镇层级
								AllowSelectLevel:"4,5,6"//选择到哪个层级
							<#else >
								AllowSelectLevel:"6"//选择到哪个层级
							</#if>
						}
					}
			);
			
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
			
			<#if reportFocus.resMarker??>
				var resMarkerX = "${reportFocus.resMarker.x!}",
					resMarkerY = "${reportFocus.resMarker.y!}",
					resMarkerMapType = "${reportFocus.resMarker.mapType!}";
				
				if(resMarkerX && resMarkerY && resMarkerMapType) {
					callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
				}
			</#if>
			
			<#if reportFocus.resMarker_iResidenceAddr??>
				var resMarkerX = "${reportFocus.resMarker_iResidenceAddr.x!}",
					resMarkerY = "${reportFocus.resMarker_iResidenceAddr.y!}",
					resMarkerMapType = "${reportFocus.resMarker_iResidenceAddr.mapType!}";
				
				if(resMarkerX && resMarkerY && resMarkerMapType) {
					callBackOfData_iResidenceAddr(resMarkerX, resMarkerY, null, resMarkerMapType);
				}
			</#if>
			
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
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

			AnoleApi.initTreeComboBox("certTypeName", "certType", "D030001", null, ["${reportFocus.certType!''}"], {
				ShowOptions: {
					EnableToolbar : false
				},
				OnChanged: function(value, items) {
					onchange4CardType(value, items, true);
				},
			});
			
			AnoleApi.initTreeComboBox("partyTypeName", "partyType", "D069003", null, ["${reportFocus.partyType!''}"], {
				ShowOptions: {
					EnableToolbar : false
				},
				OnChanged: function(value) {
					
				},
			});
			
			if($("#dataSource").val()=='02'||$("#dataSource").val()=='01') {
				var petitionTypesArray = [], petitionTypes = "${reportFocus.petitionTypes!}";
				if(petitionTypes) {
					petitionTypesArray = petitionTypes.split(",");
				}
				AnoleApi.initTreeComboBox("petitionTypesName", "petitionTypes", "A001130002", null, petitionTypesArray, {
					RenderType : "01",
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						_capDynamicContent();
					},
				});
			}
			if($("#dataSource").val()=='03') {
				AnoleApi.initTreeComboBox("controlTypeName", "controlType", "B210012002", null, ["${reportFocus.controlType!''}"], {
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						_capDynamicContent();
					},
				});
				
				AnoleApi.initTreeComboBox("petitionPartyTypeName", "petitionPartyType", "B210012003", null, ["${reportFocus.petitionPartyType!''}"], {
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						_capDynamicContent();
					},
				});
			}
			
			onchange4CardType($('#certType').val());
			
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
			
			detailFormJsonObject = $('#ppDetailForm').serializeArray();
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
		});
		
		function onchange4CardType(value, items, isClear) {
			if(value == '111') {
				$('#identityCard_').validatebox({
					validType:'idcard'
				});
			} else {
				$('#identityCard_').validatebox({
					validType:['maxLength[50]','characterCheck']
				});
			}
		}
		
		function initParty(partyId){
			$.ajax({   
				 url: '${rc.getContextPath()}/zhsq/reportPetPer/getPartyInfo.json?partyId='+partyId+'&t='+Math.random(),
				 type: 'POST',
				 timeout: 3000,
				 dataType:"json",
				 error: function(data){
				 	$.messager.alert('友情提示','人员信息获取失败!','warning'); 
				 },
				 success: function(data) {
				 	if(data.result=='1'){
				 		var ciRs = data.ciRs;
				 		$('#iResidenceAddr').val(ciRs.residenceAddr);
				 		$('#iResidenceAddr_').val(ciRs.residenceAddr);
						$('#residenceAddrNo').val(ciRs.residenceAddrNo);
						$('#partyType').val(ciRs.type);
						$('#partyTypeName').val(ciRs.typeCN);
						
						$('#x_iResidenceAddr').val(ciRs.longitude);
						$('#y_iResidenceAddr').val(ciRs.latitude);
						$('#mapt_iResidenceAddr').val('5');
						$("#mapTab2_iResidenceAddr").html('标注地理位置');
						
						var dataSource = $("#dataSource").val();
						if(dataSource=='02'){
							$('#iResidenceAddr_').validatebox({
								required: false
							});
							$('#certTypeName').validatebox({
								required: false
							});
							$('#partyTypeName').validatebox({
								required: false
							});
						}
				 	}else{
					 	$('#iResidenceAddr').val('');
					 	$('#iResidenceAddr_').val('');
						$('#residenceAddrNo').val('');
						$('#partyType').val('');
						$('#partyTypeName').val('');
						
						$('#x_iResidenceAddr').val('');
						$('#y_iResidenceAddr').val('');
						$('#mapt_iResidenceAddr').val('');
						$("#mapTab2_iResidenceAddr").html('标注地理位置');
				 	}
				 }
			});
		}
		
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
			//_itemSizeAdjust();
			
			if($("#isFirstInit").val()=='1'){
				detailFormJsonObject = $('#ppDetailForm').serializeArray();
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
				nextNodeName = $('#nodeName_').val(),
				formTypeId = '${formTypeId!}';
			
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
			
			if(formTypeId=='362'&&curNodeName=='task1'){
				_capDynamicContent();
			}
		}
	
		function _subTaskOperate() {
			var isValid =  $("#flowSaveForm").form('validate');
			var dataSource = $('#dataSource').val();
			if(isValid && checkAttachmentStatus4BigFileUpload('bigFileUploadDiv')&&'01'==dataSource) {
				isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
			}
			isValid = checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');
			if(isValid) {
				isValid = $("#ppDetailForm").form('validate');
				if(isValid) {
					var residenceAddrNo = $("#residenceAddrNo").val();
					if(residenceAddrNo==null||residenceAddrNo==''){
						residenceAddrNo = $("#regionCode").val();
					}
					if(formCompare4PP() == false) {
						modleopen();
						$("#ppDetailForm").attr("action", "${rc.getContextPath()}/zhsq/reportPetPer/saveReportFocus.jhtml");
						$("#ppDetailForm").ajaxSubmit(function(data) {
							modleclose();
							if(data.success && data.success == true) {
								/*
								var checkedRadio = $('#tr_epath input[type=radio][name=nextNode]:checked').eq(0);
								if(checkedRadio.length > 0) {
									BaseWorkflowNodeHandle.checkRadio(checkedRadio.eq(0));
								}*/
								var isRegionNotChanged = specificFormAttrCompare(detailFormJsonObject, $('#ppDetailForm').serializeArray(), {'regionCode' : ''}),
									compareFlag = specificFormCompare4PP();
								
								if(isRegionNotChanged == true && compareFlag == true) {
									if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
										basWorkSubTaskCallback();
									}
								} else {
									var checkedRadio = $('#tr_epath input[type=radio][name=nextNode]:checked').eq(0),
										isClearNextUser = false;
									
									if(isRegionNotChanged === false) {
										//isClearNextUser = true;
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
					} else if(basWorkSubTaskCallback != null && typeof basWorkSubTaskCallback === 'function') {
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
				
				if(formTypeId=='362'){
					advice = toDo_capDynamicContent('@partyName_@',adviceNote,'partyName_',advice);
					advice = toDo_capDynamicContent('@controlTypeName@',adviceNote,'controlTypeName',advice);
					advice = toDo_capDynamicContent('@petitionPartyTypeName@',adviceNote,'petitionPartyTypeName',advice);
					advice = toDo_capDynamicContent('@petitionTypesName@',adviceNote,'petitionTypesName',advice);
					
					if(adviceNote.indexOf('@petitionTrend@') >= 0) {
						advice = advice.replaceAll('@petitionTrend@', $("#petitionTrend").val());
					}
					
					if(advice.indexOf('@') >= 0) {
						advice = '';
					}
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
				nextNodeName = $('#nodeName_').val(),
				formTypeId = $('#formTypeId').val(),
				dataSource = $('#dataSource').val(),
				isEditableNode = $('#isEditableNode').val(),
				isRequired = false;
			
			if((formTypeId == '358' && curNodeName == 'task3' && nextNodeName == 'task4') || (isEditableNode == 'false' && dataSource != '05')) {
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
		
		function set_identityCard(value){
			$("#identityCard").val(value);
		}
		
		function formCompare4PP() {
			var isForce2Save = $('#isForce2Save').val();
			var compareFlag = true;
			
			if(isForce2Save === 'false') {
				var detailFormJsonObjectNow = $('#ppDetailForm').serializeArray();
				
				compareFlag = formAttrCompare(detailFormJsonObject, detailFormJsonObjectNow);
			} else {
				compareFlag = false;
				$('#isForce2Save').val('false');
			}
			
			return compareFlag;
		}
		
		function specificFormCompare4PP() {
			var detailFormJsonObjectNow = $('#ppDetailForm').serializeArray(),
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
	</script>
	
	<#include "/component/ComboBox.ftl" />
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
	<#include "/zzgl/reportFocus/petitionPerson/detail_base.ftl" />
</html>