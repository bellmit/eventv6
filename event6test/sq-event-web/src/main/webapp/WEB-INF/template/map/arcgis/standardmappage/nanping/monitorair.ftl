<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>空气质量监测</title>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/monitor-air.css"/>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/themes/icon.css">
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/js/layer/layer.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
		<style>
		.layer-con{top: 0;left:0;margin-top:0px;margin-left:0px;display:block;-webkit-box-shadow: none; box-shadow: none;}
		</style>
	</head>
	<body>
	<div id="MaxJqueryWindowContent"></div>
		<!--弹窗 start-->
		<div class="layer-con">
			<div class="layer-con-dom" width="100%" height="100%">
				<!--在此处加 页面格式-->
				<!--go-->
				<div class="mr-main-jainc mr-main-jainc1" style="display: block;" id="data_div">
					<!--上-->
					<div class="main-jainc-top ">
						<div class="main-jainc-top-content clearfix ">
							<div class="main-jtc-left fl ld-bot-t${zgAqiStation.zgAQI.state!}"><!--替换类ld-bot-t1~`ld-bot-t7-->  
								<p>${zgAqiStation.zgAQI.aqi}</p>
								<div class="mjtcl-text">
							 		 <span class="mrdata-bq">${zgAqiStation.zgAQI.stateName?replace("污染","")!}</span>
									 <div class="mj-mg-box">
										<div class="mjtcl-bot-mg ld-bot-t${zgAqiStation.zgAQI.state!}">
											<div class="bot-mg"></div>
											<div class="bot-mg1"></div>
											<div class="bot-mg2"></div>
											<div class="bot-mg3"></div>
										</div>
									</div>
								
								</div>
								<div class="mjtcl-type ld-leg-t${zgAqiStation.zgAQI.state!}">
									<p>实时空气质量指数（AQI）</p>
								</div>
							</div>
							<div class="main-jtc-right fr">
								<ul class="mjtcr-list clearfix">
									<li>
										<p>PM2.5</p>
										<p>${zgAqiStation.zgAQI.pm25}</p>
										<p>μm/m³</p>
									</li>
									<li>
										<p>PM10</p>
										<p>${zgAqiStation.zgAQI.pm10}</p>
										<p>μm/m³</p>
									</li>
									<li>
										<p>CO</p>
										<p>${zgAqiStation.zgAQI.co}</p>
										<p>μm/m³</p>
									</li>
									<li>
										<p>NO2</p>
										<p>${zgAqiStation.zgAQI.no2}</p>
										<p>μm/m³</p>
									</li>
									<li>
										<p>O3</p>
										<p>${zgAqiStation.zgAQI.o3}</p>
										<p>μm/m³</p>
									</li>
									<li>
										<p>SO2</p>
										<p>${zgAqiStation.zgAQI.so2}</p>
										<p>μm/m³</p>
									</li>
								</ul>
							</div>
						</div>
						<div class="main-jainc-top-btn mt10 clearfix">
							<div class="mjtb-box fr clearfix">
								<a href="javascript:fnSMS();" class="SMS-notification clearfix active">
									<i></i>
									<p>短信通知</p>
								</a>
								<a href="javascript:fnEVENT();" class="event-reporting clearfix active">
									<i></i>
									<p>事件上报</p>
								</a>
							</div>
						</div>
					</div>
					<!--下-->
					<div class="main-jainc-bottom ">
						<div class="mjb-title clearfix">
							<p>空气质量监测数据</p>
							<div class="mjb-time fr clearfix">
								<div class="mjb-time-item fl active" id="h">
									<p>24小时</p>
								</div>
								<div class="mjb-time-item fl">
									<p>天</p>
								</div>
							</div>
						</div>
						<div class="mjb-echarts-box xm-chart">
							<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
							<div id="UsingTrainH" style="width: 100%;height:100%;"></div>
						</div>
					</div>
				</div>
				<div class="mr-main-jainc mr-main-jainc2" id="event_div">
					<iframe id="event_ifr" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
				</div>
				<div class="mr-main-jainc mr-main-jainc3"  id="sms_div">
					<div class="mr-mj-event-top">
						<div class="mrmet-item clearfix mt10">
							<div class="mrmet-item-left fl">
								<p>联&nbsp;&nbsp;系&nbsp;&nbsp;人</p>
							</div>
							<input type="text" class="text2" placeholder="电话号码" id="mobilePhones"/>
						</div>
						<div class="mrmet-item-title mt10">
							<p>提示：请直接输入联系人手机号码，若发送多个号码，请用“；”隔开，例如：1890000000；18911111111</p>
						</div>
						<div class="mrmet-item clearfix mt40">
							<div class="mrmet-item-left fl">
								<p>短信内容</p>
							</div>
							<textarea class="text2" id="smsContent">【告警提示】${zgAqiStation.stationName}：${zgAqiStation.zgAQI.stateName}，  aqi：${zgAqiStation.zgAQI.aqi}μm/m³、PM2.5：${zgAqiStation.zgAQI.pm25}μm/m³、PM10：${zgAqiStation.zgAQI.pm10}μm/m³、CO:${zgAqiStation.zgAQI.co}μm/m³,NO2:${zgAqiStation.zgAQI.no2}μm/m³,O3:${zgAqiStation.zgAQI.o3}μm/m³,SO2:${zgAqiStation.zgAQI.so2}μm/m³；</textarea>
						</div>
					</div>
					<div class="mr-mj-event-button">
						<a href="javascript:fnSendSMS();" class="fl mr-submit">
							<p>发送</p>
						</a>
						<a href="javascript:closeMaxJqueryWindow();" class="fr mr-cancel">
							<p>取消</p>
						</a>
					</div>
				</div>
			</div>
		</div>
		<!--弹窗 end-->
		
		<script src="${uiDomain!''}/web-assets/plugins/echarts-3.2.3/echarts.js"></script>
	</body>
<script>
var intChart = echarts.init(document.getElementById('UsingTrainH'));
 $(function() {
 $(".mjb-time-item").click(function(){
			$(".mjb-time-item").removeClass("active");
			$(this).addClass("active");
			if($(this).attr("id")=='h'){
			 	 setAIRData("1")
			}else{
				 setAIRData("2")
			}
});

setAIRData("1");
});var dialogs = [];
	var maxJqueryWindowId;
eventSqZzgridUrl = '${rc.getContextPath()}';
function showMaxJqueryWindow(title, targetUrl,width,height, maximizable, scroll, callBackOnClose) {
		$('#event_ifr').attr('src',targetUrl);
	}
	var winType ='';
	function flashData(){
	 if (winType != "" && winType == '0') {//关闭详细窗口
            closeMaxJqueryWindow();
            winType = "";
        }
	}
function fnSendSMS(){
	var mobilePhoneVal = $("#mobilePhones").val();
	if(mobilePhoneVal.length == 0){
		layer.alert('请输入联系人电话号码!');
		return;
	}
	var mobilePhones = mobilePhoneVal.split(","),msgFail='';
	for(var i=0,l=mobilePhones.length;i<l;i++){
		if(!isMobile(mobilePhones[i])) {
			msgFail += "[" + mobilePhones[i] + "] ";
		}
	}
	if(msgFail.length>0){
		layer.alert( "存在如下不合理的手机号码：" + msgFail);
		return;
	}

var smsContent = $("#smsContent").val();
	if(smsContent.length==0){
		layer.alert("短信内容!");
		return;
	}

var layerIndex =  layer.load(1);
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/sendsms.json',
        type: 'POST',
       data: { mobilePhones:mobilePhoneVal,smsContent:smsContent},
        dataType:"json",
        error: function(data){
        	 layer.alert('短信发送异常!');
        },
        complete:function(){
			layer.close(layerIndex); 
        },
        success: function(data){
			layer.alert(data.flag?'短信发送成功!':'短信发送失败!');
			if(data.flag){
				$("#smsContent").val('');
				$("#mobilePhones").val('');
			}
		}
	});
}
function closeMaxJqueryWindow(){
	$("#data_div").show().siblings().hide();
}
//事件上报
function fnEVENT(){
	//$('#event_div').show().siblings().hide();
	var event = {
	    "isReport":false,
	     "callBack":'window.closeMaxJqueryWindow()',    
		"eventName" : '${zgAqiStation.stationName}检测预警',
		"occurred" : '${zgAqiStation.location}',
		"content":'【告警提示】${zgAqiStation.stationName}：${zgAqiStation.zgAQI.stateName}， aqi：${zgAqiStation.zgAQI.aqi}μm/m³、PM2.5：${zgAqiStation.zgAQI.pm25}μm/m³、PM10：${zgAqiStation.zgAQI.pm10}μm/m³、CO:${zgAqiStation.zgAQI.co}μm/m³,NO2:${zgAqiStation.zgAQI.no2}μm/m³,O3:${zgAqiStation.zgAQI.o3}μm/m³,SO2:${zgAqiStation.zgAQI.so2}μm/m³；',
		"happenTimeStr":'${zgAqiStation.zgAQI.monitorTime?string("yyyy-MM-dd HH:mm:ss")}', 
		"resMarker":{'x':'${resMarker.x!}','y':'${resMarker.y!}','mapType':${resMarker.mapType!},'markerType':'${resMarker.markerType!}'},
 	}
 	var event = JSON.stringify(event);    
 	var url="${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson="+encodeURIComponent(event);
window.parent.showMaxJqueryWindow("上报事件", url,window.parent.fetchWinWidth(),window.parent.fetchWinHeight());
 	//window.parent.parent.showMaxJqueryWindow("上报事件", url,window.parent.fetchWinWidth(),window.parent.fetchWinHeight());
	//$('#event_ifr').attr('src', "${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson="+encodeURIComponent(event));
}

function fnSMS(){
	$('#sms_div').show().siblings().hide();
}

function setAIRData(mtype){
var statioId='${zgAqiStation.statioId}';
	$.ajax({ 
        url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/getDataListByStation.json',
        type: 'POST',
        data: { statioId:statioId,mtype:mtype},
        dataType:"json",
        error: function(data){
        	 layer.alert('异常!');
        },
        complete:function(){
			
        },
        success: function(data){
        var startTime="";
        var arrayTitles =[],aqis=[],cos=[],O3s=[],so2s=[],no2s=[],pm25s=[],pm10s=[];
         if(mtype=='1') { 
        	 data.reverse();
         }
         for (var i=0;i<data.length;i++)
		{
		    if(mtype=='2'){
				arrayTitles.push(data[i].DAY_STR);
				if(data.length>14){
		         	 startTime=data[data.length-15].DAY_STR;
		        }
			}else if(mtype=='1') {
			    arrayTitles.push(data[i].HOUR_STR);
			   if(i==11){//取最近24小时
			    startTime=data[11].HOUR_STR;
			     // break;
			    }
			}
			aqis.push(data[i].AQI);
			cos.push(data[i].CO);
			O3s.push(data[i].O3);
			so2s.push(data[i].SO2);
			no2s.push(data[i].NO2);
			pm25s.push(data[i].PM2_5);
			pm10s.push(data[i].PM10);
		}
		var option = {
		    title: false,
		    tooltip : {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'line',
		            label: {
		                show: true,
		                formatter: ' {value}',
		                padding: [4, 6, 6, 4],
		                backgroundColor: '#446299',
		                shadowColor: 'rgba(76, 118, 255, 0.3)',
		                shadowBlur: 10,
		                shadowOffsetY: 4,
		            },
		            lineStyle: {
		                color: '#446299',
		            }
		        },
		        padding: [5 , 10],
		        formatter: '{b0}：<br>{a0}：{c0}<br>{a1}：{c1}<br>{a2}：{c2}<br>{a3}：{c3}<br>{a4}：{c4}<br>{a5}：{c5}<br>{a6}：{c6}',
		        extraCssText: 'box-shadow: 0 2px 4px rgba(102, 168, 255, 0.23);',
		        textStyle: {
		            color: '#fff',
		        },
		    },
		    legend: {
			        show:true,
			            textStyle:{
			            color:'#666'
			        },
		            top:-5,
		            itemGap: 40,
		            itemWidth: 18,
		            inactiveColor: 'rgba(0, 0, 0, 0.3)',
		            data:['AQI','PM2.5','PM10','CO','NO2','O3','SO2'],
		    },
		    grid: {
		       top: '14%', 
		        left: '10px',
		        right: '10px',
		        bottom: '30%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            boundaryGap: false,  
		            axisLine: {
		                lineStyle: {
		                    color: '#66a8ff',
		                }
		            },
		            axisTick: false,
		            axisLabel: {
		                color: '#999',
		            },
		            data: arrayTitles//日期
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value',
		            name: false,
		            nameTextStyle: {
		                color: '#999',
		            },
		            nameGap: '14',
		            // max: '200',
		            axisLine: {
		                show: false,
		            },
		            axisTick: false,
		            axisLabel: {
		                color: '#ccc',
		            },
		            splitLine: {
		                lineStyle: {
		                    color: '#ccc',
		                    width: 1,
		                    type: 'dotted',
		                },
		            },
		        }
		    ],
		    dataZoom:[
		        {
		            type: 'slider',
		            height: 12,
		            bottom: 6,
		            start: -10,
		            end: 37,
		            backgroundColor:'rgba(102,168,255,.2)',
		            dataBackground: {
		                areaStyle: {
		                    color:'rgba(102,168,255,.2)',
		                },
		            },
		            fillerColor: 'rgba(167,183,204,0.5)',
		            borderColor: 'rgba(102,168,255,.05)',
		            handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
		            handleSize: '110%',
		            handleStyle:{
		                color:'rgba(102,168,255,.5)',
		
		            },
		            textStyle:{
		                color:"rgba(167,183,204,0.9)"},
		        }
		    ],
		    series : [
		        {
		            name:'AQI',
		            type:'line',
		            lineStyle: {
		                color: '#ff6161',
		                shadowBlur: 15,
		                shadowColor: 'rgba(255, 97, 97, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(255, 97, 97, 0.6)'},
		                            {offset: 1, color: 'rgba(255, 97, 97, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#ff6161',
		                    label :
		                        {
		                            show: false,
		                            color: '#ff6161',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:aqis
		        },
		        {
		            name:'PM2.5',
		            type:'line',
		            lineStyle: {
		                color: '#66a8ff',
		                shadowBlur: 15,
		                shadowColor: 'rgba(102, 168, 255, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(102, 168, 255, 0.6)'},
		                            {offset: 1, color: 'rgba(102, 168, 255, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#66a8ff',
		                    label :
		                        {
		                            show: false,
		                            color: '#66a8ff',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:pm25s
		        },
		         {
		            name:'PM10',
		            type:'line',
		            lineStyle: {
		                color: '#60db2c',
		                shadowBlur: 15,
		                shadowColor: 'rgba(96, 219, 44, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(96, 219, 44, 0.6)'},
		                            {offset: 1, color: 'rgba(96, 219, 44, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#60db2c',
		                    label :
		                        {
		                            show: false,
		                            color: '#60db2c',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:pm10s
		        },
		        {
		            name:'CO',
		            type:'line',
		            lineStyle: {
		                color: '#7e0023',
		                shadowBlur: 15,
		                shadowColor: 'rgba(96, 219, 44, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(96, 219, 44, 0.6)'},
		                            {offset: 1, color: 'rgba(96, 219, 44, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#7e0023',
		                    label :
		                        {
		                            show: false,
		                            color: '#7e0023',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:cos
		        },
		        {
		            name:'NO2',
		            type:'line',
		            lineStyle: {
		                color: '#ffde33',
		                shadowBlur: 15,
		                shadowColor: 'rgba(96, 219, 44, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(96, 219, 44, 0.6)'},
		                            {offset: 1, color: 'rgba(96, 219, 44, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#ffde33',
		                    label :
		                        {
		                            show: false,
		                            color: '#ffde33',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:no2s
		        },
		        {
		            name:'O3',
		            type:'line',
		            lineStyle: {
		                color: '#9b2cdb',
		                shadowBlur: 15,
		                shadowColor: 'rgba(155, 44, 219, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(155, 44, 219, 0.6)'},
		                            {offset: 1, color: 'rgba(155, 44, 219, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#9b2cdb',
		                    label :
		                        {
		                            show: false,
		                            color: '#9b2cdb',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:O3s
		        },{
		            name:'SO2',  
		            type:'line',
		            lineStyle: {
		                color: '#cc0033',
		                shadowBlur: 15,
		                shadowColor: 'rgba(155, 44, 219, 0.6)',
		                shadowOffsetY: 10,
		            },
		            areaStyle: {
		                normal: {
		                    //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
		                    color: new echarts.graphic.LinearGradient(
		                        0, 0, 0, 1,
		                        [
		                            {offset: 0, color: 'rgba(155, 44, 219, 0.6)'},
		                            {offset: 1, color: 'rgba(155, 44, 219, 0.2)'},
		                        ]
		                        //区域颜色渐变
		                    ),
		                    origin: 'start',
		                }
		            },
		            symbolSize: 8,
		            itemStyle : {
		                normal: {
		                    color: '#cc0033',
		                    label :
		                        {
		                            show: false,
		                            color: '#cc0033',
		                        }
		                },
		                emphasis: {
		                    label : {
		                        show: true,
		                        textStyle : {
		                            fontSize : '13',
		                        }
		                    }
		                }
		            },
		            data:so2s
		        }
		    ]
		};
			var myDate = new Date();
			option.dataZoom=[{
	            startValue:startTime,
	            zoomLock:true
	        }, {
	            type: 'inside'
	        }];
	 
		intChart.clear();
		intChart.setOption(option);
		
		}
	});
	
	
	function GetDateStr(AddDayCount) { 
        var dd = new Date(); 
        dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期 
        var y = dd.getFullYear(); 
        var m = dd.getMonth()+1;//获取当前月份的日期 
        if (m < 10) {
		    m = "0" + m;
		}
        var d = dd.getDate(); 
        if (d < 10) {
		    d = "0" + d;
		}
        return y + "年" + m + "月"+d+"日";
}
}


</script>
   
</html>
