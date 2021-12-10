<script type="text/javascript">
	function add() {
		var url = "${rc.getContextPath()}/zhsq/reportFeedback/toForm.jhtml?";
		
		openJqueryWindowByParams({
			maxWidth: 900,
			maximizable: true,
			title: "新增入格事件",
			targetUrl: url
		});
	}
	
	function edit() {
		var seUUId = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
            seUUId = selectItem.seUUId;
		}
		
		if(seUUId == "") {
			$.messager.alert('警告','请选中要编辑的记录再执行此操作!','warning');
		} else {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toForm.jhtml?seUUId=" + seUUId;
		  	openJqueryWindowByParams({
		  		maxWidth: 900,
		  		maximizable: true,
		  		title: "编辑入格事件",
		  		targetUrl: url
		  	});
		}
	}
	
	function del() {
		var seUUId = "", selectItem = $('#list').datagrid('getSelected');
		
		if(selectItem != null) {
            seUUId = selectItem.seUUId;
		}
		
		if(seUUId == "") {
			$.messager.alert('警告','请选中要删除的记录再执行此操作!','warning');
		} else {
			$.messager.confirm('提示', '您确定删除选中的入格事件吗？', function(r) {
				if(r){
					modleopen();
					
					$.ajax({
						type: "POST",
						url: '${rc.getContextPath()}/zhsq/reportFeedback/delReportSend.jhtml',
						data:{'seUUId': seUUId},
						dataType:"json",
						success: function(data) {
							if (data.success && data.success == true) {
								modleclose();
								reloadDataForSubPage(data.tipMsg, true);
							}
						},
						error:function(data){
							$.messager.alert('错误','删除入格事件失败！','error');
						}
					});
				}
			});
		}
	}
	
	function detail(fbUUId,docheck) {
		if(fbUUId) {
			var url = "${rc.getContextPath()}/zhsq/reportFeedback/toFeedbackDetail.jhtml?fbUUId=" + fbUUId + "&fromPage=listPage&doCheck="+docheck;
			openJqueryWindowByParams({
				maxWidth: 900,
				maximizable: true,
				title: "查看反馈信息",
				targetUrl: url
			});
		} else {
			$.messager.alert('警告','请选择需要查看的记录!','warning');
		}
	}
</script>