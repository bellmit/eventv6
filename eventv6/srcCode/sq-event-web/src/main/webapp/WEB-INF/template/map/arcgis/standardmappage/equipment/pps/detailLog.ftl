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
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="LeftTd" width="400">
						<label class="LabName"><span>姓名：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.visitor??>${ppsLog.visitor}</#if></div>
					</td>
					<td rowspan="4" style="200px;">
						<img style="max-width:200px;" src="data:image/png;base64,<#if ppsLog.visitorImg??>${ppsLog.visitorImg}</#if>"/>
					</td>
				</tr>
				<tr>
					<td class="LeftTd">
						<label class="LabName"><span>证件类型：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.idType??>${ppsLog.idType}</#if></div>
					</td>
				</tr>
				<tr>
					<td class="LeftTd">
						<label class="LabName"><span>证件号码：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.idcard??>${ppsLog.idcard}</#if></div>
					</td>
				</tr>
				<tr>
					<td class="LeftTd">
						<label class="LabName"><span>手机号：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.visitorTel??>${ppsLog.visitorTel}</#if></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span>访客单位：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.visitorCor??>${ppsLog.visitorCor}</#if></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span>单位电话：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.corPhone??>${ppsLog.corPhone}</#if></div>
			    	</td>
			    </tr>
		    	<tr>
					<td class="LeftTd">
						<label class="LabName"><span>被访者：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.respName??>${ppsLog.respName}</#if></div>
			    	</td>
			    	<td class="LeftTd">
						<label class="LabName"><span>被访者手机：</span></label>
						<div class="Check_Radio FontDarkBlue"><#if ppsLog.respTel??>${ppsLog.respTel}</#if></div>
			    	</td>
			    </tr>
		    	<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span>访客事由：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:480px;"><#if ppsLog.remark??>${ppsLog.remark}</#if></div>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="LeftTd">
						<label class="LabName"><span>访客抓拍：</span></label>
						<div class="Check_Radio FontDarkBlue" style="width:480px;">
						<img style="max-width:300px;" src="data:image/png;base64,<#if ppsLog.imgUrl??>${ppsLog.imgUrl}</#if>"/>
						</div>
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
</script>

</html>
