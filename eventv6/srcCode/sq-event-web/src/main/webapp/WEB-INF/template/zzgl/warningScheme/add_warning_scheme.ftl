<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>预警方案管理 新增/编辑</title>
		<!-- 扫黑除恶样式 -->
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
		<#include "/component/commonFiles-1.1.ftl" />
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
		
		
		<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
		
		<style>
		.datagrid-row-selected{background: #fff;color: #000;}
		.datagrid-row-over{background: #fff;color: #000;}
		.area1 {
                width: 98%;
                height: 92px;
        }
        .datagrid-body{height:120px;}
        .fixed{ position:fixed; left:0px; bottom:0px; width:100%; z-index:9999;}
		</style>
	</head>
	<body>
		<form id="schemeMatchForm" name="schemeMatchForm" action="" method="post" enctype="multipart/form-data">
			<!-- 主键Id -->
			<input type="hidden" id="schemeId" name="schemeId" value="<#if schemeMatch.schemeId??>${schemeMatch.schemeId?c}</#if>" />
			<input id="updateTimeStr" name="updateTimeStr" value="<#if schemeMatch.updateTime??>${schemeMatch.updateTime?string("yyyy/MM/dd HH:mm:ss")}</#if>" type="hidden"/>

			<div class="container_fluid  fw-main">
				<div class="form-warp-sh" style="padding-top:10px"><!-- 外框 -->
				    <div id="schemeMatchInfo" >
				        <ul class="fw-xw-from clearfix ">
							<li class="xw-com1" style="margin-bottom:5px;width:51%">
								<span class="fw-from1"><i class="spot-xh">*</i>方案名：</span>
								<input type="text" id="schemeName" value="${schemeMatch.schemeName!}" style="font-size:12px;color:#000" name="schemeName" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',required:true,validType:'maxLength[80]'"/>
							</li>
							<li class="xw-com2" style="margin-bottom:5px;display:none">
								<span class="fw-from1">业务模块：</span>
								<input id="bizType" name="bizType" value="${schemeMatch.bizType!}" type="text" class="queryParam hide"/>
								<input id="bizTypeName" style="font-size:12px" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',required:true" />
							</li>
							<li class="xw-com1" style="width:51%">
								<span class="fw-from1">备注：</span>
								<textarea style="width:300px;font-size:12px" name="remark" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" >${schemeMatch.remark!}</textarea>
							</li>
							
						</ul>
				    </div>
				    
				    <div id="schemeKeywordInfo" style="width:100%">
				        <div id="list_title" style="margin:0 auto;width:96%;margin-left:45px">
				            <table id="list"></table>
				        </div>
				        <div id="list_body"></div>
				    </div>
				    <div class="fw-toptitle" style="position:unset">
				        <h6 class="note-s" style="margin-left:26px">多个关键词用中文逗号"，"分隔</h6>
			        </div>
				    
					
					<!-- 操作按钮 -->
					<div id="btnDiv" class="btn-warp fixed">
						<a class="btn-bon green-btn" onclick="saveData();">保存</a>
						<a class="btn-bon blue-btn" onclick="returnPage();">取消</a>
					</div>
				</div>
			</div>
		</form>
	</body>
	
	<script type="text/javascript">
	
	var col_num=0;
	
	var tr_width=$("#list_title").width()/5;
	
	var col_second_part=[];
	
	var inputNameArr=[];
	
	var griddata=[{
	    createT:'关键词'
	}]
	
		$(function () {
		
		    <#if dicts??>
				<#list dicts as dic>
				    col_second_part.push({});
				    col_second_part[col_num].field='value_'+"${dic.dictGeneralCode}";
				    col_second_part[col_num].title="${dic.dictName}";
				    col_second_part[col_num].align='center';
				    col_second_part[col_num].width=tr_width;
				    col_second_part[col_num].height=100;
				    var inputName='code_'+"${dic.dictGeneralCode}";
				    inputNameArr.push(inputName);
				    
				    col_second_part[col_num].formatter=function(value){
				                                 if(value){
				                                     return '<textarea class="area1 easyui-validatebox" data-options="tipPosition:'+'\'bottom\''+',validType:['+'\'maxLength[100]\''+','+'\'characterCheck\''+']" id="'+'code_'+'${dic.dictGeneralCode}'+'" cols="'+tr_width+'" rows="2" style="margin-top:12px" align-text="center">'+value+'</textarea><input type="hidden" name="'+'code_'+'${dic.dictGeneralCode}'+'"><input type="hidden" name="'+'keyid_'+'${dic.dictGeneralCode}'+'">'
				                                 }else{
                                                     return '<textarea class="area1 easyui-validatebox" data-options="tipPosition:'+'\'bottom\''+',validType:['+'\'maxLength[100]\''+','+'\'characterCheck\''+']" id="'+'code_'+'${dic.dictGeneralCode}'+'" cols="'+tr_width+'" rows="2" style="margin-top:12px" align-text="center"></textarea><input type="hidden" name="'+'code_'+'${dic.dictGeneralCode}'+'"><input type="hidden" name="'+'keyid_'+'${dic.dictGeneralCode}'+'">'
				                                 }
                                             }
				    col_num+=1;
				</#list>
			</#if>
			
		    <#if keywordList?? && (keywordList?size>0)>
		    <#list keywordList as map>
	
	            <#list map?keys as itemKey>  
	            griddata[0]["${itemKey}"]="${map[itemKey]}";
                </#list>
	
	        </#list>
	        </#if>
	        
	        
		    buildTable();
		    //方案应用模块
            AnoleApi.initTreeComboBox("bizTypeName", "bizType", "A001093098", null, ["01"], {
                ChooseType : "1",
                ShowOptions:{
                    EnableToolbar : true
                }
            });
			
		
			
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
			</#if>
			
			<#if keyidList?? && (keyidList?size>0)>
		    <#list keyidList as map>
	
	            <#list map?keys as itemKey>  
	                $("input[name="+"${itemKey}"+"]").val("${map[itemKey]}");
                </#list>
	
	        </#list>
	        </#if>
		});
		
		function buildTable(){
		
		$('#list').datagrid({
			fitColumns: true, //自适应宽度
			nowrap: true,
			striped: true,
			singleSelect: true,
			data:griddata,
			columns: [
			[
				{field:'createT',title:'条件项', rowspan:2,align:'center', width:tr_width},
				{ title:'判定值',colspan:col_num, align:'center'}
			],col_second_part],
			//pagination: true,
			//pageSize: 20,
			//toolbar: '#jqueryToolbar',
            onLoadError: function() {
                listError();
            },
            onClickRow:function(index, row){
            
            }
		});
		
		 for(var i=0,j=inputNameArr.length;i<j;i++){
		    $("#"+inputNameArr[i]).validatebox();
		}
	

           
		}
		
		function saveData(){
		
		var isValid = $("#schemeMatchForm").form('validate');
		
        if(isValid){
        
	    $("#schemeMatchForm").attr("action", "${rc.getContextPath()}/zhsq/warningScheme/saveOrUpdatScheme.jhtml");
		
		for(var i=0,j=inputNameArr.length;i<j;i++){
		    $("input[name="+inputNameArr[i]+"]").val($("#"+inputNameArr[i]).val());
		}
		
		$("#schemeMatchForm").ajaxSubmit(function (data) {
					if (data.result && data.result == true) {
					    if($("#schemeId").val()>0){
					        parent.reloadDataForSubPage("更新成功",true);
					    }else{
					        parent.reloadDataForSubPage("新增成功",false);
					    }
						
					} else {
						$.messager.alert('提示',data.tipMsg,'info');
					}
		});
        }

		
		
		}
		
		function returnPage(){
		    parent.returnSubPage();
		}
		
	
	</script>

</html>