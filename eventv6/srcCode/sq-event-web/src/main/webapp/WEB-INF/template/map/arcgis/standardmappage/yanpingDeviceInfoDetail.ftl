<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${(deviceInfo.deviceName)!}信息</title>
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
	<link href="${rc.getContextPath()}/css/mawei/css/reset.css" rel="stylesheet">
    <link href="${rc.getContextPath()}/css/mawei/css/index.css" rel="stylesheet">
    <script type="text/javascript" src="${rc.getContextPath()}js/mawei/js/divscroll.js"></script>
    <script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
</head>
<body>
	
        <!--详情信息-->
        <div class="main-mw" style="padding-bottom:0px">
        <div class="topmain fl_l" style="margin-top:10px">
            <div class="title">
            	<#if (deviceInfos.deviceType)?? && deviceInfos.deviceType=='0007'>
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-jg-b.png">
            	</#if>
                <h3><#if deviceInfos.deviceName??>${deviceInfos.deviceName}</#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p><#if deviceInfos.deviceInstallAddress??>${deviceInfos.deviceInstallAddress}</#if></p>
                </li>
                <li>
                    <span>所属网格</span>
                    <p><#if deviceInfos.regionName??>${deviceInfos.regionName}</#if></p>
                </li>
            </ul>
        </div>
        
        <div class="pic-sb fl_r">
        	<#if deviceInfo.deviceImage??> 
        		<img src="${deviceInfo.deviceImage}" />
        	<#else>
        		<img src="${rc.getContextPath()}/css/mawei/img/img-bg-no.png" />
        	</#if>
        </div>
        
        <div  style="clear:both;"></div><!--清除浮动-->
        <#if deviceInfos?? && deviceInfos.deviceType='0007'>
    		 <div class="conmain">
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">传感器类型名称</li>
	                    <li style="width: 20%;">传感器名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 30%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                <#if runData??>
	                <#list runData as data>
	                    <tr>
	                    	<td><#if data.Type??>${data.Type}</#if></td>
	                        <td><#if data.Name??>${data.Name}</#if></td>
	                  		<td><#if data.NewestValue??>${data.NewestValue}</#if> <#if data.Unit?? && data.Unit='百分比'>%<#else>${data.Unit}</#if></td>
                        	<td><#if data.GatherTime??>${data.GatherTime}</#if></td>
	                    </tr>
	                </#list>
     				</#if>	                    
	                </table>
	            </div>
	        </div>
    	</#if>
    	</div>	       
</body>
<script type="text/javascript">
	

</script>

</html>
