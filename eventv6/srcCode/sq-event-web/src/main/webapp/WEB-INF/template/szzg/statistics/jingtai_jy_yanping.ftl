<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>经济运行</title>
<#include "/szzg/common.ftl" />
  <style type="text/css">
  body{color:#fff;}
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    .image_hand{cursor:pointer;}
    .list_div{height:360px;}
    .buildleft{float:none;width:918px;min-height:38px;margin-left:10px;}
    .buildleft-list ul li{cursor:pointer;display:inline;margin:0px;}
	.buildright{margin-left:10px;}
	.fisright-chart2{margin-left:0px;padding:0px;min-height:350px;padding-left:50px;}
	.buildcon{margin-top:0px;}
	.grosslist{width:800px;height:350px;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:930px;">
        <div class="buildcon">
          <div class="buildleft">
            <div class="buildleft-list">
              <ul id="ul_org" style="margin-top:10px;">
                <li onclick="yxClick(this,1)">GDP</li>
                <li onclick="yxClick(this,6)">城乡居民收支</li>
                <li onclick="yxClick(this,3)">外贸外资</li>
                <li onclick="yxClick(this,2)">房地产</li>
                <li onclick="yxClick(this,4)">金融存贷款</li>
                <li onclick="yxClick(this,5)">全社会工业用电量</li>
                <li onclick="yxClick(this,7)">财政收入</li>
                <li onclick="yxClick(this,8)">零售总额/固定资产投资</li>
              </ul>
            </div>
          </div>
			<!-- GDP -->
          <div class="buildright" id="buildright_1">
            <div  style="width:460px;float:left;">
                <div class="gross-chart" id="chart_1_1" style="width:460px;height:350px;"></div>
            </div><!--end .fisright-chart1-->
            <div  style="width:460px;float:left;">
               <div class="gross-chart" id="chart_1_2" style="width:460px;height:350px;"></div>
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
           <!-- 房地产 -->
          <div class="buildright" id="buildright_2">
				<div  style="width:460px;float:left;">
					<div class="gross-chart" id="chart_2_1" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart1-->
				<div  style="width:460px;float:left;">
				   <div class="gross-chart" id="chart_2_2" style="width:460px;height:350px;"></div>
				</div>
          </div><!--end .buildright-->
          
		   <!-- 外贸外资 -->
          <div class="buildright" id="buildright_3">
                <div  style="width:460px;float:left;">
					<div class="gross-chart" id="chart_3_1" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart1-->
				<div  style="width:460px;float:left;">
				   <div class="gross-chart" id="chart_3_2" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 金融存贷款 -->
          <div class="buildright" id="buildright_4">
				<div  style="width:460px;float:left;">
					<div class="gross-chart" id="chart_4_1" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart1-->
				<div  style="width:460px;float:left;">
				   <div class="gross-chart" id="chart_4_2" style="width:460px;height:350px;"></div>
				</div>
          </div><!--end .buildright-->
          
		  
		   <!--全社会工业用电量  -->
          <div class="buildright" id="buildright_5">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_5"  >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
           
		   <!-- 城乡居民收支 -->
          <div class="buildright" id="buildright_6">
	            <div  style="width:460px;float:left;">
					<div class="gross-chart" id="chart_6_1" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart1-->
				<div  style="width:460px;float:left;">
				   <div class="gross-chart" id="chart_6_2" style="width:460px;height:350px;"></div>
				</div>
          </div><!--end .buildright-->
          
		   <!--财政收入 -->
          <div class="buildright" id="buildright_7">
            <div class="fisright-chart2" style="padding-left:5px;">
	                <div class="grosslist" id="grosslist_7" style="width:900px;" >
	                </div>
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
		  
		    <!-- 零售总额/固定资产投资 -->
         <div class="buildright" id="buildright_8">
           <div  style="width:460px;float:left;">
					<div class="gross-chart" id="chart_8_1" style="width:460px;height:350px;"></div>
				</div><!--end .fisright-chart1-->
				<div  style="width:460px;float:left;">
				   <div class="gross-chart" id="chart_8_2" style="width:460px;height:350px;"></div>
				</div>
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
     $("#ul_org li").eq(0).click();//
 });
 //零售总额/固定资产投资
function find8(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '2017年分月累计社会消费品零售总额(万元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['总量','增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:45,y:50,x2:30,y2:50},	calculable : true,
		xAxis : [{type:'category',data:['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'总量(万元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'总量',type:'bar',yAxisIndex:0,data:[236696,321991,398358,504465,626384,742882,866382,980852,1111466,1256160,1424480 	  ],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[0];})()}}},
			{name:"增长",type:'line',yAxisIndex:1,data:[6.7,8.4,7.3,7.8,11.4,10.9,10.5 ,9.4	,8.6,7.8,8.8 ],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[1];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_8_1"));
	myChart.setOption(option);
	
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '2017年分月累计固定资产投资',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['总量','增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:45,y:50,x2:30,y2:50},	calculable : true,
		xAxis : [{type:'category',data:['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'总量',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'总量',type:'bar',yAxisIndex:0,data:[92808,207180,309106,404803,526028,707896,959284,1130753,1400816,1585395,1825318],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[2];})()}}},
			{name:"增长",type:'line',yAxisIndex:1,data:[33.6,23.1,26.4,47.6,'17.0','17.0',17.8,16.8,22.6,19.3 ,19.8],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[3];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_8_2"));
	myChart.setOption(option1);
}  

//财政收入
function find7(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '2017年分月累计公共财政总收入和地方公共财政收入(万元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['一般公共预算总收入','地方公共预算收入','总收入增长','预算收入增长'],x:'center',y:'bottom',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:50},
		calculable : true,
		xAxis : [{type:'category',data:['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'收入(万元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'一般公共预算总收入',type:'bar',yAxisIndex:0,data:[20036,24468,34309,44819,57139,65818,73997,80801,91083,102614,112266],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[0];})()}}},
			{name:'地方公共预算收入',type:'bar',yAxisIndex:0,data:[11581,14404,20741,27245,36593,41839,47760,52348,58481,64839,71919],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[1];})()}}},
			{name:'总收入增长',type:'line',yAxisIndex:1,data:[58.9,'39.0' ,'33.0',37.8 ,31.1 ,21.5 ,20.4,19.9,16.5,14.8,'13.0'],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[2];})()}}},
			{name:"预算收入增长",type:'line',yAxisIndex:1,data:[34.4 ,17.9,14.3 ,21.7 ,17.2,12.1 ,13.1,13.2, 10.7,'5.0','8.0'],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[3];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("grosslist_7"));
	myChart.setOption(option);
	
}   


//全社会工业用电量
function find5(){
var option = {title:{text: '2017年分月累计全社会工业用电量(万千瓦时)',x:'center',textStyle:{color:'#fff',fontSize:21}},tooltip:{trigger:'axis',axisPointer:{type:'shadow'},
	formatter:function(params){	
	return params[0].name+'<br/>'+params[1].seriesName+':'+(params[0].value+params[1].value)+'<br/>'+params[0].seriesName+':'+params[0].value;}},
				legend:{data:['用电量增量','增长'],x:'center',y:'bottom',textStyle:{color:'#fff'}},toolbox:{},
				xAxis:[{type:'category',splitLine:{show:false},data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis:[{type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:50,textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		grid:{x:40,y:30,x2:30,y2:50},calculable : false,
				series:[
				{name:'总量',type:'bar',stack:'总量',yAxisIndex:0,itemStyle:{normal:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'},emphasis:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'}},data:[]},
				{name:'用电量增量',type:'bar',stack:'总量',yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop'}}},data:[]},
				{name:'增长',type:'line',yAxisIndex:1,itemStyle:{normal:{label:{show:true}}},data:[]}]};	
	var list = [52131 ,75374 ,102258 ,127532 ,154563 ,183178 ,211264 ,239248 ,265690 ,297760 ,320627 ];
	option=zengliang(list,option);
	option.xAxis[0].data=['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
	option.series[2].data=[20.5 ,20.6 ,21.1 ,19.5 ,17.1 ,15.9 ,14.4 ,13.6,12.7,13.4 ,11.5 ];
	
	var myChart = echarts.init(document.getElementById("grosslist_5"));
	myChart.setOption(option);
} 
// 金融存贷款
function find4(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '存款余额(亿元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['总量','增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:['2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'总量(亿元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'总量',type:'bar',stack:'值',yAxisIndex:0,data:[453.85 ,501.79 ,504.69 ,507.53 ,514.52 ,521.01 ,524.01 ,519.22,511.41,505.47 ,'503.40' ],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
			{name:"增长",type:'line',yAxisIndex:1,data:[10.22 ,25.05 ,19.47 ,18.95 ,18.54 ,20.03 ,'9.70' ,'5.00','10.10',16.65 ,'12.90' ],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_4_1"));
	myChart.setOption(option);
	
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '贷款余额(亿元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['总量','增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:['2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'总量(亿元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'总量',type:'bar',stack:'值',yAxisIndex:0,data:[454.95 ,469.52 ,472.38 ,471.83 ,477.01 ,480.83 ,480.39 ,485.87 ,489.23,496.61 ,493.52 ],itemStyle:{normal:{label:{show:true,position:'insideTop'},color:(function(a,b,c){return colorObj[2];})()}},barWidth:30},
			{name:"增长",type:'line',yAxisIndex:1,data:[8.19 ,'3.90' ,5.73 ,7.07 ,'8.40' ,9.27 ,12.74 ,13.69,14.61 ,17.52 ,14.69 ],itemStyle:{normal:{color:(function(a,b,c){return colorObj[3];})()}}}]
		};
	var myChart1 = echarts.init(document.getElementById("chart_4_2"));
	myChart1.setOption(option1);
}  

//房地产
function find2(){
	var option = {title:{text: '2017年分月累计房屋新开工面积',x:'center',textStyle:{color:'#fff',fontSize:21}},tooltip:{trigger:'axis',axisPointer:{type:'shadow'},
				//formatter:function(params){	return params[0].name+'<br/>'+params[1].seriesName+':'+(params[0].value+params[1].value)+'<br/>'+params[0].seriesName+':'+params[0].value;}
				},
				legend:{data:['新开工面积'],x:'center',y:'bottom',textStyle:{color:'#fff'}},toolbox:{},xAxis:[{type:'category',splitLine:{show:false},data:['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis:[{type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:80,textStyle:{color:"#fff"}}}],grid:{x:20,y:30,x2:0,y2:50},calculable : false,
				series:[
				//{name:'总面积',type:'bar',stack:'总量',itemStyle:{normal:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'},emphasis:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'}},data:[]},
				{name:'新开工面积',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideTop'}}},data:[30818,30818,208900,208900,208900,251556,288898,341060,341060,341060,682172 ]}]};	
	
	
	var list = [30818,30818,208900,208900,208900,251556,288898,341060,341060,341060,682172 ];
	//option=zengliang(list,option);
	
	var option1 = {title:{text: '2017年分月累计房屋竣工面积',x:'center',textStyle:{color:'#fff',fontSize:21}},tooltip:{trigger:'axis',axisPointer:{type:'shadow'},
				//formatter:function(params){	return params[0].name+'<br/>'+params[1].seriesName+':'+(params[0].value+params[1].value)+'<br/>'+params[0].seriesName+':'+params[0].value;}
				},
				legend:{data:['竣工面积'],x:'center',y:'bottom',textStyle:{color:'#fff'}},toolbox:{},xAxis:[{type:'category',splitLine:{show:false},data:['1-2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis:[{type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{rotate:80,textStyle:{color:"#fff"}}}],grid:{x:20,y:30,x2:0,y2:50},calculable : false,
				series:[
				//{name:'总面积',type:'bar',stack:'总量',itemStyle:{normal:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'},emphasis:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'}},data:[]},
				{name:'竣工面积',type:'bar',stack:'总量',itemStyle:{normal:{label:{show:true,position:'insideTop'}}},data:[120189 ,120189 ,143191 ,143191 ,378861 ,378861 ,378861 ,378861 ,378861 ,425572 ,491258 ]}]};	
	var list1 = [120189 ,120189 ,143191 ,143191 ,378861 ,378861 ,378861 ,378861 ,378861 ,425572 ,491258 ];
	//option1=zengliang(list1,option1);
	
	var myChart = echarts.init(document.getElementById("chart_2_1"));
	myChart.setOption(option);
	var myChart = echarts.init(document.getElementById("chart_2_2"));
	myChart.setOption(option1);
}
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478',3:'rgb(218,112,214)'};
 var yearData = ['2015年','2016年','2017年'];

 
//外贸外资
function find3(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '出口总值和实际利用外资(万美元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['出口总值','外资总值'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [ 
			{name:'出口总值',type:'bar',stack:'值',yAxisIndex:0,data:[14739,8978,14962],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
			{name:'外资总值',type:'bar',stack:'值',yAxisIndex:0,data:[1125,1600,2600],itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}},barWidth:30}]
		};
		var myChart = echarts.init(document.getElementById("chart_3_1"));
		myChart.setOption(option);
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '出口总值和实际利用外资增长(%)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['出口总值增长','外资总值增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:50,x2:5,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'出口总值增长',type:'bar',data:[-12.3,21.78,15.3],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}},
			{name:'外资总值增长',type:'bar',data:[-31.4,42.2,62.5],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}}}
			]
		};
		var myChart1 = echarts.init(document.getElementById("chart_3_2"));
		myChart1.setOption(option1);
} 

 
 //城乡居民收支
function find6(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '可支配收入(元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['城镇收入','农村收入','城镇增长','农村增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'收入(元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'城镇收入',type:'bar',stack:'值',yAxisIndex:0,data:[27395,28984,31071],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
			{name:'农村收入',type:'bar',stack:'值',yAxisIndex:0,data:[13711,14822,16102],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}},barWidth:30},
			{name:'城镇增长',type:'line',yAxisIndex:1,data:[8.9,5.8,7.2],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}},
			{name:"农村增长",type:'line',yAxisIndex:1,data:[9.6,8.1,8.6],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_6_1"));
	myChart.setOption(option);
	
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '消费支出(元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['城镇支出','农村支出','城镇增长','农村增长'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:50,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'支出(元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'增长(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'城镇支出',type:'bar',stack:'值',yAxisIndex:0,data:[18305,19312,19795],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
			{name:'农村支出',type:'bar',stack:'值',yAxisIndex:0,data:[9993,10722,11655],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}},barWidth:30},
			{name:'城镇增长',type:'line',yAxisIndex:1,data:[7.4,5.5,2.5],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}},
			{name:"农村增长",type:'line',yAxisIndex:1,data:[8.8,7.3,8.7],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_6_2"));
	myChart.setOption(option1);
}   

 //GDP
 function find1(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '地区生产总值(亿元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['第一产业','第二产业','第三产业'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [ 
{name:'第三产业',type:'bar',stack:'值',yAxisIndex:0,data:[104.06,113.58 ,130.62],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
{name:'第二产业',type:'bar',stack:'值',yAxisIndex:0,data:[137.96,146.36,163.24 ],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}},barWidth:30},
{name:'第一产业',type:'bar',stack:'值',yAxisIndex:0,data:[35.04,38.72,'37.80'],itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}},barWidth:30}]
		};
		var myChart = echarts.init(document.getElementById("chart_1_1"));
		myChart.setOption(option);
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '地区生产总值增幅(%)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['第一产业','第二产业','第三产业'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:50,x2:5,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'第一产业',type:'bar',data:[2.7,3.3,-0.1 ],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}}},
			{name:'第二产业',type:'bar',data:[2.9,6.5 ,4.3 ],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}}},
			{name:'第三产业',type:'bar',data:[12.7,7.5,9.6 ],barWidth:30,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}}
			]
		};
		var myChart1 = echarts.init(document.getElementById("chart_1_2"));
		myChart1.setOption(option1);
 } 
 
 
 var clickObj = {};

function yxClick(o,n){
	showLiDiv(o, n);
	if(clickObj[n]){
		return;
	}
	clickObj[n] = 1;
	switch(n){	
		case 1: find1();break;//GDP
		case 2: find2();break;//房地产
		case 3: find3();break;//外贸外资
		case 4: find4();break;//金融存贷款
		case 5: find5();break;//全社会工业用电量
		case 6: find6();break;//城乡居民收支
		case 7: find7();break;//财政收入
		case 8: find8();break;//
	}
}
 function showLiDiv(o,n){
	$(".buildright").hide();
	$("#buildright_"+n).show();
	$("#ul_org li").removeClass("buildleft-list-dq");
	$(o).addClass("buildleft-list-dq");
 }
 
 function zengliang(list,option){
	var lastSum = 0;
	for(var i=0,l=list.length;i<l;i++){
		var cha = list[i]-lastSum;
		//option.xAxis[0].data.push((i+1)+'月');
		if(cha >=0){
			option.series[0].data.push(lastSum);
			option.series[1].data.push(cha);
			//option.series[2].data.push('-');
		}else{
			option.series[0].data.push(lastSum+cha);
			option.series[1].data.push('-');
			//option.series[2].data.push(Math.abs(cha));
		}
		lastSum=list[i];
	}
	return option;
 }
</script>
</html>
