<!DOCTYPE html>
<html>
<head>
    <title>平安罗坊</title>
    <meta http-equiv="Access-Control-Allow-Origin" content="*" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/luofang/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/jquery.mCustomScrollbar.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/luofang_page_css/css/content.css"/>

    <script src="${uiDomain!''}/css/luofang_page_css/js/jquery.1.12.4.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/css/luofang_page_css/js/swiper.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${uiDomain!''}/css/luofang_page_css/js/jquery.mCustomScrollbar.concat.min.js" type="text/javascript" charset="utf-8"></script>
    <style>


    </style>
</head>
<body>
<form id="eventCaseForm" action="" method="post" enctype="multipart/form-data">
    <input type="hidden" id="caseId" name="caseId" value="<#if eventCase.caseId??>${eventCase.caseId?c}</#if>" />


</form>
<div class="warp_layer_zfjl  warp_layer" style="visibility:visible;background-color:inherit;">
	<div class="warp_layer_c" style="height:auto;background-color:rgba(0, 0, 0, .5) ;">
		<div class="cont_evbar cont_evbar_l">
			<h5>执法记录信息</h5>
			<#--<a href="#" class="cont_layer_close" title="关闭"><img src="${uiDomain!''}/css/luofang_page_css/images/icon_cont_layer_close.png"/></a>-->
		</div>

        <div class="cont_layer_aj_wrap" style="overflow:hidden;max-height:auto;">
            <div class="cont_layer_aj_top">
                <div class="layer_aj_topc">
                    <div class="layer_aj_top_l">
                        <h4><#if eventCase.eventClass??>【${eventCase.eventClass}】</#if></h4>
                        <p>于<span>${eventCase.happenTimeStr!}</span>在<span>${eventCase.occurred!}</span><#--<img src="style/images/icon_cont_layer_add.png" />-->发生<span>${eventCase.content!}</span>。</p>
                        <ul class="cont_layer_co cont_layer_coaj">
                            <li>信息来源： <span id="sourceName">${eventCase.sourceName!}</span></li>
                            <li>采集渠道： <span id="collectWayName">${eventCase.collectWayName!}</span></li>
                            <li>影响范围： <span id="influenceDegreeName">${eventCase.influenceDegreeName!}</span></li>
                            <li>联系人员： <span id="contactUser">${eventCase.contactUser!} <#if eventCase.tel??>(${eventCase.tel})</#if></span></li>
                            <li>当前状态： <span id="statusName">${eventCase.statusName!}</span></li>
                            <li>案件编号： <span id="code">${eventCase.code!}</span></li>
                            <li>涉及人员： <span id="involvedPersonName"><#if eventCase.involvedNumName??>(<b>${eventCase.involvedNumName}</b>)</#if>${eventCase.involvedPersonName!}</span></li>
                            <li>所属网格： <span id="gridPath">${eventCase.gridPath!}</span></li>
                        </ul>
                    </div>
                    <div class="layer_aj_top_r">
                        <ul class="layer_aj_nav">
                            <#--<li><img src="style/images/icon_cont_aj_nav_bg1.png"/>图片</li>-->
                            <#--<li><img src="style/images/icon_cont_aj_nav_bg1.png"/>音频</li>-->
                        </ul>
                        <div id="slider" class="fr" style="width:310px; height:310px;">
                            <ul></ul>
                        </div>
                    </div>
                </div>
                <#if curTaskName??>
                    <div class="layer_aj_hj">
                        <p>当前环节： <span>${curTaskName!}</span></p>
                        <i></i>
                        <h5><#if taskPersonStr??>${taskPersonStr}</#if></h5>
                    </div>
                </#if>
            </div>
            <div class="cont_layer_aj_bt" style="height:300px;">
                <div class="layer_aj_bt_h">
                    <p>处理环节</p>
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
                            <div class="aj_ks aj_ks_green">
                                <div class="aj_ks1"></div>
                                <div class="aj_ks2"></div>
                                <div class="aj_ks3"></div>
                            </div>
                            <p>历史环节</p>
                        </li>
                    </ul>
                </div>
                <div class="layer_aj_bt_b">
                    <p class="layer_aj_bt_b1">办理环节</p>
                    <p class="layer_aj_bt_b2">办理人/办理时间</p>
                    <p class="layer_aj_bt_b3">办理意见</p>
                </div>
                <div class="layer_aj_bt_s">
                    <div class="layer_aj_bt_line"></div>
                    <ul class="layer_aj_bt_items">

                    </ul>
                </div>
            </div>
        </div>
	</div>
</div>
</body>
<script type="text/javascript">

    var instanceId = "<#if instanceId??>${instanceId?c}</#if>";
    var caseId = "<#if eventCase.caseId??>${eventCase.caseId?c}</#if>";

    $(function() {
        getFlowData();
        getImages(caseId, 'EVENT_CASE');
    });


    function getFlowData(){
        $.ajax({
            type: "POST",
            url: '${rc.getContextPath()}/zhsq/workflow/workflowController/flowData.jhtml?instanceId=' + instanceId,
            data: {'caseId': $("#caseId").val()},
            dataType: "json",
            success: function (data) {
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
                $('.cont_layer_aj_bt').css('height',(data.taskList.length*100+200)+'px');
                $('.layer_aj_bt_line').css('height',(data.taskList.length*100+88)+'px');
            },
            error:function(data){
                console.log(data);
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

    var downPath = "${IMG_URL!''}";
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
</script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event.js"></script>
<script type="text/javascript" src="${ANOLE_COMPONENT_URL}/js/components/imageview/anole.imageview.api.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/nbspslider-1.0/js/jquery.nbspSlider.1.0.min.js" ></script>
</html>




