<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>隐患路段、隐患路口列表</title>
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
                	<li class="LC1">
                		<#if drType?? && drType=='1'>
                		路口名称：
                		<#elseif drType?? && drType=='2'>
                		路段名称：
                		</#if>
                	</li>
                	<li class="LC2"><input id="drName" name="drName" type="text" class="inp1" /></li>
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
		var results = "",//获取定位对象集合
			layerName = "",
			inputNum = 1,
			currentPageNum = 1;
		
		$(document).ready(function(){
			var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
	       	$("#content").height(winHeight-56); 
		    loadMessage(1,$("#pageSize").val());
		});
		
		$('#drName').keydown(function(e){ 
			if(e.keyCode==13){ 
				loadMessage(1,$("#pageSize").val());
			} 
		}); 
		
		$('#inputNum').keydown(function(e){ 
			if(e.keyCode==13){ 
				pageSubmit();
			} 
		});
		
		$("#moreSearch").toggle(function(){
		    $(".AdvanceSearch").css("display","block");
		},function(){
		   $(".AdvanceSearch").css("display","none");
		});
		
		function pageSubmit() {
			inputNum = $("#inputNum").val();
			var pageCount = $("#pageCount").text();
			
			if(isNaN(inputNum)) {
				inputNum = 1;
			}
			if(inputNum > pageCount) {
				inputNum = pageCount;
			}
			if(inputNum <= 0 || inputNum == "") {
				inputNum = 1;
			}
			change('4');
		}
	
		function ShowOrCloseSearchBtn() {
			var temp= $(".ListSearch").is(":hidden");//是否隐藏 
			if(temp == false) {
				$(".ListSearch").hide();
			} else {
				$(".ListSearch").show();
			}
		}
		
		function CloseSearchBtn() {
			$(".ListSearch").hide();
		}
		
		function loadMessage(pageNo,pageSize,searchType) {
			layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
			window.parent.currentLayerName = layerName;
			if('searchBtn'==searchType) {
				window.parent.clearSpecialLayer(layerName);
				window.parent.currentListNumStr = "";
			}
			results="";
			var drName = $('#drName').val();
			var pageSize = $("#pageSize").val();
			$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
	    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
			var postData = 'page='+pageNo+'&rows='+pageSize+'&drName='+drName+"&drType=${drType!}&infoOrgCode=${infoOrgCode!}";
			
			$.ajax({
				type: "POST",
				url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/listDangerRoadData.jhtml?t='+Math.random(),  
				data: postData,
				dataType:"json",
				success: function(data){
					$.unblockUI();
					//设置页面页数
					$('#pagination-num').text(pageNo);
					$('#inputNum').val(pageNo);
					$('#records').text(data.total);
					var totalPage = Math.floor(data.total/pageSize);
					if(data.total%pageSize>0) totalPage+=1;
					$('#pageCount').text(totalPage);
					var list=data.rows;
					var tableBody = '<div class="liebiao">';
					
					if(list && list.length > 0) {
						for(var i = 0, len = list.length; i < len; i++){
							var val=list[i],
								drName = val.drName,
								infoOrgName = val.infoOrgName,
						  	  	roadName = val.roadName,
						  	  	infoOrgTitle = val.infoOrgFullName || val.infoOrgName || '';
						  
						  if(!drName) {
						  	drName = "&nbsp;";//为了排版整齐
						  } else if(drName.length>10) {
						  	drName = drName.substring(0,10)+"...";
						  }
						  
						  if(!infoOrgName) {
						  	infoOrgName = "&nbsp;";
						  } else if(infoOrgName.length>6) {
						  	infoOrgName = infoOrgName.substring(0,6)+"...";
						  }
						  
						  if(!roadName) {
						  	roadName = "&nbsp;";
						  } else if(roadName.length>10) {
						  	roadName = roadName.substring(0,10)+"...";
						  }
						  
						  tableBody+='<dl onclick="selected(\''+val.drId+'\',\''+(val.drName==null?'':val.drName)+'\')">';
							  tableBody+='<dt>';
								  tableBody+='<span class="fr" title="' +infoOrgTitle + '">'+
											  	infoOrgName+
											 '</span>';
								  
								  tableBody+='<b class="FontDarkBlue" title="'+ (val.drName==null?'':val.drName) +'">'+ drName +'</b>';
								  
								  tableBody+='<dd>';
								  
								  tableBody+='<span title="联系电话" class="fr"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />'+ (val.collTel==null?'':val.collTel) +'</span>';
								  tableBody+='<span title="'+ (val.roadName==null?'':val.roadName) +'">'+ roadName +'</span>';
								  
								  tableBody+='</dd>';
							  
							  tableBody+='</dt>';
						  tableBody+='</dl>';
						
						  results=results+","+val.drId;
						}
						
						results=results.substring(1);
					} else {
						tableBody += '<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
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
			
			$("#inputNum").val(currentPageNum);
			
		    loadMessage(pagenum,pageSize);
		}
		
		function selected(id, name){
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
				}
			},1000);
		}
		//地图定位
		function gisPosition(res){
			if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
				var opt = {};
				opt.w = 340;
				opt.h = 195;
				opt.ecs = $('#elementsCollectionStr').val();
				opt.gridId = $('#gridId').val();
				opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfDangerRoad.jhtml?ids="+res;
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
				var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfDangerRoad.jhtml?ids="+res;
				window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
				window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),340,195);
			}
		}
	</script>
</body>
</html>