<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员列表</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/yancheng/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/_big-screen/yancheng/css/yc-list.css"/>
<style>
.ycl-cl-item i{
cursor: pointer;
}

.ycl-clid-video>i {
    cursor: pointer !important;
    background: url('${uiDomain!''}/web-assets/_big-screen/yancheng/images/video-voice/list-video1.png') no-repeat;
    background-size: 100% 100%;
}
</style>
</head>
<body style="border:none">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="<#if pageSize??>${pageSize?c}<#else>20</#if>" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div id="searchDiv" class="MetterCondition" style="max-height: 500px;overflow-x: hidden">
            	<ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:66px;">姓名：</li>
                	<li class="LC2" style="float:left; line-height:26px;"><input id="name" name="name" type="text" class="inp1" /></li>
                </ul>
				<#if infoOrgCode?index_of("320903") == 0>
					<ul class="content" style="height:26px;">
						<li class="LC1" style="float:left; line-height:26px; width:66px;">证件号码：</li>
						<li class="LC2" style="float:left; line-height:26px;"><input id="identityCard" name="identityCard" type="text" class="inp1" /></li>
					</ul>
					<ul style="height:26px;">
						<li class="LC1" style="float:left; line-height:26px; width:66px;">手机号：</li>
						<li class="LC2" style="float:left; line-height:26px;"><input id="mobileTelephone" name="mobileTelephone" type="text" class="inp1" /></li>
					</ul>
			    </#if>
                <ul id="content-md0" class="type content light">
                	<p>网格层级：</p>
            	<#if infoOrgCode?index_of("320903") == 0>
            		<li onclick="selectOnly(this)" gridLevel="">网格及以上</li>
        			<li onclick="selectOnly(this)" class="current" gridLevel="6">网格级</li>
            	<#else>
            		<li onclick="selectOnly(this)" class="current" gridLevel="">网格及以上</li>
        			<li onclick="selectOnly(this)" gridLevel="6">网格级</li>
            	</#if>
                    <div class="clear"></div>
                </ul>
                <ul id="content-md1" class="type content light">
                	<p>在线状态：</p>
            		<li onclick="selectOnly(this)" class="current" style="width:60px;" onlineStatus="">全部</li>
            		<li onclick="selectOnly(this)" style="width:60px;" onlineStatus="1">在线</li>
            		<li onclick="selectOnly(this)" style="width:60px;" onlineStatus="2">离线</li>
                    <div class="clear"></div>
                </ul>
                <ul id="content-md2" class="type content light">
                	<p>选择职务：</p>
            		<#if dutyDC??>
						<#list dutyDC as l>
							<li onclick="select(this)" id="${l.dictId}" value="${l.dictGeneralCode}">${l.dictName}</li>
						</#list>
					</#if>
                    <div class="clear"></div>
                </ul>
				<div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="loadMessage(1,$('#pageSize').val(),'searchBtn');" /></div>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="showRecords" style="height:55px;">
            <div class="ycl-ct-btn" style="display: none;">
                    <div class="ycl-ctb-content clearfix">
                        <a href="javascript:coid(0);" class="fl clearfix">
                            <i class="sp"></i>
                            <p>多人视频</p>
                        </a>
                        <a href="javascript:coid(0);" class="fr clearfix">
                            <i class="yy"></i>
                            <p>多人语音</p>
                        </a>
                    </div>
                </div>
        	<ul>
        		<li style="padding-left:0px;">共<span id="records">0</span>人，其中：</br>在线人数<span id="OnLinecount">0</span>人，离线人数<span id="NoLinecount">0</span>人。</li>
        	</ul>
        </div>
       		
        <div class="ListShow content ycl-c-list bs" style="" id="content">
           
 
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
var isOnAnychat=false;//anychat是否在线
/*默认选中网格员*/
(function($){
    var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
    $("#content").height(winHeight-56);
    isOnAnychat=parent.isInitAnycat==0?false:true;
    if (typeof parent.getMpushOnlineList!= "undefined"){
    	parent.getMpushOnlineList('getOnlineUsers'); 
    }
    
    loadMessage(1,$("#pageSize").val());
    $("#searchDiv").css({
        maxHeight: $("#content").height()
    });
    $("#searchDiv").mCustomScrollbar({theme:"minimal-dark"});

})(jQuery);

var countyGridName = "<#if countyGridName??>${countyGridName!''}</#if>";
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
//var temp1= $(".ListSearch").is(":visible");//是否可见
	
}
$('#name').keydown(function(e){ 
	if(e.keyCode==13){ 
		loadMessage(1,$("#pageSize").val());
	} 
});
function CloseSearchBtn(){
	$(".ListSearch").hide();
}

	var results=[];//获取定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		if ($("#elementsCollectionStr").val() != "") {
			layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
			window.parent.currentLayerName = layerName;
		}
		
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer(layerName);
			window.parent.currentListNumStr = "";
		}
		results=[];
		var gridId = $('#gridId').val();
		var name = $('#name').val();
		<#if infoOrgCode?index_of("320903") == 0>
			var identityCard = $('#identityCard').val();
			var mobileTelephone = $('#mobileTelephone').val();
		</#if>
		var pageSize = $("#pageSize").val();
        $.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
    	var dutys = "";
        var userDutys = [];
    	var onlineStatus = "";
    	var gridLevel = "";
    	var postData;
    	$('#content-md0').find('li').each(function() {
			if ($(this).attr('class') == 'current') {
				gridLevel = $(this).attr('gridLevel');
			}
		});
    	$('#content-md1').find('li').each(function() {
			if ($(this).attr('class') == 'current') {
				onlineStatus = $(this).attr('onlineStatus');
			}
		});
    	$('#content-md2').find('li').each(function(index) {
			if($(this).attr('class')=='current'){
				<#if isNanan??>
					userDutys = $(this).html() + "," + userDutys;
				<#else >
					dutys = $(this).attr('value') + "," + dutys;
				</#if>
			}
		});
		
		if (dutys!=null && dutys!="" && dutys.length > 0) {
			dutys = dutys.substring(0, dutys.length - 1);
			<#if infoOrgCode?index_of("320903") == 0>
				postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&duty='+dutys+'&identityCard='+identityCard+'&mobileTelephone='+mobileTelephone;
			<#else>
				postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&duty='+dutys;
			</#if>
		}else if (userDutys!=null && userDutys!="" && userDutys.length > 0) {
            userDutys = userDutys.substring(0, userDutys.length - 1);
            postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&userDutys='+userDutys;
        }  else {
		    <#if infoOrgCode?index_of("320903") == 0>
				postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&identityCard='+identityCard+'&mobileTelephone='+mobileTelephone;
			<#else>
				postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name;
			</#if>
		}
		if(onlineStatus != null && onlineStatus != ""){
            postData += '&onlineStatus='+onlineStatus;
		}
		if(gridLevel != null && gridLevel != ""){
            postData += '&gridLevel='+gridLevel;
		}
		if($('#elementsCollectionStr').val() != "") {
			postData += '&elementsCollectionStr='+$('#elementsCollectionStr').val();
		}
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminListData.json?t='+Math.random(), 
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
				if(list && list.length>0) {
					var locationPageSize = 0;//定位分页，每次100条
					var locationPageIds = "";
					for(var i=0;i<list.length;i++){
					  	var val=list[i];
					  	//判断是否在线
					    var active='active';
					    var statusTxt="在<br>线";
					    if (val.isOnline == '0') {
					  		statusTxt="离<br>线";
					  		active='';    
					  	}   
					  	
					  	//if (typeof parent.mupushUsers != "undefined" && parent.mupushUsers.indexOf(val.userId+"")==-1) 
					  	var userId='${userId}';
					  	//userId="30021138";///////////////////////////////////////////////
					  	tableBody+='<div  onclick="selected(\''+val.gridAdminId+'\', this)" class="ycl-cl-item clearfix bs '+active+'" style="cursor: pointer;" >';     
					  	tableBody+=' <div class="ycl-cli-status fl"><p >'+statusTxt+'</p></div>';
					  	tableBody+=' <div class="ycl-cli-data fl ">';   
						  		tableBody+='<div class="ycl-clid-phone clearfix">';  
						  		tableBody+='<p class="list-name ml10"' +'_userId="'+val.userId+'" title="'+(val.partyName == null ? '' : val.partyName)+'">' + (val.partyName == null ? '' : val.partyName) + '</p>';
						  		tableBody+='<p>' + (val.mobileTelephone==null?'': + (val.mobileTelephone))+'</p>';
						        tableBody+='</div>';
					  			tableBody+='<div class="ycl-clid-video clearfix">';
					  			
					  			if(val.userId != userId && (typeof parent.mupushUsers != "undefined") && parent.mupushUsers.length>0 && parent.mupushUsers.indexOf(val.userId+"")>-1 ){   
					  				tableBody+='<i  msgType="mpush" oper_="1" _isOnline="'+val.isOnline+'"  _userId="'+val.userId+'" _userName="'+val.userName+'" _partyName="'+val.partyName+'"></i>';
					  			}  
						  		  
					  			if(val.userId != userId &&isOnAnychat && val.enabledAnyChat &&  val.enabledAnyChat==1 ){ 
					  				tableBody+='<i oper_="1" _isOnline="'+val.isOnline+'" _userId="'+val.userId+'" _partyName="'+val.partyName+'"></i>';
					  			}
					  			if(val.infoOrgCode != null && val.infoOrgCode.length>6){
		                            tableBody+='<p class="ml10" title="'+(val.gridPath==null?'':val.gridPath)+'">'+(val.gridPath==null?'':val.gridPath)+'</p>';
								}else{
		                            tableBody+='<p class="ml10" title="'+(val.gridName==null?'':val.gridName)+'">'+(val.gridName==null?'':val.gridName)+'</p>';
								}
					  			tableBody+='</div>';
					  	       tableBody+='</div>';   
					  			
                        locationPageIds=locationPageIds+","+val.gridAdminId;
                        locationPageSize = locationPageSize + 1;
                        if(locationPageSize == 100 || locationPageSize==parseInt(pageSize)){
                            locationPageIds=locationPageIds.substring(1, locationPageIds.length);
                            results.push(locationPageIds);
                            locationPageIds = "";
                            locationPageSize = 0;
                        }else{
                            results.push(val.gridAdminId);
						}
                        tableBody+='  <a href="javascript:void(0);" class="ycl-cli-btn fl" style="display:none"></a></div>';
					}
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				var mywindow;
				$("#content").html(tableBody);
				$("#content .ycl-cl-item i").click(function(event) {
				 var msgType=$(this).attr("msgType");
				 var oper=$(this).attr("oper_");
				 var isOnline=$(this).attr("_isOnline");
				 var userId = $(this).attr("_userId"),partyName=$(this).attr("_partyName");
				 if(msgType!=''&& msgType=='mpush'){
					 event.stopPropagation();//阻止将点击事件向上传递 
					 parent.sendToUser(userId,partyName,oper);  
					 	
				 }else{
					 if(isOnline!='0'){     
						 event.stopPropagation();//阻止将点击事件向上传递 
						 parent.transBuffer_userId(userId,partyName,oper)
					 } 
					 
				 }
				
		
				});
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content").html(tableBody);
			}
		});
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminCountData.json?t='+Math.random(), 
			data: postData,
			dataType:"json",
			success: function(data){
				
				//$.unblockUI();
				//设置页面页数
				$('#OnLinecount').text(data.OnLinecount);
				$('#NoLinecount').text(data.NoLinecount);
				
			},
			error:function(data){
			//$.unblockUI();
				
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
	function selected(id, obj){
        $('.selected').each(function (i, e) {
			$(e).removeClass("selected");
        });
        $(obj).addClass("selected");
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 390;
			opt.h = 300;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		window.parent.locationAtPoint(id, $('#elementsCollectionStr').val());
		
		if(window.parent.qnviccub != undefined) {
	  		$("#qnviccub", window.parent.document).remove();
	  	}
        var eclist = parent.analysisOfElementsCollectionList($('#elementsCollectionStr').val());
        var menuSummaryWidth = eclist["menuSummaryWidth"];
        var menuSummaryHeight = eclist["menuSummaryHeight"];
        if (menuSummaryWidth == "null") {
            menuSummaryWidth = 430;
        }
        if (menuSummaryHeight == "null") {
            menuSummaryHeight = 365;
        }

        var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+id;

        var winHeight = window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-200;

        var params = {
            title: "人员详情表",
            targetUrl: url,
            height: 380,
            width: 430,
            top: 32,
            left: 0,
            modal: false,
            collapsible: true,
            resizable: false
        }
		
		parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
		parent.showMaxJqueryWindowByParams(params);
	  	
	//	setTimeout(function() {
	//		if($('#elementsCollectionStr').val() != "") {
	//			window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),390,300,id);
	//		}else {
	//			window.parent.localtionGridAdminOrXldjPoint(id,'');
	//		}
	//	},1000);
	
	}
	
	//地图定位
	function gisPosition(res){
		if (typeof parent.MMApi != "undefined") {	// 判断是否是高德地图
			var opt = {};
			opt.w = 390;
			opt.h = 300;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			opt.url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res;
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
		if (res == null || res.length == 0){return ;}
		// for(var i=0;i<res.length;i++){
            if($('#elementsCollectionStr').val() != "") {
                var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res;
                window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
                window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),430,365);
            }else {
                var gridAdminurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res+"&showType=2";
                window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGridAdminOrXldj('"+gridAdminurl+"','')";
                window.parent.getArcgisDataOfGridAdminOrXldj(gridAdminurl,'');
            }
		// }

	}
	
	//--网格职务选择
	function select(obj){
		if($(obj).attr('class')!='current'){
			$(obj).addClass("current");
		}else{
			$(obj).removeClass("current");
		}
	}
	
	function selectOnly(obj) {
		if ($(obj).attr('class') != 'current') {
			$(obj).parent().children().removeClass("current");
			$(obj).addClass("current");
		}
	}
</script>
</body>
</html>