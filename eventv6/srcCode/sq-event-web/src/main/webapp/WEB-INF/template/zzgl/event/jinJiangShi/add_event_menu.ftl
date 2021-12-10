<@override name="btnListContent">
	<#if isShowSaveBtn?? && !isShowSaveBtn>
	<#else>
		<a href="###" onclick="showAdvice('saveEvent');" class="BigNorToolBtn SaveBtn">保存</a>
	</#if>
	
	<#if (isReport?? && isReport)>
		<a href="###" onclick="showAdvice('saveEventAndReport');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	<#else>
		<a href="###" onclick="showAdvice('saveEventAndStart', 'startWorkFlow');" class="BigNorToolBtn BigShangBaoBtn">提交</a>
	</#if>
</@override>

<@override name="function_flashData_body">
	$("#tableForm").attr("action", document.URL);
	if(isNotBlankString(msg)) {
		$.messager.alert('', msg, 'info',function() {
			$("#tableForm").submit();
		});
	} else {
		$("#tableForm").submit();
	}
</@override>

<@extends name="/zzgl/event/jinJiangShi/add_event_000.ftl" />