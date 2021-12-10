<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>发布信息</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body class="easyui-layout">

<div class="MainContent">
	
</div>
<div id="FerryInfoDiv" region="center" border="false" style="overflow:hidden;">
	<table id="list"></table>
</div>

<div id="jqueryToolbar">
	<div class="ConSearch">
		<input type="hidden" id="viewModel"/>
		<form id="searchForm">
		<div class="fl">
        	<ul>
        		<li>所属网格：</li>
        		<li><input type="text" style="display:none;" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>">
        			<input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if gridName??>${gridName}</#if>" style="width:150px;"/>
        		</li>
        		<li>姓名：</li>
        		<li><input type="text" id="name" class="inp1" style="width:150px" /></li>
              	<li>公民身份号码：</li>
            	<li><input type="text" id="identityCard" class="inp1" style="width:150px" />
				</li>
            </ul>
        </div>
        <div class="btns">
        	<ul>
            	<li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        </form>
	</div>

	<div class="h_10" id="TenLineHeight1"></div>
	<div class="ToolBar">
		<div class="blind"></div><!-- 文字提示 -->
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
	</div>
</div>
<#include "/component/ComboBox.ftl">
<script type="text/javascript">
  	var type = "${type!''}";
	$(function() {
		AnoleApi.initGridZtreeComboBox("gridName", "gridId", null, {
      		Async : {
				enable : true,
				autoParam : [ "id=gridId" ],
				dataFilter : _filter,
				otherParam : {
					"startGridId" : ${gridId?c}
				}
			}
      	});
		loadDataList();
	});
	
	function loadDataList() {
		$('#list').datagrid({
			width:600,
			height:600,
			nowrap: false,
			striped: true,
			fitColumns:true,
			fit: true,
			idField:'infoOpenId',
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisDataListOfPeople.json',
			columns:[[  
				{field:'name', title: '姓名', align:'left',width:60, formatter:function(value, rec, index){
                	var f = '';
                	if(value!=null){
                		f = '<a href="###" onclick="showRow('+rec.ciRsId+')"><span style="color:blue; text-decoration:underline;">'+value+'</span></a>';
                	}
					return f;
				}}, {
									field : 'identityCard',
									title : '公民身份号码',
									align : 'center',
									width : 150
								}, {
									field : 'gridName',
									title : '所属网格',
									align : 'center',
									width : 120
								}, {
									field : 'gender',
									title : '性别',
									align : 'center',
									width : 35
								}, {
									field : 'birthday',
									title : '出生日期',
									align : 'center',
									width : 80
								}, {
									field : 'familyAddress',
									title : '现住地址',
									width : 200
								}, {
									field : 'phone',
									title : '固定电话',
									width : 90
								}, {
									field : 'residentMobile',
									title : '移动电话',
									width : 90
								}
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,
			pageSize: 20,//每页显示的记录条数，默认为
			queryParams:{gridId:$("#gridId").val(),type:type},
			onLoadSuccess:function(data){
			    $('#list').datagrid('clearSelections');	//清除掉列表选中记录
				if(data.total == 0) {
					var body = $(this).data().datagrid.dc.body2;
					body.append('<div style="text-align: center;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>');
				}
			},
			onLoadError: function() {
				$('.datagrid-body-inner').eq(0).addClass("l_elist");
				$('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
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
	
	function resetCondition() {
		$("#searchForm")[0].reset();
		searchData();
	}
	
	function searchData() {
		var a = new Array();
		a["gridId"]=$("#gridId").val();
		a["type"]=type;
		var name = $("#name").val();
		if(name!=null && name!="") a["name"]=name;
		var identityCard = $("#identityCard").val();
		if(identityCard!=null && identityCard!="") a["identityCard"]=identityCard;
		doSearch(a);
	}
	
	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		$("#list").datagrid('load');
	}
	
	function showRow(id) {
		var url = '${RS_URL}/cirs/viewResident.jhtml?ciRsId='+id;
		showMaxJqueryWindow("信息查看", url, 920, fixHeight(0.9));
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
		$("#list").datagrid('load');
	}
	function reloadDataForSuccess(result){
		closeMaxJqueryWindow();
		DivShow(result);
		$("#list").datagrid('load');
	}
</script>

<#include "/component/maxJqueryEasyUIWin.ftl" />

</body>
</html>