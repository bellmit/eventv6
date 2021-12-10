<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>播放设置</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="background-color: #fff;">
	<div id="trajectoryDiv"
		style="left: 0px; top: 0px; width: 100%; height: 100%; overflow: hidden;">
		<div class="NorMapOpenDiv1" style="bottom: 10px">
			<div class="box" style="width:520px;border:none;">
				<div class="con trajectory">
					<!---->
					<div class="time">
						<div id="beginTimeDiv" class="begin fl"></div>
						<div id="centerTimeDiv" class="center fl"></div>
						<div id="endTimeDiv" class="end fr"></div>
						<div class="clear"></div>
					</div>
					<div class="schedule">
						<div id="trajectoryProgressLine" class="line">
							<div id="trajectoryProgress" class="rate" style="width:2%;">
								<div class="control"></div>
							</div>
						</div>
					</div>
					
					<div class="tools">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tbody>
								<tr>
									<td width="33%" align="center">
										<a id="run" href="javascript:void(0)" style="background: #751ece;">
											<img title="播放" src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/gj_07.png">
										</a>
									</td>
									<td width="33%" align="center">
										<a id="pause" href="javascript:void(0)" style="background:#751ece;">
											<img title="暂停" src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/gj_09.png">
										</a>
									</td>
									<td width="33%" align="center">
										<a id="stop" href="javascript:void(0)" style="background: #9e61dd;">
											<img title="停止" src="${rc.getContextPath()}/theme/arcgis/standardmappage/images/gj_06.png">
										</a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		initEvents();
	});
	
	//绑定事件
function initEvents(){
	
	var lushu = parent.getLushu();
	var taxiMarker = parent.getTaxiMarker();
	
	$("#run").click(function(){
		taxiMarker.enableMassClear(); //设置后可以隐藏改点的覆盖物
		taxiMarker.hide();
		lushu.start();
		// map.clearOverlays();	//清除所有覆盖物
	});
	
	$("#pause").click(function(){
		lushu.pause();
	});
	
	$("#stop").click(function(){
		lushu.stop();
		parent.map.clearOverlays();
	});
	
	$("#hide").onclick = function() {
		lushu.hideInfoWindow();
	}
	$("#show").onclick = function() {
		lushu.showInfoWindow();
	}
	
	setProgressBar();
}

var points;

function setProgressBar(){

	points = [];
	
	points = parent.getPointsArrayStore();
	if(!points) return;
	
	var len = points.length;
	if(len){
		$('#beginTimeDiv').html(points[0].locateTime);
		$('#centerTimeDiv').html(points[0].locateTime);
		$('#endTimeDiv').html(points[len-1].locateTime);
	}
}

function callFunc(currPointIndex,progressBarPercent){
	//console.log(progressBarPercent);
	if(progressBarPercent >= 100) return;
	$('#centerTimeDiv').html(points[currPointIndex].locateTime);
	$('#trajectoryProgress').css({'width':toFixNum(progressBarPercent-0+2)+'%'});
}

function toFixNum(num){
	return num.toFixed(2);
}

</script>
</html>
