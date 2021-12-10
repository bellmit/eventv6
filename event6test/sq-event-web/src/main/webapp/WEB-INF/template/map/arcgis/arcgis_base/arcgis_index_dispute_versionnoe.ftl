<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>矛盾纠纷</title>
<#include "/component/commonFiles-1.1.ftl" />
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/xdate.js" type="text/javascript"></script>
<link href="${uiDomain!''}/css/special_arcgis_pingtan/style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="disputeMainer">
  <div class="countrymain" id="countrymain">
  </div><!--end .countrymain-->
  <div class="pingtan" id="pingtan">
<!--     <h3>平潭整体风险指数<i class="dagico"></i></h3> -->
<!--     <p>自2014年1月1日至2016年8月31日，平潭共计发生矛盾纠纷案件<span class="szlan">18980</span>起</p> -->
<!--     <p>本月截止6号发生案件785起，相较上月同期<span class="szadd">增加7起</span>，呈增多趋势</p> -->
<!--     <p>本月截止6号发生案件类型最多的是<span class="szadd">城乡建设发展纠纷</span>，建议相关部门采取有效的预防干预措</p> -->
  </div>
  <div class="danger-date" id="danger-date"></div>
<#if regionCode?index_of("341602002013") == 0>
	<#include "/map/arcgis/arcgis_base/subDispute/mayuanDispute.ftl" />
<#else>
	<#include "/map/arcgis/arcgis_base/subDispute/nanpingDispute.ftl" />
</#if>
  <div class="clearfloat"></div>
  <div class="chartmain">
    <div class="chart-con">
      <h2>类型占比分析</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/top10Pie.jhtml"></iframe></div>
    </div><!--end .chart-con-->
    <div class="chart-con" style="width:596px; background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg4.png) no-repeat;">
      <h2>纠纷舆情趋势</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/trendLine.jhtml"></iframe></div>
    </div><!--end .chart-con-->
    <div class="chart-con">
      <h2>纠纷化解分析</h2>
      <div class="chartimg"><iframe width="100%" height="100%" style="border:none;" src="${BI_DOMAIN}/report/disputeMediationEchartsController/resolutionRate.jhtml"></iframe></div>
    </div><!--end .chart-con-->
  </div><!--end .chartmain-->
</div><!--end .disputeMainer-->
</body>
</html>
<script type="text/javascript">
$(function(){
	getTopDispute();
});

function getTopDispute(){
 	layer.load(0);
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/topDispute.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
			getWholeInfo();
			if(data!=null&&data.length>0){
				for(var i=0;i<3;i++){
					var regionName = data[i].regionName;
					if(regionName.length>3) regionName = regionName.substring(0,3);
					var img = '';
					var cls = '';
					var color = '';
					if(data[i].riskNum<=3){
						cls = '3';
						color = 'green';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg8.png) no-repeat;';
					}else if(data[i].riskNum>3&&data[i].riskNum<=6){
						cls = '2';
						color = 'blue';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg7.png) no-repeat;';
					}else if(data[i].riskNum>6){
						cls = '1';
						color = 'red';
						img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg6.png) no-repeat;';
					}
					fillColor(data[i].regionCode, color);
					$('#countrymain').append('<div class="country-con"><h2>'
							+regionName+'</h2><dl><dt><i class="danger'+cls+'" style="'+img+'">'
							+data[i].riskNum+'</i><br /><span class="'+color+'">风险指数</span></dt><dd>矛盾纠纷案件'
							+data[i].allNum+'起,'
							+data[i].allNumRateStr+'<br />化解率为'
							+data[i].mediationRateStr+'%，'+data[i].mediationNumRateStr+'<br />建议'
							+data[i].allNumAdvStr+'，'+data[i].mediationNumAdvStr+'。</dd></dl></div>');
				}
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
    		layer.closeAll('loading');
		}
	});
}

function getWholeInfo(){
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/wholeInfo.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
    		layer.closeAll('loading');
			if(data != null){
				var riskNum = data.riskNum;
				var text = '';
				var img = '';
				if(riskNum >= 0 && riskNum < 5){
					text = '低风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg5.png) no-repeat';
				}else if(riskNum >= 5 && riskNum < 8){
					text = '中等风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg9.png) no-repeat';
				}else if(riskNum >= 8 && riskNum < 11){
					text = '高风险';
					img = 'url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_bg10.png) no-repeat';
				}
				$('#danger-date').css('background',img);
				$('#danger-date').html(text);
				var disputeTypeStr = '';
				if(data.monthMediationType!=null){
					disputeTypeStr = '<p>本月截止'+data.previousDate+'号发生案件类型最多的是<span class="szadd">'+data.monthMediationType.DISPUTE_TYPE_STR+'</span>，建议相关部门采取有效的预防干预措</p>';
				}
				var img = 'background:url(${uiDomain!''}/images/map/gisv0/special_arcgis_pingtan/images/dispute_danger'+riskNum+'.png) no-repeat;';
				$('#pingtan').append('<h3>'+data.regionName+'整体风险指数<i class="dagico" style="'+img+'"></i></h3><p>自2014年1月1日至'
						+data.previousMaxDate+'，'+data.regionName+'共计发生矛盾纠纷案件<span class="szlan">'
						+data.allNum+'</span>起</p><p>本月截止'+data.previousDate+'号发生案件'
						+data.thismonth2Lastday+'起，'+data.thismonth2LastdayMsg+'</p>'+disputeTypeStr);
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
    		layer.closeAll('loading');
		}
	});
}

</script>
