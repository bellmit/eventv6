<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>空气质量</title>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/monitor-air.css"/>
		<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
		<style>
		.pon-sopt-left {
		    border-top-left-radius: 4px;
		    border-bottom-left-radius: 4px;
		    min-width: 218px;
		    position: absolute;
		    right: 46px;
		    bottom: 0px;
		    top: 0px;
		    background: rgba(255, 255, 255, 0.9);
		}
		.pon-sopt-right {
	    height: 100%;
	    width: 46px;
	    position: relative;
	    float:right;
	    vertical-align:middle;
	   }
	   .mr-sopt-text {
		    line-height: 46px;
		}
		</style>
	<body>
           <!--点位-->
		<a class="pon-w w-sopt5" id="watch">
			<div class="pon-sopt-right ld-leg-t${zgAqiStation.zgAQI.state!}">
				<div class="pon-sopt-left">
					<div class="pon-sopt-left-text">
						<p title="${zgAqiStation.stationName!}">${zgAqiStation.stationName!}</p>
						<span class="mrdata-bq bgcolor-tob-${zgAqiStation.zgAQI.state!}">${zgAqiStation.zgAQI.stateName!}</span>
					</div>  
				</div>
				<!--数据-->
				<span class="mr-sopt-text">${zgAqiStation.zgAQI.aqi!}</span>
			</div>
		</a>
<script>
//$("#close",parent.document).hide();
$(".title",parent.document).remove();
$("#iframe_info",parent.document).attr("height",'46px');


$("#watch").click(function(){
	var url="${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfNanpingAqiController/monitorair.jhtml?seqid=${zgAqiStation.seqid!}";
	var title="${zgAqiStation.stationName!} ";
	if(${zgAqiStation.zgAQI.aqi!}>100){
		title+='    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="${uiDomain}/web-assets/common/images/basic/mapgrid/icon-gaoj-r.png" height="20" width="20"/>';
	}
	    title+='    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${zgAqiStation.zgAQI.monitorTime!?string("yyyy-MM-dd HH:mm:ss")!}';

  window.parent.closeMaxJqueryWindow();
  window.parent.showMaxJqueryWindow(title , url,800,560);
 
});
</script>
	</body>

   
</html>
