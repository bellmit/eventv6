var Sys = {};
var ua = navigator.userAgent.toLowerCase();
Sys.system = ua.match(/windows nt ([\d.]+)/)[1];// window nt 版本号
if (window.ActiveXObject)
	Sys.ie = ua.match(/msie ([\d.]+)/)[1];// ie 版本号
else if (document.getBoxObjectFor)
	Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];// firefox 版本号
else if (window.MessageEvent && !document.getBoxObjectFor)
	Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];// chrome 版本号
else if (window.opera)
	Sys.opera = ua.match(/opera.([\d.]+)/)[1];// opera 版本号
else if (window.openDatabase)
	Sys.safari = ua.match(/version\/([\d.]+)/)[1];// safari 版本号
/**
 * 
 * @param url
 * @param title
 * @param left
 * @param top
 * @param width
 * @param height
 * @returns
 */
function _winOpen(url, title, left, top, width, height) {
	var winoption = "left = "
			+ left
			+ ", top = "
			+ top
			+ ", height = "
			+ height
			+ ", width = "
			+ width
			+ ", toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes";
	var tmp = window.open(url, title, winoption);
	return tmp;
}
/**
 * 弹出满屏窗体
 * 
 * @param strURL
 * @returns
 */
function winOpenFullScreen(url, title) {
	var _h = Sys.system > 6.0 ? 41 : 30;
	var _w = Sys.system > 6.0 ? 20 : 10;
	if (Sys.chrome) {
		_h += 26;
		_w -= 5;
	}
	var sheight = screen.availHeight - _h;
	var swidth = screen.availWidth - _w;
	var tmp = _winOpen(url, title, 0, 0, swidth, sheight);
	return tmp;
}

/**
 * 弹出居中窗体
 * 
 * @param url
 * @param title
 * @param iWidth
 * @param iHeight
 */
function openwindow(url, title, iWidth, iHeight) {
	var iTop = (window.screen.availHeight - (Sys.system > 6.0 ? 41 : 30) - iHeight) / 2;
	var iLeft = (window.screen.availWidth - (Sys.system > 6.0 ? 20 : 10) - iWidth) / 2;
	window
			.open(
					url,
					title,
					'height='
							+ iHeight
							+ ',width='
							+ iWidth
							+ ',top='
							+ iTop
							+ ',left='
							+ iLeft
							+ ',toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=yes');
}
