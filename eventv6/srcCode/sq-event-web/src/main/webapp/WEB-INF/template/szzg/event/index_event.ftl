<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>事件统计</title>
		<#include "/szzg/common.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/layer/layer.js"></script>
  <style type="text/css">
    .btn_hand{width:80px; height:35px;font-size:18px; line-height:18px; background:#4489ca; border:none; color:#fff; cursor:pointer; text-align:center; vertical-align:middle; margin-top:4px;padding-left: 0;margin-left:40px;}
	.fisdate{height:38px;margin-left:16px;}
    </style>
</head>

<body class="npmap">
<div class="npcityMainer">
  <div class="npcityBottom" style="left:0px;position:relative;">
      <div class="npAlarminfo citybgbox bpsmain" style="width:195px;height:217px;padding-top: 10px;">
        <div class="buildcon" style="margin-top:0px;">
		  <div class="fisdate">
                 开始日期:<input type="text" class="inp1 Wdate timeClass" id="begin"  value="${lastDate}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M-d'});" >
		 </div>
		<div class="fisdate">
			结束日期:<input type="text" class="inp1 Wdate timeClass" id="end"  value="${currentDate}"  style="width:100px;margin-left:7px;background-color:rgb(89,114,132);color:#fff"
					onClick="WdatePicker({readOnly:true,isShowOK:false,isShowClear:false, isShowToday:false,dateFmt:'yyyy-M-d'});" >
			 </div>
		<div class="fisdate">
		<button class="btn_hand" onclick="dateChange()">查询</button>
                </div>
          <div class="clearfloat"></div>
        </div><!--end .buildcon-->
      </div><!--end .bpsmain-->
    <div class="clearfloat"></div>
  </div><!--end .npcityBottom-->
</div><!--end .npcityMainer-->
</body>

<script type="text/javascript">
var heatLayer,parentMap,MAP,ESRI,WKID,textGraphic=[],areaGraphic=[],mediaList,gridLayer,COLOR;
 $(function(){
    parentMap = window.parent.$.fn.ffcsMap,MAP=parentMap.getMap(),ESRI = parentMap.getEsri(),COLOR = parentMap.getColor();
	WKID = window.parent.currentArcgisConfigInfo.wkid,gridLayer = MAP.getLayer("gridLayer"),gridGraphics = gridLayer.graphics,SYMBOL = parentMap.getSymbol(),
	mediaList=$("#media_list",parent.document);
	heatLayer = parentMap.createHeatMap("heatLayer", {
		"useLocalMaximum": true,single:true,singleMax:20,
        "radius": 20,
        "gradient": {
            "0.15": "rgb(000,000,255)",
            "0.65": "rgb(000,255,255)",
            "0.75": "rgb(000,255,000)",
            "0.85": "rgb(255,255,000)",
            "1.00": "rgb(255,000,000)"
        }
	});
	window.parent.eventHeatMap = heatLayer;
	parentMap.centerAt({
					x : parentMap.getCurrentMapCenterObj().centerPoint.x,          //中心点X坐标
					y : parentMap.getCurrentMapCenterObj().centerPoint.y,           //中心点y坐标
					wkid : window.parent.currentArcgisConfigInfo.wkid, //wkid 2437
					//zoom : 11
		    	});
	for(var i=0,l=gridGraphics.length;i<l;i++){//隐藏原有区域名称,新创建名称
		if(gridGraphics[i].attributes){
			areaGraphic.push(gridGraphics[i]);
		}else{
			textGraphic.push(gridGraphics[i]);
		}
	}
	var blackColor = COLOR.fromString("rgba(0,0,0,0)");
	for(var i=0,l=areaGraphic.length;i<l;i++){
		gridLayer.remove(areaGraphic[i]);
		areaGraphic[i].symbol.setColor(blackColor);
		gridLayer.add(areaGraphic[i]);
	}
	var fffColor = COLOR.fromString("#f00");
	for(var i=textGraphic.length,l=gridGraphics.length;i<l;i++){
			gridGraphics[i].setSymbol(new SYMBOL.SimpleFillSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID,
				new SYMBOL.SimpleLineSymbol(SYMBOL.SimpleLineSymbol.STYLE_SOLID, fffColor, 2),blackColor));
			gridGraphics[i].symbol.setColor(blackColor);
	}
	 gridGraphics = gridLayer.graphics;
	dateChange();
 });
 
var heatDataObj={'1':[],'2':[],'3':[],'4':[]};
 function dateChange(){
	parent.eventMeasure('clear');
	var beginV = document.getElementById("begin").value;
	var endV = document.getElementById("end").value;
	var month = 3;
	var layerIndex = layer.load(1);
	var time = (new Date()).getTime();
	$.ajax({
        url: '${rc.getContextPath()}/zhsq/szzg/eventController/findEventHeatData.json',
        type: 'POST',
       data: { beginTime : beginV,endTime :endV,month:month},
        dataType:"json",
        error: function(data){
        	 parent.layer.alert('信息获取异常!');
        },
        complete:function(){
       	 layer.close(layerIndex); 
        },
        success: function(data){
        	if(!data.success){//校验失败
        		parent.layer.alert(data.msg==1?('结束日期必须大于开始日期4天以上'):('开始日期与结束日期必须小于'+month+'个月'));
        		return
        	}
			time1 = (new Date()).getTime();
			console.log('time0:'+data.allTime0+"\t"+data.list1.length);
			console.log('time1:'+data.allTime1+"\t"+data.list2.length);
			console.log('time2:'+data.allTime2+"\t"+data.list3.length);
			console.log('time3:'+data.allTime3+"\t"+data.list4.length);
			console.log('total:'+(data.list1.length+data.list2.length+data.list3.length+data.list4.length));
			console.log('ajax:'+(time1-time));
        	
			parent.document.getElementById("wrap_media").style.display='block';
			parent.document.getElementById("minID").value=data.MIN_ID?data.MIN_ID:0;
			parent.document.getElementById("maxID").value=data.MAX_ID?data.MAX_ID:0;

			for(var i=0;i<5;i++){
				$(window.parent.document.getElementById("media_time_"+i)).html(data.dayArr[i]);
			}
			//mediaList.children().eq(0).addClass("active").siblings().removeClass("active");
			heatDataObj={'0':[],'1':[],'2':[],'3':[],'4':[]};
			var spatialReference= new ESRI.SpatialReference({ wkid : window.parent.currentArcgisConfigInfo.wkid });
			for(var j=1;j<5;j++){
				var list = data['list'+j];
				for(var i=0,l=list.length;i<l;i++){
					var heatObj = {},xy=list[i].split(":");
					heatObj.attributes ={count:1};
					heatObj.geometry = new ESRI.geometry.Point(xy[0], xy[1] ,spatialReference);
					if(j==1)heatDataObj['1'].push(heatObj);
					if(j<=2)heatDataObj['2'].push(heatObj);
					if(j<=3)heatDataObj['3'].push(heatObj);
					if(j<=4)heatDataObj['4'].push(heatObj);
				}
			}
			time2 = (new Date()).getTime();
			
			showHeat(0);
			<#if autoPlay=="true">
			parent.eventMeasure('play');
			</#if>
			time3 = (new Date()).getTime();
			
			
		}
    });
 }
 
 var heatIndex = 0,timeoutIndex=0;
 function showHeat(num){
	if(num != undefined ){ 
		heatIndex = num;
		clearTimeout(timeoutIndex);
		$("#play_box",parent.document).removeClass("on");
	}
	if(heatIndex>4){
		heatIndex = 0;
		$("#play_box",parent.document).removeClass("on");
		return;
	}
	$(".media_time",parent.document).children().removeClass("active");
	$("#media_time_"+(heatIndex),parent.document).parent().addClass("active");
	mediaList.children().eq(heatIndex).addClass("active").siblings().removeClass("active");
	heatLayer.clearData();
	heatLayer.setData(heatDataObj[heatIndex]);
	//heatLayer.setSingleData(heatDataObj[heatIndex]);
	if(num != undefined ){ 
		$("#play_box",parent.document).removeClass("on");
	return;}
	timeoutIndex = setTimeout(function(){
		showHeat();
	},1000);
	if(heatIndex++>4){
		heatIndex = 0;
		$("#play_box",parent.document).removeClass("on");
		return;
	}
 }
 
 function clear_(){
	clearTimeout(timeoutIndex);
 }
</script>
</html>
