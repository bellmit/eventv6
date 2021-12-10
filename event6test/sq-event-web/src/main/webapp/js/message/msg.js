	setInterval("syncAlarmInfo()",60000);
//	showFade();

	function showFade(snapTime, deviceNote, name, data){
	    $.messager.show({
	        title:'报警信息',
	        msg: snapTime + '<br/>人脸识别设备：'+deviceNote+'<br/>捕获对象：'+name+'<br/><a id="alarm_a" style="cursor:pointer;border:1px solid #193C0B;background-color:#47AD14;color:#000;border-radius:3px;">点击查看</a><br/>',
//	        msg:'2016年6月12日<br/>人脸识别设备：测试机1<br/>捕获对象：测试黑名单1<br/><a style="cursor:pointer;border:1px solid #193C0B;background-color:#47AD14;color:#000;border-radius:3px;" onclick="showAlarmList()">点击查看</a><br/>',
	        timeout:0,
	        height:130,
	        showType:'fade'
	    });
	    $('#alarm_a').click(function(){
	    	var d = JSON.stringify(data);
	    	var url = js_ctx + "/zhsq/alarm/index.jhtml?data="+d;
			showMaxJqueryWindow("人脸对比详情", url, 800, 400, true);
	    });
	}
	
	function syncAlarmInfo(){
        var now = new Date();
        var date =new Date(now.getTime() - 60*1000);
		
		var beginTime = CurentTime(date);
		var endTime = CurentTime(now);
		
		$.ajax({
			type: "POST",
			url:js_ctx+'/zhsq/alarm/getData.json',
			data: "beginTime="+beginTime+"&endTime="+endTime,
			dataType:"json",
			async: true,
			success: function(data){
				if(data != null){
					var snapTime = data.stSnapTime;
					var deviceNote = data.equName;
					var name = data.strName;
					showFade(snapTime, deviceNote, name, data);
				}
			}
		});
	}
	
	function CurentTime(now)
    { 
        
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
       
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        var ss = now.getSeconds();          //秒
       
        var clock = year + "-";
        if(month < 10)
            clock += "0";
        clock += month + "-";
        if(day < 10)
            clock += "0";
        clock += day + " ";
        if(hh < 10)
            clock += "0";
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm + ":"; 
        if (ss < 10) clock += '0';
        clock += ss;
        return(clock); 
    } 