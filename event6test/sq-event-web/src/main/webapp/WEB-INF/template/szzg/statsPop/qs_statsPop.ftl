<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>趋势</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    </style>
</head>

<body class="npmap">

<div class="npcityMainer">
  <div class="npcityBottom"  style="bottom:0;position: relative;left: 0px;">
      <div class="npAlarminfo citybgbox bpsmain" >
        <div class="bpscon" >
         <div style="float: left;">
        	<div id="ul_org_div" style="width:135px;height:402px;">
          <ul class="bpscon-tit key-tit" id='ul_org'>
          <li onclick="rkClick(this,'${orgcode}')" title="${orgcode}"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon15.png" width="13" height="17"></i>${orgname}</li>
           <#list orgList as org>
            <li onclick="rkClick(this,'${org.orgCode}')" title="${org.orgCode}"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon15.png" width="13" height="17"></i>${org.orgName}</li>
           </#list>
          </ul>
           </div>
          </div>
          <div class="bpscon-box" id="div_0">
            <div class="key-chart" style="padding-top:0px;">
              <div class="key-left" style="width:460px">
                <div class="key-con">
                  <div class="dpchart"><div id="chart_rk1" style="width:430px;height:180px"></div></div>
                </div>
                <div class="key-con" style="margin-top:3px;">
                  	<div class="dpchart"><div id="chart_rk2" style="width:430px;height:180px"></div></div>
                </div>
              </div>
              <div class="key-right" style="margin:0px 10px 0 456px;width:271px;">
                <div class="agechart"><div id="chart_rk3" style="width:275px;height:395px"></div></div>
              </div>
              <div class="clearfloat"></div>
            </div>
          </div>
      
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
 $(function(){
     $("#ul_org li").first().click();
     $("#ul_org_div").mCustomScrollbar({theme: "minimal-dark"});
 });
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
function rkClick(o,orgCode){
	 $(".bpscon-box").hide();
	 $("#div_0").show();
	$("#ul_org li").removeClass("bpscon-tit-dq");
	$(o).addClass("bpscon-tit-dq");
	index = layer.load(0, {time: 5000});
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: { orgCode : orgCode,module :'qs'},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){
        	showAge(data.age);
        	showYear(data.rate,"chart_rk2");
        	showYear2(data.frate,"chart_rk1");
        	layer.close(index); 
        }
    });
}

function showYear(data,id){
	if(data.length == 0){
		noData(id);
		return;
	}
	var xAxisArr=[],series=[{name:"人口增长率",type:'line',data:[],textStyle:{color:"#fff"}},
	                        {name:"出生率",type:'line',data:[],textStyle:{color:"#fff"}},
	                        {name:"死亡率",type:'line',data:[],textStyle:{color:"#fff"}}	                        ];
	for(var i=0,l=data.length;i<l;i++){
		xAxisArr.push(data[i].SYEAR);
		series[0].data.push({value:data[i].GROWTH_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}});
		series[1].data.push({value:data[i].BIRTHS_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}});
		series[2].data.push({value:data[i].DEATHS_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}}});
	}
	var option = {tooltip : {trigger: 'axis'},
		    legend: {data: ['人口增长率','出生率','死亡率'],textStyle:{color:'#fff'}},
		    calculable : true, grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'value' ,data : xAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    xAxis : [{type : 'category',data : xAxisArr ,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : series
		};
	var myChart = echarts.init(document.getElementById(id));
	myChart.setOption(option);
}

function showYear2(data,id){
	if(data.length == 0){
		noData(id);
		return;
	}
	var xAxisArr=[],series=[{name:"总负担系数",type:'line',data:[],textStyle:{color:"#fff"}},
	                        {name:"负担老人系数",type:'line',data:[],textStyle:{color:"#fff"}},
	                        {name:"负担少年系数",type:'line',data:[],textStyle:{color:"#fff"}}	                        ];
	for(var i=0,l=data.length;i<l;i++){
		xAxisArr.push(data[i].SYEAR);
		series[0].data.push({value:data[i].BURDEN_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}});
		series[1].data.push({value:data[i].ELDERLY_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}});
		series[2].data.push({value:data[i].JUVENILE_NUM,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}});
	}
	var option = {tooltip : {trigger: 'axis'},
		    legend: {data: ['总负担系数','负担老人系数','负担少年系数'],textStyle:{color:'#fff'}},
		    calculable : true, grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'value' ,data : xAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    xAxis : [{type : 'category',data : xAxisArr ,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : series
		};
	var myChart = echarts.init(document.getElementById(id));
	myChart.setOption(option);
}

function showAge(data){
	if(data.length == 0){
		noData("chart_rk3");
		return;
	}
	
	var series=[],yAxisArr=[];
	for(var i=0,l=data.length;i<l;i++){
		var d = data[i];
		yAxisArr.push(d.stypeStr);
		series.push({value:d.snum});
	}
	var option = {tooltip : {trigger: 'axis'},
		    legend: {data: ['人数'],textStyle:{color:'#fff'}},
		    calculable : true,
		    xAxis : [{type : 'value',axisLabel:{textStyle:{color:"#fff"}}}],
		    grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'category',data : yAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : [ 
		        {name:"人数",type:'bar',data:series,textStyle:{color:"#fff"}}]
		};
	var myChart = echarts.init(document.getElementById("chart_rk3"));
	myChart.setOption(option);
}
function showPie(data,type){
	if(data.length == 0){
    	noData(type.id);
    	return;
    }
	var optionPie = {title:type.title,calculable:true,legend:{orient : 'vertical', x : 'right',textStyle:{color:'#fff'},data:[]},
		   tooltip : { trigger: 'item',formatter:"{b}:{c}({d}%)"},   series : [{name:"",type:'pie', radius : ['40%', '60%'],data:[], 
			   itemStyle:{normal:{label:{show:false},labelLine:{show:false}},
				   emhasis:{label:{show:true,position:'center',
					   textStyle:{fontSize:'30', fontWeight : 'bold'}}}}}]};
	for(var i=0,l=data.length;i<l;i++){
		optionPie.legend.data.push(data[i][type.name]);
		optionPie.series[0].data.push({value:data[i][type.snum], name:data[i][type.name]});
	}
	var myChart = echarts.init(document.getElementById(type.id));
	myChart.setOption(optionPie);
}
function isNum(n){
	return n?n:0;
}
</script>
</html>
