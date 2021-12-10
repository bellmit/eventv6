<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡防列表</title>
<#include "/component/commonFiles-1.1.ftl" />

<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>

<style>

</style>
</head>
<body class="easyui-layout">

	<div id="jqueryToolbar">
	<div class="ConSearch">
        <div class="fl">
        	<ul>
        		<li>所属网格：</li>
                <li>
                	<input id="gridId" name="gridId" type="hidden" value="${startGridId?c}"/>
                	<input id="bizType" name="bizType" type="hidden"/>
                	<input id="gridCode" name="gridCode" type="hidden"/>
                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;" value="<#if startGridName??>${startGridName}</#if>"/></li>
            	<li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="事件名称/巡防、处置情况/巡防区域" style="font-size:12px;font-style:italic;color:gray; width:210px;" onfocus="if(this.value=='事件名称/巡防、处置情况/巡防区域'){this.value='';}$(this).attr('style','width:210px;');" onblur="if(this.value==''){$(this).attr('style','font-size:12px;font-style:italic;color:gray;width:210px;');this.value='事件名称/巡防、处置情况/巡防区域';}"/></li>
            	<li>
            	</li>
            </ul>
        </div>
        <div class="btns">
        	<ul>            	
            	<li><a href="#" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(1)">查询</a></li>
            	<li><a href="#" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>‍
        
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBar">
		<div class="blind"></div>
    	<script type="text/javascript">
			function DivHide(){
				$(".blind").slideUp();//窗帘效果展开
			}
			function DivShow(msg){
				$(".blind").html(msg);
				$(".blind").slideDown();//窗帘效果展开
				setTimeout("this.DivHide()",800);
			}
			
			
		</script>	
        <div class="tool fr">
        	<@ffcs.right rightCode="report" parentCode="${system_privilege_action?default('')}">
        		<a href="#" class="NorToolBtn ShangBaoBtn" onclick="report();">上报</a>
        	</@ffcs.right>
           	<@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
       			<a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
			</@ffcs.right>
			<@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
			</@ffcs.right>
			<@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
				<a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
			</@ffcs.right>
        </div>
    </div>
	

</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
		<table id="list"></table>
	</div>
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
<script type="text/javascript">

//-- 供子页调用的重新载入数据方法
function reloadDataForSubPage(result){
	closeMaxJqueryWindow();
	$.messager.alert('提示', result, 'info');
	$("#list").datagrid('load');
}
</script>
<script type="text/javascript">
	var idStr = "";
    var startGridId = "${startGridId?c}";
    var startGridName = "${startGridName}";
    var bizType = "${bizType}";
    var ztreeComboBoxObj = null;
    $(function(){
    	loadDataList();//init data
    	
    	ztreeComboBoxObj = AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#gridId").val(grid.gridId);
				$("#gridCode").val(grid.gridCode);
			}
		});
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
			idField:'patrolId',
			url:'${rc.getContextPath()}/zhsq/patrolController/listData.json',
			frozenColumns:[[
                {field:'ck',checkbox:true},
				{field:'patrolId',title:'ID', align:'center',hidden:true}
			]],
			columns:[[
                {field:'name',title:'巡防事件名称', align:'left',width:fixWidth(0.17),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		if(value==null)return "";
						if(value!=null && value.length>15){
							value = value.substring(0,14) ;
						}
                		var f = '<a href="###" title='+ rec.name +' onclick=show('+ rec.patrolId+ ')>'+value+'</a>&nbsp;';
                		return f;
                	}
                },
                {field:'content',title:'巡防总体情况', align:'center',width:fixWidth(0.2),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		if(value==null)return "";
						if(value!=null && value.length>18){
							value = value.substring(0,17) ;
						}
						var tag='';
						tag = '<span title="'+rec.content+'">'+value+'</span>';
						return tag;
                	}
                },
                {field:'startPatrolTimeStr',title:'巡防时间', align:'center',width:fixWidth(0.25),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var startTime = value != null?value:'无';
                		var endTime = rec.endPatrolTimeStr != null?rec.endPatrolTimeStr:'无';
                		var f = startTime + ' - ' + endTime;
                		//var f = '<a href="###" title='+ rec.name +' onclick=show('+ rec.patrolId+ ')>'+value+'</a>&nbsp;';
                		return f;
                	}
                },
                {field:'occurred',title:'巡防区域',hidden:true},
				{field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'principal',title:'带队负责人', align:'center',width:fixWidth(0.15),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		if(value==null)return "";
						if(value!=null && value.length>18){
							value = value.substring(0,17) ;
						}
						var tag='';
						tag = '<span title="'+rec.principal+'">'+value+'</span>';
						return tag;
                	}},
				{field:'status',title:'状态', align:'center',width:fixWidth(0.1),sortable:true,
					formatter:function(value,rec,rowIndex){
						var f;
						if(value == '0'){
							f = '草稿';
						}else if(value == '1'){
							f = '已上报';
						}else if(value == '2'){
							f = '结案';
						}
                		return f;
                	}
				}
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
			queryParams:{bizType:bizType,gridId:$("#gridId").val()},
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
    
	function edit() {
		var rows = $('#list').datagrid('getSelections');
		var num=rows.length;
		if(num!=1){
		   $.messager.alert('提示','请选中要编辑的数据再执行此操作!','warning');
		   return;
		}else{
			var status=rows[0].status;
			if(status!=null && status!=""){
				if(status=='0'){
					var id=rows[0].patrolId;
					var url = '${rc.getContextPath()}/zhsq/patrolController/toEdit.jhtml?patrolId='+id;
					showMaxJqueryWindow("编辑巡防信息", url, 600, fixHeight(0.9));
					$('#list').datagrid('unselectAll');	
				}else{
					$.messager.alert('提示','已上报或结案记录不可编辑','info');
					$('#list').datagrid('unselectAll');	
				}
			}
		}
		
	}
	
	function add(){
		var url = '${rc.getContextPath()}/zhsq/patrolController/toAdd.jhtml?bizType='+bizType;
		showMaxJqueryWindow("新增巡防信息", url, 600, fixHeight(0.9));
	}
	
	function del() {
		var ids = [];
		var statusArry = [];
		var rows = $('#list').datagrid('getSelections');
		for(var i = 0; i<rows.length; i++){
			ids.push(rows[i].patrolId);
			statusArry.push(rows[i].status);
		}
		var idStr = ids.join(',');
		if(idStr == null || idStr == "") {
			$.messager.alert('提示','请选中要删除的数据再执行此操作!','warning');
			return false;
		}
		var guidang = 0;
		var shangbao = 0;
		for(var j = 0; j<statusArry.length; j++){
			if(statusArry[j]=='2'){
				guidang = guidang + 1;
			}else if(statusArry[j]=='1'){
				shangbao = shangbao + 1;
			}
		}
		if(guidang==0 && shangbao ==0 ){
			$.messager.confirm('提示', '您确定删除选中的信息吗？', function(r){
				if (r){
					modleopen();
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/patrolController/del.jhtml',
						data: 'idStr='+idStr,
						dataType:"json",
						success: function(data){
							modleclose();
							$.messager.alert('提示', '您成功删除'+data.result+'条信息！', 'info');
							$("#list").datagrid('load');
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','已上报或结案记录不能删除！','info');
			return;
		}
	}
	
	//function onGridTreeSelected(gridId, items){alert(1);
	//	if(items!=undefined && items!=null && items.length>0){
	//		var grid = items[0];alert(grid.orgCode);
	//		$("#gridCode").val(grid.orgCode);
	//	} 
	//}
    
	function show(recordId){
		if(!recordId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		    var url = "${rc.getContextPath()}/zhsq/patrolController/show.jhtml?patrolId="+recordId+"&json="+Math.random();
		    showMaxJqueryWindow("查看巡防信息", url, 600, fixHeight(0.85));
		}
	}
	
	//上报
	var reportPatrolId = 0;
	function report(){
		var rows = $('#list').datagrid('getSelections');
		var num=rows.length;
		if(num!=1){
		    $.messager.alert('提示','请选择一条记录','info');
		}else{
			var status = rows[0].status;
			if(status!="0"){
				$.messager.alert('提示','已上报或结案记录不能进行上报操作','info');
				$('#list').datagrid('unselectAll');	
			}else{
				var id=rows[0].patrolId;
				reportPatrolId = id;
				var eventName = rows[0].name;
				var occurred=rows[0].occurred;
				var happenDateStr = rows[0].startPatrolTimeStr;
				var content = ""+rows[0].content;
				var gridId = rows[0].gridId;
				var gridCode = rows[0].gridCode;
				var gridName = rows[0].gridName;
				var event = {
					"eventName" : eventName,
					"happenTimeStr" : happenDateStr,
					"occurred" : occurred,
					"content" : content,
					"gridId" : gridId,
					"gridCode" : gridCode,
					"gridName" : gridName,
					<#if patrolEventType??>
						"type" : '${patrolEventType}',
						"typesForList" : '${patrolEventType}',
					</#if>
					"status" : '',
					"callBack" : 'parent.reportCallback',
					"eventReportRecordInfo":{
						'bizId' : reportPatrolId
					}
				};
				
				var event = JSON.stringify(event); 
				var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml?eventJson='+encodeURIComponent(event);
				showMaxJqueryWindow("上报巡防信息", url, 850, 390,'no');
			}
		}
	}
	
	//上报回调函数
	function reportCallback(date){
		if(date.instanceId != null && date.instanceId != ""){
			$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/patrolController/report.jhtml',
			data: 'patrolId='+reportPatrolId+'&&status=1&eventId='+date.eventId,
			success: function(data){
				closeMaxJqueryWindow();
			    $.messager.alert('提示','上报成功','info');
				$("#list").datagrid('load');
				$('#list').datagrid('unselectAll');
			},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
		});
		}else{
			$.messager.alert('提示','上报失败，请联系系统管理员！','error');
		}
	}
	
	
</script>
<script type="text/javascript">
    
	var idStr = "";
    var startGridId = "${startGridId?c}";
    
	function searchData(b) {
		var a = new Array();
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="事件名称/巡防、处置情况/巡防区域") a["keyWord"]=keyWord;
		var gridId = $("#gridId").val();
		if(gridId!=null && gridId!=""){
			a["gridId"]=gridId;
		}
		var gridCode = $("#gridCode").val();
		if(gridCode!=null && gridCode!=""){a['gridCode']=gridCode;}
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');
	}
	
	function resetCondition() {
		$("#keyWord").val("事件名称/巡防、处置情况/巡防区域");
		$("#keyWord").attr('style','font-size:12px;font-style:italic;color:gray; width:210px;');
		$('#gridName').val("${startGridName}");
		$('#gridId').val("${startGridId?c}");
		searchData();
	}
	
	
</script>
</html>