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
	 	<div name="tab" id="content0" class="NorForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<input type="hidden" name="reqId" value="${bo.reqId}"/>
				<tr>
                    <td class="LeftTd" colspan="2"><div class="title FontDarkBlue">基础信息</div></td>
                </tr>
				<tr>
					<td colspan="2">
						<label class="LabName"><span>事件类别：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.typeStr)!}</span>
					</td>
					<!--<td>
						<label class="LabName"><span>提交时间：</span></label>
						<span class="Check_Radio FontDarkBlue">${(bo.creatTimeStr)!}</span>
					</td>-->
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
						<span class="Check_Radio FontDarkBlue">${(bo.desc)!}</span>
					</td>
				</tr>
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
