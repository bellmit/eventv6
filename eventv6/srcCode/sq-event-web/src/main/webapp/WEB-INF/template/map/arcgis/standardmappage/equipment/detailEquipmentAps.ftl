<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>立体防控-车闸设备-概要信息</title>
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
		<div class="con LyWatch" style="height:135px;overflow:auto;">
        	<ul>
                <li style="width:305px;">
                	<p class="FontDarkBlue spanWordBreak" style="font-size:14px;width:100%;">
                		<a href="javascript:void(0);" onclick="showDetail('${equipment.eqpSn!}','${equipment.eqpId!}')">
                			<b>${equipment.eqpName!}&nbsp;</b>
                		</a>
                	</p>
                	<p>
                    	设备号：<span class="FontDarkBlue spanWordBreak">${equipment.eqpSn!}&nbsp;</span>
                    </p>
                    <p>设备地址：<span class="FontDarkBlue spanWordBreak">${equipment.location!}&nbsp;</span></p>
                    <p>近一个月曾有<font style="color:red;">${result!'0'}</font>辆车辆异常进出&nbsp;
                    <#if result??&&result!=0>
                	<a class="detail" style="color:#2980B9;cursor:pointer;">点击查看》</a></p>
                	</#if>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <div>
        	<ul>
        		<li>
        			<p style="padding-left: 38%;"><a href="###" class="NorToolBtn" onclick="showDetail('${equipment.eqpSn!}','${equipment.eqpId!}');">查看进出记录</a></p>
        		</li>
        	</ul>
        </div>
        
	</div>
</body>
<script type="text/javascript">
$(".detail").click(function() {
	var div = $(".listCarWindow", window.parent.document);
	if($("#listCarDetail", window.parent.document).attr("src") == "") {
		$("#listCarDetail", window.parent.document).attr("src","${rc.getContextPath()}/zhsq/map/arcgis/equ/car/list.jhtml?equSn=${equipment.eqpSn!''}");
	}
	if(div.hasClass("dest")) {
		div.removeClass("dest");
		div.toggle();
	} else {
		div.addClass("dest");
		div.toggle();
	}
});

function showDetail(title,id) {
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toIndexEquipmentAps.jhtml?plaId="+id;
	window.parent.showMaxJqueryWindow(title,url,848,405);	
}
</script>
</html>