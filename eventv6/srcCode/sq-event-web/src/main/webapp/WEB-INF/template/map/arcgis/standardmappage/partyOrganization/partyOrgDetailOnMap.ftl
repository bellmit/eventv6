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
        	<#if ciPartyGroup??>
            	<div class="title FontDarkBlue">组织概况</div>
            	<table id="formInfo" width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="LeftTd" style="width:60%;"><label class="LabName"><span>支部名称：</span></label><div class="Check_Radio FontDarkBlue" style="width:430px;">${ciPartyGroup.partyGroupName!''}</div></td>
                    <td><label class="LabName"><span>支部书记：</span></label><div class="Check_Radio FontDarkBlue">${ciPartyGroup.secretaryName!''}</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>联系电话：</span></label><div class="Check_Radio FontDarkBlue">${ciPartyGroup.telephone!''}</div></td>
                    <td><label class="LabName"><span>隶属党组织：</span></label><div class="Check_Radio FontDarkBlue">${ciPartyGroup.parentPartyGroupName!''}</div></td>
                  </tr>
                  <tr>
                   	<td class="LeftTd"><label class="LabName"><span>所在楼宇：</span></label><div class="Check_Radio FontDarkBlue"><#if buildingAddress??>${buildingAddress!''}<#else>未知</#if></div></td>
                    <td><label class="LabName"><span>所在网格：</span></label><div class="Check_Radio FontDarkBlue">${ciPartyGroup.gridName!''}</div></td>
                  </tr>
                  <tr>
                  <#if !showPartyMember??>
                    <td class="LeftTd"><label class="LabName"><span>党员数：</span></label>
                    <div class="Check_Radio FontDarkBlue"><span id="dangyuanNum">${ciPartyGroup.memberNum!''}</span><a href="###" onclick="showPartyInfo(${ciPartyGroup.partyGroupId})" style="color:#f39800; text-decoration:underline;">查看详情</a></div></td>
                  </#if>
                    <td <#if showPartyMember??>colspan="2"</#if>><label class="LabName"><span>备注：</span></label><div class="Check_Radio FontDarkBlue" style="width:230px;">${ciPartyGroup.partyGroupRemark!''}</div></td>
                  </tr>
                </table>
            </#if>
        	<#if ciPartyGroup??>
            	<div class="title FontDarkBlue">三员信息</div>
            	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>宣传委员：</span></label><div class="Check_Radio FontDarkBlue">${ciPartyGroup.propagandist!'无'}（由社区党员担任）</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>组织委员：</span></label>
                      <div class="Check_Radio FontDarkBlue">${ciPartyGroup.coordinator!'无'}（由居民代表，班楼长担任）</div></td>
                  </tr>
                  <tr>
                    <td class="LeftTd"><label class="LabName"><span>纪检书记：</span></label>
                      <div class="Check_Radio FontDarkBlue">${ciPartyGroup.supervisor!'无'}（由人大、政协代表及纪检干部担任）</div></td>
                  </tr>
                </table>
            </#if>
        </div>
	</div>
	
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	
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
			if (parseInt($("#dangyuanNum").html()) > 0) {
				var orgCode = '${ciPartyGroup.orgCode!''}';
		 	 	var url = "${ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgisdataofthing/partyMemberIndex.jhtml?partyGroupId="+partyGroupId+"&orgCode=" + orgCode;
		 	 	var title = "党员信息";
			 	showMaxJqueryWindow(title,url);
			}
		}
	</script>
</body>
</html>
