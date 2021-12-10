<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>公交车</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style>
.ListSearch{display:block;height:auto;}
#content{margin-top: 25px;}
.t-clo{
	cursor:pointer;
	display: inline-block;
	border-left:10px solid transparent;
	border-right:10px solid transparent
}
.t-clo.down{
	border-top: 10px solid rgba(255, 0, 102,1);
	border-bottom: 10px solid rgba(68, 138, 202,.2)
}
.t-clo.up{
	border-top: 10px solid rgba(255, 0, 102,.2);
	border-bottom: 10px solid rgba(68, 138, 202,1)
}
.liebiao dl{position: relative;}
.c-detail{
	background:url('${rc.getContextPath()}/images/ColumnView.png');
	position: absolute;
    width: 22px;
    height: 20px;
    top: 18px;
    right: 10px;
    opacity: .8;
    cursor: pointer;
}
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div style="display:none;" class="CloseBtn" onclick="CloseSearchBtn()"></div>
        	<div class="" style="height: auto;padding: 5px;background: #fff; width: 100%; box-sizing: border-box;">
            	<ul>
                	<!--<li class="LC1">线路名称：</li>-->
                	<li class="LC2" style="width:100%;" >
                		<input id="buslineName" type="text" class="inp1" placeHolder="请输入线路名称" style="display: inline-block;width: 170px;height: 26px;" />
                		<input type="button" value="查询" class="NorBtn" style="display: inline-block;width: 50px;padding-left: 0;" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" />
                		<!--<div title="关闭" class="t-clo down"></div>-->
                	</li>
                </ul>
            </div>
        </div>
        <!--
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        -->
        <div class="ListShow content" id="content">
        	
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
var isExpire = "false";
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

function setNull2Blank(v){
	if(!v) return '暂无';
	
	if(v == 'null')  return '暂无';
	
	if(v == 'undefined')  return '暂无';
	
	return v;
}

function toggleContent(){
	$(".t-clo").click(function(){
    	var self = $(this);
    	if(self.hasClass("up")){
    		parent.closeOrOpenList('open');
    		self.removeClass("up");
    		self.addClass("down");
    		self.attr("title","关闭");
    	}else{//关闭
    		parent.closeOrOpenList('close');
    		self.removeClass("down");
    		self.addClass("up");
    		self.attr("title","打开");
    	}
    });
}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    toggleContent();
	});
	var results="";//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results="";
		var gridId = $('#gridId').val();
		var type = $('#type').val();
		if(type==""){
		  type=null;
		}
		var carNo = $('#carNo').val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&carNo='+carNo;
		if(type!=null && type!=""){
			postData+=('&type='+type);
		}
		var buslineName = $.trim($('#buslineName').val());
		if(buslineName != ''){
			postData+=('&buslineName='+buslineName);
		}
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/bus/pageData.json?t='+Math.random(),
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
					  
					  tableBody+='<dl onClick="selected(\''+val.buslineId+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b class="FontDarkBlue" >'+(val.buslineName==null?'':val.buslineName)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title="">上行时间:'+setNull2Blank(val.stimeUp)+'~'+setNull2Blank(val.etimeUp)+'</dd>';
					  tableBody+='<dd title="">下行时间:'+setNull2Blank(val.stimeDown)+'~'+setNull2Blank(val.etimeDown)+'</dd>';
					  tableBody+='<div class="c-detail" title="线路信息" onclick="showDetailInfo()" data-buslineId="'+val.buslineId+'"></div>';
					  tableBody+='</dl>';
					  results=results+","+val.carId;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				//$(".AdvanceSearch").css("display","none");
				//gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		//CloseSearchBtn();
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
	
	function selected(id){
	  	var url = '${rc.getContextPath()}/zhsq/map/bus/buslineDetailData.jhtml?buslineId='+id;
		$.getJSON(url,function(res){
			parent.oaMap.removeAll();
			if(res.carList && res.carList.length>0){
				var map = parent.getNewMap();
				var carList = res.carList;
				for(var z=0,zlen=res.carList.length;z<zlen;z++){
					map.put(res.carList[z].devId,res.carList[z].carNo);
				}
				parent.carMap = map;
			}else{
				parent.layer.msg('所选择线路暂无车辆.');
				parent.carMap = parent.getNewMap();
			}
			if(res.upStation && res.upStation.length>0){//上行线路
				var upPointArr = [];
				for(var i=0,len=res.upStation.length;i<len;i++){
					upPointArr[i] = parent.oaMap.getPoint(parent.toFixNum(res.upStation[i].lng),parent.toFixNum(res.upStation[i].lat));
					upPointArr[i].stationName = res.upStation[i].stationName||'';
				}
				parent.oaMap.batchGPS2BD('up',upPointArr,function(data){
					if(data && data.length>0){
						parent.busService(data);
						//parent.oaMap.drawLine(data);
						var path = '${rc.getContextPath()}/images/icon_03.png';
						var icon = parent.oaMap.getIcon(path,15,15);
						for(var x=0;x<data.length;x++){
							var infoWindow = parent.oaMap.getInfoWindow('上行站点('+res.buslineInfo.buslineName+')',(x+1)+":"+upPointArr[x].stationName,100,20);
							var marker = parent.oaMap.getMarker(data[x],icon);
							parent.oaMap.addMarker(marker);
							//marker.setAnimation(parent.BMAP_ANIMATION_BOUNCE);
							marker.mark = upPointArr[x].stationName;
							marker.addEventListener('mouseover',function(){
								var sm = this;
								var pix = parent.map.pointToPixel(sm.getPosition());
								parent.layer.msg(sm.mark,{offset:[pix.y+'px', pix.x+'px'],time:1000});
							});
							parent.oaMap.addClickHander_marker_showInfoWindow(infoWindow,marker);
						}
						
						var v = parent.oaMap.getMap().getViewport(data);//通过viewport设置中心点
						parent.oaMap.getMap().centerAndZoom(v.center,v.zoom);
						//parent.oaMap.centerAndZoom(data[0],16);
					}
				},true);
			}
			
			if(res.downStation && res.downStation.length>0){//下行线路
				var downPointArr = [];
				for(var j=0,len=res.downStation.length;j<len;j++){
					downPointArr[j] = parent.oaMap.getPoint(parent.toFixNum(res.downStation[j].lng),parent.toFixNum(res.downStation[j].lat));
					downPointArr[j].stationName = res.downStation[j].stationName||'';
				}
				parent.oaMap.batchGPS2BD('down',downPointArr,function(data){
					if(data && data.length>0){
						parent.busService(data,{strokeColor:'#1d9ed7',strokeOpacity:1});
						//parent.oaMap.drawLine(data,{strokeColor:'red'});
						var path = '${rc.getContextPath()}/images/icon_03.png';
						var icon = parent.oaMap.getIcon(path,15,15);
						for(var y=0;y<data.length;y++){
							var infoWindow = parent.oaMap.getInfoWindow('下行站点('+res.buslineInfo.buslineName+')',(y+1)+":"+downPointArr[y].stationName,100,20);
							var marker = parent.oaMap.getMarker(data[y],icon);
							parent.oaMap.addMarker(marker);
							marker.mark = downPointArr[y].stationName;
							marker.addEventListener('mouseover',function(){
								var sm = this;
								var pix = parent.map.pointToPixel(sm.getPosition());
								parent.layer.msg(sm.mark,{offset:[pix.y+'px', pix.x+'px'],time:1000});
							});
							//marker.setAnimation(parent.BMAP_ANIMATION_BOUNCE);
							parent.oaMap.addClickHander_marker_showInfoWindow(infoWindow,marker);
						}
						//parent.oaMap.centerAndZoom(data[0],16);
					}
				},true);
			}
			
		});
	}

	//--定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 490;
			opt.h = 270;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfCases.jhtml?ids="+res;
			return parent.MMApi.markerIcons(opt);
		}
		
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res==""){
			return ;
		}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfCases.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),490,270);
		}else {
			var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSocietyController/getArcgisLocateDataListOfCases.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfCases('"+gisDataUrl+"')";
			window.parent.getArcgisDataOfCases(gisDataUrl);
		}
	}
	
	//详情信息
	function showDetailInfo(e){
		var e = e||window.event;
		e.stopPropagation();
		var self = $(e.target);
		var buslineId = self.attr("data-buslineId");
		parent.showDetailInfo(buslineId);
	}
</script>
</body>
</html>