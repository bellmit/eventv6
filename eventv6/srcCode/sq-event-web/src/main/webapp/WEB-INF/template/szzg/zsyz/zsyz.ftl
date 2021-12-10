<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ComboBox.ftl" />
</head>
<body style="border:none">
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<div class="ListSearch">
        	<div class="condition">
        		<ul>
                	<li class="LC1" style="width:72px;">项目名称：</li>
                	<li class="LC2" style="width:140px;">
                	<input type="text" id="pName" value="" class="inp1 InpDisable" style="width:140px;" />
                	
                	</li>
                </ul>
        		<ul>
                	<li class="LC1" style="width:72px;">项目类型：</li>
                	<li class="LC2" style="width:140px;">
                	<input type="hidden" id="pType"/>
                    <input type="text" style="width:140px;" id="pTypeStr" name="pTypeStr" class="inp1" />
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

function CloseSearchBtn(){
	$(".ListSearch").hide();
}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    
	});
	
	function selected(id){
		setTimeout(function() {
				var elementsCollectionStr = $('#elementsCollectionStr').val();
			if (elementsCollectionStr != "") {
				window.parent.getDetailOnMapOfListClick(elementsCollectionStr,null, null,id);
			} else {
				window.parent.localtionKeyPlacePoint(id,'');
			}
		},1000);
	}
	//地图定位
	function gisPosition(res){
		var elementsCollectionStr = $('#elementsCollectionStr').val();
		var url =  window.parent.analysisOfElementsCollection(elementsCollectionStr,"menuSummaryUrl");
		//window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"++"','"+elementsCollectionStr+"')";
		window.parent.getArcgisDataOfZhuanTi(url,elementsCollectionStr,null,null,30,39,{},false,res);

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
		
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
    	var postData = {page:pageNo,rows:pageSize};
    	
    	var pType = document.getElementById('pType').value;
    	if(pType.length>0){postData['pType']=pType;};
    	
    	var pName = document.getElementById('pName').value;
    	if(pName.length>0){postData['pName']=pName;};
		
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/szzg/zgStatisticsController/ZSYZlistData.json?t='+Math.random(), 
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
					  val.name=val.projectName, val.wid=val.id;
					  tableBody+='<dl onclick="selected(\''+val.id+'\')">';
					  tableBody+='<dt>';
					  //tableBody+='<span class="fr">'+countyStatusObj[val.status]+'</span>';
					  tableBody+='<b class="FontDarkBlue" title="'+val.projectName+'">'+val.projectName+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd>地址：'+val.addr+'</dd>';
					  tableBody+='</dl>';
					results=results+","+val.id;
					}
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
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
	
	  $(function() {

			AnoleApi.initListComboBox("pTypeStr", "pType", null, null, null, {
				DataSrc: [{"name":"签约", "value":"1"},{"name":"开工", "value":"2"}],
				 ShowOptions : {
		EnableToolbar : true
	  }

			});
    });
	
</script>
</body>
</html>