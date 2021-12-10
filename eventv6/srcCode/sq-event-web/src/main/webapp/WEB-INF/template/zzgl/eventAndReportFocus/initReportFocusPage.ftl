<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>入格列表</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-7.min.js"></script>
</head>

<body>
<div id="event"></div>
<div id="bi"></div>
<div id="mobile"></div>

<script type="text/javascript">
    $.ajax({
			type: "POST",
			url: '${rc.getContextPath()}/zhsq/eventAndReportJsonp/initReportFocusType.jhtml',
			dataType:"json",
			success: function(data) {
				$("#event").html(data);
			},
			error:function(data){
				$("#event").html('错误 连接超时');
			}
		});
	 $.ajax({
			type: "POST",
			url: '${SQ_BI_URL}/report/nanan/leader/initReportFocusType.jhtml',
			dataType:"json",
			success: function(data) {
				$("#bi").html(data);
			},
			error:function(data){
				$("#bi").html('错误 连接超时');
			}
		});
</script>
</body>
</html>