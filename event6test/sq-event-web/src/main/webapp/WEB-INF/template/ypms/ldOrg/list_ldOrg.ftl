<!DOCTYPE html>
<html>
<head>
	<title>列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/component/commonFiles-1.1.ftl" />
	<#include "/component/ComboBox.ftl" />
	<#include "/component/maxJqueryEasyUIWin.ftl" />
	<#include "/component/listSet.ftl" />

	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.exedit.js"></script>
	<!--<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.excheck.js"></script>
	<script type="text/javascript" src="${rc.getContextPath()}/js/ztree/jquery.ztree.core.js"></script>-->
	<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/js/ztree/zTreeStyle/zTreeStyle.css" />

	<style type="text/css">
		.inp1 {width:100px;}
	</style>
</head>
<body class="easyui-layout">
	<div region="west" title="<span class='easui-layout-title'>知识库</span>" split="true" style="width:220px;">
		<ul id="treeDemo" class="ztree" ></ul>
	</div>
	<div id="_DivCenter" region="center" >
	<form id="submitForm">
	   <div id="content-d" class="MC_con content light">
			<div name="tab" id="div0" class="NorForm">
				<#include "/ypms/ldOrg/detail_ld.ftl" />
				<#include "/ypms/ldOrg/detail_dw.ftl" />
				<table id="_table" width="100%" border="0" cellspacing="0" cellpadding="0">
					<input type="text" id="ldId" name="ldId" hidden="true"/>
					<input type="text" id="orgId" name="orgId" hidden="true"/>
					<input type="text" id="orgCode" name="orgCode" hidden="true"/>
					<tr>
						<td colspan="2">
							<label class="LabName"><span>单位类型：</span></label>
							<label class="LabName" style="width:100px;"><span><input type="radio" style="margin-bottom:7px" name="ldType" value="0" class="f-type">联动单位</label>
							<label class="LabName" style="width:120px;"><span><input type="radio" style="margin-bottom:7px" name="ldType" value="1" class="f-type" />专业化队伍</label>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<label class="LabName"><span>名称：</span></label>
							<input type="text" id="ldName" name="ldName" style="width:320px" class="inp1 easyui-validatebox" data-options="required:true, validType:'maxLength[24]', tipPosition:'bottom'"  />
						</td>
					</tr>
					<#include "/ypms/ldOrg/form_ldOrg.ftl" />
					<#include "/ypms/ldOrg/form_dwOrg.ftl" />
					
					<tr>
						<td colspan="2">
							<label class="LabName"><span>备注：</span></label>
							<textarea id="ldBz" name="ldBz" style="width: 400px;height:120px;margin:10px auto">	
							</textarea>
						</td>
					</tr>
				</table>
			</div>
			</div>
	</form>
		<div class="BigTool" id="_btn">
	    	<div class="BtnList" id="_BtnList">
	    		<a href="javascript:;" class="BigNorToolBtn SaveBtn" onClick="save();">保存</a>
	    		<a href="javascript:;" class="BigNorToolBtn CancelBtn" onClick="cancel();">关闭</a>
	        </div>
	    </div>
	</div>
	
</body>
<script type="text/javascript">
var zNodes = $.parseJSON('${dataDict!}');
$("#_table").hide();
$("#detail_ld").hide();
$("#detail_dw").hide();
$("#_btn").hide();
var setting = {
		 view: {
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			selectedMulti: false,
			showLine: false
		}, 
		async: {
			autoParam:["id"],    
            enable:true,
            dataType:"json",
            type:"post",
			url:"${rc.getContextPath()}/zhsq/ldOrg/getTreeData.json"
		},
		 edit: {
			enable: true,
			editNameSelectAll: true,
			showRemoveBtn: setRemoveBtn,
			showRenameBtn: setRenameBtn
		}, 
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeEditName: beforeEdit,
			beforeRemove: beforeRemove,
			onClick: zTreeOnClick,
			onRemove:onRemove
		}
	};
	
	function addHoverDom(treeId, treeNode) {
		if (treeNode.isParent == true) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
				+ "' title='add node' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			
			if (btn) btn.bind("click", function(){
				var url = '${rc.getContextPath()}/web/aceTree/page.jhtml?multi=true&chooseType=0&showToLevel=8&folderSelect=true&ldType=' + treeNode.ldType;
				showMaxJqueryWindow('新增', url, 600, 450);
				return false; //return false在点击之后会删除新增按钮
			});
		}
	};
	
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};
	
	function zTreeOnClick(event, treeId, treeNode) {
	    if (treeNode.isParent == true) {
	    	return;
	    } else if (treeNode.ldType == '0') {
	    	$("#detail_ld").show();
	    	$("#detail_dw").hide();
	    	$("#_table").hide();
	    	$("#_btn").hide();
	    } else {
	    	$("#detail_dw").show();
	    	$("#detail_ld").hide();
	    	$("#_table").hide();
	    	$("#_btn").hide();
	    }
	    $.ajax({
			type : "POST",
			url: '${rc.getContextPath()}/zhsq/ldOrg/searchById.json?id='+treeNode.id,
			date: {},
			dataType : "json",
			success : function(data) {
				spanRemove();
				if (data.ldType == '0') {
					$("#detail_ld_ldType").after("<span class='Check_Radio FontDarkBlue spanRemove' >联动单位</span>");
					$("#detail_ld_ldName").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldName+"</span>");
					
					if (data.leader != null) {
						$("#detail_ld_leader").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.leader+"</span>");
					}
					if (data.leaderDuty != null) {
						$("#detail_ld_leaderDuty").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.leaderDuty+"</span>");
					}
					if (data.leaderMobile != null) {
						$("#detail_ld_leaderMobile").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.leaderMobile+"</span>");
					}
					if (data.contacter != null) {
						$("#detail_ld_contacter").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.contacter+"</span>");
					}
					if (data.contacterDuty != null) {
						$("#detail_ld_contacterDuty").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.contacterDuty+"</span>");
					}
					if (data.contacterMobile != null) {
						$("#detail_ld_contacterMobile").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.contacterMobile+"</span>");
					}
					
					if (data.ldBz != null) {
						$("#detail_ld_ldBz").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldBz+"</span>");
					}
				}
				if (data.ldType == '1') {
					$("#detail_dw_ldType").after("<span class='Check_Radio FontDarkBlue spanRemove' >专业化队伍</span>");
					$("#detail_dw_ldName").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldName+"</span>");
					
					if (data.lxr != null) {
						$("#detail_dw_lxr").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.lxr+"</span>");
					}
					if (data.lxrMobile != null) {
						$("#detail_dw_lxrMobile").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.lxrMobile+"</span>");
					}
					
					if (data.ldItem != null) {
						$("#detail_dw_ldItem").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldItem+"</span>");
					}
					if (data.ldPrice != null) {
						$("#detail_dw_ldPrice").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldPrice+"</span>");
					}
					if (data.ldDisCount != null) {
						$("#detail_dw_ldDisCount").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldDisCount+"</span>");
					}
					if (data.ldServer != null) {
						$("#detail_dw_ldServer").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldServer+"</span>");
					}
					if (data.ldBz != null) {
						$("#detail_dw_ldBz").after("<span class='Check_Radio FontDarkBlue spanRemove' >"+data.ldBz+"</span>");
					}
				}
				
				
			},
			error : function(data) {}
		});
	};
	
	function setRenameBtn(treeId, treeNode) {
		return !treeNode.isParent;
	}
	
	function setRemoveBtn(treeId, treeNode) {
		return !treeNode.isParent;
	}
	
	function beforeEdit(treeId, treeNode) {
		$.messager.confirm('提示', '您确定编辑该联动单位吗?', function(r) {
			if (r) {
				$("#_table").hide();
				$("#detail_ld").hide();
				$("#detail_dw").hide();
				$("#_btn").hide();
				
				$.ajax({
					type : "POST",
					url: '${rc.getContextPath()}/zhsq/ldOrg/searchById.json?id='+treeNode.id,
					date: {},
					dataType : "json",
					success : function(data) {
						$("#ldId").val(data.ldId);
						$("#orgId").val(data.orgId);
						$("#orgCode").val(data.orgCode);
						$("#ldName").val(data.ldName);
						$("#ldType").val(data.ldType);
						$("input:radio[name=ldType][value="+data.ldType+"]").attr("checked",true);
						
						$("#leaderId").val(data.leaderId);
						$("#leader").val(data.leader);
						$("#leaderDuty").val(data.leaderDuty);
						$("#leaderMobile").val(data.leaderMobile);
						$("#contacterId").val(data.contacterId);
						$("#contacter").val(data.contacter);
						$("#contacterDuty").val(data.contacterDuty);
						$("#contacterMobile").val(data.contacterMobile);
						$("#lxrId").val(data.lxrId);
						$("#lxr").val(data.lxr);
						$("#lxrMobile").val(data.lxrMobile);
						
						$("#ldPrice").val(data.ldPrice);
						$("#ldDisCount").val(data.ldDisCount);
						$("#ldServer").val(data.ldServer);
						$("#ldBz").val(data.ldBz);
						
						$("#_table").show();
						if (data.ldType == 0) {
							$(".ld").show();
							$(".dw").hide();
						} else {
							$(".ld").hide();
							$(".dw").show();
						}
						$("#_BtnList").css("width","400");
						$("#_btn").show();
						
						//showOrHide(data.ldType);
					},
					error : function(data) {}
				});
			}
		});
	}
	
	function beforeRemove(treeId, treeNode){
		$.messager.confirm('提示', '您确定删除该联动吗?', function(r) {
			if (r) {
				$.ajax({
					type : "POST",
					url: '${rc.getContextPath()}/zhsq/ldOrg/del.json?id='+treeNode.id,
					date: {},
					dataType : "json",
					success : function(data) {
						if (data.result == 'fail') {
							$.messager.alert('错误', '删除失败！', 'error');
						} else {
							$.messager.alert('提示', '删除成功！', 'info');
							refreash();
						}
					},
					error : function(data) {
						$.messager.alert('错误', '连接超时！', 'error');
					}
				});
			}
		});
		return nodeResult;
	}
	
	function onRemove(){
	}
	
	function refreash(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	}
	
	function cancel() {
		$("#_table").hide();
		$("#_btn").hide();
	}
	
	$('input[type=radio][name=ldType]').change(function() {
		//var val = $('input[name="ldType"]:checked ').val();
        if (this.value == '0') {
        	$(".ld").show();
			$(".dw").hide();
        }
        else if (this.value == '1') {
        	$(".ld").hide();
			$(".dw").show();
        }
    });
	
	function save(){
		var isValid = $('#submitForm').form('validate');
		if (isValid) {
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/ldOrg/save.json',
				data: $('#submitForm').serializeArray(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							location.reload();
						});
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
	
	function spanRemove(){
		$(".spanRemove").remove();
	}
	
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
	
</script>
</html>
