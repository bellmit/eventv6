<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>企业-概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
<#include "/component/ImageView.ftl" />
<style type="text/css">
	.NorMapOpenDiv ul li{
		padding-right:3px;
		/*width:120px; 限制宽度导致折行 @YangCQ */
	}
	.LyWatch ul li{
		margin:5px 5px 5px 10px;
		vertical-align:top;
	}
	.LyWatch span{width:60%;}
</style>

</head>
<body>
		<div style="width:700px;height:160px">
        	<ul>
                <li class="pic">
                    <a href="#">
                        <div id = "imgDiv" style="margin-left: 5px;margin-top: 5px" >
						<#if bo.photo??>
                            <img src="${RESOURSE_SERVER_PATH}<#if bo.photo??>${bo.photo}</#if>" border=0 width="120px;" height="145px;" />
						<#else>
							<img id="peopleImg"  src="${uiDomain!''}/images/map/gisv0/map_config/unselected/situation/untitled.png" width="120px;" height="145px;"/>
						</#if>
                    </a>
                </li>
             </ul>
				<div style="margin-left: 20%;margin-top: -145px;">
					<p>姓名：<span class="FontDarkBlue"><#if bo.pName??>${bo.pName}</#if></span></p></br>
					<p>证件号码：<span class="FontDarkBlue"><#if bo.certNumber??>${bo.certNumber}</#if></span></p></br>
	                <p>联系方式：<span class="FontDarkBlue"><#if bo.phone??>${bo.phone}</#if></span></p></br>
	                <p>所在派出所：<span class="FontDarkBlue" style="width:80%;"><#if bo.pStationName??>${bo.pStationName}</#if></span></p>	</br>			
	                <p>所在警务区：<span class="FontDarkBlue" style="width:80%;"><#if bo.pDistrictName??>${bo.pDistrictName}</#if></span></p></br>			
	                <p style="width: 42%;">备注：<span class="FontDarkBlue" style="width:80%;"><#if bo.mark??>${bo.mark}</#if></span></p>				
				</div>
           <div class="clear"></div>
		</div>			
	   
</body>
<script type="text/javascript">
    (function ($) {
        $(window).load(function () {
            var options = {
                axis: "yx",
                theme: "minimal-dark"
            };
            enableScrollBar('content-d', options);
        });
    })(jQuery);


    var fieldId  = "<#if record.cbiId??>${record.cbiId?c}</#if>";
    var paths = "<#if record.photoPath??>${RESOURSE_SERVER_PATH!''}${record.photoPath!''}</#if>";

    function ffcs_viewImg(fieldId){
        var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?pmId="+ fieldId;
        var name = "图片查看";
        openPostWindow(url, paths, name);
    }


    function openPostWindow(url, data, name){
        var tempForm = document.createElement("form");
        tempForm.id="tempForm1";
        tempForm.method="post";
        tempForm.action=url;
        tempForm.target=name;
        var hideInput = document.createElement("input");
        hideInput.type="hidden";
        hideInput.name= "paths";
        hideInput.value= data;
        tempForm.appendChild(hideInput);
        document.body.appendChild(tempForm);
        tempForm.submit(function(){
            openWindow(name);
        });
        //tempForm.fireEvent("onsubmit");
        //tempForm.submit();
        document.body.removeChild(tempForm);
    }

 /*    function openWindow(name){
        window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
    } */
</script>
</html>
