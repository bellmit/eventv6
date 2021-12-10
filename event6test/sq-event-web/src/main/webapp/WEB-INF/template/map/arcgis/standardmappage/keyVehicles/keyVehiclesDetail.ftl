<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>详情页面</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
.ThreeColumn .Check_Radio{width:235px;}
.ThreeColumn .w85{width:85px;}
.LabName{width:120px;}
.NorForm td{border-bottom:none;}
</style>
</head>

<body>
	<div id="content-d" class="MC_con content light" style="height:400px;">
        <div class="NorForm ThreeColumn">
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            	  <tr>
                  	<td class="LeftTd">
                        <label class="LabName">
                            <span>所属区域：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue"><#if rgDemoBase.gridName??>${rgDemoBase.gridName}</#if></div>
                    </td>
                  </tr>
                  <tr>
                  	<td class="LeftTd">
                        <label class="LabName">
                            <span>扶贫示范基地名称：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue"><#if rgDemoBase.demoBaseName??>${rgDemoBase.demoBaseName}</#if></div>
                    </td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>负责人：</span></label><div class="Check_Radio FontDarkBlue"><#if rgDemoBase.dutyName??>${rgDemoBase.dutyName}</#if></div></td>
                  </tr>
                  <tr>
                      <td class="LeftTd"><label class="LabName"><span>负责人电话：</span></label><div class="Check_Radio FontDarkBlue"><#if rgDemoBase.dutyPhone??>${rgDemoBase.dutyPhone}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>基地建设时间：</span></label><div class="Check_Radio FontDarkBlue"><#if rgDemoBase.baseBuildDate??>${rgDemoBase.baseBuildDate?string("yyyy-MM-dd")}</#if></div></td>
                  </tr>
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>基地规模（人）：</span></label><div class="Check_Radio FontDarkBlue"><#if rgDemoBase.baseScale??>${rgDemoBase.baseScale}</#if></div></td>
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
