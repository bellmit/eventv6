<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>联防长列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;">
	<input type="hidden" id="infoOrgCode" value="${orgCode!''}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />

	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	
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
function isNull(v){
		return v?v:'';
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
		var elementsCollectionStr = $('#elementsCollectionStr').val(),obj;
		if ( elementsCollectionStr!= "") {
			obj = window.parent.analysisOfElementsCollectionList(elementsCollectionStr);
			layerName = obj["menuLayerName"];
			window.parent.currentLayerName = layerName;
		}
	
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results=[];
		var infoOrgCode = $('#infoOrgCode').val();
		var order = $("#order option:selected").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData =  'page='+pageNo+'&rows='+pageSize+'&orgCode='+infoOrgCode+'&mapt=${mapt}';
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/jointDefence/findTenPerson.json',  
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo > 1) {
					data.total = parseInt($('#records').text());
				}
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
					  var imageGenderSpan="";
					  tableBody+='<dl onclick="selected(\''+val.ORG_CODE+'\',\''+val.I_IDENTITY_CARD+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b class="FontDarkBlue">'+isNull(val.PARTY_NAME)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd>'+isNull(val.RESIDENT_MOBILE)+'</dd>';
					  tableBody+='</dl>';
					  if(val.CEN_LAT != undefined  && val.CEN_LON != undefined )
						results.push({wid:(val.ORG_CODE+'&idCard='+val.I_IDENTITY_CARD),x:val.CEN_LON,y:val.CEN_LAT,gridName:val.PARTY_NAME,name:val.PARTY_NAME,elementsCollectionStr:'menuSummaryUrl_,_'+obj.menuSummaryUrl});
					
					}
				} else {
					tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
                gisPosition(results, "");
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
		        pagenum = inputNum
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
	
	var ss;
	
	function selected(orgCode, idCard){
        var elementsCollectionStr =  $('#elementsCollectionStr').val();
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 400;
			opt.h = 236;
			opt.ecs =elementsCollectionStr;
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		setTimeout(function() {
				obj = window.parent.analysisOfElementsCollectionList(elementsCollectionStr);
				window.parent.getDetailOnMapOfListClick(elementsCollectionStr,obj['menuSummaryWidth'],obj['menuSummaryHeight'],orgCode+'&idCard='+idCard);
		},1000);
	}
	//--定位
	function gisPosition(res, controlDesc) {
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				//return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		
		if(res==undefined|| res.length==0 ){
			return;
		}
        var elementsCollectionStr = $('#elementsCollectionStr').val();
		if(elementsCollectionStr != "") {
			var obj = window.parent.analysisOfElementsCollectionList(elementsCollectionStr);
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/findTenTeamInfo.jhtml";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+elementsCollectionStr+"')";
			window.parent.clearSimilarLayer(obj.menuLayerName);
			window.parent.getArcgisDataOfZhuanTi(url,elementsCollectionStr,400,236,undefined,undefined,{},false,res);
		}else {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/getArcgisLocateDataListOfPeople.jhtml?userIds="+res+"&type=${type}&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfPeople('"+url+"','${type}')";
			window.parent.getArcgisDataOfPeople(url,'${type}');
		}
	}
</script>
</body>
</html>