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
    .buildleft-list ul li{cursor:pointer;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:1060px;">
        <div class="buildcon">
          <div class="buildleft">
            <div class="buildleft-list">
              <ul id="ul_org">
                <li onclick="yxClick(this,'s1',1)">地区生产总值</li>
                <li onclick="yxClick(this,'s2',2,4)">财政收支</li>
                <li onclick="yxClick(this,'s3',3,4)">固定资产投资</li>
                <li onclick="yxClick(this,'s4',4,4)">社会消费品零售总额</li>
                <li onclick="yxClick(this,'s5',5)">对外经济</li>
                <li onclick="yxClick(this,'s6',6,4)">工商注册</li>
                <li onclick="yxClick(this,'s7',7,5)">房地产情况</li>
                <li onclick="yxClick(this,'s8',8,4)">拆迁安置</li>
                <li onclick="xzcfClick(this,'s11',11,4)">行政处罚情况</li>
				  <!--
                <li onclick="yxClick(this,'s9',9)">城市建设</li>
                <li onclick="yxClick(this,'s10',10)">进区项目</li> -->
              </ul>
            </div>
          </div>
			<!-- 地区生产总值 -->
          <div class="buildright" id="buildright_1">
            <div class="fisright-chart1">
                <h5>地区生产总体情况<span class="fr">单位：万元</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_1"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_1">
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>当月各产业生产总值</h5>
                <div class="gross-chart" id="chart_1" style="width:400px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>当月企业区域占比<span class="fr">单位：万元</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_1" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
           <!-- 财政支出 -->
          <div class="buildright" id="buildright_2">
            <div class="fisright-chart1">
                <h5>财政收支总体情况<span class="fr">单位：万元</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_2"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_2" style="font-size:12px;">
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>财政收支(年度)</h5>
                <div class="gross-chart" id="chart_2" style="width:390px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>财政收支报表<span class="fr">单位：万元</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_2" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 固定资产投资 -->
          <div class="buildright" id="buildright_3">
            <div class="fisright-chart1">
                <h5>固定资产投资总体情况<span class="fr">单位：万元</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_3"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_3" >
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>固定资产投资(年度)</h5>
                <div class="gross-chart" id="chart_3" style="width:390px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>固定资产投资报表<span class="fr">单位：万元</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_3" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 社会消费品零售总额 -->
          <div class="buildright" id="buildright_4">
            <div class="fisright-chart1">
                <h5>社会消费品零售总额总体情况<span class="fr">单位：万元</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_4"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_4" >
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>社会消费品零售总额(年度)</h5>
                <div class="gross-chart" id="chart_4" style="width:390px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>社会消费品零售总额报表<span class="fr">单位：万元</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_4" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		  
		   <!-- 对外经济 -->
          <div class="buildright" id="buildright_5">
            <div class="fisright-chart1">
                <h5>对外经济总体情况<span class="fr">单位：万元</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_5"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_5" >
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>对外经济(年度)</h5>
                <div class="gross-chart" id="chart_5" style="width:390px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>对外经济报表<span class="fr">单位：万元</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_5" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
           
		   <!-- 工商注册-->
          <div class="buildright" id="buildright_6">
            <div class="fisright-chart1">
                <h5 style="padding:0px;">工商注册数量</h5>
				<div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_6"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
				</div>	
              <div class="fislist" style="margin:0px;">
                <div class="gross-chart" id="chart_6_1" style="width:390px;height:170px;"></div>
                <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5 style="padding:0px;">工商注册资金(万元)</h5>
                <div class="gross-chart" id="chart_6" style="width:390px;height:165px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>工商注册报表<span class="fr"></span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_6" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
          
		   <!-- 房地产情况-->
          <div class="buildright" id="buildright_7">
            <div class="fisright-chart1">
                <h5 >房地产总体情况<span class="fr">单位：万㎡</span></h5>
				<div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_7"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
				</div>
			  <div class="fislist" >
					<ul id="fislist_7" >
                    
					</ul>
                  <div class="clearfloat"></div>
                <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>房地产已售情况(年度)</h5>
                <div class="gross-chart" id="chart_7" style="width:390px;height:195px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>房地产情况报表<span class="fr">单位：万㎡</span></h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_7" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->
		  
		    <!-- 拆迁安置 -->
          <div class="buildright" id="buildright_8">
            <div class="fisright-chart1">
                <h5>拆迁安置历年累计情况</h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_8"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
              <div class="fislist">
                  <ul id="fislist_8" style="font-size:12px;">
                    
                  </ul>
                  <div class="clearfloat"></div>
              </div><!--end .fislist-->
                <h5>年度拆迁安置</h5>
                <div class="gross-chart" id="chart_8" style="width:390px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2">
                <h5>拆迁安置报表</h5>
                <div class="list_div">
	                <div class="grosslist" id="grosslist_8" >
	                </div><!--end .fislist-->
                </div><!--end .fislist-->
            </div><!--end .fisright-chart2-->
          </div><!--end .buildright-->

            <!-- 行政处罚 -->
            <div class="buildright" id="buildright_11">
                <div class="fisright-chart1">
                    <h5>行政处罚总体情况<span class="fr">单位：次</span></h5>
                    <div class="fisdate">
                        <input type="text" class="inp1 Wdate timeClass" id="date_11"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-MM',ychanged:xzcfYearChange,Mchanged:xzcfYearChange});" >
                    </div><!--end .fisdate-->
                    <div class="fislist">
                        <ul id="fislist_11" >
                        </ul>
                        <div class="clearfloat"></div>
                    </div><!--end .fislist-->
                    <h5>行政处罚(年度)</h5>
                    <div class="gross-chart" id="chart_11" style="width:390px;height:180px;"></div>
                    <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
                </div><!--end .fisright-chart1-->
                <div class="fisright-chart2">
                    <h5>当月行政处罚列表<span class="fr">单位：次</span></h5>
                    <div id="list_title">
                    </div><!--end .fislist-->
                    <div class="list_div" style="height: 310px;">
                        <div class="grosslist"  id="grosslist_11">
                        </div>
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
     //$("#ul_org li").first().click();//
     $(".list_div").mCustomScrollbar({theme: "minimal-dark"});
 });
 
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};
  //拆迁安置 
 function findCQAZ(data){
	var monthObj = {};
	for(var j=0,lj=data.list.length;j<lj;j++){
		var d = data.list[j];
		monthObj[d.dictCode] = d.s3;
    }
	//if(!clickObj['fislist_8']){//只显示最新的历年
		//clickObj['fislist_8'] = true;
		$("#fislist_8").html("<li style='width:84px;margin:0px;'>拆迁户数<br><b style='font-size:18px;'>"+isNull(monthObj['S003015002'])+
		 "</b></li><li class='col-pri1'  style='width:84px;margin:0px;'>安置户数<br><b style='font-size:18px;'>"+isNull(monthObj['S003015004'])+
		 "</b></li><li  style='width:84px;margin:0px;'>拆迁面积<br><b style='font-size:18px;'>"+isNull(monthObj['S003015001'])+
		 "</b></li><li class='col-pri1'  style='width:84px;margin:0px;'>安置面积<br><b style='font-size:18px;'>"+isNull(monthObj['S003015003'])+"</b></li>");
	//}
	 if(data.monthlist.length==0){
		noData("chart_8");
        return;
	 }
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		 var d = data.monthlist[i];
		 if(!monthObj[d.SMONTH] ){
			monthObj[d.SMONTH] = {};
		 }
		 monthObj[d.SMONTH][d.DICT_CODE] = d.S1;
	 }
	 var series=[{name:"拆迁户数",type:'line',data:[],yAxisIndex:0},
		         {name:"安置户数",type:'line',data:[],yAxisIndex:0},
				 {name:"拆迁面积",type:'bar',data:[],yAxisIndex:1,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()},barWidth:20}},
				 {name:"安置面积",type:'bar',data:[],yAxisIndex:1,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[1];})()},barWidth:20}}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		xAxis.push(j+'月');
		series[0].data.push(formatNum((monthObj[j]?monthObj[j]['S003015002']:0)));
		series[1].data.push(formatNum((monthObj[j]?monthObj[j]['S003015004']:0)));
		series[2].data.push(formatNum((monthObj[j]?monthObj[j]['S003015001']:0)));
		series[3].data.push(formatNum((monthObj[j]?monthObj[j]['S003015003']:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
			legend: {data: ["拆迁户数","安置户数","拆迁面积","安置面积"],textStyle:{color:'#fff'}},
			calculable : true, grid:{x:50,y:40,x2:50,y2:20},
			yAxis : [{name : '户数(户)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
					{name:'面积(万㎡)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
			xAxis : [{type : 'category',data:xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
			series : series
		};
 	var myChart = echarts.init(document.getElementById("chart_8"));
 	myChart.setOption(option);
 }
 
 
  //房地产情况
 function findFDC(data){
	 var monthObj = {};
	 for(var i=0,l=data.list.length;i<l;i++){
		var d = data.list[i];
		if(d.dictCode == 'S003013001'){//施工面积
			monthObj['sg'] = d.s1;
		}else if(d.dictCode == 'S003013002'){//新开工面积
			monthObj['kg'] = d.s1;
		}else if(d.dictCode == 'S003013003'){//竣工面积
			monthObj['jg'] = d.s1;
		}
	 }
	 $("#fislist_7").html("<li>施工面积<br><b>"+isNull(monthObj['sg'])+"</b></li><li class='col-pri1'>新开工面积<br><b>"+isNull(monthObj['kg'])
	 +"</b></li><li class='col-pri1' style='border:5px solid #f9a57a;'>竣工面积<br><b>"+isNull(monthObj['jg'])+"</b></li>");
	if(data.monthlist.length==0){
		noData("chart_7");
        return;
	 }	
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		var d = data.monthlist[i];
		 if(!monthObj[d.SMONTH] ){
			monthObj[d.SMONTH] = {};
		 }
		 monthObj[d.SMONTH][d.DICT_CODE] = d.S1;
	 }
	 var  series=[{name:"面积",type:'line',data:[],yAxisIndex :1,axisLabel:{textStyle:{color:"#fff"}}},
        	{name:"套数",type:'bar',data:[],yAxisIndex:0,axisLabel:{textStyle:{color:"#fff"}},itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()},barWidth:20}}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 series[0].data.push(formatNum((monthObj[j]?monthObj[j]['S003013004002']:0)));
		 series[1].data.push(formatNum((monthObj[j]?monthObj[j]['S003013004001']:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
			legend: {data: ["套数","面积"],textStyle:{color:'#fff'}},
			calculable : true, grid:{x:50,y:40,x2:50,y2:20},
			yAxis : [{name : '套数(套)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}},
					{name:'面积(万㎡)',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
			xAxis : [{type : 'category',data:xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
			series : series
		};
 	var myChart = echarts.init(document.getElementById("chart_7"));
 	myChart.setOption(option);
 } 
 
 
 //工商注册
 function findGSZC(data){
	var monthObj = {};
	if(data.monthlist.length==0){
		noData("chart_6");
		noData("chart_6_1");
        return;
	 }
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		var d = data.monthlist[i];
		 if(!monthObj[d.SMONTH] ){
			monthObj[d.SMONTH] = {};
		 }
		 monthObj[d.SMONTH][d.DICT_CODE] = d.S1;
	 }
	 var seriesNum=[{name:"新增个体工商户",stack: '总量',type:'line',data:[], itemStyle: {normal: {areaStyle: {type: 'default'}}}},{name:"新增注册企业",type:'line',stack: '总量',data:[], itemStyle: {normal: {areaStyle: {type: 'default'}}}}],
	  seriesM=[{name:"新增个体工商户资金数额",type:'line',stack: '总量',data:[], itemStyle: {normal: {areaStyle: {type: 'default'}}}},
	  {name:"新增注册资金",type:'line',stack: '总量',data:[], itemStyle: {normal: {areaStyle: {type: 'default'}}}}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 seriesNum[1].data.push(formatNum((monthObj[j]?monthObj[j]['S003011002']:0)));
		 seriesNum[0].data.push(formatNum((monthObj[j]?monthObj[j]['S003011004']:0)));
		 seriesM[1].data.push(formatNum((monthObj[j]?monthObj[j]['S003011001']:0)));
		 seriesM[0].data.push(formatNum((monthObj[j]?monthObj[j]['S003011003']:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["新增个体工商户","新增注册企业"],textStyle:{color:'#fff'}},
	    calculable : true, grid:{x:50,y:30,x2:5,y2:30},
	    yAxis : [{name : '',type : 'value',axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : seriesNum
	};
 	var myChart = echarts.init(document.getElementById("chart_6_1"));
 	myChart.setOption(option);
	
	var myChart1 = echarts.init(document.getElementById("chart_6"));
	option.series = seriesM;
	option.legend.show = false;
	option.legend.data = ["新增个体工商户资金数额","新增注册资金"];
	option.grid.y = 10;
 	myChart1.setOption(option);
 } 
 
  //对外经济
 function findDWJJ(data){
	var monthObj = {};
	 $("#fislist_5").html("<li>本月<br><b>"+isNull(data.list[0]?data.list[0].s1:"")+"</b></li><li class='col-pri1'>本年累计<br><b>"+isNull(data.list[0]?data.list[0].s2:"")
	 +"</b></li><li class='col-pri2'>比上年<br>同期"+(parseFloat(data.list[0]?data.list[0].s3:"")>0?"增长":"亏损")+"<br><b>"+isNull(data.list[0]?data.list[0].s3:"").replace("-","")+"%</b></li>");
	if(data.monthlist.length==0){
		noData("chart_5");
        return;
	 }	
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		 monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1,s2:data.monthlist[i].S2};
	 }
	 var series=[{name:"月总额",type:'line',data:[]},
		         {name:"本年累计",type:'line',data:[]}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
		 series[1].data.push(formatNum((monthObj[j]?monthObj[j].s2:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["月总额","本年累计"],textStyle:{color:'#fff'}},
	    calculable : true, grid:{x:50,y:30,x2:5,y2:30},
	    yAxis : [{name : '万元',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : series
	};
 	var myChart = echarts.init(document.getElementById("chart_5"));
 	myChart.setOption(option);
 }

 //行政处罚
 function findXZCF(data){
     var monthObj = {};
     $("#fislist_11").html("<li>本月<br><b>"+(data.monthCount?data.monthCount:"")+"</b></li><li class='col-pri1'>本年累计<br><b>"+isNull(data.yearCount?data.yearCount:"")
             +"</b></li><li class='col-pri2'>比上月<br>"+(parseFloat(data.monthCount-data.preMonthCount)>0?"增长":"减少")+"<br><b>"+isNull(data.changeRate?data.changeRate:"")+"%</b></li>");
     if(data.monthlist.length==0){
         noData("chart_11");
         return;
     }
     for(var i=0,l=data.monthlist.length;i<l;i++){
         monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1};
     }
     var series=[{name:"处罚次数",type:'line',data:[]}],
             xAxis = [];
     for(j=1;j<13;j++){
         xAxis.push(j+'月');
         series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
     }
     var option = {
         tooltip : {trigger: 'axis'},
         legend: {data: ["处罚次数"],textStyle:{color:'#fff'}},
         calculable : true, grid:{x:50,y:30,x2:5,y2:30},
         yAxis : [{name : '次',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
         xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
         series : series
     };
     var myChart = echarts.init(document.getElementById("chart_11"));
     myChart.setOption(option);
 }
 
  //社会消费品零售总额
 function findXFP(data){
	var monthObj = {};
	 $("#fislist_4").html("<li>本月<br><b>"+isNull(data.list[1]?data.list[1].s1:"")+"</b></li><li class='col-pri1'>本年累计<br><b>"+isNull(data.list[1]?data.list[1].s2:"")
	 +"</b></li><li class='col-pri2'>比上年<br>同期"+(parseFloat(data.list[1]?data.list[1].s3:"")>0?"增长":"亏损")+"<br><b>"+isNull(data.list[1]?data.list[1].s3:"").replace("-","")+"%</b></li>");
	if(data.monthlist.length==0){
		noData("chart_4");
        return;
	 }	
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		 monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1,s2:data.monthlist[i].S2};
	 }
	 var series=[{name:"月限额",type:'line',data:[]},
		         {name:"本年累计",type:'line',data:[]}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
		 series[1].data.push(formatNum((monthObj[j]?monthObj[j].s2:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["月限额","本年累计"],textStyle:{color:'#fff'}},
	    calculable : true, grid:{x:50,y:30,x2:5,y2:30},
	    yAxis : [{name : '万元',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : series
	};
 	var myChart = echarts.init(document.getElementById("chart_4"));
 	myChart.setOption(option);
 }
 
 //固定资产投资
 function findCCTZ(data){
	var monthObj = {};
	 $("#fislist_3").html("<li>本月<br><b>"+isNull(data.list[1]?data.list[1].s1:"")+"</b></li><li class='col-pri1'>本年累计<br><b>"+isNull(data.list[1]?data.list[1].s2:"")
	 +"</b></li><li class='col-pri2'>比上年<br>同期"+(parseFloat(data.list[1]?data.list[1].s3:"")>0?"增长":"亏损")+"<br><b>"+isNull(data.list[1]?data.list[1].s3:"").replace("-","")+"%</b></li>");
	if(data.monthlist.length==0){
		noData("chart_3");
        return;
	 }	
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		 monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1,s2:data.monthlist[i].S2};
	 }
	 var series=[{name:"月投资",type:'line',data:[]},
		         {name:"本年累计",type:'line',data:[]}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
		 series[1].data.push(formatNum((monthObj[j]?monthObj[j].s2:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["月投资","本年累计"],textStyle:{color:'#fff'}},
	    calculable : true,  grid:{x:50,y:30,x2:5,y2:30},
	    yAxis : [{name : '万元',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : series
	};
 	var myChart = echarts.init(document.getElementById("chart_3"));
 	myChart.setOption(option);
 }
 
 //财政支出
 function findCZSZ(data){
	var monthObj = {};
	for(var j=0,lj=data.list.length;j<lj;j++){
		var d = data.list[j];
		if(d.dictCode == 'S003003001'){//支出
			monthObj['srxj'] = d.s1;
			monthObj['srbxj'] = d.s2;
		}else if(d.dictCode == 'S003003002'){//收入
			monthObj['zcxj'] = d.s1;
			monthObj['zcbxj'] = d.s2;
		}
    }
	 $("#fislist_2").html("<li style='width:84px;margin:0px;'>收入现价<br><b style='font-size:18px;'>"+isNull(monthObj.srxj)+
	 "</b></li><li class='col-pri1'  style='width:84px;margin:0px;'>收入不变价<br><b style='font-size:18px;'>"+isNull(monthObj.srbxj)+
	 "</b></li><li  style='width:84px;margin:0px;'>支出现价<br><b style='font-size:18px;'>"+isNull(monthObj.zcxj)+
	 "</b></li><li class='col-pri1'  style='width:84px;margin:0px;'>支出不变价<br><b style='font-size:18px;'>"+isNull(monthObj.zcbxj)+"</b></li>");
	
	 if(data.monthlist.length==0){
		noData("chart_2");
        return;
	 }			//支出			收入
	 monthObj={"S003003001":{},"S003003002":{}};
	 for(var i=0,l=data.monthlist.length;i<l;i++){
		 monthObj[data.monthlist[i].DICT_CODE][data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1,s2:data.monthlist[i].S2};
	 }
	 var series=[{name:"支出现价",type:'line',data:[]},
		         {name:"支出不变价",type:'line',data:[]},
				 {name:"收入现价",type:'line',data:[]},
		         {name:"收入不变价",type:'line',data:[]}],
		 xAxis = [];
	 for(j=1;j<13;j++){
		 xAxis.push(j+'月');
		 series[0].data.push(formatNum((monthObj["S003003001"][j]?monthObj["S003003001"][j].s1:0)));
		 series[1].data.push(formatNum((monthObj["S003003001"][j]?monthObj["S003003001"][j].s2:0)));
		 
		 series[2].data.push(formatNum((monthObj["S003003002"][j]?monthObj["S003003002"][j].s1:0)));
		 series[3].data.push(formatNum((monthObj["S003003002"][j]?monthObj["S003003002"][j].s2:0)));
	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["支出现价","支出不变价","收入现价","收入不变价"],textStyle:{color:'#fff'},x:'right'},
	    calculable : true,  grid:{x:50,y:40,x2:5,y2:30},
	    yAxis : [{name : '万元',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: xAxis ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : series
	};
 	var myChart = echarts.init(document.getElementById("chart_2"));
 	myChart.setOption(option);
 }
 
 function formatNum(n){
	 return n==null||n.length==0?0:n;
 }
//地区生产总值
 function findSCZY(data){
	 $("#fislist_1").html("<li>现价<br><b>"+isNull(data.list[0]?data.list[0].s1:"")+"</b></li><li class='col-pri1'>不变价<br><b>"+isNull(data.list[0]?data.list[0].s2:"")
	 +"</b></li><li class='col-pri2'>比上年<br>同期"+(parseFloat(data.list[0]?data.list[0].s3:"")>0?"增长":"亏损")+"<br><b>"+isNull(data.list[0]?data.list[0].s3:"").replace("-","")+"%</b></li>");
	if(data.list.length==0){
        return;
	 }
	 var series=[{name:"现价",type:'bar',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[0];})()},barWidth:20}},
	         	{name:"不变价",type:'bar',data:[],itemStyle:{normal:{color:(function(a,b,c){return colorObj[2];})()},barWidth:20}}];
	 for(var j=0,lj=data.list.length;j<lj;j++){
		var d = data.list[j];
		if(d.dictCode == 'S003001001'){
			series[0].data.push(formatNum(d.s1));
			series[1].data.push(formatNum(d.s2));
		}else if(d.dictCode == 'S003001002'){
			series[0].data.push(formatNum(d.s1));
			series[1].data.push(formatNum(d.s2));
		}else if(d.dictCode == 'S003001003'){
			series[0].data.push(formatNum(d.s1));
			series[1].data.push(formatNum(d.s2));
		}
 	 }
	 var option = {tooltip : {trigger: 'axis'},
	    legend: {data: ["现价","不变价"],textStyle:{color:'#fff'}},
	    calculable : true, grid:{x:50,y:30,x2:20,y2:30},
	    yAxis : [{name : '万元',type : 'value',axisLine:{lineStyle:{color: '#fff'}},axisLabel:{textStyle:{color:"#fff"}}}],
	    xAxis : [{type : 'category',data: ['第一产业','第二产业','第三产业'] ,axisLabel:{textStyle:{color:"#fff"}}}],
	    series : series
	};
 	var myChart = echarts.init(document.getElementById("chart_1"));
 	myChart.setOption(option);
 }

 var clickObj = {};
 var config = {id: "",width: "100%",renderTo: "",headerAlign: "center",headerHeight: "20",folderCloseIcon: "${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon29.png",
		 folderOpenIcon: "${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon29_1.png",dataAlign: "center",indentation: "10",	hoverRowBackground: "false",folderColumnIndex: "0",	expandLayer:1,
			columns:[],data:[]};
 var curruntLi={};
function yxClick(o,stype,n,level){
	showLiDiv(o, n);
	curruntLi={stype:stype,n:n,level:level?level:3};
	if(clickObj[stype]){
		return;
	}
	clickObj[stype] = 1;
	var date = document.getElementById("date_"+n).value.split("-");
	yxYearChange(o,date[0],date[1]);
}
//行政处罚
function xzcfClick(o,stype,n,level){
     showLiDiv(o, n);

     var date = document.getElementById("date_11").value.split("-");
     xzcfYearChange(o,date[0],date[1]);
 }
 function yxYearChange(o,y,m){
	var index = layer.load(0, {time: 5000});
	var stype = curruntLi.stype,n = curruntLi.n;
	if(!y){
		y = o.cal.date.y,m=o.cal.date.M;
	}
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findTreeTable.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,syear:y,smonth:m,level:curruntLi.level},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(data){
        	if(data.list.length == 0){
        		noData("chart_"+n);
        		layer.close(index);
        		//return;
        	}
			$("#grosslist_"+n).html("");
        	var title = [{dataField:'dictName',headerText:'指标',dataAlign: "left",width:130}];
        	for(var i=0,l=data.title.length;i<l;i++){
        		title.push({dataField:'s'+data.title[i].DICT_ORDERBY,headerText:data.title[i].DICT_NAME, dataAlign:'center',width:45});
        	}
        	title[3].width=70;
        	var tdata = [];
        	for(var j=0,lj=data.list.length;j<lj;j++){
        		var d = data.list[j];
				var row ={id:d.dictId,pid:d.dictPid,dictName:d.dictName,s1:isNull(d.s1),
						s2:isNull(d.s2),s3:n==8?isNull(d.s3):arrowFun(d.s3),s4:isNull(d.d4)};
				tdata.push(row);
        	}
        	config.renderTo="grosslist_"+n;
        	config.id="id_"+n;
        	config.columns=title;
        	config.data=arry2TreeFormat(tdata);
        	var treeGrid = new TreeGrid(config);
			treeGrid.show();
			
			switch(n){
				case 1: findSCZY(data);break;//地区生产总值
				case 2: findCZSZ(data);break;//财政支出
				case 3: findCCTZ(data);break;//固定资产投资
				case 4: findXFP(data);break;//社会消费品零售总额
				case 5: findDWJJ(data);break;//对外经济
				case 6: findGSZC(data);break;//工商注册
				case 7: findFDC(data);break;//房地产情况
				case 8: findCQAZ(data);break;//拆迁安置
			}
			
        	layer.close(index); 
        }
    });
 }
 //行政处罚
 function xzcfYearChange(o,y,m){

     var index = layer.load(0, {time: 5000});

     if(!y){
         y = o.cal.date.y,m=o.cal.date.M;
     }
     $.ajax({
         url: '${rc.getContextPath()}/zhsq/szzg/zgPenaltyController/findPenaltyByYM.json?t='+Math.random(),
         type: 'POST',
         data: { syear:y,smonth:m},
         dataType:"json",
         error: function(data){
             $.messager.alert('提示','信息获取异常!','warning');
             layer.close(index);
         },
         success: function(data){
             if(data.list.length == 0){
                 noData("chart_11");
                 layer.close(index);
                 //return;
             }
             var list = data.list;
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\">企业名</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\"  >决定文书号</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\"  >处罚结论</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 90px;height: 30px;font-weight: 100;padding-top: 10px;\"  >处罚日期</div></th>"
             title +="</tr>";

             $("#list_title").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].enterpriseName+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].decisionInstrument+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyConclusion+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 90px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyDateStr+"</div></td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             $("#grosslist_11").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_11").html(dataList)
             findXZCF(data);

             layer.close(index);
         }
     });
 }
function arrowFun(n){
	return n==null || n.length==0?"":(n==0?"0%":("<img src=\'${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/np_city_icon2"+(parseFloat(n)>0?7:8)+".png\'/>"+Math.abs(n)+"%"));
}
 function showLiDiv(o,n){
	$(".buildright").hide();
	$("#buildright_"+n).show();
	$("#ul_org li").removeClass("buildleft-list-dq");
	$(o).addClass("buildleft-list-dq");
 }
function isNull(n){
	return n==null || n.length==0?"":n;
}

function arry2TreeFormat(sNodes){
	var r = [];
	var tmpMap = [];
	var id="id",pid="pid",children="children";
	for (i=0, l=sNodes.length; i<l; i++) {
		tmpMap[sNodes[i][id]] = sNodes[i];
	}
	for (i=0, l=sNodes.length; i<l; i++) {
		if (tmpMap[sNodes[i][pid]] && sNodes[i][id] != sNodes[i][pid]) {
			if (!tmpMap[sNodes[i][pid]][children])
				tmpMap[sNodes[i][pid]][children] = [];
			tmpMap[sNodes[i][pid]][children].push(sNodes[i]);
		} else {
			r.push(sNodes[i]);
		}
	}
	return r;
}
</script>
</html>
