<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<!-- saved from url=(0055)http://www.womai.com/adv.jsp -->
<HTML xmlns="http://www.w3.org/1999/xhtml"><HEAD><TITLE></TITLE>
<META content="text/html; charset=gb2312" http-equiv=Content-Type>
<STYLE type=text/css>BODY {
	POSITION: absolute; PADDING-BOTTOM: 0px; LINE-HEIGHT: 25px; MARGIN: 0px; PADDING-LEFT: 0px; WIDTH: 533px; PADDING-RIGHT: 0px; HEIGHT: 160px; PADDING-TOP: 0px
}
#lantern {
	WIDTH: 533px; HEIGHT: 160px; OVERFLOW: hidden; CURSOR: pointer
}
#lanternMain {
	BACKGROUND-COLOR: #868686; WIDTH: 533px; HEIGHT: 135px
}
#lanternNavy {
	TEXT-ALIGN: center; WIDTH: 1000px; FLOAT: left; HEIGHT: 25px; line-height:25px; COLOR: #868686; FONT-SIZE: 12px; OVERFLOW: hidden
}
#lanternNavy .div_off1 {
	 TEXT-ALIGN: center; BACKGROUND-COLOR: #f5f5f5; FLOAT: left; HEIGHT: 25px
}
#lanternNavy .div_on1 {
	 BACKGROUND-COLOR: #e8e8e8; PADDING-LEFT: 10px; PADDING-RIGHT: 25px; FLOAT: left; HEIGHT: 25px; color:#ff4800;
}
#lanternNavy .div_off2 {
	 TEXT-ALIGN: center; BORDER-LEFT: #d6d6d6 1px dashed; BACKGROUND-COLOR: #f5f5f5; FLOAT: left; HEIGHT: 25px
}
#lanternNavy .div_on2 {
	 BORDER-LEFT: #dddddd 1px dashed; BACKGROUND-COLOR: #e8e8e8; PADDING-LEFT: 10px; PADDING-RIGHT: 25px; FLOAT: left; HEIGHT: 25px; color:#ff4800;
}
#lanternNavy .div_off3 {
	 TEXT-ALIGN: center; BORDER-LEFT: #dddddd 1px dashed; BACKGROUND-COLOR: #f5f5f5; FLOAT: left; HEIGHT: 25px; BORDER-RIGHT: #fc0 1px dashed
}
#lanternNavy .div_on3 {
	 BORDER-LEFT: #dddddd 1px dashed; BACKGROUND-COLOR: #e8e8e8; PADDING-LEFT: 10px; PADDING-RIGHT: 25px; FLOAT: left; HEIGHT: 25px; BORDER-RIGHT: #dddddd 1px dashed; color:#ff4800;
}
#lanternImg {
	Z-INDEX:0; POSITION: absolute; WIDTH: 533px; HEIGHT: 135px; OVERFLOW: hidden
}
</STYLE>

<SCRIPT type=text/javascript>
//ff支持
function isIE(){ //ie? 
   if (window.navigator.userAgent.toLowerCase().indexOf("msie")>=1) 
    return true; 
   else 
    return false; 
} 

//首页幻灯
var Lantern={

    onChange:[],
    oInterval:[],
    otimeOut:[],
    opacityNum:101,
    cycNum:0,
    showNum:0,
    width:533,//整体宽度
    navyCtr:[],//2维:  0.原长 1.目标长 2.speed 
    navyTime:10,//navy动画时间
    picMoveSpeed:22,//图片移动速度
    timeOut_time:4500,//停滞时间
    info ://0.图片url 1.名称 2.链接地址 
    [],
    
    init: function(){
        Lantern.onChange=false;
        for(var i=0;i<Lantern.info.length;i++)
        {
            var picDiv
            var picTemp
            picDiv=document.createElement('div');
            picTemp=document.createElement('img');
	        picDiv.id ="LanternImg"+i;
            picDiv.name=i;
	        picTemp.src = Lantern.info[i][0]; //[图片url][标题名]
	        picTemp.style.width = "533px";
	        picDiv.style.position ="absolute";
	        picDiv.style.left =Lantern.width+"px";
	        picDiv.onclick=function(){window.open(Lantern.info[this.name][2]);};
	        
	        picDiv.onmousemove= function(){clearTimeout(Lantern.otimeOut)};//停止轮换
	        picDiv.onmouseout= function(){Lantern.otimeOut=setTimeout("Lantern.otimeOut=Lantern.cycLantern()",0);};
	        
	        picDiv.appendChild(picTemp);
	        document.getElementById("lanternImg").appendChild(picDiv);
	        var divTemp
	        divTemp=document.createElement('div');
	        divTemp.id ="LanternN"+i;
            divTemp.name=i;
	        divTemp.innerHTML="<strong>"+(i+1)+"</strong><span id=\"lanternNc"+i+"\" style=\"display:none;\">&nbsp;"+Lantern.info[i][1]+"</span>";
	        if(i==0)
            {
               divTemp.className ="div_off1";
            }
            else if(i==Lantern.info.length-1)
            {
                divTemp.className ="div_off3";//标题移动时，最右边闪过的右边框色
            }
            else
            {
                divTemp.className ="div_off2";//标题栏右边框
            }
	        //divTemp.className="div_off";
	        if(i==0)
	            divTemp.onclick=function(){window.open(Lantern.info[this.name][2]);};
	        else
	            divTemp.onclick=function(){if(!Lantern.onChange){Lantern.onChange=true;Lantern.setNavy(this.name);}};
	        document.getElementById("lanternNavy").appendChild(divTemp);
        }
        
        Lantern.initNany();
    },
    
    initNany:function(){
        navyCtr=new Array();
        for(var k=0;k<Lantern.info.length;k++)
            Lantern.navyCtr[k]=[];
        document.getElementById("lanternNc0").style.display ="";
        document.getElementById("LanternN0").className ="div_on1";
        var onLength,offLength
        onLength=document.getElementById("LanternN0").offsetWidth
        offLength=(Lantern.width-onLength)/(Lantern.info.length-1)
        var numtemp=0;
        for(var j=0;j<Lantern.info.length;j++)
        {
              if(j!=0)//未选
              {
                     Lantern.navyCtr[j][1]=offLength;
                     document.getElementById("lanternNc"+j).style.display ="none";
                    if(j==Lantern.info.length-1)
                    {
                        document.getElementById("LanternN"+j).className ="div_off3";
                    }
                    else
                    {
                        document.getElementById("LanternN"+j).className ="div_off2";
                    }
                     document.getElementById("LanternN"+j).style.width=offLength+"px";
                     if(j==Lantern.info.length-1) 
                     {
                        document.getElementById("LanternN"+j).style.width=(Lantern.width-onLength-numtemp)+"px";  
                     }
                     else
                     {
                        numtemp+=offLength;
                     }
              }
              else//已选
              {
                 Lantern.navyCtr[j][1]=onLength;
              }
        }
        if(isIE())
        document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN0").offsetLeft+10)+"px";
        else
        document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN0").offsetLeft-document.getElementById("lantern").offsetLeft+10)+"px";
        document.getElementById("LanternImg0").style.display ="";
        document.getElementById("LanternImg0").style.left ="0px";
        Lantern.otimeOut=setTimeout("Lantern.cycLantern()",Lantern.timeOut_time);
    },
    
    setNavy:function(i){
        if(i==Lantern.info.length-1)
             document.getElementById("lanternNavy").style.backgroundColor ="#ff6600";
        else
             document.getElementById("lanternNavy").style.backgroundColor ="#f5f5f5";
        document.getElementById("lanternNc"+i).style.display ="";
        if(i==0)
        {
            document.getElementById("LanternN"+i).className ="div_on1";
        }
        else if(i==Lantern.info.length-1)
        {
            document.getElementById("LanternN"+i).className ="div_on3";
        }
        else
        {
            document.getElementById("LanternN"+i).className ="div_on2";
        }
        document.getElementById("LanternN"+i).style.width=null;
        var onLength,offLength
        onLength=document.getElementById("LanternN"+i).offsetWidth
        offLength=(Lantern.width-onLength)/(Lantern.info.length-1)
        var numtemp=0;
        for(var j=0;j<Lantern.info.length;j++)
        {
              Lantern.navyCtr[j][0]=Lantern.navyCtr[j][1];
              if(i!=j)//未选
              {
                     Lantern.navyCtr[j][1]=offLength;
                     document.getElementById("lanternNc"+j).style.display ="none";
                       if(j==Lantern.info.length-1)
                        {
                            document.getElementById("LanternN"+j).className ="div_off3";
                        }
                        else
                        {
                            document.getElementById("LanternN"+j).className ="div_off2";
                        }
                     if(j==Lantern.info.length-1) 
                     {
                        document.getElementById("LanternN"+j).style.width=(Lantern.width-onLength-numtemp)+"px";
                     }
                     else
                     {
                        numtemp+=offLength
                     }
                     document.getElementById("LanternN"+j).style.width=Lantern.navyCtr[j][0]+"px";
              Lantern.navyCtr[j][2]=(Lantern.navyCtr[j][1]-Lantern.navyCtr[j][0])/Lantern.navyTime ;
              }
              else//已选
              {
                 Lantern.navyCtr[j][1]=onLength-5;
                 document.getElementById("LanternN"+j).style.width=Lantern.navyCtr[j][0]-34<=0?"0px":(Lantern.navyCtr[j][0]-34)+"px";                
                 Lantern.navyCtr[j][2]=(Lantern.navyCtr[j][1]-Lantern.navyCtr[j][0])/Lantern.navyTime ;
             
              }
        }
        document.getElementById("LanternImg"+i).style.display ="";
        if(Lantern.onChange)
        {
                document.getElementById("LanternN"+i).onclick=function(){window.open(Lantern.info[this.name][2]);};
                document.getElementById("LanternN"+Lantern.showNum).onclick=function(){if(!Lantern.onChange){Lantern.onChange=true;Lantern.setNavy(this.name);}};
                document.getElementById("LanternImg"+i).style.zIndex=0;
                document.getElementById("LanternImg"+Lantern.showNum).style.zIndex=-1;
                Lantern.oInterval=setInterval('Lantern.changeLantern('+i+')',10);
        }
    },
    
    imgMoveOver:false,
    navyMoveOver:false,
     changeLantern:function(i){
            if(Lantern.otimeOut!=null)
                clearTimeout(Lantern.otimeOut)
             //move
             if(!Lantern.navyMoveOver)
                Lantern.moveNavy(i);
             if(!Lantern.imgMoveOver)
             {
                Lantern.moveImg(i);
             }
             else
             {
                Lantern.flashImg(i);
             }
    },
    
     moveNavy:function(select){
            var breaktime=0;
            for(var i=0;i<Lantern.info.length;i++)
            {
                  if((Lantern.navyCtr[i][2]>0&&document.getElementById("LanternN"+i).offsetWidth<Lantern.navyCtr[i][1])||(Lantern.navyCtr[i][2]<0&&document.getElementById("LanternN"+i).offsetWidth>Lantern.navyCtr[i][1]))
                  {
                       if(i==select)
                       {
                            document.getElementById("LanternN"+i).style.width=(document.getElementById("LanternN"+i).offsetWidth+Lantern.navyCtr[i][2]-34)+"px";  
                       }
                       else
                       {
                            document.getElementById("LanternN"+i).style.width=(document.getElementById("LanternN"+i).offsetWidth+Lantern.navyCtr[i][2])+"px";  
                       }
                          
                  }
                  else
                  {
                      if(i==select)
                      {
                           for(var j=0;j<Lantern.info.length;j++)
                           {
                                document.getElementById("LanternN"+j).style.width=Lantern.navyCtr[j][1]+"px"; 
                           }
                           if(isIE())
                                document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN"+select).offsetLeft+10)+"px";
                           else
                                document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN"+select).offsetLeft-document.getElementById("lantern").offsetLeft+10)+"px";
                           Lantern.navyMoveOver=true;
                           break;
                  }
              }
               if(i==select)
               {
                
                   if(isIE())
                        document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN"+select).offsetLeft+10)+"px";
                   else
                        document.getElementById("lanternPoint").style.left=(document.getElementById("LanternN"+select).offsetLeft-document.getElementById("lantern").offsetLeft+10)+"px";
               }
            }
    },
    
     moveImg:function(i){
            if(document.getElementById("LanternImg"+i).offsetLeft>0)
            {
               document.getElementById("LanternImg"+i).style.left=(document.getElementById("LanternImg"+i).offsetLeft-Lantern.picMoveSpeed)+"px";
            }
            else
            {
                document.getElementById("LanternImg"+i).style.left="0px";
                document.getElementById("LanternImg"+Lantern.showNum).style.left=Lantern.width+"px";
                Lantern.imgMoveOver=true;
            }
    },
    
      flashImg:function(i){
             document.getElementById("LanternImg"+i).style.opacity="100"; 
                    Lantern.showNum=i;
                    Lantern.imgMoveOver=false;
                    Lantern.navyMoveOver=false;
                    Lantern.opacityNum=181;
                    Lantern.cycNum=i;
                    clearInterval(Lantern.oInterval);
                    Lantern.otimeOut=setTimeout("Lantern.otimeOut=Lantern.cycLantern()",Lantern.timeOut_time);
                    Lantern.onChange=false;
    },
      
    cycLantern:function(){
        if(!Lantern.onChange)
        {
            Lantern.onChange=true;
            if(Lantern.cycNum==Lantern.info.length-1)
                Lantern.cycNum=0;
            else
                Lantern.cycNum++;
           Lantern.setNavy(Lantern.cycNum)
        }
    }
    
}
</SCRIPT>

<META name=GENERATOR content="MSHTML 8.00.6001.18939"></HEAD>
<BODY>
<DIV id=lantern>
<DIV id=lanternMain>
<DIV id=lanternImg>
<DIV style="Z-INDEX: 100; BORDER-BOTTOM: #fff 1px solid; POSITION: absolute; WIDTH: 533px; HEIGHT: 2px; TOP: 221px">
<IMG style="POSITION: absolute" id=lanternPoint src="adv/lanternpoint.gif"> 
</DIV></DIV></DIV>
<DIV id=lanternNavy></DIV>
<SCRIPT type=text/javascript>
   Lantern.info=parent.document.lanterninfo();
   Lantern.init();
</SCRIPT>
</DIV></BODY></HTML>
