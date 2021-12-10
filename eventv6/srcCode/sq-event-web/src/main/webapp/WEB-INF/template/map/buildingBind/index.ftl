<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no">
    <title>地图首页</title>

    <style>
        html,
        body,
        #viewDiv {
            padding: 0;
            margin: 0;
            height: 100%;
        }

        .PreviewBtn,.CancelBtn {
            display:block;
            width:55px;
            height:28px;
            padding-left:20px;
            font-family:Microsoft YaHei;
            font-size:16px;
            line-height:26px;
            color:#fff;
            text-align:center;
            border-radius:3px;
            background-repeat:no-repeat;
            background-position:10px 5px;
            transition:all 0.2s;
        }
        .PreviewBtn:hover,.CancelBtn:hover {
            color:#fff;
            text-decoration:none;
        }
        .PreviewBtn {
            margin-right:9px;
            background-color: #80c269;
        }
        .PreviewBtn:hover {
            background-color:#ffb23c;
        }
        .CancelBtn {
            background-color:#1fb997;
        }
        .CancelBtn:hover {
            background-color:#1bb08f;
        }

        .ConSearch .fl ul{
            margin-left: 10px;
        }
    </style>
    <#include "/map/arcgis/arcgis_base/4.4/arcgis_3d_common.ftl" />
    <script src="${SQ_ZHSQ_EVENT_URL!''}/js/map/arcgis/library/4.4/geometry.js"></script>
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/normal.css" />

    <script src="${SQ_ZHSQ_EVENT_URL!''}/js/map/arcgis/library/4.4/geometry.js"></script>

    <script>
        var map, p_Point, view, p_Polyline, polyline, p_GraphicsLayer, p_Graphic, p_SimpleLineSymbol, p_Camera,
                p_PictureMarkerSymbol, p_TextSymbol;
        var trajectoryLayer,IMGLayer1, IMGLayer4;
        var linePoints;
        var layerName = "trajectoryLayer";
        var currentCoordIndex = 0;
        var highlight = null;
        var popupFlag = false;

        require([
            "esri/widgets/Track",
            "esri/views/SceneView",
            "esri/layers/SceneLayer",
            "esri/Map",
            "esri/geometry/Point",
            "esri/Graphic",
            "esri/geometry/Polyline",
            "esri/layers/GraphicsLayer",
            "esri/symbols/SimpleLineSymbol",
            "esri/Camera",
            "esri/symbols/PictureMarkerSymbol",
            "esri/symbols/TextSymbol",
            "dojo/domReady!"
        ], function (Track,
                     SceneView,
                     SceneLayer,
                     Map,
                     Point,
                     Graphic,
                     Polyline,
                     GraphicsLayer,
                     SimpleLineSymbol,
                     Camera,
                     PictureMarkerSymbol,
                     TextSymbol) {
            p_Point = Point;
            p_Polyline = Polyline;
            p_GraphicsLayer = GraphicsLayer;
            p_Graphic = Graphic;
            p_SimpleLineSymbol = SimpleLineSymbol;
            p_Camera = Camera;
            p_PictureMarkerSymbol = PictureMarkerSymbol;
            p_TextSymbol = TextSymbol;
            map = new Map({
                basemap: "satellite",
                opacity:0
            });

//            var sceneLayer = new SceneLayer({
//                url: "https://27.155.101.41:6443/arcgis/rest/services/Hosted/hu277/SceneServer"
//            });
//            map.add(sceneLayer);
            view = new SceneView({
                map: map,
                container: "viewDiv",
                opacity:0,
                camera: {
                    position: [
//                        116.66850029839421,
//                        26.63551030375047,
//                        250  // elevation in meters
                        112.5654505349728,
                        0.4442245774139906,
                        100
                    ],
                    tilt: 65,
                    heading: 50
                }
            });
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp020407/SceneServer"

            });
            map.add(sceneLayer);

//            var sceneLayer = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//            });
//            map.add(sceneLayer);
//            var sceneLayer1 = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y20801/SceneServer"
//            });
//            map.add(sceneLayer1);
//            var sceneLayer2 = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp208dk/SceneServer"
//            });
//            map.add(sceneLayer2);
//            var sceneLayer3 = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//            });
//            map.add(sceneLayer3);
//            var sceneLayer4 = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp209/SceneServer"
//            });
//            map.add(sceneLayer4);
//            var sceneLayer5 = new SceneLayer({
//                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp212/SceneServer"
//            });
//            map.add(sceneLayer5);



            //一、根据点击的点高亮显示
            view.whenLayerView(sceneLayer).then(function(lyrView) {
                view.on("click", function(event) {
                    //关闭地图本来的弹出框
                    event.stopPropagation();

                    //获取点击的点
                    var screenPoint = {
                        x: event.x,
                        y: event.y
                    };

                    //根据点查询事物
                    view.hitTest(screenPoint).then(function(response) {
                        var result = response.results[0];

                        if (result) {
                            var graphic = response.results[0].graphic;

                            view.whenLayerView(graphic.layer).then(function(lyrView){
                                var objectId = graphic.attributes.oid;
                                if(typeof objectId != 'undefined' && objectId != null && objectId != ""){
                                    showBuildingInfo(objectId, event.mapPoint, lyrView, graphic);//获取楼宇信息并展示
                                }
                            });
                        }
                    });
                });
            });

            trajectoryLayer = new p_GraphicsLayer({id:layerName});
            IMGLayer1 = new p_GraphicsLayer({id:"IMGLayer1"});
            IMGLayer4 = new p_GraphicsLayer({id:"IMGLayer4"});

            polyline = new Polyline();




            map.add(trajectoryLayer);
            map.add(IMGLayer1);
            map.add(IMGLayer4);

        });


        var timer1,timer4;
        var currentPoint1,currentPoint4;
        var prevLocation1,prevLocation4;
        /**
         * 开始播放轨迹
         */
        function goTrack(){
            var imageurl="${SQ_ZHSQ_EVENT_URL!''}/images/map/3d/car.png";
            var IMGsymbol = new p_PictureMarkerSymbol(imageurl, 30, 30);
            var carId = document.getElementById("carId").value;

            if(carId == "1"){
                if(typeof IMGLayer4 != 'undefined' && IMGLayer4 != null){
                    IMGLayer4.removeAll();
                }
                var i=0;
                prevLocation1 = new p_Point(lineCoordinate1[0].x, lineCoordinate1[0].y);
                currentPoint1 = new p_Point(lineCoordinate1[i].x, lineCoordinate1[i].y);
                timer1 = setInterval(function(){
                    if( i<lineCoordinate1.length) {

                        if(typeof IMGLayer1 != 'undefined' && IMGLayer1 != null){
                            IMGLayer1.removeAll();
                        }

                        currentPoint1 = new p_Point(lineCoordinate1[i].x, lineCoordinate1[i].y);
                        var IMGpoint = new p_Point(lineCoordinate1[i].x, lineCoordinate1[i].y, lineCoordinate1[i].z);
                        var IMGgraphic = new p_Graphic(IMGpoint, IMGsymbol);
                        IMGLayer1.add(IMGgraphic);
                        //gotoCar();

                        prevLocation1 = currentPoint1.clone();
                        i++;
                    }else{
                        stopTrack(timer1);
                    }
                },1500);
            }

            if(carId == "2"){
                if(typeof IMGLayer1 != 'undefined' && IMGLayer1 != null){
                    IMGLayer1.removeAll();
                }
                var j=0;
                prevLocation4 = new p_Point(lineCoordinate4[0].x, lineCoordinate4[0].y);
                currentPoint4 = new p_Point(lineCoordinate4[0].x, lineCoordinate4[0].y);
                timer4 = setInterval(function(){
                    if( j<lineCoordinate4.length) {

                        if(typeof IMGLayer4 != 'undefined' && IMGLayer4 != null){
                            IMGLayer4.removeAll();
                        }

                        currentPoint4 = new p_Point(lineCoordinate4[j].x, lineCoordinate4[j].y);

                        var IMGpoint = new p_Point(lineCoordinate4[j].x, lineCoordinate4[j].y, lineCoordinate4[j].z);
                        var IMGgraphic = new p_Graphic(IMGpoint, IMGsymbol);
                        IMGLayer4.add(IMGgraphic);
                        //gotoCar();

                        prevLocation4 = currentPoint4.clone();
                        j++;
                    }else{
                        stopTrack(timer4);
                    }
                },1500);
            }

        }

        function getHeading(point, oldPoint) {
            // get angle between two points
            var angleInDegrees = Math.atan2(point.y - oldPoint.y, point.x -
                    oldPoint.x) * 180 /
                    Math.PI;

            // move heading north
            return -90 + angleInDegrees;
        }

        /**
         * 停止播放轨迹
         * @param timer
         */
        function stopTrack(timer){
            if(typeof timer != 'undefined' && timer != null){
                window.clearInterval(timer);
            }else{
                window.clearInterval(timer1);
                window.clearInterval(timer4);
                var cam = new p_Camera({
                    position: [
                        116.66850029839421,
                        26.63551030375047,
                        250  // elevation in meters
                    ],
                    tilt: 65,
                    heading: 50
                });
                view.goTo(cam);
            }
        }

        /**
         * 跟踪某辆车
         */
        function gotoCar(){
            var carId = document.getElementById("carId").value;
            if(carId != ""){

                var currentPoint,prevLocation;
                if(carId == "car1"){
                    currentPoint = currentPoint1;
                    prevLocation = prevLocation1;
                }else if(carId == "car2"){
                    currentPoint = currentPoint4;
                    prevLocation = prevLocation4;
                }
                view.goTo({
                    center: currentPoint,
                    tilt: 50,
                    scale: 800,
                    heading: 360 - getHeading(currentPoint, prevLocation),
                    speedFactor: 1.5,
                    duration: 1.5
                });
            }
        }

        /**
         * 显示按钮
         */
        function showBtnsDiv(){
            if(typeof trajectoryLayer != 'undefined' && trajectoryLayer != null){
                trajectoryLayer.removeAll();
            }

            if($("#carId").val() != ''){
                var index = $("#carId").val();
                var line,lineCoordinate;
                if(index == "1"){
                    line = line1;
                    lineCoordinate = lineCoordinate1;
                }else if(index == "2"){
                    line = line2;
                    lineCoordinate = lineCoordinate2;
                }
                var polyline1 = new p_Polyline(line),
                        lineSymbol = new p_SimpleLineSymbol({
                            color: [226, 119, 40],
                            width: 2
                        });
                var textSymbol1 =  new p_TextSymbol({
                    type: "text",
                    color: "red",
                    text: "规定线路",
                    xoffset: 3,
                    yoffset: 3,
                    font: {
                        size: 12,
                        family: "sans-serif",
                        weight: "bolder"
                    }
                });
                var point1 = new p_Point(lineCoordinate[1].x, lineCoordinate[1].y, 7);
                var textGraphic1 = new p_Graphic(point1, textSymbol1);

                var polylineGraphic1 = new p_Graphic({
                    geometry: polyline1,
                    symbol: lineSymbol
                });

                trajectoryLayer.add(polylineGraphic1);
                trajectoryLayer.add(textGraphic1);


                $("#btnsDiv").show();
            }else{
                $("#btnsDiv").hide();
            }

        }

        /**
         * 查询楼宇信息
         * @param objectId
         * @param mapPoint
         * @param lyrView
         * @param highlightGraphic
         */
        function showBuildingInfo(objectId, mapPoint, lyrView, highlightGraphic){
            var url = "${rc.getContextPath()}/zhsq/map/buildBindController/getBuildingBindInfo.json?";
            var data = "&mapType=30&oid="+objectId ;
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                dataType:"json",
                success: function(data){
                    if(data.buildingInfo != null){
                        var buildingInfo = data.buildingInfo;
                        if(buildingInfo.buildingId != null){
                            if(highlight){
                                highlight.remove();
                            }
                            highlight = lyrView.highlight(highlightGraphic);
                            popupFlag = true;
                            view.popup.open({
                                location: mapPoint,  // location of the click on the view
                                visible: true,
                                actions:[],
                                title: "楼宇信息",  // title displayed in the popup
                                content: "楼宇名称：" + buildingInfo.buildingName + "<br>" +
                                "楼宇地址：" + buildingInfo.buildingAddress// content displayed in the popup
                            });
                        }else{
                            popupFlag = false;
                            if(!popupFlag){
                                if (highlight) {
                                    highlight.remove();
                                    view.popup.close();
                                }
                            }
                        }
                    }else{
                        popupFlag = false;
                        if(!popupFlag){
                            if (highlight) {
                                highlight.remove();
                                view.popup.close();
                            }
                        }
                    }
                }
            });
        }
    </script>
</head>

<body class="easyui-layout">
<div class="MainContent">
    <div id="jqueryToolbar" class="MainContent">
        <div class="ConSearch">
            <div class="fl">
                <ul>
                    <li>选择车辆:</li>
                    <li>
                        <select id="carId" name="carId" class="sel1" style="width: 100px" onchange="showBtnsDiv()">
                            <option value="">请选择车辆</option>
                            <option value="1">渣土车1</option>
                            <option value="2">渣土车2</option>
                        </select>
                    </li>
                </ul>
            </div>
            <div class="btns" id="btnsDiv" style="display: none">
                <ul>
                    <li><a href="#" class="PreviewBtn" title="开始轨迹播放" onclick="goTrack()">开始</a></li>
                    <li><a href="#" class="CancelBtn" title="结束轨迹播放" onclick="stopTrack()">结束</a></li>
                </ul>
            </div>‍
        </div>
    </div>
</div>
<div id="viewDiv"></div>
</body>

</html>