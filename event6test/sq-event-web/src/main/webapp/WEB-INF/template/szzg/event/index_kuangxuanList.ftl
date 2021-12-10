<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>框选列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<script type="text/javascript" src="${uiDomain!''}/js/echarts/echarts-all.js"></script>
<style>
.right_content{overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
.topDiv{width:220px;height:140px;float:left;position: relative;    left: 30px;}
.topDiv div{width:178px;}
.topNum{height:20px;font-family:'Arial Negreta','Arial';font-weight:700;font-style:normal;font-size: 18px;}
.topNum span{margin-top:5px;position: absolute;margin-left: 65px;}
.topEchart{height:100px;}
.topName{height:20px;position: relative;top: -5px;font-family:'Arial Normal','Arial';font-weight:400;font-style:normal;font-size:14px;color: #333333;text-align:center;line-height: normal;}
.zhanbi{position:absolute;top:50px;height:36px;}
</style>
</head>
<body class="easyui-layout">
<div style="width:900px;height:140px;overflow:hidden;" region="north">
<div class="topDiv">
	<div class="topNum" style="color:#FF9900;"><span>TOP1</span></div>
	<div class="topEchart" id="topEchart_1"></div>
	<div class="topName"><span id="topName_1"></span></div>
</div>
<div class="topDiv">
	<div class="topNum" style="color:#FFCC00;"><span>TOP2</span></div>
	<div class="topEchart" id="topEchart_2"></div>
	<div class="topName"><span id="topName_2"></span></div>
</div>
<div class="topDiv">
	<div class="topNum" style="color:#CCCC00;"><span>TOP3</span></div>
	<div class="topEchart" id="topEchart_3"></div>
	<div class="topName"><span id="topName_3"></span></div>
</div>
<div class="topDiv">
	<div class="topNum" style="color:#868686;"><span>TOP4</span></div>
	<div class="topEchart" id="topEchart_4"></div>
	<div class="topName"><span id="topName_4"></span></div>
</div>
<div class="topDiv">
	<div class="topNum" style="color:#868686;"><span>TOP5</span></div>
	<div class="topEchart" id="topEchart_5"></div>
	<div class="topName"><span id="topName_5"></span></div>
</div>

</div>
<div id="jqueryToolbar">
    <div class="ConSearch">
        <div class="fl">
            <ul>
                <li>所属网格：</li>
	                <li>
	                	<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam"/>
	                	<input id="gridId" name="gridId" type="text" class="hide queryParam"/>
	                	<input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:150px;"/>
	                </li>
	            	<li>事件分类：</li>
	                <li>
	                	<input id="type" name="type" type="text" value="" class="queryParam hide"/>
	                	<input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:180px;"/>
	                </li>
	            	<li>关键字：</li>
	                <li><input id="keyWord" name="keyWord" type="text" class="inp1" id="keyWord" value="事件描述/标题/事发详址" style="color:gray; width:180px;" onfocus="if(this.value=='事件描述/标题/事发详址'){this.value='';}$(this).attr('style','width:150px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:150px;');this.value='事件描述/标题/事发详址';}" /></li>


            </ul>
        </div>

        <div class="btns">
            <ul>
                <li><a href="#" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(1)">查询</a></li>
                <li><a href="#" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件"
                       onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>
        ‍

    </div>
    <div class="h_10 clear"></div>
    <div class="ToolBar" id="ToolBar" style="height:0px;">
        <div class="blind"></div>
        
    </div>


</div>


<div id="hospitalDiv" region="center" border="false" style="width:100%; overflow:hidden;">
    <table id="list"></table>
</div>
</body>
<script type="text/javascript">
	var dataDict = {<#list dataDict as t>'${t.dictGeneralCode}':'${t.dictName}',</#list>};
    $(function () {
	
        //加载区域
       AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
			if(items!=undefined && items!=null && items.length>0){
				var grid = items[0];
				$("#infoOrgCode").val(grid.orgCode);
			} 
		});
		
		AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, null, { 
					ChooseType : "1" 
				});
		loadTop();
		loadDataList();
		
    });
	var list ;

    function loadDataList() {
        list = $('#list').datagrid({
            width: 1000,
            height: (($(parent.document).height()-200)),
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
            idField: 'EVENT_ID',
            url: '${rc.getContextPath()}//zhsq/szzg/eventController/findEventList.json',
            columns: [[
                {field: 'EVENT_ID',  hidden: 'true'},
                {field:'EVENT_NAME',title:'事件标题', align:'left',width:180,
                	formatter:function(value,rec,rowIndex){
					return "<div title='"+value+"' class='right_content' onclick='showDetailRow("+rec.EVENT_ID+");'><a style='cursor: pointer;' herf='javascript:void(0);' >"+value+"</a></div>";}},
				{field:'HAPPEN_TIME',title:'事发时间', align:'center',width:80,formatter:function(value,rec,rowIndex){return fmtDate(value);}},
				{field:'HANDLE_DATE',title:'办结期限', align:'center',width:150,formatter:function(value,rec,rowIndex){return fmtDate(value,true);}},
				{field:'TYPE_',title:'事件分类', align:'center',width:200,
                    formatter:function(value, rec, index){return '<div title="'+value+'" class="right_content">'+value+'</div>';}},
				{field:'GRID_PATH',title:'所属网格', align:'center',width:200,
                    formatter:function(value, rec, index){return '<div title="'+value+'" class="right_content">'+isNull(value)+'</div>';}},
				{field:'STATUS',title:'当前状态', align:'center',width:80,formatter:function(value, rec, index){return dataDict[value];}}	,
				{field:'CREATE_TIME',title:'采集时间', align:'center',width:90,formatter:function(value,rec,rowIndex){return fmtDate(value);}}
            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            queryParams: {dateNo:${dateNo},orgCode:'${orgCode}'},
            onLoadSuccess: function (data) {
                if (data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
            },
            onLoadError: function () {
                $('.datagrid-body-inner').eq(0).addClass("l_elist");
                $('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
            }
        });

        //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 20,//每页显示的记录条数，默认为
            pageList: [20, 30, 40, 50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
        });
    }
	function isNull(v){
		return v?v:'';
	}
	function typeFn(v){
		if(v==undefined || v == ''){return '';}
		var arr = v.split(','),str='';
		if(arr.length ==0){return v;}
		for(var i=arr.length-1;i>=0;i--){
			str+=arr[i]+'-';
		}
		return str.substr(0,str.length-1);
	}
	function showDetailRow(eventId){
		if(!eventId){
		    $.messager.alert('提示','请选择一条记录！','info');
		}else{
		var url  = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId="+eventId;
		parent.showMaxJqueryWindow('事件详情',url,965,500,false);	
		//parent.getDetailOnMapOfListClick('menuSummaryWidth_,_965,_,menuSummaryHeight_,_500,_,menuLayerName_,_eventMeasureLayer1,_,menuName_,_查看事件信息,_,menuSummaryUrl_,_'+url+'&wid=',320,110,1);
		}
	}
	function fnW(str){
			return (str>9?str:"0"+str)+'';
			} 
		function fmtDate(num,time){
			if(num == null || num == undefined){return '';}
			var date=new Date(num);		
			var str=date.getFullYear()+"/"+fnW((date.getMonth()+1))+"/"+fnW(date.getDate());
			if(time){ str+=" "+fnW(date.getHours())+":"+fnW(date.getMinutes())+":"+fnW(date.getSeconds());}
			return str;
		}
    function searchData() {
        var queryParams = {orgCode:'${orgCode}',dateNo:${dateNo}};
		var infoOrgCode = document.getElementById('infoOrgCode').value;
        if (infoOrgCode.length>0) {
            queryParams["orgCode"] = infoOrgCode;
        }
		var type = document.getElementById('type').value;
        if (type.length>0) {
            queryParams["type"] = type;
        }
		var keyWord = document.getElementById('keyWord').value;
        if (keyWord.length>0 && keyWord!='事件描述/标题/事发详址') {
            queryParams["keyWord"] = keyWord;
        }
       	$('#list').datagrid('clearSelections');
       	$('#list').datagrid('options').queryParams = queryParams;
		$("#list").datagrid('load');
    }

    function resetCondition() {
		document.getElementById("infoOrgCode").value="";
	    document.getElementById("gridId").value="";
	    document.getElementById("gridName").value="";
	    document.getElementById("type").value="";
	    document.getElementById("typeName").value="";
	    document.getElementById("keyWord").value="事件描述/标题/事发详址";
        searchData();
    }
	function fnt(value){
		if(value ==undefined ){
			return '';
		}
		return value.split('_')[1];
	}
	function loadTop(){
		var topArr=[<#list top5 as t>
			{type:'${t.DICT_NAME}',count:${t.TOTAL_}},
		</#list>],
		eventCount = ${eventCount};
		
		for(var i=0,l=topArr.length>5?5:topArr.length;i<l;i++){
		var name_ = fnt(topArr[i].type);
			var option = {tooltip : {show:false,trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"},title:{ text: '',textStyle:{color:'#fff'}},
			legend: {show:false,data:[name_,'事件其他数量']},toolbox: {show : true},calculable : false,
			series : [{name:'事件',type:'pie',radius:['60%','80%'],itemStyle:{normal:{label:{ show : true,
                        position : 'center',formatter:function(){return '占比\n'+(topArr[i].count/eventCount*100).toFixed(1)+'%';},
                        textStyle : {fontSize : '18'}},labelLine:{show:false}}},
			data:[{name: name_,value:topArr[i].count},{name:'事件其他数量',value:(eventCount-topArr[i].count)}]}],color:['#2ec7c9','#b6a2de']};
			var myChart = echarts.init(document.getElementById('topEchart_'+(i+1)));
			myChart.setOption(option);
			$("#topName_"+(i+1)).html(name_);
			//$("#zhanbi_"+(i+1)).html("<p>占比</p><span>"+(topArr[i].count/eventCount*100).toFixed(1)+"%</span>");
		}
		
	}
	/**/
</script>
</html>