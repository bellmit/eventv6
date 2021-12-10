<link rel="stylesheet" type="text/css" href="${uiDomain!''}/js/jquery-easyui-1.4/themes/icon.css">

<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/jquery.mCustomScrollbar.css">

<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>

<script type="text/javascript" src="${uiDomain!''}/js/jquery.easyui.patch.js"></script><!--�����޸�easyui-1.4��easyui-numberboxʧȥ�����������С���������-->

<script type="text/javascript" src="${uiDomain!''}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/layer/layer.js"></script>

<script type="text/javascript" src="${uiDomain!''}/js/echarts/echarts-all.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/TreeGrid.js"></script>
<script type="text/javascript" src="${uiDomain!''}/js/jquery.mCustomScrollbar.concat.min.js"></script>

  <style type="text/css">
    body{color:#fff;}
    </style>
    
<script type="text/javascript">
function noData(id){
	var div = document.getElementById(id);
	var w = div.style.width,h = div.style.height;
	if(w.length == 0 || h.length == 0){
		w = "100px",h = "120px";
	}
	div.innerHTML = "<div style='width:"+w+";height:"+h+";vertical-align: middle;text-align:center;display: table-cell;'><img src='${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/nodata.png'></div>";
}
</script>