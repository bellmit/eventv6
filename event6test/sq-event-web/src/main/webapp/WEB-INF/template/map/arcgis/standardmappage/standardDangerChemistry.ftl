<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>危险化学品企业列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="${elementsCollectionStr!}" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">企业名称：</li>
                	<li class="LC2"><input id="companyName" name="companyName" type="text" class="inp1" /></li>
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
        <div class="ListShow content" style="" id="content"></div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
	
	$('#companyName').keydown(function(e){ 
		if(e.keyCode==13){ 
			loadMessage(1,$("#pageSize").val());
		} 
	}); 
	
	$(document).ready(function(){
		var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val()); 
	});
	
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	
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
		var companyName = $('#companyName').val();
		var pageSize = $("#pageSize").val();
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
		var postData = 'page='+pageNo+'&rows='+pageSize+'&infoOrgId='+gridId+'&companyName='+companyName;
		
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/listDangerChemistryData.jhtml?t='+Math.random(),  
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
				var tableBody = '<div class="liebiao">';
				
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
						  var val=list[i];
						  var address = val.address;
						  var companyName = val.companyName;
						  
						  if(!address) {
						  	address = "&nbsp;";//为了排版整齐
						  } else if(address.length>15){
						  	address = address.substring(0,15)+"...";
						  }
						  
						  if(!companyName) {
						  	companyName = "";
						  } else if(companyName.length>12){
						  	companyName = companyName.substring(0,12)+"...";
						  }
						  
						  tableBody+='<dl onclick="selected(\''+val.dcmId+'\')">';
							  tableBody+='<dt><b class="FontDarkBlue">'+companyName+'</b>';
							  tableBody+='<dd><span title="'+ (val.address==null?'':val.address) +'">'+address+'</span>';
							  tableBody+='</dd></dt></dl>';
							  results=results+","+val.dcmId;
					}
					
					results=results.substring(1);
				} else {
					tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(list);
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
	//--定位
	function gisPosition(rowsObj) {
		if ($('#elementsCollectionStr').val() != "") {
			window.parent.markerArcgisDataOfZhuanTi($('#elementsCollectionStr').val(), rowsObj, "2204", {
				fieldId : "dcmId",
				fieldName : "companyName"
			});
		}
	}
	function selected(id) {
		if ($('#elementsCollectionStr').val() != "") {
			window.parent.clickMarkerById($('#elementsCollectionStr').val(), id);
		}
	}
	
</script>
</body>
</html>