<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>江西省市域治理简报列表</title>
	<!-- easyUI start -->
	<#include "/component/standard_common_files-1.1.ftl" />
	<link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/gray/easyui.css">
	<script type="text/javascript" src="${uiDomain}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
	<!-- easyUI end -->
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/themes/default/css/normal.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/themes/default/plat-extend.css">

	<script type="text/javascript" src="${uiDomain}/js/function.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/rs/easyui-datagrid-extend.js"></script>
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/zTreeStyle.css" />
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/anole_combobox.css" />
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/jquery.anole.combobox.js"></script>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/anole.combobox.api.js"></script>
	<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>

	<script type="text/javascript" src="${SQ_EVENT_URL}/js/plugIn/plug_in.js"></script>
	<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
	<@block name="extraJs"></@block>
	<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
	<#include "/component/bigFileUpload.ftl" />
	
	<style type="text/css">
		.datagrid-body{position:relative;}/*解决兼容性视图下列表无法跟随滚动*/
		.NorToolBtn{width:auto}
		.width65px{width:105px;}
		input[type="radio"]{
			vertical-align: middle;
		}
		.bigFile-upload-box .zt-pxt>a {
		    display: none;
		}
		#bigFileUploadDiv_tipText{
			margin-top:5px
		}
		.datagrid-mask-msg{    z-index: 10000;}
	</style>
	
</head>

<body>
<body class="easyui-layout">
<div id="_DivCenter" region="center" style="padding-bottom: 20px">
	<table id="list"></table>
</div>
<div id="jqueryToolbar">
	<div class="ConSearch">
		<form id="searchForm">

			<div class="fl">
				<ul>
					<li>当前辖区：</li>
					<li>
<#--						<input type="text" id="gridName" class="inp1" style="width:135px;" value="${gridName}" readonly></input>-->
						<span id="gridName" style="width:135px;margin-right: 35px">${gridName}</span>
					</li>
				</ul>
			</div>
			<div class="btns">
				<ul>
					<li><a href="###" class="chaxun" title="查询按钮" onclick="conditionSearch()">查询</a></li>
				</ul>
			</div>

			<div class="h_10 clear"></div>
			<div class="tool fr" id="toolBarFrDiv">
			</div>
		</form>
	</div>
	<div class="h_10" id="TenLineHeight1"></div>
	<div class="ToolBar">
		<div class="tool fr" id="toolBarFrDiv">
			<@actionCheck></@actionCheck>
<#--			<a id="export" href="javascript:void(0)" onclick="doImportPdf()" class="NorToolBtn ExportBtn">导入月报</a>-->
		</div>
	</div>
</div>

<div class="easyui-window" title="导入PDF"  closed="true" modal="true" minimizable="false" maximizable="false" style="width:700px;height:400px;padding:5px;">

	<form id="importPDF" method="post" action="${rc.getContextPath()}/zhsq/zzgl/briefingController/importPDF.jhtml" autocomplete="off" enctype="multipart/form-data">

		<label class="LabName"><span>PDF上传：</span></label>
		<div id="bigFileUploadDiv"></div>

		<div>
			<div style="text-align: center;margin-top: 40%;margin-left: 35%;">
				<a href="#" class="BigNorToolBtn SaveBtn" onclick="submitForm()" href="javascript:void(0)" >提交</a>
				<a href="#" class="BigNorToolBtn ResetBtn" onclick="cancelForm()" href="javascript:void(0)" >取消</a>
			</div>
		</div>
	</form>
</div>

<div class="easyui-window" title="导出PDF"  closed="true" modal="true" minimizable="false" maximizable="false" style="width:700px;height:500px;padding:5px">

	<form id="exportPdf" method="post" action="${rc.getContextPath()}/zhsq/zzgl/briefingController/exportPdf.jhtml" autocomplete="off" enctype="multipart/form-data">



	</form>
</div>
</body>

<script type="text/javascript">
	var createTimeDateRender = null;

	$(function(){

		loadDataList();

	});

	//导入月报点击事件
	function doImportPdf() {
		var rows = $('#list').datagrid('getSelections');
		var length = rows.length
		if(length==0){
			$.messager.alert("提示","请选择一份简报！")
			return
		}else{
			$.ajax({
				url: '${rc.getContextPath()}/zhsq/zzgl/briefingController/getAttachmentId.json',
				type: 'POST',
				dataType:"json",
				async: false,
				data: {"reportId":rows[0].REPORT_ID},
				error: function(data){
					$.messager.alert('友情提示','请求数据异常!','warning');
				},
				success: function(data){
					console.log(data.attaId);
					if(data.attaId){
						bigFileUploadOpt["attachmentData"].attachmentIds = data.attaId;
					}
					$('#bigFileUploadDiv').html('');
					bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
					$('#importPDF').parent().parent().show();
				}
			});
			// bigFileUploadOpt["attachmentData"].attachmentIds = rows[0].attaId;
			// $('#bigFileUploadDiv').html('');
			// bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
			// $('#importPDF').parent().parent().show();
		}
	}
	//查询
	function conditionSearch(){
		searchData();
	}

	function searchData(searchArray) {
		var a = queryData(searchArray);
		doSearch(a);
	}

	function doSearch(queryParams){
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams=queryParams;
		refreshData();//点击查询重置第一页刷新，修改删除当前也刷新
	}


	/**
	 *reload:默认undefined
	 *		false:点击查询重置第一页刷新，
	 *		true:修改删除后当前页刷新
	 */
	function refreshData(reload){//
		$('#list').datagrid('clearSelections');	//清除选择的行
		if(reload){
			$("#list").datagrid('reload');			//当前页刷新
		}else{
			$("#list").datagrid('load');			//重置到第一页刷新
		}
	}

	function queryData(searchArray){
		var postData = {};
		if (searchArray!=undefined && searchArray!=null){
			postData = searchArray;
		}
		$("#eventQueryForm .queryParam").each(function() {
			var name = $(this).attr("name"), val = $(this).val();
			if (name && val){
				postData[name] = val;
			}
		});
		return postData;
	}

	//点击详情相应事件
	function clickDetail(reportId,dateStr,gridId,infoOrgCode) {
		modleopen();
		var url = "${rc.getContextPath()}/zhsq/zzgl/briefingController/report.jhtml?dateStr="+ dateStr +"&reportId=" + reportId+"&gridId="+gridId+"&infoOrgCode="+infoOrgCode;
		showMaxJqueryWindow("详情页面", url,($(window).width())*0.8,($(window).height())*0.9);
	}


	//加载DataGrid列表数据
	function loadDataList(){
		var queryParams = queryData();
		$('#list').datagrid({
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,  //隔行变色
			fit: true,
			fitColumns: true,
			scrollOnSelect:true,
			singleSelect: true,
			/*idField:'eventId',*/
			url:'${rc.getContextPath()}/zhsq/zzgl/briefingController/briefing/listData.json?defaultInfoOrgCode=${defaultInfoOrgCode}&dateEnd=${dateEnd}',
			columns:[[
				/*{field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true,outMenu:true},*/
				{field:'GRID_NAME',title:'机构名称', align:'center',width:fixWidth(0.2)},
				{field:'DATE_STR',title:'月份', align:'center',width:fixWidth(0.1),sortable:true},
				{field:'operation',title:'操作', align:'center',width:fixWidth(0.1),
					formatter:function (value, rec, index) {
						var dateStr = rec.DATE_STR+"";
						var arr = dateStr.split("-");
						var resultDate = arr[0]+arr[1];
						var f = '<a href="###" title="详情" style="text-decoration:none;" onclick="clickDetail('+rec.REPORT_ID+","+resultDate+","+rec.GRID_ID+","+rec.INFO_ORG_CODE+')">详情</a>&nbsp;'
					    if(rec.PDF_URL!=null && rec.PDF_URL!='' && rec.PDF_URL!=undefined){
							f+='|&nbsp;<a href="###" title="导出月报" style="text-decoration:none;" onclick="exportPdf('+rec.REPORT_ID+',\''+rec.GRID_NAME+'\')">导出月报</a>&nbsp;';
							return f;
						}
						return f;
					}},
			]],
			toolbar:'#jqueryToolbar',
			pagination:true,pageSize: 20,
			queryParams:queryParams,
			onSelect: function(rowIndex, rowData){},
			/*onDblClickRow:function(index,rec){
                showDetailRow(rec.eventId, rec.instanceId, rec.workFlowId,rec.type);
            },*/
			onLoadError: function () {//数据加载异常
				$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
			},
			onLoadSuccess: function(data){

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

	//初始化导入PDF框
	var bigFileUploadOpt = {
		useType: 'edit',
		fileExt: '.pdf',
		attachmentData: {attachmentType:'BRIEF_REPORT'},
		module: 'event',
		fileNumLimit: 1,
		individualOpt : {
			isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
		},
		deleteCallback:function(obj){
			var rows = $('#list').datagrid('getSelections');
			$.ajax({
				url: '${rc.getContextPath()}/zhsq/zzgl/briefingController/delReportPdfUrl.json',
				type: 'POST',
				dataType:"json",
				async: false,
				data: {"reportId":rows[0].REPORT_ID},
				error: function(data){
					$.messager.alert('友情提示','删除简报Pdfurl异常!','warning');
				},
				success: function(data){
					$("#list").datagrid('reload');
				}
			});
		},
	};

	<#if reportId??>
		bigFileUploadOpt["useType"] = 'edit';
		bigFileUploadOpt["attachmentData"].eventSeq = "1,2,3";
		bigFileUploadOpt["attachmentData"].bizId = '${reportId?c}';
		bigFileUploadOpt["attachmentData"].attachmentIds = "${attaId!}";
	</#if>

	bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);


	function submitForm() {
		var rows = $('#list').datagrid('getSelections');
		var reportId = rows[0].REPORT_ID;

		var options = {
			data:{"reportId":reportId},
			success:function (data) {
				if(data!=0){
					$.messager.alert("提示","提交成功！")
					$('#importPDF').parent().parent().hide();
					location.reload();
				}else{
					$.messager.alert("提示","提交失败，请重新上传！")
				}
			}
		}
		$('#importPDF').ajaxSubmit(options);
	}

	function cancelForm() {
		$('#importPDF').parent().parent().hide();
	}

	function exportPdf(reportId,gridName) {
		window.open("${rc.getContextPath()}/zhsq/zzgl/briefingController/exportPdf.jhtml?reportId="+reportId+"&gridName="+encodeURI(gridName))
	}
</script>

</body>
</html>
