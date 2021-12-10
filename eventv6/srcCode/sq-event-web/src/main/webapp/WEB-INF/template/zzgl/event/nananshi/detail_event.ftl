<@override name="eventDetailPageTitle">南安市事件信息详情</@override>

<@override name="geographicalLabelingInput">
    <span>
        <span id="occurrdDiv">${event.occurred!}</span><#if !(isDetail2Edit?? && isDetail2Edit)><span id="resmarkerDiv" style="display: none"><#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/></span></#if>
    </span>
</@override>
<@override name="additionalInitialization">
	var eventType = "${event.type!}";
	
	if(eventType != '22') {
		$('#showimageNa').html('<a id="showImageType" onclick="showImageType(<#if event.eventId??>${event.eventId?c}</#if>)" style="height: 20px;font-size:14px;position: relative;top: 10px;color: red;cursor: pointer;display:none;">事件照片对比</a>');
	}
</@override>
<@override name="feedbackLi">
    <li id="07_li" class="hide">信息反馈列表</li>
</@override>
<@override name="feedbackConList">
    <div id="07_li_div" class="t_a_b_s hide">
        <div class="tabss">
            <div id="eventFeedbackListDiv"></div>
        </div>
    </div>
</@override>
<@override name="eventEvaParams">, 'bizType':'${bizType!}'</@override>
<@override name="feedbackContentConstructor">
    var feedbackCount = data.feedbackCount;
    if(feedbackCount > 0){
        $("#07_li").show();
        var url = "${rc.getContextPath()}/zhsq/reportFeedback/toListFeedback.jhtml?bizSign=" + $("#eventId").val()+"&bizType=${bizType!}";
        $("#eventFeedbackListDiv").append('<iframe id="feedbackIframe" iframeSrc="'+ url +'" scrolling="no" frameborder="0" style="width:100%; height:100%;" />');
        $("#eventFeedbackListDiv > iframe").width($("#workflowDetail").width());
        $("#eventFeedbackListDiv").height($("#workflowDetail").height());
    }
</@override>

<@override name="gridPathTr">
    <tr class="DotLine">
        <td align="right" >所属区域：</td>
        <td colspan="5">
            <code>${event.gridPath!}</code>
        </td>
    </tr>
</@override>

<@override name="slideInitFunction4EventDetail">
	var labelDict = capLabelDict("${event.type!}"),
		typeNameObj = {};
	
	if(labelDict) {
		for(var index in labelDict) {
			typeNameObj[labelDict[index].value] = labelDict[index].name;
		}
	}
	
	getImages(eventId, '${EVENT_ATTACHMENT_TYPE!}', typeNameObj);
</@override>

<@override name="extraDetailFunction">

function showMix(fieldId, index){
    if(allPicArray.length != 0){
    var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId + "&index="+index+"&paths="+allPicArray+"&titles="+seqPicArray.toString();
    var name = "图片查看";
		try{
			parent.showMyPic(url);
		}catch(e){
			showMaxJqueryWindow(name, url);
		}
    }
}
</@override>

<@override name="showImageTypeBlock">
    function showImageType (eventId) {
    	var imgUrl = "${$COMPONENTS_DOMAIN}/zhsq/showImage/showImageType.jhtml?bizId="+eventId+"&attachmentType=ZHSQ_EVENT";
    	
    	try {
    		parent.showMaxJqueryWindow("事件照片对比", imgUrl);
    	} catch(e) {
    		showMaxJqueryWindow("事件照片对比", imgUrl);
    	}

    }
</@override>
<@override name="mapMarkerDiv">
    var markerOperation = $("#markerOperation").val(); // 地图操作类型
    var id = $("#id").val();
    var module = $("#module").val(); // 模块
    if (typeof module == "undefined" || module == "") {
        module = 'EVENT_V1';
    }
    var showName = "标注地理位置";

    $.ajax({
        url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?markerOperation='+markerOperation+'&id='+id+'&module='+module+'&t='+Math.random(),
        type: 'POST',
        timeout: 3000,
        dataType:"json",
        async: false,
        error: function(data){
            $.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning');
        },
        success: function(data) {
            if(data && data.x != "" && data.x != null){
                $('#resmarkerDiv').css('display','inherit');
                $('#occurrdDiv').css('cursor','pointer');
                $('#occurrdDiv').attr('onclick','showMap()');
            }else{
                $('#resmarkerDiv').css('display','none');
            }
        }
    });
    if(markerOperation == 3) {
        $("#mapTab2").html("");//为了展示可视部分
        $("#mapTab2").parent().addClass("mapTab3")
                            .css({"padding-left": "0px", "float": "none", "vertical-align": "top"})
                            .attr("title", "查看地理位置");
        } else {
            $("#mapTab2").html(showName);
    }
</@override>

<#if event.type?? && event.type == '24'>
	<@override name="contactUserLabelSpan">所有人：</@override>
	<@override name="detailExtend">
		<#if event.isAdopted??>
		<tr>
			<td align="right">是否采用：</td>
			<td colspan="5">
				<code><#if event.isAdopted == '1'>是<#else>否</#if></code>
			</td>
		</tr>
		</#if>
	</@override>
	<@override name="additionalHandlePageBefore">
		<#if isHandle?? && isHandle && handleEventPage?? && isShowAdoptedOption?? && isShowAdoptedOption>
		<div class="ListShow" style="padding: 0;">
			<div class="NorForm DetailEdit">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="LeftTd">
							<label class="LabName" style="width: 86px;"><span><label class="Asterik">*</label>是否采用：</span></label>
							<div id="isAdoptedRadioDiv" class="Check_Radio">
								<input type="radio" name="isAdopted" id="isAdopted_1" value="1" style="cursor: pointer;"></input><label for="isAdopted_1" style="cursor: pointer;">是</lable>
								<input type="radio" name="isAdopted" id="isAdopted_0" value="0" style="margin-left: 10px; cursor: pointer;"></input><label for="isAdopted_0" style="cursor: pointer;">否</lable>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		</#if>
	</@override>
</#if>

<@override name="extraFtlInclude">
	<#include "/zzgl/event/nananshi/check_attachment.ftl" />
</@override>
<@extends name="/zzgl/event/detail_event.ftl" />
