package cn.ffcs.zhsq.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class CommonJsServlet
 */
public class CommonJsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommonJsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		buildJs(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		buildJs(request, response);
	}

	private void buildJs(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String referer = request.getHeader("Referer");
		//System.out.println("Referer: " + referer);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		StringBuffer sb = new StringBuffer("");
		//sb.append("if(!window.top.frames['mainFrame'])");
		//sb.append("window.top.location.href='").append(request.getContextPath()).append("/system/index.jhtml';");
	    sb.append("try{window.top.frames['topFrame'].getObject('CFun').disabled = false;");
		sb.append("window.top.frames['topFrame'].getObject('tip').style.display='none';}catch(e){}");

        sb.append("try{");
        sb.append("    window.top.right_c.document.frames['topFrame'].getObject('CFun').disabled = false;");
        sb.append("    window.top.right_c.document.frames['topFrame'].getObject('tip').style.display='none'; ");
        sb.append("}catch(e){");
        sb.append(" try{window.top.right_c.contentWindow.frames['topFrame'].getObject('CFun').disabled = false;");
        sb.append("    window.top.right_c.contentWindow.frames['topFrame'].getObject('tip').style.display='none';");
        sb.append(" }catch(e){}");
        sb.append("}");
        writer.print(sb.toString());
		writer.close();
	}
}
