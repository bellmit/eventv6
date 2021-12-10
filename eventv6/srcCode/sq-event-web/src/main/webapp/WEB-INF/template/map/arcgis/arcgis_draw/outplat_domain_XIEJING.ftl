<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>地图首页</title>
	<script type="text/javascript">
		var callBackUrl = "${callBackUrl}";
	</script>

<#include "/map/arcgis/arcgis_base/arcgis_common.ftl" />
    <style type="text/css">
        *{margin:0; padding:0; list-style:none;}
        .AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
        .AlphaBack1{color:#fff}
        .AlphaBack1 select{color:#000}
        .AlphaBack1 tr td{padding-top:3px;padding-right:2px;}
        .inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
        .button1{width:60px; height:28px; line-height:26px; text-align:center;}
        .NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#ff9f00; background-position:7px 5px;}
        .NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
        .LeftTree .con li.selectRow{background: 160px 12px no-repeat rgb(142, 177, 228);}
        .LeftTree .con li:hover {background: 160px 12px no-repeat rgb(142, 177, 228);}
        .LeftTree .con li{color: black}
    </style>
<link rel="stylesheet" type="text/css" href="${SQ_ZHSQ_EVENT_URL}/css/anole_combobox.css" />


</head>

<body>
<input type="hidden" name="mapt" id="mapt" value="${mapt}" />
<input type="hidden" readonly="true" name="x" id="x" value="${x?c}" />
<input type="hidden" readonly="true" name="y" id="y" value="${y?c}" />
<input type="hidden" readonly="true" name="mapCenterLevel" id="mapCenterLevel" value="${mapCenterLevel?c}" />

<input type="hidden" name="gridId" id="gridId" value="<#if gridId??>${gridId?c}</#if>" />


<#if isGetXIEJINGAddress?? && isGetXIEJINGAddress=="true">
<div style="width: 300px;float: left;height: 100%;">
    <div class="LeftTree" style="width: 300px;height: 100%">
        <div id="content-d" class="con content light" style="height: 100%">
        </div>
        <div class="NorPage" style="width: 295px;height: 30px">
            <ul>
                <li class="PreBtn"><a href="javascript:change('1');"><img src="${uiDomain!''}/images/pre3.png" /></a></li>
                <li class="yema" style="width: 245px">共 <span id="pagination-num">0</span>/<span id="pageCount">0</span> 页</li>
                <li class="NextBtn"><a href="javascript:change('2');"><img src="${uiDomain!''}/images/next3.png" /></a></li>
            </ul>
        </div>
    </div>
</div>
</#if>
<#if isEdit??>
	<#if (isEdit == "true")>
        <div class="MapBar" id="MapBar" style="float: left;width: 100%;margin-left: 305px;">
            <div class="con AlphaBack1" style="height:32px">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr style="float:left">
                        <td>
                            <a href="#" onclick="endPointLocate()" class="NorToolBtn SmallSaveBtn">确定</a> &nbsp;
						</td>
                        <td>
							<div id="tipMessage" style="color:yellow;font-size:10px;float: right">您未选择地址！</div>
                            <input type="hidden" name="mapt" id="mapt" value="" />
                            <input type="hidden" name="x" id="x" value="" />
                            <input type="hidden" name="y" id="y" value="" />
                            <input type="hidden" name="XIEJINGAddress" id="XIEJINGAddress" value="" />
                            <input type="hidden" name="XIEJINGregionCode" id="XIEJINGregionCode" value="" />
                            <!-- 	地图类型：<input type="text" readonly="true" name="mapt" id="mapt" value="" />
    x：<input type="text" readonly="true" name="x" id="x" value="" />
    y：<input type="text" readonly="true" name="y" id="y" value="" />  -->
                        </td>
                    </tr>
                </table>
            </div>
        </div>
	</#if>
</#if>
    <div class="page-container" id="map0" style="position: absolute; width:100%; height:100%;margin-left: 305px; z-index: 1;">
    </div>
    <div class="page-container" id="map1" style="position: absolute; width:480px; height:100%;margin-left: 305px; z-index: 2;display:none;">
    </div>
    <div class="page-container" id="map2" style="position: absolute; width:480px; height:100%;margin-left: 305px; z-index: 3;display:none;">
    </div>
	<div style="width: 60%;margin-left: 300px">

	</div>
	<div id="jsSlider" style="margin-left: 300px"></div>
	<div class="MapTools" style="margin-left: 300px">
		<ul>
			<li class="ThreeWei" onclick="threeWeiClick()"></li>
			<!--
			<li class="ClearMap"></li>
			<li class="SelectMap"></li>
			<li class="MapFull"></li>
			-->
		</ul>
		<div id="mapStyleDiv" class="MapStyle" style="display:none">
			<span class="current">二维图</span>
			<span>三维图</span>
			<span>卫星图</span>
		</div>
	</div>

    <div style="height:0px">
        <iframe id="cross_domain_frame" name="cross_domain_frame" width="60%" height="450px" src="" marginwidth=0 marginheight=0 scrolling="no" frameborder=0></iframe>
    </div>

</body>
<script>
var flagPoint = false;
var mapType = '<#if mapType??>${mapType}</#if>';
var isEdit = '${isEdit}';


jQuery(document).ready(function() {
    $("#content-d").css("height",$(document).height() - 60);
	$("#map0").css("height",$(document).height());
	$("#map0").css("width",(document.body.offsetWidth  - 300));
	$("#map1").css("height",$(document).height());
	$("#map1").css("width",(document.body.offsetWidth  - 300));
	$("#map2").css("height",$(document).height());
	$("#map2").css("width",(document.body.offsetWidth  - 300));
	$("html").attr("class","");
	getArcgisInfo();
	/*
	getArcgisOfGridDatas(document.getElementById("gridId").value,currentArcgisConfigInfo.mapType);
	setTimeout(function(){
		getMapArcgisPointInfo();
	},3000); 
	*/
	window.onresize=function(){
	  	$("#map0").css("height",$(document).height());
		$("#map0").css("width",document.body.offsetWidth - 300);
		$("#map1").css("height",$(document).height());
		$("#map1").css("width",document.body.offsetWidth - 300);
		$("#map2").css("height",$(document).height());
		$("#map2").css("width",document.body.offsetWidth - 300);
        setTimeout(function() {
            setMapArcgisPoint();
        },1000);
	 }

    var options = {
        axis : "yx",
        theme : "minimal-dark"
    };
    enableScrollBar('content-d',options);

    loadXIEJINGAddrss();


});

function showPolygon() {
	var map = $.fn.ffcsMap.getMap();

	//-- 区域
	var locationList = jQuery.parseJSON('${locationListJson}');
	if(locationList != null && locationList.length>0) {
		var locationPoints = [];
		for(var i=0;i<locationList.length;i++) {
			//var point = new google.maps.LatLng(locationList[i].y, locationList[i].x);
			var point = new esri.geometry.Point(locationList[i].x, locationList[i].y);
			locationPoints.push(point);
		}
		locationPoints.push(new esri.geometry.Point(locationList[0].x, locationList[0].y));
		//polygon = new google.maps.Polygon({map:map, path: locationPoints, strokeColor: 'blue', strokeOpacity: 0.8, strokeWeight: 2, fillColor: 'blue', fillOpacity: 0.35});
		
		var polygon = new esri.geometry.Polygon();
		polygon.addRing(locationPoints);
		
		
		var symbol = new esri.symbols.SimpleFillSymbol();
		symbol.setColor(new dojo.Color([255,0,0,0.25]));   
		
		var graphic = new esri.Graphic(polygon, symbol);
		
		var polygonGraphicsLayer = new esri.layers.GraphicsLayer({id:'polygonGraphicsLayer'});
		polygonGraphicsLayer.add(graphic);
		map.addLayer(polygonGraphicsLayer);
	}
}
 function threeWeiClick(){
 	var mapStyleDiv = document.getElementById("mapStyleDiv");
 	if(mapStyleDiv.style.display == 'none') {
 		mapStyleDiv.style.display = 'block';
 	}else {
 		mapStyleDiv.style.display = 'none';
 	}
 }
var currentMapStyleObj;
//获取arcgis地图路径的配置信息
function getArcgisInfo(){
	$.ajax({   
		 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getArcgisInfo.json?t='+Math.random(),
		 type: 'POST',
		 timeout: 3000,
		 dataType:"json",
		 async: false,
		 error: function(data){
		 	$.messager.alert('友情提示','地图配置信息获取出现异常!','warning'); 
		 },
		 success: function(data){
		 	var aci = eval(data.arcgisConfigInfos);
		 	arcgisConfigInfos = new Array();
		 	var mapTypeTemporary=0;
		 	var mapTypeN=0;
		 	for(var i=0; i<aci.length; i++){
		 		if(mapType == "") {
		 			if(i==0){
		    			arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeTemporary = aci[i].mapType;
		    			mapTypeN++;
		    		}else if(mapTypeTemporary == aci[i].mapType) {
		    			arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		    		}
		 		}else if(mapType == "2") {
		 			if(aci[i].mapType == 5) {
		 				arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		 			}
		 		}else if(mapType == "3") {
		 			if(aci[i].mapType == 30) {
		 				arcgisConfigInfos[mapTypeN] = aci[i];
		    			mapTypeN++;
		 			}
		 		}else {
		 			arcgisConfigInfos=aci;
		 		}
		 	}
		    var htmlStr = "";
		    for(var i=0; i<arcgisConfigInfos.length; i++){
		    	if(i==0){
		    		htmlStr += "<span class=\"current\" onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}else {
		    		htmlStr += "<span onclick=\"switchArcgisByNumber(this,"+i+")\">"+arcgisConfigInfos[i].mapTypeName+"</span>"
		    	}
		    }
		    var mapStyleDiv = document.getElementById("mapStyleDiv");
		    mapStyleDiv.innerHTML = htmlStr;
		    $("#mapStyleDiv").width(60*arcgisConfigInfos.length+8)
		    
		    if(htmlStr!=""){
		    	currentMapStyleObj = mapStyleDiv.getElementsByTagName('span')[0]
		    }
		    
		    if(arcgisConfigInfos.length > 0) {
		    	loadArcgis(arcgisConfigInfos[0],"map","jsSlider","switchMap");
		    }
		 }
	 });
}

function switchArcgisByNumber(obj,n) {
	if(currentMapStyleObj != obj){
		currentMapStyleObj.className = "";
		obj.className = "current";
		currentMapStyleObj = obj;
		switchArcgis(arcgisConfigInfos[n],n);
	}
}

function getArcgisDataByCurrentSet(){
	getArcgisOfGridDatas(document.getElementById("gridId").value,currentArcgisConfigInfo.mapType);

	setTimeout(function() {
		setMapArcgisPoint();
	},1000);
	
}

function setMapArcgisPoint() {

	var x = $('#x').val();
	var y = $('#y').val();
    var mapCenterLevel = $('#mapCenterLevel').val();
	if(x == 0 && y == 0) {
		x = currentArcgisConfigInfo.mapCenterX;
		y = currentArcgisConfigInfo.mapCenterY;
	}
	if(mapCenterLevel == 0){
        mapCenterLevel =currentArcgisConfigInfo.zoom;
	}
	$('#x').val(x);
	$('#y').val(y);
	$('#mapt').val(currentArcgisConfigInfo.mapType);
	if(isEdit == "true" || isEdit == true){
		isEdit = true;
	}else {
		isEdit = false;
	}
	$("#map").ffcsMap.onClickGetPoint(x,y,'${rc.getContextPath()}/js/map/arcgis/library/style/images/RedShinyPin.png', pointGetCallBack,isEdit);
	
	
	
	$("#map").ffcsMap.centerAt({
		x : parseFloat(x),          //中心点X坐标
		y : parseFloat(y),           //中心点y坐标
		wkid : 4326, //wkid 2437
		zoom : mapCenterLevel
	});
		    	
}

function pointGetCallBack(data) {
	var xys = data.toString().split(",");
	var x = xys[0];
	var y = xys[1];
	$('#x').val(x);
	$('#y').val(y);
    setTimeout(function() {
        loadXIEJINGAddrss(x, y);
    },100);

	//$('#mapt').val(arcgisMapConfigInfo.mapStartType);
}

function endPointLocate() {
	var x = $('#x').val();
	var y = $('#y').val();
	var mapt = $('#mapt').val();
	
	var data = 'x='+x+'&y='+y+'&mapt='+mapt;
	var XIEJINGAddress = $("#XIEJINGAddress").val();
    var XIEJINGregionCode = $("#XIEJINGregionCode").val();
	var targetDownDivId = "<#if targetDownDivId??>${targetDownDivId}</#if>";

	if(typeof XIEJINGAddress != "undefined" && XIEJINGAddress != null && XIEJINGAddress != ''){
		data = data + '&address='+XIEJINGAddress;
	}
    if(typeof XIEJINGregionCode != "undefined" && XIEJINGregionCode != null && XIEJINGregionCode != ''){
        data = data + '&regionCode='+XIEJINGregionCode;
    }
    if(typeof targetDownDivId != "undefined" && targetDownDivId != null && targetDownDivId != ''){
        data = data + '&targetDownDivId='+targetDownDivId;
    }
	var url;
	if(callBackUrl != ''){
		url = callBackUrl+'?'+ data;
	}else {
		url = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml?'+ data;
	}
	$('#cross_domain_frame').attr('src', url);
	//parent.closeMaxJqueryWindow(); // 关闭弹出框

}

function loadXIEJINGAddrss(x, y){
	var list = null;
    var postData = {};
    var x = $("#x").val();
    if(x!=null && x!="") {
        postData["x"]=x;
    }
    var y = $("#y").val();
    if(y!=null && y!="") {
        postData["y"]=y;
    }

    modleopen();
    $.ajax({
        url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getAuxiliaryPoliceAddrsByXY.json?',
        type: 'POST',
        data: postData,
        dataType: "json",
        async: false,
        error: function (result) {
            $.messager.alert('友情提示', '获取协警地址异常!', 'warning');
        },
        success: function (result) {
            //$("#tipMessage").html("您未选择地址！默认第一条！");
            modleclose();//关闭遮罩层
            $('#pagination-num').text(1);
			if(result != null){
                $('#records').text(result.total);
                var totalPage = 1;
                $('#pageCount').text(totalPage);
                list=result.rows;
			}else{
                $('#records').text(0);
                var totalPage = 1;
                $('#pageCount').text(1);
			}
            var tableBody="";
            if(list && list.length>0) {
                tableBody+='<ul style="width:100%;">';
                for(var i=0;i<list.length;i++){
                    var val=list[i].addressPathName;
                    var regionCode=list[i].regionCode;
//					if(i==0){
//                        $("#XIEJINGAddress").val(val);
//					}
                    var addressName = val;
                    tableBody+='<li ';
                    if(addressName!=null && addressName.length>22){
                        addressName = addressName.substring(0,22)+"...";
                    }
                    tableBody+='onclick="selectRow(\''+val+'\',\''+regionCode+'\',this)" title="'+val+'"> '+ addressName +'</li>';
                }
                tableBody+='</ul>';
            } else {
                $("#tipMessage").html("未获取到附近地址！");
                tableBody+='<div class="nodata" style="width: 174px;text-align: center;"></div>';
            }
            $(".LeftTree .mCSB_container").html(tableBody);


        },
        error:function(data){
            var tableBody='<div class="liebiao"><ul>数据读取错误！！！</ul></div>';
            $("#content-md").html(tableBody);
        }

    });
}

function getArcgisOfGridDatas(gridId,mapt) {
		var url =  '${rc.getContextPath()}/zhsq/map/arcgis/arcgisdata/getArcgisDataOfGridForPoint.json?mapt='+mapt+'&wid='+gridId;
		$("#map").ffcsMap.render('gridLayer',url,2,true);
}

	function selectRow(XIEJINGAddress, regionCode,obj){
		if(typeof XIEJINGAddress != 'undefined' && XIEJINGAddress != null && XIEJINGAddress != ''){
			$("#XIEJINGAddress").val(XIEJINGAddress);
            $("#XIEJINGregionCode").val(regionCode);

			$(".LeftTree .mCSB_container ul li").removeClass("selectRow");
			$(obj).addClass("selectRow");
			var title,value;
            title = "您选择的地址是：" + XIEJINGAddress;
			if(XIEJINGAddress.length>15){
				value = "您选择的地址是：" + XIEJINGAddress.substring(0,15)+"...";;
			}else{
                value = "您选择的地址是：" + XIEJINGAddress;
			}
			$("#tipMessage").html(value);
            $("#tipMessage").attr("title",title);
        }
	}
</script>
<#include "/component/maxJqueryEasyUIWin.ftl" />
</html>
