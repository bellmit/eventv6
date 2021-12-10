<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>各视联网信息中心-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<style type="text/css">
	.spanWordBreak{word-break: break-all;}
	.NorToolBtn {
		display:block;
		float:left;
		margin-right:10px;
		padding:4px 7px 4px 7px;
		color:#fff;
		line-height:14px;
		background-repeat:no-repeat;
		background-color:#2980B9;
		background-position:7px 5px;
		transition:all 0.2s;
		border-radius:3px;
	}
	.NorToolBtn:hover {
		color:#fff;
		text-decoration:none;
		background-color:#3498DB;
	}
</style>

</head>
<body>
	<div class="NorMapOpenDiv" style="width:100%;">	
		<div class="con LyWatch" style="height:140px;overflow:auto;">
        	<ul>
                <li style="width:305px;">
                	<p class="FontDarkBlue spanWordBreak" style="font-size:14px;width:100%;"><b>${equipment.eqpName!''}&nbsp;</b></p>
                	<p>
                    	设备号：<span class="FontDarkBlue spanWordBreak">${equipment.eqpSn!''}&nbsp;</span>
                    </p>
                    <p>设备地址：<span class="FontDarkBlue spanWordBreak">${equipment.location!''}&nbsp;</span></p>
                    <p>今天访客数：<span class="FontDarkBlue spanWordBreak"><#if equipment.todayVisitNum??>${equipment.todayVisitNum?c}<#else>0</#if>&nbsp;</span></p>
                    <p>近一周访客数：<span class="FontDarkBlue spanWordBreak"><#if equipment.thisWeekVisitNum??>${equipment.thisWeekVisitNum?c}<#else>0</#if>&nbsp;</span></p>
                    
                	<p>该楼目前居住着<font style="color:red;">${result!'0'}</font>个重点人员&nbsp;
                	<#if result??&&result!=0>
                	<a class="imp" style="color:#2980B9;cursor:pointer;">点击查看》</a></p>
                	</#if>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <div>
        	<ul>
        		<li>
        			<p style="padding-left: 38%;"><a href="###" class="NorToolBtn" onclick="queryLog();">查看访客记录</a></p>
        		</li>
        	</ul>
        </div>
	</div>
</body>
</html>
<script>
	$(".imp").click(function() {
		var div = $(".listWindow", window.parent.document);
		if($("#listDetail", window.parent.document).attr("src") == "") {
			$("#listDetail", window.parent.document).attr("src","${rc.getContextPath()}/zhsq/map/arcgis/equ/imp/list.jhtml");
		}
		if(div.hasClass("dest")) {
			div.removeClass("dest");
			div.toggle();
		} else {
			div.addClass("dest");
			div.toggle();
		}
	});

	function queryLog() {
		var url = "${rc.getContextPath()}/zhsq/visitorMachineController/index.jhtml?bizId=<#if equipment.bizId??>${equipment.bizId}<#else>0</#if>";
		window.parent.showMaxJqueryWindow("访客记录", url, 900, 400);
	}
</script>