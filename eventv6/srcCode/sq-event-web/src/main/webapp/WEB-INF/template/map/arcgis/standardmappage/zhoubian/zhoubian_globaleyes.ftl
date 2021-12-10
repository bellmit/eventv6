<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>全球眼列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="platformName" name="platformName" type="text" class="inp1" /></li>
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
function pageSubmit(){
inputNum = $("#inputNum").val();
var pageCount = $("#pageCount").text();
if(isNaN(inputNum)){
	inputNum=1;
}
if(inputNum>pageCount){
	inputNum=pageCount;
}
if(inputNum<=0||inputNum==""){
	inputNum=1;
}
change('4');
}
$('#platformName').keydown(function(e){ 
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
});
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
	    //var winHeight = window.parent.document.getElementById('map' + window.parent.currentN).offsetHeight - 62;
	    var winHeight = 900;
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
		var platformName = $('#platformName').val();
		if(platformName=="==输入查询内容==") platformName="";
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'pageNo='+pageNo+'&pageSize='+pageSize+'&name='+platformName+'&x='+${x}+'&y='+${y}+'&distance='+${distance}+'&mapType='+${mapType};
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t='+Math.random(), 
			data : {
				pageNo : pageNo,
				pageSize : pageSize,
				name : platformName,
				mapType : "${mapType}",
				distance : "${distance}",
				x : "${x}",
				y : "${y}",
				infoOrgCode : "${infoOrgCode}",
				zhoubianType : "${zhoubianType}"
			},
			dataType:"json",
			success: function(data){
				$.unblockUI();
				// 设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo == 1) {
					$('#records').text(data.total);
				} else {
					data.total = parseInt($('#records').text());
					console.log("data.total:"+data.total);
				}
				var totalPage = Math.floor(data.total / pageSize);
				if (data.total % pageSize > 0)
					totalPage += 1;
				$('#pageCount').text(totalPage);
				var list = data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  var userTypeLabel = '';
					  if(val.useType!=null) {
						 if(val.useType=="1") userTypeLabel="社区";
						 else if(val.useType=="2") userTypeLabel="旅游";
						 else if(val.useType=="3") userTypeLabel="交通";
					  }		
					  tableBody+='<dl onclick="selected(\''+val.MONITOR_ID+'\',\''+(val.PLATFORM_NAME==null?'':val.PLATFORM_NAME)+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">'+'</span>';
					  tableBody+='<b class="FontDarkBlue">'+(val.PLATFORM_NAME==null?'':val.PLATFORM_NAME)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd>'+(val.CHANNEL_NAME==null?'':val.CHANNEL_NAME)+'</dd>';
					  tableBody+='</dl>';
					  results=results+","+val.MONITOR_ID;
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
	
	function selected(id, name){
		gisPosition(id);
		
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				//window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),475,406,id);
                window.parent.showGlobalEyes(name, $('#elementsCollectionStr').val(), id);
			}else {
				window.parent.locationGlobalEyesPoint(id);
			}
		},1000);
	}
	
	//地图定位
	function gisPosition(res){
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
			//return ;
		}
		
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),475,406);
		}else {
			var qqyurl= "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGlobalEyes('"+qqyurl+"')";
			window.parent.getArcgisDataOfGlobalEyes(qqyurl);
		}
	}
</script>
</body>
</html>