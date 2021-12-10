<@override name="extraJs">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/other-task.css"/>
</@override>
<@override name="eventAddExtendContent">
	<div id="eventByInformantTelDiv" class="other-task hide" style="z-index: 2; width: 650px;">
		<div class="other-task-title">
			<div class="other-task-title-l bg-lssbsj">
				<i id="otherTaskClose" class="other-task-close" style="cursor: pointer;"><img src="${uiDomain!''}/web-assets/basic/other/oa/images/icon-r-back.png"/></i>
			</div>
			<div class="other-task-title-r bg-lssbsj">
				<p>举报历史</p>
			</div>
		</div>
		<div class="other-task-con" style="padding: 0;">
			<div class="other-task-context" style="width:100%; overflow:hidden; position:relative;">
				<table style="width:100%" id="eventByInformantTelList"></table>
			</div>
		</div>
	</div>
</@override>
<@override name="contactUserTr">
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span>受理人员：</span></label>
			<input  id="contactUser" name="contactUser" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="<#if event.eventId??>${event.contactUser!}</#if>" />
		</td>
	</tr>
</@override>
<@override name="contactTelTr">
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span>联系电话：</span></label>
			<input name="tel" id="tel" type="text" class="inp1 easyui-validatebox" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="<#if event.eventId??>${event.tel!}</#if>" />
		</td>
	</tr>
</@override>
<@override name="singleLineExtraInfoTr">
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span>举报人员：</span></label>
			<input  id="informant" name="informant" type="text" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="${event.informant!}" />
		</td>
	</tr>
	<tr>
		<td style="border-bottom:none;">
			<label class="LabName"><span>举报电话：</span></label>
			<div class="other-task-input">
				<input name="informantTel" id="informantTel" type="text" class="inp1 easyui-validatebox" style="width:100%;" data-options="tipPosition:'bottom',validType:'mobileorphone'" value="${event.informantTel!}" />
				<i id="informantTelSearch" class="other-task-open" title="举报历史" onclick="searchEventByInformantTel();" style="cursor: pointer; display: none;">
					<img src="${uiDomain!''}/web-assets/basic/other/oa/images/icon-sousuo.png"/>
				</i>
			</div>
		</td>
	</tr>
</@override>
<@override name="bigFileUploadInitOption">
	$.extend(bigFileUploadOpt, {
		fileExt: '.jpg,.gif,.png,.jpeg,.webp,.mp4,.mp3'
	});
</@override>
<@override name="extraSetting4eventType">
	OnChanged : function(eventType, items) {
		var eventName = $('#eventName').val();
		
		if(isBlankStringTrim(eventName) && isNotBlankParam(items) && items.length > 0) {
			$('#eventName').val(items[0].name);
		}
	},
</@override>
<@override name="initExpandScript">
	var eventByInformantTelDivWidth = $('#eventByInformantTelDiv').width(),
		isShowEventQuickSearch = '${isShowEventQuickSearch!false}';
	
	$('#content').height(152);
	
	if(isShowEventQuickSearch == 'true') {
		$('#otherTaskClose').on('click', function() {
			$('#eventByInformantTelDiv').animate({"right": 0 - eventByInformantTelDivWidth});
		});
		
		$('#eventByInformantTelDiv').css('right', 0 - eventByInformantTelDivWidth);
		$('#informantTelSearch').show();
		$('#eventByInformantTelDiv').show();
	}
</@override>
<@override name="defaultEventSourceValue"><#if event.eventId??>"${event.source!}"<#else>"08"</#if></@override>
<@override name="extraFtlInclude">
	<script type="text/javascript">
		function searchEventByInformantTel() {
			var informantTel = $('#informantTel').val();
			
			if(isNotBlankStringTrim(informantTel) && $('#informantTel').validatebox('isValid')) {
				$('#eventByInformantTelDiv').animate({"right":"0px"});
				loadEventByInformantTel(informantTel);
			}
		}
		
		function loadEventByInformantTel(informantTel) {
			$('#eventByInformantTelList').datagrid({
	            width:500,
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
	                {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true},
	                {field:'code',title:'事件编号', align:'center',width:fixWidth(0.4), formatter: clickFormatter},
	                {field:'typeName',title:'事件分类', align:'center',width:fixWidth(0.3)},
	                {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.2), formatter: dateFormatter}
	            ]],
	            pageSize: 20,
	            pagination:true,
	            queryParams:{'eventType' : 'all', 'informantTel' : informantTel},
	            onDblClickRow:function(index,rec){
	               showEventDetail(rec.eventId);
	            },
	            onLoadError: function () {//数据加载异常
	                $('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
	            }
	        });
			
	        //设置分页控件
	        var p = $('#eventByInformantTelList').datagrid('getPager');
	        $(p).pagination({
	            pageSize: 20,//每页显示的记录条数，默认为
	            pageList: [20,30,40,50,100,200],//可以设置每页记录条数的列表
	            beforePageText: '第',//页数文本框前显示的汉字
	            afterPageText: '页    共 {pages} 页',
	            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
	        });
		}
		
		function showEventDetail(eventId) {
			if(parent.showMaxJqueryWindow && typeof parent.showMaxJqueryWindow === 'function') {
				var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=" + eventId;
				parent.showMaxJqueryWindow("查看事件信息", url, null, null);
			}
		}
		
		function clickFormatter(value, rec, rowIndex) {
			var title = "";
			
			if(value) {
				title += '<a href="###" title="'+ value +'" onclick="showEventDetail(\'' + rec.eventId + '\')">' + value +'</a>';
			}
			
			return title;
		}
	
		function dateFormatter(value, rowData, rowIndex) {
			if(value && value.length >= 10) {
				value = value.substring(0,10);
			}
			
			return value;
		}
	</script>
</@override>
