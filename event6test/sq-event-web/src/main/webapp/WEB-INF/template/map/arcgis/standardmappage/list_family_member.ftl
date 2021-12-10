<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>家庭成员列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>

<style>

</style>
</head>
<body class="easyui-layout">

	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
</body>
<script type="text/javascript">
    $(function(){
    	loadDataList();//init data
    });
    
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			remoteSort:false,
			striped: true,
			fit: true,
			singleSelect: true,
			idField:'ciRsId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/familyListData.json',
			columns:[[
                {field:'holderRelationCN',title:'与户主关系', align:'center',width:fixWidth(0.2),sortable:true},
                {field:'name',title:'姓名', align:'left',width:fixWidth(0.17),sortable:true},
                {field:'genderCN',title:'性别', align:'center',width:fixWidth(0.25),sortable:true},
                {field:'birthday',title:'出生日期',hidden:true},
				{field:'residenceAddr',title:'现居地址', align:'center',width:fixWidth(0.38),sortable:true}
			]],
// 			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			onLoadSuccess:function(data){
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
				if(data.total==0){
				var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.gif" title="暂无数据"/></div>');
				}
			},
			queryParams:{ciRsId:${ciRsId!''}},
			onSelect:function(index,rec){
				idStr=rec.ciRsId;
			}  
		});
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }
	
</script>
</html>