<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>十户联防</title>
<style>
ul,li{list-style: none;margin: 0;padding: 0;}

.div_xian{width: 48%;float:left;margin-right: 1%;    margin-top: 20px;}

.div_xian ul li{width: 100%;height: 40px; line-height: 40px; border: 1px solid #5cc6ab;border-radius:8px;border-left: 6px solid; margin: auto; color:#5cc6ab;margin-bottom: 10px;}
.div_xian ul li span{font-size: 12px;padding-left: 10px;display:inline-block;width: 55%;border-right: 1px solid #5cc6ab;height: 22px;line-height: 22px;}
.div_xian ul li strong{color: #ff965c;font-size: 14px; margin-left: 8px;}
.peoper ul li{color: #f6d66c;border: 1px solid #f6d66c;border-left: 6px solid;}
.peoper ul li span{border-right: 1px solid #f6d66c;}

</style>
<script  type="text/javascript">
function init(){
	var title = parent.document.getElementById('openTitle');
	title.style.width = 'auto';
	title.innerHTML='${title.GRID_PATH}';
	
}
</script>
</head>

<body onload="init()">
<div>
<div class="div_xian">
	<ul>
		<li><span>联防组数量</span><strong>${stat.LF_ORG_NUM!'0'}</strong></li>
		<li><span>总人口数量</span><strong>${stat.ALL_RS_NUM!'0'}</strong></li>
		<li><span>受理事件数量</span><strong>${stat.ACCEPT_EVENT_NUM!'0'}</strong></li>
	</ul>
</div>
<div class="div_xian peoper">
	<ul>
		<li><span>联防长数量</span><strong>${stat.LF_ADMIN_NUM!'0'}</strong></li>
		<li><span>已上报事件数量</span><strong>${stat.REPORT_EVENT_NUM!'0'}</strong></li>
		<li><span>处理中事件</span><strong>${stat.DOING_EVENT_NUM!'0'}</strong></li>
	</ul>
</div>
</div>
</body>
</html>
