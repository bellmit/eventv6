<!DOCTYPE html>
<html>
<head>
    <title>守重企业信息列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/listSet.ftl" />
    <style type="text/css">
        .inp1 {
            width: 100px;
        }
    </style>
</head>
<body class="easyui-layout">
<div id="_DivCenter" region="center">
    <table id="list"></table>
</div>
<div id="jqueryToolbar">
    <div class="ConSearch">
        <form id="searchForm">
            <div class="fl">
                <ul>
                    <li>所属网格：</li>
                    <li>
                        <input type="hidden" id="gridCode" name="gridCode" value="${gridCode!''}" />
                        <input type="text" id="gridName" value="${gridName!''}" class="inp1 InpDisable" style="width: 150px;" />
                    </li>
                    <li>企业名称：</li>
                    <li>
                        <input class="inp1" type="text" id="enterpriseName" name="enterpriseName" />
                    </li>
                </ul>
            </div>
            <div class="btns">
                <ul>
                    <li>
                        <a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a>
                    </li>
                    <li>
                        <a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a>
                    </li>
                </ul>
            </div>
        </form>
    </div>
    <div class="h_10" id="TenLineHeight1"></div>
    <div class="ToolBar">
        <div class="tool fr">
            <a href="javascript:void(0)" class="NorToolBtn DelBtn" onclick="del();">删除</a>
            <a href="javascript:void(0)" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
            <a href="javascript:void(0)" class="NorToolBtn AddBtn" onclick="add();">新增</a>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function() {

        //加载列表
        loadList();

        //加载网格
        AnoleApi.initGridZtreeComboBox("gridName", null,
                function(gridId, items) {
                    if (items && items.length > 0) {
                        $("#gridCode").val(items[0].orgCode);
                    }
                });
    });

    //加载列表
    function loadList() {
        $('#list').datagrid({
            rownumbers : true, //行号
            fitColumns : true, //自适应宽度
            nowrap : true,
            striped : true,
            singleSelect : true,
            fit : true,
            url : '${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/listData.json',
            columns : [ [
                {	field : 'gridName',
                    title : '所属网格',
                    align : 'center',
                    width : 100
                },
                {
                    field : 'enterpriseName',
                    title : '企业名称',
                    align : 'center',
                    width : 100,
                    formatter : function(value, rec, index) {
                        if (value == null) {
                            value = "";
                        }
                        var f = '<a href="###" onclick="detail('+ rec.enterpriseId+ ')"><span style="color:lightred; text-decoration:underline;">'+ value+ '</span></a>&nbsp;';
                        return f;
                    }
                },
                {	field : 'registrationId',
                    title : '工商注册号',
                    align : 'center',
                    width : 100
                },
                {	field : 'evaluationTime',
                    title : '评定时间',
                    align : 'center',
                    width : 100,
                    formatter:function(value,rec,rowIndex){
                        return formatDatebox(value);
                    }
                },
                {	field : 'evaluationLevel',
                    title : '评定级别 ',
                    align : 'center',
                    width : 100,
                } ] ],
            pagination : true,
            pageSize : 20,
            toolbar : '#jqueryToolbar',
            onLoadSuccess : function(data) {
                listSuccess(data); //暂无数据提示
            },
            onLoadError : function() {
                listError();
            }
        });
    }

    //新增
    function add() {
        var url = '${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/add.jhtml';
        showMaxJqueryWindow('新增守重企业信息', url, 600, 420);
        //showCustomJqueryWindow("新增守重企业信息",600, 350,url);
    }

    //编辑
    function edit() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var id = rows[0].enterpriseId;
            var url = '${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/edit.jhtml?id='+ id;
            showMaxJqueryWindow('编辑守重企业信息', url, 600, 420);
            //showCustomJqueryWindow("编辑商标信息",600, 350,url);
        }
    }

    //详情
    function detail(id) {

        if (!id) {
            $.messager.alert('提示', '请选择一条记录', 'warning');
        } else {
            var url = "${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/detail.jhtml?id="+ id;
            showMaxJqueryWindow('守重企业详情', url, 600, 420);
            //showCustomJqueryWindow("商标详情",600, 350,url);
        }
    }

    //删除
    function del() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            $.messager.confirm('提示','您确定删除选中的信息吗?',function(r) {
                if (r) {modleopen(); //打开遮罩层
                    $.ajax({
                        type : 'POST',
                        url : '${rc.getContextPath()}/zhsq/szzg/zgFaithfulEnterprise/del.json',
                        data : {enterpriseId : rows[0].enterpriseId},
                        dataType : 'json',
                        success : function(data) {
                            if (data.result == 'fail') {$.messager.alert('错误','删除失败！','error');
                            } else {
                                $.messager.alert('提示','删除成功！','info');
                                searchData();
                            }
                        },
                        error : function(data) {
                            $.messager.alert('错误',	'连接超时！', 'error');
                        },
                        complete : function() {
                            modleclose(); //关闭遮罩层
                        }
                    });
                }
            });
        }
    }

    function searchAll() {
        searchData();
    }

    //条件查询(模糊查询)
    function searchData() {
        var a = new Array();

        var gridCode = $("#gridCode").val();
        if (gridCode != null && gridCode != "") {
            a["gridCode"] = gridCode;
        }
        var enterpriseName = $("#enterpriseName").val();
        if (enterpriseName != null && enterpriseName != "") {
            a["enterpriseName"] = enterpriseName;
        }

        doSearch(a);
    }
    //条件查询
    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    //重置
    function resetCondition() {
        $('#searchForm').form('clear');
        searchData();
    }
    //Date插件
    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, // month
            "d+": this.getDate(), // day
            "h+": this.getHours(), // hour
            "m+": this.getMinutes(), // minute
            "s+": this.getSeconds(), // second
            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
            "S": this.getMilliseconds()
            // millisecond
        }
        if (/(y+)/.test(format))
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                    .substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(format))
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }
    function formatDatebox(value) {
        if (value == null || value == '') {
            return '';
        }
        var dt;
        if (value instanceof Date) {
            dt = value;
        } else {
            dt = new Date(value);
        }

        return dt.format("yyyy-MM-dd"); //扩展的Date的format方法(上述插件实现)
    }
</script>
</html>