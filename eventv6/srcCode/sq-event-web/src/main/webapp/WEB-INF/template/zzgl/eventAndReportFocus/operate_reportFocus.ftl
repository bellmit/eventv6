<script type="text/javascript">
	/*operateParams 新增方法扩展参数
	*       dataSource 事件采集来源
	*/
	function add(operateParams) {
		var operateParamsStr = '';
		if(undefined != operateParams && typeof operateParams === "object") {
			for (var key in operateParams) {
				var params = ('&'+ key + '=' + operateParams[key]);
				operateParamsStr += params;
			}
		}
		var url = "${rc.getContextPath()}/zhsq/reportTwoVioPre/toAdd.jhtml?listType=" + $('#listType').val() + operateParamsStr;
		
		openJqueryWindowByParams({
			maxWidth: 902,
			maximizable: true,
			title: "新增两违事件",
			targetUrl: url
		});
	}
	
	function edit() {
		var reportUUID = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
			reportUUID = selectItem.reportUUID;
		}
		
		if(reportUUID == "") {
			$.messager.alert('警告','请选中要编辑的记录再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/reportTwoVioPre/toAdd.jhtml?listType=" + $('#listType').val() + "&reportUUID=" + reportUUID;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 902,
		  		maximizable: true,
		  		title: "编辑两违事件",
		  		targetUrl: url
		  	});
		}
	}
	
	function del() {
		var reportUUID = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
			reportUUID = selectItem.reportUUID;
		}
		
		if(reportUUID == "") {
			$.messager.alert('警告','请选中要删除的记录再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的两违事件吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/reportTwoVioPre/delReportFocus.jhtml',
						data:{'reportUUID': reportUUID},
						dataType:"json",
						success: function(data) {
							if (data.success && data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							}
						},
						error:function(data){
							$.messager.alert('错误','删除两违事件失败！','error');
						}
					});
				}
			});
		}
	}
	
	function detail(reportUUID, instanceId, listType) {
		if(reportUUID) {
			listType = listType || $("#listType").val();
			var url = '';
			var reportType =  $('#reportType').val();
			switch(reportType) {
			<#list reportTypeList as rtl>
			case '${rtl.REPORT_TYPE}'://${rtl.REPORT_TITLE}
				url = "${rc.getContextPath()}/zhsq/${rtl.REPORT_URL}/toDetail.jhtml?reportUUID=" + reportUUID + "&listType=" + listType + "&reportType=" + reportType;
				break;			
				</#list>           
			}
			
			if(instanceId) {
				url += "&instanceId=" + instanceId;
			}
			
			openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: "查看详情",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选择需要查看的记录!','warning');
		}
	}
</script>