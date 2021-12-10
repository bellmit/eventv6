<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="eventListPageTitle">事件列表</@block></title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<!--插件如语音盒 使用js-->
<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
<script>
	var heightbox = 0, widthbox = 0;
	(function($){//高度和宽度获取需要放置在头部，否则获取值会有问题
	    //事件分栏视图左侧列表高度，及右侧详情宽度
		heightbox=$(window).height();
		widthbox=$(window).width();
	})(jQuery);
	
	
</script>
</head>
<body class="easyui-layout" id="layoutArea">
	<@block name="eventToolbar">
		<#include "/zzgl/event/eventDataGridToolbarForColumn.ftl" />
	</@block>
	
    <div class="MetterList">
    	<div class="ColumnView fl">
            <div id="content-md" class="con content light">
        		
	        </div>
	        <div class="NorPage">
	        <input type="hidden" id="pageSize" value="20" />
	        	<ul>
	            	<li class="PreBtn"><a href="javascript:change('1');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/pre.png" /></a></li>
	            	<li class="yema">共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页 转至 </li>
	                <li class="PageInp"><input id="inputNum" name="inputNum" type="text" onkeydown="_onkeydown();" /></li>
	                <li class="PageBtn"><input type="button" value="确定" onclick="pageSubmit()"/></li>
	            	<li class="NextBtn"><a href="javascript:change('2');"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/next.png" /></a></li>
	            </ul>
	        </div>
        </div>
        <div class="line fl"></div>
        <div class="box fl" style="overflow:hidden;" id="detail">
        	<iframe id="eventDetail" frameborder='0' src='' style='width:100%;height:100%;' scrolling="no"></iframe>
        </div>
        <div class="clear"></div>
    </div>
<#include "/zzgl/event/singleComboboxSelect.ftl">
<#include "/component/customEasyWin.ftl" />

<script type="text/javascript">
	var inputNum;
	var results="";//获取定位对象集合
	var startGridId = "${startGridId?c}";
	var idStr = "";
	var list = null;
	var _instanceId = "";
	var _bizPlatform = "";
	var pageNum = 1;
	function searchData(b, searchArray) {
		loadMessage(1,$('#pageSize').val(), searchArray);
	}

	function flashData(){
		searchData();
	}
	
	function loadMessage(pageNo,pageSize, searchArray){
		idStr = "";
		modleopen();
		pageNum = pageNo;
		if(pageNo == "1") {
			$("#inputNum").val("");
		} else if($("#inputNum").val() != "") {
			$("#inputNum").val(pageNo);
		}
		 <#if extraParams??>
	    	<#if extraParams!="">
	    		var extraParams = ${extraParams!''};
				for(var index in extraParams) {
					if($('#' + index).length == 0) {
						$('#eventQueryForm').append('<input type="hidden" id="'+index+'" name="'+index+'" value ="'+extraParams[index]+'" class="queryParam"/>');
					}
				}
				
				if(typeof adjustListDisplayElement === 'function') {
					adjustListDisplayElement(extraParams);
				}
			</#if>
		</#if> 
		postData = queryData(searchArray);
		postData["page"] = pageNo;
		postData["rows"] = pageSize;
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/listData.json?t='+Math.random(),
			data: postData,
			dataType:"json",
			success: function(data){
				//设置页面页数
				$('#pagination-num').text(pageNo);
				$('#records').text(data.total);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				list=data.rows;
				columnListSet(list);
				var tableBody="";
				tableBody+='';
				if(list && list.length>0) {
					$("#detail").removeClass("nodata");
					addIcons();//显示图示
					for(var i = 0, len = list.length; i < len; i++){
					    var val=list[i];
					    if(i == 0){
					    	tableBody+='<dl  class="cik" onclick="selected(this,\''+val.eventId+'\',\''+val.instanceId+'\',\''+val.workFlowId+'\',\''+val.bizPlatform+'\',\''+val.type+'\')">';
					    }else{
				        	tableBody+='<dl  onclick="selected(this,\''+val.eventId+'\',\''+val.instanceId+'\',\''+val.workFlowId+'\',\''+val.bizPlatform+'\',\''+val.type+'\')">';
				        }
				        
				        tableBody += '<input type="hidden" id="'+ val.eventId + '_status' +'" value="'+ val.status +'" />';
				        
					    var eventClass = val.eventClass;
						if(eventClass!=null){
							eventClass = val.eventClass.substring(val.eventClass.indexOf("-")+1,val.eventClass.length);
						}
						var eventName = val.eventName;
						if(eventName!=null && eventName.length>=8){
							eventName = eventName.substring(0,8)+"...";
						}
						if(val.influenceDegree == '04'){
							eventName = "<b class='FontRed'>[重大]</b>" + eventName;
						}
					    var content = val.content;
						if(content!=null && content!="" && content.length>=12){
							content = content.substring(0,12)+"...";
						}else{
							content = content;
						}
						
						var img = "";
					    var happenTimeStr = (val.happenTimeStr==null)?"无发生日期":val.happenTimeStr.substr(0,10);
					    var createDateStr = (val.createDateStr==null)?"无采集日期":val.createDateStr.substr(0,10);
						tableBody += '<dt><span class="fr">'+happenTimeStr+'</span>';
						
						tableBody+='<b>'+eventName+'</b>';
					    if(val.urgencyDegreeName!=null && val.urgencyDegree!="01"){
					    	tableBody += '<img title="'+ val.urgencyDegreeName +'" src="${rc.getContextPath()}/images/icon_05.png" />';
					    }
					    <#if eventType?? && eventType=="todo">
					    	if(val.remindStatus=='1' || val.remindStatus=='3'){
								tableBody += '<img title="催办" src="${rc.getContextPath()}/images/icon_14.png">';
							}
						</#if>
					    if(val.handleDateFlag == '2'){
					    	tableBody += '<img title="将到期" src="${rc.getContextPath()}/images/icon_09.png">';
					    }else if(val.handleDateFlag == '3'){
					    	tableBody += '<img title="已过期" src="${rc.getContextPath()}/images/icon_07.png">';
					    }
					    <#if false && (eventType=='history' || eventType=="all") >
							var attrFlag = val.attrFlag;
							if(attrFlag.indexOf('1,') != -1){
								tableBody += '<img title="图片" src="${rc.getContextPath()}/images/attr_flag_pic.png">';
							}
							if(attrFlag.indexOf('2,') != -1){
								tableBody += '<img title="音频" src="${rc.getContextPath()}/images/attr_flag_audio.png">';
							}
							if(attrFlag.indexOf('3,') != -1){
								tableBody += '<img title="视频" src="${rc.getContextPath()}/images/attr_flag_video.png">';
							}
						</#if>
					    tableBody+='</dt>';
						tableBody+='<dd>'+content+'</dd>';
						
					    tableBody+='</dl>';
					    
						results=results+","+val.eventId+"_"+(val.taskInfoId==null?'':val.taskInfoId);
					}
					results=results.substring(1, results.length);
					var obj = $("#content-md dl:first-child");
					selected(obj,list[0].eventId,list[0].instanceId,list[0].workFlowId,list[0].bizPlatform,list[0].type);
					$("#eventDetail").show();
				} else {
					tableBody+='<dl>未查到相关数据！！</dl>';
					$("#eventDetail").attr("src","");
					$("#eventDetail").hide();
					$("#detail").addClass("nodata");
					modleclose();
				}
				
				$("#content-md .mCSB_container").html(tableBody);
				$("#content-md .mCSB_container dl").width(250);//显示设置width样式，为了防止菜单页签切换时，宽度被修改
				$("#content-md").mCustomScrollbar("update");
				
				<@block name="listLoadSuccessExtraOperate"></@block>
			},
			error:function(data){
				var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
				$("#content-md").html(tableBody);
			}
		});
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
		  $.messager.alert('提示', "当前已经是首页", 'info');
		  return;
		}else if(flag==2){
		  $.messager.alert('提示', "当前已经是尾页", 'info');
		  return;
		}
	    loadMessage(pagenum,pageSize);
	}
	
	function pageSubmit(){
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		//防止文本比较，出现3>28的情况，需要转换为数字
		if(inputNum!=null && inputNum!=""){
			inputNum = parseInt(inputNum);
		}
		if(pageCount!=null && pageCount!=""){
			pageCount = parseInt(pageCount);
		}
		if(inputNum>pageCount){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		change('4');
	}

	function selected(obj,eventId,instanceId,workFlowId,bizPlatform,type) {
		var url = "";
		idStr = eventId;
		_instanceId = instanceId;
		_bizPlatform = bizPlatform;
		
		$(".con dl").removeClass("cik");
		$(obj).addClass("cik");
		
		modleopen();
		
		if(bizPlatform == "001"){//旧事件
			url = '${SQ_ZZGRID_URL}/zzgl/event/innerPlatform/detail.jhtml?eventId='+eventId;
		}else{
			url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=${eventType}&instanceId="+instanceId+"&workFlowId="+workFlowId+"&eventId="+eventId+"&model=${model}&cachenum=" + Math.random();
		}
		
		$("#eventDetail").attr("src",url);
		$("#eventDetail").load(function(){
			btnAuthority();//列表按钮展示权限设置
			modleclose();
		});
	}
	
	function linkMenu(eventType){
		var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/listEvent.jhtml?t="+eventType+"&model=c";
		window.location.href=url;
	}
	
	function _onkeydown(){
		var keyCode = event.keyCode;
		if(keyCode == 13){
			pageSubmit();
		}
	}
</script>

<script>
	$(function() {
		var options = { 
			axis : "yx", 
			theme : "minimal-dark" 
		}; 
		enableScrollBar('content-md',options); 
		
		eventInit("${rc.getContextPath()}", "");
		
		//事件分栏视图左侧列表高度，及右侧详情宽度
		$(".ColumnView .con").css("height",heightbox-115);
		$(".MetterList .box").css("height",heightbox-86);
		$(".MetterList .line").css("height",heightbox-86);
		
		$(".MetterList .box").css("width",widthbox-266);
		
		searchData();
	});
	
</script>
</body>
</html>