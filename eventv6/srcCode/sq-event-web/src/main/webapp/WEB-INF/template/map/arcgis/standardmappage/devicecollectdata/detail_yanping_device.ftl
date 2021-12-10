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
            	<#if deviceInfo?? && deviceInfo.deviceType='0002'><!--井盖-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-hyjc-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='0003'><!--垃圾桶-->
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
         <div class="conmain">
         <#if alertFlag?? && alertFlag='1' >
    		     <ul class="mss">
	                <li>
	                  <span class="fl_l" style="width:60px;">告警原因</span>
	                    <ul class="st-class clearfix" style="width:160px;">
	                    		<li class="fc-red">${alertMap.alarmReason!}</li>
	                    </ul>
	                    <span class="fl_l" style="width:60px;">告警时间</span>
	                    <ul class="st-class clearfix" style="width:160px;">
	     					<li class="fc-red">${alertMap.startTime!}</li>
	                    </ul>
	                </li>
	                
	            </ul>
	              <div class="gd-item" style="width:100%;text-align:center;margin-bottom:5px">
	                  <a href="#" onclick="event_disposal()" class="BigNorToolBtn ShangBaoBtn" style="float:none !important">上报</a>
	              </div>
	     </#if>
        <#if deviceInfo?? && deviceInfo.deviceType='0002'><!--井盖-->
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>电池电量</td>
	                  		<td>${(itemMap.electricity)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>倾斜角度</td>
	                  		<td>${(itemMap.inclinationAngle)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>剩余电压</td>
	                  		<td>${(itemMap.redidualVoltage)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>信号强度</td>
	                  		<td>${(itemMap.signalIntensity)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                </table>
	            </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='0003'><!--垃圾桶-->
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>电池状态</td><!-- 电池状态：0表示未充电 1表示主电池充电池状态：0表示未充电 1表示主电池充 -->
	                  		<td>
		                  		<#if itemMap?? && itemMap.batterystatus?? && itemMap.batterystatus=="0">
		                    		未充电
		                    	<#elseif itemMap?? && itemMap.batterystatus?? && itemMap.batterystatus=="1">
		                    		<li class="fc-blue">主电池充电</li>
		                    	<#else>
		                    		无		
		                    	</#if>
	                  		</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>当前容量</td>
	                  		<td>${(itemMap.capacity)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>压缩装置</td><!-- 0正常 1异常-->
	                  		<td>
	                  		<#if itemMap?? && itemMap.compress?? && itemMap.compress=="0">
		                    		正常
		                    	<#elseif itemMap?? && itemMap.compress?? && itemMap.compress=="1">
		                    		异常
		                    	<#else>
		                    		无		
		                    </#if>
		                    </td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>当天70%启动压缩装置次数</td>
	                  		<td>${(itemMap.compress70count)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                     <tr>
	                        <td>当天90%启动压缩装置次数</td>
	                  		<td>${(itemMap.compress90count)!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>开门状态</td><!-- 0正常 1异常-->
	                  		<td>
	                  		<#if itemMap?? && itemMap.open?? && itemMap.open=="0">
		                    		正常
		                    	<#elseif itemMap?? && itemMap.open?? && itemMap.open=="1">
		                    		异常
		                    	<#else>
		                    		无		
		                    </#if>
	                  		</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>主电池电压</td>
	                  		<td>${(itemMap.primarybattery )!}</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>滚动屏状态</td>
	                  		<td>
	                  		<#if itemMap?? && itemMap.scroll?? && itemMap.scroll=="0">
		                    		正常
		                    	<#elseif itemMap?? && itemMap.scroll?? && itemMap.scroll=="1">
		                    		异常
		                    	<#else>
		                    		无		
		                    </#if>
	                  		</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                    <tr>
	                        <td>语音装置</td>
	                  		<td>
	                  		<#if itemMap?? && itemMap.voice?? && itemMap.voice=="0">
		                    		正常
		                    	<#elseif itemMap?? && itemMap.voice?? && itemMap.voice=="1">
		                    		异常
		                    	<#else>
		                    		无		
		                    </#if>
	                  		</td>
                        	<td>${collTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	                    </tr>
	                </table>
	            </div>
    	</#if>
       </div>
	</div>  
<script type="text/javascript"> 
		function event_disposal(){   
	var event = {
		"eventName" : '${alertMap.alarmReason!}',
		"occurred" : '${deviceInfo.deviceInstallAddress!}',
		"happenTimeStr":'${alertMap.startTime!}',
		"resMarker":{'x':'${resMarker.x!}','y':'${resMarker.y!}','mapType':${resMarker.mapType!},'markerType':'${resMarker.markerType!}'},
		"content":'${alertMap.startTime!} ${deviceInfo.deviceInstallAddress!}${deviceInfo.deviceName!}(设备业务标识:${deviceInfo.deviceServiceId!})${alertMap.alarmReason!}'
	}
	var event = JSON.stringify(event); 
	var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson='+encodeURIComponent(event);
	
	window.parent.showMaxJqueryWindow("上报事件", url, 801, 390)
	
	}
</script>     
</body>

</html>
