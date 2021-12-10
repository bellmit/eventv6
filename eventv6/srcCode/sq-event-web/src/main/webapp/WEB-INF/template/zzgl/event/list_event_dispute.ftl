<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />

<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>

</head>
<body class="easyui-layout" id="layoutArea">
<#include "/component/customEasyWin.ftl" />
<div class="MainContent">
	<#include "/zzgl/event/eventDataGridToolbar.ftl" />
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
$(function(){
     eventInit("${rc.getContextPath()}", "", "${eventType}", "${model!}");
});	
</script>
<script type="text/javascript">
	var idStr = "";
    var orgCode = '${orgCode}';
    var infoOrgCode = '${infoOrgCode}';
    var startGridId = "${startGridId?c}";
    var eventType = '${eventType}';
    var outLinkId = "";//外部链接事件
    var gridFlag="0";
    var typeVal = "";
    var _instanceId = "";
    var _bizPlatform = "";
    var t = "<#if t??>${t}</#if>";
    var winType = "";//用于判断是否关闭详细窗口
    var type = "";
    var happenTimeStart = "";
    var happenTimeEnd = "";
    var urgencyDegrees = new Array();
    
    $(function(){
    	loadDataList();
    	$('#bigType').change(function(){
    		var newValue = $(this).children('option:selected').val();
    		var bigType = newValue;
    		getSmallType(bigType,"");
    	});
    });
    
    function singleMixedGridSelectCallback(gridId,gridName,orgId,orgCode,gridPhoto){
            gridFlag="1";
			$('#gridId').attr('value',gridId);
			$("#gridName").attr("value",gridName);
	}
	
    function loadDataList(){
    	var queryParams = {eventAttrTrigger:'${eventAttrTrigger!}', typesForList:'${typesForList!}', isRemoveTypes:'<#if isRemoveTypes??>${isRemoveTypes?c}<#else>false</#if>', keyWord:'${keyWord!}',eventType:eventType<#if eventType=="view" || eventType=="all" || eventType=="toRemind" || eventType=="history">,infoOrgCode:infoOrgCode</#if>};

	    <#if extraParams??>
	    	<#if extraParams!="">
		        var extraParams = ${extraParams!''};
		    	type = extraParams.type;
		    	if(extraParams.urgencyDegrees != null && extraParams.urgencyDegrees != ""){
		    		urgencyDegrees = extraParams.urgencyDegrees.split(",");
		    		$('#urgencyDegree').combobox('setValues',urgencyDegrees);
		    	}
		    	happenTimeStart = extraParams.happenTimeStart;
		    	happenTimeEnd = extraParams.happenTimeEnd;
		    	
		    	if(type) {
		    		queryParams.type = type;
		    		queryParams.urgencyDegrees = urgencyDegrees;
		    		queryParams.happenTimeStart = happenTimeStart;
		    		queryParams.happenTimeEnd = happenTimeEnd;
		    	}
		    </#if>
		</#if>  
		
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
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//
				{field:'type',title:'type', align:'center',hidden:true},//
				/*{field:'urgencyDegree',title:'', align:'center',width:40,
					formatter:function(value,rec,rowIndex){
						var urgency = rec.urgencyDegree;
						var urgencyPic = "";
						if(urgency!=null && urgency!="" && urgency!='01'){
							urgencyPic = '<img title="'+ rec.urgencyDegreeName +'" src="${rc.getContextPath()}/images/icon_05.png">';
						}
						<#if eventType=="todo">
							if(rec.remindStatus=='1' || rec.remindStatus=='3'){
								urgencyPic += '<img title="催办" src="${rc.getContextPath()}/images/icon_14.png">';
							}
						</#if>
						var f = urgencyPic;
						return f;
					}
				},*/
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.3),sortable:true,
                	formatter:function(value,rec,rowIndex){
                		var urgency = rec.urgencyDegree;
                		var urgencyName = rec.urgencyDegreeName;
                		var urgencyPic = "";
                		var handleStatus = rec.handleDateFlag;
                		
                		var urgency = rec.urgencyDegree;
						var urgencyPic = "";
						<#if eventType=="todo">
							if(rec.remindStatus=='1' || rec.remindStatus=='3'){
								urgencyPic += '<i title="催办" class="ToolBarRemind"></i>';
							}
						</#if>
						if(urgencyName!=null && urgency!='01'){
							urgencyPic += '<i title="'+ rec.urgencyDegreeName +'" class="ToolBarUrgency"></i>';
						}
						
                		if(value==null){
                			value="";
                		}
                		if(value.length>20){
							value = value.substring(0,20);
						}
						
						var attentionVal = '';
                		var remindVal = '';
                		var urgeVal = "";
                		if(rec.isAttention){
							attentionVal = '<li class="guanzhu" onMouseover="showVal(this)" onMouseout="hideVal(this)"  onclick=attentionEvent(this,"'+ rec.isAttention+ '",'+rec.eventId+')>已关注</li>';
						}else{
							attentionVal = '<li class="guanzhu"  onclick=attentionEvent(this,"'+ rec.isAttention + '",'+rec.eventId+')>添加关注</li>';
						}
						var userIds = rec.userIds;
						if(rec.wfStatus=='1'){
							remindVal = '<li class="line"></li><li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
							urgeVal = '<li class="cuiban" onclick=urgeEvent(this,'+rec.eventId+','+rec.instanceId+')>催办</li>';
						}
						var tab = '';
						<#if eventType=="all" || eventType=="toRemind">
							tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
						<#elseif eventType=="my">
							if(urgeVal != ""){
								tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+urgeVal+'</ul><div class="arrow"></div></div></div>';
							}
						</#if>
						
						var handlePic = "";
						<#if eventType != 'draft'>
							if(handleStatus == '2'){
								handlePic = '<i title="将到期" class="ToolBarDue"></i>';
							}
							if(handleStatus == '3'){
								handlePic = '<i title="已过期" class="ToolBarOverDue"></i>';
							}
						</#if>
						<#if false && (eventType=='history' || eventType=="all") >
							var attrFlag = rec.attrFlag;
							if(attrFlag.indexOf('1,') != -1){
								handlePic += '<img title="图片" src="${rc.getContextPath()}/images/attr_flag_pic.png">';
							}
							if(attrFlag.indexOf('2,') != -1){
								handlePic += '<img title="音频" src="${rc.getContextPath()}/images/attr_flag_audio.png">';
							}
							if(attrFlag.indexOf('3,') != -1){
								handlePic += '<img title="视频" src="${rc.getContextPath()}/images/attr_flag_video.png">';
							}
						</#if>
						
						var influencePic = "";
						if(rec.remindStatus=='2' || rec.remindStatus=='3'){
							influencePic = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
						}
						if(rec.influenceDegree == '04'){
							influencePic += "<b class='FontRed'>[重大]</b>";
						}
						
                		var f = tab + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
						return f;
					},
					
					styler: function(value,rec,index){
						return "text-align:left";
                    }
					
				},
				
				{field:'happenTimeStr',title:'发生时间', align:'center',width:fixWidth(0.1),sortable:true,
                	formatter:function(value,rowData,rowIndex){
						if(value!=null && value.length>=10){
							value = value.substring(0,10);
						}
						return value;
					}},
                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
				{field:'gridName',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true},
				 <#if eventType=="not">
				 	{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true},
					{field:'taskStatusName',title:'任务状态', align:'center',width:fixWidth(0.1),sortable:true,hidden:true,
						styler: function(value,rec,index){
							return "color:#b3d465";
	                    }
					 }
				<#else>
					{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true}
				</#if>
				<#if eventType=="todo">
					,{field:'taskReceivedStaus',title:'是否接收', align:'center',width:fixWidth(0.12),sortable:true,
						formatter:function(value,rec,rowIndex) {
							var valName = "未接收";
							if(value && value == "1") {
								valName = "已接收";
							}
							
							return valName;
						}
					}
				</#if>
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onSelect:function(index,rec){
				idStr=rec.eventId;
				typeVal=rec.type;
				_instanceId=rec.instanceId;
				_bizPlatform = rec.bizPlatform;
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.eventId, rec.instanceId, rec.workFlowId,rec.type);
			},
			onLoadError: function () {//数据加载异常
        		$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
    		},
			onLoadSuccess: function(data){//事件标题内容左对齐
				if(data.total == 0) {
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				} else {
					var panel = $(this).datagrid('getPanel');
		            var tr = panel.find('.datagrid-view2 tr.datagrid-row');//获取展示的列表行
		            var trLen = 0; 
		            var trHeight = 0;
		            if(tr) {
		            	trLen = tr.length;
		            	trHeight = tr.eq(0).height();
		            } 
		            var index = data.total;
		            var pageSize = $('#list').datagrid('getPager').data("pagination").options.pageSize;//获取当前页面大小
		            if(pageSize && pageSize!="" && data.total > pageSize){
		            	index = pageSize;
		            }
	             	var dataViewHeight = $('.datagrid-view').height();//获取列表的高度(包括列表标题)
	             	var trIndexMax = parseInt(dataViewHeight / trHeight, 10) - 1 - 1;//扣除一行标题行，表行索引从0开始
	             	
		            tr.each(function(i){
		            	var td = $(this).children('td[field="eventName"]');
		                td.children("div").css({//左对齐
		                	"text-align": "left"  
		                });  
		                
		                $(this).mouseover(function(){  
		                	var trOffset = $(this).offset();
		                	var scrollTop = $(".datagrid-body").scrollTop();
		                	
		                	if(i==(trLen - 1) && i != 0 && (scrollTop > 0 || trIndexMax == i)){//有滚动条或者无滚动条时能展示的最大行数的最后一行
			                	$(this).find('.OperateNotice').css("top",trOffset.top - 158 + scrollTop+ "px");
		                		$(this).find('.OperateNotice').css("left",trOffset.left + "px");
			                	$(this).find('.arrow').css("top","28px");
			                	$(this).find('.arrow').css('background','url('+rootPath+'/images/arrow2.png)');
			                	$(this).find('.OperateNotice').show();
		                	} else {
		                		$(this).find('.OperateNotice').css("left",trOffset.left + "px");
			                	$(this).find('.OperateNotice').css("top",trOffset.top - 87 + scrollTop+ "px");
			                	$(this).find('.OperateNotice').show();
		                	}
		                }); 
		                $(this).mouseout(function(){  
		                	$(this).find('.OperateNotice').hide();
		                }); 
		                /*if(i==(trLen - 1) && i != 0 && trLen == (index*2)){
		                	$(this).find('.OperateNotice').css("top","-30px");
		                	$(this).find('.arrow').css("top","30px");
		                	$(this).find('.arrow').css('background','url(../images/arrow2.png)');
		                }*/
		            });  
					columnListSet($('#list').datagrid("getRows")); 
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
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
		});
    }
    
    function addIcons(){
    	var iconDivLength = $("#iconDiv").length;
    	if(iconDivLength == 0){
	    	var icons = 
	    	'<div id="iconDiv" class="fl">'+
	    		<#if eventType??>
	    			<#if eventType != 'draft'>
	    				'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="allSearchData(this);"><i class="ToolBarAll"></i>所有</a>'+
	    			</#if>
	    			<#if eventType == 'todo'>
	    				'<a href="###" onclick="remindSearchData(1, this);"><i class="ToolBarRemind"></i>催办</a>'+
	    			</#if>
	    			<#if eventType != 'draft'>
		    			'<a href="###" onclick="urgencySearchData(this);"><i class="ToolBarUrgency"></i>紧急</a>'+
				    	'<a href="###" onclick="handleSearchData(\'2\', this);"><i class="ToolBarDue"></i>将到期</a>'+
				    	'<a href="###" onclick="handleSearchData(\'3\', this);"><i class="ToolBarOverDue"></i>已过期</a>'+
				    	'<a href="###" onclick="handleSearchData(\'1\', this);"><i class="ToolBarNormal"></i>正常</a>'+
			    	</#if>
	    		</#if>
	    	'</div>';
	    	$('.ToolBar').append(icons);
    	}
    }
    
	function searchData(b, searchArray) {
		var a = queryData(searchArray);
		a["typesForList"] = "${typesForList!}";
		a["eventAttrTrigger"] = "${eventAttrTrigger!}";
		a["isRemoveTypes"] = "<#if isRemoveTypes??>${isRemoveTypes?c}</#if>";
		
		doSearch(a);
	}
    
	function checkOutLink() {
		var rows = $('#list').datagrid('getSelections');
		if(rows[0].outLinkId > 0) {
			$.messager.alert('提示','该条事件来自通用办公，请到通用办公功能中进行操作！','info');
			return true;
		}
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		flashData();
	}
	
	function reloadDataForDistribute(result,type){
		if(typeof(type)!='undefined' && type == 'closed'){
			closeMaxJqueryWindow();
			$.messager.alert('提示', result, 'info');
		}else if(typeof(type)!='undefined' && type == 'close'){
			closeMaxJqueryWindow();
		}
		else{
			closeCustomEasyWin();
		}
		
		idStr = "";//清除已操作的数据
		$("#list").datagrid('load');
	}
	
	function reloadDataForCancleWin() {
		closeMaxJqueryWindow();
		idStr = "";//清除已操作的数据
		$("#list").datagrid('load');
	}
	
	function msgPage(result){
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
	}
	function imageViewTool(imageSrc){
	    showImageViewer(imageSrc);
	}
	
	function showOutLinkInfo(outLinkId,type) {
		if(type == '0203' || type == '0213') {
			type = '41';
		}else if(type == '0211') {
			type = '42'
		}else if(type == '0212') {
			type = '43'
		}else if(type == '0210') {
			type = '50'
		}else {
			$.messager.alert('错误','外部事件引用出错！','error');
			return false;
		}
		var url = '${rc.getContextPath()}/admin/workflow/show/'+outLinkId+'.jhtml?eventId='+type+'&type=3' ;
		showMaxJqueryWindow("通用办公事件详情", url);
	}
	
	function flashData(){
		if(winType!="" && winType=='0'){
			closeMaxJqueryWindow();
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
	
	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
		if(!eventId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
			if(bizPlatform == "001"){
				var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
		    	showMaxJqueryWindow("查看事件信息", url);
				/*$.ajax({
					type: "POST",
					url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/getOldEventId.json?eventId='+eventId+'&t='+Math.random(),
					data: '',
					dataType:"json",
					success: function(data){
						if(data != null && data.itemInstanceId != null && data.itemInstanceId != ''){
							var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+data.itemInstanceId;
		    				showMaxJqueryWindow("查看事件信息", url);
						}else{
							alert('关联数据错误');
						}
					}
				});*/
			}else{
				//workflowTask(eventId);
				var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model!}&cachenum=" + Math.random();
		    	showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
			}
		}
	}
	
	function workflowTask(eventId){
		var taskName = "测试环节";
		var taskAdvice = "测试环节，测试任务测试环节";
		var urlT = '${rc.getContextPath()}/zhsq/event/eventDisposalController/workflowTask.jhtml?eventId='+eventId+'&taskAdvice='+encodeURIComponent(taskAdvice)+'&taskName='+encodeURIComponent(taskName);
		
		$.ajax({
			type: "POST",
    		url : urlT,
			data: '',
			dataType:"json",
			success: function(data){
				if(data.result){
		   			$.messager.alert('提示','事件任务新增成功！','info');
		   		}else{
		   			$.messager.alert('提示','事件任务新增失败！','info');
		   		}
			},
			error:function(data){
				$.messager.alert('错误','连接错误！','error');
			}
    	});
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
						$(obj).unbind('mouseover');
						$(obj).unbind('mouseout');
						//修复督办操作后，取消关注成功后，仍显示“已关注”的问题
						$(obj).removeAttr("onmouseover");
						$(obj).removeAttr("onmouseout");
						
						$(obj).text('添加关注');
						DivShow('取消关注成功！');
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
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toRemind.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&taskId="+taskId+"&model=${model!}&cachenum=" + Math.random();
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
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>