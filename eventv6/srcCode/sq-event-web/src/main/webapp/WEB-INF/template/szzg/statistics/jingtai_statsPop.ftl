<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>人口年度分析</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    .image_hand{cursor:pointer;}
    .list_div{height:360px;}
    .buildleft{float:none;width:100%;min-height:30px;}
    .buildleft-list ul li{cursor:pointer;display:inline}
	.buildright{margin-left:10px;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:860px;">
        <div class="buildcon">
         
          <div class="buildright" id="buildright_1">
                <div class="gross-chart" id="chart_1_1" style="width:860px;height:420px;"></div>
           
          </div><!--end .buildright-->
        
          <div class="clearfloat"></div>
        </div><!--end .buildcon-->
      </div><!--end .bpsmain-->
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
 $(function(){
     find1();//
 });
 
 function formatNum(n){
	 return n==null||n.length==0?0:n;
 }
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
 var yearData = ['2010年','2011年','2012年','2013年','2014年','2015年','2016年']
 function find1(){
	var option = {tooltip : {trigger: 'axis'},title:{ text: '年度人口分析',textStyle:{color:'#fff'}},
	    legend: {data: ["年末常住人口","年末户籍人口","城镇化率"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true, //grid:{x:40,y:30,x2:5,y2:30},
	    yAxis : [{name : '人口(万人)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'城镇化率(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"年末常住人口",type:'bar',data:[159.54,161.64,162.43,163,163.47,163.68,164.15],itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"年末户籍人口",type:'bar',data:[120.71,120.88,121.26,121.73,123.21,124.1,124.8],itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
				 {name:"城镇化率",type:'line',data:[64.25,65.95,66.58,67.58,68.35,69.42,70],yAxisIndex:1}]
	};
		var myChart = echarts.init(document.getElementById("chart_1_1"));
		myChart.setOption(option);
		
	
 }

</script>
</html>
