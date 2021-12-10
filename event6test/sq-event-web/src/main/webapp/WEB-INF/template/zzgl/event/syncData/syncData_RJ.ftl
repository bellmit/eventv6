<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>榕基南平事件同步</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script src="${rc.getContextPath()}/js/syncData/jquery.console.js"></script>
</head>

<body>
起始条数：<input type="text" id="from" style="width:50px;" value=""/>
<input type="text" id="limit" style="width:50px;" value=""/>
<input type="button" onclick="startSync()" value="同步"/>
<input type="button" onclick="stopSync()" value="停止"/>

</body>

<script>
	var isStop = false;
	
	function startSync() {
		var form = $("#from").val();
		if (form == '') {
			form = 1;
		} else {
			form = parseInt(form);
		}
		var limit = $("#limit").val();
		if (limit == '') {
			limit = 20;
		} else {
			limit = parseInt(limit);
		}
		syncData(1, form, limit);
	}
	
	function stopSync() {
		isStop = true;
	}
	
	function syncData(index, from, limit) {
		isStop = false;
		$.log("第" + index + "次同步【" + from + " - " + limit + "】条...");
		$.ajax({
			type : "post",
			url : "${rc.getContextPath()}/zhsq/event/syncEventDataForRJController/excuteSync.jhtml",
			data : {from : from, limit : limit},
			dataType : "json",
			success : function(data) {
				if (data) {
					if (data.retcode == "0") {
						$.log("本次同步成功，大概剩下：" + (data.totalNum - from - limit) + "条！");
						// 发起下一次同步
						if (isStop) {
							$.log("停止同步！", "red");
						} else {
							syncData(++index, from + limit, limit);
						}
					} else {
						$.log(data.msg, "red");
						if (data.retcode == "8") {
							$.log("同步完毕!!!");
						}
					}
				}
			}
		});
	}
</script>

</html>



