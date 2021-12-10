<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>烟感概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

<!-- css -->
<link href="${rc.getContextPath()}/css/mawei/css/reset.css" rel="stylesheet">
<link href="${rc.getContextPath()}/css/mawei/css/index.css" rel="stylesheet">

<script type="text/javascript" src="${rc.getContextPath()}js/mawei/js/divscroll.js"></script>

<style type="text/css">
	.PhotoEdit{width:120px; height:24px;margin-right:10px; line-height:24px; text-align:center; position:absolute; z-index:1; bottom:-8px; background:#000; filter:alpha(opacity=60); opacity:0.6; color:#fff; display:block; cursor:pointer;}
</style>
</head>
<body>
    <!--框-->
    <div class="main-mw">
        <!--详情信息-->
        <div class="topmain fl_l">
            <div class="title">
                <img src="${rc.getContextPath()}/css/mawei/img/ico-yg-b.png">
                <h3><#if deviceInfo.deviceName?exists>${deviceInfo.deviceName}</#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p><#if deviceInfo.deviceInstallAddress?exists>${deviceInfo.deviceInstallAddress}</#if></p>
                </li>
                <!--<li>
                    <span>所属网格</span>
                    <p><span class="FontDarkBlue">${deviceInfo.regionName!}</span></p>
                </li>-->
                <li>
                    <span>采集时间</span>
                    <p><span class="FontDarkBlue">${collTime!}</span></p>
                </li>
            </ul>
        </div>
        <div class="pic-sb fl_r">
	        <#if deviceInfo.deviceImage?exists>
		        <#if deviceInfo.deviceImage != "">
	   	 			<img src="${deviceInfo.deviceImage}"/>
	   	 		<#else>
	   	 			<img src="${rc.getContextPath()}/css/mawei//img/img-bg-no.png" />
	   	 		</#if>
	   	 	<#else>
	            <img src="${rc.getContextPath()}/css/mawei//img/img-bg-no.png" />
	   	 	</#if>
        </div>

        <div class="cle_r"></div><!--清除浮动-->


		<#if itemMap?? && deviceCollectAlert??>
           <#if itemMap["alarm_type_online"]??><#assign online=itemMap["alarm_type_online"]><#else><#assign online="n"></#if>
           <#if itemMap["alarm_type_smoke"]??><#assign smoke=itemMap["alarm_type_smoke"]><#else><#assign smoke="n"></#if>
           <#if itemMap["alarm_type_fire"]??><#assign fire=itemMap["alarm_type_fire"]><#else><#assign fire="n"></#if>
           <#if itemMap["alarm_type_electricity"]??><#assign electricity=itemMap["alarm_type_electricity"]><#else><#assign electricity="n"></#if>
           <#if itemMap["alarm_type"]??><#assign alarmType=itemMap["alarm_type"]><#else><#assign alarmType="n"></#if>
	        
	       <div class="conmain clearfix">
	            <ul class="mss" style="float: left;">
	                <li>
	                    <span class="fl_l">是否上线</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <#if online?? && online=="1"><li class="fc-blue">上线</li></#if> 
	                        <#if online?? && online=="2"><li>失联</li></#if>
	                        <#if online=="n"><li>暂无数据</li></#if>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">设备状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <#if alarmType?? && alarmType=="8"><li class="fc-blue">自检故障</li> </#if>
	                        <#if alarmType?? && alarmType=="7"><li>拆除 </li></#if>
	                        <#if alarmType=="n"><li>暂无数据</li></#if>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">烟雾告警状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                       <#if smoke?? && smoke=="9"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-gaoj-n.png" title="烟雾告警解除"></li></#if>
	                       <#if smoke?? && smoke=="3"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-gaoj-l.png" title="烟雾告警"></li></#if>
	                       <#if smoke=="n"><li>暂无数据</li></#if>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="mss" style="float: right;">
	                <li>
	                    <span class="fl_l">火警状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <#if fire?? && fire=="4"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-fire-l.png" title="火警"></li></#if>
	                       	<#if fire?? && fire=="10"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-fire-n.png" title="火警解除"></li></#if>
	                       	<#if fire=="n"><li>暂无数据</li></#if>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">电量状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <#if electricity?? && electricity=="5"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-dc-l.png" title="电量正常"></li></#if>
	                        <#if electricity?? && electricity=="6"><li><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-dc-n.png" title="电量低"></li></#if>
	                        <#if electricity=="n"><li>暂无数据</li></#if>
	                    </ul>
	                </li>
	            </ul>
	        </div>
	    <#else>
	           <div class="conmain clearfix">
	            <ul class="mss" style="float: left;">
	                <li>
	                    <span class="fl_l">是否上线</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <li>暂无数据</li>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">设备状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                         <li>暂无数据</li>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">烟雾告警状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <li>暂无数据</li>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="mss" style="float: right;">
	                <li>
	                    <span class="fl_l">火警状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <li>暂无数据</li>
	                    </ul>
	                </li>
	                <li>
	                    <span class="fl_l">电量状态</span>
	                    <ul class="st-class st-class2 clearfix">
	                        <li>暂无数据</li>
	                    </ul>
	                </li>
	            </ul>
	        </div>
        </#if>
    </div>

</body>
<script type="text/javascript">
$(function() {
		//添加滚动条样式
		//$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
		
		$("#localImag").hover(function(){
			$(this).find(".PhotoEdit").slideDown(200);
			}, function(){
			$(this).find(".PhotoEdit").slideUp(200);
		});
});

function prevImg(imgUrl){
	window.open(imgUrl);
}
</script>

</html>
