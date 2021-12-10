/**
 * jQuery Anole ComboBox v1.0.3
 *
 * Copyright 2014, YangCQ
 * No licensed under the licenses.
 * http://www.ximifan.com/
 *
 * Includes jquery.js、jquery.ztree.core.js、jquery.ztree.excheck.js
 * http://jquery.com/
 *
 * Date: 2014-06-06 09:50:03
 */
(function($) {
	// 定义支持哪些展示方式
	var _ShowType = {
		List : "0",
		Tree : "1",
		Table : "2"
	};
	// 定义支持哪些选择方式
	var _CtrlType = {
		Radio : "0",
		Checkbox : "1"
	};
	// 存放渲染后的组件对象
	var _CtrlObject = {};
	
	var _Sys = {
		version : "jQuery-Anole-ComboBox-v1.0.3",
		IncIsReady : 2
	};
	
	// 下拉框渲染为普通列表形式
	function renderForList(index, type, settings, element) {
		var me = this;
		_renderForComm(this, index, type, settings, element);
		this.structureData = function(data, mode) {
			this.downDivTableId = this._ctrl_name + this.epk + "_table_" + this._ctrl_index;
			if ($("#" + this.downDivTableId).length == 0) {
				this.prefixName =  this._ctrl_name + this.epk + this._ctrl_index;
				// 自动与非自动
				if (this.settings.DownDiv.IsAutoStyle) {
					me.divHolder.css('width', 'auto');
				} else {
					me.divHolder.css('width', '100%');
				}
				me.divHolder.css({
					"background-color" : "transparent",
					"border" : "0",
					"left" : (me.element.position().left + me._margin_left_w - 5) + "px"
				});
				me.divHolder.attr('id', this.downDivTableId);
				me.divHolder.addClass('DropDownList');
				var _items = new Array();
				if (_CtrlType.Radio == type) {
					if (typeof data == "object" && typeof data.length != "undefined") {
						var _item_heads = new Array();
						var _item_bodys = new Array();
						var _item_head_count = me.settings.ShowOptions.TableHeadCount;
						var _item_body_count = me.settings.ShowOptions.TableBodyCount;
						var _item_body = "";
						var table_body = '<div class="nav"><ul>_item_head</ul></div><div class="ListShow"><div class="tabss2">_item_body</div></div>';
						var _temp_item_id = '';
						var _temp_head_index = 0;
						var _temp_body_index = 0;
						var _temp_item_body = '';
						var _temp_item_isShow = true;
						$.each(data, function(index, item) {
							if (item[me.settings.DataPCode] == _temp_item_id) {
								if (_temp_body_index > 0) {
									_temp_item_body += '<li class="line">|</li>';
								}
								_temp_item_body += '<li style="margin:0; padding:0;"><a href="#" rowid="'+index+'" value="'+item[me.settings.DataValue]+'">'+item[me.settings.DataName]+'</a></li>';
								_temp_body_index++;
								// _item_body_count个换一行
								if (_temp_body_index % _item_body_count == 0) {
									_item_body += '<ul>' + _temp_item_body + '</ul>';
									_temp_item_body = '';
									_temp_body_index = 0;
								}
								// 最后一个
								if (data.length - 1 == index) {
									if (_temp_item_id != '') {
										if (_temp_item_body != '') {
											_item_body += '<ul>' + _temp_item_body + '</ul>';
											_temp_item_body = '';
										}
										_item_bodys.push('<div '+(_temp_item_isShow?'class="StyleSelect"':'class="StyleSelect hide"')+'>' + _item_body + '</div>');
										_items.push((_items.length == 0 ? '' : '<div class="h_10"></div>') + table_body.replace('_item_head', _item_heads.join("")).replace('_item_body', _item_bodys.join("")));
										_item_head = '';
										_item_body = '';
										_item_heads.splice(0, _item_heads.length);
										_item_bodys.splice(0, _item_bodys.length);
									}
								}
							} else {
								if (_temp_item_id != '') {
									if (_temp_item_body != '') {
										_item_body += '<ul>' + _temp_item_body + '</ul>';
										_temp_item_body = '';
									}
									_item_bodys.push('<div '+(_temp_item_isShow?'class="StyleSelect"':'class="StyleSelect hide"')+'>' + _item_body + '</div>');
									_item_body = '';
									_temp_head_index++;
									_temp_item_isShow = false;
								}
								if (_temp_head_index != 0 && _temp_head_index % _item_head_count == 0) {
									_items.push((_items.length == 0 ? '' : '<div class="h_10"></div>') + table_body.replace('_item_head', _item_heads.join("")).replace('_item_body', _item_bodys.join("")));
									_item_head = '';
									_item_body = '';
									_item_heads.splice(0, _item_heads.length);
									_item_bodys.splice(0, _item_bodys.length);
									_temp_item_isShow = true;
								}
								_item_heads.push('<li '+(_temp_item_isShow?'class="current"':'')+' id="'+me.prefixName+"_li_"+index+'" name="'+me.prefixName+'_li" value="'+item[me.settings.DataValue]+'">'+item[me.settings.DataName]+'</li>');
								// 参数复位
								_temp_body_index = 0;
								_temp_item_id = item[me.settings.DataCode];
							}
						});
					}
				} else if (_CtrlType.Checkbox == type) {
					// TODO: 暂无实现
				}
				var table_html = '<div class="LeftShadow"><div class="RightShadow"><div class="list"><div class="ConList" style="padding:10px;">'+_items.join("")+'</div></div><div class="BottomShadow"></div></div></div>';
				me.divHolder.html(table_html);
				if (me.settings.DownDiv.IsAutoStyle) {
					var inputEWidth = me.element.innerWidth();
					if (me.divHolder.innerWidth() - 10 <= inputEWidth) {
						me.divHolder.css({ "width" : (inputEWidth + me._border_left_w + me._border_right_w + 10) + "px" });
					}
				}
				me.divHolder.find('.nav').each(function() {
					var navObj = $(this);
					var lis = navObj.find('li');
					lis.each(function(i, ele) {
						$(this).bind('click', function() {
							lis.removeClass('current');
							$(this).addClass('current');
							var obj = navObj.next().find('.StyleSelect');
							obj.hide();
							obj.eq(i).show();
						});
					});
					// 单选：选择前先清除样式
					if (_CtrlType.Radio == type) {
						navObj.next().find('a').each(function() {
							$(this).bind('click', function() {
								me.setDataValue($(this).attr('rowid'), data);
								me.showOrHideDiv(null, "hide", true);
							});
						});
					} else if (_CtrlType.Checkbox == type) {
						// TODO: 暂无实现
					}
				});
				me.renderCompleted(mode);
			}
		};
		
		this.setDataValue = function(rowids, rows) {
			if (_CtrlType.Radio == type) {
				var item = rows[rowids];
				item = typeof item == "undefined" ? {} : item;
				var dataCode = item[this.settings.DataValue];
				var dataName = item[this.settings.DataName];
				if (this.settings.BackfillType == "0") {
					this.element.val(dataName);
				} else if (this.settings.BackfillType == "1") {
					this.element.val(dataCode);
				} else if (this.settings.BackfillType == "2") {
					this.element.val(dataCode + "-" + dataName);
				}
				if (this.settings.TargetId.length > 0) {
					var targetObj = $("#" + this.settings.TargetId);
					var oldVal = targetObj.val();
					targetObj.val(dataCode);
					// Callback Function
					if (oldVal != dataCode && this.settings.OnChanged != null) {
						this.settings.OnChanged.call(this);
					}
				}
				if (this.settings.OnSelected != null) {
					this.settings.OnSelected.call(this, item);
				}
			} else if (_CtrlType.Checkbox == type) {
				// TODO:暂无实现
			}
			me.initDropBtn();
		};
		
		this.apiSelectedNodes = function(codes) {
			var rowids = new Array();
			$.each(this.settings.DataSrc, function(i, node) {
				$.each(codes, function(j, code) {
					if (node[me.settings.DataValue] == code) {
						rowids.push(i);
						node.checked = true;
					}
				});
			});
			this.setDataValue(rowids, this.settings.DataSrc);
		};
	}
	
	// 下拉框渲染为树形结构形式
	function renderForTree(index, type, settings, element) {
		var me = this;
		this.IsReady = false;
		_renderForComm(this, index, type, settings, element);
		// 数据构造接口
		this.structureData = function(data, mode) {
			if (_Sys.IncIsReady == 2) {
				_Sys.IncIsReady = 1;
				var jsPath = _getJsPath();
				if (typeof $.fn.zTree == "undefined") {
					$.getScript(jsPath + "jquery.ztree.core-3.5.min.js", function() {
						if (typeof $.fn.zTree.consts.event.CHECK == "undefined") {
							$.getScript(jsPath + "jquery.ztree.excheck-3.5.min.js", function() {
								_Sys.IncIsReady = 0;
								me.structureDataEx(data, mode);
							});
						} else {
							_Sys.IncIsReady = 0;
							me.structureDataEx(data, mode);
						}
					});
				} else if (typeof $.fn.zTree.consts.event.CHECK == "undefined") {
					$.getScript(jsPath + "jquery.ztree.excheck-3.5.min.js", function() {
						_Sys.IncIsReady = 0;
						me.structureDataEx(data, mode);
					});
				} else {
					_Sys.IncIsReady = 0;
					me.structureDataEx(data, mode);
				}
			} else if (_Sys.IncIsReady == 1) {
				window.setTimeout(function() { me.structureData(data, mode); }, 100);
			} else {
				me.structureDataEx(data, mode);
			}
		};
		
		this.structureDataEx = function(data, mode) {
			this.downDivUlId = this._ctrl_name + this.epk + "_ul_" + this._ctrl_index;
			if ($("#" + this.downDivUlId).length == 0) {
				this.divHolder.html("<ul id='"+this.downDivUlId+"' class='ztree' style='width: 100%;' ></ul>");
				var setting = {
					async: {
						enable : false,
						url : "",
						autoParam : [],
						dataFilter : null,
						otherParam : {}
					},
					data: {
						simpleData: {
							enable: true,
							idKey: me.settings.DataCode,
							pIdKey: me.settings.DataPCode,
							rootPId: me.settings.RootPCode,
							valKey: me.settings.DataValue
						}
					},
					callback: {
						onCheck: this._zTreeOnCheck,
						onClick: this._zTreeOnClick
					}
				};
				if (_CtrlType.Radio == type) {
					setting.check = {
						enable: true,
						chkStyle: "radio",
						radioType: "all"
					};
					setting.check = {
						enable: false
					};
					/*if (this.ShowType == _ShowType.List) {
						
					}*/
					// setting.callback.onDblClick = this._zTreeOnDblClick;
				} else if (_CtrlType.Checkbox == type) {
					setting.check = {
						enable: true,
						chkStyle: "checkbox",
						chkboxType: { "Y": "ps", "N": "ps" }
					};
				}
				if (me.settings.Async.enable) {
					$.extend(setting.async, me.settings.Async);
					setting.async.url = data;
					setting.callback.onAsyncSuccess = this._zTreeOnAsyncSuccess;
					$.fn.zTree.init($("#" + this.downDivUlId), setting);
				} else {
					delete setting.async;
					$.fn.zTree.init($("#" + this.downDivUlId), setting, data);
					me.renderCompleted(mode);
				}
			}
		};
		
		this._zTreeOnAsyncSuccess = function(event, treeId, treeNode, msg) {
			if (me.IsRenderComplete == 1) {
				if (me.settings.IsTriggerLoad) {
					me.renderCompleted();
				} else {
					me.renderCompleted("noClick");
				}
			}
		};
		
		this._zTreeOnClick = function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if (_CtrlType.Radio == type) {
				if (treeNode["nocheck"] == false) {
					// treeObj.checkNode(treeNode, true, true, true);
					var nodes = treeObj.getSelectedNodes();
					if (nodes.length > 0 && (me.ShowType == _ShowType.List || (me.ShowType == _ShowType.Tree && nodes[0][me.settings.DataValue] != "-9999"))) {
						me._zTreeOnCheckEx(nodes);
						me.showOrHideDiv(null, "hide", true);
					}
				}/* else if (treeNode["isleaf"] == true) {
					me._zTreeOnCheckEx(treeObj.getSelectedNodes());
					me.showOrHideDiv(null, "hide", true);
				}*/
			} else if (_CtrlType.Checkbox == type) {
				treeObj.checkNode(treeNode, !treeNode.checked, true, true);
			}
		};
		
		this._zTreeOnDblClick = function(event, treeId, treeNode) {
			me.showOrHideDiv(null, "hide", true);
		};
		
		this._zTreeOnCheck = function(event, treeId, treeNode) {
			var nodes = $.fn.zTree.getZTreeObj(treeId).getCheckedNodes(true);
			me._zTreeOnCheckEx(nodes);
		};
		
		this._zTreeOnCheckEx = function(treeNodes) {
			var names = new Array();
			var values = new Array();
			$.each(treeNodes, function(i, node) {
				names.push(node[me.settings.DataName]);
				values.push(node[me.settings.DataValue]);
			});
			me.setDataValue(names, values, treeNodes);
		};
		
		// 设置值接口
		this.setDataValue = function(names, values, nodes) {
			var nvs = new Array();
			$.each(values, function(i, value) {
				nvs.push(value + "-" + names[i]);
			});
			if (me.settings.BackfillType == "0") {
				me.element.val(names.join(","));
			} else if (me.settings.BackfillType == "1") {
				me.element.val(values.join(","));
			} else if (me.settings.BackfillType == "2") {
				me.element.val(nvs.join(","));
			}
			if (me.settings.TargetId.length > 0) {
				var targetObj = $("#" + me.settings.TargetId);
				var oldVal = targetObj.val();
				targetObj.val(values.join(","));
				if (oldVal != values.join(",") && me.settings.OnChanged != null) {
					me.settings.OnChanged.call(this, values.join(","), nodes);
				}
			}
			if (_CtrlType.Radio == type) {
				if (me.settings.OnSelected != null) {
					me.settings.OnSelected.call(this, values.length > 0 ? values[0] : null, nodes);
				}
			} else if (_CtrlType.Checkbox == type) {
				if (me.settings.OnSelected != null) {
					me.settings.OnSelected.call(this, values, nodes);
				}
			}
			me.initDropBtn();
		};
		
		this.apiSelectedNodes = function(codes) {
			var names = new Array();
			var values = new Array();
			$.each(this.settings.DataSrc, function(i, node) {
				$.each(codes, function(j, code) {
					if (node[me.settings.DataValue] == code) {
						names.push(node[me.settings.DataName]);
						values.push(code);
						node.checked = true;
					}
				});
			});
			this.setDataValue(names, values);
		};
	}
	
	/* =============================================================
	 * 					  ------ 公共渲染方式 ------
	 * =============================================================*/
	function _renderForComm(target, index, type, settings, element) {
		target.IsRenderComplete = 2;
		target.settings = settings;
		target.element = element;
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
		target._top = target.element.position().top;//target.element.offset().top - target._margin_top_w;
		target._left = target.element.position().left;//target.element.offset().left - target._margin_left_w;
		target._background_color = target.element.css("background-color");
		target._ctrl_name = "combobox_";
		target._ctrl_index = index;
		target.epk = _getEpk(element);
		target.dropButtonId = target._ctrl_name + target.epk + "_button_" + target._ctrl_index;
		target.downDivId = target._ctrl_name + target.epk + "_div_" + target._ctrl_index;
		target.btnImgId = target._ctrl_name + target.epk + "_img_" + target._ctrl_index;
		$('body').append('<div id="' + target.dropButtonId + '"><img id="' + target.btnImgId + '"/></div>').append('<div id="' + target.downDivId + '"></div>');
		// target.element.after('<div id="' + target.dropButtonId + '"><img id="' + target.btnImgId + '"/></div>').after('<div id="' + target.downDivId + '"></div>');
		// btn持有者
		target.btnHolder = $("#" + target.dropButtonId).hide();
		// div持有者
		target.divHolder = $("#" + target.downDivId).hide();
		// btn img 持有者
		target.btnImgHolder = target.btnHolder.find("#" + target.btnImgId);
		/* ==========================================
		 * ------ 下拉按钮相关事件 ------
		 * =========================================*/
		target.btnImgHolder.click(function(e) {
			e.stopPropagation();
			target.showOrHideDiv(e);
		});
		target.btnImgOverEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				if (target.settings.DropButton.BINOpenHoverPath != null && target.settings.DropButton.BINOpenHoverPath.length > 0) {
					$(this).attr("src", target.settings.ContextPath + target.settings.DropButton.BINOpenHoverPath);
				}
			} else {				// 打开
				if (target.settings.DropButton.BIYOpenHoverPath != null && target.settings.DropButton.BIYOpenHoverPath.length > 0) {
					$(this).attr("src", target.settings.ContextPath + target.settings.DropButton.BIYOpenHoverPath);
				}
			}
		};
		target.btnImgOutEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				if (target.settings.DropButton.BINOpenPath != null && target.settings.DropButton.BINOpenPath.length > 0) {
					$(this).attr("src", target.settings.ContextPath + target.settings.DropButton.BINOpenPath);
				}
			} else {				// 打开
				if (target.settings.DropButton.BIYOpenPath != null && target.settings.DropButton.BIYOpenPath.length > 0) {
					$(this).attr("src", target.settings.ContextPath + target.settings.DropButton.BIYOpenPath);
				}
			}
		};
		// 鼠标移入/移出事件
		target.btnImgHolder.hover(target.btnImgOverEvent, target.btnImgOutEvent);
		// 初始化下拉按钮图片样式
		target.initDropBtnImg = function() {
			target.btnImgHolder.css({
				"width" : "100%",
				"height" : "100%"
			});
			target.btnImgHolder.attr("isopen", "0");
			target.btnImgHolder.attr("src", target.settings.ContextPath + target.settings.DropButton.BINOpenPath);
		};
		target.initTextField = function() {
			if (!target.settings.EnabledSearch) {// false禁用
				target.element.attr('readonly', 'readonly');
				target.element.css({ "cursor" : "pointer" });
				target.element.click(function(e) {
					e.stopPropagation();
					target.showOrHideDiv(e);
				});
			}
		};
		/* ==========================================
		 * ------ button ------
		 * =========================================*/
		// 初始化按钮DIV样式
		target.initDropBtn = function() {
			var btnHeight = target.element.innerHeight();
			var btnWidth = btnHeight;
			if (!target.settings.DropButton.IsAutoStyle
					&& typeof target.settings.DropButton.Style != "undefined") {
				if (typeof target.settings.DropButton.Style.width != "undefined") {
					btnWidth = parseInt(target.settings.DropButton.Style.width) || 0;
				}
			}
			target.btnHolder.css({
				"position" : "absolute",
				"left" : (target._left + target._margin_left_w + target._border_left_w + target.element.innerWidth() - btnWidth) + "px",
				"top" : target._top + target._margin_top_w + target._border_top_w + "px",
				"width" : btnWidth + "px",
				"height" : btnHeight + "px",
				// "background-color" : target._background_color,
				"z-index" : 100,
				"font-size" : "0px",
				"line-height" : "0px",
				"cursor" : "pointer"
			});
			if (target.settings.DropButton.IsAutoStyle) {	// 是否自适应样式
				// 暂不支持
			} else {
				if (typeof target.settings.DropButton.Style != "undefined") {
					delete target.settings.DropButton.Style.position;
					delete target.settings.DropButton.Style.left;
					delete target.settings.DropButton.Style.top;
					// delete target.settings.DropButton.Style.width;
					delete target.settings.DropButton.Style.height;
					target.btnHolder.css(target.settings.DropButton.Style);
				}
			}
		};
		target.showBtn = function() {
			target.btnHolder.show();
		};
		target.hideBtn = function() {
			target.btnHolder.hide();
		};
		/* ==========================================
		 * ------ tree/list div ------
		 * =========================================*/
		target.divHolder.click(function(e) {
			e.stopPropagation();
		});
		// 初始化下拉div样式
		target.initDownDiv = function(defWidth, defHeight) {
			target.divHolder.css({
				"position" : "absolute",
				"left" : target._left + target._margin_left_w + "px",
				"top" : target._top + target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w + "px",
				"background-color" : target._background_color,
				"z-index" : 101,
				"overflow-y" : "auto",
				"min-width" : defWidth + "px",
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
			});
			if (target.settings.DownDiv.IsAutoStyle) {	// 是否自适应样式
				target.divHolder.css({
					"width" : "auto",
					"height" : "auto",
					"overflow-y" : "hidden"
				});
			} else {
				if (typeof target.settings.DownDiv.Style != "undefined") {
					delete target.settings.DownDiv.Style.position;
					delete target.settings.DownDiv.Style.left;
					delete target.settings.DownDiv.Style.top;
					if (typeof target.settings.DownDiv.Style["min-width"] != undefined) {
						var _width = parseInt(target.settings.DownDiv.Style["min-width"]);
						if (!isNaN(_width) && _width > 0 && _width < defWidth) {
							target.settings.DownDiv.Style["min-width"] = defWidth + "px";
						}
					}
					target.divHolder.css(target.settings.DownDiv.Style);
					if (typeof target.settings.DownDiv.Style.width == "undefined") {
						target.divHolder.css({ "width" : defWidth + "px" });
					}
					if (typeof target.settings.DownDiv.Style.height == "undefined") {
						target.divHolder.css({ "height" : defHeight + "px" });
					}
				}
			}
		};
		target.renderCompleting = function(mode) {
			if (target.IsRenderComplete != 1) {
				target.IsRenderComplete = 1;
				target.btnImgHolder.unbind('mouseenter').unbind('mouseleave');
				target.btnImgHolder.attr("src", target.settings.ContextPath + target.settings.DropButton.BILoadingPath);
				if (target.settings.Async.enable) {
					var url = target.settings.DataSrc;
					target.settings.DataSrc = [];
					target.structureData(url, mode);
				} else if (typeof target.settings.DataSrc != "object") {
					$.ajax({
						type : "POST",
						url : target.settings.DataSrc,
						data : "",
						contentType : "application/json; charset=utf-8",
						dataType : "json",
						success : function(data) {
							target.settings.DataSrc = data;
							// Play with returned data in JSON format
							if (target.settings.OnRenderCompleting != null) {
								target.settings.OnRenderCompleting.call(target, _CtrlObject[target.epk]);
							}
							// 构造数据
							target.structureData(target.settings.DataSrc, mode);
						}
					});
				} else {
					if (target.settings.OnRenderCompleting != null) {
						target.settings.OnRenderCompleting.call(target, _CtrlObject[target.epk]);
					}
					// 构造数据
					window.setTimeout(function() { target.structureData(target.settings.DataSrc, mode); }, 300);
				}
			}
		};
		target.renderCompleted = function(mode) {
			if (target.IsRenderComplete != 0) {
				target.IsRenderComplete = 0;
				target.btnImgHolder.hover(target.btnImgOverEvent, target.btnImgOutEvent);
				if (target.settings.OnRenderCompleted != null) {
					target.settings.OnRenderCompleted.call(target, _CtrlObject[target.epk]);
				}
				if (mode != "noClick") {
					target.element.click();
				} else {
					target.btnImgHolder.attr("src", target.settings.ContextPath + target.settings.DropButton.BINOpenPath);
				}
			}
		};
		// 显示或隐藏DIV函数
		target.showOrHideDiv = function(event, type, iReset) {
			if (event != null && typeof type == "undefined") {
				if (target.IsRenderComplete != 0) {
					return target.renderCompleting();
				}
				var e = event.target || event.srcElement;
				if (target.btnImgHolder.attr("isopen") == "0") {// 当前为关闭状态
					// 关闭所有打开状态
					_hideAll(e.id);
					target.btnImgHolder.attr("isopen", "1");
					var realpath = target.settings.DropButton.BIYOpenPath;
					// 判断click对象是否是下拉按钮图片
					if (e.tagName == "IMG" && e.id == target.btnImgId) {
						realpath = target.settings.DropButton.BIYOpenHoverPath;
					}
					target.btnImgHolder.attr("src", target.settings.ContextPath + realpath);
					target.divHolder.slideDown(target.settings.ShowOptions.Speed);
					// 构造数据
					// target.structureData(target.settings.DataSrc);
					/* ==============================================
					 * 显示div并且给document绑定一个click事件，监听鼠标移动事件。
					 * so页面事件尽量不要阻止事件冒泡。
					 * ==============================================*/
					$(document).bind("click", function(_event) {
						var _e = _event.target || _event.srcElement;
						_hideAll(_e.id);
						$(this).unbind("click");
					});
					/*$(document).bind('mouseover', function(e) {
						$(this).bind("click", function(event) {
							var e = event.target || event.srcElement;
							_hideAll(e.id);
							$(this).unbind("click");
						}).unbind("mouseover");
					});*/
				} else {
					target.btnImgHolder.attr("isopen", "0");
					var realpath = target.settings.DropButton.BINOpenPath;
					// 判断click对象是否是下拉按钮图片
					if (e.tagName == "IMG" && e.id == target.btnImgId) {
						realpath = target.settings.DropButton.BINOpenHoverPath;
					}
					target.btnImgHolder.attr("src", target.settings.ContextPath + realpath);
					target.divHolder.slideUp(target.settings.ShowOptions.Speed);
					$(document).unbind("click");// 解绑click事件
				}
			} else {
				if (type == "show") {
					target.btnImgHolder.attr("isopen", "1");
					if (iReset) {
						var realpath = target.settings.DropButton.BIYOpenPath;
						target.btnImgHolder.attr("src", target.settings.ContextPath + realpath);
					}
					target.divHolder.slideDown(target.settings.ShowOptions.Speed);
				} else {
					target.btnImgHolder.attr("isopen", "0");
					if (iReset) {
						var realpath = target.settings.DropButton.BINOpenPath;
						target.btnImgHolder.attr("src", target.settings.ContextPath + realpath);
					}
					target.divHolder.slideUp(target.settings.ShowOptions.Speed);
				}
			}
		};
		/* ==========================================
		 * ------ 对象初始化函数 ------
		 * =========================================*/
		target.init = function() {
			target.initTextField();
			target.initDropBtnImg();
			target.initDropBtn();
			target.initDownDiv(target.element.innerWidth(), target.settings.DownDiv.Style.height);
			target.showBtn();
		};
	}
	

	function _getJsPath() {
		// 取得组件包的绝对路径(假设和jquery.anole.combobox.js部署在同一个目录)
		var jsPath = "";
		var arrScript = document.getElementsByTagName("script");
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			var index = src.indexOf('jquery.anole.combobox.js');
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
	
	function _getEpk(e) {
		return e.get(0).tagName + e.attr("id") + e.attr("name");
	}
	// epk : element 主键
	function _hideAll(epk) {
		$("input[anole='" + _Sys.version + "']").each(function(index, element) {
			var api = $(element).anoleApi();
			if (api.IsRenderComplete == 0) {
				api.showOrHideDiv(null, "hide", epk != api.epk);
			}
		});
	}
	
	$.fn.anoleApi = function() {
		var epk = _getEpk($(this));
		return _CtrlObject[epk];
	};
	
	$.fn.anoleRender = function(options) {
		var settings = {
			ContextPath : null,		// 上下文路径(必传)
			RenderType : "",		// 渲染类型(高位：低位：)
			TargetId : "",			// 目标ID
			DataSrc : null,			// 数据源(Array数据对象或URL字符串)
			Async : {
				enable : false,
				autoParam : [],
				dataFilter: null,
				otherParam: {}
			},
			DataName : "name",		// 字典名称
			DataValue : "value",	// 字典值
			DataCode : "id",		// 字典编码
			DataPCode : "pid",		// 字典父编码
			RootPCode : "",			// 根节点编码
			DefaultName : null,		// 默认显示名称
			ShowOptions : {
				Speed : 300,		// 显示速度
				TableHeadCount : 6,
				TableBodyCount : 7
			},
			BackfillType : "0",		// 回填方式(0-名称,1-代码,2-代码+名称,中间逗号分隔)
			IsTriggerLoad : false,	// 是否触发才加载数据(false-否,true-是)
			EnabledSearch : false,	// 是否启用搜索框功能
			SearchType : "0",		// 搜索类型(0-当前下拉数据中搜索,1-根据URL到数据库中搜索)
			FilterSize : 20,		// 启用搜索框的结果集大小,默认20条启用
			FilterType : "1",		// 启用搜索框的显示结果类型(1-当有子节点显示,父节点也必须显示;2-当父节点被隐藏,子节点的层次调整为父节点的层次)
			// ------ event ------
			OnChanged : null,		// 选择框值变化回调事件
			OnSelected : null,		// 选中回调事件，返回数据集。
			OnRenderCompleting : null,	// 渲染中回调函数，返回api对象。
			OnRenderCompleted : null,	// 渲染完毕回调函数，返回api对象。
			// ------ 下拉按钮个性属性 ------
			DropButton : {
				IsAutoStyle : true,		// 自适应样式
				BINOpenPath : "images/sys_07.png",
				BINOpenHoverPath : "images/sys_07.png",
				BIYOpenPath : "images/sys_07.png",
				BIYOpenHoverPath : "images/sys_07.png",
				BILoadingPath : "images/loading.gif",
				Style : {}				// 个性样式
			},
			// ------ 下拉DIV个性属性 ------
			DownDiv : {
				IsAutoStyle : true,			// 自适应样式
				ItemHoverBgColor : "#E6E6E6",	// item项背景颜色
				Style : {
					height : "150"
				}
			}
		};
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
				if (settings.RenderType.length == 2) {
					var obj = null;
					var e = $(element);
					var epk = _getEpk(e);
					var high = settings.RenderType.substring(0, 1);
					var type = settings.RenderType.substring(1, 2);
					e.attr("anole", _Sys.version);
					if (_ShowType.Tree == high || _ShowType.List == high) {	// 以树形结构展示
						obj = new renderForTree(index, type, settings, e);
					} else if (_ShowType.Table == high) {					// 以表格形式展示
						obj = new renderForList(index, type, settings, e);
					} else {
						throw new Error("请参照文档正确传入【RenderType】参数！");
					}
					obj.ShowType = high;
					obj.init();
					if (obj != null) {
						_CtrlObject[epk] = obj;
						if (!settings.IsTriggerLoad) {
							obj.renderCompleting("noClick");
						}
					}
				} else {
					throw new Error("请参照文档正确配置【RenderType】参数！");
				}
			} catch (e) {
				alert(e.message);
				return false;
			}
		});
	};
})(jQuery);