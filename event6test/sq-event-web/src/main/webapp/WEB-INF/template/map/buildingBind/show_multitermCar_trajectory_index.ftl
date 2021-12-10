<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no">
    <title>轨迹播放(多条条轨迹)</title>

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
        var trajectoryLayer,IMGLayer1, IMGLayer2, IMGLayer3;
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
//                        116.66850029839421,
//                        26.63551030375047,
//                        250  // elevation in meters
                        112.5654505349728,
                        0.4442245774139906,
                        100
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
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//            });
//            map.add(sceneLayer);
//            var sceneLayer1 = new SceneLayer({
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y20801/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/y20801/SceneServer"
//            });
//            map.add(sceneLayer1);
//            var sceneLayer2 = new SceneLayer({
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp208dk/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp208dk/SceneServer"
//            });
//            map.add(sceneLayer2);
//            var sceneLayer3 = new SceneLayer({
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/y208/SceneServer"
//            });
//            map.add(sceneLayer3);
//            var sceneLayer4 = new SceneLayer({
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp209/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp209/SceneServer"
//            });
//            map.add(sceneLayer4);
//            var sceneLayer5 = new SceneLayer({
////                url: "http://27.155.101.41:6080/arcgis/rest/services/Hosted/yp212/SceneServer"
//                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp212/SceneServer"
//            });
//            map.add(sceneLayer5);


            trajectoryLayer = new p_GraphicsLayer({id:layerName});
            IMGLayer1 = new p_GraphicsLayer({id:"IMGLayer1"});
            IMGLayer2 = new p_GraphicsLayer({id:"IMGLayer2"});
            IMGLayer3 = new p_GraphicsLayer({id:"IMGLayer3"});

            polyline = new Polyline();


            var polyline1 = new p_Polyline(line1),
                    polyline2 = new p_Polyline(line2),
                    polyline3 = new p_Polyline(line3),

                    lineSymbol = new p_SimpleLineSymbol({
                        color: [226, 119, 40],
                        width: 2
                    });
            var textSymbol1 =  new TextSymbol({
                type: "text",
                color: "red",
                text: "线路1",
                xoffset: 3,
                yoffset: 3,
                font: {
                    size: 12,
                    family: "sans-serif",
                    weight: "bolder"
                }
            });
            var point1 = new Point(lineCoordinate1[1].x, lineCoordinate1[1].y, lineCoordinate1[1].z);
            var textGraphic1 = new Graphic(point1, textSymbol1);

            var textSymbol2 =  new TextSymbol({
                type: "text",
                color: "red",
                text: "线路2",
                xoffset: 3,
                yoffset: 3,
                font: {
                    size: 12,
                    family: "sans-serif",
                    weight: "bolder"
                }
            });
            var point2 = new Point(lineCoordinate2[1].x, lineCoordinate2[1].y, lineCoordinate2[1].z);
            var textGraphic2 = new Graphic(point2, textSymbol2);



            var textSymbol3 =  new TextSymbol({
                type: "text",
                color: "red",
                text: "线路3",
                xoffset: 3,
                yoffset: 3,
                font: {
                    size: 12,
                    family: "sans-serif",
                    weight: "bolder"
                }
            });
            var point3 = new Point(lineCoordinate3[1].x, lineCoordinate3[1].y, lineCoordinate3[1].z);
            var textGraphic3 = new Graphic(point3, textSymbol3);

            var polylineGraphic1 = new p_Graphic({
                        geometry: polyline1,
                        symbol: lineSymbol
                    }),
                    polylineGraphic2 = new p_Graphic({
                        geometry: polyline2,
                        symbol: lineSymbol
                    }),
                    polylineGraphic3 = new p_Graphic({
                        geometry: polyline3,
                        symbol: lineSymbol
                    });

            trajectoryLayer.add(polylineGraphic1);
            trajectoryLayer.add(textGraphic1);
            trajectoryLayer.add(polylineGraphic2);
            trajectoryLayer.add(textGraphic2);
            trajectoryLayer.add(polylineGraphic3);
            trajectoryLayer.add(textGraphic3);

            map.add(trajectoryLayer);
            map.add(IMGLayer1);
            map.add(IMGLayer2);
            map.add(IMGLayer3);

        });

        var timer1,timer2,timer3;
        var currentPoint1,currentPoint2,currentPoint3;
        var prevLocation1,prevLocation2,prevLocation3;
        function goTrack(){
            var imageurl="${SQ_ZHSQ_EVENT_URL!''}/images/map/3d/car.png";
            var IMGsymbol = new p_PictureMarkerSymbol(imageurl, 30, 30);

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
                    gotoCar();

                    prevLocation1 = currentPoint1.clone();
                    i++;
                }else{
                    stopTrack(timer1);
                }
            },1500);


            var j=0;
            prevLocation2 = new p_Point(lineCoordinate2[0].x, lineCoordinate2[0].y);
            currentPoint2 = new p_Point(lineCoordinate2[0].x, lineCoordinate2[0].y);
            timer2 = setInterval(function(){
                if( j<lineCoordinate2.length) {

                    if(typeof IMGLayer2 != 'undefined' && IMGLayer2 != null){
                        IMGLayer2.removeAll();
                    }

                    currentPoint2 = new p_Point(lineCoordinate2[j].x, lineCoordinate2[j].y);

                    var IMGpoint = new p_Point(lineCoordinate2[j].x, lineCoordinate2[j].y, lineCoordinate2[j].z);
                    var IMGgraphic = new p_Graphic(IMGpoint, IMGsymbol);
                    IMGLayer2.add(IMGgraphic);
                    gotoCar();

                    prevLocation2 = currentPoint2.clone();
                    j++;
                }else{
                    stopTrack(timer2);
                }
            },1500);



            var k=0;
            prevLocation3 = new p_Point(lineCoordinate3[0].x, lineCoordinate3[0].y);
            currentPoint3 = new p_Point(lineCoordinate3[0].x, lineCoordinate3[0].y);
            timer3 = setInterval(function(){
                if( k<lineCoordinate3.length) {

                    if(typeof IMGLayer3 != 'undefined' && IMGLayer3 != null){
                        IMGLayer3.removeAll();
                    }

                    currentPoint3 = new p_Point(lineCoordinate3[k].x, lineCoordinate2[k].y);

                    var IMGpoint = new p_Point(lineCoordinate3[k].x, lineCoordinate3[k].y, lineCoordinate3[k].z);
                    var IMGgraphic = new p_Graphic(IMGpoint, IMGsymbol);
                    IMGLayer3.add(IMGgraphic);
                    gotoCar();

                    prevLocation3 = currentPoint3.clone();
                    k++;
                }else{
                    stopTrack(timer3);
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

        function stopTrack(timer){
            if(typeof timer != 'undefined' && timer != null){
                window.clearInterval(timer);
            }else{
                window.clearInterval(timer1);
                window.clearInterval(timer2);
                window.clearInterval(timer3);
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

        function gotoCar(){
            var selectCar = document.getElementById("selectCar").value;
            if(selectCar != ""){
                var currentPoint,prevLocation;
                if(selectCar == "car1"){
                    currentPoint = currentPoint1;
                    prevLocation = prevLocation1;
                }else if(selectCar == "car2"){
                    currentPoint = currentPoint2;
                    prevLocation = prevLocation2;
                }else if(selectCar == "car3"){
                    currentPoint = currentPoint3;
                    prevLocation = prevLocation3;
                }
                view.goTo({
                    easing: "linear",
                    center: currentPoint,
                    tilt: 50,
                    scale: 800,
                    heading: 360 - getHeading(currentPoint, prevLocation),
                    speedFactor: 1.5,
//                    duration: 1.5
                });
            }
        }
    </script>
</head>

<body>
<div>
    <button onclick="goTrack()" style="width: 150px;height: 30px;text-align: center">开始</button>
    <button onclick="stopTrack()" style="width: 150px;height: 30px;text-align: center">停止</button>
    选择车辆:
    <select id="selectCar" style="width: 150px;height: 30px;text-align: center">
        <option value=""></option>
        <option value="car1">渣土车1</option>
        <option value="car2">渣土车2</option>
        <option value="car3">渣土车3</option>
    </select>
    <!--<button onclick="gotoCar()" style="width: 150px;height: 30px;text-align: center">确定</button>-->
</div>
<div id="viewDiv"></div>
</body>

</html>