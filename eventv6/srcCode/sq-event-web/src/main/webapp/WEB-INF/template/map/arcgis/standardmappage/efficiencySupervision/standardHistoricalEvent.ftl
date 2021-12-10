<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史事件</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content" style="" id="content">
        	
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
var inputNum;
function pageSubmit(){
inputNum = $("#inputNum").val();
var pageCount = $("#pageCount").text();
if(isNaN(inputNum)){
	inputNum=1;
}
if(parseInt(inputNum)>parseInt(pageCount)){
	inputNum=pageCount;
}
if(inputNum<=0||inputNum==""){
	inputNum=1;
}
change('4');
}

function ShowOrCloseSearchBtn(){
var temp= $(".ListSearch").is(":hidden");//是否隐藏 
if(temp == false) {
	$(".ListSearch").hide();
}else {
	$(".ListSearch").show();
}
//var temp1= $(".ListSearch").is(":visible");//是否可见
	
}
function CloseSearchBtn(){
	$(".ListSearch").hide();
}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map0').offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    
	  /*  $('#order').combobox({
			onChange:function(){
				loadMessage(1,$("#pageSize").val());
			}
		});*/
	});
	var results="";//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize){
		results="";
		var gridId = $('#gridId').val();
		if(name=="==输入查询内容==") name="";
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId;
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController/historicalEventListData.json?t='+Math.random(),
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
				var list=data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  
					  var image1="";
					  var resourceImage="";
					  if(val.urgencyDegree == null || val.urgencyDegree == '01'){
						image1="<img alt=\"\" src=\"${uiDomain!''}/images/map/gisv0/unselected/event_01.gif\">";
					  }else{
						image1="<img alt=\"\" src=\"${uiDomain!''}/images/map/gisv0/unselected/event_"+val.urgencyDegree+".gif\">";
					  }
					  if(val.source == '03'){
						resourceImage='<div style="margin-top:-7px;margin-left: -5px; float:right;"><img style="width:42px;height:13px;" src="${rc.getContextPath()}/images/12345.png"></div>';
					  }
					  
					  var str="";
					  var str1="";
					  if(val.content == null){
						str="(无事件描述)";
					  }else{
					    str=val.content;
					  }
						
					  if(val.content == null){
						str1="(无事件描述)";
					  }else if(val.content.length>10){
						str1=val.content.substring(0,10)+"...";
					  }else{
						str1=val.content;
					  }	
					  
					  var handleDateStatus="";
					  if(val.handleDateStatus == null || val.handleDateStatus == '1'){
							handleDateStatus="<img width=\"15\" height=\"15\" title=\"处理时限正常\" src=\"${uiDomain!''}/images/map/gisv0/unselected/time_normal.png\" />";
						}else if(val.handleDateStatus == null || val.handleDateStatus == '2'){
							handleDateStatus="<img width=\"15\" height=\"15\" title=\"处理时限将到期\" src=\"${uiDomain!''}/images/map/gisv0/unselected/time_to_expire.png\" />";
						}else if(val.handleDateStatus == null || val.handleDateStatus == '3'){
							handleDateStatus="<img width=\"15\" height=\"15\" title=\"处理时限已过期\" src=\"${uiDomain!''}/images/map/gisv0/unselected/time_expired.png\" />";
						}
					  
					  tableBody+='<dl onClick="selected(\''+(val.taskInfoId==null?'':val.taskInfoId)+'\',\''+val.eventId+'\',\'event_1604_todo\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr" title=\'发生时间：'+val.happenTimeStr.substring(0,10)+'\'>'+val.happenTimeStr.substring(0,10)+'&nbsp;'+image1+(val.source == '03'? resourceImage : '')+'&nbsp;'+handleDateStatus+'</span>';
					  tableBody+='<b class="FontDarkBlue" title=\'事件分类：'+val.eventClass+'\'>'+val.typeName+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title=\'事件描述：'+str+'\'>'+str1+'</dd>';
					  tableBody+='</dl>';
					  results=results+","+val.eventId/*+"_"+(val.taskInfoId==null?'':val.taskInfoId)*/;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
	var currentPageNum=1;
	 //分页
     function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
			case '1':		//上页
			    if(pagenum==1){
			      flag=1;
			      break;
			    }
				pagenum = parseInt(pagenum) - 1;
				pagenum = pagenum < firstnum ? firstnum : pagenum;
				break;
			case '2':		//下页
			    if(pagenum==lastnum){
			      flag=2;
			      break;
			    }
				pagenum = parseInt(pagenum) + 1;
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
		    case '3':
		        flag=3;
		        pagenum=1;
		        break;
		    case '4':
		        pagenum = inputNum;
		        if(pagenum==lastnum){
			      flag=4;
			      break;
			    }
				pagenum = parseInt(pagenum);
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
			default:
				break;
		}
		
		if(flag==1){
		  alert("当前已经是首页");
		  return;
		}else if(flag==2){
		  alert("当前已经是尾页");
		  return;
		}
		currentPageNum = pagenum;
	    loadMessage(pagenum,pageSize);
	}
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	
	function selected(tid,eventId,type){
		setTimeout(function() {
			window.parent.localtionPendingPoint(tid,eventId);
		},1000);
	}
	
	//地图定位
	function gisPosition(res){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if(res==""){
			return;
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/dataOfEventScheduling/getArcgisLocateDataListOfPending.jhtml?ids="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfPending('"+url+"')";
		window.parent.getArcgisDataOfPending(url);
	}
</script>
</body>
</html>