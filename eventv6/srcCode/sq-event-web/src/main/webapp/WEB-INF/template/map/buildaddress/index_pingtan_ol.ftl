<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>地图首页</title> 
	<style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		.AlphaBack1 tr td{padding-top:3px;padding-right:2px;}
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
	</style>
	<#include "/map/mapabc/relyJs.ftl"/>
	<script type="text/javascript" src="${rc.getContextPath()}/js/map/map.api.js"></script>
</head>
<body style="width:100%;height:100%;border:none;" >
	<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />
	<input type="hidden" name="gridCode" id="gridCode" value="<#if gridCode??>${gridCode}</#if>" />
	<input type="hidden" name="orgCode" id="orgCode" value="<#if orgCode??>${orgCode}</#if>" />
	<input type="hidden" name="mapt" id="mapt" value="<#if mapt??>${mapt}</#if>" />
	<input type="hidden" readonly="true" name="x" id="x" value="<#if x??>${x?c}</#if>" />
	<input type="hidden" readonly="true" name="y" id="y" value="<#if y??>${y?c}</#if>" />
    <input type="hidden" readonly="true" name="x" id="hs" value="<#if hs??>${hs!''}</#if>"/>
	<input type="hidden" readonly="true" name="wid" id="wid" value="" />
	
	<input type="hidden" readonly="true" name="ssqy" id="ssqy" value="" />
	<input type="hidden" readonly="true" name="name" id="name" value="" />
	<input type="hidden" readonly="true" name="xingzhi" id="xingzhi" value="" />
	<input type="hidden" readonly="true" name="adress" id="adress" value="" />
	
 	<div>
		<div class="page-container" id="map0" style="position: absolute; width:100%; height:100%; z-index: 1;">
		</div>
	</div>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		        <tr style="float:left">
                    <td>
                        <a href="#" onclick="savePointLocate()" id="savePointLocate" class="NorToolBtn SmallSaveBtn" style="display:none;">保存</a>
                    </td>
                    <td>
                        <a href="#" onclick="choose('drawBuilding')" id="drawBuilding" class="NorToolBtn DrawBorderBtn" style="display:none;">轮廓编辑</a>
                    </td>
                    <td>
                        <a href="#" onclick="choose('cancleDrawOrEditBuilding')" id="cancleDrawOrEditBuilding" class="NorToolBtn BackBtn" style="display:none;">取消轮廓编辑</a>
                    </td>
                    <td>
                        <a href="#" onclick="choose('delDrawData')" id="delDrawData" class="NorToolBtn DrawBorderBtn" style="display:none;">删除轮廓</a>
                    </td>

		            <td>
                        <div id="tipMessage" style="color:yellow;font-size:13px"></div>
		 				<div class="blind" style="color:yellow;font-size:13px;float:right;"></div>
		    	 	</td>
		         </tr>
		    </table>    	
		</div>
	</div>
	<div class="MapTools" style="top:261px;z-index:2;">
		<ul>
	    	<li class="ClearMap" onclick="clearMyLayer();"></li>
	    </ul>
		<div id="mapStyleDiv" class="MapStyle" style="display:none">
			<span class="current">矢量图</span>
			<span>影像图</span>
		</div>
	</div>

</body>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/customEasyWin.ftl" />
<script type="text/javascript">
var selectRowNum = 0;
var test1="tets";
var map = null;
var ds;//数据连接源
var conncallback;//连接源回调
var OPERATION_CODE = "QUERY";//判断是新增还是修改
var isEditOrAdd = "ADD";//判断是新增还是修改
var editOrAddFlag = false;//判断是新增还是修改

var blindBuildingFlag = true;//是否绑定轮廓操作标识
var showGridLayerFlag = false; //是否显示所在网格的轮廓标识
var drawBuildingHSFlag = true;//绘制楼宇轮廓标识

var drawBuilding;//绘制楼宇轮廓工具
var selectClick;//选择楼宇轮廓工具
var modifyTool;//编辑工具

var buildingPolygonLayer;//楼宇轮廓图层
var mapApi2;
var markers = null;
var fid = "";//轮廓数据表fid
var buildDS = null;
function _init() {
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",$(document).width());
	window.onresize=function(){
		$("#map0").css("height",$(document).height());
		$("#map0").css("width",$(document).width());
	};
	$(document).mouseup(function() {
		$(this).unbind("mousemove");
	});
}

$(function(){
    layer.load(0);
 	_init();
	var eventDomain = "${rc.getContextPath()}";
	var zzgridDomain = "${SQ_ZZGRID_URL}";
	//验证用户标识，成功后回调
	//SPGIS.VerifyKey("2da73f2f-6485-479b-9e0c-da85b49d370c");
	MMApi.LoadMap(eventDomain, zzgridDomain, "${SPGIS_IP!''}", "SpGisMap", function(mapApi) {
		mapApi.Init("map0", $("#gridId").val(), $("#orgCode").val());
		map = MMGlobal.mapObj;
        map.showSicMapOfName("pingtanimage");
		markers = new SPGIS.Layer.Markers("markers");
		//定位到平潭
		//var bounds=new SPGIS.Geometry.Bounds(119.784790,25.499390 ,119.796021,25.509094);
		//map.showOfBounds(bounds);
		//vectorlayer = new SPGIS.Layer.Vector("vectorlayer");
		//map.addLayer(vectorlayer);
		
		//叠加wms服务
		//addWMSLayer(map);
		mapApi.drawBuildLine();
		


		//连接数据源
		//创建数据源,fwlkbz_point:房屋轮廓标注点数据表
		conncallback=function(d){
 			// alert(d);
 		};
		ds = new SPGIS.Data.DataSource("fwlkbz_point",conncallback);
		//连接数据源
		ds.conn();
		initSyncDataToSPGisEvent();
        layer.closeAll('loading');
    });
 	
});


function initSyncDataToSPGisEvent() {
	buildDS = new SPGIS.Data.DataSource("ALLJZW", function() {
		if (buildDS.isSucceed()) {
			/*
            drawPointClick = SPGIS.Control.ClickControl(function(e) {
				var wid = $("#wid").val();
				if(typeof(wid) != 'undefined' && wid != null && wid != "") {
                    markers.clearMarkers();

                    layer.load(0);
                    //在鼠标点击的地方，获取到坐标，并添加标注图标
                    var lonlat = map.getLonLatFromPixel(e.xy).toShortString();
                    var lon = lonlat.split(",")[0] * 1.0;
                    var lat = lonlat.split(",")[1] * 1.0;
                    var size = new SPGIS.Mapping.Size(21,25);
                    var offset = new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h);
                    var icon = new SPGIS.Theme.Icon(MMGlobal.ContextPath + '/js/map/spgis/lib/img/marker-gold.png', size, offset);
                    var markerLonLat = new SPGIS.Mapping.LonLat(lon, lat);
                    var feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);
                    feature.closeBox = false;
                    feature.data.overflow = "hidden";
                    feature.data.icon = icon;
                    var marker = feature.createMarker();
                    markers.addMarker(marker);
                    map.addLayer(markers);
                    //获取坐标
                    if (lon != null && lat != null){
                        $("#x").val(lon);
                        $("#y").val(lat);
                        $("#mapt").val(MMGlobal.MapType);
                    }

					//查询楼宇轮廓
                    var p1 = new SPGIS.Geometry.Point((lon - 0.00000000001), (lat + 0.00000000001));
                    var p2 = new SPGIS.Geometry.Point((lon + 0.00000000001), (lat + 0.00000000001));
                    var p3 = new SPGIS.Geometry.Point((lon + 0.00000000001), (lat - 0.00000000001));
                    var p4 = new SPGIS.Geometry.Point((lon - 0.00000000001), (lat - 0.00000000001));
                    var ring = new SPGIS.Geometry.LinearRing([p1, p2, p3, p4]);
                    var polygon = new SPGIS.Geometry.Polygon(ring);
                    var param = new SPGIS.Data.PolygonParameter(polygon);
                    var query = new SPGIS.Data.QueryData(buildDS, param);
                    query.submit(function (d) {
                        layer.closeAll('loading');
                        Lemon.MSG.MsgPanel.Debug(d);
                        if (d.chains.length != 0) {
                            var datas = d.chains;
                            if (datas != null && datas.length > 0) {
                                //获取楼宇轮廓数据
                                var coordinatesArray = datas[0].geometry.coordinates[0];
								var hs="";
								for(var i=0;i<coordinatesArray.length;i++){
									var coordinates = coordinatesArray[i];
									for(var j=0;j<coordinates.length;j++){
                                        if(hs != null && hs != ''){
                                            hs = hs + ",";
                                        }
                                        hs = hs + coordinates[j][0] + ',' + coordinates[j][1];
									}
								}
								$("#hs").val(hs);
                            } else {

                            }
                        }
						$("#savePointLocate").show();
                    });
                }else{
                    DivShow('请选择要标注的楼宇!');
				}
			});
			MMGlobal.mapObj.addControl(drawPointClick);
            drawPointClick.activate();
            */
		}
	});
	// 连接数据源
	buildDS.conn();
}

function editCustomidByFid(ds, fid, customid, callBack) {
	if (fid != "" && ds != null && ds.isSucceed() && customid != "") {
		var param = new SPGIS.Data.UpdateParameter("fid=='" + fid + "'");
		param.setField("customid", customid);
		var edit = new SPGIS.Data.EditData(ds, param);
		edit.submit(function(d) {
			Lemon.MSG.MsgPanel.Debug(d);
			if (typeof callBack == "function") {
				callBack.call(this);
			}
		});
	} else {
		if (typeof callBack == "function") {
			callBack.call(this);
		}
	}
}
var WMSlayer;
//叠加wms图层
function addWMSLayer(mapObject){
	//http://183.250.187.97:8080/spgis/jack/wms
	//wms图层参数
	var params = {
		layers: 'ALLJZW',//楼宇：CQJZW，网格：WANGGE
        transparent: true
	};
	//wms选项
	var options = {
		buffer: 0,
		displayOutsideMaxExtent: true,
		isBaseLayer: false,
		opacity: 1,
		color: 'red'
	};
	//创建wms图层
    WMSlayer = new SPGIS.Layer.WMS("ALLJZW","http://${SPGIS_IP!''}:8080/spgis/jack/wms",params,options);

    map.addLayer(WMSlayer);
}


/**
 * 选择功能（绑定楼宇、标注中心点）
 */
function choose(type){
    if(type != null && type != ''){
        //removeAllTools();
        if(type == 'drawBuilding'){
            $('#savePointLocate').show();
            $(' #drawPoint').hide();
            $('#drawBuilding').hide();
            $('#cancleDrawOrEditBuilding').show();

            var wid = $("#wid").val();
            if(wid != null && wid != "" && typeof(wid) != 'undefined'){
                $("#cancleDrawPoint").hide();
				markers.clearMarkers();
                if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
                    drawPointClick.deactivate();
                    drawPointClick = "";
                }

                $("#cancleDrawOrEditBuild").show();
                $("#drawBuild").hide();
                $("#drawPoint").show();

                var hs = document.getElementById("hs").value;
                if(hs != null && hs != '' && typeof(hs) != 'undefined'){
                    addModifyTool(hs);//编辑轮廓
                }else{//绘制轮廓
                    drawPointFlag = false;
                    blindGridFlag = false;
                    drawGridHSFlag = true;
                    drawBuildHS();
                }
            }else{
                chooseGridTipMessage();
            }


        }
        if(type == "cancleDrawOrEditBuilding"){
            $('#drawBuilding').show();
            $('#savePointLocate').hide();
            $('#cancleDrawOrEditBuilding').hide();
			//取消编辑状态
			var wid = $("#wid").val();
			var gridId = $("#gridId").val();
            setMapcenter(wid, gridId);
            cancleDrawOrEditBuild();
            getBuildingHSById();
            addDrawPointTool();
        }
        if(type == "delDrawData"){
            $('#drawBuilding').show();
            $('#drawPoint').show();
            $('#saveButton').hide();
            $('#cancleDrawPoint').hide();
            $('#cancleDrawOrEditBuilding').hide();
            delBuildingHSData()
            //map.removeLayer(iconLayer);
            //map.removeLayer(gridLayer);
        }

    }
}

//保存并推送楼宇id和坐标信息
function savePointLocate() {
    //cancleDrawOrEditBuild();


    var wid = $('#wid').val();
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
        //取消绘制中心点图层
        if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
            drawPointClick.deactivate();
        }

        //取消编辑轮廓
        if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
            modifyFeature.deactivate();//取消编辑轮廓
        }
        var x = $('#x').val();
        var y = $('#y').val();
        var hs = $('#hs').val();
        var mapt = $('#mapt').val();

        editCustomidByFid(buildDS, fid, wid, function() {
			//推送标注信息给星云
			saveDateToSP(x, y, mapt, wid);
			//本地保存标注信息
			saveDateToFFCS(x, y, hs, mapt, wid);
		});

        addDrawPointTool();
	}else{
		DivShow('请选择要标注的楼宇!');
	}
}

//本地保存
function saveDateToFFCS(x, y, hs, mapt, wid){
	var data = 'wid='+wid+'&x='+x+'&y='+y+'&mapt='+ mapt+'&hs='+hs+'&saveCenterPointFlag=true';
	var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofbuilding/updateMapDataOfBuildOfBinding.json?'+data;
	if(wid != null && wid !=''){
		$.ajax({
			url: url,
			type: 'POST',
			timeout: 3000,
			dataType:"json",
			async: false,
			error: function(data){
				$.messager.alert('提示','操作报错!','warning');
			},
			success: function(data){
				if(data.flag == true) {
                    var markerImgId = "markerImg_"+selectRowNum;
                    window.parent.document.getElementById(markerImgId).innerHTML = '<img src="${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png" class="FontDarkBlue" style="float:left" title="已绑定">';

                    document.getElementById("hs").value = hs;
					$('#savePointLocate').hide();
                    $('#drawBuilding').show();
                    $('#cancleDrawOrEditBuilding').hide();

					DivShow('保存成功!');
                    setMapcenter(wid);
                    getBuildingHSById();
				}else {
					DivShow('保存失败!');
				}
			}
		});
	}else{
		alert("请选择要标注的楼宇!");
	}
}

//推送信息给星云
function saveDateToSP(x, y, mapt, wid){
	delSPGISDate(wid);//先删除后新增
}


//查询星云数据
function querySPGISDate(wid){
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "QUERY";
		//属性查询参数对象，传入的是字段名称和要查询的值
	 	var param = new SPGIS.Data.AttributeParameter("customid==" + wid.toString());
		//数据查询功能对象，传入的参数是数据源、查询参数
		var query = new SPGIS.Data.QueryData(ds,param); 
		//执行查询
		query.submit(submitSucceed);
	}
}

//新增星云数据
function addSPGISDate(){
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	var wid = $('#wid').val();
	var ssqy = $('#ssqy').val();
	var name = $('#name').val();
	var xingzhi = $('#xingzhi').val();
	var adress = $('#adress').val();
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "ADD";
		//从数据源中获取当前数据源的元数据，可以查看数据源的字段结构		
		var prop=ds.getMetaData();
		prop.customid = wid;
		//prop.ssqy = ssqy;//所属区域
		//prop.name = name;//名称
		//prop.xingzhi = xingzhi;//性质
		//prop.adress = adress;//地址
		//构造地图上的点对象
	 	var p1 = new SPGIS.Geometry.Point(x, y);   
		var para=new SPGIS.Data.InsertParameter();
		para.add(p1,prop,ssqy,name,xingzhi,adress);
		var add = new SPGIS.Data.EditData(ds,para); 
		add.submit(submitSucceed);
	}
}

//修改星云数据
function editSPGISDate(){
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	var wid = $('#wid').val();
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "EDIT";
		var param=new SPGIS.Data.UpdateParameter("customid=="+ wid.toString());
		param.setField("customid",wid);
		param.setField("geometry",wid);
		var edit = new SPGIS.Data.EditData(ds,param); 
		edit.submit(submitSucceed);
	}
}

//删除星云数据
function delSPGISDate(wid){
	//判断数据源是否连接成功，如果连接不成功返回false，不成功的原因可能是当前连接的数据源名称
	if(ds.isSucceed()){
		OPERATION_CODE = "DEL";
		//删除要素参数对象
		var param = new SPGIS.Data.DeleteParameter("customid=="+ wid.toString());
		var edit = new SPGIS.Data.EditData(ds,param); 
		edit.submit(submitSucceed);
	}
}

/**
 * 绘制中心点
 */
function drawCenterPoint() {
    var buildingId = $('#wid').val();
    if(buildingId != null && buildingId != ''){
        drawPointFlag = true;
        addDrawPointTool();
    }else{
        DivShow('请选择要标注的楼宇!');
    }
}

//添加标注中心点工具
var drawPointClick;
function addDrawPointTool(){

    if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
        modifyFeature.deactivate();//取消编辑轮廓
    }

    //添加标注功能
    drawPointClick = SPGIS.Control.ClickControl(function(e) {
        var wid = $("#wid").val();
        if(typeof(wid) != 'undefined' && wid != null && wid != "" && drawPointClick != null && drawPointClick != '') {
            markers.clearMarkers();

            layer.load(0);
            //在鼠标点击的地方，获取到坐标，并添加标注图标
            var lonlat = map.getLonLatFromPixel(e.xy).toShortString();
            var lon = lonlat.split(",")[0] * 1.0;
            var lat = lonlat.split(",")[1] * 1.0;
            var size = new SPGIS.Mapping.Size(21,25);
            var offset = new SPGIS.Mapping.Pixel(-(size.w / 2), -size.h);
            var icon = new SPGIS.Theme.Icon(MMGlobal.ContextPath + '/js/map/spgis/lib/img/marker-gold.png', size, offset);
            var markerLonLat = new SPGIS.Mapping.LonLat(lon, lat);
            var feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);
            feature.closeBox = false;
            feature.data.overflow = "hidden";
            feature.data.icon = icon;
            var marker = feature.createMarker();
            markers.addMarker(marker);
            map.addLayer(markers);
            //获取坐标
            if (lon != null && lat != null){
                $("#x").val(lon);
                $("#y").val(lat);
                $("#mapt").val(MMGlobal.MapType);
            }

            //查询楼宇轮廓
            var p1 = new SPGIS.Geometry.Point((lon - 0.00000000001), (lat + 0.00000000001));
            var p2 = new SPGIS.Geometry.Point((lon + 0.00000000001), (lat + 0.00000000001));
            var p3 = new SPGIS.Geometry.Point((lon + 0.00000000001), (lat - 0.00000000001));
            var p4 = new SPGIS.Geometry.Point((lon - 0.00000000001), (lat - 0.00000000001));
            var ring = new SPGIS.Geometry.LinearRing([p1, p2, p3, p4]);
            var polygon = new SPGIS.Geometry.Polygon(ring);
            var param = new SPGIS.Data.PolygonParameter(polygon);
            var query = new SPGIS.Data.QueryData(buildDS, param);
            query.submit(function (d) {
                layer.closeAll('loading');
                Lemon.MSG.MsgPanel.Debug(d);
                if (d.chains.length != 0) {
                    var datas = d.chains;
                    if (datas != null && datas.length > 0) {
                        //获取楼宇轮廓数据
						//if(datas[0].geometry != null && datas[0].geometry.coordinates.size>0){
							var coordinatesArray = datas[0].geometry.coordinates[0];
							var hs="";
							for(var i=0;i<coordinatesArray.length;i++){
								var coordinates = coordinatesArray[i];
								for(var j=0;j<coordinates.length;j++){
									if(hs != null && hs != ''){
										hs = hs + ",";
									}
									hs = hs + coordinates[j][0] + ',' + coordinates[j][1];
								}
							}
							$("#hs").val(hs);
						//}
                    } else {
                        alert("无法查询到对方的楼宇轮廓数据！\n请选择有轮廓的楼宇或请确认轮廓数据提供方提供的数据源ALLJZW是否正常！");
                    }
                    $("#savePointLocate").show();
                }else{
					alert("无法查询到对方的楼宇轮廓数据！\n请选择有轮廓的楼宇或请确认轮廓数据提供方提供的数据源ALLJZW是否正常！");
				}

            });
        }else{
			if(drawPointClick != null && drawPointClick != ""){
                DivShow('请选择要标注的楼宇!');
			}

        }

    });
    map.addControl(drawPointClick);
    drawPointClick.activate();
}



/**
 * 清除标注中心点状态
 */
function reomveDrawPointTool(){
    if(drawPointClick != null && drawPointClick != '' && typeof(drawPointClick) != 'undefined'){
        drawPointClick.deactivate();
    }
}

/**
 * 添加绘制网格轮廓工具
 */
var drawBuildTool;
var drawBuildlayer;
function drawBuildHS(){
    drawBuildlayer = new SPGIS.Layer.Vector("vectorlayer");
    map.addLayer(drawBuildlayer);
    drawBuildTool = map.createAction();
    drawBuildTool.createPolygon(drawBuildlayer,function(e){
        var str = "";
        var rings = e.feature.geometry.components;
        var ringPoints = rings[0].components;
        var hs = '';
        var sumX=0,sumY=0,sumLength=0;
        sumLength = ringPoints.length;
        for(var j = 0;j < ringPoints.length;j++){
            if(hs != null && hs != ''){
                hs = hs + ",";
            }
            hs += ringPoints[j].x + ',' + ringPoints[j].y;
            sumX +=  Number(ringPoints[j].x);
            sumY +=  Number(ringPoints[j].y);
        }
        //计算中心点坐标
        var x = sumX/sumLength;
        var y = sumY/sumLength;
        if(!saveCenterPointFlag || $("#x").val() == "" || $("#x").val() == null){
            $("#x").val(x);
            $("#y").val(y);
        }
        $("#hs").val(hs);
    });
}


//添加编辑工具
var modifyFeature;
function addModifyTool(){

    if(buildingPolygonLayer != null && buildingPolygonLayer != '' && typeof(buildingPolygonLayer) != 'undefined'){
        modifyFeature = new SPGIS.Control.ModifyFeature(buildingPolygonLayer.layer);
        map.addControl(modifyFeature);
        modifyFeature.activate();
		//获取编辑后的数据
        modifyFeature.onModificationEnd = function(e){
            var data = e.geometry.components[0].components;
            var hs = "";
            for(var i = 0;i < data.length;i++){
                if(hs != null && hs != ''){
                    hs = hs + ",";
                }
                hs += data[i].x + ',' + data[i].y;
            }
            $("#hs").val(hs);
        }
    }

}

/**
 * 取消楼宇轮廓绘制/编辑
 */
function cancleDrawOrEditBuild(){
    initSyncDataToSPGisEvent();//添加标注绑定功能
    var hs = document.getElementById("hs").value;
    if(hs != null && hs != '' && typeof(hs) != 'undefined'){
        if(modifyFeature != null && modifyFeature != '' && typeof(modifyFeature) != 'undefined'){
			buildingPolygonLayer.removeAllFeatures();
            map.removeLayer(buildingPolygonLayer);
            modifyFeature.deactivate();//取消编辑轮廓
        }
    }else{//取消绘制轮廓：画完后双击就取消绘制功能了
        if(drawBuildlayer != null && drawBuildlayer != '' && typeof(drawBuildlayer) != 'undefined'){
            drawBuildlayer.removeAllFeatures();
            map.removeLayer(drawBuildlayer);//取消轮廓绘制
        }

    }

}


function submitSucceed(d){
	Lemon.MSG.MsgPanel.Debug(d);
	if(OPERATION_CODE != null && OPERATION_CODE != '' && typeof(OPERATION_CODE) != 'undefined'){
		if(OPERATION_CODE == "QUERY"){
			if(d.chains.length != 0){
				markers.clearMarkers();
				var datas = d.chains;
				for(var i = 0;i < datas.length;i++){
					var lonIn = datas[i].geometry.coordinates[0];
					var latIn = datas[i].geometry.coordinates[1];
					var markerLonLat = new SPGIS.Mapping.LonLat(lonIn,latIn);
					var size = new SPGIS.Mapping.Size(21,25);
					var offset = new SPGIS.Mapping.Pixel(-(size.w/2), -size.h);
					var icon = new SPGIS.Theme.Icon('${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png',size,offset);
					feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);  
			 	    feature.data.icon = icon;
			 	    feature.data.id = i;
			 	    var marker = feature.createMarker();
			 	    markers.addMarker(marker);	
			 	    if(i == 0){
			 	    	//设置地图中心点
			 	    	var lonlat = new SPGIS.Mapping.LonLat(lonIn, latIn);
						map.setCenter(lonlat);
			 	    }		
				}
				map.addLayer(markers);
			}else{
				markers.clearMarkers();
				DivShow('该楼宇未标注!');
			}
		}else if(OPERATION_CODE == "ADD"){
			//alert("新增成功");
		}else if(OPERATION_CODE == "EDIT"){
			//alert("修改成功");
		}else if(OPERATION_CODE == "DEL"){
			addSPGISDate();
		}
		
	}
	
}

/**
 * 地图中心点定位
 */
function setMapcenter(wid, gridId, infoOrgCode){
	if(wid != null && wid != '' && typeof(wid) != 'undefined'){
		//使用高德的数据进行定位
		//querySPGISDate(wid);
		//使用我们的数据库数据进行定位
		var targetType = 'build';
		var param = "?mapt=5&wid="+wid+"&targetType="+targetType;
		url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataCenterAndLevel.jhtml";
		url = url+param;
		$.ajax({   
			 url: url,
			 type: 'POST',
			 timeout: 3000, 
			 dataType:"json",
			 error: function(data){
			 	alert('网格地图信息获取出现异常，请联系维护人员处理！'); 
			 },
			 success: function(data){
			 	var obj = data.result;
			 	if(obj != null) {
			 		markers.clearMarkers();
					var lonIn = obj.x;
					var latIn = obj.y;
					var markerLonLat = new SPGIS.Mapping.LonLat(lonIn,latIn);
					var size = new SPGIS.Mapping.Size(21,25);
					var offset = new SPGIS.Mapping.Pixel(-(size.w/2), -size.h);
					var icon = new SPGIS.Theme.Icon('${rc.getContextPath()}/js/map/spgis/lib/img/marker-gold.png',size,offset);
					feature = new SPGIS.FeatureOrdinary(markers, markerLonLat);  
			 	    feature.data.icon = icon;
			 	    feature.data.id = wid;
			 	    var marker = feature.createMarker();
			 	    markers.addMarker(marker);				
					map.addLayer(markers);
		 	    	//设置地图中心点
		 	    	var lonlat = new SPGIS.Mapping.LonLat(lonIn, latIn);
					$("#x").val(lonIn);
                    $("#y").val(latIn);
                    $("#mapt").val(5);

                    map.setCenter(lonlat);
			 	}
			 }
		 });
	}else if(gridId != null && gridId != '' && typeof(gridId) != 'undefined'){
		//MMApi.setCenter(gridId);
	}
}


/**
 * 根据楼宇id获取楼宇轮廓
 */
function getBuildingHSById(){
    var url = "";
    var buildingId = $('#wid').val();
    var param = "?wid="+buildingId+"&mapt=5";
    url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfBuild.json";
    url = url+param;
    $.ajax({
        url: url,
        type: 'POST',
        timeout: 3000,
        dataType:"json",
        async: false,
        error: function(data){
            alert('获取楼宇轮廓信息出现异常，请联系维护人员处理！');
        },
        success: function(data){
            if(data.list.length>0) {
                document.getElementById("hs").value = data.list[0].hs;

                if(typeof(data.list[0].hs) != 'undefined' && data.list[0].hs != null && data.list[0].hs != ''){
                    document.getElementById("drawBuilding").style.display="block";
                }else{
                    document.getElementById("drawBuilding").style.display="none";
                }
                //画楼宇轮廓
                getBuildingPolygon();
            } else {
                $('#drawBuilding').hide();
                $('#delDrawData').hide();
                removeBuildingPolygonLayer();
                markers.clearMarkers();
                DivShow('该楼宇未标注!');
            }

        }
    });
}

/**
 * 在地图上画出多边形区域
 */
var buildFeature;
function getBuildingPolygon(){
    //清除绘制楼宇轮廓后留下的图层
    //clearDrawBuildingPolygon();
    removeBuildingPolygonLayer();
    var hs = document.getElementById("hs").value;

    if(hs != null && hs != '' && typeof(hs) != 'undefined'){
        $('#delDrawData').show();
    }else{
        $('#delDrawData').hide();
    }

    var pointList = [];
    var polygon_points = hs.split(",");
    for(var i=0;i<polygon_points.length;i++){
        var pointX, pointY;
        if(i%2==0){
            pointX = polygon_points[i];
            pointY = polygon_points[i+1];
            var point = new SPGIS.Geometry.Point(pointX, pointY);

            pointList.push(point);
        }
        i++;
    }

    var ring = new SPGIS.Geometry.LinearRing(pointList);
    buildFeature = new SPGIS.Feature(new SPGIS.Geometry.Polygon(ring));
    buildingPolygonLayer = new SPGIS.Layer.Vector("layer");

    buildingPolygonLayer.addFeatures(buildFeature,SPGIS.MeasureStyle);


    map.addLayer(buildingPolygonLayer);
}

/**
 * 清楚楼宇的轮廓图层
 */
function removeBuildingPolygonLayer(){
	if(typeof(buildingPolygonLayer) != "undefined" && buildingPolygonLayer != null && buildingPolygonLayer != "") {
        buildingPolygonLayer.removeAllFeatures(buildFeature);
        buildFeature = "";
		map.removeLayer(buildingPolygonLayer);
    }
}

function clearDrawBuildingPolygon(){

}

/**
 * 根据楼宇id删除楼宇轮廓和中心点
 */
function delBuildingHSData(){
    $('#savePointLocate').hide();
    var url = "";
    var buildingId = $('#wid').val();
    var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/deleteArcgisDrawAreaPanel.json?";
    var data = "targetType=build&wid="+ buildingId +"&mapt=5";
    if(!confirm('确定要删除?')){
        return false;
    }
    var type = 1;
    var layerName="";
    layerName = "buildLayer";
    type = 1


    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType:"json",
        success: function(data){
            DivShow('删除成功！');
            $('#delDrawData').hide();
            var markerImgId = "markerImg_"+selectRowNum;
            window.parent.document.getElementById(markerImgId).innerHTML = "";
            document.getElementById("hs").value = "";
            document.getElementById("drawBuilding").style.display="none";
            //$("#markerImg_" + selectRowNum).html('<img class="FontDarkBlue" src="${rc.getContextPath()}/images/tj_wg_80.png" style="float:left;border-width:0;" title="已标注">');
            //清除绘制楼宇轮廓后留下的图层
            markers.clearMarkers();
            removeBuildingPolygonLayer();
            initSyncDataToSPGisEvent();//添加标注绑定功能
        },
        error:function(msg){
            DivShow('删除失败！');
        }
    });
}

function DivHide(){
	$(".blind").hide();//窗帘效果展开
}
function DivShow(msg){
	$(".blind").html(msg);
	$(".blind").show();//窗帘效果展开
	setTimeout("this.DivHide()",2000);
}


</script>
</html>
