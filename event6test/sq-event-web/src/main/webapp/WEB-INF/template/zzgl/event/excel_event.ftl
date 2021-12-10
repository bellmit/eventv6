<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="eventExcelPageTitle">事件列表</@block></title>   
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />

<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
</style>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>

</head>
<body class="easyui-layout" id="layoutArea">
<div class="MainContent">  
<@block name="eventToolbar">
  <#if privateFolderName??>
		<#include "/zzgl/event${privateFolderName}/eventDataGridToolbar.ftl" />
	<#else>
	<#include "/zzgl/event/eventDataGridToolbar.ftl" />
  </#if>
 </@block>
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>

<script type="text/javascript">
	var idStr = "";
    var orgCode = '${orgCode}';
    var infoOrgCode = '${infoOrgCode}';
    var startGridId = "${startGridId?c}";
    var outLinkId = "";//外部链接事件
    var _instanceId = "";
    var _bizPlatform = "";
    var t = "<#if t??>${t}</#if>";
    var winType = "";//用于判断是否关闭详细窗口
    var type = "";
    var happenTimeStart = "";
    var happenTimeEnd = "";
    var urgencyDegrees = new Array();
    
    $(function(){
    	eventInit("${rc.getContextPath()}", "", "${eventType}", "${model}");
    	
    	loadDataList();
    });
	
    function loadDataList(){
		var queryParams = queryData();
		
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
		    	
		    	for(var index in extraParams){	
		    		queryParams[index]=extraParams[index];
		    		if($('#' + index).length == 0) {
		    			$('#eventQueryForm').append('<input type="hidden" id="'+index+'" name="'+index+'" value ="'+extraParams[index]+'" class="queryParam"/>');
		    		}
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
			singleSelect: false,
			idField:'eventId',
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
			<@block name="generalColumns">
			columns:[[
				{field:'eventId',title:'ID', align:'center',hidden:true},//id 
                {
                    field: 'image', title: '<input id=\"imageCheckbox\" type=\"checkbox\"  ><span title=\"导出图片\">导出图片</span>', align:'center', width: 90,
                    formatter: function (value, rec, rowIndex) {
                        return "<input type=\"checkbox\" id=\"image_"+rec.eventId+"\"  name=\"image\"   value=\""+rec.eventId+"\" >";
                    }
                },
				{field:'type',title:'type', align:'center',hidden:true},//   
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
				<#if isJianyin=='1'>
				{field:'bizPlatform',title:'入格事项', align:'center',width:fixWidth(0.08),sortable:true, formatter: enterEventFormatter},
				</#if>
				{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
				<#if eventType != 'draft'>
				{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
				</#if>
				{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
				<@block name="gridPathField">
				{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true, formatter: titleFormatter},
				</@block>
				<#if isJianyin != '1'>
				{field:'curOrgName',title:'承办单位', align:'center',width:fixWidth(0.1),sortable:true},
				</#if>
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
				
				,{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
			]],
			</@block>
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,
			queryParams:queryParams,
			onSelect:function(index,rec){
				idStr=rec.eventId;
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
					$("#eventIdList").val('');
					$('.datagrid-body').eq(1).append('<div class="nodata"></div>');
				} else {
	            	addIcons();
					var panel = $(this).datagrid('getPanel'),
						tr = panel.find('.datagrid-view2 tr.datagrid-row'),//获取展示的列表行
		            	trLen = 0,
		            	trHeight = 0,
		            	dataViewHeight = 0,
		            	trIndexMax = 0;
		            
		            if(tr) {
		            	trLen = tr.length;
		            	trHeight = tr.eq(0).height();
		            } 
		            
	             	dataViewHeight = $('.datagrid-view').height();//获取列表的高度(包括列表标题)
	             	trIndexMax = parseInt(dataViewHeight / trHeight, 10) - 1;//扣除一行，表行索引从0开始
	             	
		            tr.each(function(i){
		            	var operateNotice = $(this).find('.OperateNotice');
		            	
		                $(this).mouseover(function() {  
		                	var trOffset = $(this).offset(),
		                		scrollTop = $(".datagrid-body").scrollTop(),
		                		arrowHeight = $(this).find('.arrow').eq(0).outerHeight(true);
		                	
		                	if(i==(trLen - 1) && i != 0 && (scrollTop > 0 || trIndexMax == i)){//有滚动条或者无滚动条时能展示的最大行数的最后一行
		                		$(this).find('.arrow').css({'top': '28px', 'background': 'url(${rc.getContextPath()}/images/arrow2.png)'});
		                		
		                		operateNotice.css('top', trHeight * (i - 1) + 9 + "px");//增添9个像素是为了更贴合行
		                	} else {
		                		operateNotice.css('top', trHeight * (i + 1) - 2 + "px");//扣除2个像素是为了更贴合行底
		                	}
		                	
		                	operateNotice.css('left', trOffset.left + "px").show();
		                }); 
		                
		                $(this).mouseout(function(){  
		                	operateNotice.hide();
		                }); 
		            }); 
		            var rows = $("#list").datagrid("getRows");  
		            var result = "";
					for(var i=0;i<rows.length;i++){  
					    if(result!=""){
                        	result +="|" ;
                        }
                        result +=  rows[i].eventId;
					} 
					$("#eventIdList").val(result); 
					columnListSet(rows); 
					bindCheckEvent();
				}
	        }   
		});
		
		
		function bindCheckEvent(){
                $("#imageCheckbox").unbind();
                $("input[name='image']").unbind().bind("click", function () {
                    //总记录数
                    var totolrows = $("input[name='image']").length;
                    //选中的记录数
                    var checkrows = $("input[name='image']:checked").length;

                    if (checkrows == totolrows) {
                        $("#imageCheckbox").attr("checked", 'checked');
                    }
                    else {
                        $("#imageCheckbox").removeAttr("checked");
                    }

                    $("#imageList").val("");
                    var items = $("input[name='image']:checked");
                    var result = "";
                    $.each(items, function (index, item) {
                        if(result!=""){
                        	result +="|" ;
                        }
                        result +=  item.value;

                    });
                    $("#imageList").val(result);
				 });
				 //全选
                $("#imageCheckbox").click(function () {
                    if ($(this).attr('checked') == 'checked') {
                        $("input[name='image']").attr("checked", 'checked');
                    } else {
                        $("input[name='image']").removeAttr("checked");
                    }

                    $("#imageList").val("");
                    var items = $("input[name='image']:checked");
                    var result = "";
                    $.each(items, function (index, item) {

                        result = result + "|" + item.value;

                    });
                    $("#imageList").val(result);
                });
                

		
		}
		
		//设置分页控件
	    var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50,100,200],//可以设置每页记录条数的列表
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
		flashData();//点击查询重置第一页刷新，修改删除当前也刷新
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
		$("#list").datagrid('reload');
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
	
	/**
	*reload:默认undefined
	*		false:点击查询重置第一页刷新，
	*		true:修改删除后当前页刷新
	*/
	function flashData(reload){//
		if(winType!="" && winType=='0'){
			closeMaxJqueryWindow();
			winType = "";
		}
		idStr = "";								//清除已操作的数据
		$('#list').datagrid('clearSelections');	//清除选择的行
		if(reload){
			$("#list").datagrid('reload');			//当前页刷新
		}else{
			$("#list").datagrid('load');			//重置到第一页刷新
		}
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
			}else{
				var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
		    	showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), fetchWinHeight(), true);
			}
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
					$.messager.alert('错误','添加关注失败！','error');
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
					$.messager.alert('错误','取消关注失败！','error');
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
	
	function clickFormatter(value, rec, rowIndex) {
		var urgency = rec.urgencyDegree,
			urgencyName = rec.urgencyDegreeName,
			urgencyPic = "",
			handleStatus = rec.handleDateFlag,
			remindStatus = rec.remindStatus,
			isAttention = rec.isAttention,
			wfStatus = rec.wfStatus,
			eventType = "${eventType!}",
			value = value || "";
		
		if(value.length > 20) {
			value = value.substring(0,20);
		}
		
		var attentionVal = '';
		<#if isShowAttentionBtn?? && isShowAttentionBtn>
		if(isAttention) {
			attentionVal = '<li class="guanzhu" onMouseover="showVal(this)" onMouseout="hideVal(this)"  onclick=attentionEvent(this,"'+ rec.isAttention+ '",'+rec.eventId+')>已关注</li>';
		} else {
			attentionVal = '<li class="guanzhu"  onclick=attentionEvent(this,"'+ rec.isAttention + '",'+rec.eventId+')>添加关注</li>';
		}
		</#if>
		
		var handlePic = "";
		<#if eventType != 'draft'>
			if(handleStatus == '2'){
				handlePic = '<i title="将到期" class="ToolBarDue"></i>';
			} else if(handleStatus == '3'){
				handlePic = '<i title="已过期" class="ToolBarOverDue"></i>';
			}
		</#if>
		
		var influencePic = "";
		if(remindStatus == '2' || remindStatus == '3') {
			influencePic = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
		}
		if(rec.influenceDegree == '04'){
			influencePic += "<b class='FontRed'>[重大]</b>";
		}
		
		var tab = '';

		
		if(urgencyName && urgency != '01') {
			urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency"></i>';
		}
		
		var f = tab + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
		return f;
	}
	
	function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0,10);
		}
		
		return value;
	}
	function enterEventFormatter(value, rowData, rowIndex) {
		var isEnterEvent = "否";
		
		if(value && value == '100001') {
			isEnterEvent = "是";
		}
		
		return isEnterEvent;
	}
	<@block name="extarFunction"></@block>
</script>

</body>
</html>