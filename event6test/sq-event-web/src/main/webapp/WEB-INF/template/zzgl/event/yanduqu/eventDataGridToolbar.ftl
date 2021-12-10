<@override name="extraStyle">
	.wsb {width: 400px;height: 170px;text-align: center;overflow-y: auto;}
	.wsb span{display: block;padding: 3px;font-size: 16px; overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
</@override>

<@override name="extraListParam">
	<div id="dd"  data-options="
			buttons:[{
				text:'关闭',width:70,height:30,
				handler:function(){
					$('#dd').window('close');
				}
			}]">
	</div>
</@override>

<#if eventType?? && eventType == 'all' && eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
	<@override name="extendCondition">
		<tr>
			<td>
				<label class="LabName width65px"><span>是否疑似重复：</span></label>
				<input type="text" id="isDuplicate" name="isDuplicate" class="hide queryParam"/>
				<input type="text" id="isDuplicateName" class="inp1 selectWidth" />
			</td>
		</tr>
		<tr>
			<td>
				<label class="LabName width65px"><span>是否自采自结：</span></label>
				<input type="text" id="isSelfCollectSettle" name="isSelfCollectSettle" class="hide queryParam"/>
				<input type="text" id="isSelfCollectSettleName" class="inp1 selectWidth" />
			</td>
		</tr>
	</@override>
	
	<@override name="collectWaysCondition"></@override>
	<@override name="influenceDegreesCondition"></@override>
	<@override name="urgencyDegreesCondition"></@override>
	
	<@override name="extendConditionInit">
		AnoleApi.initListComboBox("isDuplicateName", "isDuplicate", null, null, [""], {
        	DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
        	ShowOptions:{
                EnableToolbar : true
            }
        });
        
        AnoleApi.initListComboBox("isSelfCollectSettleName", "isSelfCollectSettle", null, null, [""], {
        	DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
        	ShowOptions:{
                EnableToolbar : true
            }
        });
	</@override>
	
	<@override name="collectWaysConditionInit"></@override>
	<@override name="influenceDegreesConditionInit"></@override>
	<@override name="urgencyDegreesConditionInit"></@override>
	
<#else>
	<#if eventType?? && eventType == 'all'>
		<@override name="extendCondition">
			<tr>
				<td>
					<label class="LabName width65px"><span>是否自采自结：</span></label>
					<input type="text" id="isSelfCollectSettle" name="isSelfCollectSettle" class="hide queryParam"/>
					<input type="text" id="isSelfCollectSettleName" class="inp1 selectWidth" />
				</td>
			</tr>
		</@override>
		
		<@override name="extendConditionInit">
	        AnoleApi.initListComboBox("isSelfCollectSettleName", "isSelfCollectSettle", null, null, [""], {
	        	DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
	        	ShowOptions:{
	                EnableToolbar : true
	            }
	        });
		</@override>
	</#if>
	
</#if>

<@override name="listLoadSuccessExtraOperate">
	var eventAttrTrigger = "${eventAttrTrigger!}";
	
	if(eventAttrTrigger == '12345HotLine') {
		var listTotal = data.total,
			  eventDuplicationRate = '0.0',
			  queryParam = queryData(),
			  isDuplicate = queryParam.isDuplicate;
		
		if($('#_eventDuplicationRateSpan').length == 0) {
			$('.ToolBar').append('<div style="line-height: 32px;">截止当前事件重复率：<span id="_eventDuplicationRateSpan" >0.0</span>%</div>');
		}
		
		if(isBlankParam(isDuplicate) && listTotal > 0) {
			queryParam["listType"] = 5;
			
			queryParam["isDuplicate"] = 1;
			
			//modleopen();//由于会与列表的数据加载遮罩层相冲突，故暂时屏蔽
			
			$.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/event/eventDisposal4OuterController/fetchDataCount4Jsonp.jhtml',
                data: queryParam,
                dataType:"json",
                success: function(data) {
                	//modleclose();//由于会与列表的数据加载遮罩层相冲突，故暂时屏蔽
                	
                	data = eval("("+data+")");
                	
                	var duplicationRate = (data.total / listTotal) * 100 ;
                	eventDuplicationRate =  duplicationRate.toFixed(1);//保留一位小数
                	
                	$('#_eventDuplicationRateSpan').html(eventDuplicationRate);
                },
                error:function(data) {
                	modleclose();
                    $.messager.alert('错误', '事件覆盖率获取失败！', 'error');
                }
            });
		} else {
			if(isDuplicate == '1') {
				eventDuplicationRate = '100.0';
			}
		
			$('#_eventDuplicationRateSpan').html(eventDuplicationRate);
		}
	}
	 
</@override>

<@override name="function_addIcons_body">
	var eventAttrTrigger = "${eventAttrTrigger!}";
	
	if(eventAttrTrigger == '12345HotLine') {
		return;
	} else {
		<@super></@super>
	}
</@override>

<@override name="exclusiveFunction">
 function subShenHe(){
    	var rows = $('#list').datagrid('getSelections');
    	if(rows.length == 0){
    		 $.messager.alert('提示', '请选中要提交审核的事件！', 'info');
    		 return;
    	}else{
    		$.messager.confirm('提示', '确认将勾选事件推送到领导通APP中?', function(r){
    			if(r){
    				modleopen();
    				var ids = [];
			    	for(var i=0,len=rows.length;i<len;i++){
			    		ids.push(rows[i].eventId);
			    	}
					$.ajax({
			                type: "POST",
			                url: '${rc.getContextPath()}/zhsq/eventPush/save.json',
			                data: 'ids='+ids,
			                dataType:"json",
			                success: function(data){
			                    modleclose();
			                    if(data.result == "fail" && data.eventNames != null && data.eventNames.length > 0){
			                    	
			                    	var tipStr = "";
			                    	for(var i = 0,len = data.eventNames.length;i<len;i++){
			                    		tipStr += "<span>"+data.eventNames[i].eventName + "</span>";
			                    	}
			                    	
			                    	$('#dd').dialog({    
									    title: '以下事件已提交审核',    
									    width: 400,    
									    height: 250,    
									    closed: false,    
									    cache: false,      
									    modal: true,								    
									    content:'<div class="wsb"><div style="width:350px;margin:auto;">'+tipStr+'</div></div>'								
									 }) 
									 
			                    }else if(data.result == "success"){
			                    	$.messager.alert('提示', '事件提交成功!', 'info');
			                    }
			                },
			                error:function(data){
			               	 	modleclose();
			                    $.messager.alert('错误','连接超时！','error');
			                }
	            	});
	    		}
	    	});
    	}
    }
    
    function eventCheck() {//核验事件
    	if(isBlankStringTrim(idStr)) {
            $.messager.alert('提示','请选择一条需要核验的事件记录!','warning');
            return;
        }
        
        var url = "${rc.getContextPath()}/zhsq/eventExtend/toAddTimeApplication.jhtml?applicationType=10&eventId=" + idStr;
        
        openJqueryWindowByParams({
        	title: "核验事件信息",
        	targetUrl: url,
        	maxHeight: 280,
        	maxWidth: 480
        });
    }
    
    function iconFormatter(value, rec, rowIndex) {
		var urgency = rec.urgencyDegree,
			  urgencyName = rec.urgencyDegreeName,
			  remindStatus = rec.remindStatus,
			  eventType = "${eventType!}",
			  urgencyPic = "",
			  influencePic = "";
		
		if(remindStatus == '2' || remindStatus == '3') {
			var supervisionType = rec.supervisionType;//改为实际值
			
			 influencePic = '<i title="已督办" class="ToolBarSupervise" style="margin: 0;"></i>'
		}
		
		if(eventType == 'todo' && (remindStatus == '1' || remindStatus == '3')) {
			urgencyPic += '<i title="催办" class="ToolBarRemind" style="margin: 0;"></i>';
		}
		
		if(urgencyName && urgency != '01') {
			urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency" style="margin: 0;"></i>';
		}
		
		return influencePic + urgencyPic + ' ';
	}
	
	function handleDateStrFormatter(value, rec, rowIndex) {
		var handlePic = "", 
			  handleStatus = rec.handleDateFlag,
			  eventType = "${eventType!}";
		
		if(eventType != 'draft') {
			if(handleStatus == '2') {
				handlePic = '<i title="将到期" class="ToolBarDue" style="margin: 0 0 2px 0;"></i>';
			} else if(handleStatus == '3') {
				handlePic = '<i title="已过期" class="ToolBarOverDue" style="margin: 0 0 2px 0;"></i>';
			}
		}
		
		if(value) {
			handlePic += ' ' + value;
		}
		
		return handlePic;
	}
	
	function editEventAttr(zonePath) {
		if(isBlankStringTrim(idStr)) {
			$.messager.alert('提示','请选择一条需要编辑的事件记录!','warning');
			return;
		}
		
		var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toEditEventAttr.jhtml?eventId=' + idStr;
		
		if(zonePath) {
			url += '&zonePath=' + zonePath;
		}
		
		showMaxJqueryWindow("编辑事件信息", url, 400, 260);
	}
</@override>

<@override name="selfDefBtnAuthority">
	if(status == '00' || status == '01' || status == '02' || status == '03') {
		$('#eventCheck').show();
	} else {
		$('#eventCheck').hide();
	}
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />