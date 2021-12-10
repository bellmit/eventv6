<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>关联信息反馈列表</title>
    <#include "/component/commonFiles-1.1.ftl" />

    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
</head>

<body class="easyui-layout">
<div class="MainContent">
    <div id="jqueryToolbar"></div>
</div>
<div id="feedbackRecordContentDiv" region="center" border="false" style="width:100%; overflow:hidden; position:relative;">
    <table style="width:100%" id="list"></table>
</div>

<script type="text/javascript">
    $(function() {
        loadDataList();
    });

    function loadDataList(){
        $('#list').datagrid({
            width:600,
            height:300,
            nowrap: true,
            rownumbers:true,
            remoteSort:false,
            striped: true,
            fit: true,
            fitColumns: true,
            singleSelect: true,
            idField:'fbUUId',
            url : '${rc.getContextPath()}/zhsq/reportFeedback/listFeedbackDataByBizSign.json',
            columns:[[
                {field:'fbUUId',title:'ID', align:'center',hidden:true},
                {field:'seContent',title:'下达内容', align:'left',width:fixWidth(0.2),sortable:true,formatter: clickFormatter},
                {field:'seTime',title:'下达时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
                {field:'fbTime',title:'反馈时间', align:'center',width:fixWidth(0.1),sortable:true, formatter: dateFormatter},
                {field:'fbContent',title:'反馈内容', align:'center',width:fixWidth(0.2),sortable:true,formatter: titleFormatter},
                {field:'gridPathName',title:'所属网格', align:'center',width:fixWidth(0.2),sortable:true,formatter: titleFormatter},
                {field:'fbStatusStr',title:'反馈状态', align:'center',width:fixWidth(0.12),sortable:true}
            ]],
            toolbar:'#jqueryToolbar',
            pagination:false,
            pageSize: 20,
            queryParams: {'bizSign': '${bizSign}','bizType':'${bizType}'},
            onDblClickRow:function(index,rec){
                detail(rec.fbUUId);
            },
            onLoadError: function () {//数据加载异常
                $('.datagrid-body').eq(1).append('<div class="ErrorImg FontRed"><b>数据读取错误！！！</b></div>');
            },
            onLoadSuccess: function(data){//事件标题内容左对齐
                if(data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
            }
        });
    }

    function detail(fbUUId){
        if(fbUUId) {
            var url = "${rc.getContextPath()}/zhsq/reportFeedback/toFeedbackDetail.jhtml?fbUUId=" + fbUUId;
            parent.parent.openJqueryWindowByParams({
                maxWidth: 900,
                maximizable: true,
                title: "查看反馈信息",
                targetUrl: url
            });
        } else {
            $.messager.alert('警告','请选择需要查看的记录!','warning');
        }
    }

    function clickFormatter(value, rec, rowIndex) {
        var title = "";

        if(value) {
            title = '<a class="eName" href="###" title="'+ value +'" onclick=detail("'+ rec.fbUUId+ '")>'+value+'</a>';
        }

        return title;
    }

    function titleFormatter(value, rec, rowIndex) {
        var title = "";

        if(value) {
            title = "<span title='"+ value +"'>"+ value +"</span>";
        }

        return title;
    }

    function dateFormatter(value, rowData, rowIndex) {
        if(value && value.length >= 10) {
            value = value.substring(0,10);
        }

        return value;
    }
</script>
</body>
</html>