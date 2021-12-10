<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>矛盾纠纷弹窗(案件详情)</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/mask/event-mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/sp-v2.css" />
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/sp-v2-tc.css" />
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
</head>
<body>
    <div class="h-x plr25 prr25 pt tc-main  height-p100">
        <div class="seven">
            <p class="font-size-0 mtr20">
                <span class="active mrr20" da-name="sev-span01"><i>1.</i>基本清单</span>
                <span class="mrr20" da-name="sev-span02"><i>2.</i>当事人信息（多人）</span>
                <span class="mrr20" da-name="sev-span03"><i>3.</i>化解情况</span>
                <span class="mrr20" da-name="sev-span04"><i>4.</i>流程信息</span>
            </p>
            <!-- 人物内容 start -->
            <div class="mtr5 p1-main-ren ptr15 pbr15 posi-rela">
                <!-- 基本清单 start -->
                <div class="sev-span01 posi-rela ">
                    <!-- top start -->
                    <div class="sev-span01-top plr20 prr20 ptr20 pbr20">
                            <ul class="p1-tc-right-ul clearfix">
                                <li class="p1-color-qin mrr20 mbr25 fl"><span>事件编号：</span>${disputeMediation.mediationCode}</li>
                                <li class="p1-color-qin mrr20 mbr25 fl" title="${disputeMediation.disputeEventName}"><span>事件名称：</span>${disputeMediation.disputeEventName}</li>
                                <li class="p1-color-qin mbr25 fl"><span>事件类型：</span>
                                    <#if disputeType_9x??>
                                        <#list disputeType_9x as l>
                                            <#if disputeMediation.disputeType2??>
                                                <#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if>
                                            </#if>
                                        </#list>
                                    </#if>
                                </li>
                                <li class="p1-color-qin mrr20 mbr25 fl"><span>事件规模：</span>
                                    <#if disputeScaleDC??>
                                        <#list disputeScaleDC as l>
                                            <#if disputeMediation.disputeScale??>
                                                <#if (l.dictGeneralCode?string==disputeMediation.disputeScale?string)>${l.dictName}</#if>
                                            </#if>
                                        </#list>
                                    </#if>
                                </li>
                                <li class="p1-color-qin mrr20 mbr25 fl"><span>发生日期：</span>${disputeMediation.happenTime?string('yyyy-MM-dd')}</li>
                                <li class="p1-color-qin mbr25 fl"><span>化解时限：</span>${disputeMediation.mediationDeadlineStr}</li>
                                <li class="p1-color-qin mrr20 mbr25 fl" title="${disputeMediation.involvedOrgName}"><span>涉及单位：</span>${disputeMediation.involvedOrgName}</li>
                                <li class="p1-color-qin mrr20 mbr25 fl"><span>涉及人数：</span>${disputeMediation.involveNum}</li>
                                <li class="p1-color-qin mbr25 fl" title="${disputeMediation.happenAddr}"><span>发生地址：</span>${disputeMediation.happenAddr}</li>
                                <li class="p1-color-qin mrr20 fl"><span>预警等级：</span>
                                    <#if disputeMediation.warningLevel??>
                                        <#if disputeMediation.warningLevel== '01'>
                                            一级（有可能引发群体性事件等严重影响社会稳定的事件纠纷）
                                        <#elseif disputeMediation.warningLevel == '02'>
                                            二级（有可能引发群体闹事苗头的事件纠纷）
                                        <#elseif disputeMediation.warningLevel == '03'>
                                            三级（有可能引发群体越级非正常访的事件纠纷）
                                        </#if>
                                    </#if>
                                </li>
                            </ul>
                    </div>
                    <!-- top end -->
                    <!-- title -->
                    <div class="flex flex-jb rc-clc-top mtr30 mbr20">
                        <h1 class="title-ext1">调度问题</h1>
                    </div>
                    <div class="sev-span01-top sev-span01-top02 p3-tc-bor  ptr20 pbr20 p1-color-qin posi-rela">
                        <div class="h-x plr20 prr20 height-p100">
                            ${disputeMediation.disputeCondition}
                        </div>
                    </div>
                </div>
                <!-- 基本清单 end -->

                <!-- 当事人信息（多人） start -->
                <div class="sev-span02 height-p100 hide posi-rela">
                    <div class="height-p100 h-x">
                        <#if involvedPeoples??>
                            <#list involvedPeoples as item>
                                <#if item_index == 0>
                                    <div class="sev-span01-top plr10 prr10 ptr10 pbr10">
                                <#else >
                                    <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mtr15">
                                </#if>
                                    <ul class="p1-tc-right-ul clearfix">
                                        <li class="p1-color-qin mrr20 mbr15 fl"><span>当&#x3000事&#x3000人：</span><span class="name-tc">${item.name}</span></li>
                                        <li class="p1-color-qin mrr20 mbr15 fl"><span>类&#x3000&#x3000别：</span><#if disputeType_9x??><#list disputeType_9x as l><#if disputeMediation.disputeType2??><#if (l.dictGeneralCode?string==disputeMediation.disputeType2?string)>${l.dictName}</#if></#if></#list></#if></li>
                                        <li class="p1-color-qin mbr15 fl"><span>性&#x3000&#x3000别：</span><span class="name-tc">${item.sexName}</span></li>
                                        <li class="p1-color-qin mrr20 mbr15 fl"><span>民&#x3000&#x3000&#x3000族：</span>${item.nationName}</li>
                                        <li class="p1-color-qin mrr20 mbr15 fl" title="${item.homeAddr}"><span>住&#x3000&#x3000址：</span>${item.homeAddr}</li>
                                        <li class="p1-color-qin mbr15 fl"><span>证&#x3000&#x3000件：</span>${item.cardTypeName}</li>
                                        <li class="p1-color-qin mrr20  fl"><span>证&#x3000件&#x3000号：</span>${item.idCard}</li>
                                        <li class="p1-color-qin mrr20  fl"><span>学&#x3000&#x3000历：</span>${item.eduName}</li>
                                        <li class="p1-color-qin  fl"><span>联系方式：</span>${item.tel}</li>
                                    </ul>
                                </div>
                            </#list>
                        </#if>
                        <!-- top start -->
                        <!--<div class="sev-span01-top plr10 prr10 ptr10 pbr10">
                                <ul class="p1-tc-right-ul clearfix">
                                    <li class="p1-color-qin mrr20 mbr15 fl"><span>当&#x3000事&#x3000人：</span><span class="name-tc">张春华</span></li>
                                    <li class="p1-color-qin mrr20 mbr15 fl"><span>类&#x3000&#x3000别：</span>邻里用水纠纷</li>
                                    <li class="p1-color-qin mbr15 fl"><span>性&#x3000&#x3000别：</span><span class="name-tc">女</span></li>
                                    <li class="p1-color-qin mrr20 mbr15 fl"><span>民&#x3000&#x3000族：</span>汉</li>
                                    <li class="p1-color-qin mrr20 mbr15 fl"><span>住&#x3000&#x3000址：</span>江西省南昌市红谷滩新区新街3号</li>
                                    <li class="p1-color-qin mbr15 fl"><span>证&#x3000&#x3000件</span>居民身份证</li>
                                    <li class="p1-color-qin mrr20  fl"><span>证&#x3000件&#x3000号：</span>20235156151646</li>
                                    <li class="p1-color-qin mrr20  fl"><span>学&#x3000&#x3000历：</span>本科</li>
                                    <li class="p1-color-qin  fl"><span>联系方式：</span>15600060000</li>
                                </ul>
                        </div>-->
                        <!-- top end -->
                        <!-- top start -->
                        <!--<div class="sev-span01-top plr10 prr10 ptr10 pbr10 mtr15">
                            <ul class="p1-tc-right-ul clearfix">
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>当&#x3000事&#x3000人：</span><span class="name-tc">张之华</span></li>
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>类&#x3000&#x3000别：</span>邻里用水纠纷</li>
                                <li class="p1-color-qin mbr15 fl"><span>性&#x3000&#x3000别：</span><span class="name-tc">男</span></li>
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>民&#x3000&#x3000族：</span>汉</li>
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>住&#x3000&#x3000址：</span>江西省南昌市红谷滩新区新街3号</li>
                                <li class="p1-color-qin mbr15 fl"><span>证&#x3000&#x3000件</span>居民身份证</li>
                                <li class="p1-color-qin mrr20  fl"><span>证&#x3000件&#x3000号：</span>20235156151646</li>
                                <li class="p1-color-qin mrr20  fl"><span>学&#x3000&#x3000历：</span>本科</li>
                                <li class="p1-color-qin  fl"><span>联系方式：</span>15600060000</li>
                            </ul>
                        </div>-->
                        <!-- top end -->
                    </div>
                </div>
                <!-- 当事人信息（多人） end -->
                <!-- 化解情况 start -->
                <div class="sev-span03 height-p100 hide posi-rela">
                    <div class="height-p100 h-x">
                        <div class="flex flex-jb rc-clc-top mtr20 mbr15">
                            <h1 class="title-ext1">化解信息</h1>
                        </div>
                        <!-- top start -->
                        <div class="sev-span01-top plr10 prr10 ptr15 pbr15">
                            <ul class="p1-tc-right-ul p1-tc-right-ul02 clearfix">
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>化解方式：</span>
                                    <#if mediationTypeDC_9x??>
                                        <#list mediationTypeDC_9x as l>
                                            <#if disputeMediation.mediationType??>
                                                <#if (l.dictGeneralCode?string==disputeMediation.mediationType?string)>${l.dictName}</#if>
                                            </#if>
                                        </#list>
                                    </#if>
                                </li>
                                <li class="p1-color-qin mrr20 mbr15 fl"><span>化解是否成功：</span><span class="name-tc">是</span></li>
                            </ul>
                            <p class="p1-tc-right-ul02-p flex">化解情况：<span>${disputeMediation.mediationResult}</span></p>
                        </div>
                        <!-- top end -->
                        <div class="flex flex-jb rc-clc-top mtr25 mbr15">
                            <h1 class="title-ext1">化解责任人信息</h1>
                        </div>
                        <#if persons??>
                            <#list persons as item>
                                <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                                    <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                        <li class="p1-color-qin mrr20 fl"><span>负责人组织：</span>${item.mediationOrgName}</li>
                                        <li class="p1-color-qin mrr20 fl"><span>负责人姓名：</span><span class="name-tc">${item.mediator}</span></li>
                                        <li class="p1-color-qin mrr20 fl"><span>负责人职务：</span>${item.mediationPost}</li>
                                        <li class="p1-color-qin  fl"><span>手机号：</span>${item.mediatorTel}</li>
                                    </ul>
                                </div>
                            </#list>
                        </#if>
                        <!--<div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>负责人组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>
                        <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>负责人组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>
                        <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>负责人组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>负责人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>-->
                        <div class="flex flex-jb rc-clc-top mtr25 mbr15">
                            <h1 class="title-ext1">包案领导信息</h1>
                        </div>
                        <#if leaders??>
                            <#list leaders as item>
                                <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                                    <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                        <li class="p1-color-qin mrr20 fl"><span>领导组织：</span>${item.mediationOrgName}</li>
                                        <li class="p1-color-qin mrr20 fl"><span>领导人姓名：</span><span class="name-tc">${item.mediator}</span></li>
                                        <li class="p1-color-qin mrr20 fl"><span>领导人职务：</span>${item.mediationPost}</li>
                                        <li class="p1-color-qin  fl"><span>手机号：</span>${item.mediatorTel}</li>
                                    </ul>
                                </div>
                            </#list>
                        </#if>
                        <!--<div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>领导组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>
                        <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>领导组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>
                        <div class="sev-span01-top plr10 prr10 ptr10 pbr10 mbr10">
                            <ul class="p1-tc-right-ul p1-tc-right-ul03 clearfix ">
                                <li class="p1-color-qin mrr20 fl"><span>领导组织：</span>XXXX</li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人姓名：</span><span class="name-tc">张春华</span></li>
                                <li class="p1-color-qin mrr20 fl"><span>领导人职务：</span>XXXX</li>
                                <li class="p1-color-qin  fl"><span>手机号：</span>15600600600</li>
                            </ul>
                        </div>-->
                    </div>
                </div>
                <!-- 化解情况 end -->
                <!-- 流程信息 start -->
                <div class="sev-span04 height-p100 hide posi-rela">
                    <div class="font-size-r14 cor-fff">
                        <span class="mrr50"><i class="tc-span-i mrr5"></i>当前环节</span>
                        <span><i class="tc-span-i02 mrr5"></i>历史环节</span>
                    </div>
                    <div class="mtr10 sev-span04-top font-size-0 ">
                        <span class="font-size-r15 wi20 colorbde5fd text-align-c">办理环节</span>
                        <span class="font-size-r15 wi40 colorbde5fd text-align-c">办理人/办理时间</span>
                        <span class="font-size-r15 wi40 colorbde5fd text-align-c">办理意见</span>
                    </div>
                    <!-- 流程图内容 -->
                    <ul class="sev-span04-nr h-x ptr15 font-size-0">
                        <#if disputeFlowInfos??>
                            <#list disputeFlowInfos as item>
                                <#if item_index==0>
                                    <li class="clearfix">
                                        <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">${item.handleLinkName}<i class="tc-span-i tc-i-pos"></i></span>
                                        <div class="font-size-r15 wi40 colorbde5fd fl">
                                            <div class="plr40 prr40">
                                                <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                                    <p><span class="name-tc">${item.handlerName}</span></p>
                                                    <p class="mtr10">办结时间：${item.handleTime?string("yyyy-MM-dd")}</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="font-size-r15 wi40 colorbde5fd fl">
                                            <div class="plr40 prr40">
                                                <#if item.handleOpinion??>
                                                <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                                    <p class="text-line-c2">${item.handleOpinion}</p>
                                                </div>
                                                </#if>
                                            </div>
                                        </div>
                                    </li>
                                <#else >
                                    <li class="clearfix">
                                    <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">${item.handleLinkName}<i class="tc-span-i02 tc-i-pos"></i></span>
                                    <div class="font-size-r15 wi40 colorbde5fd fl">
                                        <div class="plr40 prr40">
                                            <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                                <p><span class="name-tc">${item.handlerName}</span></p>
                                                <p class="mtr10">办结时间：${item.handleTime?string("yyyy-MM-dd")}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="font-size-r15 wi40 colorbde5fd fl">
                                        <div class="plr40 prr40">
                                            <#if item.handleOpinion??>
                                            <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                                <p class="text-line-c2">${item.handleOpinion}</p>
                                            </div>
                                            </#if>
                                        </div>
                                    </div>
                                    </li>
                                </#if>
                            </#list>
                        </#if>
                        <#--<!-- tc-span-i 当前环节  tc-span-i02 历史环节 &ndash;&gt;
                        <li class="clearfix">
                            <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">市级处理<i class="tc-span-i tc-i-pos"></i></span>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                        <p><span class="name-tc">李华</span>（综合执法部门）耗时<span class="name-tc mlr5 mrr5">2</span>分钟</p>
                                        <p class="mtr10">办结时间：2018-02-24 10:19:21</p>
                                    </div>
                                </div>
                            </div>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                        <p class="text-line-c2">意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容</p>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="clearfix">
                            <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">事件采集<i class="tc-span-i tc-i-pos"></i></span>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                        <p><span class="name-tc">李华</span>（综合执法部门）耗时<span class="name-tc mlr5 mrr5">2</span>分钟</p>
                                        <p class="mtr10">办结时间：2018-02-24 10:19:21</p>
                                    </div>
                                </div>
                            </div>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                        <p class="text-line-c2">意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容</p>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="clearfix">
                            <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">市级处理<i class="tc-span-i tc-i-pos"></i></span>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                        <p><span class="name-tc">李华</span>（综合执法部门）耗时<span class="name-tc mlr5 mrr5">2</span>分钟</p>
                                        <p class="mtr10">办结时间：2018-02-24 10:19:21</p>
                                    </div>
                                </div>
                            </div>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                        <p class="text-line-c2">意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容</p>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="clearfix">
                            <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">事件采集<i class="tc-span-i tc-i-pos"></i></span>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                        <p><span class="name-tc">李华</span>（综合执法部门）耗时<span class="name-tc mlr5 mrr5">2</span>分钟</p>
                                        <p class="mtr10">办结时间：2018-02-24 10:19:21</p>
                                    </div>
                                </div>
                            </div>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                        <p class="text-line-c2">意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容</p>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li class="clearfix">
                            <span class="font-size-r15 wi20 colorbde5fd text-align-c posi-rela mtr30 fl">事件采集<i class="tc-span-i02 tc-i-pos"></i></span>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj">
                                        <p><span class="name-tc">李华</span>（综合执法部门）耗时<span class="name-tc mlr5 mrr5">2</span>分钟</p>
                                        <p class="mtr10">办结时间：2018-02-24 10:19:21</p>
                                    </div>
                                </div>
                            </div>
                            <div class="font-size-r15 wi40 colorbde5fd fl">
                                <div class="plr40 prr40">
                                    <div class="plr15 prr15 ptr15 pbr15 box-nr-bj02">
                                        <p class="text-line-c2">意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容意见内容意见内容意见内容意见内意见内容</p>
                                    </div>
                                </div>
                            </div>
                        </li>-->
                    </ul>

                </div>
                <!-- 流程信息 end -->
          
            </div>
            <!-- 人物内容 end -->
        </div>

    </div>
    <script src="${uiDomain}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
    <script>
        // 滚动条美化
        $('.h-x').niceScroll({
            cursorcolor: "#185ab2",
            cursoropacitymax: 1,
            cursorwidth: "4px",
            autohidemode: false,
            // background: "#06325c",
            cursorborder: "none",
            cursorborderradius: "0",
        })
        // 点击切换七项
        $('.seven>p>span').on('click', function () {
                if (!$(this).hasClass('active')) {
                    $(this).siblings().removeClass('active')
                    $(this).addClass('active')
                    var daname = $(this).attr('da-name')
                    $('.' + daname).fadeIn(300).siblings().hide()
                    $('.h-x').getNiceScroll().resize();
                }
            })
    </script>
</body>
</html>