<!DOCTYPE html>
<html>
<head>
    <title>详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<#include "/component/commonFiles-1.1.ftl" />
<#include "/component/ComboBox.ftl" />
    <script type="text/javascript" src="${uiDomain!''}/js/openJqueryEasyUIWin.js"></script>
    <link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
</head>
<body>

<div id="content-d" class="MC_con content light">
    <div name="tab" id="div0" class="NorForm">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <label class="LabName"><span>诉求编号：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.appealNo)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>诉求类别：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.appealCatalogStr)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>诉求标题：</span></label>
                    <span style="width:80%" class="Check_Radio FontDarkBlue">${(bo.appealTitle)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>诉求内容：</span></label>
                    <span style="width:80%" class="Check_Radio FontDarkBlue">${(bo.content)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>来源：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.sourceStr)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>诉求提交时间：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.appealTimeStr)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>诉求人：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.userName)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>手机号：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.phone)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>附件：</span></label>
                    <div class="ImgUpLoad" id="fileupload"></div>
                </td>

            </tr>
        </table>
    </div>
</div>
<div class="BigTool">
    <div class="BtnList">
        <a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
    </div>
</div>


</body>
<script type="text/javascript">

    $(function(){
        swfOpt = {
            positionId:'fileupload',//附件列表DIV的id值',
            type:'detail',//add edit detail
            initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
            context_path:'${SQ_FILE_URL}',
            ajaxData: {'bizId':'${bo.appealId?c}','attachmentType':'PUBLIC_APPEAL','eventSeq':'1,2,3'},
            showPattern: 'list',
            imgDomain: '${imgDownPath!}'
        };
        fileUpload(swfOpt);
    });

    var appealId = "${(bo.appealId)!}";
    function report() {
        $("#tableForm").attr("action", "${rc.getContextPath()}/zhsq/eventWechat/saveEventWechat.jhtml");

        startWorkflow(${(bo.appealId)!});
    }

    function startWorkflow(appealId) {
        if (appealId && appealId > 0) {
            modleopen();

            $.ajax({
                type: "POST",
                url: '${rc.getContextPath()}/zhsq/publicAppeal/startWorkflow.jhtml',
                data: {'publicAppealId': appealId},
                dataType: "json",
                success: function (data) {
                    if (data.success && data.success == true) {
                        parent.searchData();
                        parent.eventDetail(data.data,"2");
                        if (typeof(parent.closeBeforeMaxJqueryWindow) == 'function') {
                            parent.closeBeforeMaxJqueryWindow();
                        }
                    } else {
                        modleclose();

                        if (data.tipMsg) {
                            $.messager.alert('错误', data.tipMsg, 'error');
                        } else {
                            $.messager.alert('错误', '操作失败！', 'error');
                        }
                    }
                },
                error: function (data) {
                    $.messager.alert('错误', '连接错误！', 'error');
                }
            });
        }
    }

    //关闭
    function cancel() {
        parent.closeMaxJqueryWindow();
    }

    //    function reject() {
    //        var url = '${rc.getContextPath()}/zhsq/publicAppeal/to/reject.jhtml?appealId=' + appealId;
    //        showMaxJqueryWindow('驳回意见', url, 650, fixHeight(0.4));
    //    }

    function reject(){
        var opt = {
            'maxHeight': 150,
            'maxWidth': 600
        };
        var url = '${rc.getContextPath()}/zhsq/publicAppeal/to/reject.jhtml?id=' + appealId;

        opt.title = "驳回";
        opt.targetUrl = url;

        openJqueryWindowByParams(opt);
    }

    function close(){
        $.messager.alert('提示', '操作成功！', 'info', function() {
            parent.searchData();
            parent.closeMaxJqueryWindow();
        });
    }

</script>
</html>
