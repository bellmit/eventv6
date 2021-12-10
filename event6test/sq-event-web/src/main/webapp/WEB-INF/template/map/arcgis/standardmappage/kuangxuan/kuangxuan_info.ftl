<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>框选统计</title>
</head>

<script type="text/javascript">

function showqqy(){
 var url="${rc.getContextPath()}/zzgl/map/data/situation/boxglobalEyes.jhtml?geoString=${geoString}";

 if(${qqyCount}!=0){
 window.parent.selectOperateType('qqykx', url);
 }
}

function showxfs(){
	 var url="${rc.getContextPath()}/zzgl/map/data/goods/boxresource.jhtml?resTypeId=22&geoString=${geoString}&markerType=02010601&mapt=${mapType}";
	 if(${xfsCount}!=0){
	 	window.parent.selectOperateType('xfskx', url);
	 }
}
function showpaichusuo(){
	 var url="${rc.getContextPath()}/zzgl/map/data/goods/boxresource.jhtml?resTypeId=25&geoString=${geoString}&markerType=020304&mapt=${mapType}";
	 if(${paichusuoCount}!=0){
	 	window.parent.ffcs_show('派出所信息',url,0,249,0,false);
	 }
}
function showwgy(){
 var url="${rc.getContextPath()}/zzgl/map/data/situation/boxgridAdmin.jhtml?geoString=${geoString}";

 if(${wgrysCount}!=0){
//selectOperateType('kx', url)
window.parent.selectOperateType('kx', url);
 }
}

</script>

<body  style="border:none;background-color: rgb(255,255,255);">
	<DIV id="mainpanel" class="pageContent" style="overflow: hidden;">
		 <table width="100%" class="jcsj-searchList-2 " cellpadding="1" cellspacing="1" border="0" >
			<tr>
				<td colspan="8" bgcolor="#f4f4f4" style="height: 30px;font-size: 14px;font-weight: bold;"><img src="${rc.getContextPath()}/jsp/scim/gis/img/executive.png" width="13px" height="13px"/>&nbsp;人</td>
			</tr>
			
			
			
			<tr>
				<th >网格人员数</th>
				<td colspan="4"  ><a href="javascript:void(0);" onclick="showwgy()"><#if wgrysCount??>${wgrysCount!''}</#if></td>
			</tr>
			<tr>
				<td colspan="8" bgcolor="#f4f4f4" style="height: 30px;font-size: 14px;font-weight: bold;"><img src="${rc.getContextPath()}/jsp/scim/gis/img/inventory_categories.png" width="13px" height="13px"/>&nbsp;物</td>
			</tr>
			<tr>
				<th  >全球眼数</th>
				<td colspan="4"  ><a href="javascript:void(0);" onclick="showqqy()"><#if qqyCount??>${qqyCount!''}</#if></a></td>
			</tr>
			<tr>
				<th  >消防栓数</th>
				<td colspan="4"  ><a href="javascript:void(0);" onclick="showxfs()"><#if xfsCount??>${xfsCount!''}</#if></a></td>
			</tr>
			<tr>
				<th >派出所数</th>
				<td colspan="4"  ><a href="javascript:void(0);" onclick="showpaichusuo()"><#if paichusuoCount??>${paichusuoCount!''}</#if></a></td>
			</tr>
		</table>
	</DIV>
	
</body>
</html>
