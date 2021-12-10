<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>矛盾纠纷弹窗(疑似上报用户列表)</title>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/common/css/reset.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/plugins/swiper-5.4.1/package/css/swiper.min.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/mask/event-mask.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/_big-screen/jiangxi-diyu/css/sp-v2-tc.css" />
    <link rel="stylesheet" href="${uiDomain}/js/paging/paging.css">
    <style>
        #gridName {
            color:#ffffff!important;
            background: url("${ANOLE_COMPONENT_URL}/js/components/combobox/images/sys_07.png") right center no-repeat rgba(5, 17, 34, .5)!important;
        }
        .ztree li a {
            color: #ffffff!important;
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
            <div class="clearfix filter-box flex">
                <div class="filter-item mlr20 fl">
                    <p>关键字</p>
                    <input type="text" class="fl bs mlr10" id="keywords">
                </div>
                <div class="filter-item fl mlr20">
                    <p>所属区域</p>
                    <input type="hidden" id="gridId" name="gridId" value="${startGridId?c}">
                    <input type="hidden" id="orgCode" name="orgCode" value="${orgCode}">
                    <input name="gridName" id="gridName" type="text" class="fl bs mlr10" value="${startGridName!}"/>
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
                            <col width='20%'>
                            <col width='20%'>
                            <col width='60%'>
                        </colgroup>
                        <thead>
                            <tr>
                                <th>姓名</th>
                                <th>性别</th>
                                <th>所属区域</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div class="ts-rbi3p-tbody ts-rbi3p-tbody1 niceitem bs">
                    <table class="ts-rbip-table">
                        <colgroup>
                            <col width='20%'>
                            <col width='20%'>
                            <col width='60%'>
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
    </div>
    <script src="${uiDomain}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain}/web-assets/plugins/swiper-5.4.1/package/js/swiper.min.js"></script>
    <script src="${uiDomain}/web-assets/plugins/jquery-nicescroll/jquery.nicescroll-1.js"></script>
    <script src="${uiDomain}/js/paging/pagingV2.js"  charset="utf-8"  type="text/javascript"></script>
    <#include "/component/ComboBox.ftl">
    <script>
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
             getDataList(1);
         }

         function getDataList(page){
             var keyWord = $("#keywords").val();
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
                     keyWord:keyWord,
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
                 var sex = '';
                 if(obj.IDENTITY_CARD != null && obj.IDENTITY_CARD != '' && obj.IDENTITY_CARD != 'undefined'){
                    if(obj.IDENTITY_CARD.length == 18){
                        (parseInt(userCard.substr(16,1)) % 2 == 1)?"男性":"女性"
                    }
                    if(obj.IDENTITY_CARD.length == 15){
                        (parseInt(userCard.substr(14,1)) % 2 == 1)?"男性":"女性"
                    }
                 }
                 htmlStr += '<tr>';
                 htmlStr += '    <td class="sj-btn">'+obj.PARTY_NAME+'</td>';
                 htmlStr += '    <td>'+sex+'</td>';
                 htmlStr += '    <td>'+obj.ORG_NAME+'</td>';
                 htmlStr += '</tr>';
             }
             $("#listData").html(htmlStr);
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
    </script>
</body>
</html>