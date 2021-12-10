<!DOCTYPE html>
<html>
<head>
	<title>水质监测历史列表</title>
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
			fitColumns: false, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			width:500,
			height:250,
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfYanpingStDeviceController/findTrashHistoryList.jhtml',
			queryParams: $('#searchForm').serializeJson(), 
			columns: [[
					{field:'BATTERYSTATUS', title:'电池状态', align:'center',width:100,
						formatter:function(value,rec,rowIndex){
							if(value == null){
								value = "";
							}
							switch(value) {
								case "0":
							  		value = "未充电";
								  	break;
								case "1":
								  	value = "主电池充电池";
								  	break;
							}
						  return value;
						}
					}, 
					{field:'CAPACITY', title:'当前容量', align:'center',width:100},
					{field:'TCOMPRESS', title:'压缩装置', align:'center',width:100,
						formatter:function(value,rec,rowIndex){
							if(value == null){
								value = "";
							}
							switch(value) {
								case "0":
							  		value = "正常";
								  	break;
								case "1":
								  	value = "异常";
								  	break;
							}
						  return value;
						}
					},
					{field:'COMPRESS70COUNT', title:'当天70%启动压缩装置次数', align:'center',width:150},
					{field:'COMPRESS90COUNT', title:'当天90%启动压缩装置次数', align:'center',width:150},
					{field:'TOPEN', title:'开门状态', align:'center',width:100,
						formatter:function(value,rec,rowIndex){
							if(value == null){
								value = "";
							}
							switch(value) {
								case "0":
							  		value = "正常";
								  	break;
								case "1":
								  	value = "异常";
								  	break;
							}
						  return value;
						}
					},
					{field:'PRIMARYBATTERY', title:'主电池电压', align:'center',width:120},
					{field:'TSCROLL', title:'滚动屏状态', align:'center',width:120,
						formatter:function(value,rec,rowIndex){
							if(value == null){
								value = "";
							}
							switch(value) {
								case "0":
							  		value = "正常";
								  	break;
								case "1":
								  	value = "异常";
								  	break;
							}
						  return value;
						}
					
					},
					{field:'VOICE', title:'语音装置', align:'center',width:100,
						formatter:function(value,rec,rowIndex){
							if(value == null){
								value = "";
							}
							switch(value) {
								case "0":
							  		value = "正常";
								  	break;
								case "1":
								  	value = "异常";
								  	break;
							}
						  return value;
						}
					
					},
					{field:'COLLE_TIME', title:'采集时间', align:'center',width:150,
						formatter:function(value,rec,rowIndex){
							return formatDatebox(value);
						}
					}
					
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
	function formatDatebox(value) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
	} 
</script>
</html>