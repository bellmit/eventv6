;(function($, doc, win) {
	$.extend({
		log: function(message, color) {
			if (typeof color == 'undefined') color = 'green';
			var _console = $("#_consoleDiv");
			if (_console.length == 0) {
				_console = $("<div id='_consoleDiv' style='height:auto;width:auto;background-color:black;color:green;font-size:14px;'><b>日志：</b><br/></div>");
				$('body').append(_console);
			}
			var now = new Date(),
				y = now.getFullYear(),
				m = now.getMonth() + 1,
				d = now.getDate(),
				h = now.getHours(),
				min = now.getMinutes(),
				s = now.getSeconds(),
				time = y + '/' + m + '/' + d + ' ' + h + ':' + min + ':' + s;
			_console.append('<div style="padding:3px;color:'+color+'"><b>' + time + ' -->: ' + message + '</b></div>');
		}
	});
})(jQuery, document, window);