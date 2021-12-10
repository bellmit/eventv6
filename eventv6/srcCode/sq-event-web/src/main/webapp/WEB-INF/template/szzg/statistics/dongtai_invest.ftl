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
		  
		  <div class="buildright" id="div_3" style="display:none">
			<div style="width:60px;position:absolute;top:0px;z-index:100;">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="showLiDiv(2)" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
			</div>
            <div  style="width:460px;float:left;">
                <div class="gross-chart" id="chart_3_1" style="width:460px;height:380px;"></div>
            </div><!--end .fisright-chart1-->
            <div  style="width:460px;float:left;">
               <div class="gross-chart" id="chart_3_2" style="width:460px;height:380px;"></div>
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
	var option = {tooltip:{trigger:'axis'},title:{ text: '固定资产投资额累计(万元)',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{data:['投资额累计','一产投资额','二产投资额','三产投资额'],x:'center',y:'bottom',textStyle:{color:'#fff'}},
				toolbox:{show:true},grid:{x:50,y:30,x2:10,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}}],
				series : [
					{name:'投资额累计',type:'bar',stack:'投资额',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[3];})()}},barWidth:30},
					{name:'三产投资额',type:'bar',stack:'值',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
					{name:'二产投资额',type:'bar',stack:'值',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}},barWidth:30},
					{name:'一产投资额',type:'bar',stack:'值',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}},barWidth:30}]
				};
				
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '固定资产投资额涨幅(%)',x:'center',textStyle:{color:'#fff',fontSize:21}},
		legend:{data:['投资额累计涨幅','一产涨幅','二产涨幅','三产涨幅'],x:'center',y:'bottom',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:30,x2:5,y2:50},calculable : false,
		xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'投资额累计涨幅',type:'bar',stack: '投资额',data:[],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[3];})()}}},
			{name:'三产涨幅',type:'bar',stack: '值',data:[],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}},
			{name:'二产涨幅',type:'bar',stack: '值',data:[],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}},
			{name:'一产涨幅',type:'bar',stack: '值',data:[],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}}}]
		};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,table:'invest'},
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
				
				option.series[0].data.push({value:isNum(d.GDP_SUM),year:d.YEAR_,column:'GDP',colName:'累计'});
				option.series[1].data.push({value:isNum(d.THREE_SUM),year:d.YEAR_,column:'THREE',colName:'第三产业'});
				option.series[2].data.push({value:isNum(d.TWO_SUM),year:d.YEAR_,column:'TWO',colName:'第二产业'});
				option.series[3].data.push({value:isNum(d.ONE_SUM),year:d.YEAR_,column:'ONE',colName:'第一产业'});
				
				option1.series[0].data.push(isNum(d.GDP_INC));
				option1.series[1].data.push(isNum(d.THREE_INC));
				option1.series[2].data.push(isNum(d.TWO_INC));
				option1.series[3].data.push(isNum(d.ONE_INC));
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
	var option = {tooltip:{trigger:'axis'},title:{ text: param.year+'年'+param.colName+'投资额(万元)',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{show:false,data:['总值']},toolbox:{show:true},grid:{x:50,y:30,x2:10,y2:30},calculable : false,
				xAxis : [{type:'category',data:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}}],
				series : [
					{name:'总值',type:'line',stack:'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,textStyle:{color:'#fff'}}}}}]
				};
				
	var option1 = {tooltip:{trigger:'axis'},title:{ text: param.year+'年'+param.colName+'投资额涨幅(%)',x:'center',textStyle:{color:'#fff',fontSize:21}},
		legend:{show:false,data:['增幅']},toolbox:{show:true},grid:{x:40,y:30,x2:5,y2:30},calculable : false,
		xAxis : [{type:'category',data:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'增幅',type:'line',stack: 'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,textStyle:{color:'#fff'}}}}}]
		};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,year:param.year,table:'invest'},
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
			var monthSUM = {1:0,2:0,3:0,4:0,5:0,6:0,7:0,8:0,9:0,10:0,11:0,12:0},monthINC = {1:0,2:0,3:0,4:0,5:0,6:0,7:0,8:0,9:0,10:0,11:0,12:0},
			month=[1,2,3,4,5,6,7,8,9,10,11,12];
			for(var i=0,l=data.list.length;i<l;i++){
				monthSUM[data.list[i].MONTH_] = isNum(data.list[i][param.column+'_SUM']);
				monthINC[data.list[i].MONTH_] = isNum(data.list[i][param.column+'_INC']);
			}
			for(var i=0,l=month.length;i<l;i++){
				option.series[0].data.push({value:monthSUM[month[i]],month:month[i],column:param.column});
				option1.series[0].data.push({value:monthINC[month[i]]});
			}
			var myChart = echarts.init(document.getElementById("chart_2_1"));
			myChart.setOption(option);
			myChart.on('click',function(param){
				findYearJiDu(param.data);
				showLiDiv(3);
			});
			var myChart1 = echarts.init(document.getElementById("chart_2_2"));
			myChart1.setOption(option1);
		}
	});
	
 }
 
 function findYearJiDu(param){
	var jidu = {1:'1月',2:'2月',3:'3月',4:'4月',5:'5月',6:'6月',7:'7月',8:'8月',9:'9月',10:'10月',11:'11月',12:'12月'};
	var option = {tooltip:{trigger:'axis'},title:{ text:'近年'+jidu[param.month]+'投资额趋势(万元)',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{show:false,data:['投资额']},toolbox:{show:true},grid:{x:50,y:30,x2:10,y2:30},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}}],
				series : [
					{name:'投资额',type:'bar',stack:'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,position:'insideRight'}}}}]
				};
				
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '近年'+jidu[param.month]+'投资额涨幅趋势(%)',x:'center',textStyle:{color:'#fff',fontSize:21}},
		legend:{show:false,data:['涨幅']},toolbox:{show:true},grid:{x:40,y:30,x2:5,y2:30},calculable : false,
		xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'涨幅',type:'bar',stack: 'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true,position:'insideRight'}}}}]
		};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,month:param.month,table:'invest'},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			 
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_3_1");
				noData("chart_3_2");
				return ;
			}
			for(var i=0,l=data.list.length;i<l;i++){
				var d = data.list[i];
				option.xAxis[0].data.push(d.YEAR_+'年');
				option1.xAxis[0].data.push(d.YEAR_+'年');
				
				option.series[0].data.push(d[param.column+'_SUM']);
				option1.series[0].data.push(d[param.column+'_INC']);
			}
			var myChart = echarts.init(document.getElementById("chart_3_1"));
			myChart.setOption(option);
			var myChart1 = echarts.init(document.getElementById("chart_3_2"));
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
