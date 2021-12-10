<@override name="eventDetailPageTitle">
	江西省平台事件详情页面
</@override>
<@override name="eventDetailAdditionalQuote">

<style>
.WitchLink {
    width: 150px !important;
    margin-left: -50px;
}

.DBLink {
    padding: 10px 0 0 142px!important;
}

.ProcessingLink .ht ul li:nth-child(1){
	margin-right: 120px!important;
}

.t_pic{margin-left: 40px;}
.f_pic{margin-left: 40px;}
.LinkList{margin-left: 40px;}


.more-table {
    text-align: center;
    height: 30px;
    line-height: 30px;
}

.more-table a {
    display: block;
    width: 260px;
    margin: 0px auto;
    -webkit-border-radius: 2px;
    -moz-border-radius: 2px;
    border-radius: 2px;
    transition: all 0.5s;
}

.bg-gray {
    background-color: #5294e8;
    color: #fff;
}

a.bg-gray:hover {
    text-decoration: none;
    background-color: #e2e8ee;
}

</style>
</@override>


<@override name="additionalHandlePageBefore">
<div id="similarDivision"></div>
<div id="similarEventDiv" flag="0" region="center" border="false" style="width:100%;height:230px;overflow:hidden; position:relative;display:none;">
    <table style="width:100%" id="similarEventTable"></table>
</div>
<div id="moreTableDiv" class="more-table" style="display:none"><a id="switchTable" href="javascript:switchMoreTable();" class="bg-gray seemore" style="cursor: pointer;">发现有重复的上报事件,请选择是否归并处理</a></div>
</@override>

<@override name="additionalInitialization">
<#if showSimilarList?? && showSimilarList=='true' && isHandle?? && isHandle>
findSimilarCount();
</#if>
</@override>

<@override name="extraDetailFunction">

	function findSimilarCount(){
	
		var queryParams=queryData();
		$.ajax({
			type: 'POST',
			url: '${rc.getContextPath()}/zhsq/szzg/eventController/getSimilarEventCount.jhtml',
			data:queryParams,
			dataType: 'json',
			success: function(data) {
				if(data&&data>0){
					$('#moreTableDiv').show();
					$('#similarDivision').css('margin-top','15px');
				}else{
					$('#moreTableDiv').hide();
					$('#similarDivision').css('margin-top','0px');
				}
			},
			error: function(data) {
			},
			complete : function() {
			}
		});
	
	}
	

	function switchMoreTable(){
		var switch_a=$('#switchTable');
		if(switch_a.hasClass('seemore')){
			switch_a.removeClass('seemore');
			switch_a.addClass('closemore');
			switch_a.html('收起相似事件');
			$('#similarEventDiv').show();
			if($('#similarEventDiv').attr('flag')=='0'){
				loadDataList();
				$('#similarEventDiv').attr('flag','1');
			}
		}else if(switch_a.hasClass('closemore')){
			switch_a.removeClass('closemore');
			switch_a.addClass('seemore');
			switch_a.html('发现有重复的上报事件,请选择是否归并处理');
			$('#similarEventDiv').hide();
		}
	}


	var defaultShowWindowHeight=560;
	var similarMsg;
	function loadDataList(){
    
    	var queryParams = queryData();
    	var IsCheckFlag=false;
    	var checkRow;
    
		$('#similarEventTable').datagrid({
        	idField: 'eventId',
            width:600,
			height:300,
			nowrap: true,
			remoteSort:false,
			//rownumbers:true,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			//pagination:true,
			//pageSize: 5,
			toolbar:'#jqueryToolbar',
			url:'${rc.getContextPath()}/zhsq/szzg/eventController/getSimilarEventList.jhtml?listType=5',
			queryParams:queryParams,
            columns: [[
                
                {field:'eventId',align:'center',checkbox:true},//
                {field:'infoOrgCode',align:'center',checkbox:true,hidden:true},//
                {field:'gridId',align:'center',checkbox:true,hidden:true},//
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.35),sortable:true,
					formatter:function(value,rec,rowIndex){
						if(value == null){
							value = "";
						}
						var f = '<a class="eName" href="###" title="'+ rec.eventName +'" onclick="showDetailRow(\'' + rec.eventId + '\',\''+rec.instanceId+'\')")>'+value+'</a>';
						return f;
					}	
				},
				{field:'happenTimeStr',title:'事发时间',halign:'left',align:'left',width:fixWidth(0.2),formatter: titleFormatter},//
				{field:'eventClass',title:'事件分类', halign:'left',align:'left',width:fixWidth(0.2),sortable:true,formatter: titleFormatter},
				{field:'gridPath',title:'所属网格', halign:'left',align:'left',width:fixWidth(0.3),sortable:true,formatter: titleFormatter}

            ]],
            onLoadError: function () {//数据加载异常
    		},
    		onCheck: function (rowIndex, rowData) {
    			if(checkRow==rowIndex){//判断上次选中的是否是同一行
    				//是同一行则将这行取消选中
    				$("#similarEventTable").datagrid("unselectRow", rowIndex);
    				IsCheckFlag = false;
    				checkRow=null;
    				similarMsg="";
    			}else{
    				if(!IsCheckFlag){
    					IsCheckFlag = true;
    				}
    				checkRow=rowIndex;
    			}
		    },
			onLoadSuccess:function(data){
				$(".datagrid-header-check").html("");
				if(data.total == 0){
					$('#similarEventDiv').hide();
				}
			},
             
        });
        
        //设置分页控件
	    //var p = $('#similarEventTable').datagrid('getPager');
		//$(p).pagination({
		//	pageSize: 5,//每页显示的记录条数，默认为
		//	pageList: [5,10,15,20],//可以设置每页记录条数的列表
		//	beforePageText: '第',//页数文本框前显示的汉字
		//	afterPageText: '页    共 {pages} 页',
		//	displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		//});
        
    }
    
    
    function queryData() {
    	var searchArray = {
    		"infoOrgCodeAccurate":"${event.gridCode!}",
    		"eventNameAccurate":"${event.eventName!}",
    		"eliminateEventIdList":"${event.eventId!}",
    		"status":"00,01,02",
    		"eventType":"all",
    		"page":"1",
    		"rows":"5"
    	};
    	
		return searchArray;
	}
    
    
    function titleFormatter(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ value +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	
    function titleFormatterGrid(value, rowData, rowIndex) {
		var title = "";
		
		if(value) {
			title = '<span title="'+ rowData.gridPath +'" >'+ value +'</span>';
		}
		
		return title;
	}
	
	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
        if(!eventId){
            $.messager.alert('提示','请选择一条记录！','info');
        }else{
            var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?instanceId="+instanceId+"&eventId="+eventId+"&cachenum=" + Math.random();
                
            showMaxJqueryWindow("查看相似事件信息", url, fetchWinWidth(), defaultShowWindowHeight && defaultShowWindowHeight<document.body.clientHeight?defaultShowWindowHeight:undefined, true);
        }
    }

</@override>

<@override name="variableAdjust">
	handleTaskNameWidth = 165;		//处理环节总宽度
</@override>

<@extends name="/zzgl/event/detail_event.ftl" />