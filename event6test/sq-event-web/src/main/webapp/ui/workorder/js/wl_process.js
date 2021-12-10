$(function(){
	var wlClientY, wlArr, wlDate, wlH, wlTop;
	$('.container-wl').on('click', '.wl-pro-o', function(){
		var xthis = $(this);
		wlClientY = xthis.offset().top; //获取按钮距离顶部的高度
   		wlArr = xthis.parentsUntil('.wl-pro-wrap'); //获取祖先‘wl-pro-wrap’下所有节点
   		$(wlArr[wlArr.length - 1]).nextAll().remove(); //删除本级往后的所有节点
   		var url = basePath+'/gmis/rgWorkOrder/getChildrenOrderTransact.jhtml';
   		wlDate = [];
   		var trId = xthis.attr('data-transactId');
   		if(!trId || trId == ''){
   			return;
   		}
   		var layIndex1 = null;
   		$.ajax({
   			url:url,
   			async:true,
   			type:'post',
   			dataType:'JSON',
   			data:{transactId:trId},
   			success:function(res){
   				if(res && res.length>0){
   					for(var i=0;i<res.length;i++){
   						wlDate.push({'add':res[i].orgName,transactId:res[i].transactId,'data':res[i],'workOrderOperationList':res[i].workOrderOperationList,'childrens':res[i].childrens});
   					}
   					xthis.parents('.wl-pro-wrap').append( //添加新的地市级
   	   						'<div class="wl-pro-lev2">' +
   	   							'<div class="wl-pro-sec2">' +
   	   								'<div class="wl-pro-sect">' +
   	   									'<div class="wl-bar"></div>' +
   	   									
   	   								'</div>' +
   	   							'</div>'+
   	   			    		'</div>'
   	   					);

                    for(var i=0; i<wlDate.length; i++){
                        var isLast = '';
                        var isLastDiv = '';

                        if(wlDate[i].childrens <= 0 ){
                            isLast='style="display: none;"';
                            isLastDiv='wl-pro-art-last';

                        }
   						$(wlArr[wlArr.length - 1]).next().find('.wl-pro-sect').append(
   							'<div class="wl-pro-art '+isLastDiv+'">' +
   								'<p>'+wlDate[i].add+'</p>' +
   								'<a href="javascript:void(0);" class="wl-pro-arti wl-pro-o" '+ isLast +' data-transactId="'+wlDate[i].transactId+'"></a>' +
   							'</div>'
   						);
   						$(wlArr[wlArr.length - 1]).next().find('.wl-pro-sect').children().last().data('opt',wlDate[i].workOrderOperationList);
   			   		}
   			   		wlH = $(wlArr[wlArr.length - 1]).next().find('.wl-pro-sec2').height(); //获取新生成的地市级的高度
   				    $(wlArr[wlArr.length - 1]).next().find('.wl-pro-sec2').css({'margin-top': wlClientY + 12 - wlH/2 - 53});
   				    wlTop = $(wlArr[wlArr.length - 1]).next().find('.wl-pro-sec2').offset().top; //获取新生成的地市级距离顶部的高度
   				    if(wlTop < 44){ //新成的地市级距离顶部的高度小于44时将margin-top设置成44
   				    	$(wlArr[wlArr.length - 1]).next().find('.wl-pro-sec2').css({'margin-top': 44});
   				    }
   				    //设置指示箭头的位置
   				    $(wlArr[wlArr.length - 1]).next().find('.wl-bar').css({'top': wlClientY + 11 - $(wlArr[wlArr.length - 1]).next().find('.wl-pro-sec2').offset().top});
   				    xthis.parent().siblings().find('.wl-pro-c').removeClass('wl-pro-c').addClass('wl-pro-o');
   				    xthis.removeClass('wl-pro-o').addClass('wl-pro-c');
   				}else{
   					xthis.hide().parent().addClass('wl-pro-art-last');
   				}
   			},
   			beforeSend:function(){
   				layIndex1 = layer.load(1);
   			},
   			complete:function(){
   				layer.close(layIndex1);
   			}
   		});
   		/*wlDate =[
   			{add: '维吾尔自治区1'},
   			{add: '维吾尔自治区2'},
   			{add: '维吾尔自治区3'},
   			{add: '维吾尔自治区4'},
   			{add: '维吾尔自治区5'},
   			{add: '维吾尔自治区6'},
   			{add: '维吾尔自治区7'},
   			{add: '维吾尔自治区8'},
   			{add: '维吾尔自治区9'}
   		];*/
		
	});
	
	$('.container-wl').on('click', '.wl-pro-c', function(){
		wlClientY = $(this).offset().top; //获取按钮距离顶部的高度
   		wlArr = $(this).parentsUntil('.wl-pro-wrap'); //获取祖先‘wl-pro-wrap’下所有节点
   		$(wlArr[wlArr.length - 1]).nextAll().remove(); //删除本级往后的所有节点
   		$(this).removeClass('wl-pro-c').addClass('wl-pro-o');
	});
	
	var getRecordHtml = function(list){
		if(!list || list.length==0)
			return '';
		var html = '';
		html+='<div class="wl-modal">' ;
		html+=		'<div class="wl-modal-fx"></div>' ;
		html+=		'<div class="wl-modal-con">' ;
		for(var i=0;i<list.length;i++){
			html+=			'<div class="wl-modal-sec">' ;
			html+=				'<div class="wl-modal-sec-h"><p>'+list[i].createTimeCn+'</p>';
			if(list[i].transactStatus=='051' || list[i].transactStatus=='052'){
				html+=					'<span class="wl-modal-orange">驳 回</span>';
			}else if(list[i].transactStatus=='040'){
				html+=					'<span class="wl-modal-green">通过</span>';
			}else if(list[i].transactStatus=='020'){
				html+=					'<span class="wl-modal-blue">派发</span>';
			}else if(list[i].transactStatus=='030'){
				html+=					'<span class="wl-modal-orange">待审核</span>';
			}else if(list[i].transactStatus=='070'){
				html+=					'<span class="wl-modal-green">归档</span>';
			}else{
				html+=					'<span class="wl-modal-orange">处理中</span>';
			}
			html+=					'<p>'+list[i].createUserCn+'</p></div>' ;
			html+=				'<ul class="wl-modal-sec-b">' ;
			html+=					'<li><i></i>'+list[i].operationContent+'</li>' ;
			html+=				'</ul>' ;
			html+=			'</div>' ;
		}
		html+=		'</div>' ;
		html+=	'</div>';
		return html;
	};
	
	var initI, artIndex, sectIndex;
	$('.container-wl').on('mouseenter', '.wl-pro-art', function(e){
   		wlArr = $(this).parentsUntil('.wl-pro-wrap'); //获取祖先‘wl-pro-wrap’下所有节点
		clearTimeout(initI); //清楚模态框删除的定时器
		$('.wl-modal').remove(); //删除模态框
		var data = $(this).data('opt');
		var html = getRecordHtml(data);
		if(html=='')
			return;
		$(this).prepend(html);
		
		$(this).find('.wl-modal-con').niceScroll({
		    cursorcolor: "rgba(0,0,0,.2)",//#CC0071 光标颜色
		    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
		    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		    cursorwidth: "5px", //像素光标的宽度
		    cursorborder: "0", // 游标边框css定义
		    cursorborderradius: "5px",//以像素为光标边界半径
		    autohidemode: false //是否隐藏滚动条
		});
		
		var wlCX, wlCY, wW, wH;
		wlCX = e.clientX;
		wlCY = e.clientY;
		wW = $(window).width();
		wH = $(window).height();
		
		if($(this).parents().hasClass('wl-pro-lev1')){
			if(wlCX < 280){
				$(this).children('.wl-modal').css({'right': -430});
				$(this).find('.wl-modal-fx').addClass('wl-modal-fx4');
			}
		}
		else if($(wlArr[wlArr.length - 1]).index() === ($('.wl-pro-wrap').children().length - 1) && (wW - wlCX) < 280){
			$(this).children('.wl-modal').css({'left': -430});
			$(this).find('.wl-modal-fx').addClass('wl-modal-fx2');
		}
		else{
			if(wlCY < 280){
				$(this).children('.wl-modal').css({'bottom': -266});
				$(this).find('.wl-modal-fx').addClass('wl-modal-fx1');
			}
			else if((wW - wlCX) < 280){
				$(this).children('.wl-modal').css({'left': -430});
				$(this).find('.wl-modal-fx').addClass('wl-modal-fx2');
			}
			else if((wH - wlCY) < 280){
				$(this).children('.wl-modal').css({'top': -266});
				$(this).find('.wl-modal-fx').addClass('wl-modal-fx3');
			}
			else{
				$(this).children('.wl-modal').css({'bottom': -266});
				$(this).find('.wl-modal-fx').addClass('wl-modal-fx1');
			}
		}
		$('.container-wl').on('mouseenter', '.wl-pro-arti', function(e){
			e.stopPropagation();
		});
	});
	
	//添加、清除模态框
	$('.container-wl').on('mouseleave', '.wl-pro-art', function(e){
		initI = setTimeout(function(){$('.wl-modal').remove();}, 50);
		$(this).find('.wl-modal').hover(function(e){
			clearTimeout(initI);
			e.stopPropagation();
		}, function(){
			initI = setTimeout(function(){$('.wl-modal').remove();}, 50);
		});
	});
});






