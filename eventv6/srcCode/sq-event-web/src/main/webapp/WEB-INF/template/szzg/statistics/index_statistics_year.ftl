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
				<li onclick="yxClick(this,'s9',9)">楼宇经济</li>
                <li onclick="yxClick(this,'s10',10)">城市建设</li>
                <li onclick="yxClick(this,'s12',12)">进区项目</li> 
      
              </ul>
            </div>
          </div>
          
          
         
          
   <!-- 楼宇经济 -->
            <div class="buildright" id="buildright_9">
                <div class="fisright-chart1">
                    <h5>楼宇经济概况<span class="fr">单位：个</span></h5>
                    <div class="fisdate" >
                        <input type="text" class="inp1 Wdate timeClass" id="date_9"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff;"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:yxYearChange9,Mchanged:yxYearChange9});" >
                    </div><!--end .fisdate-->
                    <div class="fislist" >
                        <ul id="fislist_9" >
                        </ul>
                        <div class="clearfloat"></div>
                    </div><!--end .fislist-->
                    <h5>历年发展趋势</h5>
                    <div class="gross-chart" id="chart_9" style="width:390px;height:180px;"></div>
                    <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
                </div><!--end .fisright-chart1-->
                <div class="fisright-chart2">
                    <h5>楼宇经济报表</h5>
                    <div id="list_title_9">
                    </div><!--end .fislist-->
                    <div class="list_div" style="height: 310px;">
                        <div class="grosslist"  id="grosslist_9">
                        </div>
                    </div><!--end .fislist-->
                </div><!--end .fisright-chart2-->
            </div><!--end .buildright-->
          
		  
		  
		          
   <!-- 城市建设 -->
            <div class="buildright" id="buildright_10">
                <div class="fisright-chart1">
                    <h5>城市建设概况</h5>
                    <div class="fisdate">
                        <input type="text" class="inp1 Wdate timeClass" id="date_10"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:yxYearChange10,Mchanged:yxYearChange10});" >
                    </div><!--end .fisdate-->
                    <div class="fislist">
                        <ul id="fislist_10" >
                        </ul>
                        <div class="clearfloat"></div>
                    </div><!--end .fislist-->
                    <h5>历年发展趋势</h5>
                    <div class="gross-chart" id="chart_10" style="width:390px;height:180px;"></div>
                    <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
                </div><!--end .fisright-chart1-->
                <div class="fisright-chart2">
                    <h5>城市建设报表</h5>
                    <div id="list_title_10">
                    </div><!--end .fislist-->
                    <div class="list_div" style="height: 310px;">
                        <div class="grosslist"  id="grosslist_10">
                        </div>
                    </div><!--end .fislist-->
                </div><!--end .fisright-chart2-->
            </div><!--end .buildright-->
            
              
                         
   <!-- 进区项目 -->
            <div class="buildright" id="buildright_12">
                <div class="fisright-chart1">
                    <h5>进区项目概况<span class="fr">单位：个</span></h5>
                    <div class="fisdate">
                        <input type="text" class="inp1 Wdate timeClass" id="date_12"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
                               onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:yxYearChange12,Mchanged:yxYearChange12});" >
                    </div><!--end .fisdate-->
                    <div class="fislist">
                        <ul id="fislist_12" >
                        </ul>
                        <div class="clearfloat"></div>
                    </div><!--end .fislist-->
                    <h5>历年发展趋势</h5>
                    <div class="gross-chart" id="chart_12" style="width:390px;height:180px;"></div>
                    <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
                </div><!--end .fisright-chart1-->
                <div class="fisright-chart2">
                    <h5>进区项目报表</h5>
                    <div id="list_title_12">
                    </div><!--end .fislist-->
                    <div class="list_div" style="height: 310px;">
                        <div class="grosslist"  id="grosslist_12">
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
 
 
 
 

 

 function findXZCF9(data){
     var monthObj = {};
     $("#fislist_9").html("<li class='col-pri1' >入驻企业数<br><b>"+isNull(data.yearCount?data.yearCount:"") +"</b></li>");
    // if(data.monthlist.length==0){
      //   noData("chart_9");
       //  return;
    // }
     //for(var i=0,l=data.monthlist.length;i<l;i++){
     //    monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1};
 //    }
     var series=[ {
            name:'企业区域占比',
            type:'line',
            stack: '总量',
            data:[0.4,0.25,0.5,0.8,0.2,0.8,0.45]
        },
        {
            name:'税收收入占比',
            type:'line',
            stack: '总量',
            data:[0,0.14,0.3,0.5,0.1,0.2,0.10]
        }],
             xAxis = [];
   //  for(j=1;j<13;j++){
     //    xAxis.push(j+'月');
      //   series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
   //  }
     var option = {
         tooltip : {trigger: 'axis'},
         legend: {data: ["企业区域占比","税收收入占比"],textStyle:{color:'#fff'}},
           calculable : true, grid: {
                x:32,
                y:20,
                width:300,
                height:130
                //borderWidth:0
            },
         yAxis : [{ type: 'bar', axisLine:{lineStyle:{color: '#fff'}},  
            axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                },axisLabel:{textStyle:{color:"#fff"}}}],
         xAxis : [{type : 'category',data: ['2013', '2014', '2015', '2016', '2017', '2018', '2019'] ,  
            axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                },axisLabel:{textStyle:{color:"#fff"}}}],
         series : series
     };
     var myChart = echarts.init(document.getElementById("chart_9"));
     myChart.setOption(option);
 }
 
 
 
  function findXZCF10(data){
     var monthObj = {};
   $("#fislist_10").html("<li>施工面积<br><b>"+(data.monthCount?data.monthCount:"")+"</b></li><li class='col-pri1'>拆迁面积<br><b>"+isNull(data.yearCount?data.yearCount:"")+"</b></li><li >固定投资<br><b>"+isNull(data.changeRate?data.changeRate:"")+"</b></li>");
    // if(data.monthlist.length==0){
      //   noData("chart_9");
       //  return;
    // }
     //for(var i=0,l=data.monthlist.length;i<l;i++){
     //    monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1};
 //    }
     var series= [
        {
            name:'施工面积',
            type:'bar',
            data:[20, 49, 23, 25, 32.6,  33]
        },
        {
            name:'拆迁面积',
            type:'bar',
            data:[26,26.4, 28.7,  48.7, 18.8,26]
        },
        {
            name:'固定投资',
            type:'line',
            yAxisIndex: 1,
            data:[20, 22, 33, 45, 23.4, 23]
        }
    ]
        
             xAxis = [];
   //  for(j=1;j<13;j++){
     //    xAxis.push(j+'月');
      //   series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
   //  }
     var option = {
         tooltip : {trigger: 'axis'},
         legend: {data: ["施工面积","拆迁面积","固定投资"],textStyle:{color:'#fff'}},
           calculable : true, grid: {
                x:52,
                y:20,
                width:280,
                height:130
                //borderWidth:0
            },
         yAxis : [ {
            type: 'value',
            min: 10,
            max: 50,
            interval: 50,
            axisLabel: {
             textStyle: {
                        color: '#ffffff'
                    },
                formatter: '{value} 万m²'
            },  
            axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                }  
        },
        {
            type: 'value',
            min: 10,
            max: 50,
            interval: 5,
            axisLabel: { 
            textStyle: {
                        color: '#ffffff'
                   },
                formatter: '{value} 亿元'
            },  
            axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                }
        }],
         xAxis : [{type : 'category',data: [ '2014', '2015', '2016', '2017', '2018', '2019'] ,  
                axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                }, axisLabel:{textStyle:{color:"#fff"}}
                }],
         series : series
     };
     var myChart = echarts.init(document.getElementById("chart_10"));
     myChart.setOption(option);
 }
 
 
 
 function findXZCF12(data){
     var monthObj = {};
   $("#fislist_12").html("<li>在建项目<br><b>"+(data.monthCount?data.monthCount:"")+"</b></li><li class='col-pri1'>完成项目<br><b>"+isNull(data.yearCount?data.yearCount:"")
             +"</b></li><li >环比去年<br>同期<br><b>"+isNull(data.changeRate?data.changeRate:"")+"</b></li>");
    // if(data.monthlist.length==0){
      //   noData("chart_9");
       //  return;
    // }
     //for(var i=0,l=data.monthlist.length;i<l;i++){
     //    monthObj[data.monthlist[i].SMONTH] = {s1:data.monthlist[i].S1};
 //    }
     var series= [{
                name: '项目新增情况',
                type: 'line',
                smooth: true,
                symbol: 'circle', // 拐点类型
                symbolSize: 0, // 拐点圆的大小
                itemStyle: {
                    normal: {
                        color: '#289df5', // 折线条的颜色
                        borderColor: '#289df5', // 拐点边框颜色
                        areaStyle: {
                            type: 'default',
                            opacity: 0.1
                        }
                    }
                },
                data: [5, 4, 9, 5, 8, 3, 2]
            } 
    ]
        
             xAxis = [];
   //  for(j=1;j<13;j++){
     //    xAxis.push(j+'月');
      //   series[0].data.push(formatNum((monthObj[j]?monthObj[j].s1:0)));
   //  }
     var option = {
      color: ['#289df5'],
            grid: {
                x:25,
                y:15,
                width:340,
                height:140
                //borderWidth:0
            },
            xAxis: {
                type: 'category',
                axisLine: {
                    show: false
                },
                axisTick: {
                    length: 0
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLabel: {
                    interval: 0,
                    textStyle: {
                        color: '#ffffff'
                    }
                },
                name: '(年)',
                nameTextStyle: {
                    padding: [24, 0, 0, 0],
                    color: '#a3a4b2'
                },
                boundaryGap:false,
                data: ['2013', '2014', '2015', '2016', '2017', '2018', '2019']
            },
            yAxis: {
                type: 'value',
                axisLine: {
                    show: false
                },
                axisTick: {
                    length: 0 // 刻度线的长度
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: ["#051d5f"],
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLabel: {
                    textStyle: {
                        color: '#ffffff'
                    }
                }
            },
            series:series
     };
     var myChart = echarts.init(document.getElementById("chart_12"));
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

	switch(n){
				case 9: yxYearChange9(o,date[0],date[1]);break;
				case 10: yxYearChange10(o,date[0],date[1]);break;//财政支出
				case 12: yxYearChange12(o,date[0],date[1]);break;//固定资产投资
				
	}

	
	
}



function yxYearChange9(o,y,m){
	var index = layer.load(0, {time: 5000});
	var stype = curruntLi.stype,n = curruntLi.n;
	if(!y){
		y = o.cal.date.y;
	}
	$.ajax({
	 url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findTreeTable.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,syear:y,level:curruntLi.level},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
         success: function(data){
         
         
            data={
			   list:[
			        
			        {
			        area:'万宁市',
			        decisionInstrument:1000,
			        penaltyConclusion:'100%',
			        penaltyDateStr:'100%'
			        },
			        {
			        area:'万城镇',
			        decisionInstrument:150,
			        penaltyConclusion:'15%',
			        penaltyDateStr:'3%'
			        },
			        {
			        area:'东澳镇',
			        decisionInstrument:160,
			        penaltyConclusion:'16%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'礼纪镇',
			        decisionInstrument:140,
			        penaltyConclusion:'14%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'南桥镇',
			        decisionInstrument:130,
			        penaltyConclusion:'13%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'三更罗镇',
			        decisionInstrument:70,
			        penaltyConclusion:'7%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'国营东兴农场',
			        decisionInstrument:20,
			        penaltyConclusion:'2%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'国营东和农场',
			        decisionInstrument:20,
			        penaltyConclusion:'2%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'国营新中农场',
			        decisionInstrument:10,
			        penaltyConclusion:'1%',
			        penaltyDateStr:'1%'
			        },
			        {
			        area:'兴隆华侨农场',
			        decisionInstrument:10,
			        penaltyConclusion:'1%',
			        penaltyDateStr:'1%'
			        } ,
			        {
			        area:'地方国营六连林场',
			        decisionInstrument:10,
			        penaltyConclusion:'1%',
			        penaltyDateStr:'1%'
			        } ,
			        {
			        area:'新海社区居委会',
			        decisionInstrument:30,
			        penaltyConclusion:'3%',
			        penaltyDateStr:'1%'
			        } ,
			        {
			        area:'龙滚镇',
			        decisionInstrument:50,
			        penaltyConclusion:'5%',
			        penaltyDateStr:'2%'
			        } ,
			        {
			        area:'和乐镇',
			        decisionInstrument:40,
			        penaltyConclusion:'4%',
			        penaltyDateStr:'2%'
			        } ,
			        {
			        area:'后安镇',
			        decisionInstrument:40,
			        penaltyConclusion:'4%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'大茂镇',
			        decisionInstrument:20,
			        penaltyConclusion:'2%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'长丰镇',
			        decisionInstrument:50,
			        penaltyConclusion:'5%',
			        penaltyDateStr:'2%'
			        },
			        {
			        area:'山根镇',
			        decisionInstrument:50,
			        penaltyConclusion:'5%',
			        penaltyDateStr:'2%'
			        }
			    ],
			    yearCount: 1000,
			    monthlist:[]
			   
			};
             $("#grosslist_"+n).html("");
             var list = data.list;
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\">区域</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\"  >企业数量</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 110px;height: 30px;font-weight: 100;text-align: center\"  >企业占比</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 90px;height: 30px;font-weight: 100;padding-top: 10px;\"  >税收占比</div></th>"
             title +="</tr>";

             $("#list_title_9").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].area+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].decisionInstrument+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 110px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyConclusion+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 90px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyDateStr+"</div></td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             $("#grosslist_9").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_9").html(dataList)
             findXZCF9(data);

             layer.close(index);
         }
     });
	

}
   

function yxYearChange10(o,y,m){
	var index = layer.load(0, {time: 5000});
	var stype = curruntLi.stype,n = curruntLi.n;
	if(!y){
		y = o.cal.date.y;
	}
	$.ajax({
	 url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findTreeTable.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,syear:y,level:curruntLi.level},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
         success: function(data){
         
         
            data={
			   list:[
			        {
			        area:'施工面积',
			        penaltyConclusion:'33万m²',
			        penaltyDateStr:'10%'
			        },
			        {
			        area:'拆迁面积',
			        penaltyConclusion:'26万m²',
			        penaltyDateStr:'46%'
			        },
			        {
			        area:'固定投资',
			        penaltyConclusion:'23亿元',
			        penaltyDateStr:'10%'
			        }
			    
			    ],topArr:[
		     	{type:'施工面积',count:'80平方'},
			   {type:'拆迁面积',count:'200平方'},
			  {type:'固定投资',count:'120亿'}
	       	],
			    monthCount: '80平方',
			    yearCount:'200平方',
			    changeRate:'6亿',
			    monthlist:[]
			   
			};
             $("#grosslist_"+n).html("");
             var list = data.list;
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 140px;height: 30px;font-weight: 100;text-align: center\">指标</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 140px;height: 30px;font-weight: 100;text-align: center\"  >本年累计</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 140px;height: 30px;font-weight: 100;padding-top: 10px;\"  >环比去年增长</div></th>"
             title +="</tr>";

             $("#list_title_10").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 140px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].area+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 140px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyConclusion+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 140px;height:font-weight: 100;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyDateStr+"</div></td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             $("#grosslist_10").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_10").html(dataList)
             findXZCF10(data);

             layer.close(index);
         }
     });
	

}
   function yxYearChange12(o,y,m){
	var index = layer.load(0, {time: 5000});
	var stype = curruntLi.stype,n = curruntLi.n;
	if(!y){
		y = o.cal.date.y;
	}
	$.ajax({
	 url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findTreeTable.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,syear:y,level:curruntLi.level},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
         success: function(data){
         
         
            data={
			   list:[
			        {
			        area:'1月',
			        decisionInstrument:1,
			        penaltyConclusion:1,
			        penaltyDateStr:'48%'
			        },
			        {
			        area:'2月',
			        decisionInstrument:2,
			        penaltyConclusion:2,
			        penaltyDateStr:'50%'
			        },
			        {
			        area:'3月',
			        decisionInstrument:2,
			        penaltyConclusion:2,
			        penaltyDateStr:'52%'
			        },
			        {
			        area:'4月',
			        decisionInstrument:3,
			        penaltyConclusion:3,
			        penaltyDateStr:'55%'
			        },
			        {
			        area:'5月',
			        decisionInstrument:2,
			        penaltyConclusion:2,
			        penaltyDateStr:'45%'
			        }
			    
			    ],topArr:[
		     	{type:'在建项目',count:'3'},
			   {type:'完成项目',count:'3'},
			  {type:'环比去年同期增长',count:'70%'}
			  ],
			    monthCount: '2',
			    yearCount:'2',
			    changeRate:'50%',
			    monthlist:[]
			   
			};
             $("#grosslist_"+n).html("");
             var list = data.list;
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 90px;height: 30px;font-weight: 80;text-align: center\">月份</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 95px;height: 30px;font-weight: 80;text-align: center\"  >在建项目</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 95px;height: 30px;font-weight: 80;text-align: center\"  >完成项目</div></th>"
             title +="<th style=\"border: 1px solid #6E8CA4;\"><div  style=\" width: 140px;height: 30px;font-weight: 140;padding-top: 10px;\"  >环比去年同期增长</div></th>"
             title +="</tr>";

             $("#list_title_12").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 90px;height:font-weight: 80;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].area+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 95px;height:font-weight: 80;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].decisionInstrument+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 95px;height:font-weight: 80;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyConclusion+"</div></td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;\"><div style=\"width: 140px;height:font-weight: 140;padding-top: 10px;word-wrap: break-word;text-align:center;\">"+list[i].penaltyDateStr+"</div></td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             $("#grosslist_12").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_12").html(dataList)
             findXZCF12(data);

             layer.close(index);
         }
     });
	

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


</script>
</html>
