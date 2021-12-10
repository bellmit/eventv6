<!DOCTYPE html>
<html>
<head>
	<title>烟感历史告警数据列表</title>
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
					<input type="hidden" id="deviceId" name="deviceId" value="<#if deviceInfo??><#if deviceInfo.deviceServiceId??>${deviceInfo.deviceServiceId}</#if></#if>">
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
function setCurrDate(){
	var myDate = new Date();
	var date = myDate.format('yyyy-MM-dd');
	$("#startTimeAla").val(date);
	$("#endTimeAla").val(date);
}

$(function() {
	setCurrDate();
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
			url: '${rc.getContextPath()}/zhsq/map/arcgis/mw/moreDetail/alarmInfo.jhtml',
			queryParams: $('#searchForm').serializeJson(), 
			columns: [[
				<#if deviceInfo.deviceType == "100004">
					{field:'alarm_type', title:'告警类型', align:'center', width:fixWidth(0.30),
				   	formatter : function(value, rec, index) {
						var alarmCN = '';
						switch(value) {
								case "1":
									alarmCN = "上线";
								  	break;
								case "2":
									alarmCN = "失联";
								  	break;
								case "3":
									alarmCN = "烟雾告警";
								  	break;
								case "4":
									alarmCN = "火警";
								  	break;
								case "5":
									alarmCN = "电量正常";
								  	break;
								case "6":
									alarmCN = "电量低";
								  	break;
								case "7":
									alarmCN = "拆除";
								  	break;
								case "8":
									alarmCN = "自检故障";
								  	break;
								case "9":
									alarmCN = "烟雾告警解除";
								  	break;
								case "10":
									alarmCN = "火警解除";
								  	break;
						}	
						return alarmCN;
					}},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
				</#if>
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
