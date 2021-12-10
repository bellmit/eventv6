<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>协警地图标注</title>
    <script type="text/javascript">
        var callBackUrl = '${(callBackUrl)!}';
        var uiDomain = '${(uiDomain)!}';
        var js_ctx = "${rc.getContextPath()}";
        var _myServer = '${(SQ_ZHSQ_EVENT_URL)!}/js/map/arcgis/library/3.8';
        _myServer = _myServer.replace('http://', '');
        var _myServer_compact = '${(SQ_ZHSQ_EVENT_URL)!}';
        _myServer_compact = _myServer_compact.replace('http://', '');
        var AUTOMATIC_CLEAR_MAP_LAYER = '${(AUTOMATIC_CLEAR_MAP_LAYER)!}';
        var ARCGIS_DOCK_MODE = '${(ARCGIS_DOCK_MODE)!}';
    </script>
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
    <link rel="stylesheet" href="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
    <link rel="stylesheet" href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css"/>
    <link rel="stylesheet" href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css"/>
    <link rel="stylesheet" href="${uiDomain!''}/css${styleCSS!''}/normal.css"/>
    <link rel="stylesheet" href="${uiDomain!''}/css${styleCSS!''}/easyuiExtend.css"/>

    <script src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    <script src="${uiDomain!''}/js/jquery-ui.js"></script>
    <script src="${uiDomain!''}/js/jquery.ui.draggable.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/3.8/init.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMap.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsMeasure.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSlider.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsSymbolPicker.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsLayerPicker.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/library/ffplugin/ffcsTianDiTuLayer.js"></script>
    <script src="${SQ_ZHSQ_EVENT_URL}/js/map/arcgis/arcgis_base/arcgis_versionnoe.js"></script>
    <script src="${uiDomain!''}/js/function.js"></script>

    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        .AlphaBack1 {
            background-color: rgba(0, 53, 103, 0.5);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567', endColorstr='#8c003567');
        }

        .AlphaBack1 {
            color: #fff
        }

        .AlphaBack1 select {
            color: #000
        }

        .AlphaBack1 tr td {
            padding-top: 3px;
            padding-right: 2px;
        }

        .inp1 {
            width: 100px;
            height: 24px;
            line-height: 24px;
            padding: 0 3px;
            border: 1px solid #666;
        }

        .button1 {
            width: 60px;
            height: 28px;
            line-height: 26px;
            text-align: center;
        }

        .NorToolBtn {
            padding: 4px 7px 4px 25px;
            color: #fff;
            margin-left: 5px;
            margin-right: 5px;
            margin-top: 0px;
            display: block;
            float: left;
            line-height: 14px;
            background-repeat: no-repeat;
            background-color: #ff9f00;
            background-position: 7px 5px;
        }

        .NorToolBtn:hover {
            color: #fff;
            text-decoration: none;
            background-color: #ff9f00;
        }

        .LeftTree .con li.selectRow {
            background: 160px 12px no-repeat rgb(142, 177, 228);
        }

        .LeftTree .con li:hover {
            background: 160px 12px no-repeat rgb(142, 177, 228);
        }

        .LeftTree .con li {
            color: black
        }
    </style>

</head>
<body>
<input type="hidden" name="mapt" id="mapt" value="${(mapt)!}"/>
<input type="hidden" name="x" id="x" value="${(x?c)!}"/>
<input type="hidden" name="y" id="y" value="${(y?c)!}"/>
<input type="hidden" name="mapCenterLevel" id="mapCenterLevel" value="${(mapCenterLevel?c)!}"/>
<input type="hidden" name="gridId" id="gridId" value="${(gridId?c)!}"/>
<input type="hidden" name="wgGisType" id="wgGisType" value="${(wgGisType)!}"/>
<input type="hidden" name="wgGisId" id="wgGisId" value="${(wgGisId?c)!}"/>

<#--<#if isEdit??>-->
<#--    <#if (isEdit == "true")>-->
<#--        <div class="MapBar" id="MapBar" style="float: left;width: 100%;">-->
<#--            <div class="con AlphaBack1" style="height:32px">-->
<#--                <table width="100%" border="0" cellspacing="0" cellpadding="0">-->
<#--                    <tr style="float:left">-->
<#--                        <td>-->
<#--                            <a href="###" onclick="endPointLocate()" class="NorToolBtn SmallSaveBtn">标注</a>&nbsp;-->
<#--                        </td>-->
<#--                        <td>-->
<#--                            <div id="tipMessage" style="color:yellow;font-size:10px;float: right"></div>-->
<#--                        </td>-->
<#--                    </tr>-->
<#--                </table>-->
<#--            </div>-->
<#--        </div>-->
<#--    </#if>-->
<#--</#if>-->
<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
</div>
<div class="page-container" id="map1" style="position: absolute; width:480px; height:100%; z-index: 2;display:none;">
</div>
<div class="page-container" id="map2" style="position: absolute; width:480px; height:100%; z-index: 3;display:none;">
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
<script>
    var flagPoint = false;
    var mapType = '${(mapType)!}';
    var isEdit = '${(isEdit)!}';
    var cenx,gridx=0;
    var ceny,gridy=0;
    var level,gridLevel=0;

    window.onresize = function () {
        resizeMap();
    }

    $(function () {
        $('#content-d').css('height', $(document).height() - 60);
        initMapDiv();
        getArcgisInfo();
    })

    function resizeMap() {
        initMapDiv();
        setTimeout(function () {
            setMapArcgisPoint();
        }, 1000);
    }

    function initMapDiv() {
        $('#map0').css('height', $(document).height());
        $('#map0').css('width', $(document).width());
        $('#map1').css('height', $(document).height());
        $('#map1').css('width', $(document).width());
        $('#map2').css('height', $(document).height());
        $('#map2').css('width', $(document).width());
    }

    function showPolygon() {
        var map = $.fn.ffcsMap.getMap();

        //-- 区域
        var locationList = jQuery.parseJSON('${locationListJson}');
        if (locationList != null && locationList.length > 0) {
            var locationPoints = [];
            for (var i = 0; i < locationList.length; i++) {
                var point = new esri.geometry.Point(locationList[i].x, locationList[i].y);
                locationPoints.push(point);
            }
            locationPoints.push(new esri.geometry.Point(locationList[0].x, locationList[0].y));

            var polygon = new esri.geometry.Polygon();
            polygon.addRing(locationPoints);


            var symbol = new esri.symbols.SimpleFillSymbol();
            symbol.setColor(new dojo.Color([255, 0, 0, 0.25]));

            var graphic = new esri.Graphic(polygon, symbol);

            var polygonGraphicsLayer = new esri.layers.GraphicsLayer({id: 'polygonGraphicsLayer'});
            polygonGraphicsLayer.add(graphic);
            map.addLayer(polygonGraphicsLayer);
        }
    }

    function threeWeiClick() {
        var mapStyleDiv = document.getElementById("mapStyleDiv");
        if (mapStyleDiv.style.display == 'none') {
            mapStyleDiv.style.display = 'block';
        } else {
            mapStyleDiv.style.display = 'none';
        }
    }

    var currentMapStyleObj;

    //获取arcgis地图路径的配置信息
    function getArcgisInfo() {
        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t=' + Math.random(),
            type: 'POST',
            timeout: 3000,
            dataType: 'json',
            async: false,
            error: function (data) {
                $.messager.alert('友情提示', '地图配置信息获取出现异常!', 'warning');
            },
            success: function (data) {
                var aci = eval(data.arcgisConfigInfos);
                arcgisConfigInfos = [];
                var mapTypeTemporary = 0;
                var mapTypeN = 0;
                for (var i = 0; i < aci.length; i++) {
                    if (mapType == '') {
                        if (i == 0) {
                            arcgisConfigInfos[mapTypeN] = aci[i];
                            mapTypeTemporary = aci[i].mapType;
                            mapTypeN++;
                        } else if (mapTypeTemporary == aci[i].mapType) {
                            arcgisConfigInfos[mapTypeN] = aci[i];
                            mapTypeN++;
                        }
                    } else if (mapType == '2') {
                        if (aci[i].mapType == 5) {
                            arcgisConfigInfos[mapTypeN] = aci[i];
                            mapTypeN++;
                        }
                    } else if (mapType == '3') {
                        if (aci[i].mapType == 30) {
                            arcgisConfigInfos[mapTypeN] = aci[i];
                            mapTypeN++;
                        }
                    } else {
                        arcgisConfigInfos = aci;
                    }
                }
                var htmlStr = '';
                for (var i = 0; i < arcgisConfigInfos.length; i++) {
                    if (i == 0) {
                        htmlStr += '<span class="current" onclick="switchArcgisByNumber(this,' + i + ')">' + arcgisConfigInfos[i].mapTypeName + '</span>';
                    } else {
                        htmlStr += '<span onclick="switchArcgisByNumber(this,' + i + ')">' + arcgisConfigInfos[i].mapTypeName + '</span>';
                    }
                }
                var mapStyleDiv = document.getElementById('mapStyleDiv');
                mapStyleDiv.innerHTML = htmlStr;
                $('#mapStyleDiv').width(60 * arcgisConfigInfos.length + 8)
                if (htmlStr) {
                    currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
                }
                if (arcgisConfigInfos.length > 0) {
                    loadArcgis(arcgisConfigInfos[0], 'map', 'jsSlider', 'switchMap');
                }
            }
        });
    }

    function switchArcgisByNumber(obj, n) {
        if (currentMapStyleObj != obj) {
            currentMapStyleObj.className = '';
            obj.className = 'current';
            currentMapStyleObj = obj;
            switchArcgis(arcgisConfigInfos[n], n);
        }
    }

    function getArcgisDataByCurrentSet() {//地图加载之后回调
        getArcgisOfGridDatas();
        /*两违*/
        var wgGisId = $('#wgGisId').val();
        if(wgGisId){
        	getArcgisOftwoVioPreDatas();
        }
        setMapArcgisPoint();
    }

    function setMapArcgisPoint() {//设置点位
        var x = $('#x').val();
        var y = $('#y').val();
        var mapCenterLevel = $('#mapCenterLevel').val();
        if (mapCenterLevel == 0) {
            if(gridLevel==0){
                mapCenterLevel = currentArcgisConfigInfo.zoom;
            }else{
                mapCenterLevel=gridLevel;
            }
        }
        if (x == 0 && y == 0) {
            if(cenx==0 && ceny==0){
                x = currentArcgisConfigInfo.mapCenterX;
                y = currentArcgisConfigInfo.mapCenterY;
            }else{
                x=gridx;
                y=gridy;
            }
        }else if(x==gridx && y==gridy){
            if(0!=cenx && 0!=ceny){
                x=cenx;y=ceny;mapCenterLevel=level;
            }
        }

        $('#x').val(x);
        $('#y').val(y);
        $('#mapt').val(currentArcgisConfigInfo.mapType);

        if (isEdit == "true" || isEdit == true) {
            isEdit = true;
        } else {
            isEdit = false;
        }

        $('#map').ffcsMap.onClickGetPoint(x, y, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', pointGetCallBack, isEdit);

        $("#map").ffcsMap.centerAt({
            x: parseFloat(x),          //中心点X坐标
            y: parseFloat(y),           //中心点y坐标
            zoom: mapCenterLevel
        });
    }
    
    function changeCenterPoint(showX, showY) {//修改定位中心店
        $('#x').val(showX);
        $('#y').val(showY);
        var mapCenterLevel = $('#mapCenterLevel').val();
        $('#map').ffcsMap.showCenterPoint(showX, showY, '${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png');
        $("#map").ffcsMap.centerAt({
            x: parseFloat(showX),          //中心点X坐标
            y: parseFloat(showY),           //中心点y坐标
            zoom: mapCenterLevel
        });
    }

    function pointGetCallBack(data) {
        var xys = data.toString().split(',');
        var x = xys[0];
        var y = xys[1];
        $('#x').val(x);
        $('#y').val(y);
    }

    function endPointLocate() {
        var url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?' + data;
        var x = $('#x').val();
        var y = $('#y').val();
        var mapt = $('#mapt').val();
        var data = 'x=' + x + '&y=' + y + '&mapt=' + mapt;

        var address = window.parent.document.getElementById('addressName').value;
        if (address) {
            data = data + '&address=' + address;
        }
        var targetDownDivId = '${(targetDownDivId)!}';
        if (targetDownDivId) {
            data = data + '&targetDownDivId=' + targetDownDivId;
        }

        if (callBackUrl) {
            url = callBackUrl + '?' + data;
        }

        window.parent.document.getElementById('cross_domain_frame').src = url;
    }

    function getArcgisOfGridDatas() {//加载轮廓
        var gridId = document.getElementById('gridId').value;
        var mapt = currentArcgisConfigInfo.mapType;
        var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridForPoint.json?mapt=' + mapt + '&wid=' + gridId;
        $('#map').ffcsMap.render('gridLayer', url, 2, true,'','','','','','','','',null,'',callBackGrid);
    }
    function callBackGrid(datas){
        if(datas){
            gridx=datas.x;
            gridy=datas.y;
            gridLevel=datas.mapCenterLevel;
        }
    }

    function callBacktuban(datas){
        if(datas){
            cenx=datas.x;
            ceny=datas.y;
            level=datas.mapCenterLevel;
        }
    }

    function getArcgisOftwoVioPreDatas() {//加载两违轮廓
        var wgGisType = $('#wgGisType').val();
        var wgGisId = $('#wgGisId').val();
        var mapt = currentArcgisConfigInfo.mapType;
        var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisCommonHs.json?mapt=' + mapt + '&wid=' + wgGisId+'&type='+wgGisType;
        $('#map').ffcsMap.render('twoVioPreLayer', url, 2, true,'','','','','','','','',null,'',callBacktuban);
    }

</script>
</html>
