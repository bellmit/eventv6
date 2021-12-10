<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<#include "/component/commonFiles-1.1.ftl" />
</head>

<body>
    <div id="content-d" class="MC_con content light">
                <div class="NorForm">
                	<#if ciRsParty??>
	                	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	                      <tr>
	                        <td class="LeftTd" style="width:60%;"><label class="LabName"><span>所属党支部：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyGroupName??>${ciRsParty.partyGroupName}</#if></div></td>
	                        <td><label class="LabName"><span>隶属关系：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.suborRela??>${ciRsParty.suborRela}</#if></div></td>
	                      </tr>
	                      <tr>
	                        <td class="LeftTd"><label class="LabName"><span>入党时间：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyJoinDate??>${ciRsParty.partyJoinDate}</#if></div></td>
	                        <td><label class="LabName"><span>入伍时间：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyArmDate??>${ciRsParty.partyArmDate}</#if></div></td>
	                      </tr>
	                      <tr>
	                        <td class="LeftTd"><label class="LabName"><span>党内职务：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyPost??>${ciRsParty.partyPost}</#if></div></td>
	                        <td><label class="LabName"><span>转入时间：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyTransferDate??>${ciRsParty.partyTransferDate}</#if></div></td>
	                      </tr>
	                      <tr>
	                        <td class="LeftTd"><label class="LabName"><span>党员关系所在地：</span></label>
	                        <div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyAddress??>${ciRsParty.partyAddress}</#if></div></td>
	                        <td><label class="LabName"><span>党小组：</span></label><div class="Check_Radio FontDarkBlue"><#if ciRsParty.partyTeam??>${ciRsParty.partyTeam}</#if></div></td>
	                      </tr>
	                      <tr>
	                        <td class="LeftTd"><label class="LabName"><span>工作岗位：</span></label>
	                        <div class="Check_Radio FontDarkBlue"><#if ciRsParty.workUnit??>${ciRsParty.workUnit}</#if></div></td>
	                        <td><label class="LabName"><span>是否党员志愿者：</span></label><div class="Check_Radio FontDarkBlue">
	                        	<#if ciRsParty.isVolunteer??>
			  						<#if ciRsParty.isVolunteer=="1">是<#else>否</#if>
								</#if>				
							</div></td>
	                      </tr>
	                    </table>
                    </#if>
                </div>
	</div>
	
	
	<script>
		(function($){
			$(window).load(function(){
				
				$.mCustomScrollbar.defaults.scrollButtons.enable=true; //enable scrolling buttons by default
				$.mCustomScrollbar.defaults.axis="yx"; //enable 2 axis scrollbars by default
				
				
				$("#content-d").mCustomScrollbar({theme:"dark"});
				
				$("#content-md").mCustomScrollbar({theme:"minimal-dark"});
			});
		})(jQuery);
	</script>
	<script type="text/javascript">

	//查看所属的党员信息
	function showPartyInfo(partyGroupId){
	 	 var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisdataofthing/partyMemberInfoIndex.jhtml?partyGroupId="+partyGroupId;
	 	 var title = "党员信息";
		 window.parent.showMaxJqueryWindow(title,url,720,400);
	}
	</script>
</body>
</html>
