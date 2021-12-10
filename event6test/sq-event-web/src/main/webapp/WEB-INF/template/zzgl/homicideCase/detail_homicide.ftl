<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-详情</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.cellClass{width:85%;}
		.areaClass{width:88%;}
		.LabName span{padding-right: 0px;}
		.NorForm td{    padding: 12px 0px 6px 10px;}
		.Asterik{color:red;}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div class="NorForm">
			<form id="homicideCaseForm" name="homicideCaseForm" action="" method="post">
				<div id="homicideCaseTabDiv" class="homicideTabDiv">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" class="LeftTd" style="width:70%;">
								<label class="LabName"><span><label class="Asterik">*</label>命案名称：</span></label><div class="Check_Radio FontDarkBlue cellClass">${relatedEvent.reName!''}</div>
							</td>
							<td class="LeftTd" style="width:30%;">
								<label class="LabName"><span>所属网格：</span></label><div class="Check_Radio FontDarkBlue" style="width:50%;">${relatedEvent.gridPath!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>发生开始日期：</span></label><div class="Check_Radio FontDarkBlue">${relatedEvent.occuDateStr!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>侦查结束日期：</span></label><div class="Check_Radio FontDarkBlue">${relatedEvent.spyEndDateStr!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>案件编号：</span></label><div class="Check_Radio FontDarkBlue">${relatedEvent.reNo!''}</div>
							</td>
						</tr>
						<tr>
							<td colspan="3" class="LeftTd">
								<label class="LabName"><span>简要情况：</span></label><div class="Check_Radio FontDarkBlue areaClass">${relatedEvent.situation!''}</div>
					        </td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	
	<div class="BigTool" style="position: absolute;bottom: <#if relatedEvent.reId??>10px<#else>0px</#if>;">
    	<div class="BtnList1" style="position: relative;width: 130px;    height: 32px;    margin: 0 auto;">
			<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
        </div>
    </div>
	
	<script type="text/javascript">
		$(function(){
			//$("#content-d").css("height", "294");
		});
		function cancel(){
			//parent.parent.closeMaxJqueryWindow();
			parent.parent.closeWinExt();
		}
	</script>
	
</body>
</html>
