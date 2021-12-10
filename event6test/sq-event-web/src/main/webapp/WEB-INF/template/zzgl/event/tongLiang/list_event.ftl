<@override name="eventListPageTitle">
	重庆市铜梁区事件列表页面
</@override>
<@override name="extraListParam">
	<input  class="hide queryParam" id="getHandleDateLight" name="getHandleDateLight" value="true" />
</@override>
<@override name="createTimeSearch">
    <li class="eventCreateTimeLi">采集时间：</li>
    <li class="eventCreateTimeLi">
        <input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!}"></input>
        <input class="inp1 hide queryParam" type="text" id="createDateTimeEnd" name="createDateTimeEnd" value="${createDateTimeEnd!}"></input>
        <input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:310px;" value="${createTimeStart}<#if createTimeStart??> 00:00:00</#if><#if createTimeStart?? && createDateTimeEnd??> ~ </#if>${createDateTimeEnd!}"/>
    </li>
</@override>
 <@override name="eventStatusTrBlock">
     <#if eventType=="all" || eventType=="attention" ||eventType='done'>
         <tr>
             <td><label class="LabName width65px"><span>当前状态：</span></label>
                 <select name="statuss" id="statuss" class="easyui-combobox" style="width: 170px; height:28px;" data-options="panelHeight:100,multiple:true,onSelect:function(record){comboboxSelect(record, this.id);},onUnselect:function(record){comboboxUnselect(record, this.id)}">
                     <#if statusDC??>
                         <option value="">不限</option>
                         <#list statusDC as l>
                             <#if l.dictGeneralCode != "06" && l.dictGeneralCode != "99" && l.dictGeneralCode != "05">
                                 <option value="${l.dictGeneralCode}">${l.dictName}</option>
                             </#if>
                         </#list>
                     </#if>
                 </select>
             </td>
         </tr>
     </#if>
 </@override>
<@override name="createTimeDateRenderInit">
    if($('#_createTimeDateRender').length > 0) {
        createTimeDateRender = init4DateRender('_createTimeDateRender', 'createTimeStart', 'createDateTimeEnd');
    }
</@override>
<@override name="anoleDateRenderFormat">
        var dateRender = null;

        dateRender = $('#' + renderId).anoleDateRender({
            format:'yyyy-MM-dd HH:mm:ss',
            BackfillType : "1",
            ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
            ShowOptions : {
                TabItems : ["常用", "年", "季", "月", "清空"]
            },
            BackEvents : {
                OnSelected : function(api,typeName) {
                    var _start = "", _end = "";
                    if (typeName == "自定义") {
                        _start = api.getStartDate();
                        _end = api.getEndDate();
                    } else {
                        _start = api.getStartDate("yyyy-MM-dd")+ " 00:00:00";
                        _end = api.getEndDate("yyyy-MM-dd") + " 23:59:59";
                    }

                    $("#" + startTimeId).val(_start);
                    $("#" + endTimeId).val(_end);
                    $("#" + renderId).val(_start + ' ~ ' + _end);
                },
                OnCleared : function() {
                    $("#" + startTimeId).val('');
                    $("#" + endTimeId).val('');
                }

            }
        }).anoleDateApi();

        return dateRender;
</@override>
<@override name="extendConditionInit">
    $('#__end_dateINPUT_createTimeDateRenderundefined').css({'width':'155px'});
    $('#__start_dateINPUT_createTimeDateRenderundefined').css({'width':'155px'});
    $('#date_INPUT_createTimeDateRenderundefined_div_0').css({'width':'370px'});
    $('#date_INPUT_createTimeDateRenderundefined_div_0 >div.nav').css({'width':'370px'});
    $('#date_INPUT_createTimeDateRenderundefined_div_0 >div.con').css({'width':'370px'});
</@override>
<@override name="statussResetCondition">
    <#if eventType=="all" || eventType=="attention" || eventType=="done">
        $('#statuss').combobox('setValue', "");
        <#if statuss??>
            var statusArr = "${statuss}".split(',');
            if($('#statuss').val()!=null)
            $('#statuss').combobox('setValues', statusArr);
        </#if>
    </#if>
</@override>
<@override name="advancedStatussSearch">
    <#if eventType=="all" || eventType=="attention" || eventType=="done">
        var eventStatus = "";
        var statuss = $('#statuss').combobox('getValues');

        if(statuss.length > 0){
            var status = "";

            for(var i=0;i<statuss.length;i++){
                status = statuss[i];

                if(status == 'do'){
                    eventStatus += ',00,01,02,03';
                } else if(status == 'end'){
                    eventStatus += ',04';
                } else {
                    eventStatus += "," + status;
                }
            }

            if(eventStatus && eventStatus.length > 0) {
                eventStatus = eventStatus.substring(1);
            }
        }
        if(eventStatus!=null && eventStatus!="") {
            postData["eventStatus"]=eventStatus;
        }
    </#if>
</@override>
<#if eventType=="all">
    <@override name="generalColumns">
        columns:[[
        {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true,outMenu:true},
        {field:'type',title:'type', align:'center',hidden:true,outMenu:true},

        <@block name="checkbox"></@block>

        {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortName:'T1.EVENT_NAME',sortable:true,formatter: clickFormatter,outMenu:true},
        {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.18),sortName:'T1.TYPE_',sortable:true, formatter: titleFormatter},
        {field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.18), formatter: titleFormatter},
        {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.15),sortName:'T1.HAPPEN_TIME',sortable:true, formatter: titleFormatter},
        {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.15),sortName:'T1.CREATE_TIME',sortable:true, formatter: titleFormatter},
        {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.1), formatter: titleFormatter},
        {field:'finTimeStr',title:'办结时间', align:'center',width:fixWidth(0.15),sortName:'T1.FIN_TIME',sortable:true, formatter: titleFormatter},
        {field:'contactUser',title:'联系人员', align:'center',width:fixWidth(0.1),sortName:'T1.CONTACT_USER',sortable:true, formatter: titleFormatter},
        {field:'tel',title:'联系电话', align:'center',width:fixWidth(0.1),sortName:'T1.TEL',sortable:true, formatter: titleFormatter}
        <@block name="extraListColumn"></@block>
        ]],
    </@override>
<#elseif eventType=="todo" || eventType="done" || eventType="history">
    <@override name="handleDateStrField">
        {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.15),sortName:'T1.CREATE_TIME',sortable:true, formatter: titleFormatter},
    </@override>
    <@override name="createTimeStrField">,</@override>
</#if>
<@override name="function_clickFormatter_body">
    var urgency = rec.urgencyDegree,
            urgencyName = rec.urgencyDegreeName,
            urgencyPic = "",
            handleDateLight = rec.handleDateLight,
            handleStatus = rec.handleDateFlag,
            remindStatus = rec.remindStatus,
            isAttention = rec.isAttention,
            wfStatus = rec.wfStatus,
            eventStatus = rec.status,
            isShowUrge = true,
            eventType = "${eventType!}",
            value = value || "";

    if(value.length > 20) {
        value = value.substring(0,20);
    }
    
    if(eventStatus=='04'){
    	isShowUrge=false;
    }

    var attentionVal = '';
<#if isShowAttentionBtn?? && isShowAttentionBtn>
    if(isAttention) {
        attentionVal = '<li class="guanzhu" onMouseover="showVal(this)" onMouseout="hideVal(this)"  onclick=attentionEvent(this,"'+ rec.isAttention+ '",'+rec.eventId+')>已关注</li>';
    } else {
        attentionVal = '<li class="guanzhu"  onclick=attentionEvent(this,"'+ rec.isAttention + '",'+rec.eventId+')>添加关注</li>';
    }
</#if>

    var handlePic = "";
<#if eventType != 'draft'>
    if(handleStatus == '2'){
        handlePic = '<i title="将到期" class="ToolBarDue"></i>';
    } else if(handleStatus == '3'){
        handlePic = '<i title="已过期" class="ToolBarOverDue"></i>';
    }
</#if>

    var influencePic = "";
    
    if(handleDateLight){
        if(handleDateLight == 'greenAndYellow' || handleDateLight =='yellowAndRed'){
            influencePic = '<img src="${uiDomain}/web-assets/common/images/'+ handleDateLight +'Light.gif" style="margin:0 10px 0 0; width:28px; height:28px;">';
        }else{
            influencePic = '<img src="${uiDomain}/web-assets/common/images/'+ handleDateLight +'Light.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
        }
    }
    
    if(remindStatus == '2' || remindStatus == '3') {
		var supervisionType = rec.supervisionType;//改为实际值
		influencePic += '<img src="${rc.getContextPath()}/images/duban'+ supervisionType +'.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
	}

    if(rec.influenceDegree == '04'){
        influencePic += "<b class='FontRed'>[重大]</b>";
    }

    var tab = '';
<#if eventType == "all" || eventType == "toRemind">
    var remindVal = "";

    if(wfStatus == '1' && isShowUrge) {
		<#if isShowAttentionBtn?? && isShowAttentionBtn>
            remindVal += '<li class="line"></li>';
		</#if>

        remindVal += '<li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
    }

    if(remindVal) {
        tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
    }
<#elseif eventType == "my">
    if(wfStatus == '1' && isShowUrge) {
        var urgeVal = '<li class="cuiban" onclick=urgeEvent(this,'+rec.eventId+','+rec.instanceId+')>催办</li>';
        tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+urgeVal+'</ul><div class="arrow"></div></div></div>';
    }
<#elseif eventType == "todo">
    if(remindStatus == '1' || remindStatus == '3') {
        urgencyPic += '<i title="催办" class="ToolBarRemind"></i>';
    }
</#if>

    if(urgencyName && urgency != '01') {
        urgencyPic += '<i title="'+ urgencyName +'" class="ToolBarUrgency"></i>';
    }

    var f = tab + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
    return f;
</@override>



<@override name="selfDefBtnAuthority">
	if(status == '04') {
    	$('#reHandleEvent').show();
    } else {
    	$('#reHandleEvent').hide();
    }
</@override>

<@override name="exclusiveFunction">

function reHandleEvent() {
	if(idStr) {
		$.messager.confirm('提示', '您确定将选中的事件退回到结案人手中重新办理吗？', function(r) {
			if(r) {
    			var status = null;
    			
	            if($('#list').length) {
	                var item = $('#list').datagrid('getSelections');
	                if(item.length == 1) {
	                    status = item[0].status;
	                }
	            } else if($("#"+ idStr +"_status").length) {
	                status = $("#"+ idStr +"_status").val();
	            }
	
	            if(status) {
	            	if(status != '04') {
	            		$.messager.alert('警告','该事件还未归档，无法进行退回到结案人办理操作!','warning');
	            	} else if(_instanceId) {
	            		modleopen();
	            		
	            		$.ajax({
	            			type: "POST",
	            			url: '${rc.getContextPath()}/zhsq/workflow/workflowController/rejectInstance.jhtml',
	            			data: {'instanceId': _instanceId,'eventId':idStr,'taskId':-1,'advice':'督导组检查驳回'},
	            			dataType:"json",
	            			success: function(data) {
	            				modleclose();
	            				
	            				if(data.result == 'success') {
	            					$.messager.alert('提示', '事件退回到结案人办理操作成功！', 'info');
	            					
	            					<#if model?? && model == 'l'>
	            						loadMessage((pageNum?pageNum:1),$('#pageSize').val());
	            					<#else>
	            						flashData(true);
	            					</#if>
	            				} else if(isNotBlankStringTrim(data.msgWrong)) {
	            					$.messager.alert('错误', data.msgWrong, 'error');
	            				} else {
	            					$.messager.alert('错误','事件退回到结案人办理操作失败！','error');
	            				}
	            			},
	            			error:function(data) {
	            				$.messager.alert('错误','事件退回到结案人办理操作失败！','error');
	            			}
	            		});
	            	} else {
	            		 $.messager.alert('警告','该事件无法进行退回到结案人办理的操作!','warning');
	            	}
	            } else {
	                $.messager.alert('警告','该事件无法进行退回到结案人办理的操作!','warning');
	            }
            }
		});
    } else {
        $.messager.alert('警告','请选择一条需要退回到结案人办理的事件!','warning');
    }
}


</@override>


<@extends name="/zzgl/event/list_event.ftl" />