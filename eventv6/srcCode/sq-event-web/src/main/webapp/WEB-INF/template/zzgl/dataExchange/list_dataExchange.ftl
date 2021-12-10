<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及案件列表</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body class="easyui-layout">
<input type="hidden" id="bizId" name="bizId" value="" />
<input type="hidden" id="bizName" value="" />

	<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
        <form id="searchForm">
        <div class="fl">
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
    		
        </div>
    </div>
	
</div>

<div id="ContentDiv" region="center" border="false" style="width:100%; overflow:hidden;">
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
			url:'${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/listRelatedEventsData.json',
			columns:[[
				{field:'reId',title:'ID', align:'center',hidden:true},//
				{field:'reName',title:'案(事)件名称', align:'left',width:fixWidth(0.2),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						var f = '<a href="###" title='+ rec.reName +' onclick="showDetailRow('+ rec.reId +')">'+value+'</a>&nbsp;';
						return f;
					}
				},
				{field:'occuDateStr',title:'发生日期', align:'center',width:fixWidth(0.08),sortable:true,
	                formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}
				 },
				{field:'eventTypeCN',title:'案件类型', align:'center',width:fixWidth(0.08),sortable:true},
				{field:'eventLevelCN',title:'案件分级', align:'center',width:fixWidth(0.08),sortable:true},
				{field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'situation',title:'案件情况', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'occuAddr',title:'发生地详址', align:'center',width:fixWidth(0.2),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:{bizType:'${bizType}',gridCode:gridCode,gridId:gridId},
			onSelect:function(index,rec){
				idStr=rec.reId;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.reId);
			},
			onLoadSuccess: function(data){
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
				var panel = $(this).datagrid('getPanel');  
				var tr = panel.find('div.datagrid-body tr');  
	            tr.each(function(i){
	            	var td = $(this).children('td[field="reName"]');
	                td.children("div").css({
	                	"text-align": "left"  
	                });
	            });  
	            
	            relatedEventsList = $('#list').datagrid("getRows"); 
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
    
	function searchData(b) {
		var a = getQueryData();
		var keyWord = $("#keyWord").val();
		if(keyWord!=null && keyWord!="" && keyWord!="案(事)件名称/案件情况") {
			a["keyWord"]=keyWord;
		} else {
			a["keyWord"]="";
		}
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		flashData();
	}
	
	function flashData(msg, type){
		if(msg!=undefined && msg!=null && msg!=""){
			$.messager.alert('提示',msg,'info');
			if(type!=undefined && type!=null){
				if(type == "1"){
					closeMaxJqueryWindow();
				}else if(type == "2"){
					closeCustomEasyWin();
				}
			}
		}
		idStr = "";								//清除已操作的数据
		$('#list').datagrid('clearSelections');	//清除选择的行
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	function showDetailRow(reId){
		if(!reId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		    var url = "${rc.getContextPath()}/zhsq/relatedEvents/MajorRelatedEventsController/detailRelatedEvents.jhtml?reId="+reId;
		    //showMaxJqueryWindow("查看涉事案件信息", url, 800, 406, false, 'auto');
		    showMaxJqueryWindow("查看涉事案件信息", url, 650, 400);
		}
	}
	
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/customEasyWin.ftl" />

</body>
</html>