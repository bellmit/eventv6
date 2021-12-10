<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
    <#include "/component/maxJqueryEasyUIWin.ftl" />
    <#include "/component/listSet.ftl" />
    <style>

    </style>
</head>
<body class="easyui-layout">
<div id="_DivCenter" region="center" >
    <table id="list"></table>
</div>
<div id="jqueryToolbar">
    <div class="ConSearch">
        <form id="searchForm" method="post">
            <input type="hidden" id="myControlLibraryId" name="myControlLibraryId" value="${id!}" />
            <input type="hidden" id="controlLibraryId" name="controlLibraryId" value="${bo.controlLibraryId!}" />
            <input type="hidden" id="libType" name="libType" value="${bo.libType!}" />
            <input type="hidden" id="controlTaskId" name="controlTaskId" value="${bo.controlTaskId!}" />
            <input type="hidden" id="deviceDis" name="deviceDis" value="${deviceDis!}" />
            <div class="fl">
                <ul>
                    <li>姓名：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" id="name" name="name" style="width: 156px" />
                    </li>
                    <li>身份证号：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" name="identityCardNumber" id="identityCardNumber" style="width: 156px" />
                    </li>
                    <li>手机号：</li>
                    <li>
                        <input class="inp1 InpDisable" type="text" name="mobile" id="mobile" style="width: 156px" />
                    </li>
                </ul>
            </div>
            <div class="btns">
                <ul>
                    <li><a href="javascript:;" class="chaxun" title="查询数据" onclick="searchData()">查询</a></li>
                    <li><a href="javascript:;" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
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
            <@actionCheck></@actionCheck>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function() {
        loadList(); //加载列表
    });

    //加载列表
    function loadList() {
        $('#list').datagrid({
            rownumbers: true, //行号
            fitColumns: true, //自适应宽度
            nowrap: true,
            striped: true,
            singleSelect: true,
            fit: true,
            url: '${rc.getContextPath()}/zhsq/event/controlPersonnel/listData.jhtml',
            queryParams: $('#searchForm').serializeJson(),
            columns: [[
                {field:'name', title:'姓名', align:'center', width:120,formatter : function(val, rec) {
                    var myControlLibraryId = $("#myControlLibraryId").val();
                        return '<div class="sub"><a style="cursor: pointer;" title="'+val+'" onclick="warning(\''+myControlLibraryId+'\',\''+rec.controlLibraryId+'\'' +
                                ',\''+rec.controlTaskId+'\',\''+rec.controlObjectId+'\',\''+rec.identityCardNumber+'\',\''+rec.name+'\')">'+val+'</a></div>';
                    }},
                {field:'identityCardNumber', title:'身份证号', align:'center', width:100},
                {field:'mobile', title:'手机号', align:'center', width:120},
                {field:'gender', title:'性别', align:'center', width:50,formatter:function(value,rec){
                        if(value == 'M'){
                            value='男';
                        }else if (value == 'F') {
                            value='女';
                        }
                        return value;
                    }},
                {field:'birthday', title:'出生日期', align:'center', width:100,formatter:function (value, rec) {
                    if (value == null){
                        return '';
                    }
                    return new Date(value).format('yyyy-MM-dd');}},
                {field:'nationalityCN', title:'民族', align:'center', width:50},
                {field:'nationality', title:'民族', align:'center', width:50,hidden:true},
                {field:'description', title:'描述', align:'center', width:150}
            ]],
            pagination: true,
            pageSize: 20,
            toolbar: '#jqueryToolbar',
            onLoadSuccess: function(data) {
                listSuccess(data); //暂无数据提示
            },
            onLoadError: function() {
                listError();
            }
        });
    }

    function warning(myControlLibraryId,controlLibraryId,controlTaskId,controlObjectId,identityCardNumber,name) {
        var url = '${rc.getContextPath()}/zhsq/event/warning/index.jhtml?myControlLibraryId='+myControlLibraryId+"&controlLibraryId="+controlLibraryId
                +"&controlTaskId="+controlTaskId+"&controlObjectId="+controlObjectId+"&identityCardNumber="+identityCardNumber+"&name="+name;
        //showMaxJqueryWindow('历史警情', url);
        parent.showDetail('历史警情', url);
    }

    //新增
    function add() {
        var controlLibraryId = $("#controlLibraryId").val();
        var libType = $("#libType").val();
        var controlTaskId = $("#controlTaskId").val();
        var url = '${rc.getContextPath()}/zhsq/event/controlPersonnel/form.jhtml?controlLibraryId=' + controlLibraryId +
                "&libType=" +libType + "&controlTaskId=" + controlTaskId;
        showMaxJqueryWindow('新增', url, 800, 400);
    }

    //编辑
    function edit() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var controlLibraryId = $("#controlLibraryId").val();
            var url = '${rc.getContextPath()}/zhsq/event/controlPersonnel/form.jhtml?id=' + rows[0].id;
            showMaxJqueryWindow('编辑', url, 800, 400);
        }
    }

    //详情
    function detail() {
        var rows = $('#list').datagrid('getSelections');
        if (rows.length != 1) {
            $.messager.alert('提示', '请选择一条记录!', 'warning');
        } else {
            var url = "${rc.getContextPath()}/zhsq/event/controlPersonnel/detail.jhtml?id=" + rows[0].id;
            showMaxJqueryWindow('设置布控人员', url);
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
                        url: '${rc.getContextPath()}/zhsq/event/controlPersonnel/del.jhtml',
                        data: {
                            id: rows[0].id,
                            controlObjectId: rows[0].controlObjectId
                        },
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

    //查询
    function searchData() {
        $('#list').datagrid('reload', $('#searchForm').serializeJson());
    }

    function exportData() {
        $("#searchForm").submit();
    }

    //重置
    function resetCondition() {
        //$('#searchForm').form('clear');
        $("#name").val('');
        $("#identityCardNumber").val('');
        $("#mobile").val('');
        searchData();
    }
    //关闭
    function cancel() {
        parent.closeMaxJqueryWindow();
    }
</script>
</body>
</html>
