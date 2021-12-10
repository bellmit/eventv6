<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>重大产业</title>

<#include "/component/commonFiles.ftl" />

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="100" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">产业名称：</li>
                	<li class="LC2">
                		<input id="industryName"  name="industryName" type="text" class="inp1"/>
                	</li>
                </ul>
                <ul>
                	<li class="LC1">产业状态：</li>
                	<li class="LC2">
                		<input id="industryType"  name="industryType"  type="hidden"/>
                		<input id="industryStatus" style="width:184px;" name="industryStatus" type="text" class="sel1"/>
                	</li>
                </ul>
                <ul>
                	<li class="LC1">年度：</li>
                	<li class="LC2">
  	                    	<input class="inp1 inp2 InpDisable  Wdate timeClass easyui-validatebox" type="text" id="yearStr" value=""
                                   style="height:25px;" name="yearStr"   onClick="WdatePicker({isShowClear:false,maxDate:'%y',dateFmt:'yyyy'})" readonly="true"/>
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></li>
                </ul>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
          <div class="tushi2">
          <img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/industry_begin.png" height="15" width="15"/>已开工
          <img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/insdustry_year.png" height="15" width="15"/>年内开工
          <img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/industry_store.png" height="15" width="15"/>储备项目
        </div>
        
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow content"  id="content"   >

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
	    AnoleApi.initListComboBox("industryStatus", "industryType", "B868");
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
		var industryName = $('#industryName').val();
		var industryStatus = $('#industryStatus').val();
		var yearStr=$('#yearStr').val();
		if(industryStatus=="==全部==")
		{
			industryStatus ="";
		}
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&industryName='+industryName+'&industryStatus='+industryStatus+'&yearStr='+yearStr+'';
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/majorIndustryListData.json?t='+Math.random(),
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
					  
					  var content = val.content;
					  if(content!=null && content!="" && content.length>15){
						content = content.substring(0,15)+"...";
					  }					  
					  var addr=(val.address==null?'':val.address);
					  var industryStatus=(val.industryStatus==null?'':val.industryStatus);
					  tableBody+='<dl onClick="selected(\''+val.industryId+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b  class="FontDarkBlue" >'+(val.industryName==null?'':val.industryName)+'<span style="position:relative;left: 10px;">'+'('+industryStatus+')'+'</span>'+'</b>';
					  tableBody+='</dl>';
					  results=results+","+val.industryId;
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
	
	
	function selected(id) {
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 490;
			opt.h = 270;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {		
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),490,270,id)
			}else {
				window.parent.localtionCasesPoint(id);
			}
		},1000);
	}
	
	//--定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 490;
			opt.h = 270;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfMajorIndustry.jhtml?ids="+res;
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
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfMajorIndustry.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),490,270);
		}else {
			var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfMajorIndustry.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfCases('"+gisDataUrl+"')";
			window.parent.getArcgisDataOfDevice(gisDataUrl);
		}
	}
	
	

</script>
<#include "/component/ComboBox.ftl" />
</body>
</html>