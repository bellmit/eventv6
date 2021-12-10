
;(function(exports){
    var doc = exports.document,
        a = {},
        expose = +new Date(),
        rExtractUri = /((?:http|https|file):\/\/.*?\/[^:]+)(?::\d+)?:\d+/,
        isLtIE8 = ('' + doc.querySelector).indexOf('[native code]') === -1;
    exports.getCurrAbsPath = function(){
        // FF,Chrome
        if (doc.currentScript){
            return doc.currentScript.src;
        }

        var stack;
        try{
            a.b();
        }
        catch(e){
            stack = e.fileName || e.sourceURL || e.stack || e.stacktrace;
        }
        // IE10
        if (stack){
            var absPath = rExtractUri.exec(stack)[1];
            if (absPath){
                return absPath;
            }
        }

        // IE5-9
        for(var scripts = doc.scripts,
                i = scripts.length - 1,
                script; script = scripts[i--];){
            if (script.className !== expose && script.readyState === 'interactive'){
                script.className = expose;
                // if less than ie 8, must get abs path by getAttribute(src, 4)
                return isLtIE8 ? script.getAttribute('src', 4) : script.src;
            }
        }
    };
}(window));


/***
 * Messenger =======================================================================================================
 */
window.GmMessenger = (function(){

    // 消息前缀, 建议使用自己的项目名, 避免多项目之间的冲突
    // !注意 消息前缀应使用字符串类型
    var Gm_prefix = "[PROJECT_NAME]",
        supportPostMessage = 'postMessage' in window;

    // GmTarget 类, 消息对象
    function GmTarget(target, name){
        var errMsg = '';
        if(arguments.length < 2){
            errMsg = 'target error - target and name are both requied';
        } else if (typeof target != 'object'){
            errMsg = 'target error - target itself must be window object';
        } else if (typeof name != 'string'){
            errMsg = 'target error - target name must be string type';
        }
        if(errMsg){
            throw new Error(errMsg);
        }
        this.target = target;
        this.name = name;
    }

    // 往 target 发送消息, 出于安全考虑, 发送消息会带上前缀
    if ( supportPostMessage ){
        // IE8+ 以及现代浏览器支持
        GmTarget.prototype.send = function(msg){
            this.target.postMessage(Gm_prefix + msg, '*');
        };
    } else {
        // 兼容IE 6/7
        GmTarget.prototype.send = function(msg){
            var targetFunc = window.navigator[Gm_prefix + this.name];
            if ( typeof targetFunc == 'function' ) {
                targetFunc(Gm_prefix + msg, window);
            } else {
                throw new Error("target callback function is not defined");
            }
        };
    }

    // 信使类
    // 创建GmMessenger实例时指定, 必须指定GmMessenger的名字, (可选)指定项目名, 以避免Mashup类应用中的冲突
    // !注意: 父子页面中projectName必须保持一致, 否则无法匹配
    function GmMessenger(messengerName, projectName){
        this.targets = {};
        this.name = messengerName;
        this.listenFunc = [];
        Gm_prefix = projectName || Gm_prefix;
        if(typeof Gm_prefix !== 'string') {
            Gm_prefix = Gm_prefix.toString();
        }
        this.initListen();
    }

    // 添加一个消息对象
    GmMessenger.prototype.addTarget = function(target, name){
        var targetObj = new GmTarget(target, name);
        this.targets[name] = targetObj;
    };

    //移除一个消息对象
    GmMessenger.prototype.remove = function(name){
        var targetObj = this.targets[name];
        if(targetObj){
            targetObj.target=null;
            targetObj.name=null;
            try{
                delete this.targets[name]
            }catch(e){}
        }
    };

    // 初始化消息监听
    GmMessenger.prototype.initListen = function(){
        var self = this;
        var generalCallback = function(msg){
            if(typeof msg == 'object' && msg.data){
                msg = msg.data;
            }
            // 剥离消息前缀
            if(msg.indexOf && msg.indexOf(Gm_prefix)>-1){
                msg = msg.slice(Gm_prefix.length);
                for(var i = 0; i < self.listenFunc.length; i++){
                    self.listenFunc[i](msg);
                }
            }
        };

        if ( supportPostMessage ){
            if ( 'addEventListener' in document ) {
                window.addEventListener('message', generalCallback, false);
            } else if ( 'attachEvent' in document ) {
                window.attachEvent('onmessage', generalCallback);
            }
        } else {
            // 兼容IE 6/7
            window.navigator[Gm_prefix + this.name] = generalCallback;
        }
    };

    // 监听消息
    GmMessenger.prototype.listen = function(callback){
        this.listenFunc.push(callback);
    };
    // 注销监听
    GmMessenger.prototype.clear = function(){
        this.listenFunc = [];
    };
    // 广播消息
    GmMessenger.prototype.send = function(msg){
        var targets = this.targets,target;
        for(target in targets){
            if(targets.hasOwnProperty(target)){
                targets[target].send(msg);
            }
        }
    };

    return GmMessenger;
})();


/***
 * JSON2.js =======================================================================================
 */
if (typeof JSON !== 'object') {
    JSON = {};
}

(function () {
    'use strict';

    var rx_one = /^[\],:{}\s]*$/,
        rx_two = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
        rx_three = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
        rx_four = /(?:^|:|,)(?:\s*\[)+/g,
        rx_escapable = /[\\\"\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        rx_dangerous = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10
            ? '0' + n
            : n;
    }

    function this_value() {
        return this.valueOf();
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function () {

            return isFinite(this.valueOf())
                ? this.getUTCFullYear() + '-' +
            f(this.getUTCMonth() + 1) + '-' +
            f(this.getUTCDate()) + 'T' +
            f(this.getUTCHours()) + ':' +
            f(this.getUTCMinutes()) + ':' +
            f(this.getUTCSeconds()) + 'Z'
                : null;
        };

        Boolean.prototype.toJSON = this_value;
        Number.prototype.toJSON = this_value;
        String.prototype.toJSON = this_value;
    }

    var gap,
        indent,
        meta,
        rep;


    function quote(string) {

        rx_escapable.lastIndex = 0;
        return rx_escapable.test(string)
            ? '"' + string.replace(rx_escapable, function (a) {
            var c = meta[a];
            return typeof c === 'string'
                ? c
                : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
        }) + '"'
            : '"' + string + '"';
    }


    function str(key, holder) {

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = gap,
            partial,
            value = holder[key];

        if (value && typeof value === 'object' &&
            typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }


        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

        switch (typeof value) {
            case 'string':
                return quote(value);

            case 'number':

                return isFinite(value)
                    ? String(value)
                    : 'null';

            case 'boolean':
            case 'null':

                return String(value);

            case 'object':

                if (!value) {
                    return 'null';
                }

                gap += indent;
                partial = [];

                if (Object.prototype.toString.apply(value) === '[object Array]') {

                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }

                    v = partial.length === 0
                        ? '[]'
                        : gap
                        ? '[\n' + gap + partial.join(',\n' + gap) + '\n' + mind + ']'
                        : '[' + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }

                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        if (typeof rep[i] === 'string') {
                            k = rep[i];
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (
                                    gap
                                        ? ': '
                                        : ':'
                                ) + v);
                            }
                        }
                    }
                } else {

                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (
                                    gap
                                        ? ': '
                                        : ':'
                                ) + v);
                            }
                        }
                    }
                }

                v = partial.length === 0
                    ? '{}'
                    : gap
                    ? '{\n' + gap + partial.join(',\n' + gap) + '\n' + mind + '}'
                    : '{' + partial.join(',') + '}';
                gap = mind;
                return v;
        }
    }

    if (typeof JSON.stringify !== 'function') {
        meta = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"': '\\"',
            '\\': '\\\\'
        };
        JSON.stringify = function (value, replacer, space) {

            var i;
            gap = '';
            indent = '';

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

            } else if (typeof space === 'string') {
                indent = space;
            }

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                (typeof replacer !== 'object' ||
                typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }

            return str('', {'': value});
        };
    }


    if (typeof JSON.parse !== 'function') {
        JSON.parse = function (text, reviver) {

            var j;

            function walk(holder, key) {

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }

            text = String(text);
            rx_dangerous.lastIndex = 0;
            if (rx_dangerous.test(text)) {
                text = text.replace(rx_dangerous, function (a) {
                    return '\\u' +
                        ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

            if (
                rx_one.test(
                    text
                        .replace(rx_two, '@')
                        .replace(rx_three, ']')
                        .replace(rx_four, '')
                )
            ) {

                j = eval('(' + text + ')');


                return typeof reviver === 'function'
                    ? walk({'': j}, '')
                    : j;
            }

            throw new SyntaxError('JSON.parse');
        };
    }
}());


var APPNAME_COMBOSELECTOR = "GM_MESSAGER_COMBO_SELECTOR";
var PRE_IFRAME = "iframe_";
function getGmMessageForComboSelector(messengerName) {
    if(window[APPNAME_COMBOSELECTOR]==undefined || window[APPNAME_COMBOSELECTOR]==null) {
        window[APPNAME_COMBOSELECTOR] = new GmMessenger(messengerName, APPNAME_COMBOSELECTOR);
    }
    return window[APPNAME_COMBOSELECTOR];
}

//去掉属性值为null的属性
function getCleanObject(obj){
    if(obj){
        if (Object.prototype.toString.apply(obj) === '[object Array]') {
            var newObjs = [];
            for (var i = 0, l = obj.length; i < l; i++) {
                if(obj[i]){
                    var newObj = {};
                    for (k in obj[i]) {
                        if(obj[i][k]!=null) newObj[k] = obj[i][k];
                    }
                    newObjs.push(newObj);
                }else{
                    newObjs.push(obj[i]);
                }
            }
            return newObjs;

        }else if(typeof(obj)=="object"){
            var newObj = {};
            for (k in obj) {
                if(obj[k]!=null) newObj[k] = obj[k];
            }
            return newObj
        }
    }
    return obj
}

function comboselectorMessageFormat(cid, fun, params){
    var msgParams = params; //getCleanObject(params);
    var paramstr = msgParams ? JSON.stringify(msgParams): "null";
    return '{"cid":"'+cid+'", "fun":"'+fun+'", "params":'+paramstr+'}';
}

function fireComboselectorAction(fun, parmas, obj, multiple){
    if(typeof(fun)=="function"){
        try{
            if(Object.prototype.toString.call(parmas) === '[object Array]'){
                var curParams;
                if(multiple){
                    curParams = parmas;
                }else{
                    curParams = [parmas];
                }

                if(obj){
                    if(curParams.length==0){
                        curParams = [parmas];
                    }
                    return fun.apply(obj, curParams);

                }else{
                    return fun(parmas);
                }
            }else{
                if(obj){
                    return fun.apply(obj, [parmas]);
                }else{
                    return fun(parmas);
                }
            }

        }catch(e){
            alert("执行方法错误：\n[" + fun +"]\n参数："+parmas + "\n错误信息："+e); return;
        }
    }
}

function stringEquals(a, b){
    if(a==b || (a+"")==(b+"")) return true;

    return false;
}

function getMyArrayIndex(arr, item, compareString, attr){
    for(var i=0, l=arr.length; i<l; i++){
        if(attr){
            if(arr[i][attr]==item || (compareString && stringEquals(arr[i][attr], item))) return i;
        }else{
            if(arr[i]==item || (compareString && stringEquals(arr[i][attr], item))) return i;
        }
    }
    return -1;
}

function getValidStrWithTrim(val){
    if(val==undefined || val==null) return "";

    return (val+"").replace(/(^\s*)|(\s*$)/g, "");
}
function getSubStringWithEllipsis(val, length){
    var sText = getValidStrWithTrim(val);
    if(sText.length>5){
        sText = sText.substr(0,5)+"..";
    }
    return sText
}




