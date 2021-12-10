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
    <link href="${rc.getContextPath()}/css/mawei/css/index.css?v=2017" rel="stylesheet">

    <!-- JavaScript -->
    <script src="${rc.getContextPath()}/js/mawei/js/jquery-1.7.1.min.js"></script>

</head>
<body>

    <!--框-->
    <div class="main-mw" style="min-height: 200px;">
        <!--详情信息-->
        <div class="topmain fl_l">
            <div class="title">
                <img src="${rc.getContextPath()}/css/mawei/img/ico-dici-b.png">
                <h3><#if deviceInfo.deviceName?exists><#if deviceInfo.deviceName?length gt 18 >${deviceInfo.deviceName[0..18]}...<#else>${deviceInfo.deviceName}</#if></#if></h3>
            </div>
            <ul class="mss">
                <li>
                    <span>设备地址</span>
                    <p>${deviceInfo.deviceInstallAddress!}</p>
                </li>
                
                <li>
                    <span>采集时间</span>
                    <p>${collTime!}</p>
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
                    <span class="fl_l">设备状态</span>
                    <ul class="st-class clearfix">
                    <#if dataList?? && dataList?size gt 0><#list dataList as deviceCollectData>
	                     <#if deviceCollectData.collectItemCode=="device_status"> 
	                     <#if deviceCollectData.collectItemValue?? && deviceCollectData.collectItemValue=="0"><li class="fc-blue">空闲</li>
	                     <#elseif  deviceCollectData.collectItemValue?? && deviceCollectData.collectItemValue=="1"><li class="fc-blue">占用  </li>       
	                    </#if>
	                   </#if> 
                   </#list></#if>
                        
                    </ul>
                    <p class="fl_l">采集时间：${collTime!}</p>
                </li>
                
            </ul>
        </div>
    </div>

<!-- jQuery -->
<script type="text/javascript" src="js/divscroll.js"></script>
<script type="text/javascript">
    $(function() {
        $('.gd-item').perfectScrollbar();
    });
</script>
</body>
</html>