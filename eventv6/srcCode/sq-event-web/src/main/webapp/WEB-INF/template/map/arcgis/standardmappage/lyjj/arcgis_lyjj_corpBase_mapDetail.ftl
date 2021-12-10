<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>企业-概要信息</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		width:120px;
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
	}
</style>

</head>
<body>
	<div class="NorMapOpenDiv">
		<div class="con LyWatch" style="height:300px;overflow:hide;">
        	<ul>
        		<li style="width:400px"><span class="FontDarkBlue" style="font-size:14px;font-weight:bold">
        			<a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showCorpDetail('${record.corpName!''}','${record.corpId?c}')">
        				<#if record.corpName??>${record.corpName}</#if>
        			</a>
        		</span></li>
				<li style="width:400px;" title="<#if record.regAddress??>${record.regAddress}</#if>">注册地址：<span class="FontDarkBlue">
							<#if record.regAddress??>
								<#if (record.regAddress)?length lt 26 >
				            		${record.regAddress}
				            	<#else>
				            		${record.regAddress[0..25]}..
				            	</#if>
				            <#else>
				            	暂无
							</#if>
							</span></li>
				<li style="width:400px;">工商登记号：<span class="FontDarkBlue">
							<#if record.regNum??>
								${record.regNum}
							<#else>
				            	暂无
				            </#if>
							</span></li>
				<li style="width:400px;">企业类型：<span class="FontDarkBlue">
                    		<#if record.corpType??>
					         <#if record.corpType=='01'>企业法人</#if>
				             <#if record.corpType=='02'>机关法人</#if>
				             <#if record.corpType=='03'>事业单位法人</#if>
				             <#if record.corpType=='04'>社会团体法人</#if>
				             <#if record.corpType=='05'>民办非企业法人</#if>	 
				             <#if record.corpType=='06'>基金会法人</#if>	 
				             <#if record.corpType=='07'>其它</#if>
				           <#else>
				            	暂无	 	 
				           </#if>
						   </span></li>
				<li style="width:400px;">联系人：<span class="FontDarkBlue">
							<#if record.linkMan??>
								${record.linkMan}
							<#else>
				            	暂无
							</#if>
							</span></li>
				<li style="width:400px;">联系电话：<span class="FontDarkBlue">
							<#if record.linkManTel??>
								${record.linkManTel}
							<#else>
				            	暂无
				            </#if>
							</span></li>
			</ul>
           <div class="clear"></div>
        </div>
	</div>			
</body>
<script>
function showCorpDetail(title,corpId) {
	var sq_lyjj_url = window.parent.document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/corpBase/view.jhtml?corpId="+corpId+"&fromMap=1";
	window.parent.showMaxJqueryWindow(title,url,980,420);
}
</script>
</html>
