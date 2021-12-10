<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>社区视频预览(live)水印</title>
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <#if needWaterMark>
        <script type="text/javascript" src="${uiDomain!''}/js/security/waterMark.js"></script>
    </#if>
    <style type="text/css">
        body {
            overflow-x : hidden;
            overflow-y : hidden;
        }
    </style>
</head>
<body style="padding: 0px;margin: 0px;">

    <iframe id="content" style="overflow: hidden;width: 100%;height: 100%;border: none;" src="${rc.getContextPath()}/zhsq/nanChang3D/index.jhtml?streamAddr=${red5!}&live=1&liveStream=${liveStream!false}&watermarkContent=${watermarkContent!''}" allow="autoplay" allowtransparency></iframe>

    <script type="text/javascript">
        var height = $(document).height();
    <#if needWaterMark>
        document.ready(function(){
            var x = document.body.style.backgroundColor='#fff';
            watermark({watermark_txt:"${watermarkContent!'未登录'}",watermark_cols:4,watermark_rows:5,watermark_x:-20,watermark_angle:5,watermark_x_space:150,watermark_y_space:80});
            var x = document.getElementsByClassName('panel-body');
            var i;
            for (i = 0; i < x.length; i++) {
                x[i].style.backgroundColor ='transparent';
            }
        });
    <#else>
        location.href = "${rc.getContextPath()}/zhsq/nanChang3D/index.jhtml?streamAddr=${red5!}&live=1&liveStream=${liveStream!false}";
    </#if>
        $("#content").css("height",(height));
    </script>
</body>
</html>