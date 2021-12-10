<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>detail01-详情页</title>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/dispute/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/detail_css/css/detail.css"/>
    <script src="${rc.getContextPath()}/dispute/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${rc.getContextPath()}/dispute/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<style>
    i.spot-xh {
        display: inline-block;
        color: #f54952;
        padding-right: 5px; /* vertical-align: middle; */
    }

    .spot-xhs {
        padding-left: 10px
    }

    .spot-xhl {
        padding-left: 20px
    }

    .det-con2 > li.fr {
        float: none !important;
    }

    .parent_div {
        width: 92%;
    }

    .panel-tool .panel-tool-max {
        display: unset !important;
    }

    .baseDispute {
        min-width: 96px;
    }

    .det-nav-wrap > li > a:hover, .det-nav-wrap > li > .active {
        background-color: #5294e8 !important;
    }

    .fw-det-tog-top > h5 > i {
        background-color: #5294e8 !important;
    }

    .det-btn-wrap > .det-bg-gray {
        background-color: #5294e8 !important;
        color: rgb(255, 255, 255) !important
    }

    .p-right {
        text-align: right
    }

    .det-con-tt {
        min-width: 100px;
        padding-right: 10px;
        text-align: right;
    }
    .h300{height: 300px!important;}
</style>
<body class="xiu-body">
<div class="container-detail flex">
    <div class="det-nav">
        <ul class="det-nav-wrap">
            <li>
                <a href="#1" class="active flex flex-ac flex-jc"><i class="det-dis-none"></i>纠纷基本信息</a>
                <!--要使用图标i时，把det-dis-none类去掉-->
            </li>
            <li>
                <a href="#2" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>主要当事人信息</a>
            </li>
            <li>
                <a href="#3" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>化解信息</a>
            </li>
            <li>
                <a href="#4" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>包案领导人信息</a>
            </li>
            <li>
                <a href="#5" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>化解责任人信息</a>
            </li>
            <li>
                <a href="#6" class="flex flex-ac flex-jc"><i class="det-dis-none"></i>流程信息</a>
            </li>
        </ul>
    </div>
    <input type="hidden" id="mediationId" name="mediationId" value="<#if disputeMediation.mediationId??>${disputeMediation.mediationId}</#if>">
    <div class="det-wrapper flex1">
        <div class="fw-det-tog-top mlr20" id="1">
            <h5><i></i>基本信息</h5>
            <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
        </div>
        <div class="fw-det-toggle">
            <ul class="det-con2 clearfix">
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>事件编号：</p>
                    <p><#if disputeMediation.mediationCode??>${disputeMediation.mediationCode}</#if></p>
                </li>
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>事件名称：</p>
                    <p><#if disputeMediation.disputeEventName??>${disputeMediation.disputeEventName}</#if></p>
                </li>
                <li class="xw-com3 fr">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>事件类别：</p>
                    <p>
                        <#if disputeType_9x??>
                            <#list disputeType_9x as l>
                                <#if disputeMediation.disputeType2??>
                                    <#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
                                </#if>
                            </#list>
                        </#if>

                    </p>
                </li>
            </ul>
            <ul class="det-con2 clearfix">
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>事件规模：</p>
                    <p>
                        <#if disputeScaleDC??>
                            <#list disputeScaleDC as l>
                                <#if disputeMediation.disputeScale??>
                                    <#if (l.dictGeneralCode?string==disputeMediation.disputeScale?string)>${l.dictName}</#if>
                                </#if>
                            </#list>
                        </#if>
                    </p>
                </li>
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs  "><i class="spot-xh"></i>发生日期：</p>
                    <p><#if disputeMediation.happenTime??>${disputeMediation.happenTime?string('yyyy-MM-dd')}</#if></p>
                </li>
                <li class="xw-com3 fr">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>化解时限：</p>
                    <p><#if disputeMediation.mediationDeadlineStr??>${disputeMediation.mediationDeadlineStr}</#if></p>
                </li>
            </ul>
            <ul class="det-con2 clearfix">
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>涉及单位：</p>
                    <p><#if disputeMediation.involvedOrgName??>${disputeMediation.involvedOrgName}</#if></p>
                </li>
                <li class="xw-com3">
                    <p class="det-con-tt spot-xhs  "><i class="spot-xh"></i>涉及人数：</p>
                    <p><#if disputeMediation.involveNum??>${disputeMediation.involveNum}（人）</#if></p>
                </li>
                <li class="xw-com3 fr">
                    <p class="det-con-tt spot-xhs "><i class="spot-xh"></i>发生地址：</p>
                    <p><#if disputeMediation.happenAddr??>${disputeMediation.happenAddr}</#if></p>
                </li>
            </ul>
            <ul class="det-con2 clearfix">
                <li class="xw-com3 fr">
                    <p class="det-con-tt spot-xhs">预警等级：</p>
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
                </li>
            </ul>
            <ul class="det-con2 clearfix">
                <li class="xw-com1">
                    <p class="det-con-tt spot-xhs"><i class="spot-xh"></i>事件简述：</p>
                    <p><#if disputeMediation.disputeCondition??>${disputeMediation.disputeCondition}</#if></p>
                </li>
            </ul>
        </div>

        <div class="det-section">
            <div class="fw-det-tog-top" id="2">
                <h5><i></i>主要当事人信息</h5>
                <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
            </div>
            <div class="fw-det-toggle">
                <div class="det-box mt10" id="main_people" style="margin-left:45%;color: #f08750;font-size:18px;">
                </div>
            </div>
        </div>
        <div class="det-section">
            <div class="fw-det-tog-top" id="3">
                <h5><i></i>化解信息</h5>
                <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
            </div>
            <div class="fw-det-toggle">
                <ul class="det-con2 clearfix">
                    <li class="xw-com3">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解方式：</p>
                        <p>
                            <#if mediationTypeDC_9x??>
                                <#list mediationTypeDC_9x as l>
                                    <#if disputeMediation.mediationType??>
                                        <#if (l.dictGeneralCode?string==disputeMediation.mediationType?string)>${l.dictName}</#if>
                                    </#if>
                                </#list>
                            </#if>
                        </p>
                    </li>
                    <li class="xw-com3">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人姓名：</p>
                        <p><#if disputeMediation.mediator??>${disputeMediation.mediator}</#if></p>
                    </li>
                    <li class="xw-com3 fr">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人电话：</p>
                        <p><#if disputeMediation.mediationTel??>${disputeMediation.mediationTel}</#if></p>
                    </li>
                </ul>
                <ul class="det-con2 clearfix">
                    <li class="xw-com3">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解组织：</p>
                        <p><#if disputeMediation.mediationOrgName??>${disputeMediation.mediationOrgName}</#if></p>
                    </li>
                    <li class="xw-com3 fr">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解是否成功：</p>
                        <p><#if disputeMediation.isSuccess??><#if disputeMediation.isSuccess=="1">是<#else>否</#if></#if></p>
                    </li>
                    <li class="xw-com3" style="width:100%">
                        <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解情况：</p>
                        <p><#if disputeMediation.mediationResult??>${disputeMediation.mediationResult}</#if></p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="det-section">
            <div class="fw-det-tog-top" id="4">
                <h5><i></i>包案领导人信息</h5>
                <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
            </div>
            <#if leaders??>
                <#list leaders as item>
                    <div class="fw-det-toggle">
                        <ul class="det-con2 clearfix">
                            <li class="xw-com3">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>包案领导人单位：</p>
                                <p>${item.mediationOrgName}</p>
                            </li>
                            <li class="xw-com3">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>包案领导人姓名：</p>
                                <p>${item.mediator}</p>
                            </li>
                            <li class="xw-com3">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>包案领导人职务：</p>
                                <p>${item.mediationPost}</p>
                            </li>
                            <li class="xw-com3 fr">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>包案领导人手机号：</p>
                                <p>${item.mediatorTel}</p>
                            </li>
                        </ul>
                    </div>
                </#list>
            </#if>
        </div>
        <div class="det-section">
            <div class="fw-det-tog-top" id="5">
                <h5><i></i>化解责任人信息</h5>
                <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
            </div>
            <#if persons??>
                <#list persons as item>
                    <div class="fw-det-toggle">
                        <ul class="det-con2 clearfix">
                            <li class="xw-com3">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人单位：</p>
                                <p>${item.mediationOrgName}</p>
                            </li>
                            <li class="xw-com3">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人姓名：</p>
                                <p>${item.mediator}</p>
                            </li>
                            <li class="xw-com3 ">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人职务：</p>
                                <p>${item.mediationPost}</p>
                            </li>
                            <li class="xw-com3 fr">
                                <p class="det-con-tt spot-xhs baseDispute p-right"><i class="spot-xh"></i>化解责任人手机号：</p>
                                <p>${item.mediatorTel}</p>
                            </li>
                        </ul>
                    </div>
                </#list>
            </#if>
        </div>
        <div class="det-section">
            <div class="fw-det-tog-top" id="6">
                <h5><i></i>流程信息</h5>
                <a href="##"><img src="${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif"> </a>
            </div>
            <!--处理环节-->
                <div class="fw-det-toggle">
                    <div class="">
                        <div class="det-section det-bor-bot-none">
                            <div class="det-links-des">
                                <ul class="layer_aj_bt_n">
                                    <li>
                                        <div class="aj_ks aj_ks_yellow">
                                            <div class="aj_ks1"></div>
                                            <div class="aj_ks2"></div>
                                            <div class="aj_ks3"></div>
                                        </div>
                                        <p>当前环节</p>
                                    </li>
                                    <li>
                                        <div class="aj_ks aj_ks_gray">
                                            <div class="aj_ks1"></div>
                                            <div class="aj_ks2"></div>
                                        </div>
                                        <p>历史环节</p>
                                    </li>
                                </ul>
                            </div>
                            <div class="layer_aj_bt_b">
                                <p class="layer_aj_bt_b1">办理环节</p>
                                <p class="layer_aj_bt_b2">办理人/办理时间</p>
                            </div>
                            <div class="layer_aj_bt_s h300">
                                <div class="layer_aj_bt_line"></div>
                                    <ul class="layer_aj_bt_items">
                                        <#if disputeFlowInfos??>
                                            <#list disputeFlowInfos as item>
                                                <#if item_index == 0>
                                                    <li>
                                                        <h5 class="aj_items_h aj_items_h_blue">${item.handleLinkName}</h5>
                                                        <div class="aj_ks aj_ks_yellow">
                                                            <div class="aj_ks1"></div>
                                                            <div class="aj_ks2"></div>
                                                            <div class="aj_ks3"></div>
                                                        </div>
                                                        <p class="aj_items_t aj_items_t_yellow">${item.handlerName}<br><span>办理时间</span>：${item.handleTime?string("yyyy-MM-dd")}</p>
                                                    </li>
                                                <#else >
                                                    <li>
                                                        <h5 class="aj_items_h aj_items_h_green">${item.handleLinkName}</h5>
                                                        <div class="aj_ks aj_ks_gray">
                                                            <div class="aj_ks1"></div>
                                                            <div class="aj_ks2"></div>
                                                        </div>
                                                        <p class="aj_items_t aj_items_t_green">${item.handlerName}<br><span>办理时间</span>：${item.handleTime?string("yyyy-MM-dd")}</p>
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
        <#if !(needClose??)>
            <div class="det-section">
                <div class="det-btn-wrap mt25 mb10">
                    <a href="javascript:void(0);" class="det-btn det-bg-gray" onclick="javascript:cancl();">关闭</a>
                </div>
            </div>
        </#if>
    </div>
</div><!--container-fluid-->
<script>
    var swiper;
    $(window).on('load resize', function () {
        setTimeout(function () {
            $('.det-ann-pic').width($('.det-section').width() * 0.3);
            swiper = new Swiper('.swiper-container', {
                allowTouchMove: false,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
            });
        }, 200);
    });
    $(function () {
        var src;
        $('.fw-det-tog-top').on('click', function () {
            $(this).siblings('.fw-det-toggle').toggle(300);
            src = $(this).find('img').attr('src');
            if (src == '${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif') {
                $(this).find('img').attr("src", "${rc.getContextPath()}/style/images/icon_fw_detail_down.gif");
            } else {
                $(this).find('img').attr("src", "${rc.getContextPath()}/style/images/icon_fw_detail_tog.gif");
            }
        });

        $('.det-nav-wrap').on('click', 'li a', function () {
            $(this).addClass('active').parent().siblings().children().removeClass('active');
        });
        //附件关闭按钮
        $('.pic_content').each(function (index) {
            $(this).hover(function () {
                $('.pic_content').eq(index).find('.off_btn').removeClass('dn');
            }, function () {
                $('.pic_content').eq(index).find('.off_btn').addClass('dn');
            });
        });
    });
</script>
<script src="${rc.getContextPath()}/dispute/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
<script>
    $(function () {
        //获取人员信息
        <#if involvedPeoples??>
        <#list involvedPeoples as l>
        detailMainPeople("${l.hashId!''}");
        </#list>
        </#if>
        $(window).on('load resize', function () {
            $winH = $(window).height();
            $topNav = $('.top-nav').height();
            $('.det-wrapper').height($winH - $topNav);
            $(".fw-container-detail,.det-wrapper,.layer_aj_bt_s").niceScroll({
                cursorcolor: "rgba(0, 0, 0, 0.3)",
                cursoropacitymax: 1,
                touchbehavior: false,
                cursorwidth: "4px",
                cursorborder: "0",
                cursorborderradius: "4px",
                //  autohidemode: false //隐藏式滚动条
            });
            $('.layer_aj_bt_s').height($(window).height() - 180);
            $(".layer_aj_bt_s").getNiceScroll().resize();
        });

        var deparH = $('.layer_aj_bt_items').height();
        $('.layer_aj_bt_line').height(deparH + 24);
        $('.layer_aj_bt_s').css("outline");
    });

    function detailMainPeople(ipId) {
        if (ipId != null && ipId != "") {
            $.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/involvedPeople/detail.jhtml?hashId=' + ipId + '',
                dataType: 'json',
                success: function (data) {
                    var main_people_html = '<div style="border:1px #e5e5e5 solid;margin-top:5px"><ul class="det-con2 clearfix" style="margin-top:15px">' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>姓名：</p>' +
                        '<p>' + checkData(data.involvedPeople.name) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right" style="width: 81px"><i class="spot-xh"></i>性别：</p>' +
                        '<p>' + checkData(data.involvedPeople.sexName) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3 fr">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>证件类型：</p>' +
                        '<p>' + checkData(data.involvedPeople.cardTypeName) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>证件号码：</p>' +
                        '<p>' + checkData(data.involvedPeople.idCardHS) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right" style="width: 81px"><i class="spot-xh"></i>民族：</p>' +
                        '<p>' + checkData(data.involvedPeople.nationName) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3 fr">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>人员类别：</p>' +
                        '<p>' + checkData(data.involvedPeople.peopleTypeName) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh">&nbsp;</i>学历：</p>' +
                        '<p>';
                    if (data.involvedPeople.eduName) {
                        main_people_html += data.involvedPeople.eduName;
                    }
                    main_people_html += '</p>' +
                        '</li>' +
                        '<li class="xw-com3">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>居住详址：</p>' +
                        '<p>' + checkData(data.involvedPeople.homeAddr) + '</p>' +
                        '</li>' +
                        '<li class="xw-com3 fr" style="float: none;">' +
                        '<p class="det-con-tt spot-xhs p-right"><i class="spot-xh"></i>联系电话：</p>' +
                        '<p>' + (data.involvedPeople.tel == null ? "暂无" : data.involvedPeople.tel) + '</p>' +
                        '</li>' +
                        '</ul></div>';
                    $('#main_people').before(main_people_html);
                }
            });
        }
    }

    function cancl() {
        parent.closeMaxJqueryWindow();
    }

    function checkData(obj){
        if(obj == null || !obj || obj == 'undefined' || obj == ''){
            return '';
        }else{
            return obj;
        }
    }
</script>
</body>
</html>
