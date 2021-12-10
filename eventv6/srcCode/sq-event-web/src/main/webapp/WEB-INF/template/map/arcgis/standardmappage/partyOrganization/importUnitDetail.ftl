<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重点单位详情页面</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
.ThreeColumn .Check_Radio{width:260px;}
.ThreeColumn .w85{width:85px;}
.LabName{width:70px;}
.NorForm td{border-bottom:none;}
</style>
</head>

<body>
	<div id="content-d" class="MC_con content light" style="height:400px;">
        <div class="NorForm ThreeColumn">
        	<#if importUnit??>
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
            	  <tr>
                  	<td class="LeftTd" colspan="2"><label class="LabName"><span>单位名称：</span></label><div class="Check_Radio FontDarkBlue">${importUnit.enterpriseName!''}</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd" colspan="2"><label class="LabName"><span>单位地址：</span></label><div class="Check_Radio FontDarkBlue">${importUnit.address!''}</div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>单位类型：</span></label><div class="Check_Radio FontDarkBlue w85">${importUnit.typeLabel!''}</div></td>
                    <td class="LeftTd"><label class="LabName"><span>所属组织：</span></label><div class="Check_Radio FontDarkBlue w85">${importUnit.orgName!''}</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd" colspan="2"><label class="LabName"><span>主要产品：</span></label><div class="Check_Radio FontDarkBlue">${importUnit.mainProduct!''}</div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>审批单位：</span></label><div class="Check_Radio FontDarkBlue w85">${importUnit.approval!''}</div></td>
                    <td class="LeftTd"><label class="LabName"><span>安防措施：</span></label><div class="Check_Radio FontDarkBlue w85">${importUnit.safetyMeasure!''}</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd" colspan="2"><label class="LabName"><span>单位备注：</span></label><div class="Check_Radio FontDarkBlue">${importUnit.remark!''}</div></td>
                  </tr>
                </table>
            </#if>
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
