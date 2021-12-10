<!DOCTYPE html>
<html>
<head>
	<title>新增/编辑</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<style type="text/css">
		.inp1 {width:220px;}
		.LabName{
			width: 120px;
		}
		.Asterik{
			color: red;
		}
		.sqxt{
			display: none;
		}
	</style>
</head>
<body>
	<form id="submitForm">
		<input type="hidden" id="sendId" name="sendId" value="${(bo.sendId)!}" />
		<input type="hidden" id="saveType" name="saveType" />
		<input type="hidden" id="status" name="status" value="01"/>
		<input type="hidden" id="receiveState" name="receiveState" value="0"/>
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>流程名称：</span></label>
							<input type="text" id="flowName" name="flowName" value="${(bo.flowName)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[60]', tipPosition:'bottom'"  />
						</td><td>
							<label class="LabName"><span><label class="Asterik">*</label>协助类型：</span></label>
							<input type="hidden" name="sendType" id="sendType" value='${(bo.sendType)!}' />
							<input type="text" id="sendTypeName" name="sendTypeName" value="${(bo.sendTypeCN)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[10]', tipPosition:'bottom'" />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>发起日期：</span></label>
							<input type="text" id="publishDate" name="publishDate" value="${(bo.publishDateStr)!}" class="inp1 Wdate" data-options="required:true,trequired:true,,tipPosition:'bottom'" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly />
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>办理期限(天)：</span></label>
							<input type="number" class="inp1 easyui-numberbox" style="height: 30px" value="${(bo.limitDateNum)!}" id="limitDateNum" name="limitDateNum" data-options="min:0, max:9999,required:true, trequired:true,ipPosition:'bottom'"/>
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>当事人姓名：</span></label>
							<input type="text" id="receiveName" name="receiveName" value="${(bo.receiveName)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[20]', tipPosition:'bottom'"  />
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>当事人地址：</span></label>
							<input type="text" id="receiveAddr" name="receiveAddr" value="${(bo.receiveAddr)!}" class="inp1 easyui-validatebox" data-options="required:true,validType:'maxLength[60]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr>
						<td>
							<label class="LabName"><span>详细说明：</span></label>
							<textarea style="height:150px;" name="expound" id="expound" value="" class="inp1 InpDisable easyui-validatebox" data-options="required:false,validType:'maxLength[500]'" >${(bo.expound)!}</textarea>
						</td>
						<td style="width: 50%">
							<label class="LabName"><span>附件：</span></label>
							<div id="attatch_div" style="height:150px;width: 70%;margin-left: 23%;">
							</div>
						</td>
					</tr>
					<tr class="sqxt">
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>原告：</span></label>
							<input type="text" id="plaintiff" name="plaintiff" value="${(bo.plaintiff)!}" class="inp1 easyui-validatebox sqxt-input" data-options="required:true,validType:'maxLength[60]', tipPosition:'bottom'"  />
						</td>
						<td>
							<label class="LabName "><span><label class="Asterik">*</label>被告：</span></label>
							<input type="text" id="defendant" name="defendant" value="${(bo.defendant)!}" class="inp1 easyui-validatebox sqxt-input" data-options="required:true,validType:'maxLength[60]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<tr class="sqxt">
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>案号：</span></label>
							<input type="text" id="caseNo" name="caseNo" value="${(bo.caseNo)!}" class="inp1 easyui-validatebox sqxt-input" data-options="required:true,validType:'maxLength[50]', tipPosition:'bottom'"  />
						</td>
						<td>
							<label class="LabName"><span><label class="Asterik">*</label>案由：</span></label>
							<input type="text" id="caseReason" name="caseReason" value="${(bo.caseReason)!}" class="inp1 easyui-validatebox sqxt-input" data-options="required:true,validType:'maxLength[500]', tipPosition:'bottom'"  />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save(1);">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn BigShangBaoBtn" onClick="save(2);">提交</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>

<!--新版附件-->
<#--<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/jqry-9-1-12-4.min.js" type="text/javascript" charset="utf-8"></script>-->
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
<script>
	var base = '${rc.getContextPath()}';//项目的上下文，（工程名）
	var imgDomain = '${imgDomain}';//文件服务器域名
	var uiDomain = '${uiDomain}';//公共样式域名
	var skyDomain = '${skyDomain}';//网盘挂载IP【文档在线预览服务器IP：询问赛男团队，获取ip值】
	var componentsDomain = '${componentsDomain}';//公共组件工程域名
	var fileDomain = '${fileDomain}';//文件服务工程域名
</script>
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/custom_msgClient.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js" type="text/javascript" ></script>
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>


<script type="text/javascript">
	//0 启动工作流成功，派发区专班失败 1 成功 -1 查询区专班人员失败 -2 查询上级组织失败 -3 启动工作流失败
	var messageMap = {
						"0":"启动成功，下派失败！",
					 	"1":"下派成功！",
					 	"-1":"未获取区专班人员！",
					 	"-2":"未获取上级组织！"
					}
	var sendId = '${(bo.sendId)!}';
	var attD;
	$(function () {
		AnoleApi.initListComboBox("sendTypeName", "sendType", "B12322001",function(value,item,inobj) {
			if(value == "003"){
				$('.sqxt').show()
				$('#plaintiff').validatebox('options').required = true;
				$('#defendant').validatebox('options').required = true;
				$('#caseNo').validatebox('options').required = true;
				$('#caseReason').validatebox('options').required = true;
			}else{
				$('.sqxt').hide()
				$('#plaintiff').validatebox('options').required = false;
				$('#defendant').validatebox('options').required = false;
				$('#caseNo').validatebox('options').required = false;
				$('#caseReason').validatebox('options').required = false;
			}

		},['${(bo.sendType)!}']);
		initAttatch()
	})


	/**
	 *
	 * @param saveType 1 保存草稿 2 保存并发布
	 */
	function save(saveType) {
		$("#saveType").val(saveType);
		var isValid = $('#submitForm').form('validate');
		var publishDate = $("#publishDate").val();
		var su = attD.submitValidate();
		if(su["incomplete"] != 0){
			layer.msg("附件未上传完！");
			return;
		}
		if(!publishDate){
			layer.msg("未填写发起日期！");
			return;
		}
        var limitDateNum = $("#limitDateNum").val();
        if(limitDateNum % 1 != 0 || limitDateNum <=0){
            layer.msg("办理期限只能为正整数！");
            return;
        }else{
			$("#limitDateNum").val(parseInt(limitDateNum))
		}

		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/gdPersionSendFlow/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					var message = data.tipMsg;
					if(data.type != "-99"){
						message = messageMap[data.type]
					}
					if (!data.success) {
						if(data.type != "-99"){
							layer.alert( message,{icon: 2,title:'提示'}, function() {
								parent.closeMaxJqueryWindow();
							});
							parent.searchData();
						}else{
							layer.msg(message, {icon: 2});
						}
					} else {
						layer.alert( message,{icon: 1,title:'提示'}, function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
					}
				},
				error: function(data) {
					$.messager.alert('错误', '连接超时！', 'error');
				},
				complete : function() {
					modleclose(); //关闭遮罩层
				}
			});
		}
	}
	
	//取消
	function cancel() {
		parent.closeMaxJqueryWindow();
	}

	function initAttatch() {
		var attachmentData = {};
		var useType = "add";
		if(sendId != ''){
			useType = "edit";
			attachmentData = {bizId:sendId,attachmentType:'GD_PERSION_FLOW_TYPE'}
		}
		attD = $("#attatch_div").bigfileUpload({
			useType: useType,//附件上传的使用类型，add,edit,view，（默认edit）;
			imgDomain : imgDomain,//图片服务器域名
			uiDomain : uiDomain,//公共样式域名
			skyDomain : skyDomain,//网盘挂载IP
			componentsDomain : componentsDomain,//公共组件域名
			fileDomain : fileDomain,//文件服务域名
			// fileExt : '.jpeg,.jpg,.png,.gif,.doc,.xls,',//允许上传的附件类型
			appcode:"zhsq_event",//文件所属的应用代码（默认值components）
			module:"gdPersionSendFlow",//文件所属的模块代码（默认值bigfile）
			attachmentData:attachmentData,
			uploadSuccessCallback : function(file,response){
				// console.log(file.id);
				// console.log(response.attachmentId);
			}

		});

	}
</script>
</html>
