/**
 * Created by sulch on 2018/5/10.
 */

var drawUrbanPartsPointFlag = true;//是否编辑中心点操作标识
var blindUrbanPartsFlag = true;//是否绑定轮廓操作标识
var showUrbanPartsLayerFlag = false; //是否显示所在网格的轮廓标识
var drawUrbanPartsHSFlag = true;//绘制楼宇轮廓标识

var saveCenterPointFlag = false; //是否保存中心点坐标
var saveUrbanPartsHSFlag = false   //是否保存楼宇轮廓信息



/**
 * 判断是否有轮廓信息
 */
function hasUrbanPartsArcgisData(bizId, bizType) {
    if(typeof bizId != 'undefined' && bizId != null && bizId != ''){
        var url = "";
        var param = "?wid="+bizId+"&mapt="+currentArcgisConfigInfo.mapType+"&bizType="+bizType;
        url = js_ctx + "/zhsq/map/mapDataMaintain/getArcgisDataOfUrbanParts.json";

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
                $("#urbanParts_tipMessage").html("");
                var tipMess = "";
                if(data != null && typeof(data.list) != 'undefined' && data.list.length==0 && wid != "" && wid != null && typeof(wid) != undefined) {
                    tipMess = "未标注中心点！";
                    window.document.getElementById("urbanParts_tipMessage").innerHTML = tipMess;

                    $("#drawUrbanPartsPoint").show();
                    $("#delDrawUrbanPartsData").hide();
                    $("#editUrbanParts").show();
                }else{
                    if(data == null || typeof(data.list) == 'undefined' || data.list[0].x == "" || data.list[0].y == ""){
                        tipMess = "未标注中心点。";
                        window.document.getElementById("urbanParts_tipMessage").innerHTML = tipMess;
                        $("#drawUrbanPartsPoint").show();
                        $("#editUrbanParts").show();
                        $("#delDrawUrbanPartsData").hide();
                    }else{
                        $("#urbanPartsMapBar").show();
                        $("#drawUrbanPartsPoint").show();
                        $("#editUrbanParts").show();
                        $("#delDrawUrbanPartsData").show();
                        urbanPartsDataInit(data.list[0]);
                        $("#map"+currentN).ffcsMap.centerAt({
                            x : data.list[0].x,          //中心点X坐标
                            y : data.list[0].y,           //中心点y坐标
                            wkid : currentArcgisConfigInfo.wkid
                        });
                    }
                }
            }
        });
    }else{
        $("#urbanPartsMapBar").hide();
    }

}

/**
 * 定位中心点
 * @param id
 * @param mapt
 */
function locateUrbanPartsCenter(id) {
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


//获取当前编辑的部件的信息
function loadUrbanPartsArcgisData(wid, bizType) {
    var url = "";
    var param = "?wid="+wid+"&mapt="+currentArcgisConfigInfo.mapType+"&bizType="+bizType;
    url = js_ctx + "/zhsq/map/mapDataMaintain/getArcgisDataOfUrbanParts.json";
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
                urbanPartsDataInit(data.list[0]);
            }else if( data == null || data.list ==null || data.list.length==0) {
                $("#urbanParts_mapt").val("");
                $("#urbanParts_x").val("");
                $("#urbanParts_y").val("");

            }
        }
    });
}

function urbanPartsDataInit(data) {
    $("#urbanParts_mapt").val(data.mapt);
    $("#urbanParts_x").val(data.x);
    $("#urbanParts_y").val(data.y);
    $("#map"+currentN).ffcsMap.centerAt({
        x : data.x,          //中心点X坐标
        y : data.y,           //中心点y坐标
        wkid : currentArcgisConfigInfo.wkid
    });
}


function showCenterPoint(x,y){
    $("#map").ffcsMap.showCenterPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
}


/**
 * 点击网格轮廓显示网格信息
 * @param data
 * @returns {string}
 */
function getUrbanPartsInfoDetail(data){
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
function layerAddUrbanPartsMenu(layerName){
    selected = null;
    var map = $("#map"+currentN).ffcsMap.getMap();
    if(typeof layerName == "undefined" || layerName == null || layerName == "" || layerName == "urbanPartsLayer"){
        layerName = "urbanPartsLayer";
    }

    var layer = map.getLayer(layerName);

    //创建右键菜单
    ctxMenuForGraphics = new dojo.dijit.Menu({});

    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "编辑",
        class: "menuItemStyle",
        onClick: function() {
            var data = getSelectedData(selected);
            var subTypeId = $("#urbanParts_typeId").val();
            var subTypeCode = $("#urbanParts_typeCode").val();
            var url = ZZGRID_URL + '/zzgl/res/default/edit.jhtml?resTypeId='+subTypeId+'&isCrossDomain=true';
            if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02020401" || subTypeCode == "020116"){
                url = ZZGRID_URL + "/zzgl/res/default/edit.jhtml?resTypeId="+subTypeId+"&isCrossDomain=true&resId="+data.wid;
            }else if(subTypeCode == "02020501"){//全球眼
                url = ZZGRID_URL + '/zzgl/res/globalEyes/selectCompanyType.jhtml?isCrossDomain=true';
            }else if(subTypeCode == "02010601"){
                url = ZZGRID_URL + "/zzgl/fireGrid/fireResource/edit1.jhtml?isCrossDomain=true&id="+data.wid;
            }

            var mapDataDetailWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", url, {title: "编辑部件信息", width:870, height:400, scrolling: "no"})[0];
            parent.addMsgServer(mapDataDetailWin);
        }
    }));

    ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
    ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
        label: "删除",
        class: "menuItemStyle",
        onClick: function() {
            var data = getSelectedData(selected);
            var idStr = data.wid;
            var subTypeId = $("#urbanParts_typeId").val();
            var subTypeCode = $("#urbanParts_typeCode").val();
            var url = js_ctx + '/zhsq/map/mapDataMaintain/delUrbanParts.jhtml';
            //if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02020401"){//公共厕所、路灯、城市公共公交车站
            //    url = ZZGRID_URL + "/zzgl/res/default/batchDelete.jhtml";
            //}else if(subTypeCode == "02010601"){//消防栓
            //    url = ZZGRID_URL + "/zzgl/fireGrid/fireResource/batchDelete.jhtml";
            //}

            $.messager.confirm('提示', '您确定删除选中的部件信息吗?', function(r) {
                if (r){
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: 'bizType='+subTypeCode+'&idStr='+data.wid,
                        dataType:"json",
                        success: function(data){
                            if(data.result==0){
                                parent.DivShow('操作失败!');
                            }else{
                                parent.DivShow('操作成功!');
                                //reGisPositionByType("urbanParts");

                                parent.DataIframe.tabChange("urbanParts");
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

    //ctxMenuForGraphics.addChild(new dijit.MenuSeparator());
    //ctxMenuForGraphics.addChild(new dojo.dijit.MenuItem({
    //    label: "编辑中心点",
    //    class: "menuItemStyle",
    //    onClick: function() {
    //        $("#map"+currentN).ffcsMap.ffcsDisplayhot({w:650,h:400},"urbanPartsLayer",'部件',null,null);
    //        var data = getSelectedData(selected);
    //        clearSpecialLayer("urbanPartsLayer");
    //        $("#map"+currentN).ffcsMap.showCenterPoint(data.x,data.y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
    //        drawUrbanPartsPoint(data.x,data.y);
    //        drawUrbanPartsCenterPoint();
    //        $("#wid").val(data.wid);
    //        $("#urbanParts_x").val(data.x);
    //        $("#urbanParts_y").val(data.y);
    //        $("#urbanPartsMapBar").show();
    //        chooseUrbanPartsEditTool('drawUrbanPartsPoint');
    //    }
    //}));


    ctxMenuForGraphics.startup();

    if(typeof layer != 'undefined'){
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
    }


    //启动右键菜单
    ctxMenuForGraphics.startup();
}


/**
 * 选择功能（绑定网格、标注中心点）
 */
function chooseUrbanPartsEditTool(type){
    if(type != null && type != ''){
        if(type == "drawUrbanPartsPoint"){
            $('#saveUrbanPartsButton').show();
            drawUrbanPartsPointFlag = true;
            $('#drawUrbanPartsPoint').hide();
            $('#cancleDrawUrbanPartsPoint').show();
            drawUrbanPartsCenterPoint();
            stopMapMenu();
        }
        if(type == "cancleDrawPoint"){
            $('#drawUrbanPartsPoint').show();
            $('#saveUrbanPartsButton').hide();
            $('#cancleDrawUrbanPartsPoint').hide();
            drawUrbanPartsPointFlag = false;
            //结束编辑并重新定位
            endEditUrbanPartsMapData();
            startMapMenu();
        }
        if(type == "edit"){
            $('#drawUrbanPartsPoint').show();
            $('#saveUrbanPartsButton').hide();
            $('#cancleDrawUrbanPartsPoint').hide();
            //结束编辑并重新定位
            endEditUrbanPartsMapData();
            //编辑
            editUrbanparts();
        }
    }
}


/**
 * 保存绘制的数据
 */
function saveUrbanPartsData(){
    var wid = $('#wid').val();
    var x = $("#urbanParts_x").val();
    var y = $("#urbanParts_y").val();
    var bizType = $("#urbanParts_typeCode").val();

    drawUrbanPartsHSFlag = false;
    blindUrbanPartsFlag = true;

    if(bizType == "02010601"){//消防栓的标注类型是0601
        bizType = "0601";
    }
    $("#urbanParts_tipMessage").html("");
    $('#saveUrbanPartsButton').hide();
    var data = 'wid='+wid+'&x='+x+'&y='+y+'&bizType='+bizType+'&mapt='+currentArcgisConfigInfo.mapType;
    var url = js_ctx + '/zhsq/map/mapDataMaintain/updateMapDataOfUrbanParts.json?'+data;

    $.ajax({
        url: url,
        type: 'POST',
        dataType:"json",
        async: false,
        error: function(data){
            $.messager.alert('提示','操作报错!','warning');
        },
        success: function(data){
            $('#drawUrbanPartsPoint').show();
            $('#cancleDrawUrbanPartsPoint').hide();
            $('#drawUrbanParts').show();
            $('#cancleDrawOrEditUrbanParts').hide();

            if(data.flag == true) {
                parent.DivShow('操作成功!');
            }else {
                parent.DivShow('操作失败!');
            }
            $("#hs").val("");
            endEditUrbanPartsMapData();

        }
    });
    if(drawUrbanPartsPointFlag == true && blindBuildingFlag == false){
        drawUrbanPartsPointFlag = false;
    } else {
        drawUrbanPartsPointFlag = true;
    }

}


function selectRowUrbanParts(bizId, bizType){
    clearSpecialLayer("urbanPartsLayer");
    loadUrbanPartsArcgisData(bizId, bizType);//获取数据
    layerAddUrbanPartsMenu("urbanPartsLayer");
}

/**
 *中心点函数
 */
function drawUrbanPartsCenterPoint() {
    var x = $("#urbanParts_x").val();
    var y = $("#urbanParts_y").val();
    $("#map"+currentN).ffcsMap.showCenterPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png');
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', urbanPartsPointCallBack,true);
}

/**
 * 编辑中心点
 * @param callBack
 * @param x
 * @param y
 */
function drawUrbanPartsPoint(x,y){
    $("#map"+currentN).ffcsMap.toolbarDeactivate();
    $("#map"+currentN).ffcsMap.changeIsDrawEdit();
    if(x == null || x == "" || x == 0) {
        x = defaultx;
        y = defaulty;
    }
    $("#map"+currentN).ffcsMap.onClickGetPoint(x, y, js_ctx + '/js/map/arcgis/library/style/images/RedShinyPin.png', urbanPartsPointCallBack,true);
}

/**
 * 中心点编辑回调
 * @param data
 */
function urbanPartsPointCallBack(data) {
    var xys = data.toString().split(",");
    var x = xys[0];
    var y = xys[1];
    $('#urbanParts_x').val(x);
    $('#urbanParts_y').val(y);
    var mapt = currentArcgisConfigInfo.mapType;
    $("#urbanParts_mapt").val(mapt);
}


/**
 * 编辑完毕后需要关闭地图编辑工具并重新加载地图业务数据和轮廓
 */
function endEditUrbanPartsMapData(){
    $("#map").ffcsMap.toolbarDeactivate();
    $("#map").ffcsMap.onClickGetPointEnd();
    $("#map").ffcsMap.changeIsDrawEdit();
    $("#map"+currentN).ffcsMap.clear({layerName : 'select_cpoint_ly'})
    reGisPositionByType("urbanParts");
}

/**
 * 显示部件信息
 * @param gridId
 */
function showUrbanPartsDetail(bizId, bizType, bizName){
    var url = ZZGRID_URL + '/zzgl/res/default/show.jhtml?resId='+bizId;
    if(typeof bizType != "undefined" && bizType != null){
        //全球眼现网已作废
        if(bizType == "02010301" || bizType == "02010501" || bizType == "02020401"){
            url = ZZGRID_URL + '/zzgl/res/default/show.jhtml?resId='+bizId+"&isCrossDomain=true";
        }else if(bizType == "02010601"){
            url = ZZGRID_URL + '/zzgl/fireGrid/fireResource/detail1.jhtml?id='+bizId+"&isCrossDomain=true";
        }
    }
    windowPanelHelper.showWindow(bizName+"信息", url, {title: bizName+"信息", width:650, height:400, scrolling: "no"});
}

/**
 * 编辑部件
 */
function editUrbanparts(){
    var wid = $('#wid').val();
    var subTypeId = $("#urbanParts_typeId").val();
    var subTypeCode = $("#urbanParts_typeCode").val();
    var url = ZZGRID_URL + '/zzgl/res/default/edit.jhtml?resTypeId='+subTypeId+'&isCrossDomain=true';
    if(subTypeCode == "02010301" || subTypeCode == "02010501" || subTypeCode == "02020401" || subTypeCode == "020116"){
        url = ZZGRID_URL + "/zzgl/res/default/edit.jhtml?resTypeId="+subTypeId+"&isCrossDomain=true&resId="+wid;
    }else if(subTypeCode == "02020501"){//全球眼
        url = ZZGRID_URL + '/zzgl/res/globalEyes/selectCompanyType.jhtml?isCrossDomain=true';
    }else if(subTypeCode == "02010601"){
        url = ZZGRID_URL + "/zzgl/fireGrid/fireResource/edit1.jhtml?isCrossDomain=true&id="+wid;
    }

    var mapDataDetailWin = parent.windowPanelHelper.showWindow("mapDataDetailWin", url, {title: "编辑部件信息", width:870, height:400, scrolling: "no"})[0];
    parent.addMsgServer(mapDataDetailWin);
}