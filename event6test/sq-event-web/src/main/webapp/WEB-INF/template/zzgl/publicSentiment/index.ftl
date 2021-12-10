<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>舆情</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>
<body>
    <#--<div class="NorForm">-->
        <#--<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">-->
            <#--<tr>-->
                <#--<td class="LeftTd">-->
                    <#--<label class="LabName">-->
                        <#--<span>第一个链接获取到的sessionId：</span>-->
                    <#--</label>-->
                    <#--<div class="Check_Radio FontDarkBlue">-->
                        <#--<#if firstSessionId??>${firstSessionId}</#if>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
            <#--<tr>-->
                <#--<td>-->
                    <#--<label class="LabName">-->
                        <#--<span>第二个链接返回的json：</span>-->
                    <#--</label>-->
                    <#--<div class="Check_Radio FontDarkBlue" style="width:90%;">-->
                        <#--<#if secondJson??>${secondJson}</#if>-->
                    <#--</div>-->
                <#--</td>-->
            <#--</tr>-->
        <#--</table>-->
    <#--</div>-->
<#--<iframe id="test" width="100%" border="0" height="600" style="display:none;border:0px;" src="http://yqg.yingyan189.com/r/mobile/login?account=ffwgsy&password=123456"></iframe>-->
</body>
<script language="javascript" type="text/javascript">
    /***
     * 第一种方法，直接在页面跳转
     * @type {string}
     */
    <#--window.location.href="<#if publicSentimentURL??>${publicSentimentURL!''}</#if>";-->

    /***
     * 第二种方法：会有跨域
     */
//    $(function(){
//        requestUrl();
//    });
//
//    function requestUrl(){
//        $.ajax({
//            type : "POST",
//            url : 'http://yqg.yingyan189.com/r/mobile/login?account=ffwgsy&password=123456',
//            dataType : "jsonp",
//            success : function(data) {
//                var sessionId = data.data.sessionId;
//                var requestUrl2 = "http://yqg.yingyan189.com/view/modules/ThemeAnalysis/ThemeAnalysis.jsp?id=2c90801a5800c55301591f07c0a6730e&mod_key=opinionTheme&sessionId=" + sessionId;
//                window.location.href = requestUrl2;
//            }
//        });
//    }

    /**
     * 第三种方法
     */
    <#--window.open("<#if publicSentimentURL??>${publicSentimentURL!''}</#if>");-->

    /**
     * 第四种
     * @type {string}
     */
    <#--window.top.frames['mainFrame'].location = "<#if publicSentimentURL??>${publicSentimentURL!''}</#if>";-->

    /**
     * 第五种
     * @type {number}
     */
//    var i = 0;
//    var iframe = document.getElementById("test");
//    iframe.onload = function() {
//        if (i == 0)
//        {
//            iframe.src = "http://yqg.yingyan189.com/view/modules/ThemeAnalysis/ThemeAnalysis.jsp?id=2c90801a5800c55301591f07c0a6730e&mod_key=opinionTheme";
//        }else if (i == 1)
//        {
//            iframe.style.display = "block";
//        }
//        i = 1;
//    };

//    var i = 0;
//    var iframe = document.getElementById("test");
//    iframe.onload = function() {
//        if (i == 0)
//        {
//            window.location.href = "http://yqg.yingyan189.com/view/modules/ThemeAnalysis/ThemeAnalysis.jsp?id=2c90801a5800c55301591f07c0a6730e&mod_key=opinionTheme";
//        }
//        i = 1;
//    };


    <#--window.location.href = "http://yqg.yingyan189.com/view/modules/ThemeAnalysis/ThemeAnalysis.jsp?id=2c90801a5800c55301591f07c0a6730e&mod_key=opinionTheme&sessionId=<#if sessionId??>${sessionId}</#if>";-->
    <#--window.location.href = "http://yqg.yingyan189.com/view/modules/ThemeAnalysis/ThemeAnalysis.jsp?id=2c90801a59a537a7015a350b089b308f&mod_key=opinionTheme&sessionId=<#if sessionId??>${sessionId}</#if>";-->
    <#if DETAIL_URL??>
        window.location.href = "${DETAIL_URL!''}";
    </#if>
</script>
</script>
</html>
