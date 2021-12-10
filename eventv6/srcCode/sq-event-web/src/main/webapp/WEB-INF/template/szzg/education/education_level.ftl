<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>文化程度分析</title>

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css">

<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/echarts4.2.0.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>
  <style type="text/css">
    body{color:#fff;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">


        <div class="npAlarminfo citybgbox bpsmain" style="width:570px;margin-left: 3px">

                    <ul id="ul_org"  class="bpscon-tit" style="overflow: auto;width: 130px;height: 400px;">
                    <#list education as org>
                        <li onclick="rkClick(this,'${org.dictGeneralCode}')" title="${org.dictName}" >${org.dictName}</li>
                    </#list>
                    </ul>



                <div class="bpscon-box" style="margin-right:20px;width: 400px;padding-bottom:5px;">

                    <div class="bpscon-box-chart" style="    margin: 2px 2px 0 0px;width: 412px; margin-left: 3px;height: 200px;">
                        &nbsp;&nbsp;按区域统计Top5（单位：人）

                        <input type="text" class="inp1 Wdate timeClass" id="year"  value="${yearStr}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:rkYearChange});" >
                        <div class="bpscon-chart" style="padding:0px"><div id="chart_rk1" style="width:400px;height:175px"></div></div>

                        <div class="clearfloat"></div>

                    </div>
                </div>
                <div class="key-con" style="width: 395px;margin-top: 10px;margin-left: 12px;height: 170px;">
                    <div class="grosslist" style="width:100%;height:170px;overflow-y: auto;overflow-x: hidden;" id="grosslist">
                        <table width="100%" cellspacing="0" cellpadding="0" id="table" class="replist">
                            <thead>
                            <tr class="grosslist">
                                <th style=" border:1px solid #6d8ca4;">区域</th>
                                <th style=" border:1px solid #6d8ca4;">总人数(万人)</th>
                                <th style=" border:1px solid #6d8ca4;">男女比例</th>
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
    var type_education ;
    //添加滚动条样式
    $("#ul_org").mCustomScrollbar({theme: "minimal-dark"});
    $("#grosslist").mCustomScrollbar({theme: "minimal-dark"});

    $(function () {
        $("#ul_org li").first().click();
    });



    function rkYearChange(o,type){
        if(type == undefined){
            type = type;
        }

        var type =type_education;


        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/education/findChartByParams.json?t='+Math.random(),
            type: 'POST',
            data: { type : type,yearStr : o.cal.date.y,module :'education'},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                showPie(data.list,"chart_rk1");
                getTable(type,o.cal.date.y);
            }
        });
    }

    function getTable(type,year) {

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/education/getEducationCharts.json?t='+Math.random(),
            type: 'POST',
            data: {yearStr : year,type :type},
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
                    for(var i = 0;i < data.length; i++){ //遍历一下json数据
                        var trow = getDataRow(data[i]); //定义一个方法,返回tr数据
                        tbody.appendChild(trow);
                    }
            }
        });
    }
    function getDataRow(h){

        var row = document.createElement('tr'); //创建行

        var idCell = document.createElement('td'); //创建第一列id
        idCell.innerHTML = h.ORG_NAME; //填充数据
        row.appendChild(idCell); //加入行  ，下面类似

        var nameCell = document.createElement('td');//创建第二列name
        nameCell.innerHTML = h.TOTAL;
        row.appendChild(nameCell);

        var jobCell = document.createElement('td');//创建第三列job
        jobCell.innerHTML = h.PROP;
        row.appendChild(jobCell);


        return row; //返回tr数据
    }

    function rkClick(o,type){
        if(type == undefined){
            type = type;
        }
        $("#ul_org li").removeClass("bpscon-tit-dq");
        $(o).addClass("bpscon-tit-dq");

        var year = document.getElementById("year").value;
        type_education=type;

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/education/findChartByParams.json',
            type: 'POST',
            data: {yearStr : year,type :type,module :'education'},
            dataType:"json",
            error: function(data){
                $.messager.alert('提示','信息获取异常!','warning');

            },
            success: function(data){
                showPie(data.list,"chart_rk1");
                getTable(type,year);
            }
        });
    }

    function showPie(data,id){

        if(data.length == 0){
            noData(id);
            return;
        }
        var optionPie = {calculable:true,legend:{x:'right',orient: 'vertical',textStyle:{color:'#fff'},data:[],top:10},
			color:["#8AC58A","#E1E14C","#E56E51","#ffa800","#00cfff"],
            tooltip : { trigger: 'item',formatter:"{b}:{c}({d}%)"},   series : [{name:"",type:'pie', radius : ['30%', '50%'],center: ['45%','50%'],data:[],
                itemStyle:{normal:{label:{show:false},labelLine:{show:false}},
                    emhasis:{label:{show:true,position:'center',
                        textStyle:{fontSize:'30', fontWeight : 'bold'}}}}}]};
        var l = data.length;
        if(l>5){
        	l = 5;
        }
        for(var i=0;i<l;i++){
            optionPie.legend.data.push(data[i].ORG_NAME);
            optionPie.series[0].data.push({value:data[i].TOTAL_PEOPLE,name:data[i].ORG_NAME});
        }
        var myChart = echarts.init(document.getElementById(id));
        myChart.setOption(optionPie);
    }

    function isNum(n){
        return n?n:0;
    }
    


</script>
</html>
