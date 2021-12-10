<@override name="extendCondition">
    <#if eventType?? >
        <tr>
            <td>
                <label class="LabName width65px"><span>事件类别：</span></label>
                <input type="text" id="eventLabel" name="eventLabel" class="hide queryParam"/>
                <input type="text" id="eventLabelName" class="inp1 selectWidth" />
            </td>
        </tr>
    </#if>
</@override>

<@override name="extendConditionInit">
    <#if eventType??>
        AnoleApi.initListComboBox("eventLabelName", "eventLabel", "", null, null, {
            RenderType : "01",
            DataSrc: [{"name":"重特大案(事)件", "value":"majorRelatedEvents"},
                      {"name":"命案防控", "value":"homicideCase"},
                      {"name":"矛盾纠纷排查化解", "value":"disputeMediation"},
                      {"name":"涉及师生(事)件", "value":"schoolRelatedEvents"},
                      {"name":"涉及线路案(事)件", "value":"careRoads"}
                     ],
            ShowOptions:{
            EnableToolbar : true
            }
        });
    </#if>
</@override>

<@override name="addEventPageInfo">
    opt.title = "新增事件信息";
    opt.targetUrl = url;
    openJqueryWindowByParams(opt);
</@override>
<@override name="editEventPageInfo">
    opt.title = "编辑事件信息";
    opt.targetUrl = url;
    openJqueryWindowByParams(opt);
</@override>

<@extends name="/zzgl/event/eventDataGridToolbar.ftl" />
