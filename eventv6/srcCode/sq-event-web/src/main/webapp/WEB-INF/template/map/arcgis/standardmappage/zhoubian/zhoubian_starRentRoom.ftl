<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>周边资源列表（出租屋）</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
.liebiao p{float:left;}
</style>
</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="orgCode" value="${orgCode!''}" />
	<input type="hidden" id="operateStatus" value="${operateStatus!''}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
				<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
            	<ul>
                	<li class="LC1">地址：</li>
                	<li class="LC2"><input id="address" name="address" type="text" class="inp1" /></li>
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
	function pageSubmit() {
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if (isNaN(inputNum)) {
			inputNum = 1;
		}
		if (parseInt(inputNum) > parseInt(pageCount)) {
			inputNum = pageCount;
		}
		if (inputNum <= 0 || inputNum == "") {
			inputNum = 1;
		}
		change('4');
	}
	
	function ShowOrCloseSearchBtn() {
		var temp = $(".ListSearch").is(":hidden");// 是否隐藏
		if (temp == false) {
			$(".ListSearch").hide();
		} else {
			$(".ListSearch").show();
		}
	}
	
	$('#name').keydown(function(e) {
		if (e.keyCode == 13) {
			loadMessage(1, $("#pageSize").val());
		}
	});
	
	function CloseSearchBtn() {
		$(".ListSearch").hide();
	}
	
	$(document).ready(function() {
		var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
		$("#content").height(winHeight - 56);
		loadMessage(1, $("#pageSize").val());
	});
	
	var results = "";// 获取定位对象集合
	var layerName="";
	function loadMessage(pageNo, pageSize,searchType) {
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results = "";
		$.blockUI({
			message : "加载中...",
			css : {
				width : '150px',
				height : '50px',
				lineHeight : '50px',
				top : '40%',
				left : '20%',
				background : 'url(${rc.getContextPath()}/css/loading.gif) no-repeat',
				textIndent : '20px'
			},
			overlayCSS : {
				backgroundColor : '#fff'
			}
		});
		$.ajax({
			type : "POST",
			url : '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t=' + Math.random(),
			data : {
				pageNo : pageNo,
				pageSize : $("#pageSize").val(),
				name : $('#name').val(),
				address : $('#address').val(),
				mapType : "${mapType}",
				distance : "${distance}",
				x : "${x}",
				y : "${y}",
				infoOrgCode : "${infoOrgCode}",
				zhoubianType : "${zhoubianType}"
			},
			dataType : "json",
			success : function(data) {
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
					tableBody+='<ul>';
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  console.info(val);
					  console.info(val.RENT_ID);
					    if((i+1)%2==0){//基数
					       tableBody+='<li onclick="selected(\''+val.RENT_ID+'\',\''+val.ROOM_ID+'\',\''+val.BUILDING_ID+'\',\''+(val.ROOM_NAME==null?'':val.ROOM_NAME)+'\')" class="bg">';
					       
					       //星级出租屋，星级判断
					       if(val.STATS_==1){
					       	tableBody+='<p class="OneStar"></p>';
					       }else if(val.STATS_==2){
					       	tableBody+='<p class="TwoStar"></p>';
					       }else if(val.STATS_==3){
					       	tableBody+='<p class="ThreeStar"></p>';
					       }else if(val.STATS_==4){
					       	tableBody+='<p class="FourStar"></p>';
					       }else if(val.STATS_==5){
					       	tableBody+='<p class="FiveStar"></p>';
					       }else if(val.STATS_==0 || val.STATS_==null){
					       	tableBody+='<p class="NoStar"></p>';
					       }
					       //tableBody+='<p class="OneStar"></p>';
					       if(val.isExpires=="2"){
					       		 tableBody+='<p><img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" /><b>'+(val.ROOM_ADDRESS==null?'':val.ROOM_ADDRESS)+'</b></p>';
					       }else{
					       		tableBody+='<p style="width:170px;"><b>'+(val.ROOM_ADDRESS==null?'':val.ROOM_ADDRESS)+'</b></p>';
					       }
					       
						   tableBody+='<br><p style="width:170px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/fangdong.png" />'+(val.OWNER_NAME==null?'':val.OWNER_NAME)+'&nbsp;&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />'+(val.MOBILE_TELEPHONE==null?'':val.MOBILE_TELEPHONE)+'</p>';
						   //tableBody+='<p>网格：'+(val.gridName==null?'':val.gridName)+'</p>';
						   
					       //tableBody+='<p>到期时间：'+(val.hireEndStr==null?'':val.hireEndStr)+'</p>';
					       tableBody+='<div class="clear"></div>';
					       tableBody+='</li>';
					       
					    }else{
					       //tableBody+='<li onclick="selected(\''+val.RENT_ID+'\',\''+val.ROOM_ID+'\',\''+val.BUILDING_ID+'\',\''+(val.ROOM_NAME==null?'':val.ROOM_NAME)+'\')">';
					         tableBody+='<li onclick="selected(\''+val.RENT_ID+'\',\''+val.ROOM_ID+'\',\''+val.BUILDING_ID+'\',\''+(val.ROOM_NAME==null?'':val.ROOM_NAME)+'\')" >';
					       
					       //星级出租屋，星级判断
					       if(val.STATS_==1){
					       	tableBody+='<p class="OneStar"></p>';
					       }else if(val.STATS_==2){
					       	tableBody+='<p class="TwoStar"></p>';
					       }else if(val.STATS_==3){
					       	tableBody+='<p class="ThreeStar"></p>';
					       }else if(val.STATS_==4){
					       	tableBody+='<p class="FourStar"></p>';
					       }else if(val.STATS_==5){
					       	tableBody+='<p class="FiveStar"></p>';
					       }else if(val.STATS_==0 || val.STATS_==null){
					       	tableBody+='<p class="NoStar"></p>';
					       }
					       if(val.isExpires=="2"){
					       		tableBody+='<p><img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" /><b>'+(val.ROOM_ADDRESS==null?'':val.ROOM_ADDRESS)+'</b></p>';
					       }else{
					       		tableBody+='<p style="width:170px;"><b>'+(val.ROOM_ADDRESS==null?'':val.ROOM_ADDRESS)+'</b></p>';
					       }
					       
						   tableBody+='<br><p style="width:170px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/fangdong.png" />'+(val.OWNER_NAME==null?'':val.OWNER_NAME)+'&nbsp;&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />'+(val.MOBILE_TELEPHONE==null?'':val.MOBILE_TELEPHONE)+'</p>';
						   //tableBody+='<p>网格：'+(val.gridName==null?'':val.gridName)+'</p>';
						   
					       //tableBody+='<p>到期时间：'+(val.hireEndStr==null?'':val.hireEndStr)+'</p>';
					       tableBody+='<div class="clear"></div>';
					       tableBody+='</li>';
					      
					    }
						results=results+","+val.RENT_ID;
					}
			        tableBody+='</ul>';
					results=results.substring(1, results.length);
				} else {
					tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error : function(data) {
				$.unblockUI();
				var tableBody = '<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		CloseSearchBtn();
	}
	var currentPageNum=1;
	// 分页
	function change(_index) {
		var flag;
		var pagenum = $("#pagination-num").text();
		var lastnum = $("#pageCount").text();
		var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
		case '1': // 上页
			if (pagenum == 1) {
				flag = 1;
				break;
			}
			pagenum = parseInt(pagenum) - 1;
			pagenum = pagenum < firstnum ? firstnum : pagenum;
			break;
		case '2': // 下页
			if (pagenum == lastnum) {
				flag = 2;
				break;
			}
			pagenum = parseInt(pagenum) + 1;
			pagenum = pagenum > lastnum ? lastnum : pagenum;
			break;
		case '3':
			flag = 3;
			pagenum = 1;
			break;
		case '4':
			pagenum = inputNum;
			if (pagenum == lastnum) {
				flag = 4;
				break;
			}
			pagenum = parseInt(pagenum);
			pagenum = pagenum > lastnum ? lastnum : pagenum;
			break;
		default:
			break;
		}
	
		if (flag == 1) {
			alert("当前已经是首页");
			return;
		} else if (flag == 2) {
			alert("当前已经是尾页");
			return;
		}
		currentPageNum = pagenum;
		loadMessage(pagenum, pageSize);
	}
	
	$("#moreSearch").toggle(function() {
		$(".AdvanceSearch").css("display", "block");
	}, function() {
		$(".AdvanceSearch").css("display", "none");
	});
	
	function selected(id) {
		setTimeout(function() {
			if ($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400,235,id)
			}
		}, 1000);
	}
	// 地图定位
	function gisPosition(res) {
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		if (res == "") {
			//return;
		}
		//window.parent.clearMyLayer();
		
		if ($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400,235);
		}
	}
	</script>
</body>
</html>