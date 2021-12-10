/*
 * CmsTpl:
 * 用于模板生成的页面
 */

if ( ! window.Br ) {
	var Br = {}, u = navigator.userAgent;
	Br.ie = navigator.appName == 'Microsoft Internet Explorer' ? true : false;
	Br.fox = /firefox/i.test( u );
	Br.ajax_type = ( function() {
		if ( window.XMLHttpRequest ) {
			try {
				var o = new XMLHttpRequest(), o = null;	return 1;
			} catch (e) {}
		}
		try {
			var a = 'MSXML2.XMLHTTP', o = new ActiveXObject( a ), o = null; return a;
		} catch ( e ) {}
		try {
			var a = 'Microsoft.XMLHTTP',  o = new ActiveXObject( a ), o = null;	return a;
		} catch ( e ) {}
		alert( ' Cannot create XMLHTTP object! ' );
	} )();

	if (!window.Ajax) window.Ajax = {reqs:[],getreq:function(){if(Br.ie||Br.fox){for(var i=0,l=Ajax.reqs.length;i<l;i++){if(!Ajax.reqs[i].obj&&!Ajax.reqs[i].eX&&!Ajax.reqs[i].loading)break;}if(i==l)i=Ajax.reqs.push({req:Br.ajax_type==1?new XMLHttpRequest():new ActiveXObject(Br.ajax_type)})-1;Ajax.reqs[i].loading=true;return Ajax.reqs[i];}else{return{req:Br.ajax_type==1?new XMLHttpRequest():new ActiveXObject(Br.ajax_type)};}},
		Z:function(a,b){if(b&&a.req.readyState<=1)a.eX=b;if(a.sync){CmsTpl.remove(Ajax.reqs,a);}delete a.req.onreadystatechange;a.obj=a.loading=null;},
		aF:function(a){for(var i=0,l=Ajax.reqs.length;i<l;i++){if(Ajax.reqs[i].obj==a){this.Z(Ajax.reqs[i],true);}}},cntp:'application/x-www-form-urlencoded',ifmod:'Thu, 01 Jan 1970 00:00:00 GMT',
		eB:function(a,b,c,d,e,f,g,h,i,j){var u=a;if(c&&typeof c=='object'){var s=[];for(var i in c){s.push(i+'='+CmsTpl.url_esc(c[i]));}c=s.join('&');}if((a.length>350&&a.indexOf('?')>0)){c=(c?c+'&':'')+CmsTpl.s_fr(a,'?');u=CmsTpl.s_to(a,'?');}if(!c)c=null;var k=Ajax.getreq(),l=k.req;if(h)k.obj=h;if(d)k.sync=d;l.open(c?'POST':'GET',(u.charAt(0)=='/'||u.indexOf('http://')==0||u.indexOf('https://')==0)?u:Cfg.path+u,!d);
			if(c)l.setRequestHeader('Content-Type',Ajax.cntp);if(d)l.setRequestHeader('If-Modified-Since',Ajax.ifmod);function onchange(){if(l.readyState==4){if(l.status==0)return;var m=false;if(l.status<400){if(g){if(l.responseXML.documentElement!=null){if('setProperty' in l.responseXML)l.responseXML.setProperty('SelectionLanguage','XPath');m=l.responseXML.documentElement;}else{if(Cfg.debug){
				var ex=l.responseXML.parseError;debugger;}}}else m=l.responseText;}if(m===false)Ajax.iM(a,f,l);Ajax.Z(k,true);if(b){if(j==null)b.call(i,m);else b.apply(i,[m].concat(j));}if(Br.ie||Br.fox){b=c=d=e=f=g=h=m=null;}}};l.onreadystatechange=onchange;if(d){l.send(c);}else{setTimeout(function(){if(k.eX){Ajax.Z(k);k.eX=null;}else{k.req.send(c)}},10);}if(d&&Br.fox)onchange();return k;},
		send:function(a,b,c,d,e,f){this.eB(a,b,null,e,true,f,null,null,c,d);},sendx:function(a,b,c,d,e,f){this.eB(a,b,null,e,true,f,true,null,c,d);},iM:function(a,b,c){if(typeof b=='function'){return b(c,a);}if(b!==true){if(Cfg.debug){var t=c.status,d='ajax error '+t+': '+a+'\n\n',e=c.responseText;if(t>=500&&t<=600){DFish.confirm(d+Loc.debug.view_more,function(){$.wr(window.open(),e)});}
			else if(Loc.httpcode[t])DFish.alert(d+Loc.httpcode[t]);else alert(d+e);}else DFish.alert(Loc.internet_error);}},scripts:{},loadjs:function(a,b,c,d,e,f){if(b&&typeof b!='function')b=Fn.JS.F(b);if(typeof a=='object'){var f=a.length,g=[];if(f>1){CmsTpl.each(a,function(v,i){Ajax.loadjs(v,function(){if(Ajax.scripts[v].loaded)f--;if(!f){if(g.length){for(var j=0;j<g.length;j++){
				if(!!g[j])window.execScript(g[j]);}}if(b)b();}},c,g,i)});return;}else{a=a[0];}}if(Ajax.scripts[a]&&Ajax.scripts[a].loaded){if(b)b();Ajax.scripts[a].fireEvent('load');return;}if(!Ajax.scripts[a]){Ajax.scripts[a]=new Fn.Event();}if(Ajax.scripts[a].loading){if(b)Ajax.scripts[a].addEventOnce('load',b);return;}Ajax.scripts[a].loading=true;Ajax.scripts[a].loaded=false;
					Ajax.send(CmsTpl.url_set(a,'v',Cfg.ver),function(s){if(s===false)return;if(d)d[e]=s;else if(s)window.execScript(s);Ajax.scripts[a].text=s;Ajax.scripts[a].loading=false;Ajax.scripts[a].loaded=true;if(b){b()}Ajax.scripts[a].fireEvent('load');},null,null,c);},sends:function(a,b,c,d,e){var f=a.length,g=new Array(f);for(var i=0;i<a.length;i++){(function(k){Ajax.send(a[k],function(s){g[k]=s;
						if(--f==0)b.call(c,g);},c,d,e);})(i);}},
		sendxs:function(a,b,c,d,e,f,ch){var r=ch?CmsTpl.unique(a):a,n=r.length,g=new Array(a.length),h={};for(var i=0;i<r.length;i++){(function(k){Ajax.sendx(r[k],function(s){if(f)h[r[k]]=s;else g[k]=s;if(--n==0){if(ch){for(var j=0;j<a.length;j++)g[j]=h[a[j]];}b.call(c,g);}},c,d,e,f);})(i);}},require:function(a,b){this.loadjs(a,b,true);}}

}

//if ( ! Br.ie ) document.write( '<script src=' + Cfg.path + 'js/ieemu.js></script>' );


var CmsTpl = {
	
	ww : window,
	
	editmode : false,
	
	m : function( a ) {
		if ( typeof a == 'number' )
			return a;
		a = parseFloat( a );
		return isNaN( a ) ? 0 : a;
	},
	remove : function( a, b ) {
		if ( a )
	    	for ( var i = 0, l = a.length; i < l; i ++ )
	    		if ( a[ i ] == b ) {
	    			a.splice( i, 1 ); break;
	    		}
	    return a;
	},
	each : function( x, a, b, c ) {
		var i = 0, l = x.length, m = c ? 'apply' : 'call';
		if ( ! l ) return x;
		if ( b ) for ( i = l - 1; i >= 0; i -- ) { a[ m ]( null, x[ i ], i ) }
		else for ( ; i < l; i ++ ) { a[ m ]( x, x[ i ], i ) }
		return x;
	},
	unique : function( a ) {
		var b = [];
		for ( var i = 0; i < a.length; i ++ )
			if ( js.index( b, a[ i ] ) == -1 ) b.push( a[ i ] );
		return b;
	},
	s_up : function( a ) {
		return a.charAt( 0 ).toUpperCase() + a.substr( 1 );
	},
	s_fr : function( a, b, c ) {
		var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
		return d == -1 ? '' : a.substr( d + b.length );
	},
	// 在a中取以b结束的字符串(不包括b) /@ c -> last indexOf ?
	s_to : function( a, b, c ) {
		var d = c ? a.lastIndexOf( b ) : a.indexOf( b );
		return d == -1 ? '' : a.substr( 0, d );
	},
	esc : function( s ) {
		return encodeURIComponent( s );
	},
	url : function( a ) {
		return a ? ( a.indexOf( '/' ) == 0 || a.indexOf( 'http://' ) == 0 || a.indexOf( 'https://' ) == 0 ? '' : Cfg.path ) + a : '';
	},	
	absurl : function( a ) {
		a = this.url( a );
		if ( a && a.indexOf( '/' ) == 0 ) {
			var b = document.location;
			a = b.protocol + '//' + b.host + a;
		}
		return a;
	},
	// 设置URL参数  /@ a->url, b->name, c->value
	url_set : function( a, b, c ) {
		if ( b == '#' )
			return /#.*/.test( a ) ? a.replace( /#.*/, '#' + c ) : a + '#' + c;
		if ( arguments.length > 3 ) {
			for ( var i = 1, s = a; i < arguments.length; i += 2 )
				s = arguments.callee( s, arguments[ i ], arguments[ i + 1 ] );
			return s;
		} else {
			var d = RegExp( '\\b' + b + '=[^&]*' );
			return d.test( a ) ? a.replace( d, b + '=' + c ) : a + ( a.indexOf( '?' ) == -1 ? '?' : '&' ) + b + '=' + c;
		}
	},
	url_esc : function( a ) {
		return a == null ? '' : encodeURIComponent( a )
	},
	url_unesc : function( a ) {
		return decodeURIComponent( a.replace( /\+/g, ' ' ) )
	},
	eid : function( a, b ) {
		var c = Math.random(),
			d = String( c );
		return ( a || '' ) + Math.floor( c * 9 + 1 ) + d.slice( 2, b ? b + 1 : d.length );
	},
		
	$ : function( a ) {
		return this.ww.document.getElementById( a ) || document.getElementById( a );
	},
	
	x : function( a, b ) {
		return a.selectSingleNode( b );
	},
	
	tags : function( a, b ) {
		return ( b || this.ww.document ).getElementsByTagName( a );
	},
	
	dc : function( a ) {
		return a ? ( a.ownerDocument || o.document ) : this.ww.document;
	},

	win : function( a ) {
		return a ? this.dc( a )[ Br.ie ? 'parentWindow' : 'defaultView' ] : this.ww;
	},

	parseJSON : function( a, b ) {
		return this.JSON.parse( a, b );
	},
	
	css : function( a, v ) {
		if ( typeof a == 'string' )
			a = this.$( a );
		if ( ! a || ! v )
			return a;
		var ar;
		if ( typeof v == 'string' ) {
			ar = v.replace( /-(\w)/g, function( $0,$1 ) { return $1.toUpperCase() } ).split( /:(?!\/)|;/ );
		} else {
			ar = [];
			for ( var i in v ) ar.push( i, v[ i ] );
		}
		for ( var i = 0; i < ar.length; i += 2 ) {
			var b = ar[ i ], d = ar[ i + 1 ], c;
			if ( d == null )
				continue;
			if ( b.charAt( 0 ) == '+' ) {
				if ( d ) {
					b = b.substr( 1 );
					c = this.m( a.style[ b ] );
					d = this.m( d );
					if ( ( b == 'width' || b == 'height' ) ) {
						if ( a.style[ b ] == '' )
							c = this.m( $[ b == 'width' ? 'boxwd' : 'boxht' ]( a, a[ 'offset' + this.s_up( b ) ] ) );
						if ( c < -d )
							c = -d;
					}
					a.style[ b ] = ( c + d ) + 'px';
				}
			} else {
				if ( b == 'float' )
					b = Br.ie ? 'styleFloat' : 'cssFloat';
				if ( b == 'background' )
				if ( b == 'width' || b == 'height' )
					d = Math.max( this.m( d ), 0 );
				if ( typeof d == 'number' && b != 'zIndex' )
					d += 'px';
				a.style[ b ] = d;
			}
		}
		return a;
	},

	pf : function() {
		return this.editmode ? 'PL.Draw' : 'CmsTpl';
	},
		
	over_tab : function( a, o ) {
		var w = this.win( a ),
			b = this.tags( 'li', a.parentNode ),
			c = this.$( a.parentNode.id + '-page' );
		c = this.editmode || this.previewmode ? c.children[ 0 ].children : c.children;
		for ( var i = 0, d, e, f, g, tmp; i < b.length; i ++ ) {
			d = b[ i ].contains( o || w.event.srcElement || w.event.target );
			e = b[ i ];
			f = e.getAttribute( 'runs' );
			f = f && this.parseJSON( f );
			e.className = d ? 'on' : '';
			if ( f ) {
				g = this.tabcss( f, d );
				tmp = this.css( e.firstChild, g.al );
				tmp = this.css( tmp.firstChild, g.ar );
				tmp = this.css( tmp.firstChild, g.ac );
				tmp = this.css( tmp.firstChild, g.ft );
			}
			c[ i ].style.display = d ? 'block' : 'none';
		}
	},
	
	out_tab : function() {
		
	},
	
	// @a -> feature, b -> focus?
	tabcss : function( a, b ) {
		var e = a.runs,
			sal = '', sar = '', sac = e && e.w ? 'width:' + e.w + 'px;padding-left:0;padding-right:0;' : '',
			sft = '';
		if ( e ) {
			if ( b ) {
				if ( e.onft == 1 ) {
					sft = 'font-size:' + e.onfts + ';' + ( e.onftc ? 'color:' + e.onftc + ';' : '' ) + 'font-weight:' + ( e.onftw ? 'bold;' : 'normal;' );
				}
				if ( e.onbg == 1 ) {
					sal = 'background:' + ( e.onbgi ? 'url(' + this.url( e.onbgi ) + ') ' : 'none ' ) + ( e.onbgc || '' ) + ' ' + e.onbgr + ' ' + e.onbgpx + 'px ' + e.onbgpy + 'px;';
					sar = 'background:none;';
					sac += 'background:none;';
				}
			} else {
				if ( e.ft == 1 ) {
					sft = 'font-size:' + e.fts + ';' + ( e.ftc ? 'color:' + e.ftc + ';' : '' ) + 'font-weight:' + ( e.onftw ? 'bold;' : 'normal;' );
				}
				if ( e.bg == 1 ) {
					sal = 'background:' + ( e.onbgi ? 'url(' + this.url( e.onbgi ) + ') ' : 'none ' ) + ( e.bgc || '' ) + ' ' + e.bgr + ' ' + e.bgpx + 'px ' + e.bgpy + 'px;';
					sar = 'background:none;';
					sac += 'background:none;';
				}
			}
		}
		return e && { al : sal, ar : sar, ac : sac, ft : sft };
	},
	
	// @a -> feature, b -> focus?, c -> last?, d -> proxy?, m -> 
	s_tab : function( a, b, c, d, m ) {
		var e = d ? 'parent.CmsTpl' : ( m || this.pf() ),
			f = a.lk && a.lk != '#' && a.lk.indexOf( 'javascript:' ) == -1 ? a.lk : '';
		return '<li' + ( b ? ' class="on"' : '' ) + ( a.id ? ' id="' + a.id + '"' : '' ) + ' onmouseover=' + e + '.over_tab(this) onmouseout=' + e + '.out_tab(this)' + ( a.runs && this.editmode ? ' runs=\'' + js.toJSONString( a ) + '\'' : '' ) + '><div class="tl"><div class="tr' + ( c ? ' tr1': '' ) +
			'"><div class="tc"' + ( a.dataId ? ' dataId="' + a.dataId + '"' : '' ) + '>' + ( f ? '<a href="' + a.lk + '" target=_blank>' + a.n + '</a>' : a.n ) + '</div></div></div></li>';
	},
	
	modMarqueeLoad : function( x ) {
		var e  = CmsTpl.$( x.id ),
			e1 = CmsTpl.$( x.id + '-1' ),
			e2 = e1 && e1.parentNode.insertCell();
		if ( ! e ) return;
		e2.innerHTML = e1.innerHTML;
		function Marquee() {
			if ( e2.offsetWidth <= e.scrollLeft )
				e.scrollLeft -= e1.offsetWidth;
			else
				e.scrollLeft ++;
		}
		var sp = CmsTpl.m( x.speed ),
			ma = setInterval( Marquee, sp );
		e.onmouseover = function() { clearInterval( ma ) };
		e.onmouseout  = function() { ma = setInterval( Marquee, sp ) };
	},
	
	s_printf : function( s ) {
		if ( s ) {
			var a = arguments;
			return s.replace( /\$(\d+)/g, function( b, c ) { return a[ parseFloat( c ) + 1 ] } );
		}
	},
	
	swf : function( a, b, c, d, e ) {
		var hc={};if(typeof hc.util=="undefined"){hc.util={};}if(typeof hc.ha=="undefined"){hc.ha={};}hc.gZ=function(_1,id,w,h,_5,c,_7,_8,_9,_a){this.DETECT_KEY=_a?_a:"detectflash";this.skipDetect=hc.util.hh(this.DETECT_KEY);this.hn={};this.hq={};this.hb=new Array();if(_1){this.ho("swf",_1);}if(id){this.ho("id",id);}if(w){this.ho("width",w);}if(h){this.ho("height",h);}if(_5){this.ho("version",new hc.gY(_5.toString().split(".")));}this.hm=hc.ha.hg();if(!window.opera&&document.all&&this.hm.major>7){hc.gZ.doPrepUnload=true;}if(c){this.addParam("bgcolor",c);}var q=_7?_7:"high";
		this.addParam("quality",q);this.ho("hp",false);this.ho("doExpressInstall",false);var _c=(_8)?_8:window.location;this.ho("xiRedirectUrl",_c);this.ho("redirectUrl","");if(_9){this.ho("redirectUrl",_9);}};hc.gZ.prototype={hp:function(_d){this.hv=!_d?"expressinstall.swf":_d;this.ho("hp",true);},ho:function(_e,_f){this.hb[_e]=_f;},he:function(_10){return this.hb[_10];},addParam:function(_11,_12){this.hn[_11]=_12;},hf:function(){return this.hn;},addVariable:function(_13,_14){this.hq[_13]=_14;},getVariable:function(_15){return this.hq[_15];},hk:function(){return this.hq;},
		hj:function(){var _16=new Array();var key;var _18=this.hk();for(key in _18){_16[_16.length]=key+"="+String(_18[key]).replace(/"/g,'&quot;');;}return _16;},getSWFHTML:function(){var _19="";if(navigator.plugins&&navigator.mimeTypes&&navigator.mimeTypes.length){if(this.he("doExpressInstall")){this.addVariable("MMplayerType","PlugIn");this.ho("swf",this.hv);}_19="<embed type=\"application/x-shockwave-flash\" src=\""+this.he("swf")+"\" width=\""+this.he("width")+"\" height=\""+this.he("height")+"\" style=\""+this.he("style")+"\"";_19+=" id=\""+this.he("id")+"\" name=\""+this.he("id")+"\" ";
		var _1a=this.hf();for(var key in _1a){_19+=[key]+"=\""+_1a[key]+"\" ";}var _1c=this.hj().join("&");if(_1c.length>0){_19+="flashvars=\""+_1c+"\"";}_19+="/>";}else{if(this.he("doExpressInstall")){this.addVariable("MMplayerType","ActiveX");this.ho("swf",this.hv);}_19="<object id=\""+this.he("id")+"\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" width=\""+this.he("width")+"\" height=\""+this.he("height")+"\" style=\""+this.he("style")+"\">";_19+="<param name=\"movie\" value=\""+this.he("swf")+"\" />";var _1d=this.hf();for(var key in _1d){
		_19+="<param name=\""+key+"\" value=\""+_1d[key]+"\" />";}var _1f=this.hj().join("&");if(_1f.length>0){_19+="<param name=\"flashvars\" value=\""+_1f+"\" />";}_19+="</object>";}return _19;},write:function(_20){if(this.he("hp")){var _21=new hc.gY([6,0,65]);if(this.hm.hu(_21)&&!this.hm.hu(this.he("version"))){this.ho("doExpressInstall",true);this.addVariable("MMredirectURL",escape(this.he("xiRedirectUrl")));document.title=document.title.slice(0,47)+" - Flash Player Installation";this.addVariable("MMdoctitle",document.title);}}
		if(this.skipDetect||this.he("doExpressInstall")||this.hm.hu(this.he("version"))){var n=(typeof _20=="string")?document.getElementById(_20):_20;n.innerHTML=this.getSWFHTML();return true;}else{if(this.he("redirectUrl")!=""){document.location.replace(this.he("redirectUrl"));}}return false;}};hc.ha.hg=function(){var _23=new hc.gY([0,0,0]);if(navigator.plugins&&navigator.mimeTypes.length){var x=navigator.plugins["Shockwave Flash"];if(x&&x.description){_23=new hc.gY(x.description.replace(/([a-zA-Z]|\s)+/,"").replace(/(\s+r|\s+b[0-9]+)/,".").split("."));}}else{
		if(navigator.userAgent&&navigator.userAgent.indexOf("Windows CE")>=0){var axo=1;var _26=3;while(axo){try{_26++;axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash."+_26);_23=new hc.gY([_26,0,0]);}catch(e){axo=null;}}}else{try{var axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.7");}catch(e){try{var axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash.6");_23=new hc.gY([6,0,21]);axo.AllowScriptAccess="always";}catch(e){if(_23.major==6){return _23;}}try{axo=new ActiveXObject("ShockwaveFlash.ShockwaveFlash");}catch(e){}}if(axo!=null){
		_23=new hc.gY(axo.GetVariable("$version").split(" ")[1].split(","));}}}return _23;};hc.gY=function(_29){this.major=_29[0]!=null?parseInt(_29[0]):0;this.minor=_29[1]!=null?parseInt(_29[1]):0;this.rev=_29[2]!=null?parseInt(_29[2]):0;};hc.gY.prototype.hu=function(fv){if(this.major<fv.major){return false;}if(this.major>fv.major){return true;}if(this.minor<fv.minor){return false;}if(this.minor>fv.minor){return true;}if(this.rev<fv.rev){return false;}return true;};hc.util={hh:function(_2b){var q=document.location.search||document.location.hash;if(_2b==null){return q;}
		if(q){var _2d=q.substring(1).split("&");for(var i=0;i<_2d.length;i++){if(_2d[i].substring(0,_2d[i].indexOf("="))==_2b){return _2d[i].substring((_2d[i].indexOf("=")+1));}}}return "";}};return new hc.gZ(a,b,c,d,e);	
	},
	
	JSON : (function(){var JSON={};function f(n){return n<10?'0'+n:n;};var cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,escapable=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,gap,indent,meta={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},rep;function quote(string){escapable.lastIndex=0;return escapable.test(string)?'"'+string.replace(escapable,function(a){var c=meta[a];
		return typeof c==='string'?c:'\\u'+('0000'+a.charCodeAt(0).toString(16)).slice(-4);})+'"':'"'+string+'"';};function str(key,holder){var i,k,v,length,mind=gap,partial,value=holder[key];if(value&&typeof value==='object'&&value instanceof Date){value=value.getUTCFullYear()+'-'+f(value.getUTCMonth()+1)+'-'+f(value.getUTCDate())+'T'+f(value.getUTCHours())+':'+f(value.getUTCMinutes())+':'+f(value.getUTCSeconds())+'Z';}if(typeof rep==='function'){value=rep.call(holder,key,value);}switch(typeof value){case 'string':return quote(value);case 'number':
		return isFinite(value)?String(value):'null';case 'boolean':case 'null':return String(value);case 'object':if(!value){return 'null';}gap+=indent;partial=[];if(Object.prototype.toString.apply(value)==='[object Array]'){length=value.length;for(i=0;i<length;i+=1){partial[i]=str(i,value)||'null';}v=partial.length===0?'[]':gap?'[\n'+gap+partial.join(',\n'+gap)+'\n'+mind+']':'['+partial.join(',')+']';gap=mind;return v;}if(rep&&typeof rep==='object'){length=rep.length;for(i=0;i<length;i+=1){k=rep[i];if(typeof k==='string'){v=str(k,value);if(v){partial.push(quote(k)+
		(gap?': ':':')+v);}}}}else{for(k in value){if(Object.hasOwnProperty.call(value,k)){v=str(k,value);if(v){partial.push(quote(k)+(gap?': ':':')+v);}}}}v=partial.length===0?'{}':gap?'{\n'+gap+partial.join(',\n'+gap)+'\n'+mind+'}':'{'+partial.join(',')+'}';gap=mind;return v;}};if(typeof JSON.stringify!=='function'){JSON.stringify=function(value,replacer,space){var i;gap='';indent='';if(typeof space==='number'){for(i=0;i<space;i+=1){indent+=' ';}}else if(typeof space==='string'){indent=space;}rep=replacer;if(replacer&&typeof replacer!=='function'&&
		(typeof replacer!=='object'||typeof replacer.length!=='number')){throw new Error('JSON.stringify');}return str('',{'':value});};}if(typeof JSON.parse!=='function'){JSON.parse=function(text,reviver){var j;function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==='object'){for(k in value){if(Object.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v;}else{delete value[k];}}}}return reviver.call(holder,key,value);};text=String(text);cx.lastIndex=0;if(cx.test(text)){text=text.replace(cx,function(a){return '\\u'+
		('0000'+a.charCodeAt(0).toString(16)).slice(-4);});}if(/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,'@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,']').replace(/(?:^|:|,)(?:\s*\[)+/g,''))){j=eval('('+text+')');return typeof reviver==='function'?walk({'':j},''):j;}throw new SyntaxError('JSON.parse');};}return JSON;}()),
	
	fontZoom : function( a ) {
		this.ww.document.body.style.fontSize = a + 'px';
	},
	// @ 翻页模块输出接口
	page : function( x ) {
		return this._page2( x );
	},
	_pgregtx : function( a ) {
		return '\\bclass=[\'"]?(?:[\\w ]+)?\\b' + a + '\\b(?:[\\w ]+)?[\'"]?';
	},
	_pgreg_cur : function( a ) {
		return RegExp( '(<\\w+ [^>]*' + this._pgregtx( a ) + '[^>]*>)\\d+' );
	},
	_pg_parse : function( a ) {
		var b = a.match( /<li\b[^>]*>[\s\S]+?(?=<\/?(?:ul|li)\b)/gi ),
			c = [ 'curpage', 'currecords', 'firstpage', 'endpage', 'first', 'end', 'pre', 'next', 'num', 'on', 'dots', 'skip' ],
			o = c.concat(),
			d = {};
		if ( b == null )
			return a;
		for ( var i = 0, e; i < b.length; i ++ ) {
			var e = b[ i ];
			for ( var j = 0; j < c.length; j ++ )
				if ( RegExp( this._pgregtx( c[ j ] ) ).test( e ) ) {
					d[ c[ j ] ] = e;
					c.splice( j, 1 );
					break;
				}
		}
		var e  = a.match( /^[\s\S]+?(?=<li)/i );
		d.head = e && e[ 0 ];
		e = a.match( /<\/ul>[\s\S]*?$/i );
		d.foot = e && e[ 0 ];
		o.sort( function( m, n ) { 
				var k = d[ m ] ? a.split( RegExp( CmsTpl._pgregtx( m ) ) )[ 0 ].length : 0,
					l = d[ n ] ? a.split( RegExp( CmsTpl._pgregtx( n ) ) )[ 0 ].length : 0;
				return k == l ? 0 : ( k < l ? -1 : 1 );
			 } );
		d.sort = o;
		if ( ! d.currecords ) d.currecords = '<li>第<span class="currecords">20</span>条/共<span class="sumrecords">200</span>条</li>';
		if ( ! d.curpage ) d.curpage = '<li>第<span class="curpage">3</span>页/共<span class="sumpages">10</span>页</li>';
		return d;
	},
	_page2 : function( x ) {
		x.num = this.m( x.num ); // 翻页码数
		var b  = this._pg_parse( x.encode == 1 ? CmsTpl.url_unesc( x.content ) : x.content ),
			s  = [ '<div ' + ( x[ 'class' ] ? 'class="' + x[ 'class' ] + '"' : '' ) + ' style="' + ( x.align == 'left' ? 'float:left' : ( x.align == 'right' ? 'float:right' : 'margin:0 auto' ) ) + ';' + ( x.fontSize ? 'font-size:' + x.fontSize + ';' : '' ) + ';">' ],
			d  = ' style="'  + ( x.color ? 'color:' + x.color + ';' : '' ) + '"',
			u  = x.url || '#$0', // url
			tr = this.m( x.totalResults ), // 总记录数
			si = this.m( x.startIndex ),   // 开始位置
			ip = this.m( x.itemsPerPage ), // 每页记录数
			ip = ip == 0 ? 1 : ip; // 除数不能为零
			cr = this.m( x.curRecords ),   // 当前记录数
			cp = Math.floor( si / ip ) + 1,   // 当前页
			xp = Math.ceil( tr / ip ), // 最大页数
			sp = Math.max( 1, cp - Math.floor( x.num / 2 ) ), // 起始页
			ep = sp + x.num - 1,
			pUrl = function( a, i ) {
				return a.replace( /(<a [^>]*\bhref=['"]?)([^> ]*?)(?=["']?[ >])/, '$1' + ( i == 1 && x.indexUrl ? x.indexUrl : CmsTpl.s_printf( u, i ) ) );
			},
			pNum = function( a, i ) {
				return a.replace( />\d+(?=<)?/, '>' + i );
			};

		if (this.m( x.extendParam) == 1){  // 是否继承URL参数
			u = u + window.location.href.replace(/^.*?(\?.*)$/, '$1').replace('$','%24');
		}

		if ( typeof b == 'string' )
			return s.join( '' ) + b + '</div>';
		if ( b.head )
			s.push( b.head );
		if ( xp - sp < x.num )
			sp = Math.max( 1, xp - x.num + 1 );

		var r = {};
		if ( x.showCurrecords == 1 && b.currecords ) {
			r.currecords = b.currecords.replace( this._pgreg_cur( 'currecords' ), '$1' + ( cr || Math.min( si + ip, xp ) ) )
				.replace( this._pgreg_cur( 'sumrecords' ), '$1' + tr );
		}
		if ( x.showCurpage == 1 && b.curpage ) {
			r.curpage = b.curpage.replace( this._pgreg_cur( 'curpage' ), '$1' + cp )
				.replace( this._pgreg_cur( 'sumpages' ), '$1' + Math.max( 1, xp ) );
		}
		if ( cp > 1 && b.firstpage && b.pre && b.first ) {
			if ( x.showFirst == 1 ) { // firstpage
				r.firstpage = pUrl( b.firstpage, 1 );
				r.pre = pUrl( b.pre, cp - 1 );
			} else if ( x.showFirst == 2 && sp > 1 ) { //first
				r.pre = pUrl( b.pre, cp - 1 );
				r.first = pUrl( b.first, 1 ) + ( b.dots || '' );
			}
		}
		if ( b.on && b.num ) {
			r.num = [];
			for ( var i = sp, l = Math.max( 1, Math.min( xp, sp + x.num - 1 ) ); i <= l; i ++ ) {
				r.num.push( pNum( i == cp ? b.on : pUrl( b.num, i ), i ) );
			}
		}
		if ( cp < xp && b.next && b.endpage && b.end ) {
			if ( x.showFirst == 1 ) { // endpage
				r.next = pUrl( b.next, cp + 1 );
				r.endpage = pUrl( b.endpage, xp );
			} else if ( x.showFirst == 2 && ep < xp ) { //end
				r.end  = ( b.dots || '' ) + pNum( pUrl( b.end, xp ), xp );
				r.next = pUrl( b.next, cp + 1 );
			}
		}
		if ( x.showSkip == 1 && b.skip ) {
			r.skip = ( b.skip.replace( /(type=\"?(?:submit|button)\"?)/i, '$1 onclick=return(CmsTpl.pageJump(this,"' + u + '"))' ) );
		}
		for ( var i = 0, e; i < b.sort.length; i ++ ) {
			if ( e = r[ b.sort[ i ] ] )
				s.push( typeof e == 'object' ? e.join( '' ) : e );
		}
		return s.join( '' ) + ( b.foot || '' ) + '</div>';
	},
	
	pageJump : function( a, u ) {
		var b = a.parentNode.getElementsByTagName( 'input' );
		for ( var i = 0, c; i < b.length; i ++ ) {
			c = b[ i ].type == 'text' && b[ i ].value.replace( /\s/gi, '' );
			if ( c && ! isNaN( c ) ) {
				window.location = CmsTpl.s_printf( u, c );
				break;
			}
		}
		return false;
	},
	
	// 视频模块输出接口 /@ a -> feature
	video : function( a ) {
		var b = a,
			d = [],
			f = this.swf( this.url( "fpd/swf/StrobeMediaPlayback.swf" ), b.id, b.width, b.height );
		for ( var i = 0, c = 'width,height,controlBarMode,controlBarAutoHide,postergrp,poster,playButtonOverlay,loop,autoPlay,bufferingOverlay'.split( ',' ), e; i < c.length; i ++ ) {
			e = b[ c[ i ] ];
			if ( e ) f.addVariable( c[ i ], this.esc( e ) );
		}
		f.addVariable( 'src', this.esc( this.absurl( b.url ) ) );
		f.addParam( "wmode", "opaque" );
		f.addParam( "AllowFullScreen", "true" );
		return '<div align=center>' + f.getSWFHTML() + '</div>';
	},
	
	// 幻灯片模块输出接口 /@ a -> feature
	ppt : function( a ) {
		var b = a, fo, id = 'ppt_' + b.id;
		b.w = this.m( b.width );
		b.h = this.m( b.height );
		var focus_width  = b.w,
			focus_height = b.h;
			swf_height   = focus_height;
		if ( ! b.face )
			b.face = 'ppt5';
		if ( b.face == 'ppt1' ) {			
			fo = this.swf( this.url( "fpd/swf/ppt1.swf" ), id, focus_width, swf_height, 7 );
			fo.addVariable("pics",  b.pics.join( '###' ) );
			fo.addVariable("links", b.links.join( '###' ) );
			fo.addVariable("texts", b.texts.join( '###' ) ); 
			fo.addVariable("descripts", b.descs.join( '###' ) );
			fo.addVariable("borderwidth", focus_width);
			fo.addVariable("borderheight", focus_height);
			fo.addVariable("border_color", "#666"); 
			fo.addVariable("is_border", "");
			fo.addVariable("is_text", "1");
			fo.addParam("wmode", "opaque");
		} else if( b.face == 'ppt2' ){
			var text_height=20;    //标题栏的宽度
			var swf_height = focus_height + text_height;		
			var fo = this.swf( this.url( "fpd/swf/ppt5.swf" ), id, focus_width, swf_height );
			fo.addParam("quality", "high");//高质量播放
			fo.addParam("wmode", "opaque");
			fo.addVariable("pics",  b.pics.join( '|' ));
			fo.addVariable("links", b.links.join( '|' ));
			fo.addVariable("texts", b.texts.join( '|' )); 
			fo.addVariable("borderwidth", focus_width);
			fo.addVariable("borderheight", focus_height);
			fo.addVariable("textheight", text_height);
			fo.addVariable("bgcolor", "#F0F0F0"); 
			fo.addVariable("width", focus_width);
			fo.addVariable("height", focus_height);
	 	}  else if( b.face == 'ppt3' ) {
			var swf_height   = focus_height + 0;			
			var fo = this.swf( this.url( "fpd/swf/ppt4.swf" ), id, focus_width, swf_height );
			fo.addParam("quality", "high");//高质量播放
			fo.addParam("wmode", "transparent");//透明方式
			fo.addVariable("ppurl",   b.pics.join( '|' ) + '|'  );
			fo.addVariable("pplink",  b.links.join( '|' ) + '|'  );
			fo.addVariable("ppfinfo", b.texts.join( '|' ) + '|'  );			
		} else if( b.face == 'ppt4' ) {
			var fo = this.swf( this.url( "fpd/swf/sohu.swf" ), id, focus_width, swf_height );
			fo.addParam("quality", "high");//高质量播放
			fo.addParam("wmode", "opaque");
			fo.addVariable("p", b.pics.join( '|' ));
			fo.addVariable("l", b.links.join( '|' ));
			fo.addVariable("icon", b.texts.join( '|' )); 
			fo.addVariable("speed", "4000");
			fo.addVariable("bgcolor", "#F0F0F0"); 
			fo.addVariable("width", focus_width);
			fo.addVariable("height", focus_height);			
		} else if( b.face == 'ppt5' ) {
			var fo = this.swf( this.url( "fpd/swf/minhou.swf" ), id, focus_width, swf_height );
			fo.addParam("quality", "high");//高质量播放
			fo.addParam("wmode", "opaque");
			fo.addVariable("imgs", b.pics.join( '|' ));
			fo.addVariable("urls", b.links.join( '|' ));
			fo.addVariable("titles", b.texts.join( '|' )); 
			fo.addVariable("pw", focus_width);
			fo.addVariable("ph", focus_height);
			fo.addVariable("Times", 4000 );
			fo.addVariable("sizes", 14 );
			fo.addVariable("isbold", 'yes' );
			fo.addVariable("umcolor", '16777215' );
			fo.addVariable("bgnub", '6710886' );
			fo.addVariable("btnbg", '4228856' );
			fo.addVariable("hovercolor", '16777215' );
			fo.addVariable("txtcolor", '16777215' );
			fo.addVariable("txtLeft", 4 );
			fo.addVariable("nubtouming", 100 );
			fo.addVariable("hovertouming", 100 );
			//fo.addVariable("rname", '榕基软件' );
			//fo.addVariable("rlink", 'http://www.i-task.com.cn' );
		} else if( b.face == 'ppt6' ) {
		 	var focus_width  = 540 ;//b.w;
			var focus_height = 360;//b.h;
			var swf_height = focus_height+0;
			var fo = this.swf( this.url( "fpd/swf/540x360.swf" ), id, focus_width, swf_height );
			fo.addParam("quality", "high");//高质量播放
			fo.addParam("wmode", "opaque");
			fo.addVariable("bigPicURL",   b.pics.join( '[|]' ));
			fo.addVariable("smallPicURL", b.thumbs.join( '[|]' ));
			fo.addVariable("PicURL",      b.links.join( '[|]' ));
			fo.addVariable("t",           b.texts.join( '[|]' )); 
			fo.addVariable("describe",    b.descs.join( '[|]' )); 
			fo.addVariable("borderwidth", focus_width);
			fo.addVariable("borderheight", focus_height);
			fo.addVariable("textheight", text_height);
			fo.addVariable("bgcolor", "#F0F0F0"); 
			fo.addVariable("width", focus_width);
			fo.addVariable("height", focus_height);
		} else if( b.face == 'ppt7' ) {
			var fo = this.swf( this.url( "fpd/swf/pz1.swf" ), id, focus_width, swf_height, '5', '#ffffff' );
			fo.addParam( "quality", "high" );
			fo.addParam( "wmode", "opaque" );
			fo.addParam( "salign", "t" );
			fo.addVariable( "b", b.pics.join( '|' ) );
			fo.addVariable( "p", b.thumbs.join( '|' ) );	
			fo.addVariable( "l", b.links.join( '|' ) );
			fo.addVariable( "icon",  b.texts.join( '|' ) );	
			fo.addVariable( "icon2", b.descs.join( '|' ) );
		}
		return '<div align=center>' + fo.getSWFHTML() + '</div>';
	},
	adtimer  : {},
	doublead : function( a ) {
		var lastScrollY = 0;
		function heartBeat() {
			var diffY	= document.documentElement.scrollTop,
				percent = .1 * ( diffY - lastScrollY ); 
			if ( percent > 0 ) percent = Math.ceil( percent ); 
			else percent = Math.floor( percent );
			document.getElementById(a.adid + '-1').style.top = parseInt(document.getElementById
			(a.adid + '-1').style.top)+percent+"px";
			document.getElementById(a.adid + '-2').style.top = parseInt(document.getElementById
			(a.adid + '-2').style.top)+percent+"px";
			lastScrollY=lastScrollY+percent; 
		}
		document.write( '<div id="' + a.adid + '-1" style="position:absolute;left:0px;top:' + a.top + 'px;"' + ( a.title ? ' title="' + a.title + '"' : '' ) + '><a' + ( a.link ? ' href="' + a.link + '" target=_blank' : '' ) + '><img src=' + a.url + '></a><br><a href=javascript:void(0) onclick=CmsTpl.doubleadclose("' + a.adid + '")><img src=' + Cfg.path + 'fpd/g/adclose.png></a></div>' +
			'<div id="' + a.adid + '-2" style="position:absolute;right:0px;top:' + a.top + 'px;text-align:right"' + ( a.title ? ' title="' + a.title + '"' : '' ) + '><a' + ( a.link ? ' href="' + a.link + '" target=_blank' : '' ) + '><img src=' + a.url + '></a><br><a href=javascript:void(0) onclick=CmsTpl.doubleadclose("' + a.adid + '")><img src=' + Cfg.path + 'fpd/g/adclose.png></a></div>' );
		if ( a.scroll == 'scroll' ) {
			this.adtimer[ a.adid ] = setInterval( heartBeat, 30 );
		}
	},
	doubleadclose : function( a ) {
		clearInterval( this.adtimer[ a ] );
		this.$( a + '-1' ).parentNode.removeChild( this.$( a + '-1' ) );
		this.$( a + '-2' ).parentNode.removeChild( this.$( a + '-2' ) );
	},
	df_jscr : function( a ) {
		var b = 'loc/' + ( Cfg.lang || 'zh_CN' ) + '.js',
			t = Cfg.php ? [ b, 'fn/t/dialog.js', 'fn/t/menu.js', 'fn/div.js', 'fn/tree.js', 'fn/oper.js', 'fn/xbox.js', 'vm/vm.js', 'vm/lb.js', 'vm/mx.js', 'vm/sh.js' ] : [ b, 'main.js?v=' + Cfg.ver ];
		for ( var i = 0; i < t.length; i ++ )
			t[ i ] = ( a ? '<script src="' : '' ) + Cfg.path + 'js/' + t[ i ] + ( a ? '"></script>' : '' );
		return t;
	},
	importDfish : function( a ) {
		if ( window.VM ) {
			a();
		} else {
			Ajax.send( Cfg.path + 'js/base.js', function( s ) {
				window.execScript ? window.execScript( s ) : window.eval( s );
				Ajax.loadjs( CmsTpl.df_jscr(), a );
			} );
		}
	},
	dynform : function( a ) {
		var b = this.eid( 'dyn-', 7 );
		document.write( '<div id=' + b + '></div>' );
		var c = this.$( b ).offsetWidth;
		this.importDfish( function() {
			VM.start( 'vm:|' + Cfg.path + a.url, CmsTpl.$( b ), c ? c - 2 : 700, -1 );
		} );
	}
	
}

if ( Br.fox && ! window.emulateEventHandlers ) {
	(function() {
		var eventNames = ["click", "dblclick", "mouseover", "mouseout",
							"mousedown", "mouseup", "mousemove", "input",
							"keydown", "keypress", "keyup", "dragstart"];
		for (var i = 0; i < eventNames.length; i++) {
			document.addEventListener(eventNames[i], function (e) {
				window.event = e;
			}, true);	// using capture
		}
		HTMLElement.prototype.contains = function (oEl) {
			if (oEl == this) return true;
			if (oEl == null) return false;
			return this.contains(oEl.parentNode);
		};
	})();
}