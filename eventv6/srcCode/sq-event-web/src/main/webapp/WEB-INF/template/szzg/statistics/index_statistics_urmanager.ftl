<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>城管事件分析</title>
		<#include "/szzg/common.ftl" />
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    .image_hand{cursor:pointer;}
    .list_div{height:360px;}
    .buildleft-list ul li{cursor:pointer;}
    .right_content{overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
    .topDiv{width:93px;height:70px;float:left;}
    .topDiv div{width:93px;}
.topEchart{width:93px;height:100px;}
.topName{height:20px;font-family:'Arial Normal','Arial';font-weight:400;font-style:normal;font-size:14px;color: #ffffff;text-align:center;line-height: normal;}
.fisright-chart1 {
    height: 384px;
}
.fisright-chart2 {
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
                <li onclick="yxClick(this,'s1',1)">城管事件分析</li>
                
              </ul>
            </div>
          </div>
			<!-- 城管事件分析 -->
          <div class="buildright" id="buildright_1">
            <div class="fisright-chart1" >
                <h5>累计处理事件<span class="fr">单位：件</span></h5>
                <div class="fisdate">
                  <input type="text" class="inp1 Wdate timeClass" id="date_1"  value="${currentDate}"  style="width:90px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M',ychanged:yxYearChange,Mchanged:yxYearChange});" >
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
<div class="topDiv">
	<div class="topName zhanbi" style="left:534px;" id="zhanbi_4"></div>
	<div class="topEchart" id="topEchart_4"></div>
</div>

</div>
                
                <h5>高发问题TOP5</h5>
                <div class="gross-chart" id="chart_1" style="width:380px;height:180px;"></div>
                <!--图表颜色：#7970f3 #64e2ad，图表线：#8694a9-->
            </div><!--end .fisright-chart1-->
            <div class="fisright-chart2" >
                <h5>城管区域事件分析<span class="fr">单位：件</span></h5>
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
	         for(var i=0,j=list.length;i<j;i++){
	             list[i].thisMonth=((((Math.random())*100))+100).toFixed(0);
			     list[i].thisYear=((((Math.random())*100))+1000).toFixed(0);
			     list[i].ring=((((Math.random())*10))+5).toFixed(0);
	         }
             var title ="";
             title += "<tr>";
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 100px;height:30px\">区域</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 100px;height:30px\">本月新增</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 100px;height:30px\">本年累计</th>"
             title +="<th style=\"border: 1px solid #6E8CA4;text-align: center;vertical-align:middle;width: 120px;height:30px\">环比去年同期增长</th>"
             title +="</tr>";

             $("#list_title").html(title);

             var dataList = "";
             for(var i=0;i<list.length;i++){
                 dataList +="<tr>";
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 100px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].area+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 100px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].thisMonth+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 100px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].thisYear+"</td>"
                 dataList +="<td style=\"border: 1px solid #6E8CA4;width: 120px;height:30px;font-weight: 100;word-wrap: break-word;text-align:center;vertical-align:middle\">"+list[i].ring+"%</td>"
                 dataList +="</tr>"
             }
             //添加滚动条样式
             //$("#grosslist_1").mCustomScrollbar({theme: "minimal-dark"});
             $("#grosslist_1").html(dataList);
             
             loadTop();
             buildtop5();
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
        data: { stype : stype,syear:y,smonth:m,level:curruntLi.level},
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
			
			var data={
			    title:[
			        {
			        dataField:'newSchoolAgeChildren',
			        headerText:'本月新增'
			        },
			        {
			        dataField:'lastSchoolAgeChildren',
			        headerText:'本月累计'
			        },
			        {
			        dataField:'ring',
			        headerText:'环比去年同期增长'
			        }
			    ],
			    list:[
			        {
			        area:'万城镇',
			        thisMonth:132,
			        thisYear:189,
			        ring:'35'
			        },
			        {
			        area:'东澳镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'礼纪镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'北大镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'南桥镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'三更罗镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'国营东兴农场',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'国营东和农场',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'国营新中农场',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'兴隆华侨农场',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'地方国营六连林场',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'新海社区居委会',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'龙滚镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'和乐镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'后安镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'大茂镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'长丰镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
			        },
			        {
			        area:'山根镇',
			        thisMonth:200,
			        thisYear:300,
			        ring:'37'
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
			{type:'结案率',count:58},
		
			{type:'上报数',count:10924},
		
			{type:'立案数',count:9815},
			
			{type:'结案数',count:8762}
		],
		eventCount = [64,10924,9815,8762];
function loadTop(){
		
		for(var i=0;i<topArr.length;i++){
			var option = {tooltip : {show:false,trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"},title:{ text: '',textStyle:{color:'#fff'}},
			legend: {show:false,data:[topArr[i].type,'事件其他数量']},toolbox: {show : true},calculable : false,
			series : [{name:'事件',type:'pie',radius:['60%','80%'],itemStyle:{normal:{label:{show:false},labelLine:{show:false}}},
			data:[{name:topArr[i].type,value:topArr[i].count},{name:'事件其他数量',value:(eventCount[i]-topArr[i].count)}]}],color:['#2ec7c9','#b6a2de']};
			var myChart = echarts.init(document.getElementById('topEchart_'+(i+1)));
			myChart.setOption(option);
			$("#topName_"+(i+1)).html(topArr[i].type);
			$("#zhanbi_"+(i+1)).html("<p style='padding-top:55px'>"+topArr[i].type+"</p><span>"+(i==0?(topArr[i].count/eventCount[i]*100).toFixed(1)+"%":eventCount[i])+"</span>");
		}
		
	}
	
	
	function buildtop5(){
	
	data=[
	{
	GRID_NAME:'市容管理',
	FINISH_VALUE:81,
	DISPUTE_VALUE:102,
	RATE:81
	},
	{
	GRID_NAME:'施工管理',
	FINISH_VALUE:75,
	DISPUTE_VALUE:98,
	RATE:76
	},
	{
	GRID_NAME:'安全管理',
	FINISH_VALUE:65,
	DISPUTE_VALUE:99,
	RATE:65
	},
	{
	GRID_NAME:'违章管理',
	FINISH_VALUE:62,
	DISPUTE_VALUE:98,
	RATE:61
	},
	{
	GRID_NAME:'打架斗殴',
	FINISH_VALUE:59,
	DISPUTE_VALUE:99,
	RATE:59
	
	}
	
	
	];
	
	var html = '', level,levelHtml='',nameList=[],dataList=[],chaList=[],maxNameLength=0;
			for(var i=0,l=data.length;i<l;i++){
				if(data[i].GRID_NAME.length*21 > maxNameLength){
					maxNameLength = data[i].GRID_NAME.length*21;
				}
				var j = l-1-i;
				nameList[j]=(i+1)+' '+data[i].GRID_NAME;
				var d = {name:data[i].GRID_NAME,value:data[i].RATE,value1:data[i].FINISH_VALUE,value2:data[i].DISPUTE_VALUE};   
				if(data[i].RATE<14){
					d['itemStyle']={normal:{label:{position:'right'}}};
					//d['label']={position:'insideLeft'};
				}
				dataList[j]=d;
				chaList[j] = {name:data[i].GRID_NAME,value:(100-data[i].RATE),value1:data[i].FINISH_VALUE,value2:data[i].DISPUTE_VALUE};
				level = parseInt(data[i].LEVEL_);
				
			}
			
	
	
	
	var flag=4;//防止顺序颠倒
	var option = {title:{},grid:{x:maxNameLength,y:0,x2:0,y2:0,borderWidth:0},
			tooltip:{
				formatter:function(params){
					return params.data.value1+'(件)/'+params.data.value2+'(件)';
				}							   										
			},legend:{data:['结案率','差额'],show:false},toolbox:{show:false},calculable : true,
					  xAxis : [ { type : 'value',show:false,min:0,max:100}]
					  ,yAxis : [{ type : 'category',axisLine:{show:false},
					  splitLine:{show:false},axisTick:{show:false},axisLabel:{textStyle:{color:'#FFFFFF',fontSize:14,fontWeight:'bold'}},
								data:nameList}],
					series:[{name:'结案率',type:'bar',stack: '1',barGap:1,barCategoryGap:1,barWidth:15,itemStyle:{normal:{color:'#668ed7',barBorderRadius:0
						   ,label:{show:true,position:'insideRight',formatter:function(params){
							   											    var result =data[flag].RATE+'%';
							   											    flag-=1;
							   												return result;
						   											},
							   textStyle:{color:'#fff',fontSize:14}}}},data:dataList},
					  {name:'差额',type:'bar',stack: '1',tooltip:{
					  formatter:function(params){
					  	if(params.value==100){
							return params.data.value1+'(件)/'+params.data.value2+'(件)';
						}
						else{
							return '';
						}	  	
					  }
				},itemStyle:{barBorderColor:'rgba(51, 105, 255, 0.3)',color:' rgba(30, 99, 255, 0.05)',
						 normal:{barBorderWidth:1,barBorderColor:'rgba(51, 105, 255, 0.3)',color:' rgba(30, 99, 255, 0.05)',barBorderRadius:0,label:{show:false}}},data:chaList}]};			 
			var chart = echarts.init(document.getElementById("chart_1"));
			chart.setOption(option);
	
	}
	
	


</script>
</html>
