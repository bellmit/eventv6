<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
    <title>log4j日志级别动态配置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/component/commonFiles-1.1.ftl" />
    <#include "/component/ComboBox.ftl" />
    <link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
    <style type="text/css">
        .inp1 {width:150px;}
        table {
            border-collapse: collapse;
        }
        th{
            background-color: #EEEEEE;
            border: solid;
            border-width: 0 1px 1px 0;
            border-color: #ccc;
            border-style: solid;
            height: 38px;
            font-size: 15px;
        }
        tr td{
            height: 38px;
            font-size: 14px;
            text-align: center;
            border: solid;
            border-color: #ccc;
            border-width: 0 1px 1px 0;
        }
        .span_calss{
            font-family:'Arial Normal', 'Arial';
            font-weight:400;
            font-size: 13px;
            display: inline-block
        }
        .span_level{
            font-size: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body >
<div style="width: 100%">
    <table style="width: 100%">
        <thead>
            <tr>
                <th name="loggerName" style="width: 40%">日志名称</th>
                <th name="appender" style="width: 20%">日志输出目的地</th>
                <th name="loggerLevel" style="width: 20%">日志级别</th>
                <th name="action" style="width: 20%">操作</th>
            </tr>
        </thead>
    </table>
</div>
<div class="table-con" style="width: 100%; height: -webkit-calc(100vh - 250px); height: calc(100vh - 250px); overflow-y: auto;">
    <table style="width: 100%; margin-top: -40px;">
        <thead style="height: 0px; overflow: hidden;">
        <tr>
            <th name="loggerName" style="width: 40%">日志名称</th>
            <th name="appender" style="width: 20%">日志输出目的地</th>
            <th name="loggerLevel" style="width: 20%">日志级别</th>
            <th name="action" style="width: 20%">操作</th>
        </tr>
        </thead>
        <tbody>
        <#if loggerAttrList?? && (loggerAttrList?size > 0)>
            <#list loggerAttrList as list>
            <tr>
                <td>${list.loggerName!}</td>
                <td>${list.loggerAppenderRef!}</td>
                <td>
                    <input type="hidden" id="loggerLevel${list_index}" name="loggerLevel${list_index}" value="${list.loggerLevel!}"/>
                    <input type="text" id="loggerLevelName${list_index}" name="loggerLevelName${list_index}" class="inp1 easyui-validatebox" data-options="tipPosition:'bottom'"/>
                </td>
                <td>
                    <a href="javascript:;" class="BigNorToolBtn SaveBtn" style="margin-left: 35%;" onClick="save('${list.loggerName!}','${list_index}');">保存</a>
                </td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>

<table>
    <div id="div_text" style="width: 100%;">
        <p>
            <span style="font-weight:700;color: red" class="span_calss">说明：</span>
        </p>
        <#if monitorInterval??>
        	<p>
        		<span class="span_calss FontRed">logger级别修改成功后，配置需要在<b>${monitorInterval}</b>秒后生效！</span>
        	</p>
        </#if>
        <p>
            <span class="span_calss">此功能必须手动配置logger，并设置logger的级别；logger级别不能放在Appender里，而是具体配置在logger中！</span>
        </p>
        <p>
            <span  class="span_calss">日志级别的动态修改仅在项目运行期间有效，重新部署项目，日志级别恢复到默认的配置级别（log4j2.xml文件中配置的级别）。</span>
        </p>
        <p>
            <span  class="span_calss">日志级别由高到低分别为（低级别日志信息向上包含高级别日志信息）：</span>
        </p>
        <p>
            <span class="span_level">OFF：</span>
            <span class="span_calss">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp是最高等级的，用于关闭所有日志记录。</span>
        </p>
        <p>
            <span class="span_level">FATAL：</span>
            <span class="span_calss">&nbsp&nbsp指出每个严重的错误事件将会导致应用程序的退出。</span>
        </p>
        <p>
            <span class="span_level">ERROR：</span>
            <span class="span_calss">&nbsp&nbsp指出虽然发生错误事件，但仍然不影响系统的继续运行。（大部分错误都用的是error来捕获。比如发生异常时打印的信息）</span>
        </p>
        <p>
            <span class="span_level">WARN：</span>
            <span class="span_calss">&nbsp&nbsp&nbsp 表明会出现潜在错误的情形。（程序运行时的警告信息）</span>
        </p>
        <p>
            <span class="span_level">INFO：</span>
            <span class="span_calss">&nbsp&nbsp&nbsp 表明消息在粗粒度级别上突出强调应用程序的运行过程。</span>
        </p>
        <p>
            <span class="span_level">DEBUG：</span>
            <span class="span_calss">&nbsp&nbsp指出细粒度信息事件对调试应用程序是非常有帮助的。（DEBUG及DEBUG以下级别可以打印SQL语句信息）</span>
        </p>
        <p>
            <span class="span_level">TRACE：</span>
            <span class="span_calss">&nbsp&nbsp与 DEBUG 相比更细致化的记录事件消息。</span>
        </p>
        <p>
            <span class="span_level">ALL：</span>
            <span class="span_calss">&nbsp&nbsp&nbsp&nbsp&nbsp  是最低等级的，用于打开所有日志记录。</span>
        </p>
    </div>
</table>

</body>
<script src="${rc.getContextPath()}/js/jquery.nicescroll.js"></script>
<script type="text/javascript">
    $(function () {

        $(".table-con").niceScroll({
            cursorcolor: "#ccc", // 改变滚动条颜色，使用16进制颜色值
             cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
             cursorwidth: "5px", // 滚动条的宽度，单位：便素
             cursorborder: "1px solid #fff", // CSS方式定义滚动条边框
            autohidemode:true,
        });

        //初始化日志级别选择下拉框
        <#if loggerAttrList?? && (loggerAttrList?size > 0)>
            <#list loggerAttrList as list>
                AnoleApi.initListComboBox("loggerLevelName${list_index}", "loggerLevel${list_index}", null, null ,["${list.loggerLevel!'ERROR'}"], {
                    /*ShowOptions : {
                        EnableToolbar : true
                    },*/
                    DataSrc: [{"name":"OFF", "value":"OFF"},
                              {"name":"FATAL", "value":"FATAL"},
                              {"name":"ERROR", "value":"ERROR"},
                              {"name":"WARN", "value":"WARN"},
                              {"name":"INFO", "value":"INFO"},
                              {"name":"DEBUG", "value":"DEBUG"},
                              {"name":"TRACE", "value":"TRACE"},
                              {"name":"ALL", "value":"ALL"}]
                });
            </#list>
        </#if>
    });

    /*
    * loggerName 日志名称
    * index 日志行记录索引--动态获取改变的 loggerLevel
    * */
    function save(loggerName,index) {
        //alert(loggerName + "   " + loggerLevel);

        var loggerLevel = $('#loggerLevel'+index).val();

        $.ajax({
            type:'POST',
            url:'${rc.getContextPath()}/zhsq/hotChangeLog4jLevel/changeLog4jLevel.json?',
            data:{loggerName:loggerName,loggerLevel:loggerLevel},
            dataType:'json',
            success:function (data) {
                if (data.result == 'success') {
                    $.messager.alert('提示', '操作成功！', 'info');
                } else {
                    $.messager.alert('错误', '操作失败！', 'error');
                }
            },
            error:function () {
                $.messager.alert('错误', '连接超时！', 'error');
            }
        });
    }
</script>
</html>
