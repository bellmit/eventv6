<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="overflow: hidden; text-align:center; padding:10px 0;" onload="initPlayUrl()">
<object type='application/x-vlc-plugin' codebase="${rc.getContextPath()}/pack/vlc.exe#version=2.2.1" id='vlc' events='True' width="720" height="540">
    <param name='mrl' value='' />
    <param name='volume' value='50' />
    <param name='autoplay' value='true' />
    <param name='loop' value='false' />
    <param name='fullscreen' value='false' />
</object>
</body>
<script type="text/javascript">
    var vdUrl ="rtsp://2.18.220.116:554/pag://172.16.65.12:7302:${zfyNo!''}:0:MAIN:TCP?cnid=12345&pnid=1&streamform=rtp";
    var vlc; // VLC对象
    var itemId;  // 播放列表中播放节目的id
    function initPlayUrl(){
        vlc=document.getElementById("vlc");
        // 添加播放地址
        //vlc.playlist.add(window.opener.vdUrl);
        // 播放
        // vlc.playlist.play();
        // 添加播放地址方式2 -- 推荐用下面的方法控制播放列表
        itemId= vlc.playlist.add(vdUrl);
        vlc.playlist.playItem(itemId);
    }

    //全屏
    function screenFull(){
        vlc.video.toggleFullscreen();
    }

</script>
</html>
