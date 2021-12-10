/**
 * jQuery Anole Date
 *
 * Copyright 2014, YangCQ
 * No licensed under the licenses.
 * http://www.ximifan.com/
 *
 * Includes jquery.js
 * http://jquery.com/
 *
 * Date: 2014-11-13 17:36:03
 */
(function($) {
	var address = "";
	var addressCode = "";
	var addressGridId = "";
	var province, city, county, street, community;
	var parentGridId,level;
	
	var _AnoleAddress = {
		sys : {						// 存放内部参数与函数
			version : "jQuery-Anole-Address-v1.0.0",
			type : "",
			name : "jquery.anole.address.js",
			path : "/js/components/addressPlugin/",
			ContextPath : null,		// 上下文路径(必传)
			DataSrc : "/zzgl/addressPluginController/addressForJsonp.jhtml?jsoncallback=?",
			html : '<div class="nav"><ul>[[item-head]]</ul></div><div class="con">[[item-body]]</div>',
			itemRender : {
				"省份" : [ "_renderForProvince", "_handleEventByProvince" ],
				"城市" : [ "_renderForCity", "_handleEventByCity" ],
				"县区" : [ "_renderForCounty", "_handleEventByCounty" ],
				"街道" : [ "_renderForStreet", "_handleEventByStreet" ],
				"社区" : [ "_renderForCommunity", "_handleEventByCommunity" ],
				"清空" : []
			}
		},
		renderObj : {},
		settings : {
			// ------ 常规参数控制 ------
			ContextPath : null,		// 上下文路径(必传)
			//BackfillType : "0",		// 回填方式(0-行政区划综合名称:行政编码-行政名称,1-行政名称)
			// ------ 显示参数控制 ------
			ShowOptions : {
				ErrorBgColor : "#fff3f3",
				TabItems : [ "省份", "城市", "县区", "街道", "社区", "清空" ],
				TabItemsCode : [ "Provinces", "Cities", "Counties", "Streets", "Communities", "Clear" ],
				Provinces : [  ],
				ProvincesId : [  ],
				ProvincesCode : [  ],
				Cities : [  ],
				CitiesId : [  ],
				CitiesCode : [  ],
				Counties : [  ],
				CountiesId : [  ],
				CountiesCode : [  ],
				Streets : [  ],
				StreetsId : [  ],
				StreetsCode : [  ],
				Communities : [  ],
				CommunitiesId : [  ],
				CommunitiesCode : [  ]
			},
			// ------ 回调事件 ------
			BackEvents : {
				OnSelected : null,
				OnCleared : null
			},
			// ------ 下拉按钮个性属性 ------
			DropButton : {
				BINOpenPath : "",//"img/Addressview.png",
				BINOpenHoverPath : "",//"img/Addressview.png",
				BIYOpenPath : "",//"img/Addressview.png",
				BIYOpenHoverPath :"", //"img/Addressview.png"
			},
			// ------ 下拉DIV个性属性 ------
			DownDiv : {
				Style : {}
			},
			_AddressCode : "",
			startProvinceCode : "",
			startCityCode : "",
			startCountyCode : "",
			startStreetCode : "",
			startCommunityCode : ""
		}
	};
	// 渲染地址控件入口
	_AnoleAddress.sys.renderForAddress = function(index, ctrlType, settings, element) {
		var me = this;
		me.data = {};
		new _AnoleAddress.sys._renderForComm(this, index, ctrlType, settings, element);
		var html_all = _AnoleAddress.sys.html;
		var item_head = new Array();
		var item_body = new Array();
		for (var i = 0; i < settings.ShowOptions.TabItems.length; i++) {
			var _item = settings.ShowOptions.TabItems[i];
			var rObj = _AnoleAddress.sys.itemRender[_item];
			var sHtml = "";
			
			if ((typeof (rObj[0]) != "undefined") && i == 0) {
				//获取省份页签数据
				_AnoleAddress.sys.getData('', 1, settings);
				sHtml = _AnoleAddress.sys[rObj[0]].call(this, this, i, { renderObj : rObj}, settings, element).getHtml();
			}
			
			if (i == 0) {
				item_head.push('<li class="current ' + (_item == "清空" ? "AddressClear" : "") + '">' + _item + '</li>');
				item_body.push('<div id = "'+ _AnoleAddress.settings.ShowOptions.TabItemsCode[i] +'">' + sHtml + '</div>');
			} else {
				item_head.push('<li class="' + (_item == "清空" ? "AddressClear" : "") + '">' + _item + '</li>');
				item_body.push('<div class="hide" id = "'+ _AnoleAddress.settings.ShowOptions.TabItemsCode[i] +'">' + sHtml + '</div>');
			}
		}
		html_all = html_all.replace('\[\[item-head\]\]', item_head.join(''));
		html_all = html_all.replace('\[\[item-body\]\]', item_body.join(''));
		me.divHolder.addClass('AddressControl');
		me.divHolder.html(html_all);
		_AnoleAddress.sys.html = html_all;
		var _bodys = me.divHolder.find('.con').children();
		me.divHolder.find('.nav ul li').each(function(i, e) {
			if ($(this).html() == "清空") {
				$(this).click(function() {
					me.data = {};
					element.val('');
					me.cleanSelectedStyle('AddressSelected', 'all', settings);
					if ($.isFunction(settings.BackEvents.OnCleared)) {
						settings.BackEvents.OnCleared.call(this);
					}
					settings.ShowOptions.Cities = [];
					settings.ShowOptions.CitiesId = [];
					settings.ShowOptions.Counties = [];
					settings.ShowOptions.CountiesId = [];
					settings.ShowOptions.Streets = [];
					settings.ShowOptions.StreetsId = [];
					settings.ShowOptions.Communities = [];
					settings.ShowOptions.CommunitiesId = [];
					document.getElementById("Cities").innerHTML = "";
					document.getElementById("Counties").innerHTML = "";
					document.getElementById("Streets").innerHTML = "";
					document.getElementById("Communities").innerHTML = "";
					me.showOrHideDiv(null, "hide", true);
				});
			} else {
				$(this).click(function() {
					$(this).addClass("current").siblings().removeClass("current");
					_bodys.eq(i).show().siblings().hide();
				});
			}
		});
		
		//只初始化省份页签
		var _item = settings.ShowOptions.TabItems[0];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		if (typeof (rObj[0]) != "undefined") {
			var cObj = me.divHolder.find('.con').children().eq(0);
			_AnoleAddress.sys[rObj[0]].call(this, this, 0, { renderObj : rObj, xaddress : "" }, settings, element).initEvents(cObj);
		}
		
		//跳转到页签
		var _bodys = me.divHolder.find('.con').children();
		me.divHolder.find('.nav ul li').each(function(i, e) {
			if(i == 0 && i != 5){
				$(this).addClass("current").siblings().removeClass("current");
				_bodys.eq(i).show().siblings().hide();
			}
		});
		
		
		/**
		 * 返回地址字符串，包含分隔符‘/’
		 */
		this.getAddressWithMark = function() {
			return address;
		};
		/**
		 * 返回地址字符串不包含分隔符‘/’
		 */
		this.getAddress = function() {
			var addressArry = address.split(' / ');
			var newAddress = '';
			for (var i = 0; i < addressArry.length; i++){
				newAddress += addressArry[i];
			}
			return newAddress;
		};
		/**
		 * 返回地址code
		 */
		this.getAddressCode = function() {
			return addressCode;
		};
	};
	/* =============================================================
	 * 				------ 业务相关渲染方法与处理方法 ------
	 * =============================================================*/
	// 省份
	_AnoleAddress.sys._renderForProvince = function(target, index, params, settings, element) {
		var _clickEvent = params.renderObj[1];
		var _cuAry = new Array();
		var _subHtml = '';
		
		for (var i = 0; i < settings.ShowOptions.Provinces.length; i++) {
			_cuAry.push('<li>' + settings.ShowOptions.Provinces[i] + '</li>');
		}
		
		this.getHtml = function() {
			var _html = '\
			<div class="AddressList">\
                <ul>' + _cuAry.join('') + '</ul>\
            </div>\
            <div class="clear" style="*height:10px;"></div>' + _subHtml;
			return _html;
		};
		this.initEvents = function(cObj) {
			cObj.find('.AddressList li').each(function(i, e) {
				$(this).click(function() {
					_AnoleAddress.sys[_clickEvent].call(this, target, $(this).html(), i, params, settings, element);
				});
			});
		};
		return {
			getHtml : this.getHtml,
			initEvents : this.initEvents
		};
	};
	_AnoleAddress.sys._handleEventByProvince = function(target, type, index, params, settings, element) {
		parentGridId = settings.ShowOptions.ProvincesId[index];
		addressCode =  settings.ShowOptions.ProvincesCode[index];
		addressGridId = parentGridId;
		//获取城市
		_AnoleAddress.sys.getData(parentGridId, 2, settings);
		
		document.getElementById("Cities").innerHTML = "";
		document.getElementById("Counties").innerHTML = "";
		document.getElementById("Streets").innerHTML = "";
		document.getElementById("Communities").innerHTML = "";
		var me = target;
		me.data = {};
		var _item = settings.ShowOptions.TabItems[1];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		var sHtml = "";	
		sHtml = _AnoleAddress.sys[rObj[0]].call(this, target, 1, { renderObj : rObj}, settings, element).getHtml();
		me.divHolder.addClass('AddressControl');
		document.getElementById("Cities").innerHTML = sHtml;
//		//只初始化城市页签
		var _item = settings.ShowOptions.TabItems[1];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		if (typeof (rObj[0]) != "undefined") {
			var cObj = me.divHolder.find('.con').children().eq(1);
			_AnoleAddress.sys[rObj[0]].call(this, target, 1, { renderObj : rObj, xaddress : "" }, settings, element).initEvents(cObj);
		}
		
		target.setAddreSection(this,1);
	};
	
	
	// 城市
	_AnoleAddress.sys._renderForCity = function(target, index, params, settings, element) {
		var _cuAry = new Array();
		var _clickEvent = params.renderObj[1];
		var _subHtml = '';
		for (var i = 0; i < settings.ShowOptions.Cities.length; i++) {
			_cuAry.push('<li>' + settings.ShowOptions.Cities[i] + '</li>');
		}
		this.getHtml = function() {
			var _html = '\
			<div class="AddressList">\
                <ul>' + _cuAry.join('') + '</ul>\
            </div>\
            <div class="clear" style="*height:10px;"></div>' + _subHtml;
			return _html;
		};
		this.initEvents = function(cObj) {
			cObj.find('.AddressList li').each(function(i, e) {
				$(this).click(function() {
					_AnoleAddress.sys[_clickEvent].call(this, target, $(this).html(), i, params, settings, element);
				});
			});
		};
		return {
			getHtml : this.getHtml,
			initEvents : this.initEvents
		};
	};
	_AnoleAddress.sys._handleEventByCity = function(target, type, index, params, settings, element) {
		parentGridId = settings.ShowOptions.CitiesId[index];
		addressCode =  settings.ShowOptions.CitiesCode[index];
		addressGridId = parentGridId;
		//获取区县
		_AnoleAddress.sys.getData(parentGridId, 3, settings);
		
		document.getElementById("Counties").innerHTML = "";
		document.getElementById("Streets").innerHTML = "";
		document.getElementById("Communities").innerHTML = "";
		var me = target;
		me.data = {};
		var _item = settings.ShowOptions.TabItems[2];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		var sHtml = "";	
		sHtml = _AnoleAddress.sys[rObj[0]].call(this, target, 2, { renderObj : rObj}, settings, element).getHtml();
		me.divHolder.addClass('AddressControl');
		document.getElementById("Counties").innerHTML = sHtml;
//		//只初始化区县页签
		var _item = settings.ShowOptions.TabItems[2];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		if (typeof (rObj[0]) != "undefined") {
			var cObj = me.divHolder.find('.con').children().eq(2);
			_AnoleAddress.sys[rObj[0]].call(this, target, 2, { renderObj : rObj, xaddress : "" }, settings, element).initEvents(cObj);
		}
		
		target.setAddreSection(this,2);
	};
	// 县区
	_AnoleAddress.sys._renderForCounty = function(target, index, params, settings, element) {
		var _cuAry = new Array();
		var _clickEvent = params.renderObj[1];
		var _subHtml = '';
		for (var i = 0; i < settings.ShowOptions.Counties.length; i++) {
			_cuAry.push('<li>' + settings.ShowOptions.Counties[i] + '</li>');
		}
		this.getHtml = function() {
			var _html = '\
			<div class="AddressList">\
                <ul>' + _cuAry.join('') + '</ul>\
            </div>\
            <div class="clear" style="*height:10px;"></div>' + _subHtml;
			return _html;
		};
		this.initEvents = function(cObj) {
			cObj.find('.AddressList li').each(function(i, e) {
				$(this).click(function() {
					_AnoleAddress.sys[_clickEvent].call(this, target, $(this).html(), i, params, settings, element);
				});
			});
		};
		return {
			getHtml : this.getHtml,
			initEvents : this.initEvents
		};
	};
	_AnoleAddress.sys._handleEventByCounty = function(target, type, index, params, settings, element) {
		parentGridId = settings.ShowOptions.CountiesId[index];
		addressCode =  settings.ShowOptions.CountiesCode[index];
		addressGridId = parentGridId;
		//获取区县
		_AnoleAddress.sys.getData(parentGridId, 4, settings);
		
		document.getElementById("Streets").innerHTML = "";
		document.getElementById("Communities").innerHTML = "";
		var me = target;
		me.data = {};
		var _item = settings.ShowOptions.TabItems[3];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		var sHtml = "";	
		sHtml = _AnoleAddress.sys[rObj[0]].call(this, target, 3, { renderObj : rObj}, settings, element).getHtml();
		me.divHolder.addClass('AddressControl');
		document.getElementById("Streets").innerHTML = sHtml;
//		//只初始化区县页签
		var _item = settings.ShowOptions.TabItems[3];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		if (typeof (rObj[0]) != "undefined") {
			var cObj = me.divHolder.find('.con').children().eq(3);
			_AnoleAddress.sys[rObj[0]].call(this, target, 3, { renderObj : rObj, xaddress : "" }, settings, element).initEvents(cObj);
		}
		
		target.setAddreSection(this,3);
	};
	// 街道
	_AnoleAddress.sys._renderForStreet = function(target, index, params, settings, element) {
		var _cuAry = new Array();
		var _subHtml = '';
		var _clickEvent = params.renderObj[1];
		for (var i = 0; i < settings.ShowOptions.Streets.length; i++) {
			_cuAry.push('<li>' + settings.ShowOptions.Streets[i] + '</li>');
		}
		this.getHtml = function() {
			var _html = '\
			<div class="AddressList">\
                <ul>' + _cuAry.join('') + '</ul>\
            </div>\
            <div class="clear" style="*height:10px;"></div>' + _subHtml;
			return _html;
		};
		this.initEvents = function(cObj) {
			cObj.find('.AddressList li').each(function(i, e) {
				$(this).click(function() {
					_AnoleAddress.sys[_clickEvent].call(this, target, $(this).html(), i, params, settings, element);
				});
			});
		};
		return {
			getHtml : this.getHtml,
			initEvents : this.initEvents
		};
	};
	_AnoleAddress.sys._handleEventByStreet = function(target, type, index, params, settings, element) {
		parentGridId = settings.ShowOptions.StreetsId[index];
		addressCode =  settings.ShowOptions.StreetsCode[index];
		addressGridId = parentGridId;
		//获取社区
		_AnoleAddress.sys.getData(parentGridId, 5, settings);
		
		document.getElementById("Communities").innerHTML = "";
		var me = target;
		me.data = {};
		var _item = settings.ShowOptions.TabItems[4];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		var sHtml = "";	
		sHtml = _AnoleAddress.sys[rObj[0]].call(this, target, 4, { renderObj : rObj}, settings, element).getHtml();
		me.divHolder.addClass('AddressControl');
		document.getElementById("Communities").innerHTML = sHtml;
//		//只初始化社区页签
		var _item = settings.ShowOptions.TabItems[4];
		var rObj = _AnoleAddress.sys.itemRender[_item];
		if (typeof (rObj[0]) != "undefined") {
			var cObj = me.divHolder.find('.con').children().eq(4);
			_AnoleAddress.sys[rObj[0]].call(this, target, 4, { renderObj : rObj, xaddress : "" }, settings, element).initEvents(cObj);
		}
		target.setAddreSection(this,4);
	};
	// 社区
	_AnoleAddress.sys._renderForCommunity = function(target, index, params, settings, element) {
		var _cuAry = new Array();
		var _subHtml = '';
		var _clickEvent = params.renderObj[1];
		for (var i = 0; i < settings.ShowOptions.Communities.length; i++) {
			_cuAry.push('<li>' + settings.ShowOptions.Communities[i] + '</li>');
		}
		this.getHtml = function() {
			var _html = '\
			<div class="AddressList">\
                <ul>' + _cuAry.join('') + '</ul>\
            </div>\
            <div class="clear" style="*height:10px;"></div>' + _subHtml;
			return _html;
		};
		this.initEvents = function(cObj) {
			cObj.find('.AddressList li').each(function(i, e) {
				$(this).click(function() {
					_AnoleAddress.sys[_clickEvent].call(this, target, $(this).html(), i, params, settings, element);
				});
			});
		};
		return {
			getHtml : this.getHtml,
			initEvents : this.initEvents
		};
	};
	_AnoleAddress.sys._handleEventByCommunity = function(target, type, index, params, settings, element) {
		parentGridId = settings.ShowOptions.CommunitiesId[index];
		addressCode =  settings.ShowOptions.CommunitiesCode[index];
		addressGridId = parentGridId;
		target.setAddreSection(this,5);
	};
	
	/* =============================================================
	 * 					  ------ 公共渲染方式 ------
	 * =============================================================*/
	_AnoleAddress.sys._renderForComm = function(target, index, ctrlType, settings, element) {
		target.settings = settings;
		target.element = element;
		target._ctrl_name = ctrlType;
		target._ctrl_index = index;
		target.epk = _AnoleAddress.sys.getEpk(element);
		target.downDivId = target._ctrl_name + "_" + target.epk + "_div_" + target._ctrl_index;
		$('body').append('<div id="' + target.downDivId + '"></div>');
		// div持有者
		target.divHolder = $("#" + target.downDivId).hide();
		/* ==========================================
		 * ------ 下拉按钮相关事件 ------
		 * =========================================*/
		target.btnImgOverEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target.setBackGround(target.settings.DropButton.BINOpenHoverPath);
			} else {				// 打开
				target.setBackGround(target.settings.DropButton.BIYOpenHoverPath);
			}
		};
		target.btnImgOutEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target.setBackGround(target.settings.DropButton.BINOpenPath);
			} else {				// 打开
				target.setBackGround(target.settings.DropButton.BIYOpenPath);
			}
		};
		// 初始化文本框下拉按钮样式
		target.initTextField = function() {
			target.element.attr("isopen", "0");
			target.element.attr('readonly', 'readonly');
			target.element.css({ "cursor" : "pointer" });
			target.setBackGround(target.settings.DropButton.BINOpenPath);
			target.element.click(function(e) {
				target.isBreak = true;
				$(document).click();
				e.stopPropagation();
				target.isBreak = false;
				target.showOrHideDiv(e);
			});
		};
		/* ==========================================
		 * ------ tree/list div ------
		 * =========================================*/
		target.divHolder.click(function(e) {
			e.stopPropagation();
		});
		// 初始化下拉div样式
		target.initDownDiv = function(defWidth, defHeight) {
			target._border_top_w = parseInt(target.element.css('border-top-width')) || 0;
			target._border_left_w = parseInt(target.element.css('border-left-width')) || 0;
			target._border_right_w = parseInt(target.element.css('border-right-width')) || 0;
			target._border_bottom_w = parseInt(target.element.css('border-bottom-width')) || 0;
			var whiteHeight = target.element.outerHeight(true) - (target.element.innerHeight() + target._border_top_w + target._border_bottom_w);
			target._margin_top_w = whiteHeight / 2;
			target._margin_bottom_w = whiteHeight / 2;
			var whiteWidth = target.element.outerWidth(true) - (target.element.innerWidth() + target._border_left_w + target._border_right_w);
			target._margin_left_w = whiteWidth / 2;
			target._margin_right_w = whiteWidth / 2;
			target._top = target.element.offset().top - target._margin_top_w;
			target._left = target.element.offset().left - target._margin_left_w;
			target._background_color = target.element.css("background-color");
			var _css = {
				"position" : "absolute",
				"left" : target._left + target._margin_left_w + "px",
				"top" : target._top + target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w + 1 + "px",
				"background-color" : target._background_color,
				"z-index" : 8001,
				"border-top-width" : target._border_top_w + "px",
				"border-top-color" : target.element.css("border-top-color"),
				"border-top-style" : target.element.css("border-top-style") == "none" ? "solid" : target.element.css("border-top-style"),
				"border-left-width" : target._border_left_w + "px",
				"border-left-color" : target.element.css("border-left-color"),
				"border-left-style" : target.element.css("border-left-style") == "none" ? "solid" : target.element.css("border-left-style"),
				"border-right-width" : target._border_right_w + "px",
				"border-right-color" : target.element.css("border-right-color"),
				"border-right-style" : target.element.css("border-right-style") == "none" ? "solid" : target.element.css("border-right-style"),
				"border-bottom-width" : target._border_right_w + "px",
				"border-bottom-color" : target.element.css("border-right-color"),
				"border-bottom-style" : target.element.css("border-right-style") == "none" ? "solid" : target.element.css("border-right-style")
			};
			target.divHolder.css(_css);
		};
		
		// 显示或隐藏DIV函数
		target.showOrHideDiv = function(event, type, iReset) {
			if (target.isBreak == true) return;
			if (event != null && typeof type == "undefined") {
				var e = event.target || event.srcElement;
				if (target.element.attr("isopen") == "0") {// 当前为关闭状态
					target.initDownDiv();
					// 关闭所有打开状态
					_AnoleAddress.sys.hideAll(e.id);
					target.element.attr("isopen", "1");
					target.setBackGround(target.settings.DropButton.BIYOpenHoverPath);
					target._background_color = target.element.css("background-color");
					target.divHolder.css({ "background-color" : target._background_color });
					target.showOrHideDivEx(true);
				} else {
					target.element.attr("isopen", "0");
					target.setBackGround(target.settings.DropButton.BINOpenHoverPath);
					target.showOrHideDivEx(false);
				}
			} else {
				if (type == "show") {
					target.element.attr("isopen", "1");
					if (iReset) {
						target.setBackGround(target.settings.DropButton.BIYOpenPath);
					}
					target.showOrHideDivEx(false);
				} else {
					target.element.attr("isopen", "0");
					if (iReset) {
						target.setBackGround(target.settings.DropButton.BINOpenPath);
					}
					target.showOrHideDivEx(false);
				}
			}
		};
		
		target.setBackGround = function(imgPath) {
			if (imgPath != null && imgPath.length > 0) {
				target.element.css("background", "url(" + target.settings.ContextPath + _AnoleAddress.sys.path + imgPath + ") no-repeat right");
				if (target.element.hasClass("validatebox-invalid")) {
					target.element.css("background-color", target.element.val() == '' ? target.settings.ShowOptions.ErrorBgColor : "#fff");
				} else {
					target.element.css("background-color", "#fff");
				}
			}
		};
		
		target.showOrHideDivEx = function(isShow) {
			if (isShow) {
				target.divHolder.show();//slideDown(target.settings.ShowOptions.Speed);
			} else {
				target.divHolder.hide();//slideUp(target.settings.ShowOptions.Speed);
			}
		};
		// 拼接地址信息
		target.setAddreSection = function(curObj, level) {
			if(level == 1){
				province = $(curObj).html();
				address = province;
				target.cleanSelectedStyle('AddressSelected', "Provinces");
			}else if(level == 2){
				city = $(curObj).html();
				address = province + ' / ' + city;
				target.cleanSelectedStyle('AddressSelected', "Cities");
			}else if(level == 3){
				county = $(curObj).html();
				address = province + ' / ' + city + ' / ' + county;
				target.cleanSelectedStyle('AddressSelected', "Counties");
			}else if(level == 4){
				street = $(curObj).html();
				address = province + ' / ' + city + ' / ' + county + ' / ' + street;
				target.cleanSelectedStyle('AddressSelected', "Streets");
			}else if(level == 5){
				community = $(curObj).html();
				address = province + ' / ' + city + ' / ' + county + ' / ' + street + ' / ' + community;
				target.cleanSelectedStyle('AddressSelected', "Communities");
			}
			target.element.val(address);//为目标输入框赋值
			$(curObj).addClass('AddressSelected');//添加选中样式
			
			//跳转到下一个页签
			var _bodys = target.divHolder.find('.con').children();
			target.divHolder.find('.nav ul li').each(function(i, e) {
				if(i == level && i != 5){
					$(this).addClass("current").siblings().removeClass("current");
					_bodys.eq(i).show().siblings().hide();
				}
			});
//			// 选中回调函数
			if ($.isFunction(settings.BackEvents.OnSelected)) {
				var epk = _AnoleAddress.sys.getEpk(target.element);
				settings.BackEvents.OnSelected.call(this, {
					getAddress : _AnoleAddress.renderObj[epk].getAddress,
					getAddressWithMark : _AnoleAddress.renderObj[epk].getAddressWithMark,
					getAddressCode : _AnoleAddress.renderObj[epk].getAddressCode
				});
			}
		};
		// 清除选中样式
		target.cleanSelectedStyle = function(className, id, settings) {
			if(id == 'all'){
				target.divHolder.find('ul li').removeClass(className);
				var _bodys = target.divHolder.find('.con').children();
				target.divHolder.find('.nav ul li').each(function(i, e) {
					if(i == 0){
						$(this).addClass("current").siblings().removeClass("current");
						_bodys.eq(i).show().siblings().hide();
					}
				});
			}else{
				target.divHolder.find('#'+ id +' ul li').removeClass(className);
			}
			
		};
		
		// 拼接地址信息2
		target.setAddre = function(addStr, level, target) {
			if(level == 1){
				province = addStr;
				address = province;
			}else if(level == 2){
				city = addStr;
				address = province + ' / ' + city;
			}else if(level == 3){
				county = addStr;
				address = province + ' / ' + city + ' / ' + county;
			}else if(level == 4){
				street = addStr;
				address = province + ' / ' + city + ' / ' + county + ' / ' + street;
			}else if(level == 5){
				community = addStr;
				address = province + ' / ' + city + ' / ' + county + ' / ' + street + ' / ' + community;
			}
			target.element.val(address);//为目标输入框赋值
			
			//跳转到下一个页签
			var _bodys = target.divHolder.find('.con').children();
			target.divHolder.find('.nav ul li').each(function(i, e) {
				if(i == level && i != 6){
					$(this).addClass("current").siblings().removeClass("current");
					_bodys.eq(i).show().siblings().hide();
				}
			});
		};
		
		/* ==========================================
		 * ------ 对象初始化函数 ------
		 * =========================================*/
		target.init = function() {
			target.initTextField();
			/* ==============================================
			 * 显示div并且给document绑定一个click事件，监听鼠标移动事件。
			 * so页面事件尽量不要阻止事件冒泡。
			 * ==============================================*/
			$(document).bind("click", function(_event) {
				var _e = _event.target || _event.srcElement;
				_AnoleAddress.sys.hideAll(_e.id);
			});
		};
	};
	
	_AnoleAddress.sys.getEpk = function(e) {
		return e.get(0).tagName + e.attr("id") + e.attr("name");
	};
	
	// epk : element 主键
	_AnoleAddress.sys.hideAll = function(epk) {
		$("input[anole='" + _AnoleAddress.sys.version + "']").each(function(index, element) {
			var api = $(element).anoleAddressApi(true);
			api.showOrHideDiv(null, "hide", epk != api.epk);
		});
	};
	
	_AnoleAddress.sys.getJsPath = function() {
		// 取得组件包的绝对路径(假设和jquery.anole.date.js部署在同一个目录)
		var jsPath = "";
		var arrScript = document.getElementsByTagName("script");
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			var index = src.indexOf(_AnoleAddress.sys.name);
			if (index >= 0) {
				jsPath = src.substring(0, index);
				if (jsPath.charAt(0) == '/') {
					src = location.href;
					index = src.indexOf('//');
					if (index != -1) {
						index = src.indexOf('/', index + 2);
						if (index != -1)
							src = src.substring(0, index);
						jsPath = src + jsPath;
					}
				}
				break;
			}
		}
		return jsPath;
	};
	
	_AnoleAddress.sys.loadScript = function(jsName) {
		var src = _AnoleAddress.sys.getJsPath();
		document.write('<script type="text/javascript" src="' + src + jsName + '"><\/script>');
	};
	
	_AnoleAddress.sys.anoleContextPath = function() {
		var jsPath = _AnoleAddress.sys.getJsPath();
		jsPath = jsPath.replace(_AnoleAddress.sys.path, '');
		return jsPath;
	};
	
	_AnoleAddress.sys.getData = function(parentGridId,level, settings){
		if(level == '1'){
			settings.ShowOptions.Provinces = [];
			settings.ShowOptions.ProvincesId = [];
			settings.ShowOptions.Cities = [];
			settings.ShowOptions.CitiesId = [];
			settings.ShowOptions.Counties = [];
			settings.ShowOptions.CountiesId = [];
			settings.ShowOptions.Streets = [];
			settings.ShowOptions.StreetsId = [];
			settings.ShowOptions.Communities = [];
			settings.ShowOptions.CommunitiesId = [];
		}else if(level == '2'){
			settings.ShowOptions.Cities = [];
			settings.ShowOptions.CitiesId = [];
			settings.ShowOptions.Counties = [];
			settings.ShowOptions.CountiesId = [];
			settings.ShowOptions.Streets = [];
			settings.ShowOptions.StreetsId = [];
			settings.ShowOptions.Communities = [];
			settings.ShowOptions.CommunitiesId = [];
		}else if(level == '3'){
			settings.ShowOptions.Counties = [];
			settings.ShowOptions.CountiesId = [];
			settings.ShowOptions.Streets = [];
			settings.ShowOptions.StreetsId = [];
			settings.ShowOptions.Communities = [];
			settings.ShowOptions.CommunitiesId = [];
		}else if(level == '4'){
			settings.ShowOptions.Streets = [];
			settings.ShowOptions.StreetsId = [];
			settings.ShowOptions.Communities = [];
			settings.ShowOptions.CommunitiesId = [];
		}else if(level == '5'){
			settings.ShowOptions.Communities = [];
			settings.ShowOptions.CommunitiesId = [];
		}
		var src = _getJsPath() + "/zzgl/addressPluginController/addressForJsonp.jhtml?jsoncallback=?&parentGridId=" + parentGridId + "&level=" + level;
		$.ajax({
			type : "POST",
			url : src,
			contentType : "application/json; charset=utf-8",
			dataType : "jsonp",
			async : false,
			success : function(data) {
				console.log(data)
				$.each(data, function(index, item) {
					if(level == '1'){
						settings.ShowOptions.Provinces.push(item.gridName);
						settings.ShowOptions.ProvincesId.push(item.gridId);
						settings.ShowOptions.ProvincesCode.push(item.gridCode);
					}else if(level == '2'){
						settings.ShowOptions.Cities.push(item.gridName);
						settings.ShowOptions.CitiesId.push(item.gridId);
						settings.ShowOptions.CitiesCode.push(item.gridCode);
					}else if(level == '3'){
						settings.ShowOptions.Counties.push(item.gridName);
						settings.ShowOptions.CountiesId.push(item.gridId);
						settings.ShowOptions.CountiesCode.push(item.gridCode);
					}else if(level == '4'){
						settings.ShowOptions.Streets.push(item.gridName);
						settings.ShowOptions.StreetsId.push(item.gridId);
						settings.ShowOptions.StreetsCode.push(item.gridCode);
					}else if(level == '5'){
						settings.ShowOptions.Communities.push(item.gridName);
						settings.ShowOptions.CommunitiesId.push(item.gridId);
						settings.ShowOptions.CommunitiesCode.push(item.gridCode);
					}
				});
			}
		});
	};
	
	//获取ContextPath上下文路径
	function _getJsPath() {
		// 取得组件包的绝对路径(假设和jquery.anole.address.js部署在同一个目录)
		var jsPath = "";
		var arrScript = document.getElementsByTagName("script");
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			var index = src.indexOf('/js/addressPlugin/jquery.anole.address.js');
			if (index >= 0) {
				jsPath = src.substring(0, index);
				if (jsPath.charAt(0) == '/') {
					src = location.href;
					index = src.indexOf('//');
					if (index != -1) {
						index = src.indexOf('/', index + 2);
						if (index != -1)
							src = src.substring(0, index);
						jsPath = src + jsPath;
					}
				}
				break;
			}
		}
		return jsPath;
	}
	
	$.fn.anoleAddressApi = function(isInner) {
		var epk = _AnoleAddress.sys.getEpk($(this));
		if (isInner) {
			return _AnoleAddress.renderObj[epk];
		} else {
			return {
				//getStartDate : _AnoleAddress.renderObj[epk].getStartDate,
				//getEndDate : _AnoleAddress.renderObj[epk].getEndDate,
			};
		}
	};
	
	$.fn.anoleAddressRender = function(options) {
		var settings = $.extend(true, {}, _AnoleAddress.settings);
		if (settings.ContextPath == null) {
			settings.ContextPath = _AnoleAddress.sys.anoleContextPath();
		}
		if (typeof options.ShowOptions != "undefined") {
			$.extend(settings.ShowOptions, options.ShowOptions);
			delete options.ShowOptions;
		}
		if (typeof options.DropButton != "undefined") {
			$.extend(settings.DropButton, options.DropButton);
			delete options.DropButton;
		}
		if (typeof options.DownDiv != "undefined") {
			$.extend(settings.DownDiv, options.DownDiv);
			delete options.DownDiv;
		}
		$.extend(settings, options);
		return this.each(function (index, element) {
			try {
				if (settings.ContextPath == null) {
					throw new Error("请参照文档正确配置【ContextPath】参数！");
				} else if (settings.ContextPath.length > 0) {
					settings.ContextPath += "/";
				}
				var e = $(element);
				var epk = _AnoleAddress.sys.getEpk(e);
				e.attr("anole", _AnoleAddress.sys.version);
				var obj = new _AnoleAddress.sys.renderForAddress(index, _AnoleAddress.sys.type, settings, e);
				obj.init();
				if (obj != null) {
					_AnoleAddress.renderObj[epk] = obj;
				}
			} catch (e) {
				alert(e.message);
				return false;
			}
		});
	};
	
})(jQuery);