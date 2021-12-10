<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <title>新增商标信息</title>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
<#include "/component/maxJqueryEasyUIWin.ftl" />
    <link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
    <script type="text/javascript" src="${rc.getContextPath()}/js/My97DatePicker/WdatePicker.js"></script>
    <style>
        .PhotoEdit{width:96%; height:24px; line-height:24px; text-align:center; position:absolute;
            z-index:999;
            bottom:0px;
            left:7px;
            background:#000; filter:alpha(opacity=60); opacity:0.6; color:#fff; display:none; cursor:pointer;}
    </style>
</head>
<body>
<!--用于地图-->
<input type="hidden" id="id" name="id" value="${trademark.trademarkId!''}" />
<input type="hidden" id="module" name="module" value="<#if module??>${module }</#if>"/>
<input type="hidden" id="markerOperation" name="markerOperation" value="<#if markerOperation??>${markerOperation}</#if>"/>
<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId }</#if>"/>
<form id="submitForm" name="submitForm" action="" method="post" enctype="multipart/form-data">
    <input type="hidden" id="trademarkId" name="trademarkId" value="${(trademark.trademarkId)!}" />
    <div id="content-d" class="MC_con content light" style="overflow-x:hidden;">
        <div class="NorForm NorForm2">
            <table>
                <tr>
                    <td class="LeftTd" style="width: 230px;">
                        <label class="LabName">
                            <span>所在网格：</span>
                        </label>
                        <input type="hidden" id="gridCode" name="gridCode" value="${trademark.gridCode!''}" />
                        <input type="text" id="gridName" name="gridName" value="${trademark.gridName!''}" class="inp1 inp2 easyui-validatebox" style="" />
                    </td>

                    <td class="LeftTd" rowspan="4"><label class="LabName"><span>商标图样：</span></label>
                        <input name="trademarkImg" id="trademarkImg" type="hidden" value="<#if trademark.trademarkImg??>${trademark.trademarkImg!}</#if>"/>
                        <div class="FontRed" style="margin-bottom:5px;">	<span class="warning_tips" style="margin-bottom: -2px;"></span>	图片大小不能超过5MB</div>
                        <div class="Check_Radio FontDarkBlue">
                            <div id="localImag" style="position:relative;" >
                                <img id="preview" width="160px" height="120px" src="<#if trademark.trademarkImg?? >${RESOURSE_SERVER_PATH!}${trademark.trademarkImg!}</#if>  " /><#--<#if !trademark.trademarkImg?? > ${rc.getContextPath()}/ui/images/defalt.jpg</#if>-->
                                <div  id="photo" style="filter: alpha(opacity = 0); opacity: 0;margin-top:-117px;width:160px;height:117px;" >
                                    <input name="upPhoto" type="file"  id="upPhoto"  style=" filter: alpha(opacity = 0);width: 160px;height:100px;"
                                           accept="image/gif, image/jpg,image/jpeg,image/png, image/bmp" onchange="checkFile(this,'localImag','preview', 'gif,jpg,jpeg,png,bmp', '请选择类型为：gif,jpg,jpeg,png,bmp 的图片文件！',5)" hidefocus>
                                </div>
                                <div class="PhotoEdit">双击图片选择上传</div>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 230px;">
                        <label class="LabName">
                            <span>单位名称：</span>
                        </label>
                        <input type="text" id="unitName" name="unitName" class="inp1 inp2 easyui-validatebox" value="${trademark.unitName!''}" data-options="required:true,validType:['maxLength[200]','characterCheck']"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 200px;">
                        <label class="LabName">
                            <span>认定日期：</span>
                        </label>
                        <input type="text" class="Wdate inp1"  id="thatTime" name="thatTime" onclick="WdatePicker({startDate:'', dateFmt:'yyyy-MM-dd', readOnly:true, alwaysUseStartDate:true})"
                               value="<#if trademark.thatTimeDate??>${trademark.thatTimeDate?string('yyyy-MM-dd')}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td class="LeftTd" style="width: 200px;">
                        <label class="LabName">
                            <span>商标内容：</span>
                        </label>
                        <input type="text" id="brand" name="brand" class="inp1 inp2 easyui-validatebox" value="${trademark.brand}" data-options="validType:['maxLength[100]','characterCheck']"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd" >
                        <label class="LabName">
                            <span>单位地址：</span>
                        </label>
                        <input id="unitAddress" name="unitAddress" type="text" style="width: 300px;"
                               class="inp1 easyui-validatebox"
                               value="${trademark.unitAddress}"
                               data-options="required:true,validType:['maxLength[100]','characterCheck'],tipPosition:'bottom'"/>
                        <input type="hidden" id="latitude" name="latitude" value="${trademark.latitude!''}" /> <#--纬度-->
                        <input type="hidden" id="longitude" name="longitude" value="${trademark.longitude!''}"/><#--经度-->
                    <#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="LeftTd" style="width: 600px">
                        <label class="LabName">
                            <span>核定商品范围：</span>
                        </label>
                        <textarea id="scope" name="scope" class="easyui-validatebox" style="width: 430px;" rows="5" wrap="soft" data-options="validType:['maxLength[500]','characterCheck']">${trademark.scope!''}</textarea>
                    </td>
                </tr>

                <tr>
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
<script type="text/javascript">

    $("#content-d").mCustomScrollbar({theme: "minimal-dark"});

    $(function(){
        $("#localImag").hover(function(){
            $(this).find(".PhotoEdit").slideDown(200);
        }, function(){
            $(this).find(".PhotoEdit").slideUp(200);
        });
    });

    $(function() {

        //加载网格
        AnoleApi.initGridZtreeComboBox("gridName",  "gridCode", function(gridId, items) {
            if (items && items.length > 0) {
                $("#gridCode").val(items[0].orgCode);
            }
        });
    });





    //保存
    function save() {
        var isValid = $('#submitForm').form('validate');
        if (isValid) {
            document.getElementById('longitude').value = document.getElementById('x').value;
            document.getElementById('latitude').value = document.getElementById('y').value;
            modleopen(); //打开遮罩层
            $("#submitForm").attr("action","${rc.getContextPath()}/zhsq/szzg/trademark/save.jhtml");
            var ajax_option={
                type:'post',
                dataType:'text'
            }
            var result = $('#submitForm').ajaxSubmit(ajax_option);

            if(result){
                $.messager.alert('提示', '保存成功！', 'info', function() {
                    parent.searchData();
                    parent.closeMaxJqueryWindow();
                });
                modleclose();//关闭遮罩层
            }else{
                $.messager.alert('错误', '连接超时！', 'error');
            }

            return false;

        }

    }


    //取消
    function cancel() {
        parent.closeMaxJqueryWindow();
    }
    //地图标注
    function showMap(){
        //${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml
        //var callBackUrl = "http://gd.fjsq.org:8301/event/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml";
        var callBackUrl = "${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml";
        var width = 480;
        var height = 360;
        var gridId = $("#gridId").val();
        var markerOperation = $('#markerOperation').val();
        var id = $('#id').val();
        //var gridId = ""
        var mapType = $('#module').val();
        var isEdit = true;
        showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType);
    }
</script>

<script type="text/javascript">
    var img_width=160;
    var img_heigth=117;
    // 在file 中利用onchange调用checkFile函数
    function setImagePreview(ImgDivId,ImgId,DocId)
    {
        var docObj=document.getElementById(DocId);
        var imgObjPreview=document.getElementById(ImgId);
        if(docObj.files && docObj.files[0]){
            //火狐下，直接设img属性
            imgObjPreview.style.display = 'block';
            imgObjPreview.style.width =img_width+ 'px';
            imgObjPreview.style.height = 117+'px';
            imgObjPreview.style.border = '1px';
            imgObjPreview.style.solid = '#ccc';
            //imgObjPreview.src = docObj.files[0].getAsDataURL();
            //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
            imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
        }
        else{
            //IE下，使用滤镜
            docObj.select();
            var imgSrc = document.selection.createRange().text;
            var localImagId = document.getElementById(ImgDivId);
            //必须设置初始大小  style="width: 150px; height: 200px;border:1px solid #ccc;"
            localImagId.style.width = img_width+"px";
            localImagId.style.height = img_heigth+"px";
            localImagId.style.border ="1px";
            localImagId.style.solid = "#ccc";
            //图片异常的捕捉，防止用户修改后缀来伪造图片
            try{
                localImagId.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
            }
            catch(e){
                alert("您上传的图片格式不正确，请重新选择!");
                return false;
            }
            imgObjPreview.style.display = 'none';
            document.selection.empty();
        }
        return true;
    }
    var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过2M。";
    //限制文件选择
    function checkFile(file,ImgDivId,ImgId, type, str, limitSize){
        var isIE = (document.all) ? true : false;
        if(file!=null && file.value != ''){
            var lastPos = file.value.lastIndexOf('.');
            var pos, fileType;
            if(lastPos > -1){
                pos = lastPos + 1;
                fileType = file.value.substring(pos, file.value.length);
                fileType = fileType.toLowerCase();
                if(type.indexOf(fileType) > -1){
                    var files = $(file);
                    var size =0;
                    if(isIE){
                        // $.messager.alert('提示信息', tipMsg);

                    }
                    else
                        size = files[0].files[0].size;
                    if (limitSize != undefined && size > limitSize * 1024 * 1024) {
                        $.messager.alert('提示信息', '图片大小不能大于' + limitSize + 'M！', 'info');
                        return false;
                    }

                    // 如果是符合要求的图片，才进行预览显示
                    setImagePreview(ImgDivId,ImgId,file.id);
                }else{
                    var file = $("#"+file.id);
                    file.after(file.clone().val(""));
                    file.remove();

                }
            }
        }
    }


    /**
     * 初始化文件
     * @param _config
     *	var _config = {
 * 		positionId:'附件列表DIV的id值',
 * 		upload_table:'logo',//表格ID
 *		cancel_button:'cancel_button1'//取消全部按钮
 * 	    fileSize:'fileSize1',//自定义文件属性
 *	    filePath:'filePath1',//自定义文件属性
 *	    fileName:'fileName1',//自定义文件属性
 *      type:'',//add edit detail
 *      initType:'',//ajax、hidden()
 *      ajaxUrl:'',
 *      ajaxData:'',
 *      hiddenId:'',
 *      upload_url:'',//文件上传路径
 *      file_upload_limit:''
 *      file_size_limit:'',//默认1024MB
 *      file_types:'',//默认*.*
 *	}
     */
    function fileUpload(_config){
        var _data = getJSessionId();
        var _type = _config.type;
        var _cancel_button = _config.cancel_button || 'cancel_button';
        var _upload_table = _config.upload_table || 'upload_table';
        if(_config.context_path!=null){
            default_config.upload_url=_config.context_path+'/upFileServlet'+_data.jSessionId+'?method=up&moudle=attr&___userId='+_data.userId+'&___partyName='+_data.partyName;
            default_config.download_url=_config.context_path+'/upFileServlet?method=down';
            default_config.delete_url=_config.context_path+'/upFileServlet?method=delete';
            default_config.script_path=_config.context_path+'/scripts/updown/swfupload/';
        }

        var fileSizeLimit = _config.file_size_limit||"10 MB";



        fileSizeLimit = fileSizeLimit.toUpperCase();

        if(fileSizeLimit.toLowerCase() == fileSizeLimit) {//添加默认单位
            fileSizeLimit += " KB";
        }
        fileSizeLimit+='(支持mp4,mov,flv格式)';

        //1、init  div
        var _content = new StringBuffer();
        if(_config.type != 'detail'){
            _content.append('<div class="FontRed" style="margin-bottom:5px;">')
                    .append('	<span class="warning_tips" style="margin-bottom: -2px;"></span>')
                    .append('	文件大小不能超过').append(fileSizeLimit)
                    .append('</div>');

            _content.append('<div id="upload_file"></div>&nbsp;&nbsp;&nbsp;&nbsp;');
            if(_config.noDiplay){
                _content.append('<input id="').append(_cancel_button).append('"  style="display:none;" type="button" value=""');
                _content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn" disabled="disabled"/>');
            }else{
                _content.append('<input id="').append(_cancel_button).append('" type="button" value=""');
                _content.append('onclick="cancelUpload(\'').append(_upload_table).append('\',\'').append(_cancel_button).append('\');" class="can_btn3" disabled="disabled"/>');
            }


            _content.append('<input type="hidden" id="fileNum_'+_cancel_button+'" name="fileNum" value="0"/>');
        }
        if(_config.noDiplay){
            _content.append('<table id="').append(_upload_table).append('" style="display:none;" class="upload_table hide">');
        }else{
            _content.append('<table id="').append(_upload_table).append('" class="upload_table hide">');
        }

        /*_content.append('<tr><th colspan="2" width="250" align="left" style="padding-left:20px;">文件名称</th>')
                .append('<th width="50">大小</th><th width="100">状态</th>');
        _content.append('<th width="100">操作</th></tr></table>');*/
        $("#"+_config.positionId).html(_content.toString());
        //2、init data
        if(_config.initType=='ajax'){
            //AJAX从Action获取Json Data并加载
            $.ajax({
                type: "POST",
                async: false,
                url: _config.ajaxUrl,
                data: _config.ajaxData||{},
                dataType:'json',
                success: function(result){
                    var fileNumCount = parseInt($("#fileNum_"+_cancel_button).val()) + result.length;
                    $("#fileNum_"+_cancel_button).val(fileNumCount);

                    if(result.length > 0){
                        $('#'+_cancel_button).removeAttr('disabled');
                        $('#'+_cancel_button).attr('class', 'can_btn');
                    }
                    $(result).each(function(e){
                        addFile({
                            type:_type,
                            fileId:this.attachmentId,
                            fileName:this.fileName,
                            fileSize:formatFileSize(this.fileSize),
                            filePath:this.filePath,
                            table:_upload_table,
                            cancel_button:_cancel_button,
                            download_url : _config.download_url || default_config.download_url
                        });
                    });


                }
            });
        } else if(_config.initType == 'hidden'){
            //从隐藏域获取数据并加载
            var result = $.parseJSON($("#"+_config.hiddenId).val());
            $(result).each(function(e){

                addFile({
                    type:_type,
                    fileId:this.attachmentId,
                    fileName:this.fileName,
                    fileSize:this.fileSize,
                    filePath:this.filePath,
                    table:_upload_table,
                    cancel_button:_cancel_button,
                    download_url : _config.download_url || default_config.download_url
                });
            });
        }

        //3、 init fileupload comp
        if(_type != 'detail'){
            var config  = {
                upload_url: _config.upload_url||default_config.upload_url,
                // File Upload Settings
                //指定要上传的文件的最大体积，可以带单位，合法的单位有:B、KB、MB、GB(大小写均可)，如果省略了单位，则默认为KB。该属性为0时，表示不限制文件的大小。
                //设置值需为正整数，设置小数时，只取整数部分，设置负值及非数字时，设置为0
                file_size_limit : fileSizeLimit,
                file_types:_config.file_types||"*.doc;*.docx;*.xls;*.xlsx;*.txt;*.jpg;*.gif;*.png;*.rar;*.zip;*.tif;*.pdf",//文件类型
                //file_types_description : "产品LOGO",
                file_upload_limit:_config.file_upload_limit||50,
                //file_queue_limit:10,
                file_dialog_start_handler:fileDialog,
                file_queue_error_handler : fileQueueError,//上传文件错误时触发
                file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
                file_queued_handler : fileQueued,//选择完文件后就触发
                upload_progress_handler : uploadProgress,
                upload_error_handler : uploadError,//上传错误触发
                upload_success_handler : uploadSuccess,
                upload_complete_handler : uploadComplete,
                // Flash Settings
                flash_url : default_config.script_path+"swfupload.swf",
                button_placeholder_id : "upload_file",
                button_image_url: default_config.script_path+"images/upload.png",
                button_text : '',//'<span class="theFont">上 传文件</span>',
                button_width: 81,
                button_height: 21,
                button_text_left_padding: 12,
                button_cursor:-2,
                use_query_string : true,
                post_params :_config.ajaxData||{"a":"1"},
                custom_settings : {
                    cancel_upload: _cancel_button,
                    upload_table : _upload_table,
                    fileType : _config.fileType||'fileType',
                    upload_target: 'file_progress',
                    download_url : _config.download_url || default_config.download_url
                }//,debug:true
            };

            swfUpload = new SWFUpload(config);

            return swfUpload;
        }
    }
    /**
     * 上传文件错误时触发
     * @param file
     * @param errorCode
     * @param message
     */
    function fileQueueError(file, errorCode, message) {
        try {
            switch (errorCode) {
                case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                    $.messager.alert('文件大小为0！', 'error');
                    break;
                case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                    $.messager.alert('错误',"文件大小超过"+this.settings.file_size_limit);
                    break;
                case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                    $.messager.alert('错误','文件扩展名不符上传规定！', 'error');
                    break;
                case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                    $.messager.alert('错误','文件上传数超过限制！', 'error');
                    break;
                default:
                    $.messager.alert('错误','文件上传错误！', 'error');
                    break;
            }

        } catch (ex) {
            this.debug(ex);
        }
    }
</script>
</body>
</html>
