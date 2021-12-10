<script type="text/javascript">
	function add(opt) {
		var operateParamsStr = '';
		var url = "${rc.getContextPath()}/zhsq/reportBusPro/toAdd.jhtml?listType=" + $('#listType').val();
		
		if(undefined != opt && typeof opt === "object") {
			for (var key in opt) {
				var params = ('&'+ key + '=' + opt[key]);
				operateParamsStr += params;
			}
		}
		
		if(isNotBlankParam(operateParamsStr)) {
			url += operateParamsStr;
		}
		
		openJqueryWindowByParams({
			maxWidth: 900,
			maximizable: true,
			title: "新增营商环境问题信息",
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
			var url = "${rc.getContextPath()}/zhsq/reportBusPro/toAdd.jhtml?listType=" + $('#listType').val() + "&reportUUID=" + reportUUID;
			
		  	openJqueryWindowByParams({
		  		maxWidth: 900,
		  		maximizable: true,
		  		title: "编辑营商环境问题信息",
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
			$.messager.confirm('提示', '您确定删除选中的营商环境问题信息吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/reportBusPro/delReportFocus.jhtml',
						data:{'reportUUID': reportUUID},
						dataType:"json",
						success: function(data) {
							if (data.success && data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							}
						},
						error:function(data){
							$.messager.alert('错误','删除营商环境问题信息失败！','error');
						}
					});
				}
			});
		}
	}
	
	function detail(reportUUID, instanceId, listType,reportStatus,dataSource) {
		if(reportUUID) {
			listType = listType || $("#listType").val();
			
			var url = "${rc.getContextPath()}/zhsq/reportBusPro/toDetail.jhtml?reportUUID=" + reportUUID + "&listType=" + listType + "&reportType=" + $('#reportType').val();
			
			if(instanceId) {
				url += "&instanceId=" + instanceId;
			}
			
			if(listType=='2'
				&&(dataSource=='03'||dataSource=='04'||dataSource=='06'||dataSource=='07')
				&&(reportStatus=='99'||reportStatus=='00')){
				$.messager.alert('提示','请在客户端查看需要的记录!','warning');
				return ;
			}
			
			openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: "查看营商环境问题信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选择需要查看的记录!','warning');
		}
	}
</script>