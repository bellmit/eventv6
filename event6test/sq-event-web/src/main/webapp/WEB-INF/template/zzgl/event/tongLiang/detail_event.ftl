<@override name="eventDetailPageTitle">
	重庆铜梁事件详情页面
</@override>
<@override name="eventDetailAdditionalQuote">

<style>
.WitchLink {
    width: 150px !important;
    margin-left: -50px;
}

.DBLink {
    padding: 10px 0 0 142px!important;
}

.ProcessingLink .ht ul li:nth-child(1){
	margin-right: 120px!important;
}

.t_pic{margin-left: 40px;}
.f_pic{margin-left: 40px;}
.LinkList{margin-left: 40px;}

#bigFileUploadDiv_zt_pxt{
	margin-top:5px;
}

</style>


</@override>

<@override name="curNodeTaskNameTr">
<#if event.status && event.status!='04'>
<tr>
    <td align="right" >当前环节：</td>
    <td colspan="5">
        <code>${curNodeTaskName!}<#if taskPersonStr??>|${taskPersonStr}</#if></code>
    </td>
</tr>
</#if>
</@override>

<@override name="variableAdjust">
	handleTaskNameWidth = 165;		//处理环节总宽度
</@override>

<@extends name="/zzgl/event/detail_event.ftl" />