<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图首页</title>

    <#--<#include "/map/base/map_common.ftl" />-->
    <script type="text/javascript">
        var uiDomain = "${uiDomain!''}";
        var gmisDomain = "${GMIS_DOMAIN!''}";
        var js_ctx =  "${rc.getContextPath()}";
        var _myServer ="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8";
        _myServer = _myServer.replace("http://","");
        var _myServer_compact = "${SQ_ZHSQ_EVENT_URL}";
        _myServer_compact = _myServer_compact.replace("http://","");

        var AUTOMATIC_CLEAR_MAP_LAYER = "<#if AUTOMATIC_CLEAR_MAP_LAYER??>${AUTOMATIC_CLEAR_MAP_LAYER}</#if>";
        var IS_IMAGE_MAP_SHOW_CONTOUR = "<#if IS_IMAGE_MAP_SHOW_CONTOUR??>${IS_IMAGE_MAP_SHOW_CONTOUR}</#if>";
        var IS_VECTOR_MAP_SHOW_CONTOUR = "<#if IS_VECTOR_MAP_SHOW_CONTOUR??>${IS_VECTOR_MAP_SHOW_CONTOUR}</#if>";
        var IS_GRID_ARCGIS_SHOW_CENTER_POINT = "<#if IS_GRID_ARCGIS_SHOW_CENTER_POINT??>${IS_GRID_ARCGIS_SHOW_CENTER_POINT}</#if>";
        var OUTLINE_FONT_SETTINGS = "<#if OUTLINE_FONT_SETTINGS??>${OUTLINE_FONT_SETTINGS}</#if>";
        var LC_INFO_ORG_CODE = "<#if LC_INFO_ORG_CODE??>${LC_INFO_ORG_CODE}</#if>";
        var OUTLINE_FONT_SETTINGS_BUILD = "<#if OUTLINE_FONT_SETTINGS_BUILD??>${OUTLINE_FONT_SETTINGS_BUILD}</#if>";
        var IS_ACCUMULATION_LAYER = "<#if LC_INFO_ORG_CODE??>${IS_ACCUMULATION_LAYER}</#if>";
        var ARCGIS_DOCK_MODE = "<#if ARCGIS_DOCK_MODE??>${ARCGIS_DOCK_MODE}</#if>";
        var arcgisFactorUrl = "<#if arcgisFactorUrl??>${arcgisFactorUrl}</#if>";
        var CENTER_POINT_SETTINGS_BUILD = "<#if CENTER_POINT_SETTINGS_BUILD??>${CENTER_POINT_SETTINGS_BUILD}</#if>";
        var SURVEILLANCE_MSG_METHOD = "<#if SURVEILLANCE_MSG_METHOD??>${SURVEILLANCE_MSG_METHOD}</#if>";

        //网格各层级显示中心点名称的地图层级设置
        var MAP_LEVEL_TRIG_CONDITION_PROVINCE = "<#if MAP_LEVEL_TRIG_CONDITION_PROVINCE??>${MAP_LEVEL_TRIG_CONDITION_PROVINCE}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_CITY = "<#if MAP_LEVEL_TRIG_CONDITION_CITY??>${MAP_LEVEL_TRIG_CONDITION_CITY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_COUNTY = "<#if MAP_LEVEL_TRIG_CONDITION_COUNTY??>${MAP_LEVEL_TRIG_CONDITION_COUNTY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_STREET = "<#if MAP_LEVEL_TRIG_CONDITION_STREET??>${MAP_LEVEL_TRIG_CONDITION_STREET}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_COMMUNITY = "<#if MAP_LEVEL_TRIG_CONDITION_COMMUNITY??>${MAP_LEVEL_TRIG_CONDITION_COMMUNITY}<#else>0</#if>";
        var MAP_LEVEL_TRIG_CONDITION_GRID = "<#if MAP_LEVEL_TRIG_CONDITION_GRID??>${MAP_LEVEL_TRIG_CONDITION_GRID}<#else>0</#if>";
        var playerMessenger;
    </script>

    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/dojo/dijit/themes/claro/claro.css">
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/style/css/mapindex.css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css" />
    <link rel="stylesheet" href="${rc.getContextPath()}/map/base/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
    <link rel="stylesheet" href="${rc.getContextPath()}/map/base/css/mapgrid-zTree.css" />
    <link rel="stylesheet" href="${rc.getContextPath()}/map/base/css/reset.css" />
    <link rel="stylesheet" href="${rc.getContextPath()}/map/base/css/mapgrid.css" />

    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>
    <script src="${uiDomain!''}/js/jquery-ui.js" ></script>
    <script src="${uiDomain!''}/js/jquery.ui.draggable.js" ></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/init.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMap.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMeasure.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSlider.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsFillQuery.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsTianDiTuLayer.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_versionnoe.js"></script>
    <#--<script src="${rc.getContextPath()}/js/map/arcgis/arcgis_base/function_versionnoe.js"></script>-->

    <script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
    <script src="${rc.getContextPath()}/map/base/zTree_v3/js/jquery.ztree.core.js"></script>
    <#--<script src="${rc.getContextPath()}/map/base/js/mapgrid.js"></script>-->
    <script src="${rc.getContextPath()}/map/base/js/jquery.nicescroll.js"></script>
    <script src="${rc.getContextPath()}/map/base/js/map_base.js"></script>

</head>
<body style="width:100%;height:100%;border:none;" >
<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
<div>
    <div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
    </div>
    <div class="page-container" id="map1" style="position: absolute; width:100%; height:100%; z-index: 2;display:none;">
    </div>
    <div class="page-container" id="map2" style="position: absolute; width:100%; height:100%; z-index: 3;display:none;">
    </div>
</div>
<div id="jsSlider"></div>
<#include "/map/base/left_nav.ftl">
<#include "/map/base/right_nav.ftl">
<div class="MapTools" style="z-index: 2">
    <ul>
        <li class="ClearMap" onclick="clearMyLayerB();"></li>
        <li class="ThreeWei" onclick="threeWeiClick();"></li>
    </ul>
    <div id="mapStyleDiv" class="MapStyle" style="display:none">
        <span class="current">二维图</span>
        <span>三维图</span>
        <span>卫星图</span>
    </div>
</div>
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
</form>
</body>
<script type="text/javascript">
    var SHOW_CURRENT_GRID_LEVEL_OUTLINE = "<#if SHOW_CURRENT_GRID_LEVEL_OUTLINE??>${SHOW_CURRENT_GRID_LEVEL_OUTLINE}</#if>";

    $(function(){
        window.focus();
        modleopen();
        $("#map0").css("height",$(document).height());
        $("#map0").css("width",$(document).width());
        $("#map1").css("height",$(document).height());
        $("#map1").css("width",$(document).width());
        $("#map2").css("height",$(document).height());
        $("#map2").css("width",$(document).width());
        setTimeout(function(){
            var level;
            level = (parseInt($("#gridLevel").val()) < 6) ? parseInt($("#gridLevel").val())+1 : parseInt($("#gridLevel").val());
            if(SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && typeof document.getElementById("gridLevelName"+(level-1)) != 'undefined'
                    && document.getElementById("gridLevelName"+(level-1)) != null){
                document.getElementById("gridLevelName"+(level-1)).checked = true;
            }else{
                document.getElementById("gridLevelName"+level).checked = true;
            }

            getArcgisInfo(function() {// 地图初始化完毕
                locateCenterAndLevel($("#gridId").val(),currentArcgisConfigInfo.mapType);
            });

            changeCheckedAndStatus($("#gridLevel").val(),level);

            modleclose();


            getMenuInfo();
        },100);
        window.onresize=function(){
            $("#map0").css("height",$(document).height());
            $("#map0").css("width",$(document).width());
            $("#map1").css("height",$(document).height());
            $("#map1").css("width",$(document).width());
            $("#map2").css("height",$(document).height());
            $("#map2").css("width",$(document).width());
        }


    });



    function getArcgisDataByCurrentSet(){

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
                    loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap",backFn);
                }
            }
        });
    }


    function clearMyLayerB(param) {
        clearMyLayer();
    }

</script>
</html>
