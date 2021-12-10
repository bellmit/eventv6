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
            <div  style="width:920px;float:left;">
                <div class="gross-chart" id="chart_1_1" style="width:920px;height:380px;"></div>
            </div><!--end .fisright-chart1-->
          </div><!--end .buildright-->
		  
		  <div class="buildright" id="div_2" style="display:none">
			<div style="width:60px;position:absolute;top:0px;z-index:100;">
				<img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/back.png" onclick="showLiDiv(1)" style="width:20px;height:20px;cursor:pointer;" title="返回"/>
			</div>
            <div  style="width:920px;float:left;">
                <div class="gross-chart" id="chart_2_1" style="width:920px;height:380px;"></div>
            </div><!--end .fisright-chart1-->
          </div><!--end .buildright-->
		  
		  
          <div class="clearfloat"></div>
        </div><!--end .buildcon-->
      </div><!--end .bpsmain-->
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
var orgCodeStr = '${orgCode}'.split('_'),orgCode,typeNum;
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478',3:'rgb(218,112,214)'};
 function getTXT(type){
	var txt = {'1':{title:'农民人均可支配收入',legend:'收入'},'2':{title:'限额以上单位零售额',legend:'零售额'},'3':{title:'限额以上批发零售业销售额',legend:'销售额'}};
	return txt[typeNum][type];
 }
 function findYears(){
	orgCode = orgCodeStr[0],typeNum=orgCodeStr[1];
	var option = {tooltip:{trigger:'axis'},title:{ text: getTXT('title'),x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{data:[getTXT('legend')],show:false},toolbox:{show:true},grid:{x:80,y:40,x2:10,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
				series : [
					{name:getTXT('legend'),type:'bar',stack:'额',data:[],itemStyle:{normal:{label:{show:true,position:'insideRight'},color:(function(a,b,c){return colorObj[3];})()}},barWidth:30}]
				};
	
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,table:'retail',type:typeNum},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_1_1");
				return ;
			}
			for(var i=data.list.length-1;i>=0;i--){
				var d = data.list[i];
				option.xAxis[0].data.push(d.YEAR_+'年');				
				option.series[0].data.push({value:isNum(d.VAL_),year:d.YEAR_});
				
			}
			var myChart = echarts.init(document.getElementById("chart_1_1"));
			myChart.setOption(option);
			myChart.on('click',function(param){
				findItem(param.data);
				showLiDiv(2);
			});
		}
	});
	
 }
var monthObj = {'1':{3:'第一季度',6:'第二季度',9:'第三季度',12:'第四季度'},
			'2':{1:'1月',2:'2月',3:'3月',4:'4月',5:'5月',6:'6月',7:'7月',8:'8月',9:'9月',10:'10月',11:'11月',12:'12月'},
			'3':{1:'1月',2:'2月',3:'3月',4:'4月',5:'5月',6:'6月',7:'7月',8:'8月',9:'9月',10:'10月',11:'11月',12:'12月'}};
 function findItem(param){
	var option = {tooltip:{trigger:'axis'},title:{ text: param.year+'年'+getTXT('title'),x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{show:false,data:['总值']},toolbox:{show:true},grid:{x:80,y:40,x2:10,y2:30},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
				series : [
					{name:getTXT('legend'),type:'line',stack:'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true}}}}]
				};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGDPDate.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode,year:param.year,table:'retail',type:typeNum},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_2_1");
				return ;
			}
			var month = monthObj[typeNum];
			for(var i=0,l=data.list.length;i<l;i++){
				var d = data.list[i];
				option.xAxis[0].data.push(month[d.MONTH_]);				
				option.series[0].data.push(isNum(d.VAL_));
			}
			var myChart = echarts.init(document.getElementById("chart_2_1"));
			myChart.setOption(option);
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
