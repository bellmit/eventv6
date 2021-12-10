 
function inputScript(filePath) {
    var script = '<' + 'script type="text/javascript" src="' + filePath + '"' + '><' + '/script>';
    document.writeln(script);
}
inputScript("../lib/jquery-1.11.1.js");
inputScript("../lib/json2.js");
inputScript("../lib/jquery.cookie.js");
inputScript("../lib/jQuery.md5.js");

inputScript("../lib/spgis.min.js");

