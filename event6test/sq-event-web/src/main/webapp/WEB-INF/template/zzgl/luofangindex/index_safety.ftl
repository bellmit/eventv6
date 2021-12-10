<!DOCTYPE html>
<html>
<head>
    <title>平安罗坊</title>
    <meta http-equiv="Access-Control-Allow-Origin" content="*" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/luofang/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/jquery.mCustomScrollbar.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/content.css"/>

    <script src="${uiDomain!''}/css/luofang_page_css/js/jquery.1.12.4.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/css/luofang_page_css/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/css/luofang_page_css/js/jquery.mCustomScrollbar.concat.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<div class="container_fluid">
    <div class="container_wrap container1">
        <canvas id="canvas"></canvas>
        <div>
            <div class="container_wrap container2">
                <div class="header">
                    <a href="javascript:;" class="logo"><img src="${uiDomain!''}/css/luofang_page_css/images/icon_logo.png"/></a>
                    <div class="tools">
                        <!-- /.ace-nav -->
                    </div>

                </div> <!--header-->
                <div class="container">
                    <div class="sidebar">
                        <div class="sidebar_header"><h4><a href="${UAMurl}/admin/luofang_admin">首页</a></h4></div>

                        <ul class="collapse">
                            <li>
                                <img src="${uiDomain!''}/css/luofang_page_css/images/icon_page_slide_bg.png"/>
                                <h5>平安罗坊</h5>
                                <img src="${uiDomain!''}/css/luofang_page_css/images/icon_page_slide_left.png"/>
                            </li>
                            <li>
                                <a href="javascript:;">执法记录</a>
                            </li>
                            <li>
                                <a href="javascript:;">GIS地图</a>
                            </li>
                        </ul>
                    </div><!--sidebar end-->
                    <div class="wrap_cont">
                        <div class="wrap_iframe">
						<#include "/zzgl/eventCase/indexPage/pop_eventCase.ftl" />

                            <div class="swiper-container">
                                <div class="swiper-wrapper">
                                    <!--执法记录-->
								<#include "/zzgl/eventCase/indexPage/list_eventCase.ftl" />
                                    <div class="swiper-slide">
                                        <iframe src="http://182.106.184.222:8007/zhsq_event/zhsq/map/arcgis/arcgis/toMapArcgisOfNewVersion.jhtml?homePageType=ARCGIS_STANDARD_HOME&accessToken=20180313190736&userCode=EC5ei5MXjYg " width="100%" height="100%" style="border: none;">
                                        </iframe>
                                    </div>
                                </div>
                                <!-- Add Pagination -->
                                <div class="swiper-pagination"></div>
                            </div>
                        </div>
                    </div>
                </div><!--wrap_cont end-->
            </div><!--container end-->
        </div><!--container-fluid-->
        <script>
            $(window).on('load resize', function(){
                var winH = $(window).height(), headerH = $('.header').height();
                $('.container').height(winH - headerH - 1);
                $(".cont_body, .cont_xf_text, .cont_layer_con").mCustomScrollbar();
            });
            $(function(){
                //	sidebar
                $('.collapse>li>a').on('click', function(){
                    $('.collapse').each(function(){
                        $(this).find('li>a').removeClass('on').children().remove();
                    });
                    $(this).addClass('on').parentsUntil('.sidebar').addClass('active').siblings().removeClass('active');
                });
            });

            //切换单页显示内容数量
            $('.cont_page_count').on('click', function(e){
                $(this).find('.cont_page_count_lists').fadeIn(200);
                e.stopPropagation();
                $('body').on('click', function(){
                    $('.cont_page_count_lists').fadeOut(200);
                });
            });
            $('.cont_page_count_lists>li').on('click', function(e){
                $(this).parent().fadeOut(200);
                e.stopPropagation();
            });

            //	nav
            $('.wrap_tools li a').on('click', function(){
                $(this).not('.tools_back').addClass('active').parent().siblings().children('a').removeClass('active');
            });
            //	tools
            $('.tools_user').on('click', function(e){
                $('.tools_user_items').fadeIn(200);
                e.stopPropagation();
                $('body').on('click', function(){
                    $('.tools_user_items').fadeOut(200);
                });
            });
            $('.tools_user_items li').on('click', function(e){
                $('.tools_user_items').fadeOut(200);
                e.stopPropagation();
            });

            //			内容切换
            var swiperI;
            var swiper = new Swiper('.swiper-container', {
//				hashNavigation: true,
                effect: 'cube',
                grabCursor: true,
                cubeEffect: {
                    shadow: true,
                    slideShadows: true,
                    shadowOffset: 20,
                    shadowScale: 0.94,
                },
                pagination: {
                    el: '.swiper-pagination',
                    clickable: true,
                },
            });
            $('.collapse>li').on('click', function(){
                swiperI = $(this).index() - 1;
                if(swiperI == -1){
                    return false;
                }else{
                    swiper.slideTo(swiperI);
                }
            });
        </script>
        <script>
            //宇宙特效
            "use strict";
            var canvas = document.getElementById('canvas'),
                    ctx = canvas.getContext('2d'),
                    w = canvas.width = window.innerWidth,
                    h = canvas.height = window.innerHeight,
                    hue = 217,
                    stars = [],
                    count = 0,
                    maxStars = 300;//星星数量

            var canvas2 = document.createElement('canvas'),
                    ctx2 = canvas2.getContext('2d');
            canvas2.width = 100;
            canvas2.height = 100;
            var half = canvas2.width / 2,
                    gradient2 = ctx2.createRadialGradient(half, half, 0, half, half, half);
            gradient2.addColorStop(0.025, '#CCC');
            gradient2.addColorStop(0.1, 'hsl(' + hue + ', 61%, 33%)');
            gradient2.addColorStop(0.25, 'hsl(' + hue + ', 64%, 6%)');
            gradient2.addColorStop(1, 'transparent');

            ctx2.fillStyle = gradient2;
            ctx2.beginPath();
            ctx2.arc(half, half, half, 0, Math.PI * 2);
            ctx2.fill();

            // End cache

            function random(min, max) {
                if (arguments.length < 2) {
                    max = min;
                    min = 0;
                }

                if (min > max) {
                    var hold = max;
                    max = min;
                    min = hold;
                }

                return Math.floor(Math.random() * (max - min + 1)) + min;
            }

            function maxOrbit(x, y) {
                var max = Math.max(x, y),
                        diameter = Math.round(Math.sqrt(max * max + max * max));
                return diameter / 2;
                //星星移动范围，值越大范围越小，
            }

            var Star = function() {

                this.orbitRadius = random(maxOrbit(w, h));
                this.radius = random(60, this.orbitRadius) / 8;
                //星星大小
                this.orbitX = w / 2;
                this.orbitY = h / 2;
                this.timePassed = random(0, maxStars);
                this.speed = random(this.orbitRadius) / 400000;
                //星星移动速度
                this.alpha = random(2, 10) / 10;

                count++;
                stars[count] = this;
            }

            Star.prototype.draw = function() {
                var x = Math.sin(this.timePassed) * this.orbitRadius + this.orbitX,
                        y = Math.cos(this.timePassed) * this.orbitRadius + this.orbitY,
                        twinkle = random(10);

                if (twinkle === 1 && this.alpha > 0) {
                    this.alpha -= 0.05;
                } else if (twinkle === 2 && this.alpha < 1) {
                    this.alpha += 0.05;
                }

                ctx.globalAlpha = this.alpha;
                ctx.drawImage(canvas2, x - this.radius / 2, y - this.radius / 2, this.radius, this.radius);
                this.timePassed += this.speed;
            }

            for (var i = 0; i < maxStars; i++) {
                new Star();
            }

            function animation() {
                ctx.globalCompositeOperation = 'source-over';
                ctx.globalAlpha = 0.5; //尾巴
                ctx.fillStyle = 'hsla(' + hue + ', 64%, 6%, 2)';
                ctx.fillRect(0, 0, w, h)

                ctx.globalCompositeOperation = 'lighter';
                for (var i = 1, l = stars.length; i < l; i++) {
                    stars[i].draw();
                };

                window.requestAnimationFrame(animation);
            }

            animation();
            //				弹窗控制
            $('.cont_layer_close').on('click', function(){
                $('.warp_layer_zjfu,.warp_layer_yyzj,.warp_layer_zhdj').css('visibility', 'hidden');
            });
            $('.cont_pic_layer_close').on('click', function(){
                $('.cont_layer_pic_wrapper').css('visibility', 'hidden');
            });
        </script>
</body>
</html>