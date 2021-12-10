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
	var _ShowMenuType = {
		List : "0",
		Tree : "1",
		Table : "2"
	};
	// 定义支持哪些选择方式
	var _CtrlMenuType = {
		Radio : "0",
		Checkbox : "1"
	};
	// 存放渲染后的组件对象
	var _CtrlMenuObject = {};
	
	var _SysMenu = {
		version : "jQuery-leaple-ComboBox-v1.0.3",
		IncIsReady : 2,
		path : "/js/combobox/"
	};
	
	// 下拉框渲染为普通列表形式
	function renderMenuForList(index, type, settings, element) {
		var me = this;
		// 缓存相关数据
		me._data = {
			name : "",
			value : "",
			nodes : null
		};
		_renderMenuForComm(this, index, type, settings, element);
		this.structureMenuData = function(data, mode) {
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
					"border" : "0",
					"left" : (me.element.position().left + me._margin_left_w - 5) + "px"
				});
				me.divHolder.attr('id', this.downDivTableId);
				me.divHolder.addClass('DropDownList');
				var _items = new Array();
				if (_CtrlMenuType.Radio == type) {
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
				} else if (_CtrlMenuType.Checkbox == type) {
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
					if (_CtrlMenuType.Radio == type) {
						navObj.next().find('a').each(function() {
							$(this).bind('click', function() {
								me.setMenuDataValue($(this).attr('rowid'), data);
								me.showOrHideMenuDiv(null, "hide", true);
							});
						});
					} else if (_CtrlMenuType.Checkbox == type) {
						// TODO: 暂无实现
					}
				});
				me.renderMenuCompleted(mode);
			}
		};
		
		this.setMenuDataValue = function(rowids, rows) {
			if (_CtrlMenuType.Radio == type) {
				var item = rows[rowids];
				item = typeof item == "undefined" ? {} : item;
				var dataCode = item[this.settings.DataValue];
				var dataName = item[this.settings.DataName];
				var showText = "";
				if (this.settings.BackfillType == "0") {
					showText = dataName;
				} else if (this.settings.BackfillType == "1") {
					showText = dataCode;
				} else if (this.settings.BackfillType == "2") {
					showText = dataCode + "-" + dataName;
				}
				this.element.val(showText);
				me._data.name = showText;
				me._data.value = dataCode;
				me._data.nodes = item;
				if (this.settings.TargetId && this.settings.TargetId.length > 0) {
					var targetObj = $("#" + this.settings.TargetId);
					var oldVal = targetObj.val();
					targetObj.val(dataCode);
					// Callback Function
					if (oldVal != dataCode && $.isFunction(this.settings.OnChanged)) {
						this.settings.OnChanged.call(this);
					}
				}
				if ($.isFunction(this.settings.OnSelected)) {
					this.settings.OnSelected.call(this, item);
				}
			} else if (_CtrlMenuType.Checkbox == type) {
				// TODO:暂无实现
			}
		};

		this.getMenuDataValue = function() {
			return this._data.value;
		};
		
		this.getMenuDataName = function() {
			return this._data.name;
		};
		
		this.getSelectedNodes = function() {
			return this._data.nodes;
		};

		this.setMenuSelectedNodes = function(codes) {
			var rowids = new Array();
			$.each(this.settings.DataSrc, function(i, node) {
				$.each(codes, function(j, code) {
					if (node[me.settings.DataValue] == code) {
						rowids.push(i);
						node.checked = true;
					}
				});
			});
			if (rowids.length > 0) this.setMenuDataValue(rowids, this.settings.DataSrc);
		};
	}
	
	// 下拉框渲染为树形结构形式
	function renderMenuForTree(index, type, settings, element) {
		var me = this;
		// 缓存相关数据
		me._data = {
			name : "",
			value : "",
			nodes : null
		};
		_renderMenuForComm(this, index, type, settings, element);
		// 数据构造接口
		this.structureMenuData = function(data, mode) {
			if (_SysMenu.IncIsReady == 2) {
				_SysMenu.IncIsReady = 1;
				var jsPath = _getMenuJsPath();
				if (typeof $.fn.zTree == "undefined") {
					$.getScript(jsPath + "jquery.ztree.core-3.5.min.js", function() {
						if (typeof $.fn.zTree.consts.event.CHECK == "undefined") {
							$.getScript(jsPath + "jquery.ztree.excheck-3.5.min.js", function() {
								_SysMenu.IncIsReady = 0;
								me.structureMenuDataEx(data, mode);
							});
						} else {
							_SysMenu.IncIsReady = 0;
							me.structureMenuDataEx(data, mode);
						}
					});
				} else if (typeof $.fn.zTree.consts.event.CHECK == "undefined") {
					$.getScript(jsPath + "jquery.ztree.excheck-3.5.min.js", function() {
						_SysMenu.IncIsReady = 0;
						me.structureMenuDataEx(data, mode);
					});
				} else {
					_SysMenu.IncIsReady = 0;
					me.structureMenuDataEx(data, mode);
				}
			} else if (_SysMenu.IncIsReady == 1) {
				window.setTimeout(function() { me.structureMenuData(data, mode); }, 100);
			} else {
				me.structureMenuDataEx(data, mode);
			}
		};
		
		this.structureMenuDataEx = function(data, mode) {
			if ($("#" + me.downDivUlId).length == 0) {
				this.divHolder.html("<ul id='"+me.downDivUlId+"' class='ztree' style='width: 100%;' ></ul>");
				var setting = {
					async: {
						enable : false,
						url : "",
						autoParam : [],
						dataFilter : null,
						otherParam : {},
						dataType : "jsonp",
						type : "get"
					},
					data: {
						key : {
							name : me.settings.DataName
						},
						simpleData: {
							enable: true,
							idKey: me.settings.DataCode,
							pIdKey: me.settings.DataPCode,
							rootPId: me.settings.RootPCode,
							valKey: me.settings.DataValue
						}
					},
					view: {
						fontCss: this.getMenuFontCss
					},
					callback: {
						onCheck: this._zTreeMenuOnCheck,
						onClick: this._zTreeMenuOnClick
					}
				};
				if (_CtrlMenuType.Radio == type) {
					setting.check = {
						enable: true,
						chkStyle: "radio",
						radioType: "all"
					};
					setting.check = {
						enable: false
					};
					/*if (this.ShowType == _ShowMenuType.List) {
						
					}*/
					// setting.callback.onDblClick = this._zTreeMenuOnDblClick;
				} else if (_CtrlMenuType.Checkbox == type) {
					setting.check = {
						enable: true,
						chkStyle: "checkbox",
						chkboxType: { "Y": "ps", "N": "ps" }
					};
				}
				if (me.settings.Async.enable) {
					$.extend(setting.async, me.settings.Async);
					setting.async.url = data;
					setting.callback.onAsyncSuccess = this._zTreeMenuOnAsyncSuccess;
					$.fn.zTree.init($("#" + me.downDivUlId), setting);
				} else {
					delete setting.async;
					$.fn.zTree.init($("#" + me.downDivUlId), setting, data);
					me.renderMenuCompleted(mode);
				}
			}
		};
		
		this._zTreeMenuOnAsyncSuccess = function(event, treeId, treeNode, msg) {
			if (me.IsRenderComplete == 1) {
				if (me.settings.IsTriggerLoad) {
					me.renderMenuCompleted();
				} else {
					me.renderMenuCompleted("noClick");
				}
			}
		};
		
		this._zTreeMenuOnClick = function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if (_CtrlMenuType.Radio == type) {
				if (me.settings.ChooseType == "1" || treeNode["nocheck"] == false) {
					// treeObj.checkNode(treeNode, true, true, true);
					var nodes = treeObj.getSelectedNodes();
					if (nodes.length > 0 && (me.ShowType == _ShowMenuType.List || (me.ShowType == _ShowMenuType.Tree && nodes[0][me.settings.DataValue] != "-1"))) {
						me._zTreeMenuOnCheckEx(nodes);
						me.showOrHideMenuDiv(null, "hide", true);
					}
				}/* else if (treeNode["isleaf"] == true) {
					me._zTreeMenuOnCheckEx(treeObj.getSelectedNodes());
					me.showOrHideMenuDiv(null, "hide", true);
				}*/
			} else if (_CtrlMenuType.Checkbox == type) {
				treeObj.checkNode(treeNode, !treeNode.checked, true, true);
			}
		};
		
		this._zTreeMenuOnDblClick = function(event, treeId, treeNode) {
			me.showOrHideMenuDiv(null, "hide", true);
		};
		
		this._zTreeMenuOnCheck = function(event, treeId, treeNode) {
			var nodes = $.fn.zTree.getZTreeObj(treeId).getCheckedNodes(true);
			me._zTreeMenuOnCheckEx(nodes);
		};
		
		this.getMenuFontCss = function(treeId, treeNode) {
			return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
		};
		
		this._zTreeMenuOnCheckEx = function(treeNodes) {
			var names = new Array();
			var values = new Array();
			$.each(treeNodes, function(i, node) {
				names.push(node[me.settings.DataName]);
				values.push(node[me.settings.DataValue]);
			});
			me.setMenuDataValue(names, values, treeNodes);
		};
		
		// 设置值接口
		this.setMenuDataValue = function(names, values, nodes) {
			var nvs = new Array();
			$.each(values, function(i, value) {
				nvs.push(value + "-" + names[i]);
			});
			var showText = "";
			if (me.settings.BackfillType == "0") {
				showText = names.join(",");
			} else if (me.settings.BackfillType == "1") {
				showText = values.join(",");
			} else if (me.settings.BackfillType == "2") {
				showText = nvs.join(",");
			}
			me.element.val(showText);
			me._data.name = showText;
			me._data.value = values.join(",");
			me._data.nodes = nodes;
			if (me.settings.TargetId && me.settings.TargetId.length > 0) {
				var targetObj = $("#" + me.settings.TargetId);
				var oldVal = targetObj.val();
				targetObj.val(values.join(","));
				if (oldVal != values.join(",") && $.isFunction(me.settings.OnChanged)) {
					me.settings.OnChanged.call(this, values.join(","), nodes);
				}
			}
			if (_CtrlMenuType.Radio == type) {
				if ($.isFunction(me.settings.OnSelected)) {
					me.settings.OnSelected.call(this, values.length > 0 ? values[0] : null, nodes);
				}
			} else if (_CtrlMenuType.Checkbox == type) {
				if ($.isFunction(me.settings.OnSelected)) {
					me.settings.OnSelected.call(this, values, nodes);
				}
			}
			if (_CtrlMenuType.Radio == type || _CtrlMenuType.Checkbox == type) {
				me._showMenuNodes();
			}
		};
		
		this.getMenuDataValue = function() {
			return this._data.value;
		};
		
		this.getMenuDataName = function() {
			return this._data.name;
		};
		
		this.getSelectedNodes = function() {
			return this._data.nodes;
		};
		
		this.setMenuSelectedNodes = function(codes) {
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
			if (names.length > 0) this.setMenuDataValue(names, values);
		};
		
		this.setMenuDisabled = function(flag) {
			me.settings.Disabled = flag;
			me.setMenuBackGround(me.settings.DropButton.BINOpenPath);
		};
	}
	
	/* =============================================================
	 * 					  ------ 公共渲染方式 ------
	 * =============================================================*/
	function _renderMenuForComm(target, index, type, settings, element) {
		target.IsRenderComplete = 2;
		target.settings = settings;
		target.element = element;
		target._ctrl_name = "combobox_";
		target._ctrl_index = index;
		target.epk = _getMenuEpk(element);
		target.downDivUlId = target._ctrl_name + target.epk + "_ul_" + target._ctrl_index;
		target.downDivId = target._ctrl_name + target.epk + "_div_" + target._ctrl_index;
		target.cleanDivId = target._ctrl_name + target.epk + "_clean_" + target._ctrl_index;
		target.nodeList = [];
		// target.element.after('<div id="' + target.downDivId + '"></div>');
		target.element.unbind("click");
		var div = $('#' + target.downDivId);
		if (div.length > 0) {
			div.unbind();
			div.remove();
		}
		div = $('#' + target.cleanDivId);
		if (div.length > 0) {
			div.unbind();
			div.remove();
		}
		$('body').append('<div id="' + target.downDivId + '"></div>').append('<div id="' + target.cleanDivId + '" title="清空输入框"></div>');
		// div持有者
		target.divHolder = $("#" + target.downDivId).hide();
		target.divCleaner = $("#" + target.cleanDivId).hide();
		/* ==========================================
		 * ------ 下拉按钮相关事件 ------
		 * =========================================*/
		/*target.btnImgOverEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target.setMenuBackGround(target.settings.DropButton.BINOpenHoverPath);
			} else {				// 打开
				target.setMenuBackGround(target.settings.DropButton.BIYOpenHoverPath);
			}
		};
		target.btnImgOutEvent = function() {
			var isopen = $(this).attr("isopen");
			if (isopen == "0") {	// 关闭
				target.setMenuBackGround(target.settings.DropButton.BINOpenPath);
			} else {				// 打开
				target.setMenuBackGround(target.settings.DropButton.BIYOpenPath);
			}
		};*/
		// 初始化文本框下拉按钮样式
		target.initMenuTextField = function() {
			target.element.attr("isopen", "0");
			target.setMenuBackGround(target.settings.DropButton.BINOpenPath);
			target.element.css({ "padding-right" : "24px" });
			if (!target.settings.EnabledSearch) {// false禁用
				target.element.attr('readonly', 'readonly');
				target.element.css({ "cursor" : "pointer" });
			} else {
				target.element.keyup(target._searchMenuNodes);
				//target.element.blur(target._resetMenuSearchText);
			}
			target.element.click(function(e) {
				if (!target.settings.Disabled) {
					target.isBreak = true;
					if (target.settings.IsTriggerDocument) $(document).click();
					e.stopPropagation();
					target.isBreak = false;
					target.showOrHideMenuDiv(e);
				}
			});
		};
		// TODO:
		target._updateMenuNodes = function(highlight) {
			var zTree = $.fn.zTree.getZTreeObj(target.downDivUlId);
			for( var i = 0, l = target.nodeList.length; i < l; i++) {
				target.nodeList[i].highlight = highlight;
				zTree.updateNode(target.nodeList[i]);
				zTree.expandNode(target.nodeList[i], true, true, false);
				var pNode = target.nodeList[i].getParentNode();
				while (pNode != null) {
					try {
						zTree.expandNode(pNode, true, true, false);
						pNode = pNode.getParentNode();
					} catch (e) {
						break;
					}
				}
			}
		};
		target._showMenuNodes = function() {
			if (target.IsRenderComplete == 0 && target.settings.EnabledSearch) {
				var zTree = $.fn.zTree.getZTreeObj(target.downDivUlId);
	            var hiddenNodes = zTree.getNodesByParam("isHidden", true);
	            if (hiddenNodes) {
	              zTree.showNodes(hiddenNodes);
	            }
	            target._updateMenuNodes(false);
			}
		};
		target._searchMenuNodes = function(e) {
			target.showOrHideMenuDiv(null, "show", true);
			var zTree = $.fn.zTree.getZTreeObj(target.downDivUlId);
			// zTree.checkAllNodes(false);
			target._updateMenuNodes(false);
            var hiddenNodes = zTree.getNodesByParam("isHidden", true);
            if (hiddenNodes) {
              zTree.showNodes(hiddenNodes);
            }
            var searchForName = target.element.val();
            if (!searchForName || searchForName=="") {
              return;
            }
            var nodes = zTree.getNodesByFilter(function (node) {
                if (!node.isParent && node.name.indexOf(searchForName) == -1) {
                  return true;
                }
                return false;
            });
            zTree.hideNodes(nodes); //hide child node
            var noChildParentNodes = zTree.getNodesByFilter(function (node) {
                if (node.isParent && (!node.children || node.children.length == 0 || node.children.length == zTree.getNodesByParam("isHidden", true, node).length)) {
                  return true;
                }
                return false;
            });
            zTree.hideNodes(noChildParentNodes); //hide no child parent node
            var keyType = "name";
			target.nodeList = zTree.getNodesByParamFuzzy(keyType, searchForName);
			target._updateMenuNodes(true);
		};
		target._resetMenuSearchText = function() {
			if (target.settings.EnabledSearch) {
				var zTree = $.fn.zTree.getZTreeObj(target.downDivUlId);
	            var hiddenNodes = zTree.getNodesByParam("isHidden", true);
	            if (hiddenNodes) {
	              zTree.showNodes(hiddenNodes);
	            }
	            target.element.val(target._data.name);
			}
		};
		/* ==========================================
		 * ------ tree/list div ------
		 * =========================================*/
		target.divHolder.click(function(e) {
			e.stopPropagation();
		});
		target.divCleaner.click(function(e) {
			target._data.name = '';
			target._data.value = '';
			target._data.nodes = null;
			target.element.val(target._data.name);
			target._resetMenuSearchText();
			if (target.settings.TargetId && target.settings.TargetId.length > 0) {
				var targetObj = $("#" + target.settings.TargetId);
				var oldVal = targetObj.val();
				targetObj.val(target._data.value);
				if (oldVal != target._data.value && $.isFunction(target.settings.OnChanged)) {
					target.settings.OnChanged.call(this, target._data.value, null);
				}
			}
			if ($.isFunction(target.settings.OnCleared)) {
				target.settings.OnCleared.call(this);
			}
		});
		// TODO: 重复定位DIV位置
		target._repeatMenuPosition = function(hDoc, hDiv, maxH, minH) {
			target._top = target.element.offset().top;
			var hElement = target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w;
			var _top = target._top + hElement;
			var hTop = target._top;
			var hBtm = hDoc - _top;
			var hHalf = (hTop + hBtm) / 2;
			if (hHalf >= hDiv) {// 文档扣除元素高度的一半不小于DIV高度
				if (hBtm >= hDiv) {
					target.divHolder.css({ "top" : _top + "px" });
				} else {
					target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
				}
			} else {
				var _maxH = 0;
				if (hBtm + 30 >= hTop) {
					if (hBtm >= hDiv) {
						if (hBtm > maxH) {
							_maxH = maxH;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						} else {
							_maxH = hBtm - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						}
						target.divHolder.css({ "top" : _top + "px" });
					} else {
						if (!target.settings.DownDiv.IsAutoStyle) {
							_maxH = hBtm - 3;
							target.divHolder.css({ "top" : _top + "px", "max-height" : _maxH + "px" });
						}
					}
				} else {
					if (hTop >= hDiv) {
						if (hTop > maxH) {
							_maxH = maxH;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						} else {
							_maxH = hTop - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
						}
						hDiv = target.divHolder.outerHeight(true);
						target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
					} else {
						if (!target.settings.DownDiv.IsAutoStyle) {
							_maxH = hTop - 3;
							target.divHolder.css({ "max-height" : _maxH + "px" });
							hDiv = target.divHolder.outerHeight(true);
							target.divHolder.css({ "top" : (_top - hDiv - hElement) + "px" });
						}
					}
				}
				if (minH >= _maxH) {
					target.divHolder.css({ "min-height" : _maxH + "px" });
				} else {
					target.divHolder.css({ "min-height" : minH + "px" });
				}
			}
			if (target.settings.ShowOptions.EnableToolbar) {
				_top = target._top + target._margin_top_w + target._border_top_w + ((target.element.innerHeight() - 18) / 2);
				target.divCleaner.css({ "top" : _top + "px" });
			}
		};
		// 初始化下拉div样式
		target.initMenuDownDiv = function(defWidth, defHeight) {
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
			// target._top = target.element.position().top;
			// target._left = target.element.position().left;
			target._top = target.element.offset().top;
			target._left = target.element.offset().left;
			target._background_color = target.element.css("background-color");
			var _css = {
				"position" : "absolute",
				"left" : target._left + target._margin_left_w + "px",
				"top" : target._top + target._margin_top_w + target._margin_bottom_w + target.element.innerHeight() + target._border_top_w + target._border_bottom_w + "px",
				"background-color" : target._background_color,
				"z-index" : 8001,
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
			};
			target.divHolder.css(_css);
			if (target.settings.DownDiv.IsAutoStyle) {	// 是否自适应样式
				target.divHolder.css({
					"width" : "auto",
					"height" : "auto",
					"overflow-y" : "hidden",
					"overflow-x" : "hidden"
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
		
		target.initMenuToolbar = function() {
			var _left = 5 + target._left + target._border_left_w + target._border_right_w + target._margin_left_w + target._margin_right_w + target.element.innerWidth() - 27;
			var _top = target._top + target._margin_top_w + target._border_top_w + ((target.element.innerHeight() - 18) / 2);
			var _css = {
				"position" : "absolute",
				"left" : _left + "px",
				"top" : _top + "px",
				"background" : "url(" + target.settings.ContextPath + _SysMenu.path + "images/eraser.png) no-repeat",
				"z-index" : 8002,
				"overflow-y" : "auto",
				"width" : "18px",
				"height" : "18px",
				"border" : "0px",
				"padding" : "0px",
				"margin" : "0px",
				"cursor" : "pointer"
			};
			target.divCleaner.css(_css);
		};
		
		target.doMenuFilterDataEx = function(sData, fData) {
			var _tempData = fData;
			if (Object.prototype.toString.call(fData) === '[object String]') {
				var _fIndex = -1;
				for (var i = sData.length - 1; i >= 0; i--) {
					if (sData[i].dictCode == fData) {
						_fIndex = i;
						sData[i].count = 1;
						fData = sData[i].dictPcode;
					}
            	}
				if (_fIndex > -1) {
					var _Codes = new Array();
					_Codes.push(_tempData);
					for (var i = _fIndex; i < sData.length; i++) {
						for ( var j = 0; j < _Codes.length; j++) {
							if (sData[i].dictPcode == _Codes[j]) {
								sData[i].count = 1;
								_Codes.push(sData[i].dictCode);
							}
						}
	            	}
				}
			}
			return sData;
		};
		
		target.doMenuFilterData = function(data) {
			if (target.settings.FilterData != null && Object.prototype.toString.call(target.settings.FilterData) === '[object Array]') {
				for ( var i = 0; i < target.settings.FilterData.length; i++) {
					var obj = target.settings.FilterData[i];
					data = target.doMenuFilterDataEx(data, obj);
				}
				for (var i = data.length - 1; i >= 0; i--) {
					// 过滤类型（0-保留指定数据，去除其它数据，1-保留其它数据，去除指定数据）
					if (target.settings.FilterType == "0") {
						if (data[i].count != 1) {
							data.splice(i, 1);
						}
					} else if (target.settings.FilterType == "1") {
						if (data[i].count == 1) {
							data.splice(i, 1);
						}
					}
            	}
			}
			return data;
		};
		
		target.renderMenuCompleting = function(mode) {
			if (target.IsRenderComplete != 1) {
				target.IsRenderComplete = 1;
				target.setMenuBackGround(target.settings.DropButton.BILoadingPath);
				if (target.settings.Async.enable) {
					var url = target.settings.DataSrc;
					target.settings.DataSrc = [];
					target.structureMenuData(url, mode);
				} else if (typeof target.settings.DataSrc != "object") {
					$.ajax({
						type : "POST",
						url : target.settings.DataSrc,
						data : "",
						contentType : "application/json; charset=utf-8",
						dataType : "jsonp",
						success : function(data) {
							target.settings.DataSrc = target.doMenuFilterData(data);
							// Play with returned data in JSON format
							if ($.isFunction(target.settings.OnRenderCompleting)) {
								target.settings.OnRenderCompleting.call(target, _CtrlMenuObject[target.epk]);
							}
							// 构造数据
							target.structureMenuData(target.settings.DataSrc, mode);
						}
					});
				} else {
					if ($.isFunction(target.settings.OnRenderCompleting)) {
						target.settings.OnRenderCompleting.call(target, _CtrlMenuObject[target.epk]);
					}
					// 构造数据
					window.setTimeout(function() { target.structureMenuData(target.settings.DataSrc, mode); }, 300);
				}
			}
		};
		target.renderMenuCompleted = function(mode) {
			if (target.IsRenderComplete != 0) {
				target.IsRenderComplete = 0;
				// 鼠标移入/移出事件
				// target.element.hover(target.btnImgOverEvent, target.btnImgOutEvent);
				if ($.isFunction(target.settings.OnRenderCompleted)) {
					target.settings.OnRenderCompleted.call(target, _CtrlMenuObject[target.epk]);
				}
				if (mode != "noClick") {
					target.element.click();
				} else {
					target.setMenuBackGround(target.settings.DropButton.BINOpenPath);
				}
			}
		};
		// 显示或隐藏DIV函数
		target.showOrHideMenuDiv = function(event, type, iReset) {
			if (target.isBreak == true) return;
			if (event != null && typeof type == "undefined") {
				if (target.IsRenderComplete != 0) {
					return target.renderMenuCompleting();
				}
				var e = event.target || event.srcElement;
				if (target.element.attr("isopen") == "0") {// 当前为关闭状态
					// 关闭所有打开状态
					_hideMenuAll(e.id);
					target.element.attr("isopen", "1");
					target.initMenuDownDiv(target.element.innerWidth(), target.settings.DownDiv.Style.height);
					if (target.settings.ShowOptions.EnableToolbar) {
						target.initMenuToolbar();
						target.element.css({ "background" : "" });
					} else {
						target.setMenuBackGround(target.settings.DropButton.BIYOpenHoverPath);
					}
					// TODO:
					if (_ShowMenuType.Table == target.ShowType) {
						target.divHolder.css({
							"background-color" : "transparent",
							"border" : "0",
							"left" : (target._left + target._margin_left_w - 5) + "px"
						});
					}/* else {
						target._background_color = target.element.css("background-color");
						target.divHolder.css({ "background-color" : target._background_color });
					}*/
					target.showOrHideMenuDivEx(true);
					// 构造数据
					// target.structureMenuData(target.settings.DataSrc);
				} else {
					target.element.attr("isopen", "0");
					target.setMenuBackGround(target.settings.DropButton.BINOpenHoverPath);
					target.showOrHideMenuDivEx(false);
				}
			} else {
				if (type == "show") {
					target.element.attr("isopen", "1");
					if (target.settings.ShowOptions.EnableToolbar) {
						target.element.css({ "background" : "" });
					} else {
						if (iReset) {
							target.setMenuBackGround(target.settings.DropButton.BIYOpenPath);
						}
					}
					target.showOrHideMenuDivEx(true);
				} else {
					target.element.attr("isopen", "0");
					if (iReset) {
						target.setMenuBackGround(target.settings.DropButton.BINOpenPath);
					}
					target.showOrHideMenuDivEx(false);
				}
			}
		};
		
		target.setMenuBackGround = function(imgPath) {
			if (imgPath != null && imgPath.length > 0) {
				target.element.css("background", "url(" + target.settings.ContextPath + _SysMenu.path + imgPath + ") no-repeat right");
				if (target.settings.Disabled) {
					target.element.css("background-color", target.settings.ShowOptions.DisabledColor);
				} else {
					if ($.isFunction(target.settings.OnBusiVerify)) {
						var iFlag = target.settings.OnBusiVerify.call(this);
						target.element.css("background-color", iFlag ? "#fff" : target.settings.ShowOptions.ErrorBgColor);
					} else {
						if (target.element.hasClass("validatebox-invalid")) {
							target.element.css("background-color", target.element.val() == '' ? target.settings.ShowOptions.ErrorBgColor : "#fff");
						} else {
							target.element.css("background-color", "#fff");
						}
					}
				}
			}
		};
		
		target.showOrHideMenuDivEx = function(isShow) {
			if (isShow) {
				var hDoc = $(document).height();
				var maxH = parseInt(target.divHolder.css("max-height")) || 0;
				var minH = parseInt(target.divHolder.css("min-height")) || 0;
				target._repeatMenuPosition(hDoc, target.divHolder.outerHeight(true), maxH, minH);
				target.divHolder.show();//slideDown(target.settings.ShowOptions.Speed);
				if (target.settings.ShowOptions.EnableToolbar) {
					target.divCleaner.show();//slideDown(target.settings.ShowOptions.Speed);
				}
				target.timerId = window.setInterval(function() {
					target._repeatMenuPosition(hDoc, target.divHolder.outerHeight(true), maxH, minH);
				}, 500);
			} else {
				target.divHolder.hide();//slideUp(target.settings.ShowOptions.Speed);
				if (target.settings.ShowOptions.EnableToolbar) {
					target.divCleaner.hide();//slideUp(target.settings.ShowOptions.Speed);
				}
				window.clearInterval(target.timerId);
			}
		};
		/* ==========================================
		 * ------ 对象初始化函数 ------
		 * =========================================*/
		target.init = function() {
			target.initMenuTextField();
			/* ==============================================
			 * 显示div并且给document绑定一个click事件，监听鼠标移动事件。
			 * so页面事件尽量不要阻止事件冒泡。
			 * ==============================================*/
			$(document).bind("click", function(_event) {
				var _e = _event.target || _event.srcElement;
				_hideMenuAll(_e.id);
			});
		};
	}
	
	function _getMenuEpk(e) {
		return e.get(0).tagName + e.attr("id") + e.attr("name");
	}
	// epk : element 主键
	function _hideMenuAll(epk) {
		$("input[anole='" + _SysMenu.version + "']").each(function(index, element) {
			var api = $(element).menuTreeApi();
			if (api.IsRenderComplete == 0) {
				api.showOrHideMenuDiv(null, "hide", epk != api.epk);
			}
		});
	}
	
	function _getMenuJsPath() {
		// 取得组件包的绝对路径(假设和jquery.anole.combobox.js部署在同一个目录)
		var jsPath = "";
		var arrScript = document.getElementsByTagName("script");
		for ( var i = 0; i < arrScript.length; i++) {
			var src = arrScript[i].src;
			var index = src.indexOf('jquery.menu.combobox.js');
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
	
	$.anoleMenuContextPath = function() {
		var jsPath = _getMenuJsPath();
		jsPath = jsPath.replace(_SysMenu.path, '');
		return jsPath;
	};
	
	_loadScriptMenu = function(jsName) {
		var jsPath = _getMenuJsPath();
		document.write('<script type="text/javascript" src="' + jsPath + jsName + '"><\/script>');
	};
	
	$.fn.menuTreeApi = function() {
		var epk = _getMenuEpk($(this));
		return _CtrlMenuObject[epk];
	};
	
	$.fn.anoleMenuRender = function(options) {
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
				Speed : 3,			// 显示速度
				TableHeadCount : 6,
				TableBodyCount : 7,
				EnableToolbar : false,
				ErrorBgColor : "#fff3f3",
				IsShowAllData : false,
				DisabledColor : "#EBEBE5"
			},
			Disabled : false,		// 禁用true、启用false
			BackfillType : "0",		// 回填方式(0-名称,1-代码,2-代码+名称,中间逗号分隔)
			IsTriggerLoad : false,	// 是否触发才加载数据(false-否,true-是)
			IsTriggerDocument : true,
			EnabledSearch : false,	// 是否启用搜索框功能
			SearchType : "0",		// 搜索类型(0-当前下拉数据中搜索,1-根据URL到数据库中搜索)
			FilterSize : 20,		// 启用搜索框的结果集大小,默认20条启用
			FilterData : null,		// 过滤数据
			FilterType : "0",		// 过滤类型（0-保留指定数据，去除其它数据，1-保留其它数据，去除指定数据）
			ChooseType : "0",		// 节点选择方式(0-只能选择叶子, 1-都可以选择)
			// ------ event ------
			OnChanged : null,		// 选择框值变化回调事件
			OnSelected : null,		// 选中回调事件，返回数据集。
			OnRenderCompleting : null,	// 渲染中回调函数，返回api对象。
			OnRenderCompleted : null,	// 渲染完毕回调函数，返回api对象。
			OnCleared : null,		// 清除后回调函数
			OnBusiVerify : null,	// 业务验证
			// ------ 下拉按钮个性属性 ------
			DropButton : {
				IsAutoStyle : true,		// 自适应样式
				BINOpenPath : "../../images/sys_07.png",
				BINOpenHoverPath : "../../images/sys_07.png",
				BIYOpenPath : "../../images/sys_07.png",
				BIYOpenHoverPath : "../../images/sys_07.png",
				BILoadingPath : "images/loading1.gif",
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
		if (settings.ShowOptions.IsShowAllData) {
			settings.Async.otherParam["other"] = "1";
		}
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
					var epk = _getMenuEpk(e);
					var high = settings.RenderType.substring(0, 1);
					var type = settings.RenderType.substring(1, 2);
					e.attr("anole", _SysMenu.version);
					if (_ShowMenuType.Tree == high || _ShowMenuType.List == high) {	// 以树形结构展示
						obj = new renderMenuForTree(index, type, settings, e);
					} else if (_ShowMenuType.Table == high) {					// 以表格形式展示
						obj = new renderMenuForList(index, type, settings, e);
					} else {
						throw new Error("请参照文档正确传入【RenderType】参数！");
					}
					obj.ShowType = high;
					obj.init();
					if (obj != null) {
						_CtrlMenuObject[epk] = obj;
						if (!settings.IsTriggerLoad) {
							obj.renderMenuCompleting("noClick");
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
	// 加载插件js
	if (typeof $.fn.zTree == "undefined") {
		_loadScriptMenu("jquery.ztree.core-3.5.min.js");
		_loadScriptMenu("jquery.ztree.excheck-3.5.min.js");
		_loadScriptMenu("jquery.ztree.exhide-3.5.min.js");
	}
})(jQuery);