<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- External CSS -->
    <title></title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/css/pop.css"/>
    <!-- JavaScript -->
    <script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
    <style>
    	.cursor{cursor:pointer;}
    	.overflow{width:260px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;}
    	.underline{text-decoration:underline;}
    </style>
</head>

<body>    
    <!-- 弹窗 -->
	<div class="pop-ol" style="display:none;width:280px; height:190px;">
    	<div class="pop-ol-title">
     		<h5 id="titleHtml">待办通知</h5>
     		<a class="icon_close" style="cursor:pointer;" onclick="closeDoor();"></a>
    	</div>     		
    	<ol id="contentHtml" style="margin-left:5px;overflow-y:auto;height: 145px;white-space: nowrap;">   
    		<!-- 		
    		<li id="willExpire" ></li> 
    		<li id="todo" ></li>
    		<li id="interconnection" ></li>
    		-->
    	</ol>
  	</div>
  	<div id="newMessageDIV" style="display:none"></div>   
</body>
<script>	
	var modeType = "fuzhou";
	setInterval("getPageNum()",30000);	
	$(function(){
		getPageNum();
		//美化滚动条
		$("#contentHtml").niceScroll({
			cursorcolor: "#fff",//#CC0071 光标颜色
			cursoropacitymax: 0.5, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
			touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
			cursorwidth: "5px", //像素光标的宽度
			cursorborder: "0", // 游标边框css定义
			cursorborderradius: "5px",//以像素为光标边界半径
			autohidemode: true //是否隐藏滚动条
		});
	});
													
	function getPageNum(){	
		$.ajax({
			type : 'POST',
			dataType : "jsonp",		
			data: {'page': '1', 'rows': '20','eventType':'todo'},			
			url : "${rc.getContextPath()}/zhsq/event/eventDisposal4OuterController/fetchEventData4Jsonp.json?jsonpcallback=setAllEvent",
			success: function(data) {	
														
			}
		});
	}
	
	function setAllEvent(data){
		var html = "";
		if(data != null && data.list.length > 0){
			$(".pop-ol").show();
			for(var i=0,l=data.list.length;i<l;i++){
				var urgencyResults = "";
				var results = "";
				var val = data["list"][i];
				var eId = val.eventId+","+val.workFlowId+","+val.instanceId+",null,todo";
				if(val.urgencyDegree!="01"){
			    	urgencyResults = val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!todo";
			    }else{
			    	results = val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!todo";
			    }
				html += '<li title="'+val["eventName"]+'" onclick="showDetailRow(this,\''+eId+'\',\''+val.instanceId+'\',\''+val.workFlowId+'\',\''+val.type+'\',\''+val.urgencyDegree+'\',\''+urgencyResults+'\',\''+results+'\');" class="cursor" >';
				html += '<div class="overflow">'+(parseInt(i)+parseInt(1))+'、<span class="underline">'+val["eventName"]+'</span></div>';
				html += '</li>';
			}
		}
		$("#contentHtml").html(html);
	}
	
	function closeDoor(){
		$(".pop-ol").hide();
	}
	
	function play(){ 
	    if($.browser.msie && $.browser.version=='8.0'){   
	        $('#newMessageDIV').html('<embed src="${rc.getContextPath()}/object/normal2.mp3"/>');   
	    }else{   
	        //IE9+,Firefox,Chrome均支持<audio/>   
	        $('#newMessageDIV').html('<audio autoplay="autoplay"><source src="${rc.getContextPath()}/object/normal2.mp3"'   
	        + 'type="audio/wav"/><source src="${rc.getContextPath()}/object/normal2.mp3" type="audio/mpeg"/></audio>');   
	    }   
	}
	
	
	function showDetailRow(obj,eventId,instanceId,workFlowId,type, urgencyDegree,urgencyResults,results){
        if (modeType == "fuzhou") {
            clearSpecialLayer("eventLayer");
            clearSpecialLayer("urgencyEventLayer");
			var urgencyResults = "";
			var results = "";
            var arr = new Array();
            arr = eventId.split(",");
			results = arr[0]+"!"+arr[1]+"!"+arr[2]+"!"+arr[3]+"!"+arr[4];
            urgencyGisPosition(urgencyResults,results);//先定位
		}
		localtionEventPoint('${eventType}',eventId,instanceId,workFlowId,type,urgencyDegree,modeType);
	}
	
	//--定位
	function urgencyGisPosition(res,res1){
		
		if(res!=""){
			var ids = "";
			var results =  new Array();
			results = res.split(",");
			for(i=0;i<results.length;i++){
				var result = new Array();
				result = results[i].split("!");
				ids = ids + "," + result[0];			
				//console.info(result[0]);
			}
			ids=ids.substring(1, ids.length);
			//console.info("ids -> "+ids);
			
			if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
				var opt = {};
				opt.w = 340;
				opt.h = 230;
				opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2";
				parent.MMApi.markerIcons(opt, "urgencyEventLayer");
			} else {
				if("1" != IS_ACCUMULATION_LAYER) {
					clearSpecialLayer("urgencyEventLayer");
				}else {
//					if(currentListNumStr.indexOf(currentPageNum+"")>=0) {
//						return;
//					}else {
						currentListNumStr = currentListNumStr+","+currentPageNum;
//					}
				}
				var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2&modeType="+modeType;
				getArcgisDataOfUrgencyEvent(url,res);
			}
		}
		
		gisPosition(res1,res);
	}
	//--定位
	function gisPosition(res,res1){
		if(res==""){
			return;
		}
		var ids = "";
		var results =  new Array();
		results = res.split(",");
		for(i=0;i<results.length;i++){
			var result = new Array();
			result = results[i].split("!");
			ids = ids + "," + result[0];			
			//console.info(result[0]);
		}
		ids=ids.substring(1, ids.length);
		//console.info("ids -> "+ids);
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 230;
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2";
			return parent.MMApi.markerIcons(opt, "eventLayer");
		} else {
			if("1" != IS_ACCUMULATION_LAYER) {
				clearSpecialLayer("eventLayer");
			}else {
				if(res1==""){
//					if(currentListNumStr.indexOf(currentPageNum+"")>=0) {
//						return;
//					}else {
						currentListNumStr = currentListNumStr+","+currentPageNum;
//					}
				}
			}
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2&modeType="+modeType;
			currentLayerLocateFunctionStr="getArcgisDataOfEvent('"+url+"','"+res+"')";
			getArcgisDataOfEvent(url,res);
		}
	}
	
	
</script>
</html>