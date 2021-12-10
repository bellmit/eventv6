<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出租屋-详情</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<style type="text/css">
	.UnitInfo ul li{
		padding-right:3px;
		width: 164px;
	}
</style>

</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body>

<div>
	<div class="UnitInfo">
		<ul>
			<li style="width:330px">名&nbsp;&nbsp;称：<span><#if areaRoomRent.roomName??>${areaRoomRent.roomName}</#if></span></li>
			<li>房&nbsp;&nbsp;东：<span><#if areaRoomRent.ownerName??>${areaRoomRent.ownerName}&nbsp;</#if></span></li>
			<li>联系电话：<span><#if areaRoomRent.fixedTelephone??>${areaRoomRent.fixedTelephone}</#if></span></li>
			<li>租户类型：<span><#if areaRoomRent.liveTypeLabel??>${areaRoomRent.liveTypeLabel}&nbsp;</#if></span></li>
			<li>到期时间：<span><#if areaRoomRent.hireEndStr??>${areaRoomRent.hireEndStr}</#if></span></li>
			<li>出租面积：<span><#if areaRoomRent.rentArea??>${areaRoomRent.rentArea}(平方米)&nbsp;</#if></span></li>
			<li>所在楼宇：<span><#if areaRoomRent.buildingName??>${areaRoomRent.buildingName}</#if></span></li>
			<li style="width:330px;">地&nbsp;&nbsp;址：<span><#if areaRoomRent.roomAddress??>${areaRoomRent.roomAddress}</#if></span></li>
			<#if inspectionList??>
				<#if (inspectionList?size>0)>
					<li>
						<p class="VisitRecord" style="padding-left: 22px">
								<a id="visitRecord" name="visitRecord" style="color:#0075a9;" href="javascript:showInspectionRecord('${roomId?c}');">走访记录(<#if inspectionCount??>${inspectionCount?c}</#if>)</a>
						</p>
					</li>
				</#if>
			</#if>
		</ul>
		<div class="clear"></div>
	</div>
</div>
<script type="text/javascript">
	var isCross;
	<#if isCross??>
		isCross = '${isCross}';
	</#if>

	function showPopDetail(title,partyId) {
		if (isCross != undefined) { // 跨域
			var roomId = ${roomId?c};
			var url= '${SQ_ZZGRID_URL}/zzgl/grid/ciRsRoom/popDetail.jhtml?roomId='+roomId+'&menu=1&partyId='+partyId;
			url = url.replace(/\&/g,"%26");
			title = encodeURIComponent(encodeURIComponent(title));
			var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+658+","+345+",'no')";
			$("#cross_domain_iframe").attr("src",urlDomain);
		} else {
			var roomId = ${roomId?c};
			var url= '${SQ_ZZGRID_URL}/zzgl/grid/ciRsRoom/popDetail.jhtml?roomId='+roomId+'&menu=1&partyId='+partyId;
			window.parent.showMaxJqueryWindow(title,url,658,345);
		}
	}
    function showInspectionRecord(partyId) {
        var height = 500;
        var width = 1100;
        var title = "巡查记录";
        var url = '${SQ_ZZGRID_URL}/zzgl/grid/busInspectionRecord/placePatrolAdd.jhtml?plaType=rentRoom&type=0&targetId=' + partyId;
        if (isCross != undefined) { // 跨域
            url = url.replace(/\&/g,"%26");
            title = encodeURIComponent(encodeURIComponent(title));

            var urlDomain = "${SQ_ZZGRID_URL}/zzgl/map/gis/commonCrossDomain.jhtml?callBack="+"showMaxJqueryWindow('"+title+"','"+url+"',"+width+","+height+",'no')";
            $("#cross_domain_iframe").attr("src",urlDomain);
        } else {
            window.parent.showMaxJqueryWindow(title,url,width,height);
        }
    }
</script>
</body>
</html>