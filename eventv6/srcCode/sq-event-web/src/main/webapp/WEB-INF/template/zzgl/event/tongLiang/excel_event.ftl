<@override name="createTimeSearch">
    <li class="eventCreateTimeLi">采集时间：</li>
    <li class="eventCreateTimeLi">
        <input class="inp1 hide queryParam" type="text" id="createTimeStart" name="createTimeStart" value="${createTimeStart!}"></input>
        <input class="inp1 hide queryParam" type="text" id="createDateTimeEnd" name="createDateTimeEnd" value="${createDateTimeEnd!}"></input>
        <input type="text" id="_createTimeDateRender" class="inp1 InpDisable" style="width:310px;" value="${createTimeStart}<#if createTimeStart??> 00:00:00</#if><#if createTimeStart?? && createDateTimeEnd??> ~ </#if>${createDateTimeEnd!}"/>
    </li>
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
                    $("# " + endTimeId).val(_end);
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
<#if eventType=="all">
    <@override name="generalColumns">
        columns:[[
            {field:'eventId',title:'ID', align:'center',hidden:true},//id
            {
                field: 'image', title: '<input id=\"imageCheckbox\" type=\"checkbox\"  ><span title=\"导出图片\">导出图片</span>', align:'center', width: 90,
                formatter: function (value, rec, rowIndex) {
                    return "<input type=\"checkbox\" id=\"image_"+rec.eventId+"\"  name=\"image\"   value=\""+rec.eventId+"\" >";
                }
            },
            {field:'type',title:'type', align:'center',hidden:true},//
            {field:'eventName',title:'事件标题', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
            {field:'eventClass',title:'事件分类', align:'center',width:fixWidth(0.2),sortable:true},
            {field:'gridPath',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true,
                formatter:function(value, rec, index){
                    if(value == null)return "";
                        return '<div title="'+rec.gridPath+'" >'+value+'</div>'
                    }
            },
            <#if isJianyin=='1'>
                {field:'bizPlatform',title:'入格事项', align:'center',width:fixWidth(0.08),sortable:true, formatter: enterEventFormatter},
            </#if>
            {field:'happenTimeStr',title:'事发时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
            {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: titleFormatter},
            <#if eventType=="not">
                {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true},
                {field:'taskStatusName',title:'任务状态', align:'center',width:fixWidth(0.1),sortable:true,hidden:true,
                    styler: function(value,rec,index){
                            return "color:#b3d465";
                    }
                }
            <#else>
                {field:'statusName',title:'当前状态', align:'center',width:fixWidth(0.12),sortable:true}
            </#if>
            ,{field:'finTimeStr',title:'办结时间', align:'center',width:fixWidth(0.15),sortName:'T1.FIN_TIME',sortable:true, formatter: titleFormatter},
            {field:'contactUser',title:'联系人员', align:'center',width:fixWidth(0.1),sortName:'T1.CONTACT_USER',sortable:true, formatter: titleFormatter},
            {field:'tel',title:'联系电话', align:'center',width:fixWidth(0.1),sortName:'T1.TEL',sortable:true, formatter: titleFormatter}
            <#--<#if isJianyin != '1'>
                ,{field:'curOrgName',title:'承办单位', align:'center',width:fixWidth(0.1),sortable:true},
            </#if>-->
            <#if eventType=="todo">
                ,{field:'taskReceivedStaus',title:'是否接收', align:'center',width:fixWidth(0.12),sortable:true,
                    formatter:function(value,rec,rowIndex) {
                        var valName = "未接收";
                        if(value && value == "1") {
                            valName = "已接收";
                        }

                        return valName;
                    }
                }
            </#if>
        ]],
    </@override>
<#elseif eventType=="todo" || eventType="done" || eventType="history">
    <@override name="handleDateStrField">
        {field:'createTimeStr',title:'采集时间', align:'center',width:fixWidth(0.15),sortName:'T1.CREATE_TIME',sortable:true, formatter: titleFormatter},
    </@override>
    <@override name="createTimeStrField">,</@override>
</#if>
<@override name="extarFunction">
    function titleFormatter(value, rowData, rowIndex) {
        var title = "";

        if(value) {
            title = '<span title="'+ value +'" >'+ value +'</span>';
        }

        return title;
    }
</@override>
<@override name="eventToolbar">
    <#include "/zzgl/event/eventDataGridToolbar.ftl" />
</@override>

<@extends name="/zzgl/event/excel_event.ftl" />