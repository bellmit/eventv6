package cn.ffcs.zhsq.utils.domain;

import javax.servlet.http.HttpSession;

public interface IDomain {
	
	public static int LOCAL = 0;
	
	public static int REMOTE = 1;

	public String getDebugUrl();
	
	public boolean isDebug();
	
	public String getCode();
	
	public String getDomain(HttpSession session);
	
	public int getType();
}
