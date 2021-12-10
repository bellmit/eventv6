/**
 * 定义ffcs对象
 */
var ffcs = new Object();
var dialogWidth = "260px";

/**
 * 对话框组件，自定义对话框内的html内容
 * 
 * @param title：对话框标题（该参数传false将不显示标题）
 * @param content：html对象或html字符串
 * @param area：对话框宽度、高度（该参数非必传，默认宽度260px，高度自适应）
 * @param closeBtn：是否显示右上角的关闭按钮（该参数非必传，默认显示，1显示，0不显示）
 * @param type：是否显示全屏（默认显示，1不全屏，2全屏）
 * @param fix：是否固定不让其滚动（true滚动 false 不滚动）
 * @return 组件的index
 */
ffcs.dialogWithHtml = function(title, content, area, closeBtn,type,fix) {
	var index = layer.open({
	    type: type == null ? 1 : type,
	    area: area == null ? dialogWidth : area,
	    title: title,
	    content: content,
	    closeBtn: closeBtn == null ? 1 : closeBtn,
	    fix: fix==null? true:fix
	});
	return index;
};

/**
 * 加载组件
 * 
 * @return 组件的index
 */
ffcs.loading = function() {
	var index = layer.load(
		0, {
			shade: [0.1, '#000']
		}
	);
	return index;
};

/**
 * 提示组件
 * 
 * @param msg：提示信息
 * @return 组件的index
 */
ffcs.alertMsg = function(msg) {
	var index = layer.msg(msg, {
		area: '200px',
		time: 2 * 1000 //2秒关闭
	});
	return index;
};

/**
 * 确认框组件
 * 
 * @param msg：提示信息
 * @param confirmBtn：确认按钮执行的回调方法
 * @param cancelBtn：取消按钮执行的回调方法（该参数非必传，默认执行关闭确认框操作）
 * @param area：对话框宽度、高度（该参数非必传，默认宽度260px，高度自适应）
 * @return 组件的index
 */
ffcs.confirmMsg = function(msg, confirmBtn, cancelBtn, area) {
	var index = layer.confirm(
		msg, {
	    	btn: ['确定', '取消'], //按钮
	    	area: area == null ? dialogWidth : area,
	    	title: false, //不显示标题
	    	closeBtn: 0
    	},
    	function() {
    		confirmBtn();
    		layer.close(index);
    	},
    	cancelBtn
	);
	return index;
};

/**
 * 提示并确认组件（解决Ajax回调时不居中的问题）
 * 
 * @param msg：提示信息
 * @param confirmBtn：确认按钮执行的回调方法
 * @return 组件的index
 */
ffcs.alertAndConfirm = function(msg, confirmBtn) {
	if (confirmBtn == null) {
		var index = layer.alert(msg, {
			title: false //不显示标题
		});
		return index;
	}
	var index = layer.alert(msg, {
			title: false, //不显示标题
		}, function() {
			confirmBtn();
			layer.close(index);
		}
	);
	return index;
};
