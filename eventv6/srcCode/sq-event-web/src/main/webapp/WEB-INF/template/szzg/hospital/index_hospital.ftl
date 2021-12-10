<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>医院信息编辑</title>
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
                <li>所属区域：</li>
                <li><input id="orgCode" type="hidden" value="${orgCode}"/>
                    <input id="orgName" type="text" class="inp1 InpDisable" style="width:100px;" value="${orgName}"/>
                </li>

                <li>医院名称</li>
                <li><input name="hospitalName" id="hospitalName" maxLength="30" type="text"
                           class="inp1 easyui-validatebox"
                           style="width: 120px;"
                           data-options="tipPosition:'bottom'"/></li>

                <li>医院地址</li>
                <li><input name="address" id="address" maxLength="30" type="text" class="inp1 easyui-validatebox"
                           style="width: 120px;"
                           data-options="tipPosition:'bottom'"/></li>


                <li>医院类型：</li>
                <li>
                    <input id="type" name="type" type="hidden"/>
                    <input id="typeName" name="typeName" type="text" class="inp1 InpDisable" style="width:120px;"
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


<div id="hospitalDiv" region="center" border="false" style="width:100%; overflow:hidden;">
    <table id="list"></table>
</div>
</body>
<script type="text/javascript">


    $(function () {
        //加载区域
        AnoleApi.initGridZtreeComboBox("orgName", null, function (gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value = items[0].orgCode;
            }
        }, {
            rootName: "行政区划",
            ChooseType: '1',
            isShowPoorIcon: "0",
            ShowOptions: {EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });


        //加载数据字典：医院类型
        AnoleApi.initTreeComboBox("typeName", null, 'S008001', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('type').value = items[0].dictCode;
            }
        }, null, {ChooseType: '1', ShowOptions: {EnableToolbar: true}});

    });

    var orgCode =${orgCode}

            $(function () {

                if (orgCode == "") {
                    $('#hospitalDiv').html("<table id='list'><tr><td style='color:red;'>没有网格，无法载入数据</td></tr></table>");
                } else {
                    loadDataList();
                }
            })

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
            idField: 'seqId',
            url: '${rc.getContextPath()}/zhsq/szzg/hospital/listData.json',
            columns: [[
                {field: 'seqId', checkbox: true, width: 40, hidden: 'true'},
                {
                    field: 'orgPath',
                    title: '所属区域',
                    align: 'left',
                    width: 220,
                },
                {
                    field: 'hospitalName',
                    title: '医院名称',
                    align: 'center',
                    width: 180,
                    formatter: function (val, rec) {
                        var content = '<a href="javascript:detail('+ rec.seqId + ')"><li title="' + val + '" class="tip">' + val + '</li></a>';
                        return content;
                    }
                },
                {
                    field: 'typeName',
                    title: '医院类型',
                    align: 'center',
                    width: $(this).width() * 0.12,

                },
                {
                    field: 'address',
                    title: '医院地址',
                    align: 'center',
                    width: $(this).width() * 0.16,
                    formatter: function (val, rec) {
                        var content =  '<li title="' + val + '" class="tip">' + val + '</li>';
                        return content;
                    }
                },
                {
                    field: 'tel',
                    title: '联系电话',
                    align: 'center',
                    width: $(this).width() * 0.10,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            queryParams: {orgCode:${orgCode}},
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
        var url = '${rc.getContextPath()}/zhsq/szzg/hospital/add.jhtml';
        showMaxJqueryWindow("新增医院信息", url, 700, fixHeight(0.9));
    }

    function detail(seqId) {

        if (seqId != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/hospital/detail.jhtml?seqId=' + seqId+'&show=1';
            showMaxJqueryWindow("医院信息", url, 700, fixHeight(0.9));
        }
    }

    function edit() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = '${rc.getContextPath()}/zhsq/szzg/hospital/edit.jhtml?seqId=' + rows[0].seqId;
            showMaxJqueryWindow("编辑医院信息", url, 700, fixHeight(0.9));
        }
    }



    //删除
    function del() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            $.messager.confirm('提示', '您确定删除选中的信息吗?', function(r) {
                if (r) {
                    modleopen(); //打开遮罩层
                    $.ajax({
                        type: 'POST',
                        url: '${rc.getContextPath()}/zhsq/szzg/hospital/delete.json',
                        data: 'seqid=' + rows[0].seqId,
                        dataType: 'json',
                        success: function(data) {
                            if (data.result == 'fail') {
                                $.messager.alert('错误', '删除失败！', 'error');
                            } else {
                                $.messager.alert('提示', '删除成功！', 'info');
                                searchData();
                            }
                        },
                        error: function(data) {
                            $.messager.alert('错误', '连接超时！', 'error');
                        },
                        complete : function() {
                            modleclose(); //关闭遮罩层
                        }
                    });
                }
            });
        }
    }


    function searchData() {
        var a = new Array();

        if ($("#hospitalName").val() != null & $("#hospitalName").val() != "") {
            a["hospitalName"] = $.trim($("#hospitalName").val());
        }

        if ($("#address").val() != null & $("#address").val() != "") {
            a["address"] = $.trim($("#address").val());
        }
        if ($("#orgCode").val() != null & $("#orgCode").val() != "") {
            a["orgCode"] = $.trim($("#orgCode").val());
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
        $('#hospitalName').val("");
        $('#address').val("");
        $('#type').val("");
        $('#typeName').val("");
        $('#orgCode').val("");
        $('#orgName').val("");
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