<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>立体防控-卡口设备-概要信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
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
                <li>
                	<p class="FontDarkBlue spanWordBreak" style="font-size:14px;width:100%;"><b>${equipment.eqpName!}&nbsp;</b></p>
                	<p>
                    	卡口编号：<span class="FontDarkBlue spanWordBreak">${equipment.eqpSn!}&nbsp;</span>
                    </p>
                    <p>
                    	车道数：<span class="FontDarkBlue spanWordBreak"><#if equipment.driveWayNum??>${equipment.driveWayNum?c}</#if>&nbsp;</span>
                    </p>
                    <p>设备地址：<span class="FontDarkBlue spanWordBreak">${equipment.location!}&nbsp;</span></p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <div>
        	<ul>
        		<li>
        			<p style="padding-left: 38%;"><a href="###" class="NorToolBtn" onclick="showBayonetRunData();">查看运行数据</a></p>
        		</li>
        	</ul>
        </div>
	</div>
	
	<script type="text/javascript">
		function showBayonetRunData() {
			var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toBayonetRunDataList.jhtml?eqpSn=${equipment.bizId!}';
			parent.showMaxJqueryWindow("卡口运行数据", url, 1018, 466);
		}
	</script>
</body>
</html>