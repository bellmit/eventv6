<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<title>地图首页</title> 
	<script src="${rc.getContextPath()}/js/uncompressed/jquery-1.9.1.js" type="text/javascript"></script>
	<script src="${rc.getContextPath()}/js/map/arcgis/library/ffplugin/skylineapi.js" type="text/javascript"></script>
	<script type="text/javascript">
	var load3D = true;
 	var skylineViewData ;
 	var skylineViewUrl;
	</script>
</head>
<body style="width:100%;height:100%;border:none;" >


<div id="dialog" title="三维地图显示">
    <object id="TE3DWindow" classid="CLSID:3a4f9192-65a8-11d5-85c1-0001023952c1" style="height: 98%;
        width: 100%;">
    </object>
    <object id="sgworld" classid="CLSID:3a4f91b1-65a8-11d5-85c1-0001023952c1" style="height: 0">
    </object>
</div>

</body>
<script type="text/javascript">
var height = $(document).height()-2;
var width = $(document).width()-2;
$('#TE3DWindow').css('height',height);
$('#TE3DWindow').css('width',width);
init();
function init(){
	if (load3D) {
	     onload();
	     load3D = false;
	 }
	 //$("#TE3DWindow").show();
     clearlabel();
     modelShow('a9');
     setTimeout("cnterAtLevel(parent.lc_x, parent.lc_y, 500);", 5*1000 );
     setTimeout("createLabel(parent.lc_x, parent.lc_y, 9.0 , 'http://183.60.192.206:8000/lecong_GeoSP/geosp/images/Pin.png',  230, 340, parent.lc_show_url, parent.lc_name,  parent.lc_name)", 6*1000 );
	 
}

</script>
</html>
