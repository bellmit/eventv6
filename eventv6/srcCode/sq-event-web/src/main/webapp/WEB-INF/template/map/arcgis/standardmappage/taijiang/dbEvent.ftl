<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>代办事件</title>
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/editMessageStyle.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/grid_pc.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/houseLayer.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/index.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/layer_fillet.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/nav_hover.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/pop.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/xm_map_right.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/xmwz.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/frame.css" rel="stylesheet" type="text/css" />
<link href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/public.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/function.js"></script>
</head>
<body style="border:none;">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="gridFlag" value="${gridFlag}" />
	 
    <div class="SecMenu" style="display:block;">
			<div class="con">
            	<div class="SelectKind">
                	<p><input name="content"  id="content" type="text" class="inp1" value="==事件描述=="  onfocus="if (this.value=='==事件描述==') {this.value=''}" onblur="if(this.value==''){this.value='==事件描述=='}" /><input name="search" type="button" class="btn1" onclick="loadMessage(1,10);"/></p>
				    <div class="AdvanceSearch" style="display:none;">
                    	<ul>
                        	<li>事件类型：<select id="bigType" name="bigType" class="easyui-combobox" editable="false" data-options="panelHeight:null,width:120" >
					    			<option value="">==请选择==</option>
					    			<#if bigTypeDC??>
					    				<#list bigTypeDC as l>
					    					<option value="${l.COLUMN_VALUE}">${l.COLUMN_VALUE_REMARK}</option>
					    				</#list>
					    			</#if>
					    	    </select>
							</li>
                        </ul>
                    </div>
				</div>
                 <div class="list2"  id="eventContent" style="overflow:auto;">
				</div>
                <div class="page">
                	<div class="fl"><select name="pageSize"  id="pageSize" onchange="change('3');"><option value="5">每页 5 条</option><option value="10" selected="selected">每页 10 条</option><option value="50">每页 50 条</option></select></div>
                    <div class="fr"><a href="javascript:change('1');">上一页</a> <a href="javascript:change('2');">下一页</a> 共<span id="pagination-num">1</span>/<span id="pageCount">4</span>页</div>
                </div>
            </div>
	 </div>			
	
<script type="text/javascript">
	$(document).ready(function(){
	    var winHeight=window.document.body.clientHeight;
        $("#eventContent").css("height",winHeight-70); 
		loadMessage(1,10);
	});
	var eventType = 'todo';
	function loadMessage(pageNo,pageSize){
		var results="";//一般事件标识集合（用于定位）
		var urgencyResults="";//紧急事件标识集合（用于定位）
		var gridId = $('#gridId').val();
		var gridFlag=$('#gridFlag').val();
		if(gridFlag == '0'){
			eventType == 'todo';
		}else if(gridFlag = '1'){
			eventType = 'all';
		}
		console.log(gridFlag);
		console.log(eventType);
		var content=$('#content').val();
		if(content=="==事件描述==") content="";
		$.blockUI({message: "加载中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',left:'20%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		var postData = 'page='+pageNo+'&rows='+pageSize+'&gridId='+gridId+'&content='+content+'&bigType='+bigType+'&gridFlag='+gridFlag+'&statusName=innerPlatform';
		
		$.ajax({
			type: "POST",
			url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/dbEventListData.json',
			data: postData,
			dataType:"json",
			success: function(data){
				$.unblockUI();
				
				//设置页面页数
				$('#pagination-num').text(pageNo);
				var totalPage = Math.floor(data.total/pageSize);
				if(data.total%pageSize>0) totalPage+=1;
				$('#pageCount').text(totalPage);
				var list=data.rows;
				var tableBody="";
				if(list && list.length>0) {
					for(var i=0;i<list.length;i++){
					  var val=list[i];
					  var eId = val.eventId+","+val.workFlowId+","+val.instanceId+","+val.taskId+","+eventType;
					  var eventClass = val.eventClass;
						if(eventClass!=null){
							//eventClass = val.eventClass.substring(val.eventClass.indexOf("-")+1,val.eventClass.length);
						}
						var content = val.content;
						if(content==null || content==""){
							content = "";
						}else if(content.length > 12){
							content = content.substring(0,12)+"...";
						}
						if(val.handleDateStatus==1){
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限正常" src="${rc.getContextPath()}/images/gis/nochange/time_normal.png" />';
						}else if(val.handleDateStatus==2){
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限将到期" src="${rc.getContextPath()}/images/gis/nochange/time_to_expire.png" />';
						}else{
							var img = '&nbsp;&nbsp;<img width="15" height="15" title="处理时限已到期" src="${rc.getContextPath()}/images/gis/nochange/time_expired.png" />';
						}
						
					    if((i+1)%2==0){//基数样式
					       tableBody+='<ul  class="back"><li class="ListIcon">';
					    }else{
					       tableBody+='<ul><li class="ListIcon">';
					    }
					   if(val.urgencyDegree=="01"){
					   		if(val.handleDateStatus!=1&&val.handleDateStatus!=2) {
					   			//tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_01shanshuo.gif"/></li>';
					   			tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_green.gif"/></li>';
					   		}else {
					   			//tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_01.png"/></li>';
					   			tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_green.gif"/></li>';
					   		}
					   		results=results+","+val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!"+eventType;
					   }else{
					      //tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_taijiang/images/event_02.gif"/></li>';
					      tableBody+='<img src="${uiDomain!''}/images/map/gisv0/map_config/unselected/event/event_red.gif"/></li>';
					      urgencyResults=urgencyResults+","+val.eventId+"!"+val.workFlowId+"!"+val.instanceId+"!"+val.taskId+"!"+eventType;
					}
					   tableBody+='<li class="ListInfo" onclick="showDetailRow(this,\''+eId+'\',\''+val.instanceId+'\',\''+val.workFlowId+'\',\''+val.type+'\',\''+val.urgencyDegree+'\')">';
					   tableBody+='<p>事件分类：'+(eventClass==null?'':eventClass)+'</p>';
					   tableBody+='<p>发生时间：'+(val.happenTimeStr==null?'':val.happenTimeStr)+'</p>';
					   tableBody+='<p>事件描述：'+(content==null?'':content)+'</p>';
					   tableBody+='</li></ul>';
					}
					results=results.substring(1, results.length);
					urgencyResults=urgencyResults.substring(1, urgencyResults.length);
				} else {
					tableBody+='<div style="text-align: center;"><img style="width: 174px;" src="${uiDomain!''}/images/map/gisv0/special_config/images/nodata.png" title="暂无数据"/></div>';
				}
				$("#eventContent").html(tableBody);
				$(".AdvanceSearch").css("display","none");
				gisPosition(results);
				urgencyGisPosition(urgencyResults);
			},
			error:function(data){
				$.unblockUI();
				var tableBody='<ul>数据读取错误！！！</ul>';
				$("#eventContent").html(tableBody);
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
	
	
	function showDetailRow(obj,eventId,instanceId,workFlowId,type, urgencyDegree){
// 		gisPosition(tid);
		window.parent.localtionEventPoint(eventType,eventId,instanceId,workFlowId,type,urgencyDegree);
// 		setTimeout(function() {
// 			window.parent.localtionDbEventPoint(tid,eventId);
// 		},1000);
	
		//window.parent.localtionDbEventPoint(tid,eventId);
	}
	
	//--定位
	function gisPosition(res){console.log(res);
	window.parent.clearMyLayer();
		if(res==""){
			return;
		}
		//console.info("ids -> "+ids);
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2";
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfEvent('"+url+"','"+res+"')";
		window.parent.getArcgisDataOfEvent(url,res);
	}

	//--定位
	function urgencyGisPosition(res){
		if(res==""){
			return;
		}
		var ids = "";
		//console.info("ids -> "+ids);
		
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofeventlocal/getArcgisLocateDataListOfEvent.jhtml?ids="+res+"&showType=2";
		window.parent.getArcgisDataOfUrgencyEvent(url,res);
	}
	//地图定位
// 	function gisPosition(res){
// 	window.parent.clearMyLayer();
// 		if(res==""){
// 			return;
// 		}
		
// 		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController/getArcgisImportantEventLocateDataList.jhtml?ids="+res+"&showType=2";
// 		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfDbEvent('"+url+"')";
// 		window.parent.getArcgisDataOfDbEvent(url);
// 	}
</script>
</body>
</html>