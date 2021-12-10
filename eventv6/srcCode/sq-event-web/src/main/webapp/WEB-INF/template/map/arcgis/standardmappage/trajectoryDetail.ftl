<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设备信息</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
</head>
<iframe id="cross_domain_iframe" scrolling='no' frameborder='0' style='display:none;'></iframe>
<body style="background-color: #fff;">
	<div style="margin:2px 3px 0px 3px;">
		<div class="con ManagerWatch">
        	<div class="ManagerInfo">
                <ul>
                    <li style="width:245px;">
                    	<p><span style="width:200px">${deviceName}</span></p>
                    	<p><code>设备编号：</code>${deviceSn}</p>
                        <p><code>设备厂商：</code>${deviceManufacture}</p>
                        <p><code>所有人：</code>${ownerRsName}</p>
                        <p><code>所属网格：</code>${infoOrgCode}</p>
                        <p><code>备注：</code>${note}</p>
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
            <#--
            <div class="ManagerContact">
            	<ul>
                	<li class="GreenBg" onclick="dynamicTrajectoryQuery()"><img  src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_1.png" />实时轨迹定位</li>
                </ul>
                <div class="clear"></div>
            </div>
            -->
            <div class="h_10"></div>
            <div class="ManagerSearch">
            	<div class="nav">
                    <ul>
                        <li class="current">轨迹查询</li>
                    </ul>
                    <div class="line"></div>
                </div>
                <div class="con">
					<input type="hidden" id="appDeviceId" name="appDeviceId" value="${appDeviceId}"/>
                	<ul>
                    	<li><input type="text" id="start" onclick="WdatePicker({startDate:'${startTime}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true, alwaysUseStartDate:true})" class="Wdate" style="width:135px; height:26px; *height:24px; line-height:24px;" value="${startTime}"/></li>
                        <li>—</li>
                    	<li><input type="text" id="end" onclick="WdatePicker({startDate:'${endTime}', dateFmt:'yyyy-MM-dd HH:mm', readOnly:true, alwaysUseStartDate:true})" class="Wdate" style="width:135px; height:26px; *height:24px; line-height:24px;" value="${endTime}"/></li>
                        <li><a href="javascript:void(0);" onclick="trajectoryQuery()" class="btn"><img src="${uiDomain!''}/images/map/gisv0/special_config/images/mc_4.png" />查询</a></li>
                    </ul>
                    <div class="clear"></div> 
                </div>
            </div>
        </div>
	</div>
</body>
<script type="text/javascript">
	var isCross;
	<#if isCross??>
		isCross = '${isCross}';
	</#if>
	 
	 //
	 
	var zhsq_url = "${SQ_ZHSQ_EVENT_URL}";
	 
	 //
	 
	var current = $("#end").val();
	current = Date.parse(current.replace(/-/g,"/"));
	var currentDate = new Date(current); // 当前时间
	
	var currentYear = currentDate.getFullYear();//当前年份
	var currentMonth = currentDate.getMonth() + 1;// 当前月份
	
	var currentDate2 = new Date(currentYear, currentMonth, 0);
	var lastDate = currentDate2.getDate(); // 当前月最后一天
	
	var dateLine = new Date();
	dateLine.setFullYear(currentYear);
	dateLine.setMonth(currentMonth - 1);
	dateLine.setDate(lastDate);
	
	// 获取三个月前的时间
	function get3MonthBefor(currentDate){  
	   var resultDate,year,month,date,hms; 
	   year = currentDate.getFullYear();
	   month = currentDate.getMonth()+1;
	   date = 1;
	   switch(month)
	   {
			case 1:
			case 2:
				month += 10;
				year--;
				break;
			default:
			    month -= 2;
			 	break;
	   }
	   
	   month = (month < 10) ? ('0' + month) : month;
	   resultDate = year + '/' + month +'/' +date;
	   return new Date(resultDate);
	}
	
	function trajectoryQuery(){
		var start = $("#start").val();
		var end = $("#end").val();
		var appDeviceId = $("#appDeviceId").val();
		
		var tempDate = get3MonthBefor(currentDate);
		
		if (start > end) {
			alert("开始时间需要小于结束时间");
			return;
		}
		
		var start2 = Date.parse(start.replace(/-/g,"/"));
		var startDate = new Date(start2); // 开始时间
		
		var end2 = Date.parse(end.replace(/-/g,"/"));
		var endDate = new Date(end2); // 结束时间
		
		if (startDate.getTime() < tempDate.getTime() || startDate.getTime() > dateLine.getTime() || endDate.getTime() > dateLine.getTime()) {
			alert("只能查询三个月内（从" + (tempDate.getMonth() + 1) + "月1号开始）的轨迹数据");
			return;
		}
		
		var params = "?locateTimeBegin="+start+"&locateTimeEnd="+end+"&appDeviceId="+appDeviceId+"&t="+Math.random();
		
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisDataTrajectoryOfTrajectory.jhtml"+params;
		window.parent.getTrajectoryOfBoat(url,start,end);
	}
	
	//function dynamicTrajectoryQuery(){
	//	var start = '${startTime}';
	//	var end = '${endTime}';
	//	var appDeviceId = $("#appDeviceId").val();
	//	var params = "?locateTimeBegin="+start+"&appDeviceId="+appDeviceId+"&t="+Math.random();
		
	//	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofgoods/getArcgisDataTrajectoryOfTrajectory.jhtml"+params;
	//	window.parent.getDynamicTrajectoryOfBoat(url,start,end);
	//}
</script>
</html>
