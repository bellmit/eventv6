<div class="paginationToolBar" style='right:0px;'>
	<table cellpadding="2" cellspacing="1" width="100%" border="0" class="listBtmView" style="margin-left:0px;">
		<tr>
			<td class="panelBar">
				<style>
				.panelBar1{ background:url(${rc.getContextPath()}/theme/scim/gis/query/img/btm_bg.png) repeat-x; width:260px; padding-bottom: 3px;}
				.btn_panel{ background:url(${rc.getContextPath()}/theme/scim/gis/query/img/btn_bg.png) repeat-x; vertical-align: middle; line-height:18px; border:1px solid #ddd; padding:2px 5px; margin-right: 3px;}
				.redsz{ color:#ff0000; font-weight:bold;}
				</style>
				<div class="panelBar1" style="width:100%;height:100%;">
					<!-- <input type="hidden" id="pageSize" value="20" />-->
					<!--<span class="btn_panel"><a href="###" onclick="page(0)">首页</a></span>
					<span class="btn_panel"><a href="###" onclick="page(-1)">上页</a></span>
					<span class="btn_panel"><a href="###" onclick="page(1)" >下页</a></span>
					<span class="btn_panel"><a href="###" onclick="page(-3)">尾页</a></span>
					<span style="margin-left: 3px;">第<span class="redsz" id="curPage"></span>/<span id="totalPage"></span>页</span>-->
					
					<TABLE border=0 cellSpacing=0 cellPadding=0 sizset="false" sizcache05563487786151169="241.1.1">
						<TBODY sizset="false" sizcache05563487786151169="241.1.1">
							<TR sizset="false" sizcache05563487786151169="241.1.1">
								<td sizset="false" sizcache03555538485480856="122.2.2">
									<#if orgCode??>
										<#if orgCode = "3501">
											<SELECT id="pageSize" class=pagination-page-list jQuery18007400522054311513="61" onChange="page(0)"><OPTION selected>300</OPTION><OPTION>500</OPTION><OPTION >1000</OPTION></SELECT>
										<#else>
											<SELECT id="pageSize" class=pagination-page-list jQuery18007400522054311513="61" onChange="page(0)"><OPTION selected>10</OPTION><OPTION>20</OPTION><OPTION >50</OPTION></SELECT>
										</#if>
									<#else>
										<SELECT id="pageSize" class=pagination-page-list jQuery18007400522054311513="61" onChange="page(0)"><OPTION selected>10</OPTION><OPTION>20</OPTION><OPTION >50</OPTION></SELECT>
									</#if>
								</td>
								<TD>
									<DIV class=pagination-btn-separator></DIV>
								</TD>
								<TD>
									<A class="l-btn l-btn-plain" href="javascript:page(0)" sizset="true" sizcache05563487786151169="256.0.2" jQuery180037435986275055144="64">
										<SPAN class=l-btn-left sizset="false" sizcache05563487786151169="256.0.2">
										<SPAN class=l-btn-text sizset="false" sizcache05563487786151169="256.0.2">
											<SPAN class="l-btn-empty pagination-first">&nbsp;</SPAN>
										</SPAN>
										</SPAN>
									</A>
								</TD>
								<TD>
									<A class="l-btn l-btn-plain" href="javascript:page(-1)" sizset="true" sizcache05563487786151169="257.0.2" jQuery180037435986275055144="65">
									<SPAN class=l-btn-left sizset="false" sizcache05563487786151169="257.0.2">
										<SPAN class=l-btn-text sizset="false" sizcache05563487786151169="257.0.2">
										<SPAN class="l-btn-empty pagination-prev">&nbsp;</SPAN>
										</SPAN>
									</SPAN>
									</A>
								</TD>
								<TD>
									<DIV class=pagination-btn-separator></DIV>
								</TD>
								<TD>
									<SPAN style="text-align:right;width:28px;"><span class="redsz" id="curPage">0</span></SPAN>
								</TD>
								<TD>
									<SPAN style="PADDING-LEFT: 0px">/</SPAN>
								</TD>
								<TD>
									<SPAN style="PADDING-RIGHT: 6px;width:32px;"><span id="totalPage">0</span></SPAN>
								</TD>
								<TD>
									<DIV class=pagination-btn-separator></DIV>
								</TD>
								<TD>
									<A class="l-btn l-btn-plain" href="javascript:page(1)"
										sizset="true" sizcache05563487786151169="258.0.2"
										jQuery180037435986275055144="67"><SPAN class=l-btn-left
										sizset="false" sizcache05563487786151169="258.0.2"><SPAN
											class=l-btn-text sizset="false"
											sizcache05563487786151169="258.0.2"><SPAN
												class="l-btn-empty pagination-next">&nbsp;</SPAN>
										</SPAN>
									</SPAN>
									</A>
								</TD>
								<TD>
									<A class="l-btn l-btn-plain" href="javascript:page(-3)"
										sizset="true" sizcache05563487786151169="259.0.2"
										jQuery180037435986275055144="68"><SPAN class=l-btn-left
										sizset="false" sizcache05563487786151169="259.0.2"><SPAN
											class=l-btn-text sizset="false"
											sizcache05563487786151169="259.0.2"><SPAN
												class="l-btn-empty pagination-last">&nbsp;</SPAN>
										</SPAN>
									</SPAN>
									</A>
								</TD>
								<TD>
									<DIV class=pagination-btn-separator></DIV>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</div>
			</td>
		</tr>
	</table>
	<#if footerType??>
		<#if footerType=="event">
			<style>
				.paginationToolBar {
					height:65px;
				}
			</style>
			<table cellpadding="2" cellspacing="1" width="100%" border="0" class="listBtmView">
				<tr>
					<td class="panelBar" colspan="8" style="padding-top:4px; padding-left:20px;">
						时限图示说明：<br/>
						<img width="15" height="15" src="${rc.getContextPath()}/theme/scim/images/time_normal.png" /> 正常
						<img width="15" height="15" src="${rc.getContextPath()}/theme/scim/images/time_to_expire.png" /> 将到期
						<img width="15" height="15" src="${rc.getContextPath()}/theme/scim/images/time_expired.png" /> 已过期
						
					</td>
				</tr>
			</table>
		</#if>
	</#if>
</div>