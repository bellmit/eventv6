<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安监企业列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ComboBox.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
	                <!--<li>所属网格：</li>
	            	<li><input type="hidden" id="gridCode" name="gridCode" value="<#if startGridCode??>${startGridCode}</#if>"><input type="hidden" id="gridId" name="gridId" value="<#if startGridId??>${startGridId}</#if>"><input name="gridName" id="gridName" type="text" class="inp1 InpDisable" value="<#if startGridName??>${startGridName}</#if>" style="width:150px;"/></li>
	        		 -->
	        		<li>场所类型：</li>
	                <li>
	                    <input type="hidden" name="placeCata" id="placeCata"  />
						<input class="inp1" type="text" name="placeCataName" id="placeCataName"  style="width:175px;"/>
	                </li>
                </ul>
                <ul>
	        		<li>模糊查询：</li>
	                <li><input name="keyWord" type="text" class="inp1" id="keyWord" value="场所名称/家庭地址" style="color:gray; width:150px;" onfocus="if(this.value=='场所名称/家庭地址'){this.value='';}$(this).attr('style','width:150px;');" onblur="if(this.value==''){$(this).attr('style','color:gray;width:150px;');this.value='场所名称/家庭地址';}" onkeydown="_onkeydown();" /></li>
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
<#--场所类型下拉框初始化-->
	    AnoleApi.initListComboBox("placeCataName", "placeCata", "${placeCataDDCode}", null, null, {
		  ShowOptions : {
			EnableToolbar : true
		  }
	    });
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
$('#corName').keydown(function(e){ 
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
		var pageSize = $("#pageSize").val();
		var placeCata = $("#placeCata").val();
		var keyWord = $("#keyWord").val();
		if(placeCata == undefined) {
			placeCata = "";
		}
		if(keyWord == undefined || keyWord=='场所名称/家庭地址') {
			keyWord = "";
		}
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&placeCata='+placeCata+'&keyWord='+keyWord;
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/zzSmallPlaceListData.json?t='+Math.random(),  
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
					  
					  var placeName = val.placeName;
					  if(placeName!=null && placeName!="" && placeName.length>12){
						placeName = placeName.substring(0,12)+"...";
					  }else if(placeName==null) {
					  	placeName = "";
					  }else{
						placeName = placeName;
					  }
					  
					  var placeAddr = val.placeAddr;
					  if(placeAddr!=null && placeAddr!="" && placeAddr.length>15){
						placeAddr = placeAddr.substring(0,15)+"...";
					  }else if(placeAddr==null ){
					  	placeAddr = "";
					  }else{
						placeAddr = placeAddr;
					  }
					  
					  tableBody+='<dl onclick="selected(\''+val.placeId+'\',\''+(val.placeName==null?'':val.placeName)+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">'
					 // +(val.representativeName==null?'':val.representativeName)
					 // +'&nbsp;'+(val.telephone==null?'':('<span class="PeoplePhone" width="20px" height="20px" style="padding:0 0 0 15px;background-position-y:0px;"></span>'+val.telephone))
					  +'</span>';
					  tableBody+='<b class="FontDarkBlue" title=\''+(val.placeName==null?'':val.placeName)+'\'>'+placeName+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title=\''+(val.placeAddr==null?'':val.placeAddr)+'\'>'+'地址：'+placeAddr+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.placeId;
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
		setTimeout(function() {
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),340,245,id)
		},1000);
		
	}
	//--定位
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
			return ;
		}
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisZzSmallPlaceLocateDataList.jhtml?ids="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
		window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),340,245);
	}
</script>
</body>
</html>