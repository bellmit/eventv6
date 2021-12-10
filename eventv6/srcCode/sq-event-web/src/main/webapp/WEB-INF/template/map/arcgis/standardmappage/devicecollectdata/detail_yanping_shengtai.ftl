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
	<div class="main-mw">
        <!--详情信息-->
        <div class="topmain fl_l">
            <div class="title">
            	<#if deviceInfo?? && deviceInfo.deviceType='0004'><!--水质-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-hyjc-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='0005'><!--空气-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-hyjc-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='0006'><!--小流域-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-hyjc-b.png">
            	</#if>
                <h3><#if deviceInfo.deviceName??>${deviceInfo.deviceName}</#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p><#if deviceInfo.deviceInstallAddress??>${deviceInfo.deviceInstallAddress}</#if></p>
                </li>
                <li>
                    <span>所属网格</span>
                    <p><#if deviceInfo.regionName??>${deviceInfo.regionName}</#if></p>
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
        <#if deviceInfo?? && deviceInfo.deviceType='0004'><!--水质-->
    		 <div class="conmain">
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>PH</td>
	                  		<td>${(itemMap.ph)!}</td>
                        	<td>${(itemMap.dataTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>水温</td>
	                  		<td>${(itemMap.temp)!} ℃</td>
                        	<td>${(itemMap.dataTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>溶解氧</td>
	                  		<td>${(itemMap.dorate)!} mg/L</td>
                        	<td>${(itemMap.dataTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>电导率</td>
	                  		<td>${(itemMap.elec)!} μs/cm</td>
                        	<td>${(itemMap.dataTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>浊度</td>
	                  		<td>${(itemMap.turb )!} NTU</td>
                        	<td>${(itemMap.dataTime)!}</td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='0005'><!--空气-->
    		<div class="conmain">
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>PM2.5</td>
	                  		<td>${(itemMap.pmTwoFive)!}</td>
                        	<td>${(itemMap.monitorTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>PM10</td>
	                  		<td>${(itemMap.pmTen)!}</td>
                        	<td>${(itemMap.monitorTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>空气质量值AQI</td>
	                  		<td>${(itemMap.aqi)!}</td>
                        	<td>${(itemMap.monitorTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>空气质量状况</td>
	                  		<td>${(itemMap.dayAqiStatus)!}</td>
                        	<td>${(itemMap.monitorTime)!}</td>
	                    </tr>
	                    <tr>
	                        <td>首要污染物</td>
	                  		<td>${(itemMap.primaryPollutant )!}</td>
                        	<td>${(itemMap.monitorTime)!}</td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='0006'><!--小流域-->
    		 <div class="conmain">
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>PH</td>
	                  		<td>${(itemMap.ph)!}</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>溶解氧</td>
	                  		<td>${(itemMap._do)!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>高锰酸盐指数</td>
	                  		<td>${(itemMap.codmn)!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>化学需氧量</td>
	                  		<td>${(itemMap.codcr)!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>五日生化需氧量</td>
	                  		<td>${(itemMap.bod5 )!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>氨氮</td>
	                  		<td>${(itemMap.nhn )!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                    <tr>
	                        <td>总磷</td>
	                  		<td>${(itemMap.tp )!} mg/L</td>
                        	<td>${(itemMap.yearMonth)!}</td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	</#if>
	</div>        
</body>

</html>
