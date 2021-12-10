<script type="text/javascript">
	//督办记录获取初始化
	function init4FetchRemind() {
		var listType = "${listType!}";
		
		if(listType == '5' || listType == '2') {//辖区所有、待办获取督办信息
			if($('#remindListLi').length == 0) {
				$('#topTitleUl').append('<li id="remindListLi" class="hide"><a href="###" divId="remindListDiv">督办列表</a></li>');
			}
			
			if($('#remindListDiv').length == 0) {
				$('#formDiv').append(
					'<div id="remindListDiv" class="fw-main tabContent" style="padding-top: 3px;">' + 
						'<div id="remindListContentDiv" class="appraise"></div>' + 
					'</div>'
				);
			}
			
			if($('#dubanIconDiv').length == 0) {
				$('#topTitleDiv').after('<div id="dubanIconDiv" class="dubanIcon hide"></div>');
			}
			
			fetchRemindInfo();
		}
	}
	
	//获取督办记录
	function fetchRemindInfo() {
        $.ajax({
            type: "POST",
            url : '${rc.getContextPath()}/zhsq/workflow4Base/findUrgeOrRemindList.jhtml',
            data: {'instanceId': '<#if instanceId??>${instanceId?c}</#if>', 'category': '2'},
            dataType:"json",
            success: function(data){
                var superviseResultList = data.remindMapList;

                if(superviseResultList && superviseResultList.length > 0) {
                    var superviseResultContent = "", supervise = null;
                    $("#remindListLi").show();
                    $("#dubanIconDiv").show();

                    for(var index in superviseResultList) {
                        supervise = superviseResultList[index];

                        superviseResultContent += '<div class="list">';
                        superviseResultContent += '    <span>';
                        superviseResultContent += '        <p>';
                        if(supervise.remindUserName) {
                            superviseResultContent += '			<em class="FontDarkBlue">' + supervise.remindUserName + '</em>';
                        }
                        if(supervise.remindDateStr) {
                            superviseResultContent += '			于 ' + supervise.remindDateStr;
                        }
                        
                        superviseResultContent += '				<b class="FontRed">督办</b>';
					
                        superviseResultContent += '			</p>';
                        if(supervise.remarks) {
                            superviseResultContent += '			' + supervise.remarks;
                        }
                        superviseResultContent += '    </span>';
                        superviseResultContent += '</div>';
                    }
                    
                    $("#remindListContentDiv").html(superviseResultContent);
                }
                
            },
            error:function(data){
                $.messager.alert("错误", "获取督办信息失败！", "error");
            }
        });
    }
    
    function flashData(msg) {//工作办理回调
    	parent.reloadDataForSubPage(msg, true);
    }
    
    //入格事项详情、可编辑页判断是否上传处理前、中、后附件
    //当前页面附件上传成功回调
    function uploadback(file,response) {
        //判断当前页面是否上传处理后图片
        var attachmentId = response.attachmentId||-1;
        var attachmentEventSeq = response.eventSeq||-1;
/*        var handledLableVal = 3;
        if(attachmentEventSeq == handledLableVal){*/
        //上传了附件
        var newFileIds = $('#isUploadNewFile').val() + response.attachmentId + ',';
        $('#isUploadNewFile').val(newFileIds);
        //}
    }
    //当前页面删除附件回调
    function deleteback(obj) {
        var attachmentId = obj.attachmentId||'';
        var newFileIds = $('#isUploadNewFile').val()||'';

        if(newFileIds.indexOf(attachmentId+',')>-1){
            newFileIds = newFileIds.replace(attachmentId+',','');
            $('#isUploadNewFile').val(newFileIds);
        }
    }
    //校验当前页面是否上传了处理后附件
    /*
    * typeName：需要上传的业务附件名称，eg：处理前、处理中、处理后、行政处罚书、立案决定书...
    * typeLabelSeq：标签序列 1 2 3
    * isUpload：是否上传，默认false
    * optionObj：额外参数对象
    * */
    function verifyAttIsUpload(typeName,typeLabelSeq,isUpload,optionObj) {
	    typeName = typeName||'处理前';
        typeLabelSeq = typeLabelSeq||1;
        //是否上传图片
        isUpload = isUpload||false;
        optionObj = optionObj||{};
        var isVerAttr = false;//默认不上传

        if(isUpload || isUpload == 'true'){
            isVerAttr = true;
        }
        //校验附件是否上传成功校验结果，默认上传成功，当环节不需要强制上传附件时，可以正常提交工作流
        var isValid = true;
        if(isVerAttr){
            isValid = checkAttachmentStatus4BigFileUpload('bigFileUploadDiv');
        }
        if(isVerAttr && isValid){
            //判断当前页面附件是否有重新上传并上传成功（页面第一次进入或重新刷新页面时，$('#isUploadNewFile').val()为空 isValid为false）
            //重新上传时暂不判断是前中后哪种图片，留在checkAttachment4BigFileUpload方法中进行判断具体需要上传前中后哪种附件
            isValid = $('#isUploadNewFile').val().length > 0;
            if(!isValid){
                $.messager.alert('警告', "请先上传"+ typeName +"图片！", 'warning');
                return
            }
            //校验文件是否上传，不上传该方法会弹出提示框，而seqVal又是变量，如果上方的弹框已经提示，下方法的弹框不再提示，故原校验放在下面
            isValid = checkAttachment4BigFileUpload(typeLabelSeq,$('#bigFileUploadDiv div[file-status="complete"]'),'',optionObj);
        }
        return isValid;
    }
    /*
    * 获取地图定位信息
    * */
    function capcapMarkerData(option) {
        var markerOperation = 2; // 地图操作类型
        var id = $("#id").val();
        var module = $("#module").val(); // 模块
        
        option = option || {};
        
        if(isBlankStringTrim(module)) {
        	$.messager.alert('错误','缺少地图模块类型！','error');
        	return;
        }

        $.ajax({
            url: '${rc.getContextPath()}/zhsq/map/arcgis/arcgis/getMapMarkerDataOfEvent.json?id='+id+'&module='+module+'&t='+Math.random(),
            type: 'POST',
            timeout: 3000,
            dataType:"json",
            async: false,
            error: function(data) {
                $.messager.alert('友情提示','获取地图标注信息获取出现异常!','warning');
            },
            success: function(data) {
                if(data && data.x != "" && data.x != null) {
                	var mapMarkerOpt = {
	                	el		: "resmarkerDiv",//div挂载点
	                	context	: '${GIS_DOMAIN!}',//gis域名
	                	width	: 480,//弹框宽度，可以不传，默认480px
	                	height	: 360,//弹框高度，可以不传，默认360px
	                	data	: { //业务数据
	                		id				: id,//业务标识
	                		showName		: '',
	                		markerType 		: module,//模块类型
	                		markerOperation : markerOperation,//地图操作类型 0和1为添加修改标注，2为查看标注 
	                		initPosType		: 1,
	                		initPosVal		: {
	                			x	:	data.x,
	                			y	:	data.y
	                		}
	                	} 
	                };
                    
                    for(var index in option) {
						if(typeof option[index] === 'object') {
							$.extend(mapMarkerOpt[index], option[index]);
						} else {
							mapMarkerOpt[index] = option[index];
						}
					}
					
                    $('#resmarkerDiv').show();
                    new MapMarker(mapMarkerOpt);
                } else {
                    $('#resmarkerDiv').hide();

                    //定位信息为空时 校验发生地址是否为空 为空的话 隐藏详情地址ul元素
                    var occurred = '${reportFocus.occurred!}';

                    if(isBlankStringTrim(occurred)) {
                        //隐藏ul
                        $('#resmarkerDiv').parent().parent().hide();
                    }
                }
                
            }
        });
    }
    
    /**
    * 分送、选送人员构造
    * isClearNextUser 是否清除办理人员信息，true表示清除原有，并重新构造；默认为true
    */
    function _handlerConstructor(data) {
    	data = data || {};
    	data = $.extend({
    		'isClearNextUser': true
    	}, data);
    	
    	if(data.isClearNextUser === false) {
    		return;
    	}
    	
    	$('#distributeUserDiv').hide();
		$('#htmlDistributeUserNames').html('');
		$('#distributeUserIds').val('');
		$('#distributeOrgIds').val('');
		$('#selectUserDiv').hide();
		$('#htmlSelectUserNames').html('');
		$('#selectOrgIds').val('');
		$('#selectUserIds').val('');
		
		if(data.distributeUser) {
			var distributeUserIds = data.distributeUser.userIds || '',
				distributeOrgIds = data.distributeUser.orgIds || '',
				isDisplayUser = data.distributeUser.isDisplayUser;
				
			$('#distributeUserIds').val(distributeUserIds);
			$('#distributeOrgIds').val(distributeOrgIds);
			
			if (isDisplayUser) {//只展示人员信息，不可修改
				var htmlUserNames = data.distributeUser.userNames || '';
				
				if(htmlUserNames) {
					var htmlUserContent = "",
						htmlUserArray = {},
						len = 0;
					
					htmlUserArray = htmlUserNames.split(',');
					len = htmlUserArray.length;
					
					if(len > 0) {
						htmlUserContent += '<div class="Check_Radio">';
						
						for(var index = 0; index < len; index++) {
							htmlUserContent += '<span class="SelectAll" style="margin-bottom: 3px;">' + htmlUserArray[index] + '</span>';
						}
						
						htmlUserContent += '</div>';
					}
					
					$('#htmlDistributeUserNames').html(htmlUserContent);
				}
				
				$('#distributeUserDiv').show();
			}
		}
		
		if(data.selectUser) {
			var selectUserIds = data.selectUser.userIds || '',
				selectOrgIds = data.selectUser.orgIds || '',
				selectOrgNames = data.selectUser.orgNames || '',
				isSelectUser = data.selectUser.isSelectUser;
			
			if (isSelectUser) {// 事件采集、指定到人、指定到组织
				var htmlUserNames = data.selectUser.userNames || '';
				
				if(htmlUserNames) {
					var htmlUserContent = "",
						htmlUserArray = {},
						htmlIdArray = {},
						htmlUserOrgIdArray = {},
						htmlUserOrgNameArray = {},
						len = 0,
						userLabelId = "";
						
					htmlUserArray = htmlUserNames.split(',');
					htmlIdArray = selectUserIds.split(',');
					htmlUserOrgIdArray = selectOrgIds.split(',');
					htmlUserOrgNameArray = selectOrgNames.split(',');
					len = htmlUserArray.length;
					
					if(len > 0) {
						htmlUserContent += '<div class="Check_Radio">';
						htmlUserContent += '<p style="display:block; height:28px;">';
						htmlUserContent += '<span class="SelectAll">';
						htmlUserContent += "<input type='checkbox'  id='htmlSelectUserCheckAll' onclick='_checkAllSelectUser();' />";
						htmlUserContent += "<label style='cursor:pointer;' for='htmlSelectUserCheckAll'>全选</label>";
						htmlUserContent += "</span>";
						htmlUserContent += '</p>';
						
						for(var index = 0; index < len; index++) {
							userLabelId = htmlIdArray[index] + "_" + htmlUserOrgIdArray[index] + "_" + index;
							
							htmlUserContent += "<input type='checkbox' name='htmlSelectUserCheckbox' id='"+ userLabelId +"' userid='"+ htmlIdArray[index] +"' orgid='"+ htmlUserOrgIdArray[index] +"' onclick='_checkSelectUser();' />";
							htmlUserContent += "<label style='cursor:pointer;' for='"+ userLabelId +"'>"+htmlUserArray[index]+"("+htmlUserOrgNameArray[index]+")"+"</label>" + '&nbsp;&nbsp;';
						}
						
						htmlUserContent += '</div>';
					}
					
					$('#htmlSelectUserNames').html(htmlUserContent);
				}
				
				$('#selectUserDiv').show();
			}else {
				$('#selectUserDiv').show();
				$('#selectUserPath').show();
			}
		}
    }
    
    function _itemSizeAdjust() {
		var tableWidth = $('#dealProcessTable').width();
		$('#flowSaveForm .DealMan').each(function(index) {
			var siblingLabel = $(this).siblings('label.LabName'),
				siblingWidth = 0;
			
			$(this).siblings().not('.DealMan').each(function() {
				siblingWidth += $(this).width();
			});
			
			if(siblingLabel.length > 0) {
				$(this).width((tableWidth - siblingWidth) * 0.98);
			}
		});
	}
</script>