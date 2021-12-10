<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>新组织-人员选择2</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />

    <script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/workflow/easyui-utils.js"></script>
    <script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/ztree/search_tree.js"></script>

    <style type="text/css">
        html, body{overflow:hidden;}
        /**表格列表总样式*/
        .select-table{
            font-size:12px;
            border-collapse:collapse;
        }
        .select-table .select-th {
            height:28px;
            line-height:28px;
            color:#000000;

            border-bottom:1px solid #39BBF8;
            border-right:1px solid #39BBF8;
            background-color: #F3F8FE;
        }
        .select-table .select-td {

            border-bottom:1px solid #39BBF8;
            border-right:1px solid #39BBF8;
            background-color: #F3F8FE;
        }

        .user-table-cell {
            margin: 0;
            padding: 0 4px;
            white-space: nowrap;
            word-wrap: normal;
            overflow: hidden;
            height: 18px;
            line-height: 18px;
            font-size: 12px;
        }
        /**用户搜索 查询按钮样式*/
        .userSearchBtn{display: inline-block; height: 24px; line-height: 24px; text-decoration: none; background: #2980B9; color:#fff; border-radius: 3px; margin: 0 0 0 5px; padding: 0 8px;}
        .userSearchBtn:hover{background: #3498DB; color: #fff; text-decoration: none;}
        .keyBlank{color:gray;}

    </style>
</head>

<body>
<!-- 组织、角色、职位人员选择面板 -->
<div id="userSelectorWin2" class="easyui-window">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center',border:false" style="overflow:hidden;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="select-table" style="height:255px;">
                <input type="hidden" name="preNode2" id="preNode2" />
                <input type="hidden" name="targetNode2" id="targetNode2" />
                <tr>
                    <td width="25%" style="background-color: #FFFFFF;" valign="top" class="select-td">
                        <div id="leftPanel" class="easyui-panel"  data-options="title:'组织机构'" style="overflow:hidden;">
                            <input type="text" id="orgInfoSearchCondition4User2" title="请输入组织名称后回车" placeholder="请输入组织名称后回车" class="inp1 hide" style="width: 92%; margin: 4px 0 4px 4px;" value="" onkeydown = "_ztreeOnkeydown4User(this);" />
                            <div id="leftTree" class="ztree" style="height:220px; overflow:auto;"></div>
                        </div>
                    </td>
                    <td width="35%" style="background-color: #FFFFFF;" valign="top" class="select-td">
                        <table id="userTable"></table>

                    </td>
                    <td width="10%" align="center" class="select-td">
                        <div style="width:60px; margin:0 auto;">
                            <a href="###" class="NorToolBtn AddBtn" onclick="addCheckUser();" style="margin:0 0 10px 0;">添加</a>
                            <a href="###" class="NorToolBtn DelBtn" onclick="delUser();" style="margin:0 0 10px 0;">删除</a>
                            <a href="###" class="NorToolBtn DelBtn" onclick="delAllUser();" style="margin:0 0 10px 0;">全删</a><br/>
                        </div>
                    </td>
                    <td width="30%" style="background-color: #FFFFFF;" valign="top" class="select-td">
                        <table id="selUserTable"></table>
                    </td>
                </tr>
            </table>
        </div>
        <div id="userSelectorSouthDiv" class="" data-options="region:'south',border:false" style="padding-bottom:10px;height:42px;">
            <div id="userSelectorBtnList" class="BtnList">
                <a href="###" class="BigNorToolBtn BigJieAnBtn" onclick="saveNextUser();">确定</a>
                <a href="###" class="BigNorToolBtn CancelBtn" onclick="cancelSelectUser();">取消</a>
            </div>
        </div>

    </div>
</div>
</body>

<script type="text/javascript">

    function alterDatagrid() {//调整表格行高、列宽
        $("#userSelectorWin2 .datagrid-cell, #userSelectorWin2 .datagrid-cell-group, #userSelectorWin2 .datagrid-header-rownumber, #userSelectorWin2 .datagrid-cell-rownumber").addClass("user-table-cell");

        $("#userSelectorWin2 .datagrid-header-row, #userSelectorWin2 .datagrid-row").height(25);

        $("#userSelectorWin2 .datagrid-cell-rownumber, #userSelectorWin2 .datagrid-header-rownumber").css({"padding": 0});

        //调整选人弹出框内部的标题颜色
        $("#userSelectorWin2 td div.panel-title").css('color', '#000');
        $("#userSelectorWin2 td div.panel-title").parent()
            .css('background', '#eeeeee')
            .css('filter', 'progid:DXImageTransform.Microsoft.gradient(startColorstr=#F8F8F8,endColorstr=#eeeeee,GradientType=0)');//IE下生效

    }

    /**
     * 人员选择器
     *
     * @param {Object}
     *            nodeId
     */
    var _nodeId = '';
    var _curnodeName = '';
    var _nodeName = '';
    /*-------------导致滚动条异常----------------*/
    var nodeType_ = 'task';

    var hiddenId = 'userIds',
        fileldId = 'userNames',
        curOrgIds = 'curOrgIds',
        curOrgNames = 'curOrgNames',
        targetIndex = "undefinded",// 选择列表中被选中行的索引
        $userSelectedLimit = null,//已选择的用户记录数限制
        $formId	= "<#if formId??>${formId?c}</#if>",//表单id
        $formType = "",//表单类型
        callback4Confirm = null,//确定回调函数
        callback4Cancel = null;//取消回调函数

    /********************************************************************
     * nextUserIdElId			下一环节办理人员id存放的元素id
     * nextUserNameElId			下一环节办理人员姓名存放的元素id
     * nextOrgIdElId				下一环节办理人员组织id存放的元素id
     * nextOrgNameElId			下一环节办理人员组织名称存放的元素id
     * curNodeName				当前节点名称
     * nextNodeName				下一节点名称
     * nextNodeId				下一节点id
     * nextNodeType				下一节点类型
     * formId					表单id
     * formType					表单类型
     * userSelectedLimit			已选择的用户记录数限制
     * isShowOrgNameFuzzyQuery	是否展示组织名称模糊查询条件，true为展示；默认为false
     * isShowUserPartyNameFuzzyQuery 是否展示用户姓名模糊查询条件，true为展示；默认为false
     * callback4Confirm			确定回调函数
     * callback4Cancel			取消回调函数
     ********************************************************************/
    function selectUserByObj2(obj) {
        if(obj == null) {
            obj = {};
        }

        var isShowOrgNameFuzzyQuery = obj.isShowOrgNameFuzzyQuery || false,
            _ztreeOnNodeCreated = null,
            orgRootId = obj.orgRootId || '<#if orgRootId??>${orgRootId?c}</#if>',
            _instanceId = obj.instanceId || '',
            _formTypeId = obj.formTypeId || '',
            isShowUserPartyNameFuzzyQuery = obj.isShowUserPartyNameFuzzyQuery || false;

        _nodeId = obj.nextNodeId || $("#nodeId").val();
        _curnodeName = obj.curNodeName || $("#curNodeName").val();
        _nodeName = obj.nextNodeName || $("#nodeName_").val();
        hiddenId = obj.nextUserIdElId || '';
        fileldId = obj.nextUserNameElId || '';
        nodeType_ = obj.nextNodeType || '';
        curOrgIds = obj.nextOrgIdElId || '';
        curOrgNames = obj.nextOrgNameElId || '';
        $userSelectedLimit = obj.userSelectedLimit || null;
        $formId = obj.formId || '';
        $formType = obj.formType || '';
        callback4Confirm = obj.callback4Confirm;
        callback4Cancel = obj.callback4Cancel;

        if (nodeType_ == 'fork' && !$("#" + nodeId).attr('checked')) {
            return false;
        }

        $("#targetNode2").val(_nodeName);
        $("#preNode2").val(_curnodeName);

        // 清空人员选择器
        $('#userTable').datagrid('loadData', { total : 0, rows : [] });
        $('#selUserTable').datagrid('loadData', { total : 0, rows : [] });
        // $("#promoter").attr("checked",false);
        $("#fast_user_div").hide();

        //清空查询条件
        $('#orgInfoSearchCondition4User2').val('');

        if(isShowOrgNameFuzzyQuery == true) {
            $('#leftTree').height(220 - 10 - $('#orgInfoSearchCondition4User2').outerHeight(true) - 2);
            $('#orgInfoSearchCondition4User2').show();
            _ztreeOnNodeCreated = _ztreeExpandAll4User;
        } else {
            $('#leftTree').height(220 - 10);//扣除orgInfoSearchCondition4User2的上下边距，共10px
        }

        if(isShowUserPartyNameFuzzyQuery == true) {
            $('#_userTableTitle').hide();
            $('#_searchUserName').show();
            $('#_searchUserName').width(700 * 0.35 * 0.85);
        } else {
            $('#_searchUserName').hide();
            $('#_userTableTitle').show();
        }

        // start:组织树渲染
        var nodeCode = 'U5R2D3';
        $('#leftPanel').panel({ title : '组织机构', fit : true, border : false });
        var setting = {
            view: {
                expandSpeed: "",	//不设置折叠、打开的动画效果
                showLine: false,	//不展示连线
                fontCss: setFontCss_ztree
            },
            async : {
                enable : true,
                url : "${rc.getContextPath()}/zhsq/keyelement/keyElementController/getTreeForEvent.jhtml",
                autoParam : [ 'id', 'gridLevel=level', 'professionCode' ],
                otherParam : {
                    nodeId : _nodeId,
                    nodeCode : nodeCode,
                    formId : $formId,
                    formType : $formType,
                    formTypeId : _formTypeId,
                    orgRootId : orgRootId,
                    instanceId : _instanceId,
                    curNodeName : _curnodeName,
                    nextNodeName : _nodeName
                },
                type : "get"
            },
            callback : {
                onAsyncSuccess : function(event, treeId, treeNode, msg) {
                    var leftHtml = $("#leftTree").html();

                    if(leftHtml.length == 0){
                        $.messager.alert('警告','没有可使用的组织机构，请先配置！','warning');
                    }

                },
                onClick : function(event, treeId, treeNode, clickFlag) {
                    var level = parseInt(treeNode.gridLevel);
                    if (isNaN(level) || treeNode.clickable) {
                        $("#_searchUserName").attr('id_queryattr', treeNode.id);
                        $("#_searchUserName").attr('nodecode_queryattr', nodeCode);
                        $("#_searchUserName").attr('nodeId_queryattr', _nodeId);
                        $("#_searchUserName").attr('formId_queryattr', $formId);
                        $("#_searchUserName").attr('formType_queryattr', $formType);

                        _searchOrgUserList(2);
                    }

                    event.stopPropagation();
                },
                onNodeCreated : _ztreeOnNodeCreated
            }
        };
        $.fn.zTree.init($("#leftTree"), setting);
        // end:组织树渲染

        $("#userSelectorWin2").window({
            title : '人员选择',
            width : 700,
            height : 350,
            left : ($(window).width() - 700) / 2,
            top : ($(window).height() - 350) / 2 + $(window).scrollTop(),
            modal : true,
            minimizable : false,
            maximizable : false,
            collapsible : false
        });
        setSelUser();
        alterDatagrid();
    }

    function selectUser(_hiddenId, _fileldId, _curOrgIds, _curOrgNames, _nodeType, nodeId,
                        curNodeName, nodeName, userSelectedLimit) {
        var obj = {
            nextUserIdElId 		: _hiddenId,
            nextUserNameElId 	: _fileldId,
            nextOrgIdElId 		: _curOrgIds,
            nextOrgNameElId 	: _curOrgNames,
            curNodeName 		: curNodeName,
            nextNodeName 		: nodeName,
            nextNodeId 			: nodeId,
            nextNodeType 		: _nodeType,
            userSelectedLimit 	: userSelectedLimit
        };

        selectUserByObj(obj);
    }

    function destoryTree() {
        leftTree = null;
        $("#userTable").datagrid('reload');
        $("#selUserTable").datagrid('reload');
    }

    function selectUserWin(result, curNodeName, nodeName) {
        // 组织、角色、职位人员选择面板
        var leftTree = null;
        var leftSetting = {
            async : {
                enable : true,
                autoParam : [ 'id' ],
                otherParam : {
                    "curnodeName" : curNodeName,
                    "nodeName" : nodeName
                },
                // otherParam:{"id":"-1"}
                type : "post",
                url : "${rc.getContextPath()}/zhsq/workflow/workflowController/getOrgTreeForEvent.jhtml"
            },
            callback : {
                onClick : function(event, treeId, treeNode, clickFlag) {
                    $("#userTable").egrid('reload', {
                        'type' : treeNode.type,
                        id : treeNode.id
                    });

                    event.stopPropagation();
                }
            }

        };
        if (nodeType_ == 'task' && (_nodeId != $("#nodeId").val())) {
            destoryTree();
        } else if (nodeType_ == 'fork' && (_nodeId != $("#forkNodeId").val())) {
            destoryTree();
        }

        leftTree = $.fn.zTree.init($("#leftTree"), leftSetting);
        for ( var i = 0; i < result.length; i++) {
            leftTree.addNodes(null, {
                id : result[i].ACTOR_ID,
                name : result[i].ACTOR_NAME,
                open : true,
                isParent : true,
                'type' : result[i].ACTOR_TYPE
            });
        }

        left = parseInt(($(window).width() - 700) / 2);

        $("#userSelectorWin2").window({
            title : '人员选择',
            width : 700,
            height : 350,
            left : left,
            top : ($(window).height() - 350) / 2 + $(window).scrollTop(),
            modal : true,
            minimizable : false,
            maximizable : false,
            collapsible : false
        });
    }

    function delUser() {
        var row = $('#selUserTable').datagrid('getSelected');
        // $('#selUserTable').datagrid('deleteRow', row.index);
        if (row) {
            $('#selUserTable').datagrid('deleteRow', targetIndex);
            if (row.userId == $("#promoter").attr("userId")) {
                $("#promoter").attr("checked", false);
            }
        } else {
            $.messager.alert('提示', '请选择一条记录!');
        }

        event.stopPropagation();
    }

    function delAllUser() {
        clearAll();
        $("#promoter").attr("checked", false);
        /*
         * var rows = $("#selUserTable").datagrid('getRows'); $.each(rows,
         * function(i, val){ $("#selUserTable").datagrid('deleteRow',i); });
         */

        event.stopPropagation();
    }

    function saveNextUser() {//点击确定回调方法
        var ids = '';
        var names = '';
        var orgIds = '';
        var orgNames = '';
        var rows = $("#selUserTable").datagrid('getRows');

        $.each(rows, function(i, val) {
            ids += val.userId + ',';
            names += val.partyName + ',';
            orgIds += val.orgId + ',';
            orgNames += val.orgName + ',';
        });

        names = names.substring(0, names.length - 1)

        $('#' + hiddenId).val(ids.substring(0, ids.length - 1));
        if($('#' + fileldId)[0].nodeName.toLowerCase() == 'input') {
            $('#' + fileldId).val(names).attr('title', names);
        } else {
            $('#' + fileldId).html(names);
        }
        $('#' + curOrgIds).val(orgIds.substring(0, orgIds.length - 1));
        $('#' + curOrgNames).val(orgNames.substring(0, orgNames.length - 1));

        if(callback4Confirm && typeof callback4Confirm === 'function') {
            callback4Confirm();
        }

        closeWin();

    }

    // 关闭窗口
    function closeWin() {
        $("#userSelectorWin2").window('close');
        clearAll();
        event.stopPropagation();
    }

    $(function() {
        $("#userSelectorWin2").window('close');
        // 用户列表
        $("#userTable")
            .datagrid(
                {
                    //title : '人员列表',
                    title : '<span id="_userTableTitle">人员列表</span><input type="text" id="_searchUserName" class="inp1 keyBlank hide" value="请输入用户姓名，回车查询" defaultValue="请输入用户姓名，回车查询" onfocus="_onfocus4userSearch(this);" onblur="_onblur4userSearch(this);" onkeydown="_searchOrgUserList(1);" />',
                    nowrap : false,
                    fit : true,// 自动大小
                    border : false,
                    method : 'post',
                    loadMsg : '数据加载中...',
                    singleSelect : true,
                    rownumbers : true,
                    url : "${rc.getContextPath()}/zhsq/keyelement/keyElementController/getUserListByOrgId.jhtml",
                    idField : 'userId',
                    onDblClickRow : function(rowIndex, rowData) {

                        var rows = $("#selUserTable").datagrid('getRows');

                        if (isExistRow(rows, rowData.userId, rowData.orgId)) {
                            appendRow($("#selUserTable"), {
                                userId : rowData.userId,
                                partyName : rowData.partyName,
                                orgId : rowData.orgId,
                                orgName : rowData.orgName
                            });

                            if (rowData.userId == $("#promoter").attr("userId")) {
                                $("#promoter").attr("checked", true);
                            }
                        }

                        event.stopPropagation();
                    },
                    onLoadSuccess: function(data) {
                        if(data.total > 0) {
                            alterDatagrid();
                        }
                    },
                    columns : [ [
                        { field : 'userId', hidden : true },
                        { field : 'orgId', hidden : true },
                        { field : 'orgName', hidden : true },
                        { title : '用户名称', field : 'partyName', width : '213', align : 'center' }
                    ] ],
                    queryParams : {
                        'type' : '1'
                    }
                });
        // 已选择用户列表
        $("#selUserTable").datagrid({
            title : '已选择人员',
            nowrap : false,
            fit : true,// 自动大小
            border : false,
            method : 'post',
            singleSelect : true,
            rownumbers : true,
            url : "",
            idField : 'userId',
            // pagination:false,
            columns : [ [
                { field : 'userId', hidden : true },
                { field : 'orgId', hidden : true },
                { title : '用户名称', field : 'partyName', width : '178', align : 'center',
                    formatter:function(value,rec,rowIndex) {
                        var orgName = rec.orgName,
                            content = value;

                        if(orgName) {
                            content = '<span title="'+ orgName +'">'+value+'</span>';
                        }

                        return content;
                    }
                }
            ] ],
            queryParams : {
                'type' : '1'
            },
            onClickRow : function(rowIndex, rowData) {
                targetIndex = rowIndex;

                event.stopPropagation();
            },
            onDblClickRow : function(rowIndex, rowData) {
                $("#selUserTable").datagrid('deleteRow', rowIndex);
                if (rowData.userId == $("#promoter").attr("userId")) {
                    $("#promoter").attr("checked", false);
                }

                event.stopPropagation();
            }
        });
        // 快速定位
        $("#promoter").click(function() {
            var rows = $("#selUserTable").datagrid('getRows');
            if ($(this).attr('checked')) {
                if (isExistRow(rows, $(this).attr('userId'), $(this).attr('orgId'))) {
                    $("#selUserTable").datagrid('appendRow', {
                        userId : $(this).attr('userId'),
                        orgId : $(this).attr('orgId'),
                        partyName : $(this).attr('userName')
                    });
                }
            } else {
                deleteRow("#selUserTable", rows, $(this).attr('userId'));
            }

            event.stopPropagation();
        });
    });

    function appendRow(tableObj, rows) {//添加行
        $(tableObj).datagrid("appendRow", rows);
        alterDatagrid();
    }

    function deleteRow(tableId, rows, uId) {//删除行
        $.each(rows, function(i, val) {
            if (val.userId == uId) {
                var index = $("#selUserTable").datagrid('getRowIndex', val);
                $(tableId).datagrid('deleteRow', index);

            }
        });
    }

    function isExistRow(rows, uId, uOrgId) {//判断用户是否已选择
        var tag = true;
        if($userSelectedLimit && rows.length >= $userSelectedLimit) {
            tag = false;
            $.messager.alert('警告', '已选择人员记录不得超过'+$userSelectedLimit+'条！', 'warning');
        } else {
            $.each(rows, function(i, val) {
                if (val.userId == uId && val.orgId == uOrgId) {
                    tag = false; return false;
                }
            });

            if(!tag) {
                $.messager.alert('提示', '该用户已添加过！', 'info');
            }
        }
        return tag;
    }

    /** ********************************用户人员选择面板********************************* */

    // 打开人员选择面板
    function openSignleUserWin(result) {
        if (_nodeId != $("#nodeId").val()) {
            $("#signleUserTree").datagrid('reload');
            $("#signleSelUserTree").datagrid('reload');
        }

        $('#signleUserTree').datagrid('loadData', {
            total : 0,
            rows : []
        });

        for ( var i = 0; i < result.length; i++) {
            $("#signleUserTree").datagrid('appendRow', {
                userId : result[i].ACTOR_ID,
                partyName : result[i].ACTOR_NAME
            });
        }

        left = parseInt(($(window).width() - 700) / 2);

        $("#signleUserWin").window({
            title : '人员选择',
            width : 650,
            height : 350,
            left : left,
            top : ($(window).height() - 350) / 2 + $(window).scrollTop(),
            modal : true,
            minimizable : false,
            maximizable : false,
            collapsible : false
        });
    }

    // 保存人员选择面板
    function signleSaveNextUser() {
        var ids = '';
        var names = '';

        var rows = $("#signleSelUserTree").datagrid('getRows');

        $.each(rows, function(i, val) {
            ids += val.userId + ',';
            names += val.partyName + ',';
        });

        $("#signleUserWin").window('close');
        $('#' + hiddenId).val(ids.substring(0, ids.length - 1));
        $('#' + fileldId).html(names.substring(0, names.length - 1));

    }

    // 关闭人员选择面板
    function signleCloseWin() {
        $("#signleUserWin").window('close');
    }

    $(function() {
        // 用户列表
        $("#signleUserTree").datagrid({
            title : '人员列表',
            nowrap : false,
            fit : true,// 自动大小
            method : 'post',
            singleSelect : true,
            rownumbers : true,
            idField : 'userId',
            onDblClickRow : function(rowIndex, rowData) {

                var rows = $("#signleSelUserTree").datagrid('getRows');

                if (isExistRow(rows, rowData.userId, rowData.orgId)) {
                    $("#signleSelUserTree").datagrid('appendRow', {
                        userId : rowData.userId,
                        partyName : rowData.partyName
                    });
                }

                event.stopPropagation();
            },
            columns : [ [ {
                field : 'userId',
                hidden : true
            }, {
                title : '用户名称',
                field : 'partyName',
                width : '200',
                align : 'center'
            }

            ] ]
        });

        // 已选择用户列表
        $("#signleSelUserTree").datagrid({
            title : '已选择人员',
            nowrap : false,
            fit : true,// 自动大小
            method : 'post',
            singleSelect : true,
            rownumbers : true,
            url : "",
            idField : 'userId',
            columns : [ [ {
                field : 'userId',
                hidden : true
            }, {
                title : '用户名称',
                field : 'partyName',
                width : '200',
                align : 'center'
            } ] ],
            onDblClickRow : function(rowIndex, rowData) {
                $("#signleSelUserTree").datagrid('deleteRow', rowIndex);
                event.stopPropagation();
            }
        });

        $("#signlePromoter").click(function() {
            var rows = $("#signleSelUserTree").datagrid('getRows');
            if ($(this).attr('checked')) {
                if (isExistRow(rows, $(this).attr('userId'), $(this).attr('orgId'))) {
                    $("#signleSelUserTree").datagrid('appendRow', {
                        userId : $(this).attr('userId'),
                        partyName : $(this).attr('userName')
                    });
                }
            } else {
                deleteRow("#signleSelUserTree", rows, $(this).attr('userId'));
            }

            event.stopPropagation();
        });

    });

    /**
     * 清空选中表
     */
    function clearAll() {
        $('#userTable').datagrid('loadData', {
            total : 0,
            rows : []
        });
        $('#selUserTable').datagrid('loadData', {
            total : 0,
            rows : []
        });

        var treeObj = $.fn.zTree.getZTreeObj("leftTree");
        treeObj.cancelSelectedNode();//清除组织树选中项

        $('#userTable').datagrid('clearSelections');//清除组织用户选中项

        $("#_searchUserName").attr('id_queryattr', ''),
            $("#_searchUserName").attr('nodecode_queryattr', '');
        $("#_searchUserName").attr('nodeId_queryattr', '');
        $("#_searchUserName").attr('formId_queryattr','');
        $("#_searchUserName").attr('formType_queryattr', '');
        $("#_searchUserName").val("");
        _onblur4userSearch($("#_searchUserName"));
    }

    // 取消
    function cancelSelectUser() {
        if(callback4Cancel && typeof callback4Cancel === 'function') {
            callback4Cancel();
        }

        closeWin();
    }

    // 添加用户，同一组织下的同一用户不能重复添加
    function addCheckUser() {//点击“添加”按钮触发事件
        var row = $('#userTable').datagrid('getSelected');
        if (row) {
            var userId = row.userId;
            var partyName = row.partyName;
            var orgId = row.orgId;
            var orgName = row.orgName;
            var data = $('#selUserTable').datagrid('getData');

            if(isExistRow(data.rows, userId, orgId)) {
                appendRow($('#selUserTable'), {
                    userId : userId,
                    orgId : orgId,
                    orgName : orgName,
                    partyName : partyName
                });
            }
        } else {
            $.messager.alert('提示', '请选择一条记录!');
        }

        event.stopPropagation();
    }

    //点击查询按钮
    function _searchOrgUserList(optType) {
        if(optType == 1 && event.keyCode == 13 || optType == 2) {
            var orgId = $("#_searchUserName").attr('id_queryattr'),
                nodeCode = $("#_searchUserName").attr('nodecode_queryattr'),
                nodeId = $("#_searchUserName").attr('nodeId_queryattr'),
                formId = $("#_searchUserName").attr('formId_queryattr'),
                formType = $("#_searchUserName").attr('formType_queryattr'),
                partyName = "";

            if(!$("#_searchUserName").hasClass("keyBlank")) {
                partyName = $("#_searchUserName").val();
            }

            if(orgId) {
                $("#userTable").egrid('reload', {
                    id : orgId,
                    nodeCode : nodeCode,
                    nodeId : nodeId,
                    formId : formId,
                    formType : formType,
                    partyName: partyName
                });
            } else {
                $.messager.alert('警告', '请选择组织机构!', 'warning');
            }
        }
    }

    function _onfocus4userSearch(obj) {//用户名称搜索 获取焦点事件
        if($(obj).hasClass("keyBlank")){
            $(obj).val("");
            $(obj).removeClass('keyBlank')
        }
    }

    function _onblur4userSearch(obj) {//用户名称搜索 失去焦点事件
        var keyWord = $(obj).val();

        if(keyWord == ''){
            $(obj).addClass('keyBlank');
            $(obj).val($(obj).attr("defaultValue"));
        }
    }

    // 将saveForm中的usernames对应到已选人员选择面板
    function setSelUser() {
        var userIds = $("#" + hiddenId).val();
        var userNames = $("#" + fileldId).html();
        var orgIds = $("#" + curOrgIds).val();
        var orgNames = $("#" + curOrgNames).val();

        if($("#" + fileldId)[0].nodeName.toLowerCase() == 'input') {
            userNames = $("#" + fileldId).val();
        }

        if (userIds != "" && userNames != "") {
            var arr_userIds = userIds.split(",");
            var arr_userNames = userNames.split(",");
            var arr_orgIds = orgIds.split(",");
            var arr_orgNames = orgNames.split(",");
            var isPromoterSel = false;

            for ( var i = 0; i < arr_userIds.length; i++) {
                appendRow($('#selUserTable'), {
                    userId : arr_userIds[i],
                    orgId : arr_orgIds[i],
                    orgName : arr_orgNames[i],
                    partyName : arr_userNames[i]
                });

                if (arr_userIds[i] == $("#promoter").attr("userId")) {
                    isPromoterSel = true;
                }
            }

            $("#promoter").attr("checked", isPromoterSel);
        } else {
            $("#promoter").attr("checked", false);
        }
    }

    // 快速定位
    function searchInputFocus() {
        $("#fastSearchTxt").val("");
    }

    function searchInputBlur() {
        $("#fastSearchTxt").val("请输入人员姓名或者帐号...");
    }

    function fastSearchUser() {
        _curnodeName = $("#preNode2").val();
        _nodeName = $("#targetNode2").val();
        var fastSearchTxt = $("#fastSearchTxt").val();
        if (fastSearchTxt == null || fastSearchTxt == "") {
            $("#fast_user_div").hide();
            return;
        } else {
            $("#fast_user_div").html("数据加载中...");
            $("#fast_user_div").show();
            $
                .post(
                    "${rc.getContextPath()}/zhsq/workflow/workflowController/searchUser.jhtml?t="
                    + Math.random(),
                    {
                        "inputName" : fastSearchTxt,
                        curnodeName : _curnodeName,
                        nodeName : _nodeName
                    },
                    function(data) {
                        if (data.length > 0) {
                            var itemsHtml = '<table width="100%" class="select-table">';
                            for ( var i = 0; i < data.length; i++) {
                                // itemsHtml += '<tr style="cursor:pointer"
                                // ondblclick="itemSelected('+data[i].userId+',\''+data[i].partyName+'-'+data[i].userName+'\')">';
                                itemsHtml += '<tr style="cursor:pointer" ondblclick="itemSelected('
                                    + data[i].orgId
                                    + ',\''
                                    + data[i].userId
                                    + ','
                                    + data[i].partyName
                                    + '-'
                                    + data[i].userName + '\')">';
                                itemsHtml += '<td class="select-td" >'
                                    + data[i].partyName + '-'
                                    + data[i].userName + '-'
                                    + data[i].orgName + '</td>';
                                itemsHtml += '</tr>';
                            }
                            itemsHtml += '</table>';
                            $("#fast_user_div").html(itemsHtml);
                        } else {
                            $("#fast_user_div")
                                .html(
                                    "<table width='100%'><tr><td><span style='color:red;'>没有找到相应数据</span></td><td><a href='javascript:void(0)' onclick='hideFastUser()'>关闭</a></td></tr></table>");
                        }
                    }, "json");
        }
    }

    function itemSelected(orgId, userIdName) {//快速选择 人员选择
        userId = userIdName.split(",")[0];
        userName = userIdName.split(",")[1];
        var data = $('#selUserTable').datagrid('getData');

        if(isExistRow(data.rows, userId, orgId)) {
            $('#selUserTable').datagrid('appendRow', {
                userId : userId,
                orgId : orgId,
                partyName : userName
            });
            if (userId == $("#promoter").attr("userId")) {
                $("#promoter").attr("checked", true);
            }
        }

        $("#fast_user_div").hide();

        event.stopPropagation();
    }

    function hideFastUser() {
        $("#fast_user_div").hide();
    }

    function displayFastUser() {
        $("#fast_user_div").show();
    }

    function _ztreeOnkeydown4User(obj) {
        var keyCode = event.keyCode,
            keyValue = $(obj).val();

        if(keyCode == 13 && keyValue) {
            var treeId = 'leftTree';

            _ztreeExpandAll4User(null, treeId);
        }
    }

    function _ztreeExpandAll4User(event, treeId, treeNode) {
        var orgInfoSearchCondition4User = $('#orgInfoSearchCondition4User').val();

        if(isNotBlankStringTrim(orgInfoSearchCondition4User)) {
            var treeObj = $.fn.zTree.getZTreeObj(treeId),
                nodeArray = [],
                node = null;

            if(treeNode) {
                nodeArray.push(treeNode);
            } else {
                nodeArray = treeObj.transformToArray(treeObj.getNodes());
            }

            for(var index in nodeArray) {
                node = nodeArray[index];

                if(node.isParent == true) {
                    treeObj.expandNode(node, true, true);
                }
            }

            search_ztree(treeId, 'orgInfoSearchCondition4User');
        }
    }

</script>

</html>