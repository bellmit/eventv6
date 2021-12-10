<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表</title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
.orange{ color:#ff8019;}
.clearfloat{ clear:both;}
.facemain{ margin-bottom:10px;}
.face-img{ border:1px solid #d1d2d4; padding:2px; float:left; width:220px;}
.face-con{ margin-left:240px; margin-right:15px;}
.face-con h3{ background:#f2f2f2; font-size:14px; padding:8px 8px;}
.face-con .face-ltxt{ width:100px; display:inline-block; text-align:right;}
.face-con p{ padding:5px 0;}
</style>
</head>
<body>

<div class="MC_con content light" id="content-d" style="width:100%;">

<#list blackList as l>
<div class="facemain">
  <div class="face-img">
    <img src="${l.faceRecognition_image_url!''}" width="219" height="219" />
    <p>抓拍图像</p>
  </div>
  <div class="face-con">
    <h3>报警信息</h3>
    <p><label class="face-ltxt">姓名：</label>${l.name}</p>
    <p><label class="face-ltxt">性别：</label>${l.sex}</p>
    <p><label class="face-ltxt">证件类型：</label>${l.card_type}</p>
    <p><label class="face-ltxt">证件号码：</label>${l.card_number}</p>
    <p><label class="face-ltxt">匹对时间：</label>${l.check_time}</p>
    <p><label class="face-ltxt">对应抓拍记录的摄像头id：</label>${l.camera_id}</p>
    <p><label class="face-ltxt">相似度：</label><span class="orange"><b>${l.similarity}</b></span></p>
  </div>
  <div class="clearfloat"></div>
</div>
</#list>


</div>
</body>
<script type="text/javascript">
$(function() {
	$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
});

function getData(){
	
}
</script>
</html>