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
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/public.css" rel="stylesheet" type="text/css" />
    <link href="${uiDomain!''}/images/map/gisv0/special_config/css/map.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${uiDomain!''}/css${styleCSS!''}/normal.css" />

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

        });

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

        function getTrajectoryDatasByCarIds(){
            var carIds = document.getElementById("carId").value;
            var startTime = document.getElementById("start").value;
            var endTime = document.getElementById("end").value;
            if(carIds == ''){
                carIds = '1,2,3';
            }
            var url = "${rc.getContextPath()}/zhsq/map/buildBindController/getTrajectoryDatasByCarIds.json?";
            var data = "&mapType=30&carIds="+carIds +"&startTime="+startTime+"&endTime="+endTime;
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                dataType:"json",
                success: function(data){
                    if(typeof trajectoryLayer != 'undefined' && trajectoryLayer != null){
                        trajectoryLayer.removeAll();
                    }
                    if(typeof data != 'undefined' && data != null) {
                        addLayerOnMap(data);
                    }else{
                        alert("该车在该时间段内无轨迹信息！");
                    }
                }
            });
        }

        function addLayerOnMap(data){
            for(var key in data){
                IMGLayer1 = new p_GraphicsLayer({id:"IMGLayer1"});
                var lineArray = data[key];
                var line=[];
                for(var i=0;i<lineArray.length;i++){
                    line.push([Number(lineArray[i].x),Number(lineArray[i].y),Number(lineArray[i].z)]);
                }
                var polyline1 = new p_Polyline(line);
                var lineSymbol = new p_SimpleLineSymbol({
                    color: [226, 119, 40],
                    width: 2
                });
                var polylineGraphic = new p_Graphic({
                    geometry: polyline1,
                    symbol: lineSymbol
                });

                trajectoryLayer.add(polylineGraphic);
                map.add(trajectoryLayer);
            }
            //map.add(trajectoryLayer);
        }
    </script>
</head>

<body class="easyui-layout">
<div class="MainContent">
    <div id="jqueryToolbar" class="MainContent">
        <div class="ConSearch">
            <div class="btns">
                <ul>
                    <li><a href="#" class="PreviewBtn" title="开始轨迹播放" onclick="getTrajectoryDatasByCarIds()">开始</a></li>
                    <li><a href="#" class="CancelBtn" title="结束轨迹播放" onclick="stopTrack()">结束</a></li>
                </ul>
            </div>‍
            <div class="fl">
                <ul>
                    <li>选择车辆:</li>
                    <li>
                        <select id="carId" name="carId" class="sel1" style="width: 100px">
                            <option value="">所有车辆</option>
                            <option value="1">渣土车1</option>
                            <option value="2">渣土车2</option>
                            <option value="3">渣土车3</option>
                        </select>
                    </li>
                    <li>时间：</li>
                    <li><input type="text" id="start" onclick="WdatePicker({el:'start',startDate:'${startTime}',maxDate:'#F{$dp.$D(\'end\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:150px; height:26px; line-height:24px;" value="${startTime}"/></li>
                    <li>—</li>
                    <li><input type="text" id="end" onclick="WdatePicker({el:'end',startDate:'${endTime}',minDate:'#F{$dp.$D(\'start\')}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true})" class="Wdate inp1" style="width:150px; height:26px; line-height:24px;" value="${endTime}"/></li>
                </ul>
            </div>
            <div class="btns">
                <ul>
                    <li><a href="#" class="chaxun" title="查询按钮" onclick="getTrajectoryDatasByCarIds()">查询</a></li>
                    <li><a href="#" class="chongzhi" title="重置查询条件" onclick="stopTrack()">重置</a></li>
                </ul>
            </div>‍
        </div>
    </div>
</div>
<div id="viewDiv"></div>
</body>

</html>