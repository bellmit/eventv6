<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>帮扶对象</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:15px 14px;}
    </style>
</head>

<body class="npmap">

<div class="npcityMainer">
  <div class="npcityBottom"  style="bottom:0;position: relative;left: 0px;">
      <div class="npAlarminfo citybgbox bpsmain" >
        <div class="bpscon">
          <ul class="bpscon-tit key-tit" id='ul_org'>
            <!--
            <li onclick="rkClick(this,'S001004')" title="困难党员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>困难党员</li>
            <li onclick="rkClick(this,'S001005')" title="退休人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>退休人员</li>
            <li onclick="rkClick1(this,'S001006')" value="S001006" title="低保人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>低保人员</li>
            <li onclick="rkClick(this,'S001007')" title="失业人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>	失业人员</li>
            <li onclick="rkClick(this,'S001008')" title="服兵役人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>服兵役人员</li>
            <li onclick="rkClick2(this,'S001009')" title="残疾人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>残疾人员</li>
            <li onclick="rkClick3(this,'S001010')" title="居家养老人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>居家养老人员</li>
          -->

            <!-- <li onclick="rkClick4(this,'S001004')" title="困难党员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>困难党员</li> -->
            <li onclick="rkClick4(this,'S001005')" title="退休人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>退休人员</li>
            <li onclick="rkClick4(this,'S001006')" value="S001006" title="低保人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>低保人员</li>
            <li onclick="rkClick4(this,'S001007')" title="失业人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>	失业人员</li>
            <li onclick="rkClick4(this,'S001008')" title="服兵役人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>服兵役人员</li>
            <li onclick="rkClick4(this,'S001009')" title="残疾人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>残疾人员</li>
            <li onclick="rkClick5(this)" title="持证残疾人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>持证残疾人员</li>
            <li onclick="rkClick4(this,'S001010')" title="居家养老人员"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>居家养老人员</li>
              <!-- <li onclick="rkClick4(this,'S001011')" title="特困人群"><i><img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon16.png" width="13" height="17" alt=""></i>特困人群</li> -->
          </ul>

		<!-- 持证残疾人员  -->
          <div class="bpscon-box" id="div_5">
            <div class="key-chart" style="padding-top:0px;">
              <div class="key-left" style="width:725px;padding-left:13px;">
                  <div class="dpchart"><div id="chart_rk10" style="width:725px;height:400px"></div></div>
              </div>
              <div class="clearfloat"></div>
            </div>
          </div>
		  <!-- 困难党员、退休人员、失业人员 、服兵役人员  -->
          <div class="bpscon-box" id="div_0">
            <div class="key-chart" style="padding-top:0px;">
              <div class="key-left" style="width:447px;padding-left:13px;">
                <div class="key-con">
                  <div class="dpchart"><div id="chart_rk1" style="width:417px;height:180px"></div></div>
                </div>
                <div class="key-con" style="margin-top:3px;">
                  	<div class="dpchart"><div id="chart_rk2" style="width:417px;height:180px"></div></div>
                </div>
              </div>
              <div class="key-right" style="margin:0px 10px 0 456px;width:270px">
                <div class="agechart"><div id="chart_rk3" style="width:270px;height:395px"></div></div>
              </div>
              <div class="clearfloat"></div>
            </div>
          </div>
          
          <!--低保  -->
           <div class="bpscon-box" id="div_1" style="display:none;">
    <div style="padding-top:4px;padding-left:15px;">        
              年份：<input type="text" class="inp1 Wdate timeClass" id="year"  value="${currentYear}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:dbYearChange});" />
            </div>	
            <div class="key-chart" style="padding-top:4px;padding-left:7px;">
              <div class="key-left" style="width:360px;padding-left:6px;">
                <div class="key-con" style="padding: 5px;">
                  <div class="dpchart"><div id="chart_rk4" style="width:340px;height:180px"></div></div>
                </div>
              </div> 
                <div class="key-right" style="margin:0px 10px 0 360px;width:358px">
                  	<div class="agechart"><div id="chart_rk5" style="width:350px;height:180px"></div></div>
                </div>
                <div class="key-con" style="margin-top:3px;margin-left:6px;">
                  	<div style="width:702px;height:56px;overflow-y:hidden;">
						<table  width="700px" border="0" cellspacing="0" cellpadding="0" class="replist">
						<tbody>
							<tr class="coltit"><td rowspan="2" width="12%">区域</td><td colspan="3" width="27%">城市低保</td><td colspan="3" width="27%">农村低保</td><td colspan="3" width="27%">农村五保</td>
						</tr>
						<tr class="coltit">
							<td>户数</td><td>人数</td><td>金额(元)</td><td>户数</td><td>人数</td><td>金额(元)</td><td>户数</td><td>人数</td><td>金额(元)</td>
						</tr>
						</tbody>
						</table>
					</div>
					<div class="dpchart" style="width:702px;height:102px;overflow-y:auto;">
						<table id="table_db" width="700px" border="0" cellspacing="0" cellpadding="0" class="replist">
						</table>
					</div>
                </div>
              </div> 
              <div class="clearfloat"></div>
            </div>
         <!--残疾  -->
         <div class="bpscon-box" id="div_2" style="display:none;">
         <div style="padding-top:4px;padding-left:15px;">
            年份：  <input type="text" class="inp1 Wdate timeClass" id="year2"  value="${currentYear}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:cjYearChange});" />
		</div>					
            <div class="key-chart" style="padding-top:4px;padding-left:7px;">
              <div class="key-left" style="width:360px;padding-left:6px;">
                <div class="key-con" style="padding: 5px;">
                  <div class="dpchart"><div id="chart_rk6" style="width:340px;height:180px"></div></div>
                </div>
              </div> 
                <div class="key-right" style="margin:0px 10px 0 360px;">
                  	<div class="agechart"><div id="chart_rk7" style="width:350px;height:180px"></div></div>
                </div>
                <div class="key-con" style="margin-top:3px;margin-left:6px;">
                  	<div style="width:700px;height:28px;overflow-y:hidden;" >
						<table  width="680px" border="0" cellspacing="0" cellpadding="0" class="replist">
						<tbody>
							<tr class="coltit"><td style='width:169px;'>残疾类型</td>	<td style='width:100px;'>1级</td><td style='width:101px;'>2级</td><td style='width:101px;'>3级</td><td style='width:101px;'>4级</td><td style='width:101px;'>合计</td></tr>
						</tbody>
						</table>
					</div>
					<div class="dpchart" style="width:700px;height:138px;overflow-y:auto;" id="div_cj">
						<table id="table_cj" width="680px" border="0" cellspacing="0" cellpadding="0" class="replist">
						</table>
					</div>
                </div>
              </div> 
              <div class="clearfloat"></div>
            </div> 
           
            <!-- 居家养老  -->
         <div class="bpscon-box" id="div_3" style="display:none;">
         <div style="padding-top:4px;padding-left:15px;">
            年份：  <input type="text" class="inp1 Wdate timeClass" id="year3"  value="${currentYear}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:jjylYearChange});" />
		</div>					
            <div class="key-chart" style="padding-top:4px;padding-left:7px;">
              <div class="key-left" style="width:270px;padding-left:6px;">
                <div class="key-con" style="padding: 5px;">
                  <div class="dpchart"><div id="chart_rk8" style="width:250px;height:180px"></div></div>
                </div>
              </div> 
                <div class="key-right" style="margin:0px 10px 0 270px;width:448px;">
                  	<div class="agechart"><div id="chart_rk9" style="width:445px;height:180px"></div></div>
                </div>
                <div class="key-con" style="margin-top:3px;margin-left:6px;">
                  	<div style="width:702px;height:56px;overflow-y:hidden;">
						<table width="698px" border="0" cellspacing="0" cellpadding="0" class="replist">
						<tbody>
							<tr class="coltit"><td rowspan="2" width="12%">区域</td><td colspan="2" width="27%">80-89周岁享受生活补贴情况</td><td colspan="2" width="27%">90-99周岁享受生活补贴情况</td><td colspan="2" width="27%">100周岁以上享受生活补贴情况</td>
						</tr>
						<tr class="coltit">
							<td>发放总人数</td><td>实发金额</td><td>发放总人数</td><td>实发金额</td><td>发放总人数</td><td>实发金额</td>
						</tr>
						</tbody>
						</table>
					</div>
					<div class="dpchart" style="width:702px;height:102px;overflow-y:auto;">
						<table id="table_jjyl" width="698px" border="0" cellspacing="0" cellpadding="0" class="replist">
						
						</table>
					</div>
                </div>
              </div> 
              <div class="clearfloat"></div>
            </div> 
           
         <!--  -->
         <div class="bpscon-box" id="div_4" style="display:none;">
            <div class="key-chart" style="padding-top:4px;padding-left:7px;">
              <div class="key-left" style="width:360px;padding-left:6px;">
                <div class="key-con" style="padding: 5px;">
                  <iframe data-iframe="true" id="iframe1"  scrolling="no" region="center" src="" style="width:340px;height:180px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
                </div>
              </div>  
                <div class="key-right" style="margin:0px 6px 0 360px;">
                  	  <iframe data-iframe="true" id="iframe2"  scrolling="no"  region="center" src="" style="width:340px;height:180px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
                </div>
                <div class="key-con" style="margin-top:3px;margin-left:6px;">
                 <iframe data-iframe="true" region="center" id="iframe3"  scrolling="no" src="" style="width:698px;height:250px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
                </div>
              </div> 
              <div class="clearfloat"></div>
            </div> 
          <div class="clearfloat"></div>
           
          <div class="clearfloat"></div>
        </div><!--end .bpscon-->
      </div><!--end .bpsmain-->
      
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
 $(function(){
     $("#ul_org li").first().click();
     $(".dpchart").mCustomScrollbar({theme: "minimal-dark"});
 });
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
 
 //居家养老start
 function jjylYearChange(o,stype){
		if(!stype){
			stype = $(".bpscon-tit-dq").attr("value");
		}
		index = layer.load(0, {time: 5000});
	 	var stype ={'S001010001':'80-89','S001010006':'90-99','S001010011':'100+'};
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
	        type: 'POST',
	        data: {  module :'jjyl_2',syear: o.cal.date.y},
	        dataType:"json",
	        error: function(data){
	            $.messager.alert('提示','信息获取异常!','warning');
	            layer.close(index); 
	        },
	        success: function(data){ 
	        	for(var i=0,l=data.age.length;i<l;i++){
	        		data.age[i]['stypeStr'] = stype[data.age[i].stype];
	        	}
	        	showPie(data.age,{id:"chart_rk8",snum:'snum',name:'stypeStr',title:{text:"年龄阶段人数分布",textStyle:{color:"#fff",fontSize:15}}});
	        	 var tb = $("#table_jjyl");
	        	 /*$("#table_jjyl tr").each(function(i){
	        		 if(i>1){
	        			 $(this).remove();
	        		 }
	        	 });*/
				 tb.html("");
	        	 for(var i=0,l=data.table.length;i<l;i++){
	        		 tb.append("<tr "+(i%2==1?"class='coltit'":"")+"><td style='width:88px;'>"+data.table[i].GRID_NAME+"</td><td style='width:111px;'>"+data.table[i].ALL_80_NUM+"</td><td style='width:88px;'>"+
	        data.table[i].ALL_80_MONEY+"</td><td style='width:111px;'>"+data.table[i].ALL_90_NUM+"</td><td style='width:88px;'>"+data.table[i].ALL_90_MONEY+"</td><td style='width:111px;'>"+data.table[i].ALL_100_NUM+"</td><td style='width:88px;'>"+data.table[i].ALL_100_MONEY+"</td></tr>");
	        	 }
	        	layer.close(index); 
	        }
	    });
	}
 function rkClick3(o,stype){
	 $(".bpscon-box").hide();
	 $("#div_3").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
 	var year = document.getElementById("year3").value;
 	index = layer.load(0, {time: 5000});
 	jjylYearChange({cal:{date:{y:year}}});
 	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: {module :'jjyl_1'},
        dataType:"json",
        error: function(data){
        	$.messager.alert('提示','信息获取异常!','warning');
        	layer.close(index);
        },
        success: function(data){
        	if(data.multiple.length == 0){
        		noData("chart_rk9");
        		layer.close(index); 
        		return;
        	}
        	var xAxis = [],
        	 series=[{name:"发放金额",type:'line',data:[],yAxisIndex :1,axisLabel:{textStyle:{color:"#fff"}}},
        	{name:"发放总人数",type:'bar',data:[],yAxisIndex:0,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()},barWidth:20}},
        	{name:"新增人数",type:'bar',data:[],yAxisIndex:0,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()},barWidth:20}},
        	{name:"终止人数",type:'bar',data:[],yAxisIndex:0,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()},barWidth:20}}];
        	for(var i=0,l=data.multiple.length;i<l;i++){
        		var d = data.multiple[i];
        		xAxis.push(d.SYEAR);
        		series[0].data.push(d.MONEY?d.MONEY:0);
        		series[1].data.push(d.PERSON?d.PERSON:0);
        		series[2].data.push(d.NEW_NUM?d.NEW_NUM:0);
        		series[3].data.push(d.OVER_NUM?d.OVER_NUM:0);
        	}
        	var option = {tooltip : {trigger: 'axis'},
        		    legend: {data: ["发放金额","发放总人数","新增人数","终止人数"],textStyle:{color:'#fff'}},
        		    calculable : true, grid:{x:40,y:40,x2:40,y2:20},
        		    yAxis : [{name : '人口数',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
    		        		{name:'金额(万元)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
        		    xAxis : [{type : 'category',data:xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
        		    series : series
        		};
        	var myChart = echarts.init(document.getElementById("chart_rk9"));
        	myChart.setOption(option);
        	layer.close(index); 
        }
    });
 }
 //居家养老end
 
 //残疾start
 function cjYearChange(o,stype){
		if(!stype){
			stype = $(".bpscon-tit-dq").attr("value");
		}
		index = layer.load(0, {time: 5000});
		$.ajax({
	        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
	        type: 'POST',
	        data: { stype : stype, module :'cj',syear: o.cal.date.y},
	        dataType:"json",
	        error: function(data){
	        	 $.messager.alert('提示','信息获取异常!','warning');
	         	layer.close(index);
	        },
	        success: function(data){ 
	        	showPie(data.type,{id:"chart_rk6",snum:'snum',name:'stypeStr',title:{text:"残疾类型占比",textStyle:{color:"#fff",fontSize: 15}}});
	        	showPie(data.level,{id:"chart_rk7",snum:'snum',name:'stypeStr',title:{text:"残疾等级占比",textStyle:{color:"#fff",fontSize: 15}}});
	        	 var tb = $("#table_cj");
	        	 /*$("#table_cj tr").each(function(i){
	        		 if(i>0){
	        			 $(this).remove();
	        		 }
	        	 });*/
				 tb.html("");
	        	 for(var i=0,l=data.table.length;i<l;i++){
	        		 tb.append("<tr "+(i%2==1?"class='coltit'":"")+"><td style='width:169px;'>"+data.table[i].DICT_NAME+"</td><td style='width:100px;'>"+data.table[i].G1+"</td><td style='width:101px;'>"+
	        data.table[i].G2+"</td><td style='width:101px;'>"+data.table[i].G3+"</td><td style='width:101px;'>"+data.table[i].G4+"</td><td style='width:101px;'>"+data.table[i].SUMNUM+"</td></tr>");
	        	 }
	        	layer.close(index); 
	        }
	    });
	}
 function rkClick2(o,stype){
	 $(".bpscon-box").hide();
	 $("#div_2").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
 	var year = document.getElementById("year2").value;
 	cjYearChange({cal:{date:{y:year}}}, stype);
 }
//残疾end
	 
//低保start
function dbYearChange(o,stype){
	if(!stype){
		stype = $(".bpscon-tit-dq").attr("value");
		o.title = $(".bpscon-tit-dq").attr("title");
	}
	index = layer.load(0, {time: 5000});
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatsPopController/findChartByParams.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype, module :'db',syear: o.cal.date.y},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){ 
        	showOrgCode(data.money,o.title+"发放金额","chart_rk4",'金额');
        	showOrgCode(data.person,o.title+"人数","chart_rk5");
        	showDBTable(data.table);
        	layer.close(index); 
        }
    });
}
 function rkClick1(o,stype){
	 $(".bpscon-box").hide();
	 $("#div_1").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
 	var year = document.getElementById("year").value;
 	dbYearChange({cal:{date:{y:year}},title:o.title}, stype);
 }
 function showDBTable(data){
	 var tb = $("#table_db");
	tb.html("");
	 for(var i=0,l=data.length;i<l;i++){
		 tb.append("<tr "+(i%2==1?"class='colgh'":"")+"><td style='width:88px;'>"+data[i].GRID_NAME+"</td><td style='width:52px;'>"+data[i].DB_CS_HS_NUM+"</td><td style='width:52px;'>"+
data[i].DB_CS_RS_NUM+"</td><td style='width:96px;'>"+data[i].DB_CS_JE_NUM+"</td><td style='width:52px;'>"+data[i].DB_RC_HS_NUM+"</td><td style='width:52px;'>"+data[i].DB_RC_RS_NUM+"</td><td style='width:96px;'>"+data[i].DB_RC_JE_NUM+"</td><td style='width:52px;'>"
+data[i].WB_RC_HS_NUM+"</td><td style='width:52px;'>"+data[i].WB_RC_RS_NUM+"</td><td style='width:96px;'>"+data[i].WB_RC_JE_NUM+"</td></tr>");
	 }
 }
//低保end


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
        	layer.close(index); 
        }
    });
}
 function rkClick5(o){
	$(".bpscon-box").hide();
	 $("#div_5").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
	var option = {tooltip:{trigger:'axis'},title:{ text: '延平区各类残疾人持证情况',x:'left',textStyle:{color:'#fff',fontSize:21}},
		legend:{padding:[30,30,0,0],y:'top',x:'right',data:['视力残疾','听力残疾','智力残疾','肢体残疾','言语残疾','精神残疾','多重残疾'],textStyle:{color:"#fff"}},toolbox:{show:true},
		grid:{x:30,y:60,x2:60,y2:30},calculable : false,
		xAxis : [{type:'category',data:['2014年','2015年','2016年','2017年'],axisLabel:{textStyle:{color:"#fff"}}}],
		yAxis : [{type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
		series : [
			{name:'视力残疾',type:'line',data:[80,95,95,52]},
			{name:'听力残疾',type:'line',data:[77,99,106,173]},
			{name:'智力残疾',type:'line',data:[92,78,95,58]},
			{name:'肢体残疾',type:'line',data:[254,289,312,232]},
			{name:'言语残疾',type:'line',data:[3,8,5,4]},
			{name:'精神残疾',type:'line',data:[81,130,154,99]},
			{name:'多重残疾',type:'line',data:[27,25,24,32]}
			]
		};
	var myChart = echarts.init(document.getElementById("chart_rk10"));
	myChart.setOption(option);
 }
 function rkClick4(o,stype){
	$(".bpscon-box").hide();
	 $("#div_4").show();
 	$("#ul_org li").removeClass("bpscon-tit-dq");
 	$(o).addClass("bpscon-tit-dq");
 	//var year = document.getElementById("year2").value;
 	//cjYearChange({cal:{date:{y:year}}}, stype);
 	var url="${BI_URL!}";
 	//url="http://gd.fjsq.fj.cegn.cn:8088/bi";
	
     if(stype=='S001011'){
         $("#iframe1").attr("src",url+"/report/szzg/poor/echarts?type=3");
         $("#iframe2").attr("src",url+"/report/szzg/poor/echarts?type=2");
         $("#iframe3").attr("src",url+"/report/szzg/poor/echarts?type=1");
     }else{
		var type = '';
		switch(stype){
			case 'S001004':type = 'A';break;
			case 'S001005':type = 'C';break;
			case 'S001006':type = 'B';break;
			case 'S001007':type = 'D';break;
			case 'S001008':type = 'G';break;
			case 'S001009':type = 'E';break;
			case 'S001010':type = 'F';break;
		}
	 
	 
 		$("#iframe1").attr("src",url+"/report/new/typePopulation/ageView?new=_new&type="+type);
	 	$("#iframe2").attr("src",url+"/report/new/typePopulation/sexView?new=_new&type="+type);
	 	$("#iframe3").attr("src",url+"/report/new/typePopulation/view?type="+type);
 	}
 }
function showYear(data,title,id){
	if(data.length == 0){
		noData(id?id:"chart_rk2");
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
	var myChart = echarts.init(document.getElementById(id?id:"chart_rk2"));
	myChart.setOption(option);
}

function showOrgCode(data,title,id,legend){
	if(data.length == 0){
		noData(id?id:"chart_rk1");
		return;
	}
	var xAxisArr=[],series=[];
	for(var i=0,l=data.length;i<l;i++){
		xAxisArr.push(data[i].orgName);
		series.push({value:data[i].snum});
	}
	var option = {tooltip : {trigger: 'axis'},title:{text:"各区"+title,textStyle:{color:"#fff",fontSize: 15}},
		    legend: {data: [legend?legend:'人数'],textStyle:{color:'#fff'},x:"right"},
		    calculable : true, grid:{x:45,y:30,x2:10,y2:30},
		    yAxis : [{type : 'value',axisLabel:{textStyle:{color:"#fff"}}}],
		    xAxis : [{type : 'category',data : xAxisArr,axisLabel:{textStyle:{color:"#fff"}}}],
		    series : [ {name:legend?legend:"人数",type:'bar',data:series,textStyle:{color:"#fff"},itemStyle:{barWidth:20},barWidth:20}]
		};
	var myChart = echarts.init(document.getElementById(id?id:"chart_rk1"));
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
		optionPie.series[0].data.push({value:data[i][type.snum], name:data[i][type.name],itemStyle:{normal:{color:(function(a,b,c){return colorObj[i];})()}}});
	}
	var myChart = echarts.init(document.getElementById(type.id));
	myChart.setOption(optionPie);
}
function isNum(n){
	return n?n:0;
}

</script>
</html>
