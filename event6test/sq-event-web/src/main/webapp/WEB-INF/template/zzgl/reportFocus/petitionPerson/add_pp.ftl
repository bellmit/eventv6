<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>信访人员稳控-新增/编辑</title>
		<script>
			window.__enableEasyComp__ = false;
		</script>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/jquery.baseCombo.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/residentSelector.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
			.inputWidth{width: 170px;}
			.w300{width: 300px;}
		</style>
	</head>
	<body>
		<form id="ppForm" name="ppForm" action="" method="post">
			<input type="hidden" id="isStart" name="isStart" value="false" />
			<input type="hidden" id="isSaveAttrInfo" name="isSaveAttrInfo" value="true" />
			<input type="hidden" name="isSaveResMarkerInfo" value="true" />
			<input type="hidden" id="reportId" name="reportId" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" name="reportUUID" value="${reportFocus.reportUUID!}" />
			<input type="hidden" name="reportType" value="${reportType!}" />
			<!--用于地图-->
			<input type="hidden" id="id" name="id" value="<#if reportFocus.reportId??>${reportFocus.reportId?c}</#if>" />
			<input type="hidden" id="name" name="name" value="" />
			<input type="hidden" id="markerOperation" name="markerOperation" value="0"/>
			<input type="hidden" id="module" value="PETITION_PERSON"/>
			<input type="hidden" id="module_iResidenceAddr" value="PETITION_PERSON_IRESIDENCEADDR"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<#--报告方式-->
			<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
			<input name="userTel" type="hidden" value="${userTel!''}" />			
							
			<div id="content-d" class="MC_con content light">
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
								<input type="hidden" id="reportTime" name="reportTime" value="${reportFocus.reportTimeStr!}"></input>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>所属区域：</span></label>
								<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
								<input type="text" id="gridName" value="${reportFocus.regionName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>报告方式：</span></label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
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
													console.log('111111111111');
													console.log(api);
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
						<#if reportFocus.dataSource == '02' || reportFocus.dataSource == '01'>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span><label class="Asterik">*</label>上访类型：</span></label>
								<input type="hidden" id="petitionTypes" name="petitionTypes" value="${reportFocus.petitionTypes!}"/>
								<input type="text" name="petitionTypesName" id="petitionTypesName" value="${reportFocus.petitionTypesName!}" class="inp1 easyui-validatebox w300" data-options="required:true,tipPosition:'bottom'"/>
							</td>
						</tr>
						<#if reportFocus.dataSource=='02'>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>信访动态：</span></label><textarea name="petitionTrend" id="petitionTrend" cols="" rows="" class="area1 autoWidth easyui-validatebox" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']">${reportFocus.petitionTrend!'描述购买出行车票或串联等信息'}</textarea>
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
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="saveFFP(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveFFP(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
		
		<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			
			$('#ppForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
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
			
		
			var bigFileUploadOpt = {
				useType: 'add',
				fileExt: '.jpg,.gif,.png,.jpeg,.webp',
				attachmentData: {attachmentType:'PETITION_PERSON'},
				module: 'petitionPerson',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();

			var attachFileUploadOpt = {
						useType: 'add',
						fileExt: '.mp4,.avi,.amr,.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
						attachmentData: {attachmentType:'PETITION_PERSON'},
						module: 'petitionPerson',
						individualOpt: {
							isUploadHandlingPic: true
						}
					};
			
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
					}
				});
			}
			if($("#dataSource").val()=='03') {
				AnoleApi.initTreeComboBox("controlTypeName", "controlType", "B210012002", null, ["${reportFocus.controlType!''}"], {
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						
					},
				});
				
				AnoleApi.initTreeComboBox("petitionPartyTypeName", "petitionPartyType", "B210012003", null, ["${reportFocus.petitionPartyType!''}"], {
					ShowOptions: {
						EnableToolbar : false
					},
					OnChanged: function(value) {
						
					},
				});
			}
			
			onchange4CardType($('#certType').val());
			
			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
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
						
						//不可编辑
						$('#certTypeName').attr('disabled',"disabled");
						$('#identityCard_').attr('disabled',"disabled");
						$('#iResidenceAddr_').attr('disabled',"disabled");
						$('#partyTypeName').attr('disabled',"disabled");
						
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
		
		function saveFFP(btnType) {
			var isValid =  $("#ppForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				mapType = $('#mapt').val(),
				MASSES_REPORT_DATASOURCE = "01",
				dataSource = $('#dataSource').val();
			var partyName_ = $("#partyName_").val();
			
			console.log($("#ppForm").find(".validatebox-invalid"));
			
			if(isValid) {
				isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
				isValid = checkAttachmentStatus4BigFileUpload('attachFileUploadDiv');

				if(isValid && MASSES_REPORT_DATASOURCE == dataSource) {
					isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
				}
			}
			
			if(isValid) {
				if(partyName_==""||partyName_==null){
					$.messager.alert('提示', '姓名必填', 'info');
					return;
				} 
				var residenceAddrNo = $("#residenceAddrNo").val();
				if(residenceAddrNo==null||residenceAddrNo==''){
					residenceAddrNo = $("#regionCode").val();
				}
				modleopen();
				var isStart = btnType == 1;
				
				$("#ppForm").attr("action", "${rc.getContextPath()}/zhsq/reportPetPer/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#ppForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						if(isStart) {
							var	outerCallBack = "${reportFocus.callBack!}";
							if(outerCallBack){
								eval(outerCallBack)(data);
							}
							
							parent.searchData();
							parent.detail(data.reportUUID, data.instanceId, '2');
							if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
								parent.closeBeforeMaxJqueryWindow();
							}
						} else {
		  					parent.reloadDataForSubPage(data.tipMsg);
		  				}
					} else {
						$.messager.alert('错误', data.tipMsg, 'error');
					}
				});
			}
		}
		
		function showMap() {
			var callBackUrl = '${SQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml',
				width = 480, height = 360,
				gridId = "${defaultGridIdStr!'-1'}",
				markerOperation = $('#markerOperation').val(),
				id = $('#id').val(),
				mapType = 'PETITION_PERSON',
				isEdit = true,
				parameterJson = {
					'id' : $('#id').val(),
					'name' : $('#name').val()
				};
			
			showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
		}
	    
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height(), winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				_winHeight = winHeight;
				_winWidth = winWidth;
				
				$('#ppForm .MC_con').height(winHeight - $('#ppForm .BigTool').outerHeight());
				$('#ppForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
		
		function set_identityCard(value){
			$("#identityCard").val(value);
		}
		
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>