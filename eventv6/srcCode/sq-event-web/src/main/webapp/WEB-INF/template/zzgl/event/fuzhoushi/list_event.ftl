<@override name="eventListPageTitle">
	福州市事件列表页面
</@override>

<@override name="extraStyle">
	/*弹出div层样式*/
    .PopDiv {
    	position: absolute;
    	left:24%;
    	width: 68%;
    	overflow: hidden;
    	background-color: #fff;
    	border: 1px solid #bbb;
    	box-shadow:-3px -3px 6px #626262;
    	z-index: 11;
    }
	#queryConfigTable tr {
        width: 50%;
    	float: left;
    }
    #queryConfigTable tr td {
	    width: 100%;
	    display: block;
	    padding-left: 20px;
	    height: 35px;
	    box-sizing: border-box;
    }
    #queryConfigTable .LabName {
      width:auto;
    }
</@override>

<@override name="extendConditionInit">
	$('#jqueryToolbar').append(
	'<div id="queryConfigDiv" class="clear PopDiv NorForm hide" style="width: 500px;">' +
		'<div id="queryConfigTableDiv" style="max-height: 300px;">' +
			'<table id="queryConfigTable" width="100%" border="0" cellspacing="0" cellpadding="0">' +
			'</table>' +
		'</div>' +
		'<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			'<tr>' +
	    		'<td width="50%">' +
	    			'<a href="###" class="BigNorToolBtn BigJieAnBtn" style="float:right;" onclick="queryConfig();">确定</a>' +
	    		'</td>' +
	    		'<td>' +
	    			'<a href="###" onclick="closeQueryConfigDiv();" class="BigNorToolBtn CancelBtn">取消</a>' +
	    		'</td>' +
	    	'</tr>' +
		'</table>' +
	'</div>');
	
	$('#eventQueryForm').append(
	'<div id="toppingFieldDiv" class="clear PopDiv NorForm hide" style="width: 500px;">' +
		'<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			'<tr>' +
				'<td><div class="Check_Radio" style="float: right;"><label class="LabName"><span style="cursor: pointer;"><input type="radio" id="toppingFieldRadio_type" name="toppingFieldRadio" dictId="typeToppingField" dictNameId="typeToppingFieldName" style="cursor: pointer;" onclick="checkToppingFieldRadio(this);" checked/>事件分类：</span></label></div></td>' +
				'<td>' +
					'<input id="typeToppingField" name="typeToppingField" type="text" value="" attrId="type" class="queryParam hide"/>' +
					'<input id="typeToppingFieldName" type="text" class="inp1 selectWidth"/>' +
				'</td>' +
			'</tr>' +
			'<tr>' +
				'<td><div class="Check_Radio" style="float: right;"><label class="LabName"><span style="cursor: pointer;"><input type="radio" id="toppingFieldRadio_source" name="toppingFieldRadio" dictId="sourceToppingField" dictNameId="sourceToppingFieldName" style="cursor: pointer;" onclick="checkToppingFieldRadio(this);"/>信息来源：</span></label></div></td>' +
				'<td>' +
					'<input id="sourceToppingField" name="sourceToppingField" type="text" value="" attrId="source" class="queryParam hide"/>' +
					'<input id="sourceToppingFieldName" type="text" class="inp1 selectWidth" disabled/>' +
				'</td>' +
			'</tr>' +
			'<tr>' +
				'<td><div class="Check_Radio" style="float: right;"><label class="LabName"><span>字体颜色：</span></label></div></td>' +
				'<td>' +
					'<input id="toppingFieldFontColor" type="text" class="inp1" style="width: 127px;" value="#454E63" data-jscolor="{}"/>' +
				'</td>' +
			'</tr>' +
			'<tr>' +
				'<td><div class="Check_Radio" style="float: right;"><label class="LabName"><span>字体大小：</span></label></div></td>' +
				'<td>' +
					'<input id="toppingFieldFontSize" name="toppingFieldFontSize" type="text" value="" class="queryParam hide"/>' +
					'<input id="toppingFieldFontSizeName" type="text" class="inp1 selectWidth"/>' +
				'</td>' +
			'</tr>' +
		'</table>' +
		'<table width="100%" border="0" cellspacing="0" cellpadding="0">' +
			'<tr>' +
	    		'<td width="50%">' +
	    			'<a href="###" class="BigNorToolBtn BigJieAnBtn" style="float:right;" onclick="toppingSort();">确定</a>' +
	    		'</td>' +
	    		'<td>' +
	    			'<a href="###" onclick="closeToppingDiv();" class="BigNorToolBtn CancelBtn">取消</a>' +
	    		'</td>' +
	    	'</tr>' +
		'</table>' +
	'</div>');
	
	$("#jqueryToolbar tr.query4todo").removeClass('queryConfig').hide();
	
	var typesDictCode = "${typesDictCode!}";
    if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
        AnoleApi.initTreeComboBox("typeToppingFieldName", "typeToppingField", {
            "A001093199" : [${typesDictCode!}]
        }, null, null, {//0 展示指定的字典；1 去除指定的字典；
            FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
            EnabledSearch : true,
            ShowOptions: {
                EnableToolbar : true
            }
        });
    } else {
        AnoleApi.initTreeComboBox("typeToppingFieldName", "typeToppingField", "A001093199", null, null, {
            EnabledSearch : true,
            ShowOptions: {
                EnableToolbar : true
            }
        });
    }
    
    var sourceDictCode = "${sourceDictCode!}";
    if(isNotBlankStringTrim(sourceDictCode)) {
        AnoleApi.initTreeComboBox("sourceToppingFieldName", "sourceToppingField", {
            "A001093222" : [${sourceDictCode!}]
        }, null, null, {//0 展示指定的字典；1 去除指定的字典；
            ShowOptions: {
                EnableToolbar : true
            }
        });
    } else {
        AnoleApi.initTreeComboBox("sourceToppingFieldName", "sourceToppingField", "A001093222", null, null, {
        	ShowOptions: {
        		EnableToolbar : true
        	}
        });
    }
    
	AnoleApi.initListComboBox("toppingFieldFontSizeName", "toppingFieldFontSize", null, null, null, {
		DataSrc: [{"name":"13", "value":"13px"},{"name":"14", "value":"14px"},{"name":"15", "value":"15px"},{"name":"16", "value":"16px"},{"name":"17", "value":"17px"},{"name":"18", "value":"18px"},{"name":"19", "value":"19px"},{"name":"20", "value":"20px"}]
	});
	
	var isQueryConfigChecked = "checked";
	
	if($('#queryConfig').length > 0) {
		isQueryConfigChecked = "";
	}
	
	$('#advanceSearchTable tr.queryConfig').each(function() {
    	var configName = $(this).attr('configName');
    	var queryConfigItem = '<tr>' + 
    						  	'<td><div class="Check_Radio"><label class="LabName"><span style="cursor: pointer;"><input type="checkbox" style="cursor: pointer;" value="' + configName + '" ' + isQueryConfigChecked + ' />' + configName + '</span></label></div></td>' +
    						  '</tr>';
    	
    	if($('#queryConfigTable input[type=checkbox][value=' + configName + ']').length == 0) {
    		$('#queryConfigTable').append(queryConfigItem);
    	}
    });
    
    if($('#advanceSearchTable tr.queryConfig').length % 2 == 1) {
    	$('#queryConfigTable').append('<tr><td></td></tr>');
    }
    
    var options = { 
        axis : "yx", 
        theme : "minimal-dark" 
    }; 
    enableScrollBar('queryConfigTableDiv',options);
    
    queryConfig();
</@override>

<@override name="exclusiveFunction">
	function openQueryConfigDiv() {
    	$("#queryConfigDiv").css({'top':$(window).height()/4});
    	$("#queryConfigDiv").show();
    }
    
    function closeQueryConfigDiv() {
    	$("#queryConfigDiv").hide();
    }
    
    function queryConfig() {
    	var checkedItem = $('#queryConfigTable input[type=checkbox]:checked');
    	
    	$('#advanceSearchTable tr.queryConfig').addClass('isHide').removeClass('isShow').hide();
    	$('#advanceSearchTable tr.queryConfigSub').hide();
    	
    	checkedItem.each(function() {
    		$('#advanceSearchTable tr.queryConfig[configName=' + $(this).val() + ']').addClass('isShow').removeClass('isHide').show();
    	});
    	
    	$('#advanceSearchTable tr.queryConfig.isHide input').val('');
    	
    	if(checkedItem.length == 0) {
        	$('#advanceSearchLi').hide();
        } else {
        	$('#advanceSearchLi').show();
        }
        
        closeQueryConfigDiv();
    }
    
    function openToppingDiv() {
    	$("#toppingFieldDiv").css({'top':$(window).height()/4});
    	$("#toppingFieldDiv").show();
    }
    
    function closeToppingDiv() {
    	$("#toppingFieldDiv").hide();
    }
    
    function toppingSort() {
    	closeToppingDiv();
		conditionSearch();
    }
    
    function checkToppingFieldRadio(radio) {
    	$('#' + $(radio).attr('dictNameId')).removeAttr('disabled');
    	
    	$('#toppingFieldDiv input[type=radio][name=toppingFieldRadio]').not('input:checked').each(function() {
    		$('#' + $(this).attr('dictId')).val('');
    		$('#' + $(this).attr('dictNameId')).attr('disabled', 'true');
    		$('#' + $(this).attr('dictNameId')).val('');
    	});
    }
</@override>

<@extends name="/zzgl/event/list_event.ftl" />