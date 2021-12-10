<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及业务案件采集事件</title>
<#include "/component/standard_common_files-1.1.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<#include "/component/FieldCfg.ftl" />
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.datebox .combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/js/jquery-easyui-1.4/themes/gray/images/datebox_arrow.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
.LabName span{padding-right: 5px;}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="id" name="id" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if relatedEvents.reId??>1<#else>0</#if>" />
		<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
		<input type="hidden" id="reId" name="reId" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="bizType" name="bizType" value="<#if relatedEvents.bizType??>${relatedEvents.bizType}</#if>" />
		<input type="hidden" id="bizId" name="bizId" value="<#if relatedEvents.bizId??>${relatedEvents.bizId?c}</#if>" />
		<input type="hidden" id="bizName" value="" />
		<input type="hidden" id="isDetection" name="isDetection" value="<#if relatedEvents.isDetection??>${relatedEvents.isDetection}<#else>0</#if>" />
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<#if relatedEvents.reNo??>
						<tr>
							<td colspan="2">
								<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue">${relatedEvents.reNo}</div>
							</td>
						</tr>
					</#if>
					<tr>
						<td colspan="2" class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>案件名称：</span></label><input type="text" class="inp1 easyui-validatebox"
							data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" name="reName" id="reName" value="<#if relatedEvents.reName??>${relatedEvents.reName}</#if>" style="width:476px;" />
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>发生日期：</span></label><input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" style="width: 155px;height: 28px;" id="occuDateStr" name="occuDateStr" value="<#if relatedEvents.occuDateStr??>${relatedEvents.occuDateStr}</#if>"/>
						</td>
						<td class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>所属网格：</span></label>
							<input type="hidden" id="gridCode" name="gridCode" value="<#if relatedEvents.gridCode??>${relatedEvents.gridCode}</#if>" />
							<input type="text" class="inp1" style="width: 155px;" id="gridName" value="<#if relatedEvents.gridName??>${relatedEvents.gridName}</#if>" />
						</td>
					</tr>
					<tr>
						<td colspan="2" class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>发生地详址：</span></label><input type="text" class="inp1 easyui-validatebox"
							data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" name="occuAddr" id="occuAddr" value="<#if relatedEvents.occuAddr??>${relatedEvents.occuAddr}</#if>" style="width:476px;" />
						</td>
					</tr>
					<tr>
			    		<td colspan="2" class="LeftTd">
			    			<label class="LabName"><span>地理标注：</span></label>
			    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
			        	</td>
			        </tr>
			    	<tr>
						<td class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>案件类型：</span></label>
							<input type="hidden" id="eventType" name="eventType" value="<#if relatedEvents.eventType??>${relatedEvents.eventType}</#if>" />
							<input type="text" class="inp1 easyui-validatebox" style="width: 155px;" id="eventTypeName" 
							data-options="tipPosition:'right',required:true" value="<#if relatedEvents.eventTypeCN??>${relatedEvents.eventTypeCN}</#if>" />
				    	</td>
				    	<td class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>案件分级：</span></label>
							<input id="eventLevelName" type="text" class="inp1 easyui-validatebox" style="width:155px;" data-options="tipPosition:'left', required:true" value="<#if relatedEvents.eventLevelCN??>${relatedEvents.eventLevelCN}</#if>"/>
							<input type="hidden" id="eventLevel" name="eventLevel" value="<#if relatedEvents.eventLevel??>${relatedEvents.eventLevel}</#if>" />
				    	</td>
				    </tr>
			    	<tr>
						<td colspan="2" class="LeftTd">
							<label class="LabName"><span><font color="red">*</font>案件情况：</span></label><textarea name="situation" id="situation" cols="" rows="" class="area1 easyui-validatebox"
							data-options="tipPosition:'top',required:true,validType:['maxLength[4000]','characterCheck']" style="width:476px;height:100px;resize:none;"><#if relatedEvents.situation??>${relatedEvents.situation}</#if></textarea>
						</td>
					</tr>
			    </table>
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="#" onclick="tableSubmit('saveRelatedEvents');" class="BigNorToolBtn BigJieAnBtn">保存</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
	</form>
	
	<#include "/component/ComboBox.ftl" />
	
</body>

<script type="text/javascript">
	var queryValue = "";
	var lotUrl = "${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/listCareRoads.jhtml?lotName=";
	
	$(function(){
	    $(window).load(function() {
	        var options = {
	            axis : "yx",
	            theme : "minimal-dark"
	        };
	        enableScrollBar('content-d',options);
	    });
		AnoleApi.initGridZtreeComboBox("gridName", null, function(val, items) {
			if (items && items.length > 0) {
				$("#gridCode").val(items[0].orgCode);
			}
		});
        AnoleApi.initListComboBox("eventTypeName", "eventType", "${eventTypeDC}", null, [<#if relatedEvents.eventType??>"${relatedEvents.eventType}"</#if>]);
        AnoleApi.initListComboBox("eventLevelName", "eventLevel", "${eventLevelDC}", null, [<#if relatedEvents.eventLevel??>"${relatedEvents.eventLevel}"</#if>]);
		layer.load(0);// 加载遮罩层
		$.excuteFieldCfg({
			moduleCode: "major_related_events"// 必传，模块编码
		}, function(isSuccess, msg) {// 回调函数，isSuccess：true成功/false失败
			layer.closeAll('loading'); // 关闭加载遮罩层
			if (!isSuccess) {
				$.messager.alert('警告', msg, 'warning');
			}
		});
	});
	
	function changeDetectionValue(){
		var isDetection = $("#isDetectionCheck").is(":checked");
		if(isDetection){
			$("#isDetection").val(1);
		}else{
			$("#isDetectionCheck").removeAttr("checked");
			$("#isDetection").val(0);
		}
	}
	
	function countCrimeNum(){
		var ecapeNum = $("#ecapeNum").val();
		var arrestedNum = $("#arrestedNum").val();
		var crimeNum = 0;
		
		if(ecapeNum == ''){
			ecapeNum = 0;
		}
		
		if(arrestedNum == ''){
			arrestedNum = 0;
		}
		
		crimeNum = parseInt(ecapeNum, 10) + parseInt(arrestedNum, 10);
		$("#crimeNum").val(crimeNum);
		$("#crimeNum").numberspinner('setValue', crimeNum);
	}
	
	function changeBiz(record){
		if(record != null){
			var lotId = record.lotId;
			var lotName = record.lotName;
			
			if(lotId!=undefined && lotId!=""){
				$("#bizId").val(lotId);
			}
			
			if(lotName!=undefined && lotName!=""){
				$("#bizName").val(lotName);
			}
		}
	}
	
	function checkExtraValidate(){//额外的字段检测
		var docType = $("#prisonersDocType").val();
		var bizId = $("#bizId").val();
		var flag = true;
		var msg = "";
		
		if(docType == '1'){
			var docNo = $("#prisonersDocNo").val(); 
			flag = checkIdCard(docNo);
			msg = "身份证号码不合法！";
		}
		
		if(!flag && msg!=""){
			$.messager.alert('警告',msg,'warning');
		}
		return flag;
	}
	
	function tableSubmit(m){
		var isValid =  $("#tableForm").form('validate');
		var isNoValid = checkExtraValidate();
		if(isValid && isNoValid){
			var msg = "新增";
			<#if relatedEvents.reId??>
				m = "editRelatedEvents";
				msg = "编辑";
			</#if>
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/"+m+".jhtml");
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
  				if(data.result){
  					msg += "成功！";
  				}else{
  					msg += "失败！";
  				}
  				parent.flashData(msg, "1");
			});
	  	}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function showMap() {
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = $(window).width() - 15;
		var height = $(window).height() - 15;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var mapType = $("#module").val();
		var isEdit = true;
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	
	function getParameterJson() {
		var parameterJson = {
			"id": $("#id").val(),
			"name": $("#name").val()
		}
		return parameterJson;
	}
</script>
</html>