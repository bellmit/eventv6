<@override name="eventPrintPageTitle">盐都区打印事件详情</@override>
<@override name="eventAttrTr">
	<#if EVENT_DETAILS_SHOW_PICTURE??>
    	<#if attList?? && attList?size &gt; 0>
		  <tr class="tr_style">
		 	<td class="pictd" colspan=4>
		 		<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr style="border:0;">
						<#assign fileCount = 0, picTypes = 'png,jpg,gif,jpeg,webp' />
						<#list attList as att>
							<#if att.filePath??>
								<#assign filePrefix = (att.filePath?substring(att.filePath?last_index_of('.') + 1))?lower_case />
								
								<#if (picTypes?index_of(filePrefix) >= 0) && (fileCount < EVENT_DETAILS_SHOW_PICTURE)>
									<#assign fileCount++ />
									<td style="text-align:center;vertical-align:middle;border:0;" >
				    					<img title="${att.fileName!}" style="width:${1000/pictureNum-10}px;height:auto;" src="${imgDomain}${att.filePath}" />
									</td>
								</#if>
							</#if>
						</#list>
					</tr>
				</table>
			</td>
		  </tr>
		</#if>
	</#if>
</@override>
	  
<@extends name="/zzgl/event/print.ftl" />