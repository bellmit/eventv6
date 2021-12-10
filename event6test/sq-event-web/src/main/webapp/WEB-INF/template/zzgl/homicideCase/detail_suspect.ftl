<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>命案防控-嫌疑人-详情页面</title>
	<#include "/component/standard_common_files-1.1.ftl" />
	<style type="text/css">
		.FontDarkBlue{width:200px;} 
		.LabName span{padding-right: 0px;}
		.Asterik{color:#f00;}
	</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post">
		<div id="content-d" class="MC_con content light">
			<div id="norFormDiv" class="NorForm">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="LeftTd" >
								<label class="LabName"><span><label class="Asterik">*</label>姓名：</span></label><div class="Check_Radio FontDarkBlue ">${people.name!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>证件类型：</span></label><div class="Check_Radio FontDarkBlue">${people.cardTypeName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>证件号码：</span></label><div class="Check_Radio FontDarkBlue">${people.idCardHS!''}</div>
							</td>
							
							
							
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>曾用名：</span></label><div class="Check_Radio FontDarkBlue ">${people.usedName!''}</div>
							</td>
							<td class="LeftTd" >
								<label class="LabName"><span><label class="Asterik">*</label>性别：</span></label><div class="Check_Radio FontDarkBlue">${people.sexName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span><label class="Asterik">*</label>出生日期：</span></label><div class="Check_Radio FontDarkBlue">${people.birthdayStr!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>国籍：</span></label><div class="Check_Radio FontDarkBlue ">${people.nationalityName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>民族：</span></label><div class="Check_Radio FontDarkBlue">${people.nationName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>学历：</span></label><div class="Check_Radio FontDarkBlue">${people.eduName!''}</div>
							</td>
							
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>婚姻状况：</span></label><div class="Check_Radio FontDarkBlue">${people.marriageName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>政治面貌：</span></label><div class="Check_Radio FontDarkBlue">${people.politicsName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>籍贯：</span></label><div class="Check_Radio FontDarkBlue ">${people.birthPlaceName!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>宗教信仰：</span></label><div class="Check_Radio FontDarkBlue">${people.religionName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>职业类别：</span></label><div class="Check_Radio FontDarkBlue">${people.professionTypeName!''}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>职业：</span></label><div class="Check_Radio FontDarkBlue ">${people.profession!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd" colspan="3">
								<label class="LabName"><span>服务处所：</span></label><div class="Check_Radio FontDarkBlue" style="width:804px;" >${people.workUnit!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><#if people.bizType?? && people.bizType=='03'><label class="Asterik">*</label></#if>户籍地：</span></label><div class="Check_Radio FontDarkBlue ">${people.reOrgCodeName!''}</div>
							</td>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>户籍地详址：</span></label><div class="Check_Radio FontDarkBlue" style="width:507px">${people.registAddrHS!''}</div>
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span>现住地：</span></label><div class="Check_Radio FontDarkBlue ">${people.liOrgCodeName!''}</div>
							</td>
							<td class="LeftTd" colspan="2">
								<label class="LabName"><span>现住详址：</span></label><div class="Check_Radio FontDarkBlue" style="width:507px">${people.residenceAddrHS!''}</div>
							</td>
						</tr>
						<tr <#if people.bizType?? && people.bizType!='03'>class="hide"</#if>>
							<td class="LeftTd">
								<label class="LabName"><span>是否严重精神<br/>障碍患者：</span></label><div class="Check_Radio FontDarkBlue">${people.mentalDisease!'否'}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>是否未成年人：</span></label><div class="Check_Radio FontDarkBlue">${people.minors!'否'}</div>
							</td>
							<td class="LeftTd">
								<label class="LabName"><span>是否青少年：</span></label><div class="Check_Radio FontDarkBlue">${people.teenager!'否'}</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		
		<div class="BigTool">
        	<div class="BtnList">
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">关闭</a>
            </div>
        </div>
	</form>
	
	<script type="text/javascript">
		$(function(){
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	        
	        $("#norFormDiv").width($(document).width());
	    });
	    
		function cancel(){
			parent.closeMaxJqueryWindow();
		}
	</script>
	
</body>
</html>
