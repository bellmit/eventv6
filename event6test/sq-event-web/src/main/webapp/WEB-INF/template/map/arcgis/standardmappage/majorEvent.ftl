<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>
</head>
<body style="border:none;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
	<input type="hidden" id="orgCode" value="${orgCode}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="20" />
    <div class="" style="display:block;">
        <div class="ListSearch">
        	<div class="MetterCondition">
            	<ul>
                	<li><input name="keyWord" id="keyWord" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onblur="if(this.value=='')value='输入事件描述、标题、事发详址进行查询';" onfocus="if(this.value=='输入事件描述、标题、事发详址进行查询')value='';" value="输入事件描述、标题、事发详址进行查询" style="width:240px;" /></li>
                </ul>
                <ul class="time">
                	<li><input name="startHappenTime" id="startHappenTime" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='起始事发时间')value='';WdatePicker({isShowWeek:true});" value="起始事发时间" style="width:110px;" readonly="readonly" /></li>
                    <li style="width:20px;">-</li>
                	<li><input name="endHappenTime" id="endHappenTime" type="text" class="inp1" onkeydown="if (event.keyCode==13) {}" onfocus="if(this.value=='结束事发时间')value='';WdatePicker({isShowWeek:true});" value="结束事发时间" style="width:110px;" readonly="readonly" /></li>
                </ul>
                <#if eventType=='attention' || eventType=='all'>
                <ul id = "status" class="type">
                	<p>事件状态：</p>
                	<li onclick="selected(this)" id="do">处理中</li>
                	<li onclick="selected(this)" id="end">已归档</li>
                	<div class="clear"></div>
                </ul>
                </#if>
                <#if type??>
                <#else>
                <ul id="content-md2" class="type content light" style="height: 150px;">
                	<p>选择事件类型：</p>
                	<#if eventBigType??>
	    				<#list eventBigType as l>
	    					<li onclick="selected(this)" id="${l.dictGeneralCode}">${l.dictName}</li>
	    				</#list>
	    			</#if>
                    <div class="clear"></div>
                </ul>
                </#if>
                
                <div class="SearchBtn"><input name="" type="button" value="查询" class="NorBtn" onclick="search()" /></div>
                <div class="clear"></div>
            </div>
        	<div class="CloseBtn" onclick="CloseSearchBtn()"></div>
        </div>
        <div class="tushi2"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_03.png" />重大<img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_05.png" />紧急<img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_07.png" />超时<img src="${uiDomain!''}/images/map/gisv0/special_config/images/icon_09.png" />即将超时</div>
        
        <div class="showRecords">
        	<ul>
        		<li>共查询到<span id="records">0</span>条记录</li>
        	</ul>
        </div>
        <div class="ListShow2" id="content-md">
        </div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">

var inputNum;
var eventType = "${eventType}";
var handleDateFlag = "${handleDateFlag!''}";
var location_single_flag = "${location_single_flag!''}";
var modeType = "${modeType!''}";
window.parent.clearSpecialLayer('eventLayer');
window.parent.clearSpecialLayer('urgencyEventLayer');
function pageSubmit(){
	inputNum = $("#inputNum").val();
	var pageCount = $("#pageCount").text();
	if(isNaN(inputNum)){
		inputNum=1;
	}
	if(inputNum>pageCount){
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
	    //var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
	    winHeight=window.parent.document.getElementById('get_grid_name_frme').offsetHeight-77;
       	$("#content-md").height(winHeight); 
	    loadMessage(1,$("#pageSize").val());
	    parent.eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}", "${eventType}");
	    
	    
		$("#content-md2").mCustomScrollbar({theme:"minimal-dark"});
		$("#content-md").mCustomScrollbar({theme:"minimal-dark"});
	});
	var results="";//获取定位对象集合
	var urgencyResults="";//紧急事件定位对象集合
	var layerName="";
	function loadMessage(pageNo,pageSize,searchType){
		layerName = window.parent.analysisOfElementsCollection($('#elementsCollectionStr').val(),"menuLayerName");
		window.parent.currentLayerName = layerName;
		
		if('searchBtn'==searchType) {
			window.parent.clearSpecialLayer('eventLayer');
			window.parent.clearSpecialLayer('urgencyEventLayer');
			window.parent.currentListNumStr = "";
		}
		var keyWord = $('#keyWord').val();
		if(keyWord=="输入事件描述、标题、事发详址进行查询"){
			keyWord="";
		}
		var startHappenTime = $('#startHappenTime').val();
		if(startHappenTime=="起始事发时间"){
			startHappenTime="";
		}
		var endHappenTime = $('#endHappenTime').val();
		if(endHappenTime=="结束事发时间"){
			endHappenTime="";
		}
        var types = "${type!''}";
        $('#content-md2 li.current').each(function (index) {
            types = types + "," + $(this).attr('id');
        });
        //add by ztc ：去掉types第一个逗号
        types = $.trim(types);
        if (types.length > 0) {
            if (types.substr(0, 1) == ',') {
                types = types.substr(1);
            }
        }
		var status = "";
		$('#status li').each(function(index) {
			if($(this).attr('class')=='current'){
				if($(this).attr('id')=='do'){
					status = status + '00,01,02,03';
				}
				if($(this).attr('id')=='end'){
					if(status != ""){
						status = status + ',' + '04';
					}else{
						status = status + '04';
					}
				}
			}
		});
		//alert(types);
		results="";//一般事件标识集合（用于定位）
		urgencyResults="";//紧急事件标识集合（用于定位）
		var gridId = "";
		var infoOrgCode = "${infoOrgCode!''}";
		var handleStatuss = "<#if objectName??>${objectName}</#if>";
		
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
    	//var postData = 'eventStatus='+status+'&handleDateFlag='+handleDateFlag+'&infoOrgCode='+infoOrgCode+'&page='+pageNo+'&rows='+pageSize+'&eventType='+eventType+'&type='+types+'&keyWord='+keyWord+'&gridId='+gridId+"&happenTimeStart="+startHappenTime+"&happenTimeEnd="+endHappenTime+"&eventAttrTrigger=${eventAttrTrigger!}&typesForList=${typesForList!}";
		//var postData = 'eventType=${eventType}&page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&name='+name+'&order='+order+"&keyWord="+keyWord+"&startHappenTime="+startHappenTime+"&endHappenTime="+endHappenTime;
    	var postData = 'urgencyDegree=04,03,02&eventStatus='+status+'&infoOrgCode='+infoOrgCode+'&page='+pageNo+'&rows='+pageSize+'&eventType='+eventType+'&type='+types+'&keyWord='+keyWord+'&gridId='+gridId+"&happenTimeStart="+startHappenTime+"&happenTimeEnd="+endHappenTime+"&eventAttrTrigger=${eventAttrTrigger!}&typesForList=${typesForList!}";
    	var url='${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json?t='+Math.random();
    	var modeType='${modeType}';
    	if(modeType=='jurisdiction'){
    		url='${rc.getContextPath()}/zhsq/map/nanpingVideoDataController/findPagListByUrgencyDegree.json?t='+Math.random();
    		postData = 'orgCode='+infoOrgCode+'&urgencyDegrees=04,03,02&page='+pageNo+'&rows='+pageSize+'&keyWord='+keyWord+'&gridId='+gridId+"&happenTimeStart="+startHappenTime+"&happenTimeEnd="+endHappenTime
    	}
    	$.ajax({
			type: "POST",
			url:url,
			data: postData,
			dataType:"json",
			success: function(data){
				$('#records').text(data.total);
				$.unblockUI();
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				parent.columnListSet(list);
				var tableBody="";
				tableBody+="<div class='liebiao'>";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					    var val=list[i];
					    var eId = val.eventId+","+val.workFlowId+","+val.instanceId+","+val.taskId+","+eventType;
				        tableBody+='<dl onclick="selected('+val.eventId+')" style="position: relative;">';
					    var eventClass = val.eventClass;
					    var eventName = val.eventName;
					    if(eventName!=null){
					    	if(eventName.length > 10){
					    		eventName = eventName.substring(0,10) + '...';
					    	}
					    }
						if(eventClass!=null){
							eventClass = val.eventClass.substring(val.eventClass.indexOf("-")+1,val.eventClass.length);
						}
					    var content = val.content;
					    var contentAll='';
						if(content!=null && content!="" && content.length>12){
							contentAll=content;
							content = content.substring(0,22)+"...";
						}else{
							content = content;
						}
						var img = "";
					    var happenTimeStr = (val.happenTimeStr==null)?"无发生日期":val.happenTimeStr.substr(0,10);

						if(val.remindStatus=='2' || val.remindStatus=='3'){
							tableBody+='<div class="duban"></div>';
						}
					    
					    tableBody+='<dt><span class="fr">'+happenTimeStr+'</span>';
						
					    if(val.urgencyDegree!="01"){
					    	tableBody+='<img src="${rc.getContextPath()}/images/icon_05.png" />';
					    	urgencyResults=urgencyResults+","+val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!"+eventType;
					    }else{
					    	results=results+","+val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!"+eventType;
					    }
					    if(val.handleDateFlag == '2'){
					    	tableBody+='<img title="将到期" src="${rc.getContextPath()}/images/icon_09.png">';
						}
						if(val.handleDateFlag == '3'){
							tableBody+='<img title="已过期" src="${rc.getContextPath()}/images/icon_07.png">';
						}
					    if(val.remindStatus=="0"){
					    	tableBody+='<img src="${rc.getContextPath()}/images/icon_12.png" />';
					    }
					    if(val.influenceDegree=="04"){
					    	tableBody+='<img src="${rc.getContextPath()}/images/icon_03.png" />';
					    }
					    tableBody+='<b class="FontDarkBlue" title="'+val.eventName+'">'+eventName+'</b></dt>';
						tableBody+='<dd title="'+contentAll+'">'+content+'</dd>';
						
					    tableBody+='</dl>';
					}
					results=results.substring(1, results.length);
					urgencyResults=urgencyResults.substring(1, urgencyResults.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				tableBody+="</div>";
				try{
					if (modeType != 'fuzhou') {
						gisPosition(urgencyResults);
					}
				}catch(e){
					
				}
				
				$("#content-md .mCSB_container").html(tableBody);
				$("#content-md").mCustomScrollbar("update");
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content-md").html(tableBody);
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
			opt.w = 475;
			opt.h = 406;
			opt.ecs = $('#elementsCollectionStr').val();
			return parent.MMApi.clickOverlayById(id, opt);
		}
		

		setTimeout(function() {
                if ($('#elementsCollectionStr').val() != "") {
					//window.parent.getDetailOnMapOfListClick( $('#elementsCollectionStr').val(),null,null, id);
                    window.parent.getDetailOnMapOfListClick($('#elementsCollectionStr').val(),null,null,id)
                
                } else {
                    window.parent.getDetailOnMapOfListClick(id);
                }
           
		},1000);
	}
	

	//--定位
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
		if (res == null || res.length == 0){return ;}
		// for(var i=0;i<res.length;i++){
            if($('#elementsCollectionStr').val() != "") {
                var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataList.jhtml?ids="+res;
                window.parent.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";   
                window.parent.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),490,270,30,30);
            }else { 
                var gridAdminurl="${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataList.jhtml?ids="+res+"&showType=2";
                window.parent.currentLayerLocateFunctionStr="getArcgisDataOfGridAdminOrXldj('"+gridAdminurl+"','')";
                window.parent.getArcgisDataOfGridAdminOrXldj(gridAdminurl,'');
            }
	}
	
	//--查询
	function search(){
		loadMessage(1,$('#pageSize').val(),'searchBtn');
	}
</script>
</body>
</html>