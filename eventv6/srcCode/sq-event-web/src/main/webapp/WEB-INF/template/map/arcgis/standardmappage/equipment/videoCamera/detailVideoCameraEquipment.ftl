<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>视频摄像头-概要信息</title>

<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.spanWordBreak{word-break: break-all;}
	.shwoButtonClass{
    	display: block;
	    margin-right: 10px;
	    color: rgb(255, 255, 255);
	    line-height: 14px;
	    background-color: rgb(41, 128, 185);
	    padding: 4px 7px 4px 7px;
	    background-repeat: no-repeat;
	    background-position: 7px 5px;
	    transition: all 0.2s;
	    border-radius: 3px;
	}
	.shwoButtonClass:hover {
	    color: #fff;
	    text-decoration: none;
	    background-color: #3498DB;
	}
	a{text-decoration:none; color:#000; transition:all 0.3s; -moz-transition:all 0.3s; -webkit-transition:all 0.3s;}
	a:hover {text-decoration:underline;color:#CA0000;}
	
</style>

</head>
<body>
	<div class="NorMapOpenDiv" style="width:100%;">	
		<div class="con LyWatch" style="height:165px;overflow:auto;">
			<input type="hidden" id="eqpId" value="<#if equipment?? && equipment.eqpId??>${equipment.eqpId?c}</#if>"/>
        	<ul>
                <li>
                	<p>
                		摄像名称：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.eqpName??>${equipment.eqpName!''}</#if>&nbsp;</span>
                	</p>
                	<p>
                    	像机号：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.eqpSn??>${equipment.eqpSn!''}</#if>&nbsp;</span>
                    </p>
                    <p>
                    	设备地址：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.location??>${equipment.location!''}</#if>&nbsp;</span>
                    </p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <#if equipment.videoUrl??>
        <div align="center" style="width:100%">
        	<a href="###" onclick="showVideoCamera()" style="width:100px;float:center" class="shwoButtonClass">查看实时视频</a>
        </div>
        </#if>
	</div>
</body>
<script type="text/javascript">
	function showVideoCamera(){
		var videoUrl = "<#if equipment.videoUrl??>${equipment.videoUrl!''}<#else>${IDSS_DOMAIN!''}/equipment/video/play.jhtml?id=${equipment.eqpId?c}</#if>";
		if(videoUrl.indexOf("http")<0){
			videoUrl = "${IDSS_DOMAIN!''}" + videoUrl;
		}
		var width = 420;
		var height = 420;
		parent.showMaxJqueryWindow("视频",videoUrl,width,height);
	}
</script>
</html>