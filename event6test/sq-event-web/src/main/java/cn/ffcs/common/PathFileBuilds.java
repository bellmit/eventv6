package cn.ffcs.common;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 补丁文件自动生成类
 * 
 * @author ldg by Sep 09, 2009 / modify Dec 16, 2009
 * 
 * 
 * 使用说明: 由于目前没做成GUI版本,只能在命令行运行. <br>
 * 补丁列表里的路径如:/fjweb/WebRoot/WEB-INF/pages/member/memberlevelater/schmbrlevelalter.jsp
 * 或:/fjweb/src/com/doone/common/action/CustInfoCollection.java
 * 
 * 
 * 把要打的补丁列表拷贝到 G:\\补丁列表.txt 文件中并保存, <br>
 * 运行的结果放在 G:\\补丁 目录下
 * 
 */
public class PathFileBuilds {

	// 以下要修改的变量值
//	public static final String ROOTPATH = "D:\\Workspaces\\MyEclipse8.5";// 根目录(要修改:工程所在的目录)
	public static final String PROJECT_NAME_S = "sq_zhsq_event";// 工程名(要修改:工程名称)
	public static final String PROJECT_NAME_P = "sq_zhsq_event";// 工程名(要修改:工程名称)
	public static final String TOMCATEPATH = "D:\\javaWebSoft\\tomcat6-1";//S端部署tomcat路径
	public static final String WEB_ROOT_NAME = "webapps\\"+PROJECT_NAME_S;// webRoot名称(默认为WebRoot)
	public static final String SOUR_FILE_PATH = "D:\\javaWebSoft\\workspace3\\补丁\\补丁列表.txt";// 补丁列表(要修改:补丁列表文件)
	public static final String DES_PATH_S = "D:\\javaWebSoft\\workspace3\\补丁\\zhsq_event\\WebRoot";// 目的地目录(要修改:生成的补丁文件所放的目录)

//	lineString = TOMCATEPATH + SEPARATOR + WEB_ROOT_NAME + FILE_PATH + lineString;
	// 以下不需要修改
	public static final String SEPARATOR = File.separator;// 路径符号
	// 运行工程的目录： "\\WEB-INF\\classes\\"
	public static final String FILE_PATH = SEPARATOR + "WEB-INF" + SEPARATOR + "classes" + SEPARATOR;

	public List lineList = new ArrayList();// 取出的行放入用于判断是否有重复行
	public int totalRow = 0;// 总行数
	public int emptyLines = 0;// 总空行数
	public int repeatTotal = 0;// 重复总行数

	public int totalFile = 0;// 总文件数
	public int totalFileSuccess = 0;// 拷贝成功总数
	public int totalFileSuccess_P = 0;// 拷贝到P端成功总数
	public int totalFileFail = 0;// 拷贝失败总数
	public int totalFileFail_P = 0;// 拷贝失败总数
	public int totalInnerFileSuccess = 0;// 内部类文件拷贝成功总数
	public int totalInnerFileFail = 0;// 内部类文件拷贝失败总数

	public boolean readLines(String filePath) {
		if (filePath == null || "".equals(filePath)) {
			return false;
		}
		System.out.println("源文件名:  " + filePath);
		filePath = myReplace(filePath);// 把路径符替换

		File file = new File(filePath);
		if (file == null) {
			return false;
		}

		try {
			String lineStr = null;
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// 取出一行
			while ((lineStr = br.readLine()) != null) {
				totalRow++;
				lineStr = lineStr.trim();
				if ("".equals(lineStr)) {
					emptyLines++;
					continue;
				}
				boolean finded = false;
				for (int i = 0; i < lineList.size(); i++) {
					if (lineList.get(i).toString().equals(lineStr)) {// 判断是否有重复行
						repeatTotal++;
						finded = true;
						System.out.println("=======重复行:" + lineStr);
						break;
					}
				}
				if (!finded) {
					lineList.add(lineStr);
					build(lineStr);
				}
				if (lineList.size() == 0) {
					lineList.add(lineStr);
					build(lineStr);
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("------------------统计信息-----------------");
		System.out.println("A:总行数:" + totalRow);
		System.out.println("B:总空行数:" + emptyLines);
		System.out.println("C:重复总行数:" + repeatTotal);
		System.out.println("D:有效行数:" + (totalRow - emptyLines - repeatTotal));
		System.out.println("E:总文件数:" + totalFile);
		System.out.println("F:普通文件拷贝成功>>>总数:" + totalFileSuccess);
		System.out.println("F:普通文件P端拷贝成功>>>总数:" + totalFileSuccess_P);
		System.out.println("G:普通文件拷贝失败<<<总数:" + totalFileFail);
		System.out.println("H:内部类文件拷贝成功总数:" + totalInnerFileSuccess);
		System.out.println("I:内部类文件拷贝失败总数:" + totalInnerFileFail);
		System.out.println("J:文件拷贝>>>>>>>>>成功总数:" + (totalFileSuccess + totalInnerFileSuccess));
		System.out.println("K:文件拷贝<<<<<<<<<失败总数:" + (totalFileFail + totalInnerFileFail));
		if (totalFile == totalFileSuccess && totalInnerFileFail == 0 && totalFileFail == 0 && (totalRow - emptyLines - repeatTotal) != 0) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>成功");
		} else {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<失败");
		}
		System.out.println("----------------统计信息说明---------------");
		// System.out.println("说明:" + "A=B+C+D , D=A-B-C , E=F+G+H+I");
		System.out.println("所有文件拷贝成功标志:" + "E=F 且 G=0,I=0");

		return true;
	}

	public boolean build(String lineString) {
		System.out.println("--123--"+lineString);
		String fullPath = lineString;
		if (lineString == null || "".equals(lineString)) {
			emptyLines++;
			return false;
		}
		totalFile++;

		lineString = myReplace(lineString);
		String primaryLineString = lineString;

		String porjectName = SEPARATOR + PROJECT_NAME_S + SEPARATOR;// 取工程名称
		String path = "";// 要创建的文件路径

		
		if(lineString.indexOf("WebRoot") > 0){
			// jsp,js,html,jpeg 等等在页面目录(WebRoot)下的文件
			int index2 = lineString.indexOf(porjectName);
			if (primaryLineString.indexOf(porjectName) < 0) {
				totalFileFail++;
				System.out.println(">>>>>>>>>>>>>>>不是有效的" + PROJECT_NAME_S + "工程文件,拷贝失败:" + primaryLineString);
				return false;
			}
			System.out.println("1+"+lineString);
			String filepath = lineString;
			filepath = filepath.substring(index2 + porjectName.length());
			if (primaryLineString.indexOf(SEPARATOR + PROJECT_NAME_S + SEPARATOR) < 0) {
				totalFileFail++;
				System.out.println(">>>>>>>>>>>>>>>不是有效的" + WEB_ROOT_NAME + "工程文件,拷贝失败:" + primaryLineString);
				return false;
			}
			int index3 = filepath.indexOf(SEPARATOR);
			path = filepath.substring(index3,filepath.length());
			path = path.substring(0,path.lastIndexOf(SEPARATOR));
			if (copyFile(lineString, DES_PATH_S + path)) {
				totalFileSuccess++;
				System.out.println("文件拷贝成功:" + lineString);
			} else {
				totalFileFail++;
				System.out.println("+++++++++++1+++++++++++>>文件拷贝失败:" + lineString);
			}
		}else{
			//类文件或是配置文件
			if (lineString.indexOf("src") > 0) {
				// 类文件:
				lineString = lineString.replaceAll("java", "class");
				if (lineString.indexOf(porjectName) < 0) {
					totalFileFail++;
					System.out.println(">>>>>>>>>>>>>>>不是有效的" + PROJECT_NAME_S + "工程文件,拷贝失败:" + lineString);
					return false;
				}
				int index = lineString.indexOf("src");
				lineString = lineString.substring(index + 4);
				// 取出要创建的文件路径
				int i = 0;
				if (lineString.lastIndexOf(SEPARATOR) > 0) {// 如是路径则截取
					i = lineString.lastIndexOf(SEPARATOR);
					path = FILE_PATH + lineString.substring(0, i);
				} else {
					path = FILE_PATH;
				}
				
				String fileFullName = lineString.substring(i + 1);// 取出文件名(含扩展名)
				if (fileFullName == null || "".equals(fileFullName) || fileFullName.indexOf(".") < 0) {
					totalFileFail++;
					System.out.println(">>>>>>>>>>>>>>>不是有效的文件,拷贝失败:" + primaryLineString);
					return false;
				}
				String fileName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
				File file = new File(TOMCATEPATH + SEPARATOR + WEB_ROOT_NAME + path);
				int boindex = fullPath.indexOf("/bo/");
				int apiindex = fullPath.indexOf("/api/");
				lineString = TOMCATEPATH + SEPARATOR + WEB_ROOT_NAME + FILE_PATH + lineString;
				
				if (copyFile(lineString, DES_PATH_S + path)) {
					totalFileSuccess++;
					System.out.println("文件拷贝成功:" + lineString);
					if(boindex!=-1||apiindex!=-1){
						System.out.println("s端路径"+DES_PATH_S+path);
						String pathP = (DES_PATH_S+path).replace(PROJECT_NAME_S, PROJECT_NAME_P);
						System.out.println("P端路径"+pathP);
						if(copyFile(lineString, pathP)){
							totalFileSuccess_P++;
							System.out.println("成功拷贝文件到P端:" + lineString);
						}else{
							totalFileFail_P++;
							System.out.println("++++++++++++++2++++++++>>文件拷贝到P端失败:" + lineString);
							return false;
						}
					}
				} else {
					totalFileFail++;
					System.out.println("++++++++++++++2++++++++>>文件拷贝失败:" + lineString);
					return false;
				}
				/** ******************************************************* */
				
				String[] fileList = file.list();
				for (int j = 0; j < fileList.length; j++) {
					if (fileList[j].indexOf((fileName + "$")) > -1) {// 搜索是否含有fileName文件名的内部类
						System.out.println(fileName + "有内部类:" + fileList[j]);
						// 拷贝内部类
						String lineStringInnerClass = lineString.replace(fileFullName, fileList[j]);
						
						if (copyFile(lineStringInnerClass, DES_PATH_S + path)) {
							totalInnerFileSuccess++;
							System.out.println("内部类文件拷贝成功:" + lineStringInnerClass);
						} else {
							totalInnerFileFail++;
							System.out.println("++++++++++++++++++++++>内部类文件拷贝失败:" + lineStringInnerClass);
						}
					}
				}
				/** ******************************************************** */
				
			}else if(lineString.indexOf("resources") > 0){//配置文件
				System.out.println(lineString);
				String lastfilePath = lineString.substring((lineString.indexOf("resources")+"resources".length()+1),lineString.length());
				System.out.println(lastfilePath);
//				path = lastfilePath.substring(lastfilePath.indexOf(PROJECT_NAME_S)+PROJECT_NAME_S.length()+1,lastfilePath.length());
				System.out.println(lastfilePath.lastIndexOf(""));
				System.out.println(lastfilePath.lastIndexOf("\\"));
				if(lastfilePath.lastIndexOf("\\")>0) {
					lastfilePath = lastfilePath.substring(0,lastfilePath.lastIndexOf("\\"));
				}else{
					lastfilePath = "";
				}
				
				
				path = DES_PATH_S + FILE_PATH + lastfilePath;
				if (copyFile(lineString, path)) {
					totalFileSuccess++;
					System.out.println("文件拷贝成功:" + lineString);
				} else {
					totalFileFail++;
					System.out.println("++++++++++++3++++++++++>>文件拷贝失败:" + lineString);
					return false;
				}
				
			}
		}
			

		return true;
	}

	/**
	 * 拷贝文件到指定目录
	 * 
	 * @return
	 */
	public boolean copyFile(String filePath, String path) {
		if (filePath == null || "".equals(filePath) || path == null || "".equals(path)) {
			return false;
		}
		File fileIn = new File(filePath);
		if (!fileIn.exists()) {
			return false;
		}
		File fileOutPath = new File(path);
		fileOutPath.mkdirs();

		File fileOut = new File(path + SEPARATOR + fileIn.getName());

		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			byte b[] = new byte[1024 * 10];
			int bi = 0;
			fis = new FileInputStream(fileIn);
			fos = new FileOutputStream(fileOut);
			while ((bi = fis.read(b)) > 0) {
				fos.write(b, 0, bi);
				fos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			fis.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 把路径符替换成当前操作系统的路径符号
	 * 
	 * @param string
	 * @return
	 */
	public static String myReplace(String string) {
		if ("/".equals(SEPARATOR)) {
			string = string.replaceAll("\\", SEPARATOR + "/");
		}
		if ("\\".equals(SEPARATOR)) {
			string = string.replaceAll("/", SEPARATOR + "\\");
		}
		return string;

	}

	/**
	 * 入口函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		PathFileBuilds patchFileBuild = new PathFileBuilds();
		patchFileBuild.readLines(SOUR_FILE_PATH);
	}

}
