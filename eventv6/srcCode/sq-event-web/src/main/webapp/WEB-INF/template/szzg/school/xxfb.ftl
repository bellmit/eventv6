
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>


    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">

    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css">

    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet"
          type="text/css"/>
    <script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>

    <script type="text/javascript" src="${uiDomain!''}/js/jquery.easyui.patch.js"></script>
    <!--�����޸�easyui-1.4��easyui-numberboxʧȥ�����������С���������-->

    <script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/layer/layer.js"></script>

    <script type="text/javascript" src="${rc.getContextPath()}/js/echart3/echarts.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/echart3/macarons.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/TreeGrid.js"></script>
    <script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>


    <script type="text/javascript">
        function noData(id) {
            var div = document.getElementById(id);
            var w = div.style.width, h = div.style.height;
            if (w.length == 0 || h.length == 0) {
                w = "100px", h = "120px";
            }
            div.innerHTML = "<div style='width:" + w + ";height:" + h + ";vertical-align: middle;text-align:center;display: table-cell;'><img src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
        }
    </script>


    <style type="text/css">
        body {
            color: #fff;
        }

        .npcityBottom .bpscon-tit li {
            padding: 19px 14px;
        }

        .buildleft {
            float: none;
            width: 450px;
            min-height: 38px;
            margin-left: 10px;
            margin-top: 20px;
        }

        .buildleft-list ul li {
            cursor: pointer;
            display: inline;
            margin: 0px;
        }

        .buildcon {
            margin-top: 0px;
        }

        .grosslist {
            width: 800px;
            height: 350px;
        }
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;z-index: 0px">

        <div class="npAlarminfo citybgbox bpsmain" style="width:810px;height:430px;margin-left: 5px">
            <div class="buildcon">
                <div class="buildleft">
                    <div class="buildleft-list">
                        <ul id="ul_org" style="margin-top:10px;">
                            <li onclick="yxClick(this,1)">学校总数统计图表</li>
                            <li onclick="yxClick(this,2)">男女生人数统计图表</li>
                            <li onclick="yxClick(this,3)">师生人数统计图表</li>

                        </ul>
                    </div>
                </div>
            </div>
            <div class="key-left" style="width:780px;height:310px;margin-left: 2px">
                <input type="text" class="inp1 Wdate timeClass" id="year" value="${year}"
                       style="width:100px;margin-left:7px;margin-top:20px;margin-bottom:3px;background-color:rgb(89,114,132);color:#fff"
                       onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:rkYearChange});">
                <div class="key-con" style="width:780px;height:310px;overflow: hidden" id="chart_1">
                    <div class="dpchart">
                        <div id="chart_rk1" style="width:780px;height:310px;"></div>
                    </div>
                </div>
                <div class="key-con" style="width:780px;height:310px;display: none;overflow: hidden" id="chart_2">
                    <div class="dpchart">
                        <div id="chart_rk2" style="width:780px;height:310px;"></div>
                    </div>
                </div>
                <div class="key-con" style="width:780px;height:310px;display: none;overflow: hidden" id="chart_3">
                    <div class="dpchart">
                        <div id="chart_rk3" style="width:780px;height:310px;"></div>
                    </div>
                </div>
            </div>
        </div><!--end .bpsmain-->

    </div>
</div>
</body>

<script type="text/javascript">

    var chooseDiv=1;
    //添加滚动条样式
    $("#ul_div").mCustomScrollbar({theme: "minimal-dark"});
    var myChart;
    $(function () {
        data();
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/school/findDataByChart.json?t=' + Math.random(),
            type: 'POST',
            dataType: "json",
            data: {year: $('#year').val()},
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                showSchoolChart(data, "chart_rk1");
//                showSexChart()
//                showTeacherChart()
            }
        });

    });

    function data() {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/school/searchData.json?t=' + Math.random(),
            type: 'POST',
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                var point = new Array(data.point.length);
                var data = data.point;
                for (var i = 0; i < data.length; i++) {
                    var p = new Object();
                    p.name = data[i].NAME;
                    p.x = data[i].X;
                    p.y = data[i].Y;
                    point[i] = p;
                }

                var mapLocationObject = {
                    locationMaplist: point
                };
                window.parent.locationPointsOnMap(mapLocationObject);

            }
        });

    }


    function rkYearChange(o) {

        $("#ul_org li").removeClass("bpscon-tit-dq");
        $(o).addClass("bpscon-tit-dq");

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/school/findDataByChart.json?t=' + Math.random(),
            type: 'POST',
            data: {year: o.cal.date.y},
            dataType: "json",
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                switch (chooseDiv) {
                    case 1:
                        showSchoolChart(data, "chart_rk1");
                        return;
                    case 2:
                        showSexChart(data, "chart_rk2");
                        return;
                    case 3:
                        showTeacherChart(data, "chart_rk3");
                        return;
                    default:
                        showSchoolChart(data, "chart_rk1");
                        return;
                }


            }
        });
    }

    function showSchoolChart(data, id) {

        var data1 = data.dataList['school'];
        var data2 = data.dataList['count2'];

        if (data1.length == 0) {
            noData(id);
            return;
        }
        var xAxisArr = [], series = [
                    {name: "幼儿园", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}},
                    {name: "小学", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}},
                    {name: "中学", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}},
                    {name: "中专技校", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}},
                    {name: "高职高专", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}},
                    {name: "大学", type: 'bar', barMaxWidth: 50, stack: '数量', data: [], textStyle: {color: "#fff"}}],
                legend = [];


        for (var i = 0, l = data1.length; i < l; i++) {
            xAxisArr.push(data1[i].ORG_NAME);

            series[0].data.push(data1[i].SHOOL1);
            series[1].data.push(data1[i].SHOOL2);
            series[2].data.push(data1[i].SHOOL3);
            series[3].data.push(data1[i].SHOOL4);
            series[4].data.push(data1[i].SHOOL5);
            series[5].data.push(data1[i].SHOOL6);

        }
        option = {
            title: {
                subtext: '学校总数：' + data2[0].SCHOOL_NUMBER,
                top: 0,
                left: 15
            },
            tooltip: {
                trigger: 'axis',
                showDelay: 0, // 显示延迟，添加显示延迟可以避免频繁切换，单位ms},
                fontSize: 5,
                confine: true
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                right: 5,
                top: 20,
                bottom: 20,
                data: ['幼儿园', '小学', '中学', '中专技校', '高职高专', '大学'], textStyle: {color: '#fff'},


            },
            calculable: true, grid: {
                x: 35, y: 50, x2:
                        220, y2:
                        30
            },
            yAxis: [{type: 'value', axisLabel: {textStyle: {color: "#fff"}}}],
            xAxis: [{type: 'category', data: xAxisArr, axisLabel: {textStyle: {color: "#fff"}}}],
            series: series
        };

        if (myChart != null && myChart != "" && myChart != undefined) {
            myChart.dispose();
        }

        myChart = echarts.init(document.getElementById(id), 'macarons');
        myChart.setOption(option);


    };

    function showSexChart(data, id) {

        var data1 = data.dataList['malesFemales'];//统计数据
        var data2 = data.dataList['count2'];//汇总数据

        if (data1.length == 0) {
            noData(id);
            return;
        }

        var series = [
                    {name: "幼儿园(男)", type: 'bar', barMaxWidth: 50, stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {name: "幼儿园(女)", type: 'bar', barMaxWidth: 50, stack: '女生', data: [], textStyle: {color: "#fff"}},
                    {name: "小学(男)", type: 'bar', barMaxWidth: 50, stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {name: "小学(女)", type: 'bar', barMaxWidth: 50, stack: '女生', data: [], textStyle: {color: "#fff"}},
                    {name: "中学(男)", type: 'bar', barMaxWidth: 50, stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {name: "中学(女)", type: 'bar', barMaxWidth: 50, stack: '女生', data: [], textStyle: {color: "#fff"}},
                    {name: "中职中专(男)", type: 'bar', barMaxWidth: 50, stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {name: "中职中专(女)", type: 'bar', barMaxWidth: 50, stack: '女生', data: [], textStyle: {color: "#fff"}},
                    {name: "高职高专(男)", type: 'bar', barMaxWidth: 50, stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {
                        name: "高职高专(女)",
                        type: 'bar',
                        barMaxWidth: 50,
                        stack: '女生',

                        data: [],
                        textStyle: {color: "#fff"}
                    },
                    {name: "大学(男)", type: 'bar', stack: '男生', data: [], textStyle: {color: "#fff"}},
                    {
                        name: "大学(女)",
                        type: 'bar',
                        barMaxWidth: 50,
                        stack: '女生',

                        data: [],
                        textStyle: {color: "#fff"}
                    }],
                legend = [],
                xAxis = [];
        for (var i = 0, l = data1.length; i < l; i++) {
            xAxis.push(data1[i].ORG_NAME);

            series[0].data.push(data1[i].MALES1);
            series[1].data.push(data1[i].FEMALES1);
            series[2].data.push(data1[i].MALES2);
            series[3].data.push(data1[i].FEMALES2);
            series[4].data.push(data1[i].MALES3);
            series[5].data.push(data1[i].FEMALES3);

            series[6].data.push(data1[i].MALES4);
            series[7].data.push(data1[i].FEMALES4);
            series[8].data.push(data1[i].MALES5);
            series[9].data.push(data1[i].FEMALES5);
            series[10].data.push(data1[i].MALES6);
            series[11].data.push(data1[i].FEMALES6);

        }

        var option = {
            title: {
                subtext: '男生总数：' + data2[0].MALES + ' (人),女生总数：' + data2[0].FEMALES + ' (人)',
                top: 0,
                left: 15
            },
            tooltip: {
                trigger: 'axis',
                showDelay: 0, // 显示延迟，添加显示延迟可以避免频繁切换，单位ms},
                fontSize: 5,
                confine: true
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                right: 5,
                top: 20,
                bottom: 20,

                data: ['幼儿园(男)', '幼儿园(女)', '小学(男)', '小学(女)', '中学(男)', '中学(女)', '中职中专(男)', '中职中专(女)',
                    '高职高专(男)', '高职高专(女)', '大学(男)', '大学(女)'], textStyle: {color: '#fff'},


            },
            calculable: true, grid: {
                x: 60, y: 50, x2:
                        220, y2:
                        35
            },
            yAxis: [{type: 'value', axisLabel: {textStyle: {color: "#fff"}}}],
            xAxis: [{type: 'category', data: xAxis, axisLabel: {textStyle: {color: "#fff"}}}],
            series: series
        };

        if (myChart != null && myChart != "" && myChart != undefined) {
            myChart.dispose();
        }
        myChart = echarts.init(document.getElementById(id), 'macarons');
        myChart.setOption(option);


    }


    function showTeacherChart(data, id) {
        var data = data.dataList['count'];
        if (data.length == 0) {
            noData(id);
            return;
        }
        var xAxisArr = [], series = [
            {name: "教师总数", type: 'line', data: [], textStyle: {color: "#400cff"}},
            {name: "学生总数", type: 'line', data: [], textStyle: {color: "#ff2528"}}];
        for (var i = 0, l = data.length; i < l; i++) {
            xAxisArr.push(data[i].YEAR);
            if (data[i].TEACHERS != null) {
                series[0].data.push(data[i].TEACHERS);

            }
            if (data[i].STUDENTS != null) {
                series[1].data.push(data[i].STUDENTS);

            }

        }
        var option = {
            tooltip: {trigger: 'axis'},
            legend: {data: ['教师总数', '学生总数'], textStyle: {color: '#fff'}},
            calculable: true, grid: {x: 55, y: 30, x2: 10, y2: 30},
            yAxis: [{type: 'value', axisLabel: {textStyle: {color: "#fff"}}}],
            xAxis: [{type: 'category', data: xAxisArr, axisLabel: {textStyle: {color: "#fff"}}}],
            series: series
        };

        if (myChart != null && myChart != "" && myChart != undefined) {
            myChart.dispose();
        }
        myChart = echarts.init(document.getElementById(id), 'macarons');
        myChart.setOption(option);


    }

    function isNum(n) {
        return n ? n : 0;
    }

    var clickObj = {};

    function yxClick(o, n) {
        showLiDiv(o, n);

        clickObj[n] = 1;

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/school/findDataByChart.json?t=' + Math.random(),
            type: 'POST',
            data: {year: $('#year').val()},
            dataType: "json",
            error: function (data) {
                $.messager.alert('提示', '信息获取异常!', 'warning');

            },
            success: function (data) {
                switch (n) {
                    case 1:
                        showSchoolChart(data, "chart_rk1");
                        break;//学校总数统计图表
                    case 2:
                        showSexChart(data, "chart_rk2");
                        break;//男女生人数统计图表
                    case 3:
                        showTeacherChart(data, "chart_rk3");
                        break;//师生人数统计图表

                }
            }
        });

    }

    function showLiDiv(o, n) {
        chooseDiv = n;
        if(n==3)
        {
            $('#year').hide()
        }else
        {
            $('#year').show()
        }

        $(".key-con").hide();
        $("#chart_" + n).show();
        $("#ul_org li").removeClass("buildleft-list-dq");
        $(o).addClass("buildleft-list-dq");
    }
</script>
</html>
