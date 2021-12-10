<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>涉事案件详情</title>
	<#include "/component/commonFiles-1.1.ftl" />
</head>

<body>
	<div id="content-d" class="MC_con content light">
            <div class="NorForm">
            	<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span>案(事)件编号：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.reNo??>${relatedEvents.reNo}</#if></div></td>
                    <td colspan="2"><label class="LabName"><span><font color="red">*</font>案(事)件标题：</span></label><div class="Check_Radio FontDarkBlue" style="width:382px;"><#if relatedEvents.reName??>${relatedEvents.reName}</#if></div></td>
                  </tr>
                  <tr>
                  	<td class="LeftTd"><label class="LabName"><span><font color="red">*</font>发生日期：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.occuDateStr??>${relatedEvents.occuDateStr}</#if></div></td>
                    <td style="width:300px;"><label class="LabName"><span><font color="red">*</font>所属网格：</span></label><div class="Check_Radio FontDarkBlue" style="width:68%;"><#if relatedEvents.gridName??>${relatedEvents.gridName}</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>案件性质：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.natureName??>${relatedEvents.natureName}</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span><font color="red">*</font>发生地点：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.occuAddr??>${relatedEvents.occuAddr}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span><font color="red">*</font>主犯(嫌疑犯)证件姓名：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersName??>${relatedEvents.prisonersName}</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>主犯(嫌疑犯)证件类型：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersDocTypeName??>${relatedEvents.prisonersDocTypeName}</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>主犯(嫌疑犯)证件号码：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.prisonersDocNo??>${relatedEvents.prisonersDocNo}</#if></div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span><font color="red">*</font>作案人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.crimeNum??>${relatedEvents.crimeNum}人</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>在逃人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.ecapeNum??>${relatedEvents.ecapeNum}人</#if></div></td>
                    <td><label class="LabName"><span><font color="red">*</font>抓捕人数：</span></label><div class="Check_Radio FontDarkBlue"><#if relatedEvents.arrestedNum??>${relatedEvents.arrestedNum}人</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span><font color="red">*</font>案件情况：</span></label><div class="Check_Radio FontDarkBlue" style="width:608px;"><#if relatedEvents.situation??>${relatedEvents.situation}</#if></div></td>
                  </tr>
                  <tr>
                    <td colspan="3" class="LeftTd"><label class="LabName"><span><font color="red">*</font>侦破情况：</span></label><div class="Check_Radio FontDarkBlue" style="width:608px;"><#if relatedEvents.detectedOverview??>${relatedEvents.detectedOverview}</#if></div></td>
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
</script>

</html>
