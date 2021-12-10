<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no">
    <title>轨迹播放(一条轨迹)</title>

    <style>
        html,
        body,
        #viewDiv {
            padding: 0;
            margin: 0;
            height: 100%;
        }
    </style>
    <#include "/map/arcgis/arcgis_base/4.4/arcgis_3d_common.ftl" />
    <script src="${SQ_ZHSQ_EVENT_URL!''}/js/map/arcgis/library/4.4/geometry.js"></script>

    <script>
        var map, p_Point, view, p_Polyline, polyline, p_GraphicsLayer, p_Graphic, p_SimpleLineSymbol, p_Camera,
                p_PictureMarkerSymbol;
        var trajectoryLayer,IMGLayer;
        var linePoints;
        var layerName = "trajectoryLayer";
        var currentCoordIndex = 0;

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
                     PictureMarkerSymbol) {
            p_Point = Point;
            p_Polyline = Polyline;
            p_GraphicsLayer = GraphicsLayer;
            p_Graphic = Graphic;
            p_SimpleLineSymbol = SimpleLineSymbol;
            p_Camera = Camera;
            p_PictureMarkerSymbol = PictureMarkerSymbol;
            map = new Map({
                basemap: "satellite",
                opacity:0
            });

//            var sceneLayer = new SceneLayer({
//                url: "https://27.155.101.41:6443/arcgis/rest/services/Hosted/hu277/SceneServer"
//            });
//
//            map.add(sceneLayer);
            view = new SceneView({
                map: map,
                container: "viewDiv",
                opacity:0,
                camera: {
                    position: [
                        112.5654505349728,
                        0.4442245774139906,
                        100  // elevation in meters
                    ],
                    tilt: 65,
                    heading: 50
                },
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
            trajectoryLayer = new p_GraphicsLayer({id:layerName});
            IMGLayer = new p_GraphicsLayer({id:"IMGLayer"});



            var polyline = new p_Polyline(line1),
                lineSymbol = new p_SimpleLineSymbol({
                    color: [226, 119, 40],
                    width: 2
                });
            var polylineGraphic = new p_Graphic({
                geometry: polyline,
                symbol: lineSymbol
            });

            trajectoryLayer.add(polylineGraphic);
            map.add(trajectoryLayer);
            map.add(IMGLayer);


        });

        var timer;
        function goTrack(){
            var prevLocation = new p_Point(lineCoordinate1[0].x, lineCoordinate1[0].y);
            var imageurl="${SQ_ZHSQ_EVENT_URL!''}/images/map/3d/car.png";

            var i=0;
//          api的例子
//            view.goTo(shiftCamera(60),
//                // Animation options for a slow linear camera flight
//                {
//                    speedFactor: 0.1,
//                    easing: "linear"
//                }
//            );

            timer = setInterval(function(){
                if( i<lineCoordinate1.length) {

                    if(typeof IMGLayer != 'undefined' && IMGLayer != null){
                        IMGLayer.removeAll();
                    }

                    var currentPoint = new p_Point(lineCoordinate1[i].x, lineCoordinate1[i].y);

                    var IMGsymbol = new p_PictureMarkerSymbol(imageurl, 30, 30);
                    var IMGpoint = new p_Point(lineCoordinate1[i].x, lineCoordinate1[i].y, lineCoordinate1[i].z);
                    var IMGgraphic = new p_Graphic(IMGpoint, IMGsymbol);
                    IMGLayer.add(IMGgraphic);


                    view.goTo({
                        easing: "linear",//linear, in-cubic, out-cubic, in-out-cubic, in-expo, out-expo, in-out-expo
                        center: currentPoint,
                        tilt: 65,
                        scale: 100,
                        heading: 360 - getHeading(currentPoint, prevLocation),
                        speedFactor: 1.5//,//速度因子，很好理解，默认是1.
                        //duration: 1.5//持续时间，如果有这个，那么speedFactor就会被覆盖。
                        //easing:缓动方式。通常，easing必选，speedFactor和duration、maxDuration三选一。
                    });
                    //console.log("lineCoordinate1["+i+"].lng:"+lineCoordinate1[i].lng+";lineCoordinate1["+i+"].lat:"+lineCoordinate1[i].lat)
                    prevLocation = currentPoint.clone();
                    i++;
                }else{
                    alert("轨迹播放结束");
                    stopTrack();
                }
            },1500);
        }

        function getHeading(point, oldPoint) {
            // get angle between two points
            var angleInDegrees = Math.atan2(point.y - oldPoint.y, point.x -
                    oldPoint.x) * 180 /
                    Math.PI;

            // move heading north
            return -90 + angleInDegrees;
        }

        function stopTrack(){
            window.clearInterval(timer);
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
    </script>
</head>

<body>
<div>
    <button onclick="goTrack()" style="width: 150px;height: 30px;text-align: center">开始</button>
    <button onclick="stopTrack()" style="width: 150px;height: 30px;text-align: center">停止</button>
</div>
<div id="viewDiv"></div>
</body>

</html>