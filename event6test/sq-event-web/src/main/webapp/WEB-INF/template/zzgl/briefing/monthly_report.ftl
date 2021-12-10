<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <title>江西省社会治理后台月报页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<script type="text/javascript">
		window.parent.modleclose();</script>
    <link rel="stylesheet" href="${uiDomain}/web-assets/common/css/reset.css" />
    <link rel="stylesheet" href="${uiDomain}/web-assets/_wangge/jiangxi/css/monthly-report.css" />

    <#include "/component/standard_common_files-1.1.ftl" />
    <link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/js/jquery-easyui-1.4/themes/gray/easyui.css">
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
    <script src="${uiDomain}/web-assets/common/js/echarts4.2.0.min.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/themes/default/plat-extend.css">
    <script type="text/javascript" src="${uiDomain}/web-assets/plugins/echarts-3.2.3/echarts.js"></script>
	<style>
		.ResetBtn {
            background-image: url("${rc.getContextPath()}/images/reject.png");
		    background-color: #8fc31f;
		}
		.mr-rbbi-pic {
    margin: 10px auto 0;
}
	</style>
</head>

<body><div style="display:none">${time!}</div>
<div id="im"></div>
<div class="container clearfix">
    <div class="mr-left fl">
        <div class="mr-l-top pl25 bs">
            <i></i>
            <p>目录</p>
        </div>
        <div class="mr-l-bottom h-x">
            <i class="mr-lb-line"></i>
            <ul class="mr-lb-list">
                <li>
                    <a href="#item1">
                        <i></i>
                        <p>总体情况</p>
                    </a>
                </li>
                <li>
                    <a href="#item2">
                        <i></i>
                        <p>类型分析</p>
                    </a>
                </li>
                <li>
                    <a href="#item3">
                        <i></i>
                        <p>上报渠道</p>
                    </a>
                </li>
                
                    <li>
                        <a href="#item4">
                            <i></i>
                            <#if gridLevel="省"><p>市级情况</p></#if>
							<#if gridLevel="市"><p>县级情况</p></#if>
                        </a>
                    </li>
                <li>
                    <a href="#item5">
                        <i></i>
                        <p>县级排名</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="mr-right fl">
        <div class="mr-r-top clearfix">
            <div class="clearfix fr mr-rt-btn mr25 mt20">
                <a href="javascript:void(0);" class="export fl" onclick="exportWord()">导出</a>
                <a href="javascript:void(0);" class="edit fl ml20 bs" onclick="editOverallSituation()">编辑</a>
            </div>
        </div>
        <div class="mr-r-bottom pl30 pr20 bs pb25">
            <div class="h-x mr-rb-box pr10">
                <div class="mr-rbb-item">
                    <p class="mr-rb-title1" style="width: 500px">全${gridLevel!}综治中心实体化运行情况</p>
                    <p class="mr-rb-title1">分析报告</p>
                    <p class="mr-rb-title2">（${year!}年${month!}月）</p>
                    <div class="mr-rbb-box1">
                        <div class="mr-rbbb1-content">
                            <p>一、总体情况：${overallSituation!}</p>
                            <p>二、类型分析：${typeAnalysis!}</p>
                            <p>三、上报渠道：${reportChannel!}</p>
                                <p>四、<#if gridLevel="省">市</#if><#if gridLevel="市">县</#if>级情况：${cityLevelSituation!}</p>
                                <p>五、县级排名：${townLevelSituation!}</p>
                        </div>
                    </div>
                    <p class="mr-rb-title3">江西省综治中心</p>
                </div>
                <div class="mr-rbb-item" id="item1">
                    <p class="mr-rbbi-title1" style="font-size: 20px">一、总体情况</p>
                    <p class="mr-rbbi-title2" style="font-size: 18px">（一）概况</p>
					<p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，全${gridLevel!}共上报民生民安事项${allTotalEvent!}件。经查重认定，有效事项${totalEvent!}件，有效事项占比${thisValidRate}，有效率较上月${compareLastRate!}；办结${totalDisposal!}件，办结率${totalDisposalRate!}，较上月${compareLastDisposalRate!}。其中：</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">民生类有效事项${peopleLifeEvent!}件，占比${peopleLifeRate!}；办结${peopleLifeDisposal!}件，办结率${peopleLifeDisposalRate!}。</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">民安类有效事项${peopleSafeEvent!}件，占比${peopleSafeRate!}；办结${peopleSafeDisposal!}件，办结率${peopleSafeDisposalRate!}。（见图一）。</p>
                    <div class="mr-rbbi-box1">
                        <div id="image1" class="mr-rbbi-pic" style="width:1000px;height:600px;margin-top:0px;" ></div>

                        <p style="font-size: 16px;margin-top: 0">（图一）</p>
                    </div>
                    <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（二）主要特点</p>
                    <#if mainFeatureCSList?? && (mainFeatureCSList?size>0)>
                        <#list mainFeatureCSList as item>
                            <#if item_index==0>
                                <p class="mr-rbbi-text" style="font-size: 18px">1.本月民安类问题的主要特点是：${item.VAL_!}</p>
                            <#else>
                                <p class="mr-rbbi-text" style="font-size: 18px">${item.VAL_!}</p>
                            </#if>
                        </#list>
                    <#else>
                        <p class="mr-rbbi-text" style="font-size: 18px">1.本月民安类问题的主要特点是：</p>
                    </#if>

                    <#if mainFeatureCLList?? && (mainFeatureCLList?size>0)>
                        <#list mainFeatureCLList as item>
                            <#if item_index==0>
                                <p class="mr-rbbi-text" style="font-size: 18px">2.本月民生类问题的主要特点是：${item.VAL_!}</p>
                            <#else>
                                <p class="mr-rbbi-text" style="font-size: 18px">${item.VAL_!}</p>
                            </#if>
                        </#list>
                    <#else>
                        <p class="mr-rbbi-text" style="font-size: 18px">2.本月民生类问题的主要特点是：</p>
                    </#if>

                    <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（三）本月工作中存在的问题</p>
                    <#if problemList?? && (problemList?size>0)>
                        <#list problemList as item>
                            <#if item_index==0>
                                <p class="mr-rbbi-text" style="font-size: 18px">${item_index+1}.${item.VAL_!}</p>
                            <#else>
                                <p class="mr-rbbi-text" style="font-size: 18px">${item_index+1}.${item.VAL_!}</p>
                            </#if>
                        </#list>
                    </#if>
                </div>
                <div class="mr-rbb-item" id="item2">
                    <p class="mr-rbbi-title1" style="font-size: 20px">二、类型分析</p>
                    <p class="mr-rbbi-title2" style="font-size: 18px">（一）民安类事项</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，全${gridLevel!}共上报民安类有效事项${peopleSafeEvent!}件，办结${peopleSafeDisposal!}件，办结率${peopleSafeDisposalRate!}。</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">事项大类中（见图二）：</p>
                    <#if peopleSafeListMap??>
                        <#list peopleSafeListMap as peopleSafeTypeEventMap>
                            <p class="mr-rbbi-text" style="font-size: 18px">
                                ${peopleSafeTypeEventMap.eventType!}共上报有效事项${peopleSafeTypeEventMap.thisReportTotal!}件，环比上月${peopleSafeTypeEventMap.lastReportTotal!}件${peopleSafeTypeEventMap.peopleSafeTypeRGRate!}，占民安类事项${peopleSafeTypeEventMap.peopleSafeTypeRate!}；办结${peopleSafeTypeEventMap.thisDisposalCounts!}件，办结率${peopleSafeTypeEventMap.peopleSafeTypeDisposalRate!}；
                            </p>
                        </#list>
                    </#if>
                    <p class="mr-rbbi-text" style="font-size: 18px">事项小类中，数量占比前十位的见表一。</p>
                    <div class="mr-rbbi-box1">
                        <div id="image2" class="mr-rbbi-pic" style="width:1000px;height:600px;margin-top:0px;" ></div>
                        <P style="font-size: 16px;margin-top: 0">（图二）</P>
                    </div>
                    <div class="mr-rbbi-box2 mt20" style="margin-bottom: 10px">
                        <p style="font-size: 16px">表一：数量占比前十位的民安事项小类</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>事项小类</th>
                                <th>事项大类</th>
                                <th>事项上报总数</th>
                                <th>有效事项上报数</th>
                                <th>有效事项占比民安类有效事项</th>
                                <th>有效事项化解数</th>
                                <th>有效事项化解率</th>
                                <th>有效事项逾期数</th>
                                <th>有效事项逾期率</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if tableData1??>
                            <#list tableData1 as table1>
                            <tr>
                                <td>${table1.number!}</td>
                                <td>${table1.smallType!}</td>
                                <td>${table1.bigType!}</td>
                                <td>${table1.allCounts!}</td>
                                <td>${table1.reportCounts!}</td>
                                <td>${table1.rate!}</td>
                                <td>${table1.disposalCounts!}</td>
                                <td>${table1.disposalRate!}</td>
                                <td>${table1.overdue!}</td>
                                <td>${table1.overdueRate!}</td>
                            </tr>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                    <p class="mr-rbbi-title2" style="font-size: 18px">（二）民生类事项</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，全${gridLevel!}共上报民生类有效事项${peopleLifeEvent!}件，办结${peopleLifeDisposal!}件，办结率${peopleLifeDisposalRate!}。</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">事项大类中（见图三）：</p>
                    <#if peopleLifeListMap??>
                        <#list peopleLifeListMap as peopleLifeTypeEventMap>
                            <p class="mr-rbbi-text" style="font-size: 18px">${peopleLifeTypeEventMap.eventType!}共上报有效事项${peopleLifeTypeEventMap.thisReportTotal!}件，环比上月${peopleLifeTypeEventMap.lastReportTotal!}件${peopleLifeTypeEventMap.peopleLifeTypeRGRate!}，占民生类事项${peopleLifeTypeEventMap.peopleLifeTypeRate!}；办结${peopleLifeTypeEventMap.thisDisposalCounts!}件，办结率${peopleLifeTypeEventMap.peopleLifeTypeDisposalRate!}；</p>
                        </#list>
                    </#if>
                    <p class="mr-rbbi-text" style="font-size: 18px">事项小类中，数量占比前十位的见表二。</p>
                    <div class="mr-rbbi-box1">
                        <div id="image3" class="mr-rbbi-pic" style="width:1000px;height:600px;" ></div>
                        <P style="font-size: 16px;margin-top: 0">（图三）</P>
                    </div>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表二：数量占比前十位的民生事项小类</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>事项小类</th>
                                <th>事项大类</th>
                                <th>事项上报总数</th>
                                <th>有效事项上报数</th>
                                <th>有效事项占比民生类有效事项</th>
                                <th>有效事项办结数</th>
                                <th>有效事项办结率</th>
                                <th>有效事项逾期数</th>
                                <th>有效事项逾期率</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if tableData2??>
                            <#list tableData2 as table1>
                            <tr>
                                <td>${table1.number!}</td>
                                <td>${table1.smallType!}</td>
                                <td>${table1.bigType!}</td>
                                <td>${table1.allCounts!}</td>
                                <td>${table1.reportCounts!}</td>
                                <td>${table1.rate!}</td>
                                <td>${table1.disposalCounts!}</td>
                                <td>${table1.disposalRate!}</td>
                                <td>${table1.overdue!}</td>
                                <td>${table1.overdueRate!}</td>
                            </tr>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="mr-rbb-item" id="item3">
                    <p class="mr-rbbi-title1" style="font-size: 20px">三、事项上报渠道分析</p>
                    <div class="mr-rbbi-box1">
                        <div id="image4" class="mr-rbbi-pic" style="width:1000px;height:400px;" ></div>
                        <P style="font-size: 16px;margin-top: 0">（图四）</P>
                    </div>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表三</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>上报渠道</th>
                                <th>事项上报总数</th>
                                <th>有效事项上报数</th>
                                <th>有效事项占比</th>
                                <th>有效事项办结数</th>
                                <th>有效事项办结率</th>
                                <th>有效事项逾期数</th>
                                <th>有效事项逾期率</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if tableData3??>
                            <#list tableData3 as table3>
                            <tr>
                                <#if table3.number=1||table3.number=4||table3.channel="平安志愿者">
                                    <td>${table3.number!}</td>
                                </#if>
                                <#if table3.number=2>
                                    <#if gridLevel="省">
                                        <td rowspan="5">${table3.number!}</td>
                                    <#else>
                                        <td rowspan="4">${table3.number!}</td>
                                    </#if>
                                </#if>
                                <#if table3.number=1||table3.number=2||table3.number=4||table3.channel="平安志愿者">
                                    <td style="font-weight: bold">${table3.channel!}</td>
                                <#else>
                                    <td style="color: black">${table3.channel!}</td>
                                </#if>
								<td>${table3.allCounts?c}</td>
                                <td>${table3.reportCounts?c}</td>
                                <td>${table3.rate!}</td>
                                <td>${table3.disposalCounts?c}</td>
                                <td>${table3.disposalRate!}</td>
                                <td>${table3.overdue?c}</td>
                                <td>${table3.overdueRate!}</td>
                            </tr>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                    <div class="mr-rbb-item" id="item4">
                        <p class="mr-rbbi-title1" style="font-size: 20px">四、${gridChildTitle!'设区市'}事项上报情况</p>
                        <p class="mr-rbbi-title2" style="font-size: 18px">（一）总体情况</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，${gridChildTitle!'设区市'}共上报事项${allCityTotalEvent!}件。其中，有效事项${cityTotalEvent!}件，已办结${cityTotalDisposal!}件。</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">
                            有效事项上报率较高的${gridChildTitle!'设区市'}分别是：
                            <#if cityList1??>
                            <#list cityList1 as cityList1Text>
                              <#if (cityList1Text_index<3)>
                                ${cityList1Text.gridName}，占比${cityList1Text.rate} <#if (cityList1Text_index<2)>；</#if>
                              </#if>
                            </#list>
                            </#if>
                           。
                        </p>
                        <p class="mr-rbbi-text" style="font-size: 18px">
                            有效事项上报率较低的${gridChildTitle!'设区市'}分别是：
                            <#if cityList1??>
                                <#list cityList1?reverse  as cityList2Text>
                                    <#if (cityList2Text_index<3)>
                                        ${cityList2Text.gridName}，占比${cityList2Text.rate}<#if (cityList2Text_index<2)>；</#if>
                                    </#if>
                                </#list>
                            </#if> 。（见图五）
							</p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img5" style="width:800px; height:1000px "></div>
                            <P style="font-size: 16px;margin-top: 0">（图五）</P>
                        </div>
                        <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（二）综治中心上报事项情况</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，<#if gridChildTitle??>县乡村三<#else>市县乡村四</#if>级综治中心上报事项${allCmdTotalEvent}件。其中，有效事项${cmdTotalEvent!}件，已办结${cmdTotalDisposal!}件。</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">有效事项上报率居后5位的${gridChildTitle!'设区市'}分别是：
                            <#if cmdList1??>
                            <#list cmdList1?reverse as cmdList1Text>
                            <#if (cmdList1Text_index<5)>
                              ${cmdList1Text.gridName}，占比${cmdList1Text.rate}<#if (cmdList1Text_index<4)>；</#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图六）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img6" style="width:800px; height:650px "></div>
                            <P style="font-size: 16px;margin-top: 0">（图六）</P>
                        </div>
                        <p class="mr-rbbi-text" style="font-size: 18px"><#if gridChildTitle??>县乡村三<#else>市县乡村四</#if>级综治中心有效事项上报数量居前<#if (cmdList1?size>5)>5<#else >${cmdList2?size}</#if>位的类别是：
                            <#if cmdList2??>
                            <#list cmdList2 as cmdList2Text>
                            <#if (cmdList2Text_index<5)>
                            ${cmdList2Text.eventType}，占比${cmdList2Text.rate}<#if (cmdList2Text_index<4)>；</#if>
                            </#if>
                            </#list>
                            </#if>
                        。（见图七）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img7" style="width:1000px;height:600px;"></div>
                            <P style="font-size: 16px;margin-top: 0">（图七）</P>
                        </div>
                        <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（三）网格员上报事项情况</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，网格员上报事项${allGridManTotalEvent!}件。其中，有效事项${gridManTotalEvent!}件，已办结${gridManTotalDisposal!}件。</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">有效事项上报率居后<#if (gridManList1?size>5)>5<#else >${gridManList1?size}</#if>位的${gridChildTitle!'设区市'}分别是：
                            <#if gridManList1??>
                            <#list gridManList1?reverse as gridManList1Text>
                            <#if (gridManList1Text_index<5)>
                            ${gridManList1Text.gridName}，占比${gridManList1Text.rate}<#if (gridManList1Text_index<4)>；</#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图八）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img8" style="width:800px; height:650px"></div>
                            <P style="font-size: 16px;margin-top: 0">（图八）</P>
                        </div>
                        <p class="mr-rbbi-text" style="font-size: 18px">网格员有效事项上报数量居前<#if (gridManList2?size>5)>5<#else >${gridManList2?size}</#if>位的类别是：
                            <#if gridManList2??>
                            <#list gridManList2 as gridManList2Text>
                            <#if (gridManList2Text_index<5)>
                            ${gridManList2Text.eventType}，占比${gridManList2Text.rate}<#if (gridManList2Text_index<4)>；</#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图九）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img9" style="width:1000px;height:600px;"></div>
                            <P style="font-size: 16px;margin-top: 0">（图九）</P>
                        </div>
                        <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（四）平安志愿者上报事项情况</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，平安志愿者上报事项${volunteerTotalEvent!}件，已办结${volunteerTotalDisposal!}件。</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">
						 有效事件上报率居后<#if (volunteerList1?size>5)>5<#else >${volunteerList1?size}</#if>位的${gridChildTitle!'设区市'}分别是：
                            <#if volunteerList1??>
                            <#list volunteerList1?reverse as volunteerList1Text>
                            <#if (volunteerList1Text_index<5)>
                            ${volunteerList1Text.gridName}，占比${volunteerList1Text.rate} <#if (volunteerList1Text_index<4)>； </#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图十）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img10" style="width:800px; height:650px "></div>
                            <P style="font-size: 16px;margin-top: 0">（图十）</P>
                        </div>
                        <p class="mr-rbbi-text" style="font-size: 18px">平安志愿者上报事项数量居前<#if (volunteerList2?size>5)>5<#else >${volunteerList2?size}</#if>位的类别是：
                            <#if volunteerList2??>
                            <#list volunteerList2 as volunteerList2Text>
                            <#if (volunteerList2Text_index<5)>
                            ${volunteerList2Text.eventType}，占比${volunteerList2Text.rate}<#if (volunteerList2Text_index<4)>； </#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图十一）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img11" style="width:1000px;height:600px;"></div>
                            <P style="font-size: 16px;margin-top: 0">（图十一）</P>
                        </div>
                        <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（五）<#if gridChildTitle??>县、乡<#else>市、县</#if>两级综治责任单位上报事项情况</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，<#if gridChildTitle??>县、乡<#else>市、县</#if>两级综治责任单位上报事项${allDutyCenterTotalEvent!}件。其中，有效事项${dutyCenterTotalEvent!}件，已办结${dutyCenterTotalDisposal!}件。
						</p>
                        <p class="mr-rbbi-text" style="font-size: 18px">有效事项上报率居后<#if (dutyCenterList1?size>5)>5<#else >${dutyCenterList1?size}</#if>位的${gridChildTitle!'设区市'}分别是：
                            <#if dutyCenterList1??>
                            <#list dutyCenterList1?reverse as dutyCenterList1Text>
                            <#if (dutyCenterList1Text_index<5)>
                            ${dutyCenterList1Text.gridName}，占比${dutyCenterList1Text.rate}<#if (dutyCenterList1Text_index<4)>； </#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图十二）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img12" style="width:800px; height:650px "></div>
                            <P style="font-size: 16px;margin-top: 0">（图十二）</P>
                        </div>
                        <p class="mr-rbbi-text" style="font-size: 18px">${month!}月份，<#if gridChildTitle??>县、乡<#else>市、县</#if>两级综治责任单位有效事项上报数量居前<#if (dutyCenterList2?size>5)>5<#else >${dutyCenterList2?size}</#if>位的类别是：
                            <#if dutyCenterList2??>
                            <#list dutyCenterList2 as dutyCenterList2Text>
                            <#if (dutyCenterList2Text_index<5)>
                            ${dutyCenterList2Text.eventType}，占比${dutyCenterList2Text.rate}<#if (dutyCenterList2Text_index<4)>； </#if>
                            </#if>
                            </#list>
                            </#if>
                            。（见图十三）
                        </p>
                        <div class="mr-rbbi-box1">
                            <div class="mr-rbbi-pic" id="img13" style="width:1000px;height:600px;"></div>
                            <P style="font-size: 16px;margin-top: 0">（图十三）</P>
                        </div>
                    </div>
               
                <div class="mr-rbb-item" id="item5">
                    <p class="mr-rbbi-title1" style="font-size: 18px">五、县（市、区）事项上报数量排名</p>
                    <p class="mr-rbbi-title2" style="font-size: 18px">（一）有效事项占比情况排名</p>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表四</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr style="border-bottom: #E8E8E8 1px solid">
                                <th colspan="8">靠前十位县（市、区）排名</th>
                            </tr>
                            <tr>
                                <th rowspan="2">排名</th>
                                <th rowspan="2">辖区</th>
                                <th rowspan="2">事项上报总数</th>
                                <th colspan="3" style="border-bottom: #E8E8E8 1px solid">无效事项</th>
                                <th rowspan="2">有效事项上报数</th>
                                <th rowspan="2">有效事项上报率</th>
                            </tr>
                            <tr>
                                <th style="border-left: #E8E8E8 1px solid">重复事项</th>
                                <th>内容缺失</th>
                                <th>工作当事项</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if cityValidList??>
                            <#list cityValidList as cvList>
                            <#if cvList_index<10>
                            <tr>
                                <td>${cvList_index+1}</td>
                                <td>${cvList.gridName!}</td>
                                <td>${cvList.allCounts!}</td>
                                <td>${cvList.invalid1!}</td>
                                <td>${cvList.invalid2!}</td>
                                <td>${cvList.invalid3!}</td>
                                <td>${cvList.reportCounts!}</td>
                                <td>${cvList.rate!}%</td>
                            </tr>
                            </#if>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表五</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr style="border-bottom: #E8E8E8 1px solid">
                                <th colspan="8">靠后十位县（市、区）排名</th>
                            </tr>
                            <tr>
                                <th rowspan="2">排名</th>
                                <th rowspan="2">辖区</th>
                                <th rowspan="2">事项上报总数</th>
                                <th colspan="3" style="border-bottom: #E8E8E8 1px solid">无效事项</th>
                                <th rowspan="2">有效事项上报数</th>
                                <th rowspan="2">有效事项上报率</th>
                            </tr>
                            <tr>
                                <th style="border-left: #E8E8E8 1px solid">重复事项</th>
                                <th>内容缺失</th>
                                <th>工作当事项</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if cityValidList??>
                            <#list cityValidList?reverse as cvList>
                            <#if cvList_index<10>
                            <tr>
                                <td>${cvList_index+1}</td>
                                <td>${cvList.gridName!}</td>
                                <td>${cvList.allCounts!}</td>
                                <td>${cvList.invalid1!}</td>
                                <td>${cvList.invalid2!}</td>
                                <td>${cvList.invalid3!}</td>
                                <td>${cvList.reportCounts!}</td>
                                <td>${cvList.rate!}%</td>
                            </tr>
                            </#if>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                        
                    </div>
                    <p class="mr-rbbi-title2 mt30" style="font-size: 18px">（二）有效事项逾期情况</p>
                    <p class="mr-rbbi-text" style="font-size: 18px">有效事项无逾期的县（市、区）有${dueCountyCityNumber!}个
                        <#if dueCountyCityList??&&(dueCountyCityList?size>0)>
                            ，分别是：
                            <#list dueCountyCityList as dueCountyCityListText>
                            ${dueCountyCityListText.gridName}
                            <#if (dueCountyCityListText_index==dueCountyCityList?size-1)>
                                。
                            <#else >
                                、
                            </#if>
                            </#list>
							<#else >。
                        </#if>
                    </p>
                    <p class="mr-rbbi-text" style="font-size: 18px">有效事项逾期的县（市、区）见表六。</p>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表六</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr style="border-bottom: #E8E8E8 1px solid">
                                <th colspan="7">有效事项逾期率较高的县（市、区）</th>
                            </tr>
                            <tr>
                                <th>排名</th>
                                <th>辖区</th>
                                <th>事项上报总数</th>
                                <th>有效事项上报数</th>
                                <th>有效事项逾期占比</th>
                                <th>2天未受理有效事项数</th>
                                <th>7天未办理有效事项数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if tableData6??>
                            <#list tableData6 as overdueCountyCity>
                            <#if (overdueCountyCity_index<10)>
                            <tr>
                                <td>${overdueCountyCity_index+1!}</td>
                                <td>${overdueCountyCity.gridName!}</td>
                                <td>${overdueCountyCity.allCounts!}</td>
                                <td>${overdueCountyCity.reportCounts!}</td>
                                <td>${overdueCountyCity.rate!}</td>
                                <td>${overdueCountyCity.twoOverdue!}</td>
                                <td>${overdueCountyCity.weekOverdue!}</td>
                            </tr>
                            </#if>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
                    </div>
					 <p class="mr-rbbi-title2" style="font-size: 18px">（三）县（市、区）事项上报情况（按常住人口维度）排名</p>
                    <div class="mr-rbbi-box2 mt20">
                        <p style="font-size: 16px">表七</p>
                        <table class="mr-rbbi-table mt10">
                            <thead>
                            <tr style="border-bottom: #E8E8E8 1px solid">
                                <th colspan="6">后十位县（市、区）排名</th>
                            </tr>
                            <tr>
                                <th>排名</th>
                                <th>辖区</th>
                                <th>县（市、区）事项上报情况（人/件）</th>
                                <th>事项上报总数</th>
                                <th>有效事项上报数</th>
                                <th>有效事项办结数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#if tableData4??>
                            <#list tableData4?reverse as countyCityReport>
                            <#if countyCityReport_index<10>
                            <tr>
                                <td>${countyCityReport_index+1}</td>
                                <td>${countyCityReport.gridName!}</td>
                                <td>${countyCityReport.perCapita!}</td>
                                <td>${countyCityReport.allCounts!}</td>
                                <td>${countyCityReport.reportCounts!}</td>
                                <td>${countyCityReport.disposalCounts!}</td>
                            </tr>
                            </#if>
                            </#list>
                            </#if>
                            </tbody>
                        </table>
						<p>（注：县（市、区）有效事项上报情况（按常住人口维度）=2020年江西省统计局公布的各设区市常住人口数/当月有效事项上报数）</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="editDiv" class="easyui-window" title="编辑总体情况"  closed="true" modal="true" minimizable="false" maximizable="false" style="width:900px;height:750px;padding:5px;">

        <form id="editForm" method="post" action="${rc.getContextPath()}/zhsq/zzgl/briefingController/briefing/saveDetail.jhtml?reportId=${reportId}" autocomplete="off" enctype="multipart/form-data">
            <input type="hidden" id="applicationId" name="applicationId"/>
            <div class="NorForm">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="border-t">
                    <tr>
                        <td>
                            <label class="LabName"><span>总体情况：</span></label>
                            <input type="text" value="${overallSituation!}" class="inp1 easyui-validatebox" id="overallSituation" name="overallSituation" maxlength="500" style="width:70%;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>类型分析：</span></label>
                            <input type="text" value="${typeAnalysis!}" class="inp1 easyui-validatebox" id="typeAnalysis" name="typeAnalysis" maxlength="500" style="width:70%;"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>上报渠道：</span></label>
                            <input type="text" value="${reportChannel!}" class="inp1 easyui-validatebox" id="reportChannel" name="reportChannel" maxlength="500" style="width:70%;"/>
                        </td>
                    </tr>
                        <tr>
                            <td>
                                <label class="LabName"><span> <#if gridLevel="省">市级情况</#if><#if gridLevel="市">县级情况</#if>：</span></label>
                                <input type="text" value="${cityLevelSituation!}" class="inp1 easyui-validatebox" id="cityLevelSituation" name="cityLevelSituation" maxlength="500" style="width:70%;"/>
                            </td>
                        </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>县级排名：</span></label>
                            <input type="text" value="${townLevelSituation!}" class="inp1 easyui-validatebox" id="townLevelSituation" name="townLevelSituation" maxlength="500" style="width:70%;"/>
                        </td>
                    </tr>
                    <tr>
                        <td id="mainFeatureCSTd">
                            <label class="LabName"><span>民安类特点：</span></label>
                            <#if mainFeatureCSList?? && (mainFeatureCSList?size>0)>
                                <#list mainFeatureCSList as item>
                                    <#if item_index==0>
                                        <textarea type="text" class="inp1 easyui-validatebox" id="mainFeatureCS0" name="mainFeatureCS" maxlength="500" style="width:70%;height: 60px">${item.VAL_!}</textarea>
                                        <a onclick="addMainFeatureCS()"><img id="addMainFeatureCS" title="新增民安类主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                                    <#else >
                                        <div><textarea id="mainFeatureCS${item_index}" type="text" class="inp1 easyui-validatebox" name="mainFeatureCS" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px">${item.VAL_!}</textarea><a id="mainFeatureCSA${item_index}" onclick="delMainFeatureCS(this.id)" style="position:relative;left:10.7%"><img title="删除民安类问题主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png"></a></div>
                                    </#if>
                                </#list>
                            <#else >
                                <textarea type="text" class="inp1 easyui-validatebox" id="mainFeatureCS0" name="mainFeatureCS" maxlength="500" style="width:70%;height: 60px"></textarea>
                                <a onclick="addMainFeatureCS()"><img id="addMainFeatureCS" title="新增民安类主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td id="mainFeatureCLTd">
                            <label class="LabName"><span>民生类特点：</span></label>
                            <#if mainFeatureCLList?? && (mainFeatureCLList?size>0)>
                                <#list mainFeatureCLList as item>
                                    <#if item_index==0>
                                        <textarea type="text" class="inp1 easyui-validatebox" id="mainFeatureCL0" name="mainFeatureCL" maxlength="500" style="width:70%;height: 60px">${item.VAL_!}</textarea>
                                        <a onclick="addMainFeatureCL()"><img id="addMainFeatureCL" title="新增民生类主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                                    <#else >
                                        <div><textarea id="mainFeatureCL${item_index}" type="text" class="inp1 easyui-validatebox" name="mainFeatureCL" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px">${item.VAL_!}</textarea><a id="mainFeatureCLA${item_index}" onclick="delMainFeatureCL(this.id)" style="position:relative;left:10.7%"><img title="删除民生类问题主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png"></a></div>
                                    </#if>
                                </#list>
                            <#else >
                                <textarea type="text" class="inp1 easyui-validatebox" id="mainFeatureCL0" name="mainFeatureCL" maxlength="500" style="width:70%;height: 60px"></textarea>
                                <a onclick="addMainFeatureCL()"><img id="addMainFeatureCL" title="新增民生类主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td id="problemTd">
                            <label class="LabName"><span>存在的问题：</span></label>
                            <#if problemList?? && (problemList?size>0)>
                                <#list problemList as item>
                                    <#if item_index==0>
                                        <textarea type="text" class="inp1 easyui-validatebox" id="problem0" name="problem" maxlength="500" style="width:70%;height: 60px">${item.VAL_!}</textarea>
                                        <a onclick="addProblem()"><img id="addProblem" title="新增本月工作中存在的问题" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                                    <#else>
                                        <div><textarea id="problem${item_index}" type="text" class="inp1 easyui-validatebox" name="problem" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px">${item.VAL_!}</textarea><a id="problem${item_index}" onclick="delProblem(this.id)" style="position:relative;left:10.7%"><img title="删除问题" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png"></a></div>
                                    </#if>
                                </#list>
                            <#else >
                                <textarea type="text" class="inp1 easyui-validatebox" id="problem0" name="problem" maxlength="500" style="width:70%;height: 60px"></textarea>
                                <a onclick="addProblem()"><img id="addProblem" title="新增本月工作中存在的问题" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\web-assets\_zhcs\simingqu\images\toyi\icon-add2.png"></a>
                            </#if>
                        </td>
                    </tr>
                </table>
            </div>
            <div >
                <div class="" style="position: relative;text-align: center; padding: 20px;margin-left:35%">
                    <a href="#" class="BigNorToolBtn SaveBtn" onclick="saveForm()" href="javascript:void(0)" >保存</a>
                    <a href="#" class="BigNorToolBtn ResetBtn" onclick="resetForm()" href="javascript:void(0)" >清空</a>
                </div>
            </div>
        </form>

    </div>
</div>
<script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    $(function () {
        $('.h-x').niceScroll({
            cursorcolor: "#ccc",
            cursoropacitymax: 0.8,
            cursorwidth: "4px",
            cursorborderradius: "2px",
            cursorborder: 'none',
            autohidemode: false,
        });
        // 左侧线的高度
        var ulHeight = $('.mr-lb-list').height();
        $('.mr-lb-line').height(ulHeight - 16);

        // 左侧的点击事件
        $('.mr-lb-list').on('click','li',function(){
            $(this).addClass('active').siblings().removeClass('active');
        })
        // 右边内容的锚点切换
        var mainPage = $(".mr-rbb-item");
        var mainTopArr = new Array();
        for(var i=0;i<mainPage.length;i++){
            var top = mainPage.eq(i)[0].offsetTop;
            mainTopArr.push(top-10);
        }
        function abc(){
            var scrollTop = $(this).scrollTop();
            var k;
            for(var i=1;i<=mainTopArr.length;i++){
                if(scrollTop>=mainTopArr[i]){
                    k=i-1;
                }else if(scrollTop <= (mainTopArr[1] - 120)){
                    $('.mr-lb-list').children('li').removeClass('active');
                }
            }
            $('.mr-lb-list').find("li").eq(k).addClass("active").siblings().removeClass("active");
        }
        $('.mr-rb-box').on('scroll',abc);
		 loadImg1();
		loadImg2();
		loadImg3();
		loadImg4();
		$.ajax({
			type: "POST",
			url: '${COMPONENTS_URL}/org/getAssemblyOrgTree.jhtml?callback=?&defaultOrgCode=${infoOrgCode}&startgridId=${gridId}&id=${gridId}',
			dataType:"jsonp",
			async:false,
			success: function(data){
				for(var i=0,l=data.length,j=data.length-1;i<l;i++,j--){
					gridObj[data[i].text] = j;
				}
				var height = (data.length*50+100)+"px";
				$("#img5").height(height);
				$("#img6").height(height);
				$("#img8").height(height);
				$("#img10").height(height);
				$("#img12").height(height);
				loadImg5();
				loadImg6();
				loadImg7();
				loadImg8();
				loadImg9();
				loadImg10();
				loadImg11();
				loadImg12();
				loadImg13();
					
			},
			error:function(data){
				$.messager.alert('错误','连接超时！','error');
			}
		});
       
    });
    
    var gridObj = {};
    
    function editOverallSituation() {
    	$('#editDiv').css({'height':$(window).height()*0.8+'px'});
        $('#editDiv').parent().css('margin-top',(parseInt(($(window).height()*(-0.48)))+parseInt(400))+'px');
        $('#editDiv').parent().show();
    }

    //保存按钮
    function saveForm(){
        var options = {
            success:function (data) {
                $.messager.alert("提示","编辑成功！")
                $('#editDiv').parent().hide();
                location.reload();
            }
        }
        $('#editForm').ajaxSubmit(options);
    }

    //重置按钮
    function resetForm() {
        $('.inp1').val('');
    }

    //民安类问题主要特点的增加与删除Input框
    var mainFeatureCSFlag=${mainFeatureCSList?size};
    var aFlagCS=${mainFeatureCSList?size};
    function addMainFeatureCS(){
        mainFeatureCSFlag++;
        aFlagCS++;
        if(mainFeatureCSFlag>6){
            mainFeatureCSFlag--;
            return;
        }
        $('<div id=mainFeatureCSDiv'+mainFeatureCSFlag+'>'+'<textarea id="mainFeatureCS'+mainFeatureCSFlag+'" type="text" class="inp1 easyui-validatebox" name="mainFeatureCS" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px"></textarea>'+'<a id="mainFeatureCSA'+aFlagCS+'" onclick="delMainFeatureCS(this.id)" style="position:relative;left:10.7%">'+'<img title="删除民安类问题主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png">'+'</a>'+'</div>').appendTo($('#mainFeatureCSTd'));
    }

    function delMainFeatureCS(aFlag){
        $('#'+aFlag).parent().remove();
        mainFeatureCSFlag--;
    }

    //民生类问题主要特点的增加与删除Input框
    var mainFeatureCLFlag=${mainFeatureCLList?size};
    var aFlagCL=${mainFeatureCLList?size};
    function addMainFeatureCL(){
        mainFeatureCLFlag++;
        aFlagCL++;
        if(mainFeatureCLFlag>6){
            mainFeatureCLFlag--;
            return;
        }
        $('<div id=mainFeatureCLDiv'+mainFeatureCLFlag+'>'+'<textarea id="mainFeatureCL'+mainFeatureCLFlag+'" type="text" class="inp1 easyui-validatebox" name="mainFeatureCL" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px"></textarea>'+'<a id="mainFeatureCLA'+aFlagCL+'" onclick="delMainFeatureCL(this.id)" style="position:relative;left:10.7%">'+'<img title="删除民生类问题主要特点" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png">'+'</a>'+'</div>').appendTo($('#mainFeatureCLTd'));

    }

    function delMainFeatureCL(aFlag){
        $('#'+aFlag).parent().remove();
        mainFeatureCLFlag--;
    }

    //问题分析的增加与删除Input框
    var problemFlag=${problemList?size};
    var aFlagProblem=${problemList?size};
    function addProblem() {
        problemFlag++;
        aFlagProblem++;
        if(problemFlag>5){
            problemFlag--;
            return;
        }
        $('<div id=problemDiv'+problemFlag+'>'+'<textarea id="problem'+problemFlag+'" type="text" class="inp1 easyui-validatebox" name="problem" maxlength="500" style="width:70%;height:60px;position:relative;left:10.7%;margin-right:4px"></textarea>'+'<a id="problem'+aFlagProblem+'" onclick="delProblem(this.id)" style="position:relative;left:10.7%">'+'<img title="删除问题" style="cursor: pointer;" width="23px" alt="" src="${uiDomain}\\web-assets\\_zhcs\\simingqu\\images\\toyi\\icon-del.png">'+'</a>'+'</div>').appendTo($('#problemTd'));
    }

    function delProblem(aFlag) {
        $('#'+aFlag).parent().remove();
        problemFlag--; 
    }
	var colorArr = ['#D87C7C','#919E8B','#D7AB82','#6E7074','#61A0A8','#EFA18D','#787464','#CC7E63','#724E58','#626F8A'];
	var colorBarArr = ['#D7AB82','#D87C7C','#919E8B','#6E7074','#61A0A8','#EFA18D','#787464','#CC7E63','#724E58','#626F8A'];
	var gridIndex=0,gridName='';
    function loadImg5() {
        var legendData = [], allCountData = [],reportCountData = [],disposalCountData = [];
        <#if cityList2??>
        <#list cityList2  as listObj>
		gridName = "${listObj.gridName}";gridIndex = gridObj[gridName];<#if gridLevel="市">if(gridIndex == undefined){gridIndex =${cityList2?size}-1; };</#if>
        legendData[gridIndex]=gridName;
        allCountData[gridIndex]=${listObj.allCounts};
        reportCountData[gridIndex]=${listObj.reportCounts};
        disposalCountData[gridIndex]=${listObj.disposalCounts};
        </#list>
        </#if>
        var img5Chart = echarts.init(document.getElementById('img5'));
		chartOption.yAxis.data = legendData;
		chartOption.series[0].data = allCountData;
		chartOption.series[1].data = reportCountData;
		chartOption.series[2].data = disposalCountData;
        img5Chart.setOption(chartOption);
    }
	
	var globalFontSize = 17,numFontSize=14,numFontSize2=12;
	
    //图片加载区
	var pieOption = {
            color:colorArr,
            tooltip: {
                trigger: 'item',
                formatter: '{b} : {d}%\n ({c})'
            },
            legend: {
                show: false
            },
            animation:false,
            series: [{
                name: '总体情况',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                data: [],
                itemStyle:{
                    normal:{
                        label:{
                            show:true, fontSize: globalFontSize,
                            formatter: '{b} : {d}% \n({c})' 
                        },
                        lableLine:{
                            show:true
                        }
                    },
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        }
	var chartOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },color: colorBarArr,
            legend: {
                data:['事项上报总数','有效事项上报数', '有效事项办结数'],
				textStyle:{fontSize :globalFontSize}
            },
            animation:false,
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                boundaryGap: [0, 0.01],
				 axisLabel: {
                   fontSize:numFontSize2
                }
            },
            yAxis: {
                type: 'category',
                data:[],
				 axisLabel: {
                   fontSize:globalFontSize
                }

            },
            series: [
                {
                    name: '事项上报总数',
                    type: 'bar',
                    data: [],
                    label:{
                        show:true,
                        position: 'right',  
						fontSize:numFontSize2
                    }
                },{
                    name: '有效事项上报数',
                    type: 'bar',
                    data: [],
                    label:{
                        show:true,
                        position: 'right',  
						fontSize:numFontSize2
                    }
                },
                {
                    name: '有效事项办结数',
                    type: 'bar',
                    data: [],
                    label:{
                        show:true,
                        position: 'right',  
						fontSize:numFontSize2
                    }
                }
            ]
        };
	
    function loadImg6() {
        var legendData = [];
        var reportCountData = [],allCountData=[];
        var disposalCountData = [];
        <#if cmdList11??>
        <#list cmdList11 as listObj>
			gridName = "${listObj.gridName}";gridIndex = gridObj[gridName];
			legendData[gridIndex]=gridName;
			allCountData[gridIndex]=${listObj.allCounts};
			reportCountData[gridIndex]=${listObj.reportCounts};
			disposalCountData[gridIndex]=${listObj.disposalCounts};
        </#list>
        </#if>
        var img6Chart = echarts.init(document.getElementById('img6'));
        chartOption.yAxis.data = legendData;
		chartOption.series[0].data = allCountData;
		chartOption.series[1].data = reportCountData;
		chartOption.series[2].data = disposalCountData;
        img6Chart.setOption(chartOption);
    }
    function loadImg7() {
        var seriesData = [];
        <#if cmdList2??>
        <#list cmdList2 as cmdList2Text>
        seriesData.push({name:"${cmdList2Text.eventType}",value:${cmdList2Text.reportCounts}        });
        </#list>
        </#if>
		
        var img7Chart = echarts.init(document.getElementById('img7'));
		pieOption.series[0].data = getPieDataCross(seriesData);
        img7Chart.setOption(pieOption);
    }

    function loadImg8() {
        var legendData = [];
        var reportCountData = [],allCountData=[];
        var disposalCountData = [];
        <#if gridManList11??>
        <#list gridManList11 as listObj>
			gridName = "${listObj.gridName}";gridIndex = gridObj[gridName];
			legendData[gridIndex]=gridName;
			allCountData[gridIndex]=${listObj.allCounts};
			reportCountData[gridIndex]=${listObj.reportCounts};
			disposalCountData[gridIndex]=${listObj.disposalCounts};
        </#list>
        </#if>
        var img8Chart = echarts.init(document.getElementById('img8'));
         chartOption.yAxis.data = legendData;
		chartOption.series[0].data = allCountData;
		chartOption.series[1].data = reportCountData;
		chartOption.series[2].data = disposalCountData;
        img8Chart.setOption(chartOption);
    }

    function loadImg9() {
        var seriesData = [];
        <#if gridManList2??>
        <#list gridManList2 as gridManList2Text>
        seriesData.push({name:"${gridManList2Text.eventType}",value:${gridManList2Text.reportCounts}});
        </#list>
        </#if>

        var img9Chart = echarts.init(document.getElementById('img9'));
		pieOption.series[0].data = getPieDataCross(seriesData);
        img9Chart.setOption(pieOption);
    }

    function loadImg10() {
        var legendData = [];
        var reportCountData = [],allCountData=[];
        var disposalCountData = [];
        var str;
        <#if volunteerList11??>
        <#list volunteerList11 as listObj>
			gridName = "${listObj.gridName}";gridIndex = gridObj[gridName];
			legendData[gridIndex]=gridName;
			allCountData[gridIndex]=${listObj.allCounts};
			reportCountData[gridIndex]=${listObj.reportCounts};
			disposalCountData[gridIndex]=${listObj.disposalCounts};
        </#list>
        </#if>
        var img10Chart = echarts.init(document.getElementById('img10'));
        chartOption.series[0].data = allCountData;
		chartOption.series[1].data = reportCountData;
		chartOption.series[2].data = disposalCountData;
        img10Chart.setOption(chartOption);
    }

    function loadImg11() {
        var seriesData = [];
        <#if volunteerList2??>
        <#list volunteerList2 as volunteerList2Text>
        seriesData.push({name:"${volunteerList2Text.eventType}",value:${volunteerList2Text.reportCounts}});
        </#list>
        </#if>

        var img11Chart = echarts.init(document.getElementById('img11'));
		pieOption.series[0].data = getPieDataCross(seriesData);
        img11Chart.setOption(pieOption);
    }

    function loadImg12() {
        var legendData = [];
        var reportCountData = [];
        var disposalCountData = [],allCountData=[];
        <#if dutyCenterList11??>
        <#list dutyCenterList11 as listObj>
			gridName = "${listObj.gridName}";gridIndex = gridObj[gridName];
			legendData[gridIndex]=gridName;
			allCountData[gridIndex]=${listObj.allCounts};
			reportCountData[gridIndex]=${listObj.reportCounts};
			disposalCountData[gridIndex]=${listObj.disposalCounts};
        </#list>
        </#if>
        var img12Chart = echarts.init(document.getElementById('img12'));
        chartOption.yAxis.data = legendData;
        chartOption.series[0].data = allCountData;
		chartOption.series[1].data = reportCountData;
		chartOption.series[2].data = disposalCountData;
        img12Chart.setOption(chartOption);
    }

    function loadImg13() {
        var seriesData = [];
        <#if dutyCenterList2??>
        <#list dutyCenterList2 as dutyCenterList2Text>
        seriesData.push({name:"${dutyCenterList2Text.eventType}",value:${dutyCenterList2Text.reportCounts}});
        </#list>
        </#if>

        var img13Chart = echarts.init(document.getElementById('img13'));
		pieOption.series[0].data = getPieDataCross(seriesData);
        img13Chart.setOption(pieOption);
    }

    function exportWord() {
        var img1Chart =echarts.getInstanceByDom(document.getElementById('image1'))
        var img1Data =  img1Chart.getDataURL();
        var img2Chart =echarts.getInstanceByDom(document.getElementById('image2'))
        var img2Data =  img2Chart.getDataURL();
        var img3Chart =echarts.getInstanceByDom(document.getElementById('image3'))
        var img3Data =  img3Chart.getDataURL();
        var img4Chart =echarts.getInstanceByDom(document.getElementById('image4'))
        var img4Data =  img4Chart.getDataURL();
            var img5Chart =echarts.getInstanceByDom(document.getElementById('img5'))
            var img5Data =  img5Chart.getDataURL();
            var img6Chart =echarts.getInstanceByDom(document.getElementById('img6'))
            var img6Data =  img6Chart.getDataURL();
            var img7Chart =echarts.getInstanceByDom(document.getElementById('img7'))
            var img7Data =  img7Chart.getDataURL();
            var img8Chart =echarts.getInstanceByDom(document.getElementById('img8'))
            var img8Data =  img8Chart.getDataURL();
            var img9Chart =echarts.getInstanceByDom(document.getElementById('img9'))
            var img9Data =  img9Chart.getDataURL();
            var img10Chart =echarts.getInstanceByDom(document.getElementById('img10'))
            var img10Data =  img10Chart.getDataURL();
            var img11Chart =echarts.getInstanceByDom(document.getElementById('img11'))
            var img11Data =  img11Chart.getDataURL();
            var img12Chart =echarts.getInstanceByDom(document.getElementById('img12'))
            var img12Data =  img12Chart.getDataURL();
            var img13Chart =echarts.getInstanceByDom(document.getElementById('img13'))
            var img13Data =  img13Chart.getDataURL();
        var j = $('<form id="eventQueryForm" method="post" action="${rc.getContextPath()}/zhsq/zzgl/briefingController/exportWord.jhtml"></form>')
        j.appendTo($("#im"));
        $('<input type="hidden" id="txtName" name="txtName" value="简报" />').appendTo($("#eventQueryForm"));
        $('<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${infoOrgCode}" />').appendTo($("#eventQueryForm"));
        $('<input type="hidden" id="gridId" name="gridId" value="${gridId}" />').appendTo($("#eventQueryForm"));
        /*特点*/
        var temp9 = $('<input type="hidden"  name="dateStr"  value="${dateStr!}" />');
        temp9.appendTo($("#eventQueryForm"));
        var temp4 = $('<input type="hidden"  name="overallSituation"  value="${overallSituation!}" />');
        temp4.appendTo($("#eventQueryForm"));
        var temp5 = $('<input type="hidden"  name="typeAnalysis"  value="${typeAnalysis!}" />');
        temp5.appendTo($("#eventQueryForm"));
        var temp6 = $('<input type="hidden"  name="reportChannel"  value="${reportChannel!}" />');
        temp6.appendTo($("#eventQueryForm"));
        var temp7 = $('<input type="hidden"  name="cityLevelSituation"  value="${cityLevelSituation!}" />');
        temp7.appendTo($("#eventQueryForm"));
        var temp8 = $('<input type="hidden"  name="townLevelSituation"  value="${townLevelSituation!}" />');
        temp8.appendTo($("#eventQueryForm"));
        <#if problemList?? && (problemList?size>0)>
            <#list problemList as item>
                var str = "${item.VAL_?replace("\n","<w:br/>")}";
                var temp1 = $('<textarea style="display: none"  name="problemList"  value="" ></textarea>');
                temp1.val(str)
                temp1.appendTo($("#eventQueryForm"));
            </#list>
        </#if>
        <#if mainFeatureCSList?? && (mainFeatureCSList?size>0)>
            <#list mainFeatureCSList as item>
                var str = "${item.VAL_?replace("\n","<w:br/>")}";
                var temp2 = $('<textarea style="display: none"  name="mainFeatureCSList"  value="" ></textarea>');
                temp2.val(str)
                temp2.appendTo($("#eventQueryForm"));
            </#list>
        </#if>
        <#if mainFeatureCLList?? && (mainFeatureCLList?size>0)>
            <#list mainFeatureCLList as item>
                var str = "${item.VAL_?replace("\n","<w:br/>")}";
                var temp3 = $('<textarea style="display: none"  name="mainFeatureCLList"  value="" ></textarea>');
                temp3.val(str)
                temp3.appendTo($("#eventQueryForm"));
            </#list>
        </#if>

        var img1Text = $('<textarea  id="img1Data" name="img1Data" style="display: none" ></textarea>')
        img1Text.val(img1Data)
        img1Text.appendTo($("#eventQueryForm"))

        var img2Text = $('<textarea  id="img2Data" name="img2Data" style="display: none" ></textarea>')
        img2Text.val(img2Data)
        img2Text.appendTo($("#eventQueryForm"))

        var img3Text = $('<textarea  id="img3Data" name="img3Data" style="display: none" ></textarea>')
        img3Text.val(img3Data)
        img3Text.appendTo($("#eventQueryForm"))

        var img4Text = $('<textarea  id="img4Data" name="img4Data" style="display: none" ></textarea>')
        img4Text.val(img4Data)
        img4Text.appendTo($("#eventQueryForm"))

        var img5Text = $('<textarea  id="img5Data" name="img5Data" style="display: none" ></textarea>')
        img5Text.val(img5Data)
        img5Text.appendTo($("#eventQueryForm"))

        var img6Text = $('<textarea  id="img6Data" name="img6Data" style="display: none" ></textarea>')
        img6Text.val(img6Data)
        img6Text.appendTo($("#eventQueryForm"))

        var img7Text = $('<textarea  id="img7Data" name="img7Data" style="display: none" ></textarea>')
        img7Text.val(img7Data)
        img7Text.appendTo($("#eventQueryForm"))

        var img8Text = $('<textarea  id="img8Data" name="img8Data" style="display: none" ></textarea>')
        img8Text.val(img8Data)
        img8Text.appendTo($("#eventQueryForm"))

        var img9Text = $('<textarea  id="img9Data" name="img9Data" style="display: none" ></textarea>')
        img9Text.val(img9Data)
        img9Text.appendTo($("#eventQueryForm"))

        var img10Text = $('<textarea  id="img10Data" name="img10Data" style="display: none" ></textarea>')
        img10Text.val(img10Data)
        img10Text.appendTo($("#eventQueryForm"))

        var img11Text = $('<textarea  id="img11Data" name="img11Data" style="display: none" ></textarea>')
        img11Text.val(img11Data)
        img11Text.appendTo($("#eventQueryForm"))

        var img12Text = $('<textarea  id="img12Data" name="img12Data" style="display: none" ></textarea>')
        img12Text.val(img12Data)
        img12Text.appendTo($("#eventQueryForm"))

        var img13Text = $('<textarea  id="img13Data" name="img13Data" style="display: none" ></textarea>')
        img13Text.val(img13Data)
        img13Text.appendTo($("#eventQueryForm"))
        $("#eventQueryForm").submit();
        $("#eventQueryForm").remove();
    }
    //加载图一
    function loadImg1(){
        var image1 = echarts.init(document.getElementById('image1'), 'light');
        var data = genData1(2);
		pieOption.series[0].data = data.seriesData;
        image1.setOption(pieOption);

        function genData1(count) {
            var legendData = [];
            var seriesData = [];
            legendData.push("民生类事项");
            legendData.push("民安类事项");
            seriesData.push({
                name: "民生类事项",
                value: ${peopleLifeEvent!}
            });
            seriesData.push({
                name: "民安类事项",
                value: ${peopleSafeEvent!}
            })

            return {
                legendData: legendData,
                seriesData: seriesData,
            };
        }
    }

    //图二
    function loadImg2(){
        var image2 = echarts.init(document.getElementById('image2'), 'light');
        var data = genData2(2);
        pieOption.series[0].data = data.seriesData;
        image2.setOption(pieOption);

        function genData2(count) {
            var legendData = [];
            var seriesData = [];
            <#if peopleSafeListMap??>
            <#list peopleSafeListMap as peopleSafeTypeEventMap>
            legendData.push("${peopleSafeTypeEventMap.eventType!}");
            seriesData.push({
                name:" ${peopleSafeTypeEventMap.eventType!}",
                value: ${peopleSafeTypeEventMap.thisReportTotal!}
            });
            </#list>
            </#if>
            return {
                legendData: legendData,
                seriesData: seriesData,
            };
        }
    }

    //图三
    function loadImg3() {
        var image3 = echarts.init(document.getElementById('image3'), 'light');
        var data = genData3(8);
        pieOption.series[0].data = data.seriesData;
        image3.setOption(pieOption);

        function genData3(count) {
            var legendData = [];
            var valueList = [];
            <#if peopleLifeListMap??>
            <#list peopleLifeListMap as peopleLifeTypeEventMap>
            legendData.push("${peopleLifeTypeEventMap.eventType!}");
            valueList.push(${peopleLifeTypeEventMap.thisReportTotal!});
            </#list>
            </#if>
            var seriesData = [];
            for (var i = 0; i < count; i++) {
                seriesData.push({
                    name: legendData[i],
                    value: valueList[i]
                });
            }

            return {
                legendData: legendData,
                seriesData: getPieDataCross(seriesData)
            };
        }
    }
	//饼图交叉排布
	function getPieDataCross(data){
		var temp = null,newData=[];
		for (var i=0,len = data.length; i<len; i++) {
			for ( var j=0,l = len-1-i; j<l; j++) { 
				if (data[j].value > data[j+1].value) { 
					temp = data[j];
					data[j] = data[j+1];
					data[j+1] = temp;
				}
			}
		}
		for(var i=0,h=parseInt(data.length/2),l=data.length;i<h;i++){
			newData.push(data[i]);
			newData.push(data[l-i-1]);
		}
		if(data.length%2==1){
			newData.push(data[parseInt(data.length/2)]);
		}
		return newData;
	}



    //图四
    function loadImg4() {

        function fetchData(cb) {
            // 通过 setTimeout 模拟异步加载
            setTimeout(function() {
                var tempData1 = [];
                var tempData2 = [];
                var tempData3 = [];
                var tempCategories = [];
                <#if tableData3??>
                <#list tableData3 as table3>
                    <#if gridLevel="省">
                        <#if table3.channel="网格员"||table3.channel="综治中心"||table3.channel="平安志愿者"||table3.number=4>
                            <#if table3.number=4>
                                tempCategories.push("省、市、县综治责任单位");
                            <#else >
                                tempCategories.push("${table3.channel!}");
                            </#if>
                            tempData1.push(${table3.reportCounts!});
	                       tempData2.push(${table3.disposalCounts!});
                            tempData3.push(${table3.allCounts!});
                        </#if>
                    <#else>
                        <#if table3.channel="网格员"||table3.channel="综治中心"||table3.channel="平安志愿者"||table3.number=4>
                            <#if table3.number=4>
                                tempCategories.push("市、县综治责任单位");
                            <#else >
                                tempCategories.push("${table3.channel!}");
                            </#if>
                            tempData1.push(${table3.reportCounts!});
                            tempData2.push(${table3.disposalCounts!});
                            tempData3.push(${table3.allCounts!});
                        </#if>
                    </#if>
                </#list>
                </#if>
                
                cb({
                    categories: tempCategories,
                    data1: tempData1,
                    data2: tempData2,
                    data3: tempData3
                });
            }, 200);
        }
        var image4 = echarts.init(document.getElementById('image4'), 'light');
        // 显示标题，图例和空的坐标轴
        image4.showLoading();
        image4.setOption({
            // 普通样式。
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
			color: colorBarArr,
            legend: {
                data:['事项上报总数','有效事项上报数', '有效事项办结数'],show:true,
				textStyle:{fontSize :globalFontSize}
            },
            animation:false,
            xAxis: {
                data: [],
                axisLine: { // 坐标轴刻度相关设置。
                    show: true,
                },
                axisLabel: {
                    color: 'black',
                    interval: 0,
                    fontSize: globalFontSize,
                },
                splitLine: {
                    show: false
                },
            },
            yAxis: {
                splitNumber: 6,
                min: 0, // 设置y轴刻度的最小值
                axisLine: { // 坐标轴刻度相关设置。
                    show: true,
                },
                axisLabel: {
                    fontSize: numFontSize,
                },
                splitLine: {
                    show: true
                }
            },
            series: [{
                name: '事项上报总数',
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{c}',
                            position: 'top', //在上方显示
                            fontSize: numFontSize,
                         //   distance :30
                        }
                    }
                },
                data: [],
                barWidth:40,
            },{
                name: '有效事项上报数',
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{c}',
                            position: 'top', //在上方显示
                           fontSize: numFontSize,
                          // distance :0
                        },
                    }
                },
                data: [],
                barWidth: 40,
            }, {
                name: '有效事项办结数',
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: true,
                            formatter: '{c}',
                            position: 'top', //在上方显示
                            fontSize: numFontSize,
                         //  distance :15
                        }
                    }
                },
                data: [],
                barWidth: 40,
            }]
        });

        // 异步加载数据
        fetchData(function(data) {
            image4.setOption({
                xAxis: {
                    data: data.categories
                },
                series: [{
                    name: '事项上报总数',
                    data: data.data3
                },{
                    // 根据名字对应到相应的系列
                    name: '有效事项上报数',
                    data: data.data1
                }, {
                    name: '有效事项办结数',
                    data: data.data2
                }]
            });
            image4.hideLoading();
        });
    }

</script>
</body>
</html>