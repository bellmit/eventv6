<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>居民就医</title>



<#include "/szzg/common.ftl" />


</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">


        <div class="npAlarminfo citybgbox bpsmain" style="width:650px;height:450px">
            <div class="bpscon-tit" style="border: hidden" id="ul_div">
                <ul id='ul_org'>
                <#list list as org>
                    <li onclick="rkClick(this,'${org.dictCode}')" title="${org.dictCode}">${org.dictName}</li>
                </#list>
                </ul>
            </div>


            <div class="key-left" style="width:460px;margin-left: 10px;">
                <div class="key-con">
                    <div class="dpchart">
                        <div id="chart_rk1" style="width:460px;height:200px"></div>
                    </div>
                </div>

            </div>

            <div class="key-con" style="    width: 460px;margin-top: 10px;margin-left: 140px; height: 200px;">

                <div class="grosslist" id="table_flow" style="max-height:100%;width: 100%;overflow:auto;">
                    <table width="96%" border="0" cellspacing="0" cellpadding="0" id="table"  style=" border:1px solid #6d8ca4;" class="replist">
                        <thead>
                        <tr class="grosslist">
                            <th style=" border:1px solid #6d8ca4;">区域</th>
                            <th style=" border:1px solid #6d8ca4;">医疗网点数</th>
                            <th style=" border:1px solid #6d8ca4;">人口数(万人)</th>
                            <th style=" border:1px solid #6d8ca4;">比例</th>

                        </tr>
                        </thead>
                        <tbody id="tbMain"></tbody>
                    </table>
                </div>

                <div class="clearfloat"></div>
            </div><!--end .bpscon-->
        </div><!--end .bpsmain-->
    </div>
</div>
</body>

<script type="text/javascript">

    var colorObj = {0: '#ffbc48', 1: '#20c0d6', 2: '#fc3478'};
    //添加滚动条样式
    $("#ul_div").mCustomScrollbar({theme: "minimal-dark"});
    $("#table_flow").mCustomScrollbar({theme: "minimal-dark"});
    $(function () {
        $("#ul_org li").first().click();



    });

    function data(hospitalMType,orgCode) {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/hospital/searchData.json?t=' + Math.random(),
            type: 'POST',
            data:{hospitalMType:hospitalMType,orgCode:orgCode},
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                var point = new Array(data.length);
                ;
                for (var i = 0; i < data.length; i++) {
                    var p = new Object();
                    p.name = data[i].hospitalName;
                    p.x = data[i].x;
                    p.y = data[i].y;
                    point[i] = p;
            }

                var mapLocationObject = {
                    locationMaplist: point
                };
                window.parent.locationPointsOnMap(mapLocationObject);

            }
        });

    }


    function getTable(orgCode, data) {

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
            if(data[i].TOTALPOPU!=null)
            {
                var trow = getDataRow(data[i]); //定义一个方法,返回tr数据
                tbody.appendChild(trow);
            }

        }

    }
    function getDataRow(h) {

        var row = document.createElement('tr'); //创建行

        var idCell = document.createElement('td'); //创建第一列id
        idCell.innerHTML = h.ORG_NAME; //填充数据
        row.appendChild(idCell); //加入行  ，下面类似

        var nameCell = document.createElement('td');//创建第二列name
        nameCell.innerHTML = h.HOSPITAL_NUMBER;
        row.appendChild(nameCell);

        var jobCell = document.createElement('td');//创建第三列job
        jobCell.innerHTML = h.TOTALPOPU;
        row.appendChild(jobCell);

        if(h.TOTALPOPU != 0) {
            var propCell = document.createElement('td');//创建第三列job
            propCell.innerHTML = '1:' + h.TOTALPOPU / h.HOSPITAL_NUMBER;
            row.appendChild(propCell);
        }else {
            var propCell = document.createElement('td');//创建第三列job
            propCell.innerHTML = '1:0';
            row.appendChild(propCell);

        }

        //到这里，json中的数据已经添加到表格中，下面为每行末尾添加删除按钮


        return row; //返回tr数据
    }

    function rkClick(o, type) {
        if (type == undefined) {
            type = type;
        }

        $("#ul_org li").removeClass("bpscon-tit-dq");
        $(o).addClass("bpscon-tit-dq");

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/hospital/getChartData.json?t=' + Math.random(),
            type: 'POST',
            data: {type: type, orgCode:${orgCode}},
            dataType: "json",
            error: function (data) {
                    $.messager.alert('提示', '信息获取异常!', 'warning');

                },
                success: function (data) {

                    showData(data.dataList, "chart_rk1");
                    getTable(type, data.dataList);
                }

            });

    }

    function showData(data,id) {

        if (data.length == 0) {
            noData(id);
            return;
        }
        var series = [
                    {
                        name: "医疗服务网点数",
                        type: 'bar',
                        data: [],
                        yAxisIndex: 1,
                        axisLabel: {textStyle: {color: "#fff"}},
                        itemStyle: {
                            normal: {
                                barBorderRadius: 0, color: (function (a, b, c) {
                                    return colorObj[2];
                                })()
                            }, barWidth: 20
                        }
                    },
                    {
                        name: "人口数(万人)",
                        type: 'bar',
                        data: [],
                        yAxisIndex: 1,
                        axisLabel: {textStyle: {color: "#fff"}},
                        itemStyle: {
                            normal: {
                                barBorderRadius: 0, color: (function (a, b, c) {
                                    return colorObj[1];
                                })()
                            }, barWidth: 20
                        }
                    }],
                xAxis = [];
        for (var i = 0, l = data.length; i < l; i++) {
           if(data[i].TOTALPOPU!=null)
           {
               xAxis.push(data[i].ORG_NAME);
               series[0].data.push(data[i].HOSPITAL_NUMBER);
               series[1].data.push(data[i].TOTALPOPU);
           }


        }
        var option = {
            tooltip: {trigger: 'axis'},
            legend: {data: ["医疗服务网点数", "人口数(万人)"], textStyle: {color: '#fff'}},
            calculable: true, grid: {x: 30, y: 40, x2: 44, y2: 20},
            yAxis: [{
                name: '',
                type: 'value',
                axisLine: {lineStyle: {color: '#fff'}},
                axisLabel: {textStyle: {color: "#fff"}}
            },
                {
                    name: '',
                    type: 'value',
                    axisLine: {lineStyle: {color: '#fff'}},
                    axisLabel: {textStyle: {color: "#fff"}}
                }],
            xAxis: [{type: 'category', data: xAxis, axisLabel: {textStyle: {color: "#fff"}}}],
            series: series
        };
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(option);
    }

    function isNum(n) {
        return n ? n : 0;
    }


</script>
</html>
