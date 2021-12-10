<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>林地面积</title>
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
	var option = {tooltip:{trigger:'axis'},title:{ text: '',x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{data:['林地面积','森林蓄积','森林覆盖率'],textStyle:{color:'#fff'}},toolbox:{show:true},grid:{x:80,y:40,x2:30,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
							{name:'覆盖率(%)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
				series : [
					{name:'林地面积',type:'bar',yAxisIndex:0,data:[],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[0];})()}},barWidth:30},
					{name:'森林蓄积',type:'bar',yAxisIndex:0,data:[],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[2];})()}},barWidth:30},
					{name:"森林覆盖率",type:'line',yAxisIndex:1,data:[],itemStyle:{normal:{label:{show:true},color:(function(a,b,c){return colorObj[1];})()}}}
				]};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGeneralData.json?t='+Math.random(),
		type: 'POST',
		data: {orgCode:orgCode},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_1_1");
				return ;
			}
			var yearObj = {},yearArr=[];
			for(var i=0,l=data.list.length;i<l;i++){
				var d = data.list[i];
				if(!yearObj[d.DIM_TIME]){
					yearObj[d.DIM_TIME] = {};
					option.xAxis[0].data.push(d.DIM_TIME+'年');	
					yearArr.push(d.DIM_TIME);
				}
				yearObj[d.DIM_TIME][d.IDX_PARAM] = isNum(d.IDX_VAL);
			}
			for(var i=0,l=yearArr.length;i<l;i++){
				var d = yearObj[yearArr[i]];
				option.series[0].data.push({value:isNum(d.ldmj),year:yearArr[i],column:'ldmj',colName:'林地面积'});
				option.series[1].data.push({value:isNum(d.slmj),year:yearArr[i],column:'slmj',colName:'森林蓄积'});
				option.series[2].data.push({value:isNum(d.slfgl),year:yearArr[i],column:'slfgl',colName:'森林覆盖率'});
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

 function findItem(param){
	var option = {tooltip:{trigger:'axis'},title:{ text: param.year+'年'+param.colName,x:'center',textStyle:{color:'#fff',fontSize:21}},
				legend:{show:false,data:['总值']},toolbox:{show:true},grid:{x:60,y:40,x2:10,y2:50},calculable : false,
				xAxis : [{type:'category',data:[],axisLabel:{rotate:40,textStyle:{color:"#fff"}}}],
				yAxis : [{name:'',type:'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
				series : [
					{name:"总值",type:'bar',stack:'GDP',data:[],barWidth:30,itemStyle:{normal:{label:{show:true}}}}]
				};
	$.ajax({
		url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findGeneralData.json?t='+Math.random(),
		type: 'POST',
		data: {year:param.year,type:param.column},
		dataType:"json",
		error: function(data){
			$.messager.alert('提示','信息获取异常!','warning');
			
		},
		success: function(data){ 
			if(data.list.length==0){
				noData("chart_2_1");
				return ;
			}
			for(var i=0,l=data.list.length;i<l;i++){
				var d = data.list[i];
				option.xAxis[0].data.push(d.REGION_NAME);				
				option.series[0].data.push(isNum(d.IDX_VAL));
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
