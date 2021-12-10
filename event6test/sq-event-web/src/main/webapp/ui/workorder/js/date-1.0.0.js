function callBackFunc(node){
	console.log('callBackFunc Is Invoked........');
}

var myCendae;

(function (undefined) {	
	var MyCendae = function(target,config,callBackFunc){
		var self = this,
			currentDate = new Date(),
			currentYear = util.dateFormate(currentDate,'yyyy'),
			currentMonth = util.dateFormate(currentDate,'M'),
			currentDay = util.dateFormate(currentDate,'d');
			self.globalConstant.currDay = util.dateFormate(currentDate,'yyyy-M-d');
		var optionsRaw = document.querySelector('.my-calendar').getAttribute('data-config'),
	        options = (optionsRaw == null || optionsRaw == "") ? {} : (new Function('return {' + optionsRaw + '};'))();
		self.config = util.extend(self.config,config);
		self.drawTable(currentYear,currentMonth);
		//util.querySelector("."+self.classes.container).innerHTML=html;
		myCendae = self;
	}

	MyCendae.prototype = {
		config : {
			tableWidth : '300px',						//布局宽度
			tableHeight : '300px',						//布局高度
			webFontSize : '14px',						//pc布局上的字体大小
			appFontSize : '40px',						//手机端布局上的字体大小			
			event : 'onclick',							//事件类型名称
			dateFormate : 'yyyy-MM-dd',					//日期格式
			callBackFunc : 'callBackFunc(this);'		//事件回调函数
		},
		globalConstant : {
			curYear : null,
			curMonth : null,
			preMonthDays : 0,
			curMonthDays : 0,
			nextMonthDays : 0,
			currDay : ''
		},
		classes : {
			container :'my-calendar',
			monthDayHeader : 'monthDayHeader',
			outMonthDay : 'outMonthDay',
			inMonthDay : 'inMonthDay',
			today : 'todayDay'
		},
		WeekOrder : {
			"一" : 1,
			"二" : 2,
			"三" : 3,
			"四" : 4,
			"五" : 5,
			"六" : 6,
			"日" : 7
		},
		getDaysInOneMonth : function(year, month){  
		  month = parseInt(month, 10);  
		  var d= new Date(year, month, 0);  
		  return d.getDate();  
		},
		monthWeekDayOrder : function(year,month,day){
			var week = "日一二三四五六".split("")[new Date(Date.UTC(year, month-1, day)).getDay()];
			return this.WeekOrder[week];
		},
		getHeader : function(){
			for(var i = 0 ;i<['一','二','三','四','五','六','日'].length;i++){
				
			}
		},
		drawTable : function drawTable(year,month){
				var html = '';
				html += '<table id="myCalendar" class="Cal" cellspacing="0" cellpadding="0" style="width:'+this.config.tableWidth+';height:'+this.config.tableHeight+'">';
				//console.log(browser.versions.mobile);
				if(!browser.versions.mobile){
					html += '<tbody id="tbodyId" style="font-size:'+this.config.webFontSize+'">';
				}else{
					html += '<tbody id="tbodyId" style="font-size:'+this.config.appFontSize+'">';
				}
					html += '<tr>';
						html += '<td colspan="7"><table class="CalTitle" cellspacing="0" style="width:100%;">';
								html += '<tbody>';
									html += '<tr>';
										html += '<td class="CalNextPrev" align="center"><a id="A_CalNextPrev1"  data-y="'+year+'" data-m="'+month+'" href="javascript:void(0);">&lt;</a></td>';
										html += '<td align="center">'+year+'年'+month+'月</td>';
										html += '<td class="CalNextPrev" align="center"><a id="A_CalNextPrev2"  data-y="'+year+'" data-m="'+month+'" href="javascript:void(0);">&gt;</a></td>';
									html += '</tr>';
								html += '</tbody>';
							html += '</table>';
						html += '</td>';
					html += '</tr>';
					html += '<tr class="tr_head">';
						html += '<th class="monthDayHeader" align="center" abbr="一" scope="col">一</th>';
						html += '<th class="monthDayHeader" align="center" abbr="二" scope="col">二</th>';
						html += '<th class="monthDayHeader" align="center" abbr="三" scope="col">三</th>';
						html += '<th class="monthDayHeader" align="center" abbr="四" scope="col">四</th>';
						html += '<th class="monthDayHeader" align="center" abbr="五" scope="col">五</th>';
						html += '<th class="monthDayHeader" align="center" abbr="六" scope="col">六</th>';
						html += '<th class="monthDayHeader" align="center" abbr="日" scope="col">日</th>';
					html += '</tr>';
				
			//var $span = util.parseDom(html,'span');
			var monthData,tr = '<tr class="tr_0">',
				maxDay = this.getDaysInOneMonth(year,month),
			    weekBeginOrder = this.monthWeekDayOrder(year,month,1),
				weekEndOrder = this.monthWeekDayOrder(year,month,maxDay);
				this.globalConstant.curMonthDays = maxDay;
			var days = [];
			if(weekBeginOrder < 5){//小于5时，数据从第二行开始
				monthData = this.getPreMonthDays(year,month-1,2,weekBeginOrder);
			}else{//否则从第一行开始，可以完全显示一个月份的数据
				monthData = this.getPreMonthDays(year,month,1,weekBeginOrder);
			}
			var _y_ = (month==1)?(year-1):year,
				_m_ = (_y_-year<0)?12:(month-1);
			for(var k1 = monthData.beginDay;k1<=monthData.endDay;k1++){
				days.push({date : _y_+'-'+_m_+'-'+k1,day : k1});
			}

			for(var _k=1;_k<=maxDay;_k++){
				days.push({date:year+'-'+month+'-'+_k,day:_k});
			}

			var _y = (month==12)?(year-0+1):year,
				_m = (_y-year>0)?1:(month-0+1);
			for(var _k2 = 1;_k2 <= 41-(maxDay+monthData.endDay-monthData.beginDay);_k2++){
				days.push({date:_y+'-'+_m+'-'+_k2,day:_k2});
			}
			var tr = '<tr class="tr">',_m,_m2 = 0;
			for(_m = 0,len = days.length; _m < len;_m++){
				if((_m2+1)%8==0){
					tr += '</tr>';
					_m2 = 0;
					tr += '<tr class="tr">';
				}
				
				var arr = days[_m].date.split('-');
				var y_ = arr[0];
				var m_ = arr[1];
					m_ = (m_.length>1) ? m_ : ('0'+m_);
				var d_ = arr[2];
					d_ = (d_.length>1) ? d_ : ('0'+d_);
				var showDate = y_ + '-' + m_ + '-' + d_;
				
				if(this.isCurrentMonth(days[_m].date)){
					if(days[_m].date == this.globalConstant.currDay){
						tr += '<td id="week_'+_m2+'" '+this.config.event+'="'+this.config.callBackFunc+'" class="'+this.classes.today+'" data-date="'+showDate+'" align="center">'+days[_m].day+'</td>';
					}else{
						tr += '<td id="week_'+_m2+'" '+this.config.event+'="'+this.config.callBackFunc+'" class="'+this.classes.inMonthDay+'" data-date="'+showDate+'" align="center">'+days[_m].day+'</td>';
					}
				}else{
					tr += '<td id="week_'+_m2+'" '+this.config.event+'="'+this.config.callBackFunc+'" class="'+this.classes.outMonthDay+'" data-date="'+showDate+'" align="center">'+days[_m].day+'</td>';
				}
				_m2++;
			}

			html += tr;
			html += '</tbody>';
			html += '</table>';
			//var $span = util.parseDom(html,'span');
			//util.querySelector("."+this.classes.container).appendChild($span);
			util.querySelector("."+this.classes.container).innerHTML = html;
			util.addEvent(util.getElementById('A_CalNextPrev1'),'click',function(){	
				var y = this.getAttribute('data-y'),
					m = this.getAttribute('data-m'),
					y2 = (m==1)?(y-1):y,
					m2 = (y2-y<0)?12:(m-1);
				myCendae.drawTable(y2,m2);
			});
			util.addEvent(util.getElementById('A_CalNextPrev2'),'click',function(){
				var y = this.getAttribute('data-y'),
					m = this.getAttribute('data-m'),
					y2 = (m==12)?(y-0+1):y,
					m2 = (y2-y>0)?1:(m-0+1);
				myCendae.drawTable(y2,m2);
			});
		},
		getPreMonthDays : function(year,month,trLineNum,weekOrder){
			var _year = (month==1)?(year-1):year,
				_month = ((_year-year)<0)?12:month,
			    maxDay = this.getDaysInOneMonth(_year,_month),
				beginDay = maxDay - ((trLineNum-1)*7+(weekOrder-1)) + 1;
				this.globalConstant.preMonthDays = (maxDay-beginDay+1);
			return {yearMonth:_year+'-'+_month,beginDay:beginDay,endDay:maxDay};
		},
		getNextMonthDays : function(year,month){
			var year = (month==12)?(year-0+1):year,
				month = ((year-year)>0)?1:month,
			    days = 42 - (this.globalConstant.curMonthDays+this.globalConstant.preMonthDays);
			return {yearMonth:year+'-'+month,beginDay:1,endDay:days};
		},
		isCurrentMonth : function(formateDate){
			if( this.globalConstant.currDay.substring(0,this.globalConstant.currDay.lastIndexOf('-'))
														== formateDate.substring(0,formateDate.lastIndexOf('-')) )
				return true;
			return false;
		}
	}

	window.MyCendae = MyCendae;

	var util = MyCendae.util = {
		/*日期格式化
		 * time: 时间毫秒数
		 * 调用方式: new Date(time).format('yyyy-MM-dd hh:mm:ss')
		*/
		dateFormate : function(date,formate) {
			var o = {
				"M+" : date.getMonth() + 1, //month
				"d+" : date.getDate(), //day
				"h+" : date.getHours(), //hour
				"m+" : date.getMinutes(), //minute
				"s+" : date.getSeconds(), //second
				"q+" : Math.floor((date.getMonth() + 3) / 3), //quarter
				"S" : date.getMilliseconds()    //millisecond
			};
			if (/(y+)/.test(formate))
				formate = formate.replace(RegExp.$1, (date.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			for ( var k in o)
				if (new RegExp("(" + k + ")").test(formate))
					formate = formate.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]: ("00" + o[k]).substr(("" + o[k]).length));
			return formate;
		},
		copy : function(source,target) { 
			for (var key in source) {
				source[key] = (typeof target[key]==='undefined')?source[key]:((typeof target[key]==='object')? deepCopy(target[key]): target[key]);
			 } 
		    return source; 
		},
		extend : function(a, b) {
			for (var i in b) {
				if (b.hasOwnProperty(i)) {
					a[i] = b[i];
				}
			}
			return a;
		},
		isIE8: function() {
			return !!( (/msie 8./i).test(navigator.appVersion) && !(/opera/i).test(navigator.userAgent) && window.ActiveXObject && XDomainRequest && !window.msPerformance );
		},

		getElementById : function (elem) {
			return (typeof elem == 'string') ? document.getElementById(elem) : elem;
		},
		querySelector: function (selector) {
			return document.querySelector(selector);
		},
		parseDom : function(arg,tagName) {
			var e = document.createElement(tagName);
			e.innerHTML = arg;
			return e;
		},
		parseDomNodes : function(arg,tagName) {
			var e = document.createElement(tagName);
			e.innerHTML = arg;
			return e.childNodes;
		},
		make : function(tagName, attributes, attach) {
			var k, e = document.createElement(tagName);
			if (!!attributes) for (k in attributes) if (attributes.hasOwnProperty(k)) e.setAttribute(k, attributes[k]);
			if (!!attach) attach.appendChild(e);
			return e;
		},
		addEvent : function(elem, eventName, callback) {
			var listener = function (event) {
				event = event || window.event;
				var target = event.target || event.srcElement;
				var block = callback.apply(elem, [event, target]);
				if (block === false) {
					if (!!event.preventDefault) event.preventDefault();
					else {
						event.returnValue = false;
						event.cancelBubble = true;
					}
				}
				return block;
			};
			if (elem.attachEvent) { // IE only.  The "on" is mandatory.
				elem.attachEvent("on" + eventName, listener);
			} else { // Other browsers.
				elem.addEventListener(eventName, listener, false);
			}
			return listener;
		}
	}

	var browser = MyCendae.browser = {
	    versions:function(){
	        var u = navigator.userAgent, app = navigator.appVersion;
	        return {
	            trident: u.indexOf('Trident') > -1, //IE内核
	            presto: u.indexOf('Presto') > -1, //opera内核
	            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
	            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
	            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
	            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
	            android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
	            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
	            iPad: u.indexOf('iPad') > -1, //是否iPad
	            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
	            weixin: u.indexOf('MicroMessenger') > -1, //是否微信 （2015-01-22新增）
	            qq: u.match(/\sQQ/i) == " qq" //是否QQ
	        };
	    }(),
	    language:(navigator.browserLanguage || navigator.language).toLowerCase()
	}
})();