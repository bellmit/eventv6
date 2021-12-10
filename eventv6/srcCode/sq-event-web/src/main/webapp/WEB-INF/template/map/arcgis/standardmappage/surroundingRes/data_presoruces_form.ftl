<div class="cont"> 
       	<!-- 周边网格员 -->
        <#if gridAdmin??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
               <tr>
               	 <td style="width:35px;"><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/jcsj/xm_qing_icon1.png" width="32" height="32" /></td>
                 <td onclick="" style="width:70%;padding-top:2px;">
                 	${gridAdmin.objName}
                 	<br/>
                 	备注：${gridAdmin.remark}&nbsp;
                 </td>
                 <td  align="right">${gridAdmin.distance}</td>
               </tr>
            </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/jcsj/xm_qing_icon1.png" width="32" height="32" />&nbsp;没有查找到周边网格员</td>
				</tr>
			</table>
		</#if>
       	<!-- end 周边网格员 -->		
		
       	<!-- 周边派出所 -->
        <#if policeStation??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
               <tr>
               	 <td style="width:35px;"><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/jingwushi/jws.png" width="32" height="32" /></td>
                 <td onclick="" style="width:70%;padding-top:2px;">
                 	${policeStation.objName}
                 	<br/>
                 	备注：${policeStation.remark}&nbsp;
                 </td>
                 <td  align="right">${policeStation.distance}</td>
               </tr>
            </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/resource/xfs/xfs.png" width="32" height="32" />&nbsp;没有查找到周边派出所</td>
				</tr>
			</table>
		</#if>
       	<!-- end 周边派出所 -->
        <!-- 周边全球眼资源 -->
        <#if ciCommunityMonitor??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
               <tr>
               	 <td style="width:35px;"><img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/chuangqy.png" width="32" height="32" /></td>
                 <td onclick="" style="width:70%;padding-top:2px;">
                 	${ciCommunityMonitor.objName}
                 	<br/>
                 	地址：${ciCommunityMonitor.remark}&nbsp;
                 </td>
                 <td  align="right">${ciCommunityMonitor.distance}</td>
               </tr>
            </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/chuangqy.png" width="32" height="32" />&nbsp;没有查找到周边全球眼资源</td>
				</tr>
			</table>
		</#if>
       	<!-- end 周边全球眼资源 -->
       	<!-- 周边消防栓资源 -->
        <#if xiaofangshuan??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
               <tr>
               	 <td style="width:35px;"><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/resource/xfs/xfs.png" width="32" height="32" /></td>
                 <td onclick="" style="width:70%;padding-top:2px;">
                 	${xiaofangshuan.objName}
                 	<br/>
                 	备注：${xiaofangshuan.remark}&nbsp;
                 </td>
                 <td  align="right">${xiaofangshuan.distance}</td>
               </tr>
            </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/styles/sosp_haicang/images/resource/xfs/xfs.png" width="32" height="32" />&nbsp;没有查找到周边消防栓资源</td>
				</tr>
			</table>
		</#if>
       	<!-- end 周边消防栓资源 -->


		<!-- 周边党组织资源 -->
      	<#if partyInfo??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
                <tr>
                 	<td style="width:35px;">
                 		<img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/dangzz.png" width="32" height="32" />
                 	</td>
                  	<td onclick="" style="width:70%;padding-top:2px;">${partyInfo.objName}<br/>地址：<#if partyInfo.remark??>${partyInfo.remark}</#if>&nbsp;</td>
                  	<td align="right">${partyInfo.distance}</td>
                </tr>
              </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/louy.png" width="32" height="32" />&nbsp;&nbsp;没有查找到周边党组织资源</td>
				</tr>
			</table>
		</#if>
        <!-- end 周边党组织资源 -->
        <!-- 周边楼宇资源 -->
		<#if objBuilding??>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
                <tr>
                 	<td style="width:35px;">
                 		<img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/louy.png" width="32" height="32" />
                 	</td>
                  	<td onclick="" style="width:70%;padding-top:2px;">${objBuilding.objName}<br/>地址：<#if objBuilding.remark??>${objBuilding.remark}</#if>&nbsp;</td>
                  	<td align="right">${objBuilding.distance}</td>
                </tr>
              </table>
		<#else>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="resdetail">
				<tr>
					<td><img src="${rc.getContextPath()}/theme/scim/wap/wapstyle/sosp_haicang/images/louy.png" width="32" height="32" />&nbsp;&nbsp;没有查找到周边楼宇资源</td>
				</tr>
			</table>
		</#if>
         <!-- end 周边楼宇资源 -->           	
	</div>