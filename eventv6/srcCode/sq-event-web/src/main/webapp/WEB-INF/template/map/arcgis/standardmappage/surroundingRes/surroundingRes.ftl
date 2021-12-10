<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事件调度</title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-common.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ligerUI/skins/Aqua/css/ligerui-dialog.css" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jqueryeasyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script src="${rc.getContextPath()}/theme/scim/scripts/tablestyle.js"></script>
<style>
.btn_handle{ background:url(${rc.getContextPath()}/theme/scim/images/scim/btn_handle.png) no-repeat; width:56px; height:48px; display:block; text-indent:-99999px;}
textarea{
	width:95%;
	border:1px solid #dadada;
}
</style>
<style type="text/css">
.w_mainer{ background:#fff; font-family:Verdana, Geneva, sans-serif, "微软雅黑"; font-size:15px;}
.resdetail{ margin-bottom:5px;border-bottom:1px solid #bbb;}
.resdetail td{
	padding:5px;
	line-height:180%; 
	font-size:15px;
}
.resdetail tr{height:35px;}
img{vertical-align:middle;}
.rad{
	background:none;
}
</style>
<#setting number_format="0.##########">
</head>
<body>
<input type="hidden" id="defaultDistance" value="" />
<input type="hidden" id="x" value="${x}" />
<input type="hidden" id="y" value="${y}" />
<div class="w_mainer" style="padding:0 0;">
    <div>
    	<table border="0" style="background:url(${rc.getContextPath()}/theme/scim/wap/wapstyle/images/cx_bg.png) repeat-x; height:48px;" width="100%">
		 		<tr>
		 			<td>
						<input type="radio" name="distance" id="distanceNearby" value="99999" class="rad" /><label for="distanceNearby">最近资源</label>
						<input type="radio" name="distance" id="distance3k" value="3000" class="rad"/><label for="distance3k">3公里以内</label>
						<input type="radio" name="distance" id="distance10k" value="10000" class="rad" /><label for="distance10k">10公里以内</label>
		 			</td>
		 		</tr>
		</table>
		<div id='resourcesData'>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$(":radio[name=distance]").click(function(){
			var distance = $(this).val();
			var x = $("#x").val();
			x = x == "" || x == null ? 0 : x;
			var y = $("#y").val();
			y = y == "" || y == null ? 0 : y;
			loadResources(distance, x, y);
		});
		
		//默认点击项
		var defaultDistance = $("#defaultDistance").val();
		if (defaultDistance == "") {
			defaultDistance = "99999"
		}
		$(":radio[name=distance][value=" + defaultDistance + "]").click();
	});
	
	function loadResources(distance, x, y){
		//设置样式
		var urls = "${rc.getContextPath()}/zzgl/map/data/res/nearbyResources.jhtml?distance=" + distance + "&x=" + x + "&y=" + y+"&mapt="+window.parent.mapt;
		$.ajax({
		    url:urls,
		    data:'',
		    type:'POST',
		    dataType:"text",
		    async: true,
		    success:function(xxxJson){
		    	document.getElementById("resourcesData").innerHTML=xxxJson;
		 	}
	    });
	}
	
	function lookResources(distance, x, y, listType){
		var url = "${rc.getContextPath()}/zzgl/map/data/res/toGetSurroundingResourceList.jhtml";
		url = url + "?distance="+distance+"&x="+x+"&y="+y+"&listType="+listType+"&mapt="+window.parent.mapt;
		if("gridAdmin" == listType) {
			window.parent.ffcs_show('周边资源-网格员',url,0,249,0,false);
		}else if("police"==listType) {
			window.parent.ffcs_show('周边资源-派出所',url,0,249,0,false);
		}else if("qqy"==listType) {
			window.parent.ffcs_show('周边资源-全球眼',url,0,249,0,false);
		}else if("xfs"==listType) {
			window.parent.ffcs_show('周边资源-消防栓',url,0,249,0,false);
		}else if("party"==listType) {
			window.parent.ffcs_show('周边资源-党组织',url,0,249,0,false);
		}
	}
</script>
</body>
</html>
