var rTitle;
var optionText;
//二级联动的列表
var that,optionsText;
//轮廓的显示与隐藏
var outlineWidth;
var outlineText;

window.onload = function () {
    //地区树
    $('.left-ztree>.ztree').niceScroll({
        cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", // 游标边框css定义
        cursorborderradius: "4px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });
    $('.ztree li').on('click', function(){
        setTimeout(function(){$(".left-ztree>.ztree").getNiceScroll().resize();}, 280);
    });
    //地区树的显示与隐藏
    $('.address-sel').on('click',function(){
        $('.left-ztree').toggle();
        return false;
    });
    $('.left-ztree').click(function(){
        return false;
    });


    //目录的变化
    // $('.level-content ul li').click(function(){
    //     var i = $(this).index();
    //     $(this).parent().parent().parent().hide();
    //     $(this).parent().parent().parent().siblings('.right-content-second').show();
    //     $('.second').show();
    //     $('.back').addClass('color-blue');
    //     $(".rc-content-box").getNiceScroll().show();
    //     $(".rc-content-box").getNiceScroll().resize();
    //     //详情的滚动条
    //     $('.rc-content-box').niceScroll({
    //         cursorcolor:"rgba(0, 0, 0, 0.3)",
    //         cursoropacitymax:1,
    //         touchbehavior:false,
    //         cursorwidth:"4px",
    //         cursorborder:"0",
    //         cursorborderradius:"4px"
    //     });
    // });

    $('.back').click(backLevel);

    // $('.right-tital-items').click(function(){
    //     $('.right-tital-items').find('.rt-items-icon').removeClass('rt-items-active');
    //     $(this).find('.rt-items-icon').addClass('rt-items-active');
    //     rTitle = $(this).prop('title');
    //     switch (rTitle){
    //         case '人':
    //             $('.right-content').hide();
    //             $('.rc-local-search-people').show();
    //             break;
    //         case '地':
    //             $('.right-content').hide();
    //             $('.rc-local-search-place').show();
    //             break;
    //         case '事':
    //             $('.right-content').hide();
    //             $('.rc-local-search-event').show();
    //             break;
    //         case '物':
    //             $('.right-content').hide();
    //             $('.rc-local-search-things').show();
    //             break;
    //         case '组织':
    //             $('.right-content').hide();
    //             $('.rc-local-search-organization').show();
    //             break;
    //         default:
    //             break;
    //     }
    //     rightContentHide();
    // });
    $('.search-box').click(function(){
        $('.right-content').hide();
        $('.rc-local-search').show();
        rightContentHide();
    });

    //右边隐藏的代码
    $('.right-content-switch').click(function(){
        if($(this).hasClass('right-content-switch2')){
            $('.right-content').animate({'right': '-230px'},function(){
                $('.right-tital').removeClass('right-tital-boxshow2');
                $(".rc-content-box").getNiceScroll().hide();
                $(".level-content").getNiceScroll().hide();
                $('.rt-items-icon').removeClass('rt-items-active');
            });
            $(this).removeClass('right-content-switch2').addClass('right-content-switch1');
            $(this).find('img').prop('src',zhXc+'icon-to-right.png');
        }else if($(this).hasClass('right-content-switch1')){
            rightContentHide();
            $(this).removeClass('right-content-switch1').addClass('right-content-switch2');
            $(this).find('img').prop('src',zhXc+'icon-to-left.png');
        }
    })
//清空搜索内容
    $('.close').click(function(){
        $(this).parent().siblings('.right-content-second').find('.rc-content').hide();
        $(this).parent().siblings('.right-content-second').addClass('no-content');
        $(".rc-content-box").getNiceScroll().hide();
    })
//下拉选择框的控制代码
    $('.dd-content').click(function(){
        $('.drop-down').toggle();
        $('.drop-down').css({'top':'36px'});
        $('.drop-down').animate({'top':'26px'});
        return false;
    });

    $('.dd-options').on('click','ul li a', function(){
        optionText = $(this).text();
        $('.dd-content').find('p').text(optionText);
    });

    $('.dd-options').on('mouseover','ul li a',function(){
        $('.dd-options').find('a').removeClass('active');
        $(this).addClass('active');
        $that = $(this);
        $('.dd-secondary-optios').on('mouseover',function(){
            $that.addClass('active');
        })
        $('.dd-secondary-optios-list').on('click','ul li a',function(){
            optionsText = $that.text();
            $('.dd-content').find('p').text(optionsText);
        })
        switch ($(this).text()){
            case '全部':
                $('.drop-down').css({'width': '60px'});
                $('.dd-secondary-optios').hide();
                break;
            case '人':
                secondaryLinkage($('.dd-secondary-optios-people'),'dds-optios-people');
                break;
            case '地':
                secondaryLinkage($('.dd-secondary-optios-place'),'dds-optios-place');
                break;
            case '事':
                secondaryLinkage($('.dd-secondary-optios-event'),'dds-optios-event');
                break;
            case '物':
                secondaryLinkage($('.dd-secondary-optios-things'),'dds-optios-things');
                break;
            case '组织':
                secondaryLinkage($('.dd-secondary-optios-organization'),'dds-optios-organization');
                break;
        }
    });



    $('.outline').on('click',function(){
        outlineWidth = 200;
        $('.outline-list').toggle();
        $('.outline-list').css({'width': outlineWidth})
        return false;
    });
    // $('.outline-list').on('click','li a',function(){
    //     outlineText = $(this).text();
    //     $(this).parent().parent().siblings('.outline').find('p').text(outlineText);
    // });
}

function backLevel(){
    $('.right-content-first').show();
    $('.right-content-second').hide();
    $('.second').hide();
    $(".rc-content-box").getNiceScroll().hide();
    $('.back').removeClass('color-blue');
}

//右边内容的弹出与隐藏
//右边显示的函数
var zhXc = js_ctx + '/map/base/images/'
function rightContentHide(){
    $('.right-content-switch').removeClass('right-content-switch1').addClass('right-content-switch2');
    $('.right-content-switch').find('img').prop('src',zhXc+'icon-to-left.png');
    $('.right-content').animate({'right': '60px'},function(){
        $('.right-tital').addClass('right-tital-boxshow2');
        $(".rc-content-box").getNiceScroll().show();
        $(".rc-content-box").getNiceScroll().resize();
        //详情的滚动条
        $('.rc-content-box').niceScroll({
            cursorcolor:"rgba(0, 0, 0, 0.3)",
            cursoropacitymax:1,
            touchbehavior:false,
            cursorwidth:"4px",
            cursorborder:"0",
            cursorborderradius:"4px"
        });
        //人员的滚动条
        $(".level-content").getNiceScroll().show();
        $(".level-content").getNiceScroll().resize();
        $('.level-content').niceScroll({
            cursorcolor:"rgba(0, 0, 0, 0.3)",
            cursoropacitymax:1,
            touchbehavior:false,
            cursorwidth:"4px",
            cursorborder:"0",
            cursorborderradius:"4px"
        });
    });
}
//右边显示的代码
var rTitle;

//取消下拉选择框的代码
$(document).click(function(){
    $('.drop-down').hide();
    $(".dd-secondary-optios-list").getNiceScroll().hide();

    $('.left-ztree').hide();
    $('.outline-list').hide();
});

function secondaryLinkage(item,item1){
    $(".dd-secondary-optios-list").hide();
    $(".dd-secondary-optios-list").getNiceScroll().hide();
    $('.drop-down').css({'width': '244px'});
    $('.dd-secondary-optios').show();
    item.show();
    $('.dd-secondary-optios').removeClass('dds-optios-organization dds-optios-things dds-optios-event dds-optios-place dds-optios-people').addClass(item1);
    item.getNiceScroll().show();
    item.getNiceScroll().resize();
    item.niceScroll({
        cursorcolor:"rgba(0, 0, 0, 0.3)",
        cursoropacitymax:1,
        touchbehavior:false,
        cursorwidth:"4px",
        cursorborder:"0",
        cursorborderradius:"4px",
        autohidemode: false
    });
}


//当前选择网格的级别，当前加载网格轮廓的级别
function changeCheckedAndStatus(gridLevel,level) {
    $("#li2").addClass("hide");
    document.getElementById("gridLevelName2").checked = false;
    $("#li3").addClass("hide");
    document.getElementById("gridLevelName3").checked = false;
    $("#li4").addClass("hide");
    document.getElementById("gridLevelName4").checked = false;
    $("#li5").addClass("hide");
    document.getElementById("gridLevelName5").checked = false;
    $("#li6").addClass("hide");
    document.getElementById("gridLevelName6").checked = false;

    $("#BuildOutlineDiv").addClass("hide");
    if (level-1 >= 5) {
        if(document.getElementById("buildName0").checked == true) {
            getArcgisDataOfBuildsByCheck();
        }
    }else {
        document.getElementById("buildName0").checked=false;
    }
    if (typeof MMApi != "undefined") {// 高德地图
        for (var i = gridLevel; i <= 6; i++) {
            $("#li"+i).removeClass("hide");
        }
        $("#li3").addClass("hide");
        if (document.getElementById("gridLevelName"+level) != undefined) {
            document.getElementById("gridLevelName"+level).checked = true;
            var value = document.getElementById("li"+level).innerText;
            $("#level").html(value);
        }
    } else {
        if(document.getElementById("gridLevelName"+gridLevel) != undefined) {
            $("#li"+gridLevel).removeClass("hide");
        }

        if(((typeof LUO_FANG != 'undefined' && LUO_FANG == "true") || (typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' && SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true"))
            && document.getElementById("gridLevelName"+(level-1)) != undefined){
            var operateLevel = level;
            if($("#gridLevel").val() == "6"){
                operateLevel = level;
            }else{
                operateLevel = level -1;
            }
            $("#li"+(operateLevel)).removeClass("hide");
            document.getElementById("gridLevelName"+(operateLevel)).checked = true;
            var value = document.getElementById("li"+(operateLevel)).innerText;
            $("#level").html(value);
            if(document.getElementById("gridLevelName"+(parseInt(gridLevel)+1)) != undefined) {
                $("#li"+(parseInt(gridLevel)+1)).removeClass("hide");
            }
        }else{
            if(document.getElementById("gridLevelName"+level) != undefined) {
                $("#li"+level).removeClass("hide");
                document.getElementById("gridLevelName"+level).checked = true;
                var value = document.getElementById("li"+level).innerText;
                $("#level").html(value);
            }
            if(document.getElementById("gridLevelName"+(parseInt(gridLevel)+2)) != undefined) {
                $("#li"+(parseInt(gridLevel)+2)).removeClass("hide");
            }
        }
    }
    if (parseInt(gridLevel) > 4) {
        $("#BuildOutlineDiv").removeClass("hide");
    }
}

//选择树的回调函数
function gridTreeClickCallback(gridId,gridName,orgId,orgCode,gridInitPhoto,gridLevel,gridCode){
    $(".SelectTree").css("display","none");
    if(gridCode != $("input[name='gridCode']").val() || (typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' && SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && gridIds.split(",").length>2)) {
        $("#changeGridName").text(gridName);
        $("input[name='gridId']").val(gridId);
        $("input[name='gridCode']").val(gridCode);
        $("input[name='gridName']").val(gridName);
        $("input[name='gridLevel']").val(gridLevel);
        $("input[name='orgCode']").val(orgCode);
        if (typeof MMApi != "undefined") {// 高德地图
            var gridLevel = parseInt($("#gridLevel").val());
            var level = gridLevel + 1;
            if (gridLevel == 2) {
                level = gridLevel + 2;
            } else if (gridLevel == 6) {
                level = gridLevel;
            }
            changeCheckedAndStatus(gridLevel, level);
            MMApi.setCenter(gridId, gridCode);
            $("#buildName0").attr("checked", false);
            if (currentLayerListFunctionStr != undefined && currentLayerListFunctionStr != '') {
                eval(currentLayerListFunctionStr);
            }
        } else {
            var level = (parseInt(gridLevel) < 6) ? parseInt(gridLevel)+1 : parseInt(gridLevel);
            //document.getElementById("gridLevelName"+level).checked = true;
            changeCheckedAndStatus(gridLevel,level);

            locateCenterAndLevel(gridId,currentArcgisConfigInfo.mapType);
            clearMyLayer();
            if((typeof LUO_FANG != 'undefined' && LUO_FANG == "true") || (typeof SHOW_CURRENT_GRID_LEVEL_OUTLINE != 'undefined' && SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && level != 2)){
                level = level - 1;
            }
            getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level);

            if(currentLayerListFunctionStr != undefined && currentLayerListFunctionStr!=''){
                eval(currentLayerListFunctionStr);
            }
        }
        if (typeof clearMyLayerA == "function") {
            clearMyLayerA();
        }
    }
}


function getArcgisDataOfGridsByLevel(level) {
    if(document.getElementById("gridLevelName"+level).checked == true) {
        var glns = $("input[name='gridLevelName']");
        for(var i=0; i<glns.length; i++) {
            if(glns[i].value!=level){
                glns[i].checked = false;
            }
        }

        var value = document.getElementById("li"+level).innerText;
        $("#level").html(value);

        getArcgisDataOfGrids($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType ,level)
    }else {
        if(ARCGIS_DOCK_MODE == "1") {
            featureHide("gridLayer"+gridLyerNum);
        }else {
            $("#map"+currentN).ffcsMap.clear({layerName : "gridLayer"});
        }

    }
}

//获取楼宇定位点
function getArcgisDataOfBuildsByCheck() {
    if(document.getElementById("buildName0").checked == true) {
        $("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
        $("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
        getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
    }else {
        if(ARCGIS_DOCK_MODE == "1") {
            featureHide("buildLayer"+gridLyerNum);
        }else {
            $("#map"+currentN).ffcsMap.clear({layerName : "buildLayerPoint"});
            $("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
        }

    }

}

//获取图层信息
function getMenuInfo(){
    var orgCode = $("#orgCode").val();
    var homePageType = $("#homePageType").val();

    $.ajax({
        url: js_ctx + '/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
        type: 'POST',
        timeout: 300000,
        data: { orgCode:orgCode,homePageType:homePageType,gdcId:4,isRootSearch:1},
        dataType:"json",
        async: true,
        error: function(data){
            $.messager.alert('友情提示','图层配置信息获取出现异常!','warning');
        },
        success: function(data){
            gisDataCfg=eval(data.gisDataCfg);
            if(gisDataCfg != null){
                var htmlStr = "";
                var callBack = "";
                var menuList = gisDataCfg.childrenList;
                if (menuList.length > 0) {
                    var menuListULHTML = "";
                    var ddOptionsHTML = "";
                    for(var i=0; i<menuList.length; i++){
                        var menuListLiHTML = "<li class='flex flex-jc'><a href='#' class='right-tital-items flex flex-ac flex-clm mt20' onclick=\""+menuList[i].callBack+"\" title='"+menuList[i].menuName+"'>";
                        // var menuListLiHTML = "<li class='flex flex-jc'><a href='#' class='flex flex-ac flex-clm mt20' title='"+menuList[i].menuName+"'>";
                        menuListLiHTML += "<div class='rt-items-icon'>";
                        menuListLiHTML += "<img src='"+ uiDomain + menuList[i].largeIco +"'/>";
                        menuListLiHTML += "</div></a></li>";
                        menuListULHTML = menuListULHTML + menuListLiHTML;

                        var ddOptionHTML = "<li><a href=\"javascript:void(0);\">"+menuList[i].menuName+"</a></li>"
                        ddOptionsHTML = ddOptionsHTML + ddOptionHTML;
                    }
                    var menuListUL = document.getElementById("menuList");
                    menuListUL.innerHTML = menuListULHTML;

                    var ddOptions = document.getElementById("ddOptions");
                    ddOptions.innerHTML = ddOptions.innerHTML + ddOptionsHTML;
                } else {
                    $.messager.alert('友情提示','地图首页图层内容未配置，请联系管理员!','info');
                }
            }
        }
    });
}


var currentClassificationFuncStr = "";//用于记录当前
//右图层菜单点击展开面板
function classificationClick(elementsCollectionStr){
    var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var menuName = eclist["menuName"];
    var menuCode = eclist["menuCode"];
    var callBack = eclist["callBack"];
    if (callBack == "classificationClick") {
        getChildListOfLayer(elementsCollectionStr);
    }

    nowAlphaBackShow = "firstall";
    currentLayerListFunctionStr="";
    currentClassificationFuncStr=callBack+"(\""+elementsCollectionStr+"\")";


    //显示数据面板
    $('.right-tital-items').find('.rt-items-icon').removeClass('rt-items-active');
    $(this).find('.rt-items-icon').addClass('rt-items-active');
    rTitle = $(this).prop('title');
    $('.right-content').hide();
    $('.rc-local-search-option').show();
    rightContentHide();
}

//获取子级图层
function getChildListOfLayer(elementsCollectionStr) {
    var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var gdcId = eclist["gdcId"];
    var orgCode = eclist["orgCode"];
    var menuCode = eclist["menuCode"];
    var homePageType = $("#homePageType").val();
    var childOptions =null;
    if (menuCode != '') {
        childOptions = document.getElementById("childOptions");
    }
    $.ajax({
        url: js_ctx+'/zhsq/map/menuconfigure/menuConfig/getGisDataCfgRelationTreeVersionTwo.json?t='+Math.random(),
        type: 'POST',
        timeout: 300000,
        data: { orgCode:orgCode,homePageType:homePageType,gdcId:gdcId,isRootSearch:0},
        dataType:"json",
        async: true,
        error: function(data){
            $.messager.alert('友情提示','专题图层信息获取出现异常!','warning');
        },
        success: function(data){
            gisDataCfg=eval(data.gisDataCfg);
            var childOptionsHtml = "";
            if(gisDataCfg != null){
                var childOptionsList = gisDataCfg.childrenList;
                if(childOptionsList != null && childOptionsList.length>0){
                    for(var j=0;j<childOptionsList.length;j++){
                        var title = childOptionsList[j].menuName;
                        var callBack = childOptionsList[j].callBack;

                        childOptionsHtml+="<li>"
                                        +"<a href='#' class='flex flex-ac flex-jc mt10' onclick=\""+childOptionsList[j].callBack+"\" title=\""+title+"\">"
                                        +"<span>"+childOptionsList[j].menuName+"</span>"
                                        +"</a>"
                                        +"</li>";

                    }
                    if(typeof(childOptions) != 'undefined' && childOptions != null && typeof(childOptions.innerHTML) != 'undefined'){
                        childOptions.innerHTML = childOptionsHtml;
                    }
                }
            }
        }
    });
    //}
}

function showObjectList(elementsCollectionStr) {
    //设置数据面板
    var i = $(this).index();
    $(this).parent().parent().parent().hide();
    $(this).parent().parent().parent().siblings('.right-content-second').show();
    $('.second').show();
    $('.back').addClass('color-blue');
    $(".rc-content-box").getNiceScroll().show();
    $(".rc-content-box").getNiceScroll().resize();
    //详情的滚动条
    $('.rc-content-box').niceScroll({
        cursorcolor:"rgba(0, 0, 0, 0.3)",
        cursoropacitymax:1,
        touchbehavior:false,
        cursorwidth:"4px",
        cursorborder:"0",
        cursorborderradius:"4px"
    });

    var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var menuCode = eclist["menuCode"];
    var menuName = eclist["menuName"];
    var callBack = eclist["callBack"];
    var layerName = eclist["menuLayerName"];

    if("1" == AUTOMATIC_CLEAR_MAP_LAYER) {
        clearMyLayer();
        toHideZhouBianSketch();
    }
    if (typeof clearMyLayerA == "function") {
        clearMyLayerA();
    }
    if(typeof gHeatLayer != 'undefined' && gHeatLayer != null){
        gHeatLayer.clearData();
    }
    clearSpecialLayer(layerName);
    currentListNumStr=""
    $("#get_grid_name_frme").attr("height",document.getElementById('map'+currentN).offsetHeight-62);
    nowAlphaBackShow = "NorList";
    getObjectListUrl(elementsCollectionStr);
    currentLayerListFunctionStr = callBack+"(\""+elementsCollectionStr+"\")";
}

//解析字符串elementsCollectionStr
function analysisOfElementsCollectionList(elementsCollectionStr){
    if(elementsCollectionStr) {
        var ecs = elementsCollectionStr.split(",_,");
        var eclist = {};
        for(var i=0;i<ecs.length;i++){
            var e = ecs[i].split("_,_");
            eclist[e[0]]=e[1];
        }
        return eclist;
    }

    return {};
}


function getObjectListUrl(elementsCollectionStr,gridId) {
    var _mapt = "5";
    if (typeof MMApi != "undefined") {
        _mapt = MMGlobal.MapType;
    } else {
        _mapt = currentArcgisConfigInfo.mapType;
    }
    var eclist = analysisOfElementsCollectionList(elementsCollectionStr);
    var menuCode = eclist["menuCode"];
    var menuListUrl = eclist["menuListUrl"];
    if(menuListUrl.indexOf('http://')<0){
        menuListUrl = js_ctx + menuListUrl;
    }
    if(gridId!=undefined) {
        if(menuListUrl.indexOf("?")<=0){
            menuListUrl += "?t="+Math.random();
        }
        menuListUrl += "&gridId="+gridId+"&noneGetLayer=true";
        var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+
            '<input id="elementsCollectionStr" name="elementsCollectionStr" type="hidden" value="'+elementsCollectionStr+'"/>'+
            '</form>';
        document.getElementById('get_grid_name_frme').contentWindow.document.write(html);
        document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
    }else {
        if(menuListUrl.indexOf("?")<=0){
            menuListUrl += "?t="+Math.random();
        }
        menuListUrl += "&mapt="+_mapt+"&gridId="+$("#gridId").val()+"&gridCode="+$("#gridCode").val()+"&orgCode="+$("#orgCode").val()+"&infoOrgCode="+$("#orgCode").val();

        var html = '<form action="'+menuListUrl+'" method="post" target="_self" id="postData_form">'+
            '<input id="elementsCollectionStr" name="elementsCollectionStr" type="hidden" value="'+elementsCollectionStr+'"/>'+
            '</form>';
        document.getElementById('get_grid_name_frme').contentWindow.document.write(html);
        document.getElementById('get_grid_name_frme').contentWindow.document.getElementById('postData_form').submit();
    }
}