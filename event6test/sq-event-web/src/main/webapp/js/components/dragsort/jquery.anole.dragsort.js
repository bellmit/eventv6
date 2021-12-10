if (typeof (DragSortApi) == "undefined") {
	var DragSortApi = {};
}

DragSortApi._Sys = {
	name : "jquery.anole.dragsort.js",
	path : "/js/components/dragsort/"
};

DragSortApi._getJsPath = function() {
	// 取得组件包的绝对路径(假设和anole.imageview.api.js部署在同一个目录)
	var jsPath = "";
	var arrScript = document.getElementsByTagName("script");
	for ( var i = 0; i < arrScript.length; i++) {
		var src = arrScript[i].src;
		var index = src.indexOf(DragSortApi._Sys.name);
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

DragSortApi._getContextPath = function() {
	var jsPath = DragSortApi._getJsPath();
	jsPath = jsPath.replace(DragSortApi._Sys.path, '');
	return jsPath;
};

DragSortApi._loadScript = function(jsName) {
	var jsPath = DragSortApi._getJsPath();
	document.write('<script type="text/javascript" src="' + jsPath + jsName + '"><\/script>');
};

DragSortApi._loadCss = function(cssName) {
	var jsPath = DragSortApi._getJsPath();
	document.write('<link rel="stylesheet" type="text/css" href="' + jsPath + cssName + '"/>');
};

DragSortApi.sort = function(dataGridObj, options) {
	var settings = { vw : { id : "", title : "", isAllData : false, rmIds : [] }, db : { code : "" }, event : { saveAfter : null } };
	settings = $.extend(true, settings, options);
	if (dataGridObj != null) {
		var msg = dialog().content('<span class="dragsortloading"></span>正在加载数据，请稍等...');
		msg.showModal();
		var rows = [];
		if (settings.vm.isAllData) {
			var options = dataGridObj.datagrid('options');
			var pm = "page=1&rows=99999999";
			for ( var key in options.queryParams) {
				pm += "&" + key + "=" + options.queryParams[key];
			}
			$.ajax({
				type : "get",
				url : options.url,
				data : pm,
				cache : false,
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					msg.close().remove();
					rows = data;
					if (settings.vm.rmIds && settings.vm.rmIds.length > 0) {
						for (var key in settings.vm.rmIds) {
							for (var i = rows.length - 1; i >= 0; i--) {
								if (settings.vm.rmIds[key] == rows[i][settings.vm.id]) {
									rows.splice(i, 1);
									break;
								}
			            	}
						}
					}
					DragSortApi.show(dataGridObj, settings, rows);
				},
				error : function() {
					msg.content('加载数据失败！');
					setTimeout(function () {
						msg.close().remove();
					}, 1000);
				}
			});
		} else {
			rows = dataGridObj.datagrid('getRows');
			msg.close().remove();
			DragSortApi.show(dataGridObj, settings, rows);
		}
	}
};

DragSortApi.show = function(dataGridObj, settings, rows) {
	var html = "";
	var colors = [ "qingse", "lvse", "lanse", "huangse", "hongse", "heise" ];
	for ( var row in rows) {
		var i = parseInt(row);
		var title = rows[row][settings.vm.title] == null ? "" : rows[row][settings.vm.title];
		html += "<li><span>" + (i + 1) + "</span><div title='" + title
				+ "' class='" + colors[i % 6] + "' id='"
				+ rows[row][settings.vm.id] + "'>" + title + "</div></li>";
	}
	var pDlg = dialog({
		title: '请拖拽方块进行排序',
		content: '<div style="height: 280px;overflow-x:hidden; overflow-y:auto;padding: 10px;position:relative;"><ul class="dragsortlist">'+html+'</ul></div>',
		okValue: '保 存',
		width: 600,
		padding:0,
		ok: function () {
			var cDlg = dialog().content('<span class="dragsortloading"></span>正在保存当前排序状态...');
			cDlg.showModal();
			DragSortApi.save(dataGridObj, pDlg, cDlg, settings);
			return false;
		},
		cancelValue: '关 闭',
		cancel: function () {}
	});

	pDlg.showModal();

	$(".dragsortlist").dragsort({ dragSelector: "div", dragBetween: true, placeHolderTemplate: "<li class='placeHolder'><div></div></li>" });
};

DragSortApi.save = function(dataGridObj, pDlg, cDlg, settings) {
	var data = $(".dragsortlist li").map(function() { return $(this).children().eq(1).attr("id"); }).get();
	var pm = "code="+settings.db.code+"&ids="+data.join(',');
	$.ajax({
		type : "POST",
		url : DragSortApi._getContextPath() + "/zhsq/map/menuconfigure/menuConfig/sort.jhtml?"+pm+"&jsoncallback=?",
		contentType : "application/json; charset=utf-8",
		dataType : "jsonp",
		success : function(data) {
			cDlg.content(data.msg);
			if (data.flag) {
				setTimeout(function () {
					cDlg.close().remove();
					pDlg.close().remove();
					dataGridObj.datagrid('load');
					if ($.isFunction(settings.event.saveAfter)) {
						settings.event.saveAfter.call(this);
					}
				}, 1000);
			} else {
				setTimeout(function () {
					cDlg.close().remove();
				}, 1000);
			}
		},
		error : function() {
			cDlg.content('保存失败！');
			setTimeout(function () {
				cDlg.close().remove();
			}, 1000);
		}
	});
};

// 加载相关js、css
if (!window.jQuery) {
	// document.write('<script type="text/javascript" src="jquery.js"></script>');
}
DragSortApi._loadCss("css/dragsort.css");
DragSortApi._loadCss("css/ui-dialog.css");
DragSortApi._loadScript("plugs/jquery.dragsort-0.5.1.min.js");
DragSortApi._loadScript("plugs/dialog-plus-min.js");
