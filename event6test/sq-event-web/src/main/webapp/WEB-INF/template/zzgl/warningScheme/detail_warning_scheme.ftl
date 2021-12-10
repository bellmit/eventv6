<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>预警方案管理 详情</title>
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
								<textarea style="width:300px;font-size:12px;border:none" readonly="readonly" name="remark" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[100]','characterCheck']" >${schemeMatch.schemeName!}</textarea>
							</li>
							<li class="xw-com2" style="margin-bottom:5px;display:none">
								<span class="fw-from1">业务模块：</span>
								<input id="bizType" name="bizType" value="${schemeMatch.bizType!}" type="text" class="queryParam hide"/>
								<input id="bizTypeName" style="font-size:12px" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',required:true" />
							</li>
							<li class="xw-com1" style="width:51%">
								<span class="fw-from1">备注：</span>
								<textarea style="width:300px;font-size:12px;border:none" readonly="readonly" name="remark" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[200]','characterCheck']" >${schemeMatch.remark!}</textarea>
							</li>
							
						</ul>
				    </div>
				    
				    <div id="schemeKeywordInfo" style="width:100%">
				        <div id="list_title" style="margin:0 auto;width:96%;margin-left:45px">
				            <table id="list"></table>
				        </div>
				        <div id="list_body"></div>
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
	}];
	
	var flag=0;
	
		$(function () {
		
		    <#if dicts??>
				<#list dicts as dic>
				    col_second_part.push({});
				    col_second_part[col_num].field='value_'+"${dic.dictGeneralCode}";
				    col_second_part[col_num].title="${dic.dictName}";
				    col_second_part[col_num].align='center';
				    col_second_part[col_num].width=tr_width;
				    col_second_part[col_num].formatter=function(value){
				                                 if(value){
				                                     return '<textarea readonly="readonly" style="width:'+tr_width+'px;font-size:12px;border:none;readonly:readonly" name="remark" class="textfrom flex1 easyui-validatebox" >'+value+'</textarea>'
				                                 }else{
                                                     return '<textarea readonly="readonly" style="width:'+tr_width+'px;font-size:12px;border:none;readonly:readonly" name="remark" class="textfrom flex1 easyui-validatebox" ></textarea>'
				                                 }
                                             }
				    var inputName='code_'+"${dic.dictGeneralCode}";
				    inputNameArr.push(inputName);
				    
				    col_num+=1;
				</#list>
			</#if>
			
			<#list keywordList as map>
	
	            <#list map?keys as itemKey>  
	            griddata[0]["${itemKey}"]="${map[itemKey]}";
                </#list>
	
	        </#list>
		
		    buildTable();
		    
			<#if msgWrong??>
				$.messager.alert('错误', '${msgWrong!}', 'error');
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
	
		}
		
	</script>

</html>