<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
.datagrid-btable tr td{position:relative;}/*添加关注等提示相对定位*/
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>

</head>
<body class="easyui-layout">
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>
<#include "/component/customEasyWin.ftl" />
<script type="text/javascript">
	var idStr = "";
    var outLinkId = "";//外部链接事件
    var gridFlag="0";
    var typeVal = "";
    var _instanceId = "";
    var t = "<#if t??>${t}</#if>";
    var winType = "";//用于判断是否关闭详细窗口
    $(function(){
    	loadDataList();
    });
    
    function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto){
            gridFlag="1";
			 $('#gridId').attr('value',gridId);
			$("#gridName").attr("value",gridName);
	}
	
    function loadDataList(){
    	$('#list').datagrid({
			width:600,
			height:300,
			nowrap: false,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventOverviewController/stat_newevent/listData.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//
				{field:'type',title:'type', align:'center',hidden:true},//
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var urgency = rec.urgencyDegree;
                		var urgencyName = rec.urgencyDegreeName;
                		var urgencyPic = "";
                		var handleStatus = rec.handleStatus;
                		
                		var urgency = rec.urgencyDegree;
						var urgencyPic = "";
						
                		if(value==null){
                			value="";
                		}
                		if(value.length>10){
							value = value.substring(0,10);
						}
						
						var attentionVal = '';
                		var remindVal = '';
                		var urgeVal = "";
						var tab = '';
						var handlePic = "";
						
						var influencePic = "";
						
                		var f = tab + influencePic+'<a href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ',"'+ rec.eventFlag+ '",'+rec.instanceId+','+rec.workFlowId+',"'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
						return f;
					},
					
					styler: function(value,rec,index){
						return "text-align:left";
                    }
					
				},
				
				{field:'happenTimeStr',title:'发生时间', align:'center',width:fixWidth(0.1),sortable:true,
                formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>10){
							value = value.substring(0,10);
						}
						return value;
					}},
                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.22),sortable:true}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryData(),
			onSelect:function(index,rec){
				idStr=rec.eventId;
				typeVal=rec.type;
				_instanceId=rec.instanceId;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.eventId, rec.eventFlag, rec.instanceId, rec.workFlowId,rec.type);
			},
			onLoadSuccess: function(data){//事件标题内容左对齐
				if (data.total > 0) {
	            	addIcons();
				} else {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				}
				var panel = $(this).datagrid('getPanel');   
	            var tr = panel.find('div.datagrid-body tr');  
	            var trLen = tr.length; 
	            var index = 20;
	            var pageSize = $('#list').datagrid('getPager').data("pagination").options.pageSize;//获取当前页面大小
	            if(pageSize!=null && pageSize!=""){
	            	index = pageSize;
	            }
	            
	            tr.each(function(i){
	            	var td = $(this).children('td[field="eventName"]');
	            	var ind = trLen - i;   
	            	td.css({"z-index":ind});
	                td.children("div").css({//左对齐
	                	"text-align": "left"  
	                });  
	                $(this).mouseover(function(){  
	                	if(i==(trLen - 1) && i != 0){
	                		td.css({"z-index":2});
	                	}
	                	$(this).find('.OperateNotice').show();
	                }); 
	                $(this).mouseout(function(){  
	                	if(i==(trLen - 1) && i != 0){
	                		td.css({"z-index":1});
	                	}
	                	$(this).find('.OperateNotice').hide();
	                }); 
	                if(i==(trLen - 1) && i != 0 && trLen == (index*2)){
	                	$(this).find('.OperateNotice').css("top","-30px");
	                	$(this).find('.arrow').css("top","30px");
	                	$(this).find('.arrow').css('background','url(../images/arrow2.png)');
	                }
	            });  
				columnListSet($('#list').datagrid("getRows")); 
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
    
    function addIcons(){
    }
    
	function searchData(b, searchArray) {
		var a = queryData(searchArray);
		doSearch(a);
	}
    
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		flashData();
	}
	
	function msgPage(result){
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
	}
	
	function imageViewTool(imageSrc){
	    showImageViewer(imageSrc);
	}
	
	function flashData() {
		if(winType!="" && winType=='0'){
			closeCustomEasyWin();
			winType = "";
		}
		idStr = "";								//清除已操作的数据
		$('#list').datagrid('clearSelections');	//清除选择的行
		$("#list").datagrid('load');			//重新加载事件列表
	}
	
	var wingrid="";
	function callSpeech(phoneNum, reporterName, pictureUrl){
			//var url = "${rc.getContextPath()}/zzgl/event/requiredEvent/emp.jhtml?bCall=" + phoneNum + "&userName="
			var url = "${rc.getContextPath()}/voiceInterface/emp/go.jhtml?bCall=" + phoneNum + "&userName="
					+ encodeURIComponent(encodeURIComponent(reporterName)) + "&userImg=" + encodeURI(pictureUrl);
			wingrid = $.ligerDialog.open({ 
				title:"语音盒呼叫",
				url:url,
				height:250,
				width:800,
				showMax:false,
				showToggle:false,
				showMin:false,
				isResize:false,
				slide:false,
				isDrag:true,
				isunmask:true,
				isMax:false,
				isClosed:false,
				buttons:null
			});
			return wingrid;
	}
	
	function showDetailRow(eventId,eventFlag,instanceId,workFlowId,type){
		if(!eventId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
			var url = "";
			if (eventFlag == "0") {
				url = "${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId=" + eventId;
			} else {
				url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=view&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&cachenum=" + Math.random();
			}
		    showCustomEasyWindow("查看事件信息", url, 900, 400);
		}
	}
	
	function showVal(obj){
		$(obj).text('取消关注');
	}
	
	function hideVal(obj){
		$(obj).text('已关注');
	}
	
	function attentionEvent(obj,isAttention,eventId){
		var attention = $(obj).html();
		var postData = 'eventId='+eventId;
		if(attention=='添加关注'){
			$.ajax({
				type: "POST",
				url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/addAttention.json?t='+Math.random(),
				data: postData,
				dataType:"json",
				success: function(data){
					//alert(data);
					if(data==true){
						$(obj).html('已关注');
						$(obj).addClass('canleAtt');
						$(obj).bind('mouseover', function () {  
							$(this).text('取消关注');
			            }); 
						$(obj).bind('mouseout', function () {  
							$(this).text('已关注');
			            }); 
			            //$.messager.alert('提示','关注成功，已添加到关注列表！','info');
			            DivShow('关注成功，已添加到关注列表！');
					}
				},
				error:function(data){
					alert(data);
				}
			});
		}else if(attention=='取消关注'){
			$.ajax({
				type: "POST",
				url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/canclAttention.json?t='+Math.random(),
				data: postData,
				dataType:"json",
				success: function(data){
					//alert(data);
					if(data==true){
						$(obj).text('添加关注');
						DivShow('取消关注成功！');
						$(obj).unbind('mouseover');
						$(obj).unbind('mouseout');
					}
				},
				error:function(data){
					alert(data);
				}
			});
		}
	}
	
	function remindEvent1(obj,eventId,instanceId){
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddRemind.jhtml?eventId=" + eventId + "&instanceId=" +instanceId;
		showMaxJqueryWindow("督办信息", url, 480, 280);
	}
	
	function urgeEvent(obj,eventId,instanceId){
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddUrge.jhtml?eventId=" + eventId + "&instanceId=" +instanceId;
		showMaxJqueryWindow("催办信息", url, 480, 280);
	}
	
	function remindEvent(obj,instanceId,taskId,eventId,userIds,userNames){
		//alert("eventId="+eventId + "-" + userIds + "-" + userNames);
		return;
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toRemind.jhtml?instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&cachenum=" + Math.random();
		showCustomEasyWindow("查看事件信息", url, 1024);
		var postData = 'instanceId='+instanceId+'&taskId='+taskId+'&remarks=123';
		$.ajax({
				type: "POST",
				url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/addRemind.json?t='+Math.random(),
				data: postData,
				dataType:"json",
				success: function(data){
					alert("success:" + data);
				},
				error:function(data){
					alert("error:"+data);
				}
			});
	}
	
	function gridTreeClickCallback(gridId,gridName,orgId,orgCodeIn,gridInitPhoto) {
		$('#eventRecordContentDiv').panel('setTitle', ('<span class="easui-layout-title">'+gridName+" 事件列表</span>"));
		resetCondition();
		startGridId = gridId;
		orgCode = orgCodeIn;
		gridFlag="1";
		searchData();
	}
	
	function queryData(searchArray){
    	var postData = {};
		if(searchArray!=undefined && searchArray!=null){
			postData = searchArray;
		}
		postData['gridId'] = "<#if gridId??>${gridId}</#if>";
		postData['startTime'] = "<#if startTime??>${startTime}</#if>";
		postData['endTime'] = "<#if endTime??>${endTime}</#if>";
		postData['queryKey'] = "<#if queryKey??>${queryKey}</#if>";
		postData['userId'] = "<#if userId??>${userId}</#if>";
		postData['infoOrgCode'] = "<#if infoOrgCode??>${infoOrgCode}</#if>";
		return postData;
    }
	
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>