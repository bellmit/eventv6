<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>矛盾纠纷弹窗(未匹配到当事人案件列表 和 疑似集中上报案件列表)</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/layui-v2.5.5/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-5.4.1/package/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/mask/event-mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/sp-v2-tc.css" />
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/data.css" />
    <link rel="stylesheet" href="${uiDomain}/js/paging/paging.css">
    <style>
        #gridName,#disputeType2Str{
            color:#ffffff!important;
            background: url("${ANOLE_COMPONENT_URL}/js/components/combobox/images/sys_07.png") right center no-repeat rgba(5, 17, 34, .5)!important;
        }
        .ztree li a {
            color: #ffffff!important;
        }
        .maed-c-left1 {
            position: relative;
            width: 100%;
            height: 4.2rem;
        }
        .maed-clb-list li>div>p:first-child{
            width:1.26rem!important;
        }
        .maed-clb-list li>div>p:last-child{
            width: calc(100% - 1.26rem);
        }
    </style>
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
<div class="mask-container mask-container1 niceitem bs plr10 prr10">
    <div class="sj-c-table sj-c-table1 bs show">
        <div class="clearfix filter-box posi-rela flex">
            <div class="filter-item mlr20 fl">
                <p>关键字</p>
                <input type="text" class="fl bs mlr10" id="keywords">
            </div>
            <div class="filter-ann fl mlr20 j-click-div">
                高级筛选
            </div>
            <div class="filter-ann-box hide">
                <!-- 内容 -->

                <div class="plr20 prr20 ptr30 pbr30 filter-ann-box-div clearfix">
                    <div class="filter-item fl ">
                        <p>发生时间</p>
                        <div class="fi-time fl bs mlr10 clearfix font-size-0">
                            <input type="text" class="demo-input" placeholder="日期范围" id="test5">
                                <!-- <p>2020-06-18 09:30:29</p> -->
                            <i></i>
                        </div>
                    </div>
                    <div class="filter-item fl mlr20">
                        <p>所属区域</p>
                        <#--<input type="text" class="fl bs mlr10">-->
                        <input type="hidden" id="gridId" name="gridId" value="${startGridId?c}">
                        <input type="hidden" id="orgCode" name="orgCode" value="${orgCode}">
                        <input name="gridName" id="gridName" type="text" class="fl bs mlr10" value="${startGridName!}"/>
                    </div>
                    <div class="filter-item fl mlr20">
                        <p>事件类别</p>
                        <input type="hidden" id="disputeType2" name="disputeType2" value="">
                        <input type="hidden" id="disputeTypeList" name="disputeTypeList" value=""/>
                        <input name="disputeType2Str" id="disputeType2Str" type="text" class="fl bs mlr10"/>
                    </div>
                </div>
                <i class="if-change-i"></i>
            </div>
            <div class="filter-btn fl mlr20 clearfix">
                <a href="javascript:getDataList(1);" class="fl fb-confirm bs">确认</a>
                <a href="javascript:resetData();" class="fl mlr10 fb-reset bs">重置</a>
            </div>
        </div>
        <div class="sj-ct-top mtr10">
            <div class="ts-rbi3p-thead bs">
                <table class="ts-rbip-table">
                    <colgroup>
                        <col width='15%'>
                        <col width='13%'>
                        <col width='13%'>
                        <col width='12%'>
                        <col width='12%'>
                        <col width='20%'>
                        <col width='15%'>
                    </colgroup>
                    <thead>
                        <tr>
                            <th>事件名称</th>
                            <th>发生日期</th>
                            <th>采集日期</th>
                            <th>事件类别</th>
                            <th>事件规模</th>
                            <th>发生地点</th>
                            <th>所属区域</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="ts-rbi3p-tbody ts-rbi3p-tbody1 niceitem bs">
                <table class="ts-rbip-table">
                    <colgroup>
                        <col width='15%'>
                        <col width='13%'>
                        <col width='13%'>
                        <col width='12%'>
                        <col width='12%'>
                        <col width='20%'>
                        <col width='15%'>
                    </colgroup>
                    <tbody id="listData">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="sj-ct-pagination" id="pageDiv">
            <div id="pageTable"></div>
        </div>
    </div>
    <div class="sj-c-details sj-c-details1 bs hide">
        <div class="flex sj-back-box">
            <a href="javascript:void(0);" class="flex flex-ac sj-back">
                <i></i>
                <p>返回</p>
            </a>
        </div>
        <div class="maed-content mtr45">
            <div class="clearfix prr30">
                <div class="maed-c-left1 niceitem fl">
                    <p class="me-cl-title" id="detail-eventName"></p>
                    <p class="me-cl-small-title" id="detail-eventType"></p>
                    <div class="maed-cl-top bs niceitem">
                        <p class="maed-clt-title">于<span id="detail-eventTime"></span>在<span id="detail-eventAddr"></span>发生：</p>
                        <p class="maed-clt-text" id="detail-eventContent"></p>
                    </div>
                    <div class="maed-cl-bottom niceitem mtr15 bs">
                        <ul class="maed-clb-list bs">
                            <li class="clearfix">
                                <div class="maed-w100 fl clearfix">
                                    <p>所属网格：</p>
                                    <p id="detail-eventOrgName"></p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fl clearfix">
                                    <p>事件编号：</p>
                                    <p id="detail-eventCode"></p>
                                </div>
                                <div class="maed-w50 fr clearfix">
                                    <p>事件规模：</p>
                                    <p id="detail-eventScale"></p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fl clearfix">
                                    <p>涉及单位：</p>
                                    <p id="detail-eventUnit"></p>
                                </div>
                                <div class="maed-w50 fr clearfix">
                                    <p>涉及人数：</p>
                                    <p id="detail-eventPeoNum"></p>
                                </div>
                            </li>
                            <li class="clearfix">
                                <div class="maed-w50 fl clearfix">
                                    <p>预警等级：</p>
                                    <p id="detail-eventWarningLevel"></p>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
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
                        <ul class="layer_aj_bt_items" id="flowInfo">
                            <li class="flex flex-ac">
                                <h5 class="aj_items_h aj_items_h_green">事件采集</h5>
                                <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <div class="aj_items_t2 flex">
                                    <p class="aj_items_t aj_items_t_green bs">李华(综合执法部门)<span> 耗时
                                            2分钟</span><br><span>办理时间</span>：2018-02-24 10:19:21</p>
                                </div>
                                <p class="aj_items_t aj_items_t_result aj_items_tr_blue">因为处理问题，提出的意见和某某建议</p>
                            </li>
                            <li class="flex flex-ac">
                                <h5 class="aj_items_h aj_items_h_green">事件采集</h5>
                                <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <div class="aj_items_t2 flex">
                                    <p class="aj_items_t aj_items_t_green bs">李华(综合执法部门)<span> 耗时
                                            2分钟</span><br><span>办理时间</span>：2018-02-24 10:19:21</p>
                                </div>
                                <p class="aj_items_t aj_items_t_result aj_items_tr_blue">因为处理问题，提出的意见和某某建议</p>
                            </li>
                            <li class="flex flex-ac">
                                <h5 class="aj_items_h aj_items_h_green">事件采集</h5>
                                <div class="aj_ks aj_ks_gray">
                                    <div class="aj_ks1"></div>
                                    <div class="aj_ks2"></div>
                                </div>
                                <div class="aj_items_t2 flex">
                                    <p class="aj_items_t aj_items_t_green bs">李华(综合执法部门)<span> 耗时
                                            2分钟</span><br><span>办理时间</span>：2018-02-24 10:19:21</p>
                                </div>
                                <p class="aj_items_t aj_items_t_result aj_items_tr_blue">因为处理问题，提出的意见和某某建议</p>
                            </li>
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
<script src="${uiDomain}/web-assets/plugins/layui-v2.5.5/layui/layui.js"></script>
<script src="${uiDomain}/js/paging/pagingV2.js"  charset="utf-8"  type="text/javascript"></script>
<#include "/component/ComboBox.ftl">
<script>
    var startGridId = ${startGridId?c};
    var startGridName = '${startGridName}';
    var orgCode = '${orgCode}';
    AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(value,row){
        $("#orgCode").val(row[0].gridCode)
    }, {
        Async: {
            enable: true,
            autoParam: ["id=gridId"],
            dataFilter: _filter,
//					otherParam : {
//						"startGridId" : startGridId
//					}
        }
    });
    AnoleApi.initTreeComboBox("disputeType2Str", "disputeType2", "B799", function(value,obj){
            if(obj[0].check_Child_State == '0'){
                var disputeTypelist = '';
                for (const uaElement of obj[0].children) {
                    disputeTypelist+=uaElement.value+","
                }
                disputeTypelist = disputeTypelist.substring(0,disputeTypelist.length-1)
                $("#disputeTypeList").val(disputeTypelist)
            }else if(obj[0].check_Child_State == '-1'){
                $("#disputeTypeList").val("")
            }
        }, [],
        {
            ChooseType: "1",
            EnabledSearch: true,
            ShowOptions: {
                EnableToolbar : true
            },OnCleared:function(){
                $("#disputeTypeList").val("");
            }
        });
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


    // 返回
    $('.sj-back-box').on('click',function(){
        $('.sj-c-details').hide().siblings().fadeIn(300)
    })
    // 方法一
    layui.use(['laydate','layer'], function () {
        var laydate = layui.laydate;
        var layer = layui.layer;
        //日期时间选择器
        laydate.render({
            elem: '#test5'
            ,range: true
        });
    });
    // 高级搜索
    $('.j-click-div').on('click',function(){
        $('.filter-ann-box').slideToggle(300)
    })
    $('.if-change-i').on('click',function(){
        $(this).parent().slideUp(300)
    })
    var listType = '${listType}';
    var infoOrgCode = '${orgCode}';
    var curPage = 1;
    var rows = 10;
    var nowYear = '${nowYear}';
    var month = '${month}';
    var createStart = '';
    var createEnd = '';

    if(month == '' || month == undefined || month == null){
        createStart = nowYear + "-01-01";
        createEnd = nowYear + "-12-31";
    }else{
        getStartEndTime(nowYear,month);
    }

    getDataList(1);

    function resetData(){
        $("#keywords").val("");
        $("#orgCode").val("${orgCode}");
        $("#gridId").val("${startGridId?c}");
        $("#gridName").val("${startGridName}");
        $("#disputeType2").val("");
        $("#disputeTypeList").val("");
        $("#disputeType2Str").val("");
        $('#test5').val("");
        getDataList(1);
    }

    function getDataList(page){
        var keyWord = $("#keywords").val();
        var publishDateStr = $('#test5').val().split(' - ');
        var happenStart = publishDateStr[0],
            happenEnd = publishDateStr[1]?publishDateStr[1]:"";
        var disputeType = $("#disputeType2").val();
        var disputeTypeList = $("#disputeTypeList").val();
        if(disputeTypeList != null && disputeTypeList != ""){
            disputeTypeList = disputeType+","+disputeTypeList;
            disputeType = '';
        }
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/disputeMediation/bigScreen/screenListData/listData.json',
            data: {
                listType:listType,
                page:page,
                rows:rows,
                orgCode:$("#orgCode").val(),
                createStart:createStart,
                createEnd:createEnd,
                happenStart:happenStart,
                happenEnd:happenEnd,
                keyWord:keyWord,
                disputeType:disputeType,
                disputeTypeList:disputeTypeList,
            },
            dataType:"json",
            success: function(data){
                console.log(data);
                setListData(data.rows);

                if(page==1){
                    $("#pageTable").remove();
                    $("#pageDiv").html('<div id="pageTable" style="float: right"></div>');
                    $("#pageTable").createPage({
                        pageNum: (Math.floor((data.total-1)/rows)+1),
                        current: page,
                        backfun: function(e) {
                            getDataList(e.current);
                        }
                    });
                }
            }
        });
    }

    function setListData(data){
        var htmlStr = '';
        for (const obj of data) {
            var createTimeStr = new Date(obj.createTime).formatString("yyyy-MM-dd hh:mm:ss");
            htmlStr += '<tr>';
            htmlStr += '    <td class="sj-btn" data-id="'+obj.mediationId+'"><a href="javascript:(0);" title="'+obj.disputeEventName+'">'+obj.disputeEventName+'</a></td>';
            htmlStr += '    <td title="'+checkData(obj.happenTimeStr)+'">'+checkData(obj.happenTimeStr)+'</td>';
            htmlStr += '    <td title="'+checkData(createTimeStr)+'">'+checkData(createTimeStr)+'</td>';
            htmlStr += '    <td title="'+checkData(obj.disputeTypeStr)+'">'+checkData(obj.disputeTypeStr)+'</td>';
            htmlStr += '    <td title="'+checkData(obj.disputeScaleStr)+'">'+checkData(obj.disputeScaleStr)+'</td>';
            htmlStr += '    <td title="'+checkData(obj.happenAddr)+'">'+checkData(obj.happenAddr)+'</td>';
            htmlStr += '    <td title="'+checkData(obj.orgName)+'">'+checkData(obj.orgName)+'</td>';
            htmlStr += '</tr>';
        }
        $("#listData").html(htmlStr);

        // 选择详情
        $('.sj-btn').on('click',function(){
            $('.sj-c-table').hide().siblings().fadeIn(300)
            var mediationId = $(this).data("id");
            getDetailInfo(mediationId)
        })
    }

    function getDetailInfo(id){
        let load = layer.load(1);
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/disputeMediation/bigScreen/detailInfo/detail.json',
            data: {
                mediationId:id
            },
            dataType:"json",
            async:true,
            success: function(data){
                console.log(data);
                setDetailInfo(data)
                layer.close(load)
            }
        });
    }

    function setDetailInfo(data){
        //案件详细信息
        var dispute = data.disputeMediation;
        var scaleStr = "";
        if(dispute.disputeScale == '1'){
            scaleStr = "个体性事件";
        }else if(dispute.disputeScale == '2'){
            scaleStr = "群体性事件";
        }else if(dispute.disputeScale == '3'){
            scaleStr = "重大群体性事件";
        }
        var warningLevelStr = "";
        if(dispute.warningLevel == "01"){
            warningLevelStr = "一级（有可能引发群体性事件等严重影响社会稳定的事件纠纷）";
        }else if(dispute.warningLevel == "02"){
            warningLevelStr = "二级（有可能引发群体闹事苗头的事件纠纷）";
        }else if(dispute.warningLevel == "03"){
            warningLevelStr = "三级（有可能引发群体越级非正常访的事件纠纷）";
        }
        var disputeTypeStr = "";
        var disputeTypeDC = data.disputeTypeDC;
        for (const obj of disputeTypeDC) {
            if(obj.dictGeneralCode == dispute.disputeType2){
                disputeTypeStr = obj.dictName;
                break;
            }
        }
        $("#detail-eventName").text(checkData(dispute.disputeEventName));
        $("#detail-eventType").text(checkData(disputeTypeStr));
        $("#detail-eventTime").text(checkData(dispute.happenTimeStr));
        $("#detail-eventAddr").text(checkData(dispute.happenAddr));
        $("#detail-eventContent").text(checkData(dispute.disputeCondition));
        $("#detail-eventOrgName").text(checkData(dispute.gridPath));
        $("#detail-eventCode").text(checkData(dispute.mediationCode));
        $("#detail-eventScale").text(checkData(scaleStr));
        $("#detail-eventUnit").text(checkData(dispute.involvedOrgName));
        $("#detail-eventPeoNum").text(checkData(dispute.involveNum)+" (人)");
        $("#detail-eventWarningLevel").text(checkData(warningLevelStr));

        //流程信息
        var htmlStr = '';
        var flowList = data.disputeFlowInfos;
        for (var i = 0; i < flowList.length; i++) {
            var obj = flowList[i];
            htmlStr += '<li class="flex flex-ac">';
            htmlStr += '<div class="aj_items_h aj_items_h_green">'+checkData(obj.handleLinkName)+'</div>';
            htmlStr += '<div class="aj_ks aj_ks_'+(i==0?"blue":"gray")+'"><div class="aj_ks1"></div><div class="aj_ks2"></div></div>';
            htmlStr += '<div class="aj_items_t2 flex">';
            htmlStr += '<p class="aj_items_t aj_items_t_green bs">'+obj.handlerName+'<span>办理时间</span>：'+new Date(obj.handleTime).formatString("yyyy-MM-dd hh:mm:ss")+'</p>';
            htmlStr += '</div>';
            if(obj.handleOpinion != null && obj.handleOpinion != ''){
                htmlStr += '<p class="aj_items_t aj_items_t_result aj_items_tr_red">'+obj.handleOpinion+'</p>';
            }
        }
        $("#flowInfo").html(htmlStr);
    }

    function checkData(obj){
        if(obj == null || !obj || obj == 'undefined' || obj == ''){
            return '';
        }else{
            return obj;
        }
    }

    //通过年月获取传入年月的第一天和最后一天
    function getStartEndTime(nowYear,month){
        var new_year = nowYear;//取当前的年份
        var new_month = month;//取当前的月份
        var d = new Date(new_year,new_month,1);                //取当年当月中的第一天
        var lastDay = new Date(d.getTime()-1000*60*60*24).getDate();//获取当月最后一天日期
        if(d.getMonth() < 10){
            var mon = "0" + d.getMonth();
        }else{
            var mon = d.getMonth();
        }
        createStart=d.getFullYear() + '-' + mon + '-' + "0"+d.getDate();
        createEnd=d.getFullYear() + '-' + mon + '-' + lastDay;
    }

    Date.prototype.formatString = function(fmt){
        var o = {
            "M+" : this.getMonth()+1,                 //月份
            "d+" : this.getDate(),                    //日
            "h+" : this.getHours(),                   //小时
            "m+" : this.getMinutes(),                 //分
            "s+" : this.getSeconds(),                 //秒
            "q+" : Math.floor((this.getMonth()+3)/3), //季度
            "S"  : this.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    };
</script>
</body>
</html>