<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>经济运行</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
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
                <li onclick="yxClick(this,1)">地区生产总值</li>
                <li onclick="yxClick(this,2)">投资额与零售总额</li>
                <li onclick="yxClick(this,3)">外资外贸</li>
                <li onclick="yxClick(this,4)">银行业存贷款</li>
                <li onclick="yxClick(this,5)">全社会用电量</li>
                <li onclick="yxClick(this,6)">居民收入</li>
                <li onclick="yxClick(this,7)">学校人员</li>
                <li onclick="yxClick(this,8)">医疗资源</li>
              </ul>
            </div>
          </div>
			<!-- 地区生产总值 -->
          <div class="buildright" id="buildright_1">
            <div  style="width:460px;float:left;">
                <div class="gross-chart" id="chart_1_1" style="width:460px;height:350px;"></div>
            </div><!--end .fisright-chart1-->
            <div  style="width:460px;float:left;">
               <div class="gross-chart" id="chart_1_2" style="width:460px;height:350px;"></div>
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
           <!-- 投资额与零售总额 -->
          <div class="buildright" id="buildright_2">
           
            <div class="fisright-chart2">
				<div class="grosslist" id="grosslist_2" >
				</div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 外资外贸 -->
          <div class="buildright" id="buildright_3">
            <div class="fisright-chart2">
                 <div class="grosslist" id="grosslist_3" >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 驻澄银行业存贷款 -->
          <div class="buildright" id="buildright_4">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_4"  >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		  
		   <!--全社会用电量  -->
          <div class="buildright" id="buildright_5">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_5"  >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
           
		   <!-- 居民收入 -->
          <div class="buildright" id="buildright_6">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_6"  >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!--学校人员 -->
          <div class="buildright" id="buildright_7">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_7"  >
	                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
		  
		    <!-- 医疗资源 -->
         <div class="buildright" id="buildright_8">
            <div class="fisright-chart2">
	                <div class="grosslist" id="grosslist_8"  >
	                </div><!--end .fislist-->
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
 $(function(){
     $("#ul_org li").eq(0).click();//
 });
 
function find8(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '医疗资源',textStyle:{color:'#fff'}},
	    legend: {data: ["卫生技术人员(人)","医生数(人)","床位数(张)"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true, grid:{x:60,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [
		         {name:"卫生技术人员(人)",type:'bar',data:[6967,7010,7340,7852,8435,8975,9405],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
				{name:"医生数(人)",type:'bar',data:[0,0,6967,7010,7340,7852,8435,8975,9405],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"床位数(张)",type:'line',data:[5366,6240,6871,7402,7605,7829,8080],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
				 ]
	};
	var myChart = echarts.init(document.getElementById("grosslist_8"));
	myChart.setOption(option);
}  
function find7(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '各类学校人员人数(人)',textStyle:{color:'#fff'}},
	    legend: {data: ["在校学生数","专任教师数"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true, grid:{x:60,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"在校学生数",type:'bar',data:[174281,172200,168745,167198,166394,165569,168432],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"专任教师数",type:'bar',data:[14454,14314,14276,14206,14426,14320,14365],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}}
				 ]
	};
	var myChart = echarts.init(document.getElementById("grosslist_7"));
	myChart.setOption(option);
}   
function find6(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '居民收入(元)',textStyle:{color:'#fff'}},
	    legend: {data: ["城镇职工平均工资","城镇居民人均可支配收入","农民人均纯收入"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true, grid:{x:60,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"城镇职工平均工资",type:'line',data:[44392,53357,58973,64460,69823,71471,75620],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"城镇居民人均可支配收入",type:'line',data:[30184,34888,39437,43144,46880,50701,54631],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"农民人均纯收入",type:'line',data:[14898,17460,19660,21882,23965,26012,28181],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}}
				 ]
	};
	var myChart = echarts.init(document.getElementById("grosslist_6"));
	myChart.setOption(option);
}   
function find5(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '全社会用电量(亿千万时)',textStyle:{color:'#fff'}},
	    legend: {data: ["总用电量","工业用电量"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true,grid:{x:40,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"总用电量",type:'bar',data:[218.95,233.35,229.54,239.47,237.87,235.6,244.93],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"工业用电量",type:'bar',data:[198.56,211.77,205.88,211.62,212.09,208.47,214.41],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}}
				 ]
	};
	var myChart = echarts.init(document.getElementById("grosslist_5"));
	myChart.setOption(option);
}  
function find4(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '银行业存贷款',textStyle:{color:'#fff'}},
	    legend: {data: ["年末存款余额","年末贷款余额","居民人均储蓄"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true,grid:{x:40,y:50,x2:50,y2:30},
	    yAxis : [{name : '业务额(亿元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'储蓄额(元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"年末存款余额",type:'bar',data:[2005.10,2261.74,2474.21,2725.73,2942.15,3099.29,3624.54],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"年末贷款余额",type:'bar',data:[1546.02,1780.76,2051.00,2157.16,2357.37,2485.51,2751.37],yAxisIndex:0,itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
				 {name:"居民人均储蓄",type:'line',data:[54468,58173,65761,72139,76560,83660,87664],yAxisIndex:1}]
	};
	var myChart = echarts.init(document.getElementById("grosslist_4"));
	myChart.setOption(option);
}  
function find3(){
	 var option = {tooltip : {trigger: 'axis'},title:{ text: '外资外贸(亿美元)',textStyle:{color:'#fff'}},
	    legend: {data: ["实际利用外资","进出口总额","自营出口总额","一般预算收入"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true,grid:{x:40,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"实际利用外资",type:'line',data:[7.50,7.21,7.72,8.08,8.55,10.1,10.55]},
		         {name:"进出口总额",type:'line',data:[158.49,209.06,192.65,199.99,223.04,203.1,199.93]},
		         {name:"自营出口总额",type:'line',data:[94.24,118.90,105.15,108.33,130.23,124.1,119.75]},
				 {name:"一般预算收入",type:'line',data:[130.72,153.37,167.19,182.28,200.66,218.9,229.91]}]
	};
	var myChart = echarts.init(document.getElementById("grosslist_3"));
	myChart.setOption(option);
} 
function find2(){
	var option = {tooltip : {trigger: 'axis'},title:{ text: '投资额与零售总额(亿元)',textStyle:{color:'#fff'}},
	    legend: {data: ["固定资产投资额","工业投入","社会消费品零售总额"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true, grid:{x:40,y:50,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: yearData ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : [{name:"固定资产投资额",type:'bar',data:[626.52,711.91,834.28,948.79,1045.97,1128.6,1133.03],itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
		         {name:"工业投入",type:'bar',data:[330.45,362.79,411.59,462.51,478.55,514.6,493.72],itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}},
				 {name:"社会消费品零售总额",type:'bar',data:[381.37,447.85,512.25,577.93,643.07,705.2,776.05],itemStyle:{normal:{label:{show:true,position:'insideTop',formatter:function(a,b,c){return c;}}}}}]
	};
	var myChart = echarts.init(document.getElementById("grosslist_2"));
	myChart.setOption(option);
}
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478',3:'rgb(218,112,214)'};
 var yearData = ['2010年','2011年','2012年','2013年','2014年','2015年','2016年']
 function find1(){
	var option = {tooltip:{trigger:'axis'},title:{ text: '地区生产总值(亿元)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['一产值','二产值','三产值','可比价'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:50,x2:40,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{name:'增加值(亿元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
		{name:'合计可比价(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
{name:'三产值',type:'bar',stack:'值',yAxisIndex:0,data:[780.43,938.54,1043.78,1107.18,1178.69,1250.16,1357.93],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
{name:'二产值',type:'bar',stack:'值',yAxisIndex:0,data:[1184.23,1354.31,1443.91,1477.35,1520.9,1584.42,1680.99],itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()}},barWidth:30},
{name:'一产值',type:'bar',stack:'值',yAxisIndex:0,data:[36.26,43.02,47.69,51.53,54.36,46.28,44.34],itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}},barWidth:30},
			{name:"可比价",type:'line',data:[13.3,12.0,10.6,9.6,7.8,7.4,7.4],yAxisIndex:1,itemStyle:{normal:{color:(function(a,b,c){return colorObj[3];})()}}}]
		};
		var myChart = echarts.init(document.getElementById("chart_1_1"));
		myChart.setOption(option);
		
	var option1 = {tooltip:{trigger:'axis'},title:{ text: '生产总值占GDP比重(%)',textStyle:{color:'#fff',fontSize:16}},
		legend:{data:['一产比重','二产比重','三产比重'],x:'right',textStyle:{color:'#fff'}},
		toolbox:{show:true},grid:{x:40,y:50,x2:5,y2:30},
		calculable : true,
		xAxis : [{type:'category',data:yearData,axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'三产比重',type:'bar',stack: '增加值',yAxisIndex:0,data:[39.01,40.18,41.17,42.00,42.80 ,43.40 ,44.04],barWidth:30},
			{name:'二产比重',type:'bar',stack: '增加值',yAxisIndex:0,data:[59.18,57.98,56.95,56.04,55.23 ,55.00 ,54.52],barWidth:30},
			{name:'一产比重',type:'bar',stack: '增加值',yAxisIndex:0,data:[1.81,1.84,1.88,1.95,1.97 ,1.61 ,1.44],barWidth:30}]
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
		case 1: find1();break;//地区生产总值
		case 2: find2();break;//投资额
		case 3: find3();break;//外资外贸
		case 4: find4();break;//驻澄银行业存贷款
		case 5: find5();break;//全社会用电量
		case 6: find6();break;//居民收入
		case 7: find7();break;//各类学校人员人数
		case 8: find8();break;//医疗资源
	}
}
 function showLiDiv(o,n){
	$(".buildright").hide();
	$("#buildright_"+n).show();
	$("#ul_org li").removeClass("buildleft-list-dq");
	$(o).addClass("buildleft-list-dq");
 }
</script>
</html>
