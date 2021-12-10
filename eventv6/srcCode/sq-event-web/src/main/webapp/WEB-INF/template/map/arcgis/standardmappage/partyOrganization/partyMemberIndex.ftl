<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>党员列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>

<style>

</style>
</head>
<body class="easyui-layout">

	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
<script type="text/javascript">
	var idStr = "";
    var partyGroupId = "${partyGroupId}";
    var orgCode = "${orgCode}";
    var ztreeComboBoxObj = null;
    $(function(){
    	loadDataList();//init data
    });
    
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			remoteSort:false,
			striped: true,
			fit: true,
			//singleSelect: true,
			idField:'ciRsId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/listPartyMemberIndexData.json',
			columns:[[
                {field:'name',title:'名称', align:'left',width:fixWidth(0.1),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		if(value==null)return "";
                		var f = '<a href="###" title='+ rec.name +' onclick="show('+ rec.ciRsId+ ', '+rec.ciPartyId+');">'+value+'</a>&nbsp;';
                		return f;
                	}
                },
                {field:'identityCard',title:'公民身份号码', align:'center',width:fixWidth(0.13),sortable:true},
                {field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.18),sortable:true},
                {field:'genderCN',title:'性别', align:'center',width:fixWidth(0.08),sortable:true},
				{field:'birthday',title:'出生日期', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'partyJoinDate',title:'入党时间', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'partyGroupName',title:'隶属党组织', align:'center',width:fixWidth(0.3),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			onLoadSuccess:function(data){
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
		 
				if(data.total==0){
				var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/frame/images/nodata.gif" title="暂无数据"/></div>');
				
				}
			},
			queryParams:{partyGroupId:partyGroupId,orgCode:orgCode},
			onSelect:function(index,rec){
				idStr=rec.patrolId;
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
    
    function show(ciRsId, ciPartyId){
    	showPopDetail("",ciRsId,ciPartyId)
    }
    

    var isCross;

    function showPopDetail(title,ciRsId,ciPartyId) {
    	if (isCross != undefined) { // 跨域
    		var url = "${POPULATION_URL}/party/viewBaseAndActivity.jhtml?ciRsId="+ciRsId;
    		url = url.replace(/\&/g,"%26");
    		title = encodeURIComponent(encodeURIComponent(title));
    		var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+980+","+370+",'no')";
    		$("#cross_domain_iframe").attr("src",urlDomain);
    	} else {
    		<#if IsNewPartyOrg??>
    		var url = "${POPULATION_URL}/party/viewPartyJY.jhtml?ciPartyId="+ciPartyId;
    		<#else>
    		var url = "${POPULATION_URL}/party/viewBaseAndActivity.jhtml?ciRsId="+ciRsId;
    		</#if>
    		window.parent.showMaxJqueryWindow(title,url,860,370);
    	}
    }
</script>
</html>