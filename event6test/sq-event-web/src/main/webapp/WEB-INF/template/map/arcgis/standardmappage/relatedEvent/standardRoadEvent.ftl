<!DOCTYPE html>
<html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>校园周边列表</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style type="text/css">
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="orgCode" value="${infoOrgCode!''}" />
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="mapt" value="${mapt?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
	
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="MetterCondition">
            	<ul>
                	<li>
                		<input name="keyWord" id="keyWord" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='输入事件名称、地段、地点进行查询';" onfocus="if(this.value=='输入事件名称、地段、地点进行查询')value='';" value="输入事件名称、地段、地点进行查询" style="width:240px;" />
                	</li>
                </ul>
                <ul class="time">
                	<li><input name="startTime" id="startTime" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='起始发生时间')value='';WdatePicker({isShowWeek:true});" value="起始发生时间" style="width:110px;" readonly="readonly" /></li>
                    <li style="width:20px;">-</li>
                	<li><input name="endTime" id="endTime" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='结束发生时间')value='';WdatePicker({isShowWeek:true});" value="结束发生时间" style="width:110px;" readonly="readonly" /></li>
                </ul>
                <div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></div>
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
if(parseInt(inputNum)>parseInt(pageCount)){
	inputNum=pageCount;
}
if(inputNum<=0||inputNum==""){
	inputNum=1;
}
change('4');
}
$('#name').keydown(function(e){ 
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
		var gridId = $('#gridId',window.parent.document).val();
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = {
			page: pageNo,
			rows: pageSize,
			infoOrgCode: $("#orgCode").val(),
			mapt: $("#mapt").val(),
			bizType: '1',
			gridId: gridId
		};
		var keyWord = $('#keyWord').val();
		if (keyWord == "输入事件名称、地段、地点进行查询") keyWord = "";
		var startTime = $('#startTime').val();
		if (startTime == "起始发生时间") startTime = "";
		var endTime = $('#endTime').val();
		if (endTime == "结束发生时间") endTime = "";
		if (keyWord != "") postData.keyWord = keyWord;
		if (startTime != "") postData.startTime = startTime;
		if (endTime != "") postData.endTime = endTime;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/relatedEventListData.json?t='+Math.random(),
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
					  
					  tableBody+='<dl onclick="selected(\''+val.RE_ID+'\',\''+(val.RE_NAME==null?'':val.RE_NAME)+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<b class="FontDarkBlue">'+(val.RE_NAME==null?'':val.RE_NAME)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd style="overflow:hidden;height:20px;">地段：'+(val.LOT_NAME==null?'':val.LOT_NAME)+'</dd>';
					  tableBody+='<dd style="overflow:hidden;height:20px;">时间：'+(val.OCCU_DATE==null?'':val.OCCU_DATE)+'</dd>';
					  tableBody+='</dl>';
					}
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				window.parent.spillGisMarkerOfZhuanTi(list, $('#elementsCollectionStr').val(), 350, 280);
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
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),350,280,id)
			}else {
				window.parent.localtionCampusPoint('${markType!''}',id);
			}
		},1000);
	}
</script>
</body>
</html>