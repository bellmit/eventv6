<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style>
	.LabName{
		width:150px;
	}
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
						<label class="LabName"><span>监测站点名称：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.name)!}</span>
					</td>
					
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>经度：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.longitude)!}</span>
					</td>
					<td>
						<label class="LabName"><span>纬度：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.dimensions)!}</span>
					</td>
				</tr>
				<tr>
				   <td>
						<label class="LabName"><span>水质类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.szlb)!}</span>
					</td>
					<td>
						<label class="LabName"><span>H(无量纲)：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.phvalue)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>溶解氧(mg/l)：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.rjy)!}</span>
					</td>
					<td>
						<label class="LabName"><span>高锰酸盐指数(mg/l)：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.mgzs)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>氨氯(mg/l)：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.al)!}</span>
					</td>
				<td>
				</td>
				</tr>
				
				<tr>
					<td>
						<label class="LabName"><span>检测时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.endTime?string("yyyy-MM-dd"))!}</span>
					</td>
					<td>
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
