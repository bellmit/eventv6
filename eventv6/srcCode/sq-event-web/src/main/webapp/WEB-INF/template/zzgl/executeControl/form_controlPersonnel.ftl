<!DOCTYPE html>
<html>
<head>
	<title>保存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#--<人员选择控件-->
    <script type="text/javascript" src="${COMPONENTS_DOMAIN}/js/rs/jquery.baseCombo.js"></script>
    <script type="text/javascript" src="${COMPONENTS_DOMAIN}/js/rs/residentSelector.js"></script>
    <link href="${rc.getContextPath()}/upload/picUpload.css" rel="stylesheet" />
    <link href="${rc.getContextPath()}/upload/jingzfp.css" rel="stylesheet" />
    <style type="text/css">
        .inp1 {width:220px;margin-left: 5px}
        .textbox{
            width: 223px;
            height: 28px;
            margin-left: 5px !important;
        }
        i.spot-xh{
            display: inline-block;
            color: #f54952;
            padding-right: 5px;
        }
    </style>
    <script type="text/javascript">
        var base = "${rc.getContextPath()}";
        var fileDomain = '${fileDomain}';
        var imgDomain = '${IMG_URL}';
        var uiDomain = '${uiDomain}';
        var skyDomain = '${skyDomain}';
        var componentsDomain = '${COMPONENTS_DOMAIN}';
    </script>
</head>
<!--新版附件-->
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/layui.css">
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/layui-v2.4.5/layui/layui.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/css/big-file-upload.css">
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/upload-common.js"></script>
<script src="${uiDomain}/web-assets/extend/bigfileupload/bigfileupload-v2.1.0/js/big-file-upload.js" type="text/javascript" charset="utf-8"></script>
<body>
	<form id="submitForm">
		<div id="content-d" class="MC_con content light">
			<div name="tab" class="NorForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <input type="hidden" id="id" name="id" value="${bo.id!}" />
                    <input type="hidden" id="controlObjectId" name="controlObjectId" value="${bo.controlObjectId!}" />
                    <input type="hidden" id="controlLibraryId" name="controlLibraryId" value="${bo.controlLibraryId!}" />
                    <input type="hidden" id="libType" name="libType" value="${bo.libType!}" />
                    <input type="hidden" id="controlTaskId" name="controlTaskId" value="${bo.controlTaskId!}" />
                    <tr>
                        <td>
                            <label class="LabName"><span><i class="spot-xh">*</i>姓名</span></label>
                            <input type="text" id="name" name="name" required="true" value="${(bo.name)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'"  />
                        </td>
                        <td>
                            <label class="LabName"><span>身份证号</span></label>
                            <input type="text" id="identityCardNumber" name="identityCardNumber" value="${(bo.identityCardNumber)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[18]', tipPosition:'bottom'" readonly />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>性别</span></label>
                            <input type="hidden" id="gender" name="gender" value="${bo.gender!}" />
                            <#if bo.gender != null>
                            <input type="text" id="genderCN" name="genderCN" value="${(bo.gender=='M')?string("男","女")!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" readonly />
                            <#else>
                            <input type="text" id="genderCN" name="genderCN" value="${(bo.gender)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" readonly />
                            </#if>
                        </td>
                        <td>
                            <label class="LabName"><span>手机号</span></label>
                            <input type="text" id="mobile" name="mobile" value="${(bo.mobile)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[11]', tipPosition:'bottom'" readonly />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>出生日期</span></label>
                            <input type="text" id="birthday" name="birthday" value="${(bo.birthday?string('yyyy-MM-dd'))!}" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom'" readonly />
                        </td>
                        <td>
                            <label class="LabName"><span>民族</span></label>
                            <input type="hidden" id="nationality" name="nationality" value="${bo.nationality!}" />
                            <input type="text" id="nationalityCN" name="nationalityCN" value="${(bo.nationalityCN)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[20]', tipPosition:'bottom'" readonly />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label class="LabName"><span>相似度</span></label>
                            <input type="text" id="similarity" name="similarity" value="${(bo.similarity)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[3]', tipPosition:'bottom'"  />
                        </td>
                        <td>
                            <label class="LabName"><span>描述</span></label>
                            <input type="text" id="description" name="description" value="${(bo.description)!}" class="inp1 easyui-validatebox" data-options="validType:'maxLength[100]', tipPosition:'bottom'"  />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="fw-det-toggle clearfix" id="attForm_">
                                <div class="wl-add-types-con flex flex-ac">
                                    <label class="LabName fw-from1"><span><i class="spot-xh">*</i>图片上传</span></label>
                                    <div id="bigupload" class="parent_div"></div>
                                </div>
                            </div>
                        </td>
                    </tr>
				</table>
			</div>
		</div>
		<div class="BigTool">
	    	<div class="BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">取消</a>
	        </div>
	    </div>
	</form>
</body>
<script type="text/javascript">
    var attachmentNum = 0;
    var bigupload=null;
    $(function() {
        //性别
        nationComboBox = AnoleApi.initListComboBox("genderCN", "gender", null, null, ["${bo.gender!}"], {
            DataSrc : [{"name":"男", "value":"M"},{"name":"女", "value":"F"}],
            ShowOptions : {
                EnableToolbar : true
            },
            DefText : '请选择'
        });
       /* //民族
        nationComboBox = AnoleApi.initListComboBox("nationalityCN", "nationality", "${NATIONALITY}", null, ["${bo.nationality!}"], {
            ShowOptions : {
                EnableToolbar : true
            }
        });*/
        //人员选择控件初始化
        var singleChoose = $('#name').residentSelector({
            height : 30,
            width : 225,
            panelHeight:300,
            panelWidth:450,
            dataDomain : '${RS_DOMAIN}',
            type:'v6resident',
            onClickRow:function(index,row){
                console.log(row);
                fillCiRsInfo(row);
            }
        });
        //附件上传
        uploadFile();
    });

    //附件上传必填验证
    function attVali(){
        attachmentNum = bigupload.getAttachmentNum ();
        if(attachmentNum<1){//是否存在附件
            layer.msg("请上传照片");
            return false;
        }
        return true;
    }

    //新版附件上传
    function uploadFile() {
        bigupload = $("#bigupload").bigfileUpload({
            useType: 'edit',////附件上传的使用类型，edit,view，（默认edit）;
            chunkSize : 5,//切片的大小（默认5M）
            fileNumLimit : 5,//最大上传的文件数量（默认9）
            maxSingleFileSize:5,//单个文件最大值（默认300）,单位M
            fileExt : '.jpg,.png,',//支持上传的文件后缀名(默认开放：.bmp,.pdf,.jpg,.text,.png,.gif,.doc,.xls,.docx,.xlsx,.ppt,.pptx,.mp3,.wav,.MIDI,.m4a,.WMA,.wma,.mp4,)
            //initFileArr : attarr_1,////初始化的附件对象数组(默认为{})
            attachmentData:{bizId:'${bo.id!}',
                isBindBizId:'yes',
                attachmentType:'bkk_form'},
            appcode:"testAPP",//文件所属的应用代码（默认值components）
            module:"testModule",//文件所属的模块代码（默认值bigfile）
            imgDomain : imgDomain,//图片服务器域名
            uiDomain: uiDomain,
            skyDomain: skyDomain,
            fileDomain: fileDomain,
            componentsDomain : componentsDomain,//图片服务器域名
            //serverUrl : 'http://zzh.v6.aishequ.org:9611/partFile/uploadPartFile.jhtml',
            isSaveDB : true,//是否需要组件完成附件入库功能，默认接口为sqfile中的cn.ffcs.file.service.FileUploadService接口
            isUseLabel: false,//是否开启附件便签功能
            isDelDbData : true,
            labelDict : [
                {'name':'处理前','value':'1'},
                {'name':'处理中','value':'2'},
                {'name':'处理后','value':'3'}],
            isUseSetText: false,//是否开启附件设置回调功能
            setCallback:function(obj){
                console.log(obj);
                $("."+obj.attr("span-class")).html("设为主图");
                obj.find("."+obj.attr("span-class")).html("主图");
            },
            uploadSuccessCallback : function(file,response){
                var html = '<input type="hidden" name="idCardReverse" value="'+response.path+'" />';
                $("#submitForm").append(html);
            }
        });
    }

    //人口信息回填
    function fillCiRsInfo(data){
        $("#name").val(data["name"]);//为了使必填验证通过
        $("input[name='name']").val(data["name"]);//姓名 人员选择器返回值会将ciRId填回到name
        $('#identityCardNumber').val(data["identityCard"]);//身份证
        $('#gender').val(data["gender"]);//性别
        $('#genderCN').val(data["genderCN"]);//性别
        $('#nationalityCN').val(data["ethnicCN"]);//民族
        $('#nationality').val(data["ethnic"]);//民族
        $('#mobile').val(data["phone"]);//手机号
        $('#birthday').val(formatDate(data["birthday"].time, 'yyyy-MM-dd'));//出生日期
    }

    function formatDate(dateStr, format) {
        var date = new Date(dateStr);
        var o = {
            "M+": date.getMonth() + 1, //month
            "d+": date.getDate(),    //day
            "h+": date.getHours(),   //hour
            "m+": date.getMinutes(), //minute
            "s+": date.getSeconds(), //second
            "q+": Math.floor((date.getMonth() + 3) / 3),  //quarter
            "S": date.getMilliseconds() //millisecond
        };
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1,
                        RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }

	//保存
	function save() {
        var mobile = $("#mobile").val();
        var similarity = $("#similarity").val();
        //图片校验
        if (!attVali()) {
            return;
        }
        /*if(mobile==''){
            $.messager.alert('提示', '请输入正确的手机号码!', 'info');
            return;
        }
        mobile = mobile.trim();
        if(!/1[\d]{10}/.test(mobile)){
            $.messager.alert('提示', '请输入正确的手机号码!', 'info');
            return;
        }*/
        if(similarity<0 || similarity >100){
            $.messager.alert('提示', '请输入合适的相似度!', 'info');
            return;
        }
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/event/controlPersonnel/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
                    console.log(data);
					if (data.result == 'fail') {
                        $.messager.alert('错误', '保存失败！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
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
</script>
</html>
