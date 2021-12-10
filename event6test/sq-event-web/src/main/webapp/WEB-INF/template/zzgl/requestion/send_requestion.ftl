<!DOCTYPE html>
<html>
<head>
	<title>办理页面</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<link href="${rc.getContextPath()}/css/zhsq_event.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
	<style>
        .leftTitle {
            width: 190px;
            height: 55px;
            line-height: 55px;
            border-bottom: 1px solid #fff;
            text-align: center;
            vertical-align: middle;
            background-color: #e1e1e1;
            cursor: pointer;
        }

        .leftNow {
            background-color: #fff;
        }

        .leftTitle span {
            font-size: 14px;
            color: #333333;
            font-weight: 700;
        }

        .LeftTd2 {
            text-align: center;
            background-color: #66ccff;
            border-bottom: 1px solid #cecece;
            width: 100px;
        }

        .LeftTd3 {
            text-align: center;
            background-color: #fff;
            border-bottom: 1px solid #cecece;
            width: 100px;
        }

        .LabName2 {
            color: #333333;
            font-weight: 400;
            text-align: center;
        }

        .LabName3 {
            color: #333333;
            font-weight: 400;
            text-align: center;
            width: 120px;
        }

        .LabName4 {
            width: 194px;
            text-align: center;
        }

        .list th, .list td {
            border: 1px solid #BED3DF; /* 单元格边框 */
            padding-top: 2px;
            padding-bottom: 2px;
            font-size: 14px;
            padding-left: 2px;
            padding-right: 2px;
            height: 30px;
            border-bottom: 1px solid #9E9E9E;
            font-weight: bold;
            padding-top: 8px;
            text-align: center;
        }
    </style>
</head>
<body>
	<div class="MC_con content light">
		<div class="ConList">
	     	<div class="nav" id="tab" style="margin-top:10px;">
		        <ul>
		            <li class="current">基本情况</li>
		            <li>办理详情</li>
		        </ul>
	    	</div>
	 	</div>
	 	<div name="tab" id="content0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td>
						<label class="LabName"><span>事件类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.typeStr)!}</span>
					</td>
					<td>
						<label class="LabName"><span>提交时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatTimeStr)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>标      题：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.title)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>事件描述：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.content)!}</span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>企业名称：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.reqObjName)!}</span>
					</td>
				</tr>
				<tr>
					<td>
						<label class="LabName"><span>联  系 人：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkMan)!}</span>
					</td>
					<td>
						<label class="LabName"><span>联系方式：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.linkTel)!}</span>
					</td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">附件信息</div></td>
                </tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>附       件：</span></label>
						<div id="fileupload1" class="ImgUpLoad" style="padding-top:4px;"></div></td>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>备       注：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.desc)!}</span>
					</td>
				</tr>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">受理信息<a class="NorToolBtn" style="margin-left:10px;font-size:14px;float:right;padding:4px 7px 7px 7px;color:rgb(255, 255, 255);" href="###" onclick="showBackList('${bo.reqId}');">查看驳回信息</a></div></td>
                </tr>
			</table>
			<input type="hidden" id="caseTypeTime" name="caseTypeTime" value="${caseTypeTime}"/>
		<form id="tableForm" name="tableForm" action="${rc.getContextPath()}/zhsq/requestion/submit.jhtml"  method="post">
			<input type="hidden" name="buttonType" value=""/>
			<input type="hidden" name="taskId" value="${taskId}"/>
			<input type="hidden" name="nextNodeName" value="end1"/>
			<input type="hidden" name="instanceId" value="${instanceId}"/>
			<input type="hidden" name="reqId" value="${bo.reqId}"/>
			<table class="list" id="list_5" border="0" cellspacing="0" cellpadding="0" >
                <tr style="background: center bottom #f4f4f4;">
               	   <th style="border:1px solid #bed3df ;width: 190px;" ><a style="margin-left:10px;font-size:14px;" href="###" class="NorToolBtn AddBtn" onclick="addRec(5,'list_5')">新增</a>操作</th>
                   <th style="border:1px solid #bed3df ;width: 150px;" >联动单位</th>
                   <th style="border:1px solid #bed3df ;width: 160px;" >联络员</th>
                   <th style="border:1px solid #bed3df ;width: 160px;" >分管领导</th>
                   <th style="border:1px solid #bed3df ;width: 120px;" >状态</th>         
                </tr>
                <#if childList ?? && (childList?size > 0)>
	                <#list childList as line>
	                    <tr class="childList">
	                    	<input status="${(line.wfStatus)!}" id="gridId${(line.linkageUnitId)!}" name="temp_gridId" value="${(line.linkageUnitId)!}" class="hide queryParam" />
	                        <td>
	                        	<a style="margin-left:10px;font-size:14px;" href="###" class="NorToolBtn DelBtn" gridId="${(line.linkageUnitId)!}" rluId="${(line.rluId)!}" onclick="delRecList(this,'list_5')">删除</a>
	                        	<#if line.state=='9' || line.state=='6'>
	                        		<a style="margin-left:10px;font-size:14px;" nodeId="${(line.rluId)!}" href="###" class="NorToolBtn AddBtn" onclick="backRec(this,'list_5')">评价</a>
			                	</#if>
			                	<#if line.state=='8'>
	                        		<a style="margin-left:10px;font-size:14px;" nodeId="${(line.rluId)!}" id="a_${(line.wfInstanceId)!}" wfInstanceId="${(line.wfInstanceId)!}" href="###" class="NorToolBtn SubmitBtn" onclick="subRec(this,'list_5')">提交</a>
			                	</#if>
			                </td>
			                <td>
			                	<span class=" FontDarkBlue" >${(line.linkageUnitName)!}</span>
			                </td>
			                <td>
			                	<span class=" FontDarkBlue" >${(line.linkMan)!}(${(line.linkManTel)!})</span>
	                        </td>
	                        <td>
	                        	<span class=" FontDarkBlue" >${(line.leaderName)!}(${(line.leaderTel)!})</span>
	                        </td>
	                        <td>
	                        	<i title="查看详情" class="childShow" style="padding-right: 15px;" onClick="toShowReqLink('${(line.rluId)!}')"></i>
	                        	<#if line.state=='9'><span id="span${(line.rluId)!}">已归档</span>
	                        	<#elseif line.state=='8'><span id="span${(line.rluId)!}">单位退回</span>
	                        	<#elseif line.state=='7'><span id="span${(line.rluId)!}">单位办理</span>
	                        	<#elseif line.state=='6'><span id="span${(line.rluId)!}">已评价</span>
	                        	<#else><span id="span${(line.rluId)!}"></span></#if>
	                        </td>
	                    </tr>
	                    <tr>
			                <td style="background: center bottom #f4f4f4;">处理期限</td>
			                <td>${(line.limitDate)!}</td>
			                <td>天（工作日）</td>
			                <td style="background: center bottom #f4f4f4;">截止日期</td>
			                <td>${(line.overTime)!}</td>
		                </tr>
	                </#list>
	            </#if>
           </table>
           <tr>
                <td colspan="5" class="LeftTd RightTd"><label class="LabName"><span>附件：</span></label>
                <div id="fileupload" class="ImgUpLoad" style="padding-top:4px;"></div></td>
    		</tr>
           <tr>
				<td colspan="5">
					<label class="LabName"><span>受理意见：</span></label>
					<textarea name="detectedOverview" id="detectedOverview" cols="" rows="" class="area1 easyui-validatebox" data-options="validType:['maxLength[1000]','characterCheck']"  style="width:650px;float: left;margin-bottom:4px;margin-top:4px;"></textarea>
				</td>
			</tr>
		</form>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center" style="width: 120px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理环节</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理信息</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>处理意见</span></label>
					</td>
					<td align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>附件</span></label>
					</td>
				</tr>
				<#list taskList as task>
				    <tr>
				        <td align="center" style="vertical-align: middle;border: 1px solid #cecece;">${(task.TASK_NAME)!}<#if task.OPERATE_TYPE=='2'><br/>（驳回 ）<br/></#if></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">办理人：${(task.TRANSACTOR_NAME)!}<br/>办理时间：${(task.END_TIME)!}</td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;" title="${(task.REMARKS)!}"><div style="white-space:break-word;text-overflow:ellipsis;overflow:hidden;width:220px;">${(task.REMARKS)!}</div></td>
				        <td style="vertical-align: middle;border: 1px solid #cecece;">
				        	<#if task.fileList ?? && (task.fileList?size > 0)>
	                			<#list task.fileList as file>
	                				<div>
										<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br>
									</div>
							    </#list>
				            </#if>
				        </td>
				    </tr>
				</#list>
			</table>
		</div>
	</div>
	<div class="BigTool">
    	<div class="BtnList">
    	    <a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="javascript:send();">派发</a>
    	    <a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="javascript:end_();">归档</a>
    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
        </div>
    </div>
</body>
<script type="text/javascript">
	$(function(){
		var $NavDiv2 = $("#tab ul li");
		$NavDiv2.click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			var NavIndex2 = $NavDiv2.index(this);
			$("div[id^='content']").hide();
			$("#content"+NavIndex2).show();
		});
		
		//addRec(5,'list_5');
		
		var swfOpt1 = {
	    	positionId:'fileupload',//附件列表DIV的id值',
			type:'add',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',
			script_context_path:'${SQ_FILE_URL}',
			ajaxData: {'eventSeq':1},//未处理
			imgDomain:'${imgDownPath!}',//图片域名 type为add或者edit，showPattern为pic时，生效
			appCode: 'zhsq_event'
	    };
	    
		fileUpload(swfOpt1);
		
		var swfOpt = {
	    	positionId:'fileupload1',//附件列表DIV的id值',
			type:'detail',//add edit detail
			initType:'jsonp',//ajax、hidden编辑表单时获取已上传附件列表方式
			context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			script_context_path:'${SQ_FILE_URL}',//${SQ_FILE_URL}
			ajaxData: {'bizId':${bo.reqId?c},'attachmentType':'${REQ_ATTACHMENT_TYPE!}','eventSeq':'1'}
	    };
		fileUpload(swfOpt);
	});

	//关闭
	function cancel() {
		parent.closeMaxJqueryWindow();
	}
	
	/*新增一行记录*/
    function addRec(num, tabid) {
    	var temp_1 = 0;
    	if($(".childList")&&$(".childList").size()>0){
    		temp_1 = $(".childList").size();
    	}
    	var temp_2 = 0;
    	if($(".temp")&&$(".temp").size()>0){
    		temp_2 = $(".temp").size();
    	}
		if((temp_1+temp_2)==4){
    		$.messager.alert('提示', '最多新增4个联动单位！', 'error');
    		return ;
    	}
    	
        var str = "";
        //var tab = document.getElementById(tabid);
        var rowIndex = Math.ceil(Math.random()*1000000);
        str = " <tr class=\"temp\">" +
                "<td>" +
                	"<a style=\"margin-left:10px;font-size:14px;\" href=\"###\" class=\"NorToolBtn DelBtn\"onclick=\"delRec(this,'list_5')\">删除</a>" +
                "</td>" +
                "<td>" +
                	"<input id='infoOrgCode"+rowIndex+"' name=\"gridCode\" value=\"\" class=\"hide queryParam\" />"+
                	"<input type=\"text\" id='gridName"+rowIndex+"' name=\"gridName\" value=\"\" class=\"inp1 InpDisable\" style=\"width:130px;float: center;margin-bottom:4px;\"/>"+
                	"<input id='gridId"+rowIndex+"' name=\"gridId\" value=\"\" class=\"hide queryParam\" />"+
                "</td>" +
                "<td>" +
                	"<input disabled=\"disabled\" style=\"width:140px;float: center;margin-bottom:4px;\" type=\"text\" id='linkMan"+rowIndex+"' name=\"linkMan\" class=\"inp1 easyui-validatebox\"/>"+
                "</td>" +
                "<td>" +
                	"<input disabled=\"disabled\" style=\"width:140px;float: center;margin-bottom:4px;\" type=\"text\" id='leaderName"+rowIndex+"' name=\"leaderName\" class=\"inp1 easyui-validatebox\"/>"+
                "</td>" +
                "<td>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"background: center bottom #f4f4f4;\">处理期限" + "</td>" +
                "<td>" +"<input type=\"text\" id='limitDate"+rowIndex+"' name=\"limitDate\" value=\"\" class=\"inp1 inp2 easyui-numberbox\" style=\"width:130px;float: center;margin-bottom:4px;height:25px;\" data-options=\"tipPosition:\'bottom\',max:99,min:0\"/>"+ "</td>" +
                "<td>天（工作日）" + "</td>" +
                "<td style=\"background: center bottom #f4f4f4;\">截止日期" + "</td>" +
                "<td>" + 
                	"<input disabled=\"disabled\" style=\"width:105px;float: center;margin-bottom:4px;\" type=\"text\" id='afterDate"+rowIndex+"' name=\"afterDate\" class=\"inp1\"/>"+
                "</td>" +
                "</tr>"
         var newStrObj = $("#list_5").append(str);
         $.parser.parse(newStrObj);
         
         $("#limitDate"+rowIndex).numberbox({
        	onChange:function(newValue,oldValue){
				if(newValue==''||newValue==null){
					
				}else{
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/requestion/getWorkingDay.json',
						data: {
							workingDay: newValue
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$.messager.alert('错误', '工作日计算失败！', 'error');
							} else {
								$("#afterDate"+rowIndex).val(data.afterDate);
							}
						},
						error: function(data) {
							$.messager.alert('错误', '工作日计算连接超时！', 'error');
						},
						complete : function() {
							//modleclose(); //关闭遮罩层
						}
					});
				}
			} 
         });
         
         var caseTypeTime = $("#caseTypeTime").val();
		 $("#limitDate"+rowIndex).numberbox('setValue', caseTypeTime);
		
		 AnoleApi.initFuncOrgZtreeComboBox("gridName"+ (rowIndex), "infoOrgCode"+ (rowIndex), function(gridId, items){
			if(isNotBlankParam(items) && items.length>0){
				var grid = items[0];
				console.log(grid);
				$("#infoOrgCode"+ (rowIndex)).val(grid.orgCode);
				$("#gridId"+ (rowIndex)).val(grid.id);
				
				var count = 0;
				$("input[id^='gridId']").each(function(){
					   if($(this).val()==grid.id){
					   		count = count + 1;
					   }
				});
                //编辑
				if (count >= 2) {
					$("#infoOrgCode"+ (rowIndex)).val("");
					$("#gridName"+ (rowIndex)).val("");
					$("#gridId"+ (rowIndex)).val("");
					$("#linkMan"+ (rowIndex)).val("");
					$("#leaderName"+ (rowIndex)).val("");
					$.messager.alert('提示', '已经存在该联动单位信息!', 'warning');
				} else {
					$.ajax({
						type: 'POST',
						url: '${rc.getContextPath()}/zhsq/requestion/searchDeal.json',
						data: {
							id: grid.id
						},
						dataType: 'json',
						success: function(data) {
							if (data.result == 'fail') {
								$("#infoOrgCode"+ (rowIndex)).val("");
								$("#gridName"+ (rowIndex)).val("");
								$("#gridId"+ (rowIndex)).val("");
								$("#linkMan"+ (rowIndex)).val("");
								$("#leaderName"+ (rowIndex)).val("");
								$.messager.alert('错误', '没有找到相应的受理人员信息！', 'error');
							} else {
								$("#linkMan"+ (rowIndex)).val(data.corpLink.linkMan+"("+data.corpLink.linkManTel+")");
								$("#leaderName"+ (rowIndex)).val(data.corpLink.leaderName+"("+data.corpLink.leaderTel+")");
							}
						},
						error: function(data) {
							$.messager.alert('错误', '连接超时！', 'error');
						},
						complete : function() {
							modleclose(); //关闭遮罩层
						}
					});
				}
			} 
		});
    }
    
    /*删除一行列表*/
    function delRecList(obj, tabid) {
    	
    	$.messager.confirm('提示', '您确定删除该信息吗?', function(r) {
			if (r) {
				$.ajax({
					type: 'POST',
					url: '${rc.getContextPath()}/zhsq/requestion/delChild.json',
					data: {
						rluId: $(obj).attr("rluId")
					},
					dataType: 'json',
					success: function(data) {
						if (data.result == 'fail') {
							$.messager.alert('错误', '删除失败！', 'error');
						} else {
							$("#gridId"+$(obj).attr("gridId")).val("");
							$(obj).parent().parent().next().html("").remove();
							$(obj).parent().parent().html("").remove();
							parent.searchData();
						}
					},
					error: function(data) {
						$.messager.alert('错误', '连接超时！', 'error');
					},
					complete : function() {
						
					}
				});
			}
		});
    }
    
    /*删除一行记录*/
    function delRec(obj, tabid) {
        if ($(".temp")&&$(".temp").length > 0) {
        	$(obj).parent().parent().next().remove();
            $(obj).parent().parent().remove();
        } else {
        	$(obj).parent().parent().find("input").each(function () {//每次获取一行信息temp_gridId
                 $(this).val("");
            });
        }
    }
	
	/*为一行记录添加评价信息*/
    function backRec(obj, tabid) {
		var url = '${rc.getContextPath()}/zhsq/requestion/todeal.jhtml?id='+$(obj).attr("nodeId");
		showMaxJqueryWindow('评价', url, 500, 300);
    }
    
    function subRec(obj, tabid) {
    	
    	var url = '${rc.getContextPath()}/zhsq/requestion/todo.jhtml?id=' + $(obj).attr("nodeId")+'&taskId=0&instanceId='+$(obj).attr("wfInstanceId")+'&tasktype=2';
		showMaxJqueryWindow('中心办理', url, null, null);
    }
    
    function flashRec(wfInstanceId,id,str){
    	$("#a_"+wfInstanceId).hide();
    	$("#span"+id).html(str);
		parent.searchData();
    }
    
    /*派发记录*/
    function send() {
    
    	var flag = false;
    	$(".temp input[id^='linkMan']").each(function(){
		   if($(this).val()!=""&&$(this).val()!=null){
		   		flag = true;
		   		return false;
		   }
		});
		if(!flag){
			parent.$.messager.alert('提示', '联动单位未配置完全', 'info');
			return ;
		}
		$("input[id^='limitDate']").each(function(){
		   if($(this).val()==""||$(this).val()==null){
		   		flag = false;
		   		return false;
		   }
		});
		if(!flag){
			parent.$.messager.alert('提示', '处理期限未配置完全', 'info');
			return ;
		}
		
		if(flag){
			var isValid =  $("#tableForm").form('validate');
			if(isValid){
				modleopen();
				$("input[name='buttonType']").val("2");
				$("#tableForm").ajaxSubmit(function(data) {
	  				if(data.result=="success"){
	  				     parent.searchData();
	  					 parent.$.messager.alert('提示', '派发成功', 'info', function () {
	  					 	parent.closeMaxJqueryWindow();
			                //window.location.href = '${rc.getContextPath()}/zhsq/requestion/todo.jhtml?id=' + $("input[name='reqId']").val()+'&taskId='+$("input[name='taskId']").val()+'&instanceId='+$("input[name='instanceId']").val()+'&todoType=send';
			             });
	  				}else{
	  					 parent.$.messager.alert('提示', '派发失败', 'info', function () {
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
    
    function end_(){
		var isValid =  $("#tableForm").form('validate');
		if(isValid){
			var flag = true;
			$("input[name='temp_gridId']").each(function(){
			   if($(this).val()!='' && $(this).attr("status")!='2'){
			   		flag = false;
			   		return false;
			   }
			});
			if(flag){
				var isAll = "1";
				$("span[id^='span']").each(function(){
				   if($(this).html()!='已评价'){
				   		isAll = "2";
				   		return false;
				   }
				});
				if(isAll=="2"){
					$.messager.confirm('提示', '联动单位未全部评价，是否继续归档?', function(r) {
						if (r) {
							modleopen();
							$("input[name='buttonType']").val("3");
							$("#tableForm").ajaxSubmit(function(data) {
				  				if(data.result=="success"){
				  					 parent.searchData();
				  					 parent.$.messager.alert('提示', '归档成功', 'info', function () {
						                parent.closeMaxJqueryWindow();
						             });
				  				}else{
				  					parent.$.messager.alert('提示', '归档失败', 'info', function () {
						                return;
						            });
				  				}
							});
						}
					});
				}else{
					modleopen();
					$("input[name='buttonType']").val("3");
					$("#tableForm").ajaxSubmit(function(data) {
		  				if(data.result=="success"){
		  					 parent.searchData();
		  					 parent.$.messager.alert('提示', '归档成功', 'info', function () {
				                parent.closeMaxJqueryWindow();
				             });
		  				}else{
		  					parent.$.messager.alert('提示', '归档失败', 'info', function () {
				                return;
				            });
		  				}
					});
				}
			}else{
				parent.$.messager.alert('提示', '派发单位未全部归档', 'info', function () {
	                return;
	            });
			}			
		}
	}
	
	function flashList(id,str){
		$("#span"+id).html(str);
		parent.searchData();
	}
	
	function toShowReqLink(rluId){
		
		var url = '${rc.getContextPath()}/zhsq/requestion/toShowReqLink.jhtml?rluId='+rluId;
		showMaxJqueryWindow('查看详情', url, null, null);
	}
	
	function showBackList(reqId){
		
		var url = '${rc.getContextPath()}/zhsq/requestion/toShowBackList.jhtml?reqId='+reqId;
		showMaxJqueryWindow('驳回信息', url, null, null);
	}
</script>
</html>
