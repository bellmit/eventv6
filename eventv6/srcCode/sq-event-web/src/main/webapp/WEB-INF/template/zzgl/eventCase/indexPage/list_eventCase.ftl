<#assign rootPath = rc.getContextPath() >
<div class="swiper-slide zfjl_swiper-slide">
    <h5 class="cont_header swiper-no-swiping">执法记录</h5>
    <div class="cont_body swiper-no-swiping">




        <div class="cont_tab_zt">
            <div class="cont_tab_xz">
                <a class="on_orange" href="javascript:void(0);" onclick="iconSelect(this)"><i class="cont_tab_icon1"></i>所有</a>
                <a href="javascript:void(0);" onclick="iconSelect(this,1)"><i class="cont_tab_icon2"></i>紧急</a>
                <a href="javascript:void(0);" onclick="iconSelect(this,2)"><i class="cont_tab_icon3"></i>超时</a>
            </div>
            <ul class="cont_evbar_bans">
                <li class="bgcl1">当日待办<span id="todoNum">0</span>件</li>
                <li class="bgcl2">当月结案<span id="doneNum">0</span>件</li>
                <li class="bgcl3">超时未办结<span id="handleNum">0</span>件</li>
            </ul>
        </div>





        <#--<div class="cont_evbar">-->
            <#--<h5>查询</h5>-->
            <#--<ul class="cont_evbar_btn" style="float:left;margin-left:5px;">-->
                <#--<li><a href="javascript:iconSelect();">所有</a></li>-->
                <#--<li><a href="javascript:iconSelect(1);">紧急</a></li>-->
                <#--<li><a href="javascript:iconSelect(2);">超时</a></li>-->
            <#--</ul>-->
            <#--<ul class="cont_evbar_btn">-->
                <#--<li><a href="javascript:zfjl_searchData();" class="bgcl1">查&nbsp;&nbsp;&nbsp;询</a></li>-->
                <#--<li><a href="javascript:zfjl_resetCondition();" class="bgcl2">重&nbsp;&nbsp;&nbsp;置</a></li>-->
            <#--</ul>-->
        <#--</div>-->
        <ul class="cont_input">
            <li>
                <p>关键字</p>
                <div class="cont_input1">
                    <input type="hidden" id="zfjl_regionCode" value="${gridCode}" />
                    <input type="hidden" id="zfjl_page" value="1" />
                    <input type="hidden" id="zfjl_endPage" value="1" />
                    <input type="hidden" id="zfjl_rows"  value="10" />
                    <input type="hidden" id="handleDateFlag"  value="" />
                    <input type="hidden" id="urgencyDegree"  value="" />
                    <input type="text" id="zfjl_search_registryName"  placeholder="请输入查询内容" />
                    <div class="cont_cx_btm" onclick="zfjl_searchData();"><i class="cont_ico_sch"></i></div>

                </div>
            </li>
            <#--<li>-->
                <#--<p>当日待办 <i id="todoNum">0</i> 件</p>-->
                <#--<p>当月结案 <i id="doneNum">0</i> 件</p>-->
                <#--<p>超时未办结 <i id="handleNum">0</i> 件</p>-->
            <#--</li>-->
        </ul>
        <table id="zfjl_table" class="cont_table">
            <thead class="fp-tr">
            <tr><th></th><th>案件标题</th><th>案发时间</th><th>案件分类</th><th>所属网格</th><th>当前状态</th><th>采集时间</th></tr>
            </thead>
            <tbody class="zfjl">

            </tbody>
        </table>
    </div>
    <div class="cont_page">
        <div class="cont_page_l">
            <div class="cont_page_count">
                <p id="zfjl_rows_p" >10</p>
                <img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_page_pic1.png" />
                <ul class="cont_page_count_lists">
                    <li onclick="zfjl_changeRows(10)" >10</li>
                    <li onclick="zfjl_changeRows(20)" >20</li>
                </ul>
            </div>
            <span class="cont_page_line"></span>
            <div class="cont_page_btn">
                <a href="javascript:zfjl_goPage('first');" title="首页"  ><img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_page_pic2.png"/></a>
                <a href="javascript:zfjl_goPage('previous');" title="上一页"  ><img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_page_pic3.png"/></a>
                <div class="cont_page_input">第<input type="text" id="zfjl_pageCN" value="1"
                                                     onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                                     onchange="zfjl_goPage('go');" />页</div>
                <a href="javascript:zfjl_goPage('next');" title="下一页"  ><img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_page_pic4.png"/></a>
                <a href="javascript:zfjl_goPage('last');" title="尾页"  ><img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_page_pic5.png"/></a>
            </div>
        </div>
        <div class="cont_page_r"><p id="zfjl_desc"></p></div>	</div>
</div>

<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>

<script>
    function open_warp_zfjl(id) {
        zfjl_detailData(id);
        $('.warp_layer_zfjl').css('visibility', 'visible');
    }

    $('.cont_layer_close').on('click', function(){
        $('.warp_layer_zfjl,.warp_layer_zfjl,.warp_layer_zhdj').css('visibility', 'hidden');
    });


    $(window).on('load', function(){
        zfjl_searchData();
        todayTodoNum();
        doneNum();
        handleNum();
    });

    //重置
    function zfjl_resetCondition() {
        $("#zfjl_search_registryName").val("");
        zfjl_searchData();
    }


    function zfjl_searchData(){
        $("#zfjl_page").val("1");
        zfjl_loadData();
    }

    function zfjl_changeRows(rows){
        $("#zfjl_rows").val(rows);
        $("#zfjl_rows_p").html(rows);
        $("#zfjl_page").val("1");
        zfjl_loadData();
    }

    function zfjl_goPage(type){
        var is =true;
        if(type=='first'){
            $("#zfjl_page").val("1");
        }
        if(type=='last'){
            var zfjl_endPage = $("#zfjl_endPage").val();
            $("#zfjl_page").val(zfjl_endPage);
        }
        if(type=='previous'){
            var zfjl_page =  $("#zfjl_page").val();
            if(zfjl_page==1){
                alert("已经是第一页");
                is =false;
            }
            zfjl_page = Number(zfjl_page)-Number(1);
            $("#zfjl_page").val(zfjl_page);
        }
        if(type=='next'){
            var zfjl_endPage = $("#zfjl_endPage").val();
            var zfjl_page =  $("#zfjl_page").val();
            if(zfjl_page==zfjl_endPage){
                alert("已经是最后一页");
                is =false;
            }
            zfjl_page = Number(zfjl_page)+Number(1);
            $("#zfjl_page").val(zfjl_page);
        }

        if(type=='go'){
            var zfjl_endPage = $("#zfjl_endPage").val();
            var zfjl_pageCN =  $("#zfjl_pageCN").val();
            if(zfjl_pageCN!=""&&zfjl_pageCN<=zfjl_endPage){
                $("#zfjl_page").val(zfjl_pageCN);
            }else{
                alert("请输入合法的数字");
            }
        }
        if(is)
            zfjl_loadData();

    }

    function zfjl_loadData(){
        var regionCode = $('#zfjl_regionCode').val();
        var page = $('#zfjl_page').val();
        var rows = $('#zfjl_rows').val();
        var registryName = $('#zfjl_search_registryName').val();
        var handleDateFlag = $('#handleDateFlag').val();
        var urgencyDegree = $('#urgencyDegree').val();
        $.ajax({
            type:"post",
            dataType:"json",
            url:"${rc.getContextPath()}/zhsq/eventCase/listData.json",
            data:{
                "regionCode" : regionCode,
                "keyWord" : registryName,
                "handleDateFlag" : handleDateFlag,
                "urgencyDegree" : urgencyDegree,
                "listType" : '5',
                "page" : page,
                "rows" : rows
            },
            success:function(data) {
                var totalPage =   Math.ceil(data.total/rows) ;
                $("#zfjl_endPage").val(totalPage);
                $("#zfjl_page").val(page);
                $("#zfjl_pageCN").val(page);

                $('.zfjl_table_tr').remove();
                $.each( data.rows, function(index, content)
                {
                    var trHtml ="";
                    var caseName = content.caseName ;
                    if(caseName == null){
                        caseName ='';
                    }
                    var happenTimeStr = content.happenTimeStr ;
                    if(happenTimeStr == null){
                        happenTimeStr ='';
                    }
                    var eventClass = content.eventClass ;
                    if(eventClass == null){
                        eventClass ='';
                    }
                    var gridPath = content.gridPath ;
                    if(gridPath == null){
                        gridPath ='';
                    }
                    var statusName = content.statusName ;
                    if(statusName == null){
                        statusName ='';
                    }
                    var createTimeStr = content.createTimeStr ;
                    if(createTimeStr == null){
                        createTimeStr ='';
                    }

//			    if(solveProblem.length > 6){
//			    	solveProblem = solveProblem.substring(0,6)+'...';
//			    }

                    trHtml +='<tr class="zfjl_table_tr"  onclick="open_warp_zfjl('+content.caseId+'); " >'+
                            '<td align="center">'+Number(Number((page-1)*rows)+Number(index)+Number(1))+'</td>'+
                            '<td  >'+caseName+'</td>'+
                            '<td  >'+happenTimeStr+'</td>'+
                            '<td   >'+eventClass+'</td>'+
                            '<td   >'+gridPath+'</td>'+
                            '<td   >'+statusName+'</td>'+
                            '<td   >'+createTimeStr+'</td>'+
                            '</tr>';
                    $(trHtml).appendTo($("#zfjl_table"));
                });
                var last = data.total ;
                if(data.total >Number((page)*rows)){
                    last = Number((page)*rows) ;
                }


                $("#zfjl_desc").html ('显示 '+Number(Number((page-1)*rows)+Number(1))+'到 '+last+'， 共有 <span>'+data.total+'</span> 条记录');
                var  Jcon = $('.zfjl_swiper-slide').find('.mCSB_container').html();
                $('.zfjl_swiper-slide').find('.cont_body').html(Jcon).mCustomScrollbar();

                $('#zfjl_search_registryName').val(registryName);
//                $('#zfjl_search_visitUserName').val(visitUserName);
            },
            error:function() {
                alert("网络异常！");
            }
        });
    }

    function todayTodoNum(){
        var regionCode = $('#zfjl_regionCode').val();
        $.ajax({
            type:"post",
            dataType:"json",
            url:"${rc.getContextPath()}/zhsq/eventCase/caseCount.json",
            data:{
                "regionCode" : regionCode,
                "status" : "00,01,02",
                "createTimeStart" : "${createTimeStart!}",
                "createTimeEnd" : "${createTimeEnd!}",
                "listType" : '5',
                "page" : 1,
                "rows" : 10000
            },
            success:function(data) {
                $("#todoNum").html(data.total);
            },
            error:function() {
                alert("网络异常！");
            }
        });
    }

    function handleNum(){
        var regionCode = $('#zfjl_regionCode').val();
        $.ajax({
            type:"post",
            dataType:"json",
            url:"${rc.getContextPath()}/zhsq/eventCase/caseCount.json",
            data:{
                "regionCode" : regionCode,
                "status" : "00,01,02",
                "handleDateFlag" : "03",
                "listType" : '5',
                "page" : 1,
                "rows" : 10000
            },
            success:function(data) {
                $("#handleNum").html(data);
            },
            error:function() {
                alert("网络异常！");
            }
        });
    }

    function doneNum(){
        var regionCode = $('#zfjl_regionCode').val();
        $.ajax({
            type:"post",
            dataType:"json",
            url:"${rc.getContextPath()}/zhsq/eventCase/caseCount.json",
            data:{
                "regionCode" : regionCode,
                "status" : "03,04",
                "endTimeStart" : "${endTimeStart!}",
                "endTimeEnd" : "${endTimeEnd!}",
                "listType" : '5',
                "page" : 1,
                "rows" : 10000
            },
            success:function(data) {
                $("#doneNum").html(data);
            },
            error:function() {
                alert("网络异常！");
            }
        });
    }

    function zfjl_detailData(id){
        $.ajax({
            type:"post",
            dataType:"json",
            url:"${rc.getContextPath()}/zhsq/eventCase/case/detailData.json?caseId="+id,
            success:function(data) {
                $('.layer_aj_top_l h4').html(data.eventCase.eventClass);
                $('.layer_aj_top_l p').html("于<span>"+data.eventCase.happenTimeStr+"</span>在<span>"+data.eventCase.occurred+" 发生<span>"+data.eventCase.caseName+"</span>。");

                $('#sourceName').html(data.eventCase.sourceName);
                $('#collectWayName').html(data.eventCase.collectWayName);
                $('#influenceDegreeName').html(data.eventCase.influenceDegreeName);
                $('#contactUser').html(data.eventCase.contactUser);
                $('#statusName').html(data.eventCase.statusName);
                $('#code').html(data.eventCase.code);
                if(data.eventCase.involvedPersonName != null){
                    $('#involvedPersonName').html(data.eventCase.involvedPersonName);
                }
                $('#gridPath').html(data.eventCase.gridPath);
                var curTaskName = "";
                if(data.curTaskName != undefined){
                    curTaskName = data.curTaskName;
                    $('.layer_aj_hj p').html("当前环节： <span>"+curTaskName+"</span>");
                    $('.layer_aj_hj h5').html(data.taskPersonStr);
                }else{
                    $('.layer_aj_hj').hide();
                }

                $('.layer_aj_bt_items').html('');
                for(var i=0;i<data.taskList.length;i++){
                    if(data.taskList[i].IS_CURRENT_TASK!= null){
                        var taskul = '<li><h5 class="aj_items_h aj_items_h_yellow">'+data.taskList[i].TASK_NAME+'</h5><div class="aj_ks aj_ks_yellow"><div class="aj_ks1"></div><div class="aj_ks2"></div><div class="aj_ks3"></div></div><p class="aj_items_t aj_items_t_yellow">'+data.taskList[i].HANDLE_PERSON+'</p></li>';
                        $('.layer_aj_bt_items').append(taskul);
                    }else{
                        var remark = '';
                        if(data.taskList[i].REMARKS!=null){
                            remark = '<p class="aj_items_t aj_items_t1 aj_items_t_blue">'+data.taskList[i].REMARKS+'</p>';
                        }
                        var taskul = '<li><h5 class="aj_items_h aj_items_h_green">'+data.taskList[i].TASK_NAME+'</h5><div class="aj_ks aj_ks_green"><div class="aj_ks1"></div><div class="aj_ks2"></div><div class="aj_ks3"></div></div><p class="aj_items_t aj_items_t_green">'+data.taskList[i].ORG_NAME+'('+data.taskList[i].TRANSACTOR_NAME+')<span> 耗时 </span>'+data.taskList[i].INTER_TIME+'<br /><span>办结时间</span>：'+data.taskList[i].END_TIME+'</p></li>';
                        $('.layer_aj_bt_items').append(taskul);
                    }
                }
                $('.layer_aj_bt_s').css("height", (data.taskList.length * 100 + 100)+'px');
                $('.layer_aj_bt_line').css("height", (data.taskList.length * 100 + 100)+'px');

                getImages(data.eventCase.caseId, 'EVENT_CASE');

            },
            error:function() {
                alert("网络异常！");
            }
        });
    }

    //初始化图片轮播
    function initNbspSlider(){
        var slider = $("#slider");

        if(slider.length > 0) {
            slider.nbspSlider({
                widths:         "310px",        // 幻灯片宽度
                heights:        "310px",
                effect:	         "vertical",
                numBtnSty:       "square",
                speeds:          300,
                autoplay:       1,
                delays:         4000,
                preNexBtnShow:   0,
                altOpa:         0.3,            // ALT区块透明度
                altBgColor:     '#ccc',         // ALT区块背景颜色
                altHeight:      '20px',         // ALT区块高度
                altShow:         1,             // ALT区块是否显示(1为是0为否)
                altFontColor:    '#000',        // ALT区块内的字体颜色
                prevId: 		'prevBtn',      // 上一张幻灯片按钮ID
                nextId: 		'nextBtn'		// 下一张幻灯片按钮I
            });
        }
    }

    var downPath = "${downPath}";
    function getImages(bizId, attachmentType){
        $("#slider").html('<ul></ul>');
        var url = '${rc.getContextPath()}/zhsq/att/getList.jhtml';
        var data = {'bizId':bizId,'attachmentType':attachmentType};
        $.post(url,data,function(result){
            var picFileTotal = 0;

            if (result.length>0) {
                if(typeof(imgsCallBack)=='function'){
                    picFileTotal = imgsCallBack(result, "EVENT_CASE");
                }
            }

            if(picFileTotal > 0) {//有图片、音频文件时，才进行控件初始化
                initNbspSlider();
            } else {
                $("#slider ul").append('<li style="padding-left: 17%;padding-top: 11%;"><span class="noimg" style="display: block;" ></span></li>');
                $("#slider").css({"width": "310px", "height": "310px"});
            }
        },"json");
    }

    var titleArray = new Array();
    var allPicArray = new Array();
    var firstTitleArray = new Array();
    var firstPicArray = new Array();
    var secondTitleArray = new Array();
    var secondPicArray = new Array();
    var lastTitleArray = new Array();
    var lastPicArray = new Array();
    function imgsCallBack(result, eventSeqNameObj) {
        var picFileTotal = 0;

        if(result.length > 0) {
            var firstNum = 0, secondNum = 0, lastNum = 0,
                    sliderImgDiv = $("#slider ul"),
                    suffixStartIndex = -1,
                    imgSuffix = '',
                    imgStr = "png,jpg,gif,jpeg",
                    audioStr = "amr,mp3",
                    videoStr = "mp4",
                    fileShowSrc = "",
                    width = 0,
                    height = 0,
                    imageLi = '';
            imgSrc = '',
                    fileTitleName = "",
                    eventSeq = '';

            for(var i= 0, len = result.length; i < len; i++) {
                imageLi = '';
                fileTitleName = "";
                width = result[i].imgWidth;
                height = result[i].imgHeight;
                imgSrc = downPath + result[i].filePath,
                        eventSeq = result[i].eventSeq;
                suffixStartIndex = imgSrc.lastIndexOf('.');

                if(suffixStartIndex >= 0) {
                    imgSuffix = imgSrc.substr(suffixStartIndex + 1).toLowerCase();
                }

                if(eventSeqNameObj && typeof eventSeqNameObj === 'object') {
                    fileTitleName = eventSeqNameObj[eventSeq];
                }

                if(!fileTitleName) {
                    switch(eventSeq) {
                        case '1': {

                            fileTitleName = "处理前";
                            break;
                        }
                        case '2': {
                            fileTitleName = "处理中";
                            break;
                        }
                        case '3': {
                            fileTitleName = "处理后";
                            break;
                        }
                    }
                }

                if(audioStr.indexOf(imgSuffix) >= 0 || videoStr.indexOf(imgSuffix) >= 0) {
                    fileShowSrc = rootPath + '/scripts/updown/swfupload/images/thumbnail/audio.jpg';
                    var downloadUrl = rootPath + '/zhsq/att/toSeeVideo.jhtml?videoType=2&attachmentId=' + result[i].attachmentId,
                            titleName = '音频';

                    if(videoStr.indexOf(imgSuffix) >= 0) {
                        titleName = '视频';
                    }

                    fileTitleName += titleName;

                    imageLi = '<li><a style="cursor:pointer;" title="点击播放'+ titleName +'" target="_blank" href="'+ downloadUrl +'"><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,300,this)" alt="'+ fileTitleName +'" src="'+fileShowSrc+'" /></a></li>';

                } else if(imgStr.indexOf(imgSuffix) >= 0) {//只有图片才展示
                    fileTitleName += '图片';

                    imageLi = '<li><a style="cursor:pointer;" title="点击放大图片" onclick=showMix("playImg",'+i+')><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,300,this)" alt="'+ fileTitleName +'" src="'+imgSrc+'" /></a></li>';
                }

                if(imageLi) {
                    switch(eventSeq) {
                        case '1': {
                            firstTitleArray.push(fileTitleName);
                            firstPicArray.push(imgSrc);
                            firstNum ++;
                            break;
                        }
                        case '2': {
                            secondTitleArray.push(fileTitleName);
                            secondPicArray.push(imgSrc);
                            secondNum ++;
                            break;
                        }
                        case '3': {
                            lastTitleArray.push(fileTitleName);
                            lastPicArray.push(imgSrc);
                            lastNum ++;
                            break;
                        }
                    }

                    titleArray.push(fileTitleName);
                    allPicArray.push(imgSrc);
                    sliderImgDiv.append(imageLi);
                }
            }

            picFileTotal = firstNum + secondNum + lastNum;

            if(picFileTotal > 0) {
                ImageViewApi.initImageView("playImg",allPicArray,false,true,titleArray);
                if(firstNum > 0){
                    $("#firstImgNum").html(firstNum);
                    ImageViewApi.initImageView("firstImgCode11",firstPicArray);
                    $('#firstImgCode1').click(function(){
                        var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=firstImgCode1";
                        var name = "图片查看";
                        openPostWindow(url, firstPicArray, firstTitleArray);
                    });
                }else{
                    $("#firstImgCode1").attr("style", "display:none");
                    $("#firstImgCodeNone").removeAttr("style");
                }
                if(secondNum > 0){
                    $("#secondImgNum").html(secondNum);
                    ImageViewApi.initImageView("secondImgCode11",secondPicArray);
                    $('#secondImgCode1').click(function(){
                        var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=secondImgCode1";
                        var name = "图片查看";
                        openPostWindow(url, secondPicArray, secondTitleArray);
                    });
                }else{
                    $("#secondImgCode1").attr("style", "display:none");
                    $("#secondImgCodeNone").removeAttr("style");
                }
                if(lastNum > 0){
                    $("#lastImgNum").html(lastNum);
                    ImageViewApi.initImageView("lastImgCode11",lastPicArray);
                    $('#lastImgCode1').click(function(){
                        var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=lastImgCode1";
                        var name = "图片查看";
                        openPostWindow(url, lastPicArray, lastTitleArray);
                    });
                }else{
                    $("#lastImgCode1").attr("style", "display:none");
                    $("#lastImgCodeNone").removeAttr("style");
                }
            }

        }

        return picFileTotal;
    }

    function AutoResizeImage(maxWidth, maxHeight, objImg) {
        var img = new Image();
        img.src = objImg.src;
        var hRatio;
        var wRatio;
        var Ratio = 1;
        var w = img.width;
        var h = img.height;
        wRatio = maxWidth / w;
        hRatio = maxHeight / h;
        if (maxWidth == 0 && maxHeight == 0) {
            Ratio = 1;
        } else if (maxWidth == 0) {//
            if (hRatio < 1)
                Ratio = hRatio;
        } else if (maxHeight == 0) {
            if (wRatio < 1)
                Ratio = wRatio;
        } else if (wRatio < 1 || hRatio < 1) {
            Ratio = (wRatio <= hRatio ? wRatio : hRatio);
        }
        if (Ratio < 1) {
            w = w * Ratio;
            h = h * Ratio;
        }
        objImg.height = h;
        objImg.width = w;
    }

    var rootPath = "${rootPath!''}";

    var contextPath = "${rc.getContextPath()}";
    function showMix(fieldId, index) {//幻灯片点击事件
        ffcs_viewImg_win(fieldId, index);
    }


    function iconSelect(obj,iconType) {//快速查询相应事件
        $(".cont_tab_xz > a").removeClass("on_orange");
        $(obj).addClass("on_orange");

//        $("#eventCaseQueryForm .iconParam").each(function() {
//            $(this).val("");
//        });

        $("#handleDateFlag").val('');
        $("#urgencyDegree").val('');
        switch(iconType) {
            case 1: {
                $("#urgencyDegree").val("02,03,04");
                break;
            }
            case 2: {
                $("#handleDateFlag").val('03');
                break;
            }
        }
        zfjl_searchData();
    }
</script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/imageview/anole.imageview.api.js"></script>