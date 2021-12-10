var dcPersonCount = 0;
var lastPosition={};
var sbData = false;
var isDebugger = false;
function isDebug(){
	if(isDebugger)
		debugger;
}

function GetQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
//随机抽查
function assignRet(eventData){
	$('.sj-dx').find('input').attr('checked', true);
	var html = getEventHtml(eventData);
	$("#rsj").html(html).show();
	$("#rsj").siblings("ul").hide();
	$('.cont_bg').hide();  //隐藏遮罩图片
	$('.mask_box').show();  //遮挡滚动窗口，避免鼠标移入影响
	$('.draw-sj-cont').addClass('active');
	
	$('.draw-man-cont .cont_bg').show();
	$('.draw-sj-cont .cont_bg').hide();
	$('.mask_box').hide();
}
function getEventHtml(eventData){
	var html = '';
	html+='<li class="draw-sj-li">';
	html+='    <input type="hidden" name="gridCode" value="'+eventData.gridCode+'">';
	html+='    <input type="hidden" name="eventId" value="'+eventData.eventId+'">';
	html+='    <div class="event-con">';
	html+='    		<div class="event-cont">';
	html+='    			<h5 class="event-title">【'+eventData.eventClass+'】</h5><span><a id="event-title-a" onclick=showDcDetailRow('+ eventData.eventId+ ','+eventData.instanceId+','+eventData.workFlowId+') title="'+eventData.eventName+'" style="cursor:pointer;">'+eventData.eventName+'</a></span>';
	html+='    		</div>';
//    html+='        <h5 class="event-title">【'+eventData.eventClass+'】<span><a id="event-title-a" onclick=showDcDetailRow('+ eventData.eventId+ ','+eventData.instanceId+','+eventData.workFlowId+') title="'+eventData.eventName+'" style="cursor:pointer;">'+eventData.eventName+'</a></span></h5>';
	html+='        <p class="event-text">于';
	html+='            <span class="e-sy">'+eventData.happenTimeStr+'</span>';
	html+='        </p>';
	html+='       <p class="event-text">在';
	html+='            <span class="e-sy z-addr">'+eventData.occurred+'</span>';
	html+='            <i class="eve-pon"></i>发生';
	if(eventData.content){
	    if(eventData.content.length > 55){
	        html+='           <span class="cc-red ml10" title="'+eventData.content+'">'+eventData.content.substring(0,55)+'...</span>';
        }else{
			html+='           <span class="cc-red ml10" title="'+eventData.content+'">'+eventData.content+'</span>';
	    }
	}else{
	   html+='           <span class="cc-red ml10" title="'+eventData.content+'">暂无信息</span>';
	}
	html+='        </p>';
	html+='        <div class="eve-man">';
	html+='            <p>所属网格：<span>'+eventData.gridPath+'</span></p>';
	html+='            <p>联系人员：<span>'+eventData.contactUser+'（'+eventData.tel+'）</span></p>';
	html+='        </div>';
	html+='    </div>';
	html+='</li>';
	return html;
}

function showDcDetailRow(eventId,instanceId,workFlowId){
	if(!eventId){
	    $.messager.alert('提示','请选择一条记录！','info');
	}else{
    	var url = base+'/zhsq/event/eventDisposalController/detailEvent.jhtml?eventType=all&model=1&instanceId='+instanceId+'&workFlowId='+workFlowId+'&eventId='+eventId;
    	showMaxJqueryWindow("事件详情", url, fetchWinWidth(), fetchWinHeight(), true);
	}
}

function getPersonHtml(person){
	var pm = (!!(person.mobileTelephone))?person.mobileTelephone:person.fixedTelephone;
	var html = '';
	html += '<li class="draw-man-li">';
	html += '	<input type="hidden" name="userId" value="'+person.userId+'"/>';
	html += '	<input type="hidden" name="gridId" value="'+person.gridId+'"/>';
	html += '	<input type="hidden" name="pm" value="'+(pm==null?'':pm)+'"/>';
	html += '    <div class="man-pep">';
	if(person.photo){
		html += '        <img class="man-img" src="'+imgDownPath+person.photo+'">';
	}else{
		html += '        <img class="man-img">';
	}
	html += '        <div class="man-txt">';
	var pn = (!!(person.partyName2))?person.partyName2:person.partyName;
	html += '            <p>姓名：<span>'+(pn==null?'未命名':pn)+'</span></p>';
	html += '            <p>职位：<span>督查员</span></p>';
	html += '           <p>电话：<span>'+(pm==null?'':pm)+'</span></p>';
	html += '           <p>地址：<span title="'+person.gridPath+'">'+person.gridName+'</span></p>';
	html += '      </div>';
	html += '   </div>';
	html += '</li>';
	return html;
}

function getPersonBlankHtml(){
/*
	var html = '';
	html += '<li class="draw-man-li">';
	html += '	<input type="hidden" name="userId" />';
	html += '	<input type="hidden" name="gridId" />';
	html += '	<input type="hidden" name="pm" />';
	html += '    <div class="man-pep">';
	html += '        <img class="man-img">';
	html += '        <div class="man-txt">';
	html += '            <p>姓名：<span></span></p>';
	html += '            <p>职位：<span></span></p>';
	html += '           <p>电话：<span></span></p>';
	html += '           <p>地址：<span title=""></span></p>';
	html += '      </div>';
	html += '   </div>';
	html += '</li>';
	return html;
*/
	$('.draw-man-cont .cont_bg').show();
	$('.draw-sj-cont .cont_bg').hide();
	$('.mask_box').hide();
}

function getRandomEventAndPerson(){

	try{
		var url = base+'/zhsq/event/doubleRandomTask/generateEventData.jhtml?t='+new Date().getTime();
		var o = {};
		var $checked = $('.sj-dx').find('input').is(":checked");
		if($checked){
			var v = $("#rsj").find('input[name="gridCode"]').first().val();
			if(!v){
				layer.msg("您正在选择指定的事件,但未获取到该事件的所属网格,请切换事件");
				return false;
			}else{
				o.gridCode = v;
			}
		}
		var delay = GetQueryString('delay');
		if(delay != null){
			o.delay = delay;
		}
		o.type=$('input:radio[name="layoutType"]:checked').val();
		$.ajaxSettings.async = false;//同步执行请求
		var flag = false;
		$.getJSON(url,o,function(res){
			if(res.success){
				flag = true;
				if(!$checked){
					var eventHtml = getEventHtml(res.event);
					$("#rsj").html(eventHtml);
				}
				dcPersonCount = res.personList.length;
				for(var i=0;i<dcPersonCount;i++){
					var personHtml = getPersonHtml(res.personList[i]);
					$("#rry"+i).html(personHtml);
				}
			}else{
				if(!$checked){
					var eventHtml = getEventHtml(res.event || {});
					$("#rsj").html(eventHtml);
				}
				//var personHtml = getPersonBlankHtml();
				//$("#rry"+0).html(personHtml);
				//$("#rry"+1).html(personHtml);
				
				$('.draw-sj-cont').removeClass('active');
				$('.draw-man-cont .cont_bg').show();
				$('.draw-sj-cont .cont_bg').hide();
				$('.mask_box').hide();
				
				layer.msg(res.msg);
			}
		});
		return flag;
	} catch(e){

	} finally{
	}
	
}

function loading(){
	$.blockUI({
			   message: "加载中..." , 
			   css: {
				   		zIndex:99999999999999,
				   		width:'150px',
				   		height:'50px',
				   		lineHeight:'50px',
				   		top:'45%',
				   		left:'45%',
    					background:'url('+base+'/css/loading.gif) no-repeat',
    					textIndent:'20px'
    		   },
    		   overlayCSS:{
    			   		zIndex:99999999999998,
    			   		backgroundColor:'#fff'
    		   }
    });
}

function loadOver(){
	$.unblockUI();
}

function dataLoading(){
	layer.msg("抽取中...");
}

$(function($){
	var $rsj = $("#rsj"),$rry0 = $("#rry0"),$rry1 = $("#rry1");
	var $drawCon = $('.draw-sj-con'), $arrLi = $('.draw-sj-con ul li'), $begin = $('.btn-open'), $flag = true, $ckecked;
	var $manCon = $('.draw-man-con'), $arrItems = $manCon.eq(0).find('ul li'),$submit=$('.btn-cs.btn-green');
	
//	点击巡查事件添加阴影,只抽查督查人员
	$('.sj-dx').on('click', function(){
		//debugger;
		var $checked = $('.sj-dx').find('input').is(":checked");
		if($checked){
			//$('.draw-sj-cont').addClass('active');
			var delay = GetQueryString('delay');
			if(!delay || delay ==null)
				delay='';
			var url = base+'/zhsq/event/doubleRandomTask/listEvent.jhtml?dc=assign&delay='+delay+'&t='+new Date().getTime();
			showMaxJqueryWindow("选择指定督查事件", url,$(window).width(),$(window).height());
			$('.sj-dx').find('input').attr('checked', false);
		}else{
			$('.draw-sj-cont').removeClass('active');
			$('.draw-man-cont .cont_bg').show();
			$('.draw-sj-cont .cont_bg').hide();
			$('.mask_box').hide();
		}
	});
	$('.sj-dx label').on('click', function(e){
		e.stopPropagation();
	});
	/*$('.draw-sj-cont').on('click', function(){  //点击左边区域添加active，限定不做抽取
		if($(this).hasClass('active')){
			$(this).removeClass('active');
			$('.sj-dx').find('input').attr('checked', false);
		}else{
			$(this).addClass('active');
			$('.sj-dx').find('input').attr('checked', true);
		}
	});*/
	for(var i=0; i<2; i++){ //将巡查事件内容克隆成4份
		$drawCon.append($drawCon.find('ul.clone-b').clone());
	}
	$manCon.each(function(){ //将督查人员内容克隆成4份
		for(var i=0; i<2;i++){
			$(this).append($(this).find('ul.clone-b').clone());
		}
	});
	$begin.on('click', function(){ //开始随机抽取
	
		//$('.cont_bg').remove();  //隐藏遮罩图片
		//$('.mask_box').show();  //遮挡滚动窗口，避免鼠标移入影响
		var hc = $begin.html();
		if(hc == '抽取中'){
			layer.msg("正在抽取中,请勿频繁点击");
			return;
		}
		$begin.html('抽取中');
		if($(this).hasClass('img_')){
			$(this).removeClass('img_');
			$begin.css({'background': 'url('+base+'/js/event/doubleRandomTask/img/ui_btn_an.png) no-repeat'});
		}else{
			$(this).addClass('img_');
			$begin.css({'background': 'url('+base+'/js/event/doubleRandomTask/img/ui_btn_xin.png) no-repeat'});
		}
		
		//$manCon.css('top', '0px');
		
		var url = base+'/zhsq/event/doubleRandomTask/generateEventData.jhtml?t='+new Date().getTime();
		var o = {};
		var $checked = $('.sj-dx').find('input').is(":checked");
		if($checked){
			var v = $("#rsj").find('input[name="gridCode"]').first().val();
			if(!v){
				layer.msg("您正在选择指定的事件,但未获取到该事件的所属网格,请切换事件");
				return false;
			}else{
				o.gridCode = v;
			}
		}
		var delay = GetQueryString('delay');
		if(delay != null){
			o.delay = delay;
		}
		o.isChecked = $checked;
		o.type=$('input:radio[name="layoutType"]:checked').val();
		$.ajaxSettings.async = false;//同步执行请求
        try{
			$.getJSON(url,o,function(res){
				//debugger;
				//console.log(res);
				if(res.success){
					flag = true;
					if(!o.isChecked){
						var eventHtml = getEventHtml(res.event);
						$("#rsj").html(eventHtml);
					}
					dcPersonCount = res.personList.length;
					for(var i=0;i<dcPersonCount;i++){
						var personHtml = getPersonHtml(res.personList[i]);
						$("#rry"+i).html(personHtml);
					}

					$rry1.closest(".draw-man-cont").show();//显示第二个督查人员块
					sbData = false;
					var $checked = $('.sj-dx').find('input').is(":checked");
					if(lastPosition.sjTop){//隐藏真实值,显示虚拟值  && !$checked
						//$drawCon.css("top",lastPosition.sjTop);
						$manCon.css("top",lastPosition.sjTop);
						$manCon.eq(0).css("top",lastPosition.ry0);
						$manCon.eq(1).css("top",lastPosition.ry1);
						if(!$checked){
							$rsj.hide();
							$rsj.siblings("ul").show();
						}
						$rry0.hide();
						$rry0.siblings("ul").show();
						$rry1.hide();
						$rry1.siblings("ul").show();
					}
					if($checked){
						$rsj.show();
						$rsj.siblings("ul").hide();
					}
					$('.cont_bg').hide();  //隐藏遮罩图片
					$('.mask_box').show();  //遮挡滚动窗口，避免鼠标移入影响
					
					$begin.html('抽取中');
					$manCon.css('top', '0px');
					if(!$('.draw-sj-cont').hasClass('active')){  //选择巡查事件后抽取不滚动
						$drawCon.css('top', '0px');
						getRandom(0, $arrLi.length - 1);
					}else{
						$(".draw-sj-con .cont_bg").hide();
						$rsj.show();
						$rsj.siblings("ul").hide();
					}
					createRandomNumber_1(2, $arrItems.length); //抽取督查人员

				}else{
					if(!$checked){
						if(res.event){
							var eventHtml = getEventHtml(res.event || {});
							$("#rsj").html(eventHtml);
						}
					}
					
					if(res.event){
						$('.draw-sj-cont').removeClass('active');
						$('.draw-man-cont .cont_bg').show();
						$('.draw-sj-cont .cont_bg').hide();
						$('.mask_box').hide();
					}
					
					layer.msg(res.msg);
					$begin.html('重新抽取');
				}
			});
		}catch(e){
		}finally{
			$('.draw-man-con').css('top','0px');
			$('.mask_box').hide();
		}
	});
	function getRandom(min,max){ //获取巡查事件随机数
	    //x上限，y下限
	    var x = max;  //最大值
	    var y = min;  //最小值
	    if(x<y){
	        x=min;
	        y=max;
	    }
	    var rand = parseInt(Math.random() * (x - y + 1) + y);
	    autoPlay(rand);
	}
	
	/* dingyw 2017/12/09
	function autoPlay(index){
		$drawCon.animate({'top': -($arrLi.length*2 + index) * 210}, 900, function(){
			$begin.html('重新抽取'); $('.mask_box').hide();
		});
		$drawCon.find('ul:nth-child(3) li').eq(index).addClass('on').siblings().removeClass('on');
	}
	*/
	
	//add by dingyw 2017/12/09
	function autoPlay(index){
		$drawCon.animate({'top': -($arrLi.length*2 + index) * 210}, 1000, function(){ 
			$begin.html('抽取完毕'); 
			$('.mask_box').hide();
			$begin.css({'background': 'url('+base+'/js/event/doubleRandomTask/img/ui_btn_xin.png) no-repeat'});
		});
		$drawCon.find('ul:nth-child(3) li').eq(index).addClass('on').siblings().removeClass('on');
	}
	
	
	function createRandomNumber_1(num,maxNum){ //随机获取区间内的两个不重复的随机数
	
	    var flag = 0,
	    i=0,
	    arrLen=0,
	    ran=0,
	    arr=[],
	    res=[]; //选出的随机数存放在res内
	    
	    if(maxNum - num < 0){
		    flag = maxNum;
		    maxNum = num;
		    num = flag;
	    }
	    
	    for(;i<maxNum;i++){
	    arr[i] = i-0+1;
	    }
	    
	    arr.length = maxNum;
	    flag = 0;
	    while(num > flag){
		    arrLen = arr.length;
		    ran = Math.floor(arrLen*Math.random());
		    res.push(arr.splice(ran,1)[0]-1);
		    flag++;
	    }
	    $manCon.eq(0).animate({'top': -($arrItems.length*2 + res[0]) * 100}, 1000, function(){$flag = true; finishCallback();});
		$manCon.eq(0).find('ul:nth-child(3) li').eq(res[0]).addClass('on').siblings().removeClass('on');
	    //$manCon.eq(1).animate({'top': -($arrItems.length*2 + res[1]) * 100}, 900);
	    $manCon.eq(1).animate({'top': -($arrItems.length*2 + res[1]) * 100}, 900, 
	    	function(){
	    		$flag = true; 
	    		$drawCon.css("top",0);
				$manCon.eq(0).css("top",0);
				$manCon.eq(1).css("top",0);
	    });
		$manCon.eq(1).find('ul:nth-child(3) li').eq(res[1]).addClass('on').siblings().removeClass('on');
	}
	
	function finishCallback(){
		$begin.html('重新抽取');
		$rsj.show();
		$rsj.siblings("ul").hide();
		//debugger;
		if(dcPersonCount==1){
			$rry1.closest(".draw-man-cont").hide();//隐藏督查人员,因为只有一个督查人员
			$rry0.show();
			$rry0.siblings("ul").hide();
			$rry1.closest(".draw-man-cont").find('.cont_bg').hide();
			$rry1.closest(".draw-man-cont").find('.mask_box').show();
			$rry1.html('');
		}else if(dcPersonCount==2){
			$rry0.show();
			$rry0.siblings("ul").hide();
			$rry1.show();
			$rry1.siblings("ul").hide();
		}
		lastPosition.sjTop = $drawCon.css("top");
		lastPosition.ry0 = $manCon.eq(0).css("top");
		lastPosition.ry1 = $manCon.eq(1).css("top");
		$drawCon.css("top",0);
		$manCon.eq(0).css("top",0);
		$manCon.eq(1).css("top",0);
	}
	
	$(".btn-cs.btn-green").click(function(){
		if(sbData){
			layer.msg("该事件已经保存成功,请重新抽取数据");
			return;
		}
		var submitData = {};
		var eventId = $rsj.find("input[name='eventId']").val();
		if(!eventId || eventId==''){
			layer.msg("请先获取需要督查的事件数据");
			return;
		}else{
			submitData.eventId = eventId;
		}
		submitData.judgePersons=[];
		var v1 = $rry0.find("input[name='userId']").val();
		var v1Grid = $rry0.find("input[name='gridId']").val();//网格id
		var v1pm = $rry0.find("input[name='pm']").val();
		var v2 = $rry1.find("input[name='userId']").val();
		var v2Grid = $rry1.find("input[name='gridId']").val();//网格id
		var v2pm = $rry1.find("input[name='pm']").val();
		if(!v1 || v1==''){
			layer.msg("督查人员不能为空");
			return;
		}else if(!v2 || v2==''){
			submitData.judgePersons.push(v1+'_'+v1Grid);
			if(v1pm && v1pm!=''){
				submitData.smobile = v1pm;
				submitData.suserid = v1;
			}
			var sIndex = layer.confirm('当前只有一个督查人员,确定提交数据？', {
				  btn: ['确定','取消'] //按钮
				}, function(){
					submitForm(submitData,sIndex);
				}, function(){
				  
				});
		}else{
			submitData.judgePersons.push(v1+'_'+v1Grid);
			submitData.judgePersons.push(v2+'_'+v2Grid);
			if(v1pm && v1pm!='' && v2pm && v2pm!=''){
				submitData.smobile = v1pm+','+v2pm;
				submitData.suserid = v1+','+v2;
			}else if(v1pm && v1pm!=''){
				submitData.smobile = v1pm;
				submitData.suserid = v1;
			}else if(v1pm && v1pm!=''){
				submitData.smobile = v2pm;
				submitData.suserid = v2;
			}
			var sIndex = layer.confirm('确定提交数据？', {
				  btn: ['确定','取消'] //按钮
				}, function(){
					submitForm(submitData,sIndex);
				}, function(){
				  
				});
		}
	});
	function submitForm(data,sIndex){
		layer.close(sIndex);
		var index = layer.load(1, {
		  shade: [0.1,'#fff'] //0.1透明度的白色背景
		});
		data.taskType=$taskType;
		$.ajaxSettings.async = true;
		var url = base+'/zhsq/event/doubleRandomTask/saveDoubleRandomEvent.jhtml?t='+new Date().getTime();
		$.getJSON(url,data,function(res){
			layer.close(index);
			if(res.success){
				sbData = true;
				$('.sj-dx').find('input').attr('checked', false);
				layer.msg("提交成功");
				setTimeout(function(){
					url = base+'/zhsq/event/doubleRandomTask/sendSms.jhtml?t='+new Date().getTime();
					var $checked = $('#duanxin').is(":checked");
					if($checked && data.smobile){
						$.getJSON(url,{'otherMobileNums':data.smobile,'userIds':data.suserid},function(res){
							if(res){
								layer.msg("短信发送成功");
							}else{
								layer.msg("短信发送失败");
							}
						});//发送短信
					}
				},500);
			}else{
				var msg = res.msg;
				if(!msg)
					msg = '保存失败';
				layer.msg(msg);
			}
		});
	}
	
	$('.btn-green').on('click', function(){
		$(this).css({'background': 'url('+base+'/js/event/doubleRandomTask/img/ui_tj_an.png) no-repeat'});
		setTimeout(function(){
			$('.btn-green').css({'background': 'url('+base+'/js/event/doubleRandomTask/img/ui_tj_xin.png) no-repeat'});
		}, 300)
	})
}(jQuery));