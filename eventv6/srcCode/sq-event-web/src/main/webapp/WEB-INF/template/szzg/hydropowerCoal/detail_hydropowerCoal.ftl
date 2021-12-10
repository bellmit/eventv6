<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.LabName {width:130px}
	</style>
</head>
<body>
	<div id="content-d" class="MC_con content light">
		<div name="tab" id="div0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<label class="LabName"><span>所属区域：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.orgName)!}</span>
					</td>
					<td>
						<label class="LabName"><span>所属年月：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.syear)!}</span>
					</td>
					
				</tr>
				<tr>
					<td >
						<label class="LabName"><span>类型：</span></label>
						<span class="Check_Radio FontDarkBlue">
						<#if bo.type=='1'> 
						 水
						<#elseif  bo.type=='2'> 
						 电
						<#elseif  bo.type=='3'> 
						 煤
						<#else> 
						</#if>
						</span>
					</td>
					<td>
						<label class="LabName"><span>使用量（<#if bo.type=='2'>度<#else>吨</#if>）：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.usage)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>同比（%）：</span></label>
						<span class="Check_Radio FontDarkBlue"><#if bo.yearBasis??>${bo.yearBasis?string("0.##")}</#if></span>
					</td>
					<td>
						<label class="LabName"><span>环比（%）：</span></label>
						<span class="Check_Radio FontDarkBlue"><#if bo.linkRelRatio??>${bo.linkRelRatio?string("0.##")}</#if></span>
					</td>
					
			</tr>
			<tr>
				    <td>
						<label class="LabName"><span>同比增长（%）：</span></label>
						<span class="Check_Radio FontDarkBlue"><#if bo.yearBasisInc??>${bo.yearBasisInc?string("0.##")}</#if></span>
					</td>
					<td>
						<label class="LabName"><span>环比增长（%）：</span></label>
						<span class="Check_Radio FontDarkBlue"><#if bo.linkRelRatioInc??>${bo.linkRelRatioInc?string("0.##")}</#if></span>
					</td>
				
				</tr>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
</script>
</html>
