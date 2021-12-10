<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>安全隐患专题图层</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="infoOrgCode" value="${orgCode}" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li class="LC1">检查场所：</li>
                	<li class="LC2">
                		<input id="corplaceName"  type="text" class="inp1" />
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">检查情况：</li>
                	<li class="LC2">
                		<input id="checkContent"  type="text" class="inp1" />
                	</li>
                </ul>
            	<ul>
                	<li class="LC1">对象类型：</li>
                	<li class="LC2">
                		<select name="siteType" id="siteType" class="sel1">
                			<option value="">------不限------</option>
							<option value="1">企业</option>
							<option value="2">场所</option>
							<option value="3">出租屋</option>
                		</select>
                	</li>
                </ul>
                <ul class="time">
                	<li><input name="beginCheckDate" id="beginCheckDate" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='检查开始时间')value='';WdatePicker({isShowWeek:true});" value="检查开始时间" style="width:110px;" readonly="readonly" /></li>
                    <li style="width:20px;">&nbsp;-</li>
                	<li><input name="checkDate" id="checkDate" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='检查结束时间')value='';WdatePicker({isShowWeek:true});" value="检查结束时间" style="width:110px;" readonly="readonly" /></li>
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
		var infoOrgCode = $('#infoOrgCode').val();
		var corplaceName = $('#corplaceName').val();
		var checkContent = $('#checkContent').val();
		var siteType = $("#siteType option:selected").val();
		
		var beginCheckDate = $('#beginCheckDate').val();
		
		if(beginCheckDate=="检查开始时间"){
			beginCheckDate="";
		}
		
		var checkDate = $('#checkDate').text();
		
		if(checkDate=="检查结束时间"){
			checkDate="";
		}
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
		var postData = 'page='+pageNo+'&rows='+pageSize+'&infoOrgCode='+infoOrgCode+'&corplaceName='+corplaceName+'&checkContent='+checkContent+'&siteType='+siteType+'&beginCheckDate='+beginCheckDate+'&checkDate='+checkDate;
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/riskListData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){ 
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo==1) {
					$('#records').text(data.total);
				}
				var totalCount=$('#records').text();
				var totalPage = Math.floor(totalCount/pageSize);
				if(totalCount%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  tableBody+='<dl onclick="selected(\''+val.checkId+'\')">';
					  tableBody+='<dt>';
					  
					  if (val.checkDate != null) {
						  var date = new Date(val.checkDate);
						  var dateS = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
						  tableBody+='<span class="fr">'+(dateS)+'</span>';
					  }
					  tableBody+='<b class="FontDarkBlue" >'+(val.corplaceName==null?'':val.corplaceName)+'</b>';
					  tableBody+='</dt>';
					  tableBody+='<dd title='+val.checkContent+'>'+(val.checkContent==null?'':val.checkContent.substring(0, 36))+'</dd>';
					  tableBody+='</dl>';
					  results=results+","+val.checkId;
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
		setTimeout(function() {
			window.parent.localtionRiskPoint(id);
		},1000);
	}
	
	//--定位
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
		
		var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfRisk.jhtml?ids="+res;
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfRisk('"+gisDataUrl+"')";
		window.parent.getArcgisDataOfRisk(gisDataUrl);
	}
</script>
</body>
</html>