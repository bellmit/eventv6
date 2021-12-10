<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>企业-概要信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<script type="text/javascript" src="${SQ_ZZGRID_URL}/base/getNewDictionaryListByConfig.jhtml?var=newDictionaryData&bid=corEnv"></script>
<script type="text/javascript" src="${SQ_ZZGRID_URL}/js/global.js"></script>
    <script type="text/javascript" src="${SQ_ZZGRID_URL}/component/comboselector/clientJs.jhtml?version=v2"></script>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
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
<div style="display:none">
    <input type="text" id="a1" class="comboselector w160" style="height:28px;" data-options="dType:'resident', editable:false,<#if record.corRsId??>value:${record.corRsId!''},</#if> afterSelect:combo_afterSelectCor"  />
    <input type="text" id="a2" class="comboselector w160" style="height:28px;"  data-options="dType:'resident', editable:false,<#if record.unitRsId??>value:${record.unitRsId!''},</#if> afterSelect:combo_afterSelectUnit"  />
    </div>
	<div class="NorMapOpenDiv" fit="true">
		<div class="con LyWatch" style="height:209px;overflow:auto;">
        	<ul>
				<#if record.corName??>
	        		<li style="width:360px">
	        			<p class="FontDarkBlue" style="font-size:14px;font-weight:bold;cursor:default;width:100%;" title="${record.corName}">
		        				<#if (record.corName)?length lt 21 >
		                    		${record.corName}
		                    	<#else>
		                    		${record.corName[0..20]}
		                    	</#if>
	        			</p>
	        		</li>
				</#if>
				<#if record.corAddr??>
					<li style="width:360px;cursor:default;" title="${record.corAddr}">
						<p>
							<#if (record.corAddr)?length lt 25 >
	                    		${record.corAddr}
	                    	<#else>
	                    		${record.corAddr[0..24]}
	                    	</#if>
						</p>
					</li>
				</#if>
				
				<li style="width:150px;">行业分类：<span class="FontDarkBlue" stringMap="category">${ record.category!''}</span></li>
				<li style="width:150px;">企业法人：<span class="FontDarkBlue" id="corRsIdName"></span></li>
				<li style="width:330px;">单位负责人：<span class="FontDarkBlue" id=unitRsIdName></span><span class="FontDarkBlue" id="unitResidentMobile"></span></li>
			</ul>
        	
        	
           <div class="clear"></div>
        </div>
	</div>			
	   
<script type="text/javascript">

function combo_afterSelectCor(data, target){
	if(data){
		$("#corRsIdName").html(data.I_NAME);
		$("#corAddr").val(data.residenceAddr);
		$("#corRsId").val(data.ciRsId);
		$("#corIdentityCard").html(data.identityCard);
		$("#corResidentMobile").html(data.residentMobile);
	}else{
	    $("#corRsIdName").html("");
		$("#corAddr").val("");
		$("#corRsId").val("");
		$("#corIdentityCard").html("");
		$("#corResidentMobile").html("");
	}
}

function combo_afterSelectUnit(data, target){
    if(data){
    	$("#unitRsIdName").html(data.I_NAME);
		$("#unitCertTypeCN").html(data.certTypeCN);
		$("#unitIdentityCard").html(data.identityCard);
		if(data.residentMobile!=null){
			$("#unitResidentMobile").html("("+data.residentMobile+")");
		}
    }else{
    	$("#unitRsIdName").html("");
		$("#unitCertTypeCN").html("");
		$("#unitIdentityCard").html("");
		$("#unitResidentMobile").html("");
    }
}
</script>
</body>
</html>
