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
            	<#if deviceInfo?? && deviceInfo.deviceType='100001'><!--水表（小）-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-sb-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='100002'><!--水表（大）-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-sb-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='100003'><!--消防栓-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-xfs-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='100011'><!--路灯-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-lud-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='100012'><!--电动车充电桩-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-cjcar-b.png">
            	<#elseif deviceInfo?? && deviceInfo.deviceType='100017'><!--环境监测-->
            		<img src="${rc.getContextPath()}/css/mawei/img/ico-hyjc-b.png">
            	</#if>
                <h3><#if deviceInfo.deviceName??>${deviceInfo.deviceName}</#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p style="width:70%;"><#if deviceInfo.deviceInstallAddress??>${deviceInfo.deviceInstallAddress}</#if></p>
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
        <div class="cle_r"></div><!--清除浮动-->
        
        <#if deviceInfo?? && deviceInfo.deviceType='100001'><!--水表（小）-->
    		 <div class="conmain">
	            <ul class="mss">
	                <li>
	                    <span class="fl_l" style="width:80px;">表盖状态</span>
	                    <ul class="st-class clearfix" style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="1">
	                    		<li class="fc-red">开</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="0">
	                    		<li class="fc-blue">闭</li>
	                    	<#else>
	                    		<li>无</li>	
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">超磁报警</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	<#if itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>设备状态</td>
	                  		<td id="device_status"></td>
                        	<td id="time_device_status"></td>
	                    </tr>
	                    <tr>
	                        <td>水表度数</td>
	                        <td><span id="water_degree"></span>m³</td>
                        	<td id="time_water_degree"></td>
	                    </tr>   
	                    <tr>
	                        <td>信号强度</td>
	                        <td><span id="signal_intensity"></span>dBm</td>
                        	<td id="time_signal_intensity"></td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='100002'><!--水表（大）-->
    		<div class="conmain">
	            <ul class="mss">
	                <li>
	                    <span class="fl_l" style="width:80px;">压力上限</span>
	                    <ul class="st-class clearfix"  style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_up?? && itemMap.alarm_type_up=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_up?? && itemMap.alarm_type_up=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">压力下限</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	<#if itemMap?? && itemMap.alarm_type_bottom?? && itemMap.alarm_type_bottom=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_bottom?? && itemMap.alarm_type_bottom=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l" style="width:80px;">表盖状态</span>
	                    <ul class="st-class clearfix" style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="1">
	                    		<li class="fc-red">开</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="0">
	                    		<li class="fc-blue">闭</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">超磁报警</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	<#if itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>设备状态</td>
	                  		<td id="device_status"></td>
                        	<td id="time_device_status"></td>
	                    </tr>
	                    <tr>
	                        <td>正向流量</td>
	                        <td><span id="forward_flow"></span>m³</td>
                        	<td id="time_forward_flow"></td>
	                    </tr>   
	                    <tr>
	                        <td>反向流量</td>
	                        <td><span id="reverse_flow"></span>m³</td>
                        	<td id="time_reverse_flow"></td>
	                    </tr>
	                    <tr>
	                        <td>压力</td>
	                        <td><span id="pressure"></span>MPa</td>
                        	<td id="time_pressure"></td>
	                    </tr>
	                    <tr>
	                        <td>温度</td>
	                        <td><span id="temperature"></span>℃</td>
                        	<td id="time_temperature"></td>
	                    </tr>
	                    <tr>  
	                        <td>信号强度</td>
	                        <td><span id="signal_intensity"></span>dBm</td>
                        	<td id="time_signal_intensity"></td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='100003'><!--消防栓-->
    		 <div class="conmain">
	            <ul class="mss" style="margin:10px 0;">
	                <li style="margin-bottom:0;">
	                    <span class="fl_l" style="width:80px;">压力波动</span>
	                    <ul class="st-class clearfix"  style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_pressure_wave?? && itemMap.alarm_type_pressure_wave=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_pressure_wave?? && itemMap.alarm_type_pressure_wave=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">压力上限</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	<#if itemMap?? && itemMap.alarm_type_pressure_up?? && itemMap.alarm_type_pressure_up=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_pressure_up?? && itemMap.alarm_type_pressure_up=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                </li>
	                <li style="margin-bottom:0;">
	                    <span class="fl_l" style="width:80px;">压力下限</span>
	                    <ul class="st-class clearfix"  style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_pressure_bottom?? && itemMap.alarm_type_pressure_bottom=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_pressure_bottom?? && itemMap.alarm_type_pressure_bottom=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">消防栓盖</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	<#if itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="1">
	                    		<li class="fc-red">开</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_open?? && itemMap.alarm_type_open=="0">
	                    		<li class="fc-blue">闭</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                </li>
	                <li style="margin-bottom:0;">
	                    <span class="fl_l" style="width:80px;">超磁报警</span>
	                    <ul class="st-class clearfix" style="width:180px;">
	                    	<#if itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="1">
	                    		<li class="fc-red">报警</li>
	                    	<#elseif itemMap?? && itemMap.alarm_type_superdiamagnetic?? && itemMap.alarm_type_superdiamagnetic=="0">
	                    		<li class="fc-blue">正常</li>
	                    	<#else>
	                    		<li>无</li>		
	                    	</#if>
	                    </ul>
	                    <span class="fl_l" style="width:80px;">&nbsp;</span>
	                    <ul class="st-class clearfix" style="width:120px;">
	                    	 <li>&nbsp;</li>		
	                    </ul>
	                </li>
	            </ul>
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>设备状态</td>
	                  		<td id="device_status"></td>
                        	<td id="time_device_status"></td>
	                    </tr>
	                    <tr>
	                        <td>压力</td>
	                        <td><span id="pressure"></span>MPa</td>
                        	<td id="time_pressure"></td>
	                    </tr>
	                    <tr>
	                        <td>温度</td>
	                        <td><span id="temperature"></span>℃</td>
                        	<td id="time_temperature"></td>
	                    </tr>
	                    <tr>  
	                        <td>信号强度</td>
	                        <td><span id="signal_intensity"></span>dBm</td>
                        	<td id="time_signal_intensity"></td>
	                    </tr>
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='100011'><!--路灯-->
    		<div class="conmain">
	            <ul class="mss">
	                <li>
	                    <span class="fl_l">开关状态</span>
	                    <ul class="st-class clearfix">
	                        <li class="ld-o" id="lighting_status1"><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-lud-l.png" title="开启">开</li>
	                        <li class="ld-c" id="lighting_status2"><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-lud-n.png" title="关闭">关</li>
	                        <li style="color:red" id="tip"></li>
	                    </ul>
	                    <div class="fl_r lud-kg clearfix">
	                        <a class="lud-open" href="javascript:void(0);" onclick="forcSwitch('${deviceInfo.deviceServiceId}','0')">打开路灯</a>
	                        <a class="lud-close" href="javascript:void(0);" onclick="forcSwitch('${deviceInfo.deviceServiceId}','1')">关闭路灯</a>
	                    </div>
	                </li>
	                <li> 
	                    <span class="fl_l">亮度</span>
	                    <ul class="st-class clearfix" style="width:158px">
	                        <li class="fc-blue"><font id="brightness"></font>%</li>
	                    </ul>
	                    <p class="fl_l" style="width:220px">采集时间：<span id="time_brightness"></span></p>
	                </li>
	                <li>
	                    <span class="fl_l">电压</span>
	                    <ul class="st-class clearfix" style="width:158px">
	                        <li class="fc-blue" id=""><font id="voltage"></font>V</li>
	                    </ul>
	                    <p class="fl_l" style="width:220px">采集时间：<span id="time_voltage"></span></p>
	                </li>
	                <li>
	                    <span class="fl_l">电流</span>
	                    <ul class="st-class clearfix" style="width:158px">
	                        <li class="fc-blue" id=""><font id="electricity"></font>A</li>
	                    </ul>
	                    <p class="fl_l" style="width:220px">采集时间：<span id="time_electricity"></span></p>
	                </li>
	            </ul>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='100012'><!--电动车充电桩-->
    		 <div class="conmain">
	            <ul class="mss">
	                <li>
	                    <span class="fl_l" style="width:80px;">主机状态</span>
	                    <ul class="st-class clearfix" style="width:180px;">
	                    	<li id="host_status1">离线</li>
	                    	<li class="fc-blue" id="host_status2" style="display:none;">在线</li>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>总端口数</td>
	                  		<td id="total_port"></td>
                        	<td id="time_total_port"></td>
	                    </tr>
	                    <tr>
	                        <td>在用端口数</td>
	                        <td id="use_port"></td>
                        	<td id="time_use_port"></td>
	                    </tr>   
	                    <tr>          
	                        <td>空闲端口数</td>
	                        <td id="free_port"></td>
                        	<td id="time_free_port"></td>
	                    </tr>
	                    <tr>
	                        <td>故障端口数</td>
	                        <td id="fault_port"></td>
                        	<td id="time_fault_port"></td>
	                    </tr> 
	                </table>
	            </div>
	        </div>
    	<#elseif deviceInfo?? && deviceInfo.deviceType='100017'><!--环境监测-->
    		 <div class="conmain">
	            <ul class="mss">
	                
	            </ul>
	            <ul class="tb-cjs-title clearfix">
	                    <li style="width: 30%;">名称</li>
	                    <li style="width: 20%;">采集值</li>
	                    <li style="width: 50%;">采集时间</li>
	            </ul>
	            <div class="gd-item">
	                <table class="tb-cjs" width="100%;">
	                    <tr>
	                        <td>PM2.5</td>
	                  		<td id="pm25"></td>
                        	<td id="time_pm25">ug/m3</td>
	                    </tr>
	                    <tr>
	                        <td>温度</td>
	                        <td id="temperature"></td>
                        	<td id="time_temperature">℃</td>
	                    </tr>   
	                    <tr>                       
	                        <td>湿度</td>
	                        <td id="humidity"></td>
                        	<td id="time_humidity">%</td>
	                    </tr>
	                    <tr>
	                        <td>噪音</td>
	                        <td id="noise"></td>
                        	<td id="time_noise">dB</td>
	                    </tr> 
	                </table>
	            </div>
	        </div>
    	</#if>
	</div>        
</body>
<script type="text/javascript">
	<#if dataList?? && (dataList?size > 0)>
 		<#list dataList as deviceCollectData>
 			<#if deviceCollectData.collectItemCode??>
 				<#if deviceCollectData.collectItemCode=="lighting_status"> 
 					<#if deviceCollectData.collectItemValue=="0">
 						$('#lighting_status1').show();
 						$('#lighting_status2').hide();
 					<#else>
 						$('#lighting_status2').show();
 						$('#lighting_status1').hide();
 					</#if> 
 				<#elseif deviceCollectData.collectItemCode=="host_status"> 	
 					<#if deviceCollectData.collectItemValue=="0">
 						$('#host_status2').show();
 						$('#host_status1').hide();
 					<#else>
 						$('#host_status1').show();
 						$('#host_status2').hide();
 					</#if> 
 				<#else>
 					$('#${(deviceCollectData.collectItemCode)!}').html("${(deviceCollectData.collectItemValue)!}");
 					$('#time_${(deviceCollectData.collectItemCode)!}').html("${(deviceCollectData.collectTime?string("yyyy-MM-dd HH:mm:ss"))!}");
 				</#if>
 			</#if>
  		</#list>
  	</#if>

	//路灯开关设置
	function forcSwitch(deviceId,lightingStatus){
		$.blockUI({message: "发送指令中..." , css: {width: '150px',height:'50px',lineHeight:'50px',top:'40%',
    	background:'url(${rc.getContextPath()}/css/loading.gif) no-repeat',textIndent:'20px'},overlayCSS:{backgroundColor:'#fff'}});
		$.ajax({
				type: 'POST',
			    url:'${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfDeviceController/forcSwitch.jhtml?deviceId='+deviceId+'&lightingStatus='+lightingStatus+'&t='+Math.random(),
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					$.unblockUI();
					if (data.res_code=='0') {
				
						//$.messager.alert('友情提示',data.res_message+'!','info');
						if(lightingStatus=='0'){
							$("#tip").html("路灯已打开！");
							$('#lighting_status1').show();
 							$('#lighting_status2').hide();
						}else{
						   $("#tip").html("路灯已关闭！");
							$('#lighting_status1').hide();
 							$('#lighting_status2').show();
						}
					} else {
						$("#tip").html(data.res_message+'!');
						//$.messager.alert('友情提示',data.res_message+'!','warning');
					}
				}, 
				error: function(data) {
					$.unblockUI();
					$("#tip").html('连接超时！');
					//$.messager.alert('错误', c, 'error');
					
				},
				complete : function() { 
					//setTimeout(clearTip(),2000);   
					setTimeout(function(){
						$("#tip").html('');
					},2000);
				}
			});
	}
	function clearTip(){
		 $("#tip").html('');
	}
</script>

</html>
