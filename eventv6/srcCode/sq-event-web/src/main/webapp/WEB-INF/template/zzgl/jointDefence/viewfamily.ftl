<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>家庭成员情况</title>

    <#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>
<body class="easyui-layout" style="width:100%">
<input type="hidden" id="familyId" name="familyId"  value="${familyId}" />
<!--------------------------------easyUi列表------------------------------------------>
<div region="center"  border="false" split="true">
	<table id="list" style="width:100%"></table>
</div>

<script type="text/javascript">
$(function() {
         
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
			idField:'msgId',
			url:'${rc.getContextPath()}/zhsq/jointDefence/viewfamilyData.json',
			columns:[[
				{
					field : 'ck',
					checkbox : true
				},
				{
					field : 'holderRelationCN',
					title : '与户主关系',
					width : 60
				},
				{
					field : 'name',
					title : '姓名',
					align : 'center',
					width : 40,formatter:function(value,rec,rowIndex){
						return'<a class="eName" href="###"  onclick="detailRS(' + rec.ciRsId + ')")>'+value+'</a>';
					 }
				},
				{
					field : 'genderCN',
					title : '性别',
					align : 'center',
					width : 30
				},
				{
					field : 'birthday',
					title : '出生日期',
					align : 'center',
					width : 60
				},
				{
					field : 'phone',
					title : '固定电话',
					width : 75
				},
				{
					field : 'residentMobile',
					title : '移动电话',
					width : 75
				}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{'familyId' :$('#familyId').val()},
			onDblClickRow:function(index,rec){
				
			},
			onLoadSuccess:function(data){
			    $("#tempstatus").val("1");
				if(data.total == 0){
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
					
				}
			},
			onLoadError:function(){
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
			}
		});
		
	}
	function detailRS(rsId){
	        var url = '${RS_URL}/62/cirs/viewResident.jhtml?ciRsId='+rsId;
			openJqueryWindowByParams({
            title: "查看成员信息",
            targetUrl: url,
            width:'1000',
            height:'450'
           });
	}
</script>
</body>
</html>
