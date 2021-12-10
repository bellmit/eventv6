<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>宜居罗坊-详情</title>
<#if Session.gmisDomain?exists>
	<#assign gmisDomain = Session.gmisDomain>
</#if>
<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />

</head>
<body>
	<div class="con LyWatch"  >
        	<ul>
                <li style="width:350px; word-wrap: break-word;">
                	<p><b class="FontDarkBlue"  style="font-size:14px;">${livableLF.name!''}
                	</b></p>

                    <p title="<#if livableLF.survey?exists>${livableLF.survey}</#if>">

                    <span>
					<#if livableLF.survey?exists>${livableLF.survey}</#if>
                    </span></p>

                </li>
            </ul>
        </div>
</body>
<script type="text/javascript">
function showDetail(title,id) {
	var sq_zzgrid_url = window.parent.document.getElementById("SQ_ZZGRID_URL").value;
	var url = "${gmisDomain!''}/gmis/landscapeManage/detail.jhtml?gardenId="+id+"&showClose=false";
	//window.parent.showDetailLigerUI(title,url,850,480)
	window.parent.showMaxJqueryWindow(title,url,600,400);	
}
</script>
</html>