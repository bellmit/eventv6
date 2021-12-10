<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>矛盾纠纷弹窗</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-5.4.1/package/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/anfu/css/event-mask.css"/>

    <script>
        // 页面缩放
        var winW, winH, whdef, rem;
        function fullPage() { //将页面等比缩放
            winW = window.outerWidth,
                winH = window.outerHeight,
                whdef = 100 / 1920,
                rem = winW * whdef, // 以默认比例值乘以当前窗口宽度,得到该宽度下的相应FONT-SIZE值
                document.querySelector('html').style.fontSize = rem + 'px';
        }
        fullPage();
        window.onresize = fullPage;
    </script>
    <style>
        .datagrid-mask {
            z-index:51;
            position: absolute;
            left: 0;
            top: 0;
            opacity: 0.3;
            background: #ccc;
        }

        .swiper-slide>img {
            height: auto;
        }

        .maed-c-left {
            height: 4.5rem;
        }

        .maed-c-right {
            height: 4.5rem;
        }

        .legal-mcccr-center {
            height: 3.21rem;
        }
        .lmcc-mask-box {
            height: 90%;
            padding-top:30px;
        }
    </style>
</head>
<body>
<div class="mask-container mask-container1 bs plr40 prr40">
    <div class="sj-c-details sj-c-details1 bs show">
        <div class="maed-content">
            <div class="clearfix prr30">
                <div class="maed-c-left niceitem fl">
                    <p class="me-cl-title">${disputeMediation.disputeEventName}</p>
                    <p class="me-cl-small-title">[
                        <#if disputeType_9x??>
                            <#list disputeType_9x as l>
                                <#if disputeMediation.disputeType2??>
                                    <#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
                                </#if>
                            </#list>
                        </#if>
                    ]</p>
<#--                    <p class="me-cl-title">事件名称事件名称事件名称事件名称事件名称事件名称事件名称事件名称事件名称</p>-->
<#--                    <p class="me-cl-small-title">[城市管理类-事件-市容环境-私搭乱建] </p>-->
                    <div class="maed-cl-top bs niceitem">
                        <p class="maed-clt-title">于<span>${disputeMediation.happenTime?string('yyyy-MM-dd HH:mm:ss')}</span>在<span>${disputeMediation.happenAddr}</span>发生：
                        </p>
                        <p class="maed-clt-text">
                            ${disputeMediation.disputeCondition}
                        </p>
                    </div>
                    <div class="maed-cl-bottom niceitem mtr15 bs">
                        <ul class="maed-clb-list bs">
                            <li class="clearfix">
                                <div class="maed-w100 fl clearfix">
                                    <p>所属网格：</p>
                                    <p>${disputeMediation.gridName}</p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fr clearfix">
                                    <p>紧急程度：</p>
                                    <p>
                                        <#if disputeMediation.warningLevel??>
                                            <#if disputeMediation.warningLevel== '01'>
                                                一级（有可能引发群体性事件等严重影响社会稳定的事件纠纷）
                                            <#elseif disputeMediation.warningLevel == '02'>
                                                二级（有可能引发群体闹事苗头的事件纠纷）
                                            <#elseif disputeMediation.warningLevel == '03'>
                                                三级（有可能引发群体越级非正常访的事件纠纷）
                                            </#if>
                                        </#if>
                                    </p>
                                </div>
                                <div class="maed-w50 fl clearfix">
                                    <p>事件编号：</p>
                                    <p>${disputeMediation.mediationCode}</p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fr clearfix">
                                    <p>联系人员：</p>
                                    <p></p>
                                </div>
                                <div class="maed-w50 fl clearfix">
                                    <p>影响范围：</p>
                                    <p>
                                        <#if disputeMediation.disputeScale??>
                                            <#if disputeMediation.disputeScale== '1'>
                                                个体性事件
                                            <#elseif disputeMediation.disputeScale == '2'>
                                                群体性事件
                                            <#elseif disputeMediation.disputeScale == '3'>
                                                重大群体性事件
                                            </#if>
                                        </#if>
                                    </p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fr clearfix">
                                    <p>信息来源：</p>
                                    <p></p>
                                </div>
                                <div class="maed-w50 fl clearfix">
                                    <p>涉及人员：</p>
                                        <p>
                                            <#if disputeMediation.involveNum??>
                                                ${disputeMediation.involveNum}人
                                            </#if>
                                        </p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fr clearfix">
                                    <p>当前状态：</p>
                                    <p>
                                        <#if disputeMediation.disputeStatus??>
                                            <#if disputeMediation.disputeStatus== '2'>
                                                未化解
                                            <#elseif disputeMediation.disputeStatus == '3'>
                                                已化解
                                            </#if>
                                        </#if>
                                    </p>
                                </div>
                                <div class="maed-w50 fl clearfix">
                                    <p>采集渠道：</p>
                                    <p></p>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="maed-c-right fr bs">
                    <div class="legal-mcccr-top bs">
                        <ul class="lmcccrt-list clearfix">
                            <li class="attrLabel active" labelType="img" total="0" isLoad="0">
                                <a href="javascript:void(0);">
                                    <p>图片(0)</p>
                                </a>
                            </li>
                            <li class="attrLabel" labelType="sound" total="0" isLoad="0">
                                <a href="javascript:void(0);" >
                                    <p>视频(0)</p>
                                </a>
                            </li>
                            <li class="attrLabel" labelType="video" total="0" isLoad="0">
                                <a href="javascript:void(0);">
                                    <p>录音(0)</p>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="attrSwiper legal-mcccr-center bs" id="img_swiper_div">
                        <div class="swiper-container" id="img_swiper">
                            <div class="swiper-wrapper" id="img_swiper_content">
                                <div class="swiper-slide swiper-slide-active">
                                    <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                </div>

                            </div>
                            <div class="swiper-button-next1" id="img_swiper_next"></div>
                            <div class="swiper-button-prev1" id="img_swiper_prev"></div>
                        </div>
                    </div>
                    <div class="attrSwiper legal-mcccr-center bs" id="sound_swiper_div" style="display:none">
                        <div class="swiper-container" id="sound_swiper">
                            <div class="swiper-wrapper" id="sound_swiper_content">
                                <div class="swiper-slide">
                                    <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                </div>
                            </div>
                            <div class="swiper-button-next1" id="sound_swiper_next"></div>
                            <div class="swiper-button-prev1" id="sound_swiper_prev"></div>
                        </div>
                    </div>
                    <div class="attrSwiper legal-mcccr-center bs" id="video_swiper_div" style="display:none">
                        <div class="swiper-container" id="video_swiper">
                            <div class="swiper-wrapper" id="video_swiper_content">
                                <div class="swiper-slide">
                                    <img src="${uiDomain!''}/web-assets/_big-screen/tianshui/images/mask/thing-pic.png">
                                </div>
                            </div>
                            <div class="swiper-button-next1" id="video_swiper_next"></div>
                            <div class="swiper-button-prev1" id="video_swiper_prev"></div>
                        </div>
                    </div>
                    <div class="legal-mcccr-bottom">
                        <div class="lmcccrb-top clearfix">
                            <div class="lmcccrb-top-left fl clearfix" style="margin-left:30%">
                                <i></i>
                                <p>暂无数据</p>
                            </div>

                        </div>
                        <div class="lmcccrb-page">
                            <p>（ <span>0</span> / 0 ）</p>
                        </div>
                    </div>
                </div>
            </div>
<#--            <div class="event-details-btn">-->
<#--                <a href="javascript:void(0);">查看周边摄像头</a>-->
<#--                <a href="javascript:void(0);">查看周边资源</a>-->
<#--            </div>-->
        </div>
        <a href="javascript:void(0);" class="lmcc-mask-open lmcc-mask-open1">
            <i class="lmccm-open"></i>
            <p>处</p>
            <p>理</p>
            <p>记</p>
            <p>录</p>
        </a>
        <div class="lmcc-mask-box lmcc-mask-box1">
            <a href="javascript:void(0);" class="lmcc-mask-open">
                <i class="lmccm-close"></i>
                <p>处</p>
                <p>理</p>
                <p>记</p>
                <p>录</p>
            </a>
            <div class="lmcc-mask bs">
                <div class="lmcc-mask-content lmcc-mask-content1">
                    <div class="det-links-des clearfix">
                        <ul class="flex layer_aj_bt_n fr">
                            <li class="clearfix fr">
                                <div class="aj_ks aj_ks_blue fl">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <p>当前环节</p>
                            </li>
                            <li class="flex flex-ac fl">
                                <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <p>历史环节</p>
                            </li>
                        </ul>
                    </div>
                    <div class="layer_aj_bt_b bs clearfix">
                        <p class="layer_aj_bt_b1">办理环节</p>
                        <p class="layer_aj_bt_b2">办理人/办理时间</p>
                        <p class="layer_aj_bt_b3">办理意见</p>
                    </div>
                    <div class="layer_aj_bt_s layer_aj_bt_s1">
                        <div class="layer_aj_bt_line"></div>
                        <ul class="layer_aj_bt_items">

                            <#if disputeFlowInfos??>
                                <#list disputeFlowInfos as item>
                                    <#if item_index==0>
                                        <li class="flex flex-ac">
                                            <div class="aj_items_i aj_items_it-whirte">${item.handleLinkName}</div>
                                            <div class="aj_ks aj_ks_blue">
                                                <div class="aj_ks1"></div>
                                                <div class="aj_ks2"></div>
                                            </div>
                                            <div class="aj_items_t2 flex">
                                                <p class="aj_items_t aj_items_t_green bs">${item.handlerName}</p>
                                            </div>
                                            <#if item.handleOpinion??>
                                                <p class="aj_items_t aj_items_t_result aj_items_tr_red">${item.handleOpinion}</p>
                                            </#if>
                                        </li>

                                    <#else >
                                        <li class="flex flex-ac">
                                            <h5 class="aj_items_h aj_items_h_green">${item.handleLinkName}</h5>
                                            <div class="aj_ks aj_ks_gray">
                                                <div class="aj_ks1"></div>
                                                <div class="aj_ks2"></div>
                                            </div>
                                            <div class="aj_items_t2 flex">
                                                <p class="aj_items_t aj_items_t_green bs">${item.handlerName}(${item.handleUnitName})<span>
                                                    </span><br><span>办理时间</span>：${item.handleTime?string("yyyy-MM-dd")}</p>
                                            </div>
                                            <#if item.handleOpinion??>
                                                <p class="aj_items_t aj_items_t_result aj_items_tr_blue">${item.handleOpinion}</p>
                                            </#if>

                                        </li>


                                    </#if>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${uiDomain}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${uiDomain}/web-assets/plugins/swiper-5.4.1/package/js/swiper.min.js"></script>
<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
<#include "/component/ComboBox.ftl">
<script>
    // 滚动条美化
    $('.niceitem').niceScroll({
        cursorcolor: "#185ab2",
        cursoropacitymax: 1,
        cursorwidth: "4px",
        autohidemode: false,
        // background: "#06325c",
        cursorborder: "none",
        cursorborderradius: "0",
    })
    //事件详情图片轮番
    var swiper;
    function picSwiper() {
        var swiI = 1;
        swiper = new Swiper('.swiper-container', {
            allowTouchMove: false,
            slidesPerView : 1,
            navigation: {
                nextEl: '.swiper-button-next1',
                prevEl: '.swiper-button-prev1',
            },
        });
        //图片轮番的页数
        $('.swiper-button-next1, .swiper-button-prev1').on('click', function () {
            swiI = swiper.activeIndex;
            console.log(swiper.activeIndex);
            swiI += 1;
            $('.lmcccrb-page p span').text(swiI);
        });
    }
    picSwiper();
    //事件处理记录
    $(".layer_aj_bt_s").niceScroll({
        cursorcolor: "#185ab2",
        cursoropacitymax: 1,
        cursorwidth: "4px",
        autohidemode: false,
        cursorborder: "none",
        cursorborderradius: "0",
    })
    $('.sj-c-details>.lmcc-mask-open').click(function () {
        $(this).hide();
        $(".maed-cl-top").getNiceScroll().hide();
        $(".maed-clb-list").getNiceScroll().hide();
        $('.lmcc-mask-box').show();
        $('.lmcc-mask').animate({ "right": "0" }, 100, function () {
            $('.lmcc-mask-box').css({ "overflow": "unset" });
            $('.layer_aj_bt_line').height($('.layer_aj_bt_items').height() + 38);
            $(".layer_aj_bt_s").getNiceScroll().resize();
        });
    });
    $('.lmcc-mask-box>.lmcc-mask-open').click(function () {
        $('.lmcc-mask-box').css({ "overflow": "hidden" });
        $('.lmcc-mask').animate({ "right": "-100%" }, 100, function () {
            $('.lmcc-mask-box').hide();
            $(".layer_aj_bt_s").getNiceScroll().resize();
            $(".maed-cl-top").getNiceScroll().show();
            $(".maed-clb-list").getNiceScroll().show();
            $('.sj-c-details>.lmcc-mask-open').show();
        });
    });
    // 表格的筛选操作
    $('.fi-s-btn').click(function(){
        $(this).toggleClass('active').siblings().removeClass('active');
        $(this).siblings('.ts-lti-box').slideToggle(200);
    })
    $('.ts-ltib-profile .ts-ltibi-contet').click(function(){
        var text =  $(this).text();
        $(this).parents('.ts-lti-box').siblings('a').children('p').text(text);
        $(this).parents('.ts-lti-box').siblings('a').removeClass('active');
        $('.ts-lti-box').slideUp(200);
    })

    $('.attrLabel').click(function(){
        $(this).addClass('active').siblings().removeClass('active');
        $('.attrSwiper').hide();
        var labelType=$(this).attr('labelType');
        $('#'+labelType+'_swiper_div').show();
        $('#'+labelType+'_release').show();
        if($(this).attr('isLoad')=='0'){
            var swiper;
            swiper = new Swiper('#'+labelType+'_swiper', {
                allowTouchMove: false,
                navigation: {
                    nextEl: '#'+labelType+'_swiper_next',
                    prevEl: '#'+labelType+'_swiper_prev',
                },
            });
            //图片轮番的页数
            var attrTitleList={};
            var attrTimeList={};
            $('#'+labelType+'_swiper_next, #'+labelType+'_swiper_prev').on('click', function () {
                var swiI = swiper.activeIndex;
                $('#'+labelType+'_title').html(attrTitleList[labelType+'_'+swiI]);
                $('#'+labelType+'_time').html(attrTimeList[labelType+'_'+swiI]);
                swiI += 1;
                $('.lmcccrb-page p span:visible').eq(0).text(swiI);
            });
            if(parseInt($(this).attr('total'))<2){
                $('#'+labelType+'_swiper_next').hide();
                $('#'+labelType+'_swiper_prev').hide();
            }
            if(parseInt($(this).attr('total'))==0){
                $('#'+labelType+'_release').html('<div class="lmcccrb-page" style="margin-top: 0.30rem;"><p>暂无数据</p></div>');
            }else{
                $('#'+labelType+'_title').html(attrTitleList[labelType+'_0']);
                $('#'+labelType+'_time').html(attrTimeList[labelType+'_0']);
            }
            $(this).attr('isLoad','1');
        }
    });
</script>
</body>
</html>