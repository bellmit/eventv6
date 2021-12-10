<script type="text/javascript" src="${uiDomain}/js/openJqueryEasyUIWin.js"></script>
<script type="text/javascript">
	function initLabelLeft(leftMenuDivWidth){
	
		if($('#leftMenuDD > p').length > 1) {
        	$('#leftMenuDiv').show();
        	
	        $('#leftMenuDD > p').each(function() {
	        	var eventLabelType = $(this).attr('labelTypeName');
	        	
	        	if(isNotBlankStringTrim(eventLabelType)) {
	        		$('#eventLabelContentIncludeDiv div.' + eventLabelType).show();
	        	}
	        });
	        
	        leftMenuDivWidth = $('#leftMenuDiv').outerWidth(true);
        } else {
        	$('#eventBasicInfo').hide();
        }
        
        //左方菜单点击相应事件
		$('#leftMenuDD > p').on('click', function() {
			$(this).addClass("current finish").siblings().removeClass("current finish");
		});
        
        return leftMenuDivWidth;
	}
	//判断是否三证合一  是返回true
    function isThreeMerge() {
        var isCheck = $("#YorN").attr("checked");
        var YorN = (isCheck) ? "1" : "0";
        if (YorN == '1') {
            return true;
        } else {
            return false;
        }
    }

    //查看人口信息
    function detailPartyIndividual(partyId) {
        //var url = "${SQ_ZZGRID_URL}/resident/detail/"+ciRsId+".jhtml";
        var url = "${RS_DOMAIN}/cirs/viewResidentSnippet.jhtml?partyId=" + partyId;

        if(typeof(parent.showCustomEasyWindow) != 'undefined') {
            parent.showCustomEasyWindow("人员基本信息",url,900,520);
        } else if(typeof(parent.showMaxJqueryWindow) != 'undefined') {
            parent.showMaxJqueryWindow("人员基本信息",url,900,520);
        } else if(typeof(openJqueryWindowByParams) != 'undefined') {
        	var opt={
        		height:520,
        		width:900,
        		maxHeight:document.body.scrollHeight,
        		maxWidth:document.body.scrollWidth,
        		targetUrl:url,
        		title:'人员基本信息'
        	}
       		openJqueryWindowByParams(opt);
        } else {
            $.messager.alert('错误','人员基本信息查看失败！', 'error');
        }
    }
</script> 