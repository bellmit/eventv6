<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv=X-UA-Compatible content="IE=edge">
<title>迁徙图</title>
<link href="${rc.getContextPath()}/js/map/spgis/lib/migration/migration_map.css" rel="stylesheet" type="text/css" />
<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#container {
	height: 100%
}
</style>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/map/map.api.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/map/utils/calendar.js"></script>
</head>

<body style="width:100%;border:0;position:relative;overflow:hidden;background:#fcf9f2;">
<div id="container"></div>
<div class="movemain AlphaBack">
  <div class="search-wrap">
    <input type="radio" name="flowType" value="0" checked="checked"/>省内
    <input type="radio" name="flowType" value="1"/>省外
  </div> <!--end .search-wrap-->
  <div class="sortList-warp">
    <div class="button-box">
      <span class="btn-group"><a href="javascript:void(0);" onclick="showList(this);" listid="list1" class="current">迁入热市</a></span>
      <span class="btn-group"><a href="javascript:void(0);" onclick="showList(this);" listid="list2">迁出热市</a></span>
    </div><!--end .button-box-->
    <div id="list1" class="outer-wrap">
      
    </div><!--end .outer-wrap-->
    <div id="list2" class="outer-wrap" style="display:none;">
      
    </div><!--end .outer-wrap-->
  </div><!--end .sortList-warp-->
</div><!--end .movemain-->
<div class="timelinemain AlphaBack">
  <div class="timelinebox">
    <div class="leftArrow-btn"></div>
    <div class="rightArrow-btn"></div>
    <div class="midline"></div>
    <div class="swiper-container">
      
    </div><!--end .swiper-container-->
  </div><!--end .timelinebox-->
</div><!--end .timelinemain-->
</body>
<script type="text/javascript">
	$(function() {
		$(".outer-wrap").height($(window).height() - 78).width("100%");
		$("#container").height($(window).height() - 26);
		var btns = $(".button-box").find("a");
		btns.click(function() {
			btns.removeClass("current");
			$(this).addClass("current");
			
			queryData(getParams());
		});
		
		$('input[name="flowType"]').click(function() {
			queryData(getParams());
		});
		
		MMApi.LoadMap(parent.MMGlobal.ContextPath, parent.MMGlobal.SQ_ZZGRID_URL, "${SPGIS_IP!''}", "SpGisMap", function(mapApi) {
			mapApi.initMigrateDtMap("container");
			resetTimeAxis();
		});
	});
	
	function resetTimeAxis() {
		var obj = $(".swiper-container");
		obj.children().remove();
		
		var xDate = new XDate();
		for (var i = 6; i >= 0; i--) {
			xDate.addDays(-i);
			var lunar = calendar.solar2lunar(xDate.getFullYear(), xDate.getMonth() + 1, xDate.getDate());
			var html = '<div class="time-row" id="'+xDate.toString("yyyy-MM-dd")+'"><span class="solar-date">'+xDate.toString("MM.dd")+'</span><br /><span class="lunar-date">'+lunar.IMonthCn + lunar.IDayCn+'</span></div>';
			obj.append(html);
			xDate.addDays(i);
		}
		
		$(".leftArrow-btn").click(function() {
			var rows = $(".swiper-container div:visible:last").hide();
			var rowObj = $(".swiper-container div:visible:first");
			var date = new XDate(rowObj.attr("id"));
			date.addDays(-1);
			if ($("#" + date.toString("yyyy-MM-dd")).length > 0) {
				$("#" + date.toString("yyyy-MM-dd")).show();
			} else {
				var lunar = calendar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
				var html = '<div class="time-row" id="'+date.toString("yyyy-MM-dd")+'"><span class="solar-date">'+date.toString("MM.dd")+'</span><br /><span class="lunar-date">'+lunar.IMonthCn + lunar.IDayCn+'</span></div>';
				rowObj.before(html);
				rowObj.prev().click(timeEvent);
			}
		});
		$(".rightArrow-btn").click(function() {
			$(".swiper-container div:visible:first").hide();
			var rowObj = $(".swiper-container div:visible:last");
			var date = new XDate(rowObj.attr("id"));
			date.addDays(1);
			if ($("#" + date.toString("yyyy-MM-dd")).length > 0) {
				$("#" + date.toString("yyyy-MM-dd")).show();
			} else {
				var lunar = calendar.solar2lunar(date.getFullYear(), date.getMonth() + 1, date.getDate());
				var html = '<div class="time-row" id="'+date.toString("yyyy-MM-dd")+'"><span class="solar-date">'+date.toString("MM.dd")+'</span><br /><span class="lunar-date">'+lunar.IMonthCn + lunar.IDayCn+'</span></div>';
				rowObj.after(html);
				rowObj.next().click(timeEvent);
			}
		});
		
		resetTime();
		
		xDate = new XDate();
		xDate.addDays(-1);// 选择前一天的迁徙数据
		if ($("#" + xDate.toString("yyyy-MM-dd")).length > 0) {
			$("#" + xDate.toString("yyyy-MM-dd")).click();
		}
		
	}
	
	function resetTime() {
		var timeRows = $(".swiper-container").children();
		timeRows.unbind("click");
		timeRows.click(timeEvent);
	}
	
	function timeEvent() {
		var timeItems = $(".swiper-container").children();
		timeItems.css("color", "");
		timeItems.removeClass("selected");
		$(this).css("color", "red").addClass("selected");
		
		queryData(getParams());
	}
	
	function showList(obj) {
		$(".outer-wrap").hide();
		$("#" + $(obj).attr("listid")).show();
	}
	
	function getParams() {
		var _date = $(".swiper-container div.selected").attr("id");
		var xDate = new XDate(_date);
		var _listid = $(".button-box a.current").attr("listid");
		var isOutType = $('input[name="flowType"]:checked').val();
		return { date : xDate.toString("yyyyMMdd"), type : (_listid == "list2") ? "1" : "0", listid : _listid, isOutType : isOutType };
	}
	
	function queryData(params) {
		MMApi.removeAllMigrationLine();
		MMApi.initMigrateDtMapEx(params.isOutType);
		$.ajax({   
			url: "${rc.getContextPath()}/zhsq/map/arcgis/arcgis/findMigrationData.jhtml",
			type: 'post',
			dataType: "json",
			data : params,
			cache: false,
			error: function(data) {
				alert("系统无法获取迁徙数据！");
			},
			success: function(data) {
				var listObj = $("#" + params.listid);
				listObj.children().remove();
				if (data != null && data.length > 0) {
					for (var i = 0; i < data.length; i++) {
						var _html = '<div class="row" title="迁徙人数：'+(data[i]["P_CNT"]==null?"0":""+data[i]["P_CNT"])+'人，迁徙比例：'+data[i]["ratio"]+'"><span class="serial-number">'+(i+1)+'</span>\
						<a href="javascript:void(0);" class="city-name">'+(data[i]["FROM_CITY_NAME"]==null?"未知":data[i]["FROM_CITY_NAME"])+'</a>\
						<i class="c-arrow"></i>\
						<a href="javascript:void(0);" class="city-name">'+(data[i]["TO_CITY_NAME"]==null?"未知":data[i]["TO_CITY_NAME"])+'</a><span class="city-name" style="margin-left:3px; width:100px;">'+data[i]["ratio"]+'（'+data[i]["P_CNT"]+'）</span></div>';
						
						listObj.append(_html);
						
						if (data[i]["FROM_CITY_NAME"] != null && data[i]["TO_CITY_NAME"] != null) {
							MMApi.drawMigrationLine(data[i]["FROM_CITY_NAME"], data[i]["TO_CITY_NAME"]);
						}
					}
				}
			}
		});
	}
</script>
</html>
