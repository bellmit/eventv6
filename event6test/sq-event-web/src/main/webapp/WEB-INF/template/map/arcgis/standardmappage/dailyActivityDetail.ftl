<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详情页面</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
.ThreeColumn .Check_Radio{width:260px;}
.ThreeColumn .w85{width:85px;}
.LabName{width:90px;}
.NorForm td{border-bottom:none;}
</style>
</head>

<body>
	<div id="content-d" class="MC_con content light" style="height:400px;width:500px;">
        <div class="NorForm ThreeColumn">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            	  <tr>
                  	<td class="LeftTd" colspan="2"><label class="LabName"><span>活动名称：</span></label><div class="Check_Radio FontDarkBlue"><#if dailyActivity.activityName??>${dailyActivity.activityName}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd" colspan="2"><label class="LabName"><span>活动地点：</span></label><div class="Check_Radio FontDarkBlue"><#if dailyActivity.activityAddr??>${dailyActivity.activityAddr}</#if></div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>活动时间：</span></label><div style="width: 161px;" class="Check_Radio FontDarkBlue w85"><#if dailyActivity.activityDateStr??>${dailyActivity.activityDateStr}</#if></div></td>
                    <td class="LeftTd"><label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue w85"><#if dailyActivity.gridName??>${dailyActivity.gridName}</#if></div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>负责人：</span></label><div class="Check_Radio FontDarkBlue w85"><#if dailyActivity.principal??>${dailyActivity.principal}</#if></div></td>
                    <td class="LeftTd"><label class="LabName"><span>负责人电话：</span></label><div class="Check_Radio FontDarkBlue w85"><#if dailyActivity.principalTel??>${dailyActivity.principalTel}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd" colspan="2"><label class="LabName"><span>活动内容：</span></label><div class="Check_Radio FontDarkBlue"><#if dailyActivity.activityContent??>${dailyActivity.activityContent}</#if></div></td>
                  </tr>
                </table>
        </div>
    </div>
	
	<script>
		(function($){ 
		    $(window).load(function(){ 
		        var options = { 
		            axis : "yx", 
		            theme : "minimal-dark" 
		        }; 
		        enableScrollBar('content-d',options); 
		    }); 
		})(jQuery);
	</script>
</body>
</html>
