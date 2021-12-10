<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>南安矛盾纠纷</title>
    <#include "/component/standard_common_files-1.1.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/openWin.js"></script>
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
        <div class="fl">
            <ul>
                <form id="searchForm">

                    <li>所属网格：</li>

                    <li>
                        <input id="gridId" name="gridId" type="text" class="hide queryParam" />
                        <#if orgCodeName ??>
                            <input id="gridName" name="gridName" value="${orgCodeName}" type="text" class="inp1 InpDisable " style="width:100px;"/>
                        <#else>
                            <input id="gridName" name="gridName" type="text" class="inp1 InpDisable" style="width:100px;"/>
                        </#if>

                        <#if orgCode ??>
                            <input id="infoOrgCode" name="infoOrgCode" value="${orgCode}"type="text" class="hide"/>
                        <#else>
                            <input id="infoOrgCode" name="infoOrgCode" value="${InfoOrgCode}"type="text" class="hide"/>
                        </#if>

                    </li>

                    <li>案件编号：</li>
                    <li><input name="no" type="text" class="inp1" id="no"
                               style="font-size:12px; width:160px;"/>
                    </li>
                    <li>纠纷类别：</li>
                    <li>
                        <#if typecode ??>
                            <input id="type_" name="type_" value="${typecode}" type="text" class="hide queryParam"/>
                        <#else>
                            <input id="type_" name="type_" type="text" class="hide queryParam"/>
                        </#if>
                        <input id="typeStr" name="typeStr" type="text" class="inp1 InpDisable" style="width:120px;"/>
                    </li>
                    <li style="position:relative;">
                        <a href="javascript:void(0)" class="AdvanceSearchBtn FontDarkBlue">高级查询</a>
                        <div class="AdvanceSearch DropDownList hide" style="    width: 225px;    top: 42px;    left: -90px;">
                            <div class="LeftShadow">
                                <div class="RightShadow">
                                    <div class="list NorForm" style="position:relative;">
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td><label class="LabName width65px" style="width: 70px;"><span>状态：</span></label>

                                                    <#if status ??>
                                                        <input id="status" name="status" value="${status}" type="text" class="hide queryParam"/>
                                                    <#else>
                                                        <input id="status" name="status" type="text" class="hide queryParam"/>
                                                    </#if>
                                                    <input id="statusStr" name="statusStr" type="text" class="inp1 InpDisable" style="width:100px;"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><label class="LabName width65px" style="width: 70px;"><span>开始日期：</span></label>
                                                    <input type="text" id="startDateStr" name="startDateStr"  class="inp1 Wdate  inp1 " data-options="required:true, validType:'maxLength[20]', tipPosition:'bottom'"
                                                           data-options="required:true, tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDateStr\')}'});" readonly style="cursor: pointer;font-size:12px;"/>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td><label class="LabName width65px" style="width: 70px;"><span>结束日期：</span></label>
                                                    <input type="text" id="endDateStr" name="endDateStr"  class="inp1 Wdate  inp1 " data-options="required:true, validType:'maxLength[20]', tipPosition:'bottom'"
                                                           data-options="required:true, tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'startDateStr\')}'});" readonly style="cursor: pointer;font-size:12px;"/>
                                                </td>
                                            </tr>



                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                </form>
            </ul>
        </div>
        <div class="btns">
            <ul>
                <li><a href="#" class="chaxun" title="点击查询" onclick="searchData()">查询</a></li>
                <li><a href="#" class="chongzhi" title="重置查询条件" onclick="resetCondition()">重置</a></li>
            </ul>
        </div>
    </div>



</div>
<#include "/component/ComboBox.ftl">

<script type="text/javascript">
    var opt = {
        maxWidth: 735,
        maxHeight: 400
    };

</script>
<script type="text/javascript">
    var search = 0;
    var flag = "";
    var startGridId = ${startGridId?c};
    var startGridName = '${startGridName}';
    var InfoOrgCode = '${InfoOrgCode}';
    //传入的矛盾纠纷类型编码
    var typecode = '${typecode}';
    //传入的矛盾纠纷状态
    var mdjfstatus = '${status}';
    var createTimeStart = '${createTimeStart}';
    var createTimeEnd = '${createTimeEnd}';

    //记录主要当事人人数
    var peopleSize = 0;

    $(function () {

        if( createTimeStart != null){
            $('#startDateStr').attr('value',createTimeStart);
        }
        if(createTimeEnd != null){
            $('#endDateStr').attr('value',createTimeEnd);
        }


        if (startGridId == -99) {
            $('#gridAdminContentDiv').html("<table id='list'><tr><td style='color:#ff0000;'>没有找到相应的网格</td></tr></table>");
        } else {
            loadDataList();
        }
		 

        AnoleApi.initGridZtreeComboBox("gridName", "gridId", function (gridId, items) {
            if (items != undefined && items != null && items.length > 0) {
                var grid = items[0];
                $("#infoOrgCode").val(grid.orgCode);
            }
        }, {
			startGridId :  ${startGridId?c}
		});
        var type_,status;
        if (typecode !=null){
            type_ = typecode;
        }else {
            type_='';
        }
        AnoleApi.initTreeComboBox("typeStr", "type_", "A001093100", null, [type_],
            {
                ChooseType: "1", ShowOptions: {EnableToolbar: true}, DefText: '请选择'
            });
        if (mdjfstatus !=null){
            status = mdjfstatus;
        }else {
            status='';
        }
        AnoleApi.initTreeComboBox("statusStr", "status", "A001093101", null, [status],
            {
                ShowOptions: {EnableToolbar: true}, DefText: '请选择'
            });

    });


    function titleFormatter(value, rowData, rowIndex) {
        var title = "";

        if(value) {
            title = '<span title="'+ value +'" >'+ value +'</span>';
        }

        return title;
    }
    function loadDataList() {
        $('#list').datagrid({
            width: 600,
            height: 600,
            nowrap: true,
            rownumbers: true,
            remoteSort: false,
            striped: true,
            fit: true,
            fitColumns: true,
            singleSelect: true,
            idField: 'id',
            url: '${rc.getContextPath()}/zhsq/event/mediationNa/listData.json',
            frozenColumns: [[]],
            columns: [[
                {field: 'no',title: '案件编号',align: 'left',width: $(this).width() * 0.16 ,
                    formatter: function (value, rec, index) {
                        if (value == null) return "";
                        var f = '';
                        if (value != null) {
                            f = '<a href="###" title="' + rec.no + '" onclick="showRow(' + rec.id + ')"><span style="color:blue; text-decoration:underline;">' + value + '</span></a>&nbsp;';
                        }
                        return f;
                    }},
                {field: 'flowName', title: '所属网格', align: 'center', width: $(this).width() * 0.14},
                {field: 'disputeType', title: '纠纷类别', align: 'center', width: $(this).width() * 0.1},
                {field: 'partyName', title: '当事人', align: 'center', width: $(this).width() * 0.2, formatter: titleFormatter},
                {field: 'locality', title: '发生地', align: 'center', width: $(this).width() * 0.08, formatter: titleFormatter},
                {field: 'acceptInstitution', title: '受理机构', align: 'center', width: $(this).width() * 0.1, formatter: titleFormatter},
                {field: 'status',title: '状态',align: 'center',width: $(this).width() * 0.08}
            ]],
            toolbar: '#jqueryToolbar',
            pagination: true,
            pageSize: 20,
            //queryParams: queryParams,
            queryParams: $('#searchForm').serializeJson(),
            onLoadSuccess: function (data) {
                $('#list').datagrid('clearSelections');	//清除掉列表选中记录
                if (data.total == 0) {
                    var body = $(this).data().datagrid.dc.body2;
                    body.append('<div style="text-align: center;"><img src="${rc.getContextPath()}/theme/xingwang/images/nodata.png" title="暂无数据"/></div>');

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
            displayMsg: '当前显示第 {from} 到 {to} 条记录   共 {total} 条记录'
        });
    }
    function showRow(id) {
        var url= "${rc.getContextPath()}/zhsq/event/mediationNa/view.jhtml?id="+id;
        showMaxJqueryWindow("矛盾纠纷详情", url, null, null);
    }
    //csk 重置按钮，按地域编码擦寻
    function resetCondition() {
        var orgCode= '${orgCode}';
        var typecode= '${typecode}';
        var status = '${status}';
        var createTimeStart = '${createTimeStart}';
        var createTimeEnd = '${createTimeEnd}';
        var url = "${rc.getContextPath()}/zhsq/event/mediationNa/toMdjfList.jhtml?orgCode="+orgCode+"&typecode="+typecode+"&status="+status+"&createTimeStart="+createTimeStart+"&createTimeEnd="+createTimeEnd;
        window.location.replace(url);
    }

    //查询
    function searchData() {
        var a = {};
        a['infoOrgCode']=$("#infoOrgCode").val();
        a['no']=$("#no").val();
        a['type_']=$("#type_").val();
        a['status']=$("#status").val();
        a['startDateStr']=$("#startDateStr").val();
        a['endDateStr']=$("#endDateStr").val();
        $('#list').datagrid('clearSelections');
        $("#list").datagrid('options').queryParams = a;
        $("#list").datagrid('load');
    }


    //-- 供子页调用的重新载入数据方法
    function reloadDataForSubPage(result) {
        closeMaxJqueryWindow();
        $.messager.alert('提示', result, 'info');
        $("#list").datagrid('load');
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