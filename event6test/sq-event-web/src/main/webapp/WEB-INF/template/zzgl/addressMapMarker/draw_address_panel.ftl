<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta HTTP-EQUIV="pragma" CONTENT="no-cache" />
    <title>地址标注编辑panel</title>
    <style type="text/css">
		*{margin:0; padding:0; list-style:none;}
		.AlphaBack1{background-color:rgba(0, 53, 103, 0.5); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8c003567',endColorstr='#8c003567');}
		.AlphaBack1{color:#fff}
		.AlphaBack1 select{color:#000}
		/*.AlphaBack1 tr td{padding-top:3px;padding-right:2px;}*/
		.inp1{width:100px; height:24px; line-height:24px; padding:0 3px; border:1px solid #666;}
		.button1{width:60px; height:28px; line-height:26px; text-align:center;}
		.NorToolBtn{padding:4px 7px 4px 25px; color:#fff; margin-left:5px;margin-right:5px; margin-top:0px;display:block; float:left; line-height:14px; background-repeat:no-repeat; background-color:#448aca; background-position:7px 5px;}
		.NorToolBtn:hover{color:#fff; text-decoration:none; background-color:#ff9f00;}
		.styleChangeA:hover{text-decoration : none}

		#resourcesDisplayDiv{width:160px; position:absolute; z-index:10000; top:32px; left:550px; display:none;}
		#resourcesDisplayDiv ul li{border-bottom:1px dotted #FFF; color:#fff; padding:5px; font-size:14px; font-weight:bold;}
		#resourcesDisplayDiv ul li span{float:left; margin-right:10px; margin-top:2px;}
	</style>
    <script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
    
	
    <link rel="stylesheet" href="${rc.getContextPath()}/js/cxcolor/css/jquery.cxcolor.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/3.8/js/esri/css/esri.css">
	<link rel="stylesheet" href="${rc.getContextPath()}/js/map/arcgis/library/mnbootstrap/css/maptools.css">
	<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/cxcolor/js/jquery.cxcolor.js" ></script>
</head>
<body>
	<div class="MapBar">
		<div class="con AlphaBack1" style="height:32px">
			<input type="hidden" id="gridId" value="${gridId}" />
            <input type="hidden" id="gridX" value="<#if gridX??>${gridX}</#if>" />
            <input type="hidden" id="gridY" value="<#if gridY??>${gridY}</#if>" />
            <input type="hidden" id="addressId" value="${addressId}" />
	    	<input type="hidden" id="FACTOR_STATION" value="<#if FACTOR_STATION??>${FACTOR_STATION}</#if>" />
	    	<input type="hidden" id="FACTOR_SERVICE" value="<#if FACTOR_SERVICE??>${FACTOR_SERVICE}</#if>" />
	    	<input type="hidden" id="FACTOR_URL" value="<#if FACTOR_URL??>${FACTOR_URL}</#if>" />
	    	<input type="hidden" id="parentGridId" value="${parentGridId}" /> 
	    	<input type="hidden" id="mapCenterLevel" value="${mapCenterLevel}" />

	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr style="float:left;">
	          	<td>
	          		<a href="#" onclick="save()" id="saveButton" class="NorToolBtn SmallSaveBtn">保存</a>
	          	</td>
	    		<td>
	          	 	<a href="#" onclick="drawCenterPoint()" id="drawCenterPoint" class="NorToolBtn SetCenterBtn">标注</a>
	          	</td>
                  <td>
                      <a href="#" onclick="cancleDrawCenterPoint()" style="display: none;" id="cancleDrawCenterPoint" class="NorToolBtn SetCenterBtn">取消标注</a>
                  </td>
	    		<td>
	          		<a href="#" onclick="deleteData()" id="deleteData" class="NorToolBtn DelBtn">删除标注点</a>
	          	</td>
	          	<td>
	          		<input type="hidden" id="mapt" value="" disabled="true" />
	    			<input type="hidden" id="x" value="" readonly="true"/>
	    			<input type="hidden" id="y" value="" readonly="true"/>
	    	 	</td>
	    	 	<td>
	    	 		<div class="MapLevel fl" style="width:120px">
		    	 		<ul>
			                <li><input name="displayLevel" type="checkbox" onclick="getMapDataOfAddressGrid();" id="displayLevel" /></span>所在网格轮廓</li>
		            	</ul>
	       			 </div>
	    	 	</td>
	    	 	<td>
	    	 		<div id="tipMessage" style="color:yellow;font-size:13px"></div>
	 				<div class="blind" style="color:yellow;font-size:13px;float:right;"></div>
	    	 	</td>
	    	 </tr>
	        </table>
	    </div>

	</div>    
    <div id="myMap" style="position:absolute; top:0px;bottom:0px;right:0px; left:0px; border: #ccc 1xp solid;">
    	<iframe data-iframe="true" name="iframeOfMapLoad" id="iframeOfMapLoad" src="${rc.getContextPath()}/zhsq/addressController/toMapOfDrawAddress.jhtml" style="width:100%;height:100%;" frameborder="0" allowtransparency="true"></iframe>
    </div>
</body>
<script>
var gridId = '${gridId!''}';

function getMapDataOfAddressGrid(){
	window.frames["iframeOfMapLoad"].getArcgisDataOfGrid();
}
//获取当前编辑的网格的信息
function loadArcgisData(addressId) {
    $("#cancleDrawCenterPoint").hide();
    var url = "${rc.getContextPath()}/zhsq/addressController/getAddressById.json?";
    var data = "addressId="+addressId;
    $("#tipMessage").html("");
    var tipMess = "";
	$.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType:"json",
        success: function(data){
            if(data != null && data.xieJingAddress != null && data.xieJingAddress.x >0 && data.xieJingAddress.y >0){
				$("#saveButton").show();
				$("#drawCenterPoint").show();
                $("#deleteData").show();

				if(data.mapCenterLevel != null){
                    $("#mapCenterLevel").val(data.mapCenterLevel);
				}
				if(typeof data.gridId != 'undefined' && data.gridId != null){
                    $("#gridId").val(data.gridId);
				}
                dataInit(data);
            }else{
                $("#saveButton").hide();
                $("#drawCenterPoint").show();
                $("#deleteData").hide();
                tipMess = "地址未标注！";
                window.document.getElementById("tipMessage").innerHTML = tipMess;
                if(data.gridX != null) {
                    $("#x").val(data.gridX);
                }
                if(data.gridY != null) {
                    $("#y").val(data.gridY);
                }
                if(data.gridId != null){
                    $("#gridId").val(data.gridId);
                }
                window.frames["iframeOfMapLoad"].hideCenterPointImg(data.gridX, data.gridY);
                //window.frames["iframeOfMapLoad"].showCenterPoint($("#x").val(),$("#y").val());

            }
			//地图中心点定位到地址标注点
            window.frames["iframeOfMapLoad"].locateCenterAndLevel(data.xieJingAddress, data.gridX, data.gridY);
        },
        error:function(msg){
            DivShow('获取地址标注数据出错！');
        }
    });
}

function dataInit(data) {
    if(data.xieJingAddress != null && data.xieJingAddress.x >0 && data.xieJingAddress.y >0) {
        $("#x").val(data.xieJingAddress.x);
        $("#y").val(data.xieJingAddress.y);
        window.frames["iframeOfMapLoad"].showCenterPoint(parseFloat(data.xieJingAddress.x),parseFloat(data.xieJingAddress.y));
    }else{
        if(data.gridX >0 && data.gridY >0) {
            $("#x").val(data.gridX);
            $("#y").val(data.gridY);
        }
        window.frames["iframeOfMapLoad"].hideCenterPointImg(data.gridX, data.gridY);
        //地图中心点定位到地址标注点
        //window.frames["iframeOfMapLoad"].locateCenterAndLevel(null, data.gridX, data.gridY);
	}

}

/**
*中心点函数
*/
function drawCenterPoint() {
    $("#drawCenterPoint").hide();
	$("#cancleDrawCenterPoint").show();
    $("#saveButton").show();
	var x = $("#x").val();
	var y = $("#y").val();
	window.frames["iframeOfMapLoad"].showCenterPoint(x,y);
	window.frames["iframeOfMapLoad"].drawPoint(pointCallBack,x,y);
}

/**
 * 取消编辑
 */
function cancleDrawCenterPoint() {
    $("#drawCenterPoint").show();
    $("#cancleDrawCenterPoint").hide();
	loadArcgisData($("#addressId").val());
    window.frames["iframeOfMapLoad"].cancleDrawPoint();
}


function getMapArcgisDatas() {
	window.frames["iframeOfMapLoad"].getMapArcgisData();
}


function pointCallBack(data) {
	var xys = data.toString().split(",");
	var x = xys[0];
	var y = xys[1];
	$('#x').val(x);
	$('#y').val(y);
}

function save(){
    $("#drawCenterPoint").show();
    $("#cancleDrawCenterPoint").hide();
	$("#tipMessage").html("");
	var x=$("#x").val(), y=$("#y").val();
	var colorNum = 0;

	if(!x||!y){
		DivShow('中心点未设置！');
		return;
	}

	var url = "${rc.getContextPath()}/zhsq/addressController/saveAddressMapData.json?";
	var data = "addressId="+$("#addressId").val()+"&x="+x+"&y="+y;
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: "json",
        success: function (data) {
            if (data.flag == true) {
                DivShow('保存成功！');
                window.frames["iframeOfMapLoad"].clearAddressLayer();
                window.frames["iframeOfMapLoad"].showCenterPoint(x, y);
                loadArcgisData($("#addressId").val());
            }
        },
        error: function (msg) {
            DivShow('保存失败！');
        }
    });
}

function deleteData() {
	$("#tipMessage").html("");
	var url = "${rc.getContextPath()}/zhsq/addressController/deleteAddressMapData.json?";
	var data = "addressId="+$("#addressId").val();
    var x=$("#x").val(), y=$("#y").val();
    if(!x||!y){
        DivShow('中心点未设置！');
        return;
    }

	if(!confirm('确定要删除?')){
		return false;
	}

	$.ajax({
	   type: "POST",
	   url: url,
	   data: data,
	   dataType:"json",
	   success: function(data){
		 if(data.flag==true){
			DivShow('删除成功！');
			window.frames["iframeOfMapLoad"].clearAddressLayer();
			loadArcgisData($("#addressId").val());
		 }if(data.flag==false){
			DivShow('删除失败！');
		 }
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
