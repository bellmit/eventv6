<link href="${uiDomain!''}/web-assets/extend/search-downpage/jquery.multiple.select-1.0.0/css/component-chosen.css" rel="stylesheet" />
<script type="text/javascript" src="${uiDomain!''}/web-assets/extend/search-downpage/jquery.multiple.select-1.0.0/js/jquery.multiple.select-1.0.0.js"></script>

<style>
input[type='radio']:checked + label .radio-input1 {
    border: 1px solid #6071f6;
}
input[type='radio']:checked + label .radio-input1 div {
    box-shadow: 0px 4px 7px #5294e8 inset;
    background-color: #5294e8;
}
.radio-input1 div {
    position: absolute;
    top: 1px;
    left: 1px;
    border-radius: 50%;
    width: 8px;
    height: 8px;
    background-color: #c6c3c3;
    box-shadow: 0px 4px 7px #c6c3c3 inset;
}
.radio-input1{
    position: relative;
    width: 12px;
    height: 12px;
    border: 1px solid #cdcbcb;
    border-radius: 50%;
    background-color: #fff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    display:inline-block;
    margin-right: 10px;
    margin-top:-3px;
    vertical-align:middle;
    line-height:1;
    cursor: pointer;
}
.radio-box {
    font-size: 14px;
    line-height: 18px;
    color: #666666;
    cursor: pointer;
}
.mr {
    margin-right: 10px;
}
.det-radio>dd {
    float: left;
    display: flex;
    align-items: center;
    height: 30px;
}
.det-radio {
    float: left;
}
td{
	padding-left:0
}
.td-title{
	width:100px !important;
}
.det-textarea{
	width: calc(100% - 120px);
}
.det-textarea>textarea {
	width: 100%;
    height: 66px;
    resize: none;
    box-sizing: border-box;
    padding: 8px 12px;
    font-size: 14px;
    background-color: #fafafa;
    border: solid 1px #cccccc;
    color: #333;
}
.span-text{
	width: calc(100% - 105px);
}
</style>
	<!-- 办理环节 handle -->
	<form id="handleForm" <#if showHandle!='y'>style="display:none"</#if> >
	<input type="hidden" id="sendId" name="sendId" value="${(bo.sendId)!}">
	<input type="hidden" id="sendType" name="sendType" value="">
	<div name="tab" id="div0" class="NorForm" style="border:1px solid #c5d0dc;margin-top:10px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<label class="LabName"><span>当前环节：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentNode=="">-<#else>${(bo.currentNode)!"-"}</#if>
					</span>
				</td>
				<td>
					<label class="LabName"><span>当前状态：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.statusStr=="">-<#else>${(bo.statusStr)!"-"}</#if>
					</span>
				</td>
			</tr>
			<tr>
				<td>
					<label class="LabName"><span>当前办理人：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentHandler=="">-<#else>${(bo.currentHandler)!"-"}</#if>
					</span>
				</td>
				<td>
					<label class="LabName"><span>当前办理组织：</span></label>
					<span class="Check_Radio FontDarkBlue span-text">
					<#if bo.currentCodeCn=="">-<#else>${(bo.currentCodeCn)!"-"}</#if>
					</span>
				</td>
			</tr>
			
			<tr>
				<td>
					<label class="LabName"><span>下一环节：</span></label>
					<dl class="det-radio ml40" >
					<#if nodeName=="task2">
                        <dd>
                            <input type="radio" id="nextLink1" name="nextLink" hidden="hidden" value="task3" checked/>
                            <label for="nextLink1" class="mr radio-box nextLink" data-val="1">
                                <div class="radio-input1 ">
                                    <div></div>
                                </div>
                                	镇街道专班
                            </label>
                        </dd>
                    </#if>
                    <#if nodeName=="task3">
                        <dd>
                            <input type="radio" id="nextLink2" name="nextLink" hidden="hidden" value="task4" checked/>
                            <label for="nextLink2" class="mr radio-box nextLink" data-val="2">
                                <div class="radio-input1 ">
                                    <div></div>
                                </div>
                                	村居专班
                            </label>
                        </dd>
                        <dd>
                            <input type="radio" id="nextLink3" name="nextLink" hidden="hidden" value="task5" />
                            <label for="nextLink3" class="mr radio-box nextLink" data-val="3">
                                <div class="radio-input1">
                                    <div></div>
                                </div>
                               		 网格员
                            </label>
                        </dd>
                    </#if>
                    <#if nodeName=="task4">
	                    <dd>
	                        <input type="radio" id="nextLink3" name="nextLink" hidden="hidden" value="task5" checked/>
	                        <label for="nextLink3" class="mr radio-box nextLink" data-val="3">
	                            <div class="radio-input1">
	                                <div></div>
	                            </div>
	                           		 网格员
	                        </label>
	                    </dd>
                    </#if>
                    <#if nodeName=="task5">
                    	<dd>
	                        <input type="radio" id="nextLink4" name="nextLink" hidden="hidden" value="task6" checked/>
	                        <label for="nextLink4" class="mr radio-box nextLink" data-val="4">
	                            <div class="radio-input1">
	                                <div></div>
	                            </div>
	                           		法院
	                        </label>
	                    </dd>
                    </#if>
                    <#if nodeName=="task6">
                    	<dd>
	                       <input type="radio" id="nextLink5" name="nextLink" hidden="hidden" value="end1" checked/>
                            <label for="nextLink5" class="mr radio-box nextLink" data-val="5">
                                <div class="radio-input1">
                                    <div></div>
                                </div>
                               		 归档
                            </label>
	                    </dd>
	                    <#if bo.status=="04">
		                    <dd>
		                       <input type="radio" id="nextLink6" name="nextLink" hidden="hidden" value="resend" />
	                            <label for="nextLink6" class="mr radio-box nextLink" data-val="6">
	                                <div class="radio-input1">
	                                    <div></div>
	                                </div>
	                               		重新发起
	                            </label>
		                    </dd>
	                    </#if>
                    </#if>
                    </dl>
				</td>
			</tr>
			<#if nodeName=="task6">
				<tr>
					<td colspan="2">
						<label class="LabName"><span>办理意见：</span></label>
						<textarea style="height:150px;width:82%;" name="advice" id="advice" value="" class="inp1 InpDisable easyui-validatebox" data-options="required:false,validType:'maxLength[500]'" ></textarea>
					</td>
				</tr>
			</#if>

			<!-- 选人及选组织操作 -->
			<#if nodeName=="task2" || nodeName=="task3">
			<tr class="org-tr">
				<td colspan="2">
				<#if nodeName=="task2">
	                <label class="LabName width65px"><span><label class="Asterik">*</label>选择镇组织：</span></label>
	            </#if>
	            <#if nodeName=="task3">
	                <label class="LabName width65px"><span><label class="Asterik">*</label>选择村居组织：</span></label>
	            </#if>
	                <input type="hidden" id="regionCode" name="sendOrgCode" value="" />
					<input type="text"  id="regionName" name="sendOrgName" class="inp1" style="width:150px;height:35px" value="" />
				</td>
			</tr>
			</#if>
			
			<#if nodeName=="task3" || nodeName=="task4">
			<tr class="person-tr">
				<td colspan="2">
					<div id="person-div" style="margin-top: 15px;">
				    <label class="LabName"><span><label class="Asterik">*</label>选择网格员：</span></label>
                    <select id="handleSelect" class="form-control form-control-chosen" style="display:none"
						data-placeholder="选择办理人员" multiple data-no_results_text="没有数据匹配"> 
					</select>
					<input type="hidden" id="handlerInfo" name="handlerInfo" class="person-data" />
					<input type="hidden" id="handlerInfoJson" name="handlerInfoJson" class="person-data-json"/>
					</div>
			    </td>
			</tr>
			</#if>
			
			<#if nodeName=="task5">
				<tr>
					<td colspan="2">
						<label class="LabName"><span>是否送达：</span></label>
						<dl class="det-radio ml40" >
                        <dd>
                            <input type="radio" id="receiveState1" name="receiveState" hidden="hidden" value="1" checked/>
                            <label for="receiveState1" class="mr radio-box receiveState" data-val="1">
                                <div class="radio-input1 ">
                                    <div></div>
                                </div>
                                	是
                            </label>
                        </dd>
                        <dd>
                            <input type="radio" id="receiveState2" name="receiveState" hidden="hidden" value="2" />
                            <label for="receiveState2" class="mr radio-box receiveState" data-val="2">
                                <div class="radio-input1 ">
                                    <div></div>
                                </div>
                                	否
                            </label>
                        </dd>
                        </dl>
					</td>
				</tr>
				<tr class="receiveState-tr">
					<td colspan="2">
						<label class="LabName"><span><label class="Asterik">*</label>送达图片：</span></label>
						<div id="attatch_div_handle"></div>
					</td>
				</tr>
				
				<tr class="notReceiveState-tr">
					<td colspan="2">
						<label class="LabName"><span><label class="Asterik">*</label>未送达原因：</span></label>
						<div class="flex1 flex-clm">
		                    <span class="det-textarea flex flex1 fl">
		                        <textarea style="padding:7px;" placeholder="请填写未送达原因" class="flex flex1 " maxlength="200" name="advice" id="advice"></textarea>
		                    </span>
		                </div>
					</td>
				</tr>
			</#if>
			
			<#if nodeName=="task6">
				<#if bo.receiveState=="1">
				<tr>
					<td colspan="2">
						<label class="LabName"><span><label class="Asterik">*</label>送达图片：</span></label>
						<div id="attatch_div_handle"></div>
					</td>
				</tr>
				</#if>
				
				<#if bo.receiveState=="2">
				<tr>
					<td colspan="2">
						<label class="LabName"><span><label class="Asterik">*</label>未送达原因：</span></label>
						<span class="Check_Radio FontDarkBlue span-text">${(bo.advice)!}</span>
					</td>
				</tr>
				</#if>
			</#if>
			
		</table>
	</div>
	</form>
<script>
var nodeName = '${nodeName!""}';
var handlerInfoJSONArr = [];//办理人员的JSON的数据
var currOrgId = '${regionId!}';//当前组织的orgId
var bigfileUpload_handle;//附件上传对象

$(function(){
	
	$('.nextLink').on('click', function(){
	    var val = $(this).attr("data-val");
	    if(val == "2"){
	    	$(".org-tr").show();
	    	$(".person-tr").hide();
	    }else if(val == "3"){
	    	$(".org-tr").hide();
	    	$(".person-tr").show();
	    }
	});
	$('.receiveState').on('click', function(){
	    var val = $(this).attr("data-val");
	    if(val == "1"){
	    	$(".receiveState-tr").show();
	    	$(".notReceiveState-tr").hide();
	    }else if(val == "2"){
	    	$(".receiveState-tr").hide();
	    	$(".notReceiveState-tr").show();
	    }
	});
	
	createUploadeElem();
	createSelectPerson();
	showChooseElem();
	showPersonHandleElem();
	
	
	if(typeof($("#regionName").attr("class")) != "undefined"){
		//所属地区
		AnoleApi.initGridZtreeComboBox("regionName", "regionCode", function (regionCode, items){
			if(items!=undefined && items!=null && items.length>0){
				var org = items[0];
				$("#regionCode").val(org.orgCode);
			}
		},{
			ShowOptions : {
				EnableToolbar : true
			},
		});
	}
});





//根据下一环节显示操作内容
function showChooseElem(){
	var val = $('input[name="nextLink"]:checked').val(); 
	if(val == "task3" || val == "task4"){
    	$(".org-tr").show();
    	$(".person-tr").hide();
    }else if(val == "task5"){
    	$(".org-tr").hide();
    	$(".person-tr").show();
    }
}
//根据是否送达显示操作内容
function showPersonHandleElem(){
	var val = $('input[name="receiveState"]:checked').val(); 
	if(val == "1"){
    	$(".receiveState-tr").show();
    	$(".notReceiveState-tr").hide();
    }else if(val == "2"){
    	$(".receiveState-tr").hide();
    	$(".notReceiveState-tr").show();
    }
}

//保存处理意见
function handleSubmit(){
	var nextLinkVal = $("input[name='nextLink']:checked").val();
	var receiveStateVal = $("input[name='receiveState']:checked").val();
	var sendOrgCode = $("#regionCode").val();	
	//根据下一环节获取包装对应的人员信息，及验证
	if (nextLinkVal == "task3"){
		//镇组织必填验证
		if(sendOrgCode=="" || sendOrgCode.length != 9){
			layer.msg('请选择镇级组织！');
			return;
		}
		$("#sendType").val("org");
	}else if (nextLinkVal == "task4"){
		//村组织必填验证
		//镇组织必填验证
		if(sendOrgCode=="" || sendOrgCode.length != 12){
			layer.msg('请选择村级组织！');
			return;
		}
		$("#sendType").val("org");
	}else if (nextLinkVal == "task5"){
		//办理人员包装及验证
		var handlerInfo = [];
		$('#handlerInfo').parent().find(".search-choice-close").each(function(){
			handlerInfo.push($(this).attr('value'));
		});
		$('#handlerInfo').val(handlerInfo.join(";"));
		if(handlerInfo != null && handlerInfo.length > 0){
			delete handlerInfoJSONArr[0].regisValue;
			delete handlerInfoJSONArr[0].text;
			$("#handlerInfoJson").val(JSON.stringify(handlerInfoJSONArr));
		}else{
			layer.msg('请选择相应的办理人员！');
			return;
		}
		$("#sendType").val("person");
	}else if(nextLinkVal == "task6"){
		$("#sendType").val("court");
	}else if (nextLinkVal == "resend"){//重新发起
		$("#sendType").val("end1");
		
		modleopen(); //打开遮罩层
		$.ajax({
		    type: 'POST',
		    url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/saveHandleInfo.json',
		    data:  $('#handleForm').serializeArray(),
		    dataType: 'json',
		    success: function(data) {
				var code = data.code;
				var msg = data.msg;
				if (code == "02") {
					//$.messager.alert('错误', msg, 'error');
					layer.msg(msg, {icon:2});
				} else {
					var sendId = $("#sendId").val();
					var resendUrl = '${rc.getContextPath()}/zhsq/gdPersionSendFlow/resend.jhtml?id='+sendId;
					window.location.href = resendUrl;
					return;
				}
			},
			error: function(data) {
				//$.messager.alert('错误', '连接超时！', 'error');
				layer.alert( '连接超时！', {icon: 2,title:'错误'}, function() {
					parent.closeMaxJqueryWindow();
				});
			},
			complete : function() {
				modleclose(); //关闭遮罩层
			}
		});
		return;
	}else if (nextLinkVal == "end1"){//归档
		$("#sendType").val("end1");
	}else{
		$("#sendType").val("org");
	}	
	
	if(receiveStateVal == "1"){
		var attachmentNum = $("#attatch_div_handle .file-block").length;
		if(attachmentNum == 0){
			layer.msg('请上传送达图片！');
			return;
		}
	}else if(receiveStateVal == "2"){
		//办理意见
		var advice = $("#advice").val();
		if(advice == ""){
			layer.msg('请填写未送达原因！');
			return;
		}
	}
	
	//附件上传完成验证
	var uploadResult = bigfileUpload_handle.submitValidate();
	if(uploadResult.incomplete!=0){
		layer.msg('附件尚未全部上传成功！');
		return;
	}

	modleopen(); //打开遮罩层
	$.ajax({
	    type: 'POST',
	    url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/saveHandleInfo.json',
	    data:  $('#handleForm').serializeArray(),
	    dataType: 'json',
	    success: function(data) {
			var code = data.code;
			var msg = data.msg;
			if (code == "02") {
				//$.messager.alert('错误', msg, 'error');
				layer.msg(msg, {icon:2});
			} else {
				layer.alert( msg, {icon: 1,title:'提示'}, function() {
					parent.closeMaxJqueryWindow();
				});
				parent.searchDataReLoad();
			}
		},
		error: function(data) {
			//$.messager.alert('错误', '连接超时！', 'error');
			layer.alert( '连接超时！', {icon: 2,title:'错误'}, function() {
				parent.closeMaxJqueryWindow();
			});
		},
		complete : function() {
			modleclose(); //关闭遮罩层
		}
	});
}

//驳回操作
function handleReject(){
	//关闭父窗口
	var url = '${rc.getContextPath()}/zhsq/gdPersionSendFlow/reject.jhtml?id='+${(bo.sendId)!};
	showMaxJqueryWindow('驳回', url, $(window).width() * 0.5, $(window).height() * 0.5);
}

//附件上传初始化
function createUploadeElem(){
	bigfileUpload_handle = $("#attatch_div_handle").bigfileUpload({
		//法院办理的时候需要展示详情页
		<#if nodeName=="task5">
		useType: 'edit',//附件上传的使用类型，add,edit,view，（默认edit）;
		<#else>
		useType: 'view',
		</#if>
		imgDomain : imgDomain,//图片服务器域名
		uiDomain : uiDomain,//公共样式域名
		skyDomain : skyDomain,//网盘挂载IP
		componentsDomain : componentsDomain,//公共组件域名
		fileDomain : fileDomain,//文件服务域名
		<#if nodeName=="task5">
		showTip : true,
		maxSingleFileSize : 10,
		<#else>
		showTip : false,
		</#if>
		fileExt : '.jpeg,.jpg,.png,.gif,.bmp,.webp,',//允许上传的附件类型
		appcode:"zhsq_event",//文件所属的应用代码（默认值components）
		module:"gdPersionSendFlow",//文件所属的模块代码（默认值bigfile）
		attachmentData:{bizId:'${bo.sendId!}',isBindBizId:'yes',attachmentType:'GD_PERSION_FLOW_TYPE_HANDLE'},
		uploadSuccessCallback : function(file,response){
			console.log(file.id);
			console.log(response.attachmentId);
		}
	});
}

//选人组件初始化
function createSelectPerson(){
	//var jsonpUrl = 'http://components.v6.aishequ.org/comp/foxmail/selectUser/getUserInfo.jhtml?orgId=1';
	//var jsonpUrl = 'http://components.cim.aishequ.org/comp/foxmail/selectUser/getUserInfo.jhtml?orgId='+orgIdCurrTown;
	var jsonpUrl = "${componentsDomain}/comp/foxmail/selectUser/getUserInfo.jhtml?orgId="+currOrgId;
	$('.form-control-chosen').chosen({
	  jsonpUrl:jsonpUrl,			
	  show_placeholder:true,
	  placeholderTxt : "请输入网格员名字搜索",
	  use_jsonp_data:true,
	  multiple:false,
	  allow_single_deselect: true,
	  width: 'calc(100% - 120px)',
	  chooseCallback:function(data,target){//data所选的对象数据，target:s所选的li元素
		var item = {
				"userName":data.entity.userName,"userId":data.key,"partyName":data.entity.partyName,
				"orgName":data.entity.orgName,"orgId":data.entity.socialOrgId,"orgCode":data.entity.orgCode,
				//"regionName":regionName,"regionId":regionId,"regionCode":regionCode,
				"regisValue":data.entity.regisValue,
				"verifyMobile":data.entity.verifyMobile,
				"text":data.html
		}; 
		  
		var id = target.closest(".chosen-container").nextAll("input[type='hidden']").attr("id");
		if(!!id){
			handlerInfoJSONArr = [];
			 if(id.indexOf ("handlerInfo" ) >= 0){
				handlerInfoJSONArr.push(item);
			 }
		 }
		 $("#nextStep").html(item.orgName);
		 
		 /* var widthLi = $("li.search-choice").outerWidth(true);
		 var widthDiv = $("li.search-choice").parent().parent().width();
		 $("li.search-field").width(widthDiv-widthLi-5); */
		
		 
	  },
	  removeCallback:function(target){//target:所点击的X的<a>元素
		var id = target.closest(".chosen-container").nextAll("input[type='hidden']").attr("id");
		var userId = target.attr("key");
		if(id.indexOf ("handlerInfo" ) >= 0){
			handlerInfoJSONArr = removeArrayList(handlerInfoJSONArr,userId);
		}
		 $("#nextStep").html("-");
	  }
	}); 
	
	$(".chosen-container-multi .chosen-choices ").css("bottom","7px");
	//$(".chosen-drop").css("width","500px");
	//$(".chosen-container-multi .chosen-choices ").css("border","0px");
	//$(".chosen-container-multi .chosen-choices .search-field input[type=text]").css("font-size","12px");
	//$(".chosen-container .chosen-results li ").css("font-size","12px");
}

//从src数组移除数据,返回新数组->src:原数组,userId:目标userId
function removeArrayList(src,userId){
	var result = [];
	for(var i=0;i<src.length;i++){
		if(src[i].userId+"" != userId){
			result.push(src[i]);
		}
	}
	src = result;
	return result;
}




</script>



