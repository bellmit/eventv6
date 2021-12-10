<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报警机-概要信息</title>

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
	<div class="MC_con content light" id="content-d" style="width:100%;">	
		<div class="con LyWatch" style="height:100%;overflow:auto;">
			<input type="hidden" id="eqpId" value="<#if equipment?? && equipment.eqpId??>${equipment.eqpId?c}</#if>"/>
			<input type="hidden" id="bizId" value="<#if equipment?? && equipment.bizId??>${equipment.bizId!''}</#if>"/>
        	<ul>
                <li>
                	<p>
                		设备名称：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.eqpName??>${equipment.eqpName!}</#if>&nbsp;</span>
                	</p>
                	<p>
                    	设备序列号：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.eqpSn??>${equipment.eqpSn!}</#if>&nbsp;</span>
                    </p>
                	<p>
                    	用户手机号：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.managerTel??>${equipment.managerTel!}</#if>&nbsp;</span>
                    </p>
                	<p>
                    	设备型号：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.eqpModel??>${equipment.eqpModel!}</#if>&nbsp;</span>
                    </p>
                    <p>
                    	设备地址：<span class="FontDarkBlue spanWordBreak"><#if equipment?? && equipment.location??>${equipment.location!}</#if>&nbsp;</span>
                    </p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
        <div align="center" style="width:100%;margin-bottom:10px">
        	<a href="#" onclick="showAmsLog()" style="width:100px;float:center" class="shwoButtonClass">查看报警记录</a>
        </div>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	});
	
	function showAmsLog(){
		var detailURL = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/toDataListOfAmsLog.jhtml?bizId="+ $("#eqpId").val();
		var width = 900;
		var height = 420
		parent.showMaxJqueryWindow("报警记录列表",detailURL,width,height);
	}
</script>

</html>