<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>区域趋势</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
	.buildright{margin-left:10px;position:relative;}
	.buildcon{margin-top:0px;}
    </style>
</head>

<body class="npmap" onload="findYears()">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:930px;">
        <div class="buildcon">
          
		  <div class="buildright" id="div_1">
            <div  style="width:460px;float:left;">
                <div class="gross-chart" id="chart_1_1" style="width:460px;height:380px;"></div>
            </div><!--end .fisright-chart1-->
            <div  style="width:460px;float:left;">
               <div class="gross-chart" id="chart_1_2" style="width:460px;height:380px;"></div>
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
		  
		  <div class="buildright" id="div_2" style="display:none">
			<div style="width:60px;position:absolute;top:0px;z-index:100;">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="showLiDiv(1)" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
			</div>
            <div  style="width:460px;float:left;">
                <div class="gross-chart" id="chart_2_1" style="width:460px;height:380px;"></div>
            </div><!--end .fisright-chart1-->
            <div  style="width:460px;float:left;">
               <div class="gross-chart" id="chart_2_2" style="width:460px;height:380px;"></div>
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
		  
		  
          <div class="clearfloat"></div>
        </div><!--end .buildcon-->
      </div><!--end .bpsmain-->
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
var orgCode = '${orgCode}';
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478',3:'rgb(218,112,214)'};
 function findYears(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '公共财政预算收入累计',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{data:['收入'],show:false},toolbox:{show:true},grid:{x:50,y:30,x2:10,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}}],
				series : [
					{name:'收入',type:'bar',stack:'投资额',data:[],itemStyle:{normal:{label:{show:true,position:'insideRight'},color:(function(a,b,c){return colorObj[3];})()}},barWidth:30}]
				};
				
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '公共财政预算收入增幅(%)',x:'center',textStyle:{color:'#fff',fontSize:21}},
		legend:{data:['增幅'],show:false},
		toolbox:{show:true},grid:{x:40,y:30,x2:5,y2:50},calculable : false,
		xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'增幅',type:'bar',stack: '增幅',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,position:'insideRight'},color:(function(a,b,c){return colorObj[3];})()}}}]
		};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,table:'gpbr'},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_1_1");
				noData("chart_1_2");
				return ;
			}
			for(var i=data.list.length-1;i>=0;i--){
				var d = data.list[i];
				option.xAxis[0].data.push(d.YEAR_+'年');
				option1.xAxis[0].data.push(d.YEAR_+'年');
				
				option.series[0].data.push({value:isNum(d.GDP_SUM),year:d.YEAR_,column:'GPBR'});
				
				option1.series[0].data.push(isNum(d.GDP_INC));
			}
			var myChart = echarts.init(document.getElementById("chart_1_1"));
			myChart.setOption(option);
			myChart.on('click',function(param,a,b,c){
				findItem(param.data);
				showLiDiv(2);
			});
			var myChart1 = echarts.init(document.getElementById("chart_1_2"));
			myChart1.setOption(option1);
		}
	});
	
 }
 
 function findItem(param){
	var option = {title:{text: param.year+'年每月收入增量',x:'center',textStyle:{color:'#fff',fontSize:21}},tooltip:{trigger:'axis',axisPointer:{type:'shadow'},
				formatter:function(params){var tar;if(params[1].value!='-'){tar=params[1];}else{tar=params[0];}return tar.name+'<br/>'+tar.seriesName+':'+tar.value;}},
				legend:{data:['支出','收入'],x:'center',y:'bottom',textStyle:{color:'#fff'}},toolbox:{},xAxis:[{type:'category',splitLine:{show:false},data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis:[{type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}}],grid:{x:40,y:30,x2:10,y2:50},calculable : false,
				series:[
				{name:'总量',type:'bar',stack:'总量',itemStyle:{normal:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'},emphasis:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'}},data:[]},
				{name:'收入',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'top'}}},data:[]},
				{name:'支出',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'bottom'}}},data:[]}]};	
				
	var option1 = {tooltip:{trigger:'axis'},title:{ text: param.year+'年每月涨幅(%)',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{data:['涨幅'],x:'center',y:'bottom',textStyle:{color:'#fff'}},
				toolbox:{show:true},grid:{x:40,y:30,x2:5,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
				series : [
					{name:'涨幅',type:'bar',stack: '值',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,position:'insideRight'},color:(function(a,b,c){return colorObj[2];})()}}}]
				};	
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,year:param.year,table:'gpbr'},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_2_1");
				noData("chart_2_2");
				return ;
			}
			var lastSum = 0;
			for(var i=0,l=data.list.length;i<l;i++){
				var d = data.list[i],sum = isNum(d.GPBR_SUM),cha = sum-lastSum;
				option.xAxis[0].data.push(d.MONTH_+'月');
				option1.xAxis[0].data.push(d.MONTH_+'月');
				
				option1.series[0].data.push(isNum(d.GPBR_INC));
				if(cha >=0){
					option.series[0].data.push(lastSum);
					option.series[1].data.push(cha);
					option.series[2].data.push('-');
				}else{
					option.series[0].data.push(lastSum+cha);
					option.series[1].data.push('-');
					option.series[2].data.push(Math.abs(cha));
				}
				lastSum=sum;
			}
			var myChart = echarts.init(document.getElementById("chart_2_1"));
			myChart.setOption(option);
			var myChart1 = echarts.init(document.getElementById("chart_2_2"));
			myChart1.setOption(option1);
		}
	});
	
 }
 
 
function isNum(n){
	return n?n:0;
}
 var clickObj = {};

 function showLiDiv(n){
	$(".buildright").hide();
	$("#div_"+n).show(1000);
 }
</script>
</html>
