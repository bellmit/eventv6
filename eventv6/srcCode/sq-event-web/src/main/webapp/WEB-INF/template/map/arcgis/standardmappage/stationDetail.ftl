<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" > 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>安检点位检查</title>

<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jquery-easyui-1.4/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/jquery-easyui-1.4/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/ui/css/easyuiExtend.css"  />
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/zzgl_core.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/ui/css/normal.css"  />
<link rel="stylesheet" href="${rc.getContextPath()}/ui/css/jquery.mCustomScrollbar.css">
<script src="${rc.getContextPath()}/ui/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/ui/js/function.js"></script>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/lhgdialog/picList.css?_=1"/>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/uploadifyNew/uploadify.css"/>
<script type="text/javascript" src="${rc.getContextPath()}/uploadifyNew/jquery.uploadify.min.js?_=2"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/jqueryeasyui-last/datagrid-detailview.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/js/global.js?_=8"></script>
<#include "/component/ComboBox.ftl">
<body>
<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zzgl/grid/safetyStation/save.jhtml" method="POST">
  <input type="hidden" id="catalogNames"/>
  <div id="content-x" class="MC_con content light" style="overflow-x:hidden; position:relative;">
  <div style="margin-top:10px;" class="NorForm NorForm2" >
	<table width="100%" border="0" style="word-break: break-word;" cellspacing="0" cellpadding="0" class="border-t">
		 <tr>
		 	 <td class="LeftTd" style="width:50%;"><label class="LabName"><span>点位名称：</span></label>
		 		  <div class="Check_Radio FontDarkBlue" style="width:55%;" ><label id="stationName"></label></div>						  	                      
			 </td>	
			 
			 <td class="LeftTd" ><label class="LabName"><span>点位类型：</span></label>
				 <div class="Check_Radio FontDarkBlue"  style="width:55%;"  ><label id="catalogs"></label></div>	    
			 </td>
		  </tr>

		  <tr>
			 <td class="LeftTd" <#if standard??>colspan="1"<#else>colspan="2"</#if> ><label class="LabName"><span>点位地址：</span></label>
		 		 <div class="Check_Radio FontDarkBlue" style="width:60%" ><label id="address"></label></div>
                                 <span id="_bz" style="float:left;display:block;"><a href="javascript:void(0)" onclick="mapLabel()"><img src="${rc.getContextPath()}/theme/standardfordetail/images/local.png" />已标注</span>
			 </td>	
			<#if standard??>
				<#if standard=='mapLabelHide'>
					<td class="LeftTd" >
					<span style="float:right; margin-right:150px;">
					 	<img src="${rc.getContextPath()}/theme/standardfordetail/images/local.png" />已标注
					 </span>
					 </td>
				<#else>
					<td class="LeftTd" ><label class="LabName"><span>所属企业：</span></label>
						<div class="Check_Radio FontDarkBlue"  style="width:55%;"  ><label><#if corBaseInfo.corName??><a style="cursor:pointer;color:red;text-decoration:underline;" onclick="showCorBaseInfo(${corBaseInfo.cbiId});">${corBaseInfo.corName}</a></#if></label></div>	    
					</td>
				</#if>
			</#if>
		 </tr>
		 <tr>
		 	 <td class="LeftTd" colspan="2" ><label class="LabName"><span>产品：</span></label>
		 	 	 <div class="Check_Radio FontDarkBlue" style="width:80%" ><label id="product"></label></div>	
			 </td>	
		 
		 </tr>
		 <tr>
		 	 <td class="LeftTd"  ><label class="LabName"><span>责任人：</span></label>
		 		 <div class="Check_Radio FontDarkBlue"  style="width:55%;"  ><label id="responsible"></label></div>          
			 </td>	
			 
			 <td class="LeftTd" ><label class="LabName"><span>联系电话：</span></label>
		 		 <div class="Check_Radio FontDarkBlue"  style="width:55%;"  ><label id="tel"></label></div>
			 </td>

		 </tr>

		 <tr>							     
		     <td class="LeftTd" colspan="2" ><label class="LabName"><span>点位描述：</span></label>
		 		 <div class="Check_Radio FontDarkBlue" style="width:78%" ><label id="overview"></label></div>
			 </td>
		 </tr>
		 <tr>							     
		     <td class="LeftTd"  ><label class="LabName"><span>责任人照片：</span></label>
				<div id ="picList1" style="margin-top:25px;"></div>
			 </td>
			 <td class="LeftTd"  ><label class="LabName"><span>现场照片：</span></label>
			 	<div id ="picList2" style="margin-top:25px;"></div>
			 </td>
		 </tr>
	</table>
</div>
</div>
</form>
<#include "/component/map_labeling.ftl">
<#include "/component/maxJqueryEasyUIWin.ftl" />
<script type="text/javascript">
    var mapType,x,y;

	$("#content-x").mCustomScrollbar({theme:"minimal-dark"});
	   
	//放大预览图片
	function imgShow(url) {
		parent.imgShowDialog(url);
	 }
	   

<#if safetyStation??>
	loadData(${safetyStation});
</#if>
function loadData(safetyStation){
	$("#stationName").text(safetyStation.stationName);
	$("#address").text(safetyStation.address);
	$("#product").text(safetyStation.product);
	$("#responsible").text(safetyStation.responsible);
	$("#tel").text(safetyStation.tel);
	$("#overview").text(safetyStation.overview);
	//////// 图片
	if(safetyStation.respImg){
		$('#picList1').html(loadpic("table1",safetyStation.respImg));
	}
	if(safetyStation.stationImg){
		$('#picList2').html(loadpic("table2",safetyStation.stationImg));
	}
	////////类别
	AnoleApi.initTreeComboBox("catalogNames", null, "A001400001",null,eval('([' + safetyStation.catalogs + '])'), {
		RenderType : "01",
		OnRenderCompleted : function(api) {
			$("#catalogs").text(api.getDataName());
		}
	});
	//$("#catalogs").text(formatValues('CATALOG',safetyStation.catalogs));
	if(safetyStation.mapType2 && safetyStation.mapType2!=''){
           x = safetyStation.x2;
	   y = safetyStation.y2;
	   mapType = safetyStation.mapType2;
        }else{
           $('#_bz').css("display", "none");
        }
    
	<#if standard??>
	   $('#_bz').css("display", "none");
	</#if>
}

function loadpic(tableId,fileUrl){
	var filepath = '${RESOURSE_SERVER_PATH}'+fileUrl;
	var html="<div id=\'"+tableId+"\' border=\"0\" style=\"margin-left:50px;width:200px;\" >";
	html+="<input type=\"hidden\" id=\"respImg\"  name=\"respImg\" value=\""+fileUrl+"\" />";		        		
	html+="<div class=\"imgbox\"><div class=\"w_upload\">";
	html+="<span class=\"item_box\"><img style='margin-top:3px;cursor: pointer;' title=\"单击查看大图\" src=\""+filepath+"\" onclick=\"imgShow(\'"+filepath+"\')\"></span>";
	html+="</div>";	        		
	html+="</div>";	
	return html;
}

        function mapLabel(){
             var gridId = '${gridId?c}';
             var callBackUrl = '${SQ_ZZGRID_URL}/zzgl/important/toArcgisCrossDomain.jhtml';
             if(mapType && mapType!=""){
	        initMapMarkerInfoSelector(gridId, callBackUrl, x, y, mapType, 480, 380, false, "COR_BASE");
	     }
	}

	function mapMarkerSelectorCallback(mapt, x, y){
            closeMaxJqueryWindowForCross();
	}
	
	function showCorBaseInfo(id) {
		<#if corBaseInfo.corName??>
			var url = "/zzgl/grid/safetyStation/detailA.jhtml?corpId=" + id;
			parent.showDetailInfo(url, "${corBaseInfo.corName}", 610, 350);
		</#if>
	}
</script>
</body>
</html>