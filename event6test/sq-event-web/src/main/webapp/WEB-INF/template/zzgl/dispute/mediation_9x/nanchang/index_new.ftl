<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>我的矛盾纠纷-南昌</title>
<#include "/component/commonFiles-1.1.ftl" />
    <script type="text/javascript"
            src="${rc.getContextPath()}/base/getDictionaryListByConfig.jhtml?var=dictionaryData&bid=resident"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/module/resident/resident.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
    <!-- <script type="text/javascript" src="${APP_URL_GEO}/js/components/geoAddressPlugin/jquery.anole.address.js"></script>  -->
    <script type="text/javascript" src="${rc.getContextPath()}/js/module/addressPlugin/jquery.anole.address.js"></script> 
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">

        /*图标选中凹陷效果只有在ie9及其以上才有效果*/
        .icon_select {
            background: #ccc;
            box-shadow: inset 1px 1px 0px 0px #999;
            border-radius: 3px;
            height: 23px;
            line-height: 23px;
            display: inline-block;
            padding: 0 15px 0 0;
            text-align: center;
            margin-left: 10px;
        }

    </style>
    <script type="text/javascript" src="${rc.getContextPath()}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
<#global ffcs=JspTaglibs["/WEB-INF/tld/RightTag.tld"] >
</head>
<body class="easyui-layout">

<div class="MainContent">

</div>
<div id="FerryInfoDiv" region="center" border="false" style="overflow:hidden;">
    <table id="list"></table>
</div>

<div id="jqueryToolbar" style="padding-top:0;">
    <div class="ConSearch" style="padding:10px 10px 0 10px;">
    <input type="hidden"  id="createId" name="createId" value="${createId!''}">
        <div class="fl">
            <ul>
                <li>所属区域：</li>
                <li><input type="hidden" id="gridId" name="gridId" value="${startGridId?c}"><input name="gridName"
                                                                                                   id="gridName"
                                                                                                   type="text"
                                                                                                   class="inp1 InpDisable"
                                                                                                   value="${startGridName!}"
                                                                                                   style="width:150px;"/>
                </li>
                <li>关键字：</li>
                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="事件名称/事件简述/发生地点"
                           style="font-size:12px;font-style:italic;color:gray; width:160px;"
                           onfocus="if(this.value=='事件名称/事件简述/发生地点'){this.value='';}$(this).attr('style','width:160px;');"
                           onblur="if(this.value==''){$(this).attr('style','font-size:12px;font-style:italic;color:gray;width:160px;');this.value='事件名称/事件简述/发生地点';}"/>
                </li>
                <li>发生日期：</li>
                <li><input type="text" class="Wdate inp1"
                           onfocus="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})"
                           id="startHappenTime" style="width: 115px;" name="startHappenTime"
                           value="${startHappenTime!}"/></li>
                <li>至</li>
                <li><input type="text" class="Wdate inp1"
                           onfocus="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})"
                           id="endHappenTime" style="width: 115px;" name="endHappenTime" value="${endHappenTime!}"/>
                </li>
                <li style="position:relative;">
                    <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                    <div class="AdvanceSearch DropDownList hide" style="width:375px; top: 42px; left: -130px;">
                        <div class="LeftShadow">
                            <div class="RightShadow">
                                <div class="list NorForm" style="position:relative;">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td><label class="LabName width65px"><span>事件类别：</span></label>
                                                <input type="hidden" id="disputeType2" name="disputeType2"
                                                       value="${disputeType!''}">
                                                <input name="disputeType2Str" id="disputeType2Str" style="width:150px;"
                                                       type="text" class="inp1 InpDisable"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><label class="LabName width65px"><span>化解时限：</span></label>
                                                <input class="inp1 Wdate fl" type="text" id="mediationDeadlineStart"
                                                       name="mediationDeadlineStart"
                                                       style="width:110px; *width:100px; cursor:pointer;"
                                                       onclick="WdatePicker({readOnly:true})" readonly="readonly"/>
                                                <span class="Check_Radio" style="padding:0 5px;">至</span>
                                                <input class="inp1 Wdate fl" type="text" id="mediationDeadlineEnd"
                                                       name="mediationDeadlineEnd"
                                                       style="width:110px; *width:100px; cursor:pointer;"
                                                       onclick="WdatePicker({readOnly:true})" readonly="readonly"/>
                                            </td>
                                        </tr>
                                        <tr>
                                    	
                                        	<td><label class="LabName width65px"><span>采集日期：</span></label>
                                        	<input class="inp1 Wdate fl" type="text" id="createTimeStart" name="createTimeStart" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly"/>
                                        	<span class="Check_Radio" style="padding:0 5px;">至</span>
                                        	<input class="inp1 Wdate fl" type="text" id="createTimeEnd" name="createTimeEnd" style="width:110px; *width:100px; cursor:pointer;" onclick="WdatePicker({readOnly:true})" readonly="readonly" />
                                        	</td>
                                    	</tr>
                                    	<tr>
                                    		<td><label class="LabName width65px"><span>状态：</span></label>
                                        	<select id="status" name="status" class="sel1" style="width: 92px;">
                                        		<option selected="selected" value="">不限</option>
                                        		<option value="1">草稿</option>
		                  						<option value="2">上报</option>
		                  						<option value="3">结案</option>
                                        	</select>
                                        	</td>
                                    	</tr>
                                        <tr>
                                            <td><label class="LabName width65px"><span>是否化解成功：</span></label>
                                                <select id="isSuccess" name="isSuccess" class="sel1"
                                                        style="width: 92px;">
                                                    <option selected="selected" value="">不限</option>
                                                    <option value="1">是</option>
                                                    <option value="0">否</option>
                                                </select>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>

            </ul>
        </div>
        <div class="btns">
            <ul>
                <li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
                <li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
    </div>

    <div class="h_10" id="TenLineHeight1"></div>
    <div class="ToolBar">
        <div id="iconDiv" class="fl">
            <a href="#" id="_allSearchAnchor" class="icon_select" onclick="allSearchData(this);"><i
                    class="ToolBarAll"></i>所有</a>
            <a href="#" onclick="handleSearchData('2', this);"><i class="ToolBarDue"></i>将到期</a>
            <a href="#" onclick="handleSearchData('3', this);"><i class="ToolBarOverDue"></i>已过期</a>
            <a href="#" onclick="handleSearchData('1', this);"><i class="ToolBarNormal"></i>正常</a>
        </div>
        <div class="tool fr" id="toolFrDIV" style="width:550px">
		 	<a href="javascript:report()" class="NorToolBtn ShangBaoBtn">上报</a>
	        <a href="javascript:batchDelete()" class="NorToolBtn DelBtn">删除</a>
	        <a href="javascript:editRow()" class="NorToolBtn EditBtn">编辑</a>
	        <a href="javascript:create();" class="NorToolBtn  AddBtn">新增</a>
        <!-- 	<@actionCheck></@actionCheck> -->
        </div>

    </div>

</div>
<#include "/component/ComboBox.ftl">

<script type="text/javascript">
    var opt = {
        maxWidth: 735,
        maxHeight: 400
    };

    $(function () {
        var $NavDiv = $(".nav ul li");
        $NavDiv.click(function () {
            $(this).addClass("current").siblings().removeClass("current");
            var NavIndex = $NavDiv.index(this);
            $(".TreeShow .con div").eq(NavIndex).show().siblings().hide();
        });

        $(".TreeHide").click(function () {
            $(".TreeShow").show();
        });
        $(".TreeClose").click(function () {
            $(".TreeShow").hide();
        });
    });
</script>
<script type="text/javascript">
    var search = 0;
    var flag = "";
    var startGridId = ${startGridId?c};
    var startGridName = '${startGridName}';
    var InfoOrgCode = '${InfoOrgCode}';
    var event_url = "${event_url}";
    var itype = "${itype!''}";
    var mediationType = "${mediationType!''}";
    var disputeType = "${disputeType!''}";

    var _isSuccess = "${isSuccess!}", _startCreateTime = "${startCreateTime!}", _endCreateTime = "${endCreateTime!}";

    $(function () {
        if (startGridId == -99) {
            $('#gridAdminContentDiv').html("<table id='list'><tr><td style='color:red;'>没有找到相应的网格</td></tr></table>");
        } else {
            loadDataList();
        }
        AnoleApi.initTreeComboBox("disputeType2Str", "disputeType2", "${disputeTypeDict!''}", null, [<#if disputeType??>'${disputeType}'</#if>],
        		{
        			ChooseType: "1"
        		});
        if (startGridId > 0) {
            AnoleApi.initGridZtreeComboBox("gridName", "gridId", null, {
                Async: {
                    enable: true,
                    autoParam: ["id=gridId"],
                    dataFilter: _filter,
//					otherParam : {
//						"startGridId" : startGridId
//					}
                }
            });
        } else {
            AnoleApi.initGridZtreeComboBox("gridName", "gridId");
        }

        $("#isSuccess option[value='" + _isSuccess + "']").attr("selected", true);
    });

    function loadDataList() {
        var queryParams = {
                    gridId: startGridId,
                    itype: itype,
                    mediationType: mediationType,
                    disputeType: disputeType,
                    createId:${createId!''},
                    search: 0
                },
                startHappenTime = "${startHappenTime!}", endHappenTime = "${endHappenTime!}";
        if (startHappenTime) {
            queryParams.startHappenTime = startHappenTime;
        }
        if (endHappenTime) {
            queryParams.endHappenTime = endHappenTime;
        }
        if (_isSuccess) {
            queryParams.isSuccess = _isSuccess;
        }
        if (_startCreateTime) {
            queryParams.startCreateTime = _startCreateTime;
        }
        if (_endCreateTime) {
            queryParams.endCreateTime = _endCreateTime;
        }

        $('#list').datagrid({
            rowStyler: function (index, row) {
                if (search == 1) {
                    if (row.count == 1) {
                        return 'background-color:#ffe466;font-weight:bold;';
                    }
                    if (row.count == 2) {
                        return 'background-color:#ffbd4a;font-weight:bold;';
                    }

                    if (row.count >= 3) {
                        return 'background-color:#ff4c4c;font-weight:bold;';
                    }
                }
            },
            width: 600,
            height: 600,
            nowrap: true,
            rownumbers: true,
            remoteSort: false,
            striped: true,
            fit: true,
            fitColumns: true,
            singleSelect: true,
            idField: 'mediationId',
            url: '${rc.getContextPath()}/zhsq/disputeMediation/listData.json',
            frozenColumns: [[]],
            columns: [[
                {
                    field: 'disputeEventName',
                    title: '事件名称',
                    align: 'left',
                    width: $(this).width() * 0.15,
                    formatter: function (value, rec, index) {
                        if (value == null) return "";
                        var f = '';
                        if (value != null) {
                            f = '<a href="###" title="' + rec.disputeEventName + '" onclick="showRow(' + rec.mediationId + ')"><span style="color:blue; text-decoration:underline;">' + value + '</span></a>&nbsp;';
                        }
                        return f;
                    }
                },
                {field: 'happenTimeStr', title: '发生日期', align: 'center', width: $(this).width() * 0.1},
                {field: 'createTime', title: '采集日期', align: 'center', width: $(this).width() * 0.1,
                    formatter:function(value,rec){
                        if (value != null && value != ''){
                            var date = new Date(value);
                            return date.formatString("yyyy-MM-dd hh:mm:ss");;
                        }
                        return "";
                }},
                {field: 'disputeTypeStr', title: '事件类别', align: 'center', width: $(this).width() * 0.2},
                {field: 'disputeScaleStr', title: '事件规模', align: 'center', width: $(this).width() * 0.1},
                {
                    field: 'happenAddr', title: '发生地点', align: 'center', width: $(this).width() * 0.13,
                    formatter: function (value, rec, rowIndex) {
                        if (value == null) return "";
                        if (value != null && value.length > 18) {
                            value = value.substring(0, 17);
                        }
                        var tag = '';
                        tag = '<span title="' + rec.happenAddr + '">' + value + '</span>';
                        return tag;
                    }
                },
                {
                    field: 'gridPath', title: '所属区域', align: 'center', width: $(this).width() * 0.1,
                    formatter: function (value, rec, rowIndex) {
                        var gridPath = value || '',
                                f = gridPath;

                        if (gridPath) {
                            f = '<div style="cursor:pointer" title="' + gridPath + '">' + gridPath + '</div>';
                        }

                        return f;
                    }
                },
                {
                    field: 'disputeStatus',
                    title: '状态',
                    align: 'center',
                    width: $(this).width() * 0.08,
                    formatter: function (value, rec, index) {
                        if (value == null) return "";
                        var f = '';
                        if (value != null) {
                            if (value == '1') f = "草稿";
                            else if (value == '2') f = "上报";
                            else if (value == '3') f = "结案";
                            else if (value == '4') f = "作废";
                        }
                        return f;
                    }
                },
                {field: 'status', hidden: true}
            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            queryParams: queryParams,
            onLoadSuccess: function (data) {
                $('#list').datagrid('clearSelections');	//清除掉列表选中记录
                if (data.total == 0) {
                    var body = $(this).data().datagrid.dc.body2;
                    body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/xingwang/images/nodata.png" title="暂无数据"/></div>');

                }
            },
            onClickRow: function (rowIndex, rowData) {
                $("#del").show();
                $("#edit").show();
                $("#add").show();
                $("#report").show();
                if (rowData.disputeStatus == '3' || rowData.disputeStatus == '2') {
                    $("#report").hide();
                    $("#del").hide();
                    $("#edit").hide();
                }
            }
        });

        //设置分页控件
        var p = $('#list').datagrid('getPager');
        $(p).pagination({
            pageSize: 20,//每页显示的记录条数，默认为
            pageList: [20, 30, 40, 50],//可以设置每页记录条数的列表
            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'/*,
			onBeforeRefresh:function(){
				$(this).pagination('loading');
				alert('before refresh');
				$(this).pagination('loaded');
			}*/
        });
    }

    function allSearchData(obj) {//点击所有图标
        iconSelect(obj);
    	flag = "";
        if(search==1)
        {
            quickSearch();
        }else
        {
            searchData();
        }

    }

    function handleSearchData(handleStatus, obj) {//点击超时、即将超时图标
        var searchArray = {};
        iconSelect(obj);
        searchArray["handleDateFlag"] = handleStatus;
        flag = handleStatus;
        if (search == 1) {
            quickSearch(searchArray);
        }
        else {
            searchData(searchArray);
        }
    }

    function iconSelect(obj) {//为选择的图标增添凹陷效果
        if (isNotBlankParam(obj)) {
            iconUnSelect();
            $(obj).addClass('icon_select');
        }
    }

    function iconUnSelect() {//去除图片的凹陷效果
        $("#iconDiv > a[class='icon_select']").each(function () {
            $(this).removeClass('icon_select');
        });
    }

    function resetCondition() {
        $("#gridName").val(startGridName);
        $("#gridId").val(startGridId);
        $("#keyWord").val("事件名称/事件简述/发生地点");
        $("#keyWord").attr('style', 'font-size:12px;font-style:italic;color:gray; width:160px;');
        $("#startHappenTime").val("${startHappenTime!}");
        $("#endHappenTime").val("${endHappenTime!}");
        $("#mediationDeadlineStart").val("");
        $("#mediationDeadlineEnd").val("");
        $("#disputeType2").val("");
        $("#disputeType2Str").val("");
        $('#createTimeStart').val("");
		$('#createTimeEnd').val("");
        $("#status").val("不限");
        $("#isSuccess option[value='" + _isSuccess + "']").attr("selected", true);
        iconSelect($("#_allSearchAnchor"));
        searchData();
    }

    //一键筛选
    function quickSearch(searchArray) {
        var a = new Array();
// 		var postData = {};
        if (searchArray != undefined && searchArray != null) {
            a = searchArray;
        }
        a["nanchang"]="1";
        if (mediationType != null && mediationType != "") {
            a["mediationType"] = mediationType;
        }

        var disputeType = $("#disputeType2").val();
        if (disputeType != null && disputeType != "") {
            a["disputeType"] = disputeType;
        }
        var startHappenTime = $("#startHappenTime").val();
        if (startHappenTime != null && startHappenTime != "") a["startHappenTime"] = startHappenTime;
        var endHappenTime = $("#endHappenTime").val();
        if (endHappenTime != null && endHappenTime != "") a["endHappenTime"] = endHappenTime;

        if (_startCreateTime) {
            a["startCreateTime"] = _startCreateTime;
        }
        if (_endCreateTime) {
            a["endCreateTime"] = _endCreateTime;
        }
        a["search"] = 1;
        search = 1;
        var isSuccess = $("#isSuccess").val();
        if (isSuccess) {
            a["isSuccess"] = isSuccess;
        }

        var mediationDeadlineStart = $("#mediationDeadlineStart").val();
        if (mediationDeadlineStart != null && mediationDeadlineStart != "") a["mediationDeadlineStart"] = mediationDeadlineStart;
        var mediationDeadlineEnd = $("#mediationDeadlineEnd").val();
        if (mediationDeadlineEnd != null && mediationDeadlineEnd != "") a["mediationDeadlineEnd"] = mediationDeadlineEnd;

        var gridId = $("#gridId").val();
        if (gridId != null && gridId != "") {
            a["gridId"] = gridId;
        } else {
            a["gridId"] = startGridId;
        }
        var keyWord = $("#keyWord").val();
        if (keyWord != null && keyWord != "" && keyWord != "事件名称/事件简述/发生地点") {
            a["keyWord"] = keyWord;
        }
        a["itype"] = itype;
        doSearch(a);
    }

    //查询

    function searchData(searchArray) {
        var a = new Array();
// 		var postData = {};
        if (searchArray != undefined && searchArray != null) {
            a = searchArray;
        }
        if (mediationType != null && mediationType != "") {
            a["mediationType"] = mediationType;
        }
        a["search"] = 0;
        search = 0;
        var disputeType = $("#disputeType2").val();
        if (disputeType != null && disputeType != "") {
            a["disputeType"] = disputeType;
        }
        var createTimeStart=$('#createTimeStart').val();
		if (createTimeStart != null && createTimeStart != "") {
			a["createTimeStart"]=createTimeStart;
		}
		var createTimeEnd=$('#createTimeEnd').val();
		if (createTimeEnd != null && createTimeEnd != "") {
			a["createTimeEnd"]=createTimeEnd;
		}
		
		var status=$('#status').val();
		if (status != null && status != "") {
			a["status"]=status;
		}
        var startHappenTime = $("#startHappenTime").val();
        if (startHappenTime != null && startHappenTime != "") a["startHappenTime"] = startHappenTime;
        var endHappenTime = $("#endHappenTime").val();
        if (endHappenTime != null && endHappenTime != "") a["endHappenTime"] = endHappenTime;

        if (_startCreateTime) {
            a["startCreateTime"] = _startCreateTime;
        }
        if (_endCreateTime) {
            a["endCreateTime"] = _endCreateTime;
        }

        var isSuccess = $("#isSuccess").val();
        if (isSuccess) {
            a["isSuccess"] = isSuccess;
        }
		var createId = $("#createId").val();
        if (createId != null && createId != "") {
            a["createId"] = createId;
        }
        var mediationDeadlineStart = $("#mediationDeadlineStart").val();
        if (mediationDeadlineStart != null && mediationDeadlineStart != "") a["mediationDeadlineStart"] = mediationDeadlineStart;
        var mediationDeadlineEnd = $("#mediationDeadlineEnd").val();
        if (mediationDeadlineEnd != null && mediationDeadlineEnd != "") a["mediationDeadlineEnd"] = mediationDeadlineEnd;

        var gridId = $("#gridId").val();
        if (gridId != null && gridId != "") {
            a["gridId"] = gridId;
        } else {
            a["gridId"] = startGridId;
        }
        var keyWord = $("#keyWord").val();
        if (keyWord != null && keyWord != "" && keyWord != "事件名称/事件简述/发生地点") {
            a["keyWord"] = keyWord;
        }
        a["itype"] = itype;
        doSearch(a);
    }

    function doSearch(queryParams) {
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = queryParams;
        $("#list").datagrid('load');
    }

    function create() {
        var url = '${rc.getContextPath()}/zhsq/disputeMediation/9x/create.jhtml?gridId=' + startGridId + '&itype=' + itype+'&location=nanchang';
        showMaxJqueryWindow("新增矛盾纠纷", url, null, null);
    }

    function editRow() {
        var rows = $('#list').datagrid('getSelections');
        var num = rows.length;
        if (num != 1) {
            $.messager.alert('提示', '请选择一条记录！', 'info');
        } else {
            var disputeStatus = rows[0].disputeStatus;
            if (disputeStatus != null && disputeStatus != "") {
                if (disputeStatus == '1') {
                    var id = rows[0].mediationId;
                    var url = '${rc.getContextPath()}/zhsq/disputeMediation/9x/edit.jhtml?mediationId=' + id + '&itype=' + itype + '&location=nanchang';
                    showMaxJqueryWindow("编辑矛盾纠纷", url, null,null, 'no');
                    $('#list').datagrid('unselectAll');
                } else {
                    $.messager.alert('提示', '已上报或结案归档记录不可编辑', 'info');
                    $('#list').datagrid('unselectAll');
                }
            }
        }
    }

    function showRow(id) {
        var url = '${rc.getContextPath()}/zhsq/disputeMediation/show/9x.jhtml?mediationId=' + id + '&itype=' + itype + '&location=nanchang';
        showMaxJqueryWindow("矛盾纠纷详情", url, null, null, 'no');
    }

	/* 删除 */
    function batchDelete() {
        var ids = [];
        var rows = $('#list').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            ids.push(rows[i].mediationId);
            if (rows[i].disputeStatus != '1') {
                $.messager.alert('提示', '上报和结案事件无法删除！', 'warning');
                return;
            }
        }
        var idStr = ids.join(',');
        if (idStr == null || idStr == "") {
            $.messager.alert('提示', '请选中要删除的数据再执行此操作！', 'warning');
            return;
        }
        $.messager.confirm('提示', '您确定删除选中的这些数据吗？', function (r) {
            if (r) {
                $.ajax({
                    type: "POST",
                    url: '${rc.getContextPath()}/zhsq/disputeMediation/batchDelete.jhtml',
                    data: 'mediationIds=' + idStr,
                    dataType: "json",
                    success: function (data) {
                        $.messager.alert('提示', data.result, 'info');
                        $("#list").datagrid('load');
                    },
                    error: function (data) {
                        $.messager.alert('错误', '连接超时！', 'error');
                    }
                });
            }
        });
    }

    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
    }

    //上报
    var reportDisputeId = 0;

    function report() {
        var rows = $('#list').datagrid('getSelections');
        var num = rows.length;
        if (num != 1) {
            $.messager.alert('提示', '请选择一条记录！', 'info');
        } else {
            var disputeStatus = rows[0].disputeStatus;
            var isSuccess = rows[0].isSuccess;
            if (disputeStatus != 1 || (typeof(isSuccess) != 'undefined' && isSuccess != null && isSuccess == 1 )) {
                $.messager.alert('提示', '该记录已化解不能进行上报操作', 'info');
                $('#list').datagrid('unselectAll');
            } else {
                //获取矛盾纠纷附件ids： outerAttachmentIds;
                var outerAttachmentIds = '';
                $.ajax({
                    type:"POST",
                    url: '${rc.getContextPath()}/zhsq/disputeMediation/detailAtt.jhtml',
                    data:{'mediationId':rows[0].mediationId,'attachmentType':'dispute_attachment_type','eventSeq':'1'},//返回附件list
                    dataType:'json',
                    success:function (data) {
                        for (var i=0,len=data.length; i<len ; i++){
                            outerAttachmentIds += data[i].attachmentId + ",";
                            //alert(outerAttachmentIds);
                        }
                        //将上报到事件的功能写到ajax方法执行成功之后，否则获取不到附件ids，跳转到事件页面之后，ajax获取的附件ids会在跳转到的事件页面并初始化之后才返回
                        var id = rows[0].mediationId;
                        reportDisputeId = id;
                        var eventName = rows[0].disputeEventName;
                        var occurred = rows[0].happenAddr;
                        var happenDateStr = rows[0].happenTimeStr;
                        var content = "" + rows[0].disputeCondition;
                        var gridId = rows[0].gridId;
                        var gridCode = rows[0].gridCode;
                        var gridName = rows[0].gridName;
                        var disputeType2 = rows[0].disputeType2;
                        var mediationId = rows[0].mediationId;
                        var eventType = "50";
                        if(disputeType2!=""){
                            eventType = eventType + disputeType2.substr(0, 2);
                        }
                        var event = {
                            "eventName": eventName,
                            "happenTimeStr": happenDateStr,
                            "occurred": occurred,
                            "content": content,
                            "gridId": gridId,
                            "gridCode": gridCode,
                            "gridName": gridName,
                            "outerAttachmentIds":outerAttachmentIds,
                            "type": eventType,
                            "typesForList": eventType,
                            'mediationId':mediationId,
                            "status": '',
                            "callBack": 'parent.reportCallback',
                            "iframeUrl": '${iframeUrl!}',
                            "iframeCloseCallBack": '${iframeCloseCallBack!}',
                            "eventReportRecordInfo": {
                                'bizType': 'DISPUTE_MEDIATION',
                                'bizId': reportDisputeId
                            }
                        };

                        var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByDisputeJson.jhtml';

                        var reportWinId = openJqueryWindowByParams($.extend({}, opt, {
                            maxWidth: 800,
                            title: "上报矛盾纠纷信息"
                        }));

                        var reportForm = '<form id="_report4eventForm" name="_report4eventForm" action="" target="' + reportWinId + '_iframe" method="post">' +
                                '</form>';

                        $("#jqueryToolbar").append($(reportForm));
                        $("#_report4eventForm").append($('<input type="hidden" id="_reportEventJson" name="eventJson" value="" />'));

                        $("#_reportEventJson").val(JSON.stringify(event));
                        $("#_report4eventForm").attr('action', url);

                        $("#_report4eventForm").submit();

                        $("#_report4eventForm").remove();
                    },
                    error:function(data){
                        $.messager.alert('错误','获取附件信息失败！','error');
                    }
                });
            }
        }
    }

    //上报回调函数
    function reportCallback(date) {
        if (date.instanceId != null && date.instanceId != "") {
            $.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/disputeMediation/report.jhtml',
                data: 'mediationId=' + reportDisputeId + '&&status=2&eventId=' + date.eventId,
                success: function (data) {
                    closeMaxJqueryWindow();
                    $.messager.alert('提示', '上报成功', 'info');
                    $("#list").datagrid('load');
                    $('#list').datagrid('unselectAll');
                },
                error: function (data) {
                    $.messager.alert('错误', '连接超时！', 'error');
                }
            });
        } else {
            $.messager.alert('提示', '上报失败，请联系系统管理员！', 'error');
        }
    }
    
    //导出
    function exportGridt() {
		//var options = $("#list").datagrid('options');
		
		var pm = "";
		//for (var key in options.queryParams) {
			if (search == '1') {
				pm += "&nanchang=1";				
			}
			pm += "&mediationType=" + mediationType;
			pm += "&disputeType=" + disputeType;
			pm += "&startHappenTime=" + $("#startHappenTime").val();
			pm += "&endHappenTime=" + $("#endHappenTime").val();
			pm += "&startCreateTime=" + _startCreateTime;
			pm += "&endCreateTime=" + _endCreateTime;
			pm += "&search=" + search;
			pm += "&isSuccess=" + $("#isSuccess").val();
			pm += "&mediationDeadlineStart=" + $("#mediationDeadlineStart").val();
			pm += "&mediationDeadlineEnd=" + $("#mediationDeadlineEnd").val();
			pm += "&gridId=" + $("#gridId").val();
			var keyWord = $("#keyWord").val();
	        if (keyWord != null && keyWord != "" && keyWord != "事件名称/事件简述/发生地点") {
	        	pm += "&keyWord=" + keyWord;
	        }
			pm += "&itype=" + itype;
			pm += "&handleDateFlag=" + flag;
			pm += "&InfoOrgCode=" + InfoOrgCode;
		//}
		var url = "${rc.getContextPath()}/common/export/excel.jhtml?beanId=disputeMediationServiceImpl" + pm;
		window.open(url);
	}


    Date.prototype.formatString = function(fmt){
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    };
</script>
</body>
</html>