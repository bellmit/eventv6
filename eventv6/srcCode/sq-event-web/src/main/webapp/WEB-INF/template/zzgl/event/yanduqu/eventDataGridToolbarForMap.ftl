<@override name ="generalParam">
	<input type="hidden" id="initiatorId" name="initiatorId" value="${initiatorId}">
	<input type="hidden" id="initiatorOrgId" name="initiatorOrgId" value="${initiatorOrgId}">
	<div class="fl">
                <ul>
                    <li>事件分类：</li>
                    <li>
                        <input id="type" name="type" type="text" value="" class="queryParam hide"/>
                        <input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:150px;"/>
                    </li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="width:400px; top: 42px; left: -130px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td>
                                                    <label class="LabName width65px"><span>事发时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeStart" name="happenTimeStart" value="${happenTimeStart!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="happenTimeEnd" name="happenTimeEnd" value="${happenTimeEnd!''}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px"><span>采集时间：</span></label><input class="inp1 Wdate fl queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!""}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input><span class="Check_Radio" style="padding:0 5px;">至</span><input class="inp1 Wdate fl queryParam" type="text" id="createTimeEnd" name="createTimeEnd" value="${createTimeEnd!""}" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"></input></td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div class="BottomShadow"></div>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
</@override> 
<@override name="anoleApiInitData"></@override> 
<@override name="changeEventList"></@override> 
<@override name="resetConditionFunction">
function resetCondition() {//重置
	$('#eventQueryForm')[0].reset();
    allSearchData($("#_allSearchAnchor"));
}
</@override>
<@override name="extendCondition">
<#if eventType?? && eventType == 'all' && eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
    <tr>
        <td>
            <label class="LabName width65px"><span>是否疑似重复：</span></label>
            <input type="text" id="isDuplicate" name="isDuplicate" class="hide queryParam"/>
            <input type="text" id="isDuplicateName" class="inp1 selectWidth" />
        </td>
    </tr>
</#if>
</@override>
<@override name="extendConditionInit">
<#if eventType?? && eventType == 'all' && eventAttrTrigger?? && eventAttrTrigger == '12345HotLine'>
        AnoleApi.initListComboBox("isDuplicateName", "isDuplicate", null, null, [""], {
        	DataSrc: [{"name":"是", "value":"1"},{"name":"否", "value":"0"}],
        	ShowOptions:{
                EnableToolbar : true
            }
        });
        
</#if>
</@override>

<@override name="extraQueryParams">
		postData["initiatorId"] = $("#initiatorId").val();
		postData["initiatorOrgId"] = $("#initiatorOrgId").val();
</@override>

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
			
			modleopen();
			
			$.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/event/eventDisposal4OuterController/fetchDataCount4Jsonp.jhtml',
                data: queryParam,
                dataType:"json",
                success: function(data) {
                	modleclose();
                	
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

<@override name="exclusiveFunction">
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
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />