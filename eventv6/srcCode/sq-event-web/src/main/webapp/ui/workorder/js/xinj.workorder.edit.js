var globalIndex = 0,currDate =  new Date().format('yyyy-MM-dd');

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
	
	if (cDate >= currDate) {
		$('.wl-files-date-c').html('（共'+datedifference(cDate,currDate)+'个工作日）');
	} else {
		$('.wl-files-date-c').html('（完成时限必须大于当前日期）');
	}
	
	//核查类型点击事件
	$('#warn_parent').on('click', 'li>a', function(){
		if (!$(this).hasClass('active')) {
			$(this).addClass('active').parent().siblings().children().removeClass('active');
			$('#warn_contents_choose').html('');
			getParentDataList($(this).attr('data-type'));
		}
	});
	
	//核查子类型点击事件
	$('#warn_child').on('click', 'li>a', function(){
		if (!$(this).hasClass('active')) {
			$(this).addClass('active').parent().siblings().children().removeClass('active');
			getWarnContents();
		}
	});
	
	$('#datePicker').click(function(){
		if ($('#my-calendar').find('table[id="myCalendar"]').length) {
			$('#my-calendar').css('display','block');
		} else {
			var width = $(this).width(),
			top = $(this).offset().top + 40;
			$('#my-calendar').css({'top':top+'px','position':'absolute','z-index':'99999','background':'white','border':'#c5d9e8 1px solid'});
			new MyCendae(document.body,{tableWidth:width+'px',tableHeight:'300px',event:'onclick',callBackFunc:'dateCallBackFunc(this);' });
		}
	});
	
	$('div[id="attas"]').find('div.pic_content').each(function(index) {
		$(this).hover(function() {
			$('.pic_content').eq(index).find('.off_btn').removeClass('dn');
		}, function() {
			$('.pic_content').eq(index).find('.off_btn').addClass('dn');
		});
	});
	
	//加载核查子类型数据
	getParentDataList();
	
	//左侧选择核查内容事件
	$("#warn_contents_choose").on("dblclick","p",function(){
		var $p = $(this),id = $(this).attr('id');
		if (!$p.hasClass('active')) {
			$p.addClass('active');
			$p.html($p.html()+'<i class="exa_on"></i>');
			
			var arr = id.split('@'),typeP = arr[1],html = '';
			
			var $typePObj = $("#warn_list_choosed").find('div[id="c_'+typeP+'"]');
			//debugger;
			if ($typePObj && $typePObj.length) {//父类已存在，直接append核查内容
				html += '<li>';
				html += '	<p id="'+id+'">'+$p.text()+'<a href="javascript:void(0);" class="exa_close"></a></p>';
				html += '</li>';
				$('ul[id="ul_c_'+typeP+'"]').find("li").last().after(html);
			} else {//父类不存在，添加父类及核查内容
				
				var typeCn = $('div[id="'+typeP+'"]').find("a").first().text(),
					typePCn = $('div[id="'+typeP+'"]').find("a").last().text();
		
			    html += '<div class="exa-sec">';
		    	html += '	<div id="c_'+typeP+'" class="exa-sec-header flex flex_ac">';
	    		html += '		<span></span>';
    			html += '		<a href="javascript:void(0);">'+typeCn+'</a>';
				html += '		<i></i>';
				html += '		<a href="javascript:void(0);">'+typePCn+'</a>';
				html += '	</div>';
				html += '	<ul id="ul_c_'+typeP+'" class="exa-sec-body">';
				html += '		<li>';
				html += '			<p id="'+id+'">'+$p.text()+'<a href="javascript:void(0);" class="exa_close"></a></p>';
				html += '		</li>';
				html += '	</ul>';
				html += '</div>';
				
				var $listObj = $('#warn_list_choosed').find('div[class="exa-sec"]');
				if ($listObj && $listObj.length) {//已存在其他类
					$listObj.last().after(html);
				} else {//已选内容为空
				   $('#warn_list_choosed').html(html);
				}
			}
			
			map.put(id, id);
			
			$('#warn_list_choosed').css({'overflow':'hidden','margin-bottom':'30px'});
		} else {
			//debugger;
			var activeLiLen = $p.closest('ul').find('p.active').length;
			if (activeLiLen > 1) {
				$("#warn_list_choosed").find('p[id="'+id+'"]').parent().remove();
			} else {
				$("#warn_list_choosed").find('p[id="'+id+'"]').parent().parent().parent().remove();
			}
			map.removeByKey(id);
			$p.removeClass('active');
			$p.find("i").remove();
		}
		
		//加载区域数据
		getRegionData();
		//$('#warn_list_choosed').css('overflow','hidden');
	});
	
	//右侧移除核查内容事件
	$("#warn_list_choosed").on("click",".exa_close",function(){
		var id = $(this).parent().attr('id');
		//debugger;
		var $p = $('#warn_contents_choose').find('p[id="'+id+'"]');
		$p.removeClass('active');
		$p.find('i').remove();
		if ($(this).closest("ul").find("li").length > 1) {
			$(this).closest("li").remove();
		} else {
			$(this).closest("ul").parent().remove();
		}
		map.removeByKey(id);
		
		//加载区域数据
		getRegionData();
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
				//console.log(att);
				var index = (globalIndex++),
					fileObj = getFileObj(att.path);
					html = '';
				var filePath = 'img' == fileObj.fileTye ? fileObj.path : att.path;
				html +='<div class="pic pic_content" id="pic_'+index+'">';
				html +='	<span title="'+fileObj.title+'" onclick="viewOrDownLoad(\''+filePath+'\',\''+fileObj.fileTye+'\');" class="wl_upload">';
                if(fileObj.fileType== 'img'){
                    html +='		<img style="width:56px;height:36px;" src="'+fileObj.path+'" />';
                }else{
                    html +='		<img  src="'+fileObj.path+'" />';
                }
				html +='		<p>'+att.fileName+'</p>';
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
		var attId = $(this).attr('data-id');
			$pObj = $(this).parent();
		$.messager.confirm('提示','确定删除该附件?',function(r){
		    if (r){
		    	if (attId) {
		    		modleopen();
		    		var url = '/gmis/rgWorkOrderStandard/delAtt.json';
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
	
	//表单保存/提交
	$('ul.wl-submit').on('click','li',function(){
		
		var dataType = $(this).attr('data-type');
		if (dataType == '3') {//关闭窗口
			parent.closeMaxJqueryWindow();
			return;
		}
		
		var workOrderTitle = $('#workOrderTitle').val();
		if (!checkStrLenAndNull('核查工单名称',workOrderTitle,200))
			return;
		
		var dataType = $(this).attr('data-type');
		var msg = dataType == '1' ? '确定将标准化工单保存为草稿?' : '确定发布该标准化工单?',
			workOrderStatus = dataType == '1' ? '000' : '020';
		var $pObjList = $('#warn_list_choosed').find('p');
		if (!$pObjList.length) {
			$.messager.alert("警告","请添加核查内容","warning");
			return;
		}
		var checkContent = $('#checkContent').val();
		if (!checkStrLenAndNull('备注',checkContent,1000))
			return;
		
		var completeDate = $("#completeDate").val();
		if (!completeDate) {
			$.messager.alert("警告","请选择完成时限","warning");
			return;
		}
		
		var $regionIpts = $('input.ipt_checkbox:checked');
		if (!$regionIpts.length) {
			$.messager.alert("警告","请先选择接收地区","warning");
			return;
		}
		
		$.messager.confirm('提示',msg,function(r){
		    if (r){
		    	modleopen();
		    	try {
		    		var warnFormHtml = '';
			    	$('#warn_list_choosed').find('p').each(function(index){
			    		//w.type + '@' + w.typeP + '@' + w.typeS + '@' + w.matchFlag;
			    		var id = $(this).attr('id');
			    		if (!id) return true;
			    		var arr = id.split('@');
			    		var type = arr[0], typeP = arr[1], typeS = arr[2], matchFlag = arr[3];
			    		warnFormHtml += '<input type="hidden" id="type_'+index+'" name="warnList['+index+'].type" value="'+type+'">';
			    		warnFormHtml += '<input type="hidden" id="typeP_'+index+'" name="warnList['+index+'].typeP" value="'+typeP+'">';
			    		warnFormHtml += '<input type="hidden" id="typeS_'+index+'" name="warnList['+index+'].typeS" value="'+typeS+'">';
			    		warnFormHtml += '<input type="hidden" id="matchFlag_'+index+'" name="warnList['+index+'].matchFlag" value="'+matchFlag+'">';
			    	});
			    	$('#warnFormSpan').html(warnFormHtml);
			    	
			    	$('#workOrderStatus').val(workOrderStatus);
			    	
			    	var waitOrgCodes = [];
			    	$regionIpts.each(function(){
			    		if ($(this).attr('id') == 'area-all')
			    			return true;
			    		if ($(this).is(':checked')) {
			    			waitOrgCodes.push($(this).parent().attr('data-info'));
			    		}
			    	});
			    	$('#waitOrgCodes').val(waitOrgCodes.join(','));
			    	
			    	
			    	var url = "/gmis/rgWorkOrderStandard/update.json";
			    	ajaxPostRequest(url,$("#checkFormId").serialize(),function(data){
			    		modleclose();
			    		if (data.state) {
			    			$.messager.alert("成功",(dataType=='1'?'保存成功':'发布成功'),"success",function(){
			    				window.parent.window.searchData();
			    				parent.closeMaxJqueryWindow();
			    			});
			    		} else {
			    			$.messager.alert("错误",data.errMsg,"error");
			    		}
			    	},false);
			    	
				} catch (e) {
					modleclose();
				} finally {
					modleclose();
				}
		    }
		});
	});
});


//加载核查子类型数据方法
function getParentDataList(){
	var type  = $('ul#warn_parent li').find('a.active').attr('data-type');
	if (type) {
		var url = '/gmis/rgWorkOrderStandard/getParentDataList.json';
		ajaxPostRequest(url,{'type':type},function(data){
			var html = '<li><a href="javascript:void(0);" data-type="">全部</a></li>';
			if (data) {
				for (var i = 0,len = data.length; i < len ; i++) {
					var obj = data[i];
					html += '<li><a href="javascript:void(0);" data-type="'+obj.typeP+'">'+obj.typePCn+'</a></li>';
				}
			}
			$('#warn_child').html(html);
			modleclose();
		},true);
	}
}

//加载核查内容数据方法
function getWarnContents() {
	var type  = $('ul#warn_parent li').find('a.active').attr('data-type'),
		typeP  = $('ul#warn_child li').find('a.active').attr('data-type'),
		url = '/gmis/rgWorkOrderStandard/getWarnContents.json';
	ajaxPostRequest(url,{'type':type,'typeP':typeP},function(data){
		var html = '';
		if (data && data.length) {
			for (var i = 0,len = data.length; i < len ; i++) {
				var warnObj = data[i];
				html += '<div class="exa-sec">';
				html += '	<div id="'+warnObj.typeP+'" class="exa-sec-header flex flex_ac">';
				html += '		<span></span>';
				html += '		<a href="javascript:void(0);">'+warnObj.typeCn+'</a>';
				html += '		<i></i>';
				html += '		<a href="javascript:void(0);">'+warnObj.typePCn+'</a>';
				html += '	</div>';
				html += '	<ul id="ul_'+warnObj.typeP+'" class="exa-sec-body">';
				var warnList = warnObj.warnHyList;
				for (var j = 0 , jLen = warnList.length ; j < jLen ; j++) {
					var w = warnList[j];
					//"type": "SJ","typeP": "T_FP_POOR_PSB","typeS": "MATCH_FLG","matchFlag": "2"
					var flag = w.type + '@' + w.typeP + '@' + w.typeS + '@' + w.matchFlag;
					if (map.get(flag)) {
						html += '<li><p id="'+flag+'" class="active">'+w.warnTitle+'<i class="exa_on"></i></p></li>';
					} else {
						html += '<li><p id="'+flag+'">'+w.warnTitle+'</p></li>';
					}
				}
				html += '	</ul>';
				html += '</div>';
			}
		}
		$('#warn_contents_choose').html(html);
		modleclose();
	},true);
}

function chooseAll(node) {
	var isChecked = $(node).is(':checked');
	$('ul.wl-files-area').find('input').each(function(){
		$(this).prop("checked",isChecked);
	});
}

/**
 * 加载该类型贫困户的地区及各个地区贫困户数量
 */
function getRegionData() {
	
	var html = '<li>';
		html += '	<input onclick="chooseAll(this);" class="magic-checkbox" type="checkbox" id="area-all">';
		html += '	<label for="area-all">全选</label>';
		html += '</li>';
	
	var $pObjList = $('#warn_list_choosed').find('p');
	if (!$pObjList.length) {
		$('ul.wl-files-area').html(html);
		return;
	}
	
	try {
		var warnFormHtml = '';
		$('#warn_list_choosed').find('p').each(function(index){
			//w.type + '@' + w.typeP + '@' + w.typeS + '@' + w.matchFlag;
			var id = $(this).attr('id');
			if (!id) return true;
			var arr = id.split('@');
			var type = arr[0], typeP = arr[1], typeS = arr[2], matchFlag = arr[3];
			warnFormHtml += '<input type="hidden" id="type_'+index+'" name="warnList['+index+'].type" value="'+type+'">';
			warnFormHtml += '<input type="hidden" id="typeP_'+index+'" name="warnList['+index+'].typeP" value="'+typeP+'">';
			warnFormHtml += '<input type="hidden" id="typeS_'+index+'" name="warnList['+index+'].typeS" value="'+typeS+'">';
			warnFormHtml += '<input type="hidden" id="matchFlag_'+index+'" name="warnList['+index+'].matchFlag" value="'+matchFlag+'">';
		});
		//console.log(warnFormHtml)
		$('#regionDataForm').html(warnFormHtml);
		
		var url = '/gmis/rgWorkOrderStandard/getRegionData.json';
		ajaxPostRequest(url,$('#regionDataForm').serialize(),function(data){
			//console.log(data);
			if (data) {
				for (var k = 0, kLen = data.length; k < kLen ; k++) {
					var obj = data[k];
					html += '<li data-info="'+obj.ORG_CODE+'-'+obj.ORG_NAME+'">';
					html += '	<input class="magic-checkbox ipt_checkbox" type="checkbox" id="area'+k+'">';
					html += '	<label for="area'+k+'">'+obj.ORG_NAME+'（'+obj.CNT_+'户）</label>';
					html += '</li>';
				}
				$('ul.wl-files-area').html(html);
			}
			modleclose();
		},true);
	} catch (e) {
	} finally {
		//modleclose();
		$('#regionDataForm').html('');
	}
}

/**
 * 日期选择回调方法
 * @param node
 */
function dateCallBackFunc(node){
	var dateSelected = node.getAttribute('data-date');
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