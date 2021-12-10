<!DOCTYPE html>
<html>
<head>
	<title>详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
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
        .BtnList{width: 150px !important;}
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
				<input type="hidden" name="reqId" value="${bo.reqId}"/>
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
					<td colspan="2">
						<label class="LabName"><span>满意度回访：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;"><#if bo.satisfaction=='1' >满意</#if> 
		        					<#if bo.satisfaction=='2' >基本满意</#if>
		        					<#if bo.satisfaction=='3' >不满意</#if></span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>回访信息：</span></label>
						<span class="Check_Radio FontDarkBlue" style="width:85%;">${(bo.visit)!}</span>
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
						<span class="Check_Radio FontDarkBlue">${(bo.desc)!}</span>
					</td>
				</tr>
			</table>
			<table class="list" id="list_5" border="0" cellspacing="0" cellpadding="0" >
                <#if childList ?? && (childList?size > 0)>
	                <#list childList as line>
		                <tr style="background: center bottom #f4f4f4;">
		                   <th style="border:1px solid #bed3df ;width: 200px;" >联动单位</th>
		                   <th style="border:1px solid #bed3df ;width: 200px;" >联络员</th>
		                   <th style="border:1px solid #bed3df ;width: 200px;" >分管领导</th>
		                   <th style="border:1px solid #bed3df ;width: 200px;" >状态</th>         
		                </tr>
	                    <tr>
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
	                        	<#if line.state=='9'><span id="span${(line.rluId)!}">已归档</span>
	                        	<#elseif line.state=='8'>单位退回
	                        	<#elseif line.state=='7'>单位办理
	                        	<#elseif line.state=='6'>已评价
	                        	<#else></#if>
	                        </td>
	                    </tr>
			            <tr style="background: center bottom #f4f4f4;">
		                   <th colspan="3" style="border:1px solid #bed3df ;width: 160px;" >回访信息</th>
		                   <th colspan="1" style="border:1px solid #bed3df ;width: 160px;" >满意度</th>
		                </tr>
	                    <tr>
			                <td colspan="3" title="${(line.visit)!}">
			                	<div class=" FontDarkBlue" style="white-space:nowrap;text-overflow:ellipsis;overflow:hidden;width:93%">${(line.visit)!}</div>
			                </td>
			                <td colspan="1">
			                	<span class=" FontDarkBlue" >
				                	<#if line.satisfaction=='1' >满意</#if> 
		        					<#if line.satisfaction=='2' >基本满意</#if>
		        					<#if line.satisfaction=='3' >不满意</#if>
			                	</span>
	                        </td>
	                    </tr>
	                </#list>
	            </#if>
           </table>
		</div>
		<div name="tab" id="content1" class="NorForm" style="display:none;width:auto;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
					<th align="center" style="width: 120px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理环节</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>办理信息</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>处理意见</span></label>
					</th>
					<th align="center" style="width: 220px;border: 1px solid #cecece;">
						<label class="LabName"><span>附件</span></label>
					</th>
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
										<a target="_blank" href="${SQ_FILE_URL}/upFileServlet?method=down&attachmentId=${file.attachmentId}">${file.fileName}</a><br/>
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
</script>
</html>
