<!DOCTYPE html>
<html>
<head>
	<title>告警信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<link href="${uiDomain!''}/images/map/gisv0/special_config/css/smartCity_map_style.css" rel="stylesheet" type="text/css" />
	<style>
	.npAlarminfo{
		background:none;
	}
	
	</style>
</head>
<body>
<div class="npAlarminfo citybgbox" id="content-d"   style="overflow-x:hidden;overflow-y:auto;height:250px;"  >
<div   class="alarlist" style="border-top: 1px solid #81a1b9;">
      <div class="alarlistbox" >
            <#if (list?size>0) >
                      <#list list as obj >
	                     <div class="list-dynamic">
				          <div class="round6" style="background:<#if obj.alarmLevel=1 >#fd7f7f<#elseif obj.alarmLevel=2>#ffbd3e<#else>#56bdf5</#if>"></div>
				          <div class="data" style="background:<#if obj.alarmLevel=1 >#fd7f7f<#elseif obj.alarmLevel=2>#ffbd3e<#else>#56bdf5</#if>">
				            <p>${obj.alarmTypeName!}</p>
				            <div class="triangle-right" style="border-left:<#if obj.alarmLevel=1 >5px solid #fd7f7f<#elseif obj.alarmLevel=2>5px solid #ffbd3e<#else>5px solid #56bdf5</#if>"></div>
				          </div>
				          <p class="dynamic-item">
				            <a href="javaScript:void(0);" onclick="showUrl('${obj.alarmTypeName!}','<#if obj.alarmUrl??>${obj.alarmUrl!}<#else>''</#if>')">${obj.alarmContent!}</a>
				          </p>
				        </div><!--end .list-dynamic"-->
	               	  </#list>
	               	  <#else>
			</#if>
        
      </div>
    </div><!--end .alarlist"-->
</div>
<script type="text/javascript"> 
$(function(){
		//改变滚动条样式
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
		
});

function showUrl(alarmTypeName,url){
   if(url!=''){
		parent.layer.open({
		  closeBtn:'1',
	      type: 2,
	      title: [alarmTypeName, 'background-color: rgba(19,55,81,0); border-bottom : 0px solid rgba(19,55,81,0.8); color: #F8F8F8;font-size: 20px;'], 
	      shadeClose: true,
	      shade: 0,
	      shade: false,
	      maxmin: true, //开启最大化最小化按钮    
	      //skin: 'layerSkin',
	      area: ['1084px', '482px'],
	      content: url,
	      cancel : function(index, layero) {
	           
	      }
	    });
   }
}
</script>
</body>

</html>
