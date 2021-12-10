var iisurl = "http://183.60.192.206:8000/";
//var iisurl = "http://localhost:8080";
//var flyPath = "C:\\Users\\MaC\\Desktop\\locongna.fly";
//var flyPath = iisurl + "lecong.FLY";
var flyPath = iisurl + "lecong.fly";
var routeurl = "http://183.60.192.205:6080/arcgis/rest/services/RouteAnalysis/NAServer/Route";
var searchurl = iisurl + "lecong_GeoSP/geosp/handler/POIService.ashx?jsoncallback=?"
var busUrl = iisurl + "lecong_GeoSP/pathanalysis.asmx/GetPathAnalysis?"; //问号必须存在



function CreateSGWorld() {//初始化skyline控件

    var obj = document.getElementById("sgworld");
    if (obj == null) {
        obj = document.createElement('object');
        document.body.appendChild(obj);
        obj.name = "sgworld";
        obj.id = "sgworld";
        obj.classid = "CLSID:3a4f91b1-65a8-11d5-85c1-0001023952c1";
        obj.style.height = "0%";
        obj.style.width = "100%";
    }
    return obj;
}
$(document).ready(function () {
    onload();
    //$(".show_tool span").each(function () {
    $("#sgworld").each(function () {
        $(this).bind("mouseover", function () {
            var numFlag = $(this).attr("flag");
            if (numFlag != undefined) {
                if (numFlag != 2) {
                    $(this).css("background-color", "#D7D7D7").css("font-size", "15px");
                }
            } else {
                $(this).css("background-color", "#D7D7D7").css("font-size", "15px");
            }
        })
        $(this).bind("mouseout", function () {
            var numFlag = $(this).attr("flag");
            if (numFlag != undefined) {
                if (numFlag != 2) {
                    $(this).css("background-color", "#f5f5f5").css("font-size", "12px");
                }
            } else {
                $(this).css("background-color", "#f5f5f5").css("font-size", "12px");
            }
        })
        $(this).bind("click", function () {
            var numFlag = $(this).attr("flag");
            if (numFlag != undefined) {
                if ($(this).attr("flag") == 3) {
                    $("span").css("background-color", "#f5f5f5").css("font-size", "12px");
                    for (var i = 0; i < $("span").length; i++) {
                        var clsName = $("span")[i].className;
                        $("#" + clsName).attr("flag", 1);
                    }
                } else if ($(this).attr("flag") == 1) {
                    $("span").css("background-color", "#f5f5f5").css("font-size", "12px");
                    for (var i = 0; i < $("span").length; i++) {
                        var clsName = $("span")[i].className;
                        $("#" + clsName).attr("flag", 1);
                    }
                    $(this).css("background-color", "Blue").css("font-size", "12px").attr("flag", 2);
                } else if ($(this).attr("flag") == 0) {

                    var indexEle = $("span").index(this);
                    var cName = $("span")[indexEle].className;
                    $("#" + cName).attr("flag", 1);
                    $("." + cName).css("background-color", "#f5f5f5").css("font-size", "12px");
                } else {

                }
            }
        })

    });

});
function onload() {//载入fly

    try {
        sgworld.Project.Open(flyPath);
    }
    catch (e) {
        //alert("请确认加载的FLY文件路径是否正确！");
    }
}
var test = "";
function zoomIn() {//放大
    var sgworld = CreateSGWorld();
    sgworld.Navigate.ZoomIn();
} //放大
function zoomOut() {//缩小
    var sgworld = CreateSGWorld();
    sgworld.Navigate.ZoomOut();
} //缩小
function centerAt(x, y) {//飞到坐标
    var sgworld = CreateSGWorld();
    var position = sgworld.Creator.CreatePosition(x, y, 0, 2, 0, 0, 0, 0); //设置XY
    position = position.AimTo(sgworld.Creator.CreatePosition(position.X, position.Y, -1, 0, position.Pitch, position.Roll, position.Yaw, 0)); //设置角度为自上往下
    sgworld.Navigate.FlyTo(position); //飞至对象
} //定位
function cnterAtLevel(x, y, h) {//飞到指定坐标 并设置放大范围
    var sgworld = CreateSGWorld();
    var position = sgworld.Creator.CreatePosition(x, y, 0, 2, 0, 0, 0, 0);
    position = position.AimTo(sgworld.Creator.CreatePosition(position.X, position.Y, -1, 0, position.Pitch, position.Roll, position.Yaw, 0));
    position.Altitude = h; //根据需求设置高度
    sgworld.Navigate.FlyTo(position);
} //带高度定位
function Clear() {//删除临时文件夹 以删除临时添加对象
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
        sgworld.ProjectTree.DeleteItem(id);
    }
} //清楚临时数据
function createline(arr, r, g, b, width) {//根据坐标串划取连线
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");

    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); } //查看temp文件夹是否存在 否则创建
    //var cVerticesArray = [0,  0,  0, 1,  1, 11, 2, 3, 12, -122.415592,  37.761254, 13, -122.415557,  37.760973, 14, -122.415081,  37.76099,  15 ];
    var cPolyline = sgworld.Creator.CreatePolylineFromArray(arr, sgworld.Creator.CreateColor(r, g, b, 1), 3, sgworld.ProjectTree.FindItem("temp"), "line")
    cPolyline.LineStyle.Width = width;
    sgworld.Navigate.FlyTo(cPolyline.ID);
} //轨迹生成
function clearline() {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
        sgworld.ProjectTree.DeleteItem(id);
    }
}
function createModel(x, y, z, modelurl) {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");

    if (id > 0) {
    } else { id = sgworld.ProjectTree.CreateGroup("temp", 0); }
    var position = sgworld.Creator.CreatePosition(x, y, z,3);

    var model = sgworld.Creator.CreateModel(position, modelurl,1,3,id);

}
//显示模型轮廓
function outlineShow(gridid) {
    var id = sgworld.ProjectTree.FindItem("1:1000乐从DLG数据\\建筑_" + gridid);
    sgworld.ProjectTree.SetVisibility(id, true);
    sgworld.Navigate.FlyTo(id);
}
//隐藏模型轮廓
function outlineHied(gridid) {
    var id = sgworld.ProjectTree.FindItem("1:1000乐从DLG数据\\建筑_" + gridid);
    sgworld.ProjectTree.SetVisibility(id, false);
    sgworld.Navigate.FlyTo(id);
}

function fullExtent() {//飞至佛山上空
    var SGWorld = CreateSGWorld();
    try {
        SGWorld.Navigate.FlyTo(SGWorld.ProjectTree.FindItem("俯瞰乐从"));
    } catch (e) { }
} //全图（城市全景）
function drawBarriers() {//添加障碍点 左击事件
    var sgworld = CreateSGWorld();
    i = i + 1; //为障碍点区分编号 方便管理
    if (edit != 0) {
        try {
            var mouseInfo = sgworld.Window.GetMouseInfo()
            var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
            if (CursorCoord == null)
            { return false; } else {
                var pos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
                var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
                sgworld.Creator.CreateImageLabel(pos, "http://172.16.2.147/indexIcon.png", cLabelStyle, sgworld.ProjectTree.FindItem("temp"), "Barrier" + i)
            }
        } catch (e) { return false; }
    } return false;
} //添加障碍点（缺少更新路径分析）
function endBarriers() {//添加障碍点 右击事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        sgworld.Window.SetInputMode(0);
        edit = 0;
        i = 0;
        sgworld.DetachEvent("OnLButtonDown", drawBarriers);
        sgworld.DetachEvent("OnRButtonUp", endBarriers);
    }
}
var edit = 0; var i = 0
function AddBarriers() {//添加障碍物
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态
    sgworld.AttachEvent("OnLButtonDown", drawBarriers); //绑定鼠标事件
    sgworld.AttachEvent("OnRButtonUp", endBarriers);
    edit = 1;
}
function EnvelopeQueryOLB() {//获取地图范围左键Down事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        // try {
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (CursorCoord == null)
        { return false; } else {
            startpos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
        }
        //} catch (e) { return false;}
    } return false;
}
function EnvelopeQueryORB() {//获取地图范围左键Up事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y); //获取鼠标所在坐标
        var id = sgworld.ProjectTree.FindItem("temp\\EnvelopeQuery");
        if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        } //删除创建的临时文件
        var maxX, minX, maxY, minY;
        if (startpos.X > CursorCoord.Position.X) { maxX = startpos.X; minX = CursorCoord.Position.X; } else { minX = startpos.X; maxX = CursorCoord.Position.X; }
        if (startpos.Y > CursorCoord.Position.Y) { maxY = startpos.Y; minY = CursorCoord.Position.Y; } else { minY = startpos.Y; maxY = CursorCoord.Position.Y; }
        //alert("minX:" + minX + "minY:" + minY + "maxX:" + maxX + "maxY:" + maxY);//比较并返回XY值

        searchInExtent(minX, minY, maxX, maxY, keywork);
        sgworld.Window.SetInputMode(0);
        edit = 0; //设置编辑状态为否
        startpos = null;
        sgworld.AttachEvent("OnLButtonDown", EnvelopeQueryOLB);
        sgworld.AttachEvent("OnLButtonUp", EnvelopeQueryORB);
        sgworld.AttachEvent("OnFrame", EnvelopeQueryOF);
        sgworld.DetachEvent("OnLButtonDown", EnvelopeQueryOLB); //解除事件绑定
        sgworld.DetachEvent("OnLButtonUp", EnvelopeQueryORB);
        sgworld.DetachEvent("OnFrame", EnvelopeQueryOF);
    }
    return true;
}
function EnvelopeQueryOF() {//地图范围鼠标移动事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        var id = sgworld.ProjectTree.FindItem("temp\\EnvelopeQuery");
        if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (startpos == null)
        { return false; } else {
            var cVerticesArray = [startpos.X, startpos.Y, 0, startpos.X, CursorCoord.Position.Y, 0, CursorCoord.Position.X, CursorCoord.Position.Y, 0, CursorCoord.Position.X, startpos.Y, 0];
            var nLineColor = 0xFF00FF00;
            var nFillColor = 0x7FFF0000;
            sgworld.Creator.CreatePolygonFromArray(cVerticesArray, nLineColor, nFillColor, 2, sgworld.ProjectTree.FindItem("temp"), Description = "EnvelopeQuery");
        }
    }
}
var startpos = null; //起始位置变量
function envelopeQuery(str) {//框选地图获得范围事件
    keywork = str;
    var sgworld = CreateSGWorld();

    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); }
    sgworld.Window.SetInputMode(1);
    sgworld.AttachEvent("OnLButtonDown", EnvelopeQueryOLB);
    sgworld.AttachEvent("OnLButtonUp", EnvelopeQueryORB);
    sgworld.AttachEvent("OnFrame", EnvelopeQueryOF);
    edit = 1; sgworld.Command.Execute(1056, 0);
} //拉框查询
function BufferQueryOLB() {//缓冲区查询左键Down事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        // try {
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (CursorCoord == null)
        { return false; } else {
            startpos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
        }
        //} catch (e) { return false;}
    } return false;
}
function BufferQueryORB() {//缓冲区查询右键Up事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        var id = sgworld.ProjectTree.FindItem("temp\\BufferQuery");
        if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (startpos == null)
        { return false; } else {
            var Radius = startpos.DistanceTo(sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0));
            var Circle = sgworld.Creator.CreateCircle(startpos, Radius, nLineColor, nFillColor, sgworld.ProjectTree.FindItem("temp"), Description = "BufferQuery");
            Circle.FillStyle.Color.SetAlpha(nAlpha);

            searchInBuffer(startpos.X, startpos.Y, Radius, keyword);
        }
        sgworld.Window.SetInputMode(0);
        edit = 0;
        startpos = null;
        var id = sgworld.ProjectTree.FindItem("temp\\BufferQuery");
        if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
        sgworld.DetachEvent("OnLButtonDown", BufferQueryOLB);
        sgworld.DetachEvent("OnRButtonUp", BufferQueryORB);
        sgworld.DetachEvent("OnFrame", BufferQueryOF);
    }
    return true;
}
var keyword = "";
function bufferQuery(str) {//缓冲区查询事件
    keyword = str;
    var sgworld = CreateSGWorld();
    nLineColor = '0xFF00FF00'; nFillColor = '0x7FFF0000'; nAlpha = 0.5;
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态为选择状态
    sgworld.AttachEvent("OnLButtonDown", BufferQueryOLB);
    sgworld.AttachEvent("OnRButtonUp", BufferQueryORB);
    sgworld.AttachEvent("OnFrame", BufferQueryOF);
    edit = 1;
} //缓冲区（功能有待考虑完善）
var nLineColor = 0xFF00FF00; var nFillColor = 0x7FFF0000; var nAlpha = 0.5; //初始属性设置
function CreateBufferQuery(X, Y, R, LineColor, FillColor, Alpha) {
    var id = sgworld.ProjectTree.FindItem("temp\\BufferQuery");
    if (id > 0) {
        sgworld.ProjectTree.DeleteItem(id);
    }
    var pos = sgworld.Creator.CreatePosition(X, Y, 0, 2, 0, 0, 0, 0);
    nLineColor = LineColor;
    nFillColor = FillColor;
    nAlpha = Alpha;
    var Circle = sgworld.Creator.CreateCircle(pos, R, nLineColor, nFillColor, sgworld.ProjectTree.FindItem("temp"), Description = "BufferQuery");
    Circle.FillStyle.Color.SetAlpha(nAlpha);
} //根据数据设置缓冲区
function BufferQueryOF() {//缓冲区查询鼠标移动事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        var id = sgworld.ProjectTree.FindItem("temp\\BufferQuery");
        if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (startpos == null)
        { return false; } else {
            var Radius = startpos.DistanceTo(sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0));
            var Circle = sgworld.Creator.CreateCircle(startpos, Radius, nLineColor, nFillColor, sgworld.ProjectTree.FindItem("temp"), Description = "BufferQuery");
            Circle.FillStyle.Color.SetAlpha(nAlpha);
        }
    }
}
var XY = "112.791133,23.334195;112.714599,23.156860;112.824733,23.386462;112.862067,23.339795;112.854600,23.287528;112.793000,23.252061;112.718332,23.315528";
var arr_pos = [], arr_parent = [], arr_child = [];
function LoadLable(arr1, arr2, arr3) {//拓扑分析
    var sgworld = CreateSGWorld();
    arr_pos = arr1; arr_parent = arr2; arr_child = arr3;
    var id = sgworld.ProjectTree.FindItem("LoadLable");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("LoadLable", 0); }
    var XYs = XY.split(';');
    var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
    for (var i = 0; i < XYs.length; i++) {
        var X = XYs[i].split(',')[0];
        var Y = XYs[i].split(',')[1];
        var pos = sgworld.Creator.CreatePosition(X, Y, 0, 2, 0, 0, 0, 0);

        var point = sgworld.Creator.CreateImageLabel(pos, "http://172.16.2.147/indexIcon.png", cLabelStyle, sgworld.ProjectTree.FindItem("temp"), "LoadLable" + i);
    }
    //sgworld.DetachEvent("OnFrame", BufferQueryOF());
    CenterAt(XYs[0].split(',')[0], XYs[0].split(',')[1]);

    //    sgworld.AttachEvent("OnFrame", LoadLableOF);
}

function LoadLableOF() {//拓扑分析获取对象事件

    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("LoadLable");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("LoadLable", 0); }
    var XYs = XY.split(';');
    var mouseInfo = sgworld.Window.GetMouseInfo()
    var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y, 2);
    if (CursorCoord.ObjectID != "") {
        var obj = sgworld.Creator.GetObject(CursorCoord.ObjectID);
        //for (var i = 1; i < XYs.length - 1; i++) {
        //    var X1 = XYs[0].split(',')[0], Y1 = XYs[0].split(',')[1], X2 = XYs[i].split(',')[0], Y2 = XYs[i].split(',')[1];
        //    CreateArrow(XYs[0].split(',')[0], XYs[0].split(',')[1], XYs[i].split(',')[0], XYs[i].split(',')[1]);
        //    var verticesArray = [X1, Y1, 0, X2, Y2, 0];
        //    sgworld.Creator.CreatePolylineFromArray(verticesArray, "0000ff", 2, sgworld.ProjectTree.FindItem("LoadLable"), "");
        //}
        for (var i = 0; i < arr_parent.length / 2; i++) {
            CreateArrow(arr_pos[0], arr_pos[1], arr_parent[i], arr_parent[2 * i + 1]);
            var verticesArray = [arr_pos[0], arr_pos[1], 0, arr_parent[i], arr_parent[2 * i + 1], 0];
            sgworld.Creator.CreatePolylineFromArray(verticesArray, "0000ff", 2, sgworld.ProjectTree.FindItem("LoadLable"), "");
        }
        for (var i = 1; i < arr_child.length / 2; i++) {
            CreateArrow(arr_child[2 * i], arr_child[2 * i + 1], arr_pos[0], arr_pos[1]);
            var verticesArray = [arr_child[2 * i], arr_child[2 * i + 1], 0, arr_pos[0], arr_pos[1], 0];
            sgworld.Creator.CreatePolylineFromArray(verticesArray, "0000ff", 2, sgworld.ProjectTree.FindItem("LoadLable"), "");
        }


        return false;
    } else {
        sgworld.ProjectTree.DeleteItem(sgworld.ProjectTree.FindItem("LoadLable"))
        return false;
    }
} //拓扑功能的鼠标移动显示事件
function LoadGeAnMap() {
    var sgworld = CreateSGWorld();
    var wmsStr = "[INFO]\r\nMeters=0\r\nMPP=2.6822090148925781e-006\r\nUrl=http://192.168.14.117/SFS/streamer.ashx?request=GetMap&Version=1.1.1&Service=WMS&SRS=EPSG:4326&BBOX=-180,-90,180,90&HEIGHT=128&WIDTH=256&Layers=Honolulu_Globe_Imagery.I.mpt&Styles=&Format=image/jpeg\r\nxul=-180\r\nylr=-90\r\nxlr=180\r\nyul=90\r\nWKT=GEOGCS['WGS 84',DATUM['WGS_1984',SPHEROID['WGS 84',6378137,298.257223563,AUTHORITY['EPSG','7030']],AUTHORITY['EPSG','6326']],PRIMEM['Greenwich',0,AUTHORITY['EPSG','8901']],UNIT['degree',0.01745329251994328,AUTHORITY['EPSG','9122']],AUTHORITY['EPSG','4326']]";
    sgworld.Creator.CreateImageryLayer("wms", -180, 90, 180, -90, "<EXT><ExtInfo><![CDATA[" + wmsStr + "]]></ExtInfo><ExtType>wms</ExtType></EXT>", "gisplg.rct", 0, "test");
}
function DelPoint() {//删除点事件
    var sgworld = CreateSGWorld();
    sgworld.Window.SetInputMode(1);
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); }
    sgworld.AttachEvent("OnLButtonDown", DelPointOLB);
}
function DelPointOLB() {//删除点点击事件
    var sgworld = CreateSGWorld();
    var mouseInfo = sgworld.Window.GetMouseInfo()
    var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y, 2);
    if (CursorCoord.ObjectID != "") {
        var obj = sgworld.Creator.GetObject(CursorCoord.ObjectID);

        alert("确定删除" + obj.name + "?");
        sgworld.Creator.DeleteObject(CursorCoord.ObjectID);
        sgworld.Window.SetInputMode(0);
        sgworld.DetachEvent("OnLButtonDown", DelPointOLB()); return true;
    } else { return false; }
}
function CreatePointOLB() {//添加障碍点 左击事件
    var sgworld = CreateSGWorld();
    i = i + 1; //为障碍点区分编号 方便管理
    if (edit != 0) {
        try {
            var mouseInfo = sgworld.Window.GetMouseInfo()
            var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
            if (CursorCoord == null)
            { return false; } else {
                var pos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
                var style = sgworld.Creator.CreateLabelStyle(0);
                style.Scale = scale_temp;
                var label = sgworld.Creator.CreateImageLabel(pos, imagerpath_temp, style, sgworld.ProjectTree.FindItem("point"), pointname_temp);
                var message = sgworld.Creator.CreatePopupMessage(popupcap_temp, popuppath_temp, 0, 0, popupheight_temp, popupwidth_temp, 0);
                label.Message.MessageID = message.ID;
            }

        } catch (e) { return false; }
    } return false;
}
function CreatePointORB() {//添加点 右击事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        sgworld.Window.SetInputMode(0);
        edit = 0;
        i = 0;
        sgworld.AttachEvent("OnLButtonDown", CreatePointOLB); //绑定鼠标事件
        sgworld.AttachEvent("OnRButtonUp", CreatePointORB);
        sgworld.DetachEvent("OnLButtonDown", CreatePointOLB);
        sgworld.DetachEvent("OnRButtonUp", CreatePointORB);
    }
}
var imagerpath_temp = ""; var scale_temp = 1; var popupheight_temp = 400; var popupwidth_temp = 400; var popuppath_temp = "http://www.baidu.com"; var popupcap_temp = ""; var pointname_temp = "";
function CreatePointFromWorld(imagepath, scale, popupheight, popupwidth, popuppath, popupcap, pointname) {//手动在地图上添加点
    var sgworld = CreateSGWorld();
    imagerpath_temp = imagepath; scale_temp = scale; popupcap_temp = popupcap; popupheight_temp = popupheight; popuppath_temp = popuppath; popupwidth_temp = popupwidth; pointname_temp = pointname;
    var id = sgworld.ProjectTree.FindItem("point");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("point", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态
    sgworld.AttachEvent("OnLButtonDown", CreatePointOLB); //绑定鼠标事件
    sgworld.AttachEvent("OnRButtonUp", CreatePointORB);
    edit = 1;

} //手动在地图上添加点
function CreatePoint(X, Y, Z, imagepath, scale, popupheight, popupwidth, popuppath, popupcap, pointname) {//利用XY等数据生成点
    var sgworld = CreateSGWorld();
    imagerpath_temp = imagepath; scale_temp = scale; popupcap_temp = popupcap; popupheight_temp = popupheight; popuppath_temp = popuppath; popupwidth_temp = popupwidth; pointname_temp = pointname;
    var id = sgworld.ProjectTree.FindItem("point");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("point", 0); }
    var pos = sgworld.Creator.CreatePosition(X, Y, Z, 2, 0, 0, 0, 0);
    var style = sgworld.Creator.CreateLabelStyle(0);
    style.Scale = scale;
    var label = sgworld.Creator.CreateImageLabel(pos, imagepath, style, sgworld.ProjectTree.FindItem("point"), pointname);
    var message = sgworld.Creator.CreatePopupMessage(popupcap, popuppath, 0, 0, popupheight, popupwidth, 0);
    label.Message.MessageID = message.ID;
    sgworld.Navigate.FlyTo(label.ID);
} //利用XY等数据生成点
function CreateLabelOLB() {//添加障碍点 左击事件
    var sgworld = CreateSGWorld();
    i = i + 1; //为障碍点区分编号 方便管理
    if (edit != 0) {
        try {
            var mouseInfo = sgworld.Window.GetMouseInfo()
            var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
            if (CursorCoord == null)
            { return false; } else {
                var pos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
                var style = sgworld.Creator.CreateLabelStyle(0);
                style.Scale = scale_temp;
                var label = sgworld.Creator.CreateImageLabel(pos, imagerpath_temp, style, sgworld.ProjectTree.FindItem("temp"), pointname_temp);
                var message = sgworld.Creator.CreatePopupMessage(popupcap_temp, popuppath_temp, 0, 0, popupheight_temp, popupwidth_temp, 0);
                label.Message.MessageID = message.ID;
                //var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
                //var point = sgworld.Creator.CreateImageLabel(pos, "http://172.16.2.147/indexIcon.png", cLabelStyle, sgworld.ProjectTree.FindItem("temp"), "label" + i);
                //var message = sgworld.Creator.CreatePopupMessage("默认标题", "http://www.baidu.com");
                //point.Message.MessageID = message.ID;

            }

        } catch (e) { return false; }
    } return false;
}
function CreateLabelORB() {//添加点 右击事件
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        sgworld.Window.SetInputMode(0);
        edit = 0;
        i = 0;
        sgworld.DetachEvent("OnLButtonDown", CreatelabelOLB);
        sgworld.DetachEvent("OnRButtonUp", CreatelabelORB);
    }
}
function CreateLabelFromWorld(imagepath, scale, popupheight, popupwidth, popuppath, popupcap, pointname) {
    var sgworld = CreateSGWorld();
    imagerpath_temp = imagepath; scale_temp = scale; popupcap_temp = popupcap; popupheight_temp = popupheight; popuppath_temp = popuppath; popupwidth_temp = popupwidth; pointname_temp = pointname;
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态
    sgworld.AttachEvent("OnLButtonDown", CreateLabelOLB); //绑定鼠标事件
    sgworld.AttachEvent("OnRButtonUp", CreateLabelORB);
    edit = 1;
}
//三维创建标注的方法
function createLabel(X, Y, Z, imagepath, popupheight, popupwidth, popuppath, popupcap, pointname) {//利用XY等数据生成点

    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("lable");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("lable", 0); }
    var id = sgworld.ProjectTree.FindItem("lable");
    if (id > 0) {
    } else { id = sgworld.ProjectTree.CreateGroup("lable", 0); }
    var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);

    var pos = sgworld.Creator.CreatePosition(X, Y, Z, 0, 0, 0, 0, 0);
    var img = sgworld.Creator.CreateImageLabel(pos, imagepath, cLabelStyle, id, pointname);
    var mes = sgworld.Creator.CreatePopupMessage(popupcap, popuppath, 0, 0, popupwidth, popupheight, -1);


    img.Tooltip.Text = popupcap;
    sgworld.Navigate.FlyTo(img.ID);
    img.Message.MessageID = mes.ID;
} //利用XY等数据生成点

function clearlabel() {
    try {
        flag_mouse = "";
        var id = sgworld.ProjectTree.FindItem("lable"); if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
    } catch (e) { }

}
function CancelMapState() {
    var sgworld = CreateSGWorld();
    sgworld.Window.SetInputMode(0);
    edit = 0;
    i = 0;

    sgworld.DetachEvent("OnLButtonDown", drawBarriers); //绑定鼠标事件
    sgworld.DetachEvent("OnRButtonUp", endBarriers);
    sgworld.DetachEvent("OnLButtonDown", EnvelopeQueryOLB);
    sgworld.DetachEvent("OnLButtonUp", EnvelopeQueryORB);
    sgworld.DetachEvent("OnFrame", EnvelopeQueryOF);
    sgworld.DetachEvent("OnLButtonDown", BufferQueryOLB);
    sgworld.DetachEvent("OnRButtonUp", BufferQueryORB);
    sgworld.DetachEvent("OnFrame", BufferQueryOF);
    sgworld.DetachEvent("OnFrame", LoadLableOF);
    sgworld.DetachEvent("OnLButtonDown", DelPointOLB);
    sgworld.DetachEvent("OnLButtonDown", CreatePointOLB); //绑定鼠标事件
    sgworld.DetachEvent("OnRButtonUp", CreatePointORB);
    sgworld.DetachEvent("OnLButtonDown", CreateLabelOLB); //绑定鼠标事件
    sgworld.DetachEvent("OnRButtonUp", CreateLabelORB);
    sgworld.DetachEvent("OnLButtonDown", CreatePointOLB); //绑定鼠标事件
    sgworld.DetachEvent("OnRButtonUp", CreatePointORB);
    sgworld.DetachEvent("OnLButtonDown", drawOLB); //绑定鼠标事件
    sgworld.DetachEvent("OnRButtonUp", drawORB);

    sgworld.DetachEvent("OnLButtonDown", map_Lclick);

    sgworld.DetachEvent("OnLButtonDown", onlbt);
    sgworld.DetachEvent("OnRButtonUp", onrbt);
    sgworld.DetachEvent("OnFrame", onframe);

}
function Load(path) {
    var sgworld = CreateSGWorld();
    try {
        sgworld.Project.Open(path);
    } catch (e) {
        alert("文件未正确打开 请检查地址");
    }

}
var xmlDoc = null;
function LoadImage() {//readxml
    xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
    xmlDoc.async = false;
    xmlDoc.load("URL.xml");
    if (xmlDoc == null) {
        alert('您的浏览器不支持xml文件读取,于是本页面禁止您的操作,推荐使用IE5.0以上可以解决此问题!');
        window.location.href = '/Index.aspx';
        return;
    }
    if (xmlDoc.parseError.errorCode != 0) {
        alert(xmlDoc.parseError.reason);
        return;
    }
    //获得根接点
    var nodes = xmlDoc.documentElement.childNodes;
    //得到根接点下共有子接点个数，并循环
    var URL, XMin, YMin, XMax, YMax, name;
    for (var i = 0; i < nodes.length; i++) {
        if (nodes(i).nodeName == "ShunDe05m") {
            //readTree(nodes(i));
            XMin = nodes(i).childNodes(1).text;
            YMin = nodes(i).childNodes(2).text;
            XMax = nodes(i).childNodes(3).text;
            YMax = nodes(i).childNodes(4).text;
            URL = nodes(i).childNodes(0).text + "/WMSServer?request=GetMap&Version=1.1.1&Service=WMS&SRS=EPSG:4326&BBOX=" + XMin + "," + YMin + "," + XMax + "," + YMax + "&HEIGHT=216&WIDTH=256&Layers=1&Styles=&Format=image/jpeg/tif&TRANSPARENT=true&BGCOLOR=0x000000\r\nxul=" + XMin + "\r\nylr=" + YMin + "\r\nxlr=" + XMax + "\r\nyul=" + YMax + "\r\nWKT=GEOGCS['WGS 84',DATUM['WGS_1984',SPHEROID['WGS 84',6378137,298.257223563,AUTHORITY['EPSG','7030']],AUTHORITY['EPSG','6326']],PRIMEM['Greenwich',0,AUTHORITY['EPSG','8901']],UNIT['degree',0.01745329251994328,AUTHORITY['EPSG','9122']],AUTHORITY['EPSG','4326']]";
            name = nodes(i).nodeName;
        }
    }
    LoadWMS(URL, XMin, YMin, XMax, YMax, name);
}
function LoadWMS(url, minx, miny, maxx, maxy, name) {
    var id = sgworld.ProjectTree.FindItem("wms");
    if (id > 0) {
    } else { id = sgworld.ProjectTree.CreateGroup("wms", 0); }
    var SGWorld = CreateSGWorld();
    var wmsStr = "[INFO]";
    wmsStr += "\r\nMeters=0";
    wmsStr += "\r\nMPP=5.3644180297851562E-06";
    wmsStr += "\r\nUrl= " + url;
    wmsStr += "\r\nxul=" + minx;
    wmsStr += "\r\nylr=" + miny;
    wmsStr += "\r\nxlr=" + maxx;
    wmsStr += "\r\nyul=" + maxy;
    var rastlayer = SGWorld.Creator.CreateImageryLayer("wms", minx, maxy, maxx, miny, "<EXT><ExtInfo><![CDATA[" + wmsStr + "]]></ExtInfo><ExtType>wms</ExtType></EXT>", "gisplg.rct", id, name);
    //rastlayer.Position.AltitudeType = 0;
    rastlayer.UseNull = true;
    rastlayer.NullValue = 0;
    SGWorld.Navigate.FlyTo(rastlayer.ID);
}
function LoadWFS(url, servicename, LyaerNames) {

    var n1 = url.toUpperCase().lastIndexOf('/SERVICES/') + 10;
    var n2 = url.toUpperCase().lastIndexOf('/MAPSERVER');
    var serviceName = url.substring(n1, n2);

    var layers = LyaerNames.split(",");
    for (var i = 0; i < layers.length; i++) {
        var layername = layers[i];
        if (layername != "") {
            var tConnectionString = "TEPlugName=WFS;Server=" + url + "/WFSServer;WFSVersion=1.1.0;LayerName=" + serviceName + ":" + layername + ";CRS_XY_OR_YX=2;";

            var flayer = SGWorld.Creator.CreateFeatureLayer(servicename, tConnectionString, 0);
            //flayer.Position.AltitudeType = 0;
            var cFeatureLayerDataSource = flayer.DataSourceInfo;
            //                ifdsCou (cFeatureLayerDataSource.Attributes.Recornt != undefined) {
            //                    var cAttributes = cFeatureLayerDataSource.Attributes;
            //cFeatureLayerDataSource.Attributes.ImportAll = true;
            var itemid = SGWorld.ProjectTree.FindItem(servicename);
            if (itemid != 0) {
                flayer.Streaming = false;
                flayer.Load();
                var postition = SGWorld.ProjectTree.GetGroupLocation(itemid);
                postition.Distance = 250000;
                SGWorld.Navigate.FlyTo(postition, 0);
            }
        }
    }
}

function draw() {
    lujingdian_arr = [];
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("point");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("point", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态
    sgworld.AttachEvent("OnLButtonDown", drawOLB); //绑定鼠标事件
    sgworld.AttachEvent("OnRButtonUp", drawORB);
    edit = 1;
}
function drawclear() {
    try {
        flag_mouse = "";
        var id = sgworld.ProjectTree.FindItem("temp_draw"); if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
    } catch (e) { }
}                   //路径分析清除事件
var lujingdian_arr;
function drawORB() {
    var sgworld = CreateSGWorld();
    if (edit != 0) {
        sgworld.Window.SetInputMode(0);
        edit = 0;
        i = 0;
        sgworld.AttachEvent("OnLButtonDown", drawOLB); //绑定鼠标事件
        sgworld.AttachEvent("OnRButtonUp", drawORB);
        sgworld.DetachEvent("OnLButtonDown", drawOLB); //绑定鼠标事件
        sgworld.DetachEvent("OnRButtonUp", drawORB);
    }
}
function drawOLB() {
    if (edit != 0) {
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        if (CursorCoord == null)
        { return false; } else {
            lujingdian_arr.push(CursorCoord.Position.x + "," + CursorCoord.Position.y + ";");
            var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
            var pos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 0, 0, 0, 0, 0);
            var path = iisurl + "lecong_GeoSP/geosp/images/起.png";
            if (lujingdian_arr.length == 2) {
                path = iisurl + "lecong_GeoSP/geosp/images/终.png";
            }
            var id = sgworld.ProjectTree.FindItem("temp_draw");
            if (id > 0) {
            } else { sgworld.ProjectTree.CreateGroup("temp_draw", 0); }
            var id = sgworld.ProjectTree.FindItem("temp_draw");
            var img = sgworld.Creator.CreateImageLabel(pos, path, cLabelStyle, id, "guihua" + lujingdian_arr.length); //a

        }
        url();
    }
}
function url() {

    var text = "";
    if (lujingdian_arr.length < 2) { return; }
    else {
        for (var i = 0; i < lujingdian_arr.length; i++) {
            text = text + lujingdian_arr[i];
        }
        var url = text.substring(0, text.length - 1);

        lujing(url);
    }
}
function routAnalysis3d(startX, startY, endX, endY) {
    var str = startX + "," + startY + ";" + endX + "," + endY;
    lujing(str);
    var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
    var posstart = sgworld.Creator.CreatePosition(startX, startY, 0, 0, 0, 0, 0, 0);
    var posend = sgworld.Creator.CreatePosition(endX, endY, 0, 0, 0, 0, 0, 0);
    var pathstart = iisurl + "lecong_GeoSP/geosp/images/起.png"

    var pathend = iisurl + "lecong_GeoSP/geosp/images/终.png";

    var id = sgworld.ProjectTree.FindItem("temp_draw");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("temp_draw", 0); }
    var id = sgworld.ProjectTree.FindItem("temp_draw");
    sgworld.Creator.CreateImageLabel(posstart, pathstart, cLabelStyle, id, "guihua1"); //a
    sgworld.Creator.CreateImageLabel(posend, pathend, cLabelStyle, id, "guihua2"); //a

}
function lujing(str) {
    var sgworld = CreateSGWorld();

    var url = routeurl + "/solve?stops=" + str + "&barriers=&polylineBarriers=&polygonBarriers=&outSR=&ignoreInvalidLocations=true&accumulateAttributeNames=&impedanceAttributeName=Length&restrictionAttributeNames=Oneway&attributeParameterValues=&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=false&returnDirections=true&returnRoutes=true&returnStops=false&returnBarriers=false&returnPolylineBarriers=false&returnPolygonBarriers=false&directionsLanguage=zh-CN&directionsStyleName=&outputLines=esriNAOutputLineTrueShapeWithMeasure&findBestSequence=false&preserveFirstStop=false&preserveLastStop=false&useTimeWindows=false&startTime=0&outputGeometryPrecision=&outputGeometryPrecisionUnits=esriDecimalDegrees&directionsOutputType=esriDOTComplete&directionsTimeAttributeName=&directionsLengthUnits=esriNAUMeters&returnZ=false&f=pjson";

    jQuery.support.cors = true;
    $.ajax({

        url: url,
        success: function aa(data, textStatus, jqXHR) {
            var json = data;

            if (json != "") {
                try {
                    var obj = eval("(" + json + ")"); //转换后的JSON对象
                    var arr_temp = []; var totle = obj.routes.features[0].attributes.Total_Length; totle = parseInt(totle);

                    for (var i = 0; i < obj.routes.features[0].geometry.paths[0].length; i++) {
                        arr_temp.push(obj.routes.features[0].geometry.paths[0][i][0]);
                        arr_temp.push(obj.routes.features[0].geometry.paths[0][i][1]);
                        arr_temp.push(0);
                    }
                    var str1 = "";
                    for (var i = 0; i < obj.directions[0].features.length; i++) {
                        var txt = obj.directions[0].features[i].attributes.text;
                        var chang = obj.directions[0].features[i].attributes.length;
                        var chang = parseInt(chang * 1); var ss = i + 1;
                        txt = txt.replace('Location 1', "起点");

                        if (i == obj.directions[0].features.length - 1) {
                            var num = str.split(';').length;
                            txt = txt.replace('Location ' + num, "");
                        }
                        if (chang == 0) { str1 += ss + "、" + txt + "\r\n"; } else {
                            str1 += ss + "、" + txt + chang + "米" + "\r\n";
                        }

                    }
                    alert(str1);
                    var id = sgworld.ProjectTree.FindItem("temp_draw\\路径规划");
                    if (id > 0) {
                    } else { sgworld.ProjectTree.DeleteItem(id); } //查看temp文件夹是否存在 否则创建
                    var id = sgworld.ProjectTree.FindItem("temp_draw");
                    if (id > 0) {
                    } else { sgworld.ProjectTree.CreateGroup("temp_draw", 0); } //查看temp文件夹是否存在 否则创建
                    var cPolyline = sgworld.Creator.CreatePolylineFromArray(arr_temp, sgworld.Creator.CreateColor(0, 255, 0, 1), 2, sgworld.ProjectTree.FindItem("temp_draw"), "路径规划")
                    var color = sgworld.Creator.CreateColor(255, 0, 0, 255);
                    cPolyline.LineStyle.Color.FromABGRColor(color.abgrColor);
                    cPolyline.LineStyle.Color.SetAlpha(0.5);
                    cPolyline.LineStyle.Width = 15;
                    sgworld.Navigate.FlyTo(cPolyline.ID);

                } catch (e) {
                }
            }
        },
        dataType: "text",
        error: function bb(jqXHR, textStatus, errorThrown) {
            alert("请求数据失败！" + textStatus);
        }

    });
    sgworld.Window.SetInputMode(0);
    edit = 0;
} //路径分析服务访问事件
function cleardraw() {
    try {
        flag_mouse = "";
        var id = sgworld.ProjectTree.FindItem("temp_draw"); if (id > 0) {
            sgworld.ProjectTree.DeleteItem(id);
        }
        sgworld.Window.SetInputMode(0);
        sgworld.DetachEvent("OnLButtonDown", drawOLB);
        sgworld.DetachEvent("OnRButtonUp", drawORB);

    } catch (e) { }
}                   //路径分析清除事件
function json(json) {
    if (json != null) {
        try {
            var obj = eval("(" + json + ")"); //转换后的JSON对象
            //Text = obj.geometry;
            var arr_temp = [];
            for (var i = 0; i < obj.routes.features[0].geometry.paths[0].length; i++) {
                arr_temp.push(obj.routes.features[0].geometry.paths[0][0][0]);
                arr_temp.push(obj.routes.features[0].geometry.paths[0][0][1]);
                arr_temp.push(0);
            }
            var id = sgworld.ProjectTree.FindItem("temp\\路径规划");
            if (id > 0) {
            } else { sgworld.ProjectTree.DeleteItem(id); } //查看temp文件夹是否存在 否则创建
            var id = sgworld.ProjectTree.FindItem("temp");
            if (id > 0) {
            } else { sgworld.ProjectTree.CreateGroup("temp", 0); } //查看temp文件夹是否存在 否则创建
            alert(arr_temp);
            var cPolyline = sgworld.Creator.CreatePolylineFromArray(arr_temp, sgworld.Creator.CreateColor(0, 255, 0, 1), 2, sgworld.ProjectTree.FindItem("temp"), "路径规划")
            sgworld.Navigate.FlyTo(cPolyline.ID);
        } catch (e) { alert(json); }
    }

}
var quanxian = 0;
function download() {
    try {
        var wsh = new ActiveXObject("WScript.shell");
        wsh.RegDelete("HKEY_CURRENT_USER\\Software\\Skyline\\TerraExplorer\\Settings\\");
        wsh.RegWrite("HKEY_CURRENT_USER\\Software\\Skyline\\TerraExplorer\\Settings\\", "", "REG_SZ");
    } catch (e) {


        if (quanxian == 0) { $("#xiazai").attr("href", "http://192.168.14.91/setup.exe"); }
        else if (quanxian == 1) {
            $("#xiazai").attr("href", "http://localhost/iisstart.htm");
        }
    }
}
var logon = function () {
    try {
        DetectTE(SGVersijiieon);
    }
    catch (e) {
    }
}
function EnterSkylineGlobe(res) {
    try {
        if (res == 0) // we are ok we can start SkylineGlobe
        {
            if (g_UseTerraExplorer) {
                window.location.href = g_UrlIE;
            }
            else {
                tIEVersion = msieversion();
                // xploveu
                if (tIEVersion != 0)
                //window.open(g_UrlIE + g_Param, g_Target);    // IE version
                    alert("you have installed pro, open 3d application url use ie");
                else
                //window.open(g_UrlFF + g_Param, "_self");     // Other browsers version
                    alert("you have installed pro, open 3d application url use  Other browsers");
            }
        }
        if (res == -100) // we need to download SkylineGlobe
        {
            if (g_TerrainMode == "3D") // if 3d required, download TE, otherwise start main page. It will select 2D.
            {
                SGDownload("o=Enter");
            }
            else // start 2D mode
            {
                SGStart({ TerrainMode: "2D", Target: g_Target, Param: g_Param })
            }
        }
        if (res == -102) // we need to download SkylineGlobe, but could not detect if TE installed
        {
            if (g_TerrainMode == "3D") // if 3d required, download TE, otherwise start main page. It will select 2D.
            {
                SGDownload("o=Enter&noDetect=1");
            }
            else // start 2D mode
            {
                SGStart({ TerrainMode: "2D", Target: g_Target, Param: g_Param })
            }
        }
        if (res == -101) // we need to update SkylineGlobe
            SGDownload("o=Update");
    }
    catch (e) {
        alert("site " + e.description);
    }
}
function DeleteTemp(name) {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
        var child_id = sgworld.ProjectTree.GetNextItem(id, 11);
        while (sgworld.ProjectTree.GetNextItem(child_id, 15) == id) {
            var obj_name = sgworld.ProjectTree.GetItemName(child_id);
            if (obj_name == name) { sgworld.ProjectTree.DeleteItem(child_id); return; } else { child_id = sgworld.ProjectTree.GetNextItem(child_id, 13) }
        } alert("未找到对象，删除失败！");
    } else { return; }
}
function Hide_Temp(name) {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
        var child_id = sgworld.ProjectTree.GetNextItem(id, 11);
        while (sgworld.ProjectTree.GetNextItem(child_id, 15) == id) {
            var obj_name = sgworld.ProjectTree.GetItemName(child_id);
            if (obj_name == name) { sgworld.ProjectTree.SetVisibility(child_id, false); return; } else { child_id = sgworld.ProjectTree.GetNextItem(child_id, 13) }
        } alert("未找到对象，隐藏失败！");
    } else { return; }
}
function Show_Temp(name) {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("temp");
    if (id > 0) {
        var child_id = sgworld.ProjectTree.GetNextItem(id, 11);
        while (sgworld.ProjectTree.GetNextItem(child_id, 15) == id) {
            var obj_name = sgworld.ProjectTree.GetItemName(child_id);
            if (obj_name == name) { sgworld.ProjectTree.SetVisibility(child_id, true); return; } else { child_id = sgworld.ProjectTree.GetNextItem(child_id, 13) }
        } alert("未找到对象，显示失败！");
    } else { return; }
}
function CreateArrow(x1, y1, x2, y2) {
    var sgworld = CreateSGWorld();
    var posFrom = sgworld.Creator.CreatePosition(x1, y1, 0, 2, 0, 0, 0, 0);
    var posTo = sgworld.Creator.CreatePosition(x2, y2, 0, 2, 0, 0, 0, 0);
    var Length = posFrom.DistanceTo(posTo);
    posFrom = posFrom.AimTo(posTo);
    posFrom.X = posTo.X; posFrom.Y = posTo.Y;
    if (Length < 1000) {

        sgworld.Creator.CreateArrow(posFrom, Length, 1, "0000ff", "804000", sgworld.ProjectTree.FindItem("LoadLable"), "Arrow");
    } else {
        sgworld.Creator.CreateArrow(posFrom, 1000, 1, "0000ff", "804000", sgworld.ProjectTree.FindItem("LoadLable"), "Arrow");
    }
}
function map_click() {
    var sgworld = CreateSGWorld();
    sgworld.Window.SetInputMode(1);
    sgworld.AttachEvent("OnLButtonDown", map_Lclick);
}
function map_Lclick() {
    var sgworld = CreateSGWorld();
    var mouseInfo = sgworld.Window.GetMouseInfo();
    var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);

    alert("X:" + CursorCoord.Position.x + "--Y:" + CursorCoord.Position.y);
    try { OnEndmap_click(); } catch (e) { }
    return true;
}
//清除地图单击状态
function clearmapclick() {
    sgworld.Window.SetInputMode(0);
    sgworld.DetachEvent("OnLButtonDown", map_Lclick); //清除鼠标事件
}
function measure() {
    var sgworld = CreateSGWorld();
    var id = sgworld.ProjectTree.FindItem("Measure");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("Measure", 0); }
    sgworld.Window.SetInputMode(1); //设置鼠标状态
    edit = 1;
    sgworld.AttachEvent("OnLButtonDown", MeasureOLB);
    sgworld.AttachEvent("OnRButtonUp", MeasureORB);
    sgworld.AttachEvent("OnFrame", MeasureOF);

}
var arr_Measure = []; var i_Measure = 0; var last_pos = null;
function MeasureOLB() {

    var sgworld = CreateSGWorld();
    if (edit != 0) {
        var id = sgworld.ProjectTree.FindItem("Measure");
        if (id > 0) {
        } else { sgworld.ProjectTree.CreateGroup("Measure", 0); }
        var mouseInfo = sgworld.Window.GetMouseInfo()
        var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
        var pos = sgworld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0); //获取位置

        var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
        var point = sgworld.Creator.CreateImageLabel(pos, iisurl + "Samples/arcgis/Images/MeasurePoint.png", cLabelStyle, sgworld.ProjectTree.FindItem("Measure"), "Measure" + i_Measure);

        if (last_pos != null) {
            var R = "" + last_pos.DistanceTo(pos) + "";
            R = baoliuxiaoshu(R);
            var X_middle = (pos.X + last_pos.X) / 2; var Y_middle = (pos.Y + last_pos.Y) / 2;
            var pos_middle = sgworld.Creator.CreatePosition(X_middle, Y_middle, 0, 0, 0, 0, 0, 0);
            sgworld.Creator.CreateTextLabel(pos_middle, R, cLabelStyle, sgworld.ProjectTree.FindItem("Measure"), "R" + i_Measure);
            i_Measure = i_Measure + 1;
        }
        last_pos = pos; //画出点出点以及两点中间显示距离

        arr_Measure.push(CursorCoord.Position.x);
        arr_Measure.push(CursorCoord.Position.y);
        arr_Measure.push(0);
        // sgworld.Creator.CreatePolylineFromArray(arr_Measure, "-16711936", 2, sgworld.ProjectTree.FindItem("Measure"), "temp_Measure");
    }
}
function MeasureORB() {
    var sgworld = CreateSGWorld();

    i_Measure = 0; last_pos = null; arr_Measure = []; edit = 0;

    sgworld.AttachEvent("OnLButtonDown", MeasureOLB);
    sgworld.AttachEvent("OnRButtonUp", MeasureORB);
    sgworld.AttachEvent("OnFrame", MeasureOF);

    return true;
}
function clearMeasure() {
    var id = sgworld.ProjectTree.FindItem("Measure");
    if (id > 0) {
        sgworld.ProjectTree.DeleteItem(id);
    }
    sgworld.Window.SetInputMode(0);
    sgworld.DetachEvent("OnLButtonDown", MeasureOLB);
    sgworld.DetachEvent("OnRButtonUp", MeasureORB);
    sgworld.DetachEvent("OnFrame", MeasureOF);
}
function MeasureOF() {
    var sgworld = CreateSGWorld();

    var mouseInfo = sgworld.Window.GetMouseInfo()
    var CursorCoord = sgworld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
    if (edit != 0 && arr_Measure.length > 0) {
        var id = sgworld.ProjectTree.FindItem("Measure\\temp_Measure");
        if (id > 0) { sgworld.ProjectTree.DeleteItem(id); }
        arr_Measure.push(CursorCoord.Position.x);
        arr_Measure.push(CursorCoord.Position.y);
        arr_Measure.push(0);
        sgworld.Creator.CreatePolylineFromArray(arr_Measure, "-16711936", 2, sgworld.ProjectTree.FindItem("Measure"), "temp_Measure");
        arr_Measure.pop();
        arr_Measure.pop();
        arr_Measure.pop();
    }
}
var editmode = 0;
function MeasurePolygon() {
    var SGWorld = CreateSGWorld();
    SGWorld.Window.SetInputMode(1);
    editmode = 3;
    SGWorld.AttachEvent("OnLButtonDown", onlbt);
    SGWorld.AttachEvent("OnRButtonUp", onrbt);
    SGWorld.AttachEvent("OnFrame", onframe);
}
function onframe() {
    var SGWorld = CreateSGWorld();
    if (editmode != 0) {
        if (gPolyObj != null) {
            try {
                var mouseInfo = SGWorld.Window.GetMouseInfo()
                var CursorCoord = SGWorld.Window.pixelToWorld(mouseInfo.X, mouseInfo.Y);
                if (CursorCoord == null)
                    return false;
                if (gPolyObj.ObjectType == 1) {
                    SGWorld.Creator.DeleteObject(gPolyObj.ID);
                    var cVerticesArray = [startpos.X, startpos.Y, 0, CursorCoord.Position.x, CursorCoord.Position.y, 0];
                    gPolyObj = SGWorld.Creator.CreatePolylineFromArray(cVerticesArray, SGWorld.Creator.CreateColor(0, 255, 0, 1), 2, 0, "temp")

                } else
                    if (gPolyObj.ObjectType == 2) {
                        gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).X = CursorCoord.Position.x;
                        gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).Y = CursorCoord.Position.y;
                        gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).Z = 0;
                    }
                    else {

                        gPolyObj.Geometry.Points.Item(gPolyObj.Geometry.Points.count - 1).X = CursorCoord.Position.x;
                        gPolyObj.Geometry.Points.Item(gPolyObj.Geometry.Points.count - 1).Y = CursorCoord.Position.y;
                        gPolyObj.Geometry.Points.Item(gPolyObj.Geometry.Points.count - 1).Z = 0;
                    }
            }
            catch (e) { }
        }
    } else { return false; }
}
var gPolyObj = null; var startpos = null;
function onlbt(Flags, X, Y) {
    var SGWorld = CreateSGWorld();
    if (editmode != 0) {
        var CursorCoord = SGWorld.Window.pixelToWorld(X, Y);
        if (CursorCoord == null)
            return false;


        if (gPolyObj == null) {
            // We always start with a polyline and change it to Polygon (for area) after the second click)
            startpos = SGWorld.Creator.CreatePosition(CursorCoord.Position.x, CursorCoord.Position.y, 0, 2, 0, 0, 0, 0);
            var myGeometry = SGWorld.Creator.GeometryCreator.CreateLineStringGeometry([CursorCoord.Position.x, CursorCoord.Position.y, 0, CursorCoord.Position.x, CursorCoord.Position.y, 0])
            gPolyObj = SGWorld.Creator.createPolyline(myGeometry, SGWorld.Creator.CreateColor(0, 255, 0, 1), 2, 0, "temp");
            gPolyObj.LineStyle.Width = 1;
            gPolyObj.Geometry.StartEdit();
        }
        else {
            if (gPolyObj.ObjectType == 1) {
                // Deleting the temporary line
                var x = gPolyObj.Geometry.Points.Item(0).X;
                var y = gPolyObj.Geometry.Points.Item(0).Y;
                SGWorld.Creator.DeleteObject(gPolyObj.ID);
                // Creating the polygon
                var myGeometry = SGWorld.Creator.GeometryCreator.CreateLinearRingGeometry([x, y, 0, CursorCoord.Position.x, CursorCoord.Position.y, 0, CursorCoord.Position.x, CursorCoord.Position.y, 0])
                gPolyObj = SGWorld.Creator.createPolygon(myGeometry, SGWorld.Creator.CreateColor(0, 255, 0, 1), SGWorld.Creator.CreateColor(0, 255, 0, 0.5), 2, 0, "temp");
                gPolyObj.LineStyle.Width = 1;
                gPolyObj.Terrain.GroundObject = true;
                gPolyObj.Geometry.StartEdit();
            }
            else {
                gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).X = CursorCoord.Position.x;
                gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).Y = CursorCoord.Position.y;
                gPolyObj.Geometry.Rings(0).Points.Item(gPolyObj.Geometry.Rings(0).Points.count - 1).Z = 0;
                gPolyObj.Geometry.Rings(0).Points.AddPoint(CursorCoord.Position.x, CursorCoord.Position.y, 0);
            }

        }
    } else { return false; }
}
function onrbt(Flags, X, Y) {
    var SGWorld = CreateSGWorld();
    if (gPolyObj == null || ((gPolyObj.ObjectType == 1 && gPolyObj.Geometry.Points.count <= 2) || (gPolyObj.ObjectType == 2 && gPolyObj.Geometry.Rings(0).Points.count <= 3))) {
        //                Reset(0, 0);
        return false;
    }
    if (gPolyObj.ObjectType == 1)
        gPolyObj.Geometry.Points.DeletePoint(gPolyObj.Geometry.Points.count - 1);
    else {
        gPolyObj.Geometry.Rings(0).Points.DeletePoint(gPolyObj.Geometry.Rings(0).Points.count - 1);
    }
    editmode = 0;
    gPolyObj.Geometry.EndEdit();
    SGWorld.Window.SetInputMode(0);
    var area = gPolyObj.Geometry.area;
    SGWorld.Creator.DeleteObject(gPolyObj.ID);
    startpos = null;
    gPolyObj = null;
    gPolyMethod = null;
    SGWorld.AttachEvent("OnLButtonDown", onlbt);
    SGWorld.AttachEvent("OnRButtonUp", onrbt);
    SGWorld.AttachEvent("OnFrame", onframe);
    SGWorld.DetachEvent("OnLButtonDown", onlbt);
    SGWorld.DetachEvent("OnRButtonUp", onrbt);
    SGWorld.DetachEvent("OnFrame", onframe);
    getArea(area);
    //  try {
    OnEndMeasurePolygon();
    //  } catch (e) { }
    return true;
}
function ClearImage(name) {
    var id = sgworld.ProjectTree.FindItem("wms");
    if (id > 0) {
        var child_id = sgworld.ProjectTree.GetNextItem(id, 11);
        while (sgworld.ProjectTree.GetNextItem(child_id, 15) == id) {
            var obj_name = sgworld.ProjectTree.GetItemName(child_id);
            if (obj_name == name) { sgworld.ProjectTree.DeleteItem(child_id); return; } else { child_id = sgworld.ProjectTree.GetNextItem(child_id, 13) }
        } alert("未找到对象，删除失败！");
    } else { alert("未找到对象，删除失败！"); }
}
function baoliuxiaoshu(num) {
    var arr = num.split('.');
    var other = "";
    if (arr.length > 1) {
        if (arr[1].length > 1) { other = arr[1].substring(0, 2); }
        else { other = arr[1]; }
    }
    other = arr[0] + "." + other;
    return other;

}
var last_X = -1, last_Y = -1; var Area = -1;
function GetMap_click(x, y) {
    if (x != null && y != null) {
        last_X = x; last_Y = y;
    } else {
        if (last_X == -1 && last_Y != -1) {
            return;
        }
    }
    return [last_X, last_Y];
}
function getArea(area) {
    if (area != null) { Area = area; }
    else { if (Area == -1) { return; } }
    return Area;
}
//模型展示
function modelShow(gridid) {
    var id = sgworld.ProjectTree.FindItem("乐从建筑物精细模型\\" + gridid);
    sgworld.ProjectTree.SetVisibility(id, true);
    sgworld.Navigate.FlyTo(id);
    var ItemID = sgworld.ProjectTree.FindItem("shiliang\\lecong_poly");
    sgworld.ProjectTree.SetVisibility(ItemID, false);
}
//隐藏模型
function modeHied(gridid) {
    var id = sgworld.ProjectTree.FindItem("乐从建筑物精细模型\\" + gridid);
    sgworld.ProjectTree.SetVisibility(id, false);

    sgworld.ProjectTree.SetVisibility(sgworld.ProjectTree.FindItem("camera"), false);
}



function OnError(XMLHttpRequest, textStatus, errorThrown) {
    targetDiv = $("#data");
    if (errorThrown || textStatus == "error" || textStatus == "parsererror" || textStatus == "notmodified") {
        targetDiv.replaceWith("请求数据时发生错误！");
        return;
    }
    if (textStatus == "timeout") {
        targetDiv.replaceWith("请求数据超时！");
        return;
    }
}
//创建圆 
function createcircle3d(dXCoord, dYCoord, dCircleRadius) {
    var dAltitude = 100.0;
    var eAltitudeTypeCode = 0; //AltitudeTypeCode.ATC_TERRAIN_RELATIVE;
    var dYaw = 0.0;
    var dPitch = 0.0;
    var dRoll = 0.0;
    var dDistance = 2000;
    //Create Position
    var cPos = sgworld.Creator.CreatePosition(dXCoord, dYCoord, dAltitude, eAltitudeTypeCode, dYaw, dPitch, dRoll, dDistance);

    var nRed = 0;
    var nGreen = 0;
    var nBlue = 255;
    var nAlpha = 0x7F; // 50% opacity 
    var cFillColor = sgworld.Creator.CreateColor(nRed, nGreen, nBlue, nAlpha);
    var nLineColor = sgworld.Creator.CreateColor(nRed, nGreen, nBlue); ;   // Abgr value - Solid blue
    var id = sgworld.ProjectTree.FindItem("Circle");
    if (id > 0) {
    } else { sgworld.ProjectTree.CreateGroup("Circle", 0); }
    var id = sgworld.ProjectTree.FindItem("Circle");
    var cCircle = sgworld.Creator.CreateCircle(cPos, dCircleRadius, nLineColor, cFillColor, id, "Circle");
    sgworld.Navigate.FlyTo(id);
}
//清除圆
function clearcircle3d() {
    var id = sgworld.ProjectTree.FindItem("Circle");
    if (id > 0) {
        sgworld.ProjectTree.DeleteItem(id);
    }
}
//模糊查询
function search(words) {
    var info = "FuzzyQuery|" + words;
    $.ajax({
        url: searchurl,
        dataType: "jsonp",
        data: { data: info },
        success: searchResult,
        error: OnError
    });
}
//周边查询
function searchInBuffer(x, y, r, words) {
    var info = "GetPoiByRadius|" + x + "|" + y + "|" + r + "|" + words;
    $.ajax({
        url: searchurl,
        dataType: "jsonp",
        data: { data: info },
        success: searchResult,
        error: OnError
    });

}
//拉框查询
function searchInExtent(minX, minY, maxX, maxY, words) {
    var info = "GetPoiByExtent|" + minX + "|" + minY + "|" + maxX + "|" + maxY + "|" + words;
    $.ajax({
        url: searchurl,
        dataType: "jsonp",
        data: { data: info },
        success: searchResult,
        error: OnError
    });
}
//查询结果
var str = ""
function searchResult(ret) {
    if (ret.data.length == 0) {
        alert("没有查询到数据！");
    }
    else {
        for (var i = 0; i < ret.data.length; i++) {
            //createLable(ret.data[i].P_x, , 'Images/lable.png', 'http://wiss.cc/', '物联软通', 'labid', '001', 400, 200);
            createLabel(ret.data[i].P_x, ret.data[i].P_y, 1, iisurl + 'lecong_GeoSP/geosp/images/Pin.png', 400, 400, 'http://wiss.cc/', ret.data[i].P_ABName, ret.data[i].P_ABName);
        }
        var id = sgworld.ProjectTree.FindItem("lable");
        if (id != 0) {
            sgworld.Navigate.FlyTo(id);
        }
    }
}

//公交换乘
function busTransfer(startPoint, endPoint) {

    var dataStr = "startPoint=" + startPoint + "&endPoint=" + endPoint;
    $.ajax({
        type: "get",
        url: busUrl,
        dataType: "jsonp",
        jsonp: 'eastdawn',
        data: dataStr,
        success: function (result, textStatus) {
            var res = result.data;

            //返回结果
            var coordinateArray = res[0].CoordinateArray;

            var coordinates = coordinateArray.split("$");
            for (var i = 0; i < coordinates.length; i++) {
                bustransfer(coordinates[i], i, coordinates.length);
            }
            var desc = "";
            desc += transferMethod(result);
            alert(desc);

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("1:" + XMLHttpRequest.status + "2:" + XMLHttpRequest.readyState + "3:" + textStatus + "4:" + errorThrown);
        }

    });
}
function bustransfer(str, fn, tn) {
    jQuery.support.cors = true;
    var path = routeurl + "/solve?stops=" + str + "&barriers=&polylineBarriers=&polygonBarriers=&outSR=&ignoreInvalidLocations=true&accumulateAttributeNames=&impedanceAttributeName=Length&restrictionAttributeNames=Oneway&attributeParameterValues=&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=false&returnDirections=true&returnRoutes=true&returnStops=false&returnBarriers=false&returnPolylineBarriers=false&returnPolygonBarriers=false&directionsLanguage=zh-CN&directionsStyleName=&outputLines=esriNAOutputLineTrueShapeWithMeasure&findBestSequence=false&preserveFirstStop=false&preserveLastStop=false&useTimeWindows=false&startTime=0&outputGeometryPrecision=&outputGeometryPrecisionUnits=esriDecimalDegrees&directionsOutputType=esriDOTComplete&directionsTimeAttributeName=&directionsLengthUnits=esriNAUMeters&returnZ=false&f=pjson";

    $.ajax({
        url: path,
        success: function aa(data, textStatus, jqXHR) {
            var json = data;

            if (json != "") {
                try {
                    var obj = eval("(" + json + ")"); //转换后的JSON对象
                    var arr_temp = []; var totle = obj.routes.features[0].attributes.Total_Length; totle = parseInt(totle);


                    for (var i = 0; i < obj.routes.features[0].geometry.paths[0].length; i++) {
                        arr_temp.push(obj.routes.features[0].geometry.paths[0][i][0]);
                        arr_temp.push(obj.routes.features[0].geometry.paths[0][i][1]);
                        arr_temp.push(0);
                    } //<li><img src="images/1.png" />从location1 <span>开始</span></li>

                    var id = sgworld.ProjectTree.FindItem("temp_draw\\路径规划");
                    if (fn == 0) {
                        if (id > 0) {
                        } else { sgworld.ProjectTree.DeleteItem(id); } //查看temp文件夹是否存在 否则创建
                        var id = sgworld.ProjectTree.FindItem("temp_draw");
                        if (id > 0) {
                        } else { sgworld.ProjectTree.CreateGroup("temp_draw", 0); } //查看temp文件夹是否存在 否则创建
                    }
                    var cPolyline = sgworld.Creator.CreatePolylineFromArray(arr_temp, sgworld.Creator.CreateColor(0, 255, 0, 1), 2, sgworld.ProjectTree.FindItem("temp_draw"), "路径规划")
                    var color = sgworld.Creator.CreateColor(255, 0, 0, 255);
                    cPolyline.LineStyle.Color.FromABGRColor(color.abgrColor);
                    cPolyline.LineStyle.Color.SetAlpha(0.5);
                    cPolyline.LineStyle.Width = 15;
                    //
                    var cLabelStyle = sgworld.Creator.CreateLabelStyle(2);
                    var id = sgworld.ProjectTree.FindItem("temp_draw");
                    if (tn == 1) {
                        var paths = obj.routes.features[0].geometry.paths[0];
                        var startpoint = sgworld.Creator.CreatePosition(paths[0][0], paths[0][1], 0, 0, 0, 0, 0, 0);
                        var endpoint = sgworld.Creator.CreatePosition(paths[paths.length - 1][0], paths[paths.length - 1][1], 0, 0, 0, 0, 0, 0);
                        var path = "";
                        path = iisurl + "lecong_GeoSP/geosp/images/起.png";
                        var img = sgworld.Creator.CreateTextLabel(startpoint, "", cLabelStyle, id, ""); //a
                        img.ImageFileName = path;
                        path = iisurl + "lecong_GeoSP/geosp/images/终.png";
                        img = sgworld.Creator.CreateTextLabel(endpoint, "", cLabelStyle, id, ""); //a
                        img.ImageFileName = path;
                        sgworld.Navigate.FlyTo(id);
                        showitem("draw");
                    }
                    else {
                        if (fn == 0) {

                            id = sgworld.ProjectTree.CreateGroup("temp_draw", 0);
                            var paths = obj.routes.features[0].geometry.paths[0];
                            var startpoint = sgworld.Creator.CreatePosition(paths[0][0], paths[0][1], 0, 0, 0, 0, 0, 0);
                            var endpoint = sgworld.Creator.CreatePosition(paths[paths.length - 1][0], paths[paths.length - 1][1], 0, 0, 0, 0, 0, 0);
                            var path = "";
                            path = iisurl + "lecong_GeoSP/geosp/images/起.png";
                            var img = sgworld.Creator.CreateTextLabel(startpoint, "", cLabelStyle, id, ""); //a
                            img.ImageFileName = path;
                            path = iisurl + "lecong_GeoSP/geosp/images/换.png";
                            img = sgworld.Creator.CreateTextLabel(endpoint, "", cLabelStyle, id, ""); //a
                            img.ImageFileName = path;
                        }
                        else {
                            if (fn == tn - 1) {
                                var paths = obj.routes.features[0].geometry.paths[0];
                                var startpoint = sgworld.Creator.CreatePosition(paths[0][0], paths[0][1], 0, 0, 0, 0, 0, 0);
                                var endpoint = sgworld.Creator.CreatePosition(paths[paths.length - 1][0], paths[paths.length - 1][1], 0, 0, 0, 0, 0, 0);
                                var path = "";
                                path = iisurl + "lecong_GeoSP/geosp/images/换.png";
                                var img = sgworld.Creator.CreateTextLabel(startpoint, "", cLabelStyle, id, ""); //a
                                img.ImageFileName = path;
                                path = iisurl + "lecong_GeoSP/geosp/images/终.png";
                                img = sgworld.Creator.CreateTextLabel(endpoint, "", cLabelStyle, id, ""); //a
                                img.ImageFileName = path;
                                sgworld.Navigate.FlyTo(id);
                                showitem("draw");
                            }
                            else {
                                var paths = obj.routes.features[0].geometry.paths[0];
                                var startpoint = sgworld.Creator.CreatePosition(paths[0][0], paths[0][1], 0, 0, 0, 0, 0, 0);
                                var endpoint = sgworld.Creator.CreatePosition(paths[paths.length - 1][0], paths[paths.length - 1][1], 0, 0, 0, 0, 0, 0);
                                var path = "";
                                path = iisurl + "lecong_GeoSP/geosp/images/换.png";
                                var img = sgworld.Creator.CreateTextLabel(startpoint, "", cLabelStyle, id, ""); //a
                                img.ImageFileName = path;

                                img = sgworld.Creator.CreateTextLabel(endpoint, "", cLabelStyle, id, ""); //a
                                img.ImageFileName = path;
                            }
                        }
                    }


                } catch (e) {
                }
            }
        },
        dataType: "text",
        error: function bb(jqXHR, textStatus, errorThrown) {
            alert("请求数据失败！" + textStatus);
        }

    });
}
function transferMethod(wayResult) {
    var resultString = "";
    var typeMark = wayResult.data[0].TypeMark;
    var number = wayResult.data.length;
    //此处为无结果方案
    if (typeMark == "0") {
        resultString = "";
        return resultString;
    }
    //此处为直达方案
    else if (typeMark == "1") {
        for (var i = 0; i < number; i++) {
            if (number > 1) {
                resultString += "<span style='font-weight:bold;'>方案" + (i + 1) + ":</span><br />";
            }
            resultString += "在" + getMethodResult(wayResult.data[i].SNameArray, 0);
            resultString += "站乘坐";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 0) + "路公交(";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 0) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 0) + "站，到达终点";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 1) + "站。";
            //resultString += "$" + wayResult.data[i].CoordinateArray + "|";
        }
        // resultString = resultString.substring(0, resultString.length - 1);
        return resultString;
    }
    //换乘一次方案
    else if (typeMark == "2") {

        for (var i = 0; i < number; i++) {
            if (number > 1) {
                resultString += "<span style='font-weight:bold;'>方案" + (i + 1) + ":</span><br />";
            }
            resultString += "在" + getMethodResult(wayResult.data[i].SNameArray, 0);
            resultString += "站乘坐";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 0) + "路公交(";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 0) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 0) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 1) + "站；<br />换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 1) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 1) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 1) + "站，到达终点";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 2) + "站。<br />";
            //resultString += "$" + wayResult.data[i].CoordinateArray + "|";
        }
        //resultString = resultString.substring(0, resultString.length - 1);
        return resultString;
    }
    //换乘两次方案
    else if (typeMark == "3") {

        for (var i = 0; i < number; i++) {
            if (number > 1) {
                resultString += "<span style='font-weight:bold;'>方案" + (i + 1) + ":</span><br />";
            }
            resultString += "在" + getMethodResult(wayResult.data[i].SNameArray, 0);
            resultString += "站乘坐";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 0) + "路公交(";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 0) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 0) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 1) + "站；<br />换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 1) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 1) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 1) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 2) + "站；<br />再换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 2) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 2) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 2) + "站,到达终点";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 3) + "站。<br />";
            //resultString += "$" + wayResult.data[i].CoordinateArray + "|";
        }
        //resultString = resultString.substring(0, resultString.length - 1);
        return resultString;
    }
    //换乘三次方案
    else {

        for (var i = 0; i < number; i++) {
            if (number > 1) {
                resultString += "<span style='font-weight:bold;'>方案" + (i + 1) + ":</span><br />";
            }
            resultString += "在" + getMethodResult(wayResult.data[i].SNameArray, 0);
            resultString += "站乘坐";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 0) + "路公交(";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 0) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 0) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 1) + "站；<br />换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 1) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 1) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 1) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 2) + "站；<br />再换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 2) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 2) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 2) + "站到达";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 3) + "站；<br />再换乘";
            resultString += getMethodResult(wayResult.data[i].RNameArray, 3) + "路公交（";
            resultString += getMethodResult(wayResult.data[i].RDirectionArray, 3) + ")方向,途经";
            resultString += getMethodResult(wayResult.data[i].IntervalArray, 3) + "站,到达终点";
            resultString += getMethodResult(wayResult.data[i].SNameArray, 4) + "站。<br />";
            //resultString += "$" + wayResult.data[i].CoordinateArray + "|";
        }
        //resultString = resultString.substring(0, resultString.length - 1);
        return resultString;
    }
}
//
function getMethodResult(myArray, position) {
    var result = myArray.split('$');
    var myResult = result[position];
    return myResult;
}
function getLayer() {

    var MyLayersGroup = sgworld.ProjectTree.FindItem("乐从建筑物白模"); //乐从建筑物精细模型
    var i = 0;

    var ItemID = sgworld.ProjectTree.GetNextItem(MyLayersGroup, 11);
    while (ItemID != 0) {

        var mylayer = sgworld.ProjectTree.GetLayer(ItemID);
        var cFeatureLayerGroup = mylayer.FeatureGroups.point;
        var myProperty = cFeatureLayerGroup.GetProperty("File Name");
        //var myServerProperty = mylayer.DataSourceInfo.ConnectionString;

        var modelPath = myProperty.split("<Value>")[1].split("</Value>")[0];
        cFeatureLayerGroup.SetProperty("File Name", modelPath.replace("113.107.145.67:20008", "183.60.192.206:8000")); // + "?username=mjw"
        var itemname = sgworld.ProjectTree.GetItemName(ItemID);

        ItemID = sgworld.ProjectTree.GetNextItem(sgworld.ProjectTree.FindItem("乐从建筑物白模\\" + itemname), 13);

    }

    sgworld.Project.SaveAs("locong");
}
function setColor() {
    var ItemID = sgworld.ProjectTree.FindItem("shiliang\\lecong_poly");
    sgworld.ProjectTree.SetVisibility(ItemID, true);
    var mylayer = sgworld.ProjectTree.GetLayer(ItemID);
    var cFeatureLayerGroup = mylayer.FeatureGroups.Polygon;
    var myProperty = cFeatureLayerGroup.GetClassification("Fill Color");
    myProperty = '<Classification FuncType="2"><Class><Condition>&lt;"[cun_name]"="葛岸"&gt;</Condition><Value>#FFFFFF</Value></Class><Class><Condition>&lt;"[cun_name]"="良村"&gt;</Condition><Value>#000000</Value></Class><Class><Condition>&lt;"[cun_name]"="路洲"&gt;</Condition><Value>8454016</Value></Class><Class><Condition>&lt;"[cun_name]"="道教"&gt;</Condition><Value>8454143</Value></Class><Class><Condition>&lt;"[cun_name]"="1"&gt;</Condition><Value>16777215</Value></Class><Class><Condition>&lt;"[cun_name]"="2"&gt;</Condition><Value>4194368</Value></Class><Class><Condition>&lt;"[cun_name]"="3"&gt;</Condition><Value>12632256</Value></Class><Class><Condition>&lt;"[cun_name]"="4"&gt;</Condition><Value>8421440</Value></Class><Class><Condition>&lt;"[cun_name]"="5"&gt;</Condition><Value>8421504</Value></Class><Class><Condition>&lt;"[cun_name]"="6"&gt;</Condition><Value>4227200</Value></Class><Class><Condition>&lt;"[cun_name]"="上华"&gt;</Condition><Value>32896</Value></Class><Class><Condition>&lt;"[cun_name]"="乐从居委会"&gt;</Condition><Value>0</Value></Class><Class><Condition>&lt;"[cun_name]"="劳村"&gt;</Condition><Value>8388672</Value></Class><Class><Condition>&lt;"[cun_name]"="大墩"&gt;</Condition><Value>4194368</Value></Class><Class><Condition>&lt;"[cun_name]"="大罗"&gt;</Condition><Value>4194368</Value></Class><Class><Condition>&lt;"[cun_name]"="大闸"&gt;</Condition><Value>4194304</Value></Class><Class><Condition>&lt;"[cun_name]"="小布"&gt;</Condition><Value>8388608</Value></Class><Class><Condition>&lt;"[cun_name]"="小涌"&gt;</Condition><Value>4210688</Value></Class><Class><Condition>&lt;"[cun_name]"="岳步"&gt;</Condition><Value>16384</Value></Class><Class><Condition>&lt;"[cun_name]"="平步居委会"&gt;</Condition><Value>16512</Value></Class><Class><Condition>&lt;"[cun_name]"="新隆"&gt;</Condition><Value>64</Value></Class><Class><Condition>&lt;"[cun_name]"="杨滘"&gt;</Condition><Value>16711808</Value></Class><Class><Condition>&lt;"[cun_name]"="水藤"&gt;</Condition><Value>8388736</Value></Class><Class><Condition>&lt;"[cun_name]"="沙滘"&gt;</Condition><Value>10485760</Value></Class><Class><Condition>&lt;"[cun_name]"="沙边"&gt;</Condition><Value>16711680</Value></Class><Class><Condition>&lt;"[cun_name]"="罗沙"&gt;</Condition><Value>32768</Value></Class><Class><Condition>&lt;"[cun_name]"="腾冲"&gt;</Condition><Value>8453888</Value></Class><Class><Condition>[cun_name]</Condition><Value><Unique/></Value></Class><DefaultValue>255</DefaultValue></Classification>';
    cFeatureLayerGroup.SetClassification("Fill Color", myProperty); //'<Classification FuncType="2"><Class><Condition><"[cun_name]"="平步居委会"></Condition><Value>8421631</Value><DefaultValue>6579300</DefaultValue></Classification>'); //</Class><Class><Condition><"新隆"="新隆"></Condition><Value>8454143</Value></Class><Class><Condition><"1"="1"></Condition><Value>8454016</Value></Class><Class><Condition><"2"="2"></Condition><Value>8453888</Value></Class><Class><Condition><"3"="3"></Condition><Value>16777088</Value></Class><Class><Condition><"4"="4"></Condition><Value>16744448</Value></Class><Class><Condition><"5"="5"></Condition><Value>12615935</Value></Class><Class><Condition><"6"="6"></Condition><Value>16744703</Value></Class><Class><Condition><"上华"="上华"></Condition><Value>255</Value></Class><Class><Condition><"乐从居委会"="乐从居委会"></Condition><Value>65535</Value></Class><Class><Condition><"劳村"="劳村"></Condition><Value>65408</Value></Class><Class><Condition><"大墩"="大墩"></Condition><Value>4259584</Value></Class><Class><Condition><"大罗"="大罗"></Condition><Value>16776960</Value></Class><Class><Condition><"大闸"="大闸"></Condition><Value>12615680</Value></Class><Class><Condition><"小布"="小布"></Condition><Value>12615808</Value></Class><Class><Condition><"小涌"="小涌"></Condition><Value>16711935</Value></Class><Class><Condition><"岳步"="岳步"></Condition><Value>4210816</Value></Class><Class><Condition><"杨滘"="杨滘"></Condition><Value>4227327</Value></Class><Class><Condition><"水藤"="水藤"></Condition><Value>65280</Value></Class><Class><Condition><"沙滘"="沙滘"></Condition><Value>8421376</Value></Class><Class><Condition><"沙边"="沙边"></Condition><Value>8404992</Value></Class><Class><Condition><"罗沙]"="罗沙"></Condition><Value>16744576</Value></Class><Class><Condition><"腾冲"="腾冲"></Condition><Value>4194432</Value></Class><Class><Condition><"良教"="良教"></Condition><Value>8388863</Value></Class><Class><Condition><"良村"="良村"></Condition><Value>128</Value></Class><Class><Condition><"葛岸"="葛岸"></Condition><Value>33023</Value></Class><Class><Condition><"路洲"="路洲"></Condition><Value>32768</Value></Class><Class><Condition><"道教"="道教"></Condition><Value>4227072</Value></Class>

}
function hexStrToBGR(_HexStr) {
    var color_ = sgworld.Creator.CreateColor();
    color_.FromHTMLColor(_HexStr);
    var result_ = color_.ToBGRColor();
    return result_;
};

//重置地图大小 
function resizeMap() {
    var mapheight = $(window).height() - $('.show_tool').height() - 3;
    $('#world3D').css("height", mapheight);
}
$(document).ready(function () {
    resizeMap();
    $(window).resize(function () {
        resizeMap();
    });
});