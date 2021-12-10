
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>新增事件信息</title>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/detail.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/form-add.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/style/css/event-info.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
		<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
		<link href="${rc.getContextPath()}/ui/workorder/css/picUpload.css" rel="stylesheet" />
		<#include "/component/commonFiles-1.1.ftl" />
		<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
		<script type="text/javascript" src="${SQ_ZZGRID_URL}/theme/scim/scripts/jq/plugins/json/json2.js"></script>
		<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
		<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
		<#include "/component/ComboBox.ftl" />
		<script type="text/javascript">
			var base = "${rc.getContextPath()}",imgDomain = '${imgDomain}',fileDomain = '${fileDomain}';
		</script>
		<style>
	    	.pic_content:hover .off_btn{display:block;}
	    	.textbox.combo>input{width:90%!important;}
	    	.textbox.numberbox.spinner>input{width:90%!important;line-height:30px;}
	    	.textbox.numberbox>input{width:100%!important;line-height:30px;}
	    	.textbox-addon.textbox-addon-right>a{margin-top:5px!important;}
    		.l-btn.l-btn-small {color: #444!important;background:-webkit-linear-gradient(top,#ffffff 0,#d8d8d8 100%)!important;}
	    	.addinfo.p{margin-top:9px;float:left;margin-right:15px;}
	    </style>
	</head>
	<body class="xiu-body">
	  	<#if attList?? && attList?size gt 0>
            <input type="hidden" id="global_index" value="${attList?size}">
            <#else>
            <input type="hidden" id="global_index" value="0">
       </#if>
		<div class="container-detail">
			<div class="form-warp-sh">
				<div class="fw-toptitle">
					<h6 class="note-s">带<span>*</span>为必填项</h6>
				</div>
				<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" id="name" name="name" value="<#if event.eventName??>${event.eventName}</#if>" />
				
				<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
				<input type="hidden" id="gridCode" name="gridCode" value="<#if event.gridCode??>${event.gridCode}</#if>">
				<input type="hidden" id="type" name="type" value="<#if event.type??>${event.type}</#if>" />
				<input type="hidden" id="code" name="code" value="<#if event.code??>${event.code}</#if>" />
				<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
				<input type="hidden" name="parentEventId" value="<#if parentEventId??>${parentEventId?c}</#if>" />
				<input type="hidden" id="id" name="id" value="<#if mediationId??>${mediationId}</#if>"/>
		        <input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>"/>
		        <input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
				
				<!--办理意见-->
				<input type="hidden" id="result" name="result" value="${event.result!}" />
				
				<!--用于地图-->
				<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
				<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
				<!--事件上报关联模块-->
				<input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
				<input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
				
				<div class="fw-main" >
					<div class="fw-det-tog ">
						<div class="fw-det-toggle">
							<ul class="fw-xw-from clearfix mr30">
								<li class="xw-com2">
									<span class="fw-from1">事件分类：</span>
									<input class="from flex1 bg-btm-arrow2 " id="typeName" name="typeName"  type="text" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" placeholder="请选择">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">所属网格：</span>
									<input class="from flex1 bg-btm-arrow2 " id="gridName" name="gridName" type="text" value="<#if event.gridName??>${event.gridName}</#if>" placeholder="请选择所属网格">
								</li>
								<li class="xw-com2">
									<span class="fw-from1"><i class="spot-xh">*</i>事件标题：</span>
									<input class="inp1 easyui-validatebox from flex1 bg-btm-arrow1" name="eventName" id="eventName" type="text" value="<#if event.eventName??>${event.eventName}</#if>" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" placeholder="请输入事件标题">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">影响范围：</span>
									<input type="hidden" id="influenceDegree" name="influenceDegree" value="<#if event.influenceDegree??>${event.influenceDegree}</#if>" />
									<input class="from flex1 bg-btm-arrow2" type="text" id="influenceDegreeName" value="<#if event.influenceDegreeName??>${event.influenceDegreeName}</#if>" placeholder="请输入信息来源">
								</li>
								<li class="xw-com2">
									<span class="fw-from1"><i class="spot-xh">*</i>事发时间：</span>
									<input id="happenTimeStr" name="happenTimeStr" class="from flex1 bg-btm-arrow1 pr30" type="text" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
									<a href="javascript:void(0);" onclick="WdatePicker({el:'happenTimeStr',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="time-switch flex flex-ac flex-jc">
										<img src="${rc.getContextPath()}/style/images/icon_time.png"/>
									</a>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">信息来源：</span>
									<input type="hidden" id="source" name="source" value="<#if event.source??>${event.source}</#if>" />
									<input class="from flex1 bg-btm-arrow2" type="text" id="sourceName" value="<#if event.sourceName??>${event.sourceName}</#if>" placeholder="请输入信息来源">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">紧急程度：</span>
									<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="<#if event.urgencyDegree??>${event.urgencyDegree}</#if>" />
									<input class="from flex1 bg-btm-arrow2" type="text" id="urgencyDegreeName" value="<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>" placeholder="请选择">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">地理标注：</span>
									<div class="flex1 flex" >
										<a href="javascript:void(0);" class="geographical-position flex flex-ac">
											<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
										</a>
									</div>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">涉及人数：</span>
									<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
									<input class="from flex1 bg-btm-arrow2" type="text" id="involvedNumName" name="involvedNumName"  value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" placeholder="请选择">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">联系人员：</span>
									<input class="from flex1 bg-btm-arrow1" id="contactUser" name="contactUser" type="text" value="<#if event.contactUser??>${event.contactUser}</#if>" placeholder="请输入网格员">
								</li>
								<li class="xw-com2">
									<span class="fw-from1">联系电话：</span>
									<input class="from flex1 bg-btm-arrow1" name="tel" id="tel" type="text" value="<#if event.tel??>${event.tel}</#if>" placeholder="请输入11位电话号码">
								</li>
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh">*</i>事发祥址：</span>
									<input class="inp1 easyui-validatebox from flex1 bg-btm-arrow1" name="occurred" id="occurred" type="text" value="<#if event.occurred??>${event.occurred}</#if>" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" placeholder="您还未输入事发祥址">
								</li>
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh">*</i>事件描述：</span>
									<textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox from flex1 bg-btm-arrow1" style="width:400px; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[1024]','characterCheck']" ><#if event.content??>${event.content}</#if></textarea>
								</li>
								<li class="xw-com2">
									<span class="fw-from1">涉及人员：</span>
									<input type="hidden" name="eventInvolvedPeople" id="eventInvolvedPeople" value="<#if event.eventInvolvedPeople??>${event.eventInvolvedPeople}</#if>" />
					    			<input type="hidden" name="involvedPersion" id="involvedPersion" value="<#if event.involvedPersion??>${event.involvedPersion}</#if>" />
									<div class="flex1 flex addinfo" >
										<code class="fl FontDarkBlue" onclick="showInvoledPeopleSelector();">
											<a href="javascript:void(0);" class="involve-people flex flex-ac">
												<p>点击添加人员</p>
											</a>
										</code>
										<div id="involvedPeopleName" style="margin-left:10px;">
						            		<#if involvedPeopleList?? >
						            			<#list involvedPeopleList as l>
										    		<p style='margin-top:9px!important' title="<#if l.name??>${l.name}</#if>(<#if l.idCard??>${l.idCard}</#if>)"><#if l.name??>${l.name}</#if><imgsrc='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople("<#if l.name??>${l.name}</#if>","<#if l.idCard??>${l.idCard}</#if>", $(this).parent());'/></p>
										    	</#list>
						            		</#if>
					            		</div>
									</div>
								</li>
							</ul>
							<div class=" dotted-line"></div>
							<ul class="fw-xw-from clearfix mr30 mt20">
								<li class="xw-com1">
									<span class="fw-from1">图片上传：</span>
									<div class="fw-radio-box">
										<input type="radio" id="fileupload_upload_pic_radio_0" class="fw-radio" name="eventSeq" value="1" checked="checked"/>
										<label for="fileupload_upload_pic_radio_0" class="mr radio-box">
											<div class="radio-input1 ">
												<div></div>
											</div>
											处理前
										</label>
										 <input type="radio" id="fileupload_upload_pic_radio_1" class="fw-radio" name="eventSeq" value="2"/>
										<label for="fileupload_upload_pic_radio_1" class="mr radio-box">
											<div class="radio-input1">
												<div></div>
											</div>
											处理中 
										</label>
										<input type="radio" id="fileupload_upload_pic_radio_2" class="fw-radio" name="eventSeq" value="3"/>
										<label for="fileupload_upload_pic_radio_2" class="mr radio-box">
											<div class="radio-input1">
												<div></div>
											</div>
											处理后
										</label>
									</div>
								</li>
								<div class="xw-com1 pic-upload">
									<div id="attas" class="flex mt15 flex-wrap">
										<a href="javascript:void(0);">
											<div id="chooeseAtta" class="upload-box flex flex-ac flex-jc" style="width:110px;height:100px;">
												<img src="${rc.getContextPath()}/style/images/icon_upload.png" style="width:50px;height:40px;"/>
											</div>
											<p>点击上传</p>
										</a>
										<#list attList as att>
											<input type="hidden" name="attList[${att_index}].id" value="${att.attachmentById}"/>
	                                        <input type="hidden" name="attList[${att_index}].title" value="${att.fileName}"/>
	                                        <input type="hidden" name="attList[${att_index}].fileName" value="${att.fileName}"/>
	                                        <input type="hidden" name="attList[${att_index}].filePath" value="${att.filePath}"/>
						    				<#if att.title == 'image'>
											<div class="pic pic_content" id="pic_${att_index}">
												<span title="点击预览该图片" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
													<img title="${att.fileName}" style="width:56px;height:36px;" src="${imgDomain}${att.filePath}" />
													<p>[处理前]${att.fileName}</p>
												</span>
												<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
											</div>
						    				<#elseif att.title == 'excel'>
						    				<div class="pic pic_content" id="pic_${att_index}">
												<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
													<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_excel.png" />
													<p>[处理前]${att.fileName}</p>
												</span>
												<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
											</div>
											<#elseif att.title == 'word'>
						    				<div class="pic pic_content" id="pic_${att_index}">
												<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
													<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_word.png" />
													<p>[处理前]${att.fileName}</p>
												</span>
												<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
											</div>
											<#elseif att.title == 'ppt'>
						    				<div class="pic pic_content" id="pic_${att_index}">
												<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
													<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_ppt.png" />
													<p>[处理前]${att.fileName}</p>
												</span>
												<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
											</div>
											<#else>
						    				<div class="pic pic_content" id="pic_${att_index}">
												<span title="点击下载该附件" onclick="viewOrDownLoad('${rc.getContextPath()}/upFileServlet?method=down&attachmentId=${att.attachmentId}');" class="wl_upload">
													<img src="${rc.getContextPath()}/ui/workorder/img/icon_wl_add_word.png" />
													<p>[处理前]${att.fileName}</p>
												</span>
												<div data-id="${att.attachmentId}" class="off_btn displ dn"></div>
											</div>
											</#if>
					    				</#list>
									</div>
								</div>
							</ul>
							<div class=" dotted-line"></div>
						</div>
					</div>
				</div>
				<div class="btn-warp">
					<a class="btn-bon" style="background: #f54952;" onclick="tableSubmit('saveEventAndReportDispute', '3'<#if callBack??>, '${callBack}'</#if>);" style="background: #fff;">提交</a>
					<a class="btn-bon btn-cancel" onclick="cancel();" style="background: #fff;">取消</a>
				</div>
			</div>
		</div>
	</body>
	<script src="${rc.getContextPath()}/dispute/jquery-nicescroll/jquery.nicescroll.js" type="text/javascript" charset="utf-8"></script> 
	<script src="${rc.getContextPath()}/ui/workorder/js/ajaxupload.js" type="text/javascript" charset="utf-8"></script>
	<script src="${rc.getContextPath()}/ui/workorder/js/xinj.workorder.common.js?v_=201806072033" type="text/javascript" charset="utf-8"></script>
	<script src="${rc.getContextPath()}/ui/workorder/js/xinj.workorder.create.js?v_=201806072034" type="text/javascript" charset="utf-8"></script>
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	<#include "/component/customEasyWin.ftl" />
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/zzgl/dispute/mediation_9x/map/maxJqueryEasyUIWin.ftl" />
	<script type="text/javascript">
		var $winH, $topH, $btnH;
        $(window).on('load resize', function () {
            $winH = $(window).height();
            $topH = $('.fw-toptitle').height();
            $btnH = $('.btn-warp').height();
            $('.fw-main').height($winH - $topH - $btnH - 76-36);
            $(".fw-main").niceScroll({
                cursorcolor:"rgba(0, 0, 0, 0.3)",
                cursoropacitymax:1,
                touchbehavior:false,
                cursorwidth:"4px",
                cursorborder:"0",
                cursorborderradius:"4px"
            });
        });
	</script>
	
	<script type="text/javascript">
	var type = "${event.type!}";
	var _winHeight = 0;
	var _winWidth = 0;
	
	$(function(){
		getMapMarkerData();
		//var global_index = $('#global_index').val();
		var isUploadHandlingPic = <#if isUploadHandlingPic??>${isUploadHandlingPic?c}</#if>;
		var radioList = [{'name':'处理前', 'value':'1'},{'name':'处理后', 'value':'3'}];
		var eventSeq = "1,3";
		
		_winHeight = $(window).height();
		_winWidth = $(window).width();
		
		var options = { 
            axis : "yx", 
            theme : "minimal-dark" 
        }; 
        enableScrollBar('content-d',options);
        
        $("#norFormDiv").width($(window).width());
        
		if(isUploadHandlingPic){
			eventSeq = "1,2,3";
			radioList = [{'name':'处理前', 'value':'1'},{'name':'处理中', 'value':'2'},{'name':'处理后', 'value':'3'}];
	   	}
		
		var typesDictCode = "${typesDictCode!}";
		if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			AnoleApi.initTreeComboBox("typeName", "type", { 
				"${bigTypePcode!}" : [${typesDictCode!}] 
			}, null, null, {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>"
			});
		} else {
			AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}");
		}
		
		<#if rootGridId?? && (rootGridId > 0)>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#gridCode").val(grid.orgCode);
				}
			}, {
				Async : {
					enable : true,
					autoParam : [ "id=gridId" ],
					dataFilter : _filter,
					otherParam : {
						"startGridId" : ${rootGridId?c}
					}
				}
			});
		<#else>
			AnoleApi.initGridZtreeComboBox("gridName", "gridId", function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#gridCode").val(grid.orgCode);
				}
			});
		</#if>
		
		AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "${influenceDegreePcode}", null, [<#if event.influenceDegree??>"${event.influenceDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "${urgencyDegreePcode}", null, [<#if event.urgencyDegree??>"${event.urgencyDegree}"<#else>"01"</#if>]);
		AnoleApi.initListComboBox("involvedNumName", "involvedNum", "${involvedNumPcode}", null, [<#if event.involvedNum??>"${event.involvedNum}"<#else>"00"</#if>]);
		AnoleApi.initListComboBox("sourceName", "source", "${sourcePcode}", null, [<#if event.source??>"${event.source}"<#else>"01"</#if>]);
		
		<#if event.resMarker??>
			var resMarkerX = "${event.resMarker.x!}",
				resMarkerY = "${event.resMarker.y!}",
				resMarkerMapType = "${event.resMarker.mapType!}";
			
			if(resMarkerX && resMarkerY && resMarkerMapType) {
				callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
			}
		</#if>
	});
	
	function involedPeopleCallback(users) {
		if(users == ""){
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
			return;
		}
		var usersDiv = "";
		var userNames = "";
		var userArray = users.split("；");
		if(userArray != ""){
			$.each(userArray, function(i, n){
				var items = n.split("，");
				if(typeof(items[1])!="undefined" ){
					var userName = items[1];
					if(userName.length > 3){//名字显示前三个字
						userName = userName.substr(0, 3);
					}
					usersDiv += "<p title="+items[1]+"("+items[2]+")>"+userName+"<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople(\""+items[1]+"\",\""+items[2]+"\", $(this).parent());'/></p>";
					userNames += items[1] + "，";
				}
			});
			
			userNames = userNames.substr(0, userNames.length - 1);
			$("#involvedPeopleName").html(usersDiv);//用于页面显示
			$("#eventInvolvedPeople").val(users);//用于后台保存
			$("#involvedPersion").val(userNames);
		}else{
			$("#involvedPeopleName").html("");//用于页面显示
			$("#eventInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
		}
	}
	
	//btnType：0 保存；1 提交；2 结案；3 上报；
	function tableSubmit(m, btnType, callback, toClose){
		var isValid =  $("#tableForm").form('validate');
		var advice = $("#advice").val();
		var isAdviceVisible = $("#adviceDiv").is(":visible");
		if(!type) {
			type = $("#type").val();
		}
		if(isValid && isAdviceVisible && advice=="") {
			$.messager.alert('警告','请填写办理意见！','warning');
		} else if(isValid && toClose=="1" && !isAdviceVisible) {
			openAdviceDiv();
		} else if(isValid){
			var msg = "添加";
			<#if event.eventId??>
				m = "editEvent";
				msg = "更新";
			</#if>
			<#if (isReport?? && isReport)>
				msg = "上报";
			</#if>
			
			if(isBlankString(toClose)){
				toClose = "";
			} else if(toClose == "1") {
				$("#result").val($("#advice").val());
			}
			
			if(isNotBlankStringTrim(callback)){
				<#if !(isReport?? && isReport)>
					m += "AndStart";
				</#if>
			}
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);
	      	
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		data.btnType = btnType;//设置按钮类型
		  		if(data.eventId){
		  			var iframeUrl = "${iframeUrl!}",
		  				iframeCallBack = "${callBack!}";
	  				if(iframeUrl && iframeCallBack){//跨域回调
	  					if(iframeUrl.indexOf('?') != -1){
							iframeUrl += "&";
						}else{
							iframeUrl += "?";
						}
						
						data.isCrossDomain = true;
						return
						iframeUrl += "callBack=" + iframeCallBack + "&callBackParams="+ encodeURIComponent(JSON.stringify(data));
						$("#crossOverIframe").attr("src", iframeUrl);
	  				}else if(callback!=undefined && callback!=null && callback!=""){//本项目回调
		  				eval(callback)(data);
		  			}else{ 
		  				if(data.result){
		  					msg += "成功";
		  				}else{
		  					msg += "失败";
		  				}
		  				if(data.result == true){
		  					parent.reloadDataForSubPage(msg,data.type);
		  				}else{
		  					$.messager.alert('提示', '上报失败，请联系系统管理员！','error');
		  					modleclose();
		  				}
		  			}
				}else{
					modleclose();
					$.messager.alert('错误','连接超时，请重试！', 'error');
				}
			});
	  	}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
		/* debugger
		var closeCallBack = "${iframeCloseCallBack!}",
			iframeUrl = "${iframeUrl!}";
		
		if(iframeUrl && closeCallBack){
			if(iframeUrl.indexOf('?') != -1){
				iframeUrl += "&";
			}else{
				iframeUrl += "?";
			}
			
			iframeUrl += "callBack=" + closeCallBack;
			$("#crossOverIframe").attr("src", iframeUrl);
		}else{
			parent.closeMaxJqueryWindow();
		} */
	}
	
	function openAdviceDiv() {
		$('.PopDiv').css({'top':_winHeight/4});
		$("#operateMask").show();
		$("#adviceDiv").show();
		$("#advice").focus();
	}
	
	function closeAdviceDiv() {
		$("#adviceDiv").hide();
		$("#operateMask").hide();
		$("#advice").val("");
	}
	
	function closed(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			modleopen();
			var eventId = $("#eventId").val();
			if(isNotBlankStringTrim(eventId)){
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/startWorkFlow.jhtml',
					data: 'eventId='+ eventId +'&toClose=1',
					dataType:"json",
					success: function(data){
						if(parent.startWorkFlow != undefined){
				   			parent.startWorkFlow(data);
				   		}else{
				   			$.messager.alert('错误','连接错误！','error');
				   		}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
			}else{
				tableSubmit('saveEvent', '2', parent.startWorkFlow, "1");
			}
		}
	}
	
	/*
		地图标注改造 2015-06-29，参考geo项目行政区划地图标注
		markerOperation:地图标注操作类型
						 0表示添加
						 1表示编辑
						 2表示查看
		isEdit:是否是编辑状态
	*/
	
	function showMap(){
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 480;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = true;
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	function getParameterJson() {
		var parameterJson={
			"id":$("#id").val(),
			"name":$("#name").val()
		}
		return parameterJson;
	}
	$(window).resize(function(){
		var winHeight = $(window).height();
		var winWidth = $(window).width();
		
		if(winHeight != _winHeight || winWidth != _winWidth) {
			location.reload();
		}
    });
	
	
	function getMapMarkerData(){
		var id = $("#id").val(); // 业务id
		var module = $("#module").val(); // 模块
		var markerOperation = $("#markerOperation").val(); // 地图操作类型
		//var markerOperation = 1; // 地图操作类型
		var showName = "标注地理位置";
		$(".mapTab2").addClass("mapTab2");
		$.ajax({   
			 url: '${rc.getContextPath()}/zhsq/map/gis/getMapMarkerData.json?id='+id+'&module='+module+'&t='+Math.random(),
			 type: 'POST',
			 timeout: 3000,
			 dataType:"json",
			 async: false,
			 error: function(data){
			 	$.messager.alert('友情提示','地址库信息获取出现异常!','warning'); 
			 },
			 success: function(data){
			 	if (data != null) {
				 	if (markerOperation == 0 || markerOperation == 1) { // 添加标注
						if (data.x != "" && data.x != null) {
							showName = "修改地理位置";
						} else {
							showName = "标注地理位置";
						}
					} else if (markerOperation == 2) { // 查看标注
                        if (data.x != "" && data.x != null) {
                            showName = "查看地理位置";
                        } else {
                            showName = "未标注地理位置";
                        }
					}

				 	if (data.x != "" && data.x != null) {
				 		$("#x").val(data.x);
				 	} else {
				 		$("#x").val("");
				 	}
				 	
				 	if (data.y != "" && data.y != null) {
				 		$("#y").val(data.y);
				 	} else {
				 		$("#y").val("");
				 	}
				 
			 		if (data.mapt != "" && data.mapt != null) {
				 		$("#mapt").val(data.mapt);
				 	}
                    try{
                        callbackMap(data.x,data.y);
                    }catch(e){

                    }
			 	}else{
                    showName = "未标注地理位置";
				}
				$("#mapTab2").html(showName);
			 }
		});
	}
</script>
</html>
