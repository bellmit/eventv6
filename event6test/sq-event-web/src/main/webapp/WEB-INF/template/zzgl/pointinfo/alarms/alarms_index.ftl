<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	
	<#include "/component/AnoleDate.ftl">
	
	<style type="text/css">
		.inp1 {width:100px;}
        .sub{
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;
        }
        a{cursor: pointer;}
	</style>
</head>
<body class="easyui-layout">
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
					<li>是否违规：</li>
                    <li>
						<input type="hidden" id="status" name="status"  value = ""/>
						<input type="text" id="statusCN" name="statusCN" class="inp1" value = ""/>
                    </li>
                    <li>告警时间：</li>
                    <li>
                        <input id="date" type="text" class="inp1" style="width:220px;"/>
						<input type="hidden" id="startTime" name="startTime" value = ""/>
						<input type="hidden" id="endTime" name="endTime" value = ""/>
                    </li>
				</ul>
			</div>
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
			<div class="tool fr">
				<#--
				<a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
				<a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
				<a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
				-->
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		AnoleApi.initTreeComboBox("statusCN", "status", "D505", null,null, 
				{ ShowOptions : {EnableToolbar : true},DefText : '请选择'});
	
		//日期控件
		$("#date").anoleDateRender({
			ShowOptions : {
				TabItems : [ "常用", "年", "季", "月", "清空" ],
				CommUsed : [ "本周", "本月", "今天", "确定" ]
			},
			BackEvents : {
				OnSelected : function(api, type) {
					$("#startTime").val(api.getStartDate());
					$("#endTime").val(api.getEndDate());
				},
				OnCleared : function(){
					$("#startTime").val("");
					$("#endTime").val("");
				}
			}
		}).anoleDateApi();

		loadList(); //加载列表
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			fit: true,
			url: '${rc.getContextPath()}/zhsq/event/pointInfo/queryAlarms.json',
            queryParams: $('#searchForm').serializeJson(),
			columns: [[
				{field:'id', hidden:true,title:'防控事件告警Id'},
				{field:'cid', hidden:true,title:'设备Id'},
				{field:'taskId', hidden:true,title:'任务Id'},
                {field:'deviceName', title:'设备名称', align:'left', width:200,formatter : function(val, rec, rowIndex) {
                    return '<a title="'+val+'" onclick="detail(\''+rec.id+'\')">'+val+'</a>';
                }},
				{field:'alarmTime', title:'触发时间', align:'left', width:100,formatter:function(val, rec, rowIndex) {
					return "<span title="+timesToString(isNull(val))+">"+timesToString(isNull(val))+"</span>";
                }},
				{field:'duration', title:'持续时间', align:'left', width:100,formatter:function(val, rec, rowIndex) {
					return "<span title="+formatSeconds(isNull(val))+">"+formatSeconds(isNull(val))+"</span>";
                }},
				{field:'taskName', title:'所在任务', align:'left', width:100,formatter:function(val, rec, rowIndex) {
					return "<span title="+isNull(val)+">"+isNull(val)+"</span>";
                }},
				{field:'violationStatusCN', title:'是否违规', align:'left', width:100,formatter : function(val, rec, rowIndex) {
                    if(val == "" || val == null ){return "";}
					return "<span title="+isNull(val)+">"+isNull(val)+"</span>";
                }}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	function isNull(v){
		if(v == null || v == ""){
			return "";
		}
		return v;
	}
	
	//时间戳转字符串
	function timesToString(value) {
		if(value == null || value ==''){
			return "";
		}
     	var date = new Date(parseInt(value));
     	var month = date.getMonth() + 1 ;
     	month = month < 10 ? '0' + month : month ;
     	var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
     	var hour =  date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
     	var minutes =  date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
     	var seconds =  date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
     	var tt = [date.getFullYear(), month, day].join('-') + '  ' + [hour,minutes,seconds].join(':');
     	return tt;
	}
	

    //将秒数换成时分秒格式
    function formatSeconds(value) {
    	if(value == null || value ==''){
			return "";
		}
        var secondTime = parseInt(value/1000)// 秒
        var minuteTime = 0;// 分
        var hourTime = 0;// 小时
        if(secondTime > 60) {//如果秒数大于60，将秒数转换成整数
            //获取分钟，除以60取整数，得到整数分钟
            minuteTime = parseInt(secondTime / 60);
            //获取秒数，秒数取佘，得到整数秒数
            secondTime = parseInt(secondTime % 60);
            //如果分钟大于60，将分钟转换成小时
            if(minuteTime > 60) {
                //获取小时，获取分钟除以60，得到整数小时
                hourTime = parseInt(minuteTime / 60);
                //获取小时后取佘的分，获取分钟除以60取佘的分
                minuteTime = parseInt(minuteTime % 60);
            }
        }
        var result = "" + parseInt(secondTime) + "秒";
        if(minuteTime > 0) {
            result = "" + parseInt(minuteTime) + "分" + result;
        }
        if(hourTime > 0) {
            result = "" + parseInt(hourTime) + "小时" + result;
        }
        return result;
    }


	//详情
	function detail(id) {
		var url = "${rc.getContextPath()}/zhsq/event/pointInfo/detail_alarms.jhtml?id=" + id;
		showMaxJqueryWindow('查看${__moduleName!}', url, 800, 450,true);
	}
	
	//查询
	function searchData() {
		$('#list').datagrid('load', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
</script>
</html>
