
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style>
body{ font-size:12px;}
.poorbox{ width:360px;}
ul{ list-style:none; padding:0; margin:0;}
.poorbox ul li{ height:45px; width:92px; float:left; margin:10px; color:#fff; font-size:12px; padding:8px 0 0px 67px; line-height:1.4}
.poorbox ul .p-ys1{ background:#3dc9a0 url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon1.jpg) no-repeat;}
.poorbox ul .p-ys2{ background:#6da9f4 url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon2.jpg) no-repeat;}
.poorbox ul .p-ys3{ background:#ff6262 url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon3.jpg) no-repeat;}
.poorbox ul .p-ys4{ background:#fda627 url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon4.jpg) no-repeat;}
.poorbox ul .p-ys5{ background:#8bc34a url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon5.jpg) no-repeat;}
.poorbox ul .p-ys6{ background:#677e88 url(${uiDomain!''}/images/map/gisv0/special_config/images/p_icon6.jpg) no-repeat;}
.p-sz{ font-size:18px;}
.poor-bs{ width:260px;}
.poor-bs ul li{ float:left; width:130px; padding:10px 0;}
.poor-bs ul li img{ float:left; margin-top:-4px; margin-right:5px;}
</style>
</head>

<body>
<div class="poorbox">
  <#if statMap??>
  <ul>
    <li class="p-ys1">总人口数<br /><span class="p-sz"><#if statMap.zrk??>${statMap.zrk?string(',###')}<#else>0</#if></span></li>
    <li class="p-ys2">总户数<br /><span class="p-sz"><#if statMap.TOTAL_FAMILY_NUMBER??>${statMap.TOTAL_FAMILY_NUMBER?string(',###')}<#else>0</#if></span></li>
    <li class="p-ys3">贫困户<br /><span class="p-sz"><#if statMap.POOR_NUMBER??>${statMap.POOR_NUMBER?string(',###')}<#else>0</#if></span></li>
    <li class="p-ys4">低保贫困户<br /><span class="p-sz"><#if statMap.LOW_NUMBER??>${statMap.LOW_NUMBER?string(',###')}<#else>0</#if></span></li>
    <li class="p-ys5">政策保障户<br /><span class="p-sz"><#if statMap.POLICY_NUMBER??>${statMap.POLICY_NUMBER?string(',###')}<#else>0</#if></span></li>
    <li class="p-ys6">非贫困户<br /><span class="p-sz"><#if statMap.NORMAL_NUMBER??>${statMap.NORMAL_NUMBER?string(',###')}<#else>0</#if></span></li>
  </ul>
  <#else>
  <ul>
    <li class="p-ys1">总人口数<br /><span class="p-sz">0</span></li>
    <li class="p-ys2">总户数<br /><span class="p-sz">0</span></li>
    <li class="p-ys3">贫困户<br /><span class="p-sz">0</span></li>
    <li class="p-ys4">低保贫困户<br /><span class="p-sz">0</span></li>
    <li class="p-ys5">政策保障户<br /><span class="p-sz">0</span></li>
    <li class="p-ys6">非贫困户<br /><span class="p-sz">0</span></li>
  </ul>
  </#if>
</div>
<div style="clear:both"></div>
</body>
</html>
