
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件列表</title>
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />

	<link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/icon.css">

	<link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain}/css/normal.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain}/css/easyuiExtend.css" />
	<link rel="stylesheet" type="text/css" href="${uiDomain}/css/jquery.mCustomScrollbar.css" />

	<script type="text/javascript" src="${uiDomain}/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${uiDomain}/web-assets/common/js/jqry-9-1-7.min.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/jquery.easyui.patch.js"></script><!--用于修复easyui-1.4中easyui-numberbox失去焦点后不能输入小数点的问题-->
	<script type="text/javascript" src="${uiDomain}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/jquery.form.js" ></script>
	<script type="text/javascript" src="${uiDomain}/js/zzgl_core.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/function.js"></script>
	<script type="text/javascript" src="${uiDomain}/js/layer/layer.js"></script>


	<script type="text/javascript">
		var rootPath = "${rc.getContextPath()}";
		uiPath = "${uiDomain}";
		var _jSessionId = "EE15E98C5C7BF308B95D623FAB125649";
		//页面初始化
		$(document).ready(function(){
			//默认执行函数
			var defaultAction = "";
			if(window[defaultAction]) window[defaultAction]();

			//初始化
		});

		(function($){

			/**
			 * 表单序列化成json对象
			 *
			 */
			$.fn.serializeJson = function(){
				var serializeObj = {};
				var array=this.serializeArray();
				var str=this.serialize();
				$(array).each(function(){
					if(serializeObj[this.name]){
						if($.isArray(serializeObj[this.name])){
							serializeObj[this.name].push(this.value);
						}else{
							serializeObj[this.name]=[serializeObj[this.name],this.value];
						}
					}else{
						serializeObj[this.name]=this.value;
					}
				});
				return serializeObj;
			};
		})(jQuery);


		/*日期格式化
         * time: 时间毫秒数
         * 调用方式: new Date(time).format('yyyy-MM-dd hh:mm:ss')
        */
		Date.prototype.format = function(f) {
			var o = {
				"M+" : this.getMonth() + 1, //month
				"d+" : this.getDate(), //day
				"h+" : this.getHours(), //hour
				"m+" : this.getMinutes(), //minute
				"s+" : this.getSeconds(), //second
				"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
				"S" : this.getMilliseconds()    //millisecond
			};
			if (/(y+)/.test(f))
				f = f.replace(RegExp.$1, (this.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			for ( var k in o)
				if (new RegExp("(" + k + ")").test(f))
					f = f.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]: ("00" + o[k]).substr(("" + o[k]).length));
			return f;
		};
	</script>

	<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
</head>

<body class="easyui-layout" id="layoutArea">
<div class="MainContent">
	<!--
    #网格树下拉框
    说明：
    #1、
    #2、
     -->
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/zTreeStyle.css" />
	<link rel="stylesheet" type="text/css" href="${ANOLE_COMPONENT_URL}/js/components/combobox/css/anole_combobox.css" />
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/jquery.anole.combobox.js"></script>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/combobox/anole.combobox.api.js"></script><script type="text/javascript" src="${uiDomain}/web-assets/extend/js/excelExportBaseOnPage.js"></script>
	<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/date/jquery.anole.date.js"></script>
	<link rel="stylesheet" href="${ANOLE_COMPONENT_URL}/js/components/date/css/date.css" type="text/css"/>
	<style type="text/css">
		.width65px{width:105px;}
		.w150{width:130px;}
		.keyBlank{color:gray;}
		/*图标选中凹陷效果只有在ie9及其以上才有效果*/
		.icon_select{width:100px;background:#ccc;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
		.icon_unselect{width:100px;box-shadow:inset 1px 1px 0px 0px #999; border-radius:3px; height:23px; line-height:23px; display:inline-block; padding:0 15px 0 0;text-align:center; margin-left: 10px;}
	</style>
	<div id="jqueryToolbar">

		<form id="eventCaseQueryForm">

			<div class="ConSearch">

				<div class="fl" id="topSearchUl">
					<ul>
						<#if gridId??>
						<li>所属网格：</li>
						<li>
							<input id="infoOrgCode" name="infoOrgCode" type="text" class="hide queryParam" value=""/>
							<input id="gridId" name="gridId" type="text" class="hide queryParam" value=""/>
							<input id="gridName" name="gridName" type="text" class="inp1" style="width:150px;" value=""/>
						</li>
						</#if>

						<li>关键字：</li>
						<li><input name="keyWord" type="text" class="inp1 queryParam" id="keyWord" value="" style="color:gray; width:150px;" placeholder="事件描述/标题/事发详情"/></li>
						<li style="position:relative;">
							<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
							<div id="_advanceSearchDiv" class="AdvanceSearch DropDownList hide" style="width:320px; top: 42px; left: -130px;">
								<div class="LeftShadow">
									<div class="RightShadow">
										<div class="list NorForm" style="position:relative;">
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td>
															<label class="LabName width65px"><span>事件编号：</span></label>
															<input type="text" id="eventCode" name="code" class="inp1 InpDisable queryParam" style="width:135px;" value="" defaultValue="事件编号"  />
														</td>
													</tr>
													<#if typeFlag??>
													<tr>
														<td>
															<label class="LabName width65px"><span>事件分类：</span></label>
															<input id="eventType" name="type" type="text" value="" class="queryParam hide"/>
															<input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:135px;"/>
														</td>
													</tr>
													</#if>
												</table>
											</table>
										</div>
										<div class="BottomShadow"></div>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
				<div class="btns">
					<ul>
						<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
						<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
					</ul>
				</div>
				<div class="clear"></div>‍

			</div>
			<div class="h_10 clear"></div>
			<div class="ToolBar" id="toolbarDiv">
				<div class="blind"></div><!-- 文字提示 -->
				<script type="text/javascript">
					function DivHide() {
						$(".blind").slideUp();//窗帘效果展开
					}
					function DivShow(msg) {
						$(".blind").html(msg);
						$(".blind").slideDown();//窗帘效果展开
						setTimeout("this.DivHide()",800);
					}
				</script>
				<div id="toolFrDIV" class="tool fr">
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">
		var startInfoOrgCode = 'MzU=',
				taskReceivedStausComboBox = null,
				orderByFieldComboBox = null,
				createTimeDateRender = null;
		$(function(){
			<#if gridId??>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId,items){
				if(items!=undefined && items!=null && items.length>0){
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				}
			}, {
				ShowOptions: {
					EnableToolbar : true
				},
				Async : {
					enable : true,
					autoParam : [ "id=gridId"],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : ${gridId!}
					}
				}
			});
			</#if>

			<#if typeFlag??>
			AnoleApi.initTreeComboBox("typeName", "eventType", "A001093199", null, null, {
				ChooseType : "1",
				EnabledSearch : true,
				ShowOptions: {
					EnableToolbar : true
				}
			});
			</#if>
		});


		function resetCondition() {//重置
			$('#eventCaseQueryForm')[0].reset();
			// $('#keyWord').addClass('keyBlank');

			var queryParams=queryData();
			$("#list").datagrid('options').queryParams = queryParams;
			// orgtreeset.reset();
			// $("#infoOrgCode").val(startInfoOrgCode);

			searchData();
		}

		function searchData(isCurrent){//查询
			doSearch(queryData(), isCurrent);
		}

		function queryData() {
			var searchArray = new Array();
			var info = $("#infoOrgCode").val();
			$("#eventCaseQueryForm .queryParam").each(function() {
				var val = $(this).val(), key = $(this).attr("name");

				if($(this).hasClass("keyBlank")) {
					val = "";
				}
				if(isNotBlankString(val) && isBlankString(searchArray[key])){
					searchArray[key] = val;
				}
			});
			if("${signFlag}"){
				searchArray["signFlag"]="${signFlag}";
			}
			if("${status}"){
				searchArray["status"]="${status}";
			}
			if("${type}"&&isBlankString(searchArray["type"])){
				searchArray["type"]="${type}";
			}
			if("${department}"){
				searchArray["department"]="${department}";
			}
			if("${orgType}"){
				searchArray["orgType"]="${orgType}";
			}
			if("${hoOrgType}"){
				searchArray["hoOrgType"]="${hoOrgType}";
			}
			if("${orgLevel}"){
				searchArray["orgLevel"]="${orgLevel}";
			}
			if("${hoOrgLevel}"){
				searchArray["hoOrgLevel"]="${hoOrgLevel}";
			}
			if("${bizPlatform}"){
				searchArray["bizPlatform"]="${bizPlatform}";
			}
			if("${otherType}"){
				searchArray["otherType"]="${otherType}";
			}

			if("${infoOrgCode}"&&isBlankString(searchArray["infoOrgCode"])){
				searchArray["infoOrgCode"]="${infoOrgCode}";
			}
			if("${infoOrgId}"){
				searchArray["infoOrgId"]="${infoOrgId}";
			}
			if("${chiefLevel}"){
				searchArray["chiefLevel"]="${chiefLevel}";
			}
			if("${orgCode}"&&isBlankString(searchArray["orgCode"])){
				searchArray["orgCode"]="${orgCode}";
			}
			if("${orgId}"){
				searchArray["orgId"]="${orgId}";
			}
			if("${startTime}"){
				searchArray["createTimeStart"]="${startTime}";
			}

			if("${endTime}"){
				if ("${endTime}".length==10){
					searchArray["createTimeEnd"]="${endTime}"+' 23:59:59';
				}else {
					searchArray["createTimeEnd"]="${endTime}";
				}
			}
			return searchArray;
		}

		function doSearch(queryParams, isCurrent){
			$('#list').datagrid('clearSelections');
			$("#list").datagrid('options').queryParams = queryParams;

			if(isCurrent && isCurrent == true) {
				$("#list").datagrid('reload');
			} else {
				$("#list").datagrid('load');
			}
		}

		function reloadDataForSubPage(msg, isCurrent) {
			try{
				closeMaxJqueryWindow();
			} catch(e) {}

			if(msg) {
				DivShow(msg);
			}

			searchData(isCurrent);
		}


		function _onkeydown(){
			var keyCode = event.keyCode;
			if(keyCode == 13){
				searchData();
			}
		}

		function _onfocus(obj) {
			if($(obj).hasClass("keyBlank")){
				$(obj).val("");
				$(obj).removeClass('keyBlank')
			}
		}

		function _onblur(obj) {
			var keyWord = $(obj).val();

			if(keyWord == ''){
				$(obj).addClass('keyBlank');
				$(obj).val($(obj).attr("defaultValue"));
			}
		}

	</script>

	<div id="CustomJqueryWindow" class="easyui-window" title="信息窗口" minimizable="false" maximizable="true" collapsible="false" inline="false"
		 closed="true" modal="true" style="width:790px;height:480px;padding:1px; overflow:hidden;">
	</div>
</div>
	<div id="eventRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
		<table style="width:100%" id="list"></table>
	</div>
<script>

	$(function(){

		loadDataList();

	});

	function loadDataList(){

		var queryParams = queryData();

		$('#list').datagrid({
			idField: 'teamId',
			width:600,
			height:300,
			nowrap: true,
			rownumbers:true,
			remoteSort:false,
			striped: true,
			fit: true,
			fitColumns: true,
			singleSelect: true,
			pagination:true,
			pageSize: 20,
			toolbar:'#jqueryToolbar',
			url:'${rc.getContextPath()}/zhsq/zzgl/briefingController/getEventListData.json',
			queryParams:queryParams,
			columns: [[
				{field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true},//
				{field:'type',title:'type', align:'center',hidden:true},//
				{field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
				{field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.14),sortable:true},
				{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.14),sortable:true},
				{field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
				{field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.18),sortable:true, formatter: titleFormatter},
				{field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.1),sortable:true}
				,{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter}
			]],
			onLoadError: function () {//数据加载异常
				$('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
			},
			onSelect: function(index,rec) {
			},
			onDblClickRow:function(index,rec){
				showDetailRow(rec.eventId, rec.instanceId, rec.workFlowId,rec.type);
			},
			onLoadSuccess:function(data){
			if(data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
			},

		});

		//设置分页控件
		var p = $('#list').datagrid('getPager');
		$(p).pagination({
			pageSize: 20,//每页显示的记录条数，默认为
			pageList: [20,30,40,50],//可以设置每页记录条数的列表
			beforePageText: '第',//页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
		});

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
	function dateFormatter(value, rowData, rowIndex) {
		if(value && value.length >= 10) {
			value = value.substring(0,10);
		}

		return value;
	}
	function clickFormatter(value, rec, rowIndex) {
		var urgency = rec.urgencyDegree,
				urgencyName = rec.urgencyDegreeName,
				urgencyPic = "",
				// handleStatus = rec.handleDateFlag,
				remindStatus = rec.remindStatus,
				isAttention = rec.isAttention,
				wfStatus = rec.wfStatus,
				eventType = "all",
				value = value || "";

		if(value.length > 20) {
			value = value.substring(0,20);
		}

		var attentionVal = '';
		if(isAttention || eventType == "attention") {
			attentionVal = '<li class="guanzhu" onMouseover="showVal(this)" onMouseout="hideVal(this)"  onclick=attentionEvent(this,"'+ rec.isAttention+ '",'+rec.eventId+')>已关注</li>';
		} else {
			attentionVal = '<li class="guanzhu"  onclick=attentionEvent(this,"'+ rec.isAttention + '",'+rec.eventId+')>添加关注</li>';
		}

		// var handlePic = "";
		// if(handleStatus == '2'){
		// 	handlePic = '<i title="将到期" class="ToolBarDue"></i>';
		// } else if(handleStatus == '3'){
		// 	handlePic = '<i title="已过期" class="ToolBarOverDue"></i>';
		// }

		var influencePic = "";
		if(remindStatus == '2' || remindStatus == '3') {
			influencePic = '<img src="${rc.getContextPath()}/images/duban2.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
		}
		if(rec.influenceDegree == '04'){
			influencePic += "<b class='FontRed'>[重大]</b>";
		}

		var tab = '';
		var remindVal = "";

		if(wfStatus == '1') {
			remindVal += '<li class="line"></li>';

			remindVal += '<li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
		}

		if(remindVal) {
			tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
		}

		if(urgencyName && urgency != '01') {
			urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency"></i>';
		}

		var f = tab + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic;
		return f;
	}
	function showDetailRow(eventId,instanceId,workFlowId,bizPlatform,type){
		if(!eventId){
			$.messager.alert('提示','请选择一条记录！','info');
		}else{
			if(bizPlatform == "001"){
				var url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
				showMaxJqueryWindow("查看事件信息", url);
			}else{
				var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=c&cachenum=" + Math.random();
				showMaxJqueryWindow("查看事件信息", url, fetchWinWidth({maxWidth:$(document).width()}), fetchWinHeight({maxHeight: $(document).height()}), true);
			}
		}
	}
</script>
</body>
</html>