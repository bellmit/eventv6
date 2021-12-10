var map = new Map(),globalIndex = 0;

$(function(){
	//核查内容添加滚动条
	$('.exa-cont').niceScroll({
	    cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
	    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
	    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
	    cursorwidth: "5px", //像素光标的宽度
	    cursorborder: "0", // 游标边框css定义
	    cursorborderradius: "5px",//以像素为光标边界半径
	    autohidemode: false //是否隐藏滚动条
	});
	
	$(document).click(function(e){
		var calendar = $('#my-calendar'),
			$dateIpt = $('#datePicker');
		
		if (!($dateIpt.is(e.target) || $dateIpt.has(e.target).length)) {
			if(!calendar.is(e.target) && calendar.has(e.target).length === 0){
				calendar.css('display','none');
			}
		}
		
	});
	
	
	$('div[id="attas"]').find('div.pic_content').each(function(index) {
		$(this).hover(function() {
			$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
		}, function() {
			$('.pic_content').eq(index).find('.off_btn').addClass('dn');
		});
	});
	

	new AjaxUpload($("#chooeseAtta"),{//异步上传
		action: base + '/file/uploadFile.jhtml?limitSize=5',
		name: 'uploadFile',
		onSubmit : function(file, ext){
			/* var exts = "jpg|png",paths = "|";
			if ( !RegExp( "\.(?:" + exts + ")$$", "i" ).test(file)){
				$.messager.alert("上传失败","只能上传以下类型：" + exts.replace(/\|/g,"或"));
				return false;
			} */
	 	},
	 	onComplete: function(file,response,statusText){
			//var jsonData = $.parseJSON(response);
			if(response.indexOf('<pre')>=0){
				var reg = /<pre.+?>(.+)<\/pre>/g;//去除浏览器自带pre标签,擦
				var result = response.match(reg);
				response = RegExp.$1;
			}
			var jsonData = $.parseJSON(response);
			if(jsonData.result=='success'){
				var att = jsonData.resultList[0];
				var index = (globalIndex++);
				var html = '';
				html +='<div class="pic pic_content" id="pic_'+index+'">';
				html +='	<span class="wl_upload">';
                if (att.path.endWith('.bmp') || att.path.endWith('.jpg')
                    || att.path.endWith('.jpeg') || att.path.endWith('.png')|| att.path.endWith('.gif')) {
                    html +='		<img style="width:56px;height:36px;" src="'+getFilePath(att.path)+'" />';
                } else{
                    html +='		<img src="'+getFilePath(att.path)+'" />';
                }
				html +='		<p title="'+att.fileName+'">'+att.fileName+'</p>';
				html +='	</span>';
				html +='	<div class="off_btn displ dn"></div>';
				html +='	<input type="hidden" name="attList['+index+'].title" value="'+att.fileName+'" />';
				html +='	<input type="hidden" name="attList['+index+'].fileName" value="'+att.fileName+'" />';
				html +='	<input type="hidden" name="attList['+index+'].filePath" value="'+att.path+'" />';
				html +='</div>';
				$("#attas").append(html);
        	}else if(jsonData.result=='limit'){
        		$.messager.alert("上传失败","原因：文件大于"+jsonData.limit+"M");
        	}else{
        		$.messager.alert("上传失败","原因："+jsonData.result);
        	}
		}   
	});
	
	//删除附件
	$("#attas").on("click",".off_btn",function(){
		$pObj = $(this).parent();
		$.messager.confirm('提示','确定删除该附件?',function(r){
		    if (r){
		    	$pObj.remove();
		    }
		});
	});
	
	//接收地区选择事件
	$('.wl-files-area').on('click','input.ipt_checkbox',function(){
		var len = $('input.ipt_checkbox').length,
			cLen = $('input.ipt_checkbox:checked').length;
		if (cLen == len) {
			$('#area-all').trigger('click');
			//$('#area-all').attr("checked",'true');
		} else {
			$('#area-all').removeAttr('checked');
		}
		
	});
});

function chooseAll(node) {
	var isChecked = $(node).is(':checked');
	$('ul.wl-files-area').find('input').each(function(){
		$(this).prop("checked",isChecked);
	});
}

/**
 * 日期选择回调方法
 * @param node
 */
function dateCallBackFunc(node){
	var currDate =  new Date().format('yyyy-MM-dd'),
		dateSelected = node.getAttribute('data-date');
	if (dateSelected <= currDate) {
		$.messager.alert("警告",'完成时限必须大于当前日期',"warning");
	} else {
		var diffDays = datedifference(dateSelected,currDate);
		$('.wl-files-date-c').html('（共'+diffDays+'个工作日）');
		$('#datePicker').val(dateSelected);
		$('#my-calendar').css('display','none');
	}
}

/**
 * WdatePicker.js
 * 日期选择回调方法
 */
function pickedCallBack() {
	var currDate =  new Date().format('yyyy-MM-dd'),
		dateSelected = $('#completeDate').val();
	if (dateSelected <= currDate) {
		$('#datePicker').val('');
		$.messager.alert("警告",'完成时限必须大于当前日期',"warning");
	} else {
		var diffDays = datedifference(dateSelected,currDate);
		$('.wl-files-date-c').html('（共'+diffDays+'个工作日）');
	}
}