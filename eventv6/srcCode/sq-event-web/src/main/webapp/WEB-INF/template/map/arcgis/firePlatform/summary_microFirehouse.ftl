<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>资源概要信息</title>
	<#include "/component/commonFiles-1.1.ftl" />
	
	<style type="text/css">
		.titleWidth{width: 95%}
		.divWidth{width: 64%;}
		.LabName{width: 65px;}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div id="norFormDiv" class="NorForm">
			<div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd">
							<div class="Check_Radio FontDarkBlue titleWidth" style="font-size: 14px; padding-left: 5px;font-weight: bold;">
								${fireTeamName!}
							</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue divWidth">${gridName!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>类&nbsp;&nbsp;型：</span></label><div class="Check_Radio FontDarkBlue">${typeName!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>负责人员：</span></label><div class="Check_Radio FontDarkBlue divWidth">${teamLeader!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>联系电话：</span></label><div class="Check_Radio FontDarkBlue">${tel!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>人&nbsp;&nbsp;数：</span></label><div class="Check_Radio FontDarkBlue">${teamMemberAmount!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>基本装备：</span></label><div class="Check_Radio FontDarkBlue divWidth">${equipment!''}</div>
						</td>
					</tr>
					<tr>
						<td class="LeftTd">
							<label class="LabName"><span>位&nbsp;&nbsp;置：</span></label><div class="Check_Radio FontDarkBlue divWidth">${positionDesc!''}</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
			var winWidth = $(document).width();//扣除左右边距
			
			$("#content-d").width(winWidth);
			$("#norFormDiv").width(winWidth);
			
			var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
		});
	</script>
</body>
</html>
