<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>两违案件列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<style type="text/css">
		#content{
			height: 500px;
		}
		.inp1 {
		    width: 130px;
		    height: 24px;
		    border: 1px solid #c3c3c3;
		    line-height: 24px;
		}
	</style>
	<script type="text/javascript">
		var gridTypeArr = {
		    "exclusiveGrid" : "exclusiveGrid"
		}
	</script>
</head>
<body style="border:none">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div id="searchDiv" class="MetterCondition" style="max-height: 500px;overflow-y: scroll;overflow-x: hidden">
            	<ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:100px;">用地单位或个人：</li>
                	<li class="LC2"  style="width: 60px;float:left; line-height:26px;"><input id="useUnitPer" name="useUnitPer" type="text" class="inp1" /></li>
                </ul>
                
                <div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="SearchFunction();" /></div>
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
	$(function () {
		getTwoViolationlistDataJsonp(orgCode,'1',$('#pageSize').val(),$('#useUnitPer').val());
	})


	var GMIS_DOMAIN = '${SQ_GMIS_URL}';
	var orgCode=window.parent.$('#orgCode').val();
	var pageSize = $('#pageSize').val();
	var pageNo = '1';
	
	function getTwoViolationlistDataJsonp(orgCode,pageNo,pageSize,useUnitPer){
        $.ajax({
            type: 'POST',
            url: GMIS_DOMAIN+'/gmis/twoViolation/listDataJsonp.json?jsonpcallback=getlistDataJsonp',
            data: {orgCode:orgCode,page:pageNo,rows:pageSize,searchOutline : 'true',useUnitPer:useUnitPer},
            dataType: 'jsonp',
        });
    }
	function dataChange(OldData){
		 var arr= new Array();
		for(var i= 0;i<OldData.length;i++){
			var newObj = {};
	        newObj.x = OldData[i].cenX;
	        newObj.y = OldData[i].cenY;
	        //newObj.x = '118.2702054';
	        //newObj.y = '25.00571002';
			newObj.id = OldData[i].id;
			newObj.tbId = OldData[i].id;
			//newObj.type = OldData[i].constructionStatus;
			newObj.type = OldData[i].id;
			newObj.hs=OldData[i].mapOutline;
		    newObj.lineColor = "#ff0000"; //边界线颜色
	        newObj.lineWidth = 1; //边界线宽度
	        newObj.areaColor = "#ff0000"; //区域颜色
	        newObj.nameColor = "#ff0000"; //区域颜色
	        //newObj.name  = OldData[i].mapNum;  //网格名称
	        newObj.gridName  = OldData[i].orgName;  //网格名称
	        newObj._oldData = OldData[i];
	        newObj.colorNum = 0;//区域透明度
	        arr.push(newObj);
		}
        return arr;
    }
	function dataChange1(OldData){
		var arr= new Array();
		for(var i= 0;i<OldData.length;i++){
			var newObj = {};
	        newObj.X = OldData[i].cenX;
	        newObj.Y = OldData[i].cenY;
			newObj.ID = OldData[i].id;
			newObj.WID = OldData[i].id;
	        newObj.NAME  = OldData[i].mapNum;  //网格名称
	        newObj.MAP_TYPE  = 5;  //网格名称
	        arr.push(newObj);
		}
        return arr;
	}
    function getlistDataJsonp(data){
    	var data=data.data;
    	//轮廓
    	var oldData = dataChange(data.rows);
    	window.parent.getGridLayer(oldData,"NNTBLKHSLayer");
    	//撒点：图标＋名称
    	var oldData1 = dataChange1(data.rows);
    	window.parent.$.fn.ffcsMap.render4PointList($('#elementsCollectionStr').val(),'NNTBLKLayer',oldData1,'${uiDomain}'+"/web-assets/_wangge/images/icon_IllegalBuilding.png",36,36,14);
    	
		//设置页面页数
		$('#pagination-num').text(currentPageNum);
		$('#records').text(data.total);
		var totalPage = Math.floor(data.total/pageSize);
		if(data.total%pageSize>0) totalPage+=1;
		$('#pageCount').text(totalPage);
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"NNTBLKLayer");
		window.parent.currentLayerName = layerName;
		var list=data.rows;
		var tableBody="";
		tableBody+='<div class="liebiao">';
		if(list && list.length>0) {
			tableBody+='<ul>';
            for (var i = 0; i < list.length; i++) {
                var val = list[i];
                tableBody += '<dl onclick="selected(\'' + val.id + '\')">';
                tableBody += '<dt>';
                
                tableBody += '<b class="FontDarkBlue" title=\'' + val.mapNum + '\'>' + (val.mapNum == null ? '' : val.mapNum) + '</b>';
                tableBody += '</dt>';
                tableBody += '<dd>'+val.useUnitPer+'</dd>';

                tableBody += '</dl>';
            }
	        tableBody+='</ul>';
		} else {
			tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
		}
        tableBody+='</div>';
		var conHeight=window.parent.$('#get_grid_name_frme').height();
		$("#content").height(conHeight-70);
		$("#content").html(tableBody);
    }

var inputNum;

function ShowOrCloseSearchBtn(){
	var temp= $(".ListSearch").is(":hidden");//是否隐藏 
	if(temp == false) {
		$(".ListSearch").hide();
	}else {
		$(".ListSearch").show();
	}
}
$('#useUnitPer').keydown(function(e){ 
	if(e.keyCode==13){ 
		getTwoViolationlistDataJsonp(orgCode,'1',$('#pageSize').val(),$('#useUnitPer').val());
	} 
});
function SearchFunction(){
	getTwoViolationlistDataJsonp(orgCode,'1',$('#pageSize').val(),$('#useUnitPer').val());
}
function CloseSearchBtn(){
	$(".ListSearch").hide();
}

function pageSubmit() {
    inputNum = $("#inputNum").val();
    var pageCount = $("#pageCount").text();
    if (isNaN(inputNum)) {
        inputNum = 1;
    }
    if (parseInt(inputNum) > parseInt(pageCount)) {
        inputNum = pageCount;
    }
    if (inputNum <= 0 || inputNum == "") {
        inputNum = 1;
    }
    change('4');
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
		$('#pagination-num').val(pagenum);
		$('#pagination-num').text(pagenum);
		getTwoViolationlistDataJsonp(orgCode,pagenum,pageSize);
	    
	}
	
	function selected(id) {
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 342;
			opt.h = 245;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		window.parent.clearGridLayer();
	
		setTimeout(function() {
			if($('#elementsCollectionStr').val() != "") {
				window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),342,245,id)
			}else {
				window.parent.localtionGridsPoints(id,'');
			}
		},1000);
	}
</script>
</body>
</html>