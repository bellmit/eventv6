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
	<input type="hidden" id="elementsCollectionStr" value="gdcId_,_41321,_,orgCode_,_3502,_,homePageType_,_ARCGIS_STANDARD_HOME,_,smallIco_,_/images/map/gisv0/map_config/unselected/situation_globalEyes.png,_,menuCode_,_globalEyes,_,menuName_,_全球眼,_,smallIcoSelected_,_/images/map/gisv0/map_config/unselected/situation_globalEyes.png,_,menuListUrl_,_/zhsq/map/arcgis/arcgisDataOfSituationController/globalEyes.jhtml,_,menuSummaryUrl_,_${SQ_ZZGRID_URL}/zzgl/map/data/situation/globalEyesPlay.jhtml?monitorId=,_,menuLayerName_,_globalEyesLayer,_,menuDetailUrl_,_null,_,menuDetailWidth_,_null,_,menuDetailHeight_,_null,_,menuSummaryWidth_,_null,_,menuSummaryHeight_,_null,_,callBack_,_showObjectList,_," />
    <div class="" style="display:block;">
        <div class="ListShow content" style="width:200px;" id="content">
        	
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
		var winHeight = 400;
        $("#content").height(winHeight);
        loadMessage();
	});
	var results="";//获取定位对象集合
	var layerName="";
	window.parent.layer.load(0);
	function loadMessage(opts) {
		var settings = {
			pageNo : 0,
			pageSize : 10000,
            mapType : "${mapType}",
            distance : "${distance}",
            x : "${x}",
            y : "${y}",
            infoOrgCode : "${infoOrgCode}",
			zhoubianType : 'zhouBianStatOfGlobalEyesService',
			suffix : '_list'
		};
		var params = $.extend({}, settings, opts);
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		window.parent.clearSpecialLayer(layerName);
		window.parent.currentListNumStr = "";
		results="";
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t='+Math.random(), 
			data : params,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				// 设置页面页数
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
					  tableBody+='<dl onclick="selected(\''+val.MONITOR_ID+'\',\''+(val.PLATFORM_NAME==null?'':val.PLATFORM_NAME)+'\',\''+(val.COMPANY_TYPE==null?'':val.COMPANY_TYPE)+'\',\''+(val.CHANNEL_NAME==null?'':val.CHANNEL_NAME)+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">'+'</span>';
					  tableBody+='<b class="FontDarkBlue">'+(val.PLATFORM_NAME==null?'':val.PLATFORM_NAME)+'</b>';
					  tableBody+='</dt>';
						var channelName = val.CHANNEL_NAME==null?'':val.CHANNEL_NAME;
						if(channelName.length > 10){
							channelName = channelName.substring(0,10);
						}
					  tableBody+='<dd>'+(channelName)+'<span class="fr">距离：'+val.DISTANCE+'米</span></dd>';
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
                var points= new Array();
                var num = 0;
				var desc_points = "";
                points = results.split(",");
                for (i=0;i<points.length ;i++ ){
                    desc_points = desc_points + "," + points[i];
					if(parseInt(i)>=100 && parseInt(i)%100 ==0){
                        desc_points = desc_points.substring(1,desc_points.length);
                        console.log(desc_points);
                        gisPosition(desc_points);
                        desc_points = "";
					}
				}
                desc_points = desc_points.substring(1,desc_points.length);
                gisPosition(desc_points);
                window.parent.layer.closeAll('loading');
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
	
	function selected(id, name, type, channelName){
		gisPosition(id);
		
		setTimeout(function() {
			if(type != 20){
				if($('#elementsCollectionStr').val() != "") {
					//window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),475,406,id);
                    window.parent.showGlobalEyes(name, $('#elementsCollectionStr').val(), id);
				}else {
					window.parent.locationGlobalEyesPoint(id);
				}
			}else{
                window.parent.showGlobalEye(channelName);
			}
		},1000);
	}
	
	//地图定位
	function gisPosition(res){
		//window.parent.clearSpecialLayer(layerName);
		if (res == "" && results == "") {
			return;
		} else if (typeof res == "undefined") {
			res = results;
		}
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGlobalEyes.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),475,406);
		}
	}
</script>
</body>
</html>