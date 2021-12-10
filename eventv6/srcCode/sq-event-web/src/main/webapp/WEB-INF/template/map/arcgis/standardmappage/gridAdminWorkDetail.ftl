
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- External CSS -->
    <title></title>
    <!--引入 重置默认样式 statics/zhxc -->
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/popup-list.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/css/swiper.min.css"/>
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/basic/detail.css">
    <style>
        .mon-list{
            overflow: hidden;
        }
    </style>
    <!-- JavaScript -->
</head>

<body>
<div class="container">
    <div class="con-warp">
        <div class="top-tabbox-l clearfix">
            <div class="tabbox-l clearfix ml15">
                <a id="eventL" class="li-tab on-active">事件采集</a>
                <a id="visitL" class="li-tab">走访情况</a>
                <#if !region?? && region!='320925'>
                <a id="performanceL" class="li-tab">绩效考核情况</a>
                </#if>
            </div>
        </div>
        <div class="mainbox-d" style="height: 500px">
            <div class="mon-list">
                <iframe id="curList" src="${rc.getContextPath()}/zhsq/szzg/eventController/toEventList.jhtml?eventType=my&model=l&initiatorId=${gridAdminUserId}&createTimeStart=${date}&createTimeEnd=${date}&initiatorOrgId=${orgId!''}"
						scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>
            </div>
        </div>
    </div>
    <!--详情-->
    <div class="popup-detail-box">
        <div class="top-tabbox-l">
            <a class="btn-back-t ml20">返回列表</a>
        </div>
        <div class="mainbox-d pt20">
        </div>
    </div>
</div>
<script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${uiDomain!''}/web-assets/plugins/divscroll.js" type="text/javascript" charset="utf-8"></script>
<script src="${uiDomain!''}/web-assets/plugins/swiper-4.1.6/dist/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
</body>
<script>
    $(function () {
        //固定滚动条
        $('body, .container').perfectScrollbar();

        //grid-cs-tab
        $('.tabbox-l').on('click', 'a', function(){
            $(this).addClass('on-active').siblings().removeClass('on-active');
        });
        //
        $('.mon-list').on('click', function(){
            $('.con-warp').hide().siblings('.popup-detail-box').show();
        });
        $('.btn-back-t').on('click', function(){
            $('.popup-detail-box').hide().siblings('.con-warp').show();
        });
        $("#eventL").click(function () {
            $("#curList").attr("src", "${rc.getContextPath()}/zhsq/szzg/eventController/toEventList.jhtml?eventType=my&model=l&initiatorId=${gridAdminUserId}&createTimeStart=${date}&createTimeEnd=${date}&initiatorOrgId=${orgId!''}");
        });
        $("#visitL").click(function () {
            $("#curList").attr("src", "${RS_URL}/visitRecord/listVisitRecord.jhtml?recordVisitId=${gridAdminId}");
        });
        <#if !region?? && region!='320925'>
        $("#performanceL").click(function () {
            $("#curList").attr("src", "${SQ_ZZGRID_URL}/zzgl/appraisal/statDayWorkDf/yanDu/gridIndex.jhtml?infoOrgCode=${orgCode}");
        });
        </#if>
        //
        // var $winH, $topTabbox1;
        // $(window).on('load resize', function () {
        //     $winH = $(window).outerHeight(true);
        //     $topTabbox1 = $('.top-tabbox-l').outerHeight(true);
        //     $('.mainbox-d').height($winH - $topTabbox1);
        //
        // });

        //				切换事件图片
        // var swiI = 0, swiC = ['处理前.jpg', '处理中1.jpg', '处理中2.jpg', '处理中3.jpg', '处理后.jpg'];
        // $('.swiper-button-next, .swiper-button-prev').on('click', function(){
        //     swiI = swiper.activeIndex;
        //     if($(this).hasClass('swiper-button-next')){
        //         swiI +=1;
        //     }else{
        //         swiI -=1;
        //     }
        //     $('.ann-pic-name').text(swiC[swiI])
        // });
    })
</script>
</html>