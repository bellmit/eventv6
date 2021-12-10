<script type="text/javascript">
	function BaseWorkflow4UrgeHandle_initParam() {//督办、催办初始化方法
		var reportType = $('#reportType').val();
		var initObj = {
			initUrge: {
				initUrgeUrl: "${rc.getContextPath()}/zhsq/reportFocus/capSmsContent.jhtml?smsTemplateName=remindNote-reportFocus-" + reportType + '&reportType=' + reportType
			},
			addUrge: {
	    		addUrgeUrl: '${rc.getContextPath()}/zhsq/reportFocus/addUrgeOrRemind.jhtml?reportType=' + reportType,
	    		addUrgeCallback: remindCallback
	    	},
	    	sendSms: {
	    		isSendSms: true,
	    		isRemote: false
	    	}
    	};
    	
    	return initObj;
	}
	
	function btnAuthority(index, rec) {
		if($('#remind').length || $('#recallWorkflow').length) {
			var reportStatus = null,
				END_STATUS = '60';
			
			if(rec) {
				reportStatus = rec.reportStatus;
			}
			
			if(reportStatus == END_STATUS) {
				$('#remind').hide();
				$('#recallWorkflow').hide();
			} else {
				$('#remind').show();
				$('#recallWorkflow').show();
			}
		}
	}
	
	function importReportFocus(opt) {
		var url = '${BDIO_URL!}/import/index.jhtml?t=' + Math.random();
		
		opt = opt || {};
		
		for(var index in opt) {
			url += '&' + index + '=' + opt[index];
		}
		
		openJqueryWindowByParams({
			scroll:"auto",
			maxWidth: 1200,
			maxHeight: 900,
			title: "导入信息",
			targetUrl: url
		});
	}

    function exportReportFocus(opt) {

        var url = '${rc.getContextPath()}/zhsq/reportFocus/exportReportFocusData.jhtml?';

        opt = opt || {};

        for(var index in opt) {
            url += '&' + index + '=' + opt[index];
        }

        //modleopen();
        $.messager.confirm('提示','确定导出数据吗？',function (r) {
            if(r){
                location.href = url;
                /*$.ajax({
                    type: "POST",
                    url: url,
                    data:{},
                    dataType: "json",
                    success: function (data) {
                        modleclose();
                        $.messager.alert('提示', '导出成功', 'info');
                    },
                    error: function (data) {
                        modleclose();
                        $.messager.alert('错误', '连接超时！', 'error');
                    }
                });*/
            }
        });
        //modleclose();
    }

	function remind(operateType) {
		var selectItem = $('#list').datagrid('getSelected'),
			instanceId = null,
			reportId = null;
		
		if(selectItem != null) {
			reportId = selectItem.reportId;
			instanceId = selectItem.instanceId;
		} else {
			$.messager.alert('警告','请选中要操作的记录再执行此操作!','warning');
			return;
		}
		
		var url = "${rc.getContextPath()}/zhsq/reportFocus/toAddUrgeOrRemind.jhtml?formId=" + reportId + "&instanceId=" + instanceId + "&operateType=" + operateType;
		
		openJqueryWindowByParams({
			maxWidth: 480,
			maxHeight: 300,
			title: "督办信息",
			targetUrl: url
		});
	}
	
	function remindCallback(data) {
		var msg = "操作失败！";
		
		if(data.result && data.result == true) {
			var msg = "催办成功！";
			
			if(data.operateType == 1) {
				msg = "督办成功！";
			}
		} else if(data.msgWrong) {
			msg = data.msgWrong;
		}
		
		reloadDataForSubPage(msg, true);
	}

	function addIcons() {
		var listType = '${listType!}';
		
		if(listType == '5') {
	        var iconDivLength = $("#iconDiv").length;
	        
	        if(iconDivLength == 0) {
	            var icons = '<div id="iconDiv" class="fl">' +
	            				'<a href="###" id="_allSearchAnchor" class="icon_select" onclick="allSearchData(this);" click="allSearchData" ><i class="ToolBarAll"></i>所有</a>' +
	            				'<a href="###" id="_superviseSearchAnchor" onclick="superviseSearchData(this);" click="superviseSearchData"><i class="ToolBarSupervise"></i>已督办</a>' +
	            			'</div>';
				
	            $('#toolbarDiv').append(icons);
	            
	            //快捷图标有变更，需要重新获取
	            iconLen = $('#iconDiv > a').length;
	            
	            if(iconLen > 5) {
	            	$('#iconDiv').width($('#iconDiv > a').outerWidth() * iconLen);
	            }
	        }
        }
        
        if($("#actionDiv").find("a").length > 0 || $('#iconDiv').length > 0) {
        	var toolBarWidth = $('#actionDiv').outerWidth(true),
        		toolBarArchorWidth = 0;
			
        	$("#actionDiv").show();
        	
        	$('#actionDiv > a').each(function(index) {
        		toolBarArchorWidth += $(this).outerWidth(true);
        	});
        	
        	if(toolBarArchorWidth < toolBarWidth || toolBarArchorWidth == 0) {
        		$('#actionDiv').width(toolBarArchorWidth);
        	}
        } else {
        	$("#toolbarDiv").remove();
        }
    }
    
    function allSearchData(obj, isCurrent) {//点击所有图标
        iconSelect(obj);
        
        searchData(isCurrent);
    }
    
    function superviseSearchData(obj, isCurrent) {//点击督办图标
    	var searchParam = {};
    	iconSelect(obj);
    	
    	searchParam.superviseMark = "1";
    	
    	searchData(isCurrent, searchParam);
    	
    	return searchParam;
    }
    
    function iconSelect(obj){//为选择的图标增添凹陷效果
        if($("#iconDiv > a > i.icon_select_pre").length == 0) {
            if(isNotBlankParam(obj)) {
            	var iconObj = null;
            	
                iconUnSelect();
                
                if(typeof obj === 'string') {
                	iconObj = $('#' + obj);
                } else if(typeof obj === 'object') {
                	iconObj = $(obj);
                }
                
                iconObj.addClass('icon_select');
            }
        }
    }

    function iconUnSelect(){//去除图片的凹陷效果
        $("#iconDiv > a[class='icon_select']").each(function(){
            $(this).removeClass('icon_select');
        });
    }
    
    function recallWorkflow() {
    	var selectItem = $('#list').datagrid('getSelected'),
			instanceId = null,
			reportId = null;
		
		if(selectItem == null) {
			$.messager.alert('警告','请选中要操作的记录再执行此操作!','warning');
			return;
		}
		
		reportId = selectItem.reportId;
		instanceId = selectItem.instanceId;
		
		if(reportId) {
			$.messager.confirm('提示', '您确定撤回选中的记录吗？', function(r) {
				if(r) {
					var reportStatus = selectItem.reportStatus;
					
					if(reportStatus) {
						if(status == '60') {
							$.messager.alert('警告','该记录无法进行撤回操作!','warning');
						} else if(instanceId) {
							modleopen();
							
							$.ajax({
								type: "POST",
								url: '${rc.getContextPath()}/zhsq/reportFocus/recallWorkflow4ReportFocus.jhtml',
								data: {'reportId': reportId, 'instanceId': instanceId, 'reportType': $('#reportType').val()},
								dataType:"json",
								success: function(data) {
									modleclose();
									
									if(data.success && data.success == true) {
										reloadDataForSubPage('记录撤回操作成功！', true);
									} else if(isNotBlankStringTrim(data.tipMsg)) {
										$.messager.alert('错误', data.tipMsg, 'error');
									} else {
										$.messager.alert('错误','记录撤回操作失败！','error');
									}
								},
								error:function(data) {
									$.messager.alert('错误','记录撤回操作失败！','error');
								}
							});
						} else {
							$.messager.alert('警告','该记录无法进行撤回操作!','warning');
						}
					} else {
						$.messager.alert('警告','该记录无法进行撤回操作!','warning');
					}
				}
			});
		} else {
			$.messager.alert('警告','请选择一条需要撤回操作的记录!','warning');
		}
    }
</script>