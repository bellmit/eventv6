<!DOCTYPE html>
<html style="overflow:hidden">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><@block name="pageTitle">事件采集</@block></title>
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/zhsq_event.css" />
	<#include "/component/commonFiles-1.1.ftl" />
	
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/common/css/reset.css">
    <link rel="stylesheet" href="${uiDomain!''}/web-assets/_big-screen/nanchang-cc/css/index-mask.css">	
	<script type="text/javascript" src="${rc.getContextPath()}/js/plugIn/plug_in.js"></script>
	<script type="text/javascript" src="${COMPONENTS_URL}/js/fastreply/fastReply.js" defer="true"></script>
<!--    add by wuzhj 新版附件上传 样式 start     -->
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
	<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
	<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
	
	<!--    add by wuzhj 新版附件上传 样式 end     -->
<@block name="extraJs"></@block>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<style type="text/css">
	.LabName{height:30px;}
	.ImgUpLoad .upload_table {
	 width:70%;
	}
	.LabName{
	color:  #b2cdff;
	}
	.BigTool{
	background: transparent;
	}
	.BigShangBaoBtn{
		background-color: rgba(0, 160, 233, 0.4);
		color: #4ce7ff;
	}
	td{
	    border-bottom:none !important;   
	}
	
.inpt1_bg {
    background-color: #084bae !important;
    border-radius: 0.04rem ;
    border: solid 1px #2e7bec;
    color: #fff !important;
}
.ztree * {
    padding: 0;
    margin: 0;
    font-size: 12px;
    font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
    color: #fff !important;
}	
.textbox .textbox-text {
    background-color: #084bae;
    transition: all 0.5s;
    color: #fff; 
  
} 
.combo {
    background-color: #084bae !important;
    border: solid 1px #2e7bec;   
    color: #fff !important;
    transition: border 0.5s ease 0s;
}
.vv{
    width: 1.2rem;
    height: 0.38rem;
    line-height: 0.38rem;
    color: #4ce7ff;
    background-color: rgba(0, 160, 233, 0.2);
    border-radius: 0.19rem;
    font-size: 0.18rem;
    text-align: center;
    float: left;
    margin: 0.1rem;
    padding: 0 12px 0 35px;
    cursor: pointer;	
    }
    .BigNorToolBtn{
    display: inline-block;
    float: left;
    height: 32px;
    margin: 0 10px;
    padding: 0 12px 0 35px;
    font-family: Microsoft YaHei;
    font-size: 18px;
    line-height: 32px;
    color: #fff;
    text-align: center;
    border-radius: 3px;
    background-repeat: no-repeat;
    background-color: #2980B9;
    background-position: 12px 8px;
    transition: all 0.5s;
    }
.messager-body{margin-top:1.5rem}    


</style>
<#include "/component/ComboBox.ftl" />
</head>
<body style="background: transparent;">
<div>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
	    <input type="hidden" id="attachmentIds" name="attachmentIds" value="" />
		<input type="hidden" id="gridId" name="gridId" value="<#if event.gridId??>${event.gridId?c}</#if>">
		<input type="hidden" id="gridCode" name="gridCode" value="${event.gridCode!}">
		<input type="hidden" id="type" name="type" value="${event.type!}" />
		<input type="hidden" id="code" name="code" value="${event.code!}" />
		<input type="hidden" id="eventId" name="eventId" value="<#if event.eventId??>${event.eventId?c}</#if>" />
		<!--办理意见-->
		<input type="hidden" id="result" name="result" value="${event.result!}" />
		<!--事件对接业务平台-->
		<input type="hidden" name="bizPlatform" value="${event.bizPlatform!}" />
		
		<!--用于地图-->
		<input type="hidden" id="id" name="id" value="<#if event.eventId??>${event.eventId?c}</#if>" /> 
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
		<!--事件上报关联模块-->
		<input type="hidden" name="eventReportRecordInfo.bizId" value="<#if event.eventReportRecordInfo?? && event.eventReportRecordInfo.bizId??>${event.eventReportRecordInfo.bizId?c}</#if>" />
		<input type="hidden" name="eventReportRecordInfo.bizType" value="<#if event.eventReportRecordInfo??>${event.eventReportRecordInfo.bizType!}</#if>" />
		<input type="hidden" id="bizId" name="bizId" value="<#if bizId??>${bizId!}</#if>" />
		
		
 		<div style="margin: 0 auto; position:relative;">
			<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
				<div id="norFormDiv" class="NorForm" style="width:784px;">
					<div id="tableAddDiv" class="fl" >
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						  <tr>
								<td class="LeftTd">
									<label class="LabName"><span><label class="Asterik">*</label>事件分类：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox inpt1_bg" style="width:10.95rem;" data-options="required:true" id="typeName" name="typeName" maxlength="100" value="<#if event.typeName??>${event.typeName}<#else><#if event.eventClass??>${event.eventClass}</#if></#if>" />
								</td>
								<td>
									<label class="LabName"><span>所属网格：</span></label><input type="text" class="inp1 InpDisable easyui-validatebox inpt1_bg" style="width:10.95rem;" data-options="required:true" id="gridName" name="gridName" value="<#if event.gridName??>${event.gridName}</#if>" />
								</td>
							</tr>
							<tr>
					    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>事件标题：</span></label><input type="text" class="inp1 easyui-validatebox inpt1_bg" style="width:28.5rem;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" name="eventName" id="eventName" value="<#if event.eventName??>${event.eventName}</#if>" />
				    			</td>
					    	</tr>
							<tr>
					    		<td colspan="2" class="LeftTd">
				    			<label class="LabName"><span><label class="Asterik">*</label>详细地址：</span></label><input type="text" class="inp1 easyui-validatebox inpt1_bg" style="width:28.5rem;" data-options="required:true,tipPosition:'bottom',validType:['maxLength[255]','characterCheck']" name="occurred" id="occurred" value="<#if event.occurred??>${event.occurred}</#if>" />
				    		</td>
					    	</tr>
							<tr>
								<td   class="LeftTd"  >
				    			<label class="LabName"><span>地理标注：</span></label>
				    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
				        		</td>
						
					    		<td><label class="LabName"><span><label class="Asterik">*</label>责任点位：</span></label>
						 	    	<input type="hidden" id="pointSelection" name="pointSelection" value="<#if pointSelection??>${pointSelection}</#if>">
						 	    	<input type="text" class="inp1 InpDisable easyui-validatebox inpt1_bg" style="height:30px;width:112px;" id="pointSelectionName" data-options="required:true,tipPosition:'bottom',prompt:'请选择'" editable="false"  value="<#if pointSelectionName??>${pointSelectionName}</#if>" />
						  		</td>
					    		
					    		
					    		
					    	</tr>
					    	
					    	<tr>
						  <td  class="LeftTd"  >
					    		<label class="LabName"><span><label class="Asterik">*</label>事发时间：</span></label><input type="text" id="happenTimeStr" name="happenTimeStr" style="width: 10.95rem" class="inp1 Wdate easyui-validatebox inpt1_bg" cursor:pointer;" data-options="required:true" onclick="WdatePicker({readOnly:true, maxDate:'${(event.happenTimeStr!maxHappenTime)?substring(0,10)} 23:59:59', dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})" value="${event.happenTimeStr!}" readonly="readonly"></input>
					      </td>
					      
					      <td>
							<label class="LabName"><span><label class="Asterik">*</label>巡防类型：</span></label>
							<input type="hidden" id="patrolType" name="patrolType" <#if patrolType??>value="${patrolType}"</#if> />
							<input type="text" class="inp1 easyui-validatebox inpt1_bg" style="width:10.95rem;" data-options="required:true,tipPosition:'bottom'" id="patrolTypeName" value="" />
						  </td>
					      
					    </tr>
					    
					    	
					    	<tr>
					    		<td colspan="2" class="LeftTd" style="border-bottom:none;">
					    			<label class="LabName"><span><label class="Asterik">*</label>事件描述：</span></label><textarea name="content" id="content" cols="" rows="" class="area1 easyui-validatebox inpt1_bg" style="width:28.5rem; height:64px;resize: none;" data-options="required:true,tipPosition:'bottom',validType:[<#if characterLimit??>'minLength[${characterLimit?c}]',</#if>'maxLength[1024]','characterCheck']" ><#if event.content??>${event.content}</#if></textarea>
						        </td>
					    	</tr>
					    	    	<tr>
				    		<td>
					    		<label class="LabName"><span><label class="Asterik">*</label>影响范围：</span></label>
								<input type="hidden" id="influenceDegree" name="influenceDegree" value="<#if event.influenceDegree??>${event.influenceDegree}</#if>" />
								<input type="text" class="inp1 easyui-validatebox inpt1_bg"  style="width: 10.95rem" data-options="required:true,tipPosition:'bottom'" id="influenceDegreeName" value="<#if event.influenceDegreeName??>${event.influenceDegreeName}</#if>" />
							</td>
				     
					        <td>
						        <label class="LabName"><span><label class="Asterik">*</label>信息来源：</span></label>
						        <input type="hidden" id="source" name="source" value="<#if event.source??>${event.source}</#if>" />
								<input type="text" class="inp1 easyui-validatebox inpt1_bg"  style="width: 10.95rem" data-options="required:true,tipPosition:'bottom'" id="sourceName" value="<#if event.sourceName??>${event.sourceName}</#if>" />
				    		</td>
				        </tr>
				    	<tr>
				    		<td>
					    		<label class="LabName"><span><label class="Asterik">*</label>紧急程度：</span></label>
					    		<input type="hidden" id="urgencyDegree" name="urgencyDegree" value="<#if event.urgencyDegree??>${event.urgencyDegree}</#if>" />
								<input type="text" class="inp1 easyui-validatebox inpt1_bg"  style="width: 10.95rem" data-options="required:true,tipPosition:'bottom'" id="urgencyDegreeName" value="<#if event.urgencyDegreeName??>${event.urgencyDegreeName}</#if>" />
				    		</td>
				  
				    		<td>
					    		<label class="LabName"><span><label class="Asterik">*</label>涉及人数：</span></label>
					    		<input type="hidden" id="involvedNum" name="involvedNum" value="<#if event.involvedNum??>${event.involvedNum}</#if>" />
								<input type="text" class="inp1 easyui-validatebox inpt1_bg"  style="width: 10.95rem" data-options="required:true,tipPosition:'bottom'" id="involvedNumName" value="<#if event.involvedNumName??>${event.involvedNumName}</#if>" />
							</td>
				    	</tr>
					    <tr>
				    		<td class="LeftTd" style="border-bottom:none;">
				    			<label class="LabName"><span><label class="Asterik">*</label>联系人员：</span></label>
				    			<input  id="contactUser" name="contactUser" type="text" style="width:10.95rem" class="inp1 easyui-validatebox inpt1_bg" data-options="required:true,tipPosition:'bottom',validType:['maxLength[30]','characterCheck']" value="<#if event.contactUser??>${event.contactUser}</#if>" />
				    		</td>
				    		<td class="LeftTd" style="border-bottom:none;">
				    			<label class="LabName"><span><label class="Asterik">*</label>联系电话：</span></label>
				    			<input name="tel" id="tel" type="text" style="width:10.95rem" class="inp1 easyui-validatebox inpt1_bg" data-options="required:true,tipPosition:'bottom',validType:'mobileorphone'" value="<#if event.tel??>${event.tel}</#if>" />
				    		</td>
				    	</tr>
						
					    	
					    	
					    	<tr>
					    		<td class="LeftTd" style="border-bottom:none;"  colspan="2">
					    			<label class="LabName"><span>附件：</span></label>
						      	    <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;color:white"></div>
						        </td>
					    	</tr>
					    </table>
					</div>
					

				</div>
			</div>
			
		</div>  
	</form>
</div>
	<iframe id="crossOverIframe" name="crossOverIframe" src="" style="display:none;" ></iframe>
	<#include "/component/involvedPeopleSelector.ftl">
	<#include "/component/maxJqueryEasyUIWin.ftl" />
</body>

<script type="text/javascript">
	var u = navigator.userAgent, app = navigator.appVersion;   
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    var type = "${event.type!}";
    var eventTreeApi;
	
	var base = '${rc.getContextPath()}';
	var imgDomain = '${IMG_URL}';
	var uiDomain = '${uiDomain}';
	var skyDomain = '${SQ_SKY_URL}';
	var componentsDomain = '${COMPONENTS_URL}';//公共组件工程域名
	var bigupload = null;
	
	
	$(function() {
		var options = { 
	            axis : "y", 
	            theme : "minimal-dark" 
	        }; 
	    enableScrollBar('content-d',options);
	    if(isAndroid){ 
	    //if(navigator.userAgent.match("ua_ffcs")){
			$('#tableAddDiv').css('margin-left','0rem');			
		}else{
			$('#tableAddDiv').css('margin-left','6.0rem');			
		}
		
		
		$("#fileupload").bigfileUpload({
			useType: 'edit',//附件上传的使用类型，add,edit,view，（默认edit）;
			chunkSize : 5,//切片的大小（默认5M）
			fileNumLimit : 10,//最大上传的文件数量（默认9）
			maxSingleFileSize:50,//单个文件最大值（默认300）,单位M
			fileExt : '.jpg,.png,.gif,.doc,.xls,.docx,.xlsx,.ppt,.pptx,.mp4,.m4a,.pdf,.txt',//支持上传的文件后缀名(默认值请查看参数说明）
			//initFileArr : [<#if (attr?? && attr)>{'attachmentId':'${attr.attachmentId}','path':'${attr.filePath}','fileName':'${attr.fileName}'}</#if>],//初始化的附件对象数组(默认为{})
			attachmentData:{bizId:$("#bizId").val(),attachmentType:'ZHSQ_EVENT_WECHAT'},
			appcode:"sqfile",//文件所属的应用代码（默认值components）
			module:"attachment",//文件所属的模块代码（默认值bigfile）
			imgDomain : imgDomain,//图片服务器域名
			uiDomain : uiDomain,//公共样式域名
			componentsDomain : componentsDomain,//公共组件域名
			isSaveDB : true,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
			isDelDbData:true,//是否删除数据库数据(默认true)
			isUseLabel: true,//是否使用附件标签(默认false)
			//labelDict : [{'name':'处理前','value':'1'},{'name':'处理中','value':'2'},{'name':'处理后','value':'3'}],
			//isUseSetText: true,//是否使用设置功能(默认false)
			//setdefutext: "设置文本",//设置功能默认文本(默认“设为主图”)
			setCallback:function(obj){//点击设置标签的回到事件
				$("."+obj.attr("span-class")).html("设为主图");
				obj.find("."+obj.attr("span-class")).html("主图");
			},
			styleType:"box",//块状样式：box,列表样式：list,自定义样式：self
		});
		
        var typesDictCode = "${typesDictCode!}";
		if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			eventTreeApi=AnoleApi.initTreeComboBox("typeName", "type", { 
				"${bigTypePcode!}" : [${typesDictCode!}] 
			}, null, null, {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>"
			});
		} else {
			eventTreeApi=AnoleApi.initTreeComboBox("typeName", "type", "${bigTypePcode!}");
		}
        
	   var searchParams={x:0.0,y:0.0,limits:500};
	   
	   $('#pointSelectionName').combogrid({    
           panelWidth:400,    
           //idField:'pointId', 
           value:'<#if pointSelectionName??>${pointSelectionName}</#if>',   
           textField:'pointName', 
           rownumbers:true, 
           loadMsg: '正在加载责任点位信息',  
           url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/findResposibilityPoints.json',
           queryParams:searchParams,    
           columns:[[
               {field:'pointId',title:'pointId', align:'center',hidden:true},    
               {field:'pointName',title:'点位名称',align:'center',width:345}    
               //{field:'pointManager',title:'联系人',align:'center',width:140},    
               //{field:'pmTel',title:'电话',align:'center',width:140}  
           ]],
           onShowPanel:function(){
               x_=$("#x").val();
               y_=$("#y").val();
               //console.log(x_);
               //console.log(y_);
               if(x_==""||y_==""){
                   //console.log($("#gridId").val());
                   var grid_=$("#gridId").val();
                   $.ajax({
					   type: "POST",
					   async: false,
		    	   	   url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/findGridCenter.json',
					   data: 'gridId='+grid_,
					   dataType:"json",
					   success: function(data){
						   if(data.length>0){
						       searchParams.x=data[0].x;
						       searchParams.y=data[0].y;
						       $("#x").val(data[0].x);
						       $("#y").val(data[0].y);
						   }
					   }
		    	   });
               }else{
                   searchParams.x=x_;
                   searchParams.y=y_;
               }

               $('#pointSelectionName').combogrid('grid').datagrid('load');
           },
           onSelect: function (rowIndex, rowData){
               x_=$("#x").val();
               y_=$("#y").val();
               $.ajax({
					type: "POST",
					async: false,
		    	   	url : '${rc.getContextPath()}/zhsq/event/eventDisposalController/searchResposibilityPointInfo.json',
					data: {'x':x_,'y':y_,'pointId':rowData.pointId},
				    dataType:"json",
					success: function(data){
					    //console.log(data);   
					    if(data.msg){
                            $.messager.alert('提示',data.msg+'请重新选择地理位置或手动选择所属网格!','info');
                        }else{
                        	if(isNotBlankString(data.gridCode)){
				            	$('#gridCode').val(data.gridCode); 
                        	}
                        	if(isNotBlankString(data.gridName)){
                        		$('#gridName').val(data.gridName);
                        	}
                        	if(isNotBlankString(data.gridId)){
                        		$("#gridId").val(data.gridId);
                        	}
                        } 
                        $('#contactUser').val(data.pointManager);
                        $('#tel').val(data.pmTel);
                        if(data.eventType!=null&&data.eventType!=''){
                        
                            eventTreeApi.setSelectedNodes([data.eventType]);
		                
		                }
					}
		       });
               $('#pointSelection').val(rowData.pointId);
               $('#pointSelectionName').val(rowData.pointName);
           },

       });
        
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
		AnoleApi.initTreeComboBox("patrolTypeName", "patrolType", "A001093092", null, [<#if patrolType??>"${patrolType}"<#else>"0"</#if>]);
		<@block name="initExpandScript"></@block>
        
        <#if event.resMarker??>
        	var resMarkerX = "${event.resMarker.x!}",
        		resMarkerY = "${event.resMarker.y!}",
        		resMarkerMapType = "${event.resMarker.mapType!}";
        		
        	if(resMarkerX && resMarkerY && resMarkerMapType) {
        		callBackOfData(resMarkerX, resMarkerY, null, resMarkerMapType);
        	}
        </#if>
		
	});
	
	function showAdvice(m, callback, toClose) {//展示办理意见
		if(toClose && toClose == '1') {
			$('#advice').validatebox({
				required: true
			});
			
			$("#adviceTr").show();
			
			closed();
		} else {
			$("#adviceTr").hide();
			
			$('#advice').validatebox({
				required: false
			});
			
			$('#advice').val("");
			
			tableSubmit(m, callback, toClose);
		}
	}
		
	function tableSubmit(m, callback, toClose) {
		var isValid =  $("#tableForm").form('validate'),
			advice = $("#advice").val(),
			isAdviceVisible = $("#adviceDiv").is(":visible");
		
		<@block name="attachmentCheck"></@block>
		if(isValid) {
			var type = $("#type").val();
			
			if(isBlankParam(toClose)){
				toClose = "";
			} else if(toClose == "1") {
				$("#result").val($("#advice").val());
			}
			
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/event/eventDisposalController/"+m+"/"+type+".jhtml?toClose="+toClose);
			
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
		  		if(data.eventId) {
	  				var iframeUrl = "${iframeUrl!}",
						outerCallBack = "${callBack!}";
					
					 if(callback) {//为了保证提交、上报、结案能正常操作
						startWorkFlow(data);
					} else if(iframeUrl && outerCallBack){//跨域回调
						if(iframeUrl.indexOf('?') != -1) {
							iframeUrl += "&";
						} else {
							iframeUrl += "?";
						}
						
						data.isCrossDomain = true;
						iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
						$("#crossOverIframe").attr("src", iframeUrl);
					} else if(outerCallBack) {//本域回调
						startWorkFlow(data);
					} else {//自身调用回调
						var msg = "添加";
						<#if event.eventId??>
							m = "editEvent";
							msg = "更新";
						</#if>
						<#if (isReport?? && isReport)>
							msg = "上报";
						</#if>
						
						if(data.result){
		  					msg += "成功";
		  				}else{
		  					msg += "失败";
		  				}
				
						flashData(msg, data.type);
					}
				} else {
					modleclose();
					$.messager.alert('错误', '保存事件失败，请重试！', 'error');
				}
			});
	  	}
	}
	
	
	function startWorkFlow(data) {//启动流程
		var formId = data.formId,
			new_workFlowId = data.workflowId,
			wftypeId = data.wftypeId,
			orgCode = data.orgCode,
			orgType = data.orgType,
			toClose = data.toClose,
			advice = data.advice;
		
		//启动流程
		$.ajax({
			//type: "POST",
			url : '${rc.getContextPath()}/zhsq/workflow/workflowController/startFlow.jhtml',
			data: {'formId': formId ,'workFlowId': new_workFlowId,'wftypeId': wftypeId, 'orgCode': orgCode, 'orgType': orgType, 'toClose': toClose, 'advice': advice},
			dataType:"json",
			success: function(data) {
				modleclose();
			    if(data.result){
			    	var instanceId = data.instanceId;
			    	if(isNotBlankString(instanceId)) {
			    		var iframeUrl = "${iframeUrl!}",
							outerCallBack = "${callBack!}";
						
						if(iframeUrl && outerCallBack) {//跨域回调
							if(iframeUrl.indexOf('?') != -1) {
								iframeUrl += "&";
							} else {
								iframeUrl += "?";
							}
							
							data.isCrossDomain = true;
							data.eventId = formId;
							
							iframeUrl += "callBack=" + outerCallBack + "&callBackParams="+ JSON.stringify(data);
							$("#crossOverIframe").attr("src", iframeUrl);
						} else {
							var url = "${rc.getContextPath()}/zhsq/event/eventDisposalController/detailByVideo.jhtml?eventType=todo&instanceId="+instanceId+"&workFlowId="+new_workFlowId+"&eventId="+formId+"&cachenum=" + Math.random();
							if(outerCallBack) {
								$.messager.alert('', "上报成功", 'info',function(){
										modleclose();
										getDetailEvent(formId,new_workFlowId);
									});
							} else {
								if(toClose == '0') {
								//	window.location.href = url;
										$.messager.alert('', "上报成功", 'info',function(){
										modleclose();
										getDetailEvent(formId,new_workFlowId);
									});
									//showMaxJqueryWindow("事件办理", url, null, null, null, null, flashData);
								} else if(toClose == '1') {
									try{
			    						closeMaxJqueryWindow();//新增弹出窗口的关闭方法
			    					}catch(e){}
			    					$.messager.alert('','事件结案成功！','info',function() {
			    						flashData();
			    					});
								}
							}
						}
			    	}
			    } else {
			    	var msg = data.msgWrong || "事件启动失败！";
			    	
			    	$.messager.alert('错误',msg,'error', function() {
			    		location.reload();
			    	});
			    }
			},
			error:function(data) {
				$.messager.alert('错误','事件流程未启动成功！','error');
			}
		});
	}
	
	function showMap(){//地图展示
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = 360;
		var height = 360;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var id = $('#eventId').val();
		var mapType = 'EVENT_V1';
		var isEdit = true;
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
	}
	
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
	
	function flashData(msg) {
		$("#tableForm").attr("action",'${rc.getContextPath()}/zhsq/event/eventDisposalController/toAddEventByMenu.jhtml?eventJson={isReport:false}');
		if(isNotBlankString(msg)) {
			$.messager.alert('', msg, 'info',function(){
				$("#tableForm").submit();
			});
		} else {
			$("#tableForm").submit();
		}
	}
	/**
	 * 添加图片列表
	 * @param result
	 * 			isFromQueued	//true表示文件来自队列，否则表示文件来自外部
	 * @param type
	 */
	function addImgList(result, type) {
		var showPattern = result.showPattern;
		var imgStr = "bmp,doc,docx,txt,xls,tif,rtf,xml,ppt,ppx,png,pdf,jpg,gif,amr,mp3";
		var suffix = getFileSuffix(result.fileName);
		if(imgStr.indexOf(suffix)<0){
			suffix = "epub";
		}
		
		var row = new StringBuffer();
		var eventSeqName = capEventSeqName(result);

		if(showPattern == 'list' || showPattern == 'all') {
			row.append('<tr ');
		} else {
			row.append('<tr class="hide" ');
		}
		row.append(' id="').append(result.fileId).append('" type="').append(result.type)
		   .append('" init_type="').append(result.initType).append('"')
		   .append(' path="').append(result.filePath+'">');
		row.append('<td style="width: 30px;" align="center">');
		row.append('<img style="margin-top:3px;" src="').append(default_config.script_path).append('images/icon/icon_').append(suffix).append('.gif" width="16" height="16" />');
		row.append('</td>');
		row.append('<td align="left">');
		if(eventSeqName!=undefined && eventSeqName!=""){
			row.append('<label class="FontDarkBlue">').append('['+eventSeqName+']').append('</label>');
		}
		row.append(displayFileName(result.fileName)).append('</td>');
		//row.append('<td width="50" align="center">').append(result.fileSize).append('</td>');
		row.append('<td align="center">');
		
		if(!result.isFromQueued) {//为了让传递attachmentId的附件也能正常关联上业务id
			var fileType = 'fileType';
			row.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(result.fileId).append("'/>");
			row.append("<input type='hidden' id='filePath' name='filePath' value='").append(result.filePath).append("'/>");
			row.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
			row.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(result.fileSize)).append("'/>");
		}
		row.append('</td>');
		row.append('<td style="width: 100px;" align="center">');
		if(result.type != 'detail' && type==null){//文件详情无删除图片
			row.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
			   .append('style="cursor:pointer;vertical-align:middle;"')
			   .append(' onclick="deleteFile(').append(result.fileId).append(',\'').append(result.cancel_button).append('\',\'').append(result.table).append('\');" alt="删除文件" title="删除"/>&nbsp;');

		} 
		if('add' != result.type){
			row.append('<a href="').append(result.download_url).append('&attachmentId=').append(result.fileId).append('" target="_blank" >')
			   .append('<img src="').append(default_config.script_path).append('images/icon/down.png" width="16" height="16"')
			   .append(' style="vertical-align:middle;" alt="查看文件" title="查看" /></a>');		 
		}
		row.append('</td>');
		row.append('</tr>');
		
		$("#"+result.table).append(row.toString());
		$("#"+result.table).removeClass("hide");
	}

	/**
	 * 文件上传成功
	 * @param file
	 * @param serverData 服务端返回的数据
	 */
	function uploadSuccess(file, serverData) {
		
		try {
			var data = $.parseJSON(serverData);
			file.attachmentId = data.attachmentId;
			file.filePath = data.filePath;
			
			var row = document.getElementById(file.id);
			var _fileState = new StringBuffer();
			
			var cancel_button = this.customSettings.cancel_upload;
			var upload_table = this.customSettings.upload_table;
			
			var fileType = this.customSettings.fileType;
			_fileState.append("<input type='hidden' id='attachmentId' name='attachmentId' value='").append(file.attachmentId).append("'/>");
			_fileState.append("<input type='hidden' id='filePath' name='filePath' value='").append(file.filePath).append("'/>");
			_fileState.append("<input type='hidden' id='fileType' name='fileType' value='").append(fileType).append("'/>");
			_fileState.append("<input type='hidden' id='fileSize' name='fileSize' value='").append(formatFileSize(file.size)).append("'/>");
			//_fileState.append("文件上传完成");
		

			_fileState.append('<img src="').append(default_config.script_path).append('images/icon/folder_del.gif" width="16" height="16" ')
			   .append('style="cursor:pointer;vertical-align:middle;"')
			   .append('id=img_').append(file.attachmentId)
			   .append(' onclick="deleteFile(').append(file.attachmentId).append(',\'').append(cancel_button).append('\',\'').append(upload_table).append('\');" alt="删除文件" title="删除"/>&nbsp;');
			
			_fileState.append('<a href="').append(this.customSettings.download_url)
					 .append('&attachmentId=').append(file.attachmentId).append('" target="_blank">');
			_fileState.append('<img src="').append(default_config.script_path);
			_fileState.append('images/icon/down.png" width="16" height="16" style="vertical-align:middle;" alt="查看文件" title="查看"/></a>');
			$(row.cells[3]).append(_fileState.toString());
			
			$("#img_"+file.attachmentId).closest("tr").attr("id", file.attachmentId);//为了使得新增的图片的删除功能和取消上传功能有效  20140705
			$("#img_"+file.attachmentId).closest("tr").attr("path", file.filePath);
			
			var imgStr = "png,jpg,gif,jpeg",		//可查看缩略图的附件类型
				amrStr = "amr",
				audioStr = "amr,mp3",				//音频附件类型
				suffix = getFileSuffix(file.name),
				thumbImgSrc = "",
				downloadUrl = this.customSettings.download_url+'&attachmentId='+file.attachmentId;
			
			if(imgStr.indexOf(suffix) >= 0) {
				thumbImgSrc = this.customSettings.imgDomain+"/"+file.filePath;
			} else if(audioStr.indexOf(suffix) >= 0) {
				thumbImgSrc = default_config.script_path + 'images/thumbnail/audio.jpg';
				downloadUrl = this.customSettings.download_video_url +'&attachmentId='+file.attachmentId + '&videoType=2';
			}
			
			$("#"+file.id+"_thumbImg").attr("src", thumbImgSrc);//修改缩略图的文件路径
			$("#"+file.id+"_thumbClose").attr("onclick", "deleteFile("+file.attachmentId+",'"+cancel_button+"','"+upload_table+"')");//修改缩略图删除图片操作
			$("#"+file.id+"_thumbAnchor").attr("href", downloadUrl);//修改缩略图查看原图连接
			$("#"+file.id+"_thumb").attr("id", file.attachmentId+"_thumb");//修改缩略图的a标签的id
			$("#"+file.id+"_thumbSpan").attr("id", file.attachmentId+"_thumbSpan");//修改删除图标的span的id
			
			$("#fileNum_"+cancel_button).val(parseInt($("#fileNum_"+cancel_button).val())+1);//上传成功后 +1
		} catch (ex) {
			this.debug(ex);
		}
	}
	
	
	//切换事件详情
	function getDetailEvent(eventid,instanceId){
	
		
	    $('#title_p',window.parent.document).html("事件待办");
	    $('.mask-vedio',window.parent.document).removeClass('mask-container-on');
	    $('.mask-dianwei',window.parent.document).removeClass('mask-container-on');
		
		$('.mc-item',window.parent.document).addClass('mci-big').removeClass('mci-small');
		$('.mask-container',window.parent.document).fadeIn(200);
		$('.mask-container-on',window.parent.document).removeClass('mask-container-on');
		$('.mask-event',window.parent.document).addClass('mask-container-on');
		$('.mae-event-handle',window.parent.document).removeClass('mae-container-on');
		$('.mae-event-add',window.parent.document).removeClass('mae-container-on');
		$('#ascrail2000',window.parent.document).css('display','none');
		window.parent.showEventInfo(eventid,"${event.bizPlatform!}",'00','0',instanceId);
		$.ajax({
			type: "POST",
		    url : '${rc.getContextPath()}/zhsq/eventWechat/saveEventWechat.jhtml',
			data: {'eventVerifyId':$("#bizId").val(),'status':'02','operateType':'0','eventId':eventid},
			dataType:"json",
			success: function(data) {
			}
		});
		
		
	}
	
	
</script>
</html>