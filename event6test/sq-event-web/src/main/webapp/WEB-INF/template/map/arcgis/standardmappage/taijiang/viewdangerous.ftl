<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<#include "/component/commonFiles-1.1.ftl" />
    <style type="text/css">
        .w500{width:500px;}
    </style>
</head>

<body>
      <div id="content-d" class="MC_con content light" title="查看整改记录">
	       	<div class="NorForm">
	         <table width="97%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="LeftTd">
                        <label class="LabName">
                            <span>场所名称：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${record.placeName!}</div>
                    </td>
                    <td>
                        <label class="LabName">
                            <span>楼宇名称：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue">${record.buildingName!}</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>整改期限：</span></label><div class="Check_Radio FontDarkBlue"><#if record.checkTime??>${record.checkTime?string("yyyy-MM-dd")}</#if></div></td>
                    <td><label class="LabName" style="width:100px;"><span>整改通知书编号：</span></label><div class="Check_Radio FontDarkBlue">${record.safeCheckCode!}</div></td>
                  </tr>
                  <tr>
                    <td colspan="2"><label class="LabName" style="width:100px;"><span>老弱病残孕情况：</span></label><div class="Check_Radio FontDarkBlue">${old!}</div></td>
                  </tr>
                  <tr>
                    <td colspan="2" class="LeftTd">
                        <label class="LabName">
                            <span>消防设施情况：</span>
                        </label>
                        <div class="Check_Radio FontDarkBlue w500">
                            ${record.fireDevice!}
                        </div>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>隐患描述：</span></label><div class="Check_Radio FontDarkBlue w500">${record.checkDetail!}</div></td>
                  </tr>
                  <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>整改措施：</span></label><div class="Check_Radio FontDarkBlue w500">${record.checkMethod!}</div></td>
                  </tr>
                  <tr>
                    <td colspan="2" class="LeftTd"><label class="LabName"><span>整改结果：</span></label><div class="Check_Radio FontDarkBlue w500">${record.checkResult!}</div></td>
                  </tr>
                </table>
	        </div>
	  </div>
      <script>
          (function($){
              $("#content-d").mCustomScrollbar({theme:"minimal-dark"});
          })(jQuery);
      </script>
</body>
</html>