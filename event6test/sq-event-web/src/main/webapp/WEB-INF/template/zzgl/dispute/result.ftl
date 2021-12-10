<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<#include "/component/commonFiles-1.1.ftl" />
<script src="${rc.getContextPath()}/js/layer/layer.js" type="text/javascript"></script>
<script src="${rc.getContextPath()}/js/xdate.js" type="text/javascript"></script>
</head>
<body>
<div id="m1">

</div>
<div id="m2">

</div>
</body>
<script type="text/javascript">
$(function(){
 	layer.load(0);
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/topDispute.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
			getWholeInfo();
			console.log(data);
			if(data!=null&&data.length>0){
				for(var i=0;i<3;i++){
					$('#m1').append(data[i].regionName+'目前发生矛盾纠纷案件'+data[i].allNum+'起，'+data[i].allNumRateStr+'；化解率为'+
							data[i].mediationRate+','+data[i].mediationNumRateStr+'，建议'+data[i].allNumAdvStr+','+data[i].mediationNumAdvStr+'<br/>');
				}
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
		}
	});
});

function getWholeInfo(){
	$.ajax({
		type: "POST",
		url : '${rc.getContextPath()}/zhsq/dispute/wholeInfo.jhtml',
		data: '',
		dataType:"json",
		success: function(data){
    		layer.closeAll('loading');
			console.log(data);
			var monthDispute = data.thismonth2Lastday - data.lastmonthInterval;
			console.log(monthDispute);
			var msg = "";
			if(monthDispute < 0){
				msg = "相较上月同期减少"+Math.abs(monthDispute)+"起，呈增多趋势";
			}else if(monthDispute = 0){
				msg = "与上月同期相同";
			}else if(monthDispute > 0){
				msg = "相较上月同期增加"+monthDispute+"起，呈增多趋势";
			}
			if(data != null){
				$('#m2').append('平潭整体风险指数<br/>');
				$('#m2').append('自2014年1月1日至'+data.previousMaxDate+'，平潭共计发生矛盾纠纷案件'+data.allNum+'起<br/>');
				$('#m2').append('本月截止'+data.previousDate+'号发生案件'+data.thismonth2Lastday+'起，'+msg+'<br/>');
				$('#m2').append('本月截止'+data.previousDate+'号发生案件类型最多的是城乡建设发展纠纷，建议相关部门采取有效的预防干预措施<br/>');
			}
		},
		error:function(data){
			$.messager.alert('错误','连接错误！','error');
		}
	});
}
</script>
</html>
