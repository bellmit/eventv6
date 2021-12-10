<@override name="eventListPageTitle">
	江苏省盐城响水县事件列表页面
</@override>
<@override name="extendCondition">
<#if eventType?? && eventType != 'draft'>
    <tr>
        <td>
            <label class="LabName width65px"><span>督办类型：</span></label>
            <input type="text" id="supervisionType" name="supervisionType" class="hide queryParam"/>
            <input type="text" id="supervisionTypeName" class="inp1 selectWidth" />
        </td>
    </tr>
</#if>
</@override>
<@override name="extendConditionInit">
<#if eventType?? && eventType != 'draft'>
        AnoleApi.initTreeComboBox("supervisionTypeName", "supervisionType", "A001093091", null, null, {
            RenderType : "01",
            ShowOptions:{
                EnableToolbar : true
            }
        });
</#if>
</@override>


<@extends name="/zzgl/event/list_event.ftl" />