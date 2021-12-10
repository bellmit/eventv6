<div class="clear title EventLabelTitle FontDarkBlue" id="eventBasicInfo">基本信息</div>
<div class="has-more">
	<div class="basic-infor">
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<input type="hidden" name="isDetail2Edit" value="<#if isDetail2Edit??>${isDetail2Edit}</#if>" />
		<!--防止以下属性被设置为默认值，故而需要携带-->
		<input type="hidden" name="happenTimeStr" value="${event.happenTimeStr!}" />
		<input type="hidden" name="urgencyDegree" value="${event.urgencyDegree!}" />
		<input type="hidden" name="eventInvolvedPeople" value="${event.eventInvolvedPeople!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		
	    <div style="margin:0 auto;">
	    	<div id="content-d" style="position:relative;left:0;top:0;overflow-x:hidden;overflow-y:auto"><!--使得事件简介能随着滚动条移动，隐藏横向滚动条-->
	        	<div class="MetterContent" style="margin:0 auto; padding: 0;">
		            <div class="title ListShow" style="background:none; padding-right: 0; ">
		            	<div id="contentDiv" class="fl" style="width:610px; height: 310px; position: relative;">
		            		
		            		<div id="MetterBrief" style="border-bottom:1px dotted #cecece;">
		                    	<div id="dubanIconDiv" class="dubanIcon hide"></div>
			                	<ul>
			                    	<li style="word-break: break-all; width:97%; *width:94%;">
			                            <p><#if event.eventClass??>[${event.eventClass}]</#if> <span>${event.eventName!}</span></p>
			                            <p>
			                            	于 <span>${event.happenTimeStr!}</span> 
			                            	在 <span>${event.occurred!} <#if !(isDetail2Edit?? && isDetail2Edit)><#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/></#if></span>
			                            	<#if event.content??>发生 <span>${event.content!}</span></#if>
			                            </p>
			                    	</li>
			                    </ul>
		                    </div>
		                    
	                    	<div id="MetterMore" class="ListShow ListShow2" style="word-break: break-all; border: none;">
		                    	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="line-height: 30px;">
				                	<tr>
				                		<td width="80px;" align="right" >信息来源：</td>
				                		<td><code>${event.sourceName!}</code></td>
	            						<td align="right" >事件编号：</td>
                                        <td colspan="3"><code>${event.code!}</code></td>
				                	</tr>
				                	<tr>
				                		<td align="right">紧急程度：</td>
	            						<td>
		            						<#if event.urgencyDegree?? && event.urgencyDegree!='01'>
												<code style="color:#e60012">${event.urgencyDegreeName!}</code>
											<#else> 
												<code>${event.urgencyDegreeName!}</code>
											</#if>
				                		</td>
                                        <td  align="right">采集渠道：</td>
	            						<td colspan="3"><code>${event.collectWayName!}</code></td>
				                	</tr>
				                	<tr>
				                		<td align="right" >影响范围：</td>
				                		<td><code>${event.influenceDegreeName!}</code></td>
                                        <td align="right" >当前状态：</td>
                                        <td colspan="3" ><code>${event.statusName!}</code></td>
				                	</tr>
				                	<tr>
                                        <td align="right">涉及人数：</td>
										<td><code><#if event.involvedNumInt??>${event.involvedNumInt}<#else >0</#if><label><span>（人）</span></label></code></td>
                                        <td align="right" >联系人员：</td>
                                        <td colspan="3">
                                            <code>
											${event.contactUser!}
											<#if event.tel??>
												<#if (isOnVoiceCall?? && isOnVoiceCall) && (eventType?? && (eventType=='my' || eventType=='todo'))>
                                                    <a href="###" style="text-decoration:none;" onclick="showVoiceCall('${rc.getContextPath()}', window.parent.showCustomEasyWindow, '${event.tel}','<#if event.contactUser??>${event.contactUser}</#if>')">(${event.tel}<img title="语音呼叫" src="${uiDomain!''}/images/cloundcall.png">)</a>
												<#else>
                                                    (${event.tel})
												</#if>
											</#if>
                                            </code>
                                        </td>
				                	</tr>
				                	<tr>
                                    	<td align="right" >涉及人员：</td>
										<#if involvedPeopleList?? && (involvedPeopleList?size >0)>

											<td  style="text-align: center">姓名</td>
											<td style="text-align: center;">证件类型</td>
											<td colspan="2" style="text-align: center;width: 30%;">证件号码</td>
											<td style="text-align: center">联系方式</td>

											<input id="numOfInvolvedPeople" type="hidden" value="${involvedPeopleList?size}"/>
											<#list involvedPeopleList as l >
												<tr>
                                                    <td align="right">
                                                    </td>
													<td style="text-align: center">
														<code><span>${l.name!''}</span></code>
													</td>
													<td style="text-align: center">
														<code><span>${l.cardTypeName!''}</span></code>
													</td>
													<td colspan="2"  style="text-align: center">
                                                        <code><span>${l.idCard!''}</span></code>
													</td>
													<td style="text-align: center">
                                                        <code><span>${l.tel!''}</span></code>
													</td>
													<td>
														<input id="ciRsId${l_index}" value="${l.ciRsId!''}" type="hidden"/>
													</td>
												</tr>
											</#list>
											<#else >
                                                <td><code><span>（无）</span></code></td>
										</#if>
				                	</tr>
				                	<tr class="DotLine">
				                		<td align="right" >所属网格：</td>
	        							<td colspan="5">
	        								<code>${event.gridPath!}</code>
				                		</td>
				                	</tr>
				                	
				                	<#if curNodeTaskName??>
					                	<tr>
					                		<td align="right" >当前环节：</td>
					                		<td colspan="5">
	            								<code>${curNodeTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code>
					                		</td>
					                	</tr>
				                	</#if>
				                	<#if isHandle?? && handleTime?? && isHandle && stepRemainTime??>
				                		<#assign stepHandle = true>
				                	<#else>
				                		<#assign stepHandle = false>
				                	</#if>
				                	
				                	<#if event.handleDateStr?? && eventRemainTime??>
				                		<#assign eventHandle = true>
				                	<#else>
				                		<#assign eventHandle = false>
				                	</#if>
				                	
				                	<#if stepHandle || eventHandle>
					                	<tr>
					                		<td align="right" >处理时限：</td>
					                		<td colspan="5">
			                					<#if stepHandle>
				            						(环节)<code><b <#if (stepRemainTime?length>3) && (stepRemainTime?substring(0,2)=='超时')>class="FontRed"</#if>>${stepRemainTime}</b></code>
			            						</#if>
			            						<#if eventHandle>
				            						(事件)<code><b <#if event.handleDateFlag?? && event.handleDateFlag=='3'>class="FontRed"</#if>>${eventRemainTime}</b></code>
			            						</#if>
					                		</td>
					                	</tr>
				                	</#if>
				                </table>
			                </div>
			                
		                </div>
		                
		                <div id="slider" class="fr" style="width:300px; height:180px; border-left:1px solid #cecece;">
	                		<ul></ul>
		                </div>
		            	<div class="clear"></div>
		            </div>
		            
		            <#if bizDetailUrl??>
		            	<div class="WebNotice" <#if showNotice??&&showNotice='0'>style="display:none;"</#if>>
		            		<p>此事件关联了业务单信息，<a href="###" onclick="showDetail()">点击查看</a></p>
		            	</div>
		            </#if>
		            
	            </div>
	        </div>
	    </div>
		
	</div><!--basic-infor end-->
                    
</div>