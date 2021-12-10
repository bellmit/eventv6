<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" type="text/css" href="${uiDomain!''}/css/style.css" />
<script src="${rc.getContextPath()}/js/map/spgis/lib/jQuery.md5.js"></script>
<style type="text/css">
    body{ font-family:Verdana, Geneva, sans-serif, "微软雅黑", "冬青黑体简体中文 W3";font-size:14px; color:#666;}
    .facemainer{ padding:10px;}
    .facemainer h2{ font-size:16px; text-align:center; padding:7px 0; color:#fff;}
    body,h2,h3,ul,li,p{ margin:0; padding:0;}
    ul,li{ list-style:none;}
    img{border:none;}
    .facecon-left{ float:left; width:252px; position:relative;}
    .facecon-left h2{ background:#57afe2;}
    .photobox{ width:250px; height:160px; margin-bottom:10px; border:1px solid #cfcfcf; }
    .faceinfo h3{ font-size:14px;color:#3266cd; background:#eff3f5; border-left:2px solid #3266cd; padding:8px;}
    .faceinfo ul li{font-size:12px; border-bottom:1px dashed #cfcfcf; padding:10px 0;}
    .faceinfo ul li .fname{font-size:16px; color:#fa1002; text-decoration:underline; font-weight:bold;}
    .nawid{ display:inline-block; width:90px; text-align:right;}
    .blue{color:#3266cd;}
    .facecon-rigt{ float:left; margin-left:40px; position:relative;}
    .facecon-rigt h2{ background:#acacac;}
    .manlx{ background:#cdeeff; padding:4px 7px;color:#3266cd; }
    .xsdbox{ background:url(${rc.getContextPath()}/images/face_bg1.png) no-repeat; width:84px; height:40px; color:#fff; position:absolute; text-align:center; padding:22px 0;left:240px; top:78px; z-index:3}
    .xsno{ font-size:18px; font-weight:bold;}
    .blacklist{ position:absolute; right:-60px; top:0px;}
    .blacklist ul li{border:1px solid #cfcfcf; position:relative; width:47px; height:44px; margin-bottom:10px;}
    .blacklist ul li.bcurrent{border:1px solid #ec2626}
    .bno{ background:#ff3333; color:#fff; position:absolute; left:0; bottom:0; display:inline-block; width:100%; text-align:center;}
    .clearfloat{ clear:both;}
    .btnbox{ padding:15px 0px; text-align:center;}
    .btnbox a{
        text-decoration : none;
        color : #fff;
	}
</style>
</head>
<body>
<div class="facemainer">
  <div class="facecon-left">
	  <input type="hidden" id="cirsName" name="cirsName"/>
      <input type="hidden" id="deviceNum" name="deviceNum"/>
      <input type="hidden" id="deviceName" name="deviceName"/>
      <input type="hidden" id="deviceAddr" name="deviceAddr"/>
      <input type="hidden" id="hanpenTimeStr" name="hanpenTimeStr"/>
      <input type="hidden" id="typeListStr" name="typeListStr"/>
      <input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId}</#if>"/>
      <input type="hidden" id="gridName" name="gridName" value="<#if gridName??>${gridName}</#if>"/>
      <input type="hidden" id="mapt" name="mapt" value="<#if mapt??>${mapt}</#if>"/>
      <input type="hidden" id="x" name="x" value=""/>
      <input type="hidden" id="y" name="y" value=""/>

      <h2>抓拍图像</h2>
	  <div class="photobox"><img id="photoImg" src="#" width="250" height="160" alt=""/></div>
	  <div class="faceinfo">
		  <h3>报警信息</h3>
		  <ul>
<!-- 			<li><lable class="nawid">设备编号：</lable><span class="blue" id="deviceNum" data="deviceNum"></span></li> -->
			<li><lable class="nawid">设备地址：</lable><span style="display:inline-block;width:160px;" class="blue" data="remark">未知</span></li>
			<li><lable class="nawid">进入视野时间：</lable><span class="blue" id="timestampStartStr" data="timestampStartStr">${startTimeInp!''}</span></li>
<!-- 			<li><lable class="nawid">离开视野时间：</lable><span class="blue" data="timestampEndStr">2013-12-10 17：46：25</span></li> -->
			<!--<li><lable class="nawid">历史出现次数：</lable><span class="blue" data="historyAppearTimes">1</span></li> -->
		  </ul>
	  </div>
  </div><!--end .facecon-left-->
  <div class="xsdbox">相似度<p class="xsno" id="similarity" data="similarity">88%</p></div>
  <div class="facecon-rigt">
	  <h2>黑名单图像</h2>
	  <div class="photobox"><img id="blackImg" src="#" width="250" height="160" alt=""/></div>
	  <div class="faceinfo">
		  <h3>人员基本信息</h3>
		  <ul>
			<li><lable class="nawid">姓名：</lable><span class="blue" id="name" data="name"></span></li>
			<li><lable class="nawid">证件号：</lable><span class="blue" id="person_id" data="person_id">350422197801235678</span></li>
		  </ul>
	  </div>
	  <div class="blacklist">
		  <ul id="blacklist">
		  </ul>
	  </div>
  </div>
  	<div class="clearfloat"></div>
    <div class="btnbox">
        <a href="###" class="btn btn_dispatch" onclick="eventDispatch();">一键联动</a>
        <a href="###" class="btn btn_yj" onclick="emergencyPlan();">应急预案</a>
        <a href="###" class="btn btn_video" onclick="showPointVideo();">查看视频</a>
	</div>
</div><!--end .facemainer-->
</body>
<script type="text/javascript">

$(function() {
	$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	var data = '${data!}';
	var REMARK = "${REMARK!}";
	var dataJson = JSON.parse(data);

	var cameraId = dataJson.ivmFaces[0].cameraId;
	var facerecognitionImageUrl = dataJson.ivmFaces[0].facerecognitionImageUrl;
    var globalpicImageUrl = dataJson.ivmFaces[0].globalpicImageUrl;
    var ivmFacesId = dataJson.ivmFaces[0].ivmFacesId;
    var hit_faces = dataJson.ivmFaces[0].hit_faces;
    var capture_time = dataJson.ivmFaces[0].capture_time;

    $("#remark").val(REMARK);
//    $("#timestampStartStr").html(REMARK);
    $("#deviceNum").val(cameraId);
//    $("#timestampStartStr").html(capture_time);
    $("#photoImg").attr("src", facerecognitionImageUrl);

    var name = hit_faces[0].name;
    var similarity = Math.round(parseFloat(hit_faces[0].similarity) * 100) + "%";
    var cardNumber = hit_faces[0].cardNumber;
    var candidatepicImageUrl = hit_faces[0].candidatepicImageUrl;

    $("#similarity").html(similarity);
    $("#name").html(name);
    $("#person_id").html(cardNumber);
    $("#blackImg").attr("src", candidatepicImageUrl);

    for(var i=0;i<hit_faces.length;i++){
        var st = '';
        if(i==0){//
            st = ' style="border:1px solid #FF0000;"';
        }
        var content = '<li'+st+' onclick="selectBlack(this,'+i+')"><img src="'+ hit_faces[i].candidatepicImageUrl+'" width="47" height="44" alt=""/><span class="bno">'+Math.round(parseFloat(hit_faces[i].similarity) * 100)+'%</span></li>';
        $('#blacklist').append(content);
    }

// 	getData();
});

function showImg1(obj,fieldId){
	var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId;
	var name = "图片查看";
	var paths = $(obj).attr("src");
	openPostWindow(url, paths, name);
// 	window.open("${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?paths="+ paths +"&fieldId="+ fieldId,"查看图片",'');
}

var r;
function getData(){
	var mapt = $('#mapt').val();
	layer.load(0);
	$.ajax({
		type: "POST",
		url: '${rc.getContextPath()}/zhsq/alarm/videoSurveillanceController/getBlackData.json?mapt='+mapt+'&data=${data!}',
		data: '',
		dataType:"json",
		async: true,
		success: function(result){
			layer.closeAll('loading');
// 			console.log(result[0]);
			r = result[0];
			var tags = result[0].similarFaceObjMap[0].tag;
            var typeListStr ="";//黑名单人员类型字符串
			for(var i=0;i<tags.length;i++){
				var content = '<span onclick="showDetail('+result[0].similarFaceObjMap[0].ciRsId+','+tags[i][0]+',\''+tags[i][1]+'\')" class="manlx">'+tags[i][1]+'</span>';
				if(i >= 5){
					$('#tags2').append(content);
				}else{
					$('#tags').append(content);
				}
				//吸毒  刑释解教  矫正   精神障碍患者  艾滋病
				if(tags[i][0] == "11" || tags[i][0] == "14" || tags[i][0] == "13" ||
					tags[i][0] == "8" || tags[i][0] == "21"){
					if(typeListStr != null && typeListStr != ""){
                        typeListStr = typeListStr + "、" + tags[i][1];
					}else{
                        typeListStr = tags[i][1];
					}
				}
			}
            if(typeListStr != null && typeListStr != ""){
                typeListStr = "【"+ typeListStr + "】";
            }
            $('#typeListStr').val(typeListStr);
			var similarFaceObjMap = result[0].similarFaceObjMap;
			for(var i=0;i<similarFaceObjMap.length;i++){
				var st = '';
				if(i==0){//
					st = ' style="border:1px solid #FF0000;"';
				}				
				var content = '<li'+st+' onclick="selectBlack(this,'+i+')"><img src="'+similarFaceObjMap[i].picture_uri+'" width="47" height="44" alt=""/><span class="bno">'+similarFaceObjMap[i].similarity.toFixed(1)+'%</span></li>';
				$('#blacklist').append(content);
			}
			//--
			$("#photoImg").attr('src',result[0].faceImageUri);
// 			console.log('result[0].pictureUrl-'+result[0].pictureUrl);
// 			window.parent.ImageViewApi.initImageView('photoImg',result[0].pictureUrl,null,null,'详细');
			$("#blackImg").attr('src',result[0].similarFaceObjMap[0].picture_uri);
			//--
			$("[data]").each(function(index, element) {
// 				console.log($(this).html());
				if($(this).attr('data') == 'deviceNum'){
					$(this).html(result[0].deviceNum);
				}
				if($(this).attr('data') == 'timestampStartStr'){
					$(this).html(result[0].timestampStartStr);
				}
				if($(this).attr('data') == 'timestampEndStr'){
					$(this).html(result[0].timestampEndStr);
				}
// 				if($(this).attr('data') == 'historyAppearTimes'){
// 					$(this).html(result[0].historyAppearTimes);
// 				}
				if($(this).attr('data') == 'remark'){
					$(this).html(result[0].remark);
				}
				if($(this).attr('data') == 'name'){
					var ciRsName = result[0].similarFaceObjMap[0].ciRsName;
					var detail_rs = '<a class="fname" href="#" onclick="showPopDetail(\'人员详情\','+result[0].similarFaceObjMap[0].ciRsId+')">'+ciRsName+'</a>';
					if(ciRsName != null){
						$(this).html(detail_rs);
					}
				}
				if($(this).attr('data') == 'person_id'){
					$(this).html(result[0].similarFaceObjMap[0].person_id);
				}
				if($(this).attr('data') == 'similarity'){
					$(this).html(result[0].similarFaceObjMap[0].similarity.toFixed(1)+"%");
				}
		    });
			$("#hanpenTimeStr").val(r.hanpenTimeStr);
            $("#cirsName").val(result[0].similarFaceObjMap[0].ciRsName);
            $('#deviceNum').val(r.deviceNum);
            $('#deviceName').val(r.deviceName);
            $('#deviceAddr').val(r.remark);
			if(r != null && typeof(r.x) != 'undefined' && r.x != null){
                $('#x').val(r.x);
			}
            if(r != null && typeof(r.y) != 'undefined' && r.y != null){
                $('#y').val(r.y);
            }
		}
	});
}

function selectBlack(obj,index){
	$('#tags2').html("");
	$('#tags').html("");
	$('#blacklist li').css("border","1px solid #cfcfcf");
	$(obj).css("border","1px solid #ff0000");
// 	console.log('r---'+r.similarFaceObjMap[index].picture_uri);
	var similarFaceObjMap = r.similarFaceObjMap[index];
	var tags = similarFaceObjMap.tag;
	var typeListStr = "";//黑名单人员类型字符串
	for(var i=0;i<tags.length;i++){
		var content = '<span onclick="showDetail('+similarFaceObjMap.ciRsId+','+tags[i][0]+',\''+tags[i][1]+'\')" class="manlx">'+tags[i][1]+'</span>';
		if(i >= 5){
			$('#tags2').append(content);
		}else{
			$('#tags').append(content);
		}

        if (tags[i][0] == "11") {// 吸毒信息 0201
            typeListStr = typeListStr + "吸毒、"
        } else if (tags[i][0] == "14") {// 刑释解教信息 0202
            typeListStr = typeListStr + "刑释解教、"
        } else if (tags[i][0] == "13") {// 矫正信息 0203
            typeListStr = typeListStr + "矫正、"
        } else if (tags[i][0] == "8") {// 精神障碍患者信息 0207
            typeListStr = typeListStr + "精神障碍患者、"
        } else if (tags[i][0] == "21") {// 艾滋病
            typeListStr = typeListStr + "艾滋病、"
        }
		if(i==tags.length-1){
            typeListStr = typeListStr + tags[i][1];
		}else{
            typeListStr = typeListStr + "、" + tags[i][1];
		}
		$('#typeListStr').val(typeListStr);
	}
	$("#blackImg").attr('src',similarFaceObjMap.picture_uri);
	$("[data]").each(function(index, element) {
		if($(this).attr('data') == 'name'){
			if(similarFaceObjMap.ciRsId != null){
				var ciRsName = similarFaceObjMap.ciRsName;
				var detail_rs = '<a class="fname" href="#" onclick="showPopDetail(\'人员详情\','+similarFaceObjMap.ciRsId+')">'+ciRsName+'</a>';
				if(ciRsName != null){
					$(this).html(detail_rs);
				}
			}
		}
		if($(this).attr('data') == 'person_id'){
			$(this).html(similarFaceObjMap.person_id);
		}
		if($(this).attr('data') == 'similarity'){
			$(this).html(similarFaceObjMap.similarity.toFixed(1)+"%");
		}
	});
    $("#cirsName").val(similarFaceObjMap.ciRsName);
}

function showDetail(ciRsId,id,name) {
	var url = "${SQ_ZZGRID_URL}/zzgl/map/data/residentionfo/typeDetail/"+ciRsId+".jhtml?id="+id+"&gridId="+${gridId};
	addPersonLi(ciRsId,name,id,url);
}

function showPopDetail(title,ciRsId) {
	var population_url =  window.parent.document.getElementById("POPULATION_URL").value;
	var url = population_url+"/cirs/viewResident.jhtml?menu=1&ciRsId="+ciRsId;
	window.parent.showMaxJqueryWindow(title,url,800,400);
}

function addPersonLi(rsId,liName,code,url) {
	var src = "";
	var height = 230;
	var width = 635;
	var title = liName;
	if (code == "1") {// 党员信息 0101
		url = "${POPULATION_URL}/party/viewBaseAndActivity.jhtml?ciRsId="+rsId;;
		width = 950;
		height = 400;
	} else if (code == "11") {// 吸毒信息 0201
		var ENABLE_GB = '${ENABLE_GB!}';
		
		if(ENABLE_GB && ENABLE_GB == '1'){
			url = '${SQ_ZZGRID_URL}/zzgl/crowd/drug/toDetail.jhtml?ciRsId='+rsId;
			width = 720;
			height = 400;
		} else {
			height = 335;
		}
		
		src = url;
	} else if (code == "16") {// 台胞信息
		src = url;
		height = 167;
	} else if (code == "14") {// 刑释解教信息 0202
		var ENABLE_GB = '${ENABLE_GB!}';
		
		if(ENABLE_GB && ENABLE_GB == '1'){
			url = '${SQ_ZZGRID_URL}/zzgl/crowd/releasedRecord/detail_nanchang.jhtml?ciRsId='+rsId;
			width = 720;
			height = 400;
		} else {
			height = 319;
		}
	
		src = url;
	} else if (code == "13") {// 矫正信息 0203
		var ENABLE_GB = '<#if ENABLE_GB??>${ENABLE_GB!''}</#if>'
		if(ENABLE_GB != null && ENABLE_GB != '' && ENABLE_GB == '1'){
			url = "${SQ_ZZGRID_URL}/zzgl/crowd/correctional/detail.jhtml?standard=standard&ismap=2&ciRsId=" + rsId;
			width = 620;
			height = 400;
		}else{
			src = url;
			height = 227;
		}
	} else if (code == "12") {// 邪教信息 0204
		src = url;
		height = 210;
	} else if (code == "10") {// 上访信息 0205
		src = url;
		height = 164;
		width = 450;
	} else if (code == "9") {// 危险品从业信息 0206
		src = url;
		height = 172;
		width = 550;
	} else if (code == "8") {// 精神障碍患者信息 0207
		var ENABLE_GB = '${ENABLE_GB!}';
		if(ENABLE_GB && ENABLE_GB == '1'){
			url = '${SQ_ZZGRID_URL}/zzgl/crowd/mentalIllnessRecord/toDetail.jhtml?standard=standard&ciRsId='+rsId;
			width = 750;
			height = 400;
		} else {
			height = 240;
		}
		src = url;
	} else if (code == "7") {// 残障信息 0301
		src = url;
		height = 417;
	} else if (code == "6") {// 低保信息 0302
		src = url;
		height = 202;
	} else if (code == "0303") {
		src = url;
		height = 250;
		width = 635;
	}else if (code == "3") {// 居家养老信息 0303
		src = url;
		height = 137;
		width = 450;
	} else if (code == "0304") {// 志愿者信息 0304
		src = "";
	}  else if (code == "15") {// 志愿者信息 0304
		src = url;
		height = 337;
	}else if (code == "2") {// 退休信息 0401
		src = url;
		height = 170;
	} else if (code == "5") {// 失业信息 0402
		src = url;
		height = 202;
	} else if (code == "4") {// 服兵役信息 1001
		src = url;
		height = 162;
		width = 500;
	} else if (liName == "走访记录") {// 走访记录
		src = "${SQ_ZZGRID_URL}/gis.shtml?method=getPersonnelVisitsRecord&id=" + rsId + "&codes=" + code;
		height = 250;
	} else if (code == "18") {
		url = "${SQ_ZZGRID_URL}/zzgl/crowd/petitioner/detail.jhtml?ismap=2&miId=" + $("#miId").val();
		width = 900;
		height = 370;
	} else if (code == "21") {
		url = "${SQ_ZZGRID_URL}/zzgl/crowd/aids/detail.jhtml?ciRsId=" + rsId;
		width = 660;
		height = 400;
	} else if (code == "19") { //重点青少年
		var ENABLE_GB = '<#if ENABLE_GB??>${ENABLE_GB!''}</#if>'
		if(ENABLE_GB != null && ENABLE_GB != '' && ENABLE_GB == '1'){
			url = "${SQ_ZZGRID_URL}/zzgl/crowd/youth/detail.jhtml?ismap=2&standard=standard&id=" + rsId;
			width = 720;
			height = 400;
		}
	}

	// 隐藏div
	$("#person_div div").each(function(){
		$(this).css("display", "none");
	});
	
		window.parent.showMaxJqueryWindow(title,url,width,height);
}

//事件派遣
function eventDispatch(){
	var cirsName = $("#cirsName").val();//人员姓名
	var deviceName = $('#deviceName').val();
    var gridName = $('#gridName').val();
    var gridId = $('#gridId').val();
	var typeListStr = $('#typeListStr').val();
	//事件标题
    var eventName = typeListStr+cirsName+"出现在"+gridName+""+deviceName;
	//事发地址
    var occurred= $('#deviceAddr').val();//摄像头地址
	//事件类型
	var type = "事件类型";
    var happenDateStr = $("#hanpenTimeStr").val();
	//事件描述
    var content = typeListStr+ cirsName+ "出现在"+gridName+""+deviceName+"请及时做好预警、防范";
	var x = $('#x').val();
    if( typeof(x) == 'undefined' || x == null){
        x = '';
    }
    var y = $('#y').val();
    if( typeof(y) == 'undefined' || y == null){
        y = '';
    }
	var mapt = $('#mapt').val();
    if( typeof(mapt) == 'undefined' || mapt == null){
        mapt = '';
    }
    var event = {
        "eventName" : eventName,
        "happenTimeStr" : happenDateStr,
        "occurred" : occurred,
        "content" : content,
        "gridId" : gridId,
        "gridName" : gridName,
        "type" : '0221',
        "typesForList" : '0221',
        "status" : '',
        "callBack" : 'parent.closeMaxJqueryWindow',
        "eventReportRecordInfo":{
            'bizId' : ''
        },
		"resMarker" : {
            "x" : x,
            "y" : y,
            "mapType" : mapt
		},
		"isReport" : "false",
		"attachment" : [ {
				'fileName' : '抓拍图像.jpg',
				'filePath' : $("#photoImg").attr("src"),
				'eventSeq' : '1'
			}, {
				'fileName' : '黑名单图像.jpg',
				'filePath' : $("#blackImg").attr("src"),
				'eventSeq' : '1'
		} ]
    };

    var event = JSON.stringify(event);
    var url = '${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByJson.jhtml?eventJson='+encodeURIComponent(event);
    window.parent.showMaxJqueryWindow("事件派遣",url,800, 390,'no');
}

//应急预案
function emergencyPlan(){
	var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofpoplocal/showEmergencyWay.jhtml?catalogId=40000012&subClassPcode=B177";
    window.parent.showMaxJqueryWindow("应急预案",url,null,null);
}

//查看短视频
function showPointVideo(){
    var deviceNums = $("#deviceNum").val();
    var time = $("#hanpenTimeStr").val();//当前时间戳.
    var data = {
        deviceNum : deviceNums,
        interval : 5,
        time : time
    }

    var url = "${rc.getContextPath()}/zhsq/alarm/videoSurveillanceController/getPointVideoUrl.jhtml?deviceNums="+deviceNums;
    $.ajax({
        type : "POST",
        url : url,
        dataType:"json",
        async : true,
        data : data,
        success : function(data){
            if(data.rtspUrl != null && data.rtspUrl != ''){
                showGlobalEyeInfo(data.rtspUrl);
            }else{
                $.messager.alert("提示", '获取不到播放地址!',"error");
            }
        },
        error : function(){
            $.messager.alert("提示","请求错误！","error");
        }
    });
}
</script>
</html>