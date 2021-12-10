//action：表格样式装饰
//author:gbw
var trBgColor ="";
function list_decorate(objId,show_seq_no)
{  
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for (var i = 1; i < obj.rows.length; i++)
		{
			if ( i % 2 == 0 )
			var	trBgColor = "#D0DEF0";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
			obj.rows(i).cells[1].innerText = i;
			obj.rows(i).bgColor = trBgColor;
		} 
		if (i>1)
	    {
		obj.rows(1).click();		
		oldObjTr_c = "#ffffff";
		curObjTr = obj.rows(1);		
	    for (c = 0; c < curObjTr.cells.length; c++) {
           //curObjTr.cells[c].style.backgroundColor = "#C1E78A";
           curObjTr.cells[c].style.backgroundColor = "#B2D8F4";
		}
	}
	}else{
		for (var i = 1; i < obj.rows.length; i++)
		{
			if ( i % 2 == 0 )
			var	trBgColor = "#D0DEF0";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
			obj.rows[i].cells[1].textContent = i;
			obj.rows[i].bgColor = trBgColor;
		}
		if (i>1)
	    {
		obj.rows[1].click();		
		oldObjTr_c = "#ffffff";
		curObjTr = obj.rows[1];		
	    for (c = 0; c < curObjTr.cells.length; c++) {
           //curObjTr.cells[c].style.backgroundColor = "#C1E78A";
           curObjTr.cells[c].style.backgroundColor = "#B2D8F4";
		}
	}
	}
	
}


function list_decorate_All(objId,show_seq_no)
{
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for ( var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
				//trBgColor = "#E4F5CB";
			var	trBgColor="#D0DEF0"
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows(i).cells[1].innerText = i+1;
			obj.rows(i).bgColor = trBgColor;
		}
		if (i>0)
		{
			obj.rows(1).click();		
			oldObjTr_c = "#ffffff";
			curObjTr = obj.rows(1);		
		    for (c = 0; c < curObjTr.cells.length; c++) {
	        	curObjTr.cells[c].style.backgroundColor = "#8CC4EC";
			}
		}
	}else{
	    for ( var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
				//trBgColor = "#E4F5CB";
			var	trBgColor="#D0DEF0"
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows[i].cells[1].textContent = i+1;
			obj.rows[i].bgColor = trBgColor;
		}
		if (i>0)
		{
			obj.rows[1].click();		
			oldObjTr_c = "#ffffff";
			curObjTr = obj.rows[1];		
		    for (c = 0; c < curObjTr.cells.length; c++) {
	        	curObjTr.cells[c].style.backgroundColor = "#8CC4EC";
			}
		}
	}
}

function list_decorate_scroll(objId,show_seq_no)
{
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for ( var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
				//trBgColor = "#8CC4EC";
				trBgColor = "#ffffff";
			else
				trBgColor = "#ffffff";
			if ( show_seq_no )
				if(obj.rows(i).cells[1].firstChild!=null&&obj.rows(i).cells[1].firstChild.innerText!=null){
					obj.rows(i).cells[1].firstChild.innerText = i+1;
				}else{
					obj.rows(i).cells[1].innerText = i+1; //orderby jiangzhw <td> --> <span>
				}
			obj.rows(i).bgColor = trBgColor;
		}
	}else{
	    for ( var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
			var	trBgColor = "#8CC4EC";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows[i].cells[1].firstChild.textContent = i+1;
				if(obj.rows(i).cells[1].firstChild!=null&&obj.rows(i).cells[1].firstChild.textContent!=null){
					obj.rows(i).cells[1].textContent.innerText = i+1;
				}else{
					obj.rows[i].cells[1].textContent = i+1; //orderby jiangzhw <td> --> <span>
				}
			obj.rows[i].bgColor = trBgColor;
		}
	}
}

function list_scroll(objId,show_seq_no)
{
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for ( var i = 0; i < obj.rows.length; i++)
     	{
			if ( i % 2 == 1 )
				trBgColor = "#ffffff";
			else
				trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows(i).cells[1].innerText = i+1;
			obj.rows(i).bgColor = trBgColor;
	   }
	}else{
	    for ( var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
			var	trBgColor = "#ffffff";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows[i].cells[1].textContent = i+1;
			obj.rows[i].bgColor = trBgColor;
		}
	}
	
	
}

function list_decorate_common(objId,show_seq_no)
{
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for (var i = 1; i < obj.rows.length; i++)
		{
			if ( i % 2 == 0 )
				trBgColor = "#ffffff";
			else
				trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows(i).cells[1].innerText = i;
			obj.rows(i).bgColor = trBgColor;
		} 
	}else{
    	for (var i = 1; i < obj.rows.length; i++)
		{
			if ( i % 2 == 0 )
				trBgColor = "#ffffff";
			else
				trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows[i].cells[1].textContent = i;
			obj.rowsi[i].bgColor = trBgColor;
		} 
	}
}

function list_decorate_all_common(objId,show_seq_no)
{
	var obj = document.getElementById(objId);
	if(getNavi()=="MSIE"){
		for (var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
			var	trBgColor = "#D0DEF0";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows(i).cells[1].innerText = i;
			obj.rows(i).bgColor = trBgColor;
		} 
	}else{
	    for (var i = 0; i < obj.rows.length; i++)
		{
			if ( i % 2 == 1 )
			var	trBgColor = "#D0DEF0";
			else
			var	trBgColor = "#ffffff";
			if ( show_seq_no )
				obj.rows[i].cells[1].textContent = i;
			obj.rows[i].bgColor = trBgColor;
		} 
	}
}

function row_onclick()
{
var _evt=getEvent();
var _e=_evt.srcElement || _evt.target; 



if(getNavi()=="MSIE"){
    if (_e.parentElement.tagName=="TR")
    {
    	setPointer(_e.parentElement,_e.parentElement.style.backgroundColor);
	}else if(_e.parentElement.tagName=="TD"){
		setPointer(_e.parentElement.parentElement,_e.parentElement.parentElement.style.backgroundColor);
	}
    }else{
    if (_e.parentNode.nodeName=="TR")
    {
    	setPointer(_e.parentNode,_e.parentNode.style.backgroundColor);
	}else if(_e.parentNode.nodeName=="TD"){
		setPointer(_e.parentNode.parentNode,_e.parentNode.parentNode.style.backgroundColor);
	}
 }	
}
var curObjTr= null;
var oldObjTr_c= "";

function setPointer(theRow,theDefaultColor)
{
	var theCells = null;
	 theCells=theRow.cells;
		if(curObjTr!=null)
		{
			var theOldTr=null;
			theOldTr=curObjTr.cells;
			for (c = 0; c < theOldTr.length; c++) {
			  theOldTr[c].style.backgroundColor = oldObjTr_c;
	            }
		}
	curObjTr=theRow;
	oldObjTr_c=theDefaultColor;
	for (c = 0; c < theCells.length; c++) {
         theCells[c].style.backgroundColor = "#acccfe";
	
	}
}

function list_decorate_ajax(objId)
{
	var obj = document.getElementById(objId);
	for (i = 1; i < obj.rows.length; i++)
	{
		if ( i % 2 == 1 )
			trBgColor = "#D0DEF0";
		else
			trBgColor = "#ffffff"
		obj.rows[i].bgColor = trBgColor;
	}
}

function resize_div()
{
   try{
	  document.getElementById("contentscroll").style.height =  document.body.clientHeight- document.getElementById('toolbar_table').style.height;
    }catch(e)
    {
     }
}

//判定浏览器类型 lpq
function getNavi()
{
   if(navigator.userAgent.indexOf("MSIE")>0) {
        return "MSIE";
   }
   if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
        return "Firefox";
   }
   if(isSafari=navigator.userAgent.indexOf("Safari")>0) {
        return "Safari";
   } 
   if(isCamino=navigator.userAgent.indexOf("Camino")>0){
        return "Camino";
   }
   if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){
        return "Gecko";
   }  
}



function getEvent(){     //同时兼容ie和ff的写法　　取得操作对像 lpq
         if(document.all)    return window.event;        
         func=getEvent.caller;            
         while(func!=null){    
             var arg0=func.arguments[0];
             if(arg0){
                 if((arg0.constructor==Event || arg0.constructor ==MouseEvent)
                     || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){    
                     return arg0;
                 }
             }
             func=func.caller;
         }
         return null;
 }
