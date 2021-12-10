<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html style="background-color: rgba(0, 0, 0, 0);">
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>平安智慧社区大数据分析-综合执法</title>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/screen-yanduqu.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/yancheng/css/screen-yanduqu-new.css"/>

		<!--引入 重置默认样式 statics/basic -->
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<script type="text/javascript" src="${uiDomain}/js/layer/layer.js"></script>
		<script type="text/javascript" src="${rc.getContextPath()}/js/paging.js"></script>
	<style>
	.ydq-mask{display:block;    background-color: rgba(0, 0, 0, 0);}
	.ydq-mc-item1{display:block;}
	<#if closeEventHappenTime>
		.ydq-mci-event-table>table thead tr th:first-child{width:400px;}
		.ydq-mci-event-table>table thead tr th:nth-child(2){width:240px;}
		.ydq-mci-event-table>table thead tr th:nth-child(5){width:105px;}
		.ydq-mci-event-table>table thead tr th:nth-child(6){width:150px;}
	<#else>
		.ydq-mci-event-table>table thead tr th:first-child{width:400px;}
		.ydq-mci-event-table>table thead tr th:nth-child(2){width:240px;}
		.ydq-mci-event-table>table thead tr th:nth-child(3){width:240px;}
		.ydq-mci-event-table>table thead tr th:nth-child(6){width:105px;}
		.ydq-mci-event-table>table thead tr th:nth-child(7){width:150px;}
	</#if>
	.wrap-container{background:none;}
.zxfPagenum{
    color: #7fafff;
	    padding: 0 5px;
	height:20px;
	line-height:20px;
	margin:0 5px;
	border-radius:3px
}
.nextpage{
	margin:0 5px
}
.nextbtn,.prebtn,span.disabled{
	margin: 0px 10px;
	height:42px;
	line-height:42px;
	border-radius:3px;
	color:#7fafff;
	    font-size: 14px;
}
.zxfinput{
	width:50px;
	height:29px;
	text-align:center;
	box-sizing:border-box;
	margin:0 12px;background-color: rgba(0,0,0,0);
	border-radius:3px;

border: solid 1px rgba(0, 176, 255, 0.5);
    color: #7aa9ff;
}.zxfokbtn{
	width:48px;
	height:32px;
	line-height:32px;
	margin-left:10px;
	cursor:pointer;
	border-radius:3px;
}
input::-webkit-outer-spin-button,input::-webkit-inner-spin-button{
	-webkit-appearance:none
}
input[type=number]{
	-moz-appearance:textfield
}
.current {
    background: #1abc9c;
    height: 20px;
    padding: 0 5px;
    line-height: 20px;
    border-radius: 3px;
}
.list_div{
	display: -moz-box; 
	display: -ms-flexbox;
	display: -webkit-flex;
	display: flex;
	-webkit-justify-content:center;
	-moz-justify-content:center;
	-ms-justify-content:center;
	justify-content: center;
	-webkit-align-items:center;
	-moz-align-items:center;
	-ms-align-items:center;
	align-items: center;
	color:#7fafff;
	    font-size: 14px;
}
		.mt45 {
    margin-top: 15px;
}
.mt40 {
    margin-top: 40px;
}

	</style>
		<script type="text/javascript">
			//设置页面分辨率自适应
			$(function(){
				var scale, winW, winH, pageW, pageH, scaleW, scaleH;
				$(window).on('load resize', function(){
						fullPage();
				});
				function fullPage(){//将页面等比缩放
					winW = $(window).width();
					winH = $(window).height();
					scale = winW/1920;
					pageW = $('.wrap-container').width();
					pageH = $('.wrap-container').height();
					scaleW = pageW*scale;
					scaleH = pageH*scale;
					if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){//判断浏览器是否为火狐浏览器
						$('.wrap-container').css({'WebkitTransform': 'scale('+scale+')', 'MozTransform': 'scale('+scale+')', 'MsTransform': 'scale('+scale+')', 'transform': 'scale('+scale+')', 'top': (scaleH - 1080)/2, 'left': (scaleW - 1920)/2});
					}else{
						$('.wrap-container').css({'zoom': scale});
					}
					$('.container-fluid').css({'width': 1920*scale, 'height': '100%', 'overflow': 'hidden'})
				}
			});
		</script>
	</head>
	<body style="background-color: rgba(0, 0, 0, 0);">
		<div class="container-fluid">
			<div class="wrap-container">
				<!-- 弹窗 -->
				<div class="ydq-mask" >
						<!-- 事件列表 -->
						<div class="ydq-mc-item ydq-mc-item1">
							<div class="ydq-mci-title flex flex-jc mt40">
								<p>事件列表</p>
							</div>
							<div class="ydq-mci-event-table" style="width:90%;margin-top: 20px;height:750px;">
								<p>共计 <span  id="eventTypeCount">0</span> 个</p>
								<table>
									<thead>
										<tr><th>事件标题</th>
													<#if closeEventHappenTime == null>
													<th>事发时间</th>
													</#if>
													<th>办结期限</th>
													<th>事件分类</th>
													<th>所属网格</th>
													<th>事件状态</th>
													<th>采集时间</th></tr>
									</thead>
									<tbody id="eventTypeList">
										
									</tbody>
								</table>
								<div class="ydq-mciet-page flex flex-jc" id="pageDiv" style="position: absolute;top: 810px;">
									<div class="clearfix">
										<div id="eventTypePage" style="font-size:14px;" class="list_div"></div>
									</div>
								</div>
							</div>
						</div>
						
						<!-- 事件详情 -->
						<div class="ydq-mc-item ydq-mc-item2">
						<iframe id="ifr" scrolling="no" frameborder="0" style="width:100%;height:100%;"></iframe>
						</div>
								</div>
							</div>
						</div>						
	</body>
	
<script type="text/javascript">
$(function(){
	getList('eventType',1,14);
})
function listChange(num){
	$(".event_a").removeClass("active");
	$("#event_"+num).addClass("active");
	$(".event_div").hide();
	$("#div_"+num).show();
}
var i=0;
function getList(type,pageNo,pageSize){
var layerIndex = layer.load(1);
	var data = {'page':pageNo,'rows':pageSize,'infoOrgCode':"${code!''}",'eventType': 'all',
		'createTimeStart':"${beginTime!''}",'createTimeEnd':"${endTime!''}",'type': '${type}','eventStatus':'${status}'};
	<#if handleDateFlag??>
		data["handleDateFlag"] = "${handleDateFlag}";
	</#if>
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json',
		dataType : "json",
		data: data, 
		success: function (data) {
			if(type == 'eventType'){
				if(i++==0){
					if(data.total<14){$("#pageDiv").hide();}
					$("#eventTypePage").createPage({
						pageNum: (Math.floor((data.total-1)/pageSize)+1),
						current: pageNo,
						backfun: function(e) {
							getList('eventType',e.current,pageSize);
						}
					});
				}
				setEvent(data,'eventType');
			}
			
		},
		complete:function(){
			layer.close(layerIndex); 
		},
		error: function (data) {
			layer.alert('连接超时！');
		}
	})
}
function fmtDate(num){
	if(num == null || num == undefined ){
		return '';
	}
	var date=new Date(num);		
	var time=date.getFullYear()+"-"+fnW((date.getMonth()+1))+"-"+fnW(date.getDate())+" "
	+fnW(date.getHours())+":"+fnW(date.getMinutes())+":"+fnW(date.getSeconds());
	return time;
}

 function dateFormatter(value, rowData, rowIndex) {
        if(value && value.length >= 10) {
            value = value.substring(0,10);
        }

        return value;
    }
 

function fnW(str){
	return (str>9?str:"0"+str)+'';
	} 

function strFn(str){
	if(str == null ||str == 'null' || str == undefined ){
		return '';
	}
	return str;
}	

function omitSSQ(str){
	//盐都去除省市区
	return str.replace("${buildScopeSetting}", '');
}

var closeEventHappenTime = "${closeEventHappenTime}";

function setEvent(data,t){
	$("#"+t+"Count").html(data.total);
	var str = "",d="";
	var flag=false;
	if(closeEventHappenTime == "true"){
		flag = true;
	}
	if(data.rows != null && data.rows.length > 0){
		if(flag){
			for(var i=0,l=data.rows.length;i<l;i++){
				d = data.rows[i];
				str+="<tr onclick='showEventInfo("+d.eventId+")'><td title='"+strFn(d.eventName)+"'>"+strFn(d.eventName)+"</td><td>"+d.happenTimeStr+"</td><td style='display:none;'>"
				+d.handleDateStr+"</td><td title='"+strFn(d.eventClass)+"'>"+strFn(d.eventClass)+"</td><td title='"+strFn(d.gridPath)+"'>"+omitSSQ(strFn(d.gridPath))+
				"</td><td title='"+strFn(d.statusName)+"'>"+strFn(d.statusName)+"</td><td>"+dateFormatter(d.createTimeStr)+"</td></tr>";
			}
		}else{
			for(var i=0,l=data.rows.length;i<l;i++){
				d = data.rows[i];
				str+="<tr onclick='showEventInfo("+d.eventId+")'><td title='"+strFn(d.eventName)+"'>"+strFn(d.eventName)+"</td><td>"+d.happenTimeStr+"</td><td>"
				+d.handleDateStr+"</td><td title='"+strFn(d.eventClass)+"'>"+strFn(d.eventClass)+"</td><td title='"+strFn(d.gridPath)+"'>"+strFn(d.gridPath)+
				"</td><td title='"+strFn(d.statusName)+"'>"+strFn(d.statusName)+"</td><td>"+dateFormatter(d.createTimeStr)+"</td></tr>";
			}
		}
	}
	$("#"+t+"List").html(str);
	
}

function setCg(data){
	$("#cgCount").html(data.total);
	var str = "",d="";
	for(var i=0,l=data.rows.length;i<l;i++){
		d = data.rows[i];
		str+="<tr onclick='showEventInfo(0,"+d.ID_+")'><td title='"+strFn(d.TITLE)+"'>"+strFn(d.TITLE)+"</td><td title='"+strFn(d.CONTENT_)+"'>"+strFn(d.CONTENT_)+"</td><td title='"+strFn(d.ADDRESS)+"'>"+strFn(d.ADDRESS)+
		"</td><td>"+fmtDate(d.TIME_)+"</td></tr>";
	}
	$("#cgList").html(str);
}
var layerIndex2=0;
function showEventInfo(eventId,caseId){
	var url = "${rc.getContextPath()}/zhsq/szzg/eventController/getEventInfo.jhtml?";
	if(eventId>0){
		url += "eventId="+eventId;
	}else{
		url+="caseId="+caseId;
	}
	$("#ifr").attr("src",url);
	$(".ydq-mc-item1").hide();
	$(".ydq-mc-item2").show();
	
}
function closeInfo(){
	$(".ydq-mc-item2").hide();
	$(".ydq-mc-item1").show();
	$("#ifr").attr("src","");
}
</script>
</html>
