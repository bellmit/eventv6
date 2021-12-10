<html>
  <head>
     <title>简介</title>
  </head>
  <style type="text/css">
     body{text-align:center;}
     .mainDiv {
      margin:0 auto;
      width:88%;
      text-align:center;
     }
     .table1 .col1{text-align:left;padding: 3pt 0pt 3pt 0pt;}
     .table1 .col1_L{text-align:left;padding: 3pt 0pt 3pt 34pt;}
     .table1 .col2{text-align:left;padding-left:1pt;}
     .table2 .col1{font-size:10pt;}
  </style>
  <body>
    <div class="mainDiv">
      <div style="font-size:25pt;font-weight:bold;text-align:center;letter-spacing:1.5pt;padding:10pt 0pt 10pt 0pt;">
         ${e.name!''}等${e.massNum}人信访情况说明
      </div>
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1">
         <tr>
           <td colspan="4">
             <div style="font-size:16pt;line-height:28pt;text-align:left;letter-spacing:1.5pt;font-weight:bold;">
                一、基本情况
             </div>
           </td>
         </tr>
         <tr>
           <td width="100" class="col1_L">姓名：</td>
           <td class="col2" width="200">${e.name!''}</td>
           <td width="100" class="col1">性别：</td>
           <td class="col2"><#if e.sex=='M'>男<#else>女</#if></td>
         </tr>
         <tr>
           <td class="col1_L">民族：</td>
           <td class="col2">${e.nation!''}</td>
           <td class="col1">身份证号：</td>
           <td class="col2">${e.certNumber!''}</td>
         </tr>
         <tr>
           <td class="col1_L">现居地址：</td>
           <td class="col2">${e.address!''}</td>
           <td class="col1">户籍地：</td>
           <td class="col2">${e.houseHold!''}</td>
         </tr>
         <tr>
           <td class="col1_L">概况：</td>
           <td class="col2" colspan="3">${e.efact!''}</td>
         </tr>
         <tr>
           <td colspan="4">
             <div style="font-size:16pt;line-height:28pt;text-align:left;letter-spacing:1.5pt;font-weight:bold;">
                二、办理情况
             </div>
           </td>
         </tr>
         <tr>
           <td class="col1_L">上访时间：</td>
           <td class="col2" width="320">${e.visitDate!''}</td>
           <td class="col1">到访地点：</td>
           <td class="col2">${e.eplace!''}</td>
         </tr>
         <tr>
           <td class="col1_L">责任单位：</td>
           <td class="col2">${zeren!''}</td>
           <td class="col1">包案领导：</td>
           <td class="col2">${e.eoffical!''}</td>
         </tr>
         <tr>
           <td class="col1_L">稳控单位：</td>
           <td class="col2">${wenkong!''}</td>
           <td class="col1">责任人：</td>
           <td class="col2"></td>
         </tr>
         <tr>
           <td class="col1_L">是否正常访：</td>
           <td class="col2" colspan="3"><#if e.normal>是<#else>否</#if></td>
         </tr>
         <#if banliL?? && (banliL?size>0)>
         <tr>
           <td colspan="4" class="col1_L">
              处理情况（备注：按照时间先后顺序生成）：
           </td>
         </tr>
         <tr>
           <td colspan="4" class="col1_L">
             <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
               <tr style="border-top:1px solid #000000;">
                 <td>序列</td>
                 <td>转交办时间</td>
                 <td>去向</td>
                 <td>办理方式</td>
                 <td>接收状态</td>
                 <td>接收时间</td>
               </tr>
               <tr>
                 <td colspan="6" style="border-top:1px solid #000000;">&nbsp;</td>
               </tr>
               <#list banliL as c>
               <tr>
                 <td class="col1">${c_index+1}</td>
	             <td class="col1">${c.time1?datetime}</td>
	             <td class="col1">${c.quxiang!''}</td>
	             <td class="col1"><#if c.startType==2>转办<#else>交办</#if></td>
	             <td class="col1"><#if c.recv>已接收<#else>未接收</#if></td>
	             <td class="col1"><#if c.recv>${c.time2?datetime}</#if></td>
	           </tr>
               </#list>
             </table>
           </td>
         </tr>
         </#if>
         <#if fcL?? && (fcL?size>0)>
         <tr>
           <td colspan="4" class="col1_L">
              复查复核情况（备注：按照时间先后顺序生成）：
           </td>
         </tr>
         <tr>
           <td colspan="4" class="col1_L">
             <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
               <tr style="border-top:1px solid #000000;">
                 <td>申请时间</td>
                 <td>是否受理</td>
	             <td>申请事项</td>
	             <td>申请的事实和理由</td>
	             <td>牵头领导</td>
	             <td>是否听证</td>
	             <td>到期时间</td>
               </tr>
               <tr>
                 <td colspan="7" style="border-top:1px solid #000000;">&nbsp;</td>
               </tr>
               <#list fcL as c>
               <tr>
                 <td class="col1">${c.applyDate!''}</td>
	             <td class="col1"><#if c.accept>是<#else>否</#if></td>
	             <td class="col1">${c.apply1!''}</td>
	             <td class="col1">${c.apply2!''}</td> 
	             <td class="col1">${c.offical!''}</td>
	             <td class="col1"><#if c.hearing>是<#else>否</#if></td>
	             <td class="col1">${c.daoqiDate!''}</td>
	           </tr>
               </#list>
             </table>
           </td>
         </tr>
         </#if>
         <#if dubanL?? && (dubanL?size>0)>
         <tr>
           <td colspan="4">
             <div style="font-size:16pt;line-height:28pt;text-align:left;letter-spacing:1.5pt;font-weight:bold;">
                三、督办情况
             </div>
           </td>
         </tr>
         <tr>
           <td colspan="4" class="col1_L">
             <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
               <tr style="border-top:1px solid #000000;">
                 <td>督办形式</td>
	             <td>督办时间</td>
	             <td>参加人员</td>
	             <td>地点</td>
	             <td>见面对象</td>
	             <td>相关情况</td> 
               </tr>
               <tr>
                 <td colspan="6" style="border-top:1px solid #000000;">&nbsp;</td>
               </tr>
               <#list dubanL as c>
               <tr>
                 <td class="col1">${c.type!''}</td>
	       		 <td class="col1">${c.createTime!''}</td>
	             <td class="col1">${c.attende!''}</td>
	             <td class="col1">${c.place!''}</td>
	             <td class="col1">${c.meetPerson!''}</td>
	             <td class="col1">${c.remark!''}</td>
	           </tr>
               </#list>
             </table>
           </td>
         </tr>
         </#if>
       </table>
    </div>
  </body>
</html>