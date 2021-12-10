<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/scrollpagination.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
body{ padding:0; margin:0; font-family:Verdana, Geneva, sans-serif, "微软雅黑", "冬青黑体简体中文 W3"; font-size:12px;}
p,ul,li{ padding:0; margin:0; }
ul,li{ list-style:none;}
.red{ color:#f51b1b; font-weight:bold;}
.abnormal p{ color:#666; padding:4px 5px;}
.abnormal ul li{ padding:5px;}
.abnormal ul li span{ display:inline-block; margin-right:5px;padding:5px 0px;}
.abnormal ul li .plate{ border-radius:2px; background:#005fd8; color:#fff; width:80px; text-align:center; overflow:hidden; vertical-align:middle;}
.abnormal ul li .degree{ width:120px;}
</style>

<script type="text/javascript">
var equSn = "${equSn!''}";
var page = 0;
$(function() {
 	layer.load(0);
    $('#content').scrollPagination({
        'contentPage': '${rc.getContextPath()}/zhsq/map/arcgis/equ/car/listData.json',
        'contentData': {equSn : equSn, rows : 10, page : function(){return page} },
        'scrollTarget': $(window),
        'heightOffset': 10,
        'beforeLoad': function() {
			page = page + 1;
		},
        'afterLoad': function(elementsLoaded) {
    		layer.closeAll('loading');
            $(elementsLoaded).fadeInWithDelay();
        }
    });

    $.fn.fadeInWithDelay = function() {
        var delay = 0;
        return this.each(function() {
            $(this).delay(delay).animate({
                opacity: 1
            },
            200);
            delay += 100;
        });
    };

});

function show(id){
	if(id != null){
		var title = "车辆信息详情";
		var width = 720;
		var height = 400;
		var url = "${SQ_ZZGRID_URL}/zzgl/resident/car/show/"+id+".jhtml";
		window.parent.showMaxJqueryWindow(title,url,width,height);
	}
}
</script>
</head>
<body class="cn">

<div class="abnormal">
  <p>异常出入次数统计时间范围为近一个月</p>
  <ul id="content">
    </ul>
</div>
</body>
</html>