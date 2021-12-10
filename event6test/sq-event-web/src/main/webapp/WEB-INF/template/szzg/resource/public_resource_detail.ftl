<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>资源点</title>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
		vertical-align:top;
	}
	.LyWatch span{width:60%;}
	
	.gis_a{color: #cce6ff;vertical-align: -1px;width:190px;font-size: 14px;line-height: 35px;}
</style>

</head>
<body>
	<div id="content-d" class="MC_con content light NorMapOpenDiv" <#if isGis && isGis == 'Y'>style="margin-top: 25px;"</#if>>
		<div class="con LyWatch" style="">
        	<ul>
                <li style="width:120px;height:160px;">
                    <a href="javascript:void(0);">
                        <div>
						<#if res.PHOTO_PATH??>
                            <img src="${img}/${res.PHOTO_PATH!}" border=0 width="120px" height="160px" onclick="openWindow()"/>
						<#else>
                            <img src="${uiDomain!''}/images/map/gisv0/special_config/images/smartCity/notbuilding.gif" border=0 width="120px" height="160px" />
						</#if>
                        </div> 
                    </a>
                </li>
            <#if isGis && isGis == 'Y'> 
	            <li style="width:250px">
					<p style="font-size: 16px;color: #0a99ed;display: inline-block;font-weight: normal;">${res.RES_NAME!}</p>
					<p><span class="gis_a">所属网格</span> <span style="display: inline-block;color: #fff;vertical-align: top;font-size:14px;line-height: 35px;">${res.GRID_NAME!} </span></p>
					<p><span class="gis_a">地理位置</span> <span class="gis_a">${res.ADDR_!} </span></p>
					<p><span class="gis_a">备&nbsp;&nbsp;注</span> <span class="gis_a">${res.REMARK!}</span></p>			
				</li>
			<#else>
				<li style="width:250px">
					<p>${res.RES_NAME!}</p>
					<p title="${res.GRID_NAME!}">所属网格：<span class="FontDarkBlue">${res.GRID_NAME!} </span></p>
					<p title="${res.ADDR_!}">地理位置：<span class="FontDarkBlue">${res.ADDR_!} </span></p>
					<p title="${res.REMARK!}">备注：<span class="FontDarkBlue">${res.REMARK!}</span></p>			
				</li>
			</#if>
        	</ul>
        	
           <div class="clear"></div>
        </div>
	</div>			
	   
</body>
<script type="text/javascript">
    function openWindow(){
        window.open('${img}${res.PHOTO_PATH!}','图片查看','height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
    }
</script>
</html>
