<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>马尾智慧小区设备</title>

    <!-- css -->
    <link href="${rc.getContextPath()}/css/mawei/css/reset.css" rel="stylesheet">
    <link href="${rc.getContextPath()}/css/mawei/css/index.css" rel="stylesheet">

    <!-- JavaScript -->
    <script src="${rc.getContextPath()}/js/mawei/js/jquery-1.7.1.min.js"></script>

</head>
<body>

    <!--框-->
    <div class="main-mw">
        <!--详情信息-->
        <div class="topmain fl_l">
            <div class="title">
                <img src="${rc.getContextPath()}/css/mawei/img/ico-jg-b.png">
                <h3><#if deviceInfo.deviceName?exists><#if deviceInfo.deviceName?length gt 18 >${deviceInfo.deviceName[0..18]}...<#else>${deviceInfo.deviceName}</#if></#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p>${deviceInfo.deviceInstallAddress!}</p>
                </li>
               
                <li>
                    <span>告警采集时间</span>
                    <p>2017-10-10 16:40:50</p>
                </li>
            </ul>
        </div>
        <div class="pic-sb fl_r">
        <#if deviceInfo.deviceImage??>
           <img src="${deviceInfo.deviceImage!}" />
        <#else>
            <img src="${rc.getContextPath()}/css/mawei/img/img-bg-no.png" />
       </#if>
           
        </div>

        <div class="cle_r"></div><!--清除浮动-->

        <div class="conmain">
            <ul class="mss">
                <li>
                 
                    <span class="fl_l">井盖状态</span>
                    <#if itemMap?? && deviceCollectAlert??>
                    <#assign  deviceStatus=itemMap["device_status"]>
                     <ul class="st-class clearfix">
                        <#if deviceStatus?? && deviceStatus=="0"> <li class="ld-o"><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-jingg-open.png">开</li> </#if>                        
                        <#if deviceStatus?? && deviceStatus=="1"><li class="ld-o"><img class="img-css" src="${rc.getContextPath()}/css/mawei/img/img-jingg-close.png">关</li></#if>
                    </ul>
                    </p>
                    <#else>
                     <ul class="st-class clearfix">
                                                                        无
                    </ul>
                  </#if>          
                 
                </li>
            </ul>
           <#if p1["collTime"]!='-1' && p2["collTime"]!='-1'>
            <ul class="tb-cjs-title clearfix">
                    <li style="width: 30%;">设备类型</li>
                    <li style="width: 20%;">采集值</li>
                    <li style="width: 50%;">采集时间</li>
            </ul>
            <div class="gd-item">
                <table class="tb-cjs" width="100%;">
                    <tr>
                        <td>井盖井下设备</td>
                        <td>${p1["under_the_shaft"]!}</td>
                        <td><#if p1["collTime"]??><#if p1["collTime"]!='-1'> ${p1["collTime"]!} <#else>无</#if></#if></td>
                    </tr>
                    <tr>
                        <td>井盖路面积水设备</td>
                        <td>${p2["surface_gathered_water"]!}</td>
                        <td><#if p2["collTime"]??><#if p2["collTime"]!='-1'> ${p2["collTime"]!} <#else>无</#if></#if></td>
                    </tr>
                    <!--
                    <tr>
                        <td>井盖浊度设备</td>
                        <td>${p3["turbidity"]!}</td>
                        <td><#if p3["collTime"]??><#if p3["collTime"]!='-1'> ${p3["collTime"]!} <#else>无</#if></#if></td>
                    </tr>
                    <tr>
                        <td>井盖流速设备</td>
                        <td>${p4["velocity_of_flow"]!}</td>
                        <td><#if p4["collTime"]??><#if p4["collTime"]!='-1'> ${p4["collTime"]!} <#else>无</#if></#if></td>
                    </tr>
                    -->
                </table>
            </div>
            </#if>
        </div>
    </div>

<!-- jQuery -->
<script type="text/javascript" src="${rc.getContextPath()}/js/mawei/js/divscroll.js"></script>
<script type="text/javascript">
    $(function() {
        $('.gd-item').perfectScrollbar();
    });
</script>
</body>
</html>