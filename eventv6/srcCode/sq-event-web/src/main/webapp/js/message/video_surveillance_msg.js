//	setInterval("syncAlarmInfo()",10000);
//setTimeout("syncAlarmInfo_nanping()",10000);

//	var orgCode = $("#orgCode").val();
//	if(typeof(orgCode) != 'undefined' && orgCode != null && orgCode != ''){
//		var orgStart = orgCode.indexOf("3507");//南平
//		if(orgStart == 0){//南平
//			setTimeout("syncAlarmInfo_nanping()",10000);
////			setInterval("syncAlarmInfo_nanping()",10000);
//		}else if(orgStart == -1){//其他地市（需要根据对接的厂商去修改syncAlarmInfo里面的具体实现）
//			//setTimeout("syncAlarmInfo()",10000);
//		}
//	}

	//使用功能配置去调用
	if(SURVEILLANCE_MSG_METHOD){
		setInterval(SURVEILLANCE_MSG_METHOD,5000);
	}

var _result;

function syncAlarmInfo_ivm(){
	$.ajax({
		type: "POST",
		url: js_ctx + '/zhsq/alarm/videoSurveillanceController/ivm/getData.json',
		//data: data,
		dataType:"json",
		async: true,
		success: function(result){
			var data = jQuery.parseJSON(result);
			if(data.returnCode=='1' && data.ivmFaces.length > 0){
				if(_result != result){
					_result = result;
					showFade_ivm();
				}else{
					return;
				}
			}
		}
	});
}

function showFade_ivm(){
	var mapt = currentArcgisConfigInfo.mapType;
	var url = "/zhsq/alarm/videoSurveillanceController/ivm/index.jhtml?mapt="+mapt;
	var obj = showMaxJqueryWindow("人脸对比详情",js_ctx + url,640,490);
}
document.onkeydown = function disableRefresh(evt){
	evt = (evt) ? evt : window.event;
	if (evt.keyCode) {
		if(evt.keyCode == 80){
			//syncAlarmInfo('p');
			syncAlarmInfo_ivm();
		}
	}
}

	//其他地市（需要根据对接的厂商去修改）
	function syncAlarmInfo(key){
		var now = new Date();
        var date =new Date(now.getTime() - 10*1000);
		var startTime = CurentTime(date);
		var endTime = CurentTime(now);
		var data = {
			key : key,
			startTime : startTime,
			endTime : endTime
		};
		$.ajax({
			type: "POST",
			url: js_ctx + '/zhsq/alarm/videoSurveillanceController/getData2.json',
			data: data,
			dataType:"json",
			async: true,
			success: function(result){
				if(_result != result){
					_result = result;
					//				console.log(result);
					var d = jQuery.parseJSON(result);
					if(d.hitFaces!=''){
//					showFade(result);
//					console.log("---"+d.returnCode);
						var hitFaces = d.hitFaces;
//					console.log("---"+hitFaces[0].deviceNum);
						showFade2(result,key);
						gisPosition(hitFaces[0].deviceNum);
					}
				}else{
					return;
				}
			}
		});
	}

	function showFade(){
	//		var d = JSON.stringify(result);
	//		console.log(d);
	//		console.log(result);
		var mapt = currentArcgisConfigInfo.mapType;
		var url = "/zhsq/alarm/videoSurveillanceController/index.jhtml?mapt="+mapt;
	//		sendMsg(url);
		var obj = showMaxJqueryWindow("人脸对比详情",js_ctx + url,640,490);
	}

	function showFade2(result,key){
		var d = JSON.stringify(result);
		var mapt = currentArcgisConfigInfo.mapType;
		var url = js_ctx + "/zhsq/alarm/videoSurveillanceController/index2.jhtml?key="+key+"&mapt="+mapt+"&data=";
		//sendMsg(url);
		showMaxJqueryWindow("人脸对比详情",url,640,490);
	}

	//南平--对接厂商ffcs
	function syncAlarmInfo_nanping(){
		var now = new Date();
		var date =new Date(now.getTime() - 10*1000);
		var startTime = CurentTime(date);
		var endTime = CurentTime(now);
		var data = {
			startTime : startTime,
			endTime : endTime
		};
		
//		var r = '{"data":[{"camera_id":"101","globalpic_image_url":"Global_Eyes/101/FaceRecognition-02-20161214104837002_GlobalPic.jpg","candidatepic_image_url":"Global_Eyes/101/FaceRecognition-02-20161214104837002_CandidatesPic1.jpg","facerecognition_image_url":"Global_Eyes/101/FaceRecognition-02-20161214104837002.jpg","name":"叶忠帅","sex":"男","similarity":"86%","card_type":"identification card","card_number":"123456","check_time":"2016-12-14 10:48:37"}],"request_id":"1453281172407","result_code":"0","result_desc":"request successful","timestamp":"2016-01-20 17:12:52"}';
//		var d = jQuery.parseJSON(r);
//		if(d.data!=null){
//			var data = d.data;
//			
//		}
//		console.log(d.data);
		
		$.ajax({
			type: "POST",
			url: js_ctx + '/zhsq/alarm/videoSurveillanceController/getData.json',
			data: data,
			dataType:"json",
			async: true,
			success: function(result){
//				var result = '{"data":[{"camera_id":"101","globalpic_image_url":"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007_GlobalPic.jpg","candidatepic_image_url":"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007_CandidatesPic1.jpg","facerecognition_image_url":"ivm_static/Global_Eyes/101/FaceRecognition-01-20170121153538007.jpg","name":"20161228204039","sex":"男","similarity":"100%","card_type":"identification card","card_number":"1234567","check_time":"2017-01-21 15:35:38"}],"request_id":"1485155392140","result_code":"0","result_desc":"request successful","timestamp":"2017-01-23 15:09:52"}';
				if(result!=null){
					var d = jQuery.parseJSON(result);
//				console.log(d.data);
					if(d.data!=null&&d.data!=undefined){
//					gisPosition1(hitFaces[0].deviceNum,result);
						showFade();
					}

				}
				//console.log(result);

//				if(d.hitFaces!=''){
//					var hitFaces = d.hitFaces;
//					gisPosition1(hitFaces[0].deviceNum,result);
//					
//					showFade(result);
//				}
			}
		});
	}
	
	function gisPosition1(res,result){
		if (res==""){
			return ;
		}
		console.log('---'+mapMenu.get("hitFace"));
		var hitFace = mapMenu.get("hitFace");
		
		var url = js_ctx+"/zhsq/map/arcgis/arcgisDataOfSituationController/getArcgisOfGlobalEyesByDeviceNum.jhtml?deviceNum="+res+"&showType=2&data="+result;
		currentLayerLocateFunctionStr="getArcgisDataOfZhuanTi('"+url+"','"+hitFace+"')";
		getArcgisDataOfZhuanTi(url,hitFace,475,406);
	}

	function gisPosition(res){
//		clearSpecialLayer(layerName);
		if (res==""){
			return ;
		}
		var qqyurl= js_ctx + "/zhsq/alarm/videoSurveillanceController/getGisEyesByDeviceNum.jhtml?deviceNum="+res+"&showType=2";
		currentLayerLocateFunctionStr="getArcgisDataOfGlobalEyes('"+qqyurl+"')";
		getArcgisDataOfGlobalEyes(qqyurl);
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

	function showGlobalEye(deviceNum){
		var deviceNums = "";
		if(typeof(deviceNum) == 'object'){
			deviceNums = deviceNum.elementsCollectionStr;
		}else{
			deviceNums = deviceNum;
		}
		//deviceNums = '34020000001320000020';
		var now = new Date();
		var appsecret="111111";

		var account="test1";
		var app_key = "test_key";//分配给应用的AppKey
		var format = "json";//可选，指定响应格式。默认json,目前支持格式为json
		var method = "device/getDevUrl";//API接口名称
		var password="MTExMTEx";//密码
		var sign_method = "md5";//参数的加密方法选择，可选值是：md5、hmac，默认md5
		var timestamp = CurentTime(now);//当前时间戳.
		var v = "1.0";//API协议版本，可选值:1.0
		//var deviceNum = "34020000001320000040";

		var paramsStr = appsecret+"account"+ account +"app_key"+app_key+"deviceNum"+deviceNum+"format"+format+"method"+method+"password"+password+"sign_method"+sign_method+"timestamp"+timestamp+"v"+v+appsecret;
		var sign = "";//签名结果，具体参见签名
		sign = $.md5(paramsStr).toUpperCase();//"78C3F87FF2AAADC10022D07FF45FA6BC";//
		var data = {
			sign : sign,
			account : account,
			app_key : app_key,
			deviceNum : deviceNum,
			format : format,
			method : method,
			password : password,
			sign_method : sign_method,
			timestamp : timestamp,
			v : v
		};
		var rtspURL = "rtsp://192.168.18.181:5531/ffcs/l_1616";
		//var url = js_ctx + "/zhsq/alarm/videoSurveillanceController/getDevUrl.jhtml";
		var url = js_ctx + "/zhsq/alarm/videoSurveillanceController/globalEyesShowUrl.jhtml?deviceNums="+deviceNums;
		//var url = 'http://59.60.28.227:8082/cms/router/rest?sign='+sign+'&account='+account+'&app_key='+app_key+'&format='+format+'&method='+method+'&password='+password+'&sign_method='+sign_method+'&timestamp='+timestamp+'&v='+v;
		$.ajax({
			type : "POST",
			url : url,
			//data : data,
			//contentType : "application/json; charset=utf-8",
			dataType:"json",
			async : true,
			success : function(data){
				if(data.rtspUrl != null && data.rtspUrl != ''){
					showGlobalEyeInfo(data.rtspUrl);
				}else{
					$.messager.alert("提示", '获取不到播放地址(摄像头不在线)!',"error");
				}
			},
			error : function() {
				$.messager.alert("提示","请求错误！","error");
			}
		});
	}

	function sendMsg(url){
		$.ajax({
			type: "POST",
			url: js_ctx + '/zhsq/alarm/videoSurveillanceController/sendMsg.json?url='+url,
			data: "",
			dataType:"json",
			async: true,
			success: function(result){
				console.log(result);
			}
		});
	}

	function showGlobalEyeInfo(rtspUrl){
		showMaxJqueryVideoWindow("全球眼查看", rtspUrl, 400, 400, false);
	}
    //
	//function  byte[] hex2byte(byte[] b) {
	//	if ((b.length % 2) != 0)
	//		throw new IllegalArgumentException("长度不是偶数");
	//	byte[] b2 = new byte[b.length / 2];
	//	for (int n = 0; n < b.length; n += 2) {
	//		String item = new String(b, n, 2);
	//		// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
	//		b2[n / 2] = (byte) Integer.parseInt(item, 16);
	//	}
	//	return b2;
	//}


	//var str = "abcdef";
	//var val="";
	//if ((str.length % 2) != 0)
	//	alert("长度不是偶数");
	//for(var i = 0; i < str.length; i += 2){
	//	var subStr = str.substring(i,2);
	//	val = val + parseInt(subStr,16);
	//}
	//alert(val);


	//var str = "bar2baz3foo1";
	//var val="";
	//for(var i = 0; i < str.length; i++){
	//	if(val == "")
	//		val = str.charCodeAt(i).toString(16);
	//	else
	//		val += str.charCodeAt(i).toString(16);
	//}
	//alert(val);

	function stringToBytes ( str ) {
		var ch, st, re = [];
		for (var i = 0; i < str.length; i++ ) {
			ch = str.charCodeAt(i);  // get char
			st = [];                 // set up "stack"
			do {
				st.push( ch & 0xFF );  // push byte to stack
				ch = ch >> 8;          // shift value down by 1 byte
			}
			while ( ch );
			// add stack contents to result
			// done because chars have "wrong" endianness
			re = re.concat( st.reverse() );
		}
		// return an array of bytes
		return re;
	}

	function Str2Bytes(str){
		var pos = 0;
		var len = str.length;
		if(len %2 != 0){
			return null;
		}
		len /= 2;
		var hexA = new Array();
		for(var i=0; i<len; i++){
			var s = str.substr(pos, 2);
			var v = parseInt(s, 16);
			hexA.push(v);
			pos += 2;
		}
		return hexA;
	}
