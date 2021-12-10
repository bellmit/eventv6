<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><@block name="eventListPageTitle">事件列表</@block></title>
    <#include "/component/standard_common_files-1.1.ftl" />
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />

    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
        .datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
        .NorToolBtn{width:auto}
    </style>
    <script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
    <script type="text/javascript" src="${COMPONENTS_URL}/js/rs/easyui-datagrid-extend.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/jscolor/jscolor.js"></script>
</head>
<body class="easyui-layout" id="layoutArea">
<#include "/component/customEasyWin.ftl" />
<div class="MainContent">
	<@block name="eventToolbar">
		<#include "/zzgl/event/eventDataGridToolbar.ftl" />
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
    
    $(function() {
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
            
            if(typeof adjustListDisplayElement === 'function') {
            	adjustListDisplayElement(extraParams);
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
            <@block name="singleSelect">singleSelect: true,</@block>
            idField:'eventId',
            url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
            <@block name="generalColumns">
            columns:[[
                {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true,outMenu:true},
               	{field:'type',title:'type', align:'center',hidden:true,outMenu:true},

				<@block name="checkbox"></@block>

                {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortName:'T1.EVENT_NAME',sortable:true,formatter: clickFormatter,outMenu:true},
                {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.15),sortName:'T1.HAPPEN_TIME',sortable:true},
                <#if eventType != 'draft'>
                	<@block name="handleDateStrField">
                		{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.15),sortName:'T1.HANDLE_DATE',sortable:true},
                	</@block>
                </#if>
                <@block name="eventClassField">
                {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18),sortName:'T1.TYPE_',sortable:true, formatter: titleFormatter},
                </@block>
                <@block name="gridPathField">
                {field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.18), formatter: titleFormatter},
                </@block>

			<@block name="extraListColumn"></@block>

			<#if eventType=="not">
                {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.08),sortable:true},
                {field:'taskStatusName',title:'任务状态', align:'center',width:fixWidth(0.1),hidden:true,
                    styler: function(value,rec,index){
                        return "color:#b3d465";
                    }
                }
			<#else>
                {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.1), formatter: titleFormatter}
			</#if>
			<#if eventType=="todo">
                ,{field:'taskReceivedStaus',title:'是否接收', align:'center',width:fixWidth(0.1),
                formatter:function(value,rec,rowIndex) {
                    var valName = "未接收";
                    if(value && value == "1") {
                        valName = "已接收";
                    }

                    return valName;
                }
            }
			</#if>
			<#if eventType=="history">
                ,{field:'evaLevelName',title:'评价情况', align:'center',width:fixWidth(0.1)}
			</#if>
                <@block name="createTimeStrField">
                ,{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortName:'T1.CREATE_TIME',sortable:true, formatter: dateFormatter, hidden:true},
                </@block>
                {field:'influenceDegreeName',title:'影响范围', align:'center',width:fixWidth(0.1),sortName:'T1.INFLUENCE_DEGREE',sortable:true, hidden:true},
                {field:'sourceName',title:'信息来源', align:'center',width:fixWidth(0.1),sortName:'T1.SOURCE',sortable:true, hidden:true},
                {field:'urgencyDegreeName',title:'紧急程度', align:'center',width:fixWidth(0.1),sortName:'T1.URGENCY_DEGREE',sortable:true, hidden:true},
                {field:'involvedPersion',title:'涉及人员', align:'center',width:fixWidth(0.1), hidden:true},
                {field:'contactUser',title:'联系人员', align:'center',width:fixWidth(0.1),sortName:'T1.CONTACT_USER',sortable:true, hidden:true},
                {field:'tel',title:'联系电话', align:'center',width:fixWidth(0.1),sortName:'T1.TEL',sortable:true, hidden:true}
            ]],
            </@block>
            <@block name="generalPageSize">pageSize: 20,</@block>
            toolbar:'#jqueryToolbar',
            pagination:true,
            queryParams:queryParams,
            onSelect: itemSelected,
            onUnselect: eventItemSelected,
            onDblClickRow:function(index,rec){
                showDetailRow(rec.eventId, rec.instanceId, rec.workFlowId,rec.type);
            },
            onLoadError: function () {//数据加载异常
                $('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
            },
            onSortColumn: function(fieldId, order) {
            	var sortColumn = $('#list').datagrid('getColumnOption', fieldId),
            		gridLevel = $('#gridLevel').val(),
            		remotSortGridLevel = "${remotSortGridLevel!},";
            	
            	if(remotSortGridLevel.indexOf(gridLevel) > 0 && sortColumn.sortName) {
	            	$('#orderByField').val(sortColumn.sortName + ' ' + order.toUpperCase());
	            	conditionSearch();
            	}
            },
            
            <@block name="extraFunction"></@block>
           
            onLoadSuccess: function(data){//事件标题内容左对齐
            	$('#list').datagrid('buildChoosseMenu');
            	
                if(data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                } else {
                    addIcons();
                    var panel = $(this).datagrid('getPanel'),
                        tr = panel.find('.datagrid-view2 tr.datagrid-row'),//获取展示的列表行
                        trLen = 0,
                        trHeight = 0,
                        leftOffset = 0,
                        dataViewHeight = 0,
                        trIndexMax = 0,
                        toppingFieldAttrId = null,
                        toppingFieldValue = null,
                        toppingFieldFontSize = $('#toppingFieldFontSize').val(),
                        toppingFieldFontColor = $('#toppingFieldFontColor').attr('data-current-color'),
                        toppingFieldArray = $('#toppingFieldDiv input.queryParam[name$=ToppingField]');

                    if(tr) {
                        trLen = tr.length;
                        trHeight = tr.eq(0).outerHeight(true);//单行高度
                        leftOffset = tr.eq(0).find('td[field]:visible').eq(0).width() / 5;//首列宽度的1/5
                    }

                    dataViewHeight = $('.datagrid-view').height();//获取列表的高度(包括列表标题)
                    trIndexMax = parseInt(dataViewHeight / trHeight, 10) - 1;//扣除一行，表行索引从0开始
                    
                    if(toppingFieldArray) {
                    	for(var index = 0, len = toppingFieldArray.length; index < len; index++) {
                    		var toppingFieldObj = $(toppingFieldArray[index]);
	                    	var val = toppingFieldObj.val();
	                    	
	                    	if(val) {
	                    		toppingFieldAttrId = toppingFieldObj.attr('attrId');
	                    		toppingFieldValue = val;
	                    		break;
	                    	} 
                    	}
                    }
                    
                    tr.each(function(i){
                        var operateNotice = $(this).find('.OperateNotice');
                        
                        if(toppingFieldAttrId && toppingFieldValue) {
                        	if(data.rows[i][toppingFieldAttrId] == toppingFieldValue) {
                        		var rowNumberObj = panel.find('.datagrid-view1 tr.datagrid-row[datagrid-row-index=' + i + '] td > div.datagrid-cell-rownumber');
                        		
                        		if(toppingFieldFontColor) {
	                        		$(this).css('color', toppingFieldFontColor);
	                        		$(this).find('td > div.datagrid-cell > a').css('color', toppingFieldFontColor);
	                        		rowNumberObj.css('color', toppingFieldFontColor);
                        		}
                        		
                        		if(toppingFieldFontSize) {
	                        		$(this).find('td > div.datagrid-cell').css('font-size', toppingFieldFontSize);
	                        		rowNumberObj.css('font-size', toppingFieldFontSize);
                        		}
                        	}
                        }
                        
                        $(this).mouseover(function() {
                            var trOffset = $(this).offset(),
                                scrollTop = $(".datagrid-body").scrollTop(),
                                arrowHeight = $(this).find('.arrow').eq(0).outerHeight(true);//箭头高度

                            if(i==(trLen - 1) && i != 0 && (scrollTop > 0 || trIndexMax == i)){//有滚动条或者无滚动条时能展示的最大行数的最后一行
                                $(this).find('.arrow').css({'top': '28px', 'background': 'url(${rc.getContextPath()}/images/arrow2.png)'});

                                operateNotice.css('top', $(this).position().top + scrollTop - trHeight + arrowHeight);
                            } else {
                                operateNotice.css('top', $(this).position().top + scrollTop + trHeight);
                            }
                            
                            operateNotice.css('left', $(this).position().left + leftOffset).show();
                        });

                        $(this).mouseout(function(){
                            operateNotice.hide();
                        });
                    });

                    columnListSet($('#list').datagrid("getRows"));
                }
                
                <@block name="listLoadSuccessExtraOperate"></@block>
            }
        });
		
		<@block name="generalPaging">
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
        </@block>
    }

    function searchData(b, searchArray) {
        var a = queryData(searchArray);
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
    function flashData(msg, reload){//
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

	<@block name="showDetailRowFunction">
	    function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
	        if(!eventId){
	            $.messager.alert('提示','请选择一条记录！','info');
	        }else{
	            if(bizPlatform == "001"){
	                var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
	                showMaxJqueryWindow("查看事件信息", url);
	            }else{
	                var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
	                
	                <@block name="eventDetailUrlExtraParam"></@block>
	                
	                <@block name="detailEventPageInfo">
	                	showMaxJqueryWindow("查看事件信息", url, fetchWinWidth(), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
	                </@block>
	            }
	        }
	    }
    </@block>

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
        var url = null;
        
        if(eventId && instanceId) {
        	url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddRemind.jhtml?eventId=" + eventId + "&instanceId=" +instanceId;
        	showMaxJqueryWindow("督办信息", url, 480, 280);
        } else {
        	$.messager.alert('警告','缺少有效的督办事件信息!','warning');
        }
    }
    
    function remindEvent() {
    	var selectItem = $('#list').datagrid('getSelected');
    	
    	if(selectItem != null) {
    		var eventId = selectItem.eventId, instanceId = selectItem.instanceId;
    		remindEvent1(null, eventId, instanceId);
    	} else {
    		$.messager.alert('警告','请选中要督办的记录再执行此操作!','warning');
    	}
    }

    function urgeEvent(obj,eventId,instanceId){
        var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddUrge.jhtml?eventId=" + eventId + "&instanceId=" +instanceId;
        showMaxJqueryWindow("催办信息", url, 480, 280);
    }

	<@block name="function_clickFormatter">
    function clickFormatter(value, rec, rowIndex) {
    	<@block name="function_clickFormatter_body">
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
			var supervisionType = rec.supervisionType;//改为实际值
			influencePic = '<img src="${rc.getContextPath()}/images/duban'+ supervisionType +'.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
		}
	
        if(rec.influenceDegree == '04'){
            influencePic += "<b class='FontRed'>[重大]</b>";
        }

        var tab = '';
	<#if eventType == "all" || eventType == "toRemind">
        var remindVal = "";

        if(wfStatus == '1') {
			<#if isShowAttentionBtn?? && isShowAttentionBtn>
                remindVal += '<li class="line"></li>';
			</#if>

            remindVal += '<li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
        }

        if(remindVal) {
            tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
        }
	<#elseif eventType == "my">
        if(wfStatus == '1') {
            var urgeVal = '<li class="cuiban" onclick=urgeEvent(this,'+rec.eventId+','+rec.instanceId+')>催办</li>';
            tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+urgeVal+'</ul><div class="arrow"></div></div></div>';
        }
	<#elseif eventType == "todo">
        if(remindStatus == '1' || remindStatus == '3') {
            urgencyPic += '<i title="催办" class="ToolBarRemind"></i>';
        }
	</#if>

        if(urgencyName && urgency != '01') {
            urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency"></i>';
        }

        var f = tab + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
        return f;
        </@block>
    }
    </@block>

    function dateFormatter(value, rowData, rowIndex) {
        if(value && value.length >= 10) {
            value = value.substring(0,10);
        }

        return value;
    }

    function titleFormatter(value, rowData, rowIndex) {
        var title = "";

        if(value) {
            title = '<span title="'+ value +'" >'+ value +'</span>';
        }

        return title;
    }
    
    function itemSelected(index, rec) {
    	eventItemSelected(index, rec);
    }
    
    function eventItemSelected(index, rec) {
        if($('#list').datagrid('getSelections').length == 1) {
            var row = $('#list').datagrid('getSelected');

            idStr = row.eventId;
            _instanceId = row.instanceId;
            _bizPlatform = row.bizPlatform;
            
            btnAuthority(rec);//列表按钮展示权限设置
        } else {
            idStr = '';
            _instanceId = '';
            _bizPlatform = '';
        }
    }
</script>

</body>
</html>