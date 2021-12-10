<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>产业简介</title>
    <!--<link rel="icon" href="style/images/luof-icon.ico" type="image/x-icon" />-->
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/lf_zdcy/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/lf_zdcy/subject-tc.css"/>
</head>
<body>
<div class="container_fluid">
    <div class="layer_con">
        <div class="layer_cont flex flex_ac flex_jc">
            <div class="sub_tc_con">
                <!-------弹窗头部 start------->
                <#--<div class="sub_tc_con_h flex flex_ac flex_jb">-->
                    <#--<h5><img src="style/images/icon_cyjj.png"/>产业简介</h5>-->
                    <#--<a href="#" class="sub_close" title="关闭 "><img src="style/images/icon_video_close.png"/></a>-->
                <#--</div>-->
                <!-------弹窗头部 end------->
                <!-------弹窗内容 start------->
                <div class="sub_tc_con_b" style="height:calc(100% - 0px);">
                    <div class="sub_tc_cyjj">
                        ${industryinfo.industryDesc!}
                        <!--<p>正和沼气集中供气项目由江西正合生态农业有限公司投资建设，总占地面积198.3亩。</p>
                        <p>一期项目总投资约3005万元，申请中央预算内资金900万元，在2014年年底已经完成建设目标，可年处理有机废弃物1.22万t，日产沼气4100m3，年产沼气149.65万m3，主要用于新余市罗坊镇3000户居民生活用气；年产沼渣肥0.29万t，沼液肥0.66万t，截至目前实现3000户罗坊镇镇村居民安全稳定供气18个月。</p>
                        <p>二期项目于2016年4月正式开工建设, 总投资2000万元，完全投产后可年处理秸秆及猪粪有机废弃物6935t，日产沼气可达2970 m3，，年产沼气108.41万m3，主要用于新余市罗坊镇新增3000户居民生活用气及站内生产生活用气；年产沼渣肥0.49万t，沼液肥0.79万t。目前已进入调试产气阶段。</p>
                        <p>三期项目总投资8584万元，申请中央预算内资金3000万元，总占地面积约170亩。目前已完工投产。</p>-->
                    </div>
                </div>
                <!-------弹窗内容 end------->
            </div>
        </div>
    </div>
</div><!--container_fluid-->
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="style/js/jquery.nicescroll.js"></script>
<script type="text/javascript">
    $('.sub_tc_con_b').niceScroll({
        cursorcolor: "#ccc",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "5px", //像素光标的宽度
        cursorborder: "0", // 游标边框css定义
        cursorborderradius: "5px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });
</script>
</body>
</html>
