<!DOCTYPE html>
<html style="margin: 0;padding: 0;width:100%;height:100%">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="x-ua-compatible" content="IE=edge" />
    <meta
      name="viewport"
      content="width=device-width, height=device-height, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
    />
    <title>云眼视频</title>
    <link  rel="stylesheet" href="${uiDomain!''}/web-assets/common/js/aliplayer/2.8.8/aliplayer-min.css" />
    <script type="text/javascript"  charset="utf-8" src="${uiDomain!''}/web-assets/common/js/aliplayer/2.8.8/aliplayer-min.js"    ></script>
    <style>
    .prism-player{margin: 0;padding: 0;}
    
    </style>
  </head>
  <body style="margin: 0;padding: 0;width:100%;height:100%;overflow: hidden;">
    <div class="prism-player" id="player-con"></div>

    <script>
      var player = new Aliplayer(
        {
          id: "player-con",
          width:'100%',//(document.body.clientWidth+"px"),
          height: '100%',//(document.body.clientHeight+"px"),
          autoplay: true,
          isLive: true,
          playsinline: true,
          preload: true,
          controlBarVisibility: "hover",
          useH5Prism: true,
          // 跨域测试请修改一下播放地址
          source: "${url}"
        },
        function (player) {
          console.log("播放器创建了。");
          setTimeout(function(){
		 		console.log("播放器播放了。");
		 		player.play();
			},3000);
		}
        
      );
    </script>
  </body>
</html>
