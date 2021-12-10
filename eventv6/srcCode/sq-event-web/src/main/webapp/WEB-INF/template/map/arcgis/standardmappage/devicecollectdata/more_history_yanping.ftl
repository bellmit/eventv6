<!DOCTYPE html>
<html>
<head>
	<title>农业设备历史列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<style type="text/css">
		.inp1 {width:200px;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	  <table id="alarmInfo"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
				<div id="hiddenValue">
					<input type="hidden" id="deviceServiceId" name="deviceServiceId" value="<#if deviceInfo??><#if deviceInfo.deviceServiceId??>${deviceInfo.deviceServiceId}</#if></#if>">
					<input type="hidden" id="deviceType" name="deviceType" value="<#if deviceInfo??><#if deviceInfo.deviceType??>${deviceInfo.deviceType}</#if></#if>">
				</div>
				<div class="fl">
					<ul>
						<li>采集时间：</li>
						<li>
							<input class="inp1 Wdate fl queryParam" type="text" id="startTimeAla" name="startTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true, isShowClear:false})" readonly="readonly"></input>
							<span class="Check_Radio" style="padding:0 5px;">至</span>
							<input class="inp1 Wdate fl queryParam" type="text" id="endTimeAla" name="endTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true, isShowClear:false})" readonly="readonly">
							</input>
						</li>
					</ul>
				</div>
		        <div class="btns">
					<ul>
						<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchDataAla()">查询</a></li>
			            <!--<li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetConditionAla()">重置</a></li>-->
					</ul>
		        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight"></div>
	</div>
</body>
<script type="text/javascript">

$(function() {
	loadAlarmInfo(); //加载列表
});

//条件查询告警信息列表
function searchDataAla() {
	$('#alarmInfo').datagrid('reload', $('#searchForm').serializeJson());
}
//重置告警信息列表
function resetConditionAla() {
	$('#searchForm').form('clear');
	searchDataAla();
}
//加载列表历史告警信息列表
	function loadAlarmInfo() {
		$('#alarmInfo').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			width:500,
			height:250,
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfYanpingDeviceController/findHistoryList.jhtml',
			queryParams: $('#searchForm').serializeJson(), 
			columns: [[
					{field:'STYPE', title:'传感器类型名称', align:'center',width:100},
					{field:'SNAME', title:'传感器名称', align:'center',width:100},
					{field:'SUNIT', title:'传感器单位', hidden:'true',width:100},
					{field:'SNEWESTVALUE', title:'传感器最新值', align:'center',width:100,
						formatter : function(value, rec, index) {
								if(rec.SUNIT=='百分比'){
									rec.SUNIT="%";
								}
								var f=rec.SNEWESTVALUE+""+rec.SUNIT;
								return f;
							}
					},
					{field:'COLLETIME', title:'采集时间', align:'center',width:100}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata" style="height:200px;"></div>');
				} 
			},
			onLoadError: function() {
				listError();
			}
		});
	}

</script>
</html>
