<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>小学就读分析</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    .image_hand{cursor:pointer;}
    .list_div{height:360px;}
    .buildleft-list ul li{cursor:pointer;}
    .right_content{overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
    .topDiv{width:120px;height:70px;float:left;}
    .topDiv div{width:134px;}
.topEchart{width:120px;height:100px;}
.topName{height:20px;font-family:'Arial Normal','Arial';font-weight:400;font-style:normal;font-size:14px;color: #ffffff;text-align:center;line-height: normal;}
.fisright-chart1 {
    height: 384px;
}
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
                <li onclick="yxClick(this,'s1',1)">小学就读分析</li>
                
              </ul>
            </div>
          </div>
			<!-- 小学就读分析 -->
          <div class="buildright" id="buildright_1">
            <div class="fisright-chart1">
                <h5>小学就读概况<span class="fr">单位：名</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_1"  value="2019"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy',ychanged:yxYearChange,Mchanged:yxYearChange});" >
                </div><!--end .fisdate-->
                
                <div style="width:398px;height:125px;overflow:hidden;" region="north">
<div class="topDiv">
	<div class="topName zhanbi" style="left:0px;top:50px" id="zhanbi_1"></div>
	<div class="topEchart" id="topEchart_1"></div>
</div>
<div class="topDiv">
	<div class="topName zhanbi" style="left:178px;" id="zhanbi_2"></div>
	<div class="topEchart" id="topEchart_2"></div>
</div>
<div class="topDiv">
	<div class="topName zhanbi" style="left:356px;" id="zhanbi_3"></div>
	<div class="topEchart" id="topEchart_3"></div>
</div>

</div>
                
                
                <h5>历年发展趋势</h5>
                <div class="gross-chart" id="chart_1" style="width:380px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2" >
                <h5>小学就读区域分析<span class="fr">单位：名</span></h5>
                <div id="list_title"></div>
                <div class="list_div" style="height:319px">
	                <div class="grosslist" id="grosslist_1" style="">
	                
	                </div><!--end .fislist-->
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

//地区生产总值
 function findXXJD(data){
	         var list = data.list;
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 109px;height:30px\">区域</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 110px;height:30px\">本年新增适<br/>龄儿童</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 110px;height:30px\">去年新增适龄<br/>儿童</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 110px;height:30px\">环比去年同期增长</th>"
             title +="</tr>";

             $("#list_title").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 110px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].area+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 110px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].newAge+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 110px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].lastAge+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 110px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].ring+"%</td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             //$("#grosslist_1").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_1").html(dataList)
	 loadTop();
	 development();//绘制面积图
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

 function yxYearChange(o,y,m){
	var index = layer.load(0, {time: 5000});
	var stype = curruntLi.stype,n = curruntLi.n;
	if(!y){
		y = o.cal.date.y,m=o.cal.date.M;
	}
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/findTreeTable.json?t='+Math.random(),
        type: 'POST',
        data: { stype : stype,syear:y,smonth:6,level:curruntLi.level},
        dataType:"json",
        error: function(data){
        	 $.messager.alert('提示','信息获取异常!','warning');
         	layer.close(index);
        },
        success: function(datajson){
        	//if(data.list.length == 0){
        	//	noData("chart_"+n);
        	//	layer.close(index);
        		//return;
        	//}
			$("#grosslist_"+n).html("");
//万宁市
//万城镇
//东澳镇
//礼纪镇
//北大镇
//南桥镇
//三更罗镇
//国营东兴农场
//国营东和农场
//国营新中农场
//兴隆华侨农场
//地方国营六连林场
//新海社区居委会
//龙滚镇
//和乐镇
//后安镇
//大茂镇
//长丰镇
//山根镇

			
			var data={
			    title:[
			        {
			        dataField:'newSchoolAgeChildren',
			        headerText:'本年新增适龄儿童'
			        },
			        {
			        dataField:'lastSchoolAgeChildren',
			        headerText:'去年新增适龄儿童'
			        },
			        {
			        dataField:'环比去年同期增长',
			        headerText:'本年新增适龄儿童'
			        }
			    ],
			    list:[
			        {
			        area:'万城镇',
			        newAge:976,
			        lastAge:875,
			        ring:(((976-875)/875)*100).toFixed(0)
			        },
			        {
			        area:'东澳镇',
			        newAge:675,
			        lastAge:590,
			        ring:'16'
			        },
			        {
			        area:'礼纪镇',
			        newAge:1305,
			        lastAge:1243,
			        ring:'5'
			        },
			        {
			        area:'北大镇',
			        newAge:965,
			        lastAge:870,
			        ring:'7'
			        },
			        {
			        area:'南桥镇',
			        newAge:1298,
			        lastAge:1078,
			        ring:'12'
			        },
			        {
			        area:'三更罗镇',
			        newAge:1654,
			        lastAge:1368,
			        ring:'14'
			        },
			        {
			        area:'国营东兴农场',
			        newAge:457,
			        lastAge:390,
			        ring:'15'
			        },
			        {
			        area:'国营东和农场',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'国营新中农场',
			        newAge:1654,
			        lastAge:1368,
			        ring:'14'
			        },
			        {
			        area:'兴隆华侨农场',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'地方国营六连林场',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'新海社区居委会',
			        newAge:1654,
			        lastAge:1368,
			        ring:'14'
			        },
			        {
			        area:'龙滚镇',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'和乐镇',
			        newAge:1009,
			        lastAge:906,
			        ring:'10'
			        },
			        {
			        area:'后安镇',
			        newAge:1654,
			        lastAge:1368,
			        ring:'14'
			        },
			        {
			        area:'大茂镇',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'长丰镇',
			        newAge:897,
			        lastAge:840,
			        ring:'6'
			        },
			        {
			        area:'山根镇',
			        newAge:987,
			        lastAge:876,
			        ring:'8'
			        }
			    
			    ]
			   
			};
			
        	
			
			switch(n){
				case 1: findXXJD(data);break;//小学就读情况
			}
			
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

		var topArr=[
			{type:'就读率',count:8945},
		
			{type:'适龄儿童',count:8945},
		
			{type:'就读儿童',count:8537}
		],
		eventCount = [8537,8945,8537];
function loadTop(){
		
		for(var i=0;i<topArr.length;i++){
			var option = {tooltip : {show:false,trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"},title:{ text: '',textStyle:{color:'#fff'}},
			legend: {show:false,data:[topArr[i].type,'事件其他数量']},toolbox: {show : true},calculable : false,
			series : [{name:'事件',type:'pie',radius:['80%','100%'],itemStyle:{normal:{label:{show:false},labelLine:{show:false}}},
			data:[{name:topArr[i].type,value:topArr[i].count},{name:'事件其他数量',value:(eventCount[i]-topArr[i].count)}]}],color:['#2ec7c9','#b6a2de']};
			var myChart = echarts.init(document.getElementById('topEchart_'+(i+1)));
			myChart.setOption(option);
			$("#topName_"+(i+1)).html(topArr[i].type);
			$("#zhanbi_"+(i+1)).html("<p style='padding-top:55px'>"+topArr[i].type+"</p><span>"+(i==0?((topArr[2]["count"])/(topArr[1].count)*100).toFixed(1)+"%":eventCount[i])+"</span>");
		}
		
	}
	
	function development(){
	    var mjBoxEchart = echarts.init(document.getElementById('chart_1'));
        // 指定相关的配置项和数据
        var mjBoxOption = {
            
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
                },
                min:0,
                max:100
            },
            series: [{
                name: '小学就读情况',
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
                data: [85, 88, 97, 94, 89, 96, 93]
            }]
        };
 
        // 使用制定的配置项和数据显示图表
        mjBoxEchart.setOption(mjBoxOption);
        // echart图表自适应
        window.addEventListener("resize", function() {
            zxBoxEchart.resize();
        });

	}



</script>
</html>
