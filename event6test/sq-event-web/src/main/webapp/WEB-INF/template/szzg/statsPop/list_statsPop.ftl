<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${pName}</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
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

<div class="MainContent">
	<#include "/szzg/statsPop/toolbar_statsPop.ftl" />
</div>

<div id="listDiv" region="center" border="false" style="width:100%; overflow:hidden;">
	<table id="list"></table>
</div>

<script type="text/javascript">
    
    $(function(){
	     loadDataList();
	});
	var list = null;
    function loadDataList(){
    	var width = document.body.clientWidth;
    	var w = parseInt(width/3),w2=parseInt(width/9*2);
    	var colums = [
    					{field:'seqId',title:'seqId', align:'center',hidden:true},//
    					{field:'orgName',title:'所属网格', align:'center',width:w,formatter:function(value,rec,rowIndex){
    	                	return "<a title='"+value+"' href='javascript:detail("+rec.seqId+")'><div style='width:"+(w-21)+"px;height:40px;overflow:hidden;'>"+value+"</div></a>";}},
    					{field:'syear',title:'统计年份', align:'center',width:w2},
    					{field:'stypeStr',title:'统计类型', align:'center',width:w2},
    					{field:'snum',title:'统计数量', align:'center',width:w2}];
    	
    	list=$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'seqId',
			url:'${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/listData.json',
			columns:[colums],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams: {orgCode:${orgCode},syear :${currentYear},pcode:'${pcode}'},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
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
    //-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		DivShow(result.tipMsg);
		searchData();
	}
    
</script>

</body>
</html>