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
                    <span class="Check_Radio FontDarkBlue">${(bo.callId)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>诉求类型：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.kindName)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>诉求标题：</span></label>
                    <span style="width:459px;" class="Check_Radio FontDarkBlue">${(bo.title)!}</span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="LabName"><span>诉求内容：</span></label>
                    <span style="width:459px;" class="Check_Radio FontDarkBlue">${(bo.contents)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>来源：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.source)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>诉求提交时间：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.createTime)!}</span>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="LabName"><span>诉求人：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.callerName)!}</span>
                </td>
                <td>
                    <label class="LabName"><span>手机号：</span></label>
                    <span class="Check_Radio FontDarkBlue">${(bo.callerCellphone)!}</span>
                </td>
            </tr>
			<#if bo.replyList??>
       			<tr>
		   			<td colspan="2">
		            	<label class="LabName"><span>回复列表：</span></label>
		   			</td>
		    	</tr>
            	<#list bo.replyList as replyInfo>
            		<tr>
		                <td colspan="2">
		                    <label class="LabName"><span>回复意见：</span></label>
		                    <span style="width:459px;" class="Check_Radio FontDarkBlue">${(replyInfo.replyMsg)!}</span>
		                </td>
		            </tr>
		            <tr>
		                <td>
		                    <label class="LabName"><span>办理部门：</span></label>
		                    <span class="Check_Radio FontDarkBlue">${(replyInfo.replyerName)!}</span>
		                </td>
		                <td>
		                    <label class="LabName"><span>回复时间：</span></label>
		                    <span class="Check_Radio FontDarkBlue">${(replyInfo.replyTime)!}</span>
		                </td>
		            </tr>
            	</#list>
			</#if>
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

    //关闭
    function cancel() {
        parent.closeMaxJqueryWindow();
    }

</script>
</html>
