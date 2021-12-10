<!DOCTYPE html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增巡防</title>
<#include "/component/commonFiles-1.1.ftl" />
<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
</head>
<style>
.inp2{width: 140px;}
.BtnList{width: 350px !important;}
</style>
<body>
<div>
	<div id="content-d" class="MC_con content light" style="overflow-x:hidden">
	                <div class="NorForm NorForm2">
                	<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/internetEnterprise/update.jhtml"  method="post">
                   	<input type="hidden" id="state" name="state">
                   	<table width="100%" border="0" cellspacing="0" cellpadding="0">
               		<tr>
                    	<td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                	</tr>
                   	<input type="hidden" id="patrolStatus" name="status" value="0">
                   	<input type="hidden" name="reqId" value="<#if requestion.reqId??>${requestion.reqId}</#if>"/>
                   <tr>
                        <td colspan="2" class="LeftTd"><label class="LabName"><span>事件类别：</span></label>
	                        <input type="hidden" id="type" name="type" value="<#if requestion.type??>${requestion.type}</#if>">
	                        <input name="typeStr" id="typeStr" type="text" style="height: 28px;width: 20%;" class="inp1 inp2 easyui-validatebox" value="<#if requestion.typeStr??>${requestion.typeStr}</#if>"  data-options="tipPosition:'bottom',required:true,validType:['maxLength[15]','characterCheck']"/>
                        </td>
                       <!-- <td><label class="LabName"><span>提交日期：</span></label>
	                    <input type="text" id="expectTimeStr" name="expectTimeStr" class="inp1 inp2 Wdate easyui-validatebox" style="width:200px; cursor:pointer;" data-options="required:true,validType:['maxLength[100]','characterCheck']" value="<#if requestion.expectTimeStr??>${requestion.expectTimeStr}</#if>" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss', isShowClear:false, isShowToday:false})"  readonly="readonly">
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
                        <td colspan="2" class="LeftTd RightTd"><label class="LabName"><span>附       件：</span></label>
                        <div id="fileupload1" class="ImgUpLoad" style="padding-top:4px;"></div></td>
            		</tr>
            		 <tr>
						<td colspan="2">
							<label class="LabName"><span>备       注：</span></label>
							<textarea name="desc" id="desc" cols="45" rows="5" style="width: 70%;height: 45px;" class="area1 easyui-validatebox" maxLength="1000" data-options="tipPosition:'bottom'"><#if requestion.desc??>${requestion.desc}</#if></textarea></td>
						</td>
					</tr>
                     
                 </table>
              </form>
            </div>
	</div>
	<div class="BigTool">
    	<div class="BtnList" >
    		<a href="#" class="BigNorToolBtn SaveBtn" onclick="save();">保存草稿</a>
    		<a href="#" class="BigNorToolBtn JieAnBtn" onclick="javascript:update();">提交</a>
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
		AnoleApi.initListComboBox("typeStr", "type", "${caseTypeDict}", null, null, {
			ShowOptions : {
				EnableToolbar : true
			}
		});
		$("#content-d").mCustomScrollbar({theme:"minimal-dark"});
	});
	
	
		var swfOpt = {
	    	positionId:'fileupload1',//附件列表DIV的id值',
			type:'edit',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			script_context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			ajaxData: {'bizId':${requestion.reqId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE!}','eventSeq':'1'}
	    };
		fileUpload(swfOpt);
	
	
	/*更新*/
    function update() {

    	var flag = true;
    	$("input[id^='linkMan']").each(function(){
		   if($(this).val()==""||$(this).val()==null){
		   		flag = false;
		   		return false;
		   }
		});
		if(flag){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("2");
				$("#state").val("1");
				$("#tableForm").ajaxSubmit(function(data) {
	  				if(data=="success"){
	  					   parent.searchData();
	  					   parent.$.messager.alert('提示', '更新成功', 'info', function () {
	  					   parent.closeMaxJqueryWindow();
			               });
	  				}else{
	  					 parent.$.messager.alert('提示', '更新失败', 'info', function () {
			                return;
			            });
	  				}
				});
			}
		}else{
			parent.$.messager.alert('提示', '联动单位未配置完全', 'info', function () {
                
            });
		}
    }
    
    
    /*草搞更新*/
    function save() {

    	var flag = true;
    	$("input[id^='linkMan']").each(function(){
		   if($(this).val()==""||$(this).val()==null){
		   		flag = false;
		   		return false;
		   }
		});
		if(flag){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("2");
				$("#state").val('0');
				$("#tableForm").ajaxSubmit(function(data) {
	  				if(data=="success"){
	  					   parent.searchData();
	  					   parent.$.messager.alert('提示', '更新成功', 'info', function () {
	  					   parent.closeMaxJqueryWindow();
			               });
	  				}else{
	  					 parent.$.messager.alert('提示', '更新失败', 'info', function () {
			                return;
			            });
	  				}
				});
			}
		}else{
			parent.$.messager.alert('提示', '联动单位未配置完全', 'info', function () {
                
            });
		}
    }
    
    //关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
</script>
</html>