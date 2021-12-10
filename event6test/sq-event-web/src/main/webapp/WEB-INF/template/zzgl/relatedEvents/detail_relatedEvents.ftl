<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>涉事案件详情</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<style>
		.LabName span{padding-right: 1px;}
	</style>
</head>

<body>
	<div id="content-d" class="MC_con content light">
		<input type="hidden" id="id" name="id" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="2" />
		<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
            <div class="NorForm">
            	<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>案(事)件编号：</span></label><div class="Check_Radio FontDarkBlue" style="width:50%;"><#if relatedEvents.reNo??>${relatedEvents.reNo}</#if></div></td>
                    <td colspan="2"><label class="LabName"><span><font color="red">*</font>案(事)件标题：</span></label><div class="Check_Radio FontDarkBlue" style="width:351px;"><#if relatedEvents.reName??>${relatedEvents.reName}</#if></div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd" style="width:30%;"><label class="LabName"><span><font color="red">*</font>发生日期：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.occuDateStr??>${relatedEvents.occuDateStr}</#if></div></td>
                    <td style="width:30%;"><label class="LabName"><span><font color="red">*</font>所在地段：</span></label><div style="width:50%;" class="Check_Radio FontDarkBlue"><#if relatedEvents.bizName??>${relatedEvents.bizName}</#if></div></td>
                    <td><label class="LabName"><span>案件性质：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.natureName??>${relatedEvents.natureName}</#if></div></td>
                  </tr>
                  <tr>
					<td colspan="3" class="LeftTd">
						<label class="LabName"><span><font color="red">*</font>发生地点：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.occuAddr??>${relatedEvents.occuAddr}</#if></div>
					</td>
				  </tr>
				  <#if !(showType?? && showType == 'map')>
				  <tr>
		    		<td colspan="3" class="LeftTd">
		    			<label class="LabName"><span>地理标注：</span></label>
		    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
		        	</td>
		        </tr>
		        </#if>
                  <tr>
                    <td class="LeftTd" style="width:31%;"><label class="LabName" style="width:38%;"><span><font color="red">*</font>主犯(嫌疑犯)证件姓名：</span></label><div style="width:50%;" class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersName??>${relatedEvents.prisonersName}</#if></div></td>
                    <td><label class="LabName" style="width:41%;"><span><font color="red">*</font>主犯(嫌疑犯)证件类型：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersDocTypeName??>${relatedEvents.prisonersDocTypeName}</#if></div></td>
                    <td><label class="LabName" style="width:31%;"><span><font color="red">*</font>主犯(嫌疑犯)证件号码：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersDocNo??>${relatedEvents.prisonersDocNo}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span><font color="red">*</font>作案人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.crimeNum??>${relatedEvents.crimeNum}人</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>在逃人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.ecapeNum??>${relatedEvents.ecapeNum}人</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>抓捕人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.arrestedNum??>${relatedEvents.arrestedNum}人</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span><font color="red">*</font>案件情况：</span></label><div class="Check_Radio FontDarkBlue" style="width:610px;"><#if relatedEvents.situation??>${relatedEvents.situation}</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span><font color="red">*</font>侦破情况：</span></label><div class="Check_Radio FontDarkBlue" style="width:610px;"><#if relatedEvents.detectedOverview??>${relatedEvents.detectedOverview}</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span>是否破案：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.isDetectionName??>${relatedEvents.isDetectionName}</#if></div></td>
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
