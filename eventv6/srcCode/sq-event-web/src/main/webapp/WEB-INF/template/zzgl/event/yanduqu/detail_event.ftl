<@override name="eventDetailPageTitle">盐都区事件信息详情</@override>

<@override name="eventDetailAdditionalQuote">
	<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/basic/yancheng/level-star.css" />
</@override>

<#if isAble2Evaluate?? && isAble2Evaluate==true>
	<@override name="additionalInitialization">
		var panelWidth = $("#metterContentListShowDiv").outerWidth(true);
		
		$("#starEvaResultDiv").width(panelWidth);
		
		$("#starEvaResultDiv").panel({
	        height:'auto',
	        width: 'auto',
	        overflow:'no',
	        href: "${rc.getContextPath()}/zhsq/event/eventDisposal4Extra/toEvaluateEvent.jhtml?eventIdStr=" + $('#eventId').val() + "&evaType=${evaType!}&isEvaContentRequired=false&isPanelLoaded=true",
	        onLoad:function() {
	        	var winWidth = panelWidth - 1 - 1 - 1;//扣除左右边框宽度1像素，另外扣除1像素的偏差值
				
	        	$('#evaluateNorFormDiv').width(winWidth);
	        	$('#evaluateBigToolDiv').width(winWidth);
	        	$('#evaContent').width((winWidth - $('#evaContent').siblings().eq(0).outerWidth(true)) * 0.95);
	        }
	    });
	</@override>
	<@override name="additionalEventPage">
		 <div class="title ListShow" style="padding: 0; border-top: none;">
	 		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	 			<tr>
	 				<td><div id="starEvaResultDiv"></div></td>
	 			</tr>
	 		</table>
		 </div>
	</@override>
</#if>

<@override name="conListLi">
	<#if instanceId??>
		<li id="01_li" class="current">处理环节</li>
	</#if>
	<li id="02_li" class="hide">评价列表</li>
	<li id="03_li" class="hide">督办记录</li>
	<li id="07_li" class="hide">核验记录</li>
	<li id="05_li" class="hide">关联历史事件</li>
	<li id="06_li" class="hide">相似事件</li>
</@override>
<@override name="conListLiContent">
	<#if instanceId??>
		<div id="01_li_div" class="t_a_b_s">
			<div id="workflowDetail" border="false"></div>
		</div>
	</#if>
	
	<div id="02_li_div" class="t_a_b_s hide">
		<div class="tabss">
			<div id="evaResultContent" class="appraise"></div>
		</div>
	</div>
	
	<div id="03_li_div" class="t_a_b_s hide">
		<div class="tabss">
			<div id="superviseResultContent" class="appraise"></div>
		</div>
	</div>
	
	<div id="07_li_div" class="t_a_b_s hide">
		<div class="tabss">
			<div id="timeApplicationContent" class="appraise"></div>
		</div>
	</div>
	
	<div id="05_li_div" class="t_a_b_s hide">
		<div class="tabss">
			<div id="eventAncestorListDiv"></div>
		</div>
	</div>
	
	<div id="06_li_div" class="t_a_b_s hide">
		<div class="tabss">
			<div id="eventDuplicationListDiv"></div>
		</div>
	</div>
</@override>
<@override name="eventEvaParams">
	, 'evaObj': '03,0301,0302'
</@override>
<@override name="evaResultContentConstruct">
	if(evaResultList && evaResultList.length > 0) {
		var evaResultContent = "",
			evaItem = null;
		$("#02_li").show();
		
		for(var index in evaResultList) {
			evaItem = evaResultList[index];
			
			evaResultContent += '<div class="list">';
            evaResultContent += '	<span>';
            evaResultContent += '		<p>';

            if(evaItem.creatorName) {
                evaResultContent += '		<em class="FontDarkBlue">' + evaItem.creatorName + '</em>';
            }
            if(evaItem.createDateStr) {
                evaResultContent += '		于 ' + evaItem.createDateStr;
            }
            if(evaItem.evaLevelName) {
                evaResultContent += '		评价 <b class="FontRed">' + evaItem.evaLevelName + '</b>';
            } else if(evaItem.evaObj && evaItem.evaLevel && (evaItem.evaObj == '0301' || evaItem.evaObj == '0302')) {
            	evaResultContent += '		评价 ' + starFormatter(evaItem.evaLevel);
            }
            
            evaResultContent += '		</p>';
            if(evaItem.evaContent) {
                evaResultContent += '	<p>' + evaItem.evaContent + '</p>';
            }
            evaResultContent += '	 </span>';
            evaResultContent += '</div>';
		}
		
		$("#evaResultContent").html(evaResultContent);
	}
</@override>
<#if event?? && event.bizPlatform?? && event.bizPlatform == '077'>
	<@override name="fetchEventExtraAttrFunction">
		function fetchEventExtraAttr() {}
	</@override>
	<@override name="matterMoreTableContent">
		<tr>
			<td width="75px;" align="right" >信息来源：</td>
			<td><code>${event.sourceName!}</code></td>
			<td width="13%" align="right" >联系人员：</td>
			<td><code>${event.contactUser!}<#if event.tel??>(${event.tel})</#if></code></td>
		</tr>
		<tr>
			<td align="right" >当前状态：</td>
			<td width="18%"><code>${event.statusName!}</code></td>
			<td align="right" >事件编号：</td>
			<td><code>${event.code!}</code></td>
		</tr>
		<tr class="DotLine">
			<td align="right" >所属网格：</td>
			<td colspan="3"><code>${event.gridPath!}</code></td>
		</tr>
		<#if curNodeTaskName??>
		<tr>
			<td align="right" >当前环节：</td>
			<td colspan="3"><code>${curNodeTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code></td>
		</tr>
		</#if>
	</@override>
</#if>
<@override name="timeAppContentConstructor">
	if(timeAppList && timeAppList.length > 0) {
		var timeAppContent = "",
		      applicationType = null,
		      eventCheckAppCount = 0;
		
		for(var index in timeAppList) {
			timeApp = timeAppList[index];
			applicationType = timeApp.applicationType;
			
			switch(applicationType) {
				case '10' : {
					eventCheckAppCount++;
					timeAppContent += eventCheckAppConstructor(timeApp);
					 break;
				}
			}
		}
		
		if(eventCheckAppCount > 0) {
			$("#07_li").show();
		}
		
		$("#timeApplicationContent").html(timeAppContent);
	}
</@override>
<@override name="extraDetailFunction">
	function eventCheckAppConstructor(timeApp) {
		var timeAppContent = "";
		
		if(timeApp) {
			var checkAdvice = timeApp.checkAdvice || '',
				  auditorName = timeApp.auditorName || '',
				  auditorOrgName = timeApp.auditorOrgName || '',
				  timeAppCheckStatus = timeApp.timeAppCheckStatus;
			
			timeAppContent += '<div class="list">';
			timeAppContent += '    <span>';
			timeAppContent += '        <p>';
			if(isNotBlankStringTrim(auditorName) || isNotBlankStringTrim(auditorOrgName)) {
				timeAppContent += '				<em class="FontDarkBlue">' + auditorName + '(' + timeApp.auditorOrgName + ')' + '</em>';
			}
			timeAppContent += '				于 <em class="FontDarkBlue">' + timeApp.createTimeStr + '</em> 核验了该事件';
			timeAppContent += '        </p>';
			if(timeAppCheckStatus) {
				timeAppContent += '				<b>核验结果：</b><b class="FontRed">'+ timeApp.timeAppCheckStatusName +'</b>';
			}
			if(isNotBlankStringTrim(checkAdvice)) {
				timeAppContent += '        <p>';
				timeAppContent += 		   '<b>核验意见：</b>' + checkAdvice;
				timeAppContent += '        </p>';
			}
			timeAppContent += '    </span>';
			timeAppContent += '</div>';
		}
		
		return timeAppContent;
	}
	
	function starFormatter(evaLevel) {
		var starContent = "";
		
		if(evaLevel) {
			var index = 0, starTotal = parseInt(evaLevel, 10), star = "", startColor = null;
			
			while(index < 5) {
				if(index < starTotal) {
					startColor = 'star-yellow';
					
				} else {
					startColor = 'star-gray';
				}
				
				star += '<i class="' + startColor + '"></i>';
				index++;
			}
			
			starContent = '<div class="level-star">' + star + '</div>';
		}
		
		return starContent;
	}
</@override>

<@extends name="/zzgl/event/detail_event.ftl" />