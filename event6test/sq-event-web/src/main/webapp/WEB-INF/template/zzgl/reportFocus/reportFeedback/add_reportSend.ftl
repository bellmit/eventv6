<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>入格事件-新增/编辑</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<#include "/component/standard_common_files-1.1.ftl" />
		<#include "/component/ComboBox.ftl" />
		
		<style type="text/css">
			.singleCellInpClass{width: 57%}
			.singleCellClass{width: 62%;}
			.labelNameWide{width: 132px;}
		</style>
	</head>
	<body>
		<form id="reportSendForm" name="reportSendForm" action="" method="post">
			<input type="hidden" id="isStart" name="isStart" value="false" />
			<input type="hidden" id="seUUId" name="seUUId" value="<#if seUUId??>${seUUId?c}</#if>" />

			<div id="content-d" class="MC_con content light">
				<div id="reportSendNorformDiv" class="NorForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>所属区域：</span></label>
								<input type="hidden" id="regionCode" name="regionCode" value="${regionCode!}" />
								<input type="text" id="gridName" class="from flex1 bg-btm-arrow easyui-validatebox"
									   data-options="required:true,tipPosition:'bottom'" value="${regionName!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>业务类型：</span></label>
								<input type="hidden" id="bizType" name="bizType" value="<#if bizType??>${bizType}</#if>" />
								<input type="text" id="bizTypeName" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom',required:true"  value="" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>业务标识：</span></label>
								<#--<div class="Check_Radio FontDarkBlue">${bizSignName!'XXX'}</div>-->
								<input name="bizSign" type="text" value="${bizSign!'XXX'}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>数据来源：</span></label>
								<input type="hidden" id="dataSource" name="dataSource" value="${dataSource!}" />
								<input type="text" class="inp1 easyui-validatebox singleCellClass" data-options="tipPosition:'bottom'" id="dataSourceName" value="" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>数据标识：</span></label>
								<input type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom'" id="dataSign" value="${dataSign!}" />
							</td>
							<td class="LeftTd">
								<label class="LabName labelNameWide"><span>备注：</span></label>
								<textarea rows="6" style="width: 90%;" id="remark" name="remark"  class="easyui-validatebox  validatebox-text locked" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']">${remark!}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div class="BigTool">
				<div class="BtnList">
					<a href="###" onclick="saveReportData(1);" class="BigNorToolBtn BigShangBaoBtn">提交</a>
					<a href="###" onclick="saveReportData(0);" class="BigNorToolBtn SaveBtn">保存</a>
					<a href="###" onclick="closeWin();" class="BigNorToolBtn CancelBtn">取消</a>
				</div>
			</div>
		</form>
	</body>

	<script type="text/javascript">
		$(function() {
			seUUId = $('#seUUId').val();
			//业务类型
			AnoleApi.initListComboBox("bizTypeName", "bizType", null, null, ["${bizType!'0'}"], {
				RenderType : "00",
				DataSrc : [{"name":"事件", "value":"01"},{"name":"两违", "value":"02"}],
			});

			<#--AnoleApi.initListComboBox("dataSourceName", "dataSource", "B210001001", null, ["${dataSource!}"]);-->
			AnoleApi.initGridZtreeComboBox("gridName", null, function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#regionCode").val(grid.orgCode);
				}
			});

			$("#remark").width(($(window).width() - $("#remark").siblings().eq(0).outerWidth(true)) * 0.95);
		});
		
		function saveReportData(btnType) {
			var isValid =  $("#reportSendForm").form('validate'),
			if(isValid) {
				modleopen();
				var isStart = btnType == 1;
				
				$("#reportSendForm").attr("action", "${rc.getContextPath()}/zhsq/reportFeedback/saveOrUpdate.jhtml");
				
				$('#isStart').val(isStart);
				
				$("#reportSendForm").ajaxSubmit(function(data) {
					modleclose();
					if(data.success && data.success == true) {
						if(isStart) {
							parent.searchData();
							parent.detail(data.seUUId);
							if(typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
								parent.closeBeforeMaxJqueryWindow();
							}
						} else {
		  					parent.reloadDataForSubPage(data.tipMsg);
		  				}
					} else {
						$.messager.alert('错误', data.tipMsg, 'error');
					}
				});
			}
		}
		
		function closeWin() {
			parent.closeMaxJqueryWindow();
		}
	</script>
</html>