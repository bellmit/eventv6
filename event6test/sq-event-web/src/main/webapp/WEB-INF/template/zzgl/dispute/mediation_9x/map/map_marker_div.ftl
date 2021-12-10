<style type="text/css">
	.mapTab{ padding-left:20px; cursor:pointer;}/*地图为标注状态下的图标*/
	.mapTab2{ padding-left:20px; cursor:pointer; color:#4489ca;}
</style>

<a class="Check_Radio mapTab2"><b id="mapTab2" onclick="showMap();">标注地理位置</b></a>

<input id="x" name="x" type="hidden"  value=""/>
<input id="y" name="y" type="hidden"  value=""/>
<input id="hs" name="hs" type="hidden"  value=""/>
<input id="mapt" name="mapType" type="hidden"  value=""/>

<script type="text/javascript">
	$(function(){
        getMapMarkerData();
	});

	
	function getMapMarkerData(){
		var id = $("#id").val(); // 业务id
		var module = $("#module").val(); // 模块
		var markerOperation = $("#markerOperation").val(); // 地图操作类型
		
		var showName = "标注地理位置";
		$(".mapTab2").addClass("mapTab2");
		
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/gis/getMapMarkerData.json?id='+id+'&module='+module+'&t='+Math.random(),
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地址库信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			 	if (data != null) {
				 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
						if (data.x != "" && data.x != null) {
							showName = "修改地理位置";
						} else {
							showName = "标注地理位置";
						}
					} else if (markerOperation == 2) { // 查看标注
                        if (data.x != "" && data.x != null) {
                            showName = "查看地理位置";
                        } else {
                            showName = "未标注地理位置";
                        }
					}

				 	if (data.x != "" && data.x != null) {
				 		$("#x").val(data.x);
				 	} else {
				 		$("#x").val("");
				 	}
				 	
				 	if (data.y != "" && data.y != null) {
				 		$("#y").val(data.y);
				 	} else {
				 		$("#y").val("");
				 	}
				 
			 		if (data.mapt != "" && data.mapt != null) {
				 		$("#mapt").val(data.mapt);
				 	}
                    try{
                        callbackMap(data.x,data.y);
                    }catch(e){

                    }
			 	}else{
                    showName = "未标注地理位置";
				}
				$("#mapTab2").html(showName);
			 }
		});
	}
	
	function callBackOfData(x,y,hs,mapt) {
		$('#x').val(x);
		$('#y').val(y);

		$("#mapt").val(mapt);
		$("#hs").val(hs);
		var showName = "修改地理位置";
		$("#mapTab2").html(showName);
		closeMaxJqueryWindowForMapMarker();
	}
</script>
