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
                   			 <div class="sub_tc_layer_con">
								<ul class="sub_tc_layer_co">
									<li>建设内容： <span>${industryinfo.projectContent!}</span></li>
									<li>单位： <span>${industryinfo.unit!}</span></li>
									<li>规模： <span>${industryinfo.scale!}</span></li>
									<li>总投资（万元）： <span>${industryinfo.investAll!}</span></li>
									<li>已完成投资（万元）： <span>${industryinfo.investCompleted!}</span></li>
									<li>开工时间： <span>${industryinfo.beginTime?string('yyyy月MM日')}</span></li>
									<li>竣工时间： <span>${industryinfo.endTime?string('yyyy月MM日')}</span></li>
									<li>预期效益： <span>${industryinfo.expectedPer!}</span></li>
									<li>项目进度： <span>${industryinfo.projectProgress!}</span></li>
								</ul>
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
