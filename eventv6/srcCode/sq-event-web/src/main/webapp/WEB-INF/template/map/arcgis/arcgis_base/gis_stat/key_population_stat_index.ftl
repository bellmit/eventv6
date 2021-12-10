<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综治.楼房信息</title>
<link href="${rc.getContextPath()}/ui/css/normal.css" rel="stylesheet" type="text/css" />
<link href="${rc.getContextPath()}/theme/arcgis/gisstat/tjstyle.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/charts/FusionCharts/FusionCharts.js"></script>
</head>
<body style="width:100%;height:100%;border:none;" >
	
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<div id="gridmapstatistics" style="height:100%;" >
		<div >
           <label class="LabName" style="width:60px;"><span style="font-size: 15px;">网格：</span></label><input name="gridName" id="gridName" type="text" class="inp1 InpDisable easyui-validatebox" style="width:187px;" data-options="required:true" readonly value="<#if gridName??>${gridName}</#if>"/>
        </div>
		<!-- <div id="datastatisticsMap" style="height:200px;margin-left: 1px;" align="center"></div>
	 	<div id="descTab" style="margin:5px;" align="center"></div>
	 	-->
	    <div id="columnDiv"  style="height:240px;margin-left: 1px;" align="center"></div>
	    <div id="descTab" style="margin:5px;" align="center"></div>
		<div id="tab"  align="center"></div>
	</div>
</body>
<#include "/component/ComboBox.ftl">
<script type="text/javascript">

AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
	if(items!=undefined && items!=null && items.length>0){
		setTimeout(function(){
			showChar();
			gisPosition();
		},1000);
	}
});

showChar();
gisPosition();
	//显示图形
	function showChar() {
		var gridId = $("#gridId").val();
		var elementsCollectionStr = $("#elementsCollectionStr").val();
    	$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/gisstat/gisStat/getKeyPopulationStatCount.json',   
			 type: 'POST',
			 timeout: 300000, 
			 data:{gridId:gridId,elementsCollectionStr:elementsCollectionStr},
			 error: function(){   
				 $.messager.alert('友情提示','数据读取失败,详情请查看后台日志!','warning');
			 },   
			 success: function(result){
			 	/*  
				var str="";
				str+="<set label='"+result[0].label+"' value='"+result[0].count+"' />";	
				str+="<set label='"+result[0].label1+"' value='"+result[0].count1+"' />";	
				 var text_chart=" <chart palette='4' decimals='0' enableSmartLabels='1' isSmartLineSlanted='0.1' skipOverlapLabels='1' isSmartLineSlanted='0' baseFontSize='12' formatNumberScale='0' enableRotation='0'  bgAlpha='40,100' bgRatio='0,100' numberSuffix='' bgAngle='360' showBorder='10' startingAngle='70' >"+str+"</chart>";
				 var chart = new FusionCharts("${rc.getContextPath()}/js/charts/FusionCharts/Pie3D.swf", "ChartId", "300", "200", "0", "0");
				 chart.setDataXML(text_chart);	
				 chart.configure("ChartNoDataText", "暂无数据");
				 chart.render("datastatisticsMap");
				 */
				 if(result.length>1){
					 var columnStr="";
					 for(var i=1;i<result.length;i++) {
					 	columnStr+="<set label='"+result[i].label+"' value='"+result[i].value+"' />";	
					 }
					 var text_column = '<chart caption="'+result[0].chartName+'" xAxisName="" yAxisName="" numberPrefix="" animation="1" rotateYAxisName="1">'+columnStr+'</chart>';
					 var columnChart = new FusionCharts("${rc.getContextPath()}/js/charts/FusionCharts/Column3D.swf", "ChartId", "350", "240", "0", "0");
					 columnChart.setDataXML(text_column);	
					 columnChart.configure("ChartNoDataText", "暂无数据");
					 columnChart.render("columnDiv");
				 }
				 
				 
		 		var tabStr="";
				if(result.length>0) {
					tabStr+='<table width="100%" border="0" cellspacing="0" cellpadding="0" class="rk-table">';
					tabStr+='<tr><td align="center" colspan="2">'+result[0].name+'</td></tr>';
					tabStr+='<tr class="rk-table-ys"><td align="right" width="110px">'+result[0].label+'</td><td align="center">'+result[0].count+'户</td></tr>';
					tabStr+='<tr class="rk-table-ys"><td align="right">'+result[0].label1+'</td><td align="center">'+result[0].count1+'户</td></tr>';
					tabStr+='<tr class="rk-table-ys"><td align="right">'+result[0].label2+'</td><td align="center">'+result[0].count2+'%</td></tr>';
					tabStr+='</table>';
				}
				document.getElementById('tab').innerHTML=tabStr;	  
			 }
		});
    }
function gisPosition(){
	var gridId = $('#gridId').val();
	var postData = '?gridId='+gridId;
	var url="${rc.getContextPath()}/zhsq/map/gisstat/gisStat/getKeyPopulationStatLayer.json"+postData;
	window.parent.getPrecisionPovertyLayer(url,$('#elementsCollectionStr').val());
}
</script>
</html>
