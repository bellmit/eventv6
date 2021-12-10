<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>历史事件</title>
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/images/map/gisv0/map_taijiang/css/editMessageStyle.css"/>

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui-last/themes/icon.css">
<!--<script src="${rc.getContextPath()}/theme/scim/scripts/tablestyle.js"></script>-->
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<style type="text/css">
*{margin:0; padding:0; list-style:none;}
.FontDarkBlue{color:#36c;}
.FontRed{color:#e60012;}
.FontOrange{color:#f39800;}
.fr{float:right;}
.BigEventList dl{height:40px; cursor:pointer; padding:10px; border-bottom:1px solid #d2d2d2; color:#434343; font-size:14px; line-height:14px; font-family:microsoft yahei;}
.BigEventList dl:hover{background:#F7F7F7;}
.BigEventList dt{padding-bottom:10px;}
.BigEventList dt .fr{font-size:12px;}
</style>
</head>
<body style="border:none;">
	<div id="importantEvent" class="BigEventList">
	</div>
</body>	
<script type="text/javascript">
var loadFlag = false;
function re() {
	return loadFlag;
}
$(function(){
	showPointEvent();
	setInterval(function(){showPointEvent();},60000);
});
function showPointEvent(){
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/importantEvent.json?statusName=innerPlatform&gridId=${gridId}&type=jjya';
    $.ajax({   
		 url: url,
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 error: function(data){  
		   $.messager.alert('友情提示','数据(事件列表)读取失败,详情请查看后台日志!','warning'); 
		 },   
		 success: function(data){  
		      var result=data.result;
			      if(result=='yes'){
			        var val=eval(data.eventList);
			        var valYinji = eval(data.yinjiEventList);
			        var results="";//一般事件标识集合（用于定位）
					var urgencyResults="";//紧急事件标识集合（用于定位）
			        var tableBodyStart='<table style="width:100%;" cellpadding="0" cellspacing="1" border="0" class="searchList-2" ><tr style="line-height:25px;"><th style="text-align:center">&nbsp;事件类型</th><th style="text-align:center">&nbsp;发生时间</th><th style="text-align:center">&nbsp;事件内容</th></tr>';
			        var tableBody = "";
			        var content="";
			        var con="";
			        if(data.show=='yes'){
				        if(valYinji.length>0) {
				        tableBody+='';//<tr><td colspan="3"><font>重大应急事件</font></td></tr>
				        	for(var i=0;i<valYinji.length;i++){
					            content=valYinji[i].content;
					            con=valYinji[i].content;
					            if(content==null || content==""){
									content = "";
									con = "";
								}else if(content.length > 12){
									content = content.substring(0,10)+"...";
								}
					            var eId = valYinji[i].eventId+","+valYinji[i].workFlowId+","+valYinji[i].instanceId+","+valYinji[i].taskId+",todo";
					            tableBody+='<dl>';
					            tableBody+='<dt><span class="fr">'+valYinji[i].happenTimeStr.substring(0,11)+'</span><span class="FontRed">[重大应急]</span><span class="FontDarkBlue">['+valYinji[i].eventClass.substring(5)+']</span></dt>';
					            tableBody+='<dd onclick="showDetailRow(\''+eId+'\',\''+valYinji[i].instanceId+'\',\''+valYinji[i].workFlowId+'\',\''+valYinji[i].type+'\',\''+valYinji[i].urgencyDegree+'\')">'+content+'</dd></dl>';
					            if(valYinji[i].urgencyDegree=="01"){
					            	results=results+","+valYinji[i].eventId+"!"+valYinji[i].workFlowId+"!"+valYinji[i].instanceId+"!"+valYinji[i].taskId+"!todo";
					            }else{
					            	urgencyResults=urgencyResults+","+valYinji[i].eventId+"!"+valYinji[i].workFlowId+"!"+valYinji[i].instanceId+"!"+valYinji[i].taskId+"!todo";
					            }
				       		 }
				        }
			        }
			        tableBody1 = "";
			        if(val.length>0){//表示有重大事件
			        	tableBody1+='';//<tr><td colspan="3"><font >重大或紧急事件</font></td></tr>
			             for(var i=0;i<val.length;i++){
				            content=val[i].content;
				            con=val[i].content;
				            if(content==null || content==""){
								content = "";
								con = "";
							}else if(content.length > 12){
								content = content.substring(0,10)+"...";
							}
					        var eId = val[i].eventId+","+val[i].workFlowId+","+val[i].instanceId+","+val[i].taskId+",todo";
// 				            tableBody1+='<tr><td style="width:30%">'+'<span title=\''+val[i].eventClass+'\'>'+val[i].eventClass.substring(5)+'</td><td  style="width:30%">'+val[i].happenTimeStr.substring(0,11)+'</td><td  style="width:40%"><a href="javascript:showDetailRow(\''+eId+'\',\''+val[i].instanceId+'\',\''+val[i].workFlowId+'\',\''+val[i].type+'\',\''+val[i].urgencyDegree+'\')" title="'+con+'">'+content+'</a></td></tr>';
				            tableBody1+='<dl>';
				            tableBody1+='<dt><span class="fr">'+val[i].happenTimeStr.substring(0,11)+'</span><span class="FontOrange">[重大或紧急]</span><span class="FontDarkBlue">['+val[i].eventClass.substring(5)+']</span></dt>';
				            tableBody1+='<dd onclick="showDetailRow(\''+eId+'\',\''+val[i].instanceId+'\',\''+val[i].workFlowId+'\',\''+val[i].type+'\',\''+val[i].urgencyDegree+'\')">'+content+'</dd></dl>';
				            
				            // 				            eventIds=eventIds+","+val[i].eventId+"_"+(val[i].taskInfoId==null?'':val[i].taskInfoId);
				            //eventIds=eventIds+","+val[i].eventId;
				            if(val[i].urgencyDegree=="01"){
				            	results=results+","+val[i].eventId+"!"+val[i].workFlowId+"!"+val[i].instanceId+"!"+val[i].taskId+"!todo";
				            }else{
				            	urgencyResults=urgencyResults+","+val[i].eventId+"!"+val[i].workFlowId+"!"+val[i].instanceId+"!"+val[i].taskId+"!todo";
				            }
			       		 }
			             results=results.substring(1, results.length);
						 urgencyResults=urgencyResults.substring(1, urgencyResults.length);
			        }
			        tableBodyEnd='</table>';
			        
			        
			        // 地图定位
			        gisPosition(results);
					urgencyGisPosition(urgencyResults);
			        if(loadFlag == true){
			        	
			        }else {
			        	if(val.length+valYinji.length>0){
							window.parent.showNoticewin();
							loadFlag = true;
			        	}
			        }
			        $("#importantEvent").html(tableBody+tableBody1);
			        // 有数据则弹出框
			        
		      }
		 }
   	  });
	  
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
		window.parent.currentLayerLocateFunctionStr="getArcgisDataOfUrgencyEvent('"+url+"','"+res+"')";
		window.parent.getArcgisDataOfUrgencyEvent(url,res);
	}
}

function showDetailRow(eventId,instanceId,workFlowId,type, urgencyDegree){
	var eventType = 'todo';
	console.log(eventId,instanceId,workFlowId,type, urgencyDegree);
	window.parent.localtionEventPoint(eventType,eventId,instanceId,workFlowId,type,urgencyDegree);
}
</script>

</html>