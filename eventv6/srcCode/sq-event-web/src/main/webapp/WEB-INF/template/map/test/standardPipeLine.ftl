<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网格员列表</title>

<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>

<script src="${rc.getContextPath()}/js/jquery.blockUI.js" type="text/javascript"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="border:none">
	<input type="hidden" id="gridId" value="${gridId?c}" />
	<input type="hidden" id="elementsCollectionStr" value="<#if elementsCollectionStr??>${elementsCollectionStr}</#if>" />
	<input type="hidden" id="pageSize" value="<#if pageSize??>${pageSize?c}<#else>20</#if>" />
    <div class="" style="display:block;">
        <div class="ListShow content" style="" id="content">
            <div class="liebiao">
            <ul id="content-md2" class="type content light">
                <li>
					<p style="font-size: 20px">选择管道类型：</p>
				</li>

				<li onclick="showPipeLine('中国电信','#FD6525')" value="中国电信">中国电信</li>
				<li onclick="showPipeLine('电力','#2CBBFF')" value="电力">电力</li>
				<li onclick="showPipeLine('给水','#FF752A')" value="给水">给水</li>
				<li onclick="showPipeLine('路灯','#DB10FF')" value="路灯">路灯</li>
				<li onclick="showPipeLine('污水','#FF3750')" value="污水">污水</li>
				<li onclick="showPipeLine('雨水','#1FFFA9')" value="雨水">雨水</li>
				<li onclick="showPipeLine('燃气','#3B31FA')" value="燃气">燃气</li>
				<div class="clear"></div>
            </ul>
			</div>
		</div>
        <div class="NorPage">
        	<#include "/map/arcgis/standardmappage/common/standard_page.ftl" />
        </div>
    </div>		
<script type="text/javascript">
/*默认选中网格员*/
	(function($){
        var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
        $("#content").height(winHeight-30);
        showPipeLine();
	})(jQuery);

	
	//--网格职务选择
	function showPipeLine(bizType, linColor){
		parent.showPipeLines(bizType, linColor);
	}

</script>
</body>
</html>