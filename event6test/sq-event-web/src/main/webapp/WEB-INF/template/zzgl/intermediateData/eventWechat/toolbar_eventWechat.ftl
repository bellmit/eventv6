<#include "/component/ComboBox.ftl" />
<style type="text/css">
	.w150{width:150px;}
	.keyBlank{color:gray;}
</style>
<div id="jqueryToolbar" class="MainContent">
	<div class="ConSearch">
		<form id="eventWechatForm">
			<input type="hidden" id="verifyType" name="verifyType" value="${verifyType!}" />
			
	        <div class="fl">
	        	<ul>
	        		<#if isJurisdictionQuery?? && isJurisdictionQuery>
	        		<li>所属网格</li>
	        		<li>
	        			<input type="hidden" id="gridId" value="">
	        			<input type="text" class="hide queryParam" id="infoOrgCode" name="infoOrgCode" value="${infoOrgCode!}" />
	        			<input type="text" class="inp1" style="width:122px;" id="gridName" value="" />
	        		</li>
	        		</#if>
	        		<li>关键字：</li>
	        		<li><input type="text" id="keyWord" name="keyWord" class="inp1 w150 keyBlank" value="事件内容/标题/事发地址" defaultValue="事件内容/标题/事发地址" onfocus="_onfocus(this);" onblur="_onblur(this);" onkeydown="_onkeydown();" /></li>
	        		<li <#if status?? >style="display: none" </#if>>状态：</li>
	        		<li <#if status?? >style="display: none" </#if>>
	        			<input class="hide queryParam" id="status" name="status" value="${status!}" />
	        			<input type="text" class="inp1 w150" id="statusName" />
	        		</li>
	        		<@block name="advancedQueryBlock">
	        		<li style="position:relative;">
	        			<a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
	        			<div class="AdvanceSearch DropDownList hide" style="width:370px; top: 42px; left: -130px;">
	        				<div class="LeftShadow">
	        					<div class="RightShadow">
	        						<div class="list NorForm">
	        							<table width="100%" border="0" cellspacing="0" cellpadding="0">
	        								<tr>
	        									<td>
	        										<label class="LabName width65px"><span>事发时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
	        									</td>
	        								</tr>
	        								<tr id='bizPlatformTr'>
	        									<td>
	        									    <label class="LabName width65px"><span>信息来源：</span></label>
	        									    <input class="hide queryParam" id="bizPlatform" name="bizPlatformForSearch" value="${bizPlatform!}" />
	        			                            <input type="text" class="inp1 w150" id="bizPlatformNameStr" />
	        									</td>
	        								</tr>
	        							</table>
	        						</div>
	        					</div>
	        				</div>
	        			</div>
	        		</li>
	        		</@block>
	            </ul>
	        </div>
	        <div class="btns">
	        	<ul>            	
	            	<li><a href="#" class="chaxun" title="查询按钮" onclick="searchData()">查询</a></li>
	            	<li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
	            </ul>
	        </div>‍
        </form>
	</div>
	<div class="h_10 clear"></div>
	<div class="ToolBar" id="ToolBarDIV">
    	<div class="blind"></div>
    	<div class="tool fr" id="toolFrDIV"><@actionCheck></@actionCheck></div>
    </div>
	
</div>

<script type="text/javascript">
    var bizpcode="${EVENT_VERIFY_BIZPLATFORM_PCODE!}";
    var bizPlatformForSearchStr="${bizPlatformForSearch}";
    <@block name="reportType"></@block>
    

	$(function(){
		if($("#toolFrDIV").find("a").length == 0){
			$("#ToolBarDIV").hide();
		}
		
		AnoleApi.initListComboBox("statusName", "status", "${EVENT_VERIFY_STATUS_PCODE!}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
		
		<#if isJurisdictionQuery?? && isJurisdictionQuery>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				} 
			}, {
				OnCleared : function() {
					$("#infoOrgCode").val("");
				},
				ShowOptions : {
					EnableToolbar : true
				}
			});
		</#if>
		
		if(bizpcode!=null&&bizpcode!=''){
		
			if(bizPlatformForSearchStr!=null&&bizPlatformForSearchStr!=''){
				$("#bizPlatformTr").hide();
			}else{
			
				AnoleApi.initListComboBox("bizPlatformNameStr", "bizPlatform", "${EVENT_VERIFY_BIZPLATFORM_PCODE!}", null, null, {
					ShowOptions : {
						EnableToolbar : true
					}
				});
			}
		
		}else{
			$("#bizPlatformTr").hide();
		}
			
		<#if extraParam??>
			<#list extraParam?keys as mapKey>
				var inputObj = $('#eventExtendQueryForm input[name="${mapKey}"]');
				if(inputObj.length) {
					inputObj.val('${extraParam[mapKey]}');
				} else {
					$("#eventWechatForm").prepend('<input type="hidden" class="queryParam" name="${mapKey}" value="${extraParam[mapKey]}" />');
				}
			</#list>
		</#if>
	});
	
	<@block name="editFunctionBlock">
	function edit(option) {
		var eventVerifyHashId = "";
		$("input[name='eventVerifyHashId']:checked").each(function() {
			eventVerifyHashId = $(this).val();
		});
		if(eventVerifyHashId == "") {
			$.messager.alert('警告','请选中要审核的数据再执行此操作!','warning');
		} else {
			var opt = {
				'maxHeight': 410,
				'maxWidth': 720
			};
			var url = '${rc.getContextPath()}/zhsq/eventWechat/toEdit.jhtml?eventVerifyHashId=' + eventVerifyHashId,
				verifyType = $('#verifyType').val();
			
			if(isNotBlankStringTrim(verifyType)) {
				url += '&verifyType=' + verifyType;
			}
			
			if(option && typeof option === 'object') {
				for(var index in option) {
					url += '&' + index + '=' + option[index];
				}
			}
			
			opt.title = "审核事件信息";
			opt.targetUrl = url;
			
			var id = openJqueryWindowByParams(opt);
			var iframeContentWindow = $("#"+id+"_iframe").get(0).contentWindow;
			bigFileAddObserver(iframeContentWindow);
		}
	}
	</@block>
	
	<@block name="detailFunctionBlock">
	function detail(eventVerifyHashId) {
		var opt = {
			'maxHeight': 410,
			'maxWidth': 720
		};
		var url = '${rc.getContextPath()}/zhsq/eventWechat/toDetail.jhtml?eventVerifyHashId=' + eventVerifyHashId + '&verifyType',
			verifyType = $('#verifyType').val();
		
		if(isNotBlankStringTrim(verifyType)) {
			url += '&verifyType=' + verifyType;
		}
		
		opt.title = "查看事件信息";
		opt.targetUrl = url;
		
		var id = openJqueryWindowByParams(opt);
		var iframeContentWindow = $("#"+id+"_iframe").get(0).contentWindow; 
		bigFileAddObserver(iframeContentWindow);
	}
	</@block>
	
	function del(){
		var eventVerifyHashId = "";
		$("input[name='eventVerifyHashId']:checked").each(function() {
			eventVerifyHashId = $(this).val();
		});
		if(eventVerifyHashId == "") {
			$.messager.alert('警告','请选中要删除的数据再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的事件信息吗？', function(r) {
				if (r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/eventWechat/delEventWechat.jhtml',
						data: {'eventVerifyHashId' : eventVerifyHashId},
						dataType:"json",
						success: function(data) {
							modleclose();
							
							if(data.success && data.success == true){
			  					reloadDataForSubPage(data.tipMsg, true);
			  				} else {
			  					if(data.tipMsg) {
			  						$.messager.alert('错误', data.tipMsg, 'error');
			  					} else {
			  						$.messager.alert('错误', '删除失败！', 'error');
			  					}
			  				}
						},
						error:function(data){
							$.messager.alert('错误','连接超时！','error');
						}
					});
				}
			});
		}
	}
	
	function reportEventWechat(eventWechat) {
		var event = eventWechat;
		
		<@block name="jumpPageByEventType">
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml';
		</@block>
		
	  	var reportWinId = openJqueryWindowByParams(
	  	<@block name="reportEventWechatOption">
	  	{
	  		maxWidth: 1000,
	  		maxHeight: 500,
	  		title: "上报事件信息",
	  		targetUrl: url
		}
		</@block>
		);
	  	
	  	var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="'+ reportWinId +'_iframe" method="post">'+
						 '</form>';
		
		$("#jqueryToolbar").append($(reportForm));
		$("#_report4eventForm").append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));
		
		<@block name="reportEvtParam"></@block>
		
		$("#_reportEventJson").val(JSON.stringify(event));
		$("#_report4eventForm").attr('action', url);
		
		$("#_report4eventForm").submit();
		
		$("#_report4eventForm").remove();
	}

	function reportCallBack(callbackData, operateType) {
		var operateTypeName = "上报",
			status = "02",
			result = callbackData.result;
		
		operateType = operateType || 0;//为空时，默认设置为0
		
		switch(operateType) {
			case 1: {
				operateTypeName = "驳回";
				status = "03";
				break;
			}
		}
		
		if(result) {
			var selectedRow = $('#list').datagrid('getSelected'),
				operateTypeName = "上报",
				status = "02",
				ajaxData = {};
			
			modleopen();
			
			operateType = operateType || 0;//为空时，默认设置为0
			
			switch(operateType) {
				case 1: {
					operateTypeName = "驳回"; 
					status = "03";
					break;
				}
			}
			
			ajaxData = {
				'eventVerifyHashId': selectedRow.eventVerifyHashId,
				'bizId': selectedRow.bizId, 
				"status": status, 
				"operateType": operateType
			};
			
			<@block name="callbackData">
			if(callbackData.eventId) {
				ajaxData.eventId = callbackData.eventId;
			}
			</@block>
			
			$.ajax({
				type: "POST",
				url: "${rc.getContextPath()}/zhsq/eventWechat/saveEventWechat.jhtml",
				data: ajaxData,
				dataType:"json",
				success: function(data) {
					modleclose();
					
					var toClose = callbackData.toClose || '0',
						isSuccess = data.success && data.success == true;
					
					if(callbackData.userIds || toClose == '1' || operateType == '1') {//采集并上报、采集并结案、驳回操作
						var msg = null;
						
						if(isSuccess) {
							msg = operateTypeName + '成功！';
						}
						
						reloadDataForSubPage(msg, true);
					} else {//采集并提交
						var msg = callbackData.msg || callbackData.msgWrong;
						
						//刷新列表但不关闭操作窗口
						searchData(true);
						
						if(isSuccess && msg) {
							$.messager.alert('错误', msg, 'error', function() {
								reloadDataForSubPage(null, true);
							});
						}
					}
					
					if(!isSuccess) {//审核记录信息更新失败
	  					if(data.tipMsg) {
	  						$.messager.alert('错误', data.tipMsg, 'error');
	  					} else {
	  						$.messager.alert('错误', operateTypeName + '失败！', 'error');
	  					}
	  				}
				},
				error:function(data){
					modleclose();
					$.messager.alert('错误','连接超时！','error');
				}
			});
			
		} else {
			var msg = callbackData.msg || operateTypeName + '失败！';
			
			reloadDataForSubPage(msg, true);
		}
	}
	
	function authority(selectedRow) {
		if(selectedRow) {
			var showBtn = [];
			
			if(selectedRow.status == '01') {
				showBtn = ["EditBtn", "DelBtn"];
			}
			
			$("#toolFrDIV > a").hide();
			
			for(var index in showBtn) {
				$("#toolFrDIV ." + showBtn[index]).show();
			}
		}
	}
	
	<@block name="showEvent">
	function showEventWin(eventId) {
		if(eventId) {
			var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&eventId=' + eventId;
			var opt = 
			<@block name="showEventWinOption">
			{
				title: "查看事件信息",
				targetUrl: url
			}
			</@block>
			;
			
			openJqueryWindowByParams(opt);
		}
	}
	</@block>
	
	function fetchQueryParams() {
		var searchArray = new Array();
		
		if($("#keyWord").hasClass("keyBlank")){
			$("#keyWord").removeClass("queryParam");
		} else {
			$("#keyWord").addClass("queryParam");
		}
		
		$('#eventWechatForm .queryParam').each(function() {
			var queryObj = $(this),
				value = queryObj.val(),
				name = queryObj.attr("name");
			
			if(value) {
				searchArray[name] = value;
			}
			
		});
		
		return searchArray;
	}
	
	function searchData(isCurrent) {
		$('#list').datagrid('clearSelections');
		$("#list").datagrid('options').queryParams = fetchQueryParams();
		
		if(isCurrent && isCurrent == true) {
			$("#list").datagrid('reload');
		} else {
			$("#list").datagrid('load');
		}
	}
	
	function resetCondition() {
		$('#eventWechatForm')[0].reset();
		
		$('#keyWord').addClass('keyBlank');
		$("#status").val("");
		
		searchData();
	}
	
	function flashData() {//事件办理页面回调
		reloadDataForSubPage(null, true);
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
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		
		if(keyCode == 13){
			searchData();
		}
	}
	
	function DivHide(){
		$("#ToolBarDIV .blind").slideUp();//窗帘效果展开
	}
	
	function DivShow(msg){
		$("#ToolBarDIV .blind").html(msg);
		$("#ToolBarDIV .blind").slideDown();//窗帘效果展开
		setTimeout("this.DivHide()",800);
	}
</script>