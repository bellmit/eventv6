<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>绿化指标统计</title>
<#include "/component/commonFiles-1.1.ftl" />

<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>

    <style>

    </style>
</head>
<body class="easyui-layout">

<div id="jqueryToolbar">
    <div class="ConSearch">


        <div class="fl">
            <ul>

                <li>年份</li>
                <li><input type="text" class="inp1 Wdate timeClass" id="syear" name="syear" style="width:150px;"
                           onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy'})" readonly="true"/></li>


                <li>指标名称</li>
                <li><input name="name" id="name" maxLength="30" type="text" class="inp1 easyui-validatebox"
                           style="width: 140px;"
                           data-options="tipPosition:'bottom'"/></li>

                <li>类型：</li>
                <li>
                    <input id="type" name="type" type="hidden"/>
                    <input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:150px;"
                    />
                </li>

            </ul>

        </div>

        <div class="btns">
            <ul>
                <li><a href="#" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(1)">查询</a></li>
                <li><a href="#" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件"
                       onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>
        ‍

    </div>
    <div class="h_10 clear"></div>
    <div class="ToolBar" id="ToolBar">
        <div class="blind"></div>
        <script type="text/javascript">
            function DivHide() {
                $(".blind").slideUp();//窗帘效果展开
            }

            function DivShow(msg) {
                $(".blind").html(msg);
                $(".blind").slideDown();//窗帘效果展开
                setTimeout("this.DivHide()", 3000);
            }


        </script>
        <div class="tool fr">
        <@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">
            <a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>
        </@ffcs.right>
        <@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">
            <a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>
        </@ffcs.right>
        <@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">
            <a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>
        </@ffcs.right>
        </div>
    </div>


</div>


<div id="greenStatDiv" region="center" border="false" style="width:100%; overflow:hidden;">
    <table id="list"></table>
</div>
</body>
<script type="text/javascript">


    $(function () {


        //加载数据字典：文化程度
        AnoleApi.initTreeComboBox("typeName", null, 'S002001', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('type').value = items[0].dictCode;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});

    });

    $(function () {


        loadDataList();

    });


    function loadDataList() {
        $('#list').datagrid({
            width: 600,
            height: 600,
            nowrap: true,
            rownumbers: true,
            striped: true,
            fit: true,
            fitColumns: true,
            singleSelect: true,
            idField: 'seqid',
            url: '${rc.getContextPath()}/zhsq/szzg/greenstd/listData.json',
            columns: [[
                {field: 'seqid', checkbox: true, width: 40, hidden: 'true'},
                {
                    field: 'syear',
                    title: '年份',
                    align: 'left',
                    width: 100,
                },
                {
                    field: 'name',
                    title: '指标名称',
                    align: 'center',
                    width: 100,
                    formatter: function (val, rec) {
                        return "<a href='javascript:detail(" + rec.seqid + ")'>" + val + "</a>";
                    }
                },

                {
                    field: 'typeName',
                    title: '类型',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'stdval',
                    title: '指标标准',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'actval',
                    title: '我市指标',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                }


            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            queryParams: {},
            onLoadSuccess: function (data) {
                if (data.total == 0) {
                    $('.datagrid-body').eq(1).append('<div class="nodata"></div>');
                }
            },
            onLoadError: function () {
                $('.datagrid-body-inner').eq(0).addClass("l_elist");
                $('.datagrid-body').eq(1).append('<div class="r_elist">数据加载出错</div>');
            }
        });

        //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 20,//每页显示的记录条数，默认为
            pageList: [20, 30, 40, 50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
        });
    }


    function add() {
        var url = '${rc.getContextPath()}/zhsq/szzg/greenstd/add.jhtml';
        showMaxJqueryWindow("新增国家森林达标数据", url, 700, fixHeight(0.8));
    }

    function edit() {
        var rows = $('#list').datagrid('getSelected');
        if (rows.seqid != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/greenstd/edit.jhtml?seqid=' + rows.seqid;
            showMaxJqueryWindow("编辑国家森林达标数据", url, 700, fixHeight(0.8));
        } else {
            $.messager.alert('提示', '请选择一条数据再执行此操作！', 'info');
        }
    }

    function detail(seqid) {


        if (seqid != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/greenstd/detail.jhtml?seqid=' + seqid;
            showMaxJqueryWindow("国家森林指标信息", url, 700, fixHeight(0.7));
        }
    }


    function del() {
        var rows = $('#list').datagrid('getSelected');
        if (rows.seqid != null) {
            $.messager.confirm('提示', '您确定删除选中的记录吗？', function (r) {
                if (r) {
                    modleopen();
                    $.ajax({
                        type: "POST",
                        url: '${rc.getContextPath()}/zhsq/szzg/greenstd/delete.json',
                        data: 'seqid=' + rows.seqid,
                        dataType: "json",
                        success: function (data) {
                            modleclose();
                            reloadDataForSubPage('成功删除 ' + data.result + ' 条数据！');
                        },
                        error: function (data) {
                            $.messager.alert('错误', '连接超时！', 'error');
                        }
                    });
                }
            });
        } else {
            $.messager.alert('提示', '请选择一条数据再执行此操作！', 'info');
        }
    }


    function searchData() {
        var a = new Array();

        if ($("#syear").val() != null & $("#syear").val() != "") {
            a["syear"] = $.trim($("#syear").val());
        }

        if ($("#name").val() != null & $("#name").val() != "") {
            a["name"] = $.trim($("#name").val());
        }

        if ($("#type").val() != null & $("#type").val() != "") {
            a["type"] = $.trim($("#type").val());
        }
        doSearch(a);
    }

    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    function resetCondition() {
        $('#syear').val("");
        $('#name').val("");
        $('#type').val("");
        $('#typeName').val("");
        searchData();
    }

    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
    }
</script>
</html>