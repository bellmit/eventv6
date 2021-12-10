/**
 * Created by sulch on 2018/5/10.
 */

var drawGridPointFlag = true;//是否编辑中心点操作标识
var blindGridFlag = true;//是否绑定轮廓操作标识
var showGridLayerFlag = false; //是否显示所在网格的轮廓标识
var drawGridHSFlag = true;//绘制楼宇轮廓标识

var saveCenterPointFlag = false; //是否保存中心点坐标
var saveGridHSFlag = false   //是否保存楼宇轮廓信息



/**
 * 判断是否有轮廓信息
 */
function hasGridArcgisData(bizId) {
    var url = "";
    var param = "?wid="+bizId+"&mapt="+currentArcgisConfigInfo.mapType;
    url = js_ctx + "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json";


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
            $("#grid_tipMessage").html("");
            var tipMess = "";
            if(data != null && typeof(data.list) != 'undefined' && data.list.length==0 && wid != "" && wid != null && typeof(wid) != undefined) {
                tipMess = "未标注中心点。未画轮廓！";
                window.document.getElementById("grid_tipMessage").innerHTML = tipMess;

                $("#drawGridPoint").show();
                $("#drawGrid").show();
                $("#delDrawGridData").hide();
            }else{
                if(data == null || typeof(data.list) == 'undefined' || data.list[0].x == "" || data.list[0].y == ""){
                    tipMess = "未标注中心点。";
                    window.document.getElementById("grid_tipMessage").innerHTML = tipMess;
                    $("#drawGridPoint").show();
                    $("#drawGrid").show();
                    $("#delDrawGridData").show();
                }
                var hs = new Array();
                if(data != null && typeof(data.list) != 'undefined' && data.list.length >0 && data.list[0].hs != null){
                    hs = data.list[0].hs.split(",");

                }
                if(typeof(hs) == 'undefined' || hs == null || hs.length<=2){
                    tipMess = "未画轮廓！";
                    $("#grid_tipMessage").append(tipMess);
                    $("#drawGridPoint").show();
                    $("#drawGrid").show();
                    $("#delDrawGridData").hide();
                }

            }
        }
    });
}

/**
 * 定位中心点
 * @param id
 * @param mapt
 */
function locateGridCenterAndLevel(id) {
    $.ajax({
        url: js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&wid='+id+'&targetType=grid',
        type: 'POST',
        timeout: 3000,
        dataType:"json",
        async: false,
        error: function(data){
            alert("系统无法获取中心点以及显示层级!");
        },
        success: function(data){
            var obj = data.result;
            if(obj != null) {
                $("#map"+currentN).ffcsMap.centerAt({
                    x : obj.x,          //中心点X坐标
                    y : obj.y,           //中心点y坐标
                    wkid : currentArcgisConfigInfo.wkid, //wkid 2437
                    zoom : obj.mapCenterLevel
                });
            }
        }
    });
}


//获取当前编辑的网格的信息
function loadGridArcgisData(wid) {
    var url = "";
    var param = "?wid="+wid+"&mapt="+currentArcgisConfigInfo.mapType;
    url = js_ctx + "/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGrid.json";
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
            if(data != null && data.list !=null && data.list.length>0) {
                gridDataInit(data.list[0]);
            }else if( data == null || data.list ==null || data.list.length==0) {
                $("#grid_mapt").val("");
                $("#grid_x").val("");
                $("#grid_y").val("");
                $("#grid_hs").val("");

            }
        }
    });
}

function gridDataInit(data) {
    $("#grid_mapt").val(data.mapt);
    $("#grid_x").val(data.x);
    $("#grid_y").val(data.y);
    $("#grid_hs").val(data.hs);
}


function showCenterPoint(x,y){
    $("#map").ffcsMap.showCenterPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
}

function getMapArcgisDatas(gridId, layerName) {
    var url =  js_ctx + '/zhsq/map/arcgis/arcgisdata/getArcgisDrawDataOfGridsByLevel.json?mapt='+currentArcgisConfigInfo.mapType+'&showType=0'+'&gridId='+gridId;
    $("#map"+currentN).ffcsMap.render(layerName,url,2,true,null,null,null,OUTLINE_FONT_SETTINGS,null,null,true);
    var layer = $("#map"+currentN).ffcsMap.getMap().getLayer(layerName);
    $("#map"+currentN).ffcsMap.getMap().reorderLayer(layer, 0);//重新定义网格轮廓的显示层级（最底层）
    //$("#map"+currentN).ffcsMap.ffcsDisplayhot({w:650,h:400},"gridDataLayer",'网格',getGridInfoDetail,null);
}

/**
 * 点击网格轮廓显示网格信息
 * @param data
 * @returns {string}
 */
function getGridInfoDetail(data){
    var mapt = currentArcgisConfigInfo.mapType;
    var layer = data['layerName'];
    var url = "";
    var context = "";
    url =  ZZGRID_URL +'/zzgl/grid/mixedGrid/detail.jhtml?gridId='+data['wid'];
    context = '<iframe id="grid_info" name="grid_info" width="100%" height="100%" src="'+url+'" marginwidth=0 marginheight=0 scrolling="" frameborder=0></iframe>';
    return context;
}



/**
 * 网格图层右键菜单
 * @param layerName
 */
function layerAddGridMenu(layerName){
    selected = null;
    var map = $("#map"+currentN).ffcsMap.getMap();
    if(typeof layerName == "undefined" || layerName == null || layerName == "" || layerName == "gridDataLayer"){
        layerName = "gridDataLayer";
    }

    var layer = map.getLayer(layerName);

    //创建右键菜单
    ctxMenuForGraphics = new dojo.dijit.Menu({});

    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "编辑网格信息",
        class: "menuItemStyle",
        onClick: function() {
            var data = getSelectedData(selected);
            var url = ZZGRID_URL + '/zzgl/grid/mixedGrid/edit.jhtml?gridId='+data.wid+"&isCrossDomain=true";
            var mapDataDetailWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", url, {title: "编辑网格信息", width:870, height:400, scrolling: "no"})[0];
            parent.addMsgServer(mapDataDetailWin);
        }
    }));

    ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "编辑中心点",
        class: "menuItemStyle",
        onClick: function() {
            $("#map"+currentN).ffcsMap.ffcsDisplayhot({w:650,h:400},"gridDataLayer",'网格',null,null);
            var data = getSelectedData(selected);
            clearSpecialLayer("gridDataLayer");
            $("#map"+currentN).ffcsMap.showCenterPoint(data.x,data.y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
            drawGridPoint(data.x,data.y);
            drawGridCenterPoint();
            $("#wid").val(data.wid);
            $("#x").val(data.x);
            $("#y").val(data.y);
            $("#hs").val(data.hs);
            $("#girdMapBar").show();
            chooseGridEditTool('drawGridPoint');
        }
    }));
    ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "编辑轮廓",
        class: "menuItemStyle",
        onClick: function() {
            $("#map"+currentN).ffcsMap.ffcsDisplayhot({w:650,h:400},"gridDataLayer",'网格',null,null);
            var data = getSelectedData(selected);
            $("#wid").val(data.wid);
            $("#x").val(data.x);
            $("#y").val(data.y);
            $("#hs").val(data.hs);
            $("#girdMapBar").show();
            chooseGridEditTool('drawGrid');
        }
    }));
    ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "删除轮廓",
        class: "menuItemStyle",
        onClick: function() {
            var data = selected.attributes.data["_oldData"];
            $("#wid").val(data.wid);
            $("#x").val(data.x);
            $("#y").val(data.y);
            $("#hs").val(data.hs);

            $("#girdMapBar").hide();

            delGridHSData();
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

//function getSelectedData(selected){
//    var data;
//    if(typeof selected.attributes != 'undeined' && selected.attributes != null){
//        data = selected.attributes.data["_oldData"];
//    }else{
//        data = selected.geometry;
//    }
//    return data;
//}


/**
 * 选择功能（绑定网格、标注中心点）
 */
function chooseGridEditTool(type){
    if(type != null && type != ''){
        if(type == "drawGridPoint"){
            $('#saveGridButton').show();
            drawGridPointFlag = true;
            blindBuildingFlag = false;
            drawBuildingHSFlag = false;
            $('#drawGridPoint').hide();
            $('#drawGrid').hide();
            $('#cancleDrawGridPoint').show();
            drawGridCenterPoint();
        }
        if(type == 'drawGrid'){
            //先清除选择的网格的轮廓（不是列表选择的网格）
            clearLayerByName("gridLayer");

            $('#saveGridButton').show();
            $('#drawGridPoint').hide();
            $('#drawGrid').hide();
            $('#cancleDrawOrEditGrid').show();
            drawGridPanel();
        }
        if(type == "cancleDrawPoint"){
            window.parent.showGridHs();
            $('#drawGrid').show();
            $('#drawGridPoint').show();
            $('#saveGridButton').hide();
            $('#cancleDrawGridPoint').hide();
            drawGridPointFlag = false;
            //结束编辑并重新定位
            endEditGridMapData();
        }
        if(type == "cancleDrawOrEditGrid"){
            $('#drawGrid').show();
            $('#drawGridPoint').show();
            $('#saveGridButton').hide();
            $('#cancleDrawGridPoint').hide();
            $('#cancleDrawOrEditGrid').hide();
            //结束编辑并重新定位
            endEditGridMapData();
        }
        if(type == "delDrawGridData"){
            $('#drawGrid').show();
            $('#drawGridPoint').show();
            $('#saveGridButton').hide();
            $('#cancleDrawGridPoint').hide();
            $('#cancleDrawOrEditGrid').hide();
            delGridHSData();

        }
    }
}


/**
 * 保存绘制的数据
 */
function saveGridData(){
    var gridId = $('#wid').val();
    var hs = $("#grid_hs").val();
    var x = $("#grid_x").val();
    var y = $("#grid_y").val();

    drawGridHSFlag = false;
    blindGridFlag = true;


    $("#grid_tipMessage").html("");
    $('#saveGridButton').hide();
    var data = 'wid='+gridId+'&x='+x+'&y='+y+'&hs='+hs+'&mapt='+currentArcgisConfigInfo.mapType;
    var url = js_ctx + '/zhsq/map/mapDataMaintain/updateMapDataOfGrid.json?'+data;

    $.ajax({
        url: url,
        type: 'POST',
        dataType:"json",
        async: false,
        error: function(data){
            $.messager.alert('提示','操作报错!','warning');
        },
        success: function(data){
            $('#drawGridPoint').show();
            $('#cancleDrawGridPoint').hide();
            $('#drawGrid').show();
            $('#cancleDrawOrEditGrid').hide();

            if(data.flag == true) {
                parent.DivShow('操作成功!');
            }else {
                parent.DivShow('操作失败!');
            }
            $("#hs").val("");
            endEditGridMapData();
            window.parent.showGridHs();
            selectRowGridInfo(gridId);
        }
    });
    if(drawGridPointFlag == true && blindBuildingFlag == false){
        drawGridPointFlag = false;
    } else {
        drawGridPointFlag = true;
    }

}


function selectRowGridInfo(gridId){
    clearSpecialLayer("gridDataLayer");
    locateGridCenter(gridId);//定位网格中心点
    loadGridArcgisData(gridId);//获取数据
    getMapArcgisDatas(gridId, "gridDataLayer");//加载网格轮廓

    layerAddGridMenu("gridDataLayer");
}


/**
 *中心点函数
 */
function drawGridCenterPoint() {
    var x = $("#grid_x").val();
    var y = $("#grid_y").val();
    $("#map"+currentN).ffcsMap.showCenterPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', gridPointCallBack,true);
}

/**
 * 编辑轮廓
 * @param x
 * @param y
 * @param hs
 */
function drawGridPanel(){

    var x = $("#grid_x").val();
    var y = $("#grid_y").val();
    var hs = $("#grid_hs").val();

    if(hs==null || hs == "" || hs.split(",").length<4) {
        $("#map"+currentN).ffcsMap.draw("POLYGON",boundaryGridCallBack);
    }else {
        $("#map"+currentN).ffcsMap.toolbarDeactivate();
        $("#map"+currentN).ffcsMap.hideCenterPointImg(x, y);
        $("#map"+currentN).ffcsMap.edit(true,boundaryGridCallBack,false);
    }
    $("#map"+currentN).ffcsMap.onClickGetPointEnd();
}

/**
 * 编辑轮廓后回调
 * @param data
 */
function boundaryGridCallBack(data) {
    var data1 = JSON.parse(data);
    var hs = data1.coordinates.toString();
    var xys = hs.split(",");
    var x = xys[0];
    var y = xys[1];
    var mapt = currentArcgisConfigInfo.mapType;
    $("#grid_mapt").val(mapt);
    if($("#grid_x").val() == "" || $("#grid_y").val() == "") {
        $("#grid_x").val(x);
        $("#grid_y").val(y);
    }
    if(xys.length >=4) {
        $("#grid_hs").val(hs);
    }

}

/**
 * 编辑中心点
 * @param callBack
 * @param x
 * @param y
 */
function drawGridPoint(x,y){
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', gridPointCallBack,true);
}

/**
 * 中心点编辑回调
 * @param data
 */
function gridPointCallBack(data) {
    var xys = data.toString().split(",");
    var x = xys[0];
    var y = xys[1];
    $('#grid_x').val(x);
    $('#grid_y').val(y);
    var mapt = currentArcgisConfigInfo.mapType;
    $("#grid_mapt").val(mapt);
}

/**
 * 删除轮廓中心点 数据
 * @returns {boolean}
 */
function delGridHSData() {
    $('#saveGridButton').hide();
    $("#grid_tipMessage").html("");
    var gridId = $('#wid').val();
    var url = js_ctx + "/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
    var data = "targetType=grid&wid="+ gridId +"&mapt="+currentArcgisConfigInfo.mapType;
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
            $('#delDrawGridData').hide();
            endEditGridMapData();
            selectRowGridInfo(gridId);
        },
        error:function(msg){
            parent.DivShow('删除失败！');
        }
    });

}

/**
 * 编辑完毕后需要关闭地图编辑工具并重新加载地图业务数据和轮廓
 */
function endEditGridMapData(){
    $("#map").ffcsMap.toolbarDeactivate();
    $("#map").ffcsMap.onClickGetPointEnd();
    $("#map").ffcsMap.changeIsDrawEdit();
    $("#map"+currentN).ffcsMap.clear({layerName : 'select_cpoint_ly'});
}

/**
 * 显示网格信息
 * @param gridId
 */
function showGridDetail(gridId){
    var url = ZZGRID_URL + '/zzgl/grid/mixedGrid/detail.jhtml?gridId='+gridId;
    windowPanelHelper.showWindow("网格信息", url, {title: "网格信息", width:650, height:400, scrolling: "no"});
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