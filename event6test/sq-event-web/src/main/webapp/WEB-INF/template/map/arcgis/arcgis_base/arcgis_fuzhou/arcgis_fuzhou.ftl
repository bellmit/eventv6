<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta HTTP-EQUIV="pragma" CONTENT="no-cache" />
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate" />
<meta HTTP-EQUIV="expires" CONTENT="0" />
<title>福州市社会综治管理系统</title>

<link href="${uiDomain!''}/images/map/gisv0/special_arcgis/standardmappage_fuzhou/css/normal.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${uiDomain!''}/images/map/gisv0/special_arcgis/standardmappage_fuzhou/js/function.js" type="text/javascript"></script>
<script>
//政务外网平安建设地址
var peaceReportUrl="${PEACE_REPORT_URL}";
//预割接平安建设地址
//var peaceReportUrl="http://gd.ygj.aishequ.org/pajs";
//开发环境平安建设地址
//var peaceReportUrl="http://gd.fjsq.org:8084/pajs";
var gridId = "${gridId?c}";
</script>
 
<style type=text/css>
    body { BACKGROUND: #eef4fa }
</style>
</head>
<body>
<div class="main">
	<div class="MainLeft fl dest" id="MainLeft">
    <!---------------平安建设 begin-------------------->
        <div class="model_1">
        	<div class="NorTitle">平安建设</div>
            <div class="NorBox">
            	<ul>
                	<li><a style="font-size: 16px" href="javascript:reportview('网格化社会服务平台建设情况','/peacebuild/report/StatController/toQueryReportInsList.jhtml')">网格化社会服务平台建设情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('平安基础建设情况','/peacebuild/report/StatController/toQueryPeaceReportInsList.jhtml')">平安基础建设情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('矛盾纠纷多元调解平台建设情况','/peacebuild/report/StatController/toQueryConflictReportInsList.jhtml')">矛盾纠纷多元调解平台建设情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('治安防控体系建设情况','/peacebuild/report/StatController/toQueryReportSecurityControl.jhtml')">治安防控体系建设情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('重点整治单位整治情况','/peacebuild/report/StatController/toQueryReportRenovateKeyUnit.jhtml')">重点整治单位整治情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('一个中心三个队伍建设情况','/peacebuild/report/StatController/toQueryOneCentreReportInsList.jhtml')">一个中心三个队伍建设情况</a></li>
                	<li><a style="font-size: 16px" href="javascript:reportview('群众安全感调查数据汇总','/peacebuild/report/StatController/toQueryThreeRateReportInsList.jhtml')">群众安全感调查数据汇总</a></li>
                </ul>
            </div>
        </div>
    <!---------------平安建设 end-------------------->
    <div class="h_10"></div>
    <!---------------重大事件 begin-------------------->
        <div class="model_2">
        	<div class="NorTitle">重大事件监控</div>
            <div class="NorBox" id="importantEvent">
            	
            </div>
        </div>
    <!---------------重大事件 end-------------------->
	</div>
	<div class="SwitchBtn shut"></div>
	<div class="MainRight fr" id="MainRight">
    <!---------------map begin-------------------->
        <div class="map">
            <!--<div class="MapSwitchBtn MapShut"></div> 543-->
            <div class="NorBox1">
                <div class="MapShow" width="100%">
		        	<IFRAME id="frameId" name="cwin" src="" height="617" width="100%"  frameborder="0"></IFRAME>
                </div>
            </div>
        </div>
        <IFRAME id="crossDomainFrame" name="crossDomainFrame" src="" height="0" width="0" frameborder="0"></IFRAME>
    <!---------------map end-------------------->
    </div>
    <div class="clear"></div>
</div>

</body>
<script>
var openflag = false;
var eventResultSId;
setInterval(function(){						
		var width=$(window).width();
		if(openflag==false){
			$(".MainRight").css("width",width-330);
		}else{
			$(".MainRight").css("width",width-30);
		}
		$(".matter").css("width",width-400);
		$("#WebSiteBox").css("width",width-62);
},1);
getFrame();

var dynamicTiming = setInterval(function(){
	if(document.getElementById('frameId').contentWindow.impEventCrossDomainCallBack != undefined) {
		getImportantEvent();
		window.clearInterval(dynamicTiming);
	}
},0.5*1000);

function getFrame(){
	var mapurl = "${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_STANDARD_HOME";
	$("#frameId").attr("src",mapurl);
}
//加载影响范围是重大事件

function getImportantEvent(){
    $.ajax({   
		 url: '${rc.getContextPath()}/zhsq/event/eventDisposalController/importantEvent.json?statusName=innerPlatform&gridId='+gridId,   
		 type: 'POST',
		 timeout: 3000000, 
		 dataType:"json",
		 error: function(data){  
		   alert('数据(事件列表)读取失败,详情请查看后台日志!'); 
		 },   
		 success: function(data){  
		      var result=data.result;
			      if(result=='yes'){
			        var val=eval(data.eventList);
			        var eventIds="";
			        var tableBody='<ul>';
			        if(val.length>0){//表示有重大事件
			             for(var i=0;i<val.length;i++){
				            var content=val[i].content;
				            if(content==null || content==""){
								content = "";
							}else if(content.length > 12){
								content = content.substring(0,12)+"...";
							}
				            tableBody+='<li><a href="javascript:selected(\''+(val[i].taskInfoId==null?'':val[i].taskInfoId)+'\',\''+val[i].eventId+'\')">'+content+'</a></li>';
				            //eventIds=eventIds+","+val[i].eventId;
				            eventIds=eventIds+","+val[i].eventId+"_"+(val[i].taskInfoId==null?'':val[i].taskInfoId);
			       		 }
			        }else{
			        	//无重大事件通知不定位
			        	eventIds="-";			        	
			        }
	       		 tableBody+='</ul>'
		         $("#importantEvent").html(tableBody);
		         eventResultSId=eventIds.substring(1, eventIds.length);
				  //地图上事件定位
				 getImportantEventLocateData(eventResultSId);
		      }
		 }	 
   	});
}

//报表弹出层
function reportview(title,url){
	var trueUrl = peaceReportUrl+url;
	reportwindow(title,trueUrl,1000,600);
}
function reportwindow(title,url,iWidth,iHeight){
	var iWidth; //弹出窗口的宽度;
	var iHeight; //弹出窗口的高度;
	var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	window.open(url,title,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=yes,status=no');
} 
function getImportantEventLocateData(eventResultSId) {
	document.getElementById('frameId').contentWindow.impEventCrossDomainCallBack(eventResultSId);
}
function selected(tid,eventId,type){
	document.getElementById('frameId').contentWindow.impEventClickCrossDomainCallBack(tid,eventId,type);
}

</script>
</html>
