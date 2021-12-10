<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>社区微型消防站</title>
	<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
	<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
	
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>

<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
	<input type="hidden" id="elementsCollectionStr" value="${elementsCollectionStr!}" />
	<input type="hidden" id="microFirehouseType" value="${microFirehouseType!}" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">名称：</li>
                	<li class="LC2"><input id="name" name="name" type="text" class="inp1" /></li>
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
	(function($) {
		var _microFirehouseDataList = null;
		
		$(document).ready(function(){
		    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
	       	$("#content").height(winHeight-56); 
		    loadMessage(1,$("#pageSize").val());
		    
		    $('#name').keydown(function(e){ 
				if(e.keyCode==13){ 
					loadMessage(1,$("#pageSize").val());
				} 
			});
			
			//隐藏查询操作按钮
			$("#searchBtnId", parent.document).hide();
		});
		
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
		}
		function CloseSearchBtn(){
			$(".ListSearch").hide();
		}
		
		var layerName="";
		function loadMessage(pageNo,pageSize,searchType){
			layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
			window.parent.currentLayerName = layerName;
			if('searchBtn'==searchType) {
				window.parent.clearSpecialLayer(layerName);
				window.parent.currentListNumStr = "";
			}
			
			var gridId = $('#gridId').val();
			var name = $('#name').val();
			if(name=="==输入查询内容==") name="";
			var pageSize = $("#pageSize").val();
			$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
	    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
	    	var postData = 'page='+pageNo+'&rows='+pageSize + '&microFirehouseType=' + $("#microFirehouseType").val() + '&elementsCollectionStr=' + $("#elementsCollectionStr").val();
	    	
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/map/arcgis/microFirehouse/listMicroFirehouseData.json?t='+Math.random(),
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
					_microFirehouseDataList = data.rows;
					var list=data.rows;
					var tableBody="";
					tableBody+='<div class="liebiao">';
					if(list && list.length>0) {
						var locationDataArray = new Array();
						
						for(var i=0;i<list.length;i++){
						  var val = list[i],
						  	  fireTeamName = val.fireTeamName || "",
						  	  teamLeader = val.teamLeader || "",
						  	  tel = val.tel || "",
						  	  positionDesc = val.positionDesc || "",
						  	  locationData = val.locationData;
						  
						  tableBody+='<dl onclick="selected(\''+val.id+'\')">';
						  tableBody+='<dt>';
						  tableBody+='<span class="fr" ';
						  
						  if(teamLeader && teamLeader.length > 6) {
						  	tableBody += ' title = "'+ teamLeader +'">' + teamLeader.substring(0, 6);
						  } else {
						  	tableBody += '>'+ teamLeader;
						  }
						  
						  if(tel) {
						  	tableBody += '<img title="'+ tel +'" src="${uiDomain!''}/images/map/gisv0/special_config/images/people_06.png" />';
						  }
						  
						  tableBody += '</span>';
						  tableBody+='<b class="FontDarkBlue" ';
						  
						  if(fireTeamName && fireTeamName.length > 10) {
						  	tableBody += ' title = "'+ fireTeamName +'">' + fireTeamName.substring(0, 10);
						  } else {
						  	tableBody += '>'+ fireTeamName;
						  }
						  
						  tableBody+='</b></dt>';
						  tableBody+='<dd title="'+ positionDesc +'" ';
						  
						  if(positionDesc && positionDesc.length > 20) {
						  	tableBody += ' title = "'+ positionDesc +'">' + positionDesc.substring(0, 20);
						  } else {
						  	tableBody += '>'+ positionDesc;
						  }
						  
						  tableBody += '</dd>';
						  tableBody+='</dl>';
						  
						  if(locationData) {
						  	locationDataArray.push(locationData);
						  }
						}
						
						if(locationDataArray.length > 0) {
							gisPosition(locationDataArray);
						}
						
					} else {
						tableBody+='<ul>未查到相关数据！！</ul>';
					}
			        tableBody+='</div>';
					$("#content").html(tableBody);
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
		
		//记录点击事件
		selected = function(id, name){
			if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
				var opt = {};
				opt.w = 320;
				opt.h = 170;
				opt.ecs = $('#elementsCollectionStr').val();
				opt.gridId = $('#gridId').val();
				return parent.MMApi.clickOverlayById(id, opt);
			}
			
			if(id) {
				var summaryData = null;
				
				if(_microFirehouseDataList) {
					for(var index in _microFirehouseDataList) {
						if(_microFirehouseDataList[index].id == id) {
							summaryData = _microFirehouseDataList[index].summaryData;
							break;
						}
					}
				}
				
				setTimeout(function() {
					if($('#elementsCollectionStr').val() != "") {
						window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),320,170,id, undefined, summaryData);
					}
				},1000);
			}
		}
		
		//地图定位
		function gisPosition(locationDataArray){
			if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
				var opt = {};
				opt.w = 320;
				opt.h = 170;
				opt.ecs = $('#elementsCollectionStr').val();
				opt.gridId = $('#gridId').val();
				opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfResource.jhtml?ids="+res;
				return parent.MMApi.markerIcons(opt);
			}
			
			if("1" != window.parent.IS_ACCUMULATION_LAYER) {
				window.parent.clearSpecialLayer(layerName);
			}else {
				if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
					return;
				}else {
					window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
				}
			}
			
			if(locationDataArray && locationDataArray.length > 0) {
			  	if($('#elementsCollectionStr').val() != "") {
					window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+undefined+"','"+$('#elementsCollectionStr').val()+"')";
		
					window.parent.getArcgisDataOfZhuanTi(undefined,$('#elementsCollectionStr').val(),320,170, undefined, undefined, undefined, false, locationDataArray);
				}else {
					var gisDataUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisLocateDataListOfResource.jhtml?ids="+res;
				  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfResource('','"+gisDataUrl+"')";
				  	window.parent.getArcgisDataOfResource('',gisDataUrl);
				}
			}
		}
	})(jQuery);
	
	
</script>
</body>
</html>