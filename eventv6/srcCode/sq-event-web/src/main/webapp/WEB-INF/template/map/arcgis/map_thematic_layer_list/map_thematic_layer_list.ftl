<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图首页</title>
    <#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
    <#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <link href="${rc.getContextPath()}/js/map/spgis/lib/heatmap/heatmap.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/fbsource/jquery.fancybox.css?v=2.1.5" media="screen" />
    <#include "/component/ImageView.ftl" />
    <link rel="stylesheet" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
    <script src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/zhoubian_index.js"></script>
    <link rel="stylesheet" href="${uiDomain}/css/arcgis_config_index_versionnoe/style.css" />
    <script src="${ANOLE_COMPONENT_URL}/js/components/popWindow/jquery.anole.popWindow.js"></script>
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/migration_map.css">
    <link href="${rc.getContextPath()}/css/kuangxuan.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
        //图片查看器回调
        function ffcs_viewImg(fieldId){
            var sourceId = fieldId + "_Div";
            var imgDiv = $("#"+sourceId+"");
            imgDiv.find('.fancybox-button').eq(0).click();
        }
    </script>
    <style>
        .ztree li{overflow:hidden;}
        .ztree li span{font-size:14px;}
        .mapbox{ cursor: pointer;background:#fff; border:2px solid #2ab7b2; padding:5px; position:relative; min-width:30px; display:block; white-space:nowrap;}
        .mapicon{ display:inline-block;margin-right:3px;}
        .maparrow{ background:url(${uiDomain!''}/images/map_tree_arrow.png) no-repeat; width:9px; height:6px; position:absolute; left:10px; bottom:-8px;}
    </style>
</head>
<body style="width:100%;height:100%;border:none;" >
<div id="firstImgs" style="display: none;"></div>
<div>
    <div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
    </div>
    <div class="page-container" id="map1" style="position: absolute; width:100%; height:100%; z-index: 2;display:none;">
    </div>
    <div class="page-container" id="map2" style="position: absolute; width:100%; height:100%; z-index: 3;display:none;">
    </div>
</div>
<div id="jsSlider"></div>
<div class="MapTools">
    <ul>
        <li class="ClearMap" onclick="clearMyLayerA();"></li>
        <li class="ThreeWei" onclick="threeWeiClick();"></li>
    </ul>
    <div id="mapStyleDiv" class="MapStyle" style="display:none">
        <span class="current">二维图</span>
        <span>三维图</span>
        <span>卫星图</span>
    </div>
</div>
<!-- baseDataTabs end -->

<!-- 专题地图 -->
<form name="gridForm" method="post" action="${rc.getContextPath()}/admin/grid.shtml" target="_self">
    <input type="hidden" name="gridId" id="gridId" value="${gridId?c}" />
    <input type="hidden" name="gridCode" id="gridCode" value="${gridCode}" />
    <input type="hidden" name="orgCode" id="orgCode" value="${orgCode}" />
    <input type="hidden" name="socialOrgCode" id="socialOrgCode" value="${socialOrgCode}" />
    <input type="hidden" name="homePageType" id="homePageType" value="${homePageType}" />
    <input type="hidden" name="gridName" id="gridName" value="${gridName}" />
    <input type="hidden" name="gridLevel" id="gridLevel" value="${gridLevel}" />
    <input type="hidden" name="SQ_ZZGRID_URL" id="SQ_ZZGRID_URL" value="${SQ_ZZGRID_URL}" />
    <input type="hidden" name="POPULATION_URL" id="POPULATION_URL" value="${POPULATION_URL}" />
    <input type="hidden" name="elementsCollectionStr" id="elementsCollectionStr" value="${gisDataCfg.elementsCollectionStr!''}" />

</form>

<!-----------------------------------设置------------------------------------->
<div class="NorMapOpenDiv2 zhoubianWindow hide dest" style="bottom:30px; left:60px;">
    <div class="box" style="width:376px;height:404px;">
        <div class="title"><span class="fr close" onclick="javascirpt:closeZhoubian()"></span>选择您想要查看的内容</div>
        <iframe id="zhoubianConfig" name="zhoubianConfig" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>
    </div>
    <!--<div class="shadow"></div>-->
</div>
<div class="MapBar">
    <div class="con AlphaBack">
        <#include "/map/arcgis/arcgis_base/top.ftl">
        <div class="zhuanti fr"><a href="javascript:void(0);">专题图层</a></div>
    </div>

    <!-----------------------------------人地事物情------------------------------------->
    <div class="ztIcon AlphaBack titlefirstall" style="display:none;">
        <div class="title"><span class="fr" onclick="CloseX()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnId" class="fr" style="display:none;" onclick="ShowSearchBtn()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="menuLayertitle" name="titlePath">专题图层</div></div>
    </div>
    <div class="ztIconZhouBian AlphaBackZhouBian titlezhoubian" style="display:none;">
        <div class="title"><span class="fr" onclick="zhoubianListHide()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/closex.png" /></span><span id="searchBtnIdZhouBian" class="fr" style="" onclick="ShowSearchBtnZhouBian()"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/search.png" /></span><div id="titlePathZhouBian" name="titlePathZhouBian">周边资源</div></div>
    </div>
    <div class="ztIcon AlphaBack dest firstall" style="display:block;">
        <div id="divTreeMenu">
            <ul id="ulFirstall"></ul>
        </div>
        <div class="clear"></div>
    </div>
    <!-----------------------------------人------------------------------------->
    <div class="ztIcon AlphaBack people">
        <ul id="ulPeople"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------地------------------------------------->
    <div class="ztIcon AlphaBack world">
        <ul id="ulWorld"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------事------------------------------------->
    <div class="ztIcon AlphaBack metter">
        <ul id="ulMetter"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------物------------------------------------->
    <div class="ztIcon AlphaBack thing">
        <ul id="ulThing"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------情------------------------------------->
    <div class="ztIcon AlphaBack situation">
        <ul id="ulSituation"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------组织------------------------------------->
    <div class="ztIcon AlphaBack organization">
        <ul  id="ulOrganization"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------精准扶贫------------------------------------->
    <div class="ztIcon AlphaBack precisionPoverty">
        <ul  id="ulPrecisionPoverty"></ul>
        <div class="clear"></div>
    </div>
    <!-----------------------------------通用------------------------------------->
    <div class="ztIcon AlphaBack common">
        <ul  id="ulCommon"></ul>
        <div class="clear"></div>
    </div>

    <div class="NorList AlphaBack">
        <iframe id="get_grid_name_frme" name="get_grid_name_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
    </div>
    <div class="NorListZhouBian AlphaBackZhouBian zhoubianList">
        <iframe id="zhoubian_list_frme" name="zhoubian_list_frme" width="100%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
    </div>
</div>

<div id="dialog" title="三维图展示">
    <iframe id="lc_skylineview_frme" name="lc_skylineview_frme" width="100%" height="100%" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
</div>

<!-- 出租屋色块提示 -->
<div id="rentRoomTip" class="AlphaBack" style="display:none;right:261px;bottom:2px;position: absolute;z-index:10; top: auto; left: auto; padding: 7px; color:#fff; font-size:12px;">
    <div class="legend-text">出租屋色块提示：</div>
    <span class="legend-item" style="background-color:#ED0000;">超出均值</span>
    <span class="legend-item" style="background-color:#FDC100;">均值范围</span>
    <span class="legend-item" style="background-color:#05AF4C;">低于均值</span>
</div>

</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<#include "/component/videoPlayWindow.ftl" />
<#include "/component/customEasyWin.ftl" />
<#include "/component/mmp.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/main_new.css" />
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/event/event_4_parent.js"></script>
<script type="text/javascript">
    var divDrag = {
        zbWinObj: null
    };

    var LUO_FANG = "<#if LUO_FANG??>${LUO_FANG}</#if>";

    var test1="tets";
    $(function(){
        modleopen();
        zhoubianListHide();
        $("#map0").css("height",$(document).height());
        $("#map0").css("width",$(document).width());
        $("#map1").css("height",$(document).height());
        $("#map1").css("width",$(document).width());
        $("#map2").css("height",$(document).height());
        $("#map2").css("width",$(document).width());
        setTimeout(function(){
            var level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
//            document.getElementById("gridLevelName"+level).checked = true;
            if(LUO_FANG == "true" && typeof document.getElementById("gridLevelName"+(level-1)) != 'undefined'
                    && document.getElementById("gridLevelName"+(level-1)) != null){
                document.getElementById("gridLevelName"+(level-1)).checked = true;
            }else{
                document.getElementById("gridLevelName"+level).checked = true;
            }

            eventInit("${rc.getContextPath()}", "${SQ_ZZGRID_URL}");//--初始化工作流！
            getArcgisInfo(function() {
                locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
            });

            changeCheckedAndStatus($("#gridLevel").val(),level);

            modleclose();

            getLayerMenuInfo();

        },100);
        window.onresize=function(){
            $("#map0").css("height",$(document).height());
            $("#map0").css("width",$(document).width());
            $("#map1").css("height",$(document).height());
            $("#map1").css("width",$(document).width());
            $("#map2").css("height",$(document).height());
            $("#map2").css("width",$(document).width());
        }
        divDrag.zbWinObj = $(".zhoubianWindow");
        divDrag.zbWinObj.mousedown(function(e) {
            $(this).css("cursor", "move");
            var offset = $(this).offset();
            var x = e.pageX - offset.left;
            var y = e.pageY - offset.top;
            $(document).bind("mousemove", function(ev) {
                divDrag.zbWinObj.stop();
                var _x = ev.pageX - x;
                var _y = ev.pageY - y;
                divDrag.zbWinObj.css({
                    left : _x,
                    top : _y
                });
            });
        }).mouseup(function() {
            divDrag.zbWinObj.css("cursor", "default");
            $(document).unbind("mousemove");
        });
    });



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
    function getArcgisDataByCurrentSet(){
        var glns = $("input[name='gridLevelName']");
        for(var i=0; i<glns.length; i++) {
            if(glns[i].checked == true){
                var idval = $(glns[i]).attr("id");
                var l = parseInt(idval.substring("gridLevelName".length,idval.length));
                getArcgisDataOfGrids($("#gridId").val(),$("#orgCode").val(),currentArcgisConfigInfo.mapType ,l);
            }
        }

        if(document.getElementById("buildName0").checked == true) {
            getArcgisDataOfBuildsPoints($("#gridId").val(),$("#gridCode").val(),currentArcgisConfigInfo.mapType)
        }
    }

    //获取arcgis地图路径的配置信息
    function getMapArcgisInfo(){
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapArcgisInfo.json?t='+Math.random(),
            type: 'POST',
            timeout: 3000,
            dataType:"json",
            async: false,
            error: function(data){
                $.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
            },
            success: function(data){
                var mapConfigInfo=eval(data.mapConfigInfo);
                arcgisMapConfigInfo.Init(mapConfigInfo);// = new ArcgisMapConfigInfo(mapConfigInfo);

                if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
                    currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
                }else if(arcgisMapConfigInfo.mapStartType == "30") {//表示地图默认显示为2维

                    currentArcgisMapInfo.Init3D(arcgisMapConfigInfo);
                }
                loadArcgisMap("map","jsSlider","vec");
            }
        });
    }

    function threeWeiClick(){
        var mapStyleDiv = document.getElementById("mapStyleDiv");
        if(mapStyleDiv.style.display == 'none') {
            mapStyleDiv.style.display = 'block';
        }else {
            mapStyleDiv.style.display = 'none';
        }
    }
    var currentMapStyleObj;
    //获取arcgis地图路径的配置信息
    function getArcgisInfo(backFn){
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t='+Math.random(),
            type: 'POST',
            timeout: 3000,
            dataType:"json",
            async: false,
            error: function(data){
                $.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
            },
            success: function(data){
                arcgisConfigInfos=eval(data.arcgisConfigInfos);
                var htmlStr = "";

                for(var i=0; i<arcgisConfigInfos.length; i++){
                    if(i==0){
                        htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
                    }else (
                            htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
                    )
                }
                var mapStyleDiv = document.getElementById("mapStyleDiv");
                mapStyleDiv.innerHTML = htmlStr;
                $("#mapStyleDiv").width(60*arcgisConfigInfos.length+8)

                if(htmlStr!=""){
                    currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
                }

                if(arcgisConfigInfos.length > 0) {
                    loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap", backFn);
                }
            }
        });
    }
    var displayStyle = '0'; // 平铺
    function getLayerMenuInfo(){
        var orgCode = $("#orgCode").val();
        var homePageType = $("#homePageType").val();
        var gisDataCfg,menuCode="${menuCode}";
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/menuconfigure/menuConfig/getGisDataCfgByCode.json?t='+Math.random(),
            type: 'POST',
            timeout: 300000,
            data: { orgCode:orgCode,homePageType:homePageType,menuCode:menuCode},
            dataType:"json",
            async: true,
            error: function(data){
                $.messager.alert('友情提示','图层配置信息获取出现异常!','warning');
            },
            success: function(data){
                if(data != null && data.callBack != null){
                    $("#menuLayertitle").html(data.menuName);
                    $("title").html(data.menuName + "地图");
                    eval(data.callBack);
                }
            }
        });
    }

    function clearMyLayerB() {
        clearMyLayer();
    }


    var winType = "";//用于判断是否关闭详细窗口
    var mapObjectName = "";//用于确定刷新的列表
    function flashData(){
        if(winType!="" && winType=='0'){//关闭详细窗口
            closeMaxJqueryWindow();
            winType = "";
        }

        if(mapObjectName == "待办事件"){
            metter();
            showObjectList(mapObjectName);
        }else if(mapObjectName == "将到期"){
            metter();
            showObjectList(mapObjectName);
        }
    }

    var isEditFlag;
    function locationTagging(gridId,x,y,mapt,isEdit){
        var callBackUrl = 'http://${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml'
        var width = 480;
        var height = 380;
        var mapType = 'EVENT_V1';
        isEditFlag = isEdit;
        initMapMarkerInfoSelector(gridId,callBackUrl,x,y,mapt,width,height,isEdit,mapType)
    }
    function mapMarkerSelectorCallback(mapt, x, y){//将标注信息传递到新增、编辑页面
        var childIframeContents = $("#MaxJqueryWindowContent").find("iframe").contents();
        childIframeContents.find("#mapt").val(mapt);
        childIframeContents.find("#x").val(x);
        childIframeContents.find("#y").val(y);
        var showName = "修改地理位置";
        if(isEditFlag == false) {
            showName = "查看地理位置";
        }
        childIframeContents.find("#mapTab").html(showName);
        childIframeContents.find(".mapTab").addClass("mapTab2");
        closeMaxJqueryWindowForCross();

    }

    function rentRoomTip(isShow) {
        if (isShow) $("#rentRoomTip").show();
        else $("#rentRoomTip").hide();
    }

</script>
</html>
