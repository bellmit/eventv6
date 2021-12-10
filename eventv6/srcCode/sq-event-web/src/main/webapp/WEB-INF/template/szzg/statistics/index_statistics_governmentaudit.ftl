<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>政务审核分析</title>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/grayred/easyui.css">
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/grayred/normal.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/grayred/easyuiExtend.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/grayred/jquery.mCustomScrollbar.css" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css">

<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>




<script type="text/javascript" src="${uiDomain!''}/js/jquery.easyui.patch.js"></script><!--�����޸�easyui-1.4��easyui-numberboxʧȥ�����������С���������-->

<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/layer/layer.js"></script>

<script type="text/javascript" src="${uiDomain!''}/js/echarts/echarts.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/TreeGrid.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>

  <style type="text/css">
    body{color:#fff;}
    </style>
    
<script type="text/javascript">
function noData(id){
	var div = document.getElementById(id);
	var w = div.style.width,h = div.style.height;
	if(w.length == 0 || h.length == 0){
		w = "100px",h = "120px";
	}
	div.innerHTML = "<div style='width:"+w+";height:"+h+";vertical-align: middle;text-align:center;display: table-cell;'><img src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
}
</script>
  <style type="text/css">
    .npcityBottom .bpscon-tit li{padding:19px 14px;}
    .image_hand{cursor:pointer;}
    .list_div{height:360px;}
    .buildleft-list ul li{cursor:pointer;}
    .right_content{overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
    .topDiv{width:250px;height:110px;float:left;padding-button:20px}
    .topDiv div{width:300px;}
.topEchart{width:240px;height:110px;}
.topName{height:20px;font-family:'Arial Normal','Arial';font-weight:400;font-style:normal;font-size:14px;color: #ffffff;text-align:center;line-height: normal;}

.fx_event_flex{
	display: -webkit-flex;
	display: flex;
	-webkit-align-items: center;
	align-items: center;
}

.fx_event_flex>b>b {
	display: inline-block;
    margin-right: 6px;
    width: 31px;
	height: 43px;
	line-height: 48px;
	font-size: 28px;
	background-color: rgba(108, 166, 205, 0.6);
	border: solid 1px rgba(108, 166, 205);
    text-align: center;
    color: #fff;
    font-family: '造字工房明黑';
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
                <li onclick="yxClick(this,'s1',1)">政务审核分析</li>
               
              </ul>
            </div>
          </div>
			<!-- 政务审核分析 -->
          <div class="buildright" id="buildright_1">
            <div class="fisright-chart1" style="width:835px;height:384px">
                <h5 style="height:15px;padding-top:10px"><div style="float:left;padding-top:10px"><b style="padding-top:40px;padding-left:100px;font-size:16px">累计审批&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></div><div class="fenx_c_t fenx_c_tc" style="float:left;pading-button:20px">
                <div class="fx_event fx_event1">
                <div class="fx_event_flex" >
                <b id="total_count" ></b>&nbsp;
                </div>
                </div>
                
                </div>
                
                <div style="float:left;padding-top:10px"><b>万条</b></div>
                <div style="float:right;;padding-top:10px"><span class="fr" style="">本月:8095条 &nbsp;&nbsp;上月:4623条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;环比:11%&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></div></h5>
               
                
                
                <div style="width:835px;height:135px;overflow:hidden;" region="north">
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

<div>
<div style="width:400px;height:200px;float:left;border:1px solid #fff;">
<h5>本月事件受理数</h5>
<div id="chart_num" style="width:400px;height:160px"></div>
</div>

<div style="width:400px;height:200px;float:right;border:1px solid #fff;margin-right:10px">
<h5>本月事件类型</h5>
<div id="chart_type" style="width:400px;height:160px"></div>
</div>
</div>                

            
          
          
          
		    </div>
          </div>
        </div><!--end .buildcon-->
      </div><!--end .bpsmain-->
    
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
 $(function(){
     $("#ul_org li").eq(0).click();//
     //$("#ul_org li").first().click();//
     $(".list_div").mCustomScrollbar({theme: "minimal-dark"});
     loadTop();
     loadLeftChart();
     formatCount(354,'total_count');
 });
 
 var colorObj={0:'#ffbc48',1:'#20c0d6',2:'#fc3478'};

//地区生产总值
 function findXXJD(data){
	         
	loadTop();
    loadLeftChart();
    loadRightChart();
	 
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
	
	yxYearChange(o,2019,5);
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
        data: { stype : stype,syear:2019,smonth:05,level:curruntLi.level},
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
			        area:'福州市',
			        thisMonth:100,
			        thisYear:200,
			        ring:'35'
			        },
			        {
			        area:'厦门市',
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
			{type:'办结率',count:58},
		
			{type:'本月个人',count:4076},
		
			{type:'本月企业',count:8}
		
		],
		eventCount = [64,4076,4019];
		
		
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
function loadLeftChart(){
		
	var myChart = echarts.init(document.getElementById('chart_num'));

    //初始化数据
    var category = ['个人政务审批', '企业政务审批'];
    var barData = [4076, 4019];
    var flag=-1;

    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
                x:55,
                y:15,
                width:320,
                height:115,
        },
        xAxis: {
            type: 'value',
            axisLine: {
                    show: true,
                    lineStyle:{
                        color:["#ffffff"]
                    }
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
                }
        },
        yAxis: {
            type: 'category',
            data: category,
            axisLine: {
                    show: true,
                    lineStyle:{
                        color:["#ffffff"]
                    }
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
                    },
                    formatter:function(params) {//设置换行
                                            var newParamsName = "";
                                            var paramsNameNumber = params.length;
                                            var provideNumber = 4;  //一行显示几个字
                                            var rowNumber = Math.ceil(paramsNameNumber / provideNumber);
                                            if (paramsNameNumber > provideNumber) {
                                                for (var p = 0; p < rowNumber; p++) {
                                                    var tempStr = "";
                                                    var start = p * provideNumber;
                                                    var end = start + provideNumber;
                                                    if (p == rowNumber - 1) {
                                                        tempStr = params.substring(start, paramsNameNumber);
                                                    } else {
                                                        tempStr = params.substring(start, end) + "\n";
                                                    }
                                                    newParamsName += tempStr;
                                                }

                                            } else {
                                                newParamsName = params;
                                            }
                                            return newParamsName
                                        },
                }
        },
        series: [
            {
                name: '数量',
                type: 'bar',
                data: barData,
                barWidth: 14,
                barGap: 10,
                smooth: true,
                label: {
                    normal: {
                        show: true,
                        position: 'right',
                        offset: [5, -2],
                        textStyle: {
                            color: '#F68300',
                            fontSize: 13
                        },
                        


                    }
                },
                itemStyle: {
                    
                    normal: {
                        color:'#6CA6CD',
                        label: {
							show: true, //开启显示
						    position: 'right', //在上方显示
							textStyle: { //数值样式
								color: 'white',
								fontSize: 12
							},
							formatter:function(params){
							    
							    return params["data"]+'件';
							}
							
						}
                        
                    }
                }
            }
        ]
    };
    myChart.setOption(option);

}

function loadRightChart(){

var myChart = echarts.init(document.getElementById('chart_type'));

option = {
    
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    color: ["#4F96E2", "#A0D8FE", "#24CCA9","#71C671","#EEE685"],
    series : [
        {
            name: '事件类型',
            type: 'pie',
            radius : '65%',
            center: ['50%', '60%'],
            data:[
                {value:62, name:'个人报税'},
                {value:106, name:'企业报税'},
                {value:201, name:'企业年审'},
                {value:387, name:'消防审查'},
                {value:282, name:'企业登记'}
            ],
            
        }
    ]
};
myChart.setOption(option);


}

function formatCount(num,id){
			num =num?( num +''):'0';
			var str="";
			var len=num.length;
			if(len<5){
			    for(var m=0,n=5-len;m<n;m++){
				    str += "<b>"+0+"</b>";
			    }
			}
			for(var i=0;i<len;i++){
				str += "<b>"+num.charAt(i)+"</b>";
			    
			}
			$("#"+id).html(str);
}

	
	


</script>
</html>
