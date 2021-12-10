<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>水表(大)历史信息</title>
<meta charset="utf-8">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge"><!--如果系统安装ie8或以上版本，则使用最高版本ie渲染；-->
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/listSet.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style>
.datagrid-view{
	height: 300px; 
}
 p{width: 500px;}

</style>
</head>
  
<body>

<div class="OpenWindow MaxHeight">
    <div class="MetterList">
    	<div id="content-d" class="box content light">
    		<div id="hidden_value">
		    	
		    </div>
        	<div class="MetterContent">
                <div class="ConList">
                    <div class="nav" id="tab">
                        <ul>
                            
                            <li class="current" id="historyTab">历史记录</li> <!--   通过css来控制显示，每个<li>对应一个div ,比如这个对应下面注释的那个div-->
                            <li id="alarmTab" onclick="onclickTab();" >告警信息</li>
                        </ul>
                    </div>
                    <div class="ListShow tabss"  style="height:330px;width:100%">
                        
                   		<div class="con " style="height:330px;width:100%"> 
							<table id="historyInfo"></table>
							
							<div id="jqueryToolbarHis">
								<div class="ConSearch">
									<form id="searchFormHis">
									<div id="hiddenValue">
										<input type="hidden" id="deviceId" name="deviceId" value="<#if deviceInfo??><#if deviceInfo.deviceServiceId??>${deviceInfo.deviceServiceId}</#if></#if>">
										<input type="hidden" id="deviceType" name="deviceType" value="<#if deviceInfo??><#if deviceInfo.deviceType??>${deviceInfo.deviceType}</#if></#if>">
									</div>
									
									<div class="fl">
										<ul>
											<li>采集时间：</li>
											<li>
												<input class="inp1 Wdate fl queryParam" type="text" id="startTimeHis" name="startTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true, isShowClear:false})" readonly="readonly"></input>
												<span class="Check_Radio" style="padding:0 5px;">至</span>
												<input class="inp1 Wdate fl queryParam" type="text" id="endTimeHis" name="endTime" value="" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true, isShowClear:false})" readonly="readonly">
												</input>
											</li>
										</ul>
									</div>
							        <div class="btns">
										<ul>
											<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchDataHis()">查询</a></li>
								            <!--<li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetConditionHis()">重置</a></li>-->
										</ul>
							        </div>
									</form>
								</div>
								<div class="h_10" id="TenLineHeightHis"></div>
							</div>
                       	</div>
                        
                        
                        
                        <div class="hide" style="height:330px;width:100%">
                       		<table id="alarmInfo"></table>
                       		<div id="jqueryToolbarAla">
								<div class="ConSearch">
									<form id="searchFormAla">
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
								           <!-- <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetConditionAla()">重置</a></li>-->
										</ul>
							        </div>
									</form>
								</div>
								<div class="h_10" id="TenLineHeightAla"></div>
							</div>
                       	</div>
                       	
                       	
                      </div>
                        
                        
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>

<script type="text/javascript">
//获取当前时间
function setCurrDate(){
	var myDate = new Date();
	var date = myDate.format('yyyy-MM-dd');
	$("#startTimeHis").val(date);
	$("#endTimeHis").val(date);
	$("#startTimeAla").val(date);
	$("#endTimeAla").val(date);
}

$(function() {
	clickTab = "n";
	setCurrDate();
	loadHistoryInfo();
	
});
//点击tab便签加载数据
var clickTab = "n";
function onclickTab(){
	if(clickTab == "n"){
		loadAlarmInfo();
	}
	clickTab = "y";
}

//条件查询告警信息列表
function searchDataAla() {
	$('#alarmInfo').datagrid('reload', $('#searchFormAla').serializeJson());
}
//重置告警信息列表
function resetConditionAla() {
	$('#searchFormAla').form('clear');
	searchDataHis();
}
//加载列表告警信息列表
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
			queryParams: $('#searchFormHis').serializeJson(),
			columns: [[
				
				<#if deviceInfo.deviceType == "100002" || deviceInfo.deviceType == "100001">//水表告警
					/*
					{field:'device_type', title:'设备类型', align:'center', width:fixWidth(0.30),
				   	formatter : function(value, rec, index) {
						var deviceType = '';
						switch(value) {
								case "1":
									deviceType = "大表";
								  	break;
								case "2":
									deviceType = "小表";
								  	break;
								case "3":
									deviceType = "消防栓";
								  	break;
								
						}	
						return deviceType;
					}},*/
					{field:'alarm_info', title:'告警信息', align:'center', width:fixWidth(0.40)},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
				</#if>
				<#if deviceInfo.deviceType == "100005">//井盖告警
					{field:'device_status', title:'设备状态', align:'center', width:fixWidth(0.30),
				   	formatter : function(value, rec, index) {
						var deviceStatus = '';
						switch(value) {
								case "0":
									deviceStatus = "开";
								  	break;
								case "1":
									deviceStatus = "关";
								  	break;
															
						}	
						return deviceStatus;
					}},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
	
				</#if>
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbarAla',
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



//条件查询历史信息列表
function searchDataHis() {
	$('#historyInfo').datagrid('reload', $('#searchFormHis').serializeJson());
}
//重置历史信息列表
function resetConditionHis() {
	$('#searchFormHis').form('clear');
	searchDataHis();
}
//加载列表历史信息列表
	function loadHistoryInfo() {
		$('#historyInfo').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			width:500,
			height:250,
			url: '${rc.getContextPath()}/zhsq/map/arcgis/mw/moreDetail/historyInfo.jhtml',
			queryParams: $('#searchFormHis').serializeJson(),
			columns: [[
				<#if deviceInfo.deviceType == "100001">//水表（小）
					{field:'water_degree', title:'水表度数', align:'center', width:fixWidth(0.30)},
					{field:'signal_intensity', title:'信号强度', align:'center', width:fixWidth(0.40)},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
				</#if>
				<#if deviceInfo.deviceType == "100002">//水表（大）
					{field:'forward_flow', title:'正向流量', align:'center', width:fixWidth(0.30)},
					{field:'reverse_flow', title:'反向流量', align:'center', width:fixWidth(0.40)},
					{field:'pressure', title:'压力', align:'center', width:fixWidth(0.40)},
					{field:'temperature', title:'温度', align:'center', width:fixWidth(0.40)},
					{field:'signal_intensity', title:'信号强度', align:'center', width:fixWidth(0.40)},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
				</#if>
				<#if deviceInfo.deviceType == "100005">//井盖
					{field:'type', title:'子设备类型', align:'center', width:fixWidth(0.30),
					formatter : function(value, rec, index) {
					var deviceType = '';
					switch(value) {
							case "0":
								deviceType = "井盖井下";
							  	break;
							case "1":
								deviceType = "井盖路面积水";
							  	break;
							case "2":
								deviceType = "井盖浊度";
							  	break;
							case "3":
								deviceType = "井盖流速";
							  	break;
					}	
					return deviceType;
					}},
					{field:'acquisition_value', title:'采集值', align:'center', width:fixWidth(0.40)},
					{field:'recordtime', title:'采集时间', align:'center', width:fixWidth(0.30)}
				</#if>
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbarHis',
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
