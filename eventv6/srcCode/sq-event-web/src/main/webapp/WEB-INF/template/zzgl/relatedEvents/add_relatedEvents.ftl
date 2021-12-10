<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>涉及业务案件采集事件</title>
<#include "/component/standard_common_files-1.1.ftl" />
<#include "/map/arcgis/arcgis_base/arcgis_cross_domain/map_labeling.ftl" />
<#include "/component/FieldCfg.ftl" />
<style type="text/css">
.combo-arrow{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/images/sys_07.png") no-repeat center center;}
.datebox .combo-arrow:hover{background: url("${SQ_ZZGRID_URL}/js/jquery-easyui-1.4/themes/gray/images/datebox_arrow.png") no-repeat center center;}
.combo-arrow{opacity:1;}
.textbox-icon{opacity:1;}
.combo{vertical-align:top;}
.combo:hover{border:1px solid #7ecef4; box-shadow:#7ecef4 0 0 5px;}
.LabName span{padding-right: 5px;}
.inp145{width: 145px !important}
</style>
</head>
<body>
	<form id="tableForm" name="tableForm" action="" method="post" enctype="multipart/form-data">
		<input type="hidden" id="id" name="id" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="markerOperation" name="markerOperation" value="<#if relatedEvents.reId??>1<#else>0</#if>" />
		<input type="hidden" id="gridId" name="gridId" value="<#if gridId??>${gridId?c}</#if>" />
		<input type="hidden" id="module" name="module" value="<#if module??>${module}</#if>" />
		<input type="hidden" id="reId" name="reId" value="<#if relatedEvents.reId??>${relatedEvents.reId?c}</#if>" />
		<input type="hidden" id="bizType" name="bizType" value="<#if relatedEvents.bizType??>${relatedEvents.bizType}</#if>" />
		<input type="hidden" id="bizId" name="bizId" value="<#if relatedEvents.bizId??>${relatedEvents.bizId?c}</#if>" />
		<input type="hidden" id="bizName" value="" />
		<input type="hidden" id="isDetection" name="isDetection" value="<#if relatedEvents.isDetection??>${relatedEvents.isDetection}<#else>0</#if>" />
		
		<div id="content-d" class="MC_con content light">
			<div class="NorForm" style="width:784px;">
				<div class="fl" >
					<table  style="width:798px;"  border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2" class="LeftTd">
								<label class="LabName"><span><font color="red">*</font>案件名称：</span></label><input type="text" class="inp1 easyui-validatebox" data-options="required:true,validType:['maxLength[100]','characterCheck']" name="reName" id="reName" value="<#if relatedEvents.reName??>${relatedEvents.reName}</#if>" style="width:400px;" />
							</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><font color="red">*</font>所在地段：</span></label><input class="easyui-combobox" type="text" id="lotId" data-options="required:true" style="width: 155px; height:28px" />
							</td>
							<td>
				    			<label class="LabName"><span>是否破案：</span></label><div class="Check_Radio"><input type="checkBox" id="isDetectionCheck" onclick="changeDetectionValue();" <#if (relatedEvents.isDetection?? && relatedEvents.isDetection=='1')>checked</#if>/><label for="isDetectionCheck" style="cursor:pointer;">是</label></div>
				    		</td>
						</tr>
				    	<tr>
							<td class="LeftTd">
								<label class="LabName"><span><font color="red">*</font>发生日期：</span></label><input type="text" class="easyui-datetimebox easyui-validatebox" editable="false" style="width: 155px;height: 28px;" id="occuDateStr" name="occuDateStr" value="<#if relatedEvents.occuDateStr??>${relatedEvents.occuDateStr}</#if>"/>
							</td>
							<td>
				    			<label class="LabName"><span><label style="color:red;">*</label>主犯姓名：</span></label><input type="text" style="width:130px;" class="inp1 easyui-validatebox" data-options="required:true,validType:['maxLength[32]','characterCheck']" name="prisonersName" id="prisonersName" value="<#if relatedEvents.prisonersName??>${relatedEvents.prisonersName}</#if>" />
				    		</td>
						</tr>
						<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label style="color:red;">*</label>发生地点：</span></label><input type="text" class="inp1 easyui-validatebox" data-options="required:true,validType:['maxLength[100]','characterCheck']" name="occuAddr" id="occuAddr" value="<#if relatedEvents.occuAddr??>${relatedEvents.occuAddr}</#if>" style="width:400px;" />
							</td>
							<td>
					    		<label class="LabName"><span><font color="red">*</font>证件类型：</span></label>
					    		<input type="hidden" name="prisonersDocType" id="prisonersDocType" value="<#if relatedEvents.prisonersDocType??>${relatedEvents.prisonersDocType}</#if>" />
					    		<input type="text" class="inp1" id="prisonersDocTypeName" style="width:130px;"  value="<#if relatedEvents.prisonersDocTypeName??>${relatedEvents.prisonersDocTypeName}</#if>" />
				    		</td>
						</tr>
						<tr>
				    		<td class="LeftTd">
				    			<label class="LabName"><span>地理标注：</span></label>
				    			<#include "/map/arcgis/arcgis_map_marker/map_marker_div.ftl"/>
				        	</td>
							<td >
				    			<label class="LabName"><span><label style="color:red;">*</label>证件号码：</span></label><input type="text" style="width:130px;"  class="inp1 easyui-validatebox" data-options="required:true,validType:['maxLength[18]','characterCheck']" name="prisonersDocNo" id="prisonersDocNo" value="<#if relatedEvents.prisonersDocNo??>${relatedEvents.prisonersDocNo}</#if>" />
				    		</td>
				        </tr>
				    	<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label style="color:red;">*</label>案件性质：</span></label>
								<input type="hidden" id="nature" name="nature" value="<#if relatedEvents.nature??>${relatedEvents.nature}</#if>" />
								<input type="text" class="inp1 easyui-validatebox" data-options="required:true" style="width: 155px;" id="natureName" value="<#if relatedEvents.natureName??>${relatedEvents.natureName}</#if>" />
					    	</td>
							<td>
				    			<label class="LabName"><span><font color="red">*</font>在逃人数：</span></label><input type="text" class="inp1 easyui-numberspinner" style="width:135px;"   data-options="required:true,min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum();},onSpinDown:function(){countCrimeNum();},onChange:function(){countCrimeNum();}" name="ecapeNum" id="ecapeNum" value="<#if relatedEvents.ecapeNum??>${relatedEvents.ecapeNum?c}<#else>0</#if>" style="width:114px; height:28px;" />
				    		</td>
					    </tr>
				    	<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label style="color:red;">*</label>案件情况：</span></label><textarea name="situation" id="situation" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[4000]','characterCheck']" style="width:400px;height:100px;resize:none;"><#if relatedEvents.situation??>${relatedEvents.situation}</#if></textarea>
							</td>
							<td >
				    			<label class="LabName"><span><font color="red">*</font>抓捕人数：</span></label><input type="text" class="inp1 easyui-numberspinner" style="width:135px;"  data-options="required:true,min:0,validType:'maxLength[5]',onSpinUp:function(){countCrimeNum();},onSpinDown:function(){countCrimeNum();},onChange:function(){countCrimeNum();}" name="arrestedNum" id="arrestedNum" value="<#if relatedEvents.arrestedNum??>${relatedEvents.arrestedNum?c}<#else>0</#if>" style="width:114px; height:28px;" />
				    		</td>
						</tr>
				    	<tr>
							<td class="LeftTd">
								<label class="LabName"><span><label style="color:red;">*</label>侦破情况：</span></label><textarea name="detectedOverview" id="detectedOverview" cols="" rows="" class="area1 easyui-validatebox" data-options="required:true,validType:['maxLength[4000]','characterCheck']" style="width:400px;height:100px;resize:none;"><#if relatedEvents.detectedOverview??>${relatedEvents.detectedOverview}</#if></textarea>
							</td>
							<td>
				    			<label class="LabName"><span><font color="red">*</font>作案人数：</span></label><input type="text" class="inp1 easyui-numberspinner" style="width:135px;"  data-options="min:0,validType:'maxLength[6]'" name="crimeNum" id="crimeNum" value="<#if relatedEvents.crimeNum??>${relatedEvents.crimeNum?c}<#else>0</#if>" style="width:114px; height:28px;" /></li>
				    		</td>
						</tr>
				    </table>
				</div>
				
			</div>
		</div>
		<div class="BigTool">
        	<div class="BtnList">
        		<a href="#" onclick="tableSubmit('saveRelatedEvents');" class="BigNorToolBtn BigJieAnBtn">保存</a>
				<a href="#" onclick="cancel();" class="BigNorToolBtn CancelBtn">取消</a>
            </div>
        </div>	
	</form>
	
	<#include "/component/ComboBox.ftl" />
	
</body>

<script type="text/javascript">
	var queryValue = "";
	var lotUrl = "${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/listCareRoads.jhtml?lotName=";
	
	$(function(){
	    $(window).load(function(){ 
	        var options = { 
	            axis : "yx", 
	            theme : "minimal-dark" 
	        }; 
	        enableScrollBar('content-d',options); 
	    }); 
	    
	    layer.load(0);// 加载遮罩层
		$.excuteFieldCfg({
			moduleCode: "related_road_events"// 必传，模块编码
		}, function(isSuccess, msg) {// 回调函数，isSuccess：true成功/false失败
			layer.closeAll('loading'); // 关闭加载遮罩层
			if (!isSuccess) {
				$.messager.alert('警告', msg, 'warning');
			}
		});
		
		$('#lotId').combobox({ 
			url:lotUrl,
            valueField:'lotId', 
            textField:'lotName',
            keyHandler:{
            	enter:function(){
            		$('#lotId').combobox("reload", lotUrl+encodeURI(queryValue));
            	},
            	query:function(queryVal){
            		queryValue = queryVal;
            		/*if(queryValue == ""){
            			$('#lotId').combobox("reload", lotUrl+encodeURI(queryVal));
            		}*/
            	}
            },
            onSelect: function(record){
				queryValue = record.lotName;
				changeBiz(record);
            },
            onLoadSuccess: function(){ //加载完成后,保留原有输入内容
            	if(queryValue == ""){
               		$('#lotId').combobox("select",$("#bizId").val());
                }else{
                	$('#lotId').combobox("setText", queryValue);//为了保留手动输入的查询内容
                }
            }
        });
        
		
		$("#lotId").next().children(":text").on("blur", function() {
        	queryValue = $('#lotId').combobox("getText");
        	$('#lotId').combobox("reload", lotUrl);
        	//$('#lotId').combobox("reload", lotUrl+encodeURI(queryValue));
        	
        	if(queryValue != ""){
    			var bizName = $("#bizName").val();
				queryValue = bizName;
				$('#lotId').combobox("setText", bizName);
    		}
		});
		
        AnoleApi.initListComboBox("prisonersDocTypeName", "prisonersDocType", "${prisonersDocTypePcode}", null, [<#if relatedEvents.prisonersDocType??>"${relatedEvents.prisonersDocType}"<#else>"1"</#if>]);
        AnoleApi.initListComboBox("natureName", "nature", "${naturePcode}", null, [<#if relatedEvents.nature??>"${relatedEvents.nature}"<#else>"1"</#if>]);

        $("#ecapeNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });
        $("#arrestedNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });
        $("#crimeNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });
	});
	
	function changeDetectionValue(){
		var isDetection = $("#isDetectionCheck").is(":checked");
		if(isDetection){
			$("#isDetection").val(1);
		}else{
			$("#isDetectionCheck").removeAttr("checked");
			$("#isDetection").val(0);
		}
	}
	
	function countCrimeNum(){
		var ecapeNum = $("#ecapeNum").val();
		var arrestedNum = $("#arrestedNum").val();
		var crimeNum = 0;
		
		if(ecapeNum == ''){
			ecapeNum = 0;
		}
		
		if(arrestedNum == ''){
			arrestedNum = 0;
		}
		
		crimeNum = parseInt(ecapeNum, 10) + parseInt(arrestedNum, 10);
		$("#crimeNum").val(crimeNum);
		$("#crimeNum").numberspinner('setValue', crimeNum);
	}
	
	function changeBiz(record){
		if(record != null){
			var lotId = record.lotId;
			var lotName = record.lotName;
			
			if(lotId!=undefined && lotId!=""){
				$("#bizId").val(lotId);
			}
			
			if(lotName!=undefined && lotName!=""){
				$("#bizName").val(lotName);
			}
		}
	}
	
	function checkExtraValidate(){//额外的字段检测
		var docType = $("#prisonersDocType").val();
		var bizId = $("#bizId").val();
		var flag = true;
		var msg = "";
		
		if(docType == '1'){
			var docNo = $("#prisonersDocNo").val(); 
			flag = checkIdCard(docNo);
			msg = "身份证号码不合法！";
		}
		
		if(bizId == ""){
			$("#lotId").combobox("setText", "");//清空无效的手动输入
			flag = false;
			if(queryValue!=null && queryValue!=""){
				msg = '您输入的所在地段“'+queryValue+'”不存在！请重新选择！';
			}
		}
		
		if(!flag && msg!=""){
			$.messager.alert('警告',msg,'warning');
		}
		return flag;
	}
	
	function tableSubmit(m){
		var isValid =  $("#tableForm").form('validate');
		var isNoValid = checkExtraValidate();
		if(isValid && isNoValid){
			var msg = "新增";
			<#if relatedEvents.reId??>
				m = "editRelatedEvents";
				msg = "编辑";
			</#if>
			$("#tableForm").attr("action","${rc.getContextPath()}/zhsq/relatedEvents/RelatedEventsController/"+m+".jhtml");
	      	modleopen();
		  	$("#tableForm").ajaxSubmit(function(data) {
  				if(data.result){
  					msg += "成功！";
  				}else{
  					msg += "失败！";
  				}
  				parent.flashData(msg, "1");
			});
	  	}
	}
	
	function cancel(){
		parent.closeMaxJqueryWindow();
	}
	
	function showMap() {
		var callBackUrl = '${SQ_ZHSQ_EVENT_URL}/zhsq/map/arcgis/arcgis/toArcgisCrossDomain.jhtml';
		var width = $(window).width() - 15;
		var height = $(window).height() - 15;
		var gridId = $("#gridId").val();
		var markerOperation = $('#markerOperation').val();
		var mapType = $("#module").val();
		var isEdit = true;
		var parameterJson = getParameterJson();
		showMapWindow(gridId,callBackUrl,width,height,isEdit,mapType,parameterJson,mapType);
	}
	
	function getParameterJson() {
		var parameterJson = {
			"id": $("#id").val(),
			"name": $("#name").val()
		}
		return parameterJson;
	}
</script>
</html>