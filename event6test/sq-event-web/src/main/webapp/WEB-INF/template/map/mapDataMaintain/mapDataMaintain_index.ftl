<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图数据维护首页</title>

    <#include "/component/commonFiles-1.1.ftl" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/themes/gray/easyui.css">
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css" />

    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-1.3.5/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${SQ_ZHSQ_EVENT_URL}/js/jqueryeasyui-last/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript" src="${SQ_ZZGRID_URL}/component/msgClient.jhtml"></script>
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/js/global.js"></script>

    <style>
        .ztree li{overflow:hidden;}
        .ztree li span{font-size:14px;}
        .mapTools{
            height: 24px;
            padding-top: 8px;
            margin-left: 20px;
            position: relative;
            float: left;
            color: #fff;
            font-size: 14px;
            font-weight: bold;
        }
    </style>
</head>
<body class="easyui-layout">

<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId}</#if>">
<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="<#if infoOrgCode??>${infoOrgCode}</#if>">



<div data-options="region:'west'" title="<span class='easui-layout-title'>网格信息域</span>" split="true" style="width:220px;height: 100%">
<#include "/component/gridTree.ftl" />
</div>

<div data-options="region:'center'" border="0" style="float: left;overflow-y: hidden;" id="divRight">

<div data-options="region:'north'" style="height: 30px;">
    <div class="MapBar con AlphaBack">
        
        <div class="mapTools">
            <span><input type="checkbox" name="showGridHs" id="showGridHs" onclick="showGridHs();"/>显示网格轮廓</span>
        </div>
        <div class="mapTools">
            <span><input type="checkbox" name="showXieJingAddress" id="showXieJingAddress" onclick="showXieJingAddress();"/>点击地图显示周边标准地址</span>
            <span class="levelCheck">：</span>
            <span class="levelCheck"><input type="radio"  name="showXieJingAddress2"  value="100" checked="checked"/>100米</span>
            <span class="levelCheck"><input type="radio" name="showXieJingAddress2"  value="200"/>200米</span>
            <span class="levelCheck"><input type="radio" name="showXieJingAddress2"  value="300"/>300米</span>
        </div>
        <div class="mapTools" style="float: right;margin-right: 100px">
            <div id="tipMessage" style="color:yellow;font-size:15px;float:right"></div>
            <div id="blind" class="blind" style="color:yellow;font-size:15px;float:right;"></div>
        </div>
    </div>
</div>

    <div class="easyui-layout" style="width:100%;height:100%;overflow-y: hidden;">
        <div data-options="region:'west'" border="0" class="LeftTree" style="width:405px;float: left;overflow-y: hidden;">
            <iframe data-iframe="true" name="DataIframe" id="DataIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
        </div>
        <div data-options="region:'center'" style="float:right;height: 100%;overflow-y: hidden;">
            <iframe data-iframe="true" name="MapIframe" id="MapIframe" src="" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
        </div>
    </div>
</div>

</body>
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript" src="${rc.getContextPath()}/js/map_gridforliger.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<script type="text/javascript">
//    window.onload = function(){
//        var selectTree = $(".SelectTree");
//        $(".local").hover(function() {
//            selectTree.removeClass("dest").animate({top: 32},10, function() {
//                selectTree.show();
//            });
//        }, function() {
//            selectTree.addClass("dest").animate({top: 32}, 10, function() {
//                selectTree.hide();
//            });
//        });
//
//        $(".location li").click(function() {
//
//            var div = $(".SelectTree");
//            if(div.hasClass("dest")) {
//                div.removeClass("dest").animate({top: 50, left: 75},10, function(){
//                    $(".SelectTree").show(500);
//                });
//            } else {
//                div.addClass("dest").animate({top: 50, left: 75}, 10, function(){
//                    $(".SelectTree").hide(500);
//                });
//            }
//        });
//    }

    $(function(){
        renderTree();
        loadArcgisMap(${gridId?c});
        $(".levelCheck").hide();
        setTimeout(loadDataList(), 1000);
    });

    function loadArcgisMap(gridId){
        var url = '${rc.getContextPath()}/zhsq/map/mapDataMaintain/toMap.jhtml?gridId='+gridId;
        $("#MapIframe").attr("src",url);
    }

    function loadDataList(){
        var url = '${rc.getContextPath()}/zhsq/map/mapDataMaintain/toMapDataList.jhtml';
        $("#DataIframe").attr("src",url);
    }

    //选择树的回调函数
    function gridTreeClickCallback(gridId,gridName,orgId,orgCode,gridInitPhoto,gridLevel,gridCode){
//        $(".SelectTree").css("display","none");
        if(gridCode != $("input[name='gridCode']").val() || (SHOW_CURRENT_GRID_LEVEL_OUTLINE == "true" && gridIds.split(",").length>2)) {
            $("#changeGridName").text(gridName);
            $("input[name='gridId']").val(gridId);
            $("input[name='gridCode']").val(gridCode);
            $("input[name='gridName']").val(gridName);
            $("input[name='gridLevel']").val(gridLevel);
            $("input[name='orgCode']").val(orgCode);
            $("input[name='infoOrgCode']").val(orgCode);

            var level = (parseInt(gridLevel) < 6) ? parseInt(gridLevel)+1 : parseInt(gridLevel);
            window.MapIframe.locateGridCenter(gridId);
            showGridHs(gridId);
			window.MapIframe.mapToolHide();
            //加载列表数据
            var currentModuleType = MapIframe.currentModuleType.value;
            if(currentModuleType == "building"){
                window.DataIframe.loadBuildingMessage(1,20);
            }else if(currentModuleType == "urbanParts"){
                window.DataIframe.loadUrbanPartsMessage(1,20);
            }else if(currentModuleType == "grid"){
                window.DataIframe.loadGridMessage(1,20);
            }
            //clearMyLayer();
            level = level - 1;

        }
    }

    /**
     * 显示网格轮廓
     * @param gridId
     */
    function showGridHs(gridId){
        if(typeof gridId == "undefined" || gridId == null || gridId == ""){
            gridId = $("#gridId").val();
        }
        if(document.getElementById("showGridHs").checked == true){
            window.MapIframe.clearLayerByName("gridLayer");
            window.MapIframe.getMapArcgisDatas(gridId, "gridLayer");
        }else{

            window.MapIframe.clearLayerByName("gridLayer");
        }

        //加载列表数据
//        var currentModuleType = MapIframe.currentModuleType.value;
//        if(currentModuleType == "building"){
//            window.DataIframe.loadBuildingMessage(1,20);
//        }else if(currentModuleType == "urbanParts"){
//            window.DataIframe.loadUrbanPartsMessage(1,20);
//        }else if(currentModuleType == "grid"){
//            window.DataIframe.loadGridMessage(1,20);
//        }

    }

    /**
     * 显示协警地址
     */
    function showXieJingAddress(){
        if(document.getElementById("showXieJingAddress").checked == true){
        	$(".levelCheck").show();
            var infoOrgCode = document.getElementById("infoOrgCode").value;
            /* if(typeof infoOrgCode != 'undefined' && infoOrgCode != null && infoOrgCode.length >= 12){
                window.MapIframe.loadXieJingData(infoOrgCode);//只加载当前所选的网格的协警地址，现换成点击地图获取数据
            } */

        }else{
        	$(".levelCheck").hide();
            window.MapIframe.clearLayerByName("xiejingLayer");
        }
    }


    function DivHide(){
        $("#tipMessage").hide();//窗帘效果展开
    }
    function DivShow(msg){
        $("#tipMessage").html(msg);
        $("#tipMessage").show();//窗帘效果展开
        setTimeout("this.DivHide()",3000);
    }

    /**
     * 添加消息发送监听
     */
    function addMsgServer(iframeObj){
        gmMsgClient.addObserver(iframeObj, receiveCloseWindowMsg, "closeWindow");
    }

    /**
     * 消息回调
     */
    function receiveCloseWindowMsg(msgData){
        windowPanelHelper.hideAllWindow();
        if(typeof msgData != "undefined" && msgData != null && msgData != "" && JSON.stringify(msgData)){
            if(typeof msgData != 'undefined' && typeof msgData.result != 'undefined'){
                $.messager.alert('提示', msgData.result, 'info');
//                var bizType = window.MapIframe.getElementById('currentModuleType').value;
                var bizType = $("#MapIframe").contents().find("#currentModuleType")[0].value;
                if(bizType == "building"){
                    //加载列表数据
                    window.DataIframe.loadBuildingMessage(1,20);
                }
                if(bizType == "urbanParts"){
                    //加载列表数据
                    window.DataIframe.loadUrbanPartsMessage(1,20);
                }
            }
        }
    }

</script>
</html>
