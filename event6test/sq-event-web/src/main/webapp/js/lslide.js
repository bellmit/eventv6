/*
function:   LevelSlide-v1.0
author:     jin
depends:    jquery.js(1.2.6~1.4)
times:  	2009-7-26;2010-05-24;2010-10-18
*/
(function($) {
	$.fn.lSlide=function(opt){
		//settings
		var settings=jQuery.extend(true,{
			productBigAreaWitch:"ul",//相对this选择器，产生滚动条的大div
			list:"ul > li",//相对this选择器，list对象
			seeColumn:1,//可视范围分组，即列数
			row:1,//可视范围分行数目（不支持多行配置）
			step:1,//滚动步长，步长越值越小滚动越平滑速度越慢，步长越大滚动越不平滑速度越快
			speed:25,//滚动间隔时间，间隔时间越短速度越快滚动越平滑，本设置效果非常微小系统已经自动设置最佳值尽量不做修改
			orientation:"left",//自动轮播开启时会以设定方向滚动，只有左右，left,right
			isBtn:{
				left:"#left",//向左按钮
				right:"#right",//向右按钮
				isChangeState:true,//按钮事件是否触发滚动方向状态
				step:10,//滚动步长，步长越值越小滚动越平滑速度越慢，步长越大滚动越不平滑速度越快
				speed:20//滚动间隔时间，间隔时间越短速度越快滚动越平滑，本设置效果非常微小系统已经自动设置最佳值尽量不做修改
			},//按钮树配置
			bugD1Width:0//table布局情况下经常有取不到width值情况，针对此可进行的补丁操作,正常情况下无需启用该补丁
		},opt);
		//settings
		var productBigAreaWitch=settings.productBigAreaWitch,
			list=settings.list,
			seeColumn=settings.seeColumn,
			row=settings.row,
			step=settings.step,
			speed=settings.speed,
			orientation=settings.orientation,
			btn=settings.isBtn,
			btnLeft=$(btn.left),
			btnRight=$(btn.right),
			btnIsChangeState=btn.isChangeState,
			btnStep=btn.step,
			btnSpeed=btn.speed,
			bugD1Width=settings.bugD1Width;
		//div
		var $this=$(this);
		var d1=$this,
			d2=d1.find(productBigAreaWitch),
			d3=d1.find(list);
		//d1Width
		var d1Width=d1.width();
		if(bugD1Width!=0){
			d1Width=bugD1Width;
		}else{
			if(d1Width==0){
				d1Width=parseInt(d1.css("width"));
				if(d1Width==0){//还有其他值情况应用中发现问题即时补充
					alert("Err:d1Width==0");
				}
			}
		}
		//other width size ...
		var d3OldSize=d3.size();
		var d3Size=d3OldSize*3;
		var splitWidth=d1Width/seeColumn;
		var d2OldWidth=splitWidth*Math.ceil(d3OldSize/row);
		var d2Width=d2OldWidth*3;
		//fall short of nmuber,return false
		if(d2OldWidth<d1Width)return false;
		//clone for fill
		for(var i=0;i<d3OldSize;i++){
			d3.eq(i).clone().appendTo(d2);
			d3.eq(d3OldSize-i-1).clone().prependTo(d2);
		}
		//bear with
		d2.width(d2Width+100);
		//default left
		var __left=d2OldWidth;
		d2.css("left",-__left);
		//
		var flag=true;
		//left
		var left=function(e){
			if(!flag)return false;
			flag=false;
			if(btnIsChangeState){
				orientation="left";
			}
			__left+=step;
			d2.css("left",-__left);
			if(__left>=d2OldWidth+splitWidth){
				for(i=1;i<=row;i++){
					d2.children().eq(0).appendTo(d2);
				}
				__left=d2OldWidth;
				d2.css("left",-__left);
			}
			flag=true;
			return false;
		};
		//right
		var right=function(e){
			if(!flag)return false;
			flag=false;
			if(btnIsChangeState){
				orientation="right";
			}
			__left-=step;
			d2.css("left",-__left);
			if(__left<=d2OldWidth-splitWidth){
				for(i=1;i<=row;i++){
					d2.children().eq(d3Size-1).prependTo(d2);
				}
				__left=d2OldWidth;
				d2.css("left",-__left);
			}
			flag=true;
			return false;
		};
		//timer
		var timerID;
		var autoPlay=function(){
			switch(orientation)
			{
				case "left":timerID=window.setInterval(left,speed);break;
				case "right":timerID=window.setInterval(right,speed);break;
			}
			return false;
		};
		var autoStop = function(){
			window.clearInterval(timerID);
			return false;
		};
		//ready autoPlay
		autoPlay();
		//
		$this.hover(autoStop,autoPlay);
		//btn
		if(btn){
			btnLeft.hover(autoStop,autoPlay);
			btnRight.hover(autoStop,autoPlay);
			//add speed btn
			var _timeId;
			btnLeft.mousedown(function(){
				step=btnStep;
				_timeId=setInterval(left,btnSpeed);
				$(document).mousemove(function(){
					clearInterval(_timeId);
					step=settings.step;
				});
				return false;
			});
			btnRight.mousedown(function(){
				step=btnStep;
				_timeId=setInterval(right,btnSpeed);
				$(document).mousemove(function(){
					clearInterval(_timeId);
					step=settings.step;
				});
				return false;
			});
			$(document).mouseup(function(){
				clearInterval(_timeId);
				step=settings.step;
				return false;
			});
		}
		return $this;
	};
})(jQuery);