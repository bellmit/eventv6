<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>导入PDF</title>
    <!-- easyUI start -->
    <#include "/component/bigFileUpload.ftl" />
    <script>
        var bigFileUploadOpt = {
            useType: 'add',
            fileExt: '.pdf',
            attachmentData: {attachmentType:'${BRIEF_REPORT!}'},
            module: 'br',
            individualOpt : {
                isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
            }
        };

        <#if event.eventId?? || attachmentIds??>
        bigFileUploadOpt["useType"] = 'edit';
        bigFileUploadOpt["attachmentData"].eventSeq = "1,2,3";

        <#if event.eventId??>
        bigFileUploadOpt["attachmentData"].bizId = '${event.eventId?c}';
        </#if>

        <#if attachmentIds??>
        bigFileUploadOpt["attachmentData"].attachmentIds = "${attachmentIds!}";
        </#if>
        </#if>

        bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
    </script>


</head>

<body>
<div id="importPDF" class="easyui-window" title="导入PDF" modal="true" minimizable="false" maximizable="false" style="width:900px;height:750px;padding:5px;">

    <form id="editForm" method="post" action="${rc.getContextPath()}/zhsq/zzgl/briefingController/briefing/saveDetail.jhtml?reportId=${reportId}" autocomplete="off" enctype="multipart/form-data">
        <label class="LabName"><span>PDF上传：</span></label>
        <div id="bigFileUploadDiv"></div>
    </form>
</div>


</body>
</html>
