<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>护路护线-概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		width:120px;
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
		width:335px;
	}
</style>

</head>
<body>
	<div id="content-d" class="NorMapOpenDiv">
		<div class="con LyWatch" style="height:144px;overflow:auto;">
        	<ul>
        		<li><span class="FontDarkBlue" style="font-size:14px;font-weight:bold"><#if careRoad??><#if careRoad.lotName??><a href="javascript:void(0);" onclick="showICareRoadDetail('${careRoad.lotId}','${careRoad.lotName}')">${careRoad.lotName}</a></#if></#if></span></li>
				<li>线路类型：<span class="FontDarkBlue"><#if careRoad??><#if careRoad.secRouteTypeLabel??>${careRoad.secRouteTypeLabel}</#if></#if></span></li>
				<li>所属网格：<span class="FontDarkBlue">${gridNames!''}</span></li>
				<li>负责人：<span class="FontDarkBlue"><#if careRoad??><#if careRoad.manager??>${careRoad.manager}</#if></#if></span></li>
				<li>联系电话：<span class="FontDarkBlue"><#if careRoad??><#if careRoad.managerContact??>${careRoad.managerContact}</#if></#if></span></li>
				<li>管理队伍：<span class="FontDarkBlue"><#if prvetionTeam??><#if prvetionTeam.name??>${prvetionTeam.name}</#if></#if></span></li>
				<li>队伍人数：<span class="FontDarkBlue"><#if prvetionTeam??><#if prvetionTeam.memberNum??>${prvetionTeam.memberNum}（人）</#if></#if></span></li>
			</ul>
           <div class="clear"></div>
        </div>
	</div>			
</body>
<script type="text/javascript">
	function showICareRoadDetail(id, title) {
		var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	    var url = sq_zzgrid_url+"/zzgl/grid/careRoad/detail.jhtml?lotId="+id;
		window.parent.showMaxJqueryWindow(title,url,635,400);
	}
	
	(function($){
		$(window).load(function(){
			var options = {
				axis : "yx",
				theme : "minimal-dark"
			};
			enableScrollBar('content-d',options);
		});
	})(jQuery);
</script>
</html>
