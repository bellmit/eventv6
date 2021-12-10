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

                <li>所属区域</li>
                <li> <input type="hidden" id="orgCode" name="orgCode"  value="${orgCode}"  >
                    <input class="inp1 inp2 InpDisable easyui-validatebox" style="width:150px;" type="text" id="orgName" value="${orgName}"
                           name="orgName"/></li>


                <li>所属年份</li>
                <li><input type="text" class="inp1 Wdate timeClass" id="syear" name="syear"  style="width:150px;"
                           onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy'})" readonly="true"/></li>



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
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/listData.json',
            columns: [[
                {field: 'seqid', checkbox: true, width: 40, hidden: 'true'},
                {
                    field: 'orgPath',
                    title: '所属区域',
                    align: 'left',
                    width: 220,
                    formatter: function (val, rec) {
                        return "<a href='javascript:detail(" + rec.seqid + ")'>" + val + "</a>";
                    }
                },
                {
                    field: 'syear',
                    title: '所属年份',
                    align: 'center',
                    width: 60,
                },
                {
                    field: 'builtupArea',
                    title: '建成区面积(平方公里)',
                    align: 'center',
                    width: 130,
                },
                {
                    field: 'popu',
                    title: '建成区人口(万人)',
                    align: 'center',
                    width: $(this).width() * 0.12,

                },
                {
                    field: 'regionalArea',
                    title: '区域面积(平方公里)',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'gCoverarea',
                    title: '绿化覆盖面积(公顷)',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'gArea',
                    title: '城市绿地面积(公顷)',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'gParkarea',
                    title: '公园绿地面积(公顷)',
                    align: 'center',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'forestArea',
                    title: '森林面积(公顷)',
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
            ShowOptions: { EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });




        $("#content-d").mCustomScrollbar({theme:"minimal-dark"});

    });

    function add() {
        var url = '${rc.getContextPath()}/zhsq/szzg/greenindicators/add.jhtml';
        showMaxJqueryWindow("新增绿化指标信息", url, 630, fixHeight(0.9));
    }

    function edit() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = '${rc.getContextPath()}/zhsq/szzg/greenindicators/edit.jhtml?seqid=' + rows[0].seqid;
            showMaxJqueryWindow("编辑绿化指标信息", url, 630, fixHeight(0.9));
        }
    }

    function detail(seqid) {


        if (seqid != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/greenindicators/detail.jhtml?seqid='+seqid;
            showMaxJqueryWindow("绿化指标信息", url,600, fixHeight(0.9));
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
                        url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/delete.json',
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

        if ($("#syear").val() != null & $("#syear").val() != "") {
            a["syear"] = $.trim($("#syear").val());
        }
        if ($("#orgCode").val() != null & $("#orgCode").val() != "") {
            a["orgCode"] = $.trim($("#orgCode").val());
        }



        doSearch(a);
    }

    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    function resetCondition() {
        $('#orgCode').val("");
        $('#orgName').val("");
        $('#syear').val("");
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