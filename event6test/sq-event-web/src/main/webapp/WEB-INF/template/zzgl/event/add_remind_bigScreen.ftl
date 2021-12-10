
<@override name="styleNameBlock">  
<style>
<#if color??>
.LabName{color: ${color};}
<#else>.LabName{color: #cfedff;}
</#if>

.NorForm td{border-bottom: none;}
.BigTool{color:#333;    background: none;padding:0;margin-top: -20px;}
</style>
</@override>

<@override name="tableSubmitBlock">  
	 window.parent.postMessage('dbTableSubmitBack()','${backUrl}');
</@override>
 
  <@override name="cancelBlock">  
	function cancel(){
        window.parent.postMessage('cancel()','${backUrl}');
    }
</@override>

<@extends name="/zzgl/event/add_remind.ftl" />
  
  
