<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>扫黑除恶-黑恶团伙管理 新增/编辑</title>
		<!-- 扫黑除恶样式 -->
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/main-shce.css"/>
		<link rel="stylesheet" type="text/css" href="${SQ_FILE_URL}/js/swfupload/css/swfupload.css" />
		<#include "/component/commonFiles-1.1.ftl" />
		<#include "/component/ComboBox.ftl" />
		<link rel="stylesheet" type="text/css" href="${uiDomain!''}/web-assets/common/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="${rc.getContextPath()}/css/sweepBlackRemoveEvil/css/sweepBlackRemoveEvil.css"/>
		
		<script type="text/javascript" src="${SQ_FILE_URL}/js/swfupload/handlers.js"></script>
		
		<!-- 扫黑除恶js -->
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/jquery.nicescroll.js" charset="utf-8"></script>
		<script type="text/javascript" src="${rc.getContextPath()}/js/sweepBlackRemoveEvil/main-shce.js" charset="utf-8"></script>

	</head>
	<body>
		<form id="eventSBREvilGangForm" name="eventSBREvilGangForm" action="" method="post" enctype="multipart/form-data">
			<input type="hidden" id="gangId" name="gangId" value="<#if bo.gangId??>${bo.gangId?c}</#if>" />
			<div class="container_fluid">
				<div class="form-warp-sh"  style="padding-top:50px"><!-- 外框 -->
					<div class="fw-toptitle">
					<h6 class="note-s">带<span>*</span>为必填项</h6>
					</div>
					<!-- 主体内容 -->
					<div class="fw-main" id="mainDiv" style="padding-top: 0;">
						<!-- 线索基础信息 -->
						<div>
							<ul class="fw-xw-from clearfix" >
					
								<li class="xw-com2">
									<span class="fw-from1"><i class="spot-xh">*</i>所属区域:</span>
									<input type="hidden" id="infoOrgCode" name="infoOrgCode" value="${bo.infoOrgCode!}" />
									<input type="text" id="infoOrgName" class="from flex1 bg-btm-arrow easyui-validatebox" data-options="required:true,tipPosition:'bottom'" value="${bo.gridPath!}" />
								</li>
	
							<li class="xw-com2">
								<span class="fw-from1"><i class="spot-xh">*</i>团伙名称:</span>
								<input class="from flex1 easyui-validatebox" data-options="validType:'maxLength[200]',required:true, tipPosition:'bottom'" type="text" placeholder="请输入团伙名称"  name="gangName" value="${(bo.gangName)!}">
							</li>
							<li class="xw-com1">
								<span class="fw-from1">主要活动地带:</span>
								<textarea name="activityZone" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[2000]','characterCheck']" >${bo.activityZone!}</textarea>
							</li>
							<li class="xw-com1">
								<span class="fw-from1">团伙涉黑情况:</span>
								<textarea name="situation" class="textfrom flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[10000]','characterCheck']" >${bo.situation!}</textarea>
							</li>
							
							<li class="xw-com3">
								<span class="fw-from1"><i class="spot-xh">*</i>打击状态:</span>
								<input type="hidden" id="hitStatus" name="hitStatus" />
								<input type="text" id="hitStatusName" class="from flex1 bg-btm-arrow easyui-validatebox" data-options="required:true,tipPosition:'bottom'" />
							</li>
							</ul>
						</div>
				
						<div class="fw-det-tog mt10">
						<div class="fw-det-tog-top" style="margin-bottom: 20px;">
							<h5><i></i>团伙成员</h5>
							<a href="##"><img src="${rc.getContextPath()}/css/sweepBlackRemoveEvil/images/icon_fw_detail_tog.png"/> </a>
						</div>
						<div class="fw-det-toggle">
						<!-- 成员信息 -->
						<div id="memberInfoDiv" class="mt30 b-repor">
							<div class="repor-head">
								<span id="href3" class="fw-from1"><i class="spot-xh"></i>成员:</span>
								<ul id="memberUl" class="headlist flex1 clearfix">
									<li id="addMemberLi">
										<a class="hd-add" title="点击新增" onclick="addMemberInfo();"></a> 
									</li>
									
									<li id="defaultMemberLi" style="display: none;">
										<a class="hd-box-spot" ></a>
										<a class="hd-box bd-on" ></a>
									</li>
									
								</ul>
							</div>
								<div id="defaultMemberContentDiv" class="report-pep xw-com1 hide" index="0">
								   <input type="text" id="ipId" class="hide" value="" />
								   <input type="text" id="bizType" class="hide" value="11" />
									<ul class="fw-xw-from clearfix ">
										<li class="xw-com2">
											<span class="fw-from1"><i class="spot-xh">*</i>姓名:</span>
											<input type="text" id="name" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[80]','characterCheck']" />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">年龄:</span>
											<input type="text" id="age" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:'numLength[3]'" />
										</li>
										<li class="xw-com2">
											<span class="fw-from1">职业:</span>
											<input type="text" id="profession" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[30]','characterCheck']">
										</li>
									
										<li class="xw-com2">
											<span class="fw-from1">身份证号:</span>
											<input type="text" id="idCard" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:'idcard'" />
										</li>
							 			<li class="xw-com3">
											<span class="fw-from1"><i class="spot-xh">*</i>是否骨干:</span>
											<label class="fw-radio-box">
												<input type="radio" class="fw-radio" id="isSkeletonStaff_1" name="isSkeletonStaff" value="1" />
												<span class="radio-input"></span>是
											</label>
											<label class="fw-radio-box">  
												<input type="radio" class="fw-radio" id="isSkeletonStaff_0" name="isSkeletonStaff" value="0"  />   
												<span class="radio-input"></span>否
											</label>
										</li>
										<li class="xw-com1">
											<span class="fw-from1">家庭住址:</span>
											<input type="text" id="homeAddr" class="from flex1 easyui-validatebox" data-options="tipPosition:'bottom',validType:['maxLength[256]','characterCheck']" />
										</li>
									</ul>
								</div>
								    
							</div>
							</div>
							</div>
					      <div class="mt20">
							<ul class="fw-xw-from clearfix" style="border-bottom: 1px dashed #ccc; padding-bottom: 20px;">
								<li class="xw-com1">
									<span class="fw-from1"><i class="spot-xh">*</i>备注:</span>
									<input class="from flex1 easyui-validatebox" name="gangRemark" data-options="validType:'maxLength[400]',required:true, tipPosition:'bottom'" type="text" placeholder="请输入备注" value="${bo.gangRemark}">
								</li>
							</ul>
						 </div>
						</div>
					
					<!-- 操作按钮 -->
					<div id="btnDiv" class="btn-warp">
						<a class="btn-bon green-btn" onclick="save();">保存</a>
					</div>
				</div>
			</div>
		</form>
	</body>
	
	<script type="text/javascript">
		$(function () {
	        $('.fw-det-tog-top').on('click', function(){
	            $(this).siblings('.fw-det-toggle').toggle(300);
	        });
			var $winH, $topH, $btnH;
			$(window).on('load resize', function () {
				$winH = $(window).height();
	            $topH = $('.fw-toptitle').height();
	            $btnH = $('.btn-warp').height();
	            $('.fw-main').height($winH - $topH - $btnH-50);
			    $(".fw-main").niceScroll({
			        cursorcolor:"rgba(0, 0, 0, 0.3)",
			        cursoropacitymax:1,
			        touchbehavior:false,
			        cursorwidth:"4px",
			        cursorborder:"0",
			        cursorborderradius:"4px"
			    });
	        });
	    });
	
		$(function () {
			var gangId = $("#gangId").val();
			//所属区域树
			AnoleApi.initGridZtreeComboBox("infoOrgName", null, function(gridId, items) {
				if(isNotBlankParam(items) && items.length > 0) {
					var grid = items[0];
					$("#infoOrgCode").val(grid.orgCode);
				}
			});
		    AnoleApi.initListComboBox("hitStatusName", "hitStatus", null, null, ['${bo.hitStatus!}'], {
	        	DataSrc : [{"name":"已扫除", "value":"1"},{"name":"扫除中", "value":"2"}],
	        	IsTriggerDocument: false,
	        	ShowOptions:{
	        		EnableToolbar : true
	        	}
	        });

			<#if members?? && (members?size>0)>
                showMembers();//若人存在，展示信息
			<#else >
               addMemberInfo();//默认展示一个成员信息新增模块
			</#if>
		});
		
		function addMemberInfo() {
			var index = index = parseInt($('#defaultMemberContentDiv').attr('index'), 10) + 1,
				isValid = index == 1;//添加首个时，不验证，否则不能加载出
	
			if(index > 1) {
				isValid =  $("#eventSBREvilGangForm").form('validate');
			}
			
			if(isValid) {
				var cloneContentObj = $('#defaultMemberContentDiv').clone(),//复制
					cloneMemberObj = $('#defaultMemberLi').clone(),
					index = parseInt($('#defaultMemberContentDiv').attr('index'), 10) + 1,
					randomIndex = Math.random().toString().substr(2),
					contentId = 'memberInfoDiv_' + index + '_' + randomIndex,
					memberId = 'memberLi_' + index + '_' + randomIndex;
				
				cloneContentObj.attr('id', contentId);
				$('#defaultMemberContentDiv').attr('index', index);
				cloneMemberObj.attr({'id': memberId, 'contentId': contentId, 'onclick': 'selectMemberInfo("' + contentId + '")'});
				
				$('#memberUl').append(cloneMemberObj);
				$('#memberInfoDiv').append(cloneContentObj);
				$("#"+contentId+" [name^='isSkeletonStaff']").attr('name', "isSkeletonStaff"+index);

				$.parser.parse(cloneContentObj);//重新渲染模块，从而使得验证生效
				
				$("input[name=isSkeletonStaff"+index+"]").attr("checked",'0');       
				
				$('#' + contentId + ' input[id=name]').validatebox({
					required: true
				});
				
				$('#' + memberId + ' > a.hd-box-spot').attr('onclick', 'delMemberInfo("'+ contentId +'", "'+ memberId +'")');
				
				var involvedName = $('#defaultMemberContentDiv').clone()
				
				
				selectMemberInfo(contentId);
			}
		}
		
		function selectMemberInfo(contentId) {
			var memberObj = $('#memberUl li[contentId='+ contentId +']');
			
			if(memberObj.length > 0) {//删除操作时，由于冒泡会触发一次父级的onclick事件
				var selectedObj = $('#memberUl a.bd-on'),
					selectedContentId = selectedObj.parent().attr('contentId');
				
				selectedObj.attr('title' , $('#' + selectedContentId + ' input[id=involvedName]').val());
				
				$('#memberUl a.hd-box').removeClass('bd-on');
				memberObj.children('a.hd-box').addClass('bd-on');
				$('#memberInfoDiv div[id^=memberInfoDiv_]').hide();
				
				$('#memberUl li[contentId='+ contentId +']').show();
				$('#' + contentId).show();
			}
		}
		
		function delMemberInfo(contentId, memberId) {
			var mli=$('#memberUl li').length;
			if(mli==3){//保留一个
				$('#defaultMemberContentDiv').attr('index', 0);
				$.messager.alert('提示', '至少保留一个成员信息', 'info');
				return;
			}
			var index = parseInt($('#defaultMemberContentDiv').attr('index'), 10) - 1;
			$('#memberUl li[contentId='+ contentId +']').remove();
			$('#' + memberId).remove();
			$('#' + contentId).remove();
			
			if(index <= 0) {//保证至少有一个成员
				index = 1;
				addMemberInfo();
			} else {//默认选中首个
				selectMemberInfo($('#memberInfoDiv div[id^=memberInfoDiv_]').eq(0).attr('id'));
			}
			
			$('#defaultMemberContentDiv').attr('index', index);
		}
		//获取成员数组
		function fetchMemberInfo() {
			var memberObjArray = [],
				memberObj = {},
				inpVal = "";
			
			$('#memberInfoDiv div[id^=memberInfoDiv_]').each(function() {
				memberObj = {};
				$(this).find('input[type=text]').each(function() {
					inpVal = $(this).val();
					
					if(inpVal) {
						memberObj[$(this).attr('id')] = inpVal;
					}
				});
				
				$(this).find('input:radio:checked').each(function() {
					inpVal = $(this).val();
					
					//if(inpVal) {
						memberObj[$(this).attr('id').split('_')[0]] = inpVal;
					//}
				});
				
				memberObjArray.push(memberObj);
			
			});
			//if(memberObjArray.length > 0) {
				//JSON.stringify(reportedObjArray);  
				//$.messager.alert('错误', JSON.stringify(memberObjArray), 'error');
			//}
			return JSON.stringify(memberObjArray);
		}
		
			//保存
	function save() {
		var isValid = $('#eventSBREvilGangForm').form('validate');
		if (isValid) {
			modleopen(); //打开遮罩层
			$.ajax({
				type: 'POST',
				url: '${rc.getContextPath()}/zhsq/eventSBREvilGang/save.json',
				data: $('#eventSBREvilGangForm').serialize()+"&memberlist="+fetchMemberInfo(),
				dataType: 'json',
				success: function(data) {
					if (data.result == 'fail') {
						$.messager.alert('错误', '保存失败！', 'error');
					}if (data.result == 'exsit') {
					modleclose(); //关闭遮罩层
						$.messager.alert('错误', '团伙名称“'+ data.gangName+'”'+'已在“'+data.gridPath+'”中使用！', 'error');
					} else {
						$.messager.alert('提示', '保存成功！', 'info', function() {
							parent.closeMaxJqueryWindow();
						});
						parent.searchData();
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
	function showMembers(){
	
	<#if members?? && (members?size>0)>
		        <#list members as list>
                    var index = index = parseInt($('#defaultMemberContentDiv').attr('index'), 10) + 1,
                                               isValid = index == 1;//添加首个时，不验证，否则不能加载出

                    if(index > 1) {
                        isValid =  $("#eventSBREvilGangForm").form('validate');
                    }
                   var cloneContentObj = $('#defaultMemberContentDiv').clone(),//复制
					cloneMemberObj = $('#defaultMemberLi').clone(),
					index = parseInt($('#defaultMemberContentDiv').attr('index'), 10) + 1,
					randomIndex = Math.random().toString().substr(2),
					contentId = 'memberInfoDiv_' + index + '_' + randomIndex,
					memberId = 'memberLi_' + index + '_' + randomIndex;
				
				cloneContentObj.attr('id', contentId);
				$('#defaultMemberContentDiv').attr('index', index);
				cloneMemberObj.attr({'id': memberId, 'contentId': contentId, 'onclick': 'selectMemberInfo("' + contentId + '")'});
				
				$('#memberUl').append(cloneMemberObj);  
				
				$('#memberInfoDiv').append(cloneContentObj);
				$("#"+contentId+" [name^='isSkeletonStaff']").attr('name', "isSkeletonStaff"+index);
                    $.parser.parse(cloneContentObj);//重新渲染模块，从而使得验证生效

                    $('#' + contentId + ' input[id=name]').validatebox({
                        required: true
                    });

                    $('#' + contentId + ' #name').val('${list.name!''}');
                    $('#' + contentId + ' #age').val('${list.age!''}');
                    $('#' + contentId + ' #profession').val('${list.profession!''}');
                    $('#' + contentId + ' #idCard').val('${list.idCard!''}');
                    $('#' + contentId + ' #homeAddr').val('${list.homeAddr!''}');
                    $('#' + contentId + ' #ipId').val('${list.ipId!''}');   
                    $("input[name='isSkeletonStaff"+index+"'][value='${list.isSkeletonStaff!}']").attr("checked",true); ;
                    $('#' + memberId + ' > a.hd-box-spot').attr('onclick', 'delMemberInfo("'+ contentId +'", "'+ memberId +'")');

                    selectMemberInfo(contentId);
				</#list >
		    </#if>
	}
	</script>

</html>