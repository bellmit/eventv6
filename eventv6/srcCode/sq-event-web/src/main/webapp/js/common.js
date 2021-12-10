//导航切换
function jeeNav(o,n){
	 o.className="selected";
	 var t;
	 var id;
	 var s;
	 for(var i=1;i<=n;i++){
	   id ="nav"+i;
	   t = document.getElementById(id);
	   s = document.getElementById("sub"+i);
	   if(id != o.id){
	   	 t.className="hide";
	   	 s.style.display = "none";
	   }
	   else{
			s.style.display = "block";
	   }
	 }
}

//投票验证
/*function check_votes(allowCount) {
var voteItems=document.getElementsByName('itemIds');
  var count = 0;
  for(var i=0;i<voteItems.length;i++)
  {
   if(voteItems[i].checked){
     count++;
   }
  }
  if(count==allowCount&&allowCount>1){
   for(var i=0;i<voteItems.length;i++){
     if(!voteItems[i].checked){
       voteItems[i].disabled = true;
     }
   }
   return true;
  }
  else{
    for(var i=0;i<voteItems.length;i++){
       voteItems[i].disabled = false;
    }
  }
  if(count==0){
	  alert("对不起，请至少选择一个投票项！");
	  return false;0
  }
  return true;
}*/
//投票验证
function check_votes(allowCount,vid,obj) {
var voteItems=document.getElementsByName('itemIds'+'_'+vid);
  var count = 0;
	  for(var i=0;i<voteItems.length;i++)
	  {
	   if(voteItems[i].checked){
	     count++;
	   }
	  }
	  /**
	  *
	  *投票后返回再次点击投票时如已达到选票的限制count减一  所点击的复选框取消选择
	  */
	  if(count>allowCount){obj.checked=false; count--;}
  if(count==allowCount&&allowCount>1){
   for(var i=0;i<voteItems.length;i++){
     if(!voteItems[i].checked){
       voteItems[i].disabled = true;
     }
   }
   return true;
  }
  else{
    for(var i=0;i<voteItems.length;i++){
       voteItems[i].disabled = false;
    }
  }
//  if(count==0){
//	  alert("对不起，请至少选择一个投票项！");
//	  return false;0
//  }
  return true;
}

//投票验证
function check_votesSel() {
var el = document.getElementsByTagName('input');
				var len = el.length;
				var count = 0;
				for(var i=0; i<len; i++){
					if((el[i].type=="checkbox") && el[i].checked == true){
						count++;
					}
					if((el[i].type=="radio") && el[i].checked == true){
						count++;
					}
				}
  if(count==0){
	  alert("对不起，请至少选择一个投票项！");
	  return false;
  }		
  return true;
}

// 字体大小
function setFontSize(size,lineheight) {
    //document.body.style.fontSize=size;
    var a = document.getElementById("content");
    //var b = a.childNodes;
    var b = a.getElementsByTagName("DIV");
    a.style.fontSize = size;
	a.style.lineHeight = lineheight;
    for (var i = 0; i < b.length; i++) {
        b[i].style.fontSize = size;
		b[i].style.lineHeight = lineheight;
    }
}