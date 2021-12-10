<!-- custom scrollbar plugin -->
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script src="${rc.getContextPath()}/js/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>

<div id="content-d" class="MC_con content light">
	<div class="h_20"></div>
	<div class="NorForm LeftForm fl">
		<ul>
	    	<li><label class="LabName">事件标题：</label><input type="text" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[100]'" name="eventName" id="eventName" value="<#if event.eventName??>${event.eventName}</#if>" /></li>
	    	
	    	<li class="DiySelect">
	    		<label class="LabName">巡防时间：</label><input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" style="width: 150px;height: 30px;" id="happenTimeStr" name="happenTimeStr" value="<#if event.happenTimeStr??>${event.happenTimeStr}</#if>"/> ～
	    		<input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" style="width: 150px;height: 30px;" id="endTimeStr" name="endTimeStr" value="<#if event.endTimeStr??>${event.endTimeStr}</#if>"/>
	    	</li>
	    	<li class="DiySelect"><label class="LabName">巡防区域：</label><input type="text" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[255]'" name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" /></li>
	    	<li><label class="LabName">巡防人数：</label>
	    		<select id="involvedNum" name="involvedNum" class="sel1">
	    			<#if involvedNumDC??>
						<#list involvedNumDC as l>
							<#if event.involvedNum??>
								<option value="${l.dictGeneralCode}" <#if (l.dictGeneralCode==event.involvedNum)>selected="selected"</#if>>${l.dictName}</option>
							<#else>
								<option value="${l.dictGeneralCode}">${l.dictName}</option>
							</#if>
						</#list>
					</#if>
	    		</select></li>
	    	<li><label class="LabName">带队负责：</label>
	    		<input type="text" class="inp1" data-options="validType:'maxLength[10]'" name="involvedPersion" id="involvedPersion" value="<#if event.involvedPersion??>${event.involvedPersion}</#if>" />
	        </li>
	    	<li><label class="LabName">巡防情况：</label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:'maxLength[1000]'" ><#if event.content??>${event.content}</#if></textarea></li>
	    	<li><label class="LabName">处置情况：</label><textarea name="result" id="result" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:'maxLength[1000]'" ><#if event.result??>${event.result}</#if></textarea></li>
	    	<li><label class="LabName" style="width:85px;">处理前图片：</label><div id="fileupload"></div></li>
	    	<li><label class="LabName" style="width:85px;">处理后图片：</label><div id="fileuploaded"></div></li>
	    </ul>
	</div>
	<div class="NorForm RightForm fr">
		<ul>
			<li><label class="LabName">影响范围：</label>
	    		<select name="influenceDegree" id="influenceDegree" class="sel1">
	    			<#if influenceDegreeDC??>
						<#list influenceDegreeDC as l>
							<#if event.influenceDegree??>
								<option value="${l.dictGeneralCode}" <#if (l.dictGeneralCode==event.influenceDegree)>selected="selected"</#if>>${l.dictName}</option>
							<#else>
								<option value="${l.dictGeneralCode}">${l.dictName}</option>
							</#if>
						</#list>
					</#if>
	    		</select></li>
	    	<li><label class="LabName">紧急程度：</label>
	    		<select name="urgencyDegree" id="urgencyDegree" class="sel1">
	    			<#if urgencyDegreeDC??>
						<#list urgencyDegreeDC as l>
							<#if event.urgencyDegree??>
								<option value="${l.dictGeneralCode}" <#if (l.dictGeneralCode==event.urgencyDegree)>selected="selected"</#if>>${l.dictName}</option>
							<#else>
								<option value="${l.dictGeneralCode}">${l.dictName}</option>
							</#if>
						</#list>
					</#if>
	    		</select></li>
	    	
	    	<li><label class="LabName">地理标注：</label>
	    		<#if event.resMarker.x?? && event.resMarker.y?? && event.resMarker.mapType??>
					<b id="mapTab" class="local" onclick="showMap();" style="cursor:pointer;">已标注</b>
				<#else>
					<b id="mapTab" onclick="showMap();" style="cursor:pointer;">未标注</b>
				</#if>
	    	</li>
	    	<input id="mapt" name="resMarker.mapType" type="hidden" value="<#if event.resMarker.mapType??>${event.resMarker.mapType}</#if>"/>
	    	<input id="x" name="resMarker.x" type="hidden"  value="<#if event.resMarker.x??>${event.resMarker.x}</#if>"/>
	    	<input id="y" name="resMarker.y" type="hidden"  value="<#if event.resMarker.y??>${event.resMarker.y}</#if>"/>
	    	<li><label class="LabName">信息来源：</label>
	    		<select name="source" id="source" class="sel1">
	    			<#if sourceDC??>
						<#list sourceDC as l>
							<#if event.source??>
								<option value="${l.dictGeneralCode}" <#if (l.dictGeneralCode==event.source)>selected="selected"</#if>>${l.dictName}</option>
							<#else>
								<option value="${l.dictGeneralCode}">${l.dictName}</option>
							</#if>
						</#list>
					</#if>
	    		</select></li>
			<li><label class="LabName">填报人：</label><input type="text" class="inp1 easyui-validatebox InpDisable" disabled data-options="validType:'maxLength[30]'" name="creator" id="creator" value="<#if creator??>${creator}</#if>" /></li>
	    </ul>
	</div>
</div>

<div class="clear"></div>

<#include "/component/involvedPeopleSelector.ftl">

<script type="text/javascript">
	(function($){
		<#if event.involvedNum??>
		<#else>
			$("#involvedNum").get(0).selectedIndex=2;// 设置巡防人数默认选中
		</#if>
		$(window).load(function(){
			$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
			$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
			$("#content-d").mCustomScrollbar({theme:"dark"});
			$("#content-md").mCustomScrollbar({theme:"minimal-dark"});
		});
	})(jQuery);
	
	$(function(){
		<#if event.eventId??>
			swfUpload1 = fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':1},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
			
			swfUpload2 = fileUpload({
				positionId:'fileuploaded',//附件列表DIV的id值',
				upload_table:'upload_table1',
				cancel_button:'cancel_button1',	
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${event.eventId?c},'attachmentType':'${event.type}','eventSeq':3},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
		<#else>
			swfUpload1 = fileUpload({ 
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'eventSeq':1},//未处理
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
			
			swfUpload2 = fileUpload({ 
				positionId:'fileuploaded',//附件列表DIV的id值',
				upload_table:'upload_table1',
				cancel_button:'cancel_button1',	
				type:'add',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'eventSeq':3},//未处理
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
		</#if>
	});
</script>
