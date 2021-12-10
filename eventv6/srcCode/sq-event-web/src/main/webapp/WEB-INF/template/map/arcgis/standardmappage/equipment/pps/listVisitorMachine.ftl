<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及案件列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
</style>
</head>
<body class="easyui-layout">
<input type="hidden" id="bizId" name="bizId" value="${bizId!''}" />
<input type="hidden" id="bizName" value="" />

<div class="MainContent">
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <form id="searchForm">
        <input id="bizType" name="bizType" type="text" style="display:none;" value="<#if bizType??>${bizType}</#if>"/>
        <div class="fl">
        	<ul>
                <li>访问姓名：</li>
                <li>
                	<input id="visitor" type="text" class="inp1 InpDisable" style="width:150px;"/>
                </li>
            	<li>被访者：</li>
                <li>
                	<input id="respName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                </li>
            </ul>
        </div>
        </form>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
    	<div class="blind"></div>
    </div>
</div>
</div>

<div id="relatedEventsContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    
    $(function(){
	     loadDataList();
	});
	
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers: true,
			remoteSort: false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'reId',
			url:'${rc.getContextPath()}/zhsq/visitorMachineController/listData.json',
			columns:[[
				{field:'logId',title:'ID', align:'center',hidden:true},//
				{field:'visitor',title:'访客姓名', align:'left',width:fixWidth(0.15),sortable:true,
					formatter:function(value,rec,rowIndex){
						var f = '<a href="###" title='+ rec.visitor +' onclick="showDetailRow(\''+ rec.logId +'\')">'+value+'</a>&nbsp;';
						return f;
					}
				},
				{field:'acqTime',title:'最近来访时间', align:'center',width:fixWidth(0.12), formatter:formartDate},
				{field:'respName',title:'被访者', align:'center',width:fixWidth(0.15),sortable:true},
				{field:'leaveTime',title:'离开时间', align:'center',width:fixWidth(0.12), formatter:formartDate},
				{field:'visitorTel',title:'联系方式', align:'center',width:fixWidth(0.15),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{bizId:$("#bizId").val()},
			onSelect:function(index,rec){
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.logId);
			},
			onLoadSuccess: function(data){
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
    
    function resetCondition() {
    	$("#visitor").val("");
		$("#respName").val("");
		searchData();
    }
    
	function searchData(b) {
		var a = {};
		var visitor = $("#visitor").val();
		var respName = $("#respName").val();
		a["bizId"] = $("#bizId").val();
		if(visitor!=null && visitor!="") {
			a["visitor"]=visitor;
		}
		if(respName!=null && respName!="") {
			a["respName"]=respName;
		}
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	function showDetailRow(logId){
		if(!logId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		    var url = "${rc.getContextPath()}/zhsq/visitorMachineController/detail.jhtml?logId="+logId;
		    window.parent.showMaxJqueryWindow("访客详情", url, 650, 400);
		}
	}
	
	function formartDate(value){
		if(value){
			var date = new Date(value);
			return date.formatString("yyyy-MM-dd hh:mm");
		}else{
			return value;
		}
	}
	
	Date.prototype.formatString = function(fmt){
		var o = {   
			"M+" : this.getMonth()+1,                 //月份   
			"d+" : this.getDate(),                    //日   
			"h+" : this.getHours(),                   //小时   
			"m+" : this.getMinutes(),                 //分   
			"s+" : this.getSeconds(),                 //秒   
			"q+" : Math.floor((this.getMonth()+3)/3), //季度   
			"S"  : this.getMilliseconds()             //毫秒   
		};   
		if(/(y+)/.test(fmt))   
			fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		for(var k in o)   
			if(new RegExp("("+ k +")").test(fmt))   
		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		return fmt;   
	}
	
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />

</body>
</html>