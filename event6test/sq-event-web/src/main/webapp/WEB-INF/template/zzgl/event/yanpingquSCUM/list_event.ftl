<@override name="eventListPageTitle">延平区智慧城管事件列表页面</@override>

<@override name="eventToolbar">
	<#include "/zzgl/event/yanpingquSCUM/eventDataGridToolbar.ftl" />
</@override>

<@override name="generalColumns">
columns:[[
    {field:'eventId',title:'ID', align:'center',checkbox:true,hidden:true,outMenu:true},
   	{field:'type',title:'type', align:'center',hidden:true,outMenu:true},

	<@block name="checkbox"></@block>

    {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.1),sortName:'T1.EVENT_NAME',sortable:true,formatter: clickFormatter,outMenu:true},
    <#if eventType != "draft">
    {field:'code',title:'事件编号', align:'center',width:fixWidth(0.19), formatter: titleFormatter<#if eventType != "todo">, hidden:true</#if>},
    {field:'occurred',title:'事发详址', align:'center',width:fixWidth(0.15), formatter: titleFormatter<#if eventType != "todo">, hidden:true</#if>},
    </#if>
    {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.14),sortName:'T1.HAPPEN_TIME',sortable:true, formatter: titleFormatter},
    <#if eventType != 'draft'>
    	<@block name="handleDateStrField">
    		{field:'handleDateStr',title:'办结期限', align:'center',width:fixWidth(0.14),sortName:'T1.HANDLE_DATE',sortable:true, formatter: titleFormatter},
    	</@block>
    </#if>
    <@block name="eventClassField">
    {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.15),sortName:'T1.TYPE_',sortable:true, formatter: titleFormatter},
    </@block>
    <@block name="gridPathField">
    {field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.15), formatter: titleFormatter},
    </@block>

<@block name="extraListColumn"></@block>

<#if eventType=="not">
    {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.08),sortable:true},
    {field:'taskStatusName',title:'任务状态', align:'center',width:fixWidth(0.1),hidden:true,
        styler: function(value,rec,index){
            return "color:#b3d465";
        }
    }
<#else>
    {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.08), formatter: titleFormatter}
</#if>
<#if eventType=="todo">
    ,{field:'taskReceivedStaus',title:'是否接收', align:'center',width:fixWidth(0.08),
    formatter:function(value,rec,rowIndex) {
        var valName = "未接收";
        if(value && value == "1") {
            valName = "已接收";
        }

        return valName;
    }
}
</#if>
<#if eventType=="history">
    ,{field:'evaLevelName',title:'评价情况', align:'center',width:fixWidth(0.1)}
</#if>
    <@block name="createTimeStrField">
    ,{field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortName:'T1.CREATE_TIME',sortable:true, formatter: dateFormatter, hidden:true},
    </@block>
    {field:'influenceDegreeName',title:'影响范围', align:'center',width:fixWidth(0.1),sortName:'T1.INFLUENCE_DEGREE',sortable:true, hidden:true},
    {field:'sourceName',title:'信息来源', align:'center',width:fixWidth(0.1),sortName:'T1.SOURCE',sortable:true, hidden:true},
    {field:'urgencyDegreeName',title:'紧急程度', align:'center',width:fixWidth(0.1),sortName:'T1.URGENCY_DEGREE',sortable:true, hidden:true},
    {field:'involvedPersion',title:'涉及人员', align:'center',width:fixWidth(0.1), hidden:true},
    {field:'contactUser',title:'联系人员', align:'center',width:fixWidth(0.1),sortName:'T1.CONTACT_USER',sortable:true, hidden:true},
    {field:'tel',title:'联系电话', align:'center',width:fixWidth(0.1),sortName:'T1.TEL',sortable:true, hidden:true}
]],
</@override>

<@extends name="/zzgl/event/list_event.ftl" />