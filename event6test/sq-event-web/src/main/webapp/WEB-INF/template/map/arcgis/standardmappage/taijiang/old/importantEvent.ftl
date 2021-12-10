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

</head>
<body style="border:none;">
	<div id="importantEvent" >
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
    $.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofregion/old/importantEvent.json?statusName=innerPlatform&gridId=${gridId}&type=jjya',   
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
			        var eventIds="";
			        var tableBodyStart='<table style="width:100%;" cellpadding="0" cellspacing="1" border="0" class="searchList-2" ><tr style="line-height:25px;"><th style="text-align:center">&nbsp;事件类型</th><th style="text-align:center">&nbsp;发生时间</th><th style="text-align:center">&nbsp;事件内容</th></tr>';
			        var tableBody = "";
			        var content="";
			        var con="";
			        if(data.show=='yes'){
				        if(valYinji.length>0) {
				        tableBody+='<tr><td colspan="3"><font>重大应急事件</font></td></tr>';
				        	for(var i=0;i<valYinji.length;i++){
					            content=valYinji[i].content;
					            con=valYinji[i].content;
					            if(content==null || content==""){
									content = "";
									con = "";
								}else if(content.length > 12){
									content = content.substring(0,10)+"...";
								}
					            tableBody+='<tr><td style="width:30%">'+'<span title=\''+valYinji[i].eventClass+'\'>'+valYinji[i].eventClass.substring(5)+'</span>'+'</td><td  style="width:30%">'+valYinji[i].happenTimeStr.substring(0,11)+'</td><td  style="width:40%"><a href="javascript:window.parent.localtionImportantEventPoint(\''+(valYinji[i].taskInfoId==null?'':valYinji[i].taskInfoId)+'\',\''+valYinji[i].eventId+'\')" title="'+con+'">'+content+'</a></td></tr>';
					            eventIds=eventIds+","+valYinji[i].eventId+"_"+(valYinji[i].taskInfoId==null?'':valYinji[i].taskInfoId);
					            //eventIds=eventIds+","+valYinji[i].eventId;
				       		 }
				        }
			        }
			        tableBody1 = "";
			        if(val.length>0){//表示有重大事件
			        	tableBody1+='<tr><td colspan="3"><font >重大或紧急事件</font></td></tr>';
			             for(var i=0;i<val.length;i++){
				            content=val[i].content;
				            con=val[i].content;
				            if(content==null || content==""){
								content = "";
								con = "";
							}else if(content.length > 12){
								content = content.substring(0,10)+"...";
							}
				            tableBody1+='<tr><td style="width:30%">'+'<span title=\''+val[i].eventClass+'\'>'+val[i].eventClass.substring(5)+'</td><td  style="width:30%">'+val[i].happenTimeStr.substring(0,11)+'</td><td  style="width:40%"><a href="javascript:window.parent.localtionImportantEventPoint(\''+(val[i].taskInfoId==null?'':val[i].taskInfoId)+'\',\''+val[i].eventId+'\')" title="'+con+'">'+content+'</a></td></tr>';
				            eventIds=eventIds+","+val[i].eventId+"_"+(val[i].taskInfoId==null?'':val[i].taskInfoId);
				            //eventIds=eventIds+","+val[i].eventId;
			       		 }
			       		 eventIds=eventIds.substring(1, eventIds.length);
			        }
			        tableBodyEnd='</table>';
			        
			        
			        // 地图定位
			        gisPosition(eventIds);
			        if(loadFlag == true){
			        	
			        }else {
			        	if(val.length+valYinji.length>0){
							window.parent.showNoticewin();
							loadFlag = true;
			        	}
			        }
			        $("#importantEvent").html(tableBodyStart+tableBody+tableBody1+tableBodyEnd);
			        // 有数据则弹出框
			        
		      }
		 }
   	  });
	  
	  	//地图定位
		function gisPosition(res){
		window.parent.clearMyLayer();
			if(res==""){
				return;
			}
			
			var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfEfficiencySupervisionController/getArcgisImportantEventLocateDataList.jhtml?ids="+res+"&showType=2";
			window.parent.currentLayerLocateFunctionStr="getArcgisDataOfImportantEvent('"+url+"')";
			window.parent.getArcgisDataOfImportantEvent(url);
		}
}
</script>

</html>