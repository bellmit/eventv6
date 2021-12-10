<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安全隐患概要信息</title>
<#include "/component/commonFiles-map-1.1.ftl" />
</head>

<body>
	<div id="content-d" class="MC_con content light" style="height:440px;">
		<div style="width:580px;padding:10px;"><span class="FontDarkBlue" style="font-size:14px;font-weight:bold"><#if safetyCheckInfo.corPlaceName??>${safetyCheckInfo.corPlaceName}</#if></span></div>
        <div class="NorForm ThreeColumn">
	      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
			 <tr>
				 <td class="LeftTd"><label class="LabName"><span>检查类型：</span></label>
					 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.routineChecksType??><#if safetyCheckInfo.safetyCheckCon.routineChecksType=='0'>一般检查<#else>整改复查</#if></#if></label></div>    
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>检查单号：</span></label>								 
		             <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.checkCode??>${safetyCheckInfo.checkCode}</#if></label></div>
				 </td>									
			  </tr>
			  <tr>
			     <td class="LeftTd"><label class="LabName"><span>检查日期：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.checkDateStr??>${safetyCheckInfo.checkDateStr}</#if></label></div>             
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>开始时间：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.startTime??>${safetyCheckInfo.safetyCheckCon.startTime}</#if></label></div>             
				 </td>	
			 </tr>
			 <tr>
			     <td class="LeftTd"><label class="LabName"><span>结束时间：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.stopTime??>${safetyCheckInfo.safetyCheckCon.stopTime}</#if></label></div>             
				 </td>
				 <td class="LeftTd" id="c1"><label class="LabName"><span>检查场所：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.checkPlace??>${safetyCheckInfo.safetyCheckCon.checkPlace}</#if></label></div>             
				 </td>
				 <td class="LeftTd" id="rc1"  style="display:none;"><label class="LabName"><span>检查人员：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.checker??>${safetyCheckInfo.safetyCheckCon.checker}</#if></label></div>          
				 </td>	
			 </tr>
			 <tr id="recheck1" style="display: none">							     
				 <td class="LeftTd"><label class="LabName"><span>整改通知书号：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.notificationNum??>${safetyCheckInfo.safetyCheckCon.notificationNum}</#if></label></div>             
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>整改期限：</span></label>
				     <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.deadlineDateStr??>${safetyCheckInfo.safetyCheckCon.deadlineDateStr}</#if></label></div>							 		              
				 </td>	
			 </tr>
			 <tr id="recheck2" style="display: none">
				 <td class="LeftTd"><label class="LabName"><span>复查情况：</span></label>
			 		  <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.reviewCaseLabel??>${safetyCheckInfo.safetyCheckCon.reviewCaseLabel}</#if></label></div>         
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>延期整改期限：</span></label>
				      <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.delayDisposeDeadline??>${safetyCheckInfo.safetyCheckCon.delayDisposeDeadline}</#if></label></div>							 		              
				 </td>	
			 </tr>
			 <tr id="check1">							     
				 <td class="LeftTd"><label class="LabName"><span>检查人员：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.checker??>${safetyCheckInfo.safetyCheckCon.checker}</#if></label></div>             
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>存在隐患：</span></label>								     											
					 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.riskGradeLabel??>${safetyCheckInfo.safetyCheckCon.riskGradeLabel}</#if></label></div>							 		              
				 </td>	
			 </tr>
			 <tr>							     
				 <td class="LeftTd"><label class="LabName"><span>是否上报：</span></label>
			 		<div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.isReport??><#if safetyCheckInfo.safetyCheckCon.isReport=='0'>否<#else>是</#if></#if></label></div>    
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>上报部门：</span></label>
				    <div class="Check_Radio FontDarkBlue"><label>
				        <#if safetyCheckInfo.safetyCheckCon.reportDepartment??>${safetyCheckInfo.safetyCheckCon.reportDepartment}</#if>
					</label></div>							    							 		              
				</td>	
			 </tr>
			 <tr id="check2">							     
				 <td class="LeftTd"><label class="LabName"><span>检查情况说明：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.checkContent??>${safetyCheckInfo.safetyCheckCon.checkContent}</#if></label></div> 
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>是否发放整改指令书：</span></label>
				     <div class="Check_Radio FontDarkBlue"><label>
				         <label><input type="radio" id="isDisposeBook" name="isDisposeBook" value="0" <#if safetyCheckInfo.safetyCheckCon.isDisposeBook??><#if safetyCheckInfo.safetyCheckCon.isDisposeBook=='0'>checked</#if><#else>checked</#if> disabled="true"/>否</label>
		                 <label><input type="radio" id="isDisposeBook" name="isDisposeBook" value="1" <#if safetyCheckInfo.safetyCheckCon.isDisposeBook??><#if safetyCheckInfo.safetyCheckCon.isDisposeBook=='1'>checked</#if></#if> disabled="true"/>是</label>
				     </label></div>								     		    							 		              
				</td>	
			 </tr>
			
			 <tr id="recheck3" style="display: none">							     
				 <td class="LeftTd"><label class="LabName"><span>复查情况说明：</span></label>
				    <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.reviewCaseNote??>${safetyCheckInfo.safetyCheckCon.reviewCaseNote}</#if></label></div>							 		
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>整改复查意见书号：</span></label>
				    <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.reviewCaseNum??>${safetyCheckInfo.safetyCheckCon.reviewCaseNum}</#if></label></div>								    								    		    							 		              
				</td>	
			</tr>
			<tr  id="recheck11" style="display: none">							     
				 <td class="LeftTd"><label class="LabName"><span>整改通知书号：</span></label>
				     <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.notificationNum??>${safetyCheckInfo.safetyCheckCon.notificationNum}</#if></label></div>							 		              
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>整改期限：</span></label>
				     <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.deadlineDateStr??>${safetyCheckInfo.safetyCheckCon.deadlineDateStr}</#if></label></div>							 		              
				 </td>	
			 </tr>
			 <tr>							     
				 <td class="LeftTd"><label class="LabName"><span>下次检查日期：</span></label>
			 		 <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.nextCheckDateStr??>${safetyCheckInfo.safetyCheckCon.nextCheckDateStr}</#if></label></div>             
				 </td>
				 <td class="LeftTd"><label class="LabName"><span>短信提醒：</span></label>
				     <div class="Check_Radio FontDarkBlue"><label><#if safetyCheckInfo.safetyCheckCon.smsNotifyMobile??>${safetyCheckInfo.safetyCheckCon.smsNotifyMobile}</#if></label></div>							 		              
				 </td>	
			 </tr>
		</table>
	</div>
	</div>
</body>

<script type="text/javascript">
	checkFormTypeChange();
	isDisposeBookChange();
	
	function checkFormTypeChange(){
	    var routineChecksType='<#if safetyCheckInfo.safetyCheckCon.routineChecksType??>${safetyCheckInfo.safetyCheckCon.routineChecksType}<#else>0</#if>';
	    
		if (routineChecksType == "0") {
			$("#check1").css('display','');
			$("#check2").css('display','');
			$("#c1").css('display','');
			$("#rc1").css('display','none');
			$("#recheck1").css('display','none');
			$("#recheck2").css('display','none');
			$("#recheck3").css('display','none');
		} else {
			$("#check1").css('display','none');
			$("#check2").css('display','none');				
			$("#c1").css('display','none');
			$("#rc1").css('display','');
			$("#recheck1").css('display','');
			$("#recheck2").css('display','');
			$("#recheck3").css('display','');
			$("#recheck11").css('display','none');
		}
	}
	
	//是否发放整改指令书
	function isDisposeBookChange() {	   
		switch ($("input[name='isDisposeBook']:checked").val()){
			case "0":
				$("#recheck11").css('display','none');
				break;
			case "1":
				$("#recheck11").css('display','');
				break;
		}
	}
</script>

</html>	  