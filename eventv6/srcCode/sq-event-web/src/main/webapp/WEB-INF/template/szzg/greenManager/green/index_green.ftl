<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>城市绿化分布</title>
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
                <li><input id="orgCode" type="hidden"  value="${orgCode}"/>
                    <input  id="orgName" type="text" class="inp1 InpDisable" style="width:100px;" value="${orgName}"/>
                </li>


                <li>绿化名称</li>
                <li>   <input name="gName" id="gName" maxLength="30" type="text" class="inp1 easyui-validatebox"
                              style="width: 140px;"
                              data-options="tipPosition:'bottom'"/></li>

                <li>地址</li>
                <li>   <input name="location" id="location" maxLength="30" type="text" class="inp1 easyui-validatebox"
                              style="width: 140px;"
                              data-options="tipPosition:'bottom'"/></li>

                <li>绿化类型：</li>
                <li>
                    <input id="gType" name="gType" type="hidden"/>
                    <input id="gTypeName" name="gTypeName" type="text" class="inp1 InpDisable" style="width:150px;"
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
        AnoleApi.initGridZtreeComboBox("orgName", null, function (gridId, items) {
            if (items && items.length > 0) {
                document.getElementById('orgCode').value = items[0].orgCode;
            }
        }, {
            rootName: "行政区划",
            ChooseType: '1',
            isShowPoorIcon: "0",
            ShowOptions: { EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });

        //加载数据字典：绿地类型
        AnoleApi.initTreeComboBox("gTypeName", null, 'S002002', function (gridId, items) {
            if (isNotBlankParam(items) && items.length > 0) {
                document.getElementById('gType').value = items[0].dictCode;
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
            url: '${rc.getContextPath()}/zhsq/szzg/green/listData.json',
            columns: [[
                {field: 'seqid', checkbox: true, width: 40, hidden: 'true'},
                {
                    field: 'orgPath',
                    title: '所属区域',
                    align: 'left',
                    width: 220,
                },
                {
                    field: 'gName',
                    title: '绿地名称',
                    align: 'center',
                    width: 100,
                    formatter: function (val, rec) {
                        return "<a href='javascript:detail(" + rec.seqid + ")'>" + val + "</a>";
                    }
                },
                {
                    field: 'location',
                    title: '地址',
                    align: 'center',
                    width: $(this).width() * 0.12,

                },
                {
                    field: 'gTypeName',
                    title: '绿化类型',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'gCoverarea',
                    title: '占地面积(公顷)',
                    align: 'center',
                    width: $(this).width() * 0.12,
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
        var url = '${rc.getContextPath()}/zhsq/szzg/green/add.jhtml';
        showMaxJqueryWindow("新增城市绿化分布数据", url,700, fixHeight(0.9));
    }

    function edit() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = '${rc.getContextPath()}/zhsq/szzg/green/edit.jhtml?seqid=' + rows[0].seqid;
            showMaxJqueryWindow("编辑城市绿化分布数据", url, 700, fixHeight(0.9));
        }
    }

    function detail(seqid) {


        if (seqid != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/green/detail.jhtml?seqid='+seqid;
            showMaxJqueryWindow("绿化分布信息", url,700, fixHeight(0.9));
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
                        url: '${rc.getContextPath()}/zhsq/szzg/green/delete.json',
                        data: 'seqid=' + rows[0].seqid,
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

        if ($("#gName").val() != null & $("#gName").val() != "") {
            a["gName"] = $.trim($("#gName").val());
        }

        if ($("#orgCode").val() != null & $("#orgCode").val() != "") {
            a["orgCode"] = $.trim($("#orgCode").val());
        }

        if ($("#location").val() != null & $("#location").val() != "") {
            a["location"] = $.trim($("#location").val());
        }

        if ($("#gType").val() != null & $("#gType").val() != "") {
            a["gType"] = $.trim($("#gType").val());
        }
        doSearch(a);
    }

    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    function resetCondition() {
        $('#gName').val("");
        $('#orgCode').val("");
        $('#orgName').val("");
        $('#location').val("");
        $('#gType').val("");
        $('#gTypeName').val("");
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