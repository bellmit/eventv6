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
	<div id="content-d" class="MC_con content light UnitInfo NorMapOpenDiv">
		<div class="con LyWatch" style="">
        	<ul>
                <li class="pic">
                    <a href="#">
                        <div id = "imgDiv" onclick='ffcs_viewImg("<#if record.cbiId??>${record.cbiId?c}</#if>")'>
						<#if record.photoPath??>
                            <img src="${RESOURSE_SERVER_PATH}<#if record.photoPath??>${record.photoPath}</#if>" border=0 width="120px;" height="145px;" />
						</#if>
                            <div>
                    </a>
                </li>
            <li style="width:250px">
				<#if record.corName??>

	        			<p class="FontDarkBlue" style="font-size:14px;font-weight:bold;cursor:default;width:100%;" title="${record.corName}">
		        				<#if (record.corName)?length lt 21 >
		                    		${record.corName}
		                    	<#else>
		                    		${record.corName[0..20]}
		                    	</#if>
		                    	
		                    	<#if record.corType??>
		                			<#if record.corType=='09'>&nbsp;<img src="${uiDomain!''}/images/map/gisv0/special_config/images/involvedTaiWan.png"></#if>
		                		</#if>
	        			</p>
				</#if>
				<#if record.corAddr??>
						<p title="${record.corAddr}">
							<#if (record.corAddr)?length lt 25 >
	                    		${record.corAddr}
	                    	<#else>
	                    		${record.corAddr[0..24]}
	                    	</#if>
						</p>
				</#if>
				
				<p>企业类型：<span class="FontDarkBlue">
							<#if corTypeDC??>
								<#list corTypeDC as l>
								     <#if record.corType??>
										<#if (l.COLUMN_VALUE==record.corType)>${l.COLUMN_VALUE_REMARK}</#if>
								    </#if>
								</#list>
							 </#if>
						   </span></p>
				<p>法人代表：<span class="FontDarkBlue"><#if record.representativeName??>${record.representativeName}</#if></span></p>
                <p>联系电话：<span class="FontDarkBlue"><#if record.telephone??>${record.telephone}</#if></span></p>
                <p>行业分类：
               		     <span class="FontDarkBlue">		
               				<#if categoryDC??>
								<#list categoryDC as l>
									<#if record.category??>
										<#if (l.COLUMN_VALUE==record.category)>${l.COLUMN_VALUE_REMARK}</#if>
									</#if>
								</#list>
							 </#if>
						</span></p>
                <p>经营范围：<span class="FontDarkBlue" style="width:80%;"><#if record.busScope??>${record.busScope}</#if></span></p>				</li>
			</ul>
        	
        	
           <div class="clear"></div>
        </div>
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
        var url = "${rc.getContextPath()}/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId;
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

    function openWindow(name){
        window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
    }
</script>
</html>
