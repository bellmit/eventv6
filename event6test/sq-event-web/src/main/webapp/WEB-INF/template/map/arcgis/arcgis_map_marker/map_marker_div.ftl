<style type="text/css">
	.mapTab{ padding-left:20px; cursor:pointer;}/*地图为标注状态下的图标*/
	.mapTab2{ padding-left:20px; cursor:pointer; color:#4489ca;}
	.mapTab3{ width:11px; height:20px;display:inline-block;}
</style>

<span class="Check_Radio mapTab2" onclick="showMap();" style="display: inline-block; float: none;"><b id="mapTab2">标注地理位置</b></span>

<input id="x" name="resMarker.x" type="hidden"  value=""/>
<input id="y" name="resMarker.y" type="hidden"  value=""/>
<input id="hs" name="hs" type="hidden"  value=""/>
<input id="mapt" name="resMarker.mapType" type="hidden"  value=""/>

<script type="text/javascript">
	getMarkerData();
	
	function getMarkerData(){
		var markerOperation = $("#markerOperation").val(); // 地图操作类型
		var id = $("#id").val();
		var module = $("#module").val(); // 模块
		if (typeof module == "undefined" || module == "") {
			module = 'EVENT_V1';
		}
		
		var showName = "标注地理位置";
		
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?markerOperation='+markerOperation+'&id='+id+'&module='+module+'&t='+Math.random(),
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning'); 
			 },
			 success: function(data) {
			 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
			 		if (data && data.x != "" && data.x != null) {
			 			showName = "修改地理位置";
			 		} else {
			 			showName = "标注地理位置";
			 		}
				} else if (markerOperation == 2) { // 查看标注
					showName = "查看地理位置";
				}
				if(data){
			 	if (data.x != "" && data.x != null) {
			 		$("#x").val(data.x);
			 	}
			 	if (data.y != "" && data.y != null) {
			 		$("#y").val(data.y);
			 	}
		 		if (data.mapt != "" && data.mapt != null) {
			 		$("#mapt").val(data.mapt);
			 	}}
			 }
		});
		 
		if(markerOperation == 3) {
			$("#mapTab2").html("");//为了展示可视部分
			$("#mapTab2").parent().addClass("mapTab3")
								  .css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
								  .attr("title", "查看地理位置");
		} else {
			$("#mapTab2").html(showName);
		}
	}

	function callBackOfData(x,y,hs,mapt) {
		$('#x').val(x);
		$('#y').val(y);
		$("#mapt").val(mapt);
		$("#hs").val(hs);
		var showName = "修改地理位置";
		$("#mapTab2").html(showName);
		try {
			closeMaxJqueryWindowForMapMarker();
		} catch(e) {}
	}
</script>
