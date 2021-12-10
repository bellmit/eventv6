<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>楼宇绑定地图首页</title>

    <style>
        html,
        body,
        #viewDiv {
            padding: 0;
            margin: 0;
            height: 100%;
        }
    </style>
    <script>

    </script>
    <#include "/map/arcgis/arcgis_base/4.4/arcgis_3d_common.ftl" />

    <script type="application/javascript">

    </script>
    <script>

        var map, p_Point, view, p_Polyline, polyline, p_GraphicsLayer, p_Graphic, p_SimpleLineSymbol, p_Camera,
                p_PictureMarkerSymbol;
        var IMGLayer1, IMGLayer2, IMGLayer3;
        var sceneLayer;
        var highlight = null;
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

//            sceneLayer = new SceneLayer({
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
                highlightOptions: {
                    color: [0, 255, 255],
                    fillOpacity: 0.6
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
                            if(highlight){
                                highlight.remove();
                            }
                            highlight = lyrView.highlight(graphic);
                            if(typeof objectId != 'undefined' && objectId != null && objectId != ""){
                                bindingBuilding(objectId);//绑定楼宇
                            }

                        });
                    }
                });
            });
        });

        function bindingBuilding(objectId){
            var url = "${rc.getContextPath()}/zhsq/map/buildBindController/saveBuildingBindInfo.json?";
            var data = "buildingId=" + $("#buildingId").val()+ "&mapType=30&oid="+objectId ;
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                dataType:"json",
                success: function(data){
                    if(data.flag==true){
                        alert('绑定成功！');
                    }
                },
                error:function(msg){
                    alert('绑定失败！');
                }
            });
        }

        //点击楼宇列表选择楼宇，如果已经绑定了就高亮该楼宇
        function showBuilding(){
            var oid = document.getElementById("buildingId").value;
            //二、根据id查询高亮
            view.whenLayerView(sceneLayer).then(function(lyrView) {
                var query = sceneLayer.createQuery();
                query.outFields = ['SPACETYPE'];
                query.where = "oid = " + oid;
                sceneLayer.queryFeatures(query).then(function(result){
                    if (highlight) {
                        highlight.remove();
                    }
                    if(typeof result.features != 'undefined' && result.features != null && result.features.length>0){
                        highlight = lyrView.highlight(result.features);
                    }
                });
            });
        }
    </script>
</head>

<body>
<input type="hidden" id="buildingId">
<input type="hidden" id="currentOid">
<input type="hidden" id="buildingName">
<div id="viewDiv"></div>
</body>

</html>