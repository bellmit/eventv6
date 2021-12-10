<script type="text/javascript">
	var inputNum;
	
	function initFunction(targetName, replaceName) {
		if(parent.titlePath && targetName && replaceName) {
			var titleHtml = $(parent.titlePath).html(),
				lastIndex = titleHtml.lastIndexOf(targetName);
			
			if(lastIndex >= 0) {
				titleHtml = titleHtml.substr(0, lastIndex) + replaceName + titleHtml.substr(lastIndex + targetName.length);
			}
			
			$(parent.titlePath).html(titleHtml);
		}
		
		var winHeight=window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-62;
       	$("#content").height(winHeight-56); 
	    loadMessage(1,$("#pageSize").val());
	}
	
	function pageSubmit(){
		inputNum = $("#inputNum").val();
		var pageCount = $("#pageCount").text();
		if(isNaN(inputNum)){
			inputNum=1;
		}
		if(parseInt(inputNum)>parseInt(pageCount)){
			inputNum=pageCount;
		}
		if(inputNum<=0||inputNum==""){
			inputNum=1;
		}
		change('4');
	}
	
	$('#conditionDiv input[type="text"]').filter(".queryParam").keydown(function(e){ 
		if(e.keyCode==13){ 
			loadMessage(1,$("#pageSize").val());
		} 
	}); 
	
	$("#moreSearch").toggle(function(){
	    $(".AdvanceSearch").css("display","block");
	},function(){
	   $(".AdvanceSearch").css("display","none");
	});
	
	function ShowOrCloseSearchBtn(){
		var temp= $(".ListSearch").is(":hidden");//是否隐藏 
		if(temp == false) {
			$(".ListSearch").hide();
		}else {
			$(".ListSearch").show();
		}
	}
	
	function CloseSearchBtn(){
		$(".ListSearch").hide();
	}
	
	var currentPageNum=1;
	 //分页
     function change(_index){
        var flag;
        var pagenum = $("#pagination-num").text();
        var lastnum = $("#pageCount").text();
        var pageSize = $("#pageSize").val();
		var firstnum = 1;
		switch (_index) {
			case '1':		//上页
			    if(pagenum==1){
			      flag=1;
			      break;
			    }
				pagenum = parseInt(pagenum) - 1;
				pagenum = pagenum < firstnum ? firstnum : pagenum;
				break;
			case '2':		//下页
			    if(pagenum==lastnum){
			      flag=2;
			      break;
			    }
				pagenum = parseInt(pagenum) + 1;
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
		    case '3':
		        flag=3;
		        pagenum=1;
		        break;
		    case '4':
		        pagenum = inputNum;
		        if(pagenum==lastnum){
			      flag=4;
			      break;
			    }
				pagenum = parseInt(pagenum);
				pagenum = pagenum > lastnum ? lastnum : pagenum;
				break;
			default:
				break;
		}
		
		if(flag==1){
			alert('当前已经是首页！');
		  	return;
		}else if(flag==2){
			alert('当前已经是尾页！');
			return;
		}
		currentPageNum = pagenum;
	    loadMessage(pagenum,pageSize);
	}
	
	function selected(controlApplyId, controlTargetId){
		var url = '${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/toTargetRecordList.jhtml?gridId='+$("#gridId").val()+"&elementsCollectionStr="+$("#elementsCollectionStr").val()+"&controlApplyId="+controlApplyId;
		
		if(controlTargetId) {
			url += "&controlTargetId=" + controlTargetId;
		}
		
		var winHeight = window.parent.document.getElementById('map'+window.parent.currentN).offsetHeight-32;
		
		var params = {
			title: "捕获详情列表",
			targetUrl: url,
			height: winHeight,
			width: 375,
			top: 32,
			left: 0,
			modal: false,
			collapsible: true,
			resizable: false
		}
		
		parent.closeMaxJqueryWindow();//关闭前一次打开的窗口
		parent.showMaxJqueryWindowByParams(params);
	}
	
	function dateFormat(value) {
		var formatDate = "";
                	
    	if(value) {
    		var time = new Date(value),
				month = time.getMonth() + 1,
				day = time.getDate(),
				hour = time.getHours(),
				min = time.getMinutes(),
				second = time.getSeconds();
			
			if(month < 10) {
				month = "0"+month;
			}
			if(day < 10) {
				day = "0"+day;
			}
			if(hour < 10) {
				hour = "0"+hour;
			}
			if(min < 10) {
				min = "0"+min;
			}
			if(second < 10) {
				second = "0"+second;
			}
			
			formatDate = month+"-"+day+" "+hour+":"+min+":"+second;
    	}
    	
    	return formatDate;
	}
	
	function changeList(pattern) {
		var url = "${rc.getContextPath()}/zhsq/map/arcgis/arcgisDataOfSituationController/toArcgisDataListOfTargetRef.jhtml?gridId="+$("#gridId").val()+"&elementsCollectionStr="+$("#elementsCollectionStr").val();
		
		if(pattern) {
			url += "&pattern=" + pattern;
		}
		
		window.location = url;
	}
	
	function selectControlStatus(obj){
		$(obj).toggleClass("current");
	}
</script>