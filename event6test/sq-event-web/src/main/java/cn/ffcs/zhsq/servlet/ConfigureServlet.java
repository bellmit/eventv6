package cn.ffcs.zhsq.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ffcs.zhsq.utils.ConstantValue;

/**
 * Servlet implementation class ConfigureServlet
 */
public class ConfigureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfigureServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		loadConfig();
	}
	
	/**
	 * 加载配置
	 * @return
	 */
	public void loadConfig() {
		String classPath = this.getClass().getResource("/").getPath();
		System.out.println(classPath);
		if((new File(classPath)).exists()) {
			System.out.println("加载配置文件："+classPath+"---------------开始！");
			ConstantValue.init(classPath);
			System.out.println("加载配置文件：---------------结束！");
		} else {
			System.out.println(classPath+"---------------加载失败！");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loadConfig();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
