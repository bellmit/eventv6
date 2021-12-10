<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>海康 布控对象列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style>
.bk-btn {
	display:block;
	float:left;
	cursor: pointer;
	padding: 2px 2px 2px 18px;
	margin: 4px 0 0 5px;
	color:#fff;
	line-height:15px;
	background-image:url(${rc.getContextPath()}/images/map/menu/controlApplyInfo.png);
	background-repeat:no-repeat;
	background-color:#2980B9;
	background-position:5% 50%;
	transition:all 0.2s;
	border-radius:3px;
}
.bk-btn:hover {
	color:#fff;
	text-decoration:none;
	background-color:#3498DB;
}
.hide{display: none;}
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="${elementsCollectionStr!}" />
	<input type="hidden" id="pageSize" class="queryParam" value="20" />
	
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div id="conditionDiv" class="condition">
        		<input type="hidden" id="infoOrgCode" class="queryParam" value="${infoOrgCode!}" />
        		<input type="hidden" id="eqpType" class="queryParam" value="${eqpType!}" />
        		<input type="hidden" id="pattern" class="queryParam" value="0" />
        		
        		<ul>
                	<li><input type="text" class="inp1 queryParam" id="controlTargetObject" onblur="if(this.value=='')value='输入身份证号、车牌号、手机号码进行查询';" onfocus="if(this.value=='输入身份证号、车牌号、手机号码进行查询')value='';" value="输入身份证号、车牌号、手机号码进行查询" defaultValue="输入身份证号、车牌号、手机号码进行查询" style="width:240px; *width: 233px;" /></li>
                </ul>
                <ul class="time">
                	<li><input id="controlBeginTimeStr" type="text" class="inp1 queryParam" title="布控开始时间" onfocus="if(this.value=='布控开始时间')value='';WdatePicker({isShowWeek:true});" value="布控开始时间" defaultValue="布控开始时间" style="width:110px; *width: 103px;" readonly="readonly" /></li>
                    <li style="width:13px; padding-left:7px;">-</li>
                	<li><input id="controlEndTimeStr" type="text" class="inp1 queryParam" title="布控结束时间" onfocus="if(this.value=='布控结束时间')value='';WdatePicker({isShowWeek:true});" value="布控结束时间" defaultValue="布控结束时间" style="width:110px; *width: 103px;" readonly="readonly" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
                </ul>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="showRecords">
        	<span class="bk-btn hide" title="查看布控任务" onclick="changeList();">布控任务</span>
          	<span>共查询到<span id="records">0</span>条记录</span>
        </div>
        <div class="ListShow content" style="" id="content">
        	
        </div>
        <div class="NorPage" style="">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
	$(document).ready(function(){
		initFunction("布控任务", "布控黑名单");
	});
	
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		
		var pageSize = $("#pageSize").val();
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		
		var postData = 'page='+pageNo+'&rows='+pageSize;
		var queryVal = "";
		var queryDefaultVal = "";
		
		$("#conditionDiv .queryParam").each(function() {
			queryVal = $(this).val();
			queryDefaultVal = $(this).attr("defaultValue");
			
			if(queryVal && queryVal != queryDefaultVal) {
				postData += '&' + $(this).attr('id') + '=' + queryVal;
			}
		});
		
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/listTargetRefData.jhtml?t='+Math.random(),  
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list= data.rows;
				var tableBody = '<div class="liebiao">';
				
				if(list && list.length > 0) {
					for(var i = 0, len = list.length;i < len; i++){
					  var val = list[i];
					  var controlTime = val.controlBeginTime;
					  var controlTargetObject = val.controlTargetObject;
					  
					  if(controlTime) {
					  	controlTime = dateFormat(controlTime);
					  	
					  	if(val.controlEndTime) {
					  		controlTime += "~" + dateFormat(val.controlEndTime);
					  	}
					  }
					  
					  if(!controlTargetObject) {
					  	controlTargetObject = "";
					  }
					  
					  tableBody+='<dl ';
					  if(val.captureTimes) {
					  	tableBody += ' onclick="selected(\''+ val.controlApplyId +'\', \''+ val.controlTargetId +'\')"';
					  } else {//捕获次数为0时，不可点击
					  	tableBody += ' style="cursor: default;" ';
					  }
					  tableBody += ' >';
						  tableBody+='<dt>';
							  tableBody+='<b class="FontDarkBlue">'+ controlTargetObject +'</b>';
							  
							  tableBody+='<dd>';
							  
							  tableBody+='<span class="fr">'+
										  	'捕获'+ (val.captureTimes ? val.captureTimes : 0) +'次'+
										 '</span>';
							  
							  tableBody+='<span>'+ controlTime +'</span>';
							  
							  tableBody+='</dd>';
						  
						  tableBody+='</dt>';
					  tableBody+='</dl>';
					}
				} else {
					tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
</script>

<#include "standardApplyInfoJs.ftl"/>

</body>
</html>