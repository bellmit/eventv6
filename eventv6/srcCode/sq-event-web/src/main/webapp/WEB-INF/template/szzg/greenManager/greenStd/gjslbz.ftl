<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>城市森林标准</title>


    <style >
        .abs-right_name{ width: 100px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}
        .abs-right_content{ width: 200px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;}

    </style>

<#include "/szzg/common.ftl" />

</head>

<body class="npmap">
<div class="npcityMainer">
    <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">


        <div class="npAlarminfo citybgbox bpsmain" style="width:660px;height: 340px; margin-left: 7px;">
                <div class="bpscon-tit" style="border: hidden"  id="ul_div" >
                    <ul id='ul_org'style="border: none">
                    <#list type as org>
                        <li onclick="rkClick(this,'${org.dictCode}')" title="${org.dictCode}" >${org.dictName}</li>
                    </#list>
                    </ul>
                </div>

                <div class="key-con" style=" width: 487px;margin-left: 10px;height: 311px;">

                    <div class="grosslist" >
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" id="table" class="replist">
                            <thead>
                            <tr class="grosslist" >
                                <th style=" border:1px solid #6d8ca4;">年份</th>
                                <th style=" border:1px solid #6d8ca4;">指标名称</th>
                                <th style=" border:1px solid #6d8ca4;">指标说明</th>
                                <th style=" border:1px solid #6d8ca4;">指标标准</th>
                                <th style=" border:1px solid #6d8ca4;">我市标准</th>
                            </tr>
                            </thead>
                            <tbody id="tbMain" style="color: white;"></tbody>
                        </table>
                    </div>

                <div class="clearfloat"></div>
            </div><!--end .bpscon-->
        </div><!--end .bpsmain-->
    </div>
</div>
</body>

<script type="text/javascript">
    //添加滚动条样式
    $("#table_flow").mCustomScrollbar({theme: "minimal-dark"});

    $(function () {
        $("#ul_org li").first().click();
    });


    function rkClick(o,type){
        if(type == undefined){
            type = type;
        }
        $("#ul_org li").removeClass("bpscon-tit-dq");
        $(o).addClass("bpscon-tit-dq");



        $.ajax({
            url: '${rc.getContextPath()}/zhsq/szzg/greenstd/searchData.json?t='+Math.random(),
            type: 'POST',
            data: {type :type},
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
        var yearCell = document.createElement('td'); //创建第一列id
        yearCell.innerHTML = h.SYEAR; //填充数据
        row.appendChild(yearCell); //加入行  ，下面类似

        var nameCell = document.createElement('td'); //创建第一列id
        nameCell.innerHTML = "<div class='abs-right_name' style='margin-left: 10px'><a href='#' title="+h.NAME+">"+h.NAME+"</a></div>"; //填充数据
        row.appendChild(nameCell); //加入行  ，下面类似

        var contentCell = document.createElement('td');//创建第二列name
        if(h.CONTENT!=null)
        {
            contentCell.innerHTML = "<div class='abs-right_content' style='margin-left: 10px'><a href='#' title="+h.CONTENT+">"+h.CONTENT+"</a></div>";
        }
       else {
            contentCell.innerHTML = "";
        }
;
        row.appendChild(contentCell);

        var stdCell = document.createElement('td');//创建第二列name
        stdCell.innerHTML = h.STDVAL;
        row.appendChild(stdCell);

        var actCell = document.createElement('td');//创建第二列name
        actCell.innerHTML = h.ACTVAL;
        row.appendChild(actCell);

        return row; //返回tr数据
    }


    function showPie(data,id){

        if(data.length == 0){
            noData(id);
            return;
        }
        var optionPie = {calculable:true,legend:{orient : 'vertical', x : 'right',textStyle:{color:'#fff'},data:[]},
            tooltip : { trigger: 'item',formatter:"{b}:{c}({d}%)"},   series : [{name:"",type:'pie', radius : ['30%', '50%'],data:[],
                itemStyle:{normal:{label:{show:false},labelLine:{show:false}},
                    emhasis:{label:{show:true,position:'center',
                        textStyle:{fontSize:'30', fontWeight : 'bold'}}}}}]};
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


    function searchData() {
        var a = new Array();


        if ($("#location").val() != null & $("#location").val() != "") {
            a["location"] = $.trim($("#location").val());
        }

        if ($("#gType").val() != null & $("#gType").val() != "") {
            a["gType"] = $.trim($("#gType").val());
        }

        searchDataList(a);
    }



    function resetCondition() {


        $('#gType').val("");
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
