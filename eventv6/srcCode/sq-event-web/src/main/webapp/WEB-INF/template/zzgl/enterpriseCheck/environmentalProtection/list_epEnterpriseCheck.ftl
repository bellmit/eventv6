<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>企业巡查信息列表</title>
	<#include "/component/commonFiles-1.1.ftl" />
	
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout">

<div class="MainContent">
	<#include "/zzgl/enterpriseCheck/environmentalProtection/toolbar_epEnterpriseCheck.ftl" />
</div>

<div id="inspectionDivInfoDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
	
	$(function(){
		loadDataList();
	});
	
	function loadDataList() {
		$('#list').datagrid({
			width:600,
			height:600,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,//让列宽自适应窗口宽度
			singleSelect: true,
			idField:'birId',
			url:'${rc.getContextPath()}/zhsq/enterpriseCheck/listEPCheckData.json',
			columns:[[
				{field:'birId',checkbox:true,width:40,hidden:'true'},
				{field:'inspectSeq',title:'检查编号',align:'left',width:fixWidth(0.1),sortable:true,
					formatter:function(value,rec,rowIndex) {
						if(!value) {
	                		value="";
	                	}
	                	return '<a href="###" title="'+ value +'" style="text-decoration:none;" onclick="detail('+rec.birId+')">'+value+'</a>'
	                }
				},
				{field:'inspectTypeName',title:'检查类型',align:'center',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex) {
						var inspectResultFlag = rec.inspectResultFlag,
							formatVal = "";
						
						if(inspectResultFlag && inspectResultFlag == '1' ) {
							formatVal = '<i title="发现污染单位" class="HasPollution" style="padding-right: 5px;"></i>';
						}
						if(!value) {
							value = "";
						}
	                	
	                	return formatVal + value;
	                }
				},
                {field:'gridName',title:'所属网格',align:'center',width:fixWidth(0.15),sortable:true},
                {field:'startTimeStr',title:'检查时间',align:'center',width:fixWidth(0.1),sortable:true,
                	formatter:function(value,rec,rowIndex) {
                		if(value && value.length > 10) {
                			value = value.substr(0, 10);
                		}
                		
                		return value;
                	}
                },
                {field:'content',title:'检查内容',align:'center',width:fixWidth(0.3),sortable:true},
                {field:'isDel',title:'状态',align:'center',width:fixWidth(0.1),sortable:true, 
                	formatter:function(value,rec,rowIndex) {
                		var valName = {'1': '归档', '2': '草稿', '3': '已上报'};
                		
                		return valName[value];
                	}
                }
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{gridCode: '${infoOrgCode!}'},
			onSelect: function(index,rec) {
				authority(rec);
			},
			onDblClickRow:function(index,rec){
				detail(rec.birId);
			},
			onLoadSuccess:function(data){
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
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
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result, isCurrent){
		try{
			closeMaxJqueryWindow();
		}catch(e){}
		DivShow(result);
		searchData(isCurrent);
	}
</script>
</body>
</html>