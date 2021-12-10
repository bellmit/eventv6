<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
    <title>绿化指标与绿化趋势</title>



</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">


        <div class="npAlarminfo citybgbox bpsmain" style="width:330px; height: 460px">



                    <div class="bpscon-box-chart" style="    margin: 2px 2px 0 0px;width: 300px; margin-left: 10px;height: 200px;">
                        按区域统计

                        <input type="text" class="inp1 Wdate timeClass" id="year"  value="${yearStr}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:rkYearChange});" >

                        <div class="bpscon-chart" style="padding:0px"><div id="chart_rk1" style="width:300px;height:175px"></div></div>

                        <div class="clearfloat"></div>

                    </div>

                <div class="key-con" style=" width: 280px;margin-top: 10px;margin-left: 10px;height: 220px;">

                    <div class="grosslist" id="table_flow" style="max-height:100%;width: 105%;overflow:auto;">
                        <table width="96%"  border="0" cellspacing="0" cellpadding="0" id="table" class="replist" style=" border:1px solid #6d8ca4;">
                            <thead>
                            <tr class="grosslist" style=" border:1px solid #6d8ca4;">
                                <th style=" border:1px solid #6d8ca4;">名称</th>
                                <th style=" border:1px solid #6d8ca4;">指标</th>

                            </tr>
                            </thead>
                            <tbody id="tbMain" style="color: white"></tbody>
                        </table>
                    </div>

                <div class="clearfloat"></div>
            </div><!--end .bpscon-->
        </div><!--end .bpsmain-->
    </div>
    <div class="npAlarminfo citybgbox bpsmain"  style="width:500px; height: 460px;margin-left: 370px;">
        <div class="key-left" style="width:460px">
            <div class="key-con">
                <div class="dpchart"><div id="chart_rk2" style="width:480px;height:200px"></div></div>
            </div>
            <div class="key-con" style="margin-top:3px;">
                <div class="dpchart"><div id="chart_rk3" style="width:480px;height:200px"></div></div>
            </div>
        </div>
    </div><!--end .bpsmain-->
</div>
</div>
</body>

<script type="text/javascript">
    var type_education ;

    var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
    //添加滚动条样式
    $("#table_flow").mCustomScrollbar({theme: "minimal-dark"});


    $(function () {



        data(${yearStr},${orgCode});
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/find_statsGreenByYear.json?t='+Math.random(),
            type: 'POST',
            data: { syear : $('#year').val(),orgCode:${orgCode}},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                showPie(data.dataList,"chart_rk1");
                getTable($('#year').val(),${orgCode});
                searchQs()
            }
        });

    });


    function  data(year,orgCode) {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/searchData.json?t='+Math.random(),
            type: 'POST',
            data:{syear:year,orgCode:orgCode},
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                var point= new Array(data.length);;
                for(var i=0;i<data.length;i++)
                {
                    var  p=new Object();
                    p.name=data[i].seqid;
                    p.x=data[i].x;
                    p.y=data[i].y;
                    point[i]=p;
                }

                var mapLocationObject = {
                    locationMaplist :point
                };
                window.parent.locationPointsOnMap(mapLocationObject);

            }
        });

    }



    function rkYearChange(o,type){
        if(type == undefined){
            type = type;
        }
        data(o.cal.date.y,${orgCode})



        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/find_statsGreenByYear.json?t='+Math.random(),
            type: 'POST',
            data: { syear : o.cal.date.y,orgCode:${orgCode}},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                showPie(data.dataList,"chart_rk1");
                getTable(o.cal.date.y);
            }
        });
    }

    function searchQs() {
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/findDataByQs.json?t='+Math.random(),
            type: 'POST',
            data: {orgCode:${orgCode}},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                showQs(data.dataList['qs_1'],"chart_rk2");
                showQs_2(data.dataList['qs_2'],"chart_rk3")
            }
        });
    }

    function showQs(data,id){
        if(data.length == 0){
            noData(id);
            return;
        }
        var xAxisArr=[],series=[{name:"城市绿化面积",type:'line',data:[],textStyle:{color:"#fff"}},
            {name:"绿化覆盖面积",type:'line',data:[],textStyle:{color:"#fff"}},
            {name:"公园绿地面积",type:'line',data:[],textStyle:{color:"#fff"}}	                        ];
        for(var i=0,l=data.length;i<l;i++){
            xAxisArr.push(data[i].syear);
            series[0].data.push(data[i].gArea);
            series[1].data.push(data[i].gCoverarea);
            series[2].data.push(data[i].gParkarea);
        }
        var option = {tooltip : {trigger: 'axis'},
            legend: {data: ['城市绿化面积','绿化覆盖面积','公园绿地面积'],textStyle:{color:'#fff'}},
            calculable : true, grid:{x:45,y:30,x2:10,y2:30},
            yAxis : [{name : '单位(公顷)',type : 'value',axisLine:{lineStyle:{color: '#fff'}} ,data : xAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
            xAxis : [{type : 'category',data : xAxisArr ,axisLabel:{textStyle:{color:"#fff"}}}],
            series : series
        };
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(option);
    }

    function showQs_2(data,id){
        if(data.length == 0){
            noData(id);
            return;
        }

        var series=[{name:"城市绿化率",type:'line',data:[],yAxisIndex:0},
                    {name:"绿化覆盖率",type:'line',data:[],yAxisIndex:0},
                    {name:"森林覆盖率",type:'line',data:[],yAxisIndex:0}],
                xAxis = [];
        for(var i=0,l=data.length;i<l;i++){
            xAxis.push(data[i].syear);
            series[0].data.push(data[i].gRate);
            series[1].data.push(data[i].gCoverrate);
            series[2].data.push(data[i].forestCoverarea);

        }
        var option = {tooltip : {trigger: 'axis'},
            legend: {data: ["城市绿化率","绿化覆盖率","森林覆盖率"],textStyle:{color:'#fff'}},
            calculable : true, grid:{x:30,y:40,x2:44,y2:20},
            yAxis : [{name : '单位(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
            xAxis : [{type : 'category',data:xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
            series : series
        };
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(option);
    }


    function getTable(year) {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenindicators/find_statsGreenByYear.json?t='+Math.random(),
            type: 'POST',
            data: {syear : year,orgCode:${orgCode}},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');
            },
            success: function(data){

                    var tbody = document.getElementById('tbMain');


                    var tab = document.getElementById("table") ;
                    //表格行数
                    var rows = tab.rows.length;

                    if(rows>=2)
                    {
                        for(var i=1;i<rows;rows--)
                        {
                            document.getElementById('table').deleteRow(i);
                        }

                    }

                    for(var i = 0;i < data.dataList.length; i++){ //遍历一下json数据
                        var trow = getDataRow(data.dataList[i]); //定义一个方法,返回tr数据
                        tbody.appendChild(trow);
                    }




            }
        });
    }
    function getDataRow(h){

        var row = document.createElement('tr'); //创建行

        var idCell = document.createElement('td'); //创建第一列id
        idCell.innerHTML ="<div style='font-size: 12px'>"+h.name+"</div>"; //填充数据
        row.appendChild(idCell); //加入行  ，下面类似

        var nameCell = document.createElement('td');//创建第二列name
        nameCell.innerHTML = h.value;
        row.appendChild(nameCell);



        return row; //返回tr数据
    }


    function showPie(data,id){

        if(data.length == 0){
            noData(id);
            return;
        }
        var optionPie = {calculable:true,legend:{orient : 'vertical', x : 'left',textStyle:{color:'#fff'},data:[]},
            tooltip : { trigger: 'item',formatter:"{b}:{c}({d}%)"},  series : [{name:"",type:'pie', radius : ['30%', '50%'],  center: ['75%', '50%'],data:[],

                itemStyle:{normal:{label:{show:false},labelLine:{show:false}},
                    emhasis:{label:{show:true,position:'center',
                        textStyle:{fontSize:'20', fontWeight : 'bold'}}}}}]};
        for(var i=0,l=4;i<l;i++){
            optionPie.legend.data.push(data[i].name);
            optionPie.series[0].data.push({value:data[i].value,name:data[i].name});
        }
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(optionPie);
    }

    function isNum(n){
        return n?n:0;
    }

    function noData(id){
        var div = document.getElementById(id);
        var w = div.style.width,h = div.style.height;
        if(w.length == 0 || h.length == 0){
            w = "100px",h = "120px";
        }
        div.innerHTML = "<div style='width:"+w+";height:"+h+";vertical-align: middle;text-align:center;display: table-cell;'><img src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
    }

</script>
</html>
