<!DOCTYPE html>
<html>
<head>
	<title>户籍人口</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${rc.getContextPath()}/js/echarts/echarts-all.js"></script>
	<style>
	body{
		color:#fff;
	}
	.fisright-chart1 {
		width:1000px;
		min-height: 381px;
		background:none;
	}
	.fisright-chart2{
		margin-left:495px;
		min-height: 381px;
		background:none;
	}
	.npAlarminfo{
		background:none;
	}
	#combobox_INPUTgridName_div_0{
		background-color:rgba(30,64,89,0.75) !important;
		//color:#fff !important;
	}
	input{
		background-color:transparent !important;
		color:#fff !important;
	}
	span{
		color:#fff !important;
	}
	#combobox_INPUTgridName_ul_0{
		height:300px !important;
		max-height:300px !important;
	}
	h5 {
   	 	font-weight: normal;
    	font-size: 14px;
    	padding: 0px 0 5px 0;
	}	
	iframe{
	color:#fff !important;
	}
	</style>
</head>
<body>
 <div class="npAlarminfo citybgbox bpsmain" style="width:1010px;">
        <div>
            <div class="fisright-chart1">
             	<iframe data-iframe="true" region="center" id="regionReportIframe" src="${BI_URL}/report/corp/regionReport?orgCode=${orgCode!}" style="width:100%;height:420px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
             </div>
            </div><!--end .fisright-chart1-->
           <!-- <div class="fisright-chart2" style="display:none;">
          	 <iframe data-iframe="true" region="center" id="capitalTypeIframe" src="${BI_URL}/report/corp/capitalType?orgCode=${orgCode!}" style="width:100%;height:420px; overflow:hidden; position:relative;" frameborder="0" allowtransparency="true"></iframe>
            </div>-->
        </div><!--end .buildright-->
 </div>
<script type="text/javascript"> 
$(function(){
	var windowsHeight = $(window).height(); 
 	$('#regionReportIframe').height(windowsHeight-50);	
 	//$('#capitalTypeIframe').height(windowsHeight-50);	
});

</script>
</body>

</html>
