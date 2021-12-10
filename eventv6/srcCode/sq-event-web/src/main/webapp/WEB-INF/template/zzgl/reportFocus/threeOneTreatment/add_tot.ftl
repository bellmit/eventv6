<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>三合一整治-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/bigFileUpload.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
			.squareMeterUnit{width: 65px;}
		</style>
	</head>
	<body>
		<form id="totForm" name="totForm" action="" method="post">
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
			<input type="hidden" id="module" value="THREE_ONE_TREATMENT"/>
			<#--信息采集来源-->
			<input type="hidden" id="dataSource" name="dataSource" value="${reportFocus.dataSource!}"/>
			<input type="hidden" name="collectSource" value="${reportFocus.collectSource!}"/>
			<#--报告方式-->
			<input name="reportWay" type="hidden" value="${reportFocus.reportWay!'2'}" />
							
			<div id="content-d" class="MC_con content light">
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
									<span>报告人电话：</span>
								</label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reporterTel!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName">
									<span><label class="Asterik">*</label>发现渠道：</span>
								</label>
								<input  id="dataSourceName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;"
										data-options="required:true,tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide">
									<span>报告时间：</span>
								</label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportTimeStr!''}</div>
								<input type="hidden" id="reportTime" name="reportTime" value="${reportFocus.reportTimeStr!}"></input>
							</td>
						</tr>
						<tr id="regionTimeTr">
							<td class="LeftTd" id="regionCodeTr">
								<label class="LabName ">
									<span><label class="Asterik">*</label>所属区域：</span>
								</label>
								<input type="hidden" id="regionLevel" name="regionLevel" />
								<input type="hidden" id="regionCode" name="regionCode" value="${reportFocus.regionCode!}" />
								<input type="text" id="gridName" style="width: 300px;" value="${reportFocus.regionName!}"
									   class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom'"/>
							</td>
							<td class="LeftTd hide" id="feedbackTimeTd">
								<label class="LabName labelNameWide">
									<span><label class="Asterik">*</label>反馈时限：</span>
								</label>
								<input type="text" id="feedbackTime" name="feedbackTime" class="inp1 Wdate easyui-validatebox singleCellClass"
									   data-options="tipPosition:'bottom'" style="width:170px; cursor:pointer;"
									   onclick="WdatePicker({readOnly:true, dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:true, isShowToday:true})"
									   value="${reportFocus.feedbackTimeStr!}" readonly="readonly"></input>
							</td>
							<td class="LeftTd hide" id="discoveryStaffTd">
								<label class="LabName labelNameWide">
									<span><label class="Asterik">*</label>发现部门：</span>
								</label>
								<input  id="discoveryStaff" name="discoveryStaff" type="text" class="inp1 easyui-validatebox singleCellInpClass"
										style="width: 280px;" data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']"
										value="${reportFocus.discoveryStaff!}" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName">
									<span>
										<label id="occurredAsterik" <#if reportFocus.dataSource == '05'>style="display: none"</#if> class="Asterik autoRequiredAsterik">*</label>发生地址：
									</span>
								</label>
								<input type="text" class="inp1 easyui-validatebox autoWidth"
									   data-options="tipPosition:'bottom',validType:['maxLength[1500]','characterCheck']"
									   name="occurred" id="occurred" value="${reportFocus.occurred!}" />
							</td>
							<td class="LeftTd hide">
								<label class="LabName labelNameWide"><span>地理标注：</span></label>
								<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName">
									<span><label id="personInvolvedLabel" class="Asterik hide">*</label>业主姓名：</span>
								</label>
								<input  id="personInvolved" name="personInvolved" type="text" class="inp1 easyui-validatebox singleCellInpClass"
										style="width: 300px;" data-options="tipPosition:'bottom',validType:['maxLength[250]','characterCheck']"
										value="${reportFocus.personInvolved!}" />
							</td>
							<td class="LeftTd ">
								<label class="LabName labelNameWide">
									<span><label id="siteAreaLabel" class="Asterik hide">*</label>场所面积：</span>
								</label>
								<input  id="siteArea" name="siteArea" type="text" class="inp1 easyui-numberbox singleCellInpClass" style="width: 210px; height: 28px;"
										data-options="tipPosition:'bottom',max:99999999.99,precision:2" value="${reportFocus.siteArea!}" />
								<label class="LabName squareMeterUnit" style="float: none; display: inline-block; margin-left: 0;">（平方米）</label>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>隐患类型：</span></label>
								<input type="hidden" id="hiddenDangerType" name="hiddenDangerType" value="${reportFocus.hiddenDangerType!}" />
								<input  id="hiddenDangerTypeName" type="text" class="inp1 easyui-validatebox singleCellInpClass" style="width: 300px;"
										data-options="tipPosition:'bottom',validType:['maxLength[50]','characterCheck']" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide">
									<span>报告方式：</span>
								</label>
								<div class="Check_Radio FontDarkBlue">${reportFocus.reportWayName!'办公操作平台'}</div>
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
								<label class="LabName"><span>备注：</span></label><textarea name="remark" id="remark" cols="" rows="" class="area1 easyui-validatebox autoWidth" style="height:64px;resize: none;" data-options="tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${reportFocus.remark!}</textarea>
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
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="saveTOT(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveTOT(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
		
		<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		var allowSelectLevel = null;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
			var dataSource = "${reportFocus.dataSource}";
			
			//放置在组件初始化之前，以防止组件初始化后导致结构调整，从而影响宽度计算
			$('#totForm .autoWidth').each(function() {
				$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
			});
			
			init4Location('occurred', {
				<#if reportFocus.dataSource?? && reportFocus.dataSource == '01'>
					//来源是非网格员只需要选择到村社区层级
					_limit_select_level : 6//选择到哪个层级
				</#if>
			});

			//加载网格
			window.gridTreeObj = AnoleApi.initGridZtreeComboBox("gridName", null,
					function(gridId, items) {
						if (items && items.length > 0) {
							$("#regionLevel").val(items[0].gridLevel);
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
			//是否隐藏所属区域所在行
			if(isBlankStringTrim(dataSource)){
				$('#regionTimeTr').hide();
				$('#regionCodeTr').hide();
			}else if(dataSource == '05'){
				$('#tipoffContentTr').show();
				$('#tipoffContent').validatebox({required:true});
			}else if(dataSource == '03'){
				//泉州市级相关部门督查（检查）发现 发现部门
				$('#regionTimeTr').show();
				$('#discoveryStaffTd').show();
				$('#discoveryStaff').validatebox({required:true});
			}
			<#if reportFocus.resMarker??>
				var resMarkerX = "${reportFocus.resMarker.x!}",
					resMarkerY = "${reportFocus.resMarker.y!}",
					resMarkerMapType = "${reportFocus.resMarker.mapType!}";
				
				if(resMarkerX && resMarkerY && resMarkerMapType) {
					callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
				}
			</#if>
		
			var bigFileUploadOpt = {
				useType: 'add',
				fileExt: '.jpg,.gif,.png,.jpeg,.webp',
				attachmentData: {attachmentType:'THREE_ONE_TREATMENT'},
				module: 'threeOneTreatment',
				individualOpt: {
					isUploadHandlingPic: true
				}
			},
			reportId = $('#reportId').val();
			
			var bigViodeUploadOpt = {	
					useType: 'add',
					fileExt: '.mp4,.avi,.amr',
					showFileExt: '.mp4,.avi,.amr',
					labelDict: [{'name':'处理前', 'value':'1'}],
					attachmentData: {attachmentType:'THREE_ONE_TREATMENT'},
					module: 'threeOneTreatment',
					individualOpt: {
						isUploadHandlingPic: true
					}
				};

			var attachFileUploadOpt = {
				useType: 'add',
				fileExt: '.zip,.rar,.doc,.docx,.xlsx,xls,.text,.ppt,.pptx,.mp3',
				labelDict: [{'name':'处理前', 'value':'1'}],
				attachmentData: {attachmentType:'THREE_ONE_TREATMENT'},
				module: 'threeOneTreatment',
				individualOpt: {
					isUploadHandlingPic: true
				}
			};
			
			if(reportId) {
				$.extend(bigFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(bigFileUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1,2,3',
					bizId: reportId,
					isBindBizId: 'yes'
				});
				
				$.extend(bigViodeUploadOpt, {
					useType: 'edit'
				});
				$.extend(bigViodeUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1',
					bizId: reportId,
					isBindBizId: 'yes'
				});

				$.extend(attachFileUploadOpt, {
					useType: 'edit'
				});
				$.extend(attachFileUploadOpt.attachmentData, {
					useType: 'edit',
					eventSeq: '1',
					bizId: reportId,
					isBindBizId: 'yes'
				});
			}
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			bigFileUpload_initFileUploadDiv('bigVideoUploadDiv', bigViodeUploadOpt);
			bigFileUpload_initFileUploadDiv('attachFileUploadDiv', attachFileUploadOpt);

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
				["${reportFocus.hiddenDangerType!''}"]);
			}
			
			//发现渠道
			if($('#dataSourceName').length > 0) {
				AnoleApi.initTreeComboBox("dataSourceName", "dataSource", "B210015001", null, ["${reportFocus.dataSource!}"], {
					ShowOptions: {
						EnableToolbar : true
					},
					OnChanged : function(value, items) {
						 onchange4DataSource(value, items);
					},
					FilterData:function(data) {
						var resuleData = [];
						var notGridSource = '03,04,05';
						if(data != null && data.length >0){
							if(dataSource == '01' || dataSource == '02'){
								for(var i=0;i<data.length;i++){
									if(dataSource == data[i].value){
										resuleData.push(data[i]);
									}
								}
							}else if(notGridSource.indexOf(dataSource)>=0){
								for(var i=0;i<data.length;i++){
									if(notGridSource.indexOf(data[i].value) >= 0){
										resuleData.push(data[i]);
									}
								}
							}
						}
						return resuleData;
					}
				});
			}

			var options = {
				axis : "yx",
				theme : "minimal-dark" 
			};
			
			enableScrollBar('content-d',options);
		});
		
		function saveTOT(btnType) {
			var isValid =  $("#totForm").form('validate'),
				longitude = $('#x').val(),
				latitude = $('#y').val(),
				occurred = $('#occurred').val(),
				mapType = $('#mapt').val(),
				dataSource = $('#dataSource').val();

			if(isValid && isNotBlankStringTrim(dataSource) && dataSource == '01'){
				isValid = checkAttachment4BigFileUpload(1, $('#bigFileUploadDiv div[file-status="complete"]'));
			}

			if(isValid) {
				if($('#occurredAsterik').is(':visible')) {
					if(isBlankStringTrim(occurred)){
						$.messager.alert('警告', "请选择发生地址！", 'warning');
						return;
					}
					if(isBlankStringTrim(longitude) && isBlankStringTrim(latitude) && isBlankStringTrim(mapType)){
						$.messager.alert('警告', "请完成地理标注！", 'warning');
						return;
					}
				}
			}
			//所属区域可选择层级
			allowSelectLevel = gridTreeObj.settings.ShowOptions.AllowSelectLevel;
			//所属区域已选择数据所属层级
			var regionLevel = $('#regionLevel').val() || '6';
			var dataSource = $('#dataSource').val() || '01';
			if(dataSource == '01' && allowSelectLevel != regionLevel){
				$.messager.alert('警告', "所属区域请精确到网格层级！", 'warning');
				return;
			}

			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#totForm").attr("action", "${rc.getContextPath()}/zhsq/reportTOT/saveReportFocus.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#totForm").ajaxSubmit(function(data) {
					modleclose();
					
					if(data.success && data.success == true) {
						if(isStart) {
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
				mapType = 'THREE_ONE_TREATMENT',
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
				
				$('#totForm .MC_con').height(winHeight - $('#totForm .BigTool').outerHeight());
				$('#totForm .autoWidth').each(function() {
					$(this).width((_winWidth - $(this).siblings().eq(0).outerWidth(true)) * 0.95);
				});
			}
		});
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
			/*群众举报发现 发生地址非必填*/
			if(value == '05'){
				$('#occurredAsterik').hide();
				/*$('#x').val(''),
				$('#y').val(''),
				$('#occurred').val(''),
				$('#mapt').val('')*/
			}else{
				$('#occurredAsterik').show();
			}
			//泉州市级相关部门督查（检查）发现 发现部门
			if(value == '03'){
				$('#regionTimeTr').show();
				$('#discoveryStaffTd').show();
				$('#discoveryStaff').validatebox({required:true});
			}else{
				$('#discoveryStaff').val('');
				$('#discoveryStaffTd').hide();
				$('#discoveryStaff').validatebox({required:false});
			}
			//动态变更所属区域可选择层级
			//网格巡查发现 选择到网格层级
			if(value == '01'){
				gridTreeObj.settings.ShowOptions.AllowSelectLevel = '6';
			}else{
				gridTreeObj.settings.ShowOptions.AllowSelectLevel = '5,6';
			}
			if(value == '05'){
				$('#tipoffContentTr').show();
				$('#tipoffContent').validatebox({required:true});
			}else{
				$('#tipoffContent').val('');
				$('#tipoffContentTr').hide();
				$('#tipoffContent').validatebox({required:false});
			}

			$('#regionTimeTr').show();
			$('#regionCodeTr').show();
		}
	</script>
	
	<#include "/zzgl/reportFocus/base/add_base.ftl" />
</html>