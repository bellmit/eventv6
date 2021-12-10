<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>应急预案树</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/customEasyWin.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
    <style>
        .abs-right_name{ width: 200px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}
    </style>

</head>
<body onload="loadGrid(),loadDataList();" class="easyui-layout">

<div id="jqueryToolbar">
    <div class="ConSearch">
        <div class="fl">
            <ul>
                <li>栏目名称：</li>
                <li>
                    <input id="key" name="key" type="text" class="inp1 InpDisable" style="width:150px;"/>
                </li>

            </ul>

        </div>

        <div class="btns">
            <ul>
                <li><a href="#" id="queryButton" class="chaxun BlueBtn" title="查询按钮" onclick="searchData(this.value)">查询</a></li>
                <li><a href="#" id="resetButton" class="chongzhi GreenBtn" style="margin-right:0;" title="重置查询条件"
                       onclick="resetCondition(this.value)">重置</a></li>
            </ul>
        </div>
        <div class="clear"></div>
        ‍

    </div>
    <div class="h_10 clear"></div>
    <div class="ToolBar">
        <div class="blind"></div><!-- 文字提示 -->
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
            <@actionCheck></@actionCheck>

        </div>
    </div>


<#--    <div class="ToolBar" id="ToolBar">-->
<#--        <div class="blind"></div>-->
<#--        <script type="text/javascript">-->
<#--            function DivHide() {-->
<#--                $(".blind").slideUp();//窗帘效果展开-->
<#--            }-->

<#--            function DivShow(msg) {-->
<#--                $(".blind").html(msg);-->
<#--                $(".blind").slideDown();//窗帘效果展开-->
<#--                setTimeout("this.DivHide()", 3000);-->
<#--            }-->

<#--        </script>-->
<#--        <div class="tool fr">-->
<#--        <@ffcs.right rightCode="del" parentCode="${system_privilege_action?default('')}">-->
<#--            <a href="#" class="NorToolBtn DelBtn" onclick="del();">删除</a>-->
<#--        </@ffcs.right>-->
<#--        <@ffcs.right rightCode="edit" parentCode="${system_privilege_action?default('')}">-->
<#--            <a href="#" class="NorToolBtn EditBtn" onclick="edit();">编辑</a>-->
<#--        </@ffcs.right>-->
<#--        <@ffcs.right rightCode="add" parentCode="${system_privilege_action?default('')}">-->
<#--            <a href="#" class="NorToolBtn AddBtn" onclick="add();">新增</a>-->
<#--        </@ffcs.right>-->
<#--        </div>-->
<#--    </div>-->
</div>


<div data-options="region:'west',title:'应急预案',split:true" border="0" style="width:200px;">
    <ul id="planTree" class="easyui-tree" data-options="animate:'true', onClick:treeClick ">
    </ul>
</div>

<div id="emeryencyDiv" region="center" border="false" style="width:100%; overflow:hidden;">
    <table id="list"></table>
</div>
</body>
<script type="text/javascript">

    var treeId;
    if (treeId == null || treeId == "") {
        treeId =${treeId}
    }

    $("#ui_div").mCustomScrollbar({theme: "minimal-dark"});
    //$("#content").mCustomScrollbar({theme: "minimal-dark"});
    var tree;

    function callTel(tel) {
        parent.callCms(tel);
    }

    function loadGrid() {
        $.ajax({
            type: 'POST',
            dataType: "json",
            url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/fingdTree.json?t=' + Math.random(),
            success: function (data) {
                data.push({id: 0, parentId: '-1', name: '应急预案'});
                tree = $("#planTree");

                tree.tree({data: arry2TreeFormat(data),formatter:function(node){
                    return "<div class='abs-right_name' title='"+node.text+"'>"+node.text+"</div>";
                }});
                var node = tree.tree('find', 0);
                tree.tree('expand', node.target);

            }
        });
    }

    function arry2TreeFormat(sNodes) {
        var r = [], tmpMap = [], id = "id", pid = "parentId", children = "children";
        for (var i = 0, l = sNodes.length; i < l; i++) {
            sNodes[i].text = sNodes[i].name;
            tmpMap[sNodes[i][id]] = sNodes[i];
        }
        for (var i = 0, l = sNodes.length; i < l; i++) {
            if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
                if (!tmpMap[sNodes[i][pid]][children]) {
                    tmpMap[sNodes[i][pid]].state = "closed";
                    tmpMap[sNodes[i][pid]][children] = [];
                }
                tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
            } else {
                sNodes[i].state = "closed";
                r.push(sNodes[i]);
            }
        }
        return r;
    }

    function treeClick(node) {
        $("#queryButton").val(node.id);
        $("#resetButton").val(node.id);
        var l = tree.tree("getChildren", node.target).length
        if (l > 0) { //有子列表则点击展开
            tree.tree('expand', node.target);
            $('#tips').show();
            $('#button').hide();
        } else {
            $('#button').show();
            $('#tips').hide();
        }
        searchData(node.id);
        treeId = node.id;
    }


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
            idField: 'id',
            url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/treeList.json',
            columns: [[
                {
                    field: 'name',
                    title: '栏目名称',
                    align: 'left',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return "<span title='" + val + "'>" + val + "</span>";
                    }
                },
                {
                    field: 'priority',
                    title: '排序',
                    align: 'left',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        return val;
                    }
                },
                {
                    field: 'status',
                    title: '状态',
                    align: 'left',
                    width: $(this).width() * 0.12,
                    formatter: function (val, rec) {
                        if (val == '1') {
                            val = '启用'
                            return val;
                        } else {
                            val = '关闭'
                            return val;
                        }
                    }
                },


            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            queryParams: {parentId: 0},
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
        var url = '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/treeAdd.jhtml?id=' + treeId;
        showMaxJqueryWindow("新增树节点信息", url, 400, fixHeight(0.5));
    }


    function edit() {
        var rows = $('#list').datagrid('getSelected');
        if (rows != null) {
            var url = '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/treeEdit.jhtml?id=' + rows.id;
            showMaxJqueryWindow("编辑树节点信息", url, 400, fixHeight(0.5));
        } else {
            $.messager.alert('提示', '请选择一条数据再执行此操作！', 'info');
        }
    }


    function del() {
        var rows = $('#list').datagrid('getSelected');

        if (rows != null) {
            var flag = false;

            $.ajax({
                type: 'POST',
                dataType: "json",
                data: {contentId: rows.id},
                url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/findListByParams.json?t=' + Math.random(),
                success: function (data) {
                    if (data.length != 0) {
                        flag = true;
                    }
                    else {
                        flag = false;
                    }
                    if (flag) {
                        $.messager.confirm('提示', '该栏目包含预案内容，是否级联删除预案？', function (r) {
                            if (r) {
                                modleopen();
                                $.ajax({
                                    type: "POST",
                                    url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/deleteTree.json',
                                    data: 'id=' + rows.id + '&del=' + true,
                                    dataType: "json",
                                    success: function (data) {
                                        modleclose();
                                        reloadDataForSubPage('成功删除数据！');
                                        loadGrid();
                                    },
                                    error: function (data) {
                                        $.messager.alert('错误', '连接超时！', 'error');
                                    }
                                });
                            } else {

                                $.messager.confirm('提示', '您确定删除选中的记录吗？', function (r) {
                                    if (r) {
                                        modleopen();
                                        $.ajax({
                                            type: "POST",
                                            url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/deleteTree.json',
                                            data: 'id=' + rows.id,
                                            dataType: "json",
                                            success: function (data) {
                                                modleclose();
                                                reloadDataForSubPage('成功删除数据！');
                                            },
                                            error: function (data) {
                                                $.messager.alert('错误', '连接超时！', 'error');
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    } else {
                        $.messager.confirm('提示', '您确定删除选中的记录吗？', function (r) {
                            if (r) {
                                modleopen();
                                $.ajax({
                                    type: "POST",
                                    url: '${rc.getContextPath()}/zhsq/szzg/emeryencyplan/deleteTree.json',
                                    data: 'id=' + rows.id,
                                    dataType: "json",
                                    success: function (data) {
                                        modleclose();
                                        reloadDataForSubPage('成功删除数据！');
                                    },
                                    error: function (data) {
                                        $.messager.alert('错误', '连接超时！', 'error');
                                    }
                                });
                            }
                        });
                    }
                },
                error: function (data) {
                    $.messager.alert('错误', '连接超时！', 'error');
                }
            });
        } else {
            $.messager.alert('提示', '请选择一条数据再执行此操作！', 'info');
        }
    }


    //

    function searchData(parentId) {
        var a = new Array();


        if ($("#key").val() != null & $("#key").val() != "") {
            a["key"] = $.trim($("#key").val());
        }
        if (parentId != null && parentId != undefined) {
            a["parentId"] = parentId;
        }
        doSearch(a);
    }

    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    function resetCondition(parentId) {
        $('#key').val("");
        searchData(parentId);
    }

    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
        loadGrid();
    }

</script>
</html>