<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
        		<ul>
                	<li class="LC1">事件内容：</li>
                	<li class="LC2">
                	<input id="name" name="name" type="text" class="inp1" />
                		
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" type="button" value="查询" class="NorBtn" /></li>
                </ul>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content" style="" id="content">
        	
        </div>
        <div class="NorPage">
        <input type="hidden" id="pageSize" value="20" />
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>
	
<script type="text/javascript">
var inputNum;
var results="";//获取定位对象集合

$(document).ready(function(){
    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
   	$("#content").height(winHeight-56); 
    loadMessage(1,$("#pageSize").val());
    /*
    $('#order').combobox({
		onChange:function(){
			loadMessage(1,$("#pageSize").val());
		}
	});
	*/
});
$('#name').keydown(function(e){ 
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
}); 
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = "eventSchedulingLayer";
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var gridId = $('#gridId').val();
		var name = $("#name").val();
		if(name=="==输入姓名==") name="";
		var order="time";
		
		var urgency_value ="";
		var type_value ="";
		var influence_value ="";
		var status_value ="";
		var time_value = "";
		var startHappenTime = "";//$('#startHappenTime').datebox('getValue');
		var endHappenTime = "";//$('#endHappenTime').datebox('getValue'); 
		var startCollectTime = "";//$('#startCollectTime').datebox('getValue');
		var endCollectTime = "";//$('#endCollectTime').datebox('getValue');
		
		var content = $('#name').val();
		urgency_value =$('#urgencyDate option:selected').val();//选中的值
		type_value =$('#typeDate option:selected').val();//选中的值
		influence_value =$('#influenceDate option:selected').val();//选中的值
		status_value =$('#statusName option:selected').val();//选中的值
		time_value ="";//选中的值
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&content='+name+'&statusName=${statusName}';
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/dataOfEventScheduling/pendingData.json?t='+Math.random(),
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
					    var eventClass = val.eventClass;
						if(eventClass!=null){
							eventClass = val.eventClass.substring(val.eventClass.indexOf("-")+1,val.eventClass.length);
						}
					    var content = val.content;
						if(content==null || content==""){
							content = "";
						}else if(content.length > 18){
							content = content.substring(0,18)+"...";
						}
						var img = "";
						var happenTimeStr = (val.happenTimeStr==null)?"无发生日期":val.happenTimeStr.substr(0,10);
					    var createDateStr = (val.createDateStr==null)?"无采集日期":val.createDateStr.substr(0,10);
					    
					    var baseInfo = happenTimeStr+' '+val.urgencyDegreeName+' '+val.influenceDegreeName;
					    if(baseInfo==null || baseInfo==""){
							baseInfo = "";
						}else if(baseInfo.length > 14){
							baseInfo = baseInfo.substring(0,14)+"...";
						}
					    
					    tableBody+='<dl onclick="selected(\''+(val.taskInfoId==null?'':val.taskInfoId)+'\',\''+val.eventId+'\',\'1201\')">';
						tableBody+='<dt>';
						tableBody+='<span class="fr"   title=\''+happenTimeStr+'&nbsp'+val.urgencyDegreeName+'&nbsp'+val.influenceDegreeName+'\'>'+baseInfo+'</span>';
						tableBody+='<b class="FontDarkBlue">'+(val.typeName==null?'':val.typeName)+'</b>';
						tableBody+='</dt>';
						tableBody+='<dd  title=\''+val.content+'\'>'+content+'</dd>';
						tableBody+='</dl>';
					    
						results=results+","+val.eventId+"_"+(val.taskInfoId==null?'':val.taskInfoId);
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<ul>未查到相关数据！！</ul>';
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

	function selected(tid,eventId,type){
	//	window.parent.choiseEvent();
	//	window.parent.sj_flag = 1;
	//	window.parent.autoGoMap(window.parent.localMapt,window.parent.mapt);
	//	window.parent.locationEventById(tid,eventId,type);
	//	gisPosition(tid);
		
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 660;
			opt.h = 240;
			opt.taskId = tid;
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(eventId, opt, "oldEvent");
		}
		
		setTimeout(function() {
			window.parent.localtionPendingPoint(tid,eventId);
		},1000);
	
	    //window.parent.localtionPendingPoint(tid,eventId);
	}
	
	//地图定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 660;
			opt.h = 240;
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/dataOfEventScheduling/getArcgisLocateDataListOfPending.jhtml?ids="+res+"&showType=2";
			return parent.MMApi.markerIcons(opt, "oldEvent");
		}
		
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

<script language="javascript">
	// 以下获取当前日期 yyyy-mm-dd
	function curDateTime() {
		var d = new Date();
		var year = d.getFullYear();
		var month = d.getMonth()+1;
		var date = d.getDate();
		var curDateTime= year;
		if(month>9)
			curDateTime = curDateTime +"-"+month;
		else
			curDateTime = curDateTime +"-0"+month;
		if(date>9)
			curDateTime = curDateTime +"-"+date;
		else
			curDateTime = curDateTime +"-0"+date;
		return curDateTime;
	}
	
	//| 求两个时间的天数差 日期格式为 YYYY-MM-dd
	function daysBetween(DateOne,DateTwo) {
	    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));
	    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);
	    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));
	    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));
	    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);
	    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));
	    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);
	    return Math.abs(cha);
	}
</script>
</body>
</html>