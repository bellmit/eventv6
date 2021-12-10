<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增巡防</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/scripts/updown/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${rc.getContextPath()}/scripts/updown/swfupload/handlers.js"></script>
</head>
<style>
.inp2{width: 140px;}
</style>
<body>
<div>
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
	                <div class="NorForm NorForm2">
                	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/patrolController/save.jhtml"  method="post">
                   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                       <tr>
                       	<!--
                       	<td class="LeftTd"><label class="LabName"><span>编号：</span></label>
                        <input name="code" id="code" type="hidden" readonly class="inp1 inp2" value="<#if patrol.code??>${patrol.code}</#if>" />
                        <div class="Check_Radio FontDarkBlue">
                        	<#if patrol.code??>${patrol.code}</#if>
                        </div>
                        </td>
                        -->
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>所属网格：</span></label>
                        <input type="hidden" id="patrolStatus" name="patrolStatus" value="0">
                        <input type="hidden" id="patrolId" name="patrolId" value="<#if patrol.patrolId??>${patrol.patrolId}</#if>">
                        <input type="hidden" id="gridId" name="gridId" value="<#if patrol.gridId??>${patrol.gridId}</#if>">
				 		<input type="hidden" id="gridCode" name="gridCode" value="<#if patrol.gridCode??>${patrol.gridCode}</#if>">
                        <input class="inp1 inp2 InpDisable easyui-validatebox" type="text" id="gridName" data-options="required:true" name="gridName" value="<#if patrol.gridName??>${patrol.gridName}</#if>" />
                        </td>
                      </tr>
                      <tr>
                      
                      <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>事件名称：</span></label>
                        <input name="name" id="name" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 440px;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if patrol.name??>${patrol.name}</#if>"/></td>
            		</tr>
                         
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>带队负责人：</span></label>
                        <input name="principal" id="principal" type="text" style="height: 28px;width: 160px;" class="inp1 inp2 easyui-validatebox" value="<#if patrol.principal??>${patrol.principal}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                        <td><label class="LabName"><span>联系电话：</span></label>
                    	<input name="principalTel" id="principalTel" type="text" class="inp1 inp2 easyui-validatebox" value="<#if patrol.principalTel??>${patrol.principalTel}</#if>"  data-options="tipPosition:'bottom',validType:'maxLength[15]',validType:'mobileorphone'"/>
                        </td>
                   </tr>
            		
            		<tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>巡防时间：</span></label>
                        <input id="startPatrolTimeStr" name="startPatrolTimeStr" type="text" style="height: 28px;width: 160px;" data-options="tipPosition:'bottom',required:true" class="inp1 fl Wdate easyui-validatebox"  onclick="WdatePicker({el:'startPatrolTimeStr',dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endPatrolTimeStr\')}'})"  value="<#if patrol.startPatrolTimeStr??>${patrol.startPatrolTimeStr}<#else><#if dateStr??>${dateStr}</#if></#if>"/>
                        <em class="unit fl">至</em>
                        <input id="endPatrolTimeStr" name="endPatrolTimeStr" type="text" style="height: 28px;width: 160px;" data-options="tipPosition:'bottom',required:true" class="inp1 fl Wdate easyui-validatebox"  onclick="WdatePicker({el:'endPatrolTimeStr',dateFmt:'yyyy-MM-dd HH:mm:ss' ,minDate:'#F{$dp.$D(\'startPatrolTimeStr\')}'})"  value="<#if patrol.endPatrolTimeStr??>${patrol.endPatrolTimeStr}<#else><#if dateStr??>${dateStr}</#if></#if>"/>
                        </td>
                    </tr>
                         
                    
                   <tr>
                        <td class="LeftTd"><label class="LabName"><span>巡防区域：</span></label>
                        <input name="occurred" id="occurred" maxLength="100" type="text" style="height: 28px;width: 160px;" class="inp1 inp2 easyui-validatebox" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if patrol.occurred??>${patrol.occurred}</#if>"/></td>
            			<td class="LeftTd"><label class="LabName"><span>巡防人数：</span></label>
                        <input id="involvedNum" name="involvedNum" class="easyui-numberspinner" style="width:100px; height:28px;" value="<#if patrol.involvedNum??>${patrol.involvedNum}</#if>" data-options="tipPosition:'bottom',min:1,max:1000,required:true">
                        <span class="Check_Radio" style="float:none;">（人）</span>
                        </td>
            		</tr>
                         
                      <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>巡防情况：</span></label>
                        <textarea name="content" id="content" cols="45" rows="5" style="width: 432px;height: 45px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']"><#if patrol.content??>${patrol.content}</#if></textarea></td>
            		</tr>
            		 <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>处置情况：</span></label>
                        <textarea name="handleResult" id="handleResult" cols="45" rows="5" style="width: 432px;height: 45px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']"><#if patrol.handleResult??>${patrol.handleResult}</#if></textarea></td>
            		</tr>
                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>巡防照片：</span></label>
                        <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
            		</tr>
                     
                    </table>
                    </form>
                    </div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="#" class="BigNorToolBtn SaveBtn" onclick="javascript:tableSubmit('save');">存为草稿</a>
    		<!--
     		<a href="#" onclick="tableSubmit('saveAndReport');" class="BigNorToolBtn ShangBaoBtn">上报</a> 
     		-->
     		<a href="#" class="BigNorToolBtn JieAnBtn"  onclick="tableSubmit('saveAndClose');">结案</a>
    		<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">取消</a>
        </div>
    </div>
</div>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">

	$(function(){
		var typeVal = $("#type").val();
		var patrolId = $("#patrolId").val();
		var urls="${rc.getContextPath()}/zhsq/patrol/patrolDisposalController/selectpatrolType.jhtml?type="+typeVal+"&operator=create";
		if(patrolId!=undefined && patrolId!=null && patrolId!=""){
			urls="${rc.getContextPath()}/zhsq/patrol/patrolDisposalController/selectpatrolType.jhtml?type="+typeVal+"&operator=edit&patrolId="+patrolId;
		}
		$("#detailInfo").panel({
			height:'auto',
			overflow:'no',
			href: urls
		});
		
		//initpatrolTypeComboBox("typeName", "type");
		//AnoleApi.initTreeComboBox("typeName", "type", "A001093199");
		AnoleApi.initGridZtreeComboBox("gridName", "gridId");
		
		<#if patrol.patrolId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'edit',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'bizId':${patrol.patrolId?c},'attachmentType':'${bizType}'},
				ajaxUrl:'${rc.getContextPath()}/zhsq/att/getList.jhtml', 	//获取上传附件的URL （新增页面可不写）	
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
			
		<#else>
			fileUpload({ 
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
				initType:'ajax',//ajax、hidden编辑表单时获取已上传附件列表方式
				context_path:'${rc.getContextPath()}',
				ajaxData: {'eventSeq':1},//未处理
				file_types:'*.jpg;*.gif;*.png;*.jpeg'
			});
		</#if>
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	});
	
	function onGridTreeSelected(gridId, items){
		if(items!=undefined && items!=null && items.length>0){
			var grid = items[0];
			$("#gridCode").val(grid.orgCode);
		} 
	}
	
	function involedPeopleCallback(users) {
		if(users == ""){
			$("#involvedPeopleName").html("");//用于页面显示
			$("#patrolInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
			return;
		}
		var usersDiv = "";
		var userNames = "";
		var userArray = users.split("；");
		if(userArray != ""){
			$.each(userArray, function(i, n){
				var items = n.split("，");
				if(typeof(items[1])!="undefined" ){
					var userName = items[1];
					if(userName.length > 3){//名字显示前三个字
						userName = userName.substr(0, 3);
					}
					usersDiv += "<p title="+items[1]+"("+items[2]+")>"+userName+"<img src='${rc.getContextPath()}/images/sys1_29.png' onclick='removeInvolvedPeople(\""+items[1]+"\",\""+items[2]+"\", $(this).parent());'/></p>";
					userNames += items[1] + "，";
				}
			});
			
			userNames = userNames.substr(0, userNames.length - 1);
			$("#involvedPeopleName").html(usersDiv);//用于页面显示
			$("#patrolInvolvedPeople").val(users);//用于后台保存
			$("#involvedPersion").val(userNames);
		}else{
			$("#involvedPeopleName").html("");//用于页面显示
			$("#patrolInvolvedPeople").val("");//用于后台保存
			$("#involvedPersion").val("");
		}
	}
	
	function tableSubmit(operation){
		if(operation == 'save'){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("#patrolStatus").val("0");
				$("#tableForm").submit();	
			}
		}
		if(operation == 'saveAndReport'){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("#patrolStatus").val("1");
				$("#tableForm").submit();	
			}
		}
		if(operation == 'saveAndClose'){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("#patrolStatus").val("2");
				$("#tableForm").submit();	
			}
		}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function closed(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			modleopen();
			var patrolId = $("#patrolId").val();
			if(patrolId!=undefined && patrolId!=null && patrolId!=""){
				$.ajax({
					type: "POST",
		    		url : '${rc.getContextPath()}/zhsq/patrol/patrolDisposalController/startWorkFlow.jhtml',
					data: 'patrolId='+ patrolId +'&toClose=1',
					dataType:"json",
					success: function(data){
						if(parent.startWorkFlow != undefined){
				   			parent.startWorkFlow(data);
				   		}else{
				   			$.messager.alert('错误','连接错误！','error');
				   		}
					},
					error:function(data){
						$.messager.alert('错误','连接错误！','error');
					}
		    	});
			}else{
				tableSubmit('savepatrol',parent.startWorkFlow, "1");
			}
		}
	}
	
	function showMap() {
		var mapt = $("#mapt").val();
		var x = $("#x").val();
		var y = $("#y").val();
		var data;
		if(x!=''&&y!=''&&mapt!=''){
			data = 'x='+x+'&y='+y+'&mapt='+mapt;
		}else{
			data = 'mapt='+mapt+"&gridId="+$("#gridId").val();
		}
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/toMapArcgisOfAnchorPoint.jhtml?' + data;
		showMaxJqueryWindow("地图标记", url);
	}
	
	function mapMarkerSelectorCallback(mapt,x,y){
		$("#mapt").val(mapt);
		$("#x").val(x);
		$("#y").val(y);
		$("#mapTab").html("已标注");
		$("#mapTab").addClass("local");
		closeMaxJqueryWindow();
	}
	
	function onTreeComboBoxChanged(val){
		this.type = val;
		var patrolId = $("#patrolId").val();
		var urls="${rc.getContextPath()}/zhsq/patrol/patrolDisposalController/selectpatrolType.jhtml?type="+val+"&operator=create";
		if(patrolId!=undefined && patrolId!=null && patrolId!=""){
			urls="${rc.getContextPath()}/zhsq/patrol/patrolDisposalController/selectpatrolType.jhtml?type="+val+"&operator=edit&patrolId="+patrolId;
		}
		$("#detailInfo").panel({
			height:'auto',
			overflow:'no',
			href: urls
		});
	}
</script>
</html>