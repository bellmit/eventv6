<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>反馈信息-<#if listType?? && listType =='1'>我的反馈<#else>辖区所有</#if>列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<#include "/component/standard_common_files-1.1.ftl" />
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/easyui-datagrid-extend.js"></script>
</head>

<body class="easyui-layout">
<div class="MainContent">
	<#include "/zzgl/reportFocus/reportFeedback/toolbar_report_feedback.ftl" />
</div>
	<div id="reportFeedbackContentDiv" region="center" border="false" style="width:100%; overflow:auto; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
    $(function(){
    	loadDataList();
    });
	
    function loadDataList(){
		var queryParams = queryData();
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'fbUUId',
			url:'${rc.getContextPath()}/zhsq/reportFeedback/listData.json',
			columns:[[
				/*{field:'fbUUId', checkbox:true, width:40, hidden:'true'},*/
				{field:'seUUId', title:'下达编号', align:'left',hidden:'true',outMenu:true},
				{field:'bizCode', title:'报告编号', align:'left', width:fixWidth(0.25), formatter: clickFormatter,outMenu:true},
                {field:'gridPathName', title:'所属区域', align:'center', width:fixWidth(0.25),outMenu:true,formatter:showTitle},
                {field:'bizTypeStr', title:'业务类型', align:'center', width:fixWidth(0.1),outMenu:true},
                {field:'bizSign', title:'业务标识', align:'center', width:fixWidth(0.1),formatter:showTitle, hidden:true},
                {field:'dataSourceStr', title:'数据来源', align:'center', width:fixWidth(0.1),formatter:showTitle, hidden:true},
                {field:'dataSign', title:'数据标识', align:'center', width:fixWidth(0.1), hidden:true},
                {field:'seContent', title:'下达内容', align:'center', width:fixWidth(0.25),formatter:showTitle},
                {field:'seTime', title:'下达时间', align:'center', width:fixWidth(0.15),formatter:formatDatebox, hidden:true},
                {field:'seStatusStr', title:'下达状态', align:'center', width:fixWidth(0.1)},
                {field:'reTime', title:'接收时间', align:'center', width:fixWidth(0.15),formatter:formatDatebox},
                {field:'reDeadline', title:'接收时限', align:'center', width:fixWidth(0.15),formatter:formatDatebox,outMenu:true},
                {field:'reStatusStr', title:'接收状态', align:'center', width:fixWidth(0.1)},
                {field:'fbTime', title:'反馈时间', align:'center', width:fixWidth(0.15),formatter:formatDatebox},
                {field:'fbDeadline', title:'反馈时限', align:'center', width:fixWidth(0.15),formatter:formatDatebox,outMenu:true},
                {field:'fbStatusStr', title:'反馈状态', align:'center', width:fixWidth(0.1)},
                {field:'fbOrgName', title:'反馈组织名称', align:'center', width:fixWidth(0.15), hidden:true},
                {field:'fbUserName', title:'反馈用户姓名', align:'center', width:fixWidth(0.1), hidden:true},
                {field:'fbContent', title:'反馈内容', align:'center', width:fixWidth(0.25),formatter:showTitle, hidden:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onDblClickRow:function(index,rec) {
				detail(rec.fbUUId,rec.reStatus == '01');
			},
			onLoadError: function () {//数据加载异常
        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
    		},
			onLoadSuccess: function(data){
				$('#list').datagrid('buildChoosseMenu');
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
	        }   
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});
    }
	
	function clickFormatter(value, rec, rowIndex) {
		var title = "";
		if(value) {
			title += '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.fbUUId + '\',\'' + rec.reStatus + '\'==\'01\')">'+ value +'</a>';
		}
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0, 10);
		}
		return value;
	}

	//Date插件
	Date.prototype.format = function (format) {
		var o = {
			"M+": this.getMonth() + 1, // month
			"d+": this.getDate(), // day
			"h+": this.getHours(), // hour
			"m+": this.getMinutes(), // minute
			"s+": this.getSeconds(), // second
			"q+": Math.floor((this.getMonth() + 3) / 3), // quarter
			"S": this.getMilliseconds()
			// millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}

	function formatDatebox(value,type) {
		if (value == null || value == '') {
			return '';
		}
		var dt;
		if (value instanceof Date) {
			dt = value;
		} else {
			dt = new Date(value);
		}
		var time = "";
		if (type != null && type == 'M' ) {
			time = dt.format("yyyy-MM");
			return "<span  title=\'"+time+"\'>"+time+"</span>";
		}
		time = dt.format("yyyy-MM-dd hh:mm:ss");
		return "<span  title=\'"+time+"\'>"+time+"</span>"; //扩展的Date的format方法(上述插件实现)
	}

	function showTitle(value,row,index){
		if (value == null || value == '') {
			return '';
		}
    	return "<span  title=\'"+value+"\'>"+value+"</span>";
	}

</script>
</body>
</html>