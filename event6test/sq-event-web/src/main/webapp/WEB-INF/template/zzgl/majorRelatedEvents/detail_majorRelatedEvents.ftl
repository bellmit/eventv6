<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>涉事案件详情</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<style>
.LabName span{padding-right: 5px;}
</style>
</head>

<body>
	<div id="content-d" class="MC_con content light">
		<input type="hidden" id="id" name="id" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="2" />
		<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
		<div class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="2">
						<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.reNo??>${relatedEvents.reNo}</#if></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>案件名称：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:480px;"><#if relatedEvents.reName??>${relatedEvents.reName}</#if></div>
					</td>
				</tr>
				<tr>
					<td class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>发生日期：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if relatedEvents.occuDateStr??>${relatedEvents.occuDateStr}</#if></div>
					</td>
					<td class="LeftTd" style="width:334px;">
						<label class="LabName"><span><font color="red">*</font>所属网格：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:63%;"><#if relatedEvents.gridPath??>${relatedEvents.gridPath}</#if></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>发生地详址：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:480px;"><#if relatedEvents.occuAddr??>${relatedEvents.occuAddr}</#if></div>
					</td>
				</tr>
				<#if !(showType?? && showType == 'map')>
				<tr>
		    		<td colspan="2" class="LeftTd">
		    			<label class="LabName"><span>地理标注：</span></label>
		    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
		        	</td>
		        </tr>
		        </#if>
		    	<tr>
					<td class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>案件类型：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if relatedEvents.eventTypeCN??>${relatedEvents.eventTypeCN}</#if></div>
			    	</td>
			    	<td class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>案件分级：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if relatedEvents.eventLevelCN??>${relatedEvents.eventLevelCN}</#if></div>
			    	</td>
			    </tr>
		    	<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>案件情况：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:480px;"><#if relatedEvents.situation??>${relatedEvents.situation}</#if></div>
					</td>
				</tr>
		    </table>
		</div>
	</div>
	
    <div class="BigTool">
    	<div class="BtnList">
      		<a href="#" class="BigNorToolBtn CancelBtn" onclick="closeWin();">关闭</a>
      	</div>
    </div>
</body>

<script type="text/javascript">
	function closeWin(){
		parent.closeMaxJqueryWindow();
	}
	
	$(function() {
		$(window).load(function(){ 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	    });
	});
	
	function showMap() {
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = $(window).width() - 15;
		var height = $(window).height() - 15;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var mapType = $("#module").val();
		var isEdit = false;
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
