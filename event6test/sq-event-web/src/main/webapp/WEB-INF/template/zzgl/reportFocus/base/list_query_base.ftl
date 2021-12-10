<script type="text/javascript">
    function doSearch(queryParams, isCurrent) {
    	$('#list').datagrid('clearSelections');
    	$("#list").datagrid('options').queryParams = queryParams;
        if("1"==queryParams.superviseMark){
            $("#superviseMarkHidden").val(queryParams.superviseMark);
        }else{
            $("#superviseMarkHidden").val("");
        }

    	btnAuthority();
    	
    	if(isCurrent && isCurrent == true) {
    		$("#list").datagrid('reload');
    	} else {
    		$("#list").datagrid('load');
    	}
    }
	
    function conditionSearch(isCurrent) {//查询
        var iconDiv = $("#iconDiv > a[class='icon_select']"),
        	searchParam = {};
        
        if(iconDiv.length > 0) {
        	eval('(' + iconDiv.eq(0).attr('click') + ')(' + iconDiv.eq(0).attr('id') + ',' + isCurrent + ')');
        } else {
        	searchData(isCurrent, searchParam);
        }
    }
    
    function searchData(isCurrent, queryParamObj) {//查询
    	var queryParam = $.extend({}, queryData(), queryParamObj);
    	
    	doSearch(queryParam, isCurrent);
    }
    
    function reloadDataForSubPage(msg, isCurrent) {
    	try {
    		closeMaxJqueryWindow();
    	} catch(e) {}
    	
    	if(msg) {
    		DivShow(msg);
    	}
    	
    	conditionSearch(isCurrent);
    }
	
    function resetCondition(formId) {//重置
    	clear4DateRender(formId);
    	$('#' + formId)[0].reset();
    	$('#keyWord').addClass('keyBlank');
    	
    	allSearchData($("#_allSearchAnchor"));
    }
    
    function _onkeydown() {
    	var keyCode = event.keyCode;
    	if(keyCode == 13) {
    		conditionSearch();
    	}
    }
    
    function _onfocus(obj) {
    	if($(obj).hasClass("keyBlank")) {
    		$(obj).val("");
    		$(obj).removeClass('keyBlank');
    	}
    }
    
    function _onblur(obj) {
    	var keyWord = $(obj).val();
    	
    	if(keyWord == '') {
    		$(obj).addClass('keyBlank');
    		$(obj).val($(obj).attr("defaultValue"));
    	}
    }
    
    function init4DateRender(formId) {
    	var startId = null, endId = null;
    	
    	$('#' + formId + ' .dateRenderWidth').each(function() {
    		startId = $(this).attr('startId');
    		endId = $(this).attr('endId');
    		
    		if(startId && endId) {
    			if($('#' + startId).length == 0) {
    				$(this).append('<input class="inp1 hide queryParam" type="text" id="' + startId + '" name="' + startId + '" ></input>');
    			}
    			if($('#' + endId).length == 0) {
    				$(this).append('<input class="inp1 hide queryParam" type="text" id="' + endId + '" name="' + endId + '" ></input>');
    			}
    			
    			init4DateRenderSingle($(this).attr('id'), startId, endId);
    		}
    	});
    }
    
    function init4DateRenderSingle(renderId, startTimeId, endTimeId, opt) {
    	var dateRender = null;
    	
    	dateRender = $('#' + renderId).anoleDateRender({
    		BackfillType : "1",
    		ChoiceType : "1",		// 选择方式（0-起始和结束时间必须都有，1-起始和结束时间有一个即可，2-起始和结束时间随意）
    		ShowOptions : {
    			TabItems : ["常用", "年", "季", "月", "清空"]
    		},
    		BackEvents : {
    			OnSelected : function(api) {
    				$("#" + startTimeId).val(api.getStartDate());
    				$("#" + endTimeId).val(api.getEndDate());
    			},
    			OnCleared : function() {
    				$("#" + startTimeId).val('');
    				$("#" + endTimeId).val('');
    			}
    		}
    	}).anoleDateApi();
    	
    	return dateRender;
    }
    
    function clear4DateRender(formId) {
    	$('#' + formId + ' .dateRenderWidth').each(function() {
    		$(this).anoleDateApi(true).doClear();
    	});
    }
</script>