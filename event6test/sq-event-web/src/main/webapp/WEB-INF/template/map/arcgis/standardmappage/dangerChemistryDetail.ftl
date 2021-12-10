<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>危险化学品企业-概要信息</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body>
	<div class="NorMapOpenDiv" style="width:100%;height:100%;">	
		<div class="con LyWatch" style="height:100%;overflow:auto;">
        	<ul>
                <li>
                	<p class="FontDarkBlue" style="font-size:14px;width:100%;word-break: break-all;"><b id="companyName">&nbsp;</b></p>
                    <p>地址：<span id="address" class="FontDarkBlue"></span></p>
                    <p>所属网格：<span class="FontDarkBlue">${gridNames!''}&nbsp;</span></p>
                    <p>负责人：<span id="personName" class="FontDarkBlue"></span></p>
                    <p>联系电话：<span class="FontDarkBlue">${dangerChemistryManager.personTel!}&nbsp;</span></p>
                </li>
            </ul>
            <div class="clear"></div>
        </div>
	</div>
	<script type="text/javascript">
		var companyName = "${dangerChemistryManager.companyName!''}";
		var address = "${dangerChemistryManager.address!''}";
		var personName = "${dangerChemistryManager.personName!''}"
  
		if(!companyName) {
			companyName = "&nbsp;";
		} else if(companyName.length>12){
			companyName = companyName.substring(0,12)+"...";
		}
		
		if(!address) {
			address = "&nbsp;";
		} else if(address.length>40){
			address = address.substring(0,40)+"...";
		}
		
		if(!personName) {
			personName = "&nbsp;";
		} else if(personName.length>10){
			personName = personName.substring(0,10)+"...";
		}
		
		$("#companyName").html(companyName);
		$("#address").html(address);
		$("#personName").html(personName);
		
	</script>
</body>
</html>