var map = new Map(),globalIndex = $('#global_index').val();

$(function(){
	
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
			var title = '';
			var dispose = $('input:radio[name="eventSeq"]:checked').val();
			if(dispose != undefined){
				if(dispose == 1){
					title = "[处理前]";
				}
				if(dispose == 2){
					title = "[处理中]";
				}
				if(dispose == 3){
					title = "[处理后]";
				}
			}
			if(jsonData.result=='success'){
				var att = jsonData.resultList[0];
				var index = (globalIndex++) + 1,
				fileObj = getFileObj(att.path);
				html = '';
				var filePath = 'img' == fileObj.fileTye ? fileObj.path : att.path;
				fileName = att.fileName;
				html +='<div class="pic pic_content" id="pic_'+index+'">';
				if(fileObj.fileType== 'img'){
					html +='	<span title="'+fileObj.title+'" onclick="viewOrDownLoad(\''+filePath+'\',\''+fileObj.fileTye+'\');" class="wl_upload">';
                    html +='		<img style="width:70px;height:50px;" src="'+fileObj.path+'" />';
                }else{
	                html +='	<span title="'+fileObj.title+'" onclick="downLoadFile(\''+filePath+'\',\''+fileName+'\');" class="wl_upload">';
	                html +='		<img  src="'+fileObj.path+'" />';
                }
				html +='		<p>'+title+att.fileName+'</p>';
				html +='	</span>';
				html +='	<div class="off_btn displ dn"></div>';
				html +='	<input type="hidden" name="attList['+index+'].title" value="'+title+att.fileName+'" />';
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
	
	$('div[id="attas"]').find('div.pic_content').each(function(index) {
		$(this).hover(function() {
			$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
		}, function() {
			$('.pic_content').eq(index).find('.off_btn').addClass('dn');
		});
	});
	
	//删除附件
	$("#attas").on("click",".off_btn",function(){
		var attId = $(this).attr('data-id');
		debugger
		$pObj = $(this).closest(".imgs_div");
		$.messager.confirm('提示','确定删除该附件?',function(r){
		    if (r){
		    	if (attId) {
		    		modleopen();
		    		var url = '/zhsq/disputeMediation/delAtt.json';
		    		ajaxPostRequest(url,{'attId':attId},function(data){
		    			if (data) {
		    				$pObj.remove();
		    			} else {
		    				$.messager.alert("错误","附件删除失败","error");
		    			}
		    			modleclose();
		    		},false);
				} else {
					$pObj.remove();
				}
		    }
		});
	});
	
});


	//文件下载
	function downLoadFile(filePath,fileName) {
		var attaId ='';
		//fileName = stripscript(fileName);
		$('#attFileUrl').val(filePath);
		$('#attName').val(fileName);
		$('form#attForm').submit();
	}
	
	function stripscript(fileName){ 
	    var pattern = new RegExp("[#@$^]") 
	    var rs = ""; 
	    for (var i = 0; i < fileName.length; i++) { 
	        rs = rs+fileName.substr(i, 1).replace(pattern, '1'); 
	    } 
	    return rs; 
	}
	
