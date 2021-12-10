<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新社会组织列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">所在网格：</li>
                	<li class="LC2"><input id="gridName" name="gridName" type="text" class="inp1" />
                	<input type="hidden" id="gridId" value="${gridId?c}" /></li>
                </ul>
                <ul>
                	<li class="LC1">组织类别：</li>
                	<li class="LC2"><input id="typeName" name="typeName" type="text" class="inp1 InpDisable" />
                	<input type="hidden" name="type" value="" id="type"/></li>
                </ul>
            	<ul>
                	<li class="LC1">组织名称：</li>
                	<li class="LC2"><input id="orgName" name="orgName" type="text" class="inp1 InpDisable" /></li>
                </ul>
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());"/></li>
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
if(parseInt(inputNum)>parseInt(pageCount)){
	inputNum=pageCount;
}
if(inputNum<=0||inputNum==""){
	inputNum=1;
}
change('4');
}
$('#name,#gridname').keydown(function(e){ 
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
	    AnoleApi.initGridZtreeComboBox("gridName", "gridId");
		AnoleApi.initListComboBox("typeName", "type", "B032");
	});
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		results="";
		var gridId = $('#gridId').val();
		var orgName = $('#orgName').val();
		var type = $('#type').val();
		var gridName = $('#gridName').val();
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&orgName='+orgName+'&type='+type;
		//alert(postData);
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofnewgroup/newSocialOrgListData.json?t='+Math.random(),
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
					  
					  var orgName = val.orgName;
					  if(orgName!=null && orgName!="" && orgName.length>15){
						orgName = orgName.substring(0,15)+"...";
					  }
					  
					  tableBody+='<dl onclick="selected(\''+val.orgId+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">'+(val.typeName==null?'':val.typeName)+'</span>';
					  tableBody+='<b class="FontDarkBlue">'+orgName+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd>'+(val.gridName==null?'':val.gridName)+'</dd>';
					  tableBody+='</dl>';
					  
					  results=results+","+val.orgId;
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
	function selected(id){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 195;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),340,195,id)
			}else {
				window.parent.localtionNewSocialOrgOrgPoint(id);
			}
		},1000);
	}
	
	var layerName="";
	//地图定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 340;
			opt.h = 195;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofnewgroup/getArcgisLocateDataListOfNewSociety.jhtml?type='01B032'&ids="+res;
			return parent.MMApi.markerIcons(opt);
		}
		
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
			console.log("return");
				return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		
		if (res==""){
			return ;
		}
		
	  	if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofnewgroup/getArcgisLocateDataListOfNewSociety.jhtml?type='01B032'&ids="+res;
			window.parent.currentLayerLocateFunctionStr='getArcgisDataOfZhuanTi("'+url+'","'+$('#elementsCollectionStr').val()+'")';
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),340,195);
		}else {
			var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofnewgroup/getArcgisLocateDataListOfNewSociety.jhtml?type='01B032'&ids="+res;
			window.parent.currentLayerLocateFunctionStr='getArcgisDataOfNewSocialOrg("'+gisDataUrl+'")';
	  		window.parent.getArcgisDataOfNewSocialOrg(gisDataUrl);
		}
	}
</script>

<#include "/component/ComboBox.ftl" />
</body>
</html>