/**
 * Created by sulch on 2018/5/10.
 */

var drawPointFlag = true;//是否编辑中心点操作标识
var blindBuildingFlag = true;//是否绑定轮廓操作标识
var showGridLayerFlag = false; //是否显示所在网格的轮廓标识
//var drawBuildingHSFlag = true;//绘制楼宇轮廓标识

var saveCenterPointFlag = false; //是否保存中心点坐标
//var saveBuildingHSFlag = false   //是否保存楼宇轮廓信息


/**
 * 判断是否有轮廓信息
 */
function hasBuildingArcgisData(bizId) {
    var url = "";
    var param = "?wid="+bizId+"&mapt="+currentArcgisConfigInfo.mapType;
    url = js_ctx + "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";

    url = url+param;
    $.ajax({
        url: url,
        type: 'POST',
        timeout: 3000,
        dataType:"json",
        async: false,
        error: function(data){
            alert('网格地图信息获取出现异常，请联系维护人员处理！');
        },
        success: function(data){
            $("#building_tipMessage").html("");
            var tipMess = "";
            if(data != null && typeof(data.list) != 'undefined' && data.list.length==0 && wid != "" && wid != null && typeof(wid) != undefined) {
                tipMess = "未标注中心点。";
                window.document.getElementById("building_tipMessage").innerHTML = tipMess;
                $("#buildingMapBar").show();
                $("#building_drawPoint").show();
            }else{
                if(data == null || typeof(data.list) == 'undefined' || data.list[0].x == "" || data.list[0].y == ""){
                    tipMess = "未标注中心点。";
                    window.document.getElementById("building_tipMessage").innerHTML = tipMess;
                    $("#buildingMapBar").show();
                    $("#building_drawPoint").show();
                }
            }
        }
    });
}

/**
 * 楼宇图层右键菜单
 * @param layerName
 */
function layerAddBuildingMenu(layerName){
    selected = null;
    var map = $("#map"+currentN).ffcsMap.getMap();
    if(typeof layerName == "undefined" || layerName == null || layerName == "" || layerName == "buildingLayer"){
        layerName = "buildingLayer";
    }

    var layer = map.getLayer(layerName);

    //创建右键菜单
    ctxMenuForGraphics = new dojo.dijit.Menu({});

    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "编辑楼宇信息",
        class: "menuItemStyle",
        onClick: function() {
            var data = getSelectedData(selected);
            var url = ZZGRID_URL + '/zzgl/grid/3509/areaBuildingInfo/edit.jhtml?buildingId='+data.wid+"&isCrossDomain=true";
            var mapDataDetailWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", url, {title: "编辑楼栋信息", width:1000, height:500, scrolling: "no"})[0];
            parent.addMsgServer(mapDataDetailWin);
        }
    }));

    ctxMenuForGraphics.addChild(new dijit.MenuSeparator());//分割线
    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "删除楼宇信息",
        class: "menuItemStyle",
        onClick: function() {
            var data = getSelectedData(selected);
            $.messager.confirm('提示', '您确定删除选中的楼栋信息吗?', function(r) {
                if (r){
                    $.ajax({
                        type: "POST",
                        url: js_ctx + '/zhsq/map/mapDataMaintain/delBuilding.jhtml',
                        data: 'idStr='+data.wid,
                        dataType:"json",
                        success: function(data){
                            if(data.result==0){
                                $.messager.alert('提示','该楼宇存在房屋信息，请删除房屋!','warning');
                            }else{
                                parent.DivShow('操作成功!');

                                parent.DataIframe.tabChange("building");
                            }
                        },
                        error:function(data){
                            $.messager.alert('错误','连接超时！','error');
                        }
                    });
                }
            });

        }
    }));


    ctxMenuForGraphics.startup();

    layer.on("mouse-over", function(evt) {
        // We'll use this "selected" graphic to enable editing tools
        // on this graphic when the user click on one of the tools
        // listed in the menu.
        selected = evt.graphic;


        // Let's bind to the graphic underneath the mouse cursor
        ctxMenuForGraphics.bindDomNode(evt.graphic.getDojoShape().getNode());
    });

    layer.on("mouse-out", function(evt) {
        ctxMenuForGraphics.unBindDomNode(evt.graphic.getDojoShape().getNode());
    });

    //启动右键菜单
    ctxMenuForGraphics.startup();
}

function getSelectedData(selected){
    var data;
    if(typeof selected.attributes != 'undeined' && selected.attributes != null){
        data = selected.attributes.data["_oldData"];
    }else{
        data = selected.geometry;
    }
    return data;
}


/**
 * 选择功能（绑定网格、标注中心点）
 */
function chooseBuildingEditTool(type){
    if(type != null && type != ''){
        if(type == "drawPoint"){
            $('#building_saveButton').show();
            drawPointFlag = true;
            blindBuildingFlag = false;
            $('#building_drawPoint').hide();
            //drawBuildingHSFlag = false;
            //$('#drawBuilding').hide();
            $('#building_cancleDrawPoint').show();
            drawCenterPoint();
            stopMapMenu();
        }
        //if(type == 'drawBuilding'){
        //    $('#saveButton').show();
        //    //$('#drawPoint').hide();
        //    //$('#drawBuilding').hide();
        //    $('#cancleDrawOrEditBuilding').show();
        //    drawPanel();
        //}
        if(type == "cancleDrawPoint"){
            //$('#drawBuilding').show();
            $('#building_drawPoint').show();
            $('#building_saveButton').hide();
            $('#building_cancleDrawPoint').hide();
            drawPointFlag = false;
            //结束编辑并重新定位
            endEditMapData();
            startMapMenu();
        }
        //if(type == "cancleDrawOrEditBuilding"){
        //    //$('#drawBuilding').show();
        //    //$('#drawPoint').show();
        //    $('#saveButton').hide();
        //    $('#cancleDrawPoint').hide();
        //    $('#cancleDrawOrEditBuilding').hide();
        //    //结束编辑并重新定位
        //    endEditMapData();
        //}
        //if(type == "delDrawData"){
        //    //$('#drawBuilding').show();
        //    //$('#drawPoint').show();
        //    $('#building_saveButton').hide();
        //    $('#building_cancleDrawPoint').hide();
        //    $('#building_cancleDrawOrEditBuilding').hide();
        //    delBuildingHSData();
        //
        //}
    }
}

///**
// * 解除绘制楼宇轮廓状态
// */
//function removeDrawBuildingTool(){
//    map.removeInteraction(drawBuilding);
//    drawBuilding = "";
//}


/**
 * 保存绘制的数据
 */
function saveBuildingData(){
    var buildingId = $('#wid').val();
    var hs = $("#building_hs").val();
    var x = $("#building_x").val();
    var y = $("#building_y").val();

    //drawBuildingHSFlag = false;
    //blindBuildingFlag = true;

    $("#building_tipMessage").html("");
    $('#building_saveButton').hide();
    var data = 'wid='+buildingId+'&x='+x+'&y='+y+'&hs='+hs+'&mapt='+currentArcgisConfigInfo.mapType;
    var url = js_ctx + '/zhsq/map/mapDataMaintain/updateMapDataOfBuilding.json?'+data;

    $.ajax({
        url: url,
        type: 'POST',
        dataType:"json",
        async: false,
        error: function(data){
            $.messager.alert('提示','操作报错!','warning');
        },
        success: function(data){
            //$('#drawPoint').show();
            $('#building_cancleDrawPoint').hide();
            //$('#drawBuilding').show();
            $('#cancleDrawOrEditBuilding').hide();

            if(data.flag == true) {
                parent.DivShow('操作成功!');
            }else {
                parent.DivShow('操作失败!');
            }
            $("#building_hs").val("");
            endEditMapData();
            $("#buildingMapBar").hide();
        }
    });
    if(drawPointFlag == true && blindBuildingFlag == false){
        drawPointFlag = false;
    } else {
        drawPointFlag = true;
    }

}

/**
 * 显示单个轮廓
 * @param buildingId
 */
function getbuildingMapDataById(buildingId){
    var url = js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingId='+buildingId;
    $("#map"+currentN).ffcsMap.render('buildLayer',url,2,true,null,null,null,OUTLINE_FONT_SETTINGS_BUILD,null,null,true);
}


/**
 * 显示轮廓(列表加载的时候)
 * @param buildingIds 楼宇id串，逗号分隔
 */
function getBuildingMapDatasByIds(buildingIds){
    $("#map"+currentN).ffcsMap.clear({layerName : "buildLayer"});
    var url =  js_ctx +'/zhsq/map/arcgis/arcgisdata/getArcgisDatasOfBuilds.json?mapt='+currentArcgisConfigInfo.mapType+'&buildingIds='+buildingIds;
    $("#map"+currentN).ffcsMap.render('buildLayer',url,1,true);
    $("#map"+currentN).ffcsMap.ffcsDisplayhot({w:370,h:280},"buildLayer",'楼栋名片',getInfoDetailOnMap,showBuildDetail);
}

/**
 *中心点函数
 */
function drawCenterPoint() {
    var x = $("#building_x").val();
    var y = $("#building_y").val();
    $("#map"+currentN).ffcsMap.showCenterPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', pointCallBack,true);
}

/**
 * 编辑轮廓
 * @param x
 * @param y
 * @param hs
 */
function drawPanel(){
    var x = $("#building_x").val();
    var y = $("#building_y").val();
    var hs = $("#building_hs").val();

    if(hs==null || hs == "" || hs.split(",").length<4) {
        $("#map"+currentN).ffcsMap.draw("POLYGON",boundaryCallBack);
    }else {
        $("#map"+currentN).ffcsMap.toolbarDeactivate();
        $("#map"+currentN).ffcsMap.hideCenterPointImg(x, y);
        $("#map"+currentN).ffcsMap.edit(true,boundaryCallBack,false);
    }
    $("#map"+currentN).ffcsMap.onClickGetPointEnd();
}

/**
 * 编辑轮廓后回调
 * @param data
 */
function boundaryCallBack(data) {
    var data1 = JSON.parse(data);
    var hs = data1.coordinates.toString();
    var xys = hs.split(",");
    var x = xys[0];
    var y = xys[1];
    var mapt = currentArcgisConfigInfo.mapType;
    $("#building_mapt").val(mapt);
    if($("#building_x").val() == "" || $("#building_y").val() == "") {
        $("#building_x").val(x);
        $("#building_y").val(y);
    }
    if(xys.length >=4) {
        $("#building_hs").val(hs);
    }
}

/**
 * 编辑中心点
 * @param callBack
 * @param x
 * @param y
 */
function drawPoint(callBack,x,y){
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', pointCallBack,true);
}

/**
 * 中心点编辑回调
 * @param data
 */
function pointCallBack(data) {
    var xys = data.toString().split(",");
    var x = xys[0];
    var y = xys[1];
    $('#building_x').val(x);
    $('#building_y').val(y);
    var mapt = currentArcgisConfigInfo.mapType;
    $("#building_mapt").val(mapt);
}

/**
 * 删除轮廓中心点 数据
 * @returns {boolean}
 */
function delBuildingHSData() {
    $('#building_saveButton').hide();
    $("#building_tipMessage").html("");
    var buildingId = $('#wid').val();
    var url = js_ctx + "/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
    var data = "targetType=build&wid="+ buildingId +"&mapt=5";
    if(!confirm('确定要删除?')){
        return false;
    }

    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType:"json",
        success: function(data){
            parent.DivShow('删除成功！');
            $('#building_delDrawData').hide();
            endEditMapData();
        },
        error:function(msg){
            parent.DivShow('删除失败！');
        }
    });

}

/**
 * 编辑完毕后需要关闭地图编辑工具并重新加载地图业务数据和轮廓
 */
function endEditMapData(){
    $("#map").ffcsMap.toolbarDeactivate();
    $("#map").ffcsMap.onClickGetPointEnd();
    $("#map").ffcsMap.changeIsDrawEdit();
    $("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'});
    $("#map"+currentN).ffcsMap.clear({layerName : 'buildLayer'});
    $("#map"+currentN).ffcsMap.clear({layerName : 'buildingLayer'});
    $("#map"+currentN).ffcsMap.clear({layerName : 'select_cpoint_ly'});

    reGisPositionByType("building");
}


/**
 * 判断是否存在
 * @param address
 */
function judgeBuildingAddressIsExits(address){
    var isOK = true;

    if(typeof(address) != 'undefined' && address != null && address != ''){
        $.ajax({
            type: "POST",
            async: false,
            url: js_ctx + '/zhsq/map/mapDataMaintain/checkBuildingAddress.json?buildingAddress='+address,
            //url: ZZGRID_URL + '/zzgl/grid/areaBuildingInfo/checkBuildingAddress.json?buildingAddress='+address,
            dataType:"json",
            success: function(data){
                if (data && data["checkResult"] == false) {
                    $.messager.alert('提示', '已存在该地址的楼宇！', 'info');
                    isOK = false;
                }
            },
            error:function(data){
                $.messager.alert('错误','连接超时！','error');
            }
        });
    }
    return isOK;
}


//辅助显示
function buildingDisplayChange(){
    var mapStyleDiv = document.getElementById("buildingDisplayDiv");
    if(mapStyleDiv.style.display == 'none') {
        mapStyleDiv.style.display = 'block';
    }else {
        mapStyleDiv.style.display = 'none';
    }
}


function getArcgisDataOfGridsByLevel(level){
    if(document.getElementById("displayLevel"+level).checked == true){
        var url = js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0&gridId='+parent.document.getElementById("gridId").value;
        $("#map"+currentN).ffcsMap.render('gridLayer'+level,url,2,true);
    }else{
        $("#map"+currentN).ffcsMap.clear({layerName : 'gridLayer'+level});
    }
}



///**
// * 添加消息发送监听
// */
//function addMsgServer(iframeObj){
//    gmMsgClient.addObserver(iframeObj, receiveCloseWindowMsg, "closeWindow");
//}
//
///**
// * 消息回调
// */
//function receiveCloseWindowMsg(msgData){
//    if(typeof msgData != "undefined" && msgData != null && msgData != "" && JSON.stringify(msgData)){
//        closeMaxJqueryWindow();
//        $.messager.alert('提示', msgData.result, 'info');
//
//    }
//}