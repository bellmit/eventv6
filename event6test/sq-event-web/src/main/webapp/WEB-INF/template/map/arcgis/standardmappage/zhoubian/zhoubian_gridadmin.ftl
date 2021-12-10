<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员列表</title>


<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body style="border:none">
	<input type="hidden" id="pageSize" value="20" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
    <div class="" style="display:block;">
    	<!--<div class="title"><span class="fr" onclick="CloseX()"><img src="${rc.getContextPath()}/theme/standardmappage/images/closex.png" /></span><span class="fr" onclick="SearchBtn()"><img src="${rc.getContextPath()}/theme/standardmappage/images/search.png" /></span><a href="#" onclick="firstall()">专题图层</a> > <a href="#" onclick="people()">人</a> > 党员</div>-->
        <div class="ListSearch">
        	<div class="MetterCondition">
            	<ul class="content" style="height:26px;">
                	<li class="LC1" style="float:left; line-height:26px; width:36px;">姓名：</li>
                	<li class="LC2" style="float:left; line-height:26px;"><input id="name" name="name" type="text" class="inp1" /></li>
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
	/*默认选中网格员*/
	(function($){
		
	})(jQuery);
	
	
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
		var name = $('#name').val();
		var pageSize = $("#pageSize").val();
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	
    	var dutys = "";
    	var postData;
    	
    	$('.type li').each(function(index) {
			if($(this).attr('class')=='current'){
				dutys = $(this).attr('value') + "," + dutys; 
			}
		});
		
		if (dutys!=null && dutys!="" && dutys.length > 0) {
			dutys = dutys.substring(0, dutys.length - 1);
			//postData = 'page='+pageNo+'&rows='+pageSize+'&name='+name+'&duty='+dutys;
		} else {
			//postData = 'page='+pageNo+'&rows='+pageSize+'&name='+name;
		}
		$.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/queryZhouBianList.json?t='+Math.random(), 
			data : {
				pageNo : pageNo,
				pageSize : $("#pageSize").val(),
				name : $('#name').val(),
				duty : dutys,
				mapType : "${mapType}",
				distance : "${distance}",
				x : "${x}",
				y : "${y}",
				zhoubianType : "${zhoubianType}"
			},
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
					tableBody+='<ul>';
					for(var i=0;i<list.length;i++){
					  var val=list[i],
					  	  mobileTelephone = val.MOBILE_TELEPHONE || '',
					  	  partyName = val.PARTY_NAME || '',
					  	  userId = val.USER_ID || '';
					  
					  tableBody+='<dl onclick="selected(\''+val.GRID_ADMIN_ID+'\')">';
					  tableBody+='<dt>';
					  tableBody+='<span class="fr">' + (val.MOBILE_TELEPHONE==null?'':('<img id="_sendMsgImg" src="${uiDomain!}/images/text_msg.png" title="发送信息" _gridAdminId="'+ val.GRID_ADMIN_ID +'" _userId="'+ userId +'" _mobileTelephone="'+ mobileTelephone +'" _partyName="'+ partyName +'" />&nbsp;'+mobileTelephone))+'</span>';
					  
					  tableBody += '<b class="FontDarkBlue" ';
					  if(partyName && partyName.length > 5) {
					  	tableBody += ' title="'+ partyName +'" ';
					  	partyName = partyName.substring(0, 5) + "...";
					  }
					  tableBody += ' >'+ partyName +'</b>' + (val.DUTY_LABEL==null?'':'<label>[' + val.DUTY_LABEL + ']</label>');
					  
					  tableBody+='</dt>';
					  tableBody+='<dd><span class="fr">距离：'+val.DISTANCE+'米</span>'+(val.GRID_NAME==null?'':val.GRID_NAME)+'</dd>';
					  tableBody+='</dl>';
					  			
					  results=results+","+val.GRID_ADMIN_ID;
					}
			        tableBody+='</ul>';
					results=results.substring(1, results.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
					//tableBody+='<ul>未查到相关数据！！</ul>';
				}
		        tableBody+='</div>';
				$("#content").html(tableBody);
				
				$("#content img[id='_sendMsgImg']").click(function(event) {
					var userId = $(this).attr("_userId"),
						url = '${rc.getContextPath()}/zhsq/map/zhoubian/zhouBianStat/toSendMsg.jhtml?gridAdminId='+ $(this).attr('_gridAdminId') +"&partyName="+$(this).attr("_partyName")+"&mobileTelephone="+$(this).attr("_mobileTelephone")+'&bizType=01';
						params = {};
					
					if(userId) {
						url += '&userId='+userId;
					}
					
					params = {
						title: "发送信息",
						targetUrl: url,
						width: 850,
						height: 400,
						resizable: true
					};
					
					parent.showMaxJqueryWindowByParams(params);
					
					event.stopPropagation();//阻止将点击事件向上传递
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
			opt.w = 390;
			opt.h = 300;
			opt.ecs = $('#elementsCollectionStr').val();
			opt.gridId = $('#gridId').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		
		if(window.parent.qnviccub != undefined) {
	  		$("#qnviccub", window.parent.document).remove();
	  	}
	  	
	  	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/gridAdminDetail.jhtml?gridAdminId='+id;

		var winHeight = window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-200;
		
		var params = {
			title: "人员详情表",
			targetUrl: url,
			height: winHeight,
			width: 385,
			top: 32,
			left: 0,
			modal: false,
			collapsible: true,
			resizable: false
		}
		
		parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
		parent.showMaxJqueryWindowByParams(params);
	
	}
	
	//地图定位
	function gisPosition(res){
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
			window.parent.clearGridAdminLayer();
			//return ;
		}
	  	var gridAdminurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisLocateDataListOfGridAdmin.jhtml?ids="+res+"&showType=2";
	  	window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGridAdminOrXldj('"+gridAdminurl+"','')";
		window.parent.getArcgisDataOfGridAdminOrXldj(gridAdminurl,'');
	}
	
	//--网格职务选择
	function select(obj){
		if($(obj).attr('class')!='current'){
			$(obj).addClass("current");
		}else{
			$(obj).removeClass("current");
		}
	}
</script>
</body>
</html>