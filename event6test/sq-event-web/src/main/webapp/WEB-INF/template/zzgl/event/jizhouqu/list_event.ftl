<@override name="eventListPageTitle">
	江西省平台吉州区事件列表页面
</@override>

<@override name="function_clickFormatter">
function clickFormatter(value, rec, rowIndex) {
	<@block name="function_clickFormatter_body">
    var urgency = rec.urgencyDegree,
        urgencyName = rec.urgencyDegreeName,
        urgencyPic = "",
        eventSource = rec.source,
        sourcePic = "",
        handleStatus = rec.handleDateFlag,
        remindStatus = rec.remindStatus,
        isAttention = rec.isAttention,
        wfStatus = rec.wfStatus,
        eventType = "${eventType!}",
        value = value || "";

    if(value.length > 20) {
        value = value.substring(0,20);
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

    var influencePic = "";
    
    if(remindStatus == '2' || remindStatus == '3') {
		var supervisionType = rec.supervisionType;//改为实际值
		influencePic = '<img src="${rc.getContextPath()}/images/duban'+ supervisionType +'.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
	}

    if(rec.influenceDegree == '04'){
        influencePic += "<b class='FontRed'>[重大]</b>";
    }

    var tab = '';
<#if eventType == "all" || eventType == "toRemind">
    var remindVal = "";

    if(wfStatus == '1') {
		<#if isShowAttentionBtn?? && isShowAttentionBtn>
            remindVal += '<li class="line"></li>';
		</#if>

        remindVal += '<li class="duban" onclick=remindEvent1(this,'+rec.eventId+','+rec.instanceId+')>督办</li>';
    }

    if(remindVal) {
        tab = '<div class="OperateNotice" style="display:none"><div class="operate"><ul>'+attentionVal+remindVal+'</ul><div class="arrow"></div></div></div>';
    }
<#elseif eventType == "my">
    if(wfStatus == '1') {
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
    
    if(eventSource === '16') {
    	sourcePic = '<img src="${rc.getContextPath()}/images/codeOffice.png" style="margin:0 10px 0 0; width:28px; height:28px;">';
    }

    var f = tab + sourcePic + influencePic+'<a class="eName" href="###" title="'+ rec.eventName +'" onclick=showDetailRow('+ rec.eventId+ ','+rec.instanceId+','+rec.workFlowId+',"'+rec.bizPlatform+'","'+rec.type+'")>'+value+'</a>'+urgencyPic+handlePic;
    return f;
    </@block>
}
</@override>

<@extends name="/zzgl/event/jiangXiProvincePlatform/list_event.ftl" />