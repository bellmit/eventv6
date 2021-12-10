<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><@block name="eventPrintPageTitle">打印事件详情</@block></title>
<#include "/component/commonFiles-1.1.ftl" />
<style type="text/css">
	*{margin:0; padding:0; list-style:none; font-family:"微软雅黑"; font-size:12px;}
	table{margin:0 auto;}
	.con{border-bottom:1px solid #333; border-right:1px solid #333;}
	.con td{padding:5px 10px; border-top:1px solid #333; border-left:1px solid #333; font-size:14px;}
	h1{text-align:center; font-size:24px; padding-top:10px; font-weight:normal;}
	.tr_style {height:30px;}
	@media print {   
	 .noprint{display:none;}
	}
</style>
</head>
<body>
<div class="noprint tool" style="position: fixed; top: 5px; right: 5px;">  
	<a href="###" class="NorToolBtn PrintBtn" onclick="window.print();">打印</a>
</div>
<h1><@block name="eventPrintHeadTitle"><#if EVENT_DETAILS_SHOW_PICTURE ??><#else>综治</#if>网格化信息平台事件详情单</@block></h1>
<table width="1000" height="44" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="420" style="font-size:14px;"><strong style="font-size:13px;">单号：</strong><#if event.code??>${event.code}</#if></td>
    <td width="330" style="font-size:14px;">&nbsp;</td>
    <td align="right" style="font-size:14px;">${year}<strong style="font-size:13px;">年</strong>${month}<strong style="font-size:13px;">月</strong>${day}<strong style="font-size:13px;">日印</strong></td>
  </tr>
</table>
<div style="word-break: break-all;">
	<table id="eventPrintTable" width="1000" border="0" cellspacing="0" cellpadding="0" class="con">
	  <tr class="tr_style">
	    <td width="90">事件类型：</td>
	    <td width="410"><#if event.eventClass??>${event.eventClass}</#if></td>
	    <td width="90">事件标题：</td>
	    <td width="410"><#if event.eventName??>${event.eventName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">所属网格：</td>
	    <#if EVENT_DETAILS_SHOW_PICTURE ??  >
	    <td width="410"><#if event.gridPath??>${event.gridPath}</#if></td>
	    <#else>
	    <td width="410"><#if event.gridName??>${event.gridName}</#if></td>
	    </#if>
	    <td width="90">采集渠道：</td>
	    <td width="410"><#if event.collectWayName??>${event.collectWayName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">信息来源：</td>
	    <td width="410"><#if event.sourceName??>${event.sourceName}</#if></td>
	    <td width="90">影响范围：</td>
	    <td width="410"><#if event.influenceDegreeName??>${event.influenceDegreeName}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">紧急程度：</td>
	    <td width="410">
	    	<#if event.urgencyDegree?? &&event.urgencyDegree=='02'>
	    		<span style="color:red"><#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if></span>
	    	<#else>
	    		<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>
	    	</#if>
	    </td>
	    <td width="90">涉及人员：</td>
	    <td width="410"><#if event.involvedNumName??>(<b>${event.involvedNumName}</b>)</#if><#if event.involvedPersion??>${event.involvedPersion}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">采集时间：</td>
	    <td width="410"><#if event.createTimeStr??>${event.createTimeStr}</#if></td>
	    <td width="90">事发时间：</td>
	    <td width="410"><#if event.happenTimeStr??>${event.happenTimeStr}</#if></td>
	  </tr>
	  <@block name="contactUseTr">
	  <tr class="tr_style">
	  	<td width="90">联系人员：</td>
	    <td width="410"><#if event.contactUser??>${event.contactUser}</#if></td>
	    <td width="90">联系电话：</td>
	    <td width="410"><#if event.tel??>${event.tel}</#if></td>
	  </tr>
	  </@block>
	  <@block name="singleLineExtraInfoTr"></@block>
	  <tr class="tr_style">
	    <td width="90">事发详址：</td>
	    <td width="910" colspan="3"><#if event.occurred??>${event.occurred}</#if></td>
	  </tr>
	  <tr height="70">
	    <td width="90">事件描述：</td>
	    <td width="910" colspan="3"><#if event.content??>${event.content}</#if></td>
	  </tr>
	  <tr class="tr_style">
	    <td width="90">处理记录：</td>
	    <td width="910" colspan="3" style="padding:0;">
	    	<#if taskList??>
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="con2">
	    		<tr>
	    			<td width="15%" style="border-left:none; border-top:none;"><strong>办理环节</strong></td>
	    			<td width="30%" style="border-top:none;"><strong>办理人/办理时间</strong></td>
	    			<td width="55%" style="border-top:none;"><strong>办理意见</strong></td>
	    		</tr>
	    		<#list taskList as l>
					<tr>
		    			<td style="border-left:none; border-top:1px solid #333;">${l.TASK_NAME!'&nbsp;'}</td>
		    			<td>
		    				<#if l.HANDLE_PERSON??>
		    					<p>${l.HANDLE_PERSON!'&nbsp;'}</p>
		    				<#else>
			    				<p>
			    					[${l.ORG_NAME}]-${l.TRANSACTOR_NAME} 耗时 
			    					<#if (l.INTER_TIME == '0分钟')>
			    						小于1分钟
			    					<#else>
			    						${l.INTER_TIME}
			    					</#if>
			    					<#if (l.ISTIMEOUT?? && l.ISTIMEOUT=='1')>超时</#if>
			    				</p>
		                        <p>办理时间：${l.END_TIME}</p>
	                        </#if>
		    			</td>
		    			<td>${l.REMARKS!'&nbsp;'}</td>
		    		</tr>
	            </#list>
	    	</table>
	    	<#else>
	    		&nbsp;
		    </#if>
	    </td>
	  </tr>
	  <@block name="eventAttrTr">
	  	<#if EVENT_DETAILS_SHOW_PICTURE??>
	    	<#if attList?? && attList?size &gt; 0>
			  <tr id="eventAttachTr" class="tr_style">
			  	<td width="90">附件记录：</td>
			  	<td width="910" colspan="3" style="padding:0;">
		  			<#list attList as attachment>
		  				<#if attachment.filePath??>
		  					<div style="text-align: center; width: 49%; height: 100%; padding: 4px 4px 24px 4px; float: left; position: relative;">
		  						<div class="picDragDiv" style="position: relative; width: 436px; height: 436px;" filePath="${attachment.filePath}">
			  						<image width="100%" height="100%" src="${IMG_URL!}/${attachment.filePath!}" title="双击移除该图片" style="cursor: pointer; display: block; padding-left: 1px;" ondblclick="$(this).parent().parent().remove();" />
			  						<span style="font-size: 14px; padding:6px 0px; display: block;"><#if attachment.eventSeq??><#if attachment.eventSeq=='1'>处理前<#elseif attachment.eventSeq=='2'>处理中<#elseif attachment.eventSeq=='3'>处理后</#if></#if></span>
			  					</div>
			  					
		  					</div>
		  				</#if>
		  			</#list>
			  	</td>
			  </tr>
			</#if>
		</#if>
	  </@block>
	</table>
</div>
</body>
<script type="text/javascript">
	$(function() {
		DragSize.prototype = {//为DriagSize方法添加其他属性以便调用
	        getMove: function(obj) {
	            var self = this;
	            obj.onmousedown = function(ev) {//为绑定的div绑定鼠标按下事件
	                var oBox = self.dom;
	                var oEvent = ev || event;
	                var disX = oEvent.clientX - this.offsetLeft;
	                var disY = oEvent.clientY - this.offsetTop;
	                oEvent.cancelBubble = true;//阻止默认
	                document.onmousemove = function(ev) {  //给页面绑定一个mousemove事件,用于拖动或者拖拽大小时用
	                    var oEvent = ev || event;
	                    var oRrsizeX = oEvent.clientX - disX;
	                    var oRrsizeY = oEvent.clientY - disY;
	                    obj.style.left = oRrsizeX + "px";
	                    obj.style.top = oRrsizeY + "px";
	                    if (obj == self.dragDiv) {
	                        if (oRrsizeX < 10) {//这里的10写死了,因为用于拖拽的点我写死了10px
	                            oRrsizeX = 10;
	                        }
	                        if (oRrsizeY < 10) {//这里的10写死了,因为用于拖拽的点我写死了10px
	                            oRrsizeY = 10;
	                        }
	                        oBox.style.width = oRrsizeX + obj.offsetWidth + "px";
	                        oBox.style.height = oRrsizeY + obj.offsetHeight + "px";
	                        obj.style.left = oRrsizeX + "px";
	                        obj.style.top = oRrsizeY + "px";
	                    }
	
	                    return false;
	                }
	                document.onmouseup = function() {//鼠标弹起时别忘了把其他事件给取消了哈
	                    document.onmousemove = null;
	                    document.onnouseup = null;
	                }
	                return false;
	            }
	        }
	    };
		
	    $('#eventAttachTr div.picDragDiv').each(function(index) {
	    	var filePath = $(this).attr('filePath'), 
	    		prefix = filePath.substr(filePath.lastIndexOf('.') + 1).toLowerCase(), 
	    		imgStr = "png,jpg,gif,jpeg,webp";
	    	
	    	if(imgStr.indexOf(prefix) >= 0) {
	    		new DragSize($(this)[0]);
	    	} else {
	    		$(this).parent().remove();
	    	}
	    });
	});
	
	function DragSize(dom) {
	    var self = this;
	    self.dom = dom;
	    self.dragDiv = createDragDiv();
	    
	    if (self.dom && self.dom.tagName == "DIV") {
	        self.dom.appendChild(self.dragDiv);
	        self.getMove(self.dom);
	        self.getMove(self.dragDiv);
	    } else {
	        throw "Wrong dom for binding.";
	    }
	    return self;
	}
	
	//生成一个显示在右下角的点
	function createDragDiv() {
	    var div = document.createElement("DIV");
	    div.style.width = div.style.height = "10px";
	    //div.style.background = "#6D71EF";
	    div.style.position = "absolute";
	    div.style.right= "0px";
	    div.style.bottom= "0px";
	    div.style.cursor = "se-resize";
	    return div;
	}
</script>
</html>
