<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件导入中间表-详情</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<style type="text/css">
		.LeftTd .LabName{width: 42%;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm" style="width:718px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<#if recordMap??>
							<#list recordMap?keys as columnName>
								<tr>
									<td class="LeftTd">
										<label class="LabName"><span>${recordMap[columnName].COMMENTS}(${columnName})：</span></label>
										<div class="Check_Radio FontDarkBlue">${recordMap[columnName].COLUMN_VALUE}</div>
									</td>
								</tr>
							</#list>
						</#if>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
				<a href="###" class="BigNorToolBtn CancelBtn" onclick="closeWin();">关闭</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function(){
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        
	        $("#norFormDiv").width($(window).width());
	        enableScrollBar('content-d',options); 
	    });
		
		function closeWin(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>
