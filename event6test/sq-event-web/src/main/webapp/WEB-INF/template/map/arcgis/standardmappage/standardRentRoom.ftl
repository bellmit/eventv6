<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租屋列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style>
.showRecords ul li {
    padding-left: 0px;
    width: 120;
    text-indent:0em;
    text-align:left;
}
.showRecords{
	line-height:14px;
}
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div id="searchDiv" class="MetterCondition" style="max-height: 500px;overflow-x: hidden">
            	<ul>
                	<li class="LC1">是否到期：</li>
                	<li class="LC2">
                		<select name="type" id="type" class="sel1">
                            <option value="">全部</option>
                			<option value="1">正常</option>
                			<option value="2">到期</option>
                		</select>
                	</li>
                </ul>
                <ul id="content-md1" class="type content light">
                	<p>住户类型：</p>
                	<li onclick="selectOnly(this)" class="current" style="width:60px;" value="">全部</li>
            		<#if liveTypeDC??>
						<#list liveTypeDC as l>
							<li onclick="selectOnly(this)" id="${l.dictId}" value="#${l.dictGeneralCode}" style="width:60px;">${l.dictName}</li>
						</#list>
					</#if>
                    <div class="clear"></div>
                </ul>
            	<ul>
                	<li class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');"/></li>
                </ul>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="showRecords">
        	<ul>
        		<li>共<span id="records">0</span>户，其中：</br>放心户<span id="fxcount">0</span>，一般户<span id="ybcount">0</span>，重点管控<span id="zdgkcount">0</span></li>
        	</ul>
        </div>
        <div class="ListShow content" style="" id="content">
        	
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
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    
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
		var type = $("#type").val();
		var liveType = "";
		$('#content-md1').find('li').each(function() {
			if ($(this).attr('class') == 'current') {
				liveType = $(this).attr('value');
				liveType = liveType.substring(1, liveType.length);
			}
		});
		if(type==2){
			isExpire = "true";
		}else if (type==1) {
			isExpire = "false";
		}else {
            isExpire = "";
		}
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&isExpire='+isExpire+'&liveType='+liveType;
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/rentRoomListData.json?t='+Math.random(),
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
					  
					  var roomName = val.roomName;
					  
					  tableBody+='<dl onclick="selected(\''+val.rentId+'\',\''+val.roomId+'\',\''+val.buildingId+'\',\''+(val.roomName==null?'':val.roomName)+'\')">';
					  tableBody+='<dt>';
					  if (val.liveType == '001') {
					  	tableBody+='<span style="padding-right: 5px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/liveType_1.png" width="12px" height="12px"></span>';
					  }
					  if (val.liveType == '002') {
					  	tableBody+='<span style="padding-right: 5px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/liveType_2.png" width="12px" height="12px"></span>';
					  }
					  if (val.liveType == '004') {
					  	tableBody+='<span style="padding-right: 5px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/liveType_3.png" width="12px" height="12px"></span>';
					  }
					  tableBody+='<span class="fr">'+'</span>';
					  tableBody+='<b class="FontDarkBlue">'+
							(val.mobileTelephone==null?'':
									('<span class="fr">' + '<img src="${uiDomain!''}/images/map/gisv0/special_config/images/people_06.png">'+val.mobileTelephone))+'</span>'+(val.ownerName==null?'':val.ownerName) + '</b>';

					  tableBody+='</dt>';
                      tableBody+='<dd title=\''+(val.roomName==null?'':val.roomName)+'\'>'+(roomName)+'</dd>';
					  tableBody+='</dl>';
					  results=results+","+val.rentId;
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
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/roomRentCount.json?t='+Math.random(), 
			data: postData,
			dataType:"json",
			success: function(data){
				$('#fxcount').text(data.fxcount);//放心户
				$('#ybcount').text(data.ybcount);//一般户
				$('#zdgkcount').text(data.zdgkcount);//重点管控
				
			},
			error:function(data){
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
	function selected(rentId, roomId, buildingId, roomName) {
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 400;
			opt.h = 235;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(rentId, opt);
		}
		
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400,235,rentId)
			}else {
				window.parent.localtionRentRoomPoint(rentId);
			}
		},1000);
	}
	//--定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 400;
			opt.h = 235;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
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
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400,235);
		}else {
			var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfRentRoom('"+gisDataUrl+"')";
			window.parent.getArcgisDataOfRentRoom(gisDataUrl);
		}
	}
	function selectOnly(obj) {
		if ($(obj).attr('class') != 'current') {
			$(obj).parent().children().removeClass("current");
			$(obj).addClass("current");
		}
	}
</script>
</body>
</html>