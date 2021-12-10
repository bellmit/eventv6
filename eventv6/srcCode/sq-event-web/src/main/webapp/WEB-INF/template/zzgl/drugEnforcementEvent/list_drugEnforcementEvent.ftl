<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>禁毒事件</title>
		<#include "/component/commonFiles-1.1.ftl" />
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	
	</head>
	<body class="easyui-layout">
		<div class="MainContent">
			<#include "/zzgl/drugEnforcementEvent/toolbar_drugEnforcementEvent.ftl" />
		</div>      
		
		<div region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
			<table style="width:100%" id="list"></table>
		</div>
			
		<script type="text/javascript">
			$(function(){
				loadDataList();
			});
		
			function loadDataList() {
				var queryParams = fetchQueryParams();
				
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
					idField:'drugEnforcementId',
					url:'${rc.getContextPath()}/zhsq/drugEnforcementEvent/listData.json',
					columns:[[
						{field:'drugEnforcementId',checkbox:true,width:40,hidden:'true'},
						{field:'addictName',title:'姓名', align:'left',width:fixWidth(0.1),sortable:true, formatter: clickFormatter},
						{field:'addictIdCard',title:'公民身份号码', align:'center',width:fixWidth(0.12),sortable:true},
						{field:'addictGridPath',title:'场所', align:'center',width:fixWidth(0.12),sortable:true, formatter: titleFormatter},
						{field:'isRestoreFamilyRelationship',title:'家庭关系修复', align:'center',width:fixWidth(0.1),sortable:true, formatter: restoreFamilyRelationshipFormatter},
						{field:'isSocialAssistance',title:'社会救助', align:'center',width:fixWidth(0.08),sortable:true, formatter: socialAssistanceFormatter},
						{field:'isEmploymentService',title:'就业服务', align:'center',width:fixWidth(0.08),sortable:true, formatter: employmentServiceFormatter},
						{field:'drugSocialSituationName',title:'社会毒情', align:'center',width:fixWidth(0.1),sortable:true},
						{field:'reportDateStr',title:'上报时间', align:'center',width:fixWidth(0.12),sortable:true},
						{field:'contactUser',title:'联系人员', align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
						{field:'status',title:'当前状态', align:'center',width:fixWidth(0.06),sortable:true, formatter: statusFormatter}
					]],
					toolbar:'#jqueryToolbar',
					pagination:true,
					pageSize: 20,
					queryParams: queryParams,
					onLoadSuccess:function(data){
						if(data.total == 0){
							$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
						}
					},
					onDblClickRow:function(index,rec){
						detail(rec.drugEnforcementId);
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
			
			function clickFormatter(value, rec, rowIndex) {
				var title = "";
				
				if(value) {
					title = '<a href="###" title="'+ value +'" onclick="detail(\'' + rec.drugEnforcementId + '\')">'+ value +'</a>';
				}
				
				return title;
			}
			
			function titleFormatter(value, rec, rowIndex) {
				var title = "";
				
				if(value) {
					title = '<span title="'+ value +'">'+ value +'</span>';
				}
				
				return title;
			}
			
			function restoreFamilyRelationshipFormatter(value, rec, rowIndex) {
				return valueFormatter('01', rec, rowIndex);
			}
			
			function socialAssistanceFormatter(value, rec, rowIndex) {
				return valueFormatter('02', rec, rowIndex);
			}
			
			function employmentServiceFormatter(value, rec, rowIndex) {
				return valueFormatter('03', rec, rowIndex);
			}
			
			function valueFormatter(value, rec, rowIndex) {
				var content = rec.content,
					val = "不需要";
				
				if(content && value) {
					if(content.indexOf(',' + value + ',') >= 0) {
						val = "需要";
					}
				}
				
				return val;
			}
			
			function statusFormatter(value, rec, rowIndex) {
				var val = "";
				
				if(value) {
					var statusObj = {};
					
					<#if statusMap??>
						<#list statusMap?keys as status>
							statusObj["${status}"] = "${statusMap[status]}";
						</#list>
					</#if>
				
					val = statusObj[value];
				}
				
				return val;
			}
			
			function reloadDataForSubPage(result, isCurrent){
				closeMaxJqueryWindow();
				
				if(result) {
					DivShow(result);
				}
				
				searchData(isCurrent);
			}
			
		</script>
	</body>
</html>