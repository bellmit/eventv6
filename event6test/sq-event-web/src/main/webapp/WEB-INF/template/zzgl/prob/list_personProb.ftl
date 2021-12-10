<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />
	<#include "/component/AnoleDate.ftl">
	<style type="text/css">
		.inp1 {width:100px;}
		  .datagrid-cell{
			white-space:normal;
			line-height:20px;
		}
	</style>
</head>
<body class="easyui-layout">
   
	<div id="_DivCenter" region="center" >
	   <table id="list"></table>
	</div>
	<div id="jqueryToolbar">
		<div class="ConSearch">
			<form id="searchForm">
			<div class="fl">
				<ul>
				    <li>所属网格：</li>
				      <li>
				      <input type="hidden" id="regionCode" name="regionCode" value="${gridCode!}" />
        			  <input type="hidden" id="gridId" name="gridId" value=""/>
        			  <input class="inp1 InpDisable" type="text" id="gridName" name="gridName" value="<#if gridName??>${gridName}</#if>"/>
				      </li>
					<li style="margin-right:0;">违纪违规时间：</li>
                    <li>
				        <input id="date1" type="text" class="inp1" style="width: 180px;" value=""/>
					    <input type="text" id="startTime" name="violationDateStart" " style="display:none;"/>
					    <input type="text" id="endTime" name="violationDateEnd" style="display:none;"/>
                    </li>
				</ul>
			</div>
			<!-- <div class="noprint tool" style="position: fixed; top: 5px; right: 5px;">  
	             <a href="#" class="NorToolBtn PrintBtn" onclick="windowPrint();">打印</a>
             </div>-->
	        <div class="btns">
				<ul>
					<li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
		            <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
				</ul>
	        </div>
			</form>
		</div>
		<div class="h_10" id="TenLineHeight1"></div>
		<div class="ToolBar">
		    <div class="blind"></div>	
		     <div class="tool fr">
		       <!-- <a href="javascript:windowPrint();" title="打印报表" class="NorToolBtn PrintBtn">打印</a> -->
	            <a href="javascript:void(0)" class="NorToolBtn ExportBtn" onclick="exportExcel()">导出</a>
	            <a href="javascript:printData();" title="打印报表" class="NorToolBtn PrintBtn">打印</a>
	        </div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
<script type="text/javascript">
	$(function() {
		loadList(); //加载列表
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items){
				if(items!=undefined && items!=null && items.length>0){
					var grid = items[0];
					$("#regionCode").val(grid.gridCode);
				} 
		});
		
		var dateApi = $("#date1").anoleDateRender({
			BackfillType : "1",
			ShowOptions : {
				TabItems : ["月"]
			},
			BackEvents : {
				OnSelected : function(api, type) {
					$("#startTime").val(api.getStartDate());
					$("#endTime").val(api.getEndDate());
					if (type == "自定义") {
						PageApi.DoSearch();
					}
				}
			}
		}).anoleDateApi();
		//dateApi.setRangeDate("${startTime!''}", "${endTime!''}");
	});
	
	//加载列表
	function loadList() {
		$('#list').datagrid({
			rownumbers: true, //行号
			fit: true,
			//fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			scrollbarSize :0,		
			idField:'probId',
			url: '${rc.getContextPath()}/zhsq/personProb/personListData.jhtml',
			//queryParams: $('#searchForm').serializeJson(),
			columns: [[
			    {field:'probId',checkbox:true,hidden:'true'},
			    {field:'probTitle', title:'问题标题', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
			    {field:'name', title:'姓名', align:'center', width:100},
			    {field:'sex', title:'性别', align:'center', width:100},
			    {field:'politics', title:'政治面貌', align:'center', width:100},
			    {field:'workUnit', title:'单位', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
			    {field:'profession', title:'职务', align:'center', width:100},
				{field:'caseBrief', title:'简要案情', align:'center', width:100,
				  formatter:function (value,rec,rowIndex) {
				     var f = '<a class="" href="###"  onclick="detail(' + rec.probId + ')")>简要案情</a>';
			    	 return f;
                }},
				{field:'professionType', title:'违纪人员<br/>职级', align:'center', width:100},		
				{field:'violationDate', title:'违纪违规<br/>时间', align:'center', width:100},
			    {field:'partyFlag', title:'党纪处分', align:'center', width:100},
			    {field:'disciplineyFlag', title:'政纪处分', align:'center', width:100},
				{field:'orgProcType', title:'组织处理<br/>类型', align:'center', width:100},
				{field:'procResult', title:'组织处理<br/>内容', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
				{field:'blameFlag', title:'是否问责', align:'center', width:100},
				{field:'transferJusticeFlag', title:'是否移送<br/>司法', align:'center', width:100},
				{field:'amountInvolved', title:'涉案金额', align:'center', width:100},
				{field:'violationMoney', title:'违纪金额', align:'center', width:100},
				{field:'violationMoneyType', title:'违纪违法<br/>资金类别', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
				{field:'violationType1', title:'违规违纪<br/>类别统计', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
				{field:'violationType2', title:'作风建设<br/>类别统计', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
				{field:'violationType0', title:'扫黑除恶<br/>类别统计', align:'center', width:150,
			      formatter:function (value,rec,rowIndex) {
				           var f = '<span>'+formatText(value)+'</span>';
					       return f;
                }},
				{field:'shape', title:'四种形态', align:'center', width:100},
				{field:'amountOfRecovery', title:'追缴资金<br/>（万元）', align:'center', width:100}
			]],
			pagination: true,
			pageSize: 20,
			toolbar: '#jqueryToolbar',
			onLoadSuccess: function(data) {
				listSuccess(data); //暂无数据提示
			},
			onLoadError: function() {
				listError();
			}
		});
	}
	
	
	
	//详情
	function detail(probId) {
		var url = "${rc.getContextPath()}/zhsq/personProb/caseBriefDetail.jhtml?probId=" + probId;
		showMaxJqueryWindow('简要案情', url, 450,250);
	}
	
	
	//查询
	function searchData() {
		$('#list').datagrid('reload', $('#searchForm').serializeJson());
	}
	
	//重置
	function resetCondition() {
		$('#searchForm').form('clear');
		searchData();
	}
	
	//刷新不会跳转到第一页
	function loadData(){
	  $("#list").datagrid('reload');
	}
	
	 //格式化列中的文字
	 function formatText(value){
        if(value != undefined && ''!= value){
            var text = value;
            if(value.length >= 11){
                text = value.substring(0,11);
            }
            return "<span style='display:block;width:100%;cursor:pointer;' title='" + value + "'>" + text + "<span>";
        }else{
            return "<span>无<span>";
        }
    }
    
     //-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		DivShow(result);
		searchData();
	}
	
	//打印
	function windowPrint() {
	    var url = '${rc.getContextPath()}/zhsq/personProb/windowPrint.jhtml';
		winOpenFullScreen(url, "打印事件详情");
	}
	
		//打印
function printData(){
var pm = "";		
	pm += "regionCode=" + $("#regionCode").val();
	pm += "&violationDateStart=" + $('#startTime').val();
	pm += "&violationDateEnd=" + $('#endTime').val();	
	//获取当前页
	var options = $("#list" ).datagrid("getPager" ).data("pagination" ).options;
    var page = options.pageNumber;
    
    var rows = options.pageSize;
	pm+="&page="+page+"&rows="+rows;
	 var url = '${rc.getContextPath()}/zhsq/personProb/printPage.jhtml?'+pm;
	window.open(url);
}
	//导出
	function exportExcel() {
	
		var pm = "";		
			pm += "&regionCode=" + $("#regionCode").val();
			pm += "&violationDateStart=" + $('#startTime').val();
			pm += "&violationDateEnd=" + $('#endTime').val();			
		var url = "${rc.getContextPath()}/common/export/excel.jhtml?beanId=personProbService" + pm;
		window.open(url);
	}
</script>
</html>
