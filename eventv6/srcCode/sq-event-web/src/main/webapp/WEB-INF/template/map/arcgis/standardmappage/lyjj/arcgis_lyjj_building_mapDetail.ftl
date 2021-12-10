<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>楼宇-详情</title>

<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body>
	<div class="con LyWatch" style="height:300px;overflow:hide;">
        	<ul>
            	<li class="pic">
            		<#if record.defaultPicPath??>
	            		<img src="${RESOURSE_SERVER_PATH}${record.defaultPicPath}" border=0 width="120px;" height="145px;" />
            		</#if>
            	</li>
                <li style="width:190px;">
                	<p title="${record.buildingName}"><b><a class="FontDarkBlue"  style="font-size:14px;" href="javascript:void(0);" onclick="showBuildDetail('${record.buildingName!''}','${record.buildingId?c}')">
                    	<#if (record.buildingName)?length lt 11 >
                    		${record.buildingName}
                    	<#else>
                    		${record.buildingName[0..10]}..
                    	</#if>
                	</a></b></p>
                    <p title="<#if record.buildingAddr?exists>${record.buildingAddr}</#if>">
                    <span>
                    <#if record.buildingAddr??>
                    	<#if (record.buildingAddr)?length lt 26 >
                    		${record.buildingAddr}
                    	<#else>
                    		${record.buildingAddr[0..25]}..
                    	</#if>
                    <#else>
                    	暂无
                    </#if>
                    </span></p>
                    <p>楼层总数：<span class="FontDarkBlue">${record.buildingExp.floorTotalNum!'未知'} </span>层</p>
                    <p>建筑年份：<span class="FontDarkBlue">${record.buildingExp.buildingYear!'未知'} </span>年</p>
                    <p>楼宇状态：<span class="FontDarkBlue"><#if record.buildingType??><#if record.buildingType=='1'>商务<#elseif record.buildingType=='2'>商住</#if><#else>未知</#if></span></p>
                </li>
            </ul>
            <div class="clear"></div>
            <div style="padding:0 10px 10px 10px; line-height:24px;">
				<p>管理单位：
                    <#if gridNames??>
                    	<span class="FontDarkBlue" title="${gridNames}">
                    	<#if gridNames?length lt 31 >
                    		${gridNames}
                    	<#else>
                    		${gridNames[0..30]}..
                    	</#if>
                    	</span>
                    <#else>
                    	暂无
                    </#if>
                    </p>
                <p>物业电话：<span class="FontDarkBlue">
                				${record.buildingExp.manageTel!'未知'}<br/>
								</span>
								</p>
            </div>
        </div>
	
	
</body>
<script type="text/javascript">
function showBuildDetail(title,buildingId) {
	var sq_lyjj_url = window.parent.document.getElementById("SQ_LYJJ_URL").value;
	var url = sq_lyjj_url+"/be/building/view.jhtml?buildingId="+buildingId+"&fromMap=1";
	window.parent.showMaxJqueryWindow(title,url,980,420);	
}
</script>
</html>