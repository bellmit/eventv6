<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>两车管理列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">关键字：</li>
                	<li><input name="keyWord" type="text" class="inp1" id="keyWord" value="所在路段/停放地点" style="color:gray;" onfocus="if(this.value=='所在路段/停放地点'){this.value='';}" onblur="if(this.value==''){$(this).attr('style','color:gray;');this.value='所在路段/停放地点';}" onkeydown="_onkeydown();" /></li>
                </ul>
        		<ul>
                	<li class="LC1">类别：</li>
                	<li class="LC2">
                		<select name="type" id="type" class="sel1">
                			<option value="">---全部---</option>
							<#if typeName??>
								<#list typeName as t>
									<option value="${t.dictGeneralCode}">${t.dictName}</option>
								</#list>
							</#if>
                		</select>
                	</li>
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

function ShowOrCloseSearchBtn(){
	var temp= $(".ListSearch").is(":hidden");//是否隐藏 
	if(temp == false) {
		$(".ListSearch").hide();
	}else {
		$(".ListSearch").show();
	}
}

$('#keyWord').keydown(function(e){ 
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
});
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
		var keyWord = $('#keyWord').val();
		
		if (keyWord == "所在路段/停放地点") {
			keyWord = "";
		}
		
		var type = $("#type").val();
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
    	var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&keyWord='+keyWord+'&type='+type;
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/parkingManageListData.json?t='+Math.random(), 
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
					  
					  var onRoad = val.onRoad;
					  if(onRoad!=null && onRoad!="" && onRoad.length>15){
						onRoad = onRoad.substring(0,15);
					  }else{
						onRoad = onRoad;
					  }
					  
					  var placeAddr =  val.placeAddr;
					  if(placeAddr!=null && placeAddr!="" && placeAddr.length>13){
						placeAddr = placeAddr.substring(0,13);
					  }else{
						placeAddr = placeAddr;
					  }
					  
					  tableBody+='<dl onclick="selected(\''+val.pmId+'\',\''+(val.onRoad==null?'':val.onRoad)+'\')">';
					  tableBody+='<dt>';
					  
					  var type = val.type;
					  
					  if (type != null && type != '') {
					  	switch(type) {
							case "01":
								type = "代管点";
								break;
							case "02":
								type = "非代管点";
								break;
							case "03":
								type = "物业管理";
								break;
							case "04":
								type = "单位自管";
								break;
						}
					  }
					  
					  tableBody+='<span class="fr">'+(type)+'</span>';
					  tableBody+='<b class="FontDarkBlue" title=\''+(val.onRoad==null?'':val.onRoad)+'\'>'+onRoad+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title=\''+(val.placeAddr==null?'':val.placeAddr)+'\'>'+(placeAddr)+'</dd>';
					  tableBody+='</dl>';
					
					  results=results+","+val.pmId;
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
	
	function selected(id){
		if(window.parent.qnviccub != undefined) {
	  		$("#qnviccub", window.parent.document).remove();
	  	}
	  	
		setTimeout(function() {
			if ($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),322,220,id)
			} else {
				window.parent.localtionParkingManagePoint(id,'');	//locationHouse
			}
		},1000);
	}
	//地图定位
	function gisPosition(res){
		if("1" != window.parent.IS_ACCUMULATION_LAYER) {
			window.parent.clearSpecialLayer(layerName);
		}else {
			if(window.parent.currentListNumStr.indexOf(currentPageNum+"")>=0) {
				return;
			}else {
				window.parent.currentListNumStr = window.parent.currentListNumStr+","+currentPageNum;
			}
		}
		
		if (res==""){
			return ;
		}

		if($('#elementsCollectionStr').val() != "") {
			var parkingManageUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/getArcgisLocateDataListOfParkingManage.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+parkingManageUrl+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(parkingManageUrl,$('#elementsCollectionStr').val(),322,220);
		}else {
			var parkingManageUrl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/getArcgisLocateDataListOfParkingManage.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfParkingManageListByIds('"+parkingManageUrl+"','')";
			window.parent.getArcgisDataOfParkingManageListByIds(parkingManageUrl,'');
		}
	}
</script>
</body>
</html>