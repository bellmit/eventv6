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
.BtnList{width: 350px !important;}
</style>
<body>
<div>
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
	                <div class="NorForm NorForm2">
                	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/internetEnterprise/pcSave.jhtml"  method="post">
                   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
                   	<input type="hidden" id="patrolStatus" name="patrolStatus" value="0">
                   	<input type="hidden" id="state" name="state">
                   	<tr>
                    	<td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                	</tr>
                   <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>事件类别：</span></label>
                        <input type="hidden" id="type" name="type" value="<#if requestion.type??>${requestion.type}</#if>">
                        <input name="typeName" id="typeName" type="text" style="height: 28px;width: 20%;" class="inp1 inp2 easyui-validatebox" value="<#if requestion.typeName??>${requestion.typeName}</#if>" data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                        <!-- <td><label class="LabName"><span>提交日期：</span></label>
	                    <input type="text" id="expectTimeStr" name="expectTimeStr" class="inp1 inp2 Wdate easyui-validatebox" style="width:80%; cursor:pointer;" data-options="required:true,validType:['maxLength[100]','characterCheck']" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
                        </td> -->
                   </tr>
                      
                      <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>标题：</span></label>
                        <input name="title" id="title" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 70%;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if requestion.title??>${requestion.title}</#if>"/></td>
            		 </tr>
                        
                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>事件描述：</span></label>
                        <textarea name="content" id="content" cols="45" rows="5" style="width: 70%;height: 45px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom',required:true,validType:['maxLength[1000]','characterCheck']"><#if requestion.content??>${requestion.content}</#if></textarea></td>
            		</tr> 
            		
            		 <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>企业名称：</span></label>
                        <input name="reqObjName" id="reqObjName" maxLength="100" type="text" class="inp1 easyui-validatebox" style="width: 70%;" data-options="tipPosition:'bottom',required:true,validType:['maxLength[100]','characterCheck']" value="<#if requestion.reqObjName??>${requestion.reqObjName}</#if>"/></td>
            		 </tr>
            		 
                    <tr>
                        <td class="LeftTd"><label class="LabName"><span>联系人：</span></label>
                        <input name="linkMan" id="linkMan" type="text" style="height: 28px;width: 40%;" class="inp1 inp2 easyui-validatebox" value="<#if requestion.linkMan??>${requestion.linkMan}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                        <td><label class="LabName"><span>联系方式：</span></label>
                        <input name="linkTel" id="linkTel" type="text" style="height: 28px;width: 40%;" class="inp1 inp2 easyui-validatebox" value="<#if requestion.linkTel??>${requestion.linkTel}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','mobileorphone']"/>
                       
                        </td>
                   </tr>
                    
            		<tr>
                    	<td class="LeftTd" colspan="2"><div class="title FontDarkBlue">附件信息</div></td>
                	</tr>
                    <tr>
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>文件上传：</span></label>
                        <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
            		</tr>
                     <tr>
					<td colspan="2">
						<label class="LabName"><span>备       注：</span></label>
						<textarea name="desc" id="desc" cols="45" rows="5" style="width: 70%;height: 45px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom'"><#if requestion.content??>${requestion.content}</#if></textarea></td>
					</td>
				</tr>
                    </table>
                </form>
            </div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    		<a href="#" class="BigNorToolBtn SaveBtn" onclick="tableSubmit('saveAndReport');">保存草稿</a>
    	 	<a href="#" class="BigNorToolBtn JieAnBtn"  onclick="tableSubmit('save');">提交</a>
    		<a href="#" class="BigNorToolBtn CancelBtn" onclick="javascript:cancl();">关闭</a>
        </div>
    </div>
</div>
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/customEasyWin.ftl" />
</body>

<script type="text/javascript">

	$(function() {
		//加载数据字典：案件类型
		AnoleApi.initListComboBox("typeName", "type", "${caseTypeDict}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
	});
	
	$(function(){
		<#if requestion.regId??>
			fileUpload({
				positionId:'fileupload',//附件列表DIV的id值',
				type:'add',//add edit detail
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
	
	
	function tableSubmit(operation){
		if(operation == 'save'){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("#status").val("1");
				$("#state").val("1");
				$("#tableForm").submit();	
			}
		}
		if(operation == 'saveAndReport'){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("#status").val("1");
				$("#state").val("0");
				$("#tableForm").submit();	
			}
		}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	//-- 供子页调用的重新载入数据方法
	function reloadDataForSubPage(result){
		closeMaxJqueryWindow();
		$.messager.alert('提示', result, 'info');
		$("#list").datagrid('load');
	}
	
</script>
</html>