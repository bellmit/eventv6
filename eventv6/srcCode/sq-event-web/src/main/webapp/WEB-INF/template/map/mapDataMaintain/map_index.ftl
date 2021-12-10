<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>地图数据维护地图页面</title>
    <#include "/map/arcgis/arcgis_base/arcgis_common_versionnoe.ftl" />
	<#include "/component/ImageView.ftl" />


    <link rel="stylesheet" href="${rc.getContextPath()}/js/cxcolor/css/jquery.cxcolor.css">
    <script type="text/javascript" src="${rc.getContextPath()}/js/cxcolor/js/jquery.cxcolor.js" ></script>
    <script type="text/javascript" src="${SQ_ZZGRID_URL!''}/js/global.js" ></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/mapDataMaintain/buildingMapTool.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/mapDataMaintain/urbanPartsMapTool.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/mapDataMaintain/gridMapTool.js"></script>

    <style type="text/css">
        html, body {
            height:100%;
            margin: 0;
            padding: 0;
            width:100%;
            overflow:hidden;

        }
        #header {
            overflow:hidden;
            height: 1.6em;
        }
        div.insetType {
            color: #97F900;
            font-size: 24px;
            font-family: Rockwell, Georgia, "Times New Roman", Times, serif;
            padding-left: 25px;
        }

        .menuItemStyle{
            height:30px;
            font-size:20px
        }
        .jsSlider{
            top: 70px
        }
        .MapTools{
            top: 270px
        }

        *{margin:0; padding:0; list-style:none;}
        .AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
        .AlphaBack1{color:#fff}
        .AlphaBack1 select{color:#000}
        .AlphaBack1 tr td{padding-top:2px;padding-right:2px;}
        .inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
        .button1{width:60px; height:28px; line-height:26px; text-align:center;}
        .NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
        .NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
        td{margin: 0;padding: 5px;list-style: none;}
        .ol-zoom{top : 3.5em}


        #buildingDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:140px; display:none;}
        #buildingDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
        #buildingDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}

    </style>
</head>

<body class="claro"  style="font-size: 0.75em;">
<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId}</#if>"/>
<input type="hidden" id="bizIds" value=""/>
<input type="hidden" id="wid" value="" />
<input type="hidden" id="name" value="" />
<input type="hidden" id="currentModuleType" value="" />
<input type="hidden" id="gridLevel" value="0" />
<#include "/map/mapDataMaintain/buildingToolBar.ftl"/>
<#include "/map/mapDataMaintain/urbanPartsToolBar.ftl"/>
<#include "/map/mapDataMaintain/gridToolBar.ftl"/>
<div id="mapDiv">
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
        <li class="ThreeWei" onclick="threeWeiClick()"></li>
    </ul>
    <div id="mapStyleDiv" class="MapStyle" style="display:none">
        <span class="current">二维图</span>
        <span>三维图</span>
        <span>卫星图</span>
    </div>
</div>
</body>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>

<script>
    var flagPoint = false;
    var defaultx;
    var defaulty;
    var tipMessage = ""; //提示信息;
    var ZZGRID_URL = "<#if SQ_ZZGRID_URL??>${SQ_ZZGRID_URL}</#if>";




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

            getArcgisInfo(function(){
            	// 地图加载完毕回调
            	window.parent.DataIframe.mapLoadedCallBack();
            	$("#map"+currentN).ffcsMap.getMap().on("click", function(evt) {
           			if(parent.document.getElementById("showXieJingAddress").checked == true){
                		if (!evt.graphic) {
                			console.log(evt);
                			 var centerX = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().x;
                             var centerY = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().y;
                             var regionCode = parent.document.getElementById("infoOrgCode").value;
                             console.log(evt.mapPoint.x+"---"+evt.mapPoint.y+"---"+regionCode);
                            
             					
                                 loadXieJingData(regionCode, evt.mapPoint.x, evt.mapPoint.y);
                            
                		}
               		}
                 });
            	
            });

            modleclose();

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
                var curIndex = 0;
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
                    currentArcgisConfigInfo = arcgisConfigInfos[curIndex];
                    loadArcgis(arcgisConfigInfos[curIndex],"map","jsSlider","switchMap",backFn);

                }
            }
        });
    }

    //获取加载地图的信息并且初始化地图信息对象
    function getMapArcgisInfo(){
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapArcgisInfo.json',
            type: 'POST',
            timeout: 3000,
            dataType:"json",
            async: false,
            error: function(data){
                $.messager.alert('友情提示','地图配置信息获取出现异常!','warning');
            },
            success: function(data){
                var mapConfigInfo=eval(data.mapConfigInfo);
                arcgisMapConfigInfo.Init(mapConfigInfo);
                if(arcgisMapConfigInfo.mapStartType == "5") {//表示地图默认显示为2维
                    currentArcgisMapInfo.Init2D(arcgisMapConfigInfo);
                }
                loadArcgisMap("map","jsSlider");
            }
        });
    }

    function getArcgisDataByCurrentSet(){
        var gridId = document.getElementById("gridId").value;
        if(gridId != "" && gridId != null && typeof(gridId) != undefined){
            //中心点定位
            locateGridCenter(gridId,currentArcgisConfigInfo.mapType);
        }
        $("#jsSlider").css("top","70px");
        $("#map"+currentN).ffcsMap.getMap().on("mouse-drag-end", function(e) {
            if(parent.document.getElementById("showXieJingAddress").checked == true && e.button==0) {//拖拽

                var centerX = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().x;
                var centerY = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().y;
                var regionCode = parent.document.getElementById("infoOrgCode").value;
                if(regionCode != 'undefined' && regionCode != null && regionCode.length>=12){
					
                    //loadXieJingData(regionCode, centerX, centerY);
                }

            }
        });
    }


    /**
     * 定位网格中心点
     * @param gridId
     * @param mapt
     */
    function locateGridCenter(gridId, mapt) {
        if(typeof mapt == 'undefined' || mapt == null || mapt == ''){
            mapt = currentArcgisConfigInfo.mapType;
        }
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+mapt+'&wid='+gridId+'&targetType=grid',
            type: 'POST',
            timeout: 3000,
            dataType:"json",
            async: false,
            error: function(data){
                alert("系统无法获取中心点以及显示层级!");
            },
            success: function(data){
                var obj = data.result;
                var showLevel = 0;
                var centerX,centerY
                if(obj != null) {
                    $("#map"+currentN).ffcsMap.centerAt({
                        x : obj.x,          //中心点X坐标
                        y : obj.y,           //中心点y坐标
                        wkid : currentArcgisConfigInfo.wkid, //wkid 2437
                        zoom : obj.mapCenterLevel
                    });
                    defaultx = obj.x;
                    defaulty = obj.y;
                    centerX = obj.x;
                    centerY = obj.y;
                }else {
                    defaultx = currentArcgisConfigInfo.mapCenterX;
                    defaulty = currentArcgisConfigInfo.mapCenterY;
                }

                if(parent.document.getElementById("showXieJingAddress").checked == true) {//拖拽
                    if(centerX == null || centerX == 0 || centerY == null || centerY == 0  ){
                        centerX = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().x;
                        centerY = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().y;
                    }

                    var regionCode = parent.document.getElementById("infoOrgCode").value;
                    if(regionCode != 'undefined' && regionCode != null && regionCode.length>=12){
                        loadXieJingData(regionCode, centerX, centerY);
                    }
                }
            }
        });
    }

    /**
     * 清除图层信息
     * @param layerName
     */
    function clearLayerByName(layerName){
        if(typeof layerName != "undefined" && layerName != null && layerName != ""){
            if(layerName != "gridLayer"){
                $("#map"+currentN).ffcsMap.clear({layerName : layerName});
            }else{
                var layer = $("#map"+currentN).ffcsMap.getMap().getLayer(layerName);
                if(typeof layer != 'undefined' && layer != null){
                    $("#map"+currentN).ffcsMap.getMap().removeLayer(layer);
                }
            }

        }
    }


//    function reGisPositionByType(){
//        $("#elementsCollectionStr").val(data.elementsCollectionStr);
//        window.parent.MapIframe.currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+$('#elementsCollectionStr').val()+"')";
//
//        window.parent.MapIframe.getArcgisDataOfZhuanTi(url,$('#elementsCollectionStr').val(),370,281);
//    }

    //创建右键菜单
    var ctxMenuForGraphics,selected,currentLocation;

    function mapAddMenu(moduleType, subTypeCode, subTypeId){
        if(typeof moduleType == "undefined" || moduleType == null || moduleType == ""){
            moduleType = $("currentModuleType").val();
        }

        var gridId = parent.document.getElementById("gridId").value;
        var url = ZZGRID_URL + '/zzgl/grid/3509/ar/zzgl/res/defaulteaBuildingInfo/create.jhtml?showRoomTag=false';
        var addTitle = "新增楼宇信息";
        var windowWidth = 1000,windowHeight=500;

        if(moduleType == "building"){
            url = ZZGRID_URL + '/zzgl/grid/3509/areaBuildingInfo/create.jhtml?showRoomTag=false&addressGridId='+gridId;
            addTitle = "新增楼宇信息";
        }
        if(moduleType == "urbanParts"){
            if(typeof subTypeCode != 'undefined' && subTypeCode != null && subTypeCode != ''
                    && typeof subTypeId != 'undefined' && subTypeId != null && subTypeId != '' ){
                $("#urbanParts_typeCode").val(subTypeCode);
                $("#urbanParts_typeId").val(subTypeId);

                if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02020401" || subTypeCode == "020116"){
                    url = ZZGRID_URL + "/zzgl/res/default/create.jhtml?resTypeId="+subTypeId;
                }else if(subTypeCode == "02020501"){//全球眼
                    url = ZZGRID_URL + '/zzgl/res/globalEyes/selectCompanyType.jhtml?';
                }else if(subTypeCode == "02010601"){
                    url = ZZGRID_URL + "/zzgl/fireGrid/fireResource/create1.jhtml?";
                }
                windowWidth = 660,windowHeight=420;
                addTitle = "新增";
            }
        }
        selected = null;

        var map = $("#map"+currentN).ffcsMap.getMap();

        //创建右键菜单
        ctxMenuForGraphics = new dojo.dijit.Menu({
            targetNodeIds: ["map0","map1","map2"],
            onOpen: function(box) {
                // Lets calculate the map coordinates where user right clicked.
                // We'll use this to create the graphic when the user clicks
                // on the menu item to "Add Point"
                currentLocation = getMapPointFromMenuPosition(box, map);
            }
        });

        ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
            label: addTitle,
            class: "menuItemStyle",
            onClick: function(evt) {
                var mp = currentLocation;
                var addUrl = url + "&mapType=" + currentArcgisConfigInfo.mapType + "&x=" + mp.x + "&y=" + mp.y+"&isCrossDomain=true";
                var mapDataWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", addUrl, {title: addTitle, width:windowWidth, height:windowHeight, scrolling: "no"})[0];
                parent.addMsgServer(mapDataWin);
            }
        }));

        //启动右键菜单
        if(moduleType == "grid"){
            ctxMenuForGraphics.destroyRendering();
        }else{
            ctxMenuForGraphics.startup();
        }

    }

    //关闭右键菜单
    function stopMapMenu(){
        if(typeof ctxMenuForGraphics != 'undefined' && ctxMenuForGraphics != null){
            ctxMenuForGraphics = new dojo.dijit.Menu({targetNodeIds: ["map0","map1","map2"]});
            ctxMenuForGraphics.destroyRendering(true);
        }
    }
    //开启右键菜单
    function startMapMenu(){
        if(typeof ctxMenuForGraphics != 'undefined' && ctxMenuForGraphics != null){
            ctxMenuForGraphics.startup();
        }
    }

    //获取坐标
    function getMapPointFromMenuPosition(box, map) {
        var x = box.x, y = box.y;
        switch( box.corner ) {
            case "TR":
                x += box.w;
                break;
            case "BL":
                y += box.h;
                break;
            case "BR":
                x += box.w;
                y += box.h;
                break;
        }

        var screenPoint = new esri.geometry.Point(x - map.position.x, y - map.position.y);
        return map.toMap(screenPoint);
    }

    /**
     * 根据业务类型业务id显示工具栏
     * @param bizType
     * @param bizId
     */
    function mapToolShow(bizType, bizId, subTypeCode){
        if(bizType == "building"){
            $("#gridMapBar").hide();
            $("#urbanPartsMapBar").hide();
            $("#buildingMapBar").show();
            hasBuildingArcgisData(bizId);
        }else if(bizType == "grid"){
            $("#buildingMapBar").hide();
            $("#urbanPartsMapBar").hide();
            $("#gridMapBar").show();
            hasGridArcgisData(bizId);
        }else if(bizType == "urbanParts"){
            $("#gridMapBar").hide();
            $("#buildingMapBar").hide();
            $("#urbanPartsMapBar").show();
            $("#urbanParts_type").val(subTypeCode);
            hasUrbanPartsArcgisData(bizId, subTypeCode);
        }

    }
    
    function mapToolHide() {
    	$(".MapBar").hide();
    }


    /**
     * 根据所属网格加载协警地址，定位并绑定右键新增菜单
     * @param regionCode
     */
    function loadXieJingData(regionCode, centerX, centerY){
    	var radioValue = getRadioValue("showXieJingAddress2");
    	function getRadioValue(radioName){
    	    var radios = parent.document.getElementsByName(radioName);
    	    var value;
    	    for(var i=0;i<radios.length;i++){
    	        if(radios[i].checked){
    	            value = radios[i].value;
    	            break;
    	        }
    	    }
    	    return value;
    	}
        divModleOpen('map'+currentN);
        if(typeof centerX == 'undefined' || centerX == null || centerX == ''){
            centerX = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().x;
        }
        if(typeof centerY == 'undefined' || centerY == null || centerY == ''){
            centerY = $("#map"+currentN).ffcsMap.getMap().extent.getCenter().y;
        }

        $("#map"+currentN).ffcsMap.clear({layerName : "xiejingLayer"});
        var url =  js_ctx +'/zhsq/map/mapDataMaintain/xiejingListData.json?regionCode='+regionCode+'&x='+centerX+'&y='+centerY;
        if(typeof radioValue != 'undifined'){
        	url=url+'&distance='+radioValue;
        }
        var imgUrl = "${uiDomain!''}/images/map/gisv0/map_config/unselected/mark_standardAddress.png";
        $("#map"+currentN).ffcsMap.render('xiejingLayer', url, 0, true, imgUrl, 30, 39);

        setTimeout(function(){
            divModleClose();
            selected = null;
            var layer = $("#map"+currentN).ffcsMap.getMap().getLayer('xiejingLayer');
            var bizType = $("#currentModuleType").val();

            var gridId = parent.document.getElementById("gridId").value;
            var addUrl = "";
            var addTitle = "新增楼宇信息";
            var width = 1080;
            var height= 500;

            if(bizType == "building"){
                addUrl = "";
                addUrl = ZZGRID_URL + '/zzgl/grid/3509/areaBuildingInfo/create.jhtml?showRoomTag=false&addressGridId='+gridId;
                addTitle = "新增楼宇信息";
                width = 1080;
                height= 500;
            }else if(bizType == "urbanParts"){
                addUrl = "";
                var subTypeId = $("#urbanParts_typeId").val();
                var subTypeCode = $("#urbanParts_typeCode").val();
                if(typeof subTypeCode != 'undefined' && subTypeCode != null && subTypeCode != ''
                        && typeof subTypeId != 'undefined' && subTypeId != null && subTypeId != '' ){

                    if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02020401"){
                        addUrl = ZZGRID_URL + "/zzgl/res/default/create.jhtml?resTypeId="+subTypeId;
                    }else if(subTypeCode == "02020501"){//全球眼
                        addUrl = ZZGRID_URL + '/zzgl/res/globalEyes/selectCompanyType.jhtml?';
                    }else if(subTypeCode == "02010601"){
                        addUrl = ZZGRID_URL + "/zzgl/fireGrid/fireResource/create1.jhtml?";
                    }
                    width = 660,height=420;
                    addTitle = "新增";
                }
            }

            //创建右键菜单
            var ctxMenuForGraphics2
            ctxMenuForGraphics2 = new dojo.dijit.Menu({});

            ctxMenuForGraphics2.addChild(new dojo.dijit.MenuItem({
                label: addTitle,
                class: "menuItemStyle",
                onClick: function() {
                    var data;
                    if(typeof selected.attributes != 'undeined' && selected.attributes != null){
                        data = selected.attributes.data;
                        var address = data.gridName;
                        var mapType = currentArcgisConfigInfo.mapType;
                        var x = data.x;
                        var y = data.y;

                        if(judgeBuildingAddressIsExits(address)){
                            var url = addUrl + "&address=" + address + "&mapType=" + mapType + "&x=" + x + "&y=" + y+"&isCrossDomain=true";
                            var mapDataWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", url, {title: addTitle, width:width, height:height, scrolling: "no"})[0];
                            parent.addMsgServer(mapDataWin);
                        }
                    }


                }
            }));

            layer.on("mouse-over", function(evt) {
                selected = evt.graphic;
                ctxMenuForGraphics2.bindDomNode(evt.graphic.getDojoShape().getNode());
            });

            layer.on("mouse-out", function(evt) {
                ctxMenuForGraphics2.unBindDomNode(evt.graphic.getDojoShape().getNode());
            });
            
            //启动右键菜单
            ctxMenuForGraphics2.startup();
        },500);
    }

    /**
     * 切换模块
     * @param moduleType
     */
    function changeModule(moduleType){
        windowPanelHelper.hideAllWindow();
        $("#bizIds").val("");
        $("#currentModuleType").val(moduleType);
        if(moduleType != "building"){
            endEditMapData();
            $("#buildingMapBar").hide();
            clearSpecialLayer("buildingLayer");
            clearSpecialLayer("buildLayer");
            clearSpecialLayer("gridDataLayer");
        }
        if(moduleType != "grid"){
            endEditGridMapData();
            $("#gridMapBar").hide();
            clearSpecialLayer("gridDataLayer");
        }
        if(moduleType != "urbanParts"){
            endEditUrbanPartsMapData();
            $("#gridMapBar").hide();
            $("#buildingMapBar").hide();
            clearSpecialLayer("buildingLayer");
            clearSpecialLayer("buildLayer");
            clearSpecialLayer("gridDataLayer");
        }
    }

    //打开遮罩
    function divModleOpen(divId) {
        $("<div class='datagrid-mask'></div>").css( {
            display : "block",
            width : "100%",
            height : $(window).height()
        }).appendTo("#"+divId);
        $("<div class='datagrid-mask-msg'></div>").html("正在处理，请稍候。。。").appendTo(
                "#"+divId).css( {
                    display : "block",
                    left : ($("#"+divId).width() / 2)+85,
                    top : (($("#"+divId).height() - 45) / 2),
                    height : 15,
                    width : 160
                });
        document.body.scroll="no";//除去滚动条
    }
    //关闭遮罩
    function divModleClose() {
        $(".datagrid-mask").css( {
            display : "none"
        });
        $(".datagrid-mask-msg").css( {
            display : "none"
        });
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
        document.body.scroll="auto";//开启滚动条
    }


    //重新定位
    function reGisPositionByType(type){
        //重新定位
        window.parent.DataIframe.gisPositionByType(type,$("#bizIds").val());
    }


</script>
</html>
