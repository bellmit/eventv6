<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>重点人群</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom"  style="bottom:0;position: relative;left: 0px;">
    <ul>      
      <div class="npAlarminfo citybgbox bpsmain" >
        <div class="bpscon">
         <ul class="bpscon-tit key-tit" id='ul_org'>
           <!-- <li onclick="rkClick(this,'S001011')" title="上访人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>上访人员</li>-->
             <li onclick="rkClick1(this,'S001')" title="重点人口"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>重点人口</li>
            <li onclick="rkClick1(this,'S001012')" title="吸毒人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>吸毒人员</li>
       <!-- <li onclick="rkClick(this,'S001013')" title="邪教人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>邪教人员</li>-->
            <li onclick="rkClick1(this,'S001014')" title="矫正人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>矫正人员</li>
            <li onclick="rkClick1(this,'S001015')" title="刑释解教人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>刑释解教人员</li>
            <li onclick="rkClick1(this,'S001016')" title="重精神病人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>重精神病人员</li>
            <!-- <li onclick="rkClick(this,'S001017')" title="危险从业人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>危险从业人员</li>-->
          </ul>  
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
              <div class="key-right" style="margin:0px 10px 0 456px;width:270px;">
                <div class="agechart"><div id="chart_rk3" style="width:270px;height:395px"></div></div>
              </div>
              <div class="clearfloat"></div>
            </div>
          </div>
          
         <!--  -->
         <div class="bpscon-box" id="div_1" style="display:none;">
            <div class="key-chart" style="padding-top:4px;padding-left:7px;">
              <div class="key-left" style="width:360px;padding-left:6px;">
                <div class="key-con" style="padding: 5px;">
                  <iframe  scrolling="no" data-iframe="true" id="iframe1" region="center" src="" style="width:340px;height:180px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>

                </div>
              </div>  
                <div class="key-right" style="margin:0px 6px 0 360px;">
                  	  <iframe  scrolling="no" data-iframe="true" id="iframe2"  region="center" src="" style="width:340px;height:180px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
                </div>
                <div class="key-con" style="margin-top:3px;margin-left:6px;">
                 <iframe  scrolling="no" data-iframe="true" region="center" id="iframe3" src="" style="width:698px;height:180px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
                </div>
              </div> 
              <div class="clearfloat"></div>
            </div> 
          <div class="clearfloat"></div>
        </div><!--end .bpscon-->
      </div><!--end .bpsmain-->
      
    </ul>
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
 $(function(){
     $("#ul_org li").first().click();
 });
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
function rkClick(o,stype){
	$(".bpscon-box").hide();
	 $("#div_0").show();   
	$("#ul_org li").removeClass("bpscon-tit-dq");
	$(o).addClass("bpscon-tit-dq");
	index = layer.load(0, {time: 5000});
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,module :'zd'},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){
        	showAge(data.age);
        	showOrgCode(data.orgcode,o.title);
        	showYear(data.year,o.title);
        	//parent.renderCustomZhuanTi(false);// 清除
        	if(data.year.length>0){
        		var year = data.year[data.year.length-1].syear;
	        	//parent.renderCustomZhuanTi(true, year, stype);// 标注
        	}
        	layer.close(index); 
        }
    });
}

 function rkClick1(o,stype){
	$(".bpscon-box").hide();
	 $("#div_1").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
 	//var year = document.getElementById("year2").value;
 	//cjYearChange({cal:{date:{y:year}}}, stype);
 	
 	var url="${BI_URL!}";
 	//url="http://gd.fjsq.fj.cegn.cn:8088/bi";
     if(stype=='S001'){
         $("#iframe1").attr("src",url+"/report/szzg/keypopulation/echarts?type=1");
         $("#iframe2").attr("src",url+"/report/szzg/keypopulation/echarts?type=2");
         $("#iframe3").attr("src",url+"/report/szzg/keypopulation/echarts?type=3");
     }else if(stype=='S001012'){
 		$("#iframe1").attr("src",url+"/report/szzg/drugfusionchart/echarts?version=V6&type=2");
	 	$("#iframe2").attr("src",url+"/report/szzg/drugfusionchart/echarts?version=V6&type=3");
	 	$("#iframe3").attr("src",url+"/report/szzg/drugfusionchart/echarts?type=1");
 	}else if(stype=='S001014'){
 		$("#iframe1").attr("src",url+"/report/szzg/correctionalfusionchart/echarts?version=V6&type=2");
	 	$("#iframe2").attr("src",url+"/report/szzg/correctionalfusionchart/echarts?version=V6&type=3");
	 	$("#iframe3").attr("src",url+"/report/szzg/correctionalfusionchart/echarts?type=1");
 	}else if(stype=='S001015'){
	 	$("#iframe1").attr("src",url+"/report/szzg/releasedRecordfusionchart/echarts?version=V6&type=2");
	 	$("#iframe2").attr("src",url+"/report/szzg/releasedRecordfusionchart/echarts?version=V6&type=3");
	 	$("#iframe3").attr("src",url+"/report/szzg/releasedRecordfusionchart/echarts?type=1");
 	}else if(stype=='S001016'){
	 	$("#iframe1").attr("src",url+"/report/szzg/mentalIllness/echarts?version=V6&type=2");
	 	$("#iframe2").attr("src",url+"/report/szzg/mentalIllness/echarts?version=V6&type=3");
	 	$("#iframe3").attr("src",url+"/report/szzg/mentalIllness/echarts?type=1");
 	}
 }

function showYear(data,title){
	if(data.length == 0){
		noData("chart_rk2");
		return;
	}
	var xAxisArr=[],series=[];
	for(var i=0,l=data.length;i<l;i++){
		xAxisArr.push(data[i].syear);
		series.push(data[i].snum);
	}
	var option = {tooltip : {trigger: 'axis'},title:{text:"年度"+title+"趋势",textStyle:{color:"#fff",fontSize: 15}},
		    legend: {data: ['人数'],textStyle:{color:'#fff'}},
		    calculable : true, grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'value' ,axisLabel:{textStyle:{color:"#fff"}}}],
		    xAxis : [{type : 'category',data : xAxisArr ,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : [ {name:"人数",type:'line',data:series,textStyle:{color:"#fff"}}]
		};
	var myChart = echarts.init(document.getElementById("chart_rk2"));
	myChart.setOption(option);
}

function showOrgCode(data,title){
	if(data.length == 0){
		noData("chart_rk1");
		return;
	}
	var xAxisArr=[],series=[];
	for(var i=0,l=data.length;i<l;i++){
		xAxisArr.push(data[i].orgName);
		series.push({value:data[i].snum});
	}
	var option = {tooltip : {trigger: 'axis'},title:{text:"各区"+title,textStyle:{color:"#fff",fontSize: 15}},
		    legend: {data: ['人数'],textStyle:{color:'#fff'}},
		    calculable : true, grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'value',axisLabel:{textStyle:{color:"#fff"}}}],
		    xAxis : [{type : 'category',data : xAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : [ {name:"人数",type:'bar',data:series,barWidth:20,textStyle:{color:"#fff"}}]
		};
	var myChart = echarts.init(document.getElementById("chart_rk1"));
	myChart.setOption(option);
}

function showAge(data){
	if(data.length == 0){
		noData("chart_rk3");
		return;
	}
	
	var yAxisObj={},yAxisArr=[] ,sexObj={};
	for(var i=0,l=data.length;i<l;i++){
		var d = data[i].stypeStr.split("("),sex = d[1].substring(0,1);
		if(!yAxisObj[d[0]]){
			yAxisArr.push(d[0]);
			yAxisObj[d[0]] = {};
		}
		yAxisObj[d[0]][sex]=data[i].snum;
	}
	var series = {'m':[],'f':[]	};
	for(var j=0,lj=yAxisArr.length;j<lj;j++){
		series.m.push(isNum(yAxisObj[yAxisArr[j]]['男']));
		series.f.push(isNum(yAxisObj[yAxisArr[j]]['女']));
	}
	var option = {tooltip : {trigger: 'axis'},
		    legend: {data: ['男','女'],textStyle:{color:'#fff'}},
		    calculable : true,
		    xAxis : [{type : 'value',axisLabel:{textStyle:{color:"#fff"}}}],
		    grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'category',data : yAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : [ {name:"男",type:'bar',stack:'sum',data:series.m,textStyle:{color:"#fff"},barWidth:20,itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()}}},
		        {name:"女",type:'bar',stack:'sum',data:series.f,textStyle:{color:"#fff"},barWidth:20,itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()}}}]
		};
	var myChart = echarts.init(document.getElementById("chart_rk3"));
	myChart.setOption(option);
}
function isNum(n){
	return n?n:0;
}
</script>
</html>
