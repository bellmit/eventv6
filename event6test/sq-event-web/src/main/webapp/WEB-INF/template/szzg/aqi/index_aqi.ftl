<!DOCTYPE html>
<html>
<head> 
	<title>空气质量</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	
  	<script type="text/javascript" src="${rc.getContextPath()}/js/echarts/echarts-all.js"></script>

	<style type="text/css">
	body {
	    font-family: "微软雅黑";
	    font-size: 0.875em;
	    color: #fff;
	}
	.clearfloat{ clear:both; height:0; font-size:1px; line-height:0;}
	h3 {
	    font-size: 18px;
	    padding: 8px 0 0 25px;
	}
	.aqi-chart1 {
	    float: left;
	    font-size: 12px;
	    line-height: 1.6;
	}
	.aqi-chart-nr {
	    width: 225px;
	    height: 104px;
	    padding: 5px 10px 0 10px;
	}
	.aqi-list {
	    border-collapse: collapse;
	    font-size: 12px;
	}
	.aqi-list tr th {
	 border: 1px solid #8694a9;
     padding: 3px;
     text-align: center;
	}
	.aqi-list tr td {
	    border: 1px solid #8694a9;
	    padding: 1px 3px;
	}
	 h5 {
	    font-weight: normal;
	    font-size: 14px;
	    padding: 0px 0 5px 0;
	}
	tr.coltit, tr.coltit {
  	  background: rgba(0,170,255,0.4);
	}
	.fisright-chart2 {
	    margin-left: 0px;
	    border: 1px solid #6d8ca4;
	    padding-top: 10px;
	}
	iframe{background: transparent;}
	</style>
</head>
<body>
<#if selType=="kqzl-1">
  <div style="display:inline" id="shhj-kq" >
   <div style="float:left" class="fisright-chart2">
 	 	<div >
        <table width="400px" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr>
            <td height="180px">
            	<div id="hour_list" style="width: 430px;height: 180px; margin-left: 10px"></div>
            </td>
          </tr>
          <tr>
            <td height="180px">
            	<div id="day_list" style="width: 430px;height: 180px; margin-left: 10px"></div>
            </td>
          </tr>
        </table> 
   	  </div>
   </div>	
   
   <div style="float:left; margin-left:10px" class="fisright-chart2">
   			<div style="text-align:center"><span id="cityName"></span>空气质量指数（AQI）时报</div>
			<table>
				<tr>
					<td>
		           		<div id="aqi_angular" style="width: 205px; height: 110px;"></div>
		           	</td>
		           	<td>
						<div class="aqi-chart1" style="float: right">
							<div id="aqi-chart-nr" class="aqi-chart-nr"></div>
						</div>
					</td>
				</tr>
			</table>
			<div style="overflow-x:hidden;overflow-y:auto;height:230px;"> 
	            <table id="kq-list" style="width:456px;height:230px;" border="0" cellspacing="0" cellpadding="0" class="aqi-list">
	            </table> 
            </div> 
    </div>
    </div>

<script type="text/javascript">
	$(document).ready(function() {
			var aqi_map = ${aqi_map};
			var kq_list =${kq_list}; // [{"seqid":7052589,"stationName":"省外办","statioid":"1290A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"102","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"33","so224":"22","no2":"33","no224":"34","pm10":"128","pm1024":"138","pm25":"76","pm2524":"59","co":"0.997","co24":"1.045","o3":"130","o38":"70","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"421.290644","y":"8870.378206","longitude":"","dimensions":"","opdate":null},{"seqid":7052590,"stationName":"省林业公司","statioid":"1291A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"98","state":"2","stateName":"良","airclass":"","className":"二级","so2":"30","so224":"41","no2":"20","no224":"36","pm10":"107","pm1024":"160","pm25":"73","pm2524":"72","co":"0.603","co24":"0.945","o3":"131","o38":"76","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"-3264.512044","y":"9715.449895","longitude":"","dimensions":"","opdate":null},{"seqid":7052591,"stationName":"林科所","statioid":"1292A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"127","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"45","so224":"21","no2":"22","no224":"33","pm10":"195","pm1024":"136","pm25":"96","pm2524":"72","co":"1.256","co24":"1.165","o3":"166","o38":"81","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"11121.302094","y":"12577.775403","longitude":"","dimensions":"","opdate":null},{"seqid":7052592,"stationName":"京东镇政府","statioid":"1293A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"92","state":"2","stateName":"良","airclass":"","className":"二级","so2":"43","so224":"42","no2":"14","no224":"25","pm10":"126","pm1024":"114","pm25":"68","pm2524":"73","co":"0.887","co24":"1.001","o3":"162","o38":"76","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"6406.144862","y":"13138.531715","longitude":"","dimensions":"","opdate":null},{"seqid":7052593,"stationName":"建工学校","statioid":"1294A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"103","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"37","so224":"28","no2":"24","no224":"59","pm10":"126","pm1024":"162","pm25":"77","pm2524":"62","co":"0.924","co24":"0.997","o3":"134","o38":"47","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"-1171.957097","y":"12199.266689","longitude":"","dimensions":"","opdate":null},{"seqid":7052594,"stationName":"象湖","statioid":"1295A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"128","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"41","so224":"21","no2":"49","no224":"68","pm10":"205","pm1024":"216","pm25":"63","pm2524":"75","co":"2.077","co24":"1.564","o3":"99","o38":"36","mainFomite":"颗粒物(PM10)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"8389.513083","y":"9149.538857","longitude":"","dimensions":"","opdate":null},{"seqid":7052595,"stationName":"武术学校","statioid":"1296A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"68","state":"2","stateName":"良","airclass":"","className":"二级","so2":"10","so224":"5","no2":"8","no224":"9","pm10":"61","pm1024":"82","pm25":"49","pm2524":"36","co":"0.541","co24":"0.57","o3":"79","o38":"34","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"-3768.449974","y":"7513.036264","longitude":"","dimensions":"","opdate":null},{"seqid":7052596,"stationName":"石化","statioid":"1297A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"142","state":"3","stateName":"轻度污染","airclass":"","className":"三级","so2":"43","so224":"26","no2":"45","no224":"53","pm10":"177","pm1024":"186","pm25":"108","pm2524":"107","co":"1.397","co24":"1.574","o3":"160","o38":"62","mainFomite":"细颗粒物(PM2.5)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼","x":"4574.951533","y":"12817.032258","longitude":"","dimensions":"","opdate":null},{"seqid":7052597,"stationName":"省站","statioid":"1298A","monitortime":1440648000000,"monitortype":"1","hour":"12","aqi":"90","state":"2","stateName":"良","airclass":"","className":"二级","so2":"46","so224":"42","no2":"21","no224":"31","pm10":"129","pm1024":"120","pm25":"65","pm2524":"61","co":"1.063","co24":"0.887","o3":"149","o38":"74","mainFomite":"颗粒物(PM10)","picpath":"","class24":"","state24":"","aqi24":"","effectinfo":"","adviseInfo":"极少数异常敏感人群应减少户外活动","x":"7699.246515","y":"9972.466179","longitude":"","dimensions":"","opdate":null}];
			var nowtime= '${nowtime}';
			getAngularPic("aqi_angular", "", aqi_map.aqi);
			KQ_layer3(aqi_map, kq_list, nowtime);
			setKQTime();
			var arr=new Array()
			for ( var i = 0; i < kq_list.length; i++) {
				var aqi = new Object();   　　//创建对象  
				aqi.name=kq_list[i].stationName;
				aqi.x=kq_list[i].longitude;
				aqi.y=kq_list[i].dimensions;
				arr.push(aqi);
			}
			var mapLocationObject = {
      		 locationMaplist : arr
			};
			window.parent.locationPointsOnMap(mapLocationObject);
	});
	function showXY(name,x,y){
		var mapLocationObject = {
		       locationMaplist : [{
		           name : name,
		           x : x,
		           y : y
		       }]
		};
		window.parent.locationPointsOnMap(mapLocationObject);
	}
	
</script>
</#if>
  	
</body>
<script type="text/javascript">
function getAngularPic(divName, titleName, value) {
//function asdf() {
	var myChart1 = echarts.init(document.getElementById(divName));
	myChart1.setOption( {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}"
	    },
	    series : [
	        {
	            name:'空气质量',
	            type:'gauge',
	            center : ['50%', '90%'],    // 默认全局居中
	            radius : [0, '180%'],
	            startAngle: 180,
	            endAngle : 0,
	            min: 0,                     // 最小值
	            max: 500,                   // 最大值
	            precision: 0,               // 小数精度，默认为0，无小数点
	            splitNumber: 5,             // 分割段数，默认为5
	            axisLine: {            // 坐标轴线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle控制线条样式
	                    color: [[0.1, '#38E936'],[0.2, '#EDED3E'],[0.3, '#EF9B52'],[0.4, '#EA4343'],[0.6, '#A1477A'],[1, '#81243D']], 
	                    width: 25
	                }
	            },
	            axisTick: {            // 坐标轴小标记
	                show: true,        // 属性show控制显示与否，默认不显示
	                splitNumber: 10,    // 每份split细分多少段
	                length :8,         // 属性length控制线长
	                lineStyle: {       // 属性lineStyle控制线条样式
	                    color: '#8694a9',
	                    width: 1,
	                    type: 'solid'
	                }
	            },
	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
	                show: true,       // 默认显示，属性show控制显示与否
	                length :25,
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: '#fff'
	                }
	            },
	            
	            splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                length :25,         // 属性length控制线长
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: '#8694a9',
	                    width: 3,
	                    type: 'solid'
	                }
	            },
	            pointer : {
	                length : '80%',
	                width : 8,
	                color : '#fff'
	            },
	            title : {
	             textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    fontWeight: 'bolder',
	                    color: '#fff'
	                }
	            },
	            detail : {
	                show : false,
	                backgroundColor: 'rgba(0,0,0,0)',
	                borderWidth: 0,
	                borderColor: '#fff',
	                width: 100,
	                height: 40,
	                offsetCenter: ['0%', 30],       // x, y，单位px
	                formatter:'{value}',
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: '#fff',
	                    fontSize : 30
	                }
	            },
	            data:[{value: value, name: 'AQI'}],
	        }
	    ]
	});
}

var KqTime;
function setKQTime(){
	
	var dateIntegralPoint = new Date();//用户登录时刻的下一个整点，也可以设置成某一个固定时刻
	dateIntegralPoint.setHours(dateIntegralPoint.getHours()+1);//小时数增加1
	dateIntegralPoint.setMinutes(0);
	dateIntegralPoint.setSeconds(0);
	var date = new Date();
	var now = date.getFullYear()+"年"+
	((date.getMonth()+1)>=10?(date.getMonth()+1):"0"+(date.getMonth()+1))+"月"+
	(date.getDate()>=10?date.getDate():("0"+date.getDate()))+"日"+
	((date.getHours()-1)>=10?(date.getHours()-1):("0"+(date.getHours()-1)))+"时";
	$('#kqTime').html(now);
	KqTime = setTimeout(setKQTime, dateIntegralPoint-date);
}

function KQ_layer3(aqi_map, kq_list, nowtime){
	$("#cityName").html(aqi_map.stationName)
	var str ='<p style="margin-bottom: 10px;" id="kqTime">'+
	nowtime
	+
	'</p>'+
	'<div class="clearfloat"></div>'+
	'<p>'+
		'空气质量状况：'+
		'<img src="${rc.getContextPath()}/images/map_jiangyin/aqi-color'+aqi_map.state+'.png" width="58" height="11" />'+
		'&nbsp;'+aqi_map.stateName+
	'</p>'+
	'<p>'+
		'健康建议：'+aqi_map.adviseInfo+
	'</p>';

	$("#aqi-chart-nr").html(str);
	
	var kqstr = '<tr><th  width="22%">监测点</th><th width="11%">AQI</th><th width="18%">质量状况</th><th width="29%">首要污染物</th><th width="8%">PM2.5</th><th width="10%">定位</th></tr>';
	//var num = kq_list.length >= 5 ? 5 : kq_list.length;
	for ( var i = 0; i < kq_list.length; i++) {
	if (i % 2 == 0) {
		kqstr += '<tr align="center">';
	} else {
		kqstr += '<tr class="aqi-list-ys">';
	}
	kqstr += '<td align="center" style="cursor:pointer" onclick="KQ_PIC_layle(\''
		+ kq_list[i].statioId + '\',\''
		+ kq_list[i].stationName + '\');">'
		+ kq_list[i].stationName + '</td>';
	kqstr += '<td align="center">' + kq_list[i].aqi + '</td>';
	kqstr += '<td align="center"><img src="${rc.getContextPath()}/images/map_jiangyin/aqi-color'+kq_list[i].state+'.png" width="38" height="11" />&nbsp;' + kq_list[i].className + '</td>';
	kqstr += '<td align="center">' + kq_list[i].mainFomite + '</td>';
	kqstr += '<td align="center">' + kq_list[i].pm25 + 'μg/m³</td>';
	//kqstr += '<td align="center" width="14%">--</td>';
	//kqstr += '<td align="center" width="16%">--</td>';
	kqstr += '<td style="cursor:pointer" align="center" onclick="showXY(\''
		+ kq_list[i].stationName
		+ '\',\''
		+ kq_list[i].longitude
		+ '\',\''
		+ kq_list[i].dimensions
		+ '\');"><img src="${rc.getContextPath()}/images/map_jiangyin/aqi-icon1.png" width="20" height="20" /></td></tr>';

	// 坐标点的展示  
	var title = '<h4>' + kq_list[i].stationName + '空气质量</h4>';
	var html = '<div class="aqi-zl">'
		+ '<h6>实时空气质量指数(AQI)<span class="a-date">'+nowtime+'</span></h6>'
		+ '<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">'
		+ '<tr>'
		+ '<td class="myaqi">'
		+ kq_list[i].aqi
		+ '</td>'
		+ '<td><img src="${rc.getContextPath()}/css/map_jxhgt/images/air_face'+kq_list[i].state+'.png" width="50" height="56" /></td>'
		+ '<td rowspan="2" align="right" valign="middle"><div class="airsj"><div class="airwz"></div><img src="${rc.getContextPath()}/css/map_jxhgt/images/aqi-img3.png" width="148" height="83" /></div></td>'
		+ '</tr>' + '<tr>' + '<td class="myzl">'
		+ kq_list[i].className + '   '
		+ kq_list[i].stateName + '</td>'
		+ '<td>&nbsp;</td>' + '</tr>' + '</table>'
		+ '<div class="airline"></div>'
		+ '<h6>最近24小时空气质量指数 </h6>' + '<p class="yaqi">'
		+ kq_list[i].aqi24 + '</p>'
		+ '<p class="djtxt">等级： <span class="yjb">'
		+ kq_list[i].class24
		+ '</span>  首要污染物： <span class="yjb">'
		+ kq_list[i].mainFomite
		+ '</span>   质量状况： <span class="yjb">'
		+ kq_list[i].state24 + '</span> </p>' + '</div>';
		
	/*var point = window.parent.initMap.addPicPoint( {
		x : kq_list[i].x,
		y : kq_list[i].y,
		url : "/css/map_jxhgt/images/hgt_icon21.png",
		w : 24,
		h : 27,
		title : title,
		content : html
	});
	window.parent.shhj.kqMap.point[kq_list[i].stationName] = point;
	point.clickFun = "";//"shhjResize(point_kq)";
	window.parent.map.infoWindow.resize(429, 328);
	window.parent.initMap.addGraphic(point);*/
	}
	$("#kq-list").html(kqstr);
	KQ_PIC_layle( aqi_map.statioId, aqi_map.stationName);
}

function KQ_PIC_layle(statioId, stationName){
	var url = '${rc.getContextPath()}/zhsq/szzg/aqi/index.jhtml?selType=kqzl-2&statioid='+statioId+'&stationName='+encodeURIComponent(stationName); 
	//showMaxJqueryWindow('详情', url, 420, 400);
	//$("#contentFrame",document).attr("src", url);
	
		$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/szzg/aqi/jsonData.json?selType=kqzl-2&statioid='+statioId,
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
			
					var hour_list_name= eval('(' + data.hour_list_name + ')');
					var hour_list_value = eval('(' + data.hour_list_value + ')');
					var day_list_name =eval('(' + data.day_list_name + ')');
					var day_list_value = eval('(' + data.day_list_value + ')');
					KQ_PIC_layle3(hour_list_name, hour_list_value, day_list_name, day_list_value, stationName);
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
}

function KQ_PIC_layle3(hour_list_name, hour_list_value, day_list_name, day_list_value, name){
	var myChart = echarts.init(document.getElementById("hour_list"));
	var myChart1 = echarts.init(document.getElementById("day_list"));
	myChart.setOption( {
		title : {
	        text: name,
	        textStyle: {      
	            color: '#fff', 
	            fontSize: 5
	        }
	    },
		tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	    	x : 'right',
		    y : 'top' ,
	        data:['最近24小时空气质量指数趋势'],
	        textStyle: {      
	            color: '#fff'
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data :  hour_list_name,
	            /*	(function(){
	            		var date = new Date();
	                    var hour = [];
	                    for(var i=0; i<24; i++){
	                    	date.setHours(date.getHours()-1);
	                    	var timeStr = (date.getDate()<10?("0"+date.getDate()+"日"):(date.getDate()+"日"))
	                    	timeStr += (date.getHours()<10?("0"+date.getHours()+"时"):(date.getHours()+"时"));
	                    	hour.unshift(timeStr)
	                    }
	                    return hour;
	            	})(),*/
	            axisLabel: {
		            show: true,
		            interval:0,
		            rotate: 45,
		            textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: '#fff'
	                }
		    	},  
		    	 splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: '#8694a9',
	                    width: 1,
	                    type: 'solid'
	                }
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLabel: {
		            show: true,
		             textStyle: {      
	                    color: '#fff'
	                }
		    	},
		    	splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: '#8694a9',
	                    width: 1,
	                    type: 'solid'
	                }
	            }
	        }
	    ],
	    grid:{
	    	x:35,
	    	y:30,
	    	x2:5,
	    	y2:55
	    },
	   
	    series : [
	        {
	            name:'最近24小时空气质量指数趋势',
	            type:'line',
	            data: hour_list_value,
	            itemStyle: {
	                normal: {
	                    color: '#ffbc48',
	                    borderColor: '#ffbc48',
	                    borderWidth: 6,
	                    borderRadius:0,
	                    label : {
	                        show: true, fontWeight: 'bold', position: 'insideTop'
	                    }
	                }
	            }
	        }
	    ]
	});
	myChart1.setOption( {
		title : {
	        text: name, 
	        textStyle: {      
	            color: '#fff', 
	            fontSize: 5
	        }
	    },
		tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	    	
	    	x : 'right',
		    y : 'top' ,
	        data:['最近两周空气质量指数趋势'],
	        textStyle: {      
	            color: '#fff'
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : day_list_name,
	            	/*(function(){
	            		var _date = new Date();
	            		var _list = [];
	            		for(var i =0; i < 14; i++){
	            			_date.setDate(_date.getDate()-1);
	            			var time = (_date.getMonth()+1)<10?("0"+(_date.getMonth()+1)+"月"):(_date.getMonth()+1)+"月";
	            			time += (_date.getDate()<10?("0"+_date.getDate()+"日"):(_date.getDate()+"日"));
	            			_list.unshift(time);
	            		}
	            		return _list;
	            	})(),*/
	            axisLabel: {
		            show: true,
		            interval:0,
		            rotate: 45,
		             textStyle: {       
	                    color: '#fff'
	                }
		    	},
		    	 splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: '#8694a9',
	                    width: 1,
	                    type: 'solid'
	                }
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	             axisLabel: {
		            show: true,
		             textStyle: {      
	                    color: '#fff'
	                }
		    	},
		    	 splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: '#8694a9',
	                    width: 1,
	                    type: 'solid'
	                }
	            }
	        }
	    ],
	    grid:{
	    	x:35,
	    	y:30,
	    	x2:5,
	    	y2:55
	    },
	    series : [
	        {
	            name:'最近两周空气质量指数趋势',
	            type:'line',
	            data: day_list_value,
	            itemStyle: {
	                normal: {
	                    color: '#ffbc48',
	                    borderColor: '#ffbc48',
	                    borderWidth: 6,
	                    borderRadius:0,
	                    label : {
	                        show: true, fontWeight: 'bold', position: 'insideTop'
	                    }
	                }
	            }
	        }
	    ]
	});
}
</script>
</html>
