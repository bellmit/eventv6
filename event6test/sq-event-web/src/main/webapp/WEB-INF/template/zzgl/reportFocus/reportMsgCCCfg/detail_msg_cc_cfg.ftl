<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>人员配置信息-详情</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${COMPONENTS_URL}/css/zzForm/component.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript" src="${GEO_URL}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/zzForm/zz-form.js"></script>
		
		<style type="text/css">
			.singleCellInpClass{width: 285px;}
			.doubleCellClass{width: 85%;}
		</style>
	</head>
	<body>
		<form id="msgCCCfgForm" name="msgCCCfgForm" action="" method="post">
			<input type="hidden" name="cfgUUID" value="${cfgInfo.cfgUUID!}" />
			
			<div id="content-d" class="MC_con content light">
				<div id="eventWechatNorformDiv" class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>组织层级：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.orgChiefLevelName!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>组织编码：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.orgCode!}</div>
							</td>
						</tr>
						<#if cfgInfo.orgChiefLevelName??>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>基准组织：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.benchmarkOrgCode!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>专业编码：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.professionCode!}</div>
							</td>
						</tr>
						</#if>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>流程图名称：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.workflowName!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>目标节点：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.wfStartNodeName!}<#if cfgInfo.wfStartNodeName?? && cfgInfo.wfEndNodeName??>-></#if>${cfgInfo.wfEndNodeName!}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>配置类型：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.cfgTypeName!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>配置值：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.cfgValue!}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>配置值：</span></label><div class="Check_Radio FontDarkBlue">${cfgInfo.ccTypeName!}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>配置状态：</span></label><div class="Check_Radio FontDarkBlue"><#if cfgInfo.cfgStatus?? && cfgInfo.cfgStatus=='0'>停用<#else>启用</#if></div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue doubleCellClass">${cfgInfo.remark!}</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">关闭</a>
				</div>
			</div>
		</form>
	</body>
	
	<script type="text/javascript">
		var _winHeight = 0, _winWidth = 0;
		
		$(function() {
			_winHeight = $(window).height();
			_winWidth = $(window).width();
		});
		
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
		
		$(window).resize(function() {
			var winHeight = $(window).height();
			var winWidth = $(window).width();
			
			if(winHeight != _winHeight || winWidth != _winWidth) {
				location.reload();
			}
		});
	</script>
</html>