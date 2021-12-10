<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>布控任务——布控申请目标捕获消息记录</title>
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	<style type="text/css">
	.MarskDiv{width:295px; height:52px; position:absolute; z-index:2; top:412px; left:0;}
	</style>
</head>
<body>
<div id="content-d" class="MC_con content light">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="${elementsCollectionStr!}" />
	<div class="ProcessingLink" style="padding:0;">
	    <#if targetRecordList?? && (targetRecordList?size > 0)>
	    	<div class="t_pic"></div>
	    	
	    	<#list targetRecordList as l>
	    		<div id="<#if l.recordId??>${l.recordId?c}</#if>" class="LinkList">
	    			<input type="hidden" id="controlTargetObject" value="${l.controlTargetObject!}" />
	    			
		        	<div class="WitchLink FontGreen Check_Radio fl">
		        		<div id="deviceTypeNameDiv" value="${l.eventBussinessType!}"></div>
		        	</div>
		        	<div class="LinkDot fl" style="cursor:pointer;" onclick="selectOne(<#if l.recordId??>${l.recordId?c}</#if>)"><img src="${rc.getContextPath()}/images/link_11.png" /></div>
		        	<div class="LinkDotNow fl hide"><img src="${rc.getContextPath()}/images/link_12.png" /></div>
		        	<div id="traceInfoDiv" class="WhoHandle fl" style="width: 40%;">
		        		<p>
		        			<span id="tracingTimeSpan">
		        				<#if l.eventTime??>${l.eventTime?string("yyyy-MM-dd HH:mm:ss")}</#if>
		        			</span> 
		        		</p>
		        		<p>
		        			<span id="eqpNameSpan" class="FontDarkBlue">${l.eqpName!}</span>&nbsp;捕获
		        		</p>
		        	</div>
		        	
		        	<div class="clear"></div>
		        </div>
	    	</#list>
	    	
	    	<div class="f_pic"></div>
	    <#else>
	    	<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>
	    </#if>
	</div>
</div>

<div class="BigTool">
	<div id="operateBtnMask" class="MarskDiv hide"></div>
	<div class="BtnList">
		<a href="#" id="startTracingBtn" onclick="startTracing();" class="BigNorToolBtn SubmitBtn">播放轨迹</a>
    </div>
</div>
        
<script type="text/javascript">
	var $recordIdArray = null,
		$eventDevoiceIdArray = null,
		$arrayIndex = 0,
		$tracingInterval = null,
		$recordListSize = parseInt("<#if targetRecordList??>${targetRecordList?size}<#else>0</#if>", 10);
	
	$(function() {
		//设备类型映射关系
		var typeNameObj = {"1": "门禁设备", "2": "访客机", "3": "卡口设备", "4": "车闸设备", "5": "人脸识别设备"};
		
		$("div [id='deviceTypeNameDiv']").each(function() {
			var val = $(this).attr("value");
			var typeName = typeNameObj[val];
			
			if(!typeName) {
				typeName = "";
			}
			
			$(this).html(typeName);
		});
		
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        }; 
        enableScrollBar('content-d',options); 
        
		gisPosition();
		
	});
	
	function selectOne(resId){
	  	if(resId) {
		  	var elementsCollectionStr = $('#elementsCollectionStr').val();
			if(elementsCollectionStr != "") {
				window.parent.getDetailOnMapOfListClick(elementsCollectionStr, 380,380,resId)
			}
		}
	}
	
	//地图定位
	function gisPosition(){
		var resIds = "";
		
		<#if targetRecordList?? && (targetRecordList?size > 0)>
	    	<#list targetRecordList as l>
	    		<#if l.eventDevoiceId??>
	    			resIds += ",${l.eventDevoiceId?c}"; 
	    		</#if>
	    	</#list>
	    </#if>
	    
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 195;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfEquipment.jhtml?ids="+res+"&isIdssControl=true&controlTargetId=${controlTargetId!}";
			return parent.MMApi.markerIcons(opt);
		}
		
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer('idssControlLayer');
		}else {
			window.parent.currentListNumStr = "";
		}
		
	  	if(resIds) {
	  		resIds = resIds.substring(1);
	  		
		  	var elementsCollectionStr = $('#elementsCollectionStr').val();
			if(elementsCollectionStr != "") {
				var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfEquipment.jhtml?ids="+resIds+"&isIdssControl=true&controlTargetId=${controlTargetId!}";
				window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
				window.parent.getArcgisDataOfZhuanTi(url, elementsCollectionStr, 380,380);
			}
		}
	}
	
	//开始展示捕获过程
	function startTracing() {
		if($recordListSize) {
			$recordIdArray = new Array();
			$eventDevoiceIdArray = new Array();
			$arrayIndex = 0;
			$eventDevoiceIdIndex = 0;
			
			$("#operateBtnMask").show();
			
			<#if targetRecordList?? && (targetRecordList?size > 0)>
		    	<#list targetRecordList as l>
		    		<#if l.recordId??>
		    			$recordIdArray.push("${l.recordId?c}");
		    			$eventDevoiceIdArray.push("${l.eventDevoiceId?c}");
		    		</#if>
		    	</#list>
		    </#if>
		    
		    $tracingInterval = setInterval("currentTracing()", 3000);
	    }
	}
	
	//展示当前
	function currentTracing() {
		var recordId = $recordIdArray[$arrayIndex++],
			eventDevoiceId = $eventDevoiceIdArray[$eventDevoiceIdIndex++],
			recordBakId = $recordIdArray[($arrayIndex - 2 + $recordListSize) % $recordListSize];//为了能够循环
		
		if(recordId) {
			//恢复上一捕获信息样式
			$("#"+ recordBakId + ' .LinkDot').show();
			$("#"+ recordBakId + ' .LinkDotNow').hide();
			$("#"+ recordBakId + ' div[id="deviceTypeNameDiv"]').removeClass("FontCurrent");
			$("#"+ recordBakId + ' div[id="traceInfoDiv"]').removeClass("FontCurrent");
			$("#"+ recordBakId + ' span[id="eqpNameSpan"]').addClass("FontDarkBlue");
			
			//修改当前捕获信息样式
			$("#"+ recordId + ' .LinkDot').hide();
			$("#"+ recordId + ' .LinkDotNow').show();
			$("#"+ recordId + ' div[id="deviceTypeNameDiv"]').addClass("FontCurrent");
			$("#"+ recordId + ' div[id="traceInfoDiv"]').addClass("FontCurrent");
			$("#"+ recordId + ' span[id="eqpNameSpan"]').removeClass("FontDarkBlue");
			
			pushTracingInfo(recordId);
			
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),380,380,recordId);
		}
		
		if($tracingInterval != null && $arrayIndex == $recordListSize) {
			$("#operateBtnMask").hide();
	    	clearInterval($tracingInterval);
	    }
	}
	
	//推送布控信息
	function pushTracingInfo(recordId) {
		if(recordId) {
			var tracingTime = $("#"+ recordId + ' span[id="tracingTimeSpan"]').eq(0).html(),
				deviceTypeName = $("#"+ recordId + ' div[id="deviceTypeNameDiv"]').eq(0).html(),
				eqpName = $("#"+ recordId + ' span[id="eqpNameSpan"]').eq(0).html(),
				controlTargetObject = $("#"+ recordId + ' input[id="controlTargetObject"]').eq(0).val();
		}
	}
</script>
</body>
</html>