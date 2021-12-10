<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人口基本情况</title>
<#include "/component/commonFiles-1.1.ftl" />
<style>
.LabName{margin-left:10px;}
.NorForm td{padding-left:0px;vertical-align:middle;}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd" style="width:100px;">
								<label class="LabName"><span>所属网格：</span></label>
							</td>
							<td class="LeftTd" style="width:200px;">
								<span>${entity.orgName}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计年份：</span></label>
							</td>
							<td class="LeftTd">
								<span>${entity.syear}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计类型：</span></label>
							</td>
							<td class="LeftTd">
								<span>${entity.stypeStr}</span>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>统计数量：</span></label>
							</td>
							<td class="LeftTd">
								<span>${entity.snum!'0'}</span>
							</td>
						</tr>
				    </table>
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>	
	</form>
	
</body>

<script type="text/javascript">

	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>