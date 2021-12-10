<!DOCTYPE html>
<html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租屋列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style>
.liebiao p{float:left;}
</style>
</head>
<body style="border:none;scolling:yes">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="condition">
            	<ul>
                	<li><input name="" id="searchContent" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='输入户主姓名、公民身份号码、地址进行查询';" onfocus="if(this.value=='输入户主姓名、公民身份号码、地址进行查询')value='';" value="输入户主姓名、公民身份号码、地址进行查询" style="width:240px;" /></li>
                </ul>
                <!--
                <ul>
                	<li class="LC1">是否到期：</li>
                	<li class="LC2">
                		<select name="type" id="type" class="sel1">
                			<option value="1">正常</option>
                			<option value="2">到期</option>
                		</select>
                	</li>
                </ul>
                -->
                <ul class="Star">
                	<li class="biaoti">选择星级：</li>
                    <li id="oneStars" onClick="ChoseStars('oneStars')"></li>
                    <li id="twoStars" onClick="ChoseStars('twoStars')"></li>
                    <li id="threeStars" onClick="ChoseStars('threeStars')"></li>
                    <li id="fourStars" onClick="ChoseStars('fourStars')"></li>
                    <li id="fiveStars" onClick="ChoseStars('fiveStars')"></li>
                </ul>
                <!--
            	<ul>
                	<li class="LC1">&nbsp;</li>
                	<li class="LC2">-->
                		<div class="SearchBtn">
                			<input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val());"/>
                		</div>
                <!--	</li>
                </ul>-->
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
     <!--   <div class="showRecords">-->
        	<div class="tushi2">
        	<!--
        		<img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_03.png" />星级
        		<img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" />到期
        	-->	
        		<table style="background-color: #f7f7f7;height:35px;width:100%;" class="pageContent">
					<tr>
						<td onclick="orderByHouseStatus(3)" style="text-align: center;cursor: hand;">
							<img style="width:18px;height:18px;vertical-align: middle;" title="星级" src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_03.png"><span id="timeSpan1" style="color:#020868;font-size:14px;">星级</span>
						</td>
						<td onclick="orderByHouseStatus(2)" style="text-align: center;cursor: hand;">
							<img style="width:18px;height:18px;vertical-align: middle;" title="到期" src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png"><span id="timeSpan2" style="color:#020868;font-size:14px;">到期</span>
						</td>
					</tr>
				</table>
        	</div>
        	
    <!--    </div>-->
        <div class="ListShow content" style="" id="content">
        	
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
    <script src="js/jquery.mCustomScrollbar.concat.min.js"></script>
    <script type="text/javascript">
		function CloseBtn(){
			$(".ListSearch").slideUp(200);
		}
		function SearchBtn(){
			$(".ListSearch").slideDown(200);
		}
		var $NavDiv = $(".zonglan p");
		       $NavDiv.click(function(){
		              $(this).addClass("current").siblings().removeClass("current");
		              var NavIndex = $NavDiv.index(this);
		              $(".tab_box div.tabs").eq(NavIndex).show().siblings().hide();
		       });
	</script>
<script type="text/javascript">
	var searchStars = "";
	var oneStars = document.getElementById('oneStars');
	var twoStars = document.getElementById('twoStars');
	var threeStars = document.getElementById('threeStars');
	var fourStars = document.getElementById('fourStars');
	var fiveStars = document.getElementById('fiveStars');
	

var isExpire = "star";
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

function orderByHouseStatus(type) {
	if(type==3) {
		$("#timeSpan2").removeClass("timeActive");
		$("#timeSpan1").addClass("timeActive");
		isExpire = "star";
	} else {
		$("#timeSpan1").removeClass("timeActive");
		$("#timeSpan2").addClass("timeActive");
		isExpire = "true";
	}
	loadMessage(1,$("#pageSize").val());
}

function ShowOrCloseSearchBtn(){
var temp= $(".ListSearch").is(":hidden");//是否隐藏 
if(temp == false) {
	$(".ListSearch").hide();
}else {
	$(".ListSearch").show();
}
//var temp1= $(".ListSearch").is(":visible");//是否可见
	
}

//选择要查询的星级
function ChoseStars(id){
	
	if(id=='oneStars'){
		oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		searchStars="1";
		twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
	}
	if(id=="twoStars"){
		oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		searchStars="2";
		threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
	}
	if(id=="threeStars"){
		oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		searchStars="3";
		fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
		fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
	}
	if(id=="fourStars"){
		oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		searchStars="4";
		fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
	}
	if(id=="fiveStars"){
		oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_select.png)';
		searchStars="5";
	}
}

function CloseSearchBtn(){
	$(".ListSearch").hide();
}
	$(document).ready(function(){
	    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	    
	   /* $('#order').combobox({
			onChange:function(){
				loadMessage(1,$("#pageSize").val());
			}
		});*/
	});
	var _total = 0;
	var results="";//获取定位对象集合
	function loadMessage(pageNo,pageSize){
		var search = $('#searchContent').val();
		results="";
		var gridId = $('#gridId').val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&isExpire='+isExpire;
		if(searchStars!=""){
			postData = postData + '&searchStars='+searchStars;
		}
		if(search=='输入户主姓名、公民身份号码、地址进行查询'){
			//var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&isExpire='+isExpire;
		}else{
			var postData = postData + '&searchContent='+search;
		}
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/starRentRoomListData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				$('#searchContent').val('输入户主姓名、公民身份号码、地址进行查询');
				oneStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
				twoStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
				threeStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
				fourStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
				fiveStars.style.backgroundImage='url(${uiDomain!''}/images/map/gisv0/special_config/images/star_noselect.png)';
				searchStars="";
		
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				if (pageNo == 1) {
					_total = data.total;
				} else if (pageNo > 1) {
					data.total = _total;
				}
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				tableBody+='<div class="liebiao">';
				if(list && list.length>0) {
					tableBody+='<ul>';
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					    if((i+1)%2==0){//基数
					       tableBody+='<li onclick="selected(\''+val.rentId+'\',\''+val.roomId+'\',\''+val.buildingId+'\',\''+(val.roomName==null?'':val.roomName)+'\')" class="bg">';
					       
					       //星级出租屋，星级判断
					       if(val.stats==1){
					       	tableBody+='<p class="OneStar"></p>';
					       }else if(val.stats==2){
					       	tableBody+='<p class="TwoStar"></p>';
					       }else if(val.stats==3){
					       	tableBody+='<p class="ThreeStar"></p>';
					       }else if(val.stats==4){
					       	tableBody+='<p class="FourStar"></p>';
					       }else if(val.stats==5){
					       	tableBody+='<p class="FiveStar"></p>';
					       }else if(val.stats==0 || val.stats==null){
					       	tableBody+='<p class="NoStar"></p>';
					       }
					       //tableBody+='<p class="OneStar"></p>';
					       if(val.isExpires=="2"){
					       		 tableBody+='<p><img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" /><b>'+(val.roomAddress==null?'':val.roomAddress)+'</b></p>';
					       }else{
					       		tableBody+='<p style="width:170px;"><b>'+(val.roomAddress==null?'':val.roomAddress)+'</b></p>';
					       }
					       
						   tableBody+='<br><p style="width:170px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/fangdong.png" />'+(val.ownerName==null?'':val.ownerName)+'&nbsp;&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />'+(val.mobileTelephone==null?'':val.mobileTelephone)+'</p>';
						   //tableBody+='<p>网格：'+(val.gridName==null?'':val.gridName)+'</p>';
						   
					       //tableBody+='<p>到期时间：'+(val.hireEndStr==null?'':val.hireEndStr)+'</p>';
					       tableBody+='<div class="clear"></div>';
					       tableBody+='</li>';
					       
					    }else{
					       //tableBody+='<li onclick="selected(\''+val.rentId+'\',\''+val.roomId+'\',\''+val.buildingId+'\',\''+(val.roomName==null?'':val.roomName)+'\')">';
					         tableBody+='<li onclick="selected(\''+val.rentId+'\',\''+val.roomId+'\',\''+val.buildingId+'\',\''+(val.roomName==null?'':val.roomName)+'\')" >';
					       
					       //星级出租屋，星级判断
					       if(val.stats==1){
					       	tableBody+='<p class="OneStar"></p>';
					       }else if(val.stats==2){
					       	tableBody+='<p class="TwoStar"></p>';
					       }else if(val.stats==3){
					       	tableBody+='<p class="ThreeStar"></p>';
					       }else if(val.stats==4){
					       	tableBody+='<p class="FourStar"></p>';
					       }else if(val.stats==5){
					       	tableBody+='<p class="FiveStar"></p>';
					       }else if(val.stats==0 || val.stats==null){
					       	tableBody+='<p class="NoStar"></p>';
					       }
					       if(val.isExpires=="2"){
					       		 tableBody+='<p><img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" /><b>'+(val.roomAddress==null?'':val.roomAddress)+'</b></p>';
					       }else{
					       		tableBody+='<p style="width:170px;"><b>'+(val.roomAddress==null?'':val.roomAddress)+'</b></p>';
					       }
					       
						   tableBody+='<br><p style="width:170px;"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/fangdong.png" />'+(val.ownerName==null?'':val.ownerName)+'&nbsp;&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/dianhua.png" />'+(val.mobileTelephone==null?'':val.mobileTelephone)+'</p>';
						   //tableBody+='<p>网格：'+(val.gridName==null?'':val.gridName)+'</p>';
						   
					       //tableBody+='<p>到期时间：'+(val.hireEndStr==null?'':val.hireEndStr)+'</p>';
					       tableBody+='<div class="clear"></div>';
					       tableBody+='</li>';
					      
					    }
						results=results+","+val.rentId;
					}
			        tableBody+='</ul>';
					results=results.substring(1, results.length);
				} else {
					tableBody+='<ul>未查到相关数据！！</ul>';
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
	    loadMessage(pagenum,pageSize);
	}
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	function selected(rentId, roomId, buildingId, roomName) {
		
		
		if($('#elementsCollectionStr').val() != "") {
			//window.parent.locationObjDetailOnMap(rentId,$('#elementsCollectionStr').val(),400,215);
			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),400,235,rentId)
		}else {
			window.parent.locationHouse(rentId);
		}
	}
	//--定位
	function gisPosition(res){
	
		if (res==""){
			return ;
		}
		
		if($('#elementsCollectionStr').val() != "") {
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
			window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),400,235);
		}else {
			var gisDataUrl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/getArcgisLocateDataListOfRentRoom.jhtml?ids="+res;
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfRentRoom('"+gisDataUrl+"')";
			window.parent.getArcgisDataOfRentRoom(gisDataUrl);
		}
		
	}
</script>
</body>
</html>