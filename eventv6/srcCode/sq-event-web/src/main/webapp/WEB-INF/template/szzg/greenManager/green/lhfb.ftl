<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>绿化分布</title>
<#include "/szzg/common.ftl" />

<#include "/component/ComboBox.ftl" />
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/normal.css"/>

    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/easyuiExtend.css"/>
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript" src="${rc.getContextPath()}/js/echarts/echarts-all.js"></script>

    <style>
        body {
            color: #fff;
        }

        .tip {
            color: white;
            text-decoration: none;
        }

        input {
            background-color: transparent !important;
            color: #fff !important;
        }

        span {
            color: #fff !important;
        }

        #combobox_INPUTorgName_div_0 {
            background-color: rgba(30, 64, 89, 0.75) !important;
        / / color: #fff !important;
        }

    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">


        <div class="npAlarminfo citybgbox bpsmain" style="width:360px; height: 480px;margin-left: 7px;">


            <div class="bpscon-box-chart"
                 style="    margin: 2px 2px 0 0px;width: 350px; margin-left:5px;height: 120px;">
                <div class="lookbox" style="margin-left: 10px;">
                    <font style="color: white">绿化类型：</font>
                    <select id="gType" style="width:138px">
                        <option value="">全部</option>
                    <#list gType as type>
                        <option value="${type.dictCode}">${type.dictName}</option>
                    </#list>
                    </select>

                </div>

                <div style="margin-left: 29px">
                    <font style="color: white;margin-left: 10px">地区：</font>

                    <input type="hidden" id="orgCode" name="orgCode" value="${orgCode}">
                    <input class="inp1 inp2 InpDisable easyui-validatebox" style="width:132px;" type="text" id="orgName"
                           value="${orgName}"
                           name="orgName"/>
                </div>
                <div>
                    <div style="float: left;margin-left: 40px;margin-top: 5px">名称：</div>
                    <div class="comand-search" style="width: 39%;margin-left: 82px;margin-top: 5px">

                        <input type="text" id="gName"  style="width: 80px" class="cs-inpt"><a href="#" ><img
                            src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_com_icon2.png"
                            onclick="searchData(1)" width="20" height="16" alt=""/></a>
                    </div>

                </div>

            </div>

            <div class="key-con" style="width: 330px;margin-top: 10px;margin-left: 5px;height: 330px;">

                <div class="grosslist" id="table_flow" style="height:330px;width:330px;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" id="table" class="replist"
                           style=" border:1px solid #6d8ca4;">
                        <thead>
                        <tr class="grosslist">
                            <th style=" border:1px solid #6d8ca4;color: white">绿地名称</th>
                            <th style=" border:1px solid #6d8ca4;color: white">总面积(㎡)</th>
                            <th style=" border:1px solid #6d8ca4;color: white">地址</th>
                        </tr>
                        </thead>
                        <tbody id="tbMain" style="color: white"></tbody>
                    </table>
                </div>

                <div class="clearfloat"></div>
            </div><!--end .bpscon-->
        </div><!--end .bpsmain-->
    </div>

</div>
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
            ShowOptions: {EnableToolbar: true},
            OnCleared: function () {
                document.getElementById('orgCode').value = '';
            }
        });
        $("#combobox_INPUTgridName_div_0").mCustomScrollbar({theme: "minimal-dark"});
    });
    //添加滚动条样式
    $("#table_flow").mCustomScrollbar({theme: "minimal-dark"});

    $(function () {
        loadDataList();
        data($('#gType').val(), $('#gName').val(),${orgCode})
    });


    function data(gType, gName, orgCode) {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/green/searchData.json?t=' + Math.random(),
            type: 'POST',
            data: {gType: gType, gName: gName, orgCode: orgCode},
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                var point = new Array(data.length);
                ;
                for (var i = 0; i < data.length; i++) {
                    var p = new Object();
                    p.name = data[i].gName;
                    p.x = data[i].x;
                    p.y = data[i].y;
                    point[i] = p;
                }

                var mapLocationObject = {
                    locationMaplist: point
                };
                //window.parent.locationPointsOnMap(mapLocationObject);

            }
        });

    }

    function loadDataList() {
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/green/searchData.json?t=' + Math.random(),
            type: 'POST',
            data: {orgCode:${orgCode}},
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');
            },
            success: function (data) {
                var tbody = document.getElementById('tbMain');


                var tab = document.getElementById("table");
                //表格行数
                var rows = tab.rows.length;

                if (rows >= 2) {
                    for (var i = 1; i < rows; rows--) {
                        document.getElementById('table').deleteRow(i);
                    }

                }

                for (var i = 0; i < data.length; i++) { //遍历一下json数据
                    var trow = getDataRow(data[i]); //定义一个方法,返回tr数据
                    tbody.appendChild(trow);
                }

            }
        });
    }


    function searchDataList(a) {
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/green/searchData.json?t=' + Math.random(),
            type: 'POST',
            data: {gName: a.gName, gType: a.gType, orgCode: a.orgCode},
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');
            },
            success: function (data) {
                var tbody = document.getElementById('tbMain');


                var tab = document.getElementById("table");
                //表格行数
                var rows = tab.rows.length;

                if (rows >= 2) {
                    for (var i = 1; i < rows; rows--) {
                        document.getElementById('table').deleteRow(i);
                    }

                }

                for (var i = 0; i < data.length; i++) { //遍历一下json数据
                    var trow = getDataRow(data[i]); //定义一个方法,返回tr数据
                    tbody.appendChild(trow);
                }

            }
        });
    }


    function getDataRow(h) {

        var row = document.createElement('tr'); //创建行

        var idCell = document.createElement('td'); //创建第一列id
        idCell.innerHTML = "<div style='font-size：12px;margin-left:10px;width: 100px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;'><a href='#' class='tip' title=" + h.gName + ">" + h.gName + "</a></div>"; //填充数据
        row.appendChild(idCell); //加入行  ，下面类似

        var nameCell = document.createElement('td');//创建第二列name
        nameCell.innerHTML = "<div style='font-size: 14px'>" + h.gArea + "</div>";
        row.appendChild(nameCell);

        var locationCell = document.createElement('td');//创建第二列name
        locationCell.innerHTML = "<div style='font-size：12px;margin-left:10px;width: 100px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;'><a href='#' class='tip' title=" + h.location + ">" + h.location + "</div>";
        row.appendChild(locationCell);

        return row; //返回tr数据
    }


    function showPie(data, id) {

        if (data.length == 0) {
            noData(id);
            return;
        }
        var optionPie = {
            calculable: true, legend: {orient: 'vertical', x: 'right', textStyle: {color: '#fff'}, data: []},
            tooltip: {trigger: 'item', formatter: "{b}:{c}({d}%)"}, series: [{
                name: "", type: 'pie', radius: ['30%', '50%'], data: [],
                itemStyle: {
                    normal: {label: {show: false}, labelLine: {show: false}},
                    emhasis: {
                        label: {
                            show: true, position: 'center',
                            textStyle: {fontSize: '30', fontWeight: 'bold'}
                        }
                    }
                }
            }]
        };
        for (var i = 0, l = 4; i < l; i++) {
            optionPie.legend.data.push(data[i].name);
            optionPie.series[0].data.push({value: data[i].value, name: data[i].name});
        }
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(optionPie);
    }

    function isNum(n) {
        return n ? n : 0;
    }


    function searchData() {
        var a = new Array();

        if ($("#gType").val() != null & $("#gType").val() != "") {
            a["gType"] = $.trim($("#gType").val());
        }

        if ($("#orgCode").val() != null & $("#orgCode").val() != "") {
            a["orgCode"] = $.trim($("#orgCode").val());
        }


        if ($("#gName").val() != null & $("#gName").val() != "") {
            a["gName"] = $.trim($("#gName").val());
        }


        data($('#gType').val(), $('#gName').val(),${orgCode})
        searchDataList(a);
    }


    function resetCondition() {
        $('#gName').val("");

        data($('#gType').val(), $('#gName').val(),${orgCode})
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
