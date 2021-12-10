<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租车信息</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/map/bMap/bMapTools.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: #fff;">
	<div style="margin:2px 3px 0px 3px;">
	<#if data?exists>
		<div class="con ManagerWatch">
        	<div class="ManagerInfo"> 
                <ul>
                    <li style="width:100%;">
                    	<p><span>${data.carNo!''}</span><code>${data.buslineName!''}</code></p>
                        <p style="width:100%;"><code>车辆类型：</code>${data.carKindName!'暂无'}<code style="margin-left:10px;">登记日期：</code>${data.carDjrq!'暂无'}</p>
                        <p style="width:100%;"><code>登记证号：</code>${data.carDjzh!'暂无'}<code style="margin-left:10px;">购置证号：</code>${data.carGzzh!'暂无'}</p>
                        <p style="width:100%;"><code>发动机号：</code>${data.carFdjh!'暂无'}<code style="margin-left:10px;">发证日期：</code>${data.carFzrq!'暂无'}</p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
            <div class="h_10"></div>
            <div class="ManagerSearch">
            	<div class="nav"> 
                    <ul>
                        <li class="current">历史轨迹查询</li>
                        <li class="GreenBg" onclick="startTimer();"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                    </ul>
                    <div class="line"></div>
                </div>
                <div class="con">
                	<ul>
                    	<li><input type="text" id="start" onclick="WdatePicker({el:'start',startDate:'${startTime}',maxDate:'#F{$dp.$D(\'end\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:130px; height:26px; line-height:24px;" value="${startTime}"/></li>
                        <li>—</li>
                    	<li><input type="text" id="end" onclick="WdatePicker({el:'end',startDate:'${endTime}',minDate:'#F{$dp.$D(\'start\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:130px; height:26px; line-height:24px;" value="${endTime}"/></li>
                        <li><a href="javascript:void(0);" onclick="trajectoryQuery(1)" class="btn"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_4.png" />查询</a></li>
                    </ul>
                    <div class="clear"></div> 
                </div>
            </div>
        </div>
    <#else>
    <table cellpadding="0" cellspacing="0" border="0"  class="searchList-2">
    	<tr style="height: 185px"><td align="center" style="color:red;font:14;width:350px" class="sj_cot2_sty" >未查到相关数据！</td></tr>
    </table>
    </#if>
		
		
	</div>
	
</body>
<script type="text/javascript">
	var isCross;
	<#if isCross??>
		isCross = '${isCross}';
	</#if>

var zhsq_url = "${SQ_ZHSQ_EVENT_URL}";
var current = $("#end").val();
current = Date.parse(current.replace(/-/g,"/"));
var currentDate = new Date(current); // 当前时间

var currentYear = currentDate.getFullYear();//当前年份
var currentMonth = currentDate.getMonth() + 1;// 当前月份

var currentDate2 = new Date(currentYear, currentMonth, 0);
var lastDate = currentDate2.getDate(); // 当前月最后一天

var dateLine = new Date();
dateLine.setFullYear(currentYear);
dateLine.setMonth(currentMonth - 1);
dateLine.setDate(lastDate);

// 获取三个月前的时间
function get3MonthBefor(currentDate){  
   var resultDate,year,month,date,hms; 
   year = currentDate.getFullYear();
   month = currentDate.getMonth()+1;
   date = 1;
   switch(month)
   {
		case 1:
		case 2:
			month += 10;
			year--;
			break;
		default:
		    month -= 2;
		 	break;
   }
   
   month = (month < 10) ? ('0' + month) : month;
   resultDate = year + '/' + month +'/' +date;
   return new Date(resultDate);
}

function trajectoryQuery(type){
	parent.clearTaxiInterval();
	//关闭实时位置定时器
	stopTimer();
	parent.stopLushu();
	//parent.markerMap.clear();
	parent.carMap.clear();
	parent.map.clearOverlays();//清除所有覆盖物
	parent.oaMap.removeAll();
	
	var start = $("#start").val();
	var end = $("#end").val();
	
	var tempDate = get3MonthBefor(currentDate);
	
	if (start > end) {
		alert("开始时间需要小于结束时间");
		return;
	}
	
	var start2 = Date.parse(start.replace(/-/g,"/"));
	var startDate = new Date(start2); // 开始时间
	
	var end2 = Date.parse(end.replace(/-/g,"/"));
	var endDate = new Date(end2); // 结束时间
	if (startDate.getTime() < tempDate.getTime()
			|| startDate.getTime() > dateLine.getTime()
			|| endDate.getTime() > dateLine.getTime()) {
		alert("只能查询三个月内（从" + (tempDate.getMonth() + 1) + "月1号开始）的轨迹数据");
		return;
	}
	
	var params = "?carId=${data.carId!c}&stime="+start+"&etime="+end+"&t="+Math.random();
  	var url = '${rc.getContextPath()}/zhsq/map/taix/taxiHistroyTrail.jhtml'+params;
	
	parent.loading();
	
	$.ajax({
		type: "POST",
		url: url,
		data: {},
		dataType:"json",
		success: function(res){ 
			
			var ployLine;
			
			parent.oaMap.removeAll();
		
			if(res.car){
				var map = parent.getNewMap();
				var car = res.car;
				map.put(car.devId,car.carNo);
				parent.carMap = map;
			}
			var tmpPoints = res.points;
			if(!tmpPoints){
				alert('该时段没有行驶轨迹');
				parent.loadOver();
				return;
			}
			
			var currPoints = [];
			var arrLen = tmpPoints.length;
			var tmpMap = new Map();
			for(var k = 0, len = arrLen; k < len ;k++){
				currPoints[k] = parent.oaMap.getPoint(tmpPoints[k].x,tmpPoints[k].y);
				tmpMap.put(tmpPoints[k].x+'_'+tmpPoints[k].y,'1');
			}
			
			parent.oaMap.batchGPS2BD('taxi',currPoints,function(data){
				
					if(data.length == tmpPoints.length){
						
						for(var m = 0 , mlen = data.length; m < mlen ; m++){
							currPoints[m].x = data[m].lng;
							currPoints[m].y = data[m].lat;
							currPoints[m].locateTime = tmpPoints[m].locateTime;
						}
						
						var title = '设备ID:'+car.devId;
						var content = car.carNo;
						var speed = 50;
						var isRotation = tmpMap.size() > parseInt(arrLen*0.5);
						parent.luShuFunc(currPoints,title,content,speed,isRotation);
						
						parent.loadOver();
					}
					
			},true);
		},
		error:function(data){
			alert('数据读取错误');
			parent.loadOver();
		}
	});
}

var prevPoint ;
var intevalTime = 40000;

function dynamicTrajectoryQuery(){

	var url = "${rc.getContextPath()}/zhsq/map/taix/daymicTrailByCarId.jhtml?isTest=0&index="+index_;
	
	$.ajax({
		type: "POST",
		url: url,
		data: {
			carId : '${data.carId!''}'
		},
		dataType:"json",
		success: function(res){ 
			index_++;
			var car = res.car;
			var tmpPoints = res.points;
			if(tmpPoints){
				var tmpP = {};
				tmpP.lng = tmpPoints[0].x;
				tmpP.lat = tmpPoints[0].y;
				tmpP.gtime = tmpPoints[0].gtime;
				console.log('gtime='+tmpPoints[0].gtime+',lng='+tmpPoints[0].x+',lat='+tmpPoints[0].y);
				parent.queue.enqueue(tmpP);
			}
				console.log('queue size='+parent.queue.size());
				var runPoint = parent.queue.dequeue();
				if(runPoint){
					parent.oaMap.GPS2BD(runPoint,function(data){
						if(!parent.dymTaxiMarker){
							prevPoint = data.points[0];
							parent.dynamicLine([runPoint],car.carNo,car.devId,0,tmpPoints[0].direction);
							return;
						}
						parent.getPointAtRoute(parent.dymTaxiMarker.getPosition(),data.points[0],200,function(result){
							var inteval = new Date(runPoint.gtime.replace(/-/g,'/')).getTime()-new Date(prevPoint.time.replace(/-/g,'/')).getTime()
							//console.log('实际时间间隔:'+inteval);
							inteval = (inteval>intevalTime)?intevalTime:inteval;
							//console.log('大于10000时为:'+(inteval-0+500));
							parent.dynamicLine(result,car.carNo,car.devId,(inteval-0+10000));
							
							prevPoint = data.points[0];
							prevPoint.time = runPoint.gtime;
						});
					});
				}
		},
		error:function(data){
			alert('数据读取错误');
		}
	});
}


parent.queue = new Queue();

function startTimer(){

	parent.clearTaxiInterval();

	if(parent.queue){
		parent.queue.clear();
	}
	
	stopTimer();
	parent.stopLushu();
	//parent.markerMap.clear();
	parent.carMap.clear();
	parent.map.clearOverlays();//清除所有覆盖物
	parent.oaMap.removeAll();
	
	index_ = 0;
	parent.map.clearOverlays();
	parent.isMarker = false;
  	var url = '${rc.getContextPath()}/zhsq/map/taix/getPervMinutesTrail.jhtml';
  	//var url = "${rc.getContextPath()}/zhsq/map/taix/daymicTrailByCarId.jhtml?isTest=1&index="+index_;
	$.ajax({
		type: "POST",
		url: url,
		data: {
			carId : '${data.carId!''}',
			prevMills : 120
		},
		dataType:"json",
		success: function(res){ 
			var car = res.car;
			var tmpPoints = res.points;
			var title = '设备ID:'+car.devId;
			var content = car.carNo;
			var speed = 500;
			
			if(!res.points){
				//alert('没有轨迹数据');
				dynamicTrajectoryQuery();
				parent.dynamicTimer = self.setInterval("dynamicTrajectoryQuery()",intevalTime);
				return;
			}
			
			var tiLen = res.points.length 
			
			for(var ti = 0 ; ti < tiLen ; ti++){
				var tmpP = {};
				tmpP.lng = tmpPoints[ti].x;
				tmpP.lat = tmpPoints[ti].y;
				tmpP.direction = tmpPoints[ti].direction;
				tmpP.gtime = tmpPoints[ti].gtime;
				parent.queue.enqueue(tmpP);
			}
			
			var firstPoint = parent.queue.dequeue();
			parent.oaMap.GPS2BD(firstPoint,function(data){
				index_++;
				prevPoint = data.points[0];
				prevPoint.time = firstPoint.gtime;
				var result = [];
				result.push(prevPoint);
				parent.dynamicLine(result,car.carNo,car.devId,0,firstPoint.direction);
				if(tiLen > 1){
					var nextPoint = parent.queue.dequeue();
					parent.oaMap.GPS2BD(nextPoint,function(dataP){
						parent.getPointAtRoute(data.points[0],dataP.points[0],200,function(result){
							parent.dynamicLine(result,car.carNo,car.devId,intevalTime);
							prevPoint = dataP.points[0];
							prevPoint.time = nextPoint.gtime;
						});
					});
				}
				parent.dynamicTimer = self.setInterval("dynamicTrajectoryQuery()",intevalTime);
			});
			
		},
		error:function(data){
			alert('数据读取错误');
		}
	});
}

function stopTimer(){
	if(parent.dynamicTimer)
		window.clearInterval(parent.dynamicTimer);
}

function Queue() {
  var items = [];
  
  //入队
  this.enqueue = function (ele) {
    items.push(ele);
  };
  
  //出队
  this.dequeue = function () {
    return items.shift();
  };
  
  //查看队头元素
  this.front = function () {
    return items[0];
  };
  
  //判断队列是否为空
  this.isEmpty = function () {
    return items.length === 0;
  };
  
  //队列大小
  this.size = function () {
    return items.length;
  };
  
  //清空队列
  this.clear = function () {
    items = [];
  };
  
  //打印队列
  this.print = function () {
    console.log(items.toString());
  };
}

var index_ = 0;
var dbPoints = [
/*
	{lng:117.969173,lat:26.639403,gtime:2017-11-18 10:09:00},
	{lng:117.969784,lat:26.640418,gtime:2017-11-18 10:09:30},
	{lng:117.970016,lat:26.640733,gtime:2017-11-18 10:10:00},
	{lng:117.9695,lat:26.640091,gtime:2017-11-18 10:10:30},
	{lng:117.96931,lat:26.639626,gtime:2017-11-18 10:11:00},
	{lng:117.969588,lat:26.638526,gtime:2017-11-18 10:11:30},
	{lng:117.969805,lat:26.636978,gtime:2017-11-18 10:12:00},
	{lng:117.968651,lat:26.633949,gtime:2017-11-18 10:12:30},
	{lng:117.969981,lat:26.630236,gtime:2017-11-18 10:13:00},
	{lng:117.973523,lat:26.627503,gtime:2017-11-18 10:13:30},
	{lng:117.977733,lat:26.625798,gtime:2017-11-18 10:14:00},
	{lng:117.979768,lat:26.624988,gtime:2017-11-18 10:14:30},
	{lng:117.98186,lat:26.627041,gtime:2017-11-18 10:15:00},
	{lng:117.982118,lat:26.627333,gtime:2017-11-18 10:15:30},
	{lng:117.981943,lat:26.627116,gtime:2017-11-18 10:16:00},
	{lng:117.979604,lat:26.624868,gtime:2017-11-18 10:16:30},
	{lng:117.97999,lat:26.623783,gtime:2017-11-18 10:17:00},
	{lng:117.983508,lat:26.622168,gtime:2017-11-18 10:17:30},
	{lng:117.985535,lat:26.618386,gtime:2017-11-18 10:18:00},
	{lng:117.987306,lat:26.615073,gtime:2017-11-18 10:18:30},
	{lng:117.985871,lat:26.612095,gtime:2017-11-18 10:19:00},
	{lng:117.98921,lat:26.611441,gtime:2017-11-18 10:19:30},
	{lng:117.989651,lat:26.607443,gtime:2017-11-18 10:20:00},
	{lng:117.991838,lat:26.603053,gtime:2017-11-18 10:20:30},
	{lng:117.991838,lat:26.603053,gtime:2017-11-18 10:21:00},
	{lng:117.99626,lat:26.60009,gtime:2017-11-18 10:21:30},
	{lng:118.000346,lat:26.598651,gtime:2017-11-18 10:22:00},
	{lng:118.003123,lat:26.599458,gtime:2017-11-18 10:22:30},
	{lng:118.006169,lat:26.60136,gtime:2017-11-18 10:23:00},
	{lng:118.009031,lat:26.603126,gtime:2017-11-18 10:23:30},
	{lng:118.009966,lat:26.600613,gtime:2017-11-18 10:24:00},

	{lng:118.011891,lat:26.598214,gtime:2017-11-18 10:24:30},
	{lng:118.015425,lat:26.594706,gtime:2017-11-18 10:25:00},
	{lng:118.018549,lat:26.590096,gtime:2017-11-18 10:25:30},
	{lng:118.022576,lat:26.586879,gtime:2017-11-18 10:26:00},
	{lng:118.025974,lat:26.582911,gtime:2017-11-18 10:26:30},
	{lng:118.031168,lat:26.580845,gtime:2017-11-18 10:27:00},
	{lng:118.031168,lat:26.580845,gtime:2017-11-18 10:27:30},
	{lng:118.03714,lat:26.57928,gtime:2017-11-18 10:28:00},
	{lng:118.043603,lat:26.579563,gtime:2017-11-18 10:28:30},
	{lng:118.049168,lat:26.58112,gtime:2017-11-18 10:29:00},
	{lng:118.054583,lat:26.583708,gtime:2017-11-18 10:29:30},
	{lng:118.057948,lat:26.58786,gtime:2017-11-18 10:30:00},
	{lng:118.061786,lat:26.589904,gtime:2017-11-18 10:30:30},
	{lng:118.06646,lat:26.591661,gtime:2017-11-18 10:31:00},
	{lng:118.071136,lat:26.592871,gtime:2017-11-18 10:31:30},
	{lng:118.072831,lat:26.59492,gtime:2017-11-18 10:32:00},
	{lng:118.073666,lat:26.594211,gtime:2017-11-18 10:32:30},
	{lng:118.073666,lat:26.594211,gtime:2017-11-18 10:33:00},
	{lng:118.073676,lat:26.594033,gtime:2017-11-18 10:33:30},
	{lng:118.073676,lat:26.594033,gtime:2017-11-18 10:34:00},
	{lng:118.073676,lat:26.594033,gtime:2017-11-18 10:34:30},
	{lng:118.073075,lat:26.590653,gtime:2017-11-18 10:35:00},
	{lng:118.073075,lat:26.590653,gtime:2017-11-18 10:35:30}
*/
];

</script>
</html>
