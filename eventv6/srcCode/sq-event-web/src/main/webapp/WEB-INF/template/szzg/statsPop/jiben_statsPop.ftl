<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>人口基本</title>
		<#include "/szzg/common.ftl" />
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="bottom:0;position: relative;left: 0px;">
<div class="npAlarminfo citybgbox bpsmain" style="width:860px;">
        <div class="bpscon">
        <div style="float: left;">
        	<div id="ul_org_div" style="width:135px;height:402px;">
          <ul class="bpscon-tit" id='ul_org'>
            <li onclick="rkClick(this,'${orgcode}')" title="${orgcode}"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon15.png" width="13" height="17"></i>${orgname}</li>
           	<#list orgList as org>
            <li onclick="rkClick(this,'${org.orgCode}')" title="${org.orgCode}"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon15.png" width="13" height="17"></i>${org.orgName}</li>
           </#list>
           </ul>
           </div>
          </div>
          <div class="bpscon-box" style="margin-left: 132px;padding-bottom:5px;">
            <div class="bpscon-box-tit" style="padding: 2px 0 0 0;">
              按区域统计  （单位：人）
              <input type="text" class="inp1 Wdate timeClass" id="year"  value="${currentYear}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:rkYearChange});" >
            </div>
            <div class="bpscon-box-chart" style="margin:2px 2px 0 0px">
              <h5>农业非农业人口分布</h5>
             <div class="bpscon-chart" style="padding:0px"><div id="chart_rk1" style="width:240px;height:175px"></div></div>
              <div class="bpscon-chart" style="padding:0px"><div id="chart_rk2" style="width:240px;height:175px"></div></div>
              <div class="bpscon-chart" style="padding:0px"><div id="chart_rk3" style="width:240px;height:175px"></div></div>
              <div class="bpscon-chart" style="padding:0px"><div id="chart_rk4" style="width:715px;height:175px"></div></div>
              <div class="clearfloat"></div>
            </div>
          </div>
          <div class="clearfloat"></div>
        </div><!--end .bpscon-->
      </div><!--end .bpsmain-->
</div>
</div>
</body>

<script type="text/javascript">
var orgCode ;
 $(function(){
     $("#ul_org li").first().click();
     $("#ul_org_div").mCustomScrollbar({theme: "minimal-dark"});
 });
function rkYearChange(o,orgcode){
	if(orgcode == undefined){
		orgcode = orgCode;
	}
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: { orgCode : orgcode,syear : o.cal.date.y,module :'jiben_1'},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){
        	showPie(data.ny,"chart_rk1");
        	showPie(data.move,"chart_rk2");
        	showPie(data.outin,"chart_rk3");
        	layer.close(index);
        }
    });
}
var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
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
	for(var i=0,l=data.length;i<l;i++){
		optionPie.legend.data.push(data[i].STYPESTR);
		optionPie.series[0].data.push({value:data[i].SNUM, name:data[i].STYPESTR,itemStyle:{normal:{color:(function(a,b,c){return colorObj[i];})()}}});
	}
	var myChart = echarts.init(document.getElementById(id));
	myChart.setOption(optionPie);
}
function rkClick(o,orgcode){
	orgCode = orgcode;
	$("#ul_org li").removeClass("bpscon-tit-dq");
	$(o).addClass("bpscon-tit-dq");
	var year = document.getElementById("year").value;
	index = layer.load(0, {time: 5000});
	rkYearChange({cal:{date:{y:year}}},orgcode);
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: { orgCode : orgcode,module :'jiben_2'},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){
        	if(data.list.length == 0){
            	noData("chart_rk4");
            	layer.close(index);
            	return;
            }
			
        	var option = {tooltip : {trigger: 'axis'},
        		    legend: {x:'right',textStyle:{color:'#fff'},data:[]},
        		    toolbox: {show : true },grid:{x:50,y:20,x2:20,y2:25},
        		    calculable : true,
        		    xAxis : [{type:'category',data:[],axisLabel:{textStyle:{color:"#fff"}}}],
        		    yAxis : [{type:'value',axisLabel:{textStyle:{color:"#fff"}}}],
        		    series : []
        		};
        	var typeObj = {},typeArr = [],//多少种类 
        	yearObj={},yearArr = [];//每条线几个点
        	for(var i=0,l=data.list.length;i<l;i++){
        		var d = data.list[i];
        		if(!yearObj[d.syear]){
        			yearObj[d.syear] = true;
        			yearArr.push(d.syear);
        			option.xAxis[0].data.push(d.syear+'年');
        		}
        		if(!typeObj[d.stypeStr]){
        			option.legend.data.push(d.stypeStr);
        			typeArr.push(d.stypeStr);
        			typeObj[d.stypeStr] = {};
        		}
        		typeObj[d.stypeStr][d.syear] = d.snum;
        	}
        	for(var j=0,lj=typeArr.length;j<lj;j++){
        		var series = {name:typeArr[j],data:[],type:'line',itemStyle: {normal: {areaStyle: {type: 'default'},color:(function(a,b,c){return colorObj[j];})()}}};
        		for(var k=0,lk=yearArr.length;k<lk;k++){
        			series.data.push(isNum(typeObj[typeArr[j]][yearArr[k]]));
        		}
        		option.series.push(series);
        	}
        	
       	 var myChart = echarts.init(document.getElementById("chart_rk4"));
       		myChart.setOption(option);
       		layer.close(index); 
        }
    });
}
function isNum(n){
	return n?n:0;
}
</script>
</html>
