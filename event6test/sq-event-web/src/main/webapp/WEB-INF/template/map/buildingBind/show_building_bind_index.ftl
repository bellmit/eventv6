<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>点击地图楼宇显示楼宇信息</title>

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

    <script>

        var map, p_Point, view, p_Graphic, p_Camera;
        var sceneLayer;
        var highlight = null;
        var popupFlag = false;
        require([
                "esri/widgets/Track",
                "esri/views/SceneView",
                "esri/layers/SceneLayer",
                "esri/Map",
                "esri/geometry/Point",
                "esri/Graphic",
                "esri/Camera",
                "esri/tasks/support/Query",
                "esri/widgets/Popup",
                "dojo/domReady!"
            ], function (Track,
                         SceneView,
                         SceneLayer,
                         Map,
                         Point,
                         Graphic,
                         Camera,
                         Query,
                         Popup) {
            p_Point = Point;
            p_Graphic = Graphic;
            p_Camera = Camera;
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
                        112.5744505349728,
                        0.4442245774139906,
                        150
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
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/y240407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/shu0407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/y30/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp010407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp020407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp030407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp050407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp070407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp080407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp090407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp10010407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp100407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp150407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp200407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp250407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/dx030407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp110407/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp14/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp17/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp18/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp19/SceneServer"
            });
            map.add(sceneLayer);
            var sceneLayer = new SceneLayer({
                url: "http://59.56.74.205:6080/arcgis/rest/services/Hosted/yp12/SceneServer"
            });
            map.add(sceneLayer);



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
        });

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

<body>
<input type="hidden" id="buildingId">
<input type="hidden" id="currentOid">
<input type="hidden" id="buildingName">
<div id="viewDiv"></div>
</body>

</html>